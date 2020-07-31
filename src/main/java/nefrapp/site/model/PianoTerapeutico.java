package nefrapp.site.model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

public class PianoTerapeutico {

	private String codicePianoTerapeutico;
	private String diagnosi;
	private String terapia;
	private Boolean visualizzato;
	
	
	public PianoTerapeutico() {
		super();
	}
	public String getCodicePianoTerapeutico() {
		return codicePianoTerapeutico;
	}
	public void setCodicePianoTerapeutico(String codicePianoTerapeutico) {
		this.codicePianoTerapeutico = codicePianoTerapeutico;
	}
	public String getDiagnosi() {
		return diagnosi;
	}
	public void setDiagnosi(String diagnosi) {
		this.diagnosi = diagnosi;
	}
	public String getTerapia() {
		return terapia;
	}
	public void setTerapia(String terapia) {
		this.terapia = terapia;
	}
	public Boolean getVisualizzato() {
		return visualizzato;
	}
	public void setVisualizzato(Boolean visualizzato) {
		this.visualizzato = visualizzato;
	}
	@Override
	public String toString() {
		return "PianoTerapeutico [codicePianoTerapeutico=" + codicePianoTerapeutico + ", diagnosi=" + diagnosi
				+ ", terapia=" + terapia + ", visualizzato=" + visualizzato + "]";
	}
	

}
