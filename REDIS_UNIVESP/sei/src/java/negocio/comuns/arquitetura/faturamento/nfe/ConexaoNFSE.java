package negocio.comuns.arquitetura.faturamento.nfe;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeader;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import controle.arquitetura.AplicacaoControle;
import controle.arquitetura.AssuntoDebugEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.faturamento.nfe.NotaFiscalSaidaVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.faturamento.nfe.AmbienteNfeEnum;
import negocio.comuns.utilitarias.faturamento.nfe.UteisNfe;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import webservice.nfse.WebServicesNFSEEnum;

public class ConexaoNFSE extends ControleAcesso {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static String conexaoNFSEGoiania(NotaFiscalSaidaVO notaFiscalSaidaVO, String xml, String caminhoCertificado, String senhaCertificado, String caminhoJks, String senhaJks, UsuarioVO usuarioLogado) throws Exception {
		UteisNfe.autenticaoNfse(caminhoCertificado, senhaCertificado, caminhoJks, senhaJks);
		StringBuffer soapMessage = new StringBuffer();
		soapMessage.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		soapMessage.append("<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">");
		soapMessage.append("<soap12:Body>");
		soapMessage.append("<GerarNfse xmlns=\"http://nfse.goiania.go.gov.br/ws/\">");
		soapMessage.append("<ArquivoXML>").append(xml.replaceAll("<", "&lt;").replaceAll(">", "&gt;")).append("</ArquivoXML>");
		soapMessage.append("</GerarNfse>");
		soapMessage.append("</soap12:Body>");
		soapMessage.append("</soap12:Envelope>");
		return enviar(notaFiscalSaidaVO, soapMessage.toString(), SOAPConstants.SOAP_1_2_PROTOCOL);
	}

