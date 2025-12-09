package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.GradeDisciplinaCompostaVO;
import negocio.comuns.academico.GradeDisciplinaVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.LogImpactoMatrizCurricularVO;
import negocio.comuns.academico.PeriodoLetivoVO;
import negocio.comuns.academico.enumeradores.TipoAlteracaoMatrizCurricularEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;

public interface GradeDisciplinaInterfaceFacade {

    /**
     * Operação responsável por retornar um novo objeto da classe <code>GradeDisciplinaVO</code>.
     */
    public GradeDisciplinaVO novo() throws Exception;

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>GradeDisciplinaVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>GradeDisciplinaVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    public void incluir(GradeDisciplinaVO obj, String situacaoGradeCurricular, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>GradeDisciplinaVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>GradeDisciplinaVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    public void alterar(GradeDisciplinaVO obj, String situacaoGradeCurricular, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>GradeDisciplinaVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>GradeDisciplinaVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public void excluir(GradeDisciplinaVO obj, UsuarioVO usuario) throws Exception;

    /**
     * Responsável por realizar uma consulta de <code>GradeDisciplina</code> através do valor do atributo
     * <code>nome</code> da classe <code>Disciplina</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>GradeDisciplinaVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<GradeDisciplinaVO> consultarPorNomeDisciplina(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public GradeDisciplinaVO consultarPorNomeDisciplina(String valorConsulta, Integer curso, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    /**
     * Responsável por realizar uma consulta de <code>GradeDisciplina</code> através do valor do atributo
     * <code>codigo</code> da classe <code>PeriodoLetivo</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>GradeDisciplinaVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<GradeDisciplinaVO> consultarPorCodigoPeriodoLetivo(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por localizar um objeto da classe <code>GradeDisciplinaVO</code>
     * através de sua chave primária.
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public GradeDisciplinaVO consultarPorCodigoPeriodoLetivoCodigoDisciplina(Integer periodo, Integer disciplina, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

    public List<GradeDisciplinaVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por excluir todos os objetos da <code>GradeDisciplinaVO</code> no BD.
     * Faz uso da operação <code>excluir</code> disponível na classe <code>GradeDisciplina</code>.
     * @param <code>gradeCurricular</code> campo chave para exclusão dos objetos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void excluirGradeDisciplinas(PeriodoLetivoVO periodoLetivo, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por alterar todos os objetos da <code>GradeDisciplinaVO</code> contidos em um Hashtable no BD.
     * Faz uso da operação <code>excluirGradeDisciplinas</code> e <code>incluirGradeDisciplinas</code> disponíveis na classe <code>GradeDisciplina</code>.
     * @param objetos  List com os objetos a serem alterados ou incluídos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void alterarGradeDisciplinas(Integer periodoLetivo, List<GradeDisciplinaVO> objetos, String situacaoGradeCurricular, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por incluir objetos da <code>GradeDisciplinaVO</code> no BD.
     * Garantindo o relacionamento com a entidade principal <code>academico.PeriodoLetivo</code> através do atributo de vínculo.
     * @param objetos List contendo os objetos a serem gravados no BD da classe.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void incluirGradeDisciplinas(Integer periodoLetivo, List<GradeDisciplinaVO> objetos, String situacaoGradeCurricular, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por localizar um objeto da classe <code>GradeDisciplinaVO</code>
     * através de sua chave primária.
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public GradeDisciplinaVO consultarPorChavePrimaria(Integer codigo, UsuarioVO usuario) throws Exception;

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade);

    public List<GradeDisciplinaVO> consultarGradeDisciplinas(Integer periodoLetivo, boolean controlarAcesso, UsuarioVO usuario, ConfiguracaoAcademicoVO configuracaoAcademicoVO) throws Exception;

    public List<GradeDisciplinaVO> consultarGradeDisciplinasEquivalentes(Integer disciplina, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public void excluirObjDisciplinaPreRequisitoVOs(Integer codigo, GradeDisciplinaVO gradeDisciplinaVO, UsuarioVO usuario) throws Exception;

    public void excluirPorCodigoDisciplinaPeriodoLetivo(Integer periodoLetivo, Integer disciplina, UsuarioVO usuario) throws Exception;

    public List<GradeDisciplinaVO> consultarGradeDisciplinasEquivalentesPorGradeDisciplina(Integer gradeDisciplina, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public List<GradeDisciplinaVO> consultarGradeDisciplinasEquivalenteGradeAtiva(Integer disciplina, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	Boolean consultarDisciplinaAplicaTCC(int gradeCurricular, int disciplina) throws Exception;
	
	GradeDisciplinaVO consultarGradeDisciplinaVOTCC(int gradeCurricular, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	List<GradeDisciplinaVO> consultarDadosParaMigracaoGradePorGradeCurricular(Integer gradeCurricular) throws Exception;
	
	void executarDefinirDisciplinaUtilizaTCC(final Integer gradeDisciplina, UsuarioVO usuario) throws Exception;
	
	public GradeDisciplinaVO consultarPorMatriculaDisciplina(String matricula, Integer disciplina, UsuarioVO usuarioVO) throws Exception;
	
	public GradeDisciplinaVO consultarPorGradeCurricularEDisciplina(Integer gradeCurricular, Integer disciplina, UsuarioVO usuarioVO, ConfiguracaoAcademicoVO configuracaoAcademicoVO) throws Exception;
	
	public GradeDisciplinaVO consultarPorMatriculaDisciplinaMatriculaPeriodo(String matricula, Integer matriculaPeriodo, Integer disciplina, UsuarioVO usuarioVO) throws Exception;
	
	public List<GradeDisciplinaVO> consultarDisciplinaMinistrouHorarioProfessorPorCodigo(Integer pessoa, Integer turma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public Integer consultarCargaHorariaDisciplinaPorDisciplinaETurma(Integer disciplina, String matricula, UsuarioVO usuarioVO);
	
	public List<GradeDisciplinaVO> consultarDisciplinasObrigatoriasNaoCumpridasDaGrade(String matricula, Integer gradeCurricular, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, Integer limite) throws Exception;

	void adicionarGradeDisciplinaCompostaVOs(GradeDisciplinaVO gradeDisciplinaVO, GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO, String situacaoGradeCurricular, GradeDisciplinaVO gradeTipo) throws Exception;

	void removerGradeDisciplinaCompostaVOs(GradeDisciplinaVO gradeDisciplinaVO, GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO) throws Exception;
	
	public Integer consultarCargaHorariaPorTurmaDisciplina(Integer turma, Integer disciplina, UsuarioVO usuarioVO);
	
	public Integer consultarCargaHorariaPorChavePrimaria(Integer codigo);
	
	public GradeDisciplinaVO consultarGradeDisciplinaPorChavePrimariaDadosCargaHorariaNrCreditos(Integer codigo);

	List<GradeDisciplinaVO> consultaRapidaGradeDisciplinaPorPeriodoLetivo(Integer periodoLetivo, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	
	public Integer consultarCargaHorariaPorGradeCurricularPeriodoLetivoDeDisciplinaQueNaoEstaNoHistorico(String matricula, Integer gradeCurricular, Integer periodoLetivo, UsuarioVO usuarioVO);

	void validarDadosFormulaCalculoComposicao(GradeDisciplinaVO obj) throws ConsistirException;

	/** 
	 * @author Wellington - 2 de fev de 2016 
	 * @param turma
	 * @param controlarAcesso
	 * @param usuario
	 * @return
	 * @throws Exception 
	 */
	List<GradeDisciplinaVO> consultarPorTurmaDisciplinaCompostaEstudarQuantidadeComposta(Integer turma, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<GradeDisciplinaVO> consultarPorGradeDisciplinaCompostaPorGrade(Integer gradeCurricular, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	Integer consultarHoraAulaDisciplinaPorDisciplinaEMatricula(Integer disciplina, String matricula,
			UsuarioVO usuarioVO);
	
	public GradeDisciplinaVO consultarPorChavePrimariaSemExcecao(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	List<LogImpactoMatrizCurricularVO> validarDadosImpactoMatriculaFormadaInclusaoGradeDisciplinaMatrizCurricular(GradeCurricularVO gradeCurricularVO, GradeDisciplinaVO gradeDisciplinaIncluirVO, StringBuilder msgAvisoAlteracaoGrade, UsuarioVO usuarioVO);

	void validarDadosImpactoAlteracaoGradeDisciplina(GradeCurricularVO gradeCurricularVO, GradeDisciplinaVO gradeDisciplinaEditadaVO, StringBuilder msgAvisoAlteracaoGrade, UsuarioVO usuarioVO) throws Exception;

	void validarDadosImpactoExclusaoGradeDisciplina(GradeCurricularVO gradeCurricularVO, GradeDisciplinaVO gradeDisciplinaExcluirVO, UsuarioVO usuarioVO) throws Exception;

	void validarDadosLiEConcordoComOsTermosAlteracaoGradeDsiciplina(GradeDisciplinaVO gradeDisciplinaVO) throws Exception;

	void excluirImpactoExclusaoGradeDisciplina(GradeDisciplinaVO gradeDisciplinaExcluirVO, UsuarioVO usuarioVO) throws Exception;

	void realizarCorrecaoImpactosAlteracaoGradeDisciplina(GradeDisciplinaVO gradeDisciplinaEdicaoVO, List<HistoricoVO> historicoVOs, TipoAlteracaoMatrizCurricularEnum tipoAlteracaoMatrizCurricularEnum, UsuarioVO usuarioVO, GradeCurricularVO gradeCurricularVO) throws Exception;
	
	public List<GradeDisciplinaVO> consultarPorGradeCurricular(Integer gradeCurricular, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception ;

	List<GradeDisciplinaVO> consultarGradeDisciplinaVOTCCsNaoAprovadoAluno(String matricula, int gradeCurricular, int nivelMontarDados, UsuarioVO usuario)
			throws Exception;
}
