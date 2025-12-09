package negocio.interfaces.biblioteca;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.biblioteca.BibliotecaExternaVO;

public interface BibliotecaExternaInterfaceFacade {

    public BibliotecaExternaVO novo() throws Exception;

    public void incluir(final BibliotecaExternaVO obj, UsuarioVO usuarioVO) throws Exception;

    public void alterar(final BibliotecaExternaVO obj, UsuarioVO usuarioVO) throws Exception;

    public void excluir(BibliotecaExternaVO obj, UsuarioVO usuario) throws Exception;

    public List<BibliotecaExternaVO> consultarPorNomeConfiguracaoBiblioteca(String valorConsulta, boolean controleAcesso, UsuarioVO usuario) throws Exception;

    public List<BibliotecaExternaVO> consultarPorCodigoConfiguracaoBiblioteca(Integer valorConsulta, boolean controleAcesso, UsuarioVO usuario) throws Exception;

    public List<BibliotecaExternaVO> consultarPorUrl(String valorConsulta, boolean controleAcesso, UsuarioVO usuario) throws Exception;

    public List<BibliotecaExternaVO> consultarPorNome(String valorConsulta, boolean controleAcesso, UsuarioVO usuario) throws Exception;

    public void excluirBibliotecasExternas(Integer configuracaoBiblioteca, UsuarioVO usuarioVO) throws Exception;

    public void alterarBibliotecasExternas(Integer configuracaoBiblioteca, List<BibliotecaExternaVO> objetos, UsuarioVO usuarioVO) throws Exception;

    public void incluirBibliotecaExternas(Integer configuracaoBiblioteca, List<BibliotecaExternaVO> objetos, UsuarioVO usuario) throws Exception;

    public BibliotecaExternaVO consultarPorChavePrimaria(Integer codigo, UsuarioVO usuario) throws Exception;

    public void setIdEntidade(String idEntidade);
}
