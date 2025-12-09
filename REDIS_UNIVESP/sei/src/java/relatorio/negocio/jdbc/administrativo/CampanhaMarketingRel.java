package relatorio.negocio.jdbc.administrativo;

import java.io.File;
import java.util.Date;
import java.util.Vector;

import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.TipoMidiaCaptacaoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import relatorio.negocio.interfaces.administrativo.CampanhaMarketingRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class CampanhaMarketingRel extends SuperRelatorio implements CampanhaMarketingRelInterfaceFacade {

	protected Date dataInicioVinculacao;
	protected Date dataFimVinculacao;
	protected FuncionarioVO requisitante;
	protected Date dataAutorizacao;
	protected Date dataFimAutorizacao;
	protected String situacao;
	protected TipoMidiaCaptacaoVO midia;

	public CampanhaMarketingRel() {
		inicializarParametros();
		inicializarOrdenacoesRelatorio();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see relatorio.negocio.jdbc.administrativo.CampanhaMarketingRelInterfaceFacade#emitirRelatorio()
	 */
	public String emitirRelatorio(UsuarioVO usuarioVO) throws Exception {
		UnidadeEnsinoRel.emitirRelatorio(getIdEntidade(), true, usuarioVO);
		
		converterResultadoConsultaParaXML(executarConsultaParametrizada(), this.getIdEntidade(), "registros" );
		return getXmlRelatorio();
	}

	private SqlRowSet executarConsultaParametrizada() throws Exception {
		String selectStr = "SELECT CampanhaMarketing.descricao as CampanhaMarketing_descricao,  " + "CampanhaMarketing.dataautorizacao as CampanhaMarketing_dataautorizacao, "
				+ "CampanhaMarketing.responsavelautorizacao as CampanhaMarketing_responsavelautorizacao, " + "CampanhaMarketing.responsavelfinalizacao as  CampanhaMarketing_responsavelfinalizacao, "
				+ "CampanhaMarketing.custoefetivado as CampanhaMarketing_custoefetivado, " + "CampanhaMarketing.custoestimado as CampanhaMarketing_custoestimado, "
				+ "CampanhaMarketing.requisitante as  CampanhaMarketing_requisitante, " + "CampanhaMarketing.resultados as  CampanhaMarketing_resultados, "
				+ "CampanhaMarketing.nrpessoasimpactadas as CampanhaMarketing_nrpessoasimpactadas, " + "CampanhaMarketing.datainiciovinculacao as CampanhaMarketing_datainiciovinculacao, "
				+ "CampanhaMarketing.datafimvinculacao as  CampanhaMarketing_datafimvinculacao, " + "CampanhaMarketing.datafinalizacaocampanha as CampanhaMarketing_datafinalizacaocampanha, "
				+ "CampanhaMarketing.datarequisicao as  CampanhaMarketing_datarequisicao, " + "CampanhaMarketing.publicoalvo as  CampanhaMarketing_publicoalvo, "
				+ "CampanhaMarketing.objetivo as CampanhaMarketing_objetivo, " + "CampanhaMarketing.situacao as CampanhaMarketing_situacao, "
				+ "CampanhaMarketing.descricao as  CampanhaMarketing_descricao, " + "Pessoa.nome as  Pessoa_nome , " + "Usuario.nome as Usuario_autorizacao,  " + "p1.nome as Usuario_finalizacao,  "
				+ "Tipomidiacaptacao.nomemidia as Tipomidiacaptacao_nomemidia "
				+ "FROM Campanhamarketingmidia LEFT JOIN Campanhamarketing on Campanhamarketingmidia.Campanhamarketing = Campanhamarketing.codigo "
				+ "LEFT JOIN tipomidiacaptacao on Campanhamarketingmidia.midia = tipomidiacaptacao.codigo " + "LEFT JOIN usuario on CampanhaMarketing.responsavelautorizacao= Usuario.codigo 	"
				+ "LEFT JOIN usuario p1  on p1.codigo = CampanhaMarketing.responsavelfinalizacao, 	" + " Funcionario, Pessoa";

		selectStr = montarVinculoEntreTabelas(selectStr);
		selectStr = montarFiltrosRelatorio(selectStr);
		selectStr += ("group by  CampanhaMarketing_descricao, " + "CampanhaMarketing_dataautorizacao, " + "CampanhaMarketing_responsavelautorizacao,  " + "CampanhaMarketing_responsavelfinalizacao, "
				+ "CampanhaMarketing_custoefetivado, " + "CampanhaMarketing_custoestimado, " + "CampanhaMarketing_requisitante, " + "CampanhaMarketing_resultados, "
				+ "CampanhaMarketing_nrpessoasimpactadas,  " + "CampanhaMarketing_datainiciovinculacao, " + "CampanhaMarketing_datafimvinculacao, " + "CampanhaMarketing_datafinalizacaocampanha, "
				+ "CampanhaMarketing_datarequisicao, " + "CampanhaMarketing_publicoalvo, " + "CampanhaMarketing_objetivo, " + "CampanhaMarketing_situacao, " + "CampanhaMarketing_descricao, "
				+ "Pessoa_nome , " + " Usuario_autorizacao,  " + " Usuario_finalizacao,    " + "Tipomidiacaptacao_nomemidia");
		selectStr = montarOrdenacaoRelatorio(selectStr);
		return getConexao().getJdbcTemplate().queryForRowSet(selectStr);
	}

	private String montarFiltrosRelatorio(String selectStr) {
		String filtros = "";
		if ((dataInicioVinculacao != null)) {
			filtros = adicionarCondicionalWhere(filtros, "(CampanhaMarketing.datainiciovinculacao>= '" + Uteis.getData(dataInicioVinculacao) + " ')", true);
			adicionarDescricaoFiltro("dataInicioVinculacao >= " + Uteis.getData(dataInicioVinculacao));
		}
		if ((dataFimVinculacao != null)) {
			filtros = adicionarCondicionalWhere(filtros, "(CampanhaMarketing.datafimvinculacao<= '" + Uteis.getData(dataFimVinculacao) + " ')", true);
			adicionarDescricaoFiltro("dataFimVinculacao <= " + Uteis.getData(dataFimVinculacao));
		}
		if ((requisitante != null) && !requisitante.getMatricula().equals("")) {
			filtros = adicionarCondicionalWhere(filtros, "(CampanhaMarketing.requisitante= " + requisitante.getCodigo() + ")", true);
			adicionarDescricaoFiltro("requisitante = " + requisitante.getCodigo());
		}

		if ((dataAutorizacao != null)) {
			filtros = adicionarCondicionalWhere(filtros, "(CampanhaMarketing.dataautorizacao >=' " + Uteis.getData(dataAutorizacao) + " ')", true);
			adicionarDescricaoFiltro("DataAutorização  >= " + Uteis.getData(dataAutorizacao));
		}
		if ((dataFimAutorizacao != null)) {
			filtros = adicionarCondicionalWhere(filtros, "(CampanhaMarketing.datafinalizacaocampanha <= '" + Uteis.getData(dataFimAutorizacao) + " ')", true);
			adicionarDescricaoFiltro("DataFimAutorização  <= " + Uteis.getData(dataFimAutorizacao));
		}
		if (situacao != null && !situacao.equals("")) {
			filtros = adicionarCondicionalWhere(filtros, "(CampanhaMarketing.situacao= '" + situacao + "')", true);
			adicionarDescricaoFiltro("situacao = " + situacao);
		}
		if (midia.getCodigo().intValue() != 0) {
			filtros = adicionarCondicionalWhere(filtros, "(CampanhaMarketingMidia.midia =" + midia.getCodigo().intValue() + " )", true);
			adicionarDescricaoFiltro("tipoMidia= " + midia.getCodigo().intValue());
		}

		selectStr += filtros;
		return selectStr;
	}

	private String montarOrdenacaoRelatorio(String selectStr) {
		String ordenacao = (String) getOrdenacoesRelatorio().get(getOrdenarPor());
		if (ordenacao.equals("Data de Vinculação")) {
			ordenacao = "CampanhaMarketing.datainiciovinculacao";
		}
		if (ordenacao.equals("Requisitante")) {
			ordenacao = "pessoa.nome";
		}
		if (ordenacao.equals("Data de Autorização")) {
			ordenacao = "CampanhaMarketing.dataautorizacao";
		}
		if (ordenacao.equals("Situação")) {
			ordenacao = "CampanhaMarketing.situacao";
		}
		if (ordenacao.equals("Responsável pela Autorização")) {
			ordenacao = "usuario.nome";
		}
		if (ordenacao.equals("Tipo de Midia de Captação")) {
			ordenacao = "CampanhaMarketingMidia.midia";
		}
		if (!ordenacao.equals("")) {
			selectStr = selectStr + " ORDER BY " + ordenacao;
		}
		return selectStr;
	}

	private String montarVinculoEntreTabelas(String selectStr) {
		String vinculos = "";
		vinculos = adicionarCondicionalWhere(vinculos, "(Pessoa.codigo = Funcionario.pessoa)", false);
		vinculos = adicionarCondicionalWhere(vinculos, "(Funcionario.codigo = CampanhaMarketing.requisitante)", true);

		if (!vinculos.equals("")) {
			if (selectStr.indexOf("WHERE") == -1) {
				selectStr = selectStr + " WHERE " + vinculos;
			} else {
				selectStr = selectStr + " WHERE " + vinculos;
			}
		}
		return selectStr;
	}

	private void inicializarOrdenacoesRelatorio() {
		Vector ordenacao = this.getOrdenacoesRelatorio();
		ordenacao.add("Data de Vinculação");
		ordenacao.add("Requisitante");
		ordenacao.add("Data de Autorização");
		ordenacao.add("Situação");
		ordenacao.add("Responsável pela Autorização");
		ordenacao.add("Tipo de Midia de Captação");

	}

	private void inicializarParametros() {
		setMidia(new TipoMidiaCaptacaoVO());
		setRequisitante(new FuncionarioVO());
		setSituacao("");
		setDataInicioVinculacao(null);
		setDataFimVinculacao(null);
		setDataAutorizacao(null);
		setDataFimAutorizacao(null);
	}

	public static String getDesignIReportRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "administrativo" + File.separator + getIdEntidade() + ".jrxml");
	}

	public static String getIdEntidade() {
		return ("CampanhaMarketingRel");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see relatorio.negocio.jdbc.administrativo.CampanhaMarketingRelInterfaceFacade#getDataAutorizacao()
	 */
	public Date getDataAutorizacao() {
		return dataAutorizacao;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see relatorio.negocio.jdbc.administrativo.CampanhaMarketingRelInterfaceFacade#setDataAutorizacao(java.util.Date)
	 */
	public void setDataAutorizacao(Date dataAutorizacao) {
		this.dataAutorizacao = dataAutorizacao;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see relatorio.negocio.jdbc.administrativo.CampanhaMarketingRelInterfaceFacade#getDataFimVinculacao()
	 */
	public Date getDataFimVinculacao() {
		return dataFimVinculacao;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * relatorio.negocio.jdbc.administrativo.CampanhaMarketingRelInterfaceFacade#setDataFimVinculacao(java.util.Date)
	 */
	public void setDataFimVinculacao(Date dataFimVinculacao) {
		this.dataFimVinculacao = dataFimVinculacao;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see relatorio.negocio.jdbc.administrativo.CampanhaMarketingRelInterfaceFacade#getDataInicioVinculacao()
	 */
	public Date getDataInicioVinculacao() {
		return dataInicioVinculacao;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * relatorio.negocio.jdbc.administrativo.CampanhaMarketingRelInterfaceFacade#setDataInicioVinculacao(java.util.Date)
	 */
	public void setDataInicioVinculacao(Date dataInicioVinculacao) {
		this.dataInicioVinculacao = dataInicioVinculacao;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see relatorio.negocio.jdbc.administrativo.CampanhaMarketingRelInterfaceFacade#getMidia()
	 */
	public TipoMidiaCaptacaoVO getMidia() {
		return midia;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * relatorio.negocio.jdbc.administrativo.CampanhaMarketingRelInterfaceFacade#setMidia(negocio.comuns.administrativo
	 * .TipoMidiaCaptacaoVO)
	 */
	public void setMidia(TipoMidiaCaptacaoVO midia) {
		this.midia = midia;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see relatorio.negocio.jdbc.administrativo.CampanhaMarketingRelInterfaceFacade#getRequisitante()
	 */
	public FuncionarioVO getRequisitante() {
		return requisitante;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seerelatorio.negocio.jdbc.administrativo.CampanhaMarketingRelInterfaceFacade#setRequisitante(negocio.comuns.
	 * administrativo.FuncionarioVO)
	 */
	public void setRequisitante(FuncionarioVO requisitante) {
		this.requisitante = requisitante;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see relatorio.negocio.jdbc.administrativo.CampanhaMarketingRelInterfaceFacade#getSituacao()
	 */
	public String getSituacao() {
		return situacao;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see relatorio.negocio.jdbc.administrativo.CampanhaMarketingRelInterfaceFacade#setSituacao(java.lang.String)
	 */
	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see relatorio.negocio.jdbc.administrativo.CampanhaMarketingRelInterfaceFacade#getDataFimAutorizacao()
	 */
	public Date getDataFimAutorizacao() {
		return dataFimAutorizacao;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * relatorio.negocio.jdbc.administrativo.CampanhaMarketingRelInterfaceFacade#setDataFimAutorizacao(java.util.Date)
	 */
	public void setDataFimAutorizacao(Date dataFimAutorizacao) {
		this.dataFimAutorizacao = dataFimAutorizacao;
	}

}
