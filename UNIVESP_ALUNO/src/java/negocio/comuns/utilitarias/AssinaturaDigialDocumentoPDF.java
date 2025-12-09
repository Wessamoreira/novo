package negocio.comuns.utilitarias;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.security.BouncyCastleDigest;
import com.itextpdf.text.pdf.security.DigestAlgorithms;
import com.itextpdf.text.pdf.security.ExternalDigest;
import com.itextpdf.text.pdf.security.ExternalSignature;
import com.itextpdf.text.pdf.security.MakeSignature;
import com.itextpdf.text.pdf.security.MakeSignature.CryptoStandard;
import com.lowagie.text.pdf.BaseFont;
import com.itextpdf.text.pdf.security.PrivateKeySignature;

import controle.arquitetura.AplicacaoControle;
import controle.arquitetura.AssuntoDebugEnum;

public class AssinaturaDigialDocumentoPDF {

	private KeyStore rep;
	private X509Certificate x509Certificate;
	private PrivateKey pk;
	private Certificate[] chain;
	private Boolean token;
	private Float larguraAssinatura = 200f;
	private Float alturaAssinatura = 40f;
	private String corAssinaturaDigitalmente;
	private String pathKeyStore;
	private String senhaKeyStore;
	private String razaoCertificado;
	private String arquivoOrigem;
	private String caminhoArquivoDestino;
	private String nomeArquivoDestino;
	private Boolean isPosicaoAssinaturaEsquerdo = false;
	private Boolean isPosicaoAssinaturaCima = false;
	private Boolean isValidarArquivoExistente = false;
	private Date dataAssinatura;
	private File arquivoTemp = null;
	private float tamanhoFonte = 8f;
	private int coordenadaLLX = 0;
	private int coordenadaLLY = 0;
	private int coordenadaURX = 0;
	private int coordenadaURY = 0;
	private String cargo = "";
	private String titulo = "";
	private boolean apresentarAssinaturaDigital = true;
	private boolean apresentarAssinaturaUltimaPagina = true;
	private String ipAssinatura;
    private String longitude;
    private String latitude;
    private String dispositivoAssinatura;
	

	public void validarDadosParaGeracaoArquivo() throws Exception {
		if (!Uteis.isAtributoPreenchido(getPathKeyStore())) {
			throw new Exception("O caminho para o certificado digital não foi encontrado. Por favor verificar na configuração do sistema o campo informado.");
		}
		if (!Uteis.isAtributoPreenchido(getSenhaKeyStore())) {
			throw new Exception("A senha para o certificado digital não foi encontrado. Por favor verificar na configuração do sistema o campo informado.");
		}
		if (!Uteis.isAtributoPreenchido(getRazaoCertificado())) {
			throw new Exception("O campo Razão do certificado digital não foi informado.");
		}
		if (!Uteis.isAtributoPreenchido(getArquivoOrigem())) {
			throw new Exception("O caminho para o arquivo original do documento a ser assinado não foi informado.");
		}
		if (!Uteis.isAtributoPreenchido(getCaminhoArquivoDestino())) {
			throw new Exception("O caminho para o arquivo a ser assinado não foi informado.");
		}
		if (!Uteis.isAtributoPreenchido(getNomeArquivoDestino())) {
			throw new Exception("O nome do arquivo a ser assinado não foi informado.");
		}
	}

	public void realizarGeracaoDocumentoPdf() throws Exception {
		validarDadosParaGeracaoArquivo();
		carregarCertificado();
		assinarDocumento();
	}

	public void carregarCertificado() throws Exception {
		Security.addProvider(new BouncyCastleProvider());
		rep = getKeyStore();
		InputStream fs = new FileInputStream(pathKeyStore);
		rep.load(fs, senhaKeyStore.toCharArray());
		String aliasChave = (String) rep.aliases().nextElement();
		pk = (PrivateKey) rep.getKey(aliasChave, senhaKeyStore.toCharArray());
		chain = rep.getCertificateChain(aliasChave);
		x509Certificate = (X509Certificate) rep.getCertificate(aliasChave);
		
		
		validarCertificado();
	}

