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
import javax.xml.ws.Response;
import java.util.*;
import java.util.logging.Logger;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

//TODO: exception handling per questo controller

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
    Map<String, String> createAuthenticationToken(@RequestBody LinkedMultiValueMap<String, String> authData){
        String user = authData.getFirst("username");
        Authentication auth;
        Utente u = new Utente();
        u.setCodiceFiscale(user);
        u = repo.findByCodiceFiscale(u.getCodiceFiscale());

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

        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("role", u.getAuthorities());
        log.info(response.toString());
        return response;
    }

    @RequestMapping(value = "/sign-paz", method = RequestMethod.POST)
    public @ResponseBody HttpStatus signUpPaziente(@RequestBody Paziente user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        Medico daAssociare = new Medico();
        Iterator it;

        if (!repo.existsByCodiceFiscale(user.getCodiceFiscale()) && user.getAuthorities().equals("ROLE_PAZIENTE")) {
            user.setAttivo(true);
            pazRepo.save(user);
            if (!user.getCuranti().isEmpty()) {
                it = user.getCuranti().iterator();
                daAssociare = (Medico)it.next();
                //TODO: aggiungi l'associazione anche per il medico
            }
            return HttpStatus.CREATED;
        }
        else return HttpStatus.BAD_REQUEST;
    }

    @RequestMapping(value = "/sign-med", method = RequestMethod.POST)
    public @ResponseBody HttpStatus signUpMedico(@RequestBody Medico user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        Paziente daAssociare = new Paziente();
        Iterator it;

        if (!repo.existsByCodiceFiscale(user.getCodiceFiscale()) && user.getAuthorities().equals("ROLE_MEDICO"))
        {
            medRepo.save(user);
            if (!user.getSeguiti().isEmpty()) {
                it = user.getSeguiti().iterator();
                daAssociare = (Paziente)it.next();
                //TODO: aggiungi l'associazione anche per il paziente
            }
            return HttpStatus.CREATED;
        }
        return HttpStatus.BAD_REQUEST;
    }

    @RequestMapping(value = "/sign-adm", method = RequestMethod.POST)
    public @ResponseBody HttpStatus signUpAmministratore(@RequestBody Amministratore user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        log.info("sign-med");
        log.info("password " + user.getPassword());

        if (!repo.existsByCodiceFiscale(user.getCodiceFiscale()) && user.getAuthorities().equals("ROLE_MEDICO"))
        {
            log.info("faccio il set");
            admRepo.save(user);
            return HttpStatus.CREATED;
        }
        return HttpStatus.BAD_REQUEST;
    }

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

    @RequestMapping(value = "/getuser", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<Utente> getUser(@RequestBody String cf) {
        Utente fetched = repo.findByCodiceFiscale(cf);
        if (fetched != null) {
            fetched.setPassword("");
            return new ResponseEntity<>(fetched, HttpStatus.OK);
        }

        return new ResponseEntity<>(fetched, HttpStatus.BAD_REQUEST);
    }

}
