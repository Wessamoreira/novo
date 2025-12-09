package webservice.nfse;

import negocio.comuns.utilitarias.UteisJSF;

public enum WebServicesNFSEEnum {
	
	UBERLANDIA_MG(11,TipoAcaoServicoNFSEEnum.GerarNfse, "http://udigital.uberlandia.mg.gov.br/WsNFe2/LoteRps.jws", "http://udigital.uberlandia.mg.gov.br/WsNFe2/LoteRps.jws?wsdl", 5403, "ReqEnvioLoteRPS.xsd", "http://udigital.uberlandia.mg.gov.br/WsNFe2/LoteRps.jws?wsdl","http://udigital.uberlandia.mg.gov.br/WsNFe2/LoteRps.jws", ""), 
	BELEM_PA_GERAR(4565,TipoAcaoServicoNFSEEnum.GerarNfse, "http://www.issdigitalbel.com.br/WsNFe2/LoteRps.jws", "http://www.issdigitalbel.com.br/WsNFe2/LoteRps.jws?wsdl", 0427, "ReqEnvioLoteRPS.xsd","http://www.issdigitalbel.com.br/WsNFe2/LoteRps.jws?wsdl", "http://www.issdigitalbel.com.br/WsNFe2/LoteRps.jws",""), 
	CAMPO_GRANDE_MS_GERAR(4141,TipoAcaoServicoNFSEEnum.GerarNfse, "http://issdigital.pmcg.ms.gov.br/WsNFe2/LoteRps.jws", "http://issdigital.pmcg.ms.gov.br/WsNFe2/LoteRps.jws?wsdl", 2729, "http://issdigital.pmcg.ms.gov.br/WsNFe2/LoteRps.jws?wsdl", "","http://issdigital.pmcg.ms.gov.br/WsNFe2/LoteRps.jws",""), 
	GOIANIA_GO(1,TipoAcaoServicoNFSEEnum.GerarNfse, "https://nfse.goiania.go.gov.br/ws/nfse.asmx", "http://nfse.goiania.go.gov.br/ws/GerarNfse", 0, "nfse_gyn_v02.xsd","http://nfse.goiania.go.gov.br/ws/GerarNfse", "https://nfse.goiania.go.gov.br/ws/nfse.asmx",""),
	RIO_DE_JANEIRO_RJ_GERAR(7,TipoAcaoServicoNFSEEnum.GerarNfse, "https://notacarioca.rio.gov.br/WSNacional/nfse.asmx", "http://notacarioca.rio.gov.br/GerarNfse", 0, "nfse_pcrj_v01.xsd","http://notacarioca.rio.gov.br/GerarNfse", "https://homologacao.notacarioca.rio.gov.br/WSNacional/nfse.asmx",""),
	RIO_DE_JANEIRO_RJ_CONSULTAR_NFSE(7,TipoAcaoServicoNFSEEnum.ConsultarNfse, "https://notacarioca.rio.gov.br/WSNacional/nfse.asmx", "http://notacarioca.rio.gov.br/ConsultarNfse", 0, "nfse_pcrj_v01.xsd","http://notacarioca.rio.gov.br/ConsultarNfse", "https://homologacao.notacarioca.rio.gov.br/WSNacional/nfse.asmx",""),
	JOAO_PESSOA_PA_GERAR(4964,TipoAcaoServicoNFSEEnum.GerarNfse,"https://sispmjp.joaopessoa.pb.gov.br:8443/sispmjp/NfseWSService", "http://nfse.abrasf.org.br/GerarNfse",0,"nfse v2 02.xsd","http://nfse.abrasf.org.br/GerarNfse","https://nfsehomolog.joaopessoa.pb.gov.br:8443/sispmjp/NfseWSService",""),
	JOAO_PESSOA_PA_CONSULTAR_LOTE_RPS(4964,TipoAcaoServicoNFSEEnum.ConsultarLoteRps, "https://sispmjp.joaopessoa.pb.gov.br:8443/sispmjp/NfseWSService", "http://nfse.abrasf.org.br/ConsultarLoteRps",0,"nfse v2 02.xsd", "http://nfse.abrasf.org.br/ConsultarLoteRps","https://nfsehomolog.joaopessoa.pb.gov.br:8443/sispmjp/NfseWSService",""),
	BELO_HORIZONTE_MG_GERAR(14, TipoAcaoServicoNFSEEnum.GerarNfse, "https://bhissdigital.pbh.gov.br:443/bhiss-ws/nfse", "http://ws.bhiss.pbh.gov.br/GerarNfse", 0, "nfse_bh.xsd", "http://ws.bhiss.pbh.gov.br/GerarNfse","https://bhisshomologa.pbh.gov.br/bhiss-ws/nfse",""),
	CURITIBA_PR_GERAR(6015, TipoAcaoServicoNFSEEnum.GerarNfse, "https://isscuritiba.curitiba.pr.gov.br/Iss.NfseWebService/nfsews.asmx?wsdl", "http://www.e-governeapps2.com.br/RecepcionarLoteRps", 0, "nfse.xsd", "http://www.e-governeapps2.com.br/RecepcionarLoteRps","https://pilotoisscuritiba.curitiba.pr.gov.br/nfse_ws/nfsews.asmx?wsdl",""),
	LONDRINA_PR_GERAR(6268, TipoAcaoServicoNFSEEnum.GerarNfse, "https://iss.londrina.pr.gov.br/ws/v1_03/sigiss_ws.php", "http://iss.londrina.pr.gov.br/ws/v1_03#GerarNota", 0, "","http://iss.londrina.pr.gov.br/ws/v1_03#GerarNota", "http://testeiss.londrina.pr.gov.br/ws/v1_03/sigiss_ws.php",""),
	TERESINA_PI_GERAR(51, TipoAcaoServicoNFSEEnum.GerarNfse, "http://www.issdigitalthe.com.br/WsNFe2/LoteRps.jws", "", 0, "","", "http://www.issdigitalthe.com.br/WsNFe2/LoteRps.jws",""),
	PALMAS_TO_GERAR(9899, TipoAcaoServicoNFSEEnum.GerarNfse, "https://www5.webiss.com.br/palmasto_wsnfse/NfseServices.svc", "http://tempuri.org/INfseServices/RecepcionarLoteRps", 0, "","http://tempuri.org/INfseServices/RecepcionarLoteRps", "https://www5.webiss.com.br/palmasto_wsnfse_homolog/NfseServices.svc",""),
	PALMAS_TO_CONSULTAR_SITUACAO_LOTE(9899, TipoAcaoServicoNFSEEnum.ConsultarSituacaoLoteRps, "https://www5.webiss.com.br/palmasto_wsnfse/NfseServices.svc", "http://tempuri.org/INfseServices/ConsultarSituacaoLoteRps", 0, "","http://tempuri.org/INfseServices/ConsultarSituacaoLoteRps", "https://www5.webiss.com.br/palmasto_wsnfse_homolog/NfseServices.svc",""),
	PALMAS_TO_CONSULTAR_LOTE(9899, TipoAcaoServicoNFSEEnum.ConsultarLoteRps, "https://www5.webiss.com.br/palmasto_wsnfse/NfseServices.svc", "http://tempuri.org/INfseServices/ConsultarLoteRps", 0, "","http://tempuri.org/INfseServices/ConsultarLoteRps", "https://www5.webiss.com.br/palmasto_wsnfse_homolog/NfseServices.svc",""),
	ARAGUAINA_TO(9805, TipoAcaoServicoNFSEEnum.GerarNfse, "https://araguainato.webiss.com.br/ws/nfse.asmx", "http://nfse.abrasf.org.br/GerarNfse", 9241, "nfse v2 02.xsd", "http://nfse.abrasf.org.br/GerarNfse", "https://homologacao.webiss.com.br/ws/nfse.asmx",""),
	PORTO_ALEGRE_RS_GERAR(7994, TipoAcaoServicoNFSEEnum.GerarNfse, "https://nfe.portoalegre.rs.gov.br:443/bhiss-ws/nfse", "http://ws.bhiss.pbh.gov.br/GerarNfse", 0, "nfse_v20_08_2015.xsd","http://ws.bhiss.pbh.gov.br/GerarNfse", "https://nfse-hom.procempa.com.br:443/bhiss-ws/nfse",""),
	NATAL_RN_GERAR(7221, TipoAcaoServicoNFSEEnum.GerarNfse, "https://wsnfsev1.natal.rn.gov.br:8444/axis2/services/NfseWSServiceV1/", "https://wsnfsev1.natal.rn.gov.br:8444/axis2/services/RecepcionarLoteRps", 0, "nfseNatal.xsd","https://wsnfsev1homologacao.natal.rn.gov.br:8443/axis2/services/RecepcionarLoteRps", "https://wsnfsev1homologacao.natal.rn.gov.br:8443/axis2/services/NfseWSServiceV1/",""),
	NATAL_RN_CONSULTAR_SITUACAO_LOTE(7221, TipoAcaoServicoNFSEEnum.ConsultarSituacaoLoteRps, "https://wsnfsev1.natal.rn.gov.br:8444/axis2/services/NfseWSServiceV1/", "https://wsnfsev1.natal.rn.gov.br:8444/axis2/services/ConsultarSituacaoLoteRps", 0, "nfseNatal.xsd","https://wsnfsev1homologacao.natal.rn.gov.br:8443/axis2/services/ConsultarSituacaoLoteRps", "https://wsnfsev1homologacao.natal.rn.gov.br:8443/axis2/services/NfseWSServiceV1/",""),
	NATAL_RN_CONSULTAR_LOTE(7221, TipoAcaoServicoNFSEEnum.ConsultarLoteRps, "https://wsnfsev1.natal.rn.gov.br:8444/axis2/services/NfseWSServiceV1/", "https://wsnfsev1.natal.rn.gov.br:8444/axis2/services/ConsultarLoteRps", 0, "nfseNatal.xsd","https://wsnfsev1homologacao.natal.rn.gov.br:8443/axis2/services/ConsultarLoteRps", "https://wsnfsev1homologacao.natal.rn.gov.br:8443/axis2/services/NfseWSServiceV1/",""),
	MACEIO_AL_GERAR(109, TipoAcaoServicoNFSEEnum.GerarNfse, "https://producao.ginfes.com.br//ServiceGinfesImpl", "", 0, "maceio/servico_enviar_lote_rps_envio_v03.xsd","", "https://homologacao.ginfes.com.br/ServiceGinfesImpl?wsdl",""),
	SAOLUIS_MA(2587,TipoAcaoServicoNFSEEnum.GerarNfse, "http://www.issdigitalslz.com.br:80/WsNFe2/LoteRps", "", 0, "ReqEnvioLoteRPS.xsd","", "http://www.issdigitalslz.com.br:80/WsNFe2/LoteRps",""),
	VITORIA_ES_GERAR(2048,TipoAcaoServicoNFSEEnum.GerarNfse, "https://wsnfse.vitoria.es.gov.br/NotaFiscalService.asmx", "http://www.abrasf.org.br/nfse.xsd/GerarNfse", 0, "vitoria/nfse v2 01.xsd","http://www.abrasf.org.br/nfse.xsd/GerarNfse", "https://wsnfsehomologa.vitoria.es.gov.br/NotaFiscalService.asmx",""),
	SERRA_ES_GERAR(2028,TipoAcaoServicoNFSEEnum.GerarNfse, "", "", 0, "TiposNFSe_v2.0.xsd","", "",""),
	BELEM_PA_CONSULTAR_SITUACAO_LOTE(4565,TipoAcaoServicoNFSEEnum.ConsultarSituacaoLoteRps, "http://www.issdigitalbel.com.br/WsNFe2/LoteRps.jws", "http://www.issdigitalbel.com.br/WsNFe2/LoteRps.jws?wsdl", 0427, "ReqEnvioLoteRPS.xsd","http://www.issdigitalbel.com.br/WsNFe2/LoteRps.jws?wsdl", "http://www.issdigitalbel.com.br/WsNFe2/LoteRps.jws",""), 
	CAMPO_GRANDE_MS_CONSULTAR_SITUACAO_LOTE(4141,TipoAcaoServicoNFSEEnum.ConsultarSituacaoLoteRps, "http://issdigital.pmcg.ms.gov.br/WsNFe2/LoteRps.jws", "http://issdigital.pmcg.ms.gov.br/WsNFe2/LoteRps.jws?wsdl", 2729, "http://issdigital.pmcg.ms.gov.br/WsNFe2/LoteRps.jws?wsdl", "","http://issdigital.pmcg.ms.gov.br/WsNFe2/LoteRps.jws",""),
	ANAPOLIS_GO_GERAR(2065,TipoAcaoServicoNFSEEnum.GerarNfse, "http://www.issnetonline.com.br/webserviceabrasf/anapolis/servicos.asmx", "http://www.issnetonline.com.br/webserviceabrasf/anapolis/servicos.asmx?wsdl", 0, "anapolis/servico_enviar_lote_rps_envio.xsd", "http://www.issnetonline.com.br/webserviceabrasf/homologacao/servicos.asmx?wsdl", "http://www.issnetonline.com.br/webserviceabrasf/homologacao/servicos.asmx","nfseAnapolis"),
	ANAPOLIS_GO_CONSULTAR_SITUACAO_LOTE(2065,TipoAcaoServicoNFSEEnum.ConsultarSituacaoLoteRps, "http://www.issnetonline.com.br/webserviceabrasf/anapolis/servicos.asmx", "http://www.issnetonline.com.br/webserviceabrasf/anapolis/servicos.asmx?wsdl", 0, "anapolis/servico_enviar_lote_rps_envio.xsd", "http://www.issnetonline.com.br/webserviceabrasf/homologacao/servicos.asmx?wsdl", "http://www.issnetonline.com.br/webserviceabrasf/homologacao/servicos.asmx",""),
	ANAPOLIS_GO_CONSULTAR_LOTE(2065,TipoAcaoServicoNFSEEnum.ConsultarLoteRps, "http://www.issnetonline.com.br/webserviceabrasf/anapolis/servicos.asmx", "http://www.issnetonline.com.br/webserviceabrasf/anapolis/servicos.asmx?wsdl", 0, "anapolis/servico_enviar_lote_rps_envio.xsd", "http://www.issnetonline.com.br/webserviceabrasf/homologacao/servicos.asmx?wsdl", "http://www.issnetonline.com.br/webserviceabrasf/homologacao/servicos.asmx",""),
	
