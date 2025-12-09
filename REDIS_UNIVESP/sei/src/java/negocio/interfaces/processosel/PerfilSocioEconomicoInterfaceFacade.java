package negocio.interfaces.processosel;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.processosel.PerfilSocioEconomicoVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
*/
public interface PerfilSocioEconomicoInterfaceFacade {
	

    public PerfilSocioEconomicoVO novo() throws Exception;
    public void incluir(PerfilSocioEconomicoVO obj, UsuarioVO usuarioVO) throws Exception;
    public void alterar(PerfilSocioEconomicoVO obj, UsuarioVO usuarioVO) throws Exception;
    public void excluir(PerfilSocioEconomicoVO obj, UsuarioVO usuarioVO) throws Exception;
    public PerfilSocioEconomicoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados,UsuarioVO usuarioLogado) throws Exception;
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuarioLogado) throws Exception;
    public List consultarPorNomePessoa(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuarioLogado) throws Exception;
    public List consultarPorDescricaoQuestionario(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuarioLogado) throws Exception;
    public void setIdEntidade(String aIdEntidade);
    public void gerarListasRespostaPergunta(PerfilSocioEconomicoVO perfilSocioEconomicoVO, UsuarioVO usuarioVO) throws Exception ;
}