package webservice.servicos;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;  
import jakarta.ws.rs.core.Response.Status;

import org.springframework.http.HttpStatus;

import controle.arquitetura.SuperControle;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisWebServiceUrl;
import webservice.arquitetura.InfoWSVO;
import webservice.servicos.excepetion.WebServiceException;

@Path(UteisWebServiceUrl.URL_SEI_SERVICO_SALA_AULA_BLACKBOARD_OPERACAO)
public class SalaAulaBlackboardOperacaoRS extends SuperControle {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 4878471902728400361L;

	@GET
	@Path(UteisWebServiceUrl.URL_SEI_SERVICO_SALA_AULA_BLACKBOARD_OPERACAO_GESTAO_MENSAGEM)
	@Produces(MediaType.APPLICATION_JSON)
	public Response realizarApuracaoNotaSalaAulaBlackboard(@Context final HttpServletRequest request) {
		InfoWSVO infoRSVO = null;
		try {
			InfoWSVO infoWSVO = validarDadosAutenticacaoTokenWebService(request);
			if (infoWSVO.getStatus() != Status.OK.getStatusCode()) {
				throw new StreamSeiException(infoWSVO.getMensagem());
			}
			Integer codigoOperacao = Integer.parseInt(request.getParameter("codigoOperacao"));
			UsuarioVO usuario = getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(Integer.parseInt(request.getParameter(UteisWebServiceUrl.ul)), Uteis.NIVELMONTARDADOS_DADOSLOGIN, null);
			Uteis.checkState(!Uteis.isAtributoPreenchido(codigoOperacao), "O Parâmetro Código Operação deve ser informado");
			Uteis.checkState(!Uteis.isAtributoPreenchido(usuario), "O Parâmetro Usuário deve ser informado");
			Thread job = new Thread(new SalaAulaBlackboardGestaoMensagem(codigoOperacao, usuario), "SalaAulaBlackboardGestaoMensagem_"+codigoOperacao);
			job.start();
			infoRSVO = new InfoWSVO(Status.OK, "Operação SOLICITADA com sucesso");
		} catch (Exception e) {
			System.out.println("Sei Blackboard - SalaAulaBlackboardOperacaoRS: " + e.getMessage());
			InfoWSVO errorInfoRSVO = new InfoWSVO();
			errorInfoRSVO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			errorInfoRSVO.setMessage(e.getMessage());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.INTERNAL_SERVER_ERROR);
		}
		return Response.status(infoRSVO.getStatus()).entity(infoRSVO).build();
	}
	
	class SalaAulaBlackboardGestaoMensagem implements Runnable {		
		private Integer codigoOperacao;
		private UsuarioVO usuario;

		public SalaAulaBlackboardGestaoMensagem(Integer codigoOperacao, UsuarioVO usuario) {
			this.codigoOperacao = codigoOperacao;
			this.usuario = usuario;
		}

		@Override
		public void run() {
			try {
				getFacadeFactory().getSalaAulaBlackboardOperacaoFacade().executarEnvioMensagemSalaAulaBlackboardOperacao(codigoOperacao, usuario);
				getFacadeFactory().getSalaAulaBlackboardOperacaoFacade().atualizarCampoMsgNotificacaoExecutadaSalaAulaBlackboardOperacao(codigoOperacao, null);
			} catch (Exception e) {
				getFacadeFactory().getSalaAulaBlackboardOperacaoFacade().atualizarCampoMsgNotificacaoExecutadaSalaAulaBlackboardOperacao(codigoOperacao, e.getMessage());
			}
		}
	}

}
