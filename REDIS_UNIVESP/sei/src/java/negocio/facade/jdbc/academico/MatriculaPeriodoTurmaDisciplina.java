package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
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

import controle.arquitetura.AplicacaoControle;
import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.ConteudoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.FrequenciaAulaVO;
import negocio.comuns.academico.GradeCurricularGrupoOptativaDisciplinaVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.GradeDisciplinaCompostaVO;
import negocio.comuns.academico.GradeDisciplinaVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.HorarioAlunoTurnoVO;
import negocio.comuns.academico.MapaEquivalenciaDisciplinaCursadaVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaTCCVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.OfertaDisciplinaVO;
import negocio.comuns.academico.ProcessoMatriculaVO;
import negocio.comuns.academico.RegistroAulaVO;
import negocio.comuns.academico.TurmaDisciplinaEstatisticaAlunoVO;
import negocio.comuns.academico.TurmaDisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.ClassificacaoDisciplinaEnum;
import negocio.comuns.academico.enumeradores.DefinicoesTutoriaOnlineEnum;
import negocio.comuns.academico.enumeradores.ModalidadeDisciplinaEnum;
import negocio.comuns.academico.enumeradores.ModeloGeracaoSalaBlackboardEnum;
import negocio.comuns.academico.enumeradores.OrigemFechamentoMatriculaPeriodoEnum;
import negocio.comuns.academico.enumeradores.TipoAlunoCalendarioMatriculaEnum;
import negocio.comuns.academico.enumeradores.TipoEstatisticaTurmaDisciplinaEnum;
import negocio.comuns.academico.enumeradores.TipoSubTurmaEnum;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.PermissaoAcessoMenuVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.avaliacaoinst.AvaliacaoInstitucionalVO;
import negocio.comuns.blackboard.enumeradores.TipoSalaAulaBlackboardEnum;
import negocio.comuns.ead.AtividadeDiscursivaVO;
import negocio.comuns.ead.CalendarioAtividadeMatriculaVO;
import negocio.comuns.ead.ConfiguracaoEADVO;
import negocio.comuns.ead.ConteudoRegistroAcessoVO;
import negocio.comuns.ead.ProgramacaoTutoriaOnlineProfessorVO;
import negocio.comuns.ead.ProgramacaoTutoriaOnlineVO;
import negocio.comuns.ead.enumeradores.PublicoAlvoAtividadeDiscursivaEnum;
import negocio.comuns.ead.enumeradores.TipoCalendarioAtividadeMatriculaEnum;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.SituacaoHistorico;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.avaliacaoinst.AvaliacaoInstitucional;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.academico.MatriculaPeriodoTurmaDisciplinaInterfaceFacade;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;
import webservice.servicos.MatriculaRSVO;
import webservice.servicos.objetos.DisciplinaRSVO;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>MatriculaPeriodoTurmaDisciplinaVO</code>. Responsável
 * por implementar operações como incluir, alterar, excluir e consultar
 * pertinentes a classe <code>MatriculaPeriodoTurmaDisciplinaVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * 
 * @see MatriculaPeriodoTurmaDisciplinaVO
 * @see ControleAcesso
 */
@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class MatriculaPeriodoTurmaDisciplina extends ControleAcesso implements MatriculaPeriodoTurmaDisciplinaInterfaceFacade {

	protected static String idEntidade;

	public MatriculaPeriodoTurmaDisciplina() throws Exception {
		super();
		setIdEntidade("Matricula");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.academico.MatriculaPeriodoTurmaDisciplinaInterfaceFacade
	 * #novo()
	 */
	public MatriculaPeriodoTurmaDisciplinaVO novo() throws Exception {
		MatriculaPeriodoTurmaDisciplina.incluir(getIdEntidade());
		MatriculaPeriodoTurmaDisciplinaVO obj = new MatriculaPeriodoTurmaDisciplinaVO();
		return obj;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final MatriculaPeriodoTurmaDisciplinaVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, GradeCurricularVO gradeCurricularVO, UsuarioVO usuario) throws Exception {
		incluir(null, null, obj, configuracaoFinanceiroVO, gradeCurricularVO, usuario);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.academico.MatriculaPeriodoTurmaDisciplinaInterfaceFacade
	 * #incluir(negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO)
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, final MatriculaPeriodoTurmaDisciplinaVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, GradeCurricularVO gradeCurricularVO, UsuarioVO usuario) throws Exception {
		try {
			MatriculaPeriodoTurmaDisciplinaVO.validarDados(obj);
			realizarDefinicaoBimestre(obj, usuario);
//			if (obj.getModalidadeDisciplina().equals(ModalidadeDisciplinaEnum.ON_LINE) && obj.getConteudo().getCodigo() == 0) {
//				obj.getConteudo().setCodigo(getFacadeFactory().getConteudoFacade().consultarCodigoConteudoAtivoTurmaDisciplinaConteudoPorCodigoTurmaDisciplinaAnoSemestre(obj.getTurma().getCodigo(), obj.getDisciplina().getCodigo(), obj.getAno(), obj.getSemestre()));
//			}			

			final String sql = "INSERT INTO MatriculaPeriodoTurmaDisciplina( " + "matriculaPeriodo, turma, disciplina, " + "semestre, ano, matricula, disciplinaIncluida, disciplinaEquivale, disciplinaComposta, " + "justificativa, observacaojustificativa, disciplinaFazParteComposicao, reposicao, inclusaoForaPrazo, " + "modalidadeDisciplina, conteudo, permiteEscolherModalidade,  " + "disciplinaPorEquivalencia, gradeDisciplinaComposta, disciplinaEmRegimeEspecial, disciplinaOptativa, " + "disciplinaReferenteAUmGrupoOptativa, gradeCurricularGrupoOptativaDisciplina, gradeDisciplina, " + "mapaEquivalenciaDisciplina, mapaEquivalenciaDisciplinaCursada, transferenciaMatrizCurricularMatricula, " + "disciplinaCursandoPorCorrespondenciaAposTransferencia " + ", turmapratica, turmateorica, liberadaSemDisponibilidadeVagas, usuarioLiberadaSemDisponibilidadeVagas, dataLiberadaSemDisponibilidadeVagas, dataultimaalteracao, bimestre  ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo";
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setInt(1, obj.getMatriculaPeriodo().intValue());
					if (obj.getTurma().getCodigo().intValue() != 0) {
						sqlInserir.setInt(2, obj.getTurma().getCodigo().intValue());
					} else {
						sqlInserir.setNull(2, 0);
					}
					sqlInserir.setInt(3, obj.getDisciplina().getCodigo().intValue());
					sqlInserir.setString(4, obj.getSemestre());
					sqlInserir.setString(5, obj.getAno());
					sqlInserir.setString(6, obj.getMatricula());
					sqlInserir.setBoolean(7, obj.getDisciplinaIncluida());
					sqlInserir.setBoolean(8, obj.getDisciplinaEquivale());
					sqlInserir.setBoolean(9, obj.getDisciplinaComposta());
					sqlInserir.setString(10, obj.getJustificativa());
					sqlInserir.setString(11, obj.getObservacaoJustificativa());
					sqlInserir.setBoolean(12, obj.getDisciplinaFazParteComposicao());
					sqlInserir.setBoolean(13, obj.getReposicao());
					sqlInserir.setBoolean(14, obj.getInclusaoForaPrazo());

					sqlInserir.setString(15, obj.getModalidadeDisciplina().name());
					if (obj.getModalidadeDisciplina().equals(ModalidadeDisciplinaEnum.ON_LINE) && obj.getConteudo().getCodigo().intValue() != 0) {
						sqlInserir.setInt(16, obj.getConteudo().getCodigo().intValue());
					} else {
						sqlInserir.setNull(16, 0);
					}
					sqlInserir.setBoolean(17, obj.getPermiteEscolherModalidade());

					// NOVOS CAMPOS
					sqlInserir.setBoolean(18, obj.getDisciplinaPorEquivalencia());
					if (obj.getGradeDisciplinaCompostaVO().getCodigo().intValue() != 0) {
						sqlInserir.setInt(19, obj.getGradeDisciplinaCompostaVO().getCodigo());
					} else {
						sqlInserir.setNull(19, 0);
					}
					sqlInserir.setBoolean(20, obj.getDisciplinaEmRegimeEspecial());
					sqlInserir.setBoolean(21, obj.getDisciplinaOptativa());
					sqlInserir.setBoolean(22, obj.getDisciplinaReferenteAUmGrupoOptativa());
					if (obj.getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo().intValue() != 0) {
						sqlInserir.setInt(23, obj.getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo());
					} else {
						sqlInserir.setNull(23, 0);
					}
					if (obj.getGradeDisciplinaVO().getCodigo().intValue() != 0) {
						sqlInserir.setInt(24, obj.getGradeDisciplinaVO().getCodigo());
					} else {
						sqlInserir.setNull(24, 0);
					}
					if (obj.getMapaEquivalenciaDisciplinaVOIncluir().getCodigo().intValue() != 0) {
						sqlInserir.setInt(25, obj.getMapaEquivalenciaDisciplinaVOIncluir().getCodigo());
					} else {
						sqlInserir.setNull(25, 0);
					}
					if (obj.getMapaEquivalenciaDisciplinaCursada().getCodigo().intValue() != 0) {
						sqlInserir.setInt(26, obj.getMapaEquivalenciaDisciplinaCursada().getCodigo());
					} else {
						sqlInserir.setNull(26, 0);
					}
					if (obj.getTransferenciaMatrizCurricularMatricula().getCodigo().intValue() != 0) {
						sqlInserir.setInt(27, obj.getTransferenciaMatrizCurricularMatricula().getCodigo());
					} else {
						sqlInserir.setNull(27, 0);
					}
					sqlInserir.setBoolean(28, obj.getDisciplinaCursandoPorCorrespondenciaAposTransferencia());
					if (obj.getTurmaPratica().getCodigo().intValue() != 0) {
						sqlInserir.setInt(29, obj.getTurmaPratica().getCodigo());
					} else {
						sqlInserir.setNull(29, 0);
					}
					if (obj.getTurmaTeorica().getCodigo().intValue() != 0) {
						sqlInserir.setInt(30, obj.getTurmaTeorica().getCodigo());
					} else {
						sqlInserir.setNull(30, 0);
					}
					sqlInserir.setBoolean(31, obj.getLiberadaSemDisponibilidadeVagas());
					if (obj.getUsuarioLiberadaSemDisponibilidadeVagas().getCodigo().intValue() != 0) {
						sqlInserir.setInt(32, obj.getUsuarioLiberadaSemDisponibilidadeVagas().getCodigo());
					} else {
						sqlInserir.setNull(32, 0);
					}
					sqlInserir.setTimestamp(33, Uteis.getDataJDBCTimestamp(new Date()));
					sqlInserir.setTimestamp(34, Uteis.getDataJDBCTimestamp(new Date()));
					sqlInserir.setInt(35, obj.getBimestre());
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
			// criarReservaTurmaDisciplina(obj, usuario);
			if ((matriculaPeriodoVO == null) || (matriculaPeriodoVO.getMatricula().equals(""))) {
				matriculaPeriodoVO = getFacadeFactory().getMatriculaPeriodoFacade().consultarPorChavePrimaria(obj.getMatriculaPeriodo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, configuracaoFinanceiroVO, usuario);
			}
			if ((matriculaVO == null) || (matriculaVO.getMatricula().equals(""))) {
				matriculaVO = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula(), 0, Uteis.NIVELMONTARDADOS_DADOSBASICOS, configuracaoFinanceiroVO, usuario);
			}
			// //
			// System.out.println("++++++++++++++++++++++ INCLUI MATRICULA PERIODO TURMA DISCIPLINA  (FINAL) +++++++++++++++++++++++++++");
			getFacadeFactory().getHistoricoFacade().incluirHistorico(obj, matriculaPeriodoVO, matriculaVO, configuracaoFinanceiroVO, gradeCurricularVO, usuario);
			//getFacadeFactory().getTrabalhoConclusaoCursoFacade().realizarCriacaoTrabalhoConclusaoCursoAluno(matriculaVO.getCurso().getCodigo(), matriculaVO.getAluno().getCodigo(), matriculaPeriodoVO.getGradeCurricular().getCodigo(), obj, usuario);
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoPadraoSistema();
			getFacadeFactory().getIntegracaoMestreGRInterfaceFacade().verificarAlunoDisciplinaInsert(obj, usuario, configuracaoGeralSistemaVO);
			definirProfessorTutoriaOnlineEGerarCalendarioAtividadeMatriculaAcessoConteudoEstudo(obj, usuario);
			
		} catch (Exception e) {
			obj.setCodigo(0);
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}	

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.academico.MatriculaPeriodoTurmaDisciplinaInterfaceFacade
	 * #consultarExistenciaVagaTurmaDisciplina(java.lang.Integer,
	 * java.lang.Integer, java.lang.Integer)
	 */
	public TurmaDisciplinaVO consultarExistenciaVagaTurmaDisciplina(Integer codigoTurma, Integer disciplina, Integer nrVagas, UsuarioVO usuario) throws Exception {
		TurmaDisciplinaVO turmaDisciplina = getFacadeFactory().getTurmaDisciplinaFacade().consultarExistenciaVagaTurmaDisciplina(codigoTurma, disciplina, nrVagas, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		return turmaDisciplina;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final MatriculaPeriodoTurmaDisciplinaVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		alterar(obj.getMatriculaObjetoVO(), obj.getMatriculaPeriodoObjetoVO(), obj, configuracaoFinanceiroVO, usuario);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.academico.MatriculaPeriodoTurmaDisciplinaInterfaceFacade
	 * #alterar(negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO)
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final MatriculaVO matriculaVO, final MatriculaPeriodoVO matriculaPeriodoVO, final MatriculaPeriodoTurmaDisciplinaVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		MatriculaPeriodoTurmaDisciplinaVO.validarDados(obj);
		if (obj.getModalidadeDisciplina().equals(ModalidadeDisciplinaEnum.ON_LINE) && obj.getConteudo().getCodigo() == 0) {
			obj.getConteudo().setCodigo(getFacadeFactory().getConteudoFacade().consultarCodigoConteudoAtivoTurmaDisciplinaConteudoPorCodigoTurmaDisciplinaAnoSemestre(obj.getTurma().getCodigo(), obj.getDisciplina().getCodigo(), obj.getAno(), obj.getSemestre()));
		}
		getFacadeFactory().getTrabalhoConclusaoCursoFacade().realizarCriacaoTrabalhoConclusaoCursoAluno(matriculaVO.getCurso().getCodigo(), matriculaVO.getAluno().getCodigo(), matriculaPeriodoVO.getGradeCurricular().getCodigo(), obj, usuario);
		final StringBuilder sql = new StringBuilder("UPDATE MatriculaPeriodoTurmaDisciplina ")
				.append("set matriculaPeriodo=?, ")
				.append("turma=?, ")
				.append("disciplina=?, ")
				.append("semestre=?, ")
				.append("ano=?, ")
				.append("matricula=?, ")
				.append("disciplinaIncluida=?, ")
				.append("disciplinaEquivale=?, ")
				.append("disciplinaComposta=?, ")
				.append("justificativa=?, ")
				.append("observacaojustificativa=?, ")
				.append("disciplinaFazParteComposicao=?, ")
				.append("reposicao=?, ")
				.append("inclusaoForaPrazo=?, ")
				.append("modalidadeDisciplina = ?, ")
				.append("conteudo=?, ")
				.append("permiteEscolherModalidade = ?, ")
						// 	novos campos
				.append("disciplinaPorEquivalencia=?, ")
				.append("gradeDisciplinaComposta=?, ")
				.append("disciplinaEmRegimeEspecial=?, ")
				.append("disciplinaOptativa=?, ")
				.append("disciplinaReferenteAUmGrupoOptativa=?, ")
				.append("gradeCurricularGrupoOptativaDisciplina=?, ")
				.append("gradeDisciplina=?, ")
				.append("mapaEquivalenciaDisciplina=?, ")
				.append("mapaEquivalenciaDisciplinaCursada=?, ")
				.append("transferenciaMatrizCurricularMatricula=?,")
				.append("disciplinaCursandoPorCorrespondenciaAposTransferencia=?, ")
				.append("turmaTeorica=?, ")
				.append("turmaPratica=? , ")
				.append("liberadaSemDisponibilidadeVagas=?, ")
				.append("usuarioLiberadaSemDisponibilidadeVagas=?, ")
				.append("dataLiberadaSemDisponibilidadeVagas=?,  ")
				.append("dataultimaalteracao=? ")
				.append("WHERE ((codigo = ?))").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		
		if(getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
				sqlAlterar.setInt(1, obj.getMatriculaPeriodo().intValue());
				if (obj.getTurma().getCodigo().intValue() != 0) {
					sqlAlterar.setInt(2, obj.getTurma().getCodigo().intValue());
				} else {
					sqlAlterar.setNull(2, 0);
				}
				sqlAlterar.setInt(3, obj.getDisciplina().getCodigo().intValue());
				sqlAlterar.setString(4, obj.getSemestre());
				sqlAlterar.setString(5, obj.getAno());
				sqlAlterar.setString(6, obj.getMatricula());
				sqlAlterar.setBoolean(7, obj.getDisciplinaIncluida());
				sqlAlterar.setBoolean(8, obj.getDisciplinaEquivale());
				sqlAlterar.setBoolean(9, obj.getDisciplinaComposta());
				sqlAlterar.setString(10, obj.getJustificativa());
				sqlAlterar.setString(11, obj.getObservacaoJustificativa());
				sqlAlterar.setBoolean(12, obj.getDisciplinaFazParteComposicao());
				sqlAlterar.setBoolean(13, obj.getReposicao());
				sqlAlterar.setBoolean(14, obj.getInclusaoForaPrazo());
				sqlAlterar.setString(15, obj.getModalidadeDisciplina().name());
				if (obj.getModalidadeDisciplina().equals(ModalidadeDisciplinaEnum.ON_LINE) && obj.getConteudo().getCodigo().intValue() != 0) {
					sqlAlterar.setInt(16, obj.getConteudo().getCodigo().intValue());
				} else {
					sqlAlterar.setNull(16, 0);
				}
				sqlAlterar.setBoolean(17, obj.getPermiteEscolherModalidade());
				// NOVOS CAMPOS
				sqlAlterar.setBoolean(18, obj.getDisciplinaPorEquivalencia());
				if (obj.getGradeDisciplinaCompostaVO().getCodigo().intValue() != 0) {
					sqlAlterar.setInt(19, obj.getGradeDisciplinaCompostaVO().getCodigo());
				} else {
					sqlAlterar.setNull(19, 0);
				}
				sqlAlterar.setBoolean(20, obj.getDisciplinaEmRegimeEspecial());
				sqlAlterar.setBoolean(21, obj.getDisciplinaOptativa());
				sqlAlterar.setBoolean(22, obj.getDisciplinaReferenteAUmGrupoOptativa());
				if (obj.getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo().intValue() != 0) {
					sqlAlterar.setInt(23, obj.getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo());
				} else {
					sqlAlterar.setNull(23, 0);
				}
				if (obj.getGradeDisciplinaVO().getCodigo().intValue() != 0) {
					sqlAlterar.setInt(24, obj.getGradeDisciplinaVO().getCodigo());
				} else {
					sqlAlterar.setNull(24, 0);
				}
				if (obj.getMapaEquivalenciaDisciplinaVOIncluir().getCodigo().intValue() != 0) {
					sqlAlterar.setInt(25, obj.getMapaEquivalenciaDisciplinaVOIncluir().getCodigo());
				} else {
					sqlAlterar.setNull(25, 0);
				}
				if (obj.getMapaEquivalenciaDisciplinaCursada().getCodigo().intValue() != 0) {
					sqlAlterar.setInt(26, obj.getMapaEquivalenciaDisciplinaCursada().getCodigo());
				} else {
					sqlAlterar.setNull(26, 0);
				}
				if (obj.getTransferenciaMatrizCurricularMatricula().getCodigo().intValue() != 0) {
					sqlAlterar.setInt(27, obj.getTransferenciaMatrizCurricularMatricula().getCodigo());
				} else {
					sqlAlterar.setNull(27, 0);
				}
				sqlAlterar.setBoolean(28, obj.getDisciplinaCursandoPorCorrespondenciaAposTransferencia());
				if (Uteis.isAtributoPreenchido(obj.getTurmaTeorica())) {
					sqlAlterar.setInt(29, obj.getTurmaTeorica().getCodigo());
				} else {
					sqlAlterar.setNull(29, 0);
				}
				if (Uteis.isAtributoPreenchido(obj.getTurmaPratica())) {
					sqlAlterar.setInt(30, obj.getTurmaPratica().getCodigo());
				} else {
					sqlAlterar.setNull(30, 0);
				}
				sqlAlterar.setBoolean(31, obj.getLiberadaSemDisponibilidadeVagas());
				if (Uteis.isAtributoPreenchido(obj.getUsuarioLiberadaSemDisponibilidadeVagas())) {
					sqlAlterar.setInt(32, obj.getUsuarioLiberadaSemDisponibilidadeVagas().getCodigo());
				} else {
					sqlAlterar.setNull(32, 0);
				}
				sqlAlterar.setTimestamp(33, Uteis.getDataJDBCTimestamp(new Date()));
				sqlAlterar.setTimestamp(34, Uteis.getDataJDBCTimestamp(new Date()));
				sqlAlterar.setInt(35, obj.getCodigo().intValue());
				return sqlAlterar;
			}
		})==0){
			incluir(obj, configuracaoFinanceiroVO, matriculaVO.getGradeCurricularAtual(), usuario);
			return;
		}
		getFacadeFactory().getHistoricoFacade().incluirHistorico(obj, matriculaPeriodoVO, matriculaVO, configuracaoFinanceiroVO, matriculaVO.getGradeCurricularAtual(), usuario);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarMatriculaPeriodoTurmaDisciplinaTransferenciaMatrizCurricular(final MatriculaPeriodoTurmaDisciplinaVO obj, UsuarioVO usuario) throws Exception {
		final StringBuilder sql = new StringBuilder("UPDATE MatriculaPeriodoTurmaDisciplina ")
				.append("set transferenciaMatrizCurricularMatricula=?, ")
				.append("disciplinaEquivale=?,  ")
				.append("mapaEquivalenciaDisciplina=?, ")
				.append("mapaEquivalenciaDisciplinaCursada=?, ")
				.append("disciplinaCursandoPorCorrespondenciaAposTransferencia=?, ")
				.append("dataultimaalteracao=? ")
				.append("WHERE ((codigo = ?))").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
				int i = 1;
				if (!obj.getTransferenciaMatrizCurricularMatricula().getCodigo().equals(0)) {
					sqlAlterar.setInt(i++, obj.getTransferenciaMatrizCurricularMatricula().getCodigo());
				} else {
					sqlAlterar.setNull(i++, 0);
				}
				sqlAlterar.setBoolean(i++, obj.getDisciplinaEquivale());
				if (!obj.getMapaEquivalenciaDisciplinaVOIncluir().getCodigo().equals(0)) {
					sqlAlterar.setInt(i++, obj.getMapaEquivalenciaDisciplinaVOIncluir().getCodigo());
				} else {
					sqlAlterar.setNull(i++, 0);
				}
				if (!obj.getMapaEquivalenciaDisciplinaCursada().getCodigo().equals(0)) {
					sqlAlterar.setInt(i++, obj.getMapaEquivalenciaDisciplinaCursada().getCodigo());
				} else {
					sqlAlterar.setNull(i++, 0);
				}
				sqlAlterar.setBoolean(i++, obj.getDisciplinaCursandoPorCorrespondenciaAposTransferencia());
				sqlAlterar.setTimestamp(i++, Uteis.getDataJDBCTimestamp(new Date()));
				sqlAlterar.setInt(i++, obj.getCodigo());
				return sqlAlterar;
			}
		});
	}
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarMatriculaPeriodoTurmaDisciplinaPorAlteracaoGradeCurricularCursoIntegral(Integer turma, Integer novaDisciplina, Integer disciplina, Integer gradeDisciplina, Integer gradeCurricularGrupoOptativaDisciplina, List<MatriculaPeriodoVO> listaMatriculaPeriodo, UsuarioVO usuario) throws Exception {		
		StringBuilder sqlStr = new StringBuilder("update MatriculaPeriodoTurmaDisciplina set dataultimaalteracao=now(), ");
		if(Uteis.isAtributoPreenchido(gradeDisciplina)){
			sqlStr.append(" gradeDisciplina = ").append(gradeDisciplina).append(" ,");
			sqlStr.append(" gradeCurricularGrupoOptativaDisciplina = null, ");
			sqlStr.append(" disciplinareferenteaumgrupooptativa = false, ");
		}
		if(Uteis.isAtributoPreenchido(gradeCurricularGrupoOptativaDisciplina)){
			sqlStr.append(" gradeCurricularGrupoOptativaDisciplina = ").append(gradeCurricularGrupoOptativaDisciplina).append(", ");	
			sqlStr.append(" gradeDisciplina = null, ");	
			sqlStr.append(" disciplinareferenteaumgrupooptativa = true, ");
		}
		sqlStr.append(" disciplina = ").append(novaDisciplina).append(" ");
		sqlStr.append(" where matriculaperiodo in (").append(UteisTexto.converteListaEntidadeCampoCodigoParaCondicaoIn(listaMatriculaPeriodo, "codigo")).append(")");
		sqlStr.append(" and turma = ").append(turma).append(" ");
		sqlStr.append(" and disciplina = ").append(disciplina).append(" ");
		sqlStr.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		getConexao().getJdbcTemplate().update(sqlStr.toString());
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarMatriculaPeriodoTurmaDisciplinaPorAlteracaoGradeCurricularCursoIntegralComReposicao(Integer mptd, Integer novaDisciplina, Integer gradeDisciplina, Integer gradeCurricularGrupoOptativaDisciplina, Integer mapaEquivalenciaDisciplina, Integer mapaEquivalenciaDisciplinaCursada, boolean disciplinaPorEquivalencia, UsuarioVO usuario) throws Exception {		
		StringBuilder sqlStr = new StringBuilder("update MatriculaPeriodoTurmaDisciplina set  ");
		if(Uteis.isAtributoPreenchido(gradeDisciplina)){
			sqlStr.append(" gradeDisciplina = ").append(gradeDisciplina).append(" ,");
			sqlStr.append(" gradeCurricularGrupoOptativaDisciplina = null, ");
			sqlStr.append(" disciplinareferenteaumgrupooptativa = false, ");
		}
		if(Uteis.isAtributoPreenchido(gradeCurricularGrupoOptativaDisciplina)){
			sqlStr.append(" gradeCurricularGrupoOptativaDisciplina = ").append(gradeCurricularGrupoOptativaDisciplina).append(", ");	
			sqlStr.append(" gradeDisciplina = null, ");	
			sqlStr.append(" disciplinareferenteaumgrupooptativa = true, ");
		}
		if(Uteis.isAtributoPreenchido(novaDisciplina)){
			sqlStr.append(" disciplina = ").append(novaDisciplina).append(", ");
		}
		if(Uteis.isAtributoPreenchido(mapaEquivalenciaDisciplina)){
			sqlStr.append(" mapaequivalenciadisciplina = ").append(mapaEquivalenciaDisciplina).append(", ");	
			sqlStr.append(" mapaequivalenciadisciplinacursada = ").append(mapaEquivalenciaDisciplinaCursada).append(", ");	
			sqlStr.append(" disciplinaporequivalencia = ").append(disciplinaPorEquivalencia).append(", ");
		}
		sqlStr.append(" dataultimaalteracao=now() ");
		sqlStr.append(" where codigo = ").append(mptd).append(" ");
		sqlStr.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		getConexao().getJdbcTemplate().update(sqlStr.toString());
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarDataUltimaAlteracao_Online(String matricula, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder("update MatriculaPeriodoTurmaDisciplina set dataultimaalteracao=now() where matriculaperiodoturmadisciplina.modalidadedisciplina = 'ON_LINE' ");
		sqlStr.append(" and matricula = '").append(matricula).append("' ");
		sqlStr.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		getConexao().getJdbcTemplate().update(sqlStr.toString());
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void registrarAlunoRecebeuNotificacaoDownloadMaterial(final Integer matriculaperiodo, final Integer disciplina, final Integer turma) throws Exception {
		final String sql = "UPDATE MatriculaPeriodoTurmaDisciplina set notificacaoDownloadMaterialEnviado=?, dataultimaalteracao=?  WHERE ((matriculaperiodo = ? AND disciplina = ? AND turma = ?))";
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				sqlAlterar.setBoolean(1, true);
				sqlAlterar.setTimestamp(2, Uteis.getDataJDBCTimestamp(new Date()));
				sqlAlterar.setInt(3, matriculaperiodo);
				sqlAlterar.setInt(4, disciplina);
				sqlAlterar.setInt(5, turma);
				return sqlAlterar;
			}
		});
	}
	
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	@Override
	public void alterarConteudoMatriculaPeriodoTurmaDisciplinaComConteudo(final Integer conteudo , final  Integer  matriculaperiodoTurmaDisciplina, UsuarioVO usuario) throws Exception {
		String logAlteracao = " Matricula: "+matriculaperiodoTurmaDisciplina+"; Excluido: ; Registro de Acesso ao Conteudo , Calendários do EAD , Avaliações On-line; na data de ;"+UteisData.getDataAtualAplicandoFormatacao("dd/MM/yyyy")+";   usuario "+usuario.getNome_Apresentar()+";";
		final String sql = "UPDATE MatriculaPeriodoTurmaDisciplina set conteudo=?, dataultimaalteracao=? , logAlteracao=?  WHERE codigo = ? " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				sqlAlterar.setInt(1, conteudo);
				sqlAlterar.setTimestamp(2, Uteis.getDataJDBCTimestamp(new Date()));
				sqlAlterar.setString(3, logAlteracao);		
				sqlAlterar.setInt(4, matriculaperiodoTurmaDisciplina);				
				return sqlAlterar;
			}
		});
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	@Override
	public ConteudoVO registrarVinculoMatriculaPeriodoTurmaDisciplinaComConteudo(final Integer matriculaperiodoTurmaDisciplina, final Integer disciplina, UsuarioVO usuario) throws Exception {
		final ConteudoVO conteudo = getFacadeFactory().getConteudoFacade().consultarConteudoAtivoPorCodigoDisciplina(disciplina, NivelMontarDados.TODOS, false, usuario);
		if (!conteudo.isNovoObj()) {
			final String sql = "UPDATE MatriculaPeriodoTurmaDisciplina set conteudo=?, dataultimaalteracao=? WHERE codigo = ? " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setInt(1, conteudo.getCodigo());
					sqlAlterar.setTimestamp(2, Uteis.getDataJDBCTimestamp(new Date()));
					sqlAlterar.setInt(3, matriculaperiodoTurmaDisciplina);
					return sqlAlterar;
				}
			});
		}
		return conteudo;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarVinculoTurmaSemGerarHistorico(final MatriculaPeriodoTurmaDisciplinaVO obj) throws Exception {
		final String sql = "UPDATE MatriculaPeriodoTurmaDisciplina set notificacaoDownloadMaterialEnviado=?, dataultimaalteracao=? WHERE ((codigo = ?))";
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				if (obj.getTurma().getCodigo().intValue() != 0) {
					sqlAlterar.setInt(1, obj.getTurma().getCodigo().intValue());
				} else {
					sqlAlterar.setNull(1, 0);
				}
				sqlAlterar.setTimestamp(2, Uteis.getDataJDBCTimestamp(new Date()));
				sqlAlterar.setInt(3, obj.getCodigo().intValue());
				return sqlAlterar;
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.academico.MatriculaPeriodoTurmaDisciplinaInterfaceFacade
	 * #excluir(negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO)
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(String matricula, Integer codigoDisciplina) throws Exception {
		MatriculaPeriodoTurmaDisciplina.excluir(getIdEntidade());
		String sql = "DELETE FROM MatriculaPeriodoTurmaDisciplina WHERE (matricula = ? and disciplina = ?)";
		getConexao().getJdbcTemplate().update(sql, new Object[] { matricula, codigoDisciplina });
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(MatriculaPeriodoTurmaDisciplinaVO obj, boolean validarAcesso, UsuarioVO usuario) throws Exception {
		MatriculaPeriodoTurmaDisciplina.excluir(getIdEntidade(), validarAcesso, usuario);
//		getFacadeFactory().getFrequenciaAulaFacade().excluirFrequenciaAulaPorMatriculaPeriodoTurmaDisciplina(obj.getCodigo(), obj.getMatricula(), validarAcesso, usuario);
		String sql = "DELETE FROM MatriculaPeriodoTurmaDisciplina WHERE (codigo = ?)" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoPadraoSistema();
		getFacadeFactory().getIntegracaoMestreGRInterfaceFacade().verificarAlunoDisciplinaDelete(obj, usuario, configuracaoGeralSistemaVO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.academico.MatriculaPeriodoTurmaDisciplinaInterfaceFacade
	 * #consultarPorDisciplina(java.lang.Integer, boolean, int)
	 */
	public List consultarPorDisciplina(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM MatriculaPeriodoTurmaDisciplina WHERE disciplina = " + valorConsulta.intValue() + " ORDER BY disciplina";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List consultarPorMatriculaEDisciplina(String matricula, Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM MatriculaPeriodoTurmaDisciplina WHERE matricula='" + matricula + "' and disciplina = " + valorConsulta.intValue() + " ORDER BY disciplina";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public MatriculaPeriodoTurmaDisciplinaVO consultarPorMatriculaDisciplina(String matricula, Integer codigoDisciplina, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM MatriculaPeriodoTurmaDisciplina WHERE matricula='" + matricula + "' and disciplina = " + codigoDisciplina.intValue() + " ORDER BY disciplina";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (!tabelaResultado.next()) {
			return new MatriculaPeriodoTurmaDisciplinaVO();
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<MatriculaPeriodoTurmaDisciplinaVO> consultaRapidaPorMatriculaDisciplinaSemRegistro(TurmaVO turmaVO, List<TurmaDisciplinaVO> listaTurmaDisciplinaVO, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("select mtd.matricula, mtd.disciplina  from ( ");
		sqlStr.append("select t.matricula, g.disciplina from ( ");
		sqlStr.append("select distinct mp.matricula from matriculaperiodo mp ");
		sqlStr.append("where mp.turma = ").append(turmaVO.getCodigo()).append(") as t ");
		sqlStr.append(",(select distinct disciplina from turmadisciplina ");
		sqlStr.append("where turma = ").append(turmaVO.getCodigo()).append(") as g) as mtd ");
		sqlStr.append("where mtd.matricula || mtd.disciplina not in ( ");
		sqlStr.append("select matriculaPeriodoTurmaDisciplina.matricula || matriculaPeriodoTurmaDisciplina.disciplina from matriculaPeriodoTurmaDisciplina ");
		sqlStr.append("WHERE matriculaPeriodoTurmaDisciplina.matricula in (");
		if (!listaTurmaDisciplinaVO.isEmpty()) {
			sqlStr.append(" SELECT MatriculaPeriodo.matricula FROM MatriculaPeriodo ");
			sqlStr.append("INNER JOIN Matricula ON (MatriculaPeriodo.matricula = Matricula.matricula) ");
			sqlStr.append("WHERE MatriculaPeriodo.turma = ").append(turmaVO.getCodigo());
			if (turmaVO.getUnidadeEnsino().getCodigo() != null && !turmaVO.getUnidadeEnsino().getCodigo().equals(0)) {
				sqlStr.append(" AND matricula.unidadeensino = ").append(turmaVO.getUnidadeEnsino().getCodigo());
			}
			sqlStr.append(" AND matricula.situacao = 'AT' AND matriculaPeriodo.situacaoMatriculaPeriodo in ('AT', 'PR') )");
			sqlStr.append(" AND matriculaPeriodoTurmaDisciplina.disciplina in ( ");
			for (TurmaDisciplinaVO turmaDisciplinaVO : listaTurmaDisciplinaVO) {
				sqlStr.append("'").append(turmaDisciplinaVO.getDisciplina().getCodigo()).append("' ");
				if (!turmaDisciplinaVO.getDisciplina().getCodigo().equals(listaTurmaDisciplinaVO.get(listaTurmaDisciplinaVO.size() - 1).getDisciplina().getCodigo())) {
					sqlStr.append(", ");
				} else {
					sqlStr.append(") ");
				}
			}
			sqlStr.append(") ");
			sqlStr.append("group by mtd.matricula, mtd.disciplina ");
			sqlStr.append("order by mtd.matricula, mtd.disciplina");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			return montarDadosconsultaRapidaPorMatriculaDisciplinaSemRegistro(tabelaResultado);
		} else {
			return new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>(0);
		}
	}

	public List<MatriculaPeriodoTurmaDisciplinaVO> montarDadosconsultaRapidaPorMatriculaDisciplinaSemRegistro(SqlRowSet tabelaResultado) throws Exception {
		List<MatriculaPeriodoTurmaDisciplinaVO> vetResultado = new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>(0);
		while (tabelaResultado.next()) {
			MatriculaPeriodoTurmaDisciplinaVO obj = new MatriculaPeriodoTurmaDisciplinaVO();
			// montarDadosBasico(obj, tabelaResultado);
			obj.setMatricula(tabelaResultado.getString("matricula"));
			obj.getDisciplina().setCodigo(tabelaResultado.getInt("disciplina"));
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.academico.MatriculaPeriodoTurmaDisciplinaInterfaceFacade
	 * #consultarPorMatriculaAtiva(java.lang.String, boolean, int)
	 */
	public List<MatriculaPeriodoTurmaDisciplinaVO> consultarPorMatriculaAtiva(String matricula, boolean controlarAcesso, int nivelMontarDados, boolean ordernarConsiderandoAnoSemestreAtual, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("select distinct matriculaPeriodoTurmaDisciplina.* ");
		sqlStr.append(" from matricula ");
		sqlStr.append(" inner join matriculaPeriodo on  matricula.matricula = matriculaPeriodo.matricula ");
		sqlStr.append(" and matriculaPeriodo.codigo = (select mp.codigo from matriculaperiodo mp where   matricula.matricula = mp.matricula and mp.situacaoMatriculaPeriodo in ('AT', 'CO')");
		if (ordernarConsiderandoAnoSemestreAtual) {
			sqlStr.append(" order by case when ((mp.ano <> '' and mp.semestre <> '' and mp.ano = '").append(Uteis.getAnoDataAtual()).append("' ");
			sqlStr.append(" and mp.semestre = '").append(Uteis.getSemestreAtual()).append("') or (mp.ano <> '' and mp.semestre = '' and mp.ano = '").append(Uteis.getAnoDataAtual()).append("')) then 1");
			sqlStr.append(" else 0 end desc, (mp.ano ||'/'|| mp.semestre) desc limit 1) ");
		} else {
			sqlStr.append(" order by mp.ano||'/'||mp.semestre desc, mp.codigo desc limit 1)");
		}
		sqlStr.append(" inner join matriculaPeriodoTurmaDisciplina on matriculaPeriodoTurmaDisciplina.matriculaPeriodo = MatriculaPeriodo.codigo");
		sqlStr.append(" where matriculaPeriodo.situacaoMatriculaPeriodo in ('AT', 'CO') and matricula.matricula = '" + matricula + "' ");
		// sqlStr.append(" AND ( ( matriculaPeriodo.ano =   CAST((SELECT EXTRACT (YEAR FROM (SELECT NOW()))) AS TEXT )  ");
		// sqlStr.append(" AND matriculaPeriodo.semestre =  CAST((SELECT CASE WHEN ( SELECT EXTRACT (MONTH FROM (SELECT NOW())) > 7 ) THEN '2' ELSE '1' END) as TEXT ) ");
		// sqlStr.append(" ) OR (( matriculaPeriodo.ano = '' or matriculaPeriodo.ano is null) and (matriculaPeriodo.semestre = '' or matriculaPeriodo.semestre is null))) ");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<MatriculaPeriodoTurmaDisciplinaVO> consultarTurmasAlunoPorMatriculaAtiva(String matricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("select distinct matriculaPeriodoTurmaDisciplina.turma");
		sqlStr.append(" from matricula  inner join matriculaPeriodo on  matricula.matricula = matriculaPeriodo.matricula ");
		sqlStr.append(" inner join matriculaPeriodoTurmaDisciplina on matriculaPeriodoTurmaDisciplina.matriculaPeriodo = MatriculaPeriodo.codigo");
		sqlStr.append(" where matriculaPeriodo.situacaoMatriculaPeriodo='AT' and matricula.matricula = '" + matricula + "'");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<MatriculaPeriodoTurmaDisciplinaVO> consultarPeriodoLetivoMatriculaAlunoPorMatriculaAtiva(String matricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("select distinct matriculaPeriodo.periodoLetivoMatricula");
		sqlStr.append(" from matricula  inner join matriculaPeriodo on  matricula.matricula = matriculaPeriodo.matricula ");
		sqlStr.append(" inner join matriculaPeriodoTurmaDisciplina on matriculaPeriodoTurmaDisciplina.matriculaPeriodo = MatriculaPeriodo.codigo");
		sqlStr.append(" where matriculaPeriodo.situacaoMatriculaPeriodo='AT' and matricula.matricula = '" + matricula + "'");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.academico.MatriculaPeriodoTurmaDisciplinaInterfaceFacade
	 * #consultarPorIdentificadorTurmaTurma(java.lang.String, boolean, int)
	 */
	public List consultarPorIdentificadorTurmaTurma(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT MatriculaPeriodoTurmaDisciplina.* FROM MatriculaPeriodoTurmaDisciplina, Turma WHERE MatriculaPeriodoTurmaDisciplina.turma = Turma.codigo and upper( Turma.identificadorTurma ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY Turma.identificadorTurma";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.academico.MatriculaPeriodoTurmaDisciplinaInterfaceFacade
	 * #consultarPorCodigoTurmaDisciplinaSemestreAno(java.lang.Integer,
	 * java.lang.Integer, java.lang.Boolean, java.lang.String, java.lang.String,
	 * boolean, int)
	 */
	public List<MatriculaPeriodoTurmaDisciplinaVO> consultarPorCodigoTurmaDisciplinaSemestreAno(TurmaVO turmaVO, Integer disciplina, String ano, String semestre, boolean diferentePendenteFinanceiramente, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("SELECT MatriculaPeriodoTurmaDisciplina.* FROM MatriculaPeriodoTurmaDisciplina ");
		sqlStr.append("INNER JOIN Disciplina ON (MatriculaPeriodoTurmaDisciplina.disciplina = Disciplina.codigo) ");
		sqlStr.append("INNER JOIN Matricula ON (MatriculaPeriodoTurmaDisciplina.matricula = Matricula.matricula) ");
		sqlStr.append("INNER JOIN Pessoa ON (Matricula.aluno = Pessoa.codigo) ");
		sqlStr.append("INNER JOIN matriculaPeriodo on matriculaPeriodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo ");
		if (turmaVO.getSubturma()) {
			if (turmaVO.getTipoSubTurma().equals(TipoSubTurmaEnum.TEORICA)) {
				sqlStr.append("INNER JOIN Turma ON turma.codigo = MatriculaPeriodoTurmaDisciplina.turmaTeorica ");
			} else if (turmaVO.getTipoSubTurma().equals(TipoSubTurmaEnum.PRATICA)) {
				sqlStr.append("INNER JOIN Turma ON turma.codigo = MatriculaPeriodoTurmaDisciplina.turmaPratica ");
			} else {
				sqlStr.append("INNER JOIN Turma ON MatriculaPeriodoTurmaDisciplina.turma = turma.codigo ");
			}
		} else {
			sqlStr.append("INNER JOIN Turma ON MatriculaPeriodoTurmaDisciplina.turma = turma.codigo ");
		}
		sqlStr.append(" WHERE ");
		if (turmaVO.getTurmaAgrupada()) {
			sqlStr.append(" Turma.codigo  in ( select turma from turmaAgrupada where turmaOrigem =  " + turmaVO.getCodigo() + ")");
		} else {
			sqlStr.append(" Turma.codigo  = " + turmaVO.getCodigo());
		}
		
		
		
		sqlStr.append(" and Disciplina.codigo = " + disciplina.intValue());
		if (ano != null && !ano.equals("")) {
			sqlStr.append(" and ( MatriculaPeriodoTurmaDisciplina.ano = '" + ano + "' or MatriculaPeriodoTurmaDisciplina.ano = '' or MatriculaPeriodoTurmaDisciplina.ano is null) ");
		}
		if (semestre != null && !semestre.equals("")) {
			sqlStr.append(" and (MatriculaPeriodoTurmaDisciplina.semestre = '" + semestre + "' or MatriculaPeriodoTurmaDisciplina.semestre = '' or MatriculaPeriodoTurmaDisciplina.semestre is null) ");
		}
		if (diferentePendenteFinanceiramente) {
			sqlStr.append(" and matriculaperiodo.situacao <> 'PF'");
		}
		sqlStr.append(" ORDER BY Pessoa.nome");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.academico.MatriculaPeriodoTurmaDisciplinaInterfaceFacade
	 * #consultarPorCodigoTurmaDisciplinaSemestreAno(java.lang.Integer,
	 * java.lang.Integer, java.lang.Boolean, java.lang.String, java.lang.String,
	 * boolean, int)
	 */
	public List<MatriculaPeriodoTurmaDisciplinaVO> consultarPorCodigoTurmaDisciplinaSemestreAnoFiltroVisaoProfessor(TurmaVO turmaVO, Integer disciplina, String ano, String semestre, String situacaoMatricula, boolean diferenteAprovadoPorAproveitamento, Boolean apenasAlunosAtivos, Boolean trazerAlunosPendentesFinanceiramente, String filtroTipoCursoAluno, String tipoLayout, String tipoAluno, FiltroRelatorioAcademicoVO filtroAcademicoVO, String mes, String anoMes, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, Date dataInicioPeriodoMatricula, Date dataFimPeriodoMatricula, Map<String, FrequenciaAulaVO> frequenciaAulaGerar, Boolean trazerAlunoTransferencia, boolean permitirRealizarLancamentoAlunosPreMatriculados) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("SELECT distinct MatriculaPeriodoTurmaDisciplina.matricula as \"MatriculaPeriodoTurmaDisciplina.matricula\", ");
		sqlStr.append(" MatriculaPeriodoTurmaDisciplina.turma as \"MatriculaPeriodoTurmaDisciplina.turma\", ");
		sqlStr.append(" MatriculaPeriodoTurmaDisciplina.turmateorica as \"MatriculaPeriodoTurmaDisciplina.turmateorica\", ");
		sqlStr.append(" MatriculaPeriodoTurmaDisciplina.turmapratica as \"MatriculaPeriodoTurmaDisciplina.turmapratica\", ");
		sqlStr.append(" MatriculaPeriodoTurmaDisciplina.permiteEscolherModalidade as \"MatriculaPeriodoTurmaDisciplina.permiteEscolherModalidade\", ");
		sqlStr.append(" MatriculaPeriodoTurmaDisciplina.conteudo as \"MatriculaPeriodoTurmaDisciplina.conteudo\", ");
		sqlStr.append(" MatriculaPeriodoTurmaDisciplina.modalidadeDisciplina as \"MatriculaPeriodoTurmaDisciplina.modalidadeDisciplina\", ");
		sqlStr.append(" MatriculaPeriodoTurmaDisciplina.matriculaperiodo as \"MatriculaPeriodoTurmaDisciplina.matriculaperiodo\", ");
		sqlStr.append(" MatriculaPeriodoTurmaDisciplina.bimestre as \"MatriculaPeriodoTurmaDisciplina.bimestre\", ");
		sqlStr.append(" MatriculaPeriodoTurmaDisciplina.semestre as \"MatriculaPeriodoTurmaDisciplina.semestre\", ");
		sqlStr.append(" MatriculaPeriodoTurmaDisciplina.ano as \"MatriculaPeriodoTurmaDisciplina.ano\", ");
		sqlStr.append(" MatriculaPeriodoTurmaDisciplina.codigo as \"MatriculaPeriodoTurmaDisciplina.codigo\", ");
		sqlStr.append(" MatriculaPeriodoTurmaDisciplina.disciplina as \"MatriculaPeriodoTurmaDisciplina.disciplina\", ");
		sqlStr.append(" MatriculaPeriodoTurmaDisciplina.disciplinaincluida as \"MatriculaPeriodoTurmaDisciplina.disciplinaincluida\", ");
		sqlStr.append(" MatriculaPeriodoTurmaDisciplina.disciplinaequivale as \"MatriculaPeriodoTurmaDisciplina.disciplinaequivale\", ");
		sqlStr.append(" MatriculaPeriodoTurmaDisciplina.disciplinaequivalente as \"MatriculaPeriodoTurmaDisciplina.disciplinaequivalente\", ");
		sqlStr.append(" MatriculaPeriodoTurmaDisciplina.disciplinaFazParteComposicao as \"MatriculaPeriodoTurmaDisciplina.disciplinaFazParteComposicao\", ");
		sqlStr.append(" MatriculaPeriodoTurmaDisciplina.justificativa as \"MatriculaPeriodoTurmaDisciplina.justificativa\", ");
		sqlStr.append(" MatriculaPeriodoTurmaDisciplina.observacaojustificativa as \"MatriculaPeriodoTurmaDisciplina.observacaojustificativa\", ");
		sqlStr.append(" MatriculaPeriodoTurmaDisciplina.inclusaoForaPrazo as \"MatriculaPeriodoTurmaDisciplina.inclusaoForaPrazo\", ");
		
		sqlStr.append(" MatriculaPeriodo.codigo as \"MatriculaPeriodo.codigo\", MatriculaPeriodo.data as \"MatriculaPeriodo.data\", MatriculaPeriodo.situacao as \"MatriculaPeriodo.situacao\", ");
		sqlStr.append(" MatriculaPeriodo.alunoTransferidoUnidade as \"MatriculaPeriodo.alunoTransferidoUnidade\",   ");
		sqlStr.append(" MatriculaPeriodo.situacaoMatriculaPeriodo as \"MatriculaPeriodo.situacaoMatriculaPeriodo\", MatriculaPeriodo.dataFechamentoMatriculaPeriodo as \"MatriculaPeriodo.dataFechamentoMatriculaPeriodo\", ");
		sqlStr.append(" MatriculaPeriodo.unidadeEnsinoCurso as \"MatriculaPeriodo.unidadeEnsinoCurso\", MatriculaPeriodo.processoMatricula as \"MatriculaPeriodo.processoMatricula\", MatriculaPeriodo.gradecurricular as \"MatriculaPeriodo.gradecurricular\", ");
		sqlStr.append(" MatriculaPeriodo.turma as \"MatriculaPeriodo.turma\", MatriculaPeriodo.periodoLetivoMatricula as \"MatriculaPeriodo.periodoLetivoMatricula\", ");
		sqlStr.append(" Matricula.gradeCurricularAtual as \"Matricula.gradeCurricularAtual\", Matricula.data as \"Matricula.data\", Matricula.matriculaSuspensa as \"Matricula.matriculaSuspensa\", Matricula.updated as \"Matricula.updated\", ");
		sqlStr.append(" Matricula.turno as \"Matricula.turno\", Matricula.gradeCurricularAtual AS \"Matricula.gradeCurricularAtual\", ");
		sqlStr.append(" Matricula.situacao as \"Matricula.situacao\", Matricula.situacaoFinanceira as \"Matricula.situacaoFinanceira\", ");
		sqlStr.append(" Pessoa.nome as \"Pessoa.nome\", sem_acentos(Pessoa.nome) as nomeSemAcento, Pessoa.codigo as \"Pessoa.codigo\", Pessoa.cpf as \"Pessoa.cpf\", ");
		
		sqlStr.append(" Turma.codigo as \"Turma.codigo\", Turma.semestral as \"Turma.semestral\", Turma.anual as \"Turma.anual\", Turma.identificadorturma as \"Turma.identificadorturma\", ");
		sqlStr.append(" Turma.turmaagrupada as \"Turma.turmaagrupada\", Turma.sala as \"Turma.sala\", Turma.gradeCurricular as \"Turma.gradeCurricular\", ");
		sqlStr.append(" Turma.chancela as \"Turma.chancela\", Turma.tipochancela as \"Turma.tipochancela\", Turma.porcentagemchancela as \"Turma.porcentagemchancela\", ");
		sqlStr.append(" Turma.valorfixochancela as \"Turma.valorfixochancela\", Turma.valorporaluno as \"Turma.valorporaluno\", Turma.qtdAlunosEstimado as \"Turma.qtdAlunosEstimado\", ");
		sqlStr.append(" Turma.custoMedioAluno as \"Turma.custoMedioAluno\", Turma.receitaMediaAluno as \"Turma.receitaMediaAluno\", chancela.instituicaoChanceladora as \"chancela.instituicaoChanceladora\", ");
		sqlStr.append(" Unidadeensino.codigo as \"Unidadeensino.codigo\" , Unidadeensino.nome as \"Unidadeensino.nome\", ");
		sqlStr.append(" Curso.codigo as \"Curso.codigo\", Curso.nome as \"Curso.nome\", Curso.periodicidade as \"Curso.periodicidade\", Curso.configuracaoAcademico as \"Curso.configuracaoAcademico\", Curso.nivelEducacional as \"Curso.nivelEducacional\", ");
		sqlStr.append(" Turno.codigo as \"Turno.codigo\", Turno.nome as \"Turno.nome\", ");
		sqlStr.append(" Periodoletivo.descricao as \"Periodoletivo.descricao\",Periodoletivo.codigo as \"Periodoletivo.codigo\",Periodoletivo.nomecertificacao as \"Periodoletivo.nomecertificacao\", pessoa.nome, ");
		sqlStr.append("  Historico.dataRegistro as dataRegistroHistorico, matriculaperiodo.datafechamentomatriculaperiodo as datafechamentomatriculaperiodo, MatriculaPeriodo.origemfechamentomatriculaperiodo as \"MatriculaPeriodo.origemfechamentomatriculaperiodo\"  ");
//		sqlStr.append("FROM matriculaperiodoturmadisciplina ");
		sqlStr.append(" FROM ( ");		
		sqlStr.append("select mptd.* from matriculaperiodoturmadisciplina mptd  where mptd.turma = ").append(turmaVO.getCodigo());
		sqlStr.append("	union all ");
		sqlStr.append("	select ");
		sqlStr.append("	mptd.* ");
		sqlStr.append("	from matriculaperiodoturmadisciplina mptd ");
		sqlStr.append("	where mptd.turma in ( select ta.turma from turmaAgrupada ta where ta.turmaOrigem = ").append(turmaVO.getCodigo()).append(")");
		sqlStr.append("	union all ");		
		sqlStr.append("	select mptd.* from matriculaperiodoturmadisciplina mptd  where mptd.turmapratica = ").append(turmaVO.getCodigo());
		sqlStr.append("	union all ");		
		sqlStr.append("	select ");
		sqlStr.append("	 mptd.* ");
		sqlStr.append("	from matriculaperiodoturmadisciplina mptd ");
		sqlStr.append("	where mptd.turmapratica in ( select ta.turma from turmaAgrupada ta where ta.turmaOrigem = ").append(turmaVO.getCodigo()).append(")");
		sqlStr.append("	union all ");
		sqlStr.append("	select mptd.* from matriculaperiodoturmadisciplina mptd where mptd.turmateorica = ").append(turmaVO.getCodigo());
		sqlStr.append("	union all ");
		sqlStr.append("	select ");
		sqlStr.append("	mptd.* ");
		sqlStr.append("	from matriculaperiodoturmadisciplina mptd ");
		sqlStr.append("	where mptd.turmateorica in (select ta.turma from turmaAgrupada ta where ta.turmaOrigem = ").append(turmaVO.getCodigo()).append(")");			
	    sqlStr.append(") AS matriculaperiodoturmadisciplina ");	    
	    sqlStr.append( "INNER JOIN Historico 		ON  Historico.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo ");
		sqlStr.append(" INNER JOIN matricula 		ON (matriculaperiodoturmadisciplina.matricula = matricula.matricula) ");
		sqlStr.append(" INNER JOIN curso     		ON (matricula.curso                           = curso.codigo) ");
		sqlStr.append(" INNER JOIN matriculaPeriodo ON matriculaPeriodo.codigo             		  = matriculaperiodoturmadisciplina.matriculaperiodo ");
		sqlStr.append(" AND (curso.periodicidade = 'IN' OR (curso.periodicidade in ('AN', 'SE') AND matriculaperiodo.codigo in (");
		sqlStr.append("SELECT mp.codigo FROM matriculaperiodo mp ");		
		sqlStr.append(" WHERE mp.matricula = Matricula.matricula ");		
		sqlStr.append(" AND mp.ano = MatriculaPeriodoTurmaDisciplina.ano ");
		sqlStr.append(" AND mp.semestre = MatriculaPeriodoTurmaDisciplina.semestre ");			
		sqlStr.append(" ORDER BY (mp.ano ||'/'|| mp.semestre) desc, case when mp.situacaoMatriculaPeriodo in ('AT', 'PR', 'FI', 'FO') then 1 else 2 end, mp.codigo desc limit 1))) ");
		if (turmaVO.getSubturma()) {
			if (turmaVO.getTipoSubTurma().equals(TipoSubTurmaEnum.TEORICA)) {
				sqlStr.append("INNER JOIN Turma ON turma.codigo = matriculaperiodoturmadisciplina.turmaTeorica ");
			} else if (turmaVO.getTipoSubTurma().equals(TipoSubTurmaEnum.PRATICA)) {
				sqlStr.append("INNER JOIN Turma ON turma.codigo = matriculaperiodoturmadisciplina.turmaPratica ");
			} else {
				sqlStr.append("INNER JOIN Turma ON matriculaperiodoturmadisciplina.turma = turma.codigo and MatriculaPeriodoTurmaDisciplina.turmaTeorica is null and MatriculaPeriodoTurmaDisciplina.turmaPratica is null ");
			}
		} else {
			sqlStr.append("INNER JOIN Turma ON matriculaperiodoturmadisciplina.turma = turma.codigo");
		}
		sqlStr.append(" INNER JOIN Disciplina 	  ON (matriculaperiodoturmadisciplina.disciplina = Disciplina.codigo) ");		
		sqlStr.append(" INNER JOIN Pessoa     	  ON (Matricula.aluno                            = Pessoa.codigo) ");
		sqlStr.append(" LEFT JOIN unidadeensino   ON turma.unidadeensino                       = unidadeensino.codigo ");
		sqlStr.append(" LEFT JOIN turno           ON turma.turno                               = turno.codigo ");
		sqlStr.append(" LEFT JOIN periodoletivo   ON turma.periodoletivo                       = periodoletivo.codigo ");
		sqlStr.append(" LEFT JOIN gradecurricular ON turma.gradecurricular                     = gradecurricular.codigo ");
		sqlStr.append(" LEFT JOIN chancela        ON turma.chancela                            = chancela.codigo ");
		
		if (tipoAluno.equals("normal")) {
			sqlStr.append(" WHERE matriculaperiodoturmadisciplina.turma = matriculaperiodo.turma ");
		} else if (tipoAluno.equals("reposicao")) {
			sqlStr.append(" WHERE matriculaperiodoturmadisciplina.turma <> matriculaperiodo.turma ");
		} else {
			sqlStr.append(" WHERE 1 = 1 ");
		}
		if (turmaVO.getTurmaAgrupada() && !turmaVO.getSubturma()) {
			sqlStr.append(" and ((turma.codigo = ").append(turmaVO.getCodigo()).append(" or turma.codigo in (select turma from turmaAgrupada where turmaOrigem =  ").append(turmaVO.getCodigo()).append("))");
			sqlStr.append("or (MatriculaPeriodoTurmaDisciplina.turmaPratica in (select TurmaAgrupada.turma from TurmaAgrupada inner join turma as turmaOrigem on turmaOrigem.codigo = TurmaAgrupada.turmaOrigem inner join turma on turma.codigo = turmaagrupada.turma where turmaOrigem.codigo = ").append(turmaVO.getCodigo()).append(" and turmaOrigem.subturma = false and turma.tiposubturma = 'PRATICA'))");
			sqlStr.append("or (MatriculaPeriodoTurmaDisciplina.turmaTeorica in (select TurmaAgrupada.turma from TurmaAgrupada inner join turma as turmaOrigem on turmaOrigem.codigo = TurmaAgrupada.turmaOrigem inner join turma on turma.codigo = turmaagrupada.turma where turmaOrigem.codigo = ").append(turmaVO.getCodigo()).append(" and turmaOrigem.subturma = false and turma.tiposubturma = 'TEORICA'))");
			sqlStr.append(") ");
		} else {
			sqlStr.append(" and Turma.codigo = ").append(turmaVO.getCodigo());
		}
		if (!turmaVO.getSubturma() && !turmaVO.getTurmaAgrupada()) {
			sqlStr.append(" and MatriculaPeriodoTurmaDisciplina.turmaPratica is null and MatriculaPeriodoTurmaDisciplina.turmaTeorica is null ");
		}
		if (turmaVO.getTurmaAgrupada()) {
			sqlStr.append(" and (Disciplina.codigo = ").append(disciplina.intValue());
			sqlStr.append(" or Disciplina.codigo in (select disciplinaequivalenteturmaagrupada from turmadisciplina where turmadisciplina.disciplina = ").append(disciplina.intValue());
			sqlStr.append(" and turmadisciplina.turma = ").append(turmaVO.getCodigo()).append(") ");
			sqlStr.append(" or Disciplina.codigo in (select equivalente from disciplinaequivalente where disciplinaequivalente.disciplina = ").append(disciplina.intValue()).append(") ");
			sqlStr.append(" or Disciplina.codigo in (select disciplina from disciplinaequivalente where disciplinaequivalente.equivalente = ").append(disciplina.intValue()).append(") ");
			sqlStr.append(" ) ");
		} else {
			sqlStr.append(" and Disciplina.codigo = ").append(disciplina.intValue());

		}
		
		
		if (diferenteAprovadoPorAproveitamento) {
			sqlStr.append(" and Historico.situacao not in ( 'AA', 'CC', 'CH', 'IS') ");
		}
		if (ano != null && !ano.equals("")) {
			sqlStr.append(" and ( MatriculaPeriodoTurmaDisciplina.ano = '").append(ano).append("' or MatriculaPeriodoTurmaDisciplina.ano = '') ");
		}
		if (semestre != null && !semestre.equals("")) {
			sqlStr.append(" and (MatriculaPeriodoTurmaDisciplina.semestre = '").append(semestre).append("' or MatriculaPeriodoTurmaDisciplina.semestre = '') ");
		}

		if (!situacaoMatricula.equals("")) {
			sqlStr.append(" and matricula.situacao = '").append(situacaoMatricula).append("' ");
		}
		
		if (apenasAlunosAtivos) {
			sqlStr.append(" and matricula.situacao not in ('CA', 'TR', 'CF') ");
			sqlStr.append("and matriculaPeriodo.situacaoMatriculaPeriodo not in ('PR','TR', 'PC', 'CA', 'TR') ");
		} else if ((usuario.getIsApresentarVisaoProfessor() || usuario.getIsApresentarVisaoCoordenador())) {
			sqlStr.append(" and matriculaperiodo.situacaoMatriculaPeriodo <> 'PC' ");
			if(!permitirRealizarLancamentoAlunosPreMatriculados) {
				sqlStr.append(" and matriculaperiodo.situacaoMatriculaPeriodo <> 'PR' ");
			}
		}
		if (!trazerAlunosPendentesFinanceiramente) {
			sqlStr.append(" and matriculaPeriodo.situacao <> 'PF' ");
		}

		if (filtroAcademicoVO != null) {
			sqlStr.append(" and ").append(adicionarFiltroSituacaoAcademicaMatriculaPeriodo(filtroAcademicoVO, "matriculaPeriodo"));
			sqlStr.append(" and ").append(adicionarFiltroSituacaoFinanceiraMatriculaPeriodo(filtroAcademicoVO, "matriculaPeriodo"));			
		}
		if (dataInicioPeriodoMatricula != null || dataFimPeriodoMatricula != null) {
			sqlStr.append(" and ").append(realizarGeracaoWherePeriodo(dataInicioPeriodoMatricula, dataFimPeriodoMatricula, "matriculaperiodo.data", false));
		}
		if (filtroTipoCursoAluno != null && !filtroTipoCursoAluno.equals("") && !filtroTipoCursoAluno.equals("todos") && !filtroTipoCursoAluno.equals("posGraduacao")) {
			sqlStr.append(" AND matricula.tipoMatricula = '").append(filtroTipoCursoAluno).append("'");
		}
		if (tipoAluno.equals("normal")) {
			sqlStr.append(" and ((MatriculaPeriodoTurmaDisciplina.disciplinaIncluida <> true or MatriculaPeriodoTurmaDisciplina.disciplinaIncluida is null)");
			sqlStr.append(" or (MatriculaPeriodoTurmaDisciplina.disciplinaIncluida = true and matriculaPeriodo.turma = MatriculaPeriodoTurmaDisciplina.turma))");
		} else if (tipoAluno.equals("reposicao")) {
			sqlStr.append(" and matriculaPeriodo.turma <> MatriculaPeriodoTurmaDisciplina.turma ");
		}
		if (frequenciaAulaGerar != null && !frequenciaAulaGerar.isEmpty()) {
			sqlStr.append("AND matricula.matricula not in(");
			boolean virgula = false;
			for (String matriculaFrequencia : frequenciaAulaGerar.keySet()) {
				if (!virgula) {
					sqlStr.append(" '").append(matriculaFrequencia).append("' ");
					virgula = true;
				} else {
					sqlStr.append(", '").append(matriculaFrequencia).append("' ");
				}
			}
			sqlStr.append(") ");
		}
		/**
		 * Adicionada regra para resolver impactos relacionados a alunos que estão Cursando por Correspondência e que disciplinas saiam duplicadas no
		 * Boletim Acadêmico
		 */
		if (!trazerAlunoTransferencia) {
			sqlStr.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));
		}
		
		SqlRowSet dadosSQL = null;
		if (turmaVO.getTurmaAgrupada() && !turmaVO.getSubturma()) {
			StringBuffer sql = new StringBuffer();			
			sql.append("select t.* from ");
			sql.append("(").append(sqlStr).append(") as t ");
			sql.append(" where  (exists (").append(sqlStr).append(" and historico.disciplina = t.\"MatriculaPeriodoTurmaDisciplina.disciplina\" and historico.disciplina = ").append(disciplina).append(" and historico.matricula = t.\"MatriculaPeriodoTurmaDisciplina.matricula\" limit 1) ") ;
			sql.append(" or not exists (").append(sqlStr).append(" and historico.disciplina = ").append(disciplina).append(" and historico.matricula = t.\"MatriculaPeriodoTurmaDisciplina.matricula\" limit 1)) ") ;
			sql.append("  order by t.nomeSemAcento ");			
			dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
			
		}else {		
			sqlStr.append(" ORDER BY nomeSemAcento");
			dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		}
		return montarDadosConsultaRapida(dadosSQL, usuario);
		}

	public List<MatriculaPeriodoTurmaDisciplinaVO> consultaRapidaPorCodigoTurmaDisciplinaSemestreAnoFiltroVisaoProfessor(List<RegistroAulaVO> registroAulaVOs, Integer disciplina, String ano, String semestre, String situacaoMatricula, boolean diferenteAprovadoPorAproveitamento, Boolean apenasAlunosAtivos, Boolean trazerAlunosPendentesFinanceiramente, String filtroTipoCursoAluno, String tipoLayout, boolean controlarAcesso, UsuarioVO usuario, TurmaVO turmaVO, boolean trazerAlunosTransferenciaMatriz, boolean permitirRealizarLancamentoAlunosPreMatriculados) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("SELECT distinct MatriculaPeriodoTurmaDisciplina.matricula as \"MatriculaPeriodoTurmaDisciplina.matricula\", ");
		sqlStr.append(" MatriculaPeriodoTurmaDisciplina.turma as \"MatriculaPeriodoTurmaDisciplina.turma\", ");
		sqlStr.append(" MatriculaPeriodoTurmaDisciplina.turmateorica as \"MatriculaPeriodoTurmaDisciplina.turmateorica\", ");
		sqlStr.append(" MatriculaPeriodoTurmaDisciplina.turmapratica as \"MatriculaPeriodoTurmaDisciplina.turmapratica\", ");
		sqlStr.append(" MatriculaPeriodoTurmaDisciplina.permiteEscolherModalidade as \"MatriculaPeriodoTurmaDisciplina.permiteEscolherModalidade\", ");
		sqlStr.append(" MatriculaPeriodoTurmaDisciplina.conteudo as \"MatriculaPeriodoTurmaDisciplina.conteudo\", ");
		sqlStr.append(" MatriculaPeriodoTurmaDisciplina.modalidadeDisciplina as \"MatriculaPeriodoTurmaDisciplina.modalidadeDisciplina\", ");
		sqlStr.append(" MatriculaPeriodoTurmaDisciplina.matriculaperiodo as \"MatriculaPeriodoTurmaDisciplina.matriculaperiodo\", ");
		sqlStr.append(" MatriculaPeriodoTurmaDisciplina.semestre as \"MatriculaPeriodoTurmaDisciplina.semestre\", ");
		sqlStr.append(" MatriculaPeriodoTurmaDisciplina.bimestre as \"MatriculaPeriodoTurmaDisciplina.bimestre\", ");
		sqlStr.append(" MatriculaPeriodoTurmaDisciplina.ano as \"MatriculaPeriodoTurmaDisciplina.ano\", ");
		sqlStr.append(" MatriculaPeriodoTurmaDisciplina.codigo as \"MatriculaPeriodoTurmaDisciplina.codigo\", ");
		sqlStr.append(" MatriculaPeriodoTurmaDisciplina.disciplina as \"MatriculaPeriodoTurmaDisciplina.disciplina\", ");
		sqlStr.append(" MatriculaPeriodoTurmaDisciplina.disciplinaincluida as \"MatriculaPeriodoTurmaDisciplina.disciplinaincluida\", ");
		sqlStr.append(" MatriculaPeriodoTurmaDisciplina.disciplinaequivale as \"MatriculaPeriodoTurmaDisciplina.disciplinaequivale\", ");
		sqlStr.append(" MatriculaPeriodoTurmaDisciplina.disciplinaequivalente as \"MatriculaPeriodoTurmaDisciplina.disciplinaequivalente\", ");
		sqlStr.append(" MatriculaPeriodoTurmaDisciplina.disciplinaFazParteComposicao as \"MatriculaPeriodoTurmaDisciplina.disciplinaFazParteComposicao\", ");
		sqlStr.append(" MatriculaPeriodoTurmaDisciplina.justificativa as \"MatriculaPeriodoTurmaDisciplina.justificativa\", ");
		sqlStr.append(" MatriculaPeriodoTurmaDisciplina.observacaojustificativa as \"MatriculaPeriodoTurmaDisciplina.observacaojustificativa\", ");
		sqlStr.append(" MatriculaPeriodoTurmaDisciplina.inclusaoForaPrazo as \"MatriculaPeriodoTurmaDisciplina.inclusaoForaPrazo\", ");
		
		sqlStr.append(" MatriculaPeriodo.codigo as \"MatriculaPeriodo.codigo\", MatriculaPeriodo.data as \"MatriculaPeriodo.data\", MatriculaPeriodo.situacao as \"MatriculaPeriodo.situacao\", ");
		sqlStr.append(" MatriculaPeriodo.alunoTransferidoUnidade as \"MatriculaPeriodo.alunoTransferidoUnidade\",   ");
		sqlStr.append(" MatriculaPeriodo.situacaoMatriculaPeriodo as \"MatriculaPeriodo.situacaoMatriculaPeriodo\", MatriculaPeriodo.dataFechamentoMatriculaPeriodo as \"MatriculaPeriodo.dataFechamentoMatriculaPeriodo\", ");
		sqlStr.append(" MatriculaPeriodo.unidadeEnsinoCurso as \"MatriculaPeriodo.unidadeEnsinoCurso\", MatriculaPeriodo.processoMatricula as \"MatriculaPeriodo.processoMatricula\", MatriculaPeriodo.gradecurricular as \"MatriculaPeriodo.gradecurricular\", ");
		sqlStr.append(" MatriculaPeriodo.turma as \"MatriculaPeriodo.turma\", MatriculaPeriodo.periodoLetivoMatricula as \"MatriculaPeriodo.periodoLetivoMatricula\", ");
		sqlStr.append(" Matricula.gradeCurricularAtual as \"Matricula.gradeCurricularAtual\", Matricula.data as \"Matricula.data\", Matricula.matriculaSuspensa as \"Matricula.matriculaSuspensa\", Matricula.updated as \"Matricula.updated\", ");
		sqlStr.append(" Matricula.turno as \"Matricula.turno\", Matricula.gradeCurricularAtual AS \"Matricula.gradeCurricularAtual\", ");
		sqlStr.append(" Matricula.situacao as \"Matricula.situacao\", Matricula.situacaoFinanceira as \"Matricula.situacaoFinanceira\", ");
		sqlStr.append(" Pessoa.nome as \"Pessoa.nome\", sem_acentos(Pessoa.nome) as nomeSemAcento, Pessoa.codigo as \"Pessoa.codigo\", Pessoa.cpf as \"Pessoa.cpf\", ");
		
		sqlStr.append(" Turma.codigo as \"Turma.codigo\", Turma.semestral as \"Turma.semestral\", Turma.anual as \"Turma.anual\", Turma.identificadorturma as \"Turma.identificadorturma\", ");
		sqlStr.append(" Turma.turmaagrupada as \"Turma.turmaagrupada\", Turma.sala as \"Turma.sala\", Turma.gradeCurricular as \"Turma.gradeCurricular\", ");
		sqlStr.append(" Turma.chancela as \"Turma.chancela\", Turma.tipochancela as \"Turma.tipochancela\", Turma.porcentagemchancela as \"Turma.porcentagemchancela\", ");
		sqlStr.append(" Turma.valorfixochancela as \"Turma.valorfixochancela\", Turma.valorporaluno as \"Turma.valorporaluno\", Turma.qtdAlunosEstimado as \"Turma.qtdAlunosEstimado\", ");
		sqlStr.append(" Turma.custoMedioAluno as \"Turma.custoMedioAluno\", Turma.receitaMediaAluno as \"Turma.receitaMediaAluno\", chancela.instituicaoChanceladora as \"chancela.instituicaoChanceladora\", ");
		sqlStr.append(" Unidadeensino.codigo as \"Unidadeensino.codigo\" , Unidadeensino.nome as \"Unidadeensino.nome\", ");
		sqlStr.append(" Curso.codigo as \"Curso.codigo\", Curso.nome as \"Curso.nome\", Curso.periodicidade as \"Curso.periodicidade\", Curso.configuracaoAcademico as \"Curso.configuracaoAcademico\", Curso.nivelEducacional as \"Curso.nivelEducacional\", ");
		sqlStr.append(" Turno.codigo as \"Turno.codigo\", Turno.nome as \"Turno.nome\", ");
		sqlStr.append(" Periodoletivo.descricao as \"Periodoletivo.descricao\",Periodoletivo.codigo as \"Periodoletivo.codigo\",Periodoletivo.nomeCertificacao as \"Periodoletivo.nomeCertificacao\", pessoa.nome, ");
		sqlStr.append(" Historico.dataRegistro as dataRegistroHistorico, MatriculaPeriodo.origemfechamentomatriculaperiodo as \"MatriculaPeriodo.origemfechamentomatriculaperiodo\" ");
		sqlStr.append("FROM MatriculaPeriodoTurmaDisciplina ");
		if (turmaVO.getSubturma()) {
			if (turmaVO.getTipoSubTurma().equals(TipoSubTurmaEnum.TEORICA)) {
				sqlStr.append("INNER JOIN Turma ON turma.codigo = MatriculaPeriodoTurmaDisciplina.turmaTeorica ");
			} else if (turmaVO.getTipoSubTurma().equals(TipoSubTurmaEnum.PRATICA)) {
				sqlStr.append("INNER JOIN Turma ON turma.codigo = MatriculaPeriodoTurmaDisciplina.turmaPratica ");
			} else {
				sqlStr.append("INNER JOIN Turma ON MatriculaPeriodoTurmaDisciplina.turma = turma.codigo and MatriculaPeriodoTurmaDisciplina.turmaTeorica is null and MatriculaPeriodoTurmaDisciplina.turmaPratica is null ");
			}
		} else {
			sqlStr.append("INNER JOIN Turma ON MatriculaPeriodoTurmaDisciplina.turma = turma.codigo ");
		}
		sqlStr.append(" INNER JOIN Disciplina ON MatriculaPeriodoTurmaDisciplina.disciplina = Disciplina.codigo ");
		sqlStr.append(" INNER JOIN Matricula  ON MatriculaPeriodoTurmaDisciplina.matricula  = Matricula.matricula ");
		sqlStr.append(" INNER JOIN Pessoa     ON Matricula.aluno                            = Pessoa.codigo ");	
		sqlStr.append(" INNER JOIN curso      ON matricula.curso                            = curso.codigo ");
		sqlStr.append(" INNER JOIN matriculaperiodo ON matriculaperiodo.codigo              = MatriculaPeriodoTurmaDisciplina.matriculaperiodo ");
		sqlStr.append(" AND (curso.periodicidade = 'IN' OR (curso.periodicidade in ('AN', 'SE') AND matriculaperiodo.codigo in (");
		sqlStr.append(" SELECT mp.codigo FROM matriculaperiodo mp ");		
		sqlStr.append(" WHERE mp.matricula = Matricula.matricula ");		
		sqlStr.append(" AND mp.ano         = MatriculaPeriodoTurmaDisciplina.ano ");
		sqlStr.append(" AND mp.semestre    = MatriculaPeriodoTurmaDisciplina.semestre ");			
		sqlStr.append("ORDER BY (mp.ano ||'/'|| mp.semestre) DESC, CASE WHEN mp.situacaoMatriculaPeriodo IN ('AT', 'PR', 'FI', 'FO') THEN 1 ELSE 2 END, mp.codigo desc LIMIT 1))) ");
		sqlStr.append("LEFT JOIN Historico       ON Historico.matricula = matricula.matricula and Historico.MatriculaPeriodoTurmaDisciplina = MatriculaPeriodoTurmaDisciplina.codigo ");
		sqlStr.append("LEFT JOIN unidadeensino   ON turma.unidadeensino                       = unidadeensino.codigo ");
		sqlStr.append("LEFT JOIN turno           ON turma.turno                               = turno.codigo ");
		sqlStr.append("LEFT JOIN periodoletivo   ON turma.periodoletivo                       = periodoletivo.codigo ");
		sqlStr.append("LEFT JOIN gradecurricular ON turma.gradecurricular                     = gradecurricular.codigo ");
		sqlStr.append("LEFT JOIN chancela        ON turma.chancela                            = chancela.codigo ");
		if (turmaVO.getTurmaAgrupada() && !turmaVO.getSubturma()) {
			sqlStr.append(" WHERE ((MatriculaPeriodoTurmaDisciplina.turmaPratica is  null and MatriculaPeriodoTurmaDisciplina.turmaTeorica is null and turma.codigo in (select turma from turmaAgrupada where turmaOrigem =  ").append(turmaVO.getCodigo()).append("))");
			sqlStr.append("or (MatriculaPeriodoTurmaDisciplina.turmaPratica is not null and MatriculaPeriodoTurmaDisciplina.turmaPratica in (select TurmaAgrupada.turma from TurmaAgrupada inner join turma as turmaOrigem on turmaOrigem.codigo = TurmaAgrupada.turmaOrigem inner join turma on turma.codigo = turmaagrupada.turma where turmaOrigem.codigo = ").append(turmaVO.getCodigo()).append(" and turmaOrigem.subturma = false and turma.tiposubturma = 'PRATICA'))");
			sqlStr.append("or (MatriculaPeriodoTurmaDisciplina.turmaTeorica is not null and MatriculaPeriodoTurmaDisciplina.turmaTeorica in (select TurmaAgrupada.turma from TurmaAgrupada inner join turma as turmaOrigem on turmaOrigem.codigo = TurmaAgrupada.turmaOrigem inner join turma on turma.codigo = turmaagrupada.turma where turmaOrigem.codigo = ").append(turmaVO.getCodigo()).append(" and turmaOrigem.subturma = false and turma.tiposubturma = 'TEORICA'))");
			sqlStr.append(") ");
		} else {
			sqlStr.append(" WHERE Turma.codigo = ").append(turmaVO.getCodigo());
		}
		if (!turmaVO.getSubturma() && !turmaVO.getTurmaAgrupada()) {
			sqlStr.append(" and MatriculaPeriodoTurmaDisciplina.turmaPratica is null and MatriculaPeriodoTurmaDisciplina.turmaTeorica is null ");
		}
		if (turmaVO.getTurmaAgrupada()) {
			sqlStr.append(" and (Disciplina.codigo = ").append(disciplina.intValue());			
			sqlStr.append(" or Disciplina.codigo in (select distinct disciplinaequivalenteTurmaagrupada from turmadisciplina where turma = ").append(turmaVO.getCodigo()).append(" and turmadisciplina.disciplina = ").append(disciplina.intValue()).append(") ");
			sqlStr.append(" or Disciplina.codigo in (select distinct equivalente from disciplinaequivalente where disciplinaequivalente.disciplina = ").append(disciplina.intValue()).append(") ");
			sqlStr.append(" or Disciplina.codigo in (select distinct disciplina from disciplinaequivalente where disciplinaequivalente.equivalente = ").append(disciplina.intValue()).append(")) ");
		} else {
			sqlStr.append(" and Disciplina.codigo = ").append(disciplina.intValue());
		}
		
		sqlStr.append(" and Historico.situacao not in ('AA', 'CH', 'CC', 'IS') ");
		if (turmaVO.getCurso().getLiberarRegistroAulaEntrePeriodo()) {
			sqlStr.append(" and historico.situacao not in('TR', 'CA') ");
			// Alteração realizada com a supervisão do Rodrigo
			if (ano != null && !ano.equals("")) {
				sqlStr.append(" and ( MatriculaPeriodoTurmaDisciplina.ano = '").append(ano).append("')  ");
			}
			if (semestre != null && !semestre.equals("")) {
				sqlStr.append(" and (MatriculaPeriodoTurmaDisciplina.semestre = '").append(semestre).append("')  ");
			}
		} else {
			if (ano != null && !ano.equals("")) {
				sqlStr.append(" and ( MatriculaPeriodoTurmaDisciplina.ano = '").append(ano).append("')  ");
			}
			if (semestre != null && !semestre.equals("")) {
				sqlStr.append(" and (MatriculaPeriodoTurmaDisciplina.semestre = '").append(semestre).append("')  ");
			}
		}
		if (!situacaoMatricula.equals("")) {
			sqlStr.append(" and matricula.situacao = '").append(situacaoMatricula).append("' ");
		}

		if (apenasAlunosAtivos) {
			sqlStr.append(" and matriculaPeriodo.situacaoMatriculaPeriodo NOT IN ('PR','TR', 'PC', 'CA') ");
			if (!trazerAlunosPendentesFinanceiramente) {
				sqlStr.append(" and matriculaPeriodo.situacao <> 'PF' ");
			}
		} else {
			sqlStr.append(" and matriculaPeriodo.situacaoMatriculaPeriodo NOT IN ('PC'");
			if ((usuario.getIsApresentarVisaoProfessor() || usuario.getIsApresentarVisaoCoordenador()) && !permitirRealizarLancamentoAlunosPreMatriculados) {
				sqlStr.append(",'PR'");
			}
			sqlStr.append(")");
		}
		if (filtroTipoCursoAluno != null && !filtroTipoCursoAluno.equals("") && !filtroTipoCursoAluno.equals("todos")) {
			sqlStr.append(" AND matricula.tipoMatricula = '").append(filtroTipoCursoAluno).append("'");
		}
		if (tipoLayout != null && !tipoLayout.equals("") && tipoLayout.equals("EspelhoDiarioReposicaoRel")) {
			sqlStr.append(" and matriculaPeriodo.turma <> MatriculaPeriodoTurmaDisciplina.turma ");
		}
		if (tipoLayout != null && !tipoLayout.equals("") && tipoLayout.equals("EspelhoDiarioModRetratoRel")) {
			sqlStr.append(" and ((MatriculaPeriodoTurmaDisciplina.disciplinaIncluida <> true or MatriculaPeriodoTurmaDisciplina.disciplinaIncluida is null)");
			sqlStr.append(" or (MatriculaPeriodoTurmaDisciplina.disciplinaIncluida = true and matriculaPeriodo.turma = MatriculaPeriodoTurmaDisciplina.turma))");
		}
		
		if (registroAulaVOs != null && !registroAulaVOs.isEmpty()) {
			String andOr = " AND ( ";
			for (RegistroAulaVO registroAula : registroAulaVOs) {
				sqlStr.append(andOr).append(" (matricula.matricula NOT IN (");
				sqlStr.append(" SELECT matricula FROM frequenciaaula WHERE registroaula = ").append(registroAula.getCodigo());
				sqlStr.append(")) ");
				andOr = " OR ";
			}
			sqlStr.append(" ) ");
		}
		if(!trazerAlunosTransferenciaMatriz){
			sqlStr.append(" ").append(getSqlFiltroBaseGradeCurricularAtual(" AND "));
		}
		SqlRowSet tabelaResultado = null;
		if (turmaVO.getTurmaAgrupada() && !turmaVO.getSubturma()) {
			StringBuffer sql = new StringBuffer();			
			sql.append("select t.* from ");
			sql.append("(").append(sqlStr).append(") as t ");
			sql.append(" where  (exists (").append(sqlStr).append(" and historico.disciplina = t.\"MatriculaPeriodoTurmaDisciplina.disciplina\" and historico.disciplina = ").append(disciplina).append(" and historico.matricula = t.\"MatriculaPeriodoTurmaDisciplina.matricula\" limit 1) ") ;
			sql.append(" or not exists (").append(sqlStr).append(" and historico.disciplina = ").append(disciplina).append(" and historico.matricula = t.\"MatriculaPeriodoTurmaDisciplina.matricula\" limit 1)) ") ;
			sql.append("  order by t.nomeSemAcento ");			
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());			
		}else {		
			sqlStr.append(" ORDER BY nomeSemAcento ");
//			System.out.println(sqlStr.toString());
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		}
		return montarDadosConsultaRapida(tabelaResultado, usuario);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.academico.MatriculaPeriodoTurmaDisciplinaInterfaceFacade
	 * #consultarPorMatriculaPeriodo(java.lang.Integer, boolean, int)
	 */
	public List consultarPorMatriculaPeriodo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM MatriculaPeriodoTurmaDisciplina WHERE matriculaPeriodo = " + valorConsulta.intValue() + " and (disciplinaFazParteComposicao = false or disciplinaFazParteComposicao is null) ORDER BY matriculaPeriodo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<MatriculaPeriodoTurmaDisciplinaVO> consultaRapidaPorTransferenciaMatrizCurricularMatricula(Integer codigoTransferenciaMatrizCurricularMatricula, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		StringBuffer sql = new StringBuffer();
		sql.append("select matriculaPeriodoTurmaDisciplina.codigo, matriculaPeriodoTurmaDisciplina.disciplina, matriculaPeriodoTurmaDisciplina.turma, matriculaPeriodoTurmaDisciplina.modalidadeDisciplina, ");
		sql.append("matriculaPeriodoTurmaDisciplina.conteudo, matriculaPeriodoTurmaDisciplina.gradeDisciplina, matriculaPeriodoTurmaDisciplina.gradeCurricularGrupoOptativaDisciplina, ");
		sql.append("matriculaPeriodoTurmaDisciplina.disciplinaReferenteAUmGrupoOptativa, matriculaPeriodoTurmaDisciplina.permiteEscolherModalidade, ");
		sql.append("matriculaPeriodoTurmaDisciplina.matriculaperiodo, matriculaPeriodoTurmaDisciplina.semestre ,matriculaPeriodoTurmaDisciplina.ano, matriculaPeriodoTurmaDisciplina.bimestre, ");
		sql.append("matriculaPeriodoTurmaDisciplina.matricula, matriculaPeriodoTurmaDisciplina.disciplinaincluida, matriculaPeriodoTurmaDisciplina.disciplinaequivale, ");
		sql.append("matriculaPeriodoTurmaDisciplina.disciplinaEquivalente, matriculaPeriodoTurmaDisciplina.disciplinaFazParteComposicao, matriculaPeriodoTurmaDisciplina.inclusaoforaprazo, ");
		sql.append("matriculaPeriodoTurmaDisciplina.transferenciaMatrizCurricularMatricula, matriculaPeriodoTurmaDisciplina.disciplinaCursandoPorCorrespondenciaAposTransferencia, ");
		sql.append("matriculaPeriodoTurmaDisciplina.mapaEquivalenciaDisciplina, matriculaPeriodoTurmaDisciplina.mapaEquivalenciaDisciplinaCursada, matriculaPeriodoTurmaDisciplina.gradedisciplinacomposta, ");
		sql.append("turma.codigo AS \"Turma.codigo\", turma.identificadorTurma AS \"Turma.identificadorTurma\", ");
		sql.append("turmapratica.codigo AS \"turmapratica.codigo\", turmapratica.identificadorTurma AS \"turmapratica.identificadorTurma\", ");
		sql.append("turmateorica.codigo AS \"turmateorica.codigo\", turmateorica.identificadorTurma AS \"turmateorica.identificadorTurma\", ");
		sql.append("disciplina.codigo AS \"Disciplina.codigo\", disciplina.nome AS \"Disciplina.nome\", disciplinaequivalente.codigo AS \"Disciplinaequivalente.codigo\", ");
		sql.append("disciplinaequivalente.nome AS \"Disciplinaequivalente.nome\", matriculaPeriodoTurmaDisciplina.turmateorica, matriculaPeriodoTurmaDisciplina.turmapratica ");
		sql.append("from matriculaPeriodoTurmaDisciplina ");
		sql.append("LEFT JOIN turma ON turma.codigo = matriculaPeriodoTurmaDisciplina.turma ");
		sql.append("LEFT JOIN turma as turmapratica ON turmapratica.codigo = matriculaPeriodoTurmaDisciplina.turmapratica ");
		sql.append("LEFT JOIN turma as turmateorica ON turmateorica.codigo = matriculaPeriodoTurmaDisciplina.turmateorica ");
		sql.append("LEFT JOIN disciplina ON disciplina.codigo = matriculaPeriodoTurmaDisciplina.disciplina ");
		sql.append("LEFT JOIN disciplina disciplinaequivalente ON disciplinaequivalente.codigo = matriculaPeriodoTurmaDisciplina.disciplinaequivalente ");
		sql.append("LEFT JOIN historico on historico.matriculaPeriodoTurmaDisciplina = matriculaPeriodoTurmaDisciplina.codigo ");
		sql.append(" WHERE historico.transferenciaMatrizCurricularMatricula = ").append(codigoTransferenciaMatrizCurricularMatricula);
		sql.append(" ORDER BY disciplina.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaCompleta(tabelaResultado);
	}

	public List<MatriculaPeriodoTurmaDisciplinaVO> consultaRapidaPorMatriculaPeriodo(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE matriculaPeriodo = ").append(valorConsulta);
		sql.append(" ORDER BY disciplina.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	public List<MatriculaPeriodoTurmaDisciplinaVO> consultaRapidaPorMatriculaPeriodoTurma(Integer matriculaPeriodo, TurmaVO turma, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		return consultaRapidaPorMatriculaPeriodoTurmaDisciplina(matriculaPeriodo, turma, null, controlarAcesso, usuario);
	}
	
	@Override
	public List<MatriculaPeriodoTurmaDisciplinaVO> consultaRapidaPorMatriculaPeriodoTurmaDisciplina(Integer matriculaPeriodo, TurmaVO turma, DisciplinaVO disciplina, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE matriculaPeriodo = ").append(matriculaPeriodo);
		if(Uteis.isAtributoPreenchido(disciplina)){
			sql.append(" and  disciplina = ").append(disciplina.getCodigo());	
		}
		if (turma.getTurmaAgrupada() && !turma.getSubturma()) {
			sql.append(" AND turma in(select turmaAgrupada.turma FROM turmaAgrupada WHERE turmaOrigem = ").append(turma.getCodigo()).append(") and matriculaPeriodoTurmaDisciplina.turmaPratica is null and matriculaPeriodoTurmaDisciplina.turmaTeorica is null ");
		} else if(turma.getSubturma()) {
			if(turma.getTipoSubTurma().equals(TipoSubTurmaEnum.PRATICA)){
				sql.append(" AND turmaPratica = ").append(turma.getCodigo());
			}else if(turma.getTipoSubTurma().equals(TipoSubTurmaEnum.TEORICA)){
				sql.append(" AND turmaTeorica = ").append(turma.getCodigo());
			}else{
				sql.append(" AND turma = ").append(turma.getCodigo()).append(" and turmaPratica is null and turmaTeorica is null ");
			}
		} else {
			sql.append(" AND turma = ").append(turma.getCodigo()).append(" and turmaPratica is null and turmaTeorica is null ");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.academico.MatriculaPeriodoTurmaDisciplinaInterfaceFacade
	 * #consultarPorMatriculaPeriodoDisciplinaSemestreAno(java.lang.Integer,
	 * java.lang.Integer, java.lang.String, java.lang.String, boolean, int)
	 */
	public MatriculaPeriodoTurmaDisciplinaVO consultarPorMatriculaPeriodoDisciplinaSemestreAno(Integer valorConsulta, Integer disciplina, String semestre, String ano, boolean diferentePendenteFinanceiramente, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		if (semestre == null) {
			semestre = "";
		}
		if (ano == null) {
			ano = "";
		}
		String sqlStr = "SELECT * FROM MatriculaPeriodoTurmaDisciplina WHERE matriculaPeriodo = " + valorConsulta.intValue() + " and disciplina= " + disciplina.intValue() + " and semestre = '" + semestre + "' and ano = '" + ano + "' ORDER BY matriculaPeriodo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (!tabelaResultado.next()) {
			return null;
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	public Boolean executarVerificacaoMatriculaRegistradaTurmaDisciplinaSemestreAno(String matriculaAluno, Integer codigoTurma, Integer disciplina, String semestre, String ano, Boolean turmaAgrupada, boolean controlarAcesso, int nivelMontarDados) throws Exception {
		if (semestre == null) {
			semestre = "";
		}
		if (ano == null) {
			ano = "";
		}
		// String sqlStr =
		// "SELECT codigo FROM MatriculaPeriodoTurmaDisciplina WHERE turma = "
		// + codigoTurma.intValue() + " and disciplina= " +
		// disciplina.intValue()
		// + " and matricula = '" + matriculaAluno + "' ";
		// if (!semestre.equals("")) {
		// sqlStr += " and semestre = '" + semestre + "' ";
		// }
		// if (!ano.equals("")) {
		// sqlStr += " and ano = '" + ano + "' ";
		// }
		// sqlStr += " ORDER BY matriculaPeriodo";
		StringBuilder sqlStr = new StringBuilder("SELECT codigo FROM MatriculaPeriodoTurmaDisciplina WHERE ");
		if (turmaAgrupada) {
			sqlStr.append(" turma in (select turma from turmaAgrupada where turmaOrigem =  " + codigoTurma.intValue() + ") ");
		} else {
			sqlStr.append(" turma  = " + codigoTurma.intValue());
		}
		sqlStr.append(" and disciplina= " + disciplina.intValue());
		sqlStr.append(" and matricula = '" + matriculaAluno + "' ");
		if (!semestre.equals("")) {
			sqlStr.append(" and semestre = '" + semestre + "' ");
		}
		if (!ano.equals("")) {
			sqlStr.append(" and ano = '" + ano + "' ");
		}
		sqlStr.append(" limit 1 ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			return false;
		}
		return true;
	}

	public MatriculaPeriodoTurmaDisciplinaVO consultarPorMatriculaTurmaDisciplinaSemestreAno(String matriculaAluno, Integer codigoTurma, Integer disciplina, String semestre, String ano, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		if (semestre == null) {
			semestre = "";
		}
		if (ano == null) {
			ano = "";
		}
		String sqlStr = "SELECT * FROM MatriculaPeriodoTurmaDisciplina WHERE turma = " + codigoTurma.intValue() + " and disciplina= " + disciplina.intValue() + " and matricula = '" + matriculaAluno + "' ";
		if (!semestre.equals("")) {
			sqlStr += " and semestre = '" + semestre + "' ";
		}
		if (!ano.equals("")) {
			sqlStr += " and ano = '" + ano + "' ";
		}
		sqlStr += " ORDER BY matriculaPeriodo";

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (!tabelaResultado.next()) {
			return null;
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	public Boolean consultarExistenciaMatriculaPeriodoturmaDisciplinaPorMatriculaTurmaDisciplinaSemestreAno(String matriculaAluno, Integer turma, Integer disciplina, String semestre, String ano, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("SELECT * FROM MatriculaPeriodoTurmaDisciplina ");
		sqlStr.append("WHERE 1=1 ");
		if (!turma.equals(0)) {
			sqlStr.append("AND turma = ").append(turma).append(" ");
		}
		if (!disciplina.equals(0)) {
			sqlStr.append("AND disciplina= ").append(disciplina);
		}
		if (!matriculaAluno.equals("")) {
			sqlStr.append("AND matricula = '").append(matriculaAluno).append("' ");
		}
		if (semestre != null && !semestre.equals("")) {
			sqlStr.append("AND semestre = '").append(semestre).append("' ");
		}
		if (ano != null && !ano.equals("")) {
			sqlStr.append("AND ano = '").append(ano).append("' ");
		}
		sqlStr.append("LIMIT 1");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return tabelaResultado.next();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.academico.MatriculaPeriodoTurmaDisciplinaInterfaceFacade
	 * #consultarPorMatricula(java.lang.String, boolean, int)
	 */
	// public List consultarPorMatricula(String valorConsulta, boolean
	// controlarAcesso, int nivelMontarDados) throws Exception {
	// ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
	// String sqlStr =
	// "SELECT * FROM MatriculaPeriodoTurmaDisciplina WHERE lower (matriculaPeriodo) = "
	// + valorConsulta.toLowerCase() + " ORDER BY matricula";
	// SqlRowSet tabelaResultado =
	// getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
	// return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
	// }
	public List consultarPorMatricula(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM MatriculaPeriodoTurmaDisciplina WHERE matricula = '" + valorConsulta + "' ORDER BY matricula";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List consultaRapidaPorMatricula(String matricula, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE matricula = '").append(matricula).append("' ");
		sql.append(" ORDER BY matriculaPeriodoTurmaDisciplina.matricula ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.academico.MatriculaPeriodoTurmaDisciplinaInterfaceFacade
	 * #consultarPorCodigo(java.lang.Integer, boolean, int)
	 */
	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM MatriculaPeriodoTurmaDisciplina WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma
	 * consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>MatriculaPeriodoTurmaDisciplinaVO</code> resultantes da
	 *         consulta.
	 */
	public static List<MatriculaPeriodoTurmaDisciplinaVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<MatriculaPeriodoTurmaDisciplinaVO> vetResultado = new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>(0);
		while (tabelaResultado.next()) {
			MatriculaPeriodoTurmaDisciplinaVO obj = new MatriculaPeriodoTurmaDisciplinaVO();
			obj = montarDados(tabelaResultado, nivelMontarDados, usuario);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	public static List<MatriculaPeriodoTurmaDisciplinaVO> montarDadosConsultaRapida(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
		List<MatriculaPeriodoTurmaDisciplinaVO> vetResultado = new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDadosBasico(tabelaResultado, usuario));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de
	 * dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>MatriculaPeriodoTurmaDisciplinaVO</code>.
	 * 
	 * @return O objeto da classe <code>MatriculaPeriodoTurmaDisciplinaVO</code>
	 *         com os dados devidamente montados.
	 */
	public static MatriculaPeriodoTurmaDisciplinaVO montarDadosBasico(SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
		// //
		// System.out.println(">> Montar dados(MatriculaPeriodoTurmaDisciplina) - "
		// + new Date());
		MatriculaPeriodoTurmaDisciplinaVO obj = new MatriculaPeriodoTurmaDisciplinaVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("MatriculaPeriodoTurmaDisciplina.codigo")));
		obj.setMatriculaPeriodo(new Integer(dadosSQL.getInt("MatriculaPeriodoTurmaDisciplina.matriculaPeriodo")));
		obj.getTurma().setCodigo(new Integer(dadosSQL.getInt("MatriculaPeriodoTurmaDisciplina.turma")));
		obj.getConteudo().setCodigo(new Integer(dadosSQL.getInt("MatriculaPeriodoTurmaDisciplina.conteudo")));
		obj.getDisciplina().setCodigo(new Integer(dadosSQL.getInt("MatriculaPeriodoTurmaDisciplina.disciplina")));
		obj.setModalidadeDisciplina(ModalidadeDisciplinaEnum.valueOf(dadosSQL.getString("MatriculaPeriodoTurmaDisciplina.modalidadeDisciplina")));
		obj.setBimestre(dadosSQL.getInt("MatriculaPeriodoTurmaDisciplina.bimestre"));
		obj.setAno(dadosSQL.getString("MatriculaPeriodoTurmaDisciplina.ano"));
		obj.setSemestre(dadosSQL.getString("MatriculaPeriodoTurmaDisciplina.semestre"));
		obj.setMatricula(dadosSQL.getString("MatriculaPeriodoTurmaDisciplina.matricula"));
		obj.setDisciplinaIncluida(dadosSQL.getBoolean("MatriculaPeriodoTurmaDisciplina.disciplinaIncluida"));
		obj.setDisciplinaEquivale(dadosSQL.getBoolean("MatriculaPeriodoTurmaDisciplina.disciplinaEquivale"));
		obj.setDisciplinaFazParteComposicao(dadosSQL.getBoolean("MatriculaPeriodoTurmaDisciplina.disciplinaFazParteComposicao"));
		obj.setPermiteEscolherModalidade(dadosSQL.getBoolean("MatriculaPeriodoTurmaDisciplina.permiteEscolherModalidade"));
		obj.getDisciplinaEquivalente().setCodigo(dadosSQL.getInt("MatriculaPeriodoTurmaDisciplina.disciplinaEquivalente"));
		obj.setJustificativa(dadosSQL.getString("MatriculaPeriodoTurmaDisciplina.justificativa"));
		obj.setObservacaoJustificativa(dadosSQL.getString("MatriculaPeriodoTurmaDisciplina.observacaojustificativa"));
		obj.setInclusaoForaPrazo(dadosSQL.getBoolean("MatriculaPeriodoTurmaDisciplina.inclusaoForaPrazo"));
		obj.setDataRegistroHistorico(dadosSQL.getDate("dataRegistroHistorico"));
		obj.setNovoObj(Boolean.FALSE);

		obj.setNivelMontarDados(NivelMontarDados.BASICO);
		obj.getTurmaTeorica().setCodigo(dadosSQL.getInt("MatriculaPeriodoTurmaDisciplina.turmateorica"));
		obj.getTurmaPratica().setCodigo(dadosSQL.getInt("MatriculaPeriodoTurmaDisciplina.turmapratica"));
		obj.getTurma().setCodigo(dadosSQL.getInt("Turma.codigo"));
		obj.getTurma().setSemestral(dadosSQL.getBoolean("Turma.semestral"));
		obj.getTurma().setAnual(dadosSQL.getBoolean("Turma.anual"));
		obj.getTurma().setIdentificadorTurma(dadosSQL.getString("Turma.identificadorturma"));
		obj.getTurma().setTurmaAgrupada(dadosSQL.getBoolean("Turma.turmaagrupada"));
		obj.getTurma().setSala(dadosSQL.getString("Turma.sala"));
		obj.getTurma().getGradeCurricularVO().setCodigo(dadosSQL.getInt("Turma.gradeCurricular"));

		obj.getTurma().getChancelaVO().setCodigo(dadosSQL.getInt("Turma.chancela"));
		obj.getTurma().getChancelaVO().setInstituicaoChanceladora(dadosSQL.getString("Chancela.instituicaoChanceladora"));

		obj.getTurma().setTipoChancela(dadosSQL.getString("Turma.tipochancela"));
		obj.getTurma().setValorFixoChancela(dadosSQL.getDouble("Turma.valorFixoChancela"));
		obj.getTurma().setPorcentagemChancela(dadosSQL.getDouble("Turma.porcentagemChancela"));
		obj.getTurma().setValorPorAluno(dadosSQL.getBoolean("Turma.valorPorAluno"));

		obj.getTurma().setQtdAlunosEstimado(dadosSQL.getInt("Turma.qtdAlunosEstimado"));
		obj.getTurma().setCustoMedioAluno(dadosSQL.getDouble("Turma.custoMedioAluno"));
		obj.getTurma().setReceitaMediaAluno(dadosSQL.getDouble("Turma.receitaMediaAluno"));

		// Unidade Ensino
		obj.getTurma().getUnidadeEnsino().setCodigo((dadosSQL.getInt("Unidadeensino.codigo")));
		obj.getTurma().getUnidadeEnsino().setNome((dadosSQL.getString("Unidadeensino.nome")));
		// Curso
		obj.getTurma().getCurso().setCodigo(dadosSQL.getInt("Curso.codigo"));
		obj.getTurma().getCurso().setNome(dadosSQL.getString("Curso.nome"));
		obj.getTurma().getCurso().setPeriodicidade(dadosSQL.getString("Curso.periodicidade"));
		obj.getTurma().getCurso().setNivelEducacional(dadosSQL.getString("Curso.nivelEducacional"));
		// Turno
		obj.getTurma().getTurno().setCodigo((dadosSQL.getInt("Turno.codigo")));
		obj.getTurma().getTurno().setNome((dadosSQL.getString("Turno.nome")));
		// PeriodoLetivo
		obj.getTurma().getPeridoLetivo().setDescricao(dadosSQL.getString("Periodoletivo.descricao"));
		obj.getTurma().getPeridoLetivo().setCodigo(dadosSQL.getInt("Periodoletivo.codigo"));
		obj.getTurma().getPeridoLetivo().setNomeCertificacao(dadosSQL.getString("Periodoletivo.nomeCertificacao"));
		
		obj.getMatriculaPeriodoObjetoVO().setCodigo(dadosSQL.getInt("MatriculaPeriodo.codigo"));
		obj.getMatriculaPeriodoObjetoVO().setData(dadosSQL.getDate("MatriculaPeriodo.data"));
		obj.getMatriculaPeriodoObjetoVO().setSituacao(dadosSQL.getString("MatriculaPeriodo.situacao"));
		obj.getMatriculaPeriodoObjetoVO().setSituacaoMatriculaPeriodo(dadosSQL.getString("MatriculaPeriodo.situacaoMatriculaPeriodo"));
		obj.getMatriculaPeriodoObjetoVO().getGradeCurricular().setCodigo(dadosSQL.getInt("MatriculaPeriodo.gradecurricular"));
		obj.getMatriculaPeriodoObjetoVO().getTurma().setCodigo(dadosSQL.getInt("MatriculaPeriodo.turma"));
		obj.getMatriculaPeriodoObjetoVO().getPeriodoLetivo().setCodigo(dadosSQL.getInt("MatriculaPeriodo.periodoLetivoMatricula"));
		obj.getMatriculaPeriodoObjetoVO().getUnidadeEnsinoCursoVO().setCodigo(dadosSQL.getInt("MatriculaPeriodo.unidadeEnsinoCurso"));
		obj.getMatriculaPeriodoObjetoVO().getProcessoMatriculaVO().setCodigo(dadosSQL.getInt("MatriculaPeriodo.processoMatricula"));
		obj.getMatriculaPeriodoObjetoVO().setProcessoMatricula(dadosSQL.getInt("MatriculaPeriodo.processoMatricula"));
		obj.getMatriculaPeriodoObjetoVO().setUnidadeEnsinoCurso(dadosSQL.getInt("MatriculaPeriodo.unidadeEnsinoCurso"));
		obj.getMatriculaPeriodoObjetoVO().setAlunoTransferidoUnidade(dadosSQL.getBoolean("MatriculaPeriodo.alunoTransferidoUnidade"));
		obj.getMatriculaPeriodoObjetoVO().setMatricula(dadosSQL.getString("MatriculaPeriodoTurmaDisciplina.matricula"));
		obj.getMatriculaPeriodoObjetoVO().setDataFechamentoMatriculaPeriodo(dadosSQL.getDate("MatriculaPeriodo.dataFechamentoMatriculaPeriodo"));
		obj.getMatriculaPeriodoObjetoVO().getMatriculaVO().setMatricula(dadosSQL.getString("MatriculaPeriodoTurmaDisciplina.matricula"));
		obj.getMatriculaPeriodoObjetoVO().getMatriculaVO().getCurso().setCodigo(dadosSQL.getInt("Curso.codigo"));
		obj.getMatriculaPeriodoObjetoVO().getMatriculaVO().getCurso().setNome(dadosSQL.getString("Curso.nome"));
		obj.getMatriculaPeriodoObjetoVO().getMatriculaVO().getCurso().setPeriodicidade(dadosSQL.getString("Curso.periodicidade"));
		obj.getMatriculaPeriodoObjetoVO().getMatriculaVO().getCurso().setNivelEducacional(dadosSQL.getString("Curso.nivelEducacional"));		
		obj.getMatriculaPeriodoObjetoVO().getMatriculaVO().getCurso().getConfiguracaoAcademico().setCodigo(dadosSQL.getInt("Curso.configuracaoAcademico"));		
		obj.getMatriculaPeriodoObjetoVO().getMatriculaVO().getGradeCurricularAtual().setCodigo(dadosSQL.getInt("Matricula.gradeCurricularAtual"));
		obj.getMatriculaPeriodoObjetoVO().getMatriculaVO().setData(dadosSQL.getDate("Matricula.data"));
		obj.getMatriculaPeriodoObjetoVO().getMatriculaVO().setUpdated(dadosSQL.getDate("Matricula.updated"));
		obj.getMatriculaPeriodoObjetoVO().getMatriculaVO().setMatriculaSuspensa(dadosSQL.getBoolean("Matricula.matriculaSuspensa"));
		obj.getMatriculaPeriodoObjetoVO().getMatriculaVO().getTurno().setCodigo(dadosSQL.getInt("Matricula.turno"));
		obj.getMatriculaPeriodoObjetoVO().getMatriculaVO().setSituacao(dadosSQL.getString("Matricula.situacao"));
		obj.getMatriculaPeriodoObjetoVO().getMatriculaVO().setSituacaoFinanceira(dadosSQL.getString("Matricula.situacaoFinanceira"));
		obj.getMatriculaPeriodoObjetoVO().getMatriculaVO().getAluno().setCodigo(dadosSQL.getInt("Pessoa.codigo"));
		obj.getMatriculaPeriodoObjetoVO().getMatriculaVO().getAluno().setNome(dadosSQL.getString("Pessoa.nome"));
		obj.getMatriculaPeriodoObjetoVO().getMatriculaVO().getAluno().setCPF(dadosSQL.getString("Pessoa.cpf"));
		if (dadosSQL.getString("MatriculaPeriodo.origemfechamentomatriculaperiodo") != null) {
			obj.getMatriculaPeriodoObjetoVO().setOrigemFechamentoMatriculaPeriodo(OrigemFechamentoMatriculaPeriodoEnum.valueOf(dadosSQL.getString("MatriculaPeriodo.origemfechamentomatriculaperiodo")));
		}
		obj.setMatriculaObjetoVO(obj.getMatriculaPeriodoObjetoVO().getMatriculaVO());		
		return obj;
	}

	public static MatriculaPeriodoTurmaDisciplinaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		// //
		// System.out.println(">> Montar dados(MatriculaPeriodoTurmaDisciplina) - "
		// + new Date());
		MatriculaPeriodoTurmaDisciplinaVO obj = new MatriculaPeriodoTurmaDisciplinaVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setMatriculaPeriodo(new Integer(dadosSQL.getInt("matriculaPeriodo")));
		obj.getTurma().setCodigo(new Integer(dadosSQL.getInt("turma")));
		obj.getDisciplina().setCodigo(new Integer(dadosSQL.getInt("disciplina")));
		obj.getConteudo().setCodigo(new Integer(dadosSQL.getInt("conteudo")));
		obj.setAno(dadosSQL.getString("ano"));
		obj.setModalidadeDisciplina(ModalidadeDisciplinaEnum.valueOf(dadosSQL.getString("modalidadeDisciplina")));
		obj.setSemestre(dadosSQL.getString("semestre"));
		obj.setMatricula(dadosSQL.getString("matricula"));
		obj.setDisciplinaIncluida(dadosSQL.getBoolean("disciplinaIncluida"));
		obj.setDisciplinaEquivale(dadosSQL.getBoolean("disciplinaEquivale"));
		obj.setPermiteEscolherModalidade(dadosSQL.getBoolean("permiteEscolherModalidade"));
		// obj.getDisciplinaEquivalente().setCodigo(dadosSQL.getInt("disciplinaEquivalente"));
		obj.setDisciplinaFazParteComposicao(dadosSQL.getBoolean("disciplinaFazParteComposicao"));
		obj.setReposicao(dadosSQL.getBoolean("reposicao"));
		obj.setInclusaoForaPrazo(dadosSQL.getBoolean("inclusaoForaPrazo"));

		// NOVOS CAMPOS
		obj.setDisciplinaPorEquivalencia(dadosSQL.getBoolean("disciplinaPorEquivalencia"));
		obj.getGradeDisciplinaCompostaVO().setCodigo(dadosSQL.getInt("gradeDisciplinaComposta"));
		obj.setDisciplinaEmRegimeEspecial(dadosSQL.getBoolean("disciplinaEmRegimeEspecial"));
		obj.setDisciplinaOptativa(dadosSQL.getBoolean("disciplinaOptativa"));
		obj.setDisciplinaReferenteAUmGrupoOptativa(dadosSQL.getBoolean("disciplinaReferenteAUmGrupoOptativa"));
		obj.getGradeCurricularGrupoOptativaDisciplinaVO().setCodigo(dadosSQL.getInt("gradeCurricularGrupoOptativaDisciplina"));
		obj.getGradeDisciplinaVO().setCodigo(dadosSQL.getInt("gradeDisciplina"));
		obj.getMapaEquivalenciaDisciplinaVOIncluir().setCodigo(dadosSQL.getInt("mapaEquivalenciaDisciplina"));
		obj.getMapaEquivalenciaDisciplinaCursada().setCodigo(dadosSQL.getInt("mapaEquivalenciaDisciplinaCursada"));
		obj.getTransferenciaMatrizCurricularMatricula().setCodigo(dadosSQL.getInt("transferenciaMatrizCurricularMatricula"));
		obj.setDisciplinaCursandoPorCorrespondenciaAposTransferencia(dadosSQL.getBoolean("disciplinaCursandoPorCorrespondenciaAposTransferencia"));
		obj.setBimestre(dadosSQL.getInt("bimestre"));
		
		
		if (Uteis.isAtributoPreenchido(dadosSQL.getInt("professor"))) {
			obj.getProfessor().setCodigo(dadosSQL.getInt("professor"));
		}
		obj.getTurmaTeorica().setCodigo(dadosSQL.getInt("turmaTeorica"));
		obj.getTurmaPratica().setCodigo(dadosSQL.getInt("turmaPratica"));
		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
			return obj;
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
			montarDadosTurma(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
			return obj;
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			montarDadosDisciplina(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			return obj;
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_PROCESSAMENTO) {
			montarDadosDisciplina(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			obj.setMatriculaObjetoVO(getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula(), null, NivelMontarDados.TODOS, usuario));
			return obj;
		}
		montarDadosTurma(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosDisciplina(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosGradeDisciplina(obj, usuario);
		montarDadosGradeCurricularGrupoOptativaDisciplina(obj, usuario);
		montarDadosMapaEquivalenciaDisciplina(obj, usuario);
		montarDadosGradeDisciplinaComposta(obj, usuario);
		// montarDadosDisciplinaEquivalente(obj,
		// Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		if (Uteis.isAtributoPreenchido(dadosSQL.getInt("conteudo"))) {
			obj.getConteudo().setCodigo(dadosSQL.getInt("conteudo"));
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_TODOS || nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS) {
			if(Uteis.isAtributoPreenchido(obj.getConteudo().getCodigo())){
				obj.setConteudo(getFacadeFactory().getConteudoFacade().consultarPorChavePrimaria(obj.getConteudo().getCodigo(), NivelMontarDados.BASICO, false, usuario));
			}
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_TODOS) {
			obj.setMatriculaObjetoVO(getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula(), null, NivelMontarDados.TODOS, usuario));
			obj.setMatriculaPeriodoObjetoVO(getFacadeFactory().getMatriculaPeriodoFacade().consultarPorChavePrimaria(obj.getMatriculaPeriodo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, null, usuario));
			return obj;
		}
		return obj;
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>TurmaVO</code> relacionado ao objeto
	 * <code>MatriculaPeriodoTurmaDisciplinaVO</code>. Faz uso da chave primária
	 * da classe <code>TurmaVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosTurma(MatriculaPeriodoTurmaDisciplinaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getTurma().getCodigo().intValue() != 0) {
			if(nivelMontarDados == Uteis.NIVELMONTARDADOS_TODOS){
				getFacadeFactory().getTurmaFacade().carregarDados(obj.getTurma(), usuario);
			}else{
				getFacadeFactory().getTurmaFacade().carregarDados(obj.getTurma(), NivelMontarDados.BASICO, usuario);
			}
		}
		if (obj.getTurmaPratica().getCodigo().intValue() != 0) {
			if(nivelMontarDados == Uteis.NIVELMONTARDADOS_TODOS){
				getFacadeFactory().getTurmaFacade().carregarDados(obj.getTurmaPratica(), usuario);
			}else{
				getFacadeFactory().getTurmaFacade().carregarDados(obj.getTurmaPratica(), NivelMontarDados.BASICO, usuario);
			}
		}
		if (obj.getTurmaTeorica().getCodigo().intValue() != 0) {
			if(nivelMontarDados == Uteis.NIVELMONTARDADOS_TODOS){		
				getFacadeFactory().getTurmaFacade().carregarDados(obj.getTurmaTeorica(), usuario);
			}else{
				getFacadeFactory().getTurmaFacade().carregarDados(obj.getTurmaTeorica(), NivelMontarDados.BASICO, usuario);
			}
		}
	}

	public static void montarDadosDisciplina(MatriculaPeriodoTurmaDisciplinaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getDisciplina().getCodigo().intValue() == 0) {
			return;
		}
		obj.setDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(obj.getDisciplina().getCodigo(), nivelMontarDados, usuario));
	}

	public static void montarDadosGradeDisciplina(MatriculaPeriodoTurmaDisciplinaVO obj, UsuarioVO usuario) throws Exception {
		if (obj.getGradeDisciplinaVO().getCodigo().intValue() == 0) {
			return;
		}
		// obj.setGradeDisciplinaVO(getFacadeFactory().getGradeDisciplinaFacade().consultarPorMatriculaDisciplinaMatriculaPeriodo(obj.getMatricula(),
		// obj.getMatriculaPeriodo(), obj.getDisciplina().getCodigo(),
		// usuario));
		obj.setGradeDisciplinaVO(getFacadeFactory().getGradeDisciplinaFacade().consultarPorChavePrimaria(obj.getGradeDisciplinaVO().getCodigo(), usuario));
	}

	public static void montarDadosGradeCurricularGrupoOptativaDisciplina(MatriculaPeriodoTurmaDisciplinaVO obj, UsuarioVO usuario) throws Exception {
		if (obj.getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo().intValue() == 0) {
			return;
		}
		obj.setGradeCurricularGrupoOptativaDisciplinaVO(getFacadeFactory().getGradeCurricularGrupoOptativaDisciplinaFacade().consultarPorChavePrimaria(obj.getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo(), usuario));
	}

	public static void montarDadosGradeDisciplinaComposta(MatriculaPeriodoTurmaDisciplinaVO obj, UsuarioVO usuario) throws Exception {
		if (obj.getGradeDisciplinaCompostaVO().getCodigo().intValue() == 0) {
			return;
		}
		obj.setGradeDisciplinaCompostaVO(getFacadeFactory().getGradeDisciplinaCompostaFacade().consultarPorChavePrimaria(obj.getGradeDisciplinaCompostaVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuario));
	}

	public static void montarDadosMapaEquivalenciaDisciplina(MatriculaPeriodoTurmaDisciplinaVO obj, UsuarioVO usuario) throws Exception {
		if (obj.getMapaEquivalenciaDisciplinaVOIncluir().getCodigo().intValue() == 0) {
			return;
		}
		obj.setMapaEquivalenciaDisciplinaVOIncluir(getFacadeFactory().getMapaEquivalenciaDisciplinaFacade().consultarPorChavePrimaria(obj.getMapaEquivalenciaDisciplinaVOIncluir().getCodigo(), NivelMontarDados.TODOS));
		for (MapaEquivalenciaDisciplinaCursadaVO cursadaMapaVO : obj.getMapaEquivalenciaDisciplinaVOIncluir().getMapaEquivalenciaDisciplinaCursadaVOs()) {
			if (cursadaMapaVO.getCodigo().equals(obj.getMapaEquivalenciaDisciplinaCursada().getCodigo())) {
				obj.setMapaEquivalenciaDisciplinaCursada(cursadaMapaVO);
			}
		}
	}

	public static void montarDadosDisciplinaEquivalente(MatriculaPeriodoTurmaDisciplinaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getDisciplinaEquivalente().getCodigo().intValue() == 0) {
			return;
		}
		obj.setDisciplinaEquivalente(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(obj.getDisciplinaEquivalente().getCodigo(), nivelMontarDados, usuario));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.academico.MatriculaPeriodoTurmaDisciplinaInterfaceFacade
	 * #excluirMatriculaPeriodoTurmaDisciplinasPorMatricula(java.lang.String)
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirMatriculaPeriodoTurmaDisciplinasPorMatricula(String matricula, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		List matriculaPeriodos = getFacadeFactory().getMatriculaPeriodoFacade().consultarUltimaMatriculaPeriodoPorMatricula(matricula, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, configuracaoFinanceiroVO, usuario);
		if (!matriculaPeriodos.isEmpty()) {
			MatriculaPeriodoVO obj = (MatriculaPeriodoVO) matriculaPeriodos.get(0);
			if (obj.getSituacaoMatriculaPeriodo().equals("AT")) {
				List objetos = consultarMatriculaPeriodoTurmaDisciplinas(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
				retirarReservaTurmaDisciplina(objetos, usuario);
			}
		}
		MatriculaPeriodo.excluir(getIdEntidade());
		String sql = "DELETE FROM MatriculaPeriodoTurmaDisciplina WHERE (matricula = ?)" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { matricula });
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirMatriculaPeriodoTurmaDisciplinasPorListaMatriculaPeriodo(List<MatriculaPeriodoVO> listaMatriculaPeriodo, Integer disciplina, Integer turma, Integer gradeCurricular, UsuarioVO usuario) throws Exception {
		MatriculaPeriodo.excluir(getIdEntidade());
		StringBuilder sqlStr = new StringBuilder("DELETE FROM MatriculaPeriodoTurmaDisciplina WHERE  disciplina = ").append(disciplina);
		sqlStr.append(" and turma = ").append(turma);		
		sqlStr.append(" and matriculaPeriodo in (").append(UteisTexto.converteListaEntidadeCampoCodigoParaCondicaoIn(listaMatriculaPeriodo, "codigo")).append(")");
		sqlStr.append(" and codigo not in ( ");
		sqlStr.append(" SELECT MatriculaPeriodoTurmaDisciplina.codigo FROM MatriculaPeriodoTurmaDisciplina  ");
		sqlStr.append(" inner join matriculaperiodo on matriculaperiodoturmadisciplina.matriculaperiodo = matriculaperiodo.codigo ");
		sqlStr.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
		sqlStr.append(" where  matriculaperiodo.turma != matriculaperiodoturmadisciplina.turma ");
		sqlStr.append(" and matriculaperiodoturmadisciplina.disciplina = ").append(disciplina).append(" ");
		sqlStr.append(" and matriculaperiodoturmadisciplina.turma = ").append(turma).append(" ");	
		sqlStr.append(" AND MatriculaPeriodo.situacaomatriculaperiodo <> 'PC'  ");
		sqlStr.append(" union all ");
		sqlStr.append(" SELECT MatriculaPeriodoTurmaDisciplina.codigo FROM MatriculaPeriodoTurmaDisciplina  ");
		sqlStr.append(" inner join matriculaperiodo on matriculaperiodoturmadisciplina.matriculaperiodo = matriculaperiodo.codigo ");
		sqlStr.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
		sqlStr.append(" where  matriculaperiodo.turma != matriculaperiodoturmadisciplina.turma ");
		sqlStr.append(" and MatriculaPeriodo.turma = ").append(turma).append(" ");	
		sqlStr.append(" AND MatriculaPeriodo.situacaomatriculaperiodo <> 'PC'  ");
		sqlStr.append(" and matriculaperiodoturmadisciplina.disciplina in ( ");
		//verifico se existe alguem estudando alguma disciplina para pagar essa disciplina
		sqlStr.append(" select mapaequivalenciadisciplinacursada.disciplina as disciplina_cursada from mapaequivalenciamatrizcurricular ");
		sqlStr.append(" inner join mapaequivalenciadisciplina on mapaequivalenciadisciplina.mapaequivalenciamatrizcurricular = mapaequivalenciamatrizcurricular.codigo");
		sqlStr.append(" inner join mapaequivalenciadisciplinacursada   on mapaequivalenciadisciplinacursada.mapaequivalenciadisciplina = mapaequivalenciadisciplina.codigo");
		sqlStr.append(" inner join mapaequivalenciadisciplinamatrizcurricular  on  mapaequivalenciadisciplinamatrizcurricular.mapaequivalenciadisciplina = mapaequivalenciadisciplina.codigo");
		sqlStr.append(" where gradecurricular = ").append(gradeCurricular).append(" and mapaequivalenciadisciplinamatrizcurricular.disciplina = ").append(disciplina).append(" ");
		sqlStr.append(" union all");
		sqlStr.append(" select ").append(disciplina);
		sqlStr.append(" )) ");
		sqlStr.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		getConexao().getJdbcTemplate().update(sqlStr.toString());
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirMatriculaPeriodoTurmaDisciplinas(List<MatriculaPeriodoTurmaDisciplinaVO> listaMptd, UsuarioVO usuario) throws Exception {
		MatriculaPeriodo.excluir(getIdEntidade());
		StringBuilder sql = new StringBuilder("DELETE FROM MatriculaPeriodoTurmaDisciplina WHERE  codigo in (").append(UteisTexto.converteListaEntidadeCampoCodigoParaCondicaoIn(listaMptd, "codigo")).append(") ");
		sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		getConexao().getJdbcTemplate().update(sql.toString());
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirMatriculaPeriodoTurmaDisciplinasPorDisciplinaEMatriculaMatrizCurricular(String matricula, Integer disciplina, Integer matrizCurricular) throws Exception {
		MatriculaPeriodo.excluir(getIdEntidade());
		String sql = "DELETE FROM MatriculaPeriodoTurmaDisciplina WHERE (matricula = ? and disciplina = ?)";
		getConexao().getJdbcTemplate().update(sql, new Object[] { matricula, disciplina });
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.academico.MatriculaPeriodoTurmaDisciplinaInterfaceFacade
	 * #
	 * consultarMatriculaPeriodoTurmaDisciplinaASeremExcluidas(java.lang.Integer
	 * , java.util.List, boolean)
	 */
	public List consultarMatriculaPeriodoTurmaDisciplinaASeremExcluidas(Integer matriculaPeriodo, List objetos, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM MatriculaPeriodoTurmaDisciplina WHERE matriculaPeriodo = " + matriculaPeriodo.intValue();
		Iterator i = objetos.iterator();
		while (i.hasNext()) {
			MatriculaPeriodoTurmaDisciplinaVO obj = (MatriculaPeriodoTurmaDisciplinaVO) i.next();
			if (obj.getCodigo().intValue() != 0) {
				sqlStr += " and (codigo != " + obj.getCodigo() + ") ";
			}
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public Integer consultaQtdMatriculaPeriodoTurmaDisciplinaReposicaoAlunoTurma(Integer gradeCurricular, Integer turma, Integer disciplina, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("select sum(qtd) as qtde from( ");
		sqlStr.append(" SELECT Count(MatriculaPeriodoTurmaDisciplina.codigo) as qtd FROM MatriculaPeriodoTurmaDisciplina  ");
		sqlStr.append(" inner join matriculaperiodo on matriculaperiodoturmadisciplina.matriculaperiodo = matriculaperiodo.codigo ");
		sqlStr.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
		sqlStr.append(" where  matriculaperiodo.turma != matriculaperiodoturmadisciplina.turma ");
		sqlStr.append(" and matriculaperiodoturmadisciplina.disciplina = ").append(disciplina).append(" ");
		sqlStr.append(" and matriculaperiodoturmadisciplina.turma = ").append(turma).append(" ");	
		sqlStr.append(" AND MatriculaPeriodo.situacaomatriculaperiodo <> 'PC' ");
		sqlStr.append(" union all ");
		sqlStr.append(" SELECT Count(MatriculaPeriodoTurmaDisciplina.codigo) as qtd FROM MatriculaPeriodoTurmaDisciplina  ");
		sqlStr.append(" inner join matriculaperiodo on matriculaperiodoturmadisciplina.matriculaperiodo = matriculaperiodo.codigo ");
		sqlStr.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
		sqlStr.append(" where  matriculaperiodo.turma != matriculaperiodoturmadisciplina.turma ");
		sqlStr.append(" and MatriculaPeriodo.turma = ").append(turma).append(" ");	
		sqlStr.append(" AND MatriculaPeriodo.situacaomatriculaperiodo <> 'PC' ");
		sqlStr.append(" and matriculaperiodoturmadisciplina.disciplina in ( ");
		sqlStr.append(" select mapaequivalenciadisciplinacursada.disciplina as disciplina_cursada from mapaequivalenciamatrizcurricular ");
		sqlStr.append(" inner join mapaequivalenciadisciplina on mapaequivalenciadisciplina.mapaequivalenciamatrizcurricular = mapaequivalenciamatrizcurricular.codigo");
		sqlStr.append(" inner join mapaequivalenciadisciplinacursada   on mapaequivalenciadisciplinacursada.mapaequivalenciadisciplina = mapaequivalenciadisciplina.codigo");
		sqlStr.append(" inner join mapaequivalenciadisciplinamatrizcurricular  on  mapaequivalenciadisciplinamatrizcurricular.mapaequivalenciadisciplina = mapaequivalenciadisciplina.codigo");
		sqlStr.append(" where gradecurricular = ").append(gradeCurricular).append(" and mapaequivalenciadisciplinamatrizcurricular.disciplina = ").append(disciplina).append(" ");
		sqlStr.append(" union all");
		sqlStr.append(" select ").append(disciplina);
		sqlStr.append(" ) ");
		sqlStr.append(" ) as t");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (Integer) Uteis.getSqlRowSetTotalizador(tabelaResultado, "qtde", TipoCampoEnum.INTEIRO);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public List<MatriculaPeriodoTurmaDisciplinaVO> consultaRapidaMatriculaPeriodoTurmaDisciplinaReposicaoAlunoTurma(Integer gradeCurricular, Integer turma, Integer disciplina, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append(" SELECT MatriculaPeriodoTurmaDisciplina.* FROM MatriculaPeriodoTurmaDisciplina  ");
		sqlStr.append(" inner join matriculaperiodo on matriculaperiodoturmadisciplina.matriculaperiodo = matriculaperiodo.codigo ");
		sqlStr.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
		sqlStr.append(" where  matriculaperiodo.turma != matriculaperiodoturmadisciplina.turma ");
		sqlStr.append(" and matriculaperiodoturmadisciplina.disciplina = ").append(disciplina).append(" ");
		sqlStr.append(" and matriculaperiodoturmadisciplina.turma = ").append(turma).append(" ");	
		sqlStr.append(" AND MatriculaPeriodo.situacaomatriculaperiodo <> 'PC'  ");
		sqlStr.append(" union all ");
		sqlStr.append(" SELECT MatriculaPeriodoTurmaDisciplina.* FROM MatriculaPeriodoTurmaDisciplina  ");
		sqlStr.append(" inner join matriculaperiodo on matriculaperiodoturmadisciplina.matriculaperiodo = matriculaperiodo.codigo ");
		sqlStr.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
		sqlStr.append(" where  matriculaperiodo.turma != matriculaperiodoturmadisciplina.turma ");
		sqlStr.append(" and MatriculaPeriodo.turma = ").append(turma).append(" ");	
		sqlStr.append(" AND MatriculaPeriodo.situacaomatriculaperiodo <> 'PC'  ");
		sqlStr.append(" and matriculaperiodoturmadisciplina.disciplina in ( ");
		//verifico se existe alguem estudando alguma disciplina para pagar essa disciplina
		sqlStr.append(" select mapaequivalenciadisciplinacursada.disciplina as disciplina_cursada from mapaequivalenciamatrizcurricular ");
		sqlStr.append(" inner join mapaequivalenciadisciplina on mapaequivalenciadisciplina.mapaequivalenciamatrizcurricular = mapaequivalenciamatrizcurricular.codigo");
		sqlStr.append(" inner join mapaequivalenciadisciplinacursada   on mapaequivalenciadisciplinacursada.mapaequivalenciadisciplina = mapaequivalenciadisciplina.codigo");
		sqlStr.append(" inner join mapaequivalenciadisciplinamatrizcurricular  on  mapaequivalenciadisciplinamatrizcurricular.mapaequivalenciadisciplina = mapaequivalenciadisciplina.codigo");
		sqlStr.append(" where gradecurricular = ").append(gradeCurricular).append(" and mapaequivalenciadisciplinamatrizcurricular.disciplina = ").append(disciplina).append(" ");
		sqlStr.append(" union all");
		sqlStr.append(" select ").append(disciplina);
		sqlStr.append(" ) ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.academico.MatriculaPeriodoTurmaDisciplinaInterfaceFacade
	 * #retirarReservaTurmaDisciplina(java.util.List)
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void retirarReservaTurmaDisciplina(List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs, UsuarioVO usuario) throws Exception {
		for (MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO : matriculaPeriodoTurmaDisciplinaVOs) {
			TurmaDisciplinaVO turmaDisciplina = getFacadeFactory().getTurmaDisciplinaFacade().consultarNrAlunosMatriculadosPelaTurmaEDisciplina(matriculaPeriodoTurmaDisciplinaVO.getTurma().getCodigo(), matriculaPeriodoTurmaDisciplinaVO.getDisciplina().getCodigo(), false, usuario);
			if (turmaDisciplina.getCodigo().intValue() != 0) {
				if (turmaDisciplina.getNrAlunosMatriculados() > 0) {
					turmaDisciplina.setNrAlunosMatriculados(turmaDisciplina.getNrAlunosMatriculados() - new Integer(1));
					getFacadeFactory().getTurmaDisciplinaFacade().atualizarNrAlunosMatriculados(turmaDisciplina, usuario);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.academico.MatriculaPeriodoTurmaDisciplinaInterfaceFacade
	 * #excluirMatriculaPeriodoTurmaDisciplinas(java.lang.Integer,
	 * java.util.List)
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirMatriculaPeriodoTurmaDisciplinas(Integer matricula, List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs, UsuarioVO usuario) throws Exception {
		List<MatriculaPeriodoTurmaDisciplinaVO> listaItems = consultarMatriculaPeriodoTurmaDisciplinaASeremExcluidas(matricula, matriculaPeriodoTurmaDisciplinaVOs, false, usuario);
		retirarReservaTurmaDisciplina(listaItems, usuario);
		validarExclusaoDoHistoricoPorMatriculaPeriodoTurmaDisciplina(listaItems, usuario);
		StringBuilder sql = new StringBuilder("DELETE FROM MatriculaPeriodoTurmaDisciplina WHERE matriculaPeriodo = ? AND codigo NOT IN (0");
		for (MatriculaPeriodoTurmaDisciplinaVO obj : matriculaPeriodoTurmaDisciplinaVOs) {
			if (obj.getCodigo() != 0) {
				sql.append(", ").append(obj.getCodigo());
			}
		}
		sql.append(")").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		getConexao().getJdbcTemplate().update(sql.toString(), new Object[] { matricula });
		ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoPadraoSistema();
		for(MatriculaPeriodoTurmaDisciplinaVO obj: listaItems) {
			getFacadeFactory().getIntegracaoMestreGRInterfaceFacade().verificarAlunoDisciplinaDelete(obj, usuario, configuracaoGeralSistemaVO);
		}
	}
	
	// Método responsável por remover o histórico do aluno, para não ficar
	// lixo no BD,
	// haja vista, que o aluno não está mais cursando a referida disciplina
	// no período.
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void validarExclusaoDoHistoricoPorMatriculaPeriodoTurmaDisciplina(List<MatriculaPeriodoTurmaDisciplinaVO> listaItems, UsuarioVO usuario) throws Exception {	
		for (MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO : listaItems) {
			getFacadeFactory().getHistoricoFacade().removerVinculoMatriculaPeriodoTurmaDisciplinaHistoricoTransferidoMatrizCurricularAfimDePreservarOHistoricoDaMatrizAntigaCasoTenhaEquivalencia(matriculaPeriodoTurmaDisciplinaVO.getMatricula(), matriculaPeriodoTurmaDisciplinaVO.getCodigo(), 0, matriculaPeriodoTurmaDisciplinaVO.getDisciplina().getCodigo(), usuario);
			if (matriculaPeriodoTurmaDisciplinaVO.getDisciplinaEquivale() || matriculaPeriodoTurmaDisciplinaVO.getDisciplinaPorEquivalencia()) {
				HistoricoVO obj = getFacadeFactory().getHistoricoFacade().consultarPorMatriculaPeriodoTurmaDisciplinaHistoricoEquivalente(matriculaPeriodoTurmaDisciplinaVO.getCodigo(), false, false, usuario);
				getFacadeFactory().getHistoricoFacade().excluirHistoricoForaPrazoVerificandoHistoricoPorEquivalencia(obj, null, usuario);
			} else {
				getFacadeFactory().getHistoricoFacade().excluirPorMatriculaPeriodoTurmaDisciplina(matriculaPeriodoTurmaDisciplinaVO.getCodigo(), usuario);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.academico.MatriculaPeriodoTurmaDisciplinaInterfaceFacade
	 * #alterarMatriculaPeriodos(java.lang.Integer, java.lang.String,
	 * java.util.List)
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	@Override
	public void alterarMatriculaPeriodos(Integer matriculaPeriodo, String matricula, List<MatriculaPeriodoTurmaDisciplinaVO> objetos, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO,  GradeCurricularVO gradeCurricularVO, UsuarioVO usuario) throws Exception {
		excluirMatriculaPeriodoTurmaDisciplinas(matriculaPeriodo, objetos, usuario);
		incluirMatriculaPeriodoTurmaDisciplinas(matriculaPeriodo, matricula, objetos, configuracaoFinanceiroVO, gradeCurricularVO, usuario);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	@Override
	public void incluirMatriculaPeriodosForaPrazo(Integer matriculaPeriodo, String matricula, List<MatriculaPeriodoTurmaDisciplinaVO> objetos, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, GradeCurricularVO gradeCurricularVO, UsuarioVO usuario) throws Exception {
		// excluirMatriculaPeriodoTurmaDisciplinas(matriculaPeriodo, objetos);
		incluirMatriculaPeriodoTurmaDisciplinasForaPrazo(matriculaPeriodo, matricula, objetos, configuracaoFinanceiroVO, gradeCurricularVO, usuario);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.academico.MatriculaPeriodoTurmaDisciplinaInterfaceFacade
	 * #incluirMatriculaPeriodoTurmaDisciplinas(java.lang.Integer,
	 * java.lang.String, java.util.List)
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)	
	public void incluirMatriculaPeriodoTurmaDisciplinasForaPrazo(Integer matriculaPrm, String matricula, List<MatriculaPeriodoTurmaDisciplinaVO> objetos, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, GradeCurricularVO gradeCurricularVO, UsuarioVO usuario) throws Exception {
		// excluirMatriculaPeriodoTurmaDisciplinas(matriculaPrm, objetos);
		Iterator e = objetos.iterator();
		while (e.hasNext()) {
			MatriculaPeriodoTurmaDisciplinaVO obj = (MatriculaPeriodoTurmaDisciplinaVO) e.next();
			obj.setMatricula(matricula);
			obj.setMatriculaPeriodo(matriculaPrm);
			if (obj.getCodigo().intValue() != 0) {
				// alterar(obj);
			} else {
				incluir(obj, configuracaoFinanceiroVO, gradeCurricularVO, usuario);
			}
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirMatriculaPeriodoTurmaDisciplinas(Integer matriculaPrm, String matricula, List objetos, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, GradeCurricularVO gradeCurricularVO, UsuarioVO usuario) throws Exception {
		excluirMatriculaPeriodoTurmaDisciplinas(matriculaPrm, objetos, usuario);
		Iterator e = objetos.iterator();
		while (e.hasNext()) {
			MatriculaPeriodoTurmaDisciplinaVO obj = (MatriculaPeriodoTurmaDisciplinaVO) e.next();
			obj.setMatricula(matricula);
			obj.setMatriculaPeriodo(matriculaPrm);
			if (obj.getCodigo().intValue() != 0) {
				alterar(obj, configuracaoFinanceiroVO, usuario);
			} else {
				incluir(obj, configuracaoFinanceiroVO, gradeCurricularVO, usuario);
			}
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	@Override
	public void incluirMatriculaPeriodoTurmaDisciplinas(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, List<MatriculaPeriodoTurmaDisciplinaVO> objetos, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, GradeCurricularVO gradeCurricularVO, UsuarioVO usuario) throws Exception {
		try {
			excluirMatriculaPeriodoTurmaDisciplinas(matriculaPeriodoVO.getCodigo(), objetos, usuario);
			Iterator e = objetos.iterator();
			while (e.hasNext()) {
				MatriculaPeriodoTurmaDisciplinaVO obj = (MatriculaPeriodoTurmaDisciplinaVO) e.next();
				obj.setSemestre(matriculaPeriodoVO.getSemestre());
				obj.setAno(matriculaPeriodoVO.getAno());
				obj.setMatricula(matriculaVO.getMatricula());
				obj.setMatriculaPeriodo(matriculaPeriodoVO.getCodigo());
				// if (obj.getCodigo().intValue() != 0) {
				if (!obj.getNovoObj()) {
					// //
					// System.out.println("++++++++++++++++++++++ ALTERA MATRICULA PERIODO TURMA DISCIPLINA +++++++++++++++++++++++++++");
					alterar(matriculaVO, matriculaPeriodoVO, obj, configuracaoFinanceiroVO, usuario);
				} else {

					// //
					// System.out.println("++++++++++++++++++++++ INCLUI MATRICULA PERIODO TURMA DISCIPLINA +++++++++++++++++++++++++++");
					incluir(matriculaVO, matriculaPeriodoVO, obj, configuracaoFinanceiroVO, gradeCurricularVO, usuario);
//					getFacadeFactory().getFrequenciaAulaFacade().incluirFrequenciaFaltaAulasRealizadasAposDataMatricula(obj, matriculaPeriodoVO.getData(), usuario);
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.academico.MatriculaPeriodoTurmaDisciplinaInterfaceFacade
	 * #incluirMatriculaPeriodoTurmaDisciplinas(java.lang.Integer,
	 * java.lang.String, java.util.List, java.lang.String, java.lang.String)
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	@Override
	public void incluirMatriculaPeriodoTurmaDisciplinas(Integer matriculaPrm, String matricula, List<MatriculaPeriodoTurmaDisciplinaVO> objetos, String semestre, String ano, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, GradeCurricularVO gradeCurricularVO, UsuarioVO usuario) throws Exception {
		excluirMatriculaPeriodoTurmaDisciplinas(matriculaPrm, objetos, usuario);
		Iterator e = objetos.iterator();
		while (e.hasNext()) {
			MatriculaPeriodoTurmaDisciplinaVO obj = (MatriculaPeriodoTurmaDisciplinaVO) e.next();
			obj.setSemestre(semestre);
			obj.setAno(ano);
			obj.setMatricula(matricula);
			obj.setMatriculaPeriodo(matriculaPrm);
			if (obj.getCodigo().intValue() != 0) {
				alterar(obj, configuracaoFinanceiroVO, usuario);
			} else {
				incluir(obj, configuracaoFinanceiroVO, gradeCurricularVO, usuario);
			}
		}
	}

	/**
	 * Operação responsável por consultar todos os
	 * <code>MatriculaPeriodoVO</code> relacionados a um objeto da classe
	 * <code>academico.Matricula</code>.
	 * 
	 * @param matricula
	 *            Atributo de <code>academico.Matricula</code> a ser utilizado
	 *            para localizar os objetos da classe
	 *            <code>MatriculaPeriodoVO</code>.
	 * @return List Contendo todos os objetos da classe
	 *         <code>MatriculaPeriodoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta
	 *                operação.
	 */
	public List consultarMatriculaPeriodoTurmaDisciplinas(Integer matriculaPeriodo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		MatriculaPeriodo.consultar(getIdEntidade(), controlarAcesso, usuario);
		List objetos = new ArrayList(0);
		String sqlStr = "SELECT distinct MatriculaPeriodoTurmaDisciplina.*, historico.historicodisciplinaforagrade FROM MatriculaPeriodoTurmaDisciplina inner join historico on historico.matricula = MatriculaPeriodoTurmaDisciplina.matricula and historico.MatriculaPeriodoTurmaDisciplina = MatriculaPeriodoTurmaDisciplina.codigo WHERE MatriculaPeriodoTurmaDisciplina.matriculaPeriodo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[] { matriculaPeriodo });
		while (tabelaResultado.next()) {
			MatriculaPeriodoTurmaDisciplinaVO novoObj = new MatriculaPeriodoTurmaDisciplinaVO();
			novoObj = MatriculaPeriodoTurmaDisciplina.montarDados(tabelaResultado, nivelMontarDados, usuario);
			novoObj.setDisciplinaForaGrade(tabelaResultado.getBoolean("historicodisciplinaforagrade"));			
			objetos.add(novoObj);
		}
		return objetos;
	}

	public List consultarMatriculaPeriodoTurmaDisciplinasDaGrade(Integer matriculaPeriodo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		MatriculaPeriodo.consultar(getIdEntidade(), controlarAcesso, usuario);
		List objetos = new ArrayList(0);
		StringBuilder sb = new StringBuilder();
		sb.append("select mptd.* from matriculaperiodoturmadisciplina mptd ");
		sb.append(" inner join matriculaperiodo on matriculaperiodo.codigo = mptd.matriculaperiodo ");
		sb.append(" inner join gradecurricular on gradecurricular.codigo = matriculaperiodo.gradecurricular ");
		sb.append(" inner join periodoletivo on periodoletivo.gradecurricular = gradecurricular.codigo ");
		sb.append(" inner join gradedisciplina on gradedisciplina.periodoletivo = periodoletivo.codigo ");
		sb.append(" inner join disciplina on disciplina.codigo = gradedisciplina.disciplina and disciplina.codigo = mptd.disciplina ");
		sb.append(" inner join historico on historico.matriculaperiodoturmadisciplina = mptd.codigo ");
		sb.append(" where mptd.matriculaperiodo = ? and (historico.situacao <> 'AA' and historico.situacao <> 'AP')");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), new Object[] { matriculaPeriodo });
		while (tabelaResultado.next()) {
			MatriculaPeriodoTurmaDisciplinaVO novoObj = new MatriculaPeriodoTurmaDisciplinaVO();
			novoObj = MatriculaPeriodoTurmaDisciplina.montarDados(tabelaResultado, nivelMontarDados, usuario);
			objetos.add(novoObj);
		}
		return objetos;
	}

	public Integer consultarQuantidadeDisciplinasDaGradePorMatriculaPeriodo(Integer matriculaPeriodo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		MatriculaPeriodo.consultar(getIdEntidade(), controlarAcesso, usuario);
		List objetos = new ArrayList(0);
		StringBuilder sb = new StringBuilder();
		sb.append("select count(distinct mptd.codigo) AS numeroDisc from matriculaperiodoturmadisciplina mptd ");
		sb.append(" inner join matriculaperiodo on matriculaperiodo.codigo = mptd.matriculaperiodo ");
		sb.append(" inner join gradecurricular on gradecurricular.codigo = matriculaperiodo.gradecurricular ");
		sb.append(" inner join periodoletivo on periodoletivo.gradecurricular = gradecurricular.codigo ");
		sb.append(" inner join gradedisciplina on gradedisciplina.periodoletivo = periodoletivo.codigo ");
		sb.append(" inner join disciplina on disciplina.codigo = gradedisciplina.disciplina and disciplina.codigo = mptd.disciplina ");
		sb.append(" inner join historico on historico.matriculaperiodoturmadisciplina = mptd.codigo ");
		sb.append(" where mptd.matriculaperiodo = ? ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), new Object[] { matriculaPeriodo });
		if (tabelaResultado.next()) {
			return tabelaResultado.getInt("numeroDisc");
		}
		return 0;
	}
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public boolean consultarMatriculaPeriodoTurmaDisciplinaPorDisciplinaTcc(Integer gradeCurricular, UsuarioVO usuario) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("select count(distinct mptd.codigo) AS QTDE from matriculaperiodoturmadisciplina mptd ");
		sb.append(" inner join matricula on matricula.matricula = mptd.matricula ");
		sb.append(" inner join gradecurricular on gradecurricular.codigo = matricula.gradecurricularatual ");
		sb.append(" inner join periodoletivo on periodoletivo.gradecurricular = gradecurricular.codigo ");
		sb.append(" inner join gradedisciplina on gradedisciplina.periodoletivo = periodoletivo.codigo ");
		sb.append(" inner join disciplina on disciplina.codigo = gradedisciplina.disciplina and disciplina.codigo = mptd.disciplina ");
		sb.append(" where gradecurricular.codigo = ? ");
		sb.append(" and (gradedisciplina.disciplinatcc = true or disciplina.classificacaoDisciplina = 'TCC') ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), new Object[] { gradeCurricular });
		return Uteis.isAtributoPreenchido(rs, Uteis.QTDE, TipoCampoEnum.INTEIRO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.academico.MatriculaPeriodoTurmaDisciplinaInterfaceFacade
	 * #consultarPorChavePrimaria(java.lang.Integer, int)
	 */
	public MatriculaPeriodoTurmaDisciplinaVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sqlStr = "SELECT * FROM MatriculaPeriodoTurmaDisciplina WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( MatriculaPeriodoTurmaDisciplina ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.academico.MatriculaPeriodoTurmaDisciplinaInterfaceFacade
	 * #consultarDisciplinaDoAlunoPorMatricula(java.lang.Integer,
	 * java.lang.String, int)
	 */
	public List consultarDisciplinaDoAlunoPorMatricula(Integer unidadeEnsino, String matricula, int nivelMontarDados, UsuarioVO usuario) throws SQLException, Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT matriculaperiodoturmadisciplina.* from matriculaperiodoturmadisciplina");
		sqlStr.append(" INNER JOIN matricula ON (matricula.matricula = matriculaperiodoturmadisciplina.matricula)");
		sqlStr.append(" INNER JOIN pessoa ON (matricula.aluno = pessoa.codigo)");
		sqlStr.append(" WHERE matriculaperiodoturmadisciplina.matricula = '" + matricula + "'");
		if (unidadeEnsino != null && unidadeEnsino.intValue() != 0) {
			sqlStr.append(" AND matricula.unidadeensino = " + unidadeEnsino);
		}
		sqlStr.append(" ORDER BY matriculaperiodoturmadisciplina.disciplina; ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List listaResultado = montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
		return listaResultado;
	}

	/**
	 * 
	 * Modificado por Victor Hugo 13/02/2015
	 * 
	 * @see negocio.facade.jdbc.academico.MatriculaPeriodoTurmaDisciplinaInterfaceFacade
	 *      #consultarDisciplinaDoAlunoPorMatricula(java.lang.Integer,
	 *      java.lang.String, int)
	 */
	@Deprecated
	@Override
	public List<MatriculaPeriodoTurmaDisciplinaVO> consultarDisciplinaOnlineDoAlunoPorMatricula(String matricula, int nivelMontarDados, Integer matriculaPeriodoTurmaDisciplina, UsuarioVO usuario) throws SQLException, Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT * FROM ((SELECT matriculaperiodoturmadisciplina.codigo as codigo, matriculaperiodoturmadisciplina.conteudo as conteudo, matriculaperiodoturmadisciplina.professor as professor, ");
		sqlStr.append(" disciplina.nome as nomedisciplina, disciplina.codigo as codigodisciplina, turmadisciplina.ordemestudoonline as ordemestudoonline, turmadisciplina.modalidadedisciplina as modalidade, ");
		sqlStr.append(" classroomGoogle.codigo as classroomGoogle_codigo, classroomGoogle.linkClassroom, ");
		sqlStr.append(" salaaulablackboard.codigo as salaaulablackboard_codigo, salaaulablackboard.linkSalaAulaBlackboard, ");
		sqlStr.append(" salaaulablackboard.id as salaaulablackboard_id, salaaulablackboard.idSalaAulaBlackboard as salaaulablackboard_idSalaAulaBlackboard  ");
		sqlStr.append(" from matriculaperiodoturmadisciplina ");
		sqlStr.append(" INNER JOIN matricula ON (matricula.matricula = matriculaperiodoturmadisciplina.matricula)");
		sqlStr.append(" inner join matriculaperiodo on matriculaperiodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo ");
		sqlStr.append(" left join gradedisciplinacomposta on matriculaperiodoturmadisciplina.gradedisciplinacomposta = gradedisciplinacomposta.codigo ");
		sqlStr.append(" left join gradecurriculargrupooptativadisciplina on ");
		sqlStr.append(" ((gradedisciplinacomposta.codigo is not null and gradedisciplinacomposta.gradecurriculargrupooptativadisciplina = gradecurriculargrupooptativadisciplina.codigo) ");
		sqlStr.append(" or (gradedisciplinacomposta.codigo is null and matriculaperiodoturmadisciplina.disciplinacomposta = false and matriculaperiodoturmadisciplina.gradecurriculargrupooptativadisciplina = gradecurriculargrupooptativadisciplina.codigo)) ");
		sqlStr.append(" left join gradedisciplina on ((gradedisciplinacomposta.codigo is not null and gradedisciplinacomposta.gradedisciplina = gradedisciplina.codigo) ");
		sqlStr.append(" or (gradedisciplinacomposta.codigo is null and matriculaperiodoturmadisciplina.disciplinacomposta = false and matriculaperiodoturmadisciplina.gradedisciplina = gradedisciplina.codigo)) ");
		sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = matriculaperiodoturmadisciplina.turma and ((gradedisciplina.codigo is not null and turmadisciplina.disciplina = gradedisciplina.disciplina)  or (gradecurriculargrupooptativadisciplina.codigo is not null 		 and turmadisciplina.disciplina = gradecurriculargrupooptativadisciplina.disciplina)) ");
		sqlStr.append(" inner join disciplina on disciplina.codigo = matriculaperiodoturmadisciplina.disciplina");
		sqlStr.append(" inner join historico on historico.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo and historico.situacao not in ('TR', 'CA', 'AC', 'TF', 'TI', 'TE') ");
		sqlStr.append(" left join calendarioatividadematricula on calendarioatividadematricula.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo and calendarioatividadematricula.tipocalendarioatividade = '").append(TipoCalendarioAtividadeMatriculaEnum.ACESSO_CONTEUDO_ESTUDO).append("'");
		sqlStr.append(" left join classroomgoogle on classroomgoogle.turma =  matriculaperiodoturmadisciplina.turma ");
		sqlStr.append(" and classroomgoogle.disciplina =  disciplina.codigo ");
		sqlStr.append(" and classroomgoogle.ano =  matriculaperiodoturmadisciplina.ano ");
		sqlStr.append(" and classroomgoogle.semestre =  matriculaperiodoturmadisciplina.semestre ");
		sqlStr.append(" and classroomgoogle.professoread =  matriculaperiodoturmadisciplina.professor ");
		sqlStr.append(" left join salaaulablackboard on salaaulablackboard.codigo =  (select salaaulablackboardpessoa.salaaulablackboard from salaaulablackboardpessoa where salaaulablackboardpessoa.matriculaperiodoturmadisciplina =  matriculaperiodoturmadisciplina.codigo  limit 1 ) ");		
		sqlStr.append(" WHERE matriculaperiodoturmadisciplina.matricula = '" + matricula + "'");
		sqlStr.append(" AND matriculaperiodoturmadisciplina.modalidadeDisciplina = '" + ModalidadeDisciplinaEnum.ON_LINE.name() + "'");
		sqlStr.append(" and matriculaperiodo.situacaomatriculaperiodo = 'AT' ");
		sqlStr.append(" AND ( (calendarioatividadematricula.codigo is null) or (calendarioatividadematricula.tipocalendarioatividade = '").append(TipoCalendarioAtividadeMatriculaEnum.ACESSO_CONTEUDO_ESTUDO).append("' and calendarioatividadematricula.datafim >= '").append(UteisData.getDataJDBC(new Date())).append(" 00:00:00')) ");
		sqlStr.append(" and matriculaperiodoturmadisciplina.codigo in ( ");
		sqlStr.append("   select mptd.codigo from matriculaperiodoturmadisciplina as mptd where mptd.matricula = matricula.matricula ");
		sqlStr.append("   and mptd.disciplina = matriculaperiodoturmadisciplina.disciplina  and mptd.modalidadeDisciplina = matriculaperiodoturmadisciplina.modalidadeDisciplina ");
		sqlStr.append("   order by mptd.ano||'/'||mptd.semestre desc, mptd.codigo desc limit 1 ");
		sqlStr.append(" ) ");
		sqlStr.append(" )");
		sqlStr.append(" union");
		sqlStr.append(" (SELECT matriculaperiodoturmadisciplina.codigo as codigo, matriculaperiodoturmadisciplina.conteudo as conteudo, matriculaperiodoturmadisciplina.professor as professor, ");
		sqlStr.append(" disciplina.nome as nomedisciplina, disciplina.codigo as codigodisciplina, turmadisciplina.ordemestudoonline as ordemestudoonline, turmadisciplina.modalidadedisciplina as modalidade, ");
		sqlStr.append(" classroomGoogle.codigo as classroomGoogle_codigo, classroomGoogle.linkClassroom, ");
		sqlStr.append(" salaaulablackboard.codigo as salaaulablackboard_codigo, salaaulablackboard.linkSalaAulaBlackboard, ");
		sqlStr.append(" salaaulablackboard.id as salaaulablackboard_id, salaaulablackboard.idSalaAulaBlackboard as salaaulablackboard_idSalaAulaBlackboard  ");
		sqlStr.append(" from matriculaperiodoturmadisciplina ");
		sqlStr.append(" INNER JOIN matricula ON (matricula.matricula = matriculaperiodoturmadisciplina.matricula)");
		sqlStr.append(" inner join matriculaperiodo on matriculaperiodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo ");
		sqlStr.append(" left join gradedisciplinacomposta on matriculaperiodoturmadisciplina.gradedisciplinacomposta = gradedisciplinacomposta.codigo ");
		sqlStr.append(" left join gradecurriculargrupooptativadisciplina on ");
		sqlStr.append(" ((gradedisciplinacomposta.codigo is not null and gradedisciplinacomposta.gradecurriculargrupooptativadisciplina = gradecurriculargrupooptativadisciplina.codigo) ");
		sqlStr.append(" or (gradedisciplinacomposta.codigo is null and matriculaperiodoturmadisciplina.disciplinacomposta = false and matriculaperiodoturmadisciplina.gradecurriculargrupooptativadisciplina = gradecurriculargrupooptativadisciplina.codigo)) ");
		sqlStr.append(" left join gradedisciplina on ((gradedisciplinacomposta.codigo is not null and gradedisciplinacomposta.gradedisciplina = gradedisciplina.codigo) ");
		sqlStr.append(" or (gradedisciplinacomposta.codigo is null and matriculaperiodoturmadisciplina.disciplinacomposta = false and matriculaperiodoturmadisciplina.gradedisciplina = gradedisciplina.codigo)) ");
		sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = matriculaperiodoturmadisciplina.turma and ((gradedisciplina.codigo is not null and turmadisciplina.disciplina = gradedisciplina.disciplina)  or (gradecurriculargrupooptativadisciplina.codigo is not null 		 and turmadisciplina.disciplina = gradecurriculargrupooptativadisciplina.disciplina)) ");
		sqlStr.append(" inner join disciplina on disciplina.codigo = matriculaperiodoturmadisciplina.disciplina");
		sqlStr.append(" inner join historico on historico.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo and historico.situacao not in ('TR', 'CA', 'AC', 'TF', 'TI', 'TE') ");
		sqlStr.append(" inner join configuracaoacademico on configuracaoacademico.codigo = historico.configuracaoacademico");
		sqlStr.append(" left join calendarioatividadematricula on calendarioatividadematricula.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo and calendarioatividadematricula.tipocalendarioatividade = '").append(TipoCalendarioAtividadeMatriculaEnum.ACESSO_CONTEUDO_ESTUDO).append("'");
		sqlStr.append(" left join classroomgoogle on classroomgoogle.turma =  matriculaperiodoturmadisciplina.turma ");
		sqlStr.append(" and classroomgoogle.disciplina =  disciplina.codigo ");
		sqlStr.append(" and classroomgoogle.ano =  matriculaperiodoturmadisciplina.ano ");
		sqlStr.append(" and classroomgoogle.semestre =  matriculaperiodoturmadisciplina.semestre ");
		sqlStr.append(" and classroomgoogle.professoread =  matriculaperiodoturmadisciplina.professor ");
		sqlStr.append(" left join salaaulablackboard on salaaulablackboard.codigo =  (select salaaulablackboardpessoa.salaaulablackboard from salaaulablackboardpessoa where salaaulablackboardpessoa.matriculaperiodoturmadisciplina =  matriculaperiodoturmadisciplina.codigo  limit 1 ) ");		
		sqlStr.append(" WHERE matriculaperiodoturmadisciplina.matricula = '" + matricula + "'");
		sqlStr.append(" AND matriculaperiodoturmadisciplina.modalidadeDisciplina = '" + ModalidadeDisciplinaEnum.PRESENCIAL.name() + "'");
		sqlStr.append(" and configuracaoacademico.utilizarApoioEADParaDisciplinasModalidadePresencial = 't' ");
		sqlStr.append(" and matriculaperiodo.situacaomatriculaperiodo = 'AT' ");
		sqlStr.append(" AND ( (calendarioatividadematricula.codigo is null) or (calendarioatividadematricula.tipocalendarioatividade = '").append(TipoCalendarioAtividadeMatriculaEnum.ACESSO_CONTEUDO_ESTUDO).append("' and calendarioatividadematricula.datafim >= '").append(UteisData.getDataJDBC(new Date())).append(" 00:00:00')) ");
		sqlStr.append(" and matriculaperiodoturmadisciplina.codigo in ( ");
		sqlStr.append("   select mptd.codigo from matriculaperiodoturmadisciplina as mptd where mptd.matricula = matricula.matricula ");
		sqlStr.append("   and mptd.disciplina = matriculaperiodoturmadisciplina.disciplina  and mptd.modalidadeDisciplina = matriculaperiodoturmadisciplina.modalidadeDisciplina ");
		sqlStr.append("   order by mptd.ano||'/'||mptd.semestre desc, mptd.codigo desc limit 1 ");
		sqlStr.append(" ))) AS t ");
		if (Uteis.isAtributoPreenchido(matriculaPeriodoTurmaDisciplina)) {
			sqlStr.append(" WHERE codigo = ");
			sqlStr.append(matriculaPeriodoTurmaDisciplina);
		}
		sqlStr.append(" ORDER BY ordemestudoonline ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		
		List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs = new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>();
		MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO = null;
		while(tabelaResultado.next()) {
			matriculaPeriodoTurmaDisciplinaVO = new MatriculaPeriodoTurmaDisciplinaVO();
			matriculaPeriodoTurmaDisciplinaVO.setCodigo(tabelaResultado.getInt("codigo"));
			matriculaPeriodoTurmaDisciplinaVO.getDisciplina().setNome(tabelaResultado.getString("nomedisciplina"));
			matriculaPeriodoTurmaDisciplinaVO.getDisciplina().setCodigo(tabelaResultado.getInt("codigodisciplina"));
			matriculaPeriodoTurmaDisciplinaVO.setOrdemEstudoOnline(tabelaResultado.getInt("ordemestudoonline"));
			matriculaPeriodoTurmaDisciplinaVO.getConteudo().setCodigo(tabelaResultado.getInt("conteudo"));
			matriculaPeriodoTurmaDisciplinaVO.getProfessor().setCodigo(tabelaResultado.getInt("professor"));
			matriculaPeriodoTurmaDisciplinaVO.setModalidadeDisciplina(ModalidadeDisciplinaEnum.valueOf(tabelaResultado.getString("modalidade")));
			matriculaPeriodoTurmaDisciplinaVO.getClassroomGoogleVO().setCodigo(tabelaResultado.getInt("classroomGoogle_codigo"));
			matriculaPeriodoTurmaDisciplinaVO.getClassroomGoogleVO().setLinkClassroom(tabelaResultado.getString("linkClassroom"));
			matriculaPeriodoTurmaDisciplinaVO.getSalaAulaBlackboardVO().setCodigo(tabelaResultado.getInt("salaaulablackboard_codigo"));
			matriculaPeriodoTurmaDisciplinaVO.getSalaAulaBlackboardVO().setId(tabelaResultado.getString("salaaulablackboard_id"));
			matriculaPeriodoTurmaDisciplinaVO.getSalaAulaBlackboardVO().setIdSalaAulaBlackboard(tabelaResultado.getString("salaaulablackboard_idSalaAulaBlackboard"));
			matriculaPeriodoTurmaDisciplinaVO.getSalaAulaBlackboardVO().setLinkSalaAulaBlackboard(tabelaResultado.getString("linkSalaAulaBlackboard"));
			
			matriculaPeriodoTurmaDisciplinaVOs.add(matriculaPeriodoTurmaDisciplinaVO);			
		}
		
		return matriculaPeriodoTurmaDisciplinaVOs;
	}

	public List<MatriculaPeriodoTurmaDisciplinaVO> consultarDisciplinaOnlineDoAlunoPorMatriculaIntegracaoEADIPOG(String matricula, int nivelMontarDados, UsuarioVO usuario) throws SQLException, Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT matriculaperiodoturmadisciplina.*, turmadisciplina.ordemestudoonline from matriculaperiodoturmadisciplina");
		sqlStr.append(" INNER JOIN matricula ON (matricula.matricula = matriculaperiodoturmadisciplina.matricula)");
		sqlStr.append(" inner join turmadisciplina on turmadisciplina.disciplina = matriculaperiodoturmadisciplina.disciplina and turmadisciplina.turma = matriculaperiodoturmadisciplina.turma");
		sqlStr.append(" WHERE matriculaperiodoturmadisciplina.matricula = '" + matricula + "'");
		sqlStr.append(" AND matriculaperiodoturmadisciplina.modalidadeDisciplina = '" + ModalidadeDisciplinaEnum.ON_LINE.name() + "'");
		sqlStr.append(" ORDER BY turmadisciplina.ordemestudoonline, matriculaperiodoturmadisciplina.codigo desc limit 1 ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List listaResultado = montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
		return listaResultado;
	}
	
	public List<MatriculaPeriodoTurmaDisciplinaVO> consultarMatriculaPeriodoTurmaDisciplinaVinculadoATurmaX(String valorParametro, UsuarioVO usuario) throws SQLException, Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT matriculaPeriodoTurmaDisciplina.* from matriculaPeriodoTUrmaDisciplina ");
		sqlStr.append(" inner join matriculaperiodo on matriculaperiodo.codigo = matriculaPeriodoTUrmaDisciplina.matriculaPeriodo ");
		sqlStr.append(" inner join turma on matriculaPeriodoTurmaDisciplina.turma = turma.codigo ");
		sqlStr.append(" where matriculaPeriodo.situacaomatriculaperiodo = 'AT'");
		sqlStr.append(" and turma.identificadorTurma like '%" + valorParametro + "%'");
		sqlStr.append(" order by matriculaperiodo ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List listaResultado = montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS, usuario);
		return listaResultado;
	}

	public void carregarDados(MatriculaPeriodoTurmaDisciplinaVO obj, UsuarioVO usuario) throws Exception {
		carregarDados((MatriculaPeriodoTurmaDisciplinaVO) obj, NivelMontarDados.TODOS, usuario);
	}

	public MatriculaPeriodoTurmaDisciplinaVO carregarDadosTurmaAgrupada(MatriculaPeriodoTurmaDisciplinaVO obj, UsuarioVO usuario) throws Exception {
		carregarDados((MatriculaPeriodoTurmaDisciplinaVO) obj, NivelMontarDados.BASICO, usuario);
		return obj;
	}

	public void carregarDados(MatriculaPeriodoTurmaDisciplinaVO obj, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception {
		SqlRowSet resultado = null;
		if ((nivelMontarDados.equals(NivelMontarDados.BASICO)) && (obj.getIsNivelMontarDadosNaoInicializado())) {
			resultado = consultaRapidaPorChavePrimariaDadosBasicos(obj.getCodigo(), usuario);
			montarDadosBasico(obj, resultado);
		}
		if ((nivelMontarDados.equals(NivelMontarDados.TODOS)) && (!obj.getIsNivelMontarDadosTodos())) {
			resultado = consultaRapidaPorChavePrimariaDadosCompletos(obj.getCodigo(), usuario);
			montarDadosCompleto(obj, resultado);
		}
	}

	public SqlRowSet consultaRapidaPorChavePrimariaDadosBasicos(Integer codigo, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE (matriculaPeriodoTurmaDisciplina.codigo = '").append(codigo).append("')");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return tabelaResultado;
	}

	private SqlRowSet consultaRapidaPorChavePrimariaDadosCompletos(Integer codigo, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaCompleta();
		sqlStr.append(" WHERE (matriculaPeriodoTurmaDisciplina.codigo = '").append(codigo).append("')");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return tabelaResultado;
	}

	private StringBuffer getSQLPadraoConsultaBasica() {
		StringBuffer sql = new StringBuffer();
		sql.append("select distinct matriculaPeriodoTurmaDisciplina.codigo, matriculaPeriodoTurmaDisciplina.disciplina, matriculaPeriodoTurmaDisciplina.turma, matriculaPeriodoTurmaDisciplina.modalidadeDisciplina, ");
		sql.append("matriculaPeriodoTurmaDisciplina.conteudo, matriculaPeriodoTurmaDisciplina.turmapratica, matriculaPeriodoTurmaDisciplina.turmateorica, ");
		sql.append("matriculaPeriodoTurmaDisciplina.permiteEscolherModalidade, ");
		sql.append("matriculaPeriodoTurmaDisciplina.matriculaperiodo, matriculaPeriodoTurmaDisciplina.semestre ,matriculaPeriodoTurmaDisciplina.ano, matriculaPeriodoTurmaDisciplina.bimestre, ");
		sql.append("matriculaPeriodoTurmaDisciplina.matricula, matriculaPeriodoTurmaDisciplina.disciplinaincluida, matriculaPeriodoTurmaDisciplina.disciplinaequivale, ");
		sql.append("matriculaPeriodoTurmaDisciplina.disciplinaEquivalente, matriculaPeriodoTurmaDisciplina.disciplinaFazParteComposicao, matriculaPeriodoTurmaDisciplina.inclusaoforaprazo, ");
		sql.append("matriculaPeriodoTurmaDisciplina.transferenciaMatrizCurricularMatricula, matriculaPeriodoTurmaDisciplina.disciplinaCursandoPorCorrespondenciaAposTransferencia, ");
		sql.append("matriculaPeriodoTurmaDisciplina.mapaEquivalenciaDisciplina, matriculaPeriodoTurmaDisciplina.mapaEquivalenciaDisciplinaCursada, ");
		sql.append("turma.codigo AS \"Turma.codigo\", turma.identificadorTurma AS \"Turma.identificadorTurma\", turma.considerarTurmaAvaliacaoInstitucional AS \"Turma.considerarTurmaAvaliacaoInstitucional\", ");
		sql.append("turmapratica.codigo AS \"turmapratica.codigo\", turmapratica.identificadorTurma AS \"turmapratica.identificadorTurma\",  turmapratica.considerarTurmaAvaliacaoInstitucional AS \"turmapratica.considerarTurmaAvaliacaoInstitucional\",");
		sql.append("turmateorica.codigo AS \"turmateorica.codigo\", turmateorica.identificadorTurma AS \"turmateorica.identificadorTurma\",  turmateorica.considerarTurmaAvaliacaoInstitucional AS \"turmateorica.considerarTurmaAvaliacaoInstitucional\", ");
		sql.append("disciplina.codigo AS \"Disciplina.codigo\", disciplina.nome AS \"Disciplina.nome\", disciplinaequivalente.codigo AS \"Disciplinaequivalente.codigo\", ");
		sql.append("disciplinaequivalente.nome AS \"Disciplinaequivalente.nome\" ");
		sql.append("from matriculaPeriodoTurmaDisciplina ");
		sql.append("LEFT JOIN turma ON turma.codigo = matriculaPeriodoTurmaDisciplina.turma ");
		sql.append("LEFT JOIN turma as turmapratica ON turmapratica.codigo = matriculaPeriodoTurmaDisciplina.turmapratica ");
		sql.append("LEFT JOIN turma as turmateorica ON turmateorica.codigo = matriculaPeriodoTurmaDisciplina.turmateorica ");
		sql.append("LEFT JOIN disciplina ON disciplina.codigo = matriculaPeriodoTurmaDisciplina.disciplina ");
		sql.append("LEFT JOIN disciplina disciplinaequivalente ON disciplinaequivalente.codigo = matriculaPeriodoTurmaDisciplina.disciplinaequivalente ");
		return sql;
	}

	private StringBuffer getSQLPadraoConsultaCompleta() {
		StringBuffer sql = new StringBuffer();
		sql.append("select matriculaPeriodoTurmaDisciplina.codigo, matriculaPeriodoTurmaDisciplina.disciplina, matriculaPeriodoTurmaDisciplina.turma, matriculaPeriodoTurmaDisciplina.modalidadeDisciplina, ");
		sql.append("matriculaPeriodoTurmaDisciplina.conteudo, matriculaPeriodoTurmaDisciplina.gradeDisciplina, matriculaPeriodoTurmaDisciplina.gradeCurricularGrupoOptativaDisciplina, ");
		sql.append("matriculaPeriodoTurmaDisciplina.disciplinaReferenteAUmGrupoOptativa, matriculaPeriodoTurmaDisciplina.permiteEscolherModalidade, matriculaPeriodoTurmaDisciplina.bimestre, ");
		sql.append("matriculaPeriodoTurmaDisciplina.matriculaperiodo, matriculaPeriodoTurmaDisciplina.semestre ,matriculaPeriodoTurmaDisciplina.ano, ");
		sql.append("matriculaPeriodoTurmaDisciplina.matricula, matriculaPeriodoTurmaDisciplina.disciplinaincluida, matriculaPeriodoTurmaDisciplina.disciplinaequivale, ");
		sql.append("matriculaPeriodoTurmaDisciplina.disciplinaEquivalente, matriculaPeriodoTurmaDisciplina.disciplinaFazParteComposicao, matriculaPeriodoTurmaDisciplina.inclusaoforaprazo, ");
		sql.append("matriculaPeriodoTurmaDisciplina.transferenciaMatrizCurricularMatricula, matriculaPeriodoTurmaDisciplina.disciplinaCursandoPorCorrespondenciaAposTransferencia, ");
		sql.append("matriculaPeriodoTurmaDisciplina.mapaEquivalenciaDisciplina, matriculaPeriodoTurmaDisciplina.mapaEquivalenciaDisciplinaCursada, matriculaPeriodoTurmaDisciplina.gradedisciplinacomposta, ");
		sql.append("turma.codigo AS \"Turma.codigo\", turma.identificadorTurma AS \"Turma.identificadorTurma\", ");
		sql.append("turmapratica.codigo AS \"turmapratica.codigo\", turmapratica.identificadorTurma AS \"turmapratica.identificadorTurma\", ");
		sql.append("turmateorica.codigo AS \"turmateorica.codigo\", turmateorica.identificadorTurma AS \"turmateorica.identificadorTurma\", ");
		sql.append("disciplina.codigo AS \"Disciplina.codigo\", disciplina.nome AS \"Disciplina.nome\", disciplinaequivalente.codigo AS \"Disciplinaequivalente.codigo\", ");
		sql.append("disciplinaequivalente.nome AS \"Disciplinaequivalente.nome\", matriculaPeriodoTurmaDisciplina.turmateorica, matriculaPeriodoTurmaDisciplina.turmapratica ");
		sql.append("from matriculaPeriodoTurmaDisciplina ");
		sql.append("LEFT JOIN turma ON turma.codigo = matriculaPeriodoTurmaDisciplina.turma ");
		sql.append("LEFT JOIN turma as turmapratica ON turmapratica.codigo = matriculaPeriodoTurmaDisciplina.turmapratica ");
		sql.append("LEFT JOIN turma as turmateorica ON turmateorica.codigo = matriculaPeriodoTurmaDisciplina.turmateorica ");
		sql.append("LEFT JOIN disciplina ON disciplina.codigo = matriculaPeriodoTurmaDisciplina.disciplina ");
		sql.append("LEFT JOIN disciplina disciplinaequivalente ON disciplinaequivalente.codigo = matriculaPeriodoTurmaDisciplina.disciplinaequivalente ");
		return sql;
	}

	private void montarDadosBasico(MatriculaPeriodoTurmaDisciplinaVO obj, SqlRowSet dadosSQL) throws Exception {
		obj.setNivelMontarDados(NivelMontarDados.BASICO);
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setMatriculaPeriodo(new Integer(dadosSQL.getInt("matriculaPeriodo")));
		obj.getTurma().setCodigo(new Integer(dadosSQL.getInt("turma")));
		obj.getTurmaTeorica().setCodigo(new Integer(dadosSQL.getInt("turmateorica")));
		obj.getTurmaPratica().setCodigo(new Integer(dadosSQL.getInt("turmapratica")));
		obj.getTurma().setConsiderarTurmaAvaliacaoInstitucional((dadosSQL.getBoolean("turma.considerarTurmaAvaliacaoInstitucional")));
		obj.getTurmaTeorica().setConsiderarTurmaAvaliacaoInstitucional((dadosSQL.getBoolean("turmateorica.considerarTurmaAvaliacaoInstitucional")));
		obj.getTurmaPratica().setConsiderarTurmaAvaliacaoInstitucional((dadosSQL.getBoolean("turmapratica.considerarTurmaAvaliacaoInstitucional")));
		obj.getDisciplina().setCodigo(new Integer(dadosSQL.getInt("disciplina")));
		obj.getConteudo().setCodigo(new Integer(dadosSQL.getInt("conteudo")));
		obj.setAno(dadosSQL.getString("ano"));
		obj.setModalidadeDisciplina(ModalidadeDisciplinaEnum.valueOf(dadosSQL.getString("modalidadeDisciplina")));
		obj.setSemestre(dadosSQL.getString("semestre"));
		obj.setBimestre(dadosSQL.getInt("bimestre"));
		obj.setMatricula(dadosSQL.getString("matricula"));
		obj.setDisciplinaIncluida(dadosSQL.getBoolean("disciplinaIncluida"));
		obj.setDisciplinaEquivale(dadosSQL.getBoolean("disciplinaEquivale"));
		obj.setDisciplinaFazParteComposicao(dadosSQL.getBoolean("disciplinaFazParteComposicao"));
		obj.setPermiteEscolherModalidade(dadosSQL.getBoolean("permiteEscolherModalidade"));
		obj.setInclusaoForaPrazo(dadosSQL.getBoolean("inclusaoForaPrazo"));
		// Turma
		obj.getTurma().setCodigo((dadosSQL.getInt("Turma.codigo")));
		obj.getTurma().setIdentificadorTurma((dadosSQL.getString("Turma.identificadorTurma")));
		// Turma Pratica
				obj.getTurmaPratica().setCodigo((dadosSQL.getInt("turmapratica.codigo")));
				obj.getTurmaPratica().setIdentificadorTurma((dadosSQL.getString("turmapratica.identificadorTurma")));
				// Turma Teorica
				obj.getTurmaTeorica().setCodigo((dadosSQL.getInt("turmateorica.codigo")));
				obj.getTurmaTeorica().setIdentificadorTurma((dadosSQL.getString("turmateorica.identificadorTurma")));
		// Disciplina
		obj.getDisciplina().setCodigo(dadosSQL.getInt("Disciplina.codigo"));
		obj.getDisciplina().setNome(dadosSQL.getString("Disciplina.nome"));
		// DisciplinaEquivalente
		obj.getDisciplinaEquivalente().setCodigo((dadosSQL.getInt("Disciplinaequivalente.codigo")));
		obj.getDisciplinaEquivalente().setNome((dadosSQL.getString("Disciplinaequivalente.nome")));
		
		obj.getMapaEquivalenciaDisciplinaVOIncluir().setCodigo(dadosSQL.getInt("mapaEquivalenciaDisciplina"));
		obj.getMapaEquivalenciaDisciplinaCursada().setCodigo(dadosSQL.getInt("mapaEquivalenciaDisciplinaCursada"));
	}

	private void montarDadosCompleto(MatriculaPeriodoTurmaDisciplinaVO obj, SqlRowSet dadosSQL) throws Exception {
		obj.setNivelMontarDados(NivelMontarDados.TODOS);
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setMatriculaPeriodo(new Integer(dadosSQL.getInt("matriculaPeriodo")));
		obj.getTurma().setCodigo(new Integer(dadosSQL.getInt("turma")));
		obj.getDisciplina().setCodigo(new Integer(dadosSQL.getInt("disciplina")));
		obj.getConteudo().setCodigo(new Integer(dadosSQL.getInt("conteudo")));
		obj.setAno(dadosSQL.getString("ano"));
		obj.setBimestre(dadosSQL.getInt("bimestre"));
		obj.setModalidadeDisciplina(ModalidadeDisciplinaEnum.valueOf(dadosSQL.getString("modalidadeDisciplina")));
		obj.setSemestre(dadosSQL.getString("semestre"));
		obj.setMatricula(dadosSQL.getString("matricula"));
		obj.setDisciplinaIncluida(dadosSQL.getBoolean("disciplinaIncluida"));
		obj.setDisciplinaEquivale(dadosSQL.getBoolean("disciplinaEquivale"));
		obj.setDisciplinaFazParteComposicao(dadosSQL.getBoolean("disciplinaFazParteComposicao"));
		obj.setPermiteEscolherModalidade(dadosSQL.getBoolean("permiteEscolherModalidade"));
		obj.getDisciplinaEquivalente().setCodigo(dadosSQL.getInt("disciplinaEquivalente"));
		obj.setInclusaoForaPrazo(dadosSQL.getBoolean("inclusaoForaPrazo"));
		// Turma
		obj.getTurma().setCodigo((dadosSQL.getInt("Turma.codigo")));
		obj.getTurma().setIdentificadorTurma((dadosSQL.getString("Turma.identificadorTurma")));
		// Turma Pratica
		obj.getTurmaPratica().setCodigo((dadosSQL.getInt("turmapratica.codigo")));
		obj.getTurmaPratica().setIdentificadorTurma((dadosSQL.getString("turmapratica.identificadorTurma")));
		// Turma Teorica
		obj.getTurmaTeorica().setCodigo((dadosSQL.getInt("turmateorica.codigo")));
		obj.getTurmaTeorica().setIdentificadorTurma((dadosSQL.getString("turmateorica.identificadorTurma")));
		// Disciplina
		obj.getDisciplina().setCodigo(dadosSQL.getInt("Disciplina.codigo"));
		obj.getDisciplina().setNome(dadosSQL.getString("Disciplina.nome"));
		obj.getGradeDisciplinaVO().setCodigo(dadosSQL.getInt("gradeDisciplina"));
		obj.getGradeDisciplinaCompostaVO().setCodigo(dadosSQL.getInt("gradeDisciplinaComposta"));
		obj.getGradeCurricularGrupoOptativaDisciplinaVO().setCodigo(dadosSQL.getInt("gradeCurricularGrupoOptativaDisciplina"));
		obj.setDisciplinaReferenteAUmGrupoOptativa(dadosSQL.getBoolean("disciplinaReferenteAUmGrupoOptativa"));
		obj.getMapaEquivalenciaDisciplinaVOIncluir().setCodigo(dadosSQL.getInt("mapaEquivalenciaDisciplina"));
		obj.getMapaEquivalenciaDisciplinaCursada().setCodigo(dadosSQL.getInt("mapaEquivalenciaDisciplinaCursada"));
		obj.getTransferenciaMatrizCurricularMatricula().setCodigo(dadosSQL.getInt("transferenciaMatrizCurricularMatricula"));
		obj.setDisciplinaCursandoPorCorrespondenciaAposTransferencia(dadosSQL.getBoolean("disciplinaCursandoPorCorrespondenciaAposTransferencia"));
		obj.getTurmaTeorica().setCodigo(dadosSQL.getInt("turmateorica"));
		obj.getTurmaPratica().setCodigo(dadosSQL.getInt("turmapratica"));		
	}

	public List<MatriculaPeriodoTurmaDisciplinaVO> montarDadosConsultaBasica(SqlRowSet tabelaResultado) throws Exception {
		List<MatriculaPeriodoTurmaDisciplinaVO> vetResultado = new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>(0);
		while (tabelaResultado.next()) {
			MatriculaPeriodoTurmaDisciplinaVO obj = new MatriculaPeriodoTurmaDisciplinaVO();
			montarDadosBasico(obj, tabelaResultado);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	public List<MatriculaPeriodoTurmaDisciplinaVO> montarDadosConsultaCompleta(SqlRowSet tabelaResultado) throws Exception {
		List<MatriculaPeriodoTurmaDisciplinaVO> vetResultado = new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>(0);
		while (tabelaResultado.next()) {
			MatriculaPeriodoTurmaDisciplinaVO obj = new MatriculaPeriodoTurmaDisciplinaVO();
			montarDadosCompleto(obj, tabelaResultado);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return MatriculaPeriodoTurmaDisciplina.idEntidade;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.academico.MatriculaPeriodoTurmaDisciplinaInterfaceFacade
	 * #setIdEntidade(java.lang.String)
	 */
	public void setIdEntidade(String idEntidade) {
		MatriculaPeriodoTurmaDisciplina.idEntidade = idEntidade;
	}

	public Hashtable processarRotinaTurmaXMatriculaPeriodoTurmaDisciplina(String valorParametro, UsuarioVO usuario) throws Exception {
		// Manter na Memoria lista de alunos processados com sucesso.
		Hashtable lista = new Hashtable();
		// consultar os registros que sofreram impacto.
		List<MatriculaPeriodoTurmaDisciplinaVO> matrPerTurDisc = consultarMatriculaPeriodoTurmaDisciplinaVinculadoATurmaX(valorParametro, usuario);
		// consultar a turma equivalente baseado na turma que o aluno está
		// matriculado, porem retirando o X do identificador da turma.
		for (MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO : matrPerTurDisc) {
			String identificadorSemX = matriculaPeriodoTurmaDisciplinaVO.getTurma().getIdentificadorTurma().replaceAll(valorParametro, "");
			try {
				TurmaVO turmaDestino = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(new TurmaVO(), identificadorSemX, 0, false, Uteis.NIVELMONTARDADOS_TODOS, usuario);
				matriculaPeriodoTurmaDisciplinaVO.setTurma(turmaDestino);
				// Alterar todos os registros para a nova turma.
				this.alterarVinculoTurmaSemGerarHistorico(matriculaPeriodoTurmaDisciplinaVO);
				// Alterar matricula para a nova turma.
				getFacadeFactory().getMatriculaPeriodoFacade().alterarTurmaBaseMatriculaPeriodo(matriculaPeriodoTurmaDisciplinaVO.getMatriculaPeriodo(), turmaDestino.getCodigo());
				// montar lista de matriculas que foram processadas
				// corretamente.
			} catch (Exception e) {
				if (!lista.contains(matriculaPeriodoTurmaDisciplinaVO.getMatricula())) {
					lista.put(matriculaPeriodoTurmaDisciplinaVO.getMatricula(), matriculaPeriodoTurmaDisciplinaVO.getMatricula());
				}
			}
		}
		return lista;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarTurmaPorMatriculaPeriodoTurmaDisciplina(Integer matriculaPeriodoTurmaDisciplina, Integer turma, UsuarioVO usuarioVO) throws Exception {
		String sqlStr = "UPDATE matriculaperiodoturmadisciplina SET turma = ?, dataultimaalteracao=? WHERE codigo = ?";
		getConexao().getJdbcTemplate().update(sqlStr, new Object[] { turma, Uteis.getDataJDBCTimestamp(new Date()), matriculaPeriodoTurmaDisciplina });
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void atualizarTurmaMatriculaPeriodoTurmaDisciplina(Integer codigo, Integer codigoTurma, boolean disciplinaIncluida) throws Exception {
		String sqlStr = "UPDATE matriculaperiodoturmadisciplina SET turma = ?, disciplinaIncluida = ?, dataultimaalteracao=? WHERE codigo = ?";
		getConexao().getJdbcTemplate().update(sqlStr, codigoTurma, disciplinaIncluida, Uteis.getDataJDBCTimestamp(new Date()), codigo);
	}

	public List<MatriculaPeriodoTurmaDisciplinaVO> consultarMatriculaPeriodoTurmaDisciplinaPorDisciplinaAnoSemestre(Integer codDisciplina, String ano, String semestre, UsuarioVO usuario) throws SQLException, Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select * from matriculaPeriodoTurmaDisciplina where disciplina = " + codDisciplina + " and ano = '" + ano + "' and semestre = '" + semestre + "'");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS, usuario);
	}

	public List<DisciplinaVO> montarListaDisciplinasDeAcordoComMatriculaPeriodoTurmaDisciplina(List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs) {
		List<DisciplinaVO> disciplinaVOs = new ArrayList(0);
		for (MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO : matriculaPeriodoTurmaDisciplinaVOs) {
			disciplinaVOs.add(matriculaPeriodoTurmaDisciplinaVO.getDisciplina());
		}
		return disciplinaVOs;
	}

	public Integer consultarCodigoPelaMatriculaTurmaDisciplina(String matricula, Integer codigoTurma, Integer codigoDisciplina) throws Exception {
		String sqlStr = "SELECT codigo FROM matriculaperiodoturmadisciplina WHERE matricula = '" + matricula + "' AND turma = " + codigoTurma + " AND disciplina = " + codigoDisciplina + " ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (!tabelaResultado.next()) {
			return 0;
		}
		return tabelaResultado.getInt("codigo");
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirComBaseNaMatricula(String matricula, ConfiguracaoFinanceiroVO confFinanVO, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sqlStr = new StringBuilder("DELETE FROM matriculaperiodoturmadisciplina WHERE matricula = '").append(matricula).append("' ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado));
		try {
			getConexao().getJdbcTemplate().update(sqlStr.toString());
		} finally {
			sqlStr = null;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirComBaseNaMatriculaPeriodo(Integer matriculaPeriodo, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sqlStr = new StringBuilder("DELETE FROM matriculaperiodoturmadisciplina WHERE matriculaPeriodo = ").append(matriculaPeriodo).append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado));
		try {
			getConexao().getJdbcTemplate().update(sqlStr.toString());
		} finally {
			sqlStr = null;
		}
	}

	public List<MatriculaPeriodoTurmaDisciplinaVO> consultarPorCodigoDisciplinaExcluirDisciplinaPermanecerUnificacaoDisciplina(Integer disciplinaExcluir, Integer disciplinaPermanecer, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select mptd1.* from matriculaperiodoturmadisciplina mptd1, matriculaperiodoturmadisciplina mptd2 ");
		sqlStr.append(" where mptd1.matriculaperiodo = mptd2.matriculaPeriodo and mptd1.disciplina = ");
		sqlStr.append(disciplinaExcluir);
		sqlStr.append(" and mptd2.disciplina = ");
		sqlStr.append(disciplinaPermanecer);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<MatriculaPeriodoTurmaDisciplinaVO> consultarPorCodigoDisciplinaExcluirMatriculaPeriodoTurmaDisciplinaComAMesmaEquivalente(Integer disciplinaExcluir, Integer disciplinaPermanecer, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select * from matriculaperiodoturmadisciplina ");
		sqlStr.append(" where disciplinaequivalente = ");
		sqlStr.append(disciplinaExcluir);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public Boolean consultarExistenciaMatriculaPeriodoTurmaDisciplinaPorCodigoDisciplina(Integer disciplina, Integer matriculaPeriodo) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT * FROM matriculaperiodoturmadisciplina WHERE disciplina = ");
		sqlStr.append(disciplina);
		sqlStr.append(" and matriculaperiodo = ");
		sqlStr.append(matriculaPeriodo);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return true;
		}
		return false;
	}

	public MatriculaPeriodoTurmaDisciplinaVO consultarPorMatriculaPeriodoDisciplina(Integer matriculaPeriodo, Integer disciplina, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM MatriculaPeriodoTurmaDisciplina WHERE matriculaPeriodo = " + matriculaPeriodo.intValue() + " and disciplina = " + disciplina.intValue() + " ORDER BY matriculaPeriodo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (!tabelaResultado.next()) {
			return null;
		}
		return montarDados(tabelaResultado, nivelMontarDados, usuario);
	}
	
	@Override
	public MatriculaPeriodoTurmaDisciplinaVO gerarMatriculaPeriodoTurmaDiscipinaPorAlteracaoMatrizCurricular(TurmaVO turma, TurmaDisciplinaVO td, MatriculaPeriodoVO mp) throws Exception {
		MatriculaPeriodoTurmaDisciplinaVO mptd = new MatriculaPeriodoTurmaDisciplinaVO();
		mptd.setTurma(turma);
		mptd.setDisciplina(td.getDisciplina());
		mptd.setMatricula(mp.getMatricula());
		mptd.setMatriculaObjetoVO(mp.getMatriculaVO());
		mptd.setMatriculaPeriodo(mp.getCodigo());
		mptd.setMatriculaPeriodoObjetoVO(mp);
		mptd.setDisciplinaIncluida(true);
		mptd.setGradeDisciplinaVO(td.getGradeDisciplinaVO());
		mptd.setModalidadeDisciplina(td.getModalidadeDisciplina());
		if(mptd.getModalidadeDisciplina().isAmbas()){
			mptd.setPermiteEscolherModalidade(true);
		}
		return mptd;
	}

	public void inicializarDadosMatriculaPeriodoTurmaDisciplinaUnificacaoDisciplina(MatriculaPeriodoTurmaDisciplinaVO mptdPermanecer, MatriculaPeriodoTurmaDisciplinaVO mptdExcluir) {
		mptdPermanecer.setTurma(mptdExcluir.getTurma());
		mptdPermanecer.setSemestre(mptdExcluir.getSemestre());
		mptdPermanecer.setModalidadeDisciplina(mptdExcluir.getModalidadeDisciplina());
		mptdPermanecer.setConteudo(mptdExcluir.getConteudo());
		mptdPermanecer.setAno(mptdExcluir.getAno());
		mptdPermanecer.setDisciplinaIncluida(mptdExcluir.getDisciplinaIncluida());
		mptdPermanecer.setDisciplinaEquivale(mptdExcluir.getDisciplinaEquivale());
		mptdPermanecer.setDisciplinaEquivalente(mptdExcluir.getDisciplinaEquivalente());
		mptdPermanecer.setPermiteEscolherModalidade(mptdExcluir.getPermiteEscolherModalidade());
		mptdPermanecer.setMatricula(mptdExcluir.getMatricula());
	}

	public List<Integer> consultarPorCodigoDisciplina(Integer codigoExcluir) {
		List<Integer> listaMatriculaPeriodo = new ArrayList(0);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT matriculaPeriodo FROM  matriculaPeriodoTurmaDisciplina WHERE disciplina = ");
		sqlStr.append(codigoExcluir);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		while (tabelaResultado.next()) {
			listaMatriculaPeriodo.add(tabelaResultado.getInt("matriculaPeriodo"));
		}
		return listaMatriculaPeriodo;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	@Override
	public void atualizarDataNotificacaoFrequenciaBaixa(List<Integer> matriculaPeriodoTurmaDisciplinaVOs) {
		StringBuilder sql = new StringBuilder("update matriculaPeriodoTurmaDisciplina set dataNotificacaoFrequenciaBaixa =  current_date, dataultimaalteracao=now() where codigo in ( 0 ");
		for (Integer codigo : matriculaPeriodoTurmaDisciplinaVOs) {
			sql.append(", ").append(codigo);
		}
		sql.append(")");
		getConexao().getJdbcTemplate().update(sql.toString());

	}

	public Boolean consultaRapidaReposicaoInclusaoAluno(String matricula, Integer disciplina, Integer codMatriculaPeriodoTurmaDisciplina) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select matriculaperiodoturmadisciplina.codigo from matriculaperiodoturmadisciplina inner join historico on historico.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo where (reposicao = true or inclusaoforaprazo = true) and matriculaperiodoturmadisciplina.matricula = '").append(matricula).append("'");
		sqlStr.append(" and matriculaperiodoturmadisciplina.disciplina = ").append(disciplina).append("");
		sqlStr.append(" and (historico.situacao = 'CS' or historico.situacao = 'AP') ");
		// sqlStr.append(" and codigo = ").append(codMatriculaPeriodoTurmaDisciplina).append("");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Integer consultarTotalPorGradeDisciplina(GradeDisciplinaVO gradeDisciplina, UsuarioVO usuarioVO) throws Exception {

		StringBuilder sqlStr = new StringBuilder("select count(matriculaperiodoturmadisciplina.codigo) as total from matricula ");
		sqlStr.append(" inner join historico on historico.matricula = matricula.matricula ");
		sqlStr.append(" inner join matriculaperiodo on historico.matriculaperiodo = matriculaperiodo.codigo ");
		sqlStr.append(" inner join gradedisciplina on gradedisciplina.periodoletivo = matriculaperiodo.periodoletivomatricula  and gradedisciplina.disciplina = historico.disciplina ");
		sqlStr.append(" inner join matriculaperiodoturmadisciplina on historico.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo ");
		sqlStr.append(" and historico.situacao = 'CS' ");
		sqlStr.append(" and historico.disciplina = ").append(gradeDisciplina.getDisciplina().getCodigo());
		sqlStr.append(" and matricula.situacao = 'AT' ");
		sqlStr.append(" and gradedisciplina.codigo = ").append(gradeDisciplina.getCodigo());

		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		int total = 0;
		if (rs.next()) {
			total = rs.getInt("total");
		}
		return total;
	}

	@Override
	public List<MatriculaPeriodoTurmaDisciplinaTCCVO> consultarPorGradeDisciplina(GradeDisciplinaVO gradeDisciplina, UsuarioVO usuarioVO) throws Exception {

		StringBuilder sqlStr = new StringBuilder("select matricula.curso, matricula.aluno, matriculaperiodo.gradecurricular, matriculaperiodoturmadisciplina.codigo, matricula.matricula from matricula ");
		sqlStr.append(" inner join historico on historico.matricula = matricula.matricula ");
		sqlStr.append(" inner join matriculaperiodo on historico.matriculaperiodo = matriculaperiodo.codigo ");
		sqlStr.append(" inner join gradedisciplina on gradedisciplina.periodoletivo = matriculaperiodo.periodoletivomatricula  and gradedisciplina.disciplina = historico.disciplina ");
		sqlStr.append(" inner join matriculaperiodoturmadisciplina on historico.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo ");
		sqlStr.append(" and historico.situacao = 'CS' ");
		sqlStr.append(" and historico.disciplina = ").append(gradeDisciplina.getDisciplina().getCodigo());
		sqlStr.append(" and matricula.situacao = 'AT' ");
		sqlStr.append(" and gradedisciplina.codigo = ").append(gradeDisciplina.getCodigo());

		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<MatriculaPeriodoTurmaDisciplinaTCCVO> lista = new ArrayList<MatriculaPeriodoTurmaDisciplinaTCCVO>(0);
		MatriculaPeriodoTurmaDisciplinaTCCVO mptdtcc = null;
		while (rs.next()) {
			mptdtcc = new MatriculaPeriodoTurmaDisciplinaTCCVO();
			mptdtcc.setCurso(rs.getInt("curso"));
			mptdtcc.setAluno(rs.getInt("aluno"));
			mptdtcc.setGradeCurricular(rs.getInt("gradecurricular"));

			mptdtcc.setCodigo(rs.getInt("codigo"));
			lista.add(mptdtcc);
		}
		return lista;
	}

	@Override
	public List<MatriculaPeriodoTurmaDisciplinaVO> consultarAlunoSubturmaPorTurmaPrincipalAnoSemestreDisciplina(TurmaVO turmaPrincipal, boolean subturma, String ano, String semestre, Integer disciplina, String situacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioVO, TipoSubTurmaEnum tipoSubTurma) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioVO);
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append("select distinct (matricula.matricula) as matricula, pessoa.codigo as aluno, matriculaperiodo.situacaomatriculaperiodo as situacao, matriculaperiodoturmadisciplina.codigo, ");
		sqlStr.append("turmabase.codigo as turma, turmabase.identificadorturma, turmabase.anual, turmabase.semestral,  disciplina.codigo as disciplina, disciplina.nome as nomeDisciplina, matriculaperiodoturmadisciplina.matriculaperiodo as matriculaperiodo, turma.nrMaximoMatricula as nrMaximoMatricula, ");
		sqlStr.append("matriculaperiodo.ano as ano, matriculaperiodo.semestre as semestre, pessoa.nome as nomeAluno, MatriculaPeriodoTurmaDisciplina.gradeDisciplina, ");
		sqlStr.append("MatriculaPeriodoTurmaDisciplina.ano as \"MatriculaPeriodoTurmaDisciplina.ano\", MatriculaPeriodoTurmaDisciplina.semestre as \"MatriculaPeriodoTurmaDisciplina.semestre\", ");
		sqlStr.append("MatriculaPeriodoTurmaDisciplina.turmaTeorica, MatriculaPeriodoTurmaDisciplina.turmaPratica, turmapratica.turmaagrupada as turmapraticaagrupada, turmateorica.turmaagrupada as turmateoricaagrupada ");
		sqlStr.append("from matriculaperiodoturmadisciplina ");
		if (tipoSubTurma.equals(TipoSubTurmaEnum.TEORICA) && subturma) {
			sqlStr.append("inner join turma on turma.codigo = matriculaperiodoturmadisciplina.turmaTeorica ");
		} else if (tipoSubTurma.equals(TipoSubTurmaEnum.PRATICA) && subturma) {
			sqlStr.append("inner join turma on turma.codigo = matriculaperiodoturmadisciplina.turmaPratica ");
		} else {
			sqlStr.append("inner join turma on turma.codigo = matriculaperiodoturmadisciplina.turma ");
		}
		sqlStr.append("inner join matriculaperiodo on matriculaperiodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo ");
		sqlStr.append("inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
		sqlStr.append("inner join pessoa on pessoa.codigo = matricula.aluno ");
		sqlStr.append("inner join turma as turmabase on turmabase.codigo = matriculaperiodoturmadisciplina.turma ");
		sqlStr.append("left join turma as turmapratica on turmapratica.codigo = matriculaperiodoturmadisciplina.turmapratica ");
		sqlStr.append("left join turma as turmateorica on turmateorica.codigo = matriculaperiodoturmadisciplina.turmateorica ");
		sqlStr.append("inner join disciplina on disciplina.codigo = matriculaperiodoturmadisciplina.disciplina ");
		sqlStr.append("inner join historico on historico.matricula =  matricula.matricula and historico.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo ");
		if(turmaPrincipal.getTurmaAgrupada() && !turmaPrincipal.getSubturma()) {
			sqlStr.append("WHERE (Turma.codigo in (select turma from turmaagrupada where turmaorigem = ").append(turmaPrincipal.getCodigo()).append(")) ");
		}else {
			sqlStr.append("WHERE (Turma.codigo = ").append(turmaPrincipal.getCodigo()).append(") ");
		}		
		if (!ano.equals("")) {
			sqlStr.append(" AND MatriculaPeriodo.ano = '").append(ano).append("'");
		}
		if (!semestre.equals("")) {
			sqlStr.append(" AND MatriculaPeriodo.semestre = '").append(semestre).append("'");
		}
		if (disciplina > 0 && !turmaPrincipal.getTurmaAgrupada()) {
			sqlStr.append(" AND MatriculaPeriodoTurmaDisciplina.disciplina = ").append(disciplina);
		}
		if (disciplina > 0 && turmaPrincipal.getTurmaAgrupada()) {
			sqlStr.append(" AND (MatriculaPeriodoTurmaDisciplina.disciplina = ").append(disciplina);
			sqlStr.append(" or exists (select disciplinaequivalente.disciplina from disciplinaequivalente where disciplinaequivalente.equivalente = ").append(disciplina).append(" and  MatriculaPeriodoTurmaDisciplina.disciplina =  disciplinaequivalente.disciplina) ");
			sqlStr.append(" or exists (select disciplinaequivalente.equivalente from disciplinaequivalente where disciplinaequivalente.disciplina = ").append(disciplina).append(" and  MatriculaPeriodoTurmaDisciplina.disciplina =  disciplinaequivalente.equivalente) ");
			sqlStr.append(" or (MatriculaPeriodoTurmaDisciplina.disciplina in (select disciplinaequivalenteturmaagrupada from turmadisciplina where disciplinaequivalenteturmaagrupada is not null and turmadisciplina.disciplina = ").append(disciplina).append(" and turmadisciplina.turma = ").append(turmaPrincipal.getCodigo()).append("))");
			sqlStr.append(" )");
		}
		sqlStr.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual("and"));
		sqlStr.append(" AND Turma.subturma = ").append(subturma);
		if (tipoSubTurma.equals(TipoSubTurmaEnum.TEORICA) && !subturma) {
			sqlStr.append(" AND matriculaperiodoturmadisciplina.turmaTeorica is null ");
		} else if (tipoSubTurma.equals(TipoSubTurmaEnum.PRATICA) && !subturma) {
			sqlStr.append(" AND matriculaperiodoturmadisciplina.turmaPratica is null ");
		}
		if (situacao.equals("AT")) {
			sqlStr.append(" AND (MatriculaPeriodo.situacaomatriculaperiodo = 'AT') ");
		} else if (situacao.equals("PR")) {
			sqlStr.append(" AND (MatriculaPeriodo.situacaomatriculaperiodo = 'PR') ");
		} else {
			sqlStr.append(" AND (MatriculaPeriodo.situacaomatriculaperiodo = 'AT' or MatriculaPeriodo.situacaomatriculaperiodo = 'PR') ");
		}
		sqlStr.append(" ORDER BY pessoa.nome");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaAlunoSubturmaPorTurmaPrincipalAnoSemestreDisciplina(rs);
	}

	/**
	 * Método responsável por atualizar os atributos nrAlunosMatriculas e
	 * nrVadasDisponiveis no objeto MatriculaPeriodoTurmaDisciplinaVO.
	 * Considerando um determinado ano e semestre
	 */
	public void atualizarNrAlunosMatriculadosTurmaDisciplina(MatriculaPeriodoVO matriculaPeriodoVO, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplina, DisciplinaVO disciplinaVO, String ano, String semestre, Boolean considerarAlunosPreMatriculados, Boolean considerarVagasReposicao) throws Exception {
		if (matriculaPeriodoTurmaDisciplina.getDisciplinaComposta()) {
			MatriculaPeriodoTurmaDisciplinaVO mptdFazParteComposicao = null;
			for(MatriculaPeriodoTurmaDisciplinaVO mptd : matriculaPeriodoVO.getMatriculaPeriodoTumaDisciplinaVOs()){
				if(mptd.getDisciplinaFazParteComposicao() && Uteis.isAtributoPreenchido(mptd.getTurma().getCodigo())
						&&  ((Uteis.isAtributoPreenchido(mptd.getGradeDisciplinaCompostaVO().getGradeDisciplina().getCodigo()) && Uteis.isAtributoPreenchido(matriculaPeriodoTurmaDisciplina.getGradeDisciplinaVO().getCodigo()) && mptd.getGradeDisciplinaCompostaVO().getGradeDisciplina().getCodigo().equals(matriculaPeriodoTurmaDisciplina.getGradeDisciplinaVO().getCodigo()))
						||  (Uteis.isAtributoPreenchido(mptd.getGradeDisciplinaCompostaVO().getGradeCurricularGrupoOptativaDisciplina().getCodigo()) && Uteis.isAtributoPreenchido(matriculaPeriodoTurmaDisciplina.getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo()) 
								&& mptd.getGradeDisciplinaCompostaVO().getGradeCurricularGrupoOptativaDisciplina().getCodigo().equals(matriculaPeriodoTurmaDisciplina.getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo())))){
						getFacadeFactory().getTurmaFacade().consultarNumeroVagasDisponivelParaMatriculaPeriodoTurmaDisciplina(mptd, disciplinaVO, ano, semestre, considerarAlunosPreMatriculados, considerarVagasReposicao);
						if(mptdFazParteComposicao == null){
							mptdFazParteComposicao = mptd;
						}
				}
			}
			if(mptdFazParteComposicao != null){
				getFacadeFactory().getMatriculaPeriodoFacade().realizarDefinicaoNumeroVagaDisciplinaCompostaComLimitacaoDisciplina(matriculaPeriodoVO, mptdFazParteComposicao);
			}
		}else{
			getFacadeFactory().getTurmaFacade().consultarNumeroVagasDisponivelParaMatriculaPeriodoTurmaDisciplina(matriculaPeriodoTurmaDisciplina, disciplinaVO, ano, semestre, considerarAlunosPreMatriculados, considerarVagasReposicao);
		}
	}

	public Integer consultarNrVagasAnoSemestreTurma_VagaTurmaDisciplinaAno(Integer turma, Integer disciplina, String ano, String semestre, Boolean considerarVagaReposicao) {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select distinct turma.").append(considerarVagaReposicao ? "nrVagasInclusaoReposicao": "nrvagas").append(" as vagaturma,  ");
		sqlStr.append(" (select vagaturmadisciplina.").append(considerarVagaReposicao ? "nrVagasMatriculaReposicao" : "nrvagasmatricula").append(" from vagaturma ");  
		sqlStr.append(" inner join vagaturmadisciplina on vagaturmadisciplina.vagaturma = vagaturma.codigo ");
		sqlStr.append(" where vagaturma.turma = turma.codigo ");
		if (Uteis.isAtributoPreenchido(ano)) {
			sqlStr.append(" and vagaturma.ano = '").append(ano).append("' ");
		}
		if (Uteis.isAtributoPreenchido(semestre)) {
			sqlStr.append(" and vagaturma.semestre = '").append(semestre).append("' ");
		}
		if (Uteis.isAtributoPreenchido(disciplina)) {
			sqlStr.append(" and vagaturmadisciplina.disciplina = ").append(disciplina).append(" ");
		}
		sqlStr.append(" limit 1) vagadisciplina ");
		sqlStr.append(" from turma ");		
		sqlStr.append(" where turma.codigo = ").append(turma).append(" ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());		
		if (tabelaResultado.next()) {						
			return tabelaResultado.getObject("vagadisciplina")!=null?tabelaResultado.getInt("vagadisciplina"):tabelaResultado.getInt("vagaturma");
		} else {
			return 0;
		}
	}	

	public Integer consultarNrMaximoVagasAnoSemestreTurma_VagaTurmaDisciplinaAno(Integer turma, Integer disciplina, String ano, String semestre) {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select distinct turma.nrmaximomatricula as vagaturma,  ");
		sqlStr.append(" (select vagaturmadisciplina.nrmaximomatricula from vagaturma ");  
		sqlStr.append(" inner join vagaturmadisciplina on vagaturmadisciplina.vagaturma = vagaturma.codigo ");
		sqlStr.append(" where vagaturma.turma = turma.codigo ");
		if (Uteis.isAtributoPreenchido(ano)) {
			sqlStr.append(" and vagaturma.ano = '").append(ano).append("' ");
		}
		if (Uteis.isAtributoPreenchido(semestre)) {
			sqlStr.append(" and vagaturma.semestre = '").append(semestre).append("' ");
		}
		if (Uteis.isAtributoPreenchido(disciplina)) {
			sqlStr.append(" and vagaturmadisciplina.disciplina = ").append(disciplina).append(" ");
		}
		sqlStr.append(" limit 1) vagadisciplina ");
		sqlStr.append(" from turma ");		
		sqlStr.append(" where turma.codigo = ").append(turma).append(" ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());		
		if (tabelaResultado.next()) {						
			return tabelaResultado.getObject("vagadisciplina")!=null?tabelaResultado.getInt("vagadisciplina"):tabelaResultado.getInt("vagaturma");
		} else {
			return 0;
		}
	}	
	
	/**
	 * Método responsável por retornar o nr. alunos matriculados em uma
	 * determinada turmaDisciplina (seja para uma gradeDisciplina ou
	 * gradeCurricularGrupoOptativaDisciplina para um determinado ano / semestre
	 * (depedendo da periodicidade de um determinado curso).
	 */
	@Override
	public Integer consultarNrAlunosMatriculadosTurmaDisciplina(Integer codigoTurma, Integer codigoDisciplina, String ano, String semestre, Boolean considerarAlunosPreMatriculados, TipoSubTurmaEnum tipoSubTurma, String matriculaDesconsiderar, Boolean considerarVagaReposicao, Boolean turmaAgrupada) throws Exception {
		StringBuilder sqlStr = getSqlPadraoConsultarVagasOcupadas(codigoTurma.toString(), codigoDisciplina, ano, semestre, considerarAlunosPreMatriculados, tipoSubTurma, matriculaDesconsiderar, considerarVagaReposicao, turmaAgrupada);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getInt("nrAlunosMatriculados");
		}
		return 0;
	}
	@Override
	public StringBuilder getSqlPadraoConsultarVagasOcupadas(String sqlTurma, Integer codigoDisciplina, String ano, String semestre, Boolean considerarAlunosPreMatriculados, TipoSubTurmaEnum tipoSubTurma, String matriculaDesconsiderar, Boolean considerarVagaReposicao, Boolean turmaAgrupada) throws Exception{
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" (select count(distinct mptd.matriculaPeriodo) as nrAlunosMatriculados from matriculaPeriodoTurmaDisciplina mptd ");
		sqlStr.append(" inner join matriculaperiodo mp on (mptd.matriculaPeriodo = mp.codigo) ");
		sqlStr.append(" inner join matricula m on (m.matricula = mp.matricula) ");
		sqlStr.append(" inner join curso as c on (c.codigo = m.curso) ");
		if (tipoSubTurma != null) {
			if (tipoSubTurma.equals(TipoSubTurmaEnum.TEORICA)) {
				sqlStr.append("inner join turma as t on t.codigo = mptd.turmaTeorica ");
			} else if (tipoSubTurma.equals(TipoSubTurmaEnum.PRATICA)) {
				sqlStr.append("inner join turma as t on t.codigo = mptd.turmaPratica ");
			} else if (tipoSubTurma.equals(TipoSubTurmaEnum.GERAL)) {
				sqlStr.append("inner join turma as t on t.codigo = mptd.turma");
			}
		} else {
			sqlStr.append("inner join turma as t on t.codigo = mptd.turma ");
		}
		sqlStr.append(" inner join unidadeensino ue on ue.codigo = m.unidadeensino  ");
		sqlStr.append(" inner join configuracoes conf on conf.codigo = ue.configuracoes  ");
		sqlStr.append(" inner join configuracaogeralsistema confg on confg.configuracoes = conf.codigo  ");
		sqlStr.append(" inner join historico as h on h.matricula = m.matricula and h.matriculaPeriodoTurmaDisciplina = mptd.codigo");
		sqlStr.append(" where (t.codigo = ").append(sqlTurma).append(" ) ");
		if(turmaAgrupada != null && turmaAgrupada) {
			sqlStr.append(" and (mptd.disciplina=").append(codigoDisciplina).append(" ");
			sqlStr.append(" or mptd.disciplina in (select disciplinaequivalente.disciplina from disciplinaequivalente where equivalente = ").append(codigoDisciplina).append(") ");
			sqlStr.append(" or mptd.disciplina in (select disciplinaequivalente.equivalente from disciplinaequivalente where disciplina = ").append(codigoDisciplina).append(") ");
			sqlStr.append(" or (mptd.disciplina in (select disciplinaequivalenteturmaagrupada from turmadisciplina where disciplinaequivalenteturmaagrupada is not null and turmadisciplina.disciplina = ").append(codigoDisciplina).append(" and turmadisciplina.turma = ").append(sqlTurma).append("))");
			sqlStr.append(" ) ");
		}else {			
			sqlStr.append(" and (mptd.disciplina=").append(codigoDisciplina).append(" ");			
			sqlStr.append(" or (t.turmaagrupada and mptd.disciplina in (select disciplinaequivalente.disciplina from disciplinaequivalente where equivalente = ").append(codigoDisciplina).append(")) ");
			sqlStr.append(" or (t.turmaagrupada and mptd.disciplina in (select disciplinaequivalente.equivalente from disciplinaequivalente where disciplina = ").append(codigoDisciplina).append(")) ");
			sqlStr.append(" or (t.turmaagrupada and (mptd.disciplina in (select disciplinaequivalenteturmaagrupada from turmadisciplina where disciplinaequivalenteturmaagrupada is not null and turmadisciplina.disciplina = ").append(codigoDisciplina).append(" and turmadisciplina.turma = ").append(sqlTurma).append(")))");
			sqlStr.append(" ) ");
		}
		if(considerarVagaReposicao != null && considerarVagaReposicao) {
			sqlStr.append(" and mptd.reposicao = true  ");			
		}else {
			sqlStr.append(" and case when (confg.desconsiderarAlunoReposicaoVagasTurma = true ) ");
			sqlStr.append(" then (mptd.reposicao = false or mptd.reposicao is null) else true end  ");
		}
		sqlStr.append(" and case when (c.periodicidade  in ('SE', 'IN')) then ((mp.ano = '").append(ano).append("') and (mp.semestre = '").append(semestre).append("')) ");
		sqlStr.append("          when (c.periodicidade = 'AN') then (mp.ano = '").append(ano).append("') else (c.periodicidade = 'IN') end ");
		String situacaoValidas = "'AT','PR','CO'";
		if (!considerarAlunosPreMatriculados) {
			situacaoValidas = "'AT','CO'";
		}
		sqlStr.append(" and (m.situacao = 'AT' and (mp.situacaoMatriculaPeriodo in (").append(situacaoValidas).append("))) ");
		if(matriculaDesconsiderar != null && !matriculaDesconsiderar.trim().isEmpty()){
			sqlStr.append(" and (m.matricula != '").append(matriculaDesconsiderar).append("') ");
		}
		/**
		 * Adicionada regra para resolver impactos relacionados a alunos que
		 * estão Cursando por Correspondência e que disciplinas saiam duplicadas
		 * no Boletim Acadêmico
		 */
		sqlStr.append(" and ((m.gradecurricularatual = h.matrizcurricular");
		sqlStr.append(" and (h.historicocursandoporcorrespondenciaapostransferencia is null or");
		sqlStr.append(" h.historicocursandoporcorrespondenciaapostransferencia = false)");
		sqlStr.append(" and (h.transferenciamatrizcurricularmatricula IS NULL OR (h.transferenciamatrizcurricularmatricula IS NOT NULL ");
		sqlStr.append(" and h.disciplina not in (select his.disciplina from historico his");
		sqlStr.append(" where his.matricula = h.matricula");
		sqlStr.append(" and his.anohistorico = h.anohistorico");
		sqlStr.append(" and his.semestrehistorico = h.semestrehistorico");
		sqlStr.append(" and his.disciplina = h.disciplina");
		sqlStr.append(" and his.historicocursandoporcorrespondenciaapostransferencia");
		sqlStr.append(" and his.transferenciamatrizcurricularmatricula = h.transferenciamatrizcurricularmatricula");
		sqlStr.append(" and his.matrizcurricular != m.gradecurricularatual limit 1");
		sqlStr.append(" )))) or (m.gradecurricularatual != h.matrizcurricular");
		sqlStr.append(" and h.historicocursandoporcorrespondenciaapostransferencia ");
		sqlStr.append(" and h.transferenciamatrizcurricularmatricula IS NOT NULL ");
		sqlStr.append(" and h.disciplina = (select disciplina from historico his");
		sqlStr.append(" where his.matricula = h.matricula ");
		sqlStr.append(" and his.anohistorico = h.anohistorico");
		sqlStr.append(" and his.semestrehistorico = h.semestrehistorico");
		sqlStr.append(" and his.disciplina = h.disciplina");
		sqlStr.append(" and his.transferenciamatrizcurricularmatricula = h.transferenciamatrizcurricularmatricula");
		sqlStr.append(" and (his.historicocursandoporcorrespondenciaapostransferencia is null or ");
		sqlStr.append(" his.historicocursandoporcorrespondenciaapostransferencia = false)");
		sqlStr.append(" and his.matrizcurricular = m.gradecurricularatual limit 1");
		sqlStr.append(" )) ");
		sqlStr.append(" or (h.matrizcurricular = mp.gradecurricular ");
		sqlStr.append(" and m.gradecurricularatual != h.matrizcurricular ");
		sqlStr.append(" and h.historicodisciplinafazpartecomposicao ");
		sqlStr.append(" and h.disciplina not in (");
		sqlStr.append(" select his.disciplina from historico his ");
		sqlStr.append(" where his.matriculaperiodo = h.matriculaperiodo ");
		sqlStr.append(" and his.disciplina = h.disciplina ");
		sqlStr.append(" and m.gradecurricularatual = his.matrizcurricular))	");
		sqlStr.append(") ");
		sqlStr.append(" ) ");
		return sqlStr;
	} 
	
	
	public MatriculaPeriodoTurmaDisciplinaVO consultarMatriculaPeriodoTurmaDisciplinaVOPorTipoSalaAulaBlackboardEnumPorNomeAlunoPorCursoPorTurmaPorDisciplinaPorAnoPorSemestre(TipoSalaAulaBlackboardEnum tipoSalaAulaBlackboardEnum,String nomeAluno, Integer curso, Integer turma, Integer disciplina,  String ano, String semestre, UsuarioVO usuarioVO) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuarioVO);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT matricula.matricula ");
		if(tipoSalaAulaBlackboardEnum.isDisciplina() || tipoSalaAulaBlackboardEnum.isProjetoIntegradorAmbientacao()  || tipoSalaAulaBlackboardEnum.isProjetoIntegrador() || tipoSalaAulaBlackboardEnum.isProjetoIntegradorGrupo() ){
			sqlStr.append(" , matriculaperiodoturmadisciplina.codigo as matriculaperiodoturmadisciplina_codigo  ");	
		}
		sqlStr.append(" from matricula ");
		sqlStr.append(" INNER JOIN matriculaPeriodo on matriculaPeriodo.matricula = matricula.matricula ");
		sqlStr.append(" INNER JOIN pessoa ON pessoa.codigo = matricula.aluno ");
		if(tipoSalaAulaBlackboardEnum.isDisciplina() || tipoSalaAulaBlackboardEnum.isProjetoIntegradorAmbientacao() || tipoSalaAulaBlackboardEnum.isProjetoIntegrador() || tipoSalaAulaBlackboardEnum.isProjetoIntegradorGrupo()){
			sqlStr.append(" INNER JOIN matriculaperiodoturmadisciplina ON matriculaperiodoturmadisciplina.matricula = matricula.matricula ");
		}else if(tipoSalaAulaBlackboardEnum.isTccAmbientacao() || tipoSalaAulaBlackboardEnum.isTcc() || tipoSalaAulaBlackboardEnum.isTccGrupo()) {
			sqlStr.append(" INNER JOIN gradecurricular ON gradecurricular.codigo = matricula.gradecurricularatual ");
		}
		sqlStr.append(" WHERE 1=1 ");
		sqlStr.append(" AND upper(sem_acentos(pessoa.nome)) like upper(sem_acentos(?)) ");
		sqlStr.append(" AND matricula.situacao = 'AT' ");
		sqlStr.append(" AND matriculaperiodo.situacaomatriculaperiodo in ('AT', 'PR', 'FI') ");
		if (Uteis.isAtributoPreenchido(curso)) {
			sqlStr.append(" AND matricula.curso = ").append(curso);
		}
		if (tipoSalaAulaBlackboardEnum.isDisciplina() || tipoSalaAulaBlackboardEnum.isProjetoIntegradorAmbientacao() || tipoSalaAulaBlackboardEnum.isProjetoIntegrador() || tipoSalaAulaBlackboardEnum.isProjetoIntegradorGrupo()){
			if(Uteis.isAtributoPreenchido(turma)) {
				sqlStr.append(" AND matriculaperiodoturmadisciplina.turma = ").append(turma);
			}
			if (Uteis.isAtributoPreenchido(disciplina)) {
				sqlStr.append(" AND matriculaperiodoturmadisciplina.disciplina = ").append(disciplina);
			}
			if (Uteis.isAtributoPreenchido(ano)) {
				sqlStr.append(" AND matriculaperiodoturmadisciplina.ano = '").append(ano).append("'");
			}
			if (Uteis.isAtributoPreenchido(semestre)) {
				sqlStr.append(" AND matriculaperiodoturmadisciplina.semestre = '").append(semestre).append("'");
			}
		} else if (tipoSalaAulaBlackboardEnum.isEstagio()){
			if (Uteis.isAtributoPreenchido(ano)) {
				sqlStr.append(" AND matriculaPeriodo.ano = '").append(ano).append("'");
			}
			if (Uteis.isAtributoPreenchido(semestre)) {
				sqlStr.append(" AND matriculaPeriodo.semestre = '").append(semestre).append("'");
			}
		} else if (tipoSalaAulaBlackboardEnum.isTccAmbientacao() || tipoSalaAulaBlackboardEnum.isTcc() || tipoSalaAulaBlackboardEnum.isTccGrupo()){
			if (Uteis.isAtributoPreenchido(disciplina)) {
				sqlStr.append(" AND gradecurricular.disciplinapadraotcc = ").append(disciplina);
			}
			if (Uteis.isAtributoPreenchido(ano)) {
				sqlStr.append(" AND matriculaPeriodo.ano = '").append(ano).append("'");
			}
			if (Uteis.isAtributoPreenchido(semestre)) {
				sqlStr.append(" AND matriculaPeriodo.semestre = '").append(semestre).append("'");
			}
		}		
		if (tipoSalaAulaBlackboardEnum.isDisciplina() || tipoSalaAulaBlackboardEnum.isProjetoIntegradorAmbientacao() || tipoSalaAulaBlackboardEnum.isProjetoIntegrador() || tipoSalaAulaBlackboardEnum.isProjetoIntegradorGrupo()){
			sqlStr.append(" ORDER BY matriculaperiodoturmadisciplina.codigo desc limit 1 ");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { nomeAluno.toLowerCase() + "%" });
		MatriculaPeriodoTurmaDisciplinaVO mptd = new MatriculaPeriodoTurmaDisciplinaVO();
		if (tabelaResultado.next()) {
			mptd.setMatricula(tabelaResultado.getString("matricula"));
			if (tipoSalaAulaBlackboardEnum.isDisciplina() || tipoSalaAulaBlackboardEnum.isProjetoIntegradorAmbientacao() || tipoSalaAulaBlackboardEnum.isProjetoIntegrador() || tipoSalaAulaBlackboardEnum.isProjetoIntegradorGrupo()){
				mptd.setCodigo(tabelaResultado.getInt("matriculaperiodoturmadisciplina_codigo"));
			}else {
				mptd.setCodigo(0);
			}
		}
		return mptd;
	}

	@Override
	public MatriculaPeriodoTurmaDisciplinaVO consultaRapidaPorMatriculaTurmaDisciplinaAnoSemestre(String matricula, TurmaVO turma, Integer disciplina, String ano, String semestre, Integer gradeCurricular,  boolean controlarAcesso, boolean consultarDisciplinaEquivalente, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sql = getSQLPadraoConsultaCompleta();
		sql.append(" inner join matricula on matricula.matricula = matriculaperiodoturmadisciplina.matricula ");
		sql.append(" inner join historico on historico.matricula = matricula.matricula and historico.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo ");
		sql.append(" inner join matriculaPeriodo on matriculaPeriodo.codigo = matriculaperiodoturmadisciplina.matriculaPeriodo ");
		sql.append(" WHERE matricula.matricula = '").append(matricula).append("' ");
		if (Uteis.isAtributoPreenchido(turma)) {
			if (turma.getSubturma()) {
				if (turma.getTipoSubTurma().equals(TipoSubTurmaEnum.TEORICA)) {
					sql.append(" AND turmateorica.codigo = ").append(turma.getCodigo());
				} else if (turma.getTipoSubTurma().equals(TipoSubTurmaEnum.PRATICA)) {
					sql.append(" AND turmapratica.codigo = ").append(turma.getCodigo());
				} else if (turma.getTipoSubTurma().equals(TipoSubTurmaEnum.GERAL)) {
					sql.append(" AND turma.codigo = ").append(turma.getCodigo());
				}
			} else if (turma.getTurmaAgrupada() && !turma.getSubturma()) {
				sql.append(" AND ( (MatriculaPeriodoTurmaDisciplina.turmaTeorica is null  and MatriculaPeriodoTurmaDisciplina.turmaPratica is null and turma.codigo in (select turma from turmaAgrupada where turmaOrigem =  ").append(turma.getCodigo()).append(")) ");
				sql.append("or (MatriculaPeriodoTurmaDisciplina.turmaPratica is not null and MatriculaPeriodoTurmaDisciplina.turmaPratica in (select TurmaAgrupada.turma from TurmaAgrupada inner join turma as turmaOrigem on turmaOrigem.codigo = TurmaAgrupada.turmaOrigem inner join turma on turma.codigo = turmaagrupada.turma where turmaOrigem.codigo = ").append(turma.getCodigo()).append(" and turmaOrigem.subturma = false and turma.tiposubturma = 'PRATICA'))");
				sql.append("or (MatriculaPeriodoTurmaDisciplina.turmaTeorica is not null and MatriculaPeriodoTurmaDisciplina.turmaTeorica in (select TurmaAgrupada.turma from TurmaAgrupada inner join turma as turmaOrigem on turmaOrigem.codigo = TurmaAgrupada.turmaOrigem inner join turma on turma.codigo = turmaagrupada.turma where turmaOrigem.codigo = ").append(turma.getCodigo()).append(" and turmaOrigem.subturma = false and turma.tiposubturma = 'TEORICA'))");				
				sql.append(") ");
			} else {
				sql.append(" AND Turma.codigo = ").append(turma.getCodigo());
			}
		}
		if (!disciplina.equals(0) && consultarDisciplinaEquivalente) {
			if (turma.getTurmaAgrupada()) {
				sql.append(" AND disciplina.codigo in (select disciplinaequivalente.equivalente from disciplinaequivalente where disciplinaequivalente.disciplina = ").append(disciplina);
				sql.append(" union select ").append(disciplina).append(")");
			} else {
				sql.append(" AND disciplina.codigo = ").append(disciplina);
			}
		} else if (!disciplina.equals(0) && !consultarDisciplinaEquivalente) {
			sql.append(" AND disciplina.codigo = ").append(disciplina);
		}
		if (!ano.equals("")) {
			sql.append(" AND matriculaperiodoturmadisciplina.ano = '").append(ano).append("' ");
		}
		if (!semestre.equals("")) {
			sql.append(" AND matriculaperiodoturmadisciplina.semestre = '").append(semestre).append("' ");
		}
		if(Uteis.isAtributoPreenchido(gradeCurricular)) {
			sql.append(getSqlFiltroBaseGradeCurricularEspecifico(" and ", gradeCurricular));
		}else {
			sql.append(getSqlFiltroBaseGradeCurricularAtual(" and "));
		}
		sql.append(" ORDER BY matriculaPeriodoTurmaDisciplina.matricula, matriculaPeriodoTurmaDisciplina.ano||'/'||matriculaPeriodoTurmaDisciplina.semestre desc, matriculaPeriodoTurmaDisciplina.codigo desc limit 1 ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		MatriculaPeriodoTurmaDisciplinaVO obj = new MatriculaPeriodoTurmaDisciplinaVO();
		if (tabelaResultado.next()) {
			montarDadosCompleto(obj, tabelaResultado);
		}
		return obj;
	}

	public MatriculaPeriodoTurmaDisciplinaVO consultaRapidaPorTransferenciaMatrizCurricularMatricula(Integer codigoTransferenciaMatrizCurricularMatricula, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sql = getSQLPadraoConsultaCompleta();
		sql.append(" WHERE (transferenciaMatrizCurricularMatricula = ").append(codigoTransferenciaMatrizCurricularMatricula).append(") ");
		sql.append(" ORDER BY matriculaPeriodoTurmaDisciplina.matricula ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		MatriculaPeriodoTurmaDisciplinaVO obj = new MatriculaPeriodoTurmaDisciplinaVO();
		if (tabelaResultado.next()) {
			montarDadosCompleto(obj, tabelaResultado);
		}
		return obj;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alteraModalidadeMatriculaPeriodoTurmaDisciplina(final Integer turma, final Integer disciplina, final String modalidade) throws Exception {
		final String sql = "UPDATE MatriculaPeriodoTurmaDisciplina set modalidadeDisciplina=?, dataultimaalteracao=now() WHERE (turma = ? AND disciplina = ?)";
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				sqlAlterar.setString(1, modalidade);
				sqlAlterar.setInt(2, turma);
				sqlAlterar.setInt(3, disciplina);
				return sqlAlterar;
			}
		});
	}

	/**
	 * 
	 * @author Victor Hugo 17/11/2014
	 * 
	 * 
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirProfessorMatriculaPeriodoTurmaDisciplina(final MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO) throws Exception {
		final String sql = "UPDATE MatriculaPeriodoTurmaDisciplina set professor=?, dataultimaalteracao=now() WHERE ((codigo = ?))";
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				if (matriculaPeriodoTurmaDisciplinaVO.getProfessor().getCodigo() != 0) {
					sqlAlterar.setInt(1, matriculaPeriodoTurmaDisciplinaVO.getProfessor().getCodigo());
				} else {
					sqlAlterar.setNull(1, 0);
				}
				sqlAlterar.setInt(2, matriculaPeriodoTurmaDisciplinaVO.getCodigo());
				return sqlAlterar;
			}
		});
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public Integer consultarQtdAlunosAtivoPorProgramacaoTutoriaOnline(ProgramacaoTutoriaOnlineVO obj, ProgramacaoTutoriaOnlineProfessorVO objProfessor){
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select count(matriculaperiodoturmadisciplina.codigo) as qtdealunoativos   ");
		sqlStr.append(" from matriculaperiodoturmadisciplina  ");
		sqlStr.append(" inner join turma on turma.codigo = matriculaperiodoturmadisciplina.turma ");
		sqlStr.append(" inner join matriculaperiodo on matriculaperiodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo and matriculaperiodo.situacaomatriculaperiodo = 'AT' ");
		sqlStr.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula and matricula.situacao = 'AT' ");
		sqlStr.append(" where matriculaperiodoturmadisciplina.professor = ").append(objProfessor.getProfessor().getCodigo());
		if(Uteis.isAtributoPreenchido(obj.getTurmaVO().getCodigo())){
			sqlStr.append(" and matriculaperiodoturmadisciplina.turma = ").append(obj.getTurmaVO().getCodigo());;	
		}
		if(Uteis.isAtributoPreenchido(obj.getDisciplinaVO().getCodigo())){
			sqlStr.append(" and matriculaperiodoturmadisciplina.disciplina = ").append(obj.getDisciplinaVO().getCodigo());;	
		}
		if(Uteis.isAtributoPreenchido(obj.getCursoVO().getCodigo())){
			sqlStr.append(" and ((turma.turmaagrupada = false and turma.curso = ").append(obj.getCursoVO().getCodigo()).append(" ) ");
			sqlStr.append("     or  ");
			sqlStr.append("     (turma.turmaagrupada and ").append(obj.getCursoVO().getCodigo()).append(" in (select t.curso from turmaagrupada inner join turma as t on t.codigo = turmaagrupada.turma where turmaagrupada.turmaorigem = turma.codigo ))) ");
		}		
		if(Uteis.isAtributoPreenchido(obj.getUnidadeEnsinoVO().getCodigo())){
			sqlStr.append(" and matricula.unidadeensino = ").append(obj.getUnidadeEnsinoVO().getCodigo());	
		}		
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (rs.next()) {
			return rs.getInt("qtdealunoativos");
		}
		return 0;
	}
	
	@Override
	public List<MatriculaPeriodoTurmaDisciplinaVO> consultarMatriculaPeriodoTurmaDisciplinaPorProgramacaoTutoriaOnline(ProgramacaoTutoriaOnlineVO programacaoTutoriaOnlineVO, ProgramacaoTutoriaOnlineProfessorVO programacaoTutoriaOnlineProfessorVO, Integer bimestre, UsuarioVO usuarioVO, Boolean considerarAlunosInativos, Boolean considerarAlunosSemTutor) throws Exception {
		StringBuilder sqlStr = executarInicializacaoSqlConsultaMatriculaPeriodoTurmaDisciplinaPorProgramacaoTutoriaOnline(programacaoTutoriaOnlineVO, programacaoTutoriaOnlineProfessorVO, bimestre, considerarAlunosInativos, considerarAlunosSemTutor);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<MatriculaPeriodoTurmaDisciplinaVO> lista = new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>(0);
		while (rs.next()) {
			MatriculaPeriodoTurmaDisciplinaVO  obj = new MatriculaPeriodoTurmaDisciplinaVO();
			obj.getMatriculaObjetoVO().getAluno().setCodigo(rs.getInt("codigopessoa"));
			obj.getMatriculaObjetoVO().getAluno().setNome(rs.getString("nomepessoa"));
			obj.getMatriculaObjetoVO().setMatricula(rs.getString("matricula"));
			obj.getMatriculaObjetoVO().getCurso().setCodigo(rs.getInt("codigocurso"));
			obj.getMatriculaObjetoVO().getCurso().setNome(rs.getString("nomecurso"));
			obj.getMatriculaObjetoVO().getCurso().setAbreviatura(rs.getString("abreviaturacurso"));
			obj.getMatriculaObjetoVO().getUnidadeEnsino().setCodigo(rs.getInt("unidadeEnsino.codigo"));
			obj.getMatriculaObjetoVO().getUnidadeEnsino().setNome(rs.getString("unidadeEnsino.nome"));
			obj.getTurma().setCodigo(rs.getInt("turma.codigo"));
			obj.getTurma().setIdentificadorTurma(rs.getString("turma.identificadorTurma"));
			
			obj.setCodigo(rs.getInt("matriculaperiodoturmadisciplina.codigo"));
			obj.setAno(rs.getString("ano"));
			obj.setSemestre(rs.getString("semestre"));
			obj.getGradeDisciplinaVO().setBimestre(rs.getInt("bimestre"));
			obj.getMatriculaPeriodoObjetoVO().setSituacaoMatriculaPeriodo(rs.getString("matriculaperiodo.situacaomatriculaperiodo"));
			obj.getSalaAulaBlackboardVO().setCodigo(rs.getInt("salaaulablackboard.codigo"));
			lista.add(obj);
		}
		return lista;
	}
	
	@Override
	public List<MatriculaPeriodoTurmaDisciplinaVO> consultarMatriculaPeriodoTurmaDisciplinaGeracaoPorCargaAutomaticas(ProgramacaoTutoriaOnlineVO programacaoTutoriaOnlineVO, ProgramacaoTutoriaOnlineProfessorVO programacaoTutoriaOnlineProfessorVO, Integer bimestre, boolean isClassroomAutomatico, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder(" ");
		sqlStr.append(" select ");
		sqlStr.append(" t.\"codigocurso\", ");
		sqlStr.append(" t.\"nomecurso\", ");
		sqlStr.append(" t.\"abreviaturacurso\", ");
		sqlStr.append(" t.\"turma.codigo\", ");
		sqlStr.append(" t.\"turma.identificadorTurma\", ");
		sqlStr.append(" t.\"disciplina.codigo\", ");
		sqlStr.append(" t.\"disciplina.nome\", ");		
		sqlStr.append(" t.\"unidadeEnsino.codigo\", ");
		sqlStr.append(" t.\"unidadeEnsino.nome\", ");
		sqlStr.append(" t.\"unidadeEnsino.razaoSocial\", ");
		sqlStr.append(" t.ano, ");
		sqlStr.append(" t.semestre, ");
		sqlStr.append(" t.bimestre ");
		sqlStr.append(" from  ( ");
		sqlStr.append(getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().executarInicializacaoSqlConsultaMatriculaPeriodoTurmaDisciplinaPorProgramacaoTutoriaOnline(programacaoTutoriaOnlineVO, programacaoTutoriaOnlineProfessorVO, bimestre, false, false));
		sqlStr.append(" ) as t ");
		sqlStr.append(" inner join turmadisciplina on  turmadisciplina.turma =  t.\"turma.codigo\"   ");
		sqlStr.append(" and  turmadisciplina.disciplina =  t.\"disciplina.codigo\"  ");
		sqlStr.append(" and  turmadisciplina.definicoestutoriaonline  = 'DINAMICA'  ");
		sqlStr.append(" where 1=1    ");
		if(isClassroomAutomatico) {
			sqlStr.append(" and not exists(   ");
			sqlStr.append(" select classroomgoogle.codigo from classroomgoogle where classroomgoogle.turma =  t.\"turma.codigo\"  ");
			sqlStr.append(" and  classroomgoogle.disciplina =  t.\"disciplina.codigo\"  ");
			sqlStr.append(" and  classroomgoogle.ano =  t.ano ");
			sqlStr.append(" and  classroomgoogle.semestre =  t.semestre ");
			sqlStr.append(" and  classroomgoogle.professoread =  ").append(programacaoTutoriaOnlineProfessorVO.getProfessor().getCodigo());
			sqlStr.append(" )  ");
		}else {
			sqlStr.append(" and not exists(   ");
			sqlStr.append(" select salaaulablackboard.codigo from salaaulablackboard where ");
			sqlStr.append(" salaaulablackboard.disciplina =  t.\"disciplina.codigo\"  ");
			if(programacaoTutoriaOnlineVO.getTipoNivelProgramacaoTutoria().isTurma()) {
				sqlStr.append(" and salaaulablackboard.turma =  t.\"turma.codigo\"  ");
			}
			if(programacaoTutoriaOnlineVO.getTipoNivelProgramacaoTutoria().isCurso()) {
				sqlStr.append(" and salaaulablackboard.curso =  t.\"codigocurso\" ");
			}			
			sqlStr.append(" and  salaaulablackboard.ano =  t.ano ");
			sqlStr.append(" and  salaaulablackboard.semestre =  t.semestre ");			
			sqlStr.append(" )  ");
		}
		
		sqlStr.append("  group by   ");
		sqlStr.append(" t.\"codigocurso\", ");
		sqlStr.append(" t.\"nomecurso\", ");
		sqlStr.append(" t.\"abreviaturacurso\", ");
		sqlStr.append(" t.\"turma.codigo\", ");
		sqlStr.append(" t.\"turma.identificadorTurma\", ");
		sqlStr.append(" t.\"disciplina.codigo\", ");
		sqlStr.append(" t.\"disciplina.nome\", ");
		sqlStr.append(" t.\"unidadeEnsino.codigo\", ");
		sqlStr.append(" t.\"unidadeEnsino.nome\", ");
		sqlStr.append(" t.\"unidadeEnsino.razaoSocial\", ");
		sqlStr.append(" t.\"ano\", ");
		sqlStr.append(" t.\"semestre\", ");
		sqlStr.append(" t.\"bimestre\" ");
		
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosMatriculaPeriodoTurmaDisciplinaPorCargaAutomaticas(rs);
	}	
	

	@Override	
	public StringBuilder executarInicializacaoSqlConsultaMatriculaPeriodoTurmaDisciplinaPorProgramacaoTutoriaOnline(ProgramacaoTutoriaOnlineVO programacaoTutoriaOnlineVO, ProgramacaoTutoriaOnlineProfessorVO programacaoTutoriaOnlineProfessorVO, Integer bimestre, Boolean considerarAlunosInativos, Boolean considerarAlunosSemTutor) {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select pessoa.codigo as codigopessoa, pessoa.nome as nomepessoa, matricula.matricula, curso.codigo as codigocurso, curso.nome as nomecurso, curso.abreviatura as abreviaturacurso, ");
		sqlStr.append(" matriculaperiodoturmadisciplina.codigo as \"matriculaperiodoturmadisciplina.codigo\", matriculaperiodoturmadisciplina.ano, matriculaperiodoturmadisciplina.semestre, ");
		sqlStr.append(" turma.codigo as \"turma.codigo\", turma.identificadorTurma as \"turma.identificadorTurma\", ");
		sqlStr.append(" disciplina.codigo as \"disciplina.codigo\", disciplina.nome as \"disciplina.nome\",  ");
		sqlStr.append(" unidadeEnsino.codigo as \"unidadeEnsino.codigo\", unidadeEnsino.nome as \"unidadeEnsino.nome\", unidadeEnsino.razaoSocial as \"unidadeEnsino.razaoSocial\", matriculaperiodo.situacaomatriculaperiodo as \"matriculaperiodo.situacaomatriculaperiodo\", ");
		sqlStr.append(" pessoaemailinstitucional.codigo as \"codigoEmail\", pessoaemailinstitucional.email as \"email\", salaaulablackboard.codigo as \"salaaulablackboard.codigo\", gradeDisciplina.bimestre ");
		sqlStr.append(" from matricula ");
		sqlStr.append(" inner join unidadeEnsino on unidadeEnsino.codigo = matricula.unidadeEnsino ");
		sqlStr.append(" inner join curso on curso.codigo = matricula.curso");
		sqlStr.append(" inner join pessoa on pessoa.codigo = matricula.aluno");
		sqlStr.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula  ");
		sqlStr.append(" inner join matriculaperiodoturmadisciplina on matriculaperiodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo ");
		sqlStr.append(" left join gradeDisciplina on gradeDisciplina.codigo = matriculaperiodoturmadisciplina.gradeDisciplina ");
		sqlStr.append(" inner join turma on turma.codigo = matriculaperiodoturmadisciplina.turma ");
		sqlStr.append(" inner join disciplina on disciplina.codigo = matriculaperiodoturmadisciplina.disciplina ");
		sqlStr.append(" left join pessoaemailinstitucional on pessoaemailinstitucional.pessoa = pessoa.codigo and pessoaemailinstitucional.statusativoinativoenum = 'ATIVO' ");
		sqlStr.append(" and pessoaemailinstitucional.codigo = (select pei.codigo from pessoaemailinstitucional pei where pei.pessoa = pessoa.codigo and pei.statusativoinativoenum = 'ATIVO' order by pei.codigo desc limit 1 ) ");
		sqlStr.append(" left join salaaulablackboard on salaaulablackboard.codigo =  (select salaaulablackboardpessoa.salaaulablackboard from salaaulablackboardpessoa where salaaulablackboardpessoa.matriculaperiodoturmadisciplina =  matriculaperiodoturmadisciplina.codigo  limit 1 ) ");	
		sqlStr.append(" where 1 = 1 ");	
		if (considerarAlunosInativos) {
			sqlStr.append(" and matriculaperiodo.situacaomatriculaperiodo <> 'AT' ");
		} else {
			sqlStr.append(" and matriculaperiodo.situacaomatriculaperiodo = 'AT' ");
		}
		if (!considerarAlunosInativos) {
			sqlStr.append(" and matricula.situacao = 'AT' ");
			}
		if(programacaoTutoriaOnlineProfessorVO != null) {
			if (considerarAlunosSemTutor) {
				sqlStr.append(" and salaaulablackboard.codigo is null ");	
			} else if (!considerarAlunosInativos) { 
				sqlStr.append(" and matriculaperiodoturmadisciplina.professor = ").append(programacaoTutoriaOnlineProfessorVO.getProfessor().getCodigo());
			} else {
				sqlStr.append(" and matriculaperiodoturmadisciplina.programacaotutoriaonlineprofessor = ").append(programacaoTutoriaOnlineProfessorVO.getCodigo());
			}
		}
		if(Uteis.isAtributoPreenchido(programacaoTutoriaOnlineVO.getAno())){
			sqlStr.append(" and ((curso.periodicidade in ('AN', 'SE') and matriculaperiodoturmadisciplina.ano = '").append(programacaoTutoriaOnlineVO.getAno()).append("' and matriculaperiodo.ano = '").append(programacaoTutoriaOnlineVO.getAno()).append("') ");
			sqlStr.append(" or (curso.periodicidade = 'IN')) ");
		}else if(!Uteis.isAtributoPreenchido(programacaoTutoriaOnlineVO.getAno())){
			sqlStr.append(" and ((curso.periodicidade in ('AN', 'SE') and matriculaperiodoturmadisciplina.ano = '").append(Uteis.getAnoDataAtual()).append("' and matriculaperiodo.ano = '").append(Uteis.getAnoDataAtual()).append("') ");
			sqlStr.append(" or (curso.periodicidade = 'IN')) ");
		}		
		if(Uteis.isAtributoPreenchido(programacaoTutoriaOnlineVO.getSemestre())){
			sqlStr.append(" and ((curso.periodicidade = 'SE' and matriculaperiodoturmadisciplina.semestre = '").append(programacaoTutoriaOnlineVO.getSemestre()).append("' and matriculaperiodo.semestre = '").append(programacaoTutoriaOnlineVO.getSemestre()).append("') ");
			sqlStr.append(" or (curso.periodicidade in ('IN', 'AN'))) ");
		}else if(!Uteis.isAtributoPreenchido(programacaoTutoriaOnlineVO.getSemestre())){
			sqlStr.append(" and ((curso.periodicidade = 'SE' and matriculaperiodoturmadisciplina.semestre = '").append(Uteis.getSemestreAtual()).append("' and matriculaperiodo.semestre = '").append(Uteis.getSemestreAtual()).append("') ");
			sqlStr.append(" or (curso.periodicidade in ('IN', 'AN'))) ");
		}	
		if(Uteis.isAtributoPreenchido(bimestre)) {
			sqlStr.append(" and gradeDisciplina.bimestre = ").append(bimestre);
		}
		if(Uteis.isAtributoPreenchido(programacaoTutoriaOnlineVO.getTurmaVO().getCodigo())){
			sqlStr.append(" and matriculaperiodoturmadisciplina.turma = ").append(programacaoTutoriaOnlineVO.getTurmaVO().getCodigo());	
		}
		if(Uteis.isAtributoPreenchido(programacaoTutoriaOnlineVO.getDisciplinaVO().getCodigo())){
			sqlStr.append(" and matriculaperiodoturmadisciplina.disciplina = ").append(programacaoTutoriaOnlineVO.getDisciplinaVO().getCodigo());;	
		}
		if(Uteis.isAtributoPreenchido(programacaoTutoriaOnlineVO.getCursoVO().getCodigo())){
			sqlStr.append(" and ((turma.turmaagrupada = false and turma.curso = ").append(programacaoTutoriaOnlineVO.getCursoVO().getCodigo()).append(" ) ");
			sqlStr.append("     or  ");
			sqlStr.append("     (turma.turmaagrupada and ").append(programacaoTutoriaOnlineVO.getCursoVO().getCodigo()).append(" in (select t.curso from turmaagrupada inner join turma as t on t.codigo = turmaagrupada.turma where turmaagrupada.turmaorigem = turma.codigo ))) ");
		}		
		if(!Uteis.isAtributoPreenchido(programacaoTutoriaOnlineVO.getTurmaVO().getCodigo()) && Uteis.isAtributoPreenchido(programacaoTutoriaOnlineVO.getUnidadeEnsinoVO().getCodigo())){
			sqlStr.append(" and unidadeEnsino.codigo = ").append(programacaoTutoriaOnlineVO.getUnidadeEnsinoVO().getCodigo());	
		}
		sqlStr.append(" order by  pessoa.nome ");
		return sqlStr;
	}
	
	private List<MatriculaPeriodoTurmaDisciplinaVO> montarDadosMatriculaPeriodoTurmaDisciplinaPorCargaAutomaticas(SqlRowSet rs) {
		List<MatriculaPeriodoTurmaDisciplinaVO> lista = new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>(0);
		while (rs.next()) {
			MatriculaPeriodoTurmaDisciplinaVO  obj = new MatriculaPeriodoTurmaDisciplinaVO();					
			obj.getTurma().setCodigo(rs.getInt("turma.codigo"));
			obj.getTurma().setIdentificadorTurma(rs.getString("turma.identificadorTurma"));
			obj.getTurma().getUnidadeEnsino().setCodigo(rs.getInt("unidadeEnsino.codigo"));
			obj.getTurma().getUnidadeEnsino().setNome(rs.getString("unidadeEnsino.nome"));			
			obj.getTurma().getUnidadeEnsino().setRazaoSocial(rs.getString("unidadeEnsino.razaoSocial"));
			obj.getTurma().getCurso().setCodigo(rs.getInt("codigocurso"));
			obj.getTurma().getCurso().setNome(rs.getString("nomecurso"));
			obj.getTurma().getCurso().setAbreviatura(rs.getString("abreviaturacurso"));
			obj.getDisciplina().setCodigo(rs.getInt("disciplina.codigo"));
			obj.getDisciplina().setNome(rs.getString("disciplina.nome"));
//			obj.getDisciplina().setModeloGeracaoSalaBlackboard(ModeloGeracaoSalaBlackboardEnum.valueOf(rs.getString("disciplina.modeloGeracaoSalaBlackboard")));
			obj.setAno(rs.getString("ano"));
			obj.setSemestre(rs.getString("semestre"));			
			lista.add(obj);
		}
		return lista;
	}

	@Override
	public void consultarCargaHorariaENrCreditosDisciplinaPorCodigoMatriculaPeriodoTurmaDisciplina(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, Map<String, Integer> cargaHorariaENrCreditos, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder();

		sqlStr.append(" select case when gradedisciplina.codigo is not null then gradedisciplina.cargahoraria else gradecurriculargrupooptativadisciplina.cargahoraria end as cargahoraria,");
		sqlStr.append(" case when gradedisciplina.codigo is not null then gradedisciplina.nrcreditos else gradecurriculargrupooptativadisciplina.nrcreditos end as nrcreditos  from matriculaperiodoturmadisciplina");
		sqlStr.append(" left join gradedisciplina on gradedisciplina.codigo = matriculaperiodoturmadisciplina.gradedisciplina");
		sqlStr.append(" left join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.codigo = matriculaperiodoturmadisciplina.gradecurriculargrupooptativadisciplina");
		sqlStr.append(" where matriculaperiodoturmadisciplina.codigo = ").append(matriculaPeriodoTurmaDisciplinaVO.getCodigo());

		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());

		if (rs.next()) {
			cargaHorariaENrCreditos.put("cargaHoraria", rs.getInt("cargahoraria"));
			cargaHorariaENrCreditos.put("nrcreditos", rs.getInt("nrcreditos"));
		}
	}

	@Override
	public MatriculaPeriodoTurmaDisciplinaVO consultarPorTurmaMatriculaOrdemEstudoOnlineMatriculaPeriodoTurmaDisciplinaAnterior(String matricula, Integer turma, Integer ordemEstudoOnline, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append(" select matriculaperiodoturmadisciplina.*, calendarioatividadematricula.datafim from matriculaperiodoturmadisciplina");
		sql.append(" inner join turma on turma.codigo = matriculaperiodoturmadisciplina.turma");
		sql.append(" inner join matricula on matricula.matricula = matriculaperiodoturmadisciplina.matricula");
		sql.append(" inner join disciplina on disciplina.codigo = matriculaperiodoturmadisciplina.disciplina");
		sql.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo and turmadisciplina.disciplina = disciplina.codigo");
		sql.append(" inner join calendarioatividadematricula on calendarioatividadematricula.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo ");
		sql.append(" and calendarioatividadematricula.tipocalendarioatividade = 'ACESSO_CONTEUDO_ESTUDO' ");
		sql.append(" where turmadisciplina.turma = ").append(turma).append(" and matricula.matricula = '").append(matricula + "'").append(" and turmadisciplina.ordemestudoonline = ").append(ordemEstudoOnline);
		sql.append(" order by calendarioatividadematricula.datafim desc limit 1");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (tabelaResultado.next()) {
			return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS, usuario);
		}
		return new MatriculaPeriodoTurmaDisciplinaVO();
	}

	@Override
	public MatriculaPeriodoTurmaDisciplinaVO consultarPorTurmaMatriculaOrdemEstudoOnlineMatriculaPeriodoTurmaDisciplinaPosterior(String matricula, Integer turma, String ano, String semestre, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" select matriculaperiodoturmadisciplina.* from matriculaperiodoturmadisciplina");
		sql.append(" inner join matriculaperiodo on matriculaperiodoturmadisciplina.matriculaperiodo = matriculaperiodo.codigo ");
		sql.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula");
		sql.append(" inner join turma on turma.codigo = matriculaperiodoturmadisciplina.turma");
		sql.append(" inner join disciplina on disciplina.codigo = matriculaperiodoturmadisciplina.disciplina");
		sql.append(" inner join historico on historico.matricula = matricula.matricula and historico.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo ");
		sql.append(" inner join configuracaoacademico on historico.configuracaoacademico = configuracaoacademico.codigo ");
		sql.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo and turmadisciplina.disciplina = disciplina.codigo");
		sql.append(" where turma.codigo = ").append(turma).append(" and matricula.matricula = '").append(matricula + "'");
		sql.append(" and not exists (select calendarioatividadematricula.codigo from calendarioatividadematricula where calendarioatividadematricula.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo ");
		sql.append(" and calendarioatividadematricula.tipocalendarioatividade = 'ACESSO_CONTEUDO_ESTUDO') ");
		sql.append(" and ((turma.semestral and matriculaperiodoturmadisciplina.semestre = '").append(semestre).append("' and matriculaperiodoturmadisciplina.ano = '").append(ano).append("') ");
		sql.append(" or (turma.anual and matriculaperiodoturmadisciplina.ano = '").append(ano).append("') ");
		sql.append(" or (turma.anual = false and turma.semestral = false)) ");
		sql.append(getSqlFiltroBaseGradeCurricularAtual(" and "));
		sql.append(" and ((matriculaperiodoturmadisciplina.modalidadedisciplina = '").append(ModalidadeDisciplinaEnum.PRESENCIAL).append("' and configuracaoacademico.utilizarapoioeadparadisciplinasmodalidadepresencial)");
		sql.append(" or matriculaperiodoturmadisciplina.modalidadedisciplina = '").append(ModalidadeDisciplinaEnum.ON_LINE).append("') ");
		sql.append(" order by turmadisciplina.ordemestudoonline asc limit 1");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (tabelaResultado.next()) {
			return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS, usuario);
		}
		return new MatriculaPeriodoTurmaDisciplinaVO();
	}
	
	/**
	 * 
	 * @author Victor Hugo 03/12/2014
	 * 
	 *         Metodo criado para incluir o codigo do conteudo na
	 *         matriculaPeriodoTurmaDisciplinaVO no momento em que o aluno
	 *         acesso o conteúdo para estudo.
	 * 
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirConteudoMatriculaPeriodoTurmaDisciplina(final MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO) throws Exception {
		final String sql = "UPDATE MatriculaPeriodoTurmaDisciplina set conteudo = ?, dataultimaalteracao=now() WHERE ((codigo = ?))";
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				if (matriculaPeriodoTurmaDisciplinaVO.getConteudo().getCodigo() != 0) {
					sqlAlterar.setInt(1, matriculaPeriodoTurmaDisciplinaVO.getConteudo().getCodigo());
				} else {
					sqlAlterar.setNull(1, 0);
				}
				sqlAlterar.setInt(2, matriculaPeriodoTurmaDisciplinaVO.getCodigo());
				return sqlAlterar;
			}
		});
	}

	/**
	 * @author Victor Hugo 09/12/2014
	 * 
	 *         Método responsável por verificar os alunos que estão com o
	 *         contéudo atrasado em relação aos dias que o mesmo tem para
	 *         estudar o mesmo olhando para a configuração feita na tela de
	 *         ConfiguraçãoEAD. Os dados são fornecidos para uma rotina diária
	 *         que verifica o mesmo toda meia noite.
	 */
	@Override
	public List<MatriculaPeriodoTurmaDisciplinaVO> consultarAlunosConteudoAtrasadoEstudosOnline() throws Exception {

		StringBuilder sqlStr = new StringBuilder();

		sqlStr.append(" select t2.situacao, matriculaperiodoturmadisciplina.* from matriculaperiodoturmadisciplina");
		sqlStr.append(" inner join (");
		sqlStr.append(" select distinct matriculaperiodoturmadisciplina,");
		sqlStr.append(" case");
		sqlStr.append(" when notificaraluno then");
		sqlStr.append(" case when floor((notumprazoconclusaoestudos * dias) / 100) = diasestudados then");
		sqlStr.append(" case when (pontosdia * diasestudados) > pontosatingidos then 'NOT1' end");
		sqlStr.append(" else case when floor((notdoisprazoconclusaoestudos * dias) / 100) = diasestudados then");
		sqlStr.append(" case when (pontosdia * diasestudados) > pontosatingidos then 'NOT2' end");
		sqlStr.append(" else case when floor((nottresprazoconclusaoestudos * dias) / 100) = diasestudados then");
		sqlStr.append(" case when (pontosdia * diasestudados) > pontosatingidos then 'NOT3' end");
		sqlStr.append(" end end end");
		sqlStr.append(" end AS situacao");
		sqlStr.append(" from (");
		sqlStr.append(" select cast((sum(qtde) / dias) as numeric(20,2)) as pontosDia, diasEstudados, sum(pontosAtingidos) AS pontosAtingidos, matricula,");
		sqlStr.append(" notumprazoconclusaoestudos, notdoisprazoconclusaoestudos, nottresprazoconclusaoestudos,notificaraluno,");
		sqlStr.append(" matriculaperiodoturmadisciplina, disciplina, turma, matriculaperiodo, qtde, dias");
		sqlStr.append(" from (");
		sqlStr.append(" select distinct CAST(EXTRACT(DAYS FROM (datafim-datainicio)) AS integer) as dias,");
		sqlStr.append(" (select sum(ponto) from conteudounidadepagina where conteudounidadepagina.unidadeconteudo = unidadeconteudo.codigo");
		sqlStr.append(" ) AS qtde,");
		sqlStr.append(" (select sum(ponto) as pontos from ConteudoUnidadePagina");
		sqlStr.append(" where conteudounidadepagina.unidadeconteudo = unidadeconteudo.codigo");
		sqlStr.append(" and codigo in");
		sqlStr.append(" (SELECT distinct conteudoUnidadePagina FROM ConteudoRegistroAcesso");
		sqlStr.append(" WHERE matricula = mptd.matricula");
		sqlStr.append(" and conteudo = mptd.conteudo");
		sqlStr.append(" ) ) AS pontosAtingidos,");
		sqlStr.append(" unidadeconteudo.codigo, datainicio,");
		sqlStr.append(" configuracaoead.notumprazoconclusaoestudos, configuracaoead.notdoisprazoconclusaoestudos, configuracaoead.nottresprazoconclusaoestudos,");
		sqlStr.append(" configuracaoead.notificaraluno,");
		sqlStr.append(" calendarioatividadematricula.descricao, CAST(EXTRACT(DAYS FROM (current_timestamp-datainicio)) AS integer) as diasEstudados,");
		sqlStr.append(" mptd.codigo AS matriculaperiodoturmadisciplina, mptd.disciplina, mptd.turma, mptd.matriculaperiodo, mptd.matricula");
		sqlStr.append(" from calendarioatividadematricula");
		sqlStr.append(" inner join matriculaperiodoturmadisciplina mptd on mptd.codigo = calendarioatividadematricula.matriculaperiodoturmadisciplina");
		sqlStr.append(" inner join conteudo on conteudo.codigo = mptd.conteudo");
		sqlStr.append(" inner join unidadeconteudo on unidadeconteudo.conteudo = conteudo.codigo");
		sqlStr.append(" inner join turma on turma.codigo = mptd.turma");
		sqlStr.append(" inner join configuracaoead on configuracaoead.codigo = turma.configuracaoead where calendarioatividadematricula.tipocalendarioatividade = 'ACESSO_CONTEUDO_ESTUDO') as t");
		sqlStr.append(" group by dias, matricula, diasEstudados, notumprazoconclusaoestudos, notdoisprazoconclusaoestudos, nottresprazoconclusaoestudos,notificaraluno,");
		sqlStr.append(" matriculaperiodoturmadisciplina, disciplina, turma, matriculaperiodo, qtde, dias");
		sqlStr.append(" ) as t1");
		sqlStr.append(" ) as t2 on t2.matriculaperiodoturmadisciplina =  matriculaperiodoturmadisciplina.codigo");
		sqlStr.append(" and t2.situacao is not null");

		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());

		List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs = new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>(0);
		MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO = null;
		while (rs.next()) {
			matriculaPeriodoTurmaDisciplinaVO = montarDados(rs, Uteis.NIVELMONTARDADOS_PROCESSAMENTO, null);
			matriculaPeriodoTurmaDisciplinaVO.setSituacaoNotificacaoAtrasoEstudosAluno(rs.getString("situacao"));
			matriculaPeriodoTurmaDisciplinaVOs.add(matriculaPeriodoTurmaDisciplinaVO);
		}
		return matriculaPeriodoTurmaDisciplinaVOs;
	}

	/**
	 * @author Victor Hugo 10/02/2015
	 * 
	 *         Método responsável por trazer os dados necessários para montar um
	 *         gráfico.
	 */
	@Override
	public List<ConteudoRegistroAcessoVO> consultarDataAcessoPontosPorAcessoDiaETotalAcumuladoGraficoLinhaEvolucaoAluno(Integer codigoMatriculaPeriodoTurmaDisciplina) throws Exception {

		StringBuilder sqlStr = new StringBuilder();

		sqlStr.append(" select dataacesso, sum(ponto) as ponto,");
		sqlStr.append(" (select sum(ponto) from (");
		sqlStr.append(" select dataacesso, sum(ponto) as ponto from (");
		sqlStr.append("		select distinct cast(min(conteudoregistroacesso.dataacesso) as date) as dataacesso, conteudoregistroacesso.conteudounidadepagina, conteudounidadepagina.ponto from conteudounidadepagina");
		sqlStr.append("		inner join conteudoregistroacesso on conteudoregistroacesso.conteudounidadepagina = conteudounidadepagina.codigo");
		sqlStr.append("		where conteudoregistroacesso.matriculaperiodoturmadisciplina = ").append(codigoMatriculaPeriodoTurmaDisciplina);
		sqlStr.append(" 	group by conteudoregistroacesso.conteudounidadepagina, conteudounidadepagina.ponto");
		sqlStr.append(") as t");
		sqlStr.append(" group by dataacesso");
		sqlStr.append(") as g");
		sqlStr.append(" where g.dataacesso <= t.dataacesso");
		sqlStr.append(") as totalacumulado");
		sqlStr.append(" from (");
		sqlStr.append("		select distinct cast(min(conteudoregistroacesso.dataacesso) as date) as dataacesso, conteudoregistroacesso.conteudounidadepagina, conteudounidadepagina.ponto from conteudounidadepagina");
		sqlStr.append("		inner join conteudoregistroacesso on conteudoregistroacesso.conteudounidadepagina = conteudounidadepagina.codigo");
		sqlStr.append("		where conteudoregistroacesso.matriculaperiodoturmadisciplina = ").append(codigoMatriculaPeriodoTurmaDisciplina);
		sqlStr.append("		group by conteudoregistroacesso.conteudounidadepagina, conteudounidadepagina.ponto");
		sqlStr.append(") as t");
		sqlStr.append(" group by dataacesso");
		sqlStr.append(" order by dataacesso asc");

		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<ConteudoRegistroAcessoVO> conteudoRegistroAcessoVOs = new ArrayList<ConteudoRegistroAcessoVO>();
		ConteudoRegistroAcessoVO conteudoRegistroAcessoVO = null;

		while (rs.next()) {
			conteudoRegistroAcessoVO = new ConteudoRegistroAcessoVO();
			conteudoRegistroAcessoVO.setPonto(rs.getDouble("ponto"));
			conteudoRegistroAcessoVO.setDataAcesso(rs.getDate("dataacesso"));
			conteudoRegistroAcessoVO.setTotalAcumulado(rs.getDouble("totalacumulado"));
			conteudoRegistroAcessoVOs.add(conteudoRegistroAcessoVO);
		}
		return conteudoRegistroAcessoVOs;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public synchronized void realizarAcessoEstudoOnline(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, Boolean forcarLiberacao, UsuarioVO usuarioVO) throws Exception {
		boolean existeConfiguracao = (getFacadeFactory().getConfiguracaoEADFacade().validarConfiguracaoEadInformadaParaTurma(matriculaPeriodoTurmaDisciplinaVO.getTurma().getCodigo()));
		if(!existeConfiguracao){
			throw new Exception("Não foi encontrado a configuração ead para o cadastro da turma por favor verificar.");	
		}
		getFacadeFactory().getProgramacaoTutoriaOnlineInterfaceFacade().realizarDefinicaoProfessorTutoriaOnline(matriculaPeriodoTurmaDisciplinaVO, usuarioVO, true, true);
		if (matriculaPeriodoTurmaDisciplinaVO.getModalidadeDisciplina().equals(ModalidadeDisciplinaEnum.ON_LINE)) {
			realizarRegrasNegocioParaMatriculaPeriodoTurmaDisciplinasModalidadeOnLine(matriculaPeriodoTurmaDisciplinaVO, forcarLiberacao, usuarioVO);
		} else if (matriculaPeriodoTurmaDisciplinaVO.getModalidadeDisciplina().equals(ModalidadeDisciplinaEnum.PRESENCIAL)) {
			realizarRegrasNegocioParaMatriculaPeriodoTurmaDisciplinasModalidadePresencial(matriculaPeriodoTurmaDisciplinaVO, usuarioVO);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void realizarRegrasNegocioParaMatriculaPeriodoTurmaDisciplinasModalidadeOnLine(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, Boolean forcarLiberar, UsuarioVO usuarioVO) throws Exception {
		if (matriculaPeriodoTurmaDisciplinaVO.getConteudo().getCodigo() == 0) {
			matriculaPeriodoTurmaDisciplinaVO.getConteudo().setCodigo(getFacadeFactory().getTurmaDisciplinaConteudoFacade().consultarConteudoDaDisciplinaAnoSemestre(matriculaPeriodoTurmaDisciplinaVO.getTurma().getCodigo(), matriculaPeriodoTurmaDisciplinaVO.getDisciplina().getCodigo(), matriculaPeriodoTurmaDisciplinaVO.getAno(), matriculaPeriodoTurmaDisciplinaVO.getSemestre(), usuarioVO));
			if (matriculaPeriodoTurmaDisciplinaVO.getConteudo().getCodigo().equals(0) && !getFacadeFactory().getConfiguracaoEADFacade().validarConfiguracaoEadPermitirAcessoSemConteudo(matriculaPeriodoTurmaDisciplinaVO.getTurma().getCodigo())) {
				throw new Exception(UteisJSF.internacionalizar("msg_AcessoEstudoOnline_naoPossuiConteudoParaTurmaDisciplinaAnoSemestre"));
			}
			if (!matriculaPeriodoTurmaDisciplinaVO.getConteudo().getCodigo().equals(0)) {
				incluirConteudoMatriculaPeriodoTurmaDisciplina(matriculaPeriodoTurmaDisciplinaVO);
			}
		}
		getFacadeFactory().getCalendarioAtividadeMatriculaInterfaceFacade().verificarExistenciaCalendariosAtividadesMatriculas(matriculaPeriodoTurmaDisciplinaVO, forcarLiberar, usuarioVO);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void realizarRegrasNegocioParaMatriculaPeriodoTurmaDisciplinasModalidadePresencial(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, UsuarioVO usuarioVO) throws Exception {
		Integer codigoConteudo;
		if (!Uteis.isAtributoPreenchido(matriculaPeriodoTurmaDisciplinaVO.getConteudo().getCodigo())) {
			codigoConteudo = getFacadeFactory().getTurmaDisciplinaConteudoFacade().consultarConteudoDaDisciplinaAnoSemestre(matriculaPeriodoTurmaDisciplinaVO.getTurma().getCodigo(), matriculaPeriodoTurmaDisciplinaVO.getDisciplina().getCodigo(), matriculaPeriodoTurmaDisciplinaVO.getAno(), matriculaPeriodoTurmaDisciplinaVO.getSemestre(), usuarioVO);
			if (codigoConteudo == 0 && !getFacadeFactory().getConfiguracaoEADFacade().validarConfiguracaoEadPermitirAcessoSemConteudo(matriculaPeriodoTurmaDisciplinaVO.getTurma().getCodigo())) {
				throw new Exception(UteisJSF.internacionalizar("msg_AcessoEstudoOnline_naoPossuiConteudoParaTurmaDisciplinaAnoSemestre"));
			}
			if (Uteis.isAtributoPreenchido(codigoConteudo)) {
				matriculaPeriodoTurmaDisciplinaVO.getConteudo().setCodigo(codigoConteudo);
				incluirConteudoMatriculaPeriodoTurmaDisciplina(matriculaPeriodoTurmaDisciplinaVO);
			}
		}
		getFacadeFactory().getCalendarioAtividadeMatriculaInterfaceFacade().verificarExistenciaCalendariosAtividadesMatriculas(matriculaPeriodoTurmaDisciplinaVO, false, usuarioVO);
	}

	/**
	 * 
	 * @author Victor Hugo 19/01/2014
	 * 
	 *         Metodo criado para incluir o codigo da programação tutoria
	 *         on-line professor na matriculaPeriodoTurmaDisciplinaVO no momento
	 *         em que o aluno acesso o conteúdo para estudo.
	 * 
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirProgramacaoTutoriaOnlineProfessorMatriculaPeriodoTurmaDisciplina(final MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, final Integer programacaoTutoriaOnlineProfessor) throws Exception {
		final String sql = "UPDATE MatriculaPeriodoTurmaDisciplina set programacaotutoriaonlineprofessor = ?, dataultimaalteracao=now() WHERE ((codigo = ?))";
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				if (programacaoTutoriaOnlineProfessor != 0) {
					sqlAlterar.setInt(1, programacaoTutoriaOnlineProfessor);
				} else {
					sqlAlterar.setNull(1, 0);
				}
				sqlAlterar.setInt(2, matriculaPeriodoTurmaDisciplinaVO.getCodigo());
				return sqlAlterar;
			}
		});
	}
	
	@Override
	public List<MatriculaPeriodoTurmaDisciplinaVO> consultaRapidaPorMatriculaEMatriculaPeriodoPorAvaliacaoInstitucional(String matricula, Integer disciplina, Integer matriculaPeriodo, AvaliacaoInstitucionalVO avaliacaoInstitucional,  Boolean trazerRespondido,  boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		StringBuffer sqlStr = new StringBuffer();						
		sqlStr.append(getSQLPadraoConsultaBasica());
		sqlStr.append(" inner join matricula on matriculaperiodoturmadisciplina.matricula = matricula.matricula ");
		sqlStr.append(" inner join historico on historico.matricula = matricula.matricula and historico.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo ");
		sqlStr.append(" inner join matriculaperiodo on matriculaperiodoturmadisciplina.matriculaperiodo = matriculaperiodo.codigo ");
		sqlStr.append(" where Matricula.matricula = '");
		sqlStr.append(matricula).append("' ");
		sqlStr.append(getSqlFiltroBaseGradeCurricularAtual(" and "));				
		sqlStr.append(" AND historico.matriculaperiodo = ").append(matriculaPeriodo).append(" ");
		sqlStr.append(" and (historico.historicoDisciplinaComposta is null or historico.historicoDisciplinaComposta = false) and (historico.gradedisciplina is not null or historico.gradeCurricularGrupoOptativaDisciplina is not null  ");
		sqlStr.append(" or historico.historicoDisciplinaForaGrade = true or historico.gradedisciplinacomposta is not null) and (historico.historicoporequivalencia is null or historico.historicoporequivalencia = false)  ");
		if (Uteis.isAtributoPreenchido(disciplina)) {
			sqlStr.append(" AND historico.disciplina = ").append(disciplina).append(" ");
		}
		
		sqlStr.append(" AND ( not exists ( select avaliacaoinstitucional from avaliacaoinstitucionalpessoaavaliada where avaliacaoinstitucionalpessoaavaliada.avaliacaoinstitucional = ").append(avaliacaoInstitucional.getCodigo()).append(" ) ");
		sqlStr.append(" or exists (select avaliacaoinstitucional from avaliacaoinstitucionalpessoaavaliada  ");		
		sqlStr.append("   where avaliacaoinstitucionalpessoaavaliada.avaliacaoinstitucional = ").append(avaliacaoInstitucional.getCodigo()).append(" ").append(AvaliacaoInstitucional.getSqlHorarioAulaAluno()).append(" )) ");

		if (!avaliacaoInstitucional.getAvaliarDisciplinasReposicao() && avaliacaoInstitucional.getQuestionarioVO().getEscopo().equals("DI")) {
			sqlStr.append(" and historico.tipohistorico not in ('AD', 'DE') ");
		}
		
		if (avaliacaoInstitucional.getQuestionarioVO().getEscopo().equals("PROFESSOR_TURMA") || avaliacaoInstitucional.getQuestionarioVO().getEscopo().equals("DI")) {
			sqlStr.append(" AND ((turmapratica.codigo is null and turmateorica.codigo is null and turma.considerarTurmaAvaliacaoInstitucional = true) ");
			sqlStr.append(" or (turmapratica.codigo is not null and turmateorica.codigo is null and turmapratica.considerarTurmaAvaliacaoInstitucional = true) ");
			sqlStr.append(" or (turmapratica.codigo is null and turmateorica.codigo is not null and turmateorica.considerarTurmaAvaliacaoInstitucional = true) ");
			sqlStr.append(" or (turmapratica.codigo is not null and turmateorica.codigo is not null and (turmateorica.considerarTurmaAvaliacaoInstitucional = true or turmapratica.considerarTurmaAvaliacaoInstitucional = true)) ");
			sqlStr.append(" ) ");
		}

		sqlStr.append(" ORDER BY disciplina.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());	
		return montarDadosConsultaBasica(tabelaResultado);
	}	
	
	public List<MatriculaPeriodoTurmaDisciplinaVO> consultaRapidaPorMatriculaEMatriculaPeriodo(String matricula, Integer disciplina, Integer matriculaPeriodo, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		StringBuffer sqlStr = new StringBuffer();						
		sqlStr.append(getSQLPadraoConsultaBasica());
		sqlStr.append(" inner join matricula on matriculaperiodoturmadisciplina.matricula = matricula.matricula ");
		sqlStr.append(" inner join historico on historico.matricula = matricula.matricula and historico.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo ");
		sqlStr.append(" inner join matriculaperiodo on matriculaperiodoturmadisciplina.matriculaperiodo = matriculaperiodo.codigo ");
		sqlStr.append(" where Matricula.matricula = '");
		sqlStr.append(matricula).append("' ");
		sqlStr.append(getSqlFiltroBaseGradeCurricularAtual(" and "));				
		sqlStr.append(" AND historico.matriculaperiodo = ").append(matriculaPeriodo).append(" ");
		sqlStr.append(" and (historico.historicoDisciplinaComposta is null or historico.historicoDisciplinaComposta = false) and (historico.gradedisciplina is not null or historico.gradeCurricularGrupoOptativaDisciplina is not null  ");
		sqlStr.append(" or historico.historicoDisciplinaForaGrade = true or historico.gradedisciplinacomposta is not null) and (historico.historicoporequivalencia is null or historico.historicoporequivalencia = false)  ");
		if (disciplina != null) {
			sqlStr.append(" AND historico.disciplina = ").append(disciplina).append(" ");
		}
		sqlStr.append(" ORDER BY disciplina.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());	
		return montarDadosConsultaBasica(tabelaResultado);
	}	
	
//	public void adicionarFiltroRelatorioAcademico(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, StringBuilder sqlStr) {
//		sqlStr.append(" AND matriculaperiodo.situacaomatriculaperiodo in (''");
//		if (filtroRelatorioAcademicoVO.getFormado()) {
//			sqlStr.append(", 'FO'");
//		}
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
//		if (filtroRelatorioAcademicoVO.getAbandonado()) {
//			sqlStr.append(", 'AC'");
//		}
//		if (filtroRelatorioAcademicoVO.getTransferenciaInterna()) {
//			sqlStr.append(", 'TI'");
//		}
//		if (filtroRelatorioAcademicoVO.getTransferenciaExterna()) {
//			sqlStr.append(", 'TS'");
//		}
//		if (filtroRelatorioAcademicoVO.getConcluido()) {
//			sqlStr.append(", 'FI'");
//		}
//		if (filtroRelatorioAcademicoVO.getCancelado()) {
//			sqlStr.append(", 'CA'");
//		}
//		sqlStr.append(" ) ");
//		if (!filtroRelatorioAcademicoVO.getPendenteFinanceiro()) {
//			sqlStr.append(" AND matriculaperiodo.situacao != 'PF'");
//		}
//	}
	
	@Override
	public List<MatriculaPeriodoTurmaDisciplinaVO> consultarPorMatriculaEUltimaMatriculaPeriodo(String matricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT mptd.* from MatriculaPeriodoTurmaDisciplina mptd ");
		sqlStr.append("where mptd.matriculaperiodo = ( ");
		sqlStr.append("SELECT mp.codigo from matriculaperiodo mp ");
		sqlStr.append("where mp.matricula = '").append(matricula).append("' ");
		sqlStr.append("and mp.situacaomatriculaperiodo not in('PC') ");
		sqlStr.append("order by case when mp.situacaomatriculaperiodo in ('AT','FI') and ((mp.ano <> '' and mp.semestre <> '' and mp.ano = '").append(Uteis.getAnoDataAtual()).append("' ");
		sqlStr.append(" and mp.semestre = '").append(Uteis.getSemestreAtual()).append("') or (mp.ano <> '' and mp.semestre = '' and mp.ano = '").append(Uteis.getAnoDataAtual()).append("')) then 2 else ");
		sqlStr.append(" case when mp.situacaomatriculaperiodo IN ('AT', 'FI') then 1 else 0 end end desc, (mp.ano ||'/' || mp.semestre) desc limit 1) ");
		sqlStr.append("order by mptd.codigo");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}
	
	public List<MatriculaPeriodoTurmaDisciplinaVO> consultaRapidaDisciplinaCompostaPorMatriculaPeriodo(Integer matriculaPeriodo, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sql = getSQLPadraoConsultaCompleta();
		sql.append("WHERE matriculaPeriodo = ").append(matriculaPeriodo);
		sql.append(" AND (matriculaPeriodoTurmaDisciplina.disciplinafazpartecomposicao is null or matriculaPeriodoTurmaDisciplina.disciplinafazpartecomposicao = false) ");
		/**
		 * Adicionada regra para caso exista mais de uma matrícula período turma
		 * disciplina, porém com turmas diferentes, considerar a de maior código.
		 */
		sql.append("and matriculaPeriodoTurmaDisciplina.codigo in ( ");
		sql.append("select max(mptd.codigo) from matriculaPeriodoTurmaDisciplina mptd ");
		sql.append("where mptd.matriculaperiodo = matriculaPeriodoTurmaDisciplina.matriculaperiodo ");
		sql.append("and mptd.disciplina = matriculaPeriodoTurmaDisciplina.disciplina ");
		sql.append("and mptd.ano = matriculaPeriodoTurmaDisciplina.ano ");
		sql.append("and mptd.semestre = matriculaPeriodoTurmaDisciplina.semestre ");
		sql.append("group by mptd.disciplina ");
		sql.append("having count(mptd.disciplina) > 1 ");
		sql.append("union ");
		sql.append("select max(mptd.codigo) from matriculaPeriodoTurmaDisciplina mptd ");
		sql.append("where mptd.matriculaperiodo = matriculaPeriodoTurmaDisciplina.matriculaperiodo ");
		sql.append("and mptd.disciplina = matriculaPeriodoTurmaDisciplina.disciplina ");
		sql.append("and mptd.ano = matriculaPeriodoTurmaDisciplina.ano ");
		sql.append("and mptd.semestre = matriculaPeriodoTurmaDisciplina.semestre ");
		sql.append("group by mptd.disciplina ");
		sql.append("having count(mptd.disciplina) = 1) ");
		sql.append("ORDER BY disciplina.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaCompleta(tabelaResultado);
	}
	
	public List<MatriculaPeriodoTurmaDisciplinaVO> consultarRapidaDisciplinaFazParteComposicaoPorMatriculaPeriodoGradeDisciplina(Integer matriculaPeriodo, Integer gradeDisciplina, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), verificarAcesso, usuarioVO);
		StringBuffer sqlStr = getSQLPadraoConsultaCompleta();
		sqlStr.append(" WHERE matriculaPeriodo = ").append(matriculaPeriodo);
		sqlStr.append(" AND gradedisciplinacomposta IN (");
		sqlStr.append(" 	SELECT codigo FROM gradedisciplinacomposta WHERE gradedisciplina = ").append(gradeDisciplina).append(")");
		sqlStr.append(" AND disciplinafazpartecomposicao ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaCompleta(tabelaResultado);
	}
	
	@Override
	public List<MatriculaPeriodoTurmaDisciplinaVO> consultarPorMatriculaPeriodoGradeCurricularAtual(Integer matriculaPeriodo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		MatriculaPeriodoTurmaDisciplina.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select matriculaperiodoturmadisciplina.* from historico ");
		sqlStr.append("inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.codigo = historico.matriculaperiodoturmadisciplina ");
		sqlStr.append("inner join matricula on matricula.matricula = matriculaperiodoturmadisciplina.matricula ");
		sqlStr.append(" inner join matriculaPeriodo on matriculaPeriodo.codigo = matriculaperiodoturmadisciplina.matriculaPeriodo ");
		sqlStr.append("where matriculaperiodoturmadisciplina.matriculaperiodo = ? ");
		/**
		 * Adicionada regra para resolver impactos relacionados a alunos que
		 * estão Cursando por Correspondência e que disciplinas saiam duplicadas
		 * no Boletim Acadêmico
		 */
		sqlStr.append(getSqlFiltroBaseGradeCurricularAtual(" and "));
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { matriculaPeriodo });
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}
	
	public StringBuilder getSqlConsultarRapidaTrazendoMatricula(){
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select distinct matriculaPeriodoTurmaDisciplina.codigo, matriculaPeriodoTurmaDisciplina.modalidadeDisciplina, matriculaPeriodoTurmaDisciplina.professor as professor, ");
		sqlStr.append(" matriculaPeriodoTurmaDisciplina.matriculaperiodo AS \"matriculaPeriodoTurmaDisciplina.matriculaperiodo\", matriculaPeriodoTurmaDisciplina.matricula AS \"matriculaPeriodoTurmaDisciplina.matricula\", ");
		sqlStr.append(" matriculaPeriodoTurmaDisciplina.ano AS \"matriculaPeriodoTurmaDisciplina.ano\", matriculaPeriodoTurmaDisciplina.semestre AS \"matriculaPeriodoTurmaDisciplina.semestre\",  matriculaPeriodoTurmaDisciplina.bimestre AS \"matriculaPeriodoTurmaDisciplina.bimestre\", ");
		sqlStr.append(" turma.codigo AS \"Turma.codigo\", turma.identificadorTurma AS \"Turma.identificadorTurma\",  matricula.gradecurricularatual as \"matricula.gradecurricularatual\",  ");
		sqlStr.append(" disciplina.codigo AS \"Disciplina.codigo\", disciplina.nome AS \"Disciplina.nome\", ");
		sqlStr.append(" pessoa.codigo AS \"pessoa.codigo\", pessoa.nome AS \"pessoa.nome\", ");
		sqlStr.append(" curso.codigo AS \"curso.codigo\", curso.nome AS \"curso.nome\", curso.periodicidade AS \"curso.periodicidade\", ");
		sqlStr.append(" turno.codigo AS \"turno.codigo\", turno.nome AS \"turno.nome\", ");
		sqlStr.append(" unidadeensino.codigo AS \"unidadeensino.codigo\", unidadeensino.nome AS \"unidadeensino.nome\", ");
		sqlStr.append(" matriculaperiodoturmadisciplina.turmapratica,turmapratica.identificadorturma AS \"turmapratica.identificadorturma\",turmapratica.subturma AS \"turmapratica.subturma\",	turmapratica.tiposubturma AS \"turmapratica.tiposubturma\", ");
		sqlStr.append(" matriculaperiodoturmadisciplina.turmateorica,turmateorica.identificadorturma AS \"turmateorica.identificadorturma\",turmateorica.subturma AS \"turmateorica.subturma\",	turmateorica.tiposubturma AS \"turmateorica.tiposubturma\" ");
		sqlStr.append(" from matriculaperiodoturmadisciplina ");		
		sqlStr.append(" inner join matricula on matricula.matricula = matriculaperiodoturmadisciplina.matricula ");
		sqlStr.append(" inner join matriculaperiodo on matriculaperiodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo ");
		sqlStr.append(" inner join pessoa on matricula.aluno = pessoa.codigo ");
		sqlStr.append(" inner join curso on matricula.curso = curso.codigo ");
		sqlStr.append(" inner join turno on matricula.turno = turno.codigo ");
		sqlStr.append(" inner join unidadeensino on matricula.unidadeensino = unidadeensino.codigo ");
		sqlStr.append(" LEFT JOIN turma ON turma.codigo = matriculaPeriodoTurmaDisciplina.turma ");
		sqlStr.append(" LEFT JOIN turma as turmateorica on	turmateorica.codigo = matriculaPeriodoTurmaDisciplina.turmateorica");
		sqlStr.append(" LEFT JOIN turma as turmapratica on turmapratica.codigo = matriculaPeriodoTurmaDisciplina.turmapratica");
		sqlStr.append(" LEFT JOIN disciplina ON disciplina.codigo = matriculaPeriodoTurmaDisciplina.disciplina ");
		return sqlStr;
	}
	
	@Override
	public List<MatriculaPeriodoTurmaDisciplinaVO> consultarRapidaPorUltimaMatriculaPeriodo(String matricula, Boolean trazerDisciplinaComposta, Boolean trazerDisciplinaFazemParteComposicao, Boolean trazerDisciplinaForaGrade, Boolean trazerDisciplinaEquivalente, Boolean trazerDisciplinaPorEquivalenvia, String situacoesMatriculaPeriodo, String situacoesMatricula) throws Exception {
		StringBuilder sqlStr = getSqlConsultarRapidaTrazendoMatricula();
		sqlStr.append(" inner join historico on historico.matriculaPeriodoTurmaDisciplina  =  matriculaPeriodoTurmaDisciplina.codigo ");
		sqlStr.append(" where matriculaperiodo.codigo = ( ");
		sqlStr.append(" select mp.codigo from matriculaperiodo mp where mp.matricula = matricula.matricula ");
		sqlStr.append(" order by (mp.ano || '/' || mp.semestre) desc, case when mp.situacaoMatriculaPeriodo in ('AT', 'PR', 'FI', 'FO') then 1 else 2 end, mp.codigo desc limit 1");
		sqlStr.append(" )");
		sqlStr.append(getSqlFiltroBaseGradeCurricularAtual(" and "));
		if(!trazerDisciplinaComposta){
			sqlStr.append(" and (historico.historicoDisciplinaComposta is null or historico.historicoDisciplinaComposta = false)");
		}
		if(!trazerDisciplinaFazemParteComposicao){
			sqlStr.append(" and (historico.historicoDisciplinaFazParteComposicao is null or historico.historicoDisciplinaFazParteComposicao = false)");
		}
		if(!trazerDisciplinaForaGrade){
			sqlStr.append(" and (historico.historicoDisciplinaForaGrade is null or historico.historicoDisciplinaForaGrade = false)");
		}
		if(!trazerDisciplinaEquivalente){
			sqlStr.append(" and (historico.historicoequivalente is null or historico.historicoequivalente = false)");
		}
		if(!trazerDisciplinaPorEquivalenvia){
			sqlStr.append(" and (historico.historicoporequivalencia is null or historico.historicoporequivalencia = false)");
		}
		if(Uteis.isAtributoPreenchido(situacoesMatricula)){
			sqlStr.append(" and (matricula.situacao not in ("+situacoesMatricula+"))");
		}
		if(Uteis.isAtributoPreenchido(situacoesMatriculaPeriodo)){
			sqlStr.append(" and (matriculaperiodo.situacaomatriculaperiodo not in ("+situacoesMatriculaPeriodo+"))");
		}
		sqlStr.append(" order by disciplina.nome ");
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<MatriculaPeriodoTurmaDisciplinaVO> objs = new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>(0);		
		while(dadosSQL.next()){
			objs.add(montarDadosBasicoTrazendoMatricula(dadosSQL));			
		}
		return objs;
	}
	
	@Override
	public List<MatriculaPeriodoTurmaDisciplinaVO> consultarRapidaPorMatriculaTurmaDisciplinaAnoSemestreGradeCurricularAtual(String matricula, Integer turma, Integer disciplina, String ano, String semestre, Boolean trazerDisciplinaComposta, Boolean trazerDisciplinaFazemParteComposicao, Boolean trazerDisciplinaForaGrade, Boolean trazerDisciplinaEquivalente, Boolean trazerDisciplinaPorEquivalenvia, String situacoesMatriculaPeriodo, String situacoesMatricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		MatriculaPeriodoTurmaDisciplina.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = getSqlConsultarRapidaTrazendoMatricula();
		sqlStr.append(" inner join historico on historico.matriculaPeriodoTurmaDisciplina  =  matriculaPeriodoTurmaDisciplina.codigo ");
		sqlStr.append(" where ");
		sqlStr.append("  ((curso.periodicidade = 'IN') or ");
		sqlStr.append(" (curso.periodicidade = 'AN' and matriculaperiodoturmadisciplina.ano = '"+ano+"') or");
		sqlStr.append(" (curso.periodicidade = 'SE' and matriculaperiodoturmadisciplina.ano = '"+ano+"' and matriculaperiodoturmadisciplina.semestre = '"+semestre+"') ");
		sqlStr.append(" )");
		if(Uteis.isAtributoPreenchido(turma)){
			sqlStr.append(" and  matriculaperiodoturmadisciplina.turma = ").append(turma);
		}
		if(Uteis.isAtributoPreenchido(disciplina)){
			sqlStr.append(" and  matriculaperiodoturmadisciplina.disciplina = ").append(disciplina);
		}
		
		if(matricula != null && !matricula.trim().isEmpty()){
			sqlStr.append(" and matriculaperiodoturmadisciplina.matricula =  '").append(matricula).append("' ");
		}
		/**
		 * Adicionada regra para resolver impactos relacionados a alunos que
		 * estão Cursando por Correspondência e que disciplinas saiam duplicadas
		 * no Boletim Acadêmico
		 */
		sqlStr.append(getSqlFiltroBaseGradeCurricularAtual(" and"));
		if(!trazerDisciplinaComposta){
			sqlStr.append(" and (historico.historicoDisciplinaComposta is null or historico.historicoDisciplinaComposta = false)");
		}
		if(!trazerDisciplinaFazemParteComposicao){
			sqlStr.append(" and (historico.historicoDisciplinaFazParteComposicao is null or historico.historicoDisciplinaFazParteComposicao = false)");
		}
		if(!trazerDisciplinaForaGrade){
			sqlStr.append(" and (historico.historicoDisciplinaForaGrade is null or historico.historicoDisciplinaForaGrade = false)");
		}
		if(!trazerDisciplinaEquivalente){
			sqlStr.append(" and (historico.historicoequivalente is null or historico.historicoequivalente = false)");
		}
		if(!trazerDisciplinaPorEquivalenvia){
			sqlStr.append(" and (historico.historicoporequivalencia is null or historico.historicoporequivalencia = false)");
		}
		if(Uteis.isAtributoPreenchido(situacoesMatricula)){
			sqlStr.append(" and (matricula.situacao not in ("+situacoesMatricula+"))");
		}
		if(Uteis.isAtributoPreenchido(situacoesMatriculaPeriodo)){
			sqlStr.append(" and (matriculaperiodo.situacaomatriculaperiodo not in ("+situacoesMatriculaPeriodo+"))");
		}
		sqlStr.append(" order by pessoa.nome ");
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<MatriculaPeriodoTurmaDisciplinaVO> objs = new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>(0);		
		while(dadosSQL.next()){
			objs.add(montarDadosBasicoTrazendoMatricula(dadosSQL));			
		}
		return objs;
		
	}
	
	@Override
	public MatriculaPeriodoTurmaDisciplinaVO consultaRapidaPorChavePrimariaTrazendoMatricula(Integer codigo) throws Exception{		
		StringBuilder sql = getSqlConsultarRapidaTrazendoMatricula();
		sql.append(" where matriculaperiodoturmadisciplina.codigo = ").append(codigo);
		SqlRowSet dadosSQL  = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		MatriculaPeriodoTurmaDisciplinaVO obj = new MatriculaPeriodoTurmaDisciplinaVO();
		if(dadosSQL.next()){
			return montarDadosBasicoTrazendoMatricula(dadosSQL);				
		}
		return obj;
	}
	
	public MatriculaPeriodoTurmaDisciplinaVO montarDadosBasicoTrazendoMatricula(SqlRowSet dadosSQL){
		MatriculaPeriodoTurmaDisciplinaVO obj = new MatriculaPeriodoTurmaDisciplinaVO();
		
			obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
			obj.setMatriculaPeriodo(new Integer(dadosSQL.getInt("MatriculaPeriodoTurmaDisciplina.matriculaPeriodo")));			
			obj.setModalidadeDisciplina(ModalidadeDisciplinaEnum.valueOf(dadosSQL.getString("modalidadeDisciplina")));
			obj.setAno(dadosSQL.getString("MatriculaPeriodoTurmaDisciplina.ano"));
			obj.setBimestre(dadosSQL.getInt("MatriculaPeriodoTurmaDisciplina.bimestre"));
			obj.setSemestre(dadosSQL.getString("MatriculaPeriodoTurmaDisciplina.semestre"));
			obj.setMatricula(dadosSQL.getString("MatriculaPeriodoTurmaDisciplina.matricula"));
			obj.getProfessor().setCodigo(dadosSQL.getInt("professor"));
			obj.setNovoObj(Boolean.FALSE);
			
			obj.getDisciplina().setCodigo(dadosSQL.getInt("disciplina.codigo"));
			obj.getDisciplina().setNome(dadosSQL.getString("disciplina.nome"));

			obj.setNivelMontarDados(NivelMontarDados.BASICO);
			obj.getTurmaPratica().setCodigo(dadosSQL.getInt("turmapratica"));
			obj.getTurmaPratica().setIdentificadorTurma(dadosSQL.getString("turmateorica.identificadorturma"));
			obj.getTurmaPratica().setSubturma(dadosSQL.getBoolean("turmateorica.subturma"));
			if (Uteis.isAtributoPreenchido(dadosSQL.getString("turmateorica.tiposubturma"))) {
				obj.getTurmaPratica().setTipoSubTurma(TipoSubTurmaEnum.valueOf(dadosSQL.getString("turmateorica.tiposubturma")));
			}
			obj.getTurmaTeorica().setCodigo(dadosSQL.getInt("turmateorica"));
			obj.getTurmaTeorica().setIdentificadorTurma(dadosSQL.getString("turmateorica.identificadorturma"));
			obj.getTurmaTeorica().setSubturma(dadosSQL.getBoolean("turmateorica.subturma"));
			if (Uteis.isAtributoPreenchido(dadosSQL.getString("turmateorica.tiposubturma"))) {
				obj.getTurmaTeorica().setTipoSubTurma(TipoSubTurmaEnum.valueOf(dadosSQL.getString("turmateorica.tiposubturma")));
			}
			obj.getTurma().setCodigo(dadosSQL.getInt("Turma.codigo"));
			obj.getTurma().setSemestral(dadosSQL.getString("Curso.periodicidade").equals("SE"));
			obj.getTurma().setAnual(dadosSQL.getString("Curso.periodicidade").equals("AN"));
			obj.getTurma().setIdentificadorTurma(dadosSQL.getString("Turma.identificadorturma"));

			// Aluno
			obj.getMatriculaObjetoVO().setMatricula(dadosSQL.getString("MatriculaPeriodoTurmaDisciplina.matricula"));
			obj.getMatriculaObjetoVO().getGradeCurricularAtual().setCodigo((dadosSQL.getInt("matricula.gradecurricularatual")));
			obj.getMatriculaObjetoVO().getAluno().setCodigo((dadosSQL.getInt("pessoa.codigo")));
			obj.getMatriculaObjetoVO().getAluno().setNome((dadosSQL.getString("pessoa.nome")));
			
			// Unidade Ensino
			obj.getMatriculaObjetoVO().getUnidadeEnsino().setCodigo((dadosSQL.getInt("Unidadeensino.codigo")));
			obj.getMatriculaObjetoVO().getUnidadeEnsino().setNome((dadosSQL.getString("Unidadeensino.nome")));
			// Curso
			obj.getMatriculaObjetoVO().getCurso().setCodigo(dadosSQL.getInt("Curso.codigo"));
			obj.getMatriculaObjetoVO().getCurso().setNome(dadosSQL.getString("Curso.nome"));					
			obj.getMatriculaObjetoVO().getCurso().setPeriodicidade(dadosSQL.getString("Curso.periodicidade"));					
			// Turno
			obj.getMatriculaObjetoVO().getTurno().setCodigo((dadosSQL.getInt("Turno.codigo")));
			obj.getMatriculaObjetoVO().getTurno().setNome((dadosSQL.getString("Turno.nome")));

		return obj;
	}
	
	public static StringBuilder getSqlFiltroBaseGradeCurricularAtual(String whereAnd){
		StringBuilder sqlStr = new StringBuilder(" ")
				.append(whereAnd).append(" (").append(getSqlFiltroBaseGradeCurricularAtual(false)).append(")");
		return sqlStr;
	}
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarAtualizacaoTurmaBaseTurmaPraticaTurmaTeoricaPorTransferenciaTurma(Integer matriculaPeriodoTurmaDisciplina, Integer turmaBase, Integer turmaPratica, Integer turmaTeorica, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder( "UPDATE matriculaperiodoturmadisciplina SET dataultimaalteracao=now() ");
		if(Uteis.isAtributoPreenchido(turmaBase)) {
			sqlStr.append(", turma  = ").append(turmaBase);
		}
		if(Uteis.isAtributoPreenchido(turmaPratica)) {
			sqlStr.append(", turmaPratica  = ").append(turmaPratica);
		}
		if(Uteis.isAtributoPreenchido(turmaTeorica)) {
			sqlStr.append(", turmaTeorica  = ").append(turmaTeorica);
		}
		sqlStr.append(" where  codigo = ").append(matriculaPeriodoTurmaDisciplina);
		sqlStr.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sqlStr.toString());
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarTurmaPorMatriculaPeriodoTurmaDisciplinaTeorica(Integer matriculaPeriodoTurmaDisciplina, Integer turmaTeorica, UsuarioVO usuarioVO) throws Exception {
		String sqlStr = "UPDATE matriculaperiodoturmadisciplina SET turmaTeorica = ?, dataultimaalteracao=now() WHERE codigo = ?";
		getConexao().getJdbcTemplate().update(sqlStr, new Object[] { turmaTeorica, matriculaPeriodoTurmaDisciplina });
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarTurmaPorMatriculaPeriodoTurmaDisciplinaPratica(Integer matriculaPeriodoTurmaDisciplina, Integer turmaPratica, UsuarioVO usuarioVO) throws Exception {
		String sqlStr = "UPDATE matriculaperiodoturmadisciplina SET turmaPratica = ?, dataultimaalteracao=now() WHERE codigo = ?";
		getConexao().getJdbcTemplate().update(sqlStr, new Object[] { turmaPratica, matriculaPeriodoTurmaDisciplina });
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void removerTurmaPorMatriculaPeriodoTurmaDisciplinaPratica(Integer matriculaPeriodoTurmaDisciplina, UsuarioVO usuarioVO) throws Exception {
		String sqlStr = "UPDATE matriculaperiodoturmadisciplina SET turmaPratica = null, dataultimaalteracao=now() WHERE codigo = ?";
		getConexao().getJdbcTemplate().update(sqlStr, new Object[] { matriculaPeriodoTurmaDisciplina });
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void removerTurmaPorMatriculaPeriodoTurmaDisciplinaTeorica(Integer matriculaPeriodoTurmaDisciplina, UsuarioVO usuarioVO) throws Exception {
		String sqlStr = "UPDATE matriculaperiodoturmadisciplina SET turmaTeorica = null, dataultimaalteracao=now() WHERE codigo = ?";
		getConexao().getJdbcTemplate().update(sqlStr, new Object[] { matriculaPeriodoTurmaDisciplina });
	}
	
	private List<MatriculaPeriodoTurmaDisciplinaVO> montarDadosConsultaAlunoSubturmaPorTurmaPrincipalAnoSemestreDisciplina(SqlRowSet rs) throws Exception {
		List<MatriculaPeriodoTurmaDisciplinaVO> mptdVOs = new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>(0);
		while (rs.next()) {
			MatriculaPeriodoTurmaDisciplinaVO mptd = new MatriculaPeriodoTurmaDisciplinaVO();
			mptd.setNovoObj(false);
			mptd.setCodigo(rs.getInt("codigo"));
			mptd.setAno(rs.getString("MatriculaPeriodoTurmaDisciplina.ano"));
			mptd.setSemestre(rs.getString("MatriculaPeriodoTurmaDisciplina.semestre"));
			mptd.getTurma().setCodigo(rs.getInt("turma"));
			mptd.getTurma().setIdentificadorTurma(rs.getString("identificadorTurma"));
			mptd.getTurma().setNrMaximoMatricula(rs.getInt("nrMaximoMatricula"));
			mptd.getTurma().setAnual(rs.getBoolean("anual"));
			mptd.getTurma().setSemestral(rs.getBoolean("semestral"));
			mptd.getDisciplina().setCodigo(rs.getInt("disciplina"));
			mptd.getDisciplina().setNome(rs.getString("nomeDisciplina"));
			mptd.getMatriculaObjetoVO().setMatricula(rs.getString("matricula"));
			mptd.getMatriculaObjetoVO().getAluno().setCodigo(rs.getInt("aluno"));
			mptd.getMatriculaObjetoVO().getAluno().setNome(rs.getString("nomeAluno"));
			mptd.getMatriculaPeriodoObjetoVO().setCodigo(rs.getInt("matriculaperiodo"));
			mptd.getMatriculaPeriodoObjetoVO().getMatriculaVO().setMatricula(rs.getString("matricula"));
			mptd.getMatriculaPeriodoObjetoVO().getMatriculaVO().getAluno().setCodigo(rs.getInt("aluno"));
			mptd.getMatriculaPeriodoObjetoVO().getMatriculaVO().getAluno().setNome(rs.getString("nomeAluno"));
			mptd.getMatriculaPeriodoObjetoVO().setSituacaoMatriculaPeriodo(rs.getString("situacao"));
			mptd.getMatriculaPeriodoObjetoVO().setAno(rs.getString("ano"));
			mptd.getMatriculaPeriodoObjetoVO().setSemestre(rs.getString("semestre"));
			mptd.getGradeDisciplinaVO().setCodigo(rs.getInt("gradeDisciplina"));
			mptd.getTurmaTeorica().setCodigo(rs.getInt("turmaTeorica"));
			mptd.getTurmaTeorica().setTurmaAgrupada(rs.getBoolean("turmaTeoricaAgrupada"));
			mptd.getTurmaTeorica().setTipoSubTurma(TipoSubTurmaEnum.TEORICA);
			mptd.getTurmaPratica().setCodigo(rs.getInt("turmaPratica"));
			mptd.getTurmaPratica().setTurmaAgrupada(rs.getBoolean("turmaPraticaAgrupada"));
			mptd.getTurmaPratica().setTipoSubTurma(TipoSubTurmaEnum.PRATICA);
			mptdVOs.add(mptd);
		}
		return mptdVOs;
	}
	
	/*
	 @throws Exception - Caso retorne consistir exception com o boolean Referente Choque Horário Marcado e Com a Lista de Mensagem preenchida então,
	 * neste caso de todas as possibilidades para definir uma turma prática/teórica acabaram gerando choque de horário, neste caso esta matricula periodo turma disciplina
	 * não poderá ser adicionada na matrícula.
	 */
	@Override
	public void realizarSugestaoTurmaPraticaTeorica(MatriculaPeriodoVO matriculaPeriodoVO, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, DisciplinaVO disciplina, ConfiguracaoAcademicoVO configuracaoAcademicoVO, List<HorarioAlunoTurnoVO> horarioAlunoTurnoVOs, Boolean considerarVagasReposicao, UsuarioVO usuarioVO) throws Exception{		
//		if(Uteis.isAtributoPreenchido(configuracaoAcademicoVO) && configuracaoAcademicoVO.getHabilitarDistribuicaoTurmaPraticaTeoricaRenovacao() && !matriculaPeriodoTurmaDisciplinaVO.getDisciplinaComposta() && !matriculaPeriodoTurmaDisciplinaVO.getSugestaoTurmaPraticaTeoricaRealizada() && !Uteis.isAtributoPreenchido(matriculaPeriodoTurmaDisciplinaVO.getCodigo())){
//			StringBuilder sql = new StringBuilder("");
//			if(!Uteis.isAtributoPreenchido(matriculaPeriodoTurmaDisciplinaVO.getTurmaPratica())){
//				sql.append(getFacadeFactory().getTurmaFacade().getSqlConsultaSubTurmaParaDistribuicaoAutomatica(matriculaPeriodoTurmaDisciplinaVO.getTurma().getCodigo(), disciplina.getCodigo(), matriculaPeriodoTurmaDisciplinaVO.getDisciplinaFazParteComposicao(), TipoSubTurmaEnum.PRATICA, matriculaPeriodoVO.getAno(), matriculaPeriodoVO.getSemestre(), true, true, configuracaoAcademicoVO.getDistribuirTurmaPraticaTeoricaComAulaProgramada(), matriculaPeriodoVO.getMatricula(), considerarVagasReposicao));
//			}
//			if(!Uteis.isAtributoPreenchido(matriculaPeriodoTurmaDisciplinaVO.getTurmaTeorica())){
//				if(!sql.toString().isEmpty()){
//					sql.append(" UNION ");
//				}
//				sql.append(getFacadeFactory().getTurmaFacade().getSqlConsultaSubTurmaParaDistribuicaoAutomatica(matriculaPeriodoTurmaDisciplinaVO.getTurma().getCodigo(), disciplina.getCodigo(), matriculaPeriodoTurmaDisciplinaVO.getDisciplinaFazParteComposicao(), TipoSubTurmaEnum.TEORICA, matriculaPeriodoVO.getAno(), matriculaPeriodoVO.getSemestre(), true, true, configuracaoAcademicoVO.getDistribuirTurmaPraticaTeoricaComAulaProgramada(), matriculaPeriodoVO.getMatricula(), considerarVagasReposicao));
//			}			
//			if(!sql.toString().isEmpty()){
//				String[] turmasPraticas =  null;
//				String[] turmasTeoricas =  null;
//				SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
//				while(rs.next()){
//					if(rs.getString("tiposubturma").equals(TipoSubTurmaEnum.PRATICA.getName())){
//						turmasPraticas = rs.getString("turmas").split(",");
//					}
//					if(rs.getString("tiposubturma").equals(TipoSubTurmaEnum.TEORICA.getName())){
//						turmasTeoricas = rs.getString("turmas").split(",");
//					}
//				}	
//				if(turmasPraticas != null || turmasTeoricas != null){
//					realizarValidacaoChoqueHorarioCombinacaoTurmaTeoricaComPratica(matriculaPeriodoVO, matriculaPeriodoTurmaDisciplinaVO, disciplina, turmasPraticas, turmasTeoricas, configuracaoAcademicoVO, horarioAlunoTurnoVOs, usuarioVO);
//					if(Uteis.isAtributoPreenchido(matriculaPeriodoTurmaDisciplinaVO.getTurmaPratica()) || Uteis.isAtributoPreenchido(matriculaPeriodoTurmaDisciplinaVO.getTurmaTeorica())){ 
//						getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().atualizarNrAlunosMatriculadosTurmaDisciplina(matriculaPeriodoVO, matriculaPeriodoTurmaDisciplinaVO, disciplina, matriculaPeriodoVO.getAno(), matriculaPeriodoVO.getSemestre(), true, considerarVagasReposicao);
//					}
//				}
//			}
//			matriculaPeriodoTurmaDisciplinaVO.setSugestaoTurmaPraticaTeoricaRealizada(true);
//		}		
	}
	
	private void realizarValidacaoChoqueHorarioCombinacaoTurmaTeoricaComPratica(MatriculaPeriodoVO matriculaPeriodoVO, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, DisciplinaVO disciplinaVO, String[] turmasPraticas, String[] turmasTeoricas, ConfiguracaoAcademicoVO configuracaoAcademicoVO, List<HorarioAlunoTurnoVO> horarioAlunoTurnoVOs, UsuarioVO usuarioVO) throws Exception{
		List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs =  new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>(0);
		matriculaPeriodoTurmaDisciplinaVOs.addAll(matriculaPeriodoVO.getMatriculaPeriodoTumaDisciplinaVOs());
		for (Iterator<MatriculaPeriodoTurmaDisciplinaVO> iterator = matriculaPeriodoTurmaDisciplinaVOs.iterator(); iterator.hasNext();) {
			MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO2 = (MatriculaPeriodoTurmaDisciplinaVO) iterator.next();
			if(matriculaPeriodoTurmaDisciplinaVO2.getDisciplina().getCodigo().equals(disciplinaVO.getCodigo())){
				iterator.remove();
			}
		}
		ConsistirException msgChoqueHorario = new ConsistirException();
		msgChoqueHorario.setReferenteChoqueHorario(true);		
		if(turmasPraticas != null){
			for(String turmaPratica: turmasPraticas){
				matriculaPeriodoTurmaDisciplinaVO.getTurmaPratica().setCodigo(Integer.valueOf(turmaPratica));
				if(turmasTeoricas != null){
					
					for(String turmaTeorica: turmasTeoricas){
						try {
							matriculaPeriodoTurmaDisciplinaVO.getTurmaTeorica().setCodigo(0);
							if (!getFacadeFactory().getHorarioAlunoFacade().realizarVerificaoChoqueHorarioPorMatriculaPeriodoTurmaDisciplinaVOs(matriculaPeriodoVO, horarioAlunoTurnoVOs, matriculaPeriodoTurmaDisciplinaVOs, matriculaPeriodoTurmaDisciplinaVO.getTurma().getCodigo(), Integer.valueOf(turmaPratica), Integer.valueOf(turmaTeorica), disciplinaVO.getCodigo(), usuarioVO, configuracaoAcademicoVO.getRemoverDisciplinaTurmaPraticaTeoricaComChoqueHorario(), configuracaoAcademicoVO.getValidarChoqueHorarioOutraMatriculaAluno())) {
								for (Iterator<MatriculaPeriodoTurmaDisciplinaVO> iterator = matriculaPeriodoTurmaDisciplinaVOs.iterator(); iterator.hasNext();) {
									MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO2 = (MatriculaPeriodoTurmaDisciplinaVO) iterator.next();
									if(matriculaPeriodoTurmaDisciplinaVO2.getDisciplina().getCodigo().equals(disciplinaVO.getCodigo())){
										iterator.remove();
									}
								}
								MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaClone = (MatriculaPeriodoTurmaDisciplinaVO) matriculaPeriodoTurmaDisciplinaVO.clone();
								matriculaPeriodoTurmaDisciplinaClone.setTurmaPratica(new TurmaVO());
								matriculaPeriodoTurmaDisciplinaClone.setAno(matriculaPeriodoVO.getAno());
								matriculaPeriodoTurmaDisciplinaClone.setSemestre(matriculaPeriodoVO.getSemestre());
								matriculaPeriodoTurmaDisciplinaClone.setMatriculaObjetoVO(matriculaPeriodoVO.getMatriculaVO());
								matriculaPeriodoTurmaDisciplinaClone.getTurmaTeorica().setCodigo(Integer.valueOf(turmaTeorica));			
								matriculaPeriodoTurmaDisciplinaClone.setDisciplina(disciplinaVO);
								matriculaPeriodoTurmaDisciplinaVOs.add(matriculaPeriodoTurmaDisciplinaClone);
								
								if (!getFacadeFactory().getHorarioAlunoFacade().realizarVerificaoChoqueHorarioPorMatriculaPeriodoTurmaDisciplinaVOs(matriculaPeriodoVO, horarioAlunoTurnoVOs, matriculaPeriodoTurmaDisciplinaVOs, matriculaPeriodoTurmaDisciplinaVO.getTurma().getCodigo(), Integer.valueOf(turmaPratica), 0, disciplinaVO.getCodigo(), usuarioVO, configuracaoAcademicoVO.getRemoverDisciplinaTurmaPraticaTeoricaComChoqueHorario(), configuracaoAcademicoVO.getValidarChoqueHorarioOutraMatriculaAluno())) {
									matriculaPeriodoTurmaDisciplinaVO.setTurmaTeorica(matriculaPeriodoTurmaDisciplinaClone.getTurmaTeorica());
									getFacadeFactory().getTurmaFacade().carregarDados(matriculaPeriodoTurmaDisciplinaVO.getTurmaPratica(), NivelMontarDados.BASICO, usuarioVO);
									getFacadeFactory().getTurmaFacade().carregarDados(matriculaPeriodoTurmaDisciplinaVO.getTurmaTeorica(), NivelMontarDados.BASICO, usuarioVO);
									Uteis.liberarListaMemoria(matriculaPeriodoTurmaDisciplinaVOs);
									matriculaPeriodoTurmaDisciplinaClone = null;
									return;
								}
								Uteis.liberarListaMemoria(matriculaPeriodoTurmaDisciplinaVOs);
								matriculaPeriodoTurmaDisciplinaClone = null;
							}
						} catch (ConsistirException e) {
							if (e.getReferenteChoqueHorario()) {
								msgChoqueHorario.adicionarListaMensagemErro(e.getMessage());
							} else {
								throw e;
							}
						}
					}
				}else{
					try {
						if (!getFacadeFactory().getHorarioAlunoFacade().realizarVerificaoChoqueHorarioPorMatriculaPeriodoTurmaDisciplinaVOs(matriculaPeriodoVO, horarioAlunoTurnoVOs, matriculaPeriodoTurmaDisciplinaVOs, matriculaPeriodoTurmaDisciplinaVO.getTurma().getCodigo(), Integer.valueOf(turmaPratica), 0, disciplinaVO.getCodigo(), usuarioVO, configuracaoAcademicoVO.getRemoverDisciplinaTurmaPraticaTeoricaComChoqueHorario(), configuracaoAcademicoVO.getValidarChoqueHorarioOutraMatriculaAluno())) {
							Uteis.liberarListaMemoria(matriculaPeriodoTurmaDisciplinaVOs);
							getFacadeFactory().getTurmaFacade().carregarDados(matriculaPeriodoTurmaDisciplinaVO.getTurmaPratica(), NivelMontarDados.BASICO, usuarioVO);
							return;
						}
					} catch (ConsistirException e) {
						if (e.getReferenteChoqueHorario()) {
							msgChoqueHorario.adicionarListaMensagemErro(e.getMessage());
						} else {
							throw e;
						}
					}
				}
			}
		}
		if(turmasPraticas == null && turmasTeoricas != null){			
			for (String turmaTeorica : turmasTeoricas) {
				try {
					matriculaPeriodoTurmaDisciplinaVO.getTurmaTeorica().setCodigo(Integer.valueOf(turmaTeorica));
					if (!getFacadeFactory().getHorarioAlunoFacade().realizarVerificaoChoqueHorarioPorMatriculaPeriodoTurmaDisciplinaVOs(matriculaPeriodoVO, horarioAlunoTurnoVOs, matriculaPeriodoTurmaDisciplinaVOs, matriculaPeriodoTurmaDisciplinaVO.getTurma().getCodigo(), 0, Integer.valueOf(turmaTeorica), disciplinaVO.getCodigo(), usuarioVO, configuracaoAcademicoVO.getRemoverDisciplinaTurmaPraticaTeoricaComChoqueHorario(), configuracaoAcademicoVO.getValidarChoqueHorarioOutraMatriculaAluno())) {
						Uteis.liberarListaMemoria(matriculaPeriodoTurmaDisciplinaVOs);
						getFacadeFactory().getTurmaFacade().carregarDados(matriculaPeriodoTurmaDisciplinaVO.getTurmaTeorica(), NivelMontarDados.BASICO, usuarioVO);
						return;
					}
				} catch (ConsistirException e) {
					if (e.getReferenteChoqueHorario()) {
						msgChoqueHorario.adicionarListaMensagemErro(e.getMessage());
					} else {
						throw e;
					}
				}
			}
		}
		Uteis.liberarListaMemoria(matriculaPeriodoTurmaDisciplinaVOs);
		matriculaPeriodoTurmaDisciplinaVO.setSugestaoTurmaPraticaTeoricaRealizada(true);
		matriculaPeriodoTurmaDisciplinaVO.setTurmaPratica(null);
		matriculaPeriodoTurmaDisciplinaVO.setTurmaTeorica(null);
		if((turmasPraticas != null || turmasTeoricas != null) && msgChoqueHorario.getListaMensagemErro().size() > 0){
			msgChoqueHorario.getListaMensagemErro().add(0, "Choque de horário ao adicionar disciplina "+disciplinaVO.getNome().toUpperCase()+" após realizar a distribuição de subturma prática/teórica:");
			throw msgChoqueHorario;
		}
		
	} 
	
	
	
	/**
     * Monta a lista de Grade Disciplina Composta com base na matricula periodo
     * turma disciplina onde, se a matriculaperiodoturmadisciplina já estiver
     * gravada então irá montar apenas as GradeDisciplinaCompostaVO na qual
     * exista uma matriculaperiodoturmadisciplina que referencia a grade de
     * disciplina composta se a matriculaperiodoturmadisciplina não estiver
     * gravada então irá montar apenas as GradeDisciplinaCompostaVO com base na
     * origem da mesma (GradeDisciplinaVO ou GrupoOptativas)
     */
    @Override
    public List<MatriculaPeriodoTurmaDisciplinaVO> realizarObtencaoMatriculaPeriodoTurmaDisciplinaFazParteComposicao(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, MatriculaPeriodoVO matriculaPeriodoVO) throws Exception {
        List<MatriculaPeriodoTurmaDisciplinaVO> gradeDisciplinaCompostaVOs = new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>(0);
        if (matriculaPeriodoTurmaDisciplinaVO.getDisciplinaComposta()) {
            if (matriculaPeriodoTurmaDisciplinaVO.getDisciplinaReferenteAUmGrupoOptativa() && matriculaPeriodoTurmaDisciplinaVO.getGradeCurricularGrupoOptativaDisciplinaVO().getGradeDisciplinaCompostaVOs().isEmpty()) {
                GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO = getFacadeFactory().getGradeCurricularGrupoOptativaDisciplinaFacade().consultarPorChavePrimaria(matriculaPeriodoTurmaDisciplinaVO.getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo(), null);
                matriculaPeriodoTurmaDisciplinaVO.setGradeCurricularGrupoOptativaDisciplinaVO(gradeCurricularGrupoOptativaDisciplinaVO);
            } else if (matriculaPeriodoTurmaDisciplinaVO.getGradeDisciplinaVO().getCodigo() > 0 && matriculaPeriodoTurmaDisciplinaVO.getGradeDisciplinaVO().getGradeDisciplinaCompostaVOs().isEmpty()) {
                GradeDisciplinaVO gradeDisciplinaVO = getFacadeFactory().getGradeDisciplinaFacade().consultarPorChavePrimaria(matriculaPeriodoTurmaDisciplinaVO.getGradeDisciplinaVO().getCodigo(), null);
                matriculaPeriodoTurmaDisciplinaVO.setGradeDisciplinaVO(gradeDisciplinaVO);
            }
            List<GradeDisciplinaCompostaVO> compostas = new ArrayList<GradeDisciplinaCompostaVO>(0);
//			List<TurmaDisciplinaCompostaVO> turmaDisciplinaCompostaVOs = getFacadeFactory().getTurmaDisciplinaCompostaFacade().consultarPorTurma(matriculaPeriodoTurmaDisciplinaVO.getTurma().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, false, null);
//			boolean estudarQuantidadeMaximaComposta = TipoControleComposicaoEnum.ESTUDAR_QUANTIDADE_MAXIMA_COMPOSTA.equals(matriculaPeriodoTurmaDisciplinaVO.getGradeDisciplinaVO().getTipoControleComposicao());
//			if (Uteis.isAtributoPreenchido(turmaDisciplinaCompostaVOs) && estudarQuantidadeMaximaComposta) {
//				for (TurmaDisciplinaCompostaVO tdc : turmaDisciplinaCompostaVOs) {
//					compostas.add(tdc.getGradeDisciplinaCompostaVO());
//				}
//			} else {
				compostas = matriculaPeriodoTurmaDisciplinaVO.getDisciplinaReferenteAUmGrupoOptativa() ? matriculaPeriodoTurmaDisciplinaVO.getGradeCurricularGrupoOptativaDisciplinaVO().getGradeDisciplinaCompostaVOs() : matriculaPeriodoTurmaDisciplinaVO.getGradeDisciplinaVO().getGradeDisciplinaCompostaVOs();
//			}
            Boolean existeComposicao = false;
			for (GradeDisciplinaCompostaVO disciplinaCompostaVO : compostas) {
				disciplinaCompostaVO.setGradeDisciplina(matriculaPeriodoTurmaDisciplinaVO.getGradeDisciplinaVO());
				existeComposicao = false;
				for (MatriculaPeriodoTurmaDisciplinaVO mptdVO : matriculaPeriodoVO.getMatriculaPeriodoTumaDisciplinaVOs()) {
					if (mptdVO.getGradeDisciplinaCompostaVO().getCodigo().equals(disciplinaCompostaVO.getCodigo())) {
//						atualizarNrAlunosMatriculadosTurmaDisciplina(mptdVO, mptdVO.getAno(), mptdVO.getSemestre(), true);
						gradeDisciplinaCompostaVOs.add(mptdVO);
						existeComposicao = true;
						break;
					}
				}
				if (!existeComposicao) {
					MatriculaPeriodoTurmaDisciplinaVO mptdVO = new MatriculaPeriodoTurmaDisciplinaVO();
					mptdVO.setNovoObj(true);
					mptdVO.setMatriculaPeriodo(matriculaPeriodoVO.getCodigo());
					mptdVO.setGradeDisciplinaCompostaVO(disciplinaCompostaVO);
					mptdVO.setDisciplinaFazParteComposicao(true);
					mptdVO.setDisciplina(disciplinaCompostaVO.getDisciplina());
					mptdVO.setAno(matriculaPeriodoVO.getAno());
					mptdVO.setSemestre(matriculaPeriodoVO.getSemestre());
					gradeDisciplinaCompostaVOs.add(mptdVO);
				}
			}         
        }
        return gradeDisciplinaCompostaVOs;
    }
    
    
    public static StringBuilder getSqlFiltroBaseGradeCurricularEspecifico(String whereAnd, Integer gradeCurricular){
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" ").append(whereAnd);
		sqlStr.append(" ((").append(gradeCurricular).append(" = historico.matrizcurricular");
		sqlStr.append(" and (historico.historicocursandoporcorrespondenciaapostransferencia is null or");
		sqlStr.append(" historico.historicocursandoporcorrespondenciaapostransferencia = false)");
		sqlStr.append(" and (historico.transferenciamatrizcurricularmatricula IS NULL OR (historico.transferenciamatrizcurricularmatricula IS NOT NULL ");
		sqlStr.append(" and historico.disciplina not in (select disciplina from historico his");
		sqlStr.append(" where his.matricula = historico.matricula");
		sqlStr.append(" and his.anohistorico = historico.anohistorico");
		sqlStr.append(" and his.semestrehistorico = historico.semestrehistorico");
		sqlStr.append(" and his.disciplina = historico.disciplina");
		sqlStr.append(" and his.historicocursandoporcorrespondenciaapostransferencia");
		sqlStr.append(" and his.transferenciamatrizcurricularmatricula = historico.transferenciamatrizcurricularmatricula");
		sqlStr.append(" and his.matrizcurricular != ").append(gradeCurricular).append(" limit 1");
		sqlStr.append(" )))) ");
		
		sqlStr.append(" or (").append(gradeCurricular).append(" != historico.matrizcurricular");
		sqlStr.append(" and historico.historicocursandoporcorrespondenciaapostransferencia ");
		sqlStr.append(" and historico.transferenciamatrizcurricularmatricula IS NOT NULL ");
		sqlStr.append(" and historico.disciplina = (select disciplina from historico his");
		sqlStr.append(" where his.matricula = historico.matricula ");
		sqlStr.append(" and his.anohistorico = historico.anohistorico");
		sqlStr.append(" and his.semestrehistorico = historico.semestrehistorico");
		sqlStr.append(" and his.disciplina = historico.disciplina");
		sqlStr.append(" and his.transferenciamatrizcurricularmatricula = historico.transferenciamatrizcurricularmatricula");
		sqlStr.append(" and (his.historicocursandoporcorrespondenciaapostransferencia is null or ");
		sqlStr.append(" his.historicocursandoporcorrespondenciaapostransferencia = false)");
		sqlStr.append(" and his.matrizcurricular = ").append(gradeCurricular).append(" limit 1");
		sqlStr.append("  ) and historico.historicoporequivalencia = false) ");
		sqlStr.append(" OR ( ");
		sqlStr.append(" ").append(gradeCurricular).append(" != historico.matrizcurricular ");
		sqlStr.append(" AND historico.historicoequivalente =  true                 ");
		sqlStr.append(" AND exists ( ");
		sqlStr.append(" select hist.codigo from historico as hist ");
		sqlStr.append(" WHERE historico.matricula = hist.matricula ");
		sqlStr.append(" and historico.mapaequivalenciadisciplina = hist.mapaequivalenciadisciplina ");
		sqlStr.append(" and hist.historicoporequivalencia = true ");
		sqlStr.append(" and hist.numeroagrupamentoequivalenciadisciplina = historico.numeroagrupamentoequivalenciadisciplina ");
		sqlStr.append(" and exists ( ");
		sqlStr.append(" SELECT his.disciplina FROM historico his ");
		sqlStr.append(" WHERE his.matricula = hist.matricula ");
		sqlStr.append(" AND his.anohistorico = hist.anohistorico ");
		sqlStr.append(" AND his.semestrehistorico = hist.semestrehistorico ");
		sqlStr.append(" AND his.disciplina = hist.disciplina ");
		sqlStr.append(" AND his.transferenciamatrizcurricularmatricula = hist.transferenciamatrizcurricularmatricula ");
		sqlStr.append(" AND (his.historicocursandoporcorrespondenciaapostransferencia IS NULL OR his.historicocursandoporcorrespondenciaapostransferencia = FALSE ) ");
		sqlStr.append(" AND his.matrizcurricular = ").append(gradeCurricular).append(" ");					
		sqlStr.append(" LIMIT 1)) ");
		sqlStr.append(" ) ");
		// Essa condição OR é responsável por trazer as disciplinas que fazem parte da composição após realizar transferencia 
		// de matriz curricular. Isso porque após a transferência o aluno irá cursar a disciplina na grade antiga mesmo estando 
		// já realizada a transferência para nova grade. O sistema então irá trazer o histórico da matriz antiga caso não possua a mesma disciplina na nova grade.
		sqlStr.append(" or (historico.matrizcurricular = matriculaPeriodo.gradecurricular ");
		sqlStr.append(" and ").append(gradeCurricular).append(" != historico.matrizcurricular ");
		sqlStr.append(" and historico.historicodisciplinafazpartecomposicao ");
		sqlStr.append(" and historico.disciplina not in (");
		sqlStr.append(" select his.disciplina from historico his ");
		sqlStr.append(" where his.matriculaperiodo = historico.matriculaperiodo ");
		sqlStr.append(" and his.disciplina = historico.disciplina ");
		sqlStr.append(" and ").append(gradeCurricular).append(" = his.matrizcurricular))	");
		sqlStr.append(") ");
		return sqlStr;
	}
    
    @Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public Boolean consultarMatriculaPeriodoTurmaDisciplinaExistePorConteudoPorDisciplinaDiferente(Integer disciplina, Integer conteudo) throws Exception {
		StringBuilder sb = new StringBuilder("");
		sb.append(" select  ");
		sb.append(" codigo  ");
		sb.append(" from matriculaperiodoturmadisciplina ");
		sb.append(" WHERE conteudo = ").append(conteudo);
		sb.append(" AND disciplina != ").append(disciplina);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		Integer existeGestao = null;
		if(tabelaResultado.next()){
			existeGestao = tabelaResultado.getInt("codigo");	
		}
		return existeGestao != null && existeGestao > 0 ? true:false;
	}
    
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public Boolean consultarMatriculaPeriodoTurmaDisciplinaExistePorConteudo(Integer conteudo) throws Exception {
    	StringBuilder sb = new StringBuilder("");
    	sb.append(" select  ");
    	sb.append(" codigo  ");
    	sb.append(" from matriculaperiodoturmadisciplina ");
    	sb.append(" WHERE conteudo = ").append(conteudo);
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
    	Integer existeGestao = null;
    	if(tabelaResultado.next()){
    		existeGestao = tabelaResultado.getInt("codigo");	
    	}
    	return existeGestao != null && existeGestao > 0 ? true:false;
    }
    
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public List<MatriculaPeriodoTurmaDisciplinaVO> consultarRapidaPorAtividadeDiscursivas(AtividadeDiscursivaVO atividade, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" select distinct matriculaPeriodoTurmaDisciplina.codigo, matriculaPeriodoTurmaDisciplina.matricula,  ");
		sql.append(" pessoa.nome  AS \"pessoa.nome\" , pessoa.codigo AS \"pessoa.codigo\", pessoa.email AS \"pessoa.email\" ");
		sql.append(" from atividadediscursiva ");
		sql.append(" inner join turma on turma.codigo = atividadediscursiva.turma");
		sql.append(" inner join matriculaperiodoturmadisciplina on ");
		sql.append(" ((atividadediscursiva.publicoAlvo = '").append(PublicoAlvoAtividadeDiscursivaEnum.TURMA.name()).append("' and ((turma.turmaAgrupada = false and  atividadediscursiva.turma  = matriculaperiodoturmadisciplina.turma) or (turma.turmaAgrupada = true and atividadediscursiva.turma in (select turmaorigem from turmaAgrupada where turma =  matriculaperiodoturmadisciplina.turma)))) ");
		sql.append(" or (atividadediscursiva.publicoAlvo = '").append(PublicoAlvoAtividadeDiscursivaEnum.ALUNO.name()).append("'  and atividadediscursiva.matriculaPeriodoTurmaDisciplina = matriculaperiodoturmadisciplina.codigo)) ");
		sql.append(" inner join matriculaperiodo on matriculaperiodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo and matriculaperiodo.situacaomatriculaperiodo = 'AT' ");
		sql.append(" inner join matricula on matricula.matricula = matriculaperiodoturmadisciplina.matricula");
		sql.append(" inner join pessoa on pessoa.codigo = matricula.aluno");
		sql.append(" where atividadediscursiva.disciplina = matriculaperiodoturmadisciplina.disciplina ");
		sql.append(" and atividadediscursiva.ano = matriculaperiodoturmadisciplina.ano ");
		sql.append(" and atividadediscursiva.semestre = matriculaperiodoturmadisciplina.semestre ");
		sql.append(" and atividadediscursiva.codigo = ").append(atividade.getCodigo());
		sql.append(" ORDER BY Pessoa.nome ");
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<MatriculaPeriodoTurmaDisciplinaVO> vetResultado = new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>(0);
		while (dadosSQL.next()) {
			MatriculaPeriodoTurmaDisciplinaVO obj = new MatriculaPeriodoTurmaDisciplinaVO();
			obj.setCodigo(dadosSQL.getInt("codigo"));
			obj.setMatricula(dadosSQL.getString("matricula"));
			obj.getMatriculaObjetoVO().setMatricula(dadosSQL.getString("matricula"));
			obj.getMatriculaObjetoVO().getAluno().setCodigo(dadosSQL.getInt("pessoa.codigo"));
			obj.getMatriculaObjetoVO().getAluno().setNome(dadosSQL.getString("pessoa.nome"));
			obj.getMatriculaObjetoVO().getAluno().setEmail(dadosSQL.getString("pessoa.email"));
			obj.setNovoObj(Boolean.FALSE);
			vetResultado.add(obj);
		}
		return vetResultado;
    }

	public List<MatriculaPeriodoTurmaDisciplinaVO> consultaRapidaPorMatriculaPeriodoDisciplinasCursandoPorCorrespodencia(Integer matriculaPeriodo, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sql = getSQLPadraoConsultaBasica();
		sql.append(" INNER JOIN historico ON historico.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo ");
		sql.append(" WHERE (matriculaPeriodoTurmaDisciplina.matriculaPeriodo = ").append(matriculaPeriodo).append(")");
		sql.append(" AND (matriculaPeriodoTurmaDisciplina.disciplinaCursandoPorCorrespondenciaAposTransferencia = true)");
        sql.append(" AND (historico.situacao = 'CS') "); 
		sql.append(" ORDER BY disciplina.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	@Override
	public List<MatriculaPeriodoTurmaDisciplinaVO> consultaRapidaPorMatriculaPeriodoDisciplinasCursandoPorEquivalencia(Integer matriculaPeriodo, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sql = getSQLPadraoConsultaBasica();
		sql.append(" INNER JOIN historico ON historico.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo ");
		sql.append(" WHERE (matriculaPeriodoTurmaDisciplina.matriculaPeriodo = ").append(matriculaPeriodo).append(")");
		sql.append(" AND (matriculaPeriodoTurmaDisciplina.disciplinaEquivale = true)");
        sql.append(" AND (historico.situacao = 'CS') "); 
		sql.append(" ORDER BY disciplina.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public MatriculaPeriodoTurmaDisciplinaVO consultarPorMatriculaMatriculaPeriodoDisciplinaCargaHoraria(String matricula, Integer matriculaPeriodo, Integer disciplina, Integer cargaHoraria, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("select matriculaperiodoturmadisciplina.codigo from matriculaperiodoturmadisciplina ");
		sb.append(" left join gradedisciplina on gradedisciplina.codigo = matriculaperiodoturmadisciplina.gradedisciplina ");
		sb.append(" left join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.codigo = matriculaperiodoturmadisciplina.gradecurriculargrupooptativadisciplina ");
		sb.append(" where matricula = '").append(matricula).append("' ").append(" and matriculaperiodo = ").append(matriculaPeriodo);
		sb.append(" and matriculaperiodoturmadisciplina.disciplina = ").append(disciplina);
		sb.append(" and case when matriculaperiodoturmadisciplina.gradedisciplina is not null then gradedisciplina.cargahoraria = ").append(cargaHoraria);
		sb.append(" else gradecurriculargrupooptativadisciplina.cargahoraria = ").append(cargaHoraria);
		sb.append(" end ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		MatriculaPeriodoTurmaDisciplinaVO obj = new MatriculaPeriodoTurmaDisciplinaVO();
		if (tabelaResultado.next()) {
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			carregarDados(obj, NivelMontarDados.TODOS, usuarioVO);
		}
		return obj;
	}
	
	@Override
	public List<MatriculaPeriodoTurmaDisciplinaVO> consultarRapidaPorMatriculaTurmaDisciplinaAnoSemestre(String matricula, Integer turma, Integer disciplina, String ano, String semestre, Boolean gradeCurricularAtual, Boolean trazerDisciplinaComposta, Boolean trazerDisciplinaFazemParteComposicao, Boolean trazerDisciplinaForaGrade, Boolean trazerDisciplinaEquivalente, Boolean trazerDisciplinaPorEquivalenvia, String situacoesMatriculaPeriodo, String situacoesMatricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		MatriculaPeriodoTurmaDisciplina.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = getSqlConsultarRapidaTrazendoMatricula();
		sqlStr.append(" inner join historico on historico.matriculaPeriodoTurmaDisciplina  =  matriculaPeriodoTurmaDisciplina.codigo ");
		sqlStr.append(" where ");
		sqlStr.append("  ((curso.periodicidade = 'IN') or ");
		sqlStr.append(" (curso.periodicidade = 'AN' and matriculaperiodoturmadisciplina.ano = '"+ano+"') or");
		sqlStr.append(" (curso.periodicidade = 'SE' and matriculaperiodoturmadisciplina.ano = '"+ano+"' and matriculaperiodoturmadisciplina.semestre = '"+semestre+"') ");
		sqlStr.append(" )");
		if(Uteis.isAtributoPreenchido(turma)){
			sqlStr.append(" and  matriculaperiodoturmadisciplina.turma = ").append(turma);
		}
		if(Uteis.isAtributoPreenchido(disciplina)){
			sqlStr.append(" and  matriculaperiodoturmadisciplina.disciplina = ").append(disciplina);
		}
		
		if(matricula != null && !matricula.trim().isEmpty()){
			sqlStr.append(" and matriculaperiodoturmadisciplina.matricula =  '").append(matricula).append("' ");
		}
		/**
		 * Adicionada regra para resolver impactos relacionados a alunos que
		 * estão Cursando por Correspondência e que disciplinas saiam duplicadas
		 * no Boletim Acadêmico
		 */
		if (gradeCurricularAtual) {
			sqlStr.append(getSqlFiltroBaseGradeCurricularAtual(" and"));
		}
		if(!trazerDisciplinaComposta){
			sqlStr.append(" and (historico.historicoDisciplinaComposta is null or historico.historicoDisciplinaComposta = false)");
		}
		if(!trazerDisciplinaFazemParteComposicao){
			sqlStr.append(" and (historico.historicoDisciplinaFazParteComposicao is null or historico.historicoDisciplinaFazParteComposicao = false)");
		}
		if(!trazerDisciplinaForaGrade){
			sqlStr.append(" and (historico.historicoDisciplinaForaGrade is null or historico.historicoDisciplinaForaGrade = false)");
		}
		if(!trazerDisciplinaEquivalente){
			sqlStr.append(" and (historico.historicoequivalente is null or historico.historicoequivalente = false)");
		}
		if(!trazerDisciplinaPorEquivalenvia){
			sqlStr.append(" and (historico.historicoporequivalencia is null or historico.historicoporequivalencia = false)");
		}
		if(Uteis.isAtributoPreenchido(situacoesMatricula)){
			sqlStr.append(" and (matricula.situacao not in ("+situacoesMatricula+"))");
		}
		if(Uteis.isAtributoPreenchido(situacoesMatriculaPeriodo)){
			sqlStr.append(" and (matriculaperiodo.situacaomatriculaperiodo not in ("+situacoesMatriculaPeriodo+"))");
		}
		sqlStr.append(" order by pessoa.nome ");
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<MatriculaPeriodoTurmaDisciplinaVO> objs = new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>(0);		
		while(dadosSQL.next()){
			objs.add(montarDadosBasicoTrazendoMatricula(dadosSQL));			
		}
		return objs;
		
	}
	
	
	@Override
	public MatriculaPeriodoTurmaDisciplinaVO realizarObtencaoDisciplinaCompostaComBaseDisciplinaFilhaComposicao(MatriculaPeriodoVO matriculaPeriodoVO, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplina){
		for(MatriculaPeriodoTurmaDisciplinaVO mp: matriculaPeriodoVO.getMatriculaPeriodoTumaDisciplinaVOs()){
			if(mp.getDisciplinaComposta() 
					&& ((Uteis.isAtributoPreenchido(mp.getGradeDisciplinaVO().getCodigo()) 
					&& Uteis.isAtributoPreenchido(matriculaPeriodoTurmaDisciplina.getGradeDisciplinaCompostaVO().getGradeDisciplina().getCodigo()) 
					&& mp.getGradeDisciplinaVO().getCodigo().equals(matriculaPeriodoTurmaDisciplina.getGradeDisciplinaCompostaVO().getGradeDisciplina().getCodigo()))
					|| (Uteis.isAtributoPreenchido(mp.getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo()) 
						&& Uteis.isAtributoPreenchido(matriculaPeriodoTurmaDisciplina.getGradeDisciplinaCompostaVO().getGradeCurricularGrupoOptativaDisciplina().getCodigo()) 
						&& mp.getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo().equals(matriculaPeriodoTurmaDisciplina.getGradeDisciplinaCompostaVO().getGradeCurricularGrupoOptativaDisciplina().getCodigo()))
					)){							
				return mp;
			}
		}
		return null;
	}
	
	
	
	@Override
	public List<MatriculaPeriodoTurmaDisciplinaVO> consultarPorMatriculaPeriodoTurmaDisciplinaComModalidadeDiferenteTurma(TurmaVO turmaVO, TurmaDisciplinaEstatisticaAlunoVO turmaDisciplinaEstatisticaAlunoVO, UsuarioVO usuario) throws Exception {		
		StringBuilder sqlStr = new StringBuilder("SELECT distinct MatriculaPeriodoTurmaDisciplina.codigo, MatriculaPeriodoTurmaDisciplina.ano, MatriculaPeriodoTurmaDisciplina.semestre, MatriculaPeriodoTurmaDisciplina.matricula, pessoa.nome as aluno FROM MatriculaPeriodoTurmaDisciplina ");		
		sqlStr.append("INNER JOIN Matricula ON (MatriculaPeriodoTurmaDisciplina.matricula = Matricula.matricula) ");
		sqlStr.append("INNER JOIN pessoa ON (pessoa.codigo = Matricula.aluno) ");
		sqlStr.append("INNER JOIN matriculaPeriodo on matriculaPeriodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo ");
		sqlStr.append("INNER JOIN historico ON (MatriculaPeriodoTurmaDisciplina.codigo = historico.MatriculaPeriodoTurmaDisciplina) ");
		if (turmaVO.getSubturma()) {
			if (turmaVO.getTipoSubTurma().equals(TipoSubTurmaEnum.TEORICA)) {
				sqlStr.append("INNER JOIN Turma ON turma.codigo = MatriculaPeriodoTurmaDisciplina.turmaTeorica ");
			} else if (turmaVO.getTipoSubTurma().equals(TipoSubTurmaEnum.PRATICA)) {
				sqlStr.append("INNER JOIN Turma ON turma.codigo = MatriculaPeriodoTurmaDisciplina.turmaPratica ");
			} else {
				sqlStr.append("INNER JOIN Turma ON MatriculaPeriodoTurmaDisciplina.turma = turma.codigo ");
			}
		} else {
			sqlStr.append("INNER JOIN Turma ON MatriculaPeriodoTurmaDisciplina.turma = turma.codigo ");
		}
		sqlStr.append(" WHERE ");
		if (turmaVO.getTurmaAgrupada() && !turmaVO.getSubturma()) {
			sqlStr.append(" Turma.codigo  in ( select turma from turmaAgrupada where turmaOrigem =  " + turmaVO.getCodigo() + ")");
		} else {
			sqlStr.append(" Turma.codigo  = " + turmaVO.getCodigo());
		}
		if(turmaVO.getAnual() || turmaVO.getSemestral()){
			sqlStr.append(" and MatriculaPeriodoTurmaDisciplina.ano = '").append(turmaDisciplinaEstatisticaAlunoVO.getAno()).append("' ");
		}
		if(turmaVO.getSemestral()){
			sqlStr.append(" and MatriculaPeriodoTurmaDisciplina.semestre = '").append(turmaDisciplinaEstatisticaAlunoVO.getSemestre()).append("' ");
		}
		sqlStr.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));
		if(turmaDisciplinaEstatisticaAlunoVO.getTipoEstatisticaTurmaDisciplinaEnum().equals(TipoEstatisticaTurmaDisciplinaEnum.MODALIDADE)) {
			sqlStr.append(" and MatriculaPeriodoTurmaDisciplina.modalidadedisciplina <> '").append(turmaDisciplinaEstatisticaAlunoVO.getTurmaDisciplinaVO().getModalidadeDisciplina().name()).append("' ");
			sqlStr.append(" and MatriculaPeriodoTurmaDisciplina.disciplina = " + turmaDisciplinaEstatisticaAlunoVO.getTurmaDisciplinaVO().getDisciplina().getCodigo().intValue());		
		}else if(turmaDisciplinaEstatisticaAlunoVO.getTipoEstatisticaTurmaDisciplinaEnum().equals(TipoEstatisticaTurmaDisciplinaEnum.CONFIGURACAO_ACADEMICA)) {
			if(Uteis.isAtributoPreenchido(turmaDisciplinaEstatisticaAlunoVO.getTurmaDisciplinaVO().getDisciplina())) {
				sqlStr.append(" and historico.configuracaoacademico != ").append(turmaDisciplinaEstatisticaAlunoVO.getTurmaDisciplinaVO().getConfiguracaoAcademicoVO().getCodigo()).append(" ");
				sqlStr.append(" and MatriculaPeriodoTurmaDisciplina.disciplina = " + turmaDisciplinaEstatisticaAlunoVO.getTurmaDisciplinaVO().getDisciplina().getCodigo().intValue());
			}else {
				sqlStr.append(" and historico.configuracaoacademico != ").append(turmaDisciplinaEstatisticaAlunoVO.getTurmaDisciplinaCompostaVO().getConfiguracaoAcademicoVO().getCodigo()).append(" ");
				sqlStr.append(" and matriculaPeriodoTurmaDisciplina.disciplina = " + turmaDisciplinaEstatisticaAlunoVO.getTurmaDisciplinaCompostaVO().getGradeDisciplinaCompostaVO().getDisciplina().getCodigo().intValue());
			}
		}
		sqlStr.append(" ORDER BY aluno ");

		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs =  new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>(0);
		MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO =  null;
		while(rs.next()){
			matriculaPeriodoTurmaDisciplinaVO = new MatriculaPeriodoTurmaDisciplinaVO();
			matriculaPeriodoTurmaDisciplinaVO.setAno(rs.getString("ano"));
			matriculaPeriodoTurmaDisciplinaVO.setSemestre(rs.getString("semestre"));
			matriculaPeriodoTurmaDisciplinaVO.setCodigo(rs.getInt("codigo"));
			matriculaPeriodoTurmaDisciplinaVO.setMatricula(rs.getString("matricula"));
			matriculaPeriodoTurmaDisciplinaVO.getMatriculaObjetoVO().setMatricula(rs.getString("matricula"));
			matriculaPeriodoTurmaDisciplinaVO.getMatriculaObjetoVO().getAluno().setNome(rs.getString("aluno"));
			matriculaPeriodoTurmaDisciplinaVOs.add(matriculaPeriodoTurmaDisciplinaVO);
		}
		return matriculaPeriodoTurmaDisciplinaVOs;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarModalidadeMatriculaPeriodoTurmaDisciplina(TurmaVO turmaVO, TurmaDisciplinaEstatisticaAlunoVO turmaDisciplinaEstatisticaAlunoVO, UsuarioVO usuario) throws Exception {		
		StringBuilder sqlStr = new StringBuilder("update MatriculaPeriodoTurmaDisciplina set modalidadedisciplina = ?, dataultimaalteracao=now() where codigo in (SELECT distinct MatriculaPeriodoTurmaDisciplina.codigo FROM MatriculaPeriodoTurmaDisciplina ");		
		sqlStr.append("INNER JOIN Matricula ON (MatriculaPeriodoTurmaDisciplina.matricula = Matricula.matricula) ");		
		sqlStr.append("INNER JOIN matriculaPeriodo on matriculaPeriodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo ");
		sqlStr.append("INNER JOIN historico ON (MatriculaPeriodoTurmaDisciplina.codigo = historico.MatriculaPeriodoTurmaDisciplina) ");
		if (turmaVO.getSubturma()) {
			if (turmaVO.getTipoSubTurma().equals(TipoSubTurmaEnum.TEORICA)) {
				sqlStr.append("INNER JOIN Turma ON turma.codigo = MatriculaPeriodoTurmaDisciplina.turmaTeorica ");
			} else if (turmaVO.getTipoSubTurma().equals(TipoSubTurmaEnum.PRATICA)) {
				sqlStr.append("INNER JOIN Turma ON turma.codigo = MatriculaPeriodoTurmaDisciplina.turmaPratica ");
			} else {
				sqlStr.append("INNER JOIN Turma ON MatriculaPeriodoTurmaDisciplina.turma = turma.codigo ");
			}
		} else {
			sqlStr.append("INNER JOIN Turma ON MatriculaPeriodoTurmaDisciplina.turma = turma.codigo ");
		}
		sqlStr.append(" WHERE ");
		if (turmaVO.getTurmaAgrupada() && !turmaVO.getSubturma()) {
			sqlStr.append(" Turma.codigo  in ( select turma from turmaAgrupada where turmaOrigem =  " + turmaVO.getCodigo() + ")");
		} else {
			sqlStr.append(" Turma.codigo  = " + turmaVO.getCodigo());
		}
		if(turmaVO.getAnual() || turmaVO.getSemestral()){
			sqlStr.append(" and MatriculaPeriodoTurmaDisciplina.ano = '").append(turmaDisciplinaEstatisticaAlunoVO.getAno()).append("' ");
		}
		if(turmaVO.getSemestral()){
			sqlStr.append(" and MatriculaPeriodoTurmaDisciplina.semestre = '").append(turmaDisciplinaEstatisticaAlunoVO.getSemestre()).append("' ");
		}
		sqlStr.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));
		sqlStr.append(" and MatriculaPeriodoTurmaDisciplina.modalidadedisciplina <> '").append(turmaDisciplinaEstatisticaAlunoVO.getTurmaDisciplinaVO().getModalidadeDisciplina().name()).append("' ");
		sqlStr.append(" and MatriculaPeriodoTurmaDisciplina.disciplina = " + turmaDisciplinaEstatisticaAlunoVO.getTurmaDisciplinaVO().getDisciplina().getCodigo().intValue());		
		sqlStr.append(" ) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		getConexao().getJdbcTemplate().update(sqlStr.toString(), turmaDisciplinaEstatisticaAlunoVO.getTurmaDisciplinaVO().getModalidadeDisciplina().name());		
	}

	@Override
	public List<MatriculaPeriodoTurmaDisciplinaVO> consultarRapidaDisciplinaFazParteComposicaoPorMatriculaPeriodoGradeCurricularGrupoOptativaDisciplina(Integer matriculaPeriodo, Integer gradeCurricularGrupoOptativaDisciplina, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), verificarAcesso, usuarioVO);
		StringBuffer sqlStr = getSQLPadraoConsultaCompleta();
		sqlStr.append(" WHERE matriculaPeriodo = ").append(matriculaPeriodo);
		sqlStr.append(" AND gradedisciplinacomposta IN (");
		sqlStr.append(" 	SELECT codigo FROM gradedisciplinacomposta WHERE gradeCurricularGrupoOptativaDisciplina = ").append(gradeCurricularGrupoOptativaDisciplina).append(")");
		sqlStr.append(" AND disciplinafazpartecomposicao ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaCompleta(tabelaResultado);
	}
	
	public List<MatriculaPeriodoTurmaDisciplinaVO> consultarMatriculaPeriodoTurmaDisciplinaSemTutor(ProgramacaoTutoriaOnlineVO programacaoTutoriaOnlineVO, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select matriculaperiodoturmadisciplina.codigo as matriculaPeriodoTurmaDisciplina, matriculaperiodoturmadisciplina.matricula as matricula, matriculaperiodo.codigo as matriculaPeriodo, turma.codigo as turma, disciplina.codigo as disciplina");
		sqlStr.append(" from matriculaperiodoturmadisciplina");
		sqlStr.append(" inner join turma on matriculaperiodoturmadisciplina.turma=turma.codigo");
		sqlStr.append(" inner join disciplina on matriculaperiodoturmadisciplina.disciplina= disciplina.codigo");
		sqlStr.append(" inner join matricula on matriculaperiodoturmadisciplina.matricula=matricula.matricula");
		sqlStr.append(" inner join matriculaperiodo on matriculaperiodoturmadisciplina.matriculaperiodo=matriculaperiodo.codigo");
		
		sqlStr.append(" where  matriculaperiodoturmadisciplina.modalidadedisciplina = '").append(ModalidadeDisciplinaEnum.ON_LINE).append("'");
		sqlStr.append(" and  matriculaperiodoturmadisciplina.professor is null");
		
		if(Uteis.isAtributoPreenchido(programacaoTutoriaOnlineVO.getTurmaVO().getCodigo())){
			sqlStr.append(" and matriculaperiodoturmadisciplina.turma = ").append(programacaoTutoriaOnlineVO.getTurmaVO().getCodigo());	
		}
		if(Uteis.isAtributoPreenchido(programacaoTutoriaOnlineVO.getDisciplinaVO().getCodigo())){
			sqlStr.append(" and matriculaperiodoturmadisciplina.disciplina = ").append(programacaoTutoriaOnlineVO.getDisciplinaVO().getCodigo());	
		}
		if(Uteis.isAtributoPreenchido(programacaoTutoriaOnlineVO.getTurmaVO().getCodigo()) && (programacaoTutoriaOnlineVO.getTurmaVO().getAnual() && Uteis.isAtributoPreenchido(programacaoTutoriaOnlineVO.getAno()) || programacaoTutoriaOnlineVO.getTurmaVO().getSemestral() && Uteis.isAtributoPreenchido(programacaoTutoriaOnlineVO.getSemestre()))){	
			sqlStr.append(" and matriculaperiodoturmadisciplina.ano = '").append(programacaoTutoriaOnlineVO.getAno()).append("'");
		}	
		
		if(Uteis.isAtributoPreenchido(programacaoTutoriaOnlineVO.getTurmaVO().getCodigo()) && programacaoTutoriaOnlineVO.getTurmaVO().getSemestral() && Uteis.isAtributoPreenchido(programacaoTutoriaOnlineVO.getSemestre())){	
			sqlStr.append(" and matriculaperiodoturmadisciplina.semestre = '").append(programacaoTutoriaOnlineVO.getSemestre()).append("'");
		}
		sqlStr.append(" and exists (select turmadisciplina.codigo from turmadisciplina where turmadisciplina.turma = turma.codigo and turmadisciplina.definicoesTutoriaOnline = '").append(DefinicoesTutoriaOnlineEnum.DINAMICA.getName()).append("') ");
		sqlStr.append(" order by matriculaperiodoturmadisciplina.codigo ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs =  new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>(0);
		MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO =  null;
		while(rs.next()){
			matriculaPeriodoTurmaDisciplinaVO = new MatriculaPeriodoTurmaDisciplinaVO();
			matriculaPeriodoTurmaDisciplinaVO.setCodigo(rs.getInt("matriculaPeriodoTurmaDisciplina"));
			matriculaPeriodoTurmaDisciplinaVO.getTurma().setCodigo(rs.getInt("turma"));
			matriculaPeriodoTurmaDisciplinaVO.getDisciplina().setCodigo(rs.getInt("disciplina"));
			matriculaPeriodoTurmaDisciplinaVO.setMatricula(rs.getString("matricula"));
			matriculaPeriodoTurmaDisciplinaVO.setMatriculaPeriodo(rs.getInt("matriculaPeriodo"));
			matriculaPeriodoTurmaDisciplinaVOs.add(matriculaPeriodoTurmaDisciplinaVO);
		}
		return matriculaPeriodoTurmaDisciplinaVOs;
	}
	
	public void definirProfessorTutoriaOnlineEGerarCalendarioAtividadeMatriculaAcessoConteudoEstudo(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, UsuarioVO usuarioVO) throws Exception {
		// Não será usado na univesp
//		if (Uteis.isAtributoPreenchido(matriculaPeriodoTurmaDisciplinaVO) && Uteis.isAtributoPreenchido(matriculaPeriodoTurmaDisciplinaVO.getModalidadeDisciplina()) && matriculaPeriodoTurmaDisciplinaVO.getModalidadeDisciplina().equals(ModalidadeDisciplinaEnum.ON_LINE)) {
//			DefinicoesTutoriaOnlineEnum definicoes = null;
//			if(matriculaPeriodoTurmaDisciplinaVO.getTurma().getTurmaDisciplinaVOs().stream().anyMatch(t -> t.getDisciplina().getCodigo().equals(matriculaPeriodoTurmaDisciplinaVO.getDisciplina().getCodigo()))){
//				definicoes = matriculaPeriodoTurmaDisciplinaVO.getTurma().getTurmaDisciplinaVOs().stream().filter(t -> t.getDisciplina().getCodigo().equals(matriculaPeriodoTurmaDisciplinaVO.getDisciplina().getCodigo())).findFirst().get().getDefinicoesTutoriaOnline();
//			} else {
//				definicoes = getFacadeFactory().getTurmaDisciplinaFacade().consultarDefinicoesTutoriaOnlineTurmaDisciplina(matriculaPeriodoTurmaDisciplinaVO.getTurma().getCodigo(),matriculaPeriodoTurmaDisciplinaVO.getDisciplina().getCodigo());
//			}
//			if (definicoes != null && definicoes.isDinamica()) {
//				getFacadeFactory().getProgramacaoTutoriaOnlineInterfaceFacade().realizarDefinicaoProfessorTutoriaOnline(matriculaPeriodoTurmaDisciplinaVO, usuarioVO, false, false);
//
//				ProgramacaoTutoriaOnlineVO programacaoTutoriaOnlineVO = new ProgramacaoTutoriaOnlineVO();
//				programacaoTutoriaOnlineVO = getFacadeFactory().getProgramacaoTutoriaOnlineInterfaceFacade().consultarProgramacaoTutoriaOnlinePorMatriculaPeriodoTurmaDisciplina(matriculaPeriodoTurmaDisciplinaVO, usuarioVO);
//				ConfiguracaoEADVO configuracaoEADVO = getFacadeFactory().getConfiguracaoEADFacade().consultarConfiguracaoEADPorTurma(matriculaPeriodoTurmaDisciplinaVO.getTurma().getCodigo());
//				if(Uteis.isAtributoPreenchido(configuracaoEADVO)) {
//				CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO = new CalendarioAtividadeMatriculaVO();
//
//				getFacadeFactory().getCalendarioAtividadeMatriculaInterfaceFacade().executarInicializacaoDataInicioCalendarioAtividadeMatricula(configuracaoEADVO, calendarioAtividadeMatriculaVO, matriculaPeriodoTurmaDisciplinaVO, programacaoTutoriaOnlineVO, usuarioVO);
//				/*Date dataInicio = getFacadeFactory().getCalendarioAtividadeMatriculaInterfaceFacade().realizarGeracaoDataInicioCalendarioAtividadeMatricula(configuracaoEADVO,calendarioAtividadeMatriculaVO, matriculaPeriodoTurmaDisciplinaVO,programacaoTutoriaOnlineVO, usuarioVO);
//				if (dataInicio != null) {
//					calendarioAtividadeMatriculaVO.setDataInicio(dataInicio);
//				} else {
//					calendarioAtividadeMatriculaVO.setDataInicio(new Date());
//				}*/
//
//				getFacadeFactory().getCalendarioAtividadeMatriculaInterfaceFacade().realizarGeracaoCalendarioAtividadeMatriculaAcessoConteudoEstudo(matriculaPeriodoTurmaDisciplinaVO, calendarioAtividadeMatriculaVO, configuracaoEADVO,usuarioVO, programacaoTutoriaOnlineVO);
//				}
//			}
//		}
	}
			
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void desvincularProgramacaoTutoriaOnlineProfessor(MatriculaPeriodoTurmaDisciplinaVO obj, UsuarioVO usuario) throws Exception {
			final StringBuilder sql = new StringBuilder("UPDATE matriculaperiodoturmadisciplina set programacaotutoriaonlineprofessor = null where matriculaperiodoturmadisciplina.codigo = ?");
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
					sqlAlterar.setInt(1, obj.getCodigo());
					return sqlAlterar;
				}
			});
		}
	
	public static StringBuilder getSqlFiltroBaseGradeCurricularAtual(boolean consultarHistoricoDisciplinaCompostaSemFilhas) {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" (matricula.gradecurricularatual = historico.matrizcurricular");
		sqlStr.append(" and (historico.historicocursandoporcorrespondenciaapostransferencia is null or");
		sqlStr.append(" historico.historicocursandoporcorrespondenciaapostransferencia = false)");
		sqlStr.append(" and (historico.transferenciamatrizcurricularmatricula IS NULL OR (historico.transferenciamatrizcurricularmatricula IS NOT NULL ");
		sqlStr.append(" and historico.disciplina not in (select disciplina from historico his");
		sqlStr.append(" where his.matricula = historico.matricula");
		sqlStr.append(" and his.anohistorico = historico.anohistorico");
		sqlStr.append(" and his.semestrehistorico = historico.semestrehistorico");
		sqlStr.append(" and his.disciplina = historico.disciplina");
		sqlStr.append(" and his.historicocursandoporcorrespondenciaapostransferencia");
		sqlStr.append(" and his.transferenciamatrizcurricularmatricula = historico.transferenciamatrizcurricularmatricula");
		sqlStr.append(" and his.matrizcurricular != matricula.gradecurricularatual limit 1");
		sqlStr.append(" ))) ").append(consultarHistoricoDisciplinaCompostaSemFilhas ? " and (historico.historicodisciplinacomposta is null or historico.historicodisciplinacomposta = false)" : "");
		sqlStr.append(") or (matricula.gradecurricularatual != historico.matrizcurricular");
		sqlStr.append(" and historico.historicocursandoporcorrespondenciaapostransferencia ");
		sqlStr.append(" and historico.transferenciamatrizcurricularmatricula IS NOT NULL ");
		sqlStr.append(" and historico.disciplina IN (select disciplina from historico his");
		sqlStr.append(" where his.matricula = historico.matricula ");
		sqlStr.append(" and his.anohistorico = historico.anohistorico");
		sqlStr.append(" and his.semestrehistorico = historico.semestrehistorico");
		sqlStr.append(" and his.disciplina = historico.disciplina");
		sqlStr.append(" and his.transferenciamatrizcurricularmatricula = historico.transferenciamatrizcurricularmatricula");
		sqlStr.append(" and (his.historicocursandoporcorrespondenciaapostransferencia is null or ");
		sqlStr.append(" his.historicocursandoporcorrespondenciaapostransferencia = false)");
		sqlStr.append(" and his.matrizcurricular = matricula.gradecurricularatual limit 1");
		sqlStr.append("  ) and (historico.historicoporequivalencia = false or historico.historicoporequivalencia is null) ");
		sqlStr.append(consultarHistoricoDisciplinaCompostaSemFilhas ? " and (historico.historicodisciplinacomposta is null or historico.historicodisciplinacomposta = false)" : "");
		sqlStr.append(") OR ( ");
		sqlStr.append(" matricula.gradecurricularatual != historico.matrizcurricular ");
		sqlStr.append(" AND historico.historicoequivalente =  true                 ");
		sqlStr.append(" AND exists ( ");
		sqlStr.append(" select hist.codigo from historico as hist ");
		sqlStr.append(" WHERE historico.matricula = hist.matricula ");
		sqlStr.append(" and historico.mapaequivalenciadisciplina = hist.mapaequivalenciadisciplina ");
		sqlStr.append(" and hist.historicoporequivalencia = true ");
		sqlStr.append(" and hist.numeroagrupamentoequivalenciadisciplina = historico.numeroagrupamentoequivalenciadisciplina ");
		sqlStr.append(" and exists ( ");
		sqlStr.append(" SELECT his.disciplina FROM historico his ");
		sqlStr.append(" WHERE his.matricula = hist.matricula ");
		sqlStr.append(" AND his.anohistorico = hist.anohistorico ");
		sqlStr.append(" AND his.semestrehistorico = hist.semestrehistorico ");
		sqlStr.append(" AND his.disciplina = hist.disciplina ");
		sqlStr.append(" AND his.transferenciamatrizcurricularmatricula = hist.transferenciamatrizcurricularmatricula ");
		sqlStr.append(" AND (his.historicocursandoporcorrespondenciaapostransferencia IS NULL OR his.historicocursandoporcorrespondenciaapostransferencia = FALSE ) ");
		sqlStr.append(" AND his.matrizcurricular = matricula.gradecurricularatual ");					
		sqlStr.append(" LIMIT 1)) ");
		sqlStr.append(consultarHistoricoDisciplinaCompostaSemFilhas ? " and (historico.historicodisciplinacomposta is null or historico.historicodisciplinacomposta = false)" : "");
		sqlStr.append(" ) ");
		// Essa condição OR é responsável por trazer as disciplinas que fazem parte da composição após realizar transferencia 
		// de matriz curricular. Isso porque após a transferência o aluno irá cursar a disciplina na grade antiga mesmo estando 
		// já realizada a transferência para nova grade. O sistema então irá trazer o histórico da matriz antiga caso não possua a mesma disciplina na nova grade.
		sqlStr.append(" or (historico.matrizcurricular = matriculaPeriodo.gradecurricular ");
		sqlStr.append(" and matricula.gradecurricularatual != historico.matrizcurricular ");
		sqlStr.append(" and historico.historicodisciplinafazpartecomposicao ");
		sqlStr.append(" and historico.disciplina not in (");
		sqlStr.append(" select his.disciplina from historico his ");
		sqlStr.append(" where his.matriculaperiodo = historico.matriculaperiodo ");
		sqlStr.append(" and his.disciplina = historico.disciplina ");
		sqlStr.append(" and matricula.gradecurricularatual = his.matrizcurricular)	");
		sqlStr.append(consultarHistoricoDisciplinaCompostaSemFilhas ? " and (historico.historicodisciplinacomposta is null or historico.historicodisciplinacomposta = false)" : "");
		sqlStr.append(") ");
		if (consultarHistoricoDisciplinaCompostaSemFilhas) {
			sqlStr.append(" or ( matricula.gradecurricularatual = historico.matrizcurricular and historico.historicodisciplinacomposta = true ");
			sqlStr.append(" and (historico.historicocursandoporcorrespondenciaapostransferencia is null or historico.historicocursandoporcorrespondenciaapostransferencia = false) ");
			sqlStr.append(" and (historico.historicoporequivalencia is null or historico.historicoporequivalencia = false) and not exists ( ");
			sqlStr.append(" select historico.codigo from historico h inner join gradedisciplinacomposta gdc on gdc.codigo = h.gradedisciplinacomposta ");
			sqlStr.append(" where h.matricula = historico.matricula and h.anohistorico = historico.anohistorico and h.semestrehistorico = historico.semestrehistorico ");
			sqlStr.append(" and h.matrizcurricular = historico.matrizcurricular ");
			sqlStr.append(" and (( gdc.gradedisciplina is not null and historico.gradedisciplina is not null and gdc.gradedisciplina = historico.gradedisciplina) ");
			sqlStr.append(" or (gdc.gradecurriculargrupooptativadisciplina is not null and historico.gradecurriculargrupooptativadisciplina is not null ");
			sqlStr.append(" and gdc.gradecurriculargrupooptativadisciplina = historico.gradecurriculargrupooptativadisciplina)) limit 1 ))");
		}
		return sqlStr;
	}
	
	
	
	@Override
	public List<MatriculaPeriodoTurmaDisciplinaVO> consultarDisciplinaDoAlunoPorMatricula(String matricula, String anoSemestre, PermissaoAcessoMenuVO permissaoAcessoMenuVOs, Integer matriculaPeriodoTurmaDisciplina, Boolean consultarRelatorioFacilitador, int nivelMontarDados, UsuarioVO usuario) throws SQLException, Exception {
		StringBuilder sqlStr = new StringBuilder();		
		sqlStr.append(" SELECT historico.codigo as historico, curso.periodicidade,  matriculaperiodoturmadisciplina.codigo as codigo, matriculaperiodoturmadisciplina.conteudo as conteudo, disciplina.abreviatura as abreviatura, matriculaperiodoturmadisciplina.professor as professor, ");
		sqlStr.append(" disciplina.nome as nomedisciplina, disciplina.codigo as codigodisciplina,   disciplina.classificacaoDisciplina as classificacaoDisciplina, turmadisciplina.ordemestudoonline as ordemestudoonline, matriculaperiodoturmadisciplina.modalidadedisciplina as modalidade, ");
		sqlStr.append(" (matriculaperiodoturmadisciplina.codigo is not null and (configuracaoacademico.utilizarApoioEADParaDisciplinasModalidadePresencial or matriculaperiodoturmadisciplina.modalidadeDisciplina = '").append(ModalidadeDisciplinaEnum.ON_LINE.name()).append("') and ((matriculaperiodo.situacaomatriculaperiodo = 'AT' and (calendarioatividadematricula.codigo is null or  calendarioatividadematricula.dataFim > now()))  or (matriculaperiodo.situacaomatriculaperiodo = 'FI' and calendarioatividadematricula.dataFim > now()))) as permiteAcessoEAD, ");
		sqlStr.append(" case when calendarioatividadematricula.codigo is not null then calendarioatividadematricula.dataInicio else periodoletivoativounidadeensinocurso.datainicioperiodoletivo  end as dataInicio, ");
		sqlStr.append(" case when calendarioatividadematricula.codigo is not null then calendarioatividadematricula.dataFim ");
		sqlStr.append(" else periodoletivoativounidadeensinocurso.datafimperiodoletivo end as dataFim, ");
		sqlStr.append(" matriculaperiodoturmadisciplina.turma, matriculaperiodoturmadisciplina.turmateorica, matriculaperiodoturmadisciplina.turmapratica, historico.anohistorico as ano, historico.semestrehistorico as semestre, ");
		sqlStr.append(" matriculaperiodoturmadisciplina.disciplinaFazParteComposicao, case when gradecurriculargrupooptativadisciplina.codigo is not null then  gradecurriculargrupooptativadisciplina.disciplinaComposta else gradedisciplina.disciplinaComposta end as disciplinaComposta, matricula.matricula, ");
		sqlStr.append(" gradecurriculargrupooptativadisciplina.codigo as gradecurriculargrupooptativadisciplina,  gradedisciplina.codigo as gradedisciplina, historico.totalfalta, historico.freguencia, historico.mediafinal, historico.notafinalconceito,  ");
		sqlStr.append(" historico.mediafinalconceito, historico.historicoDisciplinaFazParteComposicao, historico.historicoDisciplinaComposta, configuracaoacademiconotaconceito.conceitoNota, historico.configuracaoacademico, historico.situacao, matricula.unidadeensino, matriculaperiodoturmadisciplina.professor, ");
		sqlStr.append(" turmabase.identificadorTurma as turma_identificador, turmaPratica.identificadorTurma as turmaPratica_identificador, turmaTeorica.identificadorTurma as turmaTeorica_identificador, ");
		//sqlStr.append(" classroomGoogle.codigo as classroomGoogle_codigo, classroomGoogle.linkClassroom, ");
		sqlStr.append(" salaaulablackboard.codigo as salaaulablackboard_codigo, salaaulablackboard.linkSalaAulaBlackboard, ");
		sqlStr.append(" salaaulablackboard.id as salaaulablackboard_id, salaaulablackboard.idSalaAulaBlackboard as salaaulablackboard_idSalaAulaBlackboard,  ");
		sqlStr.append(" grupoblackboard.codigo as grupoblackboard_codigo, grupoblackboard.linkSalaAulaBlackboard as grupoblackboard_linkSalaAulaBlackboard, ");
		sqlStr.append(" grupoblackboard.id as grupoblackboard_id, grupoblackboard.idSalaAulaBlackboard as grupoblackboard_idSalaAulaBlackboard,  ");
		sqlStr.append(" grupoblackboard.nomegrupo as grupoblackboard_nomegrupo, grupoblackboard.idgrupo as grupoblackboard_idgrupo, matricula.unidadeensino ");
		if(consultarRelatorioFacilitador) {
			sqlStr.append(", CASE WHEN calendariorelatoriofinalfacilitador.codigo IS NOT NULL AND usuariofacilitador.QTDE > 0 THEN TRUE ELSE FALSE END AS exsiteCalendarioRelatorioFacilitador, CASE WHEN relatoriofinalfacilitador.codigo IS NOT NULL AND usuariofacilitador.QTDE > 0 THEN TRUE ELSE FALSE END AS exsiteRelatorioFacilitador ");
		}
		sqlStr.append(" from historico");
		sqlStr.append(" inner join matriculaperiodoturmadisciplina on historico.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo and historico.situacao not in ('TR', 'CA', 'AC', 'TF', 'TI', 'TE') ");
		sqlStr.append(" and matriculaperiodoturmadisciplina.codigo in ( ");
		sqlStr.append("   select mptd.codigo from matriculaperiodoturmadisciplina as mptd where mptd.matricula = matriculaperiodoturmadisciplina.matricula ");
		sqlStr.append("   and mptd.disciplina = matriculaperiodoturmadisciplina.disciplina  and mptd.ano = matriculaperiodoturmadisciplina.ano  and mptd.semestre = matriculaperiodoturmadisciplina.semestre ");
		sqlStr.append("   order by mptd.ano||'/'||mptd.semestre desc, mptd.codigo desc limit 1 ");
		sqlStr.append(" ) ");	
		
		sqlStr.append(" INNER JOIN matricula ON (matricula.matricula = historico.matricula) ");
		sqlStr.append(" INNER JOIN curso ON (matricula.curso = curso.codigo)");
		sqlStr.append(" inner join disciplina on disciplina.codigo = historico.disciplina");
		sqlStr.append(" inner join configuracaoacademico on configuracaoacademico.codigo = historico.configuracaoacademico");
		sqlStr.append(" inner join matriculaperiodo on matriculaperiodo.codigo = historico.matriculaperiodo ");		
		sqlStr.append(" inner join unidadeensinocurso on matriculaperiodo.unidadeensinocurso = unidadeensinocurso.codigo ");
	
		sqlStr.append(" left join gradedisciplinacomposta on historico.gradedisciplinacomposta = gradedisciplinacomposta.codigo ");
		sqlStr.append(" left join gradecurriculargrupooptativadisciplina on ");
		sqlStr.append(" ((gradedisciplinacomposta.codigo is not null and gradedisciplinacomposta.gradecurriculargrupooptativadisciplina = gradecurriculargrupooptativadisciplina.codigo) ");
		sqlStr.append(" or (gradedisciplinacomposta.codigo is null and historico.historicodisciplinacomposta = false and historico.gradecurriculargrupooptativadisciplina = gradecurriculargrupooptativadisciplina.codigo)) ");
		sqlStr.append(" left join gradedisciplina on ((gradedisciplinacomposta.codigo is not null and gradedisciplinacomposta.gradedisciplina = gradedisciplina.codigo) ");
		sqlStr.append(" or (gradedisciplinacomposta.codigo is null and historico.historicodisciplinacomposta = false and historico.gradedisciplina = gradedisciplina.codigo)) ");
		sqlStr.append(" left join turmadisciplina on matriculaperiodoturmadisciplina.codigo is not null and turmadisciplina.turma = matriculaperiodoturmadisciplina.turma and ((gradedisciplina.codigo is not null and turmadisciplina.disciplina = gradedisciplina.disciplina)  or (gradecurriculargrupooptativadisciplina.codigo is not null 		 and turmadisciplina.disciplina = gradecurriculargrupooptativadisciplina.disciplina)) ");
		sqlStr.append(" left join calendarioatividadematricula on calendarioatividadematricula.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo and calendarioatividadematricula.tipocalendarioatividade = '").append(TipoCalendarioAtividadeMatriculaEnum.ACESSO_CONTEUDO_ESTUDO).append("'");
		sqlStr.append(" left join processomatriculacalendario on curso.periodicidade != 'IN' and matriculaperiodo.processomatricula = processomatriculacalendario.processomatricula and unidadeensinocurso.curso = processomatriculacalendario.curso and unidadeensinocurso.turno = processomatriculacalendario.turno ");
		sqlStr.append(" left join periodoletivoativounidadeensinocurso on curso.periodicidade != 'IN' and  periodoletivoativounidadeensinocurso.codigo = processomatriculacalendario.periodoletivoativounidadeensinocurso  ");
		sqlStr.append(" left join turma as turmabase on turmabase.codigo = matriculaperiodoturmadisciplina.turma ");
		sqlStr.append(" left join turma as turmapratica on turmapratica.codigo = matriculaperiodoturmadisciplina.turmapratica ");
		sqlStr.append(" left join turma as turmateorica on turmateorica.codigo = matriculaperiodoturmadisciplina.turmateorica ");		
		sqlStr.append(" left join configuracaoacademiconotaconceito on configuracaoacademiconotaconceito.codigo = historico.mediafinalconceito ");
		sqlStr.append(" left join salaaulablackboard on salaaulablackboard.codigo =  (select salaaulablackboardpessoa.salaaulablackboard from salaaulablackboardpessoa where salaaulablackboardpessoa.matriculaperiodoturmadisciplina =  matriculaperiodoturmadisciplina.codigo  limit 1 ) ");
		sqlStr.append("	left join lateral (");
		sqlStr.append("		select salaaulablackboard.* from salaaulablackboard ");
		sqlStr.append("		inner join salaaulablackboardpessoa on salaaulablackboard.codigo = salaaulablackboardpessoa. salaaulablackboard ");
		sqlStr.append("		where  salaaulablackboardpessoa.matricula = matriculaperiodoturmadisciplina.matricula ");
		sqlStr.append("		and salaaulablackboardpessoa.tiposalaaulablackboardpessoaenum = 'ALUNO' ");
		sqlStr.append("		and salaaulablackboard.tiposalaaulablackboardenum in ('PROJETO_INTEGRADOR_GRUPO') ");
		sqlStr.append("		and salaaulablackboard.disciplina = matriculaperiodoturmadisciplina.disciplina ");
		sqlStr.append(") as grupoblackboard on 1=1 ");
		if(consultarRelatorioFacilitador) {
			sqlStr.append(" LEFT JOIN LATERAL (");
			sqlStr.append(" SELECT count(*) AS QTDE FROM salaaulablackboard");
			sqlStr.append(" INNER JOIN salaaulablackboardpessoa AS facilitador ON facilitador.salaaulablackboard = salaaulablackboard.codigo");
			sqlStr.append(" INNER JOIN pessoaemailinstitucional ef ON ef.codigo = facilitador.pessoaemailinstitucional");
			sqlStr.append(" INNER JOIN pessoa AS f ON f.codigo = ef.pessoa");
			sqlStr.append(" WHERE facilitador.tiposalaaulablackboardpessoaenum = 'FACILITADOR'");
			sqlStr.append(" AND salaaulablackboard.ano = matriculaperiodoturmadisciplina.ano");
			sqlStr.append(" AND salaaulablackboard.semestre = matriculaperiodoturmadisciplina.semestre");
			sqlStr.append(" AND f.codigo = matricula.aluno ");
			sqlStr.append(" ) usuariofacilitador ON TRUE");
			sqlStr.append(" LEFT JOIN LATERAL (");
			sqlStr.append(" SELECT codigo FROM calendariorelatoriofinalfacilitador");
			sqlStr.append(" WHERE matriculaperiodoturmadisciplina.disciplina = calendariorelatoriofinalfacilitador.disciplina");
			sqlStr.append(" AND matriculaperiodoturmadisciplina.ano = calendariorelatoriofinalfacilitador.ano");
			sqlStr.append(" AND matriculaperiodoturmadisciplina.semestre = calendariorelatoriofinalfacilitador.semestre");
			sqlStr.append(" AND now() >= datainicio AND now() <= datafim");
			sqlStr.append(" LIMIT 1");
			sqlStr.append(" ) calendariorelatoriofinalfacilitador ON TRUE");
			sqlStr.append(" LEFT JOIN LATERAL (");
			sqlStr.append(" SELECT codigo FROM Relatoriofinalfacilitador");
			sqlStr.append(" WHERE matriculaperiodoturmadisciplina.codigo = Relatoriofinalfacilitador.matriculaperiodoturmadisciplina");
			sqlStr.append(" AND Relatoriofinalfacilitador.ano = matriculaperiodoturmadisciplina.ano");
			sqlStr.append(" AND Relatoriofinalfacilitador.semestre = matriculaperiodoturmadisciplina.semestre");
			sqlStr.append(" LIMIT 1");
			sqlStr.append(" ) relatoriofinalfacilitador ON TRUE");
		}

		sqlStr.append(" WHERE historico.matricula = ? and (historico.historicoDisciplinaComposta is null or historico.historicoDisciplinaComposta = false) ");
		if(Uteis.isAtributoPreenchido(anoSemestre) && anoSemestre.contains("/")) {
			sqlStr.append(" and historico.anohistorico||'/'||historico.semestrehistorico = '").append(anoSemestre).append("' "); 			
		} if(Uteis.isAtributoPreenchido(anoSemestre) && !anoSemestre.contains("/")) {
			sqlStr.append(" and historico.anohistorico = '").append(anoSemestre).append("' "); 			
		}
		if (Uteis.isAtributoPreenchido(matriculaPeriodoTurmaDisciplina)) {
			sqlStr.append(" AND matriculaperiodoturmadisciplina.codigo = ").append(matriculaPeriodoTurmaDisciplina);
		}
		sqlStr.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));
		
		sqlStr.append(" ORDER BY case when dataInicio > now() then 1 else 0 end, dataInicio,  ordemestudoonline ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), matricula);
		System.out.println(sqlStr.toString());
		ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = null;
		if (tabelaResultado.next() && Uteis.isAtributoPreenchido(tabelaResultado.getInt("unidadeensino"))){
			configuracaoGeralSistemaVO = getAplicacaoControle().getConfiguracaoGeralSistemaVO(tabelaResultado.getInt("unidadeensino"), usuario);
		}
		tabelaResultado.beforeFirst();
		List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs = new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>();
		MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO = null;
		while(tabelaResultado.next()) {
			matriculaPeriodoTurmaDisciplinaVO = new MatriculaPeriodoTurmaDisciplinaVO();
			matriculaPeriodoTurmaDisciplinaVO.setCodigo(tabelaResultado.getInt("codigo"));
			matriculaPeriodoTurmaDisciplinaVO.setPermiteAcessoEAD(tabelaResultado.getBoolean("permiteAcessoEAD"));
			if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
				continue;
			}
			matriculaPeriodoTurmaDisciplinaVO.getDisciplina().setNome(tabelaResultado.getString("nomedisciplina"));
			matriculaPeriodoTurmaDisciplinaVO.getDisciplina().setCodigo(tabelaResultado.getInt("codigodisciplina"));
			matriculaPeriodoTurmaDisciplinaVO.getDisciplina().setClassificacaoDisciplina(ClassificacaoDisciplinaEnum.valueOf(tabelaResultado.getString("classificacaoDisciplina")));
			matriculaPeriodoTurmaDisciplinaVO.getTurma().setCodigo(tabelaResultado.getInt("turma"));
			matriculaPeriodoTurmaDisciplinaVO.getTurma().setIdentificadorTurma(tabelaResultado.getString("turma_identificador"));
			matriculaPeriodoTurmaDisciplinaVO.getTurmaPratica().setCodigo(tabelaResultado.getInt("turmaPratica"));			
			matriculaPeriodoTurmaDisciplinaVO.getTurmaPratica().setIdentificadorTurma(tabelaResultado.getString("turmaPratica_identificador"));
			matriculaPeriodoTurmaDisciplinaVO.getTurmaTeorica().setCodigo(tabelaResultado.getInt("turmaTeorica"));
			matriculaPeriodoTurmaDisciplinaVO.getTurmaTeorica().setIdentificadorTurma(tabelaResultado.getString("turmaTeorica_identificador"));
			matriculaPeriodoTurmaDisciplinaVO.setMatricula(tabelaResultado.getString("matricula"));
			matriculaPeriodoTurmaDisciplinaVO.setAno(tabelaResultado.getString("ano"));
			matriculaPeriodoTurmaDisciplinaVO.getProfessor().setCodigo(tabelaResultado.getInt("professor"));
			matriculaPeriodoTurmaDisciplinaVO.setSemestre(tabelaResultado.getString("semestre"));
			matriculaPeriodoTurmaDisciplinaVO.setDisciplinaFazParteComposicao(tabelaResultado.getBoolean("disciplinaFazParteComposicao"));
			matriculaPeriodoTurmaDisciplinaVO.getGradeDisciplinaVO().setCodigo(tabelaResultado.getInt("gradeDisciplina"));
			matriculaPeriodoTurmaDisciplinaVO.getGradeDisciplinaVO().setDisciplinaComposta(tabelaResultado.getBoolean("disciplinaComposta"));
			matriculaPeriodoTurmaDisciplinaVO.getGradeCurricularGrupoOptativaDisciplinaVO().setCodigo(tabelaResultado.getInt("gradeCurricularGrupoOptativaDisciplina"));
			matriculaPeriodoTurmaDisciplinaVO.getGradeCurricularGrupoOptativaDisciplinaVO().setDisciplinaComposta(tabelaResultado.getBoolean("disciplinaComposta"));
			matriculaPeriodoTurmaDisciplinaVO.setOrdemEstudoOnline(tabelaResultado.getInt("ordemestudoonline"));
			matriculaPeriodoTurmaDisciplinaVO.getConteudo().setCodigo(tabelaResultado.getInt("conteudo"));
			matriculaPeriodoTurmaDisciplinaVO.getDisciplina().setAbreviatura(tabelaResultado.getString("abreviatura"));
			matriculaPeriodoTurmaDisciplinaVO.getProfessor().setCodigo(tabelaResultado.getInt("professor"));
			if(Uteis.isAtributoPreenchido(tabelaResultado.getString("modalidade"))) {
				matriculaPeriodoTurmaDisciplinaVO.setModalidadeDisciplina(ModalidadeDisciplinaEnum.valueOf(tabelaResultado.getString("modalidade")));	
			}
			matriculaPeriodoTurmaDisciplinaVO.getHistoricoVO().setCodigo(tabelaResultado.getInt("historico"));
			if(tabelaResultado.getObject("mediafinal") != null) {
				matriculaPeriodoTurmaDisciplinaVO.getHistoricoVO().setMediaFinal(tabelaResultado.getDouble("mediafinal"));
			}
			matriculaPeriodoTurmaDisciplinaVO.getHistoricoVO().setTotalFalta(tabelaResultado.getInt("totalFalta"));
			matriculaPeriodoTurmaDisciplinaVO.getHistoricoVO().setConfiguracaoAcademico(getAplicacaoControle().carregarDadosConfiguracaoAcademica(tabelaResultado.getInt("configuracaoacademico")));
			matriculaPeriodoTurmaDisciplinaVO.getHistoricoVO().setFreguencia(tabelaResultado.getDouble("freguencia"));
			matriculaPeriodoTurmaDisciplinaVO.getHistoricoVO().setSituacao(tabelaResultado.getString("situacao"));
			matriculaPeriodoTurmaDisciplinaVO.getHistoricoVO().setNotaFinalConceito(tabelaResultado.getString("notafinalconceito"));
			matriculaPeriodoTurmaDisciplinaVO.getHistoricoVO().setHistoricoDisciplinaFazParteComposicao(tabelaResultado.getBoolean("historicoDisciplinaFazParteComposicao"));
			matriculaPeriodoTurmaDisciplinaVO.getHistoricoVO().setHistoricoDisciplinaComposta(tabelaResultado.getBoolean("historicoDisciplinaComposta"));
			matriculaPeriodoTurmaDisciplinaVO.getHistoricoVO().getMediaFinalConceito().setCodigo(tabelaResultado.getInt("mediafinalconceito"));
			matriculaPeriodoTurmaDisciplinaVO.getHistoricoVO().getMediaFinalConceito().setConceitoNota(tabelaResultado.getString("conceitoNota"));
			matriculaPeriodoTurmaDisciplinaVO.getMatriculaObjetoVO().getUnidadeEnsino().setCodigo(tabelaResultado.getInt("unidadeEnsino"));
			matriculaPeriodoTurmaDisciplinaVO.getMatriculaObjetoVO().getCurso().setPeriodicidade(tabelaResultado.getString("periodicidade"));
			matriculaPeriodoTurmaDisciplinaVO.setOcultarDownload(!Uteis.isAtributoPreenchido(matriculaPeriodoTurmaDisciplinaVO));
			if(matriculaPeriodoTurmaDisciplinaVO.getMatriculaObjetoVO().getCurso().getPeriodicidade().equals("IN") && matriculaPeriodoTurmaDisciplinaVO.getModalidadeDisciplina().equals(ModalidadeDisciplinaEnum.PRESENCIAL)) {
				//getFacadeFactory().getHistoricoFacade().carregarDadosHorarioAulaAluno(matriculaPeriodoTurmaDisciplinaVO.getHistoricoVO(), false);
				//matriculaPeriodoTurmaDisciplinaVO.setDataInicioPeriodoEstudo(matriculaPeriodoTurmaDisciplinaVO.getHistoricoVO().getDataPrimeiraAula());
				//matriculaPeriodoTurmaDisciplinaVO.setDataFimPeriodoEstudo(matriculaPeriodoTurmaDisciplinaVO.getHistoricoVO().getData());
			}else {
				matriculaPeriodoTurmaDisciplinaVO.setDataInicioPeriodoEstudo(tabelaResultado.getDate("dataInicio"));
				matriculaPeriodoTurmaDisciplinaVO.setDataFimPeriodoEstudo(tabelaResultado.getDate("dataFim"));
			}
//			if (!Uteis.isAtributoPreenchido(configuracaoGeralSistemaVO) 
//					|| (Uteis.isAtributoPreenchido(configuracaoGeralSistemaVO) && configuracaoGeralSistemaVO.getHabilitarRecursosAcademicosVisaoAluno())) {
//				matriculaPeriodoTurmaDisciplinaVO.getHistoricoVO().setCalendarioLancamentoNotaVO(getFacadeFactory().getCalendarioLancamentoNotaFacade().consultarPorCalendarioAcademicoUtilizar(matriculaPeriodoTurmaDisciplinaVO.getMatriculaObjetoVO().getUnidadeEnsino().getCodigo(), 
//						matriculaPeriodoTurmaDisciplinaVO.getTurma().getCodigo(), false, matriculaPeriodoTurmaDisciplinaVO.getProfessor().getCodigo(), 
//						matriculaPeriodoTurmaDisciplinaVO.getDisciplina().getCodigo(), matriculaPeriodoTurmaDisciplinaVO.getHistoricoVO().getConfiguracaoAcademico().getCodigo(), matriculaPeriodoTurmaDisciplinaVO.getMatriculaObjetoVO().getCurso().getPeriodicidade(), 
//						matriculaPeriodoTurmaDisciplinaVO.getAno(), matriculaPeriodoTurmaDisciplinaVO.getSemestre(), 
//						false, usuario));
//				
//				if(Uteis.isAtributoPreenchido(matriculaPeriodoTurmaDisciplinaVO.getHistoricoVO().getCalendarioLancamentoNotaVO())
//						&& !matriculaPeriodoTurmaDisciplinaVO.getHistoricoVO().getCalendarioLancamentoNotaVO().getApresentarCalculoMediaFinalVisaoAluno()) {
//					matriculaPeriodoTurmaDisciplinaVO.getHistoricoVO().setMediaFinal(null);
//					matriculaPeriodoTurmaDisciplinaVO.getHistoricoVO().setMediaFinalConceito(null);
//					matriculaPeriodoTurmaDisciplinaVO.getHistoricoVO().setMediaFinalTexto("");						
//				}
//			}
			if (matriculaPeriodoTurmaDisciplinaVO.getHistoricoVO().getConfiguracaoAcademico().getOcultarMediaFinalDisciplinaCasoReprovado()) {
				if (matriculaPeriodoTurmaDisciplinaVO.getHistoricoVO().getSituacao().equals(SituacaoHistorico.REPROVADO.getValor()) || matriculaPeriodoTurmaDisciplinaVO.getHistoricoVO().getSituacao().equals(SituacaoHistorico.REPROVADO_FALTA.getValor()) 
						|| matriculaPeriodoTurmaDisciplinaVO.getHistoricoVO().getSituacao().equals(SituacaoHistorico.REPROVADO_PERIODO_LETIVO.getValor())) {
					matriculaPeriodoTurmaDisciplinaVO.getHistoricoVO().setMediaFinal(null);
					matriculaPeriodoTurmaDisciplinaVO.getHistoricoVO().setMediaFinalConceito(null);
					matriculaPeriodoTurmaDisciplinaVO.getHistoricoVO().setMediaFinalTexto("");
				}
			}
			
			if((Uteis.isAtributoPreenchido(matriculaPeriodoTurmaDisciplinaVO.getHistoricoVO().getCalendarioLancamentoNotaVO())
					&& !matriculaPeriodoTurmaDisciplinaVO.getHistoricoVO().getCalendarioLancamentoNotaVO().getApresentarCalculoMediaFinalVisaoAluno()) 
					|| !matriculaPeriodoTurmaDisciplinaVO.getHistoricoVO().getApresentarSituacaoAplicandoRegraConfiguracaoAcademica()) {
				matriculaPeriodoTurmaDisciplinaVO.getHistoricoVO().setSituacao("");
			}
			if(Uteis.isAtributoPreenchido(tabelaResultado.getInt("salaaulablackboard_codigo"))) {
				matriculaPeriodoTurmaDisciplinaVO.getSalaAulaBlackboardVO().setCodigo(tabelaResultado.getInt("salaaulablackboard_codigo"));
				matriculaPeriodoTurmaDisciplinaVO.getSalaAulaBlackboardVO().setId(tabelaResultado.getString("salaaulablackboard_id"));
				matriculaPeriodoTurmaDisciplinaVO.getSalaAulaBlackboardVO().setIdSalaAulaBlackboard(tabelaResultado.getString("salaaulablackboard_idSalaAulaBlackboard"));
				matriculaPeriodoTurmaDisciplinaVO.getSalaAulaBlackboardVO().setLinkSalaAulaBlackboard(tabelaResultado.getString("linkSalaAulaBlackboard"));
			}
			if(Uteis.isAtributoPreenchido(tabelaResultado.getInt("grupoblackboard_codigo"))) {
				matriculaPeriodoTurmaDisciplinaVO.getSalaAulaBlackboardGrupoVO().setCodigo(tabelaResultado.getInt("grupoblackboard_codigo"));
				matriculaPeriodoTurmaDisciplinaVO.getSalaAulaBlackboardGrupoVO().setId(tabelaResultado.getString("grupoblackboard_id"));
				matriculaPeriodoTurmaDisciplinaVO.getSalaAulaBlackboardGrupoVO().setIdSalaAulaBlackboard(tabelaResultado.getString("grupoblackboard_idSalaAulaBlackboard"));
				matriculaPeriodoTurmaDisciplinaVO.getSalaAulaBlackboardGrupoVO().setLinkSalaAulaBlackboard(tabelaResultado.getString("grupoblackboard_linkSalaAulaBlackboard"));
				matriculaPeriodoTurmaDisciplinaVO.getSalaAulaBlackboardGrupoVO().setNomeGrupo(tabelaResultado.getString("grupoblackboard_nomegrupo"));
				matriculaPeriodoTurmaDisciplinaVO.getSalaAulaBlackboardGrupoVO().setIdGrupo(tabelaResultado.getString("grupoblackboard_idgrupo"));
			}
			if(consultarRelatorioFacilitador) {
				matriculaPeriodoTurmaDisciplinaVO.setApresentarRelatoriofinalfacilitador(tabelaResultado.getBoolean("exsiteCalendarioRelatorioFacilitador"));
				if(matriculaPeriodoTurmaDisciplinaVO.getApresentarRelatoriofinalfacilitador()) {
					matriculaPeriodoTurmaDisciplinaVO.setApresentarConsultarRelatoriofinalfacilitador(tabelaResultado.getBoolean("exsiteRelatorioFacilitador"));
				} else {
					matriculaPeriodoTurmaDisciplinaVO.setApresentarConsultarRelatoriofinalfacilitador(false);
				}
			} else {
				matriculaPeriodoTurmaDisciplinaVO.setApresentarRelatoriofinalfacilitador(false);
				matriculaPeriodoTurmaDisciplinaVO.setApresentarConsultarRelatoriofinalfacilitador(false);
			}
			matriculaPeriodoTurmaDisciplinaVOs.add(matriculaPeriodoTurmaDisciplinaVO);
		}
		Ordenacao.ordenarLista(matriculaPeriodoTurmaDisciplinaVOs, "ordenacaoTelaInicialVisaoAluno");
		return matriculaPeriodoTurmaDisciplinaVOs;
	}
	
	public void getSQLPadraoJoinMatriculaPeriodoTurmaDisciplinaTurma(StringBuilder sb) {
		sb.append(" ((turma.subturma = false and turma.turmaagrupada = false and turma.codigo = matriculaperiodoturmadisciplina.turma) ")
			.append(" or (matriculaperiodoturmadisciplina.turmapratica is not null and turma.subturma and turma.tiposubturma = '").append(TipoSubTurmaEnum.PRATICA.getName())
			.append("' and turma.codigo = matriculaperiodoturmadisciplina.turmapratica) ")
			.append(" or (matriculaperiodoturmadisciplina.turmateorica is not null and turma.subturma and turma.tiposubturma = '").append(TipoSubTurmaEnum.TEORICA.getName())
			.append("' and turma.codigo = matriculaperiodoturmadisciplina.turmateorica) ")
			.append(" or (turma.subturma and turma.tiposubturma = '").append(TipoSubTurmaEnum.GERAL.getName())
			.append("' and turma.codigo = matriculaperiodoturmadisciplina.turma ) ")
			.append(" or (turma.turmaagrupada and turma.subturma = false and exists (select turmaagrupada.codigo from turmaagrupada where turmaagrupada.turmaorigem = turma.codigo ")
			.append(" and  turmaagrupada.turma = matriculaperiodoturmadisciplina.turma )) ")
			.append(" or (matriculaperiodoturmadisciplina.turmapratica is not null and turma.turmaagrupada ")
			.append(" and exists (select turmaagrupada.codigo from turmaagrupada where turmaagrupada.turmaorigem = turma.codigo and  turmaagrupada.turma = matriculaperiodoturmadisciplina.turmapratica)) ")
			.append(" or (matriculaperiodoturmadisciplina.turmateorica is not null and turma.turmaagrupada ")
			.append(" and exists (select turmaagrupada.codigo from turmaagrupada where turmaagrupada.turmaorigem = turma.codigo and  turmaagrupada.turma = matriculaperiodoturmadisciplina.turmateorica))) ");
	}
	
	public void getSQLPadraoJoinMatriculaPeriodoTurmaDisciplinaDisciplina(StringBuilder sb) {
		sb.append(" inner join lateral ( select disciplina.codigo as codigo ")
			.append(" union select case when turma.turmaagrupada then disciplinaequivalente.equivalente else 0 end codigo from disciplinaequivalente where disciplinaequivalente.disciplina = disciplina.codigo ")
			.append(" union select case when turma.turmaagrupada then disciplinaequivalente.disciplina else 0 end codigo from disciplinaequivalente where disciplinaequivalente.equivalente = disciplina.codigo ")
			.append(" ) as disciplinas on matriculaperiodoturmadisciplina.disciplina = disciplinas.codigo ");
	}
	
	@Override
	public void alterarPorGradeDisciplinaAlteracaoMatrizAtivaInativa(GradeDisciplinaVO gradeDisciplinaVO) throws Exception {
		StringBuilder sql = new StringBuilder("update matriculaperiodoturmadisciplina set disciplina = ?, modalidadedisciplina = ?, disciplinacomposta = ? where gradedisciplina = ?");
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
				sqlAlterar.setInt(1, gradeDisciplinaVO.getDisciplina().getCodigo().intValue());
				sqlAlterar.setString(2, gradeDisciplinaVO.getModalidadeDisciplina().toString());
				sqlAlterar.setBoolean(3, gradeDisciplinaVO.getDisciplinaComposta());
				sqlAlterar.setInt(4, gradeDisciplinaVO.getCodigo());
				return sqlAlterar;
			}
		});
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarParaForaDaGradePorGradeDisciplina(GradeDisciplinaVO gradeDisciplinaVO, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("update matriculaperiodoturmadisciplina set gradedisciplina = null where gradedisciplina = ?");
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
				sqlAlterar.setInt(1, gradeDisciplinaVO.getCodigo());
				return sqlAlterar;
			}
		});
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirPorGradeDisciplina(Integer gradeDisciplina, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sqlStr = new StringBuilder("DELETE FROM matriculaperiodoturmadisciplina WHERE gradeDisciplina = ").append(gradeDisciplina).append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado));
		try {
			getConexao().getJdbcTemplate().update(sqlStr.toString());
		} finally {
			sqlStr = null;
		}
	}
	
	
	public void realizarDefinicaoBimestre(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, UsuarioVO usuario) throws Exception {
		if(Uteis.isAtributoPreenchido(matriculaPeriodoTurmaDisciplinaVO.getGradeDisciplinaCompostaVO())) {
			OfertaDisciplinaVO ofertaDisciplinaVO = getFacadeFactory().getOfertaDisciplinaFacade().consultarConfiguracaoAcademicoPorDisciplinaAnoSemestre(matriculaPeriodoTurmaDisciplinaVO.getGradeDisciplinaCompostaVO().getDisciplina().getCodigo(), matriculaPeriodoTurmaDisciplinaVO.getAno(), matriculaPeriodoTurmaDisciplinaVO.getSemestre());
			if(Uteis.isAtributoPreenchido(ofertaDisciplinaVO)) {
				switch (ofertaDisciplinaVO.getPeriodo()) {
				case BIMESTRE_01:
					matriculaPeriodoTurmaDisciplinaVO.setBimestre(1);
					break;
				case BIMESTRE_02:
					matriculaPeriodoTurmaDisciplinaVO.setBimestre(2);
					break;
				case BIMESTRE_03:
					matriculaPeriodoTurmaDisciplinaVO.setBimestre(1);
					break;
				case BIMESTRE_04:
					matriculaPeriodoTurmaDisciplinaVO.setBimestre(2);
					break;
				default:
					matriculaPeriodoTurmaDisciplinaVO.setBimestre(0);
					break;
				}				
				return ;
			}
			matriculaPeriodoTurmaDisciplinaVO.setBimestre(matriculaPeriodoTurmaDisciplinaVO.getGradeDisciplinaCompostaVO().getBimestre());
			
		} else if(Uteis.isAtributoPreenchido(matriculaPeriodoTurmaDisciplinaVO.getGradeCurricularGrupoOptativaDisciplinaVO())) {
			OfertaDisciplinaVO ofertaDisciplinaVO = getFacadeFactory().getOfertaDisciplinaFacade().consultarConfiguracaoAcademicoPorDisciplinaAnoSemestre(matriculaPeriodoTurmaDisciplinaVO.getGradeCurricularGrupoOptativaDisciplinaVO().getDisciplina().getCodigo(), matriculaPeriodoTurmaDisciplinaVO.getAno(), matriculaPeriodoTurmaDisciplinaVO.getSemestre());
			if(Uteis.isAtributoPreenchido(ofertaDisciplinaVO)) {
				switch (ofertaDisciplinaVO.getPeriodo()) {
				case BIMESTRE_01:
					matriculaPeriodoTurmaDisciplinaVO.setBimestre(1);
					break;
				case BIMESTRE_02:
					matriculaPeriodoTurmaDisciplinaVO.setBimestre(2);
					break;
				case BIMESTRE_03:
					matriculaPeriodoTurmaDisciplinaVO.setBimestre(1);
					break;
				case BIMESTRE_04:
					matriculaPeriodoTurmaDisciplinaVO.setBimestre(2);
					break;
				default:
					matriculaPeriodoTurmaDisciplinaVO.setBimestre(0);
					break;
				}				
				return ;
			}
			matriculaPeriodoTurmaDisciplinaVO.setBimestre(matriculaPeriodoTurmaDisciplinaVO.getGradeCurricularGrupoOptativaDisciplinaVO().getBimestre());
		} else if(Uteis.isAtributoPreenchido(matriculaPeriodoTurmaDisciplinaVO.getGradeDisciplinaVO())) {
			OfertaDisciplinaVO ofertaDisciplinaVO = getFacadeFactory().getOfertaDisciplinaFacade().consultarConfiguracaoAcademicoPorDisciplinaAnoSemestre(matriculaPeriodoTurmaDisciplinaVO.getGradeDisciplinaVO().getDisciplina().getCodigo(), matriculaPeriodoTurmaDisciplinaVO.getAno(), matriculaPeriodoTurmaDisciplinaVO.getSemestre());
			if(Uteis.isAtributoPreenchido(ofertaDisciplinaVO)) {
				switch (ofertaDisciplinaVO.getPeriodo()) {
				case BIMESTRE_01:
					matriculaPeriodoTurmaDisciplinaVO.setBimestre(1);
					break;
				case BIMESTRE_02:
					matriculaPeriodoTurmaDisciplinaVO.setBimestre(2);
					break;
				case BIMESTRE_03:
					matriculaPeriodoTurmaDisciplinaVO.setBimestre(1);
					break;
				case BIMESTRE_04:
					matriculaPeriodoTurmaDisciplinaVO.setBimestre(2);
					break;
				default:
					matriculaPeriodoTurmaDisciplinaVO.setBimestre(0);
					break;
				}				
				return ;
			}
			matriculaPeriodoTurmaDisciplinaVO.setBimestre(matriculaPeriodoTurmaDisciplinaVO.getGradeDisciplinaVO().getBimestre());
		}				
		matriculaPeriodoTurmaDisciplinaVO.setBimestre(0);
	}
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirComBaseNaMatriculaPeriodoMatriculaCodDisciplina(Integer codMatriculaPeriodo, String matricula, Integer codDisciplina,  UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sqlStr = new StringBuilder("DELETE FROM matriculaperiodoturmadisciplina ");
		sqlStr.append(" WHERE  matriculaperiodo = ").append(codMatriculaPeriodo);
		sqlStr.append(" AND    matricula = '").append(matricula).append("' ");
		sqlStr.append(" AND    disciplina = ").append(codDisciplina).append("  ");	
		sqlStr.append(" ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado));
		try {
			getConexao().getJdbcTemplate().update(sqlStr.toString());
		} finally {
			sqlStr = null;
		}	
		
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarGravarDisciplinasMatriculaProcessoSeletivo(MatriculaRSVO matriculaRSVO, UsuarioVO usuarioVO) throws Exception {
		matriculaRSVO.setMensagem(null);
        ConfiguracaoFinanceiroVO configuracaoFinanceiroVO = getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, matriculaRSVO.getUnidadeEnsinoRSVO().getCodigo(), usuarioVO);
       	MatriculaVO matriculaVO = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(matriculaRSVO.getMatricula(), matriculaRSVO.getUnidadeEnsinoRSVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, configuracaoFinanceiroVO, usuarioVO);
		MatriculaPeriodoVO matriculaPeriodoVO = matriculaVO.getMatriculaPeriodoVOs().get(0);			
		ProcessoMatriculaVO processoMatriculaVO = getFacadeFactory().getProcessoMatriculaFacade().consultaRapidaPorMatriculaPeriodo(matriculaPeriodoVO.getCodigo(),usuarioVO
);
		
		if(Uteis.isAtributoPreenchido(matriculaRSVO.getDisciplinasMatricula()) && Uteis.isAtributoPreenchido(processoMatriculaVO) && processoMatriculaVO.getPermiteIncluirExcluirDisciplinaVisaoAluno() && matriculaPeriodoVO.getPermitirIncluirExcluirDisciplinas()) {
			
			Uteis.checkState(matriculaRSVO.getDisciplinasMatricula().stream().allMatch(d -> d.getEstudar().equals(Boolean.FALSE)), "Deve ser informado ao menos 1 disciplina para realizar a Matrícula ");			
			List<DisciplinaRSVO> listaDisciplina = matriculaRSVO.getDisciplinasMatricula().stream().filter(disc -> disc.getEstudar()).collect(Collectors.toList());			
			
			realizarMontagenDadosMatriculaPeriodoTurmaDisciplinaDasDisciplinasProcessoSeletivo(usuarioVO,configuracaoFinanceiroVO, matriculaVO, matriculaPeriodoVO, listaDisciplina);		
			getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().incluirMatriculaPeriodoTurmaDisciplinas(matriculaVO, matriculaPeriodoVO, matriculaPeriodoVO.getMatriculaPeriodoTumaDisciplinaVOs(), configuracaoFinanceiroVO, matriculaVO.getGradeCurricularAtual(), usuarioVO);
		    
		}	
		
	}

	/**
	 * @param usuarioVO
	 * @param configuracaoFinanceiroVO
	 * @param matriculaVO
	 * @param matriculaPeriodoVO
	 * @param listaDisciplina
	 * @throws Exception
	 */
	public void realizarMontagenDadosMatriculaPeriodoTurmaDisciplinaDasDisciplinasProcessoSeletivo(
			UsuarioVO usuarioVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, MatriculaVO matriculaVO,
			MatriculaPeriodoVO matriculaPeriodoVO, List<DisciplinaRSVO> listaDisciplina) throws Exception {
		Iterator<MatriculaPeriodoTurmaDisciplinaVO> it = matriculaPeriodoVO.getMatriculaPeriodoTumaDisciplinaVOs().iterator();
		while(it.hasNext()) {
			MatriculaPeriodoTurmaDisciplinaVO matptd =  (MatriculaPeriodoTurmaDisciplinaVO) it.next();
			if(!listaDisciplina.stream().anyMatch(d -> d.getCodigo().equals(matptd.getDisciplina().getCodigo()))) {
				it.remove();					
			}				
		}				
		List<MatriculaPeriodoTurmaDisciplinaVO> listaTemp = matriculaPeriodoVO.getMatriculaPeriodoTumaDisciplinaVOs();
		matriculaPeriodoVO.setMatriculaPeriodoTumaDisciplinaVOs(null);
		getFacadeFactory().getTurmaFacade().processarDadosPermitinentesTurmaSelecionada(matriculaVO, matriculaPeriodoVO, configuracaoFinanceiroVO, usuarioVO);   
		matriculaVO.setMatriculaOnlineProcSeletivo(true);
		getFacadeFactory().getMatriculaPeriodoFacade().inicializarDadosDefinirDisciplinasMatriculaPeriodo(matriculaVO, matriculaPeriodoVO, usuarioVO, null, false, false);		
		removerTurmaDisciplinasNaoMarcadasParaEstudar(matriculaPeriodoVO, listaDisciplina, listaTemp);
	}

	
	public void removerTurmaDisciplinasNaoMarcadasParaEstudar(MatriculaPeriodoVO matriculaPeriodoVO,List<DisciplinaRSVO> listaDisciplina, List<MatriculaPeriodoTurmaDisciplinaVO> listaTemp) {
		listaTemp.stream().forEach(obj -> {
			int index = 0;
			Iterator<MatriculaPeriodoTurmaDisciplinaVO> i = matriculaPeriodoVO.getMatriculaPeriodoTumaDisciplinaVOs().iterator();
			while (i.hasNext()) {
				MatriculaPeriodoTurmaDisciplinaVO objExistente = (MatriculaPeriodoTurmaDisciplinaVO) i.next();
				if (objExistente.getDisciplina().getCodigo().equals(obj.getDisciplina().getCodigo())) {
					matriculaPeriodoVO.getMatriculaPeriodoTumaDisciplinaVOs().set(index, obj);
					break;
				}else {
				 index++;
				}
			}			
		});			
		
		Iterator<MatriculaPeriodoTurmaDisciplinaVO> it2 = matriculaPeriodoVO.getMatriculaPeriodoTumaDisciplinaVOs().iterator();
		while(it2.hasNext()) {
			MatriculaPeriodoTurmaDisciplinaVO matptd =  (MatriculaPeriodoTurmaDisciplinaVO) it2.next();
			if(!listaDisciplina.stream().anyMatch(d -> d.getCodigo().equals(matptd.getDisciplina().getCodigo()))) {
				it2.remove();					
			}				
		}
	}
}
