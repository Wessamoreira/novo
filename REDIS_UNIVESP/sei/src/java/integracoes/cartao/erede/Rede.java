package integracoes.cartao.erede;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;

import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoFormaPagamento;

public class Rede {
	
	/**
	 * 
	 * @link https://github.com/DevelopersRede/erede-java
	 * @link https://www.userede.com.br/desenvolvedores/pt/produto/e-Rede#introducao
	 * 
	 * @changed
	 * if (status < 200 || status >= 400) {
     *   RedeError redeError = new RedeError(transactionResponse.getReturnCode(), transactionResponse.getReturnMessage());
     *   throw new RedeException(httpResponse.getStatusLine().toString(), redeError, transactionResponse);
     * }
     * 
	 */
	
	public static void consultar(Store loja, String tid) throws Exception {
		TransactionResponse transaction = new eRede(loja).get(tid);
		System.out.println("Message: " + transaction.getReturnMessage()); // Mensagem de retorno da transação
		System.out.println("Ammount: " + transaction.getAmount()); // Valor total da compra sem separador de milhar. Exemplo: 1000 = R$10,00
		if (transaction.getAuthorization() != null) {
			System.out.println("Status: " + transaction.getAuthorization().getStatus()); // Status da transação (Approved, Denied, Canceled, Pending)
		}
		System.out.println("AuthorizationCode: " + transaction.getAuthorizationCode()); // Número de Autorização da transação retornada pelo emissor do cartão
	}
	
	public static void consultarPorReferencia(Store loja, String referencia) throws Exception {
		TransactionResponse transaction = new eRede(loja).getByReference(referencia);
		System.out.println("Message: " + transaction.getReturnMessage()); // Mensagem de retorno da transação
		System.out.println("Ammount: " + transaction.getAmount()); // Valor total da compra sem separador de milhar. Exemplo: 1000 = R$10,00
		if (transaction.getAuthorization() != null) {
			System.out.println("Status: " + transaction.getAuthorization().getStatus()); // Status da transação (Approved, Denied, Canceled, Pending)
		}
		System.out.println("AuthorizationCode: " + transaction.getAuthorizationCode()); // Número de Autorização da transação retornada pelo emissor do cartão
	}
	
