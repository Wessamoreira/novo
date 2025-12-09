package negocio.interfaces.academico;

import java.util.Date;
import java.util.List;

import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.GradeCurricularGrupoOptativaDisciplinaVO;
import negocio.comuns.academico.GradeDisciplinaVO;
//import negocio.comuns.academico.MapaLocalAulaTurmaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.TurmaDisciplinaCompostaVO;
import negocio.comuns.academico.TurmaDisciplinaEstatisticaAlunoVO;
import negocio.comuns.academico.TurmaDisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.DefinicoesTutoriaOnlineEnum;
import negocio.comuns.academico.enumeradores.ModalidadeDisciplinaEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import webservice.servicos.MatriculaRSVO;
import webservice.servicos.objetos.DisciplinaRSVO;

public interface TurmaDisciplinaInterfaceFacade {

	TurmaDisciplinaVO novo() throws Exception;

	void incluir(TurmaDisciplinaVO obj, UsuarioVO usuarioVO) throws Exception;

	void alterar(TurmaDisciplinaVO obj, UsuarioVO usuarioVO) throws Exception;

	void zerarAlunosMatriculadosTurma(Integer turma) throws Exception;

	void excluir(TurmaDisciplinaVO obj) throws Exception;

	void excluirTurmaDisciplinas(Integer turma, UsuarioVO usuario) throws Exception;

	void excluirTurmaDisciplinas(Integer turma, List<TurmaDisciplinaVO> objetos, UsuarioVO usuario) throws Exception;

