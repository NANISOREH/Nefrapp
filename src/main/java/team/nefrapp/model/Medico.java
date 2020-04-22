package team.nefrapp.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("ROLE_MEDICO")
public class Medico extends Utente{
    public Medico() {super();}
}
