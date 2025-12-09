/**
 * 
 */
package negocio.facade.jdbc.secretaria;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.enumeradores.TipoCalculoGabaritoEnum;
import negocio.comuns.academico.enumeradores.TipoRespostaGabaritoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.processosel.GabaritoVO;
import negocio.comuns.processosel.ResultadoProcessamentoArquivoRespostaProvaPresencialVO;
import negocio.comuns.processosel.ResultadoProcessamentoProvaPresencialMotivoErroVO;
import negocio.comuns.processosel.enumeradores.TipoProcessamentoProvaPresencial;
import negocio.comuns.secretaria.MatriculaProvaPresencialDisciplinaVO;
import negocio.comuns.secretaria.MatriculaProvaPresencialHistoricoVO;
import negocio.comuns.secretaria.MatriculaProvaPresencialRespostaVO;
import negocio.comuns.secretaria.MatriculaProvaPresencialVO;
import negocio.comuns.secretaria.enumeradores.SituacaoMatriculaProvaPresencialDisciplinaEnum;
import negocio.comuns.secretaria.enumeradores.SituacaoMatriculaProvaPresencialEnum;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoHistorico;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.secretaria.MatriculaProvaPresencialInterfaceFacade;

/**
 * @author Carlos Eugênio
 *
 */
@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class MatriculaProvaPresencial extends ControleAcesso implements MatriculaProvaPresencialInterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public MatriculaProvaPresencial() {
		super();
		setIdEntidade("MatriculaProvaPresencial");
	}

	public void validarDados(MatriculaProvaPresencialVO obj) throws Exception {
		if (obj.getMatriculaVO().getMatricula().equals("")) {
			throw new Exception("O campo MATRÍCULA deve ser informado.");
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final MatriculaProvaPresencialVO obj, ProgressBarVO progressBarVO, UsuarioVO usuario) throws Exception {
		try {
			validarDados(obj);
			final String sql = "INSERT INTO MatriculaProvaPresencial( resultadoProcessamentoArquivoRespostaProvaPresencial, matricula, totalAcerto, totalErro, quantidadeDisciplinaLocalizada, quantidadeDisciplinaNaoLocalizada, situacaoMatriculaProvaPresencial, quantidadeDisciplina, quantidadeDisciplinaConfiguracaoAcademicoNaoLocalizada, selecionado ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					if (obj.getResultadoProcessamentoArquivoRespostaProvaPresencialVO().getCodigo().intValue() != 0) {
						sqlInserir.setInt(1, obj.getResultadoProcessamentoArquivoRespostaProvaPresencialVO().getCodigo().intValue());
					} else {
						sqlInserir.setNull(1, 0);
					}
					if (!obj.getMatriculaVO().getMatricula().equals("")) {
						sqlInserir.setString(2, obj.getMatriculaVO().getMatricula().toString());
					} else {
						sqlInserir.setNull(2, 0);
					}
					sqlInserir.setBigDecimal(3, obj.getTotalAcerto());
					sqlInserir.setBigDecimal(4, obj.getTotalErro());
					sqlInserir.setInt(5, obj.getQuantidadeDisciplinaLocalizada());
					sqlInserir.setInt(6, obj.getQuantidadeDisciplinaNaoLocalizada());
					sqlInserir.setString(7, obj.getSituacaoMatriculaProvaPresencialEnum().name());
					sqlInserir.setInt(8, obj.getQuantidadeDisciplina());
					sqlInserir.setInt(9, obj.getQuantidadeDisciplinaConfiguracaoAcademicoNaoLocalizada());
					sqlInserir.setBoolean(10, obj.getSelecionado());
					return sqlInserir;
				}
			}, new ResultSetExtractor() {

				public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
			obj.setNovoObj(Boolean.FALSE);
			getFacadeFactory().getMatriculaProvaPresencialDisciplinaFacade().incluirMatriculaProvaPresencialDisciplina(obj.getCodigo(), obj.getMatriculaProvaPresencialDisciplinaVOs(), usuario);
			getFacadeFactory().getMatriculaProvaPresencialRespostaFacade().incluirMatriculaProvaPresencialResposta(obj.getCodigo(), obj.getMatriculaProvaPresencialRespostaVOs(), usuario);
			if (obj.getResultadoProcessamentoArquivoRespostaProvaPresencialVO().getTipoProcessamentoProvaPresencialEnum().name().equals(TipoProcessamentoProvaPresencial.LEITURA_ARQUIVO_RESPOSTA_GABARITO.name())) {
				executarAtualizacaoNotaMediaFinalPorTipoCalculoGabarito(obj.getResultadoProcessamentoArquivoRespostaProvaPresencialVO().getGabaritoVO(), obj, usuario);
			} else {
				if (obj.getSelecionado()) {
//					getFacadeFactory().getMatriculaProvaPresencialHistoricoFacade().incluirMatriculaProvaPresencialHistorico(obj.getCodigo(), obj.getMatriculaProvaPresencialDisciplinaVOs(), obj.getResultadoProcessamentoArquivoRespostaProvaPresencialVO().getConfiguracaoAcademicoVO().getCodigo(), obj.getResultadoProcessamentoArquivoRespostaProvaPresencialVO().getVariavelNota(), obj.getResultadoProcessamentoArquivoRespostaProvaPresencialVO().getRealizarCalculoMediaLancamentoNota(), obj.getResultadoProcessamentoArquivoRespostaProvaPresencialVO().getAno(), obj.getResultadoProcessamentoArquivoRespostaProvaPresencialVO().getSemestre(), usuario);
					executarAtualizacaoHistoricoArquivoNotaLancada(obj, progressBarVO, usuario);
				}
			}
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final MatriculaProvaPresencialVO obj, ProgressBarVO progressBarVO, UsuarioVO usuario) throws Exception {
		try {
			validarDados(obj);
			final String sql = "UPDATE MatriculaProvaPresencial set resultadoProcessamentoArquivoRespostaProvaPresencial=? matricula=?, totalAcerto=?, totalErro=?, quantidadeDisciplinaLocalizada=?, quantidadeDisciplinaNaoLocalizada=?, situacaoMatriculaProvaPresencial=?, quantidadeDisciplina=?, quantidadeDisciplinaConfiguracaoAcademicoNaoLocalizada=?, selecionado=? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					if (obj.getResultadoProcessamentoArquivoRespostaProvaPresencialVO().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(1, obj.getResultadoProcessamentoArquivoRespostaProvaPresencialVO().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(1, 0);
					}
					if (!obj.getMatriculaVO().getMatricula().equals("")) {
						sqlAlterar.setString(2, obj.getMatriculaVO().getMatricula().toString());
					} else {
						sqlAlterar.setNull(2, 0);
					}
					sqlAlterar.setBigDecimal(3, obj.getTotalAcerto());
					sqlAlterar.setBigDecimal(4, obj.getTotalErro());
					sqlAlterar.setInt(5, obj.getQuantidadeDisciplinaLocalizada());
					sqlAlterar.setInt(6, obj.getQuantidadeDisciplinaNaoLocalizada());
					sqlAlterar.setString(7, obj.getSituacaoMatriculaProvaPresencialEnum().name());
					sqlAlterar.setInt(8, obj.getQuantidadeDisciplina());
					sqlAlterar.setInt(9, obj.getQuantidadeDisciplinaConfiguracaoAcademicoNaoLocalizada());
					sqlAlterar.setBoolean(10, obj.getSelecionado());
					sqlAlterar.setInt(11, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
			getFacadeFactory().getMatriculaProvaPresencialDisciplinaFacade().alterarMatriculaProvaPresencial(obj.getCodigo(), obj.getMatriculaProvaPresencialDisciplinaVOs(), usuario);
			getFacadeFactory().getMatriculaProvaPresencialRespostaFacade().alterarMatriculaProvaPresencialResposta(obj.getCodigo(), obj.getMatriculaProvaPresencialRespostaVOs(), usuario);
			if (obj.getResultadoProcessamentoArquivoRespostaProvaPresencialVO().getTipoProcessamentoProvaPresencialEnum().name().equals(TipoProcessamentoProvaPresencial.LEITURA_ARQUIVO_RESPOSTA_GABARITO.name())) {
				executarAtualizacaoNotaMediaFinalPorTipoCalculoGabarito(obj.getResultadoProcessamentoArquivoRespostaProvaPresencialVO().getGabaritoVO(), obj, usuario);
			} else {
				executarAtualizacaoHistoricoArquivoNotaLancada(obj, progressBarVO, usuario);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(MatriculaProvaPresencialVO obj, UsuarioVO usuario) throws Exception {
		String sql = "DELETE FROM MatriculaProvaPresencial WHERE (codigo = ?)" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
	}

	public static List montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
		List vetResultado = new ArrayList(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, usuario));
		}
		return vetResultado;
	}

	public static MatriculaProvaPresencialVO montarDados(SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
		MatriculaProvaPresencialVO obj = new MatriculaProvaPresencialVO();
		obj.getMatriculaVO().setMatricula(dadosSQL.getString("matricula"));
		obj.setTotalAcerto(dadosSQL.getBigDecimal("totalAcerto"));
		obj.setTotalErro(dadosSQL.getBigDecimal("totalErro"));
		obj.setQuantidadeDisciplinaLocalizada(dadosSQL.getInt("quantidadeDisciplinaLocalizada"));
		obj.setQuantidadeDisciplinaNaoLocalizada(dadosSQL.getInt("quantidadeDisciplinaNaoLocalizada"));
		obj.setQuantidadeDisciplina(dadosSQL.getInt("quantidadeDisciplina"));
		obj.setQuantidadeDisciplinaConfiguracaoAcademicoNaoLocalizada(dadosSQL.getInt("quantidadeDisciplinaConfiguracaoAcademicoNaoLocalizada"));
		if (dadosSQL.getString("situacaoMatriculaProvaPresencial") != null) {
			obj.setSituacaoMatriculaProvaPresencialEnum(SituacaoMatriculaProvaPresencialEnum.valueOf(dadosSQL.getString("situacaoMatriculaProvaPresencial")));
		}
		obj.setSelecionado(dadosSQL.getBoolean("selecionado"));
		obj.setNovoObj(Boolean.FALSE);
		return obj;
	}

	public static String getIdEntidade() {
		return MatriculaProvaPresencial.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		MatriculaProvaPresencial.idEntidade = idEntidade;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarMatriculaProvaPresencial(ResultadoProcessamentoArquivoRespostaProvaPresencialVO resultadoProcesamentoArquivoVO, List<MatriculaProvaPresencialVO> objetos, UsuarioVO usuario) throws Exception {
		excluirMatriculaProvaPresencialPorResultado(resultadoProcesamentoArquivoVO.getCodigo(), usuario);
		incluirMatriculaProvaPresencial(resultadoProcesamentoArquivoVO, new ProgressBarVO(), objetos, usuario);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirMatriculaProvaPresencialPorResultado(Integer resultadoProcessamentoArquivo, UsuarioVO usuario) throws Exception {
		String sql = "DELETE FROM MatriculaProvaPresencial WHERE (resultadoProcessamentoArquivoRespostaProvaPresencial = ?)" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { resultadoProcessamentoArquivo });
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirMatriculaProvaPresencial(ResultadoProcessamentoArquivoRespostaProvaPresencialVO resultadoProcesamentoArquivoVO, ProgressBarVO progressBarVO, List<MatriculaProvaPresencialVO> objetos, UsuarioVO usuario) throws Exception {		
		Iterator e = objetos.iterator();
		while (e.hasNext()) {
//			progressBarVO.setStatus("Processando "+progressBarVO.getProgresso()+" de "+progressBarVO.getMaxValue());
			MatriculaProvaPresencialVO obj = (MatriculaProvaPresencialVO) e.next();
			obj.setResultadoProcessamentoArquivoRespostaProvaPresencialVO(resultadoProcesamentoArquivoVO);
//			progressBarVO.incrementar();
			incluir(obj, progressBarVO, usuario);
		}
	}

	public List<MatriculaProvaPresencialVO> consultarPorResultadoProcessamentoArquivo(Integer resultadoProcessamentoArquivo, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select matriculaprovapresencial.codigo, matriculaprovapresencial.resultadoprocessamentoarquivorespostaprovapresencial, ");
		sb.append(" matriculaprovapresencial.totalAcerto, matriculaprovapresencial.totalErro, matriculaprovapresencial.quantidadeDisciplinaLocalizada, ");
		sb.append(" matriculaprovapresencial.quantidadeDisciplinaNaoLocalizada, matriculaprovapresencial.situacaoMatriculaProvaPresencial, matriculaprovapresencial.quantidadeDisciplina, ");
		sb.append(" matriculaprovapresencial.quantidadeDisciplinaConfiguracaoAcademicoNaoLocalizada, matriculaprovapresencial.selecionado,  matriculaprovapresencial.matricula, matricula.situacao AS \"matricula.situacao\", matricula.gradecurricularatual, ");
		sb.append(" pessoa.codigo AS \"pessoa.codigo\", pessoa.nome AS \"pessoa.nome\" ");
		sb.append(" from matriculaprovapresencial ");
		sb.append(" left join matricula on matricula.matricula = matriculaprovapresencial.matricula ");
		sb.append(" left join pessoa on pessoa.codigo = matricula.aluno ");
		sb.append(" where matriculaprovapresencial.resultadoprocessamentoarquivorespostaprovapresencial = ").append(resultadoProcessamentoArquivo);
		sb.append(" order by matriculaprovapresencial.codigo ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<MatriculaProvaPresencialVO> listaMatriculaProvaPresencialVOs = new ArrayList<MatriculaProvaPresencialVO>(0);
		while (tabelaResultado.next()) {
			MatriculaProvaPresencialVO obj = new MatriculaProvaPresencialVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.getResultadoProcessamentoArquivoRespostaProvaPresencialVO().setCodigo(tabelaResultado.getInt("resultadoprocessamentoarquivorespostaprovapresencial"));
			obj.setTotalAcerto(tabelaResultado.getBigDecimal("totalAcerto"));
			obj.setTotalErro(tabelaResultado.getBigDecimal("totalErro"));
			obj.setQuantidadeDisciplinaLocalizada(tabelaResultado.getInt("quantidadeDisciplinaLocalizada"));
			obj.setQuantidadeDisciplinaNaoLocalizada(tabelaResultado.getInt("quantidadeDisciplinaNaoLocalizada"));
			obj.setQuantidadeDisciplina(tabelaResultado.getInt("quantidadeDisciplina"));
			obj.setQuantidadeDisciplinaConfiguracaoAcademicoNaoLocalizada(tabelaResultado.getInt("quantidadeDisciplinaConfiguracaoAcademicoNaoLocalizada"));
			obj.setSelecionado(tabelaResultado.getBoolean("selecionado"));
			if (tabelaResultado.getString("situacaoMatriculaProvaPresencial") != null) {
				obj.setSituacaoMatriculaProvaPresencialEnum(SituacaoMatriculaProvaPresencialEnum.valueOf(tabelaResultado.getString("situacaoMatriculaProvaPresencial")));
			}
			obj.getMatriculaVO().setMatricula(tabelaResultado.getString("matricula"));
			obj.getMatriculaVO().setSituacao(tabelaResultado.getString("matricula.situacao"));
			obj.getMatriculaVO().getGradeCurricularAtual().setCodigo(tabelaResultado.getInt("gradeCurricularAtual"));
			obj.getMatriculaVO().getAluno().setCodigo(tabelaResultado.getInt("pessoa.codigo"));
			obj.getMatriculaVO().getAluno().setNome(tabelaResultado.getString("pessoa.nome"));

			obj.setMatriculaProvaPresencialDisciplinaVOs(getFacadeFactory().getMatriculaProvaPresencialDisciplinaFacade().consultarPorMatriculaProvaPresencial(obj.getCodigo(), usuarioVO));
			obj.setMatriculaProvaPresencialRespostaVOs(getFacadeFactory().getMatriculaProvaPresencialRespostaFacade().consultarPorMatriculaProvaPresencial(obj.getCodigo(), usuarioVO));
			listaMatriculaProvaPresencialVOs.add(obj);

		}
		return listaMatriculaProvaPresencialVOs;
	}

	/**
	 * Método responsável por atualizar a nota e a média final de acordo com as
	 * regras pré-estabelecidas no gabarito.
	 * 
	 * @author Carlos Eugênio - 24/06/2015
	 * @param gabaritoVO
	 * @param matriculaProvaPresencialVO
	 * @param usuarioVO
	 * @throws Exception
	 */
	public void executarAtualizacaoNotaMediaFinalPorTipoCalculoGabarito(GabaritoVO gabaritoVO, MatriculaProvaPresencialVO matriculaProvaPresencialVO, UsuarioVO usuarioVO) throws Exception {
		if (!matriculaProvaPresencialVO.getSituacaoMatriculaProvaPresencialEnum().name().equals(SituacaoMatriculaProvaPresencialEnum.MATRICULA_ENCONTRADA.name())) {
			return;
		}
		String tituloNota = getFacadeFactory().getConfiguracaoAcademicoFacade().consultarNotaUtilizarPorConfiguracaoAcademicoTituloNota(gabaritoVO.getConfiguracaoAcademicoVO().getCodigo(), gabaritoVO.getVariavelNota(), usuarioVO);

		if (gabaritoVO.getTipoRespostaGabaritoEnum().name().equals(TipoRespostaGabaritoEnum.DISCIPLINA.name())) {

			for (MatriculaProvaPresencialDisciplinaVO matriculaProvaPresencialDisciplinaVO : matriculaProvaPresencialVO.getMatriculaProvaPresencialDisciplinaVOs()) {

				if (matriculaProvaPresencialDisciplinaVO.getSituacaoMatriculaProvaPresencialDisciplinaEnum().name().equals(SituacaoMatriculaProvaPresencialDisciplinaEnum.DISCIPLINA_LOCALIZADA.name())) {

					HistoricoVO historicoVO = getFacadeFactory().getHistoricoFacade().consultaCompletaPorMatriculaDisciplinaSituacaoHistoricoAnoSemestre(matriculaProvaPresencialVO.getMatriculaVO().getMatricula(), matriculaProvaPresencialDisciplinaVO.getDisciplinaVO().getCodigo(), SituacaoHistorico.APROVADO_APROVEITAMENTO.getValor(), gabaritoVO.getAno(), gabaritoVO.getSemestre(), false, false, usuarioVO);
					if (gabaritoVO.getTipoCalculoGabaritoEnum().name().equals(TipoCalculoGabaritoEnum.GERAL.name())) {
						adicionarNotaHistoricoDeAcordoComTituloNota(historicoVO, tituloNota, matriculaProvaPresencialVO.getTotalAcerto().doubleValue());
					} else {
						adicionarNotaHistoricoDeAcordoComTituloNota(historicoVO, tituloNota, matriculaProvaPresencialDisciplinaVO.getNota().doubleValue());
					}
					if (gabaritoVO.getRealizarCalculoMediaLancamentoNota()) {
						if(historicoVO.getHistoricoDisciplinaComposta() && !Uteis.isAtributoPreenchido(historicoVO.getHistoricoDisciplinaFilhaComposicaoVOs())) {
							historicoVO.setHistoricoDisciplinaFilhaComposicaoVOs(getFacadeFactory().getHistoricoFacade().consultaRapidaHistoricoFazParteComposicaoPorMatriculaPorGradeCurricularPorMatriculaPeriodo(historicoVO.getMatricula().getMatricula(), 0, historicoVO.getMatriculaPeriodo().getCodigo(), historicoVO.getMatrizCurricular().getCodigo(), historicoVO.getGradeDisciplinaVO().getCodigo(), historicoVO.getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo(), false, null));
							Map<Integer, ConfiguracaoAcademicoVO> mapConf =  new HashMap<Integer, ConfiguracaoAcademicoVO>(0);
							if(!mapConf.containsKey(historicoVO.getConfiguracaoAcademico().getCodigo())){
								mapConf.put(historicoVO.getConfiguracaoAcademico().getCodigo(), historicoVO.getConfiguracaoAcademico());
							}
							mapConf.put(historicoVO.getConfiguracaoAcademico().getCodigo(), historicoVO.getConfiguracaoAcademico());
							for (HistoricoVO obj : historicoVO.getHistoricoDisciplinaFilhaComposicaoVOs()) {
								if(!obj.getConfiguracaoAcademico().getNivelMontarDados().equals(NivelMontarDados.TODOS)) {
									if (!mapConf.containsKey(obj.getConfiguracaoAcademico().getCodigo())) {			
										mapConf.put(obj.getConfiguracaoAcademico().getCodigo(), getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorChavePrimaria(obj.getConfiguracaoAcademico().getCodigo(), usuarioVO));
									}
									obj.setConfiguracaoAcademico(mapConf.get(obj.getConfiguracaoAcademico().getCodigo()));
								}
							}	
						}
						getFacadeFactory().getHistoricoFacade().calcularMedia(historicoVO, historicoVO.getHistoricoDisciplinaFilhaComposicaoVOs(), gabaritoVO.getConfiguracaoAcademicoVO(), null, matriculaProvaPresencialVO.getResultadoProcessamentoArquivoRespostaProvaPresencialVO().getTipoAlteracaoSituacaoHistorico(), true, usuarioVO);
					}
					getFacadeFactory().getHistoricoFacade().alterar(historicoVO, usuarioVO);
				}
			}

		} else {
			if (gabaritoVO.getTipoCalculoGabaritoEnum().name().equals(TipoCalculoGabaritoEnum.GERAL.name())) {

				for (MatriculaProvaPresencialDisciplinaVO matriculaProvaPresencialDisciplinaVO : matriculaProvaPresencialVO.getMatriculaProvaPresencialDisciplinaVOs()) {

					if (matriculaProvaPresencialDisciplinaVO.getSituacaoMatriculaProvaPresencialDisciplinaEnum().name().equals(SituacaoMatriculaProvaPresencialDisciplinaEnum.DISCIPLINA_LOCALIZADA.name())) {
						HistoricoVO historicoVO = getFacadeFactory().getHistoricoFacade().consultaCompletaPorMatriculaDisciplinaSituacaoHistoricoAnoSemestre(matriculaProvaPresencialVO.getMatriculaVO().getMatricula(), matriculaProvaPresencialDisciplinaVO.getDisciplinaVO().getCodigo(), SituacaoHistorico.APROVADO_APROVEITAMENTO.getValor(), gabaritoVO.getAno(), gabaritoVO.getSemestre(), false, false, usuarioVO);

						adicionarNotaHistoricoDeAcordoComTituloNota(historicoVO, tituloNota, matriculaProvaPresencialVO.getTotalAcerto().doubleValue());
						if (gabaritoVO.getRealizarCalculoMediaLancamentoNota()) {
							if(historicoVO.getHistoricoDisciplinaComposta() && !Uteis.isAtributoPreenchido(historicoVO.getHistoricoDisciplinaFilhaComposicaoVOs())) {
								historicoVO.setHistoricoDisciplinaFilhaComposicaoVOs(getFacadeFactory().getHistoricoFacade().consultaRapidaHistoricoFazParteComposicaoPorMatriculaPorGradeCurricularPorMatriculaPeriodo(historicoVO.getMatricula().getMatricula(), 0, historicoVO.getMatriculaPeriodo().getCodigo(), historicoVO.getMatrizCurricular().getCodigo(), historicoVO.getGradeDisciplinaVO().getCodigo(), historicoVO.getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo(), false, null));
							}
							getFacadeFactory().getHistoricoFacade().calcularMedia(historicoVO, historicoVO.getHistoricoDisciplinaFilhaComposicaoVOs(), gabaritoVO.getConfiguracaoAcademicoVO(), null, matriculaProvaPresencialVO.getResultadoProcessamentoArquivoRespostaProvaPresencialVO().getTipoAlteracaoSituacaoHistorico(), true, usuarioVO);
						}
						getFacadeFactory().getHistoricoFacade().alterar(historicoVO, usuarioVO);
					}
				}
			} else {
				Map<Integer, BigDecimal> mapAreaConhecimentoVOs = inicializarDadosTotalNotaPorAreaConhecimento(matriculaProvaPresencialVO, usuarioVO);

				for (MatriculaProvaPresencialDisciplinaVO matriculaProvaPresencialDisciplinaVO : matriculaProvaPresencialVO.getMatriculaProvaPresencialDisciplinaVOs()) {

					if (matriculaProvaPresencialDisciplinaVO.getSituacaoMatriculaProvaPresencialDisciplinaEnum().name().equals(SituacaoMatriculaProvaPresencialDisciplinaEnum.DISCIPLINA_LOCALIZADA.name())) {

						if (mapAreaConhecimentoVOs.containsKey(matriculaProvaPresencialDisciplinaVO.getAreaConhecimentoVO().getCodigo())) {

							HistoricoVO historicoVO = getFacadeFactory().getHistoricoFacade().consultaCompletaPorMatriculaDisciplinaSituacaoHistoricoAnoSemestre(matriculaProvaPresencialVO.getMatriculaVO().getMatricula(), matriculaProvaPresencialDisciplinaVO.getDisciplinaVO().getCodigo(), SituacaoHistorico.APROVADO_APROVEITAMENTO.getValor(), gabaritoVO.getAno(), gabaritoVO.getSemestre(), false, false, usuarioVO);
							adicionarNotaHistoricoDeAcordoComTituloNota(historicoVO, tituloNota, mapAreaConhecimentoVOs.get(matriculaProvaPresencialDisciplinaVO.getAreaConhecimentoVO().getCodigo()).doubleValue());

							if (gabaritoVO.getRealizarCalculoMediaLancamentoNota()) {
								if(historicoVO.getHistoricoDisciplinaComposta() && !Uteis.isAtributoPreenchido(historicoVO.getHistoricoDisciplinaFilhaComposicaoVOs())) {
									historicoVO.setHistoricoDisciplinaFilhaComposicaoVOs(getFacadeFactory().getHistoricoFacade().consultaRapidaHistoricoFazParteComposicaoPorMatriculaPorGradeCurricularPorMatriculaPeriodo(historicoVO.getMatricula().getMatricula(), 0, historicoVO.getMatriculaPeriodo().getCodigo(), historicoVO.getMatrizCurricular().getCodigo(), historicoVO.getGradeDisciplinaVO().getCodigo(), historicoVO.getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo(), false, null));
								}
								getFacadeFactory().getHistoricoFacade().calcularMedia(historicoVO, historicoVO.getHistoricoDisciplinaFilhaComposicaoVOs(), gabaritoVO.getConfiguracaoAcademicoVO(), null, matriculaProvaPresencialVO.getResultadoProcessamentoArquivoRespostaProvaPresencialVO().getTipoAlteracaoSituacaoHistorico(), true, usuarioVO);
							}
							getFacadeFactory().getHistoricoFacade().alterar(historicoVO, usuarioVO);
						}

					}
				}
			}
		}
	}

	public Map<Integer, BigDecimal> inicializarDadosTotalNotaPorAreaConhecimento(MatriculaProvaPresencialVO matriculaProvaPresencialVO, UsuarioVO usuarioVO) {
		Map<Integer, BigDecimal> mapAreaConhecimentoVOs = new HashMap<Integer, BigDecimal>(0);
		for (MatriculaProvaPresencialRespostaVO matriculaProvaPresencialRespostaVO : matriculaProvaPresencialVO.getMatriculaProvaPresencialRespostaVOs()) {

			if (!mapAreaConhecimentoVOs.containsKey(matriculaProvaPresencialRespostaVO.getAreaConhecimentoVO().getCodigo())) {
				mapAreaConhecimentoVOs.put(matriculaProvaPresencialRespostaVO.getAreaConhecimentoVO().getCodigo(), matriculaProvaPresencialRespostaVO.getTotalAcerto());
			} else {
				mapAreaConhecimentoVOs.put(matriculaProvaPresencialRespostaVO.getAreaConhecimentoVO().getCodigo(), mapAreaConhecimentoVOs.get(matriculaProvaPresencialRespostaVO.getAreaConhecimentoVO().getCodigo()).add(matriculaProvaPresencialRespostaVO.getTotalAcerto()));
			}

		}
		return mapAreaConhecimentoVOs;
	}

	public void adicionarNotaHistoricoDeAcordoComTituloNota(HistoricoVO historicoVO, String tituloNota, Double nota) {
		if (tituloNota.equals("nota1")) {
			historicoVO.setNota1(nota);
		}
		if (tituloNota.equals("nota2")) {
			historicoVO.setNota2(nota);
		}
		if (tituloNota.equals("nota3")) {
			historicoVO.setNota3(nota);
		}
		if (tituloNota.equals("nota4")) {
			historicoVO.setNota4(nota);
		}
		if (tituloNota.equals("nota5")) {
			historicoVO.setNota5(nota);
		}
		if (tituloNota.equals("nota6")) {
			historicoVO.setNota6(nota);
		}
		if (tituloNota.equals("nota7")) {
			historicoVO.setNota7(nota);
		}
		if (tituloNota.equals("nota8")) {
			historicoVO.setNota8(nota);
		}
		if (tituloNota.equals("nota9")) {
			historicoVO.setNota9(nota);
		}
		if (tituloNota.equals("nota10")) {
			historicoVO.setNota10(nota);
		}
		if (tituloNota.equals("nota11")) {
			historicoVO.setNota11(nota);
		}
		if (tituloNota.equals("nota12")) {
			historicoVO.setNota12(nota);
		}
		if (tituloNota.equals("nota13")) {
			historicoVO.setNota13(nota);
		}
		if (tituloNota.equals("nota14")) {
			historicoVO.setNota14(nota);
		}
		if (tituloNota.equals("nota15")) {
			historicoVO.setNota15(nota);
		}
		if (tituloNota.equals("nota16")) {
			historicoVO.setNota16(nota);
		}
		if (tituloNota.equals("nota17")) {
			historicoVO.setNota17(nota);
		}
		if (tituloNota.equals("nota18")) {
			historicoVO.setNota18(nota);
		}
		if (tituloNota.equals("nota19")) {
			historicoVO.setNota19(nota);
		}
		if (tituloNota.equals("nota20")) {
			historicoVO.setNota20(nota);
		}
		if (tituloNota.equals("nota21")) {
			historicoVO.setNota21(nota);
		}
		if (tituloNota.equals("nota22")) {
			historicoVO.setNota22(nota);
		}
		if (tituloNota.equals("nota23")) {
			historicoVO.setNota23(nota);
		}
		if (tituloNota.equals("nota24")) {
			historicoVO.setNota24(nota);
		}
		if (tituloNota.equals("nota25")) {
			historicoVO.setNota25(nota);
		}
		if (tituloNota.equals("nota26")) {
			historicoVO.setNota26(nota);
		}
		if (tituloNota.equals("nota27")) {
			historicoVO.setNota27(nota);
		}
		if (tituloNota.equals("nota28")) {
			historicoVO.setNota28(nota);
		}
		if (tituloNota.equals("nota29")) {
			historicoVO.setNota29(nota);
		}
		if (tituloNota.equals("nota30")) {
			historicoVO.setNota30(nota);
		}
		
		if (tituloNota.equals("nota31")) {
			historicoVO.setNota31(nota);
		}
		if (tituloNota.equals("nota32")) {
			historicoVO.setNota32(nota);
		}
		if (tituloNota.equals("nota33")) {
			historicoVO.setNota33(nota);
		}
		if (tituloNota.equals("nota34")) {
			historicoVO.setNota34(nota);
		}
		if (tituloNota.equals("nota35")) {
			historicoVO.setNota35(nota);
		}
		if (tituloNota.equals("nota36")) {
			historicoVO.setNota36(nota);
		}
		if (tituloNota.equals("nota37")) {
			historicoVO.setNota37(nota);
		}
		if (tituloNota.equals("nota38")) {
			historicoVO.setNota38(nota);
		}
		if (tituloNota.equals("nota39")) {
			historicoVO.setNota39(nota);
		}
		if (tituloNota.equals("nota40")) {
			historicoVO.setNota40(nota);
		}		
		
	}

	/**
	 * Método responsável por atualizar o histórico de acordo com a nota que
	 * está no arquivo texto. Somente é lançada nota para as disciplinas que
	 * foram localizadas.
	 * 
	 * @author Carlos Eugênio - 24/06/2015
	 * @param matriculaProvaPresencialVO
	 * @param usuarioVO
	 * @throws Exception
	 */
	public void executarAtualizacaoHistoricoArquivoNotaLancada(final MatriculaProvaPresencialVO matriculaProvaPresencialVO, final ProgressBarVO progressBarVO, final UsuarioVO usuarioVO) throws Exception {
		  Integer configuracaoAcademico = matriculaProvaPresencialVO.getResultadoProcessamentoArquivoRespostaProvaPresencialVO().getConfiguracaoAcademicoVO().getCodigo();
		  String variavelNota = matriculaProvaPresencialVO.getResultadoProcessamentoArquivoRespostaProvaPresencialVO().getVariavelNota();
		  final Boolean realizarCalculoMediaLancamentoNota = matriculaProvaPresencialVO.getResultadoProcessamentoArquivoRespostaProvaPresencialVO().getRealizarCalculoMediaLancamentoNota();
		  final Integer quantidadeMatriculaProcessar = matriculaProvaPresencialVO.getResultadoProcessamentoArquivoRespostaProvaPresencialVO().getMatriculaProvaPresencialVOs().size();	  
		  progressBarVO.incrementar();
		  final String tituloNota = getFacadeFactory().getConfiguracaoAcademicoFacade().consultarNotaUtilizarPorConfiguracaoAcademicoTituloNota(configuracaoAcademico, variavelNota, usuarioVO);
		  
		   Integer quantidadeDisciplina = 0;
		   
		   for (MatriculaProvaPresencialDisciplinaVO matriculaProvaPresencialDisciplinaVO : matriculaProvaPresencialVO.getMatriculaProvaPresencialDisciplinaVOs()) {
		    
		    try {
		     quantidadeDisciplina = quantidadeDisciplina + 1;
		     progressBarVO.setStatus("Matrícula " + progressBarVO.getProgresso() + " de " + (progressBarVO.getMaxValue() - 1) + "  -  (Matrícula Atual = " + matriculaProvaPresencialVO.getMatriculaVO().getMatricula() + ") - Disciplina " + quantidadeDisciplina + " de " + matriculaProvaPresencialVO.getQuantidadeDisciplinaLocalizada() + " -  (Disciplina Atual = " + matriculaProvaPresencialDisciplinaVO.getDisciplinaVO().getNome() + ") ");
		     
		     if (matriculaProvaPresencialDisciplinaVO.getSituacaoMatriculaProvaPresencialDisciplinaEnum().name().equals(SituacaoMatriculaProvaPresencialDisciplinaEnum.DISCIPLINA_LOCALIZADA.name())) {
		      HistoricoVO historicoVO = getFacadeFactory().getHistoricoFacade().consultaRapidaPorChavePrimariaDadosCompletosSemExcessao(matriculaProvaPresencialDisciplinaVO.getHistoricoVO().getCodigo(), usuarioVO);
		      
		      if (historicoVO != null && !historicoVO.getCodigo().equals(0)) {
		       incluirMatriculaProvaPresencialHistorico(matriculaProvaPresencialVO, matriculaProvaPresencialDisciplinaVO, historicoVO, usuarioVO);
		       
		       if (matriculaProvaPresencialDisciplinaVO.getNota() != null) {
		        adicionarNotaHistoricoDeAcordoComTituloNota(historicoVO, tituloNota, matriculaProvaPresencialDisciplinaVO.getNota().doubleValue());
		       } else {
		        adicionarNotaHistoricoDeAcordoComTituloNota(historicoVO, tituloNota, null);
		       }
		       if (realizarCalculoMediaLancamentoNota) {
		    	   if(historicoVO.getHistoricoDisciplinaComposta() && !Uteis.isAtributoPreenchido(historicoVO.getHistoricoDisciplinaFilhaComposicaoVOs())) {
						historicoVO.setHistoricoDisciplinaFilhaComposicaoVOs(getFacadeFactory().getHistoricoFacade().consultaRapidaHistoricoFazParteComposicaoPorMatriculaPorGradeCurricularPorMatriculaPeriodo(historicoVO.getMatricula().getMatricula(), 0, historicoVO.getMatriculaPeriodo().getCodigo(), historicoVO.getMatrizCurricular().getCodigo(), historicoVO.getGradeDisciplinaVO().getCodigo(), historicoVO.getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo(), false, null));
						Map<Integer, ConfiguracaoAcademicoVO> mapConf =  new HashMap<Integer, ConfiguracaoAcademicoVO>(0);
						if(!mapConf.containsKey(historicoVO.getConfiguracaoAcademico().getCodigo())){
							mapConf.put(historicoVO.getConfiguracaoAcademico().getCodigo(), historicoVO.getConfiguracaoAcademico());
						}
						mapConf.put(historicoVO.getConfiguracaoAcademico().getCodigo(), historicoVO.getConfiguracaoAcademico());
						for (HistoricoVO obj : historicoVO.getHistoricoDisciplinaFilhaComposicaoVOs()) {
							if(!obj.getConfiguracaoAcademico().getNivelMontarDados().equals(NivelMontarDados.TODOS)) {
								if (!mapConf.containsKey(obj.getConfiguracaoAcademico().getCodigo())) {			
									mapConf.put(obj.getConfiguracaoAcademico().getCodigo(), getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorChavePrimaria(obj.getConfiguracaoAcademico().getCodigo(), usuarioVO));
								}
								obj.setConfiguracaoAcademico(mapConf.get(obj.getConfiguracaoAcademico().getCodigo()));
							}
						}	
					}
		        getFacadeFactory().getHistoricoFacade().calcularMedia(historicoVO, historicoVO.getHistoricoDisciplinaFilhaComposicaoVOs(), matriculaProvaPresencialVO.getResultadoProcessamentoArquivoRespostaProvaPresencialVO().getConfiguracaoAcademicoVO(), null, matriculaProvaPresencialVO.getResultadoProcessamentoArquivoRespostaProvaPresencialVO().getTipoAlteracaoSituacaoHistorico(), true, usuarioVO);
		       }
		       if (historicoVO.getHistoricoDisciplinaFazParteComposicao()) {
		    	   getFacadeFactory().getHistoricoFacade().executarAtualizacaoDisciplinaComposta(historicoVO, null, matriculaProvaPresencialVO.getResultadoProcessamentoArquivoRespostaProvaPresencialVO().getTipoAlteracaoSituacaoHistorico(), true, false, usuarioVO);
		       }else if(historicoVO.getHistoricoDisciplinaComposta()) { 
					getFacadeFactory().getHistoricoFacade().executarAtualizacaoDisciplinaFilhaComposicaoComBaseDisciplinaCompostaComposta(historicoVO,  matriculaProvaPresencialVO.getResultadoProcessamentoArquivoRespostaProvaPresencialVO().getTipoAlteracaoSituacaoHistorico(), false, usuarioVO);
				}
		       getFacadeFactory().getHistoricoFacade().alterar(historicoVO, usuarioVO);
		      }
		     }
		     
		    } catch (Exception e) {
		     e.printStackTrace();
		     matriculaProvaPresencialVO.getResultadoProcessamentoArquivoRespostaProvaPresencialVO().getListaResultadoProcessamentoProvaPresencialMotivoErroVOs().add(inicializarDadosMensageErro(matriculaProvaPresencialVO, matriculaProvaPresencialDisciplinaVO, e.getMessage(), usuarioVO));
		    }	   
		  }
		 }
	
	public void incluirMatriculaProvaPresencialHistorico(MatriculaProvaPresencialVO matriculaProvaPresencialVO, MatriculaProvaPresencialDisciplinaVO matriculaProvaPresencialDisciplinaVO, HistoricoVO historicoVO, UsuarioVO usuarioVO) throws Exception {
		MatriculaProvaPresencialHistoricoVO matriculaProvaPresencialHistoricoVO = getFacadeFactory().getMatriculaProvaPresencialHistoricoFacade().inicializarDadosMatriculaProvaPresencialHistoricoPorHistorico(matriculaProvaPresencialDisciplinaVO, historicoVO, matriculaProvaPresencialVO.getResultadoProcessamentoArquivoRespostaProvaPresencialVO().getConfiguracaoAcademicoVO().getCodigo(), matriculaProvaPresencialVO.getResultadoProcessamentoArquivoRespostaProvaPresencialVO().getVariavelNota(), matriculaProvaPresencialVO.getResultadoProcessamentoArquivoRespostaProvaPresencialVO().getRealizarCalculoMediaLancamentoNota(), matriculaProvaPresencialVO.getResultadoProcessamentoArquivoRespostaProvaPresencialVO().getAno(), matriculaProvaPresencialVO.getResultadoProcessamentoArquivoRespostaProvaPresencialVO().getSemestre(), usuarioVO);
		getFacadeFactory().getMatriculaProvaPresencialHistoricoFacade().incluir(matriculaProvaPresencialHistoricoVO, usuarioVO);
	}
	public ResultadoProcessamentoProvaPresencialMotivoErroVO inicializarDadosMensageErro(MatriculaProvaPresencialVO matriculaProvaPresencialVO, MatriculaProvaPresencialDisciplinaVO matriculaProvaPresencialDisciplinaVO, String mensagemErro, UsuarioVO usuarioVO) {
		ResultadoProcessamentoProvaPresencialMotivoErroVO obj = new ResultadoProcessamentoProvaPresencialMotivoErroVO();
		obj.setMatriculaVO(matriculaProvaPresencialVO.getMatriculaVO());
		obj.setDisciplinaVO(matriculaProvaPresencialDisciplinaVO.getDisciplinaVO());
		obj.setMensagemErro(mensagemErro);
		return obj;
	}
	
	public void preencherTodosListaAluno(List<MatriculaProvaPresencialVO> matriculaProvaPresencialVOs,String situacao) {
		for (MatriculaProvaPresencialVO matriculaProvaPresencialVO : matriculaProvaPresencialVOs) {
			if(matriculaProvaPresencialVO.getSituacaoMatriculaProvaPresencialEnum().name().equals(situacao)) {
				matriculaProvaPresencialVO.setSelecionado(Boolean.TRUE);
			}
		}
	}
	
	public void desmarcarTodosListaAluno(List<MatriculaProvaPresencialVO> matriculaProvaPresencialVOs, String situacao) {
		for (MatriculaProvaPresencialVO matriculaProvaPresencialVO : matriculaProvaPresencialVOs) {
			if(matriculaProvaPresencialVO.getSituacaoMatriculaProvaPresencialEnum().name().equals(situacao)) {
				matriculaProvaPresencialVO.setSelecionado(Boolean.FALSE);
			}
		}
	}

}
