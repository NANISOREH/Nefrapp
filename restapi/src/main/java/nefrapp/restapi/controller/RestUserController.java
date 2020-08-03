package nefrapp.restapi.controller;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import nefrapp.restapi.model.Amministratore;
import nefrapp.restapi.model.Medico;
import nefrapp.restapi.model.Paziente;
import nefrapp.restapi.model.Utente;
import nefrapp.restapi.repository.AmministratoreRepository;
import nefrapp.restapi.repository.MedicoRepository;
import nefrapp.restapi.repository.PazienteRepository;
import nefrapp.restapi.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.security.sasl.AuthenticationException;
import javax.transaction.Transactional;
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
    PazienteRepository pazRepo;
    @Autowired
    AmministratoreRepository admRepo;
    @Autowired
    MedicoRepository medRepo;
    @Autowired
    private AuthenticationManager authenticationManager;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

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
        log.info(authData.toString());
        String user = authData.getCodiceFiscale();
        Authentication auth;
        Utente u = new Utente();
        u = repo.findByCodiceFiscale(authData.getCodiceFiscale());

        if (u == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nessun utente con questo CF");
        }


        SimpleGrantedAuthority s = new SimpleGrantedAuthority(u.getAuthorities());
        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(s);

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

        if (u.getAutenticato() != null && u.getAutenticato() == true) {
            return u;
        }

        String token = JWT.create()
                .withSubject(((User) auth.getPrincipal()).getUsername())
                .withClaim("Authorities", auth.getAuthorities().toString())
                .withExpiresAt(new Date(System.currentTimeMillis() + 864000000))
                .sign(HMAC512("secret".getBytes())); //TODO: gestione chiave
        token = "Bearer/" + token;
        u.setAutenticato(true);
        u.setToken(token);
        repo.save(u);
        u.setPassword("");
        return u;
    }

    @RequestMapping(value = "/deauth", method = RequestMethod.POST)
    public @ResponseBody
    HttpStatus deauthUtente (@RequestBody String cf) {
        Utente u = new Utente();
        u = repo.findByCodiceFiscale(cf);
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
     * @param user
     * @return HttpStatus (CREATED o BAD_REQUEST)
     */
    @RequestMapping(value = "/sign-paz", method = RequestMethod.POST)
    public @ResponseBody
    HttpStatus signUpPaziente(@RequestBody Paziente user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        Medico daAssociare = new Medico();
        Iterator it;

        if (!repo.existsByCodiceFiscale(user.getCodiceFiscale()) && user.getAuthorities().equals("ROLE_PAZIENTE")) {
            user.setAttivo(true);
            pazRepo.save(user);
            return HttpStatus.CREATED;
        } else return HttpStatus.BAD_REQUEST;
    }

    /**
     * Registra un medico inserito nel body della request
     *
     * @param user
     * @return HttpStatus (CREATED o BAD_REQUEST)
     */
    @RequestMapping(value = "/sign-med", method = RequestMethod.POST)
    public @ResponseBody
    HttpStatus signUpMedico(@RequestBody Medico user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        Paziente daAssociare = new Paziente();
        Iterator it;

        if (!repo.existsByCodiceFiscale(user.getCodiceFiscale()) && user.getAuthorities().equals("ROLE_MEDICO")) {
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
    @RequestMapping(value = "/sign-adm", method = RequestMethod.POST)
    public @ResponseBody
    HttpStatus signUpAmministratore(@RequestBody Amministratore user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        if (!repo.existsByCodiceFiscale(user.getCodiceFiscale()) && user.getAuthorities().equals("ROLE_MEDICO")) {
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
    public @ResponseBody
    ResponseEntity<Medico> editMedico(@RequestBody Medico medico) {
        Medico m = medRepo.findByCodiceFiscale(medico.getCodiceFiscale());
        if (m == null)
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

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
    @RequestMapping(value = "/edit-paz", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<Paziente> editPaziente(@RequestBody Paziente paziente) {
        Paziente p = pazRepo.findByCodiceFiscale(paziente.getCodiceFiscale());
        if (p == null)
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

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

    @GetMapping("/getuser/{cf}")
    Utente getuser(@PathVariable("cf") String cf) {
        Utente u = repo.findByCodiceFiscale(cf);
        u.setPassword("");
        return u;
    }

    @GetMapping("/getuser/all")
    Utente[] getAllUsers() {
        Utente[] users = new Utente[50];
        int i = 0;
        Iterable<Utente> list = repo.findAll();
        for (Utente u : list) {
            users[i] = u;
            i++;
        }

        return users;
    }

    @DeleteMapping(value="/deleteuser/{cf}")
    HttpStatus deleteUser(@PathVariable("cf") String cf) {
        Utente u = repo.findByCodiceFiscale(cf);
        if (u == null) return HttpStatus.NOT_FOUND;
        repo.deleteByCodiceFiscale(cf);
        return HttpStatus.OK;
    }

    @DeleteMapping(value="/deleteuser/all")
    void deleteAllUsers() {
        repo.deleteAll();
    }
}
