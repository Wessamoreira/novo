package webservice.nfse.anapolis;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("tc:Servico")
public class Servico {

	@XStreamAlias("tc:Valores")
	private Valores Valores;
	
	@XStreamAlias("tc:ItemListaServico")
	private String ItemListaServico;
	
	@XStreamAlias("tc:CodigoCnae")
	private String CodigoCnae;
	
	@XStreamAlias("tc:CodigoTributacaoMunicipio")
	private String CodigoTributacaoMunicipio;
	
	@XStreamAlias("tc:Discriminacao")
	private String Discriminacao;
	
	@XStreamAlias("tc:MunicipioPrestacaoServico")
	private String MunicipioPrestacaoServico;
	
	@XStreamAlias("tc:ExigibilidadeISS")
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

	public String getItemListaServico() {
		return ItemListaServico;
	}

	public void setItemListaServico(String itemListaServico) {
		ItemListaServico = itemListaServico;
	}

	public String getCodigoCnae() {
		return CodigoCnae;
	}

	public void setCodigoCnae(String codigoCnae) {
		CodigoCnae = codigoCnae;
	}

	public String getDiscriminacao() {
		return Discriminacao;
	}

	public void setDiscriminacao(String discriminacao) {
		Discriminacao = discriminacao;
	}

	public String getMunicipioPrestacaoServico() {
		if (MunicipioPrestacaoServico == null) {
			MunicipioPrestacaoServico = "";
		}
		return MunicipioPrestacaoServico;
	}

	public void setMunicipioPrestacaoServico(String municipioPrestacaoServico) {
		MunicipioPrestacaoServico = municipioPrestacaoServico;
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
		return "Servico [Valores=" + Valores + ", ItemListaServico=" + ItemListaServico + ", CodigoCnae=" + CodigoCnae + ", CodigoTributacaoMunicipio=" + CodigoTributacaoMunicipio + ", Discriminacao=" + Discriminacao + ", MunicipioPrestacaoServico=" + MunicipioPrestacaoServico + ", exigibilidadeISS=" + exigibilidadeISS + ", MunicipioIncidencia=" + MunicipioIncidencia + "]";
	}
}