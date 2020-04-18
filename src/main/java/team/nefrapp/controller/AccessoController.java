package team.nefrapp.controller;

import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import team.nefrapp.entity.Utente;
import team.nefrapp.formdata.LoginForm;
import team.nefrapp.repository.UtenteRepository;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import java.util.logging.Logger;

import static team.nefrapp.security.PasswordManager.hashPassword;

@Controller
public class AccessoController {
    @Autowired
    private UtenteRepository repo;
    Logger log = Logger.getLogger("AccessoController");

    /*PER SARA: puoi testare l'accesso all'utente barebone di prova con DWNRRT85E18I483W - password. Il server è in remoto.
    * Puoi usare localhost:8080/clean per pulire la table, localhost:8080/add per aggiungere quello stesso utente
    * e controllare in console se l'hashing server-side funziona come dovrebbe e localhost:8080/all per vedere lo stato della table.
    *
    * Siccome ho implementato anche client-side hashing per rendere la password in chiaro inaccessibile anche a noi,
    * se vuoi aggiungere un utente diverso manualmente non va bene inserire nel repository un Utente con una password in chiaro,
    * andrà inserito un valore già hashato.
    * Ho creato un piccolo jsp accessibile con localhost:8080/utenti con cui puoi inserire e rimuovere utenti correttamente.*/

    @RequestMapping(value="/login", method = RequestMethod.POST)
    public String login(@ModelAttribute("login") @Valid LoginForm item, BindingResult result, HttpSession session) {
        if (result.hasErrors()) {
            log.info(result.toString());
            return "error";
        }
        else {
            Utente p = repo.findByCodiceFiscale(item.getCodiceFiscale());
            if (p != null && p.getCodiceFiscale()!= null) {
                byte[] insertedPsw = hashPassword(item.getPassword(), p.getSalt());
                if (Hex.encodeHexString(insertedPsw).equals(Hex.encodeHexString(p.getPassword()))) {
                    log.info("Accesso effettuato");
                    session.setAttribute("isPaziente", true);
                    session.setAttribute("accessDone", true);
                    return "dashboard";
                } else {
                    log.info("Accesso negato\n");
                    return "login";
                }
            }
            log.info("utente non trovato");
        }

        return "login";
    }

    @RequestMapping(value="/logout", method = RequestMethod.POST)
    public String logout (HttpSession session) {
        session.invalidate();
        return "dashboard";
    }
}
