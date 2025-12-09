/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.utilitarias.faturamento.nfe;

/**
 *
 * @author Euripedes Doutor
 */
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.faturamento.nfe.DadosEnvioVO;
import negocio.comuns.faturamento.nfe.NotaFiscalSaidaVO;
import webservice.nfse.WebServicesNFSEEnum;

public class Assinador {

	public static final String C14N_TRANSFORM_METHOD = "http://www.w3.org/TR/2001/REC-xml-c14n-20010315";
	static XMLSignatureFactory sig;
	static X509Certificate cert;
	static KeyInfo ki;
	static SignedInfo si;
	static KeyStore rep;

	public Assinador() throws Exception {
	}

	public static String assinarNFe(DadosEnvioVO dadosEnvio, Document doc, String tipoServico) throws Exception {
		carregarCertificado(dadosEnvio.getCertificado(), dadosEnvio.getSenhaCertificado());
		sig = XMLSignatureFactory.getInstance("DOM");
		ArrayList<Transform> transformList = new ArrayList<Transform>();
		Transform enveloped = sig.newTransform(Transform.ENVELOPED, (TransformParameterSpec) null);
		Transform c14n = sig.newTransform(C14N_TRANSFORM_METHOD, (TransformParameterSpec) null);
		transformList.add(enveloped);
		transformList.add(c14n);
		String tag = "";
		if (tipoServico.equals(Servicos.ENVIAR)) {
			tag = "infNFe";
		} else if (tipoServico.equals(Servicos.EVENTO_CANCELAMENTO)) {
			tag = "infEvento";
		} else if (tipoServico.equals(Servicos.CANCELAR)) {
			tag = "infCanc";
		} else if (tipoServico.equals(Servicos.INUTILIZAR)) {
			tag = "infInut";
		} else if (tipoServico.equals(Servicos.CARTACORRECAO)) {
			tag = "infEvento";
		}
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		String xml = UteisNfe.converterXMLparaString(doc);
		xml = UteisNfe.removerQuebraLinhaFinalTag(xml);
		xml = UteisNfe.removerEspacoDuplo(xml);
		doc = factory.newDocumentBuilder().parse(new ByteArrayInputStream(xml.getBytes()));
		NodeList elements = doc.getElementsByTagName(tag);

		Element el = (Element) elements.item(0);
		String id = el.getAttribute("Id");

		el.setIdAttribute("Id", true);
		el.setIdAttributeNS(null, "Id", true);
		Attr idAtrr = el.getAttributeNode("Id");
		el.setIdAttributeNode(idAtrr, true);

		Reference r = sig.newReference("#".concat(id), sig.newDigestMethod(DigestMethod.SHA1, null), transformList, null, null);
		si = sig.newSignedInfo(sig.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE, (C14NMethodParameterSpec) null), sig.newSignatureMethod(SignatureMethod.RSA_SHA1, null), Collections.singletonList(r));

		KeyInfoFactory kif = sig.getKeyInfoFactory();
		List x509Content = new ArrayList();
		x509Content.add(cert);
		X509Data xd = kif.newX509Data(x509Content);
		ki = kif.newKeyInfo(Collections.singletonList(xd));
		NodeList elements2;
		if (tipoServico.equals(Servicos.ENVIAR)) {
			elements2 = doc.getElementsByTagName("NFe");
		} else if (tipoServico.equals(Servicos.EVENTO_CANCELAMENTO)) {
			elements2 = doc.getElementsByTagName("evento");
		} else if (tipoServico.equals(Servicos.CANCELAR)) {
			elements2 = doc.getElementsByTagName("cancNFe");
		} else if (tipoServico.equals(Servicos.CARTACORRECAO)) {
			elements2 = doc.getElementsByTagName("evento");
		} else {
			elements2 = doc.getElementsByTagName("inutNFe");
		}

		Element el2 = (Element) elements2.item(0);
		DOMSignContext dsc = new DOMSignContext(getChavePrivada(dadosEnvio), el2);
		XMLSignature signature = sig.newXMLSignature(si, ki);
		signature.sign(dsc);

		String nomeArquivoXML = dadosEnvio.getCaminhoXML();
		// if (tipoServico.equals(Servicos.EVENTO_CANCELAMENTO)) {
		// nomeArquivoXML = nomeArquivoXML + "EventoCancelamento";
		// }
		// if (tipoServico.equals(Servicos.CANCELAR)) {
		// nomeArquivoXML = nomeArquivoXML + "Cancelamento";
		// }
		// if (tipoServico.equals(Servicos.INUTILIZAR)) {
		// nomeArquivoXML = nomeArquivoXML + "Inutilizacao";
		// }
		// if (tipoServico.equals(Servicos.CARTACORRECAO)) {
		// nomeArquivoXML = nomeArquivoXML + "CartaCorrecao";
		// }
		OutputStream os = new FileOutputStream(nomeArquivoXML);

