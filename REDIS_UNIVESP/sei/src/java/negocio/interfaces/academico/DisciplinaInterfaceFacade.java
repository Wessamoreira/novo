package negocio.interfaces.academico;

import java.util.List;
import java.util.Map;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.*;
import negocio.comuns.academico.enumeradores.ClassificacaoDisciplinaEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.ProgramacaoTutoriaOnlineVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.protocolo.TipoRequerimentoVO;

public interface DisciplinaInterfaceFacade {

    public DisciplinaVO novo() throws Exception;

    public void incluir(DisciplinaVO obj, UsuarioVO usuario) throws Exception;

    public void alterar(DisciplinaVO obj, UsuarioVO usuario) throws Exception;

    public void excluir(DisciplinaVO obj, UsuarioVO usuario) throws Exception;

    public DisciplinaVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<DisciplinaVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<DisciplinaVO> consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<DisciplinaVO> consultarPorNome(String valorConsulta, Integer turma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<DisciplinaVO> consultarPorNomeDisciplinasTurma(String valorConsulta, TurmaVO turma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<DisciplinaVO> consultarPorNomeAreaConhecimento(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<DisciplinaVO> consultarDisciplinaProfessorTurma(Integer professor, Integer turma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void setIdEntidade(String aIdEntidade);

    public List<DisciplinaVO> consultarDisciplinaPorGradeCurricular(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<DisciplinaVO> consultarDisciplinaPorGradeCurricularEPeriodoLetivo(Integer valorConsulta, Integer periodoLetivo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<DisciplinaVO> consultarPorTurmaCodigo(Integer valorConsulta, Integer turma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<DisciplinaVO> consultarPorTurmaNome(String valorConsulta, Integer turma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    List<DisciplinaVO> consultarDisciplinaTurmaAgrupada(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    List<DisciplinaVO> consultarDisciplinaPorTurmaHorarioTurmaProfessorDisciplina(Integer turma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<DisciplinaVO> consultarDisciplinaPorMatricula(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<DisciplinaVO> consultarDisciplinaPorCurso(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<DisciplinaVO> consultarPorCodigo_Matricula(Integer codigo, String matricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<DisciplinaVO> consultarPorNome_Matricula(String nome, String matricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<DisciplinaVO> consultarPorNomeAreaConhecimento_Matricula(String nomeAreaConhecimento, String matricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<DisciplinaVO> consultarPorNomeAreaConhecimento_Curso(String nomeAreaConhecimento, Integer curso, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<DisciplinaVO> consultarPorNomeAreaConhecimento_Turma(String nomeAreaConhecimento, Integer turma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<DisciplinaVO> consultarPorCodigo_Curso(Integer codigo, Integer curso, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<DisciplinaVO> consultarPorNomeDisciplinaCurso(String nome, Integer curso, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    
    public List<DisciplinaVO> consultarPorNome_CursoComGrade(String nome, Integer curso, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    List<DisciplinaVO> consultarDisciplinaAluno(Integer unidadeEnsino, String periodicidade, String matricula, int nivelMontarDados, UsuarioVO usuario, String ano, String semestre) throws Exception;

    public DisciplinaVO consultarPorCodigoGradeDisciplina(Integer gradeDisciplinaAdicionar, int nivelmontardadosDadosbasicos, UsuarioVO usuario) throws Exception;

    List<DisciplinaVO> consultarDisciplinasDaGradeCurricularCursoPorMatriculaTipoDisciplina(String matricula, String string, String string2, boolean b, int nivelmontardadosDadosminimos, UsuarioVO usuario) throws Exception;

    List<DisciplinaVO> consultarPorTurmaOuTurmaAgrupada(String identificadorTurma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    
    List<DisciplinaVO> consultarDisciplinasAptasAproveitamentoDisciplina(String matricula, Integer gradeCurricular, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, ConfiguracaoAcademicoVO configuracaoAcademicoVO, TipoRequerimentoVO tipoRequerimentoVO) throws Exception;

    List<DisciplinaVO> consultarDisciplinasNaoCumpridasDaGrade(String matricula, Integer gradeCurricular, Integer periodoLetivo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    List<DisciplinaVO> consultarDisciplinasOptativasCumpridasDaGrade(String matricula, Integer gradeCurricular, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    List<DisciplinaVO> consultarDisciplinasCumpridasDaGrade(String matricula, Integer gradeCurricular, Integer periodoLetivo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public DisciplinaVO consultarPeriodoLetivoDisciplinaPreRequisito(Integer codigo, Integer codigo2, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    List<DisciplinaVO> consultarDisciplinasPreRequisitoNaoCumpridasDaGrade(String matricula, Integer codigo, Integer codigo2, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void executarUnificacaoDeDisciplinasIdenticadas(ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, Integer disciplinaDefazada, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

    public void executarTransferenciaDisciplinasHistoricoAltigoGradeCorreta_Direito(Integer codigoGradeCurricular, UsuarioVO usuario) throws Exception;

    public void processarTrocaDisciplinaHistoricoDiretamente(Integer codigoGrade, Integer disciplinaDefasada, Integer disciplinaCorreta, UsuarioVO usuario);

    List<DisciplinaVO> consultaRapidaPorMatriculaEMatriculaPeriodo(String matricula, Integer unidadeEnsino, Integer matriculaPeriodo, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    List<DisciplinaVO> consultarPorNomeDisciplina(String nome, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    List<DisciplinaVO> consultarPorCodigoDisciplina(Integer codigo, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    List<DisciplinaVO> consultarDisciplinaPorMatriculaPeriodoLetivo(String valorConsulta, Integer periodoLetivo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public DisciplinaVO consultarDisciplinaEquivalenteAproveitada(Integer curso, Integer gradeCurricular, Integer disciplina, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    //public Integer consultarCargaHorariaCumpridaNoHistorico(String matricula, UsuarioVO usuario) throws Exception;

    public DisciplinaVO consultarDisciplinaPelaDisciplinaEquivalente(Integer curso, Integer gradeCurricular, Integer disciplina, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    List<DisciplinaVO> consultarPorMatriculaPeriodoAnoSemestre(String matricula, String ano, String semestre, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    List<DisciplinaVO> consultarDisciplinasDoHistoricoPorMatriculaPeriodo(Integer matriculaPeriodo, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    List<DisciplinaVO> consultarDisciplinasDaGradePorCodigoOuNome(Integer codigoDisciplina, String nomeDisciplina, int nivelMontarDados, UsuarioVO usuario, Integer... grades) throws Exception;

    List<DisciplinaVO> consultarDisciplinasPorCodigoCursoCodigoDisciplina(Integer codigoCurso, Integer codigoDisciplina, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    List<DisciplinaVO> consultarDisciplinaProfessorTurma(Integer professor, Integer turma, String ano, String semestre, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    List<DisciplinaVO> consultarDisciplinasPorCodigoCursoNomeDisciplina(Integer codigoCurso, String nomeDisciplina, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    List<DisciplinaVO> consultarDisciplinaPorMatriculaPeriodo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    List<DisciplinaVO> consultarDisciplinaHorarioProfessorPorCodigo(Integer pessoa, Integer disciplina, String ano, String semestre, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    List<DisciplinaVO> consultarDisciplinaHorarioProfessorPorNome(Integer pessoa, String disciplina, String ano, String semestre, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    List<DisciplinaVO> consultarDisciplinaHorarioProfessorPorAreaConhecimento(Integer pessoa, String areaConhecimento, String ano, String semestre, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    List<DisciplinaVO> consultaRapidaPorMatriculaEMatriculaPeriodoAtiva(String matricula, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    List<DisciplinaVO> consultarDisciplinaProfessorTurmaValidandoHorarioTurmaDia(Integer professor, Integer turma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    List<DisciplinaVO> consultarDisciplinaProfessorTurmaValidandoHorarioTurmaDia(Integer professor, Integer turma, String ano, String semestre, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    List<DisciplinaVO> consultarPorCodigo_UnidadeEnsino(Integer codigo, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

    List<DisciplinaVO> consultarPorCodigo_GradeCurricular(Integer codigo, Integer gradeCurricular, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

    List<DisciplinaVO> consultarPorNome_UnidadeEnsino(String nome, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

    List<DisciplinaVO> consultarPorNome_GradeCurricular(String nome, Integer gradeCurricular, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

    List<DisciplinaVO> consultarPorCodigoDisciplinaUnidadeEnsinoCodigoCursoCodigoTurma(Integer codigo, Integer unidadeEnsino, Integer curso, Integer turma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    List<DisciplinaVO> consultarPorNomeDisciplinaUnidadeEnsinoCodigoCursoCodigoTurma(String nome, Integer unidadeEnsino, Integer curso, Integer turma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario)
            throws Exception;

    List<DisciplinaVO> consultarDisciplinaProfessorTurmaValidandoHorarioTurmaDiaSemestreAtual(Integer professor, Integer turma, String ano, String semestre, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    List<DisciplinaVO> consultaRapidaPorMatriculasSituacaoHistorico(List<MatriculaVO> listaMatricula, String situacaoHistorico, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    List<DisciplinaVO> consultarDisciplinaCoordenadorPorCodigo(Integer coordenador, Integer disciplina, Integer unidadeEnsino, String ano, String semestre, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    List<DisciplinaVO> consultarDisciplinaCoordenadorPorNome(Integer coordenador, String nomeDisciplina, Integer unidadeEnsino, String ano, String semestre, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    List<DisciplinaVO> consultarDisciplinaCoordenadorPorAreaConhecimento(Integer coordenador, String areaConhecimento, Integer unidadeEnsino, String ano, String semestre, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    List<DisciplinaVO> consultarDisciplinaCoordenadorPorTurma(Integer coordenador, Integer turma, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

//    public Integer consultarCargaHorariaCumpridaNoHistoricoPorGradeCurricular(String matricula, Integer codigoGradeCurricular, UsuarioVO usuario) throws Exception;

    public DisciplinaVO consultarPorChavePrimariaVisaoCoordenador(Integer disciplina, Integer coordenador, Integer unidadeEnsino, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    List<DisciplinaVO> consultarPorNomeVisaoCoordenador(String nomeDisciplina, Integer coordenador, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    List<DisciplinaVO> consultarPorNomeAreaConhecimentoVisaoCoordenador(String areaConhecimento, Integer coordenador, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    List<DisciplinaVO> consultarDisciplinaProfessorTurmaAgrupada(Integer professor, Integer turma, String semestre, String ano, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, Boolean trazerTurmaBaseTurmaAgrupada) throws Exception;

    List<DisciplinaVO> consultarPorCodigo_Matricula_DisciplinaEquivalente(Integer codigo, String matricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    List<DisciplinaVO> consultarPorNome_Matricula_DisciplinaEquivalente(String nome, String matricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    List<DisciplinaVO> consultarPorCodigoCursoTurma(Integer disciplina, Integer curso, Integer turma, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    List<DisciplinaVO> consultarPorNomeCursoTurma(String nomeDisciplina, Integer curso, Integer turma, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public DisciplinaVO consultarPorChavePrimariaCursoTurmaVisaoCoordenador(Integer disciplina, Integer professor, Integer turma, Integer curso, Integer unidadeEnsino, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    List<DisciplinaVO> consultarPorNomeCursoTurmaVisaoCoordenador(String nomeDisciplina, Integer professor, Integer turma, Integer curso, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    List<DisciplinaVO> consultarPorNomeAreaConhecimentoCursoTurmaVisaoCoordenador(String areaConhecimento, Integer professor, Integer turma, Integer curso, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    
    List<DisciplinaVO> consultarPorCodigoAreaConhecimentoCursoTurmaVisaoCoordenador(Integer areaConhecimento, Integer professor, Integer turma, Integer curso, Integer unidadeEnsino, String ano, String semestre, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    List<DisciplinaVO> consultarDisciplinaPorMatriculaPeriodoParaVisualizarNota(String matricula, Integer periodoLetivoMatricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public DisciplinaVO consultarPorChavePrimariaSemExcecao(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    //List<DisciplinaVO> consultarDisciplinaProfessorTurmaAgrupada(Integer professor, Integer turma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<DisciplinaVO> consultaRapidaPorDisciplinaProfessorTurmaAgrupada(Integer professor, Integer turma, String semestre, String ano,  Boolean liberarRegistroAulaEntrePeriodo, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
    
	List<DisciplinaVO> consultaRapidaPorProfessorTurma(Integer professor, Integer turma, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public Integer consultarCargaHorariaCumpridaNoHistoricoPorGradeCurricularComDisciplinaEquivalente(String matricula, Integer codigoGradeCurricular, Boolean trazerDisciplinaForaGrade,  UsuarioVO usuario) throws Exception;

    public void adicionarObjDisciplinaCompostaVOs(DisciplinaCompostaVO disciplinaCompostaVO, DisciplinaVO obj) throws Exception;

    public void excluirObjDisciplinaCompostaVOs(Integer composta, DisciplinaVO obj) throws Exception;

    public void moverParaBaixo(DisciplinaCompostaVO obj, List listaDiscipinaComposta);

    public void moverParaCima(DisciplinaCompostaVO obj, List listaDiscipinaComposta);

    List<DisciplinaVO> consultarPorNome_CursoDisciplinaComposta(String nome, Integer curso, Integer periodoTurma, boolean trazerDisciplinaComposta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    List<DisciplinaVO> consultarPorCodigo_CursoDisciplinaComposta(Integer disciplina, Integer curso, boolean trazerDisciplinaComposta,  boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

//    public DisciplinaVO consultarPorChavePrimariaDadosConfiguracaoAcademica(Integer disciplina, UsuarioVO usuarioVO) throws ConsistirException, Exception;

    List<DisciplinaVO> consultarPorCodigo_Matricula_DisciplinaEquivalenteEDisciplinaComposta(Integer disciplina, String matricula, Integer gradeCurricular, boolean trazerDisciplinaCompostaPrincipal, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    List<DisciplinaVO> consultarPorNome_Matricula_DisciplinaEquivalenteEDisciplinaComposta(String nome, String matricula, Integer gradeCurricular, Integer periodoTurma, boolean trazerDisciplinaCompostaPrincipal, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void validarDadosRemoverDisciplinaComposta(Integer disciplinaComposta, UsuarioVO usuarioVO) throws Exception;

    /**
     * 
     * @param subDisciplina
     * @param matriculaPeriodo
     * @param usuario
     * @return
     * @throws Exception 
     * @deprecated na versao 5.0 - utilizar mapa de equivalencia.
     */
    public DisciplinaVO consultarDisciplinaPorDisciplinaCompostaMatriculaPeriodo(Integer subDisciplina, Integer matriculaPeriodo, UsuarioVO usuario) throws Exception;

    List consultarPorCodigoEspecifico(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    List consultarPorNomeEpecifico(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    List<DisciplinaVO> consutlarDisciplinaPorCursoEPeriodoLetivo(Integer codigoPeriodoLetivo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    List<DisciplinaVO> consutlarDisciplinaPorCodigoTurma(Integer codigoTurma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    
    List<DisciplinaVO> consultarPorNomeDisciplinaUnidadeEnsinoCodigoTurmaAgrupada(String nome, Integer unidadeEnsino, Integer turma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    
    List<DisciplinaVO> consultarPorCodigoDisciplinaUnidadeEnsinoCodigoTurmaAgrupada(Integer codigo, Integer unidadeEnsino, Integer turma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void excluirDisciplinaUnificacaoDisciplina(DisciplinaVO obj, UsuarioVO usuario) throws Exception;

    List<DisciplinaVO> consultarDisciplinaMinistrouHorarioProfessorPorCodigo(Integer pessoa, Integer turma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    List<DisciplinaVO> consultaRapidaPorMatriculaEMatriculaPeriodo(String matricula, Integer disciplina, Integer unidadeEnsino, Integer matriculaPeriodo, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    List<DisciplinaVO> consutlarDisciplinaPorCurso(Integer codigoCurso, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    List<DisciplinaVO> consultarDisciplinaPorMatriculaComConteudoOnline(String matricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    List<DisciplinaVO> consultarDisciplinaPorMatriculaComListaExercicio(String matricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    
    public Integer consultarCargaHorariaCumpridaNoHistoricoGradePorGradeCurricularPorMatricula(String matricula, Integer codigoGradeCurricular, UsuarioVO usuario) throws Exception;
    
    List<DisciplinaVO> consultarPorCodigoDisciplina_GradeCurricular(Integer disciplina, Integer gradeCurricular, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;
    
    public DisciplinaVO consultarDisciplinaEquivalenteTurmaAgrupadaPorTurma(Integer turma, Integer turmaOrigem, Integer disciplina, UsuarioVO usuarioVO);
    
//    public Integer consultarCargaHorariaDisciplinaPorCodigo(Integer codigo, UsuarioVO usuarioVO);
    
    List<DisciplinaVO> consultarDisciplinaPorPeriodoLetivo(Integer periodoLetivo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	Integer consultarTotalCargaHorariaCursadaInclusaoForaGradeTransferenciaMatrizCurricularConcessaoCredito(String matricula, Integer codigoGradeCurricular, UsuarioVO usuario) throws Exception;
	
	Double consultarPercentualMinimoCargaHorariaAproveitamento(Integer disciplina);
	
	Integer consultarQtdeMinimaDeAnosAproveitamento(Integer disciplina);
	
	boolean consultarDisciplinaSeClassificadaComoTcc(Integer codigo, UsuarioVO usuario) throws Exception;
	
	boolean consultarSeExisteDisciplinaPadraoTccPorGradeCurricular(Integer gradeCurricular, UsuarioVO usuario) throws Exception;
	
	DisciplinaVO consultarPorGradeCurricularDisciplinaPadraoTcc(Integer gradeCurricular, boolean retornarExcecao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	List<DisciplinaVO> consultarPorGradeCurricularDisciplinaPadraoTcc(boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	List<DisciplinaVO> consultarPorDisciplinaClassificadaTccPorDivisaoGrupo(boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	List<DisciplinaVO> consultarDisciplinaPorClassificacaoDisciplinaEnum(ClassificacaoDisciplinaEnum classificacaoDisciplinaEnum, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<DisciplinaVO> consultarPorTurmaAnoSemestre(Integer turma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<DisciplinaVO> consultarHorarioTurmaDisciplinaProgramadaPorTurma(Integer turma, boolean trazerDisciplinaCompostaPrincipal, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<DisciplinaVO> consultaRapidaPorNomeAutoComplete(String valorConsulta, Integer unidadeEnsino, int limit, boolean controlarAcesso, UsuarioVO usuario) throws Exception;	
	
	public Integer consultarCargaHorariaCumpridaNoHistoricoPorGradeCurricularComDisciplinaEquivalenteEDisciplinaPorCorrespondecia(String matricula, Integer codigoGradeCurricular, UsuarioVO usuario, String ano) throws Exception;

	public List<DisciplinaVO> consultarDisciplinaDoProfessorEAD(Integer codigoPessoa, Integer unidadeEnsino, Integer curso,  Integer turma, int nivelMontarDados, UsuarioVO usuario ) throws Exception;
	
	List<DisciplinaVO> consultarDisciplinasDoProfessor(Integer professor, Integer unidadeEnsino, Integer curso, Integer turma, String semestre, String ano, int nivelMontarDados, Boolean disciplinasEAD, Boolean trazerDisciplinaMaeComposicao, UsuarioVO usuario) throws Exception;		

	List consultarDisciplinasConteudoUsoExclusivoProfessor(boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	List<DisciplinaVO> consultarPorNomeAreaConhecimentoUnidadeEnsino(String valorConsulta, Integer codigoUnidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	void persistir(DisciplinaVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	Boolean verificarDisciplinaIncluidaGradeDisciplinaGradeCompostaGradeCurricularGrupoOptativaDisciplinaEquivalente(Integer codigoDisciplina, UsuarioVO usuarioVO) throws Exception;

	DisciplinaVO consultarPorChavePrimariaNivelEducacional(Integer codigoPrm, String nivelEducacional, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<DisciplinaVO> consultarPorNomeNivelEducional(String valorConsulta, String nivelEducacional, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	List<DisciplinaVO> consultarDisciplinasPorGradeCurricularPeriodoLetivo(Integer gradeCurricular, Integer periodoLetivo, UsuarioVO usuario) throws Exception;
	
	List<DisciplinaVO> consultarDisciplinasPorGradeCurricularPeriodoLetivoAreaConhecimento(Integer gradeCurricular, Integer periodoLetivo, Integer areaConhecimento, String matricula, String ano, String semestre, UsuarioVO usuario) throws Exception;
	
	List<DisciplinaVO> consultarDisciplinaGradeEOptativaPorPeriodoLetivoFazParteComposicao(Integer periodoLetivo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<DisciplinaVO> consultarDisciplinaGradeEOptativaPorGradeCurricularFazParteComposicao(Integer gradeCurricular, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/** 
	 * @author Wellington Rodrigues - 24 de jul de 2015 
	 * @param turma
	 * @param controlarAcesso
	 * @param nivelMontarDados
	 * @param usuario
	 * @return
	 * @throws Exception 
	 */
	List<DisciplinaVO> consultarDisciplinaGradeEOptativaPorTurmaFazParteComposicao(Integer turma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * @author Rodrigo Wind - 15/10/2015
	 * @param matricula
	 * @param tipoRequerimentoVO
	 * @return
	 */
	List<DisciplinaVO> consultarDisciplinaPorMatriculaAptoVincularRequerimento(String matricula, String ano, String semestre, TipoRequerimentoVO tipoRequerimentoVO, ConfiguracaoAcademicoVO configuracaoAcademicoVO) throws Exception;

    List<DisciplinaVO> consultarDisciplinaMatrizCurricularAluno(String matricula, String nomeDisciplina, Integer codigoDisciplina, 
                boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	/**
	 * @author Rodrigo Wind - 17/10/2015
	 * @param usuarioVO
	 * @param semestre
	 * @param ano
	 * @param buscarTurmasAnteriores
	 * @param turma
	 * @param unidadeEnsino
	 * @return
	 * @throws Exception
	 */
	List<DisciplinaVO> consultarDisciplinaComAtividadeDiscursivaPorProfessorAnoSemestreTurmaAnteriorNivelDadosCombobox(UsuarioVO usuarioVO, String semestre, String ano, Boolean buscarTurmasAnteriores, Integer turma, Integer unidadeEnsino) throws Exception;

	/**
	 * @author Rodrigo Wind - 10/11/2015
	 * @param turma
	 * @param campoConsulta
	 * @param valorConsulta
	 * @param usuario
	 * @return
	 * @throws Exception
	 */
	List<DisciplinaVO> consultarDisciplinaPorTurmaParaLancamentoNota(Integer turma, String campoConsulta, String valorConsulta,  boolean trazerDisciplinaComposta, UsuarioVO usuario) throws Exception;

	/**
	 * @author Rodrigo Wind - 07/12/2015
	 * @param matricula
	 * @param trazerDisciplinaFilhaComposicao
	 * @param naoTrazerDisciplinaCursando
	 * @param controlarAcesso
	 * @param nivelMontarDados
	 * @param usuario
	 * @return
	 * @throws Exception
	 */
	List<DisciplinaVO> consultarDisciplinaAptaParaInclusaoAluno(String matricula, boolean trazerDisciplinaFilhaComposicao, boolean naoTrazerDisciplinaCursando, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/** 
	 * @author Wellington - 30 de mar de 2016 
	 * @param curso
	 * @param controlarAcesso
	 * @param nivelMontarDados
	 * @param usuario
	 * @return
	 * @throws Exception 
	 */
	List<DisciplinaVO> consultarPorCodigoCurso(Integer curso, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	/**
	 * @author Carlos Eugênio - 31/05/2016
	 * @param matricula
	 * @param ano
	 * @param semestre
	 * @param estagio
	 * @param usuarioVO
	 * @return
	 */
	List<DisciplinaVO> consultarApenasDisciplinaEstagioPorMatriculaAnoSemestreNivelComboBox(String matricula, String ano, String semestre, Integer estagio, UsuarioVO usuarioVO);

    List<DisciplinaVO> consultarPorNome(String valorConsulta, Integer codigoCurso, Integer codigoGradeCurricular, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<DisciplinaVO> consultarDisciplinasEstagio(String consultarPor, String valorConsulta);

	/*List<DisciplinaVO> consultarDisciplinasPorMatriculaPeriodoTurmaDisciplina(String matricula, Integer unidadeEnsino,
			Integer curso, Integer turma, String semestre, String ano, int nivelMontarDados, Boolean disciplinasEAD,
			List<ModalidadeDisciplinaEnum> modalidadeDisciplinaEnums,
			UsuarioVO usuario) throws Exception;*/

	Boolean realizarVerificacaoDisciplinaECompostaTurma(Integer turma, Integer disciplina) throws Exception;

	List<DisciplinaVO> consultarPorArtefatoEntregaAluno(Integer artefato, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<DisciplinaVO> consultarDisciplinasPreRequisitoNaoCumpridasConsiderandoMapaEquivalenciaDisciplina(DisciplinaVO disciplinaVO, String matricula, Integer gradeCurricular,
			MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaVO, UsuarioVO usuarioVO) throws Exception;

	public void consultar(DataModelo dataModelo) throws Exception;

	List<DisciplinaVO> consultarDisciplinasDoProfessor(Integer professor, Integer unidadeEnsino, Integer curso,
			Integer turma, String semestre, String ano, int nivelMontarDados, Boolean disciplinasEAD,
			Boolean trazerDisciplinaMaeComposicao, UsuarioVO usuario, boolean professorExcluisivo) throws Exception;
	
	void getSQLPadraoConsultaDisciplinasProfessorProgramacaoAula(Integer professor, Integer unidadeEnsino, Integer curso, Integer turma, StringBuilder sqlStr);
	
	void getSQLPadraoConsultaDisciplinasProfessorEad(Integer professor, Integer unidadeEnsino, Integer curso, Integer turma, StringBuilder sqlStr);
	
	Map<String, Integer> consultarVinculosDisciplinaAlteracaoNome(int codigoDisciplina, String nomeDisciplina, boolean validarAlteracaoNome) throws Exception;

	DisciplinaVO consultarPorChavePrimariaUnica(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario)
			throws Exception;

    public List<DisciplinaVO> consultarPorUnidadeEnsinoCursoTurma(String campoConsulta, String valorConsulta, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Integer curso, Integer turma, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

	
	public List<DisciplinaVO> consultarPorAbreviatura(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception; 
	
	List<Integer> consultarBimestresPorProgramacaoTutoriaOnline(ProgramacaoTutoriaOnlineVO programacaoTutoriaOnlineVO) throws Exception;

	List<DisciplinaVO> consultarPorAbreviatura_Matricula_DisciplinaEquivalenteEDisciplinaComposta(String abreviatura,
			String matricula, Integer gradeCurricular, Integer periodoTurma, boolean trazerDisciplinaCompostaPrincipal,
			boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<DisciplinaVO> consultarPorAbreviatura_CursoDisciplinaComposta(String abreviatura, Integer curso,
			Integer periodoTurma, boolean trazerDisciplinaComposta, boolean controlarAcesso, int nivelMontarDados,
			UsuarioVO usuario) throws Exception;

	List<DisciplinaVO> consultaRapidaDisciplinaPorGradeCurricular(Integer valorConsulta, boolean controlarAcesso,	int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<DisciplinaVO> consultarPorAbreviaturaDisciplinaUnidadeEnsinoCodigoCursoCodigoTurma(String abreviatura,
			Integer unidadeEnsino, Integer curso, Integer turma, boolean controlarAcesso, int nivelMontarDados,
			UsuarioVO usuario) throws Exception;

	 List<DisciplinaVO> consultarDisciplinaPorAbreviatura(String valorConsultaDisciplina, Integer codigoCurso,
			Integer codigoGradeCurricular, boolean controlarAcesso, int nivelmontardadosCombobox, UsuarioVO usuarioLogado) throws Exception;

	DisciplinaVO consultarPorAbreviaturaUnica(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,
			UsuarioVO usuario) throws Exception;

	List consultarPorCodigo_CalendarioRelatorioFacilitador(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<DisciplinaVO> consultarPorNome_CalendarioRelatorioFacilitador(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<DisciplinaVO> consultarPorListaCursoTurma(String nomeDisciplina, Integer curso, Integer turma, Integer unidadeEnsino, List<CursoVO> cursos, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
}

