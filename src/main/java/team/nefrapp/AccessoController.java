package team.nefrapp;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Controller
public class AccessoController {
    @RequestMapping(value="/login", method = RequestMethod.POST)
    public String login(@ModelAttribute("login") @Valid FormItem item, BindingResult result) {
        System.out.println("username:" + item.getCodiceFiscale() +
                "\npassword:" + item.getPassword());
        if (result.hasErrors()) {
            return "paginaErrore";
        }
        else {
            //confronto e prelievo dal db
            //operazioni di sessione
            return "dashboard";
        }
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
