package jobs;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import negocio.comuns.administrativo.AtendimentoInteracaoDepartamentoVO;
import negocio.comuns.administrativo.AtendimentoVO;
import negocio.comuns.administrativo.ConfiguracaoAtendimentoUnidadeEnsinoVO;
import negocio.comuns.administrativo.ConfiguracaoAtendimentoVO;
import negocio.comuns.administrativo.GrupoDestinatariosVO;
import negocio.comuns.administrativo.enumeradores.SituacaoAtendimentoEnum;
import negocio.comuns.administrativo.enumeradores.TipoAtendimentoEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.SuperFacadeJDBC;

/**
 * 
 * @author PEDRO
 */
@Service
@Lazy
public class JobNotificacaoOuvidoriaNaoAtendidaNoPrazo extends SuperFacadeJDBC implements Serializable {

	public void realizarNotificacaoOuvidoriaNaoAtendidaNoPrazo() {
		try {
			List<ConfiguracaoAtendimentoVO> listaConfiguracaoAtendimento = getFacadeFactory().getConfiguracaoAtendimentoFacade().consultarPorCodigo(0, false, Uteis.NIVELMONTARDADOS_TODOS, null);
			realizarVerificacaoAtendimentoOuvidoriaNaoAtendidaNoPrazo(listaConfiguracaoAtendimento);
			realizarVerificacaoAtendimentoDepartamentoNaoAtendidaNoPrazo(listaConfiguracaoAtendimento);
		} catch (Exception e) {
			//System.out.println(e.getMessage());
			//System.out.println("Erro JobNotificacaoOuvidoriaNaoAtendidaNoPrazo...");
		}
	}
	
	public void realizarVerificacaoAtendimentoOuvidoriaNaoAtendidaNoPrazo(List<ConfiguracaoAtendimentoVO> listaConfiguracaoAtendimento) {
		try {						
			for (ConfiguracaoAtendimentoVO configuracao : listaConfiguracaoAtendimento) {
				if (configuracao.getTempoMaximoParaRespostaOuvidoriaPeloOuvidor() > 0) {
					GrupoDestinatariosVO grupo = getFacadeFactory().getGrupoDestinatariosFacade().consultarPorChavePrimaria(configuracao.getGrupoDestinatarioEnviarNotificacaoQuandoOuvidoriaNaoAtendidaNoPrazo().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null);
					for (ConfiguracaoAtendimentoUnidadeEnsinoVO unidade : configuracao.getListaConfiguracaoAtendimentoUnidadeEnsinoVOs()) {
						realizarBuscaOuvidoriaAptaParaNotificacao(configuracao, grupo, unidade.getUnidadeEnsinoVO().getCodigo(), unidade.getUnidadeEnsinoVO().getCidade().getCodigo());
					}
					grupo = null;
				}
			}
		} catch (Exception e) {
			//System.out.println(e.getMessage());
			//System.out.println("Erro JobNotificacaoOuvidoriaNaoAtendidaNoPrazo...");
		}
	}

