package negocio.interfaces.extensao;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.extensao.ClassificaoCursoExtensaoVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
*/
public interface ClassificaoCursoExtensaoInterfaceFacade {
	

    public ClassificaoCursoExtensaoVO novo() throws Exception;
    public void incluir(ClassificaoCursoExtensaoVO obj, UsuarioVO usuarioVO) throws Exception;
    public void alterar(ClassificaoCursoExtensaoVO obj, UsuarioVO usuarioVO) throws Exception;
    public void excluir(ClassificaoCursoExtensaoVO obj, UsuarioVO usuarioVO) throws Exception;
    public ClassificaoCursoExtensaoVO consultarPorChavePrimaria(Integer codigo,UsuarioVO usuario) throws Exception;
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso,UsuarioVO usuario) throws Exception;
    public List consultarPorNome(String valorConsulta, boolean controlarAcesso,UsuarioVO usuario) throws Exception;
    public void setIdEntidade(String aIdEntidade);
 }