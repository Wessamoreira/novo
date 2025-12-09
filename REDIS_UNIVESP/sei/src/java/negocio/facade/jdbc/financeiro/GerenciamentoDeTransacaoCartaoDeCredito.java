package negocio.facade.jdbc.financeiro;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ContaReceberNegociacaoRecebimentoVO;
import negocio.comuns.financeiro.FormaPagamentoNegociacaoRecebimentoVO;
import negocio.comuns.utilitarias.dominios.SituacaoContaReceber;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.GerenciamentoDeTransacaoCartaoDeCreditoInterfaceFacade;

/**
 * 
 * @author Victor Hugo de Paula Costa 27/05/2015
 *
 *         https://github.com/mundipagg/mundipagg-one-java
 *
 */
@Repository
@Scope("singleton")
@Lazy
public class GerenciamentoDeTransacaoCartaoDeCredito extends ControleAcesso implements GerenciamentoDeTransacaoCartaoDeCreditoInterfaceFacade {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

//	@Override
//	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
//	@Deprecated
//	public void realizarTransacaoComCartaoDeCredito(NegociacaoRecebimentoVO negociacaoRecebimentoVO, PessoaVO pessoaVO, ParceiroVO parceiroVO, FornecedorVO fornecedorVO, String identificacaoDaLoja, List<FormaPagamentoNegociacaoRecebimentoVO> formaPagamentoNegociacaoRecebimentoVOs, UsuarioVO usuario) throws Exception {
//		CreateOrderRequest request = new CreateOrderRequest();
//		List<CreditCardTransaction> listaCartaoCreditoTransacao = new ArrayList<CreditCardTransaction>();
//		Double valorTotalTransacao = 0.0;
//		Integer aux = 0;
//		List<FormaPagamentoNegociacaoRecebimentoVO> formaPagamentoNegociacaoRecebimentoVOs2 = new ArrayList<FormaPagamentoNegociacaoRecebimentoVO>();
//		for (FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO : formaPagamentoNegociacaoRecebimentoVOs) {
//			if (formaPagamentoNegociacaoRecebimentoVO.getFormaPagamento().getTipo().equals(TipoFormaPagamento.CARTAO_DE_CREDITO.getValor())) {
//				if (formaPagamentoNegociacaoRecebimentoVO.getConfiguracaoFinanceiroCartaoVO().getPermitiRecebimentoCartaoOnline()) {
//					valorTotalTransacao += formaPagamentoNegociacaoRecebimentoVO.getValorRecebimento();
//					if (!verificarCartaoJaAdicionadoNaLista(listaCartaoCreditoTransacao, formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNumeroCartao())) {
//						listaCartaoCreditoTransacao.add(montarDadosTransacaoCartaoOnline(formaPagamentoNegociacaoRecebimentoVO));
//						aux++;
//						formaPagamentoNegociacaoRecebimentoVOs2.add(formaPagamentoNegociacaoRecebimentoVO);
//					}
//				}
//			}
//		}
//		if (aux.equals(0)) {
//			return;
//		}
//		CreditCardTransaction[] colecaoCartaoCreditoTransacao = new CreditCardTransaction[aux];
//		for (int i = 0; i < aux; i++) {
//			colecaoCartaoCreditoTransacao[i] = listaCartaoCreditoTransacao.get(i);
//		}
//		if (Uteis.isAtributoPreenchido(fornecedorVO)) {
//			// TODO
//		} else if (Uteis.isAtributoPreenchido(parceiroVO)) {
//			// TODO
//		} else {
//			request.setBuyer(montarDadosPessoaValidacaoAntiFraude(pessoaVO));
//		}
//		request.setShoppingCartCollection(montarDadosCarrinhoCompra());
//		request.setAmountInCents((long) (valorTotalTransacao / 0.01));
//		request.setAmountInCentsToConsiderPaid((long) (valorTotalTransacao / 0.01));
//		Random generator = new Random();
//		Integer randomNumber = generator.nextInt(100000);
//		request.setCurrencyIsoEnum(CurrencyIsoEnum.BRL);
//		request.setEmailUpdateToBuyerEnum(EmailUpdateToBuyerEnum.No);
//		request.setMerchantKey(identificacaoDaLoja);
//		request.setCreditCardTransactionCollection(colecaoCartaoCreditoTransacao);
//		request.setOrderReference(randomNumber.toString());
//		MundiPaggClient client = new MundiPaggClient();
//		CreateOrderResponse response = client.createOrder(request);
//		if (response == null || response.getSuccess() == null) {
//			throw new Exception("Aconteceu um erro na transação, provavelmente ocasionado pela falha na conexão com a internet.");
//		}
//		if (response.getCreditCardTransactionResultCollection().length == 1) {
//			if (!response.getCreditCardTransactionResultCollection()[0].getCreditCardTransactionStatusEnum().equals(CreditCardTransactionStatusEnum.Captured)) {
//				realizarCancelamentoCartaoCreditoOperacaoReprovada(response, identificacaoDaLoja);
//				throw new Exception(response.getCreditCardTransactionResultCollection()[0].getAcquirerMessage() + " - Cartão de Número " + response.getCreditCardTransactionResultCollection()[0].getCreditCardNumber());
//			} else {
//				negociacaoRecebimentoVO.setMensagemPagamentoCartaoCredito(response.getCreditCardTransactionResultCollection()[0].getAcquirerMessage() + " - Cartão de Número " + response.getCreditCardTransactionResultCollection()[0].getCreditCardNumber());
//			}
//		} else {
//			int erros = 0;
//			for (int i = 0; i < response.getCreditCardTransactionResultCollection().length; i++) {
//				CreditCardTransactionResult cardTransactionResult = response.getCreditCardTransactionResultCollection()[i];
//				if (cardTransactionResult.getCreditCardTransactionStatusEnum().equals(CreditCardTransactionStatusEnum.Captured)) {
//					negociacaoRecebimentoVO.setMensagemPagamentoCartaoCredito(negociacaoRecebimentoVO.getMensagemPagamentoCartaoCredito() + System.getProperty("line.separator") + cardTransactionResult.getAcquirerMessage() + " - Cartão de Número " + cardTransactionResult.getCreditCardNumber());
//				} else {
//					erros++;
//					negociacaoRecebimentoVO.setMensagemPagamentoCartaoCredito(negociacaoRecebimentoVO.getMensagemPagamentoCartaoCredito() + System.getProperty("line.separator") + cardTransactionResult.getAcquirerMessage() + " - Cartão de Número " + cardTransactionResult.getCreditCardNumber());
//				}
//			}
//			if (erros > 0) {
//				realizarCancelamentoCartaoCreditoOperacaoReprovada(response, identificacaoDaLoja);
//				throw new Exception(negociacaoRecebimentoVO.getMensagemPagamentoCartaoCredito());
//			}
//		}
//		getFacadeFactory().getFormaPagamentoNegociacaoRecebimentoFacade().atualizarFormaPagamentoNegociacaoRecebimentoCartaoCreditoComChaveDeTransacao(formaPagamentoNegociacaoRecebimentoVOs, response, usuario);
//	}

//	public Boolean verificarCartaoJaAdicionadoNaLista(List<CreditCardTransaction> listaCartaoCreditoTransacao, String numeroCartao) throws Exception {
//		for (CreditCardTransaction creditCardTransaction : listaCartaoCreditoTransacao) {
//			if (creditCardTransaction.getCreditCardNumber().equals(numeroCartao)) {
//				return true;
//			}
//		}
//		return false;
//	}

//	public Buyer montarDadosPessoaValidacaoAntiFraude(PessoaVO pessoaVO) throws Exception {
//		Buyer comprador = new Buyer();
//		pessoaVO = getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(pessoaVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, null);
//		if (pessoaVO.getEmail().isEmpty()) {
//			comprador.setEmail("");
//		} else {
//			comprador.setEmail(pessoaVO.getEmail());
//		}
//		if (pessoaVO.getSexo().equals(SexoEnum.M.name())) {
//			comprador.setGenderEnum(GenderEnum.M);
//		} else if (pessoaVO.getSexo().equals(SexoEnum.F.name())) {
//			comprador.setGenderEnum(GenderEnum.F);
//		}
//		comprador.setHomePhone("55" + pessoaVO.getTelefoneRes());
//		comprador.setMobilePhone("55" + pessoaVO.getCelular());
//		comprador.setName(pessoaVO.getNome());
//		comprador.setPersonTypeEnum(PersonTypeEnum.Person);
//		comprador.setTaxDocumentNumber(Uteis.removerMascara(pessoaVO.getCPF()));
//		comprador.setTaxDocumentTypeEnum(TaxDocumentTypeEnum.CPF);
//		if (!pessoaVO.getTelefoneComer().equals("")) {
//			comprador.setWorkPhone("55" + pessoaVO.getTelefoneComer());
//		} else {
//			comprador.setWorkPhone("");
//		}
//		BuyerAddress[] coleçãoDeEndereços = new BuyerAddress[1];
//		BuyerAddress endereco = new BuyerAddress();
//		endereco.setAddressTypeEnum(AddressTypeEnum.Billing);
//		endereco.setCity(pessoaVO.getCidade().getNome());
//		endereco.setStreet(pessoaVO.getEndereco());
//		endereco.setDistrict(pessoaVO.getSetor());
//		endereco.setComplement(pessoaVO.getComplemento());
//		endereco.setNumber(pessoaVO.getNumero());
//		endereco.setCountryEnum(CountryEnum.Brazil);
//		endereco.setState(pessoaVO.getCidade().getEstado().getNome());
//		endereco.setZipCode(Uteis.removerMascara(pessoaVO.getCEP()));
//		coleçãoDeEndereços[0] = endereco;
//		comprador.setBuyerAddressCollection(coleçãoDeEndereços);
//		return comprador;
//	}

//	public CreditCardTransaction montarDadosTransacaoCartaoOnline(FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO) throws Exception {
//		CreditCardTransaction creditCardTransaction = new CreditCardTransaction();
//		if (formaPagamentoNegociacaoRecebimentoVO.getOperadoraCartaoVO().getOperadoraCartaoCreditoEnum().equals(OperadoraCartaoCreditoEnum.MASTERCARD)) {
//			creditCardTransaction.setCreditCardBrandEnum(CreditCardBrandEnum.Mastercard);
//		} else if (formaPagamentoNegociacaoRecebimentoVO.getOperadoraCartaoVO().getOperadoraCartaoCreditoEnum().equals(OperadoraCartaoCreditoEnum.VISA)) {
//			creditCardTransaction.setCreditCardBrandEnum(CreditCardBrandEnum.Visa);
//		} else if (formaPagamentoNegociacaoRecebimentoVO.getOperadoraCartaoVO().getOperadoraCartaoCreditoEnum().equals(OperadoraCartaoCreditoEnum.AMEX)) {
//			creditCardTransaction.setCreditCardBrandEnum(CreditCardBrandEnum.Amex);
//		} else if (formaPagamentoNegociacaoRecebimentoVO.getOperadoraCartaoVO().getOperadoraCartaoCreditoEnum().equals(OperadoraCartaoCreditoEnum.AURA)) {
//			creditCardTransaction.setCreditCardBrandEnum(CreditCardBrandEnum.Aura);
//		} else if (formaPagamentoNegociacaoRecebimentoVO.getOperadoraCartaoVO().getOperadoraCartaoCreditoEnum().equals(OperadoraCartaoCreditoEnum.CASA_SHOW)) {
//			creditCardTransaction.setCreditCardBrandEnum(CreditCardBrandEnum.CasaShow);
//		} else if (formaPagamentoNegociacaoRecebimentoVO.getOperadoraCartaoVO().getOperadoraCartaoCreditoEnum().equals(OperadoraCartaoCreditoEnum.ELO)) {
//			creditCardTransaction.setCreditCardBrandEnum(CreditCardBrandEnum.Elo);
//		} else if (formaPagamentoNegociacaoRecebimentoVO.getOperadoraCartaoVO().getOperadoraCartaoCreditoEnum().equals(OperadoraCartaoCreditoEnum.DINERS)) {
//			creditCardTransaction.setCreditCardBrandEnum(CreditCardBrandEnum.Diners);
//		} else if (formaPagamentoNegociacaoRecebimentoVO.getOperadoraCartaoVO().getOperadoraCartaoCreditoEnum().equals(OperadoraCartaoCreditoEnum.DISCOVER)) {
//			creditCardTransaction.setCreditCardBrandEnum(CreditCardBrandEnum.Discover);
//		}
//		creditCardTransaction.setCreditCardNumber(formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNumeroCartao());
//		creditCardTransaction.setSecurityCode(formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getCodigoVerificacao());
//		creditCardTransaction.setInstallmentCount(1);
//		creditCardTransaction.setCreditCardOperationEnum(CreditCardOperationEnum.AuthOnly);
//		creditCardTransaction.setHolderName(formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNomeCartaoCredito());
//		creditCardTransaction.setExpMonth(formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getMesValidade());
//		creditCardTransaction.setExpYear(formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getAnoValidade());
//		if (formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getTipoFinanciamentoEnum().equals(TipoFinanciamentoEnum.INSTITUICAO)) {
//			Recurrency recorrencia = new Recurrency();
//			Calendar calendar = Calendar.getInstance();
//			calendar.setTime(formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getDataCobranca());
//			recorrencia.setDateToStartBilling(calendar.getInstance());
//			recorrencia.setFrequencyEnum(FrequencyEnum.Monthly);
//			recorrencia.setInterval(1);
//			recorrencia.setRecurrences(formaPagamentoNegociacaoRecebimentoVO.getQtdeParcelasCartaoCredito());
//			creditCardTransaction.setRecurrency(recorrencia);
//			creditCardTransaction.setAmountInCents((long) (formaPagamentoNegociacaoRecebimentoVO.getValorRecebimento() / 0.01));
//		} else {
//			creditCardTransaction.setAmountInCents((long) ((formaPagamentoNegociacaoRecebimentoVO.getValorRecebimento() / 0.01) * formaPagamentoNegociacaoRecebimentoVO.getQtdeParcelasCartaoCredito()));
//			creditCardTransaction.setInstallmentCount(formaPagamentoNegociacaoRecebimentoVO.getQtdeParcelasCartaoCredito());
//		}
//		// PaymentMethodCode 1 para Teste
//		// PaymentMethodCode 20 para Stone
//		creditCardTransaction.setPaymentMethodCode(formaPagamentoNegociacaoRecebimentoVO.getConfiguracaoFinanceiroCartaoVO().getConfiguracaoFinanceiroVO().getAmbienteCartaoCreditoEnum().getKey());
//		return creditCardTransaction;
//	}

//	public ShoppingCart[] montarDadosCarrinhoCompra() throws Exception {
//		ShoppingCart carrinhoDeCompras = new ShoppingCart();
//		carrinhoDeCompras.setFreightCostInCents(0);
//		ShoppingCartItem carrinhoDeComprasItem = new ShoppingCartItem();
//		carrinhoDeComprasItem.setDescription("");
//		carrinhoDeComprasItem.setItemReference("");
//		carrinhoDeComprasItem.setName("");
//		carrinhoDeComprasItem.setQuantity(0);
//		carrinhoDeComprasItem.setTotalCostInCents(0);
//		carrinhoDeComprasItem.setUnitCostInCents(0);
//		ShoppingCartItem[] carrinhoDeComprasItemColecao = new ShoppingCartItem[1];
//		carrinhoDeComprasItemColecao[0] = carrinhoDeComprasItem;
//		carrinhoDeCompras.setShoppingCartItemCollection(carrinhoDeComprasItemColecao);
//		ShoppingCart[] carrinhoDeComprasColecao = new ShoppingCart[1];
//		carrinhoDeComprasColecao[0] = carrinhoDeCompras;
//		return carrinhoDeComprasColecao;
//	}

