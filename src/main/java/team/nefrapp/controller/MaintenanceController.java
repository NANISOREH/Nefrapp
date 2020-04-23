package team.nefrapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import team.nefrapp.model.Amministratore;
import team.nefrapp.model.Medico;
import team.nefrapp.model.Paziente;
import team.nefrapp.model.Utente;
import team.nefrapp.repository.AmministratoreRepository;
import team.nefrapp.repository.MedicoRepository;
import team.nefrapp.repository.PazienteRepository;
import team.nefrapp.repository.UtenteRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.logging.Logger;

@Controller
public class MaintenanceController {
    @Autowired
    private UtenteRepository repo;

    Logger log = Logger.getLogger("MaintenanceController");

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public MaintenanceController(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    //serve la pagina utenti.jsp, da localhost:8080/utenti
    @GetMapping(path="/utenti")
    public ModelAndView addUser(ModelAndView model) {
        Iterable<Utente> list = repo.findAll();
        ArrayList<Utente> utenti = new ArrayList<Utente>();
        for (Utente u : list) utenti.add(u);

        model.addObject("utenti", utenti);
        model.setViewName("utenti");
        return model;
    }

    @GetMapping(path="/clean")
    public String cleanTable(){
        log.info("ci entri");
        repo.deleteAll();
        return "redirect:utenti";
    }

    //aggiunge un utente i cui dati sono stati inseriti da localhost:8080/utenti
    @PostMapping(path="/utenti")
    public String addUser(@RequestParam (value="codiceFiscale") String cf,
                          @RequestParam (value="password") String password,
                          @RequestParam (value="role") String role) {

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
                p.setIsAttivo(true);
                result = rt.postForObject("http://localhost:8080/sign-up", p, HttpStatus.class);
                break;
            case "ROLE_MEDICO":
                m = new Medico();
                m.setAuthorities(role);
                m.setPassword(password);
                m.setCodiceFiscale(cf);
                result = rt.postForObject("http://localhost:8080/sign-up", m, HttpStatus.class);
                break;
            case "ROLE_ADMIN":
                a = new Amministratore();
                a.setAuthorities(role);
                a.setPassword(password);
                a.setCodiceFiscale(cf);
                result = rt.postForObject("http://localhost:8080/sign-up", a, HttpStatus.class);
                break;
        }

        log.info(result.toString());
        return "redirect:utenti";
    }

}
