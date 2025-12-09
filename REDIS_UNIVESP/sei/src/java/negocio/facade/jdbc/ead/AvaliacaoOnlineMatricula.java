package negocio.facade.jdbc.ead;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import jobs.JobAvaliacaoOnline;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.AvaliacaoOnlineMatriculaQuestaoVO;
import negocio.comuns.ead.AvaliacaoOnlineMatriculaRespostaQuestaoVO;
import negocio.comuns.ead.AvaliacaoOnlineMatriculaVO;
import negocio.comuns.ead.AvaliacaoOnlineQuestaoVO;
import negocio.comuns.ead.AvaliacaoOnlineTemaAssuntoVO;
import negocio.comuns.ead.CalendarioAtividadeMatriculaVO;
import negocio.comuns.ead.GraficoAproveitamentoAvaliacaoVO;
import negocio.comuns.ead.OpcaoRespostaQuestaoVO;
import negocio.comuns.ead.enumeradores.SituacaoAtividadeEnum;
import negocio.comuns.ead.enumeradores.SituacaoAtividadeRespostaEnum;
import negocio.comuns.ead.enumeradores.SituacaoAvaliacaoOnlineMatriculaEnum;
import negocio.comuns.ead.enumeradores.TipoControleLiberarAcessoProximaDisciplinaEnum;
import negocio.comuns.ead.enumeradores.TipoGeracaoProvaOnlineEnum;
import negocio.comuns.ead.enumeradores.TipoQuestaoEnum;
import negocio.comuns.ead.enumeradores.TipoUsoEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.ead.AvaliacaoOnlineMatriculaInterfaceFacade;

/**
 * @author Victor Hugo 10/10/2014
 */
