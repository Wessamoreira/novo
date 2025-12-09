package integracoes.cartao;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.GsonBuilder;


import integracoes.cartao.cielo.BandeiraCartaoCreditoEnum;
import integracoes.cartao.cielo.Merchant;
import integracoes.cartao.cielo.sdk.CieloEcommerce;
import integracoes.cartao.cielo.sdk.Environment;
import integracoes.cartao.cielo.sdk.Payment;
import integracoes.cartao.cielo.sdk.Sale;
import integracoes.cartao.cielo.sdk.SaleFormaPagamentoNegociacaoRecebimentoVO;
import integracoes.cartao.cielo.sdk.SaleResponse;
import integracoes.cartao.cielo.sdk.request.CieloRequestException;
import integracoes.cartao.erede.Rede;
import integracoes.cartao.erede.Store;
import integracoes.cartao.erede.TransactionResponse;
import integracoes.cartao.erede.VendaRede;
import negocio.comuns.arquitetura.ExcluirJsonStrategy;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.CieloCodigoRetornoVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ConfiguracaoRecebimentoCartaoOnlineVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.FormaPagamentoNegociacaoRecebimentoVO;
import negocio.comuns.financeiro.NegociacaoRecebimentoVO;
import negocio.comuns.financeiro.enumerador.AmbienteCartaoCreditoEnum;
import negocio.comuns.financeiro.enumerador.OperadoraCartaoCreditoEnum;
import negocio.comuns.financeiro.enumerador.SituacaoTransacaoEnum;
import negocio.comuns.financeiro.enumerador.TipoOrigemOperadoraCodigoRetornoDCC;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.TipoCartaoOperadoraCartaoEnum;
import negocio.comuns.utilitarias.dominios.TipoFormaPagamento;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.GerenciadorTransacaoCartaoInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class GerenciadorTransacaoCartao extends ControleAcesso implements GerenciadorTransacaoCartaoInterfaceFacade {

	private static final long serialVersionUID = 1L;
	
	public GerenciadorTransacaoCartao() throws Exception {
		super();
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarTransacaoComOperadoraCielo(NegociacaoRecebimentoVO negociacaoRecebimentoVO, List<FormaPagamentoNegociacaoRecebimentoVO> formaPagamentoNegociacaoRecebimentoVOs, ConfiguracaoRecebimentoCartaoOnlineVO configuracaoRecebimentoCartaoOnlineVO, UsuarioVO usuario) throws Exception{
		ConfiguracaoFinanceiroVO configuracaoFinanceiroVO = (ConfiguracaoFinanceiroVO) configuracaoRecebimentoCartaoOnlineVO.getConfiguracaoFinanceiroVO().clone();
		Merchant merchant = new Merchant(configuracaoFinanceiroVO.getMerchantIdCielo(), configuracaoFinanceiroVO.getMerchantKeyCielo());
		CieloEcommerce ce = new CieloEcommerce(merchant, (configuracaoFinanceiroVO.getAmbienteCartaoCreditoEnum().equals(AmbienteCartaoCreditoEnum.HOMOLOGACAO) ? Environment.SANDBOX : Environment.PRODUCTION));
		ArrayList<SaleFormaPagamentoNegociacaoRecebimentoVO> listaCartoes = new ArrayList<SaleFormaPagamentoNegociacaoRecebimentoVO>();
		ArrayList<SaleFormaPagamentoNegociacaoRecebimentoVO> listaVendaAutorizadas = new ArrayList<SaleFormaPagamentoNegociacaoRecebimentoVO>();
		negociacaoRecebimentoVO.setMensagemPagamentoCartaoCredito("");
		ContaReceberVO contaReceberVO = null;
		contaReceberVO = !negociacaoRecebimentoVO.getContaReceberNegociacaoRecebimentoVOs().isEmpty() && negociacaoRecebimentoVO.getContaReceberNegociacaoRecebimentoVOs().size() == 1 
				? negociacaoRecebimentoVO.getContaReceberNegociacaoRecebimentoVOs().get(0).getContaReceber() : null;
		
		CieloCodigoRetornoVO cieloCodigoRetornoVO = null;
		String nomeCustomer = "";
		
		try {
			montarDadosTransacaoCielo(listaCartoes, negociacaoRecebimentoVO, configuracaoFinanceiroVO, formaPagamentoNegociacaoRecebimentoVOs);
			Iterator<SaleFormaPagamentoNegociacaoRecebimentoVO> i = listaCartoes.iterator();
			int index = 0;
			while (i.hasNext()) {
				SaleFormaPagamentoNegociacaoRecebimentoVO vendaFormaPagamentoNegociacaoRecebimento = (SaleFormaPagamentoNegociacaoRecebimentoVO) i.next();
				Sale sale = ce.createSale(vendaFormaPagamentoNegociacaoRecebimento.getSale());
				nomeCustomer = inicializarDadosNomeCustomer(sale, usuario);
				vendaFormaPagamentoNegociacaoRecebimento.setSale(sale);
				
				String returnCode = sale.getPayment().getReturnCode().length() == 1 ? "0" + sale.getPayment().getReturnCode() : sale.getPayment().getReturnCode();
				cieloCodigoRetornoVO = getFacadeFactory().getCieloCodigoRetornoFacade().consultarPorCodigoRetorno(returnCode);
				
				SaleResponse respotaVenda = ce.captureSale(sale.getPayment().getPaymentId(), sale.getPayment().getAmount(), 0);
				if(respotaVenda.getReturnCode().equals("6")){
					negociacaoRecebimentoVO.setMensagemPagamentoCartaoCredito(negociacaoRecebimentoVO.getMensagemPagamentoCartaoCredito() + System.getProperty("line.separator") + " - Transação Autorizada com Sucesso" + "<br>");
					listaVendaAutorizadas.add(vendaFormaPagamentoNegociacaoRecebimento);
				}else{
					negociacaoRecebimentoVO.setMensagemPagamentoCartaoCredito("Operação não autorizada - " + respotaVenda.getReturnMessage());
					throw new Exception(negociacaoRecebimentoVO.getMensagemPagamentoCartaoCredito());
				}
				listaCartoes.set(index, vendaFormaPagamentoNegociacaoRecebimento);
				index++;
			}
			getFacadeFactory().getFormaPagamentoNegociacaoRecebimentoFacade().atualizarFormaPagamentoNegociacaoRecebimentoCartaoCreditoCielo(listaCartoes, contaReceberVO, usuario, null, TipoOrigemOperadoraCodigoRetornoDCC.CIELO, cieloCodigoRetornoVO == null ? null : cieloCodigoRetornoVO.getCodigo(), negociacaoRecebimentoVO.getRealizandoPagamentoJobRecorrencia(), negociacaoRecebimentoVO.getJobExecutadaManualmente(), negociacaoRecebimentoVO.getCartaoCreditoDebitoRecorrenciaPessoaVO().getCodigo(), nomeCustomer);
		} catch (Exception e) {
			if(!listaVendaAutorizadas.isEmpty()){
				cancelarVendaCartaoCieloOperacaoComErro(listaVendaAutorizadas, merchant, configuracaoFinanceiroVO);	
			}
			getFacadeFactory().getFormaPagamentoNegociacaoRecebimentoFacade().atualizarFormaPagamentoNegociacaoRecebimentoCartaoCreditoCielo(listaCartoes, contaReceberVO, usuario, e.getMessage(), TipoOrigemOperadoraCodigoRetornoDCC.CIELO, cieloCodigoRetornoVO == null ? null : cieloCodigoRetornoVO.getCodigo(), negociacaoRecebimentoVO.getRealizandoPagamentoJobRecorrencia(), negociacaoRecebimentoVO.getJobExecutadaManualmente(), negociacaoRecebimentoVO.getCartaoCreditoDebitoRecorrenciaPessoaVO().getCodigo(), nomeCustomer);
			throw e;
		}
	}
	
	public String inicializarDadosNomeCustomer(Sale sale, UsuarioVO usuarioVO) throws Exception {
		InetAddress addr = InetAddress.getLocalHost();
		if (Uteis.isAtributoPreenchido(addr.getHostName())) {
			sale.getCustomer().setName("OTM-TI - Usuário: " + usuarioVO.getNome() + " - IP: " + addr.getHostName() + "");
		} else {
			sale.getCustomer().setName("OTM-TI - Usuário: " + usuarioVO.getNome() + " - IP: Não encontrado");
		}
		return sale.getCustomer().getName();
		
	}
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarTransacaoComOperadoraRede(NegociacaoRecebimentoVO negociacaoRecebimentoVO, List<FormaPagamentoNegociacaoRecebimentoVO> formaPagamentoNegociacaoRecebimentoVOs, ConfiguracaoRecebimentoCartaoOnlineVO configuracaoRecebimentoCartaoOnlineVO, UsuarioVO usuario) throws Exception {
		ConfiguracaoFinanceiroVO configuracaoFinanceiroVO = configuracaoRecebimentoCartaoOnlineVO.getConfiguracaoFinanceiroVO();
		integracoes.cartao.erede.Environment amb = null;
		if (AmbienteCartaoCreditoEnum.HOMOLOGACAO.equals(configuracaoFinanceiroVO.getAmbienteCartaoCreditoEnum())) {
			amb = integracoes.cartao.erede.Environment.sandbox();
		} else if (AmbienteCartaoCreditoEnum.PRODUCAO.equals(configuracaoFinanceiroVO.getAmbienteCartaoCreditoEnum())) {
			amb = integracoes.cartao.erede.Environment.production();
		} else {
			throw new Exception("Ambiente do Pagamento Online não informado!");
		}
		Store loja = new Store(configuracaoFinanceiroVO.getPvRede(), configuracaoFinanceiroVO.getTokenRede(), amb);
		ArrayList<VendaRede> vendas = new ArrayList<VendaRede>();
		ArrayList<VendaRede> listaVendaAutorizadas = new ArrayList<VendaRede>();
		negociacaoRecebimentoVO.setMensagemPagamentoCartaoCredito("");
		try {
			vendas.addAll(montarDadosTransacaoRede(negociacaoRecebimentoVO, configuracaoFinanceiroVO, formaPagamentoNegociacaoRecebimentoVOs, false, usuario));
			Iterator<VendaRede> i = vendas.iterator();
			int index = 0;
			while (i.hasNext()) {
				VendaRede venda = (VendaRede) i.next();
				TransactionResponse autorizacao = Rede.autorizar(loja, venda);
				if (autorizacao.getReturnCode().equals("220")) {
					// TODO REDIRECT 3DS
				} else if ("00".equals(autorizacao.getReturnCode())) {
					venda.setTid(autorizacao.getTid());
					venda.setCodigoAutorizacao(autorizacao.getAuthorizationCode());
					listaVendaAutorizadas.add(venda);
				} else {
					negociacaoRecebimentoVO.setMensagemPagamentoCartaoCredito("Operação não autorizada - " + autorizacao.getReturnMessage());
					throw new Exception(negociacaoRecebimentoVO.getMensagemPagamentoCartaoCredito());
				}
				vendas.set(index, venda);
				index++;
			}
			getFacadeFactory().getFormaPagamentoNegociacaoRecebimentoFacade().atualizarFormaPagamentoNegociacaoRecebimentoCartaoRede(vendas, usuario, false);
		} catch (Exception e) {
			e.printStackTrace();
			if (!listaVendaAutorizadas.isEmpty()) {
				cancelarTransacaoCartaoRedeOperacaoComErro(listaVendaAutorizadas, loja, configuracaoFinanceiroVO, usuario);
			}
			getFacadeFactory().getFormaPagamentoNegociacaoRecebimentoFacade().atualizarFormaPagamentoNegociacaoRecebimentoCartaoRede(vendas, usuario, true);
			if (e.getMessage().equals("Transaction not available to capture")) {
				throw new Exception("Transaction not available to capture - Por favor confira os dados do cartão e tente novamente.");
			}
			throw e;
		}
	}
	
	public void cancelarVendaCartaoCieloOperacaoComErro(List<SaleFormaPagamentoNegociacaoRecebimentoVO> vendas, Merchant merchant, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO){
		try {
			CieloEcommerce ce = new CieloEcommerce(merchant, (configuracaoFinanceiroVO.getAmbienteCartaoCreditoEnum().equals(AmbienteCartaoCreditoEnum.HOMOLOGACAO) ? Environment.SANDBOX : Environment.PRODUCTION));
			for (SaleFormaPagamentoNegociacaoRecebimentoVO venda : vendas) {
				ce.cancelSale(venda.getSale().getPayment().getPaymentId(), venda.getSale().getPayment().getAmount());
			}
		} catch (IOException | CieloRequestException e) {
			e.printStackTrace();
		}
	}
	
	public void cancelarTransacaoCartaoRedeOperacaoComErro(List<VendaRede> vendas, Store loja, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) {
		try {
			for (VendaRede venda : vendas) {
				if (venda.getTipoCartao().equals(TipoFormaPagamento.CARTAO_DE_CREDITO.getValor())) {
					TransactionResponse cancelamento = Rede.cancelar(loja, venda);
					String cartao = venda.getFormaPagamentoNegociacaoRecebimentoVOs().get(0).getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNumeroCartao().substring(venda.getFormaPagamentoNegociacaoRecebimentoVOs().get(0).getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNumeroCartao().length() - 4);
					String codigoContaReceberRecebimento = getFacadeFactory().getContaReceberRecebimentoFacade().consultarContaReceberRecebimentoTransacaoCartao(venda.getFormaPagamentoNegociacaoRecebimentoVOs().get(0).getCodigo());
					getFacadeFactory().getTransacaoCartaoOnlineInterfaceFacade().incluirLogTransacaoCartaoOnline(
							venda.getRequisicaoCancelamento(), venda.getRespostaCancelamento(), venda.getCodigoAutorizacao(), venda.getTid(), 
							venda.getFormaPagamentoNegociacaoRecebimentoVOs().get(0).getNegociacaoRecebimentoVO().getMensagemPagamentoCartaoCredito(), 
							venda.getFormaPagamentoNegociacaoRecebimentoVOs().get(0).getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO(), 
							codigoContaReceberRecebimento, SituacaoTransacaoEnum.REPROVADO, "XXXX.XXXX.XXXX."+cartao,
							venda.getFormaPagamentoNegociacaoRecebimentoVOs().get(0).getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNumeroParcela(),
							venda.getValor(),
							venda.getFormaPagamentoNegociacaoRecebimentoVOs().get(0).getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getDataVencimento(),
							TipoCartaoOperadoraCartaoEnum.CARTAO_CREDITO, null, null, null, false, false, 0, "", usuario);
					if (!"359".equals(cancelamento.getReturnCode())) {
						// TODO verificar existencia de cancelar de um cancelamento
					}
				} else {
					// TODO débito
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public void cancelarVendaComOperadoraCielo(NegociacaoRecebimentoVO negociacaoRecebimentoVO, List<FormaPagamentoNegociacaoRecebimentoVO> formaPagamentoNegociacaoRecebimentoVOs,UsuarioVO usuarioVO) throws Exception {
		ConfiguracaoFinanceiroVO configuracaoFinanceiroVO = formaPagamentoNegociacaoRecebimentoVOs.get(0).getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getConfiguracaoFinanceiroCartaoVO().getConfiguracaoFinanceiroVO();
		Merchant merchant = new Merchant(configuracaoFinanceiroVO.getMerchantIdCielo(), configuracaoFinanceiroVO.getMerchantKeyCielo());
		ArrayList<SaleResponse> respostaVenda = new ArrayList<SaleResponse>();
		SaleResponse resposta = new SaleResponse();

		try {
			
			for (FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO : formaPagamentoNegociacaoRecebimentoVOs) {
				if (formaPagamentoNegociacaoRecebimentoVO.getFormaPagamento().getTipo().equals(TipoFormaPagamento.CARTAO_DE_CREDITO.getValor())) {
					if (formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getConfiguracaoFinanceiroCartaoVO().getPermitiRecebimentoCartaoOnline()
							&& !formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getChaveDaTransacao().isEmpty()) {
						String numeroPedidoCielo = formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getChaveDaTransacao();
						
						resposta = new CieloEcommerce(merchant, (configuracaoFinanceiroVO.getAmbienteCartaoCreditoEnum().equals(AmbienteCartaoCreditoEnum.HOMOLOGACAO) ? Environment.SANDBOX : Environment.PRODUCTION)).cancelSale(numeroPedidoCielo);
						respostaVenda.add(resposta);
						
						String codigoContaReceberRecebimento = getFacadeFactory().getContaReceberRecebimentoFacade().consultarContaReceberRecebimentoTransacaoCartao(formaPagamentoNegociacaoRecebimentoVO.getCodigo());
						String cartao = formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNumeroCartao().substring(formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNumeroCartao().length() - 4);

						getFacadeFactory().getTransacaoCartaoOnlineInterfaceFacade().incluirLogTransacaoCartaoOnline(
								new GsonBuilder().setExclusionStrategies(new ExcluirJsonStrategy()).create().toJson(resposta), 
								resposta.getReasonMessage(),
								formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getChaveDaTransacao(), 
								formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getChaveDaTransacao(), 
								formaPagamentoNegociacaoRecebimentoVO.getNegociacaoRecebimentoVO().getMensagemPagamentoCartaoCredito(), 
								formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO(), 
								codigoContaReceberRecebimento,
								SituacaoTransacaoEnum.CANCELADO,
								"XXXX.XXXX.XXXX."+cartao,
								formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNumeroParcela(),
								formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getValorParcela(),
								formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getDataVencimento(),
								TipoCartaoOperadoraCartaoEnum.CARTAO_CREDITO, null, null, null, negociacaoRecebimentoVO.getRealizandoPagamentoJobRecorrencia(), negociacaoRecebimentoVO.getJobExecutadaManualmente(), negociacaoRecebimentoVO.getCartaoCreditoDebitoRecorrenciaPessoaVO().getCodigo(), "", usuarioVO);
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public void cancelarVendaComOperadoraRede(NegociacaoRecebimentoVO negociacaoRecebimentoVO, List<FormaPagamentoNegociacaoRecebimentoVO> formaPagamentoNegociacaoRecebimentoVOs, UsuarioVO usuarioVO) throws Exception {
		ConfiguracaoFinanceiroVO configuracaoFinanceiroVO = formaPagamentoNegociacaoRecebimentoVOs.get(0).getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getConfiguracaoFinanceiroCartaoVO().getConfiguracaoFinanceiroVO();
		integracoes.cartao.erede.Environment amb = null;
		if (AmbienteCartaoCreditoEnum.HOMOLOGACAO.equals(configuracaoFinanceiroVO.getAmbienteCartaoCreditoEnum())) {
			amb = integracoes.cartao.erede.Environment.sandbox();
		} else if (AmbienteCartaoCreditoEnum.PRODUCAO.equals(configuracaoFinanceiroVO.getAmbienteCartaoCreditoEnum())) {
			amb = integracoes.cartao.erede.Environment.production();
		} else {
			throw new Exception("Ambiente do Pagamento Online não informado!");
		}
		Store loja = new Store(configuracaoFinanceiroVO.getPvRede(), configuracaoFinanceiroVO.getTokenRede(), amb);
		ArrayList<VendaRede> vendas = new ArrayList<VendaRede>();
		ArrayList<VendaRede> listaVendaCanceladas = new ArrayList<VendaRede>();
		try {
//			for (FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO : formaPagamentoNegociacaoRecebimentoVOs) {
//				if (formaPagamentoNegociacaoRecebimentoVO.getFormaPagamento().getTipo().equals(TipoFormaPagamento.CARTAO_DE_DEBITO.getValor())) {
//					// TODO
//				}
//			}
			vendas.addAll(montarDadosTransacaoRede(negociacaoRecebimentoVO, configuracaoFinanceiroVO, formaPagamentoNegociacaoRecebimentoVOs, true, usuarioVO));
			Iterator<VendaRede> i = vendas.iterator();
			int index = 0;
			while (i.hasNext()) {
				VendaRede venda = (VendaRede) i.next();
				String codigoContaReceberRecebimento = getFacadeFactory().getContaReceberRecebimentoFacade().consultarContaReceberRecebimentoTransacaoCartao(venda.getFormaPagamentoNegociacaoRecebimentoVOs().get(0).getCodigo());
				String cartao = venda.getFormaPagamentoNegociacaoRecebimentoVOs().get(0).getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNumeroCartao().substring(venda.getFormaPagamentoNegociacaoRecebimentoVOs().get(0).getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNumeroCartao().length() - 4);
				TipoCartaoOperadoraCartaoEnum tipoCartao = TipoCartaoOperadoraCartaoEnum.CARTAO_CREDITO;
				if ("CD".equals(venda.getFormaPagamentoNegociacaoRecebimentoVOs().get(0).getFormaPagamento().getTipo())) {
					tipoCartao = TipoCartaoOperadoraCartaoEnum.CARTAO_DEBITO;
				}
				TransactionResponse cancelamento = Rede.cancelar(loja, venda);
				if ("359".equals(cancelamento.getReturnCode())) {
					venda.setCodigoAutorizacao(cancelamento.getRefundId());
					listaVendaCanceladas.add(venda);
					getFacadeFactory().getTransacaoCartaoOnlineInterfaceFacade().incluirLogTransacaoCartaoOnline(
							venda.getRequisicaoCancelamento(), venda.getRespostaCancelamento(), venda.getCodigoAutorizacao(), venda.getTid(), 
							venda.getFormaPagamentoNegociacaoRecebimentoVOs().get(0).getNegociacaoRecebimentoVO().getMensagemPagamentoCartaoCredito(), 
							venda.getFormaPagamentoNegociacaoRecebimentoVOs().get(0).getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO(), 
							codigoContaReceberRecebimento, SituacaoTransacaoEnum.APROVADO, "XXXX.XXXX.XXXX."+cartao,
							venda.getFormaPagamentoNegociacaoRecebimentoVOs().get(0).getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNumeroParcela(),
							venda.getValor(),
							venda.getFormaPagamentoNegociacaoRecebimentoVOs().get(0).getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getDataVencimento(),
							tipoCartao, null, null, null, negociacaoRecebimentoVO.getRealizandoPagamentoJobRecorrencia(), negociacaoRecebimentoVO.getJobExecutadaManualmente(), negociacaoRecebimentoVO.getCartaoCreditoDebitoRecorrenciaPessoaVO().getCodigo(), "", usuarioVO);
				} else {
					getFacadeFactory().getTransacaoCartaoOnlineInterfaceFacade().incluirLogTransacaoCartaoOnline(
							venda.getRequisicaoCancelamento(), venda.getRespostaCancelamento(), venda.getCodigoAutorizacao(), venda.getTid(), 
							venda.getFormaPagamentoNegociacaoRecebimentoVOs().get(0).getNegociacaoRecebimentoVO().getMensagemPagamentoCartaoCredito(), 
							venda.getFormaPagamentoNegociacaoRecebimentoVOs().get(0).getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO(), 
							codigoContaReceberRecebimento, SituacaoTransacaoEnum.REPROVADO, "XXXX.XXXX.XXXX."+cartao,
							venda.getFormaPagamentoNegociacaoRecebimentoVOs().get(0).getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNumeroParcela(),
							venda.getValor(),
							venda.getFormaPagamentoNegociacaoRecebimentoVOs().get(0).getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getDataVencimento(),
							tipoCartao, null, null, null, negociacaoRecebimentoVO.getRealizandoPagamentoJobRecorrencia(), negociacaoRecebimentoVO.getJobExecutadaManualmente(), negociacaoRecebimentoVO.getCartaoCreditoDebitoRecorrenciaPessoaVO().getCodigo(), "", usuarioVO);
					negociacaoRecebimentoVO.setMensagemPagamentoCartaoCredito("Operação não autorizada - " + cancelamento.getReturnMessage());
					throw new Exception(negociacaoRecebimentoVO.getMensagemPagamentoCartaoCredito());
				}
				vendas.set(index, venda);
				index++;
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (!listaVendaCanceladas.isEmpty()) {
				cancelarTransacaoCartaoRedeOperacaoComErro(listaVendaCanceladas, loja, configuracaoFinanceiroVO, usuarioVO);
			}
			throw e;
		}
	}
	
	public void montarDadosTransacaoCielo(ArrayList<SaleFormaPagamentoNegociacaoRecebimentoVO> listaCartoes, NegociacaoRecebimentoVO negociacaoRecebimentoVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, List<FormaPagamentoNegociacaoRecebimentoVO> formaPagamentoNegociacaoRecebimentoVOs) throws Exception {
		for (FormaPagamentoNegociacaoRecebimentoVO obj : formaPagamentoNegociacaoRecebimentoVOs) {
			obj.setNegociacaoRecebimentoVO(negociacaoRecebimentoVO);
			obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setFormaPagamentoNegociacaoRecebimentoVO(obj);
			if (obj.getFormaPagamento().getTipo().equals(TipoFormaPagamento.CARTAO_DE_CREDITO.getValor()) &&
					obj.getConfiguracaoFinanceiroCartaoVO().getPermitiRecebimentoCartaoOnline()) {
				
				if (listaCartoes.stream().anyMatch(p->p.getNumeroCartao().equals(obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNumeroCartao()))) {
					Optional<SaleFormaPagamentoNegociacaoRecebimentoVO> findFirst = listaCartoes.stream().filter(p->p.getNumeroCartao().equals(obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNumeroCartao())).findFirst();
					findFirst.get().getSale().getPayment().setAmount(findFirst.get().getSale().getPayment().getAmount() + UteisTexto.converteStringParaInteiro(obj.getValorRecebimento().toString()));
					findFirst.get().getListaFormaPagamentoNegociacaoRecebimentoVOs().add(obj);
				} else {
					SaleFormaPagamentoNegociacaoRecebimentoVO venda = new SaleFormaPagamentoNegociacaoRecebimentoVO();
					venda.setSale(montarDadosTransacaoCartaoCredito(configuracaoFinanceiroVO, obj));
					venda.setNumeroCartao(venda.getSale().getPayment().getCreditCard().getCardNumber());
					venda.getListaFormaPagamentoNegociacaoRecebimentoVOs().add(obj);
					listaCartoes.add(venda);
				}				
			}
		}
	}
	
	public ArrayList<VendaRede> montarDadosTransacaoRede(NegociacaoRecebimentoVO negociacaoRecebimentoVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, List<FormaPagamentoNegociacaoRecebimentoVO> formaPagamentoNegociacaoRecebimentoVOs, boolean cancelamento, UsuarioVO usuario) throws Exception {
		ArrayList<VendaRede> vendas = new ArrayList<VendaRede>();
		Map<String, VendaRede> cartoes = new HashMap<String, VendaRede>(0);
		int nrCartao = 0;
		for (FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO : formaPagamentoNegociacaoRecebimentoVOs) {
			if (cancelamento) {
				formaPagamentoNegociacaoRecebimentoVO.setConfiguracaoFinanceiroCartaoVO(formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getConfiguracaoFinanceiroCartaoVO());
			}
			formaPagamentoNegociacaoRecebimentoVO.setNegociacaoRecebimentoVO(negociacaoRecebimentoVO);
			formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setFormaPagamentoNegociacaoRecebimentoVO(formaPagamentoNegociacaoRecebimentoVO);
			if ((formaPagamentoNegociacaoRecebimentoVO.getFormaPagamento().getTipo().equals(TipoFormaPagamento.CARTAO_DE_CREDITO.getValor()) ||
					formaPagamentoNegociacaoRecebimentoVO.getFormaPagamento().getTipo().equals(TipoFormaPagamento.CARTAO_DE_DEBITO.getValor())) &&
					formaPagamentoNegociacaoRecebimentoVO.getConfiguracaoFinanceiroCartaoVO().getPermitiRecebimentoCartaoOnline()
					&& (!cancelamento || (cancelamento && !formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getChaveDaTransacao().isEmpty()))) {
				if (cartoes.containsKey(formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNumeroCartao())) {
					VendaRede vendaExistente = cartoes.get(formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNumeroCartao());
					vendaExistente.setValor(vendaExistente.getValor() + formaPagamentoNegociacaoRecebimentoVO.getValorRecebimento());
					vendaExistente.getFormaPagamentoNegociacaoRecebimentoVOs().add(formaPagamentoNegociacaoRecebimentoVO);
				} else {
					VendaRede novaVenda = montarDadosTransacaoCartaoRede(formaPagamentoNegociacaoRecebimentoVO, nrCartao++);
					vendas.add(novaVenda);
					cartoes.put(formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNumeroCartao(), novaVenda);
				}
			}
		}
		return vendas;
	}
	
	public Sale montarDadosTransacaoCartaoCredito(ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, FormaPagamentoNegociacaoRecebimentoVO obj) throws Exception {
		Sale venda = new Sale("ID do pagamento");
		Payment cartaoCredito = new Payment(0);
		if(obj.getOperadoraCartaoVO().getOperadoraCartaoCreditoEnum().getBandeira() == null) {
			throw new Exception("A Bandeira "+obj.getOperadoraCartaoVO().getOperadoraCartaoCreditoEnum().getValorApresentar()+" não é suportada pelo serviço de pagamento por cartão de crédito.");
		}
		String bandeira = obj.getOperadoraCartaoVO().getOperadoraCartaoCreditoEnum().getBandeira().getDescricao();
//		if (obj.getOperadoraCartaoVO().getOperadoraCartaoCreditoEnum().equals(OperadoraCartaoCreditoEnum.MASTERCARD)) {
//			bandeira = BandeiraCartaoCreditoEnum.Mastercard.getDescricao();
//		} else if (obj.getOperadoraCartaoVO().getOperadoraCartaoCreditoEnum().equals(OperadoraCartaoCreditoEnum.VISA)) {
//			bandeira = BandeiraCartaoCreditoEnum.Visa.getDescricao();
//		} else if (obj.getOperadoraCartaoVO().getOperadoraCartaoCreditoEnum().equals(OperadoraCartaoCreditoEnum.AMEX)) {
//			bandeira = BandeiraCartaoCreditoEnum.Amex.getDescricao();
//		} else if (obj.getOperadoraCartaoVO().getOperadoraCartaoCreditoEnum().equals(OperadoraCartaoCreditoEnum.AURA)) {
//			bandeira = BandeiraCartaoCreditoEnum.Aura.getDescricao();
//		} else if (obj.getOperadoraCartaoVO().getOperadoraCartaoCreditoEnum().equals(OperadoraCartaoCreditoEnum.CASA_SHOW)) {
//			bandeira = BandeiraCartaoCreditoEnum.CasaShow.getDescricao();
//		} else if (obj.getOperadoraCartaoVO().getOperadoraCartaoCreditoEnum().equals(OperadoraCartaoCreditoEnum.ELO)) {
//			bandeira = BandeiraCartaoCreditoEnum.Elo.getDescricao();
//		} else if (obj.getOperadoraCartaoVO().getOperadoraCartaoCreditoEnum().equals(OperadoraCartaoCreditoEnum.DINERS)) {
//			bandeira = BandeiraCartaoCreditoEnum.Diners.getDescricao();
//		} else if (obj.getOperadoraCartaoVO().getOperadoraCartaoCreditoEnum().equals(OperadoraCartaoCreditoEnum.DISCOVER)) {
//			bandeira = BandeiraCartaoCreditoEnum.Discover.getDescricao();
//		}
		cartaoCredito.creditCard(obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getCodigoVerificacao(), bandeira);		
		cartaoCredito.getCreditCard().setExpirationDate(obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getMesValidade() < 10 ? "0"+obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getMesValidade()+"/"+obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getAnoValidade(): obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getMesValidade()+"/"+obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getAnoValidade()); // data de validade MM/YYYY
		cartaoCredito.getCreditCard().setCardNumber(obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNumeroCartao()); // numero do cartao
		cartaoCredito.getCreditCard().setHolder(Uteis.removerAcentuacao(obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNomeCartaoCredito()));// nome do comprador impresso no cartao
		cartaoCredito.setAmount(UteisTexto.converteStringParaInteiro(obj.getValorRecebimento().toString()));
		cartaoCredito.setInstallments(obj.getQtdeParcelasCartaoCredito());	
		if (configuracaoFinanceiroVO.getAmbienteCartaoCreditoEnum().equals(AmbienteCartaoCreditoEnum.PRODUCAO) && !getFacadeFactory().getGerenciamentoDeTransacaoCartaoDeCreditoFacade().validarNumeroCartaoCredito(obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNumeroCartao(), obj.getOperadoraCartaoVO().getNome())) {
			obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setValidarCampoNumeroCartaoCredito(false);
			throw new Exception(UteisJSF.internacionalizar("msg_NumeroCartaoCreditoInvalido"));
		}
		venda.setPayment(cartaoCredito);
		return venda;
	
	}
	
	public VendaRede montarDadosTransacaoCartaoRede(FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO, Integer nrCartao) throws Exception {
		VendaRede venda = new VendaRede();
		venda.getFormaPagamentoNegociacaoRecebimentoVOs().add(formaPagamentoNegociacaoRecebimentoVO);
		venda.setValor(formaPagamentoNegociacaoRecebimentoVO.getValorRecebimento());
		venda.setParcelas(formaPagamentoNegociacaoRecebimentoVO.getQtdeParcelasCartaoCredito());
		String time = String.valueOf(new Date().getTime());
		if(time.length() > 11) {
			time = time.substring(time.length() - 11, time.length());
		}
		String cartao = formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNumeroCartao().substring(formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNumeroCartao().length() - 4);
		String references = time+cartao+nrCartao;
		if(references.length() > 16) {
			references = references.substring(0, 15);
		}
		venda.setReferencia(references);
		venda.setTipoCartao(formaPagamentoNegociacaoRecebimentoVO.getFormaPagamento().getTipo());
		venda.setTid(formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getChaveDaTransacao());
		return venda;
	}
	
}
