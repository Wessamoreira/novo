package webservice.servicos;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import jobs.JobEnvioEmail;
import controle.arquitetura.AplicacaoControle;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.SuperFacadeJDBC;

@Path("/financeiroIntegracao")
public class IntegracaoFinanceiraRS extends SuperFacadeJDBC {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -757221068487089812L;

	@GET
	@Produces("text/html")
	@Path("/processarArquivo/{arquivo}")
	public String processarArquivo(@PathParam("arquivo") final String codigoArquivo, @Context final HttpServletRequest request, @Context final SecurityContext security) throws Exception {		
		if(Uteis.isAtributoPreenchido(codigoArquivo) && Integer.valueOf(codigoArquivo) > 0){
			if(AplicacaoControle.realizarRegistroInicioProcessamentoIntegracaoFinanceira(Integer.valueOf(codigoArquivo))){			
				getFacadeFactory().getIntegracaoFinanceiroFacade().executarIntegracaoFinanceiro(Integer.valueOf(codigoArquivo));
				AplicacaoControle.realizarRegistroTerminoProcessamentoIntegracaoFinanceira(Integer.valueOf(codigoArquivo));
				return "ok";
			}	
			JobEnvioEmail.enviarSMSNotificacaoEquipeOtimize("TENTATIVA PROCESSAMENTO INTEGRACAO FINANCEIRA EM PARALELISMO ("+codigoArquivo+") ");
			return "bloqueado";
		}else{
			return "ACESSO RESTRITO";
		}
	}

}
