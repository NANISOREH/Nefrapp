package team.nefrapp.formdata;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class LoginForm {
    @NotNull
    @Size(min=16, max=16)
    @Pattern(regexp = "^[a-zA-Z]{6}[0-9]{2}[abcdehlmprstABCDEHLMPRST]{1}[0-9]{2}([a-zA-Z]{1}[0-9]{3})[a-zA-Z]{1}$",
            message = "Inserire un Codice Fiscale valido")
    private String codiceFiscale;

    @NotNull
    @Size(min=128, max=128)
    private String password;

    public LoginForm(String u, String p) {
        codiceFiscale = u;
        password = p;
    }

    public LoginForm(){}

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
