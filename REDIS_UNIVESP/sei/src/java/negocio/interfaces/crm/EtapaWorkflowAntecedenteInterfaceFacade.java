/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.interfaces.crm;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.crm.EtapaWorkflowAntecedenteVO;
import negocio.comuns.crm.EtapaWorkflowVO;
import negocio.comuns.crm.WorkflowVO;

/**
 *
 * @author PEDRO
 */
public interface EtapaWorkflowAntecedenteInterfaceFacade {

    /**
     * Operação responsável por alterar todos os objetos da <code>EtapaWorkflowVO</code> contidos em um Hashtable no BD.
     * Faz uso da operação <code>excluirEtapaWorkflows</code> e <code>incluirEtapaWorkflows</code> disponíveis na classe <code>EtapaWorkflow</code>.
     * @param objetos  List com os objetos a serem alterados ou incluídos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    void alterarEtapaWorkflowsAntecedente(Integer etapaMae, List objetos, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>EtapaWorkflowVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>EtapaWorkflowVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    void excluir(EtapaWorkflowAntecedenteVO obj, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por incluir objetos da <code>EtapaWorkflowVO</code> no BD.
     * Garantindo o relacionamento com a entidade principal <code>CRM.Workflow</code> através do atributo de vínculo.
     * @param objetos List contendo os objetos a serem gravados no BD da classe.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    void incluirEtapaWorkflowsAntecedente(Integer etapaMae, List objetos, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por validar os dados de um objeto da classe <code>EtapaWorkflowVO</code>.
     * Todos os tipos de consistência de dados são e devem ser implementadas neste método.
     * São validações típicas: verificação de campos obrigatórios, verificação de valores válidos para os atributos.
     * @exception ConsistirExecption Se uma inconsistência for encontrada aumaticamente é gerada uma exceção descrevendo
     * o atributo e o erro ocorrido.
     */
    void validarDados(EtapaWorkflowAntecedenteVO obj) throws Exception;

    public void realizarAtualizacaoEtapaWorkflowAntecedentes(WorkflowVO workflow, EtapaWorkflowVO etapa);

    public void preencherEtapaWorkflowAntecedenteSemReferenciaMemoria(EtapaWorkflowAntecedenteVO objDestino, EtapaWorkflowAntecedenteVO objOrigem) throws Exception;
}
