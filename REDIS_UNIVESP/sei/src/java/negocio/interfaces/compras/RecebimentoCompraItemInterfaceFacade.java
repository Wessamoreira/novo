package negocio.interfaces.compras;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.RecebimentoCompraItemVO;
import negocio.comuns.compras.RecebimentoCompraVO;
import negocio.comuns.compras.enumeradores.OperacaoEstoqueEnum;
import negocio.comuns.faturamento.nfe.NotaFiscalEntradaRecebimentoCompraVO;

public interface RecebimentoCompraItemInterfaceFacade {

	
	public void atualizarCompraItem(RecebimentoCompraItemVO obj, Integer unidadeEnsino, OperacaoEstoqueEnum operacaoEstoque, UsuarioVO usuario) throws Exception;

	public void excluir(RecebimentoCompraItemVO obj) throws Exception;

	public List<RecebimentoCompraItemVO> consultarPorNomeEnderecoEstoque(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<RecebimentoCompraItemVO> consultarPorAbreviaturaProduto(Integer valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<RecebimentoCompraItemVO> consultarPorAbreviaturaProdutoConsumo(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public List<RecebimentoCompraItemVO> consultarPorCodigoCompraItem(Integer valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<RecebimentoCompraItemVO> consultarPorCompraItem(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<RecebimentoCompraItemVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public RecebimentoCompraItemVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<RecebimentoCompraItemVO> consultaRapidaPorNotaFiscalEntradaRecebimentoCompraVO(NotaFiscalEntradaRecebimentoCompraVO obj, UsuarioVO usuario) throws Exception;

	void atualizarCampoQuantidadeRecebidaValorTotal(RecebimentoCompraItemVO obj, UsuarioVO usuario);

	void persistir(List<RecebimentoCompraItemVO> lista, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void persistir(RecebimentoCompraItemVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void excluirRecebimentoCompraItem(String listaCodigo, UsuarioVO usuario) throws Exception;

	List<RecebimentoCompraItemVO> consultarRecebimentoCompraItems(RecebimentoCompraVO recebimentoCompra, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	Double consultarQuantidadeRecebibaPorCompraItemComCodigoDiferenteRecebimentoCompraItem(Integer compraItem, Integer codigoRecebimentoCompraItem, UsuarioVO usuario) throws Exception;

}