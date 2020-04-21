package team.nefrapp.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Paziente")
public class Paziente {
	
	@Id
	@Column(name="codiceFiscale")
	private String codiceFiscale;
	@Column(name="nome")
	private String nome;
	@Column(name="cognome")
	private String cognome;
	@Column(name="sesso")
	private String sesso;
	@Column(name="isAttivo")
	private Boolean isAttivo;
	@Column(name="password")
	private String password;
	@Column(name="dataNascita")
	private LocalDate dataNascita;
	@Column(name="luogoNascita")
	private String luogoNascita;
	@Column(name="codiceResidenza")
	private String codiceResidenza;
	@Column(name="codiceRecapito")
	private String codiceRecapito;
	@Column(name="codicePianoTerapeutico")
	private String codicePianoTerapeutico;
	
	
	public Paziente() {
		super();
	}
	public String getCodiceFiscale() {
		return codiceFiscale;
	}
	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCognome() {
		return cognome;
	}
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	public String getSesso() {
		return sesso;
	}
	public void setSesso(String sesso) {
		this.sesso = sesso;
	}
	public Boolean getIsAttivo() {
		return isAttivo;
	}
	public void setIsAttivo(Boolean isAttivo) {
		this.isAttivo = isAttivo;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public LocalDate getDataNascita() {
		return dataNascita;
	}
	public void setDataNascita(LocalDate dataNascita) {
		this.dataNascita = dataNascita;
	}
	public String getLuogoNascita() {
		return luogoNascita;
	}
	public void setLuogoNascita(String luogoNascita) {
		this.luogoNascita = luogoNascita;
	}
	public String getCodiceResidenza() {
		return codiceResidenza;
	}
	public void setCodiceResidenza(String codiceResidenza) {
		this.codiceResidenza = codiceResidenza;
	}
	public String getCodiceRecapito() {
		return codiceRecapito;
	}
	public void setCodiceRecapito(String codiceRecapito) {
		this.codiceRecapito = codiceRecapito;
	}
	
	public String getCodicePianoTerapeutico() {
		return codicePianoTerapeutico;
	}
	public void setCodicePianoTerapeutico(String codicePianoTerapeutico) {
		this.codicePianoTerapeutico = codicePianoTerapeutico;
	}
	@Override
	public String toString() {
		return "Paziente [codiceFiscale=" + codiceFiscale + ", nome=" + nome + ", cognome=" + cognome + ", sesso="
				+ sesso + ", isAttivo=" + isAttivo + ", password=" + password + ", dataNascita=" + dataNascita
				+ ", luogoNascita=" + luogoNascita + ", codiceResidenza=" + codiceResidenza + ", codiceRecapito="
				+ codiceRecapito + ", codicePianoTerapeutico=" + codicePianoTerapeutico + "]";
	}
	


}
