package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.DisciplinaEquivalenteVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface DisciplinaEquivalenteInterfaceFacade {

    /**
     * Operação responsável por retornar um novo objeto da classe <code>DisciplinaEquivalenteVO</code>.
     */
    public DisciplinaEquivalenteVO novo() throws Exception;

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>DisciplinaEquivalenteVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>DisciplinaEquivalenteVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    public void incluir(DisciplinaEquivalenteVO obj, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>DisciplinaEquivalenteVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>DisciplinaEquivalenteVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    public void alterar(DisciplinaEquivalenteVO obj, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>DisciplinaEquivalenteVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>DisciplinaEquivalenteVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public void excluir(DisciplinaEquivalenteVO obj, UsuarioVO usuario) throws Exception;

    /**
     * Responsável por realizar uma consulta de <code>DisciplinaEquivalente</code> através do valor do atributo
     * <code>nome</code> da classe <code>Disciplina</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>DisciplinaEquivalenteVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNomeDisciplina(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por excluir todos os objetos da <code>DisciplinaEquivalenteVO</code> no BD.
     * Faz uso da operação <code>excluir</code> disponível na classe <code>DisciplinaEquivalente</code>.
     * @param <code>disciplina</code> campo chave para exclusão dos objetos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void excluirDisciplinaEquivalentes(Integer disciplina, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por alterar todos os objetos da <code>DisciplinaEquivalenteVO</code> contidos em um Hashtable no BD.
     * Faz uso da operação <code>excluirDisciplinaEquivalentes</code> e <code>incluirDisciplinaEquivalentes</code> disponíveis na classe <code>DisciplinaEquivalente</code>.
     * @param objetos  List com os objetos a serem alterados ou incluídos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void alterarDisciplinaEquivalentes(Integer disciplina, List objetos, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por incluir objetos da <code>DisciplinaEquivalenteVO</code> no BD.
     * Garantindo o relacionamento com a entidade principal <code>academico.Disciplina</code> através do atributo de vínculo.
     * @param objetos List contendo os objetos a serem gravados no BD da classe.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void incluirDisciplinaEquivalentes(Integer disciplinaPrm, List objetos, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por localizar um objeto da classe <code>DisciplinaEquivalenteVO</code>
     * através de sua chave primária.
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public DisciplinaEquivalenteVO consultarPorChavePrimaria(Integer disciplinaPrm, Integer equivalentePrm, UsuarioVO usuario) throws Exception;

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade);

    public List consultarDisciplinaEquivalentes(Integer disciplina, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
    
    public DisciplinaEquivalenteVO consultarDisciplinaEquivalentePorDisciplinaEEquivalente(Integer disciplina, Integer equivalente, UsuarioVO usuarioVO);

	void adicionarDisciplinaEquivalente(DisciplinaVO disciplinaVO, DisciplinaVO disciplinaEquivalenteVO, UsuarioVO usuarioVO) throws Exception;
}
