package negocio.comuns.faturamento.nfe;

import java.io.ByteArrayInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import negocio.comuns.arquitetura.faturamento.nfe.DadosEnvio;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.faturamento.nfe.Assinador;
import negocio.comuns.utilitarias.faturamento.nfe.EstadosCodigoIBGE;
import negocio.comuns.utilitarias.faturamento.nfe.GerarXMLEnvio;
import negocio.comuns.utilitarias.faturamento.nfe.Servicos;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.comuns.utilitarias.faturamento.nfe.UteisNfe;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;

/**
 * 
 * @author Euripedes
 */
public class DadosEnvioVO {

	private String xmlns;
	protected String codigoEstado;
	protected String tipoAmbiente;
	private String url;
	private String soapAction;
	private String xml;
	private String versaoDados;
	protected String caminhoXML;
	protected String senhaCertificado;
	protected Boolean token;
	private String caminhoCertificado;
	protected byte[] certificado;
	protected NfeVO nfe;
	protected String caminhoJKS;
	protected String caminhoCfgToken;
	private String senhaUnidadeCertificadora;

	public DadosEnvioVO() {
		inicializarDados();
	}

	public void inicializarDados() {
		setXmlns("");
		setCodigoEstado("");
		setTipoAmbiente("");
		setUrl("");
		setSoapAction("");
		setXml("");
		setVersaoDados("2.00");
		setCaminhoXML("");
		setSenhaCertificado("");
		setSenhaUnidadeCertificadora("");
		setToken(false);
		setCaminhoJKS("");
		setCaminhoCfgToken("");
	}

	public void montarDadosStatusServico() {
		if (getTipoAmbiente().equals("2")) {
			obterPropriedadesHomologacaoStatusConexao();
		} else {
			obterPropriedadesProducaoStatusConexao();
		}
		montarXmlStatusConexao();
	}

	public void montarDadosEnviar(NfeVO nfe) throws Exception {
		if (getTipoAmbiente().equals("2")) {
			obterPropriedadesHomologacaoEnviar();
		} else {
			obterPropriedadesProducaoEnviar();
		}
		montarXmlEnvio(nfe);
	}

	public void montarDadosConsultarLote(String recibo) throws Exception {
		if (getTipoAmbiente().equals("2")) {
			obterPropriedadesHomologacaoConsultarLote();
		} else {
			obterPropriedadesProducaoConsultarLote();
		}
		montarXmlConsultaLote(recibo);
	}

	public void montarDadosConsultarNota(String chave) throws Exception {
		if (getTipoAmbiente().equals("2")) {
			obterPropriedadesHomologacaoConsultarNota();
		} else {
			obterPropriedadesProducaoConsultarNota();
		}
		montarXmlConsultaNota(chave);
	}

	public void montarDadosCancelar(String chave, String protocolo, String motivoCancelamento) throws Exception {
		if (getTipoAmbiente().equals("2")) {
			obterPropriedadesHomologacaoCancelar();
		} else {
			obterPropriedadesProducaoCancelar();
		}
		montarXmlCancelar(chave, protocolo, motivoCancelamento);
	}

	public void montarDadosInutilizar(String motivoInutilizacao, String cnpj, String modelo, String serie, String nrInicio, String nrFim) throws Exception {
		if (getTipoAmbiente().equals("2")) {
			obterPropriedadesHomologacaoInutilizar();
		} else {
			obterPropriedadesProducaoInutilizar();
		}
		montarXmlInutilizar(motivoInutilizacao, cnpj, modelo, serie, nrInicio, nrFim);
	}
	
	public void montarDadosEventoCancelamento(String chave, String cnpj, String motivoCancelamento, String protocolo) throws Exception {
		if (getTipoAmbiente().equals("2")) {
			obterPropriedadesHomologacaoCancelar();
		} else {
			obterPropriedadesProducaoCancelar();
		}
		montarXmlEventoCancelamento(chave, motivoCancelamento, cnpj, protocolo);
	}
	
	
	public void montarDadosCartaCorrecao(String chave, String cnpj, String dadosCorrecao, Integer seqEvento) throws Exception {
		if (getTipoAmbiente().equals("2")) {
			obterPropriedadesHomologacaoCartaCorrecao();
		} else {
			obterPropriedadesProducaoCartaCorrecao();
		}
		montarXmlCartaCorrecao(chave, dadosCorrecao, seqEvento, cnpj);
	}

