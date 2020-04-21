package team.nefrapp.controller;

import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import team.nefrapp.entity.Utente;
import team.nefrapp.formdata.LoginForm;
import team.nefrapp.repository.UtenteRepository;

import javax.validation.Valid;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.logging.Logger;

import static team.nefrapp.security.PasswordManager.hashPassword;

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
    public String addUser(@ModelAttribute("utenti") @Valid LoginForm item, BindingResult result) {
        if (result.hasErrors()) {
            return "error";
        } else {
            Utente p = new Utente();
            p.setCodiceFiscale(item.getCodiceFiscale());

            ArrayList<String> pswAndSalt = new ArrayList<>();
            pswAndSalt = hashPassword(item.getPassword());
            p.setPassword(pswAndSalt.get(0));
            p.setSalt(pswAndSalt.get(1));;

            repo.save(p);
        }

        return "redirect:utenti";
    }

    //serve un json per consultare la table Utente
    @GetMapping(path="/all")
    public @ResponseBody
    Iterable<Utente> getAllUsers() {
        // This returns a JSON or XML with the users
        return repo.findAll();
    }

    //svuota la tabella Utente e serve un json per consultarla
    @GetMapping(path="/clean")
    public @ResponseBody Iterable<Utente> removeAllUsers() {
        // This returns a JSON or XML with the users
        repo.deleteAll();
        return repo.findAll();
    }

    //fa un inserimento predeterminato di prova e segue il rehashing della password con il Logger,
    //poi serve un json per consultare la tabella Utente
    @GetMapping(path="/add")
    public @ResponseBody Iterable<Utente> add () {
        Utente p = new Utente();
        p.setCodiceFiscale("DWNRRT85E18I483W");

        ArrayList<String> pswAndSalt = new ArrayList<>();
        pswAndSalt = hashPassword("40fac301569aec43046e2145dd819b8eaf97372604efbec966f5901e137ce9206410f8612d7c7a04b57b9e65af22dc1de6489b27fd9aee5c804915519c6233e6");
        p.setPassword(pswAndSalt.get(0));
        p.setSalt(pswAndSalt.get(1));;
        log.info("salt set " + p.getSalt());
        log.info("pass set " + (p.getPassword()));

        repo.save(p);

        Utente retrieved = new Utente();
        retrieved = repo.findByCodiceFiscale("DWNRRT85E18I483W");
        log.info("salt retrieved " + retrieved.getSalt());
        String insertedPsw = hashPassword("40fac301569aec43046e2145dd819b8eaf97372604efbec966f5901e137ce9206410f8612d7c7a04b57b9e65af22dc1de6489b27fd9aee5c804915519c6233e6", retrieved.getSalt());
        log.info("pass retrieved " + retrieved.getPassword());
        log.info("pass inserted " + insertedPsw);

        return repo.findAll();
    }

    @GetMapping(path="/trysignup")
    public void trySignUp() {
        Utente user = new Utente();
        user.setPassword("deb386df89dd278f34e5222a7742bacff8f86bd3a4eb3386357bece118d6022f37c4bfef16a72c002c8e3362721caedc1af7ebc685c734a6c96aac5da3608817");
        user.setCodiceFiscale("PNTDNC03D08D969G");
        user.setAuthorities("ROLE_PAZIENTE");
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        repo.save(user);
    }

}
