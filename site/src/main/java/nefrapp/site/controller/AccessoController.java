package nefrapp.site.controller;

import nefrapp.site.model.Utente;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.logging.Logger;


@Controller
public class AccessoController {
    private RestTemplate rt = new RestTemplate();
    Logger log = Logger.getLogger("AccessoController");

    @GetMapping(value = "/login")
    public String serveLogin(HttpSession session) {
        if (session.getAttribute("accessDone") != null && (Boolean) session.getAttribute("accessDone") == true) {
            return "dashboard";
        } else {
            return "login";
        }
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam String username, @RequestParam String password, HttpServletResponse res, HttpSession session) {
        Cookie auth;
        Utente utente = new Utente();
        utente.setCodiceFiscale(username);
        utente.setPassword(password);

        try {
            utente = rt.postForObject("http://localhost:8080/auth", utente, Utente.class);
        } catch (HttpClientErrorException e) {
            log.info(e.getMessage());
            utente = null;
        }

        if (utente != null) {
            String token = utente.getToken();
            String role = utente.getAuthorities();
            if (utente != null) {
                utente.setToken("");
                session.setAttribute("utente", utente);
            }
            session.setAttribute("accessDone", true);
            switch (role) {
                case ("PAZIENTE"):
                    session.setAttribute("isPaziente", true);
                    break;
                case ("MEDICO"):
                    session.setAttribute("isMedico", true);
                    break;
                case ("ADMIN"):
                    session.setAttribute("isAmministratore", true);
                    break;
            }
            auth = new Cookie("nefrapp_auth", token);
            res.addCookie(auth);
            return "dashboard";
        }

        return "login";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public RedirectView logout(HttpSession session, HttpServletRequest req, HttpServletResponse response) {
        Utente u = (Utente)session.getAttribute("utente");
        HttpEntity request = makeRequest(req, u.getCodiceFiscale());

        RestTemplate rt = new RestTemplate();
        rt.exchange("http://localhost:8080/deauth", HttpMethod.POST, request, HttpStatus.class);

        session.invalidate();
        response.setContentType("text/html");
        Cookie cookie = new Cookie("nefrapp_auth", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setComment("EXPIRING COOKIE at " + System.currentTimeMillis());
        response.addCookie(cookie);

        return new RedirectView("/dashboard");
    }

    private HttpEntity makeRequest(HttpServletRequest req, String cf) {
        Cookie[] cookies = req.getCookies();
        String header = new String();

        if (cookies == null || cookies.length == 1) {
            log.info("header nullo");
            return null;
        }

        for (Cookie c : cookies) {
            if (c.getName().equals("nefrapp_auth")) {
                header = c.getValue().replace("/", " ");
            }
        }

        if (header == null || !header.startsWith("") || header.endsWith("Bearer ")) {
            log.info("header vuoto");
            return null;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("token", header);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
        map.add("cf", cf);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        return request;
    }
}