	private void validarCertificado() throws Exception {
		try {
			x509Certificate.checkValidity();
		} catch (CertificateExpiredException e) {
			throw new Exception("Certificado expirado!");
		} catch (CertificateNotYetValidException e) {
			throw new Exception("Certificado invalido!");
		} catch (NullPointerException e) {
			 throw new Exception("Não foi possível localizar/carregar o certificado de assinatura digital. Verifique o arquivo e tente novamente.");
		}
		
	}	
	
	private void assinarDocumento() throws Exception {
		PdfReader reader = null;
		PdfStamper stamper = null;
		Rectangle pagesize = null;
		FileOutputStream os = null;
		PdfSignatureAppearance appearance =  null;
		try {
			File fileParaAssina = validarCaminhoDoArquivoQueSeraAssinado();			
			criarArquivoTemporario();			
			reader = new PdfReader(arquivoTemp.getAbsolutePath());			
			os = new FileOutputStream(fileParaAssina);
			stamper = PdfStamper.createSignature(reader, os, '\0', null, true);
			appearance = stamper.getSignatureAppearance();
			
			apresentarAssinatura(reader, pagesize, appearance, false);
			Calendar car = Calendar.getInstance();
			car.setTime(getDataAssinatura());

			appearance.setReason(getRazaoCertificado());
				if(Uteis.isAtributoPreenchido(getNomeCertificadoCN()) && isApresentarAssinaturaDigital()) {
				StringBuilder textoAssinatura = new StringBuilder();
				SimpleDateFormat dataFormatada = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				String dataFormadataString = dataFormatada.format(getDataAssinatura());
					if(Uteis.isAtributoPreenchido(titulo)) {
						textoAssinatura.append("Assinado Por ").append(titulo).append(" ").append(getNomeCertificadoCN()).append("\n");
					}else {
				        textoAssinatura.append("Assinado Por ").append(getNomeCertificadoCN()).append("\n");
					}
					if(Uteis.isAtributoPreenchido(cargo)) {
						textoAssinatura.append("Cargo ").append(getCargo()).append("\n");
					}
					textoAssinatura.append("Data: ").append(dataFormadataString).append("\n");
					textoAssinatura.append("ID: ").append(getRazaoCertificado()).append("\n");
					if(Uteis.isAtributoPreenchido(getIpAssinatura())) {
						textoAssinatura.append("Ip: ").append(getIpAssinatura()).append("\n");
					}
					if(Uteis.isAtributoPreenchido(getDispositivoAssinatura())) {
						textoAssinatura.append("Dispositivo: ").append(getDispositivoAssinatura()).append("\n");
					}
					if(Uteis.isAtributoPreenchido(getLongitude()) && Uteis.isAtributoPreenchido(getLatitude())) {
						textoAssinatura.append("Geolocalização: Lat:").append(getLatitude()).append(", Long: ").append(getLongitude()).append("\n");
					}
					appearance.setLayer2Text(textoAssinatura.toString());
				}else {
				appearance.setSignDate(car);
				appearance.setReason(getRazaoCertificado());
			}
			
			ExternalSignature es = new PrivateKeySignature(pk, DigestAlgorithms.SHA256, "BC");
			ExternalDigest digest = new BouncyCastleDigest();
			MakeSignature. signDetached(appearance, digest, es, chain, null, null, null, 0, CryptoStandard.CMS);
			
		} catch (Exception e) {
			if (e.getMessage().contains("InvalidPdfException") && arquivoTemp != null && arquivoTemp.getName() != null) {
				throw new Exception("Arquivo " + arquivoTemp.getName() + " corrompido!");
			} else {
				throw e;
			}
		} finally {
			if(appearance != null) {
				appearance = null;
			}
			if (os != null) {
				os.close();
				os=  null;
			}
			if (stamper != null) {
				try {
				  stamper.close();
				}catch(Exception e){
					
				}
				stamper = null;
			}
			if (reader != null) {
				reader.close();
				reader= null;
			}
			if(arquivoTemp != null) {
			FileUtils.forceDelete(arquivoTemp);
			}
			arquivoTemp =  null;
		}

	}

