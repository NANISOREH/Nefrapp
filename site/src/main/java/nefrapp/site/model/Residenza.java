package nefrapp.site.model;

public class Residenza {

    private String codiceResidenza;
    private String via;
    private String numeroCivico;
    private String città;
    private Integer cap;


    public Residenza() {
        super();
    }

    public String getCodiceResidenza() {
        return codiceResidenza;
    }

    public void setCodiceResidenza(String codiceResidenza) {
        this.codiceResidenza = codiceResidenza;
    }

    public String getVia() {
        return via;
    }

    public void setVia(String via) {
        this.via = via;
    }

    public String getNumeroCivico() {
        return numeroCivico;
    }

    public void setNumeroCivico(String numeroCivico) {
        this.numeroCivico = numeroCivico;
    }

    public String getCittà() {
        return città;
    }

    public void setCittà(String città) {
        this.città = città;
    }

    public Integer getCap() {
        return cap;
    }

    public void setCap(Integer cap) {
        this.cap = cap;
    }

    @Override
    public String toString() {
        return "Residenza [codiceResidenza=" + codiceResidenza + ", via=" + via + ", numeroCivico=" + numeroCivico
                + ", città=" + città + ", cap=" + cap + "]";
    }


}
