package nefrapp.site.controller;

/*import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;*/
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.logging.Logger;

@Controller
public class PageController {
    Logger log = Logger.getLogger("PageController");

    @GetMapping({"/", "/dashboard"})
    public String dashboard(HttpSession session, HttpServletResponse res) {
        return "dashboard";
    }

    @GetMapping(value = "/team")
    public String team() {
        return "team";
    }

/*    @GetMapping(value = "/profilo")
    public String profilo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.info(auth.toString());
        return "profilo";
    }

    @GetMapping(value = "/authprova")
    public String authprova() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.info(auth.toString());
        return "authprova";
    }*/

    @GetMapping({"/error", "/error.jsp"})
    public String error() {
        return "error";
    }

}
