package negocio.comuns.utilitarias;


import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.of;

import java.awt.Color;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.text.Collator;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.UUID;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import java.nio.charset.StandardCharsets;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.security.PublicKey;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import java.nio.charset.StandardCharsets;


import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;



import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.json.JSONArray;
import org.primefaces.event.FileUploadEvent;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import negocio.comuns.administrativo.ConfiguracaoLdapVO;

import controle.arquitetura.AplicacaoControle;
import controle.arquitetura.AssuntoDebugEnum;
import controle.arquitetura.SuperControle;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.faces.context.FacesContext;
import jakarta.faces.model.SelectItem;
import jakarta.mail.AuthenticationFailedException;
import jakarta.mail.Authenticator;
import jakarta.mail.BodyPart;
import jakarta.mail.Message;
import jakarta.mail.Part;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jcifs.smb1.util.MD4;
import jcifs.util.Hexdump;
import kong.unirest.json.JSONObject;
import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.ConfiguracaoLdapVO;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.enumeradores.Obrigatorio;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoSubModuloEnum;
import negocio.comuns.basico.FeriadoVO;

import negocio.comuns.utilitarias.dominios.DiaSemana;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;

//import relatorio.negocio.comuns.academico.CronogramaDeAulasRelVO;
//import relatorio.negocio.comuns.academico.DiarioFrequenciaAulaVO;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;

public class Uteis implements Serializable {
	
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	private static final int TIMEOUT_CONEXAO_MS = 30000; 
	private static final int HTTP_OK = 200;
	private static final int HTTP_NO_CONTENT = 204;
	public static final String VERSAO_SISTEMA = UteisJSF.getParametrosSistema("prt_versao");
	public static final String AMBIENTE_PRODUCAO = UteisJSF.getParametrosSistema("prt_ambiente");
	public static final String APLICACAO_PRINCIPAL = UteisJSF.getParametrosSistema("prt_aplicacao");
	public static final int BLOQUEIO_VERIFICAR = 0;
	public static final int BLOQUEIO_GERAR = 1;
	private static String loginMail;
	private static String senhaMail;
	public static String BAG_KEY = "key";
	public static final int CONSULTAR = 0;
	public static final int INCLUIR = 1;
	public static final int ALTERAR = 2;
	public static final int EXCLUIR = 3;
	public static final int PROXIMO = 5;
	public static final int ANTERIOR = 6;
	public static final int INICIALIZAR = 7;
	public static final int EXISTENTE = 8;
	public static final int NOVO = 9;
	public static final int ULTIMO = 10;
	public static final int PRIMEIRO = 11;
	public static final int EMITIRRELATORIO = 12;
	public static final int GRAVAR = 13;
	public static final String PERMISSAOTOTAL = "(" + NOVO + ")" + "(" + INCLUIR + ")" + "(" + ALTERAR + ")" + "(" + EXCLUIR + ")" + "(" + CONSULTAR + ")" + "(" + EMITIRRELATORIO + ")";
	public static final String PERMISSAOINCLUIR = "(" + NOVO + ")" + "(" + INCLUIR + ")";
	public static final String PERMISSAOCONSULTAR = "(" + CONSULTAR + ")" + "(" + EMITIRRELATORIO + ")" + "(" + PROXIMO + ")" + "(" + ANTERIOR + ")";
	public static final String PERMISSAOALTERAR = "(" + ALTERAR + ")";
	public static final String PERMISSAOEXCLUIR = "(" + EXCLUIR + ")";
	public static final int TAMANHOLISTA = 10;
	public static final int NIVELMONTARDADOS_TODOS = 0;
	public static final int NIVELMONTARDADOS_DADOSBASICOS = 1;
	public static final int NIVELMONTARDADOS_PROCESSAMENTO = 6;
	public static final int NIVELMONTARDADOS_DADOSMINIMOS = 3;
	public static final int NIVELMONTARDADOS_DADOSCONSULTA = 4;
	public static final int NIVELMONTARDADOS_DADOSCONSULTARTODOS = 5;
	public static final int NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS = 2;
	public static final int NIVELMONTARDADOS_DADOSLOGIN = 8;
	public static final int NIVELMONTARDADOS_COMBOBOX = 7;
	public static final int NIVELMONTARDADOS_AUDITORIA = 7;
	public static final int NIVELMONTARDADOS_JOBATRASODEVOLUCAO = 9;
	public static final int NIVELMONTARDADOS_DADOSBASICOS_APLICATIVO = 10;
	public static final int NIVELMONTARDADOS_DADOS_CAMINHO_ARQUIVO_MINIMO = 11;
	public static final int NIVELMONTARDADOS_DADOSBASICOS_CANDIDATO = 13;
	public static final int NIVELMONTARDADOS_DADOSDOCUMENTOASSINADO = 14;
	public static final char A_AGUDO = UteisJSF.internacionalizar("prt_A_AGUDO").charAt(0);
	public static final char A_AGUDOMAIUSCULO = UteisJSF.internacionalizar("prt_A_AGUDOMAIUSCULO").charAt(0);
	public static final char A_CRASE = UteisJSF.internacionalizar("prt_A_CRASE").charAt(0);
	public static final char A_CIRCUNFLEXO = UteisJSF.internacionalizar("prt_A_CIRCUNFLEXO").charAt(0);
	public static final char A_TIO = UteisJSF.internacionalizar("prt_A_TIO").charAt(0);
	public static final char E_AGUDO = UteisJSF.internacionalizar("prt_E_AGUDO").charAt(0);
	public static final char E_CIRCUNFLEXO = UteisJSF.internacionalizar("prt_E_CIRCUNFLEXO").charAt(0);
	public static final char I_AGUDO = UteisJSF.internacionalizar("prt_I_AGUDO").charAt(0);
	public static final char O_AGUDO = UteisJSF.internacionalizar("prt_O_AGUDO").charAt(0);
	public static final char O_TIO = UteisJSF.internacionalizar("prt_O_TIO").charAt(0);
	public static final char O_CRASE = UteisJSF.internacionalizar("prt_O_CRASE").charAt(0);
	public static final char U_AGUDO = UteisJSF.internacionalizar("prt_U_AGUDO").charAt(0);
	public static final char U_TREMA = UteisJSF.internacionalizar("prt_U_TREMA").charAt(0);
	public static final char C_CEDILHA = UteisJSF.internacionalizar("prt_C_CEDILHA").charAt(0);
	public static final boolean realizarUpperCaseDadosAntesPersistencia = false;
	private List<File> listaImagem = new ArrayList<File>();
	private Map<String, File> mapaAnexo = new HashMap<String, File>();
	/** Index of the first accent character **/
	private static final int MIN = 192;
	/** Index of the last accent character **/
	private static final int MAX = 255;
	/** used to save the link between with or without accents **/
	@SuppressWarnings("UseOfObsoleteCollectionType")
	private static final Vector map = initMap();
	/** Usado na nova arquitetura **/
	public static final String ERRO = "ERRO";
	public static final String SUCESSO = "SUCESSO";
	public static final String ALERTA = "ALERTA";
	public static final HashMap<String, String> ARQUIVOS_CONTROLE_COBRANCA = new HashMap<String, String>(0);
	private static final long ONE_DAY = 24 * 60 * 60 * 1000;
	public static final String QTDE = "QTDE";
	public static final String ALGORITHM_RSA = "RSA";
	private static final String[] ORDINAIS = { "", "primeiro", "segundo", "terceiro", "quarto", "quinto", "sexto", "stimo", "oitavo", "nono" };
	private static final String[] ORDINAIS_MULTIPLOS_DEZ = { "", "dcimo", "vigsimo", "trigsimo", "quadragsimo", "quinquagsimo", "sexagsimo", "septuagsimo", "octogsimo", "nonagsimo" };
	public static long delayMillisTechCert = 2_000;
	public static long delayMillisIntegracaoMestreGR = 1_000;
	
	
	CellStyle headerStyle;
	CellStyle textoCellStyle;
	CellStyle moedaCellStyle;
	CellStyle dataCellStyle;
	CellStyle intCellStyle;
	DataFormat dataFormat;
	DataFormat doubleFormat;
	DataFormat textoFormat;
	DataFormat intFormat;
	/** Creates a new instance of CfgPadrao */
	public Uteis() {
	}

	public static boolean isVersaoDev() {
		return "DEV".equals(VERSAO_SISTEMA);
	}
	public static boolean isAmbienteProducao() {
		return !"HOMOLOGACAO".equals(AMBIENTE_PRODUCAO);
	}

	public static boolean isAplicacaoPrincipal() {
		return !"SLAVE".equals(APLICACAO_PRINCIPAL);
	}

	public static boolean isSistemaOperacionalWindows() {
		return System.getProperty("os.name").toLowerCase().contains("windows") ? true : false;
	}

	public static Color gerarCorAleatoria(Color mix) {
		Color color = null;
		Random random = new Random();
		int red = random.nextInt(256);
		int green = random.nextInt(256);
		int blue = random.nextInt(256);
		try {

			int red2 = random.nextInt(256);
			int green2 = random.nextInt(256);
			int blue2 = random.nextInt(256);
			// mix the color
			if (mix != null) {
				red = (red + green2) / 4 + (mix.getBlue() / 2) + random.nextInt(256);
				green = (green + blue2) / 4 + (mix.getRed() / 2) + random.nextInt(256);
				blue = (blue + red2) / 4 + (mix.getGreen() / 2) + random.nextInt(256);
			}
			if (red >= 256 || red < 1) {
				red = random.nextInt(256);
			}
			if (blue >= 256 || blue < 1) {
				blue = random.nextInt(256);
			}
			if (green >= 256 || green < 1) {
				green = random.nextInt(256);
			}
			color = new Color(red, green, blue);
		} catch (Exception e) {
		} finally {
			return color;
		}

	}
	
	
	
	@SuppressWarnings({ "UseOfObsoleteCollectionType", "RedundantStringConstructorCall" })
	private static Vector initMap() {
		Vector result = new Vector();
		String car = null;

		car = new String("A"); //$NON-NLS-1$
		result.add(car); /* '\u00C0' alt-0192 */
		result.add(car); /* '\u00C1' alt-0193 */
		result.add(car); /* '\u00C2' alt-0194 */
		result.add(car); /* '\u00C3' alt-0195 */
		result.add(car); /* '\u00C4' alt-0196 */
		result.add(car); /* '\u00C5' alt-0197 */
		car = new String("AE"); //$NON-NLS-1$
		result.add(car); /* '\u00C6' alt-0198 */
		car = new String("C"); //$NON-NLS-1$
		result.add(car); /* '\u00C7' alt-0199 */
		car = new String("E"); //$NON-NLS-1$
		result.add(car); /* '\u00C8' alt-0200 */
		result.add(car); /* '\u00C9' alt-0201 */
		result.add(car); /* '\u00CA' alt-0202 */
		result.add(car); /* '\u00CB' alt-0203 */
		car = new String("I"); //$NON-NLS-1$
		result.add(car); /* '\u00CC' alt-0204 */
		result.add(car); /* '\u00CD' alt-0205 */
		result.add(car); /* '\u00CE' alt-0206 */
		result.add(car); /* '\u00CF' alt-0207 */
		car = new String("D"); //$NON-NLS-1$
		result.add(car); /* '\u00D0' alt-0208 */
		car = new String("N"); //$NON-NLS-1$
		result.add(car); /* '\u00D1' alt-0209 */
		car = new String("O"); //$NON-NLS-1$
		result.add(car); /* '\u00D2' alt-0210 */
		result.add(car); /* '\u00D3' alt-0211 */
		result.add(car); /* '\u00D4' alt-0212 */
		result.add(car); /* '\u00D5' alt-0213 */
		result.add(car); /* '\u00D6' alt-0214 */
		car = new String("*"); //$NON-NLS-1$
		result.add(car); /* '\u00D7' alt-0215 */
		car = new String("0"); //$NON-NLS-1$
		result.add(car); /* '\u00D8' alt-0216 */
		car = new String("U"); //$NON-NLS-1$
		result.add(car); /* '\u00D9' alt-0217 */
		result.add(car); /* '\u00DA' alt-0218 */
		result.add(car); /* '\u00DB' alt-0219 */
		result.add(car); /* '\u00DC' alt-0220 */
		car = new String("Y"); //$NON-NLS-1$
		result.add(car); /* '\u00DD' alt-0221 */
		car = new String("_"); //$NON-NLS-1$
		result.add(car); /* '\u00DE' alt-0222 */
		car = new String("B"); //$NON-NLS-1$
		result.add(car); /* '\u00DF' alt-0223 */
		car = new String("a"); //$NON-NLS-1$
		result.add(car); /* '\u00E0' alt-0224 */
		result.add(car); /* '\u00E1' alt-0225 */
		result.add(car); /* '\u00E2' alt-0226 */
		result.add(car); /* '\u00E3' alt-0227 */
		result.add(car); /* '\u00E4' alt-0228 */
		result.add(car); /* '\u00E5' alt-0229 */
		car = new String("ae"); //$NON-NLS-1$
		result.add(car); /* '\u00E6' alt-0230 */
		car = new String("c"); //$NON-NLS-1$
		result.add(car); /* '\u00E7' alt-0231 */
		car = new String("e"); //$NON-NLS-1$
		result.add(car); /* '\u00E8' alt-0232 */
		result.add(car); /* '\u00E9' alt-0233 */
		result.add(car); /* '\u00EA' alt-0234 */
		result.add(car); /* '\u00EB' alt-0235 */
		car = new String("i"); //$NON-NLS-1$
		result.add(car); /* '\u00EC' alt-0236 */
		result.add(car); /* '\u00ED' alt-0237 */
		result.add(car); /* '\u00EE' alt-0238 */
		result.add(car); /* '\u00EF' alt-0239 */
		car = new String("d"); //$NON-NLS-1$
		result.add(car); /* '\u00F0' alt-0240 */
		car = new String("n"); //$NON-NLS-1$
		result.add(car); /* '\u00F1' alt-0241 */
		car = new String("o"); //$NON-NLS-1$
		result.add(car); /* '\u00F2' alt-0242 */
		result.add(car); /* '\u00F3' alt-0243 */
		result.add(car); /* '\u00F4' alt-0244 */
		result.add(car); /* '\u00F5' alt-0245 */
		result.add(car); /* '\u00F6' alt-0246 */
		car = new String("/"); //$NON-NLS-1$
		result.add(car); /* '\u00F7' alt-0247 */
		car = new String("0"); //$NON-NLS-1$
		result.add(car); /* '\u00F8' alt-0248 */
		car = new String("u"); //$NON-NLS-1$
		result.add(car); /* '\u00F9' alt-0249 */
		result.add(car); /* '\u00FA' alt-0250 */
		result.add(car); /* '\u00FB' alt-0251 */
		result.add(car); /* '\u00FC' alt-0252 */
		car = new String("y"); //$NON-NLS-1$
		result.add(car); /* '\u00FD' alt-0253 */
		car = new String("_"); //$NON-NLS-1$
		result.add(car); /* '\u00FE' alt-0254 */
		car = new String("y"); //$NON-NLS-1$
		result.add(car); /* '\u00FF' alt-0255 */
		result.add(car); /* '\u00FF' alt-0255 */

		return result;
	}

	@SuppressWarnings("CallToThreadDumpStack")
	public static String gerarUpperCasePrimeiraLetraDasPalavras(String s) {
		try {
			if (s == null) {
				return "";
			}
			for (int i = 0; i < s.length(); i++) {

				if (i == 0) {
					s = String.format("%s%s", Character.toUpperCase(s.charAt(0)), s.substring(1));
				}

				if (!Character.isLetterOrDigit(s.charAt(i)) && (s.charAt(i)) != '\'') {
					try {
						if (s.substring(i + 1, i + 3).lastIndexOf(" ") == 1) {
							continue;
						}
					} catch (Exception e) {
					}
					if (i + 1 < s.length()) {
						s = String.format("%s%s%s", s.subSequence(0, i + 1), Character.toUpperCase(s.charAt(i + 1)), s.substring(i + 2));
					}
				}
			}
			if (s.contains("(A)")) {
				return s.replace("(A)", "(a)");
			}
			if (s.contains("(O)")) {
				return s.replace("(O)", "(o)");
			}
			if (s.contains("Dos ")) {
				return s.replace("Dos ", "dos ");
			}
			if (s.contains("Das ")) {
				return s.replace("Das ", "das ");
			}
			if (s.contains("De ")) {
				return s.replace("De ", "de ");
			}
			if (s.contains("Da ")) {
				return s.replace("Da ", "da ");
			}
			if (s.contains("Do ")) {
				return s.replace("Do ", "do ");
			}
			if (s.contains("s ")) {
				return s.replace("s ", "S ");
			}
			return s;
		} catch (Exception e) {
			return "";
		}
	}

	public static String alterarpreposicaoParaMinusculo(String valor) {
		try {
			valor = valor.replaceAll(" A ", " a ");
			valor = valor.replaceAll(" E ", " e ");
			valor = valor.replaceAll(" I ", " i ");
			valor = valor.replaceAll(" O ", " o ");
			valor = valor.replaceAll(" U ", " U ");
			valor = valor.replaceAll(" As ", " as ");
			valor = valor.replaceAll(" Os ", " os ");
			valor = valor.replaceAll(" Um ", " um ");
			valor = valor.replaceAll(" Uma ", " uma ");
			valor = valor.replaceAll(" Uns ", " uns ");
			valor = valor.replaceAll(" Umas ", " umas ");
			valor = valor.replaceAll(" Ao ", " ao ");
			valor = valor.replaceAll("  ", "  ");
			valor = valor.replaceAll(" Aos ", " aos ");
			valor = valor.replaceAll(" s ", " s ");
			valor = valor.replaceAll(" De ", " de ");
			valor = valor.replaceAll(" Da ", " da ");
			valor = valor.replaceAll(" Do ", " do ");
			valor = valor.replaceAll(" Das ", " das ");
			valor = valor.replaceAll(" Dos ", " dos ");
			valor = valor.replaceAll(" Dum ", " dum ");
			valor = valor.replaceAll(" Duma ", " duma ");
			valor = valor.replaceAll(" Duns ", " duns ");
			valor = valor.replaceAll(" Dumas ", " dumas ");
			valor = valor.replaceAll(" Em ", " em ");
			valor = valor.replaceAll(" No ", " no ");
			valor = valor.replaceAll(" Na ", " na ");
			valor = valor.replaceAll(" Nas ", " nas ");
			valor = valor.replaceAll(" Nos ", " nos ");
			valor = valor.replaceAll(" Num ", " num ");
			valor = valor.replaceAll(" Numa ", " numa ");
			valor = valor.replaceAll(" Nuns ", " nuns ");
			valor = valor.replaceAll(" Numas ", " numas ");
			valor = valor.replaceAll(" Por ", " por ");
			valor = valor.replaceAll(" Pelo ", " pelo ");
			valor = valor.replaceAll(" Pela ", " pela ");
			valor = valor.replaceAll(" Pelos ", " pelos ");
			valor = valor.replaceAll(" Pelas ", " pelas ");
			valor = valor.replaceAll(" D'Arc ", " D'arc ");
			valor = valor.replaceAll(" Sant'Ana ", " Sant'ana ");
			return valor;
		} catch (Exception e) {
			return "";
		}
	}

	public static Date gerarDataDiaMesAno(Integer dia, Integer mes, Integer ano) throws Exception {
		String ini = dia + "/" + Uteis.getMesReferencia(mes, ano);
		return Uteis.getDateSemHora(Uteis.getDate(ini));
	}

	public static Date gerarDataInicioMes(Integer mes, Integer ano) throws Exception {
		String ini = "01/" + Uteis.getMesReferencia(mes, ano);
		return Uteis.getDateSemHora(Uteis.getDate(ini));

	}

	public static Date gerarDataFimMes(Integer mes, Integer ano) throws Exception {
		String fim = Uteis.getMesReferencia(mes, ano);
		Integer qtdDiasMes = Uteis.obterNrDiasNoMes(Uteis.getDate("01/" + fim));
		fim = qtdDiasMes + "/" + fim;
		return Uteis.getDateHoraFinalDia(Uteis.getDate(fim));
	}

	public static int obterNrDiasNoMes(Date dataPrm) {
		Calendar dataCalendario = Calendar.getInstance();
		dataCalendario.setTime(dataPrm);
		int numeroDias = dataCalendario.getActualMaximum(Calendar.DAY_OF_MONTH);
		return numeroDias;
	}

	public static Integer obterQuantidadeMesesEntreDatas(Date dataInicio, Date dataFim) throws Exception {
		Integer qtde = 0;
		while (dataInicio.before(dataFim) || dataInicio.equals(dataFim)) {
			dataInicio = Uteis.getDataFutura(dataInicio, GregorianCalendar.MONTH, 1);
			qtde++;
		}
		return qtde;
	}

	/**
	 * Adiciona um anexo ao email.
	 *
	 * @param nomeAnexo
	 * @param anexo
	 * @return
	 */
	public Uteis addAnexo(String nomeAnexo, File anexo) {
		this.mapaAnexo.put(nomeAnexo, anexo);
		return this;
	}

	/**
	 * Adiciona todos os arquivos contidos no diretorio informado na lista de imagens.
	 *
	 * @param diretorioImagens
	 * @return
	 */
	@SuppressWarnings("ManualArrayToCollectionCopy")
	public Uteis addImagensEmDiretorio(File diretorioImagens) {
		for (File file : diretorioImagens.listFiles()) {
			listaImagem.add(file);
		}
		return this;
	}

	/**
	 * Adiciona um arquivo de imagem ao email.
	 *
	 * @param imagem
	 * @return
	 */
	public Uteis addImagem(File imagem) {
		this.listaImagem.add(imagem);
		return this;
	}

	public static String removerAcentos(String text) {
		StringBuilder result = new StringBuilder(text);

		for (int bcl = 0; bcl < result.length(); bcl++) {
			int carVal = text.charAt(bcl);
			if (carVal >= MIN && carVal <= MAX) { // Remplacement
				String newVal = (String) map.get(carVal - MIN);
				result.replace(bcl, bcl + 1, newVal);
			}
		}
		return result.toString();
	}

//	public static String substituirTagCasoExista(String text, Double valorUtilizadoParaRealizarCalculoJuroOuMulta, Double multa, Double juro) {
//		while (text.contains(")%]")) {
//			int posIni = text.indexOf("[(");
//			int posFim = text.indexOf(")%]");
//			String valorStr = text.substring(posIni + 2, posFim);
//			String tagStr = text.substring(posIni, posFim + 3);
//			valorStr = valorStr.replace(",", ".");
//			Double vlr = Double.parseDouble(valorStr);
//			vlr = valorUtilizadoParaRealizarCalculoJuroOuMulta * vlr;
//			vlr = Uteis.trunc(new BigDecimal(vlr), 2).doubleValue();
//			text = text.replace(tagStr, formatarDoubleParaMoeda(vlr));
//		}
//		if (text.contains(TagModeloBoletoEnum.MULTA_VALOR.getDescricao()) && Uteis.isAtributoPreenchido(multa)) {
//			text = text.replaceAll(TagModeloBoletoEnum.MULTA_VALOR.getDescricao(), Uteis.formatarDoubleParaMoeda(multa).replace("R$", "R&#36;"));
//		}
//		if (text.contains(TagModeloBoletoEnum.JURO_VALOR.getDescricao()) && Uteis.isAtributoPreenchido(juro)) {
//			text = text.replaceAll(TagModeloBoletoEnum.JURO_VALOR.getDescricao(), Uteis.formatarDoubleParaMoeda(juro).replace("R$", "R&#36;"));
//		}
//		return text;
//	}

	public static String trocarHashTag(String hashTag, String palavra, String texto) {
		String novoTexto = texto.replace(hashTag, palavra);
		return novoTexto;
	}

	public static String trocarLetraAcentuadaPorCodigoHtml(String texto) {
		texto = texto.replaceAll("", "&Aacute;");
		texto = texto.replaceAll("", "&aacute;");
		texto = texto.replaceAll("", "&Acirc;");
		texto = texto.replaceAll("", "&acirc;");
		texto = texto.replaceAll("", "&Agrave;");
		texto = texto.replaceAll("", "&agrave;");
		texto = texto.replaceAll("", "&Aring;");
		texto = texto.replaceAll("", "&aring;");
		texto = texto.replaceAll("", "&Atilde;");
		texto = texto.replaceAll("", "&atilde;");
		texto = texto.replaceAll("", "&Auml;");
		texto = texto.replaceAll("", "&auml;");
		texto = texto.replaceAll("", "&Eacute;");
		texto = texto.replaceAll("", "&eacute;");
		texto = texto.replaceAll("", "&Ecirc;");
		texto = texto.replaceAll(" ", "&ecirc;");
		texto = texto.replaceAll("", "&Egrave;");
		texto = texto.replaceAll("", "&egrave;");
		texto = texto.replaceAll("", "&Euml;");
		texto = texto.replaceAll("", "&euml;");
		texto = texto.replaceAll("", "&Iacute;");
		texto = texto.replaceAll("", "&iacute;");
		texto = texto.replaceAll("", "&Icirc;");
		texto = texto.replaceAll("", "&icirc;");
		texto = texto.replaceAll("", "&Igrave;");
		texto = texto.replaceAll("", "&igrave;");
		texto = texto.replaceAll("", "&Iuml;");
		texto = texto.replaceAll("", "&iuml;");
		texto = texto.replaceAll("", "&Oacute;");
		texto = texto.replaceAll("", "&oacute;");
		texto = texto.replaceAll("", "&Ocirc;");
		texto = texto.replaceAll("", "&ocirc;");
		texto = texto.replaceAll("", "&Ograve;");
		texto = texto.replaceAll("", "&ograve;");
		texto = texto.replaceAll("", "&Otilde;");
		texto = texto.replaceAll("", "&otilde;");
		texto = texto.replaceAll("", "&Ouml;");
		texto = texto.replaceAll("", "&ouml;");
		texto = texto.replaceAll("", "&Uacute;");
		texto = texto.replaceAll("", "&uacute;");
		texto = texto.replaceAll("", "&Ucirc;");
		texto = texto.replaceAll("", "&ucirc;");
		texto = texto.replaceAll("", "&Ugrave;");
		texto = texto.replaceAll("", "&ugrave;");
		texto = texto.replaceAll("", "&Uuml;");
		texto = texto.replaceAll("", "&uuml;");
		texto = texto.replaceAll("", "&Ccedil;");
		texto = texto.replaceAll("", "&ccedil;");
		texto = texto.replaceAll("", "&Ntilde;");
		texto = texto.replaceAll("", "&ntilde;");
		texto = texto.replaceAll("", "&ordm;");
		texto = texto.replaceAll("", "&ordf;");
		return texto;
	}

//	public static class OrdenaListaBalanceteRelVOPorData implements Comparator<BalanceteRelVO> {
//
//		@SuppressWarnings("CallToThreadDumpStack")
//		public int compare(BalanceteRelVO obj1, BalanceteRelVO obj2) {
//			try {
//				return (obj1.getData()).compareTo(obj2.getData());
//			} catch (ParseException e) {
//			}
//			return 0;
//		}
//	}

//	public static class OrdenaListaContaReceberVOPorDataVencimento implements Comparator<ContaReceberVO> {
//
//		@SuppressWarnings("CallToThreadDumpStack")
//		public int compare(ContaReceberVO obj1, ContaReceberVO obj2) {
//			try {
//				return (obj1.getDataVencimento()).compareTo(obj2.getDataVencimento());
//			} catch (Exception e) {
//			}
//			return 0;
//		}
//	}

//	public static class OrdenaListaMatriculaPeriodoVencimentoVOPorDataVencimento implements Comparator<MatriculaPeriodoVencimentoVO> {
//
//		@SuppressWarnings("CallToThreadDumpStack")
//		public int compare(MatriculaPeriodoVencimentoVO obj1, MatriculaPeriodoVencimentoVO obj2) {
//			try {
//				return (obj1.getDataVencimento()).compareTo(obj2.getDataVencimento());
//			} catch (Exception e) {
//			}
//			return 0;
//		}
//	}

	public static String removeNomeDentroListaDeString(String listaString, String valorRemover) {
		String novaListaString = "";
		String[] listaStringTemp = listaString.split(",");
		for (int i = 0; i < listaStringTemp.length; i++) {
			if (!Uteis.removeCaractersEspeciais(listaStringTemp[i]).equals(Uteis.removeCaractersEspeciais(valorRemover))) {
				if (novaListaString.isEmpty()) {
					novaListaString += listaStringTemp[i];
				} else {
					novaListaString = novaListaString + "," + listaStringTemp[i];
				}
			}
		}
		return novaListaString;
	}

	public static String removeCaractersEspeciais(String string) {
		if (!string.contains("''")) {
			if (string.contains("'")) {
				string = string.replaceAll("'", "''");
			}
		}
		string = string.replaceAll("[]", "A");
		string = string.replaceAll("[]", "a");
		string = string.replaceAll("[]", "E");
		string = string.replaceAll("[]", "e");
		string = string.replaceAll("", "I");
		string = string.replaceAll("", "I");
		string = string.replaceAll("", "i");
		string = string.replaceAll("[]", "O");
		string = string.replaceAll("[]", "o");
		string = string.replaceAll("[]", "U");
		string = string.replaceAll("[]", "u");
		string = string.replaceAll("", "C");
		string = string.replaceAll("", "c");
		string = string.replaceAll("[]", "y");
		string = string.replaceAll("", "Y");
		string = string.replaceAll("", "n");
		string = string.replaceAll("", "N");
		string = string.replaceAll("\\\\", "");
		string = string.replaceAll("['<>|/]", "");
		string = string.replaceAll(" - ", " ");
		string = string.replaceAll("-", " ");
		string = string.replaceAll("- ", "");
		string = string.replaceAll(" -", "");
		// texto = texto.replaceAll("[ ]", "");
		string = string.replaceAll("[-#$%&*()_+={}?.,:;^~`@!\"']", "");
		return string;
	}

	public static String removeCaractersEspeciais2(String string) {
		if (!string.contains("''")) {
			if (string.contains("'")) {
				string = string.replaceAll("'", "''");
			}
		}
		string = string.replaceAll("\\\\", "");
		// string = string.replaceAll("['<>|/]¦", "");
		string = string.replaceAll("['<>|/]", "");
		// texto = texto.replaceAll("[ ]", "");
		// string = string.replaceAll("[#$%¨&*()_+={}?.,:;º°ª^~´`§@!\"]", "");
		string = string.replaceAll("[-#$%&*()_+={}?.,:;^~`/@!\"-]", "");
		return string;
	}

	public static String retirarMascaraCPF(String cpf) {
		return cpf.replace(".", "").replace("-", "");
	}
	
	public static String removeCaractersAspas(String string) {
		string = string.replaceAll("'", "");
		string = string.replaceAll("''", "");
		return string;
	}
	
	public static boolean isDiferentePreposicao(String nome) {
		if((nome.toLowerCase().equals("e") 
			|| nome.toLowerCase().equals("de")
			|| nome.toLowerCase().equals("da")
			|| nome.toLowerCase().equals("das")
			|| nome.toLowerCase().equals("do")
			|| nome.toLowerCase().equals("dos")
			|| nome.toLowerCase().equals("di")
			|| nome.toLowerCase().equals("du"))
		) {
			return false;
		}
		return true;
	}

	public static String retirarMascaraTelefone(String telefone) {
		return telefone.replace("-", "").replace(")", "").replace("(", "").replace(".", "").replace(" ", "");
	}

	public static String adicionarMascaraCPF(String cpf) {
		cpf = cpf.substring(0, 3).concat(".").concat(cpf.substring(3, 6).concat(".").concat(cpf.substring(6, 9).concat("-").concat(cpf.substring(9))));
		return cpf;
	}

	public static String retirarMascaraCNPJ(String cnpj) {
		return cnpj.replace(".", "").replace("-", "").replace("/", "");
	}

	public static String formatarDecimalDuasCasas(Double valor) {
		return formatarDecimal(valor, "0.00#");
	}

	public static String formatarDecimal(Double valor, String pattern) {
		DecimalFormat df = new DecimalFormat(pattern);
		if (valor != null) {
			return df.format(valor);
		}
		return "";
	}

	public static Integer getDataQuantidadeMesesEntreData(Date dataInicio, Date dataFim) {
		Calendar calInicio = Calendar.getInstance();
		Calendar calFim = Calendar.getInstance();
		calInicio.setTime(dataInicio);
		calFim.setTime(dataFim);
		int nrMes = calFim.get(Calendar.MONTH) - calInicio.get(Calendar.MONTH);
		dataFim = null;
		dataInicio = null;
		calFim = null;
		calInicio = null;
		return nrMes;
	}

