package negocio.interfaces.biblioteca;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.biblioteca.SecaoVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
*/
public interface SecaoInterfaceFacade {
	

    public SecaoVO novo() throws Exception;
    public void incluir(SecaoVO obj, UsuarioVO usuarioVO) throws Exception;
    public void alterar(SecaoVO obj, UsuarioVO usuarioVO) throws Exception;
    public void excluir(SecaoVO obj, UsuarioVO usuarioVO) throws Exception;
    public SecaoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public void setIdEntidade(String aIdEntidade);
    public List consultar(String campoConsulta, String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception ;
    public List<SecaoVO> consultarSecaoNivelComboBox(boolean controlarAcesso, UsuarioVO usuario) throws Exception;
}