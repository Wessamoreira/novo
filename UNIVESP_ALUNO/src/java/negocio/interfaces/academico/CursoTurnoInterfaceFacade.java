package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.CursoTurnoVO;
import negocio.comuns.academico.ProcessoMatriculaUnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface CursoTurnoInterfaceFacade {

    /**
     * Operação responsável por retornar um novo objeto da classe <code>CursoTurnoVO</code>.
     */
    public CursoTurnoVO novo() throws Exception;

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>CursoTurnoVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>CursoTurnoVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    public void incluir(CursoTurnoVO obj, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>CursoTurnoVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>CursoTurnoVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    public void alterar(CursoTurnoVO obj, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>CursoTurnoVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>CursoTurnoVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public void excluir(CursoTurnoVO obj, UsuarioVO usuario) throws Exception;

    /**
     * Responsável por realizar uma consulta de <code>CursoTurno</code> através do valor do atributo
     * <code>nome</code> da classe <code>Turno</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>CursoTurnoVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNomeTurno(String valorConsulta, boolean controleAcesso, UsuarioVO usuario) throws Exception;

    /**
     * Responsável por realizar uma consulta de <code>CursoTurno</code> através do valor do atributo
     * <code>nome</code> da classe <code>Curso</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>CursoTurnoVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNomeCurso(String valorConsulta, boolean controleAcesso, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por excluir todos os objetos da <code>CursoTurnoVO</code> no BD.
     * Faz uso da operação <code>excluir</code> disponível na classe <code>CursoTurno</code>.
     * @param <code>curso</code> campo chave para exclusão dos objetos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void excluirCursoTurnos(Integer curso, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por alterar todos os objetos da <code>CursoTurnoVO</code> contidos em um Hashtable no BD.
     * Faz uso da operação <code>excluirCursoTurnos</code> e <code>incluirCursoTurnos</code> disponíveis na classe <code>CursoTurno</code>.
     * @param objetos  List com os objetos a serem alterados ou incluídos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void alterarCursoTurnos(Integer curso, List objetos, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por incluir objetos da <code>CursoTurnoVO</code> no BD.
     * Garantindo o relacionamento com a entidade principal <code>academico.Curso</code> através do atributo de vínculo.
     * @param objetos List contendo os objetos a serem gravados no BD da classe.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void incluirCursoTurnos(Integer cursoPrm, List objetos, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por localizar um objeto da classe <code>CursoTurnoVO</code>
     * através de sua chave primária.
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public CursoTurnoVO consultarPorChavePrimaria(Integer cursoPrm, Integer turnoPrm, boolean controleAcesso, UsuarioVO usuario) throws Exception;

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade);

    public List consultarCursoTurnos(Integer curso, boolean controleAcesso, UsuarioVO usuario) throws Exception;

	List<CursoTurnoVO> consultarCursoTurnoProcessoMatricula(String campoConsulta, String valorConsulta,
			String nivelEducacional, List<ProcessoMatriculaUnidadeEnsinoVO> processoMatriculaUnidadeEnsinoVOs,
			Integer unidadeEnsinoLogado) throws Exception;
}
