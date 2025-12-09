/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.interfaces.crm;
import java.util.List;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.crm.EtapaWorkflowVO;
import negocio.comuns.crm.SituacaoProspectWorkflowVO;
import negocio.comuns.crm.WorkflowVO;
import negocio.comuns.crm.enumerador.TipoSituacaoWorkflowEnum;

/**
 *
 * @author MarcoTulio
 */
public interface WorkflowInterfaceFacade {
    
     public void persistir(WorkflowVO obj,UsuarioVO usuarioLogado, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;
    public void excluir(WorkflowVO obj, UsuarioVO usuarioLogado, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;
     public List<WorkflowVO> consultar(String valorConsulta, String campoConsulta, TipoSituacaoWorkflowEnum tipoSituacaoWorkflowEnum, boolean controlarAcesso,UsuarioVO usuario) throws Exception;
    public void validarDados(WorkflowVO obj) throws Exception;
    public WorkflowVO consultarPorChavePrimaria( Integer codigoPrm, int nivelMontarDados,UsuarioVO usuario ) throws Exception;
    public List consultarPorCodigo(Integer valorConsulta, TipoSituacaoWorkflowEnum tipoSituacaoWorkflowEnum,boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorNome(String valorConsulta,TipoSituacaoWorkflowEnum tipoSituacaoWorkflowEnum, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorNomePorSituacao(String valorConsulta, TipoSituacaoWorkflowEnum tipoSituacaoWorkflowEnum ,boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public void setIdEntidade(String aIdEntidade);
    public void ordenarListaSituacaoProspectWorkflow(List<SituacaoProspectWorkflowVO> lista);
    public void mudarPosicaoListaSituacaoProspectWorkflow(List<SituacaoProspectWorkflowVO> lista, SituacaoProspectWorkflowVO obj, Boolean direcao);
    public void adicionarObjEtapaWorkflowVOs(WorkflowVO objWorkflowVO, EtapaWorkflowVO obj, UsuarioVO usuario) throws Exception;
    public void excluirObjEtapaWorkflowVOs(WorkflowVO objWorkflowVO, String nome) throws Exception;
    public EtapaWorkflowVO consultarObjEtapaWorkflowVO(WorkflowVO objWorkflowVO, String nome) throws Exception;

    public void adicionarObjSituacaoProspectWorkflowVOs(WorkflowVO objWorkflowVO, SituacaoProspectWorkflowVO obj) throws Exception;
    public void excluirObjSituacaoProspectWorkflowVOs(WorkflowVO objWorkflowVO, Integer situacaoProspectPipeline) throws Exception;
    public SituacaoProspectWorkflowVO consultarObjSituacaoProspectWorkflowVO(WorkflowVO objWorkflowVO, Integer situacaoProspectPipeline) throws Exception;
}
