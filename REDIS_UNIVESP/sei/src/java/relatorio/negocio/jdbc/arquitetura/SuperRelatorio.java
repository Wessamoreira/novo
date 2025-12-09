package relatorio.negocio.jdbc.arquitetura;

import java.io.StringWriter;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import negocio.comuns.arquitetura.UsuarioVO;

import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;

/**
 * SuperRelatorio é uma classe padrão para que encapsulam as operações básicas
 * relativas a emissão de relatórios. Verificar a permissão do usuário para
 * realizar a emissão de um determinado relatório.
 */
public class SuperRelatorio extends ControleAcesso {

	protected String xmlRelatorio;
	protected Vector ordenacoesRelatorio;
	protected int ordenarPor;
	private String descricaoFiltros;

	public SuperRelatorio() {
		setXmlRelatorio("");
		setOrdenacoesRelatorio(new Vector());
		setOrdenarPor(0);
		setDescricaoFiltros("");
	}

	public void adicionarDescricaoFiltro(String filtro) {
		if (!descricaoFiltros.equals("")) {
			descricaoFiltros = descricaoFiltros + ";" + filtro;
		} else {
			descricaoFiltros = filtro;
		}
	}

	/**
	 * Operação padrão para realizar o EMITIR UM RELATÓRIO de dados de uma
	 * entidade no BD. Verifica e inicializa (se necessário) a conexão com o BD.
	 * Verifica se o usuário logado possui permissão de acesso a operação
	 * EMITIRRELATORIO.
	 * 
	 * @param idEntidade
	 *            Nome da entidade para a qual se deseja realizar a operação.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso a
	 *                esta operação.
	 */
	public static void emitirRelatorio(String idEntidade, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		ControleAcesso.emitirRelatorio(idEntidade, verificarAcesso, usuarioVO);
	}

	public String adicionarCondicionalWhere(String whereStr, String filtro, boolean operadorAND) {
		if (!operadorAND) {
			return filtro;
		} else {
			return whereStr + " AND " + filtro;
		}
	}

