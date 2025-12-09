package negocio.interfaces.financeiro;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.FormaPagamentoNegociacaoRecebimentoVO;
import negocio.comuns.financeiro.NegociacaoRecebimentoVO;
import negocio.comuns.utilitarias.ConsistirException;

public interface NegociacaoRecebimentoDCCInterfaceFacade {

	void validarRecebimentoPossuiMaisDeUmaFormaQuandoDCC(NegociacaoRecebimentoVO negociacaoRecebimentoVO, FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO) throws ConsistirException;

//	void adicionarFormaPagamentoNegociacaoRecebimentoCartaoCreditoQuandoDCC(NegociacaoRecebimentoVO negociacaoRecebimentoVO, FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO, UsuarioVO usuarioVO) throws ConsistirException, CloneNotSupportedException, Exception;

	void incluir(NegociacaoRecebimentoVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiro, boolean verificarAcesso, UsuarioVO usuario) throws Exception;

	/**
	 * @author Victor Hugo de Paula Costa - 20 de abr de 2016
	 * @param negociacaoRecebimentoVO
	 * @param formaPagamentoNegociacaoRecebimentoVO
	 * @throws ConsistirException
	 */
	void calcularValorQuandoTipoRecebimentoDCC(NegociacaoRecebimentoVO negociacaoRecebimentoVO, FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO) throws ConsistirException;

	/** 
	 * @author Victor Hugo de Paula Costa - 27 de abr de 2016 
	 * @param codigo
	 * @param nivelMontarDados
	 * @param configuracaoFinanceiroVO
	 * @param usuarioLogado
	 * @return
	 * @throws Exception 
	 */
	NegociacaoRecebimentoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioLogado) throws Exception;

}
