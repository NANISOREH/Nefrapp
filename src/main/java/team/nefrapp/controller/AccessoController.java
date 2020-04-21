package team.nefrapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;
import team.nefrapp.repository.UtenteRepository;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.logging.Logger;


@Controller
public class AccessoController {
    @Autowired
    private UtenteRepository repo;
    Logger log = Logger.getLogger("AccessoController");

    @GetMapping(value="/login")
    public String serveLogin(HttpServletResponse res) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            return "dashboard";
        }
        else return "login";
    }

    @PostMapping("/login")
    public String doLogin(HttpServletRequest req, HttpServletResponse res, HttpSession session) {
        Cookie auth, role;
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("username", req.getParameter("username"));
        map.add("password", req.getParameter("password"));

        RestTemplate rt = new RestTemplate();
        String token = rt.postForObject("http://localhost:8080/auth", map, String.class);

        //ruolo hardcodato come paziente temporaneamente
        if (token != null) {
            session.setAttribute("accessDone", true);
            session.setAttribute("isPaziente", true);
            auth = new Cookie ("nefrapp_auth", "Bearer/" + token);
            res.addCookie(auth);
            return "dashboard";
        }

        return "login";
    }

    @RequestMapping(value="/logout", method = RequestMethod.POST)
    public RedirectView logout (HttpSession session, HttpServletResponse response, HttpServletRequest request) {
        session.invalidate();
        log.info("logout controller method");
        return new RedirectView("/dashboard");
    }
}