	public static Date getData(String data, String pattern) throws ParseException {
		try {
			if (data != null) {
				SimpleDateFormat formatador = new SimpleDateFormat(pattern);
				return formatador.parse(data);
			}
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * Metodo que transforma a data que esta no padrao ISO8601 yyyy-MM-dd'T'HH:mm:ss.SSSZ
	 * @param dataPadraoISO8601
	 * @return
	 */
	public static Date getDataISO8601Format(String dataPadraoISO8601) {
		return java.util.Date.from(OffsetDateTime.parse(dataPadraoISO8601).toInstant());
	}

	public static String getData(Date data, String pattern) {
		if (data != null) {
			SimpleDateFormat formatador = new SimpleDateFormat(pattern, new Locale("pt", "BR"));
			return formatador.format(data);
		}
		return "";
	}

	public static String getData(Date data, String pattern, Date ultimaDataValida) {
		if (ultimaDataValida != null && ultimaDataValida.before(data)) {
			return getData(ultimaDataValida, pattern);
		} else {
			return getData(data, pattern);
		}
	}

	public static String getData(Date data, String pattern, Date ultimaDataValida, Date primeiraDataValida) {
		if (ultimaDataValida != null && data != null && ultimaDataValida.before(data)) {
			return getData(ultimaDataValida, pattern);
		} else if (primeiraDataValida != null && data != null && primeiraDataValida.after(data)) {
			return getData(primeiraDataValida, pattern);
		} else {
			return getData(data, pattern);
		}
	}

	public static Date getDataPassada(Date dataInicial, int quantidadeRegredir) throws Exception {
		// GregorianCalendar gc = new GregorianCalendar();
		// gc.setTime(dataInicial);
		// gc.roll(Calendar.MONTH, quantidadeAvancar * -1);
		// return gc.getTime();
		return obterDataPassada(dataInicial, quantidadeRegredir);
	}

	public static Date getDataFutura(Date dataInicial, int field, int quantidadeAvancar) throws Exception {
		if (quantidadeAvancar == 0) {
			return dataInicial;
		}
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(dataInicial);
		gc.add(field, quantidadeAvancar);
		return gc.getTime();
	}

	public static String obterCpfValido(String cpf) {
		if (verificaCPF(cpf)) {
			return cpf;
		} else {
			return "";
		}
	}

	public static boolean verificaCPF(String cpf) {

		try {
			cpf = retirarMascaraCPF(cpf);

			if ((cpf.equals("00000000000")) || (cpf.equals("11111111111")) || (cpf.equals("22222222222")) || (cpf.equals("33333333333")) || (cpf.equals("44444444444")) || (cpf.equals("55555555555")) || (cpf.equals("66666666666")) || (cpf.equals("77777777777")) || (cpf.equals("88888888888")) || (cpf.equals("99999999999"))) {
				return false;
			}

			int count, soma, x, y, CPF[] = new int[11];

			if (cpf.length() != 11) {
				return false;
			}

			soma = 0;
			for (count = 0; count < 11; count++) {
				CPF[count] = 0;
			}

			char vetorChar[] = new char[11];
			String temp, CPFvalido = "";

			for (count = 0; count < 11; count++) {
				// Transformar String em vetor de caracteres
				vetorChar = cpf.toCharArray();
				// Transformar cada caractere em String
				temp = String.valueOf(vetorChar[count]);
				// Transformar String em inteiro e jogar no vetor
				CPF[count] = Integer.parseInt(temp);
			}
			// MÃ?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?Â¢Ã¢â?¬Å¾Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â??Ã?Â Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¾Ã?â??Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã¢â?¬Â¦Ã?â??Ã?Â¡Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â?¦Ã?Â¡Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â©todo
			// da
			// Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?Â¢Ã¢â?¬Å¾Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â??Ã?Â Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¾Ã?â??Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã¢â?¬Â¦Ã?â??Ã?Â¡Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â?¦Ã?Â¡Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â¡rvore
			// para obter o x
			for (count = 0; count < 9; count++) {
				// Pegar soma da
				// permutaÃ?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?Â¢Ã¢â?¬Å¾Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â??Ã?Â Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¾Ã?â??Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã¢â?¬Â¦Ã?â??Ã?Â¡Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â?¦Ã?Â¡Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â§Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?Â¢Ã¢â?¬Å¾Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â??Ã?Â Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¾Ã?â??Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã¢â?¬Â¦Ã?â??Ã?Â¡Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â?¦Ã?Â¡Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â£o
				// dos
				// dÃ?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?Â¢Ã¢â?¬Å¾Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â??Ã?Â Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¾Ã?â??Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã¢â?¬Â¦Ã?â??Ã?Â¡Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â?¦Ã?Â¡Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â­gitos
				// do CPF
				soma += CPF[count] * (10 - count);
			}

			// se o resto da
			// divisÃ?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?Â¢Ã¢â?¬Å¾Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â??Ã?Â Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¾Ã?â??Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã¢â?¬Â¦Ã?â??Ã?Â¡Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â?¦Ã?Â¡Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â£o
			// der 0 ou 1, x
			// terÃ?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?Â¢Ã¢â?¬Å¾Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â??Ã?Â Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¾Ã?â??Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã¢â?¬Â¦Ã?â??Ã?Â¡Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â?¦Ã?Â¡Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â¡
			// dois
			// dÃ?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?Â¢Ã¢â?¬Å¾Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â??Ã?Â Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¾Ã?â??Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã¢â?¬Â¦Ã?â??Ã?Â¡Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â?¦Ã?Â¡Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â­gitos
			if (soma % 11 < 2) {
				x = 0;
			} // x
				// nÃ?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?Â¢Ã¢â?¬Å¾Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â??Ã?Â Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¾Ã?â??Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã¢â?¬Â¦Ã?â??Ã?Â¡Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â?¦Ã?Â¡Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â£o
				// pode ter dois
				// dÃ?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?Â¢Ã¢â?¬Å¾Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â??Ã?Â Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¾Ã?â??Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã¢â?¬Â¦Ã?â??Ã?Â¡Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â?¦Ã?Â¡Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â­gitos
			else {
				x = 11 - (soma % 11);
			} // obtendo o
				// penÃ?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?Â¢Ã¢â?¬Å¾Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â??Ã?Â Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¾Ã?â??Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã¢â?¬Â¦Ã?â??Ã?Â¡Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â?¦Ã?Â¡Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Âºltimo
				// dÃ?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?Â¢Ã¢â?¬Å¾Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â??Ã?Â Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¾Ã?â??Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã¢â?¬Â¦Ã?â??Ã?Â¡Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â?¦Ã?Â¡Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â­gito
				// do CPF

			CPF[9] = x;

			// MÃ?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?Â¢Ã¢â?¬Å¾Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â??Ã?Â Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¾Ã?â??Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã¢â?¬Â¦Ã?â??Ã?Â¡Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â?¦Ã?Â¡Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â©todo
			// da
			// Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?Â¢Ã¢â?¬Å¾Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â??Ã?Â Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¾Ã?â??Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã¢â?¬Â¦Ã?â??Ã?Â¡Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â?¦Ã?Â¡Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â¡rvore
			// para obter o y
			soma = 0;
			for (count = 0; count < 10; count++) {
				// Pegar soma da
				// permutaÃ?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?Â¢Ã¢â?¬Å¾Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â??Ã?Â Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¾Ã?â??Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã¢â?¬Â¦Ã?â??Ã?Â¡Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â?¦Ã?Â¡Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â§Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?Â¢Ã¢â?¬Å¾Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â??Ã?Â Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¾Ã?â??Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã¢â?¬Â¦Ã?â??Ã?Â¡Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â?¦Ã?Â¡Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â£o
				// dos
				// dÃ?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?Â¢Ã¢â?¬Å¾Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â??Ã?Â Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¾Ã?â??Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã¢â?¬Â¦Ã?â??Ã?Â¡Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â?¦Ã?Â¡Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â­gitos
				// do CPF
				soma += CPF[count] * (11 - count);
			}

			// se o resto da
			// divisÃ?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?Â¢Ã¢â?¬Å¾Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â??Ã?Â Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¾Ã?â??Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã¢â?¬Â¦Ã?â??Ã?Â¡Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â?¦Ã?Â¡Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â£o
			// der 0 ou 1, y
			// terÃ?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?Â¢Ã¢â?¬Å¾Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â??Ã?Â Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¾Ã?â??Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã¢â?¬Â¦Ã?â??Ã?Â¡Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â?¦Ã?Â¡Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â¡
			// dois
			// dÃ?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?Â¢Ã¢â?¬Å¾Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â??Ã?Â Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¾Ã?â??Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã¢â?¬Â¦Ã?â??Ã?Â¡Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â?¦Ã?Â¡Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â­gitos
			if (soma % 11 < 2) {
				y = 0;
			} // y
				// nÃ?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?Â¢Ã¢â?¬Å¾Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â??Ã?Â Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¾Ã?â??Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã¢â?¬Â¦Ã?â??Ã?Â¡Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â?¦Ã?Â¡Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â£o
				// pode ter dois
				// dÃ?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?Â¢Ã¢â?¬Å¾Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â??Ã?Â Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¾Ã?â??Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã¢â?¬Â¦Ã?â??Ã?Â¡Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â?¦Ã?Â¡Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â­gitos
			else {
				y = 11 - (soma % 11);
			} // obtendo o
				// Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?Â¢Ã¢â?¬Å¾Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â??Ã?Â Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¾Ã?â??Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã¢â?¬Â¦Ã?â??Ã?Â¡Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â?¦Ã?Â¡Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Âºltimo
				// dÃ?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?Â¢Ã¢â?¬Å¾Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â??Ã?Â Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¾Ã?â??Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã¢â?¬Â¦Ã?â??Ã?Â¡Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â?¦Ã?Â¡Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â­gito
				// do CPF

			CPF[10] = y;
			soma = 0;

			// Verificando se o cpf informado
			// Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?Â¢Ã¢â?¬Å¾Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â??Ã?Â Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¾Ã?â??Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã¢â?¬Â¦Ã?â??Ã?Â¡Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â?¦Ã?Â¡Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â©
			// vÃ?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?Â¢Ã¢â?¬Å¾Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â??Ã?Â Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¾Ã?â??Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã¢â?¬Â¦Ã?â??Ã?Â¡Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â?¦Ã?Â¡Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â¡lido
			// para retornar resultado
			// ao programa
			for (count = 0; count < 11; count++) {
				CPFvalido += String.valueOf(CPF[count]);
			}
			if (cpf.compareTo(CPFvalido) == 0) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	public static String gerarCPFValido() {
		try {
			Double numeroRandomico = Math.random();
			String cpf = numeroRandomico.toString();
			cpf = cpf.substring(cpf.lastIndexOf(".") + 1, 11);
			char vetorChar[] = new char[11];
			String temp, CPFvalido = "";
			int count, x, y, CPF[] = new int[11];
			int soma = 0;

			for (count = 0; count < 9; count++) {
				vetorChar = cpf.toCharArray();
				temp = String.valueOf(vetorChar[count]);
				CPF[count] = Integer.parseInt(temp);
			}

			CPF[9] = 0;
			CPF[10] = 0;

			for (count = 0; count < 9; count++) {
				soma += CPF[count] * (10 - count);
			}

			if (soma % 11 < 2) {
				x = 0;
			} else {
				x = 11 - (soma % 11);
			}

			CPF[9] = x;
			soma = 0;

			for (count = 0; count < 10; count++) {
				soma += CPF[count] * (11 - count);
			}

			if (soma % 11 < 2) {
				y = 0;
			} else {
				y = 11 - (soma % 11);
			}

			CPF[10] = y;
			for (count = 0; count < 11; count++) {
				CPFvalido += String.valueOf(CPF[count]);
			}

			return CPFvalido;
		} catch (Exception e) {
			return "";
		}
	}

	public static String getNomeResumido(String nome) {
		List<String> preposicoes2 = new ArrayList(0);
		preposicoes2.add("dos");
		preposicoes2.add("das");
		preposicoes2.add("de");
		preposicoes2.add("da");
		preposicoes2.add("e");
		preposicoes2.add("a");
		preposicoes2.add("i");
		preposicoes2.add("o");
		preposicoes2.add("DOS");
		preposicoes2.add("DAS");
		preposicoes2.add("DE");
		preposicoes2.add("DA");
		preposicoes2.add("E");
		preposicoes2.add("A");
		preposicoes2.add("I");
		preposicoes2.add("O");
		String[] nomes = nome.split(" ");
		String nomeResumido = "";
		Integer indice = 1;
		nomeResumido += nomes[0] + " ";
		while (indice < nomes.length - 1) {
			if (preposicoes2.contains(nomes[indice])) {
				nomes[indice] = "";
			} else if (nomes[indice].length() > 2) {
				nomes[indice] = nomes[indice].substring(0, 1).concat(".");
			}
			nomeResumido += nomes[indice] + " ";
			indice = indice + 1;
		}
		preposicoes2 = null;
		nomeResumido += nomes[nomes.length - 1] + " ";
		return nomeResumido;

	}

	public static String getNomeArquivo(String caminhoCompleto) {
		int indice = caminhoCompleto.lastIndexOf(File.separator);
		return caminhoCompleto.substring(indice + 1);
	}

	public static String getNomeArquivoComUnidadeEnsino(String nomeArquivo, Integer unidadeEnsino) {
		return nomeArquivo.concat("{").concat(unidadeEnsino.toString()).concat("}");
	}

	public static String getExtensaoDeArquivo(File arquivo) {
		return getExtensaoDeArquivo(arquivo.getName());
	}

	public static String getExtensaoDeArquivo(String nomeArquivo) {
		return nomeArquivo.substring(nomeArquivo.length() - 4).replace(".", "");
	}

	public static String preencherComZerosPosicoesVagas(String padrao, int tamanhoGeracao) {
		if (tamanhoGeracao > padrao.length()) {
			int nrPosicoesPreencher = tamanhoGeracao - padrao.length();
			while (nrPosicoesPreencher > 0) {
				padrao = "0" + padrao;
				nrPosicoesPreencher--;
			}
		}
		return padrao;
	}

	/**
	 * Metodo retorna um new date com o numero de meses a menos que a data atual
	 *
	 * @param numeroMeses
	 * @return Esta mudando somente os meses
	 */
	public static Date getNewDateComMesesAMenos(int numeroMeses) {
		GregorianCalendar dataInicio = new GregorianCalendar();
		dataInicio.roll(GregorianCalendar.MONTH, -numeroMeses);
		if (dataInicio.get(GregorianCalendar.MONTH) == 11) {
			dataInicio.roll(GregorianCalendar.YEAR, -1);
		}
		return dataInicio.getTime();
	}

	/**
	 * Diminui os meses e anos se necessÃ?Æ?Ã?â??Ã?â? Ã¢â? â?¢Ã?Æ?Ã¢â? Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã â??Ã?Â¢Ã¢â ?Â¬Ã?Â Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â? Å¡Ã?Â¬Ã?Â¢Ã¢â? Å¾Ã?Â¢Ã?Æ?Ã?â??Ã?â  Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â? Å¡Ã?Â¬Ã?â??Ã?Â Ã?Æ?Ã?â??Ã?â?? Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â ?Â¬Ã?Â¡Ã?â??Ã?Â Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â Ã?Â¾Ã?â??Ã?Â¢Ã?Æ?Ã â??Ã?â? Ã¢â?¬ â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â ?Â¬Ã¢â??Â¢Ã?Æ?Ã â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â ?Â¬Ã Â¡Ã?â??Ã?Â¬Ã?Æ?Ã¢â?¬Â¦Ã?â??Ã?Â Ã?Æ?Ã?â??Ã?â? Ã¢â?¬ â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â? Å¡Ã?Â¬Ã?â?¦Ã?Â
	 * Ã?Æ?Ã?â??Ã?Â¢Ã¢â? Â¬Ã?Â¡Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â¡rio
	 */
	public static Date getObterDataComMesesAMenos(Date data, int numeroMeses) {
		GregorianCalendar dataInicio = new GregorianCalendar();
		dataInicio.setTime(data);
		dataInicio.add(GregorianCalendar.MONTH, -numeroMeses);
		return dataInicio.getTime();
	}

	/**
	 * Metodo retorna um new date com o um mes a mais que a data atual
	 *
	 * @param numeroMeses
	 * @return
	 */
	public static Date getNewDateComUmMesAMais() {
		GregorianCalendar dataInicio = new GregorianCalendar();
		dataInicio.roll(GregorianCalendar.MONTH, +1);
		if (dataInicio.get(GregorianCalendar.MONTH) == 0) {
			dataInicio.roll(GregorianCalendar.YEAR, +1);
		}
		return dataInicio.getTime();
	}

	/**
	 * Retorna valor inteiro para uma string e retira da string qualquer coisa que nao seja numero. otima para tratar input text.
	 *
	 * @param numero
	 * @return
	 */
	public static Integer getValorInteiro(String numero) {
		numero = numero.replaceAll("\\D", "");
		if (isAtributoPreenchido(numero)) {
			return Integer.parseInt(numero);
		}
		return 0;
	}

	public static Long getValorLong(String numero) {
		numero = numero.replaceAll("\\D", "");
		if (isAtributoPreenchido(numero)) {
			return Long.parseLong(numero);
		}
		return 0L;
	}

	public static double getValorDoubleComCasasDecimais(String numero) {
		numero = numero.replaceAll("\\D", "");
		if (isAtributoPreenchido(numero)) {
			return Double.parseDouble(numero) / 100;
		}
		return 0d;
	}

	public static String getValorString(String numero) {
		if (numero != null && !numero.replaceAll(" ", "").trim().isEmpty()) {
			return numero;
		}
		return "";
	}

	public static void checkState(boolean expression, Object errorMessage) {
		if (expression) {
			throw new StreamSeiException(errorMessage.toString());
		}
	}

	public static boolean isAtributoPreenchido(StringBuilder texto) {
		return texto != null && isAtributoPreenchido(texto.toString());
	}

	public static boolean isAtributoPreenchido(String texto) {
		return texto != null && !texto.trim().equals("0") && !texto.trim().isEmpty();
	}

	public static boolean isAtributoPreenchido(List<?> lista) {
		return lista != null && !lista.isEmpty();
	}

	public static boolean isAtributoPreenchido(Map<?,?> mapa) {
		return mapa != null && !mapa.isEmpty();
	}

	public static boolean isAtributoPreenchido(Object objeto) {
		try {
			if (objeto != null) {
				if (objeto instanceof Double || objeto instanceof Float || objeto instanceof Long || objeto instanceof Integer || objeto instanceof Date || objeto instanceof Enum || objeto instanceof BigDecimal) {
					return objeto != null;
				}
				if (objeto instanceof String) {
					return objeto != null && !((String) objeto).trim().equals("0") && !((String) objeto).trim().isEmpty();
				}
				if (objeto instanceof SuperControle) {
					return objeto != null;
				}
				if (objeto instanceof PerfilAcessoSubModuloEnum[]) {
					return objeto != null;
				}
				if (objeto instanceof MatriculaVO) {
					return isAtributoPreenchido((String) UtilReflexao.invocarMetodoGet(objeto, "matricula"));
				}
				Integer i = (Integer) UtilReflexao.invocarMetodoGet(objeto, "codigo");
				return ((i != null && !i.equals(0)));
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean isAtributoPreenchido(Optional<?> optional) {
		return optional.isPresent() && Uteis.isAtributoPreenchido(optional.get());
	}

	public static boolean isAtributoPreenchido(Enum<?> objeto) {
		try {
			return objeto != null && !objeto.name().equals("NENHUM") && !objeto.name().equals("NENHUMA");
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean isAtributoPreenchido(Integer inteiro) {
		return inteiro != null && !inteiro.equals(0);
	}

	/**
	 * Caso o parametro isConsiderarZero = true -> considera o parametro inteiro com valor igual a zero como preenchido
	 *
	 * @param inteiro
	 * @param isConsiderarZero
	 * @return
	 *
	 */
	public static boolean isAtributoPreenchido(Integer inteiro, boolean isConsiderarZero) {
		return isConsiderarZero ? (inteiro != null) : isAtributoPreenchido(inteiro);
	}
	
	public static double arrendondarForcando2CasasDecimais(double valor) {
		boolean numeroNegativo = false;
		if (valor < 0) {
			numeroNegativo = true;
		}
		valor = Uteis.arredondar(valor, 2, 1);
		String valorStr = String.valueOf(valor);

		String inteira = valorStr.substring(0, valorStr.indexOf("."));
		String extensao = valorStr.substring(valorStr.indexOf(".") + 1, valorStr.length());
		if (extensao.length() == 1) {
			extensao += "0";
		}
		valorStr = UteisTexto.removerMascara(inteira) + "." + extensao;
		if (!numeroNegativo) {
			return Double.parseDouble(valorStr);
		} else {
			return (-1.0 * Double.parseDouble(valorStr));
		}
	}

	public static boolean isAtributoPreenchido(Long inteiro) {
		return inteiro != null && !inteiro.equals(0L);
	}

	public static boolean isAtributoPreenchido(Double d) {
		return d != null && !d.equals(0.0);
	}

	public static boolean isAtributoPreenchido(BigDecimal d) {
		return d != null && !d.equals(BigDecimal.ZERO) ;
	}

	public static boolean isAtributoPreenchido(Date data) {
		return data != null;
	}

	public static boolean isAtributoPreenchido(java.sql.Date data) {
		return data != null;
	}

	public static boolean isAtributoPreenchido(Boolean booleano) {
		return booleano != null;
	}

	public static java.sql.Date getSQLData(java.util.Date dataConverter) {
		if (dataConverter == null) {
			return null;
		}
		java.sql.Date dataSQL = new java.sql.Date(dataConverter.getTime());
		return dataSQL;
	}

	public static String getDataComHora(java.util.Date dataConverter) {
		if (dataConverter == null) {
			return "";
		}
		DateFormat formatador = DateFormat.getDateInstance(DateFormat.SHORT);
		String dataStr = formatador.format(dataConverter);

		DateFormat formatadorHora = DateFormat.getTimeInstance(DateFormat.SHORT);
		dataStr += " " + formatadorHora.format(dataConverter);

		return dataStr;
	}

	public static String encriptar(String senha) throws UnsupportedEncodingException {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256"); // "MD5"
			md.update(senha.getBytes());
			return converterParaHexa(md.digest());
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}

	/**
	 * Retorna uma String cryptografado em MD5
	 *
	 * @param String
	 * @return String
	 */
	public static String encriptarMD5(String senha) throws UnsupportedEncodingException {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(senha.getBytes());
			return converterParaHexa(md.digest());
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}

	/**
	 * Retorna uma String cryptografado em SHA1
	 *
	 * @param String
	 * @return String
	 */
	public static String encriptarSHA(String senha) throws UnsupportedEncodingException {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA1");
			md.update(senha.getBytes());
			return converterParaHexa(md.digest());
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}

	/**
	 * Retorna uma String cryptografado em MSCHAPV2
	 *
	 * @param String
	 * @return String
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 */
	public static String encriptarMSCHAPV2(String senha) throws UnsupportedEncodingException{


        //http://comments.gmane.org/gmane.network.samba.java/8580
        String ntHash = "";
        MD4 md4 = new MD4();
        byte[] bpass;
        try {
            bpass = senha.getBytes("UnicodeLittleUnmarked");
            md4.engineUpdate(bpass, 0, bpass.length);
            byte[] hashbytes = new byte[32];
            hashbytes = md4.engineDigest();
            ntHash = new String(Hexdump.toHexString(hashbytes, 0,hashbytes.length * 2));
            SecretKey key = KeyGenerator.getInstance("DES").generateKey();
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedBytes = cipher.doFinal( senha.getBytes() );
           return converterParaHexa(encryptedBytes);
        }
        catch (UnsupportedEncodingException e) {
        	return null;
        } catch (NoSuchAlgorithmException e) {
        	return null;
		} catch (NoSuchPaddingException e) {
			return null;
		} catch (InvalidKeyException e) {
			return null;
		} catch (IllegalBlockSizeException e) {
			return null;
		} catch (BadPaddingException e) {
			return null;
		}

	}

	public static String encriptarSenhaSHA1(String senha)
			throws UnsupportedEncodingException, NoSuchAlgorithmException {
		MessageDigest algorithm = MessageDigest.getInstance("SHA-1");
		byte messageDigest[] = algorithm.digest(senha.getBytes("UTF-8"));
		StringBuilder hexString = new StringBuilder();
		for (byte b : messageDigest) {
			hexString.append(String.format("%02X", 0xFF & b));
		}
		return hexString.toString();
	}

	private static String converterParaHexa(byte[] bytes) {
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			int parteAlta = ((bytes[i] >> 4) & 0xf) << 4;
			int parteBaixa = bytes[i] & 0xf;
			if (parteAlta == 0) {
				s.append('0');
			}
			s.append(Integer.toHexString(parteAlta | parteBaixa));
		}
		return s.toString();
	}

	public static String getDataAno4Digitos(java.util.Date dataConverter) {
		String dataStr = "";
		if (dataConverter != null) {
			DateFormat formatador = DateFormat.getDateInstance(DateFormat.MEDIUM);
			dataStr = formatador.format(dataConverter);
		}
		return dataStr;
	}

	public static Date getDataVencimentoPadrao(Integer diaPadrao, Date dataPadrao, int nrMesesProgredir) throws Exception {
		int mes = Uteis.getMesData(dataPadrao);
		int ano = Uteis.getAnoData(dataPadrao);
		String diaFinal = String.valueOf(diaPadrao);

		mes = mes + nrMesesProgredir;
		if (mes > 12) {
			mes = mes - 12;
			ano = ano + 1;
		}

		String mesFinal = String.valueOf(mes);
		String anoFinal = String.valueOf(ano);

		if (String.valueOf(mesFinal).length() == 1) {
			mesFinal = "0" + mesFinal;
		}
		if (String.valueOf(diaFinal).length() == 1) {
			diaFinal = "0" + diaFinal;
		}
		String dataFinal = diaFinal + "/" + mesFinal + "/" + anoFinal;
		return Uteis.getDateSemHora(Uteis.getDate(dataFinal));
	}

	public static Date getDataVencimentoDiaPorMesAno(int diaPadrao, int mes, int ano, int nrMesesProgredir) throws Exception {

		String diaFinal = String.valueOf(diaPadrao);

		mes = mes + nrMesesProgredir;
		if (mes > 12) {
			mes = mes - 12;
			ano = ano + 1;
		}

		String mesFinal = String.valueOf(mes);
		String anoFinal = String.valueOf(ano);

		if (String.valueOf(mesFinal).length() == 1) {
			mesFinal = "0" + mesFinal;
		}
		if (String.valueOf(diaFinal).length() == 1) {
			diaFinal = "0" + diaFinal;
		}
		String dataFinal = diaFinal + "/" + mesFinal + "/" + anoFinal;
		return Uteis.getDateSemHora(Uteis.getDate(dataFinal));
	}

	public static int getCalculaQuantidadeMesesEntreDatas(Date dataInicio, Date dataTermino) throws Exception {
		int total = 0;

		String dataIni = Uteis.getData(dataInicio);
		dataInicio = Uteis.getDate(dataIni);

		String dataFim = Uteis.getData(dataTermino);
		dataTermino = Uteis.getDate(dataFim);

		int nrDiasEntreDatas = (int) Uteis.nrDiasEntreDatas(dataTermino, dataInicio);
		total = nrDiasEntreDatas / 30;

		return total;
	}

	public static int getObterQuantidadeParcelasEntreDias(Date dataInicio, Date dataTermino, int diaVencimento) throws Exception {
		int meses = 0;
		int mesesCompletos = getMesData(dataTermino) - getMesData(dataInicio) - 1;

		int diaMesInicio = getDiaMesData(dataInicio);
		int diaMesTermino = getDiaMesData(dataTermino);

		if (diaVencimento >= diaMesInicio) {
			meses += 1;
		}
		if (diaVencimento <= diaMesTermino) {
			meses += 1;
		}
		return meses + mesesCompletos;
	}

	public static int getCalculaQuantidadeAnosEntreDatas(Date dataInicio, Date dataTermino) throws Exception {
		int total = 0;

		String dataIni = Uteis.getData(dataInicio);
		dataInicio = Uteis.getDate(dataIni);

		String dataFim = Uteis.getData(dataTermino);
		dataTermino = Uteis.getDate(dataFim);

		int nrDiasEntreDatas = (int) Uteis.nrDiasEntreDatas(dataTermino, dataInicio);
		total = nrDiasEntreDatas / 365;

		return total;
	}

	public static String getCompletarNumeroComZero(Integer tamanhoDesejado, Integer numero) throws Exception {
		String numFinal = "";
		int tamanhoPreenchimento = tamanhoDesejado - String.valueOf(numero).length();
		while (tamanhoPreenchimento > 0) {
			numFinal += "0";
			tamanhoPreenchimento--;
		}
		numFinal += String.valueOf(numero);
		return numFinal;
	}

	@SuppressWarnings("unchecked")
	public static <T> T deepClone(T object, Class<T> clazz) {
		try {

			String jsonString = OBJECT_MAPPER.writeValueAsString(object);

			return OBJECT_MAPPER.readValue(jsonString, clazz);

		} catch (Exception e) {

			throw new RuntimeException("Falha ao clonar objeto: " + e.getMessage(), e);
		}
	}

	public static String obterZeroEsquerda(String valor) throws Exception {
		String zeroEsquerda = "";
		int tamanho = valor.length();
		int cont = 0;
		if (valor.contains("0")) {
			while (cont < tamanho) {
				if (valor.charAt(cont) == '0') {
					zeroEsquerda += valor.charAt(cont);
				} else {
					cont = tamanho;
				}
				cont++;
			}
		}
		return zeroEsquerda;
	}

	public static java.sql.Date getDataJDBC(java.util.Date dataConverter) {
		if (dataConverter == null) {
			return null;
			// dataConverter = new Date();
		}
		java.sql.Date dataSQL = new java.sql.Date(dataConverter.getTime());
		return dataSQL;
	}

	public static java.sql.Timestamp getDataComHoraSetadaParaUltimoMinutoDia(java.util.Date dataConverter) {
		if (dataConverter == null) {
			return null;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(dataConverter);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 59);
		return new java.sql.Timestamp(c.getTime().getTime());
	}

	public static java.sql.Timestamp getDataComHoraSetadaParaPrimeiroMinutoDia(java.util.Date dataConverter) {
		if (dataConverter == null) {
			return null;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(dataConverter);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return new java.sql.Timestamp(c.getTime().getTime());
	}

	public static Date getDataUltimoDiaMes(java.util.Date dataConverter) {
		Calendar c = Calendar.getInstance();
		c.setTime(dataConverter);
		c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
		return c.getTime();
	}

	public static Date getDataPrimeiroDiaMes(java.util.Date dataConverter) {
		Calendar c = Calendar.getInstance();
		c.setTime(dataConverter);
		c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
		return c.getTime();
	}

	public static java.sql.Timestamp getDataJDBCTimestamp(java.util.Date dataConverter) {
		if (dataConverter == null) {
			return null;
		}
		java.sql.Timestamp dataSQL = new java.sql.Timestamp(dataConverter.getTime());
		return dataSQL;
	}

	public static String getDataFormatoBD(java.util.Date dataConverter) {
		return getDataBD(dataConverter, "bd");
	}

	public static String getDataBD(java.util.Date dataConverter, String padrao) {
		if (dataConverter == null) {
			return ("");
		}
		String dataStr;
		if (padrao.equals("bd")) {
			Locale aLocale = new Locale("pt", "BR");
			SimpleDateFormat formatador = new SimpleDateFormat("yyyy.MM.dd", aLocale);
			dataStr = formatador.format(dataConverter);
		} else {
			DateFormat formatador = DateFormat.getDateInstance(DateFormat.SHORT);
			dataStr = formatador.format(dataConverter);
		}
		return (dataStr);
	}

	public static String getDataBD2Digitos(java.util.Date dataConverter, String padrao) {
		if (dataConverter == null) {
			return ("");
		}
		String dataStr;
		if (padrao.equals("bd")) {
			Locale aLocale = new Locale("pt", "BR");
			SimpleDateFormat formatador = new SimpleDateFormat("yy.MM.dd", aLocale);
			dataStr = formatador.format(dataConverter);
		} else {
			DateFormat formatador = DateFormat.getDateInstance(DateFormat.SHORT);
			dataStr = formatador.format(dataConverter);
		}
		return (dataStr);
	}

	public static String getDataAplicandoFormatacao(Date data, String mascara) {
		if (data == null) {
			return "";
		}
		Locale aLocale = new Locale("pt", "BR");
		SimpleDateFormat formatador = new SimpleDateFormat(mascara, aLocale);
		String dataStr = formatador.format(data);
		return dataStr;
	}

	public static Date getDataVencimentoPadrao(Integer dataPadrao) throws Exception {
		int dia = Uteis.getDiaMesData(new Date());
		int mes = Uteis.getMesDataAtual();
		int ano = Uteis.getAnoData(new Date());
		String diaFinal = String.valueOf(dataPadrao);
		if (dia > dataPadrao.intValue()) {
			mes = mes + 1;
			if (mes > 12) {
				mes = 1;
				ano = ano + 1;
			}
		}
		String mesFinal = String.valueOf(mes);
		String anoFinal = String.valueOf(ano);

		if (String.valueOf(mesFinal).length() == 1) {
			mesFinal = "0" + mesFinal;
		}
		if (String.valueOf(diaFinal).length() == 1) {
			diaFinal = "0" + diaFinal;
		}
		String dataFinal = diaFinal + "/" + mesFinal + "/" + String.valueOf(ano);
		return Uteis.getDate(dataFinal);
	}

	/*
	 * Defini-se a mascarÃ?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â? Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â? ?Ã?Â¢Ã¢â ?Â¬Ã?Â Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å Ã?Â¬Ã?Â¢Ã¢â? Å¾Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â? Å¡Ã?Â¬Ã?â??Ã?Â Ã?Æ?Ã?â??Ã?â?? Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â ?Â¬Ã?Â¡Ã?â??Ã?Â Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â Ã?Â¾Ã?â??Ã?Â¢Ã?Æ?Ã â??Ã?â? Ã¢â?¬ â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â ?Â¬Ã¢â??Â¢Ã?Æ?Ã â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â ?Â¬Ã Â¡Ã?â??Ã?Â¬Ã?Æ?Ã¢â?¬Â¦Ã?â??Ã?Â Ã?Æ?Ã â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â? 
	 * Å¡Ã?Â¬Ã?â?¦Ã?Â¡Ã?Æ?Ã?â??Ã?Â¢Ã¢â? Â¬Ã?Â¡Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â¡ a ser aplicada a data atual de acordo com o padrÃ?Æ?Ã?â??Ã?â? Ã¢â? â?¢Ã?Æ?Ã¢â? Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã â??Ã?Â¢Ã¢â ?Â¬Ã?Â Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â? Å¡Ã?Â¬Ã?Â¢Ã¢â? Å¾Ã?Â¢Ã?Æ?Ã?â??Ã?â  Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â? Å¡Ã?Â¬Ã?â??Ã?Â Ã?Æ?Ã?â??Ã?â?? Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â ?Â¬Ã?Â¡Ã?â??Ã?Â Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â Ã?Â¾Ã?â??Ã?Â¢Ã?Æ?Ã â??Ã?â? Ã¢â?¬ â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â ?Â¬Ã¢â??Â¢Ã?Æ?Ã â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â ?Â¬Ã
	 * Â¡Ã?â??Ã?Â¬Ã?Æ?Ã¢â?¬Â¦Ã?â??Ã?Â Ã?Æ?Ã â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?  Å¡Ã?Â¬Ã?â?¦Ã?Â¡Ã?Æ?Ã?â??Ã?Â¢Ã¢â? Â¬Ã?Â¡Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â£o - dd/mm/yyyy ou MM.yy.dd e assim por diante
	 */
	public static String getDataAtualAplicandoFormatacao(String mascara) {
		Date hoje = new Date();
		return getDataAplicandoFormatacao(hoje, mascara);
	}

	public static String getData(java.util.Date dataConverter) {
		if (dataConverter == null) {
			return "";
		}
		return (getDataBD(dataConverter, ""));
	}

	public static String getData2Digitos(java.util.Date dataConverter) {
		return (getDataBD2Digitos(dataConverter, ""));
	}

	public static java.util.Date getDateHoraFinalDia(Date dataPrm) {
		try {
			// java.util.Date valorData = null;
			// DateFormat formatador =
			// DateFormat.getDateInstance(DateFormat.SHORT);
			// valorData = formatador.parse(Uteis.getData(dataPrm));
			Calendar cal = Calendar.getInstance();

			cal.setTime(dataPrm);
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 59);

			return cal.getTime();
		} catch (Exception e) {
			return new Date();
		}
	}

	public static java.util.Date getDateSemHora(Date dataPrm) throws Exception {
		java.util.Date valorData = null;
		DateFormat formatador = DateFormat.getDateInstance(DateFormat.SHORT);
		valorData = formatador.parse(Uteis.getData(dataPrm));
		Calendar cal = Calendar.getInstance();

		cal.setTime(valorData);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);

		return cal.getTime();
	}

	public static Date getDataMinutos(Date data) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(data);
		cal.set(Calendar.HOUR, 10);
		return cal.getTime();
	}

	public static Date getDataMinutosHoraDia(Date data) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(data);
		cal.set(Calendar.HOUR_OF_DAY, 10);
		return cal.getTime();
	}

	public static java.util.Date getDate(String data) throws ParseException {
		java.util.Date valorData = null;
		try {
			DateFormat formatador = DateFormat.getDateInstance(DateFormat.SHORT);
			valorData = formatador.parse(data);
		} catch (ParseException e) {
			SimpleDateFormat formatador = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US );
			valorData = formatador.parse(data);
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int hora = cal.get(Calendar.HOUR_OF_DAY);
		int minuto = cal.get(Calendar.MINUTE);
		int segundo = cal.get(Calendar.SECOND);

		cal.setTime(valorData);
		cal.set(Calendar.HOUR_OF_DAY, hora);
		cal.set(Calendar.MINUTE, minuto);
		cal.set(Calendar.SECOND, segundo);

		return cal.getTime();
	}

	/**
	 * MÃ?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬ Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â? ?Ã?Â¢Ã¢â ?Â¬Ã?Â Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å Ã?Â¬Ã?Â¢Ã¢â? Å¾Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â? Å¡Ã?Â¬Ã?â??Ã?Â Ã?Æ?Ã?â??Ã?â?? Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â ?Â¬Ã?Â¡Ã?â??Ã?Â Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â Ã?Â¾Ã?â??Ã?Â¢Ã?Æ?Ã â??Ã?â? Ã¢â?¬ â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â ?Â¬Ã¢â??Â¢Ã?Æ?Ã â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â ?Â¬Ã Â¡Ã?â??Ã?Â¬Ã?Æ?Ã¢â?¬Â¦Ã?â??Ã?Â Ã?Æ?Ã?â??Ã?â? Ã¢â?¬ â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â? Å¡Ã?Â¬Ã?â?¦Ã?Â Ã?Æ?Ã?â??Ã?Â¢Ã¢â?
	 * Â¬Ã?Â¡Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â©todo que recebe uma data como String, e a retorna como um Date, considerando o ultimo dia do mÃ?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â? Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â? ?Ã?Â¢Ã¢â ?Â¬Ã?Â Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å Ã?Â¬Ã?Â¢Ã¢â? Å¾Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â? Å¡Ã?Â¬Ã?â??Ã?Â Ã?Æ?Ã?â??Ã?â?? Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â ?Â¬Ã?Â¡Ã?â??Ã?Â Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â Ã?Â¾Ã?â??Ã?Â¢Ã?Æ?Ã â??Ã?â? Ã¢â?¬ â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â ?Â¬Ã¢â??Â¢Ã?Æ?Ã â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â ?Â¬Ã Â¡Ã?â??Ã?Â¬Ã?Æ?Ã¢â?¬Â¦Ã?â??Ã?Â
	 * Ã?Æ?Ã?â??Ã?â? Ã¢â?¬ â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â? Å¡Ã?Â¬Ã?â?¦Ã?Â Ã?Æ?Ã?â??Ã?Â¢Ã¢â? Â¬Ã?Â¡Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Âªs. Ou seja, ao receber uma data do tipo 31/02/2011, ele nunca retornarÃ?Æ?Ã?â? ?Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â? Â Ã?Â¢Ã¢â? Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?Â¢Ã¢â ?Â¬Ã?Â  Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?Â¢Ã¢â? Å¾Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬ â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â? Å¡Ã?Â¬Ã?â??Ã?Â Ã?Æ?Ã?â??Ã?â?? Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â ?Â¬Ã?Â¡Ã?â??Ã?Â Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â Ã?Â¾Ã?â??Ã?Â¢Ã?Æ?Ã â??Ã?â? Ã¢â?¬ â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â ?Â¬Ã¢â??Â¢Ã?Æ?Ã
	 * â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â ?Â¬Ã Â¡Ã?â??Ã?Â¬Ã?Æ?Ã¢â?¬Â¦Ã?â??Ã?Â Ã?Æ?Ã?â??Ã?â? Ã¢â?¬ â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â? Å¡Ã?Â¬Ã?â?¦Ã?Â Ã?Æ?Ã?â??Ã?Â¢Ã¢â? Â¬Ã?Â¡Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â¡ 03/03/2011, e sim o Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â? Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â? ?Ã?Â¢Ã¢â ?Â¬Ã?Â Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å Ã?Â¬Ã?Â¢Ã¢â? Å¾Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â? Å¡Ã?Â¬Ã?â??Ã?Â Ã?Æ?Ã?â??Ã?â?? Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â ?Â¬Ã?Â¡Ã?â??Ã?Â Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â Ã?Â¾Ã?â??Ã?Â¢Ã?Æ?Ã â??Ã?â? Ã¢â?¬
	 * â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â ?Â¬Ã¢â??Â¢Ã?Æ?Ã â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â ?Â¬Ã Â¡Ã?â??Ã?Â¬Ã?Æ?Ã¢â?¬Â¦Ã?â??Ã?Â Ã?Æ?Ã?â??Ã?â? Ã¢â?¬ â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â? Å¡Ã?Â¬Ã?â?¦Ã?Â Ã?Æ?Ã?â??Ã?Â¢Ã¢â? Â¬Ã?Â¡Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Âºltimo dia do mÃ?Æ?Ã â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â ?Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?Â¢Ã¢â ?Â¬Ã?Â Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?Â¢Ã¢â? Å¾Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬ â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â? Å¡Ã?Â¬Ã?â??Ã?Â Ã?Æ?Ã?â??Ã?â?? Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â ?Â¬Ã?Â¡Ã?â??Ã?Â Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â
	 * Ã?Â¾Ã?â??Ã?Â¢Ã?Æ?Ã â??Ã?â? Ã¢â?¬ â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â ?Â¬Ã¢â??Â¢Ã?Æ?Ã â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â ?Â¬Ã Â¡Ã?â??Ã?Â¬Ã?Æ?Ã¢â?¬Â¦Ã?â??Ã?Â Ã?Æ?Ã?â??Ã?â? Ã¢â?¬ â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â? Å¡Ã?Â¬Ã?â?¦Ã?Â Ã?Æ?Ã?â??Ã?Â¢Ã¢â? Â¬Ã?Â¡Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Âªs 02, 28/02/2011 ou 29/02/2011, dependendo do ano.
	 *
	 * @author Murillo Parreira
	 * @param data
	 *            String da data.
	 * @return dataConsiderandoUltimoDiaDoMes Date da data.
	 * @throws Exception
	 */
	public static java.util.Date getDataConsiderandoUltimoDiaDoMes(String data) throws Exception {
		Integer ano = Integer.valueOf(data.substring(data.lastIndexOf("/") + 1, data.length()));
		Integer mes = Integer.valueOf(data.substring(data.indexOf("/") + 1, data.lastIndexOf("/")));
		Integer dia = Integer.valueOf(data.substring(0, data.indexOf("/")));
		Calendar cal = new GregorianCalendar(ano, mes - 1, 1);
		Integer ultimoDiaDoMes = Integer.valueOf(cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		if (dia > ultimoDiaDoMes) {
			dia = ultimoDiaDoMes;
		}
		String dataConsiderandoUltimoDiaDoMes = dia.toString() + "/" + mes.toString() + "/" + ano.toString();
		SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");
		return formatador.parse(dataConsiderandoUltimoDiaDoMes);
	}

	public static java.util.Date getDate(String data, Locale local) throws Exception {
		java.util.Date valorData = new Date();
		if (local == null) {
			DateFormat formatador = DateFormat.getDateInstance(DateFormat.SHORT);
			valorData = formatador.parse(data);
		} else {
			DateFormat formatador = DateFormat.getDateInstance(DateFormat.SHORT, local);
			valorData = formatador.parse(data);
		}
		return valorData;
	}

	public static String getSemestreAtual() {
		GregorianCalendar gc = new GregorianCalendar();
		int mes = gc.get(GregorianCalendar.MONTH) + 1;
		if (mes > 7) {
			return "2";
		} else if (mes == 7) {
			int dia = getDiaMesData(new Date());
			if (dia <= 10) {
				return "1";
			} else {
				return "2";
			}
		} else {
			return "1";
		}
	}

	public static String getBimestreDoSemestreAtual() {
		GregorianCalendar gc = new GregorianCalendar();
		int mes = gc.get(GregorianCalendar.MONTH) + 1;
		int dia = gc.get(GregorianCalendar.DAY_OF_MONTH);

		boolean segundoSemestre = (mes > 7) || (mes == 7 && dia > 10);

		int indiceNoSemestre;
		if (!segundoSemestre) {
			indiceNoSemestre = Math.min(mes, 6) - 1;
		} else {
			if (mes == 7) {
				indiceNoSemestre = 0;
			} else {
				indiceNoSemestre = mes - 8 + 1;
			}
		}

		int bimestre = (indiceNoSemestre < 3) ? 1 : 2;
		return String.valueOf(bimestre);
	}


	public static String getSemestreData(Date data) {
		if (Uteis.isAtributoPreenchido(data)) {
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(data);
			int mes = gc.get(GregorianCalendar.MONTH) + 1;
			if (mes > 7) {
				return "2";
			} else if (mes == 7) {
				int dia = getDiaMesData(data);
				if (dia <= 20) {
					return "1";
				} else {
					return "2";
				}
			} else {
				return "1";
			}
		} else {
			return "";
		}
	}

	public static String getSemestrePorMes(String mesValidar) {
		if (Uteis.isAtributoPreenchido(mesValidar)) {
			int mes = Integer.parseInt(mesValidar);
			if (mes >= 7) {
				return "2";
			} else {
				return "1";
			}
		} else {
			return "";
		}
	}

	/**
	 * Esta rotina Ã© capaz de calcular, por exmeplo, que serÃ¡ o dÃ©cimo dia Ãºtil do mÃªs. Ou seja calcula uma data especÃ­fica, utilizando com referÃªncia uma quantidade de dias Ãºteis para frente. Dado uma data, o sistema Ã© capaz de calcular 10 dias (nrDiasUteisCalcular) Ãºtis para frente desta data. JÃ¡ descontando, sÃ¡bados e domingos.
	 *
	 * @param dataInicial
	 * @param nrDiasUteisCalcular
	 * @return
	 * @throws Exception
	 */
	public static Date getDataAvancandoNumeroEspecificoDiasUteisInicioMes(Date dataVctoBase, Integer nrDiasUteisAvancar, boolean considerarDiaUtilDataVctoBase) throws Exception {
		Calendar calInicial = Calendar.getInstance();
		calInicial.setTime(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(Uteis.getDataPrimeiroDiaMes(dataVctoBase)));
		calInicial.set(Calendar.HOUR, 0);
		calInicial.set(Calendar.MINUTE, 0);
		calInicial.set(Calendar.SECOND, 0);
		calInicial.set(Calendar.MILLISECOND, 0);

		Integer diasUteisComputados = null;
		int nrDiasAvancados = 0;
		if (considerarDiaUtilDataVctoBase) {
			diasUteisComputados = 0;
			nrDiasAvancados = 0;
		} else {
			diasUteisComputados = 1;
			nrDiasAvancados = 1;
		}
		// Nao faz sentido avancar mais do que trinta dias, pois nÃ£o existem
		// 30 dias uteis no mes.
//		for (int i = 1; ((diasUteisComputados < nrDiasUteisAvancar) && (nrDiasAvancados < 30));) {
//			if (UteisData.getValidaDiaUtil(calInicial.getTime())) { // sabado ou domingo
//				diasUteisComputados++;
//			}
//			if (Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(calInicial.getTime()).compareTo(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(dataVctoBase)) > 0) {
//				return dataVctoBase;
//			}
//			nrDiasAvancados++;
//			if ((diasUteisComputados < nrDiasUteisAvancar) && (nrDiasAvancados < 30)) {
//				calInicial.add(Calendar.DATE, i);
//			}
//		}
		return calInicial.getTime();
	}

	public static Integer getCalculaDiasUteis(Date dataInicial, Date dataFinal) throws Exception {
		Calendar calInicial = Calendar.getInstance();
		Calendar calFinal = Calendar.getInstance();
		calInicial.setTime(dataInicial);
		calFinal.setTime(dataFinal);
		calInicial.set(Calendar.HOUR, 0);
		calInicial.set(Calendar.MINUTE, 0);
		calInicial.set(Calendar.SECOND, 0);
		calInicial.set(Calendar.MILLISECOND, 0);
		calFinal.set(Calendar.HOUR, 0);
		calFinal.set(Calendar.MINUTE, 0);
		calFinal.set(Calendar.SECOND, 0);
		calFinal.set(Calendar.MILLISECOND, 0);
		Integer diasUteis = 0;
		Integer dias = 0;
		for (int i = 1; (calInicial.compareTo(calFinal) <= 0 || Uteis.getData(calInicial.getTime()).equals(Uteis.getData(calFinal.getTime()))); calInicial.add(Calendar.DATE, i)) {
			if (calInicial.get(Calendar.DAY_OF_WEEK) != 1 && calInicial.get(Calendar.DAY_OF_WEEK) != 7) { // sabado
																											// ou
																											// domingo
				diasUteis++;
			}
			dias++;
		}

		return diasUteis;
	}

	public static Integer getCalculaDiasUteisConsiderandoSabado(Date dataInicial, Date dataFinal) throws Exception {
		Integer diasUteis = 0;
		Integer diaSemana = 0;
		while (getDataJDBC(dataInicial).compareTo(getDataJDBC(dataFinal)) <= 0) {
			diaSemana = Uteis.getDiaSemana(dataInicial);
			if (diaSemana != 1) { // sabado ou domingo
				diasUteis++;
			}
			dataInicial = Uteis.obterDataAvancada(dataInicial, 1);
		}
		return diasUteis;
	}

	public static Integer getCalculaDiasNaoUteis(Date dataInicial, Date dataFinal) throws Exception {

		Integer diasNaoUteis = 0;
		Integer diaSemana = 0;
		while (getDataJDBC(dataInicial).compareTo(getDataJDBC(dataFinal)) <= 0) {
			diaSemana = Uteis.getDiaSemana(dataInicial);
			if (diaSemana == 1 || diaSemana == 7) { // sabado ou domingo
				diasNaoUteis++;
			}
			dataInicial = Uteis.obterDataAvancada(dataInicial, 1);
		}
		return diasNaoUteis;
	}

	public static String getAnoDataAtual() {
		return getData(new Date(), "yyyy");
	}

	public static String getAno(Date data) {
		return getData(data, "yyyy");
	}

	public static String getDataAtual() {
		Date hoje = new Date();
		return (Uteis.getData(hoje));
	}

	public static String getHoraAtual() {
		return getData(new Date(), "HH:mm");
	}

	public static String getHoraAtualComSegundos() {
		return getData(new Date(), "HH:mm:ss");
	}

	public static String getHoraAtualComSegundosMilisegundos() {
		return getData(new Date(), "HH:mm:ss.SSS");
	}

	public static String getAno2Digitos(Date data) {
		return getData(data, "yy");
	}

	public static Date getDateTime(Date data, int hora, int minuto, int segundo) {
		Calendar cal = Calendar.getInstance();
		if (data == null) {
			data = new Date();
		}
		cal.setTime(data);
		cal.set(Calendar.HOUR_OF_DAY, hora);
		cal.set(Calendar.MINUTE, minuto);
		cal.set(Calendar.SECOND, segundo);

		return cal.getTime();
	}

	public static String obterDataFormatoTextoddMMyyyy(Date data) {
		try {
			return getData(data, "dd/MM/yyyy");
		} catch (Exception e) {
			return "";
		}
	}

	public static String obterDiferencaHorasDuasDatas(Date dataInicial, Date dataFinal) {
		long diferenca = dataFinal.getTime() - dataInicial.getTime();
		return (diferenca / 1000 / 60 / 60) + ":" + (diferenca / 1000 / 60) + ":" + (diferenca / 1000);
	}

	public static Date obterDiferencaHorasDuasDatasMilisegundos_AcrescentandoEmDataFutura(Date dataInicial, Date dataFinal, Date dataFutura) {
		long diferenca = dataFinal.getTime() - dataInicial.getTime();
		long longDataFutura = dataFutura.getTime() + diferenca;
		return dataFutura = new Date(longDataFutura);
	}

	public static String obterDataFormatoTextoddMMyy(Date data) {
		try {
			return getData(data, "dd/MM/yy");
		} catch (Exception e) {
			return "";
		}
	}

	public static String obterPrimeiroNomeConcatenadoSobreNome(String nome, Integer qtdeSobrenome) {
		try {
			List<String> preposicoes2 = new ArrayList(0);
			preposicoes2.add("dos");
			preposicoes2.add("das");
			preposicoes2.add("de");
			preposicoes2.add("da");
			preposicoes2.add("e");
			preposicoes2.add("a");
			preposicoes2.add("i");
			preposicoes2.add("o");
			String[] listaNome = nome.trim().split(" ");
			StringBuilder resultado = new StringBuilder();
			for (int i = 0; i < listaNome.length; i++) {

				String s = listaNome[i];
				if (s.trim().isEmpty()) {
					continue;
				}
				if (i == 0) {
					resultado.append(s);
				} else if (preposicoes2.contains(s)) {
					resultado.append("");
				} else {
					resultado.append(s.subSequence(0, 1)).append(".");
				}
				resultado.append(" ");
				if (i == qtdeSobrenome) {
					return resultado.toString();
				}

			}
			return resultado.toString();
		} catch (Exception e) {
		}
		return "";
	}

	public static String obterDataFormatoTextoddMM(Date data) {
		try {
			return getData(data, "dd/MM");
		} catch (Exception e) {
			return "";
		}
	}

	public static String getHoraMinutoComMascara(Date data) {
		if (data == null) {
			return "";
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(data);
		String hora = String.valueOf(cal.get(Calendar.HOUR_OF_DAY));
		String minuto = String.valueOf(cal.get(Calendar.MINUTE));
		if (hora.length() == 1) {
			hora = "0" + hora;
		}
		if (minuto.length() == 1) {
			minuto = "0" + minuto;
		}

		return hora + ":" + minuto;
	}

	public static String gethoraHHMM(Date data) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(data);
		String hora = String.valueOf(cal.get(Calendar.HOUR_OF_DAY));
		if (hora.length() == 1) {
			hora = "0" + hora;
		}
		String minuto = String.valueOf(cal.get(Calendar.MINUTE));
		if (minuto.length() == 1) {
			minuto = "0" + minuto;
		}
		return hora + ":" + minuto;
	}

	public static String gethoraHHMMSS(Date data) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(data);
		String hora = String.valueOf(cal.get(Calendar.HOUR_OF_DAY));
		String minuto = String.valueOf(cal.get(Calendar.MINUTE));
		String segundo = String.valueOf(cal.get(Calendar.SECOND));

		return hora + minuto + segundo;
	}

	public static String gethoraHHMMSS_tamanho6(Date data) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(data);
		String hora = String.valueOf(cal.get(Calendar.HOUR_OF_DAY));
		String minuto = String.valueOf(cal.get(Calendar.MINUTE));
		String segundo = String.valueOf(cal.get(Calendar.SECOND));

		return (hora.length() == 1 ? "0" + hora : hora) + (minuto.length() == 1 ? "0" + minuto : minuto) + (segundo.length() == 1 ? "0" + segundo : segundo);
	}

	public static String gethoraHHMMSS(String tempo) {

		String hora = tempo.substring(0, 2);
		String minuto = tempo.substring(3, 5);
		Integer hora1 = Integer.parseInt(hora);
		Integer minuto1 = Integer.parseInt(minuto);
		if (hora1 > 23) {
			tempo = "";
		}
		if (minuto1 > 59) {
			tempo = "";
		}
		return tempo;
	}

	public static String somarHorario(String horario, Integer minuto) {
		String hh = horario.substring(0, 2);
		String mm = horario.substring(3, 5);
		Integer hhi = Integer.parseInt(hh);
		Integer mmi = Integer.parseInt(mm);

		mmi += minuto;

		while (mmi > 59) {
			mmi -= 60;
			hhi += 1;
		}
		while (hhi > 23) {
			hhi -= 24;
			hhi += 1;
		}

		if (mmi.intValue() == 0) {
			mm = "00";

		} else if (mmi.intValue() == 1) {
			mm = "01";

		} else if (mmi.intValue() == 2) {
			mm = "02";

		} else if (mmi.intValue() == 3) {
			mm = "03";

		} else if (mmi.intValue() == 4) {
			mm = "04";

		} else if (mmi.intValue() == 5) {
			mm = "05";

		} else if (mmi.intValue() == 6) {
			mm = "06";

		} else if (mmi.intValue() == 7) {
			mm = "07";

		} else if (mmi.intValue() == 8) {
			mm = "08";

		} else if (mmi.intValue() == 9) {
			mm = "09";

		} else {
			mm = String.valueOf(mmi);
		}

		if (hhi.intValue() == 0) {
			hh = "00";

		} else if (hhi.intValue() == 1) {
			hh = "01";

		} else if (hhi.intValue() == 2) {
			hh = "02";

		} else if (hhi.intValue() == 3) {
			hh = "03";

		} else if (hhi.intValue() == 4) {
			hh = "04";

		} else if (hhi.intValue() == 5) {
			hh = "05";

		} else if (hhi.intValue() == 6) {
			hh = "06";

		} else if (hhi.intValue() == 7) {
			hh = "07";

		} else if (hhi.intValue() == 8) {
			hh = "08";

		} else if (hhi.intValue() == 9) {
			hh = "09";

		} else {
			hh = String.valueOf(hhi);
		}

		return hh + ":" + mm;
	}

	public static String retirarSinaisSimbolosEspacoString(String label) {
		label = label.replaceAll(" ", "");
		label = label.replaceAll("-", "");
		label = label.replaceAll(",", "");
		label = label.replaceAll("_", "");
		return label;
	}

	public static String retirarAcentuacao(String prm) {
		String nova = "";
		for (int i = 0; i < prm.length(); i++) {
			if (prm.charAt(i) == Uteis.A_AGUDO || prm.charAt(i) == Uteis.A_CIRCUNFLEXO || prm.charAt(i) == Uteis.A_CRASE || prm.charAt(i) == Uteis.A_TIO) {
				nova += "a";
			} else if (prm.charAt(i) == Uteis.A_AGUDOMAIUSCULO) {
				nova += "A";
			} else if (prm.charAt(i) == Uteis.E_AGUDO || prm.charAt(i) == Uteis.E_CIRCUNFLEXO) {
				nova += "e";
			} else if (prm.charAt(i) == Uteis.I_AGUDO) {
				nova += "i";
			} else if (prm.charAt(i) == Uteis.O_AGUDO || prm.charAt(i) == Uteis.O_TIO || prm.charAt(i) == Uteis.O_CRASE) {
				nova += "o";
			} else if (prm.charAt(i) == Uteis.U_AGUDO || prm.charAt(i) == Uteis.U_TREMA) {
				nova += "u";
			} else if (prm.charAt(i) == Uteis.C_CEDILHA) {
				nova += "c";
				// } else if (Character.isSpaceChar(prm.charAt(i))){
				// nova += "_";
			} else {
				nova += prm.charAt(i);
			}
		}
		return (nova);
	}

	public static String retirarAcentuacaoAndCaracteresEspeciasRegex(String texto) {
		texto = texto.replaceAll(UteisJSF.internacionalizar("prt_Letra_A"), "A");
		texto = texto.replaceAll(UteisJSF.internacionalizar("prt_Letra_a"), "a");
		texto = texto.replaceAll(UteisJSF.internacionalizar("prt_Letra_E"), "E");
		texto = texto.replaceAll(UteisJSF.internacionalizar("prt_Letra_e"), "e");
		texto = texto.replaceAll(UteisJSF.internacionalizar("prt_Letra_I"), "I");
		texto = texto.replaceAll(UteisJSF.internacionalizar("prt_Letra_i"), "i");
		texto = texto.replaceAll(UteisJSF.internacionalizar("prt_Letra_O"), "O");
		texto = texto.replaceAll(UteisJSF.internacionalizar("prt_Letra_o"), "o");
		texto = texto.replaceAll(UteisJSF.internacionalizar("prt_Letra_U"), "U");
		texto = texto.replaceAll(UteisJSF.internacionalizar("prt_Letra_u"), "u");
		texto = texto.replaceAll(UteisJSF.internacionalizar("prt_Letra_C"), "C");
		texto = texto.replaceAll(UteisJSF.internacionalizar("prt_Letra_c"), "c");
		texto = texto.replaceAll(UteisJSF.internacionalizar("prt_Letra_y"), "y");
		texto = texto.replaceAll(UteisJSF.internacionalizar("prt_Letra_Y"), "Y");
		texto = texto.replaceAll(UteisJSF.internacionalizar("prt_Letra_n"), "n");
		texto = texto.replaceAll(UteisJSF.internacionalizar("prt_Letra_N"), "N");
		texto = texto.replaceAll(UteisJSF.internacionalizar("prt_Letra_D"), "D");
		texto = texto.replaceAll(UteisJSF.internacionalizar("prt_Letra_Esp1"), "");
		texto = texto.replaceAll(UteisJSF.internacionalizar("prt_Letra_Esp2"), "");
		texto = texto.replaceAll(UteisJSF.internacionalizar("prt_Letra_Esp3"), "");

		texto = texto.replaceAll(UteisJSF.internacionalizar("prt_Letra_Esp4"), "a");// acento
																					// Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â¡
		texto = texto.replaceAll(UteisJSF.internacionalizar("prt_Letra_Esp5"), "a");// acento
																					// Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â£
		texto = texto.replaceAll(UteisJSF.internacionalizar("prt_Letra_Esp6"), "A");// acento
																					// Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â£
		texto = texto.replaceAll(UteisJSF.internacionalizar("prt_Letra_Esp7"), "a");// acento
																					// Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â£
		texto = texto.replaceAll(UteisJSF.internacionalizar("prt_Letra_Esp8"), "a");// acento
																					// Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â¢

		texto = texto.replaceAll(UteisJSF.internacionalizar("prt_Letra_Esp9"), "a");// acento
																					// Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â¡
		texto = texto.replaceAll(UteisJSF.internacionalizar("prt_Letra_Esp10"), "a");// acento
																						// Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â 
		texto = texto.replaceAll(UteisJSF.internacionalizar("prt_Letra_Esp11"), "e");// acento
																						// Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â©
		texto = texto.replaceAll(UteisJSF.internacionalizar("prt_Letra_Esp12"), "e");// acento
																						// Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Âª
		texto = texto.replaceAll(UteisJSF.internacionalizar("prt_Letra_Esp13"), "e");// acento
																						// Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â¨
		texto = texto.replaceAll(UteisJSF.internacionalizar("prt_Letra_Esp14"), "i");// acento
																						// Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â­
		texto = texto.replaceAll(UteisJSF.internacionalizar("prt_Letra_Esp15"), "i");// acento
																						// Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â¬
		texto = texto.replaceAll(UteisJSF.internacionalizar("prt_Letra_Esp16"), "i");// acento
																						// Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â®
		texto = texto.replaceAll(UteisJSF.internacionalizar("prt_Letra_Esp17"), "o");// acento
																						// Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â³
		texto = texto.replaceAll(UteisJSF.internacionalizar("prt_Letra_Esp18"), "o");// acento
																						// Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â²
		texto = texto.replaceAll(UteisJSF.internacionalizar("prt_Letra_Esp19"), "o");// acento
																						// Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Âµ
		texto = texto.replaceAll(UteisJSF.internacionalizar("prt_Letra_Esp20"), "o");// acento
																						// Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â´
		texto = texto.replaceAll(UteisJSF.internacionalizar("prt_Letra_Esp21"), "u");// acento
																						// Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Âº
		texto = texto.replaceAll(UteisJSF.internacionalizar("prt_Letra_Esp22"), "u");// acento
																						// Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â¹
		texto = texto.replaceAll(UteisJSF.internacionalizar("prt_Letra_Esp23"), "u");// acento
																						// Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â»
		texto = texto.replaceAll(UteisJSF.internacionalizar("prt_Letra_Esp24"), "c");// acento
																						// Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â§
		texto = texto.replaceAll(UteisJSF.internacionalizar("prt_Letra_Esp25"), "C");// acento
																						// Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â§
		texto = texto.replaceAll(UteisJSF.internacionalizar("prt_Letra_Esp26"), "c");// acento
																						// Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â§
		texto = texto.replaceAll(UteisJSF.internacionalizar("prt_Letra_Esp27"), "");
		texto = texto.replaceAll("" + Uteis.A_AGUDO, "a");
		texto = texto.replaceAll("" + Uteis.A_AGUDOMAIUSCULO, "A");
		texto = texto.replaceAll("" + Uteis.A_CIRCUNFLEXO, "a");
		texto = texto.replaceAll("" + Uteis.A_CRASE, "a");
		texto = texto.replaceAll("" + Uteis.A_TIO, "a");
		texto = texto.replaceAll("" + Uteis.C_CEDILHA, "c");
		texto = texto.replaceAll("" + Uteis.E_AGUDO, "e");
		texto = texto.replaceAll("" + Uteis.E_CIRCUNFLEXO, "e");
		texto = texto.replaceAll("" + Uteis.I_AGUDO, "i");
		texto = texto.replaceAll("" + Uteis.O_AGUDO, "o");
		texto = texto.replaceAll("" + Uteis.O_CRASE, "o");
		texto = texto.replaceAll("" + Uteis.O_TIO, "o");
		texto = texto.replaceAll("" + Uteis.U_AGUDO, "u");
		texto = texto.replaceAll("" + Uteis.U_TREMA, "u");
		// texto =
		// texto.replaceAll(UteisJSF.internacionalizar("prt_Letra_Esp27"),
		// "c");//acento
		// Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â§
		// texto =
		// texto.replaceAll(UteisJSF.internacionalizar("prt_Letra_Esp28"),
		// "C");//acento
		// Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â§
		// texto =
		// texto.replaceAll(UteisJSF.internacionalizar("prt_Letra_Esp29"),
		// "a");//acento
		// Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â§
		// texto =
		// texto.replaceAll(UteisJSF.internacionalizar("prt_Letra_Esp30"),
		// "C");//acento
		// Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â§
		// texto =
		// texto.replaceAll(UteisJSF.internacionalizar("prt_Letra_Esp31"),
		// "O");//acento
		// Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â§

		return texto;
	}

	public static String getFormatoHoraMinuto(String valor) {
		if (valor.length() > 5) {
			return "";
		} else {
			if (valor.length() == 1) {
				return "0" + valor + ":00";
			} else if (valor.length() == 2) {
				return valor + ":00";
			} else if (valor.length() == 3) {
				return valor + "00";
			} else if (valor.length() == 4) {
				return valor + "0";
			} else {
				return valor;
			}
		}
	}

	public static String getCalculodeHora(String tempo, String tempo1, String tempo2, Integer nrAulas, Integer duracao) {
		Double resultado;
		resultado = (double) duracao * nrAulas;
		int res1 = (int) (resultado / 60);
		double res2 = ((resultado % 60));

		String hora = tempo.substring(0, 2);
		String minuto = tempo.substring(3, 5);
		Integer hora1 = Integer.parseInt(hora);
		Integer minuto1 = Integer.parseInt(minuto);

		String intervalohora = tempo1.substring(0, 2);
		String intervalominuto = tempo1.substring(3, 5);
		Integer intervalohora1 = Integer.parseInt(intervalohora);
		Integer intervalominuto1 = Integer.parseInt(intervalominuto);

		String intervalofinalhora = tempo2.substring(0, 2);
		String intervalofinalminuto = tempo2.substring(3, 5);
		Integer intervalofinalhora1 = Integer.parseInt(intervalofinalhora);
		Integer intervalofinalminuto1 = Integer.parseInt(intervalofinalminuto);

		intervalohora1 = intervalofinalhora1 - intervalohora1;
		intervalominuto1 = intervalofinalminuto1 - intervalominuto1;
		hora1 = hora1 + res1 + intervalohora1;
		minuto1 = (int) (minuto1 + res2 + intervalominuto1);

		if (hora1 > 23) {
			hora1 = hora1 - 23;
			hora1 = hora1 - 1;
		}
		if (minuto1 > 59) {
			minuto1 = minuto1 - 60;
			hora1 = hora1 + 1;
			minuto1 = minuto1 + 0;
		}

		if (minuto1.intValue() == 0) {
			tempo = String.valueOf(hora1) + ":00";

		} else if (minuto1.intValue() == 1) {
			tempo = String.valueOf(hora1) + ":01";

		} else if (minuto1.intValue() == 2) {
			tempo = String.valueOf(hora1) + ":02";

		} else if (minuto1.intValue() == 3) {
			tempo = String.valueOf(hora1) + ":03";

		} else if (minuto1.intValue() == 4) {
			tempo = String.valueOf(hora1) + ":04";

		} else if (minuto1.intValue() == 5) {
			tempo = String.valueOf(hora1) + ":05";

		} else if (minuto1.intValue() == 6) {
			tempo = String.valueOf(hora1) + ":06";

		} else if (minuto1.intValue() == 7) {
			tempo = String.valueOf(hora1) + ":07";

		} else if (minuto1.intValue() == 8) {
			tempo = String.valueOf(hora1) + ":08";

		} else if (minuto1.intValue() == 9) {
			tempo = String.valueOf(hora1) + ":09";

		} else {
			tempo = String.valueOf(hora1) + ":" + String.valueOf(minuto1);
		}

		return tempo;

	}

	public static String getCalculodeHoraSemIntervalo(String tempo, Integer nrAulas, Integer duracao) {
		Double resultado;
		resultado = (double) duracao * nrAulas;
		int res1 = (int) (resultado / 60);
		double res2 = ((resultado % 60));

		String hora = tempo.substring(0, 2);
		String minuto = tempo.substring(3, 5);
		Integer hora1 = Integer.parseInt(hora);
		Integer minuto1 = Integer.parseInt(minuto);

		hora1 = hora1 + res1;
		minuto1 = (int) (minuto1 + res2);

		if (hora1 > 23) {
			hora1 = hora1 - 23;
			hora1 = hora1 - 1;
		}
		if (minuto1 > 59) {
			minuto1 = minuto1 - 60;
			hora1 = hora1 + 1;
			minuto1 = minuto1 + 0;
		}

		if (minuto1 >= 0 && minuto1 <= 9) {
			minuto = "0" + String.valueOf(minuto1);
		} else {
			minuto = String.valueOf(minuto1);
		}

		if (hora1 >= 0 && hora1 <= 9) {
			hora = "0" + String.valueOf(hora1);
		} else {
			hora = String.valueOf(hora1);
		}

		tempo = hora + ":" + minuto;
		return tempo;

	}

	public static double arredondar(double valor, int casas, int abaixo) {
		valor = (new BigDecimal(valor).setScale(casas, BigDecimal.ROUND_HALF_UP)).doubleValue();
		return valor;
		/*
		 * double arredondado = valor; arredondado *= (Math.pow(10, casas)); if (abaixo == 0) { arredondado = Math.ceil(arredondado); } else { arredondado = Math.floor(arredondado); } arredondado /= (Math.pow(10, casas)); return arredondado;
		 */
	}

	public static double arredondarAbaixo(double valor, int casas, int abaixo) {
		valor = (new BigDecimal(valor).setScale(casas, BigDecimal.ROUND_DOWN)).doubleValue();
		return valor;
		/*
		 * double arredondado = valor; arredondado *= (Math.pow(10, casas)); if (abaixo == 0) { arredondado = Math.ceil(arredondado); } else { arredondado = Math.floor(arredondado); } arredondado /= (Math.pow(10, casas)); return arredondado;
		 */
	}

	public static double arrendondarForcando2CadasDecimais(double valor) {
		boolean negativo = false;
		if (valor < 0) {
			negativo = true;
		}
		valor = Uteis.arredondar(valor, 2, 1);
		String valorStr = String.valueOf(valor);

		String inteira = valorStr.substring(0, valorStr.indexOf("."));
		String extensao = valorStr.substring(valorStr.indexOf(".") + 1, valorStr.length());
		if (extensao.length() == 1) {
			extensao += "0";
		}
		valorStr = Uteis.removerMascara(inteira) + "." + extensao;
		Double valorFinal = Double.parseDouble(valorStr);
		if (negativo) {
			valorFinal = valorFinal * -1;
		}
		return valorFinal;
	}

	public static String arrendondarForcando2CadasDecimaisStr(double valor) {
		return arrendondarForcando2CadasDecimaisStrComSepador(valor, ".");
	}

	public static String arrendondarForcando2CadasDecimaisStrComSepador(double valor, String sepador) {
		boolean negativo = false;
		if (valor < 0) {
			negativo = true;
		}
		if (negativo) {
			valor = valor * -1;
		}
		valor = Uteis.arredondar(valor, 2, 1);
		String valorStr = String.valueOf(valor);

		String inteira = valorStr.substring(0, valorStr.indexOf("."));
		String extensao = valorStr.substring(valorStr.indexOf(".") + 1, valorStr.length());
		if (extensao.length() == 1) {
			extensao += "0";
		}
		valorStr = Uteis.removerMascara(inteira) + sepador + extensao;
		return valorStr;
	}

	public static String formatarDoubleParaMoeda(double num) {
		NumberFormat number = NumberFormat.getCurrencyInstance();
		return number.format(num);
	}

	public static java.sql.Timestamp obterDataComHora(Date data, String horaMinutoInicial) {
		String hora = horaMinutoInicial.substring(0, horaMinutoInicial.indexOf(":"));
		String minuto = horaMinutoInicial.substring(horaMinutoInicial.indexOf(":") + 1);
		Calendar cal = Calendar.getInstance();
		cal.setTime(data);
		cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hora));
		cal.set(Calendar.MINUTE, Integer.parseInt(minuto));
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		// cal.add(Calendar.MINUTE, minuto);

		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		return new java.sql.Timestamp(cal.getTime().getTime());
	}

	public static Date obterDataFuturaMesContabilConsiderandoDiaInicial(Date dataInicial, int nrTotalDiasProgredir) throws ParseException {
		if (dataInicial == null) {
			return null;
		}
		int nrMesesProgredir = nrTotalDiasProgredir / 30;
		int nrDiasProgredir = nrTotalDiasProgredir - (30 * nrMesesProgredir);

		int dia = Uteis.getDiaMesData(dataInicial);
		int mes = Uteis.getMesData(dataInicial);
		int ano = Uteis.getAnoData(dataInicial);

		// PROGREDINDO OS ANOS
		if (nrMesesProgredir > 12) {
			while (nrMesesProgredir > 12) {
				ano++;
				nrMesesProgredir += -12;
			}
		}

		// PROGREDINDO OS MESES
		mes += nrMesesProgredir;
		if (mes > 12) {
			mes -= 12;
			ano++;
		}

		// PROGREDINDO OS DIAS;
		boolean incrementarMes = false;
		if ((dia + nrDiasProgredir) > 30) {
			dia = (dia + nrDiasProgredir) - 30;
			incrementarMes = true;
		} else {
			dia = dia + nrDiasProgredir;
		}

		if (incrementarMes) {
			mes++;
			if (mes == 13) {
				mes = 1;
				ano++;
			}
		}
		dia--;
		Date dataFutura = Uteis.getDate(dia + "/" + mes + "/" + ano);
		return dataFutura;
	}

	public static long nrDiasEntreDatas(Date dataInicial, Date dataFinal) {
		long dias = 0;
		if (dataInicial != null && dataFinal != null) {
			dias = (dataInicial.getTime() - dataFinal.getTime()) / (1000 * 60 * 60 * 24);
		}
		return dias;
	}

	public static Date obterDataPassada(Date dataInicial, long nrDiasRegredir) {
		long dataEmDias = dataInicial.getTime() / (1000 * 60 * 60 * 24);
		dataEmDias = dataEmDias - nrDiasRegredir;
		Date dataFutura = new Date(dataEmDias * (1000 * 60 * 60 * 24));
		return dataFutura;
	}

	public static Date obterDataAvancadaPorDiaPorMesPorAno(Integer dia, Integer mes, Integer ano, Integer nrMesRegredir) {
		Calendar calendar = Calendar.getInstance();
		try {
			calendar.set(Calendar.DAY_OF_MONTH, dia);
			calendar.set(Calendar.MONTH, (mes - 1));
			calendar.set(Calendar.YEAR, ano);
			calendar.add(Calendar.DAY_OF_MONTH, nrMesRegredir);
			return calendar.getTime();
		} finally {
			calendar = null;
		}
	}

	/**
	 * MÃ?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬ Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â? ?Ã?Â¢Ã¢â ?Â¬Ã?Â Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å Ã?Â¬Ã?Â¢Ã¢â? Å¾Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â? Å¡Ã?Â¬Ã?â??Ã?Â Ã?Æ?Ã?â??Ã?â?? Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â ?Â¬Ã?Â¡Ã?â??Ã?Â Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â Ã?Â¾Ã?â??Ã?Â¢Ã?Æ?Ã â??Ã?â? Ã¢â?¬ â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â ?Â¬Ã¢â??Â¢Ã?Æ?Ã â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â ?Â¬Ã Â¡Ã?â??Ã?Â¬Ã?Æ?Ã¢â?¬Â¦Ã?â??Ã?Â Ã?Æ?Ã?â??Ã?â? Ã¢â?¬ â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â? Å¡Ã?Â¬Ã?â?¦Ã?Â Ã?Æ?Ã?â??Ã?Â¢Ã¢â?
	 * Â¬Ã?Â¡Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â©todo que recebe uma data base e calcula a data final com base em uma quantidade de dias a regredir.
	 *
	 * @param dataBase
	 * @param nrDiasRegredir
	 * @return
	 */
	public static Date obterDataAntigaPorMes(Date dataBase, Integer nrMesRegredir) {
		Calendar calendar = new GregorianCalendar();
		try {
			nrMesRegredir = (-1) * nrMesRegredir;
			calendar.setTime(dataBase);
			calendar.add(Calendar.MONTH, nrMesRegredir);
			return calendar.getTime();
		} finally {
			calendar = null;
		}
	}

	public static Date obterDataAvancadaPorMes(Date dataBase, Integer nrMesRegredir) {
		Calendar calendar = new GregorianCalendar();
		try {
			calendar.setTime(dataBase);
			calendar.add(Calendar.MONTH, nrMesRegredir);
			return calendar.getTime();
		} finally {
			calendar = null;
		}
	}

	/**
	 * MÃ?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬ Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â? ?Ã?Â¢Ã¢â ?Â¬Ã?Â Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å Ã?Â¬Ã?Â¢Ã¢â? Å¾Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â? Å¡Ã?Â¬Ã?â??Ã?Â Ã?Æ?Ã?â??Ã?â?? Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â ?Â¬Ã?Â¡Ã?â??Ã?Â Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â Ã?Â¾Ã?â??Ã?Â¢Ã?Æ?Ã â??Ã?â? Ã¢â?¬ â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â ?Â¬Ã¢â??Â¢Ã?Æ?Ã â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â ?Â¬Ã Â¡Ã?â??Ã?Â¬Ã?Æ?Ã¢â?¬Â¦Ã?â??Ã?Â Ã?Æ?Ã?â??Ã?â? Ã¢â?¬ â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â? Å¡Ã?Â¬Ã?â?¦Ã?Â Ã?Æ?Ã?â??Ã?Â¢Ã¢â?
	 * Â¬Ã?Â¡Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â©todo que recebe uma data base e calcula a data final com base em uma quantidade de dias a regredir.
	 *
	 * @param dataBase
	 * @param nrDiasRegredir
	 * @return
	 */
	public static Date obterDataAntiga(Date dataBase, Integer nrDiasRegredir) {
		Calendar calendar = new GregorianCalendar();
		try {
			nrDiasRegredir = (-1) * nrDiasRegredir;
			calendar.setTime(dataBase);
			calendar.add(Calendar.DAY_OF_MONTH, nrDiasRegredir);
			return calendar.getTime();
		} finally {
			calendar = null;
		}
	}

	public static String realizarSomahora(String minuto) {
		Calendar calendar = new GregorianCalendar();
		Date data = new Date();
		try {
			// calendar.setTime(dataBase);
			// calendar.add(Calendar.DAY_OF_MONTH, nrDiasProgredir);
			// return calendar.getTime();
		} finally {
			calendar = null;
		}
		return "";
	}

	/**
	 * MÃ?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬ Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â? ?Ã?Â¢Ã¢â ?Â¬Ã?Â Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å Ã?Â¬Ã?Â¢Ã¢â? Å¾Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â? Å¡Ã?Â¬Ã?â??Ã?Â Ã?Æ?Ã?â??Ã?â?? Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â ?Â¬Ã?Â¡Ã?â??Ã?Â Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â Ã?Â¾Ã?â??Ã?Â¢Ã?Æ?Ã â??Ã?â? Ã¢â?¬ â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â ?Â¬Ã¢â??Â¢Ã?Æ?Ã â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â ?Â¬Ã Â¡Ã?â??Ã?Â¬Ã?Æ?Ã¢â?¬Â¦Ã?â??Ã?Â Ã?Æ?Ã?â??Ã?â? Ã¢â?¬ â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â? Å¡Ã?Â¬Ã?â?¦Ã?Â Ã?Æ?Ã?â??Ã?Â¢Ã¢â?
	 * Â¬Ã?Â¡Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â©todo que recebe uma data base e calcula a data final com base em uma quantidade de dias a regredir.
	 *
	 * @param dataBase
	 * @param nrDiasRegredir
	 * @return
	 */
	public static String obterHoraAvancada(String horaInicial, Integer minutos) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(horaInicial.substring(0, 2)));
		cal.set(Calendar.MINUTE, Integer.parseInt(horaInicial.substring(3, 5)));
		cal.set(Calendar.SECOND, 0);
		cal.add(Calendar.MINUTE, minutos);

		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		return sdf.format(cal.getTime()).toString();
	}