	public static String conexaoNFSEUberlandia(NotaFiscalSaidaVO notaFiscalSaidaVO, String xml, String caminhoCertificado, String senhaCertificado, String caminhoJks, String senhaJks, UsuarioVO usuarioLogado) throws Exception {
		UteisNfe.autenticaoNfse(caminhoCertificado, senhaCertificado, caminhoJks, senhaJks);
		StringBuffer soapMessage = new StringBuffer();
		soapMessage.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		soapMessage.append("<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
		soapMessage.append("	xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"");
		soapMessage.append("	xmlns:dsf=\"http://dsfnet.com.br\">");
		soapMessage.append("	<soapenv:Body>");
		soapMessage.append("		<dsf:enviarSincrono soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">");
		soapMessage.append("			<mensagemXml xsi:type=\"xsd:string\">");
		soapMessage.append(xml.replaceAll("<", "&lt;").replaceAll(">", "&gt;"));
		soapMessage.append("</mensagemXml>");
		soapMessage.append("		</dsf:enviarSincrono>");
		soapMessage.append("	</soapenv:Body>");
		soapMessage.append("</soapenv:Envelope>");
		return enviar2(notaFiscalSaidaVO, soapMessage.toString(), SOAPConstants.SOAP_1_1_PROTOCOL);
	}

	public static String conexaoNFSEJoaoPessoa(NotaFiscalSaidaVO notaFiscalSaidaVO, String tagServicoNFSE, String xml, String caminhoCertificado, String senhaCertificado, String caminhoJks, String senhaJks, UsuarioVO usuarioLogado) throws Exception {
		UteisNfe.autenticaoNfse(caminhoCertificado, senhaCertificado, caminhoJks, senhaJks);
		StringBuffer soapMessage = new StringBuffer();
		soapMessage.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		soapMessage.append("<S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\"");
		soapMessage.append("	xmlns:nfse=\"http://nfse.abrasf.org.br\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
		soapMessage.append("	xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">");
		soapMessage.append("  <S:Header />");
		soapMessage.append("	<S:Body>");
		soapMessage.append("		<nfse:GerarNfseRequest xmlns=\"http://nfse.abrasf.org.br\">");
		soapMessage.append("			<nfseCabecMsg xmlns=\"\">");
		soapMessage.append("				<cabecalho versao=\"2.02\" xmlns=\"http://www.abrasf.org.br/nfse.xsd\">".replaceAll("<", "&lt;").replaceAll(">", "&gt;"));
		soapMessage.append("					<versaoDados>2.02</versaoDados>".replaceAll("<", "&lt;").replaceAll(">", "&gt;"));
		soapMessage.append("				</cabecalho>".replaceAll("<", "&lt;").replaceAll(">", "&gt;"));
		soapMessage.append("			</nfseCabecMsg>");
		soapMessage.append("			<nfseDadosMsg xmlns=\"\">");
		soapMessage.append(xml.toString().replaceAll("<", "&lt;").replaceAll(">", "&gt;"));
		soapMessage.append("			</nfseDadosMsg>");
		soapMessage.append("		</nfse:GerarNfseRequest>");
		soapMessage.append("	</S:Body>");
		soapMessage.append("</S:Envelope>");
		return enviar(notaFiscalSaidaVO, soapMessage.toString(), SOAPConstants.SOAP_1_2_PROTOCOL);

	}

	public static String conexaoNFSEJoaoPessoa2(NotaFiscalSaidaVO notaFiscalSaidaVO, String tagServicoNFSE, String xml, String caminhoCertificado, String senhaCertificado, String caminhoJks, String senhaJks, UsuarioVO usuarioLogado) throws Exception {
		UteisNfe.autenticaoNfse(caminhoCertificado, senhaCertificado, caminhoJks, senhaJks);
		StringBuffer soapMessage = new StringBuffer();
		soapMessage.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		soapMessage.append(" <soap:Envelope xmlns:soap=\"http://www.w3.org/2001/12/soap-envelope\">");
		soapMessage.append("  <soap:Header/>");
		soapMessage.append("  <soap:Body>");
		soapMessage.append("    <" + tagServicoNFSE + "Request xmlns=\"http://nfse.abrasf.org.br/GerarNfse\" >");
		soapMessage.append("      <inputXML>" + xml + "</inputXML>");
		soapMessage.append("    </" + tagServicoNFSE + "Request>");
		soapMessage.append("  </soap:Body>");
		soapMessage.append(" </soap:Envelope>");
		return enviar2(notaFiscalSaidaVO, soapMessage.toString(), SOAPConstants.SOAP_1_2_PROTOCOL);
	}

	public static String conexaoNFSERioDeJaneiro(NotaFiscalSaidaVO notaFiscalSaidaVO, String tagServicoNFSE, String xml, String caminhoCertificado, String senhaCertificado, String caminhoJks, String senhaJks, UsuarioVO usuarioLogado) throws Exception {
		UteisNfe.autenticaoNfse(caminhoCertificado, senhaCertificado, caminhoJks, senhaJks);
		StringBuffer soapMessage = new StringBuffer();
		soapMessage.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		soapMessage.append(" <soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">");
		soapMessage.append("  <soap:Body>");
		soapMessage.append("    <" + tagServicoNFSE + "Request xmlns=\"http://notacarioca.rio.gov.br/\">");
		soapMessage.append("      <inputXML>" + xml.replaceAll("<", "&lt;").replaceAll(">", "&gt;") + "</inputXML>");
		soapMessage.append("    </" + tagServicoNFSE + "Request>");
		soapMessage.append("  </soap:Body>");
		soapMessage.append(" </soap:Envelope>");
		return enviar2(notaFiscalSaidaVO, soapMessage.toString(), SOAPConstants.SOAP_1_1_PROTOCOL);
	}

	public static String conexaoNFSEBeloHorizonte(NotaFiscalSaidaVO notaFiscalSaidaVO, String tagServicoNFSE, String xml, String caminhoCertificado, String senhaCertificado, String caminhoJks, String senhaJks, UsuarioVO usuarioLogado) throws Exception {
		UteisNfe.autenticaoNfse(caminhoCertificado, senhaCertificado, caminhoJks, senhaJks);
		StringBuffer soapMessage = new StringBuffer();
		soapMessage.append("<?xml version='1.0' encoding='UTF-8'?>");
		soapMessage.append("<S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">");
		soapMessage.append("	<S:Body>");
		soapMessage.append("		<ns2:GerarNfseRequest xmlns:ns2=\"http://ws.bhiss.pbh.gov.br\">");
		soapMessage.append("			<nfseCabecMsg>");
		soapMessage.append("				<![CDATA[<cabecalho versao=\"1.00\" xmlns=\"http://www.abrasf.org.br/nfse.xsd\">");
		soapMessage.append("					<versaoDados>1.00</versaoDados>");
		soapMessage.append("				</cabecalho>]]>");
		soapMessage.append("			</nfseCabecMsg>");
		soapMessage.append("			<nfseDadosMsg>");
		soapMessage.append("<![CDATA[" + xml + "]]>");
		soapMessage.append("			</nfseDadosMsg>");
		soapMessage.append("		</ns2:GerarNfseRequest>");
		soapMessage.append("	</S:Body>");
		soapMessage.append("</S:Envelope>");
		System.out.println(soapMessage.toString());
		return enviar2(notaFiscalSaidaVO, Uteis.removerCaracteresEspeciais3(soapMessage.toString()), SOAPConstants.SOAP_1_1_PROTOCOL);
	}

	public static String conexaoNFSEPortoAlegre(NotaFiscalSaidaVO notaFiscalSaidaVO, String tagServicoNFSE, String xml, String caminhoCertificado, String senhaCertificado, String caminhoJks, String senhaJks, UsuarioVO usuarioLogado) throws Exception {
		UteisNfe.autenticaoNfse(caminhoCertificado, senhaCertificado, caminhoJks, senhaJks);
		StringBuffer soapMessage = new StringBuffer();
		soapMessage.append("<?xml version='1.0' encoding='UTF-8'?>");
		soapMessage.append("<S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">");
		soapMessage.append("	<S:Body>");
		soapMessage.append("		<ns2:GerarNfseRequest xmlns:ns2=\"http://ws.bhiss.pbh.gov.br\">");
		soapMessage.append("			<nfseCabecMsg>");
		soapMessage.append("				<![CDATA[<cabecalho xmlns=\"http://www.abrasf.org.br/nfse.xsd\" versao=\"1.00\">");
		soapMessage.append("					<versaoDados>1.00</versaoDados>");
		soapMessage.append("				</cabecalho>]]>");
		soapMessage.append("			</nfseCabecMsg>");
		soapMessage.append("			<nfseDadosMsg>");
		soapMessage.append("<![CDATA[" + xml + "]]>");
		soapMessage.append("			</nfseDadosMsg>");
		soapMessage.append("		</ns2:GerarNfseRequest>");
		soapMessage.append("	</S:Body>");
		soapMessage.append("</S:Envelope>");
		System.out.println(soapMessage.toString());
		return enviar2(notaFiscalSaidaVO, soapMessage.toString(), SOAPConstants.SOAP_1_1_PROTOCOL);
	}

	public static String conexaoNFSECuritibaSoap12(NotaFiscalSaidaVO notaFiscalSaidaVO, String tagServicoNFSE, String xml, String caminhoCertificado, String senhaCertificado, String caminhoJks, String senhaJks, UsuarioVO usuarioLogado) throws Exception {
		UteisNfe.autenticaoNfse(caminhoCertificado, senhaCertificado, caminhoJks, senhaJks);
		StringBuffer soapMessage = new StringBuffer();
		soapMessage.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		soapMessage.append("<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">");
		soapMessage.append("  <soap12:Body>");
		soapMessage.append("	<RecepcionarLoteRps xmlns=\"http://www.e-governeapps2.com.br/\">");
		soapMessage.append(xml.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", ""));
		soapMessage.append("	</RecepcionarLoteRps>");
		soapMessage.append("  </soap12:Body>");
		soapMessage.append("</soap12:Envelope>");
		return enviar(notaFiscalSaidaVO, soapMessage.toString(), SOAPConstants.SOAP_1_2_PROTOCOL);
	}

	public static String conexaoNFSECuritibaSoap11(NotaFiscalSaidaVO notaFiscalSaidaVO, String tagServicoNFSE, String xml, String caminhoCertificado, String senhaCertificado, String caminhoJks, String senhaJks, UsuarioVO usuarioLogado) throws Exception {
		UteisNfe.autenticaoNfse(caminhoCertificado, senhaCertificado, caminhoJks, senhaJks);
		StringBuffer soapMessage = new StringBuffer();
		soapMessage.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		soapMessage.append("<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">");
		soapMessage.append("  <soap:Body>");
		soapMessage.append("	<RecepcionarLoteRps xmlns=\"http://www.e-governeapps2.com.br/\">");
		soapMessage.append(xml.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", ""));
		soapMessage.append("	</RecepcionarLoteRps>");
		soapMessage.append("  </soap:Body>");
		soapMessage.append("</soap:Envelope>");
		return enviar2(notaFiscalSaidaVO, soapMessage.toString(), SOAPConstants.SOAP_1_1_PROTOCOL);
	}

	public static String conexaoNFSELondrina(NotaFiscalSaidaVO notaFiscalSaidaVO, String tagServicoNFSE, String xml, String caminhoCertificado, String senhaCertificado, String caminhoJks, String senhaJks, UsuarioVO usuarioLogado) throws Exception {
		UteisNfe.autenticaoNfse(caminhoCertificado, senhaCertificado, caminhoJks, senhaJks);
		StringBuffer soapMessage = new StringBuffer();
		soapMessage.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
		soapMessage.append("<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:tns=\"http://iss.londrina.pr.gov.br/ws/v1_03\">");
		soapMessage.append("  <SOAP-ENV:Body>");
		soapMessage.append(xml.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", ""));
		soapMessage.append("  </SOAP-ENV:Body>");
		soapMessage.append("</SOAP-ENV:Envelope>");
		return enviar2(notaFiscalSaidaVO, soapMessage.toString(), SOAPConstants.SOAP_1_1_PROTOCOL);
	}

	public static String conexaoNFSEPalmasSoap11(NotaFiscalSaidaVO notaFiscalSaidaVO, String tagServicoNFSE, String xml, String caminhoCertificado, String senhaCertificado, String caminhoJks, String senhaJks, UsuarioVO usuarioLogado) throws Exception {
		UteisNfe.autenticaoNfse(caminhoCertificado, senhaCertificado, caminhoJks, senhaJks);
		StringBuffer soapMessage = new StringBuffer();
		soapMessage.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		soapMessage.append("<s:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\">");
		soapMessage.append("<s:Header/>");
		soapMessage.append("<s:Body>");
		soapMessage.append("<" + tagServicoNFSE + " xmlns=\"http://tempuri.org/\">");
		soapMessage.append("<cabec>");
		soapMessage.append("</cabec>");
		soapMessage.append("<msg>" + xml.replace("\"", "&quot;").replaceAll("<", "&lt;").replaceAll(">", "&gt;") + "</msg>");
		soapMessage.append("</" + tagServicoNFSE + ">");
		soapMessage.append("</s:Body>");
		soapMessage.append("</s:Envelope>");
		System.out.println(soapMessage.toString());
		return enviar2(notaFiscalSaidaVO, soapMessage.toString(), SOAPConstants.SOAP_1_1_PROTOCOL);
	}

	public static String conexaoNFSENatal(NotaFiscalSaidaVO notaFiscalSaidaVO, String xml, String caminhoCertificado, String senhaCertificado, String caminhoJks, String senhaJks, String eventoRequest, UsuarioVO usuarioLogado) throws Exception {
		UteisNfe.autenticaoNfse(caminhoCertificado, senhaCertificado, caminhoJks, senhaJks);
		StringBuffer soapMessage = new StringBuffer();
		soapMessage.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		if (notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getAmbienteNfeEnum().equals(AmbienteNfeEnum.HOMOLOGACAO)) {
			soapMessage.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:wsn=\"https://wsnfsev1homologacao.natal.rn.gov.br:8443\">");

		} else {
			soapMessage.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:wsn=\"https://wsnfsev1.natal.rn.gov.br:8444\">");
		}
		soapMessage.append("<soapenv:Header/>");
		soapMessage.append("<soapenv:Body>");
		soapMessage.append("<wsn:" + eventoRequest + ">");
		soapMessage.append("<nfseCabecMsg>");
		soapMessage.append("<![CDATA[<cabecalho xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" versao=\"1\" xmlns=\"http://www.abrasf.org.br/ABRASF/arquivos/nfse.xsd\">");
		soapMessage.append("<versaoDados>1</versaoDados>");
		soapMessage.append("</cabecalho>]]>");
		soapMessage.append("</nfseCabecMsg>");
		soapMessage.append("<nfseDadosMsg>");
		soapMessage.append("<![CDATA[" + xml + "]]>");
		soapMessage.append("</nfseDadosMsg>");
		soapMessage.append("</wsn:" + eventoRequest + ">");
		soapMessage.append("</soapenv:Body>");
		soapMessage.append("</soapenv:Envelope>");
		System.out.println(soapMessage.toString());
		return enviar2(notaFiscalSaidaVO, soapMessage.toString(), SOAPConstants.SOAP_1_1_PROTOCOL);
	}

	public static String conexaoNFSEMaceio(NotaFiscalSaidaVO notaFiscalSaidaVO, String tagServicoNFSE, String xml, String caminhoCertificado, String senhaCertificado, String caminhoJks, String senhaJks, UsuarioVO usuarioLogado) throws Exception {
		UteisNfe.autenticaoNfse(caminhoCertificado, senhaCertificado, caminhoJks, senhaJks);
		StringBuffer soapMessage = new StringBuffer();
		soapMessage.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		soapMessage.append("<env:Envelope xmlns:env=\"http://schemas.xmlsoap.org/soap/envelope/\">");
		soapMessage.append("<env:Header/>");
		soapMessage.append("<env:Body>");
		soapMessage.append("<ns1:RecepcionarLoteRpsV3 xmlns:ns1=\"http://homologacao.ginfes.com.br\">");
		soapMessage.append("<arg0 xmlns=\"\">");
		soapMessage.append("<ns1:cabecalho versao=\"3\" xmlns:ns1=\"http://www.ginfes.com.br/cabecalho_v03.xsd\"><versaoDados>3</versaoDados></ns1:cabecalho>".replace("\"", "&quot;").replaceAll("<", "&lt;").replaceAll(">", "&gt;"));
		soapMessage.append("</arg0>");
		soapMessage.append("<arg1 xmlns=\"\">" + xml.replace("\"", "&quot;").replaceAll("<", "&lt;").replaceAll(">", "&gt;") + "</arg1>");
		soapMessage.append("</ns1:RecepcionarLoteRpsV3>");
		soapMessage.append("</env:Body>");
		soapMessage.append("</env:Envelope>");
		System.out.println(soapMessage.toString());
		return enviar(notaFiscalSaidaVO, soapMessage.toString(), SOAPConstants.SOAP_1_1_PROTOCOL);
	}

	public static String conexaoNFSEVitoria(NotaFiscalSaidaVO notaFiscalSaidaVO, String xml, String caminhoCertificado, String senhaCertificado, String caminhoJks, String senhaJks, UsuarioVO usuarioLogado) throws Exception {
		UteisNfe.autenticaoNfse(caminhoCertificado, senhaCertificado, caminhoJks, senhaJks);
		StringBuffer soapMessage = new StringBuffer();
		soapMessage.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		soapMessage.append("<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">");
		soapMessage.append("<soap12:Body>");
		soapMessage.append("<RecepcionarLoteRpsSincrono xmlns=\"http://www.abrasf.org.br/nfse.xsd\">");
		soapMessage.append("<mensagemXML>").append(xml.replaceAll("<", "&lt;").replaceAll(">", "&gt;")).append("</mensagemXML>");
		soapMessage.append("</RecepcionarLoteRpsSincrono>");
		soapMessage.append("</soap12:Body>");
		soapMessage.append("</soap12:Envelope>");
		return enviar(notaFiscalSaidaVO, soapMessage.toString(), SOAPConstants.SOAP_1_2_PROTOCOL);
	}

	public static String conexaoNFSEAnapolis(NotaFiscalSaidaVO notaFiscalSaidaVO, String xml, String caminhoCertificado, String senhaCertificado, String caminhoJks, String senhaJks, String eventoRequest, UsuarioVO usuarioLogado) throws Exception {
		UteisNfe.autenticaoNfse(caminhoCertificado, senhaCertificado, caminhoJks, senhaJks);
		StringBuffer soapMessage = new StringBuffer();
		soapMessage.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		soapMessage.append("<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">");
		soapMessage.append("<soap12:Body>");
		soapMessage.append("<" + eventoRequest + " xmlns=\"http://www.issnetonline.com.br/webservice/nfd\">");
		soapMessage.append("<xml>").append(xml.replaceAll("<", "&lt;").replaceAll(">", "&gt;")).append("</xml>");
		soapMessage.append("</" + eventoRequest + ">");
		soapMessage.append("</soap12:Body>");
		soapMessage.append("</soap12:Envelope>");
		return enviar(notaFiscalSaidaVO, soapMessage.toString(), SOAPConstants.SOAP_1_2_PROTOCOL);
	}

	public static String conexaoNFSEAraguaina(NotaFiscalSaidaVO notaFiscalSaidaVO, String tagServicoNFSE, String xml, String caminhoCertificado, String senhaCertificado, String caminhoJks, String senhaJks, UsuarioVO usuarioLogado) throws Exception {
		UteisNfe.autenticaoNfse(caminhoCertificado, senhaCertificado, caminhoJks, senhaJks);
		StringBuilder soapMessage = new StringBuilder();

		soapMessage.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:nfse=\"http://nfse.abrasf.org.br\">");
		soapMessage.append("<soapenv:Header/>");
		soapMessage.append("<soapenv:Body>");
		soapMessage.append("<nfse:GerarNfseRequest>");
		soapMessage.append("<nfseCabecMsg><![CDATA[<?xml version=\"1.0\" encoding=\"UTF-8\"?><cabecalho xmlns=\"http://www.abrasf.org.br/nfse.xsd\" versao=\"2.02\"><versaoDados>2.02</versaoDados></cabecalho>]]></nfseCabecMsg>");
		soapMessage.append(String.format("<nfseDadosMsg><![CDATA[%s]]></nfseDadosMsg>", xml));
		soapMessage.append("</nfse:GerarNfseRequest>");
		soapMessage.append("</soapenv:Body>");
		soapMessage.append("</soapenv:Envelope>");

		return enviar(notaFiscalSaidaVO, soapMessage.toString(), SOAPConstants.DYNAMIC_SOAP_PROTOCOL);
	}

	// public static String conexaoNFSEAraguaina(NotaFiscalSaidaVO
	// notaFiscalSaidaVO, String tagServicoNFSE, String xml, String
	// caminhoCertificado, String senhaCertificado, String caminhoJks, String
	// senhaJks, UsuarioVO usuarioLogado) throws Exception {
	// UteisNfe.autenticaoNfse(caminhoCertificado, senhaCertificado, caminhoJks,
	// senhaJks);
	// StringBuffer soapMessage = new StringBuffer();
	// soapMessage.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
	// soapMessage.append("<S:Envelope
	// xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\"");
	// soapMessage.append(" xmlns:nfse=\"http://nfse.abrasf.org.br\"
	// xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
	// soapMessage.append(" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">");
	// soapMessage.append(" <S:Header />");
	// soapMessage.append(" <S:Body>");
	// soapMessage.append(" <nfse:GerarNfseRequest
	// xmlns=\"http://nfse.abrasf.org.br\">");
	// soapMessage.append(" <nfseCabecMsg xmlns=\"\">");
	// soapMessage.append(" <cabecalho versao=\"2.02\"
	// xmlns=\"http://www.abrasf.org.br/nfse.xsd\">".replaceAll("<",
	// "&lt;").replaceAll(">", "&gt;"));
	// soapMessage.append(" <versaoDados>2.02</versaoDados>".replaceAll("<",
	// "&lt;").replaceAll(">", "&gt;"));
	// soapMessage.append(" </cabecalho>".replaceAll("<", "&lt;").replaceAll(">",
	// "&gt;"));
	// soapMessage.append(" </nfseCabecMsg>");
	// soapMessage.append(" <nfseDadosMsg xmlns=\"\">");
	// soapMessage.append(xml.toString().replaceAll("<", "&lt;").replaceAll(">",
	// "&gt;"));
	// soapMessage.append(" </nfseDadosMsg>");
	// soapMessage.append(" </nfse:GerarNfseRequest>");
	// soapMessage.append(" </S:Body>");
	// soapMessage.append("</S:Envelope>");
	// return enviar(notaFiscalSaidaVO, soapMessage.toString(),
	// SOAPConstants.SOAP_1_2_PROTOCOL);
	// }

	private static String enviar(NotaFiscalSaidaVO notaFiscalSaidaVO, String envelope, String protocolo) throws Exception {
		System.clearProperty("javax.net.ssl.keyStoreType");
		System.clearProperty("javax.net.ssl.keyStore");
		System.clearProperty("javax.net.ssl.keyStorePassword");
		SOAPConnection con = null;
		ByteArrayOutputStream in = new ByteArrayOutputStream();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		SOAPMessage message;
		MessageFactory factory;
		MimeHeaders header = new MimeHeaders();
		URL url = null;
		SOAPMessage res = null;
		try {
			factory = MessageFactory.newInstance(protocolo);
			try {
				AplicacaoControle.realizarEscritaTextoDebug(AssuntoDebugEnum.NOTA_FISCAL_SAIDA, "Envio: " + envelope);
				url = new URL(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getAmbienteNfeEnum().equals(AmbienteNfeEnum.HOMOLOGACAO) ? notaFiscalSaidaVO.getWebServicesNFSEEnum().getUrlHomologacao() : notaFiscalSaidaVO.getWebServicesNFSEEnum().getUrl());
				if (notaFiscalSaidaVO.getWebServicesNFSEEnum().equals(WebServicesNFSEEnum.ARAGUAINA_TO)) {
					header.addHeader("Content-Type", "text/xml; charset=utf-8");
				} else {
					header.addHeader("Content-Type", "application/soap+xml; charset=utf-8");
				}
				message = factory.createMessage(header, new ByteArrayInputStream(envelope.getBytes(StandardCharsets.UTF_8)));
				con = SOAPConnectionFactory.newInstance().createConnection();
				ConexaoNFSE.printHeaders(message);
				res = con.call(message, url);
				in = new ByteArrayOutputStream();
				message.writeTo(in);
				out = new ByteArrayOutputStream();
				res.writeTo(out);
				AplicacaoControle.realizarEscritaTextoDebug(AssuntoDebugEnum.NOTA_FISCAL_SAIDA, "Retorno: " + out.toString());
				return out.toString();
			} catch (IOException ex) {
				AplicacaoControle.realizarEscritaErroDebug(AssuntoDebugEnum.NOTA_FISCAL_SAIDA, ex);
				throw new Exception(ex.getMessage());
			}
		} catch (SOAPException ex) {
			AplicacaoControle.realizarEscritaErroDebug(AssuntoDebugEnum.NOTA_FISCAL_SAIDA, ex);
			throw new Exception(ex.getMessage());
		} finally {
			if (con != null) {
				con.close();
			}
			in.close();
			out.close();
			in = null;
			out = null;
			message = null;
			factory = null;
			header = null;
			url = null;
			res = null;
		}
	}

	private static void printHeaders(SOAPMessage message) {
		Iterator allHeaders = message.getMimeHeaders().getAllHeaders();
		while (allHeaders.hasNext()) {
			MimeHeader next = (MimeHeader) allHeaders.next();
			System.out.println(String.format("%s : %s", next.getName(), next.getValue()));

		}
	}

	private static String enviar2(NotaFiscalSaidaVO notaFiscalSaidaVO, String envelope, String protocolo) throws Exception {
		SOAPConnection con = null;
		ByteArrayOutputStream in = new ByteArrayOutputStream();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		SOAPMessage message;
		MessageFactory factory;
		MimeHeaders header = new MimeHeaders();
		URL url = null;
		SOAPMessage res = null;
		try {
			factory = MessageFactory.newInstance(protocolo);
			try {
				url = new URL(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getAmbienteNfeEnum().equals(AmbienteNfeEnum.HOMOLOGACAO) ? notaFiscalSaidaVO.getWebServicesNFSEEnum().getUrlHomologacao() : notaFiscalSaidaVO.getWebServicesNFSEEnum().getUrl());
				message = factory.createMessage(header, new ByteArrayInputStream(envelope.getBytes()));
				header.addHeader("Content-Type", "application/soap+xml");
				header.getAllHeaders();
				if (notaFiscalSaidaVO.getWebServicesNFSEEnum().equals(WebServicesNFSEEnum.LONDRINA_PR_GERAR)) {
					message.setProperty(SOAPMessage.CHARACTER_SET_ENCODING, "ISO-8859-1");
				}
				if (notaFiscalSaidaVO.getWebServicesNFSEEnum().equals(WebServicesNFSEEnum.ARAGUAINA_TO)) {
					message.setProperty(SOAPMessage.CHARACTER_SET_ENCODING, "ISO-8859-1");
				}
				message.getMimeHeaders().addHeader("SOAPAction", notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getAmbienteNfeEnum().equals(AmbienteNfeEnum.HOMOLOGACAO) ? notaFiscalSaidaVO.getWebServicesNFSEEnum().getSoapActionHomologacao() : notaFiscalSaidaVO.getWebServicesNFSEEnum().getSoapAction());
				con = SOAPConnectionFactory.newInstance().createConnection();
				res = con.call(message, url);
				in = new ByteArrayOutputStream();
				message.writeTo(in);
				System.out.print(in.toString());
				out = new ByteArrayOutputStream();
				res.writeTo(out);
				return out.toString().replaceAll("&lt;", "<").replaceAll("&gt;", ">");
			} catch (IOException ex) {
				throw new Exception(ex.getMessage());
			}
		} catch (SOAPException ex) {
			throw new Exception(ex.getMessage());
		} finally {
			if (con != null) {
				con.close();
			}
			in.close();
			out.close();
			in = null;
			out = null;
			message = null;
			factory = null;
			header = null;
			url = null;
			res = null;
		}
	}
}