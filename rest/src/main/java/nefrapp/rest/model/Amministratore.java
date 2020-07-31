package nefrapp.rest.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

//non so ancora quali campi bisogna che abbia, l'ho aggiunto coi soli campi di Utente per provare il discriminator
@Entity
@DiscriminatorValue("ROLE_ADMIN")
public class Amministratore extends Utente {
    public Amministratore() {
        super();
    }

}
