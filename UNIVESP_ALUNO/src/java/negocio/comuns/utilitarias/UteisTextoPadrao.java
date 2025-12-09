package negocio.comuns.utilitarias;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.itextpdf.text.BaseColor;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.basico.enumeradores.TipoCampoTagTextoPadraoEnum;
import negocio.interfaces.basico.TagTextoPadraoInterfaceEnum;
import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionChunk;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.base.JRBaseExpressionChunk;
import net.sf.jasperreports.engine.base.JRBaseImage;
import net.sf.jasperreports.engine.base.JRBaseSubreport;
import net.sf.jasperreports.engine.base.JRBaseTextField;
import net.sf.jasperreports.engine.util.JRLoader;

public class UteisTextoPadrao {

	public static int realizarObtecaoTamanhoTag(String tag) throws Exception {
		int posicaoIni = tag.indexOf("(");
		int posicaoFim = tag.indexOf(")");
		if ((posicaoIni == -1) || (posicaoFim == -1)) {
			return 0;
		}
		Integer num = new Integer(tag.substring(posicaoIni + 1, posicaoFim));
		return num.intValue();
	}

	public static String realizarObtecaoTextoTag(String tag) {
		int posicaoIni = tag.indexOf("{");
		int posicaoFim = tag.indexOf("}");
		if ((posicaoIni == -1) || (posicaoFim == -1)) {
			return "";
		}

		return tag.substring(posicaoIni + 1, posicaoFim);
	}

	public static String realizarObtecaoTextoTagPosterior(String tag) {
		if (tag.lastIndexOf("{") != tag.indexOf("{") && tag.lastIndexOf("{") > 0) {
			int posicaoIni = tag.lastIndexOf("{");
			int posicaoFim = tag.lastIndexOf("}");
			if ((posicaoIni == -1) || (posicaoFim == -1)) {
				return "";
			}
			return tag.substring(posicaoIni + 1, posicaoFim);
		}
		return "";
	}

	public static String[] realizarObtecaoSubTags(String tag) {
		if (tag.indexOf("|") > 0 && tag.lastIndexOf("|") > 0 && tag.indexOf("|") != tag.lastIndexOf("|")) {
			int posicaoIni = tag.indexOf("|");
			int posicaoFim = tag.lastIndexOf("|");
			tag = tag.substring(posicaoIni + 1, posicaoFim);
			if ((posicaoIni == -1) || (posicaoFim == -1) || (posicaoIni > posicaoFim)) {
				return null;
			}

			return tag.split(",");
		}
		return null;
	}

	public static String realizarObtencaoTag(String tag) {
		int posicao1 = tag.indexOf(")");
		int posicao2 = tag.indexOf("}");
		if (posicao2 != -1) {
			tag = tag.substring(posicao2 + 1, tag.length());
		} else if (posicao1 != -1) {
			tag = tag.substring(posicao1 + 1, tag.length());
		} else {
			return "";
		}
		if (tag.endsWith("]")) {
			tag = tag.substring(0, tag.length() - 1);
		}
		if (tag.contains("|")) {
			tag = tag.substring(0, tag.indexOf("|"));
		}
		posicao1 = tag.indexOf("{");
		if (posicao1 > 0) {
			tag = tag.substring(0, posicao1);
		}
		return tag.trim();
	}

	public static String adicionarTag(String texto, TagTextoPadraoInterfaceEnum marcador) {
		int parametro = texto.lastIndexOf("</p>");
		if (parametro == -1) {
			parametro = texto.lastIndexOf("</body>");
		}
		String textoAntes = texto.substring(0, parametro);
		String textoDepois = texto.substring(parametro, texto.length());
		if (marcador.getTipoCampo().equals(TipoCampoTagTextoPadraoEnum.LISTA)) {
			texto = textoAntes + "</p><div style=\"width:100%\">" + marcador.getTag() + "</div></p>" + textoDepois;
		} else {
			texto = textoAntes + " " + marcador.getTag() + textoDepois;
		}
		return texto;
	}

	public static String realizarSubstituicaoUrlImagem(String texto, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) {
		return texto.replaceAll(configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo() + "/", configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator);
	}

	public static String realizarObtencaoTagCabecalho(String texto) {
		return texto.substring(texto.indexOf("<header>"), texto.indexOf("</header>") + 7);
	}

