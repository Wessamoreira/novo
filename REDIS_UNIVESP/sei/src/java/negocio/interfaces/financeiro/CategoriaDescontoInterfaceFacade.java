package negocio.interfaces.financeiro;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.CategoriaDescontoVO;

public interface CategoriaDescontoInterfaceFacade {

	public void incluir(final CategoriaDescontoVO obj, final UsuarioVO usuarioVO) throws Exception;

	public CategoriaDescontoVO novo() throws Exception;

	public void alterar(final CategoriaDescontoVO obj, final UsuarioVO usuarioVO) throws Exception;

	public void excluir(CategoriaDescontoVO obj, final UsuarioVO usuarioVO) throws Exception;

	public List consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public CategoriaDescontoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;

}