	@Override
	public Boolean validarNumeroCartaoCredito(String numeroCartaoCredito, String bandeiraCartaoCredito) throws Exception {
		/*if (!numeroCartaoCredito.equals("")) {
			// cartoes valido Cielo para Ambiente de Teste.
			if(numeroCartaoCredito.equals("0000000000000001") //Operação realizada com sucesso
					|| numeroCartaoCredito.equals("0000000000000002") // Não Autorizada
					|| numeroCartaoCredito.equals("0000000000000003") // Cartão Expirado
					|| numeroCartaoCredito.equals("0000000000000004") // Operação realizada com sucesso
					|| numeroCartaoCredito.equals("0000000000000005") // Cartão Bloqueado
					|| numeroCartaoCredito.equals("0000000000000006") // Time Out
					|| numeroCartaoCredito.equals("0000000000000007") // Cartão Cancelado
					|| numeroCartaoCredito.equals("0000000000000008") // Problemas com o Cartão de Crédito
					|| numeroCartaoCredito.equals("0000000000000009") // Operation Successful / Time Out
					) {
				return true;
			}
			return numeroCartaoCredito.matches("^((4\\d{3})|(5[1-5]\\d{2})|(6011)|(7\\d{3}))-?\\d{4}-?\\d{4}-?\\d{4}|3[4,7]\\d{13}$");
		}*/
		return true;
	}

//	@Override
//	public void realizarCancelamentoCartaoCredito(List<FormaPagamentoNegociacaoRecebimentoVO> formaPagamentoNegociacaoRecebimentoVOs, String identificacaoDaLoja) throws Exception {
//		MundiPaggClient client = new MundiPaggClient();
//		ManageOrderRequest request = new ManageOrderRequest();
//		request.setMerchantKey(identificacaoDaLoja);
//		/**
//		 * Void para cancelamento ou estorno
//		 */
//		request.setManageOrderOperationEnum(ManageOrderOperationEnum.Void);
//		Integer aux = 0;
//		List<ManageCreditCardTransactionRequest> gerenciadorTransacaoCartaCreditos = new ArrayList<ManageCreditCardTransactionRequest>();
//		for (FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO : formaPagamentoNegociacaoRecebimentoVOs) {
//			if (formaPagamentoNegociacaoRecebimentoVO.getFormaPagamento().getTipo().equals(TipoFormaPagamento.CARTAO_DE_CREDITO.getValor())) {
//				if (formaPagamentoNegociacaoRecebimentoVO.getConfiguracaoFinanceiroCartaoVO().getPermitiRecebimentoCartaoOnline()) {
//					ManageCreditCardTransactionRequest gerenciadorTransacaoCartaCredito = new ManageCreditCardTransactionRequest();
//					gerenciadorTransacaoCartaCredito.setTransactionKey(formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getChaveDaTransacao());
//					request.setOrderKey(formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNumeroReciboTransacao());
//					gerenciadorTransacaoCartaCreditos.add(gerenciadorTransacaoCartaCredito);
//					aux++;
//				}
//			}
//		}
//		if (aux.equals(0)) {
//			return;
//		}
//		ManageCreditCardTransactionRequest[] colecaoGerenciadorTransacaoCartaCredito = new ManageCreditCardTransactionRequest[aux];
//		for (int i = 0; i < aux; i++) {
//			colecaoGerenciadorTransacaoCartaCredito[i] = gerenciadorTransacaoCartaCreditos.get(i);
//		}
//		request.setManageCreditCardTransactionCollection(colecaoGerenciadorTransacaoCartaCredito);
//		client.manageOrder(request);
//	}

//	public void realizarCancelamentoCartaoCreditoOperacaoReprovada(CreateOrderResponse response, String identificacaoDaLoja) throws Exception {
//		MundiPaggClient client = new MundiPaggClient();
//		ManageOrderRequest request = new ManageOrderRequest();
//		request.setMerchantKey(identificacaoDaLoja);
//		/**
//		 * Void para cancelamento ou estorno
//		 */
//		request.setManageOrderOperationEnum(ManageOrderOperationEnum.Void);
//		Integer aux = 0;
//		List<ManageCreditCardTransactionRequest> gerenciadorTransacaoCartaCreditos = new ArrayList<ManageCreditCardTransactionRequest>();
//		for (int i = 0; i < response.getCreditCardTransactionResultCollection().length; i++) {
//			CreditCardTransactionResult cardTransactionResult = response.getCreditCardTransactionResultCollection()[i];
//			ManageCreditCardTransactionRequest gerenciadorTransacaoCartaCredito = new ManageCreditCardTransactionRequest();
//			gerenciadorTransacaoCartaCredito.setTransactionKey(cardTransactionResult.getTransactionKey());
//			request.setOrderKey(response.getOrderKey());
//			gerenciadorTransacaoCartaCreditos.add(gerenciadorTransacaoCartaCredito);
//			aux++;
//		}
//		if (aux.equals(0)) {
//			return;
//		}
//		ManageCreditCardTransactionRequest[] colecaoGerenciadorTransacaoCartaCredito = new ManageCreditCardTransactionRequest[aux];
//		for (int i = 0; i < aux; i++) {
//			colecaoGerenciadorTransacaoCartaCredito[i] = gerenciadorTransacaoCartaCreditos.get(i);
//		}
//		request.setManageCreditCardTransactionCollection(colecaoGerenciadorTransacaoCartaCredito);
//		client.manageOrder(request);
//	}

//	public List<webservice.mundipagg.v2.DataContracts.CreditCardTransaction.CreditCardTransaction> montarDadosTransacao(NegociacaoRecebimentoVO negociacaoRecebimentoVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, List<FormaPagamentoNegociacaoRecebimentoVO> formaPagamentoNegociacaoRecebimentoVOs) throws Exception {
//		List<webservice.mundipagg.v2.DataContracts.CreditCardTransaction.CreditCardTransaction> listaCartaoCreditoTransacao = new ArrayList<webservice.mundipagg.v2.DataContracts.CreditCardTransaction.CreditCardTransaction>();
//	//	List<FormaPagamentoNegociacaoRecebimentoVO> formaPagamentoNegociacaoRecebimentoVOs2 = new ArrayList<FormaPagamentoNegociacaoRecebimentoVO>();
//		for (FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO : formaPagamentoNegociacaoRecebimentoVOs) {
//			formaPagamentoNegociacaoRecebimentoVO.setNegociacaoRecebimentoVO(negociacaoRecebimentoVO);
//			if (formaPagamentoNegociacaoRecebimentoVO.getFormaPagamento().getTipo().equals(TipoFormaPagamento.CARTAO_DE_CREDITO.getValor()) && formaPagamentoNegociacaoRecebimentoVO.getConfiguracaoFinanceiroCartaoVO().getPermitiRecebimentoCartaoOnline() && formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getTipoFinanciamentoEnum().equals(TipoFinanciamentoEnum.OPERADORA)) {
//				if (!verificarCartaoJaAdicionadoNaListaV2(listaCartaoCreditoTransacao, formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNumeroCartao())) {
//					
//					//para cada forma de pagamento( onde esta os dados do cartao ) esse metodo ira motar um objeto para a transacao com a operadora.
//					listaCartaoCreditoTransacao.add(montarDadosTransacaoCartaoOnlineV2(configuracaoFinanceiroVO, formaPagamentoNegociacaoRecebimentoVO));
//				//	formaPagamentoNegociacaoRecebimentoVOs2.add(formaPagamentoNegociacaoRecebimentoVO);
//				}
//			} else if (formaPagamentoNegociacaoRecebimentoVO.getFormaPagamento().getTipo().equals(TipoFormaPagamento.CARTAO_DE_CREDITO.getValor()) && formaPagamentoNegociacaoRecebimentoVO.getConfiguracaoFinanceiroCartaoVO().getPermitiRecebimentoCartaoOnline() && formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getTipoFinanciamentoEnum().equals(TipoFinanciamentoEnum.INSTITUICAO)) {
//					listaCartaoCreditoTransacao.add(montarDadosTransacaoCartaoOnlineV2(configuracaoFinanceiroVO, formaPagamentoNegociacaoRecebimentoVO));
//			//	formaPagamentoNegociacaoRecebimentoVOs2.add(formaPagamentoNegociacaoRecebimentoVO);
//			}
//		}
//		return listaCartaoCreditoTransacao;
//	}

//	public webservice.mundipagg.v2.DataContracts.CreditCardTransaction.CreditCardTransaction montarDadosTransacaoCartaoOnlineV2(ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO) throws Exception {
//		//vai ser minha venda (Sale)
//		webservice.mundipagg.v2.DataContracts.CreditCardTransaction.CreditCardTransaction creditCardTransaction = new webservice.mundipagg.v2.DataContracts.CreditCardTransaction.CreditCardTransaction();
//		creditCardTransaction.setCreditCardOperation(webservice.mundipagg.v2.EnumTypes.CreditCardOperationEnum.AuthAndCapture);
//		//vai ser meu cartao de credito (Payment)
//		CreditCard creditCard = new CreditCard();
//		if (formaPagamentoNegociacaoRecebimentoVO.getOperadoraCartaoVO().getOperadoraCartaoCreditoEnum().equals(OperadoraCartaoCreditoEnum.MASTERCARD)) {
//			creditCard.setCreditCardBrand(webservice.mundipagg.v2.EnumTypes.CreditCardBrandEnum.Mastercard);
//		} else if (formaPagamentoNegociacaoRecebimentoVO.getOperadoraCartaoVO().getOperadoraCartaoCreditoEnum().equals(OperadoraCartaoCreditoEnum.VISA)) {
//			creditCard.setCreditCardBrand(webservice.mundipagg.v2.EnumTypes.CreditCardBrandEnum.Visa);
//		} else if (formaPagamentoNegociacaoRecebimentoVO.getOperadoraCartaoVO().getOperadoraCartaoCreditoEnum().equals(OperadoraCartaoCreditoEnum.AMEX)) {
//			creditCard.setCreditCardBrand(webservice.mundipagg.v2.EnumTypes.CreditCardBrandEnum.Amex);
//		} else if (formaPagamentoNegociacaoRecebimentoVO.getOperadoraCartaoVO().getOperadoraCartaoCreditoEnum().equals(OperadoraCartaoCreditoEnum.AURA)) {
//			creditCard.setCreditCardBrand(webservice.mundipagg.v2.EnumTypes.CreditCardBrandEnum.Aura);
//		} else if (formaPagamentoNegociacaoRecebimentoVO.getOperadoraCartaoVO().getOperadoraCartaoCreditoEnum().equals(OperadoraCartaoCreditoEnum.CASA_SHOW)) {
//			creditCard.setCreditCardBrand(webservice.mundipagg.v2.EnumTypes.CreditCardBrandEnum.CasaShow);
//		} else if (formaPagamentoNegociacaoRecebimentoVO.getOperadoraCartaoVO().getOperadoraCartaoCreditoEnum().equals(OperadoraCartaoCreditoEnum.ELO)) {
//			creditCard.setCreditCardBrand(webservice.mundipagg.v2.EnumTypes.CreditCardBrandEnum.Elo);
//		} else if (formaPagamentoNegociacaoRecebimentoVO.getOperadoraCartaoVO().getOperadoraCartaoCreditoEnum().equals(OperadoraCartaoCreditoEnum.DINERS)) {
//			creditCard.setCreditCardBrand(webservice.mundipagg.v2.EnumTypes.CreditCardBrandEnum.Diners);
//		} else if (formaPagamentoNegociacaoRecebimentoVO.getOperadoraCartaoVO().getOperadoraCartaoCreditoEnum().equals(OperadoraCartaoCreditoEnum.DISCOVER)) {
//			creditCard.setCreditCardBrand(webservice.mundipagg.v2.EnumTypes.CreditCardBrandEnum.Discover);
//		}
//		creditCard.setCreditCardNumber(formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNumeroCartao());
//		creditCard.setSecurityCode(formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getCodigoVerificacao());
//		creditCard.setHolderName(formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNomeCartaoCredito());
//		creditCard.setExpMonth(formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getMesValidade());
//		creditCard.setExpYear(formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getAnoValidade());
//	
//		//revisar esse metodo na parte que se dis respeito ao parcelamento pela instituicao ou operadora
//		if (formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getTipoFinanciamentoEnum().equals(TipoFinanciamentoEnum.INSTITUICAO)) {
//			webservice.mundipagg.v2.DataContracts.Recurrency.Recurrency recorrencia = new webservice.mundipagg.v2.DataContracts.Recurrency.Recurrency();
//			// data que deve ser cobrada da primeira recorrencia, geralmente no dia da venda.
//			recorrencia.setDateToStartBilling(formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getDataCobranca());
//			// o tipo da recorrencia ( dia, mes , ano )
//			recorrencia.setFrequency(webservice.mundipagg.v2.EnumTypes.FrequencyEnum.Monthly);
//			// de quanto em quanto tempo de ser feito a recorrencia (com intervalor de 1 ... um ames ou um dia ou um ano)
//			recorrencia.setInterval(1);
//			//aparentemente quando eh uma venda com varias parcelas, aqui deve ser pasado a quantidade de parcelas( que vai ser quantas vezes a venda vai ser "repetida" )
//			recorrencia.setRecurrences(formaPagamentoNegociacaoRecebimentoVO.getQtdeParcelasCartaoCredito());
//			creditCardTransaction.setRecurrency(recorrencia);
//			//seta o valor da transacao
//			creditCardTransaction.setAmountInCents((long) (formaPagamentoNegociacaoRecebimentoVO.getValorRecebimento() / 0.01));
//		} else {
//			//seta o valor da transacao
//			creditCardTransaction.setAmountInCents((long) ((formaPagamentoNegociacaoRecebimentoVO.getValorRecebimento() / 0.01) * formaPagamentoNegociacaoRecebimentoVO.getQtdeParcelasCartaoCredito()));
//			creditCardTransaction.setInstallmentCount(formaPagamentoNegociacaoRecebimentoVO.getQtdeParcelasCartaoCredito());
//		}
//		creditCardTransaction.setOptions(new CreditCardTransactionOptions());
//		// PaymentMethodCode 1 para Teste
//		// PaymentMethodCode 20 para Stone
//		// PaymentMethodCode 0 Adquerinte Padrão na mundipagg
//		creditCardTransaction.getOptions().setPaymentMethodCode(configuracaoFinanceiroVO.getAmbienteCartaoCreditoEnum().getKey());
//		creditCardTransaction.setCreditCard(creditCard);
//		return creditCardTransaction;
//	}

//	public Boolean verificarCartaoJaAdicionadoNaListaV2(List<webservice.mundipagg.v2.DataContracts.CreditCardTransaction.CreditCardTransaction> listaCartaoCreditoTransacao, String numeroCartao) throws Exception {
//		for (webservice.mundipagg.v2.DataContracts.CreditCardTransaction.CreditCardTransaction creditCardTransaction : listaCartaoCreditoTransacao) {
//			if (creditCardTransaction.getCreditCard().getCreditCardNumber().equals(numeroCartao)) {
//				return true;
//			}
//		}
//		return false;
//	}

//	public webservice.mundipagg.v2.DataContracts.ShoppingCart.ShoppingCart montarDadosCarrinhoCompraV2(NegociacaoRecebimentoVO negociacaoRecebimentoVO) throws Exception {
//		// Cria um item para o carrinho de compras
//		webservice.mundipagg.v2.DataContracts.ShoppingCart.ShoppingCartItem shoppingCartItem = new webservice.mundipagg.v2.DataContracts.ShoppingCart.ShoppingCartItem();
//		shoppingCartItem.setItemReference(negociacaoRecebimentoVO.getCodigo().toString());
//		shoppingCartItem.setName(negociacaoRecebimentoVO.getTipoOrigemContaReceber().getDescricao());
//		shoppingCartItem.setDescription(negociacaoRecebimentoVO.getTipoOrigemContaReceber().getDescricao());
//		shoppingCartItem.setQuantity(1);
//		shoppingCartItem.setTotalCostInCents(Uteis.arredondarDivisaoEntreNumeros(negociacaoRecebimentoVO.getValorTotalRecebimento() / 0.01));
//		shoppingCartItem.setUnitCostInCents(Uteis.arredondarDivisaoEntreNumeros(negociacaoRecebimentoVO.getValorTotalRecebimento() / 0.01));
//		// Cria carrinho de compras e adiciona a coleção de itens o item
//		// definido acima
//		webservice.mundipagg.v2.DataContracts.ShoppingCart.ShoppingCart shoppingCart = new webservice.mundipagg.v2.DataContracts.ShoppingCart.ShoppingCart();
//		shoppingCart.setShoppingCartItemCollection(new ArrayList<webservice.mundipagg.v2.DataContracts.ShoppingCart.ShoppingCartItem>());
//		shoppingCart.getShoppingCartItemCollection().add(shoppingCartItem);
//		return shoppingCart;
//	}

//	public webservice.mundipagg.v2.DataContracts.Person.Buyer montarDadosPessoaValidacaoAntiFraudeV2(PessoaVO pessoaVO) throws Exception {
//		webservice.mundipagg.v2.DataContracts.Person.Buyer comprador = new webservice.mundipagg.v2.DataContracts.Person.Buyer();
//		pessoaVO = getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(pessoaVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, null);
//		if (pessoaVO.getEmail().isEmpty()) {
//			comprador.setEmail("");
//		} else {
//			comprador.setEmailType(EmailTypeEnum.Comercial);
//			comprador.setEmail(pessoaVO.getEmail());
//		}
//		if (pessoaVO.getSexo().equals(SexoEnum.M.name())) {
//			comprador.setGender(webservice.mundipagg.v2.EnumTypes.GenderEnum.M);
//		} else if (pessoaVO.getSexo().equals(SexoEnum.F.name())) {
//			comprador.setGender(webservice.mundipagg.v2.EnumTypes.GenderEnum.F);
//		}
//		comprador.setHomePhone("55" + pessoaVO.getTelefoneRes());
//		comprador.setMobilePhone("55" + pessoaVO.getCelular());
//		comprador.setName(pessoaVO.getNome());
//		comprador.setPersonType(webservice.mundipagg.v2.EnumTypes.PersonTypeEnum.Person);
//		comprador.setDocumentNumber(Uteis.removerMascara(pessoaVO.getCPF()));
//		comprador.setDocumentType(DocumentTypeEnum.CPF);
//		if (!pessoaVO.getTelefoneComer().equals("")) {
//			comprador.setWorkPhone("55" + pessoaVO.getTelefoneComer());
//		} else {
//			comprador.setWorkPhone("");
//		}
//		return comprador;
//	}

//	public webservice.mundipagg.v2.DataContracts.Person.Buyer montarDadosPessoaValidacaoAntiFraudeV2(ParceiroVO parceiroVO) throws Exception {
//		webservice.mundipagg.v2.DataContracts.Person.Buyer comprador = new webservice.mundipagg.v2.DataContracts.Person.Buyer();
//		parceiroVO = getFacadeFactory().getParceiroFacade().consultarPorChavePrimaria(parceiroVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, null);
//		if (parceiroVO.getEmail().isEmpty()) {
//			comprador.setEmail("");
//		} else {
//			comprador.setEmailType(EmailTypeEnum.Comercial);
//			comprador.setEmail(parceiroVO.getEmail());
//		}
//		comprador.setHomePhone("55" + parceiroVO.getTelefones());
//		comprador.setMobilePhone("55" + parceiroVO.getTelefones());
//		comprador.setName(parceiroVO.getNome());
//		comprador.setPersonType(webservice.mundipagg.v2.EnumTypes.PersonTypeEnum.Person);
//		if (parceiroVO.getParceiroJuridico()) {
//			comprador.setDocumentNumber(Uteis.removerMascara(parceiroVO.getCNPJ()));
//			comprador.setDocumentType(DocumentTypeEnum.CNPJ);
//		} else {
//			comprador.setDocumentNumber(Uteis.removerMascara(parceiroVO.getCPF()));
//			comprador.setDocumentType(DocumentTypeEnum.CPF);
//		}
//		return comprador;
//	}

//	public webservice.mundipagg.v2.DataContracts.Person.Buyer montarDadosPessoaValidacaoAntiFraudeV2(FornecedorVO fornecedorVO) throws Exception {
//		webservice.mundipagg.v2.DataContracts.Person.Buyer comprador = new webservice.mundipagg.v2.DataContracts.Person.Buyer();
//		fornecedorVO = getFacadeFactory().getFornecedorFacade().consultarPorChavePrimaria(fornecedorVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, null);
//		if (fornecedorVO.getEmail().isEmpty()) {
//			comprador.setEmail("");
//		} else {
//			comprador.setEmailType(EmailTypeEnum.Comercial);
//			comprador.setEmail(fornecedorVO.getEmail());
//		}
//		comprador.setHomePhone("55" + fornecedorVO.getTelefones());
//		comprador.setMobilePhone("55" + fornecedorVO.getTelefones());
//		comprador.setName(fornecedorVO.getNome());
//		comprador.setPersonType(webservice.mundipagg.v2.EnumTypes.PersonTypeEnum.Person);
//		if (fornecedorVO.getCNPJ().equals("")) {
//			comprador.setDocumentNumber(Uteis.removerMascara(fornecedorVO.getCPF()));
//			comprador.setDocumentType(DocumentTypeEnum.CPF);
//		} else {
//			comprador.setDocumentNumber(Uteis.removerMascara(fornecedorVO.getCNPJ()));
//			comprador.setDocumentType(DocumentTypeEnum.CPF);
//		}
//		return comprador;
//	}

