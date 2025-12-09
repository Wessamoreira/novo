package webservice.certisign;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.http.HttpStatus;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.DocumentoAssinadoPessoaVO;
import negocio.comuns.academico.DocumentoAssinadoVO;
import negocio.comuns.academico.enumeradores.DocumentoAssinadoOrigemEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisWebServiceUrl;
import webservice.arquitetura.InfoWSVO;
import webservice.certisign.comuns.CertiSignCallBackRSVO;
import webservice.servicos.excepetion.WebServiceException;

@Path(UteisWebServiceUrl.URL_SEI_WEB_SERVICE_CERTISIGN)
public class CertiSignRS  extends SuperControle{
	/**
	 * 
	 */	
	private static final long serialVersionUID = 2698257528161160954L;

	
	@POST
	@Path(UteisWebServiceUrl.URL_SEI_WEB_SERVICE_CERTISIGN_CALLBACK_DOCUMENTO)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response callBackCertiSignDocumento(@Context final HttpServletRequest request, CertiSignCallBackRSVO obj) {
		try {
			InfoWSVO infoWSVO = validarDadosAutenticacaoTokenWebService(request);
			if(infoWSVO.getStatus() != Status.OK.getStatusCode()) {
				throw new StreamSeiException(infoWSVO.getMensagem());	
			}
			UsuarioVO usuario = new UsuarioVO();
			if (Uteis.isAtributoPreenchido(getAplicacaoControle().getConfiguracaoGeralSistemaVO(null, null).getUsuarioResponsavelOperacoesExternas())) {
				usuario = getFacadeFactory().getUsuarioFacade().consultarPorPessoa(getAplicacaoControle().getConfiguracaoGeralSistemaVO(null, null).getUsuarioResponsavelOperacoesExternas().getCodigo(), Boolean.FALSE, Uteis.NIVELMONTARDADOS_DADOSLOGIN, usuario);
			}
			DocumentoAssinadoVO doc = getFacadeFactory().getDocumentoAssinadoFacade().executarAtualizacaoDadosAssinaturaPorProvedorCertisign(obj , new Date(), DocumentoAssinadoOrigemEnum.CERTISIGN_WS,usuario);
			if(Uteis.isAtributoPreenchido(doc) &&  doc.getTipoOrigemDocumentoAssinadoEnum().isContrato()) {		
			   getFacadeFactory().getMatriculaPeriodoFacade().realizarAtivacaoMatriculaValidandoRegrasEntregaDocumentoAssinaturaContratoMatricula(doc.getMatricula().getMatricula() , getAplicacaoControle().getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(doc.getUnidadeEnsinoVO().getCodigo()), Boolean.FALSE,  usuario);
		    }
			return Response.status(Status.OK).build();
		} catch (Exception e) {
			InfoWSVO errorInfoRSVO = new InfoWSVO();
			errorInfoRSVO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			errorInfoRSVO.setMessage(e.getMessage());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	

}
