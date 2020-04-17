package team.nefrapp.controller;

import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import team.nefrapp.entity.Utente;
import team.nefrapp.repository.UtenteRepository;

import java.util.ArrayList;
import java.util.logging.Logger;

import static team.nefrapp.security.PasswordManager.hashPassword;

@Controller
public class ProvaDb {
    @Autowired
    private UtenteRepository repo;
    Logger log = Logger.getLogger("pdb");

    //codice per consultazione di prova
    @GetMapping(path="/all")
    public @ResponseBody
    Iterable<Utente> getAllUsers() {
        // This returns a JSON or XML with the users
        return repo.findAll();
    }

    //codice per pulizia di prova
    @GetMapping(path="/clean")
    public @ResponseBody Iterable<Utente> removeAllUsers() {
        // This returns a JSON or XML with the users
        repo.deleteAll();
        return repo.findAll();
    }

    //codice per inserimenti di prova
    @GetMapping(path="/add")
    public @ResponseBody Iterable<Utente> add () {
        Utente p = new Utente();
        p.setCodiceFiscale("DWNRRT85E18I483W");

        ArrayList<byte[]> pswAndSalt = new ArrayList<>();
        pswAndSalt = hashPassword("faba08d4ec856cbb8efbd2db63e6e2d5a0061f87cddfc5238fd7382d140a16801e9486927dcde888aeacbf15392dbd6408f6171fe04967ee8f48b33b00e14d99");
        p.setPassword(pswAndSalt.get(0));
        p.setSalt(pswAndSalt.get(1));;
        log.info("salt set " + Hex.encodeHexString(p.getSalt()));
        log.info("pass set " + Hex.encodeHexString(p.getPassword()));

        repo.save(p);

        Utente retrieved = new Utente();
        retrieved = repo.findByCodiceFiscale("DWNRRT85E18I483W");
        log.info("salt retrieved " + Hex.encodeHexString(retrieved.getSalt()));
        byte[] insertedPsw = hashPassword("faba08d4ec856cbb8efbd2db63e6e2d5a0061f87cddfc5238fd7382d140a16801e9486927dcde888aeacbf15392dbd6408f6171fe04967ee8f48b33b00e14d99", retrieved.getSalt());
        log.info("pass retrieved " + Hex.encodeHexString(retrieved.getPassword()));
        log.info("pass inserted " + Hex.encodeHexString(insertedPsw));

        return repo.findAll();
    }
}
