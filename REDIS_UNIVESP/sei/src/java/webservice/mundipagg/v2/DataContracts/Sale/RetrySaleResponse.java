package webservice.mundipagg.v2.DataContracts.Sale;

import java.util.ArrayList;
import java.util.List;

import webservice.mundipagg.v2.DataContracts.BaseResponse;
import webservice.mundipagg.v2.DataContracts.CreditCardTransaction.CreditCardTransactionResult;

/**
 * Resposta da Retentativa de Venda
 */
public class RetrySaleResponse extends BaseResponse {
    
    /**
     * Construtor da Classe
     */
    public RetrySaleResponse() {
        this.setCreditCardTransactionResultCollection(new ArrayList());
    }
    
    /**
     * Lista de transações de cartão de crédito
     */
    private List<CreditCardTransactionResult> CreditCardTransactionResultCollection;

    /**
     * Recupera Lista de transações de cartão de crédito
     * @return 
     */
    public List<CreditCardTransactionResult> getCreditCardTransactionResultCollection() {
        return CreditCardTransactionResultCollection;
    }

    /**
     * Altera Lista de transações de cartão de crédito
     * @param CreditCardTransactionResultCollection 
     */
    public final void setCreditCardTransactionResultCollection(List<CreditCardTransactionResult> CreditCardTransactionResultCollection) {
        this.CreditCardTransactionResultCollection = CreditCardTransactionResultCollection;
    }    
}
