package team.nefrapp.rest;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.*;
import team.nefrapp.model.Utente;
import team.nefrapp.repository.UtenteRepository;

import javax.security.sasl.AuthenticationException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Logger;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

@RestController
public class RestLoginController {
    Logger log = Logger.getLogger("RestLoginController");
    @Autowired
    UtenteRepository repo;
    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * Ottiene un token di autenticazione JWT se le credenziali in input sono corrette
     * @param authData Map di stringhe contenente i dati di autenticazione
     * @return
     * @throws AuthenticationException
     * @throws JsonProcessingException
     */
    @RequestMapping(value = "/auth", method = RequestMethod.POST)
    public @ResponseBody String createAuthenticationToken(@RequestBody LinkedMultiValueMap<String, String> authData) throws AuthenticationException, JsonProcessingException {
        String user = authData.getFirst("username");
        Authentication auth;
        Utente u = new Utente();
        u.setCodiceFiscale(user);
        u = repo.findByCodiceFiscale(u.getCodiceFiscale());

        SimpleGrantedAuthority s = new SimpleGrantedAuthority(u.getAuthorities().toString());
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
                .sign(HMAC512("secret".getBytes()));

        return token;
    }

}
