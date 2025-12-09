package webservice.nfse.serra;

public class WSEntradaProxy implements WSEntrada {
  private String _endpoint = null;
  private WSEntrada wSEntrada = null;
  
  public WSEntradaProxy() {
    _initWSEntradaProxy();
  }
  
  public WSEntradaProxy(String endpoint) {
    _endpoint = endpoint;
    _initWSEntradaProxy();
  }
  
  private void _initWSEntradaProxy() {
    try {
      wSEntrada = (new WSEntradaServiceLocator()).getWSEntradaPort();
      if (wSEntrada != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)wSEntrada)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)wSEntrada)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (wSEntrada != null)
      ((javax.xml.rpc.Stub)wSEntrada)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public WSEntrada getWSEntrada() {
    if (wSEntrada == null)
      _initWSEntradaProxy();
    return wSEntrada;
  }
  
  public java.lang.String nfdEntrada(java.lang.String cpfUsuario, java.lang.String hashSenha, int codigoMunicipio, java.lang.String nfd) throws java.rmi.RemoteException{
    if (wSEntrada == null)
      _initWSEntradaProxy();
    return wSEntrada.nfdEntrada(cpfUsuario, hashSenha, codigoMunicipio, nfd);
  }
  
  public java.lang.String consultarAtividades(java.lang.String cpfUsuario, java.lang.String hashSenha, java.lang.String inscricaoMunicipal, int codigoMunicipio) throws java.rmi.RemoteException{
    if (wSEntrada == null)
      _initWSEntradaProxy();
    return wSEntrada.consultarAtividades(cpfUsuario, hashSenha, inscricaoMunicipal, codigoMunicipio);
  }
  
  public java.lang.String nfdEntradaCancelar(java.lang.String cpfUsuario, java.lang.String hashSenha, java.lang.String nfd) throws java.rmi.RemoteException{
    if (wSEntrada == null)
      _initWSEntradaProxy();
    return wSEntrada.nfdEntradaCancelar(cpfUsuario, hashSenha, nfd);
  }
  
  
}