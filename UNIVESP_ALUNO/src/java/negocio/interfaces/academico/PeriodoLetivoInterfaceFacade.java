package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.GradeDisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.PeriodoLetivoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;

import webservice.servicos.PeriodoLetivoRSVO;

public interface PeriodoLetivoInterfaceFacade {

    /**
     * Operação responsável por retornar um novo objeto da classe <code>PeriodoLetivoVO</code>.
     */
    public PeriodoLetivoVO novo() throws Exception;

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>PeriodoLetivoVO</code>. Primeiramente valida os dados (
     * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>PeriodoLetivoVO</code> que será gravado no banco de dados.
     * @exception Exception
     *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    public void incluir(PeriodoLetivoVO obj, String situacaoGradeCurricular, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>PeriodoLetivoVO</code>. Sempre utiliza a chave primária da classe
     * como atributo para localização do registro a ser alterado. Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a
     * conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>alterar</code>
     * da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>PeriodoLetivoVO</code> que será alterada no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    public void alterar(PeriodoLetivoVO obj, String situacaoGradeCurricular, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>PeriodoLetivoVO</code>. Sempre localiza o registro a ser excluído através da
     * chave primária da entidade. Primeiramente verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na
     * entidade. Isto, através da operação <code>excluir</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>PeriodoLetivoVO</code> que será removido no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public void excluir(PeriodoLetivoVO obj, UsuarioVO usuario) throws Exception;

    /**
     * Responsável por realizar uma consulta de <code>PeriodoLetivo</code> através do valor do atributo <code>sigla</code> da classe
     * <code>PeriodoLetivo</code> Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     *
     * @return List Contendo vários objetos da classe <code>PeriodoLetivoVO</code> resultantes da consulta.
     * @exception Execption
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    // public List consultarPorSiglaPeriodoLetivo(String valorConsulta) throws
    // Exception {
    // getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), true);
    // String sqlStr =
    // "SELECT GradeCurricular.* FROM GradeCurricular, PeriodoLetivo WHERE GradeCurricular.periodoLetivo = PeriodoLetivo.codigo and lower (PeriodoLetivo.sigla) like('"
    // + valorConsulta.toLowerCase() + "%') ORDER BY PeriodoLetivo.sigla";
    // Statement stm = con.createStatement();
    // ResultSet tabelaResultado = stm.executeQuery(sqlStr);
    // return montarDadosConsulta(tabelaResultado);
    // }
    public PeriodoLetivoVO consultarPorGradeCurricularDisciplina(Integer disciplina, Integer gradeCurricular, int nivelMontarDados, UsuarioVO usuario)
            throws Exception;

    /**
     * Responsável por realizar uma consulta de <code>PeriodoLetivo</code> através do valor do atributo <code>String descricao</code>. Retorna os
     * objetos, com início do valor do atributo idêntico ao parâmetro fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o
     * trabalho de prerarar o List resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>PeriodoLetivoVO</code> resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<PeriodoLetivoVO> consultarPorDescricao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    /**
     * Responsável por realizar uma consulta de <code>PeriodoLetivo</code> através do valor do atributo <code>Integer codigo</code>. Retorna os
     * objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de
     * prerarar o List resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>PeriodoLetivoVO</code> resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<PeriodoLetivoVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void excluirPeriodoLetivo(Integer gradeCurricular, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por alterar todos os objetos da <code>CursoTurnoVO</code> contidos em um Hashtable no BD. Faz uso da operação
     * <code>excluirCursoTurnos</code> e <code>incluirCursoTurnos</code> disponíveis na classe <code>CursoTurno</code>.
     *
     * @param objetos
     *            List com os objetos a serem alterados ou incluídos no BD.
     * @exception Exception
     *                Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void alterarPeriodoLetivo(Integer gradeCurricularPrm, List<PeriodoLetivoVO> objetos, String situacaoGradeCurricular, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por incluir objetos da <code>CursoTurnoVO</code> no BD. Garantindo o relacionamento com a entidade principal
     * <code>academico.Curso</code> através do atributo de vínculo.
     *
     * @param objetos
     *            List contendo os objetos a serem gravados no BD da classe.
     * @exception Exception
     *                Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void incluirPeriodoLetivo(Integer gradeCurricularPrm, List<PeriodoLetivoVO> objetos, String situacaoGradeCurricular, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por localizar um objeto da classe <code>PeriodoLetivoVO</code> através de sua chave primária.
     *
     * @exception Exception
     *                Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public PeriodoLetivoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public PeriodoLetivoVO consultarProximoPeriodoLetivoCurso(Integer gradeCurricular, Integer curso, Integer periodoLetivo, boolean controlarAcesso,
            int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<PeriodoLetivoVO> consultarPorMatricula(String matricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser possível, pois, uma mesma classe de
     * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste
     * identificador,
     */
    public void setIdEntidade(String idEntidade);

    public List<PeriodoLetivoVO> consultarPeriodoLetivos(Integer siglaPrm, boolean b, int nivelmontardadosDadosbasicos, UsuarioVO usuario) throws Exception;

    public void excluirObjGradeDisciplinaVOs(GradeDisciplinaVO obj, PeriodoLetivoVO periodoLetivoVO, Boolean validarAlteracaoMatrizCurricularAtivaInativa, UsuarioVO usuario) throws Exception;

    public PeriodoLetivoVO consultarPorPeriodoLetivoGradeCurricular(Integer periodoLetivo, Integer gradeCurricular, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public PeriodoLetivoVO consultarUltimoPeriodoLetivoGradeCurricular(Integer gradeCurricular, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<PeriodoLetivoVO> consultarPorTurma(TurmaVO turma, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<PeriodoLetivoVO> consultarPorMatriculaCurso(String matricula, Integer curso, String periodicidadeCurso, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<PeriodoLetivoVO> consultarPorMatriculaPorMatriculaPeriodoSituacaoDiferenteAtiva(String matricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<PeriodoLetivoVO> consultarPorMatriculaPorMatriculaPeriodo(String matricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<PeriodoLetivoVO> consultarPorCursoGradeCurricularAtiva(Integer codigoCurso, Integer gradecurricular, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<PeriodoLetivoVO> consultarPorMatriculaPeriodoLetivo(String matricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<PeriodoLetivoVO> consultarTodosPeriodosPorMatricula(String matricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<PeriodoLetivoVO> consultarPorGradeCurricular(Integer gradeCurricular, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public PeriodoLetivoVO consultarPorAnoSemestreMatriculaPeriodoEMatricula(String matricula, String ano, String semestre, String periodicidadeCurso, UsuarioVO usuarioVO);
	
	public void removerGradeDisciplinaForaGradeInclusaoExclusaoDisciplinaVOs(GradeDisciplinaVO grade, List<PeriodoLetivoVO> listaPeriodoLetivoVOs, UsuarioVO usuario) throws Exception;
	
	public Integer consultarQuantidadePeriodoLetivoACursar(String matricula, UsuarioVO usuarioVO) throws Exception;
	
	public Integer consultarPeriodoLetivoAnoBaseIngressoAluno(String matricula, String anoIngressoAluno, UsuarioVO usuarioVO);
	
	public Integer consultarUltimoPeriodoLetivoPorMatriculaGradeCurricular(String matricula, Integer gradeCurricular, UsuarioVO usuarioVO);
	
	public Integer consultarTotalCargaHorariaPorGradeCurricularPeriodoLetivo(Integer periodoLetivo, Integer gradeCurricular, UsuarioVO usuarioVO);
	
	public PeriodoLetivoVO consultarPeriodoLetivoAtualPorMatriculaGradeCurricular(String matricula, Integer gradeCurricular, UsuarioVO usuarioVO);

	/**
	 * @author Wellington Rodrigues - 25/05/2015
	 * @param codigoCurso
	 * @param gradecurricular
	 * @param controlarAcesso
	 * @param nivelMontarDados
	 * @param usuario
	 * @return
	 * @throws Exception
	 */
	List<PeriodoLetivoVO> consultarPorCursoGradeCurricular(Integer codigoCurso, Integer gradecurricular, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public Integer consultarMaiorNumeroPeriodoLetivoPorCurso(Integer codigoCurso, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Responsável por executar a consulta de período letivo com base na
	 * matrícula, levando em consideração a última matrícula período.
	 * 
	 * @author Wellington Rodrigues - 4 de ago de 2015
	 * @param matricula
	 * @param usuarioVO
	 * @return
	 */
	PeriodoLetivoVO consultarPeriodoLetivoAtualPorMatricula(String matricula, UsuarioVO usuarioVO);

	public Integer consultarPeriodoLetivoPorGradeCurricularDisciplina(Integer gradeCurricular, Integer disciplina, Integer mapaEquivalenciaDisciplina, UsuarioVO usuarioVO);

	PeriodoLetivoVO consultarPeriodoLetivoPorGradeCurricularGrupoOptativaDisciplina(Integer gradeCurricularGrupoOptativaDisciplina, UsuarioVO usuarioVO) throws Exception;

	void alterarCargaHorariaENrCredito(Integer periodoLetivo, Integer totalCargaHoraria, Integer totalCreditos, UsuarioVO usuario) throws Exception;
	
	PeriodoLetivoVO consultarPeriodoLetivoIngressoPorMatricula(String matricula, UsuarioVO usuarioVO) throws Exception;

	PeriodoLetivoVO realizarDefinicaoPeriodoLetivoCursarBaseadoDisciplinasAprovadasMatriculasAntigas(PessoaVO aluno,
			Integer gradeCurricular, MatriculaPeriodoVO matriculaPeriodoVO ,
			UsuarioVO usuario) throws Exception;

	PeriodoLetivoRSVO consultarPeriodoLetivoMatriculaOnline(PessoaVO aluno, Integer gradeCurricular,
			MatriculaPeriodoVO matriculaPeriodoVO, 
			UsuarioVO usuarioVO);

}
