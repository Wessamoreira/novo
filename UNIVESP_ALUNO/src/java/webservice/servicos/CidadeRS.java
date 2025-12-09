package webservice.servicos;

import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.GenericEntity;
import jakarta.ws.rs.core.Response;  
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.core.Response.Status;

import controle.arquitetura.SuperControle;
import webservice.servicos.excepetion.ErrorInfoRSVO;

/**
 * 
 * @author Alessandro
 */
@Path("/cidade")
public class CidadeRS extends SuperControle {

	/**
	 * Http Service responsável por validar se matrícula possui permissão para
	 * ativar catraca.
	 * 
	 * @param request
	 * @param security
	 * @return XML/JSON
	 */
	@GET
	@Produces({ "application/xml", "application/json" })
	@Path("/todos")
	public List<CidadeObject> getValidacaoPorMatricula(
			@Context final HttpServletRequest request,
			@Context final SecurityContext security) {
		final List<CidadeObject> objetos = new ArrayList<CidadeObject>(0);
		try {
			objetos.addAll(getFacadeFactory().getCidadeFacade()
					.consultarPorNomeRS(""));
		} catch (Exception e) {
		}
		return objetos;
	}
	
	@GET
	@Produces({ "application/xml", "application/json" })
	@Path("/estado/{estado}")
	public List<CidadeObject> consultarPorEstado(@PathParam("estado") final String estado) {
		final List<CidadeObject> objetos = new ArrayList<CidadeObject>(0);
		try {
			objetos.addAll(getFacadeFactory().getCidadeFacade().consultarPorCodigoEstadoRS(Integer.valueOf(estado), false, null));
		} catch (Exception e) {
		
		}		
		return objetos;
	}
	
	
}
