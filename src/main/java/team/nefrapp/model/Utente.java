package team.nefrapp.model;

import org.hibernate.annotations.Type;

import java.lang.String;
import java.time.LocalDate;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="authorities", discriminatorType = DiscriminatorType.STRING)
public class Utente {
    @Id
    private String codiceFiscale;
    private String password;
    @Column(insertable=false, updatable = false)
    private String authorities;
    private String nome;
    private String cognome;
    private String sesso;
    private LocalDate dataNascita;
    private String luogoNascita;
    private String codiceResidenza;
    private String codiceRecapito;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    public void setCodiceFiscale(String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }

    public String getNome() {
        return nome;
    }

    public String getAuthorities() {
        return authorities;
    }

    public void setAuthorities(String authorities) {
        this.authorities = authorities;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getSesso() {
        return sesso;
    }

    public void setSesso(String sesso) {
        this.sesso = sesso;
    }

    public LocalDate getDataNascita() {
        return dataNascita;
    }

    public void setDataNascita(LocalDate dataNascita) {
        this.dataNascita = dataNascita;
    }

    public String getLuogoNascita() {
        return luogoNascita;
    }

    public void setLuogoNascita(String luogoNascita) {
        this.luogoNascita = luogoNascita;
    }

    public String getCodiceResidenza() {
        return codiceResidenza;
    }

    public void setCodiceResidenza(String codiceResidenza) {
        this.codiceResidenza = codiceResidenza;
    }

    public String getCodiceRecapito() {
        return codiceRecapito;
    }

    public void setCodiceRecapito(String codiceRecapito) {
        this.codiceRecapito = codiceRecapito;
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