	private void obterPropriedadesHomologacaoStatusConexao() {
		if (getCodigoEstado().equals(EstadosCodigoIBGE.GOIAS)) {
			setXmlns(DadosEnvio.GOIAS_HOMOLOGACAO_STATUS_SERVICO_XMLNS);
			setUrl(DadosEnvio.GOIAS_HOMOLOGACAO_STATUS_SERVICO_URL);
			setSoapAction(DadosEnvio.GOIAS_HOMOLOGACAO_STATUS_SERVICO_SOAP_ACTION);
		}
		if (getCodigoEstado().equals(EstadosCodigoIBGE.RIO_DE_JANEIRO)) {
			setXmlns(DadosEnvio.RIO_DE_JANEIRO_HOMOLOGACAO_STATUS_SERVICO_XMLNS);
			setUrl(DadosEnvio.RIO_DE_JANEIRO_HOMOLOGACAO_STATUS_SERVICO_URL);
			setSoapAction(DadosEnvio.RIO_DE_JANEIRO_HOMOLOGACAO_STATUS_SERVICO_SOAP_ACTION);
		}
		if (getCodigoEstado().equals(EstadosCodigoIBGE.DISTRITO_FEDERAL)) {
			setXmlns(DadosEnvio.DISTRITO_FEDERAL_HOMOLOGACAO_STATUS_SERVICO_XMLNS);
			setUrl(DadosEnvio.DISTRITO_FEDERAL_HOMOLOGACAO_STATUS_SERVICO_URL);
			setSoapAction(DadosEnvio.DISTRITO_FEDERAL_HOMOLOGACAO_STATUS_SERVICO_SOAP_ACTION);
		}
	}

	private void obterPropriedadesHomologacaoEnviar() {
		if (getCodigoEstado().equals(EstadosCodigoIBGE.GOIAS)) {
			setXmlns(DadosEnvio.GOIAS_HOMOLOGACAO_ENVIAR_XMLNS);
			setUrl(DadosEnvio.GOIAS_HOMOLOGACAO_ENVIAR_URL);
			setSoapAction(DadosEnvio.GOIAS_HOMOLOGACAO_ENVIAR_SOAP_ACTION);
		}
		if (getCodigoEstado().equals(EstadosCodigoIBGE.RIO_DE_JANEIRO)) {
			setXmlns(DadosEnvio.RIO_DE_JANEIRO_HOMOLOGACAO_ENVIAR_XMLNS);
			setUrl(DadosEnvio.RIO_DE_JANEIRO_HOMOLOGACAO_ENVIAR_URL);
			setSoapAction(DadosEnvio.RIO_DE_JANEIRO_HOMOLOGACAO_ENVIAR_SOAP_ACTION);
		}
		if (getCodigoEstado().equals(EstadosCodigoIBGE.DISTRITO_FEDERAL)) {
			setXmlns(DadosEnvio.DISTRITO_FEDERAL_HOMOLOGACAO_ENVIAR_XMLNS);
			setUrl(DadosEnvio.DISTRITO_FEDERAL_HOMOLOGACAO_ENVIAR_URL);
			setSoapAction(DadosEnvio.DISTRITO_FEDERAL_HOMOLOGACAO_ENVIAR_SOAP_ACTION);
		}
	}

	private void obterPropriedadesHomologacaoConsultarLote() {
		if (getCodigoEstado().equals(EstadosCodigoIBGE.GOIAS)) {
			setXmlns(DadosEnvio.GOIAS_HOMOLOGACAO_CONSULTA_LOTE_XMLNS);
			setUrl(DadosEnvio.GOIAS_HOMOLOGACAO_CONSULTA_LOTE_URL);
			setSoapAction(DadosEnvio.GOIAS_HOMOLOGACAO_CONSULTA_LOTE_SOAP_ACTION);
		}
		if (getCodigoEstado().equals(EstadosCodigoIBGE.RIO_DE_JANEIRO)) {
			setXmlns(DadosEnvio.RIO_DE_JANEIRO_HOMOLOGACAO_CONSULTA_LOTE_XMLNS);
			setUrl(DadosEnvio.RIO_DE_JANEIRO_HOMOLOGACAO_CONSULTA_LOTE_URL);
			setSoapAction(DadosEnvio.RIO_DE_JANEIRO_HOMOLOGACAO_CONSULTA_LOTE_SOAP_ACTION);
		}
		if (getCodigoEstado().equals(EstadosCodigoIBGE.DISTRITO_FEDERAL)) {
			setXmlns(DadosEnvio.DISTRITO_FEDERAL_HOMOLOGACAO_CONSULTA_LOTE_XMLNS);
			setUrl(DadosEnvio.DISTRITO_FEDERAL_HOMOLOGACAO_CONSULTA_LOTE_URL);
			setSoapAction(DadosEnvio.DISTRITO_FEDERAL_HOMOLOGACAO_CONSULTA_LOTE_SOAP_ACTION);
		}
	}

