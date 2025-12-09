package webservice.servicos;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;  
import jakarta.ws.rs.core.Response.Status;

import controle.arquitetura.SuperControle;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.gsuite.PessoaGsuiteVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisWebServiceUrl;
import webservice.arquitetura.InfoWSVO;

@Path(UteisWebServiceUrl.URL_SEI_SERVICO_GESTAO_ENVIO_MENSAGEM)
public class GestaoEnvioMensagemAutomaticaWS extends SuperControle {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2872472998892477183L;
	
	@POST
	@Path(UteisWebServiceUrl.URL_SEI_SERVICO_MENSAGEM_GSUITE_CONTA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response mensagemGsuiteCriacaoConta(@Context final HttpServletRequest request) {
		InfoWSVO resp = null;
		try {
			InfoWSVO infoWSVO = validarDadosAutenticacaoTokenWebService(request);
			if(infoWSVO.getStatus() == Status.OK.getStatusCode()) {
				UsuarioVO usuario = new UsuarioVO();
				usuario.setCodigo(Integer.parseInt(request.getHeader(UteisWebServiceUrl.ul)));
				PessoaGsuiteVO pessoaGsuite = getFacadeFactory().getPessoaGsuiteFacade().consultarPorChavePrimaria(Integer.parseInt(request.getHeader(UteisWebServiceUrl.pessoagsuite)), Uteis.NIVELMONTARDADOS_TODOS, usuario);
				getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemGsuiteCriacaoConta(pessoaGsuite, usuario);
			}
			resp = new InfoWSVO(Status.OK, "Conta Gsuite em processamento.");
		} catch (Exception e) {
			resp = new InfoWSVO(Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}
		return Response.status(resp.getStatus()).entity(resp).build();
	}

}
