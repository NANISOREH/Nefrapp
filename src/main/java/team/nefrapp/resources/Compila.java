package team.nefrapp.resources;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Compila")
public class Compila {
	
	@Id
	@Column(name="codiceFiscalePaziente")
	private String codiceFiscalePaziente;
	@Column(name="codiceScheda")
	private String codiceScheda;
	
	
	public Compila() {
		super();
	}
	public String getCodiceFiscalePaziente() {
		return codiceFiscalePaziente;
	}
	public void setCodiceFiscalePaziente(String codiceFiscalePaziente) {
		this.codiceFiscalePaziente = codiceFiscalePaziente;
	}
	public String getCodiceScheda() {
		return codiceScheda;
	}
	public void setCodiceScheda(String codiceScheda) {
		this.codiceScheda = codiceScheda;
	}
	@Override
	public String toString() {
		return "Compila [codiceFiscalePaziente=" + codiceFiscalePaziente + ", codiceScheda=" + codiceScheda + "]";
	}
	
	

}