	public static String realizarObtencaoTagCorpo(String texto) {
		return texto.substring(texto.indexOf("<body>"), texto.lastIndexOf("</body>") + 6);
	}

	public static String realizarObtencaoTagRodape(String texto) {
		return texto.substring(texto.indexOf("<footer>"), texto.lastIndexOf("</footer>") + 9);
	}

	public static String realizarSubstituicaoTag(String texto, TagTextoPadraoInterfaceEnum tag, Object valorSubstituir) throws Exception {
		String valorFormatado = getObterValorFormatado(tag, valorSubstituir);
		String tagUsar = UteisTextoPadrao.realizarObtencaoTag(tag.getTag());
		Pattern pTag = Pattern.compile("\\[(.*?)" + tagUsar + "\\]", Pattern.CASE_INSENSITIVE);

		Matcher mTag = pTag.matcher(texto);
		while (mTag.find()) {
			String tagUtilizada = mTag.group();
			String[] textos = tagUtilizada.split("]");
			for (String text : textos) {
				if (text.contains(tagUsar)) {
					tagUtilizada = text + "]";
					if (!tagUtilizada.startsWith("[")) {
						tagUtilizada = tagUtilizada.substring(tagUtilizada.indexOf("["), tagUtilizada.length());
					}
					break;
				}
			}
			String textoAnterior = "";
			if (!tag.getTipoCampo().equals(TipoCampoTagTextoPadraoEnum.LISTA)) {
				int tamanho = realizarObtecaoTamanhoTag(tagUtilizada);
				textoAnterior = realizarObtecaoTextoTag(tagUtilizada);
				if (tamanho == 0) {
					tamanho = valorFormatado.length();
				}
				if (tamanho < valorFormatado.length()) {
					valorFormatado = valorFormatado.substring(0, tamanho);
				}
			}
			texto = texto.replace(tagUtilizada, textoAnterior + valorFormatado);
			return texto;
		}
		return texto;
	}

	public static String getObterValorFormatado(TagTextoPadraoInterfaceEnum tag, Object valorSubstituir) {
		if (valorSubstituir != null) {
			switch (tag.getTipoCampo()) {
			case BOOLEAN:
				if (valorSubstituir instanceof Boolean) {
					return (Boolean) valorSubstituir ? "Sim" : "Não";
				}
				break;
			case DATA:
				if (valorSubstituir instanceof Date) {
					return Uteis.getData((Date) valorSubstituir);
				}
			case DATA_COM_HORA:
				if (valorSubstituir instanceof Date) {
					return Uteis.getDataComHora((Date) valorSubstituir);
				}
				break;
			case DOUBLE:
				if (valorSubstituir instanceof Double) {
					return Uteis.getDoubleFormatado((Double) valorSubstituir);
				} else if (valorSubstituir instanceof Float) {
					return Uteis.getDoubleFormatado(((Float) valorSubstituir).doubleValue());
				} else if (valorSubstituir instanceof BigDecimal) {
					return Uteis.getDoubleFormatado(((BigDecimal) valorSubstituir).doubleValue());
				}
				break;
			case ENUM:
				if (valorSubstituir instanceof Enum) {
					return UteisJSF.internacionalizar("enum_" + ((Enum<?>) valorSubstituir).getClass().getName() + "_" + ((Enum<?>) valorSubstituir).name());
				}
				break;
			case INTEGER:
				if (valorSubstituir instanceof Integer) {
					return ((Integer) valorSubstituir).toString();
				}
				break;
			case MES_ANO:
				if (valorSubstituir instanceof Date) {
					return Uteis.getDataMesAnoConcatenado((Date) valorSubstituir);
				}
				break;
			case STRING:
				if (valorSubstituir instanceof String) {
					return (String) valorSubstituir;
				}
				break;
			case LISTA:
				if (valorSubstituir instanceof String) {
					return (String) valorSubstituir;
				}
				break;

			default:
				break;
			}
		}
		return "";
	}

	public static Float converterCentimetroParaPontos(Float cm) {
		return (cm * 72f) / 2.54f;
	}

	public static Float converterPontosParaCentimetro(Float cm) {
		return (cm / 72f) * 2.54f;
	}

