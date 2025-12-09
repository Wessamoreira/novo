package relatorio.negocio.jdbc.processosel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import relatorio.negocio.comuns.processosel.PerfilSocioEconomicoItemRelVO;
import relatorio.negocio.comuns.processosel.PerfilSocioEconomicoRelVO;
import relatorio.negocio.interfaces.processosel.PerfilSocioEconomicoRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class PerfilSocioEconomicoRel extends SuperRelatorio implements PerfilSocioEconomicoRelInterfaceFacade {

	protected String tipoRelatorio;
	protected static String idEntidade;
	protected Integer unidadeEnsino;
	protected PerfilSocioEconomicoRelVO perfilSocioEconomicoRelVO;

	public PerfilSocioEconomicoRel() {
		setIdEntidade("PerfilSocioEconomicoRel");
		inicializarParametros();
		inicializarOrdenacoesRelatorio();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see relatorio.negocio.jdbc.processosel.PerfilSocioEconomicoRelInterfaceFacade#emitirRelatorio()
	 */
	public List emitirRelatorio() throws Exception {
		PerfilSocioEconomicoRel.emitirRelatorio(getIdEntidade(), false, null);
		List listaResultado = new ArrayList(0);
		executarConsultaParametrizadaSexo();
		executarConsultaParametrizadaRendaMensalFamiliar();
		executarConsultaParametrizadaRendaMensalCandidato();
		executarConsultaParametrizadaComoPretendeManterCurso();
		executarConsultaParametrizadaGrauEscolaridadePai();
		executarConsultaParametrizadaGrauEscolaridadeMae();
		executarConsultaParametrizadaEscolaCursouEnsinoMedio();
		executarConsultaParametrizadaQuantidadeFaculdadePrestVestibular();
		executarConsultaParametrizadaConhecimentoProcSeletivo();
		listaResultado.add(getPerfilSocioEconomicoRelVO());
		return listaResultado;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see relatorio.negocio.jdbc.processosel.PerfilSocioEconomicoRelInterfaceFacade#inicializarOrdenacoesRelatorio()
	 */
	public void inicializarOrdenacoesRelatorio() {
		Vector ordenacao = this.getOrdenacoesRelatorio();
		ordenacao.add("Unidade Ensino");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see relatorio.negocio.jdbc.processosel.PerfilSocioEconomicoRelInterfaceFacade#inicializarParametros()
	 */
	public void inicializarParametros() {
		setUnidadeEnsino(0);
		setPerfilSocioEconomicoRelVO(new PerfilSocioEconomicoRelVO());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see relatorio.negocio.jdbc.processosel.PerfilSocioEconomicoRelInterfaceFacade#montarDadosConsulta()
	 */
	public List montarDadosConsulta(SqlRowSet rs) throws Exception {
		List listaConsulta = new ArrayList(0);
		while (rs.next()) {
			PerfilSocioEconomicoItemRelVO obj = new PerfilSocioEconomicoItemRelVO();
			obj.setTipo(rs.getString("tipo").toUpperCase());
			obj.setQuantidade(new Integer(rs.getInt("quantidade")));
			listaConsulta.add(obj);
		}
		return listaConsulta;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * relatorio.negocio.jdbc.processosel.PerfilSocioEconomicoRelInterfaceFacade#executarConsultaParametrizadaSexo()
	 */
	public void executarConsultaParametrizadaSexo() throws Exception {
		String selectStr = "Select pessoa.sexo as tipo , count(pessoa.sexo) as quantidade  " + " from PerfilSocioEconomico left join Pessoa on PerfilsocioEconomico.candidato = pessoa.codigo "
				+ " left join Inscricao on PerfilSocioEconomico.inscricao = inscricao.codigo, UnidadeEnsino " + " where inscricao.unidadeEnsino = unidadeEnsino.codigo ";
		if (unidadeEnsino.intValue() != 0) {
			selectStr += " and unidadeEnsino.codigo = " + unidadeEnsino.intValue();
		}
		selectStr += " group by tipo " + " order by quantidade ";
		
		getPerfilSocioEconomicoRelVO().setSexo(montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(selectStr)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seerelatorio.negocio.jdbc.processosel.PerfilSocioEconomicoRelInterfaceFacade#
	 * executarConsultaParametrizadaRendaMensalFamiliar()
	 */
	public void executarConsultaParametrizadaRendaMensalFamiliar() throws Exception {
		String selectStr = "Select PerfilSocioEconomico.somaRendaMensalFamilia as tipo , count(PerfilSocioEconomico.somaRendaMensalFamilia) as quantidade "
				+ " from PerfilSocioEconomico left join Pessoa on PerfilsocioEconomico.candidato = pessoa.codigo "
				+ " left join Inscricao on PerfilSocioEconomico.inscricao = inscricao.codigo, UnidadeEnsino " + " where inscricao.unidadeEnsino = unidadeEnsino.codigo ";
		if (unidadeEnsino.intValue() != 0) {
			selectStr += " and unidadeEnsino.codigo = " + unidadeEnsino.intValue();
		}
		selectStr += " group by tipo " + " order by quantidade ";
		
		getPerfilSocioEconomicoRelVO().setRendaMensalFamiliar(montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(selectStr)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seerelatorio.negocio.jdbc.processosel.PerfilSocioEconomicoRelInterfaceFacade#
	 * executarConsultaParametrizadaRendaMensalCandidato()
	 */
	public void executarConsultaParametrizadaRendaMensalCandidato() throws Exception {
		String selectStr = "Select PerfilSocioEconomico.rendaMensal as tipo , count(PerfilSocioEconomico.rendaMensal) as quantidade "
				+ " from PerfilSocioEconomico left join Pessoa on PerfilsocioEconomico.candidato = pessoa.codigo "
				+ " left join Inscricao on PerfilSocioEconomico.inscricao = inscricao.codigo, UnidadeEnsino " + " where inscricao.unidadeEnsino = unidadeEnsino.codigo ";
		if (unidadeEnsino.intValue() != 0) {
			selectStr += " and unidadeEnsino.codigo = " + unidadeEnsino.intValue();
		}
		selectStr += " group by tipo " + " order by quantidade ";
		
		getPerfilSocioEconomicoRelVO().setRendaMensalCandidato(montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(selectStr)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seerelatorio.negocio.jdbc.processosel.PerfilSocioEconomicoRelInterfaceFacade#
	 * executarConsultaParametrizadaComoPretendeManterCurso()
	 */
	public void executarConsultaParametrizadaComoPretendeManterCurso() throws Exception {
		String selectStr = "Select PerfilSocioEconomico.comoSeMaterDuranteCurso as tipo , count(PerfilSocioEconomico.comoSeMaterDuranteCurso) as quantidade "
				+ " from PerfilSocioEconomico left join Pessoa on PerfilsocioEconomico.candidato = pessoa.codigo "
				+ " left join Inscricao on PerfilSocioEconomico.inscricao = inscricao.codigo, UnidadeEnsino " + " where inscricao.unidadeEnsino = unidadeEnsino.codigo ";
		if (unidadeEnsino.intValue() != 0) {
			selectStr += " and unidadeEnsino.codigo = " + unidadeEnsino.intValue();
		}
		selectStr += " group by tipo " + " order by quantidade ";
		
		getPerfilSocioEconomicoRelVO().setComoPretendeManterCurso(montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(selectStr)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seerelatorio.negocio.jdbc.processosel.PerfilSocioEconomicoRelInterfaceFacade#
	 * executarConsultaParametrizadaGrauEscolaridadePai()
	 */
	public void executarConsultaParametrizadaGrauEscolaridadePai() throws Exception {
		String selectStr = "Select PerfilSocioEconomico.grauEscolaridadePai as tipo , count(PerfilSocioEconomico.grauEscolaridadePai) as quantidade "
				+ " from PerfilSocioEconomico left join Pessoa on PerfilsocioEconomico.candidato = pessoa.codigo "
				+ " left join Inscricao on PerfilSocioEconomico.inscricao = inscricao.codigo, UnidadeEnsino " + " where inscricao.unidadeEnsino = unidadeEnsino.codigo ";
		if (unidadeEnsino.intValue() != 0) {
			selectStr += " and unidadeEnsino.codigo = " + unidadeEnsino.intValue();
		}
		selectStr += " group by tipo " + " order by quantidade ";
		
		getPerfilSocioEconomicoRelVO().setGrauEscolaridadePai(montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(selectStr)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seerelatorio.negocio.jdbc.processosel.PerfilSocioEconomicoRelInterfaceFacade#
	 * executarConsultaParametrizadaGrauEscolaridadeMae()
	 */
	public void executarConsultaParametrizadaGrauEscolaridadeMae() throws Exception {
		String selectStr = "Select PerfilSocioEconomico.grauEscolaridadeMae as tipo , count(PerfilSocioEconomico.grauEscolaridadeMae) as quantidade "
				+ " from PerfilSocioEconomico left join Pessoa on PerfilsocioEconomico.candidato = pessoa.codigo "
				+ " left join Inscricao on PerfilSocioEconomico.inscricao = inscricao.codigo, UnidadeEnsino " + " where inscricao.unidadeEnsino = unidadeEnsino.codigo ";
		if (unidadeEnsino.intValue() != 0) {
			selectStr += " and unidadeEnsino.codigo = " + unidadeEnsino.intValue();
		}
		selectStr += " group by tipo " + " order by quantidade ";
		
		getPerfilSocioEconomicoRelVO().setGrauEscolaridadeMae(montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(selectStr)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seerelatorio.negocio.jdbc.processosel.PerfilSocioEconomicoRelInterfaceFacade#
	 * executarConsultaParametrizadaEscolaCursouEnsinoMedio()
	 */
	public void executarConsultaParametrizadaEscolaCursouEnsinoMedio() throws Exception {
		String selectStr = "Select PerfilSocioEconomico.tipoEscolaCursouEnsinoMedio as tipo , count(PerfilSocioEconomico.tipoEscolaCursouEnsinoMedio) as quantidade "
				+ " from PerfilSocioEconomico left join Pessoa on PerfilsocioEconomico.candidato = pessoa.codigo "
				+ " left join Inscricao on PerfilSocioEconomico.inscricao = inscricao.codigo, UnidadeEnsino " + " where inscricao.unidadeEnsino = unidadeEnsino.codigo ";
		if (unidadeEnsino.intValue() != 0) {
			selectStr += " and unidadeEnsino.codigo = " + unidadeEnsino.intValue();
		}
		selectStr += " group by tipo " + " order by quantidade ";
		
		getPerfilSocioEconomicoRelVO().setEscolaCursouEnsinoMedio(montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(selectStr)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seerelatorio.negocio.jdbc.processosel.PerfilSocioEconomicoRelInterfaceFacade#
	 * executarConsultaParametrizadaQuantidadeFaculdadePrestVestibular()
	 */
	public void executarConsultaParametrizadaQuantidadeFaculdadePrestVestibular() throws Exception {
		String selectStr = "Select PerfilSocioEconomico.qtdFaculdadePrestouVestib as tipo , count(PerfilSocioEconomico.qtdFaculdadePrestouVestib) as quantidade "
				+ " from PerfilSocioEconomico left join Pessoa on PerfilsocioEconomico.candidato = pessoa.codigo "
				+ " left join Inscricao on PerfilSocioEconomico.inscricao = inscricao.codigo, UnidadeEnsino " + " where inscricao.unidadeEnsino = unidadeEnsino.codigo ";
		if (unidadeEnsino.intValue() != 0) {
			selectStr += " and unidadeEnsino.codigo = " + unidadeEnsino.intValue();
		}
		selectStr += " group by tipo " + " order by quantidade ";
		
		getPerfilSocioEconomicoRelVO().setQuantidadeFaculdadePrestVestibular(montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(selectStr)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seerelatorio.negocio.jdbc.processosel.PerfilSocioEconomicoRelInterfaceFacade#
	 * executarConsultaParametrizadaConhecimentoProcSeletivo()
	 */
	public void executarConsultaParametrizadaConhecimentoProcSeletivo() throws Exception {
		String selectStr = "Select PerfilSocioEconomico.conhecimentoProcessoSeletivo as tipo , count(PerfilSocioEconomico.conhecimentoProcessoSeletivo) as quantidade "
				+ " from PerfilSocioEconomico left join Pessoa on PerfilsocioEconomico.candidato = pessoa.codigo "
				+ " left join Inscricao on PerfilSocioEconomico.inscricao = inscricao.codigo, UnidadeEnsino " + " where inscricao.unidadeEnsino = unidadeEnsino.codigo ";
		if (unidadeEnsino.intValue() != 0) {
			selectStr += " and unidadeEnsino.codigo = " + unidadeEnsino.intValue();
		}
		selectStr += " group by tipo " + " order by quantidade ";
		
		getPerfilSocioEconomicoRelVO().setConhecimentoProcSeletivo(montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(selectStr)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see relatorio.negocio.jdbc.processosel.PerfilSocioEconomicoRelInterfaceFacade#getDesignIReportRelatorio()
	 */
	public String getDesignIReportRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "processosel" + File.separator + "PerfilSocioEconomicoRel.jrxml");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see relatorio.negocio.jdbc.processosel.PerfilSocioEconomicoRelInterfaceFacade#getCaminhoBaseRelatorio()
	 */
	public String getCaminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "processosel" + File.separator);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see relatorio.negocio.jdbc.processosel.PerfilSocioEconomicoRelInterfaceFacade#getUnidadeEnsino()
	 */
	public Integer getUnidadeEnsino() {
		return unidadeEnsino;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * relatorio.negocio.jdbc.processosel.PerfilSocioEconomicoRelInterfaceFacade#setUnidadeEnsino(java.lang.Integer)
	 */
	public void setUnidadeEnsino(Integer unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}

	public static String getIdEntidade() {
		return PerfilSocioEconomicoRel.idEntidade;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see relatorio.negocio.jdbc.processosel.PerfilSocioEconomicoRelInterfaceFacade#setIdEntidade(java.lang.String)
	 */
	public void setIdEntidade(String idEntidade) {
		PerfilSocioEconomicoRel.idEntidade = idEntidade;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see relatorio.negocio.jdbc.processosel.PerfilSocioEconomicoRelInterfaceFacade#getPerfilSocioEconomicoRelVO()
	 */
	public PerfilSocioEconomicoRelVO getPerfilSocioEconomicoRelVO() {
		return perfilSocioEconomicoRelVO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * relatorio.negocio.jdbc.processosel.PerfilSocioEconomicoRelInterfaceFacade#setPerfilSocioEconomicoRelVO(relatorio
	 * .negocio.comuns.processosel.PerfilSocioEconomicoRelVO)
	 */
	public void setPerfilSocioEconomicoRelVO(PerfilSocioEconomicoRelVO perfilSocioEconomicoRelVO) {
		this.perfilSocioEconomicoRelVO = perfilSocioEconomicoRelVO;
	}

}