	private void obterPropriedadesHomologacaoConsultarNota() {
		if (getCodigoEstado().equals(EstadosCodigoIBGE.GOIAS)) {
			setXmlns(DadosEnvio.GOIAS_HOMOLOGACAO_CONSULTA_NOTA_XMLNS);
			setUrl(DadosEnvio.GOIAS_HOMOLOGACAO_CONSULTA_NOTA_URL);
			setSoapAction(DadosEnvio.GOIAS_HOMOLOGACAO_CONSULTA_NOTA_SOAP_ACTION);
		}
		if (getCodigoEstado().equals(EstadosCodigoIBGE.RIO_DE_JANEIRO)) {
			setXmlns(DadosEnvio.RIO_DE_JANEIRO_HOMOLOGACAO_CONSULTA_NOTA_XMLNS);
			setUrl(DadosEnvio.RIO_DE_JANEIRO_HOMOLOGACAO_CONSULTA_NOTA_URL);
			setSoapAction(DadosEnvio.RIO_DE_JANEIRO_HOMOLOGACAO_CONSULTA_NOTA_SOAP_ACTION);
		}
		if (getCodigoEstado().equals(EstadosCodigoIBGE.DISTRITO_FEDERAL)) {
			setXmlns(DadosEnvio.DISTRITO_FEDERAL_HOMOLOGACAO_CONSULTA_NOTA_XMLNS);
			setUrl(DadosEnvio.DISTRITO_FEDERAL_HOMOLOGACAO_CONSULTA_NOTA_URL);
			setSoapAction(DadosEnvio.DISTRITO_FEDERAL_HOMOLOGACAO_CONSULTA_NOTA_SOAP_ACTION);
		}
	}

	private void obterPropriedadesHomologacaoCancelar() {
		if (getCodigoEstado().equals(EstadosCodigoIBGE.GOIAS)) {
			setXmlns(DadosEnvio.GOIAS_HOMOLOGACAO_CANCELAMENTO_XMLNS);
			setUrl(DadosEnvio.GOIAS_HOMOLOGACAO_CANCELAMENTO_URL);
			setSoapAction(DadosEnvio.GOIAS_HOMOLOGACAO_CANCELAMENTO_SOAP_ACTION);
		}
		if (getCodigoEstado().equals(EstadosCodigoIBGE.RIO_DE_JANEIRO)) {
			setXmlns(DadosEnvio.RIO_DE_JANEIRO_HOMOLOGACAO_CANCELAMENTO_XMLNS);
			setUrl(DadosEnvio.RIO_DE_JANEIRO_HOMOLOGACAO_CANCELAMENTO_URL);
			setSoapAction(DadosEnvio.RIO_DE_JANEIRO_HOMOLOGACAO_CANCELAMENTO_SOAP_ACTION);
		}
		if (getCodigoEstado().equals(EstadosCodigoIBGE.DISTRITO_FEDERAL)) {
			setXmlns(DadosEnvio.DISTRITO_FEDERAL_HOMOLOGACAO_CANCELAMENTO_XMLNS);
			setUrl(DadosEnvio.DISTRITO_FEDERAL_HOMOLOGACAO_CANCELAMENTO_URL);
			setSoapAction(DadosEnvio.DISTRITO_FEDERAL_HOMOLOGACAO_CANCELAMENTO_SOAP_ACTION);
		}
	}

	private void obterPropriedadesHomologacaoInutilizar() {
		if (getCodigoEstado().equals(EstadosCodigoIBGE.GOIAS)) {
			setXmlns(DadosEnvio.GOIAS_HOMOLOGACAO_INUTILIZACAO_XMLNS);
			setUrl(DadosEnvio.GOIAS_HOMOLOGACAO_INUTILIZACAO_URL);
			setSoapAction(DadosEnvio.GOIAS_HOMOLOGACAO_INUTILIZACAO_SOAP_ACTION);
		}
		if (getCodigoEstado().equals(EstadosCodigoIBGE.RIO_DE_JANEIRO)) {
			setXmlns(DadosEnvio.RIO_DE_JANEIRO_HOMOLOGACAO_INUTILIZACAO_XMLNS);
			setUrl(DadosEnvio.RIO_DE_JANEIRO_HOMOLOGACAO_INUTILIZACAO_URL);
			setSoapAction(DadosEnvio.RIO_DE_JANEIRO_HOMOLOGACAO_INUTILIZACAO_SOAP_ACTION);
		}
		if (getCodigoEstado().equals(EstadosCodigoIBGE.DISTRITO_FEDERAL)) {
			setXmlns(DadosEnvio.DISTRITO_FEDERAL_HOMOLOGACAO_INUTILIZACAO_XMLNS);
			setUrl(DadosEnvio.DISTRITO_FEDERAL_HOMOLOGACAO_INUTILIZACAO_URL);
			setSoapAction(DadosEnvio.DISTRITO_FEDERAL_HOMOLOGACAO_INUTILIZACAO_SOAP_ACTION);
		}
	}
	
	
	private void obterPropriedadesHomologacaoCartaCorrecao() {
		if (getCodigoEstado().equals(EstadosCodigoIBGE.GOIAS)) {
			setXmlns(DadosEnvio.GOIAS_HOMOLOGACAO_CARTA_CORRECAO_XMLNS);
			setUrl(DadosEnvio.GOIAS_HOMOLOGACAO_CARTA_CORRECAO_URL);
			setSoapAction(DadosEnvio.GOIAS_HOMOLOGACAO_CARTA_CORRECAO_SOAP_ACTION);
		}
		if (getCodigoEstado().equals(EstadosCodigoIBGE.RIO_DE_JANEIRO)) {
			setXmlns(DadosEnvio.RIO_DE_JANEIRO_HOMOLOGACAO_CARTA_CORRECAO_XMLNS);
			setUrl(DadosEnvio.RIO_DE_JANEIRO_HOMOLOGACAO_CARTA_CORRECAO_URL);
			setSoapAction(DadosEnvio.RIO_DE_JANEIRO_HOMOLOGACAO_CARTA_CORRECAO_SOAP_ACTION);
		}
		if (getCodigoEstado().equals(EstadosCodigoIBGE.DISTRITO_FEDERAL)) {
			setXmlns(DadosEnvio.DISTRITO_FEDERAL_HOMOLOGACAO_CARTACORRECAO_XMLNS);
			setUrl(DadosEnvio.DISTRITO_FEDERAL_HOMOLOGACAO_CARTACORRECAO_URL);
			setSoapAction(DadosEnvio.DISTRITO_FEDERAL_HOMOLOGACAO_CARTACORRECAO_SOAP_ACTION);
		}
	}