	@Override
	public void processarRecebimentoCartaoCreditoDCC(ContaReceberNegociacaoRecebimentoVO contaReceberNegociacaoRecebimentoVO) throws Exception {
		int processados = 0;
		for (FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO : contaReceberNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoVOs()) {
			formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setFormaPagamentoNegociacaoRecebimentoVO(formaPagamentoNegociacaoRecebimentoVO);
			formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setContaReceberNegociacaoRecebimentoVO(contaReceberNegociacaoRecebimentoVO);
			//realizarConsultaStatusRecebimentoCartaoCreditoV2(formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO(), UUID.fromString(formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNumeroReciboTransacao()), formaPagamentoNegociacaoRecebimentoVO.getConfiguracaoRecebimentoCartaoOnlineVO().getChaveAutenticacao(), null);
			if (formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getSituacao().equals(SituacaoContaReceber.RECEBIDO.getValor())) {
				processados++;
				continue;
			} else if (formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getSituacao().equals(SituacaoContaReceber.RECUSADO.getValor())) {
				//realizarCancelamentoCartaoCreditoV2(null, contaReceberNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoVOs(), formaPagamentoNegociacaoRecebimentoVO.getConfiguracaoRecebimentoCartaoOnlineVO().getChaveAutenticacao(), null);
				getFacadeFactory().getContaReceberFacade().alterarSituacaoPagoDCCFalso(contaReceberNegociacaoRecebimentoVO.getContaReceber().getCodigo());
				return;
			} 
		}
		if(processados > 0) {
			contaReceberNegociacaoRecebimentoVO.setContasProcessadasDCC(true);
		}
	}
	
