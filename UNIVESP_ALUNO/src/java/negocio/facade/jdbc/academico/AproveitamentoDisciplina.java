package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

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

import negocio.comuns.academico.AproveitamentoDisciplinaVO;
import negocio.comuns.academico.AproveitamentoDisciplinasEntreMatriculasVO;
//import negocio.comuns.academico.ConcessaoCargaHorariaDisciplinaVO;
//import negocio.comuns.academico.ConcessaoCreditoDisciplinaVO;
import negocio.comuns.academico.ConfiguracaoAcademicoNotaConceitoVO;
import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.DisciplinasAproveitadasVO;
import negocio.comuns.academico.GradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO;
import negocio.comuns.academico.GradeCurricularGrupoOptativaDisciplinaVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.GradeDisciplinaComHistoricoAlunoVO;
import negocio.comuns.academico.GradeDisciplinaCompostaVO;
import negocio.comuns.academico.GradeDisciplinaVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.MapaEquivalenciaDisciplinaCursadaVO;
import negocio.comuns.academico.MapaEquivalenciaDisciplinaMatrizCurricularVO;
import negocio.comuns.academico.MapaEquivalenciaDisciplinaVO;
import negocio.comuns.academico.MapaEquivalenciaMatrizCurricularVO;
import negocio.comuns.academico.MatriculaComHistoricoAlunoVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PeriodoLetivoComHistoricoAlunoVO;
import negocio.comuns.academico.PeriodoLetivoVO;
import negocio.comuns.academico.enumeradores.TipoControleComposicaoEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.CidadeVO;

import negocio.comuns.protocolo.RequerimentoDisciplinasAproveitadasVO;
import negocio.comuns.protocolo.RequerimentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoHistorico;
import negocio.comuns.utilitarias.dominios.SituacaoRequerimento;

import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.academico.AproveitamentoDisciplinaInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>AproveitamentoDisciplinaVO</code>. Responsável por
 * implementar operações como incluir, alterar, excluir e consultar pertinentes
 * a classe <code>AproveitamentoDisciplinaVO</code>. Encapsula toda a interação
 * com o banco de dados.
 * 
 * @see AproveitamentoDisciplinaVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
@SuppressWarnings("unchecked")
public class AproveitamentoDisciplina extends ControleAcesso implements AproveitamentoDisciplinaInterfaceFacade {

	private static final long serialVersionUID = 4631801369222728246L;

	protected static String idEntidade;

	public AproveitamentoDisciplina() throws Exception {
		super();

	}

	/**
	 * Operação responsável por retornar um novo objeto da classe
	 * <code>AproveitamentoDisciplinaVO</code>.
	 */
	public AproveitamentoDisciplinaVO novo() throws Exception {
		AproveitamentoDisciplina.incluir(getIdEntidade());
		AproveitamentoDisciplinaVO obj = new AproveitamentoDisciplinaVO();
		return obj;
	}