	private void obterPropriedadesProducaoStatusConexao() {
		if (getCodigoEstado().equals(EstadosCodigoIBGE.GOIAS)) {
			setXmlns(DadosEnvio.GOIAS_PRODUCAO_STATUS_SERVICO_XMLNS);
			setUrl(DadosEnvio.GOIAS_PRODUCAO_STATUS_SERVICO_URL);
			setSoapAction(DadosEnvio.GOIAS_PRODUCAO_STATUS_SERVICO_SOAP_ACTION);
		}
		if (getCodigoEstado().equals(EstadosCodigoIBGE.RIO_DE_JANEIRO)) {
			setXmlns(DadosEnvio.RIO_DE_JANEIRO_PRODUCAO_STATUS_SERVICO_XMLNS);
			setUrl(DadosEnvio.RIO_DE_JANEIRO_PRODUCAO_STATUS_SERVICO_URL);
			setSoapAction(DadosEnvio.RIO_DE_JANEIRO_PRODUCAO_STATUS_SERVICO_SOAP_ACTION);
		}
		if (getCodigoEstado().equals(EstadosCodigoIBGE.DISTRITO_FEDERAL)) {
			setXmlns(DadosEnvio.DISTRITO_FEDERAL_PRODUCAO_STATUS_SERVICO_XMLNS);
			setUrl(DadosEnvio.DISTRITO_FEDERAL_PRODUCAO_STATUS_SERVICO_URL);
			setSoapAction(DadosEnvio.DISTRITO_FEDERAL_PRODUCAO_STATUS_SERVICO_SOAP_ACTION);
		}
	}

	private void obterPropriedadesProducaoEnviar() {
		if (getCodigoEstado().equals(EstadosCodigoIBGE.GOIAS)) {
			setXmlns(DadosEnvio.GOIAS_PRODUCAO_ENVIAR_XMLNS);
			setUrl(DadosEnvio.GOIAS_PRODUCAO_ENVIAR_URL);
			setSoapAction(DadosEnvio.GOIAS_PRODUCAO_ENVIAR_SOAP_ACTION);
		}
		if (getCodigoEstado().equals(EstadosCodigoIBGE.RIO_DE_JANEIRO)) {
			setXmlns(DadosEnvio.RIO_DE_JANEIRO_PRODUCAO_ENVIAR_XMLNS);
			setUrl(DadosEnvio.RIO_DE_JANEIRO_PRODUCAO_ENVIAR_URL);
			setSoapAction(DadosEnvio.RIO_DE_JANEIRO_PRODUCAO_ENVIAR_SOAP_ACTION);
		}
		if (getCodigoEstado().equals(EstadosCodigoIBGE.DISTRITO_FEDERAL)) {
			setXmlns(DadosEnvio.DISTRITO_FEDERAL_PRODUCAO_ENVIAR_XMLNS);
			setUrl(DadosEnvio.DISTRITO_FEDERAL_PRODUCAO_ENVIAR_URL);
			setSoapAction(DadosEnvio.DISTRITO_FEDERAL_PRODUCAO_ENVIAR_SOAP_ACTION);
		}
	}

	private void obterPropriedadesProducaoConsultarLote() {
		if (getCodigoEstado().equals(EstadosCodigoIBGE.GOIAS)) {
			setXmlns(DadosEnvio.GOIAS_PRODUCAO_CONSULTA_LOTE_XMLNS);
			setUrl(DadosEnvio.GOIAS_PRODUCAO_CONSULTA_LOTE_URL);
			setSoapAction(DadosEnvio.GOIAS_PRODUCAO_CONSULTA_LOTE_SOAP_ACTION);
		}
		if (getCodigoEstado().equals(EstadosCodigoIBGE.RIO_DE_JANEIRO)) {
			setXmlns(DadosEnvio.RIO_DE_JANEIRO_PRODUCAO_CONSULTA_LOTE_XMLNS);
			setUrl(DadosEnvio.RIO_DE_JANEIRO_PRODUCAO_CONSULTA_LOTE_URL);
			setSoapAction(DadosEnvio.RIO_DE_JANEIRO_PRODUCAO_CONSULTA_LOTE_SOAP_ACTION);
		}
		if (getCodigoEstado().equals(EstadosCodigoIBGE.DISTRITO_FEDERAL)) {
			setXmlns(DadosEnvio.DISTRITO_FEDERAL_PRODUCAO_CONSULTA_LOTE_XMLNS);
			setUrl(DadosEnvio.DISTRITO_FEDERAL_PRODUCAO_CONSULTA_LOTE_URL);
			setSoapAction(DadosEnvio.DISTRITO_FEDERAL_PRODUCAO_CONSULTA_LOTE_SOAP_ACTION);
		}
	}

