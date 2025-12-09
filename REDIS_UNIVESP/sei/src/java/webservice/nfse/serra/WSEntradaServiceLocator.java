/**
 * WSEntradaServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package webservice.nfse.serra;

public class WSEntradaServiceLocator extends org.apache.axis.client.Service implements WSEntradaService {

    public WSEntradaServiceLocator() {
    }


    public WSEntradaServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public WSEntradaServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for WSEntradaPort
    private java.lang.String WSEntradaPort_address = "http://apps.serra.es.gov.br:8080/tbw/services/WSEntrada";

    public java.lang.String getWSEntradaPortAddress() {
        return WSEntradaPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String WSEntradaPortWSDDServiceName = "WSEntradaPort";

    public java.lang.String getWSEntradaPortWSDDServiceName() {
        return WSEntradaPortWSDDServiceName;
    }

    public void setWSEntradaPortWSDDServiceName(java.lang.String name) {
        WSEntradaPortWSDDServiceName = name;
    }

    public WSEntrada getWSEntradaPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(WSEntradaPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getWSEntradaPort(endpoint);
    }

    public WSEntrada getWSEntradaPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            WSEntradaPortBindingStub _stub = new WSEntradaPortBindingStub(portAddress, this);
            _stub.setPortName(getWSEntradaPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setWSEntradaPortEndpointAddress(java.lang.String address) {
        WSEntradaPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (WSEntrada.class.isAssignableFrom(serviceEndpointInterface)) {
                WSEntradaPortBindingStub _stub = new WSEntradaPortBindingStub(new java.net.URL(WSEntradaPort_address), this);
                _stub.setPortName(getWSEntradaPortWSDDServiceName());
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
        if ("WSEntradaPort".equals(inputPortName)) {
            return getWSEntradaPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://webservices.sil.com/", "WSEntradaService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://webservices.sil.com/", "WSEntradaPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("WSEntradaPort".equals(portName)) {
            setWSEntradaPortEndpointAddress(address);
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
