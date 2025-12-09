package webservice.mundipagg.v2.Utility;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thoughtworks.xstream.XStream;

import webservice.mundipagg.v2.DataContracts.ErrorItem;
import webservice.mundipagg.v2.DataContracts.ErrorReport;
import webservice.mundipagg.v2.DataContracts.SaleOptions;
import webservice.mundipagg.v2.DataContracts.ServiceConstants;
import webservice.mundipagg.v2.DataContracts.Address.BillingAddress;
import webservice.mundipagg.v2.DataContracts.Address.BuyerAddress;
import webservice.mundipagg.v2.DataContracts.Address.DeliveryAddress;
import webservice.mundipagg.v2.DataContracts.AntiFraud.AntiFraudAnalysis;
import webservice.mundipagg.v2.DataContracts.AntiFraud.AntiFraudAnalysisHistory;
import webservice.mundipagg.v2.DataContracts.AntiFraud.AntiFraudAnalysisResult;
import webservice.mundipagg.v2.DataContracts.BoletoTransaction.BoletoTransaction;
import webservice.mundipagg.v2.DataContracts.BoletoTransaction.BoletoTransactionData;
import webservice.mundipagg.v2.DataContracts.BoletoTransaction.BoletoTransactionNotification;
import webservice.mundipagg.v2.DataContracts.BoletoTransaction.BoletoTransactionOptions;
import webservice.mundipagg.v2.DataContracts.BoletoTransaction.BoletoTransactionResult;
import webservice.mundipagg.v2.DataContracts.CreditCardTransaction.CreditCard;
import webservice.mundipagg.v2.DataContracts.CreditCardTransaction.CreditCardTransaction;
import webservice.mundipagg.v2.DataContracts.CreditCardTransaction.CreditCardTransactionData;
import webservice.mundipagg.v2.DataContracts.CreditCardTransaction.CreditCardTransactionNotification;
import webservice.mundipagg.v2.DataContracts.CreditCardTransaction.CreditCardTransactionOptions;
import webservice.mundipagg.v2.DataContracts.CreditCardTransaction.CreditCardTransactionResult;
import webservice.mundipagg.v2.DataContracts.CreditCardTransaction.ManageCreditCardTransaction;
import webservice.mundipagg.v2.DataContracts.CreditCardTransaction.RetrySaleCreditCardTransaction;
import webservice.mundipagg.v2.DataContracts.InstantBuy.CreditCardData;
import webservice.mundipagg.v2.DataContracts.InstantBuy.GetInstantBuyDataResponse;
import webservice.mundipagg.v2.DataContracts.Merchant.Merchant;
import webservice.mundipagg.v2.DataContracts.OnlineDebitTransaction.OnlineDebitTransactionNotification;
import webservice.mundipagg.v2.DataContracts.Order.Order;
import webservice.mundipagg.v2.DataContracts.Order.OrderData;
import webservice.mundipagg.v2.DataContracts.Order.OrderResult;
import webservice.mundipagg.v2.DataContracts.Person.Buyer;
import webservice.mundipagg.v2.DataContracts.PostNotification.StatusNotification;
import webservice.mundipagg.v2.DataContracts.Recurrency.Recurrency;
import webservice.mundipagg.v2.DataContracts.Sale.CreateSaleRequest;
import webservice.mundipagg.v2.DataContracts.Sale.CreateSaleResponse;
import webservice.mundipagg.v2.DataContracts.Sale.ManageSaleRequest;
import webservice.mundipagg.v2.DataContracts.Sale.ManageSaleResponse;
import webservice.mundipagg.v2.DataContracts.Sale.QuerySaleResponse;
import webservice.mundipagg.v2.DataContracts.Sale.RequestData;
import webservice.mundipagg.v2.DataContracts.Sale.RetrySaleOptions;
import webservice.mundipagg.v2.DataContracts.Sale.RetrySaleRequest;
import webservice.mundipagg.v2.DataContracts.Sale.RetrySaleResponse;
import webservice.mundipagg.v2.DataContracts.Sale.SaleData;
import webservice.mundipagg.v2.DataContracts.ShoppingCart.ShoppingCart;
import webservice.mundipagg.v2.DataContracts.ShoppingCart.ShoppingCartItem;
import webservice.mundipagg.v2.EnumTypes.HttpContentTypeEnum;
import webservice.mundipagg.v2.Utility.XmlConverter.DateConverter;
import webservice.mundipagg.v2.Utility.XmlConverter.DoubleConverter;
import webservice.mundipagg.v2.Utility.XmlConverter.FloatConverter;
import webservice.mundipagg.v2.Utility.XmlConverter.IntegerConverter;
import webservice.mundipagg.v2.Utility.XmlConverter.LongConverter;

/**
 * Utilitário para serializar e deserializar
 * @param <TObject>
 */
public class SerializeUtility<TObject> {
       
    /**
     * Construtor da Classe
     */
    public SerializeUtility(){}
    
    /**
     * Serializa objeto para string no formato especificado
     * @param obj
     * @param ContentType 
     * @return  
     */
    public String Serialize(TObject obj, HttpContentTypeEnum ContentType)
    {
        // Cria variavel local
        String serialized = "";
        
        // Se obj nulo, retorna string empty
        if(obj == null) 
        {
            return serialized;
        }
        
        // Verifica e serializa em xml ou json
        if(HttpContentTypeEnum.Json == ContentType) 
        {
            // Converte objeto para string Json
            Gson gson = new GsonBuilder().setDateFormat(ServiceConstants.DATE_TIME_FORMAT).create();
            serialized = gson.toJson(obj);    
        }
        else if(HttpContentTypeEnum.Xml == ContentType)
        {
            // Converte objeto para string Xml
            XStream xstream = InitiXmlConverter(null);
            serialized = xstream.toXML(obj);
        }
        
        // retorna string serializada
        return serialized;   
    }
    
