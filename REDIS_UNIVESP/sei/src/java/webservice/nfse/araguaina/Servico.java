package webservice.nfse.araguaina;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class Servico {

	@XStreamAlias("Valores")
	private Valores Valores;

	@XStreamAlias("IssRetido")
	private SimNao IssRetido;

	@XStreamAlias("ItemListaServico")
	private String ItemListaServico;

	@XStreamAlias("CodigoCnae")
	private Integer CodigoCnae;

	@XStreamAlias("CodigoTributacaoMunicipio")
	private String CodigoTributacaoMunicipio;

	@XStreamAlias("Discriminacao")
	private String Discriminacao;

	@XStreamAlias("CodigoMunicipio")
	private String CodigoMunicipio;

	@XStreamAlias("CodigoPais")
	protected String codigoPais;
	
	@XStreamAlias("ExigibilidadeISS")
	private ExigibilidadeISS exigibilidadeISS;

	@XStreamAlias("MunicipioIncidencia")
	private Integer MunicipioIncidencia;

	public Servico() {
		Valores = new Valores();
	}

	public Valores getValores() {
		return Valores;
	}

	public void setValores(Valores valores) {
		Valores = valores;
	}

	public SimNao getIssRetido() {
		return IssRetido;
	}

	public void setIssRetido(SimNao issRetido) {
		IssRetido = issRetido;
	}

	public String getItemListaServico() {
		return ItemListaServico;
	}

	public void setItemListaServico(String itemListaServico) {
		ItemListaServico = itemListaServico;
	}

	public Integer getCodigoCnae() {
		return CodigoCnae;
	}

	public void setCodigoCnae(Integer codigoCnae) {
		CodigoCnae = codigoCnae;
	}

	public String getDiscriminacao() {
		return Discriminacao;
	}

	public void setDiscriminacao(String discriminacao) {
		Discriminacao = discriminacao;
	}

	public String getCodigoMunicipio() {
		return CodigoMunicipio;
	}

	public void setCodigoMunicipio(String codigoMunicipio) {
		CodigoMunicipio = codigoMunicipio;
	}

	public ExigibilidadeISS getExigibilidadeISS() {
		return exigibilidadeISS;
	}

	public void setExigibilidadeISS(ExigibilidadeISS exigibilidadeISS) {
		this.exigibilidadeISS = exigibilidadeISS;
	}

	public Integer getMunicipioIncidencia() {
		return MunicipioIncidencia;
	}

	public void setMunicipioIncidencia(Integer municipioIncidencia) {
		MunicipioIncidencia = municipioIncidencia;
	}

	public String getCodigoPais() {
		return codigoPais;
	}

	public void setCodigoPais(String codigoPais) {
		this.codigoPais = codigoPais;
	}

	/**
	 * @return the codigoTributacaoMunicipio
	 */
	public String getCodigoTributacaoMunicipio() {
		return CodigoTributacaoMunicipio;
	}

	/**
	 * @param codigoTributacaoMunicipio
	 *            the codigoTributacaoMunicipio to set
	 */
	public void setCodigoTributacaoMunicipio(String codigoTributacaoMunicipio) {
		CodigoTributacaoMunicipio = codigoTributacaoMunicipio;
	}

	@Override
	public String toString() {
		return "Servico [Valores=" + Valores + ", codigoPais=" + codigoPais + ", IssRetido=" + IssRetido + ", ItemListaServico=" + ItemListaServico + ", CodigoCnae=" + CodigoCnae + ", CodigoTributacaoMunicipio=" + CodigoTributacaoMunicipio + ", Discriminacao=" + Discriminacao + ", CodigoMunicipio=" + CodigoMunicipio + ", exigibilidadeISS=" + exigibilidadeISS + ", MunicipioIncidencia=" + MunicipioIncidencia + "]";
	}

}