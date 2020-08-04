package nefrapp.restapi.controller;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import nefrapp.restapi.model.Amministratore;
import nefrapp.restapi.model.Medico;
import nefrapp.restapi.model.Paziente;
import nefrapp.restapi.model.Utente;
import nefrapp.restapi.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.security.sasl.AuthenticationException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Logger;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

//TODO: input sanitization dei metodi che prendono un pojo dalla request
// (annotazione @Valid nella firma del metodo e annotazioni @Value, @Min ecc. sui campi dell' oggetto entity)

@RestController
public class RestUserController {
    Logger log = Logger.getLogger("RestUserController");
    @Autowired
    UtenteRepository repo;
    @Autowired
    UtenteRepositoryCustom repoCustom;
    @Autowired
    PazienteRepository pazRepo;
    @Autowired
    AmministratoreRepository admRepo;
    @Autowired
    MedicoRepository medRepo;
    @Autowired
    private AuthenticationManager authenticationManager;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private Authentication authenticatedUser = SecurityContextHolder.getContext().getAuthentication();

    public RestUserController(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    /**
     * Ottiene un token di autenticazione JWT se le credenziali in input sono corrette
     *
     * @param authData Map di stringhe contenente i dati di autenticazione
     * @return
     * @throws AuthenticationException
     * @throws JsonProcessingException
     */
    @RequestMapping(value = "/auth", method = RequestMethod.POST)
    public @ResponseBody
    Utente createAuthenticationToken(@RequestBody Utente authData) {
        Authentication auth;
        Utente u = repo.findByCodiceFiscale(authData.getCodiceFiscale());

        //controlla se l'utente e' inesistente o gia' autenticato
        if (u == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nessun utente con questo CF");
        }
        if (u.getAutenticato() != null && u.getAutenticato() == true) {
            return u;
        }

        //crea una lista di granted authorities con i permessi dell'utente (prelevati dal database)
        SimpleGrantedAuthority s = new SimpleGrantedAuthority(u.getAuthorities());
        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(s);

        //crea un token coi dati dell'utente che l'authenticationManager autentichera',
        //usando l'implementazione concreta di UsersDetailsService presente nel package security:
        //essa contiene le informazione necessarie a Spring Security per ottenere i dati necessari
        //per autenticare l'utente
        UsernamePasswordAuthenticationToken t = new UsernamePasswordAuthenticationToken(
                authData.getCodiceFiscale(),
                authData.getPassword(),
                authorities);
        try {
            auth = authenticationManager.authenticate(t);
        } catch (org.springframework.security.core.AuthenticationException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Password errata", e);
        }

        //crea il token JWT, lo firma e aggiorna lo stato dell'utente nel database:
        //cancella la password dal DTO prima di restituirlo al client
        String token = JWT.create()
                .withSubject(((User) auth.getPrincipal()).getUsername())
                .withClaim("Authorities", auth.getAuthorities().toString())
                .withExpiresAt(new Date(System.currentTimeMillis() + 600000000))
                .sign(HMAC512("secret".getBytes())); //TODO: gestione chiave
        token = "Bearer/" + token;
        u.setAutenticato(true);
        u.setToken(token);
        repo.save(u);
        u.setPassword("");
        return u;
    }

    /**
     * Deautentica un utente loggato, aggiorna il suo stato e rimuove il suo token d'accesso dal database.
     *
     * @param data, Map nome-valore coi dati necessari alla richiesta (per ora contiene solo il codice fiscale dell'utente
     * da deautenticare)
     * @return HttpStatus (CREATED o BAD_REQUEST)
     */
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/deauth", method = RequestMethod.POST)
    public @ResponseBody
    HttpStatus deauthUtente (@RequestBody LinkedMultiValueMap<String, String> data) {
        String cf = data.getFirst("cf");
        Utente u = repo.findByCodiceFiscale(cf);
        if (u == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nessun utente con questo CF");
        }
        if (u.getAutenticato() == false) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Utente non autenticato");
        }

        u.setAutenticato(false);
        u.setToken(null);
        repo.save(u);
        return HttpStatus.OK;
    }

    /**
     * Registra un paziente inserito nel body della request
     *
     * @param user, DTO coi dati del paziente da registrare
     * @return HttpStatus (CREATED o BAD_REQUEST)
     */
    @PreAuthorize("hasAnyAuthority('[ADMIN]', '[MEDICO]')")
    @RequestMapping(value = "/sign-paz", method = RequestMethod.POST)
    public @ResponseBody
    HttpStatus signUpPaziente(@RequestBody Paziente user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        Medico daAssociare = new Medico();
        Iterator it;

        if (!repo.existsByCodiceFiscale(user.getCodiceFiscale()) && user.getAuthorities().equals("PAZIENTE")) {
            user.setAttivo(true);
            pazRepo.save(user);
            return HttpStatus.CREATED;
        } else return HttpStatus.BAD_REQUEST;
    }

    /**
     * Registra un medico inserito nel body della request
     *
     * @param user, DTO coi dati del medico da registrare
     * @return HttpStatus (CREATED o BAD_REQUEST)
     */
    @PreAuthorize("hasAuthority('[ADMIN]')")
    @RequestMapping(value = "/sign-med", method = RequestMethod.POST)
    public @ResponseBody
    HttpStatus signUpMedico(@RequestBody Medico user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        Paziente daAssociare = new Paziente();
        Iterator it;

        if (!repo.existsByCodiceFiscale(user.getCodiceFiscale()) && user.getAuthorities().equals("MEDICO")) {
            medRepo.save(user);
            return HttpStatus.CREATED;
        }
        return HttpStatus.BAD_REQUEST;
    }

