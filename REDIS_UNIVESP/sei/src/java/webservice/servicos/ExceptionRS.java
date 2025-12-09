package webservice.servicos;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class ExceptionRS extends WebApplicationException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7988547089227959946L;

	public ExceptionRS(Status status , String mensagem) {
		super(Response.status(status).entity(mensagem).build());
	}

}
