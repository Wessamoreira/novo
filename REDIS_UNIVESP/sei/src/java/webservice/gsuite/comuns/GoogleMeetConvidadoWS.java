package webservice.gsuite.comuns;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.springframework.http.HttpStatus;

import controle.arquitetura.SuperControle;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.gsuite.GoogleMeetConvidadoVO;
import negocio.comuns.gsuite.GoogleMeetVO;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisWebServiceUrl;
import webservice.arquitetura.InfoWSVO;
import webservice.servicos.excepetion.WebServiceException;

@Path(UteisWebServiceUrl.URL_SEI_SERVICO_GOOGLE_MEET_CONVIDADO)
public class GoogleMeetConvidadoWS extends SuperControle {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3002411268895401491L;
	
	@POST
	@Path(UteisWebServiceUrl.URL_SEI_SERVICO_GOOGLE_MEET_CONVIDADO_CONSULTAR)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<GoogleMeetConvidadoVO> consultarGoogleMeetConvidado(@Context final HttpServletRequest request , final GoogleMeetVO googleMeet) {
		List<GoogleMeetConvidadoVO> lista = new ArrayList<>();
		try {
			InfoWSVO infoWSVO = validarDadosAutenticacaoTokenWebService(request);
			if(infoWSVO.getStatus() != Status.OK.getStatusCode()) {
				throw new StreamSeiException(infoWSVO.getMensagem());	
			}
			Integer codUsuario = Integer.parseInt(request.getHeader(UteisWebServiceUrl.ul)); 
			UsuarioVO usuario = getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(codUsuario, Uteis.NIVELMONTARDADOS_DADOSLOGIN, null);
			//lista =  getFacadeFactory().getGoogleMeetConvidadoFacade().consultarAlunosDoHorarioTurmaDisciplinaDisponivelGsuite(googleMeet.getTurmaVO().getUnidadeEnsino().getCodigo(), googleMeet.getTurmaVO().getCurso().getCodigo(),googleMeet.getTurmaVO(), googleMeet.getDisciplinaVO().getCodigo(), googleMeet.getAno(), googleMeet.getSemestre(),googleMeet.getProfessorVO().getCodigo(), getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(), usuario);
			if(lista.isEmpty()) {
				lista = null;
			}
		} catch (Exception e) {
			System.out.println("Sei Gsuite: " + e.getMessage());
			InfoWSVO errorInfoRSVO = new InfoWSVO();
			errorInfoRSVO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			errorInfoRSVO.setMessage(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.INTERNAL_SERVER_ERROR);
		}
		return lista;
	}
}
