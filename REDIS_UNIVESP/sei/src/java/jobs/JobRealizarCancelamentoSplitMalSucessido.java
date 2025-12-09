package jobs;

import java.util.List;

import org.springframework.stereotype.Service;

import negocio.comuns.financeiro.DadosSplitMalSucedidoVO;
import negocio.facade.jdbc.arquitetura.SuperFacadeJDBC;

@Service
public class JobRealizarCancelamentoSplitMalSucessido extends SuperFacadeJDBC implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void run() {
		try {
			List<DadosSplitMalSucedidoVO> dadosSplitMalSucedidoVOs = getFacadeFactory().getGerenciadorSplitTransacaoMundiPaggFacade().consultarDadosSplitMalSucedidos();
			for (DadosSplitMalSucedidoVO dadosSplitMalSucedidoVO : dadosSplitMalSucedidoVOs) {
				try {
					getFacadeFactory().getGerenciadorSplitTransacaoMundiPaggFacade().realizarCancelamentoSplitTransacaoMalSucedido(dadosSplitMalSucedidoVO);
					getFacadeFactory().getGerenciadorSplitTransacaoMundiPaggFacade().excluirDadosSplitMalSucedido(dadosSplitMalSucedidoVO);
				} catch (Exception e) {
					continue;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
