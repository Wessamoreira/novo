package webservice.servicos;

import controle.arquitetura.SuperControle;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;

/**
 *
 * @author Carlos
 */
@Path("/caminho")
public class CaminhoUploadRS extends SuperControle {

    @GET
    //@Produces({"application/xml", "application/json"})
    @Produces("application/json")
    @Path("/upload/{conf}")
    public CaminhoUploadObject getValidacaoPorMatricula(
            @PathParam("conf") final Integer conf,
            @Context final HttpServletRequest request,
            @Context final SecurityContext security) {
        final CaminhoUploadObject objeto = new CaminhoUploadObject();
        objeto.setConf(conf);
        try {
            // Autenticacao.validarOrigem(request, security);
            //objeto.setCaminho(getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarCaminhoGerenciadorUploadArquivoFixoConfiguracao(objeto.getConf()));
        } catch (Exception e) {
            /* Registar falha no serviço */
            // //System.out.println("Erro:" + e.getMessage());
            objeto.setCaminho("");
        }
        return objeto;
    }
}
