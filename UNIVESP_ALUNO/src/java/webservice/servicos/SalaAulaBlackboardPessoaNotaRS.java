package webservice.servicos;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;  
import jakarta.ws.rs.core.Response.Status;

import org.springframework.http.HttpStatus;

import com.google.gson.Gson;

import controle.arquitetura.SuperControle;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.blackboard.SalaAulaBlackboardNotaVO;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisWebServiceUrl;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;
//import relatorio.negocio.jdbc.financeiro.ListagemDescontosAlunosRel;
import webservice.arquitetura.InfoWSVO;
import webservice.servicos.excepetion.WebServiceException;

@Path(UteisWebServiceUrl.URL_SEI_SERVICO_SALA_AULA_BLACKBOARD_NOTAS)
public class SalaAulaBlackboardPessoaNotaRS extends SuperControle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1673163113353098620L;

	@POST
	@Path(UteisWebServiceUrl.URL_SEI_SERVICO_SALA_AULA_BLACKBOARD_APURAR)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response realizarApuracaoNotaSalaAulaBlackboard(@Context final HttpServletRequest request, String salaAulaBlackboardNota) {
		InfoWSVO infoRSVO = null;
		try {
			InfoWSVO infoWSVO = validarDadosAutenticacaoTokenWebService(request);
			if (infoWSVO.getStatus() != Status.OK.getStatusCode()) {
				throw new StreamSeiException(infoWSVO.getMensagem());
			}
			Gson gson = inicializaGson();
			SalaAulaBlackboardNotaVO obj = gson.fromJson(salaAulaBlackboardNota, SalaAulaBlackboardNotaVO.class);
			UsuarioVO usuario = getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(Integer.parseInt(request.getParameter(UteisWebServiceUrl.ul)), Uteis.NIVELMONTARDADOS_DADOSLOGIN, null);
			Uteis.checkState(obj ==  null, "Não encontrado o json esperado");
			Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getCodigoOperacao()), "O Parâmetro Código da Operação deve ser informado");
			Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getTipoOrigem()), "O Parâmetro Tipo Origem da Operação deve ser informado");
			Uteis.checkState(!Uteis.isAtributoPreenchido(usuario), "O Parâmetro Usuário deve ser informado");
			executarApuracaoNotaSalaAulaBlackboard(obj, true, usuario);
			infoRSVO = new InfoWSVO(Status.OK, "Operação SOLICITADA com sucesso, clique em atualizar para buscar os dados já processados.");
		} catch (Exception e) {
			System.out.println("Sei Blackboard - SalaAulaBlackboardPessoaNotaRS: " + e.getMessage());
			InfoWSVO errorInfoRSVO = new InfoWSVO();
			errorInfoRSVO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			errorInfoRSVO.setMessage(e.getMessage());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.INTERNAL_SERVER_ERROR);
		}
		return Response.status(infoRSVO.getStatus()).entity(infoRSVO).build();
	}
	
//	@PUT
//	@Path(UteisWebServiceUrl.URL_SEI_SERVICO_SALA_AULA_BLACKBOARD_APURAR)
//	@Consumes(MediaType.APPLICATION_JSON)
//	@Produces(MediaType.APPLICATION_JSON)
//	public Response realizarApuracaoNotaSalaAulaBlackboard1(@Context final HttpServletRequest request) {
//		InfoWSVO infoRSVO = null;
//		try {
//			InfoWSVO infoWSVO = validarDadosAutenticacaoTokenWebService(request);
//			if (infoWSVO.getStatus() != Status.OK.getStatusCode()) {
//				throw new StreamSeiException(infoWSVO.getMensagem());
//			}
//			Boolean realizarCalculoMediaApuracaoNotas = Boolean.valueOf(request.getParameter("realizarCalculoMediaApuracaoNotas"));
//			Integer codigoOperacao = Integer.parseInt(request.getParameter("codigoOperacao"));
//			UsuarioVO usuario = getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(Integer.parseInt(request.getParameter(UteisWebServiceUrl.ul)), Uteis.NIVELMONTARDADOS_DADOSLOGIN, null);
//			Uteis.checkState(!Uteis.isAtributoPreenchido(codigoOperacao), "O Parâmetro Código da Operação deve ser informado");
//			Uteis.checkState(!Uteis.isAtributoPreenchido(usuario), "O Parâmetro Usuário deve ser informado");
//			executarApuracaoNotaSalaAulaBlackboard(null, realizarCalculoMediaApuracaoNotas, true, codigoOperacao, usuario);
//			infoRSVO = new InfoWSVO(Status.OK, "Operação SOLICITADA com sucesso, clique em atualizar para buscar os dados já processados.");
//		} catch (Exception e) {
//			System.out.println("Sei Blackboard1 - SalaAulaBlackboardPessoaNotaRS: " + e.getMessage());
//			InfoWSVO errorInfoRSVO = new InfoWSVO();
//			errorInfoRSVO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
//			errorInfoRSVO.setMessage(e.getMessage());
//			errorInfoRSVO.setMensagem(e.getMessage());
//			throw new WebServiceException(errorInfoRSVO, Status.INTERNAL_SERVER_ERROR);
//		}
//		return Response.status(infoRSVO.getStatus()).entity(infoRSVO).build();
//	}
	
	private void executarApuracaoNotaSalaAulaBlackboard(SalaAulaBlackboardNotaVO obj, boolean considerarAuditoria, UsuarioVO usuario) {
		try {
			if(obj.getTipoOrigem().equals("CONSOLIDAR_NOTA_SOMENTE_NO_SEI")) {
				getFacadeFactory().getSalaAulaBlackboardFacade().executarConsolidacaoDeNotasApuradasNoSeiPorSalaAulaBlackboard(obj, usuario);
			}else if(obj.getTipoOrigem().equals("APURAR_NOTA_SALA_AULA_BLACBOARD") || obj.getTipoOrigem().equals("CONSOLIDAR_NOTA_SALA_AULA_BLACBOARD")) {
				getFacadeFactory().getSalaAulaBlackboardFacade().executarApuracaoDeNotaSalaAulaBlackboard(obj, considerarAuditoria, usuario);	
			}else {
				new Exception("Não foi encontrado O Tipo Origem Especificado - " + obj.getTipoOrigem());
			}
			getFacadeFactory().getSalaAulaBlackboardOperacaoFacade().atualizarSalaAulaBlackboardOperacaoExecutada(obj.getCodigoOperacao());
		} catch (Exception e) {
			getFacadeFactory().getSalaAulaBlackboardOperacaoFacade().atualizarCampoErroSalaAulaBlackboardOperacao(obj.getCodigoOperacao(), e.getMessage());
		}
	}


}
