package team.nefrapp.resources;
import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="SchedaParametri")
public class SchedaParametri {

	@Id
	@GeneratedValue
	@Column(name="codiceSchedaParametri")
	private String codiceSchedaParametri;
	@Column(name="pressioneMinima")
	private Integer pressioneMinima;
	@Column(name="pressioneMassima")
	private Integer pressioneMassima;
	@Column(name="peso")
	private BigDecimal peso;
	@Column(name="scaricoIniziale")
	private Integer scaricoIniziale;
	@Column(name="carico")
	private Integer carico;
	@Column(name="scarico")
	private Integer scarico;
	@Column(name="uf")
	private Integer uf;
	@Column(name="tempoSostaOre")
	private Integer tempoSostaOre;
	@Column(name="tempoSostaMinuti")
	private Integer tempoSostaMinuti;
	@Column(name="data")
	private LocalDate data;
	
	
	
	public SchedaParametri() {
		super();
	}
	public String getCodiceSchedaParametri() {
		return codiceSchedaParametri;
	}
	public void setCodiceSchedaParametri(String codiceSchedaParametri) {
		this.codiceSchedaParametri = codiceSchedaParametri;
	}
	public Integer getPressioneMinima() {
		return pressioneMinima;
	}
	public void setPressioneMinima(Integer pressioneMinima) {
		this.pressioneMinima = pressioneMinima;
	}
	public Integer getPressioneMassima() {
		return pressioneMassima;
	}
	public void setPressioneMassima(Integer pressioneMassima) {
		this.pressioneMassima = pressioneMassima;
	}
	public BigDecimal getPeso() {
		return peso;
	}
	public void setPeso(BigDecimal peso) {
		this.peso = peso;
	}
	public Integer getScaricoIniziale() {
		return scaricoIniziale;
	}
	public void setScaricoIniziale(Integer scaricoIniziale) {
		this.scaricoIniziale = scaricoIniziale;
	}
	public Integer getCarico() {
		return carico;
	}
	public void setCarico(Integer carico) {
		this.carico = carico;
	}
	public Integer getScarico() {
		return scarico;
	}
	public void setScarico(Integer scarico) {
		this.scarico = scarico;
	}
	public Integer getUf() {
		return uf;
	}
	public void setUf(Integer uf) {
		this.uf = uf;
	}
	public Integer getTempoSostaOre() {
		return tempoSostaOre;
	}
	public void setTempoSostaOre(Integer tempoSostaOre) {
		this.tempoSostaOre = tempoSostaOre;
	}
	public Integer getTempoSostaMinuti() {
		return tempoSostaMinuti;
	}
	public void setTempoSostaMinuti(Integer tempoSostaMinuti) {
		this.tempoSostaMinuti = tempoSostaMinuti;
	}
	public LocalDate getData() {
		return data;
	}
	public void setData(LocalDate data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "SchedaParametri [codiceSchedaParametri=" + codiceSchedaParametri + ", pressioneMinima="
				+ pressioneMinima + ", pressioneMassima=" + pressioneMassima + ", peso=" + peso + ", scaricoIniziale="
				+ scaricoIniziale + ", carico=" + carico + ", scarico=" + scarico + ", uf=" + uf + ", tempoSostaOre="
				+ tempoSostaOre + ", tempoSostaMinuti=" + tempoSostaMinuti + ", data=" + data + "]";
	}
	
	
}
