package negocio.interfaces.faturamento.nfe;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.RecebimentoCompraVO;
import negocio.comuns.faturamento.nfe.NotaFiscalEntradaRecebimentoCompraVO;
import negocio.comuns.faturamento.nfe.NotaFiscalEntradaVO;

public interface NotaFiscalEntradaRecebimentoCompraInterfaceFacade {

	void persistir(List<NotaFiscalEntradaRecebimentoCompraVO> lista, boolean verificarAcesso, UsuarioVO usuarioVO) ;
	
	void excluir(NotaFiscalEntradaRecebimentoCompraVO obj, boolean verificarAcesso, UsuarioVO usuario) ;

	List<NotaFiscalEntradaRecebimentoCompraVO> consultaRapidaPorCompra(Integer compra, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) ;

	List<NotaFiscalEntradaRecebimentoCompraVO> consultaRapidaPorNotaFiscalEntrada(Integer notaFiscalEntrada, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) ;

	NotaFiscalEntradaRecebimentoCompraVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) ;

	List<NotaFiscalEntradaRecebimentoCompraVO> consultarTodasRecebimentoComprasPendenteFornecedorPorNotaFiscalEntrada(NotaFiscalEntradaVO notaFiscalEntrada, boolean controlarAcesso, UsuarioVO usuario) ;

	NotaFiscalEntradaRecebimentoCompraVO consultarPorRecebimentoCompras(RecebimentoCompraVO recebimentoCompraVO, UsuarioVO usuario) ;

	void estornaNotaFiscalEntradaRecebimentoCompra(NotaFiscalEntradaVO obj, UsuarioVO usuario) ;

	void removerNotaFiscalEntradaRecebimentoCompraVONaoSelecionados(NotaFiscalEntradaVO obj, UsuarioVO usuario);

	void validarDadosParaCriarContaPagar(NotaFiscalEntradaVO obj, UsuarioVO usuario);	

}