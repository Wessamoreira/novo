/**
 * MundiPaggService_ServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package webservice.mundipagg.v1.tempuri;

public class MundiPaggService_ServiceLocator extends org.apache.axis.client.Service implements MundiPaggService_Service {

    public MundiPaggService_ServiceLocator() {
    }


    public MundiPaggService_ServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public MundiPaggService_ServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for basicHttp
    private java.lang.String basicHttp_address = "https://transaction.mundipaggone.com/MundiPaggService.svc";

    public java.lang.String getbasicHttpAddress() {
        return basicHttp_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String basicHttpWSDDServiceName = "basicHttp";

    public java.lang.String getbasicHttpWSDDServiceName() {
        return basicHttpWSDDServiceName;
    }

    public void setbasicHttpWSDDServiceName(java.lang.String name) {
        basicHttpWSDDServiceName = name;
    }

    public MundiPaggService_PortType getbasicHttp() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(basicHttp_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getbasicHttp(endpoint);
    }

    public MundiPaggService_PortType getbasicHttp(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            BasicHttpStub _stub = new BasicHttpStub(portAddress, this);
            _stub.setPortName(getbasicHttpWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setbasicHttpEndpointAddress(java.lang.String address) {
        basicHttp_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (MundiPaggService_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                BasicHttpStub _stub = new BasicHttpStub(new java.net.URL(basicHttp_address), this);
                _stub.setPortName(getbasicHttpWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("basicHttp".equals(inputPortName)) {
            return getbasicHttp();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://tempuri.org/", "MundiPaggService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://tempuri.org/", "basicHttp"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("basicHttp".equals(portName)) {
            setbasicHttpEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
