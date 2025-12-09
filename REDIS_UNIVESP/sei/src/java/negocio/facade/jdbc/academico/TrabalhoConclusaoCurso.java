package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.richfaces.event.FileUploadEvent;
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

import negocio.comuns.academico.ConfiguracaoAcademicoNotaConceitoVO;
import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.ConfiguracaoTCCArtefatoVO;
import negocio.comuns.academico.CursoCoordenadorVO;
import negocio.comuns.academico.GradeDisciplinaVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaTCCVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.QuestaoTrabalhoConclusaoCursoVO;
import negocio.comuns.academico.TrabalhoConclusaoCursoArquivoVO;
import negocio.comuns.academico.TrabalhoConclusaoCursoArtefatoVO;
import negocio.comuns.academico.TrabalhoConclusaoCursoEtapaVO;
import negocio.comuns.academico.TrabalhoConclusaoCursoInteracaoVO;
import negocio.comuns.academico.TrabalhoConclusaoCursoVO;
import negocio.comuns.academico.enumeradores.EtapaTCCEnum;
import negocio.comuns.academico.enumeradores.SituacaoTCCEnum;
import negocio.comuns.academico.enumeradores.TipoArquivoTCCEnum;
import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.ComunicadoInternoDestinatarioVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.TipoTCCEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.basico.QuestaoTCCVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.FechamentoPeriodoLetivoException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.SituacaoHistorico;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.TrabalhoConclusaoCursoInterfaceFacade;