	public static String carregarTagsTextoPadraoPorTipoDesignerIreport(File arquivoIReport) throws JRException {
		List<String> listaTagsJaUsada  = new ArrayList<>();
		StringBuilder tags = new StringBuilder();
		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(arquivoIReport);

		obterTagsBandRelatorio(jasperReport.getBackground(), listaTagsJaUsada, tags);
		obterTagsBandRelatorio(jasperReport.getTitle(), listaTagsJaUsada, tags);
		obterTagsBandRelatorio(jasperReport.getSummary(), listaTagsJaUsada, tags);
		obterTagsBandRelatorio(jasperReport.getNoData(), listaTagsJaUsada, tags);
		obterTagsBandRelatorio(jasperReport.getColumnHeader(), listaTagsJaUsada, tags);
		obterTagsBandRelatorio(jasperReport.getPageHeader(), listaTagsJaUsada, tags);

		if (jasperReport.getDetailSection() != null && jasperReport.getDetailSection().getBands() != null) {
			for (int i = 0; i < jasperReport.getDetailSection().getBands().length; i++) {
				JRBand jrBand = jasperReport.getDetailSection().getBands()[i];
				obterTagsBandRelatorio(jrBand, listaTagsJaUsada, tags);
			}
		}
		/*
		 * protected JRBand pageHeader; protected JRBand columnHeader; protected
		 * JRSection detailSection; protected JRBand columnFooter; protected
		 * JRBand pageFooter; protected JRBand lastPageFooter;
		 */
		if (jasperReport.getGroups() != null) {
			for (int i = 0; i < jasperReport.getGroups().length; i++) {
				JRGroup jrGroup = jasperReport.getGroups()[i];
				if (jrGroup.getGroupFooterSection() != null && jrGroup.getGroupFooterSection().getBands() != null) {
					for (int j = 0; j < jrGroup.getGroupFooterSection().getBands().length; j++) {
						JRBand jrBand = jrGroup.getGroupFooterSection().getBands()[j];
						obterTagsBandRelatorio(jrBand, listaTagsJaUsada, tags);
					}
				}
				if (jrGroup.getGroupHeaderSection() != null && jrGroup.getGroupHeaderSection().getBands() != null) {
					for (int h = 0; h < jrGroup.getGroupHeaderSection().getBands().length; h++) {
						JRBand jrBand = jrGroup.getGroupHeaderSection().getBands()[h];
						obterTagsBandRelatorio(jrBand, listaTagsJaUsada, tags);
					}
				}
			}
		}
		StringBuilder html = new StringBuilder();
		/*
		 * html.append("<html>"); html.append("<body>"); html.append("<div>");
		 */
		html.append(tags);
		/*
		 * html.append("</div>"); html.append("</body>");
		 * html.append("</html>");
		 */
		return html.toString();
	}

	public static String carregarTagsTextoPadraoPorTipoDesignerIreportProcessoSeletivo(File arquivoIReport) throws JRException {
		List<String> listaTagsJaUsada  = new ArrayList<>();
		StringBuilder tags = new StringBuilder();
		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(arquivoIReport);
		
		obterTagsBandRelatorioProcessoSeletivo(jasperReport.getBackground(), listaTagsJaUsada, tags);
		obterTagsBandRelatorioProcessoSeletivo(jasperReport.getTitle(), listaTagsJaUsada, tags);
		obterTagsBandRelatorioProcessoSeletivo(jasperReport.getSummary(), listaTagsJaUsada, tags);
		obterTagsBandRelatorioProcessoSeletivo(jasperReport.getNoData(), listaTagsJaUsada, tags);
		
		if (jasperReport.getDetailSection() != null && jasperReport.getDetailSection().getBands() != null) {
			for (int i = 0; i < jasperReport.getDetailSection().getBands().length; i++) {
				JRBand jrBand = jasperReport.getDetailSection().getBands()[i];
				obterTagsBandRelatorioProcessoSeletivo(jrBand, listaTagsJaUsada, tags);
			}
		}
		/*
		 * protected JRBand pageHeader; protected JRBand columnHeader; protected
		 * JRSection detailSection; protected JRBand columnFooter; protected
		 * JRBand pageFooter; protected JRBand lastPageFooter;
		 */
		if (jasperReport.getGroups() != null) {
			for (int i = 0; i < jasperReport.getGroups().length; i++) {
				JRGroup jrGroup = jasperReport.getGroups()[i];
				if (jrGroup.getGroupFooterSection() != null && jrGroup.getGroupFooterSection().getBands() != null) {
					for (int j = 0; j < jrGroup.getGroupFooterSection().getBands().length; j++) {
						JRBand jrBand = jrGroup.getGroupFooterSection().getBands()[j];
						obterTagsBandRelatorioProcessoSeletivo(jrBand, listaTagsJaUsada, tags);
					}
				}
				if (jrGroup.getGroupHeaderSection() != null && jrGroup.getGroupHeaderSection().getBands() != null) {
					for (int h = 0; h < jrGroup.getGroupHeaderSection().getBands().length; h++) {
						JRBand jrBand = jrGroup.getGroupHeaderSection().getBands()[h];
						obterTagsBandRelatorioProcessoSeletivo(jrBand, listaTagsJaUsada, tags);
					}
				}
			}
		}
		StringBuilder html = new StringBuilder();
		/*
		 * html.append("<html>"); html.append("<body>"); html.append("<div>");
		 */
		html.append(tags);
		/*
		 * html.append("</div>"); html.append("</body>");
		 * html.append("</html>");
		 */
		return html.toString();
	}

