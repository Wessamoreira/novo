package negocio.interfaces.crm;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.crm.SituacaoProspectWorkflowVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
*/
public interface SituacaoProspectWorkflowInterfaceFacade {
	

    public void validarDados(SituacaoProspectWorkflowVO obj) throws Exception;
    public SituacaoProspectWorkflowVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario ) throws Exception;
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario ) throws Exception;
    public List consultarPorNomeWorkflow(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario ) throws Exception;
    public List consultarPorNomeSituacaoProspectPipeline(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario ) throws Exception;
    public Boolean verificarExisteSituacao(List<SituacaoProspectWorkflowVO> listaSituacaoProspectWorkflowVO, SituacaoProspectWorkflowVO objSituacaoProspectWorkflowVO);
    public void setIdEntidade(String aIdEntidade);
    public void incluirSituacaoProspectWorkflows(Integer workflowPrm, List objetos, UsuarioVO usuario) throws Exception;
    public void alterarSituacaoProspectWorkflows(Integer workflow, List objetos, UsuarioVO usuario) throws Exception;
    public void excluirSituacaoProspectWorkflows(Integer workflow, UsuarioVO usuario) throws Exception;
}