	private void obterPropriedadesProducaoConsultarNota() {
		if (getCodigoEstado().equals(EstadosCodigoIBGE.GOIAS)) {
			setXmlns(DadosEnvio.GOIAS_PRODUCAO_CONSULTA_NOTA_XMLNS);
			setUrl(DadosEnvio.GOIAS_PRODUCAO_CONSULTA_NOTA_URL);
			setSoapAction(DadosEnvio.GOIAS_PRODUCAO_CONSULTA_NOTA_SOAP_ACTION);
		}
		if (getCodigoEstado().equals(EstadosCodigoIBGE.RIO_DE_JANEIRO)) {
			setXmlns(DadosEnvio.RIO_DE_JANEIRO_PRODUCAO_CONSULTA_NOTA_XMLNS);
			setUrl(DadosEnvio.RIO_DE_JANEIRO_PRODUCAO_CONSULTA_NOTA_URL);
			setSoapAction(DadosEnvio.RIO_DE_JANEIRO_PRODUCAO_CONSULTA_NOTA_SOAP_ACTION);
		}
		if (getCodigoEstado().equals(EstadosCodigoIBGE.DISTRITO_FEDERAL)) {
			setXmlns(DadosEnvio.DISTRITO_FEDERAL_PRODUCAO_CONSULTA_NOTA_XMLNS);
			setUrl(DadosEnvio.DISTRITO_FEDERAL_PRODUCAO_CONSULTA_NOTA_URL);
			setSoapAction(DadosEnvio.DISTRITO_FEDERAL_PRODUCAO_CONSULTA_NOTA_SOAP_ACTION);
		}
	}

	private void obterPropriedadesProducaoCancelar() {
		if (getCodigoEstado().equals(EstadosCodigoIBGE.GOIAS)) {
			setXmlns(DadosEnvio.GOIAS_PRODUCAO_CANCELAMENTO_XMLNS);
			setUrl(DadosEnvio.GOIAS_PRODUCAO_CANCELAMENTO_URL);
			setSoapAction(DadosEnvio.GOIAS_PRODUCAO_CANCELAMENTO_SOAP_ACTION);
		}
		if (getCodigoEstado().equals(EstadosCodigoIBGE.RIO_DE_JANEIRO)) {
			setXmlns(DadosEnvio.RIO_DE_JANEIRO_PRODUCAO_CANCELAMENTO_XMLNS);
			setUrl(DadosEnvio.RIO_DE_JANEIRO_PRODUCAO_CANCELAMENTO_URL);
			setSoapAction(DadosEnvio.RIO_DE_JANEIRO_PRODUCAO_CANCELAMENTO_SOAP_ACTION);
		}
		if (getCodigoEstado().equals(EstadosCodigoIBGE.DISTRITO_FEDERAL)) {
			setXmlns(DadosEnvio.DISTRITO_FEDERAL_PRODUCAO_CANCELAMENTO_XMLNS);
			setUrl(DadosEnvio.DISTRITO_FEDERAL_PRODUCAO_CANCELAMENTO_URL);
			setSoapAction(DadosEnvio.DISTRITO_FEDERAL_PRODUCAO_CANCELAMENTO_SOAP_ACTION);
		}
	}

	private void obterPropriedadesProducaoInutilizar() {
		if (getCodigoEstado().equals(EstadosCodigoIBGE.GOIAS)) {
			setXmlns(DadosEnvio.GOIAS_PRODUCAO_INUTILIZACAO_XMLNS);
			setUrl(DadosEnvio.GOIAS_PRODUCAO_INUTILIZACAO_URL);
			setSoapAction(DadosEnvio.GOIAS_PRODUCAO_INUTILIZACAO_SOAP_ACTION);
		}
		if (getCodigoEstado().equals(EstadosCodigoIBGE.RIO_DE_JANEIRO)) {
			setXmlns(DadosEnvio.RIO_DE_JANEIRO_PRODUCAO_INUTILIZACAO_XMLNS);
			setUrl(DadosEnvio.RIO_DE_JANEIRO_PRODUCAO_INUTILIZACAO_URL);
			setSoapAction(DadosEnvio.RIO_DE_JANEIRO_PRODUCAO_INUTILIZACAO_SOAP_ACTION);
		}
		if (getCodigoEstado().equals(EstadosCodigoIBGE.DISTRITO_FEDERAL)) {
			setXmlns(DadosEnvio.DISTRITO_FEDERAL_PRODUCAO_INUTILIZACAO_XMLNS);
			setUrl(DadosEnvio.DISTRITO_FEDERAL_PRODUCAO_INUTILIZACAO_URL);
			setSoapAction(DadosEnvio.DISTRITO_FEDERAL_PRODUCAO_INUTILIZACAO_SOAP_ACTION);
		}
	}
	

