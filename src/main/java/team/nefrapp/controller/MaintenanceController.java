package team.nefrapp.controller;

import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import team.nefrapp.entity.Utente;
import team.nefrapp.formdata.LoginForm;
import team.nefrapp.repository.UtenteRepository;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.logging.Logger;

import static team.nefrapp.security.PasswordManager.hashPassword;

@Controller
public class MaintenanceController {
    @Autowired
    private UtenteRepository repo;
    Logger log = Logger.getLogger("pdb");

    //serve la pagina addUser.jsp, da localhost:8080/addUser
    @GetMapping(path="/addUser")
    public String addUser() {
        return "addUser";
    }
    //aggiunge un utente i cui dati sono stati inseriti da localhost:8080/addUser
    @PostMapping(path="/addUser")
    public String addUser(@ModelAttribute("addUser") @Valid LoginForm item, BindingResult result) {
        if (result.hasErrors()) {
            return "error";
        } else {
            Utente p = new Utente();
            p.setCodiceFiscale(item.getCodiceFiscale());

            ArrayList<byte[]> pswAndSalt = new ArrayList<>();
            pswAndSalt = hashPassword(item.getPassword());
            p.setPassword(pswAndSalt.get(0));
            p.setSalt(pswAndSalt.get(1));;
            log.info("salt set " + Hex.encodeHexString(p.getSalt()));
            log.info("pass set " + Hex.encodeHexString(p.getPassword()));

            repo.save(p);
        }

        return "redirect:all";
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

        ArrayList<byte[]> pswAndSalt = new ArrayList<>();
        pswAndSalt = hashPassword("40fac301569aec43046e2145dd819b8eaf97372604efbec966f5901e137ce9206410f8612d7c7a04b57b9e65af22dc1de6489b27fd9aee5c804915519c6233e6");
        p.setPassword(pswAndSalt.get(0));
        p.setSalt(pswAndSalt.get(1));;
        log.info("salt set " + Hex.encodeHexString(p.getSalt()));
        log.info("pass set " + Hex.encodeHexString(p.getPassword()));

        repo.save(p);

        Utente retrieved = new Utente();
        retrieved = repo.findByCodiceFiscale("DWNRRT85E18I483W");
        log.info("salt retrieved " + Hex.encodeHexString(retrieved.getSalt()));
        byte[] insertedPsw = hashPassword("40fac301569aec43046e2145dd819b8eaf97372604efbec966f5901e137ce9206410f8612d7c7a04b57b9e65af22dc1de6489b27fd9aee5c804915519c6233e6", retrieved.getSalt());
        log.info("pass retrieved " + Hex.encodeHexString(retrieved.getPassword()));
        log.info("pass inserted " + Hex.encodeHexString(insertedPsw));

        return repo.findAll();
    }
}
