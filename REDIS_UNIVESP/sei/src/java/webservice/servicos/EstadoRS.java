package webservice.servicos;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import controle.arquitetura.SuperControle;
import negocio.comuns.basico.EstadoVO;
import negocio.comuns.utilitarias.Uteis;
import webservice.servicos.excepetion.ErrorInfoRSVO;

/**
 * 
 * @author Alessandro
 */
@Path("/estado")
public class EstadoRS extends SuperControle {

	/**
	 * 
	 */
	private static final long serialVersionUID = -164129271219340562L;

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
	public List<EstadoVO> consultarEstado() {
		final List<EstadoVO> objetos = new ArrayList<EstadoVO>(0);
		try {
			objetos.addAll(getFacadeFactory().getEstadoFacade().consultarPorNome("", false, Uteis.NIVELMONTARDADOS_COMBOBOX, null));
			
		} catch (Exception e) {
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.BAD_REQUEST.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			
		}
		return objetos;
	}
	
	@GET
	@Produces({ "application/xml", "application/json" })
	@Path("/pais/{pais}")
	public List<EstadoVO> consultarEstado(@PathParam("pais") final String pais) {
		final List<EstadoVO> objetos = new ArrayList<EstadoVO>(0);
		try {
			objetos.addAll(getFacadeFactory().getEstadoFacade().consultarPorNomePaiz(pais, false, Uteis.NIVELMONTARDADOS_COMBOBOX, null));
			
		} catch (Exception e) {
			
		}
		return objetos;
	}
	
	
	
}
