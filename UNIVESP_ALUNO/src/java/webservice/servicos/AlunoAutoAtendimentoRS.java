package webservice.servicos;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;  

import negocio.facade.jdbc.arquitetura.SuperFacadeJDBC;

@Path("/alunoAutoAtendimento")
public class AlunoAutoAtendimentoRS  extends SuperFacadeJDBC {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3175273040129739233L;

	@GET
	@Produces("application/json; charset=utf-8")
	@Path("/consultarAlunoPorCPF/{cpf}")		
	public Response consultarAlunoPorCPF(@PathParam("cpf") final String cpf) {
		AlunoAutoAtendimentoRSVO alunoAutoAtendimentoRSVO;
		try {
			alunoAutoAtendimentoRSVO = getFacadeFactory().getPessoaFacade().consultarAlunoAutoAtendimentoPorCPF(cpf);
			if(alunoAutoAtendimentoRSVO.getAluno().trim().isEmpty()){
				return Response.status(Response.Status.NOT_FOUND).entity("Nenhum aluno encontrado com este CPF ("+cpf+").").build();
			}
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();			
		}		
		return Response.ok(alunoAutoAtendimentoRSVO, MediaType.APPLICATION_JSON+";charset=utf-8").build();
	}
	
}
