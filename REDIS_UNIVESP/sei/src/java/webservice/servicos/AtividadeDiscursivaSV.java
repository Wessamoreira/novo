package webservice.servicos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.stereotype.Service;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.AtividadeDiscursivaRespostaAlunoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.processosel.DisciplinasGrupoDisciplinaProcSeletivo;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import webservice.servicos.excepetion.ErrorInfoRSVO;
import webservice.servicos.excepetion.WebServiceException;

@Service
@Path("/aplicativoSEISV/atividadeDiscursiva")
public class AtividadeDiscursivaSV extends SuperControle implements Serializable  {
	
	private static final long serialVersionUID = 1L;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/consultarAtividadeDiscursivaRespostaAlunoPorMatricula/{matricula}/{ano}/{semestre}/{codigoPerfilAcesso}")
	public List<AtividadeDiscursivaRespostaAlunoVO> consultarAtividadeDiscursivaRespostaAlunoPorMatricula(@PathParam("matricula") final String matricula, 
			@PathParam("ano") final String ano, @PathParam("semestre") final String semestre, @PathParam("codigoPerfilAcesso") final String codigoPerfilAcesso) {
		try {
			UsuarioVO usuarioVO = autenticarConexao();
			usuarioVO.getPerfilAcesso().setCodigo(Integer.valueOf(codigoPerfilAcesso));
			usuarioVO.setVisaoLogar("aluno");
			MatriculaVO matriculaVO = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(matricula, null, NivelMontarDados.BASICO, usuarioVO);

			List<AtividadeDiscursivaRespostaAlunoVO> listaAtividadeDiscursivaRespostaAlunoVOs =  getFacadeFactory().getAtividadeDiscursivaRespostaAlunoInterfaceFacade().consultarAtividadeDiscursivasPorMatriculaOuCodigoMatriculaPeriodoTurmaDisciplina(matriculaVO.getMatricula(),
				0,
				getUsuarioLogado(), 
				ano, 
				semestre);

			return listaAtividadeDiscursivaRespostaAlunoVOs;

		} catch (Exception e) {
			System.out.println("MOBILE ERROR: " + e.getMessage());
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
	}

}
