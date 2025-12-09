package negocio.comuns.arquitetura.faturamento.nfe;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.security.Provider;
import java.security.Security;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.faturamento.nfe.DadosEnvioVO;
import negocio.comuns.utilitarias.faturamento.nfe.UteisNfe;
import negocio.facade.jdbc.arquitetura.ControleAcesso;

/**
 * 
 * @author Wendel Rodrigues
 * 
 * Classe responsável por realizar a criação da mensagem, envio e resposta 
 * por meio do protocolo SOAP, para Nota Fiscal Eletrônica.
 * 
 */
public class ConexaoSefaz extends ControleAcesso {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static String conexaoSefaz(DadosEnvioVO obj, UsuarioVO usuarioLogado) throws Exception {
		//UteisNfe.autenticaoNfe(obj);
		StringBuffer soapMessage = new StringBuffer();
		soapMessage.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		soapMessage.append("<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">");
		soapMessage.append("<soap12:Header>");
		soapMessage.append("<nfeCabecMsg xmlns=\"" + obj.getXmlns() + "\">");
		soapMessage.append("<cUF>" + obj.getCodigoEstado() + "</cUF>");
		soapMessage.append("<versaoDados>" + obj.getVersaoDados() + "</versaoDados>");
		soapMessage.append("</nfeCabecMsg>");
		soapMessage.append("</soap12:Header>");
		soapMessage.append("<soap12:Body>");
		soapMessage.append("<nfeDadosMsg xmlns=\"" + obj.getXmlns() + "\">");
		soapMessage.append(UteisNfe.removerTagXMLEnconding(obj.getXml().replaceAll("\r", "")));
		soapMessage.append("</nfeDadosMsg>");
		soapMessage.append("</soap12:Body>");
		soapMessage.append("</soap12:Envelope>");
		return enviar(soapMessage.toString(), obj);
	}

	private static String enviar(String envelope, DadosEnvioVO obj) throws Exception {
		System.out.println("STATUS-NFE-> Property-Renegotiation: " + System.getProperty("sun.security.ssl.allowUnsafeRenegotiation")); 
		System.out.println("STATUS-NFE-> Property-pkgs: " + System.getProperty("java.protocol.handler.pkgs"));
		System.out.println("STATUS-NFE-> Property-keyStoreType: " + System.getProperty("javax.net.ssl.keyStoreType"));
		System.out.println("STATUS-NFE-> Property-keyStore: " + System.getProperty("javax.net.ssl.keyStore"));
		if (System.getProperty("javax.net.ssl.keyStorePassword") != null && System.getProperty("javax.net.ssl.keyStorePassword").length() > 1) {
			System.out.println("STATUS-NFE-> Property-keyStorePassword: " + System.getProperty("javax.net.ssl.keyStorePassword").substring(0, 2) + "...");
		} else {
			System.out.println("STATUS-NFE-> Property-keyStorePassword: " + System.getProperty("javax.net.ssl.keyStorePassword"));
		}
		
		SOAPConnection con = null;
		ByteArrayOutputStream in = new ByteArrayOutputStream();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		SOAPMessage message;
		MessageFactory factory;
		MimeHeaders header = new MimeHeaders();
		URL url = null;
		SOAPMessage res = null;
		try {
		    factory = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
			try {										
				header.addHeader("Content-Type", "application/soap+xml");
				message = factory.createMessage(header, new ByteArrayInputStream(envelope.getBytes()));
				message.getMimeHeaders().addHeader("SOAPAction", obj.getSoapAction());
				con = SOAPConnectionFactory.newInstance().createConnection();

				System.out.println("STATUS-NFE-> enviando XML: " + obj.getXml());
				url = new URL(obj.getUrl());
				System.out.println("STATUS-NFE-> MESSAGE (envelope): " + envelope);
				System.out.println("STATUS-NFE-> URL: " + url);
				UteisNfe.imprimirCertificadosCacerts();
				res = con.call(message, url);

				in = new ByteArrayOutputStream();
				message.writeTo(in);
				out = new ByteArrayOutputStream();
				res.writeTo(out);
				return out.toString();
			} catch (IOException ex) {
				ex.printStackTrace();
				throw new Exception(ex.getMessage());
			}
		} catch (SOAPException ex) {
			ex.printStackTrace();
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
