package negocio.interfaces.financeiro;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ContaReceberNegociacaoRecebimentoVO;
import negocio.comuns.financeiro.FormaPagamentoNegociacaoRecebimentoVO;

public interface GerenciamentoDeTransacaoCartaoDeCreditoInterfaceFacade {

	Boolean validarNumeroCartaoCredito(String numeroCartaoCredito, String bandeiraCartaoCredito) throws Exception;

//	void realizarCancelamentoCartaoCredito(List<FormaPagamentoNegociacaoRecebimentoVO> formaPagamentoNegociacaoRecebimentoVOs, String identificacaoDaLoja) throws Exception;

//	void realizarTransacaoComCartaoDeCredito(NegociacaoRecebimentoVO negociacaoRecebimentoVO, PessoaVO pessoaVO, ParceiroVO parceiroVO, FornecedorVO fornecedorVO, String identificacaoDaLoja, List<FormaPagamentoNegociacaoRecebimentoVO> formaPagamentoNegociacaoRecebimentoVOs, UsuarioVO usuario) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 18 de mai de 2016 
	 * @param contaReceberNegociacaoRecebimentoVO
	 * @throws Exception 
	 */
	void processarRecebimentoCartaoCreditoDCC(ContaReceberNegociacaoRecebimentoVO contaReceberNegociacaoRecebimentoVO) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 2 de jun de 2016 
	 * @param formaPagamentoNegociacaoRecebimentoVO
	 * @param usuarioVO
	 * @throws Exception 
	 */
	void realizarCancelamentoOuEstornoTransacaoEspecificaCartaoCredito(FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO, UsuarioVO usuarioVO) throws Exception;
}
