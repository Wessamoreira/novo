package negocio.interfaces.academico;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.GradeCurricularEstagioVO;
import negocio.comuns.academico.GradeCurricularGrupoOptativaVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.GradeDisciplinaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import webservice.servicos.GradeDisciplinaObject;

public interface GradeCurricularInterfaceFacade {

    /**
     * Operação responsável por retornar um novo objeto da classe <code>GradeCurricularVO</code>.
     */
    public GradeCurricularVO novo() throws Exception;

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>GradeCurricularVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>GradeCurricularVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    public void incluir(final GradeCurricularVO obj, CursoVO cursoVO, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>GradeCurricularVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>GradeCurricularVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    public void alterar(final GradeCurricularVO obj, CursoVO cursoVO, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>GradeCurricularVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>GradeCurricularVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public void excluir(GradeCurricularVO obj, UsuarioVO usuario) throws Exception;
    
    Integer consultarCreditoExigidoGrade(Integer gradeCurricular, UsuarioVO usuario) throws Exception;

    public Integer consultarCargaHorariaExigidaGrade(Integer gradeCurricular, UsuarioVO usuario) throws Exception;

    public List consultarPorCodigoCurso(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public GradeCurricularVO consultarPorSituacaoGradeCurso(Integer valorConsulta, String situacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorCurso(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    /**
     * Responsável por realizar uma consulta de <code>GradeCurricular</code> através do valor do atributo
     * <code>Date dataCadastro</code>. Retorna os objetos com valores pertecentes ao período informado por parâmetro.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>GradeCurricularVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorDataCadastro(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    /**
     * Responsável por realizar uma consulta de <code>GradeCurricular</code> através do valor do atributo
     * <code>String nome</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>GradeCurricularVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    /**
     * Responsável por realizar uma consulta de <code>GradeCurricular</code> através do valor do atributo
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>GradeCurricularVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<GradeCurricularVO> consultarPorMatriculaAluno(String matricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por excluir todos os objetos da <code>GradeCurricularVO</code> no BD.
     * Faz uso da operação <code>excluir</code> disponível na classe <code>GradeCurricular</code>.
     * @param <code>curso</code> campo chave para exclusão dos objetos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void excluirGradeCurriculars(GradeCurricularVO obj, UsuarioVO usuario) throws Exception;

//  /**
//  * Operação responsável por alterar todos os objetos da <code>GradeCurricularVO</code> contidos em um Hashtable no BD.
//  * Faz uso da operação <code>excluirGradeCurriculars</code> e <code>incluirGradeCurriculars</code> disponíveis na classe <code>GradeCurricular</code>.
//  * @param objetos  List com os objetos a serem alterados ou incluídos no BD.
//  * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
//  */
// public void alterarGradeCurriculars(CursoVO curso, List objetos, UsuarioVO usuario) throws Exception;
//
// /**
//  * Operação responsável por incluir objetos da <code>GradeCurricularVO</code> no BD.
//  * Garantindo o relacionamento com a entidade principal <code>academico.Curso</code> através do atributo de vínculo.
//  * @param objetos List contendo os objetos a serem gravados no BD da classe.
//  * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
//  */
// public void incluirGradeCurriculars(CursoVO cursoPrm, List<GradeCurricularVO> objetos, UsuarioVO usuario) throws Exception;
 
    void persistir(GradeCurricularVO obj, CursoVO cursoVO, Boolean validarAlteracaoMatrizCurricularAtivaInativa, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por localizar um objeto da classe <code>GradeCurricularVO</code>
     * através de sua chave primária.
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public GradeCurricularVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade);

    public void ativarGrade(GradeCurricularVO obj, UsuarioVO usuario) throws Exception;

    public void desativarGrade(GradeCurricularVO obj, UsuarioVO usuario) throws Exception;

    public List<GradeCurricularVO> consultarGradeCurriculars(Integer curso, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorSituacaoGradeCursoLista(Integer codigo, String string, boolean b, int nivelmontardadosDadosbasicos, UsuarioVO usuario) throws Exception;

    public GradeCurricularVO consultarGradeCurricularAtualMatricula(String matricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    
    public GradeCurricularVO consultarGradeCurricularDaUltimaMatriculaPeriodo(String matricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void validarDados(GradeCurricularVO obj, CursoVO cursoVO, UsuarioVO usuario) throws Exception;

    public void excluirPeriodosLetivosVOs(GradeCurricularVO obj, UsuarioVO usuario) throws Exception;

    List<GradeCurricularVO> consultarGradeCurricularAtivaPorCodigoCurso(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public GradeCurricularVO consultarGradeCurricularPorMatriculaPeriodo(Integer matriculaPeriodo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    
    public List<GradeCurricularVO> consultarGradeAtualGradeAntigaPorMatriculaAluno(String matricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    
    public Boolean consultarGradeCurricularUltimaMatriculaPeriodo(String matricula, Integer gradeCurricular, UsuarioVO usuario);
    
    public GradeCurricularVO consultarPorTurmaNivelComboBox(Integer turma, UsuarioVO usuarioVO);

	List<GradeCurricularVO> consultarPorCodigoCursoCodigoDisciplina(Integer curso, Integer disciplina, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public List<GradeCurricularVO> consultarGradeCurricularAnteriorPorMatricula(String matricula, UsuarioVO usuarioVO);
	
	SqlRowSet consultarDadosGeracaoRelatorioDisciplinasGradeRel(Integer gradeCurricular, Integer unidadeEnsinoCurso, Integer curso, Integer turma, UsuarioVO usuarioVO) throws Exception;

	SqlRowSet consultarDadosGeracaoRelatorioDisciplinasGradeDisciplinasRel(Integer gradeCurricular, Integer unidadeEnsinoCurso, Integer curso, Integer turma, Boolean apresentarDisciplinaComposta, UsuarioVO usuarioVO) throws Exception;

	void adicionarGradeCurricularGrupoOptativaVO(GradeCurricularVO gradeCurricularVO, GradeCurricularGrupoOptativaVO gradeCurricularGrupoOptativaVO) throws Exception;

	void removerGradeCurricularGrupoOptativaVO(GradeCurricularVO gradeCurricularVO, GradeCurricularGrupoOptativaVO gradeCurricularGrupoOptativaVO) throws Exception;

	Integer consultarCargaHorariaExigidaGradePeriodoLetivos(Integer gradeCurricular, UsuarioVO usuario) throws Exception;

	Integer consultarCargaHorariaTCCPorMatrizCurricular(Integer matrizCurricular) throws Exception;
	
	boolean consultarDisciplinaClassificadoComoTCC(Integer matrizCurricular) throws Exception;
	
	Integer consultarCargaHorariaObrigatoriaEstagioPorMatrizCurricular(Integer matrizCurricular) throws Exception;
        
    HashMap<Integer, Integer> consultarListaCodigoDisciplinaCodigoPeriodoLetivoMatrizCurricular(Integer matrizCurricular) throws Exception;

	void realizarVerificacaoDisciplinaJaAdicionada(GradeCurricularVO gradeCurricularVO, DisciplinaVO disciplinaVO) throws Exception;

	List<GradeCurricularVO> consultarPorMatriculaGradeCurricularVOsVinculadaHistoricoInclusaoExclusaoDisciplina(String matricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	List<GradeCurricularVO> consultarGradeCurricularAtualFiltrarRenovacaoTurmaNivelCombobox(Integer unidadeEnsino, Integer curso, Integer turma, String ano, String semestre) throws Exception;
		
	Integer consultarTotalCargaHorariaGradeCurricularPorCodigo(Integer gradeCurricular, UsuarioVO usuarioVO);

	GradeDisciplinaObject consultarGradeDisciplinaCursoMatriculaOnlineExterna(Integer codigoCurso);
	
	void realizarMongagemDadosEmGradeCurricularExistente(GradeCurricularVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	void persistirAtivacaoGradeCurricular(GradeCurricularVO obj, UsuarioVO usuario) throws Exception;
	
	void persistirDesativacaoGradeCurricular(GradeCurricularVO obj, UsuarioVO usuario) throws Exception;

	List<GradeCurricularVO> consultarPorCodigoCursoPossuemAtividadeComplementar(Integer valorConsulta, UsuarioVO usuario) throws Exception;

	Integer consultarTotalDisciplinaObrigatoria(Integer gradeCurricular, boolean desconsiderarDisciplinaEstagioObrigatorio);
	
	Integer consultarTotalDisciplinaEstagio(Integer gradeCurricular);

	void adicionarGradeCurricularEstagio(GradeCurricularVO gradeCurricularVO, GradeCurricularEstagioVO gre, UsuarioVO usuarioLogado) throws Exception;

	void removerGradeCurricularEstagio(GradeCurricularVO gradeCurricularVO, GradeCurricularEstagioVO gre, UsuarioVO usuarioLogado);

	public  GradeCurricularVO consultarGradeCurricularDataAtivacaoAtualPorSituacaoGradeCurso(Integer unidadeEnsino, Integer curso , Integer turno, Integer periodoLetivo, 	String situacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    	String consultarPrazoJubilamento(String matricula);

		GradeCurricularVO consultarPorChavePrimariaUnica(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario)
				throws Exception;
}
