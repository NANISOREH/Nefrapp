package team.nefrapp.resources;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Medico")
public class Medico {
	
	@Id
	@Column(name="codiceFiscale")
	private String codiceFiscale;
	@Column(name="nome")
	private String nome;
	@Column(name="cognome")
	private String cognome;
	@Column(name="sesso")
	private String sesso;
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
	
	
	public Medico() {
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
	@Override
	public String toString() {
		return "Medico [codiceFiscale=" + codiceFiscale + ", nome=" + nome + ", cognome=" + cognome + ", sesso="
				+ sesso + ", password=" + password + ", dataNascita=" + dataNascita
				+ ", luogoNascita=" + luogoNascita + ", codiceResidenza=" + codiceResidenza + ", codiceRecapito="
				+ codiceRecapito + "]";
	}

}
