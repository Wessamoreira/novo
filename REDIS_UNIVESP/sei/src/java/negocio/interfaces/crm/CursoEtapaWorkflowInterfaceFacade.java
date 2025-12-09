package negocio.interfaces.crm;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.crm.CursoEtapaWorkflowVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
 */
public interface CursoEtapaWorkflowInterfaceFacade {

    public void validarDados(CursoEtapaWorkflowVO obj) throws Exception;

    public CursoEtapaWorkflowVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public String consultarScriptPorCodigoCursoCodigoEtapa(Integer codigoEtapa, Integer codigoCurso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void setIdEntidade(String aIdEntidade);

    public void incluirCursoEtapaWorkflows(Integer workflowPrm, List objetos, UsuarioVO usuario) throws Exception;

    public void alterarCursoEtapaWorkflows(Integer workflowPrm, List objetos, UsuarioVO usuario) throws Exception;

    public void excluirCursoEtapaWorkflows(Integer etapaWorkflow, UsuarioVO usuario) throws Exception;
}