	private void obterPropriedadesProducaoCartaCorrecao() {
		if (getCodigoEstado().equals(EstadosCodigoIBGE.GOIAS)) {
			setXmlns(DadosEnvio.GOIAS_PRODUCAO_CARTACORRECAO_XMLNS);
			setUrl(DadosEnvio.GOIAS_PRODUCAO_CARTACORRECAO_URL);
			setSoapAction(DadosEnvio.GOIAS_PRODUCAO_CARTACORRECAO_SOAP_ACTION);
		}
		if (getCodigoEstado().equals(EstadosCodigoIBGE.RIO_DE_JANEIRO)) {
			setXmlns(DadosEnvio.RIO_DE_JANEIRO_PRODUCAO_CARTACORRECAO_XMLNS);
			setUrl(DadosEnvio.RIO_DE_JANEIRO_PRODUCAO_CARTACORRECAO_URL);
			setSoapAction(DadosEnvio.RIO_DE_JANEIRO_PRODUCAO_CARTACORRECAO_SOAP_ACTION);
		}
		if (getCodigoEstado().equals(EstadosCodigoIBGE.DISTRITO_FEDERAL)) {
			setXmlns(DadosEnvio.DISTRITO_FEDERAL_PRODUCAO_CARTACORRECAO_XMLNS);
			setUrl(DadosEnvio.DISTRITO_FEDERAL_PRODUCAO_CARTACORRECAO_URL);
			setSoapAction(DadosEnvio.DISTRITO_FEDERAL_PRODUCAO_CARTACORRECAO_SOAP_ACTION);
		}
	}

	private void montarXmlStatusConexao() {
		StringBuffer soapMessage = new StringBuffer();
		soapMessage.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		soapMessage.append("<consStatServ versao=\"").append(getVersaoDados()).append("\" xmlns=\"http://www.portalfiscal.inf.br/nfe\">");
		soapMessage.append("<tpAmb>").append(getTipoAmbiente()).append("</tpAmb>");
		soapMessage.append("<cUF>").append(getCodigoEstado()).append("</cUF>");
		soapMessage.append("<xServ>STATUS</xServ>");
		soapMessage.append("</consStatServ>");
		
		setXml(soapMessage.toString());
	}

	private void montarXmlEnvio(NfeVO nota) throws Exception {
//		setXml(UteisNfe.inserirAtributeXMLNS(new GerarXMLEnvio().gerarXMLEnvio(this, nota), "enviNFe"));
		setXml(new GerarXMLEnvio().gerarXMLEnvio(this, nota));
//		//System.out.println("XMLGERADO:" + getXml());
	}

	private void montarXmlConsultaLote(String recibo) throws Exception {
		StringBuffer soapMessage = new StringBuffer();
		soapMessage.append("<consReciNFe xmlns=\"http://www.portalfiscal.inf.br/nfe\" versao=\"" + getVersaoDados() + "\">").append("<tpAmb>" + getTipoAmbiente() + "</tpAmb>").append("<nRec>" + recibo + "</nRec>").append("</consReciNFe>");
		setXml(soapMessage.toString());
	}

	public void montarXmlConsultaNota(String chave) throws Exception {
		StringBuffer soapMessage = new StringBuffer();
		soapMessage.append("<consSitNFe xmlns=\"http://www.portalfiscal.inf.br/nfe\" versao=\"" + getVersaoDados() + "\">").append("<tpAmb>" + getTipoAmbiente() + "</tpAmb>").append("<xServ>CONSULTAR</xServ>").append("<chNFe>" + chave + "</chNFe>").append("</consSitNFe>");
		setXml(soapMessage.toString());
	}

	public void montarXmlCancelar(String chave, String protocolo, String motivoCancelamento) throws Exception {
		StringBuffer soapMessage = new StringBuffer();
		soapMessage.append("<cancNFe xmlns=\"http://www.portalfiscal.inf.br/nfe\" versao=\"" + getVersaoDados() + "\">").append("<infCanc Id=\"ID" + chave + "\">").append("<tpAmb>" + getTipoAmbiente() + "</tpAmb>").append("<xServ>CANCELAR</xServ>").append("<chNFe>" + chave + "</chNFe>")
				.append("<nProt>" + protocolo + "</nProt>").append("<xJust>" + motivoCancelamento + "</xJust>").append("</infCanc>").append("</cancNFe>");
		Document doc;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		doc = builder.newDocument();
		doc.setXmlStandalone(true);
		doc.setXmlVersion("1.0");

		doc = factory.newDocumentBuilder().parse(new ByteArrayInputStream(soapMessage.toString().getBytes()));
		setXml(Assinador.assinarNFe(this, doc, Servicos.CANCELAR));
	}
	
