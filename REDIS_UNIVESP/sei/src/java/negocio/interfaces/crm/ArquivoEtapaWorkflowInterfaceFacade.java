package negocio.interfaces.crm;
import java.util.List;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.crm.ArquivoEtapaWorkflowVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
*/
public interface ArquivoEtapaWorkflowInterfaceFacade {
	

    public void validarDados(ArquivoEtapaWorkflowVO obj) throws Exception;
    public ArquivoEtapaWorkflowVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario ) throws Exception;
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario ) throws Exception;
    public List consultarPorNomeEtapaWorkflow(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario ) throws Exception;
    public List consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario ) throws Exception;
    public List consultarPorDescricao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario ) throws Exception;
    public List consultarPorNomeCurso(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario ) throws Exception;
    public void setIdEntidade(String aIdEntidade);
    public void incluirArquivoEtapaWorkflows(Integer etapaWorkflow, List objetos, UsuarioVO usuarioLogado, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;
    public void alterarArquivoEtapaWorkflows(Integer etapaWorkflow, List objetos, UsuarioVO usuarioLogado, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;
    public void excluir(ArquivoEtapaWorkflowVO obj, UsuarioVO usuarioLogado, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;
}