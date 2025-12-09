package negocio.interfaces.secretaria;

import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.secretaria.TransferenciaMatrizCurricularMatriculaVO;
import negocio.comuns.secretaria.TransferenciaMatrizCurricularVO;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

public interface TransferenciaMatrizCurricularInterfaceFacade {

	public void carregarDados(TransferenciaMatrizCurricularVO obj, NivelMontarDados nivelMontar, UsuarioVO usuario) throws Exception;

	public void incluir(final TransferenciaMatrizCurricularVO obj, UsuarioVO usuarioVO) throws Exception;

	public void alterar(final TransferenciaMatrizCurricularVO obj, UsuarioVO usuarioVO) throws Exception;

	public void excluir(TransferenciaMatrizCurricularMatriculaVO obj, UsuarioVO usuario) throws Exception;

	// void
	// realizarSugestaoMediaEFrequenciaDisciplinaEquivalente(ItemDisciplinaAntigaDisciplinaNovaVO
	// itemDisciplinaAntigaDisciplinaNovaVO, Boolean
	// usarSituacaoAprovadoAproveitamento);
	//
	// void realizarMigracaoGrade(MatriculaVO matriculaVO, MatriculaPeriodoVO
	// matriculaPeriodoVO, CursoVO curso, GradeCurricularVO gradeOrigem,
	// GradeCurricularVO gradeMigrar, List<ItemDisciplinaAntigaDisciplinaNovaVO>
	// listaDisciplinaEquivalente, List<DisciplinaForaGradeVO>
	// listaHistoricoMigrarComoForaGrade, List<HistoricoVO>
	// historicosSemDefinicao, List<HistoricoVO> historicosIguais,
	// List<HistoricoVO> historicoIguaisCursandoComPeriodoDiferente,
	//  UsuarioVO usuario)
	// throws Exception;

	public TransferenciaMatrizCurricularVO consultarPorMatriculaCodigoGradeOrigem(String matricula, Integer gradeCurricular, UsuarioVO usuarioVO);
	
	public TransferenciaMatrizCurricularVO consultarPorMatriculaCodigoGradeAtual(String matricula, Integer gradeCurricularAtual, UsuarioVO usuarioVO);

	public void realizarMigracaoMatrizCurricular(TransferenciaMatrizCurricularVO transferencia, TransferenciaMatrizCurricularMatriculaVO matriculaEspecificaProcessar, UsuarioVO usuario) throws Exception;

	public TransferenciaMatrizCurricularVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, NivelMontarDados nivelMontar, UsuarioVO usuario) throws Exception;

	public List<TransferenciaMatrizCurricularVO> consultaRapidaPorCodigoCurso(Integer codigoCurso, boolean controlarAcesso, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<TransferenciaMatrizCurricularVO> consultaRapidaPorNomeCurso(String nomeCurso, boolean controlarAcesso, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<TransferenciaMatrizCurricularVO> consultaRapidaPorDataTransferencia(Date dataTransferencia, boolean controlarAcesso, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<TransferenciaMatrizCurricularVO> consultaRapidaPorCodigoTransferencia(Integer codigoTransferencia, boolean controlarAcesso, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void cancelarMigracaoMatrizCurricular(TransferenciaMatrizCurricularVO transferencia, TransferenciaMatrizCurricularMatriculaVO matriculaEspecificaProcessar, UsuarioVO usuario) throws Exception;

	boolean executarVerificarMatriculaPeriodoPossuiTransferenciaMatrizCurricularExclusaoPreMatricula(Integer matriculaPeriodo, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;

	void gravar(TransferenciaMatrizCurricularVO transferencia, UsuarioVO usuario) throws Exception;

	void realizarMigracaoMatrizCurricular(TransferenciaMatrizCurricularVO transferencia, UsuarioVO usuario) throws Exception;

	boolean realizarMigracaoMatrizCurricularMatricula(TransferenciaMatrizCurricularVO transferencia, TransferenciaMatrizCurricularMatriculaVO matriculaProcessar, UsuarioVO usuario) throws Exception;

	public List<TransferenciaMatrizCurricularVO> consultaRapidaPorMatricula(String matricula, boolean controlarAcesso, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<TransferenciaMatrizCurricularVO> consultaRapidaPorNomeAluno(String nomeAluno, boolean controlarAcesso, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception;

	void inicializarDadosMatriculaProcessar(TransferenciaMatrizCurricularMatriculaVO matriculaProcessar);
	
	public Integer processarTransferenciaTodasMatriculasParalelismo(final TransferenciaMatrizCurricularVO transferenciaMatrizCurricularVO, final List<TransferenciaMatrizCurricularMatriculaVO> listaTransferenciaMatrizCurricularMatriculaVOs, final UsuarioVO usuarioVO) throws Exception;

	List<TransferenciaMatrizCurricularVO> consultaRapidaPorRegistroAcademicoAluno(String rgistroAcademico,	boolean controlarAcesso, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception;
}