	public void montarXmlEventoCancelamento(String chave, String motivoCancelamento, String cnpj, String protocolo) throws Exception {
		StringBuffer soapMessage = new StringBuffer();
		soapMessage.append("<envEvento xmlns=\"http://www.portalfiscal.inf.br/nfe\" versao=\"1.00\"><idLote>1</idLote><evento versao=\"1.00\" xmlns=\"http://www.portalfiscal.inf.br/nfe\">");
		soapMessage.append("<infEvento Id=\"ID110111").append(chave).append("01\">");
		soapMessage.append("<cOrgao>").append(getCodigoEstado()).append("</cOrgao>");
		soapMessage.append("<tpAmb>").append(getTipoAmbiente()).append("</tpAmb>");
		soapMessage.append("<CNPJ>").append(cnpj).append("</CNPJ>");
		soapMessage.append("<chNFe>").append(chave).append("</chNFe>");
		soapMessage.append("<dhEvento>").append(UteisData.obterDataFormatoEnvioCartaCorrecaoNFe(UteisNfe.getHorarioCancelamentoCartaoCorrecaoNfe(), false)).append("</dhEvento>");
		soapMessage.append("<tpEvento>110111</tpEvento>");
		soapMessage.append("<nSeqEvento>1</nSeqEvento>");
		soapMessage.append("<verEvento>1.00</verEvento>");
		soapMessage.append("<detEvento versao=\"1.00\">");
		soapMessage.append("<descEvento>Cancelamento</descEvento>");
		soapMessage.append("<nProt>").append(protocolo);
		soapMessage.append("</nProt><xJust>").append(UteisNfe.retirarCaracteresEspeciais(Uteis.removerAcentos(motivoCancelamento)).trim());
		soapMessage.append("</xJust></detEvento></infEvento></evento></envEvento>");
		
		////System.out.println("EVENTO_CANCELAMENTO_ENVIO:" + soapMessage.toString());
		
		Document doc;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		doc = builder.newDocument();
		doc.setXmlStandalone(true);
		doc.setXmlVersion("1.0");

		doc = factory.newDocumentBuilder().parse(new ByteArrayInputStream(soapMessage.toString().getBytes()));
		setXml(Assinador.assinarNFe(this, doc, Servicos.EVENTO_CANCELAMENTO));
	}

	public void montarXmlCartaCorrecao(String chave, String motivoCorrecao, Integer seqEvento, String cnpj) throws Exception {
		StringBuffer soapMessage = new StringBuffer();
		soapMessage.append("<envEvento xmlns=\"http://www.portalfiscal.inf.br/nfe\" versao=\"1.00\"><idLote>1</idLote><evento versao=\"1.00\" xmlns=\"http://www.portalfiscal.inf.br/nfe\">");
		soapMessage.append("<infEvento Id=\"ID110110").append(chave).append(UteisTexto.obterNumeroComDoisDigitos(seqEvento)).append("\">");
		soapMessage.append("<cOrgao>").append(getCodigoEstado()).append("</cOrgao>");
		soapMessage.append("<tpAmb>").append(getTipoAmbiente()).append("</tpAmb>");
		soapMessage.append("<CNPJ>").append(cnpj).append("</CNPJ>");
		soapMessage.append("<chNFe>").append(chave).append("</chNFe>");
		soapMessage.append("<dhEvento>").append(UteisData.obterDataFormatoEnvioCartaCorrecaoNFe(UteisNfe.getHorarioCancelamentoCartaoCorrecaoNfe(), false)).append("</dhEvento>");
		soapMessage.append("<tpEvento>110110</tpEvento>");
		soapMessage.append("<nSeqEvento>").append(seqEvento).append("</nSeqEvento>");
		soapMessage.append("<verEvento>1.00</verEvento>");
		soapMessage.append("<detEvento versao=\"1.00\">");
		soapMessage.append("<descEvento>Carta de Correcao</descEvento>");
		soapMessage.append("<xCorrecao>").append(UteisTexto.retirarAcentuacao(motivoCorrecao)).append("</xCorrecao>");
		soapMessage.append("<xCondUso>A Carta de Correcao e disciplinada pelo paragrafo 1o-A do art. 7o do Convenio S/N, de 15 de dezembro de 1970 e ");
		soapMessage.append("pode ser utilizada para regularizacao de erro ocorrido na emissao de documento fiscal, desde que o erro nao esteja ");
		soapMessage.append("relacionado com: I - as variaveis que determinam o valor do imposto tais como: base de calculo, aliquota, diferenca ");
		soapMessage.append("de preco, quantidade, valor da operacao ou da prestacao; II - a correcao de dados cadastrais que implique mudanca ");
		soapMessage.append("do remetente ou do destinatario; III - a data de emissao ou de saida.");
		soapMessage.append("</xCondUso></detEvento></infEvento></evento></envEvento>");
		
		////System.out.println("CARTACORRECAO_ENVIO:" + soapMessage.toString());
		
		Document doc;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		doc = builder.newDocument();
		doc.setXmlStandalone(true);
		doc.setXmlVersion("1.0");

		doc = factory.newDocumentBuilder().parse(new ByteArrayInputStream(soapMessage.toString().getBytes()));
		setXml(Assinador.assinarNFe(this, doc, Servicos.CARTACORRECAO));
	}