	public void realizarBuscaOuvidoriaAptaParaNotificacao(ConfiguracaoAtendimentoVO configuracao, GrupoDestinatariosVO grupo, Integer unidadeEnsino, Integer cidadeUnidadeEnsino) throws Exception {
		StringBuilder sql = new StringBuilder("");
		sql.append(" SELECT tabela3.codigo , tabela3.responsavelAtendimento, count(tabela3.dia)  from ( ");
		sql.append(" SELECT tabela2.codigo, tabela2.dia , tabela2.responsavelAtendimento, (EXTRACT(DAY FROM tabela2.dia),EXTRACT(MONTH FROM tabela2.dia),EXTRACT(YEAR FROM tabela2.dia)) as dia_feriado FROM (  ");
		sql.append(" SELECT tabela1.codigo, tabela1.responsavelAtendimento, (tabela1.dataregistro::date + tabela1.datas_geradas * '1 day' ::interval) AS dia from (  ");
		sql.append(" SELECT codigo, dataregistro,  responsavelAtendimento, ");


		/* Uso da data Corrente ate a data em que foi gerado a ouvidoria e verifico cada data para saber se esta em um dia util */
		sql.append(" (generate_series(0, '").append(Uteis.getDataJDBCTimestamp(new Date())).append("'::date - dataregistro::date, 1))  AS datas_geradas  ");
		sql.append(" FROM atendimento  ");
		sql.append(" where unidadeensino = ").append(unidadeEnsino);
		sql.append(" and situacaoAtendimentoEnum <> '").append(SituacaoAtendimentoEnum.FINALIZADA.name()).append("' ");
		sql.append(" and tipoAtendimentoEnum = '").append(TipoAtendimentoEnum.OUVIDORIA.name()).append("' ");
		sql.append(" and atendimentoAtrasado = false ");
		sql.append(" ) as tabela1  ");
		/* o valores BETWEEN 1 AND 5 significa que sao de segunda a sexta feira */
		sql.append(" )as tabela2  WHERE EXTRACT(DOW FROM tabela2.dia) BETWEEN 1 AND 5   ");
		sql.append(" ) as tabela3  ");
		sql.append(" where tabela3.dia_feriado not in (  ");
		sql.append(" select   ");
		sql.append(" case when recorrente = true   ");
		sql.append(" then (EXTRACT(DAY FROM data) , EXTRACT(MONTH FROM data), EXTRACT(YEAR FROM tabela3.dia))   ");
		sql.append(" else (EXTRACT(DAY FROM data) , EXTRACT(MONTH FROM data) , EXTRACT(YEAR FROM data))  ");
		sql.append(" end as dia_feriado  ");
		sql.append(" from feriado  ");
		sql.append(" where (cidade = ").append(cidadeUnidadeEnsino).append("  or cidade is null )  ");
		sql.append(" )group by tabela3.codigo , tabela3.responsavelAtendimento ");
		/*
		 * Some mais 1 no tempo maximo para resposta pois o banco conta o dia
		 * atual e no sistema não
		 */
		sql.append(" having count(tabela3.dia) >   ").append((configuracao.getTempoMaximoParaRespostaOuvidoriaPeloOuvidor() + 1));
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		// Percorro o while nesse metodo pois caso aconteca um erro ele continua a enviar email para proximo grupo.
		while (tabelaResultado.next()) {
			realizarPreenchimentoDadosParaEnvioNotificacaoOuvidoria(tabelaResultado, configuracao, grupo);
		}
		sql = null;
	}

	public void realizarPreenchimentoDadosParaEnvioNotificacaoOuvidoria(SqlRowSet rs, ConfiguracaoAtendimentoVO configuracao, GrupoDestinatariosVO grupo) throws Exception {
		try {
			AtendimentoVO obj = new AtendimentoVO();
			obj.setCodigo(rs.getInt("codigo"));
			obj.setResponsavelAtendimento(getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimariaUnica(rs.getInt("responsavelAtendimento"), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null));
			getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemOuvidoriaAtendimentoForaPrazo(obj, grupo, configuracao, null);
			String sql = "UPDATE Atendimento set atendimentoAtrasado = true WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(sql, new Object[] {obj.getCodigo() });
			obj = null;
			sql=null;
		} catch (Exception e) {
			//System.out.println(e.getMessage());
			//System.out.println("Erro JobNotificacaoOuvidoriaNaoAtendidaNoPrazo...");
		}
	}
	
	public void realizarVerificacaoAtendimentoDepartamentoNaoAtendidaNoPrazo(List<ConfiguracaoAtendimentoVO> listaConfiguracaoAtendimento) {
		try {			 			
			for (ConfiguracaoAtendimentoVO configuracao : listaConfiguracaoAtendimento) {
				if (configuracao.getTempoMaximoParaResponderCadaInteracaoEntreDepartamentos()> 0) {					
					for (ConfiguracaoAtendimentoUnidadeEnsinoVO unidade : configuracao.getListaConfiguracaoAtendimentoUnidadeEnsinoVOs()) {
						realizarBuscaAtendimentoDepartamentoAptaParaNotificacao(configuracao, unidade.getUnidadeEnsinoVO().getCodigo(), unidade.getUnidadeEnsinoVO().getCidade().getCodigo());
					}					
				}
			}
		} catch (Exception e) {
			//System.out.println(e.getMessage());
			//System.out.println("Erro JobNotificacaoOuvidoriaNaoAtendidaNoPrazo...");
		}
	}
	
