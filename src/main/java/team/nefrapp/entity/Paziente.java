package team.nefrapp.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

//Entity di prova
@Entity
public class Paziente{
    @Id
    private String codiceFiscale;
    private byte[] password;
    private byte[] salt;

    public byte[] getPassword() {
        return password;
    }

    public void setPassword(byte[] password) {
        this.password = password;
    }

    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }

    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    public void setCodiceFiscale(String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }

    public Paziente() {
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