package team.nefrapp.model;

import team.nefrapp.repository.UtenteRepository;

import java.time.LocalDate;

import javax.persistence.*;

@Entity
@DiscriminatorValue("ROLE_PAZIENTE")
public class Paziente extends Utente {

	private Boolean isAttivo;
	private String codicePianoTerapeutico;
	
	
	public Paziente() {
		super();
	}

	public Boolean getIsAttivo() {
		return isAttivo;
	}
	public void setIsAttivo(Boolean isAttivo) {
		this.isAttivo = isAttivo;
	}
	
	public String getCodicePianoTerapeutico() {
		return codicePianoTerapeutico;
	}
	public void setCodicePianoTerapeutico(String codicePianoTerapeutico) {
		this.codicePianoTerapeutico = codicePianoTerapeutico;
	}

	@Override
	public String toString() {
		return super.toString() + "Paziente{" +
				"isAttivo=" + isAttivo +
				", codicePianoTerapeutico='" + codicePianoTerapeutico + '\'' +
				'}';
	}
}
