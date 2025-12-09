package negocio.interfaces.compras;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.ClassificaoCustosVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
*/
public interface ClassificaoCustosInterfaceFacade {
	

    public ClassificaoCustosVO novo() throws Exception;
    public void incluir(ClassificaoCustosVO obj, UsuarioVO usuarioVO) throws Exception;
    public void alterar(ClassificaoCustosVO obj, UsuarioVO usuarioVO) throws Exception;
    public void excluir(ClassificaoCustosVO obj, UsuarioVO usuarioVO) throws Exception;
    public ClassificaoCustosVO consultarPorChavePrimaria(Integer codigo, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
    public List consultarPorDescricao(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
    public void setIdEntidade(String aIdEntidade);
}