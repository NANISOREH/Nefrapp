package team.nefrapp.model;

import org.hibernate.annotations.Type;

import java.lang.String;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Entity
public class Utente {
    @Id
    private String codiceFiscale;
    private String password;
    @Enumerated(EnumType.STRING)
    private Authorities authorities;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAuthorities() {
        return authorities.toString();
    }


    public void setAuthorities(String authorities) {
        this.authorities = Enum.valueOf(Authorities.class, authorities);
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
        return "Utente{" +
                "codiceFiscale='" + codiceFiscale + '\'' +
                ", password='" + password + '\'' +
                ", authorities='" + authorities + '\'' +
                '}';
    }
}

enum Authorities {
    ROLE_PAZIENTE{},

    ROLE_MEDICO{},

    ROLE_ADMIN{}
}

