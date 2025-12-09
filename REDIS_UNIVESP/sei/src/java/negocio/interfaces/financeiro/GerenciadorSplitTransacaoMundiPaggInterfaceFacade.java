package negocio.interfaces.financeiro;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ContaMundiPaggVO;
import negocio.comuns.financeiro.DadosSplitMalSucedidoVO;

public interface GerenciadorSplitTransacaoMundiPaggInterfaceFacade {

	void incluirDadosSplitMalSucedido(DadosSplitMalSucedidoVO dadosSplitMalSucedidoVO) throws Exception;

	void alterarDadosSplitMalSucedido(DadosSplitMalSucedidoVO dadosSplitMalSucedidoVO) throws Exception;

	List<DadosSplitMalSucedidoVO> consultarDadosSplitMalSucedidos() throws Exception;

	void excluirDadosSplitMalSucedido(DadosSplitMalSucedidoVO obj) throws Exception;

	void realizarCancelamentoSplitTransacao(List<String> financialMovementKeys) throws Exception;

	void realizarCancelamentoSplitTransacaoMalSucedido(DadosSplitMalSucedidoVO dadosSplitMalSucedidoVO) throws Exception;

	Integer consultarDadosSplitMalSucedidosEnvioSMS() throws Exception;

	String consultarMerchantKeyPorChaveContaMundiPagg(String chaveContaMundiPagg, UsuarioVO usuarioLogado) throws Exception;

	List<ContaMundiPaggVO> consultarContasMundiPagg(int nivelMontarDados, String cnpj, UsuarioVO usuarioLogado) throws Exception;
}
