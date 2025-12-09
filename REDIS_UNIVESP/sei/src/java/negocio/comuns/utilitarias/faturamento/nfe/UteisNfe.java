package negocio.comuns.utilitarias.faturamento.nfe;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.cert.CertificateException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;
import java.util.TimeZone;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.faturamento.nfe.ConfiguracaoNotaFiscalVO;
import negocio.comuns.faturamento.nfe.DadosEnvioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
/**
 *
 * @author rodrigo
 */
public class UteisNfe {
	
	public static void imprimirCertificadosCacerts() {
		FileInputStream is = null;
		try {
			String filename = System.getProperty("java.home") + "/lib/security/cacerts".replace('/', File.separatorChar);
			System.out.println("STATUS-NFE-> cacerts: " + filename);
			is = new FileInputStream(filename);
	        KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
	        String password = "changeit";
	        keystore.load(is, password.toCharArray());
	        Enumeration<String> enumeration = keystore.aliases();
	        while(enumeration.hasMoreElements()) {
	            String alias = (String)enumeration.nextElement();
	            if (!alias.contains("[jdk]")) {
	            	System.out.println("STATUS-NFE-> alias: " + alias);
	            }
//	            Certificate certificate = keystore.getCertificate(alias);
//	            System.out.println("STATUS-NFE-> certificado: " + certificate.toString());
	        }
	    } catch (CertificateException e) {
	        e.printStackTrace();
	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (KeyStoreException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
		} finally {
	        if(null != is)
	            try {
	                is.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	    }
	}
	
	public static void autenticaoNfe(DadosEnvioVO obj) throws Exception { 
		System.setProperty("sun.security.ssl.allowUnsafeRenegotiation", "true"); 
    	System.setProperty("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");
		System.clearProperty("javax.net.ssl.keyStoreType");
		System.clearProperty("javax.net.ssl.keyStore");
		System.clearProperty("javax.net.ssl.keyStorePassword");
    	Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
		System.setProperty("javax.net.ssl.keyStoreType", "PKCS12");
		System.setProperty("javax.net.ssl.keyStore", obj.getCaminhoCertificado());
		System.setProperty("javax.net.ssl.keyStorePassword", obj.getSenhaCertificado());
    }
	
	public static void autenticaoNfeTeste(String caminho, String senha) throws Exception { 
		System.setProperty("sun.security.ssl.allowUnsafeRenegotiation", "true"); 
    	System.setProperty("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");
		System.clearProperty("javax.net.ssl.keyStoreType");
		System.clearProperty("javax.net.ssl.keyStore");
		System.clearProperty("javax.net.ssl.keyStorePassword");
    	Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
		System.setProperty("javax.net.ssl.keyStoreType", "PKCS12");
		System.setProperty("javax.net.ssl.keyStore", caminho);
		System.setProperty("javax.net.ssl.keyStorePassword", senha);
    }
	
    public static String getCaminhoCertificado(ConfiguracaoNotaFiscalVO conNotaFiscalVO, ConfiguracaoGeralSistemaVO conSistemaVO) {
    	String nomeCertificado = conNotaFiscalVO.getArquivoVO().getNome();
    	String caminhoCertificado = conSistemaVO.getLocalUploadArquivoFixo() + File.separator + conNotaFiscalVO.getArquivoVO().getPastaBaseArquivo() + File.separator + nomeCertificado;
    	return caminhoCertificado;
    }
    
    public static byte[] getCertificado(ConfiguracaoNotaFiscalVO conNotaFiscalVO, ConfiguracaoGeralSistemaVO conSistemaVO) throws Exception {
    	String nomeCertificado = conNotaFiscalVO.getArquivoVO().getNome();
    	String caminhoCertificado = conSistemaVO.getLocalUploadArquivoFixo() + File.separator + conNotaFiscalVO.getArquivoVO().getPastaBaseArquivo() + File.separator + nomeCertificado;
        File file = new File(caminhoCertificado);
        byte[] certificado = new byte[(int) file.length()];
        FileInputStream fileInputStream = new FileInputStream(file);
	    fileInputStream.read(certificado);
	    fileInputStream.close();
	    return certificado;
    }

    public static void validarCaminhoCertificado(ConfiguracaoNotaFiscalVO configuracaoNotaFiscalVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
    	System.out.println("STATUS-NFE-> NomeCertificado: " + configuracaoNotaFiscalVO.getArquivoVO().getNome());
    	System.out.println("STATUS-NFE-> CaminhoBaseCertificado: " + configuracaoGeralSistemaVO.getLocalUploadArquivoFixo());
    	Assinador.carregarCertificado(getCertificado(configuracaoNotaFiscalVO, configuracaoGeralSistemaVO), configuracaoNotaFiscalVO.getSenhaCertificado());
    	String caminhoCertificado = configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + configuracaoNotaFiscalVO.getArquivoVO().getPastaBaseArquivo() + File.separator  + configuracaoNotaFiscalVO.getArquivoVO().getNome();
    	System.out.println("STATUS-NFE-> CaminhoCertificado: " + caminhoCertificado);
    	UteisNfe.autenticaoNfeTeste(caminhoCertificado, configuracaoNotaFiscalVO.getSenhaCertificado());
    }

    public static String getRecido(String mensagem) {
    	if (!mensagem.contains("<nRec>")) {
    		return "";
    	}
        return mensagem.substring(mensagem.indexOf("<nRec>") + 6, mensagem.indexOf("</nRec>"));
    }

    public static String lerXMLStatRecebimentoLote(String retorno) {
        String codigo = retorno.substring(retorno.lastIndexOf("<cStat>") + 7, retorno.lastIndexOf("</cStat>"));
        return codigo;
    }
    
    public static String obterCodigoRetorno(String xml) {
    	if (!xml.contains("<cStat>")) {
    		return "999";
    	}
        String codigo = xml.substring(xml.lastIndexOf("<cStat>") + 7, xml.lastIndexOf("</cStat>"));
        return codigo;
    }

    public static String obterProtocoloInutilizacao(String xml) {
        String codigo = xml.substring(xml.indexOf("<nProt>") + 7, xml.indexOf("</nProt>"));
        return codigo;
    }
    
    public static String getMotivoXMLretornoLoteNFe(String retorno) throws Exception {
        return CodigoRetorno.MensagemRetorno(obterCodigoRetorno(retorno));

    }

    public static String getProtocoloEnvioNFe(String retorno) throws Exception {
//        String codigo = retorno.substring(retorno.indexOf("<cStat>") + 7, retorno.indexOf("</cStat>"));
//        if (!codigo.equals("100")) {
//            throw new Exception("Nota não consta na base de dados da SEFAZ.");
//        }
        String msg = retorno.substring(retorno.lastIndexOf("<nProt>") + 7, retorno.lastIndexOf("</nProt>"));
        return msg;
    }

    public static Date getDataProtocoloEnvioNFe(String retorno) throws Exception {
        String data = retorno.substring(retorno.lastIndexOf("<dhRecbto>") + 10, retorno.lastIndexOf("</dhRecbto>"));
        return getDate(data);
    }
    
    public static Date getDataProtocoloEventoNFe(String retorno) throws Exception {
        String data = retorno.substring(retorno.lastIndexOf("<dhRegEvento>") + 13, retorno.lastIndexOf("</dhRegEvento>"));
        return getDate(data);
    }

    public static java.util.Date getDate(String dataEmissao) throws Exception {

        String ano = dataEmissao.substring(0, 4);
        String mes = dataEmissao.substring(5, 7);
        String dia = dataEmissao.substring(8, 10);
        String horaEmissao = dataEmissao.substring(11, 19);
        java.util.Date valorData = null;
        DateFormat formatador = DateFormat.getDateInstance(DateFormat.SHORT);
        valorData = formatador.parse(dia + "/" + mes + "/" + ano);
        Calendar cal = Calendar.getInstance();
        int hora = Integer.parseInt(horaEmissao.substring(0, 2));
        int minuto = Integer.parseInt(horaEmissao.substring(3, 5));
        int segundo = Integer.parseInt(horaEmissao.substring(6, 8));
        cal.setTime(valorData);
        cal.set(Calendar.HOUR_OF_DAY, hora);
        cal.set(Calendar.MINUTE, minuto);
        cal.set(Calendar.SECOND, segundo);

        return cal.getTime();
    }

    public static String getDigValProtocoloNFe(String retorno) throws Exception {
        String codigo = retorno.substring(retorno.indexOf("<digVal>") + 8, retorno.indexOf("</digVal>"));
        if (codigo.equals("")) {
            throw new Exception("Não foi possível obter o DigestValeu para o cancelamento da NFe.");
        }
        return codigo;
    }

    public static String converterXMLparaString(Document doc) throws Exception {
        String corpo = "";

        doc.setXmlStandalone(true);
        doc.setXmlVersion("1.0");

        DOMSource source = new DOMSource(doc);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer trans = tf.newTransformer();
        trans.transform(source, new StreamResult(os));

        corpo = os.toString();
        if (corpo.contains("standalone=\"no\"")) {
            corpo = corpo.replaceAll("standalone=\"no\"", "");
        }
        return corpo;
    }

    public static String obterXMLNFeSemAssinaturaPorCaminho(String caminho) throws Exception {
        File xml = new File(caminho);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document doc = factory.newDocumentBuilder().parse(new FileInputStream(xml));
        String strXml = converterXMLparaString(doc);
        Integer posicaoInicioAssinatura = strXml.indexOf("<Signature xmlns=\"http://www.w3.org/2000/09/xmldsig#\">");
        Integer posicaoFinalAssinatura = strXml.indexOf("</Signature>");
        String assinatura = strXml.substring(posicaoInicioAssinatura, posicaoFinalAssinatura);
        return strXml.replace(assinatura, "");

    }
    
    public static String obterXMLNFeOriginalPorCaminho(String caminho) throws Exception {
        File xml = new File(caminho);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document doc = factory.newDocumentBuilder().parse(new FileInputStream(xml));
        return converterXMLparaString(doc);
    }

    public static String removerQuebraLinhaFinalTag(String xml) {
        if (xml.contains(">\r")) {
            xml = xml.replace(">\r", ">");
        }
        if (xml.contains(">\n")) {
            xml = xml.replace(">\n", ">");
        }

        return xml;
    }

    public static String getSignature(String caminhoAplicacao, String lote) throws Exception {
        String nrCodigoBarra = "NFe" + Uteis.getMontarCodigoBarra(lote, 9);
        String nomeArquivoNFeAssinada = caminhoAplicacao + File.separator + nrCodigoBarra + "_sign.xml";
        DocumentBuilderFactory nfeSalva = DocumentBuilderFactory.newInstance();
        nfeSalva.setNamespaceAware(true);
        Document doc = nfeSalva.newDocumentBuilder().parse(new FileInputStream(nomeArquivoNFeAssinada));
        String xml = UteisNfe.converterXMLparaString(doc);
        return xml.substring(xml.indexOf("<Signature xmlns=\"http://www.w3.org/2000/09/xmldsig#\">"), xml.indexOf("</Signature>"));
    }

    public static String removerTagXMLEnconding(String xml) {
        if (xml.contains("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")) {
            xml = xml.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "");
        }
        return xml;
    }

    public static String getVerificaRejeicaoCancelamento(String retorno) throws Exception {
        String cStat = retorno.substring(retorno.indexOf("<cStat>") + 7, retorno.indexOf("</cStat>"));
        int stat = Integer.parseInt(cStat);
        if (stat != 101) {
            throw new Exception(retorno.substring(retorno.indexOf("<xMotivo>") + 9, retorno.indexOf("</xMotivo>")));
        }
        return retorno.substring(retorno.indexOf("<xMotivo>") + 9, retorno.indexOf("</xMotivo>"));
    }

    public static String formatarStringDouble(String valor, int nrCasa) {
        String inteira = valor.substring(0, valor.indexOf("."));
        String extensao = valor.substring(valor.indexOf(".") + 1, valor.length());

        int extensaoAdd = nrCasa - extensao.length();
        while (extensaoAdd > 0) {
            extensao += "0";
            extensaoAdd--;
        }
        if (extensaoAdd < 0) {
            extensaoAdd = extensaoAdd * (-1);
            extensao = extensao.substring(0, extensao.length() - extensaoAdd);
        }
        valor = inteira + "." + extensao;
        return valor;
    }

    public static String retirarCaracteresEspeciais(String texto) {
        String nova = "";
        for (int i = 0; i < texto.length(); i++) {
        	if ((texto.equals("NF-E EMITIDA EM AMBIENTE DE HOMOLOGACAO - SEM VALOR FISCAL")) || 
        			(texto.charAt(i) != '.' && texto.charAt(i) != '-' && texto.charAt(i) != ',' && texto.charAt(i) != '&' && texto.charAt(i) != 'º' && texto.charAt(i) != 'ª')) {
	            nova += texto.charAt(i);
        	}
        }
        texto = nova;
        nova = "";
        int sp = 0;
        for (int i = 0; i < texto.length(); i++) {
            if (texto.charAt(i) == ' ') {
                sp++;
            } else {
                sp = 0;
            }
            if (sp <= 1) {
                nova += texto.charAt(i);
            }
        }

        nova = nova.replaceAll("\n", "");

        return nova.trim();
    }
    
    public static String removeCaractersEspeciais(String string) {
        if (!string.contains("''")) {
            if (string.contains("'")) {
                string = string.replaceAll("'", "''");
            }
        }
        string = string.replaceAll("[ÂÀÁÄÃ]", "A");
		string = string.replaceAll("[âãàáä]", "a");
		string = string.replaceAll("[ÊÈÉË]", "E");
		string = string.replaceAll("[êèéë]", "e");
		string = string.replaceAll("ÎÍÌÏ", "I");
		string = string.replaceAll("îíìï", "i");
		string = string.replaceAll("[ÔÕÒÓÖ]", "O");
        string = string.replaceAll("[ôõòóö]", "o");
        string = string.replaceAll("[ÛÙÚÜ]", "U");
        string = string.replaceAll("[ûúùü]", "u");
        string = string.replaceAll("Ç", "C");
        string = string.replaceAll("ç", "c");
        string = string.replaceAll("[ýÿ]", "y");
        string = string.replaceAll("Ý", "Y");
        string = string.replaceAll("ñ", "n");
        string = string.replaceAll("Ñ", "N");
        string = string.replaceAll("\\\\", "");
        string = string.replaceAll("['<>|/]¦", "");
//        texto = texto.replaceAll("[ ]", "");
        string = string.replaceAll("[-#$%¨&*()_+={}?.,:;º°ª^~´`§@!\"]", "");
        return string;
    }
    
    public static String getUrl() {
        String contextPath = request().getContextPath();
        String ip = request().getLocalAddr();
        int port = request().getLocalPort();
        return "http://" + ip + ":" + port + contextPath + "/NFe/wsdl/";
    }

    protected static FacesContext context() {
        return (FacesContext.getCurrentInstance());
    }

    protected static HttpServletRequest request() {
        return ((HttpServletRequest) context().getExternalContext().getRequest());
    }
    
    public static String criarIdNFe(Integer numero, String codigoIBGE, String data, String cnpj, String modelo, String serie, String tipoEmissao) throws Exception {
        if (numero.longValue() > 0l) {
            data = Uteis.removerMascara(data);
            String mes = data.substring(4, 6);
            String ano = data.substring(2, 4);
            data = ano + mes;
            cnpj = Uteis.removerMascara(cnpj);
            String model = modelo;
            if (modelo.equals("1") || modelo.equals("01") || modelo.equalsIgnoreCase("1A")) {
                model = "55";
            }
            if (model.length() < 2) {
                model = Uteis.getPreencherComZeroEsquerda(model, 2);
            }
            if (serie.length() < 3) {
                serie = Uteis.getPreencherComZeroEsquerda(serie, 3);
            }
            String codigoNFe9 = Uteis.getPreencherComZeroEsquerda(numero.toString(), 9);
            String codigoNFe8 = Uteis.getPreencherComZeroEsquerda(numero.toString(), 8);
            String id = codigoIBGE + data + cnpj + model + serie + codigoNFe9 + tipoEmissao + codigoNFe8;
            int x = id.length();
            int multi = 2;
            int valorTotal = 0;
            while (x > 0) {
                if (multi == 10) {
                    multi = 2;
                }
                int valor = 0;
                if (x == 43) {
                    valor = Integer.parseInt(id.substring(x - 1, id.length()));
                } else {
                    valor = Integer.parseInt(id.substring(x - 1, x));
                }
                valorTotal = valorTotal + (valor * multi);

                multi++;
                x--;
            }
            int resto = valorTotal % 11;
            if (resto > 1) {
                resto = 11 - resto;
            } else {
                resto = 0;
            }
            return "NFe" + id + resto;
        } else {
            return "";
        }
    }
    
    public static Date getHorarioCancelamentoCartaoCorrecaoNfe() throws Exception{
    	Calendar c = Calendar.getInstance(TimeZone.getDefault());
        c.setTime(new Date());
        Integer offset = c.get(Calendar.DST_OFFSET);  
        
    	if (!offset.equals(0)) {
			return UteisData.getDataAtualSomandoOuSubtraindoMinutos(-65);
		} else return UteisData.getDataAtualSomandoOuSubtraindoMinutos(-5);
    }
    
    public static String mascaraCNPJ(String valor) {
        String mascara = "XX.XXX.XXX/XXXX-XX";
     
        int i = valor.length();
        if (i == 14) {
            String parte1 = valor.substring(12);
            String parte2 = valor.substring(8, 12);
            String parte3 = valor.substring(5, 8);
            String parte4 = valor.substring(2, 5);
            String parte5 = valor.substring(0, 2);
            return parte5 + "." + parte4 + "." + parte3 + "/" + parte2 + "-" + parte1;
        } else if (i == 11) {
            String parte1 = valor.substring(9);
            String parte2 = valor.substring(6, 9);
            String parte3 = valor.substring(3, 6);
            String parte4 = valor.substring(0, 3);
            return parte4 + "." + parte3 + "." + parte2 + "-" + parte1;
        } else if (i == 18) {
            return valor;
        } else {
            return mascara;
        }
    }
    
    public static String removerMascara(String campo) {
        String campoSemMascara = "";
        for (int i = 0; i < campo.length(); i++) {
            if ((campo.charAt(i) != ',')
                    && (campo.charAt(i) != '.')
                    && (campo.charAt(i) != '-')
                    && (campo.charAt(i) != ':')
                    && (campo.charAt(i) != '<')
                    && (campo.charAt(i) != '>')
                    && (campo.charAt(i) != '(')
                    && (campo.charAt(i) != ')')
                    && (campo.charAt(i) != '/')) {
                campoSemMascara = campoSemMascara + campo.substring(i, i + 1);
            }
        }
        return campoSemMascara;
    }
    
    public static String retirarSinaisSimbolosEspacoString(String label) {
        label = removerMascara(label);
        label = Uteis.retirarAcentuacao(label);
        label = Uteis.removerEspacosFinalString(label);
        label = label.replaceAll(" ", "");
        label = label.replaceAll("-", "");
        label = label.replaceAll(",", "");
        label = label.replaceAll("_", "");
        label = label.replaceAll("/", "");
        return label;
    }
    
    public static String getAnoDataAtual() {
        Locale aLocale = new Locale("pt", "BR");
        Date hoje;
        String hojeStr;
        DateFormat formatador;
        formatador = DateFormat.getDateInstance(DateFormat.SHORT, aLocale);
        hoje = new Date();
        hojeStr = formatador.format(hoje);
        return (hojeStr.substring(hojeStr.lastIndexOf("/") + 1));
    }
    
    public static String criarIdNFeInutilizacao(String codigoIBGE, String cnpj, String modelo, String serie, String nrInicio, String nrFim) throws Exception {
        nrInicio = Uteis.getPreencherComZeroEsquerda(nrInicio.toString(), 9);
        nrFim = Uteis.getPreencherComZeroEsquerda(nrFim.toString(), 9);
        serie = Uteis.getPreencherComZeroEsquerda(serie.toString(), 3);
        return codigoIBGE + getAnoDataAtual() + Uteis.removerMascara(cnpj) + modelo + serie + nrInicio + nrFim;
    }
    
    public static String removerEspacoDuplo(String xml) {
        if (xml.contains("  ")) {
            xml = xml.replace("  ", " ");
        }
        return xml;
    }
    
    public static void autenticaoNfse(String caminhoCertificado, String senhaCertificado, String caminhoJks, String senhaJks) throws Exception {    	
		System.setProperty("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");
		Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
		System.setProperty("sun.security.ssl.allowUnsafeRenegotiation", "true");

		System.getProperties().remove("javax.net.ssl.keyStoreType");
		System.getProperties().remove("javax.net.ssl.keyStore");
		System.getProperties().remove("javax.net.ssl.keyStorePassword");

		System.getProperties().setProperty("javax.net.ssl.keyStoreType", "PKCS12");
		System.getProperties().setProperty("javax.net.ssl.keyStore", caminhoCertificado);
		System.getProperties().setProperty("javax.net.ssl.keyStorePassword", senhaCertificado);
    }
}
