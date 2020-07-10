package team.nefrapp.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.net.CookieManager;
import java.net.CookieStore;
import java.text.Normalizer;
import java.util.logging.Logger;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@Controller
public class PageController {
    Logger log = Logger.getLogger("PageController");
    @GetMapping({"/", "/dashboard"})
    public String dashboard(HttpSession session, HttpServletResponse res) {
        return "dashboard";
    }

    @GetMapping(value="/team")
    public String team() {
        return "team";
    }

    @GetMapping(value="/profilo")
    public String profilo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.info(auth.toString());
        return "profilo";
    }

    @GetMapping(value="/authprova")
    public String authprova() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.info(auth.toString());
        return "authprova";
    }

    @GetMapping({"/error", "/error.jsp"})
    public String error() { return "error"; }

}