		doc.setXmlStandalone(true);
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer trans = tf.newTransformer();
		trans.transform(new DOMSource(doc), new StreamResult(os));
		return UteisNfe.converterXMLparaString(doc);
	}

	public static String assinarNFSe(NotaFiscalSaidaVO notaFiscalSaidaVO, ConfiguracaoGeralSistemaVO conSistemaVO, String xml, String tipoServico, String nomeArquivoXML, String prefixoTag) throws Exception {
		byte[] certificado = UteisNfe.getCertificado(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO(), conSistemaVO);
		carregarCertificado(certificado, notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getSenhaCertificado());
		sig = XMLSignatureFactory.getInstance("DOM");
		ArrayList<Transform> transformList = new ArrayList<Transform>();
		Transform enveloped = sig.newTransform(Transform.ENVELOPED, (TransformParameterSpec) null);
		Transform c14n = sig.newTransform(C14N_TRANSFORM_METHOD, (TransformParameterSpec) null);
		transformList.add(enveloped);
		transformList.add(c14n);
		String tag = "";
		if (tipoServico.equals(Servicos.ENVIAR)) {
			tag = prefixoTag + "Rps";
		} else if (tipoServico.equals(Servicos.EVENTO_CANCELAMENTO)) {
			tag = prefixoTag + "infEvento";
		} else if (tipoServico.equals(Servicos.CANCELAR)) {
			tag = prefixoTag + "infCanc";
		} else if (tipoServico.equals(Servicos.INUTILIZAR)) {
			tag = prefixoTag + "infInut";
		} else if (tipoServico.equals(Servicos.CARTACORRECAO)) {
			tag = prefixoTag + "infEvento";
		} else if (tipoServico.equals("LoteRps")) {
			tag = prefixoTag + "LoteRps";
		} else if (tipoServico.equals("GerarNfseEnvio")) {
			tag = prefixoTag + "GerarNfseEnvio";
		} else if (tipoServico.equals("ConsultarLoteRpsEnvio")) {
			tag = prefixoTag + "ConsultarLoteRpsEnvio";
		} else if (tipoServico.equals(prefixoTag + "Rps")) {
			tag = prefixoTag + "InfDeclaracaoPrestacaoServico";
		}

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		xml = UteisNfe.removerQuebraLinhaFinalTag(xml);
		xml = UteisNfe.removerEspacoDuplo(xml);
		Document doc = factory.newDocumentBuilder().parse(new ByteArrayInputStream(xml.getBytes()));
		NodeList elements = doc.getElementsByTagName(tag);
		Element el = (Element) elements.item(0);

		if (notaFiscalSaidaVO.getWebServicesNFSEEnum().equals(WebServicesNFSEEnum.ARAGUAINA_TO)) {
			Element elementReference = (Element) doc.getElementsByTagName("InfDeclaracaoPrestacaoServico").item(0);
			String attributeId = "Id";
			elementReference.setIdAttribute(attributeId, true);

			Reference reference = sig.newReference("#".concat(elementReference.getAttribute(attributeId)), sig.newDigestMethod(DigestMethod.SHA1, null), transformList, null, null);
			si = sig.newSignedInfo(sig.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE, (C14NMethodParameterSpec) null), sig.newSignatureMethod(SignatureMethod.RSA_SHA1, null), Collections.singletonList(reference));
		} else {
			Reference r = sig.newReference("", sig.newDigestMethod(DigestMethod.SHA1, null), transformList, null, null);
			si = sig.newSignedInfo(sig.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE, (C14NMethodParameterSpec) null), sig.newSignatureMethod(SignatureMethod.RSA_SHA1, null), Collections.singletonList(r));
		}

		KeyInfoFactory kif = sig.getKeyInfoFactory();
		List x509Content = new ArrayList();
		x509Content.add(cert);
		X509Data xd = kif.newX509Data(x509Content);
		ki = kif.newKeyInfo(Collections.singletonList(xd));
		NodeList elements2 = null;
		if (tipoServico.equals(Servicos.ENVIAR)) {
			elements2 = doc.getElementsByTagName(prefixoTag + "Rps");
		} else if (tipoServico.equals(Servicos.EVENTO_CANCELAMENTO)) {
			elements2 = doc.getElementsByTagName(prefixoTag + "evento");
		} else if (tipoServico.equals(Servicos.CANCELAR)) {
			elements2 = doc.getElementsByTagName(prefixoTag + "cancNFe");
		} else if (tipoServico.equals(Servicos.CARTACORRECAO)) {
			elements2 = doc.getElementsByTagName(prefixoTag + "evento");
		} else if (tipoServico.equals(Servicos.INUTILIZAR)) {
			elements2 = doc.getElementsByTagName(prefixoTag + "inutNFe");
		} else if (tipoServico.equals("LoteRps")) {
			elements2 = doc.getElementsByTagName(prefixoTag + "EnviarLoteRpsSincronoEnvio");
		} else if (tipoServico.equals("GerarNfseEnvio")) {
			elements2 = doc.getElementsByTagName(prefixoTag + "GerarNfseEnvio");
		} else if (tipoServico.equals("ConsultarLoteRpsEnvio")) {
			elements2 = doc.getElementsByTagName(prefixoTag + "ConsultarLoteRpsEnvio");
		} else if (tipoServico.equals(prefixoTag + "Rps")) {
			elements2 = doc.getElementsByTagName(prefixoTag + "Rps");
		} else if (tipoServico.equals(prefixoTag + "InfDeclaracaoPrestacaoServico")) {
			elements2 = doc.getElementsByTagName(prefixoTag + "InfDeclaracaoPrestacaoServico");
		}

		Element el2 = (Element) elements2.item(0);
		DOMSignContext dsc = new DOMSignContext(getChavePrivadaNFSe(notaFiscalSaidaVO, certificado), el2);
		XMLSignature signature = sig.newXMLSignature(si, ki);
		signature.sign(dsc);

		OutputStream os = new FileOutputStream(nomeArquivoXML);

		doc.setXmlStandalone(true);
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer trans = tf.newTransformer();
		trans.transform(new DOMSource(doc), new StreamResult(os));
		return UteisNfe.converterXMLparaString(doc);
	}

	public static void validarCertificado() throws Exception {
		try {
			cert.checkValidity();
			System.out.println("STATUS-NFE-> Certificado validado com sucesso");
		} catch (CertificateExpiredException e) {
			System.out.println("STATUS-NFE-> ValidandoCertificado: Certificado expirado");
			throw new Exception("Certificado expirado!");
		} catch (CertificateNotYetValidException e) {
			System.out.println("STATUS-NFE-> ValidandoCertificado: Certificado invalido");
			throw new Exception("Certificado invalido!");
		} catch (CertificateException e) {
			System.out.println("STATUS-NFE-> ValidandoCertificado: Exception " + e.getMessage());
			throw new Exception("Certificate Error: " + e.getMessage());
		}
	}
	
	public static void carregarCertificado(byte[] certificado, String senha) throws Exception {
		if (senha != null && senha.length() > 1) {
			System.out.println("STATUS-NFE-> SenhaCertificado: " + senha.substring(0, 2) + "...");
		} else {
			System.out.println("STATUS-NFE-> SenhaCertificado: " + senha);
		}
		InputStream fs = new ByteArrayInputStream(certificado);
		rep = KeyStore.getInstance("PKCS12");
		rep.load(fs, senha.toCharArray());
		Enumeration<String> aliasesEnum = rep.aliases();
		String aliasChave = "";
		while (aliasesEnum.hasMoreElements()) {
			aliasChave = (String) aliasesEnum.nextElement();
			if (rep.isKeyEntry(aliasChave)) {
				System.out.println("STATUS-NFE-> certificado carregado: " + aliasChave);
			}
		}
		cert = (X509Certificate) rep.getCertificate(aliasChave);
		validarCertificado();
	}

	public static PrivateKey getChavePrivada(DadosEnvioVO dadosEnvio) throws Exception {
		KeyStore ks = null;
		if (dadosEnvio.getToken()) {
			ks = KeyStore.getInstance("PKCS11");
			FileInputStream fis = new FileInputStream(dadosEnvio.getCaminhoCfgToken());
			ks.load(fis, dadosEnvio.getSenhaCertificado().toCharArray());
		} else {
			InputStream fs = new ByteArrayInputStream(dadosEnvio.getCertificado());
			ks = KeyStore.getInstance("PKCS12");
			ks.load(fs, dadosEnvio.getSenhaCertificado().toCharArray());
		}
		Enumeration aliasesEnum = ks.aliases();
		String aliasChavePrivada = "";
		while (aliasesEnum.hasMoreElements()) {
			aliasChavePrivada = (String) aliasesEnum.nextElement();
			if (ks.isKeyEntry(aliasChavePrivada)) {
				// //System.out.println(aliasChavePrivada);
			}
		}
		KeyStore.PrivateKeyEntry keyEntry = (KeyStore.PrivateKeyEntry) ks.getEntry(aliasChavePrivada, new KeyStore.PasswordProtection(dadosEnvio.getSenhaCertificado().toCharArray()));
		PrivateKey privateKey = (PrivateKey) keyEntry.getPrivateKey();
		return privateKey;
	}

	public static PrivateKey getChavePrivadaNFSe(NotaFiscalSaidaVO notaFiscalSaidaVO, byte[] certificado) throws Exception {
		KeyStore ks = null;
		InputStream fs = new ByteArrayInputStream(certificado);
		ks = KeyStore.getInstance("PKCS12");
		ks.load(fs, notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getSenhaCertificado().toCharArray());
		Enumeration aliasesEnum = ks.aliases();
		String aliasChavePrivada = "";
		while (aliasesEnum.hasMoreElements()) {
			aliasChavePrivada = (String) aliasesEnum.nextElement();
			if (ks.isKeyEntry(aliasChavePrivada)) {
				// //System.out.println(aliasChavePrivada);
			}
		}
		KeyStore.PrivateKeyEntry keyEntry = (KeyStore.PrivateKeyEntry) ks.getEntry(aliasChavePrivada, new KeyStore.PasswordProtection(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getSenhaCertificado().toCharArray()));
		PrivateKey privateKey = (PrivateKey) keyEntry.getPrivateKey();
		return privateKey;
	}

	public static String assinarNFSeDemaisCidades(NotaFiscalSaidaVO notaFiscalSaidaVO, ConfiguracaoGeralSistemaVO conSistemaVO, String xml, String tipoServico, String nomeArquivoXML) throws Exception {
		byte[] certificado = UteisNfe.getCertificado(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO(), conSistemaVO);
		carregarCertificado(certificado, notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getSenhaCertificado());
		sig = XMLSignatureFactory.getInstance("DOM");
		ArrayList<Transform> transformList = new ArrayList<Transform>();
		Transform enveloped = sig.newTransform(Transform.ENVELOPED, (TransformParameterSpec) null);
		Transform c14n = sig.newTransform(C14N_TRANSFORM_METHOD, (TransformParameterSpec) null);
		transformList.add(enveloped);
		transformList.add(c14n);
		String tag = "";
		if (tipoServico.equals(Servicos.ENVIAR)) {
			tag = "Lote";
		} else if (tipoServico.equals(Servicos.EVENTO_CANCELAMENTO)) {
			tag = "infEvento";
		} else if (tipoServico.equals(Servicos.CANCELAR)) {
			tag = "infCanc";
		} else if (tipoServico.equals(Servicos.INUTILIZAR)) {
			tag = "infInut";
		} else if (tipoServico.equals(Servicos.CARTACORRECAO)) {
			tag = "infEvento";
		}
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		xml = UteisNfe.removerQuebraLinhaFinalTag(xml);
		xml = UteisNfe.removerEspacoDuplo(xml);
		Document doc = factory.newDocumentBuilder().parse(new ByteArrayInputStream(xml.getBytes()));
		NodeList elements = doc.getElementsByTagName(tag);
		Element el = (Element) elements.item(0);
		String id = el.getAttribute("Id");
		el.setIdAttribute("Id", true);
		el.setIdAttributeNS(null, "Id", true);
		Attr idAtrr = el.getAttributeNode("Id");
		el.setIdAttributeNode(idAtrr, true);
		Reference r = sig.newReference("#".concat(id), sig.newDigestMethod(DigestMethod.SHA1, null), transformList, null, null);
		si = sig.newSignedInfo(sig.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE, (C14NMethodParameterSpec) null), sig.newSignatureMethod(SignatureMethod.RSA_SHA1, null), Collections.singletonList(r));
		KeyInfoFactory kif = sig.getKeyInfoFactory();
		List x509Content = new ArrayList();
		x509Content.add(cert);
		X509Data xd = kif.newX509Data(x509Content);
		ki = kif.newKeyInfo(Collections.singletonList(xd));
		NodeList elements2;
		if (tipoServico.equals(Servicos.ENVIAR)) {
			elements2 = doc.getElementsByTagName("ns1:ReqEnvioLoteRPS");
		} else if (tipoServico.equals(Servicos.EVENTO_CANCELAMENTO)) {
			elements2 = doc.getElementsByTagName("evento");
		} else if (tipoServico.equals(Servicos.CANCELAR)) {
			elements2 = doc.getElementsByTagName("cancNFe");
		} else if (tipoServico.equals(Servicos.CARTACORRECAO)) {
			elements2 = doc.getElementsByTagName("evento");
		} else {
			elements2 = doc.getElementsByTagName("inutNFe");
		}
		Element el2 = (Element) elements2.item(0);
		DOMSignContext dsc = new DOMSignContext(getChavePrivadaNFSe(notaFiscalSaidaVO, certificado), el2);
		XMLSignature signature = sig.newXMLSignature(si, ki);
		signature.sign(dsc);
		if (tipoServico.equals(Servicos.EVENTO_CANCELAMENTO)) {
			nomeArquivoXML = nomeArquivoXML + "EventoCancelamento";
		}
		if (tipoServico.equals(Servicos.CANCELAR)) {
			nomeArquivoXML = nomeArquivoXML + "Cancelamento";
		}
		if (tipoServico.equals(Servicos.INUTILIZAR)) {
			nomeArquivoXML = nomeArquivoXML + "Inutilizacao";
		}
		if (tipoServico.equals(Servicos.CARTACORRECAO)) {
			nomeArquivoXML = nomeArquivoXML + "CartaCorrecao";
		}
		OutputStream os = new FileOutputStream(nomeArquivoXML);
		doc.setXmlStandalone(true);
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer trans = tf.newTransformer();
		trans.transform(new DOMSource(doc), new StreamResult(os));
		return UteisNfe.converterXMLparaString(doc);
	}

	public static String assinarNFSeInfRpsComID(NotaFiscalSaidaVO notaFiscalSaidaVO, ConfiguracaoGeralSistemaVO conSistemaVO, String xml, String tipoServico, String nomeArquivoXML, String prefixoTag) throws Exception {
		byte[] certificado = UteisNfe.getCertificado(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO(), conSistemaVO);
		carregarCertificado(certificado, notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getSenhaCertificado());
		sig = XMLSignatureFactory.getInstance("DOM");
		ArrayList<Transform> transformList = new ArrayList<Transform>();
		Transform enveloped = sig.newTransform(Transform.ENVELOPED, (TransformParameterSpec) null);
		Transform c14n = sig.newTransform(C14N_TRANSFORM_METHOD, (TransformParameterSpec) null);
		transformList.add(enveloped);
		transformList.add(c14n);
		String tag = "";

		xml = UteisNfe.removerQuebraLinhaFinalTag(xml);
		xml = UteisNfe.removerEspacoDuplo(xml);
		if (tipoServico.equals(Servicos.ENVIAR)) {
			tag = prefixoTag + "InfRps";
		} else if (tipoServico.equals("GerarNfseEnvio")) {
			tag = prefixoTag + "LoteRps";
		} else if (tipoServico.equals("LoteRps")) {
			tag = prefixoTag + "LoteRps";
		} else if (tipoServico.equals(prefixoTag + "GerarNfseEnvio")) {
			tag = prefixoTag + "InfDeclaracaoPrestacaoServico";
		} else if (tipoServico.equals("EnviarLoteRpsEnvio")) {
			if (prefixoTag.isEmpty()) {
				tag = "LoteRps";
			} else {
				tag = prefixoTag + "LoteRps";
			}
		}
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		xml = UteisNfe.removerQuebraLinhaFinalTag(xml);
		xml = UteisNfe.removerEspacoDuplo(xml);
		factory.setNamespaceAware(true);
		Document doc = factory.newDocumentBuilder().parse(new ByteArrayInputStream(xml.getBytes()));
		NodeList elements = doc.getElementsByTagName(tag);
		Element el = (Element) elements.item(0);
		String id = el.getAttribute("Id");
		el.setIdAttribute("Id", true);
		el.setIdAttributeNS(null, "Id", true);
		Attr idAtrr = el.getAttributeNode("Id");
		el.setIdAttributeNode(idAtrr, true);
		Reference r = sig.newReference("#".concat(id), sig.newDigestMethod(DigestMethod.SHA1, null), transformList, null, null);
		si = sig.newSignedInfo(sig.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE, (C14NMethodParameterSpec) null),
		        sig.newSignatureMethod(SignatureMethod.RSA_SHA1, null), Collections.singletonList(r));
		KeyInfoFactory kif = sig.getKeyInfoFactory();
		List x509Content = new ArrayList();
		x509Content.add(cert);
		X509Data xd = kif.newX509Data(x509Content);
		ki = kif.newKeyInfo(Collections.singletonList(xd));
		NodeList elements2 = null;
		if (tipoServico.equals(Servicos.ENVIAR)) {
			elements2 = doc.getElementsByTagName(prefixoTag + "Rps");
		} else if (tipoServico.equals(prefixoTag + "LoteRps")) {
			elements2 = doc.getElementsByTagName("EnviarLoteRpsEnvio");
		} else if (tipoServico.equals(prefixoTag + "GerarNfseEnvio")) {
			elements2 = doc.getElementsByTagName(prefixoTag + "GerarNfseEnvio");
		} else if (tipoServico.equals(prefixoTag + "GerarNfseEnvio")) {
			tag = prefixoTag + "GerarNfseEnvio";
		} else if (tipoServico.equals("EnviarLoteRpsEnvio")) {
			if (!prefixoTag.isEmpty()) {
				elements2 = doc.getElementsByTagName(prefixoTag + "EnviarLoteRpsEnvio");
			} else {
				elements2 = doc.getElementsByTagName("EnviarLoteRpsEnvio");
			}
		}
		Element el2 = (Element) elements2.item(0);
		DOMSignContext dsc = new DOMSignContext(getChavePrivadaNFSe(notaFiscalSaidaVO, certificado), el2);
		XMLSignature signature = sig.newXMLSignature(si, ki);
		signature.sign(dsc);
		OutputStream os = new FileOutputStream(nomeArquivoXML);
		doc.setXmlStandalone(true);
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer trans = tf.newTransformer();
		trans.transform(new DOMSource(doc), new StreamResult(os));
		return UteisNfe.converterXMLparaString(doc);
	}
	
	public static String assinarNFSeAraguaina(NotaFiscalSaidaVO notaFiscalSaidaVO, ConfiguracaoGeralSistemaVO conSistemaVO, String xml, String tipoServico, String nomeArquivoXML, String prefixoTag) throws Exception {
		byte[] certificado = UteisNfe.getCertificado(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO(), conSistemaVO);
		carregarCertificado(certificado, notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getSenhaCertificado());
		sig = XMLSignatureFactory.getInstance("DOM");
		ArrayList<Transform> transformList = new ArrayList<Transform>();
		Transform enveloped = sig.newTransform(Transform.ENVELOPED, (TransformParameterSpec) null);
		Transform c14n = sig.newTransform(C14N_TRANSFORM_METHOD, (TransformParameterSpec) null);
		transformList.add(enveloped);
		transformList.add(c14n);

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		xml = UteisNfe.removerQuebraLinhaFinalTag(xml);
		xml = UteisNfe.removerEspacoDuplo(xml);
		factory.setNamespaceAware(true);
		Document doc = factory.newDocumentBuilder().parse(new ByteArrayInputStream(xml.getBytes()));
		NodeList elements = doc.getElementsByTagName("InfDeclaracaoPrestacaoServico");
		Element el = (Element) elements.item(0);
		String id = el.getAttribute("Id");
		el.setIdAttribute("Id", true);
		el.setIdAttributeNS(null, "Id", true);
		Attr idAtrr = el.getAttributeNode("Id");
		el.setIdAttributeNode(idAtrr, true);
		Reference r = sig.newReference("#".concat(id), sig.newDigestMethod(DigestMethod.SHA1, null), transformList, null, null);
		si = sig.newSignedInfo(sig.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE, (C14NMethodParameterSpec) null),
		        sig.newSignatureMethod(SignatureMethod.RSA_SHA1, null), Collections.singletonList(r));
		KeyInfoFactory kif = sig.getKeyInfoFactory();
		List x509Content = new ArrayList();
		x509Content.add(cert);
		X509Data xd = kif.newX509Data(x509Content);
		ki = kif.newKeyInfo(Collections.singletonList(xd));
		NodeList elements2 = null;
		
		elements2 = doc.getElementsByTagName(prefixoTag + "Rps");
		
		Element el2 = (Element) elements2.item(0);
		DOMSignContext dsc = new DOMSignContext(getChavePrivadaNFSe(notaFiscalSaidaVO, certificado), el2);
		XMLSignature signature = sig.newXMLSignature(si, ki);
		signature.sign(dsc);
		OutputStream os = new FileOutputStream(nomeArquivoXML);
		doc.setXmlStandalone(true);
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer trans = tf.newTransformer();
		trans.transform(new DOMSource(doc), new StreamResult(os));
		return UteisNfe.converterXMLparaString(doc);
	}

	public static String assinarNFSeInfRpsComIDCaixaBaixa(NotaFiscalSaidaVO notaFiscalSaidaVO, ConfiguracaoGeralSistemaVO conSistemaVO, String xml, String tipoServico, String nomeArquivoXML, String prefixoTag) throws Exception {
		byte[] certificado = UteisNfe.getCertificado(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO(), conSistemaVO);
		carregarCertificado(certificado, notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getSenhaCertificado());
		sig = XMLSignatureFactory.getInstance("DOM");
		ArrayList<Transform> transformList = new ArrayList<Transform>();
		Transform enveloped = sig.newTransform(Transform.ENVELOPED, (TransformParameterSpec) null);
		Transform c14n = sig.newTransform(C14N_TRANSFORM_METHOD, (TransformParameterSpec) null);
		transformList.add(enveloped);
		transformList.add(c14n);
		String tag = "";
		if (tipoServico.equals(Servicos.ENVIAR)) {
			tag = prefixoTag + "InfRps";
		} else if (tipoServico.equals("LoteRps")) {
			tag = prefixoTag + "LoteRps";
		} else if (tipoServico.equals(prefixoTag + "EnviarLoteRpsEnvio")) {
			if (notaFiscalSaidaVO.getWebServicesNFSEEnum().equals(WebServicesNFSEEnum.MACEIO_AL_GERAR)) {
				tag = "LoteRps";
			} else {
				tag = prefixoTag + "LoteRps";
			}
		}
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		xml = UteisNfe.removerQuebraLinhaFinalTag(xml);
		xml = UteisNfe.removerEspacoDuplo(xml);
		Document doc = factory.newDocumentBuilder().parse(new ByteArrayInputStream(xml.getBytes()));
		NodeList elements = doc.getElementsByTagName(tag);
		Element el = (Element) elements.item(0);
		String id = el.getAttribute("id");
		el.setIdAttribute("id", true);
		el.setIdAttributeNS(null, "id", true);
		Attr idAtrr = el.getAttributeNode("id");
		el.setIdAttributeNode(idAtrr, true);
		Reference r = sig.newReference("#".concat(id), sig.newDigestMethod(DigestMethod.SHA1, null), transformList, null, null);
		si = sig.newSignedInfo(sig.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE, (C14NMethodParameterSpec) null), sig.newSignatureMethod(SignatureMethod.RSA_SHA1, null), Collections.singletonList(r));
		KeyInfoFactory kif = sig.getKeyInfoFactory();
		List x509Content = new ArrayList();
		x509Content.add(cert);
		X509Data xd = kif.newX509Data(x509Content);
		ki = kif.newKeyInfo(Collections.singletonList(xd));
		NodeList elements2;
		if (tipoServico.equals(Servicos.ENVIAR)) {
			elements2 = doc.getElementsByTagName(prefixoTag + "Rps");
		} else if (tipoServico.equals(prefixoTag + "LoteRps")) {
			elements2 = doc.getElementsByTagName(prefixoTag + "EnviarLoteRpsEnvio");
		} else if (tipoServico.equals(prefixoTag + "GerarNfseEnvio")) {
			elements2 = doc.getElementsByTagName(prefixoTag + "GerarNfseEnvio");
		} else if (tipoServico.equals(prefixoTag + "EnviarLoteRpsEnvio")) {
			elements2 = doc.getElementsByTagName(prefixoTag + "EnviarLoteRpsEnvio");
		} else {
			elements2 = doc.getElementsByTagName(prefixoTag + "inutNFe");
		}
		Element el2 = (Element) elements2.item(0);
		DOMSignContext dsc = new DOMSignContext(getChavePrivadaNFSe(notaFiscalSaidaVO, certificado), el2);
		XMLSignature signature = sig.newXMLSignature(si, ki);
		signature.sign(dsc);
		OutputStream os = new FileOutputStream(nomeArquivoXML);
		doc.setXmlStandalone(true);
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer trans = tf.newTransformer();
		trans.transform(new DOMSource(doc), new StreamResult(os));
		return UteisNfe.converterXMLparaString(doc);
	}

	public static String assinarNFSeLoteRpsComID(NotaFiscalSaidaVO notaFiscalSaidaVO, ConfiguracaoGeralSistemaVO conSistemaVO, String xml, String tipoServico, String nomeArquivoXML, String prefixoTag) throws Exception {
		byte[] certificado = UteisNfe.getCertificado(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO(), conSistemaVO);
		carregarCertificado(certificado, notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getSenhaCertificado());
		sig = XMLSignatureFactory.getInstance("DOM");
		ArrayList<Transform> transformList = new ArrayList<Transform>();
		Transform enveloped = sig.newTransform(Transform.ENVELOPED, (TransformParameterSpec) null);
		Transform c14n = sig.newTransform(C14N_TRANSFORM_METHOD, (TransformParameterSpec) null);
		transformList.add(enveloped);
		transformList.add(c14n);
		String tag = "";
		if (tipoServico.equals(Servicos.ENVIAR)) {
			tag = prefixoTag + "LoteRps";
		} else if (tipoServico.equals(Servicos.EVENTO_CANCELAMENTO)) {
			tag = prefixoTag + "infEvento";
		} else if (tipoServico.equals(Servicos.CANCELAR)) {
			tag = prefixoTag + "infCanc";
		} else if (tipoServico.equals(Servicos.INUTILIZAR)) {
			tag = prefixoTag + "infInut";
		} else if (tipoServico.equals(Servicos.CARTACORRECAO)) {
			tag = prefixoTag + "infEvento";
		} else if (tipoServico.equals("GerarNfseEnvioLoteRps")) {
			tag = prefixoTag + "LoteRps";
		} else if (tipoServico.equals("infRps")) {
			tag = prefixoTag + "infRps";
		}
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		Document doc = factory.newDocumentBuilder().parse(new ByteArrayInputStream(xml.getBytes()));
		NodeList elements = doc.getElementsByTagName(tag);
		Element el = (Element) elements.item(0);
		String id = el.getAttribute("Id");
		el.setIdAttribute("Id", true);
		el.setIdAttributeNS(null, "Id", true);
		Attr idAtrr = el.getAttributeNode("Id");
		el.setIdAttributeNode(idAtrr, true);
		Reference r = sig.newReference("#".concat(id), sig.newDigestMethod(DigestMethod.SHA1, null), transformList, null, null);
		si = sig.newSignedInfo(sig.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE, (C14NMethodParameterSpec) null), sig.newSignatureMethod(SignatureMethod.RSA_SHA1, null), Collections.singletonList(r));
		KeyInfoFactory kif = sig.getKeyInfoFactory();
		List x509Content = new ArrayList();
		x509Content.add(cert);
		X509Data xd = kif.newX509Data(x509Content);
		ki = kif.newKeyInfo(Collections.singletonList(xd));
		NodeList elements2;
		if (tipoServico.equals(Servicos.ENVIAR)) {
			elements2 = doc.getElementsByTagName(prefixoTag + "EnviarLoteRpsEnvio");
		} else if (tipoServico.equals(Servicos.EVENTO_CANCELAMENTO)) {
			elements2 = doc.getElementsByTagName(prefixoTag + "evento");
		} else if (tipoServico.equals(Servicos.CANCELAR)) {
			elements2 = doc.getElementsByTagName(prefixoTag + "cancNFe");
		} else if (tipoServico.equals(Servicos.CARTACORRECAO)) {
			elements2 = doc.getElementsByTagName(prefixoTag + "evento");
		} else if (tipoServico.equals("GerarNfseEnvioLoteRps")) {
			elements2 = doc.getElementsByTagName(prefixoTag + "GerarNfseEnvio");
		} else if (tipoServico.equals("infRps")) {
			elements2 = doc.getElementsByTagName(prefixoTag + "Rps");
		} else {
			elements2 = doc.getElementsByTagName(prefixoTag + "inutNFe");
		}
		Element el2 = (Element) elements2.item(0);
		DOMSignContext dsc = new DOMSignContext(getChavePrivadaNFSe(notaFiscalSaidaVO, certificado), el2);
		XMLSignature signature = sig.newXMLSignature(si, ki);
		signature.sign(dsc);
		OutputStream os = new FileOutputStream(nomeArquivoXML);
		doc.setXmlStandalone(true);
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer trans = tf.newTransformer();
		trans.transform(new DOMSource(doc), new StreamResult(os));
		return UteisNfe.converterXMLparaString(doc);
	}

	public static String assinarNFSeInfRpsComIDSemQuebraLinha(NotaFiscalSaidaVO notaFiscalSaidaVO, ConfiguracaoGeralSistemaVO conSistemaVO, String xml, String tipoServico, String nomeArquivoXML, String prefixoTag) throws Exception {
		byte[] certificado = UteisNfe.getCertificado(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO(), conSistemaVO);
		carregarCertificado(certificado, notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getSenhaCertificado());
		sig = XMLSignatureFactory.getInstance("DOM");
		ArrayList<Transform> transformList = new ArrayList<Transform>();
		Transform enveloped = sig.newTransform(Transform.ENVELOPED, (TransformParameterSpec) null);
		Transform c14n = sig.newTransform(C14N_TRANSFORM_METHOD, (TransformParameterSpec) null);
		transformList.add(enveloped);
		transformList.add(c14n);
		String tag = "";
		if (tipoServico.equals(Servicos.ENVIAR)) {
			tag = prefixoTag + "InfRps";
		} else if (tipoServico.equals("GerarNfseEnvio")) {
			tag = prefixoTag + "LoteRps";
		} else if (tipoServico.equals("LoteRps")) {
			tag = prefixoTag + "LoteRps";
		} else if (tipoServico.equals(prefixoTag + "GerarNfseEnvio")) {
			tag = prefixoTag + "InfDeclaracaoPrestacaoServico";
		} else if (tipoServico.equals("EnviarLoteRpsEnvio")) {
			if (prefixoTag.isEmpty()) {
				tag = "LoteRps";
			} else {
				tag = prefixoTag + "LoteRps";
			}
		}
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		Document doc = factory.newDocumentBuilder().parse(new ByteArrayInputStream(xml.getBytes()));
		NodeList elements = doc.getElementsByTagName(tag);
		Element el = (Element) elements.item(0);
		String id = el.getAttribute("Id");
		el.setIdAttribute("Id", true);
		el.setIdAttributeNS(null, "Id", true);
		Attr idAtrr = el.getAttributeNode("Id");
		el.setIdAttributeNode(idAtrr, true);
		Reference r = sig.newReference("#".concat(id), sig.newDigestMethod(DigestMethod.SHA1, null), transformList, null, null);
		si = sig.newSignedInfo(sig.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE, (C14NMethodParameterSpec) null),
		        sig.newSignatureMethod(SignatureMethod.RSA_SHA1, null), Collections.singletonList(r));
		KeyInfoFactory kif = sig.getKeyInfoFactory();
		List x509Content = new ArrayList();
		x509Content.add(cert);
		X509Data xd = kif.newX509Data(x509Content);
		ki = kif.newKeyInfo(Collections.singletonList(xd));
		NodeList elements2 = null;
		if (tipoServico.equals(Servicos.ENVIAR)) {
			elements2 = doc.getElementsByTagName(prefixoTag + "Rps");
		} else if (tipoServico.equals(prefixoTag + "LoteRps")) {
			elements2 = doc.getElementsByTagName("EnviarLoteRpsEnvio");
		} else if (tipoServico.equals(prefixoTag + "GerarNfseEnvio")) {
			elements2 = doc.getElementsByTagName(prefixoTag + "GerarNfseEnvio");
		} else if (tipoServico.equals(prefixoTag + "GerarNfseEnvio")) {
			tag = prefixoTag + "GerarNfseEnvio";
		} else if (tipoServico.equals("EnviarLoteRpsEnvio")) {
			if (!prefixoTag.isEmpty()) {
				elements2 = doc.getElementsByTagName(prefixoTag + "EnviarLoteRpsEnvio");
			} else {
				elements2 = doc.getElementsByTagName("EnviarLoteRpsEnvio");
			}
		}
		Element el2 = (Element) elements2.item(0);
		DOMSignContext dsc = new DOMSignContext(getChavePrivadaNFSe(notaFiscalSaidaVO, certificado), el2);
		XMLSignature signature = sig.newXMLSignature(si, ki);
		signature.sign(dsc);
		OutputStream os = new FileOutputStream(nomeArquivoXML);
		doc.setXmlStandalone(true);
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer trans = tf.newTransformer();
		trans.transform(new DOMSource(doc), new StreamResult(os));
		return UteisNfe.converterXMLparaString(doc);
	}

	public static String assinarNFSeInfRpsSemID(NotaFiscalSaidaVO notaFiscalSaidaVO, ConfiguracaoGeralSistemaVO conSistemaVO, String xml, String tipoServico, String nomeArquivoXML, String prefixoTag) throws Exception {
		byte[] certificado = UteisNfe.getCertificado(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO(), conSistemaVO);
		carregarCertificado(certificado, notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getSenhaCertificado());
		sig = XMLSignatureFactory.getInstance("DOM");
		ArrayList<Transform> transformList = new ArrayList<Transform>();
		Transform enveloped = sig.newTransform(Transform.ENVELOPED, (TransformParameterSpec) null);
		Transform c14n = sig.newTransform(C14N_TRANSFORM_METHOD, (TransformParameterSpec) null);
		transformList.add(enveloped);
		transformList.add(c14n);
		String tag = "";
		xml = UteisNfe.removerQuebraLinhaFinalTag(xml);
		xml = UteisNfe.removerEspacoDuplo(xml);
		if (tipoServico.equals(Servicos.ENVIAR)) {
			tag = prefixoTag + "InfRps";
		} else if (tipoServico.equals("GerarNfseEnvio")) {
			tag = prefixoTag + "LoteRps";
		} else if (tipoServico.equals("LoteRps")) {
			tag = prefixoTag + "LoteRps";
		} else if (tipoServico.equals(prefixoTag + "GerarNfseEnvio")) {
			tag = prefixoTag + "InfDeclaracaoPrestacaoServico";
		} else if (tipoServico.equals("EnviarLoteRpsEnvio")) {
			if (prefixoTag.isEmpty()) {
				tag = "LoteRps";
			} else {
				tag = prefixoTag + "LoteRps";
			}
		}
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		xml = UteisNfe.removerQuebraLinhaFinalTag(xml);
		xml = UteisNfe.removerEspacoDuplo(xml);
		Document doc = factory.newDocumentBuilder().parse(new ByteArrayInputStream(xml.getBytes()));
		NodeList elements = doc.getElementsByTagName(tag);
		Element el = (Element) elements.item(0);
		Reference r = sig.newReference("", sig.newDigestMethod(DigestMethod.SHA1, null), transformList, null, null);
		si = sig.newSignedInfo(sig.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE, (C14NMethodParameterSpec) null), sig.newSignatureMethod(SignatureMethod.RSA_SHA1, null), Collections.singletonList(r));
		KeyInfoFactory kif = sig.getKeyInfoFactory();
		List x509Content = new ArrayList();
		x509Content.add(cert);
		X509Data xd = kif.newX509Data(x509Content);
		ki = kif.newKeyInfo(Collections.singletonList(xd));
		NodeList elements2 = null;
		if (tipoServico.equals(Servicos.ENVIAR)) {
			elements2 = doc.getElementsByTagName(prefixoTag + "Rps");
		} else if (tipoServico.equals(prefixoTag + "LoteRps")) {
			elements2 = doc.getElementsByTagName("EnviarLoteRpsEnvio");
		} else if (tipoServico.equals(prefixoTag + "GerarNfseEnvio")) {
			elements2 = doc.getElementsByTagName(prefixoTag + "GerarNfseEnvio");
		} else if (tipoServico.equals(prefixoTag + "GerarNfseEnvio")) {
			tag = prefixoTag + "GerarNfseEnvio";
		} else if (tipoServico.equals("EnviarLoteRpsEnvio")) {
			if (!prefixoTag.isEmpty()) {
				elements2 = doc.getElementsByTagName(prefixoTag + "EnviarLoteRpsEnvio");
			} else {
				elements2 = doc.getElementsByTagName("EnviarLoteRpsEnvio");
			}
		}
		Element el2 = (Element) elements2.item(0);
		DOMSignContext dsc = new DOMSignContext(getChavePrivadaNFSe(notaFiscalSaidaVO, certificado), el2);
		XMLSignature signature = sig.newXMLSignature(si, ki);
		signature.sign(dsc);
		OutputStream os = new FileOutputStream(nomeArquivoXML);
		doc.setXmlStandalone(true);
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer trans = tf.newTransformer();
		trans.transform(new DOMSource(doc), new StreamResult(os));
		return UteisNfe.converterXMLparaString(doc);
	}

	public static Date getDataValidadeCertificado(String certificado, String senha) throws Exception {
		carregarCertificado(certificado, senha);
		return cert.getNotAfter();
	}

	public static void carregarCertificado(String caminhoCertificado, String senha) throws Exception {
		InputStream fs = new FileInputStream(caminhoCertificado);
		carregarCertificado(fs, senha);
	}

	@SuppressWarnings("rawtypes")
	public static void carregarCertificado(InputStream fs, String senha) throws Exception {
		rep = KeyStore.getInstance("PKCS12");
		try {
			rep.load(fs, senha.toCharArray());
		} catch (Exception e) {
			throw new Exception("Senha inválida do certificado de emissão da nota fiscal.");
		}
		Enumeration aliasesEnum = rep.aliases();
		String aliasChave = "";
		while (aliasesEnum.hasMoreElements()) {
			aliasChave = (String) aliasesEnum.nextElement();
			if (rep.isKeyEntry(aliasChave)) {
				System.out.println(aliasChave);
			}
		}
		cert = (X509Certificate) rep.getCertificate(aliasChave);
		validarCertificado();
	}

}
