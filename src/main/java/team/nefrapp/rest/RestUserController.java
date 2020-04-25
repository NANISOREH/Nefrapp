package team.nefrapp.rest;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.core.JsonProcessingException;
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
import team.nefrapp.model.Amministratore;
import team.nefrapp.model.Medico;
import team.nefrapp.model.Paziente;
import team.nefrapp.model.Utente;
import team.nefrapp.repository.AmministratoreRepository;
import team.nefrapp.repository.MedicoRepository;
import team.nefrapp.repository.PazienteRepository;
import team.nefrapp.repository.UtenteRepository;

import javax.security.sasl.AuthenticationException;
import javax.validation.Valid;
import javax.xml.ws.Response;
import java.util.*;
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
     * @param authData Map di stringhe contenente i dati di autenticazione
     * @return
     * @throws AuthenticationException
     * @throws JsonProcessingException
     */
    @RequestMapping(value = "/auth", method = RequestMethod.POST)
    public @ResponseBody
    Utente createAuthenticationToken(@RequestBody LinkedMultiValueMap<String, String> authData){
        String user = authData.getFirst("username");
        Authentication auth;
        Utente u = new Utente();
        u.setCodiceFiscale(user);
        u = repo.findByCodiceFiscale(u.getCodiceFiscale());

        //Sì, le eccezioni qua provocano esplosioni quando dai dati d'accesso non corretti
        //ma l'unico modo vero di evitarlo è deployare (come si dovrebbe) backend rest e sito
        //come due webapp completamente distinte. Se l'api lancia eccezione il sito dovrebbe soltanto
        //raccogliere il suo bell'HttpStatus negativo e andare avanti con la sua vita.
        //Non puoi nemmeno fare exception handling dell'api nei controller del sito, perché
        //le due parti devono essere completamente disaccoppiate e comunicare solo attraverso request e response http
        if (u == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nessun utente presente ha il CF indicato");
        };

        SimpleGrantedAuthority s = new SimpleGrantedAuthority(u.getAuthorities());
        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(s);

        UsernamePasswordAuthenticationToken t = new UsernamePasswordAuthenticationToken(
                        authData.getFirst("username"),
                        authData.getFirst("password"),
                        authorities);
        try {
            auth = authenticationManager.authenticate(t);
        } catch (org.springframework.security.core.AuthenticationException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Password errata", e);
        }

        String token = JWT.create()
                .withSubject(((User) auth.getPrincipal()).getUsername())
                .withClaim("Authorities", auth.getAuthorities().toString())
                .withExpiresAt(new Date(System.currentTimeMillis() + 864000000))
                .sign(HMAC512("secret".getBytes())); //TODO: gestione chiave
        token = "Bearer/"+token;

        u.setToken(token);
        u.setPassword("");
        return u;
    }

    /**
     * Registra un paziente inserito nel body della request
     * @param user
     * @return HttpStatus (CREATED o BAD_REQUEST)
     */
    @RequestMapping(value = "/sign-paz", method = RequestMethod.POST)
    public @ResponseBody HttpStatus signUpPaziente(@RequestBody Paziente user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        Medico daAssociare = new Medico();
        Iterator it;

        if (!repo.existsByCodiceFiscale(user.getCodiceFiscale()) && user.getAuthorities().equals("ROLE_PAZIENTE")) {
            user.setAttivo(true);
            pazRepo.save(user);
            return HttpStatus.CREATED;
        }
        else return HttpStatus.BAD_REQUEST;
    }

    /**
     * Registra un medico inserito nel body della request
     * @param user
     * @return HttpStatus (CREATED o BAD_REQUEST)
     */
    @RequestMapping(value = "/sign-med", method = RequestMethod.POST)
    public @ResponseBody HttpStatus signUpMedico(@RequestBody Medico user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        Paziente daAssociare = new Paziente();
        Iterator it;

        if (!repo.existsByCodiceFiscale(user.getCodiceFiscale()) && user.getAuthorities().equals("ROLE_MEDICO"))
        {
            medRepo.save(user);
            return HttpStatus.CREATED;
        }
        return HttpStatus.BAD_REQUEST;
    }

    /**
     * Registra un amministratore inserito nel body della request
     * @param user
     * @return HttpStatus (CREATED o BAD_REQUEST)
     */
    @RequestMapping(value = "/sign-adm", method = RequestMethod.POST)
    public @ResponseBody HttpStatus signUpAmministratore(@RequestBody Amministratore user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        if (!repo.existsByCodiceFiscale(user.getCodiceFiscale()) && user.getAuthorities().equals("ROLE_MEDICO"))
        {
            admRepo.save(user);
            return HttpStatus.CREATED;
        }
        return HttpStatus.BAD_REQUEST;
    }

    /**
     * Modifica un medico con i dati inseriti nel body della request
     * @param medico
     * @return HttpStatus (OK o BAD_REQUEST)
     */
    @RequestMapping(value = "/edit-med", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<Medico> editMedico(@RequestBody Medico medico) {
        Medico m = medRepo.findByCodiceFiscale(medico.getCodiceFiscale());
        if (m == null)
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

        //passare un utente con campo password vuoto viene interpretato come password non da modificare,
        //quindi viene usato il valore della password prelevato dal database.
        //il campo password non vuoto invece viene interpretato come password da modificare,
        //e quindi viene usato il passwordencoder per hashare il nuovo valore e inserirlo nel Medico da salvare
        String password = medico.getPassword();
        if (password.equals("")){
            medico.setPassword(m.getPassword());
        } else if (password.length()>0) {
            medico.setPassword(bCryptPasswordEncoder.encode(medico.getPassword()));
        }

        medRepo.save(medico);
        medico.setPassword("");
        return new ResponseEntity<>(medico, HttpStatus.OK);
    }

    /**
     * Modifica un paziente con i dati inseriti nel body della request
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
        if (password.equals("")){
            paziente.setPassword(p.getPassword());
        } else if (password.length()>0) {
            paziente.setPassword(bCryptPasswordEncoder.encode(paziente.getPassword()));
        }

        pazRepo.save(paziente);
        paziente.setPassword("");
        return new ResponseEntity<>(paziente, HttpStatus.OK);
    }

}
