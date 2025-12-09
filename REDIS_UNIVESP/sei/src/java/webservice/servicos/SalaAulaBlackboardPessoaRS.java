package webservice.servicos;

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
import negocio.comuns.blackboard.SalaAulaBlackboardPessoaVO;
import negocio.comuns.blackboard.SalaAulaBlackboardVO;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisWebServiceUrl;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import webservice.arquitetura.InfoWSVO;
import webservice.servicos.excepetion.WebServiceException;

@Path(UteisWebServiceUrl.URL_SEI_SERVICO_SALA_AULA_BLACKBOARD_PESSOA)
public class SalaAulaBlackboardPessoaRS extends SuperControle {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -236835538696686762L;

	@POST
	@Path(UteisWebServiceUrl.URL_SEI_SERVICO_SALA_AULA_BLACKBOARD_PESSOA_HISTORICO)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String consultarPessoaEmailInstitucionalRS(@Context final HttpServletRequest request , String salaAulaBlackbord ) {
		String json = null;
		Gson gson = inicializaGson();
		List<SalaAulaBlackboardPessoaVO> lista = new ArrayList<>();
		try {
			InfoWSVO infoWSVO = validarDadosAutenticacaoTokenWebService(request);
			if(infoWSVO.getStatus() != Status.OK.getStatusCode()) {
				throw new StreamSeiException(infoWSVO.getMensagem());	
			}
			final SalaAulaBlackboardVO obj = gson.fromJson(salaAulaBlackbord, SalaAulaBlackboardVO.class);
			Integer codUsuario = Integer.parseInt(request.getParameter(UteisWebServiceUrl.ul)); 
			UsuarioVO usuario = getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(codUsuario, Uteis.NIVELMONTARDADOS_DADOSLOGIN, null);
			if(Uteis.isAtributoPreenchido(obj.getTurmaVO())) {
				obj.setTurmaVO(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(obj.getTurmaVO().getCodigo(), NivelMontarDados.BASICO, usuario));	
			}
			if(Uteis.isAtributoPreenchido(obj.getProgramacaoTutoriaOnlineVO())) {
				lista =  getFacadeFactory().getSalaAulaBlackboardPessoaFacade().consultarAlunosDoEadTurmaDisciplinaDisponivel(obj.getCursoVO(), obj.getTurmaVO(), obj.getDisciplinaVO(), obj.getAno(), obj.getSemestre(), obj.getBimestre(), obj.getProgramacaoTutoriaOnlineVO(), true, obj.getCodigo(), usuario);
			}else {
				//Optional<SalaAulaBlackboardPessoaVO> professor = obj.getListaProfessores().stream().findFirst();				
				lista =  getFacadeFactory().getSalaAulaBlackboardPessoaFacade().consultarAlunosDoHorarioTurmaDisciplinaDisponivel( 0, obj.getCursoVO().getCodigo(),obj.getTurmaVO(), obj.getDisciplinaVO().getCodigo(), obj.getAno(), obj.getSemestre(), null, true, obj.getCodigo(), getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(), usuario);
			}			
			
			json = gson.toJson(lista);
		} catch (Exception e) {
			System.out.println("Sei SalaAulaBlackboardPessoaRS : " + e.getMessage());
			InfoWSVO errorInfoRSVO = new InfoWSVO();
			errorInfoRSVO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			errorInfoRSVO.setMessage(e.getMessage());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.INTERNAL_SERVER_ERROR);
		}
		return json;
	}

}
