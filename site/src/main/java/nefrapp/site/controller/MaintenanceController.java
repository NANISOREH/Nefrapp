package nefrapp.site.controller;

import nefrapp.site.model.Amministratore;
import nefrapp.site.model.Medico;
import nefrapp.site.model.Paziente;
import nefrapp.site.model.Utente;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Logger;

@Controller
public class MaintenanceController {
    Logger log = Logger.getLogger("MaintenanceController");

    @GetMapping(path = "/utenti")
    public ModelAndView showUsers(HttpServletRequest req, ModelAndView model) {

        String token = getTokenFromRequest(req);
        HttpHeaders headers = new HttpHeaders();
        headers.set("token", token);
        HttpEntity request = new HttpEntity(headers);

        ResponseEntity<Utente[]> response = null;

        RestTemplate rt = new RestTemplate();
        try{
            response = rt.exchange("http://localhost:8080/getuser/all", HttpMethod.GET, request, Utente[].class);
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
        Utente[] list = response.getBody();
        ArrayList<Utente> utenti = new ArrayList<Utente>();
        for (Utente u : list) {
            if (u != null)
            utenti.add(u);
        }

        model.addObject("utenti", utenti);
        model.setViewName("utenti");
        return model;
    }

    @RequestMapping(value="/clean", method = RequestMethod.GET)
    public String cleanTable(HttpServletRequest req){
        String token = getTokenFromRequest(req);
        HttpHeaders headers = new HttpHeaders();
        headers.set("token", token);
        HttpEntity request = new HttpEntity(headers);

        RestTemplate rt = new RestTemplate();
        rt.exchange("http://localhost:8080/deleteuser/all", HttpMethod.DELETE, request, HttpStatus.class);
        return "redirect:utenti";
    }

    @RequestMapping(value="/deleteUser", method = RequestMethod.DELETE)
    public String deleteUser(@RequestParam(value = "cf") String cf, HttpServletRequest req){
        String token = getTokenFromRequest(req);
        HttpHeaders headers = new HttpHeaders();
        headers.set("token", token);
        HttpEntity request = new HttpEntity(headers);

        RestTemplate rt = new RestTemplate();
        rt.exchange("http://localhost:8080/deleteuser/" + cf, HttpMethod.DELETE, request, HttpStatus.class);
        return "redirect:utenti";
    }

    @PostMapping(path = "/utenti")
    public String addUser(@RequestParam(value = "codiceFiscale") String cf,
                          @RequestParam(value = "password") String password,
                          @RequestParam(value = "role") String role,
                          HttpServletRequest req) {

        String token = getTokenFromRequest(req);
        HttpHeaders headers = new HttpHeaders();
        headers.set("token", token);
        HttpEntity request;

        RestTemplate rt = new RestTemplate();
        ResponseEntity<HttpStatus> result = null;

        switch (role) {
            case "PAZIENTE":
                Paziente p = new Paziente();
                p.setAuthorities(role);
                p.setPassword(password);
                p.setCodiceFiscale(cf);
                p.setAttivo(true);
                request = new HttpEntity<Paziente>(p, headers);
                result = rt.exchange("http://localhost:8080/sign-paz", HttpMethod.POST, request, HttpStatus.class);
                break;
            case "MEDICO":
                Medico m = new Medico();
                m.setAuthorities(role);
                m.setPassword(password);
                m.setCodiceFiscale(cf);
                request = new HttpEntity<Medico>(m, headers);
                result = rt.exchange("http://localhost:8080/sign-med", HttpMethod.POST, request, HttpStatus.class);
                break;
            case "ADMIN":
                Amministratore a = new Amministratore();
                a.setAuthorities(role);
                a.setPassword(password);
                a.setCodiceFiscale(cf);
                request = new HttpEntity<Amministratore>(a, headers);
                result = rt.exchange("http://localhost:8080/sign-adm", HttpMethod.POST, request, HttpStatus.class);
                break;
        }

        log.info(result.toString());
        return "redirect:utenti";
    }


    private String getTokenFromRequest(HttpServletRequest req) {
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

        return header;
    }
}
