package webservice.servicos;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.Response.Status;

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
