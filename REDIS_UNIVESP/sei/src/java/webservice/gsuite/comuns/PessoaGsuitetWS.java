package webservice.gsuite.comuns;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.springframework.http.HttpStatus;

import com.google.gson.Gson;

import controle.arquitetura.SuperControle;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.gsuite.ClassroomGoogleVO;
import negocio.comuns.gsuite.PessoaGsuiteVO;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisWebServiceUrl;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import webservice.arquitetura.InfoWSVO;
import webservice.servicos.excepetion.WebServiceException;

@Path(UteisWebServiceUrl.URL_SEI_SERVICO_PESSOA_GSUITE)
public class PessoaGsuitetWS extends SuperControle {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1594288774677511713L;
	
	@POST
	@Path(UteisWebServiceUrl.URL_SEI_SERVICO_PESSOA_GSUITE_HISTORICO)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String consultarGoogleMeetConvidado(@Context final HttpServletRequest request , String classroom ) {
		String json = null;
		Gson gson = inicializaGson();
		List<PessoaGsuiteVO> lista = new ArrayList<>();
		try {
			InfoWSVO infoWSVO = validarDadosAutenticacaoTokenWebService(request);
			if(infoWSVO.getStatus() != Status.OK.getStatusCode()) {
				throw new StreamSeiException(infoWSVO.getMensagem());	
			}
			final ClassroomGoogleVO classroomGoogleVO = gson.fromJson(classroom, ClassroomGoogleVO.class);
			Integer codUsuario = Integer.parseInt(request.getHeader(UteisWebServiceUrl.ul)); 
			UsuarioVO usuario = getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(codUsuario, Uteis.NIVELMONTARDADOS_DADOSLOGIN, null);
			classroomGoogleVO.setTurmaVO(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(classroomGoogleVO.getTurmaVO().getCodigo(), NivelMontarDados.BASICO, usuario));
			if(Uteis.isAtributoPreenchido(classroomGoogleVO.getProfessorEad())) {
				lista =  getFacadeFactory().getPessoaGsuiteFacade().consultarAlunosDoEadTurmaDisciplinaDisponivelGsuite(classroomGoogleVO.getTurmaVO(), classroomGoogleVO.getDisciplinaVO(), classroomGoogleVO.getAno(), classroomGoogleVO.getSemestre(), classroomGoogleVO.getProfessorEad().getCodigo(), true, usuario);
			}else {
				Optional<PessoaGsuiteVO> professor = classroomGoogleVO.getClassroomTeacherVOs().stream().findFirst();
				lista =  getFacadeFactory().getPessoaGsuiteFacade().consultarAlunosDoHorarioTurmaDisciplinaDisponivelGsuite(0, classroomGoogleVO.getTurmaVO().getCurso().getCodigo(),classroomGoogleVO.getTurmaVO(), classroomGoogleVO.getDisciplinaVO().getCodigo(), classroomGoogleVO.getAno(), classroomGoogleVO.getSemestre(), professor.get().getPessoaVO().getCodigo(), true, getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(), usuario);
			}			
			
			json = gson.toJson(lista);
		} catch (Exception e) {
			System.out.println("Sei Gsuite: " + e.getMessage());
			InfoWSVO errorInfoRSVO = new InfoWSVO();
			errorInfoRSVO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			errorInfoRSVO.setMessage(e.getMessage());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.INTERNAL_SERVER_ERROR);
		}
		return json;
	}

}