	private static void obterTagsBandRelatorio(JRBand banda, List<String> listaTagsJaUsada ,StringBuilder tags) {
		if (banda != null) {
			JRExpression jrExpression = null;
			if (banda.getPrintWhenExpression() != null && Uteis.isAtributoPreenchido(banda.getPrintWhenExpression().getText())){
				forChunks: for (int j = 0; j < banda.getPrintWhenExpression().getChunks().length; j++) {
					JRExpressionChunk jrExpressionChunk = banda.getPrintWhenExpression().getChunks()[j];
					if (jrExpressionChunk.getType() == JRExpressionChunk.TYPE_PARAMETER && !listaTagsJaUsada.contains(jrExpressionChunk.getText())) {
						if (containsTagsRelatorio(Dominios.getMarcadoAluno(), jrExpressionChunk.getText(), listaTagsJaUsada, tags)) {
							continue forChunks;
						}
						if (containsTagsRelatorio(Dominios.getMarcadoContaReceber(), jrExpressionChunk.getText(), listaTagsJaUsada, tags)) {
							continue forChunks;
						}
						if (containsTagsRelatorio(Dominios.getMarcadoCurso(), jrExpressionChunk.getText(), listaTagsJaUsada, tags)) {
							continue forChunks;
						}
						if (containsTagsRelatorio(Dominios.getMarcadoDisciplina(), jrExpressionChunk.getText(), listaTagsJaUsada, tags)) {
							continue forChunks;
						}
						if (containsTagsRelatorio(Dominios.getMarcadoDisciplinaDeclaracao(), jrExpressionChunk.getText(), listaTagsJaUsada, tags)) {
							continue forChunks;
						}
						if (containsTagsRelatorio(Dominios.getMarcadoEstagio(), jrExpressionChunk.getText(), listaTagsJaUsada, tags)) {
							continue forChunks;
						}
						if (containsTagsRelatorio(Dominios.getMarcadoInscProcSeletivo(), jrExpressionChunk.getText(), listaTagsJaUsada, tags)) {
							continue forChunks;
						}
						if (containsTagsRelatorio(Dominios.getMarcadoMatricula(), jrExpressionChunk.getText(), listaTagsJaUsada, tags)) {
							continue forChunks;
						}
						if (containsTagsRelatorio(Dominios.getMarcadoOutras(), jrExpressionChunk.getText(), listaTagsJaUsada, tags)) {
							continue forChunks;
						}
						if (containsTagsRelatorio(Dominios.getMarcadoProfessor(), jrExpressionChunk.getText(), listaTagsJaUsada, tags)) {
							continue forChunks;
						}
						if (containsTagsRelatorio(Dominios.getMarcadoUnidadeEnsino(), jrExpressionChunk.getText(), listaTagsJaUsada, tags)) {
							continue forChunks;
						}
						tags.append("[(){}").append(jrExpressionChunk.getText()).append("] \r\n");
						listaTagsJaUsada.add(jrExpressionChunk.getText());
					}
				}
			}
			for (int i = 0; i < banda.getElements().length; i++) {
				JRElement elemento = banda.getElements()[i];
				if (elemento instanceof JRBaseTextField) {
					jrExpression = ((JRBaseTextField) elemento).getExpression();
				} else if (elemento instanceof JRBaseImage) {
					jrExpression = ((JRBaseImage) elemento).getExpression();
				} else if (elemento instanceof JRBaseSubreport) {
					jrExpression = ((JRBaseSubreport) elemento).getDataSourceExpression();
				}
				if (jrExpression != null && jrExpression.getChunks() != null) {
					forChunks: for (int j = 0; j < jrExpression.getChunks().length; j++) {
						JRExpressionChunk jrExpressionChunk = jrExpression.getChunks()[j];
						if (jrExpressionChunk.getType() == JRExpressionChunk.TYPE_PARAMETER && !listaTagsJaUsada.contains(jrExpressionChunk.getText())) {
							if (containsTagsRelatorio(Dominios.getMarcadoAluno(), jrExpressionChunk.getText(), listaTagsJaUsada, tags)) {
								continue forChunks;
							}
							if (containsTagsRelatorio(Dominios.getMarcadoContaReceber(), jrExpressionChunk.getText(), listaTagsJaUsada, tags)) {
								continue forChunks;
							}
							if (containsTagsRelatorio(Dominios.getMarcadoCurso(), jrExpressionChunk.getText(), listaTagsJaUsada, tags)) {
								continue forChunks;
							}
							if (containsTagsRelatorio(Dominios.getMarcadoDisciplina(), jrExpressionChunk.getText(), listaTagsJaUsada, tags)) {
								continue forChunks;
							}
							if (containsTagsRelatorio(Dominios.getMarcadoDisciplinaDeclaracao(), jrExpressionChunk.getText(), listaTagsJaUsada, tags)) {
								continue forChunks;
							}
							if (containsTagsRelatorio(Dominios.getMarcadoEstagio(), jrExpressionChunk.getText(), listaTagsJaUsada, tags)) {
								continue forChunks;
							}
							if (containsTagsRelatorio(Dominios.getMarcadoInscProcSeletivo(), jrExpressionChunk.getText(), listaTagsJaUsada, tags)) {
								continue forChunks;
							}
							if (containsTagsRelatorio(Dominios.getMarcadoMatricula(), jrExpressionChunk.getText(), listaTagsJaUsada, tags)) {
								continue forChunks;
							}
							if (containsTagsRelatorio(Dominios.getMarcadoOutras(), jrExpressionChunk.getText(), listaTagsJaUsada, tags)) {
								continue forChunks;
							}
							if (containsTagsRelatorio(Dominios.getMarcadoProfessor(), jrExpressionChunk.getText(), listaTagsJaUsada, tags)) {
								continue forChunks;
							}
							if (containsTagsRelatorio(Dominios.getMarcadoUnidadeEnsino(), jrExpressionChunk.getText(), listaTagsJaUsada, tags)) {
								continue forChunks;
							}
							tags.append("[(){}").append(jrExpressionChunk.getText()).append("] \r\n");
							listaTagsJaUsada.add(jrExpressionChunk.getText());
						}
					}
				}

			}
		}
	}