	public void realizarBuscaAtendimentoDepartamentoAptaParaNotificacao(ConfiguracaoAtendimentoVO configuracao, Integer unidadeEnsino, Integer cidadeUnidadeEnsino) throws Exception {
		StringBuilder sql = new StringBuilder("");
		sql.append(" SELECT tabela3.codigo , tabela3.responsavelAtendimento,  tabela3.funcionario, tabela3.atendimento, count(tabela3.dia)  from ( ");
		sql.append(" SELECT tabela2.codigo, tabela2.dia , tabela2.responsavelAtendimento, tabela2.funcionario, tabela2.atendimento, (EXTRACT(DAY FROM tabela2.dia),EXTRACT(MONTH FROM tabela2.dia),EXTRACT(YEAR FROM tabela2.dia)) as dia_feriado FROM (  ");
		sql.append(" SELECT tabela1.codigo, tabela1.responsavelAtendimento, tabela1.funcionario,  tabela1.atendimento, (tabela1.dataregistro::date + tabela1.datas_geradas * '1 day' ::interval) AS dia from (  ");
		sql.append(" SELECT atendimentointeracaodepartamento.codigo, atendimentointeracaodepartamento.dataregistro,   ");
		sql.append(" atendimento.responsavelAtendimento, atendimentointeracaodepartamento.funcionario, atendimentointeracaodepartamento.atendimento, ");
		/* Uso da data Corrente ate a data em que foi gerado a ouvidoria e verifico cada data para saber se esta em um dia util */
		sql.append(" (generate_series(0, '").append(Uteis.getDataJDBCTimestamp(new Date())).append("'::date - atendimentointeracaodepartamento.dataregistro::date, 1))  AS datas_geradas  ");
		sql.append(" FROM atendimentointeracaodepartamento  ");
		sql.append(" inner join atendimento on atendimento.codigo = atendimentointeracaodepartamento.atendimento  ");
		sql.append(" where atendimento.unidadeensino = ").append(unidadeEnsino);
		sql.append(" and atendimento.situacaoAtendimentoEnum <> '").append(SituacaoAtendimentoEnum.FINALIZADA.name()).append("' ");
		sql.append(" and atendimento.tipoAtendimentoEnum = '").append(TipoAtendimentoEnum.OUVIDORIA.name()).append("' ");
		sql.append(" and atendimentointeracaodepartamento.atendimentoAtrasado = false ");
		sql.append(" ) as tabela1  ");
		/* o valores BETWEEN 1 AND 5 significa que sao de segunda a sexta feira */
		sql.append(" )as tabela2  WHERE EXTRACT(DOW FROM tabela2.dia) BETWEEN 1 AND 5   ");
		sql.append(" ) as tabela3  ");
		sql.append(" where tabela3.dia_feriado not in (  ");
		sql.append(" select   ");
		sql.append(" case when recorrente = true   ");
		sql.append(" then (EXTRACT(DAY FROM data) , EXTRACT(MONTH FROM data), EXTRACT(YEAR FROM tabela3.dia))   ");
		sql.append(" else (EXTRACT(DAY FROM data) , EXTRACT(MONTH FROM data) , EXTRACT(YEAR FROM data))  ");
		sql.append(" end as dia_feriado  ");
		sql.append(" from feriado  ");
		sql.append(" where (cidade = ").append(cidadeUnidadeEnsino).append("  or cidade is null )  ");
		sql.append(" )group by tabela3.codigo , tabela3.responsavelAtendimento, tabela3.funcionario, tabela3.atendimento ");
		/*
		 * Some mais 1 no tempo maximo para resposta pois o banco conta o dia
		 * atual e no sistema não
		 */
		sql.append(" having count(tabela3.dia) >   ").append((configuracao.getTempoMaximoParaResponderCadaInteracaoEntreDepartamentos() + 1));
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		// Percorro o while nesse metodo pois caso aconteca um erro ele continua a enviar email para proximo grupo.
		while (tabelaResultado.next()) {
			realizarPreenchimentoDadosParaEnvioNotificacaoAtendimentoDepartamento(tabelaResultado, configuracao, unidadeEnsino);
		}
		sql = null;
	}

	public void realizarPreenchimentoDadosParaEnvioNotificacaoAtendimentoDepartamento(SqlRowSet rs, ConfiguracaoAtendimentoVO configuracao, Integer unidadeEnsino) throws Exception {
		try {
			AtendimentoInteracaoDepartamentoVO obj = new AtendimentoInteracaoDepartamentoVO();
			obj.setCodigo(rs.getInt("codigo"));
			obj.getAtendimentoVO().setCodigo(rs.getInt("atendimento"));
			obj.setFuncionarioVO(getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimariaUnica(rs.getInt("funcionario"), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null));
			obj.getAtendimentoVO().setResponsavelAtendimento(getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimariaUnica(rs.getInt("responsavelAtendimento"), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null));

			getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemAtendimentoDepartamentoForaPrazo(obj, configuracao, unidadeEnsino, null);
			String sql = "UPDATE atendimentointeracaodepartamento set atendimentoAtrasado = true WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(sql, new Object[] {obj.getCodigo() });
			obj = null;
			sql=null;
		} catch (Exception e) {
			//System.out.println(e.getMessage());
			//System.out.println("Erro JobNotificacaoOuvidoriaNaoAtendidaNoPrazo...");
		}
	}
}
