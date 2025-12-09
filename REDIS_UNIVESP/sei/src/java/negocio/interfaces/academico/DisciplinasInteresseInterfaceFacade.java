package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.DisciplinasInteresseVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface DisciplinasInteresseInterfaceFacade {

    /**
     * Operação responsável por retornar um novo objeto da classe <code>DisciplinasInteresseVO</code>.
     */
    public DisciplinasInteresseVO novo() throws Exception;

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>DisciplinasInteresseVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>DisciplinasInteresseVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    public void incluir(DisciplinasInteresseVO obj, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>DisciplinasInteresseVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>DisciplinasInteresseVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    public void alterar(DisciplinasInteresseVO obj, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>DisciplinasInteresseVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>DisciplinasInteresseVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public void excluir(DisciplinasInteresseVO obj, UsuarioVO usuario) throws Exception;

    /**
     * Responsável por realizar uma consulta de <code>DisciplinasInteresse</code> através do valor do atributo
     * <code>nome</code> da classe <code>Disciplina</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>DisciplinasInteresseVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNomeDisciplina(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    /**
     * Responsável por realizar uma consulta de <code>DisciplinasInteresse</code> através do valor do atributo
     * <code>nome</code> da classe <code>Pessoa</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>DisciplinasInteresseVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNomePessoa(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por excluir todos os objetos da <code>DisciplinasInteresseVO</code> no BD.
     * Faz uso da operação <code>excluir</code> disponível na classe <code>DisciplinasInteresse</code>.
     * @param <code>professor</code> campo chave para exclusão dos objetos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void excluirDisciplinasInteresses(Integer professor, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por alterar todos os objetos da <code>DisciplinasInteresseVO</code> contidos em um Hashtable no BD.
     * Faz uso da operação <code>excluirDisciplinasInteresses</code> e <code>incluirDisciplinasInteresses</code> disponíveis na classe <code>DisciplinasInteresse</code>.
     * @param objetos  List com os objetos a serem alterados ou incluídos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void alterarDisciplinasInteresses(Integer professor, List objetos, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por incluir objetos da <code>DisciplinasInteresseVO</code> no BD.
     * Garantindo o relacionamento com a entidade principal <code>basico.Pessoa</code> através do atributo de vínculo.
     * @param objetos List contendo os objetos a serem gravados no BD da classe.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void incluirDisciplinasInteresses(Integer professorPrm, List objetos, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por localizar um objeto da classe <code>DisciplinasInteresseVO</code>
     * através de sua chave primária.
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public DisciplinasInteresseVO consultarPorChavePrimaria(Integer professorPrm, Integer disciplinaPrm, UsuarioVO usuario) throws Exception;

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade);

    public void alterarDisciplinasInteresses(Integer professor, List objetos, boolean verificarAcesso, UsuarioVO usuario) throws Exception;

    public Boolean consultarExistenciaDisciplinaComProfessor(Integer disciplina, Integer professor);

    public List consultarDisciplinasInteressesPorCodigoDisciplina(Integer disciplina, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
}
