/**
 * WSEntrada.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package webservice.nfse.serra;

public interface WSEntrada extends java.rmi.Remote {
    public java.lang.String nfdEntrada(java.lang.String cpfUsuario, java.lang.String hashSenha, int codigoMunicipio, java.lang.String nfd) throws java.rmi.RemoteException;
    public java.lang.String consultarAtividades(java.lang.String cpfUsuario, java.lang.String hashSenha, java.lang.String inscricaoMunicipal, int codigoMunicipio) throws java.rmi.RemoteException;
    public java.lang.String nfdEntradaCancelar(java.lang.String cpfUsuario, java.lang.String hashSenha, java.lang.String nfd) throws java.rmi.RemoteException;
}
