package negocio.interfaces.financeiro;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoRecebimentoCartaoOnlineVO;
import negocio.comuns.financeiro.FormaPagamentoNegociacaoRecebimentoVO;
import negocio.comuns.financeiro.NegociacaoRecebimentoVO;

public interface GerenciadorTransacaoCartaoInterfaceFacade {

	void cancelarVendaComOperadoraCielo(NegociacaoRecebimentoVO negociacaoRecebimentoVO, List<FormaPagamentoNegociacaoRecebimentoVO> formaPagamentoNegociacaoRecebimentoVOs, UsuarioVO usuarioVO) throws Exception;
	
	void cancelarVendaComOperadoraRede(NegociacaoRecebimentoVO negociacaoRecebimentoVO, List<FormaPagamentoNegociacaoRecebimentoVO> formaPagamentoNegociacaoRecebimentoVOs, UsuarioVO usuarioVO) throws Exception;

	void realizarTransacaoComOperadoraCielo(NegociacaoRecebimentoVO negociacaoRecebimentoVO, List<FormaPagamentoNegociacaoRecebimentoVO> formaPagamentoNegociacaoRecebimentoVOs, ConfiguracaoRecebimentoCartaoOnlineVO configuracaoRecebimentoCartaoOnlineVO, UsuarioVO usuario) throws Exception;
	
	void realizarTransacaoComOperadoraRede(NegociacaoRecebimentoVO negociacaoRecebimentoVO, List<FormaPagamentoNegociacaoRecebimentoVO> formaPagamentoNegociacaoRecebimentoVOs, ConfiguracaoRecebimentoCartaoOnlineVO configuracaoRecebimentoCartaoOnlineVO, UsuarioVO usuario) throws Exception;
}
