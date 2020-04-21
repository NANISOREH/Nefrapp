package team.nefrapp.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import sun.java2d.pipe.SpanShapeRenderer;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.util.ArrayList;

import static javax.crypto.Cipher.SECRET_KEY;

@Order(0)
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    public JWTAuthorizationFilter(AuthenticationManager authManager) {
        super(authManager);
    }

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

    private UsernamePasswordAuthenticationToken getAuthentication(String token) {
        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();
        if (token != null) {
            // parse the token.
            String user = JWT.require(Algorithm.HMAC512("secret".getBytes()))
                    .build()
                    .verify(token.replace("Bearer ", ""))
                    .getSubject();

            Claim role = JWT.require(Algorithm.HMAC512("secret".getBytes()))
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