	public static String obterHoraRegredida(String horaInicial, Integer minutos) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(horaInicial.substring(0, 2)));
		cal.set(Calendar.MINUTE, Integer.parseInt(horaInicial.substring(3, 5)));
		cal.set(Calendar.SECOND, 0);
		cal.add(Calendar.MINUTE, -minutos);

		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		return sdf.format(cal.getTime()).toString();
	}

	public static Date obterDataAvancada(Date dataBase, Integer nrDiasProgredir) {
		Calendar calendar = new GregorianCalendar();
		try {
			calendar.setTime(dataBase);
			calendar.add(Calendar.DAY_OF_MONTH, nrDiasProgredir);
			return calendar.getTime();
		} finally {
			calendar = null;
		}
	}

	public static Date obterDataFutura(Date dataInicial, long nrDiasProgredir) {
		long dataEmDias = dataInicial.getTime() / (1000 * 60 * 60 * 24);
		dataEmDias = dataEmDias + nrDiasProgredir;
		Date dataFutura = new Date(dataEmDias * (1000 * 60 * 60 * 24));
		return dataFutura;
	}

	public static String removerEspacosFinalString(String str) {
		if (str == null) {
			return null;
		}
		if (str.equalsIgnoreCase("")) {
			return str;
		}
		String ultimoChar = str.substring(str.length() - 1);
		while ((ultimoChar.equals(" ")) && (str.length() > 0)) {
			str = str.substring(0, str.length() - 1);
			if (str.length() > 0) {
				ultimoChar = str.substring(str.length() - 1);
			}
		}
		return str;
	}

	public static String getMesReferenciaData(Date dataPrm) {
		Calendar dataCalendar = Calendar.getInstance();
		dataCalendar.setTime(dataPrm);

		int ano = dataCalendar.get(Calendar.YEAR);
		int mes = dataCalendar.get(Calendar.MONTH) + 1;

		String mesStr = String.valueOf(mes);
		if (mesStr.length() == 1) {
			mesStr = "0" + mesStr;
		}
		String mesRef = mesStr + "/" + ano;
		return mesRef;
	}

	public static String getMesReferencia(int mes, int ano) {
		String mesStr = String.valueOf(mes);
		if (mesStr.length() == 1) {
			mesStr = "0" + mesStr;
		}
		mesStr = mesStr + "/" + String.valueOf(ano);
		return mesStr;
	}

	public static String getMesReferenciaExtenso(String mes) {
		if (mes.equals("01") || mes.equals("1")) {
			return "Janeiro";
		}
		if (mes.equals("02") || mes.equals("2")) {
			return "Fevereiro";
		}
		if (mes.equals("03") || mes.equals("3")) {
			return "Maro";
		}
		if (mes.equals("04") || mes.equals("4")) {
			return "Abril";
		}
		if (mes.equals("05") || mes.equals("5")) {
			return "Maio";
		}
		if (mes.equals("06") || mes.equals("6")) {
			return "Junho";
		}
		if (mes.equals("07") || mes.equals("7")) {
			return "Julho";
		}
		if (mes.equals("08") || mes.equals("8")) {
			return "Agosto";
		}
		if (mes.equals("09") || mes.equals("9")) {
			return "Setembro";
		}
		if (mes.equals("10")) {
			return "Outubro";
		}
		if (mes.equals("11")) {
			return "Novembro";
		}
		if (mes.equals("12")) {
			return "Dezembro";
		}
		return "";
	}

	public static String getMesReferenciaAbreviadoExtenso(Integer mes) {
		if (mes.equals(1)) {
			return "Jan";
		} else if (mes.equals(2)) {
			return "Fev";
		} else if (mes.equals(3)) {
			return "Mar";
		} else if (mes.equals(4)) {
			return "Abr";
		} else if (mes.equals(5)) {
			return "Mai";
		} else if (mes.equals(6)) {
			return "Jun";
		} else if (mes.equals(7)) {
			return "Jul";
		} else if (mes.equals(8)) {
			return "Ago";
		} else if (mes.equals(9)) {
			return "Set";
		} else if (mes.equals(10)) {
			return "Out";
		} else if (mes.equals(11)) {
			return "Nov";
		} else if (mes.equals(12)) {
			return "Dez";
		}
		return "";
	}

	public static Integer getMesReferencia(String mes) {
		if (mes.equals("JANEIRO")) {
			return 1;
		}
		if (mes.equals("FEVEREIRO")) {
			return 2;
		}
		if (mes.equals("MARO") || mes.equals("MARCO")) {
			return 3;
		}
		if (mes.equals("ABRIL")) {
			return 4;
		}
		if (mes.equals("MAIO")) {
			return 5;
		}
		if (mes.equals("JUNHO")) {
			return 6;
		}
		if (mes.equals("JULHO")) {
			return 7;
		}
		if (mes.equals("AGOSTO")) {
			return 8;
		}
		if (mes.equals("SETEMBRO")) {
			return 9;
		}
		if (mes.equals("OUTUBRO")) {
			return 10;
		}
		if (mes.equals("NOVEMBRO")) {
			return 11;
		}
		if (mes.equals("DEZEMBRO")) {
			return 12;
		}
		return 0;
	}

	public static Integer getMesConcatenadoReferencia(String mes) {
		if (mes.equals("JAN")) {
			return 1;
		}
		if (mes.equals("FEV")) {
			return 2;
		}
		if (mes.equals("MAR")) {
			return 3;
		}
		if (mes.equals("ABR")) {
			return 4;
		}
		if (mes.equals("MAI")) {
			return 5;
		}
		if (mes.equals("JUN")) {
			return 6;
		}
		if (mes.equals("JUL")) {
			return 7;
		}
		if (mes.equals("AGO")) {
			return 8;
		}
		if (mes.equals("SET")) {
			return 9;
		}
		if (mes.equals("OUT")) {
			return 10;
		}
		if (mes.equals("NOV")) {
			return 11;
		}
		if (mes.equals("DEZ")) {
			return 12;
		}
		return 0;
	}

	public static Integer getAnoPlanoOrcamentario(String mesAno) {
		int ano = Integer.parseInt(mesAno.substring(mesAno.lastIndexOf("- ") + 2));
		return ano;
	}

	public static int compareMesReferencia(String mesInicial, String mesFinal) {
		String mesInicialOrdenado = mesInicial.substring(mesInicial.indexOf("/") + 1) + mesInicial.substring(0, mesInicial.indexOf("/"));
		String mesFinalOrdenado = mesFinal.substring(mesFinal.indexOf("/") + 1) + mesFinal.substring(0, mesFinal.indexOf("/"));
		return mesInicialOrdenado.compareTo(mesFinalOrdenado);
	}

	public static String getDataDiaMesAnoConcatenado() {
		String dataAtual = Uteis.getDataAtual();
		String ano = "";
		String mes = "";
		String dia = "";
		int cont = 1;
		while (cont != 3) {
			int posicao = dataAtual.lastIndexOf("/");
			if (posicao != -1) {
				cont++;
				if (cont == 2) {
					ano = dataAtual.substring(posicao + 1);
					dataAtual = dataAtual.substring(0, posicao);
				} else if (cont == 3) {
					mes = dataAtual.substring(posicao + 1);
					dia = dataAtual.substring(0, posicao);
				}
			}
		}
		return dia + mes + ano;
	}

	public static String getDiaMesPorExtensoEAno(Date data, Boolean mesMinusculo) {
		String dataExt = "";
		if (data != null) {
			if (mesMinusculo == null || !mesMinusculo) {
				dataExt += getData(data, "dd 'de' MMMMM 'de' yyyy");
			} else {
				dataExt += (getData(data, "dd 'de' MMMMM 'de' yyyy")).toLowerCase();
			}
		}
		return dataExt;
	}

	public static String getDiaPorExtensoMesPorExtensoEAnoPorExtenso(Date data, Boolean mesMinusculo) {
		String dataExt = "";
		if (data != null) {
			return getDataCidadeDiaPorExtensoMesPorExtensoEAnoPorExtenso("", "", data, mesMinusculo);
		}
		return dataExt;
	}

	public static String getDataCidadeDiaPorExtensoMesPorExtensoEAnoPorExtenso(String cidade, Date data, Boolean mesMinusculo) {
		return getDataCidadeDiaPorExtensoMesPorExtensoEAnoPorExtenso(cidade, "", data, mesMinusculo);
	}

	public static String getDataCidadeDiaMesPorExtensoEAno(String cidade, Date data, Boolean mesMinusculo) {
		return getDataCidadeDiaMesPorExtensoEAno(cidade, "", data, mesMinusculo);
	}

	public static String getDataCidadeDiaPorExtensoMesPorExtensoEAnoPorExtenso(String cidade, String estado, Date data, Boolean mesMinusculo) {
		String dataExt = "";
		if (data != null) {
			if (!cidade.equals("")) {
				dataExt += cidade + ", ";
			}
			if (!estado.equals("")) {
				dataExt += estado + ", ";
			}
			int dia = getDiaMesData(data);
//			if (dia > 1) {
//				dataExt += "aos ";
//			} else {
//				dataExt += "ao ";
//			}
			Extenso ext = new Extenso();
			ext.setNumber(dia);
			dataExt += ext.toStringNaoMonetario();
			if (dia > 1) {
				dataExt += " dias";
			} else {
				dataExt += " dia";
			}
			dataExt += " do ms de ";
			Integer mes = getMesData(data);
			String mesExtenso = getMesReferenciaExtenso(mes.toString());
			dataExt += mesExtenso.toLowerCase();
			dataExt += " de ";
			int ano = getAnoData(data);
			ext.setNumber(ano);
			dataExt += ext.toStringNaoMonetario();
		}
		return dataExt;
	}

	public static String getDataCidadeDiaMesPorExtensoEAno(String cidade, String estado, Date data, Boolean mesMinusculo) {
		String dataExt = "";
		if (data != null) {
			if (!cidade.equals("")) {
				dataExt += cidade + ", ";
			}
			if (!estado.equals("")) {
				dataExt += estado + ", ";
			}
			if (mesMinusculo == null || !mesMinusculo) {
				dataExt += getData(data, "dd 'de' MMMMM 'de' yyyy");
			} else {
				dataExt += (getData(data, "dd 'de' MMMMM 'de' yyyy")).toLowerCase();
			}
		}
		return dataExt;
	}

	public static String getDataCidadeEstadoDiaMesPorExtensoEAno(String cidade, String estadoSigla, Date data, Boolean mesMinusculo) {
		String dataExt = "";
		if (data != null) {
			if (!cidade.equals("")) {
				dataExt += cidade + "-" + estadoSigla + ", ";
			}
			if (mesMinusculo == null || !mesMinusculo) {
				dataExt += getData(data, "dd 'de' MMMMM 'de' yyyy");
			} else {
				dataExt += (getData(data, "dd 'de' MMMMM 'de' yyyy")).toLowerCase();
			}
		}
		return dataExt;
	}

	public static String getDataMesAnoConcatenado() {
		// return MM/AAAA
		int mesAtual = Calendar.getInstance().get(Calendar.MONTH) + 1;
		int anoAtual = Calendar.getInstance().get(Calendar.YEAR);
		String mesAtualStr = String.valueOf(mesAtual);
		if (mesAtualStr.length() == 1) {
			mesAtualStr = "0" + mesAtualStr;
		}
		return mesAtualStr + "/" + anoAtual;
		/*
		 * String dataAtual = Uteis.getDataAtual(); String ano = ""; String mes = ""; int cont = 1; while (cont != 3) { int posicao = dataAtual.lastIndexOf("/"); if (posicao != -1) { cont++; if (cont == 2) { ano = dataAtual.substring(posicao + 1); dataAtual = dataAtual.substring(0, posicao); } else if (cont == 3) { mes = dataAtual.substring(posicao + 1); } } } return (mes + "/" + ano);
		 */
	}

	public static String getDataMesAnoConcatenado(Date dataPrm) {
		// return MM/AAAA
		Calendar dataCalendar = Calendar.getInstance();
		dataCalendar.setTime(dataPrm);
		int mesAtual = dataCalendar.get(Calendar.MONTH) + 1;
		int anoAtual = dataCalendar.get(Calendar.YEAR);
		String mesAtualStr = String.valueOf(mesAtual);
		if (mesAtualStr.length() == 1) {
			mesAtualStr = "0" + mesAtualStr;
		}
		return mesAtualStr + "/" + anoAtual;

	}

	public static String removerMascara(String campo) {
		String campoSemMascara = "";
		for (int i = 0; i < campo.length(); i++) {
			if ((campo.charAt(i) != ',') && (campo.charAt(i) != '.') && (campo.charAt(i) != '-') && (campo.charAt(i) != ':') && (campo.charAt(i) != '/')) {
				campoSemMascara = campoSemMascara + campo.substring(i, i + 1);
			}
		}
		return campoSemMascara;
	}

	public static String aplicarMascara(String dado, String mascara) {
		if (dado == null) {
			return dado;
		}
		if (dado.equals("")) {
			return dado;
		}
		if (dado.length() == mascara.length()) {
			return dado;
		}
		dado = removerMascara(dado);
		int posDado = 0;
		String dadoComMascara = "";
		for (int i = 0; i < mascara.length(); i++) {
			if (posDado >= dado.length()) {
				break;
			}
			String caracter = mascara.substring(i, i + 1);
			if (caracter.equals("9")) {
				dadoComMascara = dadoComMascara + dado.substring(posDado, posDado + 1);
				posDado++;
			} else {
				dadoComMascara = dadoComMascara + caracter;
			}
		}
		return dadoComMascara;
	}

	public static String getDoubleFormatado(Double valor) {
		if (valor == null) {
			return "";
		}
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumFractionDigits(2);
		return nf.format(valor);
	}

	public static int getDiaMesData(Date dataPrm) {
		Calendar dataCalendar = Calendar.getInstance();
		dataCalendar.setTime(dataPrm);

		int dia = dataCalendar.get(Calendar.DAY_OF_MONTH);
		return dia;
	}

	public static int getMesData(Date dataPrm) {
		Calendar dataCalendar = Calendar.getInstance();
		dataCalendar.setTime(dataPrm);

		int mes = dataCalendar.get(Calendar.MONTH) + 1;
		return mes;
	}

	public static int getMesDataVencimento(Date dataPrm) {
		Calendar dataCalendar = Calendar.getInstance();
		dataCalendar.setTime(dataPrm);

		dataCalendar.add(Calendar.MONTH, 1);
		int mes = dataCalendar.get(Calendar.MONTH) + 1;
		return mes;
	}

	public static boolean isWorkingWeekDay(Date date) {
		int dayOfWeek = date.getDay();
		switch (dayOfWeek) {
		case 0: // SUNDAY
		case 6: // SATURDAY
			return false;
		}

		return true;
	}

	public static boolean isDiaDaSemana(Date date) {
		int dayOfWeek = date.getDay();
		switch (dayOfWeek) {
		case 0: // SUNDAY
		case 6: // SATURDAY
			return false;
		}

		return true;
	}

	public static int getAnoData(Date dataPrm) {
		Calendar dataCalendar = Calendar.getInstance();
		dataCalendar.setTime(dataPrm);

		int ano = dataCalendar.get(Calendar.YEAR);
		return ano;
	}

	public static String getValorMonetarioParaIntegracao_SemPontoNemVirgula(double valor) {
		String valorStr = String.valueOf(valor);

		String inteira = valorStr.substring(0, valorStr.indexOf("."));
		String extensao = valorStr.substring(valorStr.indexOf(".") + 1, valorStr.length());
		if (extensao.length() == 1) {
			extensao += "0";
		}
		valorStr = Uteis.removerMascara(inteira + extensao);
		return valorStr;
	}

	public static String getDiaSemanaData(Date data) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(data);
		int diaSemana = cal.get(Calendar.DAY_OF_WEEK);
		if (diaSemana < 10) {
			return "0" + diaSemana;
		}
		return "" + diaSemana;
	}

	public static DiaSemana getDiaSemanaProximoDia(DiaSemana diaSemana) {
		switch (diaSemana) {
		case DOMINGO:
			return diaSemana.SEGUNGA;
		case SEGUNGA:
			return DiaSemana.TERCA;
		case TERCA:
			return DiaSemana.QUARTA;
		case QUARTA:
			return DiaSemana.QUINTA;
		case QUINTA:
			return DiaSemana.SEXTA;
		case SEXTA:
			return DiaSemana.SABADO;
		case SABADO:
			return DiaSemana.DOMINGO;
		default:
			return DiaSemana.NENHUM;
		}
	}

	public static DiaSemana getDiaSemana(Integer numeroDia) {
		switch (numeroDia) {
		case 0:
			return DiaSemana.DOMINGO;
		case 1:
			return DiaSemana.SEGUNGA;
		case 2:
			return DiaSemana.TERCA;
		case 3:
			return DiaSemana.QUARTA;
		case 4:
			return DiaSemana.QUINTA;
		case 5:
			return DiaSemana.SEXTA;
		case 6:
			return DiaSemana.SABADO;
		default:
			return DiaSemana.NENHUM;
		}
	}

	public static int getDiaSemana(Date data) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(data);
		int diaSemana = cal.get(Calendar.DAY_OF_WEEK);
		return diaSemana;
	}

	public static int getDia(Date data) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(data);
		int diaSemana = cal.get(Calendar.DAY_OF_MONTH);
		return diaSemana;
	}

	public static DiaSemana getDiaSemanaEnum(Date data) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(data);
		int diaSemana = cal.get(Calendar.DAY_OF_WEEK);
		return DiaSemana.getEnum("0" + diaSemana);
	}

	public static double arredondarDecimal(double number, int deep) {
		double multiplier = Math.pow(10, (double) deep);
		return (double) (Math.round(number * multiplier)) / multiplier;
	}

	public static int getMesDataAtual() {
		Locale aLocale = new Locale("pt", "BR");
		Date hoje;
		String hojeStr;
		DateFormat formatador;
		formatador = DateFormat.getDateInstance(DateFormat.SHORT, aLocale);
		hoje = new Date();
		hojeStr = formatador.format(hoje);
		hojeStr = hojeStr.substring(hojeStr.indexOf("/") + 1, hojeStr.lastIndexOf("/"));
		return (Integer.parseInt(hojeStr));
	}

	public static String getAnoDataAtual4Digitos() {
		GregorianCalendar gc = new GregorianCalendar();
		return String.valueOf(gc.get(GregorianCalendar.YEAR));
	}

	public static void liberarListaMemoria(List listaLiberar) {
		if (listaLiberar != null) {
			listaLiberar.clear();
			listaLiberar = null;
		}
	}

	public static String getDiaSemana_Apresentar() {
		try {
			GregorianCalendar gc = new GregorianCalendar();
			String diaSemana = gc.getDisplayName(GregorianCalendar.DAY_OF_WEEK, GregorianCalendar.LONG, new Locale("pt", "BR"));
			return diaSemana;
		} catch (Exception e) {
			return "";
		}
	}

	public static String getDiaSemana_Apresentar(Date data) {
		try {
			SimpleDateFormat df = new SimpleDateFormat("E", new Locale("pt", "BR"));
			return df.format(data);
		} catch (Exception e) {
			return "";
		}
	}

	public static String gerarSenhaRandomica(int quantidade, boolean somenteNumeros) {
		String senha = "";
		String[] caracteres = null;
		if (somenteNumeros) {
			caracteres = montarArray("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");
		} else {
			caracteres = montarArray("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z");
		}
		for (int x = 0; x < quantidade; x++) {
			int j = (int) (Math.random() * caracteres.length);
			senha += caracteres[j];
		}
		return senha;
	}

	public static <T> T[] montarArray(T... c) {
		return c;
	}

	public static boolean validarEnvioEmail(String hostNameConfiguracaoGeralSistema) throws Exception {
		if (!hostNameConfiguracaoGeralSistema.equals("")) {
			return realizarCapturaHostname().equals(hostNameConfiguracaoGeralSistema);
		}
		return false;
	}

	public static String realizarCapturaHostname() {
		InetAddress addr = null;
		try {
			addr = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
		}
		return addr.getHostName();
	}

	public static void enviarEmail(String emailDest, String nomeDest, String emailRemet, String nomeRemet, String assunto, String corpo, String smtpPadrao, final String loginServidorSmtp, final String senhaServidorSmtp) throws Exception {
		enviarEmail(emailDest, nomeDest, emailRemet, nomeRemet, assunto, corpo, smtpPadrao, loginServidorSmtp, senhaServidorSmtp, false, null, "", false);
	}

	public static void enviarEmail(String emailDest, String nomeDest, String emailRemet, String nomeRemet, String assunto, String corpo, String smtpPadrao, final String loginServidorSmtp, final String senhaServidorSmtp, Boolean anexarImagensPadrao, List<File> listaFileCorpoMensagem, String portaSmtpPadrao, Boolean servidorEmailUtilizaSSL) throws Exception {
		Session session = null;
		MimeMessage message = null;
		MimeMultipart multipart = null;
		BodyPart messageBodyPart = null;

		try {
			Properties props = System.getProperties();
			props.put("mail.transport.protocol", "smtp");
			props.put("mail.smtp.host", smtpPadrao);
			if (portaSmtpPadrao != null || !portaSmtpPadrao.equals("")) {
				props.put("mail.smtp.port", portaSmtpPadrao);
			}
			props.put("mail.smtp.auth", "true");
			if (servidorEmailUtilizaSSL) {
				final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
				props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
				props.setProperty("mail.smtp.socketFactory.port", portaSmtpPadrao);
				// Stop socket from falling back to insecure connection)
				props.setProperty("mail.smtp.socketFactory.fallback", "false");
			} else {
				props.remove("mail.smtp.socketFactory.class");
				props.remove("mail.smtp.socketFactory.port");
				props.remove("mail.smtp.socketFactory.fallback");
			}
			// props.put("mail.smtp.starttls.enable", "true");
			// props.put("mail.smtp.timeout", "30000");
			Authenticator auth = new Authenticator() {

				@Override
				public PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(loginServidorSmtp, senhaServidorSmtp);
				}
			};
			session = Session.getInstance(props, auth);
			session.setDebug(false);
			// Transport transport = session.getTransport("smtp");
			message = new MimeMessage(session);
			message.setSentDate(new Date());
			message.setFrom(new InternetAddress(emailRemet, nomeRemet));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailDest, nomeDest));
			message.setSubject(assunto);
			multipart = new MimeMultipart("related");
			messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(corpo, "text/html; charset=ISO-8859-1");
			multipart.addBodyPart(messageBodyPart);
			if (anexarImagensPadrao) {
				for (File imagem : listaFileCorpoMensagem) {
					messageBodyPart = new MimeBodyPart();
					DataSource fds = new FileDataSource(imagem);
					messageBodyPart.setDataHandler(new DataHandler(fds));
					messageBodyPart.setHeader("Content-ID", "<" + imagem.getName().substring(0, imagem.getName().lastIndexOf(".")) + ">");
					multipart.addBodyPart(messageBodyPart);
				}
			}
			message.setContent(multipart);
			message.saveChanges();
			// transport.connect(smtpPadrao, Integer.parseInt(portaSmtpPadrao),
			// loginServidorSmtp, senhaServidorSmtp);
			Transport.send(message);
			// transport.close();
		} catch (AuthenticationFailedException e) {
			throw new ConsistirException("As configuraes de email esto incorretas, entre em contato com o administrador.");
		} catch (Exception e) {
			// tratar
			if (e.getMessage().contains("Access to default session denied")) {
				throw new Exception("Ocorreu um erro durante o envio do e-mail. A permisso de acesso sesso de email foi negada.");
			}
			throw e;
		}
	}

	// public static void enviarEmail(ConfiguracaoGeralSistemaVO cs, String
	// titulo, String mensagem) throws Exception {
	// enviarEmail(cs.getDiretorAcademico().getEmailPrincipal(),
	// cs.getDiretorAcademico().getNome(), cs.getEmailPadrao(),
	// "SEI - SISTEMA EDUCACIONAL INTEGRADO", titulo, mensagem,
	// cs.getMailServer(), cs.getLogin(), cs.getSenha());
	// }
	public static void enviarEmailAnexo(String emailDest, String nomeDest, String emailRemet, String nomeRemet, String assunto, String corpo, String nomeFile, File arquivo, String smtpPadrao, final String loginServidorSmtp, final String senhaServidorSmtp) throws Exception {
		enviarEmailAnexo(emailDest, nomeDest, emailRemet, nomeRemet, assunto, corpo, nomeFile, arquivo, smtpPadrao, loginServidorSmtp, senhaServidorSmtp, false, null, null, false);
	}

	public static void enviarEmailAnexo(String emailDest, String nomeDest, String emailRemet, String nomeRemet, String assunto, String corpo, String nomeFile, File arquivo, String smtpPadrao, final String loginServidorSmtp, final String senhaServidorSmtp, Boolean anexarImagensPadrao, List<File> listaFileCorpoMensagem, String portaSmtpPadrao, Boolean servidorEmailUtilizaSSL) throws Exception {
		MimeBodyPart messageBodyPart = null;
		try {
			Properties props = System.getProperties();
			props.put("mail.transport.protocol", "smtp");
			props.put("mail.smtp.host", smtpPadrao);
			if (portaSmtpPadrao != null || !portaSmtpPadrao.equals("")) {
				props.put("mail.smtp.port", portaSmtpPadrao);
			}
			props.put("mail.smtp.auth", "true");
			if (servidorEmailUtilizaSSL) {
				final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
				props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
				props.setProperty("mail.smtp.socketFactory.port", portaSmtpPadrao);
				// Stop socket from falling back to insecure connection)
				props.setProperty("mail.smtp.socketFactory.fallback", "false");
			} else {
				props.remove("mail.smtp.socketFactory.class");
				props.remove("mail.smtp.socketFactory.port");
				props.remove("mail.smtp.socketFactory.fallback");
			}
			// props.put("mail.smtp.starttls.enable", "true");
			// props.put("mail.smtp.timeout", "30000");
			Authenticator auth = new Authenticator() {

				@Override
				public PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(loginServidorSmtp, senhaServidorSmtp);
				}
			};
			Session session = Session.getInstance(props, auth);
			session.setDebug(false);
			// Transport transport = session.getTransport("smtp");
			MimeMessage message = new MimeMessage(session);
			message.setSentDate(new Date());
			message.setFrom(new InternetAddress(emailRemet, nomeRemet));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailDest, nomeDest));
			message.setSubject(assunto);
			// message.setContent(corpo, "text/html");

			MimeMultipart mpRoot = new MimeMultipart("mixed");
			MimeMultipart mpContent = new MimeMultipart("alternative");
			MimeBodyPart contentPartRoot = new MimeBodyPart();
			contentPartRoot.setContent(mpContent);
			mpRoot.addBodyPart(contentPartRoot);

			// enviando html
			MimeBodyPart mbp1 = new MimeBodyPart();
			mbp1.setContent(corpo, "text/html;");
			mpContent.addBodyPart(mbp1);

			// enviando anexo
			MimeBodyPart mbp2 = new MimeBodyPart();
			DataSource fds = new FileDataSource(arquivo);
			mbp2.setDisposition(Part.ATTACHMENT);
			mbp2.setDataHandler(new DataHandler(fds));
			mbp2.setFileName(nomeFile);

			mpRoot.addBodyPart(mbp2);

			if (anexarImagensPadrao) {
				for (File imagem : listaFileCorpoMensagem) {
					messageBodyPart = new MimeBodyPart();
					DataSource fds2 = new FileDataSource(imagem);
					messageBodyPart.setDataHandler(new DataHandler(fds2));
					messageBodyPart.setHeader("Content-ID", "<" + imagem.getName().substring(0, imagem.getName().lastIndexOf(".")) + ">");
					mpRoot.addBodyPart(messageBodyPart);
				}
			}

			message.setContent(mpRoot);
			message.saveChanges();
			// transport.connect(smtpPadrao, Integer.parseInt(portaSmtpPadrao),
			// loginServidorSmtp, senhaServidorSmtp);
			Transport.send(message);
			// transport.close();
		} catch (Exception e) {
			// tratar
			e.getMessage().contains("Access to default session denied");
			throw new Exception("O e-mail do remetente ou do destinatario est invlido.");
		}
	}

	public static Integer getIntervaloEntreDatas(Date dataInicial, Date dataFinal) {
		long tempoInicial = dataInicial.getTime();
		long tempoFinal = dataFinal.getTime();
		long diferencaTempo = tempoFinal - tempoInicial;
		return (int) ((diferencaTempo) / (24L * 60 * 60 * 1000));
		// return (int) ((diferencaTempo + 60L * 60 * 1000) / (24L * 60 * 60 *
		// 1000));
		// return (int) diferencaTempo * 60 * 60 * 1000;

	}

	public static Integer contarQuantidadeDePontos(String frase, String sub) {
		int cont = 0;
		for (int i = 0; i < (frase.length() - sub.length() + 1); i++) {
			String res = frase.substring(i, (i + sub.length()));
			if (res.equals(sub)) {
				cont++;
			}
		}
		return cont;
	}

	public static String substituirPadraoString(String base, String padraoRemover, String padraoManter) {
		int pos = base.indexOf(padraoRemover);
		String tmp;
		while (pos != -1) {
			tmp = base.substring(0, pos);
			tmp += padraoManter;
			tmp += base.substring(pos + padraoRemover.length());
			base = tmp;
			pos = base.indexOf(padraoRemover);
		}
		return base;
	}

