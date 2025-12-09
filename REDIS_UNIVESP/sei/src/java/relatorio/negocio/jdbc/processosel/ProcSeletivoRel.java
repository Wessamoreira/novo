package relatorio.negocio.jdbc.processosel;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import negocio.comuns.utilitarias.Uteis;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import relatorio.negocio.comuns.processosel.ProcSeletivoRelVO;
import relatorio.negocio.interfaces.processosel.ProcSeletivoRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class ProcSeletivoRel extends SuperRelatorio implements ProcSeletivoRelInterfaceFacade {

	protected Integer descricao;
	protected Date dataInicio;
	protected Date dataFim;
	protected Integer unidadeEnsino;
	protected Integer curso;

	public ProcSeletivoRel() {
		inicializarParametros();
		inicializarOrdenacoesRelatorio();
	}

	public List<ProcSeletivoRelVO> criarObjeto() throws Exception {
		List<ProcSeletivoRelVO> procSeletivoRelVOs = new ArrayList<ProcSeletivoRelVO>(0);
		SqlRowSet dadosSQL = executarConsultaParametrizada();
		while (dadosSQL.next()) {
			procSeletivoRelVOs.add(montarDados(dadosSQL));
		}
		if (procSeletivoRelVOs.isEmpty()) {
			throw new Exception("Não há dados a serem apresentados nesse relatório.");
		}
		return procSeletivoRelVOs;
	}

	private ProcSeletivoRelVO montarDados(SqlRowSet dadosSQL) {
		ProcSeletivoRelVO procSeletivoRelVO = new ProcSeletivoRelVO();
		procSeletivoRelVO.setCPF(dadosSQL.getString("cpf"));
		procSeletivoRelVO.setCursoOpcao1(dadosSQL.getString("cursoopcao1"));
		procSeletivoRelVO.setDataFim(Uteis.getDataJDBC(dadosSQL.getDate("datafim")));
		procSeletivoRelVO.setDataFimInternet(Uteis.getDataJDBC(dadosSQL.getDate("datafiminternet")));
		procSeletivoRelVO.setDataInicio(Uteis.getDataJDBC(dadosSQL.getDate("datainicio")));
		procSeletivoRelVO.setDataInicioInternet(Uteis.getDataJDBC(dadosSQL.getDate("datainiciointernet")));
		procSeletivoRelVO.setDataInscricao(Uteis.getDataJDBC(dadosSQL.getDate("datainscricao")));
		procSeletivoRelVO.setDataProva(Uteis.getDataJDBC(dadosSQL.getDate("dataprova")));
		procSeletivoRelVO.setDescricao(dadosSQL.getString("descricao"));
		procSeletivoRelVO.setDescricaoNecessidadeEspecial(dadosSQL.getString("descricaonecessidadeespecial"));
		procSeletivoRelVO.setFormaAcessoProcSeletivo(dadosSQL.getString("formaacessoprocseletivo"));
		procSeletivoRelVO.setHorarioProva(dadosSQL.getString("horarioprova"));
		procSeletivoRelVO.setMediaMinimaAprovacao(dadosSQL.getDouble("mediaminimaaprovacao"));
		procSeletivoRelVO.setNivelEducacional(dadosSQL.getString("niveleducacional"));
		procSeletivoRelVO.setNomeCurso1(dadosSQL.getString("nomecurso1"));
		procSeletivoRelVO.setNomeCurso2(dadosSQL.getString("nomecurso2"));
		procSeletivoRelVO.setNomeCurso3(dadosSQL.getString("nomecurso3"));
		procSeletivoRelVO.setNomePessoa(dadosSQL.getString("nomepessoa"));
		procSeletivoRelVO.setNomeUnidadeEnsino(dadosSQL.getString("nomeunidadeensino"));
		procSeletivoRelVO.setNrInscricao(dadosSQL.getInt("nrinscricao"));
		procSeletivoRelVO.setNrOpcoesCurso(dadosSQL.getInt("nropcoescurso"));
		procSeletivoRelVO.setOpcaoLinguaEstrangeira(dadosSQL.getString("opcaolinguaestrangeira"));
		procSeletivoRelVO.setSituacao(dadosSQL.getString("situacao"));
		procSeletivoRelVO.setUnidadeEnsino(dadosSQL.getInt("unidadeensino"));
		procSeletivoRelVO.setValorInscricao(dadosSQL.getDouble("valorinscricao"));
		return procSeletivoRelVO;
	}

	public void inicializarOrdenacoesRelatorio() {
		Vector ordenacao = this.getOrdenacoesRelatorio();
		ordenacao.add("Processo Seletivo");
		ordenacao.add("Período");
		ordenacao.add("Unidade Ensino");
		ordenacao.add("Curso");
	}

	public void inicializarParametros() {
		setDescricao(0);
		setDataInicio(null);
		setDataFim(null);
		setUnidadeEnsino(0);
		setCurso(0);
	}

	public SqlRowSet executarConsultaParametrizada() throws Exception {
		String selectStr = "SELECT procSeletivo.descricao AS descricao, procSeletivo.datainicio AS datainicio, procSeletivo.datafim AS datafim, " + "procSeletivo.datainiciointernet AS datainiciointernet, procSeletivo.datafiminternet AS datafiminternet, " + "procSeletivo.valorinscricao AS valorinscricao, procSeletivo.dataprova AS dataprova, procSeletivo.horarioprova AS horarioprova, " + "procSeletivo.nropcoescurso AS nropcoescurso, procSeletivo.niveleducacional AS niveleducacional, " + "procSeletivo.mediaminimaaprovacao AS mediaminimaaprovacao, inscricao.codigo AS nrinscricao, inscricao.data AS dataInscricao, " + "inscricao.situacao AS situacao, inscricao.cursoopcao1 AS cursoopcao1, inscricao.unidadeensino AS unidadeensino , " + "inscricao.opcaolinguaestrangeira AS opcaolinguaestrangeira, inscricao.formaacessoprocseletivo AS formaacessoprocseletivo, " + "inscricao.descricaonecessidadeespecial AS descricaonecessidadeespecial, pessoa.nome AS nomepessoa, pessoa.cpf AS cpf, " + "unidadeensino.nome AS nomeunidadeensino, c1.nome nomecurso1, c2.nome AS nomecurso2, c3.nome AS nomecurso3 " + "FROM inscricao LEFT JOIN procSeletivo ON inscricao.procseletivo = procseletivo.codigo LEFT JOIN pessoa ON inscricao.candidato = pessoa.codigo " + "LEFT JOIN unidadeensino ON inscricao.unidadeensino = unidadeensino.codigo LEFT JOIN unidadeensinocurso op1 ON inscricao.cursoopcao1 = op1.codigo " + "LEFT JOIN curso c1 ON op1.curso = c1.codigo LEFT JOIN unidadeensinocurso op2 ON inscricao.cursoopcao2 = op2.codigo LEFT JOIN curso c2 ON op2.curso = c2.codigo " + "LEFT JOIN unidadeensinocurso op3 ON inscricao.cursoopcao3 = op3.codigo LEFT JOIN curso c3 ON op3.curso = c3.codigo ";
		selectStr = montarVinculoEntreTabelas(selectStr);
		selectStr = montarFiltrosRelatorio(selectStr);
		selectStr += " GROUP BY procSeletivo.descricao, datainicio, datafim, datainiciointernet, datafiminternet, valorinscricao, procSeletivo.dataprova, horarioprova, nropcoescurso, " + "procSeletivo.niveleducacional, mediaminimaaprovacao, nrinscricao, dataInscricao, situacao, cursoopcao1, inscricao.unidadeensino, opcaolinguaestrangeira, " + "formaacessoprocseletivo, descricaonecessidadeespecial, nomepessoa, pessoa.cpf, nomeunidadeensino, nomecurso1, nomecurso2, nomecurso3";
		selectStr = montarOrdenacaoRelatorio(selectStr);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(selectStr);
		return tabelaResultado;
	}

	private String montarFiltrosRelatorio(String selectStr) {
		String filtros = "";

		if ((descricao != null) && (descricao.intValue() != 0)) {
			filtros = adicionarCondicionalWhere(filtros, "(procSeletivo.codigo =" + descricao.intValue() + " )", true);
			adicionarDescricaoFiltro("procSeletivodescricao = " + descricao);
		}

		if (dataInicio != null) {
			filtros = adicionarCondicionalWhere(filtros, "(procSeletivo.datainicio>= '" + Uteis.getData(dataInicio) + " ')", true);
			adicionarDescricaoFiltro("procSeletivodatainicio >= " + Uteis.getData(dataInicio));
		}
		if (dataFim != null) {
			filtros = adicionarCondicionalWhere(filtros, "(procSeletivo.datafim <= '" + Uteis.getData(dataFim) + " ')", true);
			adicionarDescricaoFiltro("procSeletivodatafim <= " + Uteis.getData(dataFim));
		}

		if ((unidadeEnsino != null) && (unidadeEnsino.intValue() != 0)) {
			filtros = adicionarCondicionalWhere(filtros, "(inscricao.unidadeensino =" + unidadeEnsino.intValue() + " )", true);
			adicionarDescricaoFiltro("inscricaounidadeensino = " + unidadeEnsino.intValue());
		}
		if ((curso != null) && (curso.intValue() != 0)) {
			filtros = adicionarCondicionalWhere(filtros, "( inscricao.cursoopcao1 = " + curso.intValue() + ")", true);
			adicionarDescricaoFiltro("inscricaocursoopcao1 = " + curso);
		}

		selectStr += filtros;
		return selectStr;
	}

	private String montarVinculoEntreTabelas(String selectStr) {
		String vinculos = "";
		vinculos = adicionarCondicionalWhere(vinculos, "(inscricao.procSeletivo = procSeletivo.codigo )", false);
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
		if (ordenacao.equals("Processo Seletivo")) {
			ordenacao = "procSeletivo.descricao";
		}
		if (ordenacao.equals("Periódo")) {
			ordenacao = "procSeletivo.datainicio";
		}
		if (ordenacao.equals("Unidade Ensino")) {
			ordenacao = "inscricao.unidadeensino";
		}
		if (ordenacao.equals("Curso")) {
			ordenacao = "inscricao.cursoopcao1";
		}
		if (!ordenacao.equals("")) {
			selectStr = selectStr + " ORDER BY " + ordenacao;
		}
		return selectStr;
	}

	public String designIReportRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "processosel" + File.separator + getIdEntidade() + ".jrxml");
	}

	@Override
    public String caminhoBaseRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "processosel" + File.separator);
    }
	
	public static String getCaminhoBaseIReportRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "processosel");
	}

	public Integer getCurso() {
		return curso;
	}

	public void setCurso(Integer curso) {
		this.curso = curso;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Integer getDescricao() {
		return descricao;
	}

	public void setDescricao(Integer descricao) {
		this.descricao = descricao;
	}

	public Integer getUnidadeEnsino() {
		return unidadeEnsino;
	}

	public void setUnidadeEnsino(Integer unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}

	public static String getIdEntidade() {
		return ("ProcSeletivoRel");
	}
	
}