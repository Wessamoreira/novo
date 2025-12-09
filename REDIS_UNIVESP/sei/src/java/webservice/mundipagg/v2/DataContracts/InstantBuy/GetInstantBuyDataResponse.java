package webservice.mundipagg.v2.DataContracts.InstantBuy;

import java.util.ArrayList;
import java.util.List;

import webservice.mundipagg.v2.DataContracts.BaseResponse;

/**
 * Dados de cartões de crédito de um comprador
 */
public final class GetInstantBuyDataResponse extends BaseResponse {
 
    /**
     * Construtor da Classe
     */
    public GetInstantBuyDataResponse() 
    {
        this.setCreditCardDataCollection(new ArrayList());
    }
    
    /**
     * Lista de Cartões de Crédito
     */
    private List<CreditCardData> CreditCardDataCollection;
    
    /**
     * Total de cartões de crédito retornados
     */
    private Integer CreditCardDataCount;

    /**
     * Recupera lista de Cartões de Crédito
     * @return 
     */
    public List<CreditCardData> getCreditCardDataCollection() {
        return CreditCardDataCollection;
    }

    /**
     * Altera lista de Cartões de Crédito
     * @param CreditCardDataCollection 
     */
    public void setCreditCardDataCollection(List<CreditCardData> CreditCardDataCollection) {
        this.CreditCardDataCollection = CreditCardDataCollection;
    }

    /**
     * Recupera total de cartões de crédito retornados
     * @return 
     */
    public Integer getCreditCardDataCount() {
        return CreditCardDataCount;
    }

    /**
     * Altera total de cartões de crédito retornados
     * @param CreditCardDataCount 
     */
    public void setCreditCardDataCount(Integer CreditCardDataCount) {
        this.CreditCardDataCount = CreditCardDataCount;
    }
}
