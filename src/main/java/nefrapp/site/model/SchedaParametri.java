package nefrapp.site.model;
import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

public class SchedaParametri {

	private String codiceSchedaParametri;
	private Integer pressioneMinima;
	private Integer pressioneMassima;
	private BigDecimal peso;
	private Integer scaricoIniziale;
	private Integer carico;
	private Integer scarico;
	private Integer uf;
	private Integer tempoSostaOre;
	private Integer tempoSostaMinuti;
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