	private static void obterTagsBandRelatorioProcessoSeletivo(JRBand banda, List<String> listaTagsJaUsada ,StringBuilder tags) {
		if (banda != null) {
			JRExpression jrExpression = null;
			for (int i = 0; i < banda.getElements().length; i++) {
				JRElement elemento = banda.getElements()[i];
				if (elemento instanceof JRBaseTextField) {
					jrExpression = ((JRBaseTextField) elemento).getExpression();
				} else if (elemento instanceof JRBaseSubreport) {
					jrExpression = ((JRBaseSubreport) elemento).getDataSourceExpression();
				}
				if (jrExpression != null) {
					forChunks: for (int j = 0; j < jrExpression.getChunks().length; j++) {
						JRExpressionChunk jrExpressionChunk = jrExpression.getChunks()[j];
						if (jrExpressionChunk.getType() == JRExpressionChunk.TYPE_PARAMETER && !listaTagsJaUsada.contains(jrExpressionChunk.getText())) {
							if (containsTagsRelatorio(Dominios.getMarcadoInscricao(), jrExpressionChunk.getText(), listaTagsJaUsada, tags)) {
								continue forChunks;
							}
							if (containsTagsRelatorio(Dominios.getMarcadoCandidato(), jrExpressionChunk.getText(), listaTagsJaUsada, tags)) {
								continue forChunks;
							}
							if (containsTagsRelatorio(Dominios.getMarcadoProcessoSeletivo(), jrExpressionChunk.getText(), listaTagsJaUsada, tags)) {
								continue forChunks;
							}
							if (containsTagsRelatorio(Dominios.getMarcadoResultadoProcessoSeletivo(), jrExpressionChunk.getText(), listaTagsJaUsada, tags)) {
								continue forChunks;
							}
							if (containsTagsRelatorio(Dominios.getMarcadoListaProcessoSeletivo(), jrExpressionChunk.getText(), listaTagsJaUsada, tags)) {
								continue forChunks;
							}
							tags.append("[(){}").append(jrExpressionChunk.getText()).append("] \r\n");
							listaTagsJaUsada.add(jrExpressionChunk.getText());
						}
					}
				}
				
			}
		}
	}