	// Webservice tercerizado para NFS-e de todas as cidades
	NFSE_WEBSERVICE(0, null, "", "", 0, "", "", "", "");
	
	private Integer idCidade;
	private String soapAction;
	private String url;
	private Integer codigoCidadePadraoSiafi;
	private String schemaXML; 
	private TipoAcaoServicoNFSEEnum tipoAcaoServicoNFSEEnum;
	private String urlHomologacao;
	 String soapActionHomologacao;
	private String layoutRelatorioString;
	
	WebServicesNFSEEnum(Integer idCidade,TipoAcaoServicoNFSEEnum tipoAcaoServicoNFSEEnum, String url, String soapAction, Integer codigoCidadePadraoSiafi, String schemaXML, String soapActionHomologacao, String urlHomologacao, String layoutRelatorio) {
		this.idCidade = idCidade;
		this.url = url;
		this.soapAction = soapAction;
		this.codigoCidadePadraoSiafi = codigoCidadePadraoSiafi;
		this.schemaXML = schemaXML;
		this.tipoAcaoServicoNFSEEnum = tipoAcaoServicoNFSEEnum;
		this.urlHomologacao = urlHomologacao;
		this.soapActionHomologacao = soapActionHomologacao;
		this.layoutRelatorioString = layoutRelatorio;
	}
	