//	public static class OrdenaDiarioFrequenciaVOPorAluno implements Comparator<DiarioFrequenciaAulaVO> {
//
//		public int compare(DiarioFrequenciaAulaVO obj1, DiarioFrequenciaAulaVO obj2) {
//			Collator c = Collator.getInstance(new Locale("pt", "BR"));
//			try {
//				// return
//				// (obj1.getMatricula().getAluno().getNome().trim()).compareTo(obj2.getMatricula().getAluno().getNome().trim());
//				return c.compare(obj1.getMatricula().getAluno().getNome().trim(), obj2.getMatricula().getAluno().getNome().trim());
//			} finally {
//				c = null;
//			}
//		}
//	}
//
//	public static class OrdenaCronogramaAulaVOPorDataUnidade implements Comparator<CronogramaDeAulasRelVO> {
//
//		public int compare(CronogramaDeAulasRelVO obj1, CronogramaDeAulasRelVO obj2) {
//			Collator c = Collator.getInstance(new Locale("pt", "BR"));
//			try {
//				// return
//				// (obj1.getMatricula().getAluno().getNome().trim()).compareTo(obj2.getMatricula().getAluno().getNome().trim());
//				return c.compare(obj1.getDataUnidadeOrdenacao().trim(), obj2.getDataUnidadeOrdenacao().trim());
//			} finally {
//				c = null;
//			}
//		}
//	}

	public static class OrdenaHistoricoVOsPorCodigoConfiguracaoAcademico implements Comparator<HistoricoVO> {

		public int compare(HistoricoVO obj1, HistoricoVO obj2) {
			return (obj1.getConfiguracaoAcademico().getCodigo() + "-" + obj1.getDisciplina().getNome()).compareTo(obj2.getConfiguracaoAcademico().getCodigo() + "-" + obj2.getDisciplina().getNome());
			// return
			// (obj1.getConfiguracaoAcademico().getCodigo()).compareTo(obj2.getConfiguracaoAcademico().getCodigo());
		}
	}

	// public static String montarTagImageComFoto(PessoaVO pessoaVO) throws
	// Exception {
	// StringBuilder str = new
	// StringBuilder("<img style='width: 100px; height: 120px'
	// src='fotos/imagemPadrao.jpg'/>");
	// try {
	// if (pessoaVO != null && pessoaVO.getFoto() != null &&
	// pessoaVO.getFoto().length != 0) {
	// restaurarFoto(pessoaVO);
	// String foto = "" + pessoaVO.getCodigo() + "";
	// str = new StringBuilder("");
	// str.append("<img style='width: 100px; height: 120px' src='fotos/" + foto
	// + "'/>");
	// }
	// } catch (Exception e) {
	// throw e;
	// }
	// return str.toString();
	// }
	//
	// public static void restaurarFoto(PessoaVO pessoaVO) throws Exception {
	// ServletContext sc = (ServletContext)
	// FacesContext.getCurrentInstance().getExternalContext().getContext();
	// String caminho = sc.getRealPath("fotos") + File.separator;
	// File file = new File(caminho + pessoaVO.getCodigo() + " - " +
	// pessoaVO.getDescricaoFoto());
	//
	// if (!file.exists()) {
	// FileOutputStream fos = new FileOutputStream(file);
	// fos.write(pessoaVO.getFoto());
	// fos.close();
	// fos = null;
	// }
	//
	// file = null;
	// }

	public static boolean possuiOperadoresMatematicos(String valor) {
		if ((valor.indexOf("+") != -1) || (valor.indexOf("-") != -1) || (valor.indexOf("*") != -1) || (valor.indexOf("/") != -1)) {
			return true;
		}
		return false;
	}

	public static Integer calcularIdadePessoa(Date dataAtualPrm, Date dataNascimentoPrm) {
		if ((dataNascimentoPrm == null) || (dataAtualPrm == null)) {
			return 0;
		}
		// Desmembrando a data de nascimento
		Calendar nascimentoCalendario = Calendar.getInstance();
		nascimentoCalendario.setTime(dataNascimentoPrm);
		int anoNascimento = nascimentoCalendario.get(Calendar.YEAR);
		String mesNascimentoStr = String.valueOf(nascimentoCalendario.get(Calendar.MONTH) + 1);
		if (mesNascimentoStr.length() == 1) {
			mesNascimentoStr = "0" + mesNascimentoStr;
		}
		String diaNascimentoStr = String.valueOf(nascimentoCalendario.get(Calendar.DAY_OF_MONTH));
		if (diaNascimentoStr.length() == 1) {
			diaNascimentoStr = "0" + diaNascimentoStr;
		}
		String mesDiaNascimento = mesNascimentoStr + diaNascimentoStr;

		// Desmembrando a data atual (passada como parametro)
		Calendar dataAtualCalendario = Calendar.getInstance();
		dataAtualCalendario.setTime(dataAtualPrm);

		int anoAtual = dataAtualCalendario.get(Calendar.YEAR);

		String mesAtualStr = String.valueOf(dataAtualCalendario.get(Calendar.MONTH) + 1);
		if (mesAtualStr.length() == 1) {
			mesAtualStr = "0" + mesAtualStr;
		}

		String diaAtualStr = String.valueOf(dataAtualCalendario.get(Calendar.DAY_OF_MONTH));
		if (diaAtualStr.length() == 1) {
			diaAtualStr = "0" + diaAtualStr;
		}

		String mesDiaAtual = mesAtualStr + diaAtualStr;

		int idade = anoAtual - anoNascimento;
		if (mesDiaAtual.compareTo(mesDiaNascimento) < 0) {
			idade--;
		}

		return idade;
	}

	public static String removerAcentuacao(String strRemoverAcento) {
		String input = strRemoverAcento;
		if (input != null && !input.equals("")) {
			input = Normalizer.normalize(input, Normalizer.Form.NFD);
			input = input.replaceAll("[^\\p{ASCII}]", "");
		}
		return input;
	}

	public static String corrigirApostrofo(String strComApostrofo) {
		int posicao = 0;
		String strCorrigida = null;
		if (strComApostrofo.contains("'")) {
			posicao = strComApostrofo.indexOf("'");
			String parteInicial = strComApostrofo.substring(0, posicao);
			String parteFinal = strComApostrofo.substring(posicao, strComApostrofo.length());
			strCorrigida = parteInicial + "\\" + parteFinal;
			return strCorrigida;
		} else {
			return strComApostrofo;
		}
	}

	public static int getHoraDaString(String hora) {
		if (hora.length() >= 5) {
			if (hora.contains(":")) {
				return Integer.parseInt(hora.substring(0, 2));
			}
		}
		return 0;
	}

	public static int getMinutosDaString(String hora) {
		if (hora.length() >= 5) {
			if (hora.contains(":")) {
				return Integer.parseInt(hora.substring(3, 5));
			}
		}
		return 0;
	}

	public static int getHoraMinutoSegundo(Date data, String retornar) throws Exception {
		Calendar cal = Calendar.getInstance();
		cal.setTime(data);
		if (retornar.equals("hora")) {
			return cal.get(Calendar.HOUR_OF_DAY);
		} else if (retornar.equals("minutos")) {
			return cal.get(Calendar.MINUTE);
		} else if (retornar.equals("segundos")) {
			return cal.get(Calendar.SECOND);
		} else {
			return 0;
		}
	}

	public static Integer validarSomenteNumeroString(String valorInteiro) throws Exception {
		try {
			return Integer.valueOf(valorInteiro.trim());
		} catch (Exception e) {
			throw new Exception("Digite apenas números para o campo (CÓDIGO) !");
		}
	}

	public static Boolean realizarValidacaoHora1MaiorHora2(String hora1, String hora2) {
		Integer h1 = Integer.valueOf(hora1.substring(0, 2));
		Integer h2 = Integer.valueOf(hora2.substring(0, 2));
		if (h1 > h2) {
			return true;
		} else if (h1.equals(h2)) {
			h1 = Integer.valueOf(hora1.substring(3, 5));
			h2 = Integer.valueOf(hora2.substring(3, 5));
			if (h1 > h2) {
				return true;
			}
		}
		return false;

	}

	/***
	 * MÃ?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬ Ã?Â Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?Â¢Ã¢â?¬Å¾Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬ â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â? ¬Å¡Ã?Â¬Ã?â??Ã?Â Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â ¡Ã?â??Ã?Â¬Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â ¬Ã?Â¾Ã?â??Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â  Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â ¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Å¡ Ã?â??Ã?Â¢Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â? ¬Å¡Ã?Â¬Ã?â?¦Ã?Â¡Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â ¬Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â ¬Ã?â?¦Ã?Â¾Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â¢Ã?Æ?Ã?â??Ã?â?  Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬ Ã¢â??Â¢Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â? ¬Å¡Ã?Â¬Ã?Â¢Ã¢â?¬Å¾Ã?Â¢Ã?Æ?Ã?â??Ã?â?  Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â¢Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â? ¬Å¡Ã?Â¬Ã?â? ¦Ã?Â¡Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â¬Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â¦Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â ¡Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬ Ã¢â??Â¢Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â ¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã¢â?¬Â¦Ã?â??Ã?Â ¡Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å
	 * ¡Ã?Â¬Ã?â?¦Ã?Â¡Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â©todo retornar no formato hora e minutos de acordo com a quantidade de minutos informado
	 *
	 * @param minutos
	 *            - valor em minutos
	 * @return String
	 */
	public static String getMinutosEmHorasEMinuto(String minutos) {
		// Preparando hora zerada
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		Calendar cal = Calendar.getInstance();

		cal.set(Calendar.HOUR, 12);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);

		cal.set(Calendar.MINUTE, Integer.parseInt(minutos));

		return sdf.format(cal.getTime()).toString();
	}

	public static Double converterMinutosEmHoras(Double minutos) {
		Integer horas = Integer.valueOf(Double.valueOf(minutos / 60).intValue());
		Integer minuto = Double.valueOf(minutos).intValue();
		if (horas > 0) {
			minuto = Double.valueOf(Uteis.arrendondarForcando2CadasDecimais(Double.valueOf(minutos - (horas * 60)))).intValue();
		}
		while (minuto > 59) {
			horas = horas + 1;
			minuto = minuto - 60;
		}
		return arrendondarForcando2CadasDecimais(Double.valueOf((horas + Double.valueOf(Double.valueOf(minuto) / 100))));
	}

	public static String converterMinutosEmHorasStr(Double minutos) {
		Integer horas = Integer.valueOf(Double.valueOf(minutos / 60).intValue());
		Integer minuto = Double.valueOf(minutos).intValue();
		if (horas > 0) {
			minuto = Double.valueOf(Uteis.arrendondarForcando2CadasDecimais(Double.valueOf(minutos - (horas * 60)))).intValue();
		}
		while (minuto > 59) {
			horas = horas + 1;
			minuto = minuto - 60;
		}
		double horasComp = arrendondarForcando2CadasDecimais(Double.valueOf((horas + Double.valueOf(Double.valueOf(minuto) / 100))));
		String horaString = String.valueOf(horasComp);
		String[] horaStringN = horaString.split("\\.");
		if (horaStringN[1].length() != 2) {
			horaStringN[1] = horaStringN[1] + "0";
		}

		return horaStringN[0] + ":" + horaStringN[1];

	}

	public static byte[] realizarUpload(FileUploadEvent upload) throws Exception {
		return upload.getFile().getContent();
		// UploadItem item = upload.getUploadItem();
		// File item1 = item.getFile();
		// String nome = item.getFileName();
		//
		//// String extensao = (nome.substring(nome.lastIndexOf('.') + 1));
		// String extensao = upload.getUploadedFile().getFileExtension();
		// try {
		// ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
		// byte buffer[] = new byte[4096];
		// int bytesRead = 0;
		//
		// FileInputStream fi = new FileInputStream(item1.getAbsolutePath());
		// while ((bytesRead = fi.read(buffer)) != -1) {
		// arrayOutputStream.write(buffer, 0, bytesRead);
		// }
		// arrayOutputStream.close();
		// fi.close();
		// return arrayOutputStream.toByteArray();
		// } catch (Exception e) {
		// throw e;
		// }
	}

	public static Double converterHorasEmMinutos(Double horas) {
		Integer parteInteira = horas.intValue();
		Integer minuto = Double.valueOf(Uteis.arrendondarForcando2CadasDecimais(parteInteira - horas) * 100).intValue();
		if (minuto < 0) {
			minuto = minuto * -1;
		}
		return arrendondarForcando2CadasDecimais(Double.valueOf((parteInteira * 60) + minuto));
	}

	public static Date getDataAdicionadaEmHoras(Date data, int horas) {
		Calendar calendario = new GregorianCalendar();
		calendario.setTime(data);
		calendario.add(Calendar.HOUR_OF_DAY, horas);
		return calendario.getTime();
	}

	public static boolean validarNomeParaCenso(String nome) {
		String letrasValidas = "AÃ?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?â??Ã?Â¯Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â¿Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â½Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã¢â?¬Â¦Ã?â??Ã?Â¡Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?Â¢Ã¢â?¬Å¾Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â?¦Ã?Â¡Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â¬BCÃ?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â¡DEÃ?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â°Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â¦Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â¹Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â??Ã?Â FGHIÃ?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?â??Ã?Â¯Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â¿Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â½Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â¦Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â½Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â¦Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?Â¢Ã¢â?¬Å¾Ã?Â¢YJKLMNOÃ?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã¢â?¬Â¦Ã?Â¢Ã¢â??Â¬Ã?â??Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã¯Â¿Â½Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¾Ã?â??Ã?Â¢PQRSTUÃ?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â¦Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â¡Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?ÂºÃ?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â?¦Ã?Â¾Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â¢VWXYZ ";

		for (int i = 0; i < nome.length(); i++) {
			// if (!(letrasValidas.indexOf(nome.toUpperCase().charAt(i), 0) !=
			// -1)) {
			// throw new
			// Exception("O
			// caractÃ?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?Â¢Ã¢â?¬Å¾Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â??Ã?Â Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¾Ã?â??Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â¢Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â?¦Ã?Â¡Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â¬Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â?¦Ã?Â¾Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?Â¢Ã¢â?¬Å¾Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â¢Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â?¦Ã?Â¡Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â¬Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â¦Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â¡Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã¢â?¬Â¦Ã?â??Ã?Â¡Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â?¦Ã?Â¡Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â©r
			// '"
			// + nome.charAt(i) + "' na palavra '" + nome +
			// "'
			// Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?Â¢Ã¢â?¬Å¾Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â??Ã?Â Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¾Ã?â??Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â¢Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â?¦Ã?Â¡Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â¬Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â?¦Ã?Â¾Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?Â¢Ã¢â?¬Å¾Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â¢Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â?¦Ã?Â¡Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â¬Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â¦Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â¡Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã¢â?¬Â¦Ã?â??Ã?Â¡Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â?¦Ã?Â¡Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â©
			// invÃ?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?Â¢Ã¢â?¬Å¾Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â??Ã?Â Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¾Ã?â??Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â¢Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â?¦Ã?Â¡Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â¬Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â?¦Ã?Â¾Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?Â¢Ã¢â?¬Å¾Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â¢Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â?¦Ã?Â¡Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â¬Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â¦Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â¡Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã¢â?¬Â¦Ã?â??Ã?Â¡Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â?¦Ã?Â¡Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â¡lido");
			// }
			if (i > 1) {
				if ((nome.toUpperCase().charAt(i - 1) == ' ') && (nome.toUpperCase().charAt(i) == ' ')) {
					// nome.toUpperCase().charAt(i) = '';
				}
			}
		}

		return true;
	}

	public static String getObterParteStringMantendoPalavraFinalInteira(String value, int tam, boolean removerTagsHtml) {
		if (removerTagsHtml) {
			value = retiraTags(value);
		}
		value = value.trim();
		if (value.length() > tam) {
			if (value.indexOf(" ", tam) > 0) {
				tam = value.indexOf(" ", tam);
			} else {
				return value;
			}
		} else {
			return value;
		}

		return value.substring(0, tam) + " ...";
	}

	public static String retiraTags(String textoFormatado) {
		String[] textos = textoFormatado.split("<.*?>");
		StringBuilder sb = new StringBuilder();
		for (String s : textos) {
			s = s.replace("\\\\n", "");
			s = s.replace("&nbsp;", "");
			if (!s.contains("DOCTYPE") && !s.contains("Untitled document") && !s.trim().isEmpty() && !s.trim().equals("&nbsp;")) {
				sb.append(s);
			}
		}

		return sb.toString();
	}

	public static String retiraTagsScript(String textoFormatado) {
		return textoFormatado.replaceAll("<script", "<script2").replaceAll("</script", "</script2");

	}

	public static String retirarCodigoHtmlEspaco(String textoFormatado) {
		return textoFormatado.replaceAll("&nbsp;", "");

	}

	public static boolean getValidaEmail(String email) {
		boolean isEmailValido = false;
		if (email != null && email.length() > 0) {
			String expression = "^[\\w-]+(?:\\.[\\w-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
			Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(email);
			if (matcher.matches()) {
				isEmailValido = true;
			}
		}
		return isEmailValido;
	}

	public static boolean getValidaEmailInternetAddress(String email) {
		boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
	}

	public static Date alterarSomenteDia(Integer novoDia, Date dataAntiga) {
		GregorianCalendar novaData;
		Date dataComUltimoDiaMes = getDataUltimoDiaMes(dataAntiga);
		if (novoDia > getDiaMesData(dataComUltimoDiaMes)) {
			return dataComUltimoDiaMes;
		} else {
			novaData = new GregorianCalendar(Uteis.getAnoData(dataAntiga), Uteis.getMesData(dataAntiga) - 1, novoDia);
			return novaData.getTime();
		}
	}

	public static String getDataBD0000(Date data) {
		DateFormat df = null;
		Calendar calendar = new GregorianCalendar();
		try {
			calendar.setTimeInMillis(data.getTime());
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			return df.format(calendar.getTime());
		} finally {
			df = null;
			calendar = null;
		}
	}

	public static String getDataBD2359(Date data) {
		DateFormat df = null;
		Calendar calendar = new GregorianCalendar();
		try {
			calendar.setTimeInMillis(data.getTime());
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			calendar.set(Calendar.MILLISECOND, 999);
			df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			return df.format(calendar.getTime());
		} finally {
			df = null;
			calendar = null;
		}
	}

	public static String getNomeResumidoPessoa(String nomePessoa) {
		if (nomePessoa == null) {
			return "";
		}
		String[] nomes = nomePessoa.split(" ");
		String nomeResumido = "";
		Integer indice = 1;
		nomeResumido += nomes[0] + " ";
		while (indice < nomes.length - 1) {
			if (nomes[indice].length() > 2) {
				nomes[indice] = nomes[indice].substring(0, 1).concat(".");
			}
			nomeResumido += nomes[indice] + " ";
			indice = indice + 1;
		}
		if (nomes.length == 1) {
			return nomeResumido;
		}
		nomeResumido += nomes[nomes.length - 1] + " ";
		return nomeResumido;
	}

	public static void aplicarHoraMinutoSegundoAtualEmData(Date data) {
		data.setHours(new Date().getHours());
		data.setMinutes(new Date().getMinutes());
		data.setSeconds(new Date().getSeconds());
	}

	public static void removerObjetoMemoria(Object object) {
		if (object != null) {
			Class classe = object.getClass();
			for (Field field : classe.getDeclaredFields()) {
				field.setAccessible(true);
				if (field != null) {
					try {
						if (field.getClass().getSuperclass().getSimpleName().equals("SuperVO")) {
							// removerObjetoMemoria(field.getClass().getSuperclass());
							removerObjetoMemoria(field);
						} else if (field.getType().getName().equals("java.lang.Integer") || field.getType().getName().equals("java.lang.String") || field.getType().getName().equals("java.lang.Double") || field.getType().getName().equals("java.lang.Boolean") || field.getType().getName().equals("java.lang.Long")) {
							field.set(object, null);
						} else if (field.getType().getName().equals("java.util.List") || field.getType().getName().equals("java.util.ArrayList")) {
							List<?> lista = (List<?>) field.get(object);
							// for(Object o: lista){
							// removerObjetoMemoria(o);
							// }
							if (lista != null) {
								lista.clear();
							}
							field.set(object, null);
						} else {
							field.set(object, null);
						}
						field = null;
					} catch (Exception e) {
					}
				}
			}
			if (object.getClass().getSuperclass().getSimpleName().equals("SuperVO") || object.getClass().getSuperclass().getSimpleName().equals("SuperControle") || object.getClass().getSuperclass().getSimpleName().equals("SuperControleRelatorio")) {
				removerObjetoMemoria(object.getClass().getSuperclass());
			}
			classe = null;
		}

	}

	@SuppressWarnings("CallToThreadDumpStack")
	public static Boolean getArquivoExcluido(String caminhoWebService, String caminho) {
		HttpURLConnection connection = null;
		try {
			URL url = new URL(caminhoWebService);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.connect();

			int statusCode = connection.getResponseCode();
			InputStream rstream = null;
			rstream = connection.getInputStream();

			return true;

		} catch (java.net.MalformedURLException mue) {
		} catch (java.io.IOException ioe) {
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
		return Boolean.FALSE;
	}

	public static String getCaminhoDownloadRelatorio(boolean fazerDownload, String caminhoRelatorio) {
		if (fazerDownload) {
			try {
				HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
				String caminho = request.getRequestURL().toString().replace(request.getRequestURI().toString(), "") + request.getContextPath() + "/";
				return "location.href='" + caminho + "DownloadRelatorioSV?relatorio=" + caminhoRelatorio + "'";
			} catch (Exception ex) {
				Logger.getLogger("SuperControleRelatorio").log(Level.SEVERE, null, ex);
			}
		}
		return "";
	}

	public static String getTagImageComFotoPadrao() {
		return new StringBuilder("<img style='width: 100px; height: 120px' src='fotos/imagemPadrao.jpg'/>").toString();
	}

	public static String getDiretorioWebTemporario() {
		File dir = null;
		String retorno = null;
		ServletContext contexto = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
		try {
			dir = new File(contexto.getRealPath("temp"));
			if (!dir.exists()) {
				dir.mkdirs();
			}
			retorno = dir.getPath();
		} finally {
			dir = null;
		}
		return retorno;
	}

	public static Boolean getMesmoMesAno(Date dataInicio, Date dataTermino) {
		try {
			if ((Uteis.getAnoData(dataInicio) == Uteis.getAnoData(dataTermino)) && (Uteis.getMesData(dataInicio) == Uteis.getMesData(dataTermino))) {
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	public static Date getObterDataPorDataDiaMesAno(java.util.Date dataConverter, String tipo, int quantidade) {
		Calendar c = Calendar.getInstance();
		c.setTime(dataConverter);
		if (tipo.equals("dia")) {
			c.add(Calendar.DAY_OF_MONTH, quantidade);
		} else if (tipo.equals("mes")) {
			c.add(Calendar.MONTH, quantidade);
		} else if (tipo.equals("ano")) {
			c.add(Calendar.YEAR, quantidade);
		}
		return c.getTime();
	}

	public static String getObterTempoEntreDuasData(Date dataFinal, Date dataInicial) {
		long sec = (dataFinal.getTime() - dataInicial.getTime());
		// variÃ?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?Â¢Ã¢â?¬Å¾Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â??Ã?Â Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¾Ã?â??Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â¢Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â?¦Ã?Â¡Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â¬Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â?¦Ã?Â¾Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?Â¢Ã¢â?¬Å¾Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â¢Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â?¦Ã?Â¡Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â¬Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â¦Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â¡Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã¢â?¬Â¦Ã?â??Ã?Â¡Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â?¦Ã?Â¡Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â¡veis
		// auxiliares
		long second = 1000;
		long minute = 60 * second;
		long hour = 60 * minute;
		long day = 24 * hour;

		// resultado em horas decimais
		Double rdh = Math.ceil(sec / hour);
		// resultado em minutos decimais
		double rdm = Math.ceil(sec / minute);
		// resultado em segundos
		double rds = Math.ceil(sec / second);

		Double days = Math.floor(sec / day);
		sec -= days * day;
		Double hours = Math.floor(sec / hour);
		sec -= hours * hour;
		Double minutes = Math.floor(sec / minute);
		// Caso queira saber o segunto
		sec -= minutes * minute;
		Double seconds = Math.floor(sec / second);
		//hours.intValue() < 10 ? "0" + hours.intValue() : hours.intValue() * days
		return (hours.intValue() < 10 ? "0" + hours.intValue() : hours.intValue()) + ":" + (minutes.intValue() < 10 ? "0" + minutes.intValue() : minutes.intValue()) + ":" + (seconds.intValue() < 10 ? "0" + seconds.intValue() : seconds.intValue());

	}

	public static Integer getObterDiferencaDiasEntreDuasData(Date dataFinal, Date dataInicial) {
		return getObterDiferencaDiasEntreDuasData(dataFinal, dataInicial, false);
	}

	public static Integer getObterDiferencaDiasEntreDuasData(Date dataFinal, Date dataInicial, boolean acrescentar1hora) {
		Long milliSec = 0L;
		if (acrescentar1hora) {
			milliSec = (dataFinal.getTime() - dataInicial.getTime()) + 3600000;
		} else {
			milliSec = (dataFinal.getTime() - dataInicial.getTime());
		}
		Long dias = milliSec / 3600 / 24 / 1000;
		// variÃ?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?Â¢Ã¢â?¬Å¾Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â??Ã?Â Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¾Ã?â??Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â¢Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â?¦Ã?Â¡Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â¬Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â?¦Ã?Â¾Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?Â¢Ã¢â?¬Å¾Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â¢Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â?¦Ã?Â¡Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â¬Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â¦Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â¡Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã¢â?¬Â¦Ã?â??Ã?Â¡Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â?¦Ã?Â¡Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â¡veis
		// auxiliares
		return dias.intValue();

	}

	public static Boolean getValidarTempoEsgotado(Date dataFinal, Date dataInicial) {
		long sec = (dataFinal.getTime() - dataInicial.getTime());
		// variÃ?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?Â¢Ã¢â?¬Å¾Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â??Ã?Â Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¾Ã?â??Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â¢Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â?¦Ã?Â¡Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â¬Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â?¦Ã?Â¾Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?Â¢Ã¢â?¬Å¾Ã?Â¢Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â¢Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â?¦Ã?Â¡Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â¬Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â¦Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â¡Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?â??Ã?Â¬Ã?Æ?Ã¢â?¬Â¦Ã?â??Ã?Â¡Ã?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â¬Ã?â?¦Ã?Â¡Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â¡veis
		// auxiliares
		long second = 1000;
		long minute = 60 * second;
		long hour = 60 * minute;
		long day = 24 * hour;

		// resultado em horas decimais
		double rdh = Math.ceil(sec / hour);
		// resultado em minutos decimais
		double rdm = Math.ceil(sec / minute);
		// resultado em segundos
		double rds = Math.ceil(sec / second);

		Double days = Math.floor(sec / day);
		sec -= days * day;
		Double hours = Math.floor(sec / hour);
		sec -= hours * hour;
		Double minutes = Math.floor(sec / minute);
		// Caso queira saber o segunto
		sec -= minutes * minute;
		Double seconds = Math.floor(sec / second);
		if (hours.intValue() <= 0 && minutes.intValue() <= 0 && seconds.intValue() <= 0) {
			return true;
		}
		return false;

	}

	public static String formatarTelefoneParaEnvioSms(String telefone) throws Exception {
		String telefoneFormatado = "55" + telefone.replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("-", "").replaceAll(" ", "");

		if (telefoneFormatado.length() < 12 || telefoneFormatado.length() > 13) {
			throw new Exception("numero invalido");
		}
		return telefoneFormatado;
	}

	public static String formatarTelefoneParaEnvioLocaSms(String telefone) throws Exception {
		String telefoneFormatado = "" + telefone.replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("-", "").replaceAll(" ", "");
		if (telefoneFormatado.length() < 10 || telefoneFormatado.length() > 11) {
			throw new Exception("numero invalido");
		}
		return telefoneFormatado;
	}

	public static boolean compararDatasSemConsiderarHoraMinutoSegundo(Date data1, Date data2) throws Exception {
		SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
		return sd.parse(sd.format(data1)).equals(sd.parse(sd.format(data2)));
	}

	@SuppressWarnings("CallToThreadDumpStack")
	public static String realizarMontagemDeValoresSeparadosPorVirgula(List<?> objs, String nomeMetodo) {
		StringBuilder sb = new StringBuilder(" ");
		if (objs != null && !objs.isEmpty()) {
			try {
				Method metodo = objs.get(0).getClass().getMethod(nomeMetodo, null);
				for (Object obj : objs) {
					sb.append(" ").append(metodo.invoke(obj, new Object[] {})).append(",");
				}
			} catch (Exception e) {
				return "";
			}
		}
		return sb.toString().substring(0, sb.length() - 1);
	}

	public static int gerarDigitoVerificadorNossoNumeroCodigoCedenteITAU(String agencia, String contaCorrente, String carteira, String nossoNumero) {
		try {
			int dac = 0;
			if (contaCorrente.indexOf("-") != -1) {
				contaCorrente = contaCorrente.substring(0, contaCorrente.indexOf("-"));
			}
			String campo = String.valueOf(agencia) + contaCorrente + String.valueOf(carteira) + nossoNumero;

			int multiplicador = 1;
			int multiplicacao = 0;
			int soma_campo = 0;

			for (int i = 0; i < campo.length(); i++) {
				multiplicacao = Integer.parseInt(campo.substring(i, 1 + i)) * multiplicador;

				if (multiplicacao >= 10) {
					multiplicacao = Integer.parseInt(String.valueOf(multiplicacao).substring(0, 1)) + Integer.parseInt(String.valueOf(multiplicacao).substring(1));
				}
				soma_campo = soma_campo + multiplicacao;

				if (multiplicador == 2) {
					multiplicador = 1;
				} else {
					multiplicador = 2;
				}

			}

			dac = 10 - (soma_campo % 10);

			if (dac == 10) {
				dac = 0;
			}

			return dac;
		} catch (Exception e) {
			return 0;
		}
	}

	public static String gerarDigitoVerificadorNossoNumeroCodigoCedenteBradesco(String carteira, String nossoNumero) {
		try {
			String digitoVerificador = "";
			String numeroCalculo = carteira + nossoNumero;
			if (!numeroCalculo.equals("")) {
				String[] vetorNumeroCalculo = numeroCalculo.split("");
				int numMultiplicador = 2;
				int valor = 0;
				for (int num = vetorNumeroCalculo.length - 1; num >= 0; num--) {
					if (!vetorNumeroCalculo[num].equals("")) {
						valor = valor + (numMultiplicador * Integer.parseInt(vetorNumeroCalculo[num]));
						if (numMultiplicador == 7) {
							numMultiplicador = 2;
						} else {
							numMultiplicador++;
						}
					}
				}
				int resto = (valor % 11);
				if (resto == 1) {
					digitoVerificador = "P";
				} else if (resto == 0) {
					digitoVerificador = "0";
				} else {
					digitoVerificador = String.valueOf(11 - (valor % 11));
				}
			}
			return digitoVerificador;
		} catch (Exception e) {
			return "0";
		}
	}

	public static String gerarDigitoVerificadorNossoNumeroCodigoCedenteCaixaEconomica(String carteira, String nossoNumero) {
		try {
			String digitoVerificador = "";
			String numeroCalculo = carteira + nossoNumero;
			if (!numeroCalculo.equals("")) {
				String[] vetorNumeroCalculo = numeroCalculo.split("");
				int numMultiplicador = 2;
				int valor = 0;
				for (int num = vetorNumeroCalculo.length - 1; num >= 0; num--) {
					if (!vetorNumeroCalculo[num].equals("")) {
						valor = valor + (numMultiplicador * Integer.parseInt(vetorNumeroCalculo[num]));
						if (numMultiplicador == 9) {
							numMultiplicador = 2;
						} else {
							numMultiplicador++;
						}
					}
				}
				if (valor < 11) {
					digitoVerificador = String.valueOf(11 - valor);
				} else {
					if ((11 - (valor % 11)) > 9) {
						digitoVerificador = "0";
					} else {
						digitoVerificador = String.valueOf(11 - (valor % 11));
					}
				}
			}
			return digitoVerificador;
		} catch (Exception e) {
			return "0";
		}
	}

	public static String gerarDigitoVerificadorGeralCodigoBarrasCaixaEconomica(String codigoBarras) {
		try {
			String digitoVerificador = "";
			String numeroCalculo = codigoBarras;
			if (!numeroCalculo.equals("")) {
				String[] vetorNumeroCalculo = numeroCalculo.split("");
				int numMultiplicador = 2;
				int valor = 0;
				for (int num = vetorNumeroCalculo.length - 1; num >= 0; num--) {
					if (!vetorNumeroCalculo[num].equals("")) {
						valor = valor + (numMultiplicador * Integer.parseInt(vetorNumeroCalculo[num]));
						if (numMultiplicador == 9) {
							numMultiplicador = 2;
						} else {
							numMultiplicador++;
						}
					}
				}
				if (((11 - (valor % 11)) > 9) || ((11 - (valor % 11)) == 0)) {
					digitoVerificador = "1";
				} else {
					digitoVerificador = String.valueOf(11 - (valor % 11));
				}
			}
			return digitoVerificador;
		} catch (Exception e) {
			return "0";
		}
	}

	public static String gerarDigitoVerificadorLinhaDigitavelCaixaEconomica(String numero, int multiplicador) {
		try {
			String digitoVerificador = "0";
			String numeroCalculo = numero;
			if (!numeroCalculo.equals("")) {
				String[] vetorNumeroCalculo = numeroCalculo.split("");
				int numMultiplicador = multiplicador;
				int valor = 0;
				int multiplicacao = 0;
				int resto = 0;
				for (int num = vetorNumeroCalculo.length - 1; num >= 0; num--) {
					if (!vetorNumeroCalculo[num].equals("")) {
						multiplicacao = (numMultiplicador * Integer.parseInt(vetorNumeroCalculo[num]));
						if (multiplicacao >= 10) {
							multiplicacao = Integer.parseInt(String.valueOf(multiplicacao).substring(0, 1)) + Integer.parseInt(String.valueOf(multiplicacao).substring(1));
						}
						valor = valor + multiplicacao;
						if (numMultiplicador == 2) {
							numMultiplicador = 1;
						} else {
							numMultiplicador = 2;
						}
					}
				}
				resto = valor % 10;
				if (resto == 0) {
					digitoVerificador = "0";
				} else if (valor < 10) {
					digitoVerificador = String.valueOf(10 - valor);
				} else {
					digitoVerificador = String.valueOf(10 - resto);
				}
			}
			return digitoVerificador;
		} catch (Exception e) {
			return "0";
		}
	}

	/**
	 * Transfere um arquivo para a pasta padrÃ?Æ?Ã?â??Ã?â? Ã¢â? â?¢Ã?Æ?Ã¢â? Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã â??Ã?Â¢Ã¢â ?Â¬Ã?Â Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â? Å¡Ã?Â¬Ã?Â¢Ã¢â? Å¾Ã?Â¢Ã?Æ?Ã?â??Ã?â  Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â? Å¡Ã?Â¬Ã?â??Ã?Â Ã?Æ?Ã?â??Ã?â?? Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â ?Â¬Ã?Â¡Ã?â??Ã?Â Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â Ã?Â¾Ã?â??Ã?Â¢Ã?Æ?Ã â??Ã?â? Ã¢â?¬ â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â ?Â¬Ã¢â??Â¢Ã?Æ?Ã â??Ã?â??Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â ?Â¬Ã Â¡Ã?â??Ã?Â¬Ã?Æ?Ã¢â?¬Â¦Ã?â??Ã?Â Ã?Æ?Ã?â??Ã?â? Ã¢â?¬ â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â? Å¡Ã?Â¬Ã?â?¦Ã?Â
	 * Ã?Æ?Ã?â??Ã?Â¢Ã¢â? Â¬Ã?Â¡Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â£o de upload, definida por um servidor web.
	 *
	 * @param origem
	 *            Nome do arquivo na pasta temporÃ?Æ?Ã?â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â? Â Ã?Â¢Ã¢â??Â Ã¢â??Â¢Ã?Æ?Ã?â??Ã?Â¢Ã¢â ?Â¬Ã?Â Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â Ã?Â¢Ã¢â? Å¾Ã?Â¢Ã?Æ?Ã?â??Ã?â  Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â? Å¡Ã?Â Ã?â??Ã?Â Ã?Æ?Ã?â??Ã?â ?Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â ?Â¬Ã?Â¡Ã?â ?Ã?Â¬Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â ?Â¬Ã?Â¾Ã?â ?Ã?Â¢Ã?Æ?Ã?â??Ã?â  Ã¢â?¬â?¢Ã?Æ?Ã¢â? Â Ã?Â¢Ã¢â? Â¬Ã¢â??Â¢Ã?Æ?Ã?â??Ã?â ?Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â ?Â¬Ã?Â¡Ã?â ?Ã?Â¬Ã?Æ?Ã¢â?¬Â¦Ã?â ?Ã?Â¡Ã?Æ?Ã?â??Ã?â? Ã¢â?¬ â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â? Å¡Ã?Â¬Ã?â
	 *            ¦Ã?Â¡Ã?Æ?Ã?â??Ã?Â¢Ã¢â ?Â¬Ã?Â Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â¡ria.
	 * @param destino
	 *            Nome do arquivo final na pasta definitiva de upload no servidor web.
	 * @throws Exception
	 *             Caso haja algum problema na transferÃ?Æ?Ã?â??Ã?â  Ã¢â?¬â?¢Ã?Æ?Ã¢â? Â Ã?Â¢Ã¢â? ?Â¬Ã¢â??Â¢Ã?Æ?Ã?â? ?Ã?Â¢Ã¢â?? Â¬Ã?Â Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â? Å¡Ã?Â Ã?Â¢Ã¢â?¬Å¾Ã?Â¢Ã?Æ?Ã?â ?Ã?â  Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â? Å¡ Ã?Â¬Ã?â??Ã?Â Ã?Æ?Ã?â??Ã?â ? Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡ Ã?â ?Ã?Â¬Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã Â¾Ã?â??Ã?Â¢Ã?Æ?Ã?â??Ã?â?  Ã¢â? â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â Ã¢â??Â¢Ã?Æ?Ã?â??Ã?â?? Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â ?Â¬Ã?Â¡Ã?â ?Ã?Â¬Ã?Æ?Ã¢â?¬Â¦Ã?â ?Ã?Â¡Ã?Æ?Ã?â??Ã?â? Ã¢â?¬ â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â? Å¡Ã?Â¬Ã?â
	 *             ¦Ã?Â¡Ã?Æ?Ã?â??Ã?Â¢Ã¢â ?Â¬Ã? Â¡Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Âªncia, ou as configuraÃ?Æ?Ã â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â? Â  Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â? ?Ã?Â¢Ã¢â ?Â¬Ã?Â Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â? Å¡Ã?Â Ã?Â¢Ã¢â?¬Å¾Ã?Â¢Ã?Æ?Ã?â ?Ã?â  Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â? Å¡ Ã?Â¬Ã?â??Ã?Â Ã?Æ?Ã?â??Ã?â ? Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡ Ã?â ?Ã?Â¬Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã Â¾Ã?â??Ã?Â¢Ã?Æ?Ã?â??Ã?â?  Ã¢â? â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â Ã¢â??Â¢Ã?Æ?Ã?â??Ã?â?? Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â ?Â¬Ã?Â¡Ã?â ?Ã?Â¬Ã?Æ?Ã¢â?¬Â¦Ã?â
	 *             ?Ã?Â¡Ã?Æ?Ã?â??Ã?â? Ã¢â?¬ â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â? Å¡Ã?Â¬Ã?â ¦Ã?Â¡Ã?Æ?Ã?â??Ã?Â¢Ã¢â ?Â¬Ã?Â¡Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â§ Ã?Æ?Ã â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â? Â  Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â? ?Ã?Â¢Ã¢â ?Â¬Ã?Â Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â? Å¡Ã?Â Ã?Â¢Ã¢â?¬Å¾Ã?Â¢Ã?Æ?Ã?â ?Ã?â  Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â? Å¡ Ã?Â¬Ã?â??Ã?Â Ã?Æ?Ã?â??Ã?â ? Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡ Ã?â ?Ã?Â¬Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã Â¾Ã?â??Ã?Â¢Ã?Æ?Ã?â??Ã?â?  Ã¢â? â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â Ã¢â??Â¢Ã?Æ?Ã?â??Ã?â?? Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â
	 *             ?Â¬Ã?Â¡Ã?â ?Ã?Â¬Ã?Æ?Ã¢â?¬Â¦Ã?â ?Ã?Â¡Ã?Æ?Ã?â??Ã?â? Ã¢â?¬ â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â? Å¡Ã?Â¬Ã?â ¦Ã?Â¡Ã?Æ?Ã?â??Ã?Â¢Ã¢â ?Â¬Ã? Â¡Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Âµes do sistema nÃ?Æ?Ã â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â? Â  Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â? ?Ã?Â¢Ã¢â ?Â¬Ã?Â Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â? Å¡Ã?Â Ã?Â¢Ã¢â?¬Å¾Ã?Â¢Ã?Æ?Ã?â ?Ã?â  Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â? Å¡ Ã?Â¬Ã?â??Ã?Â Ã?Æ?Ã?â??Ã?â ? Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡ Ã?â ?Ã?Â¬Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã Â¾Ã?â??Ã?Â¢Ã?Æ?Ã?â??Ã?â?  Ã¢â? â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â
	 *             Ã¢â??Â¢Ã?Æ?Ã?â??Ã?â?? Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â ?Â¬Ã?Â¡Ã?â ?Ã?Â¬Ã?Æ?Ã¢â?¬Â¦Ã?â ?Ã?Â¡Ã?Æ?Ã?â??Ã?â? Ã¢â?¬ â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â? Å¡Ã?Â¬Ã?â ¦Ã?Â¡Ã?Æ?Ã?â??Ã?Â¢Ã¢â ?Â¬Ã?Â¡Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â£o estÃ?Æ?Ã â??Ã?â? Ã¢â?¬â?¢Ã?Æ?Ã¢â? Â  Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã?â? ?Ã?Â¢Ã¢â ?Â¬Ã?Â Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â? Å¡Ã?Â Ã?Â¢Ã¢â?¬Å¾Ã?Â¢Ã?Æ?Ã?â ?Ã?â  Ã¢â?¬â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â? Å¡ Ã?Â¬Ã?â??Ã?Â Ã?Æ?Ã?â??Ã?â ? Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã?Â¡ Ã?â ?Ã?Â¬Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â??Â¬Ã Â¾Ã?â??Ã?Â¢Ã?Æ?Ã?â??Ã?â? 
	 *             Ã¢â? â?¢Ã?Æ?Ã¢â?¬Â Ã?Â¢Ã¢â??Â Ã¢â??Â¢Ã?Æ?Ã?â??Ã?â?? Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â ?Â¬Ã?Â¡Ã?â ?Ã?Â¬Ã?Æ?Ã¢â?¬Â¦Ã?â ?Ã?Â¡Ã?Æ?Ã?â??Ã?â? Ã¢â?¬ â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â? Å¡Ã?Â¬Ã?â ¦Ã?Â¡Ã?Æ?Ã?â??Ã?Â¢Ã¢â ?Â¬Ã?Â¡Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â£o definidas corretamente.
	 * @since 12/07/2011
	 * @author VinÃ?Æ?Ã?â??Ã?â? Ã¢â?¬ â?¢Ã?Æ?Ã¢â? Â Ã?Â¢Ã¢â??Â¬Ã¢â??Â¢Ã?Æ?Ã â??Ã?Â¢Ã¢â ?Â¬Ã?Â Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â? Å¡Ã?Â¬ Ã?Â¢Ã¢â?¬Å¾Ã?Â¢Ã?Æ?Ã?â??Ã?â  Ã¢â? â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â?¬Å¡Ã?Â Ã?â??Ã?Â Ã?Æ?Ã?â??Ã?â?? Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â ?Â¬Ã?Â¡Ã?â ?Ã?Â¬Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â ?Â¬Ã?Â¾Ã?â ?Ã?Â¢Ã?Æ?Ã?â??Ã?â  Ã¢â?¬â?¢Ã?Æ?Ã¢â? Â Ã?Â¢Ã¢â??Â Ã¢â??Â¢Ã?Æ?Ã?â??Ã?â ?Ã?Â¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â ?Â¬Ã?Â¡Ã?â ?Ã?Â¬Ã?Æ?Ã¢â?¬Â¦Ã?â ?Ã?Â¡Ã?Æ?Ã?â??Ã?â? Ã¢â?¬ â?¢Ã?Æ?Ã?Â¢Ã?Â¢Ã¢â? Å¡Ã?Â¬Ã?â? ¦Ã?Â¡Ã?Æ?Ã?â??Ã?Â¢Ã¢â
	 *         ?Â¬Ã?Â¡Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Â­cius Bueno
	 */
	public static String fazerUploadArquivoPastaServidor(final String origem, final String destino, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		URL url = null;
		File src = null;
		String urlPastaServidor = null;
		byte[] buffer = new byte[4096];
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		HttpURLConnection httpUrl = null;
		Base64.Encoder codificador = Base64.getEncoder();
		try {
			src = new File(getDiretorioWebTemporario().concat("/").concat(origem));
			if (!src.exists()) {
				return null;
			}
			bis = new BufferedInputStream(new FileInputStream(src));
			urlPastaServidor = configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo();
			url = new URL(urlPastaServidor.concat("/").concat(destino));
			httpUrl = (HttpURLConnection) url.openConnection();
			httpUrl.setDoOutput(true);
			httpUrl.setRequestMethod("PUT");
			httpUrl.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			httpUrl.connect();
			bos = new BufferedOutputStream(httpUrl.getOutputStream());
			// Escreve dados na rede.
			int tamanho;
			while ((tamanho = bis.read(buffer)) > 0) {
				bos.write(buffer, 0, tamanho);
			}
			bis.close();
			bos.flush();
			bos.close();
			httpUrl.disconnect();
			src.delete();
			return httpUrl.getResponseMessage();
		} finally {
			bis = null;
			src = null;
			url = null;
			buffer = null;
			httpUrl = null;
			codificador = null;
			urlPastaServidor = null;
			// tokenAutenticacao = null;
		}
	}

	
	
	public static String encodeFileToBase64Binary(String fileName) throws Exception {
	    byte[] bytes = loadFile(fileName);
	    
	    return Base64.getEncoder().encodeToString(bytes);
	}

	public static byte[] loadFile(String fileName) {
		File file = new File(fileName);

		byte[] bytes = new byte[(int) file.length()];
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			fileInputStream.read(bytes);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return bytes;
	}

	public static Integer getParteInteiraDouble(Double valor) {
		String string = valor.toString();
		Integer parteInteira = 0;
		if (string.contains(".")) {
			int pos = string.indexOf(".");
			parteInteira = Integer.parseInt(string.substring(0, pos));
		}
		return parteInteira;
	}

	public static Integer getParteDecimalDoubleDuasCasas(Double valor) {
		String string = valor.toString();
		Integer parteDecimal = 0;
		if (string.contains(".")) {
			int pos = string.indexOf(".");
			String parteDecimalString = string.substring(pos + 1, string.length());
			if (parteDecimalString.length() > 1) {
				parteDecimal = Integer.parseInt(parteDecimalString.substring(0, 2));
			} else {
				parteDecimal = Integer.parseInt(parteDecimalString + 0);
			}
		}
		return parteDecimal;
	}

	public static Long getParteDecimalDouble(Double valor) {
		String string = valor.toString();
		Long parteDecimal = (long) 0;
		if (string.contains(".")) {
			int pos = string.indexOf(".");
			parteDecimal = Long.parseLong(string.substring(pos + 1, string.length()));
		}
		return parteDecimal;
	}

	@SuppressWarnings("static-access")
	public static Date getNextWorkingDay(Date aDate, List<FeriadoVO> listaFeriadoVOs) {
		Date dataIncremental = aDate;
		Date dataAuxIncremental = aDate;
		while (!isWorkingDay(dataAuxIncremental, listaFeriadoVOs)) {
			dataAuxIncremental = new Date(dataAuxIncremental.getTime() + ONE_DAY);
		}
		Calendar cal1 = new GregorianCalendar();
		cal1.setTime(dataAuxIncremental);
		Calendar cal2 = new GregorianCalendar();
		cal2.setTime(dataIncremental);
		if (cal1.get(cal1.MONTH) != cal2.get(cal2.MONTH)) {
			Date dataRetroativo = aDate;
			while (!isWorkingDay(dataRetroativo, listaFeriadoVOs)) {
				dataRetroativo = new Date(dataRetroativo.getTime() - ONE_DAY);
			}
			return dataRetroativo;
		} else {
			dataIncremental = dataAuxIncremental;
			return dataIncremental;
		}
	}

	public static boolean isWorkingDay(Date date, List<FeriadoVO> listaFeriadoVOs) {
		if (!Uteis.isWorkingWeekDay(date)) {
			return false;
		}

		for (FeriadoVO holiday : listaFeriadoVOs) {
			if (holiday.isHoliday(date)) {
				return false;
			}
		}

		return true;
	}

	public static double truncar(double valor, int casasDecimais) {
		BigDecimal bigDecimal = new BigDecimal(valor);
		bigDecimal = bigDecimal.setScale(casasDecimais, BigDecimal.ROUND_HALF_UP);
		return bigDecimal.doubleValue();
		// double multiplicador = Double.parseDouble(String.format("1%0" +
		// casasDecimais + "d", 0));
		// long aux = (long) (valor * multiplicador);
		// return (double) aux / multiplicador;
	}

	public static Double realizarCalculoParaDivisaoComBigDecimal(Double valor1, Double valor2 ) {
		BigDecimal bd = new BigDecimal(valor1.toString());
		BigDecimal bd1 = new BigDecimal(valor2.toString());
		bd = bd.divide(bd1, MathContext.DECIMAL128);
		return bd.doubleValue();
	}

	/**
	 * Metodo que realizar o calculo de porcentagem com valores em bigdecimal
	 * @param valorBase informao cuja qual vc deseja caculcar a porcentagem
	 * @param porcentagem informao de quantos porcento vc quer calcular sobre o valor base
	 * @return
	 */
	public static BigDecimal porcentagemComBigDecimal(BigDecimal valorBase, BigDecimal porcentagem ) {
		return valorBase.multiply(porcentagem).divide(new BigDecimal(100), MathContext.DECIMAL128);
	}

	public static double truncarForcando2CadasDecimais(double valor) {
		String valorStr = String.valueOf(valor);
		if (!valorStr.contains(".")) {
			return valor;
		}
		String inteira = valorStr.substring(0, valorStr.indexOf("."));
		String extensao = valorStr.substring(valorStr.indexOf(".") + 1, valorStr.length());
		if(extensao.length()>2) {
			extensao = extensao.substring(0, 2);
		}
		valorStr = removerMascara(inteira) + "." + extensao;
		return Double.parseDouble(valorStr);
	}

	public static double truncarrForcando2CadasDecimais(double valor) {
		return truncarForcando2CadasDecimais(valor);
	}

//	public static String realizarMontagemDadosGraficoPizza(List<LegendaGraficoVO> listaDadosAcesso) {
//
//		StringBuilder resultado = new StringBuilder("");
//
//		double total = 0.0;
//		for (LegendaGraficoVO obj : listaDadosAcesso) {
//			total += obj.getValor();
//		}
//		for (LegendaGraficoVO obj : listaDadosAcesso) {
//			if (!resultado.toString().isEmpty()) {
//				resultado.append(", ");
//			}
//			resultado.append("{nome:'").append(Uteis.removerAcentos(obj.getNome()).replaceAll("Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Âª", "").replaceAll("Ã?Æ?Ã?â??Ã?Â¢Ã¢â??Â¬Ã?Â¡Ã?Æ?Ã¢â?¬Å¡Ã?â??Ã?Âº", "")).append("', y:");
//			resultado.append(Uteis.truncar(obj.getValor() * 100.0 / total, 2)).append(", valor:'").append("R$" + Uteis.getDoubleFormatado(obj.getValor()));
//			resultado.append("', cor:'").append(obj.getCor()).append("', nivel:'").append(obj.getNivel()).append("', codigo:'").append(obj.getCodigo()).append("'}");
//		}
//		return resultado.toString();
//
//	}

	/**
	 * getModulo11
	 *
	 * @param String
	 *            value
	 * @param int
	 *            type
	 * @return int dac
	 * @author Mario Grigioni
	 */
	public static String getModulo11Santander(String campo, int type) {
		// Modulo 11 - 234567 (type = 7)
		// Modulo 11 - 23456789 (type = 9)

		int multiplicador = 2;
		int multiplicacao = 0;
		int soma_campo = 0;

		for (int i = campo.length(); i > 0; i--) {
			multiplicacao = Integer.parseInt(campo.substring(i - 1, i)) * multiplicador;

			soma_campo = soma_campo + multiplicacao;

			multiplicador++;
			if (multiplicador > 7 && type == 7) {
				multiplicador = 2;
			} else if (multiplicador > 9 && type == 9) {
				multiplicador = 2;
			}
		}

		int dac = (soma_campo / 11) * 11;
		dac = soma_campo - dac;

		if (dac == 10) {
			dac = 1;
		} else if ((dac == 0 || dac == 1) && type == 9) {
			dac = 0;
		} else {
			dac = 11 - dac;
		}

		return ((Integer) dac).toString();
	}

	public static double arredondarMultiploDeCincoParaCima(Double valorBase) {
		Integer valorInt = valorBase.intValue();
		Double decimal = Uteis.arrendondarForcando2CadasDecimais(valorBase - valorInt);
		if (decimal >= 0.01 && decimal <= 0.49) {
			return valorInt + 0.50;
		} else if (decimal >= 0.51 && decimal <= 0.99) {
			return valorInt + 1;
		}
		return valorBase;
	}

	/*
	 * Mï¿½todo responsï¿½vel em copiar parte do valor de entrada
	 *
	 * @author Wendel Rodrigues
	 */
	public static String copiarDelimitandoTamanhoDoTexto(String texto, int tamanhoMaximo) {
		int tamanhoTexto = texto.length();

		if (tamanhoTexto >= tamanhoMaximo) {
			return texto.substring(0, tamanhoMaximo);
		} else {
			return texto;
		}

	}

	/*
	 * Mï¿½todo responsï¿½vel em preencher com espaï¿½os vazios a direita do valor de entrada, limitando ao tamanho mï¿½ximo de caracteres.
	 *
	 * @author Wendel Rodrigues
	 */
	public static String preencherComVazioLimitandoTamanho(String texto, int tamanhoMaximo) {
		int tamanhoTexto = texto.length();

		if (tamanhoTexto >= tamanhoMaximo) {
			return texto.substring(0, tamanhoMaximo);
		} else {
			int quantidadeZeros = tamanhoMaximo - tamanhoTexto;
			for (int i = 0; i < quantidadeZeros; i++) {
				texto += " ";
			}
			return texto;
		}

	}

	/*
	 * Mï¿½todo responsï¿½vel em preencher com zeros a direita do valor de entrada, limitando ao tamanho mï¿½ximo de caracteres.
	 *
	 * @author Wendel Rodrigues
	 */
	public static String preencherComZerosLimitandoTamanho(Integer numero, int tamanhoMaximo) {
		int tamanhoNumero = numero.toString().length();

		if (tamanhoNumero >= tamanhoMaximo) {
			return numero.toString().substring(0, tamanhoMaximo);
		} else {
			int quantidadeZeros = tamanhoMaximo - tamanhoNumero;
			String numeroConvertido = numero.toString();

			for (int i = 0; i < quantidadeZeros; i++) {
				numeroConvertido += "0";
			}
			return numeroConvertido;
		}

	}

	public static String retornarDuasCasasDecimais(String valor) {
		String decimal = valor.substring(valor.lastIndexOf(".") + 1);
		if (decimal.length() == 1) {
			return valor + "0";
		}
		return valor;
	}

	/*
	 * Mï¿½todo responsï¿½vel em retornar prefixo ou sufixo do CEP.
	 *
	 * @author Wendel Rodrigues
	 */
	public static String retornarPrefixoOuSufixoDoCep(String cep, String retorno) {
		if (cep.endsWith("")) {
			return "";
		}
		String s[] = cep.split("-");

		if (s.length == 1) {
			String c = cep.substring(0, 5) + "-" + cep.substring(5, cep.length());
			s = c.split("-");
		}

		if (retorno.equals("prefixo")) {
			return s[0];
		} else {
			return s[1];
		}
	}

	/*
	 * Mï¿½todo responsï¿½vel em retornar a URL da imagem da tag img do HTML.
	 *
	 * @author Wendel Rodrigues
	 */
	public static String retornarURLDaImagemHtml(String linha) {
		try {
			Pattern p = null;
			Matcher m = null;
			String word0 = "";

			p = Pattern.compile(".*<img[^>]*src=\"([^\"]*)", Pattern.CASE_INSENSITIVE);
			m = p.matcher(linha);
			while (m.find()) {
				word0 = m.group(1);
				return word0.toString();
			}
			return "";
		} catch (Exception e) {
			return "";
		}
	}

	public static String retornarNomeDaImagem(String link) {
		String[] texto = link.split("/");
		String nomeArquivo = "";
		for (int i = 0; i < texto.length; i++) {
			if ((i + 1) == texto.length) {
				nomeArquivo = texto[i];
			}
		}
		return nomeArquivo;
	}

	/*
	 * Mï¿½todo responsï¿½vel em fazer download da imagem passada por parametro, salvando dento do diretorio padrï¿½o de Upload.
	 *
	 * @return Retorna onde o arquivo de imagem foi salvo.
	 *
	 * @author Wendel Rodrigues
	 */
	public String executarDownloadImagemDiretorioPadraoUpload(String link, String diretorio) {
		try {

			String diretorioArquivo = diretorio + File.separator + retornarNomeDaImagem(link);

			final int BUFFER_SIZE = 1024 * 1024;
			URL url = new URL(link);
			BufferedInputStream stream = new BufferedInputStream(url.openStream(), BUFFER_SIZE);
			BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream(diretorioArquivo));
			byte buf[] = new byte[BUFFER_SIZE];
			int numBytesRead;
			do {
				numBytesRead = stream.read(buf);
				if (numBytesRead > 0) {
					fos.write(buf, 0, numBytesRead);
				}
			} while (numBytesRead > 0);
			fos.flush();
			fos.close();
			stream.close();
			buf = null;
			return diretorioArquivo;

		} catch (MalformedURLException e1) {
			e1.printStackTrace();
			return "";
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public static String removeCabecarioHTML(String htmlString) {
		String noHTMLString = htmlString;
		noHTMLString = noHTMLString.replaceAll("<html>", "");
		noHTMLString = noHTMLString.replaceAll("<head>", "");
		noHTMLString = noHTMLString.replaceAll("<title>", "");
		noHTMLString = noHTMLString.replaceAll("</title>", "");
		noHTMLString = noHTMLString.replaceAll("</head>", "");
		noHTMLString = noHTMLString.replaceAll("<body>", "");
		noHTMLString = noHTMLString.replaceAll("</body>", "");
		noHTMLString = noHTMLString.replaceAll("</html>", "");
		noHTMLString = noHTMLString.replaceAll("'", "&#39;");
		noHTMLString = noHTMLString.replaceAll("\"/", "&quot;");
		noHTMLString = noHTMLString.replaceAll("Untitled document", "");
		return noHTMLString;
	}

	public static String removeHTML(String htmlString) {
		String noHTMLString = htmlString.replaceAll("<.*?>", "");
		noHTMLString = noHTMLString.replaceAll("r", "<br/>");
		noHTMLString = noHTMLString.replaceAll("\n", " ");
		noHTMLString = noHTMLString.replaceAll("'", "&#39;");
		noHTMLString = noHTMLString.replaceAll("\"/", "&quot;");
		noHTMLString = noHTMLString.replaceAll("Untitled document", "");
		return noHTMLString;
	}

	public static Integer arredondarDivisaoEntreNumeros(Double valor) {
		String decimos = String.valueOf(valor).substring(String.valueOf(valor).indexOf(".") + 1, String.valueOf(valor).length());
		if (Double.parseDouble(decimos) > 0) {
			valor = valor + 1;
		}
		return valor.intValue();
	}

	public static Date getDataPassadaPorDataAtual(Integer quantidadeDias) throws Exception {
		try {
			Calendar data = Calendar.getInstance();
			data.add(Calendar.DAY_OF_MONTH, -quantidadeDias);
			data.set(Calendar.HOUR_OF_DAY, 0);
			data.set(Calendar.MINUTE, 0);
			data.set(Calendar.SECOND, 0);
			data.set(Calendar.MILLISECOND, 0);
			return data.getTime();
		} catch (Exception e) {
			return new Date();
		}
	}

	public static String getValorTruncadoDeDoubleParaString(Double val, int precision) {
		if (val == null) {
			return "null";
		}
		return trunc(new BigDecimal(val.toString(), MathContext.DECIMAL64), precision).toString();
	}

	public static BigDecimal trunc(BigDecimal val, int precision) {
		if (precision < 0) {
			throw new IllegalArgumentException("Preciso deve ser maior que zero");
		}
		return val.setScale(precision, BigDecimal.ROUND_DOWN);
	}

//	public static double arrendondarForcando2CasasDecimais(double valor) {
//		boolean numeroNegativo = false;
//		if (valor < 0) {
//			numeroNegativo = true;
//		}
//		valor = Uteis.arredondar(valor, 2, 1);
//		String valorStr = String.valueOf(valor);
//
//		String inteira = valorStr.substring(0, valorStr.indexOf("."));
//		String extensao = valorStr.substring(valorStr.indexOf(".") + 1, valorStr.length());
//		if (extensao.length() == 1) {
//			extensao += "0";
//		}
//		valorStr = UteisTexto.removerMascara(inteira) + "." + extensao;
//		if (!numeroNegativo) {
//			return Double.parseDouble(valorStr);
//		} else {
//			return (-1.0 * Double.parseDouble(valorStr));
//		}
//	}

	public static String adicionarMascaraCodigoBarraNFE(String valor) {
		String mascara = "XXXX.XXXX.XXXX.XXXX.XXXX.XXXX.XXXX.XXXX.XXXX.XXXX.XXXX";
		if (valor.length() == 44) {
			String parte1 = valor.substring(0, 4);
			String parte2 = valor.substring(4, 8);
			String parte3 = valor.substring(8, 12);
			String parte4 = valor.substring(12, 16);
			String parte5 = valor.substring(16, 20);
			String parte6 = valor.substring(20, 24);
			String parte7 = valor.substring(24, 28);
			String parte8 = valor.substring(28, 32);
			String parte9 = valor.substring(32, 36);
			String parte10 = valor.substring(36, 40);
			String parte11 = valor.substring(40, 44);
			return parte1 + "." + parte2 + "." + parte3 + "." + parte4 + "." + parte5 + "." + parte6 + "." + parte7 + "." + parte8 + "." + parte9 + "." + parte10 + "." + parte11;
		} else {
			return mascara;
		}
	}

	public static String getMontarCodigoBarra(String codigo, int tamanhoCodigoBarra) throws ConsistirException {
		int tamanhoCodigo = codigo.length();
		String codigoBarra = "";
		if (tamanhoCodigoBarra < tamanhoCodigo) {
			throw new ConsistirException("A Quantidade de caracteres do Cdigo de Barra esta pequeno");
		}
		int diferenca = tamanhoCodigoBarra - tamanhoCodigo;
		while (diferenca > 0) {
			codigoBarra = codigoBarra + "0";
			diferenca--;
		}

		return codigoBarra + codigo;
	}

	public static void realizarCopiaArquivo(String diretorioTemp, String diretorioOriginal, Boolean validarDiretorio) throws Exception {
		File arquivoTemp = new File(diretorioTemp);
		File arquivoOriginal = new File(diretorioOriginal);
		if (validarDiretorio) {
			if (!arquivoTemp.exists()) {
				arquivoTemp.getParentFile().mkdirs();
			}
			if (!arquivoOriginal.exists()) {
				arquivoOriginal.getParentFile().mkdirs();
			}
		}
		if (!arquivoOriginal.exists()) {
			InputStream in = new FileInputStream(arquivoTemp);
			OutputStream out = new FileOutputStream(arquivoOriginal, true);
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
		}
	}

	public static String getCasasDecimais(String numero, int quantidadeCasas) throws Exception {
		int tamanhoNumero = numero.length();
		int posicaoPonto = numero.indexOf(".");
		int quantidadeZeros = tamanhoNumero - (posicaoPonto + 1);
		while (quantidadeZeros < quantidadeCasas) {
			numero = numero + "0";
			quantidadeZeros++;
		}
		return numero;
	}

	public static String getPreencherComZeroEsquerda(String codigo, int tamanhoCodigoBarra) throws Exception {
		int tamanhoCodigo = codigo.length();
		String codigoBarra = "";

		int diferenca = tamanhoCodigoBarra - tamanhoCodigo;
		while (diferenca > 0) {
			codigoBarra = codigoBarra + "0";
			diferenca--;
		}

		return codigoBarra + codigo;
	}

	public static double arrendondarForcando4CadasDecimais(double valor) {
		valor = arredondar(valor, 4, 1);
		boolean negativo = valor < 0.0;
		String valorStr = String.valueOf(valor);
		if (!valorStr.contains(".")) {
			return valor;
		}
		String inteira = valorStr.substring(0, valorStr.indexOf("."));
		String extensao = valorStr.substring(valorStr.indexOf(".") + 1, valorStr.length());
		if (extensao.length() == 1) {
			extensao += "0";
		}
		valorStr = removerMascara(inteira) + "." + extensao;
		Double valorFinal = Double.parseDouble(valorStr);
		return negativo ? valorFinal * -1 : valorFinal;
	}

	public static String getFusoHorario() {
		Calendar c = Calendar.getInstance(TimeZone.getDefault());
		c.setTime(new Date());
		Integer offset = c.get(Calendar.DST_OFFSET);
		Integer fuso = 0;

		if (!offset.equals(0)) {
			fuso = TimeZone.getTimeZone("Brazil/DeNoronha").getRawOffset() / (60 * 60 * 1000);
		} else
			fuso = TimeZone.getDefault().getRawOffset() / (60 * 60 * 1000);

		if (fuso < 0) {
			if (fuso < -9) {
				return "" + String.valueOf(fuso) + ":00";
			} else {
				return "-0" + String.valueOf(fuso * -1) + ":00";
			}

		} else if (fuso > 9) {
			return "" + String.valueOf(fuso) + ":00";
		} else {
			return "0" + String.valueOf(fuso) + ":00";
		}
	}

	public static String getDataComHoraCompleta(java.util.Date dataConverter) {
		DateFormat formatador = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
		String dataStr = formatador.format(dataConverter);
		return dataStr;
	}

	public static java.util.Date getDateHoraInicialDia(Date dataPrm) throws Exception {
		java.util.Date valorData = null;
		DateFormat formatador = DateFormat.getDateInstance(DateFormat.SHORT);
		valorData = formatador.parse(Uteis.getData(dataPrm));
		Calendar cal = Calendar.getInstance();
		cal.setTime(valorData);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	public static Double realizarCalculoFormula(String formulaCalculoMediaFinal) {
		ScriptEngineManager factory = new ScriptEngineManager();
		// create a JavaScript engine
		ScriptEngine engine = factory.getEngineByName("JavaScript");
		// evaluate JavaScript code from String
		try {
			formulaCalculoMediaFinal = formulaCalculoMediaFinal.replaceAll(" e ", " && ");
			formulaCalculoMediaFinal = formulaCalculoMediaFinal.replaceAll(" E ", " && ");
			formulaCalculoMediaFinal = formulaCalculoMediaFinal.replaceAll(" ou ", " || ");
			formulaCalculoMediaFinal = formulaCalculoMediaFinal.replaceAll(" OU ", " || ");
			formulaCalculoMediaFinal = formulaCalculoMediaFinal.replaceAll("=", "==");
			formulaCalculoMediaFinal = formulaCalculoMediaFinal.replaceAll("====", "==");
			formulaCalculoMediaFinal = formulaCalculoMediaFinal.replaceAll(">==", ">=");
			formulaCalculoMediaFinal = formulaCalculoMediaFinal.replaceAll("<==", "<=");
			formulaCalculoMediaFinal = formulaCalculoMediaFinal.replaceAll("!==", "!=");
			formulaCalculoMediaFinal = formulaCalculoMediaFinal.replaceAll(",", ".");
			Object result = engine.eval(formulaCalculoMediaFinal);
			if (result instanceof Number) {
				return ((Number) result).doubleValue();
			} else if (result != null) {
				try {
					return Double.parseDouble(result.toString());
				} catch (NumberFormatException e) {

				}
			}
		} catch (ScriptException e) {

		}
		return null;
	}

	public static boolean validaTelefone(String telefone) {
		String tel = retirarMascaraTelefone(telefone);
		if (tel.length() < 10 || tel.length() > 11) {
			return false;
		}
		return true;
	}

	public static boolean validaCPF(String CPF) {
		if (CPF.contains("/")){
			return false;
		}
		CPF = removerMascara(CPF).trim();
		// considera-se erro CPF's formados por uma sequencia de numeros iguais
		if (CPF.equals("00000000000") || CPF.equals("11111111111") || CPF.equals("22222222222") || CPF.equals("33333333333") || CPF.equals("44444444444") || CPF.equals("55555555555") || CPF.equals("66666666666") || CPF.equals("77777777777") || CPF.equals("88888888888") || CPF.equals("99999999999") || (CPF.length() != 11))
			return (false);

		char dig10, dig11;
		int sm, i, r, num, peso;

		// "try" - protege o codigo para eventuais erros de conversao de tipo
		// (int)
		try {
			// Calculo do 1o. Digito Verificador
			sm = 0;
			peso = 10;
			for (i = 0; i < 9; i++) {
				// converte o i-esimo caractere do CPF em um numero:
				// por exemplo, transforma o caractere '0' no inteiro 0
				// (48 eh a posicao de '0' na tabela ASCII)
				num = (int) (CPF.charAt(i) - 48);
				sm = sm + (num * peso);
				peso = peso - 1;
			}

			r = 11 - (sm % 11);
			if ((r == 10) || (r == 11))
				dig10 = '0';
			else
				dig10 = (char) (r + 48); // converte no respectivo caractere
											// numerico

			// Calculo do 2o. Digito Verificador
			sm = 0;
			peso = 11;
			for (i = 0; i < 10; i++) {
				num = (int) (CPF.charAt(i) - 48);
				sm = sm + (num * peso);
				peso = peso - 1;
			}

			r = 11 - (sm % 11);
			if ((r == 10) || (r == 11))
				dig11 = '0';
			else
				dig11 = (char) (r + 48);

			// Verifica se os digitos calculados conferem com os digitos
			// informados.
			if ((dig10 == CPF.charAt(9)) && (dig11 == CPF.charAt(10)))
				return (true);
			else
				return (false);
		} catch (InputMismatchException erro) {
			return (false);
		}
	}

	public static boolean validaCNPJ(String CNPJ) {
		// considera-se erro CNPJ's formados por uma sequencia de numeros iguais
		CNPJ = retirarMascaraCNPJ(CNPJ);
		if (CNPJ.equals("00000000000000") || CNPJ.equals("11111111111111") ||
				CNPJ.equals("22222222222222") || CNPJ.equals("33333333333333") ||
				CNPJ.equals("44444444444444") || CNPJ.equals("55555555555555") ||
				CNPJ.equals("66666666666666") || CNPJ.equals("77777777777777") ||
				CNPJ.equals("88888888888888") || CNPJ.equals("99999999999999") ||
				(CNPJ.length() != 14))
			return (false);

		char dig13, dig14;
		int sm, i, r, num, peso;

		// "try" - protege o cdigo para eventuais erros de conversao de tipo (int)
		try {
			// Calculo do 1o. Digito Verificador
			sm = 0;
			peso = 2;
			for (i = 11; i >= 0; i--) {
				// converte o i-simo caractere do CNPJ em um nmero:
				// por exemplo, transforma o caractere '0' no inteiro 0
				// (48 eh a posio de '0' na tabela ASCII)
				num = (int) (CNPJ.charAt(i) - 48);
				sm = sm + (num * peso);
				peso = peso + 1;
				if (peso == 10)
					peso = 2;
			}

			r = sm % 11;
			if ((r == 0) || (r == 1))
				dig13 = '0';
			else
				dig13 = (char) ((11 - r) + 48);

			// Calculo do 2o. Digito Verificador
			sm = 0;
			peso = 2;
			for (i = 12; i >= 0; i--) {
				num = (int) (CNPJ.charAt(i) - 48);
				sm = sm + (num * peso);
				peso = peso + 1;
				if (peso == 10)
					peso = 2;
			}

			r = sm % 11;
			if ((r == 0) || (r == 1))
				dig14 = '0';
			else
				dig14 = (char) ((11 - r) + 48);

			// Verifica se os dgitos calculados conferem com os dgitos informados.
			if ((dig13 == CNPJ.charAt(12)) && (dig14 == CNPJ.charAt(13)))
				return (true);
			else
				return (false);
		} catch (InputMismatchException erro) {
			return (false);
		}
	}

	public static Date getDataInicioSemestreAno(Integer ano, Integer semestre) {
		if (ano > 0) {
			if (semestre > 0) {
				if (semestre.equals(1)) {
					return new GregorianCalendar(ano, 0, 1).getTime();
				} else {
					return new GregorianCalendar(ano, 6, 21).getTime();
				}
			}
			return new GregorianCalendar(ano, 0, 1).getTime();
		}
		return null;
	}

	public static Date getDataFimSemestreAno(Integer ano, Integer semestre) {
		if (ano > 0) {
			if (semestre > 0) {
				if (semestre.equals(1)) {
					return new GregorianCalendar(ano, 6, 20).getTime();
				} else {
					return new GregorianCalendar(ano, 11, 31).getTime();
				}
			}
			return new GregorianCalendar(ano, 11, 31).getTime();
		}
		return null;
	}

	public static boolean verificarFormulaEstaCorreta(String formula, boolean retornoTipoBoolean) {
		ScriptEngineManager factory = new ScriptEngineManager();
		// create a JavaScript engine
		ScriptEngine engine = factory.getEngineByName("JavaScript");
		// evaluate JavaScript code from String
		if (!formula.trim().isEmpty()) {
			Object result;
			try {
				formula = formula.replaceAll(" e ", " && ");
				formula = formula.replaceAll(" E ", " && ");
				formula = formula.replaceAll(" ou ", " || ");
				formula = formula.replaceAll(" OU ", " || ");
				formula = formula.replaceAll("=", "==");
				formula = formula.replaceAll("====", "==");
				formula = formula.replaceAll(">==", ">=");
				formula = formula.replaceAll("<==", "<=");
				formula = formula.replaceAll("!==", "!=");

				try {
					formula = ConfiguracaoAcademicoVO.validarUsoFuncaoMaior(formula, 0, "");
				} catch (ConsistirException e) {
					return false;
				}
				result = engine.eval(formula);
				if (retornoTipoBoolean) {
					return (result instanceof Boolean);
				}
				return validarResultadoNumerico(result);
			} catch (ScriptException e) {
				return false;
			}

		}
		return true;
	}

	/**
	 * @author Victor Hugo
	 */
	public static int arredondarParaMais(double valor) {
		valor = (new BigDecimal(valor).setScale(0, BigDecimal.ROUND_UP).intValue());
		return (int) valor;
	}

	public static java.util.Date getDateComHoraAntesDiaSeguinte(Date dataPrm) throws Exception {
		java.util.Date valorData = null;
		DateFormat formatador = DateFormat.getDateInstance(DateFormat.SHORT);
		valorData = formatador.parse(Uteis.getData(dataPrm));
		Calendar cal = Calendar.getInstance();

		cal.setTime(valorData);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);

		return cal.getTime();
	}

	public static long getDataQuantidadeDiasEntreDatasDesconsiderandoDiasUteis(Date dataIni, Date dataFim) throws Exception {
		GregorianCalendar ini = new GregorianCalendar();
		GregorianCalendar fim = new GregorianCalendar();
		ini.setTime(dataIni);
		fim.setTime(dataFim);
		long dt1 = ini.getTimeInMillis();
		long dt2 = fim.getTimeInMillis();
		long result = (int) (((dt2 - dt1) / 86400000) + 1);

		return result;
	}

	public static boolean isDate(Date dataValidar) {
		String dia;
		String mes;
		String ano;

		StringTokenizer token = new StringTokenizer(getData(dataValidar, "dd/MM/yyyy"), "/");
		try {
			dia = token.nextToken();
			mes = token.nextToken();
			ano = token.nextToken();

			if (dia.length() < 2 || dia.length() > 2)
				return false;
			if (mes.length() < 2 || mes.length() > 2)
				return false;
			if (ano.length() < 4 || dia.length() > 4)
				return false;

			int intDia = Integer.parseInt(dia);
			int intMes = Integer.parseInt(mes);
			int intAno = Integer.parseInt(ano);

			if (String.valueOf(intAno).length() < 4 || String.valueOf(intAno).length() > 4)
				return false;

			if (intMes < 1 || intMes > 12)
				return false;

			if (intMes == 1 || intMes == 3 || intMes == 5 || intMes == 7 || intMes == 8 || intMes == 12) {
				if (intDia < 1 || intDia > 31)
					return false;
			} else if (intMes == 4 || intMes == 6 || intMes == 9 || intMes == 10 || intMes == 11) {
				if (intDia < 1 || intDia > 30)
					return false;
			} else if (intMes == 2) {
				if (intDia < 1 || intDia > 29)
					return false;
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static String formataMascaraCEP(String cep) {
		String ret = "";
		int tam;
		try {
			if (cep != null) {
				tam = cep.length();
				if (tam == 8) {
					ret = cep.substring(0, tam - 6) + "." + cep.substring(tam - 6, tam - 3) + "-" + cep.substring(tam - 3);
				}
			}
		} catch (Exception err) {
		}
		return ret;
	}

	public static Boolean validarMascaraCEP(String CEP) {
		return CEP.matches("\\d{2}.\\d{3}-\\d{3}");
	}

	/**
	 * Responsvel por realizar a formatao de um valor Double de acordo com a quantidade mxima de casas decimais.
	 *
	 * @author Wellington 19/02/2015
	 * @param valor
	 * @param quantidadeCasasDecimaisPermitirAposVirgula
	 * @return
	 */
	public static String formatarDeAcordoQuantidadeCasasDecimaisAposVirgula(Double valor, int quantidadeCasasDecimaisPermitirAposVirgula) {
		if (valor == null) {
			return "";
		}
		final NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumFractionDigits(1);
		nf.setMaximumFractionDigits(quantidadeCasasDecimaisPermitirAposVirgula);
		String valorFinal = nf.format(valor);
		if (valorFinal.contains(",")) {
			while (valorFinal.substring(valorFinal.indexOf(",") + 1, valorFinal.length()).length() < quantidadeCasasDecimaisPermitirAposVirgula) {
				valorFinal += "0";
			}
		}
		return valorFinal;
	}

	public static Date getDataFuturaConsiderandoDataAtual(Date dataInicial, int quantidadeAvancar) throws Exception {
		dataInicial = getDateSemHora(dataInicial);
		if (quantidadeAvancar == 0) {
			return dataInicial;
		}
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(getDataPassada(dataInicial, 0));
		gc.add(GregorianCalendar.DAY_OF_MONTH, quantidadeAvancar);
		return getDateSemHora(gc.getTime());
	}

	public static <T extends SuperVO> List<ResultadoConjuntoValoresVO<T>> identificarContasIguaisValorOperacao1(List<T> possiveis_contas, Double valor_operacao, String identificador, String campoValor) {
		//HashMap<String, List<T>> mapa = new HashMap<>();
		List<ResultadoConjuntoValoresVO<T>> mapa =  new ArrayList<>();
		try {
			identificarContasIguaisValorOperacaoComRecursividade1(mapa, possiveis_contas, 0.0, 0, valor_operacao, identificador, campoValor, new ArrayList<>());
		} catch (StreamSeiException e) {
			if (mapa.size() != 1) {
				throw e;
			}
		}
		List<ResultadoConjuntoValoresVO<T>> mapaOrdenado = mapa.stream()
				.sorted(Comparator.comparingDouble(ResultadoConjuntoValoresVO::getValoresDoConjunto))
				.collect(Collectors.toList());
		return mapaOrdenado;
	}

	private static <T extends SuperVO> void identificarContasIguaisValorOperacaoComRecursividade1(List<ResultadoConjuntoValoresVO<T>> mapa, List<T> possiveis_contas, Double valorCorrente, Integer index, Double valor_operacao, String identificador, String campoValor, List<Integer> mapaPosicoes) {
		if (valorCorrente.equals(valor_operacao)) {
			mapa.clear();
			montarMapaResultadoConjuntoValores(mapa, possiveis_contas, mapaPosicoes, identificador, campoValor);
			throw new StreamSeiException("Encontrou valor Igual");
		} else if (valorCorrente > valor_operacao) {
			montarMapaResultadoConjuntoValores(mapa, possiveis_contas, mapaPosicoes, identificador, campoValor);
			return;
		} else if (index == possiveis_contas.size()) {
			return;
		} else {
			mapaPosicoes.add(index);
			Double campoValorTemp = getObterValorTemporario(possiveis_contas, index, campoValor);
			valorCorrente = Uteis.arrendondarForcando2CadasDecimais(valorCorrente + campoValorTemp);
			identificarContasIguaisValorOperacaoComRecursividade1(mapa, possiveis_contas, valorCorrente, index + 1, valor_operacao, identificador, campoValor, mapaPosicoes);
			valorCorrente = Uteis.arrendondarForcando2CadasDecimais(valorCorrente - campoValorTemp);
			mapaPosicoes.remove(index);
			identificarContasIguaisValorOperacaoComRecursividade1(mapa, possiveis_contas, valorCorrente, index + 1, valor_operacao, identificador, campoValor, mapaPosicoes);
		}
		return;
	}

	private static <T extends SuperVO> void montarMapaResultadoConjuntoValores(List<ResultadoConjuntoValoresVO<T>> mapa, List<T> possiveis_contas, List<Integer> mapaPosicoes, String identificador, String campoValor) {
		List<T> agrupamento = new ArrayList<>();
		StringBuilder chave = new StringBuilder();
		Double valor = 0.0;
		ResultadoConjuntoValoresVO rcv = new ResultadoConjuntoValoresVO();
		for (Integer posicao : mapaPosicoes) {
			SuperVO obj = possiveis_contas.get(posicao);
			agrupamento.add((T) obj);
			if(Uteis.isAtributoPreenchido(chave)) {
				chave.append(",").append((Integer) UtilReflexao.invocarMetodoGet(obj, identificador));
			}else {
				chave.append((Integer) UtilReflexao.invocarMetodoGet(obj, identificador));
			}
			valor = valor + (Double) UtilReflexao.invocarMetodoGet(obj, campoValor);
		}
		rcv.setCodigosDoConjunto(chave.toString());
		rcv.setListaDeConjunto(agrupamento);
		rcv.setValoresDoConjunto(valor);
		mapa.add(rcv);
	}

	private static <T extends SuperVO> Double getObterValorTemporario(List<T> possiveis_contas, Integer index, String campoValor) {
		SuperVO obj = possiveis_contas.get(index);
		Double campoValorTemp = (Double) UtilReflexao.invocarMetodoGet(obj, campoValor);
		if (campoValorTemp < 0.0) {
			campoValorTemp = campoValorTemp * -1;
		}
		return campoValorTemp;
	}

	public static <T extends SuperVO> HashMap<String, List<T>> identificarContasIguaisValorOperacao(List<T> possiveis_contas, Double valor_operacao, String identificador, String campoValor) {
		HashMap<String, List<T>> mapa = new HashMap<String, List<T>>();
		identificarContasIguaisValorOperacaoComRecursividade(possiveis_contas, valor_operacao, new ArrayList<T>(), identificador, campoValor, mapa, "IGUAL");
		return mapa;
	}

	private static <T extends SuperVO> void identificarContasIguaisValorOperacaoComRecursividade(List<T> possiveis_contas, Double valor_operacao, List<T> possiveis_contas_parciais, String identificador, String campoValor, HashMap<String, List<T>> mapa, String tipoOperacao) {
		Double campoValorTemp = 0.0;
		List<T> agrupamento = new ArrayList<>();
		StringBuilder chave = new StringBuilder();
		Double total = 0.0;
		for (SuperVO obj : possiveis_contas_parciais) {
			campoValorTemp = (Double) UtilReflexao.invocarMetodoGet(obj, campoValor);
			if (campoValorTemp < 0.0) {
				campoValorTemp = campoValorTemp * -1;
			}
			total = total + campoValorTemp;
			total = Uteis.arrendondarForcando2CadasDecimais(total + campoValorTemp);
			agrupamento.add((T) obj);
			chave.append((Integer) UtilReflexao.invocarMetodoGet(obj, identificador)).append(",");
		}
		if (tipoOperacao.equals("IGUAL") || tipoOperacao.equals("MENOR")) {
			if (tipoOperacao.equals("IGUAL") && total.equals(valor_operacao)) {
				mapa.put(chave.toString(), agrupamento);
				return;
			}
			if (tipoOperacao.equals("MENOR") && total != 0.0 && total < valor_operacao) {
				mapa.put(chave.toString(), agrupamento);
			}
			if (total > valor_operacao) {
				return;
			}
		} else if (tipoOperacao.equals("MAIOR") && total != 0.0 && total > valor_operacao) {
			mapa.put(chave.toString(), agrupamento);
			return;
		}
		for (int i = 0; i < possiveis_contas.size(); i++) {
			List<T> remaining = new ArrayList<>();
			List<T> partial_rec = new ArrayList<>(possiveis_contas_parciais);
			T n = possiveis_contas.get(i);
			partial_rec.add(n);
			for (int j = i + 1; j < possiveis_contas.size(); j++) {
				remaining.add(possiveis_contas.get(j));
			}
			identificarContasIguaisValorOperacaoComRecursividade(remaining, valor_operacao, partial_rec, identificador, campoValor, mapa, tipoOperacao);
		}

	}

	public static Integer getIdObjetoPersistir(Object objeto, String keyName) throws Exception {

		if (objeto != null) {
			Integer i = (Integer) UtilReflexao.invocarMetodoGet(objeto, keyName);
			return (i != null && !i.equals(0)) ? i : null;
		}
		return null;
	}

	public static String getCaminhoRedirecionamentoNavegacao(String caminhoPagina) {
		if(Uteis.isAtributoPreenchido(caminhoPagina)) {
			if(caminhoPagina.contains("?")) {
				return caminhoPagina + "&faces-redirect=true";
			}
			return caminhoPagina + "?faces-redirect=true";
		}
		return "";
	}

	public static String getCaminhoRedirecionamentoNavegacao(String caminhoPagina, String controlador) {
		return caminhoPagina + "?faces-redirect=true&idControlador=" + controlador;
	}

	public static Double realizarCalculoFormulaCalculo(String formula) throws ScriptException {
		ScriptEngineManager factory = new ScriptEngineManager();
		// create a JavaScript engine
		ScriptEngine engine = factory.getEngineByName("JavaScript");
		// evaluate JavaScript code from String
		Object result;
		formula = formula.replaceAll(" e ", " && ");
		formula = formula.replaceAll(" E ", " && ");
		formula = formula.replaceAll(" ou ", " || ");
		formula = formula.replaceAll(" OU ", " || ");
		formula = formula.replaceAll("=", "==");
		formula = formula.replaceAll("====", "==");
		formula = formula.replaceAll(">==", ">=");
		formula = formula.replaceAll("<==", "<=");
		formula = formula.replaceAll("!==", "!=");

		result = engine.eval(formula);
		if (result instanceof Number) {
			return ((Number) result).doubleValue();
		} else if (result != null) {
			try {
				return Double.parseDouble(result.toString());
			} catch (NumberFormatException e) {

			}
		}
		return null;
	}
	public static boolean  getValidarDataFormatadaCorretamente(String dataStr, String formato) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(formato);
			Date data = sdf.parse(dataStr);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static Date getDataYYYMMDD(String dataStr) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date data = sdf.parse(dataStr);
			return data;
		} catch (Exception e) {
			return new Date();
		}
	}

	/**
	 * Responsvel por realizar a formatao de um valor Double de acordo com a quantidade mxima de casas decimais.
	 *
	 * @author Wellington Rodrigues - 17/06/2015
	 * @param valor
	 * @param quantidadeCasasDecimaisPermitirAposVirgula
	 * @return
	 */
	public static Double formatarDeAcordoQuantidadeCasasDecimaisAposVirgulaDouble(Double valor, int quantidadeCasasDecimaisPermitirAposVirgula) {
		if (valor == null) {
			return null;
		}
		final NumberFormat nf = NumberFormat.getInstance();
		if (quantidadeCasasDecimaisPermitirAposVirgula == 0) {
			nf.setMinimumFractionDigits(0);
		} else {
			nf.setMinimumFractionDigits(1);
		}
		nf.setMaximumFractionDigits(quantidadeCasasDecimaisPermitirAposVirgula);
		return Double.parseDouble(nf.format(valor).replaceAll("\\.", "").replace(",", "."));
	}

	public static String removerCaracteresEspeciais3(String valor) {
		String retorno = valor;
		retorno = Normalizer.normalize(retorno, Normalizer.Form.NFD);
		retorno = retorno.replaceAll("[^\\p{ASCII}]", "");
		return retorno;
	}

	public static String generoEstado(String estado) throws Exception {
		String generode = "Estado de ";
		String generodo = "Estado do ";
		String generoda = "Estado da ";
		String generoretorno = "";
		if (estado.equalsIgnoreCase("AC") || estado.equalsIgnoreCase("AP") || estado.equalsIgnoreCase("AM") || estado.equalsIgnoreCase("ES") || estado.equalsIgnoreCase("CE") || estado.equalsIgnoreCase("MA") || estado.equalsIgnoreCase("PR") || estado.equalsIgnoreCase("PI") || estado.equalsIgnoreCase("RJ") || estado.equalsIgnoreCase("RN") || estado.equalsIgnoreCase("RS") || estado.equalsIgnoreCase("TO") || estado.equalsIgnoreCase("MT") || estado.equalsIgnoreCase("MS") || estado.equalsIgnoreCase("PA")) {
			generoretorno = generodo;
		} else if (estado.equalsIgnoreCase("BA") || estado.equalsIgnoreCase("PB")) {
			generoretorno = generoda;
		} else if (estado.equalsIgnoreCase("DF")) {
			generoretorno = "";
		} else {
			generoretorno = generode;
		}
		return generoretorno;
	}

	public static String substituirVogaisAcentuadasPoUnicode(String texto) {
		char[] chars = texto.toCharArray();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < chars.length; i++) {
			int unipoint = Character.codePointAt(chars, i);
			if ((unipoint < 32) || (unipoint > 127)) {
				StringBuilder hexString = new StringBuilder();
				for (int k = 0; k < 4; k++) {
					hexString.insert(0, Integer.toHexString(unipoint % 16));
					unipoint = unipoint / 16;
				}
				sb.append("\\u" + hexString);
			} else {
				sb.append(chars[i]);
			}
		}
		return sb.toString();
	}

	public static String formatarDeAcordoQuantidadeCasasDecimaisAposVirgulaStr(Double valor, int quantidadeCasasDecimaisPermitirAposVirgula, boolean isConsiderarZero) {
		if (!isAtributoPreenchido(valor, isConsiderarZero)) {
			return null;
		}
		final NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumFractionDigits(1);
		nf.setMaximumFractionDigits(quantidadeCasasDecimaisPermitirAposVirgula);
		return nf.format(valor).replace(",", ".");
	}

	public static String formatarDeAcordoQuantidadeCasasDecimaisAposVirgulaStr(Double valor, int quantidadeCasasDecimaisPermitirAposVirgula) {
		return formatarDeAcordoQuantidadeCasasDecimaisAposVirgulaStr(valor, quantidadeCasasDecimaisPermitirAposVirgula, false);
	}

	public static BigDecimal formatarDeAcordoQuantidadeCasasDecimaisAposVirgula2(Double valor, int quantidadeCasasDecimaisPermitirAposVirgula) {
		if (valor == null || valor == 0.0) {
			return new BigDecimal(0.00).setScale(quantidadeCasasDecimaisPermitirAposVirgula, RoundingMode.HALF_UP);
		}
		final NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumFractionDigits(2);
		nf.setMaximumFractionDigits(quantidadeCasasDecimaisPermitirAposVirgula);
		return new BigDecimal(valor).setScale(quantidadeCasasDecimaisPermitirAposVirgula, RoundingMode.HALF_UP);
	}

	public static BigDecimal formatarDeAcordoQuantidadeCasasDecimaisAposVirgula2(BigDecimal valor, int quantidadeCasasDecimaisPermitirAposVirgula) {
		if (valor == null || valor == BigDecimal.ZERO) {
			return new BigDecimal(0.00).setScale(quantidadeCasasDecimaisPermitirAposVirgula, RoundingMode.HALF_UP);
		}
		final NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumFractionDigits(2);
		nf.setMaximumFractionDigits(quantidadeCasasDecimaisPermitirAposVirgula);
		return valor.setScale(quantidadeCasasDecimaisPermitirAposVirgula, RoundingMode.HALF_UP);
	}

	public static String formatarDeAcordoQuantidadeCasasDecimaisAposVirgulaStr2(Double valor, int quantidadeCasasDecimaisPermitirAposVirgula) {
		if (!isAtributoPreenchido(valor)) {
			return null;
		}
		final NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumFractionDigits(1);
		nf.setMaximumFractionDigits(quantidadeCasasDecimaisPermitirAposVirgula);
		return nf.format(valor);
	}

	public static String removerZeroEsquerda(String valor) {
		return valor.replaceFirst("0*", "");
	}

	public static void criarDiretoriosDeArquivos(ConfiguracaoGeralSistemaVO configGeralVO) {
		File dir = null;
		for (PastaBaseArquivoEnum pasta : PastaBaseArquivoEnum.values()) {
			dir = new File(configGeralVO.getLocalUploadArquivoFixo() + File.separator + getCaminhoRecursivo(pasta));
			if (!dir.exists()) {
				AplicacaoControle.realizarEscritaTextoDebug(AssuntoDebugEnum.UPLOAD, "criando pasta " + dir.getAbsolutePath());
				dir.mkdirs();
			}
		}
	}

	public static String getCaminhoRecursivo(PastaBaseArquivoEnum pasta) {
		String caminho = "";
		if (pasta.getDiretorioSuperior() != null) {
			caminho += getCaminhoRecursivo(pasta.getDiretorioSuperior()) + File.separator;
		}
		return caminho + pasta.getValue();
	}

	public static Boolean realizarFormulaCondicaoUso(String formulaCalculoMediaFinal) {
		ScriptEngineManager factory = new ScriptEngineManager();
		// create a JavaScript engine
		ScriptEngine engine = factory.getEngineByName("JavaScript");
		// evaluate JavaScript code from String
		try {
			formulaCalculoMediaFinal = formulaCalculoMediaFinal.replaceAll(" e ", " && ");
			formulaCalculoMediaFinal = formulaCalculoMediaFinal.replaceAll(" E ", " && ");
			formulaCalculoMediaFinal = formulaCalculoMediaFinal.replaceAll(" ou ", " || ");
			formulaCalculoMediaFinal = formulaCalculoMediaFinal.replaceAll(" OU ", " || ");
			formulaCalculoMediaFinal = formulaCalculoMediaFinal.replaceAll("=", "==");
			formulaCalculoMediaFinal = formulaCalculoMediaFinal.replaceAll("====", "==");
			formulaCalculoMediaFinal = formulaCalculoMediaFinal.replaceAll(">==", ">=");
			formulaCalculoMediaFinal = formulaCalculoMediaFinal.replaceAll("<==", "<=");
			formulaCalculoMediaFinal = formulaCalculoMediaFinal.replaceAll("!==", "!=");

			Object result = engine.eval(formulaCalculoMediaFinal);
			if (result instanceof Boolean) {
				return (Boolean) result;
			}
		} catch (ScriptException e) {
		}
		return false;
	}

	public static Boolean getIsValorNumerico(String valor) {
		return Uteis.isAtributoPreenchido(valor) && valor.replace("0", "").replace("1", "").replace("2", "").replace("3", "").replace("4", "").replace("5", "").replace("6", "").replace("7", "").replace("8", "").replace("9", "").replace(".", "").trim().isEmpty();
	}

	/**
	 *
	 * Mtodo para gerar data de vencimento considerando que se for fevereiro e o dia for maior que o mximo de dias presente no ms, ento o vencimento ser definido para o ltimo dia.
	 *
	 * @author Marcos Paulo - 9 de mar de 2017
	 * @param diaPadrao
	 * @param dataPadrao
	 * @param nrMesesProgredir
	 * @return
	 * @throws Exception
	 */
	public static Date getDataVencimentoPadraoConsiderandoFevereiro(Integer diaPadrao, Date dataPadrao, int nrMesesProgredir) throws Exception {
		int mes = Uteis.getMesData(dataPadrao);
		int ano = Uteis.getAnoData(dataPadrao);
		String diaFinal = String.valueOf(diaPadrao);

		mes = mes + nrMesesProgredir;
		if (mes > 12) {
			mes = mes - 12;
			ano = ano + 1;
		}

		String mesFinal = String.valueOf(mes);
		String anoFinal = String.valueOf(ano);

		if (String.valueOf(mesFinal).length() == 1) {
			mesFinal = "0" + mesFinal;
		}
		if (String.valueOf(diaFinal).length() == 1) {
			diaFinal = "0" + diaFinal;
		}

		GregorianCalendar gc = new GregorianCalendar();

		if (mesFinal.equals("02") && ((gc.isLeapYear(ano) && Integer.parseInt(diaFinal) > 29) || (!gc.isLeapYear(ano) && Integer.parseInt(diaFinal) > 28))) {
			if (gc.isLeapYear(ano)) {
				diaFinal = "29";
			} else {
				diaFinal = "28";
			}
		}

		String dataFinal = diaFinal + "/" + mesFinal + "/" + anoFinal;
		return Uteis.getDateSemHora(Uteis.getDate(dataFinal));
	}

	public static Integer isQtdLinhaSql(SqlRowSet rs) {
		rs.last();
		Integer qtdLinhas = rs.getRow();
		rs.beforeFirst();
		return qtdLinhas;
	}

	public static boolean isAtributoPreenchido(SqlRowSet rs, String campo, TipoCampoEnum tipoCampo) {
		if (tipoCampo.isInteiro()) {
			Integer qtd = rs.next() ? rs.getInt(campo) : null;
			return qtd != null && qtd != 0;
		}
		return false;
	}

	public static Object getSqlRowSetTotalizador(SqlRowSet rs, String campo, TipoCampoEnum tipoCampo) {
		if (tipoCampo.isData()) {
			return rs.next() ? rs.getDate(campo) : new Date();
		}
		if (tipoCampo.isInteiro()) {
			return rs.next() ? rs.getInt(campo) : 0;
		}
		if (tipoCampo.isDouble()) {
			return rs.next() ? rs.getDouble(campo) : 0.0;
		}
		if (tipoCampo.isBigDecimal()) {
			return rs.next() ? rs.getBigDecimal(campo) : BigDecimal.ZERO;
		}
		return 0;
	}



	/**
	 * Utilizar rotina quando tiver que persistir campo date como data os Campos a serem persistido forem diferente de Date pois por default o campo a ser pesistido para Date e TimeStamp rotina utilizar a sobrePosicao passando nulo para o parametro type.
	 *
	 * @param valor
	 * @param index
	 * @param preparedStatement
	 * @throws SQLException
	 */
//	@Deprecated
//	public static <P extends Object> void setValuePreparedStatement(P valor, int index, PreparedStatement preparedStatement) throws SQLException {
//		setValuePreparedStatement(valor, null, index, preparedStatement);
//	}	
//	
	
	
	
	/**
	 * Rotina de sobrePosicao para atender a pesistencia de um campo Date para escolher se sera persistido com Data ou TimeStamp
	 *
	 * @param valor
	 * @param type
	 * @param index
	 * @param preparedStatement
	 * @throws SQLException
	 */
	@Deprecated
	public static <P extends Object> void setValuePreparedStatement(P valor, int index, PreparedStatement preparedStatement) throws SQLException {
		setValuePreparedStatement(valor, null, index, preparedStatement);
	}
	
	public static <P extends Object> void setValuePreparedStatement(P valor, int index, PreparedStatement preparedStatement, Connection arg0) throws SQLException {
		setValuePreparedStatement(valor, null, index, preparedStatement, arg0);
	}
	
	
	
	@Deprecated
	public static <P extends Object> void setValuePreparedStatement(P valor, Integer type, int index, PreparedStatement preparedStatement) throws SQLException {
		setValuePreparedStatement(valor, null, index, preparedStatement, null);
	}
	
	public static <P extends Object> void setValuePreparedStatement(P valor, Integer type, int index, PreparedStatement preparedStatement, Connection arg0) throws SQLException {
		if (Objects.isNull(valor)) {
			preparedStatement.setNull(index, Types.NULL);
			return;
		}

		if (valor instanceof SuperVO) {
			Integer codigo = (Integer) UtilReflexao.invocarMetodoGet(valor, "codigo");
			if (isAtributoPreenchido(codigo) && isAtributoPreenchido(codigo)) {
				preparedStatement.setInt(index, codigo);
			} else {
				preparedStatement.setNull(index, Types.NULL);
			}
			return;
		}
		
		if (valor instanceof Integer []) {
			if ((Integer []) valor != null) {
				preparedStatement.setArray(index, arg0.createArrayOf("int", (Integer []) valor));
			} else {
				preparedStatement.setNull(index, Types.NULL);
			}
			return;
		}

		if (valor instanceof String) {
			preparedStatement.setString(index, (String) valor);
			return;
		}

		if (valor instanceof Integer) {
			if (isAtributoPreenchido((Integer) valor, true)) {
				preparedStatement.setInt(index, (Integer) valor);
			}
			return;
		}

		if (valor instanceof Long) {
			preparedStatement.setLong(index, (Long) valor);
			return;
		}

		if (valor instanceof Float) {
			preparedStatement.setFloat(index, (Float) valor);
			return;
		}

		if (valor instanceof BigDecimal) {
			preparedStatement.setBigDecimal(index, (BigDecimal) valor);
			return;
		}

		if (valor instanceof java.sql.Date) {
			preparedStatement.setDate(index, (java.sql.Date) valor);
			return;
		}

		if (valor instanceof java.sql.Timestamp) {
			preparedStatement.setTimestamp(index, (java.sql.Timestamp) valor);
			return;
		}

		if (valor instanceof Date) {
			if (type != null && type.equals(Types.DATE)) {
				preparedStatement.setDate(index, Uteis.getDataJDBC((Date) valor));
				return;
			}
			preparedStatement.setTimestamp(index, Uteis.getDataJDBCTimestamp((Date) valor));
			return;
		}

		if (valor instanceof Boolean) {
			preparedStatement.setBoolean(index, (Boolean) valor);
			return;
		}

		if (valor instanceof Double) {
			preparedStatement.setDouble(index, (Double) valor);
			return;
		}

		if (valor instanceof Enum<?>) {
			if (((Enum<?>) valor) != null && ((Enum<?>) valor).name() != null) {
				preparedStatement.setString(index, ((Enum<?>) valor).name());
			} else {
				preparedStatement.setNull(index, Types.NULL);
			}
			return;
		}

	}

	public static final Pattern PATTERN_DIACRITICOS = Pattern.compile("[\\p{InCombiningDiacriticalMarks}\\p{IsLm}\\p{IsSk}]+");

	public static String substituirCaracteresDiacriticos(String str) {
		str = Normalizer.normalize(str, Normalizer.Form.NFD);
		str = PATTERN_DIACRITICOS.matcher(str).replaceAll("");
		return str;
	}

	public static <P extends Object> P montarDadosVO(Integer codigo, Class<P> clazz, FunctionSEI<Integer, P> function) throws Exception {
		if (Objects.isNull(codigo) || codigo == 0) {
			return clazz.newInstance();
		}
		return function.apply(codigo);
	}

	public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
		Map<Object, String> seen = new ConcurrentHashMap<>();
		return t -> seen.put(keyExtractor.apply(t), "") == null;
	}

	public static void validarSenha(ConfiguracaoGeralSistemaVO configuracaoGeralDoSistema, String senhaDigitada) throws ConsistirException {
		ConsistirException ex = new ConsistirException();
		StringBuilder resposta = new StringBuilder();
		Pattern p = null;

		if (!Uteis.isAtributoPreenchido(senhaDigitada)) {
			ex.adicionarListaMensagemErro("O campo Senha deve ser informado. \n");
		}

		if (senhaDigitada.length() < configuracaoGeralDoSistema.getQuantidadeCaracteresMinimoSenhaUsuario()) {
			ex.adicionarListaMensagemErro("A senha deve ter o tamanho mnino de " + configuracaoGeralDoSistema.getQuantidadeCaracteresMinimoSenhaUsuario() + " Dgitos \n");

		}

		if (configuracaoGeralDoSistema.getNivelSegurancaNumero()) {
			p = Pattern.compile("((?=.*\\d)|(?=.*\\W+)).*$");
			Matcher m = p.matcher(senhaDigitada);
			if (!m.matches()) {
				ex.adicionarListaMensagemErro("A Senha deve conter pelo menos um Nmero \n");
			}
		}
		if (configuracaoGeralDoSistema.getNivelSegurancaLetra()) {
			p = Pattern.compile("(?=.*[a-z]).*$");
			Matcher m = p.matcher(senhaDigitada);
			if (!m.matches()) {
				ex.adicionarListaMensagemErro("A Senha deve conter pelo menos uma Letra Minscula \n");
			}
		}
		if (configuracaoGeralDoSistema.getNivelSegurancaLetraMaiuscula()) {
			p = Pattern.compile("(?=.*[A-Z]).*$");
			Matcher m = p.matcher(senhaDigitada);
			if (!m.matches()) {
				ex.adicionarListaMensagemErro("A Senha deve conter pelo menos uma Letra Maiscula \n");
			}
		}
		if (configuracaoGeralDoSistema.getNivelSegurancaCaracterEspecial()) {
			p = Pattern.compile("(?=.*[@#$%^&+=*{}()`~:;?]).*$");
			Matcher m = p.matcher(senhaDigitada);
			if (!m.matches()) {
				ex.adicionarListaMensagemErro("A Senha deve conter pelo menos um Caracter Especial \n");
			}
		}

		if (!ex.getListaMensagemErro().isEmpty()) {
			throw ex;
		} else {
			ex = null;
		}
	}

	public static String removeCaractersEspeciaisParaNomePessoa(String string) {
		if (!string.contains("''")) {
			if (string.contains("'")) {
				string = string.replaceAll("'", "''");
			}
		}
		string = string.replaceAll("\\\\", "");
		string = string.replaceAll("['<>|/]", "");
		string = string.replaceAll("[#$%&*()_+={}?.,:;^~`@!\"]", "");
		return string;
	}

	public static String removerAcentuacaoLimitarTamanho(String valor, int tamanho) {
		valor = Uteis.removerAcentuacao(valor).trim();
		if (valor.length() > tamanho) {
			valor = valor.substring(0, tamanho);
		}
		return valor;
	}

	public static String adicionarMascaraCPFConformeTamanhoCampo(String cpfSemMascara) {
		StringBuilder cpfComMascara = new StringBuilder("");
		cpfSemMascara = cpfSemMascara.replaceAll(" ", "");
		adicionarMascaraCPFConformeTamanhoCampo(retirarMascaraCPF(cpfSemMascara), 1, cpfComMascara);
		return cpfComMascara.toString();
	}

	private static void adicionarMascaraCPFConformeTamanhoCampo(String cpfSemMascara, Integer indice, StringBuilder cpfComMascara) {
		if (indice > cpfSemMascara.length() || indice < 1 || indice > 11) {
			return;
		}
		if (indice % 3 == 0 && indice != 9) {
			cpfComMascara.append(cpfSemMascara.charAt(indice - 1) + ".");
		} else if (indice == 9) {
			cpfComMascara.append(cpfSemMascara.charAt(indice - 1) + "-");
		} else {
			cpfComMascara.append(cpfSemMascara.charAt(indice - 1));
		}
		adicionarMascaraCPFConformeTamanhoCampo(cpfSemMascara, ++indice, cpfComMascara);
	}

	public static String adicionarMascaraCEPConformeTamanhoCampo(String cepSemMascara) {
		StringBuilder cepComMascara = new StringBuilder("");
		cepSemMascara = cepSemMascara.replaceAll(" ", "");
		cepSemMascara = cepSemMascara.replaceAll("[^\\d.]", "");
		adicionarMascaraCEPConformeTamanhoCampo(retirarMascaraCPF(cepSemMascara), 1, cepComMascara);
		return cepComMascara.toString();
	}

	private static void adicionarMascaraCEPConformeTamanhoCampo(String cepSemMascara, Integer indice, StringBuilder cepComMascara) {
		if (indice > cepSemMascara.length() || indice < 1 || indice > 8) {
			return;
		}
		switch (indice) {
		case 2:
			cepComMascara.append(cepSemMascara.charAt(indice - 1) + ".");
			break;
		case 5:
			cepComMascara.append(cepSemMascara.charAt(indice - 1) + "-");
			break;
		default:
			cepComMascara.append(cepSemMascara.charAt(indice - 1));
			break;
		}
		adicionarMascaraCEPConformeTamanhoCampo(cepSemMascara, ++indice, cepComMascara);
	}

	private static Boolean validarResultadoNumerico(Object object) {
		if (object instanceof Number) {
			return Boolean.TRUE;
		} else if (object != null && object instanceof String) {
			try {
				Double.valueOf(object.toString());
				return Boolean.TRUE;
			} catch (NumberFormatException e) {
			}
		}
		return Boolean.FALSE;
	}

	public static double arrendondarForcandoCadasDecimais(double valor, int casasDecimais) {
		BigDecimal bd = BigDecimal.valueOf(valor);
		boolean negativo = valor < 0.0;
		if (valor > 0.0) {
			bd = bd.setScale(casasDecimais,  BigDecimal.ROUND_HALF_UP);
		}
		String valorStr = bd.toString();
		if (!valorStr.contains(".")) {
			return valor;
		}
		String inteira = valorStr.substring(0, valorStr.indexOf("."));
		String extensao = valorStr.substring(valorStr.indexOf(".") + 1, valorStr.length());
		if (extensao.length() == 1) {
			extensao += "0";
		}
		valorStr = removerMascara(inteira) + "." + extensao;
		Double valorFinal = Double.parseDouble(valorStr);
		return negativo ? valorFinal * -1 : valorFinal;
	}

	public static void imprimirStackTrace(String identificador) {
		System.out.println("Imprimindo StackTrace [identificador = " + identificador + "]:");
		StackTraceElement[] elements = Thread.currentThread().getStackTrace();
		for (StackTraceElement s : Thread.currentThread().getStackTrace()) {
			if (!s.getClassName().startsWith("java") &&
					!s.getClassName().startsWith("sun") &&
					!s.getClassName().startsWith("org") &&
					!s.getClassName().startsWith("com")) {
				System.out.println("\tat " + s.getClassName() + "." + s.getMethodName() + "(" + s.getFileName() + ":" + s.getLineNumber() + ")");
			}
		}
	}

	public static boolean compararBigDecimalIgualandoScaleRoundingMode(BigDecimal big1, BigDecimal big2, int escala, RoundingMode roundingMode) {
		BigDecimal bigProcessado1 = big1.setScale(escala, roundingMode);
		BigDecimal bigProcessado2 = big2.setScale(escala, roundingMode);
		return bigProcessado1.equals(bigProcessado2);
	}

	public static Function<Double, BigDecimal> converterDoubleParaBigDecimal() {
		return valor -> new BigDecimal(String.valueOf(valor));
	}

	public static String periodoEntreDatas(Date dataInicial, Date dataFinal, String pattern) {
    	return new StringBuilder()
    			.append(getData(dataInicial, pattern))
    			.append(" ")
    			.append(UteisJSF.internacionalizar("prt_ate"))
    			.append(" ")
    			.append(getData(dataFinal, pattern))
    			.toString();
    }

	public static final String STRING_VAZIA  = "";

	public static Integer getExpiracaoDia(Date dataInicial) {
		Calendar calendarInicial = Calendar.getInstance();
		calendarInicial.setTime(dataInicial);
		Calendar dataFinal = Calendar.getInstance();
		dataFinal.set(Calendar.HOUR_OF_DAY, 23);
		dataFinal.set(Calendar.MINUTE, 55);
		dataFinal.set(Calendar.SECOND, 59);
		dataFinal.set(Calendar.MILLISECOND, 59);
		Long segundos = (dataFinal.getTimeInMillis() -  calendarInicial.getTimeInMillis()) / 1000;
		return segundos.intValue();
	}


	public static  String pegarTempoEntreDuasDatas(Date dataInicial, Date dataFinal) {

		 long  TEMPOLOCALSTORAGE = dataInicial.getTime() -  dataFinal.getTime();
		  long segundos = ( TEMPOLOCALSTORAGE / 1000 ) % 60;
		  long minutos  = ( TEMPOLOCALSTORAGE / 60000 ) % 60;     // 60000   = 60 * 1000
		  long horas    = TEMPOLOCALSTORAGE / 3600000;            // 3600000 = 60 * 60 * 1000
		 return String.format ("%02d:%02d:%02d", horas, minutos, segundos);
	}

	public static String limitOffset(Integer limit, Integer offset) {
    	StringBuilder sql = new StringBuilder();
    	if (Uteis.isAtributoPreenchido(limit)) {
    		sql.append(" LIMIT ").append(limit);
			if (Uteis.isAtributoPreenchido(offset)) {
				sql.append(" OFFSET ").append(offset);
			}
		}
    	return sql.toString();
    }

	public static boolean verificarHoraValida(String hora){
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		sdf.setLenient(false);
		try{
			sdf.parse(hora);
		}catch(ParseException e){
			return false;
		}
		return true;
	}
	public static boolean verificarHoraInicialMenorFinal(String horaInicial , String horaFinal){

		try {
			SimpleDateFormat format = new SimpleDateFormat("HH:mm");

	    	Date h_inicial = format.parse(horaInicial);
			Date h_final =   format.parse(horaFinal);

	    	if (h_final.compareTo(h_inicial) >= 0) {
	    		return Boolean.TRUE;
	    	}
	    	return Boolean.FALSE;
		}
			catch (ParseException e) {
				return false;
			}


	}

	
	
	
	public static String DO_HASHB64(String dados) {
		try {

			MessageDigest md = MessageDigest.getInstance("SHA-256");

			byte[] hash = md.digest(dados.getBytes(StandardCharsets.ISO_8859_1));

			return Base64.getEncoder().encodeToString(hash);

		} catch (NoSuchAlgorithmException e) {

			e.printStackTrace();
			return null;
		}
	}
	
	

	public static List<SelectItem> montarComboboxSemOpcaoDeNenhum(@SuppressWarnings("rawtypes") Enum[] enumeradores, Obrigatorio obrigatorio) {
		List<SelectItem> lista = new ArrayList<SelectItem>();

		if (obrigatorio == Obrigatorio.NAO)
			lista.add(new SelectItem("", ""));

		for (Enum<?> enumerador : enumeradores) {
			lista.add(new SelectItem(enumerador, internacionalizarEnum(enumerador)));
		}
		return lista;
	}

	public static String internacionalizarEnum(Enum enumerador) {
		return UteisJSF.internacionalizar("enum_" + enumerador.getClass().getSimpleName() + "_" + enumerador.toString());
	}

	public void substituirTagBibliotecaLexMagister(String texto ,String tag, String valor) {
		texto.replaceAll(tag, valor);

	}

	public static Date getDataInicioSemestreAtual() {
		return getDataInicioSemestreAno(Integer.parseInt(getAnoDataAtual()), Integer.parseInt(getSemestreAtual()));
	}

	public static Date getDataFimSemestreAtual() {
		return getDataFimSemestreAno(Integer.parseInt(getAnoDataAtual()), Integer.parseInt(getSemestreAtual()));
	}

	public static Object clonar(Serializable object) {
		return SerializationUtils.clone(object);
	}

	public static boolean ClienteMovel(String userAgent) {
		Pattern pattern = Pattern.compile("(iPhone|iPad|iPod|Android|BlackBerry|Opera Mobi|Opera Mini|IEMobile)");
		Matcher matcher = pattern.matcher(userAgent);
		return matcher.find();
	}

	public static Date getHoraMeiaNoite(Date date) {
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(date);
	    calendar.set(Calendar.HOUR_OF_DAY, 0);
	    calendar.set(Calendar.MINUTE, 0);
	    calendar.set(Calendar.SECOND, 0);
	    calendar.set(Calendar.MILLISECOND, 0);

	    return calendar.getTime();
	}

	public static boolean isDataAnteriorSemConsiderarHoraMinutoSegundo(Date data1, Date data2) {
		return getHoraMeiaNoite(data1).before(getHoraMeiaNoite(data2));
	}

	public static String decriptografarPorAlgoritimoRSA(String textoSerDecriptografado, String PATH_CHAVE_PRIVADA)
			throws Exception, IOException {

		if (!verificaSeExisteChavePrivadaNoSO(PATH_CHAVE_PRIVADA)) {
			geraChaveCriptografiaPrivada(PATH_CHAVE_PRIVADA);
		}

		final PrivateKey chavePrivada;

		try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(PATH_CHAVE_PRIVADA))) {

			chavePrivada = (PrivateKey) inputStream.readObject();
		}

		String textoBase64Corrigido = textoSerDecriptografado.replaceAll("@", "+");

		byte[] bytesCriptografados = Base64.getDecoder().decode(textoBase64Corrigido);

		final String textoDescriptografado = decriptografaPorAlgoritimoRSA(bytesCriptografados, chavePrivada);

		return textoDescriptografado;
	}

	/**
	   * Decriptografa o texto puro usando chave privada.
	   */
	   private static String decriptografaPorAlgoritimoRSA(byte[] texto, PrivateKey chave) {
	    byte[] dectyptedText = null;

	    try {
	      final Cipher cipher = Cipher.getInstance(ALGORITHM_RSA);
	      // Decriptografa o texto puro usando a chave Privada
	      cipher.init(Cipher.DECRYPT_MODE, chave);
	      dectyptedText = cipher.doFinal(texto);
	    } catch (Exception ex) {
	      ex.printStackTrace();
	    }
	    return new String(dectyptedText);
	  }

	
	
	
	

	public static String criptografarPorAlgoritimoRSA(String textoSerCriptografado, String PATH_CHAVE_PUBLICA) throws Exception, IOException {

	    if (!verificaSeExisteChavePublicaNoSO(PATH_CHAVE_PUBLICA)) {
	        geraChaveCriptografiaPublica(PATH_CHAVE_PUBLICA);
	    }

	    final PublicKey chavePublica;
	    final byte[] textoCriptografado;

	    
	    try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(PATH_CHAVE_PUBLICA))) {
	        chavePublica = (PublicKey) inputStream.readObject();
	        textoCriptografado = criptografaRSA(textoSerCriptografado, chavePublica);
	    }

	    
	    String textoEmBase64 = Base64.getEncoder().encodeToString(textoCriptografado);
	    
	   
	    String textoComArroba = textoEmBase64.replaceAll("[+]", "@");

	    
	    return textoComArroba;
	}

	/**
	   * Criptografa o texto puro usando chave pblica.
	   */
	   private static byte[] criptografaRSA(String texto, PublicKey chave) {
	    byte[] cipherText = null;
	    try {
	      final Cipher cipher = Cipher.getInstance(ALGORITHM_RSA);
	      // Criptografa o texto puro usando a chave Plica
	      cipher.init(Cipher.ENCRYPT_MODE, chave);
	      cipherText = cipher.doFinal(texto.getBytes());
	    } catch (Exception e) {
	      e.printStackTrace();
	    }

	    return cipherText;
	  }

	/**
	   * Verifica se o par de chaves Pblica j foI gerada.
	   */
	  public static boolean verificaSeExisteChavePublicaNoSO(String PATH_CHAVE_PUBLICA) {

	    File chavePublica = new File(PATH_CHAVE_PUBLICA);

	    if (chavePublica.exists()) {
	      return true;
	    }

	    return false;
	  }

	  /**
	   * Verifica se o par de chaves Pblica e Privada j foram geradas.
	   */
	  public static boolean verificaSeExisteChavePrivadaNoSO(String PATH_CHAVE_PRIVADA) {

	    File chavePrivada = new File(PATH_CHAVE_PRIVADA);
	    if (chavePrivada.exists()) {
	      return true;
	    }
	    return false;
	  }

	  /**
	   * Gera a chave que contm um par de chave Privada e Pblica usando 1025 bytes.
	   * Armazena o conjunto de chaves nos arquivos private.key e public.key
	   */
	  public static void geraChaveCriptografiaPrivada(String PATH_CHAVE_PRIVADA) {
	    try {
	      final KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM_RSA);
	      keyGen.initialize(1024);
	      final KeyPair key = keyGen.generateKeyPair();

	      File chavePrivadaFile = new File(PATH_CHAVE_PRIVADA);

	   // Cria os arquivos para armazenar a chave Privada e a chave Publica
	      if (chavePrivadaFile.getParentFile() != null) {
	        chavePrivadaFile.getParentFile().mkdirs();
	      }
	      chavePrivadaFile.createNewFile();

	      // Salva a Chave Privada no arquivo
	      ObjectOutputStream chavePrivadaOS = new ObjectOutputStream(
	          new FileOutputStream(chavePrivadaFile));
	      chavePrivadaOS.writeObject(key.getPrivate());
	      chavePrivadaOS.close();
	    } catch (Exception e) {
	      e.printStackTrace();
	    }

	  }

	/**
	   * Gera a chave que contm um par de chave Privada e Pblica usando 1025 bytes.
	   * Armazena o conjunto de chaves nos arquivos private.key e public.key
	   */
	  public static void geraChaveCriptografiaPublica(String PATH_CHAVE_PUBLICA) {
	    try {
	      final KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM_RSA);
	      keyGen.initialize(1024);
	      final KeyPair key = keyGen.generateKeyPair();

	      File chavePublicaFile = new File(PATH_CHAVE_PUBLICA);


	      if (chavePublicaFile.getParentFile() != null) {
	        chavePublicaFile.getParentFile().mkdirs();
	      }

	      chavePublicaFile.createNewFile();

	      // Salva a Chave Pblica no arquivo
	      ObjectOutputStream chavePublicaOS = new ObjectOutputStream(
	          new FileOutputStream(chavePublicaFile));
	      chavePublicaOS.writeObject(key.getPublic());
	      chavePublicaOS.close();


	    } catch (Exception e) {
	      e.printStackTrace();
	    }

	  }

	public static BigDecimal converterValorDoubleParaBigDecimal(Double valor) {
		return  BigDecimal.valueOf(valor);
	}

	/*
	 * este metodo e utilizado para criaao de codigo agrupador com as letras do
	 * alfabeto,  necessario inserir um array com 3 posioes do alfabeto
	 * exemplo SE INSERIR AAA o resultado sera -> AAB
	 * e assim segue a sequencia -> AAC -> AAD -> AAE ate Z -> ABA -> ABB -> ABC
	 */
	public static String[] realizarCriacaoCodigoAgrupamento(String[] codigoAgrupamento) {

		Map<String, Integer> mapLetraNumr = new HashMap<String, Integer>();
		Map<Integer, String> mapNumrLetra = new HashMap<Integer, String>();
		String alfabeto = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		for (int i = 0; i < alfabeto.length(); i++) {
			mapLetraNumr.put(alfabeto.substring(i, (i + 1)), (i + 1));
		}
		int cont = 0;
		for (int i = alfabeto.length(); i > 0; i--) {
			mapNumrLetra.put((cont + 1), alfabeto.substring(cont, (cont + 1)));
			cont++;
		}
		int primeiroNumr = mapLetraNumr.get(codigoAgrupamento[0]);
		int segundoNumr = mapLetraNumr.get(codigoAgrupamento[1]);
		int terceiroNumr = mapLetraNumr.get(codigoAgrupamento[2]);

		if (terceiroNumr == 26) {
			if (segundoNumr == 26) {
				if (primeiroNumr == 26) {
					primeiroNumr = 1;
					segundoNumr = 1;
					terceiroNumr = 1;
				} else {
					primeiroNumr = primeiroNumr + 1;
					segundoNumr = 1;
					terceiroNumr = 1;
				}
			} else {
				segundoNumr = segundoNumr + 1;
				terceiroNumr = 1;
			}
		} else {
			terceiroNumr = terceiroNumr + 1;
		}

		String[] retorno = { mapNumrLetra.get(primeiroNumr), mapNumrLetra.get(segundoNumr),
				mapNumrLetra.get(terceiroNumr) };
		return retorno;
	}

	public static long nrDiasEntreDatasDesconsiderandoHoras(Date inicio, Date fim) {
		return inicio != null && fim != null ? nrDiasEntreDatas(getHoraMeiaNoite(inicio), getHoraMeiaNoite(fim)) : 0;
	}

	public static boolean isColunaExistente(SqlRowSet rs, String columnName) throws SQLException {
	    SqlRowSetMetaData rsmd = rs.getMetaData();
	    int columns = rsmd.getColumnCount();
	    for (int x = 1; x <= columns; x++) {
	        if (columnName.toUpperCase().equals(rsmd.getColumnName(x).toUpperCase())) {
	            return true;
	        }
	    }
	    return false;
	}

	public static String realizarGeracaoIdentificador(String entidade, String mascara, String mascaraPrincipal, List<String> niveis) throws ConsistirException {
		if (isAtributoPreenchido(niveis)) {
			int ultimoIndice = niveis.size() - 1;
			String parteInicialMascara = niveis.stream().limit(ultimoIndice).collect(joining("."));
			String parteFinalMascara = niveis.get(ultimoIndice);
			parteInicialMascara = isAtributoPreenchido(parteInicialMascara) ? parteInicialMascara + "." : "";
			return parteInicialMascara + realizarGeracaoParteFinalIdentificador(parteFinalMascara, niveis.get(ultimoIndice).length(), entidade);
		} else {
			niveis = of(mascara.split("\\.")).collect(toList());
			int ultimoIndice = niveis.size() - 1;
			mascaraPrincipal = isAtributoPreenchido(mascaraPrincipal) ? mascaraPrincipal + "." : "";
			return mascaraPrincipal + realizarGeracaoParteFinalIdentificador("0", niveis.get(ultimoIndice).length(), entidade);
		}
	}

	private static String realizarGeracaoParteFinalIdentificador(String valor, int tamanho, String entidade) throws ConsistirException {
		if (!NumberUtils.isNumber(valor)) {
			throw new ConsistirException("Falha na gerao do Identificador " + entidade + ". Valor no numrico.");
		}
		String mascaraFinal = new BigDecimal(valor).add(BigDecimal.ONE).toString();
		mascaraFinal = preencherComZerosPosicoesVagas(mascaraFinal, tamanho);
		return validarParteFinalIdentificador(mascaraFinal, tamanho, entidade);
	}

	private static String validarParteFinalIdentificador(String mascara, int tamanho, String entidade) throws ConsistirException {
		if (!isAtributoPreenchido(mascara)) {
			throw new ConsistirException("Falha na gerao do Identificador " + entidade + ". Identificador no gerado.");
		}
		if (mascara.length() > tamanho) {
			throw new ConsistirException("Falha na gerao do Identificador " + entidade + ". O tamanho do identificador excedeu o limite definido na Configurao Financeira.");
		}
		return mascara;
	}

	public static void validarIdentificador(String entidade, String identificador, String mascaraPadrao) throws ConsistirException {
		if (!isAtributoPreenchido(identificador)) {
			throw new ConsistirException("O Identificador " + entidade + " deve ser informado.");
		}
		List<String> identificadoreNiveis = of(identificador.split("\\.")).map(in -> validarIdentificadorIsNumerico(in, entidade)).collect(toList());
		long nivel = identificador.chars().filter(c -> c == '.').count() + 1;
		if (identificadoreNiveis.size() != (int)nivel) {
			throw new ConsistirException("Informe corretamente a quantidade de nmeros e pontos para o Identificador " + entidade + ", conforme mscara: " + mascaraPadrao);
		}
		List<String> niveis = of(mascaraPadrao.split("\\.")).limit(nivel).collect(toList());
		for (int i = 0; i < identificadoreNiveis.size(); i++) {
			if (identificadoreNiveis.get(i).length() != niveis.get(i).length()) {
				throw new ConsistirException("Informe corretamente a quantidade de dgitos para o nvel do Identificador " + entidade + ", conforme mscara: " + mascaraPadrao);
			}
		}
	}

	private static String validarIdentificadorIsNumerico(String identificador, String entidade) throws StreamSeiException {
		if (!getIsValorNumerico(identificador)) {
			throw new StreamSeiException("Devem ser informados somente nmeros para o Identificador " + entidade + ".");
		}
		return identificador;
	}

	public static String converterInteiroParaOrdinal(Integer inteiro) {
		if (inteiro != null && inteiro >= 0 && inteiro < 100) {
			return inteiro < 10 ? ORDINAIS[inteiro]	: ORDINAIS_MULTIPLOS_DEZ[inteiro / 10] + (inteiro % 10 > 0 ? " ": "") + ORDINAIS[inteiro % 10];
		}
		return "";
	}
	public static boolean isAtributoPreenchido(Double valor, boolean isConsiderarZero) {
		return isConsiderarZero ? (valor != null) : isAtributoPreenchido(valor);
	}

	public static String converterStringParaHex(String str) {
        char[] chars = Hex.encodeHex(str.getBytes(StandardCharsets.UTF_8));

        return String.valueOf(chars);
    }

    public static String converterHexParaString(String hex) {

        String result = "";
        try {
            byte[] bytes = Hex.decodeHex(hex.toCharArray());
            result = new String(bytes, StandardCharsets.UTF_8);
        } catch (DecoderException e) {
            throw new IllegalArgumentException("Invalid Hex format!");
        }
        return result;
    }

	public static void setCookie(String name, String value, int expiry) {
		FacesContext facesContext = FacesContext.getCurrentInstance();

		HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
		Cookie cookie = null;

		Cookie[] userCookies = request.getCookies();
		if (userCookies != null && userCookies.length > 0) {
			for (int i = 0; i < userCookies.length; i++) {
				if (userCookies[i].getName().equals(name)) {
					cookie = userCookies[i];
					break;
				}
			}
		}

		if (cookie != null && expiry > 0) {
			cookie.setValue(converterStringParaHex(value));
		} else {
			cookie = new Cookie(name, Uteis.isAtributoPreenchido(value) ? converterStringParaHex(value) : "");
			cookie.setPath("/");
		}

		cookie.setMaxAge(expiry);

		HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
		response.addCookie(cookie);
	}

	public static String getCookie(String name) {

		FacesContext facesContext = FacesContext.getCurrentInstance();

		HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
		Cookie cookie = null;

		Cookie[] userCookies = request.getCookies();
		if (userCookies != null && userCookies.length > 0) {
			for (int i = 0; i < userCookies.length; i++) {
				if (userCookies[i].getName().equals(name) && userCookies[i].getMaxAge() > 0) {
					cookie = userCookies[i];
					return converterHexParaString(cookie.getValue());
				}
			}
		}
		return null;
	}

	public static String obterSaml(ConfiguracaoLdapVO configuracaoLdapVO) {
		return new StringBuilder()
				.append("<samlp:AuthnRequest ")
				.append("xmlns=\"urn:oasis:names:tc:SAML:2.0:metadata\" ")
				.append("ID=\"id" + UUID.randomUUID() + "\" ")
				.append("Version=\"2.0\" IssueInstant=\"" + obterDataAtualFormatadaSaml() + "\" ")
				.append("xmlns:samlp=\"urn:oasis:names:tc:SAML:2.0:protocol\"> ")
				.append("<Issuer xmlns=\"urn:oasis:names:tc:SAML:2.0:assertion\">" + configuracaoLdapVO.getUrlIdentificadorAD() + "</Issuer> ")
				.append("</samlp:AuthnRequest>")
				.toString();
	}

	private static String obterDataAtualFormatadaSaml() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		return sdf.format(new Date());
	}

	public static String obterRequisicaoBase64Saml(String samlRequest) throws IOException {

		ByteArrayOutputStream os = new ByteArrayOutputStream();

		Deflater deflater = new Deflater(Deflater.DEFAULT_COMPRESSION, true);

		try (DeflaterOutputStream deflaterOutputStream = new DeflaterOutputStream(os, deflater)) {

			deflaterOutputStream.write(samlRequest.getBytes(StandardCharsets.UTF_8));
		}

		return Base64.getEncoder().encodeToString(os.toByteArray());
	}

	public static String obterUrlRedirecionamentoSaml(ConfiguracaoLdapVO configuracaoLdapVO, String base64, String emailInstitucional) throws UnsupportedEncodingException {
		return String.format("%s?SAMLRequest=%s&login_hint=%s&username=%s",
				configuracaoLdapVO.getUrlLoginAD(),
				URLEncoder.encode(base64, StandardCharsets.UTF_8.name()),
				emailInstitucional,
				emailInstitucional);
	}

	public static String removeCaractersEspeciais4(String string) {
		string = string.replaceAll("[]", "A");
		string = string.replaceAll("[]", "a");
		string = string.replaceAll("[]", "E");
		string = string.replaceAll("[]", "e");
		string = string.replaceAll("", "I");
		string = string.replaceAll("", "I");
		string = string.replaceAll("", "i");
		string = string.replaceAll("[]", "O");
		string = string.replaceAll("[]", "o");
		string = string.replaceAll("[]", "U");
		string = string.replaceAll("[]", "u");
		string = string.replaceAll("", "C");
		string = string.replaceAll("", "c");
		string = string.replaceAll("[]", "y");
		string = string.replaceAll("", "Y");
		string = string.replaceAll("", "n");
		string = string.replaceAll("", "N");
		string = string.replaceAll("\\\\", "");
		string = string.replaceAll("['<>|/]", "");
		string = string.replaceAll(" - ", " ");
		string = string.replaceAll("-", " ");
		string = string.replaceAll("- ", "");
		string = string.replaceAll(" -", "");
		// texto = texto.replaceAll("[ ]", "");
		string = string.replaceAll("[-#$%&*()_+={}?.,:;^~`@!\"']", "");
		return string;
	}
	
	public static String realizarCriacaoTexto(String caractere, int vezes) {
		String resultado = "";
		for (int i = 1;i <= vezes;i++) {
			resultado += caractere;
		}
		return resultado;
	}
	
	public static String realizarAnonimizacaoEmailIgnorandoTagNegrito(String email) {
		if (email != null) {
			String resultado = "";
			String[] partes = email.trim().split("@");
			if (partes[0].length() < 6) {
				resultado = realizarAnonimizacaoIgnorandoTagNegrito(partes[0], 3, 1, 1);
			} else {
				resultado = realizarAnonimizacaoIgnorandoTagNegrito(partes[0], 6, 3, 2);
			}
			return email.replace(partes[0], resultado);
		}
		return email;
	}
	
	public static String realizarAnonimizacaoIgnorandoTagNegrito(String texto, int minimo, int inicio, int fim) {
		if (texto != null && texto.length() > minimo) {
			String resultado = texto;
			int pos1 = resultado.indexOf("<b>");
			if (pos1 > 0) {
				if (pos1 < inicio + 1) {
					resultado = realizarCriacaoTexto("*", pos1) + resultado.substring(pos1);
				} else {
					resultado = realizarCriacaoTexto("*", inicio) + resultado.substring(inicio);
				}
			} else if (pos1 < 0) {
				resultado = realizarCriacaoTexto("*", inicio) + resultado.substring(inicio);
			}
			if (fim > 0) {
				int pos2 = resultado.indexOf("</b>");
				if (pos2 > 0) {
					int qtde = resultado.length() - pos2 - 4;
					if (qtde == 0) {
						return resultado;
					} else {
						if (fim < qtde) {
							qtde = fim;
						}
						return resultado.substring(0, resultado.length() - qtde) + realizarCriacaoTexto("*", qtde);
					}
				} else {
					return resultado.substring(0, resultado.length() - fim) + realizarCriacaoTexto("*", fim);
				}
			} else {
				return resultado;
			}
		}
		return texto;
	}
	
	public static String getSemestreDataCenso(Date data) {
		if (Uteis.isAtributoPreenchido(data)) {
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(data);
			int mes = gc.get(GregorianCalendar.MONTH) + 1;
			return mes > 6 ? "2" : "1";
		}
		return "";
	}
	
	public static Integer getQuantidadeMemoriaStringEmMega(String texto){
		int valor = (8 * (int) ((((texto.toCharArray().length) * 2) + 45) / 8)) / 1000000 ;
		return valor;
	}
	
	public static void checkStateList(boolean expression, Object errorMessage, ConsistirException ce) {
		if (expression) {
			ce.getListaMensagemErro().add(errorMessage.toString());
		}
	}

	public static String getTipoOrigemArquivoParaNome(String origem) {
		String retorno = "";
		if (isAtributoPreenchido(origem)) {
			if (origem.equals("DI")) {
				retorno = "DIPLOMA_";
			} else if (origem.equals("CE")) {
				retorno = "CERTIFICADO_";
			}
		}
		return retorno;
	}
	
	public static String removerEspacosString(String texto) {
		String textoFormatado = "";
		return textoFormatado = texto.replaceAll("\\s", "").trim();
	}
	
	public static <T> T coalesce(T t1, T t2) {
		return t1 == null ? t2 : t1;
	}

	public static String encriptarSenhaSHA512(String senha) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        MessageDigest algorithm = MessageDigest.getInstance("SHA-512");
        algorithm.reset();
        algorithm.update(senha.getBytes("utf8"));
        return String.format("%0128x", new BigInteger(1, algorithm.digest()));
    }
	
	public static boolean isAtributoPreenchido(File file) {
		return file != null && file.exists();
	}
	
	public static <T> T coalesceIsAtributoPreenchido(T t1, T t2) {
        return isAtributoPreenchido(t1) ? t1 : t2;
    }
	
	public static String formatarStringXML(String dado) {
        dado = dado.replaceAll("\n", "").replaceAll("\r", "").replaceAll("\t", " ").trim();
        return dado;
    }
	
	public static Boolean getIsValorNumerico2(String valor) {
		if (valor != null && !valor.trim().isEmpty()) {
			String string = new String(valor);
			ParsePosition pos = new ParsePosition(0);
			NumberFormat.getInstance().parse(string, pos);
			return string.length() == pos.getIndex();
		}
		return Boolean.FALSE;
	}
	
	public static boolean isJsonValidoConverteParaClasse(String json, Class classe) {
		if (!(isAtributoPreenchido(json) && Objects.nonNull(classe))) {
			return Boolean.FALSE;
		}
		try {
			new Gson().fromJson(json, classe);
			return Boolean.TRUE;
		} catch (Exception e) {
			return Boolean.FALSE;
		}
	}
	
	public static String converterObjetoParaJson(Object objeto) {
		if (Objects.isNull(objeto)) {
			return Constantes.EMPTY;
		}
		Gson gson = new Gson();
		return gson.toJson(objeto);
	}
	
	public static String getMesAtualDoisDigitos(Date data) {
		Calendar dataCalendar = Calendar.getInstance();
		dataCalendar.setTime(data);

		int ano = dataCalendar.get(Calendar.YEAR);
		int mes = dataCalendar.get(Calendar.MONTH) + 1;

		String mesRef = String.valueOf(mes);
		if (mesRef.length() == 1) {
			mesRef = "0" + mesRef;
		}
		return mesRef;
	}

	public static String calcularSomaSemestres(int anoInicial, int semestreInicial, int quantidade) {
		int ano = anoInicial;
		int semestre = semestreInicial;

		for (int i = 1; i < quantidade; i++) {
			if (semestre == 2) {
				semestre = 1;
				ano++;
			} else {
				semestre++;
			}
		}

		return ano + "/" + semestre;
	}


	/**
	 * Converte um java.util.Date em OffsetDateTime usando o fuso horrio de So Paulo.
	 *
	 * @param date a Date a ser convertida (pode ser null)
	 * @return OffsetDateTime correspondente, ou null se date for null
	 */
	public static OffsetDateTime toOffsetDateTime(Date date) {
		if (date == null) {
			throw new IllegalArgumentException("Parmetro date no pode ser null");
		}
		return date.toInstant()
				.atZone(ZoneId.of("America/Sao_Paulo"))
				.toOffsetDateTime();
	}
}

