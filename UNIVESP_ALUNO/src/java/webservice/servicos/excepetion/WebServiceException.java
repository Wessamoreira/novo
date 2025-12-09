package webservice.servicos.excepetion;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import webservice.arquitetura.InfoWSVO;

/**
 * @author Victor Hugo de Paula Costa - 3 de out de 2016
 *
 */
public class WebServiceException extends WebApplicationException {
	private static final long serialVersionUID = 1L;

	/**
	 * @author Victor Hugo de Paula Costa - 3 de out de 2016
	 */
	
	public WebServiceException(ErrorInfoRSVO mensagem, Status status) {
		super(Response.status(status).entity(mensagem).build());
	}
	
	public WebServiceException(InfoWSVO mensagem, Status status) {
		super(Response.status(status).entity(mensagem).build());
	}
}
