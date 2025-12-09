package negocio.interfaces.administrativo;
import java.util.List;

import negocio.comuns.administrativo.VisaoVO;
import negocio.comuns.arquitetura.UsuarioVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
*/
public interface VisaoInterfaceFacade {
	

    public VisaoVO novo() throws Exception;
    public void incluir(VisaoVO obj, UsuarioVO usuarioVO) throws Exception;
    public void alterar(VisaoVO obj, UsuarioVO usuarioVO) throws Exception;
    public void excluir(VisaoVO obj, UsuarioVO usuarioVO) throws Exception;
    public VisaoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public void setIdEntidade(String aIdEntidade);
}