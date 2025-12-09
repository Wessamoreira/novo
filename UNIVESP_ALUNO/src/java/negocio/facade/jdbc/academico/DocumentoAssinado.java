package negocio.facade.jdbc.academico;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import java.io.*;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Provider;
import java.security.Security;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import jakarta.ws.rs.core.MediaType;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.util.concurrent.RateLimiter;
import com.google.gson.JsonArray;
import negocio.comuns.administrativo.*;
import negocio.comuns.basico.enumeradores.TipoProvedorAssinaturaEnum;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.security.PdfPKCS7;

import controle.arquitetura.AplicacaoControle;
import controle.arquitetura.AssuntoDebugEnum;
import controle.arquitetura.DataModelo;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.RequestBodyEntity;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.DocumentoAssinadoPessoaVO;
import negocio.comuns.academico.DocumentoAssinadoVO;
import negocio.comuns.academico.EstagioVO;
import negocio.comuns.academico.ExpedicaoDiplomaVO;
//import negocio.comuns.academico.GestaoXmlGradeCurricularVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PlanoEnsinoVO;
import negocio.comuns.academico.ProgramacaoFormaturaAlunoVO;
import negocio.comuns.academico.ProgramacaoFormaturaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.AlinhamentoAssinaturaDigitalEnum;
import negocio.comuns.academico.enumeradores.DocumentoAssinadoOrigemEnum;
import negocio.comuns.academico.enumeradores.OperacaoDeVinculoEstagioEnum;
import negocio.comuns.academico.enumeradores.SituacaoDocumentoAssinadoPessoaEnum;
import negocio.comuns.academico.enumeradores.TipoAssinaturaDocumentoEnum;
import negocio.comuns.academico.enumeradores.TipoOrigemDocumentoAssinadoEnum;
import negocio.comuns.academico.enumeradores.VersaoDiplomaDigitalEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.ServidorArquivoOnlineEnum;
import negocio.comuns.basico.ConfiguracaoGEDVO;
import negocio.comuns.basico.ConfiguracaoGedOrigemVO;
import negocio.comuns.basico.PessoaEmailInstitucionalVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.basico.enumeradores.ProvedorDeAssinaturaEnum;
import negocio.comuns.estagio.ConfiguracaoEstagioObrigatorioVO;
import negocio.comuns.estagio.enumeradores.SituacaoAdicionalEstagioEnum;

import negocio.comuns.utilitarias.AssinaturaDigialDocumentoPDF;
import negocio.comuns.utilitarias.CertificadoDigital;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Constantes;
import negocio.comuns.utilitarias.ConverterImgToPdf;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UteisWebServiceUrl;
import negocio.comuns.utilitarias.dominios.OrigemArquivo;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.dominios.SituacaoArquivo;
import negocio.comuns.utilitarias.dominios.SituacaoVinculoMatricula;
import negocio.comuns.utilitarias.dominios.TimeType;
import negocio.comuns.utilitarias.dominios.TipoPessoa;

import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.academico.DocumentoAssinadoInterfaceFacade;
import relatorio.negocio.comuns.academico.HistoricoAlunoRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import webservice.arquitetura.InfoWSVO;
import webservice.certisign.comuns.CertiSignCallBackRSVO;
import webservice.certisign.comuns.CertiSignCallBackSignaturePessoaRSVO;
import webservice.certisign.comuns.CertiSignCallBackSignatureValidateRSVO;
import webservice.certisign.comuns.CertiSignRSVO;
import webservice.certisign.comuns.DeadlineRSVO;
import webservice.certisign.comuns.DocumentPessoaRSVO;
import webservice.certisign.comuns.DocumentRSVO;
import webservice.certisign.comuns.UploadRSVO;
import webservice.certisign.comuns.novaApi.AtendeeRSVO;
import webservice.certisign.comuns.novaApi.DocumentFlowActionsResponseRSVO;
import webservice.certisign.comuns.novaApi.StatusAssinaturaCertisign;
import webservice.certisign.comuns.novaApi.StepRSVO;
import webservice.nfse.generic.AmbienteEnum;
import webservice.techcert.comuns.*;

@Repository
@Scope("singleton")
public class DocumentoAssinado extends ControleAcesso implements DocumentoAssinadoInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7088210930902199266L;
	private static final List<String> listaDocumentoAssinadoSeiSignature = Arrays.asList("DIPLOMA_DIGITAL", "DIPLOMA_DIGITAL_VISUAL", "DOCUMENTACAO_ACADEMICA_REGISTRO_DIPLOMA_DIGITAL", "HISTORICO_DIGITAL", "HISTORICO_DIGITAL_VISUAL", "CURRICULO_ESCOLAR_DIGITAL", "CURRICULO_ESCOLAR_DIGITAL_VISUAL");
	protected static String idEntidade;
    private static int minimoAssinantes = 1;

	private String realizarEnvioUpload(DocumentoAssinadoVO obj, ConfiguracaoGEDVO configGedVO, String arquivoOrigem ) throws Exception {
		RequestBodyEntity jsonEntity= null;
		try {
			File fileTemp = new File(arquivoOrigem);
			Uteis.checkState(!fileTemp.exists(), "Arquivo não foi encontrado no caminho "+arquivoOrigem+" para ser enviado ao Provedor de Assinatura.");
			byte[] fileContent = Files.readAllBytes(fileTemp.toPath());
			StringBuilder sb = new StringBuilder("");
			sb.append("{\"fileName\":").append("\"").append(obj.getArquivo().getNome()).append("\",");
			sb.append("\"bytes\":\"").append(Base64.getEncoder().encodeToString(fileContent)).append("\"}");		
			jsonEntity = unirest().post(configGedVO.getUrlProvedorAssinatura(obj.getProvedorDeAssinaturaEnum()) + UteisWebServiceUrl.URL_SEI_WEB_SERVICE_CERTISIGN_UPLOAD)
			.header(UteisWebServiceUrl.Content_Type, UteisWebServiceUrl.Application_JSON)
			.header(configGedVO.getChaveToken(obj.getProvedorDeAssinaturaEnum()), configGedVO.getValorTokenProvedorAssinatura(obj.getProvedorDeAssinaturaEnum()))
			.body(sb.toString());
			HttpResponse<JsonNode> jsonResponse = jsonEntity.asJson();
			if(jsonResponse.isSuccess() || jsonResponse.getStatus() == 200) {
				UploadRSVO upload = executarVerificacaoRetornoObjeto(jsonResponse, UploadRSVO.class, "");
				return upload.getUploadId();	
			}else {
				InfoWSVO rep = new Gson().fromJson(jsonResponse.getBody().toString(), InfoWSVO.class);
				tratarMensagemErroWebService(rep, String.valueOf(jsonResponse.getStatus()), jsonResponse.getBody().toString());
				return "";
			}
		} catch (Exception e) {
			throw e;
			
		} finally {
			jsonEntity = null;
		}
	}

    private CertiSignRSVO realizarEnvioCreateDocument(CertiSignRSVO csRSVO, ProvedorDeAssinaturaEnum provedorDeAssinaturaEnum, ConfiguracaoGEDVO configGedVO) throws Exception {
        RequestBodyEntity jsonEntity = null;
        try {
            Gson gson = inicializaGson();
            jsonEntity = unirest().post(configGedVO.getUrlProvedorAssinatura(provedorDeAssinaturaEnum) + UteisWebServiceUrl.URL_SEI_WEB_SERVICE_CERTISIGN_CREATE_DOCUMENT)
                    .header(UteisWebServiceUrl.Content_Type, UteisWebServiceUrl.Application_JSON)
                    .header(configGedVO.getChaveToken(provedorDeAssinaturaEnum), configGedVO.getValorTokenProvedorAssinatura(provedorDeAssinaturaEnum))
                    .body(gson.toJson(csRSVO));
            HttpResponse<JsonNode> jsonResponse = jsonEntity.asJson();
            if (jsonResponse.isSuccess() || jsonResponse.getStatus() == 200) {
                return executarVerificacaoRetornoObjeto(jsonResponse, CertiSignRSVO.class, "");
            } else {
                InfoWSVO rep = new Gson().fromJson(jsonResponse.getBody().toString(), InfoWSVO.class);
                tratarMensagemErroWebService(rep, String.valueOf(jsonResponse.getStatus()), jsonResponse.getBody().toString());
                return null;
            }
        } catch (Exception e) {
            throw e;
        } finally {
            jsonEntity = null;
        }
    }

    public void realizarExclusaoDocument(DocumentoAssinadoVO doc, ConfiguracaoGEDVO configGedVO) throws Exception {
        try {
            if (!configGedVO.getHabilitarIntegracaoTechCert()){
                throw new StreamSeiException("Configuração de integração com TechCert não está habilitada.");
            }
            HttpResponse<JsonNode> jsonResponse = unirest().delete(configGedVO.getUrlProvedorAssinatura(doc.getProvedorDeAssinaturaEnum()) + UteisWebServiceUrl.URL_SEI_WEB_SERVICE_CERTISIGN_DELETE)
                    .header(UteisWebServiceUrl.Content_Type, UteisWebServiceUrl.Application_JSON)
                    .header(UteisWebServiceUrl.token, configGedVO.getTokenProvedorDeAssinatura())
                    .queryString("id", doc.getCodigoProvedorDeAssinatura())
                    .asJson();
            if (jsonResponse.getStatus() != 200 && jsonResponse.getStatus() != 406) {
                InfoWSVO rep = new Gson().fromJson(jsonResponse.getBody().toString(), InfoWSVO.class);
                tratarMensagemErroWebService(rep, String.valueOf(jsonResponse.getStatus()), jsonResponse.getBody().toString());
            }
        } catch (Exception e) {
            if (!e.getMessage().contains("A ação não pôde ser realizada porque o documento foi excluído.")) {
                throw e;
            }
        }
    }

    public DocumentsTechCertVO realizarExclusaoDocumentTechCert(DocumentoAssinadoVO doc, ConfiguracaoGEDVO configGedVO) {
        if (doc.getChaveProvedorDeAssinatura() == null && doc.getTipoOrigemDocumentoAssinadoEnum().isTermoEstagioObrigatorio()) {
            DocumentsTechCertVO documentsTechCertVO = new DocumentsTechCertVO();
            documentsTechCertVO.setDeletedDocumentTechCert(true);
            return documentsTechCertVO;
        }
        if (doc.getGerarNovoTermoESolicitarNovasAssinaturas()) {
            //TODO CHAMAR A API DE VERSIONAMENTO
        }
        if (!configGedVO.getHabilitarIntegracaoTechCert()){
            throw new StreamSeiException("Configuração de integração com TechCert não está habilitada.");
        }
        try {
            JSONObject body = new JSONObject();
            body.put("reason", Uteis.isAtributoPreenchido(doc.getMotivoDocumentoAssinadoInvalido()) ? doc.getMotivoDocumentoAssinadoInvalido() : "Cancelamento a pedido do usuário");
            HttpResponse<JsonNode> jsonResponse = unirest()
                    .post(montarUrlPadrao(configGedVO) + UteisWebServiceUrl.URL_TECHCERT_DOCUMENTS + "/" + doc.getChaveProvedorDeAssinatura() + "/cancellation")
                    .header(UteisWebServiceUrl.Content_Type, UteisWebServiceUrl.Application_JSON)
                    .header(configGedVO.getApikeyTechCert(), montarToken(configGedVO))
                    .body(body).asJson();
            if (jsonResponse.isSuccess() || jsonResponse.getStatus() == 200) {
                Gson gson = new GsonBuilder().create();
                return gson.fromJson(jsonResponse.getBody().toString(), DocumentsTechCertVO.class);
            } else if (jsonResponse.getStatus() == 422) {
                JSONObject error = jsonResponse.getBody().getObject();
                String code = error.optString("code", "");
                if ("DocumentIsDeleted".equalsIgnoreCase(code)) {
                    DocumentsTechCertVO documentsTechCertVO = new DocumentsTechCertVO();
                    documentsTechCertVO.setDeletedDocumentTechCert(true);
                    return documentsTechCertVO;
                }
                if ("CannotCancelExpiredDocument".equalsIgnoreCase(code)) {
                    return null;
                } else if ("DocumentAlreadyCanceled".equalsIgnoreCase(code)) {
                    DocumentsTechCertVO documentsTechCertVO = new DocumentsTechCertVO();
                    documentsTechCertVO.setDocumentCancelledTechCert(true);
                    return documentsTechCertVO;
                } else {
                    tratarErroTechCert(jsonResponse.getBody().toString(), jsonResponse.getStatus());
                    throw new StreamSeiException(jsonResponse.getBody().toString());
                }
            } else {
                tratarErroTechCert(jsonResponse.getBody().toString(), jsonResponse.getStatus());
                throw new StreamSeiException(jsonResponse.getBody().toString());
            }
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void realizarBloqueioDocument(DocumentoAssinadoVO doc, ConfiguracaoGEDVO configGedVO, UsuarioVO usuario) throws Exception {
        RequestBodyEntity jsonEntity = null;
        try {
            StringBuilder sb = new StringBuilder("");
            sb.append("{\"documents\":[").append(doc.getCodigoProvedorDeAssinatura()).append("],");
            sb.append("\"status\":").append(doc.isDocumentoAssinadoInvalido()).append("}");
            jsonEntity = unirest().post(configGedVO.getUrlProvedorAssinatura(doc.getProvedorDeAssinaturaEnum()) + UteisWebServiceUrl.URL_SEI_WEB_SERVICE_CERTISIGN_BLOQUEIO)
                    .header(UteisWebServiceUrl.Content_Type, UteisWebServiceUrl.Application_JSON)
                    .header(configGedVO.getChaveToken(doc.getProvedorDeAssinaturaEnum()), configGedVO.getValorTokenProvedorAssinatura(doc.getProvedorDeAssinaturaEnum()))
                    .body(sb.toString());
            HttpResponse<JsonNode> jsonResponse = jsonEntity.asJson();
            if (jsonResponse.getBody().toString().contains("A ação não pôde ser realizada porque o documento foi excluído.")) {
                excluir(doc, false, usuario, getAplicacaoControle().getConfiguracaoGeralSistemaVO(doc.getUnidadeEnsinoVO().getCodigo(), usuario));
            } else if (!jsonResponse.isSuccess() || jsonResponse.getStatus() != 200) {
                InfoWSVO rep = new Gson().fromJson(jsonResponse.getBody().toString(), InfoWSVO.class);
                tratarMensagemErroWebService(rep, String.valueOf(jsonResponse.getStatus()), jsonResponse.getBody().toString());
            }
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void realizarBloqueioDocumentTechCert(DocumentoAssinadoVO doc, ConfiguracaoGEDVO configGedVO, UsuarioVO usuario) throws Exception {
        try {
            if (!configGedVO.getHabilitarIntegracaoTechCert()){
                throw new StreamSeiException("Configuração de integração com TechCert não está habilitada.");
            }
            JSONObject body = new JSONObject();
            body.put("reason", Uteis.isAtributoPreenchido(doc.getMotivoDocumentoAssinadoInvalido()) ? doc.getMotivoDocumentoAssinadoInvalido() : "Realizar BloqueioDocument");
            HttpResponse<JsonNode> jsonResponse = unirest()
                    .post(montarUrlPadrao(configGedVO) + UteisWebServiceUrl.URL_TECHCERT_DOCUMENTS + "/" + doc.getChaveProvedorDeAssinatura() + "/cancellation")
                    .header(UteisWebServiceUrl.Content_Type, UteisWebServiceUrl.Application_JSON)
                    .header(configGedVO.getApikeyTechCert(), montarToken(configGedVO))
                    .body(body).asJson();
            // foi adicionado no IF a validação de que vai excluir o documento se não for ata de colação de grau
            // pois ao regerar a ata de colação de grau ao invez de excluir o documento apenas o rejeita
            if ((jsonResponse.isSuccess() || jsonResponse.getStatus() == 200) && !doc.getTipoOrigemDocumentoAssinadoEnum().isAtaColacaoGrau()) {
                excluir(doc, false, usuario, getAplicacaoControle().getConfiguracaoGeralSistemaVO(doc.getUnidadeEnsinoVO().getCodigo(), usuario));
            } else if (jsonResponse.getStatus() == 422) {
                JSONObject error = jsonResponse.getBody().getObject();
                String code = error.optString("code", "");
                if ("DocumentIsDeleted".equalsIgnoreCase(code) || "DocumentAlreadyCanceled".equalsIgnoreCase(code)
                        || "CannotCancelExpiredDocument".equalsIgnoreCase(code)) {
                    excluir(doc, false, usuario, getAplicacaoControle().getConfiguracaoGeralSistemaVO(doc.getUnidadeEnsinoVO().getCodigo(), usuario));
                } else {
                    tratarErroTechCert(jsonResponse.getBody().toString(), jsonResponse.getStatus());
                }
            }
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void realizarEnvioNotificacaoLembreteDocumentoPendenteAssinatura(DocumentoAssinadoVO doc, ConfiguracaoGEDVO configGedVO) throws Exception {
        RequestBodyEntity jsonEntity = null;
        try {
            if (doc.getProvedorDeAssinaturaEnum().isProvedorCertisign()) {
                jsonEntity = unirest().post(configGedVO.getUrlProvedorAssinatura(doc.getProvedorDeAssinaturaEnum()) + UteisWebServiceUrl.URL_SEI_WEB_SERVICE_CERTISIGN_SEND_REMINDER)
                        .header(UteisWebServiceUrl.Content_Type, UteisWebServiceUrl.Application_JSON)
                        .header(configGedVO.getChaveToken(doc.getProvedorDeAssinaturaEnum()), configGedVO.getValorTokenProvedorAssinatura(doc.getProvedorDeAssinaturaEnum()))
                        .body("{documents: [" + doc.getCodigoProvedorDeAssinatura() + "] }");
                HttpResponse<JsonNode> jsonResponse = jsonEntity.asJson();
                if (jsonResponse.getStatus() != 200 && !jsonResponse.isSuccess()) {
                    InfoWSVO rep = new Gson().fromJson(jsonResponse.getBody().toString(), InfoWSVO.class);
                    tratarMensagemErroWebService(rep, String.valueOf(jsonResponse.getStatus()), jsonResponse.getBody().toString());
                }
            }

            if (doc.getProvedorDeAssinaturaEnum().isProvedorTechCert()) {
                // 1) Monta a lista de emails dos pendentes
                JSONArray emailsJson = new JSONArray();
                for (DocumentoAssinadoPessoaVO docPessoa : doc.getListaDocumentoAssinadoPessoa()) {
                    if (SituacaoDocumentoAssinadoPessoaEnum.PENDENTE == docPessoa.getSituacaoDocumentoAssinadoPessoaEnum()) {
                        if (Uteis.isAtributoPreenchido(docPessoa.getEmailPessoa())) {
                            emailsJson.put(docPessoa.getEmailPessoa());
                        }
                    }
                }

                JSONObject body = new JSONObject();
                body.put("emails", emailsJson);

                HttpResponse<JsonNode> response = unirest().post(
                                montarUrlPadrao(configGedVO) + UteisWebServiceUrl.URL_TECHCERT_USERS
                                        + UteisWebServiceUrl.URL_TECHCERT_NOTIFY_PENDING)
                        .header(UteisWebServiceUrl.Content_Type, UteisWebServiceUrl.Application_JSON)
                        .header(configGedVO.getApikeyTechCert(), montarToken(configGedVO))
                        .body(body)
                        .asJson();
                if (response.getStatus() != 200 || !response.isSuccess()) {
                    InfoWSVO rep = new Gson().fromJson(response.getBody().toString(), InfoWSVO.class);
                    tratarMensagemErroWebService(rep, String.valueOf(response.getStatus()), response.getBody().toString());
                }
            }

        } catch (Exception e) {
            throw e;
        }
    }


    @Override
    public void realizarProcessoAlteracaoEmailNotificacaoAssinaturaCertSign(DocumentoAssinadoPessoaVO doc, EstagioVO estagioConcedenteAlteracaoEmailNotificacaoPendente, String emailEnviarNotificacaoAssinaturaConcedente, UsuarioVO usuarioLogado) throws Exception {

        if (!Uteis.isAtributoPreenchido(emailEnviarNotificacaoAssinaturaConcedente)) {
            throw new Exception("O campo e-mail deve ser informado.");
        }
        if (doc.getEmailPessoa().equals(emailEnviarNotificacaoAssinaturaConcedente)) {
            throw new Exception("O novo e-mail deve ser diferente do antigo e-mail informado .");
        }
        if (doc.getDocumentoAssinadoVO().getProvedorDeAssinaturaEnum().isProvedorCertisign()) {

            ConfiguracaoGEDVO configGedVO = getFacadeFactory().getConfiguracaoGEDFacade().consultarPorUnidadeEnsino(doc.getDocumentoAssinadoVO().getUnidadeEnsinoVO().getCodigo(), false, usuarioLogado);

            // realizando a exclusao do signer no documento
            getFacadeFactory().getDocumentoAssinadoFacade().realizarExclusaoParticipantDiscard(doc, configGedVO);

            // criando o mesmo signer so que com o novo email
            CertiSignRSVO csRSVO = new CertiSignRSVO();
            csRSVO.setDocumentId(stringParaIntegerOuNull(doc.getDocumentoAssinadoVO().getCodigoProvedorDeAssinatura()));

            List<DocumentPessoaRSVO> listaElectronicSigners = new ArrayList<>();
            List<DocumentPessoaRSVO> listaSigners = new ArrayList<>();

            DocumentPessoaRSVO dpRSVO = new DocumentPessoaRSVO();
            dpRSVO.setName(doc.getNomePessoa());
            dpRSVO.setEmail(emailEnviarNotificacaoAssinaturaConcedente);

            Uteis.checkState(!Uteis.validaCPF(Uteis.removerAcentos(Uteis.removeCaractersEspeciais(doc.getCpfPessoa())).replaceAll(" ", "")), "O campo cpf para o assinante - " + doc.getNomePessoa() + " não é válido");
            dpRSVO.setIndividualIdentificationCode(doc.getCpfPessoa());
            dpRSVO.setStep(0);
            dpRSVO.setTitle("Assinatura");
            if (doc.getTipoPessoa().isAluno() || doc.getTipoPessoa().isMembroComunidade() || !Uteis.isAtributoPreenchido(doc.getPessoaVO())) {
                listaElectronicSigners.add(dpRSVO);
            } else if (Uteis.isAtributoPreenchido(doc.getPessoaVO()) && (doc.getTipoPessoa().isFuncionario() || doc.getTipoPessoa().isProfessor())) {
                if (!doc.getDocumentoAssinadoVO().getDocumentoContrato() || (doc.getDocumentoAssinadoVO().getDocumentoContrato() && Uteis.isAtributoPreenchido(dpRSVO.getEmail()))) {
                    listaSigners.add(dpRSVO);
                }
            }

            if (Uteis.isAtributoPreenchido(listaSigners)) {
                csRSVO.setSigners(listaSigners);
            }
            if (Uteis.isAtributoPreenchido(listaElectronicSigners)) {
                csRSVO.setElectronicSigners(listaElectronicSigners);
            }

            // realizando o envio do signer a ser adicionado ao documento  com o novo email
            CertiSignRSVO csRetorno = getFacadeFactory().getDocumentoAssinadoFacade().realizarEnvioParticipantAdd(csRSVO, doc, configGedVO);
            // realizando o envio do notificaçao de pendencia de assinatura para o novo e-mail alterado
            getFacadeFactory().getDocumentoAssinadoFacade().realizarEnvioNotificacaoLembreteDocumentoPendenteAssinatura(doc.getDocumentoAssinadoVO(), configGedVO);

            if (csRetorno.getSigners() != null && !csRetorno.getSigners().isEmpty()) {
                for (DocumentPessoaRSVO docPessoaRsVO : csRetorno.getSigners()) {
                    doc.setCodigoAssinatura(docPessoaRsVO.getSignerId().toString());
                }
            }
            if (csRetorno.getElectronicSigners() != null && !csRetorno.getElectronicSigners().isEmpty()) {
                for (DocumentPessoaRSVO docPessoaRsVO : csRetorno.getElectronicSigners()) {
                    doc.setCodigoAssinatura(docPessoaRsVO.getSignerId().toString());
                }
            }

            estagioConcedenteAlteracaoEmailNotificacaoPendente.getConcedenteVO().setEmailResponsavelConcedente(emailEnviarNotificacaoAssinaturaConcedente);
            estagioConcedenteAlteracaoEmailNotificacaoPendente.setEmailResponsavelConcedente(emailEnviarNotificacaoAssinaturaConcedente);
            doc.setEmailPessoa(emailEnviarNotificacaoAssinaturaConcedente);
            getFacadeFactory().getDocumentoAssinadoPessoaFacade().atualizarEmailSignatarioConcedente(doc);
            getFacadeFactory().getConcedenteFacade().atualizarEmailConcedente(estagioConcedenteAlteracaoEmailNotificacaoPendente.getConcedenteVO());
            getFacadeFactory().getEstagioFacade().atualizarEmailResponsavelConcedente(estagioConcedenteAlteracaoEmailNotificacaoPendente);


        }

    }

    @Override
    public void realizarProcessoAlteracaoEmailNotificacaoAssinaturaTechCert(DocumentoAssinadoPessoaVO doc, EstagioVO estagioConcedenteAlteracaoEmailNotificacaoPendente, String emailEnviarNotificacaoAssinaturaConcedente, UsuarioVO usuarioLogado) throws Exception {

        if(!Uteis.isAtributoPreenchido(emailEnviarNotificacaoAssinaturaConcedente)) {
			throw new Exception("O campo e-mail deve ser informado.");
		}
		if(doc.getEmailPessoa().equals(emailEnviarNotificacaoAssinaturaConcedente)) {
			throw new Exception("O novo e-mail deve ser diferente do antigo e-mail informado .");
		}

		if(doc.getDocumentoAssinadoVO().getProvedorDeAssinaturaEnum().isProvedorTechCert() ) {

			ConfiguracaoGEDVO configGedVO = getFacadeFactory().getConfiguracaoGEDFacade().consultarPorUnidadeEnsino(doc.getDocumentoAssinadoVO().getUnidadeEnsinoVO().getCodigo(), false, usuarioLogado);
            DocumentoAssinadoVO docCompleto = getFacadeFactory().getDocumentoAssinadoFacade().consultarPorChaveProvedordeAssinatura(doc.getDocumentoAssinadoVO().getChaveProvedorDeAssinatura(), Uteis.NIVELMONTARDADOS_DADOSDOCUMENTOASSINADO, usuarioLogado);
            DocumentoAssinadoPessoaVO docPessoaVO = docCompleto
                    .getListaDocumentoAssinadoPessoa().stream()
                    .filter(pessoaAtual ->
                            pessoaAtual.getNomePessoa().equalsIgnoreCase(doc.getNomePessoa())
                                    && pessoaAtual.getCpfPessoa().equalsIgnoreCase(doc.getCpfPessoa()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Pessoa não encontrada"));

            docPessoaVO.getPessoaVO().setNome(docPessoaVO.getNomePessoa());
            docPessoaVO.getPessoaVO().setEmail(emailEnviarNotificacaoAssinaturaConcedente);
            docPessoaVO.getPessoaVO().setCPF(docPessoaVO.getCpfPessoa());
            docPessoaVO.getPessoaVO().setTipoAssinaturaDocumentoEnum(docPessoaVO.getTipoAssinaturaDocumentoEnum());
            getFacadeFactory().getDocumentoAssinadoPessoaFacade().realizarTrocaDeAssinanteTechCert(docPessoaVO, docCompleto, docPessoaVO.getPessoaVO(), configGedVO, usuarioLogado);

            //localizar o assinante e setar nele o novo email para encaminhar para notificar
            for (DocumentoAssinadoPessoaVO dap : docCompleto.getListaDocumentoAssinadoPessoa()) {
                String cpf = Uteis.removerAcentos(Uteis.removeCaractersEspeciais(dap.getPessoaVO().getCPF())).replace(" ", "");
                String cpfPessoaAtual = Uteis.removerAcentos(Uteis.removeCaractersEspeciais(docPessoaVO.getCpfPessoa())).replace(" ", "");
                if (cpf.equalsIgnoreCase(cpfPessoaAtual)) {
                    dap.setEmailPessoa(emailEnviarNotificacaoAssinaturaConcedente);
                }
            }

            // APOS TROCA DO ASSINANTE É RECEBIDO O EMAIL
//            realizarEnvioNotificacaoLembreteDocumentoPendenteAssinatura(doc.getDocumentoAssinadoVO(), configGedVO);
			estagioConcedenteAlteracaoEmailNotificacaoPendente.getConcedenteVO().setEmailResponsavelConcedente(emailEnviarNotificacaoAssinaturaConcedente);
			estagioConcedenteAlteracaoEmailNotificacaoPendente.setEmailResponsavelConcedente(emailEnviarNotificacaoAssinaturaConcedente);
			doc.setEmailPessoa(emailEnviarNotificacaoAssinaturaConcedente);
			getFacadeFactory().getDocumentoAssinadoPessoaFacade().atualizarEmailSignatarioConcedente(doc);
			getFacadeFactory().getConcedenteFacade().atualizarEmailConcedente(estagioConcedenteAlteracaoEmailNotificacaoPendente.getConcedenteVO());
			getFacadeFactory().getEstagioFacade().atualizarEmailResponsavelConcedente(estagioConcedenteAlteracaoEmailNotificacaoPendente);
		}
    }


    @Override
    public void realizarExclusaoParticipantDiscard(DocumentoAssinadoPessoaVO doc, ConfiguracaoGEDVO configGedVO) throws Exception {
        RequestBodyEntity jsonEntity = null;
        String cpf = Uteis.isAtributoPreenchido(doc.getPessoaVO()) ? doc.getPessoaVO().getCPF() : doc.getCpfPessoa();
        String nome = Uteis.isAtributoPreenchido(doc.getPessoaVO()) ? doc.getPessoaVO().getNome() : doc.getNomePessoa();
        try {
            Uteis.checkState(!Uteis.validaCPF(Uteis.removerAcentos(Uteis.removeCaractersEspeciais(cpf)).replaceAll(" ", "")), "O campo cpf para o assinante - " + nome + " não é válido");
            String body = "{ "
                    + "documentId : " + doc.getDocumentoAssinadoVO().getCodigoProvedorDeAssinatura() + " , "
                    + "stageFlowId : 0 , "
                    + "individualIdentificationCode : '" + cpf + "'}";

            jsonEntity = unirest().post(configGedVO.getUrlProvedorAssinatura(doc.getDocumentoAssinadoVO().getProvedorDeAssinaturaEnum()) + UteisWebServiceUrl.URL_SEI_WEB_SERVICE_CERTISIGN_PARTICIPANT_DISCARD)
                    .header(UteisWebServiceUrl.Content_Type, UteisWebServiceUrl.Application_JSON)
                    .header(configGedVO.getChaveToken(doc.getDocumentoAssinadoVO().getProvedorDeAssinaturaEnum()), configGedVO.getValorTokenProvedorAssinatura(doc.getDocumentoAssinadoVO().getProvedorDeAssinaturaEnum()))
                    .body(body);
            HttpResponse<JsonNode> jsonResponse = jsonEntity.asJson();
            if (jsonResponse.getStatus() != 200 && !jsonResponse.isSuccess()) {
                InfoWSVO rep = new Gson().fromJson(jsonResponse.getBody().toString(), InfoWSVO.class);
                tratarMensagemErroWebService(rep, String.valueOf(jsonResponse.getStatus()), jsonResponse.getBody().toString());
            }
        } catch (Exception e) {
            if (!e.getMessage().contains("O Usuário não tem permissão para realizar essa operação") && !e.getMessage().contains("realizada porque o documento foi excluído")) {
                throw e;
            }
        }
    }

    @Override
    public void realizarExclusaoParticipantDiscardTechCert(DocumentoAssinadoPessoaVO doc, ConfiguracaoGEDVO configGedVO, DocumentoAssinadoVO documentoAssinadoVO) throws Exception {
        try {
            String flowActionId = doc.getCodigoAssinatura();
            JSONArray deletedIds = new JSONArray();
            deletedIds.put(flowActionId);
            JSONObject payload = new JSONObject();
            payload.put("deletedFlowActionIds", deletedIds);

            StringBuilder url = new StringBuilder();
            url.append(montarUrlPadrao(configGedVO) + UteisWebServiceUrl.URL_TECHCERT_DOCUMENTS
                    + "/" + documentoAssinadoVO.getChaveProvedorDeAssinatura() + UteisWebServiceUrl.URL_TECHCERT_FLOW);
            HttpResponse<JsonNode> jsonResponse = unirest().post(url.toString())
                    .header(UteisWebServiceUrl.Content_Type, UteisWebServiceUrl.Application_JSON)
                    .header(configGedVO.getApikeyTechCert(), montarToken(configGedVO))
                    .body(payload)
                    .asJson();
            JSONObject error = jsonResponse.getBody().getObject();
            String code = error.optString("code", "");
            if (jsonResponse.getStatus() == 422 && !"FlowActionNotFound".equalsIgnoreCase(code)) {
                tratarErroTechCert(jsonResponse.getBody().toString(), jsonResponse.getStatus());
            } else if (jsonResponse.getStatus() != 200 && !jsonResponse.isSuccess() && !"FlowActionNotFound".equalsIgnoreCase(code)) {
                tratarErroTechCert(jsonResponse.getBody().toString(), jsonResponse.getStatus());
            }
        } catch (Exception e) {
            throw e;
        }
    }
    
    
   
//    /**
//     * Gera um Token JWT de curta duração para autenticação no TechCert.
//     * Este token usa a Chave Secreta (ApiSecret) e o ID do Cliente (ApiClientId)
//     * das configurações.
//     *
//     * @param configuracaoGEDVO Objeto contendo as chaves da API.
//     * @return Uma string de Token JWT.
//     */
//    private String montarToken(ConfiguracaoGEDVO configuracaoGEDVO) {
//        
//        try {
//            // 1. Pegar as credenciais das configurações
//            //    VOCÊ PRECISA GARANTIR QUE ESSES MÉTODOS EXISTAM EM ConfiguracaoGEDVO
//            String chaveSecreta = configuracaoGEDVO.getApiSecretTechCert();
//            String idCliente = configuracaoGEDVO.getApiClientIdTechCert();
//
//            if (chaveSecreta == null || idCliente == null) {
//                throw new StreamSeiException("ApiClientId ou ApiSecret do TechCert não configurados.");
//            }
//
//            // 2. Definir o algoritmo de assinatura (HMAC com SHA-256)
//            Algorithm algorithm = Algorithm.HMAC256(chaveSecreta);
//
//            // 3. Definir o tempo de validade (ex: 5 minutos)
//            //    Usando a API moderna java.time
//            Instant agora = Instant.now();
//            Instant expiracao = agora.plus(5, ChronoUnit.MINUTES);
//
//            // 4. Construir o Token
//            String token = JWT.create()
//                    .withIssuer(idCliente) // Quem está emitindo o token (seu sistema)
//                    .withIssuedAt(Date.from(agora)) // Quando foi emitido
//                    .withExpiresAt(Date.from(expiracao)) // Quando expira
//                    .sign(algorithm); // Assina com a chave secreta
//
//            return token;
//            
//        } catch (Exception e) {
//            // Logar o erro real (e) pode ser importante aqui
//            throw new StreamSeiException("Não foi possível gerar o token de autenticação TechCert.", e);
//        }
//    }
    
    
    /**
     * Retorna a Chave de API estática para autenticação no TechCert.
     *
     * @param configuracaoGEDVO Objeto contendo a chave da API.
     * @return A string da Chave de API.
     */
    private String montarToken(ConfiguracaoGEDVO configuracaoGEDVO) {
        
        String chaveApi = configuracaoGEDVO.getApiSecretTechCert();

        if (chaveApi == null || chaveApi.isBlank()) {
            throw new StreamSeiException("Chave de API (ApiSecret) do TechCert não configurada.");
        }
        
        return chaveApi;
    }

    

    @Override
    public void realizarAlteracaoStepFlowActionTechCert(ConfiguracaoGEDVO configGedVO, String chaveProvedorDeAssinatura, Map<Integer, String> flowActionEdited) throws Exception {
        try {
            JSONObject payload = montarPayloadFlowActions(flowActionEdited);
            StringBuilder url = new StringBuilder();
            url.append(montarUrlPadrao(configGedVO) + UteisWebServiceUrl.URL_TECHCERT_DOCUMENTS
                    + "/" + chaveProvedorDeAssinatura + UteisWebServiceUrl.URL_TECHCERT_FLOW);
            HttpResponse<JsonNode> jsonResponse = unirest().post(url.toString())
                    .header(UteisWebServiceUrl.Content_Type, UteisWebServiceUrl.Application_JSON)
                    .header(configGedVO.getApikeyTechCert(), montarToken(configGedVO))
                    .body(payload)
                    .asJson();
            if (jsonResponse.getStatus() == 422) {
                tratarErroTechCert(jsonResponse.getBody().toString(), jsonResponse.getStatus());
            } else if (jsonResponse.getStatus() != 200 && !jsonResponse.isSuccess()) {
                tratarErroTechCert(jsonResponse.getBody().toString(), jsonResponse.getStatus());
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public JSONObject montarPayloadFlowActions(Map<Integer, String> flowActionEdited) {
        JSONArray editedFlowActions = new JSONArray();
        for (Map.Entry<Integer, String> entry : flowActionEdited.entrySet()) {
            JSONObject obj = new JSONObject();
            obj.put("flowActionId", entry.getValue());
            obj.put("step", entry.getKey());
            editedFlowActions.put(obj);
        }
        JSONObject payload = new JSONObject();
        payload.put("editedFlowActions", editedFlowActions);
        return payload;
    }

    @Override
    public CertiSignRSVO realizarEnvioParticipantAdd(CertiSignRSVO csRSVO, DocumentoAssinadoPessoaVO doc, ConfiguracaoGEDVO configGedVO) throws Exception {
        RequestBodyEntity jsonEntity = null;
        try {
            Gson gson = inicializaGson();
            jsonEntity = unirest().post(configGedVO.getUrlProvedorAssinatura(doc.getDocumentoAssinadoVO().getProvedorDeAssinaturaEnum()) + UteisWebServiceUrl.URL_SEI_WEB_SERVICE_CERTISIGN_PARTICIPANT_ADD)
                    .header(UteisWebServiceUrl.Content_Type, UteisWebServiceUrl.Application_JSON)
                    .header(configGedVO.getChaveToken(doc.getDocumentoAssinadoVO().getProvedorDeAssinaturaEnum()), configGedVO.getValorTokenProvedorAssinatura(doc.getDocumentoAssinadoVO().getProvedorDeAssinaturaEnum()))
                    .body(gson.toJson(csRSVO));
            HttpResponse<JsonNode> jsonResponse = jsonEntity.asJson();
            if (jsonResponse.isSuccess() || jsonResponse.getStatus() == 200) {
                return executarVerificacaoRetornoObjeto(jsonResponse, CertiSignRSVO.class, "");
            } else {
                InfoWSVO rep = new Gson().fromJson(jsonResponse.getBody().toString(), InfoWSVO.class);
                tratarMensagemErroWebService(rep, String.valueOf(jsonResponse.getStatus()), jsonResponse.getBody().toString());
                throw new StreamSeiException(jsonResponse.getBody().toString());
            }
        } catch (Exception e) {
            throw e;
        } finally {
            jsonEntity = null;
        }
    }

    @Override
    public void realizarAlteracaoParticipanteTechCert(DocumentoAssinadoPessoaVO assinanteDeleted, String chaveProvedorAssinatura, AddedFlowActionTechCertVO addedFlowActionTechCertVO, ConfiguracaoGEDVO configuracaoGEDVO) throws Exception {
        try {
            if (!configuracaoGEDVO.getHabilitarIntegracaoTechCert()){
                throw new StreamSeiException("Configuração de integração com TechCert não está habilitada.");
            }

            Map<String, Object> requestBody = new LinkedHashMap<>();
            List<String> deletedIds = Collections.singletonList(assinanteDeleted.getCodigoAssinatura());
            requestBody.put("deletedFlowActionIds", deletedIds);
            requestBody.put("addedFlowActions", Collections.singletonList(addedFlowActionTechCertVO));

            Gson gson = inicializaGson();
            String jsonPayload = gson.toJson(requestBody);
            StringBuilder url = new StringBuilder();
            url.append(montarUrlPadrao(configuracaoGEDVO) + UteisWebServiceUrl.URL_TECHCERT_DOCUMENTS
                    + "/" + chaveProvedorAssinatura + UteisWebServiceUrl.URL_TECHCERT_FLOW);
            HttpResponse<JsonNode> jsonResponse = unirest().post(url.toString())
                    .header(UteisWebServiceUrl.Content_Type, UteisWebServiceUrl.Application_JSON)
                    .header(configuracaoGEDVO.getApikeyTechCert(), montarToken(configuracaoGEDVO))
                    .body(jsonPayload)
                    .asJson();
            if (!jsonResponse.isSuccess() || jsonResponse.getStatus() != 200) {
                tratarErroTechCert(jsonResponse.getBody().toString(), jsonResponse.getStatus());
                throw new StreamSeiException(jsonResponse.getBody().toString());
            }
        } catch (Exception e) {
            throw e;
        }
    }


    private UploadRSVO realizarConsultarPackageDocument(DocumentoAssinadoVO doc, ConfiguracaoGEDVO configGedVO, boolean includeOriginal, boolean includeManifest, boolean zipado) throws Exception {
        try {
            HttpResponse<JsonNode> jsonResponse = unirest().get(configGedVO.getUrlProvedorAssinatura(doc.getProvedorDeAssinaturaEnum()) + UteisWebServiceUrl.URL_SEI_WEB_SERVICE_CERTISIGN_PACKAGE)
                    .header(UteisWebServiceUrl.Content_Type, UteisWebServiceUrl.Application_JSON)
                    .header(UteisWebServiceUrl.token, configGedVO.getTokenProvedorDeAssinatura())
                    .queryString("key", doc.getChaveProvedorDeAssinatura())
                    .queryString("includeOriginal", includeOriginal)
                    .queryString("includeManifest", includeManifest)
                    .queryString("zipped", zipado)
                    .asJson();
            if ((jsonResponse.isSuccess() || jsonResponse.getStatus() == 200) && zipado) {
                return executarVerificacaoRetornoObjeto(jsonResponse, UploadRSVO.class, "");
            } else if ((jsonResponse.isSuccess() || jsonResponse.getStatus() == 200) && !zipado) {
                JSONArray jsonArray = jsonResponse.getBody().getArray();
                while (jsonArray.iterator().hasNext()) {
                    JSONObject issue = (JSONObject) jsonArray.iterator().next();
                    return new Gson().fromJson(issue.toString(), UploadRSVO.class);
                }
                return null;
            } else {
                InfoWSVO rep = new Gson().fromJson(jsonResponse.getBody().toString(), InfoWSVO.class);
                tratarMensagemErroWebService(rep, String.valueOf(jsonResponse.getStatus()), jsonResponse.getBody().toString());
                return null;
            }
        } catch (Exception e) {
            throw e;
        }
    }


    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = {Throwable.class}, propagation = Propagation.SUPPORTS)
    public void realizarProcessamentoJobValidacaoDocumentoAssinadoEnviadosPorProvedorCertiSign(Date periodoInicial, Date periodoFinal) throws ConsistirException {
        ConsistirException consistirException = new ConsistirException();
        List<DocumentoAssinadoVO> documentos = null;
        UsuarioVO usuarioOperacaoExterna;
        try {
            usuarioOperacaoExterna = getFacadeFactory().getUsuarioFacade().obterUsuarioResponsavelOperacoesExternas();
            documentos = getFacadeFactory().getDocumentoAssinadoFacade().consultarDocumentosAssinadoPendentePorPeriodo(Uteis.NIVELMONTARDADOS_TODOS, usuarioOperacaoExterna, periodoInicial, periodoFinal);
            getFacadeFactory().getDocumentoAssinadoFacade().executarProcessamentoDocumentosAssinadoEletronicaMenteValidandoSituacaoAssinaturaPorProvedorCertiSign(documentos, usuarioOperacaoExterna);
        } catch (Exception e1) {
            consistirException.adicionarListaMensagemErro(e1.getMessage());
            throw consistirException;
        }


    }


    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = {Throwable.class}, propagation = Propagation.SUPPORTS)
    public void realizarProcessamentoJobValidacaoDocumentoAssinadoEnviadosPorProvedorTechCert(Date periodoInicial, Date periodoFinal) throws ConsistirException {
        ConsistirException consistirException = new ConsistirException();
        List<DocumentoAssinadoVO> documentos = null;
        UsuarioVO usuarioOperacaoExterna;
        try {
            usuarioOperacaoExterna = getFacadeFactory().getUsuarioFacade().obterUsuarioResponsavelOperacoesExternas();
            documentos = getFacadeFactory().getDocumentoAssinadoFacade().consultarDocumentosAssinadoPendentePorPeriodoTechCert(Uteis.NIVELMONTARDADOS_TODOS, usuarioOperacaoExterna, periodoInicial, periodoFinal);
            getFacadeFactory().getDocumentoAssinadoFacade().executarProcessamentoDocumentosAssinadoEletronicaMenteValidandoSituacaoAssinaturaPorProvedorTechCert(documentos, usuarioOperacaoExterna);
        } catch (Exception e1) {
            consistirException.adicionarListaMensagemErro(e1.getMessage());
            throw consistirException;
        }
    }

    private static String PESSOA_RESPONSAVEL_ASSINATURA = "pessoaResponsavelAssinatura";
    private static String CPF_PESSOA_DIFERENTE_ASSINATURA = "cpfPessoaDiferenteAssinatura";

    /**
     * Map que retornará se a pessoa do documentoAssinadoPessoa que está sendo
     * percorrido no momento foi o responsável pela assinatura que está sendo
     * percorrida no momento
     *
     * @param nomePessoaValidar
     * @param cpfPessoaValidar
     * @param nomePessoaAssinatura
     * @param cpfPessoaAssinatura
     * @return
     * @author Felipi Alves
     */
    private Map<String, Boolean> validarDocumentoAssinadoPessoaResponsavelPelaAssinatura(String nomePessoaValidar, String cpfPessoaValidar, String nomePessoaAssinatura, String cpfPessoaAssinatura) {
        Map<String, Boolean> map = new HashMap<>(0);
        map.put(PESSOA_RESPONSAVEL_ASSINATURA, Boolean.FALSE);
        map.put(CPF_PESSOA_DIFERENTE_ASSINATURA, Boolean.FALSE);
        if (Uteis.isAtributoPreenchido(cpfPessoaValidar) && Uteis.isAtributoPreenchido(cpfPessoaAssinatura) && Objects.equals(cpfPessoaValidar, cpfPessoaAssinatura)
                && Uteis.isAtributoPreenchido(nomePessoaValidar) && Uteis.isAtributoPreenchido(nomePessoaAssinatura) && Objects.equals(nomePessoaValidar, nomePessoaAssinatura)) {
            map.replace(PESSOA_RESPONSAVEL_ASSINATURA, Boolean.TRUE);
        } else if (Uteis.isAtributoPreenchido(cpfPessoaValidar) && Uteis.isAtributoPreenchido(cpfPessoaAssinatura) && Objects.equals(cpfPessoaValidar, cpfPessoaAssinatura)) {
            map.replace(PESSOA_RESPONSAVEL_ASSINATURA, Boolean.TRUE);
        } else if (Uteis.isAtributoPreenchido(nomePessoaValidar) && Uteis.isAtributoPreenchido(nomePessoaAssinatura) && Objects.equals(nomePessoaValidar, nomePessoaAssinatura)) {
            map.replace(PESSOA_RESPONSAVEL_ASSINATURA, Boolean.TRUE);
            if (Uteis.isAtributoPreenchido(cpfPessoaValidar) && Uteis.isAtributoPreenchido(cpfPessoaAssinatura) && !Objects.equals(cpfPessoaValidar, cpfPessoaAssinatura)) {
                map.replace(CPF_PESSOA_DIFERENTE_ASSINATURA, Boolean.TRUE);
            }
        }
        return map;
    }


    @Override
    public void executarProcessamentoDocumentosAssinadoEletronicaMenteValidandoSituacaoAssinaturaPorProvedorCertiSign(List<DocumentoAssinadoVO> docs, UsuarioVO usuarioLogado) throws ConsistirException {
        ConsistirException consistirException = new ConsistirException();
        if (Uteis.isAtributoPreenchido(docs)) {
            for (DocumentoAssinadoVO doc : docs) {
                try {
                    executarProcessamentoDocumentosAssinadoEletronicaMenteValidandoSituacaoAssinaturaPorProvedorCertiSign(doc, usuarioLogado);
                } catch (Exception e) {
                    if (e.getMessage() != null) {
                        if (Uteis.isJsonValidoConverteParaClasse(e.getMessage(), InfoWSVO.class)) {
                            InfoWSVO rep = new Gson().fromJson(e.getMessage(), InfoWSVO.class);
                            if (rep != null) {
                                String erro = "Documento Assinado: " + doc.getCodigo() + " -  Erro: " + rep.getCode() + "  -  " + rep.getMessage();
                                if (!consistirException.existeErroListaMensagemErro()) {
                                    consistirException.adicionarListaMensagemErro("Falha na Verificação  ");
                                }
                                consistirException.adicionarListaMensagemErro(erro);
                            }
                        } else {
                            String erro = "Documento Assinado: " + doc.getCodigo() + " -  Erro: " + e.getMessage();
                            if (!consistirException.existeErroListaMensagemErro()) {
                                consistirException.adicionarListaMensagemErro("Falha na Verificação  ");
                            }
                            consistirException.adicionarListaMensagemErro(erro);
                        }
                    }
                }
            }
        }
        if (consistirException.existeErroListaMensagemErro()) {
            throw consistirException;
        }
    }

    @Override
    public void executarProcessamentoDocumentosAssinadoEletronicaMenteValidandoSituacaoAssinaturaPorProvedorTechCert(List<DocumentoAssinadoVO> docs, UsuarioVO usuarioLogado) throws ConsistirException {
        ConsistirException consistirException = new ConsistirException();
        double permitsPerSecond = 1_000.0 / Uteis.delayMillisTechCert;
        RateLimiter limiter = RateLimiter.create(permitsPerSecond);
        if (Uteis.isAtributoPreenchido(docs)) {
            for (DocumentoAssinadoVO doc : docs) {
                try {
                    limiter.acquire();
                    executarProcessamentoDocumentosAssinadoEletronicaMenteValidandoSituacaoAssinaturaPorProvedorTechCert(doc, usuarioLogado);
                } catch (Exception e) {
                    if (e.getMessage() != null) {
                        if (Uteis.isJsonValidoConverteParaClasse(e.getMessage(), InfoWSVO.class)) {
                            InfoWSVO rep = new Gson().fromJson(e.getMessage(), InfoWSVO.class);
                            if (rep != null) {
                                String erro = "Documento Assinado: " + doc.getCodigo() + " -  Erro: " + rep.getCode() + "  -  " + rep.getMessage();
                                if (!consistirException.existeErroListaMensagemErro()) {
                                    consistirException.adicionarListaMensagemErro("Falha na Verificação  ");
                                }
                                consistirException.adicionarListaMensagemErro(erro);
                            }
                        } else {
                            String erro = "Documento Assinado: " + doc.getCodigo() + " -  Erro: " + e.getMessage();
                            if (!consistirException.existeErroListaMensagemErro()) {
                                consistirException.adicionarListaMensagemErro("Falha na Verificação  ");
                            }
                            consistirException.adicionarListaMensagemErro(erro);
                        }
                    }
                }
            }
        }
        if (consistirException.existeErroListaMensagemErro()) {
            throw consistirException;
        }
    }

	@Override
	public void executarProcessamentoDocumentosAssinadoEletronicaMenteValidandoSituacaoAssinaturaPorProvedorCertiSign(DocumentoAssinadoVO doc, UsuarioVO usuarioLogado) throws Exception {
		if (!Objects.nonNull(doc)) {
			return;
		}

		ConfiguracaoGEDVO configGedVO = getFacadeFactory().getConfiguracaoGEDFacade().consultarPorUnidadeEnsino(doc.getUnidadeEnsinoVO().getCodigo(), false, usuarioLogado);
		ConfiguracaoGedOrigemVO configuracaoGedOrigemVO = configGedVO.getConfiguracaoGedOrigemVO(doc.getTipoOrigemDocumentoAssinadoEnum());
		ProvedorDeAssinaturaEnum provedorDeAssinaturaEnum = doc.getProvedorAssinaturaVisaoAdm() != null ? doc.getProvedorAssinaturaVisaoAdm() : configuracaoGedOrigemVO.getProvedorAssinaturaPadraoEnum();
		if (!provedorDeAssinaturaEnum.isProvedorCertisign() && !provedorDeAssinaturaEnum.isProvedorImprensaOficial()) {
			return;
		}

		DocumentoAssinadoOrigemEnum documentoAssinadoOrigemEnum = provedorDeAssinaturaEnum.isProvedorCertisign() ? DocumentoAssinadoOrigemEnum.CERTISIGN_SEI : DocumentoAssinadoOrigemEnum.IMPRENSAOFICIAL_SEI;


		if (executarProcessamentoCertisignAntigo(doc, usuarioLogado, configGedVO, documentoAssinadoOrigemEnum)) {
			return;
		}

		if (executarProcessamentoCertisignNovo(doc, usuarioLogado, configGedVO, documentoAssinadoOrigemEnum)) {
			return;
		}

		// fim for
		if (Uteis.isAtributoPreenchido(doc) && doc.getTipoOrigemDocumentoAssinadoEnum().isContrato()) {
			getFacadeFactory().getMatriculaPeriodoFacade().realizarAtivacaoMatriculaValidandoRegrasEntregaDocumentoAssinaturaContratoMatricula(doc.getMatricula().getMatricula(),  Boolean.FALSE, usuarioLogado);
		}
	}

    @Override
    public void executarProcessamentoDocumentosAssinadoEletronicaMenteValidandoSituacaoAssinaturaPorProvedorTechCert(DocumentoAssinadoVO doc, UsuarioVO usuarioLogado) throws Exception {
        if (!Objects.nonNull(doc)) {
            return;
        }

        ConfiguracaoGEDVO configGedVO = getFacadeFactory().getConfiguracaoGEDFacade().consultarPorUnidadeEnsino(doc.getUnidadeEnsinoVO().getCodigo(), false, usuarioLogado);
        ConfiguracaoGedOrigemVO configuracaoGedOrigemVO = configGedVO.getConfiguracaoGedOrigemVO(doc.getTipoOrigemDocumentoAssinadoEnum());
        ProvedorDeAssinaturaEnum provedorDeAssinaturaEnum = doc.getProvedorAssinaturaVisaoAdm() != null ? doc.getProvedorAssinaturaVisaoAdm() : configuracaoGedOrigemVO.getProvedorAssinaturaPadraoEnum();

        if (!provedorDeAssinaturaEnum.isProvedorTechCert()) {
            return;
        }

        DocumentoAssinadoOrigemEnum documentoAssinadoOrigemEnum = DocumentoAssinadoOrigemEnum.TECHCERT_SEI;

        if (executarProcessamentoTechCertAntigo(doc, usuarioLogado, configGedVO, documentoAssinadoOrigemEnum)) {
            return;
        }

        if (executarProcessamentoTechCertNovo(doc, usuarioLogado, configGedVO, documentoAssinadoOrigemEnum)) {
            return;
        }

        if (Uteis.isAtributoPreenchido(doc) && doc.getTipoOrigemDocumentoAssinadoEnum().isContrato()) {
            getFacadeFactory().getMatriculaPeriodoFacade().realizarAtivacaoMatriculaValidandoRegrasEntregaDocumentoAssinaturaContratoMatricula(doc.getMatricula().getMatricula(), Boolean.TRUE, usuarioLogado);
        }
    }

    @Override
    public void executarProcessamentoDocumentosAssinadoEletronicaMenteValidandoSituacaoAssinaturaPorProvedorTechCertApi(DocumentoAssinadoVO doc, UsuarioVO usuarioLogado) throws Exception {
        ConfiguracaoGEDVO configGedVO = getFacadeFactory().getConfiguracaoGEDFacade().consultarPorUnidadeEnsino(doc.getUnidadeEnsinoVO().getCodigo(), false, usuarioLogado);
        ConfiguracaoGedOrigemVO configuracaoGedOrigemVO = configGedVO.getConfiguracaoGedOrigemVO(doc.getTipoOrigemDocumentoAssinadoEnum());
        ProvedorDeAssinaturaEnum provedorDeAssinaturaEnum = doc.getProvedorAssinaturaVisaoAdm() != null ? doc.getProvedorAssinaturaVisaoAdm() : configuracaoGedOrigemVO.getProvedorAssinaturaPadraoEnum();
        if (provedorDeAssinaturaEnum.isProvedorTechCert()) {
            DocumentoAssinadoOrigemEnum documentoAssinadoOrigemEnum = DocumentoAssinadoOrigemEnum.TECHCERT_WS;
            executarProcessamentoTechCertAntigo(doc, usuarioLogado, configGedVO, documentoAssinadoOrigemEnum);
            executarProcessamentoTechCertNovo(doc, usuarioLogado, configGedVO, documentoAssinadoOrigemEnum);
            if (Uteis.isAtributoPreenchido(doc) && doc.getTipoOrigemDocumentoAssinadoEnum().isContrato()) {
                getFacadeFactory().getMatriculaPeriodoFacade().realizarAtivacaoMatriculaValidandoRegrasEntregaDocumentoAssinaturaContratoMatricula(doc.getMatricula().getMatricula(),  Boolean.TRUE, usuarioLogado);
            }
        }
    }

    private boolean executarProcessamentoCertisignNovo(DocumentoAssinadoVO doc, UsuarioVO usuarioLogado, ConfiguracaoGEDVO configGedVO, DocumentoAssinadoOrigemEnum documentoAssinadoOrigemEnum) throws Exception {
        try {
            DocumentFlowActionsResponseRSVO objCertSign = realizarConsultaDocumentFlowActions(doc, configGedVO);

			if (objCertSign == null || !Uteis.isAtributoPreenchido(objCertSign.getSteps())) {
				return true;
			}

			List<DocumentoAssinadoPessoaVO> assinaturasPendentes = doc.getListaDocumentoAssinadoPessoa().stream().filter(dap -> dap.getSituacaoDocumentoAssinadoPessoaEnum().isPendente()).collect(Collectors.toList());

			for (StepRSVO step : objCertSign.getSteps()) {
				for (AtendeeRSVO participante : step.getParticipantes()) {

					DocumentoAssinadoPessoaVO assinante = assinaturasPendentes.stream().filter(assinatura -> participante.getUsuario().getCpf().equals(assinatura.getCpfPessoa())).findFirst().orElse(null);

					if (participante.getUsuario().getExcluido() && Uteis.isAtributoPreenchido(assinante)) {
						getFacadeFactory().getDocumentoAssinadoPessoaFacade().excluir(assinante, usuarioLogado);
						continue;
					}
					if (participante.getStatusAssinaturaCertisign().isNaoAssinado() && !Uteis.isAtributoPreenchido(participante.getLink())) {
						getFacadeFactory().getDocumentoAssinadoPessoaFacade().atualizarDadosAssinaturaPorProvedorCertisign(doc.getCodigoProvedorDeAssinatura(), participante.getUsuario().getNome(), participante.getUsuario().getCpf(), SituacaoDocumentoAssinadoPessoaEnum.REJEITADO, "O link de assinatura foi anulado", participante.getDataAssinatura(), documentoAssinadoOrigemEnum, "");
						continue;
					}
					if (participante.getStatusAssinaturaCertisign().equals(StatusAssinaturaCertisign.INDEFINIDO) && !Uteis.isAtributoPreenchido(participante.getLink())) {
						if (Uteis.isAtributoPreenchido(assinante) && assinante.getSituacaoDocumentoAssinadoPessoaEnum().isPendente()) {
							String jsonAssinatura = new GsonBuilder().create().toJson(participante);
							getFacadeFactory().getDocumentoAssinadoPessoaFacade().atualizarDadosAssinaturaPorProvedorCertisign(doc.getCodigoProvedorDeAssinatura(), participante.getUsuario().getNome(), participante.getUsuario().getCpf(), SituacaoDocumentoAssinadoPessoaEnum.ASSINADO, "", participante.getDataAssinatura(), documentoAssinadoOrigemEnum, jsonAssinatura);
						}
					}
				}

				registrarNovosAssinantes(doc, step.getParticipantes(), usuarioLogado);
			}
		} catch (Exception e) {
			throw e;
		}

        return false;
    }

    private boolean executarProcessamentoTechCertNovo(DocumentoAssinadoVO doc, UsuarioVO usuarioLogado, ConfiguracaoGEDVO configGedVO, DocumentoAssinadoOrigemEnum documentoAssinadoOrigemEnum) throws Exception {
        try {
            DocumentsTechCertVO documentsTechCertVO = consultarDocumet(configGedVO, doc.getChaveProvedorDeAssinatura());
            DocumentKeyValidationTechCertVO documentKeyValidationTechCertVO = getFacadeFactory().getDocumentoAssinadoFacade().realizarConsultarDocumentsValidateSignaturesTechcert(doc, configGedVO, documentsTechCertVO);

            if (documentKeyValidationTechCertVO.getDocumentInvalid()){
                return true;
            }

            // Conjuntos de CPFs atuais
            List<String> flowCpfs = documentsTechCertVO.getFlowActions().stream()
                    .flatMap(flow -> {
                        List<String> cpfs = new ArrayList<>();
                        //========Signer========
                        if (flow.getUser() != null) {
                            String cpf = Uteis.retirarMascaraCPF(flow.getUser().getIdentifier());
                            if (Uteis.isAtributoPreenchido(cpf)) cpfs.add(cpf);
                        }
                        //========SignRule========
                        if (flow.getSignRuleUsers() != null && !flow.getSignRuleUsers().isEmpty()) {
                            flow.getSignRuleUsers().forEach(signRuleUser -> {
                                String cpf = Uteis.retirarMascaraCPF(signRuleUser.getIdentifier());
                                if (Uteis.isAtributoPreenchido(cpf)) cpfs.add(cpf);
                            });
                        }
                        return cpfs.stream();
                    })
                    .collect(Collectors.toList());

            List<String> docCpfs = doc.getListaDocumentoAssinadoPessoa().stream()
                    .map(pessoa ->
                            Uteis.isAtributoPreenchido(Uteis.retirarMascaraCPF(pessoa.getPessoaVO().getCPF()))
                                    ? Uteis.retirarMascaraCPF(pessoa.getPessoaVO().getCPF()) : Uteis.retirarMascaraCPF(pessoa.getCpfPessoa())
                    )
                    .collect(Collectors.toList());

            // Verifica se há mudanças necessárias
            if (flowCpfs.equals(docCpfs)) {
                return false;
            }

            // Calcula apenas as diferenças reais
            List<String> cpfsToAdd = new ArrayList<>(flowCpfs);
            cpfsToAdd.removeAll(docCpfs);             // Apenas novos CPFs do flow

            List<String> cpfsToRemove = new ArrayList<>(docCpfs);
            cpfsToRemove.removeAll(flowCpfs);          // Apenas CPFs que não estão mais no flow

            // Processa apenas as remoções necessárias
            if (!cpfsToRemove.isEmpty()) {
                List<DocumentoAssinadoPessoaVO> paraRemover = doc.getListaDocumentoAssinadoPessoa().stream()
                        .filter(pessoa -> {
                            String cpfBase = Uteis.isAtributoPreenchido(Uteis.retirarMascaraCPF(pessoa.getPessoaVO().getCPF()))
                                    ? Uteis.retirarMascaraCPF(pessoa.getPessoaVO().getCPF())
                                    : Uteis.retirarMascaraCPF(pessoa.getCpfPessoa());
                            return cpfsToRemove.contains(cpfBase);
                        })
                        .collect(Collectors.toList());

                if (Uteis.isAtributoPreenchido(paraRemover)) {
                    for (DocumentoAssinadoPessoaVO documentoAssinadoPessoaVO : paraRemover) {
                        getFacadeFactory().getDocumentoAssinadoPessoaFacade().excluir(documentoAssinadoPessoaVO, usuarioLogado);
                    }
                }
            }

            // Processa apenas as adições necessárias
            if (!cpfsToAdd.isEmpty()) {
                List<FlowActionsTechCertVO> paraAdicionar = documentsTechCertVO.getFlowActions().stream()
                        .filter(flow -> cpfsToAdd.contains(
                                Uteis.retirarMascaraCPF(flow.getUser().getIdentifier())))
                        .collect(Collectors.toList());

                if (Uteis.isAtributoPreenchido(paraAdicionar)) {
                    registrarNovosAssinantesTechCert(documentsTechCertVO, configGedVO, doc, paraAdicionar, documentKeyValidationTechCertVO, usuarioLogado);
                }
            }
        } catch (Exception e) {
            throw e;
        }
        return false;
    }

    private void registrarNovosAssinantes(DocumentoAssinadoVO doc, List<AtendeeRSVO> participantes, UsuarioVO usuario) throws Exception {

		List<AtendeeRSVO> novosAssinantes = participantes
				.stream()
				.filter(participante -> !doc.getListaDocumentoAssinadoPessoa()
						.stream()
						.map(documento -> Uteis.isAtributoPreenchido(documento.getCpfPessoa()) ? documento.getCpfPessoa() : documento.getPessoaVO().getCPF())
						.collect(Collectors.toList())
						.contains(participante.getUsuario().getCpf()))
				.collect(Collectors.toList());

		List<DocumentoAssinadoPessoaVO> documentosAssinadosPessoasVO = new ArrayList<>();

		for (AtendeeRSVO novoAssinante : novosAssinantes) {
			List<PessoaVO> pessoas = getFacadeFactory().getPessoaFacade().consultarPorCPF(novoAssinante.getUsuario().getCpf(), false, NivelMontarDados.BASICO.getValor(), usuario);
			DocumentoAssinadoPessoaVO documentoAssinado = montarNovoDocumentoAssinadoPessoaVO(novoAssinante, doc);
			if (Uteis.isAtributoPreenchido(pessoas)) {
				PessoaVO pessoa = pessoas.get(0);
				documentoAssinado.setPessoaVO(pessoa);
				documentoAssinado.setTipoPessoa(TipoPessoa.getEnum(pessoa.getTipoPessoa()));
			}
			documentosAssinadosPessoasVO.add(documentoAssinado);
		}

        getFacadeFactory().getDocumentoAssinadoPessoaFacade().persistir(documentosAssinadosPessoasVO, false, usuario);
    }

    private void registrarNovosAssinantesTechCert(DocumentsTechCertVO documentsTechCertVO, ConfiguracaoGEDVO configuracaoGEDVO, DocumentoAssinadoVO doc, List<FlowActionsTechCertVO> assinantesNovos, DocumentKeyValidationTechCertVO documentKeyValidationTechCertVO, UsuarioVO usuario) throws Exception {
        List<DocumentoAssinadoPessoaVO> documentosAssinadosPessoasVO = new ArrayList<>();
        for (FlowActionsTechCertVO assinante : assinantesNovos) {
            List<PessoaVO> pessoas = getFacadeFactory().getPessoaFacade().consultarPorCPF(assinante.getUser().getIdentifier(), false, NivelMontarDados.BASICO.getValor(), usuario);
            DocumentoAssinadoPessoaVO documentoAssinado = montarNovoDocumentoAssinadoPessoaVOTechCert(documentsTechCertVO, configuracaoGEDVO, assinante, doc, documentKeyValidationTechCertVO);
            if (Uteis.isAtributoPreenchido(pessoas)) {
                PessoaVO pessoa = pessoas.get(0);
                documentoAssinado.setPessoaVO(pessoa);
                documentoAssinado.setTipoPessoa(TipoPessoa.getEnum(pessoa.getTipoPessoa()));
            }
            documentosAssinadosPessoasVO.add(documentoAssinado);
        }
        getFacadeFactory().getDocumentoAssinadoPessoaFacade().persistir(documentosAssinadosPessoasVO, false, usuario);
    }

    private DocumentFlowTechCertVO[] atualizarAssinantesNaTechCert(DocumentoAssinadoVO documentoAssinadoVO, ConfiguracaoGEDVO configuracaoGEDVO,
                                                         List<FlowActionsTechCertVO> novosAssinantes, List<FlowActionsTechCertVO> assinantesRemovidos) {
        try {
            if (!configuracaoGEDVO.getHabilitarIntegracaoTechCert()){
                throw new StreamSeiException("Configuração de integração com TechCert não está habilitada.");
            }
            StringBuilder url = new StringBuilder();
            url.append(montarUrlPadrao(configuracaoGEDVO)).append(UteisWebServiceUrl.URL_TECHCERT_DOCUMENTS
                    + documentoAssinadoVO.getChaveProvedorDeAssinatura()
                    + UteisWebServiceUrl.URL_TECHCERT_FLOW);
            HttpResponse<JsonNode> jsonResponse = unirest().post(url.toString())
                    .header(UteisWebServiceUrl.Content_Type, UteisWebServiceUrl.Application_JSON)
                    .header(configuracaoGEDVO.getApikeyTechCert(), montarToken(configuracaoGEDVO))
                    .body(montarParticipantesDocumentsFlows(novosAssinantes, assinantesRemovidos))
                    .connectTimeout(10000)
                    .asJson();
            if (jsonResponse.isSuccess() || jsonResponse.getStatus() == 200) {
                Gson gson = new GsonBuilder().create();
                return gson.fromJson(jsonResponse.getBody().toString(), DocumentFlowTechCertVO[].class);
            } else {
                throw new StreamSeiException(jsonResponse.getBody().toString());
            }
        } catch (Exception e) {
            throw e;
        }
    }

    private String montarParticipantesDocumentsFlows(List<FlowActionsTechCertVO> novosAssinantes, List<FlowActionsTechCertVO> assinantesRemovidos) {
        JSONObject root = new JSONObject();
        JSONArray addedArray = new JSONArray();
        if (Uteis.isAtributoPreenchido(novosAssinantes)) {
            for (FlowActionsTechCertVO novo : novosAssinantes) {
                JSONObject obj = new JSONObject();
                JSONObject userJson = new JSONObject();
                userJson.put("id", novo.getUser().getId());
                userJson.put("name", novo.getUser().getName());
                userJson.put("cpf", Uteis.retirarMascaraCPF(novo.getUser().getIdentifier()));
                userJson.put("email", novo.getUser().getEmail());
                obj.put("user", userJson);
                addedArray.put(obj);
            }
            root.put("addedFlowActions", addedArray);
        }
        JSONArray deletedArray = new JSONArray();
        if (Uteis.isAtributoPreenchido(assinantesRemovidos)) {
            for (FlowActionsTechCertVO removed : assinantesRemovidos) {
                deletedArray.put(removed.getId());
            }
            root.put("deletedFlowActionIds", deletedArray);
        }
        return root.toString();
    }

    private DocumentoAssinadoPessoaVO montarNovoDocumentoAssinadoPessoaVO(AtendeeRSVO novoAssinante, DocumentoAssinadoVO documentoAssinadoVO) throws ParseException, ParseException {
        DocumentoAssinadoPessoaVO documentoAssinadoPessoaVO = new DocumentoAssinadoPessoaVO();
        documentoAssinadoPessoaVO.setNomePessoa(novoAssinante.getUsuario().getNome());
        documentoAssinadoPessoaVO.setCpfPessoa(novoAssinante.getUsuario().getCpf());
        documentoAssinadoPessoaVO.setEmailPessoa(novoAssinante.getUsuario().getEmail());
        documentoAssinadoPessoaVO.setDocumentoAssinadoVO(documentoAssinadoVO);
        documentoAssinadoPessoaVO.setDataAssinatura(novoAssinante.getDataAssinatura());
        documentoAssinadoPessoaVO.setUrlAssinatura(novoAssinante.getLink());
        documentoAssinadoPessoaVO.setTipoAssinaturaDocumentoEnum(TipoAssinaturaDocumentoEnum.ELETRONICA);
        if (novoAssinante.getStatusAssinaturaCertisign().isNaoAssinado() && !Uteis.isAtributoPreenchido(novoAssinante.getLink())) {
            documentoAssinadoPessoaVO.setSituacaoDocumentoAssinadoPessoaEnum(SituacaoDocumentoAssinadoPessoaEnum.REJEITADO);
            documentoAssinadoPessoaVO.setMotivoRejeicao("O link de assinatura foi anulado");
            documentoAssinadoPessoaVO.setDataRejeicao(novoAssinante.getDataAssinatura());
        } else if (novoAssinante.getStatusAssinaturaCertisign().isAssinado()) {
            documentoAssinadoPessoaVO.setSituacaoDocumentoAssinadoPessoaEnum(SituacaoDocumentoAssinadoPessoaEnum.ASSINADO);
            documentoAssinadoPessoaVO.setDataAssinatura(novoAssinante.getDataAssinatura());
        }

        return documentoAssinadoPessoaVO;
    }

    private DocumentActionUrlTechCertVO regraGeracaoOrdemUrlAssinatura(String name, String identifier, DocumentsTechCertVO documentsTechCertVO, ConfiguracaoGEDVO configuracaoGEDVO, FlowActionsTechCertVO flowActionsTechCertVO) {
        // 1) Ordena todos os flows por step
        List<FlowActionsTechCertVO> sortedFlows = documentsTechCertVO.getFlowActions().stream()
                .sorted(Comparator.comparingInt(FlowActionsTechCertVO::getStep))
                .collect(Collectors.toList());

        // 2) Se já foi concluído ou recusado, não gera URL
        String status = flowActionsTechCertVO.getStatus();
        if (StatusFlowActionsTechCertEnum.COMPLETED.getNome().equalsIgnoreCase(status)
                || StatusFlowActionsTechCertEnum.REFUSED .getNome().equalsIgnoreCase(status)) {
            return null;
        }

        // 3) Só gera se todos os passos anteriores estiverem completos
        int currentStep = flowActionsTechCertVO.getStep();
        for (FlowActionsTechCertVO f : sortedFlows) {
            if (f.getStep() < currentStep
                    && !StatusFlowActionsTechCertEnum.COMPLETED.getNome().equalsIgnoreCase(f.getStatus())) {
                return null;
            }
        }

        // 4) Regra especial para SignRule: só o próximo usuário que não assinou ou recusou,
        //    e somente se for o usuário atual (name+identifier)
        if ("SignRule".equalsIgnoreCase(flowActionsTechCertVO.getType())
                && flowActionsTechCertVO.getSignRuleUsers() != null) {

            // encontra o primeiro signRuleUser sem assinatura nem recusa
            SignRuleUserTechCertVO nextUser = flowActionsTechCertVO.getSignRuleUsers().stream()
                    .filter(u -> u.getSignatureDate() == null && !u.getHasRefused())
                    .findFirst()
                    .orElse(null);

            if (nextUser == null) {
                // todos já foram processados
                return null;
            }

            // verifica se é o usuário atual
            String usuarioVerificar = Uteis.isAtributoPreenchido(nextUser.getName()) ? Uteis.removerAcentos(Uteis.removerAcentos(nextUser.getName())).replace(" ", "") : Constantes.EMPTY;
        	String identificadorVerificar = Uteis.isAtributoPreenchido(nextUser.getIdentifier()) ? Uteis.removeCaractersEspeciais(Uteis.removerAcentos(Uteis.removeCaractersEspeciais(nextUser.getIdentifier()))).replace(" ", "") : Constantes.EMPTY;
            if (!identificadorVerificar.equals(identifier)
                    || !usuarioVerificar.equalsIgnoreCase(name)) {
                return null;
            }

            // monta JSON apenas para esse usuário e gera URL
            JSONObject payload = montarJsonParaGerarUrlsAssinaturaPorFlowActionSignRule(
                    nextUser.getIdentifier(),
                    nextUser.getEmail(),
                    flowActionsTechCertVO.getId()
            );
            return gerarUrlsAssinatura(
                    configuracaoGEDVO,
                    documentsTechCertVO.getId(),
                    payload
            );
        }

        // 5) Se for Signer, verifica se é o próprio usuário atual
        if ("Signer".equalsIgnoreCase(flowActionsTechCertVO.getType())
                && flowActionsTechCertVO.getUser() != null) {
        	String usuarioVerificar = Uteis.isAtributoPreenchido(flowActionsTechCertVO.getUser().getName()) ? Uteis.removerAcentos(Uteis.removerAcentos(flowActionsTechCertVO.getUser().getName())).replace(" ", "") : Constantes.EMPTY;
        	String identificadorVerificar = Uteis.isAtributoPreenchido(flowActionsTechCertVO.getUser().getIdentifier()) ? Uteis.removeCaractersEspeciais(Uteis.removerAcentos(Uteis.removeCaractersEspeciais(flowActionsTechCertVO.getUser().getIdentifier()))).replace(" ", "") : Constantes.EMPTY;
            if (!identificadorVerificar.equals(identifier)
                    || !usuarioVerificar.equalsIgnoreCase(name)) {
                return null;
            }
        }

        // 6) Priorização entre flows de mesmo step (Signer e SignRule)
        List<FlowActionsTechCertVO> sameStepPending = sortedFlows.stream()
                .filter(f -> f.getStep() == currentStep)
                .filter(f ->
                        StatusFlowActionsTechCertEnum.PENDING.getNome().equalsIgnoreCase(f.getStatus()) ||
                                StatusFlowActionsTechCertEnum.CREATED.getNome().equalsIgnoreCase(f.getStatus())
                )
                .collect(Collectors.toList());

        if (!sameStepPending.isEmpty()) {
            // a) Signers em ordem alfabética
            List<FlowActionsTechCertVO> signers = sameStepPending.stream()
                    .filter(f -> "Signer".equalsIgnoreCase(f.getType()) && f.getUser() != null)
                    .sorted(Comparator.comparing(f -> f.getUser().getName(), String.CASE_INSENSITIVE_ORDER))
                    .collect(Collectors.toList());

            // b) SignRules em ordem alfabética pelo nome do primeiro usuário
            List<FlowActionsTechCertVO> signRules = sameStepPending.stream()
                    .filter(f -> "SignRule".equalsIgnoreCase(f.getType())
                            && f.getSignRuleUsers() != null
                            && !f.getSignRuleUsers().isEmpty())
                    .sorted(Comparator.comparing(
                            f -> f.getSignRuleUsers().get(0).getName(),
                            String.CASE_INSENSITIVE_ORDER))
                    .collect(Collectors.toList());

            // c) Combina as listas na ordem de prioridade
            List<FlowActionsTechCertVO> ordemExecucao = new ArrayList<>();
            ordemExecucao.addAll(signers);
            ordemExecucao.addAll(signRules);

            // d) Só gera URL para o primeiro da fila
            if (!ordemExecucao.isEmpty()
                    && !ordemExecucao.get(0).getId().equals(flowActionsTechCertVO.getId())) {
                return null;
            }
        }

        // 7) Se passou por todas as regras, gera URL normalmente
        JSONObject payload = montarJsonParaGerarUrlsAssinaturaPorFlowAction(flowActionsTechCertVO);
        return gerarUrlsAssinatura(
                configuracaoGEDVO,
                documentsTechCertVO.getId(),
                payload
        );
    }

    private DocumentActionUrlTechCertVO regraGeracaoOrdemUrlAssinaturaNovo(String name, String identifier, DocumentsTechCertVO documentsTechCertVO, ConfiguracaoGEDVO configuracaoGEDVO, IntegracaoTechCertVO integracaoTechCertVO) {
        // 1) Ordena todos os flows por step
        List<FlowActionsTechCertVO> sortedFlows = documentsTechCertVO.getFlowActions().stream()
                .sorted(Comparator.comparingInt(FlowActionsTechCertVO::getStep))
                .collect(Collectors.toList());
        if (sortedFlows.isEmpty()) {
            return null;
        }

        // 2) Descobre o menor step PENDING ou CREATED
        Integer menorStepPending = sortedFlows.stream()
                .filter(f -> StatusFlowActionsTechCertEnum.PENDING.getNome().equalsIgnoreCase(f.getStatus())
                        || StatusFlowActionsTechCertEnum.CREATED.getNome().equalsIgnoreCase(f.getStatus()))
                .map(FlowActionsTechCertVO::getStep)
                .findFirst()
                .orElse(null);
        if (menorStepPending == null) {
            return null;
        }

        // 3) Coleta todos os flows do menor step pendente/created
        List<FlowActionsTechCertVO> flowsMenorStep = sortedFlows.stream()
                .filter(f -> f.getStep().equals(menorStepPending)
                        && (StatusFlowActionsTechCertEnum.PENDING.getNome().equalsIgnoreCase(f.getStatus())
                        || StatusFlowActionsTechCertEnum.CREATED.getNome().equalsIgnoreCase(f.getStatus())))
                .collect(Collectors.toList());
        if (flowsMenorStep.isEmpty()) {
            return null;
        }

        // 4?6) Se só tiver um flow, usa ele; se mais, ordena dentro do mesmo step
        FlowActionsTechCertVO flowParaLink;
        if (flowsMenorStep.size() == 1) {
            flowParaLink = flowsMenorStep.get(0);

        } else {
            // 4) Filtra e ordena Signers pelo nome
            List<FlowActionsTechCertVO> signers = flowsMenorStep.stream()
                    .filter(f -> "Signer".equalsIgnoreCase(f.getType()) && f.getUser() != null)
                    .sorted(Comparator.comparing(f -> f.getUser().getName(), String.CASE_INSENSITIVE_ORDER))
                    .collect(Collectors.toList());

            // 5) Filtra e ordena SignRules pelo nome do primeiro usuário
            List<FlowActionsTechCertVO> signRules = flowsMenorStep.stream()
                    .filter(f -> "SignRule".equalsIgnoreCase(f.getType())
                            && f.getSignRuleUsers() != null
                            && !f.getSignRuleUsers().isEmpty())
                    .sorted(Comparator.comparing(
                            f -> f.getSignRuleUsers().get(0).getName(),
                            String.CASE_INSENSITIVE_ORDER))
                    .collect(Collectors.toList());

            // 6) Junta Signers e SignRules na ordem
            List<FlowActionsTechCertVO> ordemExecucao = new ArrayList<>();
            ordemExecucao.addAll(signers);
            ordemExecucao.addAll(signRules);

            if (ordemExecucao.isEmpty()) {
                return null;
            }
            flowParaLink = ordemExecucao.get(0);
        }

        // 7) Verifica se é o usuário passado no método
        boolean match;
        if ("Signer".equalsIgnoreCase(flowParaLink.getType()) && flowParaLink.getUser() != null) {
            match = Uteis.removerAcentos(Uteis.removeCaractersEspeciais(flowParaLink.getUser().getName())).replace(" ", "").equalsIgnoreCase(name)
                    && Uteis.removerAcentos(Uteis.removeCaractersEspeciais(flowParaLink.getUser().getIdentifier())).replace(" ", "").equalsIgnoreCase(identifier);
        } else {
            SignRuleUserTechCertVO u = flowParaLink.getSignRuleUsers().get(0);
            match = Uteis.removerAcentos(Uteis.removeCaractersEspeciais(u.getName())).replace(" ", "").equalsIgnoreCase(name)
                    && Uteis.removerAcentos(Uteis.removeCaractersEspeciais(u.getIdentifier())).replace(" ", "").equalsIgnoreCase(identifier);
        }
        if (!match) {
            return null;
        }

        // 8) Extrai identifier e email do flow selecionado
        String targetIdentifier;
        String email;
        if ("Signer".equalsIgnoreCase(flowParaLink.getType())) {
            targetIdentifier = flowParaLink.getUser().getIdentifier();
            email = flowParaLink.getUser().getEmail();
        } else {
            SignRuleUserTechCertVO u = flowParaLink.getSignRuleUsers().get(0);
            targetIdentifier = u.getIdentifier();
            email = u.getEmail();
        }

        // 9) Localiza o DocumentPessoaRSVO correspondente em integracaoTechCertVO
        DocumentPessoaRSVO signer = null;
        List<DocumentPessoaRSVO> allSigners = new ArrayList<>();
        if (integracaoTechCertVO.getSigners() != null) {
            allSigners.addAll(integracaoTechCertVO.getSigners());
        }
        if (integracaoTechCertVO.getElectronicSigners() != null) {
            allSigners.addAll(integracaoTechCertVO.getElectronicSigners());
        }
        for (DocumentPessoaRSVO s : allSigners) {
            if (targetIdentifier.equalsIgnoreCase(s.getIdentifier())) {
                signer = s;
                break;
            }
        }
        if (signer == null && email != null) {
            for (DocumentPessoaRSVO s : allSigners) {
                if (email.equalsIgnoreCase(s.getEmail())) {
                    signer = s;
                    break;
                }
            }
        }
        if (signer == null) {
            return null;
        }

        // 10) Gera e retorna a URL
        return gerarUrlsAssinaturaNovo(
                configuracaoGEDVO,
                documentsTechCertVO.getId(),
                flowParaLink.getId(),
                signer);
    }

    private DocumentoAssinadoPessoaVO montarNovoDocumentoAssinadoPessoaVOTechCert(DocumentsTechCertVO documentsTechCertVO, ConfiguracaoGEDVO configuracaoGEDVO, FlowActionsTechCertVO novoAssinante, DocumentoAssinadoVO documentoAssinadoVO, DocumentKeyValidationTechCertVO documentKeyValidationTechCertVO) throws ParseException, ParseException {
        Date dataAssinaturaParsed = null;
        DocumentActionUrlTechCertVO links = null;

        if (documentKeyValidationTechCertVO.getId() != null) {
            SignerTechCertVO signerAtual = documentKeyValidationTechCertVO.getSigners().stream()
                    .filter(signer -> signer.getIdentifier().equals(novoAssinante.getUser().getIdentifier()))
                    .findFirst()
                    .orElse(null);
            if (signerAtual != null && signerAtual.getSigningTime() != null) {
                dataAssinaturaParsed = parseAndTruncate(signerAtual.getSigningTime(), "yyyy-MM-dd'T'HH:mm");
            }
        }

        DocumentoAssinadoPessoaVO documentoAssinadoPessoaVO = new DocumentoAssinadoPessoaVO();
        documentoAssinadoPessoaVO.setNomePessoa(novoAssinante.getUser().getName());
        documentoAssinadoPessoaVO.setCpfPessoa(Uteis.removerMascara(novoAssinante.getUser().getIdentifier()));
        documentoAssinadoPessoaVO.setEmailPessoa(novoAssinante.getUser().getEmail());
        documentoAssinadoPessoaVO.setDocumentoAssinadoVO(documentoAssinadoVO);
        documentoAssinadoPessoaVO.setCodigoAssinatura(novoAssinante.getId());

        if (StatusDocumentActionTechCertEnum.PENDING.getNome().equalsIgnoreCase(documentsTechCertVO.getStatus())
                && StatusFlowActionsTechCertEnum.PENDING.getNome().equalsIgnoreCase(novoAssinante.getStatus())
                || StatusFlowActionsTechCertEnum.CREATED.getNome().equalsIgnoreCase(novoAssinante.getStatus())) {
        	String usuarioValidar = Uteis.isAtributoPreenchido(novoAssinante.getUser().getName()) ? Uteis.removerAcentos(Uteis.removeCaractersEspeciais(novoAssinante.getUser().getName())).replace(" ", "") : Constantes.EMPTY;
            String identificadorValidar = Uteis.isAtributoPreenchido(novoAssinante.getUser().getIdentifier()) ? Uteis.removerMascara(novoAssinante.getUser().getIdentifier()).replace(" ", "") : Constantes.EMPTY;
        	links = regraGeracaoOrdemUrlAssinatura(usuarioValidar, identificadorValidar, documentsTechCertVO, configuracaoGEDVO, novoAssinante);
        }

        if (links != null) {
            documentoAssinadoPessoaVO.setUrlAssinatura(links.getEmbedUrl());
            documentoAssinadoPessoaVO.setUrlProvedorDeAssinatura(links.getUrl());
        }

        if (StatusFlowActionsTechCertEnum.PENDING.getNome().equalsIgnoreCase(novoAssinante.getStatus())
                || StatusFlowActionsTechCertEnum.CREATED.getNome().equalsIgnoreCase(novoAssinante.getStatus())) {
            documentoAssinadoPessoaVO.setSituacaoDocumentoAssinadoPessoaEnum(SituacaoDocumentoAssinadoPessoaEnum.PENDENTE);
        }

        if (StatusFlowActionsTechCertEnum.REFUSED.getNome().equalsIgnoreCase(novoAssinante.getStatus())) {
            documentoAssinadoPessoaVO.setSituacaoDocumentoAssinadoPessoaEnum(SituacaoDocumentoAssinadoPessoaEnum.REJEITADO);
            documentoAssinadoPessoaVO.setMotivoRejeicao(novoAssinante.getRefusalReason());
            documentoAssinadoPessoaVO.setDataRejeicao(dataAssinaturaParsed);
        } else if (StatusFlowActionsTechCertEnum.COMPLETED.getNome().equalsIgnoreCase(novoAssinante.getStatus())) {
            documentoAssinadoPessoaVO.setSituacaoDocumentoAssinadoPessoaEnum(SituacaoDocumentoAssinadoPessoaEnum.ASSINADO);
            documentoAssinadoPessoaVO.setDataAssinatura(dataAssinaturaParsed);
        }
        return documentoAssinadoPessoaVO;
    }

	private boolean executarProcessamentoCertisignAntigo(DocumentoAssinadoVO doc, UsuarioVO usuarioLogado, ConfiguracaoGEDVO configGedVO, DocumentoAssinadoOrigemEnum documentoAssinadoOrigemEnum) throws Exception {
		CertiSignCallBackSignatureValidateRSVO objCertSign = getFacadeFactory().getDocumentoAssinadoFacade().realizarConsultarDocumentsValidateSignatures(doc, configGedVO);

		if (objCertSign == null || !Uteis.isAtributoPreenchido(objCertSign.getKey()) || !Uteis.isAtributoPreenchido(objCertSign.getDocumentName()) || objCertSign.getIsValid() == null || !objCertSign.getIsValid()) {
			return true;
		}

		List<DocumentoAssinadoPessoaVO> listaDapPendente = doc.getListaDocumentoAssinadoPessoa().stream().filter(dap -> dap.getSituacaoDocumentoAssinadoPessoaEnum().isPendente()).collect(Collectors.toList());
		q:
		for (DocumentoAssinadoPessoaVO dap : listaDapPendente) {
			String nome = Uteis.isAtributoPreenchido(dap.getPessoaVO()) ? Uteis.removerAcentos(Uteis.removeCaractersEspeciais(dap.getPessoaVO().getNome())).replace(" ", "") : Uteis.removerAcentos(Uteis.removeCaractersEspeciais(dap.getNomePessoa())).replace(" ", "");
			String cpf = Uteis.isAtributoPreenchido(dap.getPessoaVO()) ? Uteis.removerAcentos(Uteis.removeCaractersEspeciais(dap.getPessoaVO().getCPF())).replace(" ", "") : Uteis.removerAcentos(Uteis.removeCaractersEspeciais(dap.getCpfPessoa())).replace(" ", "");

			if (!objCertSign.getElectronicSignatures().isEmpty()) {
				for (CertiSignCallBackSignaturePessoaRSVO objAssinaturaEletronica : objCertSign.getElectronicSignatures()) {
					String user = Uteis.isAtributoPreenchido(objAssinaturaEletronica.getUser()) ? Uteis.removerAcentos(Uteis.removeCaractersEspeciais(objAssinaturaEletronica.getUser())).replace(" ", "") : "";
					String identifier = Uteis.isAtributoPreenchido(objAssinaturaEletronica.getIdentifier()) ? Uteis.removerAcentos(Uteis.removeCaractersEspeciais(objAssinaturaEletronica.getIdentifier())).replace(" ", "") : "";
					Map<String, Boolean> map = validarDocumentoAssinadoPessoaResponsavelPelaAssinatura(nome, cpf, user, identifier);
					if (map.get(PESSOA_RESPONSAVEL_ASSINATURA)) {
						CertiSignCallBackRSVO obj = new CertiSignCallBackRSVO();
						obj.setAction("SIGNATURE-ELETRONIC");
						obj.setDocumentId(stringParaIntegerOuNull(doc.getCodigoProvedorDeAssinatura()));
						obj.setName(objAssinaturaEletronica.getUser());
						if (!map.get(CPF_PESSOA_DIFERENTE_ASSINATURA)) {
							obj.setIdentifier(objAssinaturaEletronica.getIdentifier());
						}
						Date dataAssinaturaRejeicao = Uteis.getData(objAssinaturaEletronica.getDate(), "yyyy-MM-dd'T'HH:mm");
						getFacadeFactory().getDocumentoAssinadoFacade().executarAtualizacaoDadosAssinaturaPorProvedorCertisign(obj, dataAssinaturaRejeicao, documentoAssinadoOrigemEnum, usuarioLogado);
						continue q;
					}
				}
			}
			if (!objCertSign.getSignatures().isEmpty()) {
				for (CertiSignCallBackSignaturePessoaRSVO objAssinatura : objCertSign.getSignatures()) {
					String signer = Uteis.isAtributoPreenchido(objAssinatura.getCertificate().getSigner()) ? Uteis.removerAcentos(Uteis.removeCaractersEspeciais(objAssinatura.getCertificate().getSigner())).replace(" ", "") : "";
					String identifier = Uteis.isAtributoPreenchido(objAssinatura.getCertificate().getIndividualIdentificationCode()) ? Uteis.removerAcentos(Uteis.removeCaractersEspeciais(objAssinatura.getCertificate().getIndividualIdentificationCode())).replace(" ", "") : "";
					Map<String, Boolean> map = validarDocumentoAssinadoPessoaResponsavelPelaAssinatura(nome, cpf, signer, identifier);
					if (map.get(PESSOA_RESPONSAVEL_ASSINATURA)) {
						CertiSignCallBackRSVO obj = new CertiSignCallBackRSVO();
						obj.setAction("SIGNATURE-DIGITAL");
						obj.setDocumentId(stringParaIntegerOuNull(doc.getCodigoProvedorDeAssinatura()));
						obj.setName(objAssinatura.getCertificate().getSigner());
						if (!map.get(CPF_PESSOA_DIFERENTE_ASSINATURA)) {
							obj.setIdentifier(objAssinatura.getCertificate().getIndividualIdentificationCode());
						}
						Date dataAssinaturaRejeicao = Uteis.getData(objAssinatura.getDate(), "dd/MM/yyyy HH:mm");
						getFacadeFactory().getDocumentoAssinadoFacade().executarAtualizacaoDadosAssinaturaPorProvedorCertisign(obj, dataAssinaturaRejeicao, documentoAssinadoOrigemEnum, usuarioLogado);
						continue q;
					}
				}
			}
		}
		return false;
	}

    @Override
    public void executarProcessamentoTechCertAntigoPosTrocaAssinante(String chaveProvedorAssinatura, UsuarioVO usuarioLogado, ConfiguracaoGEDVO configGedVO,
                                                                     DocumentoAssinadoOrigemEnum documentoAssinadoOrigemEnum) throws Exception {
        // Realizar consulta dos dados atualizados
        DocumentoAssinadoVO docCompleto = getFacadeFactory().getDocumentoAssinadoFacade().consultarPorChaveProvedordeAssinatura(chaveProvedorAssinatura, Uteis.NIVELMONTARDADOS_DADOSDOCUMENTOASSINADO, usuarioLogado);
        executarProcessamentoTechCertAntigo(docCompleto, usuarioLogado, configGedVO, documentoAssinadoOrigemEnum);
    }

    private boolean executarProcessamentoTechCertAntigo(DocumentoAssinadoVO doc, UsuarioVO usuarioLogado, ConfiguracaoGEDVO configGedVO,
                                                        DocumentoAssinadoOrigemEnum documentoAssinadoOrigemEnum) throws Exception {
        if (doc == null) {
            throw new ConsistirException("Dados Não Encontrados ( Documento Assinado ).");
        }

        DocumentsTechCertVO documentsTechCertVO = consultarDocumet(configGedVO, doc.getChaveProvedorDeAssinatura());
        DocumentKeyValidationTechCertVO documentKeyValidationTechCertVO = getFacadeFactory().getDocumentoAssinadoFacade().realizarConsultarDocumentsValidateSignaturesTechcert(doc, configGedVO, documentsTechCertVO);

        if (documentsTechCertVO.getDeletedDocumentTechCert()) {
            throw new ConsistirException("Documento Deletado na TechCert.");
        }

        if (documentKeyValidationTechCertVO.getNoPendingActionFoundForEmailAndIdentifier()) {
            throw new ConsistirException("O document não possui ações pendentes");
        }

        if (documentKeyValidationTechCertVO.getDocumentInvalid()) {
            return true;
        }

        checarAssinantes(doc, usuarioLogado, configGedVO, documentoAssinadoOrigemEnum, documentsTechCertVO, documentKeyValidationTechCertVO);

        return false;
    }

    private void checarAssinantes(DocumentoAssinadoVO doc, UsuarioVO usuarioLogado, ConfiguracaoGEDVO configGedVO, DocumentoAssinadoOrigemEnum documentoAssinadoOrigemEnum, DocumentsTechCertVO documentsTechCertVO, DocumentKeyValidationTechCertVO documentKeyValidationTechCertVO) throws Exception {
        List<DocumentoAssinadoPessoaVO> listaDapPendente = doc.getListaDocumentoAssinadoPessoa().stream().filter(dap -> dap.getSituacaoDocumentoAssinadoPessoaEnum().isPendente()).collect(Collectors.toList());
        for (DocumentoAssinadoPessoaVO dap : listaDapPendente) {
            String nome = Uteis.isAtributoPreenchido(dap.getPessoaVO()) ? Uteis.removerAcentos(Uteis.removeCaractersEspeciais(dap.getPessoaVO().getNome())).replace(" ", "") : Uteis.removerAcentos(Uteis.removeCaractersEspeciais(dap.getNomePessoa())).replace(" ", "");
            String cpf = Uteis.isAtributoPreenchido(dap.getPessoaVO()) ? Uteis.removerAcentos(Uteis.removeCaractersEspeciais(dap.getPessoaVO().getCPF())).replace(" ", "") : Uteis.removerAcentos(Uteis.removeCaractersEspeciais(dap.getCpfPessoa())).replace(" ", "");

            for (FlowActionsTechCertVO flowAssinatura : documentsTechCertVO.getFlowActions()) {

                //========================Assinatura do Tipo Signer========================
                if (flowAssinatura.getUser() != null && Uteis.isAtributoPreenchido(flowAssinatura.getUser().getId())) {
                    String user = Uteis.isAtributoPreenchido(flowAssinatura.getUser().getName()) ? Uteis.removerAcentos(Uteis.removerAcentos(flowAssinatura.getUser().getName())).replace(" ", "") : "";
                    String identifier = Uteis.isAtributoPreenchido(flowAssinatura.getUser().getIdentifier()) ? Uteis.removeCaractersEspeciais(Uteis.removerAcentos(Uteis.removeCaractersEspeciais(flowAssinatura.getUser().getIdentifier()))).replace(" ", "") : "";
                    Map<String, Boolean> map = validarDocumentoAssinadoPessoaResponsavelPelaAssinatura(nome, cpf, user, identifier);
                    if (map.get(PESSOA_RESPONSAVEL_ASSINATURA)) {
                        flowAssinatura.setIsSignRule(false);
                        validarDocumentoAssinadoComRegrasTechCert(
                                user,
                                identifier,
                                documentKeyValidationTechCertVO,
                                documentsTechCertVO,
                                flowAssinatura,
                                doc,
                                configGedVO,
                                documentoAssinadoOrigemEnum,
                                usuarioLogado
                        );
                    }
                }

                //========================Assinatura do Tipo SignRule========================
                if (flowAssinatura.getSignRuleUsers() != null && !flowAssinatura.getSignRuleUsers().isEmpty()) {
                    for (SignRuleUserTechCertVO sign : flowAssinatura.getSignRuleUsers()) {
                        String user = Uteis.isAtributoPreenchido(sign.getName()) ? Uteis.removerAcentos(Uteis.removeCaractersEspeciais(sign.getName())).replace(" ", "") : "";
                        String identifier = Uteis.isAtributoPreenchido(sign.getIdentifier()) ? Uteis.removerAcentos(Uteis.removeCaractersEspeciais(sign.getIdentifier())).replace(" ", "") : "";
                        Map<String, Boolean> map = validarDocumentoAssinadoPessoaResponsavelPelaAssinatura(nome, cpf, user, identifier);
                        if (map.get(PESSOA_RESPONSAVEL_ASSINATURA)) {
                            flowAssinatura.setIsSignRule(true);
                            flowAssinatura.setSignRuleUserTechCertVOAtual(sign);
                            validarDocumentoAssinadoComRegrasTechCert(
                                    user,
                                    identifier,
                                    documentKeyValidationTechCertVO,
                                    documentsTechCertVO,
                                    flowAssinatura,
                                    doc,
                                    configGedVO,
                                    documentoAssinadoOrigemEnum,
                                    usuarioLogado
                            );
                        }
                    }
                }
            }
        }
    }


    private void validarDocumentoAssinadoComRegrasTechCert(String name, String identifier, DocumentKeyValidationTechCertVO documentKeyValidationTechCertVO, DocumentsTechCertVO documentsTechCertVO, FlowActionsTechCertVO flowActionsTechCertVO, DocumentoAssinadoVO doc, ConfiguracaoGEDVO configGedVO, DocumentoAssinadoOrigemEnum documentoAssinadoOrigemEnum, UsuarioVO usuarioLogado) throws Exception {

        //========================CASO DE NAO POSSUIR ASSINATURAS NO DOCUMENTO========================
        if (documentKeyValidationTechCertVO.getDocumentInvalid()
                && !StatusFlowActionsTechCertEnum.COMPLETED.getNome().equalsIgnoreCase(flowActionsTechCertVO.getStatus())) {
            Date dataAssinatura = parseAndTruncate(flowActionsTechCertVO.getUpdateDate(), "yyyy-MM-dd'T'HH:mm");
            Integer codigoPessoaDocumentoAssinadoPessoa = getFacadeFactory().getDocumentoAssinadoPessoaFacade().atualizarDadosAssinaturaPorProvedorTechCert(doc.getChaveProvedorDeAssinatura(),
                    name, identifier, SituacaoDocumentoAssinadoPessoaEnum.REJEITADO,
                    Uteis.isAtributoPreenchido(documentsTechCertVO.getStatusUpdateReason()) ? documentsTechCertVO.getStatusUpdateReason() : "Documento Invalido", dataAssinatura, documentoAssinadoOrigemEnum, "", "");

            if (doc.getTipoOrigemDocumentoAssinadoEnum().isTermoEstagioObrigatorio()) {
                EstagioVO estagio = getFacadeFactory().getEstagioFacade().consultarPorDocumentoAssinador(doc.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSDOCUMENTOASSINADO, usuarioLogado);
                String reason = Uteis.isAtributoPreenchido(documentsTechCertVO.getStatusUpdateReason()) ? documentsTechCertVO.getStatusUpdateReason() : "Documento Cancelado na TECHCERT";
                estagio.setMotivo("Cancelamento automático por Termo de Assinatura rejeitado pelo assinante: " + name + System.lineSeparator() + " mensagem: " + reason);
                getFacadeFactory().getEstagioFacade().realizarConfirmacaoEstagioIndeferido(estagio, SituacaoAdicionalEstagioEnum.INDEFERIDO_FACILITADOR, true, usuarioLogado);
            }

            if (doc.getTipoOrigemDocumentoAssinadoEnum().isAtaColacaoGrau()) {
                getFacadeFactory().getProgramacaoFormaturaAlunoFacade().realizarAlteracaoColouGrau(doc, codigoPessoaDocumentoAssinadoPessoa, SituacaoDocumentoAssinadoPessoaEnum.REJEITADO, usuarioLogado);
            }
        }
        if (documentKeyValidationTechCertVO.isDocumentHasNoSignatures()
                && StatusDocumentActionTechCertEnum.CANCELED.getNome().equalsIgnoreCase(documentsTechCertVO.getStatus())
                && !StatusFlowActionsTechCertEnum.COMPLETED.getNome().equalsIgnoreCase(flowActionsTechCertVO.getStatus())) {
            Date dataAssinatura = parseAndTruncate(flowActionsTechCertVO.getUpdateDate(), "yyyy-MM-dd'T'HH:mm");
            Integer codigoPessoaDocumentoAssinadoPessoa = getFacadeFactory().getDocumentoAssinadoPessoaFacade().atualizarDadosAssinaturaPorProvedorTechCert(doc.getChaveProvedorDeAssinatura(),
                    name, identifier, SituacaoDocumentoAssinadoPessoaEnum.REJEITADO,
                    Uteis.isAtributoPreenchido(documentsTechCertVO.getStatusUpdateReason()) ? documentsTechCertVO.getStatusUpdateReason() : "Documento Cancelado na TECHCERT", dataAssinatura, documentoAssinadoOrigemEnum, "", "");

            if (doc.getTipoOrigemDocumentoAssinadoEnum().isTermoEstagioObrigatorio()) {
                EstagioVO estagio = getFacadeFactory().getEstagioFacade().consultarPorDocumentoAssinador(doc.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSDOCUMENTOASSINADO, usuarioLogado);
                String reason = Uteis.isAtributoPreenchido(documentsTechCertVO.getStatusUpdateReason()) ? documentsTechCertVO.getStatusUpdateReason() : "Documento Cancelado na TECHCERT";
                estagio.setMotivo("Cancelamento automático por Termo de Assinatura rejeitado pelo assinante: " + name + System.lineSeparator() + " mensagem: " + reason);
                getFacadeFactory().getEstagioFacade().realizarConfirmacaoEstagioIndeferido(estagio, SituacaoAdicionalEstagioEnum.INDEFERIDO_FACILITADOR, true, usuarioLogado);
            }

            if (doc.getTipoOrigemDocumentoAssinadoEnum().isAtaColacaoGrau()) {
                getFacadeFactory().getProgramacaoFormaturaAlunoFacade().realizarAlteracaoColouGrau(doc, codigoPessoaDocumentoAssinadoPessoa, SituacaoDocumentoAssinadoPessoaEnum.REJEITADO, usuarioLogado);
            }
        }
        if (documentKeyValidationTechCertVO.isDocumentHasNoSignatures()
                && StatusDocumentActionTechCertEnum.EXPIRED.getNome().equalsIgnoreCase(documentsTechCertVO.getStatus())
                && !StatusFlowActionsTechCertEnum.COMPLETED.getNome().equalsIgnoreCase(flowActionsTechCertVO.getStatus())) {
            Date dataAssinatura = parseAndTruncate(flowActionsTechCertVO.getUpdateDate(), "yyyy-MM-dd'T'HH:mm");
            Integer codigoPessoaDocumentoAssinadoPessoa = getFacadeFactory().getDocumentoAssinadoPessoaFacade().atualizarDadosAssinaturaPorProvedorTechCert(doc.getChaveProvedorDeAssinatura(), name, identifier, SituacaoDocumentoAssinadoPessoaEnum.REJEITADO,
                    "Periodo de Assinatura Expirado", dataAssinatura, documentoAssinadoOrigemEnum, "", "");

            if (doc.getTipoOrigemDocumentoAssinadoEnum().isTermoEstagioObrigatorio()) {
                EstagioVO estagio = getFacadeFactory().getEstagioFacade().consultarPorDocumentoAssinador(doc.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSDOCUMENTOASSINADO, usuarioLogado);
                String reason = "Periodo de Assinatura Expirado";
                estagio.setMotivo("Cancelamento automático por Termo de Assinatura rejeitado pelo assinante: " + name + System.lineSeparator() + " mensagem: " + reason);
                getFacadeFactory().getEstagioFacade().realizarConfirmacaoEstagioIndeferido(estagio, SituacaoAdicionalEstagioEnum.INDEFERIDO_FACILITADOR, true, usuarioLogado);
            }

            if (doc.getTipoOrigemDocumentoAssinadoEnum().isAtaColacaoGrau()) {
                getFacadeFactory().getProgramacaoFormaturaAlunoFacade().realizarAlteracaoColouGrau(doc, codigoPessoaDocumentoAssinadoPessoa, SituacaoDocumentoAssinadoPessoaEnum.REJEITADO, usuarioLogado);
            }
        }
        if (documentKeyValidationTechCertVO.isDocumentHasNoSignatures()
                && StatusDocumentActionTechCertEnum.REFUSED.getNome().equalsIgnoreCase(documentsTechCertVO.getStatus())
                && !StatusFlowActionsTechCertEnum.COMPLETED.getNome().equalsIgnoreCase(flowActionsTechCertVO.getStatus())) {
            Date dataAssinatura = parseAndTruncate(flowActionsTechCertVO.getUpdateDate(), "yyyy-MM-dd'T'HH:mm");
            Integer codigoPessoaDocumentoAssinadoPessoa = getFacadeFactory().getDocumentoAssinadoPessoaFacade().atualizarDadosAssinaturaPorProvedorTechCert(doc.getChaveProvedorDeAssinatura(), name, identifier, SituacaoDocumentoAssinadoPessoaEnum.REJEITADO,
                    Uteis.isAtributoPreenchido(documentsTechCertVO.getStatusUpdateReason()) ? documentsTechCertVO.getStatusUpdateReason() : "Documento rejeitado", dataAssinatura, documentoAssinadoOrigemEnum, "", "");

            // TODO TECHCERT ESTAGIO
            if (doc.getTipoOrigemDocumentoAssinadoEnum().isTermoEstagioObrigatorio()) {
                EstagioVO estagio = getFacadeFactory().getEstagioFacade().consultarPorDocumentoAssinador(doc.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSDOCUMENTOASSINADO, usuarioLogado);
                String reason = Uteis.isAtributoPreenchido(documentsTechCertVO.getStatusUpdateReason()) ? documentsTechCertVO.getStatusUpdateReason() : "Documento rejeitado";
                estagio.setMotivo("Cancelamento automático por Termo de Assinatura rejeitado pelo assinante: " + name + System.lineSeparator() + " mensagem: " + reason);
                getFacadeFactory().getEstagioFacade().realizarConfirmacaoEstagioIndeferido(estagio, SituacaoAdicionalEstagioEnum.INDEFERIDO_FACILITADOR, true, usuarioLogado);
            }

            if (doc.getTipoOrigemDocumentoAssinadoEnum().isAtaColacaoGrau()) {
                getFacadeFactory().getProgramacaoFormaturaAlunoFacade().realizarAlteracaoColouGrau(doc, codigoPessoaDocumentoAssinadoPessoa, SituacaoDocumentoAssinadoPessoaEnum.REJEITADO, usuarioLogado);
            }
        }
        //========================================================================================

        //========================DOCUMENTO VALIDO========================
        DocumentActionUrlTechCertVO documentActionUrlTechCertVO = null;
        // Validar link
        if (isGerarUrlFlowAction(documentsTechCertVO, flowActionsTechCertVO)) {
            documentActionUrlTechCertVO = regraGeracaoOrdemUrlAssinatura(name, identifier, documentsTechCertVO, configGedVO, flowActionsTechCertVO);
            if (!flowActionsTechCertVO.getIsSignRule() && documentActionUrlTechCertVO != null) {
                executarAtualizacaoDadosAssinaturaPorProvedorTechCertSign(name, identifier, documentsTechCertVO, flowActionsTechCertVO, documentoAssinadoOrigemEnum, usuarioLogado, documentActionUrlTechCertVO, doc, documentKeyValidationTechCertVO);
            }
            if (flowActionsTechCertVO.getIsSignRule() && documentActionUrlTechCertVO != null) {
                executarAtualizacaoDadosAssinaturaPorProvedorTechCertSignRule(name, identifier, documentsTechCertVO, flowActionsTechCertVO, documentoAssinadoOrigemEnum, usuarioLogado, documentActionUrlTechCertVO, doc, documentKeyValidationTechCertVO);
            }
        }

        // Validar assinaturas
        if (isValidoAssinante(documentKeyValidationTechCertVO)) {
            if (!flowActionsTechCertVO.getIsSignRule()) {
                executarAtualizacaoDadosAssinaturaPorProvedorTechCertSign(name, identifier, documentsTechCertVO, flowActionsTechCertVO, documentoAssinadoOrigemEnum, usuarioLogado, documentActionUrlTechCertVO, doc, documentKeyValidationTechCertVO);
            }
            if (flowActionsTechCertVO.getIsSignRule()) {
                executarAtualizacaoDadosAssinaturaPorProvedorTechCertSignRule(name, identifier, documentsTechCertVO, flowActionsTechCertVO, documentoAssinadoOrigemEnum, usuarioLogado, documentActionUrlTechCertVO, doc, documentKeyValidationTechCertVO);
            }
        }
        //=================================================================================
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DocumentsTechCertVO consultarDocumet(ConfiguracaoGEDVO configuracaoGEDVO, String documentId) {
        if (!Uteis.isAtributoPreenchido(documentId)) {
            DocumentsTechCertVO documentsTechCertVO = new DocumentsTechCertVO();
            documentsTechCertVO.setDeletedDocumentTechCert(true);
            return documentsTechCertVO;
        }
        try {
            if (!configuracaoGEDVO.getHabilitarIntegracaoTechCert()){
                throw new StreamSeiException("Configuração de integração com TechCert não está habilitada.");
            }
            StringBuilder url = new StringBuilder();
            url.append(montarUrlPadrao(configuracaoGEDVO));
            url.append(UteisWebServiceUrl.URL_TECHCERT_DOCUMENTS + "/" + documentId);
            String token = montarToken(configuracaoGEDVO);
            HttpResponse<JsonNode> jsonResponse = unirest()
                    .get(url.toString())
                    .header(UteisWebServiceUrl.Content_Type, UteisWebServiceUrl.Application_JSON)
                    .header(configuracaoGEDVO.getApikeyTechCert(), token)
                    .connectTimeout(10000)
                    .asJson();
            if (jsonResponse.isSuccess() || jsonResponse.getStatus() == 200) {
                Gson gson = new GsonBuilder().create();
                return gson.fromJson(jsonResponse.getBody().toString(), DocumentsTechCertVO.class);
            } else if (jsonResponse.getStatus() == 422) {
                JSONObject error = jsonResponse.getBody().getObject();
                String code = error.optString("code", "");
                if ("DocumentIsDeleted".equalsIgnoreCase(code)) {
                    DocumentsTechCertVO documentsTechCertVO = new DocumentsTechCertVO();
                    documentsTechCertVO.setDeletedDocumentTechCert(true);
                    return documentsTechCertVO;
                } else {
                    tratarErroTechCert(jsonResponse.getBody().toString(), jsonResponse.getStatus());
                    throw new StreamSeiException(jsonResponse.getBody().toString());
                }
            } else {
                tratarErroTechCert(jsonResponse.getBody().toString(), jsonResponse.getStatus());
                throw new StreamSeiException(jsonResponse.getBody().toString());
            }
        } catch (Exception e) {
            throw e;
        }
    }


    /**
     * Parseia uma string ISO (com offset) e trunca ao menor componente
     * que aparece no pattern dado.
     *
     * @param isoOffsetDateTime ex: "2025-07-03T14:02:38.4749573+00:00"
     * @param pattern           ex: "yyyy-MM-dd'T'HH:mm", "yyyy-MM-dd'T'HH:mm:ss"
     * @return Date truncada à precisão do pattern
     */
    public Date parseAndTruncate(String isoOffsetDateTime, String pattern) {
        OffsetDateTime odt = OffsetDateTime.parse(
                isoOffsetDateTime,
                DateTimeFormatter.ISO_OFFSET_DATE_TIME
        );
        ChronoUnit unit;
        if (pattern.contains("ss")) {
            unit = ChronoUnit.SECONDS;
        } else if (pattern.contains("mm")) {
            unit = ChronoUnit.MINUTES;
        } else if (pattern.contains("HH")) {
            unit = ChronoUnit.HOURS;
        } else {
            unit = ChronoUnit.MINUTES;
        }
        odt = odt.truncatedTo(unit);
        return Date.from(odt.toInstant());
    }

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public DocumentFlowActionsResponseRSVO realizarConsultaDocumentFlowActions(DocumentoAssinadoVO doc, ConfiguracaoGEDVO configGedVO) {
		try {
			HttpResponse<JsonNode> jsonResponse = unirest().get(configGedVO.getUrlProvedorAssinatura(doc.getProvedorDeAssinaturaEnum()) + UteisWebServiceUrl.URL_SEI_WEB_SERVICE_CERTISIGN_FLOW_ACTIONS)
					.header(UteisWebServiceUrl.Content_Type, UteisWebServiceUrl.Application_JSON)
					.header(configGedVO.getChaveToken(doc.getProvedorDeAssinaturaEnum()), configGedVO.getValorTokenProvedorAssinatura(doc.getProvedorDeAssinaturaEnum()))
					.queryString("id", doc.getCodigoProvedorDeAssinatura()).asJson();

            if (jsonResponse.isSuccess() || jsonResponse.getStatus() == 200) {
                Gson gson = new GsonBuilder().create();
                return gson.fromJson(jsonResponse.getBody().toString(), DocumentFlowActionsResponseRSVO.class);
            } else {
                throw new StreamSeiException(jsonResponse.getBody().toString());
            }
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public CertiSignCallBackSignatureValidateRSVO realizarConsultarDocumentsValidateSignatures(DocumentoAssinadoVO doc, ConfiguracaoGEDVO configGedVO) throws Exception {
        try {
            HttpResponse<JsonNode> jsonResponse = unirest().get(configGedVO.getUrlProvedorAssinatura(doc.getProvedorDeAssinaturaEnum()) + UteisWebServiceUrl.URL_SEI_WEB_SERVICE_CERTISIGN_VALIDATE_SIGNATURES)
                    .header(UteisWebServiceUrl.Content_Type, UteisWebServiceUrl.Application_JSON)
                    .header(configGedVO.getChaveToken(doc.getProvedorDeAssinaturaEnum()), configGedVO.getValorTokenProvedorAssinatura(doc.getProvedorDeAssinaturaEnum()))
                    .queryString("key", doc.getChaveProvedorDeAssinatura()).asJson();
            if (jsonResponse.isSuccess() || jsonResponse.getStatus() == 200) {
                Type type = new TypeToken<CertiSignCallBackSignatureValidateRSVO>() {
                }.getType();
                Gson gson = new GsonBuilder().create();
                return gson.fromJson(jsonResponse.getBody().toString(), type);
            } else {
                throw new StreamSeiException(jsonResponse.getBody().toString());
            }
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DocumentKeyValidationTechCertVO realizarConsultarDocumentsValidateSignaturesTechcert(DocumentoAssinadoVO doc, ConfiguracaoGEDVO configGedVO, DocumentsTechCertVO documentsTechCertVO) {
        try {
            if (!configGedVO.getHabilitarIntegracaoTechCert()){
                throw new StreamSeiException("Configuração de integração com TechCert não está habilitada.");
            }
            HttpResponse<JsonNode> jsonResponse =
                    unirest().get(montarUrlPadrao(configGedVO) + UteisWebServiceUrl.URL_TECHCERT_KEYS + "/" + documentsTechCertVO.getKey() + "/signatures")
                            .header(UteisWebServiceUrl.Content_Type, UteisWebServiceUrl.Application_JSON)
                            .header(configGedVO.getApikeyTechCert(), montarToken(configGedVO))
                            .connectTimeout(10_000)
                            .asJson();
            if (jsonResponse.isSuccess() || jsonResponse.getStatus() == 200) {
                Gson gson = new GsonBuilder().create();
                return gson.fromJson(jsonResponse.getBody().toString(), DocumentKeyValidationTechCertVO.class);
            }
            if (jsonResponse.getStatus() == 422) {
                JSONObject error = jsonResponse.getBody().getObject();
                String code = error.optString("code", "");
                if ("DocumentHasNoSignatures".equalsIgnoreCase(code)) {
                    DocumentKeyValidationTechCertVO documentKeyValidationTechCertVO = new DocumentKeyValidationTechCertVO();
                    documentKeyValidationTechCertVO.setDocumentHasNoSignatures(true);
                    return documentKeyValidationTechCertVO;
                }
                else if ("DocumentInvalidKey".equalsIgnoreCase(code) || "DocumentIsDeleted".equalsIgnoreCase(code)) {
                    DocumentKeyValidationTechCertVO documentKeyValidationTechCertVO = new DocumentKeyValidationTechCertVO();
                    documentKeyValidationTechCertVO.setDocumentInvalid(true);
                    return documentKeyValidationTechCertVO;
                }
                else if ("NoPendingActionFoundForEmailAndIdentifier".equalsIgnoreCase(code)) {
                    DocumentKeyValidationTechCertVO documentKeyValidationTechCertVO = new DocumentKeyValidationTechCertVO();
                    documentKeyValidationTechCertVO.setNoPendingActionFoundForEmailAndIdentifier(true);
                    return documentKeyValidationTechCertVO;
                }
                else {
                    tratarErroTechCert(jsonResponse.toString(), jsonResponse.getStatus());
                    throw new StreamSeiException(jsonResponse.getBody().toString());
                }
            } else {
                tratarErroTechCert(jsonResponse.toString(), jsonResponse.getStatus());
                throw new StreamSeiException(jsonResponse.getBody().toString());
            }
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DocumentoAssinadoVO executarAtualizacaoDadosAssinaturaPorProvedorCertisign(CertiSignCallBackRSVO obj, Date dataAssinaturaOrRejeicao, DocumentoAssinadoOrigemEnum documentoAssinadoOrigemEnum, UsuarioVO usuarioOperacoesExternas) throws Exception {
        DocumentoAssinadoVO doc = new DocumentoAssinadoVO();
        if (Uteis.isAtributoPreenchido(obj.getDocumentId())) {
            Gson gson = inicializaGson();
            String jsonAssinatura = gson.toJson(obj);
            doc = consultarPorCodigoProvedorAssinatura(numeroParaStringOuNull(obj.getDocumentId()), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, new UsuarioVO());
            if (Uteis.isAtributoPreenchido(obj.getName()) && (obj.isActionSignatureEletronic() || obj.isActionSignatureDigital())) {
                Integer codigoPessoaDocumentoAssinadoPessoa = getFacadeFactory().getDocumentoAssinadoPessoaFacade().atualizarDadosAssinaturaPorProvedorCertisign(numeroParaStringOuNull(obj.getDocumentId()), obj.getName(), obj.getIdentifier(), SituacaoDocumentoAssinadoPessoaEnum.ASSINADO, "", dataAssinaturaOrRejeicao, documentoAssinadoOrigemEnum, jsonAssinatura);
                if (doc.getTipoOrigemDocumentoAssinadoEnum().isTermoEstagioObrigatorio() && consultarSeDocumentoAssinadoTodoAssinado(numeroParaStringOuNull(obj.getDocumentId()))) {
                    getFacadeFactory().getEstagioFacade().realizarAlteracaoEstagioRealizando(doc, usuarioOperacoesExternas);
                } else if (doc.getTipoOrigemDocumentoAssinadoEnum().isAtaColacaoGrau()) {
                    getFacadeFactory().getProgramacaoFormaturaAlunoFacade().realizarAlteracaoColouGrau(doc, codigoPessoaDocumentoAssinadoPessoa, SituacaoDocumentoAssinadoPessoaEnum.ASSINADO, usuarioOperacoesExternas);
                }
            } else if (Uteis.isAtributoPreenchido(obj.getName()) && (obj.isActionSignatureNok() || obj.isActionApprovalNok() || obj.isActionAutorizationNok())) {
                Integer codigoPessoaDocumentoAssinadoPessoa = getFacadeFactory().getDocumentoAssinadoPessoaFacade().atualizarDadosAssinaturaPorProvedorCertisign(numeroParaStringOuNull(obj.getDocumentId()), obj.getName(), obj.getIdentifier(), SituacaoDocumentoAssinadoPessoaEnum.REJEITADO, obj.getMessage(), dataAssinaturaOrRejeicao, documentoAssinadoOrigemEnum, jsonAssinatura);
                if (doc.getTipoOrigemDocumentoAssinadoEnum().isTermoEstagioObrigatorio()) {
                    EstagioVO estagio = getFacadeFactory().getEstagioFacade().consultarPorDocumentoAssinador(doc.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSDOCUMENTOASSINADO, usuarioOperacoesExternas);
                    estagio.setMotivo("Cancelamento automático por Termo de Assinatura rejeitado pelo assinante: " + obj.getName() + System.lineSeparator() + " mensagem: " + obj.getMessage());
                    getFacadeFactory().getEstagioFacade().realizarConfirmacaoEstagioIndeferido(estagio, SituacaoAdicionalEstagioEnum.INDEFERIDO_FACILITADOR, true, usuarioOperacoesExternas);
                } else if (doc.getTipoOrigemDocumentoAssinadoEnum().isAtaColacaoGrau()) {
                    getFacadeFactory().getProgramacaoFormaturaAlunoFacade().realizarAlteracaoColouGrau(doc, codigoPessoaDocumentoAssinadoPessoa, SituacaoDocumentoAssinadoPessoaEnum.REJEITADO, usuarioOperacoesExternas);

                }
            }
        }
        return doc;
    }

    private DocumentoAssinadoVO executarAtualizacaoDadosAssinaturaPorProvedorTechCertSign(String name, String identifier, DocumentsTechCertVO documentsTechCertVO, FlowActionsTechCertVO flowActionsTechCertVO, DocumentoAssinadoOrigemEnum documentoAssinadoOrigemEnum, UsuarioVO usuarioVO, DocumentActionUrlTechCertVO documentActionUrlTechCertVO, DocumentoAssinadoVO doc, DocumentKeyValidationTechCertVO documentKeyValidationTechCertVO) throws Exception {
        if (Uteis.isAtributoPreenchido(documentsTechCertVO.getId()) && Uteis.isAtributoPreenchido(flowActionsTechCertVO.getId()) && !flowActionsTechCertVO.getIsSignRule()) {
            Gson gson = inicializaGson();
            String jsonAssinatura = gson.toJson(flowActionsTechCertVO);
            Date dataAssinaturaOrRejeicao = parseAndTruncate(flowActionsTechCertVO.getUpdateDate(), "yyyy-MM-dd'T'HH:mm");
            /**
             * STATUS Pending - deve ser atualizado o status dos assinantes
             * nesse caso foi observado que pode gerar erro ao atualizar todos os assinates
             * que passar com status PENDING e atualizar os mesmo que tem status PENDENTE
             * O quer é reforçado tambem sobre alteração de status na techcert
             * ==Ações concluídas não podem ser modificada==
             */
            if (isGerarUrlFlowAction(documentsTechCertVO, flowActionsTechCertVO)
                    && documentActionUrlTechCertVO != null
                    && Uteis.isAtributoPreenchido(documentActionUrlTechCertVO.getEmbedUrl())
                    && !documentActionUrlTechCertVO.getNoPendingActionFoundForEmailAndIdentifier()) {
                getFacadeFactory().getDocumentoAssinadoPessoaFacade().atualizarDadosAssinaturaPorProvedorTechCertUrl(
                        documentsTechCertVO.getId(),
                        name,
                        identifier,
                        SituacaoDocumentoAssinadoPessoaEnum.PENDENTE,
                        documentoAssinadoOrigemEnum,
                        "",
                        documentActionUrlTechCertVO != null ? documentActionUrlTechCertVO.getEmbedUrl() : "",
                        documentActionUrlTechCertVO != null ? documentActionUrlTechCertVO.getUrl() : "");
            }

            if (isDocumentAssinadoOrConcluido(flowActionsTechCertVO)) {
                for (SignerTechCertVO signer : documentKeyValidationTechCertVO.getSigners()) {
                    String userAtual = Uteis.isAtributoPreenchido(signer.getSubjectName()) ? Uteis.removerAcentos(Uteis.removerAcentos(signer.getSubjectName())).replace(" ", "") : "";
                    String identifierAtual = Uteis.isAtributoPreenchido(signer.getIdentifier()) ? Uteis.removeCaractersEspeciais(Uteis.removerAcentos(Uteis.removeCaractersEspeciais(signer.getIdentifier()))).replace(" ", "") : "";
                    Map<String, Boolean> map = validarDocumentoAssinadoPessoaResponsavelPelaAssinatura(userAtual, identifierAtual, name, identifier);
                    if (map.get(PESSOA_RESPONSAVEL_ASSINATURA)) {
                        Date dataAssinatura = parseAndTruncate(signer.getSigningTime(), "yyyy-MM-dd'T'HH:mm");
                        Integer codigoPessoaDocumentoAssinadoPessoa =
                                getFacadeFactory().getDocumentoAssinadoPessoaFacade().atualizarDadosAssinaturaPorProvedorTechCert(
                                        documentsTechCertVO.getId(),
                                        name,
                                        identifier,
                                        SituacaoDocumentoAssinadoPessoaEnum.ASSINADO,
                                        "",
                                        dataAssinatura,
                                        documentoAssinadoOrigemEnum,
                                        jsonAssinatura,
                                        documentActionUrlTechCertVO != null ? documentActionUrlTechCertVO.getEmbedUrl() : "");
                        // TODO TECHCERT ESTAGIO
                        if (doc.getTipoOrigemDocumentoAssinadoEnum().isTermoEstagioObrigatorio() && consultarSeDocumentoAssinadoTodoAssinado(documentsTechCertVO.getId())) {
                            getFacadeFactory().getEstagioFacade().realizarAlteracaoEstagioRealizando(doc, usuarioVO);
                        }
                        // TODO TECHCERT COLACAO DE GRAU
                        else if (doc.getTipoOrigemDocumentoAssinadoEnum().isAtaColacaoGrau()) {
                            getFacadeFactory().getProgramacaoFormaturaAlunoFacade().realizarAlteracaoColouGrau(doc, codigoPessoaDocumentoAssinadoPessoa, SituacaoDocumentoAssinadoPessoaEnum.ASSINADO, usuarioVO);
                        }
                    }
                }
            }

            if (isDocumentRecusado(documentsTechCertVO, flowActionsTechCertVO)) {
                Integer codigoPessoaDocumentoAssinadoPessoa = getFacadeFactory().getDocumentoAssinadoPessoaFacade()
                        .atualizarDadosAssinaturaPorProvedorTechCert(documentsTechCertVO.getId(), name,
                                identifier, SituacaoDocumentoAssinadoPessoaEnum.REJEITADO,
                                Uteis.isAtributoPreenchido(documentsTechCertVO.getStatusUpdateReason()) ? documentsTechCertVO.getStatusUpdateReason() : "Documento rejeitado",
                                dataAssinaturaOrRejeicao, documentoAssinadoOrigemEnum, jsonAssinatura, "");

                if (doc.getTipoOrigemDocumentoAssinadoEnum().isTermoEstagioObrigatorio()) {
                    EstagioVO estagio = getFacadeFactory().getEstagioFacade().consultarPorDocumentoAssinador(doc.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSDOCUMENTOASSINADO, usuarioVO);
                    String reason = Uteis.isAtributoPreenchido(documentsTechCertVO.getStatusUpdateReason()) ? documentsTechCertVO.getStatusUpdateReason() : "Documento rejeitado";
                    estagio.setMotivo("Cancelamento automático por Termo de Assinatura rejeitado pelo assinante: " + name + System.lineSeparator() + " mensagem: " + reason);
                    getFacadeFactory().getEstagioFacade().realizarConfirmacaoEstagioIndeferido(estagio, SituacaoAdicionalEstagioEnum.INDEFERIDO_FACILITADOR, true, usuarioVO);
                }
                if (doc.getTipoOrigemDocumentoAssinadoEnum().isAtaColacaoGrau()) {
                    getFacadeFactory().getProgramacaoFormaturaAlunoFacade().realizarAlteracaoColouGrau(doc, codigoPessoaDocumentoAssinadoPessoa, SituacaoDocumentoAssinadoPessoaEnum.REJEITADO, usuarioVO);
                }
            }

            if (isDocumentCancelado(documentsTechCertVO, flowActionsTechCertVO)) {
                Integer codigoPessoaDocumentoAssinadoPessoa = getFacadeFactory().getDocumentoAssinadoPessoaFacade()
                        .atualizarDadosAssinaturaPorProvedorTechCert(documentsTechCertVO.getId(), name,
                                identifier, SituacaoDocumentoAssinadoPessoaEnum.REJEITADO,
                                Uteis.isAtributoPreenchido(documentsTechCertVO.getStatusUpdateReason()) ? documentsTechCertVO.getStatusUpdateReason() : "Documento Cancelado na TECHCERT",
                                dataAssinaturaOrRejeicao, documentoAssinadoOrigemEnum, jsonAssinatura, "");
                if (doc.getTipoOrigemDocumentoAssinadoEnum().isTermoEstagioObrigatorio()) {
                    EstagioVO estagio = getFacadeFactory().getEstagioFacade().consultarPorDocumentoAssinador(doc.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSDOCUMENTOASSINADO, usuarioVO);
                    String reason = Uteis.isAtributoPreenchido(documentsTechCertVO.getStatusUpdateReason()) ? documentsTechCertVO.getStatusUpdateReason() : "Documento Cancelado na TECHCERT";
                    estagio.setMotivo("Cancelamento automático por Termo de Assinatura rejeitado pelo assinante: " + name + System.lineSeparator() + " mensagem: " + reason);
                    getFacadeFactory().getEstagioFacade().realizarConfirmacaoEstagioIndeferido(estagio, SituacaoAdicionalEstagioEnum.INDEFERIDO_FACILITADOR, true, usuarioVO);
                }
                if (doc.getTipoOrigemDocumentoAssinadoEnum().isAtaColacaoGrau()) {
                    getFacadeFactory().getProgramacaoFormaturaAlunoFacade().realizarAlteracaoColouGrau(doc, codigoPessoaDocumentoAssinadoPessoa, SituacaoDocumentoAssinadoPessoaEnum.REJEITADO, usuarioVO);
                }
            }

            if (isDocumentExpirado(documentsTechCertVO, flowActionsTechCertVO)) {
                    Integer codigoPessoaDocumentoAssinadoPessoa = getFacadeFactory().getDocumentoAssinadoPessoaFacade()
                            .atualizarDadosAssinaturaPorProvedorTechCert(documentsTechCertVO.getId(), name,
                                   identifier, SituacaoDocumentoAssinadoPessoaEnum.REJEITADO,
                                    "Periodo de Assinatura Expirado",
                                    dataAssinaturaOrRejeicao, documentoAssinadoOrigemEnum, jsonAssinatura, "");
                if (doc.getTipoOrigemDocumentoAssinadoEnum().isTermoEstagioObrigatorio()) {
                    EstagioVO estagio = getFacadeFactory().getEstagioFacade().consultarPorDocumentoAssinador(doc.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSDOCUMENTOASSINADO, usuarioVO);
                    String reason = "Periodo de Assinatura Expirado";
                    estagio.setMotivo("Cancelamento automático por Termo de Assinatura rejeitado pelo assinante: " + name + System.lineSeparator() + " mensagem: " + reason);
                    getFacadeFactory().getEstagioFacade().realizarConfirmacaoEstagioIndeferido(estagio, SituacaoAdicionalEstagioEnum.INDEFERIDO_FACILITADOR, true, usuarioVO);
                }
                if (doc.getTipoOrigemDocumentoAssinadoEnum().isAtaColacaoGrau()) {
                    getFacadeFactory().getProgramacaoFormaturaAlunoFacade().realizarAlteracaoColouGrau(doc, codigoPessoaDocumentoAssinadoPessoa, SituacaoDocumentoAssinadoPessoaEnum.REJEITADO, usuarioVO);
                }
            }
        }
        return doc;
    }

    private DocumentoAssinadoVO executarAtualizacaoDadosAssinaturaPorProvedorTechCertSignRule(String name, String identifier, DocumentsTechCertVO documentsTechCertVO, FlowActionsTechCertVO flowActionsTechCertVO, DocumentoAssinadoOrigemEnum documentoAssinadoOrigemEnum, UsuarioVO usuarioVO, DocumentActionUrlTechCertVO documentActionUrlTechCertVO, DocumentoAssinadoVO doc, DocumentKeyValidationTechCertVO documentKeyValidationTechCertVO) throws Exception {
        if (Uteis.isAtributoPreenchido(documentsTechCertVO.getId()) && Uteis.isAtributoPreenchido(flowActionsTechCertVO.getId())) {
            Gson gson = inicializaGson();
            String jsonAssinatura = gson.toJson(flowActionsTechCertVO);
            Date dataAssinaturaOrRejeicao = parseAndTruncate(flowActionsTechCertVO.getUpdateDate(), "yyyy-MM-dd'T'HH:mm");
            /**
             * STATUS Pending - deve ser atualizado o status dos assinantes
             * nesse caso foi observado que pode gerar erro ao atualizar todos os assinates
             * que passar com status PENDING e atualizar os mesmo que tem status PENDENTE
             * O quer é reforçado tambem sobre alteração de status na techcert
             * ==Ações concluídas não podem ser modificada==
             */
            if (isGerarUrlFlowAction(documentsTechCertVO, flowActionsTechCertVO)
                    && documentActionUrlTechCertVO != null
                    && Uteis.isAtributoPreenchido(documentActionUrlTechCertVO.getEmbedUrl())
                    && !documentActionUrlTechCertVO.getNoPendingActionFoundForEmailAndIdentifier()) {
                getFacadeFactory().getDocumentoAssinadoPessoaFacade().atualizarDadosAssinaturaPorProvedorTechCertUrl(
                        documentsTechCertVO.getId(),
                        name,
                        identifier,
                        SituacaoDocumentoAssinadoPessoaEnum.PENDENTE,
                        documentoAssinadoOrigemEnum,
                        "",
                        documentActionUrlTechCertVO != null ? documentActionUrlTechCertVO.getEmbedUrl() : "",
                        documentActionUrlTechCertVO != null ? documentActionUrlTechCertVO.getUrl() : "");
            }

            if (isDocumentAssinadoOrConcluido(flowActionsTechCertVO) && !flowActionsTechCertVO.getSignRuleUserTechCertVOAtual().getHasRefused()) {
                for (SignerTechCertVO signer : documentKeyValidationTechCertVO.getSigners()) {
                    String userAtual = Uteis.isAtributoPreenchido(signer.getSubjectName()) ? Uteis.removerAcentos(Uteis.removerAcentos(signer.getSubjectName())).replace(" ", "") : "";
                    String identifierAtual = Uteis.isAtributoPreenchido(signer.getIdentifier()) ? Uteis.removeCaractersEspeciais(Uteis.removerAcentos(Uteis.removeCaractersEspeciais(signer.getIdentifier()))).replace(" ", "") : "";
                    Map<String, Boolean> map = validarDocumentoAssinadoPessoaResponsavelPelaAssinatura(userAtual, identifierAtual, name, identifier);
                    if (map.get(PESSOA_RESPONSAVEL_ASSINATURA)) {
                        Integer codigoPessoaDocumentoAssinadoPessoa =
                                getFacadeFactory().getDocumentoAssinadoPessoaFacade().atualizarDadosAssinaturaPorProvedorTechCert(
                                        documentsTechCertVO.getId(),
                                        name,
                                        identifier,
                                        SituacaoDocumentoAssinadoPessoaEnum.ASSINADO,
                                        "",
                                        dataAssinaturaOrRejeicao,
                                        documentoAssinadoOrigemEnum,
                                        jsonAssinatura,
                                        documentActionUrlTechCertVO != null ? documentActionUrlTechCertVO.getEmbedUrl() : "");
                        // TODO TECHCERT ESTAGIO
                        if (doc.getTipoOrigemDocumentoAssinadoEnum().isTermoEstagioObrigatorio() && consultarSeDocumentoAssinadoTodoAssinado(documentsTechCertVO.getId())) {
                            getFacadeFactory().getEstagioFacade().realizarAlteracaoEstagioRealizando(doc, usuarioVO);
                        }
                        // TODO TECHCERT COLACAO DE GRAU
                        else if (doc.getTipoOrigemDocumentoAssinadoEnum().isAtaColacaoGrau()) {
                            getFacadeFactory().getProgramacaoFormaturaAlunoFacade().realizarAlteracaoColouGrau(doc, codigoPessoaDocumentoAssinadoPessoa, SituacaoDocumentoAssinadoPessoaEnum.ASSINADO, usuarioVO);
                        }
                    }
                }
            }

            if (isDocumentRecusado(documentsTechCertVO, flowActionsTechCertVO) || flowActionsTechCertVO.getSignRuleUserTechCertVOAtual().getHasRefused()) {
                Integer codigoPessoaDocumentoAssinadoPessoa = getFacadeFactory().getDocumentoAssinadoPessoaFacade()
                        .atualizarDadosAssinaturaPorProvedorTechCert(documentsTechCertVO.getId(), name,
                                identifier, SituacaoDocumentoAssinadoPessoaEnum.REJEITADO,
                                Uteis.isAtributoPreenchido(documentsTechCertVO.getStatusUpdateReason()) ? documentsTechCertVO.getStatusUpdateReason() : "Documento rejeitado",
                                dataAssinaturaOrRejeicao, documentoAssinadoOrigemEnum, jsonAssinatura, "");

                if (doc.getTipoOrigemDocumentoAssinadoEnum().isTermoEstagioObrigatorio()) {
                    EstagioVO estagio = getFacadeFactory().getEstagioFacade().consultarPorDocumentoAssinador(doc.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSDOCUMENTOASSINADO, usuarioVO);
                    String reason = Uteis.isAtributoPreenchido(documentsTechCertVO.getStatusUpdateReason()) ? documentsTechCertVO.getStatusUpdateReason() : "Documento rejeitado";
                    estagio.setMotivo("Cancelamento automático por Termo de Assinatura rejeitado pelo assinante: " + name + System.lineSeparator() + " mensagem: " + reason);
                    getFacadeFactory().getEstagioFacade().realizarConfirmacaoEstagioIndeferido(estagio, SituacaoAdicionalEstagioEnum.INDEFERIDO_FACILITADOR, true, usuarioVO);
                }
                if (doc.getTipoOrigemDocumentoAssinadoEnum().isAtaColacaoGrau()) {
                    getFacadeFactory().getProgramacaoFormaturaAlunoFacade().realizarAlteracaoColouGrau(doc, codigoPessoaDocumentoAssinadoPessoa, SituacaoDocumentoAssinadoPessoaEnum.REJEITADO, usuarioVO);
                }
            }

            if (isDocumentCancelado(documentsTechCertVO, flowActionsTechCertVO)) {
                Integer codigoPessoaDocumentoAssinadoPessoa = getFacadeFactory().getDocumentoAssinadoPessoaFacade()
                        .atualizarDadosAssinaturaPorProvedorTechCert(documentsTechCertVO.getId(), name,
                                identifier, SituacaoDocumentoAssinadoPessoaEnum.REJEITADO,
                                Uteis.isAtributoPreenchido(documentsTechCertVO.getStatusUpdateReason()) ? documentsTechCertVO.getStatusUpdateReason() : "Documento Cancelado na TECHCERT",
                                dataAssinaturaOrRejeicao, documentoAssinadoOrigemEnum, jsonAssinatura, "");
                if (doc.getTipoOrigemDocumentoAssinadoEnum().isTermoEstagioObrigatorio()) {
                    EstagioVO estagio = getFacadeFactory().getEstagioFacade().consultarPorDocumentoAssinador(doc.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSDOCUMENTOASSINADO, usuarioVO);
                    String reason = Uteis.isAtributoPreenchido(documentsTechCertVO.getStatusUpdateReason()) ? documentsTechCertVO.getStatusUpdateReason() : "Documento Cancelado na TECHCERT";
                    estagio.setMotivo("Cancelamento automático por Termo de Assinatura rejeitado pelo assinante: " + name + System.lineSeparator() + " mensagem: " + reason);
                    getFacadeFactory().getEstagioFacade().realizarConfirmacaoEstagioIndeferido(estagio, SituacaoAdicionalEstagioEnum.INDEFERIDO_FACILITADOR, true, usuarioVO);
                }
                if (doc.getTipoOrigemDocumentoAssinadoEnum().isAtaColacaoGrau()) {
                    getFacadeFactory().getProgramacaoFormaturaAlunoFacade().realizarAlteracaoColouGrau(doc, codigoPessoaDocumentoAssinadoPessoa, SituacaoDocumentoAssinadoPessoaEnum.REJEITADO, usuarioVO);
                }
            }

            if (isDocumentExpirado(documentsTechCertVO, flowActionsTechCertVO)) {
                Integer codigoPessoaDocumentoAssinadoPessoa = getFacadeFactory().getDocumentoAssinadoPessoaFacade()
                        .atualizarDadosAssinaturaPorProvedorTechCert(documentsTechCertVO.getId(), name,
                                identifier, SituacaoDocumentoAssinadoPessoaEnum.REJEITADO,
                                "Periodo de Assinatura Expirado",
                                dataAssinaturaOrRejeicao, documentoAssinadoOrigemEnum, jsonAssinatura, "");
                if (doc.getTipoOrigemDocumentoAssinadoEnum().isTermoEstagioObrigatorio()) {
                    EstagioVO estagio = getFacadeFactory().getEstagioFacade().consultarPorDocumentoAssinador(doc.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSDOCUMENTOASSINADO, usuarioVO);
                    String reason = "Periodo de Assinatura Expirado";
                    estagio.setMotivo("Cancelamento automático por Termo de Assinatura rejeitado pelo assinante: " + name + System.lineSeparator() + " mensagem: " + reason);
                    getFacadeFactory().getEstagioFacade().realizarConfirmacaoEstagioIndeferido(estagio, SituacaoAdicionalEstagioEnum.INDEFERIDO_FACILITADOR, true, usuarioVO);
                }
                if (doc.getTipoOrigemDocumentoAssinadoEnum().isAtaColacaoGrau()) {
                    getFacadeFactory().getProgramacaoFormaturaAlunoFacade().realizarAlteracaoColouGrau(doc, codigoPessoaDocumentoAssinadoPessoa, SituacaoDocumentoAssinadoPessoaEnum.REJEITADO, usuarioVO);
                }
            }
        }
        return doc;
    }

    private Boolean isSignRuleCompleted(FlowActionsTechCertVO flowActionsTechCertVO) {
        return flowActionsTechCertVO.getSignRuleUserTechCertVOAtual().getSignatureDate() != null
                && !flowActionsTechCertVO.getSignRuleUserTechCertVOAtual().getHasRefused();
    }

    private Boolean isDocumentAssinadoOrConcluido(FlowActionsTechCertVO flowActionsTechCertVO) {
        return StatusFlowActionsTechCertEnum.COMPLETED.getNome().equalsIgnoreCase(flowActionsTechCertVO.getStatus())
                || StatusDocumentActionTechCertEnum.CONCLUDED.getNome().equalsIgnoreCase(flowActionsTechCertVO.getStatus())
                || StatusDocumentActionTechCertEnum.FLOW_CONCLUDED.getNome().equalsIgnoreCase(flowActionsTechCertVO.getStatus());
    }

    private Boolean isGerarUrlFlowAction(DocumentsTechCertVO documentsTechCertVO, FlowActionsTechCertVO flowActionsTechCertVO) {
        return !StatusFlowActionsTechCertEnum.COMPLETED.getNome().equalsIgnoreCase(flowActionsTechCertVO.getStatus())
                && !StatusFlowActionsTechCertEnum.REFUSED.getNome().equalsIgnoreCase(flowActionsTechCertVO.getStatus())
                && !StatusDocumentActionTechCertEnum.CONCLUDED.getNome().equals(documentsTechCertVO.getStatus())
                && !StatusDocumentActionTechCertEnum.REFUSED.getNome().equals(documentsTechCertVO.getStatus())
                && !StatusDocumentActionTechCertEnum.EXPIRED.getNome().equals(documentsTechCertVO.getStatus())
                && !StatusDocumentActionTechCertEnum.CANCELED.getNome().equals(documentsTechCertVO.getStatus());
    }

    private Boolean isDocumentRecusado(DocumentsTechCertVO documentsTechCertVO, FlowActionsTechCertVO flowActionsTechCertVO){
        return StatusDocumentActionTechCertEnum.REFUSED.getNome().equalsIgnoreCase(documentsTechCertVO.getStatus())
                && !StatusFlowActionsTechCertEnum.COMPLETED.getNome().equalsIgnoreCase(flowActionsTechCertVO.getStatus())
                || StatusFlowActionsTechCertEnum.REFUSED.getNome().equalsIgnoreCase(flowActionsTechCertVO.getStatus());
    }

    private Boolean isDocumentCancelado(DocumentsTechCertVO documentsTechCertVO, FlowActionsTechCertVO flowActionsTechCertVO) {
        return StatusDocumentActionTechCertEnum.CANCELED.getNome().equalsIgnoreCase(documentsTechCertVO.getStatus())
                && !StatusFlowActionsTechCertEnum.COMPLETED.getNome().equalsIgnoreCase(flowActionsTechCertVO.getStatus());
    }

    private Boolean isDocumentExpirado(DocumentsTechCertVO documentsTechCertVO, FlowActionsTechCertVO flowActionsTechCertVO) {
        return StatusDocumentActionTechCertEnum.EXPIRED.getNome().equalsIgnoreCase(documentsTechCertVO.getStatus())
                && !StatusFlowActionsTechCertEnum.COMPLETED.getNome().equalsIgnoreCase(flowActionsTechCertVO.getStatus());
    }

    private Boolean isValidoAssinante(DocumentKeyValidationTechCertVO documentKeyValidationTechCertVO){
        return documentKeyValidationTechCertVO.getId() != null
                && !documentKeyValidationTechCertVO.getSigners().isEmpty()
                && !documentKeyValidationTechCertVO.isDocumentHasNoSignatures()
                && !documentKeyValidationTechCertVO.getDocumentInvalid();
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public String executarAssinaturaProvedorCertiSign(DocumentoAssinadoVO obj, String arquivoOrigem, ConfiguracaoGEDVO configGEDVO, ConfiguracaoGeralSistemaVO config, UsuarioVO usuarioVO) throws Exception {
        Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getListaDocumentoAssinadoPessoa()), "Não foi encontrado nenhum pessoa para assinar o documentos!");
        if (obj.getImpressaoContratoMatriculaExterna()) {
            configurarUnirestImpressaoContratoMatriculaExterna();
        }
        String idUpoad = realizarEnvioUpload(obj, configGEDVO, arquivoOrigem);
        CertiSignRSVO csRSVO = new CertiSignRSVO();
        csRSVO.setDocument(new DocumentRSVO());
        csRSVO.getDocument().setName(obj.getArquivo().getNome());
        csRSVO.getDocument().setUpload(new UploadRSVO());
        csRSVO.getDocument().getUpload().setId(idUpoad);
        csRSVO.getDocument().getUpload().setName(obj.getArquivo().getNome());

        List<DocumentPessoaRSVO> listaElectronicSigners = new ArrayList<>();
        List<DocumentPessoaRSVO> listaSigners = new ArrayList<>();
        for (DocumentoAssinadoPessoaVO dap : obj.getListaDocumentoAssinadoPessoa()) {
            DocumentPessoaRSVO dpRSVO = new DocumentPessoaRSVO();
            if (obj.getDocumentoContrato()) {
                dpRSVO.setName(dap.getPessoaVO().getNome());
                dpRSVO.setEmail(dap.getPessoaVO().getEmail());
                if (Uteis.isAtributoPreenchido(dap.getPessoaVO())) {
                    if (dap.getTipoPessoa().isFuncionario()) {
                        PessoaEmailInstitucionalVO pei = null;
                        if (!Uteis.isAtributoPreenchido(dap.getPessoaVO().getListaPessoaEmailInstitucionalVO())) {
                            pei = getFacadeFactory().getPessoaEmailInstitucionalFacade().consultarPorPessoa(dap.getPessoaVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO);
                        }
                        if (Uteis.isAtributoPreenchido(pei)) {
                            dpRSVO.setEmail(pei.getEmail());
                        }
                    }
                    if (Uteis.isAtributoPreenchido(dap.getPessoaVO().getCPF())) {
                        Uteis.checkState(!Uteis.validaCPF(Uteis.removerAcentos(Uteis.removeCaractersEspeciais(dap.getPessoaVO().getCPF())).replaceAll(" ", "")), "O campo cpf para o assinante - " + dap.getPessoaVO().getNome() + " não é válido");
                        dpRSVO.setIndividualIdentificationCode(dap.getPessoaVO().getCPF());
                    }
                }
            } else if (Uteis.isAtributoPreenchido(dap.getPessoaVO())) {
                PessoaEmailInstitucionalVO pei = null;
                if (!Uteis.isAtributoPreenchido(dap.getPessoaVO().getListaPessoaEmailInstitucionalVO())) {
                    pei = getFacadeFactory().getPessoaEmailInstitucionalFacade().consultarPorPessoa(dap.getPessoaVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO);
                }
                if (Uteis.isAtributoPreenchido(pei)) {
                    dpRSVO.setEmail(pei.getEmail());
                } else {
                    dpRSVO.setEmail(dap.getPessoaVO().getEmail());
                }
                dpRSVO.setName(dap.getPessoaVO().getNome());
                Uteis.checkState(!Uteis.isAtributoPreenchido(dpRSVO.getEmail()), "O campo e-mail/e-mail institucional pessoa deve ser informado para o assinante - " + dap.getPessoaVO().getNome());
                Uteis.checkState(!Uteis.validaCPF(Uteis.removerAcentos(Uteis.removeCaractersEspeciais(dap.getPessoaVO().getCPF())).replaceAll(" ", "")), "O campo cpf para o assinante - " + dap.getPessoaVO().getNome() + " não é válido");
                dpRSVO.setIndividualIdentificationCode(dap.getPessoaVO().getCPF());
            } else {
                dpRSVO.setName(dap.getNomePessoa());
                dpRSVO.setEmail(dap.getEmailPessoa());
                Uteis.checkState(!Uteis.validaCPF(Uteis.removerAcentos(Uteis.removeCaractersEspeciais(dap.getCpfPessoa())).replaceAll(" ", "")), "O campo cpf para o assinante - " + dap.getNomePessoa() + " não é válido");
                dpRSVO.setIndividualIdentificationCode(dap.getCpfPessoa());
            }
            dpRSVO.setStep(dap.getOrdemAssinatura());
            dpRSVO.setTitle("Assinatura");
            if (Uteis.isAtributoPreenchido(obj.getProgramacaoFormaturaVO().getDataLimiteAssinaturaAta())) {
                DeadlineRSVO deadlineRSVO = new DeadlineRSVO();
                deadlineRSVO.setSignatureDeadlineDate(Uteis.getDataAno4Digitos(obj.getProgramacaoFormaturaVO().getDataLimiteAssinaturaAta()));
                deadlineRSVO.setTime(LocalDateTime.now().toString());
                deadlineRSVO.setTimeType(TimeType.PERSONALIZED_TIME.getValor());
                deadlineRSVO.setTimezoneId(ZoneId.systemDefault().getId());
                dpRSVO.setBlockDocumentAfterLimit(true);
                dpRSVO.setDeadline(deadlineRSVO);
            }

            if (dap.getTipoAssinaturaDocumentoEnum().isEletronica() || dap.getTipoPessoa().isAluno() || dap.getTipoPessoa().isMembroComunidade() || !Uteis.isAtributoPreenchido(dap.getPessoaVO())) {
                listaElectronicSigners.add(dpRSVO);
            } else if (dap.getTipoAssinaturaDocumentoEnum().isCertificadoDigital() || (Uteis.isAtributoPreenchido(dap.getPessoaVO()) && (dap.getTipoPessoa().isFuncionario() || dap.getTipoPessoa().isProfessor()))) {
                if (!obj.getDocumentoContrato() || (obj.getDocumentoContrato() && Uteis.isAtributoPreenchido(dpRSVO.getEmail()))) {
                    listaSigners.add(dpRSVO);
                }
            }
        }
        Map<String, List<DocumentPessoaRSVO>> mapaElectronicSigners = listaElectronicSigners.stream().collect(Collectors.groupingBy(DocumentPessoaRSVO::getEmail));
        for (Map.Entry<String, List<DocumentPessoaRSVO>> map : mapaElectronicSigners.entrySet()) {
            Uteis.checkState(map.getValue().size() > 1, "Existe mais de uma assinatura para o mesmo Email " + map.getKey() + ". Por favor verificar a lista de assinantes.");
        }
        Map<String, List<DocumentPessoaRSVO>> mapaSigners = listaSigners.stream().collect(Collectors.groupingBy(DocumentPessoaRSVO::getEmail));
        for (Map.Entry<String, List<DocumentPessoaRSVO>> map : mapaSigners.entrySet()) {
            Uteis.checkState(map.getValue().size() > 1, "Existe mais de uma assinatura para o mesmo Email " + map.getKey() + ". Por favor verificar a lista de assinantes.");
        }
        validarUnicidadeCPFRegistroCertisign(listaElectronicSigners);
        validarUnicidadeCPFRegistroCertisign(listaSigners);
        if (Uteis.isAtributoPreenchido(listaSigners)) {
            csRSVO.setSigners(listaSigners);
        }
        if (Uteis.isAtributoPreenchido(listaElectronicSigners)) {
            csRSVO.setElectronicSigners(listaElectronicSigners);
        }
        CertiSignRSVO csRetorno = realizarEnvioCreateDocument(csRSVO, obj.getProvedorDeAssinaturaEnum(), configGEDVO);
        obj.setCodigoProvedorDeAssinatura(numeroParaStringOuNull(csRetorno.getId()));
        obj.setChaveProvedorDeAssinatura(csRetorno.getChave());
        obj.setUrlProvedorDeAssinatura(csRetorno.getSignUrl());
        for (DocumentPessoaRSVO docPessoaRsVO : csRetorno.getAttendees()) {
            obj.getListaDocumentoAssinadoPessoa().stream()
                    .filter(dap -> dap.getPessoaVO().getNome().equals(docPessoaRsVO.getName()) || dap.getNomePessoa().equals(docPessoaRsVO.getName()))
                    .forEach(dap -> {
                        dap.setAcaoAssinatura(docPessoaRsVO.getAction());
                        dap.setCodigoAssinatura(docPessoaRsVO.getSignerId().toString());
                        dap.setUrlAssinatura(docPessoaRsVO.getSignUrl());
                    });
        }
        alterar(obj, false, usuarioVO, config);
        resetUnirest();
        return arquivoOrigem;
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public String executarAssinaturaProvedorTechCert(DocumentoAssinadoVO documentoAssinadoVO, String arquivoOrigem,
                                                     ConfiguracaoGEDVO configGEDVO, ConfiguracaoGeralSistemaVO config, UsuarioVO usuarioVO) throws Exception {
        Uteis.checkState(!Uteis.isAtributoPreenchido(documentoAssinadoVO.getListaDocumentoAssinadoPessoa()), "Não foi encontrado nenhum pessoa para assinar o documentos!");
        if (!configGEDVO.getHabilitarIntegracaoTechCert()){
            throw new StreamSeiException("Configuração de integração com TechCert não está habilitada.");
        }
        List<DocumentPessoaRSVO> listaElectronicSigners = new ArrayList<>();
        List<DocumentPessoaRSVO> listaSigners = new ArrayList<>();
        IntegracaoTechCertVO integracaoTechCertVO = new IntegracaoTechCertVO();
        for (DocumentoAssinadoPessoaVO dap : documentoAssinadoVO.getListaDocumentoAssinadoPessoa()) {
            DocumentPessoaRSVO dpRSVO = new DocumentPessoaRSVO();
            dpRSVO.setTipoPessoa(dap.getTipoPessoa());
            if (documentoAssinadoVO.getDocumentoContrato()) {
                dpRSVO.setName(dap.getPessoaVO().getNome());
                dpRSVO.setEmail(dap.getPessoaVO().getEmail());
                dpRSVO.setIdentifier(Uteis.removerMascara(dap.getPessoaVO().getCPF()));
                if (Uteis.isAtributoPreenchido(dap.getPessoaVO())) {
                    if (dap.getTipoPessoa().isFuncionario()) {
                        PessoaEmailInstitucionalVO pei = null;
                        if (!Uteis.isAtributoPreenchido(dap.getPessoaVO().getListaPessoaEmailInstitucionalVO())) {
                            pei = getFacadeFactory().getPessoaEmailInstitucionalFacade().consultarPorPessoa(dap.getPessoaVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO);
                        }
                        if (Uteis.isAtributoPreenchido(pei)) {
                            dpRSVO.setEmail(pei.getEmail());
                        }
                    }
                    if (Uteis.isAtributoPreenchido(dap.getPessoaVO().getCPF())) {
                        Uteis.checkState(!Uteis.validaCPF(Uteis.removerAcentos(Uteis.removeCaractersEspeciais(dap.getPessoaVO().getCPF())).replaceAll(" ", "")), "O campo cpf para o assinante - " + dap.getPessoaVO().getNome() + " não é válido");
                        dpRSVO.setIndividualIdentificationCode(dap.getPessoaVO().getCPF());
                    }
                }
            } else if (Uteis.isAtributoPreenchido(dap.getPessoaVO())) {
                PessoaEmailInstitucionalVO pei = null;
                if (!Uteis.isAtributoPreenchido(dap.getPessoaVO().getListaPessoaEmailInstitucionalVO())) {
                    pei = getFacadeFactory().getPessoaEmailInstitucionalFacade().consultarPorPessoa(dap.getPessoaVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO);
                }
                if (Uteis.isAtributoPreenchido(pei)) {
                    dpRSVO.setEmail(pei.getEmail());
                } else {
                    dpRSVO.setEmail(dap.getPessoaVO().getEmail());
                }
                dpRSVO.setName(dap.getPessoaVO().getNome());
                Uteis.checkState(!Uteis.isAtributoPreenchido(dpRSVO.getEmail()), "O campo e-mail/e-mail institucional pessoa deve ser informado para o assinante - " + dap.getPessoaVO().getNome());
                Uteis.checkState(!Uteis.validaCPF(Uteis.removerAcentos(Uteis.removeCaractersEspeciais(dap.getPessoaVO().getCPF())).replaceAll(" ", "")), "O campo cpf para o assinante - " + dap.getPessoaVO().getNome() + " não é válido");
                dpRSVO.setIndividualIdentificationCode(dap.getPessoaVO().getCPF());
            } else {
                dpRSVO.setName(dap.getNomePessoa());
                dpRSVO.setEmail(dap.getEmailPessoa());
                Uteis.checkState(!Uteis.validaCPF(Uteis.removerAcentos(Uteis.removeCaractersEspeciais(dap.getCpfPessoa())).replaceAll(" ", "")), "O campo cpf para o assinante - " + dap.getNomePessoa() + " não é válido");
                dpRSVO.setIndividualIdentificationCode(dap.getCpfPessoa());
            }
            dpRSVO.setStep(dap.getOrdemAssinatura());
            dpRSVO.setTitle("Assinatura");
            if (Uteis.isAtributoPreenchido(documentoAssinadoVO.getProgramacaoFormaturaVO().getDataLimiteAssinaturaAta())) {
                DeadlineRSVO deadlineRSVO = new DeadlineRSVO();
                deadlineRSVO.setSignatureDeadlineDate(Uteis.getDataAno4Digitos(documentoAssinadoVO.getProgramacaoFormaturaVO().getDataLimiteAssinaturaAta()));
                deadlineRSVO.setTime(LocalDateTime.ofInstant(documentoAssinadoVO.getProgramacaoFormaturaVO().getDataLimiteAssinaturaAta().toInstant(), ZoneId.systemDefault()).toString());
                deadlineRSVO.setTimeType(TimeType.PERSONALIZED_TIME.getValor());
                deadlineRSVO.setTimezoneId(ZoneId.systemDefault().getId());
                dpRSVO.setBlockDocumentAfterLimit(true);
                dpRSVO.setDeadline(deadlineRSVO);
            }

            if (dap.getTipoAssinaturaDocumentoEnum().isEletronica() || dap.getTipoPessoa().isAluno() || dap.getTipoPessoa().isMembroComunidade() || !Uteis.isAtributoPreenchido(dap.getPessoaVO())) {
                listaElectronicSigners.add(dpRSVO);
            } else if (dap.getTipoAssinaturaDocumentoEnum().isCertificadoDigital() || (Uteis.isAtributoPreenchido(dap.getPessoaVO()) && (dap.getTipoPessoa().isFuncionario() || dap.getTipoPessoa().isProfessor()))) {
                if (!documentoAssinadoVO.getDocumentoContrato() || (documentoAssinadoVO.getDocumentoContrato() && Uteis.isAtributoPreenchido(dpRSVO.getEmail()))) {
                    listaSigners.add(dpRSVO);
                }
            }
        }
        Map<String, List<DocumentPessoaRSVO>> mapaElectronicSigners = listaElectronicSigners.stream().collect(Collectors.groupingBy(DocumentPessoaRSVO::getEmail));
        for (Map.Entry<String, List<DocumentPessoaRSVO>> map : mapaElectronicSigners.entrySet()) {
            Uteis.checkState(map.getValue().size() > 1, "Existe mais de uma assinatura para o mesmo Email " + map.getKey() + ". Por favor verificar a lista de assinantes.");
        }
        Map<String, List<DocumentPessoaRSVO>> mapaSigners = listaSigners.stream().collect(Collectors.groupingBy(DocumentPessoaRSVO::getEmail));
        for (Map.Entry<String, List<DocumentPessoaRSVO>> map : mapaSigners.entrySet()) {
            Uteis.checkState(map.getValue().size() > 1, "Existe mais de uma assinatura para o mesmo Email " + map.getKey() + ". Por favor verificar a lista de assinantes.");
        }
        validarUnicidadeCPFRegistroTechCert(listaElectronicSigners);
        validarUnicidadeCPFRegistroTechCert(listaSigners);
        if (Uteis.isAtributoPreenchido(listaSigners)) {
            integracaoTechCertVO.setSigners(listaSigners);
        }
        if (Uteis.isAtributoPreenchido(listaElectronicSigners)) {
            integracaoTechCertVO.setElectronicSigners(listaElectronicSigners);
        }
        integrarDocumentoTechCert(integracaoTechCertVO, arquivoOrigem, documentoAssinadoVO, config, configGEDVO, usuarioVO);
        alterar(documentoAssinadoVO, false, usuarioVO, config);
        resetUnirest();
        return arquivoOrigem;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public void realizarVisualizacaoArquivoProvedorCertisign(DocumentoAssinadoVO doc, ConfiguracaoGeralSistemaVO configGeral, boolean includeOriginal, boolean includeManifest, boolean zipado, UsuarioVO usuario) throws Exception {
        ConfiguracaoGEDVO configGedVO = getFacadeFactory().getConfiguracaoGEDFacade().consultarPorUnidadeEnsino(doc.getUnidadeEnsinoVO().getCodigo(), false, usuario);
        Uteis.checkState(!Uteis.isAtributoPreenchido(configGedVO), "Não foi encontrado nenhuma configuração ged contendo o provedor de assinatura para a unidade ensino informado no documento assinado.");
        String diretorio = configGeral.getLocalUploadArquivoFixo() + File.separator + doc.getArquivo().getPastaBaseArquivo().replace("/", File.separator);
        File file = new File(diretorio);
        if (!file.exists()) {
            file.mkdirs();
        }
        file = new File(diretorio + File.separator + doc.getArquivo().getNome());
        if (file.exists()) {
            file.delete();
        }
        if (!file.exists()) {
            UploadRSVO upload = realizarConsultarPackageDocument(doc, configGedVO, includeOriginal, includeManifest, zipado);
            file.createNewFile();
            Files.write(Paths.get(file.getPath()), Base64.getDecoder().decode(upload.getBytes()));
            doc.getArquivo().setExtensao(upload.getName().substring((upload.getName().lastIndexOf(".") + 1), upload.getName().length()));
        }
        getFacadeFactory().getArquivoHelper().disponibilizarArquivoAssinadoParaDowload(file.getAbsolutePath(), doc.getArquivo().getNome());
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public void realizarVisualizacaoArquivoProvedorTechCert(DocumentoAssinadoVO doc, ConfiguracaoGeralSistemaVO configGeral, UsuarioVO usuario) throws Exception {
        ConfiguracaoGEDVO configGedVO = getFacadeFactory().getConfiguracaoGEDFacade().consultarPorUnidadeEnsino(doc.getUnidadeEnsinoVO().getCodigo(), false, usuario);
        Uteis.checkState(!Uteis.isAtributoPreenchido(configGedVO), "Não foi encontrado nenhuma configuração ged contendo o provedor de assinatura para a unidade ensino informado no documento assinado.");
        String diretorio = configGeral.getLocalUploadArquivoFixo() + File.separator + doc.getArquivo().getPastaBaseArquivo().replace("/", File.separator);
        File file = new File(diretorio);
        if (!file.exists()) {
            file.mkdirs();
        }
        file = new File(diretorio + File.separator + doc.getArquivo().getNome());
        if (file.exists()) {
            file.delete();
        }
        if (!file.exists()) {
            UploadRSVO upload = consultarDocumentDownloadSignatures(configGedVO, doc.getChaveProvedorDeAssinatura());
            file.createNewFile();
            Files.write(Paths.get(file.getPath()), Base64.getDecoder().decode(upload.getBytes()));
            doc.getArquivo().setExtensao(upload.getName().substring((upload.getName().lastIndexOf(".") + 1), upload.getName().length()));
        }
        getFacadeFactory().getArquivoHelper().disponibilizarArquivoAssinadoParaDowload(file.getAbsolutePath(), doc.getArquivo().getNome());
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
    public String realizarInclusaoDocumentoAssinadoPorPlanoDeEnsino(String nomeArquivoOrigem, PlanoEnsinoVO planoEnsinoVO, String ano, String semestre, ConfiguracaoGeralSistemaVO config, UsuarioVO usuarioVO) throws Exception {
        UnidadeEnsinoVO unidadeEnsinoMatriz = getFacadeFactory().getUnidadeEnsinoFacade().obterUnidadeMatriz(false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, usuarioVO);
        DocumentoAssinadoVO obj = new DocumentoAssinadoVO();
        obj.setDataRegistro(new Date());
        obj.setUsuario(usuarioVO);
        obj.setTipoOrigemDocumentoAssinadoEnum(TipoOrigemDocumentoAssinadoEnum.PLANO_DE_ENSINO);
        obj.setGradecurricular(planoEnsinoVO.getGradeCurricular());
        obj.setPlanoEnsinoVO(planoEnsinoVO);
        obj.setUnidadeEnsinoVO(unidadeEnsinoMatriz);
        obj.setDisciplina(planoEnsinoVO.getDisciplina());
        obj.setAno(ano);
        obj.setSemestre(semestre);
        obj.getArquivo().setNome(usuarioVO.getCodigo() + Constantes.UNDERSCORE + new Date().getTime() + Constantes.PONTO + Constantes.EXTENSAO_PDF);
        obj.getArquivo().setDescricao(TipoOrigemDocumentoAssinadoEnum.PLANO_DE_ENSINO.getDescricao() + Constantes.UNDERSCORE + new Date().getTime());
        obj.getArquivo().setExtensao(Constantes.EXTENSAO_PDF);
        obj.getArquivo().setResponsavelUpload(usuarioVO);
        obj.getArquivo().setDataUpload(new Date());
        obj.getArquivo().setDataDisponibilizacao(new Date());
        obj.getArquivo().setManterDisponibilizacao(true);
        obj.getArquivo().setControlarDownload(true);
        obj.getArquivo().setDataIndisponibilizacao(null);
        obj.getArquivo().setSituacao(SituacaoArquivo.ATIVO.getValor());
        obj.getArquivo().setPastaBaseArquivo(PastaBaseArquivoEnum.DOCUMENTOS_ASSINADOS_TMP.name());
        obj.getArquivo().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.DOCUMENTOS_ASSINADOS_TMP);
        obj.getArquivo().setDisciplina(planoEnsinoVO.getDisciplina());
        obj.getArquivo().setArquivoAssinadoDigitalmente(true);

        if (Uteis.isAtributoPreenchido(planoEnsinoVO.getResponsavelAutorizacao().getCodigo()) && Uteis.isAtributoPreenchido(planoEnsinoVO.getResponsavelAutorizacao().getPessoa().getCodigo())) {
            obj.getListaDocumentoAssinadoPessoa().add(new DocumentoAssinadoPessoaVO(new Date(), planoEnsinoVO.getResponsavelAutorizacao().getPessoa(), TipoPessoa.FUNCIONARIO, SituacaoDocumentoAssinadoPessoaEnum.PENDENTE, obj, 1, null, null, null, null));
        }


        incluir(obj, false, usuarioVO, config);
        ConfiguracaoGEDVO configGEDVO = getAplicacaoControle().getConfiguracaoGEDPorUnidadeEnsino(planoEnsinoVO.getUnidadeEnsino().getCodigo(), usuarioVO);
        Uteis.checkState(!(Uteis.isAtributoPreenchido(configGEDVO) && configGEDVO.getConfiguracaoGedOrigemVO(obj.getTipoOrigemDocumentoAssinadoEnum()) != null && configGEDVO.getConfiguracaoGedOrigemVO(obj.getTipoOrigemDocumentoAssinadoEnum()).getAssinarDocumento()), "Não foi encontrado uma CONFIGURAÇÃO GED habilitada para assinar digitalmente um PLANO DE ENSINO.");

        String arquivoCriado = realizarVerificacaoProvedorDeAssinatura(obj, planoEnsinoVO.getUnidadeEnsino().getCodigo(), true, true, nomeArquivoOrigem, AlinhamentoAssinaturaDigitalEnum.RODAPE_DIREITA, "#000000", 80f, 200f, 6f, 12, 20, 5, 2, config, true, usuarioVO, true);

        if (configGEDVO.getProvedorDeAssinaturaEnum().isProvedorSei()) {
            for (DocumentoAssinadoPessoaVO dap : obj.getListaDocumentoAssinadoPessoa()) {
                try {
                    if (dap.getSituacaoDocumentoAssinadoPessoaEnum().equals(SituacaoDocumentoAssinadoPessoaEnum.PENDENTE)) {
                        ArquivoVO certificadoParaDocumento = validarCertificadoParaDocumento(obj.getUnidadeEnsinoVO(), null, config, usuarioVO, dap.getPessoaVO());
                        dap.setSituacaoDocumentoAssinadoPessoaEnum(SituacaoDocumentoAssinadoPessoaEnum.ASSINADO);
                        dap.setDataAssinatura(new Date());
                        getFacadeFactory().getDocumentoAssinadoPessoaFacade().atualizarDadosAssinatura(dap, null, null, null);
                        preencherAssinadorDigitalDocumentoPorPessoa(config.getLocalUploadArquivoFixo() + File.separator + obj.getArquivo().getPastaBaseArquivo() + File.separator + obj.getArquivo().getNome(), obj, dap, certificadoParaDocumento, dap.getPessoaVO().getSenhaCertificadoParaDocumento(), config, configGEDVO, true);
                        arquivoCriado = getFacadeFactory().getArquivoHelper().disponibilizarArquivoAssinadoParaDowload(config.getLocalUploadArquivoFixo() + File.separator + obj.getArquivo().getPastaBaseArquivo() + File.separator + obj.getArquivo().getNome(), obj.getArquivo().getNome());
                    } else {
                        dap.setSituacaoDocumentoAssinadoPessoaEnum(SituacaoDocumentoAssinadoPessoaEnum.PENDENTE);
                        dap.setDataAssinatura(null);
                    }
                } catch (Exception e) {
                    dap.setSituacaoDocumentoAssinadoPessoaEnum(SituacaoDocumentoAssinadoPessoaEnum.PENDENTE);
                    dap.setDataAssinatura(null);
                    throw e;
                }
            }

        }
        return arquivoCriado;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public void realizarDownloadArquivoProvedorCertisign(DocumentoAssinadoVO doc, ConfiguracaoGeralSistemaVO configGeral, UsuarioVO usuario) throws Exception {
        ConfiguracaoGEDVO configGedVO = getFacadeFactory().getConfiguracaoGEDFacade().consultarPorUnidadeEnsino(doc.getUnidadeEnsinoVO().getCodigo(), false, usuario);
        Uteis.checkState(!Uteis.isAtributoPreenchido(configGedVO), "Não foi encontrado nenhuma configuração ged contendo o provedor de assinatura para a unidade ensino informado no documento assinado.");
        UploadRSVO upload = realizarConsultarPackageDocument(doc, configGedVO, false, true, false);
        File file = new File(configGeral.getLocalUploadArquivoFixo() + File.separator + doc.getArquivo().getPastaBaseArquivo() + File.separator + upload.getName());
        if (file.exists()) {
            file.delete();
        }
        if (!file.exists()) {
            file.createNewFile();
        }
        Files.write(Paths.get(file.getPath()), Base64.getDecoder().decode(upload.getBytes()));
        doc.getArquivo().setNome(upload.getName());
        doc.getArquivo().setExtensao(upload.getName().substring((upload.getName().lastIndexOf(".") + 1), upload.getName().length()));
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public void realizarDownloadArquivoProvedorTechCert(DocumentoAssinadoVO doc, ConfiguracaoGeralSistemaVO configGeral, UsuarioVO usuario) throws Exception {
        ConfiguracaoGEDVO configGedVO = getFacadeFactory().getConfiguracaoGEDFacade().consultarPorUnidadeEnsino(doc.getUnidadeEnsinoVO().getCodigo(), false, usuario);
        Uteis.checkState(!Uteis.isAtributoPreenchido(configGedVO), "Não foi encontrado nenhuma configuração ged contendo o provedor de assinatura para a unidade ensino informado no documento assinado.");
        UploadRSVO upload = consultarDocumentDownloadSignatures(configGedVO, doc.getChaveProvedorDeAssinatura());
        File file = new File(configGeral.getLocalUploadArquivoFixo() + File.separator + doc.getArquivo().getPastaBaseArquivo() + File.separator + upload.getName());
        if (file.exists()) {
            file.delete();
        }
        if (!file.exists()) {
            file.createNewFile();
        }
        Files.write(Paths.get(file.getPath()), Base64.getDecoder().decode(upload.getBytes()));
        doc.getArquivo().setNome(upload.getName());
        doc.getArquivo().setExtensao(upload.getName().substring((upload.getName().lastIndexOf(".") + 1), upload.getName().length()));
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
    public String realizarVerificacaoProvedorDeAssinatura(DocumentoAssinadoVO obj, Integer unidadeEnsino, boolean isAdicionarSelo, boolean isAdicionarQRCode, String nomeArquivoOrigem, AlinhamentoAssinaturaDigitalEnum alinhamentoAssinaturaDigital, String corAssinatura, Float alturaAssinatura, Float larguraAssinatura, float tamnhoFonte, int coordenadaLLX, int coordenadaLLY, int coordenadaURX, int coordenadaURY, ConfiguracaoGeralSistemaVO config, Boolean excluirArquivoOrigem, UsuarioVO usuarioVO, Boolean disponibilizarArquivoAssinadoParaDowload) throws Exception {
        String arquivoOrigem = nomeArquivoOrigem.contains(config.getLocalUploadArquivoFixo()) ? nomeArquivoOrigem : UteisJSF.getCaminhoWeb() + "relatorio/" + nomeArquivoOrigem;
        ConfiguracaoGEDVO configGEDVO = getAplicacaoControle().getConfiguracaoGEDPorUnidadeEnsino(unidadeEnsino, usuarioVO);
        if (Uteis.isAtributoPreenchido(configGEDVO)) {
            ConfiguracaoGedOrigemVO configuracaoGedOrigemVO = configGEDVO.getConfiguracaoGedOrigemVO(obj.getTipoOrigemDocumentoAssinadoEnum());
            if (configuracaoGedOrigemVO.getAssinarDocumento()) {
                return executarAssinaturaDocumentoPorConfiguracaoGed(obj, configGEDVO, configuracaoGedOrigemVO, isAdicionarSelo, isAdicionarQRCode, arquivoOrigem, corAssinatura, tamnhoFonte, coordenadaURX, coordenadaURY, excluirArquivoOrigem, disponibilizarArquivoAssinadoParaDowload, config, usuarioVO);
            }
            return executarAssinaturaParaDocumento(obj, config.getCertificadoParaDocumento(), config.getSenhaCertificadoParaDocumento(), arquivoOrigem, alinhamentoAssinaturaDigital, corAssinatura, alturaAssinatura, larguraAssinatura, tamnhoFonte, coordenadaLLX, coordenadaLLY, coordenadaURX, coordenadaURY, config, excluirArquivoOrigem, usuarioVO, disponibilizarArquivoAssinadoParaDowload);
        } else {
            return executarAssinaturaParaDocumento(obj, config.getCertificadoParaDocumento(), config.getSenhaCertificadoParaDocumento(), arquivoOrigem, alinhamentoAssinaturaDigital, corAssinatura, alturaAssinatura, larguraAssinatura, tamnhoFonte, coordenadaLLX, coordenadaLLY, coordenadaURX, coordenadaURY, config, excluirArquivoOrigem, usuarioVO, disponibilizarArquivoAssinadoParaDowload);
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
    private String executarAssinaturaDocumentoPorConfiguracaoGed(DocumentoAssinadoVO obj, ConfiguracaoGEDVO configuracaoGEDVO, ConfiguracaoGedOrigemVO configuracaoGedOrigemVO, boolean isAdicionarSelo, boolean isAdicionarQRCode, String arquivoOrigem, String corAssinatura, float tamanhoFonte, int coordenadaURX, int coordenadaURY, Boolean excluirArquivoOrigem, Boolean disponibilizarArquivoAssinadoParaDowload, ConfiguracaoGeralSistemaVO config, UsuarioVO usuarioVO) throws Exception {
        //if(configuracaoGEDVO.getProvedorDeAssinaturaEnum().isProvedorCertisign()) {
        ProvedorDeAssinaturaEnum provedorDeAssinaturaEnum = obj.getProvedorAssinaturaVisaoAdm() != null ? obj.getProvedorAssinaturaVisaoAdm() : configuracaoGedOrigemVO.getProvedorAssinaturaPadraoEnum();
        if (!obj.getTipoOrigemDocumentoAssinadoEnum().isXmlMec()
                && (provedorDeAssinaturaEnum.isProvedorCertisign() || provedorDeAssinaturaEnum.isProvedorImprensaOficial())) {
            obj.setProvedorDeAssinaturaEnum(provedorDeAssinaturaEnum);
            return executarAssinaturaProvedorCertiSign(obj, arquivoOrigem, configuracaoGEDVO, config, usuarioVO);
        }
        if (!obj.getTipoOrigemDocumentoAssinadoEnum().isXmlMec() && (provedorDeAssinaturaEnum.isProvedorTechCert())) {
            obj.setProvedorDeAssinaturaEnum(provedorDeAssinaturaEnum);
            return executarAssinaturaProvedorTechCert(obj, arquivoOrigem, configuracaoGEDVO, config, usuarioVO);
        }
        else {
            if (isAdicionarSelo) {
                adicionarSeloPDF(obj, arquivoOrigem, config, configuracaoGEDVO);
            }
            if (isAdicionarQRCode) {
                adicionarQRCodePDF(obj, arquivoOrigem, config, configuracaoGEDVO);
            }
            validarDadosCertificadoDigital(configuracaoGEDVO.getCertificadoDigitalUnidadeEnsinoVO(), configuracaoGEDVO.getSenhaCertificadoDigitalUnidadeEnsino());
            if (Uteis.isSistemaOperacionalWindows() && arquivoOrigem.startsWith("/")) {
                arquivoOrigem = arquivoOrigem.substring(1, arquivoOrigem.length());
            }
            
            if (obj.getTipoOrigemDocumentoAssinadoEnum().isGerarArquivoPdfA()) {
				getFacadeFactory().getArquivoHelper().realizarConversaoPdfPdfA(arquivoOrigem, config, obj.getArquivo(), false, false);
			}

            if (obj.getDocumentoContrato()) {
                File fileDiretorioCorreto = new File(arquivoOrigem);
                String caminhoDocumentoAssinadoOriginal = getFacadeFactory().getDocumentoAssinadoFacade().excutarVerificacaoPessoasParaAssinarContrato(obj, fileDiretorioCorreto, config, usuarioVO);
                excluirArquivoOrigem = Boolean.FALSE;
                arquivoOrigem = caminhoDocumentoAssinadoOriginal;
            }

			
			preencherAssinadorDigitalDocumentoPdf(arquivoOrigem, configuracaoGEDVO.getCertificadoDigitalUnidadeEnsinoVO(), configuracaoGEDVO.getSenhaCertificadoDigitalUnidadeEnsino(), obj, 
					null, corAssinatura, Float.valueOf(configuracaoGedOrigemVO.getAlturaAssinaturaUnidadeEnsino()), Float.valueOf(configuracaoGedOrigemVO.getLarguraAssinaturaUnidadeEnsino()), tamanhoFonte, 
					configuracaoGedOrigemVO.getPosicaoXAssinaturaUnidadeEnsino(), configuracaoGedOrigemVO.getPosicaoYAssinaturaUnidadeEnsino(), 
					coordenadaURX, coordenadaURY, configuracaoGedOrigemVO.isUltimaPaginaAssinaturaUnidadeEnsino(), configuracaoGedOrigemVO.getAssinaturaUnidadeEnsino(),  config, excluirArquivoOrigem,disponibilizarArquivoAssinadoParaDowload);
			
			if (obj.getTipoOrigemDocumentoAssinadoEnum().isGerarArquivoPdfA() && Uteis.isAtributoPreenchido(obj.getArquivo())) {
				getFacadeFactory().getArquivoFacade().atualizarArquivoIsPdfA(obj.getArquivo());
			}
			return obj.getArquivo().getNome();
		}
	}


	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void incluir(final DocumentoAssinadoVO obj, boolean verificarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		
		incluir(getIdEntidade(), verificarAcesso, usuario);
		if (!Uteis.isAtributoPreenchido(obj.getArquivo().getCodigo()) || obj.getArquivo().getCodigo().equals(0)) {
			obj.getArquivo().setOrigem(OrigemArquivo.DOCUMENTO_ASSINADO.getValor());
			getFacadeFactory().getArquivoFacade().incluir(obj.getArquivo(), usuario, configuracaoGeralSistemaVO);
		}
		
		if (obj.getArquivoVisual() != null && Uteis.isAtributoPreenchido(obj.getArquivoVisual().getNome())) {
			obj.getArquivoVisual().setOrigem(OrigemArquivo.DOCUMENTO_ASSINADO.getValor());
			getFacadeFactory().getArquivoFacade().incluir(obj.getArquivoVisual(), usuario, configuracaoGeralSistemaVO);
		}

		final String sql = "INSERT INTO DocumentoAssinado(arquivo, dataRegistro, usuario, tipoorigemdocumentoassinado, matricula, gradecurricular, turma, disciplina, ano, semestre, unidadeEnsino, provedordeassinaturaenum, codigoprovedordeassinatura, chaveprovedordeassinatura, urlprovedordeassinatura , matriculaPeriodo, planoEnsino, curso, programacaoformatura, decisaoJudicial, versaoDiploma, expedicaoDiploma, codigoValidacaoCurriculoEscolarDigital, arquivoVisual, iddiplomadigital, iddadosregistrosdiplomadigital, codigoValidacaoHistoricoDigital) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

		obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlInserir = arg0.prepareStatement(sql);
				sqlInserir.setInt(1, obj.getArquivo().getCodigo());
				sqlInserir.setTimestamp(2, Uteis.getDataJDBCTimestamp(obj.getDataRegistro()));
				sqlInserir.setInt(3, obj.getUsuario().getCodigo().intValue());
				sqlInserir.setString(4, obj.getTipoOrigemDocumentoAssinadoEnum().name());
				if (Uteis.isAtributoPreenchido(obj.getMatricula().getMatricula())) {
					sqlInserir.setString(5, obj.getMatricula().getMatricula());
				} else {
					sqlInserir.setNull(5, 0);
				}
				if (Uteis.isAtributoPreenchido(obj.getGradeCurricular())) {
					sqlInserir.setInt(6, obj.getGradeCurricular().getCodigo());
				} else {
					sqlInserir.setNull(6, 0);
				}
				if (Uteis.isAtributoPreenchido(obj.getTurma())) {
					sqlInserir.setInt(7, obj.getTurma().getCodigo());
				} else {
					sqlInserir.setNull(7, 0);
				}
				if (Uteis.isAtributoPreenchido(obj.getDisciplina())) {
					sqlInserir.setInt(8, obj.getDisciplina().getCodigo());
				} else {
					sqlInserir.setNull(8, 0);
				}
				if (Uteis.isAtributoPreenchido(obj.getAno())) {
					sqlInserir.setString(9, obj.getAno());
				} else {
					sqlInserir.setNull(9, 0);
				}
				if (Uteis.isAtributoPreenchido(obj.getSemestre())) {
					sqlInserir.setString(10, obj.getSemestre());
				} else {
					sqlInserir.setNull(10, 0);
				}
				if (Uteis.isAtributoPreenchido(obj.getUnidadeEnsinoVO().getCodigo())) {
					sqlInserir.setInt(11, obj.getUnidadeEnsinoVO().getCodigo());
				} else {
					sqlInserir.setNull(11, 0);
				}

				int index = 11;
//				Uteis.setValuePreparedStatement(obj.getProvedorDeAssinaturaEnum(), ++index, sqlInserir);
//				Uteis.setValuePreparedStatement(obj.getCodigoProvedorDeAssinatura(), ++index, sqlInserir);
//				Uteis.setValuePreparedStatement(obj.getChaveProvedorDeAssinatura(), ++index, sqlInserir);
//				Uteis.setValuePreparedStatement(obj.getUrlProvedorDeAssinatura(), ++index, sqlInserir);
				
				if (Uteis.isAtributoPreenchido(obj.getMatriculaPeriodo().getCodigo())) {
					sqlInserir.setInt(++index, obj.getMatriculaPeriodo().getCodigo());
				} else {
					sqlInserir.setNull(++index, 0);
				}
				if (Uteis.isAtributoPreenchido(obj.getPlanoEnsinoVO())) {
					sqlInserir.setInt(++index, obj.getPlanoEnsinoVO().getCodigo());
				} else {
					sqlInserir.setNull(++index, 0); 
				}
				if (Uteis.isAtributoPreenchido(obj.getCursoVO().getCodigo())) {
					sqlInserir.setInt(++index, obj.getCursoVO().getCodigo());
				} else {
					sqlInserir.setNull(++index, 0);
				}
				if (Uteis.isAtributoPreenchido(obj.getProgramacaoFormaturaVO().getCodigo())) {
					sqlInserir.setInt(++index, obj.getProgramacaoFormaturaVO().getCodigo());
				} else {
					sqlInserir.setNull(++index, 0);
				}
				sqlInserir.setBoolean(++index, obj.getDecisaoJudicial());
				if ((obj.getTipoOrigemDocumentoAssinadoEnum() != null && (obj.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.DIPLOMA_DIGITAL) || obj.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.DOCUMENTACAO_ACADEMICA_REGISTRO_DIPLOMA_DIGITAL) || obj.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.HISTORICO_DIGITAL) || obj.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.CURRICULO_ESCOLAR_DIGITAL))) && obj.getVersaoDiploma() != null) {
					sqlInserir.setString(++index, obj.getVersaoDiploma().getValor());
				} else {
					sqlInserir.setNull(++index, 0);
				}
				if (Uteis.isAtributoPreenchido(obj.getExpedicaoDiplomaVO())) {
					sqlInserir.setInt(++index, obj.getExpedicaoDiplomaVO().getCodigo());
				} else {
					sqlInserir.setNull(++index, 0);
				}
				if (Uteis.isAtributoPreenchido(obj.getCodigoValidacaoCurriculoEscolarDigital())) {
					sqlInserir.setString(++index, obj.getCodigoValidacaoCurriculoEscolarDigital());
				} else {
					sqlInserir.setNull(++index, 0);
				}
				if (Uteis.isAtributoPreenchido(obj.getArquivoVisual().getCodigo())) {
					sqlInserir.setInt(++index, obj.getArquivoVisual().getCodigo());
				} else {
					sqlInserir.setNull(++index, 0);
				}
				if (Uteis.isAtributoPreenchido(obj.getIdDiplomaDigital())) {
					sqlInserir.setString(++index, obj.getIdDiplomaDigital());
				} else {
					sqlInserir.setNull(++index, 0);
				}
				if (Uteis.isAtributoPreenchido(obj.getIdDadosRegistrosDiplomaDigital())) {
					sqlInserir.setString(++index, obj.getIdDadosRegistrosDiplomaDigital());
				} else {
					sqlInserir.setNull(++index, 0);
				}
				if (Uteis.isAtributoPreenchido(obj.getCodigoValidacaoHistoricoDigital())) {
					sqlInserir.setString(++index, obj.getCodigoValidacaoHistoricoDigital());
				} else {
					sqlInserir.setNull(++index, 0);
				}
				return sqlInserir;
			}
		}, new ResultSetExtractor<Integer>() {

			public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
				if (arg0.next()) {
					obj.setNovoObj(Boolean.FALSE);
					return arg0.getInt("codigo");
				}
				return null;
			}
		}));
		obj.setNovoObj(Boolean.FALSE);
		getFacadeFactory().getDocumentoAssinadoPessoaFacade().persistir(obj.getListaDocumentoAssinadoPessoa(), verificarAcesso, usuario);
		if (Uteis.isAtributoPreenchido(obj.getArquivo().getCodigo())) {
			obj.getArquivo().setCodOrigem(obj.getCodigo());
			getFacadeFactory().getArquivoFacade().alterarCodigoOrigemArquivo(obj.getArquivo(), usuario);
		}
		if (Uteis.isAtributoPreenchido(obj.getArquivoVisual().getCodigo())) {
			obj.getArquivoVisual().setCodOrigem(obj.getCodigo());
			getFacadeFactory().getArquivoFacade().alterarCodigoOrigemArquivo(obj.getArquivoVisual(), usuario);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void alterar(final DocumentoAssinadoVO obj, boolean verificarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		try {
			alterar(getIdEntidade(), verificarAcesso, usuario);

			final String sql = "UPDATE DocumentoAssinado set arquivo=?, dataRegistro=?, usuario=?," + " tipoorigemdocumentoassinado=?, matricula=?, gradecurricular=?, " + " turma=?, disciplina=?, ano=?, semestre=?, unidadeEnsino=?, provedordeassinaturaenum=?, codigoprovedordeassinatura=?, chaveprovedordeassinatura=?, urlprovedordeassinatura=? , matriculaPeriodo=?, planoEnsino =?, curso=?, programacaoFormatura=?, expedicaoDiploma=?, codigoValidacaoCurriculoEscolarDigital=?, codigoValidacaoHistoricoDigital=? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setInt(1, obj.getArquivo().getCodigo());
					sqlAlterar.setTimestamp(2, Uteis.getDataJDBCTimestamp(obj.getDataRegistro()));
					sqlAlterar.setInt(3, obj.getUsuario().getCodigo().intValue());
					sqlAlterar.setString(4, obj.getTipoOrigemDocumentoAssinadoEnum().name());
					if (Uteis.isAtributoPreenchido(obj.getMatricula().getMatricula())) {
						sqlAlterar.setString(5, obj.getMatricula().getMatricula());
					} else {
						sqlAlterar.setNull(5, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getGradeCurricular())) {
						sqlAlterar.setInt(6, obj.getGradeCurricular().getCodigo());
					} else {
						sqlAlterar.setNull(6, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getTurma())) {
						sqlAlterar.setInt(7, obj.getTurma().getCodigo());
					} else {
						sqlAlterar.setNull(7, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getDisciplina())) {
						sqlAlterar.setInt(8, obj.getDisciplina().getCodigo());
					} else {
						sqlAlterar.setNull(8, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getAno())) {
						sqlAlterar.setString(9, obj.getAno());
					} else {
						sqlAlterar.setNull(9, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getSemestre())) {
						sqlAlterar.setString(10, obj.getSemestre());
					} else {
						sqlAlterar.setNull(10, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getUnidadeEnsinoVO().getCodigo())) {
						sqlAlterar.setInt(11, obj.getUnidadeEnsinoVO().getCodigo());
					} else {
						sqlAlterar.setNull(11, 0);
					}

					int index = 11;
//					Uteis.setValuePreparedStatement(obj.getProvedorDeAssinaturaEnum(), ++index, sqlAlterar);
//					Uteis.setValuePreparedStatement(obj.getCodigoProvedorDeAssinatura(), ++index, sqlAlterar);
//					Uteis.setValuePreparedStatement(obj.getChaveProvedorDeAssinatura(), ++index, sqlAlterar);
//					Uteis.setValuePreparedStatement(obj.getUrlProvedorDeAssinatura(), ++index, sqlAlterar);
					
					if (Uteis.isAtributoPreenchido(obj.getMatriculaPeriodo().getCodigo())) {
						sqlAlterar.setInt(++index, obj.getMatriculaPeriodo().getCodigo());
					} else {
						sqlAlterar.setNull(++index, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getPlanoEnsinoVO())) {
						sqlAlterar.setInt(++index, obj.getPlanoEnsinoVO().getCodigo());
					} else {
						sqlAlterar.setNull(++index, 0); 
					}
					if (Uteis.isAtributoPreenchido(obj.getCursoVO().getCodigo())) {
						sqlAlterar.setInt(++index, obj.getCursoVO().getCodigo());
					} else {
						sqlAlterar.setNull(++index, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getProgramacaoFormaturaVO().getCodigo())) {
						sqlAlterar.setInt(++index, obj.getProgramacaoFormaturaVO().getCodigo());
					} else {
						sqlAlterar.setNull(++index, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getExpedicaoDiplomaVO())) {
						sqlAlterar.setInt(++index, obj.getExpedicaoDiplomaVO().getCodigo());
					} else {
						sqlAlterar.setNull(++index, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getCodigoValidacaoCurriculoEscolarDigital())) {
						sqlAlterar.setString(++index, obj.getCodigoValidacaoCurriculoEscolarDigital());
					} else {
						sqlAlterar.setNull(++index, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getCodigoValidacaoHistoricoDigital())) {
						sqlAlterar.setString(++index, obj.getCodigoValidacaoHistoricoDigital());
					} else {
						sqlAlterar.setNull(++index, 0);
					}
//					Uteis.setValuePreparedStatement(obj.getCodigo(), ++index, sqlAlterar);

					return sqlAlterar;
				}
			}) == 0) {
				incluir(obj, verificarAcesso, usuario, configuracaoGeralSistemaVO);
				return;
			}
			getFacadeFactory().getDocumentoAssinadoPessoaFacade().persistir(obj.getListaDocumentoAssinadoPessoa(), verificarAcesso, usuario);
		} catch (Exception e) {
			throw e;
		}
	}
	

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void atualizarDocumentoAssinadoInvalido(final Integer codigo, final boolean documentoAssinadoInvalido, final String motivo, UsuarioVO usuario) throws Exception {
		final String sql = "UPDATE DocumentoAssinado set documentoAssinadoInvalido=?, motivoDocumentoAssinadoInvalido=? WHERE codigo = ? " +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				sqlAlterar.setBoolean(1, documentoAssinadoInvalido);
				sqlAlterar.setString(2, motivo);
				sqlAlterar.setInt(3, codigo);
				return sqlAlterar;
			}
		});
	}

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
    public void excluir(final DocumentoAssinadoVO obj, final boolean verificarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
        excluir(getIdEntidade(), verificarAcesso, usuario);
        if (obj.getTipoOrigemDocumentoAssinadoEnum().isTermoEstagioObrigatorio()) {
            String sql = "update estagio set documentoAssinado= null  WHERE ((documentoAssinado = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(sql, obj.getCodigo());
        }
        String sql = "DELETE FROM DocumentoAssinado WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(sql, obj.getCodigo());
        getFacadeFactory().getArquivoFacade().excluir(obj.getArquivo(), usuario, configuracaoGeralSistemaVO);
        getFacadeFactory().getArquivoFacade().excluirArquivoDoDiretorioEspecifico(obj.getArquivo(), configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + obj.getArquivo().getPastaBaseArquivo());
        if (obj.getProvedorDeAssinaturaEnum().isProvedorCertisign() || obj.getProvedorDeAssinaturaEnum().isProvedorImprensaOficial()) {
            ConfiguracaoGEDVO configGedVO = getFacadeFactory().getConfiguracaoGEDFacade().consultarPorUnidadeEnsino(obj.getUnidadeEnsinoVO().getCodigo(), false, usuario);
            realizarExclusaoDocument(obj, configGedVO);
        }
        if (obj.getProvedorDeAssinaturaEnum().isProvedorTechCert()) {
            ConfiguracaoGEDVO configGedVO = getFacadeFactory().getConfiguracaoGEDFacade().consultarPorUnidadeEnsino(obj.getUnidadeEnsinoVO().getCodigo(), false, usuario);
            realizarExclusaoDocumentTechCert(obj, configGedVO);
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
    public String realizarInclusaoDocumentoAssinadoPorAtaResultadosFinais(String nomeArquivoOrigem, TurmaVO turma, DisciplinaVO disciplina, String ano, String semestre, TipoOrigemDocumentoAssinadoEnum tipoOrigemDocumentoAssinado, ProvedorDeAssinaturaEnum provedorDeAssinaturaEnum, String corAssinatura, Float alturaAssinatura, Float larguraAssinatura, ConfiguracaoGeralSistemaVO config, UsuarioVO usuarioVO, String alinhamentoAssinaturaDigital, UnidadeEnsinoVO unidadeEnsinoVO, FuncionarioVO funcionarioPrincipal, String cargoFuncionario1, String tituloFuncionario1, FuncionarioVO funcionarioSecundario, String cargoFuncionario2, String tituloFuncionario2) throws Exception {
        DocumentoAssinadoVO obj = new DocumentoAssinadoVO();
        obj.setDataRegistro(new Date());
        obj.setUsuario(usuarioVO);
        obj.setTipoOrigemDocumentoAssinadoEnum(TipoOrigemDocumentoAssinadoEnum.ATA_RESULTADO_FINAL);
        obj.setTurma(turma);
        obj.setUnidadeEnsinoVO(unidadeEnsinoVO);
        obj.setDisciplina(disciplina);
        obj.setAno(ano);
        obj.setSemestre(semestre);
        obj.getArquivo().setNome(usuarioVO.getCodigo() + "_" + new Date().getTime() + ".pdf");
        obj.getArquivo().setDescricao(Uteis.isAtributoPreenchido(tipoOrigemDocumentoAssinado) ? tipoOrigemDocumentoAssinado.getDescricao() : " " + "_" + new Date().getTime());
        obj.getArquivo().setExtensao(".pdf");
        obj.getArquivo().setResponsavelUpload(usuarioVO);
        obj.getArquivo().setDataUpload(new Date());
        obj.getArquivo().setDataDisponibilizacao(new Date());
        obj.getArquivo().setManterDisponibilizacao(true);
        obj.getArquivo().setControlarDownload(true);
        obj.getArquivo().setDataIndisponibilizacao(null);
        obj.getArquivo().setSituacao(SituacaoArquivo.ATIVO.getValor());
        obj.getArquivo().setPastaBaseArquivo(PastaBaseArquivoEnum.DOCUMENTOS_ASSINADOS_TMP.name());
        obj.getArquivo().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.DOCUMENTOS_ASSINADOS_TMP);
        obj.getArquivo().setTurma(turma);
        obj.getArquivo().setDisciplina(disciplina);
        obj.getArquivo().setArquivoAssinadoDigitalmente(true);
        obj.setProvedorAssinaturaVisaoAdm(provedorDeAssinaturaEnum);

		if (Uteis.isAtributoPreenchido(funcionarioPrincipal.getCodigo())) {
			obj.getListaDocumentoAssinadoPessoa().add(new DocumentoAssinadoPessoaVO(new Date(), funcionarioPrincipal.getPessoa(), TipoPessoa.FUNCIONARIO, SituacaoDocumentoAssinadoPessoaEnum.PENDENTE, obj, 1, cargoFuncionario1, tituloFuncionario1, null, funcionarioPrincipal.getPessoa().getTipoAssinaturaDocumentoEnum()));
		}
		if (Uteis.isAtributoPreenchido(funcionarioSecundario.getCodigo())) {
			obj.getListaDocumentoAssinadoPessoa().add(new DocumentoAssinadoPessoaVO(new Date(), funcionarioSecundario.getPessoa(), TipoPessoa.FUNCIONARIO, SituacaoDocumentoAssinadoPessoaEnum.PENDENTE, obj, 2, cargoFuncionario2, tituloFuncionario2, null, funcionarioSecundario.getPessoa().getTipoAssinaturaDocumentoEnum()));
		}		
		
		incluir(obj, false, usuarioVO, config);

		return realizarVerificacaoProvedorDeAssinatura(obj, obj.getUnidadeEnsinoVO().getCodigo(), true, true, nomeArquivoOrigem, null, "#000000", 80f, 200f, 8f, 10, 380, 0, 0, config, true, usuarioVO,true);
//		String arquivoOrigem = UteisJSF.getCaminhoWeb() + "relatorio/" + nomeArquivoOrigem;		
//		ConfiguracaoGEDVO configGEDVO = null;
//		if (Uteis.isAtributoPreenchido(obj.getUnidadeEnsinoVO().getCodigo())) {
//			configGEDVO = getAplicacaoControle().getConfiguracaoGEDPorUnidadeEnsino(obj.getUnidadeEnsinoVO().getCodigo(), usuarioVO);
//			adicionarSeloPDF(obj, arquivoOrigem, config, configGEDVO);
//			adicionarQRCodePDF(obj, arquivoOrigem, config, configGEDVO);			
//		}
//		if(Uteis.isAtributoPreenchido(configGEDVO)) {
//			return executarAssinaturaParaDocumento(obj, configGEDVO, arquivoOrigem, corAssinatura,  config, true, usuarioVO,true, obj.getTipoOrigemDocumentoAssinadoEnum());
//		}else {
//			return executarAssinaturaParaDocumento(obj, Uteis.isAtributoPreenchido(configGEDVO) ? configGEDVO.getCertificadoDigitalUnidadeEnsinoVO() : config.getCertificadoParaDocumento(),  Uteis.isAtributoPreenchido(configGEDVO) ? configGEDVO.getSenhaCertificadoDigitalUnidadeEnsino() : config.getSenhaCertificadoParaDocumento(), arquivoOrigem , null, "#000000", 80f, 200f, 8f, 10, 380, 0, 0, config, true, usuarioVO,true);
//		}
	}

	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public String realizarInclusaoDocumentoAssinadoPorImpostoRenda(String nomeArquivoOrigem, MatriculaVO matricula, GradeCurricularVO gradeCurricular, TurmaVO turma, DisciplinaVO disciplina, String ano, String semestre, TipoOrigemDocumentoAssinadoEnum tipoOrigemDocumentoAssinado, ProvedorDeAssinaturaEnum provedorDeAssinaturaEnum,String origemArquivo, String descricaoArquivo,  String corAssinatura, Float alturaAssinatura, Float larguraAssinatura, ConfiguracaoGeralSistemaVO config, Integer codigoRequerimento, UsuarioVO usuarioVO) throws Exception {
		DocumentoAssinadoVO obj = new DocumentoAssinadoVO();
		obj.setDataRegistro(new Date());
		obj.setUsuario(usuarioVO);
		// Caso o histórico tenha sido impresso da visão do aluno em requerimento será 
		// utitlizado a reqra de requerimento
		if (codigoRequerimento != null && !codigoRequerimento.equals(0)) {
			obj.setTipoOrigemDocumentoAssinadoEnum(TipoOrigemDocumentoAssinadoEnum.REQUERIMENTO);
			obj.getArquivo().setOrigem(OrigemArquivo.REQUERIMENTO.getValor());
			obj.getArquivo().setCodOrigem(codigoRequerimento);
		} else {
			obj.setTipoOrigemDocumentoAssinadoEnum(tipoOrigemDocumentoAssinado);
		}
		obj.setMatricula(matricula);
		obj.setGradecurricular(gradeCurricular);
		obj.setTurma(turma);
		obj.setUnidadeEnsinoVO(matricula.getUnidadeEnsino());
		obj.setDisciplina(disciplina);
		obj.setAno(ano);
		obj.setSemestre(semestre);
		obj.getArquivo().setNome(Uteis.removeCaractersEspeciais(Uteis.removeCaractersEspeciais(matricula.getMatricula()) + "_" + usuarioVO.getCodigo() + "_" + new Date().getTime() + ".pdf"));
		obj.getArquivo().setOrigem(origemArquivo);
		if(obj.getArquivo().getOrigem().equals("IR")) {
			obj.getArquivo().setDescricao(descricaoArquivo);
		}else {
			obj.getArquivo().setDescricao(tipoOrigemDocumentoAssinado.getDescricao() + "_" + matricula.getMatricula() + "_" + new Date().getTime());
		}
		obj.getArquivo().setExtensao(".pdf");
		obj.getArquivo().setResponsavelUpload(usuarioVO);
		obj.getArquivo().setDataUpload(new Date());
		obj.getArquivo().setDataDisponibilizacao(new Date());
		obj.getArquivo().setManterDisponibilizacao(true);
		obj.getArquivo().setControlarDownload(true);
		obj.getArquivo().setDataIndisponibilizacao(null);
		obj.getArquivo().setSituacao(SituacaoArquivo.ATIVO.getValor());
		obj.getArquivo().setPastaBaseArquivo(PastaBaseArquivoEnum.DOCUMENTOS_ASSINADOS_TMP.name());
		obj.getArquivo().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.DOCUMENTOS_ASSINADOS_TMP);
		obj.getArquivo().setPessoaVO(matricula.getAluno());
		obj.getArquivo().setTurma(turma);
		obj.getArquivo().setDisciplina(disciplina);
		obj.getArquivo().setArquivoAssinadoDigitalmente(true);
		obj.setProvedorAssinaturaVisaoAdm(provedorDeAssinaturaEnum);
		incluir(obj, false, usuarioVO, config);
		return realizarVerificacaoProvedorDeAssinatura(obj, obj.getUnidadeEnsinoVO().getCodigo(), true, false, nomeArquivoOrigem, AlinhamentoAssinaturaDigitalEnum.RODAPE_DIREITA, corAssinatura, alturaAssinatura, larguraAssinatura, 6f, 12, 20, 5, 2, config, true, usuarioVO,true);
//		ConfiguracaoGEDVO configGEDVO = null;
//		if (Uteis.isAtributoPreenchido(obj.getUnidadeEnsinoVO().getCodigo())) {
//			configGEDVO = getAplicacaoControle().getConfiguracaoGEDPorUnidadeEnsino(obj.getUnidadeEnsinoVO().getCodigo(),  usuarioVO);			
//			adicionarSeloPDF(obj, UteisJSF.getCaminhoWeb() + "relatorio/" + nomeArquivoOrigem, config, configGEDVO);
//		}
//		if(Uteis.isAtributoPreenchido(configGEDVO)) {
//			return executarAssinaturaParaDocumento(obj, configGEDVO, UteisJSF.getCaminhoWeb() + "relatorio/" + nomeArquivoOrigem, corAssinatura,  config, true, usuarioVO,true, obj.getTipoOrigemDocumentoAssinadoEnum());
//		}else {
//			return executarAssinaturaParaDocumento(obj, Uteis.isAtributoPreenchido(configGEDVO) ? configGEDVO.getCertificadoDigitalUnidadeEnsinoVO() :config.getCertificadoParaDocumento(), Uteis.isAtributoPreenchido(configGEDVO) ? configGEDVO.getSenhaCertificadoDigitalUnidadeEnsino() :config.getSenhaCertificadoParaDocumento(), UteisJSF.getCaminhoWeb() + "relatorio/" + nomeArquivoOrigem, AlinhamentoAssinaturaDigitalEnum.RODAPE_DIREITA, corAssinatura, alturaAssinatura, larguraAssinatura, 6f, 12, 20, 5, 2, config, true, usuarioVO,true);
//		}
		
	}
	
	
	
	@SuppressWarnings("unchecked")
	public String realizarAssinaturaAgendaProfessor(String nomeArquivoOrigem, AlinhamentoAssinaturaDigitalEnum alinhamentoAssinaturaDigitalEnum,  String corAssinatura, Float alturaAssinatura, Float larguraAssinatura, ConfiguracaoGeralSistemaVO config, FuncionarioVO funcionarioVO, UsuarioVO usuarioVO) throws Exception {
		DocumentoAssinadoVO obj = new DocumentoAssinadoVO();
		obj.setDataRegistro(new Date());
		obj.setUsuario(usuarioVO);
		obj.getArquivo().setNome(usuarioVO.getCodigo() + "_" + new Date().getTime() + ".pdf");
		obj.getArquivo().setExtensao(".pdf");
		obj.getArquivo().setResponsavelUpload(usuarioVO);
		obj.getArquivo().setDataUpload(new Date());
		obj.getArquivo().setDataDisponibilizacao(new Date());
		obj.getArquivo().setManterDisponibilizacao(true);
		obj.getArquivo().setControlarDownload(true);
		obj.getArquivo().setDataIndisponibilizacao(null);
		obj.getArquivo().setSituacao(SituacaoArquivo.ATIVO.getValor());
		obj.getArquivo().setPastaBaseArquivo(PastaBaseArquivoEnum.DOCUMENTOS_ASSINADOS_TMP.getValue());
		obj.getArquivo().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.DOCUMENTOS_ASSINADOS_TMP);
		obj.getArquivo().setArquivoAssinadoDigitalmente(true);
		obj.setTipoOrigemDocumentoAssinadoEnum(TipoOrigemDocumentoAssinadoEnum.DECLARACAO);
		UnidadeEnsinoVO unidadeEnsinoVO = null;
//		List<FuncionarioCargoVO> funcionarioCargoVO = getFacadeFactory().getFuncionarioCargoFacade().consultarPorFuncionario(funcionarioVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_PROCESSAMENTO, usuarioVO);
//		if(Uteis.isAtributoPreenchido(funcionarioCargoVO)) {
//			unidadeEnsinoVO =funcionarioCargoVO.iterator().next().getUnidade();
//		}else {
//			unidadeEnsinoVO = getFacadeFactory().getUnidadeEnsinoFacade().obterUnidadeMatriz(false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO);
//		}
		return realizarVerificacaoProvedorDeAssinatura(obj, unidadeEnsinoVO.getCodigo(), false, false, nomeArquivoOrigem, null, corAssinatura, 80f, 200f, 8f, 10, 380, 0, 0, config, true, usuarioVO,true);
		
//			ConfiguracaoGEDVO configGEDVO = null;
//			List<FuncionarioCargoVO> funcionarioCargoVO = getFacadeFactory().getFuncionarioCargoFacade().consultarPorFuncionario(funcionarioVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_PROCESSAMENTO, usuarioVO);
//			if(Uteis.isAtributoPreenchido(funcionarioCargoVO)) {
//				configGEDVO = getAplicacaoControle().getConfiguracaoGEDPorUnidadeEnsino(funcionarioCargoVO.iterator().next().getUnidade().getCodigo(),  usuarioVO);
//			}else {
//				UnidadeEnsinoVO unidadeEnsinoVO = getFacadeFactory().getUnidadeEnsinoFacade().obterUnidadeMatriz(false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO);
//				configGEDVO = getAplicacaoControle().getConfiguracaoGEDPorUnidadeEnsino(unidadeEnsinoVO.getCodigo(), usuarioVO);
//			}
//			
//			if(!Uteis.isAtributoPreenchido(configGEDVO.getCodigo())) {
//				return nomeArquivoOrigem;
//			}
//			return executarAssinaturaParaDocumento(obj, configGEDVO, UteisJSF.getCaminhoWeb() + "relatorio/" + nomeArquivoOrigem, corAssinatura, config, true, usuarioVO,true, obj.getTipoOrigemDocumentoAssinadoEnum());
		
	}
	
	

	public void validarDadosCertificadoDigital(ArquivoVO certificadoDigitalVO, String senhaCertificadoDigital) throws Exception {
		if (!Uteis.isAtributoPreenchido(certificadoDigitalVO)) {
			throw new Exception("Não foi encontrado o certificado para assinatura do documento. Por favor verificar o cadastro de configuração GED vinculado à Unidade de Ensino");
		}
		if (!Uteis.isAtributoPreenchido(senhaCertificadoDigital)) {
			throw new Exception("A senha para certificado digital não foi informado. Por favor verificar o cadastro de configuração GED vinculado à Unidade de Ensino");
		}
			
		
//		if (!Uteis.isAtributoPreenchido(config.getCertificadoParaDocumento())) {
//			throw new Exception("Não foi encontrado o certificado para assinatura do documento. Por favor verificar o cadastro de configuração geral do sistema");
//		}
//		if (!Uteis.isAtributoPreenchido(config.getSenhaCertificadoParaDocumento())) {
//			throw new Exception("A senha para certificado digital não foi informado. Por favor verificar o cadastro de configuração geral do sistema");
//		}
	}
	
	@Deprecated
	/**
	 * Utilizar o metodo realizarVerificacaoProvedorDeAssinatura Pedro Andrade
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public String executarAssinaturaParaDocumento(DocumentoAssinadoVO obj, ConfiguracaoGEDVO configuracaoGEDVO, String arquivoOrigem, String corAssinatura, ConfiguracaoGeralSistemaVO config, Boolean excluirArquivoOrigem, UsuarioVO usuarioVO, Boolean disponibilizarArquivoAssinadoParaDowload, TipoOrigemDocumentoAssinadoEnum tipoOrigemDocumentoAssinadoEnum) throws Exception {
		
		ConfiguracaoGedOrigemVO configuracaoGedOrigemVO = configuracaoGEDVO.getConfiguracaoGedOrigemVO(tipoOrigemDocumentoAssinadoEnum);
		validarDadosCertificadoDigital(configuracaoGEDVO.getCertificadoDigitalUnidadeEnsinoVO(), configuracaoGEDVO.getSenhaCertificadoDigitalUnidadeEnsino());

		if (Uteis.isSistemaOperacionalWindows() && arquivoOrigem.startsWith("/")) {
			arquivoOrigem = arquivoOrigem.substring(1, arquivoOrigem.length());
		}
		
		preencherAssinadorDigitalDocumentoPdf(arquivoOrigem, configuracaoGEDVO.getCertificadoDigitalUnidadeEnsinoVO(), configuracaoGEDVO.getSenhaCertificadoDigitalUnidadeEnsino(), obj, 
				null, corAssinatura, Float.valueOf(configuracaoGedOrigemVO.getAlturaAssinaturaUnidadeEnsino()), Float.valueOf(configuracaoGedOrigemVO.getLarguraAssinaturaUnidadeEnsino()), 8f, 
				configuracaoGedOrigemVO.getPosicaoXAssinaturaUnidadeEnsino(), configuracaoGedOrigemVO.getPosicaoYAssinaturaUnidadeEnsino(), 
				0, 0, configuracaoGedOrigemVO.isUltimaPaginaAssinaturaUnidadeEnsino(), configuracaoGedOrigemVO.getAssinaturaUnidadeEnsino(),  config, excluirArquivoOrigem,disponibilizarArquivoAssinadoParaDowload);
		return obj.getArquivo().getNome();
		
	}
	
	@Deprecated
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	private String executarAssinaturaParaDocumento(DocumentoAssinadoVO obj, ArquivoVO certificadoDigitalVO, String senhaCertificadoDigital, String arquivoOrigem, AlinhamentoAssinaturaDigitalEnum alinhamentoAssinaturaDigital, String corAssinatura, Float alturaAssinatura, Float larguraAssinatura,float tamnhoFonte, int coordenadaLLX, int coordenadaLLY, int coordenadaURX, int coordenadaURY, ConfiguracaoGeralSistemaVO config, Boolean excluirArquivoOrigem, UsuarioVO usuarioVO,Boolean disponibilizarArquivoAssinadoParaDowload) throws Exception {
		
		validarDadosCertificadoDigital(certificadoDigitalVO, senhaCertificadoDigital);

		if (Uteis.isSistemaOperacionalWindows() && arquivoOrigem.startsWith("/")) {
			arquivoOrigem = arquivoOrigem.substring(1, arquivoOrigem.length());
		}
		preencherAssinadorDigitalDocumentoPdf(arquivoOrigem, certificadoDigitalVO, senhaCertificadoDigital, obj, alinhamentoAssinaturaDigital, corAssinatura, alturaAssinatura, larguraAssinatura,tamnhoFonte, coordenadaLLX, coordenadaLLY, coordenadaURX, coordenadaURY, true, true,  config, excluirArquivoOrigem,disponibilizarArquivoAssinadoParaDowload);
		return obj.getArquivo().getNome();
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void preencherAssinadorDigitalDocumentoPdf(String arquivoOrigem, ArquivoVO certificadoDigitalVO, String senhaCertificadoDigital, 
			DocumentoAssinadoVO documentoAssinado, AlinhamentoAssinaturaDigitalEnum alinhamentoAssinaturaDigital, String corAssinatura, Float alturaAssinatura, Float larguraAssinatura, 
			float tamanhoFonte, int coordenadaLLX, int coordenadaLLY, int coordenadaURX, int coordenadaURY, boolean apresentarAssinaturaUltimaPagina,  boolean apresentarAssinaturaDigital, ConfiguracaoGeralSistemaVO config, Boolean excluirArquivoOrigem, 
			Boolean disponibilizarArquivoAssinadoParaDowload) throws Exception {
		String caminhoCertificado = "";
		
		if(Uteis.isAtributoPreenchido(certificadoDigitalVO.getPastaBaseArquivo())) {
			caminhoCertificado = config.getLocalUploadArquivoFixo() + File.separator + certificadoDigitalVO.getPastaBaseArquivo() + File.separator + certificadoDigitalVO.getNome();
		}else{
			caminhoCertificado = config.getLocalUploadArquivoFixo() + File.separator + config.getCertificadoParaDocumento().getPastaBaseArquivo() + File.separator + certificadoDigitalVO.getNome();
		}
		
		ArquivoVO arquivoVO = (documentoAssinado.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.DIPLOMA_DIGITAL) || documentoAssinado.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.HISTORICO_DIGITAL) || documentoAssinado.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.CURRICULO_ESCOLAR_DIGITAL)) ? documentoAssinado.getArquivoVisual() : documentoAssinado.getArquivo();
		AssinaturaDigialDocumentoPDF assinador = new AssinaturaDigialDocumentoPDF();
		assinador.setToken(true);
		assinador.setPathKeyStore(caminhoCertificado);
		assinador.setSenhaKeyStore(senhaCertificadoDigital);
		assinador.setArquivoOrigem(arquivoOrigem);
		assinador.setCaminhoArquivoDestino(config.getLocalUploadArquivoFixo() + File.separator + arquivoVO.getPastaBaseArquivo());
		assinador.setNomeArquivoDestino(arquivoVO.getNome());
		assinador.setIsValidarArquivoExistente(true);
		String razaoCertificado = documentoAssinado.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.DIPLOMA_DIGITAL) && Uteis.isAtributoPreenchido(documentoAssinado.getCodigoValidacaoDiplomaDigital()) ? documentoAssinado.getCodigoValidacaoDiplomaDigital() : documentoAssinado.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.CURRICULO_ESCOLAR_DIGITAL) && Uteis.isAtributoPreenchido(documentoAssinado.getCodigoValidacaoCurriculoEscolarDigital()) ? documentoAssinado.getCodigoValidacaoCurriculoEscolarDigital() : documentoAssinado.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.HISTORICO_DIGITAL) && Uteis.isAtributoPreenchido(documentoAssinado.getCodigoValidacaoHistoricoDigital()) ? documentoAssinado.getCodigoValidacaoHistoricoDigital() : documentoAssinado.getNumeroDocumentoAssinado();
		assinador.setRazaoCertificado(razaoCertificado);
		assinador.setDataAssinatura(documentoAssinado.getDataRegistro());
		assinador.setCorAssinaturaDigitalmente(corAssinatura);
		assinador.setAlturaAssinatura(alturaAssinatura);
		assinador.setLarguraAssinatura(larguraAssinatura);
		assinador.setTamanhoFonte(tamanhoFonte);
		assinador.setCoordenadaLLX(coordenadaLLX);
		assinador.setCoordenadaLLY(coordenadaLLY);
		assinador.setCoordenadaURX(coordenadaURX);
		assinador.setCoordenadaURY(coordenadaURY);
		assinador.setApresentarAssinaturaDigital(apresentarAssinaturaDigital);
		assinador.setApresentarAssinaturaUltimaPagina(apresentarAssinaturaUltimaPagina);
		if(alinhamentoAssinaturaDigital != null) {
		if (alinhamentoAssinaturaDigital.isRodapeDireita()) {
			assinador.setIsPosicaoAssinaturaCima(false);
			assinador.setIsPosicaoAssinaturaEsquerdo(false);
		} else if (alinhamentoAssinaturaDigital.isRodapeEsquerda()) {
			assinador.setIsPosicaoAssinaturaCima(false);
			assinador.setIsPosicaoAssinaturaEsquerdo(true);
		} else if (alinhamentoAssinaturaDigital.isTopoDireita()) {
			assinador.setIsPosicaoAssinaturaCima(true);
			assinador.setIsPosicaoAssinaturaEsquerdo(false);
		} else if (alinhamentoAssinaturaDigital.isTopoEsquerda()) {
			assinador.setIsPosicaoAssinaturaCima(true);
			assinador.setIsPosicaoAssinaturaEsquerdo(true);
		}
		}
		if (((documentoAssinado.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.DIPLOMA_DIGITAL) && Uteis.isAtributoPreenchido(documentoAssinado.getCodigoValidacaoDiplomaDigital())) || (documentoAssinado.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.CURRICULO_ESCOLAR_DIGITAL) && Uteis.isAtributoPreenchido(documentoAssinado.getCodigoValidacaoCurriculoEscolarDigital()))) || (!documentoAssinado.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.DIPLOMA_DIGITAL))) {
			assinador.realizarGeracaoDocumentoPdf();
		} else {
			File fileDiretorioCorreto = new File(assinador.getCaminhoArquivoDestino() + File.separator + assinador.getNomeArquivoDestino());
			File fileDiretorioTMP = new File(arquivoOrigem);
			getFacadeFactory().getArquivoHelper().copiar(fileDiretorioTMP, fileDiretorioCorreto);
		}
		if(documentoAssinado.getArquivo().getServidorArquivoOnline().equals(ServidorArquivoOnlineEnum.AMAZON_S3) && !documentoAssinado.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.CURRICULO_ESCOLAR_DIGITAL)) {
			getFacadeFactory().getDocumentoAssinadoFacade().realizarUploadArquivoAmazon(documentoAssinado.getArquivo(), config, true);
		}
		if(documentoAssinado.getArquivoVisual().getServidorArquivoOnline().equals(ServidorArquivoOnlineEnum.AMAZON_S3) && (documentoAssinado.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.DIPLOMA_DIGITAL) || documentoAssinado.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.HISTORICO_DIGITAL) || documentoAssinado.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.CURRICULO_ESCOLAR_DIGITAL))) {
			getFacadeFactory().getDocumentoAssinadoFacade().realizarUploadArquivoAmazon(documentoAssinado.getArquivoVisual(), config, true);
		}
		if(disponibilizarArquivoAssinadoParaDowload && arquivoVO.getServidorArquivoOnline().equals(ServidorArquivoOnlineEnum.APACHE)) {
			getFacadeFactory().getArquivoHelper().disponibilizarArquivoAssinadoParaDowload(config.getLocalUploadArquivoFixo() + File.separator + arquivoVO.getPastaBaseArquivo() + File.separator + arquivoVO.getNome(), arquivoVO.getNome());
		}
		if (excluirArquivoOrigem) {
			File fileExcluir = new File(arquivoOrigem);
			if (fileExcluir.exists()) {
				FileUtils.forceDelete(new File(arquivoOrigem));
			}
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public Integer executarAtualizacaoDocumentoAssinadoPorSeiSignature(DocumentoAssinadoVO obj, InputStream uploadedInputStream, ByteArrayOutputStream byteArquivo, Boolean xmlUpadoMapa, String tamanhoArquivoXML, boolean pemitirAssinaturaDiarioPeloProfessorAposAssinaturaCoordenadorCurso, Boolean retornarExcecaoAssinaturaCoordenadorPrimeiro, ConfiguracaoGeralSistemaVO config, UsuarioVO usuarioVO, Boolean assinarPorCNPJ, Integer ordemAssinatura, String provedorAssinatura, Boolean escreverArquivo, Boolean realizarEscritaDebug) throws Exception {
		PdfReader reader = null;
		PdfReader reader2 = null;
		ByteArrayOutputStream baos = null;
		InputStream is = null;
		String caminhoArquivo = null;
//		ServidorArquivoOnlineS3RS servidorExternoAmazon = null;
		try {
			realizarEscritaDebugSeiSignature(obj, realizarEscritaDebug, "(4) Inicio atualização situação documento assinado no SEI");
			Integer quantidadeRegistrosAlterados = 0;
			if (obj.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.UPLOAD_INSTITUCIONAL)) {
				atualizarApresentacaoArquivoIntitucionalAposTodosResponsaveisAssinarem(obj, usuarioVO);
			}
			String uploadedFileLocation = config.getLocalUploadArquivoFixo() + File.separator + obj.getArquivo().getPastaBaseArquivo() + File.separator + obj.getArquivo().getNome();
			baos = new ByteArrayOutputStream();
			if (xmlUpadoMapa) {
				baos = byteArquivo;
				baos.flush();
			} else {
				byte[] buffer = new byte[1024];
				int len;
				while ((len = uploadedInputStream.read(buffer)) > -1) {
					baos.write(buffer, 0, len);
				}
				baos.flush();
			}
//			if (!obj.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.DIPLOMA_DIGITAL) && !obj.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.HISTORICO_DIGITAL) && !obj.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.DOCUMENTACAO_ACADEMICA_REGISTRO_DIPLOMA_DIGITAL) && !obj.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.CURRICULO_ESCOLAR_DIGITAL)) {
//				reader = new PdfReader(new ByteArrayInputStream(baos.toByteArray()));
//				realizarVerificacaoAssinaturasPDFDocumentoAssinado(usuarioVO, obj, reader);
//			}
			realizarEscritaDebugSeiSignature(obj, realizarEscritaDebug, "(5) Realizando escrita. (" + (escreverArquivo || !obj.getArquivo().getServidorArquivoOnline().equals(ServidorArquivoOnlineEnum.AMAZON_S3)) + ")");
			if (escreverArquivo || !obj.getArquivo().getServidorArquivoOnline().equals(ServidorArquivoOnlineEnum.AMAZON_S3)) {
				is = new ByteArrayInputStream(baos.toByteArray());
				// Aqui vai forçar o delete do arquivo caso já esteja baixado na maquina do
				// usuário, evitando assim a leitura suja, pois logo abaixo irá escrever o
				// arquivo novamente
				File file = new File(uploadedFileLocation);
				if (file.exists() && file.canExecute() && file.canRead() && file.canWrite()) {
					file.delete();
				}
				file = null;
				getFacadeFactory().getArquivoHelper().escreverArquivo(is, uploadedFileLocation);
			}
			if (obj.getArquivo().getServidorArquivoOnline().equals(ServidorArquivoOnlineEnum.AMAZON_S3)) {
//				realizarEscritaDebugSeiSignature(obj, "(5.1) Inicio verificação xml antes de envio para AMAZON. [Arquivo: " + obj.getArquivo().getPastaBaseArquivo() + File.separator + obj.getArquivo().getNome());
				if (obj.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.DIPLOMA_DIGITAL) && !xmlUpadoMapa) {
					File file = new File(uploadedFileLocation);
					InputStream inputVerificar = new FileInputStream(file);
					InputStream inputTamanhoVerificar = new FileInputStream(file);
					try {
						int qtdAssinadosVerificar = (Uteis.isAtributoPreenchido(obj) && !obj.getListaDocumentoAssinadoPessoa().isEmpty() ? obj.getListaDocumentoAssinadoPessoa().stream().filter(p -> p.getSituacaoDocumentoAssinadoPessoaEnum().equals(SituacaoDocumentoAssinadoPessoaEnum.ASSINADO)).collect(Collectors.toList()).size() : 0) + 1;
						validarQtdAssinaturaDocumentoXML(inputVerificar, inputTamanhoVerificar, tamanhoArquivoXML, obj, qtdAssinadosVerificar, realizarEscritaDebug);
					} catch (Exception e) {
						if (inputVerificar != null) {
							inputVerificar.close();
							inputVerificar = null;
						}
						if (inputTamanhoVerificar != null) {
							inputTamanhoVerificar.close();
							inputTamanhoVerificar = null;
						}
						FileUtils.forceDelete(file);
						throw e;
					}
					if (inputVerificar != null) {
						inputVerificar.close();
						inputVerificar = null;
					}
					if (inputTamanhoVerificar != null) {
						inputTamanhoVerificar.close();
						inputTamanhoVerificar = null;
					}
				}
//				realizarEscritaDebugSeiSignature(obj, "(6) Realizando upload xml na AMAZON. [Arquivo: " + obj.getArquivo().getPastaBaseArquivo() + File.separator + obj.getArquivo().getNome());
//				servidorExternoAmazon = new ServidorArquivoOnlineS3RS(config.getUsuarioAutenticacao(), config.getSenhaAutenticacao(), config.getNomeRepositorio());
//				getFacadeFactory().getArquivoFacade().realizarUploadServidorAmazon(servidorExternoAmazon, obj.getArquivo(), config, true);
//				if (obj.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.DIPLOMA_DIGITAL) && !xmlUpadoMapa) {
////					realizarEscritaDebugSeiSignature(obj, "(7) Realizando download xml da AMAZON para verificar assinaturas apos upar arquivo no servidor amazon. [Arquivo: " + obj.getArquivo().getPastaBaseArquivo() + File.separator + obj.getArquivo().getNome());
//					File fileDiploma = new File(getFacadeFactory().getArquivoFacade().realizarDownloadArquivoAmazon(obj.getArquivo(), servidorExternoAmazon, config, true));
//					InputStream inputStream = new FileInputStream(fileDiploma);
//					InputStream inputTamanhoVerificar = new FileInputStream(fileDiploma);
//					try {
//						int qtdAssinados = (Uteis.isAtributoPreenchido(obj) && !obj.getListaDocumentoAssinadoPessoa().isEmpty() ? obj.getListaDocumentoAssinadoPessoa().stream().filter(p -> p.getSituacaoDocumentoAssinadoPessoaEnum().equals(SituacaoDocumentoAssinadoPessoaEnum.ASSINADO)).collect(Collectors.toList()).size() : 0) + 1;
//						validarQtdAssinaturaDocumentoXML(inputStream, inputTamanhoVerificar, tamanhoArquivoXML, obj, qtdAssinados, realizarEscritaDebug);
//					} catch (Exception e) {
//						if (inputStream != null) {
//							inputStream.close();
//							inputStream = null;
//						}
//						if (inputTamanhoVerificar != null) {
//							inputTamanhoVerificar.close();
//							inputTamanhoVerificar = null;
//						}
//						FileUtils.forceDelete(fileDiploma);
//						throw e;
//					}
//					if (inputStream != null) {
//						inputStream.close();
//						inputStream = null;
//					}
//					if (inputTamanhoVerificar != null) {
//						inputTamanhoVerificar.close();
//						inputTamanhoVerificar = null;
//					}
//					FileUtils.forceDelete(fileDiploma);
//				}
			}
//			if (!obj.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.CURRICULO_ESCOLAR_DIGITAL) && !obj.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.DIPLOMA_DIGITAL) && !obj.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.HISTORICO_DIGITAL) && !obj.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.DOCUMENTACAO_ACADEMICA_REGISTRO_DIPLOMA_DIGITAL) && obj.getArquivo().getServidorArquivoOnline().equals(ServidorArquivoOnlineEnum.AMAZON_S3)) {
//				servidorExternoAmazon = new ServidorArquivoOnlineS3RS(config.getUsuarioAutenticacao(), config.getSenhaAutenticacao(), config.getNomeRepositorio());
//				caminhoArquivo = getFacadeFactory().getArquivoFacade().realizarDownloadArquivoAmazon(obj.getArquivo(), servidorExternoAmazon, config, true);
//				reader2 = new PdfReader(caminhoArquivo);
//				realizarVerificacaoAssinaturasPDFDocumentoAssinado(usuarioVO, obj, reader2);
//			}
			if (uploadedInputStream != null) {
				uploadedInputStream.close();
				uploadedInputStream = null;
			}
			realizarEscritaDebugSeiSignature(obj, realizarEscritaDebug, "(10) Inicio atualização da situação da pessoa para ASSINADO");
			for (DocumentoAssinadoPessoaVO dap : obj.getListaDocumentoAssinadoPessoa()) {
				realizarEscritaDebugSeiSignature(obj, realizarEscritaDebug, "CODIGO DOCUMENTO ASSINADO PESSOA: " + dap.getCodigo() + " ORDEM: " + dap.getOrdemAssinatura() + " ALTERAR SITUAÇÃO: " + ((provedorAssinatura.equals(Constantes.EXTERNA) && dap.getSituacaoDocumentoAssinadoPessoaEnum().isPendente() && dap.getOrdemAssinatura().equals(ordemAssinatura)) || (dap.getSituacaoDocumentoAssinadoPessoaEnum().isPendente() && dap.getPessoaVO().getCodigo().equals(usuarioVO.getPessoa().getCodigo()) && dap.getOrdemAssinatura().equals(ordemAssinatura))));
				if ((provedorAssinatura.equals(Constantes.EXTERNA) && dap.getSituacaoDocumentoAssinadoPessoaEnum().isPendente() && dap.getOrdemAssinatura().equals(ordemAssinatura)) || (dap.getSituacaoDocumentoAssinadoPessoaEnum().isPendente() && dap.getPessoaVO().getCodigo().equals(usuarioVO.getPessoa().getCodigo()) && dap.getOrdemAssinatura().equals(ordemAssinatura))) {
					try {
						if (obj.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.DIARIO) && dap.getTipoPessoa().equals(TipoPessoa.PROFESSOR) && usuarioVO.getIsApresentarVisaoProfessor()) {
							if (pemitirAssinaturaDiarioPeloProfessorAposAssinaturaCoordenadorCurso) {
								for (DocumentoAssinadoPessoaVO dap2 : obj.getListaDocumentoAssinadoPessoa()) {
									if (dap2.getSituacaoDocumentoAssinadoPessoaEnum().isPendente() && dap2.getTipoPessoa().equals(TipoPessoa.COORDENADOR_CURSO) && !dap2.getPessoaVO().getCodigo().equals(usuarioVO.getPessoa().getCodigo())) {
										if (retornarExcecaoAssinaturaCoordenadorPrimeiro) {
											throw new Exception("A assinatura do PROFESSOR só pode ser realizada após a assinatura do COORDENADOR DE CURSO.");
										} else {
											return 0;
										}
									}
								}
							}
						}
						dap.setSituacaoDocumentoAssinadoPessoaEnum(SituacaoDocumentoAssinadoPessoaEnum.ASSINADO);
						if (!Uteis.isAtributoPreenchido(dap.getDataAssinatura())) {
							dap.setDataAssinatura(new Date());
						}
						getFacadeFactory().getDocumentoAssinadoPessoaFacade().atualizarDadosAssinatura(dap, assinarPorCNPJ, ordemAssinatura, provedorAssinatura);
						realizarEscritaDebugSeiSignature(obj, realizarEscritaDebug, "(11) Foi alterado a situação do documentoAssinado da pessoa: " + dap.getPessoaVO().getCodigo() + " para assinado");
						quantidadeRegistrosAlterados++;
					} catch (Exception e) {
						dap.setSituacaoDocumentoAssinadoPessoaEnum(SituacaoDocumentoAssinadoPessoaEnum.PENDENTE);
						dap.setDataAssinatura(null);
						throw e;
					}
				}
			}
			return quantidadeRegistrosAlterados;
		} catch (Exception e) {
			throw e;
		} finally {
//			servidorExternoAmazon = null;
			if (is != null) {
				is.close();
				is = null;
			}
			if (baos != null) {
				baos.close();
				baos = null;
			}
			if (reader != null) {
				reader.close();
				reader = null;
			}
			if (reader2 != null) {
				reader2.close();
				reader2 = null;
			}
			if (uploadedInputStream != null) {
				uploadedInputStream.close();
				uploadedInputStream = null;
			}
			if (Uteis.isAtributoPreenchido(caminhoArquivo)) {
				File file = new File(caminhoArquivo);
				if (file.exists() && !file.isDirectory()) {
					file.delete();
				}
				file = null;
				caminhoArquivo = null;
			}
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public String excutarVerificacaoPessoasParaAssinarDocumento(DocumentoAssinadoVO obj, File fileAssinar, ConfiguracaoGeralSistemaVO config, boolean pemitirAssinaturaDiarioPeloProfessorAposAssinaturaCoordenadorCurso, Boolean retornarExcecaoAssinaturaCoordenadorPrimeiro, UsuarioVO usuarioVO) throws Exception {
		Uteis.checkState(obj.getTipoOrigemDocumentoAssinadoEnum().isExpedicaoDiploma() || obj.getTipoOrigemDocumentoAssinadoEnum().isXmlMec(), "Este documento só pode ser assinado usando um certificado A3, instale o programa SeiSignature e instale na sua máquina para que possa assinar este tipo de documento.");
		for (DocumentoAssinadoPessoaVO dap : obj.getListaDocumentoAssinadoPessoa()) {
			if (dap.getSituacaoDocumentoAssinadoPessoaEnum().isPendente() && dap.getPessoaVO().getCodigo().equals(usuarioVO.getPessoa().getCodigo())) {
				try {
					if(obj.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.DIARIO) && dap.getTipoPessoa().equals(TipoPessoa.PROFESSOR) && usuarioVO.getIsApresentarVisaoProfessor()) {
						if(pemitirAssinaturaDiarioPeloProfessorAposAssinaturaCoordenadorCurso) {
							for(DocumentoAssinadoPessoaVO dap2 : obj.getListaDocumentoAssinadoPessoa()){
								if(dap2.getSituacaoDocumentoAssinadoPessoaEnum().isPendente() && dap2.getTipoPessoa().equals(TipoPessoa.COORDENADOR_CURSO) && !dap2.getPessoaVO().getCodigo().equals(usuarioVO.getPessoa().getCodigo())) {
									if(retornarExcecaoAssinaturaCoordenadorPrimeiro) {
										throw new Exception("A assinatura do PROFESSOR só pode ser realizada após a assinatura do COORDENADOR DE CURSO.");									
									}else {
										return fileAssinar.getAbsolutePath().replaceAll("[\\\\]", "/");
									}
								}
							}
						}
					}
					ArquivoVO certificadoParaDocumento = validarCertificadoParaDocumento(obj.getUnidadeEnsinoVO(), null, config, usuarioVO, dap.getPessoaVO());				
					dap.setSituacaoDocumentoAssinadoPessoaEnum(SituacaoDocumentoAssinadoPessoaEnum.ASSINADO);
					dap.setDataAssinatura(new Date());
					getFacadeFactory().getDocumentoAssinadoPessoaFacade().atualizarDadosAssinatura(dap, null, null, null);
					preencherAssinadorDigitalDocumentoPorPessoa(fileAssinar.getAbsolutePath(), obj, dap, certificadoParaDocumento, dap.getPessoaVO().getSenhaCertificadoParaDocumento(), config, null,false);
				}catch (Exception e) {
					dap.setSituacaoDocumentoAssinadoPessoaEnum(SituacaoDocumentoAssinadoPessoaEnum.PENDENTE);
					dap.setDataAssinatura(null);
					throw e;
			}
		}
		}
		if (obj.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.UPLOAD_INSTITUCIONAL)) {
			atualizarApresentacaoArquivoIntitucionalAposTodosResponsaveisAssinarem(obj, usuarioVO);
		}
		return fileAssinar.getAbsolutePath().replaceAll("[\\\\]", "/");
	}
	
	private void atualizarApresentacaoArquivoIntitucionalAposTodosResponsaveisAssinarem(DocumentoAssinadoVO obj, UsuarioVO usuarioVO) throws Exception {
		boolean todosAssinaram = true;
		for (DocumentoAssinadoPessoaVO dap : obj.getListaDocumentoAssinadoPessoa()) {
			if (dap.getSituacaoDocumentoAssinadoPessoaEnum().isPendente()) {
				todosAssinaram = false;
				break;
			}
		}
		if (todosAssinaram) {
			obj.getArquivo().setApresentarPortalProfessor(true);
			obj.getArquivo().setApresentarPortalCoordenador(true);
			obj.getArquivo().setApresentarPortalAluno(true);
			obj.getArquivo().setManterDisponibilizacao(true);
			obj.getArquivo().setArquivoAssinadoDigitalmente(true);
			getFacadeFactory().getArquivoFacade().alterarApresentacaoArquivoInstitucionalProfessorCoordenadorAluno(obj.getArquivo(), false, usuarioVO);
		}
	
	}

	@Override
	public ArquivoVO validarCertificadoParaDocumento(UnidadeEnsinoVO unidadeEnsinoVO, ConfiguracaoGEDVO configuracaoGEDVO, ConfiguracaoGeralSistemaVO config, UsuarioVO usuarioVO, PessoaVO pessoaVO) throws Exception {
		List<ArquivoVO> listaCertificadoParaDocumento = getFacadeFactory().getArquivoFacade().consultarPorCodOrigemTipoOrigem(pessoaVO.getCodigo(), OrigemArquivo.CERTIFICADO_DIGITAL_PESSOA.getValor(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
		ArquivoVO certificado = null;
		if (Uteis.isAtributoPreenchido(listaCertificadoParaDocumento)) {
			certificado = listaCertificadoParaDocumento.get(0);
			File arquivoCertificado = new File(config.getLocalUploadArquivoFixo()+File.separator+certificado.getPastaBaseArquivo()+File.separator+certificado.getNome());
			if(!arquivoCertificado.exists()) {
				listaCertificadoParaDocumento.clear();
				certificado =  null;
			}else {
				try {
					CertificadoDigital.validarCertificado(config.getLocalUploadArquivoFixo()+File.separator+certificado.getPastaBaseArquivo()+File.separator+certificado.getNome(), pessoaVO.getSenhaCertificadoParaDocumento());
				}catch (Exception e) {
					listaCertificadoParaDocumento.clear();
					certificado =  null;
				}				
			}
		}
		if (certificado == null) {
			if(!Uteis.isAtributoPreenchido(configuracaoGEDVO)) {
				configuracaoGEDVO = getFacadeFactory().getConfiguracaoGEDFacade().consultarPorUnidadeEnsino(unidadeEnsinoVO.getCodigo(), false, usuarioVO);
			}
			if(!Uteis.isAtributoPreenchido(configuracaoGEDVO.getCodigo())) {
				throw new Exception("Configuração GED não foi encontrada para a Unidade de Ensino "+ unidadeEnsinoVO.getNome().toUpperCase() +". ");
			}
			
			String caminhoUnidadeCertificadora = "";
			if(Uteis.isAtributoPreenchido(configuracaoGEDVO.getCertificadoDigitalUnidadeEnsinoVO()) && Uteis.isAtributoPreenchido(configuracaoGEDVO.getSenhaCertificadoDigitalUnidadeEnsino())) {
				caminhoUnidadeCertificadora = config.getLocalUploadArquivoFixo()+File.separator+configuracaoGEDVO.getCertificadoDigitalUnidadeEnsinoVO().getPastaBaseArquivo()+File.separator+configuracaoGEDVO.getCertificadoDigitalUnidadeEnsinoVO().getNome();
			}
//			ArquivoVO file = CertificadoDigital.realizarGeracaoCertificadoPessoa(pessoaVO, unidadeEnsinoVO, config.getLocalUploadArquivoFixo(), usuarioVO, caminhoUnidadeCertificadora,  configuracaoGEDVO.getSenhaCertificadoDigitalUnidadeEnsino());
//			getFacadeFactory().getArquivoFacade().incluir(file, usuarioVO, config);
//			certificado = file;
			pessoaVO.setSenhaCertificadoParaDocumento(Uteis.removerMascara(pessoaVO.getCPF()));
			getFacadeFactory().getPessoaFacade().executarAtualizacaoSenhaCertificadoParaDocumento(pessoaVO, usuarioVO);
		}
		if (!Uteis.isAtributoPreenchido(pessoaVO.getSenhaCertificadoParaDocumento())) {
			pessoaVO.setSenhaCertificadoParaDocumento(getFacadeFactory().getPessoaFacade().consultarSenhaCertificadoParaDocumento(pessoaVO.getCodigo()));
		}
		
		return certificado;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private void preencherAssinadorDigitalDocumentoPorPessoa(String arquivoOrigem, DocumentoAssinadoVO documentoAssinado, DocumentoAssinadoPessoaVO documentoAssinadoPessoaVO, ArquivoVO certificadoParaDocumento, String senhaPessoaAssinatura, ConfiguracaoGeralSistemaVO config, ConfiguracaoGEDVO configuracaoGEDVO , Boolean validarArquivoExistente) throws Exception {

		AssinaturaDigialDocumentoPDF assinador = new AssinaturaDigialDocumentoPDF();
		assinador.setToken(true);
		assinador.setPathKeyStore(config.getLocalUploadArquivoFixo() + File.separator + certificadoParaDocumento.getPastaBaseArquivo() + File.separator + certificadoParaDocumento.getNome());
		assinador.setSenhaKeyStore(senhaPessoaAssinatura);
		assinador.setArquivoOrigem(arquivoOrigem);
		assinador.setCaminhoArquivoDestino(config.getLocalUploadArquivoFixo() + File.separator + documentoAssinado.getArquivo().getPastaBaseArquivo());
		assinador.setNomeArquivoDestino(documentoAssinado.getArquivo().getNome());
		assinador.setIsValidarArquivoExistente(validarArquivoExistente);
		assinador.setRazaoCertificado(documentoAssinado.getNumeroDocumentoAssinado());
		assinador.setDataAssinatura(new Date());
		if(Uteis.isAtributoPreenchido(documentoAssinado.getUnidadeEnsinoVO()) && Uteis.isAtributoPreenchido(documentoAssinadoPessoaVO) && Uteis.isAtributoPreenchido(documentoAssinadoPessoaVO.getOrdemAssinatura())) {
			if(!Uteis.isAtributoPreenchido(configuracaoGEDVO)) {
				configuracaoGEDVO = getFacadeFactory().getConfiguracaoGEDFacade().consultarPorUnidadeEnsino(documentoAssinado.getUnidadeEnsinoVO().getCodigo(), false, null);
			}
			if(Uteis.isAtributoPreenchido(configuracaoGEDVO)) {
			ConfiguracaoGedOrigemVO configuracaoGedOrigemVO = configuracaoGEDVO.getConfiguracaoGedOrigemVO(documentoAssinado.getTipoOrigemDocumentoAssinadoEnum());
			if(Uteis.isAtributoPreenchido(configuracaoGEDVO) && Uteis.isAtributoPreenchido(configuracaoGedOrigemVO)) {
				if(configuracaoGedOrigemVO.getApresentarAssinaturaFuncionario1() && documentoAssinadoPessoaVO.getOrdemAssinatura().equals(1)) {
					assinador.setCorAssinaturaDigitalmente("#000000");
					assinador.setAlturaAssinatura(Float.valueOf(configuracaoGedOrigemVO.getAlturaAssinaturaFuncionario1()));
					assinador.setLarguraAssinatura(Float.valueOf(configuracaoGedOrigemVO.getLarguraAssinaturaFuncionario1()));
					assinador.setCoordenadaLLX(configuracaoGedOrigemVO.getPosicaoXAssinaturaFuncionario1());
					assinador.setCoordenadaLLY(configuracaoGedOrigemVO.getPosicaoYAssinaturaFuncionario1());
					assinador.setApresentarAssinaturaUltimaPagina(configuracaoGedOrigemVO.isUltimaPaginaAssinaturaFuncionario1());
					assinador.setApresentarAssinaturaDigital(true);
				}else if(configuracaoGedOrigemVO.getApresentarAssinaturaFuncionario2() && documentoAssinadoPessoaVO.getOrdemAssinatura().equals(2)){
					assinador.setCorAssinaturaDigitalmente("#000000");
					assinador.setAlturaAssinatura(Float.valueOf(configuracaoGedOrigemVO.getAlturaAssinaturaFuncionario2()));
					assinador.setLarguraAssinatura(Float.valueOf(configuracaoGedOrigemVO.getLarguraAssinaturaFuncionario2()));
					assinador.setCoordenadaLLX(configuracaoGedOrigemVO.getPosicaoXAssinaturaFuncionario2());
					assinador.setCoordenadaLLY(configuracaoGedOrigemVO.getPosicaoYAssinaturaFuncionario2());
					assinador.setApresentarAssinaturaUltimaPagina(configuracaoGedOrigemVO.isUltimaPaginaAssinaturaFuncionario2());
					assinador.setApresentarAssinaturaDigital(true);
				}else if(configuracaoGedOrigemVO.getApresentarAssinaturaFuncionario3() && documentoAssinadoPessoaVO.getOrdemAssinatura().equals(3)) {
					assinador.setCorAssinaturaDigitalmente("#000000");
					assinador.setAlturaAssinatura(Float.valueOf(configuracaoGedOrigemVO.getAlturaAssinaturaFuncionario3()));
					assinador.setLarguraAssinatura(Float.valueOf(configuracaoGedOrigemVO.getLarguraAssinaturaFuncionario3()));
					assinador.setCoordenadaLLX(configuracaoGedOrigemVO.getPosicaoXAssinaturaFuncionario3());
					assinador.setCoordenadaLLY(configuracaoGedOrigemVO.getPosicaoYAssinaturaFuncionario3());
					assinador.setApresentarAssinaturaUltimaPagina(configuracaoGedOrigemVO.isUltimaPaginaAssinaturaFuncionario3());
					assinador.setApresentarAssinaturaDigital(true);
				}else if(configuracaoGedOrigemVO.getApresentarAssinaturaAluno() && documentoAssinadoPessoaVO.getOrdemAssinatura().equals(3)) {
					assinador.setCorAssinaturaDigitalmente("#000000");
					assinador.setAlturaAssinatura(Float.valueOf(configuracaoGedOrigemVO.getAlturaAssinaturaAluno()));
					assinador.setLarguraAssinatura(Float.valueOf(configuracaoGedOrigemVO.getLarguraAssinaturaAluno()));
					assinador.setCoordenadaLLX(configuracaoGedOrigemVO.getPosicaoXAssinaturaAluno());
					assinador.setCoordenadaLLY(configuracaoGedOrigemVO.getPosicaoYAssinaturaAluno());
					assinador.setApresentarAssinaturaUltimaPagina(configuracaoGedOrigemVO.isUltimaPaginaAssinaturaAluno());
					assinador.setApresentarAssinaturaDigital(true);
				}
				assinador.setCargo(documentoAssinadoPessoaVO.getCargo());
				assinador.setTitulo(documentoAssinadoPessoaVO.getTitulo());
				}
			if (documentoAssinado.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.CONTRATO)) {
				documentoAssinadoPessoaVO.getDadosComplementaresAssinaturaDigital();
				assinador.setIpAssinatura(documentoAssinadoPessoaVO.getIpAssinatura());
				assinador.setLatitude(documentoAssinadoPessoaVO.getLatitude());
				assinador.setLongitude(documentoAssinadoPessoaVO.getLongitude());
				assinador.setDispositivoAssinatura(documentoAssinadoPessoaVO.getDispositivoAssinatura());
			}
			}
		}
		assinador.realizarGeracaoDocumentoPdf();
	}

	public StringBuilder getSqlConsultaDadosCompletos() {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append("select  documentoassinado.codigo as \"documentoassinado.codigo\", documentoassinado.dataregistro as \"documentoassinado.dataregistro\", ");
		sqlStr.append("documentoassinado.matricula as \"documentoassinado.matricula\", documentoassinado.gradecurricular as \"documentoassinado.gradecurricular\", ");
		sqlStr.append("documentoassinado.turma as \"documentoassinado.turma\", documentoassinado.disciplina as \"documentoassinado.disciplina\", ");
		sqlStr.append("documentoassinado.ano as \"documentoassinado.ano\", documentoassinado.semestre as \"documentoassinado.semestre\",documentoassinado.matriculaPeriodo as \"documentoassinado.matriculaPeriodo\", ");
		sqlStr.append("documentoassinado.tipoorigemdocumentoassinado as \"documentoassinado.tipoorigemdocumentoassinado\", ");
		sqlStr.append("documentoassinado.provedorDeAssinaturaEnum as \"documentoassinado.provedorDeAssinaturaEnum\", ");
		sqlStr.append("documentoassinado.chaveProvedorDeAssinatura as \"documentoassinado.chaveProvedorDeAssinatura\", ");
		sqlStr.append("documentoassinado.urlProvedorDeAssinatura as \"documentoassinado.urlProvedorDeAssinatura\", ");
		sqlStr.append("documentoassinado.codigoProvedorDeAssinatura as \"documentoassinado.codigoProvedorDeAssinatura\", ");		
		sqlStr.append("documentoassinado.documentoAssinadoInvalido as \"documentoassinado.documentoAssinadoInvalido\", ");
		sqlStr.append("documentoassinado.motivoDocumentoAssinadoInvalido as \"documentoassinado.motivoDocumentoAssinadoInvalido\", documentoassinado.decisaoJudicial as \"documentoassinado.decisaoJudicial\", documentoassinado.versaoDiploma as \"documentoassinado.versaoDiploma\", documentoassinado.codigoValidacaoCurriculoEscolarDigital as \"documentoassinado.codigoValidacaoCurriculoEscolarDigital\", ");
		sqlStr.append("arquivo.codigo as \"arquivo.codigo\" , arquivo.codOrigem as \"arquivo.codOrigem\", arquivo.nome as \"arquivo.nome\", ");
		sqlStr.append("arquivo.descricao as \"arquivo.descricao\", arquivo.pastabasearquivo as \"arquivo.pastabasearquivo\", arquivo.servidorarquivoonline as \"arquivo.servidorarquivoonline\", ");
		sqlStr.append("arquivovisual.codigo as \"arquivovisual.codigo\" , arquivovisual.codOrigem as \"arquivovisual.codOrigem\", arquivovisual.nome as \"arquivovisual.nome\", ");
		sqlStr.append("arquivovisual.descricao as \"arquivovisual.descricao\", arquivovisual.pastabasearquivo as \"arquivovisual.pastabasearquivo\", ");
		sqlStr.append("arquivovisual.servidorArquivoOnline as \"arquivovisual.servidorArquivoOnline\", ");
		sqlStr.append("arquivovisual.dataUpload as \"arquivovisual.dataUpload\", ");
		sqlStr.append("disciplina.codigo as \"disciplina.codigo\",disciplina.nome as \"disciplina.nome\", ");
		sqlStr.append("turma.codigo as \"turma.codigo\",turma.identificadorTurma as \"turma.identificadorTurma\", ");
		sqlStr.append("usuario.codigo as \"usuario.codigo\",usuario.nome as \"usuario.nome\", ");
		sqlStr.append("pessoa.codigo as \"pessoa.codigo\",pessoa.nome as \"pessoa.nome\", ");
		sqlStr.append("pessoa.cpf as \"pessoa.cpf\",pessoa.dataNasc as \"pessoa.dataNasc\", ");
		sqlStr.append("unidadeEnsino.codigo as \"unidadeEnsino.codigo\", unidadeEnsino.nome as \"unidadeEnsino.nome\", ");
		sqlStr.append("unidadeEnsino.razaoSocial as \"unidadeEnsino.razaoSocial\", unidadeEnsino.cnpj as \"unidadeEnsino.cnpj\", unidadeEnsino.unidadeCertificadora as \"unidadeEnsino.unidadeCertificadora\", ");
		sqlStr.append("estagio.codigo as \"codigoOrigem\", ");
		sqlStr.append("curso.codigo as \"curso.codigo\", curso.nome as \"curso.nome\", programacaoformatura.codigo as \"programacaoformatura.codigo\", programacaoformatura.datacadastro as \"programacaoformatura.datacadastro\", colacaograu.titulo as \"colacaograu.titulo\", ");
		sqlStr.append("programacaoformatura.datalimiteassinaturaata as \"programacaoformatura.datalimiteassinaturaata\", expedicaoDiploma.codigo as \"expedicaoDiploma.codigo\", gradeCurricular.nome as \"gradeCurricular.nome\", ");
		sqlStr.append("documentoassinado.idDiplomaDigital as \"documentoassinado.idDiplomaDigital\", documentoassinado.idDadosRegistrosDiplomaDigital as \"documentoassinado.idDadosRegistrosDiplomaDigital\", unidadeEnsino.cnpjUnidadeCertificadora as \"unidadeEnsino.cnpjUnidadeCertificadora\", documentoassinado.codigoValidacaoHistoricoDigital as \"documentoassinado.codigoValidacaoHistoricoDigital\", ");
		sqlStr.append("documentoassinado.erro \"documentoassinado.erro\", documentoassinado.motivoErro \"documentoassinado.motivoErro\" ");
		sqlStr.append("from documentoassinado ");
		sqlStr.append("inner join arquivo on arquivo.codigo = documentoassinado.arquivo ");
		sqlStr.append("left join arquivo arquivovisual on arquivovisual.codigo = documentoassinado.arquivovisual ");
		sqlStr.append("left join unidadeEnsino on documentoassinado.unidadeEnsino = unidadeEnsino.codigo ");
		sqlStr.append("left join disciplina on documentoassinado.disciplina = disciplina.codigo ");
		sqlStr.append("left join turma on documentoassinado.turma = turma.codigo ");
		sqlStr.append("left join usuario on documentoassinado.usuario = usuario.codigo ");
		sqlStr.append("left join matricula on documentoassinado.matricula = matricula.matricula ");
		sqlStr.append("left join pessoa  on matricula.aluno = pessoa.codigo ");
		sqlStr.append("left join curso on curso.codigo = documentoassinado.curso ");
		sqlStr.append("left join programacaoformatura on programacaoformatura.codigo = documentoassinado.programacaoformatura ");
		sqlStr.append("left join estagio on documentoassinado.codigo = estagio.documentoassinado ");
		sqlStr.append("left join colacaograu on colacaograu.codigo = programacaoformatura.colacaograu ");
		sqlStr.append("left join gradeCurricular ON gradeCurricular.codigo = documentoassinado.gradeCurricular ");
		sqlStr.append("left join expedicaoDiploma ON expedicaoDiploma.codigo = documentoassinado.expedicaoDiploma ");
		return sqlStr;
	}

	public StringBuilder getSqlConsultaDadosArquivoAssinado() {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append("select  distinct documentoassinado.codigo as \"documentoassinado.codigo\", documentoassinado.dataregistro as \"documentoassinado.dataregistro\", ");
		sqlStr.append("documentoassinado.matricula as \"documentoassinado.matricula\", documentoassinado.gradecurricular as \"documentoassinado.gradecurricular\", ");
		sqlStr.append("documentoassinado.turma as \"documentoassinado.turma\", documentoassinado.disciplina as \"documentoassinado.disciplina\", ");
		sqlStr.append("documentoassinado.chaveProvedorDeAssinatura as \"documentoassinado.chaveProvedorDeAssinatura\", ");
		sqlStr.append("documentoassinado.urlProvedorDeAssinatura as \"documentoassinado.urlProvedorDeAssinatura\", ");
		sqlStr.append("documentoassinado.codigoProvedorDeAssinatura as \"documentoassinado.codigoProvedorDeAssinatura\", ");
		sqlStr.append("documentoassinado.ano as \"documentoassinado.ano\", documentoassinado.semestre as \"documentoassinado.semestre\", documentoassinado.matriculaPeriodo as \"documentoassinado.matriculaPeriodo\", ");
		sqlStr.append("documentoassinado.tipoorigemdocumentoassinado as \"documentoassinado.tipoorigemdocumentoassinado\", documentoassinado.provedordeassinaturaenum  as  \"documentoassinado.provedordeassinaturaenum\" , documentoassinado.codigoValidacaoCurriculoEscolarDigital as \"documentoassinado.codigoValidacaoCurriculoEscolarDigital\", ");
		sqlStr.append("arquivo.codigo as \"arquivo.codigo\" , arquivo.codOrigem as \"arquivo.codOrigem\", arquivo.nome as \"arquivo.nome\", ");
		sqlStr.append("arquivo.apresentarPortalCoordenador as \"arquivo.apresentarPortalCoordenador\", arquivo.apresentarPortalProfessor as \"arquivo.apresentarPortalProfessor\", arquivo.apresentarPortalAluno  as \"arquivo.apresentarPortalAluno\", ");
		sqlStr.append("arquivo.descricao as \"arquivo.descricao\", arquivo.dataupload as \"arquivo.dataupload\", arquivo.dataDisponibilizacao as \"arquivo.dataDisponibilizacao\", arquivo.dataIndisponibilizacao as \"arquivo.dataIndisponibilizacao\", ");
		sqlStr.append("arquivo.manterDisponibilizacao as \"arquivo.manterDisponibilizacao\", arquivo.origem as \"arquivo.origem\",arquivo.situacao as \"arquivo.situacao\", arquivo.controlardownload as \"arquivo.controlardownload\", ");
		sqlStr.append("arquivo.responsavelupload as \"arquivo.responsavelupload\", arquivo.disciplina as \"arquivo.disciplina\", arquivo.turma as \"arquivo.turma\", arquivo.extensao as \"arquivo.extensao\", arquivo.apresentarDeterminadoPeriodo as \"arquivo.apresentarDeterminadoPeriodo\", ");
		sqlStr.append("arquivo.permitirArquivoResposta as \"arquivo.permitirArquivoResposta\", arquivo.pastabasearquivo as \"arquivo.pastabasearquivo\", arquivo.arquivoResposta as \"arquivo.arquivoResposta\", arquivo.pessoa as \"arquivo.pessoa\", ");
		sqlStr.append("arquivo.professor as \"arquivo.professor\", arquivo.nivelEducacional as \"arquivo.nivelEducacional\", arquivo.cpfAlunoDocumentacao as \"arquivo.cpfAlunoDocumentacao\", arquivo.cpfRequerimento as \"arquivo.cpfRequerimento\", ");
		sqlStr.append("arquivo.indice as \"arquivo.indice\", arquivo.agrupador as \"arquivo.agrupador\", arquivo.indiceagrupador as \"arquivo.indiceagrupador\", arquivo.arquivoAssinadoDigitalmente as \"arquivo.arquivoAssinadoDigitalmente\", ");
		sqlStr.append("arquivo.curso as \"arquivo.curso\", arquivo.descricaoArquivo as \"arquivo.descricaoArquivo\", ");
		sqlStr.append("unidadeEnsino.codigo as \"unidadeEnsino.codigo\",unidadeEnsino.nome as \"unidadeEnsino.nome\", ");
		sqlStr.append("unidadeEnsino.razaoSocial as \"unidadeEnsino.razaoSocial\", unidadeEnsino.cnpj as \"unidadeEnsino.cnpj\", ");
		sqlStr.append("disciplina.codigo as \"disciplina.codigo\",disciplina.nome as \"disciplina.nome\", ");
		sqlStr.append("turma.codigo as \"turma.codigo\",turma.identificadorTurma as \"turma.identificadorTurma\", ");
		sqlStr.append("usuario.codigo as \"usuario.codigo\",usuario.nome as \"usuario.nome\", ");
		sqlStr.append("professor.codigo as \"professor.codigo\",professor.nome as \"professor.nome\", ");
		sqlStr.append("pessoa.codigo as \"pessoa.codigo\",pessoa.nome as \"pessoa.nome\", ");
		sqlStr.append("pessoa.cpf as \"pessoa.cpf\",pessoa.dataNasc as \"pessoa.dataNasc\", ");
		sqlStr.append(" curso.codigo as \"curso.codigo\",curso.nome as \"curso.nome\", ");
		sqlStr.append(" programacaoformatura.codigo as \"programacaoformatura.codigo\", programacaoformatura.datalimiteassinaturaata as \"programacaoformatura.datalimiteassinaturaata\", gradeCurricular.nome as \"gradeCurricular.nome\", cursoDocumentoAssinado.codigo as \"cursoDocumentoAssinado.codigo\", cursoDocumentoAssinado.nome as \"cursoDocumentoAssinado.nome\", ");
		sqlStr.append("arquivovisual.codigo as \"arquivovisual.codigo\" , arquivovisual.codOrigem as \"arquivovisual.codOrigem\", arquivovisual.nome as \"arquivovisual.nome\", ");
		sqlStr.append("arquivovisual.apresentarPortalCoordenador as \"arquivovisual.apresentarPortalCoordenador\", arquivovisual.apresentarPortalProfessor as \"arquivovisual.apresentarPortalProfessor\", arquivovisual.apresentarPortalAluno  as \"arquivovisual.apresentarPortalAluno\", ");
		sqlStr.append("arquivovisual.descricao as \"arquivovisual.descricao\", arquivovisual.dataupload as \"arquivovisual.dataupload\", arquivovisual.dataDisponibilizacao as \"arquivovisual.dataDisponibilizacao\", arquivovisual.dataIndisponibilizacao as \"arquivovisual.dataIndisponibilizacao\", ");
		sqlStr.append("arquivovisual.manterDisponibilizacao as \"arquivovisual.manterDisponibilizacao\", arquivovisual.origem as \"arquivovisual.origem\",arquivovisual.situacao as \"arquivovisual.situacao\", arquivovisual.controlardownload as \"arquivovisual.controlardownload\", ");
		sqlStr.append("arquivovisual.responsavelupload as \"arquivovisual.responsavelupload\", arquivovisual.disciplina as \"arquivovisual.disciplina\", arquivovisual.turma as \"arquivovisual.turma\", arquivovisual.extensao as \"arquivovisual.extensao\", arquivovisual.apresentarDeterminadoPeriodo as \"arquivovisual.apresentarDeterminadoPeriodo\", ");
		sqlStr.append("arquivovisual.permitirArquivoResposta as \"arquivovisual.permitirArquivoResposta\", arquivovisual.pastabasearquivo as \"arquivovisual.pastabasearquivo\", arquivovisual.arquivoResposta as \"arquivovisual.arquivoResposta\", arquivovisual.pessoa as \"arquivovisual.pessoa\", ");
		sqlStr.append("arquivovisual.professor as \"arquivovisual.professor\", arquivovisual.nivelEducacional as \"arquivovisual.nivelEducacional\", arquivovisual.cpfAlunoDocumentacao as \"arquivovisual.cpfAlunoDocumentacao\", arquivovisual.cpfRequerimento as \"arquivovisual.cpfRequerimento\", ");
		sqlStr.append("arquivovisual.indice as \"arquivovisual.indice\", arquivovisual.agrupador as \"arquivovisual.agrupador\", arquivovisual.indiceagrupador as \"arquivovisual.indiceagrupador\", arquivovisual.arquivoAssinadoDigitalmente as \"arquivovisual.arquivoAssinadoDigitalmente\", ");
		sqlStr.append("arquivovisual.curso as \"arquivovisual.curso\", arquivovisual.descricaoArquivo as \"arquivovisual.descricaoArquivo\" ");
		sqlStr.append("from arquivo ");
		sqlStr.append("left join documentoassinado on arquivo.codigo = documentoassinado.arquivo ");
		sqlStr.append("left join unidadeEnsino on documentoassinado.unidadeEnsino = unidadeEnsino.codigo ");
		sqlStr.append("left join disciplina on arquivo.disciplina = disciplina.codigo ");
		sqlStr.append("left join turma on arquivo.turma = turma.codigo ");
		sqlStr.append("left join usuario on arquivo.responsavelupload = usuario.codigo ");
		sqlStr.append("left join pessoa professor on arquivo.professor = professor.codigo ");
		sqlStr.append("left join pessoa  on arquivo.pessoa = pessoa.codigo ");
		sqlStr.append("left join curso on arquivo.curso = curso.codigo ");
		sqlStr.append("LEFT JOIN matricula matriculaDocumentoAssinado ON documentoassinado.matricula = matriculaDocumentoAssinado.matricula ");
		sqlStr.append("LEFT JOIN curso cursoMatricula ON matriculaDocumentoAssinado.curso = cursoMatricula.codigo ");
		sqlStr.append("LEFT JOIN programacaoformatura ON programacaoformatura.codigo = documentoassinado.programacaoformatura ");
		sqlStr.append("left join gradeCurricular on gradeCurricular.codigo = documentoassinado.gradeCurricular ");
		sqlStr.append("left join curso cursoDocumentoAssinado on cursoDocumentoAssinado.codigo = documentoassinado.curso ");
		sqlStr.append("LEFT JOIN arquivo arquivovisual ON arquivovisual.codigo = documentoassinado.arquivovisual ");
		//sqlStr.append("left join matricula on arquivo.pessoa = matricula.aluno ");
		return sqlStr;
	}

	public StringBuilder getSqlConsultarTotalRegistroArquivoAssinado() {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append("select count(distinct documentoassinado.codigo) as total ");
		sqlStr.append("from arquivo ");
		sqlStr.append("left join documentoassinado on arquivo.codigo = documentoassinado.arquivo ");
		sqlStr.append("left join unidadeEnsino on documentoassinado.unidadeEnsino = unidadeEnsino.codigo ");
		sqlStr.append("left join disciplina on arquivo.disciplina = disciplina.codigo ");
		sqlStr.append("left join turma on arquivo.turma = turma.codigo ");
		sqlStr.append("left join usuario on arquivo.responsavelupload = usuario.codigo ");
		sqlStr.append("left join pessoa professor on arquivo.professor = professor.codigo ");
		sqlStr.append("left join pessoa  on arquivo.pessoa = pessoa.codigo ");
		sqlStr.append("left join curso on arquivo.curso = curso.codigo ");
		sqlStr.append("left join matricula on arquivo.pessoa = matricula.aluno ");
		return sqlStr;
	}

	public StringBuilder getSqlConsultaDadosBasico() {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append("select  documentoassinado.codigo as \"documentoassinado.codigo\", documentoassinado.dataregistro as \"documentoassinado.dataregistro\", ");
		sqlStr.append("documentoassinado.matricula as \"documentoassinado.matricula\", documentoassinado.gradecurricular as \"documentoassinado.gradecurricular\", ");
		sqlStr.append("documentoassinado.turma as \"documentoassinado.turma\", documentoassinado.disciplina as \"documentoassinado.disciplina\", ");
		sqlStr.append("documentoassinado.ano as \"documentoassinado.ano\", documentoassinado.semestre as \"documentoassinado.semestre\", documentoassinado.matriculaPeriodo as \"documentoassinado.matriculaPeriodo\",");
		sqlStr.append("documentoassinado.tipoorigemdocumentoassinado as \"documentoassinado.tipoorigemdocumentoassinado\", ");
		sqlStr.append("documentoassinado.provedorDeAssinaturaEnum as \"documentoassinado.provedorDeAssinaturaEnum\", ");
		sqlStr.append("documentoassinado.chaveProvedorDeAssinatura as \"documentoassinado.chaveProvedorDeAssinatura\", ");
		sqlStr.append("documentoassinado.urlProvedorDeAssinatura as \"documentoassinado.urlProvedorDeAssinatura\", ");
		sqlStr.append("documentoassinado.codigoProvedorDeAssinatura as \"documentoassinado.codigoProvedorDeAssinatura\", ");
		sqlStr.append("documentoassinado.documentoAssinadoInvalido as \"documentoassinado.documentoAssinadoInvalido\", ");
		sqlStr.append("documentoassinado.motivoDocumentoAssinadoInvalido as \"documentoassinado.motivoDocumentoAssinadoInvalido\", documentoassinado.decisaoJudicial as \"documentoassinado.decisaoJudicial\", documentoassinado.versaoDiploma as \"documentoassinado.versaoDiploma\", documentoassinado.codigoValidacaoCurriculoEscolarDigital as \"documentoassinado.codigoValidacaoCurriculoEscolarDigital\", ");
		sqlStr.append("arquivo.codigo as \"arquivo.codigo\" , arquivo.codOrigem as \"arquivo.codOrigem\", arquivo.nome as \"arquivo.nome\", ");
		sqlStr.append("arquivovisual.codigo as \"arquivovisual.codigo\" , arquivovisual.codOrigem as \"arquivovisual.codOrigem\", arquivovisual.nome as \"arquivovisual.nome\", ");
		sqlStr.append("arquivovisual.descricao as \"arquivovisual.descricao\", arquivovisual.pastabasearquivo as \"arquivovisual.pastabasearquivo\", ");
		sqlStr.append("arquivovisual.servidorArquivoOnline as \"arquivovisual.servidorArquivoOnline\", ");
		sqlStr.append("arquivovisual.dataUpload as \"arquivovisual.dataUpload\", ");
		sqlStr.append("arquivo.descricao as \"arquivo.descricao\", arquivo.pastabasearquivo as \"arquivo.pastabasearquivo\",  arquivo.servidorarquivoonline as \"arquivo.servidorarquivoonline\", ");
		sqlStr.append("disciplina.codigo as \"disciplina.codigo\",disciplina.nome as \"disciplina.nome\", ");
		sqlStr.append("turma.codigo as \"turma.codigo\",turma.identificadorTurma as \"turma.identificadorTurma\", ");
		sqlStr.append("usuario.codigo as \"usuario.codigo\",usuario.nome as \"usuario.nome\", ");
		sqlStr.append("pessoa.codigo as \"pessoa.codigo\",pessoa.nome as \"pessoa.nome\", ");
		sqlStr.append("pessoa.cpf as \"pessoa.cpf\",pessoa.dataNasc as \"pessoa.dataNasc\" ,  ");
		sqlStr.append("unidadeEnsino.codigo as \"unidadeEnsino.codigo\", unidadeEnsino.nome as \"unidadeEnsino.nome\", ");
		sqlStr.append("unidadeEnsino.razaoSocial as \"unidadeEnsino.razaoSocial\", unidadeEnsino.cnpj as \"unidadeEnsino.cnpj\", unidadeEnsino.unidadeCertificadora as \"unidadeEnsino.unidadeCertificadora\", ");
		sqlStr.append("estagio.codigo as \"codigoOrigem\", ");
		sqlStr.append("curso.codigo as \"curso.codigo\", curso.nome as \"curso.nome\",  curso.ativarPreMatriculaAposEntregaDocumentosObrigatorios as \"curso.ativarPreMatriculaAposEntregaDocumentosObrigatorios\" , curso.ativarMatriculaAposAssinaturaContrato as \"curso.ativarMatriculaAposAssinaturaContrato\" ,   programacaoformatura.codigo as \"programacaoformatura.codigo\", programacaoformatura.datacadastro as \"programacaoformatura.datacadastro\", colacaograu.titulo as \"colacaograu.titulo\", programacaoformatura.dataLimiteAssinaturaAta as \"programacaoformatura.dataLimiteAssinaturaAta\", ");
		sqlStr.append("expedicaoDiploma.codigo as \"expedicaoDiploma.codigo\", gradeCurricular.nome as \"gradeCurricular.nome\", ");
		sqlStr.append("documentoassinado.idDiplomaDigital as \"documentoassinado.idDiplomaDigital\", documentoassinado.idDadosRegistrosDiplomaDigital as \"documentoassinado.idDadosRegistrosDiplomaDigital\", unidadeEnsino.cnpjUnidadeCertificadora as \"unidadeEnsino.cnpjUnidadeCertificadora\", documentoassinado.codigoValidacaoHistoricoDigital as \"documentoassinado.codigoValidacaoHistoricoDigital\", ");
		sqlStr.append("documentoassinado.erro \"documentoassinado.erro\", documentoassinado.motivoErro \"documentoassinado.motivoErro\" ");
		sqlStr.append("from documentoassinado ");
		sqlStr.append("inner join arquivo on arquivo.codigo = documentoassinado.arquivo ");
		sqlStr.append("left join arquivo arquivovisual on arquivovisual.codigo = documentoassinado.arquivovisual ");
		sqlStr.append("left join unidadeEnsino on documentoassinado.unidadeEnsino = unidadeEnsino.codigo ");
		sqlStr.append("left join disciplina on documentoassinado.disciplina = disciplina.codigo ");
		sqlStr.append("left join turma on documentoassinado.turma = turma.codigo ");
		sqlStr.append("left join usuario on documentoassinado.usuario = usuario.codigo ");
		sqlStr.append("left join matricula on documentoassinado.matricula = matricula.matricula ");
		sqlStr.append("left join pessoa  on matricula.aluno = pessoa.codigo ");
		sqlStr.append("left join curso  on documentoassinado.curso = curso.codigo ");
		sqlStr.append("left join programacaoFormatura  on documentoassinado.programacaoFormatura = programacaoFormatura.codigo ");
		sqlStr.append("left join estagio on documentoassinado.codigo = estagio.documentoassinado ");
		sqlStr.append("left join colacaograu on colacaograu.codigo = programacaoformatura.colacaograu ");
		sqlStr.append("left join expedicaoDiploma ON expedicaoDiploma.codigo = documentoassinado.expedicaoDiploma ");
		sqlStr.append("left join gradeCurricular ON gradeCurricular.codigo = documentoassinado.gradeCurricular ");
		return sqlStr;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<DocumentoAssinadoVO> consultarDocumentosAssinadoPorRelatorio(DocumentoAssinadoVO obj, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado, List<TipoOrigemDocumentoAssinadoEnum> tipoOrigemDocumentoAssinadoEnums, Integer limit) throws Exception {
		return consultarDocumentosAssinadoPorRelatorio(obj, Boolean.FALSE, controlarAcesso, nivelMontarDados, usuarioLogado, tipoOrigemDocumentoAssinadoEnums, limit);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<DocumentoAssinadoVO> consultarDocumentosAssinadoPorRelatorio(DocumentoAssinadoVO obj, boolean apresentarApenasDocumentoValido, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado, List<TipoOrigemDocumentoAssinadoEnum> tipoOrigemDocumentoAssinadoEnums, Integer limit) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuarioLogado);
		StringBuilder sqlStr = getSqlConsultaDadosBasico();
		sqlStr.append(" WHERE 1=1 ");
		if (Uteis.isAtributoPreenchido(obj.getTipoOrigemDocumentoAssinadoEnum())) {
			sqlStr.append(" and documentoassinado.tipoOrigemDocumentoAssinado  = '").append(obj.getTipoOrigemDocumentoAssinadoEnum()).append("' ");
		} else if (Uteis.isAtributoPreenchido(tipoOrigemDocumentoAssinadoEnums)) {
			sqlStr.append(" and documentoassinado.tipoOrigemDocumentoAssinado IN (");
			for (TipoOrigemDocumentoAssinadoEnum tipoOrigemDocumentoAssinadoEnum : tipoOrigemDocumentoAssinadoEnums) {
				sqlStr.append("'").append(tipoOrigemDocumentoAssinadoEnum).append("', ");
			}
			sqlStr.append("'') ");
		}
		if (Uteis.isAtributoPreenchido(obj.getMatricula().getMatricula())) {
			sqlStr.append(" and documentoassinado.matricula = '").append(obj.getMatricula().getMatricula()).append("' ");
		}
		if (Uteis.isAtributoPreenchido(obj.getAno())) {
			sqlStr.append(" and documentoassinado.ano = '").append(obj.getAno()).append("' ");
		}
		if (Uteis.isAtributoPreenchido(obj.getSemestre())) {
			sqlStr.append(" and documentoassinado.semestre = '").append(obj.getSemestre()).append("' ");
		}
		if (Uteis.isAtributoPreenchido(obj.getTurma().getCodigo())) {
			sqlStr.append(" and documentoassinado.turma = ").append(obj.getTurma().getCodigo()).append(" ");
		}
		if (Uteis.isAtributoPreenchido(obj.getDisciplina().getCodigo())) {
			sqlStr.append(" and documentoassinado.turma = ").append(obj.getDisciplina().getCodigo()).append(" ");
		}
		if (Uteis.isAtributoPreenchido(obj.getGradeCurricular().getCodigo())) {
			sqlStr.append(" and documentoassinado.gradecurricular = ").append(obj.getGradeCurricular().getCodigo()).append(" ");
		}
		if (Uteis.isAtributoPreenchido(obj.getExpedicaoDiplomaVO())) {
			sqlStr.append(" and documentoassinado.expedicaoDiploma = ").append(obj.getExpedicaoDiplomaVO().getCodigo()).append(" ");
		}
		if (Uteis.isAtributoPreenchido(obj.getCursoVO())) {
			sqlStr.append(" and documentoassinado.curso = ").append(obj.getCursoVO().getCodigo()).append(" ");
		}
		if (Uteis.isAtributoPreenchido(obj.getUnidadeEnsinoVO())) {
			sqlStr.append(" and documentoassinado.unidadeensino = ").append(obj.getUnidadeEnsinoVO().getCodigo()).append(" ");
		}
		if (apresentarApenasDocumentoValido) {
			sqlStr.append(" and documentoassinado.documentoassinadoinvalido = false ");
		}
		sqlStr.append(" ORDER BY arquivo.descricao DESC ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuarioLogado));
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<DocumentoAssinadoVO> consultarDocumentosAssinadoPorSeiSignature(int nivelMontarDados, UsuarioVO usuarioLogado, String tipoDocumentoAssinado, Integer ordemAssinaturaFiltrar) throws Exception {
		StringBuilder sqlStr = getSqlConsultaDadosBasico();
		sqlStr.append(" LEFT JOIN LATERAL (SELECT d.ordemassinatura FROM documentoassinadopessoa d WHERE d.documentoassinado = documentoassinado.codigo AND d.situacaodocumentoassinadopessoa = 'PENDENTE' ORDER BY d.ordemassinatura LIMIT 1 ) AS pessoaAssinar ON 1 = 1 ");
		sqlStr.append(" WHERE provedordeassinaturaenum = 'SEI' ");
		montarFiltrosParaConsultaDocumentos(new UnidadeEnsinoVO(), new CursoVO(), new TurmaVO(), new DisciplinaVO(), Constantes.EMPTY, Constantes.EMPTY, new MatriculaVO(), Uteis.isAtributoPreenchido(tipoDocumentoAssinado) ? tipoDocumentoAssinado : "NENHUM", null, null, SituacaoDocumentoAssinadoPessoaEnum.PENDENTE, usuarioLogado.getPessoa(), sqlStr, true, true, Uteis.isAtributoPreenchido(ordemAssinaturaFiltrar) ? ordemAssinaturaFiltrar : null);
		sqlStr.append(" order by documentoassinado.dataregistro ");
		List<DocumentoAssinadoVO> documentoAssinadoVOs = montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString()), nivelMontarDados, usuarioLogado);
		for (DocumentoAssinadoVO documentoAssinadoVO : documentoAssinadoVOs) {
			ConfiguracaoGEDVO configuracaoGEDVO = getAplicacaoControle().getConfiguracaoGEDPorUnidadeEnsino(documentoAssinadoVO.getUnidadeEnsinoVO().getCodigo(), usuarioLogado);
			if (Uteis.isAtributoPreenchido(configuracaoGEDVO)) {
				documentoAssinadoVO.setConfiguracaoGedOrigemVO((ConfiguracaoGedOrigemVO) configuracaoGEDVO.getConfiguracaoGedOrigemVO(documentoAssinadoVO.getTipoOrigemDocumentoAssinadoEnum()).clone());
			}
			Ordenacao.ordenarLista(documentoAssinadoVO.getListaDocumentoAssinadoPessoa(), "codigo");
			if (documentoAssinadoVO.getListaDocumentoAssinadoPessoa().stream().anyMatch(t -> t.getPessoaVO().getCodigo().equals(usuarioLogado.getPessoa().getCodigo())) && documentoAssinadoVO.getListaDocumentoAssinadoPessoa().stream().filter(t -> t.getSituacaoDocumentoAssinadoPessoaEnum().equals(SituacaoDocumentoAssinadoPessoaEnum.PENDENTE) && t.getPessoaVO().getCodigo().equals(usuarioLogado.getPessoa().getCodigo())).findFirst().get().getOrdemAssinatura() > 0) {
				documentoAssinadoVO.setOrdemAssinatura(documentoAssinadoVO.getListaDocumentoAssinadoPessoa().stream().filter(t -> t.getSituacaoDocumentoAssinadoPessoaEnum().equals(SituacaoDocumentoAssinadoPessoaEnum.PENDENTE) && t.getPessoaVO().getCodigo().equals(usuarioLogado.getPessoa().getCodigo())).findFirst().get().getOrdemAssinatura());
			}
		}
		return documentoAssinadoVOs;
	}
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<DocumentoAssinadoVO> consultarDocumentosAssinadoPendentePorPeriodo(int nivelMontarDados, UsuarioVO usuarioLogado , Date periodoInicial , Date periodoFinal) throws Exception {
		StringBuilder sqlStr = getSqlConsultaDadosBasico();
		sqlStr.append(" WHERE 1=1 ");
		montarFiltrosParaConsultaDocumentos(new UnidadeEnsinoVO(), new CursoVO(), new TurmaVO(), new DisciplinaVO(), "", "", new MatriculaVO(), TipoOrigemDocumentoAssinadoEnum.NENHUM.name(), periodoInicial, periodoFinal, SituacaoDocumentoAssinadoPessoaEnum.PENDENTE, new PessoaVO(),  sqlStr, false, false, null);
		sqlStr.append(" order by documentoassinado.dataregistro ");
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString()), nivelMontarDados, usuarioLogado); 
	}

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
    public List<DocumentoAssinadoVO> consultarDocumentosAssinadoPendentePorPeriodoTechCert(int nivelMontarDados, UsuarioVO usuarioLogado , Date periodoInicial , Date periodoFinal) throws Exception {
        StringBuilder sqlStr = getSqlConsultaDadosBasico();
        sqlStr.append(" WHERE 1=1 ");
        sqlStr.append(" and documentoassinado.provedordeassinaturaenum ilike '%TECHCERT%'");
        montarFiltrosParaConsultaDocumentos(new UnidadeEnsinoVO(), new CursoVO(), new TurmaVO(), new DisciplinaVO(), "", "", new MatriculaVO(), TipoOrigemDocumentoAssinadoEnum.NENHUM.name(), periodoInicial, periodoFinal, SituacaoDocumentoAssinadoPessoaEnum.PENDENTE, new PessoaVO(),  sqlStr, false, false, null);
        sqlStr.append(" order by documentoassinado.dataregistro ");
        return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString()), nivelMontarDados, usuarioLogado);
    }

	@Override
	public List<DocumentoAssinadoVO> consultarArquivoAssinado(String campoConsulta, String valorConsulta, int limite, int pagina, boolean controlarAcesso, int nivelMontarDados, boolean arquivoAssinadoDigitalmente, List<TipoOrigemDocumentoAssinadoEnum> listaTipoOrigemDocumentoAssinadoEnum, SituacaoDocumentoAssinadoPessoaEnum situacaoDocumentoAssinadoPessoaEnum, UsuarioVO usuarioLogado, Boolean trazerDocumentosDigitais, Boolean trazerDocumentosAssinadosInvalidos) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuarioLogado);
		StringBuilder sqlStr = getSqlConsultaDadosArquivoAssinado();
		montarFiltrosParaConsultaArquivoAssinados(sqlStr, campoConsulta, valorConsulta, listaTipoOrigemDocumentoAssinadoEnum, situacaoDocumentoAssinadoPessoaEnum, arquivoAssinadoDigitalmente, true, trazerDocumentosDigitais, trazerDocumentosAssinadosInvalidos);
		if (limite != 0) {
			sqlStr.append(" limit ");
			sqlStr.append(limite);
			sqlStr.append(" offset ");
			sqlStr.append(pagina);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsultaArquivoAssinado(tabelaResultado, nivelMontarDados, usuarioLogado));

	}

	@Override
	public Integer consultarTotalRegistroArquivoAssinados(String campoConsulta, String valorConsulta, Boolean controlarAcesso, int nivelMontarDados, boolean arquivoAssinadoDigitalmente, List<TipoOrigemDocumentoAssinadoEnum> listaTipoOrigemDocumentoAssinadoEnum, SituacaoDocumentoAssinadoPessoaEnum situacaoDocumentoAssinadoPessoaEnum, UsuarioVO usuarioLogado, Boolean trazerDocumentosDigitais, Boolean trazerDocumentosAssinadosInvalidos) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuarioLogado);
		StringBuilder sqlStr = getSqlConsultarTotalRegistroArquivoAssinado();
		montarFiltrosParaConsultaArquivoAssinados(sqlStr, campoConsulta, valorConsulta, listaTipoOrigemDocumentoAssinadoEnum, situacaoDocumentoAssinadoPessoaEnum, arquivoAssinadoDigitalmente, false, trazerDocumentosDigitais, trazerDocumentosAssinadosInvalidos);
		Integer totalRegistro = 0;
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (rs.next()) {
			totalRegistro = rs.getInt("total");
		}
		return totalRegistro;
	}

	public void montarFiltrosParaConsultaArquivoAssinados(StringBuilder sqlStr, String campoConsulta, String valorConsulta, List<TipoOrigemDocumentoAssinadoEnum> listaTipoOrigemDocumentoAssinadoEnum,SituacaoDocumentoAssinadoPessoaEnum situacaoDocumentoAssinadoPessoaEnum, boolean arquivoAssinadoDigitalmente, boolean isOrderBy, Boolean trazerDocumentosDigitais, Boolean trazerDocumentosAssinadosInvalidos) {		
		if(Uteis.isAtributoPreenchido(situacaoDocumentoAssinadoPessoaEnum)) {
			sqlStr.append(" INNER JOIN LATERAL ( SELECT COALESCE(re.total,0) AS total  FROM ( SELECT COUNT(d.codigo) AS total FROM documentoassinadopessoa d WHERE d.documentoassinado = documentoassinado.codigo AND d.situacaodocumentoassinadopessoa = 'ASSINADO' AND documentoassinado.tipoorigemdocumentoassinado NOT IN ('REQUERIMENTO', 'IMPOSTO_RENDA') ");
			sqlStr.append(" UNION ALL SELECT CASE WHEN documentoassinado.tipoorigemdocumentoassinado IN ('REQUERIMENTO', 'IMPOSTO_RENDA') THEN 1 ELSE 0 END AS total ");
			sqlStr.append(" UNION ALL SELECT CASE WHEN documentoassinado.tipoorigemdocumentoassinado IN ('DECLARACAO','HISTORICO','PLANO_DE_ENSINO') and arquivo.arquivoassinadodigitalmente ");
			sqlStr.append(" and not exists (select from documentoassinadopessoa d2 where d2.documentoassinado = documentoassinado.codigo) then 1 else 0 end as total ) as re where re.total > 0 ) as documentoassinadopessoa on 1=1 ");
		}		
		sqlStr.append(" WHERE arquivo.situacao = 'AT' ");
		if (arquivoAssinadoDigitalmente) {
			sqlStr.append(" AND (( arquivo.arquivoAssinadoDigitalmente = true ");
			if(Uteis.isAtributoPreenchido(situacaoDocumentoAssinadoPessoaEnum)) {
				sqlStr.append(" and (not exists ( select documentoassinadopessoa.codigo from documentoassinadopessoa where documentoassinadopessoa.documentoassinado = documentoassinado.codigo ");
				sqlStr.append(" and documentoassinadopessoa.situacaodocumentoassinadopessoa != '").append(situacaoDocumentoAssinadoPessoaEnum.name()).append("' ");					
				sqlStr.append(" ) or not exists(select documentoassinadopessoa.codigo from documentoassinadopessoa where documentoassinadopessoa.documentoassinado = documentoassinado.codigo )) ");
			}
			sqlStr.append(" ) ");
			if (campoConsulta.equals("documentoInstitucional")) {
				sqlStr.append(" or arquivo.arquivoAssinadoDigitalmente = false ");
			}
			sqlStr.append(" ) ");
		}
		if(Uteis.isAtributoPreenchido(listaTipoOrigemDocumentoAssinadoEnum)) {
			String output = listaTipoOrigemDocumentoAssinadoEnum.stream().map(p-> String.valueOf(p)).collect(Collectors.joining("','", "'", "'"));
			sqlStr.append(" and documentoassinado.tipoorigemdocumentoassinado in (").append(output).append(")");
		}
		
		if (!trazerDocumentosDigitais) {
			sqlStr.append(" and documentoassinado.tipoorigemdocumentoassinado <> 'HISTORICO_DIGITAL' and documentoassinado.tipoorigemdocumentoassinado <> 'DIPLOMA_DIGITAL' ");
		}
				
		if (!trazerDocumentosAssinadosInvalidos) {
			sqlStr.append(" and (documentoassinado.documentoAssinadoInvalido = false or documentoassinado.documentoAssinadoInvalido is null)");
		}
		
		if (campoConsulta.equals("codigo")) {
			BigInteger valorInt = new BigInteger("0");
			if (valorConsulta != null && !valorConsulta.equals(Constantes.EMPTY)) {
				valorInt = new BigInteger(StringUtils.trim(valorConsulta));
			}
			sqlStr.append(" and documentoassinado.codigo = ").append(valorInt.longValue());
			if (isOrderBy) {
				sqlStr.append(" ORDER BY arquivo.codigo DESC ");
			}
		} else if (campoConsulta.equals("descricao")) {
			sqlStr.append(" and upper( sem_acentos(arquivo.descricao)) like('").append(Uteis.removerAcentuacao(valorConsulta.toUpperCase())).append("%') ");
			if (isOrderBy) {
				sqlStr.append(" ORDER BY arquivo.descricao DESC ");
			}
		} else if (campoConsulta.equals("matricula")) {
			sqlStr.append(" and documentoassinado.matricula = '").append(valorConsulta).append("'");
			if (isOrderBy) {
				sqlStr.append(" ORDER BY documentoassinado.dataRegistro DESC ");
			}
		} else if (campoConsulta.equals("requerente") || campoConsulta.equals("nomeAlunoHistoricoDigital") || campoConsulta.equals("nomeAluno")) {
			sqlStr.append(" and upper( sem_acentos(pessoa.nome)) like('").append(Uteis.removerAcentuacao(valorConsulta.toUpperCase())).append("%') ");
			if (isOrderBy) {
				sqlStr.append(" ORDER BY arquivo.descricao DESC ");
			}
		} else if (campoConsulta.equals("documentoInstitucional")) {
			sqlStr.append(" and arquivo.apresentarDocumentoPortalTransparencia ");
			if (isOrderBy) {
				sqlStr.append(" ORDER BY arquivo.dataUpload DESC ");
			}
		} else if (campoConsulta.equals("codigoValidacaoHistoricoDigital")) {
			sqlStr.append(" and documentoassinado.codigoValidacaoHistoricoDigital = '").append(valorConsulta).append("'");
		} else if (campoConsulta.equals("CPF") || campoConsulta.equals("CPFHistoricoDigital")) {
			sqlStr.append("and (replace(replace((pessoa.cpf),'.',''),'-','')) LIKE('").append(Uteis.retirarMascaraCPF(valorConsulta)).append("')");
			if (isOrderBy) {
				sqlStr.append(" ORDER BY arquivo.descricao DESC ");
			}
		} else if (campoConsulta.equals("matrizCurricularCurriculo")) {
			sqlStr.append(" AND gradecurricular.nome ILIKE('").append(valorConsulta).append(PERCENT +"') ");
		} else if (campoConsulta.equals("cursoCurriculo")) {
			sqlStr.append(" AND cursoDocumentoAssinado.nome ILIKE('").append(valorConsulta).append(PERCENT +"') ");
		} else if (campoConsulta.equals("unidadeEnsinoCurriculo")) {
			sqlStr.append(" AND unidadeEnsino.nome ILIKE('").append(valorConsulta).append(PERCENT +"') ");
		} else if (campoConsulta.equals("codigoValidacaoCurriculo")) {
			sqlStr.append(" AND documentoassinado.codigovalidacaocurriculoescolardigital = '").append(valorConsulta).append("' ");
		}
	}

    @Override
    public DocumentoAssinadoVO consultarPorCodigoProvedorAssinatura(String codigoprovedordeassinatura, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
        StringBuilder sqlStr = getSqlConsultaDadosCompletos();
        sqlStr.append(" WHERE documentoassinado.codigoprovedordeassinatura = ?");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[]{codigoprovedordeassinatura});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( Documento Assinado ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    @Override
    public DocumentoAssinadoVO consultarPorChaveProvedordeAssinatura(String chaveProvedorDeAssinatura, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
        StringBuilder sqlStr = getSqlConsultaDadosCompletos();
        sqlStr.append(" WHERE documentoassinado.chaveprovedordeassinatura = ?");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[]{chaveProvedorDeAssinatura});
        if (!tabelaResultado.next()) {
            return null;
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    @Override
    public DocumentoAssinadoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
        StringBuilder sqlStr = getSqlConsultaDadosCompletos();
        sqlStr.append(" WHERE documentoassinado.codigo = ?");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( Documento Assinado ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    @Override
    public Boolean consultarSeExisteDocumentoAssinado(UnidadeEnsinoVO unidadeEnsinoVO, CursoVO cursoVO, TurmaVO turmaVO, DisciplinaVO disciplinaVO, String ano,
                                                      String semestre, MatriculaVO matriculaVO, TipoOrigemDocumentoAssinadoEnum tipoOrigemDocumentoAssinado, Date dataInicio, Date dataTermino,
                                                      SituacaoDocumentoAssinadoPessoaEnum situacaoDocumentoAssinadoPessoaEnum, PessoaVO pessoaAssinatura, UsuarioVO usuarioVO) {

        StringBuilder sqlStr = new StringBuilder("select count(distinct documentoassinado.codigo) as qtd from documentoassinado  ");
        sqlStr.append("inner join arquivo on arquivo.codigo = documentoassinado.arquivo ");
        sqlStr.append("left join unidadeEnsino on documentoassinado.unidadeEnsino = unidadeEnsino.codigo ");
        sqlStr.append("left join disciplina on documentoassinado.disciplina = disciplina.codigo ");
        sqlStr.append("left join turma on documentoassinado.turma = turma.codigo ");
        sqlStr.append("left join usuario on documentoassinado.usuario = usuario.codigo ");
        sqlStr.append("left join matricula on documentoassinado.matricula = matricula.matricula ");
        sqlStr.append("left join pessoa  on matricula.aluno = pessoa.codigo ");
        sqlStr.append(" where 1 = 1 ");
        montarFiltrosParaConsultaDocumentos(unidadeEnsinoVO, cursoVO, turmaVO, disciplinaVO, ano, semestre, matriculaVO, tipoOrigemDocumentoAssinado != null ? tipoOrigemDocumentoAssinado.name() : Constantes.EMPTY, dataInicio, dataTermino, situacaoDocumentoAssinadoPessoaEnum, pessoaAssinatura, sqlStr, false, false, null);
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        Boolean existeResultado = false;
        if (tabelaResultado.next()) {
            existeResultado = tabelaResultado.getInt("qtd") > 0;
        }
        return existeResultado;
    }

    public boolean consultarSeDocumentoAssinadoTodoAssinado(String codigoprovedordeassinatura) {

        StringBuilder sqlStr = new StringBuilder("select count(documentoassinado.codigo) as qtde from documentoassinado");
        sqlStr.append(" where documentoassinado.chaveprovedordeassinatura = ? ");
        sqlStr.append(" and exists (select documentoassinadopessoa.codigo from documentoassinadopessoa where documentoassinadopessoa.documentoassinado = documentoassinado.codigo ");
        sqlStr.append(" and documentoassinadopessoa.situacaodocumentoassinadopessoa != ?) ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), codigoprovedordeassinatura, SituacaoDocumentoAssinadoPessoaEnum.ASSINADO.name());
        Boolean existeResultado = false;
        if (tabelaResultado.next()) {
            existeResultado = tabelaResultado.getInt("qtde") == 0;
        }
        return existeResultado;
    }

	@Override
	public DocumentoAssinadoVO consultarDocumentoAssinadoPorTurmaPorDisciplinaPorAnoPorSemestrePorTipoOrigemDocumentoAssinadoEnum(Integer turma, Integer disciplina, String ano, String semestre, TipoOrigemDocumentoAssinadoEnum tipoOrigemDocumentoAssinadoEnum, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuarioLogado);
		StringBuilder sqlStr = getSqlConsultaDadosArquivoAssinado();
		sqlStr.append(" WHERE 1=1 ");
		if (Uteis.isAtributoPreenchido(turma)) {
			sqlStr.append(" and documentoassinado.turma =  ").append(turma);
		}
		if (Uteis.isAtributoPreenchido(disciplina)) {
			sqlStr.append(" and documentoassinado.disciplina =  ").append(disciplina);
		}
		if (Uteis.isAtributoPreenchido(ano)) {
			sqlStr.append(" and documentoassinado.ano = '").append(ano).append("'");
		}
		if (Uteis.isAtributoPreenchido(semestre)) {
			sqlStr.append(" and documentoassinado.semestre = '").append(semestre).append("'");
		}
		if (Uteis.isAtributoPreenchido(tipoOrigemDocumentoAssinadoEnum)) {
			sqlStr.append(" and documentoassinado.tipoOrigemDocumentoAssinado = '").append(tipoOrigemDocumentoAssinadoEnum.name()).append("'");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return montarDadosArquivoAssinado(tabelaResultado, nivelMontarDados, usuarioLogado);
		}
		return new DocumentoAssinadoVO();

	}

	public List<DocumentoAssinadoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<DocumentoAssinadoVO> vetResultado = new ArrayList<DocumentoAssinadoVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	public List<DocumentoAssinadoVO> montarDadosConsultaArquivoAssinado(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<DocumentoAssinadoVO> vetResultado = new ArrayList<DocumentoAssinadoVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDadosArquivoAssinado(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	public DocumentoAssinadoVO montarDadosArquivoAssinado(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		DocumentoAssinadoVO obj = new DocumentoAssinadoVO();
		obj.setNovoObj(false);
		obj.setCodigo(new Integer(dadosSQL.getInt("documentoassinado.codigo")));
		obj.setDataRegistro(dadosSQL.getDate("documentoassinado.dataregistro"));
		obj.setTipoOrigemDocumentoAssinadoEnum(TipoOrigemDocumentoAssinadoEnum.valueOf(dadosSQL.getString("documentoassinado.tipoorigemdocumentoassinado")));
		obj.setProvedorDeAssinaturaEnum(ProvedorDeAssinaturaEnum.valueOf(dadosSQL.getString("documentoassinado.provedordeassinaturaenum")));                            
		obj.getMatricula().setMatricula(dadosSQL.getString("documentoassinado.matricula"));
		obj.getMatricula().getAluno().setNome(dadosSQL.getString("pessoa.nome"));
		obj.getMatricula().getAluno().setCPF(dadosSQL.getString("pessoa.cpf"));
		obj.getMatricula().getAluno().setDataNasc(dadosSQL.getDate("pessoa.dataNasc"));
		obj.getGradeCurricular().setCodigo(dadosSQL.getInt("documentoassinado.gradeCurricular"));
		obj.getTurma().setCodigo(dadosSQL.getInt("documentoassinado.turma"));
		obj.getDisciplina().setCodigo(dadosSQL.getInt("documentoassinado.disciplina"));
		obj.getMatriculaPeriodo().setCodigo(dadosSQL.getInt("documentoassinado.matriculaPeriodo"));
		obj.setChaveProvedorDeAssinatura(dadosSQL.getString("documentoassinado.chaveProvedorDeAssinatura"));
		obj.setUrlProvedorDeAssinatura(dadosSQL.getString("documentoassinado.urlProvedorDeAssinatura"));
		obj.setCodigoProvedorDeAssinatura(dadosSQL.getString("documentoassinado.codigoProvedorDeAssinatura"));
		obj.getUnidadeEnsinoVO().setCodigo(new Integer(dadosSQL.getInt("unidadeEnsino.codigo")));
		obj.getUnidadeEnsinoVO().setNome(dadosSQL.getString("unidadeEnsino.nome"));
		obj.getUnidadeEnsinoVO().setRazaoSocial(dadosSQL.getString("unidadeEnsino.razaoSocial"));
		obj.getUnidadeEnsinoVO().setCNPJ(dadosSQL.getString("unidadeEnsino.cnpj"));
		obj.setCodigoValidacaoCurriculoEscolarDigital(dadosSQL.getString("documentoassinado.codigoValidacaoCurriculoEscolarDigital"));
		obj.getGradeCurricular().setNome(dadosSQL.getString("gradeCurricular.nome"));
		obj.getCursoVO().setCodigo(dadosSQL.getInt("cursoDocumentoAssinado.codigo"));
		obj.getCursoVO().setNome(dadosSQL.getString("cursoDocumentoAssinado.nome"));
		obj.getUsuario().setCodigo(new Integer(dadosSQL.getInt("usuario.codigo")));
		obj.getUsuario().setNome(dadosSQL.getString("usuario.nome"));
		obj.getArquivo().setCodigo(new Integer(dadosSQL.getInt("arquivo.codigo")));
		obj.getArquivo().setNome(dadosSQL.getString("arquivo.nome"));
		obj.getArquivo().setDescricao(dadosSQL.getString("arquivo.descricao"));
		obj.getArquivo().setExtensao(dadosSQL.getString("arquivo.extensao"));
		obj.getArquivo().setDataUpload(dadosSQL.getDate("arquivo.dataUpload"));
		obj.getArquivo().getResponsavelUpload().setCodigo((dadosSQL.getInt("arquivo.responsavelUpload")));
		obj.getArquivo().setCodOrigem(dadosSQL.getInt("arquivo.codOrigem"));
		obj.getArquivo().setPastaBaseArquivo(dadosSQL.getString("arquivo.pastaBaseArquivo"));
		obj.getArquivo().getTurma().setCodigo((dadosSQL.getInt("arquivo.turma")));
		obj.getArquivo().getProfessor().setCodigo((dadosSQL.getInt("arquivo.professor")));
		obj.getArquivo().setDataDisponibilizacao(dadosSQL.getDate("arquivo.dataDisponibilizacao"));
		obj.getArquivo().setDataIndisponibilizacao(dadosSQL.getDate("arquivo.dataIndisponibilizacao"));
		obj.getArquivo().setManterDisponibilizacao((dadosSQL.getBoolean("arquivo.manterDisponibilizacao")));
		obj.getArquivo().setOrigem(dadosSQL.getString("arquivo.origem"));
		obj.getArquivo().setNivelEducacional(dadosSQL.getString("arquivo.nivelEducacional"));
		obj.getArquivo().getDisciplina().setCodigo((dadosSQL.getInt("arquivo.disciplina")));
		obj.getArquivo().setSituacao(dadosSQL.getString("arquivo.situacao"));
		obj.getArquivo().setApresentarPortalProfessor((dadosSQL.getBoolean("arquivo.apresentarPortalProfessor")));
		obj.getArquivo().setApresentarPortalAluno((dadosSQL.getBoolean("arquivo.apresentarPortalAluno")));
		obj.getArquivo().setApresentarPortalCoordenador((dadosSQL.getBoolean("arquivo.apresentarPortalCoordenador")));
		obj.getArquivo().setControlarDownload((dadosSQL.getBoolean("arquivo.controlarDownload")));
		obj.getArquivo().setPermitirArquivoResposta((dadosSQL.getBoolean("arquivo.permitirArquivoResposta")));
		obj.getArquivo().setArquivoResposta((dadosSQL.getInt("arquivo.arquivoResposta")));
		obj.getArquivo().setCpfAlunoDocumentacao(dadosSQL.getString("arquivo.cpfAlunoDocumentacao"));
		obj.getArquivo().setCpfRequerimento(dadosSQL.getString("arquivo.cpfRequerimento"));
		obj.getArquivo().getResponsavelUpload().setCodigo(dadosSQL.getInt("usuario.codigo"));
		obj.getArquivo().getResponsavelUpload().setNome(dadosSQL.getString("usuario.nome"));
		obj.getArquivo().getProfessor().setCodigo(dadosSQL.getInt("arquivo.professor"));
		obj.getArquivo().getPessoaVO().setCodigo(dadosSQL.getInt("arquivo.pessoa"));
		obj.getArquivo().getCurso().setCodigo(dadosSQL.getInt("arquivo.curso"));
		obj.getArquivo().getTurma().setCodigo(dadosSQL.getInt("arquivo.turma"));
		obj.getArquivo().getDisciplina().setCodigo(dadosSQL.getInt("arquivo.disciplina"));
		obj.getArquivo().setApresentarDeterminadoPeriodo(dadosSQL.getBoolean("arquivo.apresentarDeterminadoPeriodo"));
		obj.getArquivo().setIndice(dadosSQL.getInt("arquivo.indice"));
		obj.getArquivo().setAgrupador(dadosSQL.getString("arquivo.agrupador"));
		obj.getArquivo().setIndiceAgrupador(dadosSQL.getInt("arquivo.indiceAgrupador"));
		obj.getArquivo().setDescricaoArquivo(dadosSQL.getString("arquivo.descricaoArquivo"));
		obj.getArquivoVisual().setCodigo(new Integer(dadosSQL.getInt("arquivovisual.codigo")));
		obj.getArquivoVisual().setNome(dadosSQL.getString("arquivovisual.nome"));
		obj.getArquivoVisual().setDescricao(dadosSQL.getString("arquivovisual.descricao"));
		obj.getArquivoVisual().setExtensao(dadosSQL.getString("arquivovisual.extensao"));
		obj.getArquivoVisual().setDataUpload(dadosSQL.getDate("arquivovisual.dataUpload"));
		obj.getArquivoVisual().getResponsavelUpload().setCodigo((dadosSQL.getInt("arquivovisual.responsavelUpload")));
		obj.getArquivoVisual().setCodOrigem(dadosSQL.getInt("arquivovisual.codOrigem"));
		obj.getArquivoVisual().setPastaBaseArquivo(dadosSQL.getString("arquivovisual.pastaBaseArquivo"));
		obj.getArquivoVisual().getTurma().setCodigo((dadosSQL.getInt("arquivovisual.turma")));
		obj.getArquivoVisual().getProfessor().setCodigo((dadosSQL.getInt("arquivovisual.professor")));
		obj.getArquivoVisual().setDataDisponibilizacao(dadosSQL.getDate("arquivovisual.dataDisponibilizacao"));
		obj.getArquivoVisual().setDataIndisponibilizacao(dadosSQL.getDate("arquivovisual.dataIndisponibilizacao"));
		obj.getArquivoVisual().setManterDisponibilizacao((dadosSQL.getBoolean("arquivovisual.manterDisponibilizacao")));
		obj.getArquivoVisual().setOrigem(dadosSQL.getString("arquivovisual.origem"));
		obj.getArquivoVisual().setNivelEducacional(dadosSQL.getString("arquivovisual.nivelEducacional"));
		obj.getArquivoVisual().getDisciplina().setCodigo((dadosSQL.getInt("arquivovisual.disciplina")));
		obj.getArquivoVisual().setSituacao(dadosSQL.getString("arquivovisual.situacao"));
		obj.getArquivoVisual().setApresentarPortalProfessor((dadosSQL.getBoolean("arquivovisual.apresentarPortalProfessor")));
		obj.getArquivoVisual().setApresentarPortalAluno((dadosSQL.getBoolean("arquivovisual.apresentarPortalAluno")));
		obj.getArquivoVisual().setApresentarPortalCoordenador((dadosSQL.getBoolean("arquivovisual.apresentarPortalCoordenador")));
		obj.getArquivoVisual().setControlarDownload((dadosSQL.getBoolean("arquivovisual.controlarDownload")));
		obj.getArquivoVisual().setPermitirArquivoResposta((dadosSQL.getBoolean("arquivovisual.permitirArquivoResposta")));
		obj.getArquivoVisual().setArquivoResposta((dadosSQL.getInt("arquivovisual.arquivoResposta")));
		obj.getArquivoVisual().setCpfAlunoDocumentacao(dadosSQL.getString("arquivovisual.cpfAlunoDocumentacao"));
		obj.getArquivoVisual().setCpfRequerimento(dadosSQL.getString("arquivovisual.cpfRequerimento"));
		obj.getArquivoVisual().getResponsavelUpload().setCodigo(dadosSQL.getInt("usuario.codigo"));
		obj.getArquivoVisual().getResponsavelUpload().setNome(dadosSQL.getString("usuario.nome"));
		obj.getArquivoVisual().getProfessor().setCodigo(dadosSQL.getInt("arquivovisual.professor"));
		obj.getArquivoVisual().getPessoaVO().setCodigo(dadosSQL.getInt("arquivovisual.pessoa"));
		obj.getArquivoVisual().getCurso().setCodigo(dadosSQL.getInt("arquivovisual.curso"));
		obj.getArquivoVisual().getTurma().setCodigo(dadosSQL.getInt("arquivovisual.turma"));
		obj.getArquivoVisual().getDisciplina().setCodigo(dadosSQL.getInt("arquivovisual.disciplina"));
		obj.getArquivoVisual().setApresentarDeterminadoPeriodo(dadosSQL.getBoolean("arquivovisual.apresentarDeterminadoPeriodo"));
		obj.getArquivoVisual().setIndice(dadosSQL.getInt("arquivovisual.indice"));
		obj.getArquivoVisual().setAgrupador(dadosSQL.getString("arquivovisual.agrupador"));
		obj.getArquivoVisual().setIndiceAgrupador(dadosSQL.getInt("arquivovisual.indiceAgrupador"));
		obj.getArquivoVisual().setDescricaoArquivo(dadosSQL.getString("arquivovisual.descricaoArquivo"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
			return obj;
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			obj.getArquivo().getPessoaVO().setNome(dadosSQL.getString("pessoa.nome"));
			obj.getArquivo().getPessoaVO().setCPF(dadosSQL.getString("pessoa.cpf"));
			obj.getArquivo().getPessoaVO().setDataNasc(dadosSQL.getDate("pessoa.dataNasc"));
			obj.setPessoaNome(dadosSQL.getString("pessoa.nome"));
			obj.setPessoaCpf(dadosSQL.getString("pessoa.cpf"));
			obj.setPessoaData(dadosSQL.getDate("pessoa.dataNasc"));
			return obj;
		}
		obj.getArquivo().getPessoaVO().setNome(dadosSQL.getString("pessoa.nome"));
		obj.getArquivo().getPessoaVO().setCPF(dadosSQL.getString("pessoa.cpf"));
		obj.getArquivo().getPessoaVO().setDataNasc(dadosSQL.getDate("pessoa.dataNasc"));
		obj.setPessoaNome(dadosSQL.getString("pessoa.nome"));
		obj.setPessoaCpf(dadosSQL.getString("pessoa.cpf"));
		obj.setPessoaData(dadosSQL.getDate("pessoa.dataNasc"));
		if(Uteis.isColunaExistente(dadosSQL, "programacaoformatura.codigo")) {
			obj.getProgramacaoFormaturaVO().setCodigo(dadosSQL.getInt("programacaoformatura.codigo"));	
		}
		if(Uteis.isColunaExistente(dadosSQL, "programacaoformatura.datalimiteassinaturaata")) {
			obj.getProgramacaoFormaturaVO().setDataLimiteAssinaturaAta(dadosSQL.getTimestamp("programacaoformatura.datalimiteassinaturaata"));	
		}
		if (Uteis.isAtributoPreenchido(obj.getMatricula())) {
			obj.setMatricula(getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula().getMatricula(), obj.getMatricula().getUnidadeEnsino().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS,  usuario));
		}
		obj.setListaDocumentoAssinadoPessoa(getFacadeFactory().getDocumentoAssinadoPessoaFacade().consultarDocumentosAssinadoPessoaPorDocumentoAssinado(obj, false, Uteis.NIVELMONTARDADOS_TODOS, usuario));
		return obj;
	}

	public DocumentoAssinadoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		DocumentoAssinadoVO obj = new DocumentoAssinadoVO();
		obj.setNovoObj(false);
		obj.setCodigo(new Integer(dadosSQL.getInt("documentoassinado.codigo")));
		obj.setDataRegistro(dadosSQL.getDate("documentoassinado.dataregistro"));
		obj.setTipoOrigemDocumentoAssinadoEnum(TipoOrigemDocumentoAssinadoEnum.valueOf(dadosSQL.getString("documentoassinado.tipoorigemdocumentoassinado")));
		obj.setProvedorDeAssinaturaEnum(ProvedorDeAssinaturaEnum.valueOf(dadosSQL.getString("documentoassinado.provedorDeAssinaturaEnum")));
		obj.setChaveProvedorDeAssinatura(dadosSQL.getString("documentoassinado.chaveProvedorDeAssinatura"));
		obj.setUrlProvedorDeAssinatura(dadosSQL.getString("documentoassinado.urlProvedorDeAssinatura"));
		obj.setCodigoProvedorDeAssinatura(dadosSQL.getString("documentoassinado.codigoProvedorDeAssinatura"));
		obj.setDocumentoAssinadoInvalido(dadosSQL.getBoolean("documentoassinado.documentoAssinadoInvalido"));
		obj.setMotivoDocumentoAssinadoInvalido(dadosSQL.getString("documentoassinado.motivoDocumentoAssinadoInvalido"));
		obj.getMatricula().setMatricula(dadosSQL.getString("documentoassinado.matricula"));
		obj.getGradeCurricular().setCodigo(dadosSQL.getInt("documentoassinado.gradeCurricular"));
		obj.setDecisaoJudicial(dadosSQL.getBoolean("documentoassinado.decisaoJudicial"));
		obj.setIdDiplomaDigital(dadosSQL.getString("documentoassinado.idDiplomaDigital"));
		obj.setIdDadosRegistrosDiplomaDigital(dadosSQL.getString("documentoassinado.idDadosRegistrosDiplomaDigital"));
		obj.setCodigoValidacaoHistoricoDigital(dadosSQL.getString("documentoassinado.codigoValidacaoHistoricoDigital"));
		obj.getTurma().setCodigo(dadosSQL.getInt("turma.codigo"));
		obj.getTurma().setIdentificadorTurma(dadosSQL.getString("turma.identificadorTurma"));
		obj.getDisciplina().setCodigo(dadosSQL.getInt("disciplina.codigo"));
		obj.getDisciplina().setNome(dadosSQL.getString("disciplina.nome"));
		obj.getMatriculaPeriodo().setCodigo(dadosSQL.getInt("documentoassinado.matriculaPeriodo"));
		obj.getUnidadeEnsinoVO().setCodigo(new Integer(dadosSQL.getInt("unidadeEnsino.codigo")));
		obj.getUnidadeEnsinoVO().setNome(dadosSQL.getString("unidadeEnsino.nome"));
		obj.getUnidadeEnsinoVO().setRazaoSocial(dadosSQL.getString("unidadeEnsino.razaoSocial"));
		obj.getUnidadeEnsinoVO().setCNPJ(dadosSQL.getString("unidadeEnsino.cnpj"));
		obj.getUnidadeEnsinoVO().setUnidadeCertificadora(dadosSQL.getString("unidadeEnsino.unidadeCertificadora"));
		obj.getUnidadeEnsinoVO().setCnpjUnidadeCertificadora(dadosSQL.getString("unidadeEnsino.cnpjUnidadeCertificadora"));
		obj.getGradeCurricular().setNome(dadosSQL.getString("gradeCurricular.nome"));
		obj.setCodigoValidacaoCurriculoEscolarDigital(dadosSQL.getString("documentoassinado.codigoValidacaoCurriculoEscolarDigital"));
		obj.getCursoVO().setCodigo(dadosSQL.getInt("curso.codigo"));
		obj.getCursoVO().setNome(dadosSQL.getString("curso.nome"));
		obj.getUsuario().setCodigo(new Integer(dadosSQL.getInt("usuario.codigo")));
		obj.getUsuario().setNome(dadosSQL.getString("usuario.nome"));
		obj.getArquivo().setCodigo(new Integer(dadosSQL.getInt("arquivo.codigo")));
		obj.getArquivo().setCodOrigem(dadosSQL.getInt("arquivo.codOrigem"));
		obj.getArquivo().setNome(dadosSQL.getString("arquivo.nome"));
		obj.getArquivo().setDescricao(dadosSQL.getString("arquivo.descricao"));
		obj.getArquivo().setPastaBaseArquivo(dadosSQL.getString("arquivo.pastaBaseArquivo"));
		obj.getArquivoVisual().setCodigo(new Integer(dadosSQL.getInt("arquivovisual.codigo")));
		obj.getArquivoVisual().setCodOrigem(dadosSQL.getInt("arquivovisual.codOrigem"));
		obj.getArquivoVisual().setNome(dadosSQL.getString("arquivovisual.nome"));
		obj.getArquivoVisual().setDescricao(dadosSQL.getString("arquivovisual.descricao"));
		obj.getArquivoVisual().setPastaBaseArquivo(dadosSQL.getString("arquivovisual.pastaBaseArquivo"));	
		obj.getArquivoVisual().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.DOCUMENTOS_ASSINADOS);
		obj.getArquivoVisual().setDataUpload(dadosSQL.getDate("arquivovisual.dataUpload"));
		if(dadosSQL.getString("arquivovisual.servidorArquivoOnline") != null) {
			obj.getArquivoVisual().setServidorArquivoOnline(ServidorArquivoOnlineEnum.valueOf(dadosSQL.getString("arquivovisual.servidorArquivoOnline")));	
		}
		obj.getExpedicaoDiplomaVO().setCodigo(dadosSQL.getInt("expedicaoDiploma.codigo"));
		if (dadosSQL.getString("documentoassinado.versaoDiploma") != null) {
			obj.setVersaoDiploma(VersaoDiplomaDigitalEnum.getEnum(dadosSQL.getString("documentoassinado.versaoDiploma")));
		}
		if (String.valueOf(dadosSQL.getInt("codigoOrigem")) != null) {
			obj.setCodigoOrigem(Integer.valueOf(dadosSQL.getInt("codigoOrigem")));
		}
		if(dadosSQL.getString("arquivo.servidorarquivoonline") != null) {
			obj.getArquivo().setServidorArquivoOnline(ServidorArquivoOnlineEnum.valueOf(dadosSQL.getString("arquivo.servidorarquivoonline")));
		}
		obj.getMatricula().getAluno().setCodigo(dadosSQL.getInt("pessoa.codigo"));
		obj.getMatricula().getAluno().setNome(dadosSQL.getString("pessoa.nome"));
		obj.getCursoVO().setCodigo(dadosSQL.getInt("curso.codigo"));
		obj.getCursoVO().setNome(dadosSQL.getString("curso.nome"));
		
		obj.getProgramacaoFormaturaVO().setCodigo(dadosSQL.getInt("programacaoformatura.codigo"));
		if (Uteis.isColunaExistente(dadosSQL, "programacaoformatura.datacadastro")) {
			obj.getProgramacaoFormaturaVO().setDataCadastro(dadosSQL.getDate("programacaoformatura.datacadastro"));
		}
		if (Uteis.isColunaExistente(dadosSQL, "colacaograu.titulo")) {
			obj.getProgramacaoFormaturaVO().getColacaoGrauVO().setTitulo(dadosSQL.getString("colacaograu.titulo"));
		}
		obj.getDescricaoPersonalizada();//Necessario para inicialiar o dados caso seja utilizado em algum json
		obj.getDataRegistroApresentar();//Necessario para inicialiar o dados caso seja utilizado em algum json
		obj.getTipoOrigemDocumentoAssinadoEnumApresentar();//Necessario para inicialiar o dados caso seja utilizado em algum json
		obj.setErro(dadosSQL.getBoolean("documentoassinado.erro"));
		obj.setMotivoErro(dadosSQL.getString("documentoassinado.motivoErro"));
		
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
			return obj;
		}
		obj.getMatricula().getAluno().setCPF(dadosSQL.getString("pessoa.cpf"));
		obj.getMatricula().getAluno().setDataNasc(dadosSQL.getDate("pessoa.dataNasc"));
		obj.setPessoaNome(dadosSQL.getString("pessoa.nome"));
		obj.setPessoaCpf(dadosSQL.getString("pessoa.cpf"));
		obj.setPessoaData(dadosSQL.getDate("pessoa.dataNasc"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {			
			return obj;
		}
	
		if(Uteis.isColunaExistente(dadosSQL, "programacaoformatura.dataLimiteAssinaturaAta")) {
			obj.getProgramacaoFormaturaVO().setDataLimiteAssinaturaAta(dadosSQL.getTimestamp("programacaoformatura.dataLimiteAssinaturaAta"));	
		}
		obj.setListaDocumentoAssinadoPessoa(getFacadeFactory().getDocumentoAssinadoPessoaFacade().consultarDocumentosAssinadoPessoaPorDocumentoAssinado(obj, false, Uteis.NIVELMONTARDADOS_TODOS, usuario));
		return obj;
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
	 */
	public static String getIdEntidade() {
		return DocumentoAssinado.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		DocumentoAssinado.idEntidade = idEntidade;
	}
	
	@Override
	public void consultarDocumentos(DataModelo dataModelo, UnidadeEnsinoVO unidadeEnsinoVO, CursoVO cursoVO, TurmaVO turmaVO, DisciplinaVO disciplinaVO, String ano, 
			String semestre, MatriculaVO matriculaVO, TipoOrigemDocumentoAssinadoEnum tipoOrigemDocumentoAssinado, Date dataInicio, Date dataTermino, 
			SituacaoDocumentoAssinadoPessoaEnum situacaoDocumentoAssinadoPessoaEnum, PessoaVO pessoaAssinatura, UsuarioVO usuarioVO, Integer limit, Integer offset) throws Exception {
		consultarDocumentos(dataModelo, unidadeEnsinoVO, cursoVO, turmaVO, disciplinaVO, ano, semestre, matriculaVO, tipoOrigemDocumentoAssinado, dataInicio, dataTermino, situacaoDocumentoAssinadoPessoaEnum, pessoaAssinatura, usuarioVO, limit, offset, null);
	}
	
	@Override
	public void consultarDocumentos(DataModelo dataModelo, UnidadeEnsinoVO unidadeEnsinoVO, CursoVO cursoVO, TurmaVO turmaVO, DisciplinaVO disciplinaVO, String ano, 
			String semestre, MatriculaVO matriculaVO, TipoOrigemDocumentoAssinadoEnum tipoOrigemDocumentoAssinado, Date dataInicio, Date dataTermino, 
			SituacaoDocumentoAssinadoPessoaEnum situacaoDocumentoAssinadoPessoaEnum, PessoaVO pessoaAssinatura, UsuarioVO usuarioVO, Integer limit, Integer offset, Integer ordemAssinaturaFiltrar) throws Exception {
		dataModelo.setListaConsulta(new ArrayList<>());
		
		StringBuilder sql  =  new StringBuilder(getSqlConsultaDadosBasico());
		
		if (Uteis.isAtributoPreenchido(ordemAssinaturaFiltrar) && situacaoDocumentoAssinadoPessoaEnum != null && situacaoDocumentoAssinadoPessoaEnum.isPendente()) {
			sql.append(" LEFT JOIN LATERAL (SELECT d.ordemassinatura FROM documentoassinadopessoa d WHERE d.documentoassinado = documentoassinado.codigo AND d.situacaodocumentoassinadopessoa = 'PENDENTE' ORDER BY d.ordemassinatura LIMIT 1 ) AS pessoaAssinar ON 1 = 1 ");
		}
		sql.append(" where 1 = 1 ");
		montarFiltrosParaConsultaDocumentos(unidadeEnsinoVO, cursoVO, turmaVO, disciplinaVO, ano, semestre, matriculaVO, tipoOrigemDocumentoAssinado != null ? tipoOrigemDocumentoAssinado.name() : Constantes.EMPTY, dataInicio, dataTermino, situacaoDocumentoAssinadoPessoaEnum, pessoaAssinatura, sql, tipoOrigemDocumentoAssinado != null && tipoOrigemDocumentoAssinado.isXmlMec(), false, ordemAssinaturaFiltrar);
		SqlRowSet rs =  getConexao().getJdbcTemplate().queryForRowSet(" select count(distinct t.\"documentoassinado.codigo\") as qtde from ( " + sql.toString()+") as t ");
		if(rs.next()) {
			dataModelo.setTotalRegistrosEncontrados(rs.getInt("qtde"));
			sql.append(" order by documentoassinado.dataregistro  ");
			sql.append(" limit ").append(limit).append(" offset ").append(offset);
			dataModelo.setListaConsulta(montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO));
		}
	}

	private void montarFiltrosParaConsultaDocumentos(UnidadeEnsinoVO unidadeEnsinoVO, CursoVO cursoVO, TurmaVO turmaVO, DisciplinaVO disciplinaVO, String ano, String semestre, MatriculaVO matriculaVO, String tipoOrigemDocumentoAssinado, Date dataInicio, Date dataTermino, SituacaoDocumentoAssinadoPessoaEnum situacaoDocumentoAssinadoPessoaEnum, PessoaVO pessoaAssinatura, StringBuilder sql, Boolean filtrarArquivosXML, Boolean consultarPessoa, Integer ordemAssinaturaFiltrar) {
		TipoOrigemDocumentoAssinadoEnum tipoOrigemDocumentoAssinadoEnum = null;
		if (Uteis.isAtributoPreenchido(tipoOrigemDocumentoAssinado) && !tipoOrigemDocumentoAssinado.equals("DOCUMENTOS_XML")) {
			if ((tipoOrigemDocumentoAssinado.equals("DIPLOMA_DIGITAL_VISUAL") || tipoOrigemDocumentoAssinado.equals("HISTORICO_DIGITAL_VISUAL") || tipoOrigemDocumentoAssinado.equals("CURRICULO_ESCOLAR_DIGITAL_VISUAL"))) {
				tipoOrigemDocumentoAssinadoEnum = Enum.valueOf(TipoOrigemDocumentoAssinadoEnum.class, tipoOrigemDocumentoAssinado.equals("DIPLOMA_DIGITAL_VISUAL") ? "DIPLOMA_DIGITAL" : tipoOrigemDocumentoAssinado.equals("HISTORICO_DIGITAL_VISUAL") ? "HISTORICO_DIGITAL" : "CURRICULO_ESCOLAR_DIGITAL");
			} else {
				tipoOrigemDocumentoAssinadoEnum = Enum.valueOf(TipoOrigemDocumentoAssinadoEnum.class, tipoOrigemDocumentoAssinado);
			}
		}
		if(Uteis.isAtributoPreenchido(unidadeEnsinoVO)) {
			sql.append(" and (documentoassinado.tipoorigemdocumentoassinado = 'ATA_COLACAO_GRAU' or documentoassinado.unidadeensino = ").append(unidadeEnsinoVO.getCodigo()).append(") ");
		}
		if(Uteis.isAtributoPreenchido(turmaVO)) {
			sql.append(" and documentoassinado.turma = ").append(turmaVO.getCodigo());			
		}
		if(Uteis.isAtributoPreenchido(disciplinaVO)) {
			sql.append(" and disciplina.codigo = ").append(disciplinaVO.getCodigo());
		}
		if(Uteis.isAtributoPreenchido(cursoVO) && !Uteis.isAtributoPreenchido(turmaVO)) {
			sql.append(" and ((documentoassinado.tipoorigemdocumentoassinado = 'CURRICULO_ESCOLAR_DIGITAL' AND documentoassinado.turma IS NULL) OR ((turma.turmaagrupada =  false and turma.curso = ").append(cursoVO.getCodigo()).append(") ");
			sql.append(" or (turma.turmaagrupada and exists (select turmaagrupada.codigo from turmaagrupada inner join turma as tp on tp.codigo = turmaagrupada.turma and turmaagrupada.turmaorigem = turma.codigo and  tp.curso = ").append(cursoVO.getCodigo()).append(")) ");
			sql.append(" )) ");
		}
		if(Uteis.isAtributoPreenchido(ano)) {
			sql.append(" and ( documentoassinado.ano = '").append(ano).append("' or ((documentoassinado.ano = '' or documentoassinado.ano is null) and extract(year from documentoassinado.dataregistro) = '").append(ano).append("')) ");
		}
		if(Uteis.isAtributoPreenchido(semestre)) {
			sql.append(" and (documentoassinado.semestre = '").append(semestre).append("'  or ((documentoassinado.semestre = '' or documentoassinado.semestre is null) and case when extract(month from documentoassinado.dataregistro) > 7 then '2' else '1' end = '").append(semestre).append("')) ");
		}
		if(matriculaVO != null && Uteis.isAtributoPreenchido(matriculaVO.getMatricula())) {
			sql.append(" and ((documentoassinado.matricula = '").append(matriculaVO.getMatricula()).append("') ");
			sql.append(" or exists(select documentoassinadopessoa.codigo from documentoassinadopessoa where documentoassinadopessoa.documentoassinado = documentoassinado.codigo and documentoassinadopessoa.tipoPessoa = 'ALUNO' and documentoassinadopessoa.pessoa = ").append(matriculaVO.getAluno().getCodigo()).append(")) ");			
		} 
		if(Uteis.isAtributoPreenchido(pessoaAssinatura)) {
			if (Uteis.isAtributoPreenchido(situacaoDocumentoAssinadoPessoaEnum) && situacaoDocumentoAssinadoPessoaEnum.isRejeitado()) {
				sql.append(" and (exists (select documentoassinadopessoa.codigo from documentoassinadopessoa where documentoassinadopessoa.documentoassinado = documentoassinado.codigo ");
				sql.append(" and documentoassinadopessoa.situacaodocumentoassinadopessoa = '").append(situacaoDocumentoAssinadoPessoaEnum.name()).append("' ");
				sql.append(" and documentoassinadopessoa.pessoa = ").append(pessoaAssinatura.getCodigo()).append(" ");
				sql.append(" ) or (documentoassinado.documentoAssinadoInvalido and exists (select documentoassinadopessoa.codigo from documentoassinadopessoa where documentoassinadopessoa.documentoassinado = documentoassinado.codigo ");
				sql.append(" and documentoassinadopessoa.pessoa = ").append(pessoaAssinatura.getCodigo()).append(" ");
				sql.append(" ))) ");
			} else {
				sql.append(" and exists (select documentoassinadopessoa.codigo from documentoassinadopessoa where documentoassinadopessoa.documentoassinado = documentoassinado.codigo ");
				if(Uteis.isAtributoPreenchido(situacaoDocumentoAssinadoPessoaEnum)) {
					sql.append(" and documentoassinadopessoa.situacaodocumentoassinadopessoa = '").append(situacaoDocumentoAssinadoPessoaEnum.name()).append("' ");
				}
				sql.append(" and documentoassinadopessoa.pessoa = ").append(pessoaAssinatura.getCodigo()).append(" ");
				sql.append(" ) ");
			}
		}else if(Uteis.isAtributoPreenchido(situacaoDocumentoAssinadoPessoaEnum) && situacaoDocumentoAssinadoPessoaEnum.equals(SituacaoDocumentoAssinadoPessoaEnum.ASSINADO)) {
			sql.append(" and documentoassinado.documentoAssinadoInvalido = false ");
			sql.append(" and not exists (select documentoassinadopessoa.codigo from documentoassinadopessoa where documentoassinadopessoa.documentoassinado = documentoassinado.codigo ");
			sql.append(" and documentoassinadopessoa.situacaodocumentoassinadopessoa <> '").append(situacaoDocumentoAssinadoPessoaEnum.name()).append("' ");					
			sql.append(" ) ");
			sql.append(" and (exists (select documentoassinadopessoa.codigo from documentoassinadopessoa where documentoassinadopessoa.documentoassinado = documentoassinado.codigo ");
			sql.append(" and documentoassinadopessoa.situacaodocumentoassinadopessoa = '").append(situacaoDocumentoAssinadoPessoaEnum.name()).append("' ");					
			sql.append(" ) or (not exists (select documentoassinadopessoa.codigo from documentoassinadopessoa where documentoassinadopessoa.documentoassinado = documentoassinado.codigo ) ");
			sql.append(" and arquivo.arquivoAssinadoDigitalmente)) ");
		}else if(Uteis.isAtributoPreenchido(situacaoDocumentoAssinadoPessoaEnum) && situacaoDocumentoAssinadoPessoaEnum.equals(SituacaoDocumentoAssinadoPessoaEnum.REJEITADO)) {
			sql.append(" and (documentoassinado.documentoAssinadoInvalido = true or  ");
			sql.append(" exists (select documentoassinadopessoa.codigo from documentoassinadopessoa where documentoassinadopessoa.documentoassinado = documentoassinado.codigo ");
			sql.append(" and documentoassinadopessoa.situacaodocumentoassinadopessoa = '").append(situacaoDocumentoAssinadoPessoaEnum.name()).append("' ");					
			sql.append(" )) ");
		}else if(Uteis.isAtributoPreenchido(situacaoDocumentoAssinadoPessoaEnum)) {
			sql.append(" and exists (select documentoassinadopessoa.codigo from documentoassinadopessoa where documentoassinadopessoa.documentoassinado = documentoassinado.codigo ");
			sql.append(" and documentoassinadopessoa.situacaodocumentoassinadopessoa = '").append(situacaoDocumentoAssinadoPessoaEnum.name()).append("' ");					
			sql.append(" ) ");
			sql.append(" and documentoassinado.documentoAssinadoInvalido = false ");
		}
		if ((Uteis.isAtributoPreenchido(tipoOrigemDocumentoAssinado) && listaDocumentoAssinadoSeiSignature.stream().anyMatch(d -> d.equals(tipoOrigemDocumentoAssinado))) || (tipoOrigemDocumentoAssinadoEnum != null && !tipoOrigemDocumentoAssinadoEnum.equals(TipoOrigemDocumentoAssinadoEnum.NENHUM))) {
			if (tipoOrigemDocumentoAssinado.equals("DOCUMENTOS_XML")) {
				sql.append(" and (documentoassinado.tipoorigemdocumentoassinado = 'DIPLOMA_DIGITAL' or documentoassinado.tipoorigemdocumentoassinado = 'DOCUMENTACAO_ACADEMICA_REGISTRO_DIPLOMA_DIGITAL' or documentoassinado.tipoorigemdocumentoassinado = 'HISTORICO_DIGITAL' or documentoassinado.tipoorigemdocumentoassinado = 'CURRICULO_ESCOLAR_DIGITAL') and arquivo.extensao = 'xml'");
			} else if ((tipoOrigemDocumentoAssinado.equals("DIPLOMA_DIGITAL") || tipoOrigemDocumentoAssinado.equals("HISTORICO_DIGITAL") || tipoOrigemDocumentoAssinado.equals("DOCUMENTACAO_ACADEMICA_REGISTRO_DIPLOMA_DIGITAL") || tipoOrigemDocumentoAssinado.equals("CURRICULO_ESCOLAR_DIGITAL")) && filtrarArquivosXML) {
				sql.append(" and documentoassinado.tipoorigemdocumentoassinado = '").append(tipoOrigemDocumentoAssinadoEnum.name()).append("' and arquivo.extensao = 'xml'");
			} else if ((tipoOrigemDocumentoAssinado.equals("DIPLOMA_DIGITAL_VISUAL") || tipoOrigemDocumentoAssinado.equals("HISTORICO_DIGITAL_VISUAL") || tipoOrigemDocumentoAssinado.equals("CURRICULO_ESCOLAR_DIGITAL_VISUAL")) && filtrarArquivosXML) {
				sql.append(" and documentoassinado.tipoorigemdocumentoassinado = '").append(tipoOrigemDocumentoAssinadoEnum.name()).append("' and arquivovisual.extensao <> 'xml'");
			} else {
				sql.append(" and documentoassinado.tipoorigemdocumentoassinado = '").append(tipoOrigemDocumentoAssinadoEnum.name()).append("'");
			}
			if (Uteis.isAtributoPreenchido(ordemAssinaturaFiltrar) && situacaoDocumentoAssinadoPessoaEnum != null && situacaoDocumentoAssinadoPessoaEnum.isPendente()) {
				sql.append(" and pessoaAssinar.ordemassinatura = " + ordemAssinaturaFiltrar + " ");
			}
		} else if (tipoOrigemDocumentoAssinadoEnum != null && tipoOrigemDocumentoAssinadoEnum.equals(TipoOrigemDocumentoAssinadoEnum.NENHUM) && filtrarArquivosXML) {
			sql.append(" and documentoassinado.tipoorigemdocumentoassinado <> 'DIPLOMA_DIGITAL' and documentoassinado.tipoorigemdocumentoassinado <> 'DOCUMENTACAO_ACADEMICA_REGISTRO_DIPLOMA_DIGITAL' and documentoassinado.tipoorigemdocumentoassinado <> 'HISTORICO_DIGITAL' and documentoassinado.tipoorigemdocumentoassinado <> 'CURRICULO_ESCOLAR_DIGITAL' ");
		}
		sql.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "documentoassinado.dataregistro", false));
		if (tipoOrigemDocumentoAssinadoEnum != null && tipoOrigemDocumentoAssinadoEnum.isXmlMec() && situacaoDocumentoAssinadoPessoaEnum != null && situacaoDocumentoAssinadoPessoaEnum.isAssinado()) {
			sql.append(" and documentoassinado.documentoassinadoinvalido = false ");
		}
	}
	
	@Override
	public Integer consultarTotalDocumentoPendenteUsuarioLogado(UsuarioVO usuarioVO) {
		try {
		if(Uteis.isAtributoPreenchido(usuarioVO) && Uteis.isAtributoPreenchido(usuarioVO.getPessoa())) {
			SqlRowSet rs =  getConexao().getJdbcTemplate().queryForRowSet("select count(distinct documentoassinado) as qtde from documentoassinadopessoa where situacaodocumentoassinadopessoa = ?  and pessoa = ? ", SituacaoDocumentoAssinadoPessoaEnum.PENDENTE.name(), usuarioVO.getPessoa().getCodigo());
			if(rs.next()) {
				return rs.getInt("qtde");
			}
		}
		return 0;
		}catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public DocumentoAssinadoVO consultarDocumentoAssinadoPorAlunoTipoOrigemCodigoOrigem(String matricula, Integer codigoOrigem, String origem, TipoOrigemDocumentoAssinadoEnum tipoOrigemDocumentoAssinadoEnum, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuarioLogado);
		StringBuilder sqlStr = getSqlConsultaDadosArquivoAssinado();
		sqlStr.append(" WHERE 1=1 ");
		if (Uteis.isAtributoPreenchido(matricula)) {
			sqlStr.append(" and documentoassinado.matricula =  '").append(matricula).append("' ");
		}
		if (Uteis.isAtributoPreenchido(codigoOrigem)) {
			sqlStr.append(" and arquivo.codOrigem =  ").append(codigoOrigem);
		}
		if (Uteis.isAtributoPreenchido(tipoOrigemDocumentoAssinadoEnum)) {
			sqlStr.append(" and documentoassinado.tipoOrigemDocumentoAssinado = '").append(tipoOrigemDocumentoAssinadoEnum.name()).append("'");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return montarDadosArquivoAssinado(tabelaResultado, nivelMontarDados, usuarioLogado);
		}
		return new DocumentoAssinadoVO();

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public DocumentoAssinadoVO realizarAssinaturaUnidadeCertificadoraExpedicaoDiploma(MatriculaVO matriculaVO, ExpedicaoDiplomaVO expedicaoDiplomaVO, ConfiguracaoGEDVO configGEDVO, File fileAssinar, FuncionarioVO funcionario1, String cargoFuncionario1, String tituloFuncionario1, FuncionarioVO funcionario2, String cargoFuncionario2, String tituloFuncionario2, FuncionarioVO funcionario3, String cargoFuncionario3, String tituloFuncionario3, String nomeArquivo, ConfiguracaoGeralSistemaVO config, String tipoLayout, UsuarioVO usuarioVO) throws Exception {
		try {
			DocumentoAssinadoVO obj = new DocumentoAssinadoVO();
			obj.setDataRegistro(new Date());
			obj.setUsuario(usuarioVO);
			obj.setTipoOrigemDocumentoAssinadoEnum(TipoOrigemDocumentoAssinadoEnum.EXPEDICAO_DIPLOMA);
			obj.setMatricula(matriculaVO);
			obj.setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCodigo(matriculaVO.getUnidadeEnsino().getCodigo(), false, usuarioVO));
			obj.setExpedicaoDiplomaVO(expedicaoDiplomaVO);
//			obj.getArquivo().setNome(nomeArquivo);
//			obj.getArquivo().setDescricao(nomeArquivo.substring(nomeArquivo.lastIndexOf(File.separator) + 1, nomeArquivo.lastIndexOf(".")));
//			obj.getArquivo().setExtensao(nomeArquivo.substring((nomeArquivo.lastIndexOf(".") + 1), nomeArquivo.length()));
			obj.getArquivo().setNome(fileAssinar.getName());
			obj.getArquivo().setDescricao(fileAssinar.getName().substring(fileAssinar.getName().lastIndexOf(File.separator) + 1, fileAssinar.getName().lastIndexOf(".")));
			obj.getArquivo().setExtensao(fileAssinar.getName().substring((fileAssinar.getName().lastIndexOf(".") + 1), fileAssinar.getName().length()));
			obj.getArquivo().setResponsavelUpload(usuarioVO);
			obj.getArquivo().setDataUpload(new Date());
			obj.getArquivo().setDataDisponibilizacao(new Date());
			obj.getArquivo().setManterDisponibilizacao(true);
			obj.getArquivo().setControlarDownload(true);
			obj.getArquivo().setDataIndisponibilizacao(null);
			obj.getArquivo().setSituacao(SituacaoArquivo.ATIVO.getValor());
			obj.getArquivo().setPastaBaseArquivo(PastaBaseArquivoEnum.DOCUMENTOS_ASSINADOS_TMP.name());
			obj.getArquivo().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.DOCUMENTOS_ASSINADOS_TMP);
			obj.getArquivo().setServidorArquivoOnline(ServidorArquivoOnlineEnum.getEnum(config.getServidorArquivoOnline()));
			obj.getArquivo().setPessoaVO(matriculaVO.getAluno());
			obj.getArquivo().setArquivoAssinadoDigitalmente(true);
			obj.getArquivo().setOrigem(OrigemArquivo.DOCUMENTO_GED.getValor());

            if (Uteis.isAtributoPreenchido(funcionario1.getPessoa())) {
                obj.getListaDocumentoAssinadoPessoa().add(new DocumentoAssinadoPessoaVO(new Date(), funcionario1.getPessoa(), TipoPessoa.FUNCIONARIO, SituacaoDocumentoAssinadoPessoaEnum.PENDENTE, obj, 1, cargoFuncionario1, tituloFuncionario1, null, funcionario1.getPessoa().getTipoAssinaturaDocumentoEnum()));
            }
            if (Uteis.isAtributoPreenchido(funcionario2.getPessoa())) {
                obj.getListaDocumentoAssinadoPessoa().add(new DocumentoAssinadoPessoaVO(new Date(), funcionario2.getPessoa(), TipoPessoa.FUNCIONARIO, SituacaoDocumentoAssinadoPessoaEnum.PENDENTE, obj, 2, cargoFuncionario2, tituloFuncionario2, null, funcionario2.getPessoa().getTipoAssinaturaDocumentoEnum()));
            }
            if (Uteis.isAtributoPreenchido(funcionario3.getPessoa())) {
                obj.getListaDocumentoAssinadoPessoa().add(new DocumentoAssinadoPessoaVO(new Date(), funcionario3.getPessoa(), TipoPessoa.FUNCIONARIO, SituacaoDocumentoAssinadoPessoaEnum.PENDENTE, obj, 3, cargoFuncionario3, tituloFuncionario3, null, funcionario3.getPessoa().getTipoAssinaturaDocumentoEnum()));
            }
            getFacadeFactory().getDocumentoAssinadoFacade().incluir(obj, false, usuarioVO, config);
            obj.getArquivo().setCodOrigem(obj.getCodigo());
            getFacadeFactory().getArquivoFacade().alterarCodigoOrigemArquivo(obj.getArquivo(), usuarioVO);
            File fileDiretorioCorreto = new File(config.getLocalUploadArquivoFixo() + File.separator + obj.getArquivo().getPastaBaseArquivo() + File.separator + obj.getArquivo().getNome());
            getFacadeFactory().getArquivoHelper().copiar(fileAssinar, fileDiretorioCorreto);
            boolean isAdicionarSelo = Uteis.isAtributoPreenchido(tipoLayout) && tipoLayout.equals("TextoPadrao");
            String caminhoArquivo = realizarVerificacaoProvedorDeAssinatura(obj, obj.getUnidadeEnsinoVO().getCodigo(), isAdicionarSelo, true, nomeArquivo, AlinhamentoAssinaturaDigitalEnum.RODAPE_DIREITA, "#000000", 40f, 200f, 8f, 12, 20, 5, 2, config, false, usuarioVO, true);
            if (Uteis.isAtributoPreenchido(caminhoArquivo) && obj.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.EXPEDICAO_DIPLOMA) && obj.getArquivo().getServidorArquivoOnline().equals(ServidorArquivoOnlineEnum.AMAZON_S3)) {
                obj.setCaminhoPreview(caminhoArquivo);
            }
            return obj;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public String realizarInclusaoDocumentoAssinadoPorEstagio(EstagioVO estagioVO, TipoOrigemDocumentoAssinadoEnum tipoOrigemDocumentoAssinadoEnum, String nomeArquivoOrigem, ConfiguracaoEstagioObrigatorioVO configEstagio, ConfiguracaoGeralSistemaVO config, UsuarioVO usuarioVO) throws Exception {
    	LocalDate dataAtual = LocalDate.now();
    	int ano = dataAtual.getYear();
    	int mes = dataAtual.getMonthValue();
        estagioVO.setDocumentoAssinadoVO(new DocumentoAssinadoVO());
        estagioVO.getDocumentoAssinadoVO().setDataRegistro(new Date());
        estagioVO.getDocumentoAssinadoVO().setUsuario(usuarioVO);
        estagioVO.getDocumentoAssinadoVO().setTipoOrigemDocumentoAssinadoEnum(tipoOrigemDocumentoAssinadoEnum);
        estagioVO.getDocumentoAssinadoVO().setMatricula(estagioVO.getMatriculaVO());
        estagioVO.getDocumentoAssinadoVO().setUnidadeEnsinoVO(estagioVO.getMatriculaVO().getUnidadeEnsino());

        estagioVO.getDocumentoAssinadoVO().getArquivo().setNome(estagioVO.getCodigo() + "_" + Uteis.removeCaractersEspeciais(estagioVO.getMatriculaVO().getMatricula()) + "_" + new Date().getTime() + ".pdf");
        estagioVO.getDocumentoAssinadoVO().getArquivo().setDescricao(tipoOrigemDocumentoAssinadoEnum.getDescricao() + "_" + estagioVO.getMatriculaVO().getMatricula() + "_" + new Date().getTime());
        estagioVO.getDocumentoAssinadoVO().getArquivo().setExtensao(".pdf");
        estagioVO.getDocumentoAssinadoVO().getArquivo().setResponsavelUpload(usuarioVO);
        estagioVO.getDocumentoAssinadoVO().getArquivo().setDataUpload(new Date());
        estagioVO.getDocumentoAssinadoVO().getArquivo().setDataDisponibilizacao(new Date());
        estagioVO.getDocumentoAssinadoVO().getArquivo().setManterDisponibilizacao(true);
        estagioVO.getDocumentoAssinadoVO().getArquivo().setControlarDownload(false);
        estagioVO.getDocumentoAssinadoVO().getArquivo().setDataIndisponibilizacao(null);
        estagioVO.getDocumentoAssinadoVO().getArquivo().setSituacao(SituacaoArquivo.ATIVO.getValor());
        estagioVO.getDocumentoAssinadoVO().getArquivo().setPastaBaseArquivo(PastaBaseArquivoEnum.ESTAGIO.name());
        estagioVO.getDocumentoAssinadoVO().getArquivo().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.ESTAGIO);
        estagioVO.getDocumentoAssinadoVO().getArquivo().setPessoaVO(estagioVO.getMatriculaVO().getAluno());
        estagioVO.getDocumentoAssinadoVO().getArquivo().setArquivoAssinadoDigitalmente(true);
        estagioVO.getDocumentoAssinadoVO().getArquivo().setPastaBaseArquivo(PastaBaseArquivoEnum.ESTAGIO.getValue() + File.separator + ano + File.separator + mes);
        estagioVO.getDocumentoAssinadoVO().getArquivo().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.ESTAGIO);
        ConfiguracaoGEDVO configGEDVO = getAplicacaoControle().getConfiguracaoGEDPorUnidadeEnsino(estagioVO.getMatriculaVO().getUnidadeEnsino().getCodigo(), usuarioVO);
        ConfiguracaoGedOrigemVO configuracaoGedOrigemVO = configGEDVO.getConfiguracaoGedOrigemVO(estagioVO.getDocumentoAssinadoVO().getTipoOrigemDocumentoAssinadoEnum());
        ProvedorDeAssinaturaEnum provedorDeAssinaturaEnum = estagioVO.getDocumentoAssinadoVO().getProvedorAssinaturaVisaoAdm() != null ? estagioVO.getDocumentoAssinadoVO().getProvedorAssinaturaVisaoAdm() : configuracaoGedOrigemVO.getProvedorAssinaturaPadraoEnum();
        if (!estagioVO.getDocumentoAssinadoVO().getTipoOrigemDocumentoAssinadoEnum().isXmlMec() && provedorDeAssinaturaEnum.isProvedorTechCert()) {
            estagioVO.getDocumentoAssinadoVO().getArquivo().setServidorArquivoOnline(ServidorArquivoOnlineEnum.TECHCERT);
        }
        if (!estagioVO.getDocumentoAssinadoVO().getTipoOrigemDocumentoAssinadoEnum().isXmlMec() && provedorDeAssinaturaEnum.isProvedorCertisign()) {
            estagioVO.getDocumentoAssinadoVO().getArquivo().setServidorArquivoOnline(ServidorArquivoOnlineEnum.CERTISIGN);
        } else {
            estagioVO.getDocumentoAssinadoVO().getArquivo().setServidorArquivoOnline(ServidorArquivoOnlineEnum.APACHE);
        }
        File fileDiretorioCorreto = new File(config.getLocalUploadArquivoFixo() + File.separator + estagioVO.getDocumentoAssinadoVO().getArquivo().getPastaBaseArquivo());
        if (!fileDiretorioCorreto.exists()) {
            fileDiretorioCorreto.mkdirs();
        }

        DocumentoAssinadoPessoaVO dap = new DocumentoAssinadoPessoaVO(new Date(), TipoPessoa.MEMBRO_COMUNIDADE, SituacaoDocumentoAssinadoPessoaEnum.PENDENTE, estagioVO.getDocumentoAssinadoVO(), 1, estagioVO.getResponsavelConcedente(), estagioVO.getEmailResponsavelConcedente(), estagioVO.getCpfResponsavelConcedente(), TipoAssinaturaDocumentoEnum.ELETRONICA);
        estagioVO.getDocumentoAssinadoVO().getListaDocumentoAssinadoPessoa().add(dap);

        if (Uteis.isAtributoPreenchido(estagioVO.getMatriculaVO().getAluno())) {
            estagioVO.getDocumentoAssinadoVO().getListaDocumentoAssinadoPessoa().add(new DocumentoAssinadoPessoaVO(new Date(), estagioVO.getMatriculaVO().getAluno(), TipoPessoa.ALUNO, SituacaoDocumentoAssinadoPessoaEnum.PENDENTE, estagioVO.getDocumentoAssinadoVO(), 2, "", "", null, estagioVO.getMatriculaVO().getAluno().getTipoAssinaturaDocumentoEnum()));
        }

        if (Uteis.isAtributoPreenchido(estagioVO.getMatriculaVO().getCurso().getFuncionarioResponsavelAssinaturaTermoEstagioVO())) {
            estagioVO.getDocumentoAssinadoVO().getListaDocumentoAssinadoPessoa().add(new DocumentoAssinadoPessoaVO(new Date(), estagioVO.getMatriculaVO().getCurso().getFuncionarioResponsavelAssinaturaTermoEstagioVO().getPessoa(), TipoPessoa.FUNCIONARIO, SituacaoDocumentoAssinadoPessoaEnum.PENDENTE, estagioVO.getDocumentoAssinadoVO(), 3, "", "", null, estagioVO.getMatriculaVO().getCurso().getFuncionarioResponsavelAssinaturaTermoEstagioVO().getPessoa().getTipoAssinaturaDocumentoEnum()));
        }

        if (Uteis.isAtributoPreenchido(configEstagio.getFuncionarioTestemunhaAssinatura1())) {
            estagioVO.getDocumentoAssinadoVO().getListaDocumentoAssinadoPessoa().add(new DocumentoAssinadoPessoaVO(new Date(), configEstagio.getFuncionarioTestemunhaAssinatura1().getPessoa(), TipoPessoa.FUNCIONARIO, SituacaoDocumentoAssinadoPessoaEnum.PENDENTE, estagioVO.getDocumentoAssinadoVO(), 4, "", "", null, configEstagio.getFuncionarioTestemunhaAssinatura1().getPessoa().getTipoAssinaturaDocumentoEnum()));
        }

        if (Uteis.isAtributoPreenchido(configEstagio.getFuncionarioTestemunhaAssinatura2())) {
            estagioVO.getDocumentoAssinadoVO().getListaDocumentoAssinadoPessoa().add(new DocumentoAssinadoPessoaVO(new Date(), configEstagio.getFuncionarioTestemunhaAssinatura2().getPessoa(), TipoPessoa.FUNCIONARIO, SituacaoDocumentoAssinadoPessoaEnum.PENDENTE, estagioVO.getDocumentoAssinadoVO(), 5, "", "", null, configEstagio.getFuncionarioTestemunhaAssinatura2().getPessoa().getTipoAssinaturaDocumentoEnum()));
        }
        incluir(estagioVO.getDocumentoAssinadoVO(), false, usuarioVO, config);
        estagioVO.getDocumentoAssinadoVO().setConcedenteDocumentoEstagio(estagioVO.getConcedenteVO().getConcedente());
        estagioVO.getDocumentoAssinadoVO().setResponsavelConcedente(estagioVO.getConcedenteVO().getResponsavelConcedente());
        estagioVO.getDocumentoAssinadoVO().setNomeGradeCurricularEstagio(estagioVO.getGradeCurricularEstagioVO().getNome());
        return realizarVerificacaoProvedorDeAssinatura(estagioVO.getDocumentoAssinadoVO(), estagioVO.getMatriculaVO().getUnidadeEnsino().getCodigo(), false, false, nomeArquivoOrigem, null, "#000000", 80f, 200f, 8f, 380, 10, 0, 0, config, true, usuarioVO, true);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
    public ArquivoVO realizarAssinaturaDocumentacaoAluno(UnidadeEnsinoVO unidadeEnsino, ArquivoVO arquivoVO, ConfiguracaoGEDVO configGEDVO, File fileAssinar, String idDocumentacao, ConfiguracaoGeralSistemaVO config, UsuarioVO usuarioVO) throws Exception {

        if (Uteis.isAtributoPreenchido(configGEDVO.getCodigo())) {
            //Realiza a assinatura
            if (configGEDVO.getConfiguracaoGedDocumentoAlunoVO().getAssinarDocumento() && configGEDVO.getConfiguracaoGedDocumentoAlunoVO().getProvedorAssinaturaPadraoEnum().isProvedorCertisign()) {
                executarPreenchimentoDocumentoAssinadoPorPessoaPorProvedorCertisign(unidadeEnsino, arquivoVO, configGEDVO, TipoOrigemDocumentoAssinadoEnum.DOCUMENTO_ALUNO, config, usuarioVO);
            }
            if (configGEDVO.getConfiguracaoGedDocumentoAlunoVO().getAssinarDocumento() && configGEDVO.getConfiguracaoGedDocumentoAlunoVO().getProvedorAssinaturaPadraoEnum().isProvedorTechCert()) {
                executarPreenchimentoDocumentoAssinadoPorPessoaPorProvedorTechCert(unidadeEnsino, arquivoVO, configGEDVO, TipoOrigemDocumentoAssinadoEnum.DOCUMENTO_ALUNO, config, usuarioVO);
            } else if (configGEDVO.getConfiguracaoGedDocumentoAlunoVO().getAssinarDocumento()) {
                if (configGEDVO.getConfiguracaoGedDocumentoAlunoVO().getApresentarSelo() && !arquivoVO.getOrigem().equals(OrigemArquivo.DOCUMENTO_GED.getValor())) {
                    adicionarSeloPDF(TipoOrigemDocumentoAssinadoEnum.DOCUMENTO_ALUNO, fileAssinar.getAbsolutePath(), config, configGEDVO);
                }

                if (configGEDVO.getConfiguracaoGedDocumentoAlunoVO().getAssinarDocumentoFuncionarioResponsavel() && !arquivoVO.getOrigem().equals(OrigemArquivo.DOCUMENTO_GED.getValor())) {
                    assinaturaDocumentoPeloFuncionario(unidadeEnsino, arquivoVO, configGEDVO, fileAssinar, idDocumentacao, config, usuarioVO);
                    arquivoVO.setArquivoAssinadoFuncionario(Boolean.TRUE);
                }
                if (configGEDVO.getConfiguracaoGedDocumentoAlunoVO().getAssinaturaUnidadeEnsino()) {
                    preencherAssinadorDigitalDocumentoPdf(fileAssinar.getAbsolutePath(), configGEDVO.getCertificadoDigitalUnidadeEnsinoVO(), configGEDVO.getSenhaCertificadoDigitalUnidadeEnsino(), arquivoVO.getPastaBaseArquivo(), arquivoVO.getNome(), AlinhamentoAssinaturaDigitalEnum.RODAPE_ESQUERDA, "#000000", 40f, 200f, 6f, 60, 20, 60, 2, idDocumentacao, config);
                    arquivoVO.setArquivoAssinadoUnidadeEnsino(Boolean.TRUE);
                }
//				if(configGEDVO.getAssinarDocumentacaoAlunoUnidadeCertificadora()) {
//					preencherAssinadorDigitalDocumentoPdf(fileAssinar.getAbsolutePath(), configGEDVO.getCertificadoDigitalUnidadeCertificadora(), configGEDVO.getSenhaCertificadoDigitalUnidadeCertificadora(), arquivoVO.getPastaBaseArquivo(), arquivoVO.getNome(), AlinhamentoAssinaturaDigitalEnum.RODAPE_ESQUERDA, "#000000", 40f, 200f, 6f, 250, 20, 250, 2, config);
//					arquivoVO.setArquivoAssinadoUnidadeCertificadora(Boolean.TRUE);
//				}
            }
            getFacadeFactory().getArquivoFacade().persistir(arquivoVO, false, usuarioVO, config);
        }
        return arquivoVO;
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
    public ArquivoVO realizarAssinaturaDocumentacaoProfessor(UnidadeEnsinoVO unidadeEnsino, ArquivoVO arquivoVO, ConfiguracaoGEDVO configGEDVO, File fileAssinar, ConfiguracaoGeralSistemaVO config, UsuarioVO usuarioVO) throws Exception {

        if (Uteis.isAtributoPreenchido(configGEDVO.getCodigo())) {
            //Realiza a assinatura
            if (configGEDVO.getConfiguracaoGedDocumentoProfessorVO().getAssinarDocumento() && configGEDVO.getConfiguracaoGedDocumentoProfessorVO().getProvedorAssinaturaPadraoEnum().isProvedorCertisign()) {
                executarPreenchimentoDocumentoAssinadoPorPessoaPorProvedorCertisign(unidadeEnsino, arquivoVO, configGEDVO, TipoOrigemDocumentoAssinadoEnum.DOCUMENTO_PROFESSOR, config, usuarioVO);

            }
            if (configGEDVO.getConfiguracaoGedDocumentoProfessorVO().getAssinarDocumento() && configGEDVO.getConfiguracaoGedDocumentoProfessorVO().getProvedorAssinaturaPadraoEnum().isProvedorTechCert()) {
                executarPreenchimentoDocumentoAssinadoPorPessoaPorProvedorTechCert(unidadeEnsino, arquivoVO, configGEDVO, TipoOrigemDocumentoAssinadoEnum.DOCUMENTO_PROFESSOR, config, usuarioVO);
            } else if (configGEDVO.getConfiguracaoGedDocumentoProfessorVO().getAssinarDocumento()) {
                adicionarSeloPDF(TipoOrigemDocumentoAssinadoEnum.DOCUMENTO_PROFESSOR, fileAssinar.getAbsolutePath(), config, configGEDVO);
                if (configGEDVO.getConfiguracaoGedDocumentoProfessorVO().getAssinarDocumentoFuncionarioResponsavel()) {
                    assinaturaDocumentoPeloFuncionario(unidadeEnsino, arquivoVO, configGEDVO, fileAssinar, config, usuarioVO);
                    arquivoVO.setArquivoAssinadoFuncionario(Boolean.TRUE);
                }
                if (configGEDVO.getConfiguracaoGedDocumentoProfessorVO().getAssinaturaUnidadeEnsino()) {
                    preencherAssinadorDigitalDocumentoPdf(fileAssinar.getAbsolutePath(), configGEDVO.getCertificadoDigitalUnidadeEnsinoVO(), configGEDVO.getSenhaCertificadoDigitalUnidadeEnsino(), arquivoVO.getPastaBaseArquivo(), arquivoVO.getNome(), AlinhamentoAssinaturaDigitalEnum.RODAPE_ESQUERDA, "#000000", 40f, 200f, 6f, 60, 20, 60, 2, config);
                    arquivoVO.setArquivoAssinadoUnidadeEnsino(Boolean.TRUE);
                }
//				if(configGEDVO.getAssinarDocumentacaoAlunoUnidadeCertificadora()) {
//					preencherAssinadorDigitalDocumentoPdf(fileAssinar.getAbsolutePath(), configGEDVO.getCertificadoDigitalUnidadeCertificadora(), configGEDVO.getSenhaCertificadoDigitalUnidadeCertificadora(), arquivoVO.getPastaBaseArquivo(), arquivoVO.getNome(), AlinhamentoAssinaturaDigitalEnum.RODAPE_ESQUERDA, "#000000", 40f, 200f, 6f, 250, 20, 250, 2, config);
//					arquivoVO.setArquivoAssinadoUnidadeCertificadora(Boolean.TRUE);
//				}
				
			}
		}
		return arquivoVO;
	}
	
	private void executarPreenchimentoDocumentoAssinadoPorPessoaPorProvedorCertisign(UnidadeEnsinoVO unidadeEnsino, ArquivoVO arquivoVO,  ConfiguracaoGEDVO configGEDVO, TipoOrigemDocumentoAssinadoEnum tipoOrigemDocumentoAssinadoEnum, ConfiguracaoGeralSistemaVO config, UsuarioVO usuarioVO) throws Exception {
		DocumentoAssinadoVO doc = new DocumentoAssinadoVO();
		doc.setArquivo(arquivoVO);
		doc.setDataRegistro(new Date());
		doc.setUsuario(usuarioVO);
		doc.setTipoOrigemDocumentoAssinadoEnum(tipoOrigemDocumentoAssinadoEnum);
		doc.setUnidadeEnsinoVO(unidadeEnsino);
		doc.setProvedorDeAssinaturaEnum(tipoOrigemDocumentoAssinadoEnum.isDocumentoAluno() ? configGEDVO.getConfiguracaoGedDocumentoAlunoVO().getProvedorAssinaturaPadraoEnum() : configGEDVO.getConfiguracaoGedDocumentoProfessorVO().getProvedorAssinaturaPadraoEnum());
//		doc.setTurma(turma);
//		doc.setDisciplina(disciplina);
//		doc.setAno(ano);
//		doc.setSemestre(semestre);
		PessoaVO pes = getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(usuarioVO.getPessoa().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
		doc.getListaDocumentoAssinadoPessoa().add(new DocumentoAssinadoPessoaVO(new Date(), pes, TipoPessoa.FUNCIONARIO, SituacaoDocumentoAssinadoPessoaEnum.PENDENTE, doc, 1, "", "",false, pes.getTipoAssinaturaDocumentoEnum()));
		incluir(doc, false, usuarioVO, config);
		String arquivoOrigem = config.getLocalUploadArquivoFixo() +File.separator+ doc.getArquivo().getPastaBaseArquivo() +File.separator+ doc.getArquivo().getNome();
		executarAssinaturaProvedorCertiSign(doc, arquivoOrigem, configGEDVO, config, usuarioVO);
	}

    private void executarPreenchimentoDocumentoAssinadoPorPessoaPorProvedorTechCert(UnidadeEnsinoVO unidadeEnsino, ArquivoVO arquivoVO, ConfiguracaoGEDVO configGEDVO, TipoOrigemDocumentoAssinadoEnum tipoOrigemDocumentoAssinadoEnum, ConfiguracaoGeralSistemaVO config, UsuarioVO usuarioVO) throws Exception {
        DocumentoAssinadoVO doc = new DocumentoAssinadoVO();
        doc.setArquivo(arquivoVO);
        doc.setDataRegistro(new Date());
        doc.setUsuario(usuarioVO);
        doc.setTipoOrigemDocumentoAssinadoEnum(tipoOrigemDocumentoAssinadoEnum);
        doc.setUnidadeEnsinoVO(unidadeEnsino);
        doc.setProvedorDeAssinaturaEnum(tipoOrigemDocumentoAssinadoEnum.isDocumentoAluno() ? configGEDVO.getConfiguracaoGedDocumentoAlunoVO().getProvedorAssinaturaPadraoEnum() : configGEDVO.getConfiguracaoGedDocumentoProfessorVO().getProvedorAssinaturaPadraoEnum());
//		doc.setTurma(turma);
//		doc.setDisciplina(disciplina);
//		doc.setAno(ano);
//		doc.setSemestre(semestre);
        PessoaVO pes = getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(usuarioVO.getPessoa().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
        doc.getListaDocumentoAssinadoPessoa().add(new DocumentoAssinadoPessoaVO(new Date(), pes, TipoPessoa.FUNCIONARIO, SituacaoDocumentoAssinadoPessoaEnum.PENDENTE, doc, 1, "", "", false, pes.getTipoAssinaturaDocumentoEnum()));
        incluir(doc, false, usuarioVO, config);
        String arquivoOrigem = config.getLocalUploadArquivoFixo() + File.separator + doc.getArquivo().getPastaBaseArquivo() + File.separator + doc.getArquivo().getNome();
        executarAssinaturaProvedorTechCert(doc, arquivoOrigem, configGEDVO, config, usuarioVO);
    }

    /**
     * Método Responsável por Assinar Digitalmente o Documento entregue do aluno
     *
     * @param matriculaVO
     * @param fileAssinar
     * @param config
     * @param funcionarioPrincipal
     * @param usuarioVO
     * @param obj
     * @throws IOException
     * @throws Exception
     */
    private void assinaturaDocumentoPeloFuncionario(UnidadeEnsinoVO unidadeEnsino, ArquivoVO arquivo, ConfiguracaoGEDVO configGEDVO, File fileAssinar, ConfiguracaoGeralSistemaVO config, UsuarioVO usuarioVO) throws IOException, Exception {
        assinaturaDocumentoPeloFuncionario(unidadeEnsino, arquivo, configGEDVO, fileAssinar, null, config, usuarioVO);
    }

    private void assinaturaDocumentoPeloFuncionario(UnidadeEnsinoVO unidadeEnsino, ArquivoVO arquivo, ConfiguracaoGEDVO configGEDVO, File fileAssinar, String idDocumentacao, ConfiguracaoGeralSistemaVO config, UsuarioVO usuarioVO) throws IOException, Exception {
        PessoaVO pessoaVO = getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(usuarioVO.getPessoa().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
        pessoaVO.setSenhaCertificadoParaDocumento(getFacadeFactory().getPessoaFacade().consultarSenhaCertificadoParaDocumento(pessoaVO.getCodigo()));
        ArquivoVO certificadoParaDocumento = validarCertificadoParaDocumento(unidadeEnsino, null, config, usuarioVO, pessoaVO);
        preencherAssinadorDigitalDocumentoPdf(fileAssinar.getAbsolutePath(), certificadoParaDocumento, pessoaVO.getSenhaCertificadoParaDocumento(), arquivo.getPastaBaseArquivo(), arquivo.getNome(), AlinhamentoAssinaturaDigitalEnum.RODAPE_ESQUERDA, "#000000", 40f, 200f, 6f, 400, 20, 400, 2, idDocumentacao, config);
    }

	/**
	 * @param arquivoOrigem
	 * @param certificadoDigitalVO
	 * @param senhaCertificadoDigital
	 * @param caminhoPastaBase
	 * @param nomeArquivo
	 * @param alinhamentoAssinaturaDigital
	 * @param corAssinatura
	 * @param alturaAssinatura
	 * @param larguraAssinatura
	 * @param tamanhoFonte
	 * @param coordenadaLLX
	 * @param coordenadaLLY
	 * @param coordenadaURX
	 * @param coordenadaURY
	 * @param config
	 * @throws Exception
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private void preencherAssinadorDigitalDocumentoPdf(String arquivoOrigem, ArquivoVO certificadoDigitalVO, String senhaCertificadoDigital,String caminhoPastaBase, String nomeArquivo, AlinhamentoAssinaturaDigitalEnum alinhamentoAssinaturaDigital, String corAssinatura, Float alturaAssinatura, Float larguraAssinatura, float tamanhoFonte, int coordenadaLLX, int coordenadaLLY, int coordenadaURX, int coordenadaURY, ConfiguracaoGeralSistemaVO config) throws Exception {
		preencherAssinadorDigitalDocumentoPdf(arquivoOrigem, certificadoDigitalVO, senhaCertificadoDigital, caminhoPastaBase, nomeArquivo, alinhamentoAssinaturaDigital, corAssinatura, alturaAssinatura, larguraAssinatura, tamanhoFonte, coordenadaLLX, coordenadaLLY, coordenadaURX, coordenadaURY, null, config);
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private void preencherAssinadorDigitalDocumentoPdf(String arquivoOrigem, ArquivoVO certificadoDigitalVO, String senhaCertificadoDigital,String caminhoPastaBase, String nomeArquivo, AlinhamentoAssinaturaDigitalEnum alinhamentoAssinaturaDigital, String corAssinatura, Float alturaAssinatura, Float larguraAssinatura, float tamanhoFonte, int coordenadaLLX, int coordenadaLLY, int coordenadaURX, int coordenadaURY, String idDocumentacao, ConfiguracaoGeralSistemaVO config) throws Exception {
		String caminhoCertificado = "";
		validarDadosCertificadoDigital(certificadoDigitalVO, senhaCertificadoDigital);
		
		if(Uteis.isAtributoPreenchido(certificadoDigitalVO.getPastaBaseArquivo())) {
			caminhoCertificado = config.getLocalUploadArquivoFixo() + File.separator + certificadoDigitalVO.getPastaBaseArquivo() + File.separator + certificadoDigitalVO.getNome();
		}else{
			caminhoCertificado = config.getLocalUploadArquivoFixo() + File.separator + config.getCertificadoParaDocumento().getPastaBaseArquivo() + File.separator + certificadoDigitalVO.getNome();
		}
		
		AssinaturaDigialDocumentoPDF assinador = new AssinaturaDigialDocumentoPDF();
		assinador.setToken(true);
		assinador.setPathKeyStore(caminhoCertificado);
		assinador.setSenhaKeyStore(senhaCertificadoDigital);
		assinador.setArquivoOrigem(arquivoOrigem);
		assinador.setCaminhoArquivoDestino(config.getLocalUploadArquivoFixo() + File.separator + caminhoPastaBase);
		assinador.setNomeArquivoDestino(nomeArquivo);
		assinador.setIsValidarArquivoExistente(true);
		assinador.setRazaoCertificado(Uteis.isAtributoPreenchido(idDocumentacao) ? StringUtils.leftPad(idDocumentacao, 12, "0") : StringUtils.leftPad("0", 12, "0"));
		assinador.setDataAssinatura(new Date());
		assinador.setCorAssinaturaDigitalmente(corAssinatura);
		assinador.setAlturaAssinatura(alturaAssinatura);
		assinador.setLarguraAssinatura(larguraAssinatura);
		assinador.setTamanhoFonte(tamanhoFonte);
		assinador.setCoordenadaLLX(coordenadaLLX);
		assinador.setCoordenadaLLY(coordenadaLLY);
		assinador.setCoordenadaURX(coordenadaURX);
		assinador.setCoordenadaURY(coordenadaURY);
		if (alinhamentoAssinaturaDigital.isRodapeDireita()) {
			assinador.setIsPosicaoAssinaturaCima(false);
			assinador.setIsPosicaoAssinaturaEsquerdo(false);
		} else if (alinhamentoAssinaturaDigital.isRodapeEsquerda()) {
			assinador.setIsPosicaoAssinaturaCima(false);
			assinador.setIsPosicaoAssinaturaEsquerdo(true);
		} else if (alinhamentoAssinaturaDigital.isTopoDireita()) {
			assinador.setIsPosicaoAssinaturaCima(true);
			assinador.setIsPosicaoAssinaturaEsquerdo(false);
		} else if (alinhamentoAssinaturaDigital.isTopoEsquerda()) {
			assinador.setIsPosicaoAssinaturaCima(true);
			assinador.setIsPosicaoAssinaturaEsquerdo(true);
		}
		assinador.realizarGeracaoDocumentoPdf();
		
	}
	
	public void validarDadosCertificadoDigitalUploadArquivoInstitucional(ArquivoVO arquivoVO, ConfiguracaoGEDVO configuracaoGEDVO, UsuarioVO usuarioVO) throws Exception {
//		ProvedorDeAssinaturaEnum provedorDeAssinaturaEnum = arquivoVO.getProvedorDeAssinaturaEnum() != null ? arquivoVO.getProvedorDeAssinaturaEnum() : configuracaoGEDVO.getConfiguracaoGedUploadInstitucionalVO().getProvedorAssinaturaPadraoEnum(); 
//		if (arquivoVO.getAssinarCertificadoDigitalUnidadeEnsino() && provedorDeAssinaturaEnum.isProvedorSei() &&  !Uteis.isAtributoPreenchido(configuracaoGEDVO.getCertificadoDigitalUnidadeEnsinoVO().getCodigo())) {
//			throw new Exception("Não foi encontrado Certificado da Unidade de Ensino na Configuração GED "+ configuracaoGEDVO.getNome().toUpperCase()+". Para realizar a assinatura digital é necessário adicionar o Certificado no cadastro da Configuração GED.");
//		}
//		if (arquivoVO.getAssinarCertificadoDigitalUnidadeCertificadora() && !Uteis.isAtributoPreenchido(configuracaoGEDVO.getCertificadoDigitalUnidadeCertificadora().getCodigo())) {
//			throw new Exception("Não foi encontrado Certificado da Unidade Certificadora na Configuração GED "+ configuracaoGEDVO.getNome().toUpperCase()+". Para realizar a assinatura digital é necessário adicionar o Certificado no cadastro da Configuração GED.");
//		}
	}
	
	public void validarDadosExtensaoArquivo(ArquivoVO arquivoVO) throws Exception {
		if (!arquivoVO.getIsPdF() && !arquivoVO.getIsImagem()) {
			throw new Exception("Extensão de Arquivo inválida para realizar assinatura digital, favor selecionar um arquivo no formato PDF ou Imagem!");
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public DocumentoAssinadoVO realizarAssinaturaUploadArquivoInstitucional(ArquivoVO arquivoVO, List<FuncionarioVO> listaConsultaFuncionarioVOs, ConfiguracaoGeralSistemaVO config, UsuarioVO usuarioVO) throws Exception {
		validarDadosExtensaoArquivo(arquivoVO);
		DocumentoAssinadoVO obj = null;
		ArquivoVO arquivoCloneVO = (ArquivoVO) arquivoVO.clone();
		try {
			ConfiguracaoGEDVO configGEDVO = getFacadeFactory().getConfiguracaoGEDFacade().consultarPorUnidadeEnsino(arquivoCloneVO.getUnidadeEnsinoVO().getCodigo(), false, usuarioVO);
			if (!Uteis.isAtributoPreenchido(configGEDVO.getCodigo())) {
				if (!Uteis.isAtributoPreenchido(arquivoCloneVO.getUnidadeEnsinoVO().getNome())) {
					arquivoCloneVO.setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCodigo(arquivoCloneVO.getUnidadeEnsinoVO().getCodigo(), false, usuarioVO));
				}
				throw new Exception("Configuração GED não foi encontrada para a Unidade de Ensino "+ arquivoCloneVO.getUnidadeEnsinoVO().getNome().toUpperCase() +". Por isso não é possível assinar digitalmente.");
			}
			validarDadosCertificadoDigitalUploadArquivoInstitucional(arquivoCloneVO, configGEDVO, usuarioVO);
			String nomeArquivo = arquivoCloneVO.getNome();
			String caminhoPdf = "";
			String nomeArquivoOriginalImagem = "";
			
			// Caso o arquivo seja uma imagem será feito a conversão da imagem para PDF afim de receber a assinatura
			if (arquivoCloneVO.getIsImagem()) {
				nomeArquivoOriginalImagem = arquivoCloneVO.getNome();
				List<String> files = new ArrayList<String>();
				files.add(config.getLocalUploadArquivoFixo() + File.separator + arquivoCloneVO.getPastaBaseArquivo() + File.separator + arquivoCloneVO.getNome());
				String caminhoBasePdf = config.getLocalUploadArquivoFixo() + File.separator + arquivoCloneVO.getPastaBaseArquivo() + File.separator;
				String nomeNovoArquivo = usuarioVO.getCodigo()+""+ (new Date().getTime()) + ".pdf";
				caminhoPdf = caminhoBasePdf + nomeNovoArquivo;
				ConverterImgToPdf.realizarConversaoPdf(files, caminhoPdf);
				
				arquivoCloneVO.setNome(nomeNovoArquivo);
				getFacadeFactory().getArquivoFacade().alterarNomeArquivo(arquivoCloneVO.getCodigo(), nomeNovoArquivo, usuarioVO);

				String novaDescricaoArquivo = arquivoCloneVO.getDescricao().substring(0, arquivoCloneVO.getDescricao().indexOf(".")) + ".pdf";
				getFacadeFactory().getArquivoFacade().alterarDescricaoArquivo(arquivoCloneVO.getCodigo(), novaDescricaoArquivo, usuarioVO);
			}
			String diretorioArquivo = "";
			if (caminhoPdf.equals("")) {
				diretorioArquivo = config.getLocalUploadArquivoFixo() + File.separator + arquivoCloneVO.getPastaBaseArquivo() + File.separator + nomeArquivo;
			} else {
				diretorioArquivo = caminhoPdf;
			}
			obj = new DocumentoAssinadoVO();
			obj.setDataRegistro(new Date());
			obj.setUsuario(usuarioVO);
			obj.setTipoOrigemDocumentoAssinadoEnum(TipoOrigemDocumentoAssinadoEnum.UPLOAD_INSTITUCIONAL);
			obj.setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCodigo(arquivoCloneVO.getUnidadeEnsinoVO().getCodigo(), false, usuarioVO));
			obj.setArquivo(arquivoCloneVO);
//			obj.setProvedorAssinaturaVisaoAdm(arquivoCloneVO.getProvedorDeAssinaturaEnum());
			
			
			for (FuncionarioVO funcionarioVO : listaConsultaFuncionarioVOs) {
				obj.getListaDocumentoAssinadoPessoa().add(new DocumentoAssinadoPessoaVO(new Date(), funcionarioVO.getPessoa(), TipoPessoa.FUNCIONARIO, SituacaoDocumentoAssinadoPessoaEnum.PENDENTE, obj, 0, "", "", null, funcionarioVO.getPessoa().getTipoAssinaturaDocumentoEnum()));
			}
			getFacadeFactory().getDocumentoAssinadoFacade().incluir(obj, false, usuarioVO, config);
			
			// Caso exista funcionário para assinar o arquivo somente poderá ser disponibilizado após todas as assinaturas dos funcionários
			if (!listaConsultaFuncionarioVOs.isEmpty()) {
				obj.getArquivo().setManterDisponibilizacao(false);
				obj.getArquivo().setApresentarPortalProfessor(false);
				obj.getArquivo().setApresentarPortalCoordenador(false);
				obj.getArquivo().setApresentarPortalAluno(false);
				getFacadeFactory().getArquivoFacade().alterarApresentacaoArquivoInstitucionalProfessorCoordenadorAluno(obj.getArquivo(), false, usuarioVO);
			}else if(arquivoVO.getAssinarCertificadoDigitalUnidadeEnsino()) {
				obj.getArquivo().setArquivoAssinadoDigitalmente(true);
				getFacadeFactory().getArquivoFacade().alterarApresentacaoArquivoInstitucionalProfessorCoordenadorAluno(obj.getArquivo(), false, usuarioVO);
			}
			realizarVerificacaoProvedorDeAssinatura(obj, arquivoCloneVO.getUnidadeEnsinoVO().getCodigo(), true, true, diretorioArquivo, AlinhamentoAssinaturaDigitalEnum.RODAPE_DIREITA, "#000000",80f, 200f, 6f, 12, 20, 5, 2, config, true, usuarioVO,true);
//			adicionarSeloPDF(obj, diretorioArquivo, config, configGEDVO);
//			adicionarQRCodePDF(obj, diretorioArquivo, config, configGEDVO);
//			if(Uteis.isAtributoPreenchido(configGEDVO)) {
//				executarAssinaturaParaDocumento(obj, configGEDVO, diretorioArquivo,  "#000000", config, false, usuarioVO, false, obj.getTipoOrigemDocumentoAssinadoEnum());
//			}else {
//				executarAssinaturaParaDocumento(obj, Uteis.isAtributoPreenchido(configGEDVO) ? configGEDVO.getCertificadoDigitalUnidadeEnsinoVO() : config.getCertificadoParaDocumento(),  Uteis.isAtributoPreenchido(configGEDVO) ? configGEDVO.getSenhaCertificadoDigitalUnidadeEnsino() : config.getSenhaCertificadoParaDocumento(), diretorioArquivo, AlinhamentoAssinaturaDigitalEnum.RODAPE_DIREITA, "#000000",80f, 200f, 6f, 12, 20, 5, 2, config, true, usuarioVO,true);	
//			}
			if (!nomeArquivoOriginalImagem.equals("")) {
				FileUtils.forceDelete(new File(config.getLocalUploadArquivoFixo() + File.separator + obj.getArquivo().getPastaBaseArquivo() + File.separator + nomeArquivoOriginalImagem));
			}
			return obj;
		} catch (Exception e) {
			arquivoVO.setManterDisponibilizacao(true);
			arquivoVO.setApresentarPortalProfessor(true);
			arquivoVO.setApresentarPortalCoordenador(true);
			arquivoVO.setApresentarPortalAluno(true);
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public String realizarInclusaoDocumentoAssinadoPorBoletimAcademico(String nomeArquivoOrigem, MatriculaVO matricula, GradeCurricularVO gradeCurricular, TurmaVO turma, DisciplinaVO disciplina, String ano, String semestre, TipoOrigemDocumentoAssinadoEnum tipoOrigemDocumentoAssinado, ProvedorDeAssinaturaEnum provedorDeAssinaturaEnum,String corAssinatura, Float alturaAssinatura, Float larguraAssinatura, FuncionarioVO funcionarioPrincipalVO, String cargoFuncionario1, String tituloFuncionario1, FuncionarioVO funcionarioSecundarioVO, String cargoFuncionario2, String tituloFuncionario2,  ConfiguracaoGeralSistemaVO config, UsuarioVO usuarioVO) throws Exception {
		DocumentoAssinadoVO obj = new DocumentoAssinadoVO();
		obj.setDataRegistro(new Date());
		obj.setUsuario(usuarioVO);
		obj.setTipoOrigemDocumentoAssinadoEnum(tipoOrigemDocumentoAssinado);
		obj.setMatricula(matricula);
		obj.setGradecurricular(gradeCurricular);
		obj.setTurma(turma);
		if (Uteis.isAtributoPreenchido(matricula.getMatricula())) {
			obj.setUnidadeEnsinoVO(matricula.getUnidadeEnsino());
		}else {
			obj.setUnidadeEnsinoVO(turma.getUnidadeEnsino());
		}
		obj.setDisciplina(disciplina);
		obj.setAno(ano);
		obj.setSemestre(semestre);
		if (Uteis.isAtributoPreenchido(matricula.getMatricula())) {
			obj.getArquivo().setNome(Uteis.removeCaractersEspeciais(matricula.getMatricula()) + "_" + usuarioVO.getCodigo() + "_" + new Date().getTime() + ".pdf");
			obj.getArquivo().setDescricao(tipoOrigemDocumentoAssinado.getDescricao() + "_" + matricula.getMatricula() + "_" + new Date().getTime());
		} else {
			obj.getArquivo().setNome(turma.getIdentificadorTurma() + "_" + usuarioVO.getCodigo() + "_" + new Date().getTime() + ".pdf");
			obj.getArquivo().setDescricao(tipoOrigemDocumentoAssinado.getDescricao() + "_" + matricula.getMatricula() + "_" + new Date().getTime());
		}
		obj.getArquivo().setExtensao(".pdf");
		obj.getArquivo().setResponsavelUpload(usuarioVO);
		obj.getArquivo().setDataUpload(new Date());
		obj.getArquivo().setDataDisponibilizacao(new Date());
		obj.getArquivo().setManterDisponibilizacao(true);
		
		obj.getArquivo().setControlarDownload(true);
		obj.getArquivo().setDataIndisponibilizacao(null);
		obj.getArquivo().setSituacao(SituacaoArquivo.ATIVO.getValor());
		obj.getArquivo().setPastaBaseArquivo(PastaBaseArquivoEnum.DOCUMENTOS_ASSINADOS_TMP.name());
		obj.getArquivo().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.DOCUMENTOS_ASSINADOS_TMP);
		obj.getArquivo().setPessoaVO(matricula.getAluno());
		obj.getArquivo().setTurma(turma);
		obj.getArquivo().setDisciplina(disciplina);
		obj.getArquivo().setArquivoAssinadoDigitalmente(true);
		obj.setProvedorAssinaturaVisaoAdm(provedorDeAssinaturaEnum);
		if (Uteis.isAtributoPreenchido(funcionarioPrincipalVO.getCodigo())) {
			obj.getListaDocumentoAssinadoPessoa().add(new DocumentoAssinadoPessoaVO(new Date(), funcionarioPrincipalVO.getPessoa(), TipoPessoa.FUNCIONARIO, SituacaoDocumentoAssinadoPessoaEnum.PENDENTE, obj, 1, cargoFuncionario1, tituloFuncionario1, null, funcionarioPrincipalVO.getPessoa().getTipoAssinaturaDocumentoEnum()));
		}
		if (Uteis.isAtributoPreenchido(funcionarioSecundarioVO.getCodigo())) {
			obj.getListaDocumentoAssinadoPessoa().add(new DocumentoAssinadoPessoaVO(new Date(), funcionarioSecundarioVO.getPessoa(), TipoPessoa.FUNCIONARIO, SituacaoDocumentoAssinadoPessoaEnum.PENDENTE, obj, 2, cargoFuncionario2, tituloFuncionario2, null, funcionarioSecundarioVO.getPessoa().getTipoAssinaturaDocumentoEnum()));
		}
		incluir(obj, false, usuarioVO, config);
		Integer unidadeEnsino = Uteis.isAtributoPreenchido(matricula.getMatricula()) ? matricula.getUnidadeEnsino().getCodigo() : turma.getUnidadeEnsino().getCodigo();
		
		return realizarVerificacaoProvedorDeAssinatura(obj, unidadeEnsino, true, true, nomeArquivoOrigem, AlinhamentoAssinaturaDigitalEnum.RODAPE_DIREITA, corAssinatura, alturaAssinatura, larguraAssinatura, 6f, 12, 20, 5, 2, config, true, usuarioVO, true);
		
//		ConfiguracaoGEDVO configGEDVO = null;
//		if (Uteis.isAtributoPreenchido(matricula.getMatricula())) {
//			configGEDVO = getAplicacaoControle().getConfiguracaoGEDPorUnidadeEnsino(matricula.getUnidadeEnsino().getCodigo(), usuarioVO);
//		} else {
//			configGEDVO = getAplicacaoControle().getConfiguracaoGEDPorUnidadeEnsino(turma.getUnidadeEnsino().getCodigo(), usuarioVO);
//		}		
//		
//		String arquivoOrigem =  UteisJSF.getCaminhoWeb() + "relatorio/" + nomeArquivoOrigem;
//		adicionarSeloPDF(obj, arquivoOrigem, config, configGEDVO);
//		adicionarQRCodePDF(obj, arquivoOrigem, config, configGEDVO);
//		if(Uteis.isAtributoPreenchido(configGEDVO)) {
//			return executarAssinaturaParaDocumento(obj, configGEDVO, arquivoOrigem, corAssinatura, config, true, usuarioVO,true, obj.getTipoOrigemDocumentoAssinadoEnum());
//		}
//		return executarAssinaturaParaDocumento(obj, Uteis.isAtributoPreenchido(configGEDVO) ? configGEDVO.getCertificadoDigitalUnidadeEnsinoVO() : config.getCertificadoParaDocumento(),  Uteis.isAtributoPreenchido(configGEDVO) ? configGEDVO.getSenhaCertificadoDigitalUnidadeEnsino() : config.getSenhaCertificadoParaDocumento(), arquivoOrigem, AlinhamentoAssinaturaDigitalEnum.RODAPE_DIREITA, corAssinatura, alturaAssinatura, larguraAssinatura, 6f, 12, 20, 5, 2, config, true, usuarioVO,true);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public String realizarInclusaoDocumentoAssinadoPorHistoricoAluno(String nomeArquivoOrigem, MatriculaVO matricula, GradeCurricularVO gradeCurricular, TurmaVO turma, DisciplinaVO disciplina, String ano, String semestre, TipoOrigemDocumentoAssinadoEnum tipoOrigemDocumentoAssinado, ProvedorDeAssinaturaEnum provedorDeAssinaturaEnum,String corAssinatura, Float alturaAssinatura, Float larguraAssinatura, FuncionarioVO funcionarioPrincipalVO, String cargoFuncionario1, String tituloFuncionario1, FuncionarioVO funcionarioSecundarioVO, String cargoFuncionario2, String tituloFuncionario2, 
			FuncionarioVO funcionarioTerciarioVO, String cargoFuncionario3, String tituloFuncionario3, ConfiguracaoGeralSistemaVO config, Integer codOrigemRequerimento, UsuarioVO usuarioVO) throws Exception {		
		DocumentoAssinadoVO obj = new DocumentoAssinadoVO();
		obj.setDataRegistro(new Date());
		obj.setUsuario(usuarioVO);
		obj.setTipoOrigemDocumentoAssinadoEnum(tipoOrigemDocumentoAssinado);
		obj.setMatricula(matricula);
		obj.setGradecurricular(gradeCurricular);
		obj.setTurma(turma);
		obj.setUnidadeEnsinoVO(matricula.getUnidadeEnsino());
		obj.setDisciplina(disciplina);
		obj.setAno(ano);
		obj.setSemestre(semestre);
		if (Uteis.isAtributoPreenchido(matricula.getMatricula())) {
			obj.getArquivo().setNome(Uteis.removeCaractersEspeciais(matricula.getMatricula()) + "_" + usuarioVO.getCodigo() + "_" + new Date().getTime() + ".pdf");
			obj.getArquivo().setDescricao(tipoOrigemDocumentoAssinado.getDescricao() + "_" + matricula.getMatricula() + "_" + new Date().getTime());
		} else {
			obj.getArquivo().setNome(turma.getIdentificadorTurma() + "_" + usuarioVO.getCodigo() + "_" + new Date().getTime() + ".pdf");
			obj.getArquivo().setDescricao(tipoOrigemDocumentoAssinado.getDescricao() + "_" + matricula.getMatricula() + "_" + new Date().getTime());
		}
		obj.getArquivo().setExtensao(".pdf");
		obj.getArquivo().setResponsavelUpload(usuarioVO);
		obj.getArquivo().setDataUpload(new Date());
		obj.getArquivo().setDataDisponibilizacao(new Date());
		obj.getArquivo().setManterDisponibilizacao(true);
		
		obj.getArquivo().setControlarDownload(true);
		obj.getArquivo().setDataIndisponibilizacao(null);
		obj.getArquivo().setSituacao(SituacaoArquivo.ATIVO.getValor());
		obj.getArquivo().setPastaBaseArquivo(PastaBaseArquivoEnum.DOCUMENTOS_ASSINADOS_TMP.name());
		obj.getArquivo().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.DOCUMENTOS_ASSINADOS_TMP);
		obj.getArquivo().setPessoaVO(matricula.getAluno());
		obj.getArquivo().setTurma(turma);
		obj.getArquivo().setDisciplina(disciplina);
		obj.getArquivo().setArquivoAssinadoDigitalmente(true);
		obj.setProvedorAssinaturaVisaoAdm(provedorDeAssinaturaEnum);
		if (Uteis.isAtributoPreenchido(funcionarioPrincipalVO)) {
			obj.getListaDocumentoAssinadoPessoa().add(new DocumentoAssinadoPessoaVO(new Date(), funcionarioPrincipalVO.getPessoa(), TipoPessoa.FUNCIONARIO, SituacaoDocumentoAssinadoPessoaEnum.PENDENTE, obj, 1, cargoFuncionario1, tituloFuncionario1, null, funcionarioPrincipalVO.getPessoa().getTipoAssinaturaDocumentoEnum()));
		}
		if (Uteis.isAtributoPreenchido(funcionarioSecundarioVO)) {
			obj.getListaDocumentoAssinadoPessoa().add(new DocumentoAssinadoPessoaVO(new Date(), funcionarioSecundarioVO.getPessoa(), TipoPessoa.FUNCIONARIO, SituacaoDocumentoAssinadoPessoaEnum.PENDENTE, obj, 2, cargoFuncionario2, tituloFuncionario2, null, funcionarioSecundarioVO.getPessoa().getTipoAssinaturaDocumentoEnum()));
		}
		if (Uteis.isAtributoPreenchido(funcionarioTerciarioVO)) {
			obj.getListaDocumentoAssinadoPessoa().add(new DocumentoAssinadoPessoaVO(new Date(), funcionarioTerciarioVO.getPessoa(), TipoPessoa.FUNCIONARIO, SituacaoDocumentoAssinadoPessoaEnum.PENDENTE, obj, 3, cargoFuncionario3, tituloFuncionario3, null, funcionarioTerciarioVO.getPessoa().getTipoAssinaturaDocumentoEnum()));
		}
		if (Uteis.isAtributoPreenchido(codOrigemRequerimento)) {
			obj.getArquivo().setCodOrigem(codOrigemRequerimento);
		}
		incluir(obj, false, usuarioVO, config);
		Integer unidadeEnsino = Uteis.isAtributoPreenchido(matricula.getMatricula()) ? matricula.getUnidadeEnsino().getCodigo() : turma.getUnidadeEnsino().getCodigo();
		return realizarVerificacaoProvedorDeAssinatura(obj, unidadeEnsino, true, true, nomeArquivoOrigem, null, "#000000", 80f, 200f, 8f, 380, 10, 0, 0, config, true, usuarioVO, true);
		
//		String arquivoOrigem =  UteisJSF.getCaminhoWeb() + "relatorio/" + nomeArquivoOrigem;		
//		ConfiguracaoGEDVO configGEDVO = null;
//		if (Uteis.isAtributoPreenchido(matricula.getMatricula())) {
//			configGEDVO = getFacadeFactory().getConfiguracaoGEDFacade().consultarPorUnidadeEnsino(matricula.getUnidadeEnsino().getCodigo(), false, usuarioVO);
//		} else {
//			configGEDVO = getFacadeFactory().getConfiguracaoGEDFacade().consultarPorUnidadeEnsino(turma.getUnidadeEnsino().getCodigo(), false, usuarioVO);
//		}
//		if(Uteis.isAtributoPreenchido(configGEDVO)) {
//			if(configGEDVO.getProvedorDeAssinaturaEnum().isProvedorCertisign()) {
//				return executarAssinaturaProvedorCertiSign(obj, arquivoOrigem, configGEDVO, config, usuarioVO);
//			}else {
//				adicionarSeloPDF(obj, arquivoOrigem, config, configGEDVO);
//				adicionarQRCodePDF(obj, arquivoOrigem, config, configGEDVO);
//				return executarAssinaturaParaDocumento(obj, configGEDVO, arquivoOrigem, corAssinatura, config, true, usuarioVO,true, obj.getTipoOrigemDocumentoAssinadoEnum());				
//			}
//		}else {
//			return executarAssinaturaParaDocumento(obj, config.getCertificadoParaDocumento(), config.getSenhaCertificadoParaDocumento(), arquivoOrigem, null, "#000000", 80f, 200f, 8f, 380, 10, 0, 0, config, true, usuarioVO,true);
//		}	
	}

	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public String realizarInclusaoDocumentoAssinadoPorEmissaoCertificado(String nomeArquivoOrigem, MatriculaVO matricula,UnidadeEnsinoVO unidadeEnsinoVO , TurmaVO turma, TipoOrigemDocumentoAssinadoEnum tipoOrigemDocumentoAssinado,ProvedorDeAssinaturaEnum provedorDeAssinaturaEnum, String corAssinatura, Float alturaAssinatura, Float larguraAssinatura, FuncionarioVO funcionarioPrincipalVO, String cargoFuncionario1, String tituloFuncionario1, FuncionarioVO funcionarioSecundarioVO, String cargoFuncionario2, String tituloFuncionario2, ConfiguracaoGeralSistemaVO config, UsuarioVO usuarioVO , Boolean apresentarNomeCustomizado , String nomeCustomizado) throws Exception {
		DocumentoAssinadoVO obj = new DocumentoAssinadoVO();
		obj.setDataRegistro(new Date());
		obj.setUsuario(usuarioVO);
		obj.setTipoOrigemDocumentoAssinadoEnum(tipoOrigemDocumentoAssinado);
		obj.setMatricula(matricula);
		obj.setTurma(turma);
		obj.setUnidadeEnsinoVO(unidadeEnsinoVO);
		if(apresentarNomeCustomizado) {
			 
			obj.getArquivo().setNome(nomeCustomizado + ".pdf");
			obj.getArquivo().setDescricao(nomeCustomizado);
		}else {
			if (Uteis.isAtributoPreenchido(matricula.getMatricula())) {
				obj.getArquivo().setNome(Uteis.removeCaractersEspeciais(matricula.getMatricula()) + "_" + usuarioVO.getCodigo() + "_" + new Date().getTime() + ".pdf");
				obj.getArquivo().setDescricao(tipoOrigemDocumentoAssinado.getDescricao() + "_" + Uteis.removeCaractersEspeciais(matricula.getMatricula()) + "_" + new Date().getTime());
			} else {
				obj.getArquivo().setNome(Uteis.removeCaractersEspeciais(turma.getIdentificadorTurma()) + "_" + usuarioVO.getCodigo() + "_" + new Date().getTime() + ".pdf");
				obj.getArquivo().setDescricao(tipoOrigemDocumentoAssinado.getDescricao() + "_" + Uteis.removeCaractersEspeciais(turma.getIdentificadorTurma()) + "_" + new Date().getTime());
			}
		}
		obj.getArquivo().setExtensao(".pdf");
		obj.getArquivo().setResponsavelUpload(usuarioVO);
		obj.getArquivo().setDataUpload(new Date());
		obj.getArquivo().setDataDisponibilizacao(new Date());
		obj.getArquivo().setManterDisponibilizacao(true);
		
		obj.getArquivo().setControlarDownload(true);
		obj.getArquivo().setDataIndisponibilizacao(null);
		obj.getArquivo().setSituacao(SituacaoArquivo.ATIVO.getValor());
		obj.getArquivo().setPastaBaseArquivo(PastaBaseArquivoEnum.DOCUMENTOS_ASSINADOS_TMP.name());
		obj.getArquivo().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.DOCUMENTOS_ASSINADOS_TMP);
		obj.getArquivo().setPessoaVO(matricula.getAluno());
		obj.getArquivo().setTurma(turma);
		obj.getArquivo().setArquivoAssinadoDigitalmente(true);
		obj.setProvedorAssinaturaVisaoAdm(provedorDeAssinaturaEnum);
		if (Uteis.isAtributoPreenchido(funcionarioPrincipalVO.getCodigo())) {
			obj.getListaDocumentoAssinadoPessoa().add(new DocumentoAssinadoPessoaVO(new Date(), funcionarioPrincipalVO.getPessoa(), TipoPessoa.FUNCIONARIO, SituacaoDocumentoAssinadoPessoaEnum.PENDENTE, obj, 1, cargoFuncionario1, tituloFuncionario1, null, funcionarioPrincipalVO.getPessoa().getTipoAssinaturaDocumentoEnum()));
		}
		if (Uteis.isAtributoPreenchido(funcionarioSecundarioVO.getCodigo())) {
			obj.getListaDocumentoAssinadoPessoa().add(new DocumentoAssinadoPessoaVO(new Date(), funcionarioSecundarioVO.getPessoa(), TipoPessoa.FUNCIONARIO, SituacaoDocumentoAssinadoPessoaEnum.PENDENTE, obj, 2, cargoFuncionario2, tituloFuncionario2, null, funcionarioSecundarioVO.getPessoa().getTipoAssinaturaDocumentoEnum()));
		}
		incluir(obj, false, usuarioVO, config);
		return realizarVerificacaoProvedorDeAssinatura(obj, unidadeEnsinoVO.getCodigo(), true, true, nomeArquivoOrigem, AlinhamentoAssinaturaDigitalEnum.RODAPE_DIREITA, corAssinatura, alturaAssinatura, larguraAssinatura, 6f, 12, 20, 5, 2, config, true, usuarioVO, true);
//		ConfiguracaoGEDVO configGEDVO = null;
//		if (Uteis.isAtributoPreenchido(matricula.getMatricula())) {
//			configGEDVO = getFacadeFactory().getConfiguracaoGEDFacade().consultarPorUnidadeEnsino(matricula.getUnidadeEnsino().getCodigo(), false, usuarioVO);
//		} else {
//			configGEDVO = getFacadeFactory().getConfiguracaoGEDFacade().consultarPorUnidadeEnsino(matricula.getUnidadeEnsino().getCodigo(), false, usuarioVO);
//		}
//		String arquivoOrigem =  UteisJSF.getCaminhoWeb() + "relatorio/" + nomeArquivoOrigem;
//		adicionarSeloPDF(obj, arquivoOrigem, config, configGEDVO);
//		adicionarQRCodePDF(obj, arquivoOrigem, config, configGEDVO);
//		if(Uteis.isAtributoPreenchido(configGEDVO)) {
//			return executarAssinaturaParaDocumento(obj, configGEDVO, arquivoOrigem, corAssinatura, config, true, usuarioVO,true, obj.getTipoOrigemDocumentoAssinadoEnum());
//		}else {
//			return executarAssinaturaParaDocumento(obj, Uteis.isAtributoPreenchido(configGEDVO) ?  configGEDVO.getCertificadoDigitalUnidadeEnsinoVO() : config.getCertificadoParaDocumento(),  Uteis.isAtributoPreenchido(configGEDVO) ?  configGEDVO.getSenhaCertificadoDigitalUnidadeEnsino() :config.getSenhaCertificadoParaDocumento(), arquivoOrigem, AlinhamentoAssinaturaDigitalEnum.RODAPE_DIREITA, corAssinatura, alturaAssinatura, larguraAssinatura, 6f, 12, 20, 5, 2, config, true, usuarioVO,true);
//		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarExclusaoTodosDocumentoAssinadoSelecionados(DocumentoAssinadoVO obj, OperacaoDeVinculoEstagioEnum operacaoDeVinculoEstagioEnum, String motivo, UsuarioVO usuario, ConfiguracaoEstagioObrigatorioVO configEstagio) throws Exception {
		try {
			if(obj.getTipoOrigemDocumentoAssinadoEnum().isTermoEstagioObrigatorio()) {
				EstagioVO estagio = getFacadeFactory().getEstagioFacade().consultarPorDocumentoAssinador(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuario);
				if(!Uteis.isAtributoPreenchido(estagio)) {
					estagio.setDocumentoAssinadoVO(obj);
				}
				getFacadeFactory().getEstagioFacade().executarOperacaoEstagioEmLoteSelecionados(estagio, operacaoDeVinculoEstagioEnum, motivo, usuario, configEstagio);
			}else {
				if (obj.getTipoOrigemDocumentoAssinadoEnum().isXmlMec() && Uteis.isAtributoPreenchido(obj.getListaDocumentoAssinadoPessoa()) && obj.getListaDocumentoAssinadoPessoa().stream().anyMatch(d -> d.getSituacaoDocumentoAssinadoPessoaEnum().isAssinado())) {
					rejeitarDocumentoAssinadoProvedorAssinatura(obj, motivo, usuario);
				} else {
					excluir(obj, false, usuario, getAplicacaoControle().getConfiguracaoGeralSistemaVO(obj.getUnidadeEnsinoVO().getCodigo(), usuario));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	public DocumentoAssinadoVO consultarDocumentoAssinadoPorArquivo(Integer codArquivo, UsuarioVO usuarioLogado) {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select documentoassinado.*  from  documentoassinado ");
		sqlStr.append(" where documentoassinado.arquivo = ? ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] {codArquivo});
		DocumentoAssinadoVO obj = new DocumentoAssinadoVO();
		if(tabelaResultado.next()) {
			obj.setNovoObj(false);
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setDataRegistro(tabelaResultado.getDate("dataregistro"));
			obj.setChaveProvedorDeAssinatura(tabelaResultado.getString("chaveProvedorDeAssinatura"));
			obj.setProvedorDeAssinaturaEnum(ProvedorDeAssinaturaEnum.valueOf(tabelaResultado.getString("provedorDeAssinaturaEnum")));
			obj.getUnidadeEnsinoVO().setCodigo(tabelaResultado.getInt("unidadeensino"));
			obj.getArquivo().setCodigo(tabelaResultado.getInt("arquivo"));
		}
		return obj;
	}
		
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirDocumentoAssinadoVinculadoDocumentacaoGED(Integer codigo, UsuarioVO usuario) throws Exception {
		excluir(getIdEntidade(), false, usuario);
		String sql = "DELETE FROM documentoassinado WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { codigo });
	}
	
	@Override
	public void adicionarQRCodePDF(DocumentoAssinadoVO documentoAssinadoVO, String arquivoOrigem, ConfiguracaoGeralSistemaVO config, ConfiguracaoGEDVO configuracaoGEDVO) throws Exception {
		adicionarQRCodePDF(documentoAssinadoVO.getTipoOrigemDocumentoAssinadoEnum(), arquivoOrigem, documentoAssinadoVO.getUrlQrCode(), config, configuracaoGEDVO);		
	}
	
	@Override
	public void adicionarQRCodePDF(TipoOrigemDocumentoAssinadoEnum tipoOrigemDocumentoAssinadoEnum, String arquivoOrigem, String urlQrCode, ConfiguracaoGeralSistemaVO config, ConfiguracaoGEDVO configuracaoGEDVO) throws Exception {
		ConfiguracaoGedOrigemVO configuracaoGedOrigemVO = configuracaoGEDVO.getConfiguracaoGedOrigemVO(tipoOrigemDocumentoAssinadoEnum);
		if(Uteis.isAtributoPreenchido(configuracaoGEDVO) && Uteis.isAtributoPreenchido(configuracaoGedOrigemVO) 
				&& configuracaoGedOrigemVO.getApresentarQrCode()) {
			
			
			getFacadeFactory().getArquivoHelper().adicionarQRCodePDF(arquivoOrigem, config.getUrlAcessoExternoAplicacao()+urlQrCode, Element.ALIGN_CENTER, configuracaoGedOrigemVO.getAlturaQrCode(), configuracaoGedOrigemVO.getLarguraQrCode(), configuracaoGedOrigemVO.getPosicaoXQrCode(), configuracaoGedOrigemVO.getPosicaoYQrCode(), configuracaoGedOrigemVO.isUltimaPaginaQrCode());	
		}
	}
	
	@Override	
	public void adicionarSeloPDF(DocumentoAssinadoVO documentoAssinadoVO, String arquivoOrigem, ConfiguracaoGeralSistemaVO config, ConfiguracaoGEDVO configuracaoGEDVO) throws Exception {
		adicionarSeloPDF(documentoAssinadoVO.getTipoOrigemDocumentoAssinadoEnum(), arquivoOrigem, config, configuracaoGEDVO);
	}
	
	@Override
	public void adicionarSeloPDF(TipoOrigemDocumentoAssinadoEnum tipoOrigemDocumentoAssinadoEnum, String arquivoOrigem, ConfiguracaoGeralSistemaVO config, ConfiguracaoGEDVO configuracaoGEDVO) throws Exception {
		ConfiguracaoGedOrigemVO configuracaoGedOrigemVO = configuracaoGEDVO.getConfiguracaoGedOrigemVO(tipoOrigemDocumentoAssinadoEnum);
		if(Uteis.isAtributoPreenchido(configuracaoGEDVO) && Uteis.isAtributoPreenchido(configuracaoGEDVO.getSeloAssinaturaEletronicaVO().getNome()) 
				&& Uteis.isAtributoPreenchido(configuracaoGedOrigemVO) 
				&& configuracaoGedOrigemVO.getApresentarSelo()
				&& new File(arquivoOrigem).exists()) {
		
					String caminhoSelo = config.getLocalUploadArquivoFixo() + File.separator + configuracaoGEDVO.getSeloAssinaturaEletronicaVO().getPastaBaseArquivo() + File.separator + configuracaoGEDVO.getSeloAssinaturaEletronicaVO().getNome();
					String caminhoBase = config.getLocalUploadArquivoFixo() + File.separator + configuracaoGEDVO.getSeloAssinaturaEletronicaVO().getPastaBaseArquivo();
					getFacadeFactory().getArquivoHelper().adicionarImagemPDF(caminhoSelo, arquivoOrigem, caminhoBase,Element.ALIGN_RIGHT, configuracaoGedOrigemVO.getAlturaSelo(), configuracaoGedOrigemVO.getLarguraSelo(), configuracaoGedOrigemVO.getPosicaoXSelo(), configuracaoGedOrigemVO.getPosicaoYSelo(), configuracaoGedOrigemVO.isUltimaPaginaSelo());
					
				
		}
	}
	
	@Override
	public void realizarAssinaturaFuncionarioPreVisualizacao(DocumentoAssinadoVO documentoAssinadoVO, ConfiguracaoGEDVO configuracaoGEDVO, ConfiguracaoGedOrigemVO configuracaoGedOrigemVO, FuncionarioVO funcionarioVO, CargoVO cargoVO, String titulo, Integer nrFuncionario) throws Exception{
		if(!Uteis.isAtributoPreenchido(documentoAssinadoVO.getUnidadeEnsinoVO())) {
			throw new Exception("O campo UNIDADE ENSINO deve ser informado.");
		}
		UsuarioVO usuarioVO =  getFacadeFactory().getUsuarioFacade().consultarPorPessoa(funcionarioVO.getPessoa().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, null);
		ArquivoVO certificadoParaDocumento = validarCertificadoParaDocumento(documentoAssinadoVO.getUnidadeEnsinoVO(), configuracaoGEDVO, getAplicacaoControle().getConfiguracaoGeralSistemaVO(0, usuarioVO), usuarioVO, funcionarioVO.getPessoa());
		if(Uteis.isAtributoPreenchido(usuarioVO)) {
			File fileAssinar = new File(getAplicacaoControle().getConfiguracaoGeralSistemaVO(0, usuarioVO).getLocalUploadArquivoFixo()+File.separator+ documentoAssinadoVO.getArquivo().getPastaBaseArquivo()+File.separator+documentoAssinadoVO.getArquivo().getNome());
			if(fileAssinar.exists()) {
				DocumentoAssinadoPessoaVO documentoAssinadoPessoaVO =  new DocumentoAssinadoPessoaVO();
				documentoAssinadoPessoaVO.setDataSolicitacao(new Date());
				documentoAssinadoPessoaVO.setPessoaVO(usuarioVO.getPessoa());
				if(Uteis.isAtributoPreenchido(cargoVO)) {
					documentoAssinadoPessoaVO.setCargo(getFacadeFactory().getCargoFacade().consultarPorChavePrimaria(cargoVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO).getNome());
				}
				documentoAssinadoPessoaVO.setTitulo(titulo);
				documentoAssinadoPessoaVO.setOrdemAssinatura(nrFuncionario);
				documentoAssinadoPessoaVO.setSituacaoDocumentoAssinadoPessoaEnum(SituacaoDocumentoAssinadoPessoaEnum.PENDENTE);
				documentoAssinadoPessoaVO.setDataAssinatura(new Date());
				documentoAssinadoPessoaVO.setCodigo(123456789);
				documentoAssinadoPessoaVO.setTipoPessoa(TipoPessoa.FUNCIONARIO);				
				preencherAssinadorDigitalDocumentoPorPessoa(fileAssinar.getAbsolutePath(), documentoAssinadoVO, documentoAssinadoPessoaVO, certificadoParaDocumento, funcionarioVO.getPessoa().getSenhaCertificadoParaDocumento(), getAplicacaoControle().getConfiguracaoGeralSistemaVO(0, usuarioVO), configuracaoGEDVO , false);
			}else {
				throw new Exception("Não foi encontrado o ARQUIVO para registrar a ASSINATURA.");
			}

		}else {
			throw new Exception("Não foi encontrado um USUÁRIO para o FUNCIONÁRIO "+funcionarioVO.getPessoa().getNome()+" por isto não é possivel registrar a assinatura do documento.");
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public String excutarVerificacaoPessoasParaAssinarContrato(DocumentoAssinadoVO documentoAssinado, File arquivoAssinar, ConfiguracaoGeralSistemaVO config, UsuarioVO usuarioVO) throws Exception {
		File fileAssinar = new File(arquivoAssinar.getAbsolutePath());
		boolean arquivoCriado = false;
		for (DocumentoAssinadoPessoaVO dap : documentoAssinado.getListaDocumentoAssinadoPessoa()) {
			try {
				if (dap.getAssinarDocumento() && dap.getSituacaoDocumentoAssinadoPessoaEnum().equals(SituacaoDocumentoAssinadoPessoaEnum.PENDENTE)) {
					ArquivoVO certificadoParaDocumento = validarCertificadoParaDocumento(documentoAssinado.getUnidadeEnsinoVO(), null, config, usuarioVO, dap.getPessoaVO());				
					dap.setSituacaoDocumentoAssinadoPessoaEnum(SituacaoDocumentoAssinadoPessoaEnum.ASSINADO);
					dap.setDataAssinatura(new Date());
					getFacadeFactory().getDocumentoAssinadoPessoaFacade().atualizarDadosAssinatura(dap, null, null, null);
					getFacadeFactory().getMatriculaPeriodoFacade().realizarAtivacaoMatriculaValidandoRegrasEntregaDocumentoAssinaturaContratoMatricula(documentoAssinado.getMatricula().getMatricula() ,    Boolean.FALSE,  usuarioVO);
					preencherAssinadorDigitalDocumentoPorPessoa(fileAssinar.getAbsolutePath(), documentoAssinado, dap, certificadoParaDocumento, dap.getPessoaVO().getSenhaCertificadoParaDocumento(), config, null , true);
					String arquivoTemp = getFacadeFactory().getArquivoHelper().disponibilizarArquivoAssinadoParaDowload(config.getLocalUploadArquivoFixo() + File.separator + documentoAssinado.getArquivo().getPastaBaseArquivo() + File.separator + documentoAssinado.getArquivo().getNome(), documentoAssinado.getArquivo().getNome());
					fileAssinar = new File(arquivoTemp);
					arquivoCriado = true;
				}else {
					dap.setSituacaoDocumentoAssinadoPessoaEnum(SituacaoDocumentoAssinadoPessoaEnum.PENDENTE);
					dap.setDataAssinatura(null);
				}
			}catch (Exception e) {
				dap.setSituacaoDocumentoAssinadoPessoaEnum(SituacaoDocumentoAssinadoPessoaEnum.PENDENTE);
				dap.setDataAssinatura(null);
				throw e;
			}
		}
		
		if (!arquivoCriado) {
			File file = new File(config.getLocalUploadArquivoFixo() + File.separator + documentoAssinado.getArquivo().getPastaBaseArquivo() + File.separator + documentoAssinado.getArquivo().getNome());
			if (!file.exists()) {
				file.createNewFile();
			}
			FileUtils.copyFile(new File(arquivoAssinar.getAbsolutePath()), file);
			String arquivoTemp = getFacadeFactory().getArquivoHelper().disponibilizarArquivoAssinadoParaDowload(file.getAbsolutePath(), documentoAssinado.getArquivo().getNome());
			fileAssinar = new File(arquivoTemp);
		}
		
		
		return fileAssinar.getAbsolutePath().replaceAll("[\\\\]", "/");
	}
	
	
	@Override
	public List<DocumentoAssinadoVO> consultarPorMatriculaPeriodo(Integer matriculaPeriodo, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
		StringBuilder sqlStr = getSqlConsultaDadosArquivoAssinado();
		sqlStr.append(" WHERE documentoassinado.matriculaperiodo = ? and documentoassinado.tipoorigemdocumentoassinado = 'CONTRATO' order by documentoassinado.codigo desc ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { matriculaPeriodo });
		return (montarDadosConsultaArquivoAssinado(tabelaResultado, nivelMontarDados, usuario));
	}
	
	@Override
	public DocumentoAssinadoVO consultarPorMatriculaPeriodoContratoAssinado(Integer matriculaPeriodo, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
		StringBuilder sqlStr = getSqlConsultaDadosArquivoAssinado();
		sqlStr.append(" WHERE documentoassinado.matriculaperiodo = ? and documentoassinado.tipoorigemdocumentoassinado = 'CONTRATO' order by documentoassinado.codigo desc limit 1");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { matriculaPeriodo });
		if (tabelaResultado.next()) {
			return montarDadosArquivoAssinado(tabelaResultado, nivelMontarDados, usuario);
		}
		return new DocumentoAssinadoVO();
	}
	
	@Override
	public List<DocumentoAssinadoVO> consultarPorMatriculaAlunoContratoPendenteAssinatura(String matricula, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
		StringBuilder sqlStr = getSqlConsultaDadosArquivoAssinadoJoinDocumentoAssinadoPessoa(); 
		sqlStr.append(" WHERE documentoassinado.matricula = ? and documentoassinadopessoa.situacaodocumentoassinadopessoa = 'PENDENTE' and documentoassinadopessoa.tipopessoa = 'ALUNO' and documentoassinado.tipoorigemdocumentoassinado in ('CONTRATO', 'ATA_COLACAO_GRAU') order by documentoassinado.codigo desc ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { matricula });
		List<DocumentoAssinadoVO> lista = new ArrayList<DocumentoAssinadoVO>(0);
		if (tabelaResultado.next()) {
			return montarDadosConsultaArquivoAssinado(tabelaResultado, nivelMontarDados, usuario);
		}
		return lista;
	}
	
	public List<Integer> consultarDocumentosPendenteRejeitadoMatricula(String matricula, String tipoPessoa) throws Exception {
		List<Integer> listaMatriculaPeriodo = new ArrayList<Integer>(0);
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append(" select distinct(matriculaperiodo) from documentoassinado");
		sqlStr.append(" inner join documentoassinadopessoa on documentoassinado.codigo = documentoassinadopessoa.documentoassinado");
		sqlStr.append(" where matricula = ? and documentoassinadopessoa.situacaodocumentoassinadopessoa != 'ASSINADO' and documentoassinado.matriculaperiodo is not NULL");
		if (Uteis.isAtributoPreenchido(tipoPessoa)) {
			sqlStr.append(" and documentoassinadopessoa.tipopessoa = '").append(tipoPessoa).append("'");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { matricula });
		while (tabelaResultado.next()) {
			listaMatriculaPeriodo.add(tabelaResultado.getInt("matriculaperiodo"));
		}
		return listaMatriculaPeriodo;
	}

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
    public String realizarInclusaoDocumentoAssinadoPorAtaColacaoGrau(String nomeArquivoOrigem, ProgramacaoFormaturaVO programacaoFormatura, CursoVO curso, List<ProgramacaoFormaturaAlunoVO> listaProgramacaoFormaturaAlunoVO, TipoOrigemDocumentoAssinadoEnum tipoOrigemDocumentoAssinado, ProvedorDeAssinaturaEnum provedorDeAssinaturaEnum, String corAssinatura, Float alturaAssinatura, Float larguraAssinatura, ConfiguracaoGeralSistemaVO config, UsuarioVO usuarioVO, UnidadeEnsinoVO unidadeEnsinoVO, PessoaVO funcionarioPrincipal, String cargoFuncionario1, String tituloFuncionario1, PessoaVO funcionarioSecundario, String cargoFuncionario2, String tituloFuncionario2) throws Exception {
        ConfiguracaoGEDVO configGEDVO = getAplicacaoControle().getConfiguracaoGEDPorUnidadeEnsino(unidadeEnsinoVO.getCodigo(), usuarioVO);
        ConfiguracaoGedOrigemVO configuracaoGedOrigemVO = configGEDVO.getConfiguracaoGedOrigemVO(TipoOrigemDocumentoAssinadoEnum.ATA_COLACAO_GRAU);
        if (configuracaoGedOrigemVO.getProvedorAssinaturaPadraoEnum().isProvedorTechCert()) {
            return realizarInclusaoDocumentoAssinadoPorAtaColacaoGrauTechCert(nomeArquivoOrigem, programacaoFormatura, curso, listaProgramacaoFormaturaAlunoVO, tipoOrigemDocumentoAssinado, provedorDeAssinaturaEnum, corAssinatura, alturaAssinatura, larguraAssinatura, config, usuarioVO, unidadeEnsinoVO, funcionarioPrincipal, cargoFuncionario1, tituloFuncionario1, funcionarioSecundario, cargoFuncionario2, tituloFuncionario2);
        } else {
            DocumentoAssinadoVO obj = new DocumentoAssinadoVO();
            String origem = TipoOrigemDocumentoAssinadoEnum.ATA_COLACAO_GRAU.name() + Constantes.UNDERSCORE;
            String nomeCurso = Uteis.removeCaractersEspeciais(curso.getNome().trim()).replace("/", Constantes.UNDERSCORE).replace(" ", Constantes.UNDERSCORE).toUpperCase();
            String horario = String.valueOf(new Date().getTime());
            String codigoUsuario = Uteis.isAtributoPreenchido(usuarioVO) ? usuarioVO.getCodigo().toString() : "0";
            int comprimentoDisponivelCurso = 240 - (origem.length() + codigoUsuario.length() + horario.length());
            if (nomeCurso.length() > comprimentoDisponivelCurso && comprimentoDisponivelCurso > 0) {
                nomeCurso = nomeCurso.substring(0, comprimentoDisponivelCurso);
            }
            obj.setDataRegistro(new Date());
            obj.setUsuario(usuarioVO);
            obj.setTipoOrigemDocumentoAssinadoEnum(TipoOrigemDocumentoAssinadoEnum.ATA_COLACAO_GRAU);
            obj.setUnidadeEnsinoVO(unidadeEnsinoVO);
            obj.setProgramacaoFormaturaVO(programacaoFormatura);
            obj.setCursoVO(curso);
            obj.getArquivo().setNome(origem + nomeCurso + Constantes.UNDERSCORE + codigoUsuario + Constantes.UNDERSCORE + horario + ".pdf");
            obj.getArquivo().setDescricao(origem + nomeCurso + Constantes.UNDERSCORE + codigoUsuario + Constantes.UNDERSCORE + horario);
            obj.getArquivo().setExtensao(".pdf");
            obj.getArquivo().setResponsavelUpload(usuarioVO);
            obj.getArquivo().setDataUpload(new Date());
            obj.getArquivo().setDataDisponibilizacao(new Date());
            obj.getArquivo().setManterDisponibilizacao(true);
            obj.getArquivo().setControlarDownload(true);
            obj.getArquivo().setDataIndisponibilizacao(null);
            obj.getArquivo().setSituacao(SituacaoArquivo.ATIVO.getValor());
            obj.getArquivo().setPastaBaseArquivo(PastaBaseArquivoEnum.DOCUMENTOS_ASSINADOS_TMP.name());
            obj.getArquivo().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.DOCUMENTOS_ASSINADOS_TMP);
            obj.getArquivo().setArquivoAssinadoDigitalmente(true);
            obj.setProvedorAssinaturaVisaoAdm(provedorDeAssinaturaEnum);
            obj.setTituloColacaoGrau(programacaoFormatura.getColacaoGrauVO().getTitulo());
            obj.setInfoAtaColacaoGrau(programacaoFormatura.getColacaoGrauVO().getAta());
            obj.setDataColacaoGrau(programacaoFormatura.getColacaoGrauVO().getData());
            obj.setExpirationDate(Uteis.toOffsetDateTime(programacaoFormatura.getDataLimiteAssinaturaAta()));
            if (Uteis.isAtributoPreenchido(funcionarioPrincipal.getCodigo())) {
                obj.getListaDocumentoAssinadoPessoa().add(new DocumentoAssinadoPessoaVO(new Date(), funcionarioPrincipal, TipoPessoa.FUNCIONARIO, SituacaoDocumentoAssinadoPessoaEnum.PENDENTE, obj, 1, cargoFuncionario1, tituloFuncionario1, null, funcionarioPrincipal.getTipoAssinaturaDocumentoEnum()));
            }
            if (Uteis.isAtributoPreenchido(funcionarioSecundario.getCodigo())) {
                obj.getListaDocumentoAssinadoPessoa().add(new DocumentoAssinadoPessoaVO(new Date(), funcionarioSecundario, TipoPessoa.FUNCIONARIO, SituacaoDocumentoAssinadoPessoaEnum.PENDENTE, obj, 1, cargoFuncionario2, tituloFuncionario2, null, funcionarioSecundario.getTipoAssinaturaDocumentoEnum()));
            }

            if (!listaProgramacaoFormaturaAlunoVO.isEmpty()) {
                for (ProgramacaoFormaturaAlunoVO alunos : listaProgramacaoFormaturaAlunoVO) {
                    if (Uteis.isAtributoPreenchido(alunos.getMatricula().getAluno().getCodigo())) {
                        obj.getListaDocumentoAssinadoPessoa().add(new DocumentoAssinadoPessoaVO(new Date(), alunos.getMatricula().getAluno(), TipoPessoa.ALUNO, SituacaoDocumentoAssinadoPessoaEnum.PENDENTE, obj, 1, "", "", null, alunos.getMatricula().getAluno().getTipoAssinaturaDocumentoEnum()));
                    }
                }
            }
            incluir(obj, false, usuarioVO, config);
            return realizarVerificacaoProvedorDeAssinatura(obj, obj.getUnidadeEnsinoVO().getCodigo(), false, false, nomeArquivoOrigem, null, "#000000", 80f, 200f, 8f, 10, 380, 0, 0, config, true, usuarioVO, true);
        }
    }

    public String realizarInclusaoDocumentoAssinadoPorAtaColacaoGrauTechCert(String nomeArquivoOrigem, ProgramacaoFormaturaVO programacaoFormatura, CursoVO curso, List<ProgramacaoFormaturaAlunoVO> listaProgramacaoFormaturaAlunoVO, TipoOrigemDocumentoAssinadoEnum tipoOrigemDocumentoAssinado, ProvedorDeAssinaturaEnum provedorDeAssinaturaEnum, String corAssinatura, Float alturaAssinatura, Float larguraAssinatura, ConfiguracaoGeralSistemaVO config, UsuarioVO usuarioVO, UnidadeEnsinoVO unidadeEnsinoVO, PessoaVO funcionarioPrincipal, String cargoFuncionario1, String tituloFuncionario1, PessoaVO funcionarioSecundario, String cargoFuncionario2, String tituloFuncionario2) throws Exception {
        String caminho = null;
        double permitsPerSecond = 1_000.0 / Uteis.delayMillisTechCert;
        RateLimiter limiter = RateLimiter.create(permitsPerSecond);
        if (!listaProgramacaoFormaturaAlunoVO.isEmpty()) {
            for (ProgramacaoFormaturaAlunoVO alunos : listaProgramacaoFormaturaAlunoVO) {
                limiter.acquire();
                if (Uteis.isAtributoPreenchido(alunos.getMatricula().getAluno().getCodigo())) {
                    DocumentoAssinadoVO obj = new DocumentoAssinadoVO();
                    obj.getListaDocumentoAssinadoPessoa().add(new DocumentoAssinadoPessoaVO(new Date(), alunos.getMatricula().getAluno(), TipoPessoa.ALUNO, SituacaoDocumentoAssinadoPessoaEnum.PENDENTE, obj, 1, "", "", null, alunos.getMatricula().getAluno().getTipoAssinaturaDocumentoEnum()));
                    String origem = TipoOrigemDocumentoAssinadoEnum.ATA_COLACAO_GRAU.name() + Constantes.UNDERSCORE;
                    String nomeCurso = Uteis.removeCaractersEspeciais(curso.getNome().trim()).replace("/", Constantes.UNDERSCORE).replace(" ", Constantes.UNDERSCORE).toUpperCase();
                    String horario = String.valueOf(new Date().getTime());
                    String codigoUsuario = Uteis.isAtributoPreenchido(usuarioVO) ? usuarioVO.getCodigo().toString() : "0";
                    int comprimentoDisponivelCurso = 240 - (origem.length() + codigoUsuario.length() + horario.length());
                    if (nomeCurso.length() > comprimentoDisponivelCurso && comprimentoDisponivelCurso > 0) {
                        nomeCurso = nomeCurso.substring(0, comprimentoDisponivelCurso);
                    }
                    obj.setDataRegistro(new Date());
                    obj.setUsuario(usuarioVO);
                    obj.setTipoOrigemDocumentoAssinadoEnum(TipoOrigemDocumentoAssinadoEnum.ATA_COLACAO_GRAU);
                    obj.setUnidadeEnsinoVO(unidadeEnsinoVO);
                    obj.setProgramacaoFormaturaVO(programacaoFormatura);
                    obj.setCursoVO(curso);
                    obj.getArquivo().setNome(origem + nomeCurso + Constantes.UNDERSCORE + codigoUsuario + Constantes.UNDERSCORE + horario + ".pdf");
                    obj.getArquivo().setDescricao(origem + nomeCurso + Constantes.UNDERSCORE + codigoUsuario + Constantes.UNDERSCORE + horario);
                    obj.getArquivo().setExtensao(".pdf");
                    obj.getArquivo().setResponsavelUpload(usuarioVO);
                    obj.getArquivo().setDataUpload(new Date());
                    obj.getArquivo().setDataDisponibilizacao(new Date());
                    obj.getArquivo().setManterDisponibilizacao(true);
                    obj.getArquivo().setControlarDownload(true);
                    obj.getArquivo().setDataIndisponibilizacao(null);
                    obj.getArquivo().setSituacao(SituacaoArquivo.ATIVO.getValor());
                    obj.getArquivo().setPastaBaseArquivo(PastaBaseArquivoEnum.DOCUMENTOS_ASSINADOS_TMP.name());
                    obj.getArquivo().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.DOCUMENTOS_ASSINADOS_TMP);
                    obj.getArquivo().setArquivoAssinadoDigitalmente(true);
                    obj.setProvedorAssinaturaVisaoAdm(provedorDeAssinaturaEnum);
                    obj.setTituloColacaoGrau(programacaoFormatura.getColacaoGrauVO().getTitulo());
                    obj.setInfoAtaColacaoGrau(programacaoFormatura.getColacaoGrauVO().getAta());
                    obj.setDataColacaoGrau(programacaoFormatura.getColacaoGrauVO().getData());
                    obj.setAtaColacaoGrau(true);
                    obj.setExpirationDate(Uteis.toOffsetDateTime(programacaoFormatura.getDataLimiteAssinaturaAta()));
                    if (Uteis.isAtributoPreenchido(funcionarioPrincipal.getCodigo())) {
                        obj.getListaDocumentoAssinadoPessoa().add(new DocumentoAssinadoPessoaVO(new Date(), funcionarioPrincipal, TipoPessoa.FUNCIONARIO, SituacaoDocumentoAssinadoPessoaEnum.PENDENTE, obj, 1, cargoFuncionario1, tituloFuncionario1, null, funcionarioPrincipal.getTipoAssinaturaDocumentoEnum()));
                    }
                    if (Uteis.isAtributoPreenchido(funcionarioSecundario.getCodigo())) {
                        obj.getListaDocumentoAssinadoPessoa().add(new DocumentoAssinadoPessoaVO(new Date(), funcionarioSecundario, TipoPessoa.FUNCIONARIO, SituacaoDocumentoAssinadoPessoaEnum.PENDENTE, obj, 1, cargoFuncionario2, tituloFuncionario2, null, funcionarioSecundario.getTipoAssinaturaDocumentoEnum()));
                    }

                    incluir(obj, false, usuarioVO, config);
                    caminho = realizarVerificacaoProvedorDeAssinatura(obj, obj.getUnidadeEnsinoVO().getCodigo(), false, false, nomeArquivoOrigem, null, "#000000", 80f, 200f, 8f, 10, 380, 0, 0, config, true, usuarioVO, true);
                }
            }
        }
        return caminho;
    }

    @Override
    public List<DocumentoAssinadoVO> consultarPorCodigoAlunoPendenciaAssinaturaContratoColacaoGrau(Integer codigoPessoa, String matricula, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
        StringBuilder sqlStr = getSqlConsultaDadosArquivoAssinadoJoinDocumentoAssinadoPessoa();

        sqlStr.append(" WHERE COALESCE(documentoassinado.documentoAssinadoInvalido, FALSE) IS FALSE ");
        sqlStr.append(" AND documentoassinadopessoa.tipopessoa IN ('ALUNO') AND documentoassinadopessoa.pessoa = ? ");
        sqlStr.append(" AND documentoassinadopessoa.situacaodocumentoassinadopessoa = 'PENDENTE' ");
        sqlStr.append(" AND ((documentoassinado.matricula = ? and documentoassinado.tipoorigemdocumentoassinado = 'CONTRATO' AND ((COALESCE(cursoMatricula.permitirassinarcontratopendenciadocumentacao, FALSE) = FALSE AND NOT EXISTS ");
        sqlStr.append(" (SELECT documetacaomatricula.matricula FROM documetacaomatricula WHERE documetacaomatricula.matricula = ? AND entregue = FALSE AND gerarsuspensaomatricula = TRUE LIMIT 1 ");
        sqlStr.append(" )) or cursoMatricula.permitirassinarcontratopendenciadocumentacao = true )) OR documentoassinado.tipoorigemdocumentoassinado = 'ATA_COLACAO_GRAU' OR documentoassinado.tipoorigemdocumentoassinado = 'TERMO_ESTAGIO_NAO_OBRIGATORIO' ");
        sqlStr.append(" or (documentoassinado.tipoorigemdocumentoassinado = 'TERMO_ESTAGIO_OBRIGATORIO' and documentoassinadopessoa.urlassinatura != ''))");
        sqlStr.append(" order by documentoassinado.codigo desc ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[]{codigoPessoa, matricula, matricula});
        return montarDadosConsultaArquivoAssinado(tabelaResultado, nivelMontarDados, usuario);
    }

    @Override
    public List<DocumentoAssinadoVO> verificarGeracaoDocumentoAssinado(Integer codigoPessoa, MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, UnidadeEnsinoVO unidadeEnsinoVO,  ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		return null;
//        verificarGeracaoDocumentoContratoMatricula(matriculaVO, matriculaPeriodoVO, configuracaoGeralSistemaVO, nivelMontarDados, usuario);
//        return consultarPorCodigoAlunoPendenciaAssinaturaContratoColacaoGrau(codigoPessoa, matriculaVO.getMatricula(), nivelMontarDados, usuario);
    }

    public void verificarGeracaoDocumentoContratoMatricula(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, UnidadeEnsinoVO unidadeEnsinoVO,  ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        try {
            if (Uteis.isAtributoPreenchido(usuario) && (matriculaVO.getSituacao().equals(SituacaoVinculoMatricula.ATIVA.getValor())
                    || matriculaVO.getSituacao().equals(SituacaoVinculoMatricula.PREMATRICULA.getValor()))) {
                DocumentoAssinadoPessoaVO documentoAssinadoPessoa = getFacadeFactory().getDocumentoAssinadoPessoaFacade().consultarDocumentosAssinadoPessoaPorMatriculaMatriculaPeriodo(matriculaVO.getMatricula(), matriculaPeriodoVO.getCodigo());
                if (((!Uteis.isAtributoPreenchido(documentoAssinadoPessoa)) && getFacadeFactory().getMatriculaPeriodoFacade().verificarMatriculaDeCalouro(matriculaVO.getMatricula(), matriculaPeriodoVO.getCodigo(), false, usuario))
                        && getFacadeFactory().getTextoPadraoFacade().verificarTextoPadraoAssinaDigitalmentePorMatricula(matriculaVO.getMatricula(), false, 0, usuario)
                        && getAplicacaoControle().getConfiguracaoGEDPorUnidadeEnsino(matriculaVO.getUnidadeEnsino().getCodigo(), usuario).getConfiguracaoGedContratoVO().getAssinarDocumento()
                        && getAplicacaoControle().getConfiguracaoGEDPorUnidadeEnsino(matriculaVO.getUnidadeEnsino().getCodigo(), usuario).getConfiguracaoGedContratoVO().getProvedorAssinaturaPadraoEnum().isProvedorCertisign()
                        && ((!matriculaVO.getCurso().getPermitirAssinarContratoPendenciaDocumentacao() && !getFacadeFactory().getDocumetacaoMatriculaFacade().verificaAlunoDevendoDocumentoQueSuspendeMatricula(matriculaVO.getMatricula())) || (matriculaVO.getCurso().getPermitirAssinarContratoPendenciaDocumentacao()))) {
                    matriculaPeriodoVO.setContratoMatricula(getFacadeFactory().getTextoPadraoFacade().consultarTextoPadraoContratoMatriculaPorMatriculaPeriodo(matriculaPeriodoVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario));
                    matriculaPeriodoVO.setRegerarDocumentoAssinado(true);
                    getFacadeFactory().getImpressaoContratoFacade().imprimirContratoVisaoAluno(matriculaVO, matriculaPeriodoVO, configuracaoGeralSistemaVO, usuario);
                }
                if (((!Uteis.isAtributoPreenchido(documentoAssinadoPessoa)) && getFacadeFactory().getMatriculaPeriodoFacade().verificarMatriculaDeCalouro(matriculaVO.getMatricula(), matriculaPeriodoVO.getCodigo(), false, usuario))
                        && getFacadeFactory().getTextoPadraoFacade().verificarTextoPadraoAssinaDigitalmentePorMatricula(matriculaVO.getMatricula(), false, 0, usuario)
                        && getAplicacaoControle().getConfiguracaoGEDPorUnidadeEnsino(matriculaVO.getUnidadeEnsino().getCodigo(), usuario).getConfiguracaoGedContratoVO().getAssinarDocumento()
                        && getAplicacaoControle().getConfiguracaoGEDPorUnidadeEnsino(matriculaVO.getUnidadeEnsino().getCodigo(), usuario).getConfiguracaoGedContratoVO().getProvedorAssinaturaPadraoEnum().isProvedorTechCert()
                        && ((!matriculaVO.getCurso().getPermitirAssinarContratoPendenciaDocumentacao() && !getFacadeFactory().getDocumetacaoMatriculaFacade().verificaAlunoDevendoDocumentoQueSuspendeMatricula(matriculaVO.getMatricula())) || (matriculaVO.getCurso().getPermitirAssinarContratoPendenciaDocumentacao()))) {
                    matriculaPeriodoVO.setContratoMatricula(getFacadeFactory().getTextoPadraoFacade().consultarTextoPadraoContratoMatriculaPorMatriculaPeriodo(matriculaPeriodoVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario));
                    matriculaPeriodoVO.setRegerarDocumentoAssinado(true);
                    getFacadeFactory().getImpressaoContratoFacade().imprimirContratoVisaoAluno(matriculaVO, matriculaPeriodoVO, configuracaoGeralSistemaVO, usuario);
                }
            }
        } catch (Exception e) {
            AplicacaoControle.realizarEscritaTextoDebug(AssuntoDebugEnum.SCRIPT, "ERRO NO METODO VisaoAlunoControle.verificarGeracaoDocumentoContratoMatricula: " + e.getMessage());
            e.printStackTrace();
        }
    }

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void alterDocumentoAssinadoInvalido(DocumentoAssinadoVO documentoAssinadoVO, UsuarioVO usuarioLogado) {
		documentoAssinadoVO.setDocumentoAssinadoInvalido(true);
		final String sql = "UPDATE Documentoassinado set documentoAssinadoInvalido = ? WHERE ((codigo = ?))";
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				sqlAlterar.setBoolean(1, documentoAssinadoVO.isDocumentoAssinadoInvalido());
				sqlAlterar.setInt(2, documentoAssinadoVO.getCodigo().intValue());
				return sqlAlterar;

			}
		});
	}
	
	@Override
	public List<DocumentoAssinadoVO> consultarPorCodigoProgramacaoCodigoCurso(Integer codigoProgramacaoFormatura, Integer codigoCurso, Boolean filtrarAlunosPendenteColacaoGrau, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
		StringBuilder sqlStr = getSqlConsultaDadosCompletos();
		sqlStr.append(" WHERE programacaoformatura.codigo = ?");
		sqlStr.append(" AND curso.codigo = ?  ");
		sqlStr.append(" AND documentoassinado.documentoAssinadoInvalido = false  ");
		// filtrar apenas alunos que na programação de formatura aluno a informação
		// colougrau esteja igual a "NI", que no caso é "Não informado"
		if (filtrarAlunosPendenteColacaoGrau) {
			sqlStr.append("AND EXISTS ( ");
			sqlStr.append("SELECT p.codigo ");
			sqlStr.append("FROM programacaoformaturaaluno p ");
			sqlStr.append("INNER JOIN matricula m ON m.matricula = p.matricula ");
			sqlStr.append("INNER JOIN documentoassinadopessoa d ON d.documentoassinado = documentoassinado.codigo AND d.pessoa = m.aluno AND d.tipopessoa = 'ALUNO' ");
			sqlStr.append("WHERE p.programacaoformatura = programacaoformatura.codigo AND p.colougrau = 'NI' ");
			sqlStr.append(") ");
		}
		sqlStr.append(" ORDER BY documentoassinado.codigo  ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { codigoProgramacaoFormatura, codigoCurso });
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}
	
	@Override
	public List<DocumentoAssinadoVO> consultarPorCodigoProgramacaoFormatura(Integer codigoProgramacaoFormatura, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
		StringBuilder sqlStr = getSqlConsultaDadosCompletos();
		sqlStr.append(" WHERE programacaoformatura.codigo = ?");
		sqlStr.append(" AND documentoassinado.documentoAssinadoInvalido = false  ");
		sqlStr.append(" ORDER BY documentoassinado.codigo ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { codigoProgramacaoFormatura });
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	@Override
	public DocumentoAssinadoVO consultarPorMatriculaAlunoUltimoContratoGeradoSemAssinaturaRejeitada(String matricula, TipoOrigemDocumentoAssinadoEnum  tipoorigemdocumentoassinado ,  int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
		StringBuilder sqlStr = getSqlConsultaDadosArquivoAssinadoJoinDocumentoAssinadoPessoa();
		sqlStr.append(" WHERE documentoassinado.matricula = ? and documentoassinadopessoa.situacaodocumentoassinadopessoa <> 'REJEITADO'  and documentoassinado.tipoorigemdocumentoassinado = '"+tipoorigemdocumentoassinado+"' ");
	
		sqlStr.append(" order by documentoassinado.codigo desc limit 1");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { matricula });
		if (tabelaResultado.next()) {
			return montarDadosArquivoAssinado(tabelaResultado, nivelMontarDados, usuario);
		}
		return new DocumentoAssinadoVO();
	}
	
	
	public StringBuilder getSqlConsultaDadosArquivoAssinadoJoinDocumentoAssinadoPessoa () {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append("select  distinct documentoassinado.codigo as \"documentoassinado.codigo\", documentoassinado.dataregistro as \"documentoassinado.dataregistro\", ");
		sqlStr.append("documentoassinado.matricula as \"documentoassinado.matricula\", documentoassinado.gradecurricular as \"documentoassinado.gradecurricular\", ");
		sqlStr.append("documentoassinado.turma as \"documentoassinado.turma\", documentoassinado.disciplina as \"documentoassinado.disciplina\", ");
		sqlStr.append("documentoassinado.chaveProvedorDeAssinatura as \"documentoassinado.chaveProvedorDeAssinatura\", ");
		sqlStr.append("documentoassinado.urlProvedorDeAssinatura as \"documentoassinado.urlProvedorDeAssinatura\", ");
		sqlStr.append("documentoassinado.codigoProvedorDeAssinatura as \"documentoassinado.codigoProvedorDeAssinatura\", ");
		sqlStr.append("documentoassinado.ano as \"documentoassinado.ano\", documentoassinado.semestre as \"documentoassinado.semestre\", documentoassinado.matriculaPeriodo as \"documentoassinado.matriculaPeriodo\", ");
		sqlStr.append("documentoassinado.tipoorigemdocumentoassinado as \"documentoassinado.tipoorigemdocumentoassinado\", documentoassinado.provedordeassinaturaenum  as  \"documentoassinado.provedordeassinaturaenum\" , ");
		sqlStr.append("arquivo.codigo as \"arquivo.codigo\" , arquivo.codOrigem as \"arquivo.codOrigem\", arquivo.nome as \"arquivo.nome\", ");
		sqlStr.append("arquivo.apresentarPortalCoordenador as \"arquivo.apresentarPortalCoordenador\", arquivo.apresentarPortalProfessor as \"arquivo.apresentarPortalProfessor\", arquivo.apresentarPortalAluno  as \"arquivo.apresentarPortalAluno\", ");
		sqlStr.append("arquivo.descricao as \"arquivo.descricao\", arquivo.dataupload as \"arquivo.dataupload\", arquivo.dataDisponibilizacao as \"arquivo.dataDisponibilizacao\", arquivo.dataIndisponibilizacao as \"arquivo.dataIndisponibilizacao\", ");
		sqlStr.append("arquivo.manterDisponibilizacao as \"arquivo.manterDisponibilizacao\", arquivo.origem as \"arquivo.origem\",arquivo.situacao as \"arquivo.situacao\", arquivo.controlardownload as \"arquivo.controlardownload\", ");
		sqlStr.append("arquivo.responsavelupload as \"arquivo.responsavelupload\", arquivo.disciplina as \"arquivo.disciplina\", arquivo.turma as \"arquivo.turma\", arquivo.extensao as \"arquivo.extensao\", arquivo.apresentarDeterminadoPeriodo as \"arquivo.apresentarDeterminadoPeriodo\", ");
		sqlStr.append("arquivo.permitirArquivoResposta as \"arquivo.permitirArquivoResposta\", arquivo.pastabasearquivo as \"arquivo.pastabasearquivo\", arquivo.arquivoResposta as \"arquivo.arquivoResposta\", arquivo.pessoa as \"arquivo.pessoa\", ");
		sqlStr.append("arquivo.professor as \"arquivo.professor\", arquivo.nivelEducacional as \"arquivo.nivelEducacional\", arquivo.cpfAlunoDocumentacao as \"arquivo.cpfAlunoDocumentacao\", arquivo.cpfRequerimento as \"arquivo.cpfRequerimento\", ");
		sqlStr.append("arquivo.indice as \"arquivo.indice\", arquivo.agrupador as \"arquivo.agrupador\", arquivo.indiceagrupador as \"arquivo.indiceagrupador\", arquivo.arquivoAssinadoDigitalmente as \"arquivo.arquivoAssinadoDigitalmente\", ");
		sqlStr.append("arquivo.curso as \"arquivo.curso\", arquivo.descricaoArquivo as \"arquivo.descricaoArquivo\", ");
		sqlStr.append("unidadeEnsino.codigo as \"unidadeEnsino.codigo\",unidadeEnsino.nome as \"unidadeEnsino.nome\", ");
		sqlStr.append("unidadeEnsino.razaoSocial as \"unidadeEnsino.razaoSocial\", unidadeEnsino.cnpj as \"unidadeEnsino.cnpj\", ");
		sqlStr.append("disciplina.codigo as \"disciplina.codigo\",disciplina.nome as \"disciplina.nome\", ");
		sqlStr.append("turma.codigo as \"turma.codigo\",turma.identificadorTurma as \"turma.identificadorTurma\", ");
		sqlStr.append("usuario.codigo as \"usuario.codigo\",usuario.nome as \"usuario.nome\", ");
		sqlStr.append("professor.codigo as \"professor.codigo\",professor.nome as \"professor.nome\", ");
		sqlStr.append("pessoa.codigo as \"pessoa.codigo\",pessoa.nome as \"pessoa.nome\", ");
		sqlStr.append("pessoa.cpf as \"pessoa.cpf\",pessoa.dataNasc as \"pessoa.dataNasc\", ");
		sqlStr.append("curso.codigo as \"curso.codigo\",curso.nome as \"curso.nome\", ");
		sqlStr.append("programacaoformatura.codigo as \"programacaoformatura.codigo\", programacaoformatura.datalimiteassinaturaata as \"programacaoformatura.datalimiteassinaturaata\", documentoassinado.codigoValidacaoCurriculoEscolarDigital AS \"documentoassinado.codigoValidacaoCurriculoEscolarDigital\", gradeCurricular.nome AS \"gradeCurricular.nome\", cursoDocumentoAssinado.codigo AS \"cursoDocumentoAssinado.codigo\", cursoDocumentoAssinado.nome AS \"cursoDocumentoAssinado.nome\", ");
		sqlStr.append("arquivovisual.codigo as \"arquivovisual.codigo\" , arquivovisual.codOrigem as \"arquivovisual.codOrigem\", arquivovisual.nome as \"arquivovisual.nome\", ");
		sqlStr.append("arquivovisual.apresentarPortalCoordenador as \"arquivovisual.apresentarPortalCoordenador\", arquivovisual.apresentarPortalProfessor as \"arquivovisual.apresentarPortalProfessor\", arquivovisual.apresentarPortalAluno  as \"arquivovisual.apresentarPortalAluno\", ");
		sqlStr.append("arquivovisual.descricao as \"arquivovisual.descricao\", arquivovisual.dataupload as \"arquivovisual.dataupload\", arquivovisual.dataDisponibilizacao as \"arquivovisual.dataDisponibilizacao\", arquivovisual.dataIndisponibilizacao as \"arquivovisual.dataIndisponibilizacao\", ");
		sqlStr.append("arquivovisual.manterDisponibilizacao as \"arquivovisual.manterDisponibilizacao\", arquivovisual.origem as \"arquivovisual.origem\",arquivovisual.situacao as \"arquivovisual.situacao\", arquivovisual.controlardownload as \"arquivovisual.controlardownload\", ");
		sqlStr.append("arquivovisual.responsavelupload as \"arquivovisual.responsavelupload\", arquivovisual.disciplina as \"arquivovisual.disciplina\", arquivovisual.turma as \"arquivovisual.turma\", arquivovisual.extensao as \"arquivovisual.extensao\", arquivovisual.apresentarDeterminadoPeriodo as \"arquivovisual.apresentarDeterminadoPeriodo\", ");
		sqlStr.append("arquivovisual.permitirArquivoResposta as \"arquivovisual.permitirArquivoResposta\", arquivovisual.pastabasearquivo as \"arquivovisual.pastabasearquivo\", arquivovisual.arquivoResposta as \"arquivovisual.arquivoResposta\", arquivovisual.pessoa as \"arquivovisual.pessoa\", ");
		sqlStr.append("arquivovisual.professor as \"arquivovisual.professor\", arquivovisual.nivelEducacional as \"arquivovisual.nivelEducacional\", arquivovisual.cpfAlunoDocumentacao as \"arquivovisual.cpfAlunoDocumentacao\", arquivovisual.cpfRequerimento as \"arquivovisual.cpfRequerimento\", ");
		sqlStr.append("arquivovisual.indice as \"arquivovisual.indice\", arquivovisual.agrupador as \"arquivovisual.agrupador\", arquivovisual.indiceagrupador as \"arquivovisual.indiceagrupador\", arquivovisual.arquivoAssinadoDigitalmente as \"arquivovisual.arquivoAssinadoDigitalmente\", ");
		sqlStr.append("arquivovisual.curso as \"arquivovisual.curso\", arquivovisual.descricaoArquivo as \"arquivovisual.descricaoArquivo\" ");
		sqlStr.append("from documentoassinado ");
		sqlStr.append("inner join arquivo 					ON arquivo.codigo = documentoassinado.arquivo ");
		sqlStr.append("LEFT JOIN documentoassinadopessoa 	ON documentoassinado.codigo = documentoassinadopessoa.documentoassinado  ");
		
		sqlStr.append("left join unidadeEnsino on documentoassinado.unidadeEnsino = unidadeEnsino.codigo ");
		sqlStr.append("left join disciplina on arquivo.disciplina = disciplina.codigo ");
		sqlStr.append("left join turma on arquivo.turma = turma.codigo ");
		sqlStr.append("left join usuario on arquivo.responsavelupload = usuario.codigo ");
		sqlStr.append("left join pessoa professor on arquivo.professor = professor.codigo ");
		sqlStr.append("left join pessoa  on arquivo.pessoa = pessoa.codigo ");
		sqlStr.append("left join curso on arquivo.curso = curso.codigo ");
		sqlStr.append("LEFT JOIN matricula matriculaDocumentoAssinado ON documentoassinado.matricula = matriculaDocumentoAssinado.matricula ");
		sqlStr.append("LEFT JOIN curso cursoMatricula ON matriculaDocumentoAssinado.curso = cursoMatricula.codigo ");
		sqlStr.append("LEFT JOIN programacaoformatura ON programacaoformatura.codigo = documentoassinado.programacaoformatura ");
		sqlStr.append("left join gradeCurricular on gradeCurricular.codigo = documentoassinado.gradeCurricular ");
		sqlStr.append("left join curso cursoDocumentoAssinado on cursoDocumentoAssinado.codigo = documentoassinado.curso ");
		sqlStr.append("LEFT JOIN arquivo arquivovisual ON arquivovisual.codigo = documentoassinado.arquivovisual ");
		//sqlStr.append("left join matricula on arquivo.pessoa = matricula.aluno ");
		return sqlStr;
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarCodigoValidacaoHistorico(final String codigoValidacao, final Integer codigo, UsuarioVO usuario, Boolean controlarAcesso) throws Exception {
//		try {
//			ExpedicaoDiploma.alterar(getIdEntidade(), controlarAcesso, usuario);
//			final String sql = "UPDATE documentoassinado set codigoValidacaoHistoricoDigital=? WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
//
//			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
//
//				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
//					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
//                    if (codigoValidacao == null) {
//                        sqlAlterar.setNull(1, 0);
//                    } else {
//                        sqlAlterar.setString(1, codigoValidacao);
//                    }
//					sqlAlterar.setInt(2, codigo);
//					return sqlAlterar;
//				}
//			});
//		} catch (Exception e) {
//			throw e;
//		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, DocumentoAssinadoPessoaVO> realizarGeracaoPreviewDocumentoContratoAtaColacao(DocumentoAssinadoVO documentoAssinado, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, UsuarioVO usuarioVO) throws Exception {
		Map<String, DocumentoAssinadoPessoaVO> mapDocumento = new HashedMap();
		if (Uteis.isAtributoPreenchido(documentoAssinado)) {
			if (Uteis.isAtributoPreenchido(documentoAssinado.getListaDocumentoAssinadoPessoa())) {
				documentoAssinado.getListaDocumentoAssinadoPessoa().stream().forEach(documentoAssinadoPessoa -> {
					String caminhoPreview = "";
					try {
						if (((documentoAssinadoPessoa.getTipoPessoa().equals(TipoPessoa.ALUNO) || documentoAssinadoPessoa.getTipoPessoa().equals(TipoPessoa.FUNCIONARIO) || documentoAssinadoPessoa.getTipoPessoa().equals(TipoPessoa.RESPONSAVEL_FINANCEIRO) || (documentoAssinadoPessoa.getTipoPessoa().equals(TipoPessoa.RESPONSAVEL_LEGAL)))
								&& documentoAssinadoPessoa.getPessoaVO().getCodigo().equals(usuarioVO.getPessoa().getCodigo()) && documentoAssinadoPessoa.getSituacaoDocumentoAssinadoPessoaEnum().equals(SituacaoDocumentoAssinadoPessoaEnum.PENDENTE) && documentoAssinado.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.CONTRATO))
								|| (documentoAssinadoPessoa.getTipoPessoa().equals(TipoPessoa.ALUNO) && documentoAssinadoPessoa.getPessoaVO().getCodigo().equals(usuarioVO.getPessoa().getCodigo()) && documentoAssinado.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.ATA_COLACAO_GRAU))) {
							if (documentoAssinado.getProvedorDeAssinaturaEnum().isProvedorSei()) {
								if (documentoAssinado.getArquivo().getServidorArquivoOnline().equals(ServidorArquivoOnlineEnum.AMAZON_S3)) {
									ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesDiretorioUpload();
//									ServidorArquivoOnlineS3RS servidorArquivoOnlineS3RS = new ServidorArquivoOnlineS3RS(configuracaoGeralSistemaVO.getUsuarioAutenticacao(), configuracaoGeralSistemaVO.getSenhaAutenticacao(), configuracaoGeralSistemaVO.getNomeRepositorio());
									String nomeArquivoUsar = documentoAssinado.getArquivo().getDescricao().contains(".") ? documentoAssinado.getArquivo().getDescricao() : documentoAssinado.getArquivo().getDescricao() + (documentoAssinado.getArquivo().getNome().substring(documentoAssinado.getArquivo().getNome().lastIndexOf("."), documentoAssinado.getArquivo().getNome().length()));
//									if (servidorArquivoOnlineS3RS.consultarSeObjetoExiste(documentoAssinado.getArquivo().recuperarNomeArquivoServidorExterno(documentoAssinado.getArquivo().getPastaBaseArquivo(), configuracaoGeralSistemaVO.getLocalUploadArquivoTemp(), nomeArquivoUsar))) {
//										caminhoPreview = servidorArquivoOnlineS3RS.getUrlParaDownloadDoArquivo(documentoAssinado.getArquivo().recuperarNomeArquivoServidorExterno(documentoAssinado.getArquivo().getPastaBaseArquivo(), configuracaoGeralSistemaVO.getLocalUploadArquivoTemp(), nomeArquivoUsar));
//									} else {
//										nomeArquivoUsar = documentoAssinado.getArquivo().getDescricao();
//										if (servidorArquivoOnlineS3RS.consultarSeObjetoExiste(documentoAssinado.getArquivo().recuperarNomeArquivoServidorExterno(documentoAssinado.getArquivo().getPastaBaseArquivo(), configuracaoGeralSistemaVO.getLocalUploadArquivoTemp(), nomeArquivoUsar))) {
//											caminhoPreview = servidorArquivoOnlineS3RS.getUrlParaDownloadDoArquivo(documentoAssinado.getArquivo().recuperarNomeArquivoServidorExterno(documentoAssinado.getArquivo().getPastaBaseArquivo(), configuracaoGeralSistemaVO.getLocalUploadArquivoTemp(), nomeArquivoUsar));
//										} else {
//											throw new Exception("Não foi encontrado no repositório da AMAZON o aquivo no caminho " + documentoAssinado.getArquivo().recuperarNomeArquivoServidorExterno(documentoAssinado.getArquivo().getPastaBaseArquivo(), configuracaoGeralSistemaVO.getLocalUploadArquivoTemp(), nomeArquivoUsar) + ".");
//										}
//									}
								} else {
									caminhoPreview = (configuracaoGeralSistema.getUrlExternoDownloadArquivo() + "/" + documentoAssinado.getArquivo().getPastaBaseArquivo() + "/" + documentoAssinado.getArquivo().getNome() + "?embedded=true").replace(File.separator, "/");
								}
							}
							mapDocumento.put(caminhoPreview, documentoAssinadoPessoa);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
			}
		}
		return mapDocumento;
	}
	
	public DocumentoAssinadoPessoaVO realizarGeracaoAssinantePessoaPadrao(Integer ordemAssinatura, String nomePessoa, Boolean isCNPJ, DocumentoAssinadoVO documentoAssinadoVO) {
		DocumentoAssinadoPessoaVO documentoAssinadoPessoaVO = new DocumentoAssinadoPessoaVO();
		documentoAssinadoPessoaVO.setNome(nomePessoa);
		documentoAssinadoPessoaVO.setTipoPessoa(TipoPessoa.MEMBRO_COMUNIDADE);
		documentoAssinadoPessoaVO.setOrdemAssinatura(ordemAssinatura);
		documentoAssinadoPessoaVO.setAssinarPorCNPJ(isCNPJ);
		documentoAssinadoPessoaVO.setDocumentoAssinadoVO(documentoAssinadoVO);
		return documentoAssinadoPessoaVO;
	}
	
	
	@Override
	public void realizarVerificacaoDocumentoAssinadoPendenteUsuarioLogado(UsuarioVO usuarioVO, MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, Boolean verificarAtaColacao) throws Exception {
		try {
			
			usuarioVO.getDocumentoPendenteAssinatura().removeIf(t -> t.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.CONTRATO) || t.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.ATA_COLACAO_GRAU));
			List<DocumentoAssinadoVO> listaDocumento = getFacadeFactory().getDocumentoAssinadoFacade().verificarGeracaoDocumentoAssinado(usuarioVO.getPessoa().getCodigo(), matriculaVO, matriculaPeriodoVO, matriculaVO.getUnidadeEnsino(), getAplicacaoControle().getConfiguracaoGeralSistemaVO(null, usuarioVO), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
			if (Uteis.isAtributoPreenchido(listaDocumento)) {
				ConfiguracaoGEDVO configuracaoGEDVO =  getFacadeFactory().getConfiguracaoGEDFacade().consultarPorUnidadeEnsino(matriculaVO.getUnidadeEnsino().getCodigo(), false, usuarioVO);
				if(Uteis.isAtributoPreenchido(configuracaoGEDVO) && configuracaoGEDVO.getConfiguracaoGedContratoVO().getAssinarDocumento()) {
				usuarioVO.setPermitirAlunoRecusarAssinaturaContrato(false);
				for (DocumentoAssinadoVO documentoAssinadoVO: listaDocumento) {
						if((documentoAssinadoVO.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.ATA_COLACAO_GRAU) && verificarAtaColacao) || !documentoAssinadoVO.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.ATA_COLACAO_GRAU)) {
						Map<String, DocumentoAssinadoPessoaVO> hashMapContrato = realizarGeracaoPreviewDocumentoContratoAtaColacao(documentoAssinadoVO, getAplicacaoControle().getConfiguracaoGeralSistemaVO(null, usuarioVO), usuarioVO);
						if (hashMapContrato.keySet() != null && !hashMapContrato.keySet().isEmpty() && hashMapContrato.values() != null && !hashMapContrato.isEmpty()) {
							for (String caminho : hashMapContrato.keySet()) {
								if (Uteis.isAtributoPreenchido(caminho)) {
									documentoAssinadoVO.setCaminhoPreview(caminho);
								}
							}
							for (DocumentoAssinadoPessoaVO documentoAssinadoPessoaVO : hashMapContrato.values()) {
								if (Uteis.isAtributoPreenchido(documentoAssinadoPessoaVO)) {
									documentoAssinadoPessoaVO.setAssinarDocumento(true);
									documentoAssinadoVO.setDocumentoAssinadoPessoaVO(documentoAssinadoPessoaVO);
								}
							}
							usuarioVO.getDocumentoPendenteAssinatura().add(documentoAssinadoVO);
						}			
					}
					
				}
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public synchronized void realizarAssinaturaRejeicaoDocumentoAppAluno(DocumentoAssinadoVO documentoAssinadoVO, UsuarioVO usuarioVO, String latitude, String longitude, boolean assinar) throws Exception{
		if(!Uteis.isAtributoPreenchido(documentoAssinadoVO)) {
			throw new Exception("Deve ser informado o documento as ser assinado.");
		}
		if(!documentoAssinadoVO.getDocumentoAssinadoPessoaVO().getPessoaVO().getCodigo().equals(usuarioVO.getPessoa().getCodigo())) {
			throw new Exception("O usuário "+usuarioVO.getPessoa().getNome().toUpperCase()+" difere do assinate "+documentoAssinadoVO.getDocumentoAssinadoPessoaVO().getPessoaVO().getNome().toUpperCase()+" esperado no documento.");
		}
		DocumentoAssinadoPessoaVO documentoAssinadoPessoaVO = getFacadeFactory().getDocumentoAssinadoPessoaFacade().consultarPorChavePrimaria(documentoAssinadoVO.getDocumentoAssinadoPessoaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
		if(documentoAssinadoPessoaVO.getSituacaoDocumentoAssinadoPessoaEnum().equals(SituacaoDocumentoAssinadoPessoaEnum.ASSINADO)) {
			throw new Exception("Este documento já foi ASSINADO pelo usuário "+usuarioVO.getNome().toUpperCase()+".");
		}
		if(documentoAssinadoPessoaVO.getSituacaoDocumentoAssinadoPessoaEnum().equals(SituacaoDocumentoAssinadoPessoaEnum.REJEITADO)) {
			throw new Exception("Este documento já foi REJEITADO pelo usuário "+usuarioVO.getNome().toUpperCase()+".");
		}
		if(assinar) {
			realizarAssinaturaDocumentoAppAluno(documentoAssinadoVO, usuarioVO, latitude, longitude);
		}else {
			realizarRejeicaoDocumentoAppAluno(documentoAssinadoVO, usuarioVO, latitude, longitude);
		}
	}
		
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private synchronized void realizarAssinaturaDocumentoAppAluno(DocumentoAssinadoVO documentoAssinadoVO, UsuarioVO usuarioVO, String latitude, String longitude) throws Exception{
		ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO =  getAplicacaoControle().getConfiguracaoGeralSistemaVO(null, usuarioVO);
		if(documentoAssinadoVO.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.CONTRATO)) {
			documentoAssinadoVO.getListaDocumentoAssinadoPessoa().stream().filter(documentoAssinadoPessoa -> documentoAssinadoPessoa.getTipoPessoa().equals(TipoPessoa.ALUNO) || documentoAssinadoPessoa.getTipoPessoa().equals(TipoPessoa.FUNCIONARIO) || documentoAssinadoPessoa.getTipoPessoa().equals(TipoPessoa.RESPONSAVEL_FINANCEIRO)
					|| (documentoAssinadoPessoa.getTipoPessoa().equals(TipoPessoa.RESPONSAVEL_LEGAL) && documentoAssinadoPessoa.getPessoaVO().getCodigo().equals(usuarioVO.getPessoa().getCodigo()))).forEach(documentoAssinadoPessoaAluno -> {
						documentoAssinadoPessoaAluno.setAssinarDocumento(Boolean.TRUE);
						documentoAssinadoPessoaAluno.setLongitude(longitude);
						documentoAssinadoPessoaAluno.setLatitude(latitude);
					});					
			if (documentoAssinadoVO.getProvedorDeAssinaturaEnum().isProvedorSei()) {
				if (documentoAssinadoVO.getArquivo().getServidorArquivoOnline().equals(ServidorArquivoOnlineEnum.AMAZON_S3)) {
					documentoAssinadoVO.setArquivo(getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(documentoAssinadoVO.getArquivo().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, null));
					File file = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + documentoAssinadoVO.getArquivo().getPastaBaseArquivo() + File.separator + documentoAssinadoVO.getArquivo().getNome());
//					if(!file.exists() && documentoAssinadoVO.getArquivo().getServidorArquivoOnline().equals(ServidorArquivoOnlineEnum.AMAZON_S3)) {				
//						ServidorArquivoOnlineS3RS servidorExternoAmazon = new ServidorArquivoOnlineS3RS(configuracaoGeralSistemaVO.getUsuarioAutenticacao(), configuracaoGeralSistemaVO.getSenhaAutenticacao(), configuracaoGeralSistemaVO.getNomeRepositorio());
//						getFacadeFactory().getArquivoFacade().realizarDownloadArquivoAmazon(documentoAssinadoVO.getArquivo(), servidorExternoAmazon, configuracaoGeralSistemaVO, false);
//					}
				}
			}
			String arquivoTemp = getFacadeFactory().getArquivoHelper().disponibilizarArquivoAssinadoParaDowload(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + documentoAssinadoVO.getArquivo().getPastaBaseArquivo() + File.separator + documentoAssinadoVO.getArquivo().getNome(), documentoAssinadoVO.getArquivo().getNome());
			File fileAssinar = new File(arquivoTemp);
			getFacadeFactory().getDocumentoAssinadoFacade().excutarVerificacaoPessoasParaAssinarContrato(documentoAssinadoVO, fileAssinar, configuracaoGeralSistemaVO, usuarioVO);
//		}else if(documentoAssinadoVO.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.REVALIDACAO_MATRICULA)) {
//			getFacadeFactory().getMatriculaPeriodoRevalidacaoInterfaceFacade().aceitarRevalidacaoMatricula(documentoAssinadoVO.getMatriculaPeriodoRevalidacaoVO(), documentoAssinadoVO.getMatricula(), true, longitude, latitude, usuarioVO, configuracaoGeralSistemaVO);
		}else if(documentoAssinadoVO.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.ATA_COLACAO_GRAU)) {
			if (documentoAssinadoVO.getProvedorDeAssinaturaEnum().isProvedorSei()) {
				if (documentoAssinadoVO.getArquivo().getServidorArquivoOnline().equals(ServidorArquivoOnlineEnum.AMAZON_S3)) {
					documentoAssinadoVO.setArquivo(getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(documentoAssinadoVO.getArquivo().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, null));
					File file = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + documentoAssinadoVO.getArquivo().getPastaBaseArquivo() + File.separator + documentoAssinadoVO.getArquivo().getNome());
//					if(!file.exists() && documentoAssinadoVO.getArquivo().getServidorArquivoOnline().equals(ServidorArquivoOnlineEnum.AMAZON_S3)) {				
//						ServidorArquivoOnlineS3RS servidorExternoAmazon = new ServidorArquivoOnlineS3RS(configuracaoGeralSistemaVO.getUsuarioAutenticacao(), configuracaoGeralSistemaVO.getSenhaAutenticacao(), configuracaoGeralSistemaVO.getNomeRepositorio());
//						getFacadeFactory().getArquivoFacade().realizarDownloadArquivoAmazon(documentoAssinadoVO.getArquivo(), servidorExternoAmazon, configuracaoGeralSistemaVO, false);
//					}
				}
			}
			String arquivoTemp = getFacadeFactory().getArquivoHelper().disponibilizarArquivoAssinadoParaDowload(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + documentoAssinadoVO.getArquivo().getPastaBaseArquivo() + File.separator + documentoAssinadoVO.getArquivo().getNome(), documentoAssinadoVO.getArquivo().getNome());
			File fileAssinar = new File(arquivoTemp);
			getFacadeFactory().getDocumentoAssinadoFacade().excutarVerificacaoPessoasParaAssinarContrato(documentoAssinadoVO, fileAssinar, configuracaoGeralSistemaVO, usuarioVO);
			getFacadeFactory().getProgramacaoFormaturaAlunoFacade().realizarAlteracaoColouGrau(documentoAssinadoVO, usuarioVO.getPessoa().getCodigo(), SituacaoDocumentoAssinadoPessoaEnum.ASSINADO, usuarioVO);
		}
		usuarioVO.getDocumentoPendenteAssinatura().removeIf(t -> t.getCodigo().equals(documentoAssinadoVO.getCodigo()));
	}
	
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private synchronized void realizarRejeicaoDocumentoAppAluno(DocumentoAssinadoVO documentoAssinadoVO, UsuarioVO usuarioVO, String latitude, String longitude) throws Exception{
		if(documentoAssinadoVO.getDocumentoAssinadoPessoaVO().getMotivoRejeicao().trim().isEmpty()) {
			throw new Exception("O MOTIVO DE REJEIÇÃO deve ser informado.");
		}		
		if(documentoAssinadoVO.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.CONTRATO)) {
			documentoAssinadoVO.getDocumentoAssinadoPessoaVO().setSituacaoDocumentoAssinadoPessoaEnum(SituacaoDocumentoAssinadoPessoaEnum.REJEITADO);
			getFacadeFactory().getDocumentoAssinadoPessoaFacade().atualizarDadosRejeicao(documentoAssinadoVO.getDocumentoAssinadoPessoaVO());
//		}else if(documentoAssinadoVO.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.REVALIDACAO_MATRICULA)) {
//			documentoAssinadoVO.getMatriculaPeriodoRevalidacaoVO().setMotivoRejeicaoRevalidacao(documentoAssinadoVO.getDocumentoAssinadoPessoaVO().getMotivoRejeicao());
//			getFacadeFactory().getMatriculaPeriodoRevalidacaoInterfaceFacade().recusarRevalidacaoMatricula(documentoAssinadoVO.getMatriculaPeriodoRevalidacaoVO(), documentoAssinadoVO.getMatricula(), true, longitude, latitude, usuarioVO, configuracaoGeralSistemaVO);
		}else if(documentoAssinadoVO.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.ATA_COLACAO_GRAU)) {
			getFacadeFactory().getDocumentoAssinadoPessoaFacade().atualizarSituacaoPendenteDocumentoAssinadoAlunoParaRejeitado(documentoAssinadoVO, documentoAssinadoVO.getDocumentoAssinadoPessoaVO().getMotivoRejeicao(), usuarioVO);
		}
	}
	
	
	
	@Override
	public Boolean verificarAlunoAssinouContratoMatriculaParaAtivacaoMatricula(String matriculaAluno) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" select * from documentoassinado docs  ");
		sql.append(" inner join documentoassinadopessoa  docspessoa on docspessoa.documentoassinado = docs.codigo  ");
		sql.append(" inner join matricula  on matricula.matricula = docs.matricula    ");
		sql.append(" inner join curso on curso.codigo = matricula.curso    ");
		sql.append(" where  docs.tipoorigemdocumentoassinado   ='CONTRATO'  ");
		sql.append(" and docspessoa.pessoa =  matricula.aluno ");
		sql.append(" and docs.matricula = ?  ");
		sql.append(" and docspessoa.tipopessoa =  'ALUNO'  ");
		sql.append(" and docspessoa.situacaodocumentoassinadopessoa ='ASSINADO'  ");		
		sql.append(" and curso.ativarMatriculaAposAssinaturaContrato = true   ");		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), matriculaAluno );
		return tabelaResultado.next();
	}
	
	public void consultarDocumentoAssinadoPlanoEnsino(DataModelo dataModelo, PlanoEnsinoVO planoEnsinoVO,  DisciplinaVO disciplinaVO, TipoOrigemDocumentoAssinadoEnum tipoOrigemDocumentoAssinado,	SituacaoDocumentoAssinadoPessoaEnum situacaoDocumentoAssinadoPessoaEnum, GradeCurricularVO gradeCurricularVO,  UsuarioVO usuarioVO, Integer limit, Integer offset) throws Exception {
		dataModelo.setListaConsulta(new ArrayList<>());
		StringBuilder sql  =  new StringBuilder(getSqlConsultaDadosBasico());		
		sql.append(" WHERE documentoassinado.tipoorigemdocumentoassinado = 'PLANO_DE_ENSINO' and  documentoassinado.planoensino = ? ");
		if(Uteis.isAtributoPreenchido(gradeCurricularVO.getCodigo())){
			sql.append(" and  documentoassinado.gradecurricular = ").append( gradeCurricularVO.getCodigo()) ;
		}
		if(Uteis.isAtributoPreenchido(situacaoDocumentoAssinadoPessoaEnum)) {
			sql.append(" and not exists ( select documentoassinadopessoa.codigo from documentoassinadopessoa where documentoassinadopessoa.documentoassinado = documentoassinado.codigo ");
			sql.append(" and documentoassinadopessoa.situacaodocumentoassinadopessoa != '").append(situacaoDocumentoAssinadoPessoaEnum.name()).append("' ");					
			sql.append(" ) ");
		}
		SqlRowSet rs =  getConexao().getJdbcTemplate().queryForRowSet(" select count(distinct t.\"documentoassinado.codigo\") as qtde from ( " + sql.toString()+") as t ", planoEnsinoVO.getCodigo());
		System.out.println(sql.toString());
		if(rs.next()) {
			dataModelo.setTotalRegistrosEncontrados(rs.getInt("qtde"));
			if(rs.getInt("qtde") > 0) {
				sql.append(" order by documentoassinado.dataregistro desc  ");
				sql.append(" limit ").append(limit).append(" offset ").append(offset);
				dataModelo.setListaConsulta(montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), planoEnsinoVO.getCodigo()), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO));
			}
		}
	}

	public void validarUnicidadeCPFRegistroCertisign(List<DocumentPessoaRSVO> documentPessoaRSVOS) {
		documentPessoaRSVOS.stream().filter(item -> Uteis.isAtributoPreenchido(item.getIndividualIdentificationCode()))
				.collect(Collectors.groupingBy(DocumentPessoaRSVO::getIndividualIdentificationCode))
					.forEach((cpf, listaDocumentPessoaRSVO) -> {
			if (listaDocumentPessoaRSVO.size() > 1) {
				Set<DocumentPessoaRSVO> pessoasSet = new HashSet<>(listaDocumentPessoaRSVO);
				if (pessoasSet.size() > 1) {
					throw new RuntimeException("Existe mais de uma assinatura para o mesmo CPF "+ cpf +". Por favor verificar a lista de assinantes.");
				}
			}
		});
	}

    public void validarUnicidadeCPFRegistroTechCert(List<DocumentPessoaRSVO> documentPessoaRSVOS) {
        documentPessoaRSVOS.stream().filter(item -> Uteis.isAtributoPreenchido(item.getIndividualIdentificationCode()))
                .collect(Collectors.groupingBy(DocumentPessoaRSVO::getIndividualIdentificationCode))
                .forEach((cpf, listaDocumentPessoaRSVO) -> {
                    if (listaDocumentPessoaRSVO.size() > 1) {
                        Set<DocumentPessoaRSVO> pessoasSet = new HashSet<>(listaDocumentPessoaRSVO);
                        if (pessoasSet.size() > 1) {
                            throw new RuntimeException("Existe mais de uma assinatura para o mesmo CPF " + cpf + ". Por favor verificar a lista de assinantes.");
                        }
                    }
                });
    }

    private void realizarEscritaDebugSeiSignature(DocumentoAssinadoVO documentoAssinado, Boolean realizarEscritaDebug, String mensagem) {
        if (realizarEscritaDebug && Uteis.isAtributoPreenchido(documentoAssinado) && (documentoAssinado.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.DIPLOMA_DIGITAL) || documentoAssinado.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.DOCUMENTACAO_ACADEMICA_REGISTRO_DIPLOMA_DIGITAL) || documentoAssinado.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.HISTORICO_DIGITAL) || documentoAssinado.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.EXPEDICAO_DIPLOMA))) {
            AplicacaoControle.realizarEscritaTextoDebug(AssuntoDebugEnum.SEI_SIGNATURE, "Documento Assinado: " + documentoAssinado.getCodigo() + " " + documentoAssinado.getTipoOrigemDocumentoAssinadoEnum().name() + " (" + mensagem + ")");
        }
    }

    private void validarQtdAssinaturaDocumentoXML(InputStream uploadedInputStream, InputStream inputArquivoTamanho, String tamanhoArquivo, DocumentoAssinadoVO documentoAssinado, int quantidadePessoasAssinaram, Boolean escritaDebug) throws Exception {
        realizarEscritaDebugSeiSignature(documentoAssinado, escritaDebug, "(XML AMAZON) Inicio validação arquivo xml para a amazon.");
        if (inputArquivoTamanho != null && Uteis.isAtributoPreenchido(tamanhoArquivo)) {
            byte[] buffer = null;
            File xmlDiploma = new File(getCaminhoPastaWeb() + File.separator + "relatorio" + File.separator + documentoAssinado.getArquivo().getNome());
            buffer = new byte[inputArquivoTamanho.available()];
            inputArquivoTamanho.read(buffer);
            com.google.common.io.Files.write(buffer, xmlDiploma);
            realizarEscritaDebugSeiSignature(documentoAssinado, escritaDebug, "(XML AMAZON) validação no tamanho do arquivo recebido do SeiSignature [Arquivo LACUNAS: " + Long.valueOf(tamanhoArquivo) + ", Arquivo SeiSignature: " + xmlDiploma.length());
            if (xmlDiploma.exists()) {
                if (xmlDiploma.length() <= Long.valueOf(tamanhoArquivo)) {
                    realizarEscritaDebugSeiSignature(documentoAssinado, escritaDebug, "(XML AMAZON) erro na validação das assinaturas (O tamanho do arquivo assinado da LACUNAS e menor ou igual ao original / Arquivo LACUNAS: " + tamanhoArquivo + ", Arquivo original: " + xmlDiploma.length() + ").");
                    if (inputArquivoTamanho != null) {
                        inputArquivoTamanho.close();
                        inputArquivoTamanho = null;
                    }
                    if (buffer != null) {
                        buffer = null;
                    }
                    String mensagemErro = "Erro na validação das assinaturas (O tamanho do arquivo recebido da LACUNAS e diferente do arquivo recebido do SeiSignature / Arquivo LACUNAS: " + tamanhoArquivo + ", Arquivo recebido SeiSignature: " + xmlDiploma.length() + ").";
                    xmlDiploma.delete();
                    throw new Exception(mensagemErro);
                }
            }
        }
        ByteArrayOutputStream baosInput = null;
        DocumentBuilderFactory docFactory = null;
        DocumentBuilder docBuilder = null;
        Document doc = null;
        try {
            String dadosDiploma = documentoAssinado.getDecisaoJudicial() ? "DadosDiplomaPorDecisaoJudicial" : "DadosDiploma";
            String dadosRegistro = documentoAssinado.getDecisaoJudicial() ? "DadosRegistroPorDecisaoJudicial" : "DadosRegistro";
            docFactory = DocumentBuilderFactory.newInstance();
            docBuilder = docFactory.newDocumentBuilder();
            baosInput = new ByteArrayOutputStream();
            byte[] bufferInput = new byte[1024];
            int lenInput;
            while ((lenInput = uploadedInputStream.read(bufferInput)) > 0) {
                baosInput.write(bufferInput, 0, lenInput);
            }
            baosInput.flush();
            doc = docBuilder.parse(new ByteArrayInputStream(baosInput.toByteArray()));
            doc.getDocumentElement().normalize();

            // Verificando assinaturas em DadosDiploma
            Node nodeDadosDiploma = doc.getElementsByTagName(dadosDiploma).item(0);
            NodeList nodeDadosDiplomaChildNodes = nodeDadosDiploma.getChildNodes();
            List<String> assinantes = new ArrayList<>(0);
            int totalAssinaturas = 0;
            x:
            for (int i = 0; i < nodeDadosDiplomaChildNodes.getLength(); i++) {
                Node node = nodeDadosDiplomaChildNodes.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE && (Objects.equals("ds:Signature", node.getNodeName()) || Objects.equals("Signature", node.getNodeName()))) {
                    totalAssinaturas++;
                    NodeList node1ChildNodes = node.getChildNodes();
                    for (int j = 0; j < node1ChildNodes.getLength(); j++) {
                        Node node2 = node1ChildNodes.item(j);
                        if (node2.getNodeType() == Node.ELEMENT_NODE && (Objects.equals("ds:KeyInfo", node2.getNodeName()) || Objects.equals("KeyInfo", node2.getNodeName()))) {
                            NodeList node2ChildNodes = node2.getChildNodes();
                            for (int x = 0; x < node2ChildNodes.getLength(); x++) {
                                Node node3 = node2ChildNodes.item(x);
                                if (node3.getNodeType() == Node.ELEMENT_NODE && (Objects.equals("ds:X509Data", node3.getNodeName())) || Objects.equals("X509Data", node3.getNodeName())) {
                                    NodeList node3ChildNodes = node3.getChildNodes();
                                    for (int y = 0; y < node3ChildNodes.getLength(); y++) {
                                        Node node4 = node3ChildNodes.item(y);
                                        if (node4.getNodeType() == Node.ELEMENT_NODE && (Objects.equals("ds:X509SubjectName", node4.getNodeName()) || Objects.equals("X509SubjectName", node4.getNodeName()))) {
                                            assinantes.add(node4.getTextContent());
                                            continue x;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Verificando assinaturas em DadosRegistro
            Node nodeRegistro = doc.getElementsByTagName(dadosRegistro).item(0);
            NodeList nodeRegistroChildNodes = nodeRegistro.getChildNodes();
            x:
            for (int i = 0; i < nodeRegistroChildNodes.getLength(); i++) {
                Node node = nodeRegistroChildNodes.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE && (Objects.equals("ds:Signature", node.getNodeName()) || Objects.equals("Signature", node.getNodeName()))) {
                    totalAssinaturas++;
                    NodeList node1ChildNodes = node.getChildNodes();
                    for (int j = 0; j < node1ChildNodes.getLength(); j++) {
                        Node node2 = node1ChildNodes.item(j);
                        if (node2.getNodeType() == Node.ELEMENT_NODE && (Objects.equals("ds:KeyInfo", node2.getNodeName()) || Objects.equals("KeyInfo", node2.getNodeName()))) {
                            NodeList node2ChildNodes = node2.getChildNodes();
                            for (int x = 0; x < node2ChildNodes.getLength(); x++) {
                                Node node3 = node2ChildNodes.item(x);
                                if (node3.getNodeType() == Node.ELEMENT_NODE && (Objects.equals("ds:X509Data", node3.getNodeName())) || Objects.equals("X509Data", node3.getNodeName())) {
                                    NodeList node3ChildNodes = node3.getChildNodes();
                                    for (int y = 0; y < node3ChildNodes.getLength(); y++) {
                                        Node node4 = node3ChildNodes.item(y);
                                        if (node4.getNodeType() == Node.ELEMENT_NODE && (Objects.equals("ds:X509SubjectName", node4.getNodeName()) || Objects.equals("X509SubjectName", node4.getNodeName()))) {
                                            assinantes.add(node4.getTextContent());
                                            continue x;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Verificando assinaturas em Diploma
            Node nodeDiploma = doc.getElementsByTagName("Diploma").item(0);
            NodeList nodeDiplomaChildNodes = nodeDiploma.getChildNodes();
            x:
            for (int i = 0; i < nodeDiplomaChildNodes.getLength(); i++) {
                Node node = nodeDiplomaChildNodes.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE && (Objects.equals("ds:Signature", node.getNodeName()) || Objects.equals("Signature", node.getNodeName()))) {
                    totalAssinaturas++;
                    NodeList node1ChildNodes = node.getChildNodes();
                    for (int j = 0; j < node1ChildNodes.getLength(); j++) {
                        Node node2 = node1ChildNodes.item(j);
                        if (node2.getNodeType() == Node.ELEMENT_NODE && (Objects.equals("ds:KeyInfo", node2.getNodeName()) || Objects.equals("KeyInfo", node2.getNodeName()))) {
                            NodeList node2ChildNodes = node2.getChildNodes();
                            for (int x = 0; x < node2ChildNodes.getLength(); x++) {
                                Node node3 = node2ChildNodes.item(x);
                                if (node3.getNodeType() == Node.ELEMENT_NODE && (Objects.equals("ds:X509Data", node3.getNodeName())) || Objects.equals("X509Data", node3.getNodeName())) {
                                    NodeList node3ChildNodes = node3.getChildNodes();
                                    for (int y = 0; y < node3ChildNodes.getLength(); y++) {
                                        Node node4 = node3ChildNodes.item(y);
                                        if (node4.getNodeType() == Node.ELEMENT_NODE && (Objects.equals("ds:X509SubjectName", node4.getNodeName()) || Objects.equals("X509SubjectName", node4.getNodeName()))) {
                                            assinantes.add(node4.getTextContent());
                                            continue x;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            realizarEscritaDebugSeiSignature(documentoAssinado, escritaDebug, "(XML AMAZON) Final validação arquivo xml da amazon [Total assinaturas: " + totalAssinaturas + ", Quantidade pessoas assinaram: " + quantidadePessoasAssinaram + "]");
            if (Uteis.isAtributoPreenchido(assinantes)) {
                realizarEscritaDebugSeiSignature(documentoAssinado, escritaDebug, "(XML AMAZON) final validação de assinatura do xml [Assinantes: " + assinantes.stream().map(a -> a.toString()).collect(Collectors.joining(" | ")) + "]");
            }
            if (totalAssinaturas == 0) {
                throw new Exception("Houve um erro ao verificar a assinatura do documento. Não foi localizado a assinatura no documento ao realizar download da AMAZON.");
            }
            if (totalAssinaturas > quantidadePessoasAssinaram) {
                throw new Exception("Houve um erro ao verificar a assinatura do documento. Foram localizadas assinaturas excedentes no documento ao realizar download da AMAZON.");
            }
            if ((totalAssinaturas < quantidadePessoasAssinaram)) {
                throw new Exception("Houve um erro ao verificar a assinatura do documento. Foram localizadas assinaturas faltantes no documento ao realizar download da AMAZON.");
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (baosInput != null) {
                baosInput.close();
                baosInput = null;
            }
            docFactory = null;
            docBuilder = null;
            doc = null;
            if (uploadedInputStream != null) {
                uploadedInputStream.close();
                uploadedInputStream = null;
            }
            if (inputArquivoTamanho != null) {
                inputArquivoTamanho.close();
                inputArquivoTamanho = null;
            }
        }
    }

    public void realizarVerificacaoAssinaturasPDFDocumentoAssinado(UsuarioVO usuarioVO, DocumentoAssinadoVO documentoAssinadoVO, PdfReader reader) throws Exception {
        //TODO REALIZAR ADEQUACAO PARA E-CNPJ
        int quantidadeEncontrada = 0;
        long quantidadeAssinaturasUsuario = documentoAssinadoVO.getListaDocumentoAssinadoPessoa().stream().filter(dap -> dap.getPessoaVO().getCodigo().equals(usuarioVO.getPessoa().getCodigo())).count();
        if (quantidadeAssinaturasUsuario > 0) {
            Provider provider = new BouncyCastleProvider();
            Security.addProvider(provider);
            AcroFields af = reader.getAcroFields();
            ArrayList<String> names = af.getSignatureNames();
            for (String name : names) {
                PdfPKCS7 pk = af.verifySignature(name, provider.getName());
                if (pk.getSigningCertificate().toString().contains(Uteis.removerMascara(usuarioVO.getPessoa().getCPF()))) {
                    quantidadeEncontrada++;
                }
                //af.removeField(name);
            }
            if (quantidadeEncontrada == 0) {
                throw new Exception("Houve um erro ao verificar a assinatura da pessoa " + usuarioVO.getPessoa().getNome() + ". Não foi localizado a assinatura no documento.");
            }
            if (quantidadeEncontrada > quantidadeAssinaturasUsuario) {
                throw new Exception("Houve um erro ao verificar a assinatura da pessoa " + usuarioVO.getPessoa().getNome() + ". Foram localizadas assinaturas excedentes para o assinante.");
            }
        }
	}
	
//	@Override
//	public String realizarGeracaoPreviewRepresentacaoVisual(DocumentoAssinadoVO documentoAssinado, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
//		if (documentoAssinado.getArquivoVisual().getServidorArquivoOnline().equals(ServidorArquivoOnlineEnum.AMAZON_S3)) {
//			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaAmazon = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesDiretorioUpload();
////			ServidorArquivoOnlineS3RS servidorArquivoOnlineS3RS = new ServidorArquivoOnlineS3RS(configuracaoGeralSistemaAmazon.getUsuarioAutenticacao(), configuracaoGeralSistemaAmazon.getSenhaAutenticacao(), configuracaoGeralSistemaAmazon.getNomeRepositorio());
//			String nomeArquivoUsar = documentoAssinado.getArquivoVisual().getDescricao().contains(".") ? documentoAssinado.getArquivoVisual().getDescricao() : documentoAssinado.getArquivoVisual().getDescricao() + (documentoAssinado.getArquivoVisual().getNome().substring(documentoAssinado.getArquivoVisual().getNome().lastIndexOf("."), documentoAssinado.getArquivoVisual().getNome().length()));
////			if (servidorArquivoOnlineS3RS.consultarSeObjetoExiste(documentoAssinado.getArquivoVisual().recuperarNomeArquivoServidorExterno(documentoAssinado.getArquivoVisual().getPastaBaseArquivo(), configuracaoGeralSistemaAmazon.getLocalUploadArquivoTemp(), nomeArquivoUsar))) {
////				File file = new File(getCaminhoPastaWeb() + "relatorio" + File.separator + documentoAssinado.getArquivoVisual().getNome());
////				if (file.exists()) {
////					file.delete();
////				}
////				getFacadeFactory().getArquivoHelper().realizarDownloadArquivoAmazon(documentoAssinado.getArquivoVisual(), UteisJSF.getCaminhoWeb() + File.separator + Constantes.relatorio + File.separator, configuracaoGeralSistemaAmazon);
////				return ((configuracaoGeralSistemaAmazon.getUrlAcessoExternoAplicacao() + File.separator + "relatorio" + File.separator + file.getName() + "?embedded=true").replace(File.separator, "/")).replace(File.separator, "/");
////			} else {
////				nomeArquivoUsar = documentoAssinado.getArquivoVisual().getDescricao();
//////				if (servidorArquivoOnlineS3RS.consultarSeObjetoExiste(documentoAssinado.getArquivoVisual().recuperarNomeArquivoServidorExterno(documentoAssinado.getArquivoVisual().getPastaBaseArquivo(), configuracaoGeralSistemaVO.getLocalUploadArquivoTemp(), nomeArquivoUsar))) {
//////					File file = new File(getCaminhoPastaWeb() + "relatorio" + File.separator + documentoAssinado.getArquivoVisual().getNome());
//////					if (file.exists()) {
//////						file.delete();
//////					}
//////					getFacadeFactory().getArquivoHelper().realizarDownloadArquivoAmazon(documentoAssinado.getArquivoVisual(), UteisJSF.getCaminhoWeb() + File.separator + Constantes.relatorio + File.separator, configuracaoGeralSistemaVO);
//////					return ((configuracaoGeralSistemaVO.getUrlAcessoExternoAplicacao() + File.separator + "relatorio" + File.separator + file.getName() + "?embedded=true").replace(File.separator, "/")).replace(File.separator, "/");
//////				} else {
//////					throw new Exception("Não foi encontrado no repositório da AMAZON o aquivo no caminho " + documentoAssinado.getArquivoVisual().recuperarNomeArquivoServidorExterno(documentoAssinado.getArquivoVisual().getPastaBaseArquivo(), configuracaoGeralSistemaVO.getLocalUploadArquivoTemp(), nomeArquivoUsar) + ".");
//////				}
////			}
//		} else {
//			return (configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo() + "/" + documentoAssinado.getArquivoVisual().getPastaBaseArquivo() + "/" + documentoAssinado.getArquivoVisual().getNome() + "?embedded=true").replace(File.separator, "/");
//		}
//	}
	
	@Override
	public void verificarExpedicaoDiplomaContemDocumentoAssinado(ExpedicaoDiplomaVO expedicaoDiplomaVO) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT codigo ");
		sql.append("FROM documentoassinado WHERE ");
		sql.append("expedicaodiploma = ? ");
		sql.append("AND matricula = ? ");
		sql.append("AND tipoorigemdocumentoassinado IN ('DIPLOMA_DIGITAL', 'DOCUMENTACAO_ACADEMICA_REGISTRO_DIPLOMA_DIGITAL', 'HISTORICO_DIGITAL') ");
		sql.append("AND EXISTS (SELECT FROM documentoassinadopessoa WHERE documentoassinadopessoa.documentoassinado = documentoassinado.codigo AND documentoassinadopessoa.situacaodocumentoassinadopessoa IN ('ASSINADO')) ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), expedicaoDiplomaVO.getCodigo(), expedicaoDiplomaVO.getMatricula().getMatricula());
		while (tabelaResultado.next()) {
			throw new Exception("Expedição de diploma não pode ser excluída porque tem documentos assinados que foram assinados pela LACUNAS (Diploma digital, Documentação acadêmica registro diploma digital, Histórico digital).");
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)	
	public void realizarExclusaoDocumentoAssinadoVinculadoExpedicaoDiploma(ExpedicaoDiplomaVO expedicaoDiplomaVO, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		StringBuilder sql = new StringBuilder(getSqlConsultaDadosBasico());
		sql.append("WHERE documentoassinado.matricula = ? AND documentoassinado.expedicaodiploma = ?");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), expedicaoDiplomaVO.getMatricula().getMatricula(), expedicaoDiplomaVO.getCodigo());
		List<DocumentoAssinadoVO> documentoAssinadoVOs = new ArrayList<>(0);
		while (tabelaResultado.next()) {
			documentoAssinadoVOs.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO));
		}
		if (Uteis.isAtributoPreenchido(documentoAssinadoVOs)) {
			for (DocumentoAssinadoVO obj : documentoAssinadoVOs) {
				excluir(obj, false, usuarioVO, configuracaoGeralSistemaVO);
			}
		}
	}
	
	@Override
	public void adicionarMarcaDaguaPDF(String arquivoOrigem, ConfiguracaoGeralSistemaVO config, String caminhoWeb, DocumentoAssinadoVO documentoAssinadoVO) throws Exception {
		String caminhoMarcaDagua = caminhoWeb  + "resources" + File.separator + "imagens" + File.separator + "rascunho.png";
		String caminhoBase = config.getLocalUploadArquivoFixo() + File.separator + documentoAssinadoVO.getArquivoVisual().getPastaBaseArquivo();
		getFacadeFactory().getArquivoHelper().adicionarMarcaDaguaPDF(caminhoMarcaDagua, arquivoOrigem, caminhoBase);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void realizarAssinaturaDiplomaDigital(ExpedicaoDiplomaVO expedicaoDiplomaVO, File arquivoXML, File arquivoVisual, ConfiguracaoGeralSistemaVO config, TipoOrigemDocumentoAssinadoEnum tipoOrigemDocumentoAssinadoEnum, UsuarioVO usuarioVO, Boolean persistirDocumentoAssinado, HistoricoAlunoRelVO histAlunoRelVO) throws Exception {
		DocumentoAssinadoVO obj = new DocumentoAssinadoVO();
		obj.setDataRegistro(new Date());
		obj.setUsuario(usuarioVO);
		obj.setMatricula(expedicaoDiplomaVO.getMatricula());
		obj.setTipoOrigemDocumentoAssinadoEnum(tipoOrigemDocumentoAssinadoEnum);
		obj.setUnidadeEnsinoVO(expedicaoDiplomaVO.getUnidadeEnsinoCertificadora());
		obj.setTurma(expedicaoDiplomaVO.getMatricula().getUltimoMatriculaPeriodoVO().getTurma());
		obj.setCursoVO(expedicaoDiplomaVO.getMatricula().getCurso());
		obj.setCodigoValidacaoHistoricoDigital(expedicaoDiplomaVO.getDocumentoAssinadoVO().getCodigoValidacaoHistoricoDigital());
		obj.setDecisaoJudicial((tipoOrigemDocumentoAssinadoEnum.equals(TipoOrigemDocumentoAssinadoEnum.DIPLOMA_DIGITAL) || tipoOrigemDocumentoAssinadoEnum.equals(TipoOrigemDocumentoAssinadoEnum.DOCUMENTACAO_ACADEMICA_REGISTRO_DIPLOMA_DIGITAL)) && expedicaoDiplomaVO.getEmitidoPorDecisaoJudicial());
		obj.setVersaoDiploma(expedicaoDiplomaVO.getVersaoDiploma());
		obj.setExpedicaoDiplomaVO(expedicaoDiplomaVO);
		obj.setIdDiplomaDigital(expedicaoDiplomaVO.getIdDiplomaDigital());
		obj.setIdDadosRegistrosDiplomaDigital(expedicaoDiplomaVO.getIdDadosRegistrosDiplomaDigital());
		// ASSINATURA 1 E 2 (CPF IES EMISSORA)
		if (!TipoOrigemDocumentoAssinadoEnum.HISTORICO_DIGITAL.equals(tipoOrigemDocumentoAssinadoEnum)) {
			obj.getListaDocumentoAssinadoPessoa().add(new DocumentoAssinadoPessoaVO(new Date(), expedicaoDiplomaVO.getFuncionarioPrimarioVO().getPessoa(), TipoPessoa.FUNCIONARIO, SituacaoDocumentoAssinadoPessoaEnum.PENDENTE, obj, 1, expedicaoDiplomaVO.getCargoFuncionarioPrincipalVO().getNome(), expedicaoDiplomaVO.getTituloFuncionarioPrincipal(), null, TipoAssinaturaDocumentoEnum.NENHUM));
			if (!TipoOrigemDocumentoAssinadoEnum.DOCUMENTACAO_ACADEMICA_REGISTRO_DIPLOMA_DIGITAL.equals(tipoOrigemDocumentoAssinadoEnum) && Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getFuncionarioSecundarioVO().getCodigo())) {
				obj.getListaDocumentoAssinadoPessoa().add(new DocumentoAssinadoPessoaVO(new Date(), expedicaoDiplomaVO.getFuncionarioSecundarioVO().getPessoa(), TipoPessoa.FUNCIONARIO, SituacaoDocumentoAssinadoPessoaEnum.PENDENTE, obj, 2, expedicaoDiplomaVO.getCargoFuncionarioSecundarioVO().getNome(), expedicaoDiplomaVO.getTituloFuncionarioSecundario(), null, TipoAssinaturaDocumentoEnum.NENHUM));
			}
		}

		obj.setArquivo(criarArquivoDiplomaDigital(expedicaoDiplomaVO.getMatricula().getMatricula(), arquivoXML, tipoOrigemDocumentoAssinadoEnum, persistirDocumentoAssinado, config, usuarioVO, expedicaoDiplomaVO));
		if (arquivoVisual != null && TipoOrigemDocumentoAssinadoEnum.DIPLOMA_DIGITAL.equals(tipoOrigemDocumentoAssinadoEnum)) {
			obj.setArquivoVisual(criarArquivoDiplomaDigital(expedicaoDiplomaVO.getMatricula().getMatricula(), arquivoVisual, tipoOrigemDocumentoAssinadoEnum, persistirDocumentoAssinado, config, usuarioVO, expedicaoDiplomaVO));
		}
		// ASSINATURA 3 (CNPJ IES EMISSORA)
		if (Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getFuncionarioTerceiroVO()) && !TipoOrigemDocumentoAssinadoEnum.HISTORICO_DIGITAL.equals(tipoOrigemDocumentoAssinadoEnum)) {
			DocumentoAssinadoPessoaVO documentoAssinadoPessoa = new DocumentoAssinadoPessoaVO(new Date(), expedicaoDiplomaVO.getFuncionarioTerceiroVO().getPessoa(), TipoPessoa.FUNCIONARIO, SituacaoDocumentoAssinadoPessoaEnum.PENDENTE, obj, 3, expedicaoDiplomaVO.getCargoFuncionarioTerceiroVO().getNome(), expedicaoDiplomaVO.getTituloFuncionarioTerceiro(), null, TipoAssinaturaDocumentoEnum.NENHUM);
			if (expedicaoDiplomaVO.getMatricula().getCurso().getNivelEducacionalGraduacaoGraduacaoTecnologica()) {
				documentoAssinadoPessoa.setAssinarPorCNPJ(true);
			}
			obj.getListaDocumentoAssinadoPessoa().add(documentoAssinadoPessoa);
		}

		// ASSINATURA 4 (CPF IES REGISTRADORA)
		if (!TipoOrigemDocumentoAssinadoEnum.DOCUMENTACAO_ACADEMICA_REGISTRO_DIPLOMA_DIGITAL.equals(tipoOrigemDocumentoAssinadoEnum) && !TipoOrigemDocumentoAssinadoEnum.HISTORICO_DIGITAL.equals(tipoOrigemDocumentoAssinadoEnum)) {
			if (Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getFuncionarioQuartoVO())) {
				DocumentoAssinadoPessoaVO documentoAssinadoPessoa = new DocumentoAssinadoPessoaVO(new Date(), expedicaoDiplomaVO.getFuncionarioQuartoVO().getPessoa(), TipoPessoa.FUNCIONARIO, SituacaoDocumentoAssinadoPessoaEnum.PENDENTE, obj, 4, expedicaoDiplomaVO.getCargoFuncionarioQuartoVO().getNome(), expedicaoDiplomaVO.getTituloFuncionarioQuarto(), null, TipoAssinaturaDocumentoEnum.NENHUM);
				obj.getListaDocumentoAssinadoPessoa().add(documentoAssinadoPessoa);
			} else if (TipoOrigemDocumentoAssinadoEnum.DIPLOMA_DIGITAL.equals(tipoOrigemDocumentoAssinadoEnum)) {
				obj.getListaDocumentoAssinadoPessoa().add(realizarGeracaoAssinantePessoaPadrao(4, "E-CPF Registradora", false, obj));
			}
		}

		// ASSINATURA 5 (CNPJ IES REGISTRADORA)
		if (Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getFuncionarioQuintoVO()) && !TipoOrigemDocumentoAssinadoEnum.HISTORICO_DIGITAL.equals(tipoOrigemDocumentoAssinadoEnum)) {
			DocumentoAssinadoPessoaVO documentoAssinadoPessoa = new DocumentoAssinadoPessoaVO(new Date(), expedicaoDiplomaVO.getFuncionarioQuintoVO().getPessoa(), TipoPessoa.FUNCIONARIO, SituacaoDocumentoAssinadoPessoaEnum.PENDENTE, obj, 5, expedicaoDiplomaVO.getCargoFuncionarioQuintoVO().getNome(), expedicaoDiplomaVO.getTituloFuncionarioQuinto(), null, TipoAssinaturaDocumentoEnum.NENHUM);
			if (expedicaoDiplomaVO.getMatricula().getCurso().getNivelEducacionalGraduacaoGraduacaoTecnologica()) {
				documentoAssinadoPessoa.setAssinarPorCNPJ(true);
			}
			obj.getListaDocumentoAssinadoPessoa().add(documentoAssinadoPessoa);
		} else if (TipoOrigemDocumentoAssinadoEnum.DIPLOMA_DIGITAL.equals(tipoOrigemDocumentoAssinadoEnum)) {
			obj.getListaDocumentoAssinadoPessoa().add(realizarGeracaoAssinantePessoaPadrao(5, "E-CNPJ Registradora", true, obj));
		} else if (TipoOrigemDocumentoAssinadoEnum.DOCUMENTACAO_ACADEMICA_REGISTRO_DIPLOMA_DIGITAL.equals(tipoOrigemDocumentoAssinadoEnum)) {
			DocumentoAssinadoPessoaVO documentoAssinadoPessoa = new DocumentoAssinadoPessoaVO(new Date(), expedicaoDiplomaVO.getFuncionarioTerceiroVO().getPessoa(), TipoPessoa.FUNCIONARIO, SituacaoDocumentoAssinadoPessoaEnum.PENDENTE, obj, 5, expedicaoDiplomaVO.getCargoFuncionarioTerceiroVO().getNome(), expedicaoDiplomaVO.getTituloFuncionarioTerceiro(), null, TipoAssinaturaDocumentoEnum.NENHUM);
			if (expedicaoDiplomaVO.getMatricula().getCurso().getNivelEducacionalGraduacaoGraduacaoTecnologica()) {
				documentoAssinadoPessoa.setAssinarPorCNPJ(true);
			}
			obj.getListaDocumentoAssinadoPessoa().add(documentoAssinadoPessoa);
		}

		if (TipoOrigemDocumentoAssinadoEnum.HISTORICO_DIGITAL.equals(tipoOrigemDocumentoAssinadoEnum)) {
			DocumentoAssinadoPessoaVO documentoAssinadoPessoaPrincipal = new DocumentoAssinadoPessoaVO(new Date(), expedicaoDiplomaVO.getFuncionarioPrimarioVO().getPessoa(), TipoPessoa.FUNCIONARIO, SituacaoDocumentoAssinadoPessoaEnum.PENDENTE, obj, 1, expedicaoDiplomaVO.getCargoFuncionarioPrincipalVO().getNome(), expedicaoDiplomaVO.getTituloFuncionarioPrincipal(), null, TipoAssinaturaDocumentoEnum.NENHUM);
			obj.getListaDocumentoAssinadoPessoa().add(documentoAssinadoPessoaPrincipal);
			if (histAlunoRelVO.getCurriculoIntegralizado() || histAlunoRelVO.getMatriculaVO().getSituacao().equals("FO")) {
				DocumentoAssinadoPessoaVO documentoAssinadoPessoaSecundario = new DocumentoAssinadoPessoaVO(new Date(), expedicaoDiplomaVO.getFuncionarioTerceiroVO().getPessoa(), TipoPessoa.FUNCIONARIO, SituacaoDocumentoAssinadoPessoaEnum.PENDENTE, obj, 3, expedicaoDiplomaVO.getCargoFuncionarioTerceiroVO().getNome(), expedicaoDiplomaVO.getTituloFuncionarioTerceiro(), null, TipoAssinaturaDocumentoEnum.NENHUM);
				if (expedicaoDiplomaVO.getMatricula().getCurso().getNivelEducacionalGraduacaoGraduacaoTecnologica()) {
					documentoAssinadoPessoaSecundario.setAssinarPorCNPJ(true);
				}
				obj.getListaDocumentoAssinadoPessoa().add(documentoAssinadoPessoaSecundario);
			}
			obj.setArquivoVisual(criarArquivoDiplomaDigital(expedicaoDiplomaVO.getMatricula().getMatricula(), arquivoVisual, tipoOrigemDocumentoAssinadoEnum, persistirDocumentoAssinado, config, usuarioVO, expedicaoDiplomaVO));
			obj.setGradecurricular(histAlunoRelVO.getGradeCurricularVO());
		}

        if (persistirDocumentoAssinado && !expedicaoDiplomaVO.getNovaGeracaoRepresentacaoVisualDiplomaDigital()) {
            incluir(obj, false, usuarioVO, config);
        } else if (expedicaoDiplomaVO.getNovaGeracaoRepresentacaoVisualDiplomaDigital()) {
            if (obj.getArquivoVisual() != null && Uteis.isAtributoPreenchido(obj.getArquivoVisual().getNome())) {
                getFacadeFactory().getArquivoFacade().incluir(obj.getArquivoVisual(), usuarioVO, config);
            }
        }
        File arquivoDiretorioCorreto = null;
        if (arquivoXML != null && !expedicaoDiplomaVO.getNovaGeracaoRepresentacaoVisualDiplomaDigital()) {
            arquivoDiretorioCorreto = new File(config.getLocalUploadArquivoFixo() + File.separator + obj.getArquivo().getPastaBaseArquivo() + File.separator + obj.getArquivo().getNome());
            getFacadeFactory().getArquivoHelper().copiar(arquivoXML, arquivoDiretorioCorreto);
            if (TipoOrigemDocumentoAssinadoEnum.DOCUMENTACAO_ACADEMICA_REGISTRO_DIPLOMA_DIGITAL.equals(tipoOrigemDocumentoAssinadoEnum) && obj.getArquivo().getServidorArquivoOnline().equals(ServidorArquivoOnlineEnum.AMAZON_S3)) {
                realizarUploadArquivoAmazon(obj.getArquivo(), config, true);
            }
        }
        if (arquivoVisual != null && (TipoOrigemDocumentoAssinadoEnum.DIPLOMA_DIGITAL.equals(tipoOrigemDocumentoAssinadoEnum) || TipoOrigemDocumentoAssinadoEnum.HISTORICO_DIGITAL.equals(tipoOrigemDocumentoAssinadoEnum))) {
            arquivoDiretorioCorreto = new File(config.getLocalUploadArquivoFixo() + File.separator + obj.getArquivoVisual().getPastaBaseArquivo() + File.separator + obj.getArquivoVisual().getNome());
            getFacadeFactory().getArquivoHelper().copiar(arquivoVisual, arquivoDiretorioCorreto);
        }
        expedicaoDiplomaVO.setDocumentoAssinadoVO(obj);
    }

    private ArquivoVO criarArquivoDiplomaDigital(String matricula, File arquivo, TipoOrigemDocumentoAssinadoEnum tipoOrigemDocumentoAssinadoEnum, Boolean persistirDocumentoAssinado, ConfiguracaoGeralSistemaVO config, UsuarioVO usuarioVO, ExpedicaoDiplomaVO expedicaoDiplomaVO) throws IOException {
        ArquivoVO obj = new ArquivoVO();
        obj.setNome(arquivo.getName());
        obj.setExtensao(FilenameUtils.getExtension(arquivo.getName()));
        obj.setDescricao(tipoOrigemDocumentoAssinadoEnum.getDescricao() + Constantes.UNDERSCORE + matricula + Constantes.UNDERSCORE + Uteis.getDataAplicandoFormatacao(new Date(), Constantes.MASCARA_ddMMyyyyHHmmss) + Constantes.UNDERSCORE + obj.getExtensao());
        obj.setResponsavelUpload(usuarioVO);
        obj.setDataUpload(new Date());
        obj.setDataDisponibilizacao(new Date());
        obj.setManterDisponibilizacao(true);
        obj.setControlarDownload(true);
        obj.setDataIndisponibilizacao(null);
        obj.setSituacao(SituacaoArquivo.ATIVO.getValor());
        obj.setPastaBaseArquivo(PastaBaseArquivoEnum.DOCUMENTOS_ASSINADOS_TMP.getValue());
        obj.setPastaBaseArquivoEnum(PastaBaseArquivoEnum.DOCUMENTOS_ASSINADOS_TMP);
        obj.setArquivoAssinadoDigitalmente(true);
        obj.setPessoaVO(expedicaoDiplomaVO.getMatricula().getAluno());
        obj.setServidorArquivoOnline(ServidorArquivoOnlineEnum.getEnum(config.getServidorArquivoOnline()));
        File arquivoDiretorioCorreto = new File(config.getLocalUploadArquivoFixo() + File.separator + obj.getPastaBaseArquivo() + File.separator + obj.getNome());
        getFacadeFactory().getArquivoHelper().copiar(arquivo, arquivoDiretorioCorreto);
        return obj;
    }

    @Override
    public String realizarGeracaoPreviewDocumento(DocumentoAssinadoVO documentoAssinado, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario) throws Exception {
        if (documentoAssinado.getProvedorDeAssinaturaEnum().isProvedorSei()) {
            if (documentoAssinado.getArquivo().getServidorArquivoOnline().equals(ServidorArquivoOnlineEnum.AMAZON_S3)) {
                ConfiguracaoGeralSistemaVO configuracaoGeralSistemaAmazon = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesDiretorioUpload();
//                ServidorArquivoOnlineS3RS servidorArquivoOnlineS3RS = new ServidorArquivoOnlineS3RS(configuracaoGeralSistemaAmazon.getUsuarioAutenticacao(), configuracaoGeralSistemaAmazon.getSenhaAutenticacao(), configuracaoGeralSistemaAmazon.getNomeRepositorio());
                String nomeArquivoUsar = documentoAssinado.getArquivo().getDescricao().contains(".") ? documentoAssinado.getArquivo().getDescricao() : documentoAssinado.getArquivo().getDescricao() + (documentoAssinado.getArquivo().getNome().substring(documentoAssinado.getArquivo().getNome().lastIndexOf("."), documentoAssinado.getArquivo().getNome().length()));
//                if (servidorArquivoOnlineS3RS.consultarSeObjetoExiste(documentoAssinado.getArquivo().recuperarNomeArquivoServidorExterno(documentoAssinado.getArquivo().getPastaBaseArquivo(), configuracaoGeralSistemaAmazon.getLocalUploadArquivoTemp(), nomeArquivoUsar))) {
//                    File file = new File(getCaminhoPastaWeb() + "relatorio" + File.separator + documentoAssinado.getArquivo().getNome());
//                    if (file.exists()) {
//                        file.delete();
//                    }
//                    getFacadeFactory().getArquivoHelper().realizarDownloadArquivoAmazon(documentoAssinado.getArquivo(), UteisJSF.getCaminhoWeb() + File.separator + Constantes.relatorio + File.separator, configuracaoGeralSistemaAmazon);
//                    return ((configuracaoGeralSistemaAmazon.getUrlAcessoExternoAplicacao() + File.separator + "relatorio" + File.separator + file.getName() + "?embedded=true").replace(File.separator, "/")).replace(File.separator, "/");
//                } else {
//                    nomeArquivoUsar = documentoAssinado.getArquivo().getDescricao();
////                    if (servidorArquivoOnlineS3RS.consultarSeObjetoExiste(documentoAssinado.getArquivo().recuperarNomeArquivoServidorExterno(documentoAssinado.getArquivo().getPastaBaseArquivo(), configuracaoGeralSistemaVO.getLocalUploadArquivoTemp(), nomeArquivoUsar))) {
////                        File file = new File(getCaminhoPastaWeb() + "relatorio" + File.separator + documentoAssinado.getArquivo().getNome());
////                        if (file.exists()) {
////                            file.delete();
////                        }
////                        getFacadeFactory().getArquivoHelper().realizarDownloadArquivoAmazon(documentoAssinado.getArquivo(), UteisJSF.getCaminhoWeb() + File.separator + Constantes.relatorio + File.separator, configuracaoGeralSistemaVO);
////                        return ((configuracaoGeralSistemaVO.getUrlAcessoExternoAplicacao() + File.separator + "relatorio" + File.separator + file.getName() + "?embedded=true").replace(File.separator, "/")).replace(File.separator, "/");
////                    } else {
////                        throw new Exception("Não foi encontrado no repositório da AMAZON o aquivo no caminho " + documentoAssinado.getArquivo().recuperarNomeArquivoServidorExterno(documentoAssinado.getArquivo().getPastaBaseArquivo(), configuracaoGeralSistemaVO.getLocalUploadArquivoTemp(), nomeArquivoUsar) + ".");
////                    }
//                }
            } else {
                return (configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo() + "/" + documentoAssinado.getArquivo().getPastaBaseArquivo() + "/" + documentoAssinado.getArquivo().getNome() + "?embedded=true&timestamp=" + System.currentTimeMillis()).replace(File.separator, "/");
            }
        }
        if (documentoAssinado.getProvedorDeAssinaturaEnum().isProvedorTechCert()) {
            getFacadeFactory().getDocumentoAssinadoFacade().realizarVisualizacaoArquivoProvedorTechCert(documentoAssinado, configuracaoGeralSistemaVO, null);
            return configuracaoGeralSistemaVO.getUrlAcessoExternoAplicacao() + "/relatorio/" + documentoAssinado.getArquivo().getNome();
        } else {
            getFacadeFactory().getDocumentoAssinadoFacade().realizarVisualizacaoArquivoProvedorCertisign(documentoAssinado, configuracaoGeralSistemaVO, false, true, false, null);
            return configuracaoGeralSistemaVO.getUrlAcessoExternoAplicacao() + "/relatorio/" + documentoAssinado.getArquivo().getNome();
        }
    }

    public void validarDadosDiplomaXML(Document doc, String CPF, DocumentoAssinadoVO documentoAssinado) throws Exception {
        String dadosDiploma = documentoAssinado.getDecisaoJudicial() ? "DadosDiplomaPorDecisaoJudicial" : "DadosDiploma";
        String dadosRegistro = documentoAssinado.getDecisaoJudicial() ? "DadosRegistroPorDecisaoJudicial" : "DadosRegistro";
        if (doc.getElementsByTagName(dadosDiploma).item(0) == null || doc.getElementsByTagName(dadosRegistro).item(0) == null || doc.getElementsByTagName("Diploma").item(0) == null) {
            throw new Exception("O XML informado é inválido ou incompleto, verifique o mesmo e tente novamente.");
        }
        Node nodePrincpal = null;

        if (Uteis.isAtributoPreenchido(documentoAssinado.getVersaoDiploma())) {
            Node nodeVersao = null;
            nodeVersao = doc.getElementsByTagName("Diploma").item(0);
            NodeList nodeVersaoChildNodes = nodeVersao.getChildNodes();
            for (int i = 0; i < nodeVersaoChildNodes.getLength(); i++) {
                Node node = nodeVersaoChildNodes.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE && Objects.equals("infDiploma", node.getNodeName())) {
                    NamedNodeMap atri = node.getAttributes();
                    Node versao = atri.getNamedItem("versao");
                    if (!Objects.equals(documentoAssinado.getVersaoDiploma().getValor(), versao.getNodeValue())) {
                        throw new Exception("A versão do diploma no xml (" + versao.getNodeValue() + ") e diferente do Documento Assinado (" + documentoAssinado.getVersaoDiploma().getValor() + ").");
                    }
                    break;
                }
            }
        }

        nodePrincpal = doc.getElementsByTagName(dadosDiploma).item(0);
        NodeList nodePrincpalChildNodes = nodePrincpal.getChildNodes();
        for (int i = 0; i < nodePrincpalChildNodes.getLength(); i++) {
            Node node = nodePrincpalChildNodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE && Objects.equals("Diplomado", node.getNodeName())) {
                NodeList node1ChildNodes = node.getChildNodes();
                for (int j = 0; j < node1ChildNodes.getLength(); j++) {
                    Node node2 = node1ChildNodes.item(j);
                    if (node2.getNodeType() == Node.ELEMENT_NODE && Objects.equals("CPF", node2.getNodeName())) {
                        if (!node2.getTextContent().equals(CPF)) {
                            throw new Exception("O CPF presente no XML enviado não é igual ao aluno do diploma.");
                        }
                    }
                }
            }
        }
    }

    public Boolean verificarPermissaoAssinaturaDiarioPeloProfessorAposAssinaturaCoordenadorCurso(UsuarioVO usuario) {
        try {
            verificarPermissaoUsuarioFuncionalidade("PermitirAssinaturaDiarioPeloProfessorAposAssinaturaCoordenadorCurso", usuario);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Integer validarAssinaturaDocumentoAssinado(String assinatura, DocumentoAssinadoPessoaVO documentoAssinadoPessoaVO, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO config, ByteArrayOutputStream byteXML, DocumentoAssinadoVO documentoAssinadoVO, String tipo, Boolean validarCPFCNPJ) throws Exception {
        Integer quantidadeRegistrosAlterados = 0;
        if (validarCPFCNPJ &&
                (tipo.equals("CPF") && assinatura != null && ((Uteis.isAtributoPreenchido(documentoAssinadoPessoaVO.getPessoaVO().getCPF()) && assinatura.contains(Uteis.removerMascara(documentoAssinadoPessoaVO.getPessoaVO().getCPF().trim()))) || (Uteis.isAtributoPreenchido(documentoAssinadoPessoaVO.getNome()) && documentoAssinadoPessoaVO.getNome().equals("E-CPF Registradora") && documentoAssinadoPessoaVO.getTipoPessoa().equals(TipoPessoa.MEMBRO_COMUNIDADE)))) ||
                (tipo.equals("CNPJ") && assinatura != null && documentoAssinadoPessoaVO.getAssinarPorCNPJ() && assinatura.contains(Uteis.removerMascara(documentoAssinadoVO.getUnidadeEnsinoVO().getCNPJ()))) ||
                (tipo.equals("CNPJ_CERTIFICADORA") && assinatura != null && documentoAssinadoPessoaVO.getAssinarPorCNPJ() && ((Uteis.isAtributoPreenchido(documentoAssinadoVO.getUnidadeEnsinoVO().getCnpjUnidadeCertificadora()) && assinatura.contains(Uteis.removerMascara(documentoAssinadoVO.getUnidadeEnsinoVO().getCnpjUnidadeCertificadora()))) || (Uteis.isAtributoPreenchido(documentoAssinadoPessoaVO.getNome()) && documentoAssinadoPessoaVO.getNome().equals("E-CNPJ Registradora") && documentoAssinadoPessoaVO.getTipoPessoa().equals(TipoPessoa.MEMBRO_COMUNIDADE))))) {
            quantidadeRegistrosAlterados += getFacadeFactory().getDocumentoAssinadoFacade().executarAtualizacaoDocumentoAssinadoPorSeiSignature(documentoAssinadoVO, null, byteXML, Boolean.TRUE, Constantes.EMPTY, verificarPermissaoAssinaturaDiarioPeloProfessorAposAssinaturaCoordenadorCurso(usuarioVO), true, config, usuarioVO, documentoAssinadoPessoaVO.getAssinarPorCNPJ(), documentoAssinadoPessoaVO.getOrdemAssinatura(), "EXTERNA", documentoAssinadoVO.getArquivo().getServidorArquivoOnline().equals(ServidorArquivoOnlineEnum.AMAZON_S3), Boolean.FALSE);
        } else if (!validarCPFCNPJ) {
            quantidadeRegistrosAlterados += getFacadeFactory().getDocumentoAssinadoFacade().executarAtualizacaoDocumentoAssinadoPorSeiSignature(documentoAssinadoVO, null, byteXML, Boolean.TRUE, Constantes.EMPTY, verificarPermissaoAssinaturaDiarioPeloProfessorAposAssinaturaCoordenadorCurso(usuarioVO), true, config, usuarioVO, false, documentoAssinadoPessoaVO.getOrdemAssinatura(), "EXTERNA", documentoAssinadoVO.getArquivo().getServidorArquivoOnline().equals(ServidorArquivoOnlineEnum.AMAZON_S3), Boolean.FALSE);
        }
        if (!(quantidadeRegistrosAlterados != 0)) {
            tratarRetornoMensagemAssinatura(assinatura, documentoAssinadoPessoaVO, documentoAssinadoVO, tipo, validarCPFCNPJ);
        }
        return quantidadeRegistrosAlterados;
    }

    private void tratarRetornoMensagemAssinatura(String assinatura, DocumentoAssinadoPessoaVO documentoAssinadoPessoaVO, DocumentoAssinadoVO documentoAssinadoVO, String tipo, Boolean validarCPFCNPJ) {
        if (validarCPFCNPJ) {
            if (!Uteis.isAtributoPreenchido(assinatura)) {
                String mensagem = "ORDEM: " + documentoAssinadoPessoaVO.getOrdemAssinatura();
                documentoAssinadoVO.getListaErroImportaAssinatura().add(mensagem + " não foi encontrado a assinatura.");
                return;
            }
            switch (documentoAssinadoPessoaVO.getOrdemAssinatura()) {
                case 1:
                    if (!(Uteis.isAtributoPreenchido(documentoAssinadoPessoaVO.getPessoaVO().getCPF().trim()) && assinatura.contains(Uteis.removerMascara(documentoAssinadoPessoaVO.getPessoaVO().getCPF().trim())))) {
                        String mensagem = "Ordem de assinatura: 1 \n" + "CPF: " + Uteis.removerMascara(documentoAssinadoPessoaVO.getPessoaVO().getCPF()) + "\nO cpf da assinatura no xml não e compativel com o da ordem de assinatura." + "\n";
                        mensagem += "CPF no xml: " + tratarCpfCnpjAssinatura(assinatura, "CPF") + "\n";
                        mensagem += "CPF na ordem de assinatura: " + Uteis.removerMascara(documentoAssinadoPessoaVO.getPessoaVO().getCPF().trim());
                        documentoAssinadoVO.getListaErroImportaAssinatura().add(mensagem);
                    }
                    break;
                case 2:
                    if (!(Uteis.isAtributoPreenchido(documentoAssinadoPessoaVO.getPessoaVO().getCPF().trim()) && assinatura.contains(Uteis.removerMascara(documentoAssinadoPessoaVO.getPessoaVO().getCPF().trim())))) {
                        String mensagem = "Ordem de assinatura: 2 \n" + "CPF: " + Uteis.removerMascara(documentoAssinadoPessoaVO.getPessoaVO().getCPF()) + "\nO cpf da assinatura no xml não e compativel com o da ordem de assinatura." + "\n";
                        mensagem += "CPF no xml: " + tratarCpfCnpjAssinatura(assinatura, "CPF") + "\n";
                        mensagem += "CPF na ordem de assinatura: " + Uteis.removerMascara(documentoAssinadoPessoaVO.getPessoaVO().getCPF().trim());
                        documentoAssinadoVO.getListaErroImportaAssinatura().add(mensagem);
                    }
                    break;
                case 3:
                    if (!(Uteis.isAtributoPreenchido(documentoAssinadoVO.getUnidadeEnsinoVO().getCNPJ().trim()) && assinatura.contains(Uteis.removerMascara(documentoAssinadoVO.getUnidadeEnsinoVO().getCNPJ().trim())))) {
                        String mensagem = "Ordem de assinatura: 3 \n" + "CNPJ: " + Uteis.removerMascara(documentoAssinadoVO.getUnidadeEnsinoVO().getCNPJ().trim()) + "\nO cnpj da assinatura no xml não e compativel com o da ordem de assinatura." + "\n";
                        mensagem += "CNPJ no xml: " + tratarCpfCnpjAssinatura(assinatura, "CNPJ") + "\n";
                        mensagem += "CNPJ na ordem de assinatura: " + Uteis.removerMascara(documentoAssinadoVO.getUnidadeEnsinoVO().getCNPJ().trim());
                        documentoAssinadoVO.getListaErroImportaAssinatura().add(mensagem);
                    }
                    break;
                case 4:
                    if (!documentoAssinadoPessoaVO.getTipoPessoa().equals(TipoPessoa.MEMBRO_COMUNIDADE)) {
                        if (!(Uteis.isAtributoPreenchido(documentoAssinadoPessoaVO.getPessoaVO().getCPF().trim()) && assinatura.contains(Uteis.removerMascara(documentoAssinadoPessoaVO.getPessoaVO().getCPF().trim())))) {
                            String mensagem = "Ordem de assinatura: 4 \n" + "CPF-REGISTRADORA: " + Uteis.removerMascara(documentoAssinadoPessoaVO.getPessoaVO().getCPF().trim()) + "\nO cpf-registradora da assinatura no xml não e compativel com o da ordem de assinatura." + "\n";
                            mensagem += "CPF-REGISTRADORA no xml: " + tratarCpfCnpjAssinatura(assinatura, "CPF") + "\n";
                            mensagem += "CPF-REGISTRADORA na ordem de assinatura: " + Uteis.removerMascara(documentoAssinadoPessoaVO.getPessoaVO().getCPF().trim());
                            documentoAssinadoVO.getListaErroImportaAssinatura().add(mensagem);
                        }
                    }
                    break;
                case 5:
                    if (!documentoAssinadoPessoaVO.getTipoPessoa().equals(TipoPessoa.MEMBRO_COMUNIDADE)) {
                        if (!(Uteis.isAtributoPreenchido(documentoAssinadoVO.getUnidadeEnsinoVO().getCnpjUnidadeCertificadora().trim()) && assinatura.contains(Uteis.removerMascara(documentoAssinadoVO.getUnidadeEnsinoVO().getCnpjUnidadeCertificadora().trim())))) {
                            String mensagem = "Ordem de assinatura: 5 \n" + "CNPJ-REGISTRADORA: " + Uteis.removerMascara(documentoAssinadoVO.getUnidadeEnsinoVO().getCnpjUnidadeCertificadora().trim()) + "\nO cnpj-registradora da assinatura no xml não e compativel com o da ordem de assinatura." + "\n";
                            mensagem += "CNPJ no xml: " + tratarCpfCnpjAssinatura(assinatura, "CNPJ") + "\n";
                            mensagem += "CNPJ na ordem de assinatura: " + Uteis.removerMascara(documentoAssinadoVO.getUnidadeEnsinoVO().getCnpjUnidadeCertificadora().trim());
                            documentoAssinadoVO.getListaErroImportaAssinatura().add(mensagem);
                        }
                    }
                    break;
            }
        }
    }

    private String tratarCpfCnpjAssinatura(String assinatura, String tipo) {
        if (tipo.equals("CPF")) {
            String cpfCnpj = assinatura.substring(assinatura.indexOf(":") + 1).substring(0, 11);
            return cpfCnpj;
        } else {
            String cpfCnpj = assinatura.substring(assinatura.indexOf(":") + 1).substring(0, 14);
            return cpfCnpj;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
    public void verificarDadosLivroDiploma(DocumentoAssinadoVO documentoAssinadoVO, UsuarioVO usuarioVO, Document doc) throws Exception {
        Boolean teveAlteracao = false;
        String dadosRegistro = documentoAssinadoVO.getDecisaoJudicial() ? "DadosRegistroPorDecisaoJudicial" : "DadosRegistro";
//		ControleLivroRegistroDiplomaVO livroRegistroDiplomaVO = new ControleLivroRegistroDiplomaVO();
        Node nodePrincpal = null;
//        ExpedicaoDiplomaVO expedicaoDiplomaVO = getFacadeFactory().getExpedicaoDiplomaFacade().consultarPorMatricula(documentoAssinadoVO.getMatricula().getMatricula(), false, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
//        if (Uteis.isAtributoPreenchido(expedicaoDiplomaVO)) {
//            nodePrincpal = doc.getElementsByTagName(dadosRegistro).item(0);
//            NodeList nodePrincpalChildNodes = nodePrincpal.getChildNodes();
//            for (int i = 0; i < nodePrincpalChildNodes.getLength(); i++) {
//                Node node = nodePrincpalChildNodes.item(i);
//
//                if (node.getNodeType() == Node.ELEMENT_NODE && Objects.equals("Seguranca", node.getNodeName())) {
//                    NodeList node1ChildNodes = node.getChildNodes();
//                    for (int j = 0; j < node1ChildNodes.getLength(); j++) {
//                        Node node2 = node1ChildNodes.item(j);
//                        if (node2.getNodeType() == Node.ELEMENT_NODE && Objects.equals("CodigoValidacao", node2.getNodeName())) {
//                            if (Uteis.isAtributoPreenchido(node2.getTextContent())) {
//                                expedicaoDiplomaVO.setCodigoValidacaoDiplomaDigital(node2.getTextContent());
//                                teveAlteracao = true;
//                            }
//                        }
//                    }
//                    break;
//                } else if (node.getNodeType() == Node.ELEMENT_NODE && Objects.equals("LivroRegistro", node.getNodeName())) {
//                    NodeList node1ChildNodes = node.getChildNodes();
//                    for (int j = 0; j < node1ChildNodes.getLength(); j++) {
//                        Node node2 = node1ChildNodes.item(j);
//                        if (node2.getNodeType() == Node.ELEMENT_NODE && Objects.equals("LivroRegistro", node2.getNodeName())) {
//                            if (Uteis.isAtributoPreenchido(node2.getTextContent())) {
//                                expedicaoDiplomaVO.setInformarCamposLivroRegistradora(true);
//                                expedicaoDiplomaVO.setLivroRegistradora(node2.getTextContent());
//                                teveAlteracao = true;
//                            }
//                        } else if (node2.getNodeType() == Node.ELEMENT_NODE && Objects.equals("NumeroFolhaDoDiploma", node2.getNodeName())) {
//                            if (Uteis.isAtributoPreenchido(node2.getTextContent())) {
//                                expedicaoDiplomaVO.setInformarCamposLivroRegistradora(true);
//                                expedicaoDiplomaVO.setFolhaReciboRegistradora(node2.getTextContent());
//                                teveAlteracao = true;
//                            }
//                        } else if (node2.getNodeType() == Node.ELEMENT_NODE && Objects.equals("NumeroRegistro", node2.getNodeName())) {
//                            if (Uteis.isAtributoPreenchido(node2.getTextContent())) {
//                                expedicaoDiplomaVO.setInformarCamposLivroRegistradora(true);
//                                expedicaoDiplomaVO.setNumeroRegistroDiploma(node2.getTextContent());
//                                teveAlteracao = true;
//                            }
//                        } else if (node2.getNodeType() == Node.ELEMENT_NODE && Objects.equals("ProcessoDoDiploma", node2.getNodeName())) {
//                            if (Uteis.isAtributoPreenchido(node2.getTextContent())) {
//                                expedicaoDiplomaVO.setInformarCamposLivroRegistradora(true);
//                                expedicaoDiplomaVO.setNumeroProcesso(node2.getTextContent());
//                                teveAlteracao = true;
//                            }
//                        } else if (node2.getNodeType() == Node.ELEMENT_NODE && Objects.equals("DataExpedicaoDiploma", node2.getNodeName())) {
//                            if (Uteis.isAtributoPreenchido(node2.getTextContent())) {
//                                expedicaoDiplomaVO.setInformarCamposLivroRegistradora(true);
//                                expedicaoDiplomaVO.setDataExpedicao(Uteis.getData(node2.getTextContent(), "yyyy-MM-dd"));
//                                teveAlteracao = true;
//                            }
//                        } else if (node2.getNodeType() == Node.ELEMENT_NODE && Objects.equals("DataRegistroDiploma", node2.getNodeName())) {
//                            if (Uteis.isAtributoPreenchido(node2.getTextContent())) {
//                                expedicaoDiplomaVO.setInformarCamposLivroRegistradora(true);
//                                expedicaoDiplomaVO.setDataRegistroDiploma(Uteis.getData(node2.getTextContent(), "yyyy-MM-dd"));
//                                teveAlteracao = true;
//                            }
//                        }
//                    }
//                }
//            }
//            if (teveAlteracao) {
//                if (Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getCodigo())) {
//                    getFacadeFactory().getExpedicaoDiplomaFacade().alterar(expedicaoDiplomaVO, usuarioVO, false);
//                }
//            }
//        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
    public void realizarGeracaoCodigoValidacaoDiplomaDigital(DocumentoAssinadoVO documentoAssinadoVO, UsuarioVO usuarioVO, Document doc, String localArquivo, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
//        try {
//            Node nodePrincpal = null;
//            ExpedicaoDiplomaVO expedicaoDiplomaVO = getFacadeFactory().getExpedicaoDiplomaFacade().consultarPorMatricula(documentoAssinadoVO.getMatricula().getMatricula(), false, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
//            if (Uteis.isAtributoPreenchido(expedicaoDiplomaVO) && !Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getCodigoValidacaoDiplomaDigital())) {
//                expedicaoDiplomaVO.setMatricula(getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(expedicaoDiplomaVO.getMatricula().getMatricula(), expedicaoDiplomaVO.getMatricula().getUnidadeEnsino().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO));
//                getFacadeFactory().getUnidadeEnsinoFacade().carregarDados(expedicaoDiplomaVO.getUnidadeEnsinoCertificadora(), NivelMontarDados.TODOS, usuarioVO);
//                if (!Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getCodigoValidacaoDiplomaDigital())) {
//                    String hashCodigoValidacao = getFacadeFactory().getExpedicaoDiplomaFacade().realizarGeracaoHashCodigoValidacao(expedicaoDiplomaVO, expedicaoDiplomaVO.getLivroRegistradora(), expedicaoDiplomaVO.getFolhaReciboRegistradora(), expedicaoDiplomaVO.getNumeroRegistroDiploma(), usuarioVO);
//                    nodePrincpal = doc.getElementsByTagName("DadosRegistro").item(0);
//                    NodeList nodePrincpalChildNodes2 = nodePrincpal.getChildNodes();
//                    for (int i = 0; i < nodePrincpalChildNodes2.getLength(); i++) {
//                        Node node = nodePrincpalChildNodes2.item(i);
//                        if (node.getNodeType() == Node.ELEMENT_NODE && Objects.equals("Seguranca", node.getNodeName())) {
//                            NodeList node1ChildNodes = node.getChildNodes();
//                            for (int j = 0; j < node1ChildNodes.getLength(); j++) {
//                                Node node2 = node1ChildNodes.item(j);
//                                if (node2.getNodeType() == Node.ELEMENT_NODE && Objects.equals("CodigoValidacao", node2.getNodeName())) {
//                                    expedicaoDiplomaVO.setCodigoValidacaoDiplomaDigital(hashCodigoValidacao);
//                                    getFacadeFactory().getExpedicaoDiplomaFacade().realizarGeracaoRepresentacaoVisualDiplomaDigital(expedicaoDiplomaVO);
//                                    node2.setTextContent(hashCodigoValidacao);
//                                    TransformerFactory transformerFactory = TransformerFactory.newInstance();
//                                    Transformer transformer = transformerFactory.newTransformer();
//                                    DOMSource source = new DOMSource(doc);
//                                    StreamResult result = new StreamResult(new File(localArquivo).getAbsolutePath());
//                                    transformer.transform(source, result);
//                                    getFacadeFactory().getExpedicaoDiplomaFacade().alterar(expedicaoDiplomaVO, usuarioVO, false);
//                                }
//                            }
//                        }
//                    }
//                }
//            } else if (Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getCodigoValidacaoDiplomaDigital())) {
//                getFacadeFactory().getExpedicaoDiplomaFacade().realizarGeracaoRepresentacaoVisualDiplomaDigital(expedicaoDiplomaVO);
//            }
//        } catch (Exception e) {
//            throw e;
//        }
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
    public void realizarLeituraDiplomaDigital(DocumentoAssinadoVO documentoAssinadoVO, ConfiguracaoGeralSistemaVO config, InputStream inputStream, UsuarioVO usuarioVO) throws Exception {
        Integer quantidadeRegistrosAlterados = 0;
        Boolean decisaoJudicial = documentoAssinadoVO.getDecisaoJudicial();
        Map<Integer, String> mapAssinaturasExistentes = new TreeMap<Integer, String>();
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = null;
        ByteArrayOutputStream baos = null;
        String uploadedFileLocation = null;
        File fileDiretorioCorreto = null;
        File fileDiretorioTMP = null;
        try {
            docFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            docBuilder = docFactory.newDocumentBuilder();
            baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) > 0) {
                baos.write(buffer, 0, len);
            }
            baos.flush();
            Document doc = docBuilder.parse(new ByteArrayInputStream(baos.toByteArray()));
            doc.getDocumentElement().normalize();
            Ordenacao.ordenarLista(documentoAssinadoVO.getListaDocumentoAssinadoPessoa(), "ordemAssinatura");
            validarDadosDiplomaXML(doc, Uteis.removerMascara(documentoAssinadoVO.getPessoaCpf()), documentoAssinadoVO);
            String dadosDiploma = decisaoJudicial ? "DadosDiplomaPorDecisaoJudicial" : "DadosDiploma";
            String dadosRegistro = decisaoJudicial ? "DadosRegistroPorDecisaoJudicial" : "DadosRegistro";
            if (documentoAssinadoVO.getListaDocumentoAssinadoPessoa().size() == 5) {
                obterAssinaturasDiplomaXML(doc, dadosDiploma, documentoAssinadoVO.getListaDocumentoAssinadoPessoa().size(), mapAssinaturasExistentes);
                obterAssinaturasDiplomaXML(doc, dadosRegistro, documentoAssinadoVO.getListaDocumentoAssinadoPessoa().size(), mapAssinaturasExistentes);
                obterAssinaturasDiplomaXML(doc, "Diploma", documentoAssinadoVO.getListaDocumentoAssinadoPessoa().size(), mapAssinaturasExistentes);
                for (int i = 0; i < documentoAssinadoVO.getListaDocumentoAssinadoPessoa().size(); i++) {
                    DocumentoAssinadoPessoaVO documentoAssinadoPessoaVO = documentoAssinadoVO.getListaDocumentoAssinadoPessoa().get(i);
                    if (documentoAssinadoPessoaVO.getSituacaoDocumentoAssinadoPessoaEnum().equals(SituacaoDocumentoAssinadoPessoaEnum.PENDENTE)) {
                        String assinatura = mapAssinaturasExistentes.get(i + 1);
                        switch (documentoAssinadoPessoaVO.getOrdemAssinatura()) {

						case 1:
							quantidadeRegistrosAlterados += validarAssinaturaDocumentoAssinado(assinatura, documentoAssinadoPessoaVO, usuarioVO, config, baos, documentoAssinadoVO, "CPF", true);
							break;
						case 2:
							quantidadeRegistrosAlterados += validarAssinaturaDocumentoAssinado(assinatura, documentoAssinadoPessoaVO, usuarioVO, config, baos, documentoAssinadoVO, "CPF", true);
							break;
						case 4:
							quantidadeRegistrosAlterados += validarAssinaturaDocumentoAssinado(assinatura, documentoAssinadoPessoaVO, usuarioVO, config, baos, documentoAssinadoVO, "CPF", true);
							break;

						case 3:
							quantidadeRegistrosAlterados += validarAssinaturaDocumentoAssinado(assinatura, documentoAssinadoPessoaVO, usuarioVO, config, baos, documentoAssinadoVO, "CNPJ", true);
							break;

						case 5:
							quantidadeRegistrosAlterados += validarAssinaturaDocumentoAssinado(assinatura, documentoAssinadoPessoaVO, usuarioVO, config, baos, documentoAssinadoVO, "CNPJ_CERTIFICADORA", true);
							break;

						default:
							break;
						}
					}
				}
			} else {
				obterAssinaturasDiplomaXML(doc, dadosDiploma, documentoAssinadoVO.getListaDocumentoAssinadoPessoa().size(), mapAssinaturasExistentes);
				obterAssinaturasDiplomaXML(doc, dadosRegistro, documentoAssinadoVO.getListaDocumentoAssinadoPessoa().size(), mapAssinaturasExistentes);
				obterAssinaturasDiplomaXML(doc, "Diploma", documentoAssinadoVO.getListaDocumentoAssinadoPessoa().size(), mapAssinaturasExistentes);
				for (int i = 0; i < documentoAssinadoVO.getListaDocumentoAssinadoPessoa().size(); i++) {
					DocumentoAssinadoPessoaVO documentoAssinadoPessoaVO = documentoAssinadoVO.getListaDocumentoAssinadoPessoa().get(i);
					if (documentoAssinadoPessoaVO.getSituacaoDocumentoAssinadoPessoaEnum().equals(SituacaoDocumentoAssinadoPessoaEnum.PENDENTE)) {
						String assinatura = mapAssinaturasExistentes.get(i + 1);
						switch (documentoAssinadoPessoaVO.getOrdemAssinatura()) {

						case 1:
							quantidadeRegistrosAlterados += validarAssinaturaDocumentoAssinado(assinatura, documentoAssinadoPessoaVO, usuarioVO, config, baos, documentoAssinadoVO, "CPF", true);
							break;
						case 4:
							quantidadeRegistrosAlterados += validarAssinaturaDocumentoAssinado(assinatura, documentoAssinadoPessoaVO, usuarioVO, config, baos, documentoAssinadoVO, "CPF", true);
							break;

						case 3:
							quantidadeRegistrosAlterados += validarAssinaturaDocumentoAssinado(assinatura, documentoAssinadoPessoaVO, usuarioVO, config, baos, documentoAssinadoVO, "CNPJ", true);
							break;

						case 5:
							quantidadeRegistrosAlterados += validarAssinaturaDocumentoAssinado(assinatura, documentoAssinadoPessoaVO, usuarioVO, config, baos, documentoAssinadoVO, "CNPJ_CERTIFICADORA", true);
							break;

						default:
							break;
						}
					}
				}
			}
			verificarDadosLivroDiploma(documentoAssinadoVO, usuarioVO, doc);
			uploadedFileLocation = config.getLocalUploadArquivoFixo() + File.separator + documentoAssinadoVO.getArquivo().getPastaBaseArquivo() + File.separator + documentoAssinadoVO.getArquivo().getNome();
			if (quantidadeRegistrosAlterados > 0) {
				getFacadeFactory().getArquivoHelper().escreverArquivo(new ByteArrayInputStream(baos.toByteArray()), uploadedFileLocation);
				if (documentoAssinadoVO.getArquivo().getServidorArquivoOnline().equals(ServidorArquivoOnlineEnum.AMAZON_S3)) {
					if (Uteis.isAtributoPreenchido(documentoAssinadoVO.getArquivo())) {
						fileDiretorioCorreto = new File(config.getLocalUploadArquivoFixo() + File.separator + documentoAssinadoVO.getArquivo().getPastaBaseArquivo() + File.separator + documentoAssinadoVO.getArquivo().getNome());
						fileDiretorioTMP = new File(config.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.DOCUMENTOS_ASSINADOS_TMP.getValue() + File.separator + documentoAssinadoVO.getArquivo().getNome());
						if (!fileDiretorioCorreto.exists()) {
							getFacadeFactory().getArquivoHelper().copiar(fileDiretorioTMP, fileDiretorioCorreto);
						}
						realizarUploadArquivoAmazon(documentoAssinadoVO.getArquivo(), config, true);
					}

				}
			}
			realizarGeracaoCodigoValidacaoDiplomaDigital(documentoAssinadoVO, usuarioVO, doc, uploadedFileLocation, config);
		} catch (Exception e) {
			throw e;
		} finally {
			quantidadeRegistrosAlterados = null;
			decisaoJudicial = null;
			mapAssinaturasExistentes = null;
			docFactory = null;
			docBuilder = null;
			if (baos != null) {
				baos.close();
				baos = null;
			}
			uploadedFileLocation = null;
			if (fileDiretorioCorreto != null) {
				if (fileDiretorioCorreto.exists() && documentoAssinadoVO.getArquivo().getServidorArquivoOnline().equals(ServidorArquivoOnlineEnum.AMAZON_S3)) {
					fileDiretorioCorreto.delete();
				}
				fileDiretorioCorreto = null;
			}
			if (fileDiretorioTMP != null) {
				if (fileDiretorioTMP.exists()) {
					fileDiretorioTMP.delete();
				}
				fileDiretorioTMP = null;
			}
		}
	}
	
	public Boolean validarAssinaturaDiplomaDigital(Integer indice, String assinatura, Integer quantidadeAssinaturasVerificar) {
		if (quantidadeAssinaturasVerificar == 5) {
			if (((indice == 1 || indice == 2 || indice == 4) && (assinatura.toUpperCase().contains("CPF") || assinatura.toUpperCase().contains("CERTIFICADO PF A3"))) || ((indice == 3 || indice == 5) && (assinatura.toUpperCase().contains("CNPJ") || assinatura.toUpperCase().contains("CERTIFICADO PJ A3")))) {
				return true;
			}
		} else {
			if (((indice == 1 || indice == 3) && (assinatura.toUpperCase().contains("CPF") || assinatura.toUpperCase().contains("CERTIFICADO PF A3"))) || ((indice == 2 || indice == 4) && (assinatura.toUpperCase().contains("CNPJ") || assinatura.toUpperCase().contains("CERTIFICADO PJ A3")))) {
				return true;
			}
		}
		return false;
	}
	
	public void obterAssinaturasDiplomaXML(Document doc, String elementVerificacao, Integer quantidadeAssinaturasVerificar, Map<Integer, String> mapAssinaturasExistentes) {
		
	    Node nodePrincpal = doc.getElementsByTagName(elementVerificacao).item(0);
	    NodeList nodePrincpalChildNodes = nodePrincpal.getChildNodes();
	    x:
		for (int i = 0; i < nodePrincpalChildNodes.getLength(); i++) {
    		Node node = nodePrincpalChildNodes.item(i);
	        if (node.getNodeType() == Node.ELEMENT_NODE && (Objects.equals("ds:Signature", node.getNodeName()) || Objects.equals("Signature", node.getNodeName()))) {
	        	NodeList node1ChildNodes = node.getChildNodes();
	        	for (int j = 0; j < node1ChildNodes.getLength(); j++) {
	        		Node node2 = node1ChildNodes.item(j);
	        		if (node2.getNodeType() == Node.ELEMENT_NODE && (Objects.equals("ds:KeyInfo", node2.getNodeName()) || Objects.equals("KeyInfo", node2.getNodeName()))) {
	        			NodeList node2ChildNodes = node2.getChildNodes();
	        			for (int x = 0; x < node2ChildNodes.getLength(); x++) {
	        				Node node3 = node2ChildNodes.item(x);
	        				if (node3.getNodeType() == Node.ELEMENT_NODE && (Objects.equals("ds:X509Data", node3.getNodeName())) || Objects.equals("X509Data", node3.getNodeName())) {
	        					NodeList node3ChildNodes = node3.getChildNodes();
	        					for (int y = 0; y < node3ChildNodes.getLength(); y++) {
	        						Node node4 = node3ChildNodes.item(y);
	        						if (node4.getNodeType() == Node.ELEMENT_NODE && (Objects.equals("ds:X509SubjectName", node4.getNodeName()) || Objects.equals("X509SubjectName", node4.getNodeName()))) {
	        							Integer indice = 1;
	        							if (mapAssinaturasExistentes.size() > 0) {
	        								indice = ((TreeMap<Integer, String>) mapAssinaturasExistentes).lastEntry().getKey() + 1;
	        							}
	        							if (validarAssinaturaDiplomaDigital(indice, node4.getTextContent(), quantidadeAssinaturasVerificar)) {
	        								mapAssinaturasExistentes.put(indice, node4.getTextContent());
	        								continue x;
	        							}
	        						}
	        					}
	        				}
	        			}
	        		}
	        	}
	        }
		}
	}
	
	public DocumentoAssinadoVO consultarDocumentoAssinadoPartirXmlDiploma(String codigoAlunoDiplomado, String nomeCursoDiplomado, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder(getSqlConsultaDadosBasico());
		sql.append("INNER JOIN curso cursoMatricula ON cursoMatricula.codigo = matricula.curso AND (cursoMatricula.nome = ? OR cursoMatricula.nomedocumentacao = ?) ");
		sql.append("LEFT JOIN LATERAL (SELECT count(documentoassinadopessoa.codigo) AS qtdDocumentoPessoa FROM documentoassinadopessoa WHERE documentoassinadopessoa.documentoassinado = documentoassinado.codigo) AS totalDocumentoPessoa ON TRUE ");
		sql.append("LEFT JOIN LATERAL ( SELECT count(documentoassinadopessoa.codigo) AS qtdDocumentoPessoaPendente FROM documentoassinadopessoa WHERE documentoassinadopessoa.documentoassinado = documentoassinado.codigo AND documentoassinadopessoa.situacaodocumentoassinadopessoa IN ('PENDENTE')) AS totalDocumentoPessoaPendente ON TRUE ");
		sql.append("LEFT JOIN LATERAL ( SELECT count(documentoassinadopessoa.codigo) AS qtdDocumentoPessoaAssinado FROM documentoassinadopessoa WHERE documentoassinadopessoa.documentoassinado = documentoassinado.codigo AND documentoassinadopessoa.situacaodocumentoassinadopessoa IN ('ASSINADO')) AS totalDocumentoPessoaAssinado ON TRUE ");
		sql.append("LEFT JOIN LATERAL ( SELECT count(documentoassinadopessoa.codigo) AS qtdDocumentoPessoaRejeitado FROM documentoassinadopessoa WHERE documentoassinadopessoa.documentoassinado = documentoassinado.codigo AND documentoassinadopessoa.situacaodocumentoassinadopessoa IN ('REJEITADO')) AS totalDocumentoPessoaRejeitado ON TRUE ");
		sql.append("WHERE documentoassinado.tipoorigemdocumentoassinado = 'DIPLOMA_DIGITAL' AND totalDocumentoPessoaRejeitado.qtdDocumentoPessoaRejeitado <= 0 AND totalDocumentoPessoaAssinado.qtdDocumentoPessoaAssinado NOT IN (totalDocumentoPessoa.qtdDocumentoPessoa) AND ((totalDocumentoPessoaAssinado.qtdDocumentoPessoaAssinado + totalDocumentoPessoaPendente.qtdDocumentoPessoaPendente) = totalDocumentoPessoa.qtdDocumentoPessoa) AND pessoa.codigo = ? ");
		sql.append("ORDER BY documentoassinado.codigo DESC LIMIT 1");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), nomeCursoDiplomado, nomeCursoDiplomado, Integer.valueOf(codigoAlunoDiplomado));
		if (!tabelaResultado.next()) {
			throw new Exception("Não foi encontrado um DIPLOMA DIGITAL que esteja pendente para o aluno de código:" + codigoAlunoDiplomado + " e curso: " + nomeCursoDiplomado);
		}
		return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
	}
	
	@Override
	public Map<List<String>, List<String>> realizarImportacaoXmlDiplomaRegistradoraLote(ZipFile arquivoZizado, ProgressBarVO progressBarVO) throws Exception {
		Map<List<String>, List<String>> map = new HashMap<>(0);
		List<String> listaMensagem = new ArrayList<>(0);
		List<String> listaErro = new ArrayList<>(0);
		if (arquivoZizado != null) {
			Enumeration<? extends ZipEntry> entries = arquivoZizado.entries();
			int x = 1;
			progressBarVO.setStatus("Iniciando processo de importação");
			while(entries.hasMoreElements()){
				InputStream stream = null;
				InputStream is = null;
				DocumentBuilderFactory docFactory = null;
				DocumentBuilder docBuilder = null;
				ByteArrayOutputStream baos = null;
				byte[] buffer = null;
				Document doc = null;
				try {
					ZipEntry entry = entries.nextElement();
					progressBarVO.setStatus("Processando arquivo " + x + " (" + entry.getName() + ")");
					if (!entry.getName().contains(".xml")) {
						throw new Exception("O arquivo (" + entry.getName() + ") não é formato de XML.");
					}
					stream = arquivoZizado.getInputStream(entry);
					is = arquivoZizado.getInputStream(entry);
					docFactory = DocumentBuilderFactory.newInstance();
					docFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
					docBuilder = docFactory.newDocumentBuilder();
					baos = new ByteArrayOutputStream();
					buffer = new byte[1024];
					int len;
					while ((len = stream.read(buffer)) > 0) {
						baos.write(buffer, 0, len);
					}
					baos.flush();
					doc = docBuilder.parse(new ByteArrayInputStream(baos.toByteArray()));
					doc.getDocumentElement().normalize();
					String idDiplomado = capturarIdDiplomadoXmlDiploma(doc);
					if (!Uteis.isAtributoPreenchido(idDiplomado)) {
						throw new Exception("Não foi possivel localizar o id do diplomado no xml (" + entry.getName() + ")");
					}
					String nomeCursoDiplomado = capturarCursoXmlDiploma(doc);
					if (!Uteis.isAtributoPreenchido(nomeCursoDiplomado)) {
						throw new Exception("Não foi possivel localizar o curso do diplomado no xml (" + entry.getName() + ")");
					}
					DocumentoAssinadoVO documentoAssinadoVO = consultarDocumentoAssinadoPartirXmlDiploma(idDiplomado, nomeCursoDiplomado, progressBarVO.getUsuarioVO());
					if (Uteis.isAtributoPreenchido(documentoAssinadoVO)) {
						realizarLeituraDiplomaDigital(documentoAssinadoVO, progressBarVO.getConfiguracaoGeralSistemaVO(), is, progressBarVO.getUsuarioVO());
					} else {
						throw new Exception("Xml (" + entry.getName() + ") " + "Não foi encontrado um documento assinado apto para ser importado o xml.");
					}
					if (Uteis.isAtributoPreenchido(documentoAssinadoVO.getListaErroImportaAssinatura())) {
						documentoAssinadoVO.getListaErroImportaAssinatura().stream().forEach(e -> listaErro.add("Xml (" + entry.getName() + ") " + e.toString()));
					} else {
						listaMensagem.add("O xml (" + entry.getName() + ") foi importado para o documento assinado de código (" + documentoAssinadoVO.getCodigo() + ") e aluno de matrícula (" + documentoAssinadoVO.getMatricula().getMatricula() + ") com sucesso.");
					}
				} catch (Exception e) {
					listaErro.add(e.getMessage());
				} finally {
					if (stream != null) {
						stream.close();
						stream = null;
					}
					if (is != null) {
						is.close();
						is = null;
					}
					if (docFactory != null) {
						docFactory = null;
					}
					if (docBuilder != null) {
						docBuilder = null;
					}
					if (baos != null) {
						baos.close();
						baos = null;
					}
					if (buffer != null) {
						buffer = null;
					}
					if (doc != null) {
						doc = null;
					}
				}
				x++;
			}
		}
		map.put(listaMensagem, listaErro);
		return map;
	}
	
	private String capturarIdDiplomadoXmlDiploma(Document doc) {
		try {
			String dadosDiploma = "DadosDiploma";
			Node nodePrincpal = null;
			nodePrincpal = doc.getElementsByTagName(dadosDiploma).item(0);
			NodeList nodePrincpalChildNodes = nodePrincpal.getChildNodes();
			for (int i = 0; i < nodePrincpalChildNodes.getLength(); i++) {
				Node node = nodePrincpalChildNodes.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE && Objects.equals("Diplomado", node.getNodeName())) {
					NodeList node1ChildNodes = node.getChildNodes();
					for (int j = 0; j < node1ChildNodes.getLength(); j++) {
						Node node2 = node1ChildNodes.item(j);
						if (node2.getNodeType() == Node.ELEMENT_NODE && Objects.equals("ID", node2.getNodeName())) {
							return node2.getTextContent();
						}
					}
				}
			}
		} catch (Exception e) {
			String dadosDiplomaJudicial = "DadosDiplomaPorDecisaoJudicial";
			try {
				Node nodePrincpal = null;
				nodePrincpal = doc.getElementsByTagName(dadosDiplomaJudicial).item(0);
				NodeList nodePrincpalChildNodes = nodePrincpal.getChildNodes();
				for (int i = 0; i < nodePrincpalChildNodes.getLength(); i++) {
					Node node = nodePrincpalChildNodes.item(i);
					if (node.getNodeType() == Node.ELEMENT_NODE && Objects.equals("Diplomado", node.getNodeName())) {
						NodeList node1ChildNodes = node.getChildNodes();
						for (int j = 0; j < node1ChildNodes.getLength(); j++) {
							Node node2 = node1ChildNodes.item(j);
							if (node2.getNodeType() == Node.ELEMENT_NODE && Objects.equals("ID", node2.getNodeName())) {
								return node2.getTextContent();
							}
						}
					}
				}
			} catch (Exception e2) {
				throw e2;
			}
		}
		return Constantes.EMPTY;
	}
	
	private String capturarCursoXmlDiploma(Document doc) {
		try {
			String dadosDiploma = "DadosDiploma";
			Node nodePrincpal = null;
			nodePrincpal = doc.getElementsByTagName(dadosDiploma).item(0);
			NodeList nodePrincpalChildNodes = nodePrincpal.getChildNodes();
			for (int i = 0; i < nodePrincpalChildNodes.getLength(); i++) {
				Node node = nodePrincpalChildNodes.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE && Objects.equals("DadosCurso", node.getNodeName())) {
					NodeList node1ChildNodes = node.getChildNodes();
					for (int j = 0; j < node1ChildNodes.getLength(); j++) {
						Node node2 = node1ChildNodes.item(j);
						if (node2.getNodeType() == Node.ELEMENT_NODE && Objects.equals("NomeCurso", node2.getNodeName())) {
							return node2.getTextContent();
						}
					}
				}
			}
		} catch (Exception e) {
			String dadosDiplomaJudicial = "DadosDiplomaPorDecisaoJudicial";
			try {
				Node nodePrincpal = null;
				nodePrincpal = doc.getElementsByTagName(dadosDiplomaJudicial).item(0);
				NodeList nodePrincpalChildNodes = nodePrincpal.getChildNodes();
				for (int i = 0; i < nodePrincpalChildNodes.getLength(); i++) {
					Node node = nodePrincpalChildNodes.item(i);
					if (node.getNodeType() == Node.ELEMENT_NODE && Objects.equals("DadosCurso", node.getNodeName())) {
						NodeList node1ChildNodes = node.getChildNodes();
						for (int j = 0; j < node1ChildNodes.getLength(); j++) {
							Node node2 = node1ChildNodes.item(j);
							if (node2.getNodeType() == Node.ELEMENT_NODE && Objects.equals("NomeCurso", node2.getNodeName())) {
								return node2.getTextContent();
							}
						}
					}
				}
			} catch (Exception e2) {
				throw e2;
			}
		}
		return Constantes.EMPTY;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void alterarArquivoVisualDocumentoAssinado(final DocumentoAssinadoVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		try {
			alterar(getIdEntidade(), verificarAcesso, usuario);
			final String sql = "UPDATE DocumentoAssinado set arquivoVisual = ? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					if (Uteis.isAtributoPreenchido(obj.getArquivoVisual().getCodigo())) {
						sqlAlterar.setInt(1, obj.getArquivoVisual().getCodigo());
					} else {
						sqlAlterar.setNull(1, 0);
					}
					sqlAlterar.setInt(2, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			}) == 0) {
				return;
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void realizarUploadArquivoAmazon(ArquivoVO arquivo, ConfiguracaoGeralSistemaVO config, Boolean deletarAqruivoExistente) throws Exception {
//		ServidorArquivoOnlineS3RS servidorExternoAmazon = new ServidorArquivoOnlineS3RS(config.getUsuarioAutenticacao(), config.getSenhaAutenticacao(), config.getNomeRepositorio());
//		getFacadeFactory().getArquivoFacade().realizarUploadServidorAmazon(servidorExternoAmazon, arquivo, config, deletarAqruivoExistente);
	}
	
	@Override
	public List<DocumentoAssinadoVO> consultarDocumentosDigitaisDiploma(ExpedicaoDiplomaVO expedicaoDiplomaVO, List<TipoOrigemDocumentoAssinadoEnum> tipoOrigemDocumentoAssinadoEnums, Boolean documentoAssinadoValido) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT codigo, matricula ");
		sql.append("FROM documentoassinado WHERE ");
		sql.append("matricula = ? ");
		if (documentoAssinadoValido) {
			sql.append("AND documentoassinadoinvalido IS FALSE ");
		} else {
			sql.append("AND documentoassinadoinvalido IS TRUE ");
		}
		if (Uteis.isAtributoPreenchido(tipoOrigemDocumentoAssinadoEnums)) {
			sql.append(tipoOrigemDocumentoAssinadoEnums.stream().map(t -> t.name()).collect(Collectors.joining("', '", "AND tipoorigemdocumentoassinado in ('", "') ")));
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), expedicaoDiplomaVO.getMatricula().getMatricula());
		List<DocumentoAssinadoVO> documentoAssinadoVOs = new ArrayList<>(0);
		while (tabelaResultado.next()) {
			DocumentoAssinadoVO documentoAssinadoVO = new DocumentoAssinadoVO();
			documentoAssinadoVO.setCodigo(tabelaResultado.getInt("codigo"));
			documentoAssinadoVO.getMatricula().setMatricula(tabelaResultado.getString("matricula"));
			documentoAssinadoVOs.add(documentoAssinadoVO);
		}
		return documentoAssinadoVOs;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void realizarAssinaturaCurriculoEscolar(File arquivoXML, File arquivoVisual, ConfiguracaoGeralSistemaVO config, TipoOrigemDocumentoAssinadoEnum tipoOrigemDocumentoAssinadoEnum, UsuarioVO usuarioVO, Boolean persistirDocumentoAssinado) throws Exception {
		DocumentoAssinadoVO obj = new DocumentoAssinadoVO();
		obj.setDataRegistro(new Date());
		obj.setUsuario(usuarioVO);
		obj.setTipoOrigemDocumentoAssinadoEnum(tipoOrigemDocumentoAssinadoEnum);
//		obj.setUnidadeEnsinoVO(gestaoXmlGradeCurricularVO.getUnidadeEnsinoVO());
//		obj.setCursoVO(gestaoXmlGradeCurricularVO.getCursoVO());
//		obj.setGradecurricular(gestaoXmlGradeCurricularVO.getGradeCurricularVO());
//		obj.setCodigoValidacaoCurriculoEscolarDigital(gestaoXmlGradeCurricularVO.getCodigoValidacao());
		obj.setDecisaoJudicial(Boolean.FALSE);
		obj.setVersaoDiploma(VersaoDiplomaDigitalEnum.VERSAO_1_05);
//		obj.setArquivo(criarArquivoCurriculoEscolar(gestaoXmlGradeCurricularVO, arquivoXML, tipoOrigemDocumentoAssinadoEnum, config, usuarioVO));
//		obj.setArquivoVisual(criarArquivoCurriculoEscolar(gestaoXmlGradeCurricularVO, arquivoVisual, tipoOrigemDocumentoAssinadoEnum, config, usuarioVO));
//		obj.getListaDocumentoAssinadoPessoa().add(new DocumentoAssinadoPessoaVO(new Date(), gestaoXmlGradeCurricularVO.getFuncionarioPrimario().getPessoa(), TipoPessoa.FUNCIONARIO, SituacaoDocumentoAssinadoPessoaEnum.PENDENTE, obj, 1, gestaoXmlGradeCurricularVO.getCargoPrimario().getDescricao(), gestaoXmlGradeCurricularVO.getTituloFuncionarioPrimario(), null, TipoAssinaturaDocumentoEnum.NENHUM));
//		DocumentoAssinadoPessoaVO documentoAssinadoPessoa = new DocumentoAssinadoPessoaVO(new Date(), gestaoXmlGradeCurricularVO.getFuncionarioSecundario().getPessoa(), TipoPessoa.FUNCIONARIO, SituacaoDocumentoAssinadoPessoaEnum.PENDENTE, obj, 3, gestaoXmlGradeCurricularVO.getCargoSecundario().getDescricao(), gestaoXmlGradeCurricularVO.getTituloFuncionarioSecundario(), null, TipoAssinaturaDocumentoEnum.NENHUM);
//		documentoAssinadoPessoa.setAssinarPorCNPJ(true);
//		obj.getListaDocumentoAssinadoPessoa().add(documentoAssinadoPessoa);
		incluir(obj, false, usuarioVO, config);
		File arquivoDiretorioXml = new File(config.getLocalUploadArquivoFixo() + File.separator + obj.getArquivo().getPastaBaseArquivo() + File.separator +obj.getArquivo().getNome());
		getFacadeFactory().getArquivoHelper().copiar(arquivoXML, arquivoDiretorioXml);
		if (obj.getArquivo().getServidorArquivoOnline().equals(ServidorArquivoOnlineEnum.AMAZON_S3)) {
			realizarUploadArquivoAmazon(obj.getArquivo(), config, true);
		}
		File arquivoDiretorioRepresentacaoVisual = new File(config.getLocalUploadArquivoFixo() + File.separator + obj.getArquivoVisual().getPastaBaseArquivo() + File.separator +obj.getArquivoVisual().getNome());
		getFacadeFactory().getArquivoHelper().copiar(arquivoVisual, arquivoDiretorioRepresentacaoVisual);
		if (obj.getArquivoVisual().getServidorArquivoOnline().equals(ServidorArquivoOnlineEnum.AMAZON_S3)) {
			realizarUploadArquivoAmazon(obj.getArquivoVisual(), config, true);
		}
//		gestaoXmlGradeCurricularVO.setDocumentoAssinadoVO(obj);
//		gestaoXmlGradeCurricularVO.setSituacaoDocumento("Gerado com Pendência de Assinatura");
	}
	
	private ArquivoVO criarArquivoCurriculoEscolar( File arquivo, TipoOrigemDocumentoAssinadoEnum tipoOrigemDocumentoAssinadoEnum, ConfiguracaoGeralSistemaVO config, UsuarioVO usuarioVO) throws IOException {
		ArquivoVO obj = new ArquivoVO();
		obj.setDataUpload(new Date());
		obj.setNome(arquivo.getName());
		obj.setControlarDownload(true);
		obj.setManterDisponibilizacao(true);
		obj.setDataIndisponibilizacao(null);
		obj.setResponsavelUpload(usuarioVO);
		obj.setDataDisponibilizacao(new Date());
		obj.setArquivoAssinadoDigitalmente(true);
		obj.setSituacao(SituacaoArquivo.ATIVO.getValor());
//		obj.setCurso(gestaoXmlGradeCurricularVO.getCursoVO());
		obj.setExtensao(FilenameUtils.getExtension(arquivo.getName()));
//		obj.setUnidadeEnsinoVO(gestaoXmlGradeCurricularVO.getUnidadeEnsinoVO());
		obj.setPastaBaseArquivoEnum(PastaBaseArquivoEnum.DOCUMENTOS_ASSINADOS_TMP);
		obj.setPastaBaseArquivo(PastaBaseArquivoEnum.DOCUMENTOS_ASSINADOS_TMP.getValue());
		obj.setServidorArquivoOnline(ServidorArquivoOnlineEnum.getEnum(config.getServidorArquivoOnline()));
//		obj.setDescricao(tipoOrigemDocumentoAssinadoEnum.getDescricao() + Constantes.UNDERSCORE + gestaoXmlGradeCurricularVO.getGradeCurricularVO().getCodigo() + Constantes.UNDERSCORE + Uteis.getDataAplicandoFormatacao(new Date(), Constantes.MASCARA_ddMMyyyyHHmmss) + Constantes.UNDERSCORE + obj.getExtensao());
		File arquivoDiretorioCorreto = new File(config.getLocalUploadArquivoFixo() + File.separator + obj.getPastaBaseArquivo() + File.separator + obj.getNome());
		getFacadeFactory().getArquivoHelper().copiar(arquivo, arquivoDiretorioCorreto);
		return obj;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void alterarExpedicaoDiplomaDocumentoAssinado(Integer documentoAssinado, Integer expedicaoDiploma) {
		if (Uteis.isAtributoPreenchido(documentoAssinado)) {
			StringBuilder sql = new StringBuilder();
			sql.append("UPDATE documentoAssinado SET expedicaoDiploma = ? WHERE codigo = ?");
			getConexao().getJdbcTemplate().update(sql.toString(), expedicaoDiploma, documentoAssinado);
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void rejeitarDocumentoAssinadoProvedorAssinatura(DocumentoAssinadoVO documentoAssinadoExcluir, String motivoRejeicaoDocumentoAssinadoProvedorAssinatura, UsuarioVO usuarioLogado) throws Exception  {		
		DocumentoAssinadoPessoaVO documentoAssinadoPessoaVO = new DocumentoAssinadoPessoaVO();
		documentoAssinadoPessoaVO.setDataRejeicao(new Date());
		Uteis.checkState(!Uteis.isAtributoPreenchido(motivoRejeicaoDocumentoAssinadoProvedorAssinatura), "O campo motivo rejeição documentos assinados digitalmente pendentes deve ser preenchido.");
		documentoAssinadoPessoaVO.setMotivoRejeicao(motivoRejeicaoDocumentoAssinadoProvedorAssinatura);
		documentoAssinadoPessoaVO.setSituacaoDocumentoAssinadoPessoaEnum(SituacaoDocumentoAssinadoPessoaEnum.REJEITADO);
		List<DocumentoAssinadoVO> documentoAssinadoVOs = new ArrayList<DocumentoAssinadoVO>();
		documentoAssinadoVOs.add(documentoAssinadoExcluir);
		getFacadeFactory().getDocumentoAssinadoPessoaFacade().atualizarDadosRejeicaoDocumentosAssinados(documentoAssinadoPessoaVO, documentoAssinadoVOs);
		documentoAssinadoExcluir.setDocumentoAssinadoInvalido(true);
		documentoAssinadoExcluir.setMotivoDocumentoAssinadoInvalido(motivoRejeicaoDocumentoAssinadoProvedorAssinatura);
		getFacadeFactory().getDocumentoAssinadoFacade().atualizarDocumentoAssinadoInvalido(documentoAssinadoExcluir.getCodigo(), documentoAssinadoExcluir.isDocumentoAssinadoInvalido(), documentoAssinadoExcluir.getMotivoDocumentoAssinadoInvalido(), usuarioLogado);	
	}
	
	/**
	 * Método V2 para realizar a montagem do certificado do funcionário que assinou
	 * a documentação e depois assinar o arquivo, assim retornando o arquivo
	 * assinado
	 * 
	 * @param unidadeEnsino
	 * @param arquivo
	 * @param configGEDVO
	 * @param fileAssinar
	 * @param config
	 * @param usuarioVO
	 * @return
	 * @throws IOException
	 * @throws Exception
	 * @author Felipi Alves
	 */
	private File assinaturaDocumentoPeloFuncionarioV2(UnidadeEnsinoVO unidadeEnsino, ArquivoVO arquivo, ConfiguracaoGEDVO configGEDVO, File fileAssinar, ConfiguracaoGeralSistemaVO config, UsuarioVO usuarioVO, String idDocumentacao) throws IOException, Exception {
		if (Uteis.isAtributoPreenchido(usuarioVO.getPessoa())) {
			PessoaVO pessoaVO = getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(usuarioVO.getPessoa().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
			pessoaVO.setSenhaCertificadoParaDocumento(getFacadeFactory().getPessoaFacade().consultarSenhaCertificadoParaDocumento(pessoaVO.getCodigo()));
			ArquivoVO certificadoParaDocumento = validarCertificadoParaDocumento(unidadeEnsino, null, config, usuarioVO, pessoaVO);
			return preencherAssinadorDigitalDocumentoPdfParaDocumentoMatriculaV2(fileAssinar.getAbsolutePath(), certificadoParaDocumento, pessoaVO.getSenhaCertificadoParaDocumento(), arquivo.getPastaBaseArquivo(), arquivo.getNome(), AlinhamentoAssinaturaDigitalEnum.RODAPE_ESQUERDA, "#000000", 40f, 200f, 6f, 400, 20, 400, 2, config, configGEDVO, Constantes.EMPTY, idDocumentacao);
		} else {
			throw new Exception("O usuário utilizado para a assinatura deve ter uma pessoa vinculada");
		}
	}
	
	/**
	 * Método V2 de assinatura da documentação de matrícula do aluno, retornando o
	 * arquivo assinado
	 * 
	 * @param arquivoOrigem
	 * @param certificadoDigitalVO
	 * @param senhaCertificadoDigital
	 * @param caminhoPastaBase
	 * @param nomeArquivo
	 * @param alinhamentoAssinaturaDigital
	 * @param corAssinatura
	 * @param alturaAssinatura
	 * @param larguraAssinatura
	 * @param tamanhoFonte
	 * @param coordenadaLLX
	 * @param coordenadaLLY
	 * @param coordenadaURX
	 * @param coordenadaURY
	 * @param config
	 * @param configGed
	 * @param urlValidacao
	 * @return
	 * @throws Exception
	 * @author Felipi Alves
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private File preencherAssinadorDigitalDocumentoPdfParaDocumentoMatriculaV2(String arquivoOrigem, ArquivoVO certificadoDigitalVO, String senhaCertificadoDigital, String caminhoPastaBase, String nomeArquivo, AlinhamentoAssinaturaDigitalEnum alinhamentoAssinaturaDigital, String corAssinatura, Float alturaAssinatura, Float larguraAssinatura, float tamanhoFonte, int coordenadaLLX, int coordenadaLLY, int coordenadaURX, int coordenadaURY, ConfiguracaoGeralSistemaVO config, ConfiguracaoGEDVO configGed, String urlValidacao, String idDocumentacao) throws Exception {
		return preencherAssinadorDigitalDocumentoPdfParaDocumentoMatriculaV2(arquivoOrigem, certificadoDigitalVO, senhaCertificadoDigital, caminhoPastaBase, nomeArquivo, alinhamentoAssinaturaDigital, corAssinatura, alturaAssinatura, larguraAssinatura, tamanhoFonte, coordenadaLLX, coordenadaLLY, coordenadaURX, coordenadaURY, config, configGed, urlValidacao, new Date(), Boolean.TRUE, idDocumentacao);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public File preencherAssinadorDigitalDocumentoPdfParaDocumentoMatriculaV2(String arquivoOrigem, ArquivoVO certificadoDigitalVO, String senhaCertificadoDigital, String caminhoPastaBase, String nomeArquivo, AlinhamentoAssinaturaDigitalEnum alinhamentoAssinaturaDigital, String corAssinatura, Float alturaAssinatura, Float larguraAssinatura, float tamanhoFonte, int coordenadaLLX, int coordenadaLLY, int coordenadaURX, int coordenadaURY, ConfiguracaoGeralSistemaVO config, ConfiguracaoGEDVO configGed, String urlValidacao, Date dataAssinatura, Boolean pastaBaseArquivoFixo, String idDocumentacao) throws Exception {
		String caminhoCertificado = Constantes.EMPTY;
		validarDadosCertificadoDigital(certificadoDigitalVO, senhaCertificadoDigital);
		if (Uteis.isAtributoPreenchido(certificadoDigitalVO.getPastaBaseArquivo())) {
			caminhoCertificado = config.getLocalUploadArquivoFixo() + File.separator + certificadoDigitalVO.getPastaBaseArquivo() + File.separator + certificadoDigitalVO.getNome();
		} else {
			caminhoCertificado = config.getLocalUploadArquivoFixo() + File.separator + config.getCertificadoParaDocumento().getPastaBaseArquivo() + File.separator + certificadoDigitalVO.getNome();
		}
		AssinaturaDigialDocumentoPDF assinador = new AssinaturaDigialDocumentoPDF();
		assinador.setToken(true);
		assinador.setPathKeyStore(caminhoCertificado);
		assinador.setSenhaKeyStore(senhaCertificadoDigital);
		assinador.setArquivoOrigem(arquivoOrigem);
		assinador.setCaminhoArquivoDestino(pastaBaseArquivoFixo ? (config.getLocalUploadArquivoFixo() + File.separator + caminhoPastaBase) : (caminhoPastaBase));
		assinador.setNomeArquivoDestino(nomeArquivo);
		assinador.setIsValidarArquivoExistente(true);
		assinador.setRazaoCertificado(Uteis.isAtributoPreenchido(idDocumentacao) ? StringUtils.leftPad(idDocumentacao, 12, "0") : StringUtils.leftPad("0", 12, "0"));
		assinador.setDataAssinatura(dataAssinatura);
		assinador.setCorAssinaturaDigitalmente(corAssinatura);
		assinador.setAlturaAssinatura(alturaAssinatura);
		assinador.setLarguraAssinatura(larguraAssinatura);
		assinador.setTamanhoFonte(tamanhoFonte);
		assinador.setCoordenadaLLX(coordenadaLLX);
		assinador.setCoordenadaLLY(coordenadaLLY);
		assinador.setCoordenadaURX(coordenadaURX);
		assinador.setCoordenadaURY(coordenadaURY);
		if (alinhamentoAssinaturaDigital.isRodapeDireita()) {
			assinador.setIsPosicaoAssinaturaCima(false);
			assinador.setIsPosicaoAssinaturaEsquerdo(false);
		} else if (alinhamentoAssinaturaDigital.isRodapeEsquerda()) {
			assinador.setIsPosicaoAssinaturaCima(false);
			assinador.setIsPosicaoAssinaturaEsquerdo(true);
		} else if (alinhamentoAssinaturaDigital.isTopoDireita()) {
			assinador.setIsPosicaoAssinaturaCima(true);
			assinador.setIsPosicaoAssinaturaEsquerdo(false);
		} else if (alinhamentoAssinaturaDigital.isTopoEsquerda()) {
			assinador.setIsPosicaoAssinaturaCima(true);
			assinador.setIsPosicaoAssinaturaEsquerdo(true);
		}
		assinador.realizarGeracaoDocumentoPdf();
		File arquivoAssinado = new File(assinador.getCaminhoArquivoDestino() + File.separator + assinador.getNomeArquivoDestino());
		if (Uteis.isAtributoPreenchido(arquivoAssinado)) {
			return arquivoAssinado;
		} else {
			return new File(arquivoOrigem);
		}
	}
	
	/**
	 * Método V2 de realizar assinatura da documentação de matrícula do aluno, nesse
	 * V2 vai retornar o File que foi assinado ou adicionado o selo
	 * 
	 * @param unidadeEnsino
	 * @param arquivoVO
	 * @param configGEDVO
	 * @param fileAssinar
	 * @param config
	 * @param usuarioVO
	 * @param realizandoCorrecaoPDFA
	 * @param realizarConversaoPDFPDFAImagem
	 * @param persistirArquivo
	 * @return
	 * @throws Exception
	 * @author Felipi Alves
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public File realizarAssinaturaDocumentacaoAlunoV2(UnidadeEnsinoVO unidadeEnsino, ArquivoVO arquivoVO, ConfiguracaoGEDVO configGEDVO, File fileAssinar, ConfiguracaoGeralSistemaVO config, UsuarioVO usuarioVO, Boolean realizandoCorrecaoPDFA, Boolean realizarConversaoPDFPDFAImagem, Boolean persistirArquivo, String idDocumentacao) throws Exception {
		File arquivoAssinado = fileAssinar;
		if (Uteis.isAtributoPreenchido(configGEDVO) && Uteis.isAtributoPreenchido(configGEDVO.getConfiguracaoGedDocumentoAlunoVO())) {
			ConfiguracaoGedOrigemVO configuracaoGedOrigem = configGEDVO.getConfiguracaoGedDocumentoAlunoVO();
			if (configuracaoGedOrigem.getAssinarDocumento()) {
				if (configuracaoGedOrigem.getApresentarSelo() && !arquivoVO.getOrigem().equals(OrigemArquivo.DOCUMENTO_GED.getValor()) && !realizandoCorrecaoPDFA && (!getFacadeFactory().getArquivoHelper().verificarPDFIsPDFA(arquivoAssinado.getPath()) || getFacadeFactory().getArquivoHelper().verificarQuantidadeAssinaturasPdf(arquivoAssinado.getPath()) == 0)) {
					adicionarSeloPDF(TipoOrigemDocumentoAssinadoEnum.DOCUMENTO_ALUNO, arquivoAssinado.getAbsolutePath(), config, configGEDVO);
				}
				getFacadeFactory().getArquivoHelper().realizarConversaoPdfPdfA(arquivoAssinado.getPath(), config, arquivoVO, realizarConversaoPDFPDFAImagem, false);
				if (configuracaoGedOrigem.getAssinarDocumentoFuncionarioResponsavel() && !arquivoVO.getOrigem().equals(OrigemArquivo.DOCUMENTO_GED.getValor())) {
					arquivoAssinado = assinaturaDocumentoPeloFuncionarioV2(unidadeEnsino, arquivoVO, configGEDVO, arquivoAssinado, config, usuarioVO, idDocumentacao);
					arquivoVO.setArquivoAssinadoFuncionario(Boolean.TRUE);
				}
				if (configuracaoGedOrigem.getAssinaturaUnidadeEnsino()) {
					String urlValidacao = config.getUrlAcessoExternoAplicacao() + "/visaoAdministrativo/academico/documentoAssinado.xhtml?tipoDoc=" + TipoOrigemDocumentoAssinadoEnum.DOCUMENTO_ALUNO + "&dados=0";
					arquivoAssinado = preencherAssinadorDigitalDocumentoPdfParaDocumentoMatriculaV2(arquivoAssinado.getAbsolutePath(), configGEDVO.getCertificadoDigitalUnidadeEnsinoVO(), configGEDVO.getSenhaCertificadoDigitalUnidadeEnsino(), arquivoVO.getPastaBaseArquivo(), arquivoVO.getNome(), AlinhamentoAssinaturaDigitalEnum.RODAPE_ESQUERDA, "#000000", 40f, 200f, 6f, 60, 20, 60, 2, config, configGEDVO, urlValidacao, idDocumentacao);
					arquivoVO.setArquivoAssinadoUnidadeEnsino(Boolean.TRUE);
				}
				if (Uteis.isAtributoPreenchido(arquivoAssinado) && Uteis.isAtributoPreenchido(fileAssinar) && !Objects.equals(arquivoAssinado.getPath(), fileAssinar.getPath())) {
					// exclusão do arquivo temporário utilizado para realizar a assinatura
					// (fileAssinar), permanecendo apenas o arquivo assinado temporário
					// (arquivoAssinado)
					fileAssinar.delete();
				}
			}
			if (realizandoCorrecaoPDFA) {
				arquivoVO.setErroPDFA(null);
				arquivoVO.setProcessadoPDFA(true);
			}
			if (persistirArquivo) {
				getFacadeFactory().getArquivoFacade().persistir(arquivoVO, false, usuarioVO, config);
			}
		}
		return arquivoAssinado;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void atualizarSituacaoDocumentoAssinadoErro(DocumentoAssinadoVO documentoAssinado) {
		if (Uteis.isAtributoPreenchido(documentoAssinado)) {
			String update = "UPDATE documentoAssinado SET erro = ?, motivoErro = ? WHERE codigo = ?";
			getConexao().getJdbcTemplate().update(update, documentoAssinado.getErro(), documentoAssinado.getMotivoErro(), documentoAssinado.getCodigo());
		}
	}

	/**
	 * Se o número for nulo (ou zero, se desejar), retorna null.
	 * Caso contrário, retorna number.toString().
	 * Por conta do impacto em mudanca do
	 * tipo codigoprovedordeassinatura se tornar varchar(100)
	 */
	public static String numeroParaStringOuNull(Number number) {
		if (number == null) {
			return null;
		}
		return number.toString();
	}

    /**
     * Se o número for nulo (ou zero, se desejar), retorna null.
     * Caso contrário, retorna number.toString().
     * Por conta do impacto em mudanca do
     * tipo codigoprovedordeassinatura se tornar varchar(100)
     */
    public static Integer stringParaIntegerOuNull(String string) {
        if (string == null || string.trim().isEmpty()) {
            return null;
        }
        try {
            return Integer.valueOf(string.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String nomePastaDocumento(TipoOrigemDocumentoAssinadoEnum tipoOrigemDocumentoAssinadoEnum) {
        switch (tipoOrigemDocumentoAssinadoEnum) {
            case CONTRATO:
                return "CONTRATO";
            case ESTAGIO:
                return "ESTAGIO";
            case TERMO_ESTAGIO_OBRIGATORIO:
                return "ESTAGIO";
            case TERMO_ESTAGIO_NAO_OBRIGATORIO:
                return "ESTAGIO";
            case TERMO_ADITIVO_ESTAGIO_NAO_OBRIGATORIO:
                return "ESTAGIO";
            case TERMO_RESCISAO_ESTAGIO_NAO_OBRIGATORIO:
                return "ESTAGIO";
            case ATA_COLACAO_GRAU:
                return "ATA DE COLAÇÃO DE GRAU";
            case EXPEDICAO_DIPLOMA:
                return "EXPEDIÇÃO DE DIPLOMA";
            default:
                throw new IllegalArgumentException("Tipo de documento não suportado: " + tipoOrigemDocumentoAssinadoEnum);
        }
    }

    public void gerarFolderId(ConfiguracaoGEDVO configuracaoGEDVO, TipoOrigemDocumentoAssinadoEnum tipoOrigemDocumentoAssinadoEnum, int ano, int mes) {
        if (TipoOrigemDocumentoAssinadoEnum.NENHUM.equals(tipoOrigemDocumentoAssinadoEnum)) {
            throw new IllegalArgumentException("Erro Integração: tipo de documento não pode ser NENHUM.");
        }

        String contratoKey = nomePastaDocumento(tipoOrigemDocumentoAssinadoEnum).toUpperCase();
        String anoStr = String.valueOf(ano);
        String mesStr = String.format("%02d", mes);


        // Mantém em cache apenas a pasta raiz
        String pastaRaizId = getAplicacaoControle().getFoldersCache().computeIfAbsent(
                contratoKey,
                key -> fetchOrCreate(configuracaoGEDVO, key, null)
        );

        // Tenta obter mapas de ano e mês já existentes (sem criar)
        ConcurrentHashMap<String, String> anosMap = getAplicacaoControle().getAnoCache().get(contratoKey);
        ConcurrentHashMap<String, ConcurrentHashMap<String, String>> anosMesesMap =
                getAplicacaoControle().getMesCache().get(contratoKey);

        boolean jaTemAnoEMes =
                anosMap != null
                        && anosMap.containsKey(anoStr)
                        && anosMesesMap != null
                        && anosMesesMap.containsKey(anoStr)
                        && anosMesesMap.get(anoStr).containsKey(mesStr);

        // 3) Se for um par ano+mes novo, zera só esses caches para este contrato
        if (!jaTemAnoEMes) {
            getAplicacaoControle().getAnoCache().put(contratoKey, new ConcurrentHashMap<>());
            getAplicacaoControle().getMesCache().put(contratoKey, new ConcurrentHashMap<>());

            anosMap = getAplicacaoControle().getAnoCache().get(contratoKey);
            anosMesesMap = getAplicacaoControle().getMesCache().get(contratoKey);
        }

        // 4) (Re)cria ou reutiliza o ID do ano atual
        String anoId = anosMap.computeIfAbsent(
                anoStr,
                key -> fetchOrCreate(configuracaoGEDVO, key, pastaRaizId)
        );

        // 5) (Re)cria ou reutiliza o mapa de meses daquele ano
        ConcurrentHashMap<String, String> mesesMap =
                anosMesesMap.computeIfAbsent(anoStr, k -> new ConcurrentHashMap<>());

        // 6) (Re)cria ou reutiliza o ID do mês atual
        String mesId = mesesMap.computeIfAbsent(
                mesStr,
                key -> fetchOrCreate(configuracaoGEDVO, key, anoId)
        );

        // Opcional: use ou retorne pastaRaizId, anoId e mesId
        // System.out.printf("Contrato=%s ? IDs: %s / %s / %s%n",
        //     contratoKey, pastaRaizId, anoId, mesId);
    }

    /**
     * Consulta ou cria uma pasta com nome `nome` sob o pai `parentId`.
     * Se parentId for null, cria na raiz.
     */
    private String fetchOrCreate(ConfiguracaoGEDVO configuracaoGEDVO, String nome, String parentId) {
        // 1) Monta filtro por nome e parentId
        Map<String, String> filtro = new HashMap<>();
        filtro.put("q", nome);
        if (Uteis.isAtributoPreenchido(parentId)) {
            filtro.put("parentId", parentId);
        }

        // 2) Chama a API (que pode ainda trazer pastas com nome igual e outros parentId)
        JSONArray arr = listarPastas(configuracaoGEDVO, filtro);

        // 3) Filtra manualmente só as pastas com o parentId exato
        JSONArray matching = new JSONArray();
        for (int i = 0; i < arr.length(); i++) {
            JSONObject pasta = arr.getJSONObject(i);
            String pid = pasta.optString("parentId", "");
            if (!Uteis.isAtributoPreenchido(parentId)) {
                if (!Uteis.isAtributoPreenchido(pid)) {
                    matching.put(pasta);
                }
            } else {
                if (parentId.equals(pid)) {
                    matching.put(pasta);
                }
            }
        }

        // 4) Se encontrou, retorna o primeiro ID
        if (matching.length() > 0) {
            return matching.getJSONObject(0).getString("id");
        }

        // 5) Senão cria a pasta (na raiz ou sob o parentId)
        String paiParaCriar = Uteis.isAtributoPreenchido(parentId) ? parentId : "";
        return persistirPasta(configuracaoGEDVO, nome, paiParaCriar)
                .getString("id");
    }

    public List<String> extrairDocumentIdJson(JSONArray responseArray) {
        if (responseArray == null) {
            throw new IllegalArgumentException("Resposta TechCert não pode ser nula.");
        }
        List<String> ids = new ArrayList<>();
        for (int i = 0; i < responseArray.length(); i++) {
            JSONObject obj = responseArray.optJSONObject(i);
            if (obj != null) {
                String id = obj.optString("id", null);
                if (Uteis.isAtributoPreenchido(id)) {
                    ids.add(id);
                }
            }
        }
        return ids;
    }

    /**
     * Move um documento já criado para a pasta correspondente
     * ao tipo, ano e mês informados.
     *
     * @param tipoDocumento tipo do documento (enum)
     * @param ano           ano de destino
     * @param mes           mês de destino
     * @param documentId    ID do documento no TechCert
     */
    public String moveDocumentFolder(ConfiguracaoGEDVO configuracaoGEDVO, TipoOrigemDocumentoAssinadoEnum tipoDocumento, Integer ano, Integer mes, String documentId) {
        try {
            String tipoDocumentoKey = nomePastaDocumento(tipoDocumento).toUpperCase();
            String anoStr = ano.toString();
            String mesStr = String.format("%02d", mes);
            String newFolderName = "";

            String folderId = getAplicacaoControle().getMesCache()
                    .getOrDefault(tipoDocumentoKey, new ConcurrentHashMap<>())
                    .getOrDefault(anoStr, new ConcurrentHashMap<>())
                    .get(mesStr);

            if (!Uteis.isAtributoPreenchido(folderId)) {
                throw new IllegalStateException(
                        "Pasta não encontrada em cache para " +
                                tipoDocumentoKey + " / " + anoStr + " / " + mesStr
                );
            }
            JSONArray documentoMovido = incluirMoverDocuments(
                    folderId,
                    newFolderName,
                    documentId,
                    configuracaoGEDVO);
            return extrairDocumentIdJson(documentoMovido).get(0);
        } catch (Exception e) {
            throw new RuntimeException("Falha ao mover documento para a pasta (integração TechCert): " + e.getMessage(), e);
        }
    }


    private IntegracaoTechCertVO montarConsultaParaLabels(IntegracaoTechCertVO integracaoTechCertVO, Map<String, Object> filtros, UsuarioVO usuarioLogado, DocumentoAssinadoVO documentoAssinadoVO) {
        try {
            if (documentoAssinadoVO.getTipoOrigemDocumentoAssinadoEnum() == TipoOrigemDocumentoAssinadoEnum.ATA_COLACAO_GRAU) {
                integracaoTechCertVO.setNomeCurso(documentoAssinadoVO.getCursoVO().getNome());
                integracaoTechCertVO.setTituloColacaoGrau(documentoAssinadoVO.getTituloColacaoGrau());
                integracaoTechCertVO.setInfoAtaColacaoGrau(documentoAssinadoVO.getInfoAtaColacaoGrau());
                integracaoTechCertVO.setExpirationDate(documentoAssinadoVO.getExpirationDate());
                integracaoTechCertVO.setDataColacaoGrau(documentoAssinadoVO.getDataColacaoGrau());
                return integracaoTechCertVO;
            } else {
                List<IntegracaoTechCertVO> integracaoTechCertVOS = consultarPessoaParaDocumentoAssinadoJson(filtros, usuarioLogado);
                integracaoTechCertVO.setRa(integracaoTechCertVOS.get(0).getRa());
                integracaoTechCertVO.setNomeAluno(integracaoTechCertVOS.get(0).getNomeAluno());
                integracaoTechCertVO.setMatricula(integracaoTechCertVOS.get(0).getMatricula());
                integracaoTechCertVO.setNomeCurso(integracaoTechCertVOS.get(0).getNomeCurso());
                integracaoTechCertVO.setNomePolo(integracaoTechCertVOS.get(0).getNomePolo());
                integracaoTechCertVO.setAno(integracaoTechCertVOS.get(0).getAno());
                integracaoTechCertVO.setSemestre(integracaoTechCertVOS.get(0).getSemestre());
                return integracaoTechCertVO;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void integrarDocumentoTechCert(IntegracaoTechCertVO integracaoTechCertVO, String arquivoOrigem, DocumentoAssinadoVO documentoAssinadoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, ConfiguracaoGEDVO configuracaoGEDVO, UsuarioVO usuarioLogado) {
        try {
            Date dataRegistroDate = documentoAssinadoVO.getDataRegistro();
            LocalDate dataRegistro = dataRegistroDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            int ano = dataRegistro.getYear();
            int mes = dataRegistro.getMonthValue();
            if (validarDocumentoParaIntegracaoTechCert(documentoAssinadoVO, mes, ano)) {
                gerarFolderId(configuracaoGEDVO, documentoAssinadoVO.getTipoOrigemDocumentoAssinadoEnum(), ano, mes);
                UploadsTechCertVO responseUploadDocumento = uploadArquivo(configuracaoGEDVO, arquivoOrigem);
                documentoAssinadoVO.setCodigoProvedorDeAssinatura(responseUploadDocumento.getId());
                Map<String, Object> filtros = new HashMap<>();
                filtros.put("matricula", documentoAssinadoVO.getMatricula().getMatricula());
                if (isEstagioParaTags(documentoAssinadoVO.getTipoOrigemDocumentoAssinadoEnum())){
                    integracaoTechCertVO.setConcedenteDoEstagioNome(documentoAssinadoVO.getConcedenteDocumentoEstagio());
                    integracaoTechCertVO.setResponsavelDoConcendenteNome(documentoAssinadoVO.getResponsavelConcedente());
                    integracaoTechCertVO.setEstagioNome(documentoAssinadoVO.getNomeGradeCurricularEstagio());
                }
                DocumentsCreateTechCertVO[] document = persistirDocuments(documentoAssinadoVO, configuracaoGEDVO,
                        montarResponseJsonUploadsBytesParaIntegracaoTechCert(responseUploadDocumento,
                                montarConsultaParaLabels(integracaoTechCertVO, filtros, usuarioLogado, documentoAssinadoVO)));
                documentoAssinadoVO.setChaveProvedorDeAssinatura(moveDocumentFolder(configuracaoGEDVO,
                        documentoAssinadoVO.getTipoOrigemDocumentoAssinadoEnum(), ano, mes,
                        document[0].getDocumentId()));
                DocumentsTechCertVO documentsTechCertVO = consultarDocumet(configuracaoGEDVO, document[0].getDocumentId());
                for (DocumentoAssinadoPessoaVO dap : documentoAssinadoVO.getListaDocumentoAssinadoPessoa()) {
                    String nome = Uteis.isAtributoPreenchido(dap.getPessoaVO().getNome()) ? Uteis.removerAcentos(Uteis.removeCaractersEspeciais(dap.getPessoaVO().getNome())).replace(" ", "") : Uteis.removerAcentos(Uteis.removeCaractersEspeciais(dap.getNomePessoa())).replace(" ", "");
                    String cpf = Uteis.isAtributoPreenchido(dap.getPessoaVO().getCPF()) ? Uteis.removerAcentos(Uteis.removeCaractersEspeciais(dap.getPessoaVO().getCPF())).replace(" ", "") : Uteis.removerAcentos(Uteis.removeCaractersEspeciais(dap.getCpfPessoa())).replace(" ", "");
                    for (FlowActionsTechCertVO flow : documentsTechCertVO.getFlowActions()) {
                        //===============SIGNER===============
                        if (flow.getUser() != null && Uteis.isAtributoPreenchido(flow.getUser().getId())) {
                            String user = Uteis.removerAcentos(Uteis.removeCaractersEspeciais(flow.getUser().getName())).replace(" ", "");
                            String identifier = Uteis.removerAcentos(Uteis.removeCaractersEspeciais(flow.getUser().getIdentifier())).replace(" ", "");
                            Map<String, Boolean> map = validarDocumentoAssinadoPessoaResponsavelPelaAssinatura(nome, cpf, user, identifier);
                            if (map.get(PESSOA_RESPONSAVEL_ASSINATURA)) {
                                if (dap.getOrdemAssinatura() == 1 && flow.getStep() == 1) {
                                    DocumentActionUrlTechCertVO links = null;
                                    links = regraGeracaoOrdemUrlAssinaturaNovo(user, identifier, documentsTechCertVO, configuracaoGEDVO, integracaoTechCertVO);
                                    dap.setUrlProvedorDeAssinatura(links != null ? links.getUrl() : "");
                                    dap.setUrlAssinatura(links != null ? links.getEmbedUrl() : "");
                                }
                                dap.setCodigoAssinatura(flow.getId());
                            }
                        }
                        //====================================

                        //===============SIGNRULE===============
                        if (Uteis.isAtributoPreenchido(flow.getSignRuleUsers())){
                            for (SignRuleUserTechCertVO sign : flow.getSignRuleUsers()) {
                                String user = Uteis.isAtributoPreenchido(sign) ? Uteis.removerAcentos(Uteis.removeCaractersEspeciais(sign.getName())).replace(" ", "") : "";
                                String identifier = Uteis.isAtributoPreenchido(sign.getIdentifier()) ? Uteis.removerAcentos(Uteis.removeCaractersEspeciais(sign.getIdentifier())).replace(" ", "") : "";
                                Map<String, Boolean> map = validarDocumentoAssinadoPessoaResponsavelPelaAssinatura(nome, cpf, user, identifier);
                                if (map.get(PESSOA_RESPONSAVEL_ASSINATURA)) {
                                    if (dap.getOrdemAssinatura() == 1 && flow.getStep() == 1) {
                                        DocumentActionUrlTechCertVO links = null;
                                        links = regraGeracaoOrdemUrlAssinaturaNovo(nome, cpf, documentsTechCertVO, configuracaoGEDVO, integracaoTechCertVO);
                                        dap.setUrlProvedorDeAssinatura(links != null ? links.getUrl() : "");
                                        dap.setUrlAssinatura(links != null ? links.getEmbedUrl() : "");
                                    }
                                    dap.setCodigoAssinatura(flow.getId());
                                }
                            }
                        }
                        //====================================
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Boolean isEstagioParaTags(TipoOrigemDocumentoAssinadoEnum tipoOrigemDocumentoAssinadoEnum) {
        return TipoOrigemDocumentoAssinadoEnum.TERMO_ESTAGIO_OBRIGATORIO == tipoOrigemDocumentoAssinadoEnum
                || TipoOrigemDocumentoAssinadoEnum.TERMO_ESTAGIO_NAO_OBRIGATORIO == tipoOrigemDocumentoAssinadoEnum
                || TipoOrigemDocumentoAssinadoEnum.TERMO_ADITIVO_ESTAGIO_NAO_OBRIGATORIO == tipoOrigemDocumentoAssinadoEnum
                || TipoOrigemDocumentoAssinadoEnum.TERMO_RESCISAO_ESTAGIO_NAO_OBRIGATORIO == tipoOrigemDocumentoAssinadoEnum;
    }

    @Override
    public Boolean isHabilitadoTipoDocumentoTechCert(TipoOrigemDocumentoAssinadoEnum tipoDocumentoAtual) {
        if (!Uteis.isAtributoPreenchido(tipoDocumentoAtual) || TipoOrigemDocumentoAssinadoEnum.NENHUM.equals(tipoDocumentoAtual)) {
            return Boolean.FALSE;
        }
        EnumSet<TipoOrigemDocumentoAssinadoEnum> tiposPermitidos =
                EnumSet.of(
                        TipoOrigemDocumentoAssinadoEnum.CONTRATO,
                        TipoOrigemDocumentoAssinadoEnum.ESTAGIO,
                        TipoOrigemDocumentoAssinadoEnum.TERMO_ESTAGIO_OBRIGATORIO,
                        TipoOrigemDocumentoAssinadoEnum.TERMO_ESTAGIO_NAO_OBRIGATORIO,
                        TipoOrigemDocumentoAssinadoEnum.TERMO_ADITIVO_ESTAGIO_NAO_OBRIGATORIO,
                        TipoOrigemDocumentoAssinadoEnum.TERMO_RESCISAO_ESTAGIO_NAO_OBRIGATORIO,
                        TipoOrigemDocumentoAssinadoEnum.ATA_COLACAO_GRAU,
                        TipoOrigemDocumentoAssinadoEnum.EXPEDICAO_DIPLOMA);

        return tiposPermitidos.stream().anyMatch(permitido -> permitido.equals(tipoDocumentoAtual));
    }

    public Boolean validarDocumentoParaIntegracaoTechCert(DocumentoAssinadoVO documentoAssinadoVO, Integer ano, Integer mes) throws ConsistirException {
        if (!Uteis.isAtributoPreenchido(documentoAssinadoVO.getTipoOrigemDocumentoAssinadoEnum().getDescricao())) {
            throw new ConsistirException("Tipo de documento não pode ser nulo ou vazio.");
        }
        if (TipoOrigemDocumentoAssinadoEnum.NENHUM.equals(documentoAssinadoVO.getTipoOrigemDocumentoAssinadoEnum())) {
            throw new ConsistirException("Tipo de documento não pode ser NENHUM.");
        }
        if (!Uteis.isAtributoPreenchido(documentoAssinadoVO.getTipoOrigemDocumentoAssinadoEnum().getDescricao()) || TipoOrigemDocumentoAssinadoEnum.NENHUM.equals(documentoAssinadoVO.getTipoOrigemDocumentoAssinadoEnum())) {
            throw new ConsistirException("Tipo de documento não pode ser nulo ou vazio.");
        }
//        if (!isHabilitadoTipoDocumentoTechCert(documentoAssinadoVO.getTipoOrigemDocumentoAssinadoEnum())) {
//            throw new ConsistirException("Tipo de documento não habilitado para integração com TechCert: " + documentoAssinadoVO.getTipoOrigemDocumentoAssinadoEnum().getDescricao());
//        }
        return isHabilitadoTipoDocumentoTechCert(documentoAssinadoVO.getTipoOrigemDocumentoAssinadoEnum()) && Uteis.isAtributoPreenchido(documentoAssinadoVO.getTipoOrigemDocumentoAssinadoEnum().getDescricao()) && Uteis.isAtributoPreenchido(ano) && Uteis.isAtributoPreenchido(mes);

    }

    public IntegracaoTechCertVO montarResponseJsonUploadsBytesParaIntegracaoTechCert(UploadsTechCertVO uploadsTechCertVO, IntegracaoTechCertVO integracaoTechCertVO) {
        integracaoTechCertVO.setFileCodigo(uploadsTechCertVO.getId());
        integracaoTechCertVO.setProvedorDeAssinaturaEnum(ProvedorDeAssinaturaEnum.TECHCERT);
        return integracaoTechCertVO;
    }

    public UploadsTechCertVO uploadArquivo(ConfiguracaoGEDVO configuracaoGEDVO, String arquivoOrigem) {
        try {
            File fileTemp = new File(arquivoOrigem);
            Uteis.checkState(!fileTemp.exists(), "Arquivo não foi encontrado no caminho " + arquivoOrigem + " para ser enviado ao Provedor de Assinatura.");
            byte[] arquivoBytes = Files.readAllBytes(fileTemp.toPath());
            return uploadBytes(configuracaoGEDVO, arquivoBytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List consultarPessoaParaDocumentoAssinadoJson(Map<String, Object> filtros, UsuarioVO usuario) throws Exception {
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append("select ");
        sqlStr.append("  case ");
        sqlStr.append("    when p.registroAcademico is null ");
        sqlStr.append("      or p.registroAcademico = '' ");
        sqlStr.append("      then m.matricula ");
        sqlStr.append("    else p.registroAcademico ");
        sqlStr.append("  end as ra, ");
        sqlStr.append("  p.nome as nomeAluno, ");
        sqlStr.append("  mper.matricula as matricula, ");
        sqlStr.append("  replace( ");
        sqlStr.append("    replace( ");
        sqlStr.append("      replace( ");
        sqlStr.append("        replace( ");
        sqlStr.append("          replace( ");
        sqlStr.append("            replace(c.nome, ',', ''), ");
        sqlStr.append("          ';', ''), ");
        sqlStr.append("        '''', ''), ");
        sqlStr.append("      '-', ' '), ");
        sqlStr.append("    '   ', ' '), ");
        sqlStr.append("  '  ', ' ') as nomeCurso, ");
        sqlStr.append("  replace( ");
        sqlStr.append("    replace( ");
        sqlStr.append("      replace( ");
        sqlStr.append("        replace( ");
        sqlStr.append("          replace( ");
        sqlStr.append("            replace(ue.nome, ',', ''), ");
        sqlStr.append("          ';', ''), ");
        sqlStr.append("        '''', ''), ");
        sqlStr.append("      '-', ' '), ");
        sqlStr.append("    '   ', ' '), ");
        sqlStr.append("  '  ', ' ') as nomePolo, ");
        sqlStr.append("  mper.ano as ano, ");
        sqlStr.append("  mper.semestre as semestre ");
        sqlStr.append("from ");
        sqlStr.append("  matricula m ");
        sqlStr.append("inner join matriculaperiodo mper ");
        sqlStr.append("  on mper.matricula = m.matricula ");
        sqlStr.append("inner join pessoa p ");
        sqlStr.append("  on p.codigo = m.aluno ");
        sqlStr.append("inner join unidadeEnsino ue ");
        sqlStr.append("  on ue.codigo = m.unidadeEnsino ");
        sqlStr.append("inner join curso c ");
        sqlStr.append("  on c.codigo = m.curso ");
        sqlStr.append(whereConsultarPessoaParaDocumentoAssinadoJson(filtros));
        sqlStr.append("order by ");
        sqlStr.append("  (mper.ano || '/' || mper.semestre) desc, ");
        sqlStr.append("  mper.codigo desc ");
        sqlStr.append("limit 1;");

//        System.out.println("Consulta SQL gerada: " + sqlStr.toString());
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());

        tabelaResultado.beforeFirst();
        List<?> listaConsulta = montarDadosResultadoPessoaParaDocumentoAssinadoJson(tabelaResultado);
        return listaConsulta;
    }

    private List montarDadosResultadoPessoaParaDocumentoAssinadoJson(SqlRowSet tabelaResultado) {
        List vetResultado = new ArrayList();
        while (tabelaResultado.next()) {
            vetResultado.add(montarDadosConsultaPessoaParaDocumentoAssinadoJson(tabelaResultado));
        }
        return vetResultado;
    }

    private IntegracaoTechCertVO montarDadosConsultaPessoaParaDocumentoAssinadoJson(SqlRowSet resultado) {
        IntegracaoTechCertVO integracaoTechCert = new IntegracaoTechCertVO();
        integracaoTechCert.setRa(resultado.getString("ra"));
        integracaoTechCert.setNomeAluno(resultado.getString("nomeAluno"));
        integracaoTechCert.setMatricula(resultado.getString("matricula"));
        integracaoTechCert.setNomeCurso(resultado.getString("nomeCurso"));
        integracaoTechCert.setNomePolo(resultado.getString("nomePolo"));
        integracaoTechCert.setAno(resultado.getString("ano"));
        integracaoTechCert.setSemestre(resultado.getString("semestre"));
        return integracaoTechCert;
    }

    private Boolean validarFiltrosVazioAndZero(Map<String, Object> filtros, String propriedade) {
        return filtros.containsKey(propriedade)
                && Uteis.isAtributoPreenchido(filtros)
                && Uteis.isAtributoPreenchido(filtros.get(propriedade).toString())
                && !filtros.get(propriedade).toString().trim().equals("0");
    }

    public StringBuilder whereConsultarPessoaParaDocumentoAssinadoJson(Map<String, Object> filtros) {
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append("WHERE 1 = 1");

        if (validarFiltrosVazioAndZero(filtros, "matricula")) {
            sqlStr.append("    and mper.matricula = ").append("'").append(filtros.get("matricula")).append("' ");
        }
        if (!validarFiltrosVazioAndZero(filtros, "matricula")) {
            sqlStr.append("    and mper.matricula = ").append("'").append("' ");
        }
        return sqlStr;
    }

    public JSONArray incluirMoverDocuments(String FolderId, String newFolderName, String documentId, ConfiguracaoGEDVO configuracaoGEDVO) {
        List<String> documentsId = new ArrayList<>();
        documentsId.add(documentId);
        return persistirMoverDocuments(FolderId, newFolderName, documentsId, configuracaoGEDVO);
    }

    public DocumentActionUrlTechCertVO gerarUrlsAssinatura(ConfiguracaoGEDVO configuracaoGEDVO, String DocumentId, JSONObject body) {
        try {
            if (!configuracaoGEDVO.getHabilitarIntegracaoTechCert()){
                throw new StreamSeiException("Configuração de integração com TechCert não está habilitada.");
            }
            String url = montarUrlPadrao(configuracaoGEDVO)
                    + UteisWebServiceUrl.URL_TECHCERT_DOCUMENTS + "/" + DocumentId + UteisWebServiceUrl.URL_TECHCERT_ACTION_URL;
            String token = montarToken(configuracaoGEDVO);
            HttpResponse<JsonNode> jsonResponse = unirest().post(url)
                    .header(UteisWebServiceUrl.Content_Type, UteisWebServiceUrl.Application_JSON)
                    .header(configuracaoGEDVO.getApikeyTechCert(), token)
                    .body(body)
                    .connectTimeout(10_000)
                    .asJson();
            if (jsonResponse.isSuccess() || jsonResponse.getStatus() == 200) {
                Gson gson = new GsonBuilder().create();
                return gson.fromJson(jsonResponse.getBody().toString(), DocumentActionUrlTechCertVO.class);
            }
            JSONObject error = jsonResponse.getBody().getObject();
            String code = error.optString("code", "");
            if ("NoPendingActionFoundForEmailAndIdentifier".equalsIgnoreCase(code)) {
                DocumentActionUrlTechCertVO documentActionUrlTechCertVO = new DocumentActionUrlTechCertVO();
                documentActionUrlTechCertVO.setNoPendingActionFoundForEmailAndIdentifier(true);
                return documentActionUrlTechCertVO;
            }
            else {
                tratarErroTechCert(jsonResponse.getBody().toString(), jsonResponse.getStatus());
                throw new StreamSeiException(jsonResponse.getBody().toString());
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public DocumentActionUrlTechCertVO gerarUrlsAssinaturaNovo(ConfiguracaoGEDVO configuracaoGEDVO, String DocumentId, String flowActionId, DocumentPessoaRSVO documentPessoaRSVO) {
        try {
            if (!configuracaoGEDVO.getHabilitarIntegracaoTechCert()){
                throw new StreamSeiException("Configuração de integração com TechCert não está habilitada.");
            }
            String url = montarUrlPadrao(configuracaoGEDVO)
                    + UteisWebServiceUrl.URL_TECHCERT_DOCUMENTS + "/" + DocumentId + UteisWebServiceUrl.URL_TECHCERT_ACTION_URL;
            String token = montarToken(configuracaoGEDVO);
            HttpResponse<JsonNode> jsonResponse = unirest().post(url)
                    .header(UteisWebServiceUrl.Content_Type, UteisWebServiceUrl.Application_JSON)
                    .header(configuracaoGEDVO.getApikeyTechCert(), token)
                    .body(montarJsonParaGerarUrlsAssinatura(flowActionId, documentPessoaRSVO))
                    .connectTimeout(10_000)
                    .asJson();
            if (jsonResponse.isSuccess() || jsonResponse.getStatus() == 200) {
                Gson gson = new GsonBuilder().create();
                return gson.fromJson(jsonResponse.getBody().toString(), DocumentActionUrlTechCertVO.class);
            } else {
                tratarErroTechCert(jsonResponse.getBody().toString(), jsonResponse.getStatus());
                throw new StreamSeiException(jsonResponse.getBody().toString());
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public UploadRSVO consultarDocumentDownloadSignatures(ConfiguracaoGEDVO configuracaoGEDVO, String documentId) {
        try {
            if (!configuracaoGEDVO.getHabilitarIntegracaoTechCert()){
                throw new StreamSeiException("Configuração de integração com TechCert não está habilitada.");
            }
            String url = montarUrlPadrao(configuracaoGEDVO)
                    + UteisWebServiceUrl.URL_TECHCERT_DOCUMENTS
                    + "/" + documentId
                    + UteisWebServiceUrl.URL_TECHCERT_CONTENT_64
                    + "?type=Signatures";
            String token = montarToken(configuracaoGEDVO);
            HttpResponse<String> response = unirest().get(url)
                    .header("Accept", MediaType.APPLICATION_JSON)
                    .header(configuracaoGEDVO.getApikeyTechCert(), token)
                    .connectTimeout(10_000)
                    .asString();
            if (response.isSuccess() || response.getStatus() == 200) {
                JSONObject resp = new JSONObject(response.getBody());
                return montarUploadParaDownlaod(resp);
            }
            if (response.getStatus() == 422) {
                return consultarDocumentDownload(configuracaoGEDVO, documentId);
            } else {
                tratarErroTechCert(response.getBody().toString(), response.getStatus());
                throw new StreamSeiException(response.getBody().toString());
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public UploadRSVO consultarDocumentDownload(ConfiguracaoGEDVO configuracaoGEDVO, String documentId) {
        try {
            if (!configuracaoGEDVO.getHabilitarIntegracaoTechCert()){
                throw new StreamSeiException("Configuração de integração com TechCert não está habilitada.");
            }
            String url = montarUrlPadrao(configuracaoGEDVO)
                    + UteisWebServiceUrl.URL_TECHCERT_DOCUMENTS
                    + "/" + documentId
                    + UteisWebServiceUrl.URL_TECHCERT_CONTENT_64;
            String token = montarToken(configuracaoGEDVO);
            HttpResponse<String> response = unirest().get(url)
                    .header("Accept", MediaType.APPLICATION_JSON)
                    .header(configuracaoGEDVO.getApikeyTechCert(), token)
                    .connectTimeout(10_000)
                    .asString();
            if (response.isSuccess() || response.getStatus() == 200) {
                JSONObject resp = new JSONObject(response.getBody());
                return montarUploadParaDownlaod(resp);
            }
            else {
                tratarErroTechCert(response.getBody().toString(), response.getStatus());
                throw new StreamSeiException(response.getBody().toString());
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public JSONArray listarPastas(ConfiguracaoGEDVO configuracaoGEDVO, Map<String, String> filtro) {
        try {
            if (!configuracaoGEDVO.getHabilitarIntegracaoTechCert()){
                throw new StreamSeiException("Configuração de integração com TechCert não está habilitada.");
            }
            HttpResponse<String> jsonResponse = unirest()
                    .get(montarUrlListarPastas(configuracaoGEDVO, filtro))
                    .header(UteisWebServiceUrl.Content_Type, UteisWebServiceUrl.Application_JSON)
                    .header(configuracaoGEDVO.getApikeyTechCert(), montarToken(configuracaoGEDVO))
                    .connectTimeout(10000)
                    .asString();
            JSONObject responseObj = new JSONObject(jsonResponse.getBody());
            InfoWSVO rep = new Gson().fromJson(jsonResponse.getBody().toString(), InfoWSVO.class);
            tratarMensagemErroWebService(rep, String.valueOf(jsonResponse.getStatus()), jsonResponse.getBody());
            return responseObj.optJSONArray("items") != null
                    ? responseObj.getJSONArray("items")
                    : new JSONArray();
        } catch (Exception e) {
            throw e;
        }
    }

    private JSONObject persistirPasta(ConfiguracaoGEDVO configuracaoGEDVO, String name, String parentId) {
        try {
            if (!configuracaoGEDVO.getHabilitarIntegracaoTechCert()){
                throw new StreamSeiException("Configuração de integração com TechCert não está habilitada.");
            }
            JSONObject body = new JSONObject()
                    .put("name", name)
                    .put("parentId", parentId);
            HttpResponse<String> resp = unirest().post(montarUrlPadrao(configuracaoGEDVO) + UteisWebServiceUrl.URL_TECHCERT_FOLDERS)
                    .header(UteisWebServiceUrl.Content_Type, UteisWebServiceUrl.Application_JSON)
//                    .header(configuracaoGEDVO.getApikeyTechCert(), montarToken(configuracaoGEDVO))
                    .body(body.toString())
                    .connectTimeout(10000)
                    .asString();
            InfoWSVO rep = new Gson().fromJson(resp.getBody().toString(), InfoWSVO.class);
            tratarMensagemErroWebService(rep, String.valueOf(resp.getStatus()), resp.getBody());
            return new JSONObject(resp.getBody());
        } catch (Exception e) {
            throw e;
        }
    }

    private UploadsTechCertVO uploadBytes(ConfiguracaoGEDVO configuracaoGEDVO, byte[] arquivo) {
        try {
            if (!configuracaoGEDVO.getHabilitarIntegracaoTechCert()){
                throw new StreamSeiException("Configuração de integração com TechCert não está habilitada.");
            }
            StringBuilder url = new StringBuilder();
            url.append(montarUrlPadrao(configuracaoGEDVO)).append(UteisWebServiceUrl.URL_TECHCERT_UPLOADS_BYTES);
            JSONObject body = new JSONObject()
                    .put("bytes", Base64.getEncoder().encodeToString(arquivo));
            HttpResponse<JsonNode> jsonResponse = unirest().post(url.toString())
                    .header(UteisWebServiceUrl.Content_Type, UteisWebServiceUrl.Application_JSON)
                    .header(configuracaoGEDVO.getApikeyTechCert(), montarToken(configuracaoGEDVO))
                    .body(body.toString())
                    .connectTimeout(10000)
                    .asJson();
            if (jsonResponse.isSuccess() || jsonResponse.getStatus() == 200) {
                Gson gson = new GsonBuilder().create();
                return gson.fromJson(jsonResponse.getBody().toString(), UploadsTechCertVO.class);
            } else {
                tratarErroTechCert(jsonResponse.getBody().toString(), jsonResponse.getStatus());
                throw new StreamSeiException(jsonResponse.getBody().toString());
            }
        } catch (Exception e) {
            throw e;
        }
    }

    private DocumentsCreateTechCertVO[] persistirDocuments(DocumentoAssinadoVO documentoAssinadoVO, ConfiguracaoGEDVO configuracaoGEDVO, IntegracaoTechCertVO integracaoTechCertVO) {
        try {
            if (!configuracaoGEDVO.getHabilitarIntegracaoTechCert()){
                throw new StreamSeiException("Configuração de integração com TechCert não está habilitada.");
            }
            StringBuilder url = new StringBuilder();
            url.append(montarUrlPadrao(configuracaoGEDVO)).append(UteisWebServiceUrl.URL_TECHCERT_DOCUMENTS);

            HttpResponse<JsonNode> jsonResponse = unirest().post(url.toString())
                    .header(UteisWebServiceUrl.Content_Type, UteisWebServiceUrl.Application_JSON)
                    .header(configuracaoGEDVO.getApikeyTechCert(), montarToken(configuracaoGEDVO))
                    .body(montarDocuments(configuracaoGEDVO, documentoAssinadoVO, integracaoTechCertVO).toString())
                    .connectTimeout(10000)
                    .asJson();
            if (jsonResponse.isSuccess() || jsonResponse.getStatus() == 200) {
                Gson gson = new GsonBuilder().create();
                return gson.fromJson(jsonResponse.getBody().toString(), DocumentsCreateTechCertVO[].class);
            } else {
                tratarErroTechCert(jsonResponse.getBody().toString(), jsonResponse.getStatus());
                throw new StreamSeiException(jsonResponse.getBody().toString());
            }
        } catch (Exception e) {
            throw e;
        }
    }

    private JSONArray persistirMoverDocuments(String FolderId, String newFolderName, List<String> documentId, ConfiguracaoGEDVO configuracaoGEDVO) {
        try {
            if (!configuracaoGEDVO.getHabilitarIntegracaoTechCert()){
                throw new StreamSeiException("Configuração de integração com TechCert não está habilitada.");
            }
            StringBuilder url = new StringBuilder();
            url.append(montarUrlPadrao(configuracaoGEDVO))
                    .append(UteisWebServiceUrl.URL_TECHCERT_DOCUMENTS)
                    .append(UteisWebServiceUrl.URL_TECHCERT_BATCH_FOLDER);
            HttpResponse<String> resp = unirest().post(url.toString())
                    .header(UteisWebServiceUrl.Content_Type, UteisWebServiceUrl.Application_JSON)
                    .header(configuracaoGEDVO.getApikeyTechCert(), montarToken(configuracaoGEDVO))
                    .body(montarMoverDocuments(documentId, FolderId, newFolderName).toString())
                    .connectTimeout(10000)
                    .asString();
            JSONArray array = new JSONArray(resp.getBody());
            JSONObject firstObj = array.getJSONObject(0);
            InfoWSVO rep = new Gson().fromJson(firstObj.toString(), InfoWSVO.class);
            tratarMensagemErroWebService(rep, String.valueOf(resp.getStatus()), resp.getBody());
            return new JSONArray(resp.getBody());
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Monta a URL completa para listar pastas, incluindo query string
     * a partir dos parâmetros em 'filtro' (query, limit, offset, etc).
     */
    private String montarUrlListarPastas(ConfiguracaoGEDVO configuracaoGEDVO, Map<String, String> filtro) {
        StringBuilder uri = new StringBuilder(montarUrlPadrao(configuracaoGEDVO))
                .append(UteisWebServiceUrl.URL_TECHCERT_FOLDERS);

        if (Uteis.isAtributoPreenchido(filtro)) {
            uri.append("?");
            for (Map.Entry<String, String> entry : filtro.entrySet()) {
                try {
                    String keyEnc;
                    String valEnc;

                    keyEnc = URLEncoder.encode(entry.getKey(), "UTF-8");
                    valEnc = URLEncoder.encode(entry.getValue(), "UTF-8");

                    uri.append(keyEnc)
                            .append("=")
                            .append(valEnc)
                            .append("&");
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException("Falha ao fazer URL-encode UTF-8 durante Integração TechCert", e);
                }
            }
            // remove o último '&'
            uri.setLength(uri.length() - 1);
        }

        return uri.toString();
    }

    private String montarUrlPadrao(ConfiguracaoGEDVO configuracaoGEDVO) {
//        return configuracaoGEDVO.getAmbienteTechCertEnum().isProducao()
//                ? configuracaoGEDVO.getUrlIntegracaoTechCertProducao()
//                : configuracaoGEDVO.getUrlIntegracaoTechCertHomologacao();
    	
    	return null;
    }

//    private String montarToken(ConfiguracaoGEDVO configuracaoGEDVO) {
//        return configuracaoGEDVO.getAmbienteTechCertEnum().isProducao()
//                ? configuracaoGEDVO.getTokenTechCert()
//                : configuracaoGEDVO.getTokenTechCertHomologacao();
//    }

    private JSONObject montarJsonParaGerarUrlsAssinaturaPorFlowAction(FlowActionsTechCertVO flowAction) {
        // Caso Signer (campo user preenchido)
        if (flowAction.getUser() != null) {
            JSONObject json = new JSONObject();
            json.put("identifier", Uteis.retirarMascaraCPF(flowAction.getUser().getIdentifier()));
            json.put("emailAddress", flowAction.getUser().getEmail());
            json.put("requireEmailAuthentication", false);
            json.put("flowActionId", flowAction.getId());
            return json;
        }
        return null;
    }

    private JSONObject montarJsonParaGerarUrlsAssinaturaPorFlowActionSignRule(String identifier, String email, String flowActionId) {
        JSONObject json = new JSONObject();
        json.put("identifier", Uteis.retirarMascaraCPF(identifier));
        json.put("emailAddress",email);
        json.put("requireEmailAuthentication", false);
        json.put("flowActionId", flowActionId);
        return json;
    }


    private JSONObject montarJsonParaGerarUrlsAssinatura(String flowActionId, DocumentPessoaRSVO documentPessoaRSVO) {
        JSONObject json = new JSONObject();
        json.put("identifier", Uteis.retirarMascaraCPF(documentPessoaRSVO.getIdentifier()));
        json.put("emailAddress", documentPessoaRSVO.getEmail());
        json.put("requireEmailAuthentication", false);
        json.put("flowActionId", flowActionId);
        return json;
    }

    public JSONObject montarDocuments(ConfiguracaoGEDVO configuracaoGEDVO, DocumentoAssinadoVO documentoAssinadoVO, IntegracaoTechCertVO integracaoTechCertVO) {
//        if (documentoAssinadoVO.getTipoOrigemDocumentoAssinadoEnum() == TipoOrigemDocumentoAssinadoEnum.ATA_COLACAO_GRAU) {
//            return montarDocumentsAtaColacaoDeGrau(configuracaoGEDVO, documentoAssinadoVO, integracaoTechCertVO);
//        }
//        else {
            JSONObject root = new JSONObject();

//            if (configuracaoGEDVO.getAmbienteTechCertEnum() == AmbienteEnum.HOMOLOGACAO){
//                root.put("disablePendingActionNotifications", true);
//                root.put("disableNotifications", true);
//            }

            JSONArray filesArray = new JSONArray();
            JSONObject fileObj = new JSONObject()
                    .put("displayName", documentoAssinadoVO.getArquivo().getNome())
                    .put("id", integracaoTechCertVO.getFileCodigo())
                    .put("name", documentoAssinadoVO.getArquivo().getNome())
                    .put("contentType", obterContentType(documentoAssinadoVO.getArquivo().getTipoRelatorio()));
            filesArray.put(fileObj);
            root.put("files", filesArray);

        // Segundo a documentação da techcert:
        // Qualquer informação de horário será descartada, pois a validade será definida para
        // o último horário disponível para a data escolhida no fuso horário padrão
        if (documentoAssinadoVO.getTipoOrigemDocumentoAssinadoEnum() == TipoOrigemDocumentoAssinadoEnum.ATA_COLACAO_GRAU) {
            String date = integracaoTechCertVO.getExpirationDate().format(DateTimeFormatter.ISO_LOCAL_DATE);
            root.put("expirationDate", date);
        }

            Boolean allowElectronicSignature =
                    TipoProvedorAssinaturaEnum.ASSINATURA_ELETRONICA == configuracaoGEDVO.getTipoProvedorAssinaturaTechCertEnum();
            JSONArray actionsArray = new JSONArray();

            if (Uteis.isAtributoPreenchido(integracaoTechCertVO.getSigners())) {
                integracaoTechCertVO.getSigners().forEach(signer -> {
                    JSONObject userObj = new JSONObject()
                            .put("name", signer.getName())
                            .put("identifier", Uteis.removerMascara(signer.getIndividualIdentificationCode()))
                            .put("email", signer.getEmail());

                    JSONObject actionObj = new JSONObject();
                    actionObj
                            .put("type", "Signer")
                            .put("step", signer.getStep())
                            .put("title", signer.getTipoPessoa().getDescricao())
                            .put("allowElectronicSignature", allowElectronicSignature);
                    permissoesDocumentsFlowsTechCert(actionObj);
                    actionObj.put("user", userObj);
                    actionsArray.put(actionObj);
                });
            }

            if (Uteis.isAtributoPreenchido(integracaoTechCertVO.getElectronicSigners())) {
                integracaoTechCertVO.getElectronicSigners().forEach(electronicSigner -> {
                    JSONObject userObj = new JSONObject()
                            .put("name", electronicSigner.getName())
                            .put("identifier", Uteis.removerMascara(electronicSigner.getIndividualIdentificationCode()))
                            .put("email", electronicSigner.getEmail());

                    JSONObject actionObj = new JSONObject();
                    actionObj
                            .put("type", "Signer")
                            .put("step", electronicSigner.getStep())
                            .put("title", electronicSigner.getTipoPessoa().getDescricao())
                            .put("allowElectronicSignature", allowElectronicSignature);
                    permissoesDocumentsFlowsTechCert(actionObj);
                    actionObj.put("user", userObj);
                    actionsArray.put(actionObj);
                });
            }

            root.put("flowActions", actionsArray);
            JSONArray tags = tagsPorTipoOrigemDocumentoAssinado(documentoAssinadoVO, integracaoTechCertVO);
            if (tags != null) {
                root.put("tags", tags);
            }
            return root;
//        }
    }

    public JSONObject montarDocumentsAtaColacaoDeGrau(ConfiguracaoGEDVO configuracaoGEDVO, DocumentoAssinadoVO documentoAssinadoVO, IntegracaoTechCertVO integracaoTechCertVO) {
        JSONObject root = new JSONObject();

//        if (configuracaoGEDVO.getAmbienteTechCertEnum() == AmbienteEnum.HOMOLOGACAO){
//            root.put("disablePendingActionNotifications", true);
//            root.put("disableNotifications", true);
//        }

        // 1) Arquivos
        JSONArray filesArray = new JSONArray();
        JSONObject fileObj = new JSONObject()
                .put("displayName", documentoAssinadoVO.getArquivo().getNome())
                .put("id", integracaoTechCertVO.getFileCodigo())
                .put("name", documentoAssinadoVO.getArquivo().getNome())
                .put("contentType", obterContentType(documentoAssinadoVO.getArquivo().getTipoRelatorio()));
        filesArray.put(fileObj);
        root.put("files", filesArray);

        // Expiração e ordenação
        root.put("expirationDate", integracaoTechCertVO.getExpirationDate().toString());

        // Com a regra abaixo ele obriga todos os assinantes assinar
        //        root.put("areActionsOrdered", true);

        boolean allowElectronic =
                TipoProvedorAssinaturaEnum.ASSINATURA_ELETRONICA
                        == configuracaoGEDVO.getTipoProvedorAssinaturaTechCertEnum();

        List<DocumentPessoaRSVO> manualSigners = Uteis.isAtributoPreenchido(integracaoTechCertVO.getSigners())
                ? integracaoTechCertVO.getSigners()
                : Collections.emptyList();
        List<DocumentPessoaRSVO> electronicSigners = Uteis.isAtributoPreenchido(integracaoTechCertVO.getElectronicSigners())
                ? integracaoTechCertVO.getElectronicSigners()
                : Collections.emptyList();

        // Ações individuais para (não-alunos)
        JSONArray actionsArray = new JSONArray();
        JSONArray alunoUsers = new JSONArray();
        for (DocumentPessoaRSVO signer : manualSigners) {
            if (!TipoPessoa.ALUNO.getDescricao().equalsIgnoreCase(signer.getTipoPessoa().getDescricao())) {
                JSONObject user = new JSONObject()
                        .put("name", signer.getName())
                        .put("identifier", Uteis.removerMascara(signer.getIndividualIdentificationCode()))
                        .put("email", signer.getEmail());

                JSONObject userAction = new JSONObject()
                        .put("step", signer.getStep())
                        .put("type", "Signer")
                        .put("user", user)
                        .put("title", signer.getTipoPessoa().getDescricao())
                        .put("allowElectronicSignature", allowElectronic);
                permissoesDocumentsFlowsTechCert(userAction);
                actionsArray.put(userAction);
            }
            if (TipoPessoa.ALUNO.getDescricao().equalsIgnoreCase(signer.getTipoPessoa().getDescricao())) {
                JSONObject userSignRule = new JSONObject()
                        .put("name", signer.getName())
                        .put("identifier", Uteis.removerMascara(signer.getIndividualIdentificationCode()))
                        .put("email", signer.getEmail());
                alunoUsers.put(userSignRule);
            }
        }

        for (DocumentPessoaRSVO signer : electronicSigners) {
            if (!TipoPessoa.ALUNO.getDescricao().equalsIgnoreCase(signer.getTipoPessoa().getDescricao())) {
                JSONObject user = new JSONObject()
                        .put("name", signer.getName())
                        .put("identifier", Uteis.removerMascara(signer.getIndividualIdentificationCode()))
                        .put("email", signer.getEmail());

                JSONObject userAction = new JSONObject()
                        .put("step", signer.getStep())
                        .put("type", "Signer")
                        .put("user", user)
                        .put("allowElectronicSignature", allowElectronic);
                permissoesDocumentsFlowsTechCert(userAction);
                actionsArray.put(userAction);
            }
            if (TipoPessoa.ALUNO.getDescricao().equalsIgnoreCase(signer.getTipoPessoa().getDescricao())) {
                JSONObject userSignRule = new JSONObject()
                        .put("name", signer.getName())
                        .put("identifier", Uteis.removerMascara(signer.getIndividualIdentificationCode()))
                        .put("email", signer.getEmail());
                alunoUsers.put(userSignRule);
            }
        }

        if (alunoUsers.length() > 0) {
            // determinar o step do SignRule: primeiro aluno encontrado
            int ruleStep = 1;
            if (!manualSigners.isEmpty()) {
                for (DocumentPessoaRSVO s : manualSigners) {
                    if (TipoPessoa.ALUNO.getDescricao().equalsIgnoreCase(s.getTipoPessoa().getDescricao())) {
                        ruleStep = s.getStep();
                        break;
                    }
                }
            }
            if (ruleStep == 1 && !electronicSigners.isEmpty()) {
                for (DocumentPessoaRSVO s : electronicSigners) {
                    if (TipoPessoa.ALUNO.getDescricao().equalsIgnoreCase(s.getTipoPessoa().getDescricao())) {
                        ruleStep = s.getStep();
                        break;
                    }
                }
            }

            int minimoAtual = alunoUsers.length() - minimoAssinantes;
            JSONObject ruleAction = new JSONObject()
                    .put("step", ruleStep)
                    .put("type", "SignRule")
                    .put("title", "Aluno")
                    .put("signRuleUsers", alunoUsers)
                    .put("numberRequiredSignatures", minimoAssinantes)
                    .put("ruleName", "Pelo menos (" + minimoAtual + " de " + alunoUsers.length() + " )")
                    .put("allowRuleFlowToContinueIfRefused", true)
                    .put("allowElectronicSignature", allowElectronic);
            permissoesDocumentsFlowsTechCert(ruleAction);
            actionsArray.put(ruleAction);
        }

        root.put("flowActions", actionsArray);
        JSONArray tags = tagsPorTipoOrigemDocumentoAssinado(documentoAssinadoVO, integracaoTechCertVO);

        if (tags != null) {
            root.put("tags", tags);
        }

        return root;
    }

    public JSONObject permissoesDocumentsFlowsTechCert(JSONObject object) {
        object.put("requireEmailAuthenticationToSignElectronically", false)
                .put("requireSmsAuthenticationToSignElectronically", false)
                .put("requireWhatsappAuthenticationToSignElectronically", false)
                .put("requireAuthenticatorAppToSignElectronically", false)
                .put("requireSelfieAuthenticationToSignElectronically", false)
                .put("requireDatavalidAuthenticationToSignElectronically", false)
                .put("requirePixAuthenticationToSignElectronically", false)
                .put("requireLivenessAuthenticationToSignElectronically", false)
                .put("requireIdScanAuthenticationToSignElectronically", false)
                .put("disableEmailAuthenticationToSignElectronically", true);
        return object;
    }

    @Override
    public AddedFlowActionTechCertVO permissoesDocumentsFlowsTechCert(AddedFlowActionTechCertVO addedFlowActionTechCertVO) {
        addedFlowActionTechCertVO.setRequireEmailAuthenticationToSignElectronically(false);
        addedFlowActionTechCertVO.setRequireSmsAuthenticationToSignElectronically(false);
        addedFlowActionTechCertVO.setRequireWhatsappAuthenticationToSignElectronically(false);
        addedFlowActionTechCertVO.setRequireAuthenticatorAppToSignElectronically(false);
        addedFlowActionTechCertVO.setRequireSelfieAuthenticationToSignElectronically(false);
        addedFlowActionTechCertVO.setRequireDatavalidAuthenticationToSignElectronically(false);
        addedFlowActionTechCertVO.setRequirePixAuthenticationToSignElectronically(false);
        addedFlowActionTechCertVO.setRequireLivenessAuthenticationToSignElectronically(false);
        addedFlowActionTechCertVO.setRequireIdScanAuthenticationToSignElectronically(false);
        addedFlowActionTechCertVO.setDisableEmailAuthenticationToSignElectronically(true);
        return addedFlowActionTechCertVO;
    }

    private JSONArray tagsPorTipoOrigemDocumentoAssinado(DocumentoAssinadoVO documentoAssinadoVO, IntegracaoTechCertVO integracaoTechCertVO) {
        switch (documentoAssinadoVO.getTipoOrigemDocumentoAssinadoEnum()) {
            case CONTRATO:
                return tagsContratoAndExpedicaoDoDiploma(integracaoTechCertVO);
            case ESTAGIO:
                return tagsEstagio(integracaoTechCertVO);
            case TERMO_ESTAGIO_OBRIGATORIO:
                return tagsEstagio(integracaoTechCertVO);
            case TERMO_ESTAGIO_NAO_OBRIGATORIO:
                return tagsEstagio(integracaoTechCertVO);
            case TERMO_ADITIVO_ESTAGIO_NAO_OBRIGATORIO:
                return tagsEstagio(integracaoTechCertVO);
            case TERMO_RESCISAO_ESTAGIO_NAO_OBRIGATORIO:
                return tagsEstagio(integracaoTechCertVO);
            case ATA_COLACAO_GRAU:
                return tagsAtaDeColacaoDeGrau(integracaoTechCertVO);
            case EXPEDICAO_DIPLOMA:
                return tagsContratoAndExpedicaoDoDiploma(integracaoTechCertVO);
            default:
                // Outros tipos de documentos não possuem tags específicas
                return null;
        }
    }

    private JSONArray tagsContratoAndExpedicaoDoDiploma(IntegracaoTechCertVO integracaoTechCertVO) {
        JSONArray tags = new JSONArray();
        // (Registro Acadêmico que fica no cadastro do ano
        if (Uteis.isAtributoPreenchido(integracaoTechCertVO.getRa())) {
            tags.put(labelValueTags("RA", integracaoTechCertVO.getRa()));
        }
        // Nome do aluno
        if (Uteis.isAtributoPreenchido(integracaoTechCertVO.getNomeAluno())) {
            tags.put(labelValueTags("Aluno", integracaoTechCertVO.getNomeAluno()));
        }
        // matricula do aluno
        if (Uteis.isAtributoPreenchido(integracaoTechCertVO.getMatricula())) {
            tags.put(labelValueTags("Matrícula", integracaoTechCertVO.getMatricula()));
        }
        // Curso (Nome do Curso)
        if (Uteis.isAtributoPreenchido(integracaoTechCertVO.getNomeCurso())) {
            tags.put(labelValueTags("Curso", integracaoTechCertVO.getNomeCurso()));
        }
        // Polo (Nome da unidade de ensino)
        if (Uteis.isAtributoPreenchido(integracaoTechCertVO.getNomePolo())) {
            tags.put(labelValueTags("Polo", integracaoTechCertVO.getNomePolo()));
        }
        // Ano (ano da matricula periodo)
        if (Uteis.isAtributoPreenchido(integracaoTechCertVO.getAno())) {
            tags.put(labelValueTags("Ano", integracaoTechCertVO.getAno()));
        }
        // Semestre (semestre da matricula periodo)
        if (Uteis.isAtributoPreenchido(integracaoTechCertVO.getSemestre())) {
            tags.put(labelValueTags("Semestre", integracaoTechCertVO.getSemestre()));
        }
        return tags;
    }

    private JSONArray tagsEstagio(IntegracaoTechCertVO integracaoTechCertVO) {
        JSONArray tags = new JSONArray();
        // (Registro Acadêmico que fica no cadastro do ano
        if (Uteis.isAtributoPreenchido(integracaoTechCertVO.getRa())) {
            tags.put(labelValueTags("RA", integracaoTechCertVO.getRa()));
        }
        // Nome do aluno
        if (Uteis.isAtributoPreenchido(integracaoTechCertVO.getNomeAluno())) {
            tags.put(labelValueTags("Aluno", integracaoTechCertVO.getNomeAluno()));
        }
        // matricula do aluno
        if (Uteis.isAtributoPreenchido(integracaoTechCertVO.getMatricula())) {
            tags.put(labelValueTags("Matrícula", integracaoTechCertVO.getMatricula()));
        }
        // Curso (Nome do Curso)
        if (Uteis.isAtributoPreenchido(integracaoTechCertVO.getNomeCurso())) {
            tags.put(labelValueTags("Curso", integracaoTechCertVO.getNomeCurso()));
        }
        // Polo (Nome da unidade de ensino)
        if (Uteis.isAtributoPreenchido(integracaoTechCertVO.getNomePolo())) {
            tags.put(labelValueTags("Polo", integracaoTechCertVO.getNomePolo()));
        }
        // Ano (ano da matricula periodo)
        if (Uteis.isAtributoPreenchido(integracaoTechCertVO.getAno())) {
            tags.put(labelValueTags("Ano", integracaoTechCertVO.getAno()));
        }
        // Semestre (semestre da matricula periodo)
        if (Uteis.isAtributoPreenchido(integracaoTechCertVO.getSemestre())) {
            tags.put(labelValueTags("Semestre", integracaoTechCertVO.getSemestre()));
        }
        // Estágio (nome do componente de estágio)
        if (Uteis.isAtributoPreenchido(integracaoTechCertVO.getEstagioNome())) {
            tags.put(labelValueTags("Estágio", integracaoTechCertVO.getEstagioNome()));
        }
        // Concedente (nome da concedente do estágio), Responsável
        if (Uteis.isAtributoPreenchido(integracaoTechCertVO.getConcedenteDoEstagioNome())) {
            tags.put(labelValueTags("Concedente ", integracaoTechCertVO.getConcedenteDoEstagioNome()));
        }
        // Concedente (Nome do Responsável do Concedente)
        if (Uteis.isAtributoPreenchido(integracaoTechCertVO.getResponsavelDoConcendenteNome())) {
            tags.put(labelValueTags("Responsável Concedente ", integracaoTechCertVO.getResponsavelDoConcendenteNome()));
        }
        return tags;
    }

    private JSONArray tagsAtaDeColacaoDeGrau(IntegracaoTechCertVO integracaoTechCertVO) {
        JSONArray tags = new JSONArray();
        if (Uteis.isAtributoPreenchido(integracaoTechCertVO.getNomeCurso())) {
            tags.put(labelValueTags("Curso", integracaoTechCertVO.getNomeCurso()));
        }
        if (Uteis.isAtributoPreenchido(integracaoTechCertVO.getTituloColacaoGrau())) {
            tags.put(labelValueTags("Colação de Grau", integracaoTechCertVO.getTituloColacaoGrau()));
        }
        if (Uteis.isAtributoPreenchido(integracaoTechCertVO.getInfoAtaColacaoGrau())) {
            tags.put(labelValueTags("Ata da Colação", integracaoTechCertVO.getInfoAtaColacaoGrau()));
        }
        if (Uteis.isAtributoPreenchido(integracaoTechCertVO.getDataColacaoGrau())) {
            Date data = integracaoTechCertVO.getDataColacaoGrau();
            String dataFormatada = new SimpleDateFormat("dd/MM/yyyy").format(data);
            tags.put(labelValueTags("Data Colação", dataFormatada));
        }
        return tags;
    }

    private JSONObject labelValueTags(String label, String value) {
        JSONObject tag = new JSONObject();
        if (Uteis.isAtributoPreenchido(label)
                && Uteis.isAtributoPreenchido(value)) {
            tag.put("label", label).put("value", value);
            return tag;
        }
        return tag;
    }

    public static JSONObject montarMoverDocuments(List<String> documentIds, String folderId, String newFolderName) {
        JSONObject payload = new JSONObject();
        JSONArray docsArray = new JSONArray();
        for (String id : documentIds) {
            docsArray.put(id);
        }
        payload.put("documents", docsArray);
        payload.put("folderId", folderId != null ? folderId : "");
        if (newFolderName != null && !newFolderName.trim().isEmpty()) {
            payload.put("newFolderName", newFolderName);
        }
        return payload;
    }

    public static String obterContentType(TipoRelatorioEnum tipo) {
        if (!Uteis.isAtributoPreenchido(tipo)) {
            throw new IllegalArgumentException("TipoRelatorioEnum não pode ser nulo");
        }
        switch (tipo) {
            case PDF:
                return "application/pdf";
            case EXCEL:
                return "application/vnd.ms-excel";
            case DOC:
                return "application/msword";
            case HTML:
                return "text/html";
            case PPT:
                return "application/vnd.ms-powerpoint";
            case IMAGEM:
                return "image/jpeg";
            case XML:
                return "application/xml";
            default:
                throw new IllegalArgumentException("TipoRelatorioEnum não suportado: " + tipo);
        }
    }

    protected void tratarMensagemErroWebService(InfoWSVO resp, String status, String mensagemRetorno) throws StreamSeiException {
        StringBuilder mensagem = new StringBuilder();
        if (resp != null && resp.getCodigo() != 0) {
            mensagem.append(resp.getMensagem() + " - " + resp.getMensagemCampos());
        } else if (resp != null && resp.getStatus() != 0) {
            mensagem.append(resp.getMessage());
        } else {
            mensagem.append(mensagemRetorno);
        }
        if (status.contains("400")) {
            throw new StreamSeiException("A requisição é inválida, em geral conteúdo malformado. " + mensagem);
        }
        if (status.contains("401")) {
            throw new StreamSeiException("O usuário e senha ou token de acesso são inválidos. " + mensagem);
        }
        if (status.contains("403")) {
            throw new StreamSeiException("O acesso à API está bloqueado ou o usuário está bloqueado. " + mensagem);
        }
        if (status.contains("404")) {
            throw new StreamSeiException("O endereço acessado não existe. " + mensagem);
        }
        if (status.contains("406")) {
            throw new StreamSeiException("Acesso não autorizado não autorizado para o recurso. " + mensagem);
        }
        if (status.contains("413")) {
            throw new StreamSeiException("A solicitação é muito grande A solicitação está excedendo o limite permitido para o seu perfil de token de acesso. " + mensagem);
        }
        if (status.contains("422")) {
            throw new StreamSeiException("A Requisição é válida, mas os dados passados não são válidos. " + mensagem);
        }
        if (status.contains("429")) {
            throw new StreamSeiException("O usuário atingiu o limite de requisições. " + mensagem);
        }
        if (status.contains("500")) {
            throw new StreamSeiException("Houve um erro interno do servidor ao processar a requisição. " + mensagem);
        }
    }

    public static String tratarErroTechCert(String jsonErro, Integer codeResponse) {
        ObjectMapper mapper = new ObjectMapper();
        StringBuilder resultado = new StringBuilder();
        try {
            com.fasterxml.jackson.databind.JsonNode root = mapper.readTree(jsonErro);
            //TRATAMENTO DE MENSAGEM COM CODE
            if (codeResponse == 422) {
                if(root.isArray()){
                    for (com.fasterxml.jackson.databind.JsonNode node : root) {
                        tratarErroTechCert422(node, resultado);
                    }
                }else{
                    tratarErroTechCert422(root, resultado);
                }
            }
            //TRATAMENTO DE MENSAGEM COM ERRO DE VALORES
            else if (codeResponse == 400 && root.has("errors")) {
                com.fasterxml.jackson.databind.JsonNode errors = root.get("errors");
                if (errors.has("ExpirationDate")) {
                    resultado.append("A data de expiração deve ser posterior à data e hora atuais.");
                } else {
                    for (Iterator<String> it = errors.fieldNames(); it.hasNext(); ) {
                        String campo = it.next();
                        com.fasterxml.jackson.databind.JsonNode mensagens = errors.get(campo);
                        for (com.fasterxml.jackson.databind.JsonNode mensagem : mensagens) {
                            if (resultado.length() > 0) {
                                resultado.append(System.lineSeparator());
                            }
                            resultado.append(campo).append(": ").append(mensagem.asText());
                        }
                    }
                }
                throw new StreamSeiException(resultado.toString());
            } else {
                // Outros erros: dispara tratamento genérico
                tratarMensagemErroWebService(codeResponse.toString());
            }
        } catch (IOException e) {
            return "Erro desconhecido na integração TechCert";
        } catch (ConsistirException e) {
            throw new RuntimeException(e);
        }
        return resultado.toString();
    }

    private static void tratarErroTechCert422(com.fasterxml.jackson.databind.JsonNode node, StringBuilder resultado) throws ConsistirException {
        String code = node.path("code").asText(null);
        String messageApi = node.path("message").asText(null);
        StringBuilder detalhes = new StringBuilder();
        if (messageApi != null && !messageApi.trim().isEmpty()) {
            detalhes.append(messageApi.trim());
        }
        com.fasterxml.jackson.databind.JsonNode details = node.path("details");
        String email = details.path("email").asText(null);
        String identifier = details.path("identifier").asText(null);
        if (Uteis.isAtributoPreenchido(email) && Uteis.isAtributoPreenchido(identifier)) {
        	detalhes.setLength(0);
            detalhes.append("O e-mail ").append(email)
            .append(" está sendo usado por outro estágio porém com o CPF diferente de ").append(Uteis.adicionarMascaraCPF(identifier))
            .append(". Deve ser informado um e-mail que pertença ao mesmo CPF.");
        } else if (Uteis.isAtributoPreenchido(email)) {
        	detalhes.setLength(0);
            detalhes.append("O e-mail ").append(email)
            .append(" está sendo usado por outro estágio porém com o CPF diferente");
        }
        String amigavel = tratamentoMensagemIntegracaoTechCert(code, detalhes.toString());
        if (resultado.length() > 0) {
            resultado.append(System.lineSeparator());
        }
        resultado.append(amigavel);
    }

    private static String tratamentoMensagemIntegracaoTechCert(String code, String messageAdicional) throws ConsistirException {
        String codeNorm = (code != null) ? code.toUpperCase().trim() : "";
        String detalhe = (messageAdicional != null && !messageAdicional.trim().isEmpty()) ? messageAdicional.trim() : "";
        switch (codeNorm) {
            case "EMAILALREADYBEINGUSED":
                throw new ConsistirException(Uteis.isAtributoPreenchido(detalhe) ? detalhe : "O email informado já está em uso");
            case "CERTIFICATENOTFOUND":
                throw new ConsistirException(Uteis.isAtributoPreenchido(detalhe) ? detalhe : "Certificado não encontrado");
            case "FOLDERNOTFOUND":
                throw new ConsistirException(Uteis.isAtributoPreenchido(detalhe) ? detalhe : "Pasta não encontrada");
            case "CPFMISMATCH":
                throw new ConsistirException(Uteis.isAtributoPreenchido(detalhe) ? detalhe : "O CPF informado não corresponde ao esperado");
            case "CPFNOTEXPECTED":
                throw new ConsistirException(Uteis.isAtributoPreenchido(detalhe) ? detalhe : "CPF não é o esperado para este documento");
            case "INVALIDFLOWACTION":
                throw new ConsistirException(Uteis.isAtributoPreenchido(detalhe) ? detalhe : "Ação de fluxo inválida");
            case "DOCUMENTINVALIDKEY":
                throw new ConsistirException(Uteis.isAtributoPreenchido(detalhe) ? detalhe : "Chave do documento inválida");
            case "FLOWACTIONNOTFOUND":
                throw new ConsistirException(Uteis.isAtributoPreenchido(detalhe) ? detalhe : "Fluxo de assinaturas não encontrado");
            case "CANNOTCANCELEXPIREDDOCUMENT":
                throw new ConsistirException(Uteis.isAtributoPreenchido(detalhe) ? detalhe : "Não é possivel Cancelar/Remover Documento Expirado");
            case "CANTADDACTIONSTOCOMPLETEDSTEP":
                throw new ConsistirException(Uteis.isAtributoPreenchido(detalhe) ? detalhe : "Não é possível adicionar ações em uma etapa já concluída");
            default:
                throw new ConsistirException(Uteis.isAtributoPreenchido(detalhe) ? detalhe : "Erro na integração TechCert: código '" + codeNorm + "'");
        }
    }

    @Override
    public void validarCpfEmailParaAssinatura(String email, String cpf, String nome) throws Exception {
        Uteis.checkState(!Uteis.isAtributoPreenchido(email),
                "O campo e-mail/e-mail institucional pessoa deve ser informado para o assinante - " + nome);
        String cpfTratado = Uteis.removerAcentos(Uteis.removeCaractersEspeciais(cpf)).replaceAll(" ", "");
        Uteis.checkState(!Uteis.validaCPF(cpfTratado),
                "O campo cpf para o assinante - " + nome + " não é válido");
        Uteis.checkState(!Uteis.isAtributoPreenchido(nome),
                "O campo nome pessoa deve ser informado para o assinante - " + email);
    }

    public static void tratarMensagemErroWebService(String status) throws ConsistirException {

        if (status.contains("400")) {
            throw new ConsistirException("A requisição é inválida, em geral conteúdo malformado. ");
        }
        if (status.contains("401")) {
            throw new ConsistirException("O usuário e senha ou token de acesso são inválidos. ");
        }
        if (status.contains("403")) {
            throw new ConsistirException("O acesso à API está bloqueado ou o usuário está bloqueado. ");
        }
        if (status.contains("404")) {
            throw new ConsistirException("O endereço acessado não existe. ");
        }
        if (status.contains("406")) {
            throw new ConsistirException("Acesso não autorizado não autorizado para o recurso. ");
        }
        if (status.contains("413")) {
            throw new ConsistirException("A solicitação é muito grande A solicitação está excedendo o limite permitido para o seu perfil de token de acesso. ");
        }
        if (status.contains("422")) {
            throw new ConsistirException("A Requisição é válida, mas os dados passados não são válidos. ");
        }
        if (status.contains("429")) {
            throw new ConsistirException("O usuário atingiu o limite de requisições. ");
        }
        if (status.contains("500")) {
            throw new ConsistirException("Houve um erro interno do servidor ao processar a requisição. ");
        }
    }

    private UploadRSVO montarUploadParaDownlaod(JSONObject jsonObject) {
        UploadRSVO upload = new UploadRSVO();
        upload.setName(jsonObject.getString("name"));
        upload.setType(jsonObject.getString("contentType"));
        upload.setBytes(jsonObject.getString("bytes"));
        return upload;
    }

	@Override
	public String realizarGeracaoPreviewRepresentacaoVisual(DocumentoAssinadoVO documentoAssinado,
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
