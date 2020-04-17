package team.nefrapp.controller;

import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import team.nefrapp.entity.Paziente;
import team.nefrapp.repository.PazienteRepository;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import java.util.logging.Logger;

import static team.nefrapp.security.PasswordManager.hashPassword;

@Controller
public class AccessoController {
    @Autowired
    private PazienteRepository repo;
    Logger log = Logger.getLogger("AccessoController");

    @RequestMapping(value="/login", method = RequestMethod.POST)
    public String login(@ModelAttribute("login") @Valid FormItem item, BindingResult result) {
        if (result.hasErrors()) {
            return "paginaErrore";
        }
        else {
            Paziente p = repo.findByCodiceFiscale(item.getCodiceFiscale());
            if (p != null && p.getCodiceFiscale()!= null) {
                byte[] insertedPsw = hashPassword(item.getPassword(), p.getSalt());
                if (Hex.encodeHexString(insertedPsw).equals(Hex.encodeHexString(p.getPassword()))) {
                    log.info("Accesso effettuato");
                    //gestisci dati di sessione
                    return "dashboard";
                } else {
                    log.info("Accesso negato\n");
                    log.info("pass retrieved " + Hex.encodeHexString(p.getPassword()));
                    log.info("pass inserted " + Hex.encodeHexString(insertedPsw));
                    return "login";
                }
            }
            log.info("utente non trovato");
        }

        return "login";
    }
}

class FormItem {
    @NotNull
    @Size(min=16, max=16)
    @Pattern(regexp = "^[a-zA-Z]{6}[0-9]{2}[abcdehlmprstABCDEHLMPRST]{1}[0-9]{2}([a-zA-Z]{1}[0-9]{3})[a-zA-Z]{1}$",
    message = "Inserire un Codice Fiscale valido")
    private String codiceFiscale;

    @NotNull
    @Size(min=8, max=20, message = "La password deve essere compresa tra 8 e 20 caratteri")
    String password;

    public FormItem(String u, String p) {
        codiceFiscale = u;
        password = p;
    }

    public FormItem(){}

    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    public void setCodiceFiscale(String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
