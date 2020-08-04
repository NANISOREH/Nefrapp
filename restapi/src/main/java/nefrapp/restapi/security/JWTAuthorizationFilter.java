package nefrapp.restapi.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import nefrapp.restapi.controller.RestUserController;
import nefrapp.restapi.model.Utente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.client.RestTemplate;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Filtro di autorizzazione inserito nella filter chain di Spring Security.
 * Si occupa di intercettare il token dalla request e usarlo per autorizzare ogni richiesta al container.
 */
@Order(0)
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
    final String TOKENKEY = "secret";    //TODO: gestire chiave
    Logger log = Logger.getLogger("AuthorizationFilter");

    public JWTAuthorizationFilter(AuthenticationManager authManager) {
        super(authManager);
    }

    /**
     * Logica del filtro. Preleva il token dalla request (se presente) e contatta il SecurityContext per settare l'autenticazione
     *
     * @param res
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {

        String header = req.getHeader("token");
        UsernamePasswordAuthenticationToken authentication = null;
    
        if (header != null) {
            authentication = getAuthentication(header);
        }
        if (authentication != null) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(req, res);
        return;
    }

    /**
     * Si occupa di parsare la stringa token per ottenere le effettive credenziali dell'oggetto JWT.
     * Se la stringa token ricevuta è valida, restituisce un oggetto UsernamePasswordAuthenticationToken,
     * utilizzabile dal SecurityContext per autorizzare la richiesta.
     *
     * @param token
     * @return
     */
    private UsernamePasswordAuthenticationToken getAuthentication(String token) {
        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();
        if (token != null) {
            // parse the token.
            String user = JWT.require(Algorithm.HMAC512(TOKENKEY.getBytes()))
                    .build()
                    .verify(token.replace("Bearer ", ""))
                    .getSubject();

            Claim role = JWT.require(Algorithm.HMAC512(TOKENKEY.getBytes()))
                    .build()
                    .verify(token.replace("Bearer ", ""))
                    .getClaim("Authorities");

            authorities.add(new SimpleGrantedAuthority(role.asString()));

            if (user != null) {
                return new UsernamePasswordAuthenticationToken(user, "", authorities);
            }
            return null;
        }
        return null;
    }
}