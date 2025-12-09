package negocio.facade.jdbc.administrativo;

import java.io.File;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.MesAnoEnum;
import negocio.comuns.academico.enumeradores.SituacaoMatriculaPeriodoEnum;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.PainelGestorMonitoramentoCRMVO;
import negocio.comuns.administrativo.PainelGestorMonitoramentoDetalheCRMVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.PainelGestorTipoMonitoramentoCRMEnum;
import negocio.comuns.crm.ConsultorPorMatriculaRelVO;
import negocio.comuns.crm.ProspectsVO;
import negocio.comuns.crm.enumerador.TipoFiltroMonitamentoCrmProspectEnum;
import negocio.comuns.sad.LegendaGraficoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoContaReceber;
import negocio.comuns.utilitarias.dominios.TipoOrigemContaReceber;
import negocio.facade.jdbc.arquitetura.SuperFacadeJDBC;
import negocio.interfaces.administrativo.PainelGestorMonitoramentoCRMInterfaceFacade;
import relatorio.negocio.comuns.arquitetura.CrosstabVO;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;

@Service
@Lazy
public class PainelGestorMonitoramentoCRM extends SuperFacadeJDBC implements PainelGestorMonitoramentoCRMInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3095669314247501111L;

	@Override
	public void consultarDadosIniciaisPainelGestorMonitoramentoCRM(List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dateInicio, Date dataTermino, PainelGestorMonitoramentoCRMVO painelGestorMonitoramentoCRMVO, String situacaoProspect) throws Exception {
		painelGestorMonitoramentoCRMVO.setUnidadeEnsino(null);
		painelGestorMonitoramentoCRMVO.setConsultor(null);
		montarDadosGeraisPreInscricao(painelGestorMonitoramentoCRMVO, consultarDadosPreInscicao(unidadeEnsinoVOs, dateInicio, dataTermino, false, false, 0, 0), false, false, PainelGestorTipoMonitoramentoCRMEnum.PRE_INSCRICAO);
		consultarDadosIniciaisPainelGestorMonitoramentoProspectCRM(unidadeEnsinoVOs, dateInicio, dataTermino, painelGestorMonitoramentoCRMVO);
		realizarGeracaoGraficoMonitoramentoProspectComoFicouSabendoInstituicao(unidadeEnsinoVOs, dateInicio, dataTermino, painelGestorMonitoramentoCRMVO, situacaoProspect);
	}

	@Override
	public void consultarDadosIniciaisPainelGestorMonitoramentoProspectCRM(List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dateInicio, Date dataTermino, PainelGestorMonitoramentoCRMVO painelGestorMonitoramentoCRMVO) throws Exception {
		painelGestorMonitoramentoCRMVO.setUnidadeEnsino(null);
		painelGestorMonitoramentoCRMVO.setConsultor(null);
		painelGestorMonitoramentoCRMVO.setPainelGestorTipoMonitoramentoCRMEnum(null);
		consultarTotaisMonitoramentoProspect(unidadeEnsinoVOs, dateInicio, dataTermino, 0, 0, painelGestorMonitoramentoCRMVO);
		consultarTotalMonitoramentoProspectMensal(unidadeEnsinoVOs, dateInicio, dataTermino, 0, 0, painelGestorMonitoramentoCRMVO);
		realizarGeracaoGraficoMonitoramentoProspectDasUnidadeEnsino(unidadeEnsinoVOs, dateInicio, dataTermino, painelGestorMonitoramentoCRMVO);
	}

	@Override
	public void consultarMonitoramentoProspectPorUnidade(Date dateInicio, Date dataTermino, Integer unidadeEspecifica, PainelGestorMonitoramentoCRMVO painelGestorMonitoramentoCRMVO) throws Exception {
		if (unidadeEspecifica == null || unidadeEspecifica == 0) {
			painelGestorMonitoramentoCRMVO.getUnidadeEnsino().setCodigo(0);
			painelGestorMonitoramentoCRMVO.getUnidadeEnsino().setNome("Sem Unidade Ensino");
		} else {
			painelGestorMonitoramentoCRMVO.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCodigo(unidadeEspecifica, false, null));
		}
		consultarTotaisMonitoramentoProspect(null, dateInicio, dataTermino, unidadeEspecifica, 0, painelGestorMonitoramentoCRMVO);
		consultarTotalMonitoramentoProspectMensal(null, dateInicio, dataTermino, unidadeEspecifica, 0, painelGestorMonitoramentoCRMVO);
		realizarGeracaoGraficoMonitoramentoProspectPorUnidadeEnsino(unidadeEspecifica, dateInicio, dataTermino, painelGestorMonitoramentoCRMVO);
	}

	@Override
	public void consultarMonitoramentoProspectPorConsultor(Date dateInicio, Date dataTermino, Integer unidadeEspecifica, Integer consultor, PainelGestorMonitoramentoCRMVO painelGestorMonitoramentoCRMVO) throws Exception {
		if (consultor == null || consultor == 0) {
			painelGestorMonitoramentoCRMVO.getConsultor().setCodigo(0);
			painelGestorMonitoramentoCRMVO.getConsultor().getPessoa().setNome("Sem Consultor");
		} else {
			painelGestorMonitoramentoCRMVO.setConsultor(getFacadeFactory().getFuncionarioFacade().consultaRapidaPorChavePrimaria(consultor, false, null));
		}
		consultarTotaisMonitoramentoProspect(null, dateInicio, dataTermino, unidadeEspecifica, consultor, painelGestorMonitoramentoCRMVO);
		consultarTotalMonitoramentoProspectMensal(null, dateInicio, dataTermino, unidadeEspecifica, consultor, painelGestorMonitoramentoCRMVO);
		realizarGeracaoGraficoMonitoramentoProspectPorConsultor(unidadeEspecifica, consultor, dateInicio, dataTermino, painelGestorMonitoramentoCRMVO);
	}

	public void realizarGeracaoGraficoMonitoramentoProspectDasUnidadeEnsino(List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dateInicio, Date dataTermino, PainelGestorMonitoramentoCRMVO painelGestorMonitoramentoCRMVO) throws Exception {
		painelGestorMonitoramentoCRMVO.setDadosGraficoMonitoramentoProspect("");
		StringBuilder sql = new StringBuilder("");
		sql.append(" select count(distinct prospects.codigo) as quantidade, ");
		sql.append(" unidadeEnsino.codigo,  ");
		sql.append(" case when length(unidadeEnsino.abreviatura) > 0 then unidadeEnsino.abreviatura else unidadeEnsino.nome end as nome  ");
		sql.append(" from prospects   ");
		// sql.append(" inner join compromissoagendapessoahorario on prospects.codigo = compromissoagendapessoahorario.prospect ");
		// sql.append(" and compromissoagendapessoahorario.codigo = (select max(codigo) from compromissoagendapessoahorario caph where prospects.codigo = caph.prospect ");
		// sql.append(" and caph.datacompromisso  >= '").append(Uteis.getDataJDBC(dateInicio)).append("' and caph.datacompromisso  <= '").append(Uteis.getDataJDBC(dataTermino)).append("') ");
		sql.append(" left join unidadeEnsino on unidadeEnsino.codigo = prospects.unidadeEnsino ");
		sql.append(" where 1=1 and inativo = false ");
		if (unidadeEnsinoVOs != null && !unidadeEnsinoVOs.isEmpty()) {
			sql.append(" and (prospects.unidadeensino in (0 ");
			for (UnidadeEnsinoVO unidadeEnsinoVO : unidadeEnsinoVOs) {
				if (unidadeEnsinoVO.getFiltrarUnidadeEnsino()) {
					sql.append(", ").append(unidadeEnsinoVO.getCodigo());
				}
			}
			sql.append(" )   or  prospects.unidadeensino is  null)");
		}

		sql.append(" group by unidadeEnsino.codigo,  ");
		sql.append(" unidadeEnsino.nome, unidadeEnsino.abreviatura ");
		sql.append(" order by nome ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<LegendaGraficoVO> legendaGraficoVOs = new ArrayList<LegendaGraficoVO>();
		LegendaGraficoVO legendaGraficoVO = null;
		Integer qtdeTotal = 0;
		while (rs.next()) {
			legendaGraficoVO = new LegendaGraficoVO(rs.getString("nome") == null || rs.getString("nome").trim().isEmpty() ? 0 : rs.getInt("codigo"), 
					rs.getString("nome") == null || rs.getString("nome").trim().isEmpty() ? "Sem Unidade de Ensino" : rs.getString("nome"), PainelGestorTipoMonitoramentoCRMEnum.PROSPECT_POR_UNIDADE.name(), 
							Double.valueOf(rs.getInt("quantidade")), "", false);			 
			qtdeTotal += rs.getInt("quantidade");			
			legendaGraficoVOs.add(legendaGraficoVO);
		}
		
		for(LegendaGraficoVO legendaGrafico:legendaGraficoVOs){
			adicionarDadosRelatorioGraficoMonitiramentoProspect(((legendaGrafico.getValor()*100)/qtdeTotal.doubleValue()), legendaGrafico.getValor().intValue(), 
					legendaGrafico.getLegenda(), PainelGestorTipoMonitoramentoCRMEnum.PROSPECT_POR_UNIDADE, legendaGrafico.getCodigo(), painelGestorMonitoramentoCRMVO);
		}

	}

	public void realizarGeracaoGraficoMonitoramentoProspectPorUnidadeEnsino(Integer unidadeEnsino, Date dateInicio, Date dataTermino, PainelGestorMonitoramentoCRMVO painelGestorMonitoramentoCRMVO) throws Exception {
		painelGestorMonitoramentoCRMVO.setDadosGraficoMonitoramentoProspect("");
		StringBuilder sql = new StringBuilder("");
		sql.append(" select count(distinct prospects.codigo) as quantidade, ");
		sql.append(" Funcionario.codigo,  ");
		sql.append(" Pessoa.nome ");
		sql.append(" from prospects   ");
		// sql.append(" inner join compromissoagendapessoahorario on prospects.codigo = compromissoagendapessoahorario.prospect ");
		// sql.append(" and compromissoagendapessoahorario.codigo = (select max(codigo) from compromissoagendapessoahorario caph where prospects.codigo = caph.prospect ");
		// sql.append(" and caph.datacompromisso  >= '").append(Uteis.getDataJDBC(dateInicio)).append("' and caph.datacompromisso  <= '").append(Uteis.getDataJDBC(dataTermino)).append("') ");
		sql.append(" left join Funcionario on Funcionario.codigo = prospects.consultorPadrao ");
		sql.append(" left join Pessoa on Pessoa.codigo = Funcionario.pessoa ");
		sql.append(" left join unidadeEnsino on unidadeEnsino.codigo = prospects.unidadeEnsino ");
		sql.append(" where 1=1 and inativo = false ");
		if (unidadeEnsino == null || unidadeEnsino == 0) {
			sql.append(" and unidadeEnsino.codigo is null ");
		} else {
			sql.append(" and unidadeEnsino.codigo = ").append(unidadeEnsino);
		}
		sql.append(" group by Funcionario.codigo,  ");
		sql.append(" Pessoa.nome ");
		sql.append(" order by Pessoa.nome ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
	

		List<LegendaGraficoVO> legendaGraficoVOs = new ArrayList<LegendaGraficoVO>();
		LegendaGraficoVO legendaGraficoVO = null;
		Integer qtdeTotal = 0;
		while (rs.next()) {
			legendaGraficoVO = new LegendaGraficoVO(rs.getString("nome") == null || rs.getString("nome").trim().isEmpty() ? 0 : rs.getInt("codigo"), 
					rs.getString("nome") == null || rs.getString("nome").trim().isEmpty() ? "Sem Consultor" : Uteis.getNomeResumido(rs.getString("nome")), PainelGestorTipoMonitoramentoCRMEnum.PROSPECT_POR_UNIDADE.name(), 
							Double.valueOf(rs.getInt("quantidade")), "",  false);			 
			qtdeTotal += rs.getInt("quantidade");			
			legendaGraficoVOs.add(legendaGraficoVO);
		}
		
		for(LegendaGraficoVO legendaGrafico:legendaGraficoVOs){
			adicionarDadosRelatorioGraficoMonitiramentoProspect(((legendaGrafico.getValor()*100)/qtdeTotal.doubleValue()), legendaGrafico.getValor().intValue(), 
					legendaGrafico.getLegenda(), PainelGestorTipoMonitoramentoCRMEnum.PROSPECT_POR_CONSULTOR, legendaGrafico.getCodigo(), painelGestorMonitoramentoCRMVO);
		}
		
	}

	public void realizarGeracaoGraficoMonitoramentoProspectPorConsultor(Integer unidadeEnsino, Integer consultor, Date dateInicio, Date dataTermino, PainelGestorMonitoramentoCRMVO painelGestorMonitoramentoCRMVO) throws Exception {
		painelGestorMonitoramentoCRMVO.setDadosGraficoMonitoramentoProspect("");
		StringBuilder sql = new StringBuilder("");
		sql.append(" select count(distinct prospects.codigo) as quantidade, ");
		sql.append(" curso.codigo,  ");
		sql.append(" curso.nome ");
		sql.append(" from prospects   ");
		// sql.append(" inner join compromissoagendapessoahorario on prospects.codigo = compromissoagendapessoahorario.prospect ");
		// sql.append(" and compromissoagendapessoahorario.codigo = (select max(codigo) from compromissoagendapessoahorario caph where prospects.codigo = caph.prospect ");
		// sql.append(" and caph.datacompromisso  >= '").append(Uteis.getDataJDBC(dateInicio)).append("' and caph.datacompromisso  <= '").append(Uteis.getDataJDBC(dataTermino)).append("') ");
		sql.append(" left join Funcionario on Funcionario.codigo = prospects.consultorPadrao ");
		sql.append(" left join Pessoa on Pessoa.codigo = Funcionario.pessoa ");
		sql.append(" left join unidadeEnsino on unidadeEnsino.codigo = prospects.unidadeEnsino ");
		sql.append(" left join cursointeresse  on cursointeresse.prospects = prospects.codigo ");
		sql.append(" left join curso  on cursointeresse.curso = curso.codigo ");

		sql.append(" where 1=1 and inativo = false ");
		if (unidadeEnsino == null || unidadeEnsino == 0) {
			sql.append(" and unidadeEnsino.codigo is null ");
		} else {
			sql.append(" and unidadeEnsino.codigo = ").append(unidadeEnsino);
		}
		if (consultor == null || consultor == 0) {
			sql.append(" and Funcionario.codigo is null ");
		} else {
			sql.append(" and Funcionario.codigo = ").append(consultor);
		}
		sql.append(" group by curso.codigo,  ");
		sql.append(" curso.nome ");
		sql.append(" order by curso.nome ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<LegendaGraficoVO> legendaGraficoVOs = new ArrayList<LegendaGraficoVO>();
		LegendaGraficoVO legendaGraficoVO = null;
		Integer qtdeTotal = 0;
		while (rs.next()) {
			legendaGraficoVO = new LegendaGraficoVO(rs.getString("nome") == null || rs.getString("nome").trim().isEmpty() ? 0 : rs.getInt("codigo"), 
					rs.getString("nome") == null || rs.getString("nome").trim().isEmpty() ? "Sem Consultor" : rs.getString("nome"), PainelGestorTipoMonitoramentoCRMEnum.PROSPECT_POR_UNIDADE.name(), 
							Double.valueOf(rs.getInt("quantidade")), "", false);			 
			qtdeTotal += rs.getInt("quantidade");			
			legendaGraficoVOs.add(legendaGraficoVO);
		}
		
		for(LegendaGraficoVO legendaGrafico:legendaGraficoVOs){
			adicionarDadosRelatorioGraficoMonitiramentoProspect(((legendaGrafico.getValor()*100)/qtdeTotal.doubleValue()), legendaGrafico.getValor().intValue(), 
					legendaGrafico.getLegenda(), PainelGestorTipoMonitoramentoCRMEnum.PROSPECT_POR_CURSO, legendaGrafico.getCodigo(), painelGestorMonitoramentoCRMVO);
		}

	}

	public void adicionarDadosRelatorioGraficoMonitiramentoProspect(Double porcentagem, Integer quantidade, String titulo, PainelGestorTipoMonitoramentoCRMEnum opcao, Integer codigo, PainelGestorMonitoramentoCRMVO painelGestorMonitoramentoCRMVO) {
		if (titulo.length() > 65) {
			titulo = titulo.substring(0, 65) + "...";
		}
		StringBuilder resultado = new StringBuilder("");
		resultado.append("{");
		resultado.append("name:'").append(titulo).append("',  y: ").append(quantidade).append(", percent: ").append(Uteis.arrendondarForcando2CadasDecimais(porcentagem)).append(", campo: '").append(opcao.toString()).append("', codigo: ").append(codigo);
		resultado.append("}");
		if (opcao.equals(PainelGestorTipoMonitoramentoCRMEnum.COMO_FICOU_SABENDO_INSTITUICAO)) {
			if (painelGestorMonitoramentoCRMVO.getGraficoComoFicouSabendoInstituicao().trim().isEmpty()) {
				painelGestorMonitoramentoCRMVO.setGraficoComoFicouSabendoInstituicao(resultado.toString());
			} else {
				painelGestorMonitoramentoCRMVO.setGraficoComoFicouSabendoInstituicao(painelGestorMonitoramentoCRMVO.getGraficoComoFicouSabendoInstituicao() + ", " + resultado);
			}
		} else {
			if (painelGestorMonitoramentoCRMVO.getDadosGraficoMonitoramentoProspect().isEmpty()) {
				painelGestorMonitoramentoCRMVO.setDadosGraficoMonitoramentoProspect(resultado.toString());
			} else {
				painelGestorMonitoramentoCRMVO.setDadosGraficoMonitoramentoProspect(painelGestorMonitoramentoCRMVO.getDadosGraficoMonitoramentoProspect() + ", " + resultado);
			}
		}
	}

	public void consultarTotalMonitoramentoProspectMensal(List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dateInicio, Date dataTermino, Integer unidadeEspecifica, Integer consultor, PainelGestorMonitoramentoCRMVO painelGestorMonitoramentoCRMVO) throws Exception {
		StringBuilder sql = new StringBuilder("");
		sql.append(" select sum(quantidadeMes) as quantidadeMes, mes, ano, sum(quantidadeMatriculaMes) as quantidadeMatriculaMes, ");
		sql.append(" (select count(distinct compromissoagendapessoahorario.prospect) from compromissoagendapessoahorario ");
		sql.append(" inner join prospects on prospects.codigo = compromissoagendapessoahorario.prospect   ");
		sql.append(" inner join etapaworkflow on etapaworkflow.codigo = compromissoagendapessoahorario.etapaworkflow   ");
		sql.append(" inner join situacaoprospectpipeline   on situacaoprospectpipeline.codigo = etapaworkflow.situacaodefinirprospectfinal ");
		sql.append(" and situacaoprospectpipeline.controle = 'FINALIZADO_SUCESSO' ");
		sql.append(" where inativo = false and extract('month' from compromissoagendapessoahorario.dataCompromisso::DATE)::INT = t.mes and ");
		sql.append(" extract('year' from compromissoagendapessoahorario.dataCompromisso::DATE)::INT = t.ano ");
		if ((unidadeEspecifica == null || unidadeEspecifica == 0) && unidadeEnsinoVOs != null && painelGestorMonitoramentoCRMVO.getPainelGestorTipoMonitoramentoCRMEnum() == null) {
			if (unidadeEnsinoVOs != null && !unidadeEnsinoVOs.isEmpty()) {
				sql.append(" and (prospects.unidadeensino in (0 ");
				for (UnidadeEnsinoVO unidadeEnsinoVO : unidadeEnsinoVOs) {
					if (unidadeEnsinoVO.getFiltrarUnidadeEnsino()) {
						sql.append(", ").append(unidadeEnsinoVO.getCodigo());
					}
				}
				sql.append(" )  or  prospects.unidadeensino is  null)");
			}
		} else {
			if (painelGestorMonitoramentoCRMVO.getPainelGestorTipoMonitoramentoCRMEnum() != null) {
				if ((unidadeEspecifica == null || unidadeEspecifica == 0)) {
					sql.append(" and prospects.unidadeensino is null ");
				} else {
					sql.append(" and prospects.unidadeensino = ").append(unidadeEspecifica);
				}
			}
		}
		if (painelGestorMonitoramentoCRMVO.getPainelGestorTipoMonitoramentoCRMEnum() != null && (painelGestorMonitoramentoCRMVO.getPainelGestorTipoMonitoramentoCRMEnum().equals(PainelGestorTipoMonitoramentoCRMEnum.PROSPECT_POR_CURSO) || painelGestorMonitoramentoCRMVO.getPainelGestorTipoMonitoramentoCRMEnum().equals(PainelGestorTipoMonitoramentoCRMEnum.PROSPECT_POR_CONSULTOR))) {
			if ((consultor == null || consultor == 0) && unidadeEnsinoVOs == null) {
				sql.append(" and prospects.consultorPadrao is null ");
			} else if (consultor != null && consultor > 0) {
				sql.append(" and prospects.consultorPadrao = ").append(consultor);
			}
		}
		sql.append(" ) as quantidadeFinalizadoSucessoMes, sum(quantidadeReagendamentoMes) AS quantidadeReagendamentoMes   ");
		sql.append(" from( ");
		sql.append(" select distinct prospects.codigo, ");
		sql.append(" case when compromissoagendapessoahorario.tiposituacaocompromissoenum != 'REALIZADO_COM_REMARCACAO' then count(distinct prospects.codigo) else 0 end as quantidadeMes, ");
		sql.append(" extract('month' from compromissoagendapessoahorario.dataCompromisso::DATE)::INT as mes, ");
		sql.append(" extract('year' from compromissoagendapessoahorario.dataCompromisso::DATE)::INT as ano,  ");
		sql.append(" case when matricula.aluno is not null then count(distinct prospects.codigo) else 0 end as quantidadeMatriculaMes, ");
		sql.append(" case when compromissoagendapessoahorario.tiposituacaocompromissoenum = 'REALIZADO_COM_REMARCACAO' then count(distinct prospects.codigo) else 0 end AS quantidadeReagendamentoMes ");
		sql.append(" from prospects   ");
		sql.append(" inner join compromissoagendapessoahorario on prospects.codigo = compromissoagendapessoahorario.prospect ");
		sql.append(" and compromissoagendapessoahorario.datacompromisso::DATE  >= '").append(Uteis.getDataJDBC(dateInicio)).append("' and compromissoagendapessoahorario.datacompromisso::DATE  <= '").append(Uteis.getDataJDBC(dataTermino)).append("' ");
		sql.append(" left join matricula on matricula.aluno = prospects.pessoa ");
		sql.append(" WHERE 1 = 1 and inativo = false ");
		if ((unidadeEspecifica == null || unidadeEspecifica == 0) && unidadeEnsinoVOs != null && painelGestorMonitoramentoCRMVO.getPainelGestorTipoMonitoramentoCRMEnum() == null) {
			if (unidadeEnsinoVOs != null && !unidadeEnsinoVOs.isEmpty()) {
				sql.append(" and (prospects.unidadeensino in (0 ");
				for (UnidadeEnsinoVO unidadeEnsinoVO : unidadeEnsinoVOs) {
					if (unidadeEnsinoVO.getFiltrarUnidadeEnsino()) {
						sql.append(", ").append(unidadeEnsinoVO.getCodigo());
					}
				}
				sql.append(" )  or  prospects.unidadeensino is  null)");
			}
		} else {
			if (painelGestorMonitoramentoCRMVO.getPainelGestorTipoMonitoramentoCRMEnum() != null) {
				if ((unidadeEspecifica == null || unidadeEspecifica == 0)) {
					sql.append(" and prospects.unidadeensino is null ");
				} else {
					sql.append(" and prospects.unidadeensino = ").append(unidadeEspecifica);
				}
			}
		}
		if (painelGestorMonitoramentoCRMVO.getPainelGestorTipoMonitoramentoCRMEnum() != null && painelGestorMonitoramentoCRMVO.getPainelGestorTipoMonitoramentoCRMEnum().equals(PainelGestorTipoMonitoramentoCRMEnum.PROSPECT_POR_CONSULTOR)) {
			if ((consultor == null || consultor == 0) && unidadeEnsinoVOs == null) {
				sql.append(" and prospects.consultorPadrao is null ");
			} else if (consultor != null && consultor > 0) {
				sql.append(" and prospects.consultorPadrao = ").append(consultor);
			}
		}
		sql.append(" group by prospects.codigo, matricula.aluno, compromissoagendapessoahorario.tiposituacaocompromissoenum, extract('month' from compromissoagendapessoahorario.dataCompromisso::DATE)::INT, ");
		sql.append(" extract('year' from compromissoagendapessoahorario.dataCompromisso::DATE)::INT  ");
		sql.append(" ) as t group by mes, ano order by ano, mes ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		painelGestorMonitoramentoCRMVO.getPainelGestorMonitoramentoDetalheCRMVOs().clear();
		while (rs.next()) {
			PainelGestorMonitoramentoDetalheCRMVO painelGestorMonitoramentoDetalheCRMVO = new PainelGestorMonitoramentoDetalheCRMVO();
			painelGestorMonitoramentoDetalheCRMVO.setAno(rs.getInt("ano"));
			painelGestorMonitoramentoDetalheCRMVO.setMesAnoEnum(MesAnoEnum.getEnum(String.valueOf(rs.getInt("mes"))));
			painelGestorMonitoramentoDetalheCRMVO.setQuantidadeMes(rs.getInt("quantidadeMes"));
			painelGestorMonitoramentoDetalheCRMVO.setQuantidadeMatriculaMes(rs.getInt("quantidadeMatriculaMes"));
			painelGestorMonitoramentoDetalheCRMVO.setQuantidadeFinalizadoSucessoMes(rs.getInt("quantidadeFinalizadoSucessoMes"));
			painelGestorMonitoramentoDetalheCRMVO.getUnidadeEnsinoVO().setCodigo(unidadeEspecifica);
			painelGestorMonitoramentoDetalheCRMVO.getConsultor().setCodigo(consultor);
			painelGestorMonitoramentoDetalheCRMVO.setQuantidadeReagendamentoMes(rs.getInt("quantidadeReagendamentoMes"));
			painelGestorMonitoramentoCRMVO.getPainelGestorMonitoramentoDetalheCRMVOs().add(painelGestorMonitoramentoDetalheCRMVO);
		}

	}

	public void consultarTotaisMonitoramentoProspect(List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dateInicio, Date dataTermino, Integer unidadeEspecifica, Integer consultor, PainelGestorMonitoramentoCRMVO painelGestorMonitoramentoCRMVO) throws Exception {
		painelGestorMonitoramentoCRMVO.setQuantidadeTotalProspectsCadastradosNaoMatriculado(0);
		painelGestorMonitoramentoCRMVO.setQuantidadeTotalProspectsComAgendaNaoMatriculado(0);
		painelGestorMonitoramentoCRMVO.setQuantidadeTotalProspectsSemAgendaNaoMatriculado(0);
		painelGestorMonitoramentoCRMVO.setQuantidadeTotalProspectsNaoContactadoPeriodo1NaoMatriculado(0);
		painelGestorMonitoramentoCRMVO.setQuantidadeTotalProspectsNaoContactadoPeriodo2NaoMatriculado(0);
		painelGestorMonitoramentoCRMVO.setQuantidadeTotalProspectsNaoContactadoPeriodo3NaoMatriculado(0);
		painelGestorMonitoramentoCRMVO.setQuantidadeTotalProspectsSemConsultorResponsavelNaoMatriculado(0);
		painelGestorMonitoramentoCRMVO.setQuantidadeTotalProspectsComConsultorResponsavelNaoMatriculado(0);
		painelGestorMonitoramentoCRMVO.setQuantidadeTotalProspectsCadastradosMatriculado(0);
		painelGestorMonitoramentoCRMVO.setQuantidadeTotalProspectsComAgendaMatriculado(0);
		painelGestorMonitoramentoCRMVO.setQuantidadeTotalProspectsSemAgendaMatriculado(0);
		painelGestorMonitoramentoCRMVO.setQuantidadeTotalProspectsNaoContactadoPeriodo1Matriculado(0);
		painelGestorMonitoramentoCRMVO.setQuantidadeTotalProspectsNaoContactadoPeriodo2Matriculado(0);
		painelGestorMonitoramentoCRMVO.setQuantidadeTotalProspectsNaoContactadoPeriodo3Matriculado(0);
		painelGestorMonitoramentoCRMVO.setQuantidadeTotalProspectsSemConsultorResponsavelMatriculado(0);
		painelGestorMonitoramentoCRMVO.setQuantidadeTotalProspectsComConsultorResponsavelMatriculado(0);
		StringBuilder sql = new StringBuilder("");
		sql.append(" select matriculado, sum(quantidadeTotalProspectsCadastrados) as quantidadeTotalProspectsCadastrados, sum(quantidadeTotalProspectsComAgenda) as quantidadeTotalProspectsComAgenda,");
		sql.append(" sum(quantidadeTotalProspectsSemAgenda) as quantidadeTotalProspectsSemAgenda, sum(quantidadeTotalProspectsSemConsultorResponsavel) as quantidadeTotalProspectsSemConsultorResponsavel, ");
		sql.append(" sum(quantidadeTotalProspectsComConsultorResponsavel) as quantidadeTotalProspectsComConsultorResponsavel,  ");
		sql.append(" sum(quantidadeTotalProspectsNaoContactadoPeriodo1) as quantidadeTotalProspectsNaoContactadoPeriodo1, ");
		sql.append(" sum( quantidadeTotalProspectsNaoContactadoPeriodo2) as quantidadeTotalProspectsNaoContactadoPeriodo2,  ");
		sql.append(" sum(quantidadeTotalProspectsNaoContactadoPeriodo3) as quantidadeTotalProspectsNaoContactadoPeriodo3 ");
		sql.append(" from( ");
		sql.append(" select count(distinct prospects.codigo) as quantidadeTotalProspectsCadastrados, ");
		sql.append(" case when compromissoagendapessoahorario.codigo is not null then count(distinct prospects.codigo) else 0 end as quantidadeTotalProspectsComAgenda, ");
		sql.append(" case when compromissoagendapessoahorario.codigo is null then count(distinct prospects.codigo) else 0 end as quantidadeTotalProspectsSemAgenda, ");
		sql.append(" case when ((prospects.consultorPadrao is null) or (prospects.consultorPadrao = 0)) then count(distinct prospects.codigo) else 0 end as quantidadeTotalProspectsSemConsultorResponsavel, ");
		sql.append(" case when prospects.consultorpadrao is not null then count(distinct prospects.codigo) else 0 end as quantidadeTotalProspectsComConsultorResponsavel, ");
		sql.append(" case when case when caph.dataCompromisso is null then prospects.datacadastro::DATE else caph.dataCompromisso::DATE end < current_date and ( ");
		sql.append(" (DATE_PART('year', current_date) - DATE_PART('year', case when caph.dataCompromisso is null then prospects.datacadastro::DATE else caph.dataCompromisso::DATE end))  * 12 + (DATE_PART('month', current_date) - DATE_PART('month', case when caph.dataCompromisso is null then prospects.datacadastro::DATE else caph.dataCompromisso::DATE end)) >=  ").append(painelGestorMonitoramentoCRMVO.getPeriodoUltimoContato1());
		sql.append(" and (DATE_PART('year', current_date) - DATE_PART('year',case when caph.dataCompromisso is null then prospects.datacadastro::DATE else caph.dataCompromisso::DATE end))  * 12 + (DATE_PART('month', current_date) - DATE_PART('month', case when caph.dataCompromisso is null then prospects.datacadastro::DATE else caph.dataCompromisso::DATE end))  < ").append(painelGestorMonitoramentoCRMVO.getPeriodoUltimoContato2());
		sql.append(" ) ");
		sql.append(" then count(distinct prospects.codigo) else 0 end as quantidadeTotalProspectsNaoContactadoPeriodo1, ");
		sql.append(" case when case when caph.dataCompromisso is null then prospects.datacadastro::DATE else caph.dataCompromisso::DATE end < current_date and ( ");
		sql.append(" (DATE_PART('year', current_date) - DATE_PART('year',case when caph.dataCompromisso is null then prospects.datacadastro::DATE else caph.dataCompromisso::DATE end))  * 12 + (DATE_PART('month', current_date) - DATE_PART('month', case when caph.dataCompromisso is null then prospects.datacadastro::DATE else caph.dataCompromisso::DATE end)) >= ").append(painelGestorMonitoramentoCRMVO.getPeriodoUltimoContato2());
		sql.append(" and (DATE_PART('year', current_date) - DATE_PART('year',case when caph.dataCompromisso is null then prospects.datacadastro::DATE else caph.dataCompromisso::DATE end))  * 12 + (DATE_PART('month', current_date) - DATE_PART('month', case when caph.dataCompromisso is null then prospects.datacadastro::DATE else caph.dataCompromisso::DATE end))  < ").append(painelGestorMonitoramentoCRMVO.getPeriodoUltimoContato3());
		sql.append(" ) ");
		sql.append(" then count(distinct prospects.codigo) else 0 end as quantidadeTotalProspectsNaoContactadoPeriodo2, ");
		sql.append(" case when case when caph.dataCompromisso is null then prospects.datacadastro::DATE else caph.dataCompromisso::DATE end < current_date and ( ");
		sql.append(" DATE_PART('year', current_date) - DATE_PART('year',case when caph.dataCompromisso is null then prospects.datacadastro::DATE else caph.dataCompromisso::DATE end))  * 12 + (DATE_PART('month', current_date) - DATE_PART('month', case when caph.dataCompromisso is null then prospects.datacadastro::DATE else caph.dataCompromisso::DATE end)) >= ").append(painelGestorMonitoramentoCRMVO.getPeriodoUltimoContato3());
		sql.append(" then count(distinct prospects.codigo) else 0 end as quantidadeTotalProspectsNaoContactadoPeriodo3, ");
		sql.append(" (select count(aluno) from matricula where matricula.aluno = prospects.pessoa ) > 0 as matriculado");
		sql.append(" from prospects   ");
		
		sql.append(" left join compromissoagendapessoahorario on prospects.codigo = compromissoagendapessoahorario.prospect ");
		sql.append(" and compromissoagendapessoahorario.codigo = (select max(codigo) from compromissoagendapessoahorario cpah2 ");
		sql.append(" where prospects.inativo = false and prospects.codigo = cpah2.prospect and cpah2.datacompromisso::DATE  >= '").append(Uteis.getDataJDBC(dateInicio)).append("' and cpah2.datacompromisso::DATE  <= '").append(Uteis.getDataJDBC(dataTermino)).append("') ");
		sql.append(" left join (select max(data) as dataCompromisso, prospect from ( ");
		sql.append(" select max(dataCompromisso) as data, cpah2.prospect from compromissoagendapessoahorario cpah2 inner join prospects as pros on pros.codigo = cpah2.prospect and pros.inativo = false  group by cpah2.prospect ");
		sql.append(" union all  ");
		sql.append(" select max(dataregistro) as data, hfu.prospect from historicofollowup hfu  inner join prospects as pros on pros.codigo = hfu.prospect and pros.inativo = false  group by hfu.prospect ");
		sql.append(" union all ");
		sql.append(" select max(interacaoworkflow.datainicio) as data, interacaoworkflow.prospect from interacaoworkflow  inner join prospects as pros on pros.codigo = interacaoworkflow.prospect and pros.inativo = false  group by interacaoworkflow.prospect ");
		sql.append(" ) as dataContato group by prospect) as caph on prospects.codigo = caph.prospect  ");

		sql.append(" where 1=1 ");
		sql.append(" and prospects.inativo = false  ");
		if ((unidadeEspecifica == null || unidadeEspecifica == 0) && unidadeEnsinoVOs != null) {
			if (unidadeEnsinoVOs != null && !unidadeEnsinoVOs.isEmpty()) {
				sql.append(" and (prospects.unidadeensino in (0 ");
				for (UnidadeEnsinoVO unidadeEnsinoVO : unidadeEnsinoVOs) {
					if (unidadeEnsinoVO.getFiltrarUnidadeEnsino()) {
						sql.append(", ").append(unidadeEnsinoVO.getCodigo());
					}
				}
				sql.append(" )  or  prospects.unidadeensino is  null)");
			}
		} else {
			if (painelGestorMonitoramentoCRMVO.getPainelGestorTipoMonitoramentoCRMEnum() != null) {
				if ((unidadeEspecifica == null || unidadeEspecifica == 0)) {
					sql.append(" and prospects.unidadeensino is null ");
				} else {
					sql.append(" and prospects.unidadeensino = ").append(unidadeEspecifica);
				}
			}
		}
		if (painelGestorMonitoramentoCRMVO.getPainelGestorTipoMonitoramentoCRMEnum() != null && painelGestorMonitoramentoCRMVO.getPainelGestorTipoMonitoramentoCRMEnum().equals(PainelGestorTipoMonitoramentoCRMEnum.PROSPECT_POR_CONSULTOR)) {
			if ((consultor == null || consultor == 0) && unidadeEnsinoVOs == null) {
				sql.append(" and prospects.consultorPadrao is null ");
			} else if (consultor != null && consultor > 0) {
				sql.append(" and prospects.consultorPadrao = ").append(consultor);
			}
		}
		sql.append(" group by prospects.codigo, compromissoagendapessoahorario.codigo, consultorpadrao, caph.dataCompromisso, prospects.datacadastro, matriculado ");
		sql.append(" ) as t group by matriculado ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		while (rs.next()) {
			if(rs.getBoolean("matriculado")){
				painelGestorMonitoramentoCRMVO.setQuantidadeTotalProspectsCadastradosMatriculado(rs.getInt("quantidadeTotalProspectsCadastrados"));
				painelGestorMonitoramentoCRMVO.setQuantidadeTotalProspectsComAgendaMatriculado(rs.getInt("quantidadeTotalProspectsComAgenda"));
				painelGestorMonitoramentoCRMVO.setQuantidadeTotalProspectsSemAgendaMatriculado(rs.getInt("quantidadeTotalProspectsSemAgenda"));
				painelGestorMonitoramentoCRMVO.setQuantidadeTotalProspectsNaoContactadoPeriodo1Matriculado(rs.getInt("quantidadeTotalProspectsNaoContactadoPeriodo1"));
				painelGestorMonitoramentoCRMVO.setQuantidadeTotalProspectsNaoContactadoPeriodo2Matriculado(rs.getInt("quantidadeTotalProspectsNaoContactadoPeriodo2"));
				painelGestorMonitoramentoCRMVO.setQuantidadeTotalProspectsNaoContactadoPeriodo3Matriculado(rs.getInt("quantidadeTotalProspectsNaoContactadoPeriodo3"));
				painelGestorMonitoramentoCRMVO.setQuantidadeTotalProspectsSemConsultorResponsavelMatriculado(rs.getInt("quantidadeTotalProspectsSemConsultorResponsavel"));
				painelGestorMonitoramentoCRMVO.setQuantidadeTotalProspectsComConsultorResponsavelMatriculado(rs.getInt("quantidadeTotalProspectsComConsultorResponsavel"));
			}else{
				painelGestorMonitoramentoCRMVO.setQuantidadeTotalProspectsCadastradosNaoMatriculado(rs.getInt("quantidadeTotalProspectsCadastrados"));
				painelGestorMonitoramentoCRMVO.setQuantidadeTotalProspectsComAgendaNaoMatriculado(rs.getInt("quantidadeTotalProspectsComAgenda"));
				painelGestorMonitoramentoCRMVO.setQuantidadeTotalProspectsSemAgendaNaoMatriculado(rs.getInt("quantidadeTotalProspectsSemAgenda"));
				painelGestorMonitoramentoCRMVO.setQuantidadeTotalProspectsNaoContactadoPeriodo1NaoMatriculado(rs.getInt("quantidadeTotalProspectsNaoContactadoPeriodo1"));
				painelGestorMonitoramentoCRMVO.setQuantidadeTotalProspectsNaoContactadoPeriodo2NaoMatriculado(rs.getInt("quantidadeTotalProspectsNaoContactadoPeriodo2"));
				painelGestorMonitoramentoCRMVO.setQuantidadeTotalProspectsNaoContactadoPeriodo3NaoMatriculado(rs.getInt("quantidadeTotalProspectsNaoContactadoPeriodo3"));
				painelGestorMonitoramentoCRMVO.setQuantidadeTotalProspectsSemConsultorResponsavelNaoMatriculado(rs.getInt("quantidadeTotalProspectsSemConsultorResponsavel"));
				painelGestorMonitoramentoCRMVO.setQuantidadeTotalProspectsComConsultorResponsavelNaoMatriculado(rs.getInt("quantidadeTotalProspectsComConsultorResponsavel"));
			}
		}
	}

	private void montarDadosGeraisPreInscricao(PainelGestorMonitoramentoCRMVO painelGestorMonitoramentoCRMVO, SqlRowSet rs, Boolean separadoPorCurso, Boolean separadoPorConsultor, PainelGestorTipoMonitoramentoCRMEnum painelGestorTipoMonitoramentoCRMEnum) {
		if (painelGestorTipoMonitoramentoCRMEnum.equals(PainelGestorTipoMonitoramentoCRMEnum.PRE_INSCRICAO)) {
			painelGestorMonitoramentoCRMVO.getPainelGestorMonitoramentoDetalhePreInscricaoCRMVOs().clear();
			painelGestorMonitoramentoCRMVO.setDadosGraficoPreInscricao("");
		} else {
			painelGestorMonitoramentoCRMVO.getPainelGestorMonitoramentoDetalheCRMVOs().clear();
		}
		while (rs.next()) {
			PainelGestorMonitoramentoDetalheCRMVO painelGestorMonitoramentoDetalheCRMVO = new PainelGestorMonitoramentoDetalheCRMVO();
			painelGestorMonitoramentoDetalheCRMVO.setAno(rs.getInt("ano"));
			painelGestorMonitoramentoDetalheCRMVO.setMesAnoEnum(MesAnoEnum.getEnum(String.valueOf(rs.getInt("mes"))));
			painelGestorMonitoramentoDetalheCRMVO.setPainelGestorTipoMonitoramentoCRMEnum(painelGestorTipoMonitoramentoCRMEnum);
			painelGestorMonitoramentoDetalheCRMVO.setQuantidadeDia(rs.getInt("totaldia"));
			painelGestorMonitoramentoDetalheCRMVO.setQuantidadeSemana(rs.getInt("totalsemana"));
			painelGestorMonitoramentoDetalheCRMVO.setQuantidadeMes(rs.getInt("totalmes"));

			if (separadoPorConsultor != null && separadoPorConsultor) {
				painelGestorMonitoramentoDetalheCRMVO.getConsultor().setCodigo(rs.getInt("funcionario_codigo"));
				painelGestorMonitoramentoDetalheCRMVO.getConsultor().getPessoa().setNome(rs.getString("pessoa_nome"));
			}
			if (separadoPorCurso != null && separadoPorCurso) {
				painelGestorMonitoramentoDetalheCRMVO.getCurso().setCodigo(rs.getInt("curso_codigo"));
				painelGestorMonitoramentoDetalheCRMVO.getCurso().setNome(rs.getString("curso_nome"));
			}

			if (painelGestorTipoMonitoramentoCRMEnum.equals(PainelGestorTipoMonitoramentoCRMEnum.PRE_INSCRICAO)) {
				if (painelGestorMonitoramentoCRMVO.getDadosGraficoPreInscricao().isEmpty()) {
					painelGestorMonitoramentoCRMVO.setDadosGraficoPreInscricao(painelGestorMonitoramentoCRMVO.getDadosGraficoPreInscricao() + "[Date.UTC(" + rs.getInt("ano") + ", " + rs.getInt("mes") + ",1), " + rs.getInt("totalmes") + "]");
				} else {
					painelGestorMonitoramentoCRMVO.setDadosGraficoPreInscricao(painelGestorMonitoramentoCRMVO.getDadosGraficoPreInscricao() + ",[Date.UTC(" + rs.getInt("ano") + ", " + rs.getInt("mes") + ",1), " + rs.getInt("totalmes") + "]");
				}
			}
			if (painelGestorTipoMonitoramentoCRMEnum.equals(PainelGestorTipoMonitoramentoCRMEnum.PRE_INSCRICAO)) {
				painelGestorMonitoramentoCRMVO.getPainelGestorMonitoramentoDetalhePreInscricaoCRMVOs().add(painelGestorMonitoramentoDetalheCRMVO);
			} else {
				painelGestorMonitoramentoCRMVO.getPainelGestorMonitoramentoDetalheCRMVOs().add(painelGestorMonitoramentoDetalheCRMVO);
			}
		}
	
	}

	private void definirComoPrimeiraLinhaResultadoPreInscricoesSemConsultorResponsavel(List<PainelGestorMonitoramentoDetalheCRMVO> listaFinal) {
		try {
			PainelGestorMonitoramentoDetalheCRMVO ultimoConsultor = listaFinal.get(listaFinal.size() - 1);
			if (ultimoConsultor.getConsultor().getCodigo().equals(0)) {
				listaFinal.remove(listaFinal.size() - 1);
				listaFinal.add(0, ultimoConsultor);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private SqlRowSet consultarDadosPreInscicao(List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, Boolean separadoPorCurso, Boolean separadoPorConsultor, Integer curso, Integer consultor) {
		StringBuilder sql = new StringBuilder("");
		sql.append(" select sum(totalmes)::INT as totalmes, sum(totaldia)::INT as totaldia, sum(totalsemana)::INT totalsemana, mes::INT, ano::INT ");
		if (separadoPorCurso != null && separadoPorCurso) {
			sql.append(" , curso_codigo, curso_nome");
		}
		if (separadoPorConsultor != null && separadoPorConsultor) {
			sql.append(" , funcionario_codigo, pessoa_nome");
		}
		sql.append(" from (");
		sql.append(" select case when data::DATE = current_date then count(preinscricao.codigo) else 0 end totaldia,");
		sql.append(" case when data >= (current_date - 7) and data <= current_date then count(preinscricao.codigo) else 0 end totalsemana,");
		sql.append(" count(preinscricao.codigo) as totalmes ,");
		sql.append(" extract(month from data) as mes, extract(year from data) as ano");
		if (separadoPorCurso != null && separadoPorCurso) {
			sql.append(" , curso.codigo as curso_codigo, curso.nome as curso_nome");
		}
		if (separadoPorConsultor != null && separadoPorConsultor) {
			sql.append(" , funcionario.codigo as funcionario_codigo, pessoa.nome  as pessoa_nome");
		}
		sql.append(" from preinscricao");
		if (separadoPorCurso != null && separadoPorCurso) {
			sql.append(" inner join curso on curso.codigo =   preinscricao.curso");
		}

		sql.append(" inner join prospects on prospects.codigo =   preinscricao.prospect");
		sql.append(" left join funcionario on funcionario.codigo =   prospects.consultorPadrao");
		sql.append(" left join pessoa on funcionario.pessoa =   pessoa.codigo");

		sql.append(" where ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "preinscricao.data", false));
		if (unidadeEnsinoVOs != null && !unidadeEnsinoVOs.isEmpty()) {
			sql.append(" and preinscricao.unidadeensino in (0 ");
			for (UnidadeEnsinoVO unidadeEnsinoVO : unidadeEnsinoVOs) {
				if (unidadeEnsinoVO.getFiltrarUnidadeEnsino()) {
					sql.append(", ").append(unidadeEnsinoVO.getCodigo());
				}
			}
			sql.append(" )");
		}
		if (curso != null && curso > 0) {
			sql.append(" and preinscricao.curso = ").append(curso);
		}
		if (consultor != null && consultor > 0) {
			sql.append(" and prospects.consultorPadrao = ").append(consultor);
		} else {
			
			if (consultor == -1) {
				sql.append(" and (funcionario.codigo is null or funcionario.codigo = 0) ");
			}
		}
		sql.append(" and prospects.inativo = false ");
		sql.append(" group by preinscricao.data");
		if (separadoPorCurso != null && separadoPorCurso) {
			sql.append(" ,curso.codigo, curso.nome");
		}
		if (separadoPorConsultor != null && separadoPorConsultor) {
			sql.append(" , funcionario.codigo, pessoa.nome");
		}
		sql.append(" ) as t group by mes, ano");
		if (separadoPorCurso != null && separadoPorCurso) {
			sql.append(" ,curso_codigo, curso_nome");
		}
		if (separadoPorConsultor != null && separadoPorConsultor) {
			sql.append(" ,funcionario_codigo, pessoa_nome");
		}
		sql.append(" order by ano, mes ");
		if (separadoPorCurso != null && separadoPorCurso) {
			sql.append(" , curso_nome");
		}
		if (separadoPorConsultor != null && separadoPorConsultor) {
			sql.append(" , pessoa_nome");
		}
		return getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
	}

	private List<ProspectsVO> consultarDadosProspectPreInscicao(List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, Integer curso, Integer consultor) throws Exception {
		StringBuilder sql = new StringBuilder("");
		sql.append(" select prospects.codigo, prospects.nome, prospects.emailPrincipal, prospects.telefoneResidencial, ");
		sql.append(" unidadeensino.codigo as \"unidadeensino.codigo\", unidadeensino.nome as \"unidadeensino.nome\", curso.nome as \"curso.nome\",  ");
		sql.append(" Funcionario.codigo as \"funcionario.codigo\", Pessoa.nome as \"pessoa.nome\", Pessoa.codigo as \"pessoa.codigo\",  ");
		sql.append(" preinscricao.data as \"preinscricao.data\",  preinscricao.codigo as \"preinscricao.codigo\" ");
		sql.append(" from preinscricao");
		sql.append(" inner join unidadeensino on unidadeensino.codigo = preinscricao.unidadeensino ");
		sql.append(" inner join curso on curso.codigo = preinscricao.curso ");
		sql.append(" inner join prospects on preinscricao.prospect =   prospects.codigo");
		sql.append(" left  join Funcionario on Funcionario.codigo =   prospects.consultorPadrao");
		sql.append(" left  join Pessoa on Funcionario.pessoa =   Pessoa.codigo");
		sql.append(" where  ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "preinscricao.data", false));
		sql.append(" and prospects.inativo = false  ");
		if (unidadeEnsinoVOs != null && !unidadeEnsinoVOs.isEmpty()) {
			sql.append(" and preinscricao.unidadeensino in (0 ");
			for (UnidadeEnsinoVO unidadeEnsinoVO : unidadeEnsinoVOs) {
				if (unidadeEnsinoVO.getFiltrarUnidadeEnsino()) {
					sql.append(", ").append(unidadeEnsinoVO.getCodigo());
				}
			}
			sql.append(" )");
		}
		if (curso != null && curso > 0) {
			sql.append(" and preinscricao.curso = ").append(curso);
		}
		if (consultor != null && consultor > 0) {
			sql.append(" and prospects.consultorPadrao = ").append(consultor);
		} else {
			if (curso == null || curso == 0) {
				sql.append(" and (prospects.consultorPadrao is null) or (prospects.consultorPadrao = 0) ");
			}
		}

		sql.append(" order by prospects.nome");

		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<ProspectsVO> prospectsVOs = new ArrayList<ProspectsVO>(0);
		while (rs.next()) {
			ProspectsVO prospectsVO = new ProspectsVO();
			prospectsVO.setCodigo(rs.getInt("codigo"));
			prospectsVO.setNome(rs.getString("nome"));
			prospectsVO.setEmailPrincipal(rs.getString("emailPrincipal"));
			prospectsVO.setTelefoneResidencial(rs.getString("telefoneResidencial"));
			prospectsVO.getConsultorPadrao().setCodigo(rs.getInt("funcionario.codigo"));
			prospectsVO.getConsultorPadrao().getPessoa().setCodigo(rs.getInt("pessoa.codigo"));
			prospectsVO.getConsultorPadrao().getPessoa().setNome(rs.getString("pessoa.nome"));
			prospectsVO.setCurso(rs.getString("curso.nome"));
			prospectsVO.getUnidadeEnsino().setCodigo(rs.getInt("unidadeensino.codigo"));
			prospectsVO.getUnidadeEnsino().setNome(rs.getString("unidadeensino.nome"));
			prospectsVO.getPreInscricao().setData(rs.getDate("preinscricao.data"));
			prospectsVO.getPreInscricao().setCodigo(rs.getInt("preinscricao.codigo"));
			prospectsVOs.add(prospectsVO);
		}
		return prospectsVOs;
	}

	@Override
	public List<ProspectsVO> consultarDadosProspectMonitoramento(List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, Integer curso, Integer consultor, Integer unidadeEspecifica, TipoFiltroMonitamentoCrmProspectEnum tipoFiltroMonitamentoCrmProspectEnum, MesAnoEnum mesAno, Integer ano, Integer periodoUltimoContato1, Integer periodoUltimoContato2, Integer periodoUltimoContato3, PainelGestorTipoMonitoramentoCRMEnum painelGestorTipoMonitoramentoCRMEnum, Boolean matriculado, Integer limit, Integer offset, Integer codigoSegmentacaoOpcao) throws Exception {
		StringBuilder sql = new StringBuilder("");
		sql.append(" select prospects.codigo, prospects.nome, prospects.emailPrincipal, prospects.telefoneResidencial, ");
		sql.append(" unidadeensino.codigo as \"unidadeensino.codigo\", unidadeensino.nome as \"unidadeensino.nome\",  ");
		sql.append(" Funcionario.codigo as \"funcionario.codigo\", Pessoa.nome as \"pessoa.nome\", Pessoa.codigo as \"pessoa.codigo\", prospects.sincronizadordstation  ");
		sql.append(" from prospects");
		if(!codigoSegmentacaoOpcao.equals(0)) {
			sql.append(" inner join prospectsegmentacaoopcao  on prospects.codigo = prospectsegmentacaoopcao.prospect");
			sql.append(" inner join segmentacaoopcao  on segmentacaoopcao.codigo = prospectsegmentacaoopcao.segmentacaoopcao");
			sql.append(" inner join segmentacaoprospect  on segmentacaoopcao.segmentacaoprospect = segmentacaoprospect.codigo");
		}
		sql.append(" left join unidadeensino on unidadeensino.codigo = prospects.unidadeensino ");
		sql.append(" left  join Funcionario on Funcionario.codigo =   prospects.consultorPadrao");
		sql.append(" left  join Pessoa on Funcionario.pessoa =   Pessoa.codigo");		
		if (painelGestorTipoMonitoramentoCRMEnum != null && painelGestorTipoMonitoramentoCRMEnum.equals(PainelGestorTipoMonitoramentoCRMEnum.PROSPECT_POR_CURSO)) {
			sql.append(" left join cursointeresse  on cursointeresse.prospects = prospects.codigo ");
			sql.append(" left join curso  on cursointeresse.curso = curso.codigo ");
		}
		sql.append(" where 1 = 1 ");
		if(!codigoSegmentacaoOpcao.equals(0)) {
			sql.append(" and segmentacaoopcao.codigo = ").append(codigoSegmentacaoOpcao);
		}
		if(matriculado != null){
			if(matriculado){
				sql.append(" and prospects.pessoa in (select aluno from matricula where aluno = prospects.pessoa ) ");
			}else{
				sql.append(" and prospects.pessoa not in (select aluno from matricula where aluno = prospects.pessoa ) ");
			}
		}
		sql.append(" and prospects.inativo = false  ");
		if (painelGestorTipoMonitoramentoCRMEnum == null || painelGestorTipoMonitoramentoCRMEnum.equals(PainelGestorTipoMonitoramentoCRMEnum.SEGMENTACAO_PROSPECT)) {
			if (unidadeEnsinoVOs != null && !unidadeEnsinoVOs.isEmpty()) {
				sql.append(" and (prospects.unidadeensino in (0 ");
				for (UnidadeEnsinoVO unidadeEnsinoVO : unidadeEnsinoVOs) {
					if (unidadeEnsinoVO.getFiltrarUnidadeEnsino()) {
						sql.append(", ").append(unidadeEnsinoVO.getCodigo());
					}
				}
				sql.append(" )  or  prospects.unidadeensino is  null)");
			}
		} else {
			if (painelGestorTipoMonitoramentoCRMEnum != null) {
				if (unidadeEspecifica == null || unidadeEspecifica == 0) {
					sql.append(" and prospects.unidadeensino is null ");
				} else {
					sql.append(" and prospects.unidadeensino = ").append(unidadeEspecifica);
				}
			}
		}
		if (painelGestorTipoMonitoramentoCRMEnum != null && painelGestorTipoMonitoramentoCRMEnum.equals(PainelGestorTipoMonitoramentoCRMEnum.PROSPECT_POR_CURSO)) {
			if (curso != null && curso > 0) {
				sql.append(" and curso.codigo = ").append(curso);
			} else {
				sql.append(" and curso.codigo is null ");
			}
		}
		if (painelGestorTipoMonitoramentoCRMEnum != null && (painelGestorTipoMonitoramentoCRMEnum.equals(PainelGestorTipoMonitoramentoCRMEnum.PROSPECT_POR_CURSO) 
				|| painelGestorTipoMonitoramentoCRMEnum.equals(PainelGestorTipoMonitoramentoCRMEnum.PROSPECT_POR_CONSULTOR))) {
			if (consultor != null && consultor > 0) {
				sql.append(" and prospects.consultorPadrao = ").append(consultor);
			} else {
				sql.append(" and ((prospects.consultorPadrao is null) or (prospects.consultorPadrao = 0)) ");
			}
		}
		if (tipoFiltroMonitamentoCrmProspectEnum != null) {
			if (tipoFiltroMonitamentoCrmProspectEnum.equals(TipoFiltroMonitamentoCrmProspectEnum.TODOS_PROSPECTS_SEM_CONSULTOR)) {
				sql.append(" and ((prospects.consultorPadrao is null) or (prospects.consultorPadrao = 0)) ");			
			} else if (tipoFiltroMonitamentoCrmProspectEnum.equals(TipoFiltroMonitamentoCrmProspectEnum.TODOS_PROSPECTS_SEM_CONTATO_PERIODO1)) {

				sql.append(" and prospects.codigo in  ");
				sql.append(" ( select p.codigo from prospects p ");
				sql.append(" left join (select max(data) as dataCompromisso, prospect from ( ");
				sql.append(" select max(dataCompromisso) as data, cpah2.prospect from compromissoagendapessoahorario cpah2  group by cpah2.prospect ");
				sql.append(" union all  ");
				sql.append(" select max(dataregistro) as data, hfu.prospect from historicofollowup hfu  group by hfu.prospect ");
				sql.append(" union all  ");
				sql.append(" select max(interacaoworkflow.datainicio) as data, interacaoworkflow.prospect from interacaoworkflow  group by interacaoworkflow.prospect ");
				sql.append(" ) as dataContato group by prospect) as caph on p.codigo = caph.prospect  ");
				sql.append(" where case when caph.dataCompromisso is null then p.datacadastro::DATE else caph.dataCompromisso::DATE end < current_date ");
				sql.append("  and (  ");
				sql.append(" (DATE_PART('year', current_date) - DATE_PART('year',case when caph.dataCompromisso is null then p.datacadastro::DATE else caph.dataCompromisso::DATE end))  * 12 + (DATE_PART('month', current_date) - DATE_PART('month', case when caph.dataCompromisso is null then p.datacadastro::DATE else caph.dataCompromisso::DATE end)) >=  ").append(periodoUltimoContato1);
				sql.append(" and (DATE_PART('year', current_date) - DATE_PART('year',case when caph.dataCompromisso is null then p.datacadastro::DATE else caph.dataCompromisso::DATE end))  * 12 + (DATE_PART('month', current_date) - DATE_PART('month', case when caph.dataCompromisso is null then p.datacadastro::DATE else caph.dataCompromisso::DATE end))  < ").append(periodoUltimoContato2);
				sql.append(" ) ");
				sql.append(" ) ");
			} else if (tipoFiltroMonitamentoCrmProspectEnum.equals(TipoFiltroMonitamentoCrmProspectEnum.TODOS_PROSPECTS_SEM_CONTATO_PERIODO2)) {
				sql.append(" and prospects.codigo in  ");
				sql.append(" ( select p.codigo from prospects p ");
				sql.append(" left join (select max(data) as dataCompromisso, prospect from ( ");
				sql.append(" select max(dataCompromisso) as data, cpah2.prospect from compromissoagendapessoahorario cpah2  group by cpah2.prospect ");
				sql.append(" union all  ");
				sql.append(" select max(dataregistro) as data, hfu.prospect from historicofollowup hfu  group by hfu.prospect ");
				sql.append(" union all  ");
				sql.append(" select max(interacaoworkflow.datainicio) as data, interacaoworkflow.prospect from interacaoworkflow  group by interacaoworkflow.prospect ");
				sql.append(" ) as dataContato group by prospect) as caph on p.codigo = caph.prospect  ");

				sql.append(" where case when caph.dataCompromisso is null then p.datacadastro::DATE else caph.dataCompromisso::DATE end < current_date ");
				sql.append("  and (  ");
				sql.append(" (DATE_PART('year', current_date) - DATE_PART('year',case when caph.dataCompromisso is null then p.datacadastro::DATE else caph.dataCompromisso::DATE end))  * 12 + (DATE_PART('month', current_date) - DATE_PART('month', case when caph.dataCompromisso is null then p.datacadastro::DATE else caph.dataCompromisso::DATE end)) >=  ").append(periodoUltimoContato2);
				sql.append(" and (DATE_PART('year', current_date) - DATE_PART('year',case when caph.dataCompromisso is null then p.datacadastro::DATE else caph.dataCompromisso::DATE end))  * 12 + (DATE_PART('month', current_date) - DATE_PART('month', case when caph.dataCompromisso is null then p.datacadastro::DATE else caph.dataCompromisso::DATE end))  < ").append(periodoUltimoContato3);
				sql.append(" ) ");
				sql.append(" ) ");
			} else if (tipoFiltroMonitamentoCrmProspectEnum.equals(TipoFiltroMonitamentoCrmProspectEnum.TODOS_PROSPECTS_SEM_CONTATO_PERIODO3)) {
				sql.append(" and prospects.codigo in  ");
				sql.append(" ( select p.codigo from prospects p ");
				sql.append(" left join (select max(data) as dataCompromisso, prospect from ( ");
				sql.append(" select max(dataCompromisso) as data, cpah2.prospect from compromissoagendapessoahorario cpah2  group by cpah2.prospect ");
				sql.append(" union all  ");
				sql.append(" select max(dataregistro) as data, hfu.prospect from historicofollowup hfu  group by hfu.prospect ");
				sql.append(" union all  ");
				sql.append(" select max(interacaoworkflow.datainicio) as data, interacaoworkflow.prospect from interacaoworkflow  group by interacaoworkflow.prospect ");
				sql.append(" ) as dataContato group by prospect) as caph on p.codigo = caph.prospect  ");
				sql.append(" where case when caph.dataCompromisso is null then p.datacadastro::DATE else caph.dataCompromisso::DATE end < current_date ");
				sql.append("  and (  ");
				sql.append(" (DATE_PART('year', current_date) - DATE_PART('year',case when caph.dataCompromisso is null then p.datacadastro::DATE else caph.dataCompromisso::DATE end))  * 12 + (DATE_PART('month', current_date) - DATE_PART('month', case when caph.dataCompromisso is null then p.datacadastro::DATE else caph.dataCompromisso::DATE end)) >=  ").append(periodoUltimoContato3);
				sql.append(" ) ");
				sql.append(" ) ");
			} else if (tipoFiltroMonitamentoCrmProspectEnum.equals(TipoFiltroMonitamentoCrmProspectEnum.MES_ANO_ABORDADO)) {
				sql.append(" and prospects.codigo in  ");
				sql.append(" ( select p.codigo from prospects p ");
				sql.append(" inner join compromissoagendapessoahorario caph on p.codigo = caph.prospect ");
				sql.append(" where ");
				sql.append(" DATE_PART('month', caph.dataCompromisso::DATE) = ").append(Integer.valueOf(mesAno.getKey()));
				sql.append(" and DATE_PART('year', caph.dataCompromisso::DATE) =   ").append(ano);
				sql.append(" and caph.tiposituacaocompromissoenum != 'REALIZADO_COM_REMARCACAO' ");
				sql.append(" )");
			} else if (tipoFiltroMonitamentoCrmProspectEnum.equals(TipoFiltroMonitamentoCrmProspectEnum.MES_ANO_FINALIZADO_SUCESSO)) {
				sql.append(" and prospects.codigo in  ");
				sql.append(" ( select p.codigo from prospects p ");
				sql.append(" inner join compromissoagendapessoahorario caph on p.codigo = caph.prospect ");
				sql.append(" inner join etapaworkflow on etapaworkflow.codigo = caph.etapaworkflow ");
				sql.append(" inner join situacaoprospectpipeline   on situacaoprospectpipeline.codigo = etapaworkflow.situacaodefinirprospectfinal ");
				sql.append(" where ");
				sql.append(" DATE_PART('month', caph.dataCompromisso::DATE) = ").append(Integer.valueOf(mesAno.getKey()));
				sql.append(" and DATE_PART('year', caph.dataCompromisso::DATE) =   ").append(ano);
				sql.append(" and situacaoprospectpipeline.controle = 'FINALIZADO_SUCESSO')");
			} else if (tipoFiltroMonitamentoCrmProspectEnum.equals(TipoFiltroMonitamentoCrmProspectEnum.MES_ANO_MATRICULADO)) {
				sql.append(" and prospects.codigo in  ");
				sql.append(" ( select distinct p.codigo from prospects p ");
				sql.append(" inner join compromissoagendapessoahorario caph on p.codigo = caph.prospect ");
				sql.append(" inner join matricula on matricula.aluno = p.pessoa ");
				sql.append(" where ");
				sql.append(" extract('month' from caph.dataCompromisso::DATE) = ").append(Integer.valueOf(mesAno.getKey()));
				sql.append(" and extract('year' from caph.dataCompromisso::DATE) =   ").append(ano);
				sql.append(" )");
			} else if (tipoFiltroMonitamentoCrmProspectEnum.equals(TipoFiltroMonitamentoCrmProspectEnum.MES_ANO_REAGENDADO)) {
				sql.append(" and prospects.codigo in  ");
				sql.append(" ( select p.codigo from prospects p ");
				sql.append(" inner join compromissoagendapessoahorario caph on p.codigo = caph.prospect ");
				sql.append(" where ");
				sql.append(" DATE_PART('month', caph.dataCompromisso::DATE) = ").append(Integer.valueOf(mesAno.getKey()));
				sql.append(" and DATE_PART('year', caph.dataCompromisso::DATE) =   ").append(ano);
				sql.append(" and caph.tiposituacaocompromissoenum = 'REALIZADO_COM_REMARCACAO' ");
				sql.append(" )");

			} else if (tipoFiltroMonitamentoCrmProspectEnum.equals(TipoFiltroMonitamentoCrmProspectEnum.TODOS_PROSPECTS_COM_CONSULTOR)) {
				sql.append(" and prospects.consultorPadrao is not null ");
			} else if (tipoFiltroMonitamentoCrmProspectEnum.equals(TipoFiltroMonitamentoCrmProspectEnum.TODOS_PROSPECTS_COM_AGENDA)) {
				sql.append(" and prospects.codigo in ");
				sql.append(" (select prospect from compromissoagendapessoahorario caph where prospects.codigo = caph.prospect ");
				sql.append(" and caph.datacompromisso  >= '").append(Uteis.getDataJDBC(dataInicio)).append("' and caph.datacompromisso  <= '").append(Uteis.getDataJDBC(dataTermino)).append("') ");
			} else if (tipoFiltroMonitamentoCrmProspectEnum.equals(TipoFiltroMonitamentoCrmProspectEnum.TODOS_PROSPECTS_SEM_AGENDA)) {
				sql.append(" and prospects.codigo not in ");
				sql.append(" (select prospect from compromissoagendapessoahorario caph where prospects.codigo = caph.prospect ");
				sql.append(" and caph.datacompromisso  >= '").append(Uteis.getDataJDBC(dataInicio)).append("' and caph.datacompromisso  <= '").append(Uteis.getDataJDBC(dataTermino)).append("') ");
			}
		}

		sql.append(" group by prospects.codigo, prospects.nome, prospects.emailPrincipal, prospects.telefoneResidencial, unidadeensino.codigo, unidadeensino.nome, Funcionario.codigo, Pessoa.nome, Pessoa.codigo  ");
		sql.append(" order by prospects.nome");
		if (limit != null && limit > 0) {
			sql.append(" limit ").append(limit);
			sql.append(" offset ").append(offset);
		}

		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<ProspectsVO> prospectsVOs = new ArrayList<ProspectsVO>(0);
		while (rs.next()) {
			ProspectsVO prospectsVO = new ProspectsVO();
			prospectsVO.setCodigo(rs.getInt("codigo"));
			prospectsVO.setNome(rs.getString("nome"));
			prospectsVO.setEmailPrincipal(rs.getString("emailPrincipal"));
			prospectsVO.setTelefoneResidencial(rs.getString("telefoneResidencial"));
			prospectsVO.getConsultorPadrao().setCodigo(rs.getInt("funcionario.codigo"));
			prospectsVO.getConsultorPadrao().getPessoa().setCodigo(rs.getInt("pessoa.codigo"));
			prospectsVO.getConsultorPadrao().getPessoa().setNome(rs.getString("pessoa.nome"));
			prospectsVO.getUnidadeEnsino().setCodigo(rs.getInt("unidadeensino.codigo"));
			prospectsVO.getUnidadeEnsino().setNome(rs.getString("unidadeensino.nome"));
			prospectsVO.setSincronizadoRDStation(rs.getBoolean("sincronizadordstation"));

			prospectsVOs.add(prospectsVO);
		}
		return prospectsVOs;
	}

	@Override
	public Integer consultarTotalDadosProspectMonitoramento(List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, Integer curso, Integer consultor, Integer unidadeEspecifica, TipoFiltroMonitamentoCrmProspectEnum tipoFiltroMonitamentoCrmProspectEnum, MesAnoEnum mesAno, Integer ano, Integer periodoUltimoContato1, Integer periodoUltimoContato2, Integer periodoUltimoContato3, PainelGestorTipoMonitoramentoCRMEnum painelGestorTipoMonitoramentoCRMEnum, Boolean matriculado, Integer codigoSegmentacaoOpcao) throws Exception {
		StringBuilder sql = new StringBuilder("");
		sql.append(" select count(distinct prospects.codigo) as qtde ");
		sql.append(" from prospects");
		if(!codigoSegmentacaoOpcao.equals(0)) {
			sql.append(" inner join prospectsegmentacaoopcao  on prospects.codigo = prospectsegmentacaoopcao.prospect");
			sql.append(" inner join segmentacaoopcao  on segmentacaoopcao.codigo = prospectsegmentacaoopcao.segmentacaoopcao");
			sql.append(" inner join segmentacaoprospect  on segmentacaoopcao.segmentacaoprospect = segmentacaoprospect.codigo");
		}
		sql.append(" left join unidadeensino on unidadeensino.codigo = prospects.unidadeensino ");
		sql.append(" left  join Funcionario on Funcionario.codigo =   prospects.consultorPadrao");
		sql.append(" left  join Pessoa on Funcionario.pessoa =   Pessoa.codigo");

		if (painelGestorTipoMonitoramentoCRMEnum != null && painelGestorTipoMonitoramentoCRMEnum.equals(PainelGestorTipoMonitoramentoCRMEnum.PROSPECT_POR_CURSO)) {
			sql.append(" left join cursointeresse  on cursointeresse.prospects = prospects.codigo ");
			sql.append(" left join curso  on cursointeresse.curso = curso.codigo ");
		}
		
		

		sql.append(" where 1 = 1 ");
		if(!codigoSegmentacaoOpcao.equals(0)) {
			sql.append(" and segmentacaoopcao.codigo = ").append(codigoSegmentacaoOpcao);
		}
		if(matriculado != null){
			if(matriculado){
				sql.append(" and prospects.pessoa in (select aluno from matricula where aluno = prospects.pessoa ) ");
			}else{
				sql.append(" and prospects.pessoa not in (select aluno from matricula where aluno = prospects.pessoa ) ");
			}
		}
		sql.append(" and prospects.inativo = false  ");
		if (painelGestorTipoMonitoramentoCRMEnum == null || painelGestorTipoMonitoramentoCRMEnum.equals(PainelGestorTipoMonitoramentoCRMEnum.SEGMENTACAO_PROSPECT)) {
			if (unidadeEnsinoVOs != null && !unidadeEnsinoVOs.isEmpty()) {
				sql.append(" and (prospects.unidadeensino in (0 ");
				for (UnidadeEnsinoVO unidadeEnsinoVO : unidadeEnsinoVOs) {
					if (unidadeEnsinoVO.getFiltrarUnidadeEnsino()) {
						sql.append(", ").append(unidadeEnsinoVO.getCodigo());
					}
				}
				sql.append(" )  or  prospects.unidadeensino is  null)");
			}
		} else {
			if (painelGestorTipoMonitoramentoCRMEnum != null) {
				if (unidadeEspecifica == null || unidadeEspecifica == 0) {
					sql.append(" and prospects.unidadeensino is null ");
				} else {
					sql.append(" and prospects.unidadeensino = ").append(unidadeEspecifica);
				}
			}
		}
		if (painelGestorTipoMonitoramentoCRMEnum != null && painelGestorTipoMonitoramentoCRMEnum.equals(PainelGestorTipoMonitoramentoCRMEnum.PROSPECT_POR_CURSO)) {
			if (curso != null && curso > 0) {
				sql.append(" and curso.codigo = ").append(curso);
			} else {
				sql.append(" and curso.codigo is null ");
			}
		}
		if (painelGestorTipoMonitoramentoCRMEnum != null && (painelGestorTipoMonitoramentoCRMEnum.equals(PainelGestorTipoMonitoramentoCRMEnum.PROSPECT_POR_CURSO) || painelGestorTipoMonitoramentoCRMEnum.equals(PainelGestorTipoMonitoramentoCRMEnum.PROSPECT_POR_CONSULTOR))) {
			if (consultor != null && consultor > 0) {
				sql.append(" and prospects.consultorPadrao = ").append(consultor);
			} else {
				sql.append(" and ((prospects.consultorPadrao is null) or (prospects.consultorPadrao = 0)) ");
			}
		}
		if (tipoFiltroMonitamentoCrmProspectEnum != null) {
			if (tipoFiltroMonitamentoCrmProspectEnum.equals(TipoFiltroMonitamentoCrmProspectEnum.TODOS_PROSPECTS_SEM_CONSULTOR)) {
				sql.append(" and ((prospects.consultorPadrao is null) or (prospects.consultorPadrao = 0)) ");
			} else if (tipoFiltroMonitamentoCrmProspectEnum.equals(TipoFiltroMonitamentoCrmProspectEnum.TODOS_PROSPECTS_SEM_CONTATO_PERIODO1)) {

				sql.append(" and prospects.codigo in  ");
				sql.append(" ( select p.codigo from prospects p ");
				sql.append(" left join (select max(data) as dataCompromisso, prospect from ( ");
				sql.append(" select max(dataCompromisso) as data, cpah2.prospect from compromissoagendapessoahorario cpah2  group by cpah2.prospect ");
				sql.append(" union all  ");
				sql.append(" select max(dataregistro) as data, hfu.prospect from historicofollowup hfu  group by hfu.prospect ");
				sql.append(" union all  ");
				sql.append(" select max(interacaoworkflow.datainicio) as data, interacaoworkflow.prospect from interacaoworkflow  group by interacaoworkflow.prospect ");
				sql.append(" ) as dataContato group by prospect) as caph on p.codigo = caph.prospect  ");
				sql.append(" where case when caph.dataCompromisso is null then p.datacadastro::DATE else caph.dataCompromisso::DATE end < current_date ");
				sql.append("  and (  ");
				sql.append(" (DATE_PART('year', current_date) - DATE_PART('year',case when caph.dataCompromisso is null then p.datacadastro::DATE else caph.dataCompromisso::DATE end))  * 12 + (DATE_PART('month', current_date) - DATE_PART('month', case when caph.dataCompromisso is null then p.datacadastro::DATE else caph.dataCompromisso::DATE end)) >=  ").append(periodoUltimoContato1);
				sql.append(" and (DATE_PART('year', current_date) - DATE_PART('year',case when caph.dataCompromisso is null then p.datacadastro::DATE else caph.dataCompromisso::DATE end))  * 12 + (DATE_PART('month', current_date) - DATE_PART('month', case when caph.dataCompromisso is null then p.datacadastro::DATE else caph.dataCompromisso::DATE end))  < ").append(periodoUltimoContato2);
				sql.append(" ) ");
				sql.append(" ) ");
			} else if (tipoFiltroMonitamentoCrmProspectEnum.equals(TipoFiltroMonitamentoCrmProspectEnum.TODOS_PROSPECTS_SEM_CONTATO_PERIODO2)) {
				sql.append(" and prospects.codigo in  ");
				sql.append(" ( select p.codigo from prospects p ");
				sql.append(" left join (select max(data) as dataCompromisso, prospect from ( ");
				sql.append(" select max(dataCompromisso) as data, cpah2.prospect from compromissoagendapessoahorario cpah2  group by cpah2.prospect ");
				sql.append(" union all  ");
				sql.append(" select max(dataregistro) as data, hfu.prospect from historicofollowup hfu  group by hfu.prospect ");
				sql.append(" union all  ");
				sql.append(" select max(interacaoworkflow.datainicio) as data, interacaoworkflow.prospect from interacaoworkflow  group by interacaoworkflow.prospect ");
				sql.append(" ) as dataContato group by prospect) as caph on p.codigo = caph.prospect  ");

				sql.append(" where case when caph.dataCompromisso is null then p.datacadastro::DATE else caph.dataCompromisso::DATE end < current_date ");
				sql.append("  and (  ");
				sql.append(" (DATE_PART('year', current_date) - DATE_PART('year',case when caph.dataCompromisso is null then p.datacadastro::DATE else caph.dataCompromisso::DATE end))  * 12 + (DATE_PART('month', current_date) - DATE_PART('month', case when caph.dataCompromisso is null then p.datacadastro::DATE else caph.dataCompromisso::DATE end)) >=  ").append(periodoUltimoContato2);
				sql.append(" and (DATE_PART('year', current_date) - DATE_PART('year',case when caph.dataCompromisso is null then p.datacadastro::DATE else caph.dataCompromisso::DATE end))  * 12 + (DATE_PART('month', current_date) - DATE_PART('month', case when caph.dataCompromisso is null then p.datacadastro::DATE else caph.dataCompromisso::DATE end))  < ").append(periodoUltimoContato3);
				sql.append(" ) ");
				sql.append(" ) ");
			} else if (tipoFiltroMonitamentoCrmProspectEnum.equals(TipoFiltroMonitamentoCrmProspectEnum.TODOS_PROSPECTS_SEM_CONTATO_PERIODO3)) {
				sql.append(" and prospects.codigo in  ");
				sql.append(" ( select p.codigo from prospects p ");
				sql.append(" left join (select max(data) as dataCompromisso, prospect from ( ");
				sql.append(" select max(dataCompromisso) as data, cpah2.prospect from compromissoagendapessoahorario cpah2  group by cpah2.prospect ");
				sql.append(" union all  ");
				sql.append(" select max(dataregistro) as data, hfu.prospect from historicofollowup hfu  group by hfu.prospect ");
				sql.append(" union all  ");
				sql.append(" select max(interacaoworkflow.datainicio) as data, interacaoworkflow.prospect from interacaoworkflow  group by interacaoworkflow.prospect ");
				sql.append(" ) as dataContato group by prospect) as caph on p.codigo = caph.prospect  ");
				sql.append(" where case when caph.dataCompromisso is null then p.datacadastro::DATE else caph.dataCompromisso::DATE end < current_date ");
				sql.append("  and (  ");
				sql.append(" (DATE_PART('year', current_date) - DATE_PART('year',case when caph.dataCompromisso is null then p.datacadastro::DATE else caph.dataCompromisso::DATE end))  * 12 + (DATE_PART('month', current_date) - DATE_PART('month', case when caph.dataCompromisso is null then p.datacadastro::DATE else caph.dataCompromisso::DATE end)) >=  ").append(periodoUltimoContato3);
				sql.append(" ) ");
				sql.append(" ) ");
			} else if (tipoFiltroMonitamentoCrmProspectEnum.equals(TipoFiltroMonitamentoCrmProspectEnum.MES_ANO_ABORDADO)) {
				sql.append(" and prospects.codigo in  ");
				sql.append(" ( select p.codigo from prospects p ");
				sql.append(" inner join compromissoagendapessoahorario caph on p.codigo = caph.prospect ");
				sql.append(" where ");
				sql.append(" DATE_PART('month', caph.dataCompromisso::DATE) = ").append(Integer.valueOf(mesAno.getKey()));
				sql.append(" and DATE_PART('year', caph.dataCompromisso::DATE) =   ").append(ano);
				sql.append(" )");
			} else if (tipoFiltroMonitamentoCrmProspectEnum.equals(TipoFiltroMonitamentoCrmProspectEnum.MES_ANO_FINALIZADO_SUCESSO)) {
				sql.append(" and prospects.codigo in  ");
				sql.append(" ( select p.codigo from prospects p ");
				sql.append(" inner join compromissoagendapessoahorario caph on p.codigo = caph.prospect ");
				sql.append(" inner join etapaworkflow on etapaworkflow.codigo = caph.etapaworkflow ");
				sql.append(" inner join situacaoprospectpipeline   on situacaoprospectpipeline.codigo = etapaworkflow.situacaodefinirprospectfinal ");
				sql.append(" where ");
				sql.append(" DATE_PART('month', caph.dataCompromisso::DATE) = ").append(Integer.valueOf(mesAno.getKey()));
				sql.append(" and DATE_PART('year', caph.dataCompromisso::DATE) =   ").append(ano);
				sql.append(" and situacaoprospectpipeline.controle = 'FINALIZADO_SUCESSO')");
			} else if (tipoFiltroMonitamentoCrmProspectEnum.equals(TipoFiltroMonitamentoCrmProspectEnum.MES_ANO_MATRICULADO)) {
				sql.append(" and prospects.codigo in  ");
				sql.append(" ( select distinct p.codigo from prospects p ");
				sql.append(" inner join compromissoagendapessoahorario caph on p.codigo = caph.prospect ");
				sql.append(" inner join matricula on matricula.aluno = p.pessoa ");
				sql.append(" where ");
				sql.append(" extract('month' from caph.dataCompromisso::DATE) = ").append(Integer.valueOf(mesAno.getKey()));
				sql.append(" and extract('year' from caph.dataCompromisso::DATE) =   ").append(ano);
				sql.append(" )");
			} else if (tipoFiltroMonitamentoCrmProspectEnum.equals(TipoFiltroMonitamentoCrmProspectEnum.MES_ANO_REAGENDADO)) {
				sql.append(" and prospects.codigo in  ");
				sql.append(" ( select p.codigo from prospects p ");
				sql.append(" inner join compromissoagendapessoahorario caph on p.codigo = caph.prospect ");
				sql.append(" where ");
				sql.append(" DATE_PART('month', caph.dataCompromisso::DATE) = ").append(Integer.valueOf(mesAno.getKey()));
				sql.append(" and DATE_PART('year', caph.dataCompromisso::DATE) =   ").append(ano);
				sql.append(" and caph.tiposituacaocompromissoenum = 'REALIZADO_COM_REMARCACAO' ");
				sql.append(" )");
			} else if (tipoFiltroMonitamentoCrmProspectEnum.equals(TipoFiltroMonitamentoCrmProspectEnum.TODOS_PROSPECTS_COM_CONSULTOR)) {
				sql.append(" and prospects.consultorPadrao is not null ");
			} else if (tipoFiltroMonitamentoCrmProspectEnum.equals(TipoFiltroMonitamentoCrmProspectEnum.TODOS_PROSPECTS_COM_AGENDA)) {
				sql.append(" and prospects.codigo in ");
				sql.append(" (select prospect from compromissoagendapessoahorario caph where prospects.codigo = caph.prospect ");
				sql.append(" and caph.datacompromisso  >= '").append(Uteis.getDataJDBC(dataInicio)).append("' and caph.datacompromisso  <= '").append(Uteis.getDataJDBC(dataTermino)).append("') ");
			} else if (tipoFiltroMonitamentoCrmProspectEnum.equals(TipoFiltroMonitamentoCrmProspectEnum.TODOS_PROSPECTS_SEM_AGENDA)) {
				sql.append(" and prospects.codigo not in ");
				sql.append(" (select prospect from compromissoagendapessoahorario caph where prospects.codigo = caph.prospect ");
				sql.append(" and caph.datacompromisso  >= '").append(Uteis.getDataJDBC(dataInicio)).append("' and caph.datacompromisso  <= '").append(Uteis.getDataJDBC(dataTermino)).append("') ");
			}
		}

		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (rs.next()) {
			return rs.getInt("qtde");
		}
		return 0;
	}

	@Override
	public void realizarAtualizacaorDadosDetalhePainelGestorMonitoramentoCRM(List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, PainelGestorMonitoramentoCRMVO painelGestorMonitoramentoCRMVO, PainelGestorMonitoramentoDetalheCRMVO painelGestorMonitoramentoDetalheCRMVO) throws Exception {
		if (painelGestorMonitoramentoDetalheCRMVO.getPainelGestorTipoMonitoramentoCRMEnum().equals(PainelGestorTipoMonitoramentoCRMEnum.PRE_INSCRICAO_DETALHE_CURSO) || painelGestorMonitoramentoDetalheCRMVO.getPainelGestorTipoMonitoramentoCRMEnum().equals(PainelGestorTipoMonitoramentoCRMEnum.PRE_INSCRICAO_DETALHE_CURSO_CONSULTOR)) {
			montarDadosGeraisPreInscricao(painelGestorMonitoramentoCRMVO, consultarDadosPreInscicao(unidadeEnsinoVOs, dataInicio, dataTermino, true, false, 0, painelGestorMonitoramentoDetalheCRMVO.getConsultor().getCodigo()), true, false, painelGestorMonitoramentoDetalheCRMVO.getPainelGestorTipoMonitoramentoCRMEnum());
		}
		if (painelGestorMonitoramentoDetalheCRMVO.getPainelGestorTipoMonitoramentoCRMEnum().equals(PainelGestorTipoMonitoramentoCRMEnum.PRE_INSCRICAO_DETALHE_CONSULTOR) || painelGestorMonitoramentoDetalheCRMVO.getPainelGestorTipoMonitoramentoCRMEnum().equals(PainelGestorTipoMonitoramentoCRMEnum.PRE_INSCRICAO_DETALHE_CONSULTOR_CURSO)) {
			montarDadosGeraisPreInscricao(painelGestorMonitoramentoCRMVO, consultarDadosPreInscicao(unidadeEnsinoVOs, dataInicio, dataTermino, false, true, painelGestorMonitoramentoDetalheCRMVO.getCurso().getCodigo(), 0), false, true, painelGestorMonitoramentoDetalheCRMVO.getPainelGestorTipoMonitoramentoCRMEnum());
		}
		if (painelGestorMonitoramentoDetalheCRMVO.getPainelGestorTipoMonitoramentoCRMEnum().equals(PainelGestorTipoMonitoramentoCRMEnum.PRE_INSCRICAO)) {
			if (painelGestorMonitoramentoCRMVO.getApresentarDadosConsultor() && painelGestorMonitoramentoCRMVO.getCurso().getNome().trim().isEmpty()) {
				montarDadosGeraisPreInscricao(painelGestorMonitoramentoCRMVO, consultarDadosPreInscicao(unidadeEnsinoVOs, dataInicio, dataTermino, false, true, 0, 0), false, true, PainelGestorTipoMonitoramentoCRMEnum.PRE_INSCRICAO_DETALHE_CONSULTOR);
			}
			if (painelGestorMonitoramentoCRMVO.getApresentarDadosCurso() && painelGestorMonitoramentoCRMVO.getConsultor().getPessoa().getNome().trim().isEmpty()) {
				montarDadosGeraisPreInscricao(painelGestorMonitoramentoCRMVO, consultarDadosPreInscicao(unidadeEnsinoVOs, dataInicio, dataTermino, true, false, 0, 0), true, false, PainelGestorTipoMonitoramentoCRMEnum.PRE_INSCRICAO_DETALHE_CURSO);
			}
		}

	}

	@Override
	public void consultarDadosDetalhePainelGestorMonitoramentoCRMPorConsultor(List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, PainelGestorMonitoramentoCRMVO painelGestorMonitoramentoCRMVO, PainelGestorMonitoramentoDetalheCRMVO painelGestorMonitoramentoDetalheCRMVO) throws Exception {
		if (painelGestorMonitoramentoDetalheCRMVO.getPainelGestorTipoMonitoramentoCRMEnum().equals(PainelGestorTipoMonitoramentoCRMEnum.PRE_INSCRICAO)) {
			montarDadosGeraisPreInscricao(painelGestorMonitoramentoCRMVO, consultarDadosPreInscicao(unidadeEnsinoVOs, dataInicio, dataTermino, false, true, 0, 0), false, true, PainelGestorTipoMonitoramentoCRMEnum.PRE_INSCRICAO_DETALHE_CONSULTOR);
		}
		if (painelGestorMonitoramentoDetalheCRMVO.getPainelGestorTipoMonitoramentoCRMEnum().equals(PainelGestorTipoMonitoramentoCRMEnum.PRE_INSCRICAO_DETALHE_CURSO)) {
			montarDadosGeraisPreInscricao(painelGestorMonitoramentoCRMVO, consultarDadosPreInscicao(unidadeEnsinoVOs, dataInicio, dataTermino, false, true, painelGestorMonitoramentoDetalheCRMVO.getCurso().getCodigo(), 0), false, true, PainelGestorTipoMonitoramentoCRMEnum.PRE_INSCRICAO_DETALHE_CURSO_CONSULTOR);			
		}
		if (painelGestorMonitoramentoDetalheCRMVO.getPainelGestorTipoMonitoramentoCRMEnum().equals(PainelGestorTipoMonitoramentoCRMEnum.PRE_INSCRICAO_DETALHE_CONSULTOR_CURSO)){
			montarDadosGeraisPreInscricao(painelGestorMonitoramentoCRMVO, consultarDadosPreInscicao(unidadeEnsinoVOs, dataInicio, dataTermino, false, true, painelGestorMonitoramentoDetalheCRMVO.getCurso().getCodigo(), 0), false, true, PainelGestorTipoMonitoramentoCRMEnum.PRE_INSCRICAO_DETALHE_CURSO_CONSULTOR);
		}
		if (painelGestorMonitoramentoDetalheCRMVO.getPainelGestorTipoMonitoramentoCRMEnum().equals(PainelGestorTipoMonitoramentoCRMEnum.PRE_INSCRICAO_DETALHE_CURSO_CONSULTOR)){
			montarDadosGeraisPreInscricao(painelGestorMonitoramentoCRMVO, consultarDadosPreInscicao(unidadeEnsinoVOs, dataInicio, dataTermino, false, true, painelGestorMonitoramentoDetalheCRMVO.getCurso().getCodigo(), 0), false, true, PainelGestorTipoMonitoramentoCRMEnum.PRE_INSCRICAO_DETALHE_CONSULTOR_CURSO);
		}
	}

	public  Map<String, Object> montarHTMLRelatorio(TreeMap<TurmaVO, TreeMap<FuncionarioVO, Integer>> mapaTurma) {
	    Map<String, Object> mapRetorno = new HashMap<String, Object>();
	    if (mapaTurma.isEmpty()) {
			StringBuilder string = new StringBuilder("Não há resultados a serem exibidos!");
			mapRetorno.put("semRegistros", string);
			return mapRetorno;
		}
		TreeMap<String, Integer> mapTotalCons = new TreeMap<String, Integer>();
		StringBuilder sb = new StringBuilder("<HTML>");
		sb.append("<BODY>");
		sb.append("<style type=\"text/css\"> table.bordasimples {border-collapse: collapse;} table.bordasimples tr {		font-size: 10pt;    font-family: 'Trebuchet MS', verdana;	    padding: 15px 0;    border:1px solid #ddd;    vertical-align: top;    background: url('../V2/imagens/degrade1.png') repeat-x #edefef;} td:nth-child(odd) {background: #FFF}  </style>");
		sb.append("<BR><TABLE BORDER=1 class=\"bordasimples\">");
		sb.append("<TR class=\"headerHtml\">");
		sb.append("<TD></TD>");
		final Collator coll = Collator.getInstance(new Locale("pt", "BR"));
		SortedSet<TurmaVO> keys = new TreeSet<TurmaVO>(new Comparator<TurmaVO>() {
			@Override
			public int compare(TurmaVO o1, TurmaVO o2) {
				// TODO Auto-generated method stub
				return coll.compare(o1.getIdentificadorTurma(), o2.getIdentificadorTurma());
			}
		});		
		keys.addAll(mapaTurma.keySet());
		SortedSet<FuncionarioVO> listFuncionario= new TreeSet<FuncionarioVO>(new Comparator<FuncionarioVO>() {
			@Override
			public int compare(FuncionarioVO o1, FuncionarioVO o2) {
				// TODO Auto-generated method stub
				return coll.compare(o1.getPessoa().getNome(), o2.getPessoa().getNome());
			}
		});
		for (TurmaVO turma : keys) {
			
			TreeMap<FuncionarioVO, Integer> mapaConsultor = (TreeMap<FuncionarioVO, Integer>)mapaTurma.get(turma);
			List<FuncionarioVO> chaveConsultor = new ArrayList<FuncionarioVO>(0);
			/*SortedSet<FuncionarioVO> chaveConsultor = new TreeSet<FuncionarioVO>(new Comparator<FuncionarioVO>() {
				@Override
				public int compare(FuncionarioVO o1, FuncionarioVO o2) {
					// TODO Auto-generated method stub
					return coll.compare(o1.getPessoa().getNome(), o2.getPessoa().getNome());
				}
			});*/
			
			
			chaveConsultor.addAll(mapaConsultor.keySet());
			for (FuncionarioVO funcionarioVO : chaveConsultor) {
				if (Uteis.isAtributoPreenchido(funcionarioVO)) {
					if(!listFuncionario.contains(funcionarioVO)) {
						listFuncionario.add(funcionarioVO);
					}				
				}
			}
		
		}
		
		for(FuncionarioVO funcionarioVO : listFuncionario) {
			sb.append("<TD> ").append(funcionarioVO.getPessoa().getNome()).append(" </TD>");
		}
		
		
		
		sb.append("<TD> TOTAL TURMA </TD>");
		sb.append("</TR>");
		
		Integer totalGeralTurma = 0;
		for (TurmaVO turmaVO : keys) {
			sb.append("<TR>");
			Integer totalTurma = 0;
			sb.append("<TD> ").append(turmaVO.getIdentificadorTurma()).append(" </TD>");			
			TreeMap<FuncionarioVO, Integer> mapaConsultor = (TreeMap<FuncionarioVO, Integer>)mapaTurma.get(turmaVO);
			
			SortedSet<FuncionarioVO> chaveConsultor = new TreeSet<FuncionarioVO>(new Comparator<FuncionarioVO>() {
				@Override
				public int compare(FuncionarioVO o1, FuncionarioVO o2) {
					// TODO Auto-generated method stub
					return coll.compare(o1.getPessoa().getNome(), o2.getPessoa().getNome());
				}
			});
			chaveConsultor.addAll(mapaConsultor.keySet());
			
				for(FuncionarioVO funcionarioVO :listFuncionario) {
					if(chaveConsultor.contains(funcionarioVO)) {
						sb.append("<TD><CENTER> ").append(mapaConsultor.get(funcionarioVO)).append(" </CENTER></TD>");
						totalTurma = totalTurma + (Integer)mapaConsultor.get(funcionarioVO);
						totalGeralTurma = totalGeralTurma + (Integer)mapaConsultor.get(funcionarioVO);
						if (mapTotalCons.containsKey(funcionarioVO.getPessoa().getNome())) {
							mapTotalCons.put(funcionarioVO.getPessoa().getNome(), (Integer)mapTotalCons.get(funcionarioVO.getPessoa().getNome()) + (Integer)mapaConsultor.get(funcionarioVO));
						} else {
							mapTotalCons.put(funcionarioVO.getPessoa().getNome(), (Integer)mapaConsultor.get(funcionarioVO));
						}
					}
					else {
						sb.append("<TD><CENTER> ").append(0).append(" </CENTER></TD>");
					}
				}

			sb.append("<TD><CENTER> ").append(totalTurma.toString()).append(" </CENTER></TD>");
			//mapTotalCons.put("TOTAL", totalTurma);
//			for (FuncionarioVO funcionarioVO : chaveConsultor) {
//				if (funcionarioVO.getPessoa().getNome().equals("TOTAL")) {
//					sb.append("<TD> ").append(totalTurma.toString()).append(" </TD>");
//					if (mapTotalCons.containsKey("TOTAL")) {
//						mapTotalCons.put("TOTAL", (Integer) mapTotalCons.get("TOTAL") + totalTurma);
//					} else {
//						mapTotalCons.put("TOTAL", totalTurma);
//					}
//				}
//			}
			sb.append("</TR>");
		}
		sb.append("<TR>");
		sb.append("<TD> TOTAL P/ CONS. </TD>");
		SortedSet<String> keyTotalCons = new TreeSet<String>(mapTotalCons.keySet());
		for(FuncionarioVO funcionarioVO :listFuncionario) {
			if(keyTotalCons.contains(funcionarioVO.getPessoa().getNome())) {				
				if(!funcionarioVO.getPessoa().getNome().equals("TOTAL")){
					sb.append("<TD><CENTER> ").append(mapTotalCons.get(funcionarioVO.getPessoa().getNome())).append(" </CENTER></TD>");
				}
			}
		}
		/*for (String chave : keyTotalCons) {
			if(!chave.equals("TOTAL")){
				sb.append("<TD><CENTER> ").append(mapTotalCons.get(chave)).append(" </CENTER></TD>");
			}
		}*/

			sb.append("<TD><CENTER> ").append(totalGeralTurma).append(" </CENTER></TD>");
		
		sb.append("</TR>");
		sb.append("</TABLE>");
		sb.append("</BODY>");
		sb.append("</HTML>");

		mapRetorno.put("html", sb.toString());

		//Poputal o a CrossTable para geração do relatorio
		Integer ordemLinha = 2;
		List<CrosstabVO> crosstabVOs = new ArrayList<CrosstabVO>(0);
		for (TurmaVO turmaVO : keys) {
			Integer totalTurma = 0;
			Integer cont = 1;
			
			TreeMap<FuncionarioVO, Integer> mapaConsultor = (TreeMap<FuncionarioVO, Integer>)mapaTurma.get(turmaVO);
			
			List<FuncionarioVO> chaveConsultor = new ArrayList<FuncionarioVO>(0);
			
			/*SortedSet<FuncionarioVO> chaveConsultor = new TreeSet<FuncionarioVO>(new Comparator<FuncionarioVO>() {
				@Override
				public int compare(FuncionarioVO o1, FuncionarioVO o2) {
					// TODO Auto-generated method stub
					return coll.compare(o1.getPessoa().getNome(), o2.getPessoa().getNome());
				}
			});*/
			
			chaveConsultor.addAll(mapaConsultor.keySet());
			
			for(FuncionarioVO funcionarioVO :listFuncionario) {
				CrosstabVO crosstabVO = new CrosstabVO();
				int ordemColuna = 2;
				crosstabVO.setOrdemLinha(ordemLinha);
				crosstabVO.setLabelLinha(turmaVO.getIdentificadorTurma());
				crosstabVO.setLabelColuna(funcionarioVO.getPessoa().getNome());
				crosstabVO.setOrdemColuna(cont + ordemColuna);				
				if(chaveConsultor.contains(funcionarioVO)) {					
					crosstabVO.setValorString(mapaConsultor.get(funcionarioVO).toString());		
					totalTurma = totalTurma + (Integer)mapaConsultor.get(funcionarioVO);
				}else {
					crosstabVO.setValorString("0");					
				}
				
				
				crosstabVOs.add(crosstabVO);
				cont++;
				
			}

			CrosstabVO crosstabVO2 = new CrosstabVO();
			crosstabVO2.setOrdemLinha(ordemLinha);
			crosstabVO2.setLabelLinha(turmaVO.getIdentificadorTurma());
			crosstabVO2.setLabelColuna("Total Por Turma");
			crosstabVO2.setOrdemColuna(cont+2);
			crosstabVO2.setValorString(totalTurma.toString());
			
			crosstabVOs.add(crosstabVO2);
		}
		
		
		SortedSet<String> keyTotalCons2 = new TreeSet<String>(mapTotalCons.keySet());//mapa com todos os consultores, e total para ter o valor de cada um no rodape
		Integer cont = 1;
		
		for(FuncionarioVO funcionarioVO :listFuncionario) {
			if(keyTotalCons2.contains(funcionarioVO.getPessoa().getNome())) {
				if(!funcionarioVO.getPessoa().getNome().equals("TOTAL")){
					int ordemColuna = 2;
					CrosstabVO crosstabVO = new CrosstabVO();
					crosstabVO.setOrdemLinha(keyTotalCons2.size()+2);
					crosstabVO.setLabelLinha("Total por Consultor");
					
					crosstabVO.setLabelColuna(funcionarioVO.getPessoa().getNome());
					crosstabVO.setOrdemColuna(cont + ordemColuna);
					crosstabVO.setValorString(mapTotalCons.get(funcionarioVO.getPessoa().getNome()).toString());
					
					crosstabVOs.add(crosstabVO);
					
					cont++;
				}else{
					CrosstabVO crosstabVO = new CrosstabVO();
					crosstabVO.setOrdemLinha(keyTotalCons2.size()+2);
					crosstabVO.setLabelLinha("Total por Consultor");
					
					crosstabVO.setLabelColuna("Total Por Turma");
					crosstabVO.setOrdemColuna(cont+2);
					crosstabVO.setValorString(mapTotalCons.get(funcionarioVO.getPessoa().getNome()).toString());
					
					crosstabVOs.add(crosstabVO);
				}
			}
		}
		/*for (String chave : keyTotalCons2) {// nome do consultor "chave"
			if(!chave.equals("TOTAL")){
				int ordemColuna = 2;
				CrosstabVO crosstabVO = new CrosstabVO();
				crosstabVO.setOrdemLinha(keyTotalCons2.size()+2);
				crosstabVO.setLabelLinha("Total por Consultor");
				
				crosstabVO.setLabelColuna(chave);
				crosstabVO.setOrdemColuna(cont + ordemColuna);
				crosstabVO.setValorString(mapTotalCons.get(chave).toString());
				
				crosstabVOs.add(crosstabVO);
				
				cont++;
			}else{
				CrosstabVO crosstabVO = new CrosstabVO();
				crosstabVO.setOrdemLinha(keyTotalCons2.size()+2);
				crosstabVO.setLabelLinha("Total por Consultor");
				
				crosstabVO.setLabelColuna("Total Por Turma");
				crosstabVO.setOrdemColuna(cont+2);
				crosstabVO.setValorString(mapTotalCons.get(chave).toString());
				
				crosstabVOs.add(crosstabVO);
			}
		}*/
		
		CrosstabVO crosstabVO = new CrosstabVO();
		crosstabVO.setOrdemLinha(keyTotalCons2.size()+2);
		crosstabVO.setLabelLinha("Total por Consultor");		
		crosstabVO.setLabelColuna("Total Por Turma");
		crosstabVO.setOrdemColuna(cont+2);
		crosstabVO.setValorString(totalGeralTurma.toString());		
		crosstabVOs.add(crosstabVO);
		mapRetorno.put("crossTable", crosstabVOs);
		return mapRetorno;
	}

	public List<ConsultorPorMatriculaRelVO> montarListagemRelatorioAnalitico(TreeMap<TurmaVO, TreeMap<FuncionarioVO, Integer>> mapaTurma, List<ConsultorPorMatriculaRelVO> listaFinal, Date dataInicio, Date dataTermino, 
			boolean matRecebida, boolean matAReceber, boolean matVencida, boolean matAVencer, boolean excluida, boolean transferidaDe, boolean transferidaPara, String situacaoProspect, boolean considerarConsultorVinculadoProspect, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, String situacaoAlunoCurso, Date dataInicioPagamentoMatricula, Date dataFimPagamentoMatricula, Boolean considerarApenasTurmaComAlunosMatriculados, boolean matNaoGerada, Boolean trazerAlunosBolsistas) throws Exception {
		if (mapaTurma.isEmpty()) {
			return new ArrayList<ConsultorPorMatriculaRelVO>();
		}
		SortedSet<TurmaVO> keys = new TreeSet<TurmaVO>(mapaTurma.keySet()); // essas sao as  turmas no mapa "keys" 
		for (TurmaVO turma : keys) {
			ConsultorPorMatriculaRelVO obj = new ConsultorPorMatriculaRelVO();
			obj.setTurma(turma.getIdentificadorTurma());
			TreeMap<FuncionarioVO, Integer> mapaConsultor = mapaTurma.get(turma); // esses sao os consultores no mapa "mapaConsultor"  (nome/total(por turma))
			SortedSet<FuncionarioVO> chaveConsultor = new TreeSet<FuncionarioVO>(mapaConsultor.keySet());// apenas os nomes dos consultores no mapa  "chaveConsultor"
			obj.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(turma.getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null).getNome());
			for (FuncionarioVO func : chaveConsultor) {
				if (Uteis.isAtributoPreenchido(func)) {
					func.setQtdeAlunoVinculadosConsultor(mapaConsultor.get(func));					
					func.getMatriculaConsultorVO().addAll(this.consultarMatConsultoresQuePossuemOuNaoMatriculaVinculada(turma.getCodigo(), func.getCodigo(), dataInicio, dataTermino, matRecebida, matAReceber, matVencida, matAVencer, excluida, transferidaDe, transferidaPara, situacaoProspect, considerarConsultorVinculadoProspect, filtroRelatorioAcademicoVO, situacaoAlunoCurso, dataInicioPagamentoMatricula, dataFimPagamentoMatricula, considerarApenasTurmaComAlunosMatriculados, matNaoGerada, trazerAlunosBolsistas));
					obj.getConsultor().add(func);
				}
			}
			if(!obj.getConsultor().isEmpty()) {
				listaFinal.add(obj);
			}
			
		}		
		return listaFinal;
	}
	
	public Map<String, Object> consultarDadosPainelGestorMonitoramentoConsultorPorMatricula(List<UnidadeEnsinoVO> unidadeEnsinoVOs, List<CursoVO> cursoVOs, Date dataInicio, Date dataTermino, List<TurmaVO> turmaVOs, FuncionarioVO consultor, 
			boolean matRecebida, boolean matAReceber, boolean matVencida, boolean matAVencer, boolean excluida, boolean transferidaDe, boolean transferidaPara, String situacaoProspect, boolean considerarConsultorVinculadoProspect, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, String situacaoAlunoCurso, List<ConsultorPorMatriculaRelVO> listaRelatorioAnalitico, Date dataInicioPagamentoMatricula, Date dataFimPagamentoMatricula, Boolean considerarApenasTurmaComAlunosMatriculados, Boolean trazerAlunosSemTutor, boolean matNaoGerada, Boolean trazerAlunosBolsistas) throws Exception {
		
		StringBuilder filtroCursoIN = new StringBuilder("");
		StringBuilder filtroUnidadeIN = new StringBuilder("");
		StringBuilder filtroTurmaIN = new StringBuilder("");
	
			if (Uteis.isAtributoPreenchido(cursoVOs)) {
				for (CursoVO cursoVO : cursoVOs) {
					if (cursoVO.getFiltrarCursoVO()) {
						filtroCursoIN.append(filtroCursoIN.toString().isEmpty() ? "" : ", ").append(cursoVO.getCodigo());
					}
				}
			}

			if (Uteis.isAtributoPreenchido(unidadeEnsinoVOs)) {
				for (UnidadeEnsinoVO unidadeEnsinoVO : unidadeEnsinoVOs) {
					if (unidadeEnsinoVO.getFiltrarUnidadeEnsino()) {
						filtroUnidadeIN.append(filtroUnidadeIN.toString().isEmpty() ? "" : ", ").append(unidadeEnsinoVO.getCodigo());
					}
				}
			}
			
			if (Uteis.isAtributoPreenchido(turmaVOs)) {
				for (TurmaVO turmaVO : turmaVOs) {
					if (turmaVO.getFiltrarTurmaVO()) {
						filtroTurmaIN.append(filtroTurmaIN.toString().isEmpty() ? "" : ", ").append(turmaVO.getCodigo());
					}
				}
			}
		
		StringBuilder sb = new StringBuilder("select distinct * from (");
		
			sb.append((sqlConsultorMatriculaTurmaMatricula(filtroUnidadeIN, filtroCursoIN, dataInicio, dataTermino, filtroTurmaIN, consultor, matRecebida, matAReceber, matVencida, matAVencer, excluida, transferidaDe, transferidaPara, situacaoProspect, considerarConsultorVinculadoProspect, filtroRelatorioAcademicoVO,situacaoAlunoCurso, dataInicioPagamentoMatricula, dataFimPagamentoMatricula, considerarApenasTurmaComAlunosMatriculados, matNaoGerada, trazerAlunosBolsistas)).toString());
			sb.append(" union all ");
		if (excluida) {
			sb.append((sqlConsultorMatriculaTurmaMatExcluida(filtroUnidadeIN, filtroCursoIN,dataInicio, dataTermino, filtroTurmaIN, consultor, matRecebida, matAReceber, matVencida, matAVencer, excluida, transferidaDe, transferidaPara, situacaoProspect, matNaoGerada, trazerAlunosBolsistas)).toString());
			sb.append(" union all ");
		}
		if (transferidaDe) {
			sb.append((sqlConsultorMatriculaTurmaMatTransferidaDe(filtroUnidadeIN, filtroCursoIN, dataInicio, dataTermino, filtroTurmaIN, consultor, matRecebida, matAReceber, matVencida, matAVencer, excluida, transferidaDe, transferidaPara, situacaoProspect, considerarConsultorVinculadoProspect, dataInicioPagamentoMatricula, dataFimPagamentoMatricula, considerarApenasTurmaComAlunosMatriculados, matNaoGerada, trazerAlunosBolsistas)).toString());
			sb.append(" union all ");
		}
		if (transferidaPara) {
			sb.append((sqlConsultorMatriculaTurmaMatTransferidaPara(filtroUnidadeIN, filtroCursoIN, dataInicio, dataTermino, filtroTurmaIN, consultor, matRecebida, matAReceber, matVencida, matAVencer, excluida, transferidaDe, transferidaPara, situacaoProspect, considerarConsultorVinculadoProspect,  dataInicioPagamentoMatricula, dataFimPagamentoMatricula, considerarApenasTurmaComAlunosMatriculados, matNaoGerada, trazerAlunosBolsistas)).toString());
			sb.append(" union all ");
		}
		sb.append("(select 0, '', 0 from matricula limit 1) ");
		sb.append(") as t where t.turma <> 0 order by t.identificadorturma ");
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		TreeMap<TurmaVO, TreeMap<FuncionarioVO, Integer>> mapTurma = new TreeMap<TurmaVO, TreeMap<FuncionarioVO, Integer>>();
		while (tabelaResultado.next()) {
			TurmaVO turmaVO =  new TurmaVO();
			turmaVO.setCodigo(tabelaResultado.getInt("turma"));
			turmaVO.setIdentificadorTurma(tabelaResultado.getString("identificadorturma"));
			turmaVO.getUnidadeEnsino().setCodigo(tabelaResultado.getInt("unidadeensino"));
			mapTurma.put(turmaVO, consultarConsultoresQuePossuemOuNaoMatriculaVinculada(tabelaResultado.getInt("turma"), filtroUnidadeIN, filtroCursoIN, dataInicio, dataTermino, turmaVO, consultor, 
					matRecebida, matAReceber, matVencida, matAVencer, excluida, transferidaDe, transferidaPara, situacaoProspect, considerarConsultorVinculadoProspect,filtroRelatorioAcademicoVO,situacaoAlunoCurso, dataInicioPagamentoMatricula, dataFimPagamentoMatricula, considerarApenasTurmaComAlunosMatriculados, trazerAlunosSemTutor, matNaoGerada, trazerAlunosBolsistas) );
		}
		montarListagemRelatorioAnalitico(mapTurma, listaRelatorioAnalitico, dataInicio, dataTermino,  
				matRecebida, matAReceber, matVencida, matAVencer, excluida, transferidaDe, transferidaPara, situacaoProspect, considerarConsultorVinculadoProspect,filtroRelatorioAcademicoVO,situacaoAlunoCurso, dataInicioPagamentoMatricula, dataFimPagamentoMatricula, considerarApenasTurmaComAlunosMatriculados, matNaoGerada, trazerAlunosBolsistas);
		return montarHTMLRelatorio(mapTurma);
	}
	
	public TreeMap<FuncionarioVO, Integer> consultarConsultoresQuePossuemOuNaoMatriculaVinculada(Integer turmaProcessar, StringBuilder filtroUnidadeIN, StringBuilder filtroCursoIN, Date dataInicio, Date dataTermino, TurmaVO turma, FuncionarioVO consultor, 
			boolean matRecebida, boolean matAReceber, boolean matVencida, boolean matAVencer, boolean excluida, boolean transferidaDe, boolean transferidaPara, String situacaoProspect, boolean considerarConsultorVinculadoProspect, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, String situacaoAlunoCurso, Date dataInicioPagamentoMatricula, Date dataFimPagamentoMatricula, Boolean considerarApenasTurmaComAlunosMatriculados, Boolean trazerAlunosSemTutor, boolean matNaoGerada, Boolean trazerAlunosBolsistas) throws Exception {
		StringBuilder sb = new StringBuilder("select distinct * from (");
		
			sb.append((sqlConsultarMatriculaConsultorMatricula(turmaProcessar, filtroUnidadeIN, filtroCursoIN, dataInicio, dataTermino, turma, consultor, matRecebida, matAReceber, matVencida, matAVencer, excluida, transferidaDe, transferidaPara, situacaoProspect, considerarConsultorVinculadoProspect, filtroRelatorioAcademicoVO,situacaoAlunoCurso, dataInicioPagamentoMatricula, dataFimPagamentoMatricula, considerarApenasTurmaComAlunosMatriculados, matNaoGerada, trazerAlunosBolsistas)).toString());
			sb.append(" union all ");
		
//		if (preMatricula) {
//			sb.append((sqlConsultarMatriculaConsultorMatPreMatricula(turmaProcessar, unidadeEnsino, dataInicio, dataTermino, curso, turma, consultor, matRecebida, matAReceber, matVencida, matAVencer, ativa, preMatricula, cancelada, excluida, transferidaDe, transferidaPara, situacaoProspect)).toString());
//			sb.append(" union all ");
//		}
//		if (cancelada) {
//			sb.append((sqlConsultarMatriculaConsultorMatCancelada(turmaProcessar, unidadeEnsino, dataInicio, dataTermino, curso, turma, consultor, matRecebida, matAReceber, matVencida, matAVencer, ativa, preMatricula, cancelada, excluida, transferidaDe, transferidaPara, situacaoProspect)).toString());
//			sb.append(" union all ");
//		}
		if (excluida) {
			sb.append((sqlConsultarMatriculaConsultorMatExcluida(turmaProcessar, filtroUnidadeIN, filtroCursoIN, dataInicio, dataTermino,  turma, consultor, matRecebida, matAReceber, matVencida, matAVencer, excluida, transferidaDe, transferidaPara, situacaoProspect, matNaoGerada, trazerAlunosBolsistas)).toString());
			sb.append(" union all ");
		}
		if (transferidaDe) {
			sb.append((sqlConsultarMatriculaConsultorMatTranferidaDe(turmaProcessar, filtroUnidadeIN, filtroCursoIN, dataInicio, dataTermino, turma, consultor, matRecebida, matAReceber, matVencida, matAVencer, excluida, transferidaDe, transferidaPara, situacaoProspect, considerarConsultorVinculadoProspect, dataInicioPagamentoMatricula, dataFimPagamentoMatricula, considerarApenasTurmaComAlunosMatriculados, matNaoGerada, trazerAlunosBolsistas)).toString());
			sb.append(" union all ");
		}
		if (transferidaPara) {
			sb.append((sqlConsultarMatriculaConsultorMatTranferidaPara(turmaProcessar, filtroUnidadeIN, filtroCursoIN, dataInicio, dataTermino,  turma, consultor, matRecebida, matAReceber, matVencida, matAVencer, excluida, transferidaDe, transferidaPara, situacaoProspect, considerarConsultorVinculadoProspect, dataInicioPagamentoMatricula, dataFimPagamentoMatricula, considerarApenasTurmaComAlunosMatriculados, matNaoGerada, trazerAlunosBolsistas)).toString());
			sb.append(" union all ");
		}
//		if (matRecebida) {
//			sb.append((sqlConsultarMatriculaConsultorMatRecebida(turmaProcessar, unidadeEnsino, dataInicio, dataTermino, curso, turma, consultor, matRecebida, matAReceber, matVencida, matAVencer, ativa, preMatricula, cancelada, excluida, transferidaDe, transferidaPara, situacaoProspect)).toString());
//			sb.append(" union all ");
//		}
//		if (matAReceber) {
//			sb.append((sqlConsultarMatriculaConsultorMatAReceber(turmaProcessar, unidadeEnsino, dataInicio, dataTermino, curso, turma, consultor, matRecebida, matAReceber, matVencida, matAVencer, ativa, preMatricula, cancelada, excluida, transferidaDe, transferidaPara, situacaoProspect)).toString());
//			sb.append(" union all ");
//		}
//		if (matVencida) {
//			sb.append((sqlConsultarMatriculaConsultorMatVencida(turmaProcessar, unidadeEnsino, dataInicio, dataTermino, curso, turma, consultor, matRecebida, matAReceber, matVencida, matAVencer, ativa, preMatricula, cancelada, excluida, transferidaDe, transferidaPara, situacaoProspect)).toString());
//			sb.append(" union all ");
//		}
//		if (matAVencer) {
//			sb.append((sqlConsultarMatriculaConsultorMatAVencer(turmaProcessar, unidadeEnsino, dataInicio, dataTermino, curso, turma, consultor, matRecebida, matAReceber, matVencida, matAVencer, ativa, preMatricula, cancelada, excluida, transferidaDe, transferidaPara, situacaoProspect)).toString());
//			sb.append(" union all ");
//		}		
		sb.append("(select 0, '' from matricula limit 1) ");
		
		sb.append(") as t where t.codigo <> 0 order by nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		TreeMap<FuncionarioVO, Integer> mapConsultor = new TreeMap<FuncionarioVO, Integer>();
		while (tabelaResultado.next()) {	
			FuncionarioVO funcionarioVO =  new FuncionarioVO();
			funcionarioVO.getPessoa().setNome(tabelaResultado.getString("nome"));
			funcionarioVO.setCodigo(tabelaResultado.getInt("codigo"));
			mapConsultor.put(funcionarioVO, consultarQtdMatConsultoresQuePossuemOuNaoMatriculaVinculada(turmaProcessar, tabelaResultado.getInt("codigo"), dataInicio, dataTermino, 
					matRecebida, matAReceber, matVencida, matAVencer, excluida, transferidaDe, transferidaPara, situacaoProspect, considerarConsultorVinculadoProspect, filtroRelatorioAcademicoVO,situacaoAlunoCurso, dataInicioPagamentoMatricula, dataFimPagamentoMatricula, considerarApenasTurmaComAlunosMatriculados, matNaoGerada, trazerAlunosBolsistas) );
		}	
		if(trazerAlunosSemTutor) {
			FuncionarioVO funcionarioVO =  new FuncionarioVO();
			funcionarioVO.getPessoa().setNome("SEM CONSULTOR");
			funcionarioVO.setCodigo(-1);
			mapConsultor.put(funcionarioVO, consultarQtdMatConsultoresQuePossuemOuNaoMatriculaVinculada(turmaProcessar, funcionarioVO.getCodigo(), dataInicio, dataTermino, 
					matRecebida, matAReceber, matVencida, matAVencer, excluida, transferidaDe, transferidaPara, situacaoProspect, considerarConsultorVinculadoProspect, filtroRelatorioAcademicoVO,situacaoAlunoCurso, dataInicioPagamentoMatricula, dataFimPagamentoMatricula, considerarApenasTurmaComAlunosMatriculados, matNaoGerada, trazerAlunosBolsistas) );
		}
		return mapConsultor;
	}
	
	public List<MatriculaVO> consultarMatConsultoresQuePossuemOuNaoMatriculaVinculada(Integer turmaProcessar, Integer codConsultor, Date dataInicio, Date dataTermino, 
			boolean matRecebida, boolean matAReceber, boolean matVencida, boolean matAVencer, boolean excluida, boolean transferidaDe, boolean transferidaPara, String situacaoProspect, boolean considerarConsultorVinculadoProspect, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, String situacaoAlunoCurso, Date dataInicioPagamentoMatricula, Date dataFimPagamentoMatricula, Boolean considerarApenasTurmaComAlunosMatriculados, boolean matNaoGerada, Boolean trazerAlunosBolsistas) throws Exception {
		List<MatriculaVO> listaMatricula = new ArrayList<MatriculaVO>();
		StringBuilder sb = new StringBuilder("select t.* from (");
		
		sb.append((sqlConsultarMatAtiva(turmaProcessar, codConsultor, dataInicio, dataTermino, matRecebida, matAReceber, matVencida, matAVencer, excluida, transferidaDe, transferidaPara, situacaoProspect, considerarConsultorVinculadoProspect, filtroRelatorioAcademicoVO,situacaoAlunoCurso, dataInicioPagamentoMatricula, dataFimPagamentoMatricula, considerarApenasTurmaComAlunosMatriculados, matNaoGerada, trazerAlunosBolsistas)).toString());
		sb.append(" union all ");
		
		if (excluida) {
			sb.append((sqlConsultarMatExcluida(turmaProcessar, codConsultor, dataInicio, dataTermino, matRecebida, matAReceber, matVencida, matAVencer, excluida, transferidaDe, transferidaPara, situacaoProspect, matNaoGerada, trazerAlunosBolsistas)).toString());
			sb.append(" union all ");
		}
		if (transferidaDe) {
			sb.append((sqlConsultarMatTransferidaDe(turmaProcessar, codConsultor, dataInicio, dataTermino, matRecebida, matAReceber, matVencida, matAVencer, excluida, transferidaDe, transferidaPara, situacaoProspect, considerarConsultorVinculadoProspect, dataInicioPagamentoMatricula, dataFimPagamentoMatricula, considerarApenasTurmaComAlunosMatriculados, matNaoGerada, trazerAlunosBolsistas)).toString());
			sb.append(" union all ");
		}
		if (transferidaPara) {
			sb.append((sqlConsultarMatTransferidaPara(turmaProcessar, codConsultor, dataInicio, dataTermino, matRecebida, matAReceber, matVencida, matAVencer, excluida, transferidaDe, transferidaPara, situacaoProspect, considerarConsultorVinculadoProspect, dataInicioPagamentoMatricula, dataFimPagamentoMatricula, considerarApenasTurmaComAlunosMatriculados, matNaoGerada, trazerAlunosBolsistas)).toString());
			sb.append(" union all ");
		}		
		sb.append("(select '' as matricula, '' as aluno, '' as formaingresso, 0 as matriculaPeriodo, '' as email, '' as celular, '' as unidadeEnsino, null as dataMatricula, '' as condicaopagamentoplanofinanceirocurso, '' as planofinanceirocurso, null as matriculaConfirmada, null as dataVencimentoMatricula, null as dataPagamentoMatricula, null as alunoInadimplente, '' as situacaoMatricula, '' as situacaoMatriculaPeriodo, '' as categoriacondicaopagamento, null as descontoMensalidade, null as descontoMatricula, null as valorparcela, null as valorMatricula from matricula limit 0) ");
		sb.append(") as t");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (tabelaResultado.next()) {
			MatriculaVO mat = new MatriculaVO();
			mat.setMatricula(tabelaResultado.getString("matricula"));
			mat.getAluno().setNome(tabelaResultado.getString("aluno"));
			mat.getAluno().setEmail(tabelaResultado.getString("email"));
			mat.getAluno().setCelular(tabelaResultado.getString("celular"));
			mat.getUnidadeEnsino().setNome(tabelaResultado.getString("unidadeEnsino"));			
			mat.setCondicaoPagamentoPlanoFinanceiroCurso(tabelaResultado.getString("condicaopagamentoplanofinanceirocurso"));
			mat.setPlanoFinanceiroCurso(tabelaResultado.getString("planofinanceirocurso"));
			mat.setDataMatriculaPeriodo(tabelaResultado.getDate("dataMatricula"));
			mat.setFormaIngresso(tabelaResultado.getString("formaIngresso"));
			mat.setMatriculaConfirmada(tabelaResultado.getBoolean("matriculaConfirmada"));
			
			mat.setDataVencimentoMatricula(tabelaResultado.getDate("dataVencimentoMatricula"));
			mat.setDataPagamento(tabelaResultado.getDate("dataPagamentoMatricula"));
			mat.setAlunoInadimplente(tabelaResultado.getBoolean("alunoInadimplente"));
			mat.setSituacao(SituacaoMatriculaPeriodoEnum.getDescricao(tabelaResultado.getString("situacaoMatriculaPeriodo")));
			mat.setCategoriacondicaopagamento(tabelaResultado.getString("categoriacondicaopagamento"));
			mat.setDescontoMensalidade(tabelaResultado.getDouble("descontoMensalidade"));
			mat.setDescontoMatricula(tabelaResultado.getDouble("descontoMatricula"));
			mat.setValorParcela(tabelaResultado.getDouble("valorparcela"));
			mat.setValorMatricula(tabelaResultado.getDouble("valorMatricula"));
			listaMatricula.add(mat);
		}		
		return listaMatricula;
	}
	

	public StringBuilder sqlConsultarMatAtiva(Integer turmaProcessar, Integer codConsultor, Date dataInicio, Date dataTermino, 
			boolean matRecebida, boolean matAReceber, boolean matVencida, boolean matAVencer, boolean excluida, boolean transferidaDe, boolean transferidaPara, String situacaoProspect,boolean considerarConsultorVinculadoProspect, FiltroRelatorioAcademicoVO filtroAcademicoVO,String situacaoAlunoCurso, Date dataInicioPagamentoMatricula, Date dataFimPagamentoMatricula, Boolean considerarApenasTurmaComAlunosMatriculados, boolean matNaoGerada, Boolean trazerAlunosBolsistas) throws Exception {
		StringBuilder sb = new StringBuilder("select distinct matricula.matricula, aluno.nome as aluno, matricula.formaingresso, matriculaperiodo.codigo as matriculaPeriodo, aluno.email, aluno.celular, unidadeensino.nome as unidadeEnsino, "); 
		sb.append(" matriculaperiodo.data as dataMatricula, condicaopagamentoplanofinanceirocurso.descricao AS condicaopagamentoplanofinanceirocurso, planofinanceirocurso.descricao AS planofinanceirocurso, ");
		sb.append(" case when naocontrolarmatricula = false"); 
		sb.append(" and");
		sb.append(" (("); 
		sb.append("	select contareceber.codigo from contareceber"); 
		sb.append("	where contareceber.matriculaperiodo = matriculaperiodo.codigo");
		sb.append("	and tipoorigem = 'MAT'"); 
		sb.append("	and contareceber.situacao = 'RE'");
		sb.append("	limit 1"); 
		sb.append( ") is not null or (matriculaperiodo.situacaomatriculaperiodo in ('AT', 'CO', 'FI') and not exists (");
		sb.append("	select contareceber.codigo from contareceber"); 
		sb.append("	where contareceber.matriculaperiodo = matriculaperiodo.codigo");
		sb.append("	and tipoorigem = 'MAT'"); 
		sb.append(" )) ");
		sb.append(" or (naocontrolarmatricula = true ");
		sb.append(" and");
		sb.append(" (("); 
		sb.append("	select contareceber.codigo from contareceber"); 
		sb.append("	where contareceber.matriculaperiodo = matriculaperiodo.codigo");
		sb.append("	and tipoorigem in ('MEN', 'MDI') "); 
		sb.append("	and contareceber.situacao = 'RE'");
		sb.append("	limit 1"); 
		sb.append( ") is not null or (matriculaperiodo.situacaomatriculaperiodo in ('AT', 'CO', 'FI') and not exists (");
		sb.append("	select contareceber.codigo from contareceber"); 
		sb.append("	where contareceber.matriculaperiodo = matriculaperiodo.codigo");
		sb.append("	and tipoorigem in ('MEN', 'MDI') "); 
		sb.append(" )))) ");
		
		sb.append(" or exists (select 1 from contarecebernegociado inner join contareceber cr on contarecebernegociado.negociacaocontareceber::varchar = cr.codorigem ");
		sb.append(" inner join contareceber c on c.matriculaperiodo = matriculaperiodo.codigo");
		sb.append(" and contarecebernegociado.contareceber = c.codigo and c.tipoorigem = '").append(TipoOrigemContaReceber.MATRICULA.getValor()).append("' ");
		sb.append(" where cr.tipoorigem = '").append(TipoOrigemContaReceber.NEGOCIACAO.getValor());
		sb.append("' and cr.situacao = '").append(SituacaoContaReceber.RECEBIDO.getValor()).append("')");
		
		sb.append(" )");
		sb.append(" then true else false ");
		sb.append(" end AS matriculaConfirmada,");

		sb.append("case when naocontrolarmatricula = false ");
		sb.append("then ");
			sb.append("case ");
				sb.append("when "); 
				sb.append("(");
					sb.append("select contareceber.datavencimento from contareceber  where contareceber.matriculaperiodo = matriculaperiodo.codigo and tipoorigem = 'MAT'	limit 1	");
				sb.append(")"); 
				sb.append("is not null then ");
				sb.append("(");
					sb.append("select contareceber.datavencimento from contareceber  where contareceber.matriculaperiodo = matriculaperiodo.codigo and tipoorigem = 'MAT'	limit 1	");
				sb.append(") else ");
				sb.append("(");
					sb.append("select matriculaperiodovencimento.datavencimento from matriculaperiodovencimento where matriculaperiodovencimento.matriculaperiodo = matriculaperiodo.codigo ");
					sb.append("and tipoOrigemMatriculaPeriodoVencimento = 'MATRICULA'	limit 1	");
				sb.append(")"); 
			sb.append("end ");
		sb.append("else "); 
		sb.append("(");
			sb.append("select contareceber.datavencimento from contareceber  where contareceber.matriculaperiodo = matriculaperiodo.codigo and tipoorigem in ('MEN', 'MDI') order by contareceber.datavencimento	limit 1	");
		sb.append(")");
		sb.append("end AS dataVencimentoMatricula, "); 
		sb.append(" case when naocontrolarmatricula = false then ");
		sb.append(" (select negociacaorecebimento.data::DATE from contareceber "); 
		sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = contareceber.codigo");
		sb.append(" inner join negociacaorecebimento on negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento"); 
		sb.append("	where contareceber.matriculaperiodo = matriculaperiodo.codigo"); 
		sb.append("	and tipoorigem = 'MAT'");								
		sb.append("	union all");
		sb.append(" select min(negociacaorecebimento.data::DATE) as data from contareceber");  
		sb.append(" inner join contarecebernegociado on contarecebernegociado.contareceber = contareceber.codigo"); 
		sb.append(" inner join contareceber cr on contarecebernegociado.negociacaocontareceber::varchar = cr.codorigem and cr.tipoorigem = 'NCR'");
		sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = cr.codigo"); 
		sb.append(" inner join negociacaorecebimento on negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento");	
		sb.append(" where contareceber.matriculaperiodo = matriculaperiodo.codigo and contareceber.situacao = 'NE'	and contareceber.tipoorigem = 'MAT'"); 
		sb.append(" order by data	limit 1	");
		sb.append("	) ");
		sb.append("	else ");
		sb.append(" (select negociacaorecebimento.data::DATE from contareceber "); 
		sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = contareceber.codigo");
		sb.append(" inner join negociacaorecebimento on negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento"); 
		sb.append("	where contareceber.matriculaperiodo = matriculaperiodo.codigo"); 
		sb.append("	and tipoorigem in ('MEN', 'MDI')");								
		sb.append("	union all");
		sb.append(" select min(negociacaorecebimento.data::DATE) as data from contareceber");  
		sb.append(" inner join contarecebernegociado on contarecebernegociado.contareceber = contareceber.codigo"); 
		sb.append(" inner join contareceber cr on contarecebernegociado.negociacaocontareceber::varchar = cr.codorigem and cr.tipoorigem = 'NCR'");
		sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = cr.codigo"); 
		sb.append(" inner join negociacaorecebimento on negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento");	
		sb.append(" where contareceber.matriculaperiodo = matriculaperiodo.codigo and contareceber.situacao = 'NE'	and contareceber.tipoorigem in ('MEN', 'MDI') "); 
		sb.append(" order by data	limit 1	");
		sb.append("	)  ");
		sb.append(" end AS dataPagamentoMatricula,");
				
		sb.append(" case when");
				sb.append(" ("); 
				sb.append("	select contareceber.data from contareceber"); 
				sb.append("	where contareceber.matriculaperiodo = matriculaperiodo.codigo");
				sb.append("	and tipoorigem in('MAT', 'MEN', 'MDI')"); 
				sb.append("	and contareceber.situacao = 'AR'");
				sb.append("	and cast(contareceber.datavencimento as date) < cast(current_date as date)"); 
				sb.append("limit 1 ");
				sb.append(" ) is not null then true else false");
				sb.append(" end AS alunoInadimplente, ");
				
		sb.append(" matricula.situacao as situacaoMatricula, matriculaperiodo.situacaomatriculaperiodo as situacaoMatriculaPeriodo, ");
		sb.append(" matriculaperiodo.categoriacondicaopagamento, ");
		
		sb.append(" (select (valor - valordescontocalculadoprimeirafaixadescontos)");
		sb.append(" from matriculaperiodovencimento ");
		sb.append(" where matriculaperiodovencimento.matriculaperiodo = matriculaperiodo.codigo ");
		sb.append(" and parcela ilike ('1/%') ");
		sb.append(" and tipoorigemmatriculaperiodovencimento  = 'MENSALIDADE' order by codigo desc limit 1 ");
		sb.append("	) AS descontoMensalidade,");
		
		sb.append(" (select (valor - valordescontocalculadoprimeirafaixadescontos)");
		sb.append(" from matriculaperiodovencimento ");
		sb.append(" where matriculaperiodovencimento.matriculaperiodo = matriculaperiodo.codigo ");
		sb.append(" and tipoorigemmatriculaperiodovencimento  = 'MATRICULA' order by codigo desc limit 1");
		sb.append("	) AS descontoMatricula, ");		
		sb.append("condicaopagamentoplanofinanceirocurso.valorparcela, ");
		sb.append("case when condicaopagamentoplanofinanceirocurso.naocontrolarmatricula = false then condicaopagamentoplanofinanceirocurso.valormatricula else null end AS valorMatricula ");
				
		sb.append(" from matriculaperiodo");		
		sb.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
		sb.append(" inner join pessoa aluno on matricula.aluno = aluno.codigo ");
		sb.append(" inner join unidadeensino on matricula.unidadeensino=unidadeensino.codigo ");
		sb.append(" inner join condicaopagamentoplanofinanceirocurso on condicaopagamentoplanofinanceirocurso.codigo=matriculaperiodo.condicaopagamentoplanofinanceirocurso ");
		sb.append(" inner join planofinanceirocurso on planofinanceirocurso.codigo = matriculaperiodo.planofinanceirocurso ");
		sb.append("INNER JOIN curso on curso.codigo = matricula.curso ");
		if (considerarConsultorVinculadoProspect) {
			sb.append(" left join prospects on prospects.pessoa = matricula.aluno ");
			if(considerarApenasTurmaComAlunosMatriculados) {
				sb.append(" inner join funcionario on funcionario.codigo = prospects.consultorpadrao ");
			}
			else {
				sb.append(" left join funcionario on funcionario.codigo = prospects.consultorpadrao ");
			}
		} else {
			if(considerarApenasTurmaComAlunosMatriculados) {
				sb.append(" inner join funcionario on funcionario.codigo = matricula.consultor ");
			}
			else {
				sb.append(" left join funcionario on funcionario.codigo = matricula.consultor ");
			}
		}
		sb.append(" left join pessoa on pessoa.codigo = funcionario.pessoa ");
		if (matAReceber || matRecebida || matVencida || matAVencer || matNaoGerada) {
			sb.append(" left join matriculaperiodovencimento on matriculaperiodo.codigo = matriculaperiodovencimento.matriculaperiodo and (matriculaperiodovencimento.parcela = 'MA' or matriculaperiodovencimento.parcela = 'Matrícula') ");
			sb.append(" left join contareceber crmn on crmn.codigo = matriculaperiodovencimento.contareceber ");
		}
		sb.append(" where 1=1 ");
		if (matRecebida || matAReceber || matVencida || matAVencer || matNaoGerada) {
			sb.append(" and ((");
			if(matNaoGerada) {
				sb.append(" (matriculaperiodovencimento.codigo is null or matriculaperiodovencimento.situacao = 'NG') ");
			}
			if (matAReceber || matVencida || matAVencer || matRecebida) {
				if(matNaoGerada) {
					sb.append(" or " );	
				}
				sb.append("   ( matriculaperiodovencimento.situacao in ('' ");
				if (matRecebida) {
					sb.append(",'GP'");
				}
				if (matAReceber || matVencida || matAVencer) {
					sb.append(",'GE'");
				}		
				sb.append(") ");
				if (matVencida && !matAVencer) {
					sb.append(" and matriculaperiodovencimento.datavencimento < current_date ");
				}
				if (matAVencer && !matVencida) {
					sb.append(" and matriculaperiodovencimento.datavencimento > current_date ");
				}
				sb.append(") ");
			}
			sb.append(") ");
			if (matAReceber || matVencida || matAVencer || matRecebida) {
				sb.append(" or ((crmn.situacao = '").append(SituacaoContaReceber.NEGOCIADO.getValor()).append("')");
				sb.append(" and ").append(sqlExistsParcelaMatriculaNegociada());
				sb.append(" and ( ");
			}
			if (matRecebida) {
				sb.append(sqlExistsParcelaMatriculaNegociadaRecebida());
			}
			if (matAVencer) {
				if (matAVencer && matRecebida) {
					sb.append(" or ");
				}
				sb.append(" (not ").append(sqlExistsParcelaMatriculaNegociadaRecebida());
				sb.append(" and not ").append(sqlExistsParcelaMatriculaNegociadaVencida()).append(") ");
			}
			if (matVencida) {
				if (matVencida && (matRecebida || matAVencer)) {
					sb.append(" or ");
				}
				sb.append(" (not ").append(sqlExistsParcelaMatriculaNegociadaRecebida());
				sb.append(" and ").append(sqlExistsParcelaMatriculaNegociadaVencida()).append(") ");
			}
			if (matAReceber) {
				if (matAReceber && (matVencida || matRecebida || matAVencer)) {
					sb.append(" or ");
				}
				sb.append(" (not ").append(sqlExistsParcelaMatriculaNegociadaRecebida()).append(") ");
			}
			if (matAReceber || matVencida || matAVencer || matRecebida) {
				sb.append(" ))");
			}
			sb.append(" )");
		}
		adicionarFiltroCalouroVeterano(sb, situacaoAlunoCurso);
		if(Uteis.isAtributoPreenchido(filtroAcademicoVO.getFormaIngresso())){
			sb.append(" and matricula.formaingresso = '").append(filtroAcademicoVO.getFormaIngresso().getValor()).append("' ");
		}
		sb.append(" and ").append(adicionarFiltroSituacaoAcademicaMatriculaPeriodo(filtroAcademicoVO, "matriculaperiodo"));
		//sb.append(" and matricula.situacao = 'AT' ");
		if(!trazerAlunosBolsistas) {
			sb.append(" and matriculaPeriodo.bolsista <> true ");	
		}
		sb.append(" and matriculaPeriodo.turma = ").append(turmaProcessar);
		
		if(codConsultor == -1) {
			sb.append(" and funcionario.codigo is null ");
		}
		else{
			sb.append(" and funcionario.codigo = ").append(codConsultor);
		}
		
		if (dataInicio != null) {
			sb.append(" and matriculaPeriodo.data >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
		}
		if (dataTermino != null) {
			sb.append(" and matriculaPeriodo.data <= '").append(Uteis.getDataJDBC(dataTermino)).append("' ");
		}
		
		if (dataInicioPagamentoMatricula != null) {
			
			sb.append(" and (select negociacaorecebimento.data::DATE from contareceber "); 
			sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = contareceber.codigo");
			sb.append(" inner join negociacaorecebimento on negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento"); 
			sb.append("	where contareceber.matriculaperiodo = matriculaperiodo.codigo"); 
			sb.append("	and tipoorigem = 'MAT'");				
			
			sb.append(" union all");
			sb.append(" select min(negociacaorecebimento.data::DATE) as data from contareceber");  
			sb.append(" inner join contarecebernegociado on contarecebernegociado.contareceber = contareceber.codigo"); 
			sb.append(" inner join contareceber cr on contarecebernegociado.negociacaocontareceber::varchar = cr.codorigem and cr.tipoorigem = 'NCR'");
			sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = cr.codigo"); 
			sb.append(" inner join negociacaorecebimento on negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento");	
			sb.append(" where contareceber.matriculaperiodo = matriculaperiodo.codigo and contareceber.situacao = 'NE'	and contareceber.tipoorigem = 'MAT'"); 
			sb.append(" order by data	limit 1	");
			sb.append("	)  >= '");
			sb.append(Uteis.getDataJDBC(dataInicioPagamentoMatricula)).append("' ");
		}
		
		if (dataFimPagamentoMatricula != null) {
			
			sb.append(" and (select negociacaorecebimento.data::DATE from contareceber "); 
			sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = contareceber.codigo");
			sb.append(" inner join negociacaorecebimento on negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento"); 
			sb.append("	where contareceber.matriculaperiodo = matriculaperiodo.codigo"); 
			sb.append("	and tipoorigem = 'MAT'");				
			
			sb.append(" union all");
			sb.append(" select min(negociacaorecebimento.data::DATE) as data from contareceber");  
			sb.append(" inner join contarecebernegociado on contarecebernegociado.contareceber = contareceber.codigo"); 
			sb.append(" inner join contareceber cr on contarecebernegociado.negociacaocontareceber::varchar = cr.codorigem and cr.tipoorigem = 'NCR'");
			sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = cr.codigo"); 
			sb.append(" inner join negociacaorecebimento on negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento");	
			sb.append(" where contareceber.matriculaperiodo = matriculaperiodo.codigo and contareceber.situacao = 'NE'	and contareceber.tipoorigem = 'MAT'"); 
			sb.append(" order by data	limit 1	");
			sb.append("	)  <= '");
			sb.append(Uteis.getDataJDBC(dataFimPagamentoMatricula)).append("' ");
			
		}
		System.out.println(sb.toString());
		return sb;
	}
	
	public StringBuilder sqlConsultarMatExcluida(Integer turmaProcessar, Integer codConsultor, Date dataInicio, Date dataTermino, 
			boolean matRecebida, boolean matAReceber, boolean matVencida, boolean matAVencer, boolean excluida, boolean transferidaDe, boolean transferidaPara, String situacaoProspect, boolean matNaoGerada, Boolean trazerAlunosBolsistas) throws Exception {
		StringBuilder sb = new StringBuilder("select matricula, aluno, '' as formaingresso,  0 as matriculaPeriodo, '' as email, '' as celular, '' as unidadeEnsino, null as dataMatricula, '' as condicaopagamentoplanofinanceirocurso, '' as planofinanceirocurso, null as matriculaConfirmada, null as dataVencimentoMatricula, null as dataPagamentoMatricula, null as alunoInadimplente, '' as situacaoMatricula, '' as situacaoMatriculaPeriodo, '' as categoriacondicaopagamento, null as descontoMensalidade, null as descontoMatricula, null as valorparcela, null as valorMatricula from logexclusaomatricula ");
		sb.append(" where  turma <> '' and nomeconsultor <> '' and codcurso is not null ");
		sb.append(" and codturma = ").append(turmaProcessar);
		sb.append(" and codconsultor = ").append(codConsultor);
		return sb;
	}	


	public StringBuilder sqlConsultarMatTransferidaDe(Integer turmaProcessar, Integer codConsultor, Date dataInicio, Date dataTermino, 
			boolean matRecebida, boolean matAReceber, boolean matVencida, boolean matAVencer, boolean excluida, boolean transferidaDe, boolean transferidaPara, String situacaoProspect, boolean considerarConsultorVinculadoProspect, Date dataInicioPagamentoMatricula, Date dataFimPagamentoMatricula, Boolean considerarApenasTurmaComAlunosMatriculados, boolean matNaoGerada, Boolean trazerAlunosBolsistas) throws Exception {
		StringBuilder sb = new StringBuilder("select distinct matricula.matricula, aluno.nome as aluno, matricula.formaingresso, matriculaperiodo.codigo as matriculaPeriodo, aluno.email, aluno.celular, unidadeensino.nome as unidadeEnsino, "); 
		sb.append(" matriculaperiodo.data as dataMatricula, condicaopagamentoplanofinanceirocurso.descricao AS condicaopagamentoplanofinanceirocurso, planofinanceirocurso.descricao AS planofinanceirocurso, ");
		sb.append(" case when naocontrolarmatricula = false"); 
		sb.append(" and");
		sb.append(" (("); 
		sb.append("	select contareceber.codigo from contareceber"); 
		sb.append("	where contareceber.matriculaperiodo = matriculaperiodo.codigo");
		sb.append("	and tipoorigem = 'MAT'"); 
		sb.append("	and contareceber.situacao = 'RE'");
		sb.append("	limit 1"); 
		sb.append( ") is not null or (matriculaperiodo.situacaomatriculaperiodo in ('AT', 'CO', 'FI') and not exists (");
		sb.append("	select contareceber.codigo from contareceber"); 
		sb.append("	where contareceber.matriculaperiodo = matriculaperiodo.codigo");
		sb.append("	and tipoorigem = 'MAT'"); 
		sb.append(" )) ");
		sb.append(" or (naocontrolarmatricula = true ");
		sb.append(" and");
		sb.append(" (("); 
		sb.append("	select contareceber.codigo from contareceber"); 
		sb.append("	where contareceber.matriculaperiodo = matriculaperiodo.codigo");
		sb.append("	and tipoorigem in ('MEN', 'MDI') "); 
		sb.append("	and contareceber.situacao = 'RE'");
		sb.append("	limit 1"); 
		sb.append( ") is not null or (matriculaperiodo.situacaomatriculaperiodo in ('AT', 'CO', 'FI') and not exists (");
		sb.append("	select contareceber.codigo from contareceber"); 
		sb.append("	where contareceber.matriculaperiodo = matriculaperiodo.codigo");
		sb.append("	and tipoorigem in ('MEN', 'MDI') "); 
		sb.append(" ))))) ");
		
		sb.append(" then true else false ");
		sb.append(" end AS matriculaConfirmada,");

				sb.append("case when naocontrolarmatricula = false ");
				sb.append("then ");
					sb.append("case ");
						sb.append("when "); 
						sb.append("(");
							sb.append("select contareceber.datavencimento from contareceber  where contareceber.matriculaperiodo = matriculaperiodo.codigo and tipoorigem = 'MAT'	limit 1	");
						sb.append(")"); 
						sb.append("is not null then ");
						sb.append("(");
							sb.append("select contareceber.datavencimento from contareceber  where contareceber.matriculaperiodo = matriculaperiodo.codigo and tipoorigem = 'MAT'	limit 1	");
						sb.append(") else ");
						sb.append("(");
							sb.append("select matriculaperiodovencimento.datavencimento from matriculaperiodovencimento where matriculaperiodovencimento.matriculaperiodo = matriculaperiodo.codigo ");
							sb.append("and tipoOrigemMatriculaPeriodoVencimento = 'MATRICULA'	limit 1	");
						sb.append(")"); 
					sb.append("end ");
				sb.append("else "); 
				sb.append("(");
					sb.append("select contareceber.datavencimento from contareceber  where contareceber.matriculaperiodo = matriculaperiodo.codigo and tipoorigem in ('MEN', 'MDI') order by contareceber.datavencimento	limit 1	");
				sb.append(")");
				sb.append("end AS dataVencimentoMatricula, "); 
				sb.append(" case when naocontrolarmatricula = false then ");
				sb.append(" (select negociacaorecebimento.data::DATE from contareceber "); 
				sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = contareceber.codigo");
				sb.append(" inner join negociacaorecebimento on negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento"); 
				sb.append("	where contareceber.matriculaperiodo = matriculaperiodo.codigo"); 
				sb.append("	and tipoorigem = 'MAT'");								
				sb.append("	union all");
				sb.append(" select min(negociacaorecebimento.data::DATE) as data from contareceber");  
				sb.append(" inner join contarecebernegociado on contarecebernegociado.contareceber = contareceber.codigo"); 
				sb.append(" inner join contareceber cr on contarecebernegociado.negociacaocontareceber::varchar = cr.codorigem and cr.tipoorigem = 'NCR'");
				sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = cr.codigo"); 
				sb.append(" inner join negociacaorecebimento on negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento");	
				sb.append(" where contareceber.matriculaperiodo = matriculaperiodo.codigo and contareceber.situacao = 'NE'	and contareceber.tipoorigem = 'MAT'"); 
				sb.append(" order by data	limit 1	");
				sb.append("	) ");
				sb.append("	else ");
				sb.append(" (select negociacaorecebimento.data::DATE from contareceber "); 
				sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = contareceber.codigo");
				sb.append(" inner join negociacaorecebimento on negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento"); 
				sb.append("	where contareceber.matriculaperiodo = matriculaperiodo.codigo"); 
				sb.append("	and tipoorigem in ('MEN', 'MDI')");								
				sb.append("	union all");
				sb.append(" select min(negociacaorecebimento.data::DATE) as data from contareceber");  
				sb.append(" inner join contarecebernegociado on contarecebernegociado.contareceber = contareceber.codigo"); 
				sb.append(" inner join contareceber cr on contarecebernegociado.negociacaocontareceber::varchar = cr.codorigem and cr.tipoorigem = 'NCR'");
				sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = cr.codigo"); 
				sb.append(" inner join negociacaorecebimento on negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento");	
				sb.append(" where contareceber.matriculaperiodo = matriculaperiodo.codigo and contareceber.situacao = 'NE'	and contareceber.tipoorigem in ('MEN', 'MDI') "); 
				sb.append(" order by data	limit 1	");
				sb.append("	)  ");
				sb.append(" end AS dataPagamentoMatricula,");
				
		sb.append(" case when");
				sb.append(" ("); 
				sb.append("	select contareceber.data from contareceber"); 
				sb.append("	where contareceber.matriculaperiodo = matriculaperiodo.codigo");
				sb.append("	and tipoorigem in('MAT', 'MEN', 'MDI')"); 
				sb.append("	and contareceber.situacao = 'AR'");
				sb.append("	and cast(contareceber.datavencimento as date) < cast(current_date as date)"); 
				sb.append("limit 1 ");
				sb.append(" ) is not null then true else false");
				sb.append(" end AS alunoInadimplente, ");
				
		sb.append(" matricula.situacao as situacaoMatricula, matriculaperiodo.situacaomatriculaperiodo as situacaoMatriculaPeriodo, ");
		sb.append(" matriculaperiodo.categoriacondicaopagamento, ");
		
		sb.append(" (select (valor - valordescontocalculadoprimeirafaixadescontos)");
		sb.append(" from matriculaperiodovencimento ");
		sb.append(" where matriculaperiodovencimento.matriculaperiodo = matriculaperiodo.codigo ");
		sb.append(" and parcela ilike ('1/%') ");
		sb.append(" and tipoorigemmatriculaperiodovencimento  = 'MENSALIDADE' order by codigo desc limit 1 ");
		sb.append("	) AS descontoMensalidade,");
		
		sb.append(" (select (valor - valordescontocalculadoprimeirafaixadescontos)");
		sb.append(" from matriculaperiodovencimento ");
		sb.append(" where matriculaperiodovencimento.matriculaperiodo = matriculaperiodo.codigo ");
		sb.append(" and tipoorigemmatriculaperiodovencimento  = 'MATRICULA' order by codigo desc limit 1 ");
		sb.append("	) AS descontoMatricula, ");
		sb.append("condicaopagamentoplanofinanceirocurso.valorparcela, ");
		sb.append("case when condicaopagamentoplanofinanceirocurso.naocontrolarmatricula = false then condicaopagamentoplanofinanceirocurso.valormatricula else null end AS valorMatricula ");
				
		sb.append(" from matriculaperiodo");		
		sb.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
		sb.append(" inner join pessoa aluno on matricula.aluno = aluno.codigo ");
		sb.append(" inner join unidadeensino on matricula.unidadeensino=unidadeensino.codigo ");
		sb.append(" inner join condicaopagamentoplanofinanceirocurso on condicaopagamentoplanofinanceirocurso.codigo=matriculaperiodo.condicaopagamentoplanofinanceirocurso ");
		sb.append(" inner join planofinanceirocurso on planofinanceirocurso.codigo = matriculaperiodo.planofinanceirocurso ");
		sb.append("INNER JOIN curso on curso.codigo = matricula.curso ");
		if (considerarConsultorVinculadoProspect) {
			sb.append(" left join prospects on prospects.pessoa = matricula.aluno ");
			if(considerarApenasTurmaComAlunosMatriculados) {
				sb.append(" inner join funcionario on funcionario.codigo = prospects.consultorpadrao ");
			}
			else {
				sb.append(" left join funcionario on funcionario.codigo = prospects.consultorpadrao ");
			}
		} else {
			if(considerarApenasTurmaComAlunosMatriculados) {
				sb.append(" inner join funcionario on funcionario.codigo = matricula.consultor ");
			}
			else {
				sb.append(" left join funcionario on funcionario.codigo = matricula.consultor ");
			}
		}
		sb.append(" left join pessoa on pessoa.codigo = funcionario.pessoa ");
		if (matAReceber || matRecebida || matVencida || matAVencer || matNaoGerada) {
			sb.append(" left join matriculaperiodovencimento on matriculaperiodo.codigo = matriculaperiodovencimento.matriculaperiodo and (matriculaperiodovencimento.parcela = 'MA' or matriculaperiodovencimento.parcela = 'Matrícula') ");
		}
		sb.append(" where 1=1 ");
		if(!trazerAlunosBolsistas) {
			sb.append(" and matriculaPeriodo.bolsista <> true ");	
		}
		if (matRecebida || matAReceber || matVencida || matAVencer || matNaoGerada) {
			sb.append(" and (");
			if(matNaoGerada) {
				sb.append(" (matriculaperiodovencimento.codigo is null or matriculaperiodovencimento.situacao = 'NG') ");
			}
			if (matAReceber || matVencida || matAVencer || matRecebida) {
				if(matNaoGerada) {
					sb.append(" or " );	
				}
				sb.append("   ( matriculaperiodovencimento.situacao in ('' ");
				if (matRecebida) {
					sb.append(",'GP'");
				}
				if (matAReceber || matVencida || matAVencer) {
					sb.append(",'GE'");
				}		
				sb.append(") ");
				if (matVencida && !matAVencer) {
					sb.append(" and matriculaperiodovencimento.datavencimento < current_date ");
				}
				if (matAVencer && !matVencida) {
					sb.append(" and matriculaperiodovencimento.datavencimento > current_date ");
				}
				sb.append(") ");
			}
					
			sb.append(") ");
		}
		sb.append(" and matriculaperiodo.alunotransferidounidade = true ");
		sb.append(" and matriculaPeriodo.turma = ").append(turmaProcessar);

		if(codConsultor == -1) {
			sb.append(" and funcionario.codigo is null ");
		}
		else{
			sb.append(" and funcionario.codigo = ").append(codConsultor);
		}

		if (dataInicio != null) {
			sb.append(" and matriculaPeriodo.data >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
		}
		if (dataTermino != null) {
			sb.append(" and matriculaPeriodo.data <= '").append(Uteis.getDataJDBC(dataTermino)).append("' ");
		}
		
		if (dataInicioPagamentoMatricula != null) {
			
			sb.append(" and (select negociacaorecebimento.data::DATE from contareceber "); 
			sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = contareceber.codigo");
			sb.append(" inner join negociacaorecebimento on negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento"); 
			sb.append("	where contareceber.matriculaperiodo = matriculaperiodo.codigo"); 
			sb.append("	and tipoorigem = 'MAT'");				
			
			sb.append("	union all");
			sb.append(" select min(negociacaorecebimento.data::DATE) as data from contareceber");  
			sb.append(" inner join contarecebernegociado on contarecebernegociado.contareceber = contareceber.codigo"); 
			sb.append(" inner join contareceber cr on contarecebernegociado.negociacaocontareceber::varchar = cr.codorigem and cr.tipoorigem = 'NCR'");
			sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = cr.codigo"); 
			sb.append(" inner join negociacaorecebimento on negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento");	
			sb.append(" where contareceber.matriculaperiodo = matriculaperiodo.codigo and contareceber.situacao = 'NE'	and contareceber.tipoorigem = 'MAT'"); 
			sb.append(" order by data	limit 1	");
			sb.append("	)  >= '");
			sb.append(Uteis.getDataJDBC(dataInicioPagamentoMatricula)).append("' ");
		}
		
		if (dataFimPagamentoMatricula != null) {
			
			sb.append(" and (select negociacaorecebimento.data::DATE from contareceber "); 
			sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = contareceber.codigo");
			sb.append(" inner join negociacaorecebimento on negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento"); 
			sb.append("	where contareceber.matriculaperiodo = matriculaperiodo.codigo"); 
			sb.append("	and tipoorigem = 'MAT'");				
			
			sb.append("	union all");
			sb.append(" select min(negociacaorecebimento.data::DATE) as data from contareceber");  
			sb.append(" inner join contarecebernegociado on contarecebernegociado.contareceber = contareceber.codigo"); 
			sb.append(" inner join contareceber cr on contarecebernegociado.negociacaocontareceber::varchar = cr.codorigem and cr.tipoorigem = 'NCR'");
			sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = cr.codigo"); 
			sb.append(" inner join negociacaorecebimento on negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento");	
			sb.append(" where contareceber.matriculaperiodo = matriculaperiodo.codigo and contareceber.situacao = 'NE'	and contareceber.tipoorigem = 'MAT'"); 
			sb.append(" order by data	limit 1	");
			sb.append("	)  <= '");
			sb.append(Uteis.getDataJDBC(dataFimPagamentoMatricula)).append("' ");
			
		}
		
		return sb;
	}
	
	public StringBuilder sqlConsultarMatTransferidaPara(Integer turmaProcessar, Integer codConsultor, Date dataInicio, Date dataTermino, 
			boolean matRecebida, boolean matAReceber, boolean matVencida, boolean matAVencer, boolean excluida, boolean transferidaDe, boolean transferidaPara, String situacaoProspect, boolean considerarConsultorVinculadoProspect, Date dataInicioPagamentoMatricula, Date dataFimPagamentoMatricula, Boolean considerarApenasTurmaComAlunosMatriculados, boolean matNaoGerada, Boolean trazerAlunosBolsistas) throws Exception {
		StringBuilder sb = new StringBuilder("select distinct matricula.matricula, aluno.nome as aluno, matricula.formaingresso, matriculaperiodo.codigo as matriculaPeriodo, aluno.email, aluno.celular, unidadeensino.nome as unidadeEnsino, "); 
		sb.append(" matriculaperiodo.data as dataMatricula, condicaopagamentoplanofinanceirocurso.descricao AS condicaopagamentoplanofinanceirocurso, planofinanceirocurso.descricao AS planofinanceirocurso, ");
		sb.append(" case when naocontrolarmatricula = false"); 
		sb.append(" and");
		sb.append(" (("); 
		sb.append("	select contareceber.codigo from contareceber"); 
		sb.append("	where contareceber.matriculaperiodo = matriculaperiodo.codigo");
		sb.append("	and tipoorigem = 'MAT'"); 
		sb.append("	and contareceber.situacao = 'RE'");
		sb.append("	limit 1"); 
		sb.append( ") is not null or (matriculaperiodo.situacaomatriculaperiodo in ('AT', 'CO', 'FI') and not exists (");
		sb.append("	select contareceber.codigo from contareceber"); 
		sb.append("	where contareceber.matriculaperiodo = matriculaperiodo.codigo");
		sb.append("	and tipoorigem = 'MAT'"); 
		sb.append(" )) ");
		sb.append(" or (naocontrolarmatricula = true ");
		sb.append(" and");
		sb.append(" (("); 
		sb.append("	select contareceber.codigo from contareceber"); 
		sb.append("	where contareceber.matriculaperiodo = matriculaperiodo.codigo");
		sb.append("	and tipoorigem in ('MEN', 'MDI') "); 
		sb.append("	and contareceber.situacao = 'RE'");
		sb.append("	limit 1"); 
		sb.append( ") is not null or (matriculaperiodo.situacaomatriculaperiodo in ('AT', 'CO', 'FI') and not exists (");
		sb.append("	select contareceber.codigo from contareceber"); 
		sb.append("	where contareceber.matriculaperiodo = matriculaperiodo.codigo");
		sb.append("	and tipoorigem in ('MEN', 'MDI') "); 
		sb.append(" ))))) ");
		
		sb.append(" then true else false ");
		sb.append(" end AS matriculaConfirmada,");

		sb.append("case when naocontrolarmatricula = false ");
		sb.append("then ");
			sb.append("case ");
				sb.append("when "); 
				sb.append("(");
					sb.append("select contareceber.datavencimento from contareceber  where contareceber.matriculaperiodo = matriculaperiodo.codigo and tipoorigem = 'MAT'	limit 1	");
				sb.append(")"); 
				sb.append("is not null then ");
				sb.append("(");
					sb.append("select contareceber.datavencimento from contareceber  where contareceber.matriculaperiodo = matriculaperiodo.codigo and tipoorigem = 'MAT'	limit 1	");
				sb.append(") else ");
				sb.append("(");
					sb.append("select matriculaperiodovencimento.datavencimento from matriculaperiodovencimento where matriculaperiodovencimento.matriculaperiodo = matriculaperiodo.codigo ");
					sb.append("and tipoOrigemMatriculaPeriodoVencimento = 'MATRICULA'	limit 1	");
				sb.append(")"); 
			sb.append("end ");
		sb.append("else "); 
		sb.append("(");
			sb.append("select contareceber.datavencimento from contareceber  where contareceber.matriculaperiodo = matriculaperiodo.codigo and tipoorigem in ('MEN', 'MDI') order by contareceber.datavencimento	limit 1	");
		sb.append(")");
		sb.append("end AS dataVencimentoMatricula, "); 
		sb.append(" case when naocontrolarmatricula = false then ");
		sb.append(" (select negociacaorecebimento.data::DATE from contareceber "); 
		sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = contareceber.codigo");
		sb.append(" inner join negociacaorecebimento on negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento"); 
		sb.append("	where contareceber.matriculaperiodo = matriculaperiodo.codigo"); 
		sb.append("	and tipoorigem = 'MAT'");								
		sb.append("	union all");
		sb.append(" select min(negociacaorecebimento.data::DATE) as data from contareceber");  
		sb.append(" inner join contarecebernegociado on contarecebernegociado.contareceber = contareceber.codigo"); 
		sb.append(" inner join contareceber cr on contarecebernegociado.negociacaocontareceber::varchar = cr.codorigem and cr.tipoorigem = 'NCR'");
		sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = cr.codigo"); 
		sb.append(" inner join negociacaorecebimento on negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento");	
		sb.append(" where contareceber.matriculaperiodo = matriculaperiodo.codigo and contareceber.situacao = 'NE'	and contareceber.tipoorigem = 'MAT'"); 
		sb.append(" order by data	limit 1	");
		sb.append("	) ");
		sb.append("	else ");
		sb.append(" (select negociacaorecebimento.data::DATE from contareceber "); 
		sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = contareceber.codigo");
		sb.append(" inner join negociacaorecebimento on negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento"); 
		sb.append("	where contareceber.matriculaperiodo = matriculaperiodo.codigo"); 
		sb.append("	and tipoorigem in ('MEN', 'MDI')");								
		sb.append("	union all");
		sb.append(" select min(negociacaorecebimento.data::DATE) as data from contareceber");  
		sb.append(" inner join contarecebernegociado on contarecebernegociado.contareceber = contareceber.codigo"); 
		sb.append(" inner join contareceber cr on contarecebernegociado.negociacaocontareceber::varchar = cr.codorigem and cr.tipoorigem = 'NCR'");
		sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = cr.codigo"); 
		sb.append(" inner join negociacaorecebimento on negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento");	
		sb.append(" where contareceber.matriculaperiodo = matriculaperiodo.codigo and contareceber.situacao = 'NE'	and contareceber.tipoorigem in ('MEN', 'MDI') "); 
		sb.append(" order by data	limit 1	");
		sb.append("	)  ");
		sb.append(" end AS dataPagamentoMatricula,");
				
		sb.append(" case when");
				sb.append(" ("); 
				sb.append("	select contareceber.data from contareceber"); 
				sb.append("	where contareceber.matriculaperiodo = matriculaperiodo.codigo");
				sb.append("	and tipoorigem in('MAT', 'MEN', 'MDI')"); 
				sb.append("	and contareceber.situacao = 'AR'");
				sb.append("	and cast(contareceber.datavencimento as date) < cast(current_date as date)"); 
				sb.append("limit 1 ");
				sb.append(" ) is not null then true else false");
				sb.append(" end AS alunoInadimplente, ");
				
		sb.append(" matricula.situacao as situacaoMatricula, matriculaperiodo.situacaomatriculaperiodo as situacaoMatriculaPeriodo, ");
		sb.append(" matriculaperiodo.categoriacondicaopagamento, ");
		
		sb.append(" (select (valor - valordescontocalculadoprimeirafaixadescontos)");
		sb.append(" from matriculaperiodovencimento ");
		sb.append(" where matriculaperiodovencimento.matriculaperiodo = matriculaperiodo.codigo ");
		sb.append(" and parcela ilike ('1/%') ");
		sb.append(" and tipoorigemmatriculaperiodovencimento  = 'MENSALIDADE' order by codigo desc limit 1 ");
		sb.append("	) AS descontoMensalidade,");
		
		sb.append(" (select (valor - valordescontocalculadoprimeirafaixadescontos)");
		sb.append(" from matriculaperiodovencimento ");
		sb.append(" where matriculaperiodovencimento.matriculaperiodo = matriculaperiodo.codigo ");
		sb.append(" and tipoorigemmatriculaperiodovencimento  = 'MATRICULA' order by codigo desc limit 1 ");
		sb.append("	) AS descontoMatricula, ");
		sb.append("condicaopagamentoplanofinanceirocurso.valorparcela, ");
		sb.append("case when condicaopagamentoplanofinanceirocurso.naocontrolarmatricula = false then condicaopagamentoplanofinanceirocurso.valormatricula else null end AS valorMatricula ");
				
		sb.append(" from matriculaperiodo");		
		sb.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
		sb.append(" inner join pessoa aluno on matricula.aluno = aluno.codigo ");
		sb.append(" inner join unidadeensino on matricula.unidadeensino=unidadeensino.codigo ");
		sb.append(" inner join condicaopagamentoplanofinanceirocurso on condicaopagamentoplanofinanceirocurso.codigo=matriculaperiodo.condicaopagamentoplanofinanceirocurso ");
		sb.append(" inner join planofinanceirocurso on planofinanceirocurso.codigo = matriculaperiodo.planofinanceirocurso ");
		sb.append("INNER JOIN curso on curso.codigo = matricula.curso ");
		if (considerarConsultorVinculadoProspect) {
			sb.append(" left join prospects on prospects.pessoa = matricula.aluno ");
			if(considerarApenasTurmaComAlunosMatriculados) {
				sb.append(" inner join funcionario on funcionario.codigo = prospects.consultorpadrao ");
			}
			else {
				sb.append(" left join funcionario on funcionario.codigo = prospects.consultorpadrao ");
			}
		} else {
			if(considerarApenasTurmaComAlunosMatriculados) {
				sb.append(" inner join funcionario on funcionario.codigo = matricula.consultor ");
			}
			else {
				sb.append(" left join funcionario on funcionario.codigo = matricula.consultor ");
			}
		}
		sb.append(" left join pessoa on pessoa.codigo = funcionario.pessoa ");
		if (matAReceber || matRecebida || matVencida || matAVencer || matNaoGerada) {
			sb.append(" left join matriculaperiodovencimento on matriculaperiodo.codigo = matriculaperiodovencimento.matriculaperiodo and (matriculaperiodovencimento.parcela = 'MA' or matriculaperiodovencimento.parcela = 'Matrícula') ");
		}
		sb.append(" where 1=1 ");
		if(!trazerAlunosBolsistas) {
			sb.append(" and matriculaPeriodo.bolsista <> true ");	
		}
		if (matRecebida || matAReceber || matVencida || matAVencer || matNaoGerada) {
			sb.append(" and (");
			if(matNaoGerada) {
				sb.append(" (matriculaperiodovencimento.codigo is null or matriculaperiodovencimento.situacao = 'NG') ");
			}
			if (matAReceber || matVencida || matAVencer || matRecebida) {
				if(matNaoGerada) {
					sb.append(" or " );	
				}
				sb.append("   ( matriculaperiodovencimento.situacao in ('' ");
				if (matRecebida) {
					sb.append(",'GP'");
				}
				if (matAReceber || matVencida || matAVencer) {
					sb.append(",'GE'");
				}		
				sb.append(") ");
				if (matVencida && !matAVencer) {
					sb.append(" and matriculaperiodovencimento.datavencimento < current_date ");
				}
				if (matAVencer && !matVencida) {
					sb.append(" and matriculaperiodovencimento.datavencimento > current_date ");
				}
				sb.append(") ");
			}
					
			sb.append(") ");
		}
		sb.append(" and alunotransferidounidade = false ");
		sb.append(" and matricula.aluno in ( select mat.aluno from matriculaperiodo mp ");
		sb.append(" inner join matricula mat on mat.matricula = mp.matricula ");
		sb.append(" where alunotransferidounidade = true ) ");
		sb.append(" and matriculaPeriodo.turma = ").append(turmaProcessar);
		
		if(codConsultor == -1) {
			sb.append(" and funcionario.codigo is null ");
		}
		else{
			sb.append(" and funcionario.codigo = ").append(codConsultor);
		}
		
		if (dataInicio != null) {
			sb.append(" and matriculaPeriodo.data >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
		}
		if (dataTermino != null) {
			sb.append(" and matriculaPeriodo.data <= '").append(Uteis.getDataJDBC(dataTermino)).append("' ");
		}
		
		if (dataInicioPagamentoMatricula != null) {
			
			sb.append(" and (select negociacaorecebimento.data::DATE from contareceber "); 
			sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = contareceber.codigo");
			sb.append(" inner join negociacaorecebimento on negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento"); 
			sb.append("	where contareceber.matriculaperiodo = matriculaperiodo.codigo"); 
			sb.append("	and tipoorigem = 'MAT'");				
			
			sb.append(" union all");
			sb.append(" select min(negociacaorecebimento.data::DATE) as data from contareceber");  
			sb.append(" inner join contarecebernegociado on contarecebernegociado.contareceber = contareceber.codigo"); 
			sb.append(" inner join contareceber cr on contarecebernegociado.negociacaocontareceber::varchar = cr.codorigem and cr.tipoorigem = 'NCR'");
			sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = cr.codigo"); 
			sb.append(" inner join negociacaorecebimento on negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento");	
			sb.append(" where contareceber.matriculaperiodo = matriculaperiodo.codigo and contareceber.situacao = 'NE'	and contareceber.tipoorigem = 'MAT'"); 
			sb.append(" order by data	limit 1	");
			sb.append("	)  >= '");
			sb.append(Uteis.getDataJDBC(dataInicioPagamentoMatricula)).append("' ");
		}
		
		if (dataFimPagamentoMatricula != null) {
			
			sb.append(" and (select negociacaorecebimento.data::DATE from contareceber "); 
			sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = contareceber.codigo");
			sb.append(" inner join negociacaorecebimento on negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento"); 
			sb.append("	where contareceber.matriculaperiodo = matriculaperiodo.codigo"); 
			sb.append("	and tipoorigem = 'MAT'");				
			
			sb.append(" union all");
			sb.append(" select min(negociacaorecebimento.data::DATE) as data from contareceber");  
			sb.append(" inner join contarecebernegociado on contarecebernegociado.contareceber = contareceber.codigo"); 
			sb.append(" inner join contareceber cr on contarecebernegociado.negociacaocontareceber::varchar = cr.codorigem and cr.tipoorigem = 'NCR'");
			sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = cr.codigo"); 
			sb.append(" inner join negociacaorecebimento on negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento");	
			sb.append(" where contareceber.matriculaperiodo = matriculaperiodo.codigo and contareceber.situacao = 'NE'	and contareceber.tipoorigem = 'MAT'"); 
			sb.append(" order by data	limit 1	");
			sb.append("	)  <= '");
			sb.append(Uteis.getDataJDBC(dataFimPagamentoMatricula)).append("' ");
			
		}
		
		return sb;
	}
	
	public Integer consultarQtdMatConsultoresQuePossuemOuNaoMatriculaVinculada(Integer turmaProcessar, Integer codConsultor, Date dataInicio, Date dataTermino, 
			boolean matRecebida, boolean matAReceber, boolean matVencida, boolean matAVencer, boolean excluida, boolean transferidaDe, boolean transferidaPara, String situacaoProspect, boolean considerarConsultorVinculadoProspect, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, String situacaoAlunoCurso, Date dataInicioPagamentoMatricula, Date dataFimPagamentoMatricula, Boolean considerarApenasTurmaComAlunosMatriculados, boolean matNaoGerada, Boolean trazerAlunosBolsistas) throws Exception {
		StringBuilder sb = new StringBuilder("select sum(t.qtdAluno) as qtdAluno from (");
		
			sb.append((sqlConsultarQtdMatAtiva(turmaProcessar, codConsultor, dataInicio, dataTermino, matRecebida, matAReceber, matVencida, matAVencer, excluida, transferidaDe, transferidaPara, situacaoProspect, considerarConsultorVinculadoProspect, filtroRelatorioAcademicoVO,situacaoAlunoCurso, dataInicioPagamentoMatricula, dataFimPagamentoMatricula, considerarApenasTurmaComAlunosMatriculados, matNaoGerada, trazerAlunosBolsistas)).toString());
			sb.append(" union all ");
		
//		if (preMatricula) {
//			sb.append((sqlConsultarQtdMatPreMatricula(turmaProcessar, codConsultor, dataInicio, dataTermino, matRecebida, matAReceber, matVencida, matAVencer, ativa, preMatricula, cancelada, excluida, transferidaDe, transferidaPara, situacaoProspect)).toString());
//			sb.append(" union all ");
//		}
//		if (cancelada) {
//			sb.append((sqlConsultarQtdMatCancelada(turmaProcessar, codConsultor, dataInicio, dataTermino, matRecebida, matAReceber, matVencida, matAVencer, ativa, preMatricula, cancelada, excluida, transferidaDe, transferidaPara, situacaoProspect)).toString());
//			sb.append(" union all ");
//		}
		if (excluida) {
			sb.append((sqlConsultarQtdMatExcluida(turmaProcessar, codConsultor, dataInicio, dataTermino, matRecebida, matAReceber, matVencida, matAVencer, excluida, transferidaDe, transferidaPara, situacaoProspect, matNaoGerada, trazerAlunosBolsistas)).toString());
			sb.append(" union all ");
		}
		if (transferidaDe) {
			sb.append((sqlConsultarQtdMatTransferidaDe(turmaProcessar, codConsultor, dataInicio, dataTermino, matRecebida, matAReceber, matVencida, matAVencer, excluida, transferidaDe, transferidaPara, situacaoProspect, considerarConsultorVinculadoProspect, dataInicioPagamentoMatricula, dataFimPagamentoMatricula, considerarApenasTurmaComAlunosMatriculados, matNaoGerada, trazerAlunosBolsistas)).toString());
			sb.append(" union all ");
		}
		if (transferidaPara) {
			sb.append((sqlConsultarQtdMatTransferidaPara(turmaProcessar, codConsultor, dataInicio, dataTermino, matRecebida, matAReceber, matVencida, matAVencer, excluida, transferidaDe, transferidaPara, situacaoProspect, considerarConsultorVinculadoProspect, dataInicioPagamentoMatricula, dataFimPagamentoMatricula, considerarApenasTurmaComAlunosMatriculados, matNaoGerada, trazerAlunosBolsistas)).toString());
			sb.append(" union all ");
		}
//		if (matRecebida) {
//			sb.append((sqlConsultarQtdMatRecebida(turmaProcessar, codConsultor, dataInicio, dataTermino, matRecebida, matAReceber, matVencida, matAVencer, ativa, preMatricula, cancelada, excluida, transferidaDe, transferidaPara, situacaoProspect)).toString());
//			sb.append(" union all ");
//		}
//		if (matAReceber) {
//			sb.append((sqlConsultarQtdMatAVencer(turmaProcessar, codConsultor, dataInicio, dataTermino, matRecebida, matAReceber, matVencida, matAVencer, ativa, preMatricula, cancelada, excluida, transferidaDe, transferidaPara, situacaoProspect)).toString());
//			sb.append(" union all ");
//		}
//		if (matVencida) {
//			sb.append((sqlConsultarQtdMatVencida(turmaProcessar, codConsultor, dataInicio, dataTermino, matRecebida, matAReceber, matVencida, matAVencer, ativa, preMatricula, cancelada, excluida, transferidaDe, transferidaPara, situacaoProspect)).toString());
//			sb.append(" union all ");
//		}
//		if (matAVencer) {
//			sb.append((sqlConsultarQtdMatAVencer(turmaProcessar, codConsultor, dataInicio, dataTermino, matRecebida, matAReceber, matVencida, matAVencer, ativa, preMatricula, cancelada, excluida, transferidaDe, transferidaPara, situacaoProspect)).toString());
//			sb.append(" union all ");
//		}		
		sb.append("(select 0 as qtdAluno from matricula limit 1) ");
		sb.append(") as t");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getInt("qtdAluno");
		}		
		return 0;
	}

	public StringBuilder sqlConsultorMatriculaTurmaMatricula(StringBuilder filtroUnidadeIN, StringBuilder filtroCursoIN,Date dataInicio, Date dataTermino, StringBuilder filtroTurmaIN, FuncionarioVO consultor, 
			boolean matRecebida, boolean matAReceber, boolean matVencida, boolean matAVencer, boolean excluida, boolean transferidaDe, boolean transferidaPara, String situacaoProspect, boolean considerarConsultorVinculadoProspect, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, String situacaoAlunoCurso, Date dataInicioPagamentoMatricula, Date dataFimPagamentoMatricula, Boolean considerarApenasTurmaComAlunosMatriculados, boolean matNaoGerada, Boolean trazerAlunosBolsistas) throws Exception {
		StringBuilder sb = new StringBuilder("select distinct matriculaperiodo.turma, turma.identificadorturma, turma.unidadeensino from matriculaperiodo  ");
		sb.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
		sb.append("INNER JOIN curso on curso.codigo = matricula.curso ");
		sb.append(" inner join turma on turma.codigo = matriculaperiodo.turma ");
		
		if (considerarConsultorVinculadoProspect) {
			sb.append(" left join prospects on prospects.pessoa = matricula.aluno ");
			if(considerarApenasTurmaComAlunosMatriculados) {
				sb.append(" inner join funcionario on funcionario.codigo = prospects.consultorpadrao ");
			}
			else {
			sb.append(" left join funcionario on funcionario.codigo = prospects.consultorpadrao ");
			}
		} else {
			if(considerarApenasTurmaComAlunosMatriculados) {
				sb.append(" inner join funcionario on funcionario.codigo = matricula.consultor ");
			}
			else {
				sb.append(" left join funcionario on funcionario.codigo = matricula.consultor ");
			}
		}
		sb.append(" left join pessoa on pessoa.codigo = funcionario.pessoa ");
		if (matAReceber || matRecebida || matVencida || matAVencer || matNaoGerada) {
			sb.append(" left join matriculaperiodovencimento on matriculaperiodo.codigo = matriculaperiodovencimento.matriculaperiodo and (matriculaperiodovencimento.parcela = 'MA' or matriculaperiodovencimento.parcela = 'Matrícula') ");
			sb.append(" left join contareceber crmn on crmn.codigo = matriculaperiodovencimento.contareceber ");
		}
		sb.append(" where 1=1 ");
		if (matRecebida || matAReceber || matVencida || matAVencer || matNaoGerada) {
			sb.append(" and ((");
			if(matNaoGerada) {
				sb.append(" (matriculaperiodovencimento.codigo is null or matriculaperiodovencimento.situacao = 'NG') ");
			}
			if (matAReceber || matVencida || matAVencer || matRecebida) {
				if(matNaoGerada) {
					sb.append(" or " );	
				}
				sb.append("   ( matriculaperiodovencimento.situacao in ('' ");
				if (matRecebida) {
					sb.append(",'GP'");
				}
				if (matAReceber || matVencida || matAVencer) {
					sb.append(",'GE'");
				}		
				sb.append(") ");
				if (matVencida && !matAVencer) {
					sb.append(" and matriculaperiodovencimento.datavencimento < current_date ");
				}
				if (matAVencer && !matVencida) {
					sb.append(" and matriculaperiodovencimento.datavencimento > current_date ");
				}
				sb.append(") ");
			}
			sb.append(") ");
			if (matAReceber || matVencida || matAVencer || matRecebida) {
				sb.append(" or ((crmn.situacao = '").append(SituacaoContaReceber.NEGOCIADO.getValor()).append("')");
				sb.append(" and ").append(sqlExistsParcelaMatriculaNegociada());
				sb.append(" and ( ");
			}
			if (matRecebida) {
				sb.append(sqlExistsParcelaMatriculaNegociadaRecebida());
			}
			if (matAVencer) {
				if (matAVencer && matRecebida) {
					sb.append(" or ");
				}
				sb.append(" (not ").append(sqlExistsParcelaMatriculaNegociadaRecebida());
				sb.append(" and not ").append(sqlExistsParcelaMatriculaNegociadaVencida()).append(") ");
			}
			if (matVencida) {
				if (matVencida && (matRecebida || matAVencer)) {
					sb.append(" or ");
				}
				sb.append(" (not ").append(sqlExistsParcelaMatriculaNegociadaRecebida());
				sb.append(" and ").append(sqlExistsParcelaMatriculaNegociadaVencida()).append(") ");
			}
			if (matAReceber) {
				if (matAReceber && (matVencida || matRecebida || matAVencer)) {
					sb.append(" or ");
				}
				sb.append(" (not ").append(sqlExistsParcelaMatriculaNegociadaRecebida()).append(") ");
			}
			if (matAReceber || matVencida || matAVencer || matRecebida) {
				sb.append(" ))");
			}
			sb.append(" )");
		}
		
		adicionarFiltroCalouroVeterano(sb, situacaoAlunoCurso);
		if(Uteis.isAtributoPreenchido(filtroRelatorioAcademicoVO.getFormaIngresso())){
			sb.append(" and matricula.formaingresso = '").append(filtroRelatorioAcademicoVO.getFormaIngresso().getValor()).append("' ");
		}
		
		sb.append(" and ").append(adicionarFiltroSituacaoAcademicaMatriculaPeriodo(filtroRelatorioAcademicoVO, "matriculaperiodo"));
		//sb.append(" and matricula.situacao = 'AT' ");
		if(!trazerAlunosBolsistas) {
			sb.append(" and matriculaPeriodo.bolsista <> true ");	
		}
		
		if (filtroUnidadeIN.length() > 0) {
			sb.append(" and matricula.unidadeEnsino in (").append(filtroUnidadeIN).append(") ");
		}
		if (filtroCursoIN.length() > 0) {
			sb.append(" and matricula.curso in (").append(filtroCursoIN).append(")");
		}
		if (filtroTurmaIN.length() > 0) {
			sb.append(" and matriculaPeriodo.turma in ( ").append(filtroTurmaIN).append(") ");
		}
		if (consultor.getCodigo().intValue() > 0) {
			sb.append(" and funcionario.codigo = ").append(consultor.getCodigo().intValue());
		}
		if (dataInicio != null) {
			sb.append(" and matriculaPeriodo.data >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
		}
		if (dataTermino != null) {
			sb.append(" and matriculaPeriodo.data <= '").append(Uteis.getDataJDBC(dataTermino)).append("' ");
		}
		
		if (dataInicioPagamentoMatricula != null) {
			
			sb.append(" and (select negociacaorecebimento.data::DATE from contareceber "); 
			sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = contareceber.codigo");
			sb.append(" inner join negociacaorecebimento on negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento"); 
			sb.append("	where contareceber.matriculaperiodo = matriculaperiodo.codigo"); 
			sb.append("	and tipoorigem = 'MAT'");				
			
			sb.append(" union all");
			sb.append(" select min(negociacaorecebimento.data::DATE) as data from contareceber");  
			sb.append(" inner join contarecebernegociado on contarecebernegociado.contareceber = contareceber.codigo"); 
			sb.append(" inner join contareceber cr on contarecebernegociado.negociacaocontareceber::varchar = cr.codorigem and cr.tipoorigem = 'NCR'");
			sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = cr.codigo"); 
			sb.append(" inner join negociacaorecebimento on negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento");	
			sb.append(" where contareceber.matriculaperiodo = matriculaperiodo.codigo and contareceber.situacao = 'NE'	and contareceber.tipoorigem = 'MAT'"); 
			sb.append(" order by data	limit 1	");
			sb.append("	)  >= '");
			sb.append(Uteis.getDataJDBC(dataInicioPagamentoMatricula)).append("' ");
		}
		
		if (dataFimPagamentoMatricula != null) {
			
			sb.append(" and (select negociacaorecebimento.data::DATE from contareceber "); 
			sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = contareceber.codigo");
			sb.append(" inner join negociacaorecebimento on negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento"); 
			sb.append("	where contareceber.matriculaperiodo = matriculaperiodo.codigo"); 
			sb.append("	and tipoorigem = 'MAT'");				
			
			sb.append(" union all");
			sb.append(" select min(negociacaorecebimento.data::DATE) as data from contareceber");  
			sb.append(" inner join contarecebernegociado on contarecebernegociado.contareceber = contareceber.codigo"); 
			sb.append(" inner join contareceber cr on contarecebernegociado.negociacaocontareceber::varchar = cr.codorigem and cr.tipoorigem = 'NCR'");
			sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = cr.codigo"); 
			sb.append(" inner join negociacaorecebimento on negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento");	
			sb.append(" where contareceber.matriculaperiodo = matriculaperiodo.codigo and contareceber.situacao = 'NE'	and contareceber.tipoorigem = 'MAT'"); 
			sb.append(" order by data	limit 1	");
			sb.append("	)  <= '");
			sb.append(Uteis.getDataJDBC(dataFimPagamentoMatricula)).append("' ");
			
		}
		
		return sb;		
	}
	
	public StringBuilder sqlConsultarMatriculaConsultorMatricula(Integer turmaProcessar, StringBuilder filtroUnidadeIN, StringBuilder filtroCursoIN, Date dataInicio, Date dataTermino, TurmaVO turma, FuncionarioVO consultor, 
			boolean matRecebida, boolean matAReceber, boolean matVencida, boolean matAVencer, boolean excluida, boolean transferidaDe, boolean transferidaPara, String situacaoProspect, boolean considerarConsultorVinculadoProspect,FiltroRelatorioAcademicoVO filtroAcademicoVO, String situacaoAlunoCurso, Date dataInicioPagamentoMatricula, Date dataFimPagamentoMatricula, Boolean considerarApenasTurmaComAlunosMatriculados, boolean matNaoGerada, Boolean trazerAlunosBolsistas) throws Exception {
		StringBuilder sb = new StringBuilder("select distinct funcionario.codigo, pessoa.nome from matriculaperiodo  ");
		sb.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
		sb.append("INNER JOIN curso on curso.codigo = matricula.curso ");
		if (considerarConsultorVinculadoProspect) {
			sb.append(" left join prospects on prospects.pessoa = matricula.aluno ");
			sb.append(" left join funcionario on funcionario.codigo = prospects.consultorpadrao ");
		} else {
			sb.append(" left join funcionario on funcionario.codigo = matricula.consultor ");
		}
		sb.append(" inner join pessoa on pessoa.codigo = funcionario.pessoa ");
		sb.append(" left join funcionariocargo on funcionariocargo.funcionario = funcionario.codigo ");
		sb.append(" left join cargo on cargo.codigo = funcionariocargo.cargo ");
		if (matAReceber || matRecebida || matVencida || matAVencer || matNaoGerada) {
			sb.append(" left join matriculaperiodovencimento on matriculaperiodo.codigo = matriculaperiodovencimento.matriculaperiodo and (matriculaperiodovencimento.parcela = 'MA' or matriculaperiodovencimento.parcela = 'Matrícula') ");
			sb.append(" left join contareceber crmn on crmn.codigo = matriculaperiodovencimento.contareceber ");
		}
		sb.append(" where 1=1 ");
		if (matRecebida || matAReceber || matVencida || matAVencer || matNaoGerada) {
			sb.append(" and ((");
			if(matNaoGerada) {
				sb.append(" (matriculaperiodovencimento.codigo is null or matriculaperiodovencimento.situacao = 'NG') ");
			}
			if (matAReceber || matVencida || matAVencer || matRecebida) {
				if(matNaoGerada) {
					sb.append(" or " );	
				}
				sb.append("   ( matriculaperiodovencimento.situacao in ('' ");
				if (matRecebida) {
					sb.append(",'GP'");
				}
				if (matAReceber || matVencida || matAVencer) {
					sb.append(",'GE'");
				}		
				sb.append(") ");
				if (matVencida && !matAVencer) {
					sb.append(" and matriculaperiodovencimento.datavencimento < current_date ");
				}
				if (matAVencer && !matVencida) {
					sb.append(" and matriculaperiodovencimento.datavencimento > current_date ");
				}
				sb.append(") ");
			}
			sb.append(") ");
			if (matAReceber || matVencida || matAVencer || matRecebida) {
				sb.append(" or ((crmn.situacao = '").append(SituacaoContaReceber.NEGOCIADO.getValor()).append("')");
				sb.append(" and ").append(sqlExistsParcelaMatriculaNegociada());
				sb.append(" and ( ");
			}
			if (matRecebida) {
				sb.append(sqlExistsParcelaMatriculaNegociadaRecebida());
			}
			if (matAVencer) {
				if (matAVencer && matRecebida) {
					sb.append(" or ");
				}
				sb.append(" (not ").append(sqlExistsParcelaMatriculaNegociadaRecebida());
				sb.append(" and not ").append(sqlExistsParcelaMatriculaNegociadaVencida()).append(") ");
			}
			if (matVencida) {
				if (matVencida && (matRecebida || matAVencer)) {
					sb.append(" or ");
				}
				sb.append(" (not ").append(sqlExistsParcelaMatriculaNegociadaRecebida());
				sb.append(" and ").append(sqlExistsParcelaMatriculaNegociadaVencida()).append(") ");
			}
			if (matAReceber) {
				if (matAReceber && (matVencida || matRecebida || matAVencer)) {
					sb.append(" or ");
				}
				sb.append(" (not ").append(sqlExistsParcelaMatriculaNegociadaRecebida()).append(") ");
			}
			if (matAReceber || matVencida || matAVencer || matRecebida) {
				sb.append(" ))");
			}
			sb.append(" )");
		}
		adicionarFiltroCalouroVeterano(sb, situacaoAlunoCurso);
		if(Uteis.isAtributoPreenchido(filtroAcademicoVO.getFormaIngresso())){
			sb.append(" and matricula.formaingresso = '").append(filtroAcademicoVO.getFormaIngresso().getValor()).append("' ");
		}
		
		sb.append(" and ").append(adicionarFiltroSituacaoAcademicaMatriculaPeriodo(filtroAcademicoVO, "matriculaperiodo"));
		if(!trazerAlunosBolsistas) {
			sb.append(" and matriculaPeriodo.bolsista <> true ");	
		}
		//sb.append(" and matricula.situacao = 'AT' ");
		if (filtroUnidadeIN.length() > 0) {
			sb.append(" and matricula.unidadeEnsino in (").append(filtroUnidadeIN).append(") ");
		}
		if (filtroCursoIN.length() > 0) {
			sb.append(" and matricula.curso in (").append(filtroCursoIN).append(")");
		}
		if (turma.getCodigo().intValue() > 0) {
			sb.append(" and matriculaPeriodo.turma = ").append(turma.getCodigo().intValue());
		}
		if (consultor.getCodigo().intValue() > 0) {
			sb.append(" and funcionario.codigo = ").append(consultor.getCodigo().intValue());
		}
		if (dataInicio != null) {
			sb.append(" and matriculaPeriodo.data >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
		}
		if (dataTermino != null) {
			sb.append(" and matriculaPeriodo.data <= '").append(Uteis.getDataJDBC(dataTermino)).append("' ");
		}
		
		if (dataInicioPagamentoMatricula != null) {
			
			sb.append(" and (select negociacaorecebimento.data::DATE from contareceber "); 
			sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = contareceber.codigo");
			sb.append(" inner join negociacaorecebimento on negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento"); 
			sb.append("	where contareceber.matriculaperiodo = matriculaperiodo.codigo"); 
			sb.append("	and tipoorigem = 'MAT'");				
			
			sb.append(" union all");
			sb.append(" select min(negociacaorecebimento.data::DATE) as data from contareceber");  
			sb.append(" inner join contarecebernegociado on contarecebernegociado.contareceber = contareceber.codigo"); 
			sb.append(" inner join contareceber cr on contarecebernegociado.negociacaocontareceber::varchar = cr.codorigem and cr.tipoorigem = 'NCR'");
			sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = cr.codigo"); 
			sb.append(" inner join negociacaorecebimento on negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento");	
			sb.append(" where contareceber.matriculaperiodo = matriculaperiodo.codigo and contareceber.situacao = 'NE'	and contareceber.tipoorigem = 'MAT'"); 
			sb.append(" order by data	limit 1	");
			sb.append("	)  >= '");
			sb.append(Uteis.getDataJDBC(dataInicioPagamentoMatricula)).append("' ");
		}
		
		if (dataFimPagamentoMatricula != null) {
			
			sb.append(" and (select negociacaorecebimento.data::DATE from contareceber "); 
			sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = contareceber.codigo");
			sb.append(" inner join negociacaorecebimento on negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento"); 
			sb.append("	where contareceber.matriculaperiodo = matriculaperiodo.codigo"); 
			sb.append("	and tipoorigem = 'MAT'");				
			
			sb.append(" union all");
			sb.append(" select min(negociacaorecebimento.data::DATE) as data from contareceber");  
			sb.append(" inner join contarecebernegociado on contarecebernegociado.contareceber = contareceber.codigo"); 
			sb.append(" inner join contareceber cr on contarecebernegociado.negociacaocontareceber::varchar = cr.codorigem and cr.tipoorigem = 'NCR'");
			sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = cr.codigo"); 
			sb.append(" inner join negociacaorecebimento on negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento");	
			sb.append(" where contareceber.matriculaperiodo = matriculaperiodo.codigo and contareceber.situacao = 'NE'	and contareceber.tipoorigem = 'MAT'"); 
			sb.append(" order by data	limit 1	");
			sb.append("	)  <= '");
			sb.append(Uteis.getDataJDBC(dataFimPagamentoMatricula)).append("' ");
			
		}
		
		return sb;
	}	
	
	public StringBuilder sqlConsultarQtdMatAtiva(Integer turmaProcessar, Integer codConsultor, Date dataInicio, Date dataTermino, 
			boolean matRecebida, boolean matAReceber, boolean matVencida, boolean matAVencer, boolean excluida, boolean transferidaDe, boolean transferidaPara, String situacaoProspect,boolean considerarConsultorVinculadoProspect, FiltroRelatorioAcademicoVO filtroAcademicoVO,String situacaoAlunoCurso, Date dataInicioPagamentoMatricula, Date dataFimPagamentoMatricula, Boolean considerarApenasTurmaComAlunosMatriculados, boolean matNaoGerada, Boolean trazerAlunosBolsistas) throws Exception {
		StringBuilder sb = new StringBuilder("select count(matricula.matricula) as qtdAluno from matriculaperiodo  ");
		sb.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
		sb.append("INNER JOIN curso on curso.codigo = matricula.curso ");
		if (considerarConsultorVinculadoProspect) {
			sb.append(" left join prospects on prospects.pessoa = matricula.aluno ");
			sb.append(" left join funcionario on funcionario.codigo = prospects.consultorpadrao ");
		} else {
			sb.append(" left join funcionario on funcionario.codigo = matricula.consultor ");
		}
		sb.append(" left join pessoa on pessoa.codigo = funcionario.pessoa ");
		if (matAReceber || matRecebida || matVencida || matAVencer || matNaoGerada) {
			sb.append(" left join matriculaperiodovencimento on matriculaperiodo.codigo = matriculaperiodovencimento.matriculaperiodo and (matriculaperiodovencimento.parcela = 'MA' or matriculaperiodovencimento.parcela = 'Matrícula') ");
		}
		sb.append(" where 1=1 ");
		if (matRecebida || matAReceber || matVencida || matAVencer || matNaoGerada) {
			sb.append(" and (");
			if(matNaoGerada) {
				sb.append(" (matriculaperiodovencimento.codigo is null or matriculaperiodovencimento.situacao = 'NG') ");
			}
			if (matAReceber || matVencida || matAVencer || matRecebida) {
				if(matNaoGerada) {
					sb.append(" or " );	
				}
				sb.append("   ( matriculaperiodovencimento.situacao in ('' ");
				if (matRecebida) {
					sb.append(",'GP'");
				}
				if (matAReceber || matVencida || matAVencer) {
					sb.append(",'GE'");
				}		
				sb.append(") ");
				if (matVencida && !matAVencer) {
					sb.append(" and matriculaperiodovencimento.datavencimento < current_date ");
				}
				if (matAVencer && !matVencida) {
					sb.append(" and matriculaperiodovencimento.datavencimento > current_date ");
				}
				sb.append(") ");
			}
					
			sb.append(") ");
		}
		adicionarFiltroCalouroVeterano(sb, situacaoAlunoCurso);
		if(Uteis.isAtributoPreenchido(filtroAcademicoVO.getFormaIngresso())){
			sb.append(" and matricula.formaingresso = '").append(filtroAcademicoVO.getFormaIngresso().getValor()).append("' ");
		}
		sb.append(" and ").append(adicionarFiltroSituacaoAcademicaMatriculaPeriodo(filtroAcademicoVO, "matriculaperiodo"));
		//sb.append(" and matricula.situacao = 'AT' ");
		if(!trazerAlunosBolsistas) {
			sb.append(" and matriculaPeriodo.bolsista <> true ");	
		}
		sb.append(" and matriculaPeriodo.turma = ").append(turmaProcessar);
		
		if(codConsultor == -1) {
			sb.append(" and funcionario.codigo is null ");
		}else {
			sb.append(" and funcionario.codigo = ").append(codConsultor);
		}
		
		if (dataInicio != null) {
			sb.append(" and matriculaPeriodo.data >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
		}
		if (dataTermino != null) {
			sb.append(" and matriculaPeriodo.data <= '").append(Uteis.getDataJDBC(dataTermino)).append("' ");
		}
		
		if (dataInicioPagamentoMatricula != null) {
			
			sb.append(" and (select negociacaorecebimento.data::DATE from contareceber "); 
			sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = contareceber.codigo");
			sb.append(" inner join negociacaorecebimento on negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento"); 
			sb.append("	where contareceber.matriculaperiodo = matriculaperiodo.codigo"); 
			sb.append("	and tipoorigem = 'MAT'");				
			
			sb.append(" union all");
			sb.append(" select min(negociacaorecebimento.data::DATE) as data from contareceber");  
			sb.append(" inner join contarecebernegociado on contarecebernegociado.contareceber = contareceber.codigo"); 
			sb.append(" inner join contareceber cr on contarecebernegociado.negociacaocontareceber::varchar = cr.codorigem and cr.tipoorigem = 'NCR'");
			sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = cr.codigo"); 
			sb.append(" inner join negociacaorecebimento on negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento");	
			sb.append(" where contareceber.matriculaperiodo = matriculaperiodo.codigo and contareceber.situacao = 'NE'	and contareceber.tipoorigem = 'MAT'"); 
			sb.append(" order by data	limit 1	");
			sb.append("	)  >= '");
			sb.append(Uteis.getDataJDBC(dataInicioPagamentoMatricula)).append("' ");
		}
		
		if (dataFimPagamentoMatricula != null) {
			
			sb.append(" and (select negociacaorecebimento.data::DATE from contareceber "); 
			sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = contareceber.codigo");
			sb.append(" inner join negociacaorecebimento on negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento"); 
			sb.append("	where contareceber.matriculaperiodo = matriculaperiodo.codigo"); 
			sb.append("	and tipoorigem = 'MAT'");				
			
			sb.append(" union all");
			sb.append(" select min(negociacaorecebimento.data::DATE) as data from contareceber");  
			sb.append(" inner join contarecebernegociado on contarecebernegociado.contareceber = contareceber.codigo"); 
			sb.append(" inner join contareceber cr on contarecebernegociado.negociacaocontareceber::varchar = cr.codorigem and cr.tipoorigem = 'NCR'");
			sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = cr.codigo"); 
			sb.append(" inner join negociacaorecebimento on negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento");	
			sb.append(" where contareceber.matriculaperiodo = matriculaperiodo.codigo and contareceber.situacao = 'NE'	and contareceber.tipoorigem = 'MAT'"); 
			sb.append(" order by data	limit 1	");
			sb.append("	)  <= '");
			sb.append(Uteis.getDataJDBC(dataFimPagamentoMatricula)).append("' ");
		}
		
		return sb;
	}
	
	public StringBuilder sqlConsultarMatriculaConsultorMatPreMatricula(Integer turmaProcessar, UnidadeEnsinoVO unidadeEnsino, Date dataInicio, Date dataTermino, CursoVO curso, TurmaVO turma, FuncionarioVO consultor, 
			boolean matRecebida, boolean matAReceber, boolean matVencida, boolean matAVencer, boolean ativa, boolean preMatricula, boolean cancelada, boolean excluida, boolean transferidaDe, boolean transferidaPara, String situacaoProspect) throws Exception {
		StringBuilder sb = new StringBuilder("select distinct funcionario.codigo, pessoa.nome from matriculaperiodo  ");
		sb.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
		sb.append(" inner join funcionario on funcionario.codigo = matricula.consultor ");
		sb.append(" inner join pessoa on pessoa.codigo = funcionario.pessoa ");
		sb.append(" left join funcionariocargo on funcionariocargo.funcionario = funcionario.codigo ");
		sb.append(" left join cargo on cargo.codigo = funcionariocargo.cargo ");
		if (matAReceber || matRecebida || matVencida || matAVencer) {
			sb.append(" left join matriculaperiodovencimento on matriculaperiodo.codigo = matriculaperiodovencimento.matriculaperiodo ");
		}
		sb.append(" where 1=1 ");
		if (matRecebida || matAReceber || matVencida || matAVencer) {
			sb.append(" and (matriculaperiodovencimento.parcela = 'MA' or matriculaperiodovencimento.parcela = 'Matrícula') and matriculaperiodovencimento.situacao in ('' ");
		}
		if (matRecebida) {
			sb.append(",'GP'");
		}
		if (matAReceber || matVencida || matAVencer) {
			sb.append(",'GE'");
		}
		if (matRecebida || matAReceber || matVencida || matAVencer) {
			sb.append(") ");
		}
		if (matVencida && !matAVencer) {
			sb.append(" and matriculaperiodovencimento.datavencimento < current_date ");
		}
		if (matAVencer && !matVencida) {
			sb.append(" and matriculaperiodovencimento.datavencimento > current_date ");
		}
		sb.append(" and matriculaPeriodo.situacaomatriculaPeriodo = 'PR' ");	
		sb.append(" and matriculaPeriodo.bolsista <> true ");
		if (unidadeEnsino.getCodigo().intValue() > 0) {
			sb.append(" and matricula.unidadeEnsino = ").append(unidadeEnsino.getCodigo().intValue());
		}
		if (curso.getCodigo().intValue() > 0) {
			sb.append(" and matricula.curso = ").append(curso.getCodigo().intValue());
		}
		if (turma.getCodigo().intValue() > 0) {
			sb.append(" and matriculaPeriodo.turma = ").append(turma.getCodigo().intValue());
		}
		if (consultor.getCodigo().intValue() > 0) {
			sb.append(" and funcionario.codigo = ").append(consultor.getCodigo().intValue());
		}
		if (dataInicio != null) {
			sb.append(" and matriculaPeriodo.data >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
		}
		if (dataTermino != null) {
			sb.append(" and matriculaPeriodo.data <= '").append(Uteis.getDataJDBC(dataTermino)).append("' ");
		}
		return sb;
	}
	
	public StringBuilder sqlConsultorMatriculaTurmaMatPreMatricula(UnidadeEnsinoVO unidadeEnsino, Date dataInicio, Date dataTermino, CursoVO curso, TurmaVO turma, FuncionarioVO consultor, 
			boolean matRecebida, boolean matAReceber, boolean matVencida, boolean matAVencer, boolean ativa, boolean preMatricula, boolean cancelada, boolean excluida, boolean transferidaDe, boolean transferidaPara, String situacaoProspect, boolean considerarConsultorVinculadoProspect) throws Exception {
		StringBuilder sb = new StringBuilder("select distinct matriculaperiodo.turma, turma.identificadorturma from matriculaperiodo  ");
		sb.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
		sb.append(" inner join turma on turma.codigo = matriculaperiodo.turma ");
		if (considerarConsultorVinculadoProspect) {
			sb.append(" left join prospects on prospects.pessoa = matricula.aluno ");
			sb.append(" left join funcionario on funcionario.codigo = prospects.consultorpadrao ");
		} else {
			sb.append(" left join funcionario on funcionario.codigo = matricula.consultor ");
		}
		sb.append(" left join pessoa on pessoa.codigo = funcionario.pessoa ");
		if (matAReceber || matRecebida || matVencida || matAVencer) {
			sb.append(" left join matriculaperiodovencimento on matriculaperiodo.codigo = matriculaperiodovencimento.matriculaperiodo ");
		}
		sb.append(" where 1=1 ");
		sb.append(" and matriculaPeriodo.bolsista <> true ");
		if (matRecebida || matAReceber || matVencida || matAVencer) {
			sb.append(" and (matriculaperiodovencimento.parcela = 'MA' or matriculaperiodovencimento.parcela = 'Matrícula') and matriculaperiodovencimento.situacao in ('' ");
		}
		if (matRecebida) {
			sb.append(",'GP'");
		}
		if (matAReceber || matVencida || matAVencer) {
			sb.append(",'GE'");
		}
		if (matRecebida || matAReceber || matVencida || matAVencer) {
			sb.append(") ");
		}
		if (matVencida && !matAVencer) {
			sb.append(" and matriculaperiodovencimento.datavencimento < current_date ");
		}
		if (matAVencer && !matVencida) {
			sb.append(" and matriculaperiodovencimento.datavencimento > current_date ");
		}
		sb.append(" and matriculaPeriodo.situacaomatriculaPeriodo = 'PR' ");			
		if (unidadeEnsino.getCodigo().intValue() > 0) {
			sb.append(" and matricula.unidadeEnsino = ").append(unidadeEnsino.getCodigo().intValue());
		}
		if (curso.getCodigo().intValue() > 0) {
			sb.append(" and matricula.curso = ").append(curso.getCodigo().intValue());
		}
		if (turma.getCodigo().intValue() > 0) {
			sb.append(" and matriculaPeriodo.turma = ").append(turma.getCodigo().intValue());
		}
		if (consultor.getCodigo().intValue() > 0) {
			sb.append(" and funcionario.codigo = ").append(consultor.getCodigo().intValue());
		}
		if (dataInicio != null) {
			sb.append(" and matriculaPeriodo.data >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
		}
		if (dataTermino != null) {
			sb.append(" and matriculaPeriodo.data <= '").append(Uteis.getDataJDBC(dataTermino)).append("' ");
		}
		return sb;		
	}
	
	public StringBuilder sqlConsultarQtdMatPreMatricula(Integer turmaProcessar, Integer codConsultor, Date dataInicio, Date dataTermino, 
			boolean matRecebida, boolean matAReceber, boolean matVencida, boolean matAVencer, boolean ativa, boolean preMatricula, boolean cancelada, boolean excluida, boolean transferidaDe, boolean transferidaPara, String situacaoProspect) throws Exception {
		StringBuilder sb = new StringBuilder("select count(matricula.matricula) as qtdAluno from matriculaperiodo  ");
		sb.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
		sb.append(" left join funcionario on funcionario.codigo = matricula.consultor ");
		sb.append(" left join pessoa on pessoa.codigo = funcionario.pessoa ");
		if (matAReceber || matRecebida || matVencida || matAVencer) {
			sb.append(" left join matriculaperiodovencimento on matriculaperiodo.codigo = matriculaperiodovencimento.matriculaperiodo ");
		}
		sb.append(" where 1=1 ");
		sb.append(" and matriculaPeriodo.bolsista <> true ");
		if (matRecebida || matAReceber || matVencida || matAVencer) {
			sb.append(" and (matriculaperiodovencimento.parcela = 'MA' or matriculaperiodovencimento.parcela = 'Matrícula') and matriculaperiodovencimento.situacao in ('' ");
		}
		if (matRecebida) {
			sb.append(",'GP'");
		}
		if (matAReceber || matVencida || matAVencer) {
			sb.append(",'GE'");
		}
		if (matRecebida || matAReceber || matVencida || matAVencer) {
			sb.append(") ");
		}
		if (matVencida && !matAVencer) {
			sb.append(" and matriculaperiodovencimento.datavencimento < current_date ");
		}
		if (matAVencer && !matVencida) {
			sb.append(" and matriculaperiodovencimento.datavencimento > current_date ");
		}
		sb.append(" and matriculaPeriodo.situacaomatriculaPeriodo = 'PR' ");
		sb.append(" and matriculaPeriodo.turma = ").append(turmaProcessar);
		sb.append(" and funcionario.codigo = ").append(codConsultor);
		if (dataInicio != null) {
			sb.append(" and matriculaPeriodo.data >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
		}
		if (dataTermino != null) {
			sb.append(" and matriculaPeriodo.data <= '").append(Uteis.getDataJDBC(dataTermino)).append("' ");
		}
		return sb;
	}
	
	public StringBuilder sqlConsultarMatriculaConsultorMatCancelada(Integer turmaProcessar, UnidadeEnsinoVO unidadeEnsino, Date dataInicio, Date dataTermino, CursoVO curso, TurmaVO turma, FuncionarioVO consultor, 
			boolean matRecebida, boolean matAReceber, boolean matVencida, boolean matAVencer, boolean ativa, boolean preMatricula, boolean cancelada, boolean excluida, boolean transferidaDe, boolean transferidaPara, String situacaoProspect) throws Exception {
		StringBuilder sb = new StringBuilder("select distinct funcionario.codigo, pessoa.nome from matriculaperiodo  ");
		sb.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
		sb.append(" inner join funcionario on funcionario.codigo = matricula.consultor ");
		sb.append(" inner join pessoa on pessoa.codigo = funcionario.pessoa ");
		sb.append(" left join funcionariocargo on funcionariocargo.funcionario = funcionario.codigo ");
		sb.append(" left join cargo on cargo.codigo = funcionariocargo.cargo ");
		sb.append(" where 1=1 ");
		sb.append(" and matriculaPeriodo.bolsista <> true ");
		sb.append(" and matricula.situacao = 'CA' ");			
		if (unidadeEnsino.getCodigo().intValue() > 0) {
			sb.append(" and matricula.unidadeEnsino = ").append(unidadeEnsino.getCodigo().intValue());
		}
		if (curso.getCodigo().intValue() > 0) {
			sb.append(" and matricula.curso = ").append(curso.getCodigo().intValue());
		}
		if (turma.getCodigo().intValue() > 0) {
			sb.append(" and matriculaPeriodo.turma = ").append(turma.getCodigo().intValue());
		}
		if (consultor.getCodigo().intValue() > 0) {
			sb.append(" and funcionario.codigo = ").append(consultor.getCodigo().intValue());
		}
		if (dataInicio != null) {
			sb.append(" and matriculaPeriodo.data >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
		}
		if (dataTermino != null) {
			sb.append(" and matriculaPeriodo.data <= '").append(Uteis.getDataJDBC(dataTermino)).append("' ");
		}
		return sb;
	}
	
	public StringBuilder sqlConsultarQtdMatCancelada(Integer turmaProcessar, Integer codConsultor, Date dataInicio, Date dataTermino, 
			boolean matRecebida, boolean matAReceber, boolean matVencida, boolean matAVencer, boolean ativa, boolean preMatricula, boolean cancelada, boolean excluida, boolean transferidaDe, boolean transferidaPara, String situacaoProspect) throws Exception {
		StringBuilder sb = new StringBuilder("select count(matricula.matricula) as qtdAluno from matriculaperiodo  ");
		sb.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
		sb.append(" left join funcionario on funcionario.codigo = matricula.consultor ");
		sb.append(" left join pessoa on pessoa.codigo = funcionario.pessoa ");
		sb.append(" where 1=1 ");
		sb.append(" and matriculaPeriodo.bolsista <> true ");
		sb.append(" and matricula.situacao = 'CA' ");
		sb.append(" and matriculaPeriodo.turma = ").append(turmaProcessar);
		sb.append(" and funcionario.codigo = ").append(codConsultor);
		if (dataInicio != null) {
			sb.append(" and matriculaPeriodo.data >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
		}
		if (dataTermino != null) {
			sb.append(" and matriculaPeriodo.data <= '").append(Uteis.getDataJDBC(dataTermino)).append("' ");
		}
		return sb;
	}	
	
	public StringBuilder sqlConsultorMatriculaTurmaMatCancelada(UnidadeEnsinoVO unidadeEnsino, Date dataInicio, Date dataTermino, CursoVO curso, TurmaVO turma, FuncionarioVO consultor, 
			boolean matRecebida, boolean matAReceber, boolean matVencida, boolean matAVencer, boolean ativa, boolean preMatricula, boolean cancelada, boolean excluida, boolean transferidaDe, boolean transferidaPara, String situacaoProspect, boolean considerarConsultorVinculadoProspect) throws Exception {
		StringBuilder sb = new StringBuilder("select distinct matriculaperiodo.turma, turma.identificadorturma from matriculaperiodo  ");
		sb.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
		sb.append(" inner join turma on turma.codigo = matriculaperiodo.turma ");
		if (considerarConsultorVinculadoProspect) {
			sb.append(" left join prospects on prospects.pessoa = matricula.aluno ");
			sb.append(" left join funcionario on funcionario.codigo = prospects.consultorpadrao ");
		} else {
			sb.append(" left join funcionario on funcionario.codigo = matricula.consultor ");
		}
		sb.append(" left join pessoa on pessoa.codigo = funcionario.pessoa ");
		sb.append(" where 1=1 ");
		sb.append(" and matriculaPeriodo.bolsista <> true ");			
		sb.append(" and matricula.situacao = 'CA' ");			
		if (unidadeEnsino.getCodigo().intValue() > 0) {
			sb.append(" and matricula.unidadeEnsino = ").append(unidadeEnsino.getCodigo().intValue());
		}
		if (curso.getCodigo().intValue() > 0) {
			sb.append(" and matricula.curso = ").append(curso.getCodigo().intValue());
		}
		if (turma.getCodigo().intValue() > 0) {
			sb.append(" and matriculaPeriodo.turma = ").append(turma.getCodigo().intValue());
		}
		if (consultor.getCodigo().intValue() > 0) {
			sb.append(" and funcionario.codigo = ").append(consultor.getCodigo().intValue());
		}
		if (dataInicio != null) {
			sb.append(" and matriculaPeriodo.data >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
		}
		if (dataTermino != null) {
			sb.append(" and matriculaPeriodo.data <= '").append(Uteis.getDataJDBC(dataTermino)).append("' ");
		}
		return sb;		
	}
	
	public StringBuilder sqlConsultarMatriculaConsultorMatExcluida(Integer turmaProcessar, StringBuilder filtroUnidadeIN, StringBuilder filtroCursoIN, Date dataInicio, Date dataTermino, TurmaVO turma, FuncionarioVO consultor, 
			boolean matRecebida, boolean matAReceber, boolean matVencida, boolean matAVencer, boolean excluida, boolean transferidaDe, boolean transferidaPara, String situacaoProspect, boolean matNaoGerada, Boolean trazerAlunosBolsistas) throws Exception {
		StringBuilder sb = new StringBuilder("select distinct codconsultor as codigo, nomeconsultor as nome from logexclusaomatricula ");		
		sb.append(" where turma <> '' and nomeconsultor <> '' and codcurso is not null ");
		if (filtroUnidadeIN.length() > 0) {
			sb.append(" and codunidadeensino in (").append(filtroUnidadeIN).append(") ");
		}
		if (filtroCursoIN.length() > 0) {
			sb.append(" and codcurso in (").append(filtroCursoIN).append(")");
		}
		if (turma.getCodigo().intValue() > 0) {
			sb.append(" and codturma = ").append(turma.getCodigo().intValue());
		}
		if (consultor.getCodigo().intValue() > 0) {
			sb.append(" and codconsultor = ").append(consultor.getCodigo().intValue());
		}
		return sb;
	}
	
	public StringBuilder sqlConsultarQtdMatExcluida(Integer turmaProcessar, Integer codConsultor, Date dataInicio, Date dataTermino, 
			boolean matRecebida, boolean matAReceber, boolean matVencida, boolean matAVencer, boolean excluida, boolean transferidaDe, boolean transferidaPara, String situacaoProspect, boolean matNaoGerada, Boolean trazerAlunosBolsistas) throws Exception {
		StringBuilder sb = new StringBuilder("select count(matricula) as qtdAluno from logexclusaomatricula ");
		sb.append(" where  turma <> '' and nomeconsultor <> '' and codcurso is not null ");
		sb.append(" and codturma = ").append(turmaProcessar);
		sb.append(" and codconsultor = ").append(codConsultor);
		return sb;
	}	
		
	public StringBuilder sqlConsultorMatriculaTurmaMatExcluida(StringBuilder filtroUnidadeIN, StringBuilder filtroCursoIN, Date dataInicio, Date dataTermino, StringBuilder filtroTurmaIN, FuncionarioVO consultor, 
			boolean matRecebida, boolean matAReceber, boolean matVencida, boolean matAVencer, boolean excluida, boolean transferidaDe, boolean transferidaPara, String situacaoProspect, boolean matNaoGerada, Boolean trazerAlunosBolsistas) throws Exception {
		StringBuilder sb = new StringBuilder("select distinct codturma as turma, turma as identificadorturma , unidadeensino from logexclusaomatricula ");
		sb.append(" where turma <> '' and nomeconsultor <> '' and codcurso is not null ");

		if (filtroUnidadeIN.length() > 0) {
			sb.append(" and codunidadeensino in (").append(filtroUnidadeIN).append(") ");
		}
		if (filtroCursoIN.length() > 0) {
			sb.append(" and codcurso in (").append(filtroCursoIN).append(")");
		}	
		if (filtroTurmaIN.length() > 0) {
			sb.append(" and codturma in ( ").append(filtroTurmaIN).append(") ");
		}
		if (consultor.getCodigo().intValue() > 0) {
			sb.append(" and codconsultor = ").append(consultor.getCodigo().intValue());
		}
		return sb;		
	}
	
	public StringBuilder sqlConsultorMatriculaTurmaMatTransferidaDe(StringBuilder filtroUnidadeIN, StringBuilder filtroCursoIN, Date dataInicio, Date dataTermino, StringBuilder filtroTurmaIN, FuncionarioVO consultor, 
			boolean matRecebida, boolean matAReceber, boolean matVencida, boolean matAVencer, boolean excluida, boolean transferidaDe, boolean transferidaPara, String situacaoProspect, boolean considerarConsultorVinculadoProspect, Date dataInicioPagamentoMatricula, Date dataFimPagamentoMatricula, Boolean considerarApenasTurmaComAlunosMatriculados, boolean matNaoGerada, Boolean trazerAlunosBolsistas) throws Exception {
		StringBuilder sb = new StringBuilder("select distinct matriculaperiodo.turma, turma.identificadorturma, turma.unidadeensino from matriculaperiodo  ");
		sb.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
		sb.append(" inner join turma on turma.codigo = matriculaperiodo.turma ");
		if (considerarConsultorVinculadoProspect) {
			sb.append(" left join prospects on prospects.pessoa = matricula.aluno ");
			if(considerarApenasTurmaComAlunosMatriculados) {
				sb.append(" inner join funcionario on funcionario.codigo = prospects.consultorpadrao ");
			}
			else {
				sb.append(" left join funcionario on funcionario.codigo = prospects.consultorpadrao ");
			}
		} else {
			if(considerarApenasTurmaComAlunosMatriculados) {
				sb.append(" inner join funcionario on funcionario.codigo = matricula.consultor ");
			}
			else {
				sb.append(" left join funcionario on funcionario.codigo = matricula.consultor ");
			}
		}
		sb.append(" left join pessoa on pessoa.codigo = funcionario.pessoa ");
		if (matAReceber || matRecebida || matVencida || matAVencer || matNaoGerada) {
			sb.append(" left join matriculaperiodovencimento on matriculaperiodo.codigo = matriculaperiodovencimento.matriculaperiodo and (matriculaperiodovencimento.parcela = 'MA' or matriculaperiodovencimento.parcela = 'Matrícula') ");
		}
		sb.append(" where 1=1 ");
		if(!trazerAlunosBolsistas) {
			sb.append(" and matriculaPeriodo.bolsista <> true ");	
		}
		if (matRecebida || matAReceber || matVencida || matAVencer || matNaoGerada) {
			sb.append(" and (");
			if(matNaoGerada) {
				sb.append(" (matriculaperiodovencimento.codigo is null or matriculaperiodovencimento.situacao = 'NG') ");
			}
			if (matAReceber || matVencida || matAVencer || matRecebida) {
				if(matNaoGerada) {
					sb.append(" or " );	
				}
				sb.append("   ( matriculaperiodovencimento.situacao in ('' ");
				if (matRecebida) {
					sb.append(",'GP'");
				}
				if (matAReceber || matVencida || matAVencer) {
					sb.append(",'GE'");
				}		
				sb.append(") ");
				if (matVencida && !matAVencer) {
					sb.append(" and matriculaperiodovencimento.datavencimento < current_date ");
				}
				if (matAVencer && !matVencida) {
					sb.append(" and matriculaperiodovencimento.datavencimento > current_date ");
				}
				sb.append(") ");
			}
					
			sb.append(") ");
		}
		sb.append(" and matriculaperiodo.alunotransferidounidade = true ");			

		if (filtroUnidadeIN.length() > 0) {
			sb.append(" and matricula.unidadeEnsino in (").append(filtroUnidadeIN).append(") ");
		}
		if (filtroCursoIN.length() > 0) {
			sb.append(" and matricula.curso in (").append(filtroCursoIN).append(")");
		}
		
		if (filtroTurmaIN.length() > 0) {
			sb.append(" and matriculaPeriodo.turma in ( ").append(filtroTurmaIN).append(") ");
		}
		if (consultor.getCodigo().intValue() > 0) {
			sb.append(" and funcionario.codigo = ").append(consultor.getCodigo().intValue());
		}
		if (dataInicio != null) {
			sb.append(" and matriculaPeriodo.data >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
		}
		if (dataTermino != null) {
			sb.append(" and matriculaPeriodo.data <= '").append(Uteis.getDataJDBC(dataTermino)).append("' ");
		}
		
		if (dataInicioPagamentoMatricula != null) {
			
			sb.append(" and (select negociacaorecebimento.data::DATE from contareceber "); 
			sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = contareceber.codigo");
			sb.append(" inner join negociacaorecebimento on negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento"); 
			sb.append("	where contareceber.matriculaperiodo = matriculaperiodo.codigo"); 
			sb.append("	and tipoorigem = 'MAT'");				
			
			sb.append("union all");
			sb.append(" select min(negociacaorecebimento.data::DATE) as data from contareceber");  
			sb.append(" inner join contarecebernegociado on contarecebernegociado.contareceber = contareceber.codigo"); 
			sb.append(" inner join contareceber cr on contarecebernegociado.negociacaocontareceber::varchar = cr.codorigem and cr.tipoorigem = 'NCR'");
			sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = cr.codigo"); 
			sb.append(" inner join negociacaorecebimento on negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento");	
			sb.append(" where contareceber.matriculaperiodo = matriculaperiodo.codigo and contareceber.situacao = 'NE'	and contareceber.tipoorigem = 'MAT'"); 
			sb.append(" order by data	limit 1	");
			sb.append("	)  >= '");
			sb.append(Uteis.getDataJDBC(dataInicioPagamentoMatricula)).append("' ");
		}
		
		if (dataFimPagamentoMatricula != null) {
			
			sb.append(" and (select negociacaorecebimento.data::DATE from contareceber "); 
			sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = contareceber.codigo");
			sb.append(" inner join negociacaorecebimento on negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento"); 
			sb.append("	where contareceber.matriculaperiodo = matriculaperiodo.codigo"); 
			sb.append("	and tipoorigem = 'MAT'");				
			
			sb.append("union all");
			sb.append(" select min(negociacaorecebimento.data::DATE) as data from contareceber");  
			sb.append(" inner join contarecebernegociado on contarecebernegociado.contareceber = contareceber.codigo"); 
			sb.append(" inner join contareceber cr on contarecebernegociado.negociacaocontareceber::varchar = cr.codorigem and cr.tipoorigem = 'NCR'");
			sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = cr.codigo"); 
			sb.append(" inner join negociacaorecebimento on negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento");	
			sb.append(" where contareceber.matriculaperiodo = matriculaperiodo.codigo and contareceber.situacao = 'NE'	and contareceber.tipoorigem = 'MAT'"); 
			sb.append(" order by data	limit 1	");
			sb.append("	)  <= '");
			sb.append(Uteis.getDataJDBC(dataFimPagamentoMatricula)).append("' ");
			
		}
		
		return sb;		
	}
		
	public StringBuilder sqlConsultarMatriculaConsultorMatTranferidaDe(Integer turmaProcessar, StringBuilder filtroUnidadeIN, StringBuilder filtroCursoIN, Date dataInicio, Date dataTermino, TurmaVO turma, FuncionarioVO consultor, 
			boolean matRecebida, boolean matAReceber, boolean matVencida, boolean matAVencer, boolean excluida, boolean transferidaDe, boolean transferidaPara, String situacaoProspect, boolean considerarConsultorVinculadoProspect, Date dataInicioPagamentoMatricula, Date dataFimPagamentoMatricula, Boolean considerarApenasTurmaComAlunosMatriculados, boolean matNaoGerada, Boolean trazerAlunosBolsistas) throws Exception {
		StringBuilder sb = new StringBuilder("select distinct funcionario.codigo, pessoa.nome from matriculaperiodo  ");
		sb.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
		if (considerarConsultorVinculadoProspect) {
			sb.append(" left join prospects on prospects.pessoa = matricula.aluno ");
			sb.append(" left join funcionario on funcionario.codigo = prospects.consultorpadrao ");
		} else {
			sb.append(" left join funcionario on funcionario.codigo = matricula.consultor ");
		}
		sb.append(" inner join pessoa on pessoa.codigo = funcionario.pessoa ");
		sb.append(" left join funcionariocargo on funcionariocargo.funcionario = funcionario.codigo ");
		sb.append(" left join cargo on cargo.codigo = funcionariocargo.cargo ");
		if (matAReceber || matRecebida || matVencida || matAVencer || matNaoGerada) {
			sb.append(" left join matriculaperiodovencimento on matriculaperiodo.codigo = matriculaperiodovencimento.matriculaperiodo and (matriculaperiodovencimento.parcela = 'MA' or matriculaperiodovencimento.parcela = 'Matrícula') ");
		}
		sb.append(" where 1=1 ");
		if(!trazerAlunosBolsistas) {
			sb.append(" and matriculaPeriodo.bolsista <> true ");	
		}
		if (matRecebida || matAReceber || matVencida || matAVencer || matNaoGerada) {
			sb.append(" and (");
			if(matNaoGerada) {
				sb.append(" (matriculaperiodovencimento.codigo is null or matriculaperiodovencimento.situacao = 'NG') ");
			}
			if (matAReceber || matVencida || matAVencer || matRecebida) {
				if(matNaoGerada) {
					sb.append(" or " );	
				}
				sb.append("   ( matriculaperiodovencimento.situacao in ('' ");
				if (matRecebida) {
					sb.append(",'GP'");
				}
				if (matAReceber || matVencida || matAVencer) {
					sb.append(",'GE'");
				}		
				sb.append(") ");
				if (matVencida && !matAVencer) {
					sb.append(" and matriculaperiodovencimento.datavencimento < current_date ");
				}
				if (matAVencer && !matVencida) {
					sb.append(" and matriculaperiodovencimento.datavencimento > current_date ");
				}
				sb.append(") ");
			}
					
			sb.append(") ");
		}
		sb.append(" and matriculaperiodo.alunotransferidounidade = true ");			
		if (filtroUnidadeIN.length() > 0) {
			sb.append(" and matricula.unidadeEnsino in (").append(filtroUnidadeIN).append(") ");
		}
		if (filtroCursoIN.length() > 0) {
			sb.append(" and matricula.curso in (").append(filtroCursoIN).append(")");
		}
		if (turma.getCodigo().intValue() > 0) {
			sb.append(" and matriculaPeriodo.turma = ").append(turma.getCodigo().intValue());
		}
		if (consultor.getCodigo().intValue() > 0) {
			sb.append(" and funcionario.codigo = ").append(consultor.getCodigo().intValue());
		}
		if (dataInicio != null) {
			sb.append(" and matriculaPeriodo.data >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
		}
		if (dataTermino != null) {
			sb.append(" and matriculaPeriodo.data <= '").append(Uteis.getDataJDBC(dataTermino)).append("' ");
		}
		
		if (dataInicioPagamentoMatricula != null) {
			
			sb.append(" and (select negociacaorecebimento.data::DATE from contareceber "); 
			sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = contareceber.codigo");
			sb.append(" inner join negociacaorecebimento on negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento"); 
			sb.append("	where contareceber.matriculaperiodo = matriculaperiodo.codigo"); 
			sb.append("	and tipoorigem = 'MAT'");				
			
			sb.append(" union all");
			sb.append(" select min(negociacaorecebimento.data::DATE) as data from contareceber");  
			sb.append(" inner join contarecebernegociado on contarecebernegociado.contareceber = contareceber.codigo"); 
			sb.append(" inner join contareceber cr on contarecebernegociado.negociacaocontareceber::varchar = cr.codorigem and cr.tipoorigem = 'NCR'");
			sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = cr.codigo"); 
			sb.append(" inner join negociacaorecebimento on negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento");	
			sb.append(" where contareceber.matriculaperiodo = matriculaperiodo.codigo and contareceber.situacao = 'NE'	and contareceber.tipoorigem = 'MAT'"); 
			sb.append(" order by data	limit 1	");
			sb.append("	)  >= '");
			sb.append(Uteis.getDataJDBC(dataInicioPagamentoMatricula)).append("' ");
		}
		
		if (dataFimPagamentoMatricula != null) {
			
			sb.append(" and (select negociacaorecebimento.data::DATE from contareceber "); 
			sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = contareceber.codigo");
			sb.append(" inner join negociacaorecebimento on negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento"); 
			sb.append("	where contareceber.matriculaperiodo = matriculaperiodo.codigo"); 
			sb.append("	and tipoorigem = 'MAT'");				
			
			sb.append(" union all");
			sb.append(" select min(negociacaorecebimento.data::DATE) as data from contareceber");  
			sb.append(" inner join contarecebernegociado on contarecebernegociado.contareceber = contareceber.codigo"); 
			sb.append(" inner join contareceber cr on contarecebernegociado.negociacaocontareceber::varchar = cr.codorigem and cr.tipoorigem = 'NCR'");
			sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = cr.codigo"); 
			sb.append(" inner join negociacaorecebimento on negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento");	
			sb.append(" where contareceber.matriculaperiodo = matriculaperiodo.codigo and contareceber.situacao = 'NE'	and contareceber.tipoorigem = 'MAT'"); 
			sb.append(" order by data	limit 1	");
			sb.append("	)  <= '");
			sb.append(Uteis.getDataJDBC(dataFimPagamentoMatricula)).append("' ");
			
		}
		
		return sb;
	}
	
	public StringBuilder sqlConsultarQtdMatTransferidaDe(Integer turmaProcessar, Integer codConsultor, Date dataInicio, Date dataTermino, 
			boolean matRecebida, boolean matAReceber, boolean matVencida, boolean matAVencer, boolean excluida, boolean transferidaDe, boolean transferidaPara, String situacaoProspect, boolean considerarConsultorVinculadoProspect, Date dataInicioPagamentoMatricula, Date dataFimPagamentoMatricula, Boolean considerarApenasTurmaComAlunosMatriculados, boolean matNaoGerada, Boolean trazerAlunosBolsistas) throws Exception {
		StringBuilder sb = new StringBuilder("select count(matricula.matricula) as qtdAluno from matriculaperiodo  ");
		sb.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
		if (considerarConsultorVinculadoProspect) {
			sb.append(" left join prospects on prospects.pessoa = matricula.aluno ");
			sb.append(" left join funcionario on funcionario.codigo = prospects.consultorpadrao ");
		} else {
			sb.append(" left join funcionario on funcionario.codigo = matricula.consultor ");
		}
		sb.append(" left join pessoa on pessoa.codigo = funcionario.pessoa ");
		if (matAReceber || matRecebida || matVencida || matAVencer || matNaoGerada) {
			sb.append(" left join matriculaperiodovencimento on matriculaperiodo.codigo = matriculaperiodovencimento.matriculaperiodo and (matriculaperiodovencimento.parcela = 'MA' or matriculaperiodovencimento.parcela = 'Matrícula') ");
		}
		sb.append(" where 1=1 ");
		if(!trazerAlunosBolsistas) {
			sb.append(" and matriculaPeriodo.bolsista <> true ");	
		}
		if (matRecebida || matAReceber || matVencida || matAVencer || matNaoGerada) {
			sb.append(" and (");
			if(matNaoGerada) {
				sb.append(" (matriculaperiodovencimento.codigo is null or matriculaperiodovencimento.situacao = 'NG') ");
			}
			if (matAReceber || matVencida || matAVencer || matRecebida) {
				if(matNaoGerada) {
					sb.append(" or " );	
				}
				sb.append("   ( matriculaperiodovencimento.situacao in ('' ");
				if (matRecebida) {
					sb.append(",'GP'");
				}
				if (matAReceber || matVencida || matAVencer) {
					sb.append(",'GE'");
				}		
				sb.append(") ");
				if (matVencida && !matAVencer) {
					sb.append(" and matriculaperiodovencimento.datavencimento < current_date ");
				}
				if (matAVencer && !matVencida) {
					sb.append(" and matriculaperiodovencimento.datavencimento > current_date ");
				}
				sb.append(") ");
			}
					
			sb.append(") ");
		}
		sb.append(" and matriculaperiodo.alunotransferidounidade = true ");
		sb.append(" and matriculaPeriodo.turma = ").append(turmaProcessar);
		
		if(codConsultor == -1) {
			sb.append(" and funcionario.codigo is null ");
		}else {
			sb.append(" and funcionario.codigo = ").append(codConsultor);
		}
		
		if (dataInicio != null) {
			sb.append(" and matriculaPeriodo.data >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
		}
		if (dataTermino != null) {
			sb.append(" and matriculaPeriodo.data <= '").append(Uteis.getDataJDBC(dataTermino)).append("' ");
		}
		
		if (dataInicioPagamentoMatricula != null) {
			
			sb.append(" and (select negociacaorecebimento.data::DATE from contareceber "); 
			sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = contareceber.codigo");
			sb.append(" inner join negociacaorecebimento on negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento"); 
			sb.append("	where contareceber.matriculaperiodo = matriculaperiodo.codigo"); 
			sb.append("	and tipoorigem = 'MAT'");				
			
			sb.append("union all");
			sb.append(" select min(negociacaorecebimento.data::DATE) as data from contareceber");  
			sb.append(" inner join contarecebernegociado on contarecebernegociado.contareceber = contareceber.codigo"); 
			sb.append(" inner join contareceber cr on contarecebernegociado.negociacaocontareceber::varchar = cr.codorigem and cr.tipoorigem = 'NCR'");
			sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = cr.codigo"); 
			sb.append(" inner join negociacaorecebimento on negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento");	
			sb.append(" where contareceber.matriculaperiodo = matriculaperiodo.codigo and contareceber.situacao = 'NE'	and contareceber.tipoorigem = 'MAT'"); 
			sb.append(" order by data	limit 1	");
			sb.append("	)  >= '");
			sb.append(Uteis.getDataJDBC(dataInicioPagamentoMatricula)).append("' ");
		}
		
		if (dataFimPagamentoMatricula != null) {
			
			sb.append(" and (select negociacaorecebimento.data::DATE from contareceber "); 
			sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = contareceber.codigo");
			sb.append(" inner join negociacaorecebimento on negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento"); 
			sb.append("	where contareceber.matriculaperiodo = matriculaperiodo.codigo"); 
			sb.append("	and tipoorigem = 'MAT'");				
			
			sb.append("union all");
			sb.append(" select min(negociacaorecebimento.data::DATE) as data from contareceber");  
			sb.append(" inner join contarecebernegociado on contarecebernegociado.contareceber = contareceber.codigo"); 
			sb.append(" inner join contareceber cr on contarecebernegociado.negociacaocontareceber::varchar = cr.codorigem and cr.tipoorigem = 'NCR'");
			sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = cr.codigo"); 
			sb.append(" inner join negociacaorecebimento on negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento");	
			sb.append(" where contareceber.matriculaperiodo = matriculaperiodo.codigo and contareceber.situacao = 'NE'	and contareceber.tipoorigem = 'MAT'"); 
			sb.append(" order by data	limit 1	");
			sb.append("	)  <= '");
			sb.append(Uteis.getDataJDBC(dataFimPagamentoMatricula)).append("' ");
		}
		
		return sb;
	}	
	public StringBuilder sqlConsultorMatriculaTurmaMatTransferidaPara(StringBuilder filtroUnidadeIN, StringBuilder filtroCursoIN, Date dataInicio, Date dataTermino, StringBuilder filtroTurmaIN, FuncionarioVO consultor, 
			boolean matRecebida, boolean matAReceber, boolean matVencida, boolean matAVencer, boolean excluida, boolean transferidaDe, boolean transferidaPara, String situacaoProspect, boolean considerarConsultorVinculadoProspect,  Date dataInicioPagamentoMatricula, Date dataFimPagamentoMatricula, Boolean considerarApenasTurmaComAlunosMatriculados, boolean matNaoGerada, Boolean trazerAlunosBolsistas) throws Exception {
		StringBuilder sb = new StringBuilder("select distinct matriculaperiodo.turma, turma.identificadorturma, turma.unidadeensino from matriculaperiodo  ");
		sb.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
		sb.append(" inner join turma on turma.codigo = matriculaperiodo.turma ");
		if (considerarConsultorVinculadoProspect) {
			sb.append(" left join prospects on prospects.pessoa = matricula.aluno ");
			if(considerarApenasTurmaComAlunosMatriculados) {
				sb.append(" inner join funcionario on funcionario.codigo = prospects.consultorpadrao ");
			}
			else {
				sb.append(" left join funcionario on funcionario.codigo = prospects.consultorpadrao ");
			}
		} else {
			if(considerarApenasTurmaComAlunosMatriculados) {
				sb.append(" inner join funcionario on funcionario.codigo = matricula.consultor ");
			}
			else {
				sb.append(" left join funcionario on funcionario.codigo = matricula.consultor ");
			}
		}
		sb.append(" left join pessoa on pessoa.codigo = funcionario.pessoa ");
		if (matAReceber || matRecebida || matVencida || matAVencer || matNaoGerada) {
			sb.append(" left join matriculaperiodovencimento on matriculaperiodo.codigo = matriculaperiodovencimento.matriculaperiodo and (matriculaperiodovencimento.parcela = 'MA' or matriculaperiodovencimento.parcela = 'Matrícula') ");
		}
		sb.append(" where 1=1 ");
		if(!trazerAlunosBolsistas) {
			sb.append(" and matriculaPeriodo.bolsista <> true ");	
		}
		if (matRecebida || matAReceber || matVencida || matAVencer || matNaoGerada) {
			sb.append(" and (");
			if(matNaoGerada) {
				sb.append(" (matriculaperiodovencimento.codigo is null or matriculaperiodovencimento.situacao = 'NG') ");
			}
			if (matAReceber || matVencida || matAVencer || matRecebida) {
				if(matNaoGerada) {
					sb.append(" or " );	
				}
				sb.append("   ( matriculaperiodovencimento.situacao in ('' ");
				if (matRecebida) {
					sb.append(",'GP'");
				}
				if (matAReceber || matVencida || matAVencer) {
					sb.append(",'GE'");
				}		
				sb.append(") ");
				if (matVencida && !matAVencer) {
					sb.append(" and matriculaperiodovencimento.datavencimento < current_date ");
				}
				if (matAVencer && !matVencida) {
					sb.append(" and matriculaperiodovencimento.datavencimento > current_date ");
				}
				sb.append(") ");
			}
					
			sb.append(") ");
		}
		sb.append(" and alunotransferidounidade = false ");
		sb.append(" and matricula.aluno in ( select mat.aluno from matriculaperiodo mp ");
		sb.append(" inner join matricula mat on mat.matricula = mp.matricula ");
		sb.append(" where alunotransferidounidade = true ) ");
		if (filtroUnidadeIN.length() > 0) {
			sb.append(" and matricula.unidadeEnsino in (").append(filtroUnidadeIN).append(") ");
		}
		if (filtroCursoIN.length() > 0) {
			sb.append(" and matricula.curso in (").append(filtroCursoIN).append(")");
		}
		if (filtroTurmaIN.length() > 0) {
			sb.append(" and matriculaPeriodo.turma in ( ").append(filtroTurmaIN).append(") ");
		}
		if (consultor.getCodigo().intValue() > 0) {
			sb.append(" and funcionario.codigo = ").append(consultor.getCodigo().intValue());
		}
		if (dataInicio != null) {
			sb.append(" and matriculaPeriodo.data >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
		}
		if (dataTermino != null) {
			sb.append(" and matriculaPeriodo.data <= '").append(Uteis.getDataJDBC(dataTermino)).append("' ");
		}
		
		if (dataInicioPagamentoMatricula != null) {
			
			sb.append(" and (select negociacaorecebimento.data::DATE from contareceber "); 
			sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = contareceber.codigo");
			sb.append(" inner join negociacaorecebimento on negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento"); 
			sb.append("	where contareceber.matriculaperiodo = matriculaperiodo.codigo"); 
			sb.append("	and tipoorigem = 'MAT'");				
			
			sb.append(" union all");
			sb.append(" select min(negociacaorecebimento.data::DATE) as data from contareceber");  
			sb.append(" inner join contarecebernegociado on contarecebernegociado.contareceber = contareceber.codigo"); 
			sb.append(" inner join contareceber cr on contarecebernegociado.negociacaocontareceber::varchar = cr.codorigem and cr.tipoorigem = 'NCR'");
			sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = cr.codigo"); 
			sb.append(" inner join negociacaorecebimento on negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento");	
			sb.append(" where contareceber.matriculaperiodo = matriculaperiodo.codigo and contareceber.situacao = 'NE'	and contareceber.tipoorigem = 'MAT'"); 
			sb.append(" order by data	limit 1	");
			sb.append("	)  >= '");
			sb.append(Uteis.getDataJDBC(dataInicioPagamentoMatricula)).append("' ");
		}
		
		if (dataFimPagamentoMatricula != null) {
			
			sb.append(" and (select negociacaorecebimento.data::DATE from contareceber "); 
			sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = contareceber.codigo");
			sb.append(" inner join negociacaorecebimento on negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento"); 
			sb.append("	where contareceber.matriculaperiodo = matriculaperiodo.codigo"); 
			sb.append("	and tipoorigem = 'MAT'");				
			
			sb.append(" union all");
			sb.append(" select min(negociacaorecebimento.data::DATE) as data from contareceber");  
			sb.append(" inner join contarecebernegociado on contarecebernegociado.contareceber = contareceber.codigo"); 
			sb.append(" inner join contareceber cr on contarecebernegociado.negociacaocontareceber::varchar = cr.codorigem and cr.tipoorigem = 'NCR'");
			sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = cr.codigo"); 
			sb.append(" inner join negociacaorecebimento on negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento");	
			sb.append(" where contareceber.matriculaperiodo = matriculaperiodo.codigo and contareceber.situacao = 'NE'	and contareceber.tipoorigem = 'MAT'"); 
			sb.append(" order by data	limit 1	");
			sb.append("	)  <= '");
			sb.append(Uteis.getDataJDBC(dataFimPagamentoMatricula)).append("' ");
			
		}
		
		return sb;		
	}
	
	public StringBuilder sqlConsultarMatriculaConsultorMatTranferidaPara(Integer turmaProcessar, StringBuilder filtroUnidadeIN, StringBuilder filtroCursoIN, Date dataInicio, Date dataTermino, TurmaVO turma, FuncionarioVO consultor, 
			boolean matRecebida, boolean matAReceber, boolean matVencida, boolean matAVencer, boolean excluida, boolean transferidaDe, boolean transferidaPara, String situacaoProspect, boolean considerarConsultorVinculadoProspect, Date dataInicioPagamentoMatricula, Date dataFimPagamentoMatricula, Boolean considerarApenasTurmaComAlunosMatriculados, boolean matNaoGerada, Boolean trazerAlunosBolsistas) throws Exception {
		StringBuilder sb = new StringBuilder("select distinct funcionario.codigo, pessoa.nome from matriculaperiodo  ");
		sb.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
		if (considerarConsultorVinculadoProspect) {
			sb.append(" left join prospects on prospects.pessoa = matricula.aluno ");
			sb.append(" left join funcionario on funcionario.codigo = prospects.consultorpadrao ");
		} else {
			sb.append(" left join funcionario on funcionario.codigo = matricula.consultor ");
		}
		sb.append(" inner join pessoa on pessoa.codigo = funcionario.pessoa ");
		sb.append(" left join funcionariocargo on funcionariocargo.funcionario = funcionario.codigo ");
		sb.append(" left join cargo on cargo.codigo = funcionariocargo.cargo ");
		if (matAReceber || matRecebida || matVencida || matAVencer || matNaoGerada) {
			sb.append(" left join matriculaperiodovencimento on matriculaperiodo.codigo = matriculaperiodovencimento.matriculaperiodo and (matriculaperiodovencimento.parcela = 'MA' or matriculaperiodovencimento.parcela = 'Matrícula') ");
		}
		sb.append(" where 1=1 ");
		if(!trazerAlunosBolsistas) {
			sb.append(" and matriculaPeriodo.bolsista <> true ");	
		}
		if (matRecebida || matAReceber || matVencida || matAVencer || matNaoGerada) {
			sb.append(" and (");
			if(matNaoGerada) {
				sb.append(" (matriculaperiodovencimento.codigo is null or matriculaperiodovencimento.situacao = 'NG') ");
			}
			if (matAReceber || matVencida || matAVencer || matRecebida) {
				if(matNaoGerada) {
					sb.append(" or " );	
				}
				sb.append("   ( matriculaperiodovencimento.situacao in ('' ");
				if (matRecebida) {
					sb.append(",'GP'");
				}
				if (matAReceber || matVencida || matAVencer) {
					sb.append(",'GE'");
				}		
				sb.append(") ");
				if (matVencida && !matAVencer) {
					sb.append(" and matriculaperiodovencimento.datavencimento < current_date ");
				}
				if (matAVencer && !matVencida) {
					sb.append(" and matriculaperiodovencimento.datavencimento > current_date ");
				}
				sb.append(") ");
			}
					
			sb.append(") ");
		}
		sb.append(" and alunotransferidounidade = false ");
		sb.append(" and matricula.aluno in ( select mat.aluno from matriculaperiodo mp ");
		sb.append(" inner join matricula mat on mat.matricula = mp.matricula ");
		sb.append(" where alunotransferidounidade = true ) ");
		if (filtroUnidadeIN.length() > 0) {
			sb.append(" and matricula.unidadeEnsino in (").append(filtroUnidadeIN).append(") ");
		}
		if (filtroCursoIN.length() > 0) {
			sb.append(" and matricula.curso in (").append(filtroCursoIN).append(")");
		}
		if (turma.getCodigo().intValue() > 0) {
			sb.append(" and matriculaPeriodo.turma = ").append(turma.getCodigo().intValue());
		}
		if (consultor.getCodigo().intValue() > 0) {
			sb.append(" and funcionario.codigo = ").append(consultor.getCodigo().intValue());
		}
		if (dataInicio != null) {
			sb.append(" and matriculaPeriodo.data >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
		}
		if (dataTermino != null) {
			sb.append(" and matriculaPeriodo.data <= '").append(Uteis.getDataJDBC(dataTermino)).append("' ");
		}
		
		if (dataInicioPagamentoMatricula != null) {
			
			sb.append(" and (select negociacaorecebimento.data::DATE from contareceber "); 
			sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = contareceber.codigo");
			sb.append(" inner join negociacaorecebimento on negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento"); 
			sb.append("	where contareceber.matriculaperiodo = matriculaperiodo.codigo"); 
			sb.append("	and tipoorigem = 'MAT'");				
			
			sb.append(" union all");
			sb.append(" select min(negociacaorecebimento.data::DATE) as data from contareceber");  
			sb.append(" inner join contarecebernegociado on contarecebernegociado.contareceber = contareceber.codigo"); 
			sb.append(" inner join contareceber cr on contarecebernegociado.negociacaocontareceber::varchar = cr.codorigem and cr.tipoorigem = 'NCR'");
			sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = cr.codigo"); 
			sb.append(" inner join negociacaorecebimento on negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento");	
			sb.append(" where contareceber.matriculaperiodo = matriculaperiodo.codigo and contareceber.situacao = 'NE'	and contareceber.tipoorigem = 'MAT'"); 
			sb.append(" order by data	limit 1	");
			sb.append("	)  >= '");
			sb.append(Uteis.getDataJDBC(dataInicioPagamentoMatricula)).append("' ");
		}
		
		if (dataFimPagamentoMatricula != null) {
			
			sb.append(" and (select negociacaorecebimento.data::DATE from contareceber "); 
			sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = contareceber.codigo");
			sb.append(" inner join negociacaorecebimento on negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento"); 
			sb.append("	where contareceber.matriculaperiodo = matriculaperiodo.codigo"); 
			sb.append("	and tipoorigem = 'MAT'");				
			
			sb.append(" union all");
			sb.append(" select min(negociacaorecebimento.data::DATE) as data from contareceber");  
			sb.append(" inner join contarecebernegociado on contarecebernegociado.contareceber = contareceber.codigo"); 
			sb.append(" inner join contareceber cr on contarecebernegociado.negociacaocontareceber::varchar = cr.codorigem and cr.tipoorigem = 'NCR'");
			sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = cr.codigo"); 
			sb.append(" inner join negociacaorecebimento on negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento");	
			sb.append(" where contareceber.matriculaperiodo = matriculaperiodo.codigo and contareceber.situacao = 'NE'	and contareceber.tipoorigem = 'MAT'"); 
			sb.append(" order by data	limit 1	");
			sb.append("	)  <= '");
			sb.append(Uteis.getDataJDBC(dataFimPagamentoMatricula)).append("' ");
			
		}
		
		return sb;
	}
	

	public StringBuilder sqlConsultarQtdMatTransferidaPara(Integer turmaProcessar, Integer codConsultor, Date dataInicio, Date dataTermino, 
			boolean matRecebida, boolean matAReceber, boolean matVencida, boolean matAVencer, boolean excluida, boolean transferidaDe, boolean transferidaPara, String situacaoProspect, boolean considerarConsultorVinculadoProspect, Date dataInicioPagamentoMatricula, Date dataFimPagamentoMatricula, Boolean considerarApenasTurmaComAlunosMatriculados, boolean matNaoGerada, Boolean trazerAlunosBolsistas) throws Exception {
		StringBuilder sb = new StringBuilder("select count(matricula.matricula) as qtdAluno from matriculaperiodo  ");
		sb.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
		if (considerarConsultorVinculadoProspect) {
			sb.append(" left join prospects on prospects.pessoa = matricula.aluno ");
			sb.append(" left join funcionario on funcionario.codigo = prospects.consultorpadrao ");
		} else {
			sb.append(" left join funcionario on funcionario.codigo = matricula.consultor ");
		}
		sb.append(" left join pessoa on pessoa.codigo = funcionario.pessoa ");
		if (matAReceber || matRecebida || matVencida || matAVencer || matNaoGerada) {
			sb.append(" left join matriculaperiodovencimento on matriculaperiodo.codigo = matriculaperiodovencimento.matriculaperiodo and (matriculaperiodovencimento.parcela = 'MA' or matriculaperiodovencimento.parcela = 'Matrícula') ");
		}
		sb.append(" where 1=1 ");
		if(!trazerAlunosBolsistas) {
			sb.append(" and matriculaPeriodo.bolsista <> true ");	
		}
		if (matRecebida || matAReceber || matVencida || matAVencer || matNaoGerada) {
			sb.append(" and (");
			if(matNaoGerada) {
				sb.append(" (matriculaperiodovencimento.codigo is null or matriculaperiodovencimento.situacao = 'NG') ");
			}
			if (matAReceber || matVencida || matAVencer || matRecebida) {
				if(matNaoGerada) {
					sb.append(" or " );	
				}
				sb.append("   ( matriculaperiodovencimento.situacao in ('' ");
				if (matRecebida) {
					sb.append(",'GP'");
				}
				if (matAReceber || matVencida || matAVencer) {
					sb.append(",'GE'");
				}		
				sb.append(") ");
				if (matVencida && !matAVencer) {
					sb.append(" and matriculaperiodovencimento.datavencimento < current_date ");
				}
				if (matAVencer && !matVencida) {
					sb.append(" and matriculaperiodovencimento.datavencimento > current_date ");
				}
				sb.append(") ");
			}
					
			sb.append(") ");
		}
		sb.append(" and alunotransferidounidade = false ");
		sb.append(" and matricula.aluno in ( select mat.aluno from matriculaperiodo mp ");
		sb.append(" inner join matricula mat on mat.matricula = mp.matricula ");
		sb.append(" where alunotransferidounidade = true ) ");
		sb.append(" and matriculaPeriodo.turma = ").append(turmaProcessar);
		
		if(codConsultor == -1) {
			sb.append(" and funcionario.codigo is null ");
		}else {
			sb.append(" and funcionario.codigo = ").append(codConsultor);
		}
		
		if (dataInicio != null) {
			sb.append(" and matriculaPeriodo.data >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
		}
		if (dataTermino != null) {
			sb.append(" and matriculaPeriodo.data <= '").append(Uteis.getDataJDBC(dataTermino)).append("' ");
		}
		
		if (dataInicioPagamentoMatricula != null) {
			
			sb.append(" and (select negociacaorecebimento.data::DATE from contareceber "); 
			sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = contareceber.codigo");
			sb.append(" inner join negociacaorecebimento on negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento"); 
			sb.append("	where contareceber.matriculaperiodo = matriculaperiodo.codigo"); 
			sb.append("	and tipoorigem = 'MAT'");				
			
			sb.append(" union all");
			sb.append(" select min(negociacaorecebimento.data::DATE) as data from contareceber");  
			sb.append(" inner join contarecebernegociado on contarecebernegociado.contareceber = contareceber.codigo"); 
			sb.append(" inner join contareceber cr on contarecebernegociado.negociacaocontareceber::varchar = cr.codorigem and cr.tipoorigem = 'NCR'");
			sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = cr.codigo"); 
			sb.append(" inner join negociacaorecebimento on negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento");	
			sb.append(" where contareceber.matriculaperiodo = matriculaperiodo.codigo and contareceber.situacao = 'NE'	and contareceber.tipoorigem = 'MAT'"); 
			sb.append(" order by data	limit 1	");
			sb.append("	)  >= '");
			sb.append(Uteis.getDataJDBC(dataInicioPagamentoMatricula)).append("' ");
		}
		
		if (dataFimPagamentoMatricula != null) {
			
			sb.append(" and (select negociacaorecebimento.data::DATE from contareceber "); 
			sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = contareceber.codigo");
			sb.append(" inner join negociacaorecebimento on negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento"); 
			sb.append("	where contareceber.matriculaperiodo = matriculaperiodo.codigo"); 
			sb.append("	and tipoorigem = 'MAT'");				
			
			sb.append(" union all");
			sb.append(" select min(negociacaorecebimento.data::DATE) as data from contareceber");  
			sb.append(" inner join contarecebernegociado on contarecebernegociado.contareceber = contareceber.codigo"); 
			sb.append(" inner join contareceber cr on contarecebernegociado.negociacaocontareceber::varchar = cr.codorigem and cr.tipoorigem = 'NCR'");
			sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = cr.codigo"); 
			sb.append(" inner join negociacaorecebimento on negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento");	
			sb.append(" where contareceber.matriculaperiodo = matriculaperiodo.codigo and contareceber.situacao = 'NE'	and contareceber.tipoorigem = 'MAT'"); 
			sb.append(" order by data	limit 1	");
			sb.append("	)  <= '");
			sb.append(Uteis.getDataJDBC(dataFimPagamentoMatricula)).append("' ");
		}		
		
		return sb;
	}
	

	
	public StringBuilder sqlConsultorMatriculaTurmaMatAReceber(UnidadeEnsinoVO unidadeEnsino, Date dataInicio, Date dataTermino, CursoVO curso, TurmaVO turma, FuncionarioVO consultor, 
			boolean matRecebida, boolean matAReceber, boolean matVencida, boolean matAVencer, boolean ativa, boolean preMatricula, boolean cancelada, boolean excluida, boolean transferidaDe, boolean transferidaPara, String situacaoProspect) throws Exception {
		StringBuilder sb = new StringBuilder("select distinct matriculaperiodo.turma, turma.identificadorturma from matriculaperiodo  ");
		sb.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
		sb.append(" inner join matriculaperiodovencimento on matriculaperiodo.codigo = matriculaperiodovencimento.matriculaperiodo ");
		sb.append(" inner join turma on turma.codigo = matriculaperiodo.turma   ");
		sb.append(" left join funcionario on funcionario.codigo = matricula.consultor  ");
		sb.append(" left join pessoa on pessoa.codigo = funcionario.pessoa  ");
		sb.append(" where 1=1 ");
		sb.append(" and (matriculaperiodovencimento.parcela = 'MA' or matriculaperiodovencimento.parcela = 'Matrícula') and matriculaperiodovencimento.situacao = 'GE' ");
		if (unidadeEnsino.getCodigo().intValue() > 0) {
			sb.append(" and matricula.unidadeEnsino = ").append(unidadeEnsino.getCodigo().intValue());
		}
		if (curso.getCodigo().intValue() > 0) {
			sb.append(" and matricula.curso = ").append(curso.getCodigo().intValue());
		}
		if (turma.getCodigo().intValue() > 0) {
			sb.append(" and matriculaPeriodo.turma = ").append(turma.getCodigo().intValue());
		}
		if (consultor.getCodigo().intValue() > 0) {
			sb.append(" and funcionario.codigo = ").append(consultor.getCodigo().intValue());
		}
		if (dataInicio != null) {
			sb.append(" and matriculaPeriodo.data >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
		}
		if (dataTermino != null) {
			sb.append(" and matriculaPeriodo.data <= '").append(Uteis.getDataJDBC(dataTermino)).append("' ");
		}
		return sb;		
	}
		
	public StringBuilder sqlConsultarQtdMatAReceber(Integer turmaProcessar, Integer codConsultor, Date dataInicio, Date dataTermino, 
			boolean matRecebida, boolean matAReceber, boolean matVencida, boolean matAVencer, boolean ativa, boolean preMatricula, boolean cancelada, boolean excluida, boolean transferidaDe, boolean transferidaPara, String situacaoProspect) throws Exception {
		StringBuilder sb = new StringBuilder("select count(matricula.matricula) as qtdAluno from matriculaperiodo  ");
		sb.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
		sb.append(" left join funcionario on funcionario.codigo = matricula.consultor ");
		sb.append(" left join pessoa on pessoa.codigo = funcionario.pessoa ");
		sb.append(" where 1=1 ");
		sb.append(" and alunotransferidounidade = false ");
		sb.append(" and matricula.aluno in ( select mat.aluno from matriculaperiodo mp ");
		sb.append(" inner join matricula mat on mat.matricula = mp.matricula ");
		sb.append(" where alunotransferidounidade = true ) ");
		sb.append(" and matriculaPeriodo.turma = ").append(turmaProcessar);
		sb.append(" and funcionario.codigo = ").append(codConsultor);
		if (dataInicio != null) {
			sb.append(" and matriculaPeriodo.data >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
		}
		if (dataTermino != null) {
			sb.append(" and matriculaPeriodo.data <= '").append(Uteis.getDataJDBC(dataTermino)).append("' ");
		}
		return sb;
	}
	
	public StringBuilder sqlConsultarMatriculaConsultorMatAReceber(Integer turmaProcessar, UnidadeEnsinoVO unidadeEnsino, Date dataInicio, Date dataTermino, CursoVO curso, TurmaVO turma, FuncionarioVO consultor, 
			boolean matRecebida, boolean matAReceber, boolean matVencida, boolean matAVencer, boolean ativa, boolean preMatricula, boolean cancelada, boolean excluida, boolean transferidaDe, boolean transferidaPara, String situacaoProspect) throws Exception {
		StringBuilder sb = new StringBuilder("select distinct funcionario.codigo, pessoa.nome from matriculaperiodo  ");
		sb.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
		sb.append(" inner join funcionario on funcionario.codigo = matricula.consultor ");
		sb.append(" inner join pessoa on pessoa.codigo = funcionario.pessoa ");
		sb.append(" left join funcionariocargo on funcionariocargo.funcionario = funcionario.codigo ");
		sb.append(" left join cargo on cargo.codigo = funcionariocargo.cargo ");
		sb.append(" where 1=1 ");
		sb.append(" and alunotransferidounidade = false ");
		sb.append(" and matricula.aluno in ( select mat.aluno from matriculaperiodo mp ");
		sb.append(" inner join matricula mat on mat.matricula = mp.matricula ");
		sb.append(" where alunotransferidounidade = true ) ");
		if (unidadeEnsino.getCodigo().intValue() > 0) {
			sb.append(" and matricula.unidadeEnsino = ").append(unidadeEnsino.getCodigo().intValue());
		}
		if (curso.getCodigo().intValue() > 0) {
			sb.append(" and matricula.curso = ").append(curso.getCodigo().intValue());
		}
		if (turma.getCodigo().intValue() > 0) {
			sb.append(" and matriculaPeriodo.turma = ").append(turma.getCodigo().intValue());
		}
		if (consultor.getCodigo().intValue() > 0) {
			sb.append(" and funcionario.codigo = ").append(consultor.getCodigo().intValue());
		}
		if (dataInicio != null) {
			sb.append(" and matriculaPeriodo.data >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
		}
		if (dataTermino != null) {
			sb.append(" and matriculaPeriodo.data <= '").append(Uteis.getDataJDBC(dataTermino)).append("' ");
		}
		return sb;
	}
	
	public StringBuilder sqlConsultorMatriculaTurmaMatVencida(UnidadeEnsinoVO unidadeEnsino, Date dataInicio, Date dataTermino, CursoVO curso, TurmaVO turma, FuncionarioVO consultor, 
			boolean matRecebida, boolean matAReceber, boolean matVencida, boolean matAVencer, boolean ativa, boolean preMatricula, boolean cancelada, boolean excluida, boolean transferidaDe, boolean transferidaPara, String situacaoProspect) throws Exception {
		StringBuilder sb = new StringBuilder("select distinct matriculaperiodo.turma, turma.identificadorturma from matriculaperiodo  ");
		sb.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
		sb.append(" inner join turma on turma.codigo = matriculaperiodo.turma ");
		sb.append(" left join funcionario on funcionario.codigo = matricula.consultor ");
		sb.append(" left join pessoa on pessoa.codigo = funcionario.pessoa ");
		sb.append(" where 1=1 ");
		sb.append(" and alunotransferidounidade = false ");
		sb.append(" and matricula.aluno in ( select mat.aluno from matriculaperiodo mp ");
		sb.append(" inner join matricula mat on mat.matricula = mp.matricula ");
		sb.append(" where alunotransferidounidade = true ) ");
		if (unidadeEnsino.getCodigo().intValue() > 0) {
			sb.append(" and matricula.unidadeEnsino = ").append(unidadeEnsino.getCodigo().intValue());
		}
		if (curso.getCodigo().intValue() > 0) {
			sb.append(" and matricula.curso = ").append(curso.getCodigo().intValue());
		}
		if (turma.getCodigo().intValue() > 0) {
			sb.append(" and matriculaPeriodo.turma = ").append(turma.getCodigo().intValue());
		}
		if (consultor.getCodigo().intValue() > 0) {
			sb.append(" and funcionario.codigo = ").append(consultor.getCodigo().intValue());
		}
		if (dataInicio != null) {
			sb.append(" and matriculaPeriodo.data >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
		}
		if (dataTermino != null) {
			sb.append(" and matriculaPeriodo.data <= '").append(Uteis.getDataJDBC(dataTermino)).append("' ");
		}
		return sb;		
	}
	
	public StringBuilder sqlConsultarQtdMatVencida(Integer turmaProcessar, Integer codConsultor, Date dataInicio, Date dataTermino, 
			boolean matRecebida, boolean matAReceber, boolean matVencida, boolean matAVencer, boolean ativa, boolean preMatricula, boolean cancelada, boolean excluida, boolean transferidaDe, boolean transferidaPara, String situacaoProspect) throws Exception {
		StringBuilder sb = new StringBuilder("select count(matricula.matricula) as qtdAluno from matriculaperiodo  ");
		sb.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
		sb.append(" left join funcionario on funcionario.codigo = matricula.consultor ");
		sb.append(" left join pessoa on pessoa.codigo = funcionario.pessoa ");
		sb.append(" where 1=1 ");
		sb.append(" and alunotransferidounidade = false ");
		sb.append(" and matricula.aluno in ( select mat.aluno from matriculaperiodo mp ");
		sb.append(" inner join matricula mat on mat.matricula = mp.matricula ");
		sb.append(" where alunotransferidounidade = true ) ");
		sb.append(" and matriculaPeriodo.turma = ").append(turmaProcessar);
		sb.append(" and funcionario.codigo = ").append(codConsultor);
		if (dataInicio != null) {
			sb.append(" and matriculaPeriodo.data >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
		}
		if (dataTermino != null) {
			sb.append(" and matriculaPeriodo.data <= '").append(Uteis.getDataJDBC(dataTermino)).append("' ");
		}
		return sb;
	}
	
	public StringBuilder sqlConsultarMatriculaConsultorMatVencida(Integer turmaProcessar, UnidadeEnsinoVO unidadeEnsino, Date dataInicio, Date dataTermino, CursoVO curso, TurmaVO turma, FuncionarioVO consultor, 
			boolean matRecebida, boolean matAReceber, boolean matVencida, boolean matAVencer, boolean ativa, boolean preMatricula, boolean cancelada, boolean excluida, boolean transferidaDe, boolean transferidaPara, String situacaoProspect) throws Exception {
		StringBuilder sb = new StringBuilder("select distinct funcionario.codigo, pessoa.nome from matriculaperiodo  ");
		sb.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
		sb.append(" inner join funcionario on funcionario.codigo = matricula.consultor ");
		sb.append(" inner join pessoa on pessoa.codigo = funcionario.pessoa ");
		sb.append(" left join funcionariocargo on funcionariocargo.funcionario = funcionario.codigo ");
		sb.append(" left join cargo on cargo.codigo = funcionariocargo.cargo ");
		sb.append(" where 1=1 ");
		sb.append(" and alunotransferidounidade = false ");
		sb.append(" and matricula.aluno in ( select mat.aluno from matriculaperiodo mp ");
		sb.append(" inner join matricula mat on mat.matricula = mp.matricula ");
		sb.append(" where alunotransferidounidade = true ) ");
		if (unidadeEnsino.getCodigo().intValue() > 0) {
			sb.append(" and matricula.unidadeEnsino = ").append(unidadeEnsino.getCodigo().intValue());
		}
		if (curso.getCodigo().intValue() > 0) {
			sb.append(" and matricula.curso = ").append(curso.getCodigo().intValue());
		}
		if (turma.getCodigo().intValue() > 0) {
			sb.append(" and matriculaPeriodo.turma = ").append(turma.getCodigo().intValue());
		}
		if (consultor.getCodigo().intValue() > 0) {
			sb.append(" and funcionario.codigo = ").append(consultor.getCodigo().intValue());
		}
		if (dataInicio != null) {
			sb.append(" and matriculaPeriodo.data >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
		}
		if (dataTermino != null) {
			sb.append(" and matriculaPeriodo.data <= '").append(Uteis.getDataJDBC(dataTermino)).append("' ");
		}
		return sb;
	}

	public StringBuilder sqlConsultorMatriculaTurmaMatRecebida(UnidadeEnsinoVO unidadeEnsino, Date dataInicio, Date dataTermino, CursoVO curso, TurmaVO turma, FuncionarioVO consultor, 
			boolean matRecebida, boolean matAReceber, boolean matVencida, boolean matAVencer, boolean ativa, boolean preMatricula, boolean cancelada, boolean excluida, boolean transferidaDe, boolean transferidaPara, String situacaoProspect) throws Exception {
		StringBuilder sb = new StringBuilder("select distinct matriculaperiodo.turma, turma.identificadorturma from matriculaperiodo  ");
		sb.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
		sb.append(" inner join turma on turma.codigo = matriculaperiodo.turma ");
		sb.append(" left join funcionario on funcionario.codigo = matricula.consultor ");
		sb.append(" left join pessoa on pessoa.codigo = funcionario.pessoa ");
		sb.append(" where 1=1 ");
		sb.append(" and alunotransferidounidade = false ");
		sb.append(" and matricula.aluno in ( select mat.aluno from matriculaperiodo mp ");
		sb.append(" inner join matricula mat on mat.matricula = mp.matricula ");
		sb.append(" where alunotransferidounidade = true ) ");
		if (unidadeEnsino.getCodigo().intValue() > 0) {
			sb.append(" and matricula.unidadeEnsino = ").append(unidadeEnsino.getCodigo().intValue());
		}
		if (curso.getCodigo().intValue() > 0) {
			sb.append(" and matricula.curso = ").append(curso.getCodigo().intValue());
		}
		if (turma.getCodigo().intValue() > 0) {
			sb.append(" and matriculaPeriodo.turma = ").append(turma.getCodigo().intValue());
		}
		if (consultor.getCodigo().intValue() > 0) {
			sb.append(" and funcionario.codigo = ").append(consultor.getCodigo().intValue());
		}
		if (dataInicio != null) {
			sb.append(" and matriculaPeriodo.data >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
		}
		if (dataTermino != null) {
			sb.append(" and matriculaPeriodo.data <= '").append(Uteis.getDataJDBC(dataTermino)).append("' ");
		}
		return sb;		
	}
	
		
	public StringBuilder sqlConsultarMatriculaConsultorMatRecebida(Integer turmaProcessar, UnidadeEnsinoVO unidadeEnsino, Date dataInicio, Date dataTermino, CursoVO curso, TurmaVO turma, FuncionarioVO consultor, 
			boolean matRecebida, boolean matAReceber, boolean matVencida, boolean matAVencer, boolean ativa, boolean preMatricula, boolean cancelada, boolean excluida, boolean transferidaDe, boolean transferidaPara, String situacaoProspect) throws Exception {
		StringBuilder sb = new StringBuilder("select distinct funcionario.codigo, pessoa.nome from matriculaperiodo  ");
		sb.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
		sb.append(" inner join funcionario on funcionario.codigo = matricula.consultor ");
		sb.append(" inner join pessoa on pessoa.codigo = funcionario.pessoa ");
		sb.append(" left join funcionariocargo on funcionariocargo.funcionario = funcionario.codigo ");
		sb.append(" left join cargo on cargo.codigo = funcionariocargo.cargo ");
		sb.append(" where 1=1 ");
		sb.append(" and alunotransferidounidade = false ");
		sb.append(" and matricula.aluno in ( select mat.aluno from matriculaperiodo mp ");
		sb.append(" inner join matricula mat on mat.matricula = mp.matricula ");
		sb.append(" where alunotransferidounidade = true ) ");
		if (unidadeEnsino.getCodigo().intValue() > 0) {
			sb.append(" and matricula.unidadeEnsino = ").append(unidadeEnsino.getCodigo().intValue());
		}
		if (curso.getCodigo().intValue() > 0) {
			sb.append(" and matricula.curso = ").append(curso.getCodigo().intValue());
		}
		if (turma.getCodigo().intValue() > 0) {
			sb.append(" and matriculaPeriodo.turma = ").append(turma.getCodigo().intValue());
		}
		if (consultor.getCodigo().intValue() > 0) {
			sb.append(" and funcionario.codigo = ").append(consultor.getCodigo().intValue());
		}
		if (dataInicio != null) {
			sb.append(" and matriculaPeriodo.data >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
		}
		if (dataTermino != null) {
			sb.append(" and matriculaPeriodo.data <= '").append(Uteis.getDataJDBC(dataTermino)).append("' ");
		}
		return sb;
	}
	
	public StringBuilder sqlConsultarQtdMatRecebida(Integer turmaProcessar, Integer codConsultor, Date dataInicio, Date dataTermino, 
			boolean matRecebida, boolean matAReceber, boolean matVencida, boolean matAVencer, boolean ativa, boolean preMatricula, boolean cancelada, boolean excluida, boolean transferidaDe, boolean transferidaPara, String situacaoProspect) throws Exception {
		StringBuilder sb = new StringBuilder("select count(matricula.matricula) as qtdAluno from matriculaperiodo  ");
		sb.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
		sb.append(" left join funcionario on funcionario.codigo = matricula.consultor ");
		sb.append(" left join pessoa on pessoa.codigo = funcionario.pessoa ");
		sb.append(" where 1=1 ");
		sb.append(" and alunotransferidounidade = false ");
		sb.append(" and matricula.aluno in ( select mat.aluno from matriculaperiodo mp ");
		sb.append(" inner join matricula mat on mat.matricula = mp.matricula ");
		sb.append(" where alunotransferidounidade = true ) ");
		sb.append(" and matriculaPeriodo.turma = ").append(turmaProcessar);
		sb.append(" and funcionario.codigo = ").append(codConsultor);
		if (dataInicio != null) {
			sb.append(" and matriculaPeriodo.data >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
		}
		if (dataTermino != null) {
			sb.append(" and matriculaPeriodo.data <= '").append(Uteis.getDataJDBC(dataTermino)).append("' ");
		}
		return sb;
	}	

	public StringBuilder sqlConsultarMatriculaConsultorMatAVencer(Integer turmaProcessar, UnidadeEnsinoVO unidadeEnsino, Date dataInicio, Date dataTermino, CursoVO curso, TurmaVO turma, FuncionarioVO consultor, 
			boolean matRecebida, boolean matAReceber, boolean matVencida, boolean matAVencer, boolean ativa, boolean preMatricula, boolean cancelada, boolean excluida, boolean transferidaDe, boolean transferidaPara, String situacaoProspect) throws Exception {
		StringBuilder sb = new StringBuilder("select distinct funcionario.codigo, pessoa.nome from matriculaperiodo  ");
		sb.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
		sb.append(" inner join funcionario on funcionario.codigo = matricula.consultor ");
		sb.append(" inner join pessoa on pessoa.codigo = funcionario.pessoa ");
		sb.append(" left join funcionariocargo on funcionariocargo.funcionario = funcionario.codigo ");
		sb.append(" left join cargo on cargo.codigo = funcionariocargo.cargo ");
		sb.append(" where 1=1 ");
		sb.append(" and alunotransferidounidade = false ");
		sb.append(" and matricula.aluno in ( select mat.aluno from matriculaperiodo mp ");
		sb.append(" inner join matricula mat on mat.matricula = mp.matricula ");
		sb.append(" where alunotransferidounidade = true ) ");
		if (unidadeEnsino.getCodigo().intValue() > 0) {
			sb.append(" and matricula.unidadeEnsino = ").append(unidadeEnsino.getCodigo().intValue());
		}
		if (curso.getCodigo().intValue() > 0) {
			sb.append(" and matricula.curso = ").append(curso.getCodigo().intValue());
		}
		if (turma.getCodigo().intValue() > 0) {
			sb.append(" and matriculaPeriodo.turma = ").append(turma.getCodigo().intValue());
		}
		if (consultor.getCodigo().intValue() > 0) {
			sb.append(" and funcionario.codigo = ").append(consultor.getCodigo().intValue());
		}
		if (dataInicio != null) {
			sb.append(" and matriculaPeriodo.data >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
		}
		if (dataTermino != null) {
			sb.append(" and matriculaPeriodo.data <= '").append(Uteis.getDataJDBC(dataTermino)).append("' ");
		}
		return sb;
	}
	
	public StringBuilder sqlConsultarQtdMatAVencer(Integer turmaProcessar, Integer codConsultor, Date dataInicio, Date dataTermino, 
			boolean matRecebida, boolean matAReceber, boolean matVencida, boolean matAVencer, boolean ativa, boolean preMatricula, boolean cancelada, boolean excluida, boolean transferidaDe, boolean transferidaPara, String situacaoProspect) throws Exception {
		StringBuilder sb = new StringBuilder("select count(matricula.matricula) as qtdAluno from matriculaperiodo  ");
		sb.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
		sb.append(" left join funcionario on funcionario.codigo = matricula.consultor ");
		sb.append(" left join pessoa on pessoa.codigo = funcionario.pessoa ");
		sb.append(" where 1=1 ");
		sb.append(" and alunotransferidounidade = false ");
		sb.append(" and matricula.aluno in ( select mat.aluno from matriculaperiodo mp ");
		sb.append(" inner join matricula mat on mat.matricula = mp.matricula ");
		sb.append(" where alunotransferidounidade = true ) ");
		sb.append(" and matriculaPeriodo.turma = ").append(turmaProcessar);
		sb.append(" and funcionario.codigo = ").append(codConsultor);
		if (dataInicio != null) {
			sb.append(" and matriculaPeriodo.data >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
		}
		if (dataTermino != null) {
			sb.append(" and matriculaPeriodo.data <= '").append(Uteis.getDataJDBC(dataTermino)).append("' ");
		}
		return sb;
	}

	public StringBuilder sqlConsultorMatriculaTurmaMatAVencer(UnidadeEnsinoVO unidadeEnsino, Date dataInicio, Date dataTermino, CursoVO curso, TurmaVO turma, FuncionarioVO consultor, 
			boolean matRecebida, boolean matAReceber, boolean matVencida, boolean matAVencer, boolean ativa, boolean preMatricula, boolean cancelada, boolean excluida, boolean transferidaDe, boolean transferidaPara, String situacaoProspect) throws Exception {
		StringBuilder sb = new StringBuilder("select distinct matriculaperiodo.turma, turma.identificadorturma from matriculaperiodo  ");
		sb.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
		sb.append(" inner join turma on turma.codigo = matriculaperiodo.turma ");
		sb.append(" left join funcionario on funcionario.codigo = matricula.consultor ");
		sb.append(" left join pessoa on pessoa.codigo = funcionario.pessoa ");
		sb.append(" where 1=1 ");
		sb.append(" and alunotransferidounidade = false ");
		sb.append(" and matricula.aluno in ( select mat.aluno from matriculaperiodo mp ");
		sb.append(" inner join matricula mat on mat.matricula = mp.matricula ");
		sb.append(" where alunotransferidounidade = true ) ");
		if (unidadeEnsino.getCodigo().intValue() > 0) {
			sb.append(" and matricula.unidadeEnsino = ").append(unidadeEnsino.getCodigo().intValue());
		}
		if (curso.getCodigo().intValue() > 0) {
			sb.append(" and matricula.curso = ").append(curso.getCodigo().intValue());
		}
		if (turma.getCodigo().intValue() > 0) {
			sb.append(" and matriculaPeriodo.turma = ").append(turma.getCodigo().intValue());
		}
		if (consultor.getCodigo().intValue() > 0) {
			sb.append(" and funcionario.codigo = ").append(consultor.getCodigo().intValue());
		}
		if (dataInicio != null) {
			sb.append(" and matriculaPeriodo.data >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
		}
		if (dataTermino != null) {
			sb.append(" and matriculaPeriodo.data <= '").append(Uteis.getDataJDBC(dataTermino)).append("' ");
		}
		return sb;		
	}
	
	@Override
	public void consultarDadosDetalhePainelGestorMonitoramentoCRMPorCurso(List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, PainelGestorMonitoramentoCRMVO painelGestorMonitoramentoCRMVO, PainelGestorMonitoramentoDetalheCRMVO painelGestorMonitoramentoDetalheCRMVO) throws Exception {
		if (painelGestorMonitoramentoDetalheCRMVO.getPainelGestorTipoMonitoramentoCRMEnum().equals(PainelGestorTipoMonitoramentoCRMEnum.PRE_INSCRICAO)) {
			montarDadosGeraisPreInscricao(painelGestorMonitoramentoCRMVO, consultarDadosPreInscicao(unidadeEnsinoVOs, dataInicio, dataTermino, true, false, 0, 0), true, false, PainelGestorTipoMonitoramentoCRMEnum.PRE_INSCRICAO_DETALHE_CURSO);
		}
		if (painelGestorMonitoramentoDetalheCRMVO.getPainelGestorTipoMonitoramentoCRMEnum().equals(PainelGestorTipoMonitoramentoCRMEnum.PRE_INSCRICAO_DETALHE_CONSULTOR)) {
			if (painelGestorMonitoramentoDetalheCRMVO.getConsultor().getCodigo() == -1) {
				montarDadosGeraisPreInscricao(painelGestorMonitoramentoCRMVO, consultarDadosPreInscicao(unidadeEnsinoVOs, dataInicio, dataTermino, true, false, 0, painelGestorMonitoramentoDetalheCRMVO.getConsultor().getCodigo()), true, false, PainelGestorTipoMonitoramentoCRMEnum.PRE_INSCRICAO_DETALHE_CONSULTOR_CURSO);				
			} else {
				montarDadosGeraisPreInscricao(painelGestorMonitoramentoCRMVO, consultarDadosPreInscicao(unidadeEnsinoVOs, dataInicio, dataTermino, true, false, 0, painelGestorMonitoramentoDetalheCRMVO.getConsultor().getCodigo()), true, false, PainelGestorTipoMonitoramentoCRMEnum.PRE_INSCRICAO_DETALHE_CONSULTOR_CURSO);
			}
		}
		if (painelGestorMonitoramentoDetalheCRMVO.getPainelGestorTipoMonitoramentoCRMEnum().equals(PainelGestorTipoMonitoramentoCRMEnum.PRE_INSCRICAO_DETALHE_CURSO_CONSULTOR)) {
			montarDadosGeraisPreInscricao(painelGestorMonitoramentoCRMVO, consultarDadosPreInscicao(unidadeEnsinoVOs, dataInicio, dataTermino, true, false, 0, painelGestorMonitoramentoDetalheCRMVO.getConsultor().getCodigo()), true, false, PainelGestorTipoMonitoramentoCRMEnum.PRE_INSCRICAO_DETALHE_CONSULTOR_CURSO);
		}
		if (painelGestorMonitoramentoDetalheCRMVO.getPainelGestorTipoMonitoramentoCRMEnum().equals(PainelGestorTipoMonitoramentoCRMEnum.PRE_INSCRICAO_DETALHE_CONSULTOR_CURSO)) {
			montarDadosGeraisPreInscricao(painelGestorMonitoramentoCRMVO, consultarDadosPreInscicao(unidadeEnsinoVOs, dataInicio, dataTermino, true, false, 0, painelGestorMonitoramentoDetalheCRMVO.getConsultor().getCodigo()), true, false, PainelGestorTipoMonitoramentoCRMEnum.PRE_INSCRICAO_DETALHE_CURSO_CONSULTOR);
		}
	}

	@Override
	public void consultarDadosProspectDetalhePainelGestorMonitoramentoCRM(List<UnidadeEnsinoVO> unidadeEnsinoVOs, PainelGestorMonitoramentoCRMVO painelGestorMonitoramentoCRMVO, PainelGestorMonitoramentoDetalheCRMVO painelGestorMonitoramentoDetalheCRMVO) throws Exception {
		painelGestorMonitoramentoCRMVO.getProspectsVOs().clear();
		if (painelGestorMonitoramentoDetalheCRMVO.getPainelGestorTipoMonitoramentoCRMEnum().equals(PainelGestorTipoMonitoramentoCRMEnum.PRE_INSCRICAO_DETALHE_CONSULTOR)) {
			painelGestorMonitoramentoCRMVO.setProspectsVOs(consultarDadosProspectPreInscicao(unidadeEnsinoVOs, painelGestorMonitoramentoDetalheCRMVO.getDataInicio(), painelGestorMonitoramentoDetalheCRMVO.getDataTermino(), 0, painelGestorMonitoramentoDetalheCRMVO.getConsultor().getCodigo()));
		}
		if (painelGestorMonitoramentoDetalheCRMVO.getPainelGestorTipoMonitoramentoCRMEnum().equals(PainelGestorTipoMonitoramentoCRMEnum.PRE_INSCRICAO_DETALHE_CONSULTOR_CURSO)) {
			painelGestorMonitoramentoCRMVO.setProspectsVOs(consultarDadosProspectPreInscicao(unidadeEnsinoVOs, painelGestorMonitoramentoDetalheCRMVO.getDataInicio(), painelGestorMonitoramentoDetalheCRMVO.getDataTermino(), painelGestorMonitoramentoDetalheCRMVO.getCurso().getCodigo(), painelGestorMonitoramentoCRMVO.getPainelGestorMonitoramentoDetalheCRMVO().getConsultor().getCodigo()));
		}
		if (painelGestorMonitoramentoDetalheCRMVO.getPainelGestorTipoMonitoramentoCRMEnum().equals(PainelGestorTipoMonitoramentoCRMEnum.PRE_INSCRICAO_DETALHE_CURSO)) {
			painelGestorMonitoramentoCRMVO.setProspectsVOs(consultarDadosProspectPreInscicao(unidadeEnsinoVOs, painelGestorMonitoramentoDetalheCRMVO.getDataInicio(), painelGestorMonitoramentoDetalheCRMVO.getDataTermino(), painelGestorMonitoramentoDetalheCRMVO.getCurso().getCodigo(), 0));
		}
		if (painelGestorMonitoramentoDetalheCRMVO.getPainelGestorTipoMonitoramentoCRMEnum().equals(PainelGestorTipoMonitoramentoCRMEnum.PRE_INSCRICAO_DETALHE_CURSO_CONSULTOR)) {
			painelGestorMonitoramentoCRMVO.setProspectsVOs(consultarDadosProspectPreInscicao(unidadeEnsinoVOs, painelGestorMonitoramentoDetalheCRMVO.getDataInicio(), painelGestorMonitoramentoDetalheCRMVO.getDataTermino(), painelGestorMonitoramentoCRMVO.getPainelGestorMonitoramentoDetalheCRMVO().getCurso().getCodigo(), painelGestorMonitoramentoDetalheCRMVO.getConsultor().getCodigo()));
		}
		

	}

	@Override
	public void realizarGeracaoGraficoMonitoramentoProspectComoFicouSabendoInstituicao(List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dateInicio, Date dataTermino, PainelGestorMonitoramentoCRMVO painelGestorMonitoramentoCRMVO, String situacaoProspect) throws Exception {
		painelGestorMonitoramentoCRMVO.setGraficoComoFicouSabendoInstituicao("");
		StringBuilder sql = new StringBuilder("");
		sql.append(" select count(prospects.codigo) as quantidade, tipomidiacaptacao.codigo, tipomidiacaptacao.nomemidia as nome ");
		sql.append(" from prospects   ");
		sql.append(" inner join interacaoworkflow  on interacaoworkflow.prospect = prospects.codigo ");
		sql.append(" and interacaoworkflow.codigo = (select iwf.codigo from interacaoworkflow as iwf ");
		sql.append(" where iwf.prospect = prospects.codigo ");
		sql.append(" and ").append(realizarGeracaoWherePeriodo(dateInicio, dataTermino, "iwf.dataInicio", false));			
		sql.append(" order by iwf.codigo desc limit 1) ");
		sql.append(" inner join tipomidiacaptacao on tipomidiacaptacao.codigo = prospects.tipomidia ");
		sql.append(" where 1=1 and inativo = false ");
		if(situacaoProspect != null && !situacaoProspect.trim().isEmpty()){
			if(situacaoProspect.equals("MATRICULADO")){
				sql.append(" and interacaoworkflow.motivoinsucesso is null and prospects.pessoa in (");
				sql.append(" 	select aluno from matricula where aluno =  prospects.pessoa and matricula.curso = interacaoworkflow.curso   ");
				sql.append(" ) ");
			}else if(situacaoProspect.equals("FINALIZADO_INSUCESSO")){
				sql.append(" and interacaoworkflow.motivoinsucesso is not null ");				
			}else if(situacaoProspect.equals("NORMAL")){
				sql.append(" and interacaoworkflow.motivoinsucesso is null ");
				sql.append(" and prospects.pessoa not in (");
				sql.append(" 	select aluno from matricula where aluno =  prospects.pessoa  and matricula.curso = interacaoworkflow.curso  ");
				sql.append(" ) ");
			}
		}
		if (unidadeEnsinoVOs != null && !unidadeEnsinoVOs.isEmpty()) {
			sql.append(" and (prospects.unidadeensino in (0 ");
			for (UnidadeEnsinoVO unidadeEnsinoVO : unidadeEnsinoVOs) {
				if (unidadeEnsinoVO.getFiltrarUnidadeEnsino()) {
					sql.append(", ").append(unidadeEnsinoVO.getCodigo());
				}
			}
			sql.append(" )   or  prospects.unidadeensino is  null)");
		}

		sql.append(" group by tipomidiacaptacao.codigo, tipomidiacaptacao.nomemidia ");
		sql.append(" order by quantidade desc, tipomidiacaptacao.nomemidia ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<LegendaGraficoVO> legendaGraficoVOs = new ArrayList<LegendaGraficoVO>();
		LegendaGraficoVO legendaGraficoVO = null;
		Integer qtdeTotal = 0;
		while (rs.next()) {
			legendaGraficoVO = new LegendaGraficoVO(rs.getString("nome") == null || rs.getString("nome").trim().isEmpty() ? 0 : rs.getInt("codigo"), 
					rs.getString("nome") == null || rs.getString("nome").trim().isEmpty() ? "Não Informado" : rs.getString("nome"), PainelGestorTipoMonitoramentoCRMEnum.PROSPECT_POR_UNIDADE.name(), 
							Double.valueOf(rs.getInt("quantidade")), "", false);			 
			qtdeTotal += rs.getInt("quantidade");			
			legendaGraficoVOs.add(legendaGraficoVO);
		}		
		
		for(LegendaGraficoVO legendaGrafico:legendaGraficoVOs){
			adicionarDadosRelatorioGraficoMonitiramentoProspect(((legendaGrafico.getValor()*100)/qtdeTotal.doubleValue()), legendaGrafico.getValor().intValue(), 
					legendaGrafico.getLegenda(), PainelGestorTipoMonitoramentoCRMEnum.COMO_FICOU_SABENDO_INSTITUICAO, legendaGrafico.getCodigo(), painelGestorMonitoramentoCRMVO);
		}

	}
	
	@Override
	public List<ProspectsVO> consultarDadosProspectComoFicouSabendoDaInstituicao(List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dateInicio, Date dataTermino, String situacaoProspect, Integer tipoMidiaCaptacao, Integer limit, Integer offset) throws Exception {
		
		StringBuilder sql = new StringBuilder("");
		sql.append(" select distinct prospects.codigo, prospects.nome, prospects.emailPrincipal, prospects.telefoneResidencial, ");
		sql.append(" unidadeensino.codigo as \"unidadeensino.codigo\", unidadeensino.nome as \"unidadeensino.nome\",  ");
		sql.append(" Funcionario.codigo as \"funcionario.codigo\", Pessoa.nome as \"pessoa.nome\", Pessoa.codigo as \"pessoa.codigo\"  ");
		sql.append(" from prospects");
		sql.append(" left join unidadeensino on unidadeensino.codigo = prospects.unidadeensino ");
		sql.append(" left  join Funcionario on Funcionario.codigo =   prospects.consultorPadrao");
		sql.append(" left  join Pessoa on Funcionario.pessoa =   Pessoa.codigo");		
		sql.append(" inner join interacaoworkflow  on interacaoworkflow.prospect = prospects.codigo ");
		sql.append(" and interacaoworkflow.codigo = (select iwf.codigo from interacaoworkflow as iwf ");
		sql.append(" where iwf.prospect = prospects.codigo  ");
		sql.append(" and ").append(realizarGeracaoWherePeriodo(dateInicio, dataTermino, "iwf.dataInicio", false));			
		sql.append(" order by iwf.codigo desc limit 1) ");
		sql.append(" inner join tipomidiacaptacao on tipomidiacaptacao.codigo = prospects.tipomidia ");
		sql.append(" where 1 = 1 ");
		sql.append(" and prospects.inativo = false  ");
		if(situacaoProspect != null && !situacaoProspect.trim().isEmpty()){
			if(situacaoProspect.equals("MATRICULADO")){
				sql.append(" and interacaoworkflow.motivoinsucesso is null and prospects.pessoa in (");
				sql.append(" 	select aluno from matricula where aluno =  prospects.pessoa and matricula.curso = interacaoworkflow.curso   ");
				sql.append(" ) ");
			}else if(situacaoProspect.equals("FINALIZADO_INSUCESSO")){
				sql.append(" and interacaoworkflow.motivoinsucesso is not null ");				
			}else if(situacaoProspect.equals("NORMAL")){
				sql.append(" and interacaoworkflow.motivoinsucesso is null ");
				sql.append(" and prospects.pessoa not in (");
				sql.append(" 	select aluno from matricula where aluno =  prospects.pessoa  and matricula.curso = interacaoworkflow.curso  ");
				sql.append(" ) ");
			}
		}
		if(tipoMidiaCaptacao != null && tipoMidiaCaptacao > 0){
			sql.append(" and tipomidiacaptacao.codigo = ").append(tipoMidiaCaptacao);
		}
		if (unidadeEnsinoVOs != null && !unidadeEnsinoVOs.isEmpty()) {
			sql.append(" and (prospects.unidadeensino in (0 ");
			for (UnidadeEnsinoVO unidadeEnsinoVO : unidadeEnsinoVOs) {
				if (unidadeEnsinoVO.getFiltrarUnidadeEnsino()) {
					sql.append(", ").append(unidadeEnsinoVO.getCodigo());
				}
			}
			sql.append(" )   or  prospects.unidadeensino is  null)");
		}
		
		sql.append(" group by prospects.codigo, prospects.nome, prospects.emailPrincipal, prospects.telefoneResidencial, unidadeensino.codigo, unidadeensino.nome, Funcionario.codigo, Pessoa.nome, Pessoa.codigo  ");
		sql.append(" order by prospects.nome");
		if (limit != null && limit > 0) {
			sql.append(" limit ").append(limit);
			sql.append(" offset ").append(offset);
		}

		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<ProspectsVO> prospectsVOs = new ArrayList<ProspectsVO>(0);
		while (rs.next()) {
			ProspectsVO prospectsVO = new ProspectsVO();
			prospectsVO.setCodigo(rs.getInt("codigo"));
			prospectsVO.setNome(rs.getString("nome"));
			prospectsVO.setEmailPrincipal(rs.getString("emailPrincipal"));
			prospectsVO.setTelefoneResidencial(rs.getString("telefoneResidencial"));
			prospectsVO.getConsultorPadrao().setCodigo(rs.getInt("funcionario.codigo"));
			prospectsVO.getConsultorPadrao().getPessoa().setCodigo(rs.getInt("pessoa.codigo"));
			prospectsVO.getConsultorPadrao().getPessoa().setNome(rs.getString("pessoa.nome"));
			prospectsVO.getUnidadeEnsino().setCodigo(rs.getInt("unidadeensino.codigo"));
			prospectsVO.getUnidadeEnsino().setNome(rs.getString("unidadeensino.nome"));

			prospectsVOs.add(prospectsVO);
		}
		return prospectsVOs;
		
	}
	@Override
	public Integer consultarTotalRegistroProspectComoFicouSabendoDaInstituicao(List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dateInicio, Date dataTermino, String situacaoProspect, Integer tipoMidiaCaptacao) throws Exception {
		
		StringBuilder sql = new StringBuilder("");
		sql.append(" select count(distinct prospects.codigo) as qtde ");
		sql.append(" from prospects");
		sql.append(" left join unidadeensino on unidadeensino.codigo = prospects.unidadeensino ");
		sql.append(" left  join Funcionario on Funcionario.codigo =   prospects.consultorPadrao");
		sql.append(" left  join Pessoa on Funcionario.pessoa =   Pessoa.codigo");		
		sql.append(" inner join interacaoworkflow  on interacaoworkflow.prospect = prospects.codigo ");
		sql.append(" and interacaoworkflow.codigo = (select iwf.codigo from interacaoworkflow as iwf ");
		sql.append(" where iwf.prospect = prospects.codigo  ");
		sql.append(" and ").append(realizarGeracaoWherePeriodo(dateInicio, dataTermino, "iwf.dataInicio", false));			
		sql.append(" order by iwf.codigo desc limit 1) ");
		sql.append(" inner join tipomidiacaptacao on tipomidiacaptacao.codigo = prospects.tipomidia ");
		sql.append(" where 1 = 1 ");
		sql.append(" and prospects.inativo = false  ");
		if(situacaoProspect != null && !situacaoProspect.trim().isEmpty()){
			if(situacaoProspect.equals("MATRICULADO")){
				sql.append(" and interacaoworkflow.motivoinsucesso is null and prospects.pessoa in (");
				sql.append(" 	select aluno from matricula where aluno =  prospects.pessoa and matricula.curso = interacaoworkflow.curso   ");
				sql.append(" ) ");
			}else if(situacaoProspect.equals("FINALIZADO_INSUCESSO")){
				sql.append(" and interacaoworkflow.motivoinsucesso is not null ");				
			}else if(situacaoProspect.equals("NORMAL")){
				sql.append(" and interacaoworkflow.motivoinsucesso is null ");
				sql.append(" and prospects.pessoa not in (");
				sql.append(" 	select aluno from matricula where aluno =  prospects.pessoa  and matricula.curso = interacaoworkflow.curso  ");
				sql.append(" ) ");
			}
		}
		if(tipoMidiaCaptacao != null && tipoMidiaCaptacao > 0){
			sql.append(" and tipomidiacaptacao.codigo = ").append(tipoMidiaCaptacao);
		}
		if (unidadeEnsinoVOs != null && !unidadeEnsinoVOs.isEmpty()) {
			sql.append(" and (prospects.unidadeensino in (0 ");
			for (UnidadeEnsinoVO unidadeEnsinoVO : unidadeEnsinoVOs) {
				if (unidadeEnsinoVO.getFiltrarUnidadeEnsino()) {
					sql.append(", ").append(unidadeEnsinoVO.getCodigo());
				}
			}
			sql.append(" )   or  prospects.unidadeensino is  null)");
		}
		
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		
		if (rs.next()) {
			return rs.getInt("qtde");			
		}
		return 0;
		
	}

	private void adicionarFiltroCalouroVeterano(StringBuilder sql, String situacaoAlunoCurso) throws Exception {
		if (situacaoAlunoCurso.equals("veterano")) {
			sql.append(" and (0 < (select count(matper2.codigo) from matriculaperiodo matper2 ");
			sql.append(" where matper2.matricula = matriculaperiodo.matricula  and matper2.situacaomatriculaperiodo != 'PC' ");
			sql.append(" and case when curso.periodicidade = 'IN' then matper2.data < matriculaperiodo.data else (matper2.ano||'/'||matper2.semestre) < (matriculaperiodo.ano||'/'||matriculaperiodo.semestre) end)) ");
		} else if (situacaoAlunoCurso.equals("calouro")) {
			sql.append(" and ( formaingresso not in ('TI' , 'TE') ");
			sql.append(" and matricula.matricula not in (select distinct matricula from transferenciaentrada where tipotransferenciaentrada in ('IN', 'EX') and matricula is not null )  ");
			sql.append(" and  0 = (select count(matper2.codigo) from matriculaperiodo matper2 where matper2.matricula = matriculaperiodo.matricula  ");
			sql.append(" and matper2.situacaomatriculaperiodo != 'PC' and case when curso.periodicidade = 'IN' then matper2.data < matriculaperiodo.data else (matper2.ano||'/'||matper2.semestre) < (matriculaperiodo.ano||'/'||matriculaperiodo.semestre) end ");
			sql.append(" ) ) ");
		}
	}

	 @Override
	    public String designRelatorio() {
	        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "crm" + File.separator + "ConsultorPorMatriculaRel" + ".jrxml");
	    }
	 @Override
	 public String designRelatorioAnalitico() {
		 return ("relatorio" + File.separator + "designRelatorio" + File.separator + "crm" + File.separator + "ConsultorPorMatriculaAnaliticoRel" + ".jrxml");
	 }
	 
	 @Override
	 public String designRelatorioExcel() {
	        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "crm" + File.separator + "ConsultorPorMatriculaRelExcel" + ".jrxml");
	 }

	 @Override
	 public String designRelatorioExcelAnalitico() {
		 return ("relatorio" + File.separator + "designRelatorio" + File.separator + "crm" + File.separator + "ConsultorPorMatriculaAnaliticoRelExcel" + ".jrxml");
	 }
	 
	 @Override
	 public String caminhoSubReport() {
	    return ("relatorio" + File.separator + "designRelatorio" + File.separator + "crm" + File.separator);
	 }
	 
	 @Override
	public List<ProspectsVO> consultarDadosProspectNaoSincronizadosComRdStation (List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, Integer curso, Integer consultor, Integer unidadeEspecifica, TipoFiltroMonitamentoCrmProspectEnum tipoFiltroMonitamentoCrmProspectEnum, MesAnoEnum mesAno, Integer ano, Integer periodoUltimoContato1, Integer periodoUltimoContato2, Integer periodoUltimoContato3, PainelGestorTipoMonitoramentoCRMEnum painelGestorTipoMonitoramentoCRMEnum, Boolean matriculado, Integer limit, Integer offset, Integer codigoSegmentacaoOpcao) throws Exception {
			StringBuilder sql = new StringBuilder("");
			sql.append(" select prospects.codigo, prospects.nome, prospects.emailPrincipal, prospects.telefoneResidencial, ");
			sql.append(" unidadeensino.codigo as \"unidadeensino.codigo\", unidadeensino.nome as \"unidadeensino.nome\",  ");
			sql.append(" Funcionario.codigo as \"funcionario.codigo\", Pessoa.nome as \"pessoa.nome\", Pessoa.codigo as \"pessoa.codigo\", ");
			sql.append(" prospects.celular, prospects.sincronizadordstation  ");
			sql.append(" from prospects");
			if(!codigoSegmentacaoOpcao.equals(0)) {
				sql.append(" inner join prospectsegmentacaoopcao  on prospects.codigo = prospectsegmentacaoopcao.prospect");
				sql.append(" inner join segmentacaoopcao  on segmentacaoopcao.codigo = prospectsegmentacaoopcao.segmentacaoopcao");
				sql.append(" inner join segmentacaoprospect  on segmentacaoopcao.segmentacaoprospect = segmentacaoprospect.codigo");
			}
			sql.append(" left join unidadeensino on unidadeensino.codigo = prospects.unidadeensino ");
			sql.append(" left  join Funcionario on Funcionario.codigo =   prospects.consultorPadrao");
			sql.append(" left  join Pessoa on Funcionario.pessoa =   Pessoa.codigo");		
			if (painelGestorTipoMonitoramentoCRMEnum != null && painelGestorTipoMonitoramentoCRMEnum.equals(PainelGestorTipoMonitoramentoCRMEnum.PROSPECT_POR_CURSO)) {
				sql.append(" left join cursointeresse  on cursointeresse.prospects = prospects.codigo ");
				sql.append(" left join curso  on cursointeresse.curso = curso.codigo ");
			}
			sql.append(" where (prospects.sincronizadoRDStation isnull or prospects.sincronizadoRDStation is false) ");
			if(!codigoSegmentacaoOpcao.equals(0)) {
				sql.append(" and segmentacaoopcao.codigo = ").append(codigoSegmentacaoOpcao);
			}
			if(matriculado != null){
				if(matriculado){
					sql.append(" and prospects.pessoa in (select aluno from matricula where aluno = prospects.pessoa ) ");
				}else{
					sql.append(" and prospects.pessoa not in (select aluno from matricula where aluno = prospects.pessoa ) ");
				}
			}
			sql.append(" and prospects.inativo = false  ");
			if (painelGestorTipoMonitoramentoCRMEnum == null) {
				if (unidadeEnsinoVOs != null && !unidadeEnsinoVOs.isEmpty()) {
					sql.append(" and (prospects.unidadeensino in (0 ");
					for (UnidadeEnsinoVO unidadeEnsinoVO : unidadeEnsinoVOs) {
						if (unidadeEnsinoVO.getFiltrarUnidadeEnsino()) {
							sql.append(", ").append(unidadeEnsinoVO.getCodigo());
						}
					}
					sql.append(" )  or  prospects.unidadeensino is  null)");
				}
			} else {
				if (painelGestorTipoMonitoramentoCRMEnum != null) {
					if (unidadeEspecifica == null || unidadeEspecifica == 0) {
						sql.append(" and prospects.unidadeensino is null ");
					} else {
						sql.append(" and prospects.unidadeensino = ").append(unidadeEspecifica);
					}
				}
			}
			if (painelGestorTipoMonitoramentoCRMEnum != null && painelGestorTipoMonitoramentoCRMEnum.equals(PainelGestorTipoMonitoramentoCRMEnum.PROSPECT_POR_CURSO)) {
				if (curso != null && curso > 0) {
					sql.append(" and curso.codigo = ").append(curso);
				} else {
					sql.append(" and curso.codigo is null ");
				}
			}
			if (painelGestorTipoMonitoramentoCRMEnum != null && (painelGestorTipoMonitoramentoCRMEnum.equals(PainelGestorTipoMonitoramentoCRMEnum.PROSPECT_POR_CURSO) 
					|| painelGestorTipoMonitoramentoCRMEnum.equals(PainelGestorTipoMonitoramentoCRMEnum.PROSPECT_POR_CONSULTOR))) {
				if (consultor != null && consultor > 0) {
					sql.append(" and prospects.consultorPadrao = ").append(consultor);
				} else {
					sql.append(" and ((prospects.consultorPadrao is null) or (prospects.consultorPadrao = 0)) ");
				}
			}
			if (tipoFiltroMonitamentoCrmProspectEnum != null) {
				if (tipoFiltroMonitamentoCrmProspectEnum.equals(TipoFiltroMonitamentoCrmProspectEnum.TODOS_PROSPECTS_SEM_CONSULTOR)) {
					sql.append(" and ((prospects.consultorPadrao is null) or (prospects.consultorPadrao = 0)) ");			
				} else if (tipoFiltroMonitamentoCrmProspectEnum.equals(TipoFiltroMonitamentoCrmProspectEnum.TODOS_PROSPECTS_SEM_CONTATO_PERIODO1)) {

					sql.append(" and prospects.codigo in  ");
					sql.append(" ( select p.codigo from prospects p ");
					sql.append(" left join (select max(data) as dataCompromisso, prospect from ( ");
					sql.append(" select max(dataCompromisso) as data, cpah2.prospect from compromissoagendapessoahorario cpah2  group by cpah2.prospect ");
					sql.append(" union all  ");
					sql.append(" select max(dataregistro) as data, hfu.prospect from historicofollowup hfu  group by hfu.prospect ");
					sql.append(" union all  ");
					sql.append(" select max(interacaoworkflow.datainicio) as data, interacaoworkflow.prospect from interacaoworkflow  group by interacaoworkflow.prospect ");
					sql.append(" ) as dataContato group by prospect) as caph on p.codigo = caph.prospect  ");
					sql.append(" where case when caph.dataCompromisso is null then p.datacadastro::DATE else caph.dataCompromisso::DATE end < current_date ");
					sql.append("  and (  ");
					sql.append(" (DATE_PART('year', current_date) - DATE_PART('year',case when caph.dataCompromisso is null then p.datacadastro::DATE else caph.dataCompromisso::DATE end))  * 12 + (DATE_PART('month', current_date) - DATE_PART('month', case when caph.dataCompromisso is null then p.datacadastro::DATE else caph.dataCompromisso::DATE end)) >=  ").append(periodoUltimoContato1);
					sql.append(" and (DATE_PART('year', current_date) - DATE_PART('year',case when caph.dataCompromisso is null then p.datacadastro::DATE else caph.dataCompromisso::DATE end))  * 12 + (DATE_PART('month', current_date) - DATE_PART('month', case when caph.dataCompromisso is null then p.datacadastro::DATE else caph.dataCompromisso::DATE end))  < ").append(periodoUltimoContato2);
					sql.append(" ) ");
					sql.append(" ) ");
				} else if (tipoFiltroMonitamentoCrmProspectEnum.equals(TipoFiltroMonitamentoCrmProspectEnum.TODOS_PROSPECTS_SEM_CONTATO_PERIODO2)) {
					sql.append(" and prospects.codigo in  ");
					sql.append(" ( select p.codigo from prospects p ");
					sql.append(" left join (select max(data) as dataCompromisso, prospect from ( ");
					sql.append(" select max(dataCompromisso) as data, cpah2.prospect from compromissoagendapessoahorario cpah2  group by cpah2.prospect ");
					sql.append(" union all  ");
					sql.append(" select max(dataregistro) as data, hfu.prospect from historicofollowup hfu  group by hfu.prospect ");
					sql.append(" union all  ");
					sql.append(" select max(interacaoworkflow.datainicio) as data, interacaoworkflow.prospect from interacaoworkflow  group by interacaoworkflow.prospect ");
					sql.append(" ) as dataContato group by prospect) as caph on p.codigo = caph.prospect  ");

					sql.append(" where case when caph.dataCompromisso is null then p.datacadastro::DATE else caph.dataCompromisso::DATE end < current_date ");
					sql.append("  and (  ");
					sql.append(" (DATE_PART('year', current_date) - DATE_PART('year',case when caph.dataCompromisso is null then p.datacadastro::DATE else caph.dataCompromisso::DATE end))  * 12 + (DATE_PART('month', current_date) - DATE_PART('month', case when caph.dataCompromisso is null then p.datacadastro::DATE else caph.dataCompromisso::DATE end)) >=  ").append(periodoUltimoContato2);
					sql.append(" and (DATE_PART('year', current_date) - DATE_PART('year',case when caph.dataCompromisso is null then p.datacadastro::DATE else caph.dataCompromisso::DATE end))  * 12 + (DATE_PART('month', current_date) - DATE_PART('month', case when caph.dataCompromisso is null then p.datacadastro::DATE else caph.dataCompromisso::DATE end))  < ").append(periodoUltimoContato3);
					sql.append(" ) ");
					sql.append(" ) ");
				} else if (tipoFiltroMonitamentoCrmProspectEnum.equals(TipoFiltroMonitamentoCrmProspectEnum.TODOS_PROSPECTS_SEM_CONTATO_PERIODO3)) {
					sql.append(" and prospects.codigo in  ");
					sql.append(" ( select p.codigo from prospects p ");
					sql.append(" left join (select max(data) as dataCompromisso, prospect from ( ");
					sql.append(" select max(dataCompromisso) as data, cpah2.prospect from compromissoagendapessoahorario cpah2  group by cpah2.prospect ");
					sql.append(" union all  ");
					sql.append(" select max(dataregistro) as data, hfu.prospect from historicofollowup hfu  group by hfu.prospect ");
					sql.append(" union all  ");
					sql.append(" select max(interacaoworkflow.datainicio) as data, interacaoworkflow.prospect from interacaoworkflow  group by interacaoworkflow.prospect ");
					sql.append(" ) as dataContato group by prospect) as caph on p.codigo = caph.prospect  ");
					sql.append(" where case when caph.dataCompromisso is null then p.datacadastro::DATE else caph.dataCompromisso::DATE end < current_date ");
					sql.append("  and (  ");
					sql.append(" (DATE_PART('year', current_date) - DATE_PART('year',case when caph.dataCompromisso is null then p.datacadastro::DATE else caph.dataCompromisso::DATE end))  * 12 + (DATE_PART('month', current_date) - DATE_PART('month', case when caph.dataCompromisso is null then p.datacadastro::DATE else caph.dataCompromisso::DATE end)) >=  ").append(periodoUltimoContato3);
					sql.append(" ) ");
					sql.append(" ) ");
				} else if (tipoFiltroMonitamentoCrmProspectEnum.equals(TipoFiltroMonitamentoCrmProspectEnum.MES_ANO_ABORDADO)) {
					sql.append(" and prospects.codigo in  ");
					sql.append(" ( select p.codigo from prospects p ");
					sql.append(" inner join compromissoagendapessoahorario caph on p.codigo = caph.prospect ");
					sql.append(" where ");
					sql.append(" DATE_PART('month', caph.dataCompromisso::DATE) = ").append(Integer.valueOf(mesAno.getKey()));
					sql.append(" and DATE_PART('year', caph.dataCompromisso::DATE) =   ").append(ano);
					sql.append(" and caph.tiposituacaocompromissoenum != 'REALIZADO_COM_REMARCACAO' ");
					sql.append(" )");
				} else if (tipoFiltroMonitamentoCrmProspectEnum.equals(TipoFiltroMonitamentoCrmProspectEnum.MES_ANO_FINALIZADO_SUCESSO)) {
					sql.append(" and prospects.codigo in  ");
					sql.append(" ( select p.codigo from prospects p ");
					sql.append(" inner join compromissoagendapessoahorario caph on p.codigo = caph.prospect ");
					sql.append(" inner join etapaworkflow on etapaworkflow.codigo = caph.etapaworkflow ");
					sql.append(" inner join situacaoprospectpipeline   on situacaoprospectpipeline.codigo = etapaworkflow.situacaodefinirprospectfinal ");
					sql.append(" where ");
					sql.append(" DATE_PART('month', caph.dataCompromisso::DATE) = ").append(Integer.valueOf(mesAno.getKey()));
					sql.append(" and DATE_PART('year', caph.dataCompromisso::DATE) =   ").append(ano);
					sql.append(" and situacaoprospectpipeline.controle = 'FINALIZADO_SUCESSO')");
				} else if (tipoFiltroMonitamentoCrmProspectEnum.equals(TipoFiltroMonitamentoCrmProspectEnum.MES_ANO_MATRICULADO)) {
					sql.append(" and prospects.codigo in  ");
					sql.append(" ( select distinct p.codigo from prospects p ");
					sql.append(" inner join compromissoagendapessoahorario caph on p.codigo = caph.prospect ");
					sql.append(" inner join matricula on matricula.aluno = p.pessoa ");
					sql.append(" where ");
					sql.append(" extract('month' from caph.dataCompromisso::DATE) = ").append(Integer.valueOf(mesAno.getKey()));
					sql.append(" and extract('year' from caph.dataCompromisso::DATE) =   ").append(ano);
					sql.append(" )");
				} else if (tipoFiltroMonitamentoCrmProspectEnum.equals(TipoFiltroMonitamentoCrmProspectEnum.MES_ANO_REAGENDADO)) {
					sql.append(" and prospects.codigo in  ");
					sql.append(" ( select p.codigo from prospects p ");
					sql.append(" inner join compromissoagendapessoahorario caph on p.codigo = caph.prospect ");
					sql.append(" where ");
					sql.append(" DATE_PART('month', caph.dataCompromisso::DATE) = ").append(Integer.valueOf(mesAno.getKey()));
					sql.append(" and DATE_PART('year', caph.dataCompromisso::DATE) =   ").append(ano);
					sql.append(" and caph.tiposituacaocompromissoenum = 'REALIZADO_COM_REMARCACAO' ");
					sql.append(" )");

				} else if (tipoFiltroMonitamentoCrmProspectEnum.equals(TipoFiltroMonitamentoCrmProspectEnum.TODOS_PROSPECTS_COM_CONSULTOR)) {
					sql.append(" and prospects.consultorPadrao is not null ");
				} else if (tipoFiltroMonitamentoCrmProspectEnum.equals(TipoFiltroMonitamentoCrmProspectEnum.TODOS_PROSPECTS_COM_AGENDA)) {
					sql.append(" and prospects.codigo in ");
					sql.append(" (select prospect from compromissoagendapessoahorario caph where prospects.codigo = caph.prospect ");
					sql.append(" and caph.datacompromisso  >= '").append(Uteis.getDataJDBC(dataInicio)).append("' and caph.datacompromisso  <= '").append(Uteis.getDataJDBC(dataTermino)).append("') ");
				} else if (tipoFiltroMonitamentoCrmProspectEnum.equals(TipoFiltroMonitamentoCrmProspectEnum.TODOS_PROSPECTS_SEM_AGENDA)) {
					sql.append(" and prospects.codigo not in ");
					sql.append(" (select prospect from compromissoagendapessoahorario caph where prospects.codigo = caph.prospect ");
					sql.append(" and caph.datacompromisso  >= '").append(Uteis.getDataJDBC(dataInicio)).append("' and caph.datacompromisso  <= '").append(Uteis.getDataJDBC(dataTermino)).append("') ");
				}
			}

			sql.append(" group by prospects.codigo, prospects.nome, prospects.emailPrincipal, prospects.telefoneResidencial, unidadeensino.codigo, unidadeensino.nome, Funcionario.codigo, Pessoa.nome, Pessoa.codigo  ");
			sql.append(" order by prospects.nome");
			if (limit != null && limit > 0) {
				sql.append(" limit ").append(limit);
				sql.append(" offset ").append(offset);
			}

			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
			List<ProspectsVO> prospectsVOs = new ArrayList<ProspectsVO>(0);
			while (rs.next()) {
				ProspectsVO prospectsVO = new ProspectsVO();
				prospectsVO.setCodigo(rs.getInt("codigo"));
				prospectsVO.setNome(rs.getString("nome"));
				prospectsVO.setEmailPrincipal(rs.getString("emailPrincipal"));
				prospectsVO.setTelefoneResidencial(rs.getString("telefoneResidencial"));
				prospectsVO.getConsultorPadrao().setCodigo(rs.getInt("funcionario.codigo"));
				prospectsVO.getConsultorPadrao().getPessoa().setCodigo(rs.getInt("pessoa.codigo"));
				prospectsVO.getConsultorPadrao().getPessoa().setNome(rs.getString("pessoa.nome"));
				prospectsVO.getUnidadeEnsino().setCodigo(rs.getInt("unidadeensino.codigo"));
				prospectsVO.getUnidadeEnsino().setNome(rs.getString("unidadeensino.nome"));
				prospectsVO.setCelular(rs.getString("celular"));
				prospectsVO.setSincronizadoRDStation(rs.getBoolean("sincronizadordstation"));

				prospectsVOs.add(prospectsVO);
			}
			return prospectsVOs;
		}
	 
	 private String sqlPadraoConsultarParcelaMatriculaNegociada() {
		 StringBuilder sb = new StringBuilder()
				 .append(" select 1 from contarecebernegociado inner join contareceber cr on contarecebernegociado.negociacaocontareceber::varchar = cr.codorigem ")
				 .append(" where contarecebernegociado.contareceber = crmn.codigo and cr.tipoorigem = '").append(TipoOrigemContaReceber.NEGOCIACAO.getValor()).append("' ");
		 return sb.toString();
	 }
	 
	 private String sqlExistsParcelaMatriculaNegociada() {
		 StringBuilder sb = new StringBuilder()
				 .append(" exists (").append(sqlPadraoConsultarParcelaMatriculaNegociada()).append(") ");
		 return sb.toString();
	 }
	 
	 private String sqlExistsParcelaMatriculaNegociadaRecebida() {
		 StringBuilder sb = new StringBuilder()
				 .append(" exists (").append(sqlPadraoConsultarParcelaMatriculaNegociada())
				 .append(" and cr.situacao = '").append(SituacaoContaReceber.RECEBIDO.getValor()).append("')");
		 return sb.toString();
	 }
	 
	 private String sqlExistsParcelaMatriculaNegociadaVencida() {
		 StringBuilder sb = new StringBuilder()
				 .append(" exists (").append(sqlPadraoConsultarParcelaMatriculaNegociada())
				 .append(" and datavencimento < current_date )");
		 return sb.toString();
	 }
}