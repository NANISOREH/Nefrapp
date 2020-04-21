package team.nefrapp.entity;

import java.lang.String;

import javax.persistence.Entity;
import javax.persistence.Id;

//Entity di prova
@Entity
public class Utente {
    @Id
    private String codiceFiscale;
    private String password;
    private String salt;
    private String authorities;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getAuthorities() {
        return authorities;
    }

    public void setAuthorities(String authorities) {
        this.authorities = authorities;
    }

    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    public void setCodiceFiscale(String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }

    public Utente() {
    }

    @Override
    public String toString() {
        return "Paziente{" +
                "codiceFiscale='" + codiceFiscale + '\'' +
                ", password='" + password + '\'' +
                ", salt='" + salt + '\'' +
                '}';
    }
}