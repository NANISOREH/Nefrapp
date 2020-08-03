package nefrapp.site.controller;

import nefrapp.site.model.Amministratore;
import nefrapp.site.model.Medico;
import nefrapp.site.model.Paziente;
import nefrapp.site.model.Utente;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.logging.Logger;

@Controller
public class MaintenanceController {
    Logger log = Logger.getLogger("MaintenanceController");

    @GetMapping(path = "/utenti")
    public ModelAndView showUsers(ModelAndView model) {
        RestTemplate rt = new RestTemplate();
        HttpStatus result = null;
        Utente[] list = rt.getForObject("http://localhost:8080/getuser/all", Utente[].class);
        ArrayList<Utente> utenti = new ArrayList<Utente>();
        for (Utente u : list)
        {
            if (u != null)
            utenti.add(u);
        }

        model.addObject("utenti", utenti);
        model.setViewName("utenti");
        return model;
    }

    @RequestMapping(value="/clean", method = RequestMethod.GET)
    public String cleanTable(){
        RestTemplate rt = new RestTemplate();
        rt.delete("http://localhost:8080/deleteuser/all");
        return "redirect:utenti";
    }

    @RequestMapping(value="/deleteUser", method = RequestMethod.DELETE)
    public String deleteUser(@RequestParam(value = "cf") String cf){
        RestTemplate rt = new RestTemplate();
        rt.delete("http://localhost:8080/deleteuser/" + cf);
        return "redirect:utenti";
    }

    @PostMapping(path = "/utenti")
    public String addUser(@RequestParam(value = "codiceFiscale") String cf,
                          @RequestParam(value = "password") String password,
                          @RequestParam(value = "role") String role) {

        Paziente p = null;
        Medico m = null;
        Amministratore a = null;
        RestTemplate rt = new RestTemplate();
        HttpStatus result = null;

        switch (role) {
            case "ROLE_PAZIENTE":
                p = new Paziente();
                p.setAuthorities(role);
                p.setPassword(password);
                p.setCodiceFiscale(cf);
                p.setAttivo(true);
                result = rt.postForObject("http://localhost:8080/sign-paz", p, HttpStatus.class);
                break;
            case "ROLE_MEDICO":
                m = new Medico();
                m.setAuthorities(role);
                m.setPassword(password);
                m.setCodiceFiscale(cf);
                result = rt.postForObject("http://localhost:8080/sign-med", m, HttpStatus.class);
                break;
            case "ROLE_ADMIN":
                a = new Amministratore();
                a.setAuthorities(role);
                a.setPassword(password);
                a.setCodiceFiscale(cf);
                result = rt.postForObject("http://localhost:8080/sign-adm", a, HttpStatus.class);
                break;
        }

        log.info(result.toString());
        return "redirect:utenti";
    }

}