	/**
	 * 
	 * O cancelamento é realizado até 23 hs e 59 min do dia da transação. Após este período é realizado o estorno da transação. 
	 * Em termos práticos, o cancelamento não aparece na fatura do consumidor enquanto o estorno aparecerá. 
	 * Na Mundi ambos são feitos pelo mesmo método, pois queremos deixar tudo mais simples para você!
	 * 
	 * Para adquirente Redecard, o estorno não é possível, apenas entrando em contato com a mesma.
	 * 
	 * Para cancelamentos pendentes (PendingVoid and PendingRefund), o SEI deverá entrar em contato com a adquirente para realizar o estorno/cancelamento.
	 * 
	 * @author Victor Hugo de Paula Costa - 1 de jun de 2016 
	 * @param formaPagamentoNegociacaoRecebimentoVO
	 * @param usuarioVO
	 * @throws Exception
	 */
	@Override
	public void realizarCancelamentoOuEstornoTransacaoEspecificaCartaoCredito(FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO, UsuarioVO usuarioVO) throws Exception {
		// Define loja 
		/*UUID merchantKey = UUID.fromString(formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getConfiguracaoFinanceiroCartaoVO().getConfiguracaoFinanceiroVO().getAmbienteCartaoCreditoEnum().equals(AmbienteCartaoCreditoEnum.HOMOLOGACAO) ? "c26ab2b0-96e6-4f2a-87ba-9adc8dd8ab90" : formaPagamentoNegociacaoRecebimentoVO.getConfiguracaoRecebimentoCartaoOnlineVO().getChaveAutenticacao()); // Chave da Loja - MerchantKey
		// Cria o cliente que vai efetuar a operação
		GatewayServiceClient serviceClient = new GatewayServiceClient(merchantKey, formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getConfiguracaoFinanceiroCartaoVO().getConfiguracaoFinanceiroVO().getAmbienteCartaoCreditoEnum().equals(AmbienteCartaoCreditoEnum.HOMOLOGACAO)?PlatformEnvironmentEnum.Sandbox:PlatformEnvironmentEnum.Production);
		ManageSaleRequest manageSaleRequest = new ManageSaleRequest();
		// Define os detalhes da transação de cartão de crédito
		ManageCreditCardTransaction manageCreditCardTransaction = new ManageCreditCardTransaction();
		manageCreditCardTransaction.setAmountInCents((long) (formaPagamentoNegociacaoRecebimentoVO.getValorRecebimento() / 0.01));
		manageCreditCardTransaction.setTransactionKey(UUID.fromString(formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getChaveDaTransacao()));
		// Cria a lista de transações de cartão e adiciona a transação criada a lista
		List<ManageCreditCardTransaction> manageCreditCardTransactionList = new ArrayList<ManageCreditCardTransaction>();
		manageCreditCardTransactionList.add(manageCreditCardTransaction);
		// Define a chave do pedido que será cancelado
		manageSaleRequest.setOrderKey(UUID.fromString(formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNumeroReciboTransacao()));
		manageSaleRequest.setCreditCardTransactionCollection(manageCreditCardTransactionList);
		// Submete a requisição de cancelamento
		HttpResponseGenerics<ManageSaleResponse, ManageSaleRequest> httpResponse = serviceClient.getSale().Manage(ManageOperationEnum.Cancel, manageSaleRequest);
		SituacaoTransacaoEnum situacaoTransacaoEnum = SituacaoTransacaoEnum.CANCELADO;
		if(!httpResponse.getResponse().getCreditCardTransactionResultCollection().isEmpty()) {
			for (webservice.mundipagg.v2.DataContracts.CreditCardTransaction.CreditCardTransactionResult creditCardTransactionResult : httpResponse.getResponse().getCreditCardTransactionResultCollection()) {
				if(creditCardTransactionResult.getCreditCardTransactionStatus().equals(webservice.mundipagg.v2.EnumTypes.CreditCardTransactionStatusEnum.PendingVoid)) {
					situacaoTransacaoEnum = SituacaoTransacaoEnum.CANCELAMENTO_PENDENTE;
					formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setSituacao(SituacaoContaReceber.CANCELAMENTO_PENDENTE.getValor());
				} else if(creditCardTransactionResult.getCreditCardTransactionStatus().equals(webservice.mundipagg.v2.EnumTypes.CreditCardTransactionStatusEnum.PendingRefund)) {
					situacaoTransacaoEnum = SituacaoTransacaoEnum.ESTORNO_PENDENTE;
					formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setSituacao(SituacaoContaReceber.ESTORNO_PENDENTE.getValor());
				} else if(creditCardTransactionResult.getCreditCardTransactionStatus().equals(webservice.mundipagg.v2.EnumTypes.CreditCardTransactionStatusEnum.Voided) || creditCardTransactionResult.getCreditCardTransactionStatus().equals(webservice.mundipagg.v2.EnumTypes.CreditCardTransactionStatusEnum.Refunded)) {
					situacaoTransacaoEnum = SituacaoTransacaoEnum.CANCELADO;
					formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setSituacao(SituacaoContaReceber.CANCELADO_FINANCEIRO.getValor());
				}
			}
			getFacadeFactory().getTransacaoCartaoOnlineInterfaceFacade().incluirLogTransacaoCartaoOnline(httpResponse.getRawRequest(), httpResponse.getRawResponse(), httpResponse.getResponse().getRequestKey().toString(), formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getChaveDaTransacao(), "", formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO(), formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getContaReceberRecebimento(), situacaoTransacaoEnum, formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getMascaraNumeroCartao(), formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNumeroParcela(), formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getValorParcela(), formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getDataVencimento(), usuarioVO);			
		} else {
			getFacadeFactory().getTransacaoCartaoOnlineInterfaceFacade().incluirLogTransacaoCartaoOnline(httpResponse.getRawRequest(), httpResponse.getRawResponse(), httpResponse.getResponse().getRequestKey().toString(), formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getChaveDaTransacao(), "", formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO(), formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getContaReceberRecebimento(), situacaoTransacaoEnum, formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getMascaraNumeroCartao(), formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNumeroParcela(), formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getValorParcela(), formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getDataVencimento(), usuarioVO);			
			throw new Exception("Aconteceu um erro ao cancelar essa transação. Procure o administrador do sistema.");
		}*/
	}
}