	public String getStringFromDocument(Document doc) {
		try {
			DOMSource domSource = new DOMSource(doc);
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.transform(domSource, result);
			String xmlConvertido = writer.toString();
			xmlConvertido = xmlConvertido.replaceFirst("UTF-8", "ISO-8859-1");
			return xmlConvertido;
		} catch (TransformerException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public void converterResultadoConsultaParaXML(SqlRowSet resultadoConsulta, String nomeRelatorio, String nomeRegistro) throws Exception {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.newDocument();
		// Criando o nó raiz do XML - com o nome padrão do relatório
		Element root = doc.createElement(nomeRelatorio);
		// Obtendo as informações de estrutura do ResultSet - METADados
		SqlRowSetMetaData metaDadosConsulta = resultadoConsulta.getMetaData();

		Element linhaXML;
		Element e;

		int contador = 0;

		int cols = metaDadosConsulta.getColumnCount();
		resultadoConsulta.beforeFirst();
		while (resultadoConsulta.next()) {
			contador++;
			linhaXML = doc.createElement(nomeRegistro);

			for (int j = 1; j <= cols; j++) {
				String nomeTabela = metaDadosConsulta.getTableName(j);
				String nomeColuna = metaDadosConsulta.getColumnName(j);
				String nomeClasse = metaDadosConsulta.getColumnClassName(j);
				String valorColuna = " ";
				if (nomeClasse.equals("java.sql.Timestamp")) {
					valorColuna = Uteis.getData(resultadoConsulta.getDate(j));
					if ((valorColuna == null) || (valorColuna.equals("")) || (valorColuna.equals("null"))) {
						valorColuna = " ";
					}
				} else if (nomeClasse.equals("java.lang.Double")) {
					valorColuna = String.valueOf(resultadoConsulta.getDouble(j));
					if (valorColuna == null) {
						valorColuna = "0.0";
					}
				} else if (nomeClasse.equals("java.lang.Float")) {
					valorColuna = String.valueOf(resultadoConsulta.getFloat(j));
					if (valorColuna == null) {
						valorColuna = "0.0";
					}
				} else {
					valorColuna = resultadoConsulta.getString(j);
					// if (nomeColuna.equals("dia") || nomeColuna.equals("mes"))
					// {
					// int posicao = valorColuna.lastIndexOf(".");
					// valorColuna.substring(0,posicao + 1);
					// valorColuna = " ";
					// }
					if ((valorColuna == null) || (valorColuna.equals("")) || (valorColuna.equals("null"))) {
						valorColuna = " ";
					}
				}

				e = doc.createElement(nomeColuna);
				e.appendChild(doc.createTextNode(valorColuna));
				linhaXML.appendChild(e);
			}
			root.appendChild(linhaXML);
		}
		if (contador == 0) {
			throw new ConsistirException("Não há resultados a serem exibidos neste relatório.");
		}
		doc.appendChild(root);
		this.setXmlRelatorio(getStringFromDocument(doc));

		/*
		 * //perform XSL transformation DOMSource source = new DOMSource(doc);
		 * //OutputStream outputStream = new OutputStream(); OutputStream buffer
		 * = new OutputStream(); OutputStreamWriter outSW = new
		 * OutputStreamWriter(buffer, "ISO-8859-1"); StreamResult result = new
		 * StreamResult("customers.xml"); TransformerFactory tmf =
		 * TransformerFactory.newInstance();
		 * tmf.newTransformer().transform(source,result); //result.getWriter()
		 * FileOutputStream outFile = new
		 * FileOutputStream(this.getArquivoCodigo()); String conteudo =
		 * this.getText(); for (int i =0; i < conteudo.length(); i++) {
		 * outSW.append(conteudo.charAt(i)); } outSW.close();
		 */
	}

	public String getXmlRelatorio() {
		return xmlRelatorio;
	}

	public void setXmlRelatorio(String xmlRelatorio) {
		this.xmlRelatorio = xmlRelatorio;
	}

	public Vector getOrdenacoesRelatorio() {
		return ordenacoesRelatorio;
	}

	public void setOrdenacoesRelatorio(Vector ordenacoesRelatorio) {
		this.ordenacoesRelatorio = ordenacoesRelatorio;
	}

	public int getOrdenarPor() {
		return ordenarPor;
	}

	public void setOrdenarPor(int ordenarPor) {
		this.ordenarPor = ordenarPor;
	}

	public String getDescricaoFiltros() {
		return descricaoFiltros;
	}

	public void setDescricaoFiltros(String descricaoFiltros) {
		this.descricaoFiltros = descricaoFiltros;
	}
	
	public String getFiltroPeriodo(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, String campo){
		campo = campo.trim();		
		StringBuilder selectStr = new StringBuilder();
		String and = "";
		if (filtroRelatorioAcademicoVO.getFiltrarPorPeriodoData()) {
			selectStr.append("  ").append(campo).append(".data::DATE>= '").append(Uteis.getDataJDBC(filtroRelatorioAcademicoVO.getDataInicio())).append("' and ").append(campo).append(".data::DATE <= '").append(Uteis.getDataJDBC(filtroRelatorioAcademicoVO.getDataTermino())).append("' ");
			and = " and ";
		}
		if (filtroRelatorioAcademicoVO.getFiltrarPorAnoSemestre()) {
			selectStr.append(and).append("  ").append(campo).append(".ano = '").append(filtroRelatorioAcademicoVO.getAno()).append("' and ").append(campo).append(".semestre = '").append(filtroRelatorioAcademicoVO.getSemestre()).append("' ");
			and = " and ";
		}
		if (filtroRelatorioAcademicoVO.getFiltrarPorAno()) {
			selectStr.append(and).append("  ").append(campo).append(".ano = '").append(filtroRelatorioAcademicoVO.getAno()).append("'  ");			
		}
		return selectStr.toString();
	}
	

//	public String getFiltroSituacaoParcelaMatricula(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, String campo) {
//		StringBuilder sqlStr = new StringBuilder();
//		campo = campo.trim();
//		if (filtroRelatorioAcademicoVO.getMatriculaAReceber() && !filtroRelatorioAcademicoVO.getMatriculaRecebida()) {
//			sqlStr.append("  ").append(campo).append(".situacao = 'PF'");
//		} else if (filtroRelatorioAcademicoVO.getMatriculaAReceber() && !filtroRelatorioAcademicoVO.getMatriculaRecebida()) {
//			sqlStr.append("  ").append(campo).append(".situacao != 'PF'");
//		}else{
//			sqlStr.append("  ").append(campo).append(".situacao != ''");
//		}
//		return sqlStr.toString();
//	}

//	public String getFiltroSituacaoAcademica(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, String campo) {
//		StringBuilder sqlStr = new StringBuilder();
//		campo = campo.trim();
//		sqlStr.append(" ").append(campo).append(".situacaomatriculaperiodo in (''");
//
//		if (filtroRelatorioAcademicoVO.getAtivo()) {
//			sqlStr.append(", 'AT'");
//		}
//		if (filtroRelatorioAcademicoVO.getPreMatricula()) {
//			sqlStr.append(", 'PR'");
//		}
//		if (filtroRelatorioAcademicoVO.getPreMatriculaCancelada()) {
//			sqlStr.append(", 'PC'");
//		}
//		if (filtroRelatorioAcademicoVO.getTrancado()) {
//			sqlStr.append(", 'TR'");
//		}
//		if (filtroRelatorioAcademicoVO.getConcluido()) {
//			sqlStr.append(", 'FI'");
//		}
//		if (filtroRelatorioAcademicoVO.getFormado()) {
//			sqlStr.append(", 'FO'");
//		}
//		if (filtroRelatorioAcademicoVO.getTransferenciaExterna()) {
//			sqlStr.append(", 'TS'");
//		}
//		if (filtroRelatorioAcademicoVO.getTransferenciaInterna()) {
//			sqlStr.append(", 'TI'");
//		}
//		if (filtroRelatorioAcademicoVO.getAbandonado()) {
//			sqlStr.append(", 'AC'");
//		}
//		if (filtroRelatorioAcademicoVO.getCancelado()) {
//			sqlStr.append(", 'CA'");
//		}
//		sqlStr.append(") ");
//		return sqlStr.toString();		
//	}
}
