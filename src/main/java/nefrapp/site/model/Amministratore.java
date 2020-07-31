package nefrapp.site.model;

import javax.persistence.*;

//non so ancora quali campi bisogna che abbia, l'ho aggiunto coi soli campi di Utente per provare il discriminator
public class Amministratore extends Utente {
    public Amministratore() {
        super();
    }

}