	private void apresentarAssinatura(PdfReader reader, Rectangle pagesize, PdfSignatureAppearance appearance, Boolean realizandoCorrecaoPDF) throws Exception {
		if (Uteis.isAtributoPreenchido(getCorAssinaturaDigitalmente()) && isApresentarAssinaturaDigital() && !realizandoCorrecaoPDF) {
			if(!getCorAssinaturaDigitalmente().startsWith("#")) {
				setCorAssinaturaDigitalmente("#"+getCorAssinaturaDigitalmente());
			}
			if(getCorAssinaturaDigitalmente().length() < 7) {
				setCorAssinaturaDigitalmente("#000000");
			}
			Font layer2Font = FontFactory.getFont(UteisJSF.getCaminhoWeb() + "resources/assets/fonts/SEI/arial.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, tamanhoFonte);
			appearance.setLayer2Font(layer2Font);
			pagesize = reader.getPageSize(isApresentarAssinaturaUltimaPagina() ? reader.getNumberOfPages() : 1);
			if(getIsPosicaoAssinaturaEsquerdo() || getIsPosicaoAssinaturaCima()) {
				if (reader.getPageRotation(isApresentarAssinaturaUltimaPagina() ? reader.getNumberOfPages() : 1) == 90 || reader.getPageRotation(isApresentarAssinaturaUltimaPagina() ? reader.getNumberOfPages() : 1) == 270) {
					pagesize = reader.getPageSizeWithRotation(isApresentarAssinaturaUltimaPagina() ? reader.getNumberOfPages() : 1);
				}
				appearance.setVisibleSignature(getPosicaoAssinatura(pagesize), isApresentarAssinaturaUltimaPagina() ? reader.getNumberOfPages() : 1, null);
			} else {

				appearance.setVisibleSignature(
						new Rectangle(coordenadaLLX, coordenadaLLY, 								
								coordenadaLLX + larguraAssinatura, 
								coordenadaLLY + alturaAssinatura), 
						        isApresentarAssinaturaUltimaPagina() ? reader.getNumberOfPages() : 1, null);
			}
		}
	}

	private Rectangle getPosicaoAssinatura(Rectangle pagesize) {
		Rectangle rectangle = null;
		// Top left
		if (getIsPosicaoAssinaturaEsquerdo() && getIsPosicaoAssinaturaCima()) {
			rectangle = new Rectangle(pagesize.getLeft(coordenadaLLX), pagesize.getTop(alturaAssinatura + coordenadaLLY), pagesize.getLeft(larguraAssinatura + coordenadaURX), pagesize.getTop(+coordenadaURY));
		}
		// Top right
		if (!getIsPosicaoAssinaturaEsquerdo() && getIsPosicaoAssinaturaCima()) {
			rectangle = new Rectangle(pagesize.getRight(larguraAssinatura + coordenadaURX), pagesize.getTop(alturaAssinatura + coordenadaLLY), pagesize.getRight(coordenadaLLX), pagesize.getTop(+coordenadaURY));
		}
		// Bottom left
		if (getIsPosicaoAssinaturaEsquerdo() && !getIsPosicaoAssinaturaCima()) {
			rectangle = new Rectangle(pagesize.getLeft(coordenadaLLX), pagesize.getBottom(+coordenadaURY), pagesize.getLeft(larguraAssinatura + coordenadaURX), pagesize.getBottom(alturaAssinatura + coordenadaLLY));
		}
		// Bottom right
		if (!getIsPosicaoAssinaturaEsquerdo() && !getIsPosicaoAssinaturaCima()) {
			rectangle = new Rectangle(pagesize.getRight(larguraAssinatura + coordenadaURX), pagesize.getBottom(+coordenadaURY), pagesize.getRight(coordenadaLLX), pagesize.getBottom(alturaAssinatura + coordenadaLLY));
		}
		/*
		 * Rectangle pagesize = reader.getPageSizeWithRotation(1); if (reader.getPageRotation(1) == 90 || reader.getPageRotation(1) == 270) { pagesize = reader.getPageSizeWithRotation(1); } else { pagesize = reader.getPageSize(1); } float llx = pagesize.getRight() - 120 - 10; float lly = pagesize.getBottom() + 10; float urx = pagesize.getRight() - 10; float ury = pagesize.getBottom() + 40 + 10; rectangle = new Rectangle(llx, lly, urx, ury);
		 */
		return rectangle;
	}

	public void criarArquivoTemporario() throws IOException {
		arquivoTemp = new File(caminhoArquivoDestino + File.separator + (new Date().getTime()) + "_temp.pdf");
		FileUtils.copyFile(new File(getArquivoOrigem()), arquivoTemp);		
	}

	public File validarCaminhoDoArquivoQueSeraAssinado() throws Exception {
		File file = new File(getCaminhoAteArquivoDestino());
		if (!file.exists() && getIsValidarArquivoExistente()) {
			file.createNewFile();
		}else if (!file.exists()) {
			throw new Exception("Arquivo para ser assinado não foi encontrado no servidor de arquivos no caminho ("+(caminhoArquivoDestino+File.separator+nomeArquivoDestino)+")");
		}
		return file;
	}

	public KeyStore getKeyStore() throws Exception {
		if (getToken()) {
			return KeyStore.getInstance(KeyStore.getDefaultType());
		} else {
			return KeyStore.getInstance("pkcs12", "BC");
		}
	}

	public Boolean getToken() {
		return token;
	}

	public void setToken(Boolean token) {
		this.token = token;
	}

	public String getRazaoCertificado() {
		return razaoCertificado;
	}

	public void setRazaoCertificado(String razaoCertificado) {
		this.razaoCertificado = razaoCertificado;
	}

	public String getArquivoOrigem() {
		return arquivoOrigem;
	}

	public void setArquivoOrigem(String arquivoOrigem) {
		this.arquivoOrigem = arquivoOrigem;
	}

	public String getCaminhoArquivoDestino() {
		return caminhoArquivoDestino;
	}

	public void setCaminhoArquivoDestino(String caminhoArquivoDestino) {
		this.caminhoArquivoDestino = caminhoArquivoDestino;
	}

	public String getNomeArquivoDestino() {
		return nomeArquivoDestino;
	}

	public void setNomeArquivoDestino(String nomeArquivoDestino) {
		this.nomeArquivoDestino = nomeArquivoDestino;
	}

	public String getPathKeyStore() {
		return pathKeyStore;
	}

	public void setPathKeyStore(String pathKeyStore) {
		this.pathKeyStore = pathKeyStore;
	}

	public String getSenhaKeyStore() {
		return senhaKeyStore;
	}

	public void setSenhaKeyStore(String senhaKeyStore) {
		this.senhaKeyStore = senhaKeyStore;
	}

	public Boolean getIsPosicaoAssinaturaEsquerdo() {
		return isPosicaoAssinaturaEsquerdo;
	}

	public void setIsPosicaoAssinaturaEsquerdo(Boolean isPosicaoAssinaturaEsquerdo) {
		this.isPosicaoAssinaturaEsquerdo = isPosicaoAssinaturaEsquerdo;
	}

	public Boolean getIsPosicaoAssinaturaCima() {
		return isPosicaoAssinaturaCima;
	}

	public void setIsPosicaoAssinaturaCima(Boolean isPosicaoAssinaturaCima) {
		this.isPosicaoAssinaturaCima = isPosicaoAssinaturaCima;
	}

	public String getCaminhoAteArquivoDestino() {
		return caminhoArquivoDestino + File.separator + nomeArquivoDestino;
	}

	public String getCorAssinaturaDigitalmente() {
		if (corAssinaturaDigitalmente == null) {
			corAssinaturaDigitalmente = "";
		}
		return corAssinaturaDigitalmente;
	}

	public void setCorAssinaturaDigitalmente(String corAssinaturaDigitalmente) {
		this.corAssinaturaDigitalmente = corAssinaturaDigitalmente;
	}

	public Float getLarguraAssinatura() {
		return larguraAssinatura;
	}

	public void setLarguraAssinatura(Float larguraAssinatura) {
		this.larguraAssinatura = larguraAssinatura;
	}

	public Float getAlturaAssinatura() {
		return alturaAssinatura;
	}

	public void setAlturaAssinatura(Float alturaAssinatura) {
		this.alturaAssinatura = alturaAssinatura;
	}

	public Date getDataAssinatura() {
		if(dataAssinatura == null) {
			dataAssinatura =  new Date();
		}
		return dataAssinatura;
	}

	public void setDataAssinatura(Date dataAssinatura) {
		this.dataAssinatura = dataAssinatura;
	}

	public Boolean getIsValidarArquivoExistente() {
		return isValidarArquivoExistente;
	}

	public void setIsValidarArquivoExistente(Boolean isValidarArquivoExistente) {
		this.isValidarArquivoExistente = isValidarArquivoExistente;
	}

	public String getNomeCertificadoCN() {
		if(x509Certificate != null && x509Certificate.getSubjectDN() != null && x509Certificate.getSubjectDN().getName() != null && x509Certificate.getSubjectDN().getName().contains("CN")) {
			for (String name : x509Certificate.getSubjectDN().getName().split(",")) {
				if (name.contains("CN")) {
				return name.substring(name.lastIndexOf("=") + 1, name.length());
				}
			}
		}else if(x509Certificate != null && x509Certificate.getIssuerDN() != null && x509Certificate.getIssuerDN().getName() != null && x509Certificate.getIssuerDN().getName().contains("CN")) {
			for (String name : x509Certificate.getIssuerDN().getName().split(",")) {
				if (name.contains("CN")) {
					return name.substring(name.lastIndexOf("=") + 1, name.length());
				}
			}
		}
		return "";
	}
	
	public float getTamanhoFonte() {
		return tamanhoFonte;
	}

	public void setTamanhoFonte(float tamanhoFonte) {
		this.tamanhoFonte = tamanhoFonte;
	}

	public int getCoordenadaLLX() {
		return coordenadaLLX;
	}

	public void setCoordenadaLLX(int coordenadaLLX) {
		this.coordenadaLLX = coordenadaLLX;
	}

	public int getCoordenadaLLY() {
		return coordenadaLLY;
	}

	public void setCoordenadaLLY(int coordenadaLLY) {
		this.coordenadaLLY = coordenadaLLY;
	}

	public int getCoordenadaURX() {
		return coordenadaURX;
	}

	public void setCoordenadaURX(int coordenadaURX) {
		this.coordenadaURX = coordenadaURX;
	}

	public int getCoordenadaURY() {
		return coordenadaURY;
	}

	public void setCoordenadaURY(int coordenadaURY) {
		this.coordenadaURY = coordenadaURY;
	}

	public boolean isApresentarAssinaturaDigital() {
		return apresentarAssinaturaDigital;
}

	public void setApresentarAssinaturaDigital(boolean apresentarAssinaturaDigital) {
		this.apresentarAssinaturaDigital = apresentarAssinaturaDigital;
	}

	public boolean isApresentarAssinaturaUltimaPagina() {
		return apresentarAssinaturaUltimaPagina;
	}

	public void setApresentarAssinaturaUltimaPagina(boolean apresentarAssinaturaUltimaPagina) {
		this.apresentarAssinaturaUltimaPagina = apresentarAssinaturaUltimaPagina;
	}

	public String getCargo() {
		return cargo;
	}

	public void setCargo(String cargo) {
		this.cargo = cargo;
	}

	public String getTitulo() {		
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getIpAssinatura() {
		return ipAssinatura;
	}

	public void setIpAssinatura(String ipAssinatura) {
		this.ipAssinatura = ipAssinatura;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getDispositivoAssinatura() {
		return dispositivoAssinatura;
	}

	public void setDispositivoAssinatura(String dispositivoAssinatura) {
		this.dispositivoAssinatura = dispositivoAssinatura;
	}
	
	

}
