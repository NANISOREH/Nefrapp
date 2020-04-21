package team.nefrapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import team.nefrapp.model.Utente;
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

    //rimuove un utente
    @PostMapping (path="/removeUser")
    public String removeUser(@RequestParam (value="cf") String cf) {
        repo.delete(repo.findByCodiceFiscale(cf));
        return "redirect:utenti";
    }

    //aggiunge un utente i cui dati sono stati inseriti da localhost:8080/utenti
    @PostMapping(path="/utenti")
    public String addUser(HttpServletRequest req, HttpServletResponse res) {
        String cf = req.getParameter("codiceFiscale");
        String password = req.getParameter("password");
        String role = req.getParameter("role");

        Utente user = new Utente();
        user.setAuthorities(role);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setCodiceFiscale(cf);

        if (!repo.existsByCodiceFiscale(user.getCodiceFiscale()))
            repo.save(user);

        return "redirect:utenti";
    }

}
