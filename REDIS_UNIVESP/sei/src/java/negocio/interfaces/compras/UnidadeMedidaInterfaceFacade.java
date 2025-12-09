package negocio.interfaces.compras;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.UnidadeMedidaVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
*/
public interface UnidadeMedidaInterfaceFacade {
	

    
    public void incluir(UnidadeMedidaVO obj) throws Exception;
    public void alterar(UnidadeMedidaVO obj) throws Exception;
    public void excluir(UnidadeMedidaVO obj) throws Exception;
    public List<UnidadeMedidaVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<UnidadeMedidaVO> consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<UnidadeMedidaVO> consultarPorDescricao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public UnidadeMedidaVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public void setIdEntidade(String aIdEntidade);
}