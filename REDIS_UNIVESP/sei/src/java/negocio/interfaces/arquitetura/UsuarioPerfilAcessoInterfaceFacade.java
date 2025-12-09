package negocio.interfaces.arquitetura;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioPerfilAcessoVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface UsuarioPerfilAcessoInterfaceFacade {

	public UsuarioPerfilAcessoVO novo() throws Exception;

	public void incluir(UsuarioPerfilAcessoVO obj, UsuarioVO usuarioLogado) throws Exception;

	public void alterar(UsuarioPerfilAcessoVO obj, UsuarioVO usuarioLogado) throws Exception;

	public void excluir(UsuarioPerfilAcessoVO obj, UsuarioVO usuarioLogado) throws Exception;

	public List consultarPorNomeUnidadeEnsino(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorNomeUsuario(String valorConsulta, boolean verificaPermissao, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void excluirUsuarioPerfilAcesso(Integer usuario, UsuarioVO usuarioLogado) throws Exception;

	public void alterarUsuarioPerfilAcesso(Integer usuario, List<UsuarioPerfilAcessoVO> objetos, UsuarioVO usuarioLogado) throws Exception;

	public void incluirUsuarioPerfilAcesso(Integer usuarioprm, List<UsuarioPerfilAcessoVO> objetos, UsuarioVO usuarioLogado) throws Exception;

	public UsuarioPerfilAcessoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarUsuarioPerfilAcesso(Integer usuario, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

	public void setIdEntidade(String idEntidade);

}