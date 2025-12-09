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

public class MundiPaggServiceProxy implements MundiPaggService_PortType {
  private String _endpoint = null;
  private MundiPaggService_PortType mundiPaggService_PortType = null;
  
  public MundiPaggServiceProxy() {
    _initMundiPaggServiceProxy();
  }
  
  public MundiPaggServiceProxy(String endpoint) {
    _endpoint = endpoint;
    _initMundiPaggServiceProxy();
  }
  
  private void _initMundiPaggServiceProxy() {
    try {
      mundiPaggService_PortType = (new MundiPaggService_ServiceLocator()).getbasicHttp();
      if (mundiPaggService_PortType != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)mundiPaggService_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)mundiPaggService_PortType)._getProperty("javax.xml.rpc.service.endpoint.address");
      }       
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (mundiPaggService_PortType != null)
      ((javax.xml.rpc.Stub)mundiPaggService_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public MundiPaggService_PortType getMundiPaggService_PortType() {
    if (mundiPaggService_PortType == null)
      _initMundiPaggServiceProxy();
    return mundiPaggService_PortType;
  }
  
  public CreateOrderResponse createOrder(CreateOrderRequest createOrderRequest) throws java.rmi.RemoteException{
    if (mundiPaggService_PortType == null)
      _initMundiPaggServiceProxy();
    return mundiPaggService_PortType.createOrder(createOrderRequest);
  }
  
  public ManageOrderResponse manageOrder(ManageOrderRequest manageOrderRequest) throws java.rmi.RemoteException{
    if (mundiPaggService_PortType == null)
      _initMundiPaggServiceProxy();
    return mundiPaggService_PortType.manageOrder(manageOrderRequest);
  }
  
  public QueryOrderResponse queryOrder(QueryOrderRequest queryOrderRequest) throws java.rmi.RemoteException{
    if (mundiPaggService_PortType == null)
      _initMundiPaggServiceProxy();
    return mundiPaggService_PortType.queryOrder(queryOrderRequest);
  }
  
  public GetInstantBuyDataResponse getInstantBuyData(GetInstantBuyDataRequest queryCreditCardDataRequest) throws java.rmi.RemoteException{
    if (mundiPaggService_PortType == null)
      _initMundiPaggServiceProxy();
    return mundiPaggService_PortType.getInstantBuyData(queryCreditCardDataRequest);
  }
  
  public RetryOrderResponse retryOrder(RetryOrderRequest manualRetryRequest) throws java.rmi.RemoteException{
    if (mundiPaggService_PortType == null)
      _initMundiPaggServiceProxy();
    return mundiPaggService_PortType.retryOrder(manualRetryRequest);
  }
  
  
}