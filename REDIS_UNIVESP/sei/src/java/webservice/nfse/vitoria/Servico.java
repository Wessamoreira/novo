package webservice.nfse.vitoria;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class Servico {

	private Valores Valores;
	
	private SimNao IssRetido;
	
	private String ItemListaServico;
	
	private Integer CodigoCnae;
	
	private String CodigoTributacaoMunicipio;
	
	private String Discriminacao;
	
	private String CodigoMunicipio;
	
	
	@XStreamAlias("ExigibilidadeISS")
	private ExigibilidadeISS exigibilidadeISS;
	
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
	

	/**
	 * @return the codigoTributacaoMunicipio
	 */
	public String getCodigoTributacaoMunicipio() {		
		return CodigoTributacaoMunicipio;
	}

	/**
	 * @param codigoTributacaoMunicipio the codigoTributacaoMunicipio to set
	 */
	public void setCodigoTributacaoMunicipio(String codigoTributacaoMunicipio) {
		CodigoTributacaoMunicipio = codigoTributacaoMunicipio;
	}

	@Override
	public String toString() {
		return "Servico [Valores=" + Valores + ", IssRetido=" + IssRetido + ", ItemListaServico=" + ItemListaServico + ", CodigoCnae=" + CodigoCnae + ", CodigoTributacaoMunicipio=" + CodigoTributacaoMunicipio + ", Discriminacao=" + Discriminacao + ", CodigoMunicipio=" + CodigoMunicipio + ", exigibilidadeISS=" + exigibilidadeISS + ", MunicipioIncidencia=" + MunicipioIncidencia + "]";
	}
}