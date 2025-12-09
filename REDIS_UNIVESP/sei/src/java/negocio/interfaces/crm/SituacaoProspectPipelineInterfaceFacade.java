/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package negocio.interfaces.crm;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.crm.SituacaoProspectPipelineVO;
import negocio.comuns.utilitarias.ConsistirException;

/**
 *
 * @author edigarjr
 */
public interface SituacaoProspectPipelineInterfaceFacade {
    public void persistir(SituacaoProspectPipelineVO obj, UsuarioVO usuarioVO) throws Exception;
    public void excluir(SituacaoProspectPipelineVO obj, UsuarioVO usuarioVO) throws Exception;
    public List consultar(String valorConsulta, String campoConsulta, boolean controlarAcesso,UsuarioVO usuarioLogado) throws Exception;
    public void validarDados(SituacaoProspectPipelineVO obj) throws ConsistirException, Exception;
    public SituacaoProspectPipelineVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados,UsuarioVO usuarioLogado ) throws Exception;
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuarioLogado ) throws Exception;
    public List consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuarioLogado ) throws Exception;    
    public List consultarSituacaoProspectWorkflowsInicial(int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public void setIdEntidade(String aIdEntidade);
}
