package team.nefrapp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import team.nefrapp.repository.UtenteRepository;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

@Entity
@DiscriminatorValue("ROLE_PAZIENTE")
public class Paziente extends Utente {

	private Boolean isAttivo;
	private String codicePianoTerapeutico;
	@ManyToMany(cascade={CascadeType.ALL}, fetch=FetchType.LAZY)
	@JoinTable(
			joinColumns = {@JoinColumn(name = "codiceFiscale", nullable = false)}
	)
	@JsonIgnoreProperties("seguiti")
	private Set<Medico> curanti = new HashSet<>();

	public Set<Medico> getCuranti() {
		return curanti;
	}

	public void setCuranti(Set<Medico> curanti) {
		this.curanti = curanti;
	}
	
	public Paziente() {
		super();
	}
	
	public String getCodicePianoTerapeutico() {
		return codicePianoTerapeutico;
	}

	public void setCodicePianoTerapeutico(String codicePianoTerapeutico) {
		this.codicePianoTerapeutico = codicePianoTerapeutico;
	}

	public Boolean getAttivo() {
		return isAttivo;
	}

	public void setAttivo(Boolean attivo) {
		isAttivo = attivo;
	}

	@Override
	public String toString() {
		return super.toString() + "Paziente{" +
				"isAttivo=" + isAttivo +
				", codicePianoTerapeutico='" + codicePianoTerapeutico + '\'' +
				'}';
	}
}
