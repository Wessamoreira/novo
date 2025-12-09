package webservice.servicos;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.GenericEntity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;  
import jakarta.ws.rs.core.Response.Status;

import org.springframework.stereotype.Service;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoVisaoAlunoEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import webservice.servicos.excepetion.ErrorInfoRSVO;
import webservice.servicos.excepetion.WebServiceException;

@Service
@Path("/aplicativoSEISV/listaExercicio")
public class ListaExercicioSV extends SuperControle implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -540884645888552474L;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/consultarDisciplinaExercicioAluno/{matricula}/{ano}/{semestre}/{codigoPerfilAcesso}")
	public Response consultarDisciplinaExercicioAluno(@PathParam("matricula") final String matricula, @PathParam("ano") final String ano, @PathParam("semestre") final String semestre, @PathParam("codigoPerfilAcesso") final String codigoPerfilAcesso) throws Exception {
		try {
			UsuarioVO usuarioVO = autenticarConexao();
			usuarioVO.setVisaoLogar("aluno");
			usuarioVO.getPerfilAcesso().setCodigo(Integer.valueOf(codigoPerfilAcesso));
			getFacadeFactory().getControleAcessoFacade().consultar(PerfilAcessoPermissaoVisaoAlunoEnum.LISTA_EXERCICIO_ALUNO.getValor(), true, usuarioVO);
			String periodicidade = getFacadeFactory().getCursoFacade().consultarPeriodicidadePorMatricula(matricula, usuarioVO);
			String anoSemestre = ano+"/"+semestre;
			if(periodicidade.equals("IN")) {
				anoSemestre = "";
			}else if(periodicidade.equals("AN")) {
				anoSemestre = ano;
			}
			List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs = 
					getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarDisciplinaDoAlunoPorMatricula(matricula, anoSemestre, null, null, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO).stream().filter(d -> d.getQtdeListaExercicio() > 0).collect(Collectors.toList());
			
			return Response.status(Status.OK).entity(new GenericEntity<List<MatriculaPeriodoTurmaDisciplinaVO>>(matriculaPeriodoTurmaDisciplinaVOs) {}).build();
		}catch (Exception e) {
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
	}

}