	public static WebServicesNFSEEnum getEnumPorIdCidadeTipoAcaoServico(Integer idCidade,TipoAcaoServicoNFSEEnum tipoAcaoServicoNFSEEnum) {
		for (WebServicesNFSEEnum enums : WebServicesNFSEEnum.values()) {
			if (enums.getIdCidade().equals(idCidade) && enums.getTipoAcaoServicoNFSEEnum().equals(tipoAcaoServicoNFSEEnum)) {
				return enums;
			}
		}
		return null;
	}

	public Integer getIdCidade() {
		return idCidade;
	}

	public void setIdCidade(Integer idCidade) {
		this.idCidade = idCidade;
	}

	public String getSoapAction() {
		return soapAction;
	}

	public void setSoapAction(String soapAction) {
		this.soapAction = soapAction;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getCodigoCidadePadraoSiafi() {
		return codigoCidadePadraoSiafi;
	}

	public void setCodigoCidadePadraoSiafi(Integer codigoCidadePadraoSiafi) {
		this.codigoCidadePadraoSiafi = codigoCidadePadraoSiafi;
	}
	
	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_WebServicesNFSEEnum_" + this.name());
	}

	public String getSchemaXML() {
		return schemaXML;
	}

	public void setSchemaXML(String schemaXML) {
		this.schemaXML = schemaXML;
	}
	public TipoAcaoServicoNFSEEnum getTipoAcaoServicoNFSEEnum() {
		return tipoAcaoServicoNFSEEnum;
	}

	public void setTipoAcaoServicoNFSEEnum(TipoAcaoServicoNFSEEnum tipoAcaoServicoNFSEEnum) {
		this.tipoAcaoServicoNFSEEnum = tipoAcaoServicoNFSEEnum;
	}

	public String getUrlHomologacao() {
		return urlHomologacao;
	}

	public void setUrlHomologacao(String urlHomologacao) {
		this.urlHomologacao = urlHomologacao;
	}

	public String getSoapActionHomologacao() {
		return soapActionHomologacao;
	}

	public void setSoapActionHomologacao(String soapActionHomologacao) {
		this.soapActionHomologacao = soapActionHomologacao;
	}
	
	public String getLayoutRelatorioString() {
		return layoutRelatorioString;
	}

	public void setLayoutRelatorioString(String layoutRelatorioString) {
		this.layoutRelatorioString = layoutRelatorioString;
	}
}
