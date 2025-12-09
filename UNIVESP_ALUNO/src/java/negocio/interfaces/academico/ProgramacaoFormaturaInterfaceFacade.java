package negocio.interfaces.academico;
import java.util.Date;
import java.util.List;

import org.primefaces.event.FileUploadEvent;

import negocio.comuns.academico.ColacaoGrauVO;
import negocio.comuns.academico.ControleLivroRegistroDiplomaUnidadeEnsinoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.ProgramacaoFormaturaAlunoVO;
import negocio.comuns.academico.ProgramacaoFormaturaUnidadeEnsinoVO;
import negocio.comuns.academico.ProgramacaoFormaturaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.arquitetura.UsuarioVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
*/
public interface ProgramacaoFormaturaInterfaceFacade {
	

    public ProgramacaoFormaturaVO novo() throws Exception;
    public void alterar(ProgramacaoFormaturaVO obj, UsuarioVO usuarioVO) throws Exception;
    public void excluir(ProgramacaoFormaturaVO obj, UsuarioVO usuarioVO) throws Exception;
    public ProgramacaoFormaturaVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados , UsuarioVO usuario) throws Exception;
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados , UsuarioVO usuario) throws Exception;
    public List consultarPorNomeUnidadeEnsino(String valorConsulta, int nivelMontarDados , UsuarioVO usuario) throws Exception;
    public List consultarPorNomeCurso(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List consultarPorNomeTurno(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List consultarPorIdentificadorTurmaTurma(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List consultarPorMatriculaMatricula(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List consultarPorDataColacaoGrau(Date prmIni, Date prmFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List consultarPorDataCadastro(Date prmIni, Date prmFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List consultarPorNomeUsuario(String valorConsulta, int nivelMontarDados , UsuarioVO usuario) throws Exception;
    public List<ProgramacaoFormaturaVO> consultarPorCodigoFiltroAlunosPresentesColacaoGrau(Integer valorConsulta, String colouGrau, boolean controlarAcesso, int nivelMontarDados , UsuarioVO usuario) throws Exception;
    public List<ProgramacaoFormaturaVO> consultarPorNomeUnidadeEnsinoFiltroAlunosPresentesColacaoGrau(String valorConsulta, String colouGrau, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<ProgramacaoFormaturaVO> consultarPorNomeCursoFiltroAlunosPresentesColacaoGrau(String valorConsulta, Integer unidadeEnsino, String colouGrau, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<ProgramacaoFormaturaVO> consultarPorNomeTurnoFiltroAlunosPresentesColacaoGrau(String valorConsulta, Integer unidadeEnsino, String colouGrau, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<ProgramacaoFormaturaVO> consultarPorIdentificadorTurmaFiltroAlunosPresentesColacaoGrau(String valorConsulta, Integer unidadeEnsino, String colouGrau, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<ProgramacaoFormaturaVO> consultarPorMatriculaMatriculaFiltroAlunosPresentesColacaoGrau(String valorConsulta, Integer unidadeEnsino, String colouGrau, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<ProgramacaoFormaturaVO> consultarPorNomeUsuarioFiltroAlunosPresentesColacaoGrau(String valorConsulta, String colouGrau, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<ProgramacaoFormaturaVO> consultarPorDataRequerimentoFiltroAlunosPresentesColacaoGrau(Date prmIni, Date prmFim, Integer unidadeEnsino, String colouGrau, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<ProgramacaoFormaturaVO> consultarPorDataColacaoGrauFiltroAlunosPresentesColacaoGrau(Date prmIni, Date prmFim, Integer unidadeEnsino, String colouGrau, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<ProgramacaoFormaturaVO> consultarPorDataCadastroFiltroAlunosPresentesColacaoGrau(Date prmIni, Date prmFim, Integer unidadeEnsino, String colouGrau, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public void setIdEntidade(String aIdEntidade);
	public List consultarPorDataRequerimento(Date dateTime, Date dateTime2, Integer codigo, boolean b, int nivelmontardadosDadosbasicos, UsuarioVO usuario) throws Exception;
    void incluir(ProgramacaoFormaturaVO obj,  UsuarioVO usuario) throws Exception;
    public List<ProgramacaoFormaturaVO> consultarCodigoVinculadoColacaoGrau(ColacaoGrauVO colacaoGrauVO,UsuarioVO usuarioVO) throws Exception;
    public void executarExclusaoProgramacaoFormaturaVinculadaColacaoGrau(ColacaoGrauVO colacaoGrauVO,UsuarioVO usuarioVO) throws Exception;
	public Integer consultarPorUnidadeEnsinoEColacaoGrau(Integer codigoUnidadeEnsino, Integer codigoColacaoGrau);
	public void incluir(final ProgramacaoFormaturaVO obj,UsuarioVO usuario, boolean incluirProgramacaoFormaturaAlunos) throws Exception;
	public void incluirSeNaoExistirProgramacaoFormatura(ProgramacaoFormaturaAlunoVO programacaoFormaturaAlunoVO, MatriculaVO matriculaVO, ColacaoGrauVO colacaoGrau, UsuarioVO usuario) throws Exception;
	public List<ProgramacaoFormaturaVO> consultarPorRegistroAcademico(String valorConsulta, Integer unidadeEnsino,	boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public String consultarUnidadesVinculadas(Integer codigoProgramacao);
	
	void adicionarProgramacaoFormaturaAlunoVO(ProgramacaoFormaturaVO programacaoFormaturaVO, List<TurnoVO> listaTurnoVOs, List<CursoVO> listaCursoVO, Integer turma, String matriculaAluno, Integer requerimento, Boolean filtrarEnade, Boolean filtrarTCC, Boolean filtrarCurriculoIntegralizado, UsuarioVO usuarioVO) throws Exception;
	
	public List<ProgramacaoFormaturaVO> consultarPorProgramacaoUnidadeEnsino(String valorConsulta, int nivel, UsuarioVO usuarioVO) throws Exception;
	
	public List consultarPorNomeAluno(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public List<ProgramacaoFormaturaVO> consultarPorCodigoEUnidadeEnsinos(Integer valorConsulta, List<ControleLivroRegistroDiplomaUnidadeEnsinoVO> controleLivroRegistroDiplomaUnidadeEnsinoVOs,boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public List<ProgramacaoFormaturaVO> consultarPorColacaoGrau(String valorConsultaProgramacao, List<ControleLivroRegistroDiplomaUnidadeEnsinoVO> controleLivroRegistroDiplomaUnidadeEnsinoVOs, boolean controlarAcesso, int nivelmontardados, UsuarioVO usuarioLogado) throws Exception;
	Boolean validarDataLimitePodeAssinarAta(ProgramacaoFormaturaVO programacaoFormaturaVO);
	
	public ProgramacaoFormaturaVO consultarProgramacaoFormaturaAtivaPorProgramacaoFormaturaAluno(ProgramacaoFormaturaAlunoVO programacaoFormaturaAlunoVO, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public List<ProgramacaoFormaturaAlunoVO> realizarProcessamentoExcelPlanilha(FileUploadEvent uploadEvent) throws Exception;
	
	public void realizarProcessamentoAlunosAusentes(String situacao, ProgramacaoFormaturaVO programacaoFormaturaVO, List<ProgramacaoFormaturaAlunoVO> listaProgramacaoFormaturaAlunosAusentes, List<ProgramacaoFormaturaAlunoVO> listaProgramacaoFormaturaAlunosEncontrados);

}