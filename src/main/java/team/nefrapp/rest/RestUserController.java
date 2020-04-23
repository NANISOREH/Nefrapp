package team.nefrapp.rest;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.*;
import team.nefrapp.model.Amministratore;
import team.nefrapp.model.Medico;
import team.nefrapp.model.Paziente;
import team.nefrapp.model.Utente;
import team.nefrapp.repository.AmministratoreRepository;
import team.nefrapp.repository.MedicoRepository;
import team.nefrapp.repository.PazienteRepository;
import team.nefrapp.repository.UtenteRepository;

import javax.security.sasl.AuthenticationException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Logger;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

@RestController
public class RestUserController {
    Logger log = Logger.getLogger("RestLoginController");
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
    public @ResponseBody String createAuthenticationToken(@RequestBody LinkedMultiValueMap<String, String> authData){
        String user = authData.getFirst("username");
        Authentication auth;
        Utente u = new Utente();
        u.setCodiceFiscale(user);
        u = repo.findByCodiceFiscale(u.getCodiceFiscale());

        if (u == null)  return null;

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
            return null;
        }

        String token = JWT.create()
                .withSubject(((User) auth.getPrincipal()).getUsername())
                .withClaim("Authorities", auth.getAuthorities().toString())
                .withExpiresAt(new Date(System.currentTimeMillis() + 864000000))
                .sign(HMAC512("secret".getBytes())); //TODO: gestione chiave

        return "Bearer/" + token;
    }

    @RequestMapping(value = "/sign-up", method = RequestMethod.POST)
    public @ResponseBody HttpStatus signUp(@RequestBody Utente user) {
        Paziente p = new Paziente();
        Medico m = new Medico();
        Amministratore a = new Amministratore();
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        if (!repo.existsByCodiceFiscale(user.getCodiceFiscale()))
        {
            switch (user.getAuthorities()) {
                case "ROLE_PAZIENTE" :
                    p.setAuthorities(user.getAuthorities());
                    p.setPassword(user.getPassword());
                    p.setCodiceFiscale(user.getCodiceFiscale());
                    p.setIsAttivo(true);
                    pazRepo.save(p);
                    break;
                case "ROLE_MEDICO" :
                    m.setAuthorities(user.getAuthorities());
                    m.setPassword(user.getPassword());
                    m.setCodiceFiscale(user.getCodiceFiscale());
                    medRepo.save(m);
                    break;
                case "ROLE_ADMIN" :
                    a.setAuthorities(user.getAuthorities());
                    a.setPassword(user.getPassword());
                    a.setCodiceFiscale(user.getCodiceFiscale());
                    admRepo.save(a);
                    break;
                default: break;
            }
            return HttpStatus.OK;
        }
        return HttpStatus.BAD_REQUEST;
    }

}
