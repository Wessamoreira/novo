/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.interfaces.administrativo;

import java.util.List;

import negocio.comuns.administrativo.FormacaoExtraCurricularVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;

public interface FormacaoExtraCurricularInterfaceFacade {

    /**
     * Operação responsável por retornar um novo objeto da classe <code>FormacaoExtraCurricularVO</code>.
     */
    public FormacaoExtraCurricularVO novo() throws Exception;

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>FormacaoExtraCurricularVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>FormacaoExtraCurricularVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    public void incluir(FormacaoExtraCurricularVO obj) throws Exception;

    public void incluir(FormacaoExtraCurricularVO obj, boolean verificarAcesso) throws Exception;

    public void alterar(FormacaoExtraCurricularVO obj) throws Exception;

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>FormacaoExtraCurricularVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>FormacaoExtraCurricularVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    public void alterar(FormacaoExtraCurricularVO obj, boolean verificarAcesso) throws Exception;

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>FormacaoExtraCurricularVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>FormacaoExtraCurricularVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public void excluir(FormacaoExtraCurricularVO obj) throws Exception;

    /**
     * Responsável por realizar uma consulta de <code>FormacaoExtraCurricular</code> através do valor do atributo
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>FormacaoExtraCurricularVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por excluir todos os objetos da <code>FormacaoExtraCurricularVO</code> no BD.
     * Faz uso da operação <code>excluir</code> disponível na classe <code>FormacaoExtraCurricular</code>.
     * @param <code>pessoa</code> campo chave para exclusão dos objetos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void excluirFormacaoExtraCurricular(PessoaVO pessoa, List objetos) throws Exception;

    public void excluirFormacaoExtraCurricular(PessoaVO pessoa, List objetos, boolean verificarAcesso) throws Exception;

    /**
     * Operação responsável por alterar todos os objetos da <code>FormacaoExtraCurricularVO</code> contidos em um Hashtable no BD.
     * Faz uso da operação <code>excluirFormacaoExtraCurricular</code> e <code>incluirFormacaoExtraCurricular</code> disponíveis na classe <code>FormacaoExtraCurricular</code>.
     * @param objetos  List com os objetos a serem alterados ou incluídos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void alterarFormacaoExtraCurricular(PessoaVO pessoa, List objetos) throws Exception;

    public void alterarFormacaoExtraCurricular(PessoaVO pessoa, List objetos, boolean verificarAcesso) throws Exception;

    /**
     * Operação responsável por incluir objetos da <code>FormacaoExtraCurricularVO</code> no BD.
     * Garantindo o relacionamento com a entidade principal <code>basico.Pessoa</code> através do atributo de vínculo.
     * @param objetos List contendo os objetos a serem gravados no BD da classe.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void incluirFormacaoExtraCurricular(PessoaVO pessoaPrm, List objetos) throws Exception;

    public void incluirFormacaoExtraCurricular(PessoaVO pessoaPrm, List objetos, boolean verificarAcesso) throws Exception;

    /**
     * Operação responsável por localizar um objeto da classe <code>FormacaoExtraCurricularVO</code>
     * através de sua chave primária.
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public FormacaoExtraCurricularVO consultarPorChavePrimaria(Integer codigoPrm, UsuarioVO usuario) throws Exception;

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade);

    public void adicionarObjFormacaoExtraCurricularVOs(FormacaoExtraCurricularVO obj, PessoaVO pessoa) throws Exception;

    public void excluirObjFormacaoExtraCurricularVOs(FormacaoExtraCurricularVO obj, PessoaVO pessoa) throws Exception;

    public FormacaoExtraCurricularVO consultarEmpregoAtualPorCodigoPessoa(Integer pessoa, UsuarioVO usuario) throws Exception;

    public List consultarPorCodigoPessoaOrdemNovaAntiga(Integer pessoa, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
}
