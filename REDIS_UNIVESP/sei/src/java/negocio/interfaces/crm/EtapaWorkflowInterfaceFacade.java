package negocio.interfaces.crm;

import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.crm.ArquivoEtapaWorkflowVO;
import negocio.comuns.crm.CursoEtapaWorkflowVO;
import negocio.comuns.crm.EtapaWorkflowAntecedenteVO;
import negocio.comuns.crm.EtapaWorkflowVO;
import negocio.comuns.crm.InteracaoWorkflowVO;
import negocio.comuns.crm.WorkflowVO;
import negocio.comuns.utilitarias.ConsistirException;

/**
 *
 * @author MarcoTulio
 */
public interface EtapaWorkflowInterfaceFacade {

    public void incluir(final EtapaWorkflowVO obj, UsuarioVO usuarioLogado, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

    public void alterar(final EtapaWorkflowVO obj, UsuarioVO usuarioLogado, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

    public void excluir(EtapaWorkflowVO obj, UsuarioVO usuarioLogado, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

    public void validarDados(EtapaWorkflowVO obj) throws Exception;

    public void validarUnicidade(List<EtapaWorkflowVO> lista, EtapaWorkflowVO obj) throws ConsistirException;

    public void realizarUpperCaseDados(EtapaWorkflowVO obj);

    public List<EtapaWorkflowVO> consultar(String valorConsulta, String campoConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void excluirEtapaWorkflows(Integer workflow, UsuarioVO usuario) throws Exception;

    public void alterarEtapaWorkflows(Integer workflow, List objetos, UsuarioVO usuarioLogado, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

    public void incluirEtapaWorkflows(Integer workflowPrm, List objetos, UsuarioVO usuarioLogado, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

    public EtapaWorkflowVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void alterarScriptCorEtapa(final EtapaWorkflowVO obj, UsuarioVO usuarioLogado, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

    public List<EtapaWorkflowVO> consultarUnicidade(EtapaWorkflowVO obj, boolean alteracao, UsuarioVO usuario) throws Exception;

    public List consultarEtapasAntecedentes(Integer codigoEtapaAtual,boolean controlarAcesso, int nivelMontarDados, Boolean utilizarRecusividade, UsuarioVO usuario) throws Exception;

    public void adicionarObjCursoEtapaWorkflowVOs(EtapaWorkflowVO objWorkflowVO, CursoEtapaWorkflowVO obj) throws Exception;

    public void excluirObjCursoEtapaWorkflowVOs(EtapaWorkflowVO objWorkflowVO, Integer curso) throws Exception;

    public CursoEtapaWorkflowVO consultarObjCursoEtapaWorkflowVO(EtapaWorkflowVO objWorkflowVO, Integer curso) throws Exception;

    public void adicionarObjArquivoEtapaWorkflowVOs(EtapaWorkflowVO objWorkflowVO, ArquivoEtapaWorkflowVO obj) throws Exception;

    public void excluirObjArquivoEtapaWorkflowVOs(EtapaWorkflowVO objWorkflowVO, String nome) throws Exception;

    public void preencherEtapaWorkflowSemReferenciaMemoria(EtapaWorkflowVO objDestino, EtapaWorkflowVO objOrigem);

    public boolean adicionarCursos(List<CursoEtapaWorkflowVO> lista, Integer codigo);

    public boolean adicionarArquivos(List<ArquivoEtapaWorkflowVO> lista, Integer codigo);

    public boolean adicionarEtapaWorkflowAntecedente(List<EtapaWorkflowAntecedenteVO> lista, Integer codigo);

    public void montarCursosEtapaWorkflowInteracaoWorkflow(SqlRowSet dadosSQL, InteracaoWorkflowVO obj);

    public void montarEtapasAntecedentesInteracaoWorkflow(SqlRowSet dadosSQL, InteracaoWorkflowVO obj);

    public void montarArquivosEtapaWorkflowInteracaoWorkflow(SqlRowSet dadosSQL, InteracaoWorkflowVO obj);

    public ArquivoEtapaWorkflowVO consultarObjArquivoEtapaWorkflowVO(EtapaWorkflowVO objWorkflowVO, String nome) throws Exception;
    
    public EtapaWorkflowVO consultarPorCodigoWorkFlowEtapaInicial(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    
    public EtapaWorkflowVO consultarPorCodigoCampanhaEtapaInicial(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void excluirObjEtapaWorkflowVOs(EtapaWorkflowVO objEtapaWorkflow, String nome) throws Exception;

    public void adicionarObjEtapaWorkflowSubordinadaVOs(EtapaWorkflowVO obj, EtapaWorkflowAntecedenteVO etapaWorkflowAntecedenteVO) throws Exception;

    public void atualizarCodigoSituacaoProspectWorkflow(WorkflowVO obj);
    
    public void carregarArquivosEtapaWorkflowVO(EtapaWorkflowVO objEtapaWorkflowVO, Boolean controlarAcesso, UsuarioVO usuario) throws Exception;
    
    public void carregarCursosEtapaWorkflowVO(EtapaWorkflowVO objEtapaWorkflowVO, Boolean controlarAcesso, UsuarioVO usuario) throws Exception;
}
