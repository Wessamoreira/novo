package webservice.mundipagg.v2.DataContracts.Sale;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import webservice.mundipagg.v2.DataContracts.BaseRequest;
import webservice.mundipagg.v2.DataContracts.CreditCardTransaction.RetrySaleCreditCardTransaction;

/**
 * Retentativa de Venda
 */
public final class RetrySaleRequest extends BaseRequest {
    
    /**
     * Construtor da Classe
     */
    public RetrySaleRequest() 
    {
        this.setRetrySaleCreditCardTransactionCollection(new ArrayList());
    }
    
    /**
     * Opções da transação.
     */
    private RetrySaleOptions Options;
    
    /**
     * Chave do pedido. Utilizada para identificar um pedido no gateway
     */
    private UUID OrderKey;
    
    /**
     * Lista de trasanções de cartão de crédito a serem retentadas
     */
    private List<RetrySaleCreditCardTransaction> RetrySaleCreditCardTransactionCollection;

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

    /**
     * Recupera opções da transação
     * @return 
     */
    public RetrySaleOptions getOptions() {
        return Options;
    }

    /**
     * Altera opções da transação
     * @param Options 
     */
    public void setOptions(RetrySaleOptions Options) {
        this.Options = Options;
    }
    
    /**
     * Recupera Lista de trasanções de cartão de crédito a serem retentadas
     * @return 
     */
    public List<RetrySaleCreditCardTransaction> getRetrySaleCreditCardTransactionCollection() {
        return RetrySaleCreditCardTransactionCollection;
    }

    /**
     * Altera Lista de trasanções de cartão de crédito a serem retentadas
     * @param RetrySaleCreditCardTransactionCollection 
     */
    public void setRetrySaleCreditCardTransactionCollection(List<RetrySaleCreditCardTransaction> RetrySaleCreditCardTransactionCollection) {
        this.RetrySaleCreditCardTransactionCollection = RetrySaleCreditCardTransactionCollection;
    }   
}