	List<TurmaDisciplinaVO> consultarPorDisciplina(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<TurmaDisciplinaVO> consultarPorCodigoTurma(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	TurmaDisciplinaVO consultarPorCodigoTurmaCodigoDisciplina(Integer turma, Integer disciplina, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public TurmaDisciplinaVO consultarPorMatriculaPeriodoCodigoDisciplina(Integer matriculaPeriodo, Integer disciplina, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception ;

	List<TurmaDisciplinaVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	TurmaDisciplinaVO consultarExistenciaVagaTurmaDisciplina(Integer turma, Integer disciplina, Integer nrVagas, boolean controlarAcesso, int nivelMontaDados, UsuarioVO usuario) throws Exception;

	void alterarTurmaDisciplinas(TurmaVO turma, List<TurmaDisciplinaVO> objetos, UsuarioVO usuarioVO) throws Exception;

	void incluirTurmaDisciplinas(TurmaVO turma, List<TurmaDisciplinaVO> objetos, UsuarioVO usuarioVO) throws Exception;

	List<TurmaDisciplinaVO> consultarTurmaDisciplinas(Integer turma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	TurmaDisciplinaVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	void setIdEntidade(String idEntidade);

	TurmaDisciplinaVO consultarNrAlunosMatriculadosPelaTurmaEDisciplina(Integer turma, Integer disciplina, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	void atualizarNrAlunosMatriculados(TurmaDisciplinaVO turmaDisciplina, UsuarioVO usuario) throws Exception;

    void excluirPorCodigoDisciplinaTurma(Integer turma, Integer disciplina, UsuarioVO usuario) throws Exception;

    ModalidadeDisciplinaEnum consultarModalidadeDisciplinaPorTurmaEDisciplina(Integer codigoTurma, Integer codigoDisciplina);

	Integer consultarTotalDisciplinaTurma(Integer turma);

//	List<MapaLocalAulaTurmaVO> consultarMapaLocalAulaTurma(Integer unidadeEnsino, Date dataInicio, Date dataTermino, Integer curso, Integer turma, Integer disciplina) throws Exception;

	void alterarLocalSala(TurmaDisciplinaVO obj, UsuarioVO usuario) throws Exception;
		
	List<TurmaDisciplinaVO> consultarDisciplinaDiferenteTurmaAgrupada(Integer turmaAgrupada, List<TurmaDisciplinaVO> listaTurmaDisciplinaAgrupadaVOs, UsuarioVO usuarioVO);

	TurmaDisciplinaVO consultarPorTurmaDisciplina(Integer turma, Integer disciplina, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;
	
	public boolean validarTurmaDisciplinaEOnline(TurmaVO turma, DisciplinaVO disciplinaVO, UsuarioVO usuarioLogado) throws Exception;
		
	List<TurmaDisciplinaVO> consultaRapidaPorTurma(Integer turmaSugerida, Integer turmaPrincipal, UsuarioVO usuarioVO);

	DefinicoesTutoriaOnlineEnum consultarDefinicoesTutoriaOnlineTurmaDisciplina(Integer codigoTurma, Integer codigoDisciplina) throws Exception;

	void montarDadosListaSelectItemModalidade(List<TurmaDisciplinaVO> turmaDisciplinaVOs) throws Exception;
		
	List<TurmaDisciplinaVO> consultarPorCodigoUnidadeEnsino(Integer codigoUnidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	/**
	 * @author Rodrigo Wind - 11/08/2015
	 * @param unidadeEnsino
	 * @param curso
	 * @param turno
	 * @param turma
	 * @param disciplina
	 * @param ano
	 * @param semestre
	 * @param configuracaoAcademica
	 * @param situacoesMatriculaPeriodo
	 * @return
	 * @throws Exception
	 */
	List<TurmaDisciplinaVO> consultarTurmaDisciplinaCalcularMedia(Integer unidadeEnsino, Integer curso, Integer turno, Integer turma, Integer disciplina, String ano, String semestre, Integer configuracaoAcademica, String situacoesMatriculaPeriodo, String nivelEducacional) throws Exception;

	/** 
	 * @author Wellington - 10 de mar de 2016 
	 * @param disciplinaVO
	 * @param modalidade
	 * @param disciplinaReferenteAUmGrupoOptativa
	 * @param gradeDisciplinaVO
	 * @param gradeCurricularGrupoOptativaDisciplinaVO
	 * @param configuracaoAcademico
	 * @param permiteReposicao
	 * @param nrMaximoMatricula
	 * @param nrVagas
	 * @return
	 * @throws Exception 
	 */
	TurmaDisciplinaVO executarGeracaoTurmaDisciplinaVO(DisciplinaVO disciplinaVO, ModalidadeDisciplinaEnum modalidade, Boolean disciplinaReferenteAUmGrupoOptativa, GradeDisciplinaVO gradeDisciplinaVO, GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO, ConfiguracaoAcademicoVO configuracaoAcademico, Boolean permiteReposicao, Integer nrMaximoMatricula, Integer nrVagas) throws Exception;
	
	Boolean consultarSeExisteTurmaDisciplinaVinculadaAGradeCurricular(Integer gradeCurricular, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	Boolean consultarSeTurmaDisciplinaSaoEad(Integer turma, Integer disciplina, UsuarioVO usuario) throws Exception;
	
	boolean consultarSeTurmaDisciplinaSaoEadPorUnidadeEnsinoPorCursoPorNivelEducacionalPorTurmaPorDisciplina(Integer unidadeEnsino, Integer curso, String nivelEducacional, Integer turma, Integer disciplina, UsuarioVO usuario) throws Exception;

	boolean consultarDisciplinaCompostaTurmaDisciplina(Integer turma, Integer disciplina);
	
	public TurmaDisciplinaVO consultarPorTurmaCodigoDisciplinaComGradeDiscilina(Integer turma, Integer disciplina, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception; 

	List<TurmaDisciplinaEstatisticaAlunoVO> consultarPorAlunoComModalidadeDiferenteTurma(TurmaVO turmaVO,
			TurmaDisciplinaVO turmaDisciplinaVO, UsuarioVO usuario) throws Exception;

	List<TurmaDisciplinaVO> consultarTurmaDisciplinaTurmaBasePartindoTurmaAgrupadaEDisciplina(Integer turmaAgrupada,
			Integer disciplina) throws Exception;

	void realizarVerificacaoExistenciaAlunoMatriculaOUAulaProgramadaTurmaDisciplina(TurmaVO turma);

	List<TurmaDisciplinaEstatisticaAlunoVO> consultarPorAlunoComConfiguracaoAcademicaDiferenteTurma(TurmaVO turmaVO,
			TurmaDisciplinaVO turmaDisciplinaVO, TurmaDisciplinaCompostaVO turmaDisciplinaCompostaVO,
			ConfiguracaoAcademicoVO configuracaoAcademicoVO, DisciplinaVO disciplinaVO, UsuarioVO usuario)
			throws Exception;	
	
	void alterarTurmaDisciplinaPorAlteracaoGradeCurricularCursoIntegral(Integer turma, Integer disciplina, Integer gradeDisciplina, Integer gradeCurricularGrupoOptativaDisciplina, UsuarioVO usuario) throws Exception;

	void consultaTurmaDisciplinaCompletaPorTurma(TurmaVO obj, UsuarioVO usuarioVO) throws Exception;
	
	void excluirPorGradeDisciplina(Integer gradeDisciplina, UsuarioVO usuario) throws Exception;
	
	public void removerDisciplinasTurmaMarcadasParaNaoEstudar(TurmaVO obj,	List<DisciplinaRSVO> listaDisciplina)throws Exception;



	void removerTurmaDisciplinasNaoMarcadasParaEstudar(MatriculaRSVO matriculaRSVO,
			MatriculaPeriodoVO matriculaPeriodoVO);


}