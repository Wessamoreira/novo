package negocio.comuns.arquitetura.faturamento.nfe;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.MimeHeaders;
import jakarta.xml.soap.SOAPConnection;
import jakarta.xml.soap.SOAPConnectionFactory;
import jakarta.xml.soap.SOAPEnvelope;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPPart;

public class SOAPMessageSender {

	// SAAJ - SOAP Client Testing
	private static void createSoapEnvelope(SOAPMessage soapMessage) throws SOAPException {
		SOAPPart soapPart = soapMessage.getSOAPPart();

		String myNamespace = "myNamespace";
		String myNamespaceURI = "http://www.webserviceX.NET";

		// SOAP Envelope
		SOAPEnvelope envelope = soapPart.getEnvelope();
		envelope.addNamespaceDeclaration(myNamespace, myNamespaceURI);

	}

	public static SOAPMessage callSoapWebService(String soapEndpointUrl, String soapAction, String envelope) throws SOAPException, Exception {
		SOAPMessage soapResponse = null;
		// Create SOAP Connection
		SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
		SOAPConnection soapConnection = soapConnectionFactory.createConnection();

		// Send SOAP Message to SOAP Server
		SOAPMessage createSOAPRequest = createSOAPRequest(soapAction, envelope);
		teste(createSOAPRequest);
		soapResponse = soapConnection.call(createSOAPRequest, soapEndpointUrl);

		// Print the SOAP Response
		System.out.println("Response SOAP Message:");
		soapResponse.writeTo(System.out);
		System.out.println();

		soapConnection.close();
		return soapResponse;
	}

	public static void teste(SOAPMessage soapMessage) {
		try {
			java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
			soapMessage.writeTo(baos);
			baos.toString();
		} catch (SOAPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static SOAPMessage createSOAPRequest(String soapAction, String envelope) throws Exception {
		MessageFactory messageFactory = MessageFactory.newInstance("SOAP 1.2 Protocol");

		MimeHeaders header = new MimeHeaders();
		header.addHeader("SOAPAction", soapAction);
		header.addHeader("Content-Type", "application/soap+xml");

		SOAPMessage soapMessage = messageFactory.createMessage(header, new ByteArrayInputStream(envelope.getBytes()));
		createSoapEnvelope(soapMessage);

		soapMessage.saveChanges();

		/* Print the request message, just for debugging purposes */
		System.out.println("Request SOAP Message:");
		soapMessage.writeTo(System.out);
		System.out.println("\n");

		return soapMessage;
	}

}