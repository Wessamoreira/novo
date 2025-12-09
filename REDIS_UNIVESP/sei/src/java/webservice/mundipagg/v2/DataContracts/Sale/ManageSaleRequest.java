package webservice.mundipagg.v2.DataContracts.Sale;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import webservice.mundipagg.v2.DataContracts.BaseRequest;
import webservice.mundipagg.v2.DataContracts.CreditCardTransaction.ManageCreditCardTransaction;

/**
 * Gerenciar Venda
 */
public final class ManageSaleRequest extends BaseRequest {
    
    /**
     * Construtor da Classe
     */
    public ManageSaleRequest() 
    {
        this.setCreditCardTransactionCollection(new ArrayList());
    }
    
    /**
     * Coleções de transdações de cartão de crédito
     */
    private List<ManageCreditCardTransaction> CreditCardTransactionCollection;

    /**
     * Chave do pedido. Utilizada para identificar um pedido no gateway
     */
    private UUID OrderKey;

    /**
     * Recupera Coleções de transdações de cartão de crédito
     * @return 
     */
    public List<ManageCreditCardTransaction> getCreditCardTransactionCollection() {
        return CreditCardTransactionCollection;
    }

    /**
     * Altera Coleções de transdações de cartão de crédito
     * @param CreditCardTransactionCollection 
     */
    public void setCreditCardTransactionCollection(List<ManageCreditCardTransaction> CreditCardTransactionCollection) {
        this.CreditCardTransactionCollection = CreditCardTransactionCollection;
    }

    /**
     * Recupera Chave do pedido. Utilizada para identificar um pedido no gateway
     * @return 
     */
    public UUID getOrderKey() {
        return OrderKey;
    }

    /**
     * Altera Chave do pedido. Utilizada para identificar um pedido no gateway
     * @param OrderKey 
     */
    public void setOrderKey(UUID OrderKey) {
        this.OrderKey = OrderKey;
    }
}
