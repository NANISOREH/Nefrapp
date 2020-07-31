package nefrapp.site.model;

import java.util.HashSet;
import java.util.Set;

public class Medico extends Utente {
    public Medico() {
        super();
    }

    private Set<Paziente> seguiti = new HashSet<>();

    public Set<Paziente> getSeguiti() {
        return seguiti;
    }

    public void setSeguiti(Set<Paziente> seguiti) {
        this.seguiti = seguiti;
    }
}