	public static TransactionResponse autorizar(Store loja, VendaRede venda) throws Exception {
		Transaction transaction = new Transaction(venda.getValor(), venda.getReferencia());
		if (venda.getParcelas() > 1) {
			transaction.setInstallments(venda.getParcelas());
		}
		String tipo = "";
		if (venda.getTipoCartao().equals(TipoFormaPagamento.CARTAO_DE_DEBITO.getValor())) {
			tipo = Transaction.DEBIT;
		} else if (venda.getTipoCartao().equals(TipoFormaPagamento.CARTAO_DE_CREDITO.getValor())) {
			tipo = Transaction.CREDIT;
		}
		if (Transaction.CREDIT.equals(tipo)) {
			transaction.creditCard(venda.getFormaPagamentoNegociacaoRecebimentoVOs().get(0).getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNumeroCartao(),
					venda.getFormaPagamentoNegociacaoRecebimentoVOs().get(0).getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getCodigoVerificacao(),
					Uteis.getPreencherComZeroEsquerda(venda.getFormaPagamentoNegociacaoRecebimentoVOs().get(0).getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getMesValidade().toString(), 2),
					venda.getFormaPagamentoNegociacaoRecebimentoVOs().get(0).getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getAnoValidade().toString(),
					Uteis.removerAcentuacao(venda.getFormaPagamentoNegociacaoRecebimentoVOs().get(0).getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNomeCartaoCredito()));
		} else if (Transaction.DEBIT.equals(tipo)) {
			transaction.debitCard(venda.getFormaPagamentoNegociacaoRecebimentoVOs().get(0).getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNumeroCartao(),
					venda.getFormaPagamentoNegociacaoRecebimentoVOs().get(0).getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getCodigoVerificacao(),
					Uteis.getPreencherComZeroEsquerda(venda.getFormaPagamentoNegociacaoRecebimentoVOs().get(0).getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getMesValidade().toString(), 2),
					venda.getFormaPagamentoNegociacaoRecebimentoVOs().get(0).getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getAnoValidade().toString(),
					Uteis.removerAcentuacao(venda.getFormaPagamentoNegociacaoRecebimentoVOs().get(0).getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNomeCartaoCredito()));
			ThreeDSecure ds = new ThreeDSecure();
			HttpServletRequest request =(HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
			ds.setUserAgent(request.getHeader("user-agent"));
			transaction.addUrl("https://portal.otimize-ti.com.br/rede/success", Url.THREE_D_SECURE_SUCCESS);
			transaction.addUrl("https://portal.otimize-ti.com.br/rede/failure", Url.THREE_D_SECURE_FAILURE);
			transaction.setThreeDSecure(ds);
		} else {
			throw new Exception("Tipo de cartão inválido");
		}
		String requisicao = new Gson().toJson(transaction, Transaction.class);
		venda.setRequisicao(requisicao);
		TransactionResponse transactionResponse = new eRede(loja).create(transaction);
		String resposta = new Gson().toJson(transactionResponse, TransactionResponse.class);
		venda.setResposta(resposta);
		return transactionResponse;
	}
	
	/**
	 * Cancelamento pode ser realizado em até 7 dias para transações de débito e pode variar para transações de crédito.
	 * Cancelamentos solicitados no mesmo dia da transação de autorização ou autorização com captura automática,
	 * o processamento será realizado imediatamente, caso contrário, o processamento será realizado em D+1.
	 */
	public static TransactionResponse cancelar(Store loja, VendaRede venda) throws Exception {
		Transaction transaction = new Transaction(venda.getValor(), venda.getReferencia());
		transaction.setTid(venda.getTid());
		String requisicao = new Gson().toJson(transaction, Transaction.class);
		venda.setRequisicaoCancelamento(requisicao);
		TransactionResponse transactionResponse = new eRede(loja).cancel(transaction);
		String resposta = new Gson().toJson(transactionResponse, TransactionResponse.class);
		venda.setRespostaCancelamento(resposta);
		return transactionResponse;
	}
	
	public static void persistirTransacaoRede(Transaction transaction, TransactionResponse transactionResponse) throws Exception {
		if ("debit".equals(transaction.getKind())) {
			System.out.println("Code: " + transactionResponse.getReturnCode());
			System.out.println("Message: " + transactionResponse.getReturnMessage());
			System.out.println("DateTime: " + transactionResponse.getDateTime()); // Data da Transação
			System.out.println("URL Redirect: " + transactionResponse.getThreeDSecure().getUrl());
		} else {
			System.out.println("Code: " + transactionResponse.getReturnCode());
			System.out.println("Message: " + transactionResponse.getReturnMessage());
			System.out.println("Reference: " + transactionResponse.getReference()); // Número do Pedido
			System.out.println("Tid: " + transactionResponse.getTid()); // Número identificador único da transação
			System.out.println("AuthorizationCode: " + transactionResponse.getAuthorizationCode()); // Número de Autorização da transação retornada pelo emissor do cartão
			System.out.println("DateTime: " + transactionResponse.getDateTime()); // Data da Transação
			System.out.println("RefundId: " + transactionResponse.getRefundId()); // Código de retorno da solicitação de cancelamento gerado pela Rede
			System.out.println("RefundDateTime: " + transactionResponse.getRefundDateTime()); // 	Data do cancelamento no formato YYYY-MM-DDThh:mm:ss.sTZD
			System.out.println("Ammount: " + transactionResponse.getAmount()); // Valor total da compra sem separador de milhar. Exemplo: 1000 = R$10,00
			if (transactionResponse.getAuthorization() != null) {
				System.out.println("Status: " + transactionResponse.getAuthorization().getStatus()); // Status da transação (Approved, Denied, Canceled, Pending)
			}
		}
	}

}
