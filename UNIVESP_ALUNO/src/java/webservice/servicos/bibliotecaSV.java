package webservice.servicos;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.GenericEntity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;  
import jakarta.ws.rs.core.Response.Status;

import org.springframework.stereotype.Service;

import controle.arquitetura.SuperControle;
import negocio.comuns.biblioteca.CatalogoVO;
import webservice.servicos.excepetion.ErrorInfoRSVO;
import webservice.servicos.excepetion.WebServiceException;
import webservice.servicos.objetos.bibliotecaObject;

@Service
@Path("/bibliotecaSV")
public class bibliotecaSV extends SuperControle implements Serializable {

	private static final long serialVersionUID = 1L;
	@Context
	private HttpServletRequest request;
	public static final String AUTHENTICATION_HEADER = "Authorization";
	private bibliotecaObject bibliotecaObj;
	private static final String USER_AGENTE = null;
	
	

	public bibliotecaObject getBibliotecaObj() {
		return bibliotecaObj;
	}

	public void setBibliotecaObj(bibliotecaObject bibliotecaObj) {
		this.bibliotecaObj = bibliotecaObj;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/listarCatalogoBiblioteca/{titulo}")
	public Response listarCatalogoBiblioteca(@PathParam("titulo") String titulo) {
		try {

			List<CatalogoVO> CatalogoVOs = new ArrayList<CatalogoVO>();
//			CatalogoVOs = getFacadeFactory().getMinhaBibliotecaInterfaceFacade().consultarPorTitulo("",titulo, false, 2,getUsuarioLogado());
			Collections.reverse(CatalogoVOs);
			return Response.status(Status.OK).entity(new GenericEntity<List<CatalogoVO>>(CatalogoVOs) {
			}).build();
		} catch (Exception e) {
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/listarCatalogos")
	public Response listarCatalogos() {

		try {
			List<CatalogoVO> CatalogoVOs = new ArrayList<CatalogoVO>();
//			CatalogoVOs = getFacadeFactory().getMinhaBibliotecaInterfaceFacade().consultarCatalogos();
			return Response.status(Status.OK).entity(new GenericEntity<List<CatalogoVO>>(CatalogoVOs) {
			}).build();
		} catch (Exception e) {
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
	}
	
	public static String get() throws Exception {
	       
		URL obj = new URL("http://localhost:8080/7.0.1.1M/webservice/bibliotecaSV/listarCatalogos");

		HttpURLConnection conexao = (HttpURLConnection) obj.openConnection();
		conexao.setRequestMethod("GET");

		conexao.setRequestProperty("User-Agent" , USER_AGENTE);

			
			int responseCode = conexao.getResponseCode();
					BufferedReader in = new BufferedReader(
					new InputStreamReader(conexao.getInputStream(), "UTF-8"));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
				
			}
			in.close();
			
    	  return response.toString();
    		

		}	

}
	
	
