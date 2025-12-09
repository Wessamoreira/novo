package webservice.pix;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.http.HttpStatus;

import controle.arquitetura.SuperControle;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.UteisWebServiceUrl;
import webservice.arquitetura.InfoWSVO;
import webservice.pix.comuns.PixRSVO;
import webservice.pix.comuns.WebhookRSVO;
import webservice.servicos.excepetion.ErrorInfoRSVO;
import webservice.servicos.excepetion.WebServiceException;

@Path(UteisWebServiceUrl.URL_PIX_WEBHOOK)
public class PixWebhookRS extends SuperControle{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1605426798320264892L;
	public ErrorInfoRSVO errorInfoRSVO;
	
	@GET
	@Path("/status")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response status() {
		try {
			errorInfoRSVO = new ErrorInfoRSVO(Status.OK, "WebService conectado com sucesso.");
		} catch (Exception e) {
			errorInfoRSVO = new ErrorInfoRSVO(Status.INTERNAL_SERVER_ERROR, e.getMessage());
		} 
		return Response.status(errorInfoRSVO.getStatusCode()).entity(errorInfoRSVO).build();
	}
	
	
	@POST
	@Path("/{contacorrente}/pix")
	@Consumes(MediaType.APPLICATION_JSON)
	public void callbacksPix(@PathParam("contacorrente") final Integer contaCorrente, WebhookRSVO webhookRSVO) {		
		try {
			UsuarioVO usuarioOperacaoExterna = getFacadeFactory().getUsuarioFacade().obterUsuarioResponsavelOperacoesExternas();
			for (PixRSVO pix : webhookRSVO.getPix()) {
				getFacadeFactory().getPixContaCorrenteFacade().realizarProcessamentoBaixaPix(pix, contaCorrente, null, usuarioOperacaoExterna);
			}
		} catch (Exception e) {
			System.out.println("Sei Pix: " + e.getMessage());
			InfoWSVO errorInfoRSVO = new InfoWSVO();
			errorInfoRSVO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			errorInfoRSVO.setMessage(e.getMessage());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.INTERNAL_SERVER_ERROR);
		}
	}
	
	

}
