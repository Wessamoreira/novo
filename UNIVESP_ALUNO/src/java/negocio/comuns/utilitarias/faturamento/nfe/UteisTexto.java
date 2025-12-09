package negocio.comuns.utilitarias.faturamento.nfe;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.swing.text.MaskFormatter;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilReflexao;
import negocio.comuns.utilitarias.dominios.SituacaoHistorico;

public class UteisTexto {

	public static final char A_AGUDO = 'á';
	public static final char A_AGUDOMAIUSCULO = 'Á';
	public static final char A_CRASE = 'à';
	public static final char A_CRASEMAIUSCULO = 'À';
	public static final char A_CIRCUNFLEXO = 'â';
	public static final char A_CIRCUNFLEXOMAIUSCULO = 'Â';
	public static final char A_TIO = 'ã';
	public static final char A_TIOMAIUSCULO = 'Ã';
	public static final char E_AGUDO = 'é';
	public static final char E_AGUDOMAIUSCULO = 'É';
	public static final char E_CIRCUNFLEXO = 'ê';
	public static final char E_CIRCUNFLEXOMAIUSCULO = 'Ê';
	public static final char I_AGUDO = 'í';
	public static final char I_AGUDOMAIUSCULO = 'Í';
	public static final char O_AGUDO = 'ó';
	public static final char O_AGUDOMAIUSCULO = 'Ó';
	public static final char O_TIO = 'õ';
	public static final char O_TIOMAISCULO = 'Õ';
	public static final char O_CIRCUNFLEXO = 'ô';
	public static final char O_CIRCUNFLEXOMAISCULO = 'Ô';
	public static final char U_AGUDO = 'ú';
	public static final char U_AGUDOMAIUSCULO = 'Ú';
	public static final char U_TREMA = 'ü';
	public static final char U_TREMAMAISCULO = 'Ü';
	public static final char C_CEDILHA = 'ç';
	public static final char C_CEDILHAMAISCULO = 'Ç';
	public static final char _OS = 'º';
	public static final char _AS = 'ª';
	public static final char E_COMERCIAL = '&';
	public static final char PORCENTAGEM = '%';
	public static final char ASTERISCO = '*';
	public static final char CERQUILHA = '#';
	public static final char ARROBA = '@';
	public static final char ENTER = '\n';

	public static String retirarAcentuacao(String prm) {
		String nova = "";
		for (int i = 0; i < prm.length(); i++) {
			if (prm.charAt(i) == A_AGUDO || prm.charAt(i) == A_CIRCUNFLEXO
					|| prm.charAt(i) == A_CRASE || prm.charAt(i) == A_TIO) {
				nova += "a";
			} else if (prm.charAt(i) == A_AGUDOMAIUSCULO || prm.charAt(i) == A_CIRCUNFLEXOMAIUSCULO
					|| prm.charAt(i) == A_CRASEMAIUSCULO || prm.charAt(i) == A_TIOMAIUSCULO) {
				nova += "A";
			} else if (prm.charAt(i) == E_AGUDO || prm.charAt(i) == E_CIRCUNFLEXO) {
				nova += "e";
			} else if (prm.charAt(i) == E_AGUDOMAIUSCULO || prm.charAt(i) == E_CIRCUNFLEXOMAIUSCULO) {
				nova += "E";
			} else if (prm.charAt(i) == I_AGUDO) {
				nova += "i";
			} else if (prm.charAt(i) == I_AGUDOMAIUSCULO) {
				nova += "I";
			} else if (prm.charAt(i) == O_AGUDO || prm.charAt(i) == O_TIO) {
				nova += "o";
			} else if (prm.charAt(i) == O_AGUDOMAIUSCULO || prm.charAt(i) == O_TIOMAISCULO) {
				nova += "O";
			} else if (prm.charAt(i) == O_CIRCUNFLEXO || prm.charAt(i) == O_CIRCUNFLEXOMAISCULO) {
				nova += "O";
			} else if (prm.charAt(i) == U_AGUDO || prm.charAt(i) == U_TREMA) {
				nova += "u";
			} else if (prm.charAt(i) == U_AGUDOMAIUSCULO || prm.charAt(i) == U_TREMAMAISCULO) {
				nova += "U";
			} else if (prm.charAt(i) == C_CEDILHA) {
				nova += "c";
				// } else if (Character.isSpaceChar(prm.charAt(i))){
				// nova += "_";
			} else if (prm.charAt(i) == C_CEDILHAMAISCULO) {
				nova += "C";
			} else if (prm.charAt(i) == _OS) {
				nova += " ";
			} else if (prm.charAt(i) == _AS) {
				nova += " ";
			} else if (prm.charAt(i) == E_COMERCIAL) {
				nova += "E";
			} else if (prm.charAt(i) == PORCENTAGEM) {
				nova += " ";
			} else if (prm.charAt(i) == ASTERISCO) {
				nova += " ";
			} else if (prm.charAt(i) == CERQUILHA) {
				nova += " ";
			} else if (prm.charAt(i) == ARROBA) {
				nova += " ";
			} else {
				nova += prm.charAt(i);
			}
		}
		return (nova);
	}

