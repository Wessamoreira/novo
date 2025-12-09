package negocio.interfaces.compras;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.CompraItemVO;
import negocio.comuns.compras.CompraVO;

public interface CompraItemInterfaceFacade {

	

	public void excluir(CompraItemVO obj) throws Exception;

	public List<CompraItemVO> consultarPorNomeProduto(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<CompraItemVO> consultarPorCodigoCompra(Integer valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<CompraItemVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public CompraItemVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void setIdEntidade(String idEntidade);

	void persistir(List<CompraItemVO> lista, UsuarioVO usuarioVO) throws Exception;

	List<CompraItemVO> consultarCompraItems(CompraVO compra, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	void persistir(CompraItemVO obj, UsuarioVO usuarioVO) throws Exception;

	void atualizarCampoQuantidadeRecebida(CompraItemVO obj, UsuarioVO usuario) throws Exception;

}