@Repository
@Lazy
@Scope
public class TrabalhoConclusaoCurso extends ControleAcesso implements TrabalhoConclusaoCursoInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1512193973100404751L;
	protected static String idEntidade;
	
	public TrabalhoConclusaoCurso() {
		super();
		setIdEntidade("TrabalhoConclusaoCurso");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void persitir(TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO, UsuarioVO usuarioVO) throws Exception {
		if (trabalhoConclusaoCursoVO.isNovoObj()) {
			incluir(trabalhoConclusaoCursoVO);
		} else {
			alterar(trabalhoConclusaoCursoVO);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void incluir(final TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO) throws Exception {
		trabalhoConclusaoCursoVO.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				StringBuilder sql = new StringBuilder("INSERT INTO TrabalhoConclusaoCurso ( ");
				sql.append(" matriculaPeriodoTurmaDisciplina, aluno, configuracaoTCC, orientador, orientadorSugerido, etapaTCC, situacaoTCC, tipoTCC, ");
				sql.append(" dataAlteracaoSituacao, dataInicioPlanoTCC, dataTerminoPlanoTCC, tema, problema, objetivoGeral,  ");
				sql.append(" objetivoEspecifico, metodologia, referenciaBibliografica, dataInicioElaboracaoTCC, dataTerminoElaboracaoTCC, dataInicioAvaliacaoTCC, dataTerminoAvaliacaoTCC, coordenador, titulo ");

				sql.append(") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) RETURNING codigo ");
				PreparedStatement ps = arg0.prepareStatement(sql.toString());
				int x = 1;
				ps.setInt(x++, trabalhoConclusaoCursoVO.getMatriculaPeriodoTurmaDisciplina().getCodigo());
				ps.setInt(x++, trabalhoConclusaoCursoVO.getAluno().getCodigo());
				ps.setInt(x++, trabalhoConclusaoCursoVO.getConfiguracaoTCC().getCodigo());
				if (trabalhoConclusaoCursoVO.getOrientador().getCodigo() > 0) {
					ps.setInt(x++, trabalhoConclusaoCursoVO.getOrientador().getCodigo());
				} else {
					ps.setNull(x++, 0);
				}
				if (trabalhoConclusaoCursoVO.getOrientadorSugerido().getCodigo() > 0) {
					ps.setInt(x++, trabalhoConclusaoCursoVO.getOrientadorSugerido().getCodigo());
				} else {
					ps.setNull(x++, 0);
				}
				ps.setString(x++, trabalhoConclusaoCursoVO.getEtapaTCC().name());
				ps.setString(x++, trabalhoConclusaoCursoVO.getSituacaoTCC().name());
				ps.setString(x++, trabalhoConclusaoCursoVO.getTipoTCC().name());
				if (trabalhoConclusaoCursoVO.getDataAlteracaoSituacao() != null) {
					ps.setTimestamp(x++, Uteis.getDataJDBCTimestamp(trabalhoConclusaoCursoVO.getDataAlteracaoSituacao()));
				} else {
					ps.setNull(x++, 0);
				}
				if (trabalhoConclusaoCursoVO.getDataInicioPlanoTCC() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(trabalhoConclusaoCursoVO.getDataInicioPlanoTCC()));
				} else {
					ps.setNull(x++, 0);
				}
				if (trabalhoConclusaoCursoVO.getDataTerminoPlanoTCC() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(trabalhoConclusaoCursoVO.getDataTerminoPlanoTCC()));
				} else {
					ps.setNull(x++, 0);
				}
				ps.setString(x++, trabalhoConclusaoCursoVO.getTema());
				ps.setString(x++, trabalhoConclusaoCursoVO.getProblema());
				ps.setString(x++, trabalhoConclusaoCursoVO.getObjetivoGeral());
				ps.setString(x++, trabalhoConclusaoCursoVO.getObjetivoEspecifico());
				ps.setString(x++, trabalhoConclusaoCursoVO.getMetodologia());
				ps.setString(x++, trabalhoConclusaoCursoVO.getReferenciaBibliografica());
				if (trabalhoConclusaoCursoVO.getDataInicioElaboracaoTCC() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(trabalhoConclusaoCursoVO.getDataInicioElaboracaoTCC()));
				} else {
					ps.setNull(x++, 0);
				}
				if (trabalhoConclusaoCursoVO.getDataTerminoElaboracaoTCC() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(trabalhoConclusaoCursoVO.getDataTerminoElaboracaoTCC()));
				} else {
					ps.setNull(x++, 0);
				}
				if (trabalhoConclusaoCursoVO.getDataInicioAvaliacaoTCC() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(trabalhoConclusaoCursoVO.getDataInicioAvaliacaoTCC()));
				} else {
					ps.setNull(x++, 0);
				}
				if (trabalhoConclusaoCursoVO.getDataTerminoAvaliacaoTCC() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(trabalhoConclusaoCursoVO.getDataTerminoAvaliacaoTCC()));
				} else {
					ps.setNull(x++, 0);
				}
				if (trabalhoConclusaoCursoVO.getCoordenador() != null && trabalhoConclusaoCursoVO.getCoordenador().getCodigo().intValue() != 0) {
					ps.setInt(x++, trabalhoConclusaoCursoVO.getCoordenador().getCodigo());
				} else {
					ps.setNull(x++, 0);
				}
				ps.setString(x++, trabalhoConclusaoCursoVO.getTitulo());
				
				return ps;
			}
		}, new ResultSetExtractor<Integer>() {

			@Override
			public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
				if (arg0.next()) {
					return arg0.getInt("codigo");
				}
				return 0;
			}
		}));
		getFacadeFactory().getQuestaoTrabalhoConclusaoCursoFacade().incluirQuestaoFormatacao(trabalhoConclusaoCursoVO);
		getFacadeFactory().getQuestaoTrabalhoConclusaoCursoFacade().incluirQuestaoConteudo(trabalhoConclusaoCursoVO);			
		trabalhoConclusaoCursoVO.setNovoObj(false);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void alterar(final TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO) throws Exception {
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				StringBuilder sql = new StringBuilder("UPDATE TrabalhoConclusaoCurso SET ");
				sql.append(" matriculaPeriodoTurmaDisciplina = ?, aluno = ?, configuracaoTCC = ?, orientador = ?, orientadorSugerido = ?, etapaTCC = ?, situacaoTCC = ?, tipoTCC = ?, ");
				sql.append(" dataAlteracaoSituacao = ?, dataInicioPlanoTCC = ?, dataTerminoPlanoTCC = ?, tema = ?, problema = ?, objetivoGeral = ?,  ");
				sql.append(" objetivoEspecifico = ?, metodologia = ?, referenciaBibliografica = ?, dataInicioElaboracaoTCC = ?, dataTerminoElaboracaoTCC = ?, dataInicioAvaliacaoTCC = ?, dataTerminoAvaliacaoTCC = ?, coordenador = ?, titulo=? ");
				sql.append(" WHERE codigo = ? ");
				PreparedStatement ps = arg0.prepareStatement(sql.toString());
				int x = 1;
				ps.setInt(x++, trabalhoConclusaoCursoVO.getMatriculaPeriodoTurmaDisciplina().getCodigo());
				ps.setInt(x++, trabalhoConclusaoCursoVO.getAluno().getCodigo());
				ps.setInt(x++, trabalhoConclusaoCursoVO.getConfiguracaoTCC().getCodigo());
				if (trabalhoConclusaoCursoVO.getOrientador().getCodigo() > 0) {
					ps.setInt(x++, trabalhoConclusaoCursoVO.getOrientador().getCodigo());
				} else {
					ps.setNull(x++, 0);
				}
				if (trabalhoConclusaoCursoVO.getOrientadorSugerido().getCodigo() > 0) {
					ps.setInt(x++, trabalhoConclusaoCursoVO.getOrientadorSugerido().getCodigo());
				} else {
					ps.setNull(x++, 0);
				}
				ps.setString(x++, trabalhoConclusaoCursoVO.getEtapaTCC().name());
				ps.setString(x++, trabalhoConclusaoCursoVO.getSituacaoTCC().name());
				ps.setString(x++, trabalhoConclusaoCursoVO.getTipoTCC().name());
				if (trabalhoConclusaoCursoVO.getDataAlteracaoSituacao() != null) {
					ps.setTimestamp(x++, Uteis.getDataJDBCTimestamp(trabalhoConclusaoCursoVO.getDataAlteracaoSituacao()));
				} else {
					ps.setNull(x++, 0);
				}
				if (trabalhoConclusaoCursoVO.getDataInicioPlanoTCC() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(trabalhoConclusaoCursoVO.getDataInicioPlanoTCC()));
				} else {
					ps.setNull(x++, 0);
				}
				if (trabalhoConclusaoCursoVO.getDataTerminoPlanoTCC() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(trabalhoConclusaoCursoVO.getDataTerminoPlanoTCC()));
				} else {
					ps.setNull(x++, 0);
				}
				ps.setString(x++, trabalhoConclusaoCursoVO.getTema());
				ps.setString(x++, trabalhoConclusaoCursoVO.getProblema());
				ps.setString(x++, trabalhoConclusaoCursoVO.getObjetivoGeral());
				ps.setString(x++, trabalhoConclusaoCursoVO.getObjetivoEspecifico());
				ps.setString(x++, trabalhoConclusaoCursoVO.getMetodologia());
				ps.setString(x++, trabalhoConclusaoCursoVO.getReferenciaBibliografica());
				if (trabalhoConclusaoCursoVO.getDataInicioElaboracaoTCC() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(trabalhoConclusaoCursoVO.getDataInicioElaboracaoTCC()));
				} else {
					ps.setNull(x++, 0);
				}
				if (trabalhoConclusaoCursoVO.getDataTerminoElaboracaoTCC() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(trabalhoConclusaoCursoVO.getDataTerminoElaboracaoTCC()));
				} else {
					ps.setNull(x++, 0);
				}
				if (trabalhoConclusaoCursoVO.getDataInicioAvaliacaoTCC() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(trabalhoConclusaoCursoVO.getDataInicioAvaliacaoTCC()));
				} else {
					ps.setNull(x++, 0);
				}
				if (trabalhoConclusaoCursoVO.getDataTerminoAvaliacaoTCC() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(trabalhoConclusaoCursoVO.getDataTerminoAvaliacaoTCC()));
				} else {
					ps.setNull(x++, 0);
				}
				if (trabalhoConclusaoCursoVO.getCoordenador() != null && trabalhoConclusaoCursoVO.getCoordenador().getCodigo().intValue() != 0) {
					ps.setInt(x++, trabalhoConclusaoCursoVO.getCoordenador().getCodigo());
				} else {
					ps.setNull(x++, 0);
				}

				ps.setString(x++, trabalhoConclusaoCursoVO.getTitulo());
				ps.setInt(x++, trabalhoConclusaoCursoVO.getCodigo());
				return ps;
			}
		});
		getFacadeFactory().getQuestaoTrabalhoConclusaoCursoFacade().alterarQuestaoFormatacao(trabalhoConclusaoCursoVO);
		getFacadeFactory().getQuestaoTrabalhoConclusaoCursoFacade().alterarQuestaoConteudo(trabalhoConclusaoCursoVO);			
		trabalhoConclusaoCursoVO.setNovoObj(false);
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void alterarDataUltimoAcessoAluno(final TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO) throws Exception {
		if (trabalhoConclusaoCursoVO.getDataUltimoAcessoAluno() != null) {
            final String sql = "UPDATE TrabalhoConclusaoCurso SET dataUltimoAcessoAluno = ? WHERE codigo = ?";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setTimestamp(1, Uteis.getDataJDBCTimestamp(trabalhoConclusaoCursoVO.getDataUltimoAcessoAluno()));
                    sqlAlterar.setInt(2, trabalhoConclusaoCursoVO.getCodigo());
                    return sqlAlterar;
                }
            });
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void alterarTitulo(final TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO) throws Exception {
		if (trabalhoConclusaoCursoVO.getDataUltimoAcessoAluno() != null) {
            final String sql = "UPDATE TrabalhoConclusaoCurso SET titulo = ? WHERE codigo = ?";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setString(1, trabalhoConclusaoCursoVO.getTitulo());
                    sqlAlterar.setInt(2, trabalhoConclusaoCursoVO.getCodigo());
                    return sqlAlterar;
                }
            });
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void alterarMediaFormatacao(final TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO) throws Exception {
		if (trabalhoConclusaoCursoVO.getDataUltimoAcessoAluno() != null) {
            final String sql = "UPDATE TrabalhoConclusaoCurso SET mediaFormatacao = ? WHERE codigo = ?";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setDouble(1, trabalhoConclusaoCursoVO.getMediaFormatacao());
                    sqlAlterar.setInt(2, trabalhoConclusaoCursoVO.getCodigo());
                    return sqlAlterar;
                }
            });
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void alterarMediaConteudo(final TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO) throws Exception {
		if (trabalhoConclusaoCursoVO.getDataUltimoAcessoAluno() != null) {
            final String sql = "UPDATE TrabalhoConclusaoCurso SET mediaConteudo = ? WHERE codigo = ?";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setDouble(1, trabalhoConclusaoCursoVO.getMediaConteudo());
                    sqlAlterar.setInt(2, trabalhoConclusaoCursoVO.getCodigo());
                    return sqlAlterar;
                }
            });
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void alterarMedia(final TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO) throws Exception {
		if (trabalhoConclusaoCursoVO.getDataUltimoAcessoAluno() != null) {
            final String sql = "UPDATE TrabalhoConclusaoCurso SET media = ? WHERE codigo = ?";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setDouble(1, trabalhoConclusaoCursoVO.getMedia());
                    sqlAlterar.setInt(2, trabalhoConclusaoCursoVO.getCodigo());
                    return sqlAlterar;
                }
            });
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void alterarMembrosBanca(final TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO, UsuarioVO usuarioLogado) throws Exception {
        final String sql = "UPDATE TrabalhoConclusaoCurso SET dataDefinicaoBanca = ?, observacaoBanca = ? WHERE codigo = ?"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado);
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                sqlAlterar.setTimestamp(1, Uteis.getDataJDBCTimestamp(trabalhoConclusaoCursoVO.getDataDefinicaoBanca()));
                sqlAlterar.setString(2, trabalhoConclusaoCursoVO.getObservacaoBanca());
                sqlAlterar.setInt(3, trabalhoConclusaoCursoVO.getCodigo());
                return sqlAlterar;
            }
        });
        getFacadeFactory().getTrabalhoConclusaoCursoMembroBancaFacade().alterarMembrosBanca(trabalhoConclusaoCursoVO.getCodigo(), trabalhoConclusaoCursoVO.getTrabalhoConclusaoCursoMembroBancaVOs(), usuarioLogado);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void gravarMembrosBanca(TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO, UsuarioVO usuarioVO) throws Exception {

		alterarMembrosBanca(trabalhoConclusaoCursoVO, usuarioVO);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void gravarPlanoTCC(TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO, UsuarioVO usuarioVO) throws Exception {
		SituacaoTCCEnum situacaoTmp = trabalhoConclusaoCursoVO.getSituacaoTCC();
		try {
			trabalhoConclusaoCursoVO.setSituacaoTCC(SituacaoTCCEnum.EM_ELABORACAO);
			trabalhoConclusaoCursoVO.setDataAlteracaoSituacao(new Date());
			persitir(trabalhoConclusaoCursoVO, usuarioVO);
			getFacadeFactory().getHistoricoSituacaoTCCFacade().realizarCriacaoHistoricoSituacaoTCC(trabalhoConclusaoCursoVO, "Aluno", usuarioVO);
		} catch (Exception e) {
			trabalhoConclusaoCursoVO.setSituacaoTCC(situacaoTmp);
			throw e;
		} finally {
			situacaoTmp = null;
		}
	}

	private void validarDadosPlanoTCC(TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO) throws ConsistirException {
		ConsistirException ce = new ConsistirException();
		if (trabalhoConclusaoCursoVO.getOrientador() == null || trabalhoConclusaoCursoVO.getOrientador().getCodigo() == null || trabalhoConclusaoCursoVO.getOrientador().getCodigo() == 0) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_TrabalhoConclusaoCurso_orientador_planoTCC"));
		}
		if (trabalhoConclusaoCursoVO.getTipoTCC() == null) {
			ce.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_TrabalhoConclusaoCurso_tipoTCC"));
		}
		if (trabalhoConclusaoCursoVO.getTema() == null || trabalhoConclusaoCursoVO.getTema().trim().isEmpty()) {
			ce.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_TrabalhoConclusaoCurso_tema"));
		}
		if (trabalhoConclusaoCursoVO.getProblema() == null || trabalhoConclusaoCursoVO.getProblema().trim().isEmpty()) {
			ce.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_TrabalhoConclusaoCurso_problema"));
		}
		if (trabalhoConclusaoCursoVO.getObjetivoGeral() == null || trabalhoConclusaoCursoVO.getObjetivoGeral().trim().isEmpty()) {
			ce.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_TrabalhoConclusaoCurso_objetivoGeral"));
		}
		if (trabalhoConclusaoCursoVO.getObjetivoEspecifico() == null || trabalhoConclusaoCursoVO.getObjetivoEspecifico().trim().isEmpty()) {
			ce.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_TrabalhoConclusaoCurso_objetivoEspecifico"));
		}
		if (trabalhoConclusaoCursoVO.getMetodologia() == null || trabalhoConclusaoCursoVO.getMetodologia().trim().isEmpty()) {
			ce.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_TrabalhoConclusaoCurso_metodologia"));
		}
		if (trabalhoConclusaoCursoVO.getReferenciaBibliografica() == null || trabalhoConclusaoCursoVO.getReferenciaBibliografica().trim().isEmpty()) {
			ce.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_TrabalhoConclusaoCurso_referenciaBibliografica"));
		}
		if (!ce.getListaMensagemErro().isEmpty()) {
			throw ce;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void realizarSolictacaoAvaliacaoOrientadorPlanoTCC(TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO, UsuarioVO usuarioVO) throws Exception {
		validarDadosPlanoTCC(trabalhoConclusaoCursoVO);
		SituacaoTCCEnum situacaoTmp = trabalhoConclusaoCursoVO.getSituacaoTCC();
		try {
			trabalhoConclusaoCursoVO.setSituacaoTCC(SituacaoTCCEnum.AGUARDANDO_APROVACAO_ORIENTADOR);
			trabalhoConclusaoCursoVO.setDataAlteracaoSituacao(new Date());
			persitir(trabalhoConclusaoCursoVO, usuarioVO);
			getFacadeFactory().getHistoricoSituacaoTCCFacade().realizarCriacaoHistoricoSituacaoTCC(trabalhoConclusaoCursoVO, "Aluno", usuarioVO);
			getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemAguardandoAprovacaoOrientador(trabalhoConclusaoCursoVO, usuarioVO);
		} catch (Exception e) {
			trabalhoConclusaoCursoVO.setSituacaoTCC(situacaoTmp);
			throw e;
		} finally {
			situacaoTmp = null;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void realizarRevisaoPlanoTCC(TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO, UsuarioVO usuarioVO) throws Exception {
		SituacaoTCCEnum situacaoTmp = trabalhoConclusaoCursoVO.getSituacaoTCC();
		try {
			trabalhoConclusaoCursoVO.setSituacaoTCC(SituacaoTCCEnum.EM_ELABORACAO);
			trabalhoConclusaoCursoVO.setDataAlteracaoSituacao(new Date());
			persitir(trabalhoConclusaoCursoVO, usuarioVO);
			getFacadeFactory().getHistoricoSituacaoTCCFacade().realizarCriacaoHistoricoSituacaoTCC(trabalhoConclusaoCursoVO, "Orientador", usuarioVO);
		} catch (Exception e) {
			trabalhoConclusaoCursoVO.setSituacaoTCC(situacaoTmp);
			throw e;
		} finally {
			situacaoTmp = null;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void realizarAprovacaoPlanoTCC(TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO, UsuarioVO usuarioVO) throws Exception {
		validarDadosPlanoTCC(trabalhoConclusaoCursoVO);
		SituacaoTCCEnum situacaoTmp = trabalhoConclusaoCursoVO.getSituacaoTCC();
		Date dataTerminoEtapaPervista = trabalhoConclusaoCursoVO.getDataTerminoPlanoTCC();
		try {
			if (trabalhoConclusaoCursoVO.getConfiguracaoTCC().getControlaElaboracaoTCC()) {
				trabalhoConclusaoCursoVO.setSituacaoTCC(SituacaoTCCEnum.AGUARDANDO_ARQUIVO);
				trabalhoConclusaoCursoVO.setEtapaTCC(EtapaTCCEnum.ELABORACAO_TCC);
			} else {
				if (trabalhoConclusaoCursoVO.getConfiguracaoTCC().getExigePostagemArquivoFinalAvaliacaoTCC()) {
					trabalhoConclusaoCursoVO.setSituacaoTCC(SituacaoTCCEnum.AGUARDANDO_ARQUIVO);
				} else {
					trabalhoConclusaoCursoVO.setSituacaoTCC(SituacaoTCCEnum.AGUARDANDO_AVALIACAO_BANCA);
				}
				trabalhoConclusaoCursoVO.setEtapaTCC(EtapaTCCEnum.AVALIACAO_TCC);
			}
			trabalhoConclusaoCursoVO.setDataAlteracaoSituacao(new Date());
			trabalhoConclusaoCursoVO.setDataTerminoPlanoTCC(new Date());
			trabalhoConclusaoCursoVO.setDataInicioElaboracaoTCC(new Date());
			realizarCalculoPrevisaoTerminoEtapas(trabalhoConclusaoCursoVO);
			persitir(trabalhoConclusaoCursoVO, usuarioVO);
			getFacadeFactory().getHistoricoSituacaoTCCFacade().realizarCriacaoHistoricoSituacaoTCC(trabalhoConclusaoCursoVO, "Orientador", usuarioVO);
			getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemAprovacaoOrientador(trabalhoConclusaoCursoVO, usuarioVO);
			if (trabalhoConclusaoCursoVO.getSituacaoTCC().equals(SituacaoTCCEnum.AGUARDANDO_AVALIACAO_BANCA)) {
				getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemAguardandoAvaliacaoBanca(trabalhoConclusaoCursoVO, usuarioVO);
			}
		} catch (Exception e) {
			trabalhoConclusaoCursoVO.setSituacaoTCC(situacaoTmp);
			trabalhoConclusaoCursoVO.setEtapaTCC(EtapaTCCEnum.PLANO_TCC);
			trabalhoConclusaoCursoVO.setDataTerminoPlanoTCC(dataTerminoEtapaPervista);
			realizarCalculoPrevisaoTerminoEtapas(trabalhoConclusaoCursoVO);
			throw e;
		} finally {
			situacaoTmp = null;
		}

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void realizarReprovacaoTCC(TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		SituacaoTCCEnum situacaoTmp = trabalhoConclusaoCursoVO.getSituacaoTCC();
		try {
			trabalhoConclusaoCursoVO.setSituacaoTCC(SituacaoTCCEnum.REPROVADO);
			realizarCalculoPrevisaoTerminoEtapas(trabalhoConclusaoCursoVO);
			persitir(trabalhoConclusaoCursoVO, usuarioVO);
			realizarReprovacaoHistoricoTCC(trabalhoConclusaoCursoVO, usuarioVO);
			if (usuarioVO.getCodigo().intValue() != 0) {
				getFacadeFactory().getHistoricoSituacaoTCCFacade().realizarCriacaoHistoricoSituacaoTCC(trabalhoConclusaoCursoVO, "Orientador", usuarioVO);
			} else {
				getFacadeFactory().getHistoricoSituacaoTCCFacade().realizarCriacaoHistoricoSituacaoTCC(trabalhoConclusaoCursoVO, "Rotina Autom?tica", usuarioVO);
			}
		} catch (Exception e) {
			trabalhoConclusaoCursoVO.setSituacaoTCC(situacaoTmp);
			throw e;
		} finally {
			situacaoTmp = null;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void realizarReprovacaoHistoricoTCC(TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO, UsuarioVO usuarioVO) throws Exception {
//		HistoricoVO historicoVO = getFacadeFactory().getHistoricoFacade().consultarHistoricoPorMatriculaPeriodoTurmaDisciplina(trabalhoConclusaoCursoVO.getMatriculaPeriodoTurmaDisciplina().getCodigo(), new ConfiguracaoFinanceiroVO(), usuarioVO);
		if (trabalhoConclusaoCursoVO.getHistorico() != null && trabalhoConclusaoCursoVO.getHistorico().getCodigo() != null && trabalhoConclusaoCursoVO.getHistorico().getCodigo() > 0 && trabalhoConclusaoCursoVO.getHistorico().getConfiguracaoAcademico().getCodigo().intValue() != 0) {
//			historicoVO.setConfiguracaoAcademico(getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorChavePrimaria(trabalhoConclusaoCursoVO.getHistorico().getConfiguracaoAcademico().getCodigo(), usuarioVO));
			if (trabalhoConclusaoCursoVO.getHistorico().getConfiguracaoAcademico().getUtilizarNota1() && trabalhoConclusaoCursoVO.getHistorico().getConfiguracaoAcademico().getFormulaCalculoNota1().trim().isEmpty() && trabalhoConclusaoCursoVO.getHistorico().getConfiguracaoAcademico().getFormulaUsoNota1().trim().isEmpty()) {
				trabalhoConclusaoCursoVO.getHistorico().setNota1(0.0);
			}
			if (trabalhoConclusaoCursoVO.getHistorico().getConfiguracaoAcademico().getUtilizarNota2() && trabalhoConclusaoCursoVO.getHistorico().getConfiguracaoAcademico().getFormulaCalculoNota2().trim().isEmpty() && trabalhoConclusaoCursoVO.getHistorico().getConfiguracaoAcademico().getFormulaUsoNota2().trim().isEmpty()) {
				trabalhoConclusaoCursoVO.getHistorico().setNota2(0.0);
			}
			if (trabalhoConclusaoCursoVO.getHistorico().getConfiguracaoAcademico().getUtilizarNota3() && trabalhoConclusaoCursoVO.getHistorico().getConfiguracaoAcademico().getFormulaCalculoNota3().trim().isEmpty() && trabalhoConclusaoCursoVO.getHistorico().getConfiguracaoAcademico().getFormulaUsoNota3().trim().isEmpty()) {
				trabalhoConclusaoCursoVO.getHistorico().setNota3(0.0);
			}
			if (trabalhoConclusaoCursoVO.getHistorico().getConfiguracaoAcademico().getUtilizarNota4() && trabalhoConclusaoCursoVO.getHistorico().getConfiguracaoAcademico().getFormulaCalculoNota4().trim().isEmpty() && trabalhoConclusaoCursoVO.getHistorico().getConfiguracaoAcademico().getFormulaUsoNota4().trim().isEmpty()) {
				trabalhoConclusaoCursoVO.getHistorico().setNota4(0.0);
			}
			if (trabalhoConclusaoCursoVO.getHistorico().getConfiguracaoAcademico().getUtilizarNota5() && trabalhoConclusaoCursoVO.getHistorico().getConfiguracaoAcademico().getFormulaCalculoNota5().trim().isEmpty() && trabalhoConclusaoCursoVO.getHistorico().getConfiguracaoAcademico().getFormulaUsoNota5().trim().isEmpty()) {
				trabalhoConclusaoCursoVO.getHistorico().setNota5(0.0);
			}
			if (trabalhoConclusaoCursoVO.getHistorico().getConfiguracaoAcademico().getUtilizarNota6() && trabalhoConclusaoCursoVO.getHistorico().getConfiguracaoAcademico().getFormulaCalculoNota6().trim().isEmpty() && trabalhoConclusaoCursoVO.getHistorico().getConfiguracaoAcademico().getFormulaUsoNota6().trim().isEmpty()) {
				trabalhoConclusaoCursoVO.getHistorico().setNota6(0.0);
			}
			if (trabalhoConclusaoCursoVO.getHistorico().getConfiguracaoAcademico().getUtilizarNota7() && trabalhoConclusaoCursoVO.getHistorico().getConfiguracaoAcademico().getFormulaCalculoNota7().trim().isEmpty() && trabalhoConclusaoCursoVO.getHistorico().getConfiguracaoAcademico().getFormulaUsoNota7().trim().isEmpty()) {
				trabalhoConclusaoCursoVO.getHistorico().setNota7(0.0);
			}
			if (trabalhoConclusaoCursoVO.getHistorico().getConfiguracaoAcademico().getUtilizarNota8() && trabalhoConclusaoCursoVO.getHistorico().getConfiguracaoAcademico().getFormulaCalculoNota8().trim().isEmpty() && trabalhoConclusaoCursoVO.getHistorico().getConfiguracaoAcademico().getFormulaUsoNota8().trim().isEmpty()) {
				trabalhoConclusaoCursoVO.getHistorico().setNota8(0.0);
			}
			if (trabalhoConclusaoCursoVO.getHistorico().getConfiguracaoAcademico().getUtilizarNota9() && trabalhoConclusaoCursoVO.getHistorico().getConfiguracaoAcademico().getFormulaCalculoNota9().trim().isEmpty() && trabalhoConclusaoCursoVO.getHistorico().getConfiguracaoAcademico().getFormulaUsoNota9().trim().isEmpty()) {
				trabalhoConclusaoCursoVO.getHistorico().setNota9(0.0);
			}
			if (trabalhoConclusaoCursoVO.getHistorico().getConfiguracaoAcademico().getUtilizarNota10() && trabalhoConclusaoCursoVO.getHistorico().getConfiguracaoAcademico().getFormulaCalculoNota10().trim().isEmpty() && trabalhoConclusaoCursoVO.getHistorico().getConfiguracaoAcademico().getFormulaUsoNota10().trim().isEmpty()) {
				trabalhoConclusaoCursoVO.getHistorico().setNota10(0.0);
			}
			if (trabalhoConclusaoCursoVO.getHistorico().getConfiguracaoAcademico().getUtilizarNota11() && trabalhoConclusaoCursoVO.getHistorico().getConfiguracaoAcademico().getFormulaCalculoNota11().trim().isEmpty() && trabalhoConclusaoCursoVO.getHistorico().getConfiguracaoAcademico().getFormulaUsoNota11().trim().isEmpty()) {
				trabalhoConclusaoCursoVO.getHistorico().setNota11(0.0);
			}
			if (trabalhoConclusaoCursoVO.getHistorico().getConfiguracaoAcademico().getUtilizarNota12() && trabalhoConclusaoCursoVO.getHistorico().getConfiguracaoAcademico().getFormulaCalculoNota12().trim().isEmpty() && trabalhoConclusaoCursoVO.getHistorico().getConfiguracaoAcademico().getFormulaUsoNota12().trim().isEmpty()) {
				trabalhoConclusaoCursoVO.getHistorico().setNota12(0.0);
			}
			if (trabalhoConclusaoCursoVO.getHistorico().getConfiguracaoAcademico().getUtilizarNota13() && trabalhoConclusaoCursoVO.getHistorico().getConfiguracaoAcademico().getFormulaCalculoNota13().trim().isEmpty() && trabalhoConclusaoCursoVO.getHistorico().getConfiguracaoAcademico().getFormulaUsoNota12().trim().isEmpty()) {
				trabalhoConclusaoCursoVO.getHistorico().setNota13(0.0);
			}
			trabalhoConclusaoCursoVO.getHistorico().setFreguencia(0.0);
			trabalhoConclusaoCursoVO.getHistorico().setSituacao("RE");
//			getFacadeFactory().getHistoricoFacade().calcularMediaFrequenciaAluno(historicoVO.getConfiguracaoAcademico(), historicoVO, trabalhoConclusaoCursoVO.getConfiguracaoTCC().getControlaFrequencia(), usuarioVO);
			getFacadeFactory().getHistoricoFacade().alterar(trabalhoConclusaoCursoVO.getHistorico(), usuarioVO);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void realizarAprovacaoElaboracaoTCC(TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO, UsuarioVO usuarioVO) throws Exception {
		SituacaoTCCEnum situacaoTmp = trabalhoConclusaoCursoVO.getSituacaoTCC();
		Date dataTerminoEtapaPervista = trabalhoConclusaoCursoVO.getDataTerminoElaboracaoTCC();
		try {
			if (trabalhoConclusaoCursoVO.getConfiguracaoTCC().getExigePostagemArquivoFinalAvaliacaoTCC()) {
				trabalhoConclusaoCursoVO.setSituacaoTCC(SituacaoTCCEnum.AGUARDANDO_ARQUIVO);
			} else {
				trabalhoConclusaoCursoVO.setSituacaoTCC(SituacaoTCCEnum.AGUARDANDO_AVALIACAO_BANCA);
			}
			trabalhoConclusaoCursoVO.setEtapaTCC(EtapaTCCEnum.AVALIACAO_TCC);
			trabalhoConclusaoCursoVO.setDataAlteracaoSituacao(new Date());
			trabalhoConclusaoCursoVO.setDataTerminoElaboracaoTCC(new Date());
			trabalhoConclusaoCursoVO.setDataInicioAvaliacaoTCC(new Date());
			realizarCalculoPrevisaoTerminoEtapas(trabalhoConclusaoCursoVO);
			persitir(trabalhoConclusaoCursoVO, usuarioVO);
			getFacadeFactory().getHistoricoSituacaoTCCFacade().realizarCriacaoHistoricoSituacaoTCC(trabalhoConclusaoCursoVO, "Orientador", usuarioVO);
			getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemAprovacaoOrientador(trabalhoConclusaoCursoVO, usuarioVO);
			if (trabalhoConclusaoCursoVO.getSituacaoTCC().equals(SituacaoTCCEnum.AGUARDANDO_AVALIACAO_BANCA)) {
				getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemAguardandoAvaliacaoBanca(trabalhoConclusaoCursoVO, usuarioVO);
			}
		} catch (Exception e) {
			trabalhoConclusaoCursoVO.setSituacaoTCC(situacaoTmp);
			trabalhoConclusaoCursoVO.setEtapaTCC(EtapaTCCEnum.ELABORACAO_TCC);
			trabalhoConclusaoCursoVO.setDataTerminoAvaliacaoTCC(dataTerminoEtapaPervista);
			realizarCalculoPrevisaoTerminoEtapas(trabalhoConclusaoCursoVO);
			throw e;
		} finally {
			situacaoTmp = null;
		}

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void realizarAprovacaoReprovacaoAvaliacaoTCC(TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, 
													    List<HistoricoVO> historicoAluno, UsuarioVO usuarioVO) throws Exception {
		SituacaoTCCEnum situacaoTmp = trabalhoConclusaoCursoVO.getSituacaoTCC();
		try {
//			getFacadeFactory().getHistoricoFacade().calcularMediaFrequenciaAluno(trabalhoConclusaoCursoVO.getHistorico().getConfiguracaoAcademico(), 
//																				 trabalhoConclusaoCursoVO.getHistorico(), 
//																				 trabalhoConclusaoCursoVO.getConfiguracaoTCC().getControlaFrequencia(), usuarioVO);
//			getFacadeFactory().getHistoricoFacade().alterar(trabalhoConclusaoCursoVO.getHistorico(), usuarioVO);
//			executarAtualizarTrabalhoConclusaoCursoAposLancamentoNotas(trabalhoConclusaoCursoVO, configuracaoGeralSistemaVO, usuarioVO);
			realizarVerificacaoAprovacaoAluno(historicoAluno, trabalhoConclusaoCursoVO, configuracaoGeralSistemaVO, usuarioVO);
			getFacadeFactory().getHistoricoSituacaoTCCFacade().realizarCriacaoHistoricoSituacaoTCC(trabalhoConclusaoCursoVO, "Banca", usuarioVO);
			if (trabalhoConclusaoCursoVO.getSituacaoTCC().equals(SituacaoTCCEnum.APROVADO)) {
				getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemTrabalhoConclusaoCursoAprovado(trabalhoConclusaoCursoVO, usuarioVO);
			} else if (trabalhoConclusaoCursoVO.getSituacaoTCC().equals(SituacaoTCCEnum.REPROVADO)) {
				getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemTrabalhoConclusaoCursoReprovado(trabalhoConclusaoCursoVO, usuarioVO);
			}
		} catch (Exception e) {
			trabalhoConclusaoCursoVO.setSituacaoTCC(situacaoTmp);
			throw e;
		} finally {
			situacaoTmp = null;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void executarAtualizarTrabalhoConclusaoCursoAposLancamentoNotas(TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO, 
																		   ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception {
		
		if (trabalhoConclusaoCursoVO.getHistorico().getSituacao().equals(SituacaoHistorico.APROVADO.getValor())) {
			trabalhoConclusaoCursoVO.setSituacaoTCC(SituacaoTCCEnum.APROVADO);
		} else if (trabalhoConclusaoCursoVO.getHistorico().getSituacao().equals(SituacaoHistorico.REPROVADO_FALTA.getValor())) {
			trabalhoConclusaoCursoVO.setSituacaoTCC(SituacaoTCCEnum.REPROVADO_FALTA);
		} else {
			trabalhoConclusaoCursoVO.setSituacaoTCC(SituacaoTCCEnum.REPROVADO);
		}
		trabalhoConclusaoCursoVO.setDataAlteracaoSituacao(new Date());
		persitir(trabalhoConclusaoCursoVO, usuarioVO);
		getFacadeFactory().getHistoricoSituacaoTCCFacade().realizarCriacaoHistoricoSituacaoTCC(trabalhoConclusaoCursoVO, "Banca", usuarioVO);

		List<TrabalhoConclusaoCursoArquivoVO> trabalhoConclusaoCursoArquivoVOs = null;
		if (trabalhoConclusaoCursoVO.getConfiguracaoTCC().getApagarHistoricoPostagemArquivoAposFinalizacaoTCC()) {
			trabalhoConclusaoCursoArquivoVOs = getFacadeFactory().getTrabalhoConclusaoCursoArquivoFacade().consultarPorTCCEtapa(trabalhoConclusaoCursoVO.getCodigo(), null, null, 0, 0);
			boolean arquivoFinalMantido = false;
			for (TrabalhoConclusaoCursoArquivoVO trabalhoConclusaoCursoArquivoVO : trabalhoConclusaoCursoArquivoVOs) {
				if (trabalhoConclusaoCursoArquivoVO.getTipoArquivoTCC().equals(TipoArquivoTCCEnum.ARQUIVO_TCC) && !arquivoFinalMantido) {
					arquivoFinalMantido = true;
				} else {
					getFacadeFactory().getTrabalhoConclusaoCursoArquivoFacade().excluir(trabalhoConclusaoCursoArquivoVO, configuracaoGeralSistemaVO, usuarioVO);
				}
			}
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void realizarVerificacaoAprovacaoAluno(List<HistoricoVO> historicoAluno, TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioLogado) throws Exception {
		for (HistoricoVO historicoVo : historicoAluno) {
//			historicoVo.setConfiguracaoAcademico(getConfiguracaoAcademicoVO());
//			historicoVo.setMediaFinal(null);
//			historicoVo.setMediaFinalConceito(new ConfiguracaoAcademicoNotaConceitoVO());
//			historicoVo.setHistoricoCriterioAvaliacaoAluno(false);
//			historicoVo.setCriterioAvaliacao(null);
//			getFacadeFactory().getHistoricoFacade().verificarNotasLancadas(historicoVo, getUsuarioLogado());
//			verificarAprovacaoAluno(historicoVo);

			
			historicoVo.setConfiguracaoAcademico(trabalhoConclusaoCursoVO.getHistorico().getConfiguracaoAcademico());
			historicoVo.setMediaFinal(null);
			historicoVo.setMediaFinalConceito(new ConfiguracaoAcademicoNotaConceitoVO());
			getFacadeFactory().getHistoricoFacade().verificarNotasLancadas(historicoVo, usuarioLogado);
			verificarAprovacaoAluno(historicoVo, trabalhoConclusaoCursoVO, configuracaoGeralSistemaVO, usuarioLogado);
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void verificarAprovacaoAluno(HistoricoVO historicoVO, TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioLogado) throws Exception {
		ConfiguracaoAcademicoVO ca = trabalhoConclusaoCursoVO.getHistorico().getConfiguracaoAcademico();
		historicoVO.setConfiguracaoAcademico(ca);
		try {
			calcularMedia(ca, historicoVO, trabalhoConclusaoCursoVO, configuracaoGeralSistemaVO, usuarioLogado);
		} catch (Exception e) {
			historicoVO.setSituacao("VS");
			if (!trabalhoConclusaoCursoVO.getConfiguracaoTCC().getControlaFrequencia()) {
				historicoVO.setFreguencia(100.0);
			}
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void calcularMedia(ConfiguracaoAcademicoVO ca, HistoricoVO histVO, TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioLogado) throws Exception {

		if (!ca.verificarHistoricoProvenienteImportacao(histVO)) {
			boolean resultado = false;
			try {
				if (!trabalhoConclusaoCursoVO.getConfiguracaoTCC().getControlaFrequencia()) {
					histVO.setFreguencia(100.0);
				} else {
					getFacadeFactory().getHistoricoFacade().verificaAlunoReprovadoFalta(histVO, ca, usuarioLogado);
				}				
				resultado = ca.substituirVariaveisFormulaPorValores(histVO, null, true);
			} catch (FechamentoPeriodoLetivoException e) {
				histVO.setMediaFinal(null);
				getFacadeFactory().getLogFechamentoFacade().realizarRegistroLogFechamento(histVO.getMatricula().getMatricula());
			}
			if (histVO.getMediaFinal() != null) {
				if(trabalhoConclusaoCursoVO.getHistorico().getConfiguracaoAcademico().getUtilizarArredondamentoMediaParaMais()){
					histVO.setMediaFinal(Uteis.arredondarMultiploDeCincoParaCima(histVO.getMediaFinal()));
				}else if (trabalhoConclusaoCursoVO.getHistorico().getConfiguracaoAcademico().getNotasDeCincoEmCincoDecimos() || 
						trabalhoConclusaoCursoVO.getHistorico().getConfiguracaoAcademico().getNotasDeCincoEmCincoDecimosApenasMedia()) {
					histVO.setMediaFinal(Math.round(2 * histVO.getMediaFinal()) / 2.0);
				}
				if ((!histVO.getSituacao().equals(SituacaoHistorico.REPROVADO_FALTA.getValor())) && (!histVO.getSituacao().equals(SituacaoHistorico.ISENTO.getValor())) && !histVO.getSituacao().equals("")) {
					if (resultado) {
						histVO.setSituacao(SituacaoHistorico.APROVADO.getValor());
					} else {
						histVO.setSituacao(SituacaoHistorico.REPROVADO.getValor());
					}
				}
			} else {
				histVO.setSituacao(SituacaoHistorico.CURSANDO.getValor());
			}
			trabalhoConclusaoCursoVO.setHistorico(histVO);
			getFacadeFactory().getHistoricoFacade().alterar(trabalhoConclusaoCursoVO.getHistorico(), usuarioLogado);
			executarAtualizarTrabalhoConclusaoCursoAposLancamentoNotas(trabalhoConclusaoCursoVO, configuracaoGeralSistemaVO, usuarioLogado);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void realizarSolicitacaoOrientadorTCC(TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO, UsuarioVO usuarioVO) throws Exception {
		if (trabalhoConclusaoCursoVO.getOrientadorSugerido() == null || trabalhoConclusaoCursoVO.getOrientadorSugerido().getCodigo() == null || trabalhoConclusaoCursoVO.getOrientadorSugerido().getCodigo() == 0) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_TrabalhoConclusaoCurso_orientadorSugerido"));
		}
		persitir(trabalhoConclusaoCursoVO, usuarioVO);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void realizarConfirmacaoSolicitacaoOrientadorTCC(TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO, UsuarioVO usuarioVO) throws Exception {
		try {
			trabalhoConclusaoCursoVO.setOrientador(trabalhoConclusaoCursoVO.getOrientadorSugerido());
			trabalhoConclusaoCursoVO.setOrientadorSugerido(null);
			persitir(trabalhoConclusaoCursoVO, usuarioVO);
		} catch (Exception e) {
			trabalhoConclusaoCursoVO.setOrientadorSugerido(trabalhoConclusaoCursoVO.getOrientador());
			trabalhoConclusaoCursoVO.setOrientador(null);
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void realizarDefinicaoOrientadorTCC(TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO, UsuarioVO usuarioVO) throws Exception {
		try {
			if (trabalhoConclusaoCursoVO.getOrientador() == null || trabalhoConclusaoCursoVO.getOrientador().getCodigo() == null || trabalhoConclusaoCursoVO.getOrientador().getCodigo() == 0) {
				throw new ConsistirException(UteisJSF.internacionalizar("msg_TrabalhoConclusaoCurso_orientadorSugerido"));
			}
			persitir(trabalhoConclusaoCursoVO, usuarioVO);
		} catch (Exception e) {			
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void realizarEnvioInteracaoTCC(TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO, TrabalhoConclusaoCursoInteracaoVO trabalhoConclusaoCursoInteracaoVO, UsuarioVO usuarioVO) throws Exception {
		trabalhoConclusaoCursoInteracaoVO.setEtapaTCC(trabalhoConclusaoCursoVO.getEtapaTCC());
		trabalhoConclusaoCursoInteracaoVO.setDataInteracao(new Date());
		trabalhoConclusaoCursoInteracaoVO.getTrabalhoConclusaoCurso().setCodigo(trabalhoConclusaoCursoVO.getCodigo());
		trabalhoConclusaoCursoInteracaoVO.getResponsavelInteracao().setCodigo(usuarioVO.getCodigo());
		getFacadeFactory().getTrabalhoConclusaoCursoInteracaoFacade().incluir(trabalhoConclusaoCursoInteracaoVO, usuarioVO);
		if (trabalhoConclusaoCursoVO.getEtapaTCC().equals(EtapaTCCEnum.PLANO_TCC)) {
			trabalhoConclusaoCursoVO.getTrabalhoConclusaoCursoInteracaoPlanoTCCVOs().setPage(1);
			trabalhoConclusaoCursoVO.getTrabalhoConclusaoCursoInteracaoPlanoTCCVOs().setPaginaAtual(1);
			trabalhoConclusaoCursoVO.getTrabalhoConclusaoCursoInteracaoPlanoTCCVOs().setListaConsulta(getFacadeFactory().getTrabalhoConclusaoCursoInteracaoFacade().consultarPorTCCEtapa(trabalhoConclusaoCursoVO.getCodigo(), EtapaTCCEnum.PLANO_TCC, 5, 0));
			trabalhoConclusaoCursoVO.getTrabalhoConclusaoCursoInteracaoPlanoTCCVOs().setTotalRegistrosEncontrados(getFacadeFactory().getTrabalhoConclusaoCursoInteracaoFacade().consultarTotalRegistroPorTCCEtapa(trabalhoConclusaoCursoVO.getCodigo(), EtapaTCCEnum.PLANO_TCC));
		} else if (trabalhoConclusaoCursoVO.getEtapaTCC().equals(EtapaTCCEnum.ELABORACAO_TCC)) {
			trabalhoConclusaoCursoVO.getTrabalhoConclusaoCursoInteracaoElaboracaoTCCVOs().setPage(1);
			trabalhoConclusaoCursoVO.getTrabalhoConclusaoCursoInteracaoElaboracaoTCCVOs().setPaginaAtual(1);
			trabalhoConclusaoCursoVO.getTrabalhoConclusaoCursoInteracaoElaboracaoTCCVOs().setListaConsulta(getFacadeFactory().getTrabalhoConclusaoCursoInteracaoFacade().consultarPorTCCEtapa(trabalhoConclusaoCursoVO.getCodigo(), EtapaTCCEnum.ELABORACAO_TCC, 5, 0));
			trabalhoConclusaoCursoVO.getTrabalhoConclusaoCursoInteracaoElaboracaoTCCVOs().setTotalRegistrosEncontrados(getFacadeFactory().getTrabalhoConclusaoCursoInteracaoFacade().consultarTotalRegistroPorTCCEtapa(trabalhoConclusaoCursoVO.getCodigo(), EtapaTCCEnum.ELABORACAO_TCC));
		} else if (trabalhoConclusaoCursoVO.getEtapaTCC().equals(EtapaTCCEnum.AVALIACAO_TCC)) {
			trabalhoConclusaoCursoVO.getTrabalhoConclusaoCursoInteracaoAvaliacaoTCCVOs().setPage(1);
			trabalhoConclusaoCursoVO.getTrabalhoConclusaoCursoInteracaoAvaliacaoTCCVOs().setPaginaAtual(1);
			trabalhoConclusaoCursoVO.getTrabalhoConclusaoCursoInteracaoAvaliacaoTCCVOs().setListaConsulta(getFacadeFactory().getTrabalhoConclusaoCursoInteracaoFacade().consultarPorTCCEtapa(trabalhoConclusaoCursoVO.getCodigo(), EtapaTCCEnum.AVALIACAO_TCC, 5, 0));
			trabalhoConclusaoCursoVO.getTrabalhoConclusaoCursoInteracaoAvaliacaoTCCVOs().setTotalRegistrosEncontrados(getFacadeFactory().getTrabalhoConclusaoCursoInteracaoFacade().consultarTotalRegistroPorTCCEtapa(trabalhoConclusaoCursoVO.getCodigo(), EtapaTCCEnum.AVALIACAO_TCC));
		}
	}

	public void realizarCalculoPrevisaoTerminoEtapas(TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO) {
		Date dataBase = null;
		if (trabalhoConclusaoCursoVO.getEtapaTCC().equals(EtapaTCCEnum.PLANO_TCC) && trabalhoConclusaoCursoVO.getDataInicioPlanoTCC() != null) {
			dataBase = trabalhoConclusaoCursoVO.getDataInicioPlanoTCC();
			if (trabalhoConclusaoCursoVO.getDataTerminoPlanoTCC() == null) {
				dataBase = Uteis.obterDataAvancada(dataBase, trabalhoConclusaoCursoVO.getConfiguracaoTCC().getPrazoExecucaoPlanoTCC());
				trabalhoConclusaoCursoVO.setDataTerminoPlanoTCC(dataBase);
			} else {
				dataBase = trabalhoConclusaoCursoVO.getDataTerminoPlanoTCC();
			}
			if (trabalhoConclusaoCursoVO.getConfiguracaoTCC().getControlaElaboracaoTCC()) {
				dataBase = Uteis.obterDataAvancada(dataBase, 1);
				trabalhoConclusaoCursoVO.setDataInicioElaboracaoTCC(dataBase);
				dataBase = Uteis.obterDataAvancada(dataBase, trabalhoConclusaoCursoVO.getConfiguracaoTCC().getPrazoExecucaoElaboracaoTCC());
				trabalhoConclusaoCursoVO.setDataTerminoElaboracaoTCC(dataBase);
			}
			dataBase = Uteis.obterDataAvancada(dataBase, 1);
			trabalhoConclusaoCursoVO.setDataInicioAvaliacaoTCC(dataBase);
			dataBase = Uteis.obterDataAvancada(dataBase, trabalhoConclusaoCursoVO.getConfiguracaoTCC().getPrazoExecucaoApresentacaoTCC());
			trabalhoConclusaoCursoVO.setDataTerminoAvaliacaoTCC(dataBase);
		} else if (trabalhoConclusaoCursoVO.getEtapaTCC().equals(EtapaTCCEnum.ELABORACAO_TCC) && trabalhoConclusaoCursoVO.getDataInicioElaboracaoTCC() != null) {
			dataBase = trabalhoConclusaoCursoVO.getDataInicioElaboracaoTCC();
			if (trabalhoConclusaoCursoVO.getDataTerminoElaboracaoTCC() == null) {
				dataBase = Uteis.obterDataAvancada(dataBase, trabalhoConclusaoCursoVO.getConfiguracaoTCC().getPrazoExecucaoElaboracaoTCC());
				trabalhoConclusaoCursoVO.setDataTerminoElaboracaoTCC(dataBase);
			} else {
				dataBase = trabalhoConclusaoCursoVO.getDataTerminoElaboracaoTCC();
			}
			dataBase = Uteis.obterDataAvancada(dataBase, 1);
			trabalhoConclusaoCursoVO.setDataInicioAvaliacaoTCC(dataBase);
			dataBase = Uteis.obterDataAvancada(dataBase, trabalhoConclusaoCursoVO.getConfiguracaoTCC().getPrazoExecucaoApresentacaoTCC());
			trabalhoConclusaoCursoVO.setDataTerminoAvaliacaoTCC(dataBase);
		} else if (trabalhoConclusaoCursoVO.getEtapaTCC().equals(EtapaTCCEnum.AVALIACAO_TCC) && trabalhoConclusaoCursoVO.getDataInicioAvaliacaoTCC() != null) {
			dataBase = trabalhoConclusaoCursoVO.getDataInicioAvaliacaoTCC();
			if (trabalhoConclusaoCursoVO.getDataTerminoAvaliacaoTCC() == null) {
				dataBase = Uteis.obterDataAvancada(dataBase, trabalhoConclusaoCursoVO.getConfiguracaoTCC().getPrazoExecucaoApresentacaoTCC());
				trabalhoConclusaoCursoVO.setDataTerminoAvaliacaoTCC(dataBase);
			}
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void realizarAlteracaoPrazoExecucaoEtapaTCC(TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO, UsuarioVO usuarioVO) throws Exception {
		realizarCalculoPrevisaoTerminoEtapas(trabalhoConclusaoCursoVO);
		persitir(trabalhoConclusaoCursoVO, usuarioVO);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void realizarRetornoFaseAnteriorTCC(TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO, UsuarioVO usuarioVO) throws Exception {
		EtapaTCCEnum etapaTCCEnum = trabalhoConclusaoCursoVO.getEtapaTCC();
		SituacaoTCCEnum situacaoTCCEnum = trabalhoConclusaoCursoVO.getSituacaoTCC();
		try {
			if (trabalhoConclusaoCursoVO.getEtapaTCC().equals(EtapaTCCEnum.AVALIACAO_TCC)) {
				if (trabalhoConclusaoCursoVO.getConfiguracaoTCC().getControlaElaboracaoTCC()) {
					trabalhoConclusaoCursoVO.setEtapaTCC(EtapaTCCEnum.ELABORACAO_TCC);
					trabalhoConclusaoCursoVO.setSituacaoTCC(SituacaoTCCEnum.AGUARDANDO_ARQUIVO);
				} else if (trabalhoConclusaoCursoVO.getConfiguracaoTCC().getControlaPlanoTCC()) {
					trabalhoConclusaoCursoVO.setEtapaTCC(EtapaTCCEnum.PLANO_TCC);
					trabalhoConclusaoCursoVO.setSituacaoTCC(SituacaoTCCEnum.EM_ELABORACAO);
				}
				persitir(trabalhoConclusaoCursoVO, usuarioVO);
				getFacadeFactory().getHistoricoSituacaoTCCFacade().realizarCriacaoHistoricoSituacaoTCC(trabalhoConclusaoCursoVO, "Orientador", usuarioVO);
			} else if (trabalhoConclusaoCursoVO.getEtapaTCC().equals(EtapaTCCEnum.ELABORACAO_TCC) && trabalhoConclusaoCursoVO.getConfiguracaoTCC().getControlaPlanoTCC()) {
				trabalhoConclusaoCursoVO.setEtapaTCC(EtapaTCCEnum.PLANO_TCC);
				trabalhoConclusaoCursoVO.setSituacaoTCC(SituacaoTCCEnum.EM_ELABORACAO);
				persitir(trabalhoConclusaoCursoVO, usuarioVO);
				getFacadeFactory().getHistoricoSituacaoTCCFacade().realizarCriacaoHistoricoSituacaoTCC(trabalhoConclusaoCursoVO, "Orientador", usuarioVO);
			} else {
				throw new Exception(UteisJSF.internacionalizar("msg_TrabalhoConclusaoCurso_naoPossuiFaseAnterior"));
			}
		} catch (Exception e) {
			trabalhoConclusaoCursoVO.setEtapaTCC(etapaTCCEnum);
			trabalhoConclusaoCursoVO.setSituacaoTCC(situacaoTCCEnum);
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void realizarPostagemArquivoTCC(FileUploadEvent uploadEvent, TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO, TipoArquivoTCCEnum tipoArquivoTCCEnum, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception {
		List<TrabalhoConclusaoCursoArquivoVO> trabalhoConclusaoCursoArquivoVOs = null;
		try {
			getFacadeFactory().getTrabalhoConclusaoCursoArquivoFacade().realizarPostagemArquivo(uploadEvent, tipoArquivoTCCEnum, trabalhoConclusaoCursoVO, configuracaoGeralSistemaVO, usuarioVO);
			if (trabalhoConclusaoCursoVO.getEtapaTCC().equals(EtapaTCCEnum.ELABORACAO_TCC) && !trabalhoConclusaoCursoVO.getConfiguracaoTCC().getControlarHistoricoPostagemArquivoElaboracaoTCC()) {
				trabalhoConclusaoCursoArquivoVOs = getFacadeFactory().getTrabalhoConclusaoCursoArquivoFacade().consultarPorTCCEtapa(trabalhoConclusaoCursoVO.getCodigo(), trabalhoConclusaoCursoVO.getEtapaTCC(), tipoArquivoTCCEnum, 0, 0);
			} else if (trabalhoConclusaoCursoVO.getEtapaTCC().equals(EtapaTCCEnum.AVALIACAO_TCC) && !trabalhoConclusaoCursoVO.getConfiguracaoTCC().getControlarHistoricoPostagemArquivoApresentacaoTCC()) {
				trabalhoConclusaoCursoArquivoVOs = getFacadeFactory().getTrabalhoConclusaoCursoArquivoFacade().consultarPorTCCEtapa(trabalhoConclusaoCursoVO.getCodigo(), trabalhoConclusaoCursoVO.getEtapaTCC(), tipoArquivoTCCEnum, 0, 0);
			}
			if (trabalhoConclusaoCursoArquivoVOs != null && trabalhoConclusaoCursoArquivoVOs.size() > 1) {
				for (int x = 1; x < trabalhoConclusaoCursoArquivoVOs.size(); x++) {
					getFacadeFactory().getTrabalhoConclusaoCursoArquivoFacade().excluir(trabalhoConclusaoCursoArquivoVOs.get(x), configuracaoGeralSistemaVO, usuarioVO);
				}
			}

			if (trabalhoConclusaoCursoVO.getEtapaTCC().equals(EtapaTCCEnum.ELABORACAO_TCC)) {
				trabalhoConclusaoCursoVO.getTrabalhoConclusaoCursoArquivoEtapaElaboracaoVOs().setPage(1);
				trabalhoConclusaoCursoVO.getTrabalhoConclusaoCursoArquivoEtapaElaboracaoVOs().setPaginaAtual(1);
				trabalhoConclusaoCursoVO.getTrabalhoConclusaoCursoArquivoEtapaElaboracaoVOs().setListaConsulta(getFacadeFactory().getTrabalhoConclusaoCursoArquivoFacade().consultarPorTCCEtapa(trabalhoConclusaoCursoVO.getCodigo(), EtapaTCCEnum.ELABORACAO_TCC, null, 5, 0));
				trabalhoConclusaoCursoVO.getTrabalhoConclusaoCursoArquivoEtapaElaboracaoVOs().setTotalRegistrosEncontrados(getFacadeFactory().getTrabalhoConclusaoCursoArquivoFacade().consultarTotalRegistroPorTCCEtapa(trabalhoConclusaoCursoVO.getCodigo(), EtapaTCCEnum.ELABORACAO_TCC, null));
			} else if (trabalhoConclusaoCursoVO.getEtapaTCC().equals(EtapaTCCEnum.AVALIACAO_TCC)) {
				trabalhoConclusaoCursoVO.getTrabalhoConclusaoCursoArquivoEtapaAvaliacaoVOs().setPage(1);
				trabalhoConclusaoCursoVO.getTrabalhoConclusaoCursoArquivoEtapaAvaliacaoVOs().setPaginaAtual(1);
				trabalhoConclusaoCursoVO.getTrabalhoConclusaoCursoArquivoEtapaAvaliacaoVOs().setListaConsulta(getFacadeFactory().getTrabalhoConclusaoCursoArquivoFacade().consultarPorTCCEtapa(trabalhoConclusaoCursoVO.getCodigo(), EtapaTCCEnum.AVALIACAO_TCC, null, 5, 0));
				trabalhoConclusaoCursoVO.getTrabalhoConclusaoCursoArquivoEtapaAvaliacaoVOs().setTotalRegistrosEncontrados(getFacadeFactory().getTrabalhoConclusaoCursoArquivoFacade().consultarTotalRegistroPorTCCEtapa(trabalhoConclusaoCursoVO.getCodigo(), EtapaTCCEnum.AVALIACAO_TCC, null));
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (trabalhoConclusaoCursoArquivoVOs != null) {
				Uteis.liberarListaMemoria(trabalhoConclusaoCursoArquivoVOs);
			}
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void realizarSolicitacaoAvaliacaoArquivoTCC(TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO, UsuarioVO usuarioVO) throws Exception {
		SituacaoTCCEnum situacaoTCCEnum = trabalhoConclusaoCursoVO.getSituacaoTCC();
		try {
			if (trabalhoConclusaoCursoVO.getEtapaTCC().equals(EtapaTCCEnum.ELABORACAO_TCC)) {
				if (trabalhoConclusaoCursoVO.getTrabalhoConclusaoCursoArquivoEtapaElaboracaoVOs().getListaConsulta().isEmpty()) {
					throw new Exception(UteisJSF.internacionalizar("msg_TrabalhoConclusaoCurso_arquivo"));
				}
				trabalhoConclusaoCursoVO.setSituacaoTCC(SituacaoTCCEnum.AGUARDANDO_APROVACAO_ORIENTADOR);
			} else {
				if (trabalhoConclusaoCursoVO.getTrabalhoConclusaoCursoArquivoEtapaAvaliacaoVOs().getListaConsulta().isEmpty()) {
					throw new Exception(UteisJSF.internacionalizar("msg_TrabalhoConclusaoCurso_arquivo"));
				}
				trabalhoConclusaoCursoVO.setSituacaoTCC(SituacaoTCCEnum.AGUARDANDO_AVALIACAO_BANCA);
			}
			persitir(trabalhoConclusaoCursoVO, usuarioVO);
			getFacadeFactory().getHistoricoSituacaoTCCFacade().realizarCriacaoHistoricoSituacaoTCC(trabalhoConclusaoCursoVO, "Aluno", usuarioVO);
			getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemAguardandoAprovacaoOrientador(trabalhoConclusaoCursoVO, usuarioVO);
			if (trabalhoConclusaoCursoVO.getSituacaoTCC().equals(SituacaoTCCEnum.AGUARDANDO_AVALIACAO_BANCA)) {
				getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemAguardandoAvaliacaoBanca(trabalhoConclusaoCursoVO, usuarioVO);
			}
		} catch (Exception e) {
			trabalhoConclusaoCursoVO.setSituacaoTCC(situacaoTCCEnum);
			throw e;
		}
	}

	@Override
	public void realizarEnvioNovoArquivoTCC(TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO, UsuarioVO usuarioVO) throws Exception {
		EtapaTCCEnum etapaTCCEnum = trabalhoConclusaoCursoVO.getEtapaTCC();
		SituacaoTCCEnum situacaoTCCEnum = trabalhoConclusaoCursoVO.getSituacaoTCC();
		try {
			trabalhoConclusaoCursoVO.setSituacaoTCC(SituacaoTCCEnum.AGUARDANDO_ARQUIVO);
			if (trabalhoConclusaoCursoVO.getConfiguracaoTCC().getControlaElaboracaoTCC()) {
				trabalhoConclusaoCursoVO.setEtapaTCC(EtapaTCCEnum.ELABORACAO_TCC);
			}
			persitir(trabalhoConclusaoCursoVO, usuarioVO);
			getFacadeFactory().getHistoricoSituacaoTCCFacade().realizarCriacaoHistoricoSituacaoTCC(trabalhoConclusaoCursoVO, "Orientador", usuarioVO);
		} catch (Exception e) {
			trabalhoConclusaoCursoVO.setEtapaTCC(etapaTCCEnum);
			trabalhoConclusaoCursoVO.setSituacaoTCC(situacaoTCCEnum);
			throw e;
		}
	}

	public String getSelectCompleto() {
		StringBuilder sql = new StringBuilder("SELECT TrabalhoConclusaoCurso.*, ");
		
		sql.append(" case when (TrabalhoConclusaoCurso.etapatcc = 'PLANO_TCC') then (TrabalhoConclusaoCurso.dataInicioPlanoTCC) ");
		sql.append(" when (TrabalhoConclusaoCurso.etapatcc = 'ELABORACAO_TCC') then (TrabalhoConclusaoCurso.dataInicioElaboracaoTCC) ");
		sql.append(" when (TrabalhoConclusaoCurso.etapatcc = 'AVALIACAO_TCC') then (TrabalhoConclusaoCurso.dataInicioAvaliacaoTCC) end as dataInicioEtapaAtual, ");
		sql.append(" case when (TrabalhoConclusaoCurso.etapatcc = 'PLANO_TCC') then (TrabalhoConclusaoCurso.dataTerminoPlanoTCC) ");
		sql.append(" when (TrabalhoConclusaoCurso.etapatcc = 'ELABORACAO_TCC') then (TrabalhoConclusaoCurso.dataTerminoElaboracaoTCC) "); 
		sql.append(" when (TrabalhoConclusaoCurso.etapatcc = 'AVALIACAO_TCC') then (TrabalhoConclusaoCurso.dataTerminoAvaliacaoTCC) end as dataTerminoEtapaAtual, ");
		sql.append(" aluno.nome as \"aluno.nome\", aluno.email as \"aluno.email\",  ");
		
		sql.append(" fotoAluno.nome as \"fotoAluno.nome\", fotoAluno.codigo as \"fotoAluno.codigo\", fotoAluno.pastaBaseArquivo as \"fotoAluno.pastaBaseArquivo\", ");
		sql.append(" matricula.matricula as \"matricula.matricula\", matricula.situacao as \"matricula.situacao\", curso.codigo as \"curso.codigo\", curso.nome as \"curso.nome\",  ");
		sql.append(" turno.codigo as \"turno.codigo\", turno.nome as \"turno.nome\", curso.configuracaoAcademico as \"curso.configuracaoAcademico\", ");
		sql.append(" turma.codigo as \"turma.codigo\", turma.identificadorTurma as \"turma.identificadorTurma\",  ");
		sql.append(" disciplina.codigo as \"disciplina.codigo\", disciplina.nome as \"disciplina.nome\", gradeDisciplina.configuracaoAcademico as \"gradeDisciplina.configuracaoAcademico\", ");
		sql.append(" matriculaperiodoturmadisciplina.ano as \"matriculaperiodoturmadisciplina.ano\", matriculaperiodoturmadisciplina.semestre as \"matriculaperiodoturmadisciplina.semestre\",  ");
		sql.append(" orientador.nome as \"orientador.nome\", orientador.email as \"orientador.email\", coordenador.nome as \"coordenador.nome\", ");
		sql.append(" fotoOrientador.nome as \"fotoOrientador.nome\", fotoOrientador.codigo as \"fotoOrientador.codigo\", fotoOrientador.pastaBaseArquivo as \"fotoOrientador.pastaBaseArquivo\", ");
		sql.append(" orientadorSugerido.nome as \"orientadorSugerido.nome\", orientadorSugerido.email as \"orientadorSugerido.email\",  ");
		sql.append(" fotoOrientadorSugerido.nome as \"fotoOrientadorSugerido.nome\", fotoOrientadorSugerido.codigo as \"fotoOrientadorSugerido.codigo\", fotoOrientadorSugerido.pastaBaseArquivo as \"fotoOrientadorSugerido.pastaBaseArquivo\", ");
		/**
		 * Dados da ConfiguracaoTCC
		 */
		sql.append("configuracaoTCC.codigo as \"confTCC.codigo\", ");
		sql.append("configuracaoTCC.controlaPlanoTCC as \"confTCC.controlaPlanoTCC\", ");
		sql.append("configuracaoTCC.prazoExecucaoPlanoTCC as \"confTCC.prazoExecucaoPlanoTCC\", ");
		sql.append("configuracaoTCC.prazoRespostaOrientadorPlanoTCC as \"confTCC.prazoRespostaOrientadorPlanoTCC\", ");
		sql.append("configuracaoTCC.permiteInteracaoOrientadorPlanoTCC as \"confTCC.permiteInteracaoOrientadorPlanoTCC\", ");
		sql.append("configuracaoTCC.permiteSolicitarOrientador as \"confTCC.permiteSolicitarOrientador\", ");
		sql.append("configuracaoTCC.valorPagamentoOrientadorPlanoTCC as \"confTCC.valorPagamentoCoordenadorPlanoTCC\", ");
		sql.append("configuracaoTCC.controlaElaboracaoTCC as \"confTCC.controlaElaboracaoTCC\", ");
		sql.append("configuracaoTCC.prazoExecucaoElaboracaoTCC as \"confTCC.prazoExecucaoElaboracaoTCC\", ");
		sql.append("configuracaoTCC.prazoRespostaOrientadorElaboracaoTCC as \"confTCC.prazoRespostaOrientadorElaboracaoTCC\", ");
		sql.append("configuracaoTCC.permiteInteracaoOrientadorElaboracaoTCC as \"confTCC.permiteInteracaoOrientadorElaboracaoTCC\", ");
		sql.append("configuracaoTCC.permiteArquivoApoioElaboracaoTCC as \"confTCC.permiteArquivoApoioElaboracaoTCC\", ");
		sql.append("configuracaoTCC.controlarHistoricoPostagemArquivoElaboracaoTCC as \"confTCC.controlarHistoricoPostagemArquivoElaboracaoTCC\", ");
		sql.append("configuracaoTCC.valorPagamentoOrientadorElaboracaoTCC as \"confTCC.valorPagamentoOrientadorElaboracaoTCC\", ");
		sql.append("configuracaoTCC.valorPagamentoCoordenadorElaboracaoTCC as \"confTCC.valorPagamentoCoordenadorElaboracaoTCC\", ");
		sql.append("configuracaoTCC.prazoExecucaoApresentacaoTCC as \"confTCC.prazoExecucaoApresentacaoTCC\", ");
		sql.append("configuracaoTCC.permiteInteracaoOrientadorApresentacaoTCC as \"confTCC.permiteInteracaoOrientadorApresentacaoTCC\", ");
		sql.append("configuracaoTCC.exigePostagemArquivoFinalAvaliacaoTCC as \"confTCC.exigePostagemArquivoFinalAvaliacaoTCC\", ");
		sql.append("configuracaoTCC.permiteArquivoApresentacaoTCC as \"confTCC.permiteArquivoApresentacaoTCC\", ");
		sql.append("configuracaoTCC.permiteArquivoApoioApresentacaoTCC as \"confTCC.permiteArquivoApoioApresentacaoTCC\", ");
		sql.append("configuracaoTCC.valorPagamentoOrientadorApresentacaoTCC as \"confTCC.valorPagamentoOrientadorApresentacaoTCC\", ");
		sql.append("configuracaoTCC.valorPagamentoOrientadorPlanoTCC as \"confTCC.valorPagamentoOrientadorPlanoTCC\", ");
		sql.append("configuracaoTCC.valorPagamentoCoordenadorApresentacaoTCC as \"confTCC.valorPagamentoCoordenadorApresentacaoTCC\", ");
		sql.append("configuracaoTCC.controlarHistoricoPostagemArquivoApresentacaoTCC as \"confTCC.controlarHistoricoPostagemArquivoApresentacaoTCC\", ");
		sql.append("configuracaoTCC.descricao as \"confTCC.descricao\", ");
		sql.append("configuracaoTCC.tipoTCC as \"confTCC.tipoTCC\", ");
		sql.append("configuracaoTCC.orientacaoGeral as \"confTCC.orientacaoGeral\", ");
		sql.append("configuracaoTCC.orientacaoExtensaoPrazo as \"confTCC.orientacaoExtensaoPrazo\", ");
		sql.append("configuracaoTCC.numeroDiaAntesPrimeiraAulaLiberarAcessoTCC as \"confTCC.numeroDiaAntesPrimeiraAulaLiberarAcessoTCC\", ");
		sql.append("configuracaoTCC.apagarHistoricoPostagemArquivoAposFinalizacaoTCC as \"confTCC.apagarHistoricoPostagemArquivoAposFinalizacaoTCC\", ");
		sql.append("configuracaoTCC.dataBaseVencimentoPagamentoOrientacao as \"confTCC.dataBaseVencimentoPagamentoOrientacao\", ");
		sql.append("configuracaoTCC.dataBaseProcessamentoPagamentoOrientacao as \"confTCC.dataBaseProcessamentoPagamentoOrientacao\", ");
		sql.append("configuracaoTCC.controlaFrequencia as \"confTCC.controlaFrequencia\", ");
		sql.append("configuracaoTCC.numeroDiaAntesEncerramentoEtapaPrimeiroAviso as \"confTCC.numeroDiaAntesEncerramentoEtapaPrimeiroAviso\", ");
		sql.append("configuracaoTCC.numeroDiaAntesEncerramentoEtapaSegundoAviso as \"confTCC.numeroDiaAntesEncerramentoEtapaSegundoAviso\", ");
		sql.append("configuracaoTCC.numeroDiaDepoisEncerramentoEtapaReprovacaoAutomatica as \"confTCC.numeroDiaDepoisEncerramentoEtapaReprovacaoAutomatica\" ");

		sql.append(" FROM TrabalhoConclusaoCurso ");
		sql.append(" inner join pessoa aluno on aluno.codigo = TrabalhoConclusaoCurso.aluno ");
		sql.append(" inner join configuracaoTCC on configuracaoTCC.codigo = TrabalhoConclusaoCurso.configuracaoTCC ");
		sql.append(" left join arquivo fotoAluno on fotoAluno.codigo = aluno.arquivoImagem ");
		sql.append(" inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.codigo = TrabalhoConclusaoCurso.matriculaperiodoturmadisciplina ");
		sql.append(" inner join matricula on matriculaperiodoturmadisciplina.matricula = matricula.matricula ");
		sql.append(" inner join matriculaPeriodo on matriculaPeriodo.codigo = matriculaperiodoturmadisciplina.matriculaPeriodo ");
		sql.append(" inner join curso on curso.codigo = matricula.curso ");
		sql.append(" inner join turno on turno.codigo = matricula.turno ");
		sql.append(" inner join turma on turma.codigo = matriculaperiodoturmadisciplina.turma ");
		sql.append(" inner join disciplina on disciplina.codigo = matriculaperiodoturmadisciplina.disciplina ");
		sql.append(" inner join gradeDisciplina on gradeDisciplina.disciplina = disciplina.codigo and gradedisciplina.periodoletivo = matriculaperiodo.periodoletivomatricula");
		sql.append(" left join pessoa orientador on orientador.codigo = TrabalhoConclusaoCurso.orientador ");
		sql.append(" left join arquivo fotoOrientador on fotoOrientador.codigo = orientador.arquivoImagem ");
		sql.append(" left join pessoa orientadorSugerido on orientadorSugerido.codigo = TrabalhoConclusaoCurso.orientadorSugerido ");
		sql.append(" left join arquivo fotoOrientadorSugerido on fotoOrientadorSugerido.codigo = orientadorSugerido.arquivoImagem ");
		sql.append(" left join pessoa coordenador on coordenador.codigo = TrabalhoConclusaoCurso.coordenador ");

		return sql.toString();
	}

	@Override
	public TrabalhoConclusaoCursoVO consultarTrabalhoConclusaoCursoAtualAluno(String matricula) throws Exception {
		StringBuilder sql = new StringBuilder(getSelectCompleto());
		sql.append(" WHERE matricula.matricula = '").append(matricula).append("' ");
		sql.append(" order by TrabalhoConclusaoCurso.codigo desc limit 1 ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (rs.next()) {
			return montarDadosCompleto(rs);
		}
		return null;
	}
	
	public Integer consultarQtdeNovasInteracoesTCC(Integer tcc, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sql = new StringBuilder("select count(interacao.codigo) as total from trabalhoconclusaocursointeracao as interacao ")
			.append(" inner join trabalhoconclusaocurso as tcc on tcc.codigo = interacao.trabalhoconclusaocurso and tcc.codigo = ").append(tcc)
			.append(" where interacao.responsavelinteracao <> ").append(usuarioLogado.getCodigo())
			.append(" and (interacao.datainteracao > tcc.dataUltimoAcessoAluno or tcc.dataUltimoAcessoAluno is null)");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (rs.next()) {
			return rs.getInt("total");
		}
		return 0;
	}
	
	public Integer consultarQtdeNovasSituacoesAlteradasTCC(Integer tcc, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sql = new StringBuilder("select count(historico.codigo) as total from historicosituacaotcc as historico ")
			.append(" inner join trabalhoconclusaocurso as tcc on tcc.codigo = historico.trabalhoconclusaocurso and tcc.codigo = ").append(tcc)
			.append(" where historico.usuario <> ").append(usuarioLogado.getCodigo())
			.append(" and (historico.datasituacao > tcc.dataUltimoAcessoAluno or tcc.dataUltimoAcessoAluno is null)");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
	if (rs.next()) {
		return rs.getInt("total");
	}
	return 0;
	}

	private TrabalhoConclusaoCursoVO montarDadosCompleto(SqlRowSet rs) {
		TrabalhoConclusaoCursoVO obj = new TrabalhoConclusaoCursoVO();
		obj.setNovoObj(false);
		obj.setCodigo(rs.getInt("codigo"));
		obj.getMatriculaPeriodoTurmaDisciplina().setCodigo(rs.getInt("matriculaPeriodoTurmaDisciplina"));
		obj.getAluno().setCodigo(rs.getInt("aluno"));
		obj.setTitulo(rs.getString("titulo"));
		obj.getConfiguracaoTCC().setCodigo(rs.getInt("configuracaoTCC"));
		obj.getOrientador().setCodigo(rs.getInt("orientador"));
		obj.getCoordenador().setCodigo(rs.getInt("coordenador"));
		obj.getOrientadorSugerido().setCodigo(rs.getInt("orientadorSugerido"));
		obj.setEtapaTCC(EtapaTCCEnum.valueOf(rs.getString("etapaTCC")));
		obj.setTipoTCC(TipoTCCEnum.valueOf(rs.getString("tipoTCC")));
		obj.setSituacaoTCC(SituacaoTCCEnum.valueOf(rs.getString("situacaoTCC")));
		obj.setDataAlteracaoSituacao(rs.getDate("dataAlteracaoSituacao"));
		obj.setDataInicioAvaliacaoTCC(rs.getDate("dataInicioAvaliacaoTCC"));
		obj.setDataInicioElaboracaoTCC(rs.getDate("dataInicioElaboracaoTCC"));
		obj.setDataInicioPlanoTCC(rs.getDate("dataInicioPlanoTCC"));
		obj.setDataTerminoAvaliacaoTCC(rs.getDate("dataTerminoAvaliacaoTCC"));
		obj.setDataTerminoElaboracaoTCC(rs.getDate("dataTerminoElaboracaoTCC"));
		obj.setDataTerminoPlanoTCC(rs.getDate("dataTerminoPlanoTCC"));
		obj.setTema(rs.getString("tema"));
		obj.setObjetivoEspecifico(rs.getString("objetivoEspecifico"));
		obj.setObjetivoGeral(rs.getString("objetivoGeral"));
		obj.setMetodologia(rs.getString("metodologia"));
		obj.setReferenciaBibliografica(rs.getString("referenciaBibliografica"));
		obj.setProblema(rs.getString("problema"));
		obj.setDataUltimoAcessoAluno(rs.getTimestamp("dataUltimoAcessoAluno"));
		obj.setDataDefinicaoBanca(rs.getTimestamp("dataDefinicaoBanca"));
		obj.setObservacaoBanca(rs.getString("observacaoBanca"));
		obj.setMedia(rs.getDouble("media"));
		obj.setMediaConteudo(rs.getDouble("mediaConteudo"));
		obj.setMediaFormatacao(rs.getDouble("mediaFormatacao"));
		
		obj.getAluno().setNome(rs.getString("aluno.nome"));
		obj.getAluno().setEmail(rs.getString("aluno.email"));
		if (rs.getInt("fotoAluno.codigo") > 0) {
			obj.getAluno().getArquivoImagem().setCodigo(rs.getInt("fotoAluno.codigo"));
			obj.getAluno().getArquivoImagem().setPastaBaseArquivo(rs.getString("fotoAluno.pastaBaseArquivo").replaceAll("\\\\", "/"));
			obj.getAluno().getArquivoImagem().setNome(rs.getString("fotoAluno.nome"));
		}
		obj.getOrientador().setNome(rs.getString("orientador.nome"));
		obj.getOrientador().setEmail(rs.getString("orientador.email"));
		if (rs.getInt("fotoOrientador.codigo") > 0) {
			obj.getOrientador().getArquivoImagem().setCodigo(rs.getInt("fotoOrientador.codigo"));
			obj.getOrientador().getArquivoImagem().setPastaBaseArquivo(rs.getString("fotoOrientador.pastaBaseArquivo").replaceAll("\\\\", "/"));
			obj.getOrientador().getArquivoImagem().setNome(rs.getString("fotoOrientador.nome"));
		}
		obj.getOrientadorSugerido().setNome(rs.getString("orientadorSugerido.nome"));
		obj.getOrientadorSugerido().setEmail(rs.getString("orientadorSugerido.email"));
		if (rs.getInt("fotoOrientadorSugerido.codigo") > 0) {
			obj.getOrientador().getArquivoImagem().setCodigo(rs.getInt("fotoOrientadorSugerido.codigo"));
			obj.getOrientador().getArquivoImagem().setPastaBaseArquivo(rs.getString("fotoOrientadorSugerido.pastaBaseArquivo").replaceAll("\\\\", "/"));
			obj.getOrientador().getArquivoImagem().setNome(rs.getString("fotoOrientadorSugerido.nome"));
		}
		obj.getCoordenador().setNome(rs.getString("coordenador.nome"));
		obj.getMatriculaPeriodoTurmaDisciplina().getDisciplina().setNome(rs.getString("disciplina.nome"));
		obj.getMatriculaPeriodoTurmaDisciplina().setAno(rs.getString("matriculaPeriodoTurmaDisciplina.ano"));
		obj.getMatriculaPeriodoTurmaDisciplina().setSemestre(rs.getString("matriculaPeriodoTurmaDisciplina.semestre"));
		obj.getMatriculaPeriodoTurmaDisciplina().getDisciplina().setCodigo(rs.getInt("disciplina.codigo"));
		obj.getMatriculaPeriodoTurmaDisciplina().getGradeDisciplinaVO().getConfiguracaoAcademico().setCodigo(rs.getInt("gradeDisciplina.configuracaoAcademico"));
		obj.getMatriculaPeriodoTurmaDisciplina().setMatricula(rs.getString("matricula.matricula"));
		obj.getMatriculaPeriodoTurmaDisciplina().getMatriculaObjetoVO().setSituacao(rs.getString("matricula.situacao"));
		obj.getMatriculaPeriodoTurmaDisciplina().getMatriculaObjetoVO().setMatricula(rs.getString("matricula.matricula"));
		obj.getMatriculaPeriodoTurmaDisciplina().getMatriculaObjetoVO().getCurso().setCodigo(rs.getInt("curso.codigo"));
		obj.getMatriculaPeriodoTurmaDisciplina().getMatriculaObjetoVO().getCurso().setNome(rs.getString("curso.nome"));
		obj.getMatriculaPeriodoTurmaDisciplina().getMatriculaObjetoVO().getCurso().getConfiguracaoAcademico().setCodigo(rs.getInt("curso.configuracaoAcademico"));
		obj.getMatriculaPeriodoTurmaDisciplina().getMatriculaObjetoVO().getTurno().setCodigo(rs.getInt("turno.codigo"));
		obj.getMatriculaPeriodoTurmaDisciplina().getMatriculaObjetoVO().getTurno().setNome(rs.getString("turno.nome"));
		obj.getMatriculaPeriodoTurmaDisciplina().getTurma().setIdentificadorTurma(rs.getString("turma.identificadorTurma"));
		obj.getMatriculaPeriodoTurmaDisciplina().getTurma().setCodigo(rs.getInt("turma.codigo"));
		
		obj.getConfiguracaoTCC().setNovoObj(false);
		obj.getConfiguracaoTCC().setApagarHistoricoPostagemArquivoAposFinalizacaoTCC(rs.getBoolean("confTCC.apagarHistoricoPostagemArquivoAposFinalizacaoTCC"));
		obj.getConfiguracaoTCC().setCodigo(rs.getInt("confTCC.codigo"));
		obj.getConfiguracaoTCC().setControlaElaboracaoTCC(rs.getBoolean("confTCC.controlaElaboracaoTCC"));
		obj.getConfiguracaoTCC().setControlaPlanoTCC(rs.getBoolean("confTCC.controlaPlanoTCC"));
		obj.getConfiguracaoTCC().setControlarHistoricoPostagemArquivoApresentacaoTCC(rs.getBoolean("confTCC.controlarHistoricoPostagemArquivoApresentacaoTCC"));
		obj.getConfiguracaoTCC().setControlarHistoricoPostagemArquivoElaboracaoTCC(rs.getBoolean("confTCC.controlarHistoricoPostagemArquivoElaboracaoTCC"));
		obj.getConfiguracaoTCC().setDataBaseProcessamentoPagamentoOrientacao(rs.getInt("confTCC.dataBaseProcessamentoPagamentoOrientacao"));
		obj.getConfiguracaoTCC().setDataBaseVencimentoPagamentoOrientacao(rs.getInt("confTCC.dataBaseVencimentoPagamentoOrientacao"));
		obj.getConfiguracaoTCC().setDescricao(rs.getString("confTCC.descricao"));
		obj.getConfiguracaoTCC().setExigePostagemArquivoFinalAvaliacaoTCC(rs.getBoolean("confTCC.exigePostagemArquivoFinalAvaliacaoTCC"));
		obj.getConfiguracaoTCC().setNumeroDiaAntesPrimeiraAulaLiberarAcessoTCC(rs.getInt("confTCC.numeroDiaAntesPrimeiraAulaLiberarAcessoTCC"));
		obj.getConfiguracaoTCC().setOrientacaoExtensaoPrazo(rs.getString("confTCC.orientacaoExtensaoPrazo"));
		obj.getConfiguracaoTCC().setOrientacaoGeral(rs.getString("confTCC.orientacaoGeral"));
		obj.getConfiguracaoTCC().setPermiteArquivoApoioApresentacaoTCC(rs.getBoolean("confTCC.permiteArquivoApoioApresentacaoTCC"));
		obj.getConfiguracaoTCC().setPermiteArquivoApoioElaboracaoTCC(rs.getBoolean("confTCC.permiteArquivoApoioElaboracaoTCC"));
		obj.getConfiguracaoTCC().setPermiteArquivoApresentacaoTCC(rs.getBoolean("confTCC.permiteArquivoApresentacaoTCC"));
		obj.getConfiguracaoTCC().setPermiteInteracaoOrientadorApresentacaoTCC(rs.getBoolean("confTCC.permiteInteracaoOrientadorApresentacaoTCC"));
		obj.getConfiguracaoTCC().setPermiteInteracaoOrientadorElaboracaoTCC(rs.getBoolean("confTCC.permiteInteracaoOrientadorElaboracaoTCC"));
		obj.getConfiguracaoTCC().setPermiteInteracaoOrientadorPlanoTCC(rs.getBoolean("confTCC.permiteInteracaoOrientadorPlanoTCC"));
		obj.getConfiguracaoTCC().setPermiteSolicitarOrientador(rs.getBoolean("confTCC.permiteSolicitarOrientador"));
		obj.getConfiguracaoTCC().setPrazoExecucaoApresentacaoTCC(rs.getInt("confTCC.prazoExecucaoApresentacaoTCC"));
		obj.getConfiguracaoTCC().setPrazoExecucaoElaboracaoTCC(rs.getInt("confTCC.prazoExecucaoElaboracaoTCC"));
		obj.getConfiguracaoTCC().setPrazoExecucaoPlanoTCC(rs.getInt("confTCC.prazoExecucaoPlanoTCC"));
		obj.getConfiguracaoTCC().setPrazoRespostaOrientadorElaboracaoTCC(rs.getInt("confTCC.prazoRespostaOrientadorElaboracaoTCC"));
		obj.getConfiguracaoTCC().setPrazoRespostaOrientadorPlanoTCC(rs.getInt("confTCC.prazoRespostaOrientadorPlanoTCC"));
		obj.getConfiguracaoTCC().setValorPagamentoCoordenadorApresentacaoTCC(rs.getDouble("confTCC.valorPagamentoCoordenadorApresentacaoTCC"));
		obj.getConfiguracaoTCC().setValorPagamentoCoordenadorElaboracaoTCC(rs.getDouble("confTCC.valorPagamentoCoordenadorElaboracaoTCC"));
		obj.getConfiguracaoTCC().setValorPagamentoCoordenadorPlanoTCC(rs.getDouble("confTCC.valorPagamentoCoordenadorPlanoTCC"));
		obj.getConfiguracaoTCC().setValorPagamentoOrientadorApresentacaoTCC(rs.getDouble("confTCC.valorPagamentoOrientadorApresentacaoTCC"));
		obj.getConfiguracaoTCC().setValorPagamentoOrientadorElaboracaoTCC(rs.getDouble("confTCC.valorPagamentoOrientadorElaboracaoTCC"));
		obj.getConfiguracaoTCC().setValorPagamentoOrientadorPlanoTCC(rs.getDouble("confTCC.valorPagamentoOrientadorPlanoTCC"));
		obj.getConfiguracaoTCC().setTipoTCC(TipoTCCEnum.valueOf(rs.getString("confTCC.tipoTCC")));
		obj.getConfiguracaoTCC().setControlaFrequencia(rs.getBoolean("confTCC.controlaFrequencia"));
		obj.getConfiguracaoTCC().setNumeroDiaAntesEncerramentoEtapaPrimeiroAviso(rs.getInt("confTCC.numeroDiaAntesEncerramentoEtapaPrimeiroAviso"));
		obj.getConfiguracaoTCC().setNumeroDiaAntesEncerramentoEtapaSegundoAviso(rs.getInt("confTCC.numeroDiaAntesEncerramentoEtapaSegundoAviso"));
		obj.getConfiguracaoTCC().setNumeroDiaDepoisEncerramentoEtapaReprovacaoAutomatica(rs.getInt("confTCC.numeroDiaDepoisEncerramentoEtapaReprovacaoAutomatica"));

//		obj.getConfiguracaoTCC().setOcultarOrientadoVisaoAluno(rs.getBoolean("confTCC.ocultarOrientadoVisaoAluno"));
//		obj.getConfiguracaoTCC().setNomenclaturaUtilizarParaAvaliador(rs.getBoolean("confTCC.nomenclaturaUtilizarParaAvaliador"));
//		obj.getConfiguracaoTCC().setNomenclaturaUtilizarParaComissao(rs.getBoolean("confTCC.nomenclaturaUtilizarParaComissao"));
		return obj;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Integer consultarTotalAlunosTCCGradeDisciplina(GradeDisciplinaVO gradeDisciplina, UsuarioVO usuarioLogado) {
		try {
			return getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarTotalPorGradeDisciplina(gradeDisciplina, usuarioLogado);
		} catch (Exception e) {
			return 0;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void realizarCriacaoTrabalhoConclusaoCursoGradeDisciplina(GradeDisciplinaVO gradeDisciplina, UsuarioVO usuarioLogado) throws Exception {

		getFacadeFactory().getGradeDisciplinaFacade().executarDefinirDisciplinaUtilizaTCC(gradeDisciplina.getCodigo(), usuarioLogado);
		List<MatriculaPeriodoTurmaDisciplinaTCCVO> lista = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarPorGradeDisciplina(gradeDisciplina, usuarioLogado);
		for (MatriculaPeriodoTurmaDisciplinaTCCVO mptdtcc : lista) {
			realizarCriacaoTrabalhoConclusaoCursoAluno(mptdtcc.getCurso(), mptdtcc.getAluno(), mptdtcc.getGradeCurricular(), 
					getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarPorChavePrimaria(mptdtcc.getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioLogado), 
					usuarioLogado);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void realizarCriacaoTrabalhoConclusaoCursoAluno(Integer curso, Integer aluno, Integer gradeCurricular, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, UsuarioVO usuarioVO) throws Exception {
		if (getFacadeFactory().getGradeDisciplinaFacade().consultarDisciplinaAplicaTCC(gradeCurricular, matriculaPeriodoTurmaDisciplinaVO.getDisciplina().getCodigo()) 
				&& consultarPorMatriculaPeridoTurmaDisciplina(matriculaPeriodoTurmaDisciplinaVO.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO) == null) {

			TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO = new TrabalhoConclusaoCursoVO();
			trabalhoConclusaoCursoVO.setConfiguracaoTCC(getFacadeFactory().getConfiguracaoTCCFacade().consultarPorCurso(curso, Uteis.NIVELMONTARDADOS_TODOS));
			if (trabalhoConclusaoCursoVO.getConfiguracaoTCC() != null && trabalhoConclusaoCursoVO.getConfiguracaoTCC().getCodigo() != null && trabalhoConclusaoCursoVO.getConfiguracaoTCC().getCodigo() > 0) {
				if (trabalhoConclusaoCursoVO.getConfiguracaoTCC().getTipoTCC().equals(TipoTCCEnum.AMBOS)) {
					trabalhoConclusaoCursoVO.setTipoTCC(null);
				} else {
					trabalhoConclusaoCursoVO.setTipoTCC(trabalhoConclusaoCursoVO.getConfiguracaoTCC().getTipoTCC());
				}
				if (trabalhoConclusaoCursoVO.getConfiguracaoTCC().getControlaPlanoTCC()) {
					trabalhoConclusaoCursoVO.setEtapaTCC(EtapaTCCEnum.PLANO_TCC);
					trabalhoConclusaoCursoVO.setSituacaoTCC(SituacaoTCCEnum.NOVO);
				} else if (trabalhoConclusaoCursoVO.getConfiguracaoTCC().getControlaElaboracaoTCC()) {
					trabalhoConclusaoCursoVO.setEtapaTCC(EtapaTCCEnum.ELABORACAO_TCC);
					trabalhoConclusaoCursoVO.setSituacaoTCC(SituacaoTCCEnum.AGUARDANDO_ARQUIVO);
				} else {
					trabalhoConclusaoCursoVO.setEtapaTCC(EtapaTCCEnum.AVALIACAO_TCC);
					if (!trabalhoConclusaoCursoVO.getConfiguracaoTCC().getControlaElaboracaoTCC() && !trabalhoConclusaoCursoVO.getConfiguracaoTCC().getControlaPlanoTCC()) {
						trabalhoConclusaoCursoVO.setSituacaoTCC(SituacaoTCCEnum.AGUARDANDO_ARQUIVO);
					} else {
						if (trabalhoConclusaoCursoVO.getConfiguracaoTCC().getExigePostagemArquivoFinalAvaliacaoTCC()) {
							trabalhoConclusaoCursoVO.setSituacaoTCC(SituacaoTCCEnum.AGUARDANDO_ARQUIVO);
						} else {
							trabalhoConclusaoCursoVO.setSituacaoTCC(SituacaoTCCEnum.AGUARDANDO_AVALIACAO_BANCA);
						}
					}
				}
				trabalhoConclusaoCursoVO.getAluno().setCodigo(aluno);
				trabalhoConclusaoCursoVO.setDataAlteracaoSituacao(new Date());
				trabalhoConclusaoCursoVO.setMatriculaPeriodoTurmaDisciplina(matriculaPeriodoTurmaDisciplinaVO);
				trabalhoConclusaoCursoVO.setOrientador(trabalhoConclusaoCursoVO.getConfiguracaoTCC().getOrientadorPadrao());
				realizarVerificacaoDataPrimeiraAulaLiberarTCC(trabalhoConclusaoCursoVO, usuarioVO, false);
				if (!trabalhoConclusaoCursoVO.getConfiguracaoTCC().getUtilizarProfMinistCoordeConfTurma()) {
					trabalhoConclusaoCursoVO.getCoordenador().setCodigo(getFacadeFactory().getUnidadeEnsinoFacade().consultarCodigoPessoaCoordenadorTCC(matriculaPeriodoTurmaDisciplinaVO.getTurma().getUnidadeEnsino().getCodigo(), curso));
				} else {
					List<CursoCoordenadorVO> lista = getFacadeFactory().getCursoCoordenadorFacade().consultarPorCodigoMatriculaPeriodoTurmaDisciplina(trabalhoConclusaoCursoVO.getMatriculaPeriodoTurmaDisciplina().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
					if (!lista.isEmpty()) {
						CursoCoordenadorVO c = (CursoCoordenadorVO)lista.get(0);
						trabalhoConclusaoCursoVO.getCoordenador().setCodigo(c.getFuncionario().getPessoa().getCodigo());
					}					
				}
				if (trabalhoConclusaoCursoVO.getDataInicioPlanoTCC() != null ||
					trabalhoConclusaoCursoVO.getDataInicioElaboracaoTCC() != null || trabalhoConclusaoCursoVO.getDataInicioAvaliacaoTCC() != null) {
					if (!trabalhoConclusaoCursoVO.getConfiguracaoTCC().getQuestaoConteudoVOs().isEmpty()) {
						Iterator i = trabalhoConclusaoCursoVO.getConfiguracaoTCC().getQuestaoConteudoVOs().iterator();
						while (i.hasNext()) {
							QuestaoTCCVO qtccVO = (QuestaoTCCVO)i.next();
							QuestaoTrabalhoConclusaoCursoVO q = new QuestaoTrabalhoConclusaoCursoVO();
							q.setEnunciado(qtccVO.getEnunciado());
							q.setOrigemQuestao(qtccVO.getOrigemQuestao());
							q.setValorMaximoNotaConteudo(qtccVO.getValorNotaMaximo());
							trabalhoConclusaoCursoVO.getQuestaoConteudoVOs().add(q);
						}
					}
					if (!trabalhoConclusaoCursoVO.getConfiguracaoTCC().getQuestaoFormatacaoVOs().isEmpty()) {
						Iterator i = trabalhoConclusaoCursoVO.getConfiguracaoTCC().getQuestaoFormatacaoVOs().iterator();
						while (i.hasNext()) {
							QuestaoTCCVO qtccVO = (QuestaoTCCVO)i.next();
							QuestaoTrabalhoConclusaoCursoVO q = new QuestaoTrabalhoConclusaoCursoVO();
							q.setEnunciado(qtccVO.getEnunciado());
							q.setOrigemQuestao(qtccVO.getOrigemQuestao());
							q.setValorMaximoNotaFormatacao(qtccVO.getValorNotaMaximo());
							trabalhoConclusaoCursoVO.getQuestaoFormatacaoVOs().add(q);
						}
					}
					persitir(trabalhoConclusaoCursoVO, usuarioVO);
					getFacadeFactory().getHistoricoSituacaoTCCFacade().realizarCriacaoHistoricoSituacaoTCC(trabalhoConclusaoCursoVO, "Funcionário", usuarioVO);
					for (ConfiguracaoTCCArtefatoVO configuracaoTCCArtefatoVO : trabalhoConclusaoCursoVO.getConfiguracaoTCC().getConfiguracaoTCCArtefatoVOs()) {
						TrabalhoConclusaoCursoArtefatoVO trabalhoConclusaoCursoArtefatoVO = new TrabalhoConclusaoCursoArtefatoVO();
						trabalhoConclusaoCursoArtefatoVO.setConfiguracaoTCCArtefato(configuracaoTCCArtefatoVO);
						trabalhoConclusaoCursoArtefatoVO.setEntregue(false);
						trabalhoConclusaoCursoArtefatoVO.setTrabalhoConclusaoCurso(trabalhoConclusaoCursoVO);
						getFacadeFactory().getTrabalhoConclusaoCursoArtefatoFacade().persistir(trabalhoConclusaoCursoArtefatoVO);
					}
				}
			}
		}

	}

	public void validarTrabalhoConclusaoCursoIniciado(String matricula) throws Exception {
		StringBuilder sql = new StringBuilder(" select trabalhoconclusaocurso.codigo from trabalhoconclusaocurso ");
		sql.append(" inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.codigo = trabalhoconclusaocurso.matriculaperiodoturmadisciplina  ");
		sql.append(" where matriculaperiodoturmadisciplina.matricula = '").append(matricula).append("' ");
		sql.append(" and trabalhoconclusaocurso.situacaotcc in ('AGUARDANDO_APROVACAO_ORIENTADOR', 'AGUARDANDO_AVALIACAO_BANCA', 'APROVADO') ");			
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (rs.next()) {			
			throw new Exception(UteisJSF.internacionalizar("msg_TrabalhoConclusaoCurso_bloqueioTrabalhoConclusaoCurso"));
		}
	}
	
	public void realizarVerificacaoDataPrimeiraAulaLiberarTCC(TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO, UsuarioVO usuarioVO, boolean persistirAlteracao) throws Exception {
		if (trabalhoConclusaoCursoVO.getDataInicioPlanoTCC() == null 
				&& trabalhoConclusaoCursoVO.getDataInicioElaboracaoTCC() == null 
				&& trabalhoConclusaoCursoVO.getDataInicioAvaliacaoTCC() == null) {
			Date dataBaseInicio = null;
			if (trabalhoConclusaoCursoVO.getConfiguracaoTCC().getPoliticaApresentarTCCAluno().equals("UL")) {
				dataBaseInicio = getFacadeFactory().getHorarioTurmaFacade().consultarUltimaDataAulaPorMatriculaConsiderandoReposicao(trabalhoConclusaoCursoVO.getMatriculaPeriodoTurmaDisciplina().getCodigo());
			} else {
				dataBaseInicio = getFacadeFactory().getHorarioTurmaFacade().consultarPrimeiroDiaAulaTurmaDisciplina(trabalhoConclusaoCursoVO.getMatriculaPeriodoTurmaDisciplina().getTurma().getCodigo(), 
								trabalhoConclusaoCursoVO.getMatriculaPeriodoTurmaDisciplina().getDisciplina().getCodigo(), 
								trabalhoConclusaoCursoVO.getMatriculaPeriodoTurmaDisciplina().getAno(), 
								trabalhoConclusaoCursoVO.getMatriculaPeriodoTurmaDisciplina().getSemestre());
			}
			if (dataBaseInicio != null) {
				if (trabalhoConclusaoCursoVO.getEtapaTCC().equals(EtapaTCCEnum.PLANO_TCC)) {
					trabalhoConclusaoCursoVO.setDataInicioPlanoTCC(dataBaseInicio);
				} else if (trabalhoConclusaoCursoVO.getEtapaTCC().equals(EtapaTCCEnum.ELABORACAO_TCC)) {
					trabalhoConclusaoCursoVO.setDataInicioElaboracaoTCC(dataBaseInicio);
				} else if (trabalhoConclusaoCursoVO.getEtapaTCC().equals(EtapaTCCEnum.AVALIACAO_TCC)) {
					trabalhoConclusaoCursoVO.setDataInicioAvaliacaoTCC(dataBaseInicio);
				}
				realizarCalculoPrevisaoTerminoEtapas(trabalhoConclusaoCursoVO);
				if (persistirAlteracao) {
					persitir(trabalhoConclusaoCursoVO, usuarioVO);
				}
			}
		}
	}

	@Override
	public Boolean realizarVerificacaoTrabalhoConclusaoCursoAptoApresentar(TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO, UsuarioVO usuarioVO) throws Exception {
		
		Date dataBaseInicio = Uteis.getDataJDBC(new Date());
		if (trabalhoConclusaoCursoVO != null) {
			realizarVerificacaoDataPrimeiraAulaLiberarTCC(trabalhoConclusaoCursoVO, usuarioVO, true);
			return (
					(trabalhoConclusaoCursoVO.getDataInicioPlanoTCC() != null && 
					 Uteis.getDataJDBC(Uteis.obterDataAvancada(trabalhoConclusaoCursoVO.getDataInicioPlanoTCC(), trabalhoConclusaoCursoVO.getConfiguracaoTCC().getNumeroDiaAntesPrimeiraAulaLiberarAcessoTCC()*-1)).compareTo(dataBaseInicio) <= 0) 
				|| 
					(trabalhoConclusaoCursoVO.getDataInicioElaboracaoTCC() != null && 
					 Uteis.getDataJDBC(Uteis.obterDataAvancada(trabalhoConclusaoCursoVO.getDataInicioElaboracaoTCC(), trabalhoConclusaoCursoVO.getConfiguracaoTCC().getNumeroDiaAntesPrimeiraAulaLiberarAcessoTCC()*-1)).compareTo(dataBaseInicio) <= 0) 
				|| 
					(trabalhoConclusaoCursoVO.getDataInicioAvaliacaoTCC() != null && Uteis.getDataJDBC(Uteis.obterDataAvancada(trabalhoConclusaoCursoVO.getDataInicioAvaliacaoTCC(), trabalhoConclusaoCursoVO.getConfiguracaoTCC().getNumeroDiaAntesPrimeiraAulaLiberarAcessoTCC()*-1)).compareTo(dataBaseInicio) <= 0)
				);
		}
		return false;
	}
	
	public Boolean realizarVerificacaoTrabalhoConclusaoCursoVinculadoProfessor(Integer codPessoa) throws Exception {		
		StringBuilder sql = new StringBuilder();
		sql.append(" select trabalhoconclusaocurso.codigo from trabalhoconclusaocurso where trabalhoconclusaocurso.orientador = ").append(codPessoa);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (rs.next()) {
			return true;
		}		
		return false;
	}	

	@Override
	public Boolean realizarVerificacaoPrimeiroAcessoAluno(TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO, UsuarioVO usuarioVO) throws Exception {

		return trabalhoConclusaoCursoVO.getIsPrimeiroAcessoAluno();
	}
	
	@Override
	public Integer realizarVerificacaoQtdeNovidadesTCC(TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO, UsuarioVO usuarioLogado) throws Exception {
		
		return consultarQtdeNovasInteracoesTCC(trabalhoConclusaoCursoVO.getCodigo(), usuarioLogado) + 
				consultarQtdeNovasSituacoesAlteradasTCC(trabalhoConclusaoCursoVO.getCodigo(), usuarioLogado);
	}

	@Override
	public TrabalhoConclusaoCursoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder(getSelectCompleto());
		sql.append(" WHERE TrabalhoConclusaoCurso.codigo = ").append(codigo);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (rs.next()) {
			TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO = montarDadosCompleto(rs);
			if (nivelMontarDados == Uteis.NIVELMONTARDADOS_TODOS) {
				carregarDadosSubordinados(trabalhoConclusaoCursoVO, usuarioVO);
			}
			return trabalhoConclusaoCursoVO;
		}
		return null;
	}

	@Override
	public TrabalhoConclusaoCursoVO consultarPorMatriculaPeridoTurmaDisciplina(Integer matriculaPeridoTurmaDisciplina, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder(getSelectCompleto());
		sql.append(" WHERE TrabalhoConclusaoCurso.matriculaperiodoturmadisciplina = ").append(matriculaPeridoTurmaDisciplina);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (rs.next()) {
			TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO = montarDadosCompleto(rs);
			if (nivelMontarDados == Uteis.NIVELMONTARDADOS_TODOS) {
				carregarDadosSubordinados(trabalhoConclusaoCursoVO, usuarioVO);
			}
			return trabalhoConclusaoCursoVO;
		}
		return null;
	}

	@Override
	public void carregarDadosSubordinados(TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO, UsuarioVO usuario) throws Exception {
		trabalhoConclusaoCursoVO.setTrabalhoConclusaoCursoMembroBancaVOs(getFacadeFactory().getTrabalhoConclusaoCursoMembroBancaFacade().consultarPorTCC(trabalhoConclusaoCursoVO.getCodigo()));
		trabalhoConclusaoCursoVO.getHistoricoSituacaoTCCVOs().setListaConsulta(getFacadeFactory().getHistoricoSituacaoTCCFacade().consultarPorTCC(trabalhoConclusaoCursoVO.getCodigo(), 5, 0));
		trabalhoConclusaoCursoVO.getHistoricoSituacaoTCCVOs().setTotalRegistrosEncontrados(getFacadeFactory().getHistoricoSituacaoTCCFacade().consultarTotalRegistroPorTCC(trabalhoConclusaoCursoVO.getCodigo()));
		if (trabalhoConclusaoCursoVO.getEtapaTCC().equals(EtapaTCCEnum.AVALIACAO_TCC)) {
			trabalhoConclusaoCursoVO.getTrabalhoConclusaoCursoArquivoEtapaAvaliacaoVOs().setPage(1);
			trabalhoConclusaoCursoVO.getTrabalhoConclusaoCursoArquivoEtapaAvaliacaoVOs().setPaginaAtual(1);
			trabalhoConclusaoCursoVO.getTrabalhoConclusaoCursoArquivoEtapaAvaliacaoVOs().setListaConsulta(getFacadeFactory().getTrabalhoConclusaoCursoArquivoFacade().consultarPorTCCEtapa(trabalhoConclusaoCursoVO.getCodigo(), EtapaTCCEnum.AVALIACAO_TCC, null, 5, 0));
			trabalhoConclusaoCursoVO.getTrabalhoConclusaoCursoArquivoEtapaAvaliacaoVOs().setTotalRegistrosEncontrados(getFacadeFactory().getTrabalhoConclusaoCursoArquivoFacade().consultarTotalRegistroPorTCCEtapa(trabalhoConclusaoCursoVO.getCodigo(), EtapaTCCEnum.AVALIACAO_TCC, null));

			trabalhoConclusaoCursoVO.getTrabalhoConclusaoCursoInteracaoAvaliacaoTCCVOs().setPage(1);
			trabalhoConclusaoCursoVO.getTrabalhoConclusaoCursoInteracaoAvaliacaoTCCVOs().setPaginaAtual(1);
			trabalhoConclusaoCursoVO.getTrabalhoConclusaoCursoInteracaoAvaliacaoTCCVOs().setListaConsulta(getFacadeFactory().getTrabalhoConclusaoCursoInteracaoFacade().consultarPorTCCEtapa(trabalhoConclusaoCursoVO.getCodigo(), EtapaTCCEnum.AVALIACAO_TCC, 5, 0));
			trabalhoConclusaoCursoVO.getTrabalhoConclusaoCursoInteracaoAvaliacaoTCCVOs().setTotalRegistrosEncontrados(getFacadeFactory().getTrabalhoConclusaoCursoInteracaoFacade().consultarTotalRegistroPorTCCEtapa(trabalhoConclusaoCursoVO.getCodigo(), EtapaTCCEnum.AVALIACAO_TCC));

			trabalhoConclusaoCursoVO.setTrabalhoConclusaoCursoArtefatoVOs(getFacadeFactory().getTrabalhoConclusaoCursoArtefatoFacade().consultarPorTCC(trabalhoConclusaoCursoVO.getCodigo()));
			trabalhoConclusaoCursoVO.setHistorico(getFacadeFactory().getHistoricoFacade().consultarHistoricoPorMatriculaPeriodoTurmaDisciplina(trabalhoConclusaoCursoVO.getMatriculaPeriodoTurmaDisciplina().getCodigo(), new ConfiguracaoFinanceiroVO(), usuario));
		}
		if (trabalhoConclusaoCursoVO.getEtapaTCC().equals(EtapaTCCEnum.AVALIACAO_TCC) || trabalhoConclusaoCursoVO.getEtapaTCC().equals(EtapaTCCEnum.ELABORACAO_TCC)) {
			trabalhoConclusaoCursoVO.getTrabalhoConclusaoCursoArquivoEtapaElaboracaoVOs().setPage(1);
			trabalhoConclusaoCursoVO.getTrabalhoConclusaoCursoArquivoEtapaElaboracaoVOs().setPaginaAtual(1);
			trabalhoConclusaoCursoVO.getTrabalhoConclusaoCursoArquivoEtapaElaboracaoVOs().setListaConsulta(getFacadeFactory().getTrabalhoConclusaoCursoArquivoFacade().consultarPorTCCEtapa(trabalhoConclusaoCursoVO.getCodigo(), EtapaTCCEnum.ELABORACAO_TCC, null, 5, 0));
			trabalhoConclusaoCursoVO.getTrabalhoConclusaoCursoArquivoEtapaElaboracaoVOs().setTotalRegistrosEncontrados(getFacadeFactory().getTrabalhoConclusaoCursoArquivoFacade().consultarTotalRegistroPorTCCEtapa(trabalhoConclusaoCursoVO.getCodigo(), EtapaTCCEnum.ELABORACAO_TCC, null));
			trabalhoConclusaoCursoVO.getTrabalhoConclusaoCursoInteracaoElaboracaoTCCVOs().setPage(1);
			trabalhoConclusaoCursoVO.getTrabalhoConclusaoCursoInteracaoElaboracaoTCCVOs().setPaginaAtual(1);
			trabalhoConclusaoCursoVO.getTrabalhoConclusaoCursoInteracaoElaboracaoTCCVOs().setListaConsulta(getFacadeFactory().getTrabalhoConclusaoCursoInteracaoFacade().consultarPorTCCEtapa(trabalhoConclusaoCursoVO.getCodigo(), EtapaTCCEnum.ELABORACAO_TCC, 5, 0));
			trabalhoConclusaoCursoVO.getTrabalhoConclusaoCursoInteracaoElaboracaoTCCVOs().setTotalRegistrosEncontrados(getFacadeFactory().getTrabalhoConclusaoCursoInteracaoFacade().consultarTotalRegistroPorTCCEtapa(trabalhoConclusaoCursoVO.getCodigo(), EtapaTCCEnum.ELABORACAO_TCC));
		}
		if (trabalhoConclusaoCursoVO.getConfiguracaoTCC().getControlaPlanoTCC()) {
			trabalhoConclusaoCursoVO.getTrabalhoConclusaoCursoInteracaoPlanoTCCVOs().setPage(1);
			trabalhoConclusaoCursoVO.getTrabalhoConclusaoCursoInteracaoPlanoTCCVOs().setPaginaAtual(1);
			trabalhoConclusaoCursoVO.getTrabalhoConclusaoCursoInteracaoPlanoTCCVOs().setListaConsulta(getFacadeFactory().getTrabalhoConclusaoCursoInteracaoFacade().consultarPorTCCEtapa(trabalhoConclusaoCursoVO.getCodigo(), EtapaTCCEnum.PLANO_TCC, 5, 0));
			trabalhoConclusaoCursoVO.getTrabalhoConclusaoCursoInteracaoPlanoTCCVOs().setTotalRegistrosEncontrados(getFacadeFactory().getTrabalhoConclusaoCursoInteracaoFacade().consultarTotalRegistroPorTCCEtapa(trabalhoConclusaoCursoVO.getCodigo(), EtapaTCCEnum.PLANO_TCC));
		}
	}
	
	@Override
	public List<TrabalhoConclusaoCursoVO> consultar(Integer unidadeEnsino, Integer curso, Integer turma, Integer orientador, Integer coordenador, String matricula, EtapaTCCEnum etapaTCCEnum, 
												    SituacaoTCCEnum situacaoTCCEnum, Boolean filtroSomenteAtrasados, Boolean filtroAguardandoOrientador, Boolean filtroMinhaAvalicao, 
												    Integer limit, Integer offset, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder("select (now()::date - t.dataTerminoEtapaAtual) as diasAtrasoEtapaAtual, t.* from (");
		sql.append(getSelectCompleto());
		sql.append(" WHERE 1=1 ");
		if (unidadeEnsino != null && unidadeEnsino > 0) {
			sql.append(" and matricula.unidadeEnsino = ").append(unidadeEnsino);
		}
		if (curso != null && curso > 0) {
			sql.append(" and curso.codigo = ").append(curso);
		}
		if (turma != null && turma > 0) {
			sql.append(" and turma.codigo = ").append(turma);
		}
		if (orientador != null && orientador > 0) {
			sql.append(" and orientador.codigo = ").append(orientador);
		}
		if (coordenador != null && coordenador > 0) {
			sql.append(" and coordenador.codigo = ").append(coordenador);
		}
		if (matricula != null && !matricula.trim().isEmpty()) {
			sql.append(" and matricula.matricula = '").append(matricula).append("' ");
		}
		if (etapaTCCEnum != null) {
			sql.append(" and trabalhoconclusaocurso.etapaTCC = '").append(etapaTCCEnum.name()).append("' ");
		}
		if (situacaoTCCEnum != null) {
			sql.append(" and trabalhoconclusaocurso.situacaoTCC = '").append(situacaoTCCEnum.name()).append("' ");
		}
		if (filtroAguardandoOrientador != null && filtroAguardandoOrientador) {
			sql.append(" and trabalhoconclusaocurso.orientador is null ");
		}
		if (filtroMinhaAvalicao != null && filtroMinhaAvalicao) {
			sql.append(" and orientador.codigo = ").append(coordenador);
		}
//		sql.append(" order by case trabalhoconclusaocurso.etapaTCCEnum when '").append(EtapaTCCEnum.PLANO_TCC.name()).append("' then 1 ");
//		sql.append(" when '").append(EtapaTCCEnum.ELABORACAO_TCC.name()).append("' then 2 ");
//		sql.append(" else 3 end, dataAlteracaoSituacao ");
		sql.append(" order by trabalhoconclusaocurso.dataAlteracaoSituacao");
		sql.append(") as t ");
		if (filtroSomenteAtrasados != null && filtroSomenteAtrasados) {
			sql.append(" where (now()::date - t.dataTerminoEtapaAtual) > 0 ");
		}
        if (limit != null) {
        	sql.append(" LIMIT ").append(limit);
            if (offset != null) {
            	sql.append(" OFFSET ").append(offset);
            }
        }
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaCompleta(rs);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarEnvioEmailDepartamentoFinanceiro(TrabalhoConclusaoCursoVO tcc, String msg, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		if (msg.equals("")) {
			throw new Exception("N?o foi informado nenhum texto para envio ao departamento financeiro!");
		}
		PessoaVO pessoa = ((UnidadeEnsinoVO)getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaResponsavelCobrancaUnidadePorCodigo(tcc.getMatriculaPeriodoTurmaDisciplina().getCodigo())).getResponsavelCobrancaUnidade();
		ComunicacaoInternaVO comunicacaoInternaVO = new ComunicacaoInternaVO();
		if (usuarioVO.getPessoa() == null || usuarioVO.getPessoa().getCodigo().equals(0)) {
			comunicacaoInternaVO.setResponsavel(configuracaoGeralSistemaVO.getResponsavelPadraoComunicadoInterno());
		} else {
			comunicacaoInternaVO.setResponsavel(usuarioVO.getPessoa());
		}
		comunicacaoInternaVO.setEnviarEmail(true);
		comunicacaoInternaVO.setTipoDestinatario("FU");
		comunicacaoInternaVO.setTipoMarketing(false);
		comunicacaoInternaVO.setTipoLeituraObrigatoria(false);
		comunicacaoInternaVO.setDigitarMensagem(true);
		comunicacaoInternaVO.setRemoverCaixaSaida(false);		
		comunicacaoInternaVO.setAssunto("Contato aluno com pend?ncia financeira, para realiza??o de TCC!");		
		comunicacaoInternaVO.setMensagem(comunicacaoInternaVO.getMensagemComLayout(msg));
		ComunicadoInternoDestinatarioVO comunicadoInternoDestinatarioVO = new ComunicadoInternoDestinatarioVO();
		comunicadoInternoDestinatarioVO.setTipoComunicadoInterno("LE");
		comunicadoInternoDestinatarioVO.setDataLeitura(null);
		comunicadoInternoDestinatarioVO.setCiJaRespondida(false);
		comunicadoInternoDestinatarioVO.setCiJaLida(false);
		comunicadoInternoDestinatarioVO.setRemoverCaixaEntrada(false);
		comunicadoInternoDestinatarioVO.setMensagemMarketingLida(false);
		comunicadoInternoDestinatarioVO.setDestinatario(pessoa);
		comunicacaoInternaVO.getComunicadoInternoDestinatarioVOs().add(comunicadoInternoDestinatarioVO);
		getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoInternaVO, false, usuarioVO, configuracaoGeralSistemaVO,null);
	}

	@Override
	public List<TrabalhoConclusaoCursoEtapaVO> consultarAgrupandoPorEtapaSituacao(Integer unidadeEnsino, Integer curso, Integer turma, Integer orientador, Integer coordenador, String matricula,
			Boolean filtroSomenteAtrasados, Boolean filtroAguardandoOrientador, Boolean filtroMinhaAvaliacao) {
		StringBuilder sql = new StringBuilder("SELECT count(t.codigo) as qtde, t.etapaTCC, t.situacaoTCC from (");
		sql.append(" select case when (etapatcc = 'PLANO_TCC') then (dataTerminoPlanoTCC) ");
		sql.append(" when (etapatcc = 'ELABORACAO_TCC') then (dataTerminoElaboracaoTCC) ");
		sql.append(" when (etapatcc = 'AVALIACAO_TCC') then (dataTerminoAvaliacaoTCC) end as dataTerminoEtapaAtual, trabalhoConclusaoCurso.codigo, TrabalhoConclusaoCurso.etapaTCC, TrabalhoConclusaoCurso.situacaoTCC ");
//		sql.append(" SELECT count(TrabalhoConclusaoCurso.codigo) as qtde");
		sql.append(" FROM TrabalhoConclusaoCurso ");
		sql.append(" inner join pessoa aluno on aluno.codigo = TrabalhoConclusaoCurso.aluno ");
		sql.append(" inner join configuracaoTCC on configuracaoTCC.codigo = TrabalhoConclusaoCurso.configuracaoTCC ");
		sql.append(" inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.codigo = TrabalhoConclusaoCurso.matriculaperiodoturmadisciplina ");
		sql.append(" inner join matricula on matriculaperiodoturmadisciplina.matricula = matricula.matricula ");
		sql.append(" inner join curso on curso.codigo = matricula.curso ");
		sql.append(" inner join turno on turno.codigo = matricula.turno ");
		sql.append(" inner join turma on turma.codigo = matriculaperiodoturmadisciplina.turma ");
		sql.append(" inner join disciplina on disciplina.codigo = matriculaperiodoturmadisciplina.disciplina ");
		sql.append(" left join pessoa orientador on orientador.codigo = TrabalhoConclusaoCurso.orientador ");
		sql.append(" left join pessoa coordenador on coordenador.codigo = TrabalhoConclusaoCurso.coordenador ");
		sql.append(" where 1=1 ");
		if (unidadeEnsino != null && unidadeEnsino > 0) {
			sql.append(" and matricula.unidadeEnsino = ").append(unidadeEnsino);
		}
		if (curso != null && curso > 0) {
			sql.append(" and curso.codigo = ").append(curso);
		}
		if (turma != null && turma > 0) {
			sql.append(" and turma.codigo = ").append(turma);
		}
		if (orientador != null && orientador > 0) {
			sql.append(" and orientador.codigo = ").append(orientador);
		}
		if (coordenador != null && coordenador > 0) {
			sql.append(" and coordenador.codigo = ").append(coordenador);
		}
		if (matricula != null && !matricula.trim().isEmpty()) {
			sql.append(" and matricula.matricula = '").append(matricula).append("' ");
		}
		if (filtroAguardandoOrientador != null && filtroAguardandoOrientador) {
			sql.append(" and trabalhoconclusaocurso.orientador is null ");
		}
		if (filtroMinhaAvaliacao != null && filtroMinhaAvaliacao) {
			sql.append(" and orientador.codigo = ").append(coordenador);
		}
		sql.append(" group by TrabalhoConclusaoCurso.codigo, TrabalhoConclusaoCurso.etapatcc, TrabalhoConclusaoCurso.situacaotcc, TrabalhoConclusaoCurso.dataterminoplanotcc, TrabalhoConclusaoCurso.dataterminoelaboracaotcc, TrabalhoConclusaoCurso.dataterminoavaliacaotcc");
		sql.append(" order by TrabalhoConclusaoCurso.etapaTCC, TrabalhoConclusaoCurso.situacaoTCC");
		sql.append(") as t ");
		if (filtroSomenteAtrasados != null && filtroSomenteAtrasados) {
			sql.append(" where (now()::date - t.dataTerminoEtapaAtual) > 0 ");
		}
		sql.append(" group by t.etapaTCC, t.situacaoTCC ");
		sql.append(" order by case t.situacaoTCC when '").append(SituacaoTCCEnum.NOVO.name()).append("' then 1 ");
		sql.append(" when '").append(SituacaoTCCEnum.EM_ELABORACAO.name()).append("' then 2 ");
		sql.append(" when '").append(SituacaoTCCEnum.AGUARDANDO_ARQUIVO.name()).append("' then 3 ");
		sql.append(" when '").append(SituacaoTCCEnum.AGUARDANDO_APROVACAO_ORIENTADOR.name()).append("' then 4 ");
		sql.append(" when '").append(SituacaoTCCEnum.AGUARDANDO_AVALIACAO_BANCA.name()).append("' then 5 ");
		sql.append(" when '").append(SituacaoTCCEnum.APROVADO.name()).append("' then 8 ");
		sql.append(" when '").append(SituacaoTCCEnum.REPROVADO_FALTA.name()).append("' then 9 ");
		sql.append(" when '").append(SituacaoTCCEnum.REPROVADO.name()).append("' then 10 ");
		sql.append(" else 3 end ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<TrabalhoConclusaoCursoEtapaVO> lista = new ArrayList<TrabalhoConclusaoCursoEtapaVO>(0);
		while (rs.next()) {
			TrabalhoConclusaoCursoEtapaVO obj = new TrabalhoConclusaoCursoEtapaVO();
			obj.setQuantidade(rs.getInt("qtde"));
			obj.setEtapaTCC(EtapaTCCEnum.valueOf(rs.getString("etapaTCC")));
			obj.setSituacaoTCC(SituacaoTCCEnum.valueOf(rs.getString("situacaoTCC")));
			lista.add(obj);
		}
		return lista;
	}

	public List<TrabalhoConclusaoCursoVO> montarDadosConsultaCompleta(SqlRowSet rs) {
		List<TrabalhoConclusaoCursoVO> trabalhoConclusaoCursoVOs = new ArrayList<TrabalhoConclusaoCursoVO>(0);
		while (rs.next()) {
			trabalhoConclusaoCursoVOs.add(montarDadosCompleto(rs));
		}
		return trabalhoConclusaoCursoVOs;
	}

	@Override
	public Integer consultarTotalRegistro(Integer unidadeEnsino, Integer curso, Integer turma, Integer orientador, Integer coordenador, String matricula, EtapaTCCEnum etapaTCCEnum, SituacaoTCCEnum situacaoTCCEnum, Boolean filtroSomenteAtrasados, Boolean filtroAguardandoOrientador, Boolean filtroMinhaAvaliacao) {
		StringBuilder sql = new StringBuilder("SELECT count(t.codigo) as qtde from (");
		sql.append(" select case when (etapatcc = 'PLANO_TCC') then (dataTerminoPlanoTCC) ");
		sql.append(" when (etapatcc = 'ELABORACAO_TCC') then (dataTerminoElaboracaoTCC) ");
		sql.append(" when (etapatcc = 'AVALIACAO_TCC') then (dataTerminoAvaliacaoTCC) end as dataTerminoEtapaAtual, trabalhoConclusaoCurso.codigo ");
		sql.append(" FROM TrabalhoConclusaoCurso ");
		sql.append(" inner join pessoa aluno on aluno.codigo = TrabalhoConclusaoCurso.aluno ");
		sql.append(" inner join configuracaoTCC on configuracaoTCC.codigo = TrabalhoConclusaoCurso.configuracaoTCC ");
		sql.append(" inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.codigo = TrabalhoConclusaoCurso.matriculaperiodoturmadisciplina ");
		sql.append(" inner join matricula on matriculaperiodoturmadisciplina.matricula = matricula.matricula ");
		sql.append(" inner join curso on curso.codigo = matricula.curso ");
		sql.append(" inner join turno on turno.codigo = matricula.turno ");
		sql.append(" inner join turma on turma.codigo = matriculaperiodoturmadisciplina.turma ");
		sql.append(" inner join disciplina on disciplina.codigo = matriculaperiodoturmadisciplina.disciplina ");
		sql.append(" left join pessoa orientador on orientador.codigo = TrabalhoConclusaoCurso.orientador ");
		sql.append(" left join pessoa coordenador on coordenador.codigo = TrabalhoConclusaoCurso.coordenador ");
		sql.append(" WHERE 1=1 ");
		if (unidadeEnsino != null && unidadeEnsino > 0) {
			sql.append(" and matricula.unidadeEnsino = ").append(unidadeEnsino);
		}
		if (curso != null && curso > 0) {
			sql.append(" and curso.codigo = ").append(curso);
		}
		if (turma != null && turma > 0) {
			sql.append(" and turma.codigo = ").append(turma);
		}
		if (orientador != null && orientador > 0) {
			sql.append(" and orientador.codigo = ").append(orientador);
		}
		if (coordenador != null && coordenador > 0) {
			sql.append(" and coordenador.codigo = ").append(coordenador);
		}
		if (matricula != null && !matricula.trim().isEmpty()) {
			sql.append(" and matricula.matricula = '").append(matricula).append("' ");
		}
		if (etapaTCCEnum != null) {
			sql.append(" and trabalhoconclusaocurso.etapaTCC = '").append(etapaTCCEnum.name()).append("' ");
		}
		if (situacaoTCCEnum != null) {
			sql.append(" and trabalhoconclusaocurso.situacaoTCC = '").append(situacaoTCCEnum.name()).append("' ");
		}
		if (filtroAguardandoOrientador != null && filtroAguardandoOrientador) {
			sql.append(" and trabalhoconclusaocurso.orientador is null ");
		}
		if (filtroMinhaAvaliacao != null && filtroMinhaAvaliacao) {
			sql.append(" and orientador.codigo = ").append(coordenador);
		}
		sql.append(" group by trabalhoconclusaocurso.codigo, trabalhoconclusaocurso.etapatcc, trabalhoconclusaocurso.dataterminoplanotcc, trabalhoconclusaocurso.dataterminoelaboracaotcc, trabalhoconclusaocurso.dataterminoavaliacaotcc");
		sql.append(") as t ");
		if (filtroSomenteAtrasados != null && filtroSomenteAtrasados) {
			sql.append(" where (now()::date - t.dataTerminoEtapaAtual) > 0 ");
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (rs.next()) {
			return rs.getInt("qtde");
		}
		return 0;
	}

	public static String getIdEntidade() {
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		TrabalhoConclusaoCurso.idEntidade = idEntidade;
	}
	
	public void executarEnvioMensagemTCCEmAtraso() throws Exception {
		executarEnvioMensagemPrimeiroAvisoTCCEmAtraso();
		executarEnvioMensagemSegundoAvisoTCCEmAtraso();
	}
	
	public void executarEnvioMensagemPrimeiroAvisoTCCEmAtraso() throws Exception {
		for (TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO : consultarTrabalhoConclusaoCursoParaPrimeiraNotificacaoAtraso()) {
			try {
				String tipoDestinatario = "";
				if (trabalhoConclusaoCursoVO.getSituacaoTCC().equals(SituacaoTCCEnum.AGUARDANDO_ARQUIVO) || trabalhoConclusaoCursoVO.getSituacaoTCC().equals(SituacaoTCCEnum.EM_ELABORACAO) || 
						(trabalhoConclusaoCursoVO.getSituacaoTCC().equals(SituacaoTCCEnum.NOVO) && trabalhoConclusaoCursoVO.getOrientador().getCodigo().intValue() != 0)) {
					tipoDestinatario = "AL";
				} else if (trabalhoConclusaoCursoVO.getSituacaoTCC().equals(SituacaoTCCEnum.AGUARDANDO_APROVACAO_ORIENTADOR)) {
					tipoDestinatario = "PR";
				} else if (trabalhoConclusaoCursoVO.getSituacaoTCC().equals(SituacaoTCCEnum.AGUARDANDO_AVALIACAO_BANCA) || 
						(trabalhoConclusaoCursoVO.getSituacaoTCC().equals(SituacaoTCCEnum.NOVO) && trabalhoConclusaoCursoVO.getOrientador().getCodigo().intValue() == 0)) {
					tipoDestinatario = "FU";
				}
				getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemPrimeiroAvisoTCCEmAtraso(trabalhoConclusaoCursoVO, tipoDestinatario);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void executarEnvioMensagemSegundoAvisoTCCEmAtraso() throws Exception {
		for (TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO : consultarTrabalhoConclusaoCursoParaSegundaNotificacaoAtraso()) {
			try {
				String tipoDestinatario = "";
				if (trabalhoConclusaoCursoVO.getSituacaoTCC().equals(SituacaoTCCEnum.AGUARDANDO_ARQUIVO) || trabalhoConclusaoCursoVO.getSituacaoTCC().equals(SituacaoTCCEnum.EM_ELABORACAO) || 
						(trabalhoConclusaoCursoVO.getSituacaoTCC().equals(SituacaoTCCEnum.NOVO) && trabalhoConclusaoCursoVO.getOrientador().getCodigo().intValue() != 0)) {
					tipoDestinatario = "AL";
				} else if (trabalhoConclusaoCursoVO.getSituacaoTCC().equals(SituacaoTCCEnum.AGUARDANDO_APROVACAO_ORIENTADOR)) {
					tipoDestinatario = "PR";
				} else if (trabalhoConclusaoCursoVO.getSituacaoTCC().equals(SituacaoTCCEnum.AGUARDANDO_AVALIACAO_BANCA) || 
						(trabalhoConclusaoCursoVO.getSituacaoTCC().equals(SituacaoTCCEnum.NOVO) && trabalhoConclusaoCursoVO.getOrientador().getCodigo().intValue() == 0)) {
					tipoDestinatario = "FU";
				}
				getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemSegundoAvisoTCCEmAtraso(trabalhoConclusaoCursoVO, tipoDestinatario);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public List<TrabalhoConclusaoCursoVO> consultarTrabalhoConclusaoCursoParaPrimeiraNotificacaoAtraso() throws Exception {
		StringBuilder sql = new StringBuilder(getSelectCompleto());
		sql.append("where configuracaoTCC.numeroDiaAntesEncerramentoEtapaPrimeiroAviso > 0 ");
		sql.append("and (((TrabalhoConclusaoCurso.dataTerminoPlanoTCC::timestamp - (''''||configuracaoTCC.numeroDiaAntesEncerramentoEtapaPrimeiroAviso::varchar || ' days')::interval) = now()::date) ");
		sql.append("or ((TrabalhoConclusaoCurso.dataTerminoElaboracaoTCC::timestamp - (''''||configuracaoTCC.numeroDiaAntesEncerramentoEtapaPrimeiroAviso::varchar || ' days')::interval) = now()::date) ");
		sql.append("or ((TrabalhoConclusaoCurso.dataTerminoAvaliacaoTCC::timestamp - (''''||configuracaoTCC.numeroDiaAntesEncerramentoEtapaPrimeiroAviso::varchar || ' days')::interval) = now()::date)) ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaCompleta(rs);
	}
	
	public List<TrabalhoConclusaoCursoVO> consultarTrabalhoConclusaoCursoParaSegundaNotificacaoAtraso() throws Exception {
		StringBuilder sql = new StringBuilder(getSelectCompleto());
		sql.append("where configuracaoTCC.numeroDiaAntesEncerramentoEtapaSegundoAviso > 0 ");
		sql.append("and (((TrabalhoConclusaoCurso.dataTerminoPlanoTCC::timestamp - (''''||configuracaoTCC.numeroDiaAntesEncerramentoEtapaSegundoAviso::varchar || ' days')::interval) = now()::date) ");
		sql.append("or ((TrabalhoConclusaoCurso.dataTerminoElaboracaoTCC::timestamp - (''''||configuracaoTCC.numeroDiaAntesEncerramentoEtapaSegundoAviso::varchar || ' days')::interval) = now()::date) ");
		sql.append("or ((TrabalhoConclusaoCurso.dataTerminoAvaliacaoTCC::timestamp - (''''||configuracaoTCC.numeroDiaAntesEncerramentoEtapaSegundoAviso::varchar || ' days')::interval) = now()::date)) ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaCompleta(rs);
	}
	
	public List<TrabalhoConclusaoCursoVO> consultarTrabalhoConclusaoCursoParaNotificacaoReprovacaoPorAtraso() throws Exception {
		StringBuilder sql = new StringBuilder(getSelectCompleto());
		sql.append("where ((TrabalhoConclusaoCurso.dataTerminoPlanoTCC::timestamp + (''''||configuracaoTCC.numeroDiaDepoisEncerramentoEtapaReprovacaoAutomatica::varchar || ' days')::interval) = now()::date) ");
		sql.append("or ((TrabalhoConclusaoCurso.dataTerminoElaboracaoTCC::timestamp + (''''||configuracaoTCC.numeroDiaDepoisEncerramentoEtapaReprovacaoAutomatica::varchar || ' days')::interval) = now()::date) ");
		sql.append("or ((TrabalhoConclusaoCurso.dataTerminoAvaliacaoTCC::timestamp + (''''||configuracaoTCC.numeroDiaDepoisEncerramentoEtapaReprovacaoAutomatica::varchar || ' days')::interval) = now()::date) ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaCompleta(rs);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void executarEnvioMensagemReprovacaoAutomaticaPorAtrasoTCC() throws Exception {
		for (TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO : consultarTrabalhoConclusaoCursoParaNotificacaoReprovacaoPorAtraso()) {
			try {
				realizarReprovacaoTCC(trabalhoConclusaoCursoVO, false, new UsuarioVO());
				getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemReprovacaoAutomaticaPorAtrasoTCC(trabalhoConclusaoCursoVO);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void alterarCoordenadorTCC(final UnidadeEnsinoVO unidadeEnsino, UsuarioVO usuarioLogado) throws Exception {
		if (unidadeEnsino != null) {
			for (final UnidadeEnsinoCursoVO unidade : unidadeEnsino.getUnidadeEnsinoCursoVOs()) {
//				if (unidade.getCoordenadorTCC().getPessoa().getCodigo().intValue() != 0 || unidadeEnsino.getCoordenadorTCC().getPessoa().getCodigo().intValue() != 0) {
					if (unidade.getUnidadeEnsino().intValue() != 0 && unidade.getCurso().getCodigo().intValue() != 0) {
						final String sql = "UPDATE TrabalhoConclusaoCurso SET coordenador = ? from matriculaperiodoturmadisciplina as mptd, turma where mptd.codigo = TrabalhoConclusaoCurso.matriculaperiodoturmadisciplina and turma.codigo = mptd.turma and turma.unidadeEnsino = ? and turma.curso = ?"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado);
			            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
		
			                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
			                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
			    				if (unidade.getCoordenadorTCC().getPessoa().getCodigo().intValue() != 0) {
			    					sqlAlterar.setInt(1, unidade.getCoordenadorTCC().getPessoa().getCodigo());
			    				} else {
			    					if (unidadeEnsino.getCoordenadorTCC().getPessoa().getCodigo().intValue() != 0) {
			    						sqlAlterar.setInt(1, unidadeEnsino.getCoordenadorTCC().getPessoa().getCodigo());
				                    } else {
				                    	sqlAlterar.setNull(1, 0);
				                    }
			    				}
			                    sqlAlterar.setInt(2, unidade.getUnidadeEnsino());
			                    sqlAlterar.setInt(3, unidade.getCurso().getCodigo());
			                    return sqlAlterar;
			                }
			            });
					}
//				}
			}
		}
	}
	
}