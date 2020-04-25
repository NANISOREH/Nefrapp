package team.nefrapp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@DiscriminatorValue("ROLE_MEDICO")
public class Medico extends Utente{
    public Medico() {super();}

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
