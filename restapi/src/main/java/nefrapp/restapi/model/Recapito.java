package nefrapp.restapi.model;

import javax.persistence.*;

@Entity
@Table(name = "Recapito")
public class Recapito {

    @Id
    @GeneratedValue
    @Column(name = "codiceRecapito")
    private String codiceRecapito;
    @Column(name = "telefono")
    private String telefono;
    @Column(name = "cellulare")
    private String cellulare;
    @Column(name = "email")
    private String email;

    public Recapito() {
        super();
    }

    public String getCodiceRecapito() {
        return codiceRecapito;
    }

    public void setCodiceRecapito(String codiceRecapito) {
        this.codiceRecapito = codiceRecapito;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCellulare() {
        return cellulare;
    }

    public void setCellulare(String cellulare) {
        this.cellulare = cellulare;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Recapito [codiceRecapito=" + codiceRecapito + ", telefono=" + telefono + ", cellulare=" + cellulare
                + ", email=" + email + "]";
    }

}
