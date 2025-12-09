/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.interfaces.basico;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.bancocurriculum.AreaProfissionalVO;
import negocio.comuns.basico.AreaProfissionalInteresseContratacaoVO;
import negocio.comuns.basico.PessoaVO;

/**
 *
 * @author Rogerio
 */
public interface AreaProfissionalInteresseContratacaoInterfaceFacade {

    /**
     * Operação responsável por retornar um novo objeto da classe <code>AreaProfissionalInteresseContratacaoVO</code>.
     */
    public AreaProfissionalInteresseContratacaoVO novo() throws Exception;

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>AreaProfissionalInteresseContratacaoVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>AreaProfissionalInteresseContratacaoVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    public void incluir(AreaProfissionalInteresseContratacaoVO obj, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>AreaProfissionalInteresseContratacaoVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>AreaProfissionalInteresseContratacaoVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    public void alterar(AreaProfissionalInteresseContratacaoVO obj, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>AreaProfissionalInteresseContratacaoVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>AreaProfissionalInteresseContratacaoVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public void excluir(AreaProfissionalInteresseContratacaoVO obj, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por excluir todos os objetos da <code>AreaProfissionalInteresseContratacaoVO</code> no BD.
     * Faz uso da operação <code>excluir</code> disponível na classe <code>AreaProfissionalInteresseContratacao</code>.
     * @param <code>pessoa</code> campo chave para exclusão dos objetos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void excluirAreaProfissionalInteresseContratacao(Integer pessoa, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por alterar todos os objetos da <code>AreaProfissionalInteresseContratacaoVO</code> contidos em um Hashtable no BD.
     * Faz uso da operação <code>excluirAreaProfissionalInteresseContratacao</code> e <code>incluirAreaProfissionalInteresseContratacao</code> disponíveis na classe <code>AreaProfissionalInteresseContratacao</code>.
     * @param objetos  List com os objetos a serem alterados ou incluídos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void alterarAreaProfissionalInteresseContratacao(Integer pessoa, List objetos, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por incluir objetos da <code>AreaProfissionalInteresseContratacaoVO</code> no BD.
     * Garantindo o relacionamento com a entidade principal <code>basico.Pessoa</code> através do atributo de vínculo.
     * @param objetos List contendo os objetos a serem gravados no BD da classe.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void incluirAreaProfissionalInteresseContratacao(Integer pessoaPrm, List objetos, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por localizar um objeto da classe <code>AreaProfissionalInteresseContratacaoVO</code>
     * através de sua chave primária.
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public AreaProfissionalInteresseContratacaoVO consultarPorChavePrimaria(Integer pessoaPrm, Integer disciplinaPrm, UsuarioVO usuario) throws Exception;

    public List consultarAreaProfissionalInteresseContratacaoPorCodigoPessoa(Integer pessoa, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade);

    public void alterarAreaProfissionalInteresseContratacao(Integer pessoa, List objetos, boolean verificarAcesso, UsuarioVO usuario) throws Exception;

    public void adicionarObjAreaProfissionalVOs(AreaProfissionalVO obj, PessoaVO pessoa) throws Exception;

    public void excluirObjAreaProfissionalVOs(AreaProfissionalInteresseContratacaoVO obj, PessoaVO pessoa) throws Exception;

    public void excluirAreaProfissionalInteresseContratacao(Integer pessoa, List objetos, boolean verificarAcesso) throws Exception;
 
}