	public static String retirarSinaisSimbolosEspacoString(String label) {
		label = removerMascara(label);
		label = retirarAcentuacao(label);
		label = removerEspacosFinalString(label);
		label = label.replaceAll(" ", "");
		label = label.replaceAll("-", "");
		label = label.replaceAll(",", "");
		label = label.replaceAll("_", "");
		label = label.replaceAll("/", "");

		return label;
	}

	public static String retirarAcentuacaoAndCaracteresEspeciasRegex(String texto) {
		texto = texto.replaceAll("[ÂÀÁÄÃ]", "A");
		texto = texto.replaceAll("[âãàáä]", "a");
		texto = texto.replaceAll("[ÊÈÉË]", "E");
		texto = texto.replaceAll("[êèéë]", "e");
		texto = texto.replaceAll("ÎÍÌÏ", "I");
		texto = texto.replaceAll("îíìï", "i");
		texto = texto.replaceAll("[ÔÕÒÓÖ]", "O");
		texto = texto.replaceAll("[ôõòóö]", "o");
		texto = texto.replaceAll("[ÛÙÚÜ]", "U");
		texto = texto.replaceAll("[ûúùü]", "u");
		texto = texto.replaceAll("Ç", "C");
		texto = texto.replaceAll("ç", "c");
		texto = texto.replaceAll("[ýÿ]", "y");
		texto = texto.replaceAll("Ý", "Y");
		texto = texto.replaceAll("ñ", "n");
		texto = texto.replaceAll("Ñ", "N");
		texto = texto.replaceAll("\\\\", "");
		texto = texto.replaceAll("['<>|/]¦", "");
		texto = texto.replaceAll("[ ]", "");
		texto = texto.replaceAll("[-#$%¨&*()_+={}?.,:;º°ª^~´`§@!\"]", "");
		return texto;
	}

	public static boolean isExisteExtensao(String nome) {
		// regex que valida se depois do ponto existe algum caracter e depois pode existe um ou mais caracteres;
		return nome.matches(".+\\.\\w+");
	}