	public void montarXmlInutilizar(String motivoInutilizacao, String cnpj, String modelo, String serie, String nrInicio, String nrFim) throws Exception {
		StringBuffer soapMessage = new StringBuffer();
		soapMessage.append("<inutNFe xmlns=\"http://www.portalfiscal.inf.br/nfe\" versao=\"" + getVersaoDados() + "\">").append("<infInut Id=\"ID" + UteisNfe.criarIdNFeInutilizacao(getCodigoEstado(), cnpj, modelo, serie, nrInicio, nrFim) + "\">").append("<tpAmb>" + getTipoAmbiente() + "</tpAmb>")
				.append("<xServ>INUTILIZAR</xServ>").append("<cUF>" + getCodigoEstado() + "</cUF>").append("<ano>" + UteisNfe.getAnoDataAtual() + "</ano>").append("<CNPJ>" + UteisNfe.removerMascara(cnpj) + "</CNPJ>").append("<mod>" + modelo + "</mod>").append("<serie>" + serie + "</serie>")
				.append("<nNFIni>" + nrInicio + "</nNFIni>").append("<nNFFin>" + nrFim + "</nNFFin>").append("<xJust>" + UteisNfe.retirarCaracteresEspeciais(UteisNfe.retirarCaracteresEspeciais(UteisNfe.retirarSinaisSimbolosEspacoString(motivoInutilizacao))) + "</xJust>").append("</infInut>")
				.append("</inutNFe>");
		Document doc;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		doc = builder.newDocument();
		doc.setXmlStandalone(true);
		doc.setXmlVersion("1.0");

		doc = factory.newDocumentBuilder().parse(new ByteArrayInputStream(soapMessage.toString().getBytes()));
		setXml(Assinador.assinarNFe(this, doc, Servicos.INUTILIZAR));
	}

	public String getCodigoEstado() {
		return codigoEstado;
	}

	public void setCodigoEstado(String codigoEstado) {
		this.codigoEstado = codigoEstado;
	}

	public String getSoapAction() {
		return soapAction;
	}

	public void setSoapAction(String soapAction) {
		this.soapAction = soapAction;
	}

	public String getTipoAmbiente() {
		return tipoAmbiente;
	}

	public void setTipoAmbiente(String tipoAmbiente) {
		this.tipoAmbiente = tipoAmbiente;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

	public String getXmlns() {
		return xmlns;
	}

	public void setXmlns(String xmlns) {
		this.xmlns = xmlns;
	}

	public String getCaminhoXML() {
		return caminhoXML;
	}

	public void setCaminhoXML(String caminhoXML) {
		this.caminhoXML = caminhoXML;
	}

	public String getSenhaCertificado() {
		return senhaCertificado;
	}

	public void setSenhaCertificado(String senhaCertificado) {
		this.senhaCertificado = senhaCertificado;
	}

	public Boolean getToken() {
		return token;
	}

	public void setToken(Boolean token) {
		this.token = token;
	}

	public String getVersaoDados() {
		return versaoDados;
	}

	public void setVersaoDados(String versaoDados) {
		this.versaoDados = versaoDados;
	}

	public NfeVO getNfe() {
		return nfe;
	}

	public void setNfe(NfeVO nfe) {
		this.nfe = nfe;
	}

	public String getCaminhoCfgToken() {
		return caminhoCfgToken;
	}

	public void setCaminhoCfgToken(String caminhoCfgToken) {
		this.caminhoCfgToken = caminhoCfgToken;
	}

	public String getCaminhoJKS() {
		return caminhoJKS;
	}

	public void setCaminhoJKS(String caminhoJKS) {
		this.caminhoJKS = caminhoJKS;
	}

	public byte[] getCertificado() {
		return certificado;
	}

	public void setCertificado(byte[] certificado) {
		this.certificado = certificado;
	}

	public String getCaminhoCertificado() {
		if (caminhoCertificado == null) {
			caminhoCertificado = "";
		}
		return caminhoCertificado;
	}

	public void setCaminhoCertificado(String caminhoCertificado) {
		this.caminhoCertificado = caminhoCertificado;
	}

	public String getSenhaUnidadeCertificadora() {
		if (senhaUnidadeCertificadora == null) {
			senhaUnidadeCertificadora = "";
		}
		return senhaUnidadeCertificadora;
	}

	public void setSenhaUnidadeCertificadora(String senhaUnidadeCertificadora) {
		this.senhaUnidadeCertificadora = senhaUnidadeCertificadora;
	}
}
