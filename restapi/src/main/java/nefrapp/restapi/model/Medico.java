package nefrapp.restapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
@DiscriminatorValue("ROLE_MEDICO")
public class Medico extends Utente {
    public Medico() {
        super();
    }

    @ManyToMany(mappedBy = "curanti")
    @JsonIgnoreProperties("curanti")
    private Set<Paziente> seguiti = new HashSet<>();

    public Set<Paziente> getSeguiti() {
        return seguiti;
    }

    public void setSeguiti(Set<Paziente> seguiti) {
        this.seguiti = seguiti;
    }
}
