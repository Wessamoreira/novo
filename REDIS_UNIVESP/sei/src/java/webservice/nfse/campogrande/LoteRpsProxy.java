package webservice.nfse.campogrande;

public class LoteRpsProxy implements LoteRps {
  private String _endpoint = null;
  private LoteRps loteRps = null;
  
  public LoteRpsProxy() {
    _initLoteRpsProxy();
  }
  
  public LoteRpsProxy(String endpoint) {
    _endpoint = endpoint;
    _initLoteRpsProxy();
  }
  
  private void _initLoteRpsProxy() {
    try {
      loteRps = (new LoteRpsServiceLocator()).getLoteRps();
      if (loteRps != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)loteRps)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)loteRps)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (loteRps != null)
      ((javax.xml.rpc.Stub)loteRps)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public LoteRps getLoteRps() {
    if (loteRps == null)
      _initLoteRpsProxy();
    return loteRps;
  }
  
  public java.lang.String consultarSequencialRps(java.lang.String mensagemXml) throws java.rmi.RemoteException{
    if (loteRps == null)
      _initLoteRpsProxy();
    return loteRps.consultarSequencialRps(mensagemXml);
  }
  
  public java.lang.String enviarSincrono(java.lang.String mensagemXml) throws java.rmi.RemoteException{
    if (loteRps == null)
      _initLoteRpsProxy();
    return loteRps.enviarSincrono(mensagemXml);
  }
  
  public java.lang.String enviar(java.lang.String mensagemXml) throws java.rmi.RemoteException{
    if (loteRps == null)
      _initLoteRpsProxy();
    return loteRps.enviar(mensagemXml);
  }
  
  public java.lang.String consultarLote(java.lang.String mensagemXml) throws java.rmi.RemoteException{
    if (loteRps == null)
      _initLoteRpsProxy();
    return loteRps.consultarLote(mensagemXml);
  }
  
  public java.lang.String consultarNota(java.lang.String mensagemXml) throws java.rmi.RemoteException{
    if (loteRps == null)
      _initLoteRpsProxy();
    return loteRps.consultarNota(mensagemXml);
  }
  
  public java.lang.String cancelar(java.lang.String mensagemXml) throws java.rmi.RemoteException{
    if (loteRps == null)
      _initLoteRpsProxy();
    return loteRps.cancelar(mensagemXml);
  }
  
  public java.lang.String consultarNFSeRps(java.lang.String mensagemXml) throws java.rmi.RemoteException{
    if (loteRps == null)
      _initLoteRpsProxy();
    return loteRps.consultarNFSeRps(mensagemXml);
  }
  
  
}