	public void alterarSituacaoRequerimento(AproveitamentoDisciplinaVO obj, UsuarioVO usuario) throws Exception {
		// switch (SituacaoAproveitamentoDisciplina.getEnum(obj.getSituacao()))
		// {
		// case EFETIVADO:
		// getFacadeFactory().getRequerimentoFacade().alterarSituacao(obj.getCodigoRequerimento().getCodigo(),
		// SituacaoRequerimento.FINALIZADO_DEFERIDO.getValor());
		// break;
		// case EM_AVALIACAO:
		// getFacadeFactory().getRequerimentoFacade().alterarSituacao(obj.getCodigoRequerimento().getCodigo(),
		// SituacaoRequerimento.EM_EXECUCAO.getValor());
		// break;
		// case INDEFERIDO:
		// getFacadeFactory().getRequerimentoFacade().alterarSituacao(obj.getCodigoRequerimento().getCodigo(),
		// SituacaoRequerimento.FINALIZADO_INDEFERIDO.getValor());
		// break;
		// }
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe
	 * <code>AproveitamentoDisciplinaVO</code>. Primeiramente valida os dados (
	 * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados
	 * e a permissão do usuário para realizar esta operacão na entidade. Isto,
	 * através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>AproveitamentoDisciplinaVO</code> que será
	 *            gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final AproveitamentoDisciplinaVO obj, boolean controleAcesso, UsuarioVO usuario
			) throws Exception {
		try {
			ConfiguracaoGeralSistemaVO c = getFacadeFactory().getConfiguracaoGeralSistemaFacade()
					.consultarConfiguracaoASerUsadaUnidadEnsino(obj.getUnidadeEnsino().getCodigo(),
							Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			AproveitamentoDisciplinaVO.validarDados(obj, c);
			alterarSituacaoRequerimento(obj, usuario);
			incluir(getIdEntidade(), controleAcesso, usuario);
			final String sql = "INSERT INTO AproveitamentoDisciplina( data, matricula, codigoRequerimento, unidadeensino, curso, turno, gradecurricular, periodoletivo, pessoa, unidadeEnsinoCurso, responsavelAutorizacao, matriculaPeriodo, tipo, aproveitamentoPrevisto, instituicao, cidade) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo";

			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setDate(1, Uteis.getDataJDBC(obj.getData()));
					if (!obj.getMatricula().getMatricula().equals("")) {
						sqlInserir.setString(2, obj.getMatricula().getMatricula());
					} else {
						sqlInserir.setNull(2, 0);
					}
					sqlInserir.setInt(3, obj.getCodigoRequerimento().getCodigo().intValue());
					if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
						sqlInserir.setInt(4, obj.getUnidadeEnsino().getCodigo());
					} else {
						sqlInserir.setNull(4, 0);
					}
					if (obj.getCurso().getCodigo().intValue() != 0) {
						sqlInserir.setInt(5, obj.getCurso().getCodigo());
					} else {
						sqlInserir.setNull(5, 0);
					}
					if (obj.getTurno().getCodigo().intValue() != 0) {
						sqlInserir.setInt(6, obj.getTurno().getCodigo().intValue());
					} else {
						sqlInserir.setNull(6, 0);
					}
					if (obj.getGradeCurricular().getCodigo().intValue() != 0) {
						sqlInserir.setInt(7, obj.getGradeCurricular().getCodigo().intValue());
					} else {
						sqlInserir.setNull(7, 0);
					}
					if (obj.getPeridoLetivo().getCodigo().intValue() != 0) {
						sqlInserir.setInt(8, obj.getPeridoLetivo().getCodigo().intValue());
					} else {
						sqlInserir.setNull(8, 0);
					}

					if (obj.getPessoa().getCodigo().intValue() != 0) {
						sqlInserir.setInt(9, obj.getPessoa().getCodigo().intValue());
					} else {
						sqlInserir.setNull(9, 0);
					}
					sqlInserir.setInt(10, obj.getUnidadeEnsinoCurso());
					if (obj.getResponsavelAutorizacao().getCodigo().intValue() != 0) {
						sqlInserir.setInt(11, obj.getResponsavelAutorizacao().getCodigo().intValue());
					} else {
						sqlInserir.setNull(11, 0);
					}
					if (obj.getMatriculaPeriodo().getCodigo().intValue() != 0) {
						sqlInserir.setInt(12, obj.getMatriculaPeriodo().getCodigo().intValue());
					} else {
						sqlInserir.setNull(12, 0);
					}
					sqlInserir.setString(13, obj.getTipo());
					sqlInserir.setBoolean(14, obj.getAproveitamentoPrevisto());
					sqlInserir.setString(15, obj.getInstituicao());
					if (obj.getCidadeVO().getCodigo().intValue() != 0) {
						sqlInserir.setInt(16, obj.getCidadeVO().getCodigo().intValue());
					} else {
						sqlInserir.setNull(16, 0);
					}
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
			getFacadeFactory().getDisciplinaAproveitadasFacade().incluirDisciplinasAproveitadass(obj,
					obj.getCurso().getPeriodicidade(), obj.getDisciplinasAproveitadasVOs(), usuario);

			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.getCodigoRequerimento().setSituacao(SituacaoRequerimento.EM_EXECUCAO.getValor());
			verificarCodigoDisciplinasAproveitadas(obj.getDisciplinasAproveitadasVOs());
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			throw e;
		}
	}

	// TODO URGENTE...
	public void alterarSituacaoHistoricoConcessaoCreditoDisciplina(AproveitamentoDisciplinaVO obj,
			String matricula,
			Integer matriculaPeriodo, UsuarioVO usuario) throws Exception {
//		for (ConcessaoCreditoDisciplinaVO concessaoCreditoDisciplinaVO : concessaoCreditoDisciplinaVOs) {
//			HistoricoVO historicoVO = getFacadeFactory().getHistoricoFacade()
//					.consultaRapidaPorMatricula_matriculaPeriodo_Disciplina(matricula, matriculaPeriodo,
//							obj.getMatricula().getGradeCurricularAtual().getCodigo(),
//							concessaoCreditoDisciplinaVO.getDisciplinaVO().getCodigo(), false, usuario);
//			if (historicoVO != null && historicoVO.getCodigo() != 0) {
//				validarDadosSituacaoHistorico(historicoVO.getSituacao(),
//						concessaoCreditoDisciplinaVO.getDisciplinaVO().getNome());
//				getFacadeFactory().getHistoricoFacade().alterarSituacaoHistoricoPorCodigo(historicoVO.getCodigo(),
//						SituacaoHistorico.CONCESSAO_CREDITO.getValor(), usuario);
//			} else {
//				HistoricoVO historico = getFacadeFactory().getHistoricoFacade()
//						.consultaRapidaPorMatricula_Ano_Semestre_Disciplina(matricula,
//								concessaoCreditoDisciplinaVO.getAno(), concessaoCreditoDisciplinaVO.getSemestre(),
//								concessaoCreditoDisciplinaVO.getDisciplinaVO().getCodigo(), false, usuario);
//				if (historico != null && historico.getCodigo() != 0) {
//					validarDadosSituacaoHistorico(historico.getSituacao(),
//							concessaoCreditoDisciplinaVO.getDisciplinaVO().getNome());
//					getFacadeFactory().getHistoricoFacade().alterarSituacaoHistoricoPorCodigo(historico.getCodigo(),
//							SituacaoHistorico.CONCESSAO_CREDITO.getValor(), usuario);
//				} else {
//					// TODO URGENTE
//					HistoricoVO hist = new HistoricoVO(); // obj.getMatriculaPeriodo().criarHistoricoCC(concessaoCreditoDisciplinaVO,
//															// obj.getResponsavelAutorizacao(),
//															// obj.getCurso().getCodigo(),
//															// SituacaoHistorico.CONCESSAO_CREDITO.getValor());
//					historico.getConfiguracaoAcademico().setCodigo(getFacadeFactory().getConfiguracaoAcademicoFacade()
//							.consultarCodigoConfiguracaoAcademicaUtilizarHistoricoPorMatriculaPeriodoEDisciplina(
//									matriculaPeriodo, historico.getDisciplina().getCodigo()));
//					getFacadeFactory().getHistoricoFacade().incluir(hist, usuario);
//					// throw new Exception("Não foi encontrado a disciplina "
//					// +concessaoCreditoDisciplinaVO.getDisciplinaVO().getNome()+", com o ano e
//					// semestre informado.");
//				}
//			}
//		}
	}

	// TODO URGENTE...
	public void alterarSituacaoHistoricoConcessaoCargaHorariaDisciplina(AproveitamentoDisciplinaVO obj,
			 String matricula,
			Integer matriculaPeriodo, UsuarioVO usuario) throws Exception {
//		for (ConcessaoCargaHorariaDisciplinaVO concessaoCargaHorariaDisciplinaVO : concessaoCargaHorariaDisciplinaVOs) {
//			HistoricoVO historicoVO = getFacadeFactory().getHistoricoFacade()
//					.consultaRapidaPorMatricula_matriculaPeriodo_Disciplina(matricula, matriculaPeriodo,
//							obj.getMatricula().getGradeCurricularAtual().getCodigo(),
//							concessaoCargaHorariaDisciplinaVO.getDisciplinaVO().getCodigo(), false, usuario);
//			if (historicoVO != null && historicoVO.getCodigo() != 0) {
//				validarDadosSituacaoHistorico(historicoVO.getSituacao(),
//						concessaoCargaHorariaDisciplinaVO.getDisciplinaVO().getNome());
//				getFacadeFactory().getHistoricoFacade().alterarSituacaoHistoricoPorCodigo(historicoVO.getCodigo(),
//						SituacaoHistorico.CONCESSAO_CREDITO.getValor(), usuario);
//			} else {
//				HistoricoVO historico = getFacadeFactory().getHistoricoFacade()
//						.consultaRapidaPorMatricula_Ano_Semestre_Disciplina(matricula,
//								concessaoCargaHorariaDisciplinaVO.getAno(),
//								concessaoCargaHorariaDisciplinaVO.getSemestre(),
//								concessaoCargaHorariaDisciplinaVO.getDisciplinaVO().getCodigo(), false, usuario);
//				if (historico != null && historico.getCodigo() != 0) {
//					validarDadosSituacaoHistorico(historico.getSituacao(),
//							concessaoCargaHorariaDisciplinaVO.getDisciplinaVO().getNome());
//					getFacadeFactory().getHistoricoFacade().alterarSituacaoHistoricoPorCodigo(historico.getCodigo(),
//							SituacaoHistorico.CONCESSAO_CREDITO.getValor(), usuario);
//				} else {
//					// TODO URGENTE
//					HistoricoVO hist = new HistoricoVO(); // obj.getMatriculaPeriodo().criarHistoricoCH(concessaoCargaHorariaDisciplinaVO,
//															// obj.getResponsavelAutorizacao(),
//															// obj.getCurso().getCodigo(),
//															// SituacaoHistorico.CONCESSAO_CREDITO.getValor());
//					// historico.getConfiguracaoAcademico().setCodigo(getFacadeFactory().getConfiguracaoAcademicoFacade().consultarCodigoConfiguracaoAcademicaUtilizarHistoricoPorMatriculaPeriodoEDisciplina(matriculaPeriodo,
//					// historico.getDisciplina().getCodigo()));
//					getFacadeFactory().getHistoricoFacade().incluir(hist, usuario);
//					// throw new Exception("Não foi encontrado a disciplina "
//					// +concessaoCargaHorariaDisciplinaVO.getDisciplinaVO().getNome()+", com o ano e
//					// semestre informado.");
//				}
//			}
//		}
	}

	public void validarDadosSituacaoHistorico(String situacao, String disciplina) throws Exception {
		if (situacao.equals("AP")) {
			throw new Exception("A concessão não pode ser realizada para a Disciplina " + disciplina
					+ ", sua SITUAÇÃO encontra-se: APROVADA ");
		}
		if (situacao.equals("AA")) {
			throw new Exception("A concessão não pode ser realizada para a Disciplina " + disciplina
					+ ", sua SITUAÇÃO encontra-se: APROVADA POR APROVEITAMENTO");
		}
	}

	public void verificarCodigoDisciplinasAproveitadas(List<DisciplinasAproveitadasVO> listaDisciplinasAproveitadas)
			throws Exception {
		for (DisciplinasAproveitadasVO disciplinaAproveitadaVO : listaDisciplinasAproveitadas) {
			disciplinaAproveitadaVO.setCodigo(0);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void verificarAlunoAprovadoDisciplina(HistoricoVO historico, String matricula, Integer matriculaPeriodo,
			UsuarioVO usuario ) throws Exception {
		List obj = consultarHistoricoAlunoDisciplina(matricula, historico.getDisciplina().getCodigo(), false, usuario
				);
		boolean jaIncluiu = false;
		if (obj.isEmpty()) {
			// historico.getConfiguracaoAcademico().setCodigo(getFacadeFactory().getConfiguracaoAcademicoFacade().consultarCodigoConfiguracaoAcademicaUtilizarHistoricoPorMatriculaPeriodoEDisciplina(matriculaPeriodo,
			// historico.getDisciplina().getCodigo()));
			getFacadeFactory().getHistoricoFacade().incluir(historico, usuario);
		} else {
			Iterator i = obj.iterator();
			while (i.hasNext()) {
				HistoricoVO historicoVO = (HistoricoVO) i.next();
				historicoVO.setAnoHistorico(historico.getAnoHistorico());
				historicoVO.setSemestreHistorico(historico.getSemestreHistorico());
				if (historicoVO.getSituacao().equals("AA")) {
					historico.setCodigo(historicoVO.getCodigo());
					if (historico.getConfiguracaoAcademico().getCodigo() == 0) {
						historico.getConfiguracaoAcademico().setCodigo(getFacadeFactory()
								.getConfiguracaoAcademicoFacade()
								.consultarCodigoConfiguracaoAcademicaUtilizarHistoricoPorMatriculaPeriodoEDisciplina(
										matriculaPeriodo, historico.getDisciplina().getCodigo()));
					}
					getFacadeFactory().getHistoricoFacade().alterar(historico, usuario);
					jaIncluiu = true;
				}
				/**
				 * Verifica se já existe um histórico para a matrículaperiodo, caso exista os
				 * dados do mesmo são atualizados
				 */
				if (matriculaPeriodo != null) {
					if (historicoVO.getMatriculaPeriodo().getCodigo().intValue() == matriculaPeriodo.intValue()) {
						historicoVO.setFreguencia(historico.getFreguencia());
						historicoVO.setCargaHorariaAproveitamentoDisciplina(
								historico.getCargaHorariaAproveitamentoDisciplina());
						historicoVO.setCargaHorariaCursada(historico.getCargaHorariaCursada());
						historicoVO.setInstituicao(historico.getInstituicao());
						historicoVO.setCidadeVO(historico.getCidadeVO());
						historicoVO.setMediaFinal(historico.getMediaFinal());
						historicoVO.setDisciplinasAproveitadas(historico.getDisciplinasAproveitadas());
						historicoVO.setMediaFinalConceito(historico.getMediaFinalConceito());
						historicoVO.setSituacao("AA");
						historicoVO.setTipoHistorico("NO");
						historicoVO.setResponsavel(historico.getResponsavel());
						historicoVO.setIsentarMediaFinal(historico.getIsentarMediaFinal());
						historicoVO.getGradeDisciplinaVO().setCodigo(historico.getGradeDisciplinaVO().getCodigo());
						historicoVO.getGradeCurricularGrupoOptativaDisciplinaVO()
								.setCodigo(historico.getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo());
						if (historico.getConfiguracaoAcademico().getCodigo() == 0) {
							historico.getConfiguracaoAcademico().setCodigo(getFacadeFactory()
									.getConfiguracaoAcademicoFacade()
									.consultarCodigoConfiguracaoAcademicaUtilizarHistoricoPorMatriculaPeriodoEDisciplina(
											matriculaPeriodo, historico.getDisciplina().getCodigo()));
						}
						if (historicoVO.getMatrizCurricular() == null
								|| historicoVO.getMatrizCurricular().getCodigo().equals(0)) {
							historicoVO.getMatrizCurricular().setCodigo(historico.getMatrizCurricular().getCodigo());
						}
						getFacadeFactory().getHistoricoFacade().alterar(historicoVO, usuario);
						jaIncluiu = true;
					}
					if (historicoVO.getMatriculaPeriodo().getCodigo().intValue() != matriculaPeriodo.intValue()
							&& !jaIncluiu) {
						historico.getConfiguracaoAcademico().setCodigo(getFacadeFactory()
								.getConfiguracaoAcademicoFacade()
								.consultarCodigoConfiguracaoAcademicaUtilizarHistoricoPorMatriculaPeriodoEDisciplina(
										matriculaPeriodo, historico.getDisciplina().getCodigo()));
						getFacadeFactory().getHistoricoFacade().incluir(historico, usuario);
						jaIncluiu = true;
					}
				}
			}
		}
	}

	public List consultarHistoricoAlunoDisciplina(String matricula, Integer disciplina, boolean controlarAcesso,
			UsuarioVO usuario ) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		List hist = getFacadeFactory().getHistoricoFacade().consultarPorMatriculaDisciplinaSituacao(matricula,
				disciplina, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		return hist;
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe
	 * <code>AproveitamentoDisciplinaVO</code>. Sempre utiliza a chave primária da
	 * classe como atributo para localização do registro a ser alterado.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica
	 * a conexão com o banco de dados e a permissão do usuário para realizar esta
	 * operacão na entidade. Isto, através da operação <code>alterar</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>AproveitamentoDisciplinaVO</code> que será
	 *            alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final AproveitamentoDisciplinaVO obj, UsuarioVO usuario
			) throws Exception {
		try {
			ConfiguracaoGeralSistemaVO c = getFacadeFactory().getConfiguracaoGeralSistemaFacade()
					.consultarConfiguracaoASerUsadaUnidadEnsino(obj.getUnidadeEnsino().getCodigo(),
							Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			AproveitamentoDisciplinaVO.validarDados(obj, c);
			AproveitamentoDisciplina.alterar(getIdEntidade());
			final String sql = "UPDATE AproveitamentoDisciplina set data=?, matricula=?, codigoRequerimento=?, unidadeEnsino=?, curso=?, turno=?, gradecurricular=?, periodoletivo=?,  pessoa=?, "
					+ "unidadeEnsinoCurso=?, responsavelAutorizacao=?, matriculaPeriodo=?, tipo=?, aproveitamentoPrevisto=?, instituicao=?, cidade=? WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setDate(1, Uteis.getDataJDBC(obj.getData()));
					if (!obj.getMatricula().getMatricula().equals("")) {
						sqlAlterar.setString(2, obj.getMatricula().getMatricula());
					} else {
						sqlAlterar.setNull(2, 0);
					}
					sqlAlterar.setInt(3, obj.getCodigoRequerimento().getCodigo().intValue());
					if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(4, obj.getUnidadeEnsino().getCodigo());
					} else {
						sqlAlterar.setNull(4, 0);
					}
					if (obj.getCurso().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(5, obj.getCurso().getCodigo());
					} else {
						sqlAlterar.setNull(5, 0);
					}
					if (obj.getTurno().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(6, obj.getTurno().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(6, 0);
					}
					if (obj.getGradeCurricular().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(7, obj.getGradeCurricular().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(7, 0);
					}
					if (obj.getPeridoLetivo().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(8, obj.getPeridoLetivo().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(8, 0);
					}
					if (obj.getPessoa().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(9, obj.getPessoa().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(9, 0);
					}
					if (obj.getUnidadeEnsinoCurso().intValue() != 0) {
						sqlAlterar.setInt(10, obj.getUnidadeEnsinoCurso().intValue());
					} else {
						sqlAlterar.setNull(10, 0);
					}
					if (obj.getResponsavelAutorizacao().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(11, obj.getResponsavelAutorizacao().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(11, 0);
					}
					if (obj.getMatriculaPeriodo().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(12, obj.getMatriculaPeriodo().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(12, 0);
					}
					sqlAlterar.setString(13, obj.getTipo());
					sqlAlterar.setBoolean(14, obj.getAproveitamentoPrevisto());
					sqlAlterar.setString(15, obj.getInstituicao());
					if (obj.getCidadeVO().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(16, obj.getCidadeVO().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(16, 0);
					}
					sqlAlterar.setInt(17, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
			getFacadeFactory().getDisciplinaAproveitadasFacade().alterarDisciplinasAproveitadass(obj,
					obj.getCurso().getPeriodicidade(), obj.getDisciplinasAproveitadasVOs(), usuario);
		} catch (Exception e) {
			verificarCodigoDisciplinasAproveitadas(obj.getDisciplinasAproveitadasVOs());
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarAproveitoPrevistoParaEfetivo(final AproveitamentoDisciplinaVO obj, UsuarioVO usuario)
			throws Exception {
		final String sql = "UPDATE AproveitamentoDisciplina set aproveitamentoPrevisto=?, matricula=?  WHERE ((codigo = ?)) "
				+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				int i = 0;
				sqlAlterar.setBoolean(++i, Boolean.FALSE);
				sqlAlterar.setString(++i, obj.getMatricula().getMatricula());
				sqlAlterar.setInt(++i, obj.getCodigo());
				return sqlAlterar;
			}
		});
		getFacadeFactory().getDisciplinaAproveitadasFacade().alterarDisciplinasAproveitadasPrevistasParaEfetiva(obj,
				obj.getCurso().getPeriodicidade(), obj.getDisciplinasAproveitadasVOs(), usuario);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSituacao(Integer codigo, String situacao, UsuarioVO usuario) throws Exception {
		String sql = "UPDATE AproveitamentoDisciplina set  situacao=?  WHERE ((codigo = ?))"
				+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { situacao, codigo });
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarMatricula(Integer codigo, String matricula, UsuarioVO usuario) throws Exception {
		String sql = "UPDATE AproveitamentoDisciplina set  matricula=?  WHERE ((codigo = ?))"
				+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { matricula, codigo });
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe
	 * <code>AproveitamentoDisciplinaVO</code>. Sempre localiza o registro a ser
	 * excluído através da chave primária da entidade. Primeiramente verifica a
	 * conexão com o banco de dados e a permissão do usuário para realizar esta
	 * operacão na entidade. Isto, através da operação <code>excluir</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>AproveitamentoDisciplinaVO</code> que será
	 *            removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(AproveitamentoDisciplinaVO obj, UsuarioVO usuario) throws Exception {
		try {
			AproveitamentoDisciplina.excluir(getIdEntidade());
			getFacadeFactory().getDisciplinaAproveitadasFacade().excluirDisciplinasAproveitadass(obj, usuario);
			String sql = "DELETE FROM AproveitamentoDisciplina WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	public AproveitamentoDisciplinaVO montarDadosTransferenciaInternaPeloCodigoRequerimento(Integer requerimento,
			Integer unidadeEnsino, UsuarioVO usuario )
			throws Exception {
		RequerimentoVO requerimentoVO = getFacadeFactory().getRequerimentoFacade()
				.consultarPorChavePrimariaFiltrandoPorUnidadeEnsino(requerimento, "TI", unidadeEnsino, false,
						Uteis.NIVELMONTARDADOS_TODOS, usuario);
		if (!requerimentoVO.getIsRequerimentoPago()) {
			throw new Exception(
					"O requerimento está PENDENTE FINANCEIRAMENTE por isso não é possível realizar esta operação.");
		}
		if (requerimentoVO.getSituacao().equals(SituacaoRequerimento.EM_EXECUCAO.getValor())) {
			throw new Exception("O requerimento já está em " + SituacaoRequerimento.EM_EXECUCAO.getDescricao()
					+ " por isso não é possível realizar esta operação.");
		}
		if (requerimentoVO.getSituacao().equals(SituacaoRequerimento.FINALIZADO_DEFERIDO.getValor())) {
			throw new Exception("O requerimento já está " + SituacaoRequerimento.FINALIZADO_DEFERIDO.getDescricao()
					+ " por isso não é possível realizar esta operação.");
		}
		if (requerimentoVO.getSituacao().equals(SituacaoRequerimento.FINALIZADO_INDEFERIDO.getValor())) {
			throw new Exception("O requerimento já está " + SituacaoRequerimento.FINALIZADO_INDEFERIDO.getDescricao()
					+ " por isso não é possível realizar esta operação.");
		}
//		if (!requerimentoVO.getMatricula().getMatriculaCanceladaFinanceiramente()
//				&& getFacadeFactory().getContaReceberFacade().consultarExistenciaPendenciaFinanceiraMatricula(
//						requerimentoVO.getMatricula().getMatricula(), usuario)) {
//			throw new Exception(
//					"A Transferencia Interna da Matrícula não pode ser realizada. Realize a liberação financeira da matrícula ("
//							+ requerimentoVO.getMatricula().getMatricula() + ") e tente novamente!");
//		}
		AproveitamentoDisciplinaVO AproveitamentoDisciplinaVO = new AproveitamentoDisciplinaVO();
		AproveitamentoDisciplinaVO.setCodigoRequerimento(requerimentoVO);
		AproveitamentoDisciplinaVO.setPessoa(requerimentoVO.getPessoa());
		return AproveitamentoDisciplinaVO;
	}

	public void adicionarCursoTransferenciaInterna(AproveitamentoDisciplinaVO AproveitamentoDisciplinaVO,
			UnidadeEnsinoCursoVO obj, UsuarioVO usuario) throws Exception {
		if (AproveitamentoDisciplinaVO.getCodigoRequerimento().getMatricula().getCurso().getCodigo().intValue() == obj
				.getCurso().getCodigo().intValue()) {
			throw new Exception("Não é possivel fazer uma TRANSFERÊNCIA INTERNA para o curso que já está cursando.");
		}
		AproveitamentoDisciplinaVO.setCurso(obj.getCurso());
		AproveitamentoDisciplinaVO.setTurno(obj.getTurno());
		GradeCurricularVO gradeCurricularVO = getFacadeFactory().getGradeCurricularFacade()
				.consultarPorSituacaoGradeCurso(AproveitamentoDisciplinaVO.getCurso().getCodigo(), "AT", false,
						Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		if (gradeCurricularVO.getCodigo() == 0) {
			throw new Exception("Não existe uma Grade Curricular Ativa para este curso.");
		}
		AproveitamentoDisciplinaVO.setGradeCurricular(gradeCurricularVO);
		AproveitamentoDisciplinaVO.setUnidadeEnsinoCurso(obj.getCodigo());
		return;
	}

	public List<AproveitamentoDisciplinaVO> consultaRapidaPorTipoJustificativa(String valorConsulta,
			Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE UPPER(ad.tipojustificativa) LIKE UPPER('").append(valorConsulta.toUpperCase())
				.append("%') ");
		if (unidadeEnsino.intValue() != 0) {
			sql.append(" AND  (m.unidadeensino = ").append(unidadeEnsino).append(") ");
		}
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaBasica(resultado);
	}

	public List<AproveitamentoDisciplinaVO> consultaRapidaPorInstituicaoOrigem(String valorConsulta,
			Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE UPPER(ad.instituicaoorigem) LIKE UPPER('").append(valorConsulta.toUpperCase())
				.append("%') ");
		if (unidadeEnsino.intValue() != 0) {
			sql.append(" AND  (m.unidadeensino = ").append(unidadeEnsino).append(") ");
		}
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaBasica(resultado);
	}

	public List<AproveitamentoDisciplinaVO> consultaRapidaPorCodigoRequerimento(Integer valorConsulta,
			Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE ad.codigorequerimento = ").append(valorConsulta).append(" ");
		if (unidadeEnsino.intValue() != 0) {
			sql.append(" AND  (m.unidadeensino = ").append(unidadeEnsino).append(") ");
		}
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaBasica(resultado);
	}

	public List<AproveitamentoDisciplinaVO> consultaRapidaPorMatricula(String valorConsulta, Integer unidadeEnsino,
			boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE UPPER(ad.matricula) LIKE UPPER('").append(valorConsulta.toUpperCase()).append("%') ");
		if (unidadeEnsino.intValue() != 0) {
			sql.append(" AND  (m.unidadeensino = ").append(unidadeEnsino).append(") ");
		}
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaBasica(resultado);
	}

	public List<AproveitamentoDisciplinaVO> consultaRapidaPorSituacao(String valorConsulta, Integer unidadeEnsino,
			boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE UPPER(ad.situacao) LIKE UPPER('").append(valorConsulta.toUpperCase()).append("%') ");
		if (unidadeEnsino.intValue() != 0) {
			sql.append(" AND  (m.unidadeensino = ").append(unidadeEnsino).append(") ");
		}
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaBasica(resultado);
	}

	public List<AproveitamentoDisciplinaVO> consultaRapidaPorNomeCurso(String valorConsulta, Integer unidadeEnsino,
			boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE UPPER(c.nome) LIKE UPPER('").append(valorConsulta.toUpperCase()).append("%') ");
		if (unidadeEnsino.intValue() != 0) {
			sql.append(" AND  (m.unidadeensino = ").append(unidadeEnsino).append(") ");
		}
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaBasica(resultado);
	}

	public List<AproveitamentoDisciplinaVO> consultaRapidaPorNomeAluno(String valorConsulta, Integer unidadeEnsino,
			boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE  UPPER(p.nome) LIKE UPPER('").append(valorConsulta.toUpperCase()).append("%') ");
		if (unidadeEnsino.intValue() != 0) {
			sql.append(" AND  (m.unidadeensino = ").append(unidadeEnsino).append(") ");
		}
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaBasica(resultado);
	}
	
	@Override
	public List<AproveitamentoDisciplinaVO> consultaRapidaPorRegistroAcademicoAluno(String valorConsulta, Integer unidadeEnsino,
			boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE  UPPER(p.registroAcademico) LIKE UPPER('").append(valorConsulta.toUpperCase()).append("%') ");
		if (unidadeEnsino.intValue() != 0) {
			sql.append(" AND  (m.unidadeensino = ").append(unidadeEnsino).append(") ");
		}
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaBasica(resultado);
	}

	public List<AproveitamentoDisciplinaVO> consultaRapidaPorTurma(String valorConsulta, Integer unidadeEnsino,
			boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE UPPER(t.identificadorturma) LIKE UPPER('").append(valorConsulta.toUpperCase())
				.append("%') ");
		if (unidadeEnsino.intValue() != 0) {
			sql.append(" AND  (m.unidadeensino = ").append(unidadeEnsino).append(") ");
		}
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaBasica(resultado);
	}

	public List consultaRapidaPorData(Date prmIni, Date prmFim, Integer unidadeEnsino, boolean controlarAcesso,
			UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE ad.data >= '").append(Uteis.getDataJDBC(prmIni)).append("' AND ad.data <= '")
				.append(Uteis.getDataJDBC(prmFim)).append("' ");
		if (unidadeEnsino.intValue() != 0) {
			sql.append(" AND  (m.unidadeensino = ").append(unidadeEnsino).append(") ");
		}
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaBasica(resultado);
	}

	public List consultarPorDataUnidadeEnsino(Date prmIni, Date prmFim, String unidadeEnsino, boolean matriculado,
			boolean controlarAcesso, int nivelMontarDados, 
			UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "";
		if (matriculado) {
			sqlStr = "SELECT * FROM AproveitamentoDisciplina "
					+ "LEFT JOIN unidadeEnsino ON AproveitamentoDisciplina.unidadeEnsino = unidadeEnsino.codigo "
					+ "WHERE ((AproveitamentoDisciplina.data >= '" + Uteis.getDataJDBC(prmIni)
					+ "') and (AproveitamentoDisciplina.data <= '" + Uteis.getDataJDBC(prmFim)
					+ "')) and matricula is not null " + "AND unidadeEnsino.nome = '" + unidadeEnsino
					+ "' ORDER BY AproveitamentoDisciplina.data";
		}
		if (!matriculado) {
			sqlStr = "SELECT * FROM AproveitamentoDisciplina "
					+ "LEFT JOIN unidadeEnsino ON AproveitamentoDisciplina.unidadeEnsino = unidadeEnsino.codigo "
					+ "WHERE ((AproveitamentoDisciplina.data >= '" + Uteis.getDataJDBC(prmIni)
					+ "') and (AproveitamentoDisciplina.data <= '" + Uteis.getDataJDBC(prmFim)
					+ "')) and matricula is null " + "AND unidadeEnsino.nome = '" + unidadeEnsino
					+ "' ORDER BY AproveitamentoDisciplina.data";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados,  usuario));
	}

	public List consultarTodosTiposPorData(Date prmIni, Date prmFim, String unidadeEnsino, boolean controlarAcesso,
			int nivelMontarDados,  UsuarioVO usuario)
			throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM AproveitamentoDisciplina "
				+ "LEFT JOIN unidadeEnsino ON AproveitamentoDisciplina.unidadeEnsino = unidadeEnsino.codigo "
				+ "WHERE ((AproveitamentoDisciplina.data >= '" + Uteis.getDataJDBC(prmIni)
				+ "') and (AproveitamentoDisciplina.data <= '" + Uteis.getDataJDBC(prmFim) + "')) "
				+ "AND unidadeEnsino.nome = '" + unidadeEnsino + "' ORDER BY AproveitamentoDisciplina.data";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados,  usuario));
	}

	public List consultaRapidaPorCodigo(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso,
			UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE ad.codigo >= ").append(valorConsulta).append(" ");
		if (unidadeEnsino.intValue() != 0) {
			sql.append(" AND  (m.unidadeensino = ").append(unidadeEnsino).append(") ");
		}
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaBasica(resultado);
	}

	public List<DisciplinasAproveitadasVO> buscarDisciplinaSeremAproveitadaTransferenciaInterna(String matricula,
			Integer curso, UsuarioVO usuario ) throws Exception {
		List<DisciplinasAproveitadasVO> DisciplinasAproveitadasVOs = new ArrayList<DisciplinasAproveitadasVO>(0);
		List<HistoricoVO> historicoVOs = getFacadeFactory().getHistoricoFacade()
				.consultarHistoricoParaAproveitamentoNaTransferenciaInterna(matricula, curso,
						Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		adicionarDisciplinaSeremAproveitadaTransferenciaInterna(historicoVOs, DisciplinasAproveitadasVOs);
		return DisciplinasAproveitadasVOs;
	}

	public void adicionarDisciplinaSeremAproveitadaTransferenciaInterna(List<HistoricoVO> historicoVOs,
			List<DisciplinasAproveitadasVO> disciplinasAproveitadasVOs) throws Exception {
		for (HistoricoVO historicoVO : historicoVOs) {
			DisciplinasAproveitadasVO disciplinasAproveitadasVO = new DisciplinasAproveitadasVO();
			disciplinasAproveitadasVO.setDisciplina(historicoVO.getDisciplina());
			disciplinasAproveitadasVO.setFrequencia(historicoVO.getFreguencia());
			disciplinasAproveitadasVO.setNota(historicoVO.getMediaFinal());
			disciplinasAproveitadasVOs.add(disciplinasAproveitadasVO);
		}
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma
	 * consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>AproveitamentoDisciplinaVO</code> resultantes da consulta.
	 */
	public  List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados,
			 UsuarioVO usuario) throws Exception {
		List vetResultado = new ArrayList(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados,  usuario));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de dados
	 * (<code>ResultSet</code>) em um objeto da classe
	 * <code>AproveitamentoDisciplinaVO</code>.
	 * 
	 * @return O objeto da classe <code>AproveitamentoDisciplinaVO</code> com os
	 *         dados devidamente montados.
	 */
	public  AproveitamentoDisciplinaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados,
			 UsuarioVO usuario) throws Exception {
		AproveitamentoDisciplinaVO obj = new AproveitamentoDisciplinaVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setData(dadosSQL.getDate("data"));
		obj.getMatricula().setMatricula(dadosSQL.getString("matricula"));
		obj.getCodigoRequerimento().setCodigo(new Integer(dadosSQL.getInt("codigoRequerimento")));
		obj.getResponsavelAutorizacao().setCodigo(new Integer(dadosSQL.getInt("responsavelAutorizacao")));
		obj.getCurso().setCodigo(new Integer(dadosSQL.getInt("curso")));
		obj.getTurno().setCodigo(new Integer(dadosSQL.getInt("turno")));
		// obj.getTurma().setCodigo(new Integer(dadosSQL.getInt("turma")));
		obj.getGradeCurricular().setCodigo(new Integer(dadosSQL.getInt("gradecurricular")));
		obj.getPeridoLetivo().setCodigo(new Integer(dadosSQL.getInt("periodoLetivo")));
		obj.getPessoa().setCodigo(new Integer(dadosSQL.getInt("pessoa")));
		obj.getUnidadeEnsino().setCodigo(new Integer(dadosSQL.getInt("unidadeEnsino")));
		obj.getMatriculaPeriodo().setCodigo(new Integer(dadosSQL.getInt("matriculaPeriodo")));
		obj.setTipo(dadosSQL.getString("tipo"));
		obj.setAproveitamentoPrevisto(dadosSQL.getBoolean("aproveitamentoPrevisto"));
		obj.setInstituicao(dadosSQL.getString("instituicao"));
		obj.getCidadeVO().setCodigo(dadosSQL.getInt("cidade"));
		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSCONSULTA) {
			montarDadosRequerimento(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			montarDadosPessoa(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			montarDadosCurso(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			montarDadosCidade(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			return obj;
		}
		obj.setDisciplinasAproveitadasVOs(getFacadeFactory().getDisciplinaAproveitadasFacade()
				.consultarDisciplinasAproveitadass(obj.getCodigo(), nivelMontarDados, usuario));
		// obj.setConcessaoCreditoDisciplinaVOs(getFacadeFactory()
		// .getConcessaoCreditoDisciplinaFacade()
		// .consultarConcessaoCreditoDisciplinaPorAproveitamento(
		// obj.getCodigo(), nivelMontarDados, usuario));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS) {
			return obj;
		}
		montarDadosMatricula(obj, nivelMontarDados, usuario);
		montarDadosCurso(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		montarDadosTurno(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		montarDadosGradeCurricular(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		montarDadosPeriodoLetivo(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		montarDadosMatriculaPeriodo(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS,  usuario);
		montarDadosUnidadeEnsino(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		// montarDadosTurma(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
		montarDadosResponsavelAutorizacao(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosRequerimento(obj, nivelMontarDados, usuario);
		montarDadosPessoa(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		montarDadosCidade(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);

		return obj;
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>PessoaVO</code> relacionado ao objeto
	 * <code>AproveitamentoDisciplinaVO</code>. Faz uso da chave primária da classe
	 * <code>PessoaVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public  void montarDadosMatriculaPeriodo(AproveitamentoDisciplinaVO obj, int nivelMontarDados,
			 UsuarioVO usuario) throws Exception {
		if (obj.getMatriculaPeriodo().getCodigo().intValue() == 0) {
			return;
		}
		obj.setMatriculaPeriodo(getFacadeFactory().getMatriculaPeriodoFacade().consultarPorChavePrimaria(
				obj.getMatriculaPeriodo().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, 
				usuario));
	}

	public  void montarDadosUnidadeEnsino(AproveitamentoDisciplinaVO obj, int nivelMontarDados, UsuarioVO usuario)
			throws Exception {
		if (obj.getUnidadeEnsino().getCodigo().intValue() == 0) {
			return;
		}
		obj.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(
				obj.getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
	}

	public void montarDadosPessoa(AproveitamentoDisciplinaVO obj, int nivelMontarDados, UsuarioVO usuario)
			throws Exception {
		if (obj.getPessoa().getCodigo().intValue() == 0) {
			return;
		}
		obj.setPessoa(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(obj.getPessoa().getCodigo(),
				false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
	}

	public  void montarDadosResponsavelAutorizacao(AproveitamentoDisciplinaVO obj, int nivelMontarDados,
			UsuarioVO usuario) throws Exception {
		if (obj.getResponsavelAutorizacao().getCodigo().intValue() == 0) {
			return;
		}
		obj.setResponsavelAutorizacao(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(
				obj.getResponsavelAutorizacao().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario));
	}

	public  void montarDadosRequerimento(AproveitamentoDisciplinaVO obj, int nivelMontarDados, UsuarioVO usuario
			) throws Exception {
		if (obj.getCodigoRequerimento().getCodigo().intValue() == 0) {
			return;
		}
		obj.setCodigoRequerimento(getFacadeFactory().getRequerimentoFacade().consultarPorChavePrimaria(
				obj.getCodigoRequerimento().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario
				));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>MatriculaVO</code> relacionado ao objeto
	 * <code>AproveitamentoDisciplinaVO</code>. Faz uso da chave primária da classe
	 * <code>MatriculaVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public  void montarDadosMatricula(AproveitamentoDisciplinaVO obj, int nivelMontarDados, UsuarioVO usuario)
			throws Exception {
		if ((obj.getMatricula().getMatricula() == null) || (obj.getMatricula().getMatricula().equals(""))) {
			return;
		}
		obj.setMatricula(getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(
				obj.getMatricula().getMatricula(), 0, NivelMontarDados.getEnum(nivelMontarDados), usuario));
	}

	public  void montarDadosCurso(AproveitamentoDisciplinaVO obj, int nivelMontarDados, UsuarioVO usuario)
			throws Exception {
		if ((obj.getCurso().getCodigo() == null) || (obj.getCurso().getCodigo() == 0)) {
			return;
		}
		obj.setCurso(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(obj.getCurso().getCodigo(),
				nivelMontarDados, false, usuario));
	}

	public  void montarDadosCidade(AproveitamentoDisciplinaVO obj, int nivelMontarDados, UsuarioVO usuario)
			throws Exception {
		if ((obj.getCidadeVO().getCodigo() == null) || (obj.getCidadeVO().getCodigo() == 0)) {
			return;
		}
		obj.setCidadeVO(getFacadeFactory().getCidadeFacade().consultarPorChavePrimaria(obj.getCidadeVO().getCodigo(),
				false, usuario));
	}

	public  void montarDadosTurno(AproveitamentoDisciplinaVO obj, int nivelMontarDados, UsuarioVO usuario)
			throws Exception {
		if ((obj.getTurno().getCodigo() == null) || (obj.getTurno().getCodigo() == 0)) {
			return;
		}
		obj.setTurno(getFacadeFactory().getTurnoFacade().consultarPorChavePrimaria(obj.getTurno().getCodigo(),
				Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
	}

	// public static void montarDadosTurma(AproveitamentoDisciplinaVO obj, int
	// nivelMontarDados) throws Exception {
	// if ((obj.getTurma().getCodigo() == null) || (obj.getTurma().getCodigo()
	// == 0)) {
	// return;
	// }
	// obj.setTurma(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(obj.getTurma().getCodigo(),
	// nivelMontarDados));
	// }
	public  void montarDadosGradeCurricular(AproveitamentoDisciplinaVO obj, int nivelMontarDados,
			UsuarioVO usuario) throws Exception {
		if ((obj.getGradeCurricular().getCodigo() == null) || (obj.getGradeCurricular().getCodigo() == 0)) {
			return;
		}
		obj.setGradeCurricular(getFacadeFactory().getGradeCurricularFacade()
				.consultarPorChavePrimaria(obj.getGradeCurricular().getCodigo(), nivelMontarDados, usuario));
	}

	public  void montarDadosPeriodoLetivo(AproveitamentoDisciplinaVO obj, int nivelMontarDados, UsuarioVO usuario)
			throws Exception {
		if ((obj.getPeridoLetivo().getCodigo() == null) || (obj.getPeridoLetivo().getCodigo() == 0)) {
			return;
		}
		obj.setPeridoLetivo(getFacadeFactory().getPeriodoLetivoFacade().consultarPorChavePrimaria(
				obj.getPeridoLetivo().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
	}

	/**
	 * Operação responsável por localizar um objeto da classe
	 * <code>AproveitamentoDisciplinaVO</code> através de sua chave primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto
	 *                procurado.
	 */
	public AproveitamentoDisciplinaVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados,
			 UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM AproveitamentoDisciplina WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return (montarDados(tabelaResultado, nivelMontarDados,  usuario));
	}

	public AproveitamentoDisciplinaVO consultarPorCodigoRequerimento(Integer codigoPrm, int nivelMontarDados,
			 UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT AproveitamentoDisciplina.* FROM AproveitamentoDisciplina INNER JOIN REQUERIMENTO ON REQUERIMENTO.CODIGO = APROVEITAMENTODISCIPLINA.CODIGOREQUERIMENTO WHERE REQUERIMENTO.codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			return new AproveitamentoDisciplinaVO();
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as operações
	 * desta classe.
	 */
	public static String getIdEntidade() {
		if (AproveitamentoDisciplina.idEntidade == null) {
			AproveitamentoDisciplina.idEntidade = "AproveitamentoDisciplina";
		}
		return AproveitamentoDisciplina.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de negócio
	 * pode ser utilizada com objetivos distintos. Assim ao se verificar que Como o
	 * controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		AproveitamentoDisciplina.idEntidade = idEntidade;
	}

	private StringBuffer getSQLPadraoConsultaBasica() {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT DISTINCT ad.codigo, m.matricula, t.identificadorturma, req.codigo as codigorequerimento, ad.tipo as tipo, ");
		sql.append(" p.codigo as \"p.codigo\", p.nome as \"p.nome\", p.registroAcademico as \"p.registroAcademico\" ,  c.codigo AS \"curso.codigo\", c.nome AS \"curso.nome\",  ad.data AS \"data\", u.nome AS \"responsavelautorizacao\" ");
		sql.append("FROM aproveitamentodisciplina ad ");
		sql.append("LEFT JOIN matricula m ON m.matricula = ad.matricula ");
		sql.append("LEFT JOIN pessoa p ON p.codigo = ad.pessoa ");
		sql.append("LEFT JOIN matriculaperiodo mp ON mp.codigo = ad.matriculaperiodo ");
		sql.append("LEFT JOIN turma t ON t.codigo = mp.turma ");
		sql.append("LEFT JOIN requerimento req ON req.codigo = ad.codigorequerimento ");
		sql.append("LEFT JOIN curso c ON c.codigo = ad.curso ");

		sql.append("LEFT JOIN usuario u ON u.codigo = ad.responsavelautorizacao ");
		
		return sql;
	}

	private void montarDadosBasico(AproveitamentoDisciplinaVO obj, SqlRowSet dadosSQL) {
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.getMatricula().setMatricula(dadosSQL.getString("matricula"));
		obj.getPessoa().setCodigo(dadosSQL.getInt("p.codigo"));
		obj.getPessoa().setNome(dadosSQL.getString("p.nome"));
		obj.getPessoa().setRegistroAcademico(dadosSQL.getString("p.registroAcademico"));
		obj.setTurma(dadosSQL.getString("identificadorturma"));
		obj.getCodigoRequerimento().setCodigo(dadosSQL.getInt("codigorequerimento"));
		obj.setTipo(dadosSQL.getString("tipo"));
		obj.getCurso().setCodigo(dadosSQL.getInt("curso.codigo"));
		obj.getCurso().setNome(dadosSQL.getString("curso.nome"));
		obj.setData(dadosSQL.getDate("data"));
		obj.getResponsavelAutorizacao().setNome(dadosSQL.getString("responsavelautorizacao"));
	}

	public List<AproveitamentoDisciplinaVO> montarDadosConsultaBasica(SqlRowSet tabelaResultado) throws Exception {
		List<AproveitamentoDisciplinaVO> vetResultado = new ArrayList<AproveitamentoDisciplinaVO>(0);
		while (tabelaResultado.next()) {
			AproveitamentoDisciplinaVO obj = new AproveitamentoDisciplinaVO();
			montarDadosBasico(obj, tabelaResultado);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	public SqlRowSet consultaRapidaPorChavePrimariaDadosBasicos(Integer codigoPrm, boolean controlarAcesso,
			UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE (ad.codigo = ?)");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(),
				new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( Aproveitamento Disciplina ).");
		}
		tabelaResultado.beforeFirst();
		return tabelaResultado;
	}

	public void inicializarDadosAdicionarConcessaoCreditoDisciplina(AproveitamentoDisciplinaVO obj,
			 UsuarioVO usuarioLogado) throws Exception {
//		if (obj.getCodigo() != 0) {
//			concessaoCreditoDisciplinaVO.setAproveitamentoDisciplinaVO(obj);
//		}
//		if (concessaoCreditoDisciplinaVO.getDisciplinaVO().getCodigo() != 0) {
//			concessaoCreditoDisciplinaVO.setDisciplinaVO(getFacadeFactory().getDisciplinaFacade()
//					.consultarPorChavePrimaria(concessaoCreditoDisciplinaVO.getDisciplinaVO().getCodigo(),
//							Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioLogado));
//		}
	}

	public void adicionarConcessaoCreditoDisciplina(AproveitamentoDisciplinaVO obj,
			 UsuarioVO usuarioLogado) throws Exception {
		inicializarDadosAdicionarConcessaoCreditoDisciplina(obj, usuarioLogado);

//		getFacadeFactory().getConcessaoCreditoDisciplinaFacade()
//				.validarDadosAdicionarAproveitamento(concessaoCreditoDisciplinaVO);
//		int index = 0;
//		Iterator i = obj.getConcessaoCreditoDisciplinaVOs().iterator();
//		while (i.hasNext()) {
//			ConcessaoCreditoDisciplinaVO objExistente = (ConcessaoCreditoDisciplinaVO) i.next();
//			if (objExistente.getDisciplinaVO().getCodigo()
//					.equals(concessaoCreditoDisciplinaVO.getDisciplinaVO().getCodigo())) {
//				obj.getConcessaoCreditoDisciplinaVOs().set(index, concessaoCreditoDisciplinaVO);
//				return;
//			}
//			index++;
//		}
//		obj.getConcessaoCreditoDisciplinaVOs().add(concessaoCreditoDisciplinaVO);
	}

	@SuppressWarnings("element-type-mismatch")
	public void excluirObjConcessaoCreditoDisciplinaVOs(AproveitamentoDisciplinaVO obj, Integer disciplina)
			throws Exception {
//		int index = 0;
//		Iterator i = obj.getConcessaoCreditoDisciplinaVOs().iterator();
//		while (i.hasNext()) {
//			ConcessaoCreditoDisciplinaVO objExistente = (ConcessaoCreditoDisciplinaVO) i.next();
//			if (objExistente.getDisciplinaVO().getCodigo().equals(disciplina)) {
//				obj.getConcessaoCreditoDisciplinaVOs().remove(index);
//				return;
//			}
//			index++;
//		}
	}

	public void inicializarDadosAdicionarConcessaoCargaHorariaDisciplina(AproveitamentoDisciplinaVO obj,
			 UsuarioVO usuarioLogado)
			throws Exception {
//		if (obj.getCodigo() != 0) {
//			concessaoCargaHorariaDisciplinaVO.setAproveitamentoDisciplinaVO(obj);
//		}
//		if (concessaoCargaHorariaDisciplinaVO.getDisciplinaVO().getCodigo() != 0) {
//			concessaoCargaHorariaDisciplinaVO.setDisciplinaVO(getFacadeFactory().getDisciplinaFacade()
//					.consultarPorChavePrimaria(concessaoCargaHorariaDisciplinaVO.getDisciplinaVO().getCodigo(),
//							Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioLogado));
//		}
	}

	public void adicionarConcessaoCargaHorariaDisciplina(AproveitamentoDisciplinaVO obj,
			 UsuarioVO usuarioLogado)
			throws Exception {
//		inicializarDadosAdicionarConcessaoCargaHorariaDisciplina(obj, concessaoCargaHorariaDisciplinaVO, usuarioLogado);
//
//		getFacadeFactory().getConcessaoCargaHorariaDisciplinaFacade()
//				.validarDadosAdicionarAproveitamento(concessaoCargaHorariaDisciplinaVO);
//		int index = 0;
//		Iterator i = obj.getConcessaoCargaHorariaDisciplinaVOs().iterator();
//		while (i.hasNext()) {
//			ConcessaoCargaHorariaDisciplinaVO objExistente = (ConcessaoCargaHorariaDisciplinaVO) i.next();
//			if (objExistente.getDisciplinaVO().getCodigo()
//					.equals(concessaoCargaHorariaDisciplinaVO.getDisciplinaVO().getCodigo())) {
//				obj.getConcessaoCargaHorariaDisciplinaVOs().set(index, concessaoCargaHorariaDisciplinaVO);
//				return;
//			}
//			index++;
//		}
//		obj.getConcessaoCargaHorariaDisciplinaVOs().add(concessaoCargaHorariaDisciplinaVO);
	}

	@SuppressWarnings("element-type-mismatch")
	public void excluirObjConcessaoCargaHorariaDisciplinaVOs(AproveitamentoDisciplinaVO obj, Integer disciplina)
			throws Exception {
//		int index = 0;
//
//		Iterator i = obj.getConcessaoCargaHorariaDisciplinaVOs().iterator();
//		while (i.hasNext()) {
//			ConcessaoCargaHorariaDisciplinaVO objExistente = (ConcessaoCargaHorariaDisciplinaVO) i.next();
//			if (objExistente.getDisciplinaVO().getCodigo().equals(disciplina)) {
//				obj.getConcessaoCargaHorariaDisciplinaVOs().remove(index);
//				return;
//			}
//			index++;
//		}
	}

	public Boolean realizarVerificacaoExistenciaCursandoDisciplinaHistoricoAluno(String matricula,
			DisciplinaVO disciplinaVO, UsuarioVO usuarioVO) throws Exception {
		Boolean disciplinaAprovadaExistenteHistorico = getFacadeFactory().getHistoricoFacade()
				.consultarPorMatriculaDisciplinaSituacao(matricula, disciplinaVO.getCodigo(),
						SituacaoHistorico.APROVADO.getValor(), usuarioVO);
		if (disciplinaAprovadaExistenteHistorico) {
			throw new ConsistirException("Não é possível realizar um aproveitamento para uma disciplina Aprovada.");
		}
		Boolean disciplinaAprovadaAproveitamentoExistenteHistorico = getFacadeFactory().getHistoricoFacade()
				.consultarPorMatriculaDisciplinaSituacao(matricula, disciplinaVO.getCodigo(),
						SituacaoHistorico.APROVADO_APROVEITAMENTO.getValor(), usuarioVO);
		if (disciplinaAprovadaAproveitamentoExistenteHistorico) {
			throw new ConsistirException(
					"Não é possível realizar um aproveitamento para uma disciplina que esteja com a situação Aprovada por Aproveitamento.");
		}
		return getFacadeFactory().getHistoricoFacade().consultarPorMatriculaDisciplinaSituacao(matricula,
				disciplinaVO.getCodigo(), SituacaoHistorico.CURSANDO.getValor(), usuarioVO);
	}

	/**
	 * Responsavel por preparar um único mapa de equivalencia para ser apresentado.
	 * Ao final da apresentação do mesmo. Caso algum aproveitamento tenha sido
	 * registrado para o mesmo, este aproveitamento e armazenado em uma lista do
	 * objeto aproveitamentoDisciplinaVO para persitência posterior (lista:
	 * aproveitamentoDisciplinaVO.getDisciplinasAproveitadasForaDaGradeMapaEquivalencia())
	 */
	public void realizarMontagemPainelMapaEquivalenciaDisciplinasAproveitadas(
			AproveitamentoDisciplinaVO aproveitamentoDisciplinaVO, MapaEquivalenciaDisciplinaVO mapaEquivalencia,
			UsuarioVO usuarioVO) throws Exception {
		for (MapaEquivalenciaDisciplinaCursadaVO mapaEquivalenciaDisciplinaCursadaVO : aproveitamentoDisciplinaVO
				.getDisciplinasAproveitadasForaDaGradeMapaEquivalencia()) {
			DisciplinasAproveitadasVO disciplinasAproveitadasVO = mapaEquivalenciaDisciplinaCursadaVO
					.getDisciplinasAproveitadasVO();
			if (disciplinasAproveitadasVO.getDisciplinaForaGrade()) {

				// Para cada disciplina aproveitada fora da grade, temos que verificar se a
				// mesma trata-se de uma disciplina
				// cursada do mapa de equivalencia. Caso seja, iremos vincular o aproveitamento
				// a disciplina cursada para que
				// o mesmo possa ser visto e editado pelo usuário
				for (MapaEquivalenciaDisciplinaCursadaVO mapaEquivalenciaAtualizarDisciplinaCursadaVO : mapaEquivalencia
						.getMapaEquivalenciaDisciplinaCursadaVOs()) {
					if ((disciplinasAproveitadasVO.getDisciplina().getCodigo()
							.equals(mapaEquivalenciaAtualizarDisciplinaCursadaVO.getDisciplinaVO().getCodigo()))
							&& (disciplinasAproveitadasVO.getCargaHoraria()
									.equals(mapaEquivalenciaAtualizarDisciplinaCursadaVO.getCargaHoraria()))) {
						// temos que testar tambem a carga horaria, pois pode existir aproveitamento da
						// mesma disciplina (mesmo codigo)
						// de cursos diferentes.
						mapaEquivalenciaAtualizarDisciplinaCursadaVO
								.setDisciplinasAproveitadasVO(disciplinasAproveitadasVO);
						mapaEquivalenciaAtualizarDisciplinaCursadaVO.setSelecionadoAproveitamento(Boolean.TRUE);
						break;
					}
				}
			}
		}
	}

	/**
	 * Realiza a montagem das disciplinas no momento de editar o obj. Autor Carlos.
	 * Se for fornecdido o parametro aproveitamentoDisciplinasEntreMatriculasVO,
	 * significa que serão montados somente os aproveitamentos de um
	 * AproveitamentoDisciplinasEntreMatriculasVO (que é quando o usuário faz o
	 * aproveitamento de disciplinas de uma outra matrícula do aluno). Esta opção é
	 * importante para impedir que ao montar dados de um
	 * AproveitamentoDisciplinasEntreMatriculasVO dados que já tenham sido editados
	 * pelo usuário possam ser perdidos e / ou alterado de forma indevida.
	 */
	public void realizarMontagemPainelMatrizCurricularComDisciplinasAproveitadas(AproveitamentoDisciplinaVO aproveitamentoDisciplinaVO, AproveitamentoDisciplinasEntreMatriculasVO aproveitamentoDisciplinasEntreMatriculasVO, UsuarioVO usuarioVO)throws Exception {
		List<DisciplinasAproveitadasVO> disciplinasAproveitadasVOs = aproveitamentoDisciplinaVO.getDisciplinasAproveitadasVOs();
		boolean montarSomenteDisciplinasAproveitamentoEntreMatriculas = false;
		if ((aproveitamentoDisciplinasEntreMatriculasVO != null) && (!aproveitamentoDisciplinasEntreMatriculasVO.getMatriculaOrigemAproveitamentoVO().getMatricula().equals(""))) {
			montarSomenteDisciplinasAproveitamentoEntreMatriculas = true;
		}
		MatriculaVO matriculaVO = aproveitamentoDisciplinaVO.getMatricula();
		GradeCurricularVO gradeCurricularVO = aproveitamentoDisciplinaVO.getGradeCurricular();
		// montando históricos e aproveitamentos da gradeDisciplina
		for (PeriodoLetivoVO periodoLetivo : gradeCurricularVO.getPeriodoLetivosVOs()) {
			for (GradeDisciplinaVO gradeDisciplina : periodoLetivo.getGradeDisciplinaVOs()) {
				PeriodoLetivoComHistoricoAlunoVO periodoLetivoComHistoricoAlunoVO = matriculaVO
						.getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO()
						.getPeriodoLetivoComHistoricoAlunoVOPorCodigo(periodoLetivo.getCodigo());
				if (!montarSomenteDisciplinasAproveitamentoEntreMatriculas) {
					HistoricoVO historico = periodoLetivoComHistoricoAlunoVO
							.obterHistoricoAtualGradeDisciplinaVO(gradeDisciplina.getCodigo());
					gradeDisciplina.setHistoricoAtualAluno(historico);
				}
				int i = 10;
				if (gradeDisciplina.getDisciplina().getCodigo().equals(1000)) {
					i = i + 12;
				}
				for (DisciplinasAproveitadasVO disciplinasAproveitadasVO : disciplinasAproveitadasVOs) {
					if (disciplinasAproveitadasVO.getPeriodoletivoGrupoOptativaVO().getCodigo().equals(0)) {
						// discipliinas da GradeDisciplina
						if (disciplinasAproveitadasVO.getDisciplina().getCodigo()
								.equals(gradeDisciplina.getDisciplina().getCodigo())) {
							if (montarSomenteDisciplinasAproveitamentoEntreMatriculas) {
								// ira entrar aqui somente se o metodo foi chamado para montar somente os
								// aproveitamento gerados
								// por um aproveitamento entre matriculas. Abaixo é testado para garantir que
								// somente os aproveitamentos
								// da matricula origem correta sejam montados. Os demais permanecem inalterados
								// (pois podem ja conter
								// dados lancados pelos usuarios).
								if (disciplinasAproveitadasVO.getMatriculaOrigemAproveitamentoEntreMatriculas()
										.getMatricula().equals(aproveitamentoDisciplinasEntreMatriculasVO
												.getMatriculaOrigemAproveitamentoVO().getMatricula())) {
									gradeDisciplina.setDisciplinasAproveitadasVO(disciplinasAproveitadasVO);
									gradeDisciplina.setSelecionadoAproveitamento(Boolean.TRUE);
									break;
								}
							} else {
								gradeDisciplina.setDisciplinasAproveitadasVO(disciplinasAproveitadasVO);
								gradeDisciplina.setSelecionadoAproveitamento(Boolean.TRUE);
								break;
							}
						}
					}
				}
			}
		}

		// MONTANDO HISTORICOS APROVEITAMENTOS GRUPOOPTATIVA
		for (PeriodoLetivoVO periodoLetivo : gradeCurricularVO.getPeriodoLetivosVOs()) {
			if (periodoLetivo.getControleOptativaGrupo()) {
				if (periodoLetivo.getGradeCurricularGrupoOptativa().getGradeCurricularGrupoOptativaDisciplinaVOs()
						.isEmpty()) {
					periodoLetivo.setGradeCurricularGrupoOptativa(
							getFacadeFactory().getGradeCurricularGrupoOptativaFacade().consultarPorChavePrimaria(
									periodoLetivo.getGradeCurricularGrupoOptativa().getCodigo(), NivelMontarDados.TODOS,
									usuarioVO));
				}
				for (GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO : periodoLetivo
						.getGradeCurricularGrupoOptativa().getGradeCurricularGrupoOptativaDisciplinaVOs()) {
					// montando os historicos dos mesmos
					// como um grupo de optativa pode ser o mesmo para vários períodos letivos,
					// significa que uma disciplina
					// que foi cursada no 10 período, também está presente como optativa para o 01
					// periodo. Por isto,
					// ao buscarmos um histórico para a mesma, temos que buscar chamar um método que
					// busca a mesma através
					// de qualquer periodo letivo. Que caso é o metodo
					if (!montarSomenteDisciplinasAproveitamentoEntreMatriculas) {
						HistoricoVO historico = matriculaVO.getMatriculaComHistoricoAlunoVO()
								.getGradeCurricularComHistoricoAlunoVO()
								.obterHistoricoAtualGradeCurricularGrupoOptativaVO(
										gradeCurricularGrupoOptativaDisciplinaVO.getDisciplina().getCodigo(),
										gradeCurricularGrupoOptativaDisciplinaVO.getCargaHoraria());
						historico.getSituacao(); // chamando método para forcar inicializacao da situacao do historico,
													// para historicos que vierem em branco.
						gradeCurricularGrupoOptativaDisciplinaVO.setHistoricoAtualAluno(historico);
					}
					// verificando se ha aproveitamento para montá-los também
					for (DisciplinasAproveitadasVO disciplinasAproveitadasVO : disciplinasAproveitadasVOs) {
						if (!disciplinasAproveitadasVO.getPeriodoletivoGrupoOptativaVO().getCodigo().equals(0)) {
							if (gradeCurricularGrupoOptativaDisciplinaVO.getDisciplina().getCodigo()
									.equals(disciplinasAproveitadasVO.getDisciplina().getCodigo())) {
								if (montarSomenteDisciplinasAproveitamentoEntreMatriculas) {
									// ira entrar aqui somente se o metodo foi chamado para montar somente os
									// aproveitamento gerados
									// por um aproveitamento entre matriculas. Abaixo é testado para garantir que
									// somente os aproveitamentos
									// da matricula origem correta sejam montados. Os demais permanecem inalterados
									// (pois podem ja conter
									// dados lancados pelos usuarios).
									if (disciplinasAproveitadasVO.getMatriculaOrigemAproveitamentoEntreMatriculas()
											.getMatricula().equals(aproveitamentoDisciplinasEntreMatriculasVO
													.getMatriculaOrigemAproveitamentoVO().getMatricula())) {
										gradeCurricularGrupoOptativaDisciplinaVO
												.setDisciplinasAproveitadasVO(disciplinasAproveitadasVO);
										gradeCurricularGrupoOptativaDisciplinaVO
												.setSelecionadoAproveitamento(Boolean.TRUE);
										break;
									}
								} else {
									gradeCurricularGrupoOptativaDisciplinaVO
											.setDisciplinasAproveitadasVO(disciplinasAproveitadasVO);
									gradeCurricularGrupoOptativaDisciplinaVO.setSelecionadoAproveitamento(Boolean.TRUE);
									break;
								}
							}
						}
					}
				}
			}
		}

		// MONTANDO HISTORICOS APROVEITAMENTOS FORA DA GRADE
		for (DisciplinasAproveitadasVO disciplinasAproveitadasVO : disciplinasAproveitadasVOs) {
			if (disciplinasAproveitadasVO.getDisciplinaForaGrade()) {
				boolean montarMapaEquivalencia = true;
				if (montarSomenteDisciplinasAproveitamentoEntreMatriculas) {
					// ira entrar aqui somente se o metodo foi chamado para montar somente os
					// aproveitamento gerados
					// por um aproveitamento entre matriculas. Abaixo é testado para garantir que
					// somente os aproveitamentos
					// da matricula origem correta sejam montados. Os demais permanecem inalterados
					// (pois podem ja conter
					// dados lancados pelos usuarios).
					if (!disciplinasAproveitadasVO.getMatriculaOrigemAproveitamentoEntreMatriculas().getMatricula()
							.equals(aproveitamentoDisciplinasEntreMatriculasVO.getMatriculaOrigemAproveitamentoVO()
									.getMatricula())) {
						montarMapaEquivalencia = false;
					}
				}
				if (montarMapaEquivalencia) {
					MapaEquivalenciaDisciplinaCursadaVO mapaRegistro = getFacadeFactory()
							.getMapaEquivalenciaDisciplinaCursadaFacade().consultarPorChavePrimaria(
									disciplinasAproveitadasVO.getMapaEquivalenciaDisciplinaCursada().getCodigo());
					mapaRegistro.setDisciplinasAproveitadasVO(disciplinasAproveitadasVO);
					// Montando o historico da disciplina Cursada por equivalencia, para que no
					// alterar deste dados.
					// Caso tenha historico, o mesmo já seja atualizado também.
					aproveitamentoDisciplinaVO.getMatricula().getMatriculaComHistoricoAlunoVO()
							.getGradeCurricularComHistoricoAlunoVO()
							.atualizarSituacaoMapaEquivalenciaDisciplinaCursadaVOAluno(mapaRegistro);
					aproveitamentoDisciplinaVO.getDisciplinasAproveitadasForaDaGradeMapaEquivalencia()
							.add(mapaRegistro);
				}
			}
		}
	}

	public void veriricarJaExisteAproveitamentoDisciplinaGrupoOptativa(
			AproveitamentoDisciplinaVO aproveitamentoDisciplinaVO, GradeCurricularGrupoOptativaDisciplinaVO objValidar)
			throws Exception {
		GradeCurricularVO gradeCurricularVO = aproveitamentoDisciplinaVO.getGradeCurricular();

		for (PeriodoLetivoVO periodoLetivo : gradeCurricularVO.getPeriodoLetivosVOs()) {
			if (periodoLetivo.getControleOptativaGrupo()) {
				for (GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO : periodoLetivo
						.getGradeCurricularGrupoOptativa().getGradeCurricularGrupoOptativaDisciplinaVOs()) {
					// montando os historicos dos mesmos
					if ((gradeCurricularGrupoOptativaDisciplinaVO.getDisciplina().getCodigo()
							.equals(objValidar.getDisciplina().getCodigo()))
							&& (gradeCurricularGrupoOptativaDisciplinaVO.getCargaHoraria()
									.equals(objValidar.getCargaHoraria()))) {
						if (gradeCurricularGrupoOptativaDisciplinaVO.getSelecionadoAproveitamento()) {
							throw new Exception(
									"Já existe um aproveitamento registrado para esta disciplina no período "
											+ periodoLetivo.getDescricao());
						}
					}
				}
			}
		}
	}

	/**
	 * Método capaz de gerar históricos (com os dados devidamente inicializados)
	 * para cada disciplinaAproveitadasVO. Este método precisa chamar o método
	 * realizarMontagemListaDisciplinasAproveitadas, pois este método é que monta o
	 * vincula de cada disciplinaAproveitadaVO com sua respectiva GradeDisciplina
	 * e/ou GradeCurricularGrupoOptativa.
	 * 
	 * @param aproveitamentoDisciplinaVO
	 * @param usuario
	 * @return
	 * @throws Exception
	 */
	public List<HistoricoVO> gerarHistoricosPrevistosDisciplinasAproveitadas(
			AproveitamentoDisciplinaVO aproveitamentoDisciplinaVO, UsuarioVO usuario) throws Exception {
		List<HistoricoVO> listaHistoricoRetornar = new ArrayList();
		for (DisciplinasAproveitadasVO disciplinasAproveitadasVO : aproveitamentoDisciplinaVO
				.getDisciplinasAproveitadasVOs()) {
			HistoricoVO historivoVO = getFacadeFactory().getHistoricoFacade()
					.criarHistoricoAPartirDisciplinaAproveitada(disciplinasAproveitadasVO);
			listaHistoricoRetornar.add(historivoVO);
		}
		return listaHistoricoRetornar;
	}

	/**
	 * Reliaza a montagem do Obj DisciplinasAproveitadas para realizar a inserção no
	 * banco de dados. Este método é importante, pois gera o vinculo da
	 * DisciplinaAproveitada com os objetos GradeDisciplina e
	 * GradeCurricularDisciplinaGrupoOptativa
	 */
	public void realizarMontagemListaDisciplinasAproveitadas(AproveitamentoDisciplinaVO aproveitamentoDisciplinaVO,
			String instituicao, CidadeVO cidadeVO, UsuarioVO usuarioVO) throws Exception {
		aproveitamentoDisciplinaVO.getDisciplinasAproveitadasVOs().clear();
		for (PeriodoLetivoVO periodoLetivoVO : aproveitamentoDisciplinaVO.getGradeCurricular().getPeriodoLetivosVOs()) {
			for (GradeDisciplinaVO gradeDisciplinaVO : periodoLetivoVO.getGradeDisciplinaVOs()) {
				if (gradeDisciplinaVO.getSelecionadoAproveitamento()) {
					DisciplinasAproveitadasVO.validarDados(gradeDisciplinaVO.getDisciplinasAproveitadasVO(),
							aproveitamentoDisciplinaVO.getCurso().getPeriodicidade(),
							aproveitamentoDisciplinaVO.getCurso().getConfiguracaoAcademico()
									.getPercMinimoCargaHorariaDisciplinaParaAproveitamento());
					if (!aproveitamentoDisciplinaVO.getCurso().getNivelEducacionalPosGraduacao()) {
						DisciplinasAproveitadasVO.validarAnoSemestreAproveitamentoDisciplina(
								gradeDisciplinaVO.getDisciplinasAproveitadasVO(),
								gradeDisciplinaVO.getHistoricoAtualAluno());
					}
					inicializarDadosDisciplinasAproveitadas(aproveitamentoDisciplinaVO, gradeDisciplinaVO,
							gradeDisciplinaVO.getDisciplinasAproveitadasVO(), instituicao, cidadeVO, usuarioVO);
					aproveitamentoDisciplinaVO
							.adicionarObjDisciplinasAproveitadasVOs(gradeDisciplinaVO.getDisciplinasAproveitadasVO());
				}
			}
			if (periodoLetivoVO.getControleOptativaGrupo()) {
				for (GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO : periodoLetivoVO
						.getGradeCurricularGrupoOptativa().getGradeCurricularGrupoOptativaDisciplinaVOs()) {
					if ((gradeCurricularGrupoOptativaDisciplinaVO.getSelecionadoAproveitamento())
							&& (gradeCurricularGrupoOptativaDisciplinaVO.getDisciplinasAproveitadasVO()
									.getPeriodoletivoGrupoOptativaVO().getCodigo()
									.equals(periodoLetivoVO.getCodigo()))) {
						DisciplinasAproveitadasVO.validarDados(
								gradeCurricularGrupoOptativaDisciplinaVO.getDisciplinasAproveitadasVO(),
								aproveitamentoDisciplinaVO.getCurso().getPeriodicidade(),
								aproveitamentoDisciplinaVO.getCurso().getConfiguracaoAcademico()
										.getPercMinimoCargaHorariaDisciplinaParaAproveitamento());
						if (!aproveitamentoDisciplinaVO.getCurso().getNivelEducacionalPosGraduacao()) {
							DisciplinasAproveitadasVO.validarAnoSemestreAproveitamentoDisciplina(
									gradeCurricularGrupoOptativaDisciplinaVO.getDisciplinasAproveitadasVO(),
									gradeCurricularGrupoOptativaDisciplinaVO.getHistoricoAtualAluno());
						}
						inicializarDadosDisciplinasAproveitadasGrupoOptativa(aproveitamentoDisciplinaVO,
								periodoLetivoVO, gradeCurricularGrupoOptativaDisciplinaVO, instituicao, cidadeVO,
								usuarioVO);
						aproveitamentoDisciplinaVO.adicionarObjDisciplinasAproveitadasVOs(
								gradeCurricularGrupoOptativaDisciplinaVO.getDisciplinasAproveitadasVO());
					}
				}
			}
		}

		for (MapaEquivalenciaDisciplinaCursadaVO mapaCursadaForaGrade : aproveitamentoDisciplinaVO
				.getDisciplinasAproveitadasForaDaGradeMapaEquivalencia()) {
			DisciplinasAproveitadasVO.validarDados(mapaCursadaForaGrade.getDisciplinasAproveitadasVO(),
					aproveitamentoDisciplinaVO.getCurso().getPeriodicidade(), aproveitamentoDisciplinaVO.getCurso()
							.getConfiguracaoAcademico().getPercMinimoCargaHorariaDisciplinaParaAproveitamento());
			if (!aproveitamentoDisciplinaVO.getCurso().getNivelEducacionalPosGraduacao()) {
				DisciplinasAproveitadasVO.validarAnoSemestreAproveitamentoDisciplina(
						mapaCursadaForaGrade.getDisciplinasAproveitadasVO(), mapaCursadaForaGrade.getHistorico());
			}
			inicializarDadosDisciplinasAproveitadasForaGrade(aproveitamentoDisciplinaVO, mapaCursadaForaGrade,
					instituicao, cidadeVO, usuarioVO);
			aproveitamentoDisciplinaVO
					.adicionarObjDisciplinasAproveitadasVOs(mapaCursadaForaGrade.getDisciplinasAproveitadasVO());
		}
	}

	public void inicializarDadosDisciplinasAproveitadas(AproveitamentoDisciplinaVO aproveitamentoDisciplinaVO,
			GradeDisciplinaVO gradeDisciplinaVO, DisciplinasAproveitadasVO disciplinasAproveitadasVO,
			String instituicao, CidadeVO cidadeVO, UsuarioVO usuarioVO) throws Exception {
		if (!aproveitamentoDisciplinaVO.getCodigo().equals(0)) {
			disciplinasAproveitadasVO.setAproveitamentoDisciplina(aproveitamentoDisciplinaVO.getCodigo());
		}
		if (disciplinasAproveitadasVO.getFrequencia() == null
				|| disciplinasAproveitadasVO.getFrequencia().equals(0.0)) {
			disciplinasAproveitadasVO.setFrequencia(100.00);
		}
		// if (disciplinasAproveitadasVO.getUtilizaNotaConceito() &&
		// disciplinasAproveitadasVO.getMediaFinalConceito().getCodigo() > 0) {
		// disciplinasAproveitadasVO.setMediaFinalConceito(getFacadeFactory().getConfiguracaoAcademicoNotaConceitoFacade().consultarPorChavePrimaria(disciplinasAproveitadasVO.getMediaFinalConceito().getCodigo()));
		// }
		if (!gradeDisciplinaVO.getCodigo().equals(0)) {
			disciplinasAproveitadasVO.setGradeDisciplinaVO(gradeDisciplinaVO);
			disciplinasAproveitadasVO.setPeriodoletivoGrupoOptativaVO(null);
		}
		if (gradeDisciplinaVO.getHistoricoAtualAluno().getCursando()) {
			// se está cursando é por que já existe um histórico gerado para a disciplina
			// logo o mesmo deverá ser excluído pelo sistema, para que seja gerado outro
			// histórico
			disciplinasAproveitadasVO.setExcluirHistoricoDisciplinaCursada(true);
		}
		disciplinasAproveitadasVO.setHistoricoAtual(gradeDisciplinaVO.getHistoricoAtualAluno());
	}

	public void inicializarDadosDisciplinasAproveitadasGrupoOptativa(
			AproveitamentoDisciplinaVO aproveitamentoDisciplinaVO, PeriodoLetivoVO periodoLetivoGrupoOptativaVO,
			GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO, String instituicao,
			CidadeVO cidadeVO, UsuarioVO usuarioVO) throws Exception {
		DisciplinasAproveitadasVO disciplinasAproveitadasVO = gradeCurricularGrupoOptativaDisciplinaVO
				.getDisciplinasAproveitadasVO();
		if (!aproveitamentoDisciplinaVO.getCodigo().equals(0)) {
			disciplinasAproveitadasVO.setAproveitamentoDisciplina(aproveitamentoDisciplinaVO.getCodigo());
		}
		if (disciplinasAproveitadasVO.getFrequencia() == null
				|| disciplinasAproveitadasVO.getFrequencia().equals(0.0)) {
			disciplinasAproveitadasVO.setFrequencia(100.00);
		}

		// if (disciplinasAproveitadasVO.getUtilizaNotaConceito() &&
		// disciplinasAproveitadasVO.getMediaFinalConceito().getCodigo() > 0) {
		// disciplinasAproveitadasVO.setMediaFinalConceito(getFacadeFactory().getConfiguracaoAcademicoNotaConceitoFacade().consultarPorChavePrimaria(disciplinasAproveitadasVO.getMediaFinalConceito().getCodigo()));
		// }
		if (!periodoLetivoGrupoOptativaVO.getCodigo().equals(0)) {
			disciplinasAproveitadasVO.setPeriodoletivoGrupoOptativaVO(periodoLetivoGrupoOptativaVO);
			disciplinasAproveitadasVO.setGradeDisciplinaVO(null);
			disciplinasAproveitadasVO
					.setGradeCurricularGrupoOptativaDisciplina(gradeCurricularGrupoOptativaDisciplinaVO);
		}
		if (gradeCurricularGrupoOptativaDisciplinaVO.getHistoricoAtualAluno().getCursando()) {
			// se está cursando é por que já existe um histórico gerado para a disciplina
			// logo o mesmo deverá ser excluído pelo sistema, para que seja gerado outro
			// histórico
			disciplinasAproveitadasVO.setExcluirHistoricoDisciplinaCursada(true);
		}
		disciplinasAproveitadasVO.setHistoricoAtual(gradeCurricularGrupoOptativaDisciplinaVO.getHistoricoAtualAluno());
	}

	public void inicializarDadosDisciplinasAproveitadasForaGrade(AproveitamentoDisciplinaVO aproveitamentoDisciplinaVO,
			MapaEquivalenciaDisciplinaCursadaVO mapaEquivalenciaDisciplinaCursadaVO, String instituicao,
			CidadeVO cidadeVO, UsuarioVO usuarioVO) throws Exception {
		DisciplinasAproveitadasVO disciplinasAproveitadasVO = mapaEquivalenciaDisciplinaCursadaVO
				.getDisciplinasAproveitadasVO();
		if (!aproveitamentoDisciplinaVO.getCodigo().equals(0)) {
			disciplinasAproveitadasVO.setAproveitamentoDisciplina(aproveitamentoDisciplinaVO.getCodigo());
		}
		if (disciplinasAproveitadasVO.getFrequencia() == null
				|| disciplinasAproveitadasVO.getFrequencia().equals(0.0)) {
			disciplinasAproveitadasVO.setFrequencia(100.00);
		}
		// if (disciplinasAproveitadasVO.getUtilizaNotaConceito() &&
		// disciplinasAproveitadasVO.getMediaFinalConceito().getCodigo() > 0) {
		// disciplinasAproveitadasVO.setMediaFinalConceito(getFacadeFactory().getConfiguracaoAcademicoNotaConceitoFacade().consultarPorChavePrimaria(disciplinasAproveitadasVO.getMediaFinalConceito().getCodigo()));
		// }
		if (mapaEquivalenciaDisciplinaCursadaVO.getHistorico().getCursando()) {
			// se está cursando é por que já existe um histórico gerado para a disciplina
			// logo o mesmo deverá ser excluído pelo sistema, para que seja gerado outro
			// histórico
			disciplinasAproveitadasVO.setExcluirHistoricoDisciplinaCursada(true);
		}
		// para persitir no aproveitamento o mapa
		mapaEquivalenciaDisciplinaCursadaVO.getDisciplinasAproveitadasVO()
				.setMapaEquivalenciaDisciplinaCursada(mapaEquivalenciaDisciplinaCursadaVO);

		disciplinasAproveitadasVO.setHistoricoAtual(mapaEquivalenciaDisciplinaCursadaVO.getHistorico());
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirComBaseNaMatriculaPeriodo(Integer matriculaPeriodo,
			 UsuarioVO usuario) throws Exception {
		try {
			List<AproveitamentoDisciplinaVO> aproveitamentoDisciplinaVOs = consultarPorMatriculaPeriodo(
					matriculaPeriodo, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			for (AproveitamentoDisciplinaVO obj : aproveitamentoDisciplinaVOs) {
				this.excluir(obj, usuario);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirComBaseNaMatricula(String matricula, 
			UsuarioVO usuario) throws Exception {
		try {
			List<AproveitamentoDisciplinaVO> aproveitamentoDisciplinaVOs = consultaRapidaPorMatricula(matricula, 0,
					false, usuario);
			for (AproveitamentoDisciplinaVO obj : aproveitamentoDisciplinaVOs) {
				this.excluir(obj, usuario);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public List<AproveitamentoDisciplinaVO> consultarPorMatriculaPeriodo(Integer matriculaPeriodo, int nivelMontarDados,
			 UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM AproveitamentoDisciplina WHERE matriculaperiodo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql,
				new Object[] { matriculaPeriodo });
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	@Override
	public void realizarRegistroAproveitamentoDisciplinasRegistrado(
			AproveitamentoDisciplinaVO aproveitamentoDisciplinaVO) throws Exception {
		for (PeriodoLetivoVO periodoLetivoVO : aproveitamentoDisciplinaVO.getGradeCurricular().getPeriodoLetivosVOs()) {
			for (GradeDisciplinaVO gradeDisciplinaVO : periodoLetivoVO.getGradeDisciplinaVOs()) {
				if (gradeDisciplinaVO.getSelecionadoAproveitamento()) {
					gradeDisciplinaVO.setSelecionadoAproveitamento(false);
				}
			}
			if (periodoLetivoVO.getControleOptativaGrupo()) {
				for (GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO : periodoLetivoVO
						.getGradeCurricularGrupoOptativa().getGradeCurricularGrupoOptativaDisciplinaVOs()) {
					if ((gradeCurricularGrupoOptativaDisciplinaVO.getSelecionadoAproveitamento())
							&& (gradeCurricularGrupoOptativaDisciplinaVO.getDisciplinasAproveitadasVO()
									.getPeriodoletivoGrupoOptativaVO().getCodigo()
									.equals(periodoLetivoVO.getCodigo()))) {
						gradeCurricularGrupoOptativaDisciplinaVO.setSelecionadoAproveitamento(false);
					}
				}
			}
		}
	}

	/**
	 * Realiza a montagem das disciplinas no momento de editar o obj. Autor Carlos
	 */
	public void realizarMontagemPainelMatrizCurricularComDisciplinasAproveitadas(
			AproveitamentoDisciplinaVO aproveitamentoDisciplinaVO, UsuarioVO usuarioVO) throws Exception {
		realizarMontagemPainelMatrizCurricularComDisciplinasAproveitadas(aproveitamentoDisciplinaVO, null, usuarioVO);
	}

	public void carregandoDadosHistoricoMatrizCfgAcademicoCursoOrigem(
			AproveitamentoDisciplinasEntreMatriculasVO aproveitamentoDisciplinasEntreMatriculasVO, UsuarioVO usuario)
			throws Exception {
		ConfiguracaoAcademicoVO cfgCurso = getFacadeFactory().getConfiguracaoAcademicoFacade()
				.consultarPorChavePrimaria(aproveitamentoDisciplinasEntreMatriculasVO
						.getMatriculaOrigemAproveitamentoVO().getCurso().getConfiguracaoAcademico().getCodigo(),
						usuario);
		aproveitamentoDisciplinasEntreMatriculasVO.getMatriculaOrigemAproveitamentoVO().getCurso()
				.setConfiguracaoAcademico(cfgCurso);

		MatriculaComHistoricoAlunoVO matriculaComHistoricoAlunoVO = getFacadeFactory().getHistoricoFacade()
				.carregarDadosMatriculaComHistoricoAlunoVO(
						aproveitamentoDisciplinasEntreMatriculasVO.getMatriculaOrigemAproveitamentoVO(),
						aproveitamentoDisciplinasEntreMatriculasVO.getMatriculaOrigemAproveitamentoVO()
								.getGradeCurricularAtual().getCodigo(),
						false, aproveitamentoDisciplinasEntreMatriculasVO.getMatriculaOrigemAproveitamentoVO()
								.getCurso().getConfiguracaoAcademico(),
						usuario);
		aproveitamentoDisciplinasEntreMatriculasVO.getMatriculaOrigemAproveitamentoVO()
				.setMatriculaComHistoricoAlunoVO(matriculaComHistoricoAlunoVO);
		aproveitamentoDisciplinasEntreMatriculasVO.getMatriculaOrigemAproveitamentoVO().setGradeCurricularAtual(
				matriculaComHistoricoAlunoVO.getGradeCurricularComHistoricoAlunoVO().getGradeCurricularVO());

		getFacadeFactory().getUnidadeEnsinoFacade().carregarDados(
				aproveitamentoDisciplinasEntreMatriculasVO.getMatriculaOrigemAproveitamentoVO().getUnidadeEnsino(),
				NivelMontarDados.BASICO, usuario);
	}

	public void carregandoDadosHistoricoMatrizCfgAcademicoCursoDestino(
			AproveitamentoDisciplinasEntreMatriculasVO aproveitamentoDisciplinasEntreMatriculasVO, UsuarioVO usuario)
			throws Exception {
		MatriculaComHistoricoAlunoVO matriculaComHistoricoAlunoVO = getFacadeFactory().getHistoricoFacade()
				.carregarDadosMatriculaComHistoricoAlunoVO(
						aproveitamentoDisciplinasEntreMatriculasVO.getMatriculaDestinoAproveitamentoVO(),
						aproveitamentoDisciplinasEntreMatriculasVO.getMatriculaDestinoAproveitamentoVO()
								.getGradeCurricularAtual().getCodigo(),
						false, aproveitamentoDisciplinasEntreMatriculasVO.getMatriculaDestinoAproveitamentoVO()
								.getCurso().getConfiguracaoAcademico(),
						usuario);
		aproveitamentoDisciplinasEntreMatriculasVO.getMatriculaDestinoAproveitamentoVO()
				.setMatriculaComHistoricoAlunoVO(matriculaComHistoricoAlunoVO);
		aproveitamentoDisciplinasEntreMatriculasVO.getMatriculaDestinoAproveitamentoVO().setGradeCurricularAtual(
				matriculaComHistoricoAlunoVO.getGradeCurricularComHistoricoAlunoVO().getGradeCurricularVO());
	}

	public String obterObservacaoTipoDisciplinaHistoricoOrigem(HistoricoVO historicoOrigemAluno) {
		if (historicoOrigemAluno.getHistoricoDisciplinaForaGrade()) {
			return "Disciplina Fora Grade";
		} else {
			if (historicoOrigemAluno.getHistoricoDisciplinaFazParteComposicao()) {
				return "Disciplina Parte de Uma Composição";
			} else {
				if (historicoOrigemAluno.getHistoricoDisciplinaComposta()) {
					return "Disciplina Composta";
				} else {
					if (historicoOrigemAluno.getDisciplinaReferenteAUmGrupoOptativa()) {
						return "Disciplina Referente Grupo Optativa";
					} else {
						return "Regular (GradeDisciplina)";
					}
				}
			}
		}
	}

	/**
	 * Método responsavel por inicializar os dados de uma DisciplinaAproveitaVO seja
	 * a partir de uma GradeDisciplina ou a partir de uma
	 * GradeCurricularGrupoOptativaDisciplinaVO.
	 * 
	 * @author Otimize - 28 de jun de 2016
	 * @param novaDisciplinasAproveitadaVO
	 * @param gradeDisciplinaCorrespondente
	 * @param aproveitamentoDisciplinasEntreMatriculasVO
	 * @param historicoOrigemAluno
	 * @param periodoLetivoGradeDisciplinaCorrespondente
	 * @param usuario
	 */
	private void inicializarDadosBasicosNovoAproveitamento(DisciplinasAproveitadasVO novaDisciplinasAproveitadaVO,
			GradeDisciplinaVO gradeDisciplinaCorrespondente,
			GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaCorrespondente,
			AproveitamentoDisciplinasEntreMatriculasVO aproveitamentoDisciplinasEntreMatriculasVO,
			HistoricoVO historicoOrigemAluno, PeriodoLetivoVO periodoLetivoGradeDisciplinaCorrespondente,
			UsuarioVO usuario) throws Exception {
		AproveitamentoDisciplinaVO aproveitamentoVO = aproveitamentoDisciplinasEntreMatriculasVO
				.getAproveitamentoDisciplinaVO();

		novaDisciplinasAproveitadaVO
				.setTipo(aproveitamentoDisciplinasEntreMatriculasVO.getTipoAproveitamentoDisciplinasEntreMatriculas());

		// inicializando dados gerais basicos da disciplinaAproveitada
		novaDisciplinasAproveitadaVO.setAproveitamentoDisciplina(aproveitamentoVO.getCodigo());
		novaDisciplinasAproveitadaVO.setMatriculaOrigemAproveitamentoEntreMatriculas(
				aproveitamentoDisciplinasEntreMatriculasVO.getMatriculaOrigemAproveitamentoVO());
		novaDisciplinasAproveitadaVO.setResponsavelAproveitamentoEntreMatriculas(usuario);
		novaDisciplinasAproveitadaVO.setDataAproveitamentoEntreMatriculas(new Date());
		novaDisciplinasAproveitadaVO
				.setAproveitamentoDisciplinasEntreMatriculasVO(aproveitamentoDisciplinasEntreMatriculasVO);
		novaDisciplinasAproveitadaVO.setAproveitamentoDisciplinaVO(
				aproveitamentoDisciplinasEntreMatriculasVO.getAproveitamentoDisciplinaVO());

		// inicializando dados relativos a disciplina
//		novaDisciplinasAproveitadaVO.setObservacaoAproveitamentoEntreMatriculas(
//				"Aproveitamento do Histório: " + historicoOrigemAluno.getCodigo() + UteisTexto.ENTER + " - Disciplina: "
//						+ historicoOrigemAluno.getDisciplina().getCodigo() + " ("
//						+ historicoOrigemAluno.getDisciplina().getNome() + ") " + UteisTexto.ENTER
//						+ " - Carga Horária: " + historicoOrigemAluno.getCargaHorariaDisciplina() + UteisTexto.ENTER
//						+ " - Tipo Disciplina: " + historicoOrigemAluno.getDescricaoTipoDisciplinaHistorico()
//						+ UteisTexto.ENTER + " - Média: " + historicoOrigemAluno.getMediaFinal_Apresentar()
//						+ UteisTexto.ENTER + " - Frequência: " + historicoOrigemAluno.getFrequencia_Apresentar()
//						+ UteisTexto.ENTER + " - Situação: " + historicoOrigemAluno.getSituacao_Apresentar());

		Integer codigoCfgAcademicaEspecifica = null;
		if (gradeDisciplinaCorrespondente != null) {
			novaDisciplinasAproveitadaVO.setGradeDisciplinaVO(gradeDisciplinaCorrespondente);
			novaDisciplinasAproveitadaVO.setDisciplina(gradeDisciplinaCorrespondente.getDisciplina());
			novaDisciplinasAproveitadaVO.setCargaHoraria(gradeDisciplinaCorrespondente.getCargaHoraria());
			novaDisciplinasAproveitadaVO.setQtdeCreditoConcedido(gradeDisciplinaCorrespondente.getNrCreditos());
			novaDisciplinasAproveitadaVO.setQtdeCargaHorariaConcedido(gradeDisciplinaCorrespondente.getCargaHoraria());
			novaDisciplinasAproveitadaVO.setCargaHorariaCursada(gradeDisciplinaCorrespondente.getCargaHoraria());
			novaDisciplinasAproveitadaVO
					.setNomeDisciplinaCursada(gradeDisciplinaCorrespondente.getDisciplina().getNome());
			codigoCfgAcademicaEspecifica = gradeDisciplinaCorrespondente.getConfiguracaoAcademico().getCodigo();
		}
		if (gradeCurricularGrupoOptativaCorrespondente != null) {
			novaDisciplinasAproveitadaVO
					.setGradeCurricularGrupoOptativaDisciplina(gradeCurricularGrupoOptativaCorrespondente);
			novaDisciplinasAproveitadaVO.setDisciplina(gradeCurricularGrupoOptativaCorrespondente.getDisciplina());
			novaDisciplinasAproveitadaVO.setCargaHoraria(gradeCurricularGrupoOptativaCorrespondente.getCargaHoraria());
			novaDisciplinasAproveitadaVO
					.setQtdeCreditoConcedido(gradeCurricularGrupoOptativaCorrespondente.getNrCreditos());
			novaDisciplinasAproveitadaVO
					.setQtdeCargaHorariaConcedido(gradeCurricularGrupoOptativaCorrespondente.getCargaHoraria());
			novaDisciplinasAproveitadaVO
					.setCargaHorariaCursada(gradeCurricularGrupoOptativaCorrespondente.getCargaHoraria());
			novaDisciplinasAproveitadaVO
					.setNomeDisciplinaCursada(gradeCurricularGrupoOptativaCorrespondente.getDisciplina().getNome());
			novaDisciplinasAproveitadaVO.setPeriodoletivoGrupoOptativaVO(periodoLetivoGradeDisciplinaCorrespondente);
			codigoCfgAcademicaEspecifica = gradeCurricularGrupoOptativaCorrespondente.getConfiguracaoAcademico()
					.getCodigo();
		}

		novaDisciplinasAproveitadaVO.setFrequencia(historicoOrigemAluno.getFreguencia());
		if (novaDisciplinasAproveitadaVO.getFrequencia() == null
				|| novaDisciplinasAproveitadaVO.getFrequencia().equals(0.0)) {
			// regra ja existente no aproveitamento e que estamos reaproveitando. O sistema
			// assume a freq de 100% caso nao
			// tenha freq informada na disciplina origem.
			novaDisciplinasAproveitadaVO.setFrequencia(100.00);
		}
		novaDisciplinasAproveitadaVO.setNota(historicoOrigemAluno.getMediaFinal());

		// Para definir a configuracao academica a ser considerada para a disciplina
		// destino, temos que considerar
		// que a na propria GradeDisciplina (e ou correlatos) pode existir uma
		// configuracaoAcademica específica.
		// Neste caso temos que consiredá-la.
		ConfiguracaoAcademicoVO configuracaoAcademicoDisciplinaDestinoAproveitar = aproveitamentoDisciplinasEntreMatriculasVO
				.getConfiguracaoAcademicoCursoDestino();
		if (!codigoCfgAcademicaEspecifica.equals(0)) {
			ConfiguracaoAcademicoVO cfgEspecifica = getFacadeFactory().getConfiguracaoAcademicoFacade()
					.consultarPorChavePrimaria(codigoCfgAcademicaEspecifica, usuario);
			configuracaoAcademicoDisciplinaDestinoAproveitar = cfgEspecifica;
		}

		novaDisciplinasAproveitadaVO.setConfiguracaoAcademicoVO(configuracaoAcademicoDisciplinaDestinoAproveitar);

		if (configuracaoAcademicoDisciplinaDestinoAproveitar.getUtilizaConceitoMediaFinal()) {
			novaDisciplinasAproveitadaVO.setUtilizaNotaConceito(Boolean.TRUE);
			ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO = configuracaoAcademicoDisciplinaDestinoAproveitar
					.obterConceitoAPartirMediaFinal(historicoOrigemAluno.getMediaFinal());
			novaDisciplinasAproveitadaVO
					.setMediaFinalConceito(configuracaoAcademicoNotaConceitoVO.getAbreviaturaConceitoNota());
			novaDisciplinasAproveitadaVO.setConfiguracaoAcademicoNotaConceitoVO(configuracaoAcademicoNotaConceitoVO);
		}

		if (aproveitamentoDisciplinasEntreMatriculasVO.getUtilizarAnoSemestreAtualDisciplinaAprovada() && 
				aproveitamentoDisciplinasEntreMatriculasVO.getMatriculaOrigemAproveitamentoVO().getCurso().getPeriodicidade().equals(aproveitamentoDisciplinasEntreMatriculasVO.getMatriculaDestinoAproveitamentoVO().getCurso().getPeriodicidade())
				) {
			novaDisciplinasAproveitadaVO.setAno(historicoOrigemAluno.getAnoHistorico());
			novaDisciplinasAproveitadaVO.setSemestre(historicoOrigemAluno.getSemestreHistorico());
		} else {
			novaDisciplinasAproveitadaVO.setAno(aproveitamentoDisciplinasEntreMatriculasVO.getAnoPadrao());
			novaDisciplinasAproveitadaVO.setSemestre(aproveitamentoDisciplinasEntreMatriculasVO.getSemestrePadrao());
		}

		novaDisciplinasAproveitadaVO.setInstituicao(aproveitamentoDisciplinasEntreMatriculasVO
				.getMatriculaOrigemAproveitamentoVO().getUnidadeEnsino().getNomeExpedicaoDiploma());
		novaDisciplinasAproveitadaVO.setCidade(aproveitamentoDisciplinasEntreMatriculasVO
				.getMatriculaOrigemAproveitamentoVO().getUnidadeEnsino().getCidade());
		if (!novaDisciplinasAproveitadaVO.getCidade().getCodigo().equals(0)) {
			CidadeVO cidadeCarragadaVO = getFacadeFactory().getCidadeFacade()
					.consultarPorChavePrimaria(novaDisciplinasAproveitadaVO.getCidade().getCodigo(), false, usuario);
			novaDisciplinasAproveitadaVO.setCidade(cidadeCarragadaVO);
		}
		novaDisciplinasAproveitadaVO
				.setAproveitamentoPorIsencao(aproveitamentoDisciplinasEntreMatriculasVO.getAproveitamentoPorIsencao());
		novaDisciplinasAproveitadaVO.setRealizarTransferenciaDisclinaAprovadaComoAprovadaAproveitamento(
				aproveitamentoDisciplinasEntreMatriculasVO
						.getRealizarTransferenciaDisclinaAprovadaComoAprovadaAproveitamento());
		novaDisciplinasAproveitadaVO
				.setDescricaoComplementacaoCH(aproveitamentoDisciplinasEntreMatriculasVO.getDescricaoComplementacao());
		if (!aproveitamentoDisciplinasEntreMatriculasVO.getSituacaoHistoricoConcessaoEntreMatriculas().equals("")) {
			novaDisciplinasAproveitadaVO.setSituacaoHistorico(SituacaoHistorico.getEnumPorString(
					aproveitamentoDisciplinasEntreMatriculasVO.getSituacaoHistoricoConcessaoEntreMatriculas()));
		}

		if (aproveitamentoDisciplinasEntreMatriculasVO.getTipoAproveitamentoDisciplinasEntreMatriculas().equals("CO")) {
			// Caso o tipo de aproveitamento seja por concessao de creditos, entao abaixo
			// iremos criar o VO correspondente e iniciar
			// os necessarios para o aproveitamento de credito
//			ConcessaoCreditoDisciplinaVO novaConcessaoCreditoDisciplinaVO = new ConcessaoCreditoDisciplinaVO();
			inicializarDadosBasicosNovoAproveitamentoConcessaoCredito(novaDisciplinasAproveitadaVO,
					aproveitamentoDisciplinasEntreMatriculasVO, usuario);
//			getFacadeFactory().getAproveitamentoDisciplinaFacade().adicionarConcessaoCreditoDisciplina(
//					aproveitamentoDisciplinasEntreMatriculasVO.getAproveitamentoDisciplinaVO(),
//					usuario);
		}

		if (aproveitamentoDisciplinasEntreMatriculasVO.getTipoAproveitamentoDisciplinasEntreMatriculas().equals("CH")) {
			// Caso o tipo de aproveitamento seja por concessao de creditos, entao abaixo
			// iremos criar o VO correspondente e iniciar
			// os necessarios para o aproveitamento de credito
//			ConcessaoCargaHorariaDisciplinaVO novaConcessaoCargaHorariaDisciplinaVO = new ConcessaoCargaHorariaDisciplinaVO();
			inicializarDadosBasicosNovoAproveitamentoConcessaoCargaHoraria(novaDisciplinasAproveitadaVO,
					aproveitamentoDisciplinasEntreMatriculasVO,  usuario);
//			getFacadeFactory().getAproveitamentoDisciplinaFacade().adicionarConcessaoCargaHorariaDisciplina(
//					aproveitamentoDisciplinasEntreMatriculasVO.getAproveitamentoDisciplinaVO(),
//					novaConcessaoCargaHorariaDisciplinaVO, usuario);
		}
		novaDisciplinasAproveitadaVO.setApresentarAprovadoHistorico(!aproveitamentoDisciplinasEntreMatriculasVO
				.getRealizarTransferenciaDisclinaAprovadaComoAprovadaAproveitamento());
		novaDisciplinasAproveitadaVO.setSituacaoHistorico(null);
		novaDisciplinasAproveitadaVO.getSituacaoHistorico();
	}

	/**
	 * Método responsavel por inicializar os dados de uma DisciplinaAproveitaVO seja
	 * a partir de uma MapaEquivalenciaDisciplinaCursada - ou seja, vinculada a um
	 * mapa de equivalencia
	 * 
	 * @author Otimize - 28 de jun de 2016
	 * @param novaDisciplinasAproveitadaVO
	 * @param gradeDisciplinaCorrespondente
	 * @param aproveitamentoDisciplinasEntreMatriculasVO
	 * @param historicoOrigemAluno
	 * @param periodoLetivoGradeDisciplinaCorrespondente
	 * @param usuario
	 */
	private void inicializarDadosBasicosNovoAproveitamentoMapaEquivalenciaDisciplinaCursada(
			DisciplinasAproveitadasVO novaDisciplinasAproveitadaVO,
			MapaEquivalenciaDisciplinaCursadaVO mapaEquivalenciaDisciplinaCursada,
			AproveitamentoDisciplinasEntreMatriculasVO aproveitamentoDisciplinasEntreMatriculasVO,
			HistoricoVO historicoOrigemAluno,
			// PeriodoLetivoVO periodoLetivoGradeDisciplinaCorrespondente,
			UsuarioVO usuario) throws Exception {
		AproveitamentoDisciplinaVO aproveitamentoVO = aproveitamentoDisciplinasEntreMatriculasVO
				.getAproveitamentoDisciplinaVO();

		novaDisciplinasAproveitadaVO
				.setTipo(aproveitamentoDisciplinasEntreMatriculasVO.getTipoAproveitamentoDisciplinasEntreMatriculas());

		// inicializando dados gerais basicos da disciplinaAproveitada
		novaDisciplinasAproveitadaVO.setAproveitamentoDisciplina(aproveitamentoVO.getCodigo());
		novaDisciplinasAproveitadaVO.setMatriculaOrigemAproveitamentoEntreMatriculas(
				aproveitamentoDisciplinasEntreMatriculasVO.getMatriculaOrigemAproveitamentoVO());
		novaDisciplinasAproveitadaVO.setResponsavelAproveitamentoEntreMatriculas(usuario);
		novaDisciplinasAproveitadaVO.setDataAproveitamentoEntreMatriculas(new Date());
		novaDisciplinasAproveitadaVO
				.setAproveitamentoDisciplinasEntreMatriculasVO(aproveitamentoDisciplinasEntreMatriculasVO);
		novaDisciplinasAproveitadaVO.setDisciplinaForaGrade(Boolean.TRUE);

		// inicializando dados relativos a disciplina
//		novaDisciplinasAproveitadaVO.setObservacaoAproveitamentoEntreMatriculas(
//				"Aproveitamento do Histório: " + historicoOrigemAluno.getCodigo() + UteisTexto.ENTER + " - Disciplina: "
//						+ historicoOrigemAluno.getDisciplina().getCodigo() + " ("
//						+ historicoOrigemAluno.getDisciplina().getNome() + ") " + UteisTexto.ENTER
//						+ " - Carga Horária: " + historicoOrigemAluno.getCargaHorariaDisciplina() + UteisTexto.ENTER
//						+ " - Tipo Disciplina: " + historicoOrigemAluno.getDescricaoTipoDisciplinaHistorico()
//						+ UteisTexto.ENTER + " - Média: " + historicoOrigemAluno.getMediaFinal_Apresentar()
//						+ UteisTexto.ENTER + " - Frequência: " + historicoOrigemAluno.getFrequencia_Apresentar()
//						+ UteisTexto.ENTER + " - Situação: " + historicoOrigemAluno.getSituacao_Apresentar());

		novaDisciplinasAproveitadaVO.setMapaEquivalenciaDisciplinaCursada(mapaEquivalenciaDisciplinaCursada);
		novaDisciplinasAproveitadaVO.setDisciplina(mapaEquivalenciaDisciplinaCursada.getDisciplinaVO());
		novaDisciplinasAproveitadaVO.setCargaHoraria(mapaEquivalenciaDisciplinaCursada.getCargaHoraria());
		novaDisciplinasAproveitadaVO.setQtdeCreditoConcedido(mapaEquivalenciaDisciplinaCursada.getNumeroCreditos());
		novaDisciplinasAproveitadaVO.setQtdeCargaHorariaConcedido(mapaEquivalenciaDisciplinaCursada.getCargaHoraria());
		novaDisciplinasAproveitadaVO.setCargaHorariaCursada(mapaEquivalenciaDisciplinaCursada.getCargaHoraria());
		novaDisciplinasAproveitadaVO
				.setNomeDisciplinaCursada(mapaEquivalenciaDisciplinaCursada.getDisciplinaVO().getNome());

		novaDisciplinasAproveitadaVO.setFrequencia(historicoOrigemAluno.getFreguencia());
		if (novaDisciplinasAproveitadaVO.getFrequencia() == null
				|| novaDisciplinasAproveitadaVO.getFrequencia().equals(0.0)) {
			// regra ja existente no aproveitamento e que estamos reaproveitando. O sistema
			// assume a freq de 100% caso nao
			// tenha freq informada na disciplina origem.
			novaDisciplinasAproveitadaVO.setFrequencia(100.00);
		}
		novaDisciplinasAproveitadaVO.setNota(historicoOrigemAluno.getMediaFinal());

		ConfiguracaoAcademicoVO configuracaoAcademicoDisciplinaDestinoAproveitar = aproveitamentoDisciplinasEntreMatriculasVO
				.getConfiguracaoAcademicoCursoDestino();
		if (configuracaoAcademicoDisciplinaDestinoAproveitar.getUtilizaConceitoMediaFinal()) {
			novaDisciplinasAproveitadaVO.setUtilizaNotaConceito(Boolean.TRUE);
			ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO = configuracaoAcademicoDisciplinaDestinoAproveitar
					.obterConceitoAPartirMediaFinal(historicoOrigemAluno.getMediaFinal());
			novaDisciplinasAproveitadaVO
					.setMediaFinalConceito(configuracaoAcademicoNotaConceitoVO.getAbreviaturaConceitoNota());
			novaDisciplinasAproveitadaVO.setConfiguracaoAcademicoNotaConceitoVO(configuracaoAcademicoNotaConceitoVO);
		}
		novaDisciplinasAproveitadaVO.setConfiguracaoAcademicoVO(configuracaoAcademicoDisciplinaDestinoAproveitar);

		if (aproveitamentoDisciplinasEntreMatriculasVO.getUtilizarAnoSemestreAtualDisciplinaAprovada()) {
			novaDisciplinasAproveitadaVO.setAno(historicoOrigemAluno.getAnoHistorico());
			novaDisciplinasAproveitadaVO.setSemestre(historicoOrigemAluno.getSemestreHistorico());
		} else {
			novaDisciplinasAproveitadaVO.setAno(aproveitamentoDisciplinasEntreMatriculasVO.getAnoPadrao());
			novaDisciplinasAproveitadaVO.setSemestre(aproveitamentoDisciplinasEntreMatriculasVO.getSemestrePadrao());
		}

		novaDisciplinasAproveitadaVO.setInstituicao(aproveitamentoDisciplinasEntreMatriculasVO
				.getMatriculaOrigemAproveitamentoVO().getUnidadeEnsino().getNomeExpedicaoDiploma());
		novaDisciplinasAproveitadaVO.setCidade(aproveitamentoDisciplinasEntreMatriculasVO
				.getMatriculaOrigemAproveitamentoVO().getUnidadeEnsino().getCidade());
		if (!novaDisciplinasAproveitadaVO.getCidade().getCodigo().equals(0)) {
			CidadeVO cidadeCarragadaVO = getFacadeFactory().getCidadeFacade()
					.consultarPorChavePrimaria(novaDisciplinasAproveitadaVO.getCidade().getCodigo(), false, usuario);
			novaDisciplinasAproveitadaVO.setCidade(cidadeCarragadaVO);
		}
		novaDisciplinasAproveitadaVO
				.setAproveitamentoPorIsencao(aproveitamentoDisciplinasEntreMatriculasVO.getAproveitamentoPorIsencao());
		novaDisciplinasAproveitadaVO.setRealizarTransferenciaDisclinaAprovadaComoAprovadaAproveitamento(
				aproveitamentoDisciplinasEntreMatriculasVO
						.getRealizarTransferenciaDisclinaAprovadaComoAprovadaAproveitamento());
		novaDisciplinasAproveitadaVO
				.setDescricaoComplementacaoCH(aproveitamentoDisciplinasEntreMatriculasVO.getDescricaoComplementacao());
		if (!aproveitamentoDisciplinasEntreMatriculasVO.getSituacaoHistoricoConcessaoEntreMatriculas().equals("")) {
			novaDisciplinasAproveitadaVO.setSituacaoHistorico(SituacaoHistorico.getEnumPorString(
					aproveitamentoDisciplinasEntreMatriculasVO.getSituacaoHistoricoConcessaoEntreMatriculas()));
		}

		if (aproveitamentoDisciplinasEntreMatriculasVO.getTipoAproveitamentoDisciplinasEntreMatriculas().equals("CO")) {
			// Caso o tipo de aproveitamento seja por concessao de creditos, entao abaixo
			// iremos criar o VO correspondente e iniciar
			// os necessarios para o aproveitamento de credito
//			ConcessaoCreditoDisciplinaVO novaConcessaoCreditoDisciplinaVO = new ConcessaoCreditoDisciplinaVO();
			inicializarDadosBasicosNovoAproveitamentoConcessaoCredito(novaDisciplinasAproveitadaVO,
					aproveitamentoDisciplinasEntreMatriculasVO, usuario);
//			getFacadeFactory().getAproveitamentoDisciplinaFacade().adicionarConcessaoCreditoDisciplina(
//					aproveitamentoDisciplinasEntreMatriculasVO.getAproveitamentoDisciplinaVO(),
//					 usuario);
		}

		if (aproveitamentoDisciplinasEntreMatriculasVO.getTipoAproveitamentoDisciplinasEntreMatriculas().equals("CH")) {
			// Caso o tipo de aproveitamento seja por concessao de creditos, entao abaixo
			// iremos criar o VO correspondente e iniciar
			// os necessarios para o aproveitamento de credito
//			ConcessaoCargaHorariaDisciplinaVO novaConcessaoCargaHorariaDisciplinaVO = new ConcessaoCargaHorariaDisciplinaVO();
			inicializarDadosBasicosNovoAproveitamentoConcessaoCargaHoraria(novaDisciplinasAproveitadaVO,
					aproveitamentoDisciplinasEntreMatriculasVO, usuario);
//			getFacadeFactory().getAproveitamentoDisciplinaFacade().adicionarConcessaoCargaHorariaDisciplina(
//					aproveitamentoDisciplinasEntreMatriculasVO.getAproveitamentoDisciplinaVO(),
//					 usuario);
		}
		novaDisciplinasAproveitadaVO.setApresentarAprovadoHistorico(!aproveitamentoDisciplinasEntreMatriculasVO
				.getRealizarTransferenciaDisclinaAprovadaComoAprovadaAproveitamento());
		novaDisciplinasAproveitadaVO.setSituacaoHistorico(null);
		novaDisciplinasAproveitadaVO.getSituacaoHistorico();
	}

	private void inicializarDadosBasicosNovoAproveitamentoConcessaoCredito(
			DisciplinasAproveitadasVO novaDisciplinasAproveitadaVO,
			AproveitamentoDisciplinasEntreMatriculasVO aproveitamentoDisciplinasEntreMatriculasVO,
			UsuarioVO usuario) {
//		novaConcessaoCreditoDisciplinaVO.setDisciplinaVO(novaDisciplinasAproveitadaVO.getDisciplina());
//		novaConcessaoCreditoDisciplinaVO.setGradeDisciplinaVO(novaDisciplinasAproveitadaVO.getGradeDisciplinaVO());
//		novaConcessaoCreditoDisciplinaVO
//				.setAproveitamentoDisciplinaVO(novaDisciplinasAproveitadaVO.getAproveitamentoDisciplinaVO());
//		novaConcessaoCreditoDisciplinaVO
//				.setQtdeCreditoConcedido(novaDisciplinasAproveitadaVO.getQtdeCreditoConcedido());
//		novaConcessaoCreditoDisciplinaVO
//				.setDescricaoComplementacaoCH(aproveitamentoDisciplinasEntreMatriculasVO.getDescricaoComplementacao());
//		novaConcessaoCreditoDisciplinaVO.setAno(novaDisciplinasAproveitadaVO.getAno());
//		novaConcessaoCreditoDisciplinaVO.setSemestre(novaDisciplinasAproveitadaVO.getSemestre());
	}

	private void inicializarDadosBasicosNovoAproveitamentoConcessaoCargaHoraria(
			DisciplinasAproveitadasVO novaDisciplinasAproveitadaVO,
			AproveitamentoDisciplinasEntreMatriculasVO aproveitamentoDisciplinasEntreMatriculasVO,
			 UsuarioVO usuario) {
//		novaConcessaoCargaHorariaDisciplinaVO.setDisciplinaVO(novaDisciplinasAproveitadaVO.getDisciplina());
//		novaConcessaoCargaHorariaDisciplinaVO.setGradeDisciplinaVO(novaDisciplinasAproveitadaVO.getGradeDisciplinaVO());
//		novaConcessaoCargaHorariaDisciplinaVO
//				.setAproveitamentoDisciplinaVO(novaDisciplinasAproveitadaVO.getAproveitamentoDisciplinaVO());
//		novaConcessaoCargaHorariaDisciplinaVO
//				.setQtdeCargaHorariaConcedido(novaDisciplinasAproveitadaVO.getQtdeCargaHorariaConcedido());
//		novaConcessaoCargaHorariaDisciplinaVO
//				.setDescricaoComplementacaoCH(aproveitamentoDisciplinasEntreMatriculasVO.getDescricaoComplementacao());
//		novaConcessaoCargaHorariaDisciplinaVO.setAno(novaDisciplinasAproveitadaVO.getAno());
//		novaConcessaoCargaHorariaDisciplinaVO.setSemestre(novaDisciplinasAproveitadaVO.getSemestre());
	}

	/**
	 * Método responsável por gerar um novo historico, na nova grade, com base em um
	 * histórico existente na grade anterior. Este método está preparado para
	 * trabalhar somente com historicos aprovados na grade origem e que foram
	 * mapeados na nova grade (grade destino). Considerações importantes: a) Cfg
	 * academica deve ser mantida, pois trata-se de um historico já aprovado (logo,
	 * a cfg utilizada no ato da aprovacao nao pode ser modificada) b) Se a
	 * disciplina na nova grade ou na grade anterior é composta isto não é tratado
	 * aqui. Somente a disciplina mae e suas informacores serao migradas. c) Se a
	 * disciplina fazia parte de uma equivalencia na grade Origem, ou seja, a mesma
	 * foi aprovada por equivalencia na grade origem, estas informaçoes NÃO serão
	 * mantidas na gradeDestino. Pois o mapa de equivalencia utilizado na
	 * gradeOrigem não é aplicável para a gradeDestino. Desta maneira, este
	 * histórico (que já está aprovado e teoricamente) não será mais editado, será
	 * gerado como um novo histórico convencional, mas vinculado a um registro de
	 * transferência de matriz de curricular. d) Se trata-se de uma disciplina
	 * vinculada a uma atividade complementar na gradeOrigem este vinculo será
	 * mantido na gradeDestino, caso contrário o histórico sairá errado em função da
	 * totalização da carga horária. e) Novo histórico criado, não será vinculado a
	 * nenhuma MatriculaPeriodoTurmaDisciplina, pois a turma na qual o aluno estudou
	 * na grade anterior não é compatível com a grade atual. f) PeriodoLetivo será
	 * atualizado para o periodoLetivo da gradeDisciplina correspodente. Tanto o que
	 * refere-se ao periodo letivo cursado quanto ao periodo letivo da matriz. Este
	 * método também é utilizado para gerar os históricos de reprovação nas
	 * disciplinas. Pois o comportamento é identico e a situação do histórico não é
	 * alterada na clonagem. O booleno
	 * historicoParaRefletirCorrespondenciaGradeOrigem será true somente, quando
	 * estivermos gerando um histórico para uma disciplina que está sendo cursada
	 * por outra disciplina correspondente. Ou seja, o aluno irá ser aprovado no
	 * histórico da grade origem e este histórico que está sendo gerado será
	 * aprovado automaticamente, pela existencia de correspondencia de disciplinas
	 * da gradeOrigem e gradeDestino.
	 */
	private void gerarAproveitamentoGradeDisciplinaCorrespodente(
			AproveitamentoDisciplinasEntreMatriculasVO aproveitamentoDisciplinasEntreMatriculasVO,
			GradeDisciplinaVO gradeDisciplinaCorrespondente, HistoricoVO historicoOrigemAluno,
			Boolean historicoParaRefletirCorrespondenciaGradeOrigem, UsuarioVO usuario) throws Exception {
		DisciplinasAproveitadasVO novaDisciplinasAproveitadaVO = new DisciplinasAproveitadasVO();
		// inicializando os dados do aproveitamento com base no historicoOrigemAluno
		inicializarDadosBasicosNovoAproveitamento(novaDisciplinasAproveitadaVO, gradeDisciplinaCorrespondente, null,
				aproveitamentoDisciplinasEntreMatriculasVO, historicoOrigemAluno,
				gradeDisciplinaCorrespondente.getPeriodoLetivoVO(), usuario);

		aproveitamentoDisciplinasEntreMatriculasVO.getAproveitamentoDisciplinaVO()
				.adicionarObjDisciplinasAproveitadasVOs(novaDisciplinasAproveitadaVO);

		// adicionando o histórico para lista de históricos que foram aproveitados
		aproveitamentoDisciplinasEntreMatriculasVO.getHistoricoAproveitados().add(historicoOrigemAluno);

		// Aqui iresmo avaliar e tratar um caso particular, onde a disciplina migrada
		// entre matriculas, sao compostas.
		// Sempre que a disciplina migrada for composta e composicao for exatamente a
		// mesma, temos que fazer o esforço
		// de gerar o aproveitamento das disciplinas que fazem parte da composição,
		// pois, estas disciplinas precisam
		// sair no boletim do aluno. E, migrando somente a mãe, o histórico do aluno na
		// matriz destino ficaria sem informações
		// básicas.
		verificarDisciplinasSaoCompostosEGerarAproveitamentoFilhasComposicao(aproveitamentoDisciplinasEntreMatriculasVO,
				gradeDisciplinaCorrespondente, novaDisciplinasAproveitadaVO, historicoOrigemAluno, usuario);
	}

	/**
	 * Aqui iresmo avaliar e tratar um caso particular, onde a disciplina migrada
	 * entre matriculas, sao compostas. Sempre que a disciplina migrada for composta
	 * e composicao for exatamente a mesma, temos que fazer o esforço de gerar o
	 * aproveitamento das disciplinas que fazem parte da composição, pois, estas
	 * disciplinas precisam sair no boletim do aluno. E, migrando somente a mãe, o
	 * histórico do aluno na matriz destino ficaria sem informações básicas.
	 * 
	 * @author Otimize - 29 de jul de 2016
	 * @param aproveitamentoDisciplinasEntreMatriculasVO
	 * @param gradeDisciplinaCorrespondente
	 * @param historicoOrigemAluno
	 * @param usuario
	 * @throws Exception
	 */
	private void verificarDisciplinasSaoCompostosEGerarAproveitamentoFilhasComposicao(
			AproveitamentoDisciplinasEntreMatriculasVO aproveitamentoDisciplinasEntreMatriculasVO,
			GradeDisciplinaVO gradeDisciplinaCorrespondente,
			DisciplinasAproveitadasVO disciplinasAproveitadaVOMaeComposicao, HistoricoVO historicoOrigemAluno,
			UsuarioVO usuario) throws Exception {
		if ((gradeDisciplinaCorrespondente.getDisciplinaComposta())
				&& (historicoOrigemAluno.getHistoricoDisciplinaComposta())) {
			// se entramos aqui é por que ambas são disciplinas compostas, logo temos que
			// verificar se composicao é a mesma

			// carregando dados das disciplinas fazem parte composticao gradeDestino
			GradeDisciplinaVO gradeDisciplinaComCompostas = getFacadeFactory().getGradeDisciplinaFacade()
					.consultarPorChavePrimaria(gradeDisciplinaCorrespondente.getCodigo(), usuario);
			gradeDisciplinaCorrespondente
					.setGradeDisciplinaCompostaVOs(gradeDisciplinaComCompostas.getGradeDisciplinaCompostaVOs());

			List<GradeDisciplinaCompostaVO> listaOrigemGradeDisciplinaCompostaVOs = null;
			if (historicoOrigemAluno.getDisciplinaReferenteAUmGrupoOptativa()) {
				if (!historicoOrigemAluno.getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo().equals(0)) {
					// carregando dados das disciplinas fazem parte composticao gradeOrigem -
					// GrupoOptativa
					// se nao conseguimos determinar a composicao da origem, nao temos como migrar
					// os historicos para a destino
					GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaComCompostasHistorico = getFacadeFactory()
							.getGradeCurricularGrupoOptativaDisciplinaFacade().consultarPorChavePrimaria(
									historicoOrigemAluno.getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo(),
									usuario);
					historicoOrigemAluno.getGradeCurricularGrupoOptativaDisciplinaVO().setGradeDisciplinaCompostaVOs(
							gradeCurricularGrupoOptativaComCompostasHistorico.getGradeDisciplinaCompostaVOs());
					listaOrigemGradeDisciplinaCompostaVOs = gradeCurricularGrupoOptativaComCompostasHistorico
							.getGradeDisciplinaCompostaVOs();
				} else {
					return;
				}
			} else {
				if (!historicoOrigemAluno.getGradeDisciplinaVO().getCodigo().equals(0)) {
					// carregando dados das disciplinas fazem parte composticao gradeOrigem -
					// GradeDisciplina
					GradeDisciplinaVO gradeDisciplinaComCompostasHistorico = getFacadeFactory()
							.getGradeDisciplinaFacade().consultarPorChavePrimaria(
									historicoOrigemAluno.getGradeDisciplinaVO().getCodigo(), usuario);
					historicoOrigemAluno.getGradeDisciplinaVO().setGradeDisciplinaCompostaVOs(
							gradeDisciplinaComCompostasHistorico.getGradeDisciplinaCompostaVOs());
					listaOrigemGradeDisciplinaCompostaVOs = gradeDisciplinaComCompostasHistorico
							.getGradeDisciplinaCompostaVOs();
				} else {
					// se nao conseguimos determinar a composicao da origem, nao temos como migrar
					// os historicos para a destino
					return;
				}
			}

			// verificando se as composicoes sao identicas, caso contrario nao podemos gerar
			// o aproveitamento automatico das disciplinas
			// que fazem parte da composicao, pois isto iria gerar dados inconsistente entre
			// as filhas e mãe na matrizDestino.
			List<GradeDisciplinaCompostaVO> listaDestinoGradeDisciplinaCompostaVOs = gradeDisciplinaCorrespondente
					.getGradeDisciplinaCompostaVOs();

			if (!verificarDisciplinasFazemParteComposicoesSaoIdenticas(listaOrigemGradeDisciplinaCompostaVOs,
					listaDestinoGradeDisciplinaCompostaVOs)) {
				return;
			}

			// carregando os historicos das disciplinas que fazem parte da composicao, a
			// partir da matrizOrigem do aproveitamento
			for (GradeDisciplinaCompostaVO gradeDisciplinaCompostaDestinoVO : gradeDisciplinaCorrespondente
					.getGradeDisciplinaCompostaVOs()) {
				// agora iremos percorrer todas as gradeDisciplinaCompostaDestino e gerar o
				// aproveitamento para cada uma delas
				// com base no historico origem correspondente
				HistoricoVO historicoAproveitarDisciplinaFazParteComposicao = aproveitamentoDisciplinasEntreMatriculasVO
						.getMatriculaOrigemAproveitamentoVO().getMatriculaComHistoricoAlunoVO()
						.getGradeCurricularComHistoricoAlunoVO().obterHistoricoAtualPorDisciplinaCargaHoraria(
								gradeDisciplinaCompostaDestinoVO.getDisciplina().getCodigo(),
								gradeDisciplinaCompostaDestinoVO.getCargaHoraria());
				gradeDisciplinaCompostaDestinoVO
						.setHistoricoAtualAluno(historicoAproveitarDisciplinaFazParteComposicao);
			}

			// gerando efetivamente os aproveitamentos para a disciplina mae
			try {
				disciplinasAproveitadaVOMaeComposicao.setDisciplinaComposta(Boolean.TRUE);
				gerarAproveitamentoDisciplinaFazParteComposicaoDeGradeDisciplinaComposta(
						aproveitamentoDisciplinasEntreMatriculasVO, disciplinasAproveitadaVOMaeComposicao,
						gradeDisciplinaCorrespondente.getGradeDisciplinaCompostaVOs(), gradeDisciplinaCorrespondente,
						null, usuario);
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}

			// atualizando campo de observacos da disciplina mae, para descrever os
			// aproveitamentos gerados para as disciplinas filhas.
			List<HistoricoVO> historicoDisciplinasFazemParteAproveitamentoVOs = new ArrayList<HistoricoVO>(0);
			for (DisciplinasAproveitadasVO disciplinaAproveitadaParteComposicao : disciplinasAproveitadaVOMaeComposicao
					.getDisciplinasAproveitadasFazemParteComposicao()) {
				historicoDisciplinasFazemParteAproveitamentoVOs
						.add(disciplinaAproveitadaParteComposicao.getHistoricoAtual());
			}
//			StringBuilder obs = new StringBuilder("");
//			obs.append(UteisTexto.ENTER + "  ");
//			obs.append(UteisTexto.ENTER + "===================================================");
//			obs.append(UteisTexto.ENTER
//					+ "Disciplinas que fazem parte desta composição também foram aproveitados, por meio dos Históricos das Disciplinas da Composição na Grade Origem. "
//					+ UteisTexto.ENTER);
//			int nrDisciplina = 1;
//			for (HistoricoVO historicoFazParte : historicoDisciplinasFazemParteAproveitamentoVOs) {
//				obs.append(UteisTexto.ENTER + "DISCIPLINA ").append(nrDisciplina)
//						.append(" FAZ PARTE COMPOSIÇÃO APROVEITADA");
//				obs.append(UteisTexto.ENTER + " - Disciplina: ").append(historicoFazParte.getDisciplina().getCodigo())
//						.append(" (").append(historicoFazParte.getDisciplina().getNome()).append(") ");
//				obs.append(UteisTexto.ENTER + " - Carga Horária: ")
//						.append(historicoFazParte.getCargaHorariaDisciplina());
//				obs.append(UteisTexto.ENTER + " - Tipo Disciplina: ")
//						.append(historicoFazParte.getDescricaoTipoDisciplinaHistorico());
//				obs.append(UteisTexto.ENTER + " - Média: ").append(historicoFazParte.getMediaFinal_Apresentar());
//				obs.append(UteisTexto.ENTER + " - Frequência: ").append(historicoFazParte.getFrequencia_Apresentar());
//				obs.append(UteisTexto.ENTER + " - Situação: ").append(historicoFazParte.getSituacao_Apresentar());
//				obs.append(UteisTexto.ENTER + "  ");
//				nrDisciplina = nrDisciplina + 1;
//			}
//			disciplinasAproveitadaVOMaeComposicao.setObservacaoAproveitamentoEntreMatriculas(
//					disciplinasAproveitadaVOMaeComposicao.getObservacaoAproveitamentoEntreMatriculas()
//							+ obs.toString());

		}
	}

	private Boolean verificarDisciplinasFazemParteComposicoesSaoIdenticas(
			List<GradeDisciplinaCompostaVO> listaOrigemGradeDisciplinaCompostaVOs,
			List<GradeDisciplinaCompostaVO> listaDestinoGradeDisciplinaCompostaVOs) {
		if ((listaOrigemGradeDisciplinaCompostaVOs == null) || (listaDestinoGradeDisciplinaCompostaVOs == null)) {
			return Boolean.FALSE;
		}

		// se o nr de disciplinas que fazem parte da composicao já nao batem, significa
		// que as composicoes sao diferentes, logo nao podemos continuar
		if (listaOrigemGradeDisciplinaCompostaVOs.size() != listaDestinoGradeDisciplinaCompostaVOs.size()) {
			return Boolean.FALSE;
		}

		for (GradeDisciplinaCompostaVO gradeDisciplinaCompostaVOOrigem : listaOrigemGradeDisciplinaCompostaVOs) {
			boolean localizouMesmaDisciplina = false;
			for (GradeDisciplinaCompostaVO gradeDisciplinaCompostaVODestino : listaDestinoGradeDisciplinaCompostaVOs) {
				if ((gradeDisciplinaCompostaVODestino.getDisciplina().getCodigo()
						.equals(gradeDisciplinaCompostaVOOrigem.getDisciplina().getCodigo())
						&& (gradeDisciplinaCompostaVODestino.getCargaHoraria()
								.equals(gradeDisciplinaCompostaVOOrigem.getCargaHoraria())))) {
					localizouMesmaDisciplina = true;
					break;
				}
			}
			if (!localizouMesmaDisciplina) {
				// se uma unica disciplina nao foi localizada, significa que as composicoes nao
				// sao iguais, logo nao
				// podem ser aproveitadas automaticamente
				return Boolean.FALSE;
			}
		}
		return Boolean.TRUE;
	}

	/**
	 * Método identico ao gerarAproveitamentoGradeDisciplinaCorrespodente. Contudo,
	 * trabalha para gerar uma disciplina de um grupo de optativas.
	 * 
	 * @author Otimize - 14 de jul de 2016
	 * @param aproveitamentoDisciplinasEntreMatriculasVO
	 * @param gradeCurricularGrupoOptativaDisciplinaVOCorrespondente
	 * @param historicoOrigemAluno
	 * @param historicoParaRefletirCorrespondenciaGradeOrigem
	 * @param usuario
	 * @throws Exception
	 */
	private void gerarAproveitamentoGradeGrupoOptativaDisciplinaCorrespodente(
			AproveitamentoDisciplinasEntreMatriculasVO aproveitamentoDisciplinasEntreMatriculasVO,
			GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVOCorrespondente,
			HistoricoVO historicoOrigemAluno, Boolean historicoParaRefletirCorrespondenciaGradeOrigem,
			UsuarioVO usuario) throws Exception {
		DisciplinasAproveitadasVO novaDisciplinasAproveitadaVO = new DisciplinasAproveitadasVO();

		// inicializando os dados do aproveitamento com base no historicoOrigemAluno
		inicializarDadosBasicosNovoAproveitamento(novaDisciplinasAproveitadaVO, null,
				gradeCurricularGrupoOptativaDisciplinaVOCorrespondente, aproveitamentoDisciplinasEntreMatriculasVO,
				historicoOrigemAluno,
				gradeCurricularGrupoOptativaDisciplinaVOCorrespondente.getPeriodoLetivoDisciplinaReferenciada(),
				usuario);

		aproveitamentoDisciplinasEntreMatriculasVO.getAproveitamentoDisciplinaVO()
				.adicionarObjDisciplinasAproveitadasVOs(novaDisciplinasAproveitadaVO);

		// adicionando o histórico para lista de históricos que foram aproveitados
		aproveitamentoDisciplinasEntreMatriculasVO.getHistoricoAproveitados().add(historicoOrigemAluno);

		// Aqui iresmo avaliar e tratar um caso particular, onde a disciplina migrada
		// entre matriculas, sao compostas.
		// Sempre que a disciplina migrada for composta e composicao for exatamente a
		// mesma, temos que fazer o esforço
		// de gerar o aproveitamento das disciplinas que fazem parte da composição,
		// pois, estas disciplinas precisam
		// sair no boletim do aluno. E, migrando somente a mãe, o histórico do aluno na
		// matriz destino ficaria sem informações
		// básicas.
		verificarDisciplinasSaoCompostosEGerarAproveitamentoFilhasComposicaoGrupoOptativa(
				aproveitamentoDisciplinasEntreMatriculasVO, gradeCurricularGrupoOptativaDisciplinaVOCorrespondente,
				novaDisciplinasAproveitadaVO, historicoOrigemAluno, usuario);
	}

	/**
	 * Aqui iresmo avaliar e tratar um caso particular, onde a disciplina migrada
	 * entre matriculas, sao compostas. Sempre que a disciplina migrada for composta
	 * e composicao for exatamente a mesma, temos que fazer o esforço de gerar o
	 * aproveitamento das disciplinas que fazem parte da composição, pois, estas
	 * disciplinas precisam sair no boletim do aluno. E, migrando somente a mãe, o
	 * histórico do aluno na matriz destino ficaria sem informações básicas.
	 * 
	 * @author Otimize - 29 de jul de 2016
	 * @param aproveitamentoDisciplinasEntreMatriculasVO
	 * @param gradeDisciplinaCorrespondente
	 * @param historicoOrigemAluno
	 * @param usuario
	 * @throws Exception
	 */
	private void verificarDisciplinasSaoCompostosEGerarAproveitamentoFilhasComposicaoGrupoOptativa(
			AproveitamentoDisciplinasEntreMatriculasVO aproveitamentoDisciplinasEntreMatriculasVO,
			GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVOCorrespondente,
			DisciplinasAproveitadasVO disciplinasAproveitadaVOMaeComposicao, HistoricoVO historicoOrigemAluno,
			UsuarioVO usuario) throws Exception {
		if ((gradeCurricularGrupoOptativaDisciplinaVOCorrespondente.getDisciplinaComposta())
				&& (historicoOrigemAluno.getHistoricoDisciplinaComposta())) {
			// se entramos aqui é por que ambas são disciplinas compostas, logo temos que
			// verificar se composicao é a mesma

			// carregando dados das disciplinas fazem parte composticao gradeDestino
			GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaComCompostas = getFacadeFactory()
					.getGradeCurricularGrupoOptativaDisciplinaFacade().consultarPorChavePrimaria(
							gradeCurricularGrupoOptativaDisciplinaVOCorrespondente.getCodigo(), usuario);
			gradeCurricularGrupoOptativaDisciplinaVOCorrespondente.setGradeDisciplinaCompostaVOs(
					gradeCurricularGrupoOptativaComCompostas.getGradeDisciplinaCompostaVOs());

			List<GradeDisciplinaCompostaVO> listaOrigemGradeDisciplinaCompostaVOs = null;
			if (historicoOrigemAluno.getDisciplinaReferenteAUmGrupoOptativa()) {
				if (!historicoOrigemAluno.getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo().equals(0)) {
					// carregando dados das disciplinas fazem parte composticao gradeOrigem -
					// GrupoOptativa
					// se nao conseguimos determinar a composicao da origem, nao temos como migrar
					// os historicos para a destino
					GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaComCompostasHistorico = getFacadeFactory()
							.getGradeCurricularGrupoOptativaDisciplinaFacade().consultarPorChavePrimaria(
									historicoOrigemAluno.getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo(),
									usuario);
					historicoOrigemAluno.getGradeCurricularGrupoOptativaDisciplinaVO().setGradeDisciplinaCompostaVOs(
							gradeCurricularGrupoOptativaComCompostasHistorico.getGradeDisciplinaCompostaVOs());
					listaOrigemGradeDisciplinaCompostaVOs = gradeCurricularGrupoOptativaComCompostasHistorico
							.getGradeDisciplinaCompostaVOs();
				} else {
					return;
				}
			} else {
				if (!historicoOrigemAluno.getGradeDisciplinaVO().getCodigo().equals(0)) {
					// carregando dados das disciplinas fazem parte composticao gradeOrigem -
					// GradeDisciplina
					GradeDisciplinaVO gradeDisciplinaComCompostasHistorico = getFacadeFactory()
							.getGradeDisciplinaFacade().consultarPorChavePrimaria(
									historicoOrigemAluno.getGradeDisciplinaVO().getCodigo(), usuario);
					historicoOrigemAluno.getGradeDisciplinaVO().setGradeDisciplinaCompostaVOs(
							gradeDisciplinaComCompostasHistorico.getGradeDisciplinaCompostaVOs());
					listaOrigemGradeDisciplinaCompostaVOs = gradeDisciplinaComCompostasHistorico
							.getGradeDisciplinaCompostaVOs();
				} else {
					// se nao conseguimos determinar a composicao da origem, nao temos como migrar
					// os historicos para a destino
					return;
				}
			}

			// verificando se as composicoes sao identicas, caso contrario nao podemos gerar
			// o aproveitamento automatico das disciplinas
			// que fazem parte da composicao, pois isto iria gerar dados inconsistente entre
			// as filhas e mãe na matrizDestino.
			List<GradeDisciplinaCompostaVO> listaDestinoGradeDisciplinaCompostaVOs = gradeCurricularGrupoOptativaDisciplinaVOCorrespondente
					.getGradeDisciplinaCompostaVOs();

			if (!verificarDisciplinasFazemParteComposicoesSaoIdenticas(listaOrigemGradeDisciplinaCompostaVOs,
					listaDestinoGradeDisciplinaCompostaVOs)) {
				return;
			}

			// carregando os historicos das disciplinas que fazem parte da composicao, a
			// partir da matrizOrigem do aproveitamento
			for (GradeDisciplinaCompostaVO gradeDisciplinaCompostaDestinoVO : gradeCurricularGrupoOptativaDisciplinaVOCorrespondente
					.getGradeDisciplinaCompostaVOs()) {
				// agora iremos percorrer todas as gradeDisciplinaCompostaDestino e gerar o
				// aproveitamento para cada uma delas
				// com base no historico origem correspondente
				HistoricoVO historicoAproveitarDisciplinaFazParteComposicao = aproveitamentoDisciplinasEntreMatriculasVO
						.getMatriculaOrigemAproveitamentoVO().getMatriculaComHistoricoAlunoVO()
						.getGradeCurricularComHistoricoAlunoVO().obterHistoricoAtualPorDisciplinaCargaHoraria(
								gradeDisciplinaCompostaDestinoVO.getDisciplina().getCodigo(),
								gradeDisciplinaCompostaDestinoVO.getCargaHoraria());
				gradeDisciplinaCompostaDestinoVO
						.setHistoricoAtualAluno(historicoAproveitarDisciplinaFazParteComposicao);
			}

			// gerando efetivamente os aproveitamentos para a disciplina mae
			try {
				disciplinasAproveitadaVOMaeComposicao.setDisciplinaComposta(Boolean.TRUE);
				gerarAproveitamentoDisciplinaFazParteComposicaoDeGradeDisciplinaComposta(
						aproveitamentoDisciplinasEntreMatriculasVO, disciplinasAproveitadaVOMaeComposicao,
						gradeCurricularGrupoOptativaDisciplinaVOCorrespondente.getGradeDisciplinaCompostaVOs(), null,
						gradeCurricularGrupoOptativaDisciplinaVOCorrespondente, usuario);
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}

			// atualizando campo de observacos da disciplina mae, para descrever os
			// aproveitamentos gerados para as disciplinas filhas.
			List<HistoricoVO> historicoDisciplinasFazemParteAproveitamentoVOs = new ArrayList<HistoricoVO>(0);
			for (DisciplinasAproveitadasVO disciplinaAproveitadaParteComposicao : disciplinasAproveitadaVOMaeComposicao
					.getDisciplinasAproveitadasFazemParteComposicao()) {
				historicoDisciplinasFazemParteAproveitamentoVOs
						.add(disciplinaAproveitadaParteComposicao.getHistoricoAtual());
			}
//			StringBuilder obs = new StringBuilder("");
//			obs.append(UteisTexto.ENTER + "  ");
//			obs.append(UteisTexto.ENTER + "===================================================");
//			obs.append(UteisTexto.ENTER
//					+ "Disciplinas que fazem parte desta composição também foram aproveitados, por meio dos Históricos das Disciplinas da Composição na Grade Origem. "
//					+ UteisTexto.ENTER);
//			int nrDisciplina = 1;
//			for (HistoricoVO historicoFazParte : historicoDisciplinasFazemParteAproveitamentoVOs) {
//				obs.append(UteisTexto.ENTER + "DISCIPLINA ").append(nrDisciplina)
//						.append(" FAZ PARTE COMPOSIÇÃO APROVEITADA");
//				obs.append(UteisTexto.ENTER + " - Disciplina: ").append(historicoFazParte.getDisciplina().getCodigo())
//						.append(" (").append(historicoFazParte.getDisciplina().getNome()).append(") ");
//				obs.append(UteisTexto.ENTER + " - Carga Horária: ")
//						.append(historicoFazParte.getCargaHorariaDisciplina());
//				obs.append(UteisTexto.ENTER + " - Tipo Disciplina: ")
//						.append(historicoFazParte.getDescricaoTipoDisciplinaHistorico());
//				obs.append(UteisTexto.ENTER + " - Média: ").append(historicoFazParte.getMediaFinal_Apresentar());
//				obs.append(UteisTexto.ENTER + " - Frequência: ").append(historicoFazParte.getFrequencia_Apresentar());
//				obs.append(UteisTexto.ENTER + " - Situação: ").append(historicoFazParte.getSituacao_Apresentar());
//				obs.append(UteisTexto.ENTER + "  ");
//				nrDisciplina = nrDisciplina + 1;
//			}
//			disciplinasAproveitadaVOMaeComposicao.setObservacaoAproveitamentoEntreMatriculas(
//					disciplinasAproveitadaVOMaeComposicao.getObservacaoAproveitamentoEntreMatriculas()
//							+ obs.toString());

		}
	}

	public boolean verificarDisciplinaGradeDestinoPodeReceberAproveitamento(
			AproveitamentoDisciplinasEntreMatriculasVO aproveitamentoDisciplinasEntreMatriculasVO,
			GradeDisciplinaVO gradeDisciplinaCorrespondente, UsuarioVO usuario) {
		if ((!gradeDisciplinaCorrespondente.getDisciplinasAproveitadasVO().getDisciplina().getCodigo().equals(0))
				&& ((!gradeDisciplinaCorrespondente.getDisciplinasAproveitadasVO().getCargaHoraria().equals(0))
						|| (!gradeDisciplinaCorrespondente.getDisciplinasAproveitadasVO().getQtdeCargaHorariaConcedido()
								.equals(0))
						|| (!gradeDisciplinaCorrespondente.getDisciplinasAproveitadasVO().getQtdeCreditoConcedido()
								.equals(0)))) {
			// já existe um aproveitamento ou concessao lancada para a disciplina (seja,
			// gravada ou nao). Desta maneira, nao podemos
			// sobrepor a esta informacao pré-existente. Aqui pode ser aproveitamento por
			// concessao de ch ou credito tb.
			return false;
		}
		PeriodoLetivoComHistoricoAlunoVO periodoLetivoComHistoricoAlunoVO = aproveitamentoDisciplinasEntreMatriculasVO
				.getMatriculaDestinoAproveitamentoVO().getMatriculaComHistoricoAlunoVO()
				.getGradeCurricularComHistoricoAlunoVO().getPeriodoLetivoComHistoricoAlunoVOPorCodigo(
						gradeDisciplinaCorrespondente.getPeriodoLetivoVO().getCodigo());
		HistoricoVO historicoDisciplinaGradeDestino = periodoLetivoComHistoricoAlunoVO
				.obterHistoricoAtualGradeDisciplinaVO(gradeDisciplinaCorrespondente.getCodigo());
		if (historicoDisciplinaGradeDestino.getAprovado() ) {
			// se já existe um histórico para este aluno nesta gradeDisciplina indicando que
			// o aluno
			// está cursando ou aprovada na mesma, entao não podemos permitir que um novo
			// aproveitamento
			// seja lancado para ele.
			return false;
		}
		return true;
	}

	public boolean verificarDisciplinaGradeGrupoOptativaDestinoPodeReceberAproveitamento(
			AproveitamentoDisciplinasEntreMatriculasVO aproveitamentoDisciplinasEntreMatriculasVO,
			GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVOCorrespondente,
			UsuarioVO usuario) {
		if ((!gradeCurricularGrupoOptativaDisciplinaVOCorrespondente.getDisciplinasAproveitadasVO()
				.getGradeDisciplinaVO().getDisciplina().getCodigo().equals(0))
				&& (!gradeCurricularGrupoOptativaDisciplinaVOCorrespondente.getDisciplinasAproveitadasVO()
						.getCargaHoraria().equals(0))) {
			// já existe um aproveitamento ou concessao lancada para a disciplina (seja,
			// gravada ou nao). Desta maneira, nao podemos
			// sobrepor a esta informacao pré-existente. Aqui pode ser aproveitamento por
			// concessao de ch ou credito tb.
			return false;
		}
		PeriodoLetivoVO periodoLetivoDestino = gradeCurricularGrupoOptativaDisciplinaVOCorrespondente
				.getPeriodoLetivoDisciplinaReferenciada();
		if ((periodoLetivoDestino == null) || (periodoLetivoDestino.getCodigo().equals(0))) {
			// se periodoLetivoDestino está nulo é por que o mesmo nao estava montado na
			// gradeCurricularGrupoOptativaDisciplinaVOCorrespondente.
			// Assim vamos ter que percorrer novamente a grade Destino para localizar o
			// periodo letivo deste grupo de optativa e assim
			// chegar ao seu historicoAtual para as validacoes abaixo.
			for (PeriodoLetivoVO periodoLetivoVOGradeDestino : aproveitamentoDisciplinasEntreMatriculasVO
					.getMatriculaDestinoAproveitamentoVO().getGradeCurricularAtual().getPeriodoLetivosVOs()) {
				if (periodoLetivoVOGradeDestino.getGradeCurricularGrupoOptativa().getCodigo()
						.equals(gradeCurricularGrupoOptativaDisciplinaVOCorrespondente.getGradeCurricularGrupoOptativa()
								.getCodigo())) {
					periodoLetivoDestino = periodoLetivoVOGradeDestino;
					gradeCurricularGrupoOptativaDisciplinaVOCorrespondente
							.setPeriodoLetivoDisciplinaReferenciada(periodoLetivoVOGradeDestino);
					break;
				}
			}
		}
		PeriodoLetivoComHistoricoAlunoVO periodoLetivoComHistoricoAlunoVO = aproveitamentoDisciplinasEntreMatriculasVO
				.getMatriculaDestinoAproveitamentoVO().getMatriculaComHistoricoAlunoVO()
				.getGradeCurricularComHistoricoAlunoVO()
				.getPeriodoLetivoComHistoricoAlunoVOPorCodigo(periodoLetivoDestino.getCodigo());
		HistoricoVO historicoDisciplinaGradeDestino = periodoLetivoComHistoricoAlunoVO
				.obterHistoricoAtualGradeCurricularGrupoOptativaVO(
						gradeCurricularGrupoOptativaDisciplinaVOCorrespondente.getCodigo());
		if (historicoDisciplinaGradeDestino.getAprovado()) {
			// se já existe um histórico para este aluno nesta gradeDisciplina indicando que
			// o aluno
			// está cursando ou aprovada na mesma, entao não podemos permitir que um novo
			// aproveitamento
			// seja lancado para ele.
			return false;
		}
		return true;
	}

	/**
	 * Método responsavel por dado um historico de uma GradeDisciplina no qual o
	 * alnuo foi aprovado na gradeOrigem, procurar por uma GradeDisciplina
	 * correspondente (priorizando o mesmo periodo letivo) na gradeDestino. Se
	 * encontrar o mesmo irá gerar o histórico correspondente na gradeDestino. Caso
	 * nao encontre uma GradeDisciplia correspondente este método também irá
	 * procurar por uma GradeCurricularGrupoOptativaDisciplinaVO correspondente.
	 * Garantindo assim, o máximo de aproveitamento direto no momento da
	 * transferencia. O método também irá buscar por uma
	 * GradeCurricularGrupoOptativaDisciplinaVO priorizando o período letivo da
	 * GradeDisciplina aprovada na grade origem.
	 * 
	 * @param matriculaProcessar
	 * @param gradeDestino
	 * @param listaHistoricosAprovadosNaoMapeadosGradeDestino
	 * @param usuario
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void verificandoEGerandoHistoricoGradeDisciplinaAprovadoGradeOrigemComCorrespondenteGradeDestino(
			AproveitamentoDisciplinasEntreMatriculasVO aproveitamentoDisciplinasEntreMatriculasVO, UsuarioVO usuario)
			throws Exception {
		MatriculaVO matriculaOrigem = aproveitamentoDisciplinasEntreMatriculasVO.getMatriculaOrigemAproveitamentoVO();
		MatriculaVO matriculaDestino = aproveitamentoDisciplinasEntreMatriculasVO.getMatriculaDestinoAproveitamentoVO();

		// Iremos percorrer todos os periodos letivos do aluno na matriz curricular
		// origem.
		// Para cada disciplina encontrada, iremos pegar seu historico atual (que
		// refere-se ao historico
		// que deve ser considerado para uma transferencia - pode haver históricos de
		// reprovação anteriores, que
		// não são úteis no ato da transferencia de matriz) e iremos gerar (caso seja
		// correto) um novo histórico
		// na vinculado a nova matriz curricular (gradeDestino).

		for (PeriodoLetivoComHistoricoAlunoVO periodoLetivoComHistoricoAlunoVO : matriculaOrigem
				.getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO()
				.getPeriodoLetivoComHistoricoAlunoVOs()) {
			// Verificando as GradeDisciplina do periodoLetivo que estão aprovadas
			for (GradeDisciplinaComHistoricoAlunoVO gradeDisciplinaComHistoricoAlunoVO : periodoLetivoComHistoricoAlunoVO
					.getGradeDisciplinaComHistoricoAlunoVOs()) {
				// Iremos agora obter uma disciplina correspodente na grade destino para gerar
				// um novo histórico
				if (gradeDisciplinaComHistoricoAlunoVO.getHistoricoAtualAluno().getAprovado()) {
					GradeDisciplinaVO gradeDisciplinaCorrespondente = matriculaDestino.getGradeCurricularAtual()
							.obterGradeDisciplinaCorrespondente(
									gradeDisciplinaComHistoricoAlunoVO.getGradeDisciplinaVO().getDisciplina()
											.getCodigo(),
									gradeDisciplinaComHistoricoAlunoVO.getGradeDisciplinaVO().getCargaHoraria());
					if (gradeDisciplinaCorrespondente != null) {
						// se o aluno possui foi aprovado nesta disciplina / carga horaria na grade
						// origem
						// entao temos que gerar o aproveitamento para ele na grade destino. Contudo,
						// antes
						// tambem precisamos verificar se na grade destino já nao existe um histórico
						// registrado como
						// aprovado, ou como estudando, ou mesmo referente a outro aproveitamento já
						// lançado.
						if (verificarDisciplinaGradeDestinoPodeReceberAproveitamento(
								aproveitamentoDisciplinasEntreMatriculasVO, gradeDisciplinaCorrespondente, usuario)) {
							try {
								gerarAproveitamentoGradeDisciplinaCorrespodente(
										aproveitamentoDisciplinasEntreMatriculasVO, gradeDisciplinaCorrespondente,
										gradeDisciplinaComHistoricoAlunoVO.getHistoricoAtualAluno(), false, usuario);
							} catch (Exception e) {
								aproveitamentoDisciplinasEntreMatriculasVO
										.adicionarHistoricoListaHistoricoNaoAproveitados(
												gradeDisciplinaComHistoricoAlunoVO.getHistoricoAtualAluno());
							}
						} else {
							aproveitamentoDisciplinasEntreMatriculasVO.adicionarHistoricoListaHistoricoNaoAproveitados(
									gradeDisciplinaComHistoricoAlunoVO.getHistoricoAtualAluno());
						}
					} else {
						// Se nao encontramos a gradeDisciplina correspondente, iremos buscar por uma
						// disciplina
						// de um grupoOptativa na gradeDestino que possa ser correspondente ao mesmo.
						// Neste caso
						// estaremos fazendo o aproveitamento do tipo GRADEDISCIPLINA === PARA ===>
						// GRADEGRUPOOPTATIVADISCIPLINA
						GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVOCorrespondente = matriculaDestino
								.getGradeCurricularAtual().obterGradeCurricularGrupoOptativaCorrespondente(
										gradeDisciplinaComHistoricoAlunoVO.getGradeDisciplinaVO());
						if (gradeCurricularGrupoOptativaDisciplinaVOCorrespondente != null) {
							// se o aluno possui foi aprovado nesta disciplina / carga horaria na grade
							// origem
							// entao temos que gerar o aproveitamento para ele na grade destino. Contudo,
							// antes
							// tambem precisamos verificar se na grade destino já nao existe um histórico
							// registrado como
							// aprovado, ou como estudando, ou mesmo referente a outro aproveitamento já
							// lançado.
							if (verificarDisciplinaGradeGrupoOptativaDestinoPodeReceberAproveitamento(
									aproveitamentoDisciplinasEntreMatriculasVO,
									gradeCurricularGrupoOptativaDisciplinaVOCorrespondente, usuario)) {
								try {
									gerarAproveitamentoGradeGrupoOptativaDisciplinaCorrespodente(
											aproveitamentoDisciplinasEntreMatriculasVO,
											gradeCurricularGrupoOptativaDisciplinaVOCorrespondente,
											gradeDisciplinaComHistoricoAlunoVO.getHistoricoAtualAluno(), false,
											usuario);
								} catch (Exception e) {
									aproveitamentoDisciplinasEntreMatriculasVO
											.adicionarHistoricoListaHistoricoNaoAproveitados(
													gradeDisciplinaComHistoricoAlunoVO.getHistoricoAtualAluno());
								}
							} else {
								aproveitamentoDisciplinasEntreMatriculasVO
										.adicionarHistoricoListaHistoricoNaoAproveitados(
												gradeDisciplinaComHistoricoAlunoVO.getHistoricoAtualAluno());
							}
						} else {
							aproveitamentoDisciplinasEntreMatriculasVO.adicionarHistoricoListaHistoricoNaoAproveitados(
									gradeDisciplinaComHistoricoAlunoVO.getHistoricoAtualAluno());
						}
					}
				}
			}
		}
	}

	/**
	 * Método responsavel por dado um historico de uma
	 * GradeCurricularGrupoOptativaDisciplina no qual o alnuo foi aprovado na
	 * gradeOrigem, procurar por uma GradeCurricularGrupoOptativaDisciplina
	 * correspondente (priorizando o mesmo periodo letivo) na gradeDestino. Se
	 * encontrar o mesmo irá gerar o histórico correspondente na gradeDestino. Caso
	 * nao encontre uma GradeCurricularGrupoOptativaDisciplina correspondente este
	 * método também irá procurar por uma GradeDisciplina correspondente. Garantindo
	 * assim, o máximo de aproveitamento direto no momento da transferencia. O
	 * método também irá buscar priorizando o período letivo do historico Origem
	 */
	private void verificandoEGerandoHistoricoGradeCurricularGrupoOptativaDisciplinaAprovadoGradeOrigemComCorrespondenteGradeDestino(
			AproveitamentoDisciplinasEntreMatriculasVO aproveitamentoDisciplinasEntreMatriculasVO, UsuarioVO usuario)
			throws Exception {
		MatriculaVO matriculaOrigem = aproveitamentoDisciplinasEntreMatriculasVO.getMatriculaOrigemAproveitamentoVO();
		MatriculaVO matriculaDestino = aproveitamentoDisciplinasEntreMatriculasVO.getMatriculaDestinoAproveitamentoVO();

		// Iremos percorrer todos os periodos letivos do aluno na matriz curricular
		// origem.
		// Para cada disciplina encontrada de um GradeCurricularGrupoOptativaDisciplina,
		// iremos pegar seu historico atual
		// e iremos gerar (caso seja correto) um novo histórico na vinculado a nova
		// matriz curricular (gradeDestino).
		for (PeriodoLetivoComHistoricoAlunoVO periodoLetivoComHistoricoAlunoVO : matriculaOrigem
				.getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO()
				.getPeriodoLetivoComHistoricoAlunoVOs()) {
			// Verificando as GradeCurricularGrupoOptativaDisciplina do periodoLetivo que
			// estão aprovadas
			for (GradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO gradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO : periodoLetivoComHistoricoAlunoVO
					.getGradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO()) {
				if (gradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO.getHistoricoAtualAluno().getAprovado()) {
					// Obtendo uma disciplina correspodente na grade destino e gerar um novo
					// histórico
					GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVOCorrespondente = matriculaDestino
							.getGradeCurricularAtual().obterGradeCurricularGrupoOptativaCorrespondente(
									gradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO
											.getGradeCurricularGrupoOptativaDisciplinaVO(),
									periodoLetivoComHistoricoAlunoVO.getPeriodoLetivoVO().getPeriodoLetivo());
					if (gradeCurricularGrupoOptativaDisciplinaVOCorrespondente != null) {
						if (verificarDisciplinaGradeGrupoOptativaDestinoPodeReceberAproveitamento(
								aproveitamentoDisciplinasEntreMatriculasVO,
								gradeCurricularGrupoOptativaDisciplinaVOCorrespondente, usuario)) {
							try {
								gerarAproveitamentoGradeGrupoOptativaDisciplinaCorrespodente(
										aproveitamentoDisciplinasEntreMatriculasVO,
										gradeCurricularGrupoOptativaDisciplinaVOCorrespondente,
										gradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO
												.getHistoricoAtualAluno(),
										false, usuario);
							} catch (Exception e) {
								aproveitamentoDisciplinasEntreMatriculasVO
										.adicionarHistoricoListaHistoricoNaoAproveitados(
												gradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO
														.getHistoricoAtualAluno());
							}
						} else {
							aproveitamentoDisciplinasEntreMatriculasVO.adicionarHistoricoListaHistoricoNaoAproveitados(
									gradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO.getHistoricoAtualAluno());
						}
					} else {
						// Se nao entramos a GradeCurricularGrupoOptativaDisciplinaVO correspondente,
						// iremos buscar por uma disciplina
						// de um gradeDisciplina que possa ser correspondente ao mesmo
						GradeDisciplinaVO gradeDisciplinaVOCorrespondente = matriculaDestino.getGradeCurricularAtual()
								.obterGradeDisciplinaCorrespondente(
										gradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO
												.getGradeCurricularGrupoOptativaDisciplinaVO(),
										periodoLetivoComHistoricoAlunoVO.getPeriodoLetivoVO().getPeriodoLetivo());
						if (gradeDisciplinaVOCorrespondente != null) {
							if (verificarDisciplinaGradeDestinoPodeReceberAproveitamento(
									aproveitamentoDisciplinasEntreMatriculasVO, gradeDisciplinaVOCorrespondente,
									usuario)) {
								try {
									gerarAproveitamentoGradeDisciplinaCorrespodente(
											aproveitamentoDisciplinasEntreMatriculasVO, gradeDisciplinaVOCorrespondente,
											gradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO
													.getHistoricoAtualAluno(),
											false, usuario);
								} catch (Exception e) {
									aproveitamentoDisciplinasEntreMatriculasVO
											.adicionarHistoricoListaHistoricoNaoAproveitados(
													gradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO
															.getHistoricoAtualAluno());
								}
							} else {
								aproveitamentoDisciplinasEntreMatriculasVO
										.adicionarHistoricoListaHistoricoNaoAproveitados(
												gradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO
														.getHistoricoAtualAluno());
							}
						} else {
							aproveitamentoDisciplinasEntreMatriculasVO.adicionarHistoricoListaHistoricoNaoAproveitados(
									gradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO.getHistoricoAtualAluno());
						}
					}
				}
			}
		}
	}

	/**
	 * Iremos processar os históricos que o aluno está aprovado mas estão fora da
	 * grade origem do mesmo. Isto para verificar se estes históricos que eram foram
	 * da grade origem, não podem ser aproveitados por correspodencia direta na
	 * grade destino
	 * 
	 * @author Otimize - 28 de jul de 2016
	 * @param aproveitamentoDisciplinasEntreMatriculasVO
	 * @param usuario
	 * @throws Exception
	 */
	private void verificandoEGerandoHistoricosForaGradeAprovadosGradeOrigemPorCorrespodenciaDireta(
			AproveitamentoDisciplinasEntreMatriculasVO aproveitamentoDisciplinasEntreMatriculasVO, UsuarioVO usuario)
			throws Exception {
		MatriculaVO matriculaOrigem = aproveitamentoDisciplinasEntreMatriculasVO.getMatriculaOrigemAproveitamentoVO();
		MatriculaVO matriculaDestino = aproveitamentoDisciplinasEntreMatriculasVO.getMatriculaDestinoAproveitamentoVO();

		// Iremos percorrer todos os históricos fora da grade origem para processamento
		// Tendem a ser históricos que o aluno está estudando como equivalente, para
		// pagar outra
		// disciplina de sua matriz (mapa de equivalencia). Porém neste método iremos
		// avaliar se o
		// historico em questõa na gradeDestino não pode ser aproveitado de forma direta
		// - por correspodencia.
		List<HistoricoVO> historicosForaGradeOrigem = matriculaOrigem.getMatriculaComHistoricoAlunoVO()
				.getGradeCurricularComHistoricoAlunoVO().getHistoricosDisciplinasForaGradeCurricular();

		for (HistoricoVO historicoForaGradeOrigem : historicosForaGradeOrigem) {
			if (historicoForaGradeOrigem.getAprovado()) {
				// primeiro vamos tentar obter uma gradeDisciplina na grade destino, no qual o
				// histórico possa ser vinculado
				GradeDisciplinaVO gradeDisciplinaCorrespondente = matriculaDestino.getGradeCurricularAtual()
						.obterGradeDisciplinaCorrespondente(historicoForaGradeOrigem.getDisciplina().getCodigo(),
								historicoForaGradeOrigem.getCargaHorariaDisciplina());
				if (gradeDisciplinaCorrespondente != null) {
					// se encontramos então vamos gerar o histórico na gradeDestino vinculado a esta
					// gradeDisciplina encontrada.
					if (verificarDisciplinaGradeDestinoPodeReceberAproveitamento(
							aproveitamentoDisciplinasEntreMatriculasVO, gradeDisciplinaCorrespondente, usuario)) {
						try {
							gerarAproveitamentoGradeDisciplinaCorrespodente(aproveitamentoDisciplinasEntreMatriculasVO,
									gradeDisciplinaCorrespondente, historicoForaGradeOrigem, false, usuario);
						} catch (Exception e) {
							aproveitamentoDisciplinasEntreMatriculasVO
									.adicionarHistoricoListaHistoricoNaoAproveitados(historicoForaGradeOrigem);
						}
					} else {
						aproveitamentoDisciplinasEntreMatriculasVO
								.adicionarHistoricoListaHistoricoNaoAproveitados(historicoForaGradeOrigem);
					}
				} else {
					GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVOCorrespondente = matriculaDestino
							.getGradeCurricularAtual().obterGradeCurricularGrupoOptativaCorrespondente(
									historicoForaGradeOrigem.getDisciplina().getCodigo(),
									historicoForaGradeOrigem.getCargaHorariaDisciplina());
					if (gradeCurricularGrupoOptativaDisciplinaVOCorrespondente != null) {
						if (verificarDisciplinaGradeGrupoOptativaDestinoPodeReceberAproveitamento(
								aproveitamentoDisciplinasEntreMatriculasVO,
								gradeCurricularGrupoOptativaDisciplinaVOCorrespondente, usuario)) {
							try {
								gerarAproveitamentoGradeGrupoOptativaDisciplinaCorrespodente(
										aproveitamentoDisciplinasEntreMatriculasVO,
										gradeCurricularGrupoOptativaDisciplinaVOCorrespondente,
										historicoForaGradeOrigem, false, usuario);
							} catch (Exception e) {
								aproveitamentoDisciplinasEntreMatriculasVO
										.adicionarHistoricoListaHistoricoNaoAproveitados(historicoForaGradeOrigem);
							}
						} else {
							aproveitamentoDisciplinasEntreMatriculasVO
									.adicionarHistoricoListaHistoricoNaoAproveitados(historicoForaGradeOrigem);
						}
					} else {
						aproveitamentoDisciplinasEntreMatriculasVO
								.adicionarHistoricoListaHistoricoNaoAproveitados(historicoForaGradeOrigem);
					}
				}
			}
		}
	}

	private List<GradeDisciplinaCompostaVO> carregarComposicaoHistoricoOrigemNaoAproveitado(
			HistoricoVO historicoNaoAproveitadoDiretamente, UsuarioVO usuario) throws Exception {
		if (historicoNaoAproveitadoDiretamente.getDisciplinaReferenteAUmGrupoOptativa()) {
			// carregando dados composicao para verificacao
			GradeCurricularGrupoOptativaDisciplinaVO gradeGrupoOptativaComComposicao = getFacadeFactory()
					.getGradeCurricularGrupoOptativaDisciplinaFacade()
					.consultarPorChavePrimaria(historicoNaoAproveitadoDiretamente
							.getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo(), usuario);
			// atualizando esta informacao pois será utilizada adiante
			historicoNaoAproveitadoDiretamente.getGradeCurricularGrupoOptativaDisciplinaVO()
					.setGradeDisciplinaCompostaVOs(gradeGrupoOptativaComComposicao.getGradeDisciplinaCompostaVOs());
			return gradeGrupoOptativaComComposicao.getGradeDisciplinaCompostaVOs();
		} else {
			// carregando dados composicao para verificacao
			GradeDisciplinaVO gradeComComposicao = getFacadeFactory().getGradeDisciplinaFacade()
					.consultarPorChavePrimaria(historicoNaoAproveitadoDiretamente.getGradeDisciplinaVO().getCodigo(),
							usuario);
			// atualizando esta informacao sobre as disciplinas da composicao
			historicoNaoAproveitadoDiretamente.getGradeDisciplinaVO()
					.setGradeDisciplinaCompostaVOs(gradeComComposicao.getGradeDisciplinaCompostaVOs());
			return gradeComComposicao.getGradeDisciplinaCompostaVOs();
		}
	}

	/**
	 * Verificando se alguma das disciplinas nao aproveitadas até o presente momento
	 * sao compostas. Neste caso, como as diciplinas mãe da composicao nao foram
	 * aproveitadas vamos verificar as filhas das mesmas (disciplinas que fazem
	 * parte da composicao) para verificar a possibilidade de aproveitamento das
	 * mesmas. caso seja possivel a mesma será processada diretamente. Caso
	 * contrário poderão ainda ser tratadas por meio de mapas de equivalencias
	 * abaixo.
	 * 
	 * @author Otimize - 28 de jul de 2016
	 * @param aproveitamentoDisciplinasEntreMatriculasVO
	 * @param usuario
	 * @throws Exception
	 */
	private void verificandoEGerandoHistoricosDisciplinasFazemParteComposicaoGradeOrigemPorCorrespodenciaDireta(
			AproveitamentoDisciplinasEntreMatriculasVO aproveitamentoDisciplinasEntreMatriculasVO, UsuarioVO usuario)
			throws Exception {
		MatriculaVO matriculaOrigem = aproveitamentoDisciplinasEntreMatriculasVO.getMatriculaOrigemAproveitamentoVO();
		MatriculaVO matriculaDestino = aproveitamentoDisciplinasEntreMatriculasVO.getMatriculaDestinoAproveitamentoVO();

		int i = aproveitamentoDisciplinasEntreMatriculasVO.getHistoricoNaoAproveitados().size() - 1;
		while (i >= 0) {
			HistoricoVO historicoNaoAproveitadoDiretamente = (HistoricoVO) aproveitamentoDisciplinasEntreMatriculasVO
					.getHistoricoNaoAproveitados().get(i);
			if (historicoNaoAproveitadoDiretamente.getHistoricoDisciplinaComposta()) {
				List<GradeDisciplinaCompostaVO> disciplinasFazemParteComposicao = carregarComposicaoHistoricoOrigemNaoAproveitado(
						historicoNaoAproveitadoDiretamente, usuario);
				HistoricoVO historicoAproveitarDisciplinaFazParteComposicao = null;
				for (GradeDisciplinaCompostaVO disciplinaFazParteComposicao : disciplinasFazemParteComposicao) {
					historicoAproveitarDisciplinaFazParteComposicao = matriculaOrigem.getMatriculaComHistoricoAlunoVO()
							.getGradeCurricularComHistoricoAlunoVO().obterHistoricoAtualPorDisciplinaCargaHoraria(
									disciplinaFazParteComposicao.getDisciplina().getCodigo(),
									disciplinaFazParteComposicao.getCargaHoraria());
					if ((!historicoAproveitarDisciplinaFazParteComposicao.getCodigo().equals(0))
							&& (historicoAproveitarDisciplinaFazParteComposicao.getAprovado())) {
						// Se entrarmos aqui é por que já estamos com o historico de uma disciplina que
						// faz parte da composicao.
						// Como sabemos que este historico está com a situacao APROVADO entao vamos
						// agora buscar por uma correspondencia
						// na grade destino.

						GradeDisciplinaVO gradeDisciplinaCorrespondente = matriculaDestino.getGradeCurricularAtual()
								.obterGradeDisciplinaCorrespondente(
										historicoAproveitarDisciplinaFazParteComposicao.getDisciplina().getCodigo(),
										historicoAproveitarDisciplinaFazParteComposicao.getCargaHorariaDisciplina());
						if (gradeDisciplinaCorrespondente != null) {
							// se encontramos então vamos gerar o histórico na gradeDestino vinculado a esta
							// gradeDisciplina encontrada.
							if (verificarDisciplinaGradeDestinoPodeReceberAproveitamento(
									aproveitamentoDisciplinasEntreMatriculasVO, gradeDisciplinaCorrespondente,
									usuario)) {
								try {
									gerarAproveitamentoGradeDisciplinaCorrespodente(
											aproveitamentoDisciplinasEntreMatriculasVO, gradeDisciplinaCorrespondente,
											historicoAproveitarDisciplinaFazParteComposicao, false, usuario);
								} catch (Exception e) {
									aproveitamentoDisciplinasEntreMatriculasVO
											.adicionarHistoricoListaHistoricoNaoAproveitados(
													historicoAproveitarDisciplinaFazParteComposicao);
								}
							} else {
								aproveitamentoDisciplinasEntreMatriculasVO
										.adicionarHistoricoListaHistoricoNaoAproveitados(
												historicoAproveitarDisciplinaFazParteComposicao);
							}
						} else {
							GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVOCorrespondente = matriculaDestino
									.getGradeCurricularAtual().obterGradeCurricularGrupoOptativaCorrespondente(
											historicoAproveitarDisciplinaFazParteComposicao.getDisciplina().getCodigo(),
											historicoAproveitarDisciplinaFazParteComposicao
													.getCargaHorariaDisciplina());
							if (gradeCurricularGrupoOptativaDisciplinaVOCorrespondente != null) {
								if (verificarDisciplinaGradeGrupoOptativaDestinoPodeReceberAproveitamento(
										aproveitamentoDisciplinasEntreMatriculasVO,
										gradeCurricularGrupoOptativaDisciplinaVOCorrespondente, usuario)) {
									try {
										gerarAproveitamentoGradeGrupoOptativaDisciplinaCorrespodente(
												aproveitamentoDisciplinasEntreMatriculasVO,
												gradeCurricularGrupoOptativaDisciplinaVOCorrespondente,
												historicoAproveitarDisciplinaFazParteComposicao, false, usuario);
									} catch (Exception e) {
										aproveitamentoDisciplinasEntreMatriculasVO
												.adicionarHistoricoListaHistoricoNaoAproveitados(
														historicoAproveitarDisciplinaFazParteComposicao);
									}
								} else {
									aproveitamentoDisciplinasEntreMatriculasVO
											.adicionarHistoricoListaHistoricoNaoAproveitados(
													historicoAproveitarDisciplinaFazParteComposicao);
								}
							} else {
								aproveitamentoDisciplinasEntreMatriculasVO
										.adicionarHistoricoListaHistoricoNaoAproveitados(
												historicoAproveitarDisciplinaFazParteComposicao);
							}
						}
					}
				}
			}
			i = i - 1;
		}
	}

	public boolean verificarHistoricoJaFoiAproveitadoOutraInteracao(
			AproveitamentoDisciplinasEntreMatriculasVO aproveitamentoDisciplinasEntreMatriculasVO,
			HistoricoVO historicoVerificar) {
		for (HistoricoVO historicoAproveitado : aproveitamentoDisciplinasEntreMatriculasVO.getHistoricoAproveitados()) {
			if (historicoAproveitado.getCodigo().equals(historicoVerificar.getCodigo())) {
				return true;
			}
		}
		return false;
	}

	private boolean verificarAlunoAptoParaCursarDeterminadoMapaEquivalencia(
			AproveitamentoDisciplinasEntreMatriculasVO aproveitamentoDisciplinasEntreMatriculasVO,
			MapaEquivalenciaDisciplinaVO mapaEquivalenciaCorrespondente, UsuarioVO usuario) {
		// primeiro temos que percorrer as disciplinas da matriz destino que este mapa
		// pode vir
		// a pagar (ou seja, as disciplinas que o aluno irá cursar por equivalencia -
		// disciplinas da matriz destino).
		// Para cada disciplina deste mapa, temos que garantir que a mesma já não está
		// aprovada e/ou cursando e/ou
		// já foi registrado aproveitamento para ela. Caso isto ocorra, significa que o
		// mapa nao pode ser utilizado,
		// pois a disciplina que ele iria pagar já está comprometida.
		for (MapaEquivalenciaDisciplinaMatrizCurricularVO mapaEquivalenciaDisciplinaMatrizCurricularVO : mapaEquivalenciaCorrespondente
				.getMapaEquivalenciaDisciplinaMatrizCurricularVOs()) {
			Integer disciplinaVer = mapaEquivalenciaDisciplinaMatrizCurricularVO.getDisciplinaVO().getCodigo();
			Integer cargaHorariaVer = mapaEquivalenciaDisciplinaMatrizCurricularVO.getCargaHoraria();

			GradeDisciplinaVO gradeDisciplinaSerPagaMatrizDestino = aproveitamentoDisciplinasEntreMatriculasVO
					.getMatriculaDestinoAproveitamentoVO().getGradeCurricularAtual()
					.obterGradeDisciplinaCorrespondente(disciplinaVer, cargaHorariaVer);
			if (gradeDisciplinaSerPagaMatrizDestino != null) {
				// precisamos ter certeza que esta disciplina nao está aprovada ou cursando ou
				// já aproveitada. O que vamos verificar abaixo.
				if (!verificarDisciplinaGradeDestinoPodeReceberAproveitamento(
						aproveitamentoDisciplinasEntreMatriculasVO, gradeDisciplinaSerPagaMatrizDestino, usuario)) {
					// se a disciplina a pagar da matriz nao pode receber um aproveitamento, entao o
					// mapa nao pode ser utilizado
					return false;
				}
			} else {
				GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaSerPagaMatrizDestino = aproveitamentoDisciplinasEntreMatriculasVO
						.getMatriculaDestinoAproveitamentoVO().getGradeCurricularAtual()
						.obterGradeCurricularGrupoOptativaCorrespondente(disciplinaVer, cargaHorariaVer);
				if (gradeCurricularGrupoOptativaSerPagaMatrizDestino != null) {
					if (!verificarDisciplinaGradeGrupoOptativaDestinoPodeReceberAproveitamento(
							aproveitamentoDisciplinasEntreMatriculasVO,
							gradeCurricularGrupoOptativaSerPagaMatrizDestino, usuario)) {
						// se a disciplina a pagar da matriz nao pode receber um aproveitamento, entao o
						// mapa nao pode ser utilizado
						return false;
					}
				} else {
					// se chegarmos aqui é por que nao encontramos uma disciplina compativel na
					// gradeDisciplina e nem na GradeCurricularGrupoOptativa
					// assim, temos que o mapa nao é aplicável para esta grade, pois nem todas as
					// suas disciplinas estao neste tipo de disciplina. Isto
					// pode ocorrer pois algumas equivalencias podem estar em nivel de filhas da
					// composicao. Mas neste caso este mapa de fato nao pode
					// ser tratado aqui, mas sim, no metodo que gerencia o aproveitamento de
					// disciplinas compostas.
					return false;
				}
			}
		}
		return true;
	}

	private int obterNrDisciplinasCursarMapaEquivalenciaAlunoAprovado(
			AproveitamentoDisciplinasEntreMatriculasVO aproveitamentoDisciplinasEntreMatriculasVO,
			MapaEquivalenciaDisciplinaVO mapaDisponivelEAplicavel, UsuarioVO usuario) {
		int nrDisciplinasCursarAlunoJaAprovado = 0;
		for (MapaEquivalenciaDisciplinaCursadaVO disciplinaCursar : mapaDisponivelEAplicavel
				.getMapaEquivalenciaDisciplinaCursadaVOs()) {
			for (HistoricoVO historicoNaoAproveitadoAinda : aproveitamentoDisciplinasEntreMatriculasVO
					.getHistoricoNaoAproveitados()) {
				if ((disciplinaCursar.getDisciplinaVO().getCodigo()
						.equals(historicoNaoAproveitadoAinda.getDisciplina().getCodigo()))
						&& (disciplinaCursar.getCargaHoraria()
								.equals(historicoNaoAproveitadoAinda.getCargaHorariaDisciplina()))) {
					nrDisciplinasCursarAlunoJaAprovado++;
				}
			}
		}
		return nrDisciplinasCursarAlunoJaAprovado;
	}

	/**
	 * Método responsável por obter uma Mapa de Equivalencia que possa ser utilizado
	 * de forma a aproveitar um histórico fora da grade que o aluno já foi aprovado
	 * na grade origem. Para que uma mapa possa ser utilizado, é necessário que
	 * alguma disciplina a ser cursada (do mapa) já tenha sido estuda pelo aluno
	 * (iremos utilizar o historicoAproveitarPorEquivalencia para fazer esta
	 * verificação) e que todas as disciplinas do mapa da matriz destino (que serão
	 * estudas por equivalencia) ainda estejam pendentes para o aluno na matriz
	 * destino. Ou seja, o aluno não pode estar aprovado em uma disciplina que o
	 * mapa faz referencia que deve ser estuda por equivalencia. Este método já
	 * possui uma lógica para determinar qual o melhor mapa a ser aplicado para o
	 * aluno em uma determinada disciplina (historicoAproveitarPorEquivalencia). Ele
	 * utiliza as seguintes regras de prioridade: a) primeiro verifica se disciplina
	 * em avaliação já não estava sendo estuda em um mapa na grade origem. Se sim,
	 * procura por um mapa identico na grade destino, caso exista, este mapa será
	 * utilziado, para manter o alnuo com a sensação de que nada foi alterado na sua
	 * vida academica. b) Caso a regra acima nao se aplica o sistema irá buscar pelo
	 * mapa que possa ser resolvido por completo e que tenha mais disciplinas
	 * envolvidas. c) Caso ainda não encontre um mapa resolvido, então o sistema irá
	 * buscar pelo mapa que tenha mais disciplinas resolvidas, dado os históricos
	 * aproveitados para o aluno na grade destino.
	 */
	private MapaEquivalenciaDisciplinaVO obterMapaEquivalenciaAplicavelDisciplinaAprovadaGradeOrigem(
			AproveitamentoDisciplinasEntreMatriculasVO aproveitamentoDisciplinasEntreMatriculasVO,
			HistoricoVO historicoAproveitarPorEquivalencia, UsuarioVO usuario) throws Exception {
		MatriculaVO matriculaDestino = aproveitamentoDisciplinasEntreMatriculasVO.getMatriculaDestinoAproveitamentoVO();

		List<MapaEquivalenciaDisciplinaVO> listaMapasValidos = getFacadeFactory().getMapaEquivalenciaDisciplinaFacade()
				.consultarPorMapaEquivalenciaMatrizCurricularDisciplinaCursada(
						matriculaDestino.getGradeCurricularAtual().getCodigo(),
						historicoAproveitarPorEquivalencia.getDisciplina().getCodigo(),
						historicoAproveitarPorEquivalencia.getCargaHorariaDisciplina(),
						aproveitamentoDisciplinasEntreMatriculasVO.getMapaEquivalenciaUtilizadoAproveitamento()
								.getCodigo(),
						NivelMontarDados.TODOS);
		if (listaMapasValidos.isEmpty()) {
			return null;
		}

		List<MapaEquivalenciaDisciplinaVO> listaMapasAplicaveisParaAluno = new ArrayList<MapaEquivalenciaDisciplinaVO>();
		// Agora temos que verificar quais dos mapas encontrados pode ser aplicado para
		// o aluno. Buscando sempre o mapa que
		// pode ter um melhor indice de uso.
		for (MapaEquivalenciaDisciplinaVO mapasDisponiveis : listaMapasValidos) {
			if (verificarAlunoAptoParaCursarDeterminadoMapaEquivalencia(aproveitamentoDisciplinasEntreMatriculasVO,
					mapasDisponiveis, usuario)) {
				// se o mapa é aplicável vamos adicionar o mesmo para uma lista. Posteriomente,
				// iremos processar
				// esta lista para determinar qual destes mapas válidos é o melhor a ser adotado
				// para o aluno na nova grade. Será priorizado o mapa que for resolvido por
				// completo,
				// ou que tiver mais disciplinas resolvidas.
				listaMapasAplicaveisParaAluno.add(mapasDisponiveis);
			}
		}

		MapaEquivalenciaDisciplinaVO melhorMapaAplicar = null;
		Integer nrDisciplinasCursarMelhorMapaAlunoJaAprovado = 0;
		boolean melhorMapaResolvido = false;
		// Neste ponto iremos verificar qual é o melhor mapa a ser aplicado para o aluno
		// da
		// lista de mapas aplicaveis
		for (MapaEquivalenciaDisciplinaVO mapaDisponivelEAplicavel : listaMapasAplicaveisParaAluno) {
			if (melhorMapaAplicar == null) {
				// na primeira interacao assumimos o primeiro mapa como sendo o melhor
				// para o aluno
				melhorMapaAplicar = mapaDisponivelEAplicavel;
				// atualizando estatísticas sobre melhor mapa, no caso ja somamos 1 na lista,
				// pois este mapa
				// foi obtido com base em uma disciplina que está fora da grade destino, mas que
				// existe um mapa
				// correspondente para ele.
				nrDisciplinasCursarMelhorMapaAlunoJaAprovado = 1
						+ obterNrDisciplinasCursarMapaEquivalenciaAlunoAprovado(
								aproveitamentoDisciplinasEntreMatriculasVO, mapaDisponivelEAplicavel, usuario);
				melhorMapaResolvido = (nrDisciplinasCursarMelhorMapaAlunoJaAprovado == mapaDisponivelEAplicavel
						.getMapaEquivalenciaDisciplinaCursadaVOs().size());
			} else {
				// na segunda interacao passamos a comparar o mapa da interacao com o melhor
				// registrado
				// ate o momento. Caso o mapa atual seja melhor, o mesmo passará a ser condirado
				// como
				// o melhor para o aluno.

				// PRIMEIRO - obtemos informacoes mapaInteracao para compara-lo com o melhor
				// mapa até o momento
				int nrDisciplinasCursarMapaAvaliarAlunoJaAprovado = 1
						+ obterNrDisciplinasCursarMapaEquivalenciaAlunoAprovado(
								aproveitamentoDisciplinasEntreMatriculasVO, mapaDisponivelEAplicavel, usuario);
				boolean mapaAvaliarResolvido = (nrDisciplinasCursarMapaAvaliarAlunoJaAprovado == mapaDisponivelEAplicavel
						.getMapaEquivalenciaDisciplinaCursadaVOs().size());
				if (melhorMapaResolvido) {
					// SEGUNDO - neste caso o mapa da interacao só vai ser considerado melhor se o
					// mesmo
					// tambem estiver relvido e tiver mais disciplinas aprovados que o primeiro
					if ((mapaAvaliarResolvido)
							&& (nrDisciplinasCursarMapaAvaliarAlunoJaAprovado > nrDisciplinasCursarMelhorMapaAlunoJaAprovado)) {
						nrDisciplinasCursarMelhorMapaAlunoJaAprovado = nrDisciplinasCursarMapaAvaliarAlunoJaAprovado;
						melhorMapaResolvido = mapaAvaliarResolvido;
						melhorMapaAplicar = mapaDisponivelEAplicavel;
					}
				} else {
					// TERCEIRO - caso o melhor mapa não esteje resolvido, o mapa de interacao será
					// considerado
					// melhor somente se o nr de disciplinas aprovadas for igual ou maior e o numero
					// de disciplinas que o aluno esta cursando for maior
					if (nrDisciplinasCursarMapaAvaliarAlunoJaAprovado > nrDisciplinasCursarMelhorMapaAlunoJaAprovado) {
						nrDisciplinasCursarMelhorMapaAlunoJaAprovado = nrDisciplinasCursarMapaAvaliarAlunoJaAprovado;
						melhorMapaResolvido = mapaAvaliarResolvido;
						melhorMapaAplicar = mapaDisponivelEAplicavel;
					}
				}
			}
		}
		return melhorMapaAplicar;
	}

	private void gerarHistoricoCorrespondenteGradeDestinoReferenteDisciplinaMapaEquivalencia(
			AproveitamentoDisciplinasEntreMatriculasVO aproveitamentoDisciplinasEntreMatriculasVO,
			MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaVO,
			MapaEquivalenciaDisciplinaCursadaVO mapaEquivalenciaDisciplinaCursadaVO, HistoricoVO historicoOrigemAluno,
			Boolean mapaEquivalenciaComEquivalenciaDiretaUmaDisciplina, UsuarioVO usuario) throws Exception {
		DisciplinasAproveitadasVO novaDisciplinasAproveitadaVO = new DisciplinasAproveitadasVO();

		// inicializando os dados do aproveitamento com base no historicoOrigemAluno
		inicializarDadosBasicosNovoAproveitamentoMapaEquivalenciaDisciplinaCursada(novaDisciplinasAproveitadaVO,
				mapaEquivalenciaDisciplinaCursadaVO, aproveitamentoDisciplinasEntreMatriculasVO, historicoOrigemAluno,
				usuario);

		mapaEquivalenciaDisciplinaCursadaVO.setDisciplinasAproveitadasVO(novaDisciplinasAproveitadaVO);

		aproveitamentoDisciplinasEntreMatriculasVO.getAproveitamentoDisciplinaVO()
				.adicionarObjDisciplinasAproveitadasVOs(novaDisciplinasAproveitadaVO);
		aproveitamentoDisciplinasEntreMatriculasVO.getAproveitamentoDisciplinaVO()
				.adicionarDisciplinasAproveitadasForaDaGradeMapaEquivalencia(mapaEquivalenciaDisciplinaCursadaVO);

		// adicionando o histórico para lista de históricos que foram aproveitados
		aproveitamentoDisciplinasEntreMatriculasVO.getHistoricoAproveitados().add(historicoOrigemAluno);

		// removendo histório da lista de nao aproveitados - passo importante para o
		// processamento das proximas disciplinas
		int i = aproveitamentoDisciplinasEntreMatriculasVO.getHistoricoNaoAproveitados().size() - 1;
		while (i >= 0) {
			HistoricoVO historico = (HistoricoVO) aproveitamentoDisciplinasEntreMatriculasVO
					.getHistoricoNaoAproveitados().get(i);
			if (historico.getCodigo().equals(historicoOrigemAluno.getCodigo())) {
				aproveitamentoDisciplinasEntreMatriculasVO.getHistoricoNaoAproveitados().remove(i);
				break;
			}
			i = i - 1;
		}
	}

	public GradeCurricularGrupoOptativaDisciplinaVO obterHistoricoAtualGradeGrupoOptativaDestinoPodeReceberAproveitamento(
			AproveitamentoDisciplinasEntreMatriculasVO aproveitamentoDisciplinasEntreMatriculasVO,
			GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVOCorrespondente,
			UsuarioVO usuario) {
		PeriodoLetivoVO periodoLetivoLocalizado = null;
		for (PeriodoLetivoVO periodoLetivoVOGradeDestino : aproveitamentoDisciplinasEntreMatriculasVO
				.getMatriculaDestinoAproveitamentoVO().getGradeCurricularAtual().getPeriodoLetivosVOs()) {
			if (periodoLetivoVOGradeDestino.getGradeCurricularGrupoOptativa().getCodigo()
					.equals(gradeCurricularGrupoOptativaDisciplinaVOCorrespondente.getGradeCurricularGrupoOptativa()
							.getCodigo())) {
				periodoLetivoLocalizado = periodoLetivoVOGradeDestino;
				break;
			}
		}
		for (GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO : periodoLetivoLocalizado
				.getGradeCurricularGrupoOptativa().getGradeCurricularGrupoOptativaDisciplinaVOs()) {
			if (gradeCurricularGrupoOptativaDisciplinaVO.getCodigo()
					.equals(gradeCurricularGrupoOptativaDisciplinaVOCorrespondente.getCodigo())) {
				return gradeCurricularGrupoOptativaDisciplinaVO;
			}
		}
		return null;
		// PeriodoLetivoComHistoricoAlunoVO periodoLetivoComHistoricoAlunoVO =
		// aproveitamentoDisciplinasEntreMatriculasVO.getMatriculaDestinoAproveitamentoVO().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getPeriodoLetivoComHistoricoAlunoVOPorCodigo(periodoLetivoDestino.getCodigo());
		// HistoricoVO historicoDisciplinaGradeDestino =
		// periodoLetivoDestino.obterHistoricoAtualGradeCurricularGrupoOptativaVO(gradeCurricularGrupoOptativaDisciplinaVOCorrespondente.getCodigo());
		// return historicoDisciplinaGradeDestino;
	}

	/**
	 * Dado um mapa de equivalencia já selecionado e um historico que já será
	 * utilizado para abater uma das disciplinas a serem cursadas pelo aluno, este
	 * método irá gerar o histórico correspondente na gradeDestino deste histórico
	 * fornecido como parametro (historicoNaoAproveitadoDiretamente) e também irá
	 * bucar por outros históricos no qual o aluno já tenha aproveitamento (e/ou
	 * ainda) está pendente para ser aproveitado, dentro do mesmo mapa de
	 * equivalencia. Para cada histórico aproveitado o mesmo será registrado na
	 * lista que controla historicos nesta situação.
	 */
	private void gerarHistoricosGradeDestinoPorMapaEquivalencia(
			AproveitamentoDisciplinasEntreMatriculasVO aproveitamentoDisciplinasEntreMatriculasVO,
			MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaVO, HistoricoVO historicoNaoAproveitadoDiretamente,
			Boolean mapaEquivalenciaComEquivalenciaDiretaUmaDisciplina, UsuarioVO usuario) throws Exception {
		// Primeiramnte vamos gerar as DisciplinaAproveitadaVO para as disciplinas fora
		// da grade a serem cursadas pelo
		// aluno, afim de que estes historicos sejam aproveitados por intermedio do mapa
		// de equivalencia.
		for (MapaEquivalenciaDisciplinaCursadaVO mapaEquivalenciaDisciplinaCursadaVO : mapaEquivalenciaDisciplinaVO
				.getMapaEquivalenciaDisciplinaCursadaVOs()) {
			if ((mapaEquivalenciaDisciplinaCursadaVO.getDisciplinaVO().getCodigo()
					.equals(historicoNaoAproveitadoDiretamente.getDisciplina().getCodigo()))
					&& (mapaEquivalenciaDisciplinaCursadaVO.getCargaHoraria()
							.equals(historicoNaoAproveitadoDiretamente.getCargaHorariaDisciplina()))) {
				// se encontramos a disciplina que refere-se ao objeto
				// historicoNaoAproveitadoDiretamente, entao podemos gerar o historico
				// a partir do mesmo pois, o mesmo já foi localizado e verificado anteriormente
				// a chamada deste metodo
				gerarHistoricoCorrespondenteGradeDestinoReferenteDisciplinaMapaEquivalencia(
						aproveitamentoDisciplinasEntreMatriculasVO, mapaEquivalenciaDisciplinaVO,
						mapaEquivalenciaDisciplinaCursadaVO, historicoNaoAproveitadoDiretamente,
						mapaEquivalenciaComEquivalenciaDiretaUmaDisciplina, usuario);
			} else {
				// Neste caso, teremos que buscar um histórico que possa ser utilizado para
				// ABATER
				// esta disciplina que precisa ser cursada pelo aluno. Teremos que buscar este
				// histórico, dentro
				// das seguintes possibilidades:
				// - dentro dos históricos que ainda não foram aproveitados do aluno. Neste,
				// caso seja encontrado
				// algum histórico para esta disciplina que precisa ser cursada pelo aluno,
				// teremos que verificar
				// se este histórico já não foi aproveitado.
				int j = aproveitamentoDisciplinasEntreMatriculasVO.getHistoricoNaoAproveitados().size() - 1;
				while (j >= 0) {
					HistoricoVO historicoEncontradoVO = (HistoricoVO) aproveitamentoDisciplinasEntreMatriculasVO
							.getHistoricoNaoAproveitados().get(j);
					if ((historicoEncontradoVO.getAprovado())
							&& (historicoEncontradoVO.getDisciplina().getCodigo()
									.equals(mapaEquivalenciaDisciplinaCursadaVO.getDisciplinaVO().getCodigo()))
							&& (historicoEncontradoVO.getCargaHorariaDisciplina()
									.equals(mapaEquivalenciaDisciplinaCursadaVO.getCargaHoraria()))) {
						// se entrarmos aqui é por que encontramos um histórico compatível para
						// disciplina. Logo, temos que gerar um historico correspondente para o aluno
						// na matriz destino.
						gerarHistoricoCorrespondenteGradeDestinoReferenteDisciplinaMapaEquivalencia(
								aproveitamentoDisciplinasEntreMatriculasVO, mapaEquivalenciaDisciplinaVO,
								mapaEquivalenciaDisciplinaCursadaVO, historicoEncontradoVO,
								mapaEquivalenciaComEquivalenciaDiretaUmaDisciplina, usuario);
						break;
					}
					j = j - 1;
				}
			}
		}

		// Segundo, vamos percorrer a matriculaDestino assinalando quais disciplinas
		// serao pagas por meio de um mapa
		// de equivalencia. A questao importante aqui é que tratam-se de disciplinas que
		// o aluno ainda nao esta aprovado
		// e/ou cursando. Assim, utilizaremos a variavel transiente
		// aproveitamentoIncluidoPorMapaEquivalencia como TRUE
		// A mesma ira indicar que o mapa de equivalencia esta sendo incluido de dentro
		// do Aproveitamento de Disciplina
		// em virtude de disciplinas aproveitadas de outra matricula que o aluno possui.
		for (MapaEquivalenciaDisciplinaMatrizCurricularVO mapaEquivalenciaDisciplinaMatrizCurricular : mapaEquivalenciaDisciplinaVO
				.getMapaEquivalenciaDisciplinaMatrizCurricularVOs()) {
			Integer disciplinaMatrizMarcarMapa = mapaEquivalenciaDisciplinaMatrizCurricular.getDisciplinaVO()
					.getCodigo();
			Integer chdisciplinaMatrizMarcarMapa = mapaEquivalenciaDisciplinaMatrizCurricular.getCargaHoraria();

			GradeDisciplinaVO gradeDisciplinaSerPagaMatrizDestino = aproveitamentoDisciplinasEntreMatriculasVO
					.getMatriculaDestinoAproveitamentoVO().getGradeCurricularAtual()
					.obterGradeDisciplinaCorrespondente(disciplinaMatrizMarcarMapa, chdisciplinaMatrizMarcarMapa);
			if (gradeDisciplinaSerPagaMatrizDestino != null) {
				// Se entrarmos aqui é por que encontramos um GradeDisciplina que será paga por
				// meiom do mapa de equivalencia.
				// Entao precisamos localizar seu historicoAtual e fazer as marcacoes para que a
				// tela de aproveitamento compreenda
				// que a mesma esta sendo cumprida por meio de um mapa
				gradeDisciplinaSerPagaMatrizDestino.setAproveitamentoIncluidoPorMapaEquivalencia(Boolean.TRUE);
				gradeDisciplinaSerPagaMatrizDestino.getHistoricoAtualAluno()
						.setSituacao(SituacaoHistorico.CURSANDO_POR_EQUIVALENCIA.getValor());
				gradeDisciplinaSerPagaMatrizDestino.getHistoricoAtualAluno()
						.setMapaEquivalenciaDisciplina(mapaEquivalenciaDisciplinaVO);
				gradeDisciplinaSerPagaMatrizDestino.getHistoricoAtualAluno()
						.setMapaEquivalenciaDisciplinaMatrizCurricular(mapaEquivalenciaDisciplinaMatrizCurricular);
			} else {
				GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaSerPagaMatrizDestino = aproveitamentoDisciplinasEntreMatriculasVO
						.getMatriculaDestinoAproveitamentoVO().getGradeCurricularAtual()
						.obterGradeCurricularGrupoOptativaCorrespondente(disciplinaMatrizMarcarMapa,
								chdisciplinaMatrizMarcarMapa);
				if (gradeCurricularGrupoOptativaSerPagaMatrizDestino != null) {
					// Se entrarmos aqui é por que encontramos um GradeCurricularGrupoOptativa que
					// será paga por meiom do mapa de equivalencia.
					// Entao precisamos localizar seu historicoAtual e fazer as marcacoes para que a
					// tela de aproveitamento compreenda
					// que a mesma esta sendo cumprida por meio de um mapa
					GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaSerPagaMatrizDestinoAtualizar = obterHistoricoAtualGradeGrupoOptativaDestinoPodeReceberAproveitamento(
							aproveitamentoDisciplinasEntreMatriculasVO,
							gradeCurricularGrupoOptativaSerPagaMatrizDestino, usuario);
					gradeCurricularGrupoOptativaSerPagaMatrizDestinoAtualizar
							.setAproveitamentoIncluidoPorMapaEquivalencia(Boolean.TRUE);
					gradeCurricularGrupoOptativaSerPagaMatrizDestinoAtualizar.getDisciplinasAproveitadasVO()
							.setPeriodoletivoGrupoOptativaVO(gradeCurricularGrupoOptativaSerPagaMatrizDestino
									.getPeriodoLetivoDisciplinaReferenciada());
					gradeCurricularGrupoOptativaSerPagaMatrizDestinoAtualizar.getHistoricoAtualAluno()
							.setSituacao(SituacaoHistorico.CURSANDO_POR_EQUIVALENCIA.getValor());
					gradeCurricularGrupoOptativaSerPagaMatrizDestinoAtualizar.getHistoricoAtualAluno()
							.setMapaEquivalenciaDisciplina(mapaEquivalenciaDisciplinaVO);
					gradeCurricularGrupoOptativaSerPagaMatrizDestinoAtualizar.getHistoricoAtualAluno()
							.setMapaEquivalenciaDisciplinaMatrizCurricular(mapaEquivalenciaDisciplinaMatrizCurricular);

					// Como estamos trabalhando com dois objetos: gradeCurricular e
					// gradeCurricularComHistorico. Acima atualizamos o historico
					// referente a gradeCurricular (que de fato é apresentado no JSP para o usuario
					// na tela de aproveitamento). Contudo, temos
					// que atualizar tambem o gradeCurricularComHistorico pois em alguns metodos o
					// historicoAtual registrado no mesmo é utilizado
					// para atualizar os dados da gradeCurrilar apresentada ao aluno.
					HistoricoVO historicoAtualNaMatrizComHistorico = aproveitamentoDisciplinasEntreMatriculasVO
							.getMatriculaDestinoAproveitamentoVO().getMatriculaComHistoricoAlunoVO()
							.getGradeCurricularComHistoricoAlunoVO().obterHistoricoAtualGradeCurricularGrupoOptativaVO(
									gradeCurricularGrupoOptativaSerPagaMatrizDestino.getDisciplina().getCodigo(),
									gradeCurricularGrupoOptativaSerPagaMatrizDestino.getCargaHoraria());
					historicoAtualNaMatrizComHistorico
							.setSituacao(SituacaoHistorico.CURSANDO_POR_EQUIVALENCIA.getValor());
					historicoAtualNaMatrizComHistorico.setMapaEquivalenciaDisciplina(mapaEquivalenciaDisciplinaVO);
					historicoAtualNaMatrizComHistorico
							.setMapaEquivalenciaDisciplinaMatrizCurricular(mapaEquivalenciaDisciplinaMatrizCurricular);
				}
			}

		}
	}

	/**
	 * Todos os históricos que não foram aproveitados por correspondencia direta
	 * serão tratados no método abaixo, que buscará pelo melhor mapa de equivalencia
	 * a ser aplicado no sentido de aproveitar estes tipo de histórico.
	 * 
	 * @author Otimize - 28 de jul de 2016
	 * @param aproveitamentoDisciplinasEntreMatriculasVO
	 * @param usuario
	 * @throws Exception
	 */
	private void verificandoEGerandoHistoricosNaoAproveitadosGradeDestinoPorMapaEquivalencia(
			AproveitamentoDisciplinasEntreMatriculasVO aproveitamentoDisciplinasEntreMatriculasVO, UsuarioVO usuario)
			throws Exception {
		if (aproveitamentoDisciplinasEntreMatriculasVO.getMapaEquivalenciaUtilizadoAproveitamento().getCodigo()
				.equals(0)) {
			// se nenhum mapa de equivalencia foi fornecido pelo usuário, então é por que
			// esta
			// migração, deverá migrar somente históricos por correspodencia de código/carga
			// horária.
			return;
		}

		// Iremos percorrer todos os históricos NAO aproveitados de forma direta na
		// grade destino
		// Tendem a ser históricos que o aluno está estudando como equivalente, para
		// pagar outra
		// disciplina de sua matriz (mapa de equivalencia).
		List<HistoricoVO> historicosNaoAproveitadosDiretamenteGradeDestino = aproveitamentoDisciplinasEntreMatriculasVO
				.getHistoricoNaoAproveitados();
		int nrHistorico = historicosNaoAproveitadosDiretamenteGradeDestino.size() - 1;
		while ((nrHistorico >= 0) && (nrHistorico < historicosNaoAproveitadosDiretamenteGradeDestino.size())) {
			HistoricoVO historicoNaoAproveitadoDiretamente = historicosNaoAproveitadosDiretamenteGradeDestino
					.get(nrHistorico);
			if ((historicoNaoAproveitadoDiretamente.getAprovado())
					&& (!verificarHistoricoJaFoiAproveitadoOutraInteracao(aproveitamentoDisciplinasEntreMatriculasVO,
							historicoNaoAproveitadoDiretamente))) {
				// se chegarmos aqui é por que não encontramos uma correspondencia direta para a
				// disciplina na grade destino (verificada
				// pelos metodos chamados antes de método). Logo, iremos buscar por uma mapa de
				// equivalencia que possa ser aplicado
				// no histórico fazendo com que o mesmo seja aproveitado.
				MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaVO = obterMapaEquivalenciaAplicavelDisciplinaAprovadaGradeOrigem(
						aproveitamentoDisciplinasEntreMatriculasVO, historicoNaoAproveitadoDiretamente, usuario);
				if (mapaEquivalenciaDisciplinaVO != null) {
					boolean mapaEquivalenciaComEquivalenciaDiretaUmaDisciplina = Boolean.FALSE;
					if ((mapaEquivalenciaDisciplinaVO.getMapaEquivalenciaDisciplinaCursadaVOs().size() == 1)
							&& (mapaEquivalenciaDisciplinaVO.getMapaEquivalenciaDisciplinaMatrizCurricularVOs()
									.size() == 1)) {
						// se o mapa de equivalencia tem somente uma disciplina a ser cursada e somente
						// uma disciplina
						// a ser aprovada na matriz destino, então já iremos gerar o histórico de forma
						// direta na
						// gradeDestino. Isto por que não é necessário aplicar as regras do
						// mapaEqualencia (nota por media, ou maior nota)
						// maior frequencia ou médias das frequencias, ...) Pois, temos somente uma
						// disciplina, levando para outra.
						// isto implicará em melhor perfomance e em dados mais limpos na gradeDestino.
						// Como este é o caso comum,
						// para a maioria das transferencia, também, teremos um ganho de performance
						// considerável, pois não teremos
						// que resolver o mapa na matriz destino.
						mapaEquivalenciaComEquivalenciaDiretaUmaDisciplina = Boolean.TRUE;
					}
					// gerando historicos para o mapa de equivalencia.
					try {
						gerarHistoricosGradeDestinoPorMapaEquivalencia(aproveitamentoDisciplinasEntreMatriculasVO,
								mapaEquivalenciaDisciplinaVO, historicoNaoAproveitadoDiretamente,
								mapaEquivalenciaComEquivalenciaDiretaUmaDisciplina, usuario);
					} catch (Exception e) {
						// em caso de erro em um mapa ou outros serao processados normalmente. como o
						// historico com erro
						// ira permanecer nos historicos nao aproveitados o usuario podera ver os
						// historicos pendentes
					}
				}
			}
			nrHistorico--;
		}
	}

	private HistoricoVO verificarObterHistoricoNaoAproveitadoParaDisciplina(
			AproveitamentoDisciplinasEntreMatriculasVO aproveitamentoDisciplinasEntreMatriculasVO, Integer disciplina,
			Integer cargaHoraria) {
		int i = aproveitamentoDisciplinasEntreMatriculasVO.getHistoricoNaoAproveitados().size() - 1;
		while (i >= 0) {
			HistoricoVO historicoNaoAproveitadoDiretamente = (HistoricoVO) aproveitamentoDisciplinasEntreMatriculasVO
					.getHistoricoNaoAproveitados().get(i);
			if ((historicoNaoAproveitadoDiretamente.getDisciplina().getCodigo().equals(disciplina))
					&& (historicoNaoAproveitadoDiretamente.getCargaHorariaDisciplina().equals(cargaHoraria))) {
				return historicoNaoAproveitadoDiretamente;
			}
			i = i - 1;
		}
		return null;
	}

	private void gerarAproveitamentoDisciplinaFazParteComposicaoDeGradeDisciplinaComposta(
			AproveitamentoDisciplinasEntreMatriculasVO aproveitamentoDisciplinasEntreMatriculasVO,
			DisciplinasAproveitadasVO disciplinasAproveitadaReferenteMaeComposicao,
			List<GradeDisciplinaCompostaVO> listaGradeDisciplinaCompostaVOs,
			GradeDisciplinaVO gradeDisciplinaCompostaCorrespondente,
			GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaCompostaCorrespondente,
			UsuarioVO usuario) throws Exception {
		for (GradeDisciplinaCompostaVO gradeDisciplinaFazParteCompostaVO : listaGradeDisciplinaCompostaVOs) {
			HistoricoVO historicoBaseGerarAproveitamentoFazParteComposicao = gradeDisciplinaFazParteCompostaVO
					.getHistoricoAtualAluno();

			// aqui temos que gerar um novo historico simulado para gradecurricular destino,
			// para que o mesmo seja utilizado na simulacao que vamos
			// fazer de processamento da disciplina mae da composta
			HistoricoVO novoHistoricoSimuladoDisciplinaFazParteComposicao = (HistoricoVO) historicoBaseGerarAproveitamentoFazParteComposicao
					.clone();
			novoHistoricoSimuladoDisciplinaFazParteComposicao.setCodigo(0);
			novoHistoricoSimuladoDisciplinaFazParteComposicao
					.setGradeDisciplinaComposta(gradeDisciplinaFazParteCompostaVO);
			novoHistoricoSimuladoDisciplinaFazParteComposicao
					.setDisciplina(gradeDisciplinaFazParteCompostaVO.getDisciplina());
			novoHistoricoSimuladoDisciplinaFazParteComposicao
					.setCargaHorariaDisciplina(gradeDisciplinaFazParteCompostaVO.getCargaHoraria());
			novoHistoricoSimuladoDisciplinaFazParteComposicao
					.setMatrizCurricular(aproveitamentoDisciplinasEntreMatriculasVO
							.getMatriculaDestinoAproveitamentoVO().getGradeCurricularAtual());
			novoHistoricoSimuladoDisciplinaFazParteComposicao
					.setMatricula(aproveitamentoDisciplinasEntreMatriculasVO.getMatriculaDestinoAproveitamentoVO());
			novoHistoricoSimuladoDisciplinaFazParteComposicao.setHistoricoDisciplinaFazParteComposicao(Boolean.TRUE);

			DisciplinasAproveitadasVO novaDisciplinasAproveitadaFazParteComposicao = new DisciplinasAproveitadasVO();

			novaDisciplinasAproveitadaFazParteComposicao.setGradeDisciplinaVO(gradeDisciplinaFazParteCompostaVO);
			novaDisciplinasAproveitadaFazParteComposicao
					.setGradeDisciplinaCompostaVO(gradeDisciplinaFazParteCompostaVO);
			novaDisciplinasAproveitadaFazParteComposicao.setAproveitamentoDisciplinaVO(
					aproveitamentoDisciplinasEntreMatriculasVO.getAproveitamentoDisciplinaVO());

			if (gradeDisciplinaCompostaCorrespondente != null) {
				// inicializando os dados do aproveitamento com base no historicoOrigemAluno
				inicializarDadosBasicosNovoAproveitamento(novaDisciplinasAproveitadaFazParteComposicao,
						gradeDisciplinaFazParteCompostaVO, null, aproveitamentoDisciplinasEntreMatriculasVO,
						historicoBaseGerarAproveitamentoFazParteComposicao,
						gradeDisciplinaCompostaCorrespondente.getPeriodoLetivoVO(), usuario);
			} else {
				// inicializando os dados do aproveitamento com base no historicoOrigemAluno
				inicializarDadosBasicosNovoAproveitamento(novaDisciplinasAproveitadaFazParteComposicao, null,
						gradeCurricularGrupoOptativaCompostaCorrespondente, aproveitamentoDisciplinasEntreMatriculasVO,
						historicoBaseGerarAproveitamentoFazParteComposicao,
						gradeCurricularGrupoOptativaCompostaCorrespondente.getPeriodoLetivoDisciplinaReferenciada(),
						usuario);
				// apesar do aproveitamento ser de um gupo de optativa, teremos que forcar que
				// alguns dados referentes a disciplina, sejam, inicializados
				// com os dados da filha da composicao, que é o que estamos fazendo aqui. Logo,
				// a mesma tem que fazer referencia a grupo de optativa, mas
				// como um disciplina que faz parte da composicao.
				novaDisciplinasAproveitadaFazParteComposicao
						.setDisciplina(gradeDisciplinaFazParteCompostaVO.getDisciplina());
				novaDisciplinasAproveitadaFazParteComposicao
						.setCargaHoraria(gradeDisciplinaFazParteCompostaVO.getCargaHoraria());
				novaDisciplinasAproveitadaFazParteComposicao
						.setQtdeCreditoConcedido(gradeDisciplinaFazParteCompostaVO.getNrCreditos());
				novaDisciplinasAproveitadaFazParteComposicao
						.setQtdeCargaHorariaConcedido(gradeDisciplinaFazParteCompostaVO.getCargaHoraria());
				novaDisciplinasAproveitadaFazParteComposicao
						.setCargaHorariaCursada(gradeDisciplinaFazParteCompostaVO.getCargaHoraria());
				novaDisciplinasAproveitadaFazParteComposicao
						.setNomeDisciplinaCursada(gradeDisciplinaFazParteCompostaVO.getDisciplina().getNome());
			}

			novoHistoricoSimuladoDisciplinaFazParteComposicao.setConfiguracaoAcademico(
					novaDisciplinasAproveitadaFazParteComposicao.getConfiguracaoAcademicoVO());
			novaDisciplinasAproveitadaFazParteComposicao
					.setHistoricoAtual(novoHistoricoSimuladoDisciplinaFazParteComposicao);
			gradeDisciplinaFazParteCompostaVO.setHistoricoAtualAluno(novoHistoricoSimuladoDisciplinaFazParteComposicao);

			disciplinasAproveitadaReferenteMaeComposicao.getDisciplinasAproveitadasFazemParteComposicao()
					.add(novaDisciplinasAproveitadaFazParteComposicao);
		}
	}

	private void gerarAproveitamentoGradeDisciplinaCompostaCorrespodente(
			AproveitamentoDisciplinasEntreMatriculasVO aproveitamentoDisciplinasEntreMatriculasVO,
			GradeDisciplinaVO gradeDisciplinaCompostaCorrespondente, UsuarioVO usuario) throws Exception {
		DisciplinasAproveitadasVO novaDisciplinasAproveitadaVO = new DisciplinasAproveitadasVO();
		novaDisciplinasAproveitadaVO.setDisciplinaComposta(Boolean.TRUE);
		novaDisciplinasAproveitadaVO.setGradeDisciplinaVO(gradeDisciplinaCompostaCorrespondente);

		// montando os dados do historico disciplina mae composicao, pois serao
		// importantes para deduzir a se o mesmo está aprovado ou nao
		// pelas disciplinas que fazem parte da composicao.
		gradeDisciplinaCompostaCorrespondente.getHistoricoAtualAluno()
				.setGradeDisciplinaVO(gradeDisciplinaCompostaCorrespondente);
		gradeDisciplinaCompostaCorrespondente.getHistoricoAtualAluno()
				.setDisciplina(gradeDisciplinaCompostaCorrespondente.getDisciplina());
		gradeDisciplinaCompostaCorrespondente.getHistoricoAtualAluno()
				.setCargaHorariaDisciplina(gradeDisciplinaCompostaCorrespondente.getCargaHoraria());
		gradeDisciplinaCompostaCorrespondente.getHistoricoAtualAluno()
				.setMatrizCurricular(aproveitamentoDisciplinasEntreMatriculasVO.getMatriculaDestinoAproveitamentoVO()
						.getGradeCurricularAtual());
		gradeDisciplinaCompostaCorrespondente.getHistoricoAtualAluno()
				.setAnoHistorico(aproveitamentoDisciplinasEntreMatriculasVO.getAnoPadrao());
		gradeDisciplinaCompostaCorrespondente.getHistoricoAtualAluno()
				.setSemestreHistorico(aproveitamentoDisciplinasEntreMatriculasVO.getSemestrePadrao());
		gradeDisciplinaCompostaCorrespondente.getHistoricoAtualAluno().setHistoricoDisciplinaAproveitada(Boolean.TRUE);

		// Ja teremos que carregar a configuracao academica aqui, pois a mesma sera
		// importante para determinar a media final
		// da disciplna mae nos metodos abaixos
		Integer codigoCfgAcademicaEspecifica = gradeDisciplinaCompostaCorrespondente.getConfiguracaoAcademico()
				.getCodigo();
		ConfiguracaoAcademicoVO configuracaoAcademicoDisciplinaDestinoAproveitar = aproveitamentoDisciplinasEntreMatriculasVO
				.getConfiguracaoAcademicoCursoDestino();
		if (!codigoCfgAcademicaEspecifica.equals(0)) {
			ConfiguracaoAcademicoVO cfgEspecifica = getFacadeFactory().getConfiguracaoAcademicoFacade()
					.consultarPorChavePrimaria(codigoCfgAcademicaEspecifica, usuario);
			configuracaoAcademicoDisciplinaDestinoAproveitar = cfgEspecifica;
		}
		gradeDisciplinaCompostaCorrespondente.getHistoricoAtualAluno()
				.setConfiguracaoAcademico(configuracaoAcademicoDisciplinaDestinoAproveitar);

		// Antes de gerar o aproveitamento para a disciplina mae (composta), precisamos
		// gerar os aproveitamentos (com seus respectivos
		// historicos) para as disciplinas que fazem parte da composicao. Isto é
		// importante, pois os dados mais importantes do historico
		// e aproveitamento da disciplina mae serao obtidos a partir dos dados das
		// filhas.
		gerarAproveitamentoDisciplinaFazParteComposicaoDeGradeDisciplinaComposta(
				aproveitamentoDisciplinasEntreMatriculasVO, novaDisciplinasAproveitadaVO,
				gradeDisciplinaCompostaCorrespondente.getGradeDisciplinaCompostaVOs(),
				gradeDisciplinaCompostaCorrespondente, null, usuario);

		List<HistoricoVO> historicoDisciplinasFazemParteAproveitamentoVOs = new ArrayList<HistoricoVO>(0);
		for (DisciplinasAproveitadasVO disciplinaAproveitadaParteComposicao : novaDisciplinasAproveitadaVO
				.getDisciplinasAproveitadasFazemParteComposicao()) {
			historicoDisciplinasFazemParteAproveitamentoVOs
					.add(disciplinaAproveitadaParteComposicao.getHistoricoAtual());
		}

		// inferir estes dados com base na rotina do historico que calcula a media e
		// frequencia da disciplina composta
		// com base nos historicos das disciplinas que fazem parte da composicao
		getFacadeFactory().getHistoricoFacade().executarSimulacaoAtualizacaoDisciplinaComposta(
				gradeDisciplinaCompostaCorrespondente.getHistoricoAtualAluno(),
				historicoDisciplinasFazemParteAproveitamentoVOs, true, usuario);

		// a rotina acima ira definir o historico da disciplina mae como aprovado,
		// conteudo, precisamos garantir que o mesmo
		// fique com a situacao Aprovado por Aproveitamento
		if (!gradeDisciplinaCompostaCorrespondente.getHistoricoAtualAluno().getAprovado()) {
			// neste caso, pela configuracaoAcademica da disciplina composta e as notas que
			// a mesma possui,
			// nao se chegou a uma disciplina mae aprovada. Logo, este aproveitamento desta
			// disciplina composta
			// nao pode ser realizado. Assim, o mesmo é abortado aqui.
			return;
		} else {
			// caso o historico esteja aprovado, temos que volta-lo para a situacao cursando
			// em funcao da usabilidade da tela de
			// aproveitamento. A tela só permite ver/editar um aproveitamento de disciplinas
			// que estao diferente de aprovadas.
			gradeDisciplinaCompostaCorrespondente.getHistoricoAtualAluno()
					.setSituacao(SituacaoHistorico.NAO_CURSADA.getValor());
		}

		// inicializando os dados do aproveitamento com base no historicoOrigemAluno
		inicializarDadosBasicosNovoAproveitamento(novaDisciplinasAproveitadaVO, gradeDisciplinaCompostaCorrespondente,
				null, aproveitamentoDisciplinasEntreMatriculasVO,
				gradeDisciplinaCompostaCorrespondente.getHistoricoAtualAluno(),
				gradeDisciplinaCompostaCorrespondente.getPeriodoLetivoVO(), usuario);

		StringBuilder obs = new StringBuilder("");
//		obs.append("Aproveitamento por Meio dos Histórios Aproveitados das Disciplinas que Fazem Parte da Composição. "
//				+ UteisTexto.ENTER);
		int nrDisciplina = 1;
		String separador = "";
		novaDisciplinasAproveitadaVO.setNomeDisciplinaCursada("");
//		for (HistoricoVO historicoFazParte : historicoDisciplinasFazemParteAproveitamentoVOs) {
//			obs.append(UteisTexto.ENTER + "DISCIPLINA ").append(nrDisciplina)
//					.append(" FAZ PARTE COMPOSIÇÃO APROVEITADA");
//			obs.append(UteisTexto.ENTER + " - Disciplina: ").append(historicoFazParte.getDisciplina().getCodigo())
//					.append(" (").append(historicoFazParte.getDisciplina().getNome()).append(") ");
//			obs.append(UteisTexto.ENTER + " - Carga Horária: ").append(historicoFazParte.getCargaHorariaDisciplina());
//			obs.append(UteisTexto.ENTER + " - Tipo Disciplina: ")
//					.append(historicoFazParte.getDescricaoTipoDisciplinaHistorico());
//			obs.append(UteisTexto.ENTER + " - Média: ").append(historicoFazParte.getMediaFinal_Apresentar());
//			obs.append(UteisTexto.ENTER + " - Frequência: ").append(historicoFazParte.getFrequencia_Apresentar());
//			obs.append(UteisTexto.ENTER + " - Situação: ").append(historicoFazParte.getSituacao_Apresentar());
//			if (historicoFazParte.getSituacao().equals(SituacaoHistorico.APROVADO_POR_EQUIVALENCIA.getValor())) {
//				obs.append(UteisTexto.ENTER + " - Derivado Por Meio Mapa Equivalência. ");
//			}
//			obs.append(UteisTexto.ENTER + "  ");
//			nrDisciplina = nrDisciplina + 1;
//			novaDisciplinasAproveitadaVO
//					.setNomeDisciplinaCursada(novaDisciplinasAproveitadaVO.getNomeDisciplinaCursada() + separador
//							+ historicoFazParte.getDisciplina().getNome());
//			separador = "; ";
//		}
		novaDisciplinasAproveitadaVO.setObservacaoAproveitamentoEntreMatriculas(obs.toString());

		gradeDisciplinaCompostaCorrespondente.setDisciplinasAproveitadasVO(novaDisciplinasAproveitadaVO);
		aproveitamentoDisciplinasEntreMatriculasVO.getAproveitamentoDisciplinaVO()
				.adicionarObjDisciplinasAproveitadasVOs(novaDisciplinasAproveitadaVO);

		// atualizando lista de historicos aproveitados e nao aproveitados
		for (GradeDisciplinaCompostaVO gradeDisciplinaFazParteCompostaVO : gradeDisciplinaCompostaCorrespondente
				.getGradeDisciplinaCompostaVOs()) {
			HistoricoVO historicoBaseGerarAproveitamentoFazParteComposicao = gradeDisciplinaFazParteCompostaVO
					.getHistoricoAtualAluno();
			// adicionando o histórico para lista de históricos que foram aproveitados
			aproveitamentoDisciplinasEntreMatriculasVO.getHistoricoAproveitados()
					.add(historicoBaseGerarAproveitamentoFazParteComposicao);

			// removendo o histórico aproveitado da lista de nao aproveitados
			int i = aproveitamentoDisciplinasEntreMatriculasVO.getHistoricoNaoAproveitados().size() - 1;
			while (i >= 0) {
				HistoricoVO histRemover = (HistoricoVO) aproveitamentoDisciplinasEntreMatriculasVO
						.getHistoricoNaoAproveitados().get(i);
				if ((histRemover.getDisciplina().getCodigo()
						.equals(historicoBaseGerarAproveitamentoFazParteComposicao.getDisciplina().getCodigo()))
						&& (histRemover.getCargaHorariaDisciplina().equals(
								historicoBaseGerarAproveitamentoFazParteComposicao.getCargaHorariaDisciplina()))) {
					aproveitamentoDisciplinasEntreMatriculasVO.getHistoricoNaoAproveitados().remove(i);
					break;
				}
				i = i - 1;
			}
		}
	}

	/**
	 * Método responsavel por obter um mapa de equivalencia que possa atender uma
	 * determinada disciplina que faz parte de uma composicao. Este método porem,
	 * irá buscar por um mapa que atenda as seguintes características: 1) O mapa tem
	 * que ser resolvido por completo. Ou seja, se para pagar o mapa o aluno tem que
	 * estudar 3 disciplinas. Entao deverá ter históricos para pagar por estas 3
	 * disciplinas. Ou seja, nao serao aceitos aqui mapas parcialmente resolvidos.
	 * 2) A mapa deverá ser totalmente voltado para pagar disciplinas da composicao
	 * em debate. Ou seja, o mapa poderá estar pagando mais de uma disciplina.
	 * Contudo, todas deverão ser filhas da mesma disciplina mãe (ou seja, devem
	 * fazer parte da mesma composicao). 3) Caso haja mais de uma mapa a rotina irá
	 * pegar o primeiro que for totalmente aplicável.
	 * 
	 * @author Otimize - 23 de ago de 2016
	 * @param aproveitamentoDisciplinasEntreMatriculasVO
	 * @param gradeDisciplinaCompostaVO
	 * @param usuario
	 * @return
	 * @throws Exception
	 */
	private MapaEquivalenciaDisciplinaVO obterMapaEquivalenciaAplicavelDisciplinaFazParteComposicao(
			AproveitamentoDisciplinasEntreMatriculasVO aproveitamentoDisciplinasEntreMatriculasVO,
			GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO,
			List<GradeDisciplinaCompostaVO> gradeDisciplinaCompostaVOs, UsuarioVO usuario) throws Exception {
		MatriculaVO matriculaDestino = aproveitamentoDisciplinasEntreMatriculasVO.getMatriculaDestinoAproveitamentoVO();

		List<MapaEquivalenciaDisciplinaVO> listaMapasValidos = getFacadeFactory().getMapaEquivalenciaDisciplinaFacade()
				.consultarPorMapaEquivalenciaMatrizCurricularDisciplinaMatriz(
						matriculaDestino.getGradeCurricularAtual().getCodigo(),
						gradeDisciplinaCompostaVO.getDisciplina().getCodigo(),
						aproveitamentoDisciplinasEntreMatriculasVO.getMapaEquivalenciaUtilizadoAproveitamento()
								.getCodigo(),
						NivelMontarDados.TODOS);
		if (listaMapasValidos.isEmpty()) {
			return null;
		}

		for (MapaEquivalenciaDisciplinaVO mapaDisponivel : listaMapasValidos) {

			// primerio vamos avaliar se todas as disciplinas a serem estudas pelo aluno
			// podem ser complementamente
			// atendidas pelo historicos disponiveis para aproveitamento ainda
			boolean todasDisciplinaACursarSaoAtendidas = true;
			for (MapaEquivalenciaDisciplinaCursadaVO mapaEquivalenciaDisciplinaCursadaVO : mapaDisponivel
					.getMapaEquivalenciaDisciplinaCursadaVOs()) {
				boolean achouHistoricoParaDisciplinaACursarMapa = false;
				for (HistoricoVO historicoNaoAproveitado : aproveitamentoDisciplinasEntreMatriculasVO
						.getHistoricoNaoAproveitados()) {
					if ((historicoNaoAproveitado.getDisciplina().getCodigo()
							.equals(mapaEquivalenciaDisciplinaCursadaVO.getDisciplinaVO().getCodigo()))
							&& (historicoNaoAproveitado.getCargaHorariaDisciplina()
									.equals(mapaEquivalenciaDisciplinaCursadaVO.getCargaHoraria()))) {
						achouHistoricoParaDisciplinaACursarMapa = true;
						// se entrarmos aqui é por que temos um historico compativel para ser utilizado
						// no mapa. Contudo, teremos
						// que verificar se o mesmo já nao foi utilizado em outro mapa que foi
						// processado antes por esta mesma rotina.
						for (HistoricoVO historicoJaAlocadoOutroMapa : aproveitamentoDisciplinasEntreMatriculasVO
								.getHistoricoAlocadosMapaEquivalenciaAproveitados()) {
							if (historicoJaAlocadoOutroMapa.getCodigo().equals(historicoNaoAproveitado.getCodigo())) {
								achouHistoricoParaDisciplinaACursarMapa = false;
								break;
							}
						}
						// vamos armazenar nesta variavel transient o historico base para criacao e
						// implementacao da
						// equivalencia. Assim, caso o mapa seja de fato aproveitado, ja teremos o
						// historico base do mesmo
						// setado para processamento.
						mapaEquivalenciaDisciplinaCursadaVO.setHistorico(historicoNaoAproveitado);
					}
				}
				if (!achouHistoricoParaDisciplinaACursarMapa) {
					todasDisciplinaACursarSaoAtendidas = false;
					break;
				}
			}

			if (todasDisciplinaACursarSaoAtendidas) {
				// segundo vamos avaliar se todas as disciplinas da matriz que serao pagas pelo
				// mapa de equivalencia estao
				// dentro da composicao. Pois a equivalencia deverá estar satisfeita por
				// completo e também deverá estar limitada
				// as disciplinas filhas da composição.
				boolean encontrouTodasDisciplinasMapaNaComposicao = true;
				for (MapaEquivalenciaDisciplinaMatrizCurricularVO mapaEquivalenciaDisciplinaMatrizCurricularVO : mapaDisponivel
						.getMapaEquivalenciaDisciplinaMatrizCurricularVOs()) {
					boolean encontrouDisciplanaMapaListaDisciplinasFazParteComposicao = false;
					for (GradeDisciplinaCompostaVO gradeDisciplinaCompostaVOValidar : gradeDisciplinaCompostaVOs) {
						if ((mapaEquivalenciaDisciplinaMatrizCurricularVO.getDisciplinaVO().getCodigo()
								.equals(gradeDisciplinaCompostaVOValidar.getDisciplina().getCodigo()))
								&& (mapaEquivalenciaDisciplinaMatrizCurricularVO.getCargaHoraria()
										.equals(gradeDisciplinaCompostaVOValidar.getCargaHoraria()))) {
							// Além de encontrar na lista de disciplinas que fazem parte da composicao,
							// temos que ter
							// certeza que de que esta disciplina que será paga pelo mapa já nao está como
							// aprovada
							// Caso esteja aprovada, entao o mapa nao pode ser aplicado
							if (!gradeDisciplinaCompostaVOValidar.getHistoricoAtualAluno().getAprovado()) {
								encontrouDisciplanaMapaListaDisciplinasFazParteComposicao = true;
							}
						}
					}
					if (!encontrouDisciplanaMapaListaDisciplinasFazParteComposicao) {
						encontrouTodasDisciplinasMapaNaComposicao = false;
						break;
					}
				}
				if (encontrouTodasDisciplinasMapaNaComposicao) {
					return mapaDisponivel;
				}
			}
		}
		return null;
	}

	public boolean veriricarGradeDisciplinaCompostaJaResolvidaPorOutroMapaEquivalencia(
			GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO,
			AproveitamentoDisciplinasEntreMatriculasVO aproveitamentoDisciplinasEntreMatriculasVO) {
		for (MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaAplicar : aproveitamentoDisciplinasEntreMatriculasVO
				.getMapasEquivalenciaAplicarComposicao()) {
			for (MapaEquivalenciaDisciplinaMatrizCurricularVO mapaEquivalenciaDisciplinaMatrizCurricularVO : mapaEquivalenciaDisciplinaAplicar
					.getMapaEquivalenciaDisciplinaMatrizCurricularVOs()) {
				if ((gradeDisciplinaCompostaVO.getDisciplina().getCodigo()
						.equals(mapaEquivalenciaDisciplinaMatrizCurricularVO.getDisciplinaVO().getCodigo()))
						&& (gradeDisciplinaCompostaVO.getCargaHoraria()
								.equals(mapaEquivalenciaDisciplinaMatrizCurricularVO.getCargaHoraria()))) {
					// se encontrou disciplina em outro mapa, nao temos que processá-la, pois a
					// mesma será resolvida / enderecada
					// por este outro mapa
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Verificando se alguma disciplina que faz parte da composicao pode ser
	 * aproveitada, por meio, do mapa de equivalencia.
	 * 
	 * @author Otimize - 23 de ago de 2016
	 * @return
	 */
	private int verificarObterHistoricoFazParteComposicaoCumpridoPorMeioMapaEquivalencia(
			AproveitamentoDisciplinasEntreMatriculasVO aproveitamentoDisciplinasEntreMatriculasVO,
			List<GradeDisciplinaCompostaVO> gradeDisciplinaCompostaVOs, UsuarioVO usuario) throws Exception {
		if (aproveitamentoDisciplinasEntreMatriculasVO.getMapaEquivalenciaUtilizadoAproveitamento().getCodigo()
				.equals(0)) {
			// se nenhum mapa de equivalencia foi fornecido pelo usuário, então é por que
			// esta
			// migração, deverá migrar somente históricos por correspodencia de código/carga
			// horária.
			return 0;
		}

		for (GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO : gradeDisciplinaCompostaVOs) {
			// se o historico atual ja esta montado com um historico de aprovado (vindo de
			// uma aproveitamento)
			// entao nao temos que tratar mais esta disciplina da composicao, contudo, caso
			// a mesma nao esteja
			// aprovada é porque a mesma está pendente de solução.
			if ((!gradeDisciplinaCompostaVO.getHistoricoAtualAluno().getAprovado())
					&& (!veriricarGradeDisciplinaCompostaJaResolvidaPorOutroMapaEquivalencia(gradeDisciplinaCompostaVO,
							aproveitamentoDisciplinasEntreMatriculasVO))) {
				// Se o histórico nao está cumprido, vamos atraz de uma mapa de equivalencia que
				// possa resolve-lo

				MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaVO = obterMapaEquivalenciaAplicavelDisciplinaFazParteComposicao(
						aproveitamentoDisciplinasEntreMatriculasVO, gradeDisciplinaCompostaVO,
						gradeDisciplinaCompostaVOs, usuario);

				if (mapaEquivalenciaDisciplinaVO == null) {
					return 0;
				}

				// Se chegarmos aqui é por que achamos um mapa que valido para aplicacao na
				// composicao. Contudo, o mesmo será
				// armazenado em uma lista para processamento posterior. Isto por que o mapa de
				// fato só será aplicado caso a
				// composicao esteja sendo resolvida por definitivo. Ou seja, se encontramos um
				// mapa, mas somente parte da composicao
				// foi atendida com ele, entao o mesmo terá que ser descartado.
				aproveitamentoDisciplinasEntreMatriculasVO.getMapasEquivalenciaAplicarComposicao()
						.add(mapaEquivalenciaDisciplinaVO);

				// Outro controle que temos que fazer aqui é registrar os historicos que estao
				// envolvidos no mapa adicionado acima,
				// em uma lista de controle que irá evitar que este mesmo historico possa ser
				// utilizado em outro mapa que será avaliado
				// posteriormente por esta rotina. Ou seja, se o historico foi utilizado no mapa
				// acima, vamos adicioná-lo para a lista
				// historicoAlocadosMapaEquivalenciaAproveitados de forma que o mesmo nao seja
				// considerado como valido para outro mapa
				// que virá a ser processado.
				for (MapaEquivalenciaDisciplinaCursadaVO mapaEquivalenciaDisciplinaCursadaVO : mapaEquivalenciaDisciplinaVO
						.getMapaEquivalenciaDisciplinaCursadaVOs()) {
					aproveitamentoDisciplinasEntreMatriculasVO.getHistoricoAlocadosMapaEquivalenciaAproveitados()
							.add(mapaEquivalenciaDisciplinaCursadaVO.getHistorico());
				}

				return mapaEquivalenciaDisciplinaVO.getMapaEquivalenciaDisciplinaMatrizCurricularVOs().size();
			}
		}
		return 0;
	}

	private void gerarHistoricosCorrespondentesAResolucaoMapaEquivalenciaParaSeremUtilizadosNaComposicao(
			AproveitamentoDisciplinasEntreMatriculasVO aproveitamentoDisciplinasEntreMatriculasVO,
			GradeDisciplinaVO gradeDisciplinaMaeComposicaoCorrespondente, UsuarioVO usuario) throws Exception {
		// vamor percorrer os mapas de equivalencia validos para a composicao para
		// processar o aproveitamento de cada um deles
		for (MapaEquivalenciaDisciplinaVO mapaEquivalenciaComposicao : aproveitamentoDisciplinasEntreMatriculasVO
				.getMapasEquivalenciaAplicarComposicao()) {

			// para cada mapa vamos gerar o historico base da discplina da matriz que será
			// utilizado para a criacao do aproveitamento.
			for (MapaEquivalenciaDisciplinaMatrizCurricularVO mapaEquivalenciaDisciplinaMatriz : mapaEquivalenciaComposicao
					.getMapaEquivalenciaDisciplinaMatrizCurricularVOs()) {

				GradeDisciplinaCompostaVO gradeDisciplinaCompostaCorrespondeteMapa = null;
				for (GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO : gradeDisciplinaMaeComposicaoCorrespondente
						.getGradeDisciplinaCompostaVOs()) {
					if ((gradeDisciplinaCompostaVO.getDisciplina().getCodigo()
							.equals(mapaEquivalenciaDisciplinaMatriz.getDisciplinaVO().getCodigo()))
							&& (gradeDisciplinaCompostaVO.getCargaHoraria()
									.equals(mapaEquivalenciaDisciplinaMatriz.getCargaHoraria()))) {
						gradeDisciplinaCompostaCorrespondeteMapa = gradeDisciplinaCompostaVO;
					}
				}

				if (gradeDisciplinaCompostaCorrespondeteMapa == null) {
					throw new Exception(
							"Ocorreu um erro inesperado! Ao tentar realizar o aproveitamento de uma disciplina filha da composicação, por meio de uma mapa de equivalência.");
				}

				HistoricoVO novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz = new HistoricoVO();
				novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz
						.setHistoricoDisciplinaFazParteComposicao(Boolean.TRUE);
				novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz
						.setGradeDisciplinaVO(gradeDisciplinaCompostaCorrespondeteMapa);
				novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz
						.setGradeDisciplinaComposta(gradeDisciplinaCompostaCorrespondeteMapa);
				novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz
						.setDisciplina(gradeDisciplinaCompostaCorrespondeteMapa.getDisciplina());
				novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz
						.setCargaHorariaDisciplina(gradeDisciplinaCompostaCorrespondeteMapa.getCargaHoraria());
				novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz
						.setCreditoDisciplina(gradeDisciplinaCompostaCorrespondeteMapa.getNrCreditos());
				novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz
						.setMatrizCurricular(aproveitamentoDisciplinasEntreMatriculasVO
								.getMatriculaDestinoAproveitamentoVO().getGradeCurricularAtual());
				novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz
						.setAnoHistorico(aproveitamentoDisciplinasEntreMatriculasVO.getAnoPadrao());
				novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz
						.setSemestreHistorico(aproveitamentoDisciplinasEntreMatriculasVO.getSemestrePadrao());
				novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz.setHistoricoDisciplinaAproveitada(Boolean.TRUE);

				// Ja teremos que carregar a configuracao academica aqui, pois a mesma sera
				// importante para determinar a media final
				// da disciplna mae nos metodos abaixos
				Integer codigoCfgAcademicaEspecifica = gradeDisciplinaCompostaCorrespondeteMapa
						.getConfiguracaoAcademico().getCodigo();
				ConfiguracaoAcademicoVO configuracaoAcademicoDisciplinaDestinoAproveitar = aproveitamentoDisciplinasEntreMatriculasVO
						.getConfiguracaoAcademicoCursoDestino();
				if (!codigoCfgAcademicaEspecifica.equals(0)) {
					ConfiguracaoAcademicoVO cfgEspecifica = getFacadeFactory().getConfiguracaoAcademicoFacade()
							.consultarPorChavePrimaria(codigoCfgAcademicaEspecifica, usuario);
					configuracaoAcademicoDisciplinaDestinoAproveitar = cfgEspecifica;
				}
				novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz
						.setConfiguracaoAcademico(configuracaoAcademicoDisciplinaDestinoAproveitar);

				novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz.setMediaFinal(mapaEquivalenciaComposicao
						.getMediaFinalMapaEquivalenciaCumpridoPeloAluno(mapaEquivalenciaDisciplinaMatriz));
				novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz.setFreguencia(mapaEquivalenciaComposicao
						.getFrequenciaMapaEquivalenciaCumpridoPeloAluno(mapaEquivalenciaDisciplinaMatriz));

				novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz
						.setSituacao(SituacaoHistorico.APROVADO_POR_EQUIVALENCIA.getValor());
				novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz
						.setMapaEquivalenciaDisciplinaMatrizCurricular(mapaEquivalenciaDisciplinaMatriz);
				novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz
						.setMapaEquivalenciaDisciplina(mapaEquivalenciaComposicao);

				gradeDisciplinaCompostaCorrespondeteMapa
						.setHistoricoAtualAluno(novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz);
			}
		}

		// como os históricos foram aproveitados no sentido de cumprir a disciplina
		// composta (mesmo que por meio de um mapa de equivalencia)
		// agora temos que removes-los da lista de historicosNaoAproveitados. E, ainda,
		// jogá-los para a lista de historicos aproveitados.

		// removendo histório da lista de nao aproveitados - passo importante para o
		// processamento das proximas disciplinas
		int i = aproveitamentoDisciplinasEntreMatriculasVO.getHistoricoNaoAproveitados().size() - 1;
		while (i >= 0) {
			HistoricoVO historico = (HistoricoVO) aproveitamentoDisciplinasEntreMatriculasVO
					.getHistoricoNaoAproveitados().get(i);

			// vamor percorrer os mapas de equivalencia validos para a composicao para
			// remover os itens aproveitados
			for (MapaEquivalenciaDisciplinaVO mapaEquivalenciaComposicao : aproveitamentoDisciplinasEntreMatriculasVO
					.getMapasEquivalenciaAplicarComposicao()) {
				// para cada mapa vamos gerar o historico base da discplina da matriz que será
				// utilizado para a criacao do aproveitamento.
				for (MapaEquivalenciaDisciplinaCursadaVO mapaEquivalenciaDisciplinaCursadaVO : mapaEquivalenciaComposicao
						.getMapaEquivalenciaDisciplinaCursadaVOs()) {
					if ((mapaEquivalenciaDisciplinaCursadaVO.getHistorico().getDisciplina().getCodigo()
							.equals(historico.getDisciplina().getCodigo())
							&& (mapaEquivalenciaDisciplinaCursadaVO.getHistorico().getCargaHorariaDisciplina()
									.equals(historico.getCargaHorariaDisciplina())))) {
						// se entrar aqui é por que achamos um historico que foi aproveitado no mapa,
						// logo o mesmo será removido da lista de nao processados.
						aproveitamentoDisciplinasEntreMatriculasVO.getHistoricoNaoAproveitados().remove(i);
					}
				}
			}

			i = i - 1;
		}

	}

	private void gerarHistoricosCorrespondentesAResolucaoMapaEquivalenciaParaSeremUtilizadosNaComposicaoGrupoOptativa(
			AproveitamentoDisciplinasEntreMatriculasVO aproveitamentoDisciplinasEntreMatriculasVO,
			GradeCurricularGrupoOptativaDisciplinaVO gradeGrupoOptativaDisciplinaMaeComposicaoCorrespondente,
			UsuarioVO usuario) throws Exception {
		// vamor percorrer os mapas de equivalencia validos para a composicao para
		// processar o aproveitamento de cada um deles
		for (MapaEquivalenciaDisciplinaVO mapaEquivalenciaComposicao : aproveitamentoDisciplinasEntreMatriculasVO
				.getMapasEquivalenciaAplicarComposicao()) {

			// para cada mapa vamos gerar o historico base da discplina da matriz que será
			// utilizado para a criacao do aproveitamento.
			for (MapaEquivalenciaDisciplinaMatrizCurricularVO mapaEquivalenciaDisciplinaMatriz : mapaEquivalenciaComposicao
					.getMapaEquivalenciaDisciplinaMatrizCurricularVOs()) {

				GradeDisciplinaCompostaVO gradeDisciplinaCompostaCorrespondeteMapa = null;
				for (GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO : gradeGrupoOptativaDisciplinaMaeComposicaoCorrespondente
						.getGradeDisciplinaCompostaVOs()) {
					if ((gradeDisciplinaCompostaVO.getDisciplina().getCodigo()
							.equals(mapaEquivalenciaDisciplinaMatriz.getDisciplinaVO().getCodigo()))
							&& (gradeDisciplinaCompostaVO.getCargaHoraria()
									.equals(mapaEquivalenciaDisciplinaMatriz.getCargaHoraria()))) {
						gradeDisciplinaCompostaCorrespondeteMapa = gradeDisciplinaCompostaVO;
					}
				}

				if (gradeDisciplinaCompostaCorrespondeteMapa == null) {
					throw new Exception(
							"Ocorreu um erro inesperado! Ao tentar realizar o aproveitamento de uma disciplina filha da composicação, por meio de uma mapa de equivalência.");
				}

				HistoricoVO novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz = new HistoricoVO();
				novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz
						.setHistoricoDisciplinaFazParteComposicao(Boolean.TRUE);
				novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz
						.setGradeDisciplinaVO(gradeDisciplinaCompostaCorrespondeteMapa);
				novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz
						.setGradeDisciplinaComposta(gradeDisciplinaCompostaCorrespondeteMapa);
				novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz
						.setDisciplina(gradeDisciplinaCompostaCorrespondeteMapa.getDisciplina());
				novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz
						.setCargaHorariaDisciplina(gradeDisciplinaCompostaCorrespondeteMapa.getCargaHoraria());
				novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz
						.setCreditoDisciplina(gradeDisciplinaCompostaCorrespondeteMapa.getNrCreditos());
				novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz
						.setMatrizCurricular(aproveitamentoDisciplinasEntreMatriculasVO
								.getMatriculaDestinoAproveitamentoVO().getGradeCurricularAtual());
				novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz
						.setAnoHistorico(aproveitamentoDisciplinasEntreMatriculasVO.getAnoPadrao());
				novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz
						.setSemestreHistorico(aproveitamentoDisciplinasEntreMatriculasVO.getSemestrePadrao());
				novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz.setHistoricoDisciplinaAproveitada(Boolean.TRUE);

				// Ja teremos que carregar a configuracao academica aqui, pois a mesma sera
				// importante para determinar a media final
				// da disciplna mae nos metodos abaixos
				Integer codigoCfgAcademicaEspecifica = gradeDisciplinaCompostaCorrespondeteMapa
						.getConfiguracaoAcademico().getCodigo();
				ConfiguracaoAcademicoVO configuracaoAcademicoDisciplinaDestinoAproveitar = aproveitamentoDisciplinasEntreMatriculasVO
						.getConfiguracaoAcademicoCursoDestino();
				if (!codigoCfgAcademicaEspecifica.equals(0)) {
					ConfiguracaoAcademicoVO cfgEspecifica = getFacadeFactory().getConfiguracaoAcademicoFacade()
							.consultarPorChavePrimaria(codigoCfgAcademicaEspecifica, usuario);
					configuracaoAcademicoDisciplinaDestinoAproveitar = cfgEspecifica;
				}
				novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz
						.setConfiguracaoAcademico(configuracaoAcademicoDisciplinaDestinoAproveitar);

				novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz.setMediaFinal(mapaEquivalenciaComposicao
						.getMediaFinalMapaEquivalenciaCumpridoPeloAluno(mapaEquivalenciaDisciplinaMatriz));
				novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz.setFreguencia(mapaEquivalenciaComposicao
						.getFrequenciaMapaEquivalenciaCumpridoPeloAluno(mapaEquivalenciaDisciplinaMatriz));

				novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz
						.setSituacao(SituacaoHistorico.APROVADO_POR_EQUIVALENCIA.getValor());
				novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz
						.setMapaEquivalenciaDisciplinaMatrizCurricular(mapaEquivalenciaDisciplinaMatriz);
				novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz
						.setMapaEquivalenciaDisciplina(mapaEquivalenciaComposicao);

				gradeDisciplinaCompostaCorrespondeteMapa
						.setHistoricoAtualAluno(novoHistoricoDerivadoMapaEquivalenciaDisciplinaMatriz);
			}
		}

		// como os históricos foram aproveitados no sentido de cumprir a disciplina
		// composta (mesmo que por meio de um mapa de equivalencia)
		// agora temos que removes-los da lista de historicosNaoAproveitados. E, ainda,
		// jogá-los para a lista de historicos aproveitados.

		// removendo histório da lista de nao aproveitados - passo importante para o
		// processamento das proximas disciplinas
		int i = aproveitamentoDisciplinasEntreMatriculasVO.getHistoricoNaoAproveitados().size() - 1;
		while (i >= 0) {
			HistoricoVO historico = (HistoricoVO) aproveitamentoDisciplinasEntreMatriculasVO
					.getHistoricoNaoAproveitados().get(i);

			// vamor percorrer os mapas de equivalencia validos para a composicao para
			// remover os itens aproveitados
			for (MapaEquivalenciaDisciplinaVO mapaEquivalenciaComposicao : aproveitamentoDisciplinasEntreMatriculasVO
					.getMapasEquivalenciaAplicarComposicao()) {
				// para cada mapa vamos gerar o historico base da discplina da matriz que será
				// utilizado para a criacao do aproveitamento.
				for (MapaEquivalenciaDisciplinaCursadaVO mapaEquivalenciaDisciplinaCursadaVO : mapaEquivalenciaComposicao
						.getMapaEquivalenciaDisciplinaCursadaVOs()) {
					if ((mapaEquivalenciaDisciplinaCursadaVO.getHistorico().getDisciplina().getCodigo()
							.equals(historico.getDisciplina().getCodigo())
							&& (mapaEquivalenciaDisciplinaCursadaVO.getHistorico().getCargaHorariaDisciplina()
									.equals(historico.getCargaHorariaDisciplina())))) {
						// se entrar aqui é por que achamos um historico que foi aproveitado no mapa,
						// logo o mesmo será removido da lista de nao processados.
						aproveitamentoDisciplinasEntreMatriculasVO.getHistoricoNaoAproveitados().remove(i);
					}
				}
			}

			i = i - 1;
		}

	}

	private void gerarAproveitamentoGradeCurricularGrupoOptativaCompostaCorrespodente(
			AproveitamentoDisciplinasEntreMatriculasVO aproveitamentoDisciplinasEntreMatriculasVO,
			GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaCorrespondente,
			UsuarioVO usuario) throws Exception {
		DisciplinasAproveitadasVO novaDisciplinasAproveitadaVO = new DisciplinasAproveitadasVO();
		novaDisciplinasAproveitadaVO.setDisciplinaComposta(Boolean.TRUE);
		novaDisciplinasAproveitadaVO
				.setGradeCurricularGrupoOptativaDisciplina(gradeCurricularGrupoOptativaDisciplinaCorrespondente);

		// montando os dados do historico disciplina mae composicao, pois serao
		// importantes para deduzir a se o mesmo está aprovado ou nao
		// pelas disciplinas que fazem parte da composicao.
		gradeCurricularGrupoOptativaDisciplinaCorrespondente.getHistoricoAtualAluno()
				.setGradeCurricularGrupoOptativaDisciplinaVO(gradeCurricularGrupoOptativaDisciplinaCorrespondente);
		gradeCurricularGrupoOptativaDisciplinaCorrespondente.getHistoricoAtualAluno()
				.setDisciplina(gradeCurricularGrupoOptativaDisciplinaCorrespondente.getDisciplina());
		gradeCurricularGrupoOptativaDisciplinaCorrespondente.getHistoricoAtualAluno()
				.setCargaHorariaDisciplina(gradeCurricularGrupoOptativaDisciplinaCorrespondente.getCargaHoraria());
		gradeCurricularGrupoOptativaDisciplinaCorrespondente.getHistoricoAtualAluno()
				.setMatrizCurricular(aproveitamentoDisciplinasEntreMatriculasVO.getMatriculaDestinoAproveitamentoVO()
						.getGradeCurricularAtual());
		gradeCurricularGrupoOptativaDisciplinaCorrespondente.getHistoricoAtualAluno()
				.setAnoHistorico(aproveitamentoDisciplinasEntreMatriculasVO.getAnoPadrao());
		gradeCurricularGrupoOptativaDisciplinaCorrespondente.getHistoricoAtualAluno()
				.setSemestreHistorico(aproveitamentoDisciplinasEntreMatriculasVO.getSemestrePadrao());
		gradeCurricularGrupoOptativaDisciplinaCorrespondente.getHistoricoAtualAluno()
				.setHistoricoDisciplinaAproveitada(Boolean.TRUE);

		// Ja teremos que carregar a configuracao academica aqui, pois a mesma sera
		// importante para determinar a media final
		// da disciplna mae nos metodos abaixos
		Integer codigoCfgAcademicaEspecifica = gradeCurricularGrupoOptativaDisciplinaCorrespondente
				.getConfiguracaoAcademico().getCodigo();
		ConfiguracaoAcademicoVO configuracaoAcademicoDisciplinaDestinoAproveitar = aproveitamentoDisciplinasEntreMatriculasVO
				.getConfiguracaoAcademicoCursoDestino();
		if (!codigoCfgAcademicaEspecifica.equals(0)) {
			ConfiguracaoAcademicoVO cfgEspecifica = getFacadeFactory().getConfiguracaoAcademicoFacade()
					.consultarPorChavePrimaria(codigoCfgAcademicaEspecifica, usuario);
			configuracaoAcademicoDisciplinaDestinoAproveitar = cfgEspecifica;
		}
		gradeCurricularGrupoOptativaDisciplinaCorrespondente.getHistoricoAtualAluno()
				.setConfiguracaoAcademico(configuracaoAcademicoDisciplinaDestinoAproveitar);

		// Antes de gerar o aproveitamento para a disciplina mae (composta), precisamos
		// gerar os aproveitamentos (com seus respectivos
		// historicos) para as disciplinas que fazem parte da composicao. Isto é
		// importante, pois os dados mais importantes do historico
		// e aproveitamento da disciplina mae serao obtidos a partir dos dados das
		// filhas.
		gerarAproveitamentoDisciplinaFazParteComposicaoDeGradeDisciplinaComposta(
				aproveitamentoDisciplinasEntreMatriculasVO, novaDisciplinasAproveitadaVO,
				gradeCurricularGrupoOptativaDisciplinaCorrespondente.getGradeDisciplinaCompostaVOs(), null,
				gradeCurricularGrupoOptativaDisciplinaCorrespondente, usuario);

		List<HistoricoVO> historicoDisciplinasFazemParteAproveitamentoVOs = new ArrayList<HistoricoVO>(0);
		for (DisciplinasAproveitadasVO disciplinaAproveitadaParteComposicao : novaDisciplinasAproveitadaVO
				.getDisciplinasAproveitadasFazemParteComposicao()) {
			historicoDisciplinasFazemParteAproveitamentoVOs
					.add(disciplinaAproveitadaParteComposicao.getHistoricoAtual());
		}

		// inferir estes dados com base na rotina do historico que calcula a media e
		// frequencia da disciplina composta
		// com base nos historicos das disciplinas que fazem parte da composicao
		getFacadeFactory().getHistoricoFacade().executarSimulacaoAtualizacaoDisciplinaComposta(
				gradeCurricularGrupoOptativaDisciplinaCorrespondente.getHistoricoAtualAluno(),
				historicoDisciplinasFazemParteAproveitamentoVOs, true, usuario);

		// a rotina acima ira definir o historico da disciplina mae como aprovado,
		// conteudo, precisamos garantir que o mesmo
		// fique com a situacao Aprovado por Aproveitamento
		if (!gradeCurricularGrupoOptativaDisciplinaCorrespondente.getHistoricoAtualAluno().getAprovado()) {
			// neste caso, pela configuracaoAcademica da disciplina composta e as notas que
			// a mesma possui,
			// nao se chegou a uma disciplina mae aprovada. Logo, este aproveitamento desta
			// disciplina composta
			// nao pode ser realizado. Assim, o mesmo é abortado aqui.
			return;
		} else {
			// caso o historico esteja aprovado, temos que volta-lo para a situacao cursando
			// em funcao da usabilidade da tela de
			// aproveitamento. A tela só permite ver/editar um aproveitamento de disciplinas
			// que estao diferente de aprovadas.
			gradeCurricularGrupoOptativaDisciplinaCorrespondente.getHistoricoAtualAluno()
					.setSituacao(SituacaoHistorico.NAO_CURSADA.getValor());
		}

		// inicializando os dados do aproveitamento com base no historicoOrigemAluno
		inicializarDadosBasicosNovoAproveitamento(novaDisciplinasAproveitadaVO, null,
				gradeCurricularGrupoOptativaDisciplinaCorrespondente, aproveitamentoDisciplinasEntreMatriculasVO,
				gradeCurricularGrupoOptativaDisciplinaCorrespondente.getHistoricoAtualAluno(),
				gradeCurricularGrupoOptativaDisciplinaCorrespondente.getPeriodoLetivoDisciplinaReferenciada(), usuario);

		StringBuilder obs = new StringBuilder("");
//		obs.append("Aproveitamento por Meio dos Histórios Aproveitados das Disciplinas que Fazem Parte da Composição. "
//				+ UteisTexto.ENTER);
		int nrDisciplina = 1;
		String separador = "";
		novaDisciplinasAproveitadaVO.setNomeDisciplinaCursada("");
//		for (HistoricoVO historicoFazParte : historicoDisciplinasFazemParteAproveitamentoVOs) {
//			obs.append(UteisTexto.ENTER + "DISCIPLINA ").append(nrDisciplina)
//					.append(" FAZ PARTE COMPOSIÇÃO APROVEITADA");
//			obs.append(UteisTexto.ENTER + " - Disciplina: ").append(historicoFazParte.getDisciplina().getCodigo())
//					.append(" (").append(historicoFazParte.getDisciplina().getNome()).append(") ");
//			obs.append(UteisTexto.ENTER + " - Carga Horária: ").append(historicoFazParte.getCargaHorariaDisciplina());
//			obs.append(UteisTexto.ENTER + " - Tipo Disciplina: ")
//					.append(historicoFazParte.getDescricaoTipoDisciplinaHistorico());
//			obs.append(UteisTexto.ENTER + " - Média: ").append(historicoFazParte.getMediaFinal_Apresentar());
//			obs.append(UteisTexto.ENTER + " - Frequência: ").append(historicoFazParte.getFrequencia_Apresentar());
//			obs.append(UteisTexto.ENTER + " - Situação: ").append(historicoFazParte.getSituacao_Apresentar());
//			if (historicoFazParte.getSituacao().equals(SituacaoHistorico.APROVADO_POR_EQUIVALENCIA.getValor())) {
//				obs.append(UteisTexto.ENTER + " - Derivado Por Meio Mapa Equivalência. ");
//			}
//			obs.append(UteisTexto.ENTER + "  ");
//			nrDisciplina = nrDisciplina + 1;
//			novaDisciplinasAproveitadaVO
//					.setNomeDisciplinaCursada(novaDisciplinasAproveitadaVO.getNomeDisciplinaCursada() + separador
//							+ historicoFazParte.getDisciplina().getNome());
//			separador = "; ";
//		}
		novaDisciplinasAproveitadaVO.setObservacaoAproveitamentoEntreMatriculas(obs.toString());

		gradeCurricularGrupoOptativaDisciplinaCorrespondente.setDisciplinasAproveitadasVO(novaDisciplinasAproveitadaVO);
		aproveitamentoDisciplinasEntreMatriculasVO.getAproveitamentoDisciplinaVO()
				.adicionarObjDisciplinasAproveitadasVOs(novaDisciplinasAproveitadaVO);

		// atualizando lista de historicos aproveitados e nao aproveitados
		for (GradeDisciplinaCompostaVO gradeDisciplinaFazParteCompostaVO : gradeCurricularGrupoOptativaDisciplinaCorrespondente
				.getGradeDisciplinaCompostaVOs()) {
			HistoricoVO historicoBaseGerarAproveitamentoFazParteComposicao = gradeDisciplinaFazParteCompostaVO
					.getHistoricoAtualAluno();
			// adicionando o histórico para lista de históricos que foram aproveitados
			aproveitamentoDisciplinasEntreMatriculasVO.getHistoricoAproveitados()
					.add(historicoBaseGerarAproveitamentoFazParteComposicao);
			// removendo o histórico aproveitado da lista de nao aproveitados
			int i = aproveitamentoDisciplinasEntreMatriculasVO.getHistoricoNaoAproveitados().size() - 1;
			while (i >= 0) {
				HistoricoVO histRemover = (HistoricoVO) aproveitamentoDisciplinasEntreMatriculasVO
						.getHistoricoNaoAproveitados().get(i);
				if ((histRemover.getDisciplina().getCodigo()
						.equals(historicoBaseGerarAproveitamentoFazParteComposicao.getDisciplina().getCodigo()))
						&& (histRemover.getCargaHorariaDisciplina().equals(
								historicoBaseGerarAproveitamentoFazParteComposicao.getCargaHorariaDisciplina()))) {
					aproveitamentoDisciplinasEntreMatriculasVO.getHistoricoNaoAproveitados().remove(i);
					break;
				}
				i = i - 1;
			}
		}

	}

	/**
	 * Vamos verificar se existe alguma disciplina composta na grade destino, cuja
	 * composicao pode ser atendida por completo (para isto teremos que observar a
	 * regra de resolução da composição - todas as disciplinas ou somente parte
	 * delas). Para isto iremos verificar se os historicos remanescentes podem ser
	 * aproveitados nas filhas de composicao de forma a resolve-la por completo.
	 * Este método também é capaz de avaliar se discplinas filhas da composicao pode
	 * ser pagos (cumpridos) por meio de mapas de equivalencia. De qualquer forma,
	 * uma mapa de equivalencia só é considerado e ele for resolvido por completo
	 * (nao pode ficar pendente) e tambem se a composicao tambem foi resolvida por
	 * completo (nao existe o conceito de composicao resolvida parcialmente no SEI).
	 * 
	 * @author Otimize - 28 de jul de 2016
	 * @param aproveitamentoDisciplinasEntreMatriculasVO
	 * @param usuario
	 * @throws Exception
	 */
	private void verificandoEGerandoHistoricosDisciplinaCompostaNaGradeDestinoCujaComposicaoPodeSerAtendidaPorCompleto(
			AproveitamentoDisciplinasEntreMatriculasVO aproveitamentoDisciplinasEntreMatriculasVO, UsuarioVO usuario)
			throws Exception {
		MatriculaVO matriculaDestino = aproveitamentoDisciplinasEntreMatriculasVO.getMatriculaDestinoAproveitamentoVO();

		List<GradeDisciplinaVO> listaGradesDisciplinasCompostasGrade = getFacadeFactory().getGradeDisciplinaFacade()
				.consultarPorGradeDisciplinaCompostaPorGrade(matriculaDestino.getGradeCurricularAtual().getCodigo(),
						false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		for (GradeDisciplinaVO gradeDisciplinaComComposicaoVO : listaGradesDisciplinasCompostasGrade) {
			// obtendo gradeDisciplina correspondente já montada na gradeDestino
			GradeDisciplinaVO gradeDisciplinaCorrespondente = matriculaDestino.getGradeCurricularAtual()
					.obterGradeDisciplinaCorrespondente(gradeDisciplinaComComposicaoVO.getDisciplina().getCodigo(),
							gradeDisciplinaComComposicaoVO.getCargaHoraria());
			// atualizando a lista de disciplinas que fazem parte da composicao
			gradeDisciplinaCorrespondente
					.setGradeDisciplinaCompostaVOs(gradeDisciplinaComComposicaoVO.getGradeDisciplinaCompostaVOs());

			if (verificarDisciplinaGradeDestinoPodeReceberAproveitamento(aproveitamentoDisciplinasEntreMatriculasVO,
					gradeDisciplinaCorrespondente, usuario)) {
				// agora vamos percorrer as filhas e verificar se elas possuem historico que
				// podem ser aproveitados
				// para equalizar essas disciplinas filhas (ou seja, para aproveitar o historico
				// da outra matricula
				// para a filha de uma composicao na gradeDestino.
				int nrDisciplinasCumprir = gradeDisciplinaCorrespondente.getGradeDisciplinaCompostaVOs().size();
				TipoControleComposicaoEnum tipoControle = gradeDisciplinaCorrespondente.getTipoControleComposicao();
				if (tipoControle.equals(TipoControleComposicaoEnum.ESTUDAR_QUANTIDADE_MAXIMA_COMPOSTA)) {
					nrDisciplinasCumprir = gradeDisciplinaCorrespondente.getNumeroMinimoDisciplinaComposicaoEstudar();
				}

				int nrDisciplinasCumpridas = 0;
				for (GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO : gradeDisciplinaCorrespondente
						.getGradeDisciplinaCompostaVOs()) {
					HistoricoVO historicoCorrespondeteASerAproveitado = verificarObterHistoricoNaoAproveitadoParaDisciplina(
							aproveitamentoDisciplinasEntreMatriculasVO,
							gradeDisciplinaCompostaVO.getDisciplina().getCodigo(),
							gradeDisciplinaCompostaVO.getCargaHoraria());
					if (historicoCorrespondeteASerAproveitado != null) {
						nrDisciplinasCumpridas = nrDisciplinasCumpridas + 1;
						gradeDisciplinaCompostaVO.setHistoricoAtualAluno(historicoCorrespondeteASerAproveitado);
					}
				}
				if (nrDisciplinasCumpridas >= nrDisciplinasCumprir) {
					// se entrarmos aqui é por que o mapa foi cumprido por completo, logo podemos
					// gerar o aproveitamento para a disciplina
					// mae da composicao e tambem para as filhas da composicao
					try {
						gerarAproveitamentoGradeDisciplinaCompostaCorrespodente(
								aproveitamentoDisciplinasEntreMatriculasVO, gradeDisciplinaCorrespondente, usuario);
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}
				} else {
					// limpando a lista a abaixo que é utilizada para garantir que um mesmo
					// historico nao seja aproveitado
					// mais de uma vez.
					aproveitamentoDisciplinasEntreMatriculasVO.getHistoricoAlocadosMapaEquivalenciaAproveitados()
							.clear();
					aproveitamentoDisciplinasEntreMatriculasVO.getMapasEquivalenciaAplicarComposicao().clear();

					// caso a composicao nao tenha sido comprida, vamos verificar se as disciplinas
					// da composicao que nao foram
					// aproveitadas de forma direta podem ser aproveitas por meio de uma mapa de de
					// equivalencia. Aqui, temos
					// que considerar, que um mapa que resolver as disciplinas pendentes da
					// composicao podem assim contribuir para
					// fechar a composicao por completo e, por conseguinte, viabilizar o
					// aproveitamento da mesma.
					int nrNrDisciplinasPendentesPrecisaSerResolvidoPorMapa = nrDisciplinasCumprir
							- nrDisciplinasCumpridas;
					while (nrNrDisciplinasPendentesPrecisaSerResolvidoPorMapa > 0) {
						int nrDisciplinasAproveitasPorEquivalencia = verificarObterHistoricoFazParteComposicaoCumpridoPorMeioMapaEquivalencia(
								aproveitamentoDisciplinasEntreMatriculasVO,
								gradeDisciplinaCorrespondente.getGradeDisciplinaCompostaVOs(), usuario);
						if (nrDisciplinasAproveitasPorEquivalencia == 0) {
							// se nenhuma disciplina filha da composicao pode ser aproveitada por meio do
							// mapa, entao
							// podemos sair deste método, pois, nao teremos como cumprir a composicao como
							// um todo, logo
							// ela nao poderá ser aproveita (pois, composicoes so podem ser aproveitadas se
							// cumpridas por
							// completo)
							break;
						}
						nrNrDisciplinasPendentesPrecisaSerResolvidoPorMapa = nrNrDisciplinasPendentesPrecisaSerResolvidoPorMapa
								- nrDisciplinasAproveitasPorEquivalencia;
					}
					if (nrNrDisciplinasPendentesPrecisaSerResolvidoPorMapa == 0) {
						try {
							// Uma vez que a composicao foi resolvida de fato. Entao iremos agora processar
							// todos os mapas de equivalencia
							// fazendo com que os historicos resultantes da resolucao do mapa seja criados e
							// armazendos na respectiva
							// disciplina da composicao. Assim, ao chamarmos o método de criacao dos
							// históricos abaixo, o mesmo irá
							// processar normalmente. Lembrando que neste caso, o mapa de equavelencia
							// utilizado para geracao ficara
							// registrado somente no campo observacao. Pois nao iremos considerar mapa que
							// forem totalmente resolvidos.
							// Nao teremos controles neste caso para que o mapa fique parcialmente
							// resolvido.
							gerarHistoricosCorrespondentesAResolucaoMapaEquivalenciaParaSeremUtilizadosNaComposicao(
									aproveitamentoDisciplinasEntreMatriculasVO, gradeDisciplinaCorrespondente, usuario);

							// aqui vamos gerar os historicos da disciplina mae da composicao e para suas
							// filhas, com base nos historicos já criados anterioremente.
							gerarAproveitamentoGradeDisciplinaCompostaCorrespodente(
									aproveitamentoDisciplinasEntreMatriculasVO, gradeDisciplinaCorrespondente, usuario);

						} catch (Exception e) {
							System.out.println(e.getMessage());
						}
					}
				}
			}
		}

		List<GradeCurricularGrupoOptativaDisciplinaVO> listaGradesCurricularesGrupoOptativaComposta = getFacadeFactory()
				.getGradeCurricularGrupoOptativaDisciplinaFacade().consultarPorMatrizCurricularGrupoOptativaComposta(
						matriculaDestino.getGradeCurricularAtual().getCodigo(), usuario);
		for (GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplina : listaGradesCurricularesGrupoOptativaComposta) {
			// obtendo gradeGrupoOptativaDisciplina correspondente já montada na
			// gradeDestino
			GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaCorrespondente = matriculaDestino
					.getGradeCurricularAtual().obterGradeCurricularGrupoOptativaCorrespondente(
							gradeCurricularGrupoOptativaDisciplina.getDisciplina().getCodigo(),
							gradeCurricularGrupoOptativaDisciplina.getCargaHoraria());
			// atualizando a lista de disciplinas que fazem parte da composicao
			gradeCurricularGrupoOptativaDisciplinaCorrespondente.setGradeDisciplinaCompostaVOs(
					gradeCurricularGrupoOptativaDisciplina.getGradeDisciplinaCompostaVOs());

			if (verificarDisciplinaGradeGrupoOptativaDestinoPodeReceberAproveitamento(
					aproveitamentoDisciplinasEntreMatriculasVO, gradeCurricularGrupoOptativaDisciplinaCorrespondente,
					usuario)) {
				// agora vamos percorrer as filhas e verificar se elas possuem historico que
				// podem ser aproveitados
				// para equalizar essas disciplinas filhas (ou seja, para aproveitar o historico
				// da outra matricula
				// para a filha de uma composicao na gradeGrupoOptativaDestino.
				int nrDisciplinasCumprir = gradeCurricularGrupoOptativaDisciplinaCorrespondente
						.getGradeDisciplinaCompostaVOs().size();
				// TipoControleComposicaoEnum tipoControle =
				// gradeCurricularGrupoOptativaDisciplinaCorrespondente.getTipoControleComposicao();
				// if
				// (tipoControle.equals(TipoControleComposicaoEnum.ESTUDAR_QUANTIDADE_MAXIMA_COMPOSTA))
				// {
				// nrDisciplinasCumprir =
				// gradeCurricularGrupoOptativaDisciplinaCorrespondente.getNumeroMaximoDisciplinaComposicaoEstudar();
				// }

				int nrDisciplinasCumpridas = 0;
				for (GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO : gradeCurricularGrupoOptativaDisciplinaCorrespondente
						.getGradeDisciplinaCompostaVOs()) {
					HistoricoVO historicoCorrespondeteASerAproveitado = verificarObterHistoricoNaoAproveitadoParaDisciplina(
							aproveitamentoDisciplinasEntreMatriculasVO,
							gradeDisciplinaCompostaVO.getDisciplina().getCodigo(),
							gradeDisciplinaCompostaVO.getCargaHoraria());
					if (historicoCorrespondeteASerAproveitado != null) {
						nrDisciplinasCumpridas = nrDisciplinasCumpridas + 1;
						gradeDisciplinaCompostaVO.setHistoricoAtualAluno(historicoCorrespondeteASerAproveitado);
					}
				}
				if (nrDisciplinasCumpridas >= nrDisciplinasCumprir) {
					// se entrarmos aqui é por que o mapa foi cumprido por completo, logo podemos
					// gerar o aproveitamento para a disciplina
					// mae da composicao e tambem para as filhas da composicao
					try {
						gerarAproveitamentoGradeCurricularGrupoOptativaCompostaCorrespodente(
								aproveitamentoDisciplinasEntreMatriculasVO,
								gradeCurricularGrupoOptativaDisciplinaCorrespondente, usuario);
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}
				} else {
					// limpando a lista a abaixo que é utilizada para garantir que um mesmo
					// historico nao seja aproveitado
					// mais de uma vez.
					aproveitamentoDisciplinasEntreMatriculasVO.getHistoricoAlocadosMapaEquivalenciaAproveitados()
							.clear();
					aproveitamentoDisciplinasEntreMatriculasVO.getMapasEquivalenciaAplicarComposicao().clear();

					// caso a composicao nao tenha sido comprida, vamos verificar se as disciplinas
					// da composicao que nao foram
					// aproveitadas de forma direta podem ser aproveitas por meio de uma mapa de de
					// equivalencia. Aqui, temos
					// que considerar, que um mapa que resolver as disciplinas pendentes da
					// composicao podem assim contribuir para
					// fechar a composicao por completo e, por conseguinte, viabilizar o
					// aproveitamento da mesma.
					int nrNrDisciplinasPendentesPrecisaSerResolvidoPorMapa = nrDisciplinasCumprir
							- nrDisciplinasCumpridas;
					while (nrNrDisciplinasPendentesPrecisaSerResolvidoPorMapa > 0) {
						int nrDisciplinasAproveitasPorEquivalencia = verificarObterHistoricoFazParteComposicaoCumpridoPorMeioMapaEquivalencia(
								aproveitamentoDisciplinasEntreMatriculasVO,
								gradeCurricularGrupoOptativaDisciplinaCorrespondente.getGradeDisciplinaCompostaVOs(),
								usuario);
						if (nrDisciplinasAproveitasPorEquivalencia == 0) {
							// se nenhuma disciplina filha da composicao pode ser aproveitada por meio do
							// mapa, entao
							// podemos sair deste método, pois, nao teremos como cumprir a composicao como
							// um todo, logo
							// ela nao poderá ser aproveita (pois, composicoes so podem ser aproveitadas se
							// cumpridas por
							// completo)
							break;
						}
						nrNrDisciplinasPendentesPrecisaSerResolvidoPorMapa = nrNrDisciplinasPendentesPrecisaSerResolvidoPorMapa
								- nrDisciplinasAproveitasPorEquivalencia;
					}
					if (nrNrDisciplinasPendentesPrecisaSerResolvidoPorMapa == 0) {
						try {
							// Uma vez que a composicao foi resolvida de fato. Entao iremos agora processar
							// todos os mapas de equivalencia
							// fazendo com que os historicos resultantes da resolucao do mapa seja criados e
							// armazendos na respectiva
							// disciplina da composicao. Assim, ao chamarmos o método de criacao dos
							// históricos abaixo, o mesmo irá
							// processar normalmente. Lembrando que neste caso, o mapa de equavelencia
							// utilizado para geracao ficara
							// registrado somente no campo observacao. Pois nao iremos considerar mapa que
							// forem totalmente resolvidos.
							// Nao teremos controles neste caso para que o mapa fique parcialmente
							// resolvido.
							gerarHistoricosCorrespondentesAResolucaoMapaEquivalenciaParaSeremUtilizadosNaComposicaoGrupoOptativa(
									aproveitamentoDisciplinasEntreMatriculasVO,
									gradeCurricularGrupoOptativaDisciplinaCorrespondente, usuario);

							// aqui vamos gerar os historicos da disciplina mae da composicao e para suas
							// filhas, com base nos historicos já criados anterioremente.
							gerarAproveitamentoGradeCurricularGrupoOptativaCompostaCorrespodente(
									aproveitamentoDisciplinasEntreMatriculasVO,
									gradeCurricularGrupoOptativaDisciplinaCorrespondente, usuario);

						} catch (Exception e) {
							System.out.println(e.getMessage());
						}
					}
				}
			}
		}
	}

	/**
	 * 
	 * @author Otimize - 30 de jun de 2016
	 * @param aproveitamentoDisciplinasEntreMatriculasVO
	 * @param carregarDadosHistoricoMatrizCurricularMatriculaDestino
	 * @param usuario
	 * @throws Exception
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void verificarEMapearDisciplinasPodemSerAproveitadasOutraMatricula(
			AproveitamentoDisciplinasEntreMatriculasVO aproveitamentoDisciplinasEntreMatriculasVO,
			Boolean carregarDadosHistoricoMatrizCurricularMatriculaDestino, UsuarioVO usuario) throws Exception {
		try {
			// curso origem será de onde iremos buscar os historicos aprovados do aluno para
			// aproveita-los
			// na grade destino. Ou seja, é o curso anterior do aluno do qual visa-se
			// aproveitar disciplinas
			carregandoDadosHistoricoMatrizCfgAcademicoCursoOrigem(aproveitamentoDisciplinasEntreMatriculasVO, usuario);

			// curso destino será para onde iremos gerar os historicos aproveitados do
			// aluno. Ou seja, é o curso atual do aluno
			// para o qual visa-se aproveitar disciplinas
			if (carregarDadosHistoricoMatrizCurricularMatriculaDestino) {
				carregandoDadosHistoricoMatrizCfgAcademicoCursoDestino(aproveitamentoDisciplinasEntreMatriculasVO,
						usuario);
			}
			aproveitamentoDisciplinasEntreMatriculasVO
					.setConfiguracaoAcademicoCursoDestino(aproveitamentoDisciplinasEntreMatriculasVO
							.getMatriculaDestinoAproveitamentoVO().getCurso().getConfiguracaoAcademico());
			// montando os dados da gradeCurricular na Destino, pois usaremos esta grade
			// para identificar disciplinas correspondentes.
			aproveitamentoDisciplinasEntreMatriculasVO.getMatriculaDestinoAproveitamentoVO()
					.setGradeCurricularAtual(aproveitamentoDisciplinasEntreMatriculasVO
							.getMatriculaDestinoAproveitamentoVO().getMatriculaComHistoricoAlunoVO()
							.getGradeCurricularComHistoricoAlunoVO().getGradeCurricularVO());
			// montando os dados do curso origem, pois iremos precisar de informacores do
			// curso como periodicidade
			aproveitamentoDisciplinasEntreMatriculasVO.getAproveitamentoDisciplinaVO().setCurso(
					aproveitamentoDisciplinasEntreMatriculasVO.getMatriculaDestinoAproveitamentoVO().getCurso());

			// carregando todos os dados do MapaEquivalencia que sera utilizado no
			// aproveitamento das disciplinas
			if (!aproveitamentoDisciplinasEntreMatriculasVO.getMapaEquivalenciaUtilizadoAproveitamento().getCodigo()
					.equals(0)) {
				MapaEquivalenciaMatrizCurricularVO mapaEquivalenciaVO = getFacadeFactory()
						.getMapaEquivalenciaMatrizCurricularFacade()
						.consultarPorChavePrimaria(aproveitamentoDisciplinasEntreMatriculasVO
								.getMapaEquivalenciaUtilizadoAproveitamento().getCodigo(), NivelMontarDados.TODOS, true,
								usuario);
				aproveitamentoDisciplinasEntreMatriculasVO
						.setMapaEquivalenciaUtilizadoAproveitamento(mapaEquivalenciaVO);
			}

			aproveitamentoDisciplinasEntreMatriculasVO.adicionarHistoricoResultadoProcessamento(
					aproveitamentoDisciplinasEntreMatriculasVO.getDataProcessamento(), usuario.getNome_Apresentar(),
					"Iniciando processo de aproveitamento de disciplinas de outro curso do aluno.");

			// Aproveitando todas as gradeDisciplinasVO´s da gradeOrigem (aprovadas) que
			// possuem correspondencia direta na gradeDestino
			verificandoEGerandoHistoricoGradeDisciplinaAprovadoGradeOrigemComCorrespondenteGradeDestino(
					aproveitamentoDisciplinasEntreMatriculasVO, usuario);

			// Migrando todas as GradeCurricularGrupoOptativaDisciplinaVO´s da gradeOrigem
			// (aprovadas) que possuem correspondencia na gradeDestino
			verificandoEGerandoHistoricoGradeCurricularGrupoOptativaDisciplinaAprovadoGradeOrigemComCorrespondenteGradeDestino(
					aproveitamentoDisciplinasEntreMatriculasVO, usuario);

			// Os métodos acima irão processar todos os históricos que na gradeOrigem
			// estavam vinculados
			// a gradeDisciplina e ou GradeCurricularGrupoOptativaDisciplina. Abaixo iremos
			// processar os históricos
			// que o aluno está aprovado mas estão fora da grade origem do mesmo. Isto para
			// verificar se estes históricos
			// que eram foram da grade origem, não podem ser aproveitados por correspodencia
			// direta na grade destino
			verificandoEGerandoHistoricosForaGradeAprovadosGradeOrigemPorCorrespodenciaDireta(
					aproveitamentoDisciplinasEntreMatriculasVO, usuario);

			// verificando se alguma das disciplinas nao aproveitadas até o presente momento
			// sao compostas.
			// Neste caso, como as diciplinas mãe da composicao nao foram aproveitadas vamos
			// verificar as filhas
			// das mesmas (disciplinas que fazem parte da composicao) para verificar a
			// possibilidade de aproveitamento das mesmas.
			// caso seja possivel a mesma será processada diretamente. Caso contrário
			// poderão ainda ser tratadas por meio de mapas
			// de equivalencias abaixo.
			verificandoEGerandoHistoricosDisciplinasFazemParteComposicaoGradeOrigemPorCorrespodenciaDireta(
					aproveitamentoDisciplinasEntreMatriculasVO, usuario);

			// vamos verificar se existe alguma disciplina composta na grade destino, cuja
			// composicao pode ser atendida por completo (para isto
			// teremos que observar a regra de resolução da composição - todas as
			// disciplinas ou somente parte delas). Para isto iremos verificar
			// se os historicos remanescentes podem ser aproveitados nas filhas de
			// composicao de forma a resolve-la por completo.
			verificandoEGerandoHistoricosDisciplinaCompostaNaGradeDestinoCujaComposicaoPodeSerAtendidaPorCompleto(
					aproveitamentoDisciplinasEntreMatriculasVO, usuario);

			// Neste ponto, temos que todas as correspondencias diretas já foram mapeadas e
			// realizadas pelos métodos acima
			// Todos os históricos que não foram aproveitados por correspondencia direta
			// serão tratados no método abaixo,
			// que buscará pelo melhor mapa de equivalencia a ser aplicado no sentido de
			// aproveitar estes tipo de histórico.
			verificandoEGerandoHistoricosNaoAproveitadosGradeDestinoPorMapaEquivalencia(
					aproveitamentoDisciplinasEntreMatriculasVO, usuario);

			// Método importante para a tela de aproveitamento de disciplinas. A mesma é
			// responsável por setor TRUE nos campos corretos
			// das disciplinas que foram gerados aproveitamentos para elas. De forma, que o
			// usuario possa ver os aproveitamentos e tb
			// edita-los posteriormente
			realizarMontagemPainelMatrizCurricularComDisciplinasAproveitadas(
					aproveitamentoDisciplinasEntreMatriculasVO.getAproveitamentoDisciplinaVO(),
					aproveitamentoDisciplinasEntreMatriculasVO, usuario);

			aproveitamentoDisciplinasEntreMatriculasVO.adicionarHistoricoResultadoProcessamento(new Date(),
					usuario.getNome_Apresentar(), "Finalizado aproveitamento de disciplinas de outro curso do aluno.",
					true);
			aproveitamentoDisciplinasEntreMatriculasVO.setSituacao("RE");
		} catch (Exception e) {
			aproveitamentoDisciplinasEntreMatriculasVO.adicionarHistoricoResultadoProcessamento(new Date(),
					usuario.getNome_Apresentar(), "ERRO: " + e.getMessage());
			aproveitamentoDisciplinasEntreMatriculasVO.setSituacao("ER");
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)	
	public void realizarGeracaoAproveitamentoDisciplinaAutomaticoPorRequerimento(RequerimentoVO requerimento, UsuarioVO usuarioVO) throws Exception{		
		AproveitamentoDisciplinaVO objDeferido = consultarPorCodigoRequerimento(requerimento.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS,  usuarioVO);
		if(Uteis.isAtributoPreenchido(objDeferido)) {
			objDeferido.getMatricula().setMatriculaComHistoricoAlunoVO(getFacadeFactory().getHistoricoFacade().carregarDadosMatriculaComHistoricoAlunoVO(objDeferido.getMatricula(), objDeferido.getMatricula().getGradeCurricularAtual().getCodigo(), false, objDeferido.getMatricula().getCurso().getConfiguracaoAcademico(), usuarioVO));
			objDeferido.setGradeCurricular(objDeferido.getMatricula().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getGradeCurricularVO());
//			realizarPreenchimentoRequerimentoAproveitamentoDisciplinck curto aAutomaticoPorRequerimento(objDeferido, requerimento, usuarioVO);
			alterar(objDeferido, usuarioVO);
		}else {
			objDeferido.setResponsavelAutorizacao(usuarioVO);
			objDeferido.setMatricula(requerimento.getMatricula());
			objDeferido.setMatriculaPeriodo(getFacadeFactory().getMatriculaPeriodoFacade().consultarUltimaMatriculaPeriodoPorMatriculaAnoSemestre(objDeferido.getMatricula().getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
			objDeferido.setPeridoLetivo(objDeferido.getMatriculaPeriodo().getPeridoLetivo());
			objDeferido.setUnidadeEnsinoCurso(objDeferido.getMatriculaPeriodo().getUnidadeEnsinoCurso());
			objDeferido.setAproveitamentoPrevisto(Boolean.FALSE);
			objDeferido.setDisciplinaForaGrade(Boolean.FALSE);
			objDeferido.setCodigoRequerimento(requerimento);
			objDeferido.setPessoa(requerimento.getPessoa());		
			objDeferido.setUnidadeEnsino(requerimento.getUnidadeEnsino());
			objDeferido.setCurso(requerimento.getMatricula().getCurso());
			objDeferido.getMatricula().setMatriculaComHistoricoAlunoVO(getFacadeFactory().getHistoricoFacade().carregarDadosMatriculaComHistoricoAlunoVO(objDeferido.getMatricula(), objDeferido.getMatricula().getGradeCurricularAtual().getCodigo(), false, objDeferido.getMatricula().getCurso().getConfiguracaoAcademico(), usuarioVO));
			objDeferido.setGradeCurricular(objDeferido.getMatricula().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getGradeCurricularVO());
			realizarPreenchimentoRequerimentoAproveitamentoDisciplinaAutomaticoPorRequerimento(objDeferido, requerimento, usuarioVO);
			incluir(objDeferido, false, usuarioVO);
		}
	}

	private void realizarPreenchimentoRequerimentoAproveitamentoDisciplinaAutomaticoPorRequerimento(AproveitamentoDisciplinaVO objDeferido, RequerimentoVO requerimento, UsuarioVO usuario) throws Exception {
		for (RequerimentoDisciplinasAproveitadasVO rda : requerimento.getListaRequerimentoDisciplinasAproveitadasVOs()) {
			if(rda.getSituacaoRequerimentoDisciplinasAproveitadasEnum().isDeferido() && objDeferido.getDisciplinasAproveitadasVOs().stream().noneMatch(p-> p.getDisciplina().getCodigo().equals(rda.getDisciplina().getCodigo()))) {
				DisciplinasAproveitadasVO da = realizarPreenchimentoDisciplinaAproveitadasAutomaticoPorRequerimento(objDeferido, new DisciplinasAproveitadasVO(), rda);
				objDeferido.getDisciplinasAproveitadasVOs().add(da);
				getFacadeFactory().getSalaAulaBlackboardOperacaoFacade().realizarExclusaoAlunoDaSalaAulaBlackboardTipoDisciplina(requerimento.getMatricula().getMatricula(), rda.getDisciplina().getCodigo(), usuario);
			} else if(Uteis.isAtributoPreenchido(objDeferido) && rda.getSituacaoRequerimentoDisciplinasAproveitadasEnum().isDeferido() && objDeferido.getDisciplinasAproveitadasVOs().stream().anyMatch(p-> p.getDisciplina().getCodigo().equals(rda.getDisciplina().getCodigo()))) {
				Optional<DisciplinasAproveitadasVO> findFirst = objDeferido.getDisciplinasAproveitadasVOs().stream().filter(p-> p.getDisciplina().getCodigo().equals(rda.getDisciplina().getCodigo())).findFirst();
				realizarPreenchimentoDisciplinaAproveitadasAutomaticoPorRequerimento(objDeferido, findFirst.get(), rda);
			}else if(Uteis.isAtributoPreenchido(objDeferido) && rda.getSituacaoRequerimentoDisciplinasAproveitadasEnum().isIndeferido() && objDeferido.getDisciplinasAproveitadasVOs().stream().anyMatch(p-> p.getDisciplina().getCodigo().equals(rda.getDisciplina().getCodigo()))) {
				objDeferido.getDisciplinasAproveitadasVOs().removeIf(p-> p.getDisciplina().getCodigo().equals(rda.getDisciplina().getCodigo()));
			}
		}
	}

	private DisciplinasAproveitadasVO realizarPreenchimentoDisciplinaAproveitadasAutomaticoPorRequerimento(AproveitamentoDisciplinaVO objDeferido, DisciplinasAproveitadasVO da, RequerimentoDisciplinasAproveitadasVO rda) {		
		da.setDisciplina(rda.getDisciplina());
		da.setNota(rda.getNota());
		da.setFrequencia(rda.getFrequencia());		
		da.setAno(rda.getAno());
		da.setSemestre(rda.getSemestre());				
		da.setInstituicao(rda.getInstituicao());
		da.setUtilizaNotaConceito(rda.getUtilizaNotaConceito());
		da.setMediaFinalConceito(rda.getMediaFinalConceito());
		da.setCidade(rda.getCidade());
		da.setCargaHoraria(rda.getCargaHoraria());
		da.setCargaHorariaCursada(rda.getCargaHorariaCursada());
		da.setNomeDisciplinaCursada(rda.getNomeDisciplinaCursada());
		da.setSituacaoHistorico(rda.getSituacaoHistorico());
		da.setTipo(rda.getTipo());
		da.setNomeProfessor(rda.getNomeProfessor());
		da.setTitulacaoProfessor(rda.getTitulacaoProfessor());
		da.setSexoProfessor(rda.getSexoProfessor());
		realizarPreenchimentoGradeDisciplinaAutomaticoPorRequerimento(objDeferido, da);
		return da;
	}
	

	private void realizarPreenchimentoGradeDisciplinaAutomaticoPorRequerimento(AproveitamentoDisciplinaVO objDeferido, DisciplinasAproveitadasVO da) {
		Optional<GradeDisciplinaVO> findFirst = objDeferido.getGradeCurricular().getPeriodoLetivosVOs().stream().flatMap(p-> p.getGradeDisciplinaVOs().stream()).filter(pp-> pp.getDisciplina().getCodigo().equals(da.getDisciplina().getCodigo())).findFirst();
		if(findFirst.isPresent()) {					
			da.setGradeDisciplinaVO(findFirst.get());
			PeriodoLetivoComHistoricoAlunoVO periodoLetivoComHistoricoAlunoVO = objDeferido.getMatricula().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getPeriodoLetivoComHistoricoAlunoVOPorCodigo(da.getGradeDisciplinaVO().getPeriodoLetivoVO().getCodigo());
			da.getGradeDisciplinaVO().setHistoricoAtualAluno(periodoLetivoComHistoricoAlunoVO.obterHistoricoAtualGradeDisciplinaVO(da.getGradeDisciplinaVO().getCodigo()));
			da.setHistoricoAtual(da.getGradeDisciplinaVO().getHistoricoAtualAluno());
			if (da.getGradeDisciplinaVO().getHistoricoAtualAluno().getCursando()) {
				// se está cursando é por que já existe um histórico gerado para a disciplina logo o mesmo deverá ser excluído pelo sistema, para que seja gerado outro histórico
				da.setExcluirHistoricoDisciplinaCursada(true);
			}	
		}
	}

}