	public static String removerMascara(String campo) {
		String campoSemMascara = "";
		for (int i = 0; i < campo.length(); i++) {
			if ((campo.charAt(i) != ',')
					&& (campo.charAt(i) != '.')
					&& (campo.charAt(i) != '-')
					&& (campo.charAt(i) != ':')
					&& (campo.charAt(i) != '(')
					&& (campo.charAt(i) != ')')
					&& (campo.charAt(i) != '/')) {
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

	public static String limitarQuantidadeCaracteresComIndicadorMaisTexto(String texto, Integer limite, boolean indicarExistenciaMaisTexto) {
		if (texto.length() > limite) {
			if (indicarExistenciaMaisTexto) {
				return texto.substring(0, limite) + " ...";
			}
			return texto.substring(0, limite);
		} else {
			return texto;
		}
	}

	public static String limitarQuantidadeCaracteres(String texto, Integer limite) {
		if (texto.length() > limite) {
			return texto.substring(0, limite);
		} else {
			return texto;
		}
	}

	public static String obterNumeroComDoisDigitos(Integer numero) {
		String retorno = numero.toString();
		while (retorno.length() < 2) {
			retorno = "0" + retorno;
		}
		return retorno;
	}

	public static String removerZeroEsquerda(String entrada) {
		String saida = entrada;
		if (saida.length() == 3) {
			if (saida.substring(0, 1).equals("0")) {
				saida = saida.substring(1, saida.length());
			}
		}
		return saida;
	}

	// public static String retirarAcentuacao(String prm) {
	// String nova = "";
	// for (int i = 0; i < prm.length(); i++) {
	// if (prm.charAt(i) == Uteis.A_AGUDO || prm.charAt(i) == Uteis.A_CIRCUNFLEXO ||
	// prm.charAt(i) == Uteis.A_CRASE || prm.charAt(i) == Uteis.A_TIO) {
	// nova += "a";
	// } else if (prm.charAt(i) == Uteis.A_AGUDOMAIUSCULO) {
	// nova += "A";
	// } else if (prm.charAt(i) == Uteis.E_AGUDO || prm.charAt(i) == Uteis.E_CIRCUNFLEXO) {
	// nova += "e";
	// } else if (prm.charAt(i) == Uteis.I_AGUDO) {
	// nova += "i";
	// } else if (prm.charAt(i) == Uteis.O_AGUDO || prm.charAt(i) == Uteis.O_TIO || prm.charAt(i)==Uteis.O_CRASE) {
	// nova += "o";
	// } else if (prm.charAt(i) == Uteis.U_AGUDO || prm.charAt(i) == Uteis.U_TREMA) {
	// nova += "u";
	// } else if (prm.charAt(i) == Uteis.C_CEDILHA) {
	// nova += "c";
	// //} else if (Character.isSpaceChar(prm.charAt(i))){
	// // nova += "_";
	// } else {
	// nova += prm.charAt(i);
	// }
	// }
	// return (nova);
	// }
	//
	// public static void gerarPdfApartirTexto() throws Exception {
	// try {
	// Document document = new Document();
	// PdfWriter.getInstance(document, new FileOutputStream("/home/alessandro/teste.pdf"));
	// document.open();
	// document.add(new Paragraph("Testes\nFim."));
	// document.close();
	// } catch (Exception e) {
	// //System.out.println(e.getMessage());
	// }
	// }

	public static String adicionarCampoLinha(String linha, String valorCampo, int posIni, int posFim, String caracterPreenchimento, boolean alinharEsquerda, boolean novaLinha) throws Exception {
		try {
			posFim++;
			String linhaDados = "";
			if (alinharEsquerda) {
				if (valorCampo.length() <= posFim - posIni) {
					if (valorCampo.length() != posFim - posIni) {
						int cont = valorCampo.length();
						while (cont < posFim - posIni) {
							valorCampo += caracterPreenchimento;
							cont++;
						}
					}
				} else {
					valorCampo = valorCampo.substring(0, posFim - posIni);
				}
				linhaDados += valorCampo;
			} else {
				String valor = "";
				if (valorCampo.length() <= posFim - posIni) {
					if (valorCampo.length() != posFim - posIni) {
						int contPreencheresquerda = posFim - posIni - valorCampo.length();
						int i = 1;
						while (i <= contPreencheresquerda) {
							valor += caracterPreenchimento;
							i++;
						}
					}
					valor += valorCampo;
				} else {
					valor = valorCampo.substring(0, posFim - posIni);
				}
				linhaDados += valor;
			}
			linha += linhaDados;
			if (novaLinha) {
				linha += "\n";
			}
			return linha;
		} catch (Exception e) {
			throw new Exception("Erro na inclusão da linha.");
		}
	}

	public static Integer converteStringParaInteiro(String valorStr) {
		if (valorStr == null) {
			return 0;
		}
		if (!valorStr.contains(".")) {
			return Integer.parseInt(valorStr);
		}
		String inteira = valorStr.substring(0, valorStr.indexOf("."));
		String extensao = valorStr.substring(valorStr.indexOf(".") + 1, valorStr.length());
		if(extensao.length()>2) {
			extensao = extensao.substring(0, 2);
		}else if(extensao.length()< 2) {
			extensao += "0";
		}
		return Integer.parseInt(removerMascara(inteira) + extensao);
	}
	
	public static List<Integer> converteListaCodigoStringParaInteiro(String lista) {
		List<Integer> listaInteiro = new ArrayList<Integer>();
		listaInteiro.add(0);
		String[] listaCodigos = lista.split(",");
		for (String codigo : listaCodigos) {
			listaInteiro.add(Integer.parseInt(codigo));
		}
		
		return listaInteiro;
	}
	public static String obterListaDeSemestrePorQuantidade(String ano, String semestre , Integer quantidade) {
		Uteis.checkState(!Uteis.isAtributoPreenchido(ano),"O campo ano deve ser informado.");
		Uteis.checkState(!Uteis.isAtributoPreenchido(quantidade),"O quantidade deve ser informado.");
		StringBuilder sb = new StringBuilder("");
		if(Uteis.isAtributoPreenchido(semestre)) {
			sb.append("'").append(ano).append(semestre).append("'");
			for (int i = 2; i <= quantidade; i++) {
				if(semestre.equals("2")) {
					sb.append(",'").append(ano).append("1").append("'");
					semestre = "1";
				}else if(semestre.equals("1")) {
					ano = String.valueOf(Integer.parseInt(ano)-1);
					semestre = "2";
					sb.append(",'").append(ano).append(semestre).append("'");
				}
			}
		}else {
			sb.append("'").append(ano).append("'");
			for (int i = 2; i <= quantidade; i++) {
				ano = String.valueOf(Integer.parseInt(ano)-1);
				sb.append(",'").append(ano).append("'");				
			}
		}
		return sb.toString();
	}

	public static String converteListaStringParaString(List<String> lista) {
		boolean existeVirgula = false;
		StringBuilder sql = new StringBuilder("");
		for (String obj : lista) {
			if (existeVirgula) {
				sql.append(",").append(obj);
			} else {
				sql.append(obj);
				existeVirgula = true;
			}
		}
		return Uteis.isAtributoPreenchido(sql.toString()) ? sql.toString() : "";
	}
	
	public static String converteListaInteiroParaString(List<Integer> lista) {
		boolean existeVirgula = false;
		StringBuilder sql = new StringBuilder("");
		for (Integer obj : lista) {
			if (existeVirgula) {
				sql.append(",").append(obj);
			} else {
				sql.append(obj);
				existeVirgula = true;
			}
		}
		return Uteis.isAtributoPreenchido(sql.toString()) ? sql.toString() : "0";
	}

	public static String converteListaEntidadeCampoCodigoParaString(List<? extends SuperVO> lista) {
		boolean existeVirgula = false;
		StringBuilder sql = new StringBuilder("");
		for (Object obj : lista) {
			Integer codigo = (Integer) UtilReflexao.invocarMetodoGet(obj, "codigo");
			if (Uteis.isAtributoPreenchido(codigo)) {
				if (existeVirgula) {
					sql.append(",").append(codigo);
				} else {
					sql.append(codigo);
					existeVirgula = true;
				}
			}

		}
		return Uteis.isAtributoPreenchido(sql.toString()) ? sql.toString() : "0";
	}
	
	public static String converteListaStringParaCondicaoIn(List<String> lista) {
		boolean existeVirgula = false;
		StringBuilder sql = new StringBuilder("");
		for (String obj : lista) {
			if (existeVirgula) {
				sql.append(",'").append(obj).append("'");
			} else {
				sql.append("'").append(obj).append("'");
				existeVirgula = true;
			}
		}
		return Uteis.isAtributoPreenchido(sql.toString()) ? sql.toString() : "";
	}

	public static String converteListaEntidadeCampoCodigoParaCondicaoIn(List<? extends SuperVO> lista) {
		boolean existeVirgula = false;
		StringBuilder sql = new StringBuilder("");
		for (Object obj : lista) {
			Integer codigo = (Integer) UtilReflexao.invocarMetodoGet(obj, "codigo");
			if (Uteis.isAtributoPreenchido(codigo)) {
				if (existeVirgula) {
					sql.append(",").append("'").append(codigo).append("'");
				} else {
					sql.append("'").append(codigo).append("'");
					existeVirgula = true;
				}
			}
		}
		return Uteis.isAtributoPreenchido(sql.toString()) ? sql.toString() : "'0'";
	}
	
	public static String converteListaEntidadeCampoCodigoParaCondicaoIn(List<? extends SuperVO> lista, String campo) throws Exception {
		boolean existeVirgula = false;
		String valorRetorno = "";
		StringBuilder sql = new StringBuilder("");
		for (Object obj : lista) {
			Object retorno = (Object) UtilReflexao.realizarValidacaoCampoReflection(obj, campo);
			if(retorno  instanceof Integer){
				valorRetorno = ((Integer)retorno).toString();
			}else  if (retorno  instanceof String) {
				valorRetorno = ((String)retorno);
			}
			if(Uteis.isAtributoPreenchido(valorRetorno)){
				if (existeVirgula) {
					sql.append(",").append("'").append(valorRetorno).append("'");
				} else {
					sql.append("'").append(valorRetorno).append("'");
					existeVirgula = true;
				}	
			}
		}
		return Uteis.isAtributoPreenchido(sql.toString()) ? sql.toString() : "'0'";
	}
	
	public static String converteListaEnumParaCondicaoIn(List<? extends Enum> enumerados) {
		boolean existeVirgula = false;
		StringBuilder sql = new StringBuilder("");
		for (Enum enumerado : enumerados) {
			if (existeVirgula) {
				sql.append(",").append("'").append(enumerado.name()).append("'");
			} else {
				sql.append("'").append(enumerado.name()).append("'");
				existeVirgula = true;
			}
		}
		return Uteis.isAtributoPreenchido(sql.toString()) ? sql.toString() : "'0'";
	}
	
	public static String converteListaSituacaoHistoricoParaCondicaoIn(List<? extends SituacaoHistorico> enumerados) {
		boolean existeVirgula = false;
		StringBuilder sql = new StringBuilder("");
		for (SituacaoHistorico enumerado : enumerados) {
			if (existeVirgula) {
				sql.append(",").append("'").append(enumerado.getValor()).append("'");
			} else {
				sql.append("'").append(enumerado.getValor()).append("'");
				existeVirgula = true;
			}
		}
		return Uteis.isAtributoPreenchido(sql.toString()) ? sql.toString() : "'0'";
	}

	public static void addLimitAndOffset(StringBuilder sb, Integer limite, Integer offset) {
		if (limite != null && limite > 0) {
			sb.append(" LIMIT ").append(limite);
			if (offset != null && offset >= 0) {
				sb.append(" OFFSET ").append(offset);
			}
		}
	}

	public static void addCampoParaClausaIn(StringBuilder sb, String campo, boolean isAspa) {
		if(isAspa){
			sb.append(sb.toString().isEmpty() ? "'"+campo+"'": "," + "'"+campo+"'");	
		}else{
			sb.append(sb.toString().isEmpty() ? campo : "," + campo);
		}
	}
	
	public static void addCampoParaClausaIn(StringBuilder sb, String campo) {
		addCampoParaClausaIn(sb, campo, false);
	}

	public static <P extends Object> void montarColunaExcelDinamicamente(Row linha, Cell coluna, int numeroColuna, CellStyle headerStyle, P valor) {
		int width = 0;
		coluna = linha.createCell(numeroColuna);
		if(headerStyle == null){
			// css padrao
			HSSFFont headerFont = (HSSFFont) coluna.getSheet().getWorkbook().createFont();
			headerFont.setFontHeightInPoints((short) 8);

			headerStyle = coluna.getSheet().getWorkbook().createCellStyle();
			headerStyle.setFillBackgroundColor(IndexedColors.BLACK.getIndex());
			headerStyle.setAlignment(HorizontalAlignment.CENTER);
			headerStyle.setFont(headerFont);
			headerStyle.setBorderTop(BorderStyle.MEDIUM);
			headerStyle.setBorderBottom(BorderStyle.MEDIUM);
			headerStyle.setBorderLeft(BorderStyle.MEDIUM);
			headerStyle.setBorderRight(BorderStyle.MEDIUM);
		}
		if (Objects.isNull(valor)) {
			throw new StreamSeiException("Valor nulo para Coluna.");
		}else if (valor instanceof String) {
			coluna.setCellValue((String) valor);
			headerStyle.setDataFormat((short)0);
			width = ((String) valor).length();
		} else if (valor instanceof Integer) {
			coluna.setCellValue((Integer) valor);
			headerStyle.setDataFormat((short)0);
			width = ((Integer) valor).toString().length();
		} else if (valor instanceof Long) {
			coluna.setCellValue((Long) valor);
			headerStyle.setDataFormat((short)0);
			width = ((Long) valor).toString().length();
		} else if (valor instanceof BigDecimal) {
			coluna.setCellValue(((BigDecimal) valor).doubleValue());
			headerStyle.setDataFormat((short)8);
			width = ((BigDecimal) valor).toString().length();
		} else if (valor instanceof Date) {
			coluna.setCellValue((Date) valor);			
			headerStyle.setDataFormat((short)16);
			width = ((Date) valor).toString().length();
		} else if (valor instanceof Boolean) {
			coluna.setCellValue((Boolean) valor);
			headerStyle.setDataFormat((short)0);
			width = ((Boolean) valor).toString().length();
		} else if (valor instanceof Double) {
			coluna.setCellValue((Double) valor);
			headerStyle.setDataFormat((short)8);
			width = ((Double) valor).toString().length();
		} else {
			throw new StreamSeiException("Valor para Coluna não aceitavel.");
		}
		coluna.setCellStyle(headerStyle);
		width = width * 310;//310 e valor em pixel
		if (width != 0 && width > coluna.getSheet().getColumnWidth(numeroColuna)) {
			coluna.getSheet().setColumnWidth(numeroColuna, width);
		}
	}

	private static final String ZERO = "0";

	public static String leftPadWithZeros(String input, int expectedSize) {
		if (input == null) {
			return leftPadWithZeros("", expectedSize);
		}

		StringBuilder sb = new StringBuilder(expectedSize);

		for (int i = expectedSize - input.length(); i > 0; i--) {
			sb.append(ZERO);
		}

		sb.append(input);
		return sb.toString();
	}

	/**
	 * Método para auxiliar a verificação de String nula e adicionar um prefixo caso não seja.
	 * 
	 * @param str
	 *            Campo desejado para fazer a verificação.
	 * @param defaultStr
	 *            Caso a str for nula, esse campo será usado no lugar.
	 * @param prefix
	 *            Adicionar um prefixo apenas se a str não for nula.
	 * @return string tratada
	 */
	public static String prefixNotNullStringOrDefault(String str, String defaultStr, String prefix) {
		if (str == null) {
			return defaultStr;
		}
		return prefix + str;
	}

	/**
	 * Método para auxiliar a verificação de String nula FiltroPore adicionar um prefixo caso não seja.
	 * 
	 * @param str
	 *            Campo desejado para fazer a verificação.
	 * @param defaultStr
	 *            Caso a str for nula, esse campo será usado no lugar.
	 * @param suffix
	 *            Adicionar um sufixo apenas se a str não for nula.
	 * @return string tratada
	 */
	public static String suffixNotNullStringOrDefault(String str, String defaultStr, String suffix) {
		if (str == null) {
			return defaultStr;
		}
		return str + suffix;
	}	
	
	
	public static String montarStringDeFiltroPorEnumerado(Enum<?>[] enumerado, String[] situacoes, String campo) {
		return montarStringDeFiltroPorEnumerado(enumerado, situacoes, campo, false);
	}

	/**
	 * Retorna uma string com filtro 
	 * 
	 *  Ex.: campo IN ('valorNameDoEnumerado1', ...) 
	 *  
	 * @param enumerado
	 * @param situacoes
	 * @param campo
	 * @return
	 */
	public static String montarStringDeFiltroPorEnumerado(Enum<?>[] enumerado, String[] situacoes, String campo, boolean filtroTemplateLancamentoFolha) {
		String situacaoPronta = "";

		for (Enum<?> enum1 : enumerado) {
			for(String tipo : situacoes) {

				if(tipo.equals(enum1.name())) {
					situacaoPronta +=",";
					if (filtroTemplateLancamentoFolha) {
						situacaoPronta += "'" + enum1.name() + "'";
					} else {
						situacaoPronta += "'" + enum1.name() + ";'";	
					}
				}
			}
		}

		if(situacaoPronta != "") {
			situacaoPronta = " AND " + campo + " IN (" + situacaoPronta.substring(1, situacaoPronta.length()) + ")";
		}

		return situacaoPronta;
	}  
	
	/**
	 * Calcula a quantidade de vezes que uma palavra se repete em uma String.
	 * 
	 * @param texto
	 * @param palavraChave
	 * @return
	 */
	public static int quantidadePalavrasRepetidas(String texto, String palavraChave) {
		int quant = 0;
		String [] arrayString = texto.replace("+", " ").replace("-", " ").replace("*", " ").replace("/", " ").split(" ");
		for(int i=0;i<arrayString.length;i++){
			if(arrayString[i].contains(palavraChave)){
				quant++;
			}
		}
		return quant;
	}
	public static String primeiraLetraMaiuscula(String obj) {
        String[] partes = obj.toLowerCase().split(" ");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < partes.length; i++) {
            String variavel = partes[i];
            String[] novo = variavel.split("'");
            if (variavel.equalsIgnoreCase("do") || variavel.equalsIgnoreCase("da") || variavel.equalsIgnoreCase("de") || variavel.equalsIgnoreCase("di") || variavel.equalsIgnoreCase("dos") || variavel.equalsIgnoreCase("e") || variavel.equalsIgnoreCase("")) {
            	sb.append(" ").append(variavel);
			}else {				
				if (novo.length > 1) {
					variavel = variavel.substring(0,2) + variavel.substring(2, 3).toUpperCase() + variavel.substring(3);
			        sb.append(" ").append(variavel);
				}else {
				variavel = variavel.substring(0, 1).toUpperCase() + variavel.substring(1);
		        sb.append(" ").append(variavel);
				}
			}
        }
		return sb.toString();
	}
     
	/**
	 * Forma uma string de acordo com o valor e a maskara informada.
	 * 
	 * @param valor - Exemplo de valor: 07923215000123
	 * @param maskara - Exemplo de maskara: Formatar CNPJ: '###.###.###/####-##'
	 * @throws ParseException
	 */
	public static String formatarStringComMaskara(String valor, String maskara) {
	    try {
	        MaskFormatter mask = new MaskFormatter(maskara);
	        mask.setValueContainsLiteralCharacters(false);
	        return mask.valueToString(valor);
	    } catch (ParseException ex) {
	        System.out.println(ex.getMessage());;
	    }
		return "";
	}
	
	public static boolean isValidEmailAddress(String email) {
	    boolean result = true;
	    try {
	        InternetAddress emailAddr = new InternetAddress(email);
	        emailAddr.validate();
	    } catch (AddressException ex) {
	        result = false;
	    }
	    return result;
	}

	public static Map<String, String> extrairValoresChave(String mensagemErro) {
		Map<String, String> valores = new HashMap<>();

		// Encontrar a parte da mensagem que contém os valores da chave
		int inicio = mensagemErro.indexOf("(");
		int fim = mensagemErro.indexOf(")=(", inicio);

		if (inicio != -1 && fim != -1) {
			// Extrair os nomes dos campos
			String camposString = mensagemErro.substring(inicio + 1, fim);
			String[] campos = camposString.split(", ");

			// Encontrar os valores correspondentes
			int inicioValores = mensagemErro.indexOf("=(", fim) + 2;
			int fimValores = mensagemErro.indexOf(")", inicioValores);

			if (inicioValores != -1 && fimValores != -1) {
				String valoresString = mensagemErro.substring(inicioValores, fimValores);
				String[] valoresCampos = valoresString.split(", ");

				// Associar campos e valores
				for (int i = 0; i < campos.length && i < valoresCampos.length; i++) {
					valores.put(campos[i].trim(), valoresCampos[i].trim());
				}
			}
		}

		return valores;
	}

}