    /**
     * Deserializa string no formato especificado para objeto 
     * @param TypeOfResponse
     * @param Serialized
     * @param ContentType
     * @return 
     */
    public TObject Deserializer(Class<TObject> TypeOfResponse, String Serialized, HttpContentTypeEnum ContentType) 
    {
        // Cria variavel local
        TObject obj = null;
        
        // Se string a deserializar vazia/nula, retorna null
        if(Serialized == null || Serialized.isEmpty()) 
        {
            return obj;
        }
        
        // Verifica e deserializa em xml ou json
        if(HttpContentTypeEnum.Json == ContentType) 
        {
            // Converte string Json para objeto
            Gson gson = new GsonBuilder().setDateFormat(ServiceConstants.DATE_TIME_FORMAT).create();
            obj = gson.fromJson(Serialized, TypeOfResponse);
        }
        else if(HttpContentTypeEnum.Xml == ContentType)
        {
            // Converte string Xml para objeto
            XStream xstream = InitiXmlConverter(TypeOfResponse);
            obj = TypeOfResponse.cast(xstream.fromXML(Serialized));
        }
        
        // retorna obj deserializado
        return obj;  
    }
    
    /**
     * Inicializa, Configura e retorna objeto responsavel por serializar/deserializar XML
     * @param TypeOfResponse
     * @return 
     */
    private XStream InitiXmlConverter(Class<TObject> TypeOfResponse)
    {
        // Inicializa objeto que fará a serialização/deserialização
        XStream xstream = new XStream();
        
        // Registra conversores 
        xstream.registerConverter(new DateConverter());
        xstream.registerConverter(new IntegerConverter());
        xstream.registerConverter(new LongConverter());
        xstream.registerConverter(new FloatConverter());
        xstream.registerConverter(new DoubleConverter());
        
        // Registra mapeamento de nome de classes classes 
        xstream.alias("AntiFraudAnalysisData", AntiFraudAnalysis.class);
        xstream.alias("QuerySaleAntiFraudAnalysisHistory", AntiFraudAnalysisHistory.class);
        xstream.alias("AntiFraudAnalysisResult", AntiFraudAnalysisResult.class);
        xstream.alias("BillingAddress", BillingAddress.class);
        xstream.alias("BuyerAddress", BuyerAddress.class);
        xstream.alias("DeliveryAddress", DeliveryAddress.class);
        xstream.alias("BoletoTransaction", BoletoTransaction.class);
        xstream.alias("BoletoTransactionData", BoletoTransactionData.class);
        xstream.alias("BoletoTransactionOptions", BoletoTransactionOptions.class);
        xstream.alias("BoletoTransactionResult", BoletoTransactionResult.class);
        xstream.alias("CreditCard", CreditCard.class);
        xstream.alias("CreditCardTransaction", CreditCardTransaction.class);
        xstream.alias("CreditCardTransactionData", CreditCardTransactionData.class);
        xstream.alias("CreditCardTransactionOptions", CreditCardTransactionOptions.class);
        xstream.alias("CreditCardTransactionResult", CreditCardTransactionResult.class);
        xstream.alias("ManageCreditCardTransaction", ManageCreditCardTransaction.class);
        xstream.alias("RetrySaleCreditCardTransaction", RetrySaleCreditCardTransaction.class);
        xstream.alias("GetInstantBuyDataResponse", GetInstantBuyDataResponse.class);        
        xstream.alias("Merchant", Merchant.class);
        xstream.alias("Order", Order.class);
        xstream.alias("OrderData", OrderData.class);
        xstream.alias("OrderResult", OrderResult.class);
        xstream.alias("Buyer", Buyer.class);
        xstream.alias("Recurrency", Recurrency.class);
        xstream.alias("Sale", SaleData.class);
        xstream.alias("SaleOptions", SaleOptions.class);
        xstream.alias("CreateSaleRequest", CreateSaleRequest.class);
        xstream.alias("CreateSaleResponse", CreateSaleResponse.class);
        xstream.alias("ManageSaleRequest", ManageSaleRequest.class);
        xstream.alias("ManageSaleResponse", ManageSaleResponse.class);
        xstream.alias("QuerySaleResponse", QuerySaleResponse.class);
        xstream.alias("RequestData", RequestData.class);
        xstream.alias("RetrySaleOptions", RetrySaleOptions.class);
        xstream.alias("RetrySaleResquest", RetrySaleRequest.class);
        xstream.alias("RetrySaleResponse", RetrySaleResponse.class);
        xstream.alias("ErrorItem", ErrorItem.class);
        xstream.alias("ErrorReport", ErrorReport.class);
        xstream.alias("ShoppingCartItem", ShoppingCartItem.class);
        xstream.alias("ShoppingCart", ShoppingCart.class);
        xstream.alias("StatusNotification", StatusNotification.class); 
        
        // Registra classes conflitantes de acordo com o tipo solicitado para a deserialização
        if(TypeOfResponse == GetInstantBuyDataResponse.class)
        {
            xstream.alias("CreditCard", CreditCardData.class);
        }
        if(TypeOfResponse == StatusNotification.class)
        {
            xstream.alias("CreditCardTransaction", CreditCardTransactionNotification.class); 
            xstream.alias("BoletoTransaction", BoletoTransactionNotification.class); 
            xstream.alias("OnlineDebitTransaction", OnlineDebitTransactionNotification.class); 
        }
            
        return xstream;
    }
}
