package webservice.techcert;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.DocumentoAssinadoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisWebServiceUrl;
import org.springframework.http.HttpStatus;
import webservice.arquitetura.InfoWSVO;
import webservice.servicos.excepetion.WebServiceException;
import webservice.techcert.comuns.WebhookEventTypeTechCertVO;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path(UteisWebServiceUrl.URL_SEI_WEB_SERVICE_TECHCERT)
public class TechCertRS extends SuperControle {

    private static final long serialVersionUID = 2698257528161160954L;

    @POST
    @Path(UteisWebServiceUrl.URL_SEI_WEB_SERVICE_TECHCERT_DOCUMENTO)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response documentTechCert(@Context final HttpServletRequest request, String json) {
        try {
            InfoWSVO infoWSVO = validarDadosAutenticacaoBearerTokenWebService(request);
            if (infoWSVO.getStatus() != Response.Status.OK.getStatusCode()) {
                throw new StreamSeiException(infoWSVO.getMensagem());
            }
            JsonParser parser = new JsonParser();
            JsonElement tree = parser.parse(json);
            JsonObject root = tree.getAsJsonObject();
            String type = root.get("type").getAsString();
            if (WebhookEventTypeTechCertVO.DOCUMENT_SIGNED.getValue().equalsIgnoreCase(type)) {
                UsuarioVO usuario = new UsuarioVO();
                if (Uteis.isAtributoPreenchido(getAplicacaoControle().getConfiguracaoGeralSistemaVO(null, null).getUsuarioResponsavelOperacoesExternas())) {
                    usuario = getFacadeFactory().getUsuarioFacade().consultarPorPessoa(getAplicacaoControle().getConfiguracaoGeralSistemaVO(null, null).getUsuarioResponsavelOperacoesExternas().getCodigo(), Boolean.FALSE, Uteis.NIVELMONTARDADOS_DADOSLOGIN, usuario);
                }

                String id = root
                        .getAsJsonObject("data")
                        .get("id")
                        .getAsString();

                DocumentoAssinadoVO docConsultadoVO = getFacadeFactory().getDocumentoAssinadoFacade().consultarPorChaveProvedordeAssinatura(id, Uteis.NIVELMONTARDADOS_DADOSDOCUMENTOASSINADO, new UsuarioVO());
                if (docConsultadoVO != null) {
                    getFacadeFactory().getDocumentoAssinadoFacade().executarProcessamentoDocumentosAssinadoEletronicaMenteValidandoSituacaoAssinaturaPorProvedorTechCertApi(docConsultadoVO, usuario);
                }
            }

            if (isWebhookEventTypeTechCert(type)) {
                UsuarioVO usuario = new UsuarioVO();
                if (Uteis.isAtributoPreenchido(getAplicacaoControle().getConfiguracaoGeralSistemaVO(null, null).getUsuarioResponsavelOperacoesExternas())) {
                    usuario = getFacadeFactory().getUsuarioFacade().consultarPorPessoa(getAplicacaoControle().getConfiguracaoGeralSistemaVO(null, null).getUsuarioResponsavelOperacoesExternas().getCodigo(), Boolean.FALSE, Uteis.NIVELMONTARDADOS_DADOSLOGIN, usuario);
                }

                JsonObject data = root.getAsJsonObject("data");
                List<String> documentIds = new ArrayList<>();

                if (data.has("documents") && data.get("documents").isJsonArray()) {
                    JsonArray docsArray = data.getAsJsonArray("documents");
                    for (JsonElement elem : docsArray) {
                        String id = elem.getAsJsonObject()
                                .get("id").getAsString();
                        documentIds.add(id);
                    }
                } else if (data.has("id")) {
                    String id = data.get("id").getAsString();
                    documentIds.add(id);
                }
                for (String id : documentIds) {
                    DocumentoAssinadoVO doc = getFacadeFactory().getDocumentoAssinadoFacade().consultarPorChaveProvedordeAssinatura(id, Uteis.NIVELMONTARDADOS_DADOSDOCUMENTOASSINADO, new UsuarioVO());
                    if (doc != null) {
                        getFacadeFactory().getDocumentoAssinadoFacade().executarProcessamentoDocumentosAssinadoEletronicaMenteValidandoSituacaoAssinaturaPorProvedorTechCertApi(doc, usuario);
                    }
                }
            }
            return Response.status(Response.Status.OK).build();
        } catch (Exception e) {
//            System.out.println(json);
//            System.out.println(e.getMessage());
            InfoWSVO errorInfoRSVO = new InfoWSVO();
            errorInfoRSVO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorInfoRSVO.setMessage(e.getMessage());
            errorInfoRSVO.setMensagem(e.getMessage());
            throw new WebServiceException(errorInfoRSVO, Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    private Boolean isWebhookEventTypeTechCert(String webhookEventType) {
        return WebhookEventTypeTechCertVO.DOCUMENT_REFUSED.getValue().equalsIgnoreCase(webhookEventType)
                || WebhookEventTypeTechCertVO.DOCUMENT_CANCELED.getValue().equalsIgnoreCase(webhookEventType)
                || WebhookEventTypeTechCertVO.DOCUMENT_EXPIRED.getValue().equalsIgnoreCase(webhookEventType)
                || WebhookEventTypeTechCertVO.DOCUMENT_DELETED.getValue().equalsIgnoreCase(webhookEventType);
    }
}
