package relatorio.negocio.interfaces.financeiro;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ContaPagarVO;
import relatorio.negocio.comuns.financeiro.AutorizacaoPagamentoRelVO;

public interface AutorizacaoPagamentoRelInterfaceFacade {
	public AutorizacaoPagamentoRelVO criarObjeto(ContaPagarVO contaPagarVO, UsuarioVO usuarioVO);

}
