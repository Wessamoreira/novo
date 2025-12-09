package webservice.lacuna;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Proxy;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

import com.lacunasoftware.restpki.Authentication;
import com.lacunasoftware.restpki.FullXmlSignatureStarter;
import com.lacunasoftware.restpki.RestException;
import com.lacunasoftware.restpki.RestPkiClient;
import com.lacunasoftware.restpki.SecurityContext;
import com.lacunasoftware.restpki.SignaturePolicy;
import com.lacunasoftware.restpki.XmlElementSignatureStarter;
import com.lacunasoftware.restpki.XmlInsertionOptions;
import com.lacunasoftware.restpki.XmlNamespaceManager;
import com.lacunasoftware.restpki.XmlSignatureFinisher;

import negocio.comuns.academico.DocumentoAssinadoVO;
import negocio.comuns.academico.enumeradores.TipoOrigemDocumentoAssinadoEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.lacuna.ServicoIntegracaoLacunaInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class ServicoIntegracaoLacuna extends ControleAcesso implements ServicoIntegracaoLacunaInterfaceFacade {

	private static final long serialVersionUID = 1801262272287368543L;
	private String RESTPKI_ACCESS_TOKEN = "zvvCA5wpaxqrZcgwyRwyZVAGfKd_vJHNfi9uMVw7DSbL2D6bzShTf-L9xGe6dOoE887w6C4y5uNTkpk1_t4Lw03EfJ9cb16Qv7oYzg0k6S_1NJXZA0oR44pPnte-6zfR61RE1tsb8YUPpiqr7umvW3pEwsdqay971FScgJ5oDJR92LG3ybOKo70ObBIll6k1J2cMF6eLPH_SPv0XQMGrDOQ5pyQOwax0Ed3ef-THlqgImPAVjtSF2gtk3Zh-aTbjCW3eSNntjmGaGUUeZ7JszrXrbVTbnAqz3mKQjXz4ltPXQgAsHpF4cQzELSzNei7z0_jYKCp0mYc16VTy7-SlF7aC3iTm8vl2aYqPQ60oQouzNDrLgtMbdCBYFxQQP_2vJuVAGSXY0c7ns2nveceLKJBnem-sVyZwX7AEGG5zFlBHbp-tyHFW8PiowD8oCpTmABpeHcuXVFEwlrA46gEZC9La1ITzqydjAQYGeaNCRQYNRiTF";

	/**
	 * validação se a ordem de assinatura atual é uma assinatura que não dependen de
	 * um ID para realizar a assinatura
	 * 
	 * @param documentoAssinadoVO
	 * @param ordemAssinatura
	 * @author Felipi Alves
	 * @chamado 42281
	 */
	private Boolean assinaturaCompletaXML(DocumentoAssinadoVO documentoAssinadoVO, Integer ordemAssinatura) {
		if (documentoAssinadoVO.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.DIPLOMA_DIGITAL) && documentoAssinadoVO.obterTipoAssinanteOrdemAssinatura(ordemAssinatura).equals("IESRegistradora")) {
			return Boolean.TRUE;
		} else if (documentoAssinadoVO.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.DOCUMENTACAO_ACADEMICA_REGISTRO_DIPLOMA_DIGITAL) && documentoAssinadoVO.obterTipoAssinanteOrdemAssinaturaDocumentacao(ordemAssinatura).equals("IESEmissoraRegistro")) {
			return Boolean.TRUE;
		} else if (documentoAssinadoVO.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.HISTORICO_DIGITAL)) {
			return Boolean.TRUE;
		} else if (documentoAssinadoVO.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.CURRICULO_ESCOLAR_DIGITAL)) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public SecurityContext getSecurityContext() {
		Boolean homologacao = Boolean.FALSE;
		if (homologacao) {
			return SecurityContext.lacunaTest;
		} else {
			return SecurityContext.pkiBrazil;
		}
	}

	/**
	 * Política de assinatura que vai ser utilizada ao assinar o xml pela lacuna,
	 * política de acordo com a ordem que irá ser assinada
	 * 
	 * @param documentoAssinadoVO
	 * @param ordemAssinatura
	 * @author Felipi Alves
	 * @chamado 42281
	 */
	public SignaturePolicy getToSignElementId(DocumentoAssinadoVO documentoAssinadoVO, Integer ordemAssinatura) {
		if (documentoAssinadoVO.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.DIPLOMA_DIGITAL)) {
			String tipoAssinante = documentoAssinadoVO.obterTipoAssinanteOrdemAssinatura(ordemAssinatura);
			if (tipoAssinante.equals("IESRepresentantes")) {
				return SignaturePolicy.PkiBrazilXadesAdrCompleta;
			} else if (tipoAssinante.equals("IESEmissora")) {
				return SignaturePolicy.PkiBrazilXadesAdrCompleta;
			} else if (tipoAssinante.equals("PessoasFisicas")) {
				return SignaturePolicy.PkiBrazilXadesAdrCompleta;
			} else if (tipoAssinante.equals("IESRegistradora")) {
				return SignaturePolicy.PkiBrazilXadesAdrArquivamento;
			}
		} else if (documentoAssinadoVO.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.DOCUMENTACAO_ACADEMICA_REGISTRO_DIPLOMA_DIGITAL)) {
			String tipoAssinante = documentoAssinadoVO.obterTipoAssinanteOrdemAssinaturaDocumentacao(ordemAssinatura);
			if (tipoAssinante.equals("Representantes")) {
				return SignaturePolicy.PkiBrazilXadesAdrCompleta;
			} else if (tipoAssinante.equals("IESEmissoraDadosDiploma")) {
				return SignaturePolicy.PkiBrazilXadesAdrCompleta;
			} else if (tipoAssinante.equals("IESEmissoraRegistro")) {
				return SignaturePolicy.PkiBrazilXadesAdrArquivamento;
			}
		} else if (documentoAssinadoVO.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.HISTORICO_DIGITAL)) {
			String tipoAssinante = documentoAssinadoVO.obterTipoAssinanteOrdemAssinaturaHistorico(ordemAssinatura);
			if (tipoAssinante.equals("IESEmissora")) {
				return SignaturePolicy.PkiBrazilXadesAdrTempo;
			} else if (tipoAssinante.equals("IESRegistradora")) {
				return SignaturePolicy.PkiBrazilXadesAdrArquivamento;
			}
		} else if (documentoAssinadoVO.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.CURRICULO_ESCOLAR_DIGITAL)) {
			String tipoAssinante = documentoAssinadoVO.obterTipoAssinanteOrdemAssinaturaCurriculo(ordemAssinatura);
			if (tipoAssinante.equals("IESRepresentantes")) {
				return SignaturePolicy.PkiBrazilXadesAdrCompleta;
			} else if (tipoAssinante.equals("IESEmissora")) {
				return SignaturePolicy.PkiBrazilXadesAdrArquivamento;
			}
		}
		return null;
	}

	/**
	 * Id aonde irá ser implantada a assinatura, sendo que quando retornar null vai
	 * ser uma assinatura completa
	 * 
	 * @param documentoAssinadoVO
	 * @param ordemAssinatura
	 * @author Felipi Alves
	 * @chamado 42281
	 */
	public String getElementToSIgnId(DocumentoAssinadoVO documentoAssinadoVO, Integer ordemAssinatura) {
		if (documentoAssinadoVO.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.DIPLOMA_DIGITAL)) {
			String tipoAssinante = documentoAssinadoVO.obterTipoAssinanteOrdemAssinatura(ordemAssinatura);
			if (tipoAssinante.equals("IESRepresentantes")) {
				return documentoAssinadoVO.getIdDiplomaDigital();
			} else if (tipoAssinante.equals("IESEmissora")) {
				return documentoAssinadoVO.getIdDiplomaDigital();
			} else if (tipoAssinante.equals("PessoasFisicas")) {
				return documentoAssinadoVO.getIdDadosRegistrosDiplomaDigital();
			} else if (tipoAssinante.equals("IESRegistradora")) {
				return null;
			}
		} else if (documentoAssinadoVO.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.DOCUMENTACAO_ACADEMICA_REGISTRO_DIPLOMA_DIGITAL)) {
			String tipoAssinante = documentoAssinadoVO.obterTipoAssinanteOrdemAssinaturaDocumentacao(ordemAssinatura);
			if (tipoAssinante.equals("Representantes")) {
				return documentoAssinadoVO.getIdDiplomaDigital();
			} else if (tipoAssinante.equals("IESEmissoraDadosDiploma")) {
				return documentoAssinadoVO.getIdDiplomaDigital();
			} else if (tipoAssinante.equals("IESEmissoraRegistro")) {
				return null;
			}
		} else if (documentoAssinadoVO.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.HISTORICO_DIGITAL)) {
			return null;
		} else if (documentoAssinadoVO.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.CURRICULO_ESCOLAR_DIGITAL)) {
			return null;
		}
		return null;
	}

	/**
	 * conveter o xml que vai ser assinado pela lacuma em um array de byte
	 * 
	 * @param documentoAssinado
	 * @author Felipi Alves
	 * @chamado 42281
	 */
	private byte[] getArquivoAssinar(DocumentoAssinadoVO documentoAssinado) throws Exception {
		if (documentoAssinado.getArquivo().getServidorArquivoOnline().isApache()) {
			File arquivoFisico = new File(getAplicacaoControle().getConfiguracaoGeralSistemaVO(0, null).getLocalUploadArquivoFixo() + File.separator + documentoAssinado.getArquivo().getPastaBaseArquivo() + File.separator + documentoAssinado.getArquivo().getNome());
			Uteis.checkState(!Uteis.isAtributoPreenchido(arquivoFisico), "O arquivo físico (XML) para ser assinado não foi encontrado no servidor de arquivo");
			return Files.readAllBytes(arquivoFisico.toPath());
		} else {
			return null;
		}
	}

	public void generateAccessTokenLacuna() throws RestException {
		Authentication auth = new Authentication(Util.getRestPkiClient(RESTPKI_ACCESS_TOKEN));
		auth.startWithWebPki(SecurityContext.pkiBrazil);
		getAplicacaoControle().setAuthenticationLacunaGenerated(Boolean.TRUE);
	}

	/**
	 * Método que vai retornar o token da lacuna que por sua vez foi gerada de
	 * acordo com a política de assinatura que vai ser realizada posteriormente
	 * 
	 * @param documentoAssinado
	 * @param ordemAssinatura
	 * @author Felipi Alves
	 * @chamado 42281
	 */
	@Override
	public String iniciandoRestPKI(DocumentoAssinadoVO documentoAssinado, Integer ordemAssinatura) throws Exception {
		if (!getAplicacaoControle().getAuthenticationLacunaGenerated()) {
			generateAccessTokenLacuna();
		}
		if (assinaturaCompletaXML(documentoAssinado, ordemAssinatura)) {
			FullXmlSignatureStarter signatureStarter = null;
			byte[] byteArquivoAssinar = null;
			try {
				signatureStarter = new FullXmlSignatureStarter(Util.getRestPkiClient(RESTPKI_ACCESS_TOKEN));
				byteArquivoAssinar = getArquivoAssinar(documentoAssinado);
				signatureStarter.setXml(byteArquivoAssinar);
				signatureStarter.setSignaturePolicy(getToSignElementId(documentoAssinado, ordemAssinatura));
				signatureStarter.setSecurityContext(getSecurityContext());
				String token = signatureStarter.startWithWebPki();
				return token;
			} finally {
				if (Objects.nonNull(signatureStarter)) {
					signatureStarter = null;
				}
				if (Objects.nonNull(byteArquivoAssinar)) {
					byteArquivoAssinar = null;
				}
			}
		} else {
			XmlElementSignatureStarter signatureStarter = null;
			byte[] byteArquivoAssinar = null;
			try {
				signatureStarter = new XmlElementSignatureStarter(Util.getRestPkiClient(RESTPKI_ACCESS_TOKEN));
				byteArquivoAssinar = getArquivoAssinar(documentoAssinado);
				signatureStarter.setXml(byteArquivoAssinar);
				signatureStarter.setElementToSIgnId(getElementToSIgnId(documentoAssinado, ordemAssinatura));
				signatureStarter.setSignatureElementLocation(".", new XmlNamespaceManager(), XmlInsertionOptions.AppendChild);
				signatureStarter.setSignaturePolicy(getToSignElementId(documentoAssinado, ordemAssinatura));
				signatureStarter.setSecurityContext(getSecurityContext());
				return signatureStarter.startWithWebPki();
			} finally {
				if (Objects.nonNull(signatureStarter)) {
					signatureStarter = null;
				}
				if (Objects.nonNull(byteArquivoAssinar)) {
					byteArquivoAssinar = null;
				}
			}
		}
	}

	/**
	 * Método que vai retornar o arquivo já assinado para depois trocar com o
	 * arquivo original do xml
	 * 
	 * @param documentoAssinado
	 * @param ordemAssinatura
	 * @param tokenLacuna
	 * @author Felipi Alves
	 * @chamado 42281
	 */
	@Override
	public File finalizandoRestPKI(DocumentoAssinadoVO documentoAssinado, Integer ordemAssinatura, String tokenLacuna) throws Exception {
		byte[] signedXml = null;
		File diploma = null;
		XmlSignatureFinisher signatureFinisher = new XmlSignatureFinisher(Util.getRestPkiClient(RESTPKI_ACCESS_TOKEN));
		signatureFinisher.setToken(tokenLacuna);
		signedXml = signatureFinisher.finish();
		File diretorioXmlAssinados = new File(getAplicacaoControle().getConfiguracaoGeralSistemaVO(0, null).getLocalUploadArquivoFixo() + File.separator + "xmlDiploma");
		if (!diretorioXmlAssinados.exists()) {
			diretorioXmlAssinados.mkdir();
		}
		File diretorioDocumentoAssinado = new File(getAplicacaoControle().getConfiguracaoGeralSistemaVO(0, null).getLocalUploadArquivoFixo() + File.separator + "xmlDiploma" + File.separator + documentoAssinado.getCodigo());
		if (!diretorioDocumentoAssinado.exists()) {
			diretorioDocumentoAssinado.mkdir();
		}
		String nomeArquivo = new Date().getTime() + "_" + ordemAssinatura + ".xml";
		diploma = new File(diretorioDocumentoAssinado + File.separator + nomeArquivo);
		Files.write(Paths.get(diploma.getPath()), signedXml);
		return diploma;
	}

	public static class Util {

		public static RestPkiClient getRestPkiClient() {
			return getRestPkiClient(null);
		}

		public static RestPkiClient getRestPkiClient(String token) {
			String accessToken = token;
			if (accessToken == null || accessToken.equals("") || accessToken.contains(" API ")) {
				throw new RuntimeException("The API access token was not set! Hint: to run this sample you must generate an API access token on the REST PKI website and paste it on the file src/main/resources/application.properties");
			}
			Proxy proxy = null;
			String endpoint = "http://pki.rest/";
			return new RestPkiClient(endpoint, accessToken, proxy);
		}

		public static SecurityContext getSecurityContextId() {
			return getSecurityContextId(null);
		}

		public static SecurityContext getSecurityContextId(String development) {
			if (Arrays.asList(development).contains("development")) {
				return SecurityContext.lacunaTest;
			} else {
				return SecurityContext.pkiBrazil;
			}
		}

		public static void setNoCacheHeaders(HttpServletResponse response) {
			response.setHeader("Expires", "-1");
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.setHeader("Pragma", "no-cache");
		}

		public static Path getSampleDocPath() throws IOException {
			return new ClassPathResource("/static/SampleDocument.pdf").getFile().toPath();
		}

		public static byte[] getSampleXml() throws IOException {
			Resource resource = new ClassPathResource("/static/SampleDocument.xml");
			InputStream fileStream = resource.getInputStream();
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			org.apache.commons.io.IOUtils.copy(fileStream, buffer);
			fileStream.close();
			buffer.flush();
			return buffer.toByteArray();
		}

		public static Path getSampleXmlPath() throws IOException {
			return new ClassPathResource("/static/SampleDocument.xml").getFile().toPath();
		}

		public static Path getSampleNFePath() throws IOException {
			return new ClassPathResource("/static/SampleNFe.xml").getFile().toPath();
		}

		public static byte[] getBatchDocContent(int id) throws IOException {
			Resource resource = new ClassPathResource("/static/" + String.format("%02d", id % 10) + ".pdf");
			InputStream fileStream = resource.getInputStream();
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			org.apache.commons.io.IOUtils.copy(fileStream, buffer);
			fileStream.close();
			buffer.flush();
			return buffer.toByteArray();
		}

		public static Path getBatchDocPath(int id) throws IOException {
			return new ClassPathResource("/static/" + String.format("%02d", id % 10) + ".pdf").getFile().toPath();
		}

		public static byte[] getPdfStampContent() throws IOException {
			Resource resource = new ClassPathResource("/static/PdfStamp.png");
			InputStream fileStream = resource.getInputStream();
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			IOUtils.copy(fileStream, buffer);
			fileStream.close();
			buffer.flush();
			return buffer.toByteArray();
		}

		public static byte[] getIcpBrasilLogoContent() throws IOException {
			Resource resource = new ClassPathResource("/static/icp-brasil.png");
			InputStream fileStream = resource.getInputStream();
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			IOUtils.copy(fileStream, buffer);
			fileStream.close();
			buffer.flush();
			return buffer.toByteArray();
		}

		public static Certificate getSampleCertificateFromPKCS12() throws IOException, GeneralSecurityException {
			String alias = "{ecaf2712-4631-4f0e-94d1-fa6fcbff329f}";
			String password = "1234";

			Resource resource = new ClassPathResource("/static/Pierre de Fermat.pfx");
			InputStream fileStream = resource.getInputStream();
			KeyStore certStore = KeyStore.getInstance("pkcs12");
			certStore.load(fileStream, password.toCharArray());
			Certificate certificate = certStore.getCertificate(alias);
			fileStream.close();
			return certificate;
		}

		public static Key getSampleKeyFromPKCS12() throws IOException, GeneralSecurityException {
			String alias = "{ecaf2712-4631-4f0e-94d1-fa6fcbff329f}";
			String password = "1234";

			Resource resource = new ClassPathResource("/static/Pierre de Fermat.pfx");
			InputStream fileStream = resource.getInputStream();
			KeyStore certStore = KeyStore.getInstance("pkcs12");
			certStore.load(fileStream, password.toCharArray());
			Key pkey = certStore.getKey(alias, password.toCharArray());
			fileStream.close();
			return pkey;
		}

		public static byte[] getValidationResultIcon(boolean isValid) throws IOException {
			String filename = isValid ? "ok.png" : "not-ok.png";
			Resource resource = new ClassPathResource("/static/" + filename);
			InputStream fileStream = resource.getInputStream();
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			org.apache.commons.io.IOUtils.copy(fileStream, buffer);
			fileStream.close();
			buffer.flush();
			return buffer.toByteArray();
		}

		public static Certificate getSampleCertificateFromMSCAPI() throws IOException, GeneralSecurityException {
			String alias = "Pierre de Fermat";
			String password = "";

			KeyStore certStore = KeyStore.getInstance("Windows-MY", "SunMSCAPI");
			certStore.load(null, password.toCharArray());
			return certStore.getCertificate(alias);
		}

		public static Key getSampleKeyFromMSCAPI() throws IOException, GeneralSecurityException {
			String alias = "Pierre de Fermat";
			String password = "";

			KeyStore certStore = KeyStore.getInstance("Windows-MY", "SunMSCAPI");
			certStore.load(null, password.toCharArray());
			return certStore.getKey(alias, password.toCharArray());
		}

		public static Path getSampleCodEnvelopePath() throws IOException {
			return new ClassPathResource("/static/SampleCodEnvelope.xml").getFile().toPath();
		}

		public static String joinStringsPt(List<String> strings) {
			StringBuilder text = new StringBuilder();
			int size = strings.size();
			int index = 0;
			for (String s : strings) {
				if (index > 0) {
					if (index < size - 1) {
						text.append(", ");
					} else {
						text.append(" e ");
					}
				}
				text.append(s);
				++index;
			}
			return text.toString();
		}

		public static byte[] convertFromBase64String(String contentBase64) {
			return Base64.decodeBase64(contentBase64);
		}

		public static String convertToBase64String(byte[] content) {
			byte[] base64 = Base64.encodeBase64(content);
			return new String(base64);
		}
	}
}