package relatorio.negocio.jdbc.administrativo;

import java.io.File;
import java.util.Vector;
import negocio.comuns.arquitetura.UsuarioVO;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import relatorio.negocio.interfaces.administrativo.UnidadeEnsinoRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class UnidadeEnsinoRel extends SuperRelatorio implements UnidadeEnsinoRelInterfaceFacade {

	protected Integer unidadeEnsino;
	protected String unidadeEnsinonome;
	protected Integer curso;
	protected String cursonome;

	public UnidadeEnsinoRel() throws Exception {
		inicializarParametros();
		inicializarOrdenacoesRelatorio();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see relatorio.negocio.jdbc.administrativo.UnidadeEnsinoRelInterfaceFacade#emitirRelatorio()
	 */
	public String emitirRelatorio(UsuarioVO usuarioVO) throws Exception {
		UnidadeEnsinoRel.emitirRelatorio(getIdEntidade(), true, usuarioVO);		
		converterResultadoConsultaParaXML(executarConsultaParametrizada(), this.getIdEntidade(), "registros");
		return getXmlRelatorio();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see relatorio.negocio.jdbc.administrativo.UnidadeEnsinoRelInterfaceFacade#inicializarParametros()
	 */
	public void inicializarParametros() {
		setUnidadeEnsino(0);
		setUnidadeEnsinonome("");
		setCurso(0);
		setCursonome("");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see relatorio.negocio.jdbc.administrativo.UnidadeEnsinoRelInterfaceFacade#inicializarOrdenacoesRelatorio()
	 */
	public void inicializarOrdenacoesRelatorio() {
		Vector ordenacao = this.getOrdenacoesRelatorio();
		ordenacao.add("Unidade de Ensino");
		ordenacao.add("Curso");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see relatorio.negocio.jdbc.administrativo.UnidadeEnsinoRelInterfaceFacade#executarConsultaParametrizada()
	 */
	public SqlRowSet executarConsultaParametrizada() throws Exception {
		String selectStr = "select unidadeensino.codigo as unidadeensino_codigo," + "unidadeensino.nome as unidadeensino_nome, unidadeensino.cidade as unidadeensino_cidade, "
				+ "unidadeensino.inscestadual as unidadeensino_inscestadual,unidadeensino.cnpj as unidadeensino_cnpj,"
				+ " unidadeensino.razaosocial as unidadeensino_razaosocial, unidadeensino.endereco as unidadeensino_endereco,"
				+ "unidadeensino.setor as unidadeensino_setor, unidadeensino.complemento as unidadeensino_complemento, "
				+ "unidadeensino.cep as unidadeensino_cep, unidadeensino.numero as unidadeensino_numero, "
				+ "unidadeensino.telcomercial1 as unidadeensino_telcomercial1, unidadeensino.telcomercial2 as unidadeensino_telcomercial2,unidadeensino.telcomercial3 as unidadeensino_telcomercial3,"
				+ "unidadeensino.fax as unidadeensino_fax ,cidade.nome as cidade_nome, unidadeensinocurso.curso as unidadeensinocurso_curso, "
				+ "unidadeensinocurso.turno as unidadeensinocurso_turno, " + "unidadeensinocurso.nrvagasperiodoletivo as unidadeensinocurso_nrvagasperiodoletivo,"
				+ "curso.nome as curso_nome, turno.nome as turno_nome " + "FROM unidadeensino LEFT JOIN unidadeensinocurso ON unidadeensino.codigo = unidadeensinocurso.unidadeensino "
				+ "LEFT JOIN curso ON unidadeensinocurso.curso = curso.codigo " + "LEFT JOIN turno ON unidadeensinocurso.turno = turno.codigo, " + "cidade ";
		selectStr = montarVinculoEntreTabelas(selectStr);
		selectStr = montarFiltrosRelatorio(selectStr);
		selectStr += "group by unidadeensino.codigo, unidadeensino.nome, unidadeensino.cidade, unidadeensino.inscestadual,"
				+ "unidadeensino.cnpj, unidadeensino.razaosocial, unidadeensino.endereco,unidadeensino.setor, unidadeensino.complemento, "
				+ "unidadeensino.cep, unidadeensino.numero, unidadeensino.telcomercial1, unidadeensino.telcomercial2,unidadeensino.telcomercial3, "
				+ "unidadeensino.fax, cidade.nome, unidadeensinocurso.curso,unidadeensinocurso.turno," + "unidadeensinocurso.nrvagasperiodoletivo,curso.nome,turno.nome";
		selectStr = montarOrdenacaoRelatorio(selectStr);
		return getConexao().getJdbcTemplate().queryForRowSet(selectStr);
	}

	private String montarFiltrosRelatorio(String selectStr) {
		String filtros = "";
		if ((unidadeEnsino != null) && (unidadeEnsino.intValue() != 0)) {
			filtros = adicionarCondicionalWhere(filtros, "(unidadeensino.codigo =" + unidadeEnsino.intValue() + " )", true);
			adicionarDescricaoFiltro("UnidadeEnsino = " + unidadeEnsino.intValue());
		}
		if ((curso != null) && (curso.intValue() != 0)) {
			filtros = adicionarCondicionalWhere(filtros, "( curso.codigo = " + curso.intValue() + ")", true);
			adicionarDescricaoFiltro("Curso = " + curso);
		}

		selectStr += filtros;
		return selectStr;
	}

	/**
	 * Método responsável por definir os filtros dos dados a serem apresentados no relatório.
	 * 
	 * @param selectStr
	 *            consulta inicialmente preparada, para a qual os filtros serão adicionados.
	 */
	private String montarVinculoEntreTabelas(String selectStr) {
		String vinculos = "";
		vinculos = adicionarCondicionalWhere(vinculos, "( cidade.codigo = unidadeensino.cidade )", false);

		if (!vinculos.equals("")) {
			if (selectStr.indexOf("WHERE") == -1) {
				selectStr = selectStr + " WHERE " + vinculos;
			} else {
				selectStr = selectStr + " WHERE " + vinculos;
			}
		}
		return selectStr;
	}

	protected String montarOrdenacaoRelatorio(String selectStr) {
		String ordenacao = (String) getOrdenacoesRelatorio().get(getOrdenarPor());
		if (ordenacao.equals("Unidade de Ensino")) {
			ordenacao = "unidadeensino.nome";
		}
		if (ordenacao.equals("Curso")) {
			ordenacao = "curso.nome";
		}
		if (!ordenacao.equals("")) {
			selectStr = selectStr + " ORDER BY " + ordenacao;
		}
		return selectStr;
	}

	/**
	 * Operação reponsável por retornar o arquivo (caminho e nome) correspondente ao design do relatório criado pelo
	 * IReport.
	 */
	public static String getDesignIReportRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "administrativo" + File.separator + getIdEntidade() + ".jrxml");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see relatorio.negocio.jdbc.administrativo.UnidadeEnsinoRelInterfaceFacade#getCurso()
	 */
	public Integer getCurso() {
		return curso;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see relatorio.negocio.jdbc.administrativo.UnidadeEnsinoRelInterfaceFacade#setCurso(java.lang.Integer)
	 */
	public void setCurso(Integer curso) {
		this.curso = curso;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see relatorio.negocio.jdbc.administrativo.UnidadeEnsinoRelInterfaceFacade#getUnidadeEnsino()
	 */
	public Integer getUnidadeEnsino() {
		return unidadeEnsino;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see relatorio.negocio.jdbc.administrativo.UnidadeEnsinoRelInterfaceFacade#setUnidadeEnsino(java.lang.Integer)
	 */
	public void setUnidadeEnsino(Integer unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see relatorio.negocio.jdbc.administrativo.UnidadeEnsinoRelInterfaceFacade#getUnidadeEnsinonome()
	 */
	public String getUnidadeEnsinonome() {
		return unidadeEnsinonome;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see relatorio.negocio.jdbc.administrativo.UnidadeEnsinoRelInterfaceFacade#setUnidadeEnsinonome(java.lang.String)
	 */
	public void setUnidadeEnsinonome(String unidadeEnsinonome) {
		this.unidadeEnsinonome = unidadeEnsinonome;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see relatorio.negocio.jdbc.administrativo.UnidadeEnsinoRelInterfaceFacade#getCursonome()
	 */
	public String getCursonome() {
		return cursonome;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see relatorio.negocio.jdbc.administrativo.UnidadeEnsinoRelInterfaceFacade#setCursonome(java.lang.String)
	 */
	public void setCursonome(String cursonome) {
		this.cursonome = cursonome;
	}

	public static String getIdEntidade() {
		return ("UnidadeEnsinoRel");
	}
}
