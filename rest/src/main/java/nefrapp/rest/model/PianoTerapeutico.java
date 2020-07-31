package nefrapp.rest.model;

import javax.persistence.*;

@Entity
@Table(name="PianoTerapeutico")
public class PianoTerapeutico {
	
	@Id
	@GeneratedValue
	@Column(name="codicePianoTerapeutico")
	private String codicePianoTerapeutico;
	@Column(name="diagnosi")
	private String diagnosi;
	@Column(name="terapia")
	private String terapia;
	@Column(name="visualizzato")
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
