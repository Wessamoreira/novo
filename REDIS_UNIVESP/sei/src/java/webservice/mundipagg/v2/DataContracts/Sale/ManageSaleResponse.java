package webservice.mundipagg.v2.DataContracts.Sale;

import java.util.List;

import webservice.mundipagg.v2.DataContracts.BaseResponse;
import webservice.mundipagg.v2.DataContracts.CreditCardTransaction.CreditCardTransactionResult;

/**
 * Responsta de gerenciar venda
 */
public class ManageSaleResponse extends BaseResponse {
    
    /**
     * Construtor da Classe
     */
    public ManageSaleResponse() {}
 
    /**
     * Coleção de transações de cartão de crédito
     */
    private List<CreditCardTransactionResult> CreditCardTransactionResultCollection;

    /**
     * Recupera Coleção de transações de cartão de crédito
     * @return 
     */
    public List<CreditCardTransactionResult> getCreditCardTransactionResultCollection() {
        return CreditCardTransactionResultCollection;
    }

    /**
     * Altera Coleção de transações de cartão de crédito
     * @param CreditCardTransactionResultCollection 
     */
    public void setCreditCardTransactionResultCollection(List<CreditCardTransactionResult> CreditCardTransactionResultCollection) {
        this.CreditCardTransactionResultCollection = CreditCardTransactionResultCollection;
    }    
}
