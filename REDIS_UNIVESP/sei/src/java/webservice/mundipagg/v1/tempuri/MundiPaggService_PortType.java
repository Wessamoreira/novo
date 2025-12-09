/**
 * MundiPaggService_PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package webservice.mundipagg.v1.tempuri;

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

public interface MundiPaggService_PortType extends java.rmi.Remote {
    public CreateOrderResponse createOrder(CreateOrderRequest createOrderRequest) throws java.rmi.RemoteException;
    public ManageOrderResponse manageOrder(ManageOrderRequest manageOrderRequest) throws java.rmi.RemoteException;
    public QueryOrderResponse queryOrder(QueryOrderRequest queryOrderRequest) throws java.rmi.RemoteException;
    public GetInstantBuyDataResponse getInstantBuyData(GetInstantBuyDataRequest queryCreditCardDataRequest) throws java.rmi.RemoteException;
    public RetryOrderResponse retryOrder(RetryOrderRequest manualRetryRequest) throws java.rmi.RemoteException;
}
