package nefrapp.rest.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Filtro di autorizzazione inserito nella filter chain di Spring Security.
 * Si occupa di intercettare il token dalla request e usarlo per autorizzare ogni richiesta al container.
 */
@Order(0)
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
    final String TOKENKEY = "secret";    //TODO: gestire chiave

    public JWTAuthorizationFilter(AuthenticationManager authManager) {
        super(authManager);
    }

    /**
     * Logica del filtro. Preleva il token dalla request (se presente) e contatta il SecurityContext per settare l'autenticazione
     * @param res
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {

        String header = null;
        Cookie[] cookies = req.getCookies();
        if (cookies == null || cookies.length == 1) {
            chain.doFilter(req, res);
            return;
        }

        for (Cookie c : cookies) {
            if (c.getName().equals("nefrapp_auth")) {
                header = c.getValue().replace("/", " ");
            }
        }

        if (header == null || !header.startsWith("") || header.endsWith("Bearer ")) {
            chain.doFilter(req, res);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(header);
        logger.info(authentication.toString());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
        return;
    }

    /**
     * Si occupa di parsare la stringa token per ottenere le effettive credenziali dell'oggetto JWT.
     * Se la stringa token ricevuta è valida, restituisce un oggetto UsernamePasswordAuthenticationToken,
     * utilizzabile dal SecurityContext per autorizzare la richiesta.
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

            logger.info(role.toString());
            logger.info(role.asString());
            authorities.add(new SimpleGrantedAuthority(role.asString()));

            if (user != null) {
                return new UsernamePasswordAuthenticationToken(user, "", authorities);
            }
            return null;
        }
        return null;
    }
}