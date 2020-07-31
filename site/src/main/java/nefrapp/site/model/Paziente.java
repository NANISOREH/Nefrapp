package nefrapp.site.model;

import java.util.Set;


public class Paziente extends Utente {

    private Boolean isAttivo;
    private String codicePianoTerapeutico;
    private Set<Medico> curanti;

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