@Repository
@Scope("singleton")
@Lazy
public class AvaliacaoOnlineMatricula extends ControleAcesso implements AvaliacaoOnlineMatriculaInterfaceFacade, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public static String getIdEntidade() {
		if (idEntidade == null) {
			idEntidade = "";
		}
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		AvaliacaoOnlineMatricula.idEntidade = idEntidade;
	}

	public AvaliacaoOnlineMatricula() throws Exception {
		super();
		setIdEntidade("AvaliacaoOnlineMatricula");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void incluir(final AvaliacaoOnlineMatriculaVO avaliacaoOnlineMatriculaVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			validarDados(avaliacaoOnlineMatriculaVO);
			AvaliacaoOnlineMatricula.incluir(getIdEntidade(), verificarAcesso, usuarioVO);
			final String sql = "INSERT INTO avaliacaoonlinematricula(" + " matricula, matriculaperiodoturmadisciplina, avaliacaoonline," + " nota, situacaoavaliacaoonlinematricula, datainicio, datalimitetermino," + " datatermino, quantidadeacertos, quantidadeerros, quantidadenaorespondida," + " percentualacerto, percentualerro, percentualnaorespondido, configuracaoead, notalancadanohistorico)" + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			avaliacaoOnlineMatriculaVO.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql);
					sqlInserir.setString(1, avaliacaoOnlineMatriculaVO.getMatriculaVO().getMatricula());
					sqlInserir.setInt(2, avaliacaoOnlineMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO().getCodigo());
					sqlInserir.setInt(3, avaliacaoOnlineMatriculaVO.getAvaliacaoOnlineVO().getCodigo());
					sqlInserir.setDouble(4, avaliacaoOnlineMatriculaVO.getNota());
					sqlInserir.setString(5, avaliacaoOnlineMatriculaVO.getSituacaoAvaliacaoOnlineMatriculaEnum().name());
					sqlInserir.setTimestamp(6, Uteis.getDataJDBCTimestamp(avaliacaoOnlineMatriculaVO.getDataInicio()));
					sqlInserir.setTimestamp(7, Uteis.getDataJDBCTimestamp(avaliacaoOnlineMatriculaVO.getDataLimiteTermino()));
					sqlInserir.setTimestamp(8, Uteis.getDataJDBCTimestamp(avaliacaoOnlineMatriculaVO.getDataTermino()));
					sqlInserir.setInt(9, avaliacaoOnlineMatriculaVO.getQuantidadeAcertos());
					sqlInserir.setInt(10, avaliacaoOnlineMatriculaVO.getQuantidadeErros());
					sqlInserir.setInt(11, avaliacaoOnlineMatriculaVO.getQuantidadeNaoRespondida());
					sqlInserir.setDouble(12, avaliacaoOnlineMatriculaVO.getPercentualAcerto());
					sqlInserir.setDouble(13, avaliacaoOnlineMatriculaVO.getPercentualErro());
					sqlInserir.setDouble(14, avaliacaoOnlineMatriculaVO.getPercentualNaoRespondido());
					sqlInserir.setInt(15, avaliacaoOnlineMatriculaVO.getConfiguracaoEADVO().getCodigo());
					sqlInserir.setBoolean(16, avaliacaoOnlineMatriculaVO.getNotaLancadaNoHistorico());

					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(final ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						avaliacaoOnlineMatriculaVO.setNovoObj(Boolean.FALSE);
						return rs.getInt("codigo");
					}
					return null;
				}
			}));
			getFacadeFactory().getAvaliacaoOnlineMatriculaQuestaoInterfaceFacade().persistirQuestoesAvaliacaoOnlineMatricula(avaliacaoOnlineMatriculaVO, usuarioVO);
		} catch (Exception e) {
			avaliacaoOnlineMatriculaVO.setNovoObj(Boolean.TRUE);
			avaliacaoOnlineMatriculaVO.setCodigo(0);
			throw e;
		}
	}

	@Override
	public void validarDados(AvaliacaoOnlineMatriculaVO avaliacaoOnlineMatriculaVO) throws Exception {

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(AvaliacaoOnlineMatriculaVO avaliacaoOnlineMatriculaVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		if (avaliacaoOnlineMatriculaVO.getCodigo() == 0) {
			incluir(avaliacaoOnlineMatriculaVO, verificarAcesso, usuarioVO);
		} else {
			alterar(avaliacaoOnlineMatriculaVO, verificarAcesso, usuarioVO);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final AvaliacaoOnlineMatriculaVO avaliacaoOnlineMatriculaVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			AvaliacaoOnlineMatricula.alterar(getIdEntidade(), verificarAcesso, usuarioVO);
			final String sql = "UPDATE avaliacaoonlinematricula" + " SET matricula=?, matriculaperiodoturmadisciplina=?, avaliacaoonline=?," + " nota=?, situacaoavaliacaoonlinematricula=?, datainicio=?, datalimitetermino=?," + " datatermino=?, quantidadeacertos=?, quantidadeerros=?, quantidadenaorespondida=?," + " percentualacerto=?, percentualerro=?, percentualnaorespondido=?, configuracaoead = ?, notalancadanohistorico = ? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql);

					sqlAlterar.setString(1, avaliacaoOnlineMatriculaVO.getMatriculaVO().getMatricula());
					sqlAlterar.setInt(2, avaliacaoOnlineMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO().getCodigo());
					sqlAlterar.setInt(3, avaliacaoOnlineMatriculaVO.getAvaliacaoOnlineVO().getCodigo());
					sqlAlterar.setDouble(4, avaliacaoOnlineMatriculaVO.getNota());
					sqlAlterar.setString(5, avaliacaoOnlineMatriculaVO.getSituacaoAvaliacaoOnlineMatriculaEnum().name());
					sqlAlterar.setTimestamp(6, Uteis.getDataJDBCTimestamp(avaliacaoOnlineMatriculaVO.getDataInicio()));
					sqlAlterar.setTimestamp(7, Uteis.getDataJDBCTimestamp(avaliacaoOnlineMatriculaVO.getDataLimiteTermino()));
					sqlAlterar.setTimestamp(8, Uteis.getDataJDBCTimestamp(avaliacaoOnlineMatriculaVO.getDataTermino()));
					sqlAlterar.setInt(9, avaliacaoOnlineMatriculaVO.getQuantidadeAcertos());
					sqlAlterar.setInt(10, avaliacaoOnlineMatriculaVO.getQuantidadeErros());
					sqlAlterar.setInt(11, avaliacaoOnlineMatriculaVO.getQuantidadeNaoRespondida());
					sqlAlterar.setDouble(12, avaliacaoOnlineMatriculaVO.getPercentualAcerto());
					sqlAlterar.setDouble(13, avaliacaoOnlineMatriculaVO.getPercentualErro());
					sqlAlterar.setDouble(14, avaliacaoOnlineMatriculaVO.getPercentualNaoRespondido());
					sqlAlterar.setInt(15, avaliacaoOnlineMatriculaVO.getConfiguracaoEADVO().getCodigo());
					sqlAlterar.setBoolean(16, avaliacaoOnlineMatriculaVO.getNotaLancadaNoHistorico());
					sqlAlterar.setInt(17, avaliacaoOnlineMatriculaVO.getCodigo());

					return sqlAlterar;
				}
			});
			getFacadeFactory().getAvaliacaoOnlineMatriculaQuestaoInterfaceFacade().persistirQuestoesAvaliacaoOnlineMatricula(avaliacaoOnlineMatriculaVO, usuarioVO);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(final AvaliacaoOnlineMatriculaVO avaliacaoOnlineMatriculaVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			AvaliacaoOnlineMatricula.excluir(getIdEntidade(), verificarAcesso, usuarioVO);
			String sql = "DELETE FROM avaliacaoonlinematricula WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(sql, avaliacaoOnlineMatriculaVO.getCodigo());
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirPorCalendarioAtividadeMatricula(List<CalendarioAtividadeMatriculaVO> listaCalendario, UsuarioVO usuarioVO) throws Exception {
		try {
			StringBuilder sb = new StringBuilder("DELETE FROM avaliacaoonlinematricula ");
			sb.append(" WHERE codigo in ( 0  ");
            for (CalendarioAtividadeMatriculaVO obj : listaCalendario) {
            	sb.append(", ").append(obj.getAvaliacaoOnlineMatriculaVO().getCodigo());	
            }
            sb.append(" ) ");
            sb.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
            getConexao().getJdbcTemplate().execute(sb.toString());
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public AvaliacaoOnlineMatriculaVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		AvaliacaoOnlineMatriculaVO obj = new AvaliacaoOnlineMatriculaVO();

		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.getMatriculaVO().setMatricula(tabelaResultado.getString("matricula"));
		obj.getMatriculaPeriodoTurmaDisciplinaVO().setCodigo(tabelaResultado.getInt("matriculaperiodoturmadisciplina"));
		obj.getAvaliacaoOnlineVO().setCodigo(tabelaResultado.getInt("avaliacaoonline"));
		obj.setNota(tabelaResultado.getDouble("nota"));
		obj.setSituacaoAvaliacaoOnlineMatriculaEnum(SituacaoAvaliacaoOnlineMatriculaEnum.valueOf(tabelaResultado.getString("situacaoavaliacaoonlinematricula")));
		obj.setDataInicio(Uteis.getDataJDBCTimestamp(tabelaResultado.getTimestamp("datainicio")));
		obj.setDataLimiteTermino(Uteis.getDataJDBCTimestamp(tabelaResultado.getTimestamp("datalimitetermino")));
		obj.setDataTermino(Uteis.getDataJDBCTimestamp(tabelaResultado.getTimestamp("datatermino")));
		obj.setQuantidadeAcertos(tabelaResultado.getInt("quantidadeacertos"));
		obj.setQuantidadeErros(tabelaResultado.getInt("quantidadeerros"));
		obj.setQuantidadeNaoRespondida(tabelaResultado.getInt("quantidadenaorespondida"));
		obj.setPercentualAcerto(tabelaResultado.getDouble("percentualacerto"));
		obj.setPercentualErro(tabelaResultado.getDouble("percentualerro"));
		obj.setPercentualNaoRespondido(tabelaResultado.getDouble("percentualnaorespondido"));
		obj.getConfiguracaoEADVO().setCodigo(tabelaResultado.getInt("configuracaoead"));
		obj.setNotaLancadaNoHistorico(tabelaResultado.getBoolean("notalancadanohistorico"));
		obj.setDataFimLiberacao(getFacadeFactory().getCalendarioAtividadeMatriculaInterfaceFacade().consultarDataFimAtividadeDiscursiva(tabelaResultado.getInt("avaliacaoonline"), tabelaResultado.getString("matricula"), usuarioLogado));
		
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			obj.setAvaliacaoOnlineVO(getFacadeFactory().getAvaliacaoOnlineInterfaceFacade().consultarPorChavePrimaria(obj.getAvaliacaoOnlineVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado));
		}

		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_TODOS) {
			obj.setAvaliacaoOnlineVO(getFacadeFactory().getAvaliacaoOnlineInterfaceFacade().consultarPorChavePrimaria(obj.getAvaliacaoOnlineVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado));
			obj.setConfiguracaoEADVO(getFacadeFactory().getConfiguracaoEADFacade().consultarPorChavePrimaria(obj.getConfiguracaoEADVO().getCodigo()));
			obj.setAvaliacaoOnlineMatriculaQuestaoVOs(getFacadeFactory().getAvaliacaoOnlineMatriculaQuestaoInterfaceFacade().consultarPorAvaliacaoOnlineMatricula(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado));
			return obj;
		}
		return obj;
	}

	@Override
	public List<AvaliacaoOnlineMatriculaVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		List<AvaliacaoOnlineMatriculaVO> vetResultado = new ArrayList<AvaliacaoOnlineMatriculaVO>();
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuarioLogado));
		}

		return vetResultado;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public AvaliacaoOnlineMatriculaVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		String sql = "SELECT * FROM avaliacaoonlinematricula WHERE codigo = ?";
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql, codigo);
		if (rs.next()) {
			return (montarDados(rs, nivelMontarDados, usuarioLogado));
		}
		return new AvaliacaoOnlineMatriculaVO();
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public synchronized void executarCorrecaoAvaliacaoOnline(AvaliacaoOnlineMatriculaVO obj, CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO, boolean validarPerguntasRespondidas, UsuarioVO usuarioVO, boolean simulandoAcesso) throws Exception {
		if (obj.getSituacaoAvaliacaoOnlineMatriculaEnum().equals(SituacaoAvaliacaoOnlineMatriculaEnum.EM_REALIZACAO)) {
			try {
 				List<Integer> questoesNaoRespondida = new ArrayList<Integer>(0);
				obj.setQuantidadeAcertos(0);
				obj.setQuantidadeErros(0);
				obj.setNota(null);
				obj.setCodigoPrimeiraQuestaoNaoRespondida(-1);
				for (AvaliacaoOnlineMatriculaQuestaoVO avaliacaoOnlineMatriculaQuestaoVO : obj.getAvaliacaoOnlineMatriculaQuestaoVOs()) {
					if (avaliacaoOnlineMatriculaQuestaoVO.getAvaliacaoOnlineMatriculaVO().getAvaliacaoOnlineVO().getCodigo().intValue() == 0) {
						avaliacaoOnlineMatriculaQuestaoVO.setAvaliacaoOnlineMatriculaVO(obj);
					}
					getFacadeFactory().getAvaliacaoOnlineMatriculaQuestaoInterfaceFacade().realizarCorrecaoQuestao(avaliacaoOnlineMatriculaQuestaoVO);
					if (avaliacaoOnlineMatriculaQuestaoVO.getQuestaoVO().getQuestaoNaoRespondida()) {
						questoesNaoRespondida.add(avaliacaoOnlineMatriculaQuestaoVO.getOrdemApresentacao());
						if(obj.getCodigoPrimeiraQuestaoNaoRespondida() < 0) {
							obj.setCodigoPrimeiraQuestaoNaoRespondida(avaliacaoOnlineMatriculaQuestaoVO.getOrdemApresentacao()-1);
						}
					}
					if (avaliacaoOnlineMatriculaQuestaoVO.getQuestaoVO().getAcertouQuestao()) {
						obj.setQuantidadeAcertos((obj.getQuantidadeAcertos() + 1));
					}
					if (avaliacaoOnlineMatriculaQuestaoVO.getQuestaoVO().getErrouQuestao()) {
						obj.setQuantidadeErros((obj.getQuantidadeErros() + 1));
					}
				}
				obj.setQuantidadeNaoRespondida(questoesNaoRespondida.size());
				obj.setQuantidadeErros(obj.getQuantidadeErros() + obj.getQuantidadeNaoRespondida());
				obj.setNota(Uteis.arrendondarForcando2CadasDecimais(obj.getNota()));
				obj.setPercentualAcerto(Uteis.arrendondarForcando2CadasDecimais(((obj.getQuantidadeAcertos().doubleValue() / obj.getAvaliacaoOnlineMatriculaQuestaoVOs().size()) * 100)));
				obj.setPercentualErro(Uteis.arrendondarForcando2CadasDecimais(((obj.getQuantidadeErros().doubleValue() / obj.getAvaliacaoOnlineMatriculaQuestaoVOs().size()) * 100)));
				obj.setPercentualNaoRespondido(Uteis.arrendondarForcando2CadasDecimais(((obj.getQuantidadeNaoRespondida().doubleValue() / obj.getAvaliacaoOnlineMatriculaQuestaoVOs().size()) * 100)));

				if (validarPerguntasRespondidas && !questoesNaoRespondida.isEmpty()) {
					if (questoesNaoRespondida.size() == 1) {
						throw new Exception("O Exercício " + questoesNaoRespondida.get(0) + " não está respondido.");
					}
					StringBuilder msg = new StringBuilder("Os Exercícios ");
					int x = 1;
					for (Integer questao : questoesNaoRespondida) {
						if (x > 1 && x < questoesNaoRespondida.size()) {
							msg.append(", ");
						}
						if (x == questoesNaoRespondida.size()) {
							msg.append(" e ");
						}
						msg.append(questao);
						x++;
					}
					msg.append(" devem ser respondidos.");
					throw new Exception(msg.toString());
				} else {
					if(!simulandoAcesso) {
						obj.setMatriculaPeriodoTurmaDisciplinaVO(getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarPorChavePrimaria(obj.getMatriculaPeriodoTurmaDisciplinaVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO));
					}
					if (obj.getAvaliacaoOnlineVO().getTipoUso().isRea()) {
						if (obj.getNota() >= ((obj.getAvaliacaoOnlineVO().getNotaMaximaAvaliacao() * obj.getAvaliacaoOnlineVO().getPercentualAprovacao()) / 100)) {
							obj.setSituacaoAvaliacaoOnlineMatriculaEnum(SituacaoAvaliacaoOnlineMatriculaEnum.APROVADO);
						} else {
							obj.setSituacaoAvaliacaoOnlineMatriculaEnum(SituacaoAvaliacaoOnlineMatriculaEnum.REPROVADO);
							if(!simulandoAcesso) {
							   getFacadeFactory().getCalendarioAtividadeMatriculaInterfaceFacade().realizarCriacaoCalendarioAtividadeMatriculaAvaliacaoOnline(obj, calendarioAtividadeMatriculaVO, usuarioVO);
							}
						}
					}else{
						if (obj.getNota() >= ((obj.getAvaliacaoOnlineVO().getNotaMaximaAvaliacao() * obj.getAvaliacaoOnlineVO().getPercentualAprovacao()) / 100)) {
							obj.setSituacaoAvaliacaoOnlineMatriculaEnum(SituacaoAvaliacaoOnlineMatriculaEnum.APROVADO);
							if (obj.getConfiguracaoEADVO().getTipoControleLiberarAcessoProximaDisciplina().equals(TipoControleLiberarAcessoProximaDisciplinaEnum.APROVADO_AVALIACAO_ONLINE) && !simulandoAcesso) {
								getFacadeFactory().getCalendarioAtividadeMatriculaInterfaceFacade().verificarRealizacaoProximaDisciplinaAprovadoAvaliacaoOnline(obj.getMatriculaPeriodoTurmaDisciplinaVO(), obj.getDataTermino(), obj.getConfiguracaoEADVO(), usuarioVO);
							}
						} else {
							obj.setSituacaoAvaliacaoOnlineMatriculaEnum(SituacaoAvaliacaoOnlineMatriculaEnum.REPROVADO);
							if(!simulandoAcesso) {
								obj.setConfiguracaoEADVO(getFacadeFactory().getConfiguracaoEADFacade().consultarPorChavePrimaria(obj.getConfiguracaoEADVO().getCodigo()));
								getFacadeFactory().getCalendarioAtividadeMatriculaInterfaceFacade().realizarCriacaoCalendarioAtividadeMatriculaAvaliacaoOnline(obj, calendarioAtividadeMatriculaVO, usuarioVO);
							}
						}						
					}
					if(!simulandoAcesso) {
					String variavelNotaCfgUtilizar = Uteis.isAtributoPreenchido(obj.getAvaliacaoOnlineVO().getVariavelNotaCfgPadraoAvaliacaoOnlineRea()) ? obj.getAvaliacaoOnlineVO().getVariavelNotaCfgPadraoAvaliacaoOnlineRea() : obj.getConfiguracaoEADVO().getVariavelNotaCfgPadraoAvaliacaoOnline();
					// Uteis.checkState(!Uteis.isAtributoPreenchido(variavelNotaCfgUtilizar), "Não
					// foi localizado a Variável de Nota da Avaliacao Online. Por favor verificar o
					// Cadastrado da avaliação online ou a configuração do EAD.");
					obj.setNotaLancadaNoHistorico(getFacadeFactory().getHistoricoFacade().realizarLancamentoNotaHistoricoAutomaticamente(variavelNotaCfgUtilizar, obj.getNota(), obj.getMatriculaPeriodoTurmaDisciplinaVO(), obj.getConfiguracaoEADVO().getCalcularMediaFinalAposRealizacaoAvaliacaoOnline(), usuarioVO));
					calendarioAtividadeMatriculaVO = getFacadeFactory().getCalendarioAtividadeMatriculaInterfaceFacade().consultarPorChavePrimaria(calendarioAtividadeMatriculaVO.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
					calendarioAtividadeMatriculaVO.setSituacaoAtividade(SituacaoAtividadeEnum.CONCLUIDA);
					calendarioAtividadeMatriculaVO.setDateRealizado(new Date());
					getFacadeFactory().getCalendarioAtividadeMatriculaInterfaceFacade().alterar(calendarioAtividadeMatriculaVO, false, usuarioVO);
					obj.setDataTermino(new Date());
					persistir(obj, false, usuarioVO);
					}

				}
			} catch (Exception e) {
				obj.setSituacaoAvaliacaoOnlineMatriculaEnum(SituacaoAvaliacaoOnlineMatriculaEnum.EM_REALIZACAO);
				throw e;
			}
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public synchronized void realizarGeracaoAvaliacaoOnlineRea(AvaliacaoOnlineMatriculaVO avaliacaoOnlineMatriculaVO, CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO, UsuarioVO usuarioVO) throws Exception {
		realizarGeracaoQuestoesRandomicamente(avaliacaoOnlineMatriculaVO, calendarioAtividadeMatriculaVO, avaliacaoOnlineMatriculaVO.getAvaliacaoOnlineVO().getAvaliacaoOnlineTemaAssuntoVOs(), usuarioVO, false);
		avaliacaoOnlineMatriculaVO.setSituacaoAvaliacaoOnlineMatriculaEnum(SituacaoAvaliacaoOnlineMatriculaEnum.EM_REALIZACAO);
		avaliacaoOnlineMatriculaVO.setConfiguracaoEADVO(getFacadeFactory().getConfiguracaoEADFacade().consultarPorChavePrimaria(avaliacaoOnlineMatriculaVO.getConfiguracaoEADVO().getCodigo()));
		avaliacaoOnlineMatriculaVO.setDataInicioLiberacao(calendarioAtividadeMatriculaVO.getDataInicio());
		avaliacaoOnlineMatriculaVO.setDataFimLiberacao(calendarioAtividadeMatriculaVO.getDataFim());
		avaliacaoOnlineMatriculaVO.setDataInicio(new Date());
		validarDataLimiteAvaliacaoOnlineMatricula(avaliacaoOnlineMatriculaVO, false);
		if(!Uteis.isAtributoPreenchido(calendarioAtividadeMatriculaVO.getCodigo())){
			throw new Exception(" Não foi encontrado um calendário para do tipo Avaliação Online Rea.");
		}
		calendarioAtividadeMatriculaVO.setAvaliacaoOnlineMatriculaVO(avaliacaoOnlineMatriculaVO);
		if(!usuarioVO.getPermiteSimularNavegacaoAluno()){
			persistir(calendarioAtividadeMatriculaVO.getAvaliacaoOnlineMatriculaVO(), false, usuarioVO);
			getFacadeFactory().getCalendarioAtividadeMatriculaInterfaceFacade().persistir(calendarioAtividadeMatriculaVO, false, usuarioVO);
			Thread threadAvalOn = new Thread(new JobAvaliacaoOnline(calendarioAtividadeMatriculaVO.getCodigo(), avaliacaoOnlineMatriculaVO.getDataLimiteTerminoTemporizador()));
			threadAvalOn.start();
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public synchronized void realizarGeracaoAvaliacaoOnline(AvaliacaoOnlineMatriculaVO avaliacaoOnlineMatriculaVO, CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO, String matricula, Integer codigoMatriculaPeriodoTurmaDisciplina, Integer codigoTurma, Integer codigoDisciplina, String ano, String semestre, List<AvaliacaoOnlineTemaAssuntoVO> avaliacaoOnlineTemaAssuntoVOs, UsuarioVO usuarioVO, boolean simulandoAcesso, Boolean simulandoAvaliacao) throws Exception {
		avaliacaoOnlineMatriculaVO.getMatriculaVO().setMatricula(matricula);
		
		if(!simulandoAcesso || (simulandoAcesso && Uteis.isAtributoPreenchido(codigoMatriculaPeriodoTurmaDisciplina))){
			avaliacaoOnlineMatriculaVO.setMatriculaPeriodoTurmaDisciplinaVO(getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarPorChavePrimaria(codigoMatriculaPeriodoTurmaDisciplina, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO));
		}
		realizarGeracaoQuestoesRandomicamente(avaliacaoOnlineMatriculaVO, calendarioAtividadeMatriculaVO, avaliacaoOnlineTemaAssuntoVOs, usuarioVO, simulandoAcesso);
		if(!simulandoAcesso){
			validarAvaliacaoIniciada(matricula, avaliacaoOnlineMatriculaVO.getAvaliacaoOnlineVO().getCodigo());
		}
		avaliacaoOnlineMatriculaVO.setSituacaoAvaliacaoOnlineMatriculaEnum(SituacaoAvaliacaoOnlineMatriculaEnum.EM_REALIZACAO);
		avaliacaoOnlineMatriculaVO.setDataInicioLiberacao(calendarioAtividadeMatriculaVO.getDataInicio());
		avaliacaoOnlineMatriculaVO.setDataFimLiberacao(calendarioAtividadeMatriculaVO.getDataFim());
		avaliacaoOnlineMatriculaVO.setDataInicio(new Date());
		if(!simulandoAcesso || (simulandoAcesso && Uteis.isAtributoPreenchido(avaliacaoOnlineMatriculaVO.getConfiguracaoEADVO().getCodigo()))){
			avaliacaoOnlineMatriculaVO.setConfiguracaoEADVO(getFacadeFactory().getConfiguracaoEADFacade().consultarPorChavePrimaria(avaliacaoOnlineMatriculaVO.getConfiguracaoEADVO().getCodigo()));
		}
		validarDataLimiteAvaliacaoOnlineMatricula(avaliacaoOnlineMatriculaVO, simulandoAvaliacao);
		calendarioAtividadeMatriculaVO.setAvaliacaoOnlineMatriculaVO(avaliacaoOnlineMatriculaVO);
		if(!simulandoAcesso){
			persistir(calendarioAtividadeMatriculaVO.getAvaliacaoOnlineMatriculaVO(), false, usuarioVO);
			getFacadeFactory().getCalendarioAtividadeMatriculaInterfaceFacade().persistir(calendarioAtividadeMatriculaVO, false, usuarioVO);
			Thread threadAvalOn = new Thread(new JobAvaliacaoOnline(calendarioAtividadeMatriculaVO.getCodigo(), avaliacaoOnlineMatriculaVO.getDataLimiteTerminoTemporizador()));
			threadAvalOn.start();
		}
	}

	private void validarDataLimiteAvaliacaoOnlineMatricula(AvaliacaoOnlineMatriculaVO avaliacaoOnlineMatriculaVO, Boolean simulandoAvaliacao) throws ParseException {
		avaliacaoOnlineMatriculaVO.setDataLimiteTermino(UteisData.getAdicionarDataAtualSomandoOuSubtraindoMinutos(avaliacaoOnlineMatriculaVO.getDataInicio(), avaliacaoOnlineMatriculaVO.getAvaliacaoOnlineVO().getTempoLimiteRealizacaoAvaliacaoOnline()));
		if(!simulandoAvaliacao && avaliacaoOnlineMatriculaVO.getAvaliacaoOnlineVO().isLimiteTempoProvaAlunoDentroPeriodoRealizacao() 
				&& (UteisData.validarDataInicialMaiorFinalComHora(avaliacaoOnlineMatriculaVO.getDataLimiteTermino(), avaliacaoOnlineMatriculaVO.getDataFimLiberacao()))) {
			avaliacaoOnlineMatriculaVO.setDataLimiteTermino(avaliacaoOnlineMatriculaVO.getDataFimLiberacao());
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void realizarGeracaoQuestoesRandomicamente(AvaliacaoOnlineMatriculaVO avaliacaoOnlineMatriculaVO, CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO, List<AvaliacaoOnlineTemaAssuntoVO> avaliacaoOnlineTemaAssuntoVOs, UsuarioVO usuarioVO, boolean simulandoAvaliacao) throws Exception {
		AvaliacaoOnlineMatriculaQuestaoVO obj = null;
		AvaliacaoOnlineMatriculaRespostaQuestaoVO respostaOnline = null;
		if(avaliacaoOnlineMatriculaVO.getAvaliacaoOnlineVO().getTipoGeracaoProvaOnline().equals(TipoGeracaoProvaOnlineEnum.RANDOMICO_POR_COMPLEXIDADE)) {
			avaliacaoOnlineMatriculaVO.getAvaliacaoOnlineVO().getAvaliacaoOnlineQuestaoVOs().clear();
		}
		
		if ((Uteis.isAtributoPreenchido(avaliacaoOnlineMatriculaVO.getAvaliacaoOnlineVO()) || simulandoAvaliacao) && avaliacaoOnlineMatriculaVO.getAvaliacaoOnlineVO().getTipoGeracaoProvaOnline().equals(TipoGeracaoProvaOnlineEnum.RANDOMICO_POR_COMPLEXIDADE)) {
			if(!simulandoAvaliacao  || usuarioVO.getIsApresentarVisaoAluno()) {
				avaliacaoOnlineTemaAssuntoVOs =  getFacadeFactory().getAvaliacaoOnlineTemaAssuntoInterfaceFacade().consultarAvaliacaoOnlineTemaAssuntoPorAvaliacaoOnline(avaliacaoOnlineMatriculaVO.getAvaliacaoOnlineVO().getCodigo(), usuarioVO);
			}
			avaliacaoOnlineMatriculaVO.getAvaliacaoOnlineVO().setAvaliacaoOnlineQuestaoVOs(getFacadeFactory().getAvaliacaoOnlineQuestaoInterfaceFacade().gerarQuestoesRandomicamente(avaliacaoOnlineMatriculaVO.getAvaliacaoOnlineVO(), avaliacaoOnlineMatriculaVO.getAvaliacaoOnlineVO().getQuantidadeNivelQuestaoFacil(), avaliacaoOnlineMatriculaVO.getAvaliacaoOnlineVO().getQuantidadeNivelQuestaoMedio(), avaliacaoOnlineMatriculaVO.getAvaliacaoOnlineVO().getQuantidadeNivelQuestaoDificil(), avaliacaoOnlineMatriculaVO.getAvaliacaoOnlineVO().getQuantidadeQualquerNivelQuestao(), avaliacaoOnlineMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO(), avaliacaoOnlineTemaAssuntoVOs, usuarioVO));
		} else if (!Uteis.isAtributoPreenchido(avaliacaoOnlineMatriculaVO.getAvaliacaoOnlineVO()) && (calendarioAtividadeMatriculaVO.getTipoOrigem().isAvaliacaoOnlineTurma())) {
			if(!simulandoAvaliacao  || usuarioVO.getIsApresentarVisaoAluno()) {
				avaliacaoOnlineMatriculaVO.setAvaliacaoOnlineVO(getFacadeFactory().getAvaliacaoOnlineInterfaceFacade().consultarPorChavePrimaria(Integer.parseInt(calendarioAtividadeMatriculaVO.getCodOrigem()), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO));			
				avaliacaoOnlineTemaAssuntoVOs = avaliacaoOnlineMatriculaVO.getAvaliacaoOnlineVO().getAvaliacaoOnlineTemaAssuntoVOs();
			}
			if (Uteis.isAtributoPreenchido(avaliacaoOnlineMatriculaVO.getAvaliacaoOnlineVO()) && avaliacaoOnlineMatriculaVO.getAvaliacaoOnlineVO().getTipoGeracaoProvaOnline().equals(TipoGeracaoProvaOnlineEnum.RANDOMICO_POR_COMPLEXIDADE)) {
				avaliacaoOnlineMatriculaVO.getAvaliacaoOnlineVO().setAvaliacaoOnlineQuestaoVOs(getFacadeFactory().getAvaliacaoOnlineQuestaoInterfaceFacade().gerarQuestoesRandomicamente(avaliacaoOnlineMatriculaVO.getAvaliacaoOnlineVO(), avaliacaoOnlineMatriculaVO.getAvaliacaoOnlineVO().getQuantidadeNivelQuestaoFacil(), avaliacaoOnlineMatriculaVO.getAvaliacaoOnlineVO().getQuantidadeNivelQuestaoMedio(), avaliacaoOnlineMatriculaVO.getAvaliacaoOnlineVO().getQuantidadeNivelQuestaoDificil(), avaliacaoOnlineMatriculaVO.getAvaliacaoOnlineVO().getQuantidadeQualquerNivelQuestao(), avaliacaoOnlineMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO(), avaliacaoOnlineTemaAssuntoVOs, usuarioVO));
			}
		} else if (calendarioAtividadeMatriculaVO.getTipoOrigem().isTipoOrigemRea()) {
			if(!simulandoAvaliacao  || usuarioVO.getIsApresentarVisaoAluno()) {
				avaliacaoOnlineMatriculaVO.setAvaliacaoOnlineVO(getFacadeFactory().getAvaliacaoOnlineInterfaceFacade().consultarAvaliacaoOnlinePorConteudoUnidadePaginaRecursoEducacional(Integer.parseInt(calendarioAtividadeMatriculaVO.getCodOrigem()), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO));			
				avaliacaoOnlineTemaAssuntoVOs = avaliacaoOnlineMatriculaVO.getAvaliacaoOnlineVO().getAvaliacaoOnlineTemaAssuntoVOs();
			}
			if (Uteis.isAtributoPreenchido(avaliacaoOnlineMatriculaVO.getAvaliacaoOnlineVO()) && avaliacaoOnlineMatriculaVO.getAvaliacaoOnlineVO().getTipoGeracaoProvaOnline().equals(TipoGeracaoProvaOnlineEnum.RANDOMICO_POR_COMPLEXIDADE)) {
				avaliacaoOnlineMatriculaVO.getAvaliacaoOnlineVO().setAvaliacaoOnlineQuestaoVOs(getFacadeFactory().getAvaliacaoOnlineQuestaoInterfaceFacade().gerarQuestoesRandomicamente(avaliacaoOnlineMatriculaVO.getAvaliacaoOnlineVO(), avaliacaoOnlineMatriculaVO.getAvaliacaoOnlineVO().getQuantidadeNivelQuestaoFacil(), avaliacaoOnlineMatriculaVO.getAvaliacaoOnlineVO().getQuantidadeNivelQuestaoMedio(), avaliacaoOnlineMatriculaVO.getAvaliacaoOnlineVO().getQuantidadeNivelQuestaoDificil(), avaliacaoOnlineMatriculaVO.getAvaliacaoOnlineVO().getQuantidadeQualquerNivelQuestao(), avaliacaoOnlineMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO(), avaliacaoOnlineTemaAssuntoVOs, usuarioVO));
			}
		}
		else if (!Uteis.isAtributoPreenchido(avaliacaoOnlineMatriculaVO.getAvaliacaoOnlineVO())) {
			if(!simulandoAvaliacao || usuarioVO.getIsApresentarVisaoAluno()) {
				avaliacaoOnlineMatriculaVO.setAvaliacaoOnlineVO(getFacadeFactory().getAvaliacaoOnlineInterfaceFacade().consultarAvaliacaoOnlineDaTurmaDisciplinaAnoSemestreOuDefaultTurma(avaliacaoOnlineMatriculaVO, calendarioAtividadeMatriculaVO, usuarioVO));
			}
			
		}
		if (avaliacaoOnlineMatriculaVO.getAvaliacaoOnlineVO().getAvaliacaoOnlineQuestaoVOs().isEmpty()) {
			throw new Exception("Nenhuma Questão encontrada para geração da Avaliação Online Randomicamente.");
		}
		getFacadeFactory().getAvaliacaoOnlineInterfaceFacade().validarQuantidadeQuestoesPorNivel(avaliacaoOnlineMatriculaVO.getAvaliacaoOnlineVO(), avaliacaoOnlineMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO(), usuarioVO);

		for (AvaliacaoOnlineQuestaoVO avaliacaoOnlineQuestaoVO : avaliacaoOnlineMatriculaVO.getAvaliacaoOnlineVO().getAvaliacaoOnlineQuestaoVOs()) {
			obj = new AvaliacaoOnlineMatriculaQuestaoVO();
			obj.setQuestaoVO(avaliacaoOnlineQuestaoVO.getQuestaoVO());
			obj.setAvaliacaoOnlineMatriculaVO(avaliacaoOnlineMatriculaVO);
			obj.setOrdemApresentacao(avaliacaoOnlineQuestaoVO.getOrdemApresentacao());
			for (OpcaoRespostaQuestaoVO resposta : avaliacaoOnlineQuestaoVO.getQuestaoVO().getOpcaoRespostaQuestaoVOs()) {
				respostaOnline = new AvaliacaoOnlineMatriculaRespostaQuestaoVO();
				respostaOnline.setOpcaoRespostaQuestaoVO(resposta);
				respostaOnline.setAvaliacaoOnlineMatriculaQuestaoVO(obj);
				obj.getAvaliacaoOnlineMatriculaRespostaQuestaoVOs().add(respostaOnline);
			}
			avaliacaoOnlineMatriculaVO.getAvaliacaoOnlineMatriculaQuestaoVOs().add(obj);
		}
	}

	@Override
	public void realizarVerificacaoQuestaoUnicaEscolha(AvaliacaoOnlineMatriculaVO avaliacaoOnlineMatriculaVO, AvaliacaoOnlineMatriculaRespostaQuestaoVO avaliacaoOnlineMatriculaRespostaQuestaoVO, UsuarioVO usuarioVO) throws Exception {
		if (avaliacaoOnlineMatriculaRespostaQuestaoVO.getMarcada()) {
			for (AvaliacaoOnlineMatriculaQuestaoVO avaliacaoOnlineMatriculaQuestaoVO : avaliacaoOnlineMatriculaVO.getAvaliacaoOnlineMatriculaQuestaoVOs()) {
				if (avaliacaoOnlineMatriculaQuestaoVO.getQuestaoVO().getCodigo().intValue() == avaliacaoOnlineMatriculaRespostaQuestaoVO.getOpcaoRespostaQuestaoVO().getQuestaoVO().getCodigo().intValue()) {
					if (avaliacaoOnlineMatriculaQuestaoVO.getQuestaoVO().getTipoQuestaoEnum().equals(TipoQuestaoEnum.UNICA_ESCOLHA)) {
						for (AvaliacaoOnlineMatriculaRespostaQuestaoVO avaliacaoOnlineMatriculaRespostaQuestaoVO2 : avaliacaoOnlineMatriculaQuestaoVO.getAvaliacaoOnlineMatriculaRespostaQuestaoVOs()) {
							if (((!usuarioVO.getPermiteSimularNavegacaoAluno() || !Uteis.isAtributoPreenchido(avaliacaoOnlineMatriculaVO)) 
									&& avaliacaoOnlineMatriculaRespostaQuestaoVO2.getCodigo().intValue() != avaliacaoOnlineMatriculaRespostaQuestaoVO.getCodigo().intValue()) 
							|| ((usuarioVO.getPermiteSimularNavegacaoAluno() || !Uteis.isAtributoPreenchido(avaliacaoOnlineMatriculaVO)) && !avaliacaoOnlineMatriculaRespostaQuestaoVO2.getOpcaoRespostaQuestaoVO().getOrdemApresentacao().equals(avaliacaoOnlineMatriculaRespostaQuestaoVO.getOpcaoRespostaQuestaoVO().getOrdemApresentacao()))) {
								avaliacaoOnlineMatriculaRespostaQuestaoVO2.setMarcada(false);
							}
						}
					}
					realizarCalculoQuantidadePerguntasRespondidas(avaliacaoOnlineMatriculaVO);
					if (!usuarioVO.getPermiteSimularNavegacaoAluno() && Uteis.isAtributoPreenchido(avaliacaoOnlineMatriculaVO)) {
						getFacadeFactory().getAvaliacaoOnlineMatriculaQuestaoInterfaceFacade().persistirQuestoesAvaliacaoOnlineMatricula(avaliacaoOnlineMatriculaVO, usuarioVO);
					}
					return;
				}
			}
		}
	}

	@Override
	public void realizarCalculoQuantidadePerguntasRespondidas(AvaliacaoOnlineMatriculaVO avaliacaoOnlineMatriculaVO) {
		avaliacaoOnlineMatriculaVO.setQuantidadeQuestaoMarcadas(0);
		for (AvaliacaoOnlineMatriculaQuestaoVO questao : avaliacaoOnlineMatriculaVO.getAvaliacaoOnlineMatriculaQuestaoVOs()) {
			if (questao.getIsQuestaoEstaRepondida()) {
				avaliacaoOnlineMatriculaVO.setQuantidadeQuestaoMarcadas(avaliacaoOnlineMatriculaVO.getQuantidadeQuestaoMarcadas() + 1);
			}
		}
	}

	@Override
	public AvaliacaoOnlineMatriculaVO realizarVisualizacaoGabarito(AvaliacaoOnlineMatriculaVO avaliacaoOnlineMatriculaVO, UsuarioVO usuarioVO) throws Exception {
		avaliacaoOnlineMatriculaVO = consultarPorChavePrimaria(avaliacaoOnlineMatriculaVO.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
		avaliacaoOnlineMatriculaVO.setNota(null);
		for (AvaliacaoOnlineMatriculaQuestaoVO avaliacaoOnlineMatriculaQuestaoVO : avaliacaoOnlineMatriculaVO.getAvaliacaoOnlineMatriculaQuestaoVOs()) {
			avaliacaoOnlineMatriculaQuestaoVO.setAvaliacaoOnlineMatriculaVO(avaliacaoOnlineMatriculaVO);
			getFacadeFactory().getAvaliacaoOnlineMatriculaQuestaoInterfaceFacade().realizarCorrecaoQuestao(avaliacaoOnlineMatriculaQuestaoVO);
		}
		avaliacaoOnlineMatriculaVO.setNota(Uteis.arrendondarForcando2CadasDecimais(avaliacaoOnlineMatriculaVO.getNota()));
		realizarCalculoQuantidadePerguntasRespondidas(avaliacaoOnlineMatriculaVO);
		List<GraficoAproveitamentoAvaliacaoVO> graficoAproveitamentoAvaliacaoVOs = new ArrayList<GraficoAproveitamentoAvaliacaoVO>();
		graficoAproveitamentoAvaliacaoVOs = getFacadeFactory().getGraficoAproveitamentoAvaliacaoFacade().consultarAproveitamentoAvaliacaoOnlineAluno(avaliacaoOnlineMatriculaVO.getCodigo());
		avaliacaoOnlineMatriculaVO.setMatriculaPeriodoTurmaDisciplinaVO(getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarPorChavePrimaria(avaliacaoOnlineMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO));
		avaliacaoOnlineMatriculaVO.setGraficoAproveitamentoAvaliacaoVOs(getFacadeFactory().getGraficoAproveitamentoAvaliacaoFacade().realizarParametrosGraficoAvaliacaoOnlineMatricula(graficoAproveitamentoAvaliacaoVOs, avaliacaoOnlineMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina().getNome(), 190));
		return avaliacaoOnlineMatriculaVO;
	}
	
	@Override
	public Integer consultarUltimaAvalicaoOnlineRealizadaPorMatriculaPeriodoTurmaDisciplina(Integer codigoMatriculaPeriodoTurmaDisciplina) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select max(codigo) from avaliacaoonlinematricula");
		sqlStr.append(" where matriculaperiodoturmadisciplina = ").append(codigoMatriculaPeriodoTurmaDisciplina);

		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		Integer codigoAvaliacaoMatricula = 0;
		if (rs.next()) {
			codigoAvaliacaoMatricula = rs.getInt("max");
		}
		return codigoAvaliacaoMatricula;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public AvaliacaoOnlineMatriculaVO consultarUltimaAvalicaoOnlinePorAvaliacaoOnlinePorMatriculaPeriodoTurmaDisciplina(Integer avaliacaoOnline, Integer codigoMatriculaPeriodoTurmaDisciplina, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT * FROM avaliacaoonlinematricula WHERE codigo = (");
		sqlStr.append(" select max(codigo) from avaliacaoonlinematricula");
		sqlStr.append(" where matriculaperiodoturmadisciplina = ").append(codigoMatriculaPeriodoTurmaDisciplina).append(" ");
		sqlStr.append(" and  avaliacaoonline = ").append(avaliacaoOnline).append(" ");
		sqlStr.append(" ) ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (rs.next()) {
			return (montarDados(rs, nivelMontarDados, usuarioLogado));
		}
		return new AvaliacaoOnlineMatriculaVO();
	}
	
	@Override
	public List<AvaliacaoOnlineMatriculaVO> consultarAvaliacoesOnlineMatriculaRealizadas(Integer codigoMatriculaPeriodoTurmaDisciplina, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select * from avaliacaoonlinematricula");
		sqlStr.append(" where matriculaperiodoturmadisciplina = ").append(codigoMatriculaPeriodoTurmaDisciplina);
		sqlStr.append(" and situacaoavaliacaoonlinematricula in ('APROVADO', 'REPROVADO')");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());

		return montarDadosConsulta(rs, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
	}

	@Override
	public AvaliacaoOnlineMatriculaVO consultarPorMatriculaEQuestaoAvaliacaoOnline(String matricula, Integer questao, Integer avaliacaoOnline, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select avaliacaoonlinematricula.* from avaliacaoonlinematricula");
		sqlStr.append(" inner join avaliacaoonlinematriculaquestao on avaliacaoonlinematriculaquestao.avaliacaoonlinematricula = avaliacaoonlinematricula.codigo ");
		sqlStr.append(" where matricula = '").append(matricula).append("' and questao = ").append(questao);
		sqlStr.append(" and avaliacaoonlinematricula.avaliacaoOnline = ").append(avaliacaoOnline);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (rs.next()) {
			AvaliacaoOnlineMatriculaVO obj = new AvaliacaoOnlineMatriculaVO();
			obj = (montarDados(rs, nivelMontarDados, usuarioVO));
			obj.setAvaliacaoOnlineMatriculaQuestaoVOs(getFacadeFactory().getAvaliacaoOnlineMatriculaQuestaoInterfaceFacade().consultarPorAvaliacaoOnlineMatriculaEQuestao(obj.getCodigo(), questao, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO));
			return obj;
		}
		return new AvaliacaoOnlineMatriculaVO();
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarCorrecaoQuestaoAnuladaAvaliacaoOnline(AvaliacaoOnlineMatriculaVO obj, Boolean simularAnulacao, UsuarioVO usuarioVO) throws Exception {
		try {
			for (AvaliacaoOnlineMatriculaQuestaoVO avaliacaoOnlineMatriculaQuestaoVO : obj.getAvaliacaoOnlineMatriculaQuestaoVOs()) {
				if (avaliacaoOnlineMatriculaQuestaoVO.getSituacaoAtividadeRespostaEnum().equals(SituacaoAtividadeRespostaEnum.NAO_RESPONDIDA)) {
					if (!simularAnulacao) {
						getFacadeFactory().getAvaliacaoOnlineMatriculaQuestaoInterfaceFacade().alterarSituacaoPorCodigo(avaliacaoOnlineMatriculaQuestaoVO.getCodigo(), SituacaoAtividadeRespostaEnum.ANULADA, usuarioVO);
					}
					return;
				}
				avaliacaoOnlineMatriculaQuestaoVO.setAvaliacaoOnlineMatriculaVO(obj);
				if (avaliacaoOnlineMatriculaQuestaoVO.getSituacaoAtividadeRespostaEnum().equals(SituacaoAtividadeRespostaEnum.ERROU)) {
//					CASO O ALUNO TENHA ERRADO A QUESTÃO SERÁ NECESSÁRIO EU ADICIONAR A NOTA PORQUE A QUESTÃO FOI ANULADA E REMOVER UMA QUESTÃO ERRADA DO MESMO
					obj.setQuantidadeAcertos((obj.getQuantidadeAcertos() + 1));
					obj.setQuantidadeErros((obj.getQuantidadeErros() - 1));
					getFacadeFactory().getAvaliacaoOnlineMatriculaQuestaoInterfaceFacade().realizarCorrecaoNotaAvaliacaoOnlineQuestaoAnulada(avaliacaoOnlineMatriculaQuestaoVO, obj);
				}
				avaliacaoOnlineMatriculaQuestaoVO.setSituacaoAtividadeRespostaEnum(SituacaoAtividadeRespostaEnum.ANULADA);
				if (!simularAnulacao) {
					getFacadeFactory().getAvaliacaoOnlineMatriculaQuestaoInterfaceFacade().alterarSituacaoPorCodigo(avaliacaoOnlineMatriculaQuestaoVO.getCodigo(), SituacaoAtividadeRespostaEnum.ANULADA, usuarioVO);
				}
				
			}
			if (!obj.getAvaliacaoOnlineMatriculaQuestaoVOs().isEmpty()) {
				obj.setPercentualAcerto(Uteis.arrendondarForcando2CadasDecimais(((obj.getQuantidadeAcertos().doubleValue() / obj.getTamanhoListaAvaliacaoOnlineMatriculaQuestao()) * 100)));
				obj.setPercentualErro(Uteis.arrendondarForcando2CadasDecimais(((obj.getQuantidadeErros().doubleValue() / obj.getTamanhoListaAvaliacaoOnlineMatriculaQuestao()) * 100)));
			}
			
//			obj.setMatriculaPeriodoTurmaDisciplinaVO(getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarPorChavePrimaria(obj.getMatriculaPeriodoTurmaDisciplinaVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO));
//			if (obj.getAvaliacaoOnlineVO().getTipoUso().equals(TipoUsoEnum.REA)) {
//				if (obj.getNota() >= ((obj.getAvaliacaoOnlineVO().getNotaMaximaAvaliacao() * obj.getAvaliacaoOnlineVO().getPercentualAprovacao()) / 100)) {
//					obj.setSituacaoAvaliacaoOnlineMatriculaEnum(SituacaoAvaliacaoOnlineMatriculaEnum.APROVADO);
//				} else {
//					obj.setSituacaoAvaliacaoOnlineMatriculaEnum(SituacaoAvaliacaoOnlineMatriculaEnum.REPROVADO);
//				}
//			} else {
//				if (obj.getNota() >= ((obj.getAvaliacaoOnlineVO().getNotaMaximaAvaliacao() * obj.getAvaliacaoOnlineVO().getPercentualAprovacao()) / 100)) {
//					obj.setSituacaoAvaliacaoOnlineMatriculaEnum(SituacaoAvaliacaoOnlineMatriculaEnum.APROVADO);
//					if (obj.getConfiguracaoEADVO().getTipoControleLiberarAcessoProximaDisciplina().equals(TipoControleLiberarAcessoProximaDisciplinaEnum.APROVADO_AVALIACAO_ONLINE)) {
//						getFacadeFactory().getCalendarioAtividadeMatriculaInterfaceFacade().verificarRealizacaoProximaDisciplinaAprovadoAvaliacaoOnline(obj.getMatriculaPeriodoTurmaDisciplinaVO(), obj.getDataTermino(), obj.getConfiguracaoEADVO(), usuarioVO);
//					}
//				} else {
//					obj.setSituacaoAvaliacaoOnlineMatriculaEnum(SituacaoAvaliacaoOnlineMatriculaEnum.REPROVADO);
//					obj.setConfiguracaoEADVO(getFacadeFactory().getConfiguracaoEADFacade().consultarPorChavePrimaria(obj.getConfiguracaoEADVO().getCodigo()));
//					getFacadeFactory().getCalendarioAtividadeMatriculaInterfaceFacade().realizarCriacaoCalendarioAtividadeMatriculaAvaliacaoOnline(obj.getMatriculaPeriodoTurmaDisciplinaVO(), obj.getConfiguracaoEADVO(), calendarioAtividadeMatriculaVO, usuarioVO);
//				}
//			}
			if (!simularAnulacao) {
				String variavelNotaCfgUtilizar = Uteis.isAtributoPreenchido(obj.getAvaliacaoOnlineVO().getVariavelNotaCfgPadraoAvaliacaoOnlineRea()) ? obj.getAvaliacaoOnlineVO().getVariavelNotaCfgPadraoAvaliacaoOnlineRea() : obj.getConfiguracaoEADVO().getVariavelNotaCfgPadraoAvaliacaoOnline();
				obj.setNotaLancadaNoHistorico(getFacadeFactory().getHistoricoFacade().realizarLancamentoNotaHistoricoAutomaticamente(variavelNotaCfgUtilizar, obj.getNota(), obj.getMatriculaPeriodoTurmaDisciplinaVO(), obj.getConfiguracaoEADVO().getCalcularMediaFinalAposRealizacaoAvaliacaoOnline(), usuarioVO));
				alterarAvaliacaoOnlineMatriculaAposAnulacaoQuestao(obj, usuarioVO);
			}
			
		} catch (Exception e) {
			throw e;
		}

	}
	
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarAvaliacaoOnlineMatriculaAposAnulacaoQuestao(final AvaliacaoOnlineMatriculaVO obj,  UsuarioVO usuario) throws Exception {
		try {
			final String sql = "UPDATE AvaliacaoOnlineMatricula set quantidadeAcertos=?, quantidadeErros=?, percentualAcerto=?, percentualErro=?, nota=? " + "WHERE codigo = ? " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					int i = 0;
					sqlAlterar.setInt(++i, obj.getQuantidadeAcertos());
					sqlAlterar.setInt(++i, obj.getQuantidadeErros());
					sqlAlterar.setDouble(++i, obj.getPercentualAcerto());
					sqlAlterar.setDouble(++i, obj.getPercentualErro());
					sqlAlterar.setDouble(++i, obj.getNota());
					
					sqlAlterar.setInt(++i, obj.getCodigo());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	private void validarAvaliacaoIniciada(String matricula, int avaliacaoOnline) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select codigo from avaliacaoonlinematricula where matricula = '").append(matricula).append("'");
		sqlStr.append(" and avaliacaoonline = ").append(avaliacaoOnline).append(" and situacaoavaliacaoonlinematricula = '").append(SituacaoAvaliacaoOnlineMatriculaEnum.EM_REALIZACAO).append("'");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (rs.next()) {
			throw new Exception("A Avaliação Já Foi Iniciada em Outro Momento. Atualize a Página Para Continuar a Avaliação.");
		}
	}
}
