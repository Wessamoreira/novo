package webservice.servicos;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response.Status;

import org.springframework.http.HttpStatus;

import com.google.gson.Gson;

import controle.arquitetura.SuperControle;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaEmailInstitucionalVO;
import negocio.comuns.blackboard.SalaAulaBlackboardPessoaVO;
import negocio.comuns.blackboard.SalaAulaBlackboardVO;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisWebServiceUrl;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import webservice.arquitetura.InfoWSVO;
import webservice.servicos.excepetion.WebServiceException;

@Path(UteisWebServiceUrl.URL_SEI_SERVICO_PESSOA_EMAIL_INSTITUCIONAL)
public class PessoaEmailInstitucionalRS extends SuperControle {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8891670964756776012L;
	
	@POST
	@Path(UteisWebServiceUrl.URL_SEI_SERVICO_PESSOA_EMAIL_INSTITUCIONAL_HISTORICO)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String consultarPessoaEmailInstitucionalRS(@Context final HttpServletRequest request , String salaAulaBlackbord ) {
		String json = null;
		Gson gson = inicializaGson();
		List<PessoaEmailInstitucionalVO> lista = new ArrayList<>();
		try {
			InfoWSVO infoWSVO = validarDadosAutenticacaoTokenWebService(request);
			if(infoWSVO.getStatus() != Status.OK.getStatusCode()) {
				throw new StreamSeiException(infoWSVO.getMensagem());	
			}
			final SalaAulaBlackboardVO obj = gson.fromJson(salaAulaBlackbord, SalaAulaBlackboardVO.class);
			Integer codUsuario = Integer.parseInt(request.getParameter(UteisWebServiceUrl.ul)); 
			UsuarioVO usuario = getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(codUsuario, Uteis.NIVELMONTARDADOS_DADOSLOGIN, null);
			obj.setTurmaVO(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(obj.getTurmaVO().getCodigo(), NivelMontarDados.BASICO, usuario));
			if(Uteis.isAtributoPreenchido(obj.getProgramacaoTutoriaOnlineVO())) {
				lista =  getFacadeFactory().getPessoaEmailInstitucionalFacade().consultarAlunosDoEadTurmaDisciplinaDisponivel(obj.getTurmaVO(), obj.getDisciplinaVO(), obj.getAno(), obj.getSemestre(), obj.getBimestre(), obj.getProgramacaoTutoriaOnlineVO(),  true, usuario);
			}else {
				Optional<SalaAulaBlackboardPessoaVO> professor = obj.getListaProfessores().stream().findFirst();
				lista =  getFacadeFactory().getPessoaEmailInstitucionalFacade().consultarAlunosDoHorarioTurmaDisciplinaDisponivel(0, obj.getTurmaVO().getCurso().getCodigo(),obj.getTurmaVO(), obj.getDisciplinaVO().getCodigo(), obj.getAno(), obj.getSemestre(), professor.get().getPessoaEmailInstitucionalVO().getPessoaVO().getCodigo(), true, getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(), usuario);
			}			
			
			json = gson.toJson(lista);
		} catch (Exception e) {
			System.out.println("Sei PessoaEmailInstitucionalRS : " + e.getMessage());
			InfoWSVO errorInfoRSVO = new InfoWSVO();
			errorInfoRSVO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			errorInfoRSVO.setMessage(e.getMessage());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.INTERNAL_SERVER_ERROR);
		}
		return json;
	}
	
	

}