    /**
     * Registra un amministratore inserito nel body della request
     *
     * @param user
     * @return HttpStatus (CREATED o BAD_REQUEST)
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "/sign-adm", method = RequestMethod.POST)
    public @ResponseBody
    HttpStatus signUpAmministratore(@RequestBody Amministratore user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        if (!repo.existsByCodiceFiscale(user.getCodiceFiscale()) && user.getAuthorities().equals("ADMIN")) {
            admRepo.save(user);
            return HttpStatus.CREATED;
        }
        return HttpStatus.BAD_REQUEST;
    }

    /**
     * Modifica un medico con i dati inseriti nel body della request
     *
     * @param medico
     * @return HttpStatus (OK o BAD_REQUEST)
     */
    @RequestMapping(value = "/edit-med", method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('[ADMIN]', '[MEDICO]')")
    public @ResponseBody
    ResponseEntity<Medico> editMedico(@RequestBody Medico medico) {
        Medico m = medRepo.findByCodiceFiscale(medico.getCodiceFiscale());
        if (m == null)
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

        //nel caso in cui l'utente autenticato non sia un amministratore, puo' modificare solo il suo stesso account
        if (!authenticatedUser.getAuthorities().equals("[ADMIN]")) {
            if (!authenticatedUser.getPrincipal().equals(medico.getCodiceFiscale())) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        }

        //passare un utente con campo password vuoto viene interpretato come password non da modificare,
        //quindi viene usato il valore della password prelevato dal database.
        //il campo password non vuoto invece viene interpretato come password da modificare,
        //e quindi viene usato il passwordencoder per hashare il nuovo valore e inserirlo nel Medico da salvare
        String password = medico.getPassword();
        if (password.equals("")) {
            medico.setPassword(m.getPassword());
        } else if (password.length() > 0) {
            medico.setPassword(bCryptPasswordEncoder.encode(medico.getPassword()));
        }

        medRepo.save(medico);
        medico.setPassword("");
        return new ResponseEntity<>(medico, HttpStatus.OK);
    }

    /**
     * Modifica un paziente con i dati inseriti nel body della request
     *
     * @param paziente
     * @return HttpStatus (OK o BAD_REQUEST)
     */
    @PreAuthorize("hasAnyAuthority('[ADMIN]', '[PAZIENTE]')")
    @RequestMapping(value = "/edit-paz", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<Paziente> editPaziente(@RequestBody Paziente paziente) {
        Paziente p = pazRepo.findByCodiceFiscale(paziente.getCodiceFiscale());
        if (p == null)
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

        //nel caso in cui l'utente autenticato non sia un amministratore, puo' modificare solo il suo stesso account
        if (!authenticatedUser.getAuthorities().equals("[ADMIN]")) {
            if (!authenticatedUser.getPrincipal().equals(p.getCodiceFiscale())) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        }

        //passare un utente con campo password vuoto viene interpretato come password non da modificare,
        //quindi viene usato il valore della password prelevato dal database.
        //il campo password non vuoto invece viene interpretato come password da modificare,
        //e quindi viene usato il passwordencoder per hashare il nuovo valore e inserirlo nel Paziente da salvare
        String password = paziente.getPassword();
        if (password.equals("")) {
            paziente.setPassword(p.getPassword());
        } else if (password.length() > 0) {
            paziente.setPassword(bCryptPasswordEncoder.encode(paziente.getPassword()));
        }

        pazRepo.save(paziente);
        paziente.setPassword("");
        return new ResponseEntity<>(paziente, HttpStatus.OK);
    }

    /**
     * Ottiene un Utente
     *
     * @param cf, codice fiscale dell'utente da cercare, ottenuto come pathvariable
     * @return HttpStatus (CREATED o BAD_REQUEST)
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/getuser/{cf}")
    public Utente getuser(@PathVariable("cf") String cf) {
        Utente u = repo.findByCodiceFiscale(cf);
        u.setPassword("");
        return u;
    }

    /**
     * Ottiene tutti gli utenti registrati
     *
     * @return Utente[]
     */
    @PreAuthorize("hasAuthority('[ADMIN]')")
    @GetMapping("/getuser/all")
    public @ResponseBody Utente[] getAllUsers() {
        ArrayList<Utente> users = new ArrayList<>();
        int i = 0;
        users = (ArrayList<Utente>) repo.findAll();

        return users.toArray(new Utente[users.size()]);
    }

    /**
     * Elimina un Utente
     *
     * @param cf, codice fiscale dell'utente da eliminare, ottenuto come pathvariable
     * @return HttpStatus (OK o NOT_FOUND)
     */
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping(value="/deleteuser/{cf}")
    HttpStatus deleteUser(@PathVariable("cf") String cf) {
        Utente u = repo.findByCodiceFiscale(cf);
        if (u == null) return HttpStatus.NOT_FOUND;

        //nel caso in cui l'utente autenticato non sia un amministratore, puo' cancellare solo il suo stesso account
        if (!authenticatedUser.getAuthorities().equals("[ADMIN]")){
            if (!authenticatedUser.getPrincipal().equals(cf)) {
                return HttpStatus.BAD_REQUEST;
            }
        }

        repo.deleteByCodiceFiscale(cf);
        return HttpStatus.OK;
    }

    /**
     * Elimina tutti gli utenti registrati
     *
     * @return void
     */
    @PreAuthorize("hasAuthority('[ADMIN]')")
    @DeleteMapping(value="/deleteuser/all")
    void deleteAllUsers() {
        repoCustom.deleteAllExceptAdmin();
    }
}