	@SuppressWarnings("rawtypes")
	private static boolean containsTagsRelatorio(Hashtable marcado, String tag, List<String> listaTagsJaUsada, StringBuilder tags) {
		for (Object o : marcado.entrySet()) {
			Map.Entry pair = (Map.Entry) o;
			if (pair.getValue().equals(tag)) {
				tags.append(pair.getKey()).append(" \r\n");
				listaTagsJaUsada.add(tag);
				return true;
			}
		}
		return false;
	}

	public static BaseColor converterCorHexadecimalParaRgb(String hexString)  {
		BaseColor c = new BaseColor(Integer.valueOf(hexString.substring(1, 3), 16), Integer.valueOf(hexString.substring(3, 5), 16), Integer.valueOf(hexString.substring(5, 7), 16));
		return c;
	}
	
	private static List<String> carregarNomeSubrelatorio(JRBand jrBand, String caminhoBase, List<String> listaSubrelatorio) throws JRException{
		for(JRElement element: jrBand.getElements()) {
			if(element instanceof JRBaseSubreport) {
				listaSubrelatorio = carregarNomeSubrelatorio((JRBaseSubreport)element, caminhoBase, listaSubrelatorio);								
			}
		}
		return listaSubrelatorio;
	}
	
	private static List<String> carregarNomeSubrelatorio(JRBaseSubreport jrBaseSubreport, String caminhoBase, List<String> listaSubrelatorio) throws JRException{
		
			for(int x = 0; x < jrBaseSubreport.getExpression().getChunks().length; x++) {
				JRBaseExpressionChunk chunck = (JRBaseExpressionChunk)jrBaseSubreport.getExpression().getChunks()[x];
				if(chunck.getType() == 1) {
					String subreport = chunck.getText().substring(chunck.getText().indexOf("\"")+1, chunck.getText().lastIndexOf("\""));
					if(!listaSubrelatorio.contains(subreport)) {
						listaSubrelatorio.add(subreport);
						listaSubrelatorio = carregarNomeSubrelatorio(caminhoBase, subreport,  listaSubrelatorio);
					}
				}
			}
		
		return listaSubrelatorio;
	}
	
	public static List<String> carregarNomeSubrelatorio(String caminhoBase, String nomeDesigner, List<String> listaSubrelatorio) throws JRException {	
		if(!nomeDesigner.endsWith(".jasper")) {
			nomeDesigner += ".jasper";
		}
		JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile((caminhoBase+File.separator+nomeDesigner));		
		listaSubrelatorio = carregarNomeSubrelatorio(jasperReport, caminhoBase, listaSubrelatorio);
		return listaSubrelatorio;
	}
	
	private static List<String> carregarNomeSubrelatorio(JasperReport jasperReport, String caminhoBase, List<String> listaSubrelatorio) throws JRException {
		for(JRBand band : jasperReport.getAllBands()) {
			listaSubrelatorio = carregarNomeSubrelatorio(band, caminhoBase, listaSubrelatorio);
		}
		return listaSubrelatorio;		
	}
}
