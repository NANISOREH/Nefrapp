package team.nefrapp.resources;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="segue")
public class Segue {
	
	@Id
	@Column(name="codiceFiscalePaziente")
	private String codiceFiscalePaziente;
	@Column(name="codiceFiscaleMedico")
	private String codiceFiscaleMedico;
	
	
	public Segue() {
		super();
	}
	public String getCodiceFiscalePaziente() {
		return codiceFiscalePaziente;
	}
	public void setCodiceFiscalePaziente(String codiceFiscalePaziente) {
		this.codiceFiscalePaziente = codiceFiscalePaziente;
	}
	public String getCodiceFiscaleMedico() {
		return codiceFiscaleMedico;
	}
	public void setCodiceFiscaleMedico(String codiceFiscaleMedico) {
		this.codiceFiscaleMedico = codiceFiscaleMedico;
	}
	@Override
	public String toString() {
		return "Segue [codiceFiscalePaziente=" + codiceFiscalePaziente + ", codiceFiscaleMedico=" + codiceFiscaleMedico
				+ "]";
	}
	
	
}
