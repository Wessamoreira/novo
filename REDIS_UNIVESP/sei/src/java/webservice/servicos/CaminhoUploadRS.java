package webservice.servicos;

import controle.arquitetura.SuperControle;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

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
