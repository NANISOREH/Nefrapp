package team.nefrapp.controller;

import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import team.nefrapp.entity.Paziente;
import team.nefrapp.repository.PazienteRepository;

import java.util.ArrayList;
import java.util.logging.Logger;

import static team.nefrapp.security.PasswordManager.hashPassword;

@Controller
public class ProvaDb {
    @Autowired
    private PazienteRepository repo;
    Logger log = Logger.getLogger("pdb");

    //codice per inserimenti di prova
    @GetMapping(path="/add")
    public @ResponseBody Iterable<Paziente> addUsers () {
        Paziente p = new Paziente();
        p.setCodiceFiscale("DWNRRT85E18I483W");

        ArrayList<byte[]> pswAndSalt = new ArrayList<>();
        pswAndSalt = hashPassword("password");
        p.setPassword(pswAndSalt.get(0));
        p.setSalt(pswAndSalt.get(1));

        repo.save(p);
        return repo.findAll();
    }

    //codice per consultazione di prova
    @GetMapping(path="/all")
    public @ResponseBody
    Iterable<Paziente> getAllUsers() {
        // This returns a JSON or XML with the users
        return repo.findAll();
    }

    //codice per pulizia di prova
    @GetMapping(path="/clean")
    public @ResponseBody Iterable<Paziente> removeAllUsers() {
        // This returns a JSON or XML with the users
        repo.deleteAll();
        return repo.findAll();
    }

    //codice per inserimenti di prova
    @GetMapping(path="/hash")
    public @ResponseBody Iterable<Paziente> hashTry () {
        Paziente p = new Paziente();
        p.setCodiceFiscale("DWNRRT85E18I483W");

        ArrayList<byte[]> pswAndSalt = new ArrayList<>();
        pswAndSalt = hashPassword("password");
        p.setPassword(pswAndSalt.get(0));
        p.setSalt(pswAndSalt.get(1));;
        log.info("salt set " + Hex.encodeHexString(p.getSalt()));
        log.info("pass set " + Hex.encodeHexString(p.getPassword()));

        repo.save(p);

        Paziente retrieved = new Paziente();
        retrieved = repo.findByCodiceFiscale("DWNRRT85E18I483W");
        log.info("salt retrieved " + Hex.encodeHexString(retrieved.getSalt()));
        byte[] insertedPsw = hashPassword("password", retrieved.getSalt());
        log.info("pass retrieved " + Hex.encodeHexString(retrieved.getPassword()));
        log.info("pass inserted " + Hex.encodeHexString(insertedPsw));

        return repo.findAll();
    }
}
