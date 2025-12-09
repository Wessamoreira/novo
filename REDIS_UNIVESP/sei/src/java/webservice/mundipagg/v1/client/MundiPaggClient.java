package webservice.mundipagg.v1.client;

import java.io.StringReader;
import java.rmi.RemoteException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import webservice.mundipagg.v1.model.StatusNotification;
import webservice.mundipagg.v1.service.CreateOrderRequest;
import webservice.mundipagg.v1.service.CreateOrderResponse;
import webservice.mundipagg.v1.service.GetInstantBuyDataRequest;
import webservice.mundipagg.v1.service.GetInstantBuyDataResponse;
import webservice.mundipagg.v1.service.ManageOrderRequest;
import webservice.mundipagg.v1.service.ManageOrderResponse;
import webservice.mundipagg.v1.service.QueryOrderRequest;
import webservice.mundipagg.v1.service.QueryOrderResponse;
import webservice.mundipagg.v1.service.RetryOrderRequest;
import webservice.mundipagg.v1.service.RetryOrderResponse;
import webservice.mundipagg.v1.tempuri.MundiPaggServiceProxy;

/**
 * 
 * @author wvinco
 *
 */
public class MundiPaggClient {


	private MundiPaggServiceProxy mundiPaggServce = new MundiPaggServiceProxy();


	/**
	 * 
	 * @param CreateOrderRequest recebe os paramentros para a criação de um pedido
	 * @return Retorna os dados do pedido gerados ou os dados da falha, caso existam
	 */
	public CreateOrderResponse createOrder(CreateOrderRequest request) {

		CreateOrderResponse response = new CreateOrderResponse();
		try {

			if (request == null) {
				throw new IllegalArgumentException("Request");
			}


			if (request.getMerchantKey() != null){
				//Validação do merchantKey
				if (request.getMerchantKey().split("-").length != 5) {
					throw new IllegalArgumentException("Verify the MerchantKey provided.");
				}
			}else{
				throw new IllegalArgumentException("Verify the MerchantKey provided.");
			}
			
			
			if (CreateOrderValidationRules.existTransactionInCollection(request) == false) {
				throw new IllegalArgumentException("You have to create at least one transaction (credit card and/or boleto).");
			}

			response = this.mundiPaggServce.createOrder(request);

		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return response;
	}

	/**
	 * 
	 * @param request 
	 * @return
	 */
	public ManageOrderResponse manageOrder(ManageOrderRequest request){


		ManageOrderResponse response = new ManageOrderResponse();

		try {
			if (request == null){
				throw new IllegalArgumentException("request");
			}

			if (request.getMerchantKey() != null){
				//Validação do merchantKey
				if (request.getMerchantKey().split("-").length != 5) {
					throw new IllegalArgumentException("Verify the MerchantKey provided.");
				}
			}else{
				throw new IllegalArgumentException("Verify the MerchantKey provided.");
			}

			response = this.mundiPaggServce.manageOrder(request); 

		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

		return response;
	}


	/**
	 * 
	 * @param QueryOrderRequest 
	 * @return retorna um objeto QueryOrderResponse com os dados da consulta
	 */
	public QueryOrderResponse queryOrder(QueryOrderRequest request){

		QueryOrderResponse response = new QueryOrderResponse();

		try {

			if (request == null){
				throw new IllegalArgumentException("request");
			}

			if (request.getMerchantKey() != null){
				//Validação do merchantKey
				if (request.getMerchantKey().split("-").length != 5) {
					throw new IllegalArgumentException("Verify the MerchantKey provided.");
				}
			}else{
				throw new IllegalArgumentException("Verify the MerchantKey provided.");
			}

			response = this.mundiPaggServce.queryOrder(request);

		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

		return response;

	}

	public RetryOrderResponse retryOrder(RetryOrderRequest request){
		
		RetryOrderResponse response = new RetryOrderResponse();
		
		try {

			if (request == null){
				throw new IllegalArgumentException("request");
			}

			if (request.getMerchantKey() != null){
				//Validação do merchantKey
				if (request.getMerchantKey().split("-").length != 5) {
					throw new IllegalArgumentException("Verify the MerchantKey provided.");
				}
			}else{
				
				throw new IllegalArgumentException("Verify the MerchantKey provided.");
			}
			response = this.mundiPaggServce.retryOrder(request);

		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		
	return response;  
	}
	
	public GetInstantBuyDataResponse getInstantBuyData(GetInstantBuyDataRequest request){

		GetInstantBuyDataResponse response = new GetInstantBuyDataResponse();

		try {

			if (request == null){
				throw new IllegalArgumentException("request");
			}

			if (request.getMerchantKey() != null){
				//Validação do merchantKey
				if (request.getMerchantKey().split("-").length != 5) {
					throw new IllegalArgumentException("Verify the MerchantKey provided.");
				}
			}else{
				throw new IllegalArgumentException("Verify the MerchantKey provided.");
			}

			response = this.mundiPaggServce.getInstantBuyData(request);

		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		return response;
	}

	public static StatusNotification parse(String postNotificationXml){
		StatusNotification notification = new StatusNotification();
		postNotificationXml = postNotificationXml.replaceAll(" xmlns=\"http://schemas.datacontract.org/2004/07/MundiPagg.NotificationService.DataContract\" xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance","").replace("\"","").replaceAll("i:nil=true", "");
		if (postNotificationXml != null | postNotificationXml.isEmpty() == false){
			JAXBContext context;
			try {
				context = JAXBContext.newInstance(StatusNotification.class);
				StringReader reader = new StringReader(postNotificationXml);
				Unmarshaller unmarshaller = context.createUnmarshaller();
				notification  = (StatusNotification)unmarshaller.unmarshal(reader);
			} catch (JAXBException e) {
				throw new IllegalArgumentException("xml incorrect.");
			}
		}else{
			throw new IllegalArgumentException("postNotificationXml");
			}
		return notification;
	}

}
