package nefrapp.restapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@DiscriminatorValue("PAZIENTE")
public class Paziente extends Utente {

    private Boolean isAttivo;
    private String codicePianoTerapeutico;
    @ManyToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @JoinTable(
            joinColumns = {@JoinColumn(name = "codiceFiscale", nullable = false)}
    )
    @JsonIgnoreProperties("seguiti")
    private Set<Medico> curanti = new HashSet<>();

    public Set<Medico> getCuranti() {
        return curanti;
    }

    public void setCuranti(Set<Medico> curanti) {
        this.curanti = curanti;
    }

    public Paziente() {
        super();
    }

    public String getCodicePianoTerapeutico() {
        return codicePianoTerapeutico;
    }

    public void setCodicePianoTerapeutico(String codicePianoTerapeutico) {
        this.codicePianoTerapeutico = codicePianoTerapeutico;
    }

    public Boolean getAttivo() {
        return isAttivo;
    }

    public void setAttivo(Boolean attivo) {
        isAttivo = attivo;
    }

    @Override
    public String toString() {
        return super.toString() + "Paziente{" +
                "isAttivo=" + isAttivo +
                ", codicePianoTerapeutico='" + codicePianoTerapeutico + '\'' +
                '}';
    }
}
