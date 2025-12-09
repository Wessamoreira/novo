package negocio.facade.jdbc.financeiro;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import controle.arquitetura.DataModelo;
import jobs.enumeradores.JobsEnum;
import kong.unirest.GetRequest;
import kong.unirest.HttpRequestWithBody;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.RequestBodyEntity;
import kong.unirest.Unirest;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.ContaReceberHistoricoVO;
import negocio.comuns.financeiro.ContaReceberNaoLocalizadaArquivoRetornoVO;
import negocio.comuns.financeiro.ContaReceberNegociacaoRecebimentoVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.ControleRemessaContaReceberVO;
import negocio.comuns.financeiro.ControleRemessaVO;
import negocio.comuns.financeiro.FormaPagamentoNegociacaoRecebimentoVO;
import negocio.comuns.financeiro.MapaPendenciasControleCobrancaVO;
import negocio.comuns.financeiro.NegociacaoRecebimentoVO;
import negocio.comuns.financeiro.PixContaCorrenteVO;
import negocio.comuns.financeiro.enumerador.SituacaoPixEnum;
import negocio.comuns.financeiro.enumerador.StatusPixEnum;
import negocio.comuns.financeiro.enumerador.TipoContaCorrenteEnum;
import negocio.comuns.job.RegistroExecucaoJobVO;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisWebServiceUrl;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.PixContaCorrenteInterfaceFacade;
import webservice.boletoonline.itau.comuns.TokenVO;
import webservice.pix.comuns.AbatimentoPixRSVO;
import webservice.pix.comuns.CalendarioPixRSVO;
import webservice.pix.comuns.DescontoDataFixaPixRSVO;
import webservice.pix.comuns.DescontoPixRSVO;
import webservice.pix.comuns.JuroPixRSVO;
import webservice.pix.comuns.MultaPixRSVO;
import webservice.pix.comuns.PayloadPixRSVO;
import webservice.pix.comuns.PessoaPixRSVO;
import webservice.pix.comuns.PixRSVO;
import webservice.pix.comuns.ValorCobrancaComVencimentoPixRSVO;
import webservice.pix.comuns.WebhookRSVO;

@Repository
@Scope("singleton")
@Lazy
public class PixContaCorrente extends ControleAcesso implements PixContaCorrenteInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9123563199581484147L;
	private static String idEntidade = "PixContaCorrente";

	public static String getIdEntidade() {
		return PixContaCorrente.idEntidade;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(PixContaCorrenteVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) {
		try {
			if (obj.getCodigo() == 0) {
				incluir(obj, verificarAcesso, usuarioVO);
			} else {
				alterar(obj, verificarAcesso, usuarioVO);
			}
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final PixContaCorrenteVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			PixContaCorrente.incluir(getIdEntidade(), verificarAcesso, usuario);
			incluir(obj, "pixContaCorrente", new AtributoPersistencia()					
					.add("dataGeracao", obj.getDataGeracao())
					.add("tempoExpiracao", obj.getTempoExpiracao())
					.add("txid", obj.getTxid())					
					.add("qrCode", obj.getQrCode())
					.add("jsonRetorno", obj.getJsonRetorno())
					.add("jsonEnvio", obj.getJsonEnvio())
					.add("situacaoPixEnum", obj.getSituacaoPixEnum())
					.add("statusPixEnum", obj.getStatusPixEnum())
					.add("nossoNumero", obj.getNossoNumero())
					.add("contaReceber", obj.getContaReceberVO())
					.add("valorContaReceberEnvio", obj.getValorContaReceberEnvio())
					.add("endToEndId", obj.getEndToEndId())
					.add("valor", obj.getValor())
					.add("horario", obj.getHorario())
					.add("infoPagador", obj.getInfoPagador())
					.add("contacorrente", obj.getContaCorrenteVO())
					.add("documentoPagador", obj.getDocumentoPagador())
					.add("nomePagador", obj.getNomePagador())
					.add("motivoCancelamento", obj.getMotivoCancelamento())
					.add("usuario", obj.getUsuarioVO()), usuario);

			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			throw new StreamSeiException(e);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final PixContaCorrenteVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			PixContaCorrente.alterar(getIdEntidade(), verificarAcesso, usuario);
			alterar(obj, "pixContaCorrente", new AtributoPersistencia()					
					.add("dataGeracao", obj.getDataGeracao())
					.add("tempoExpiracao", obj.getTempoExpiracao())
					.add("txid", obj.getTxid())					
					.add("qrCode", obj.getQrCode())
					.add("jsonRetorno", obj.getJsonRetorno())
					.add("jsonEnvio", obj.getJsonEnvio())
					.add("situacaoPixEnum", obj.getSituacaoPixEnum())
					.add("statusPixEnum", obj.getStatusPixEnum())
					.add("nossoNumero", obj.getNossoNumero())
					.add("contaReceber", obj.getContaReceberVO())
					.add("valorContaReceberEnvio", obj.getValorContaReceberEnvio())
					.add("endToEndId", obj.getEndToEndId())
					.add("valor", obj.getValor())
					.add("horario", obj.getHorario())
					.add("infoPagador", obj.getInfoPagador())
					.add("contacorrente", obj.getContaCorrenteVO())
					.add("documentoPagador", obj.getDocumentoPagador())
					.add("nomePagador", obj.getNomePagador())
					.add("motivoCancelamento", obj.getMotivoCancelamento())
					.add("usuario", obj.getUsuarioVO()), new AtributoPersistencia().add("codigo", obj.getCodigo()), usuario);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	
	private void realizarPersistenciaSituacaoPix(PixContaCorrenteVO obj, UsuarioVO usuario) throws Exception {
		alterar(obj, "pixContaCorrente", new AtributoPersistencia()				
				.add("situacaoPixEnum", obj.getSituacaoPixEnum())
				.add("statusPixEnum", obj.getStatusPixEnum())
				.add("motivoCancelamento", obj.getMotivoCancelamento()), new AtributoPersistencia().add("codigo", obj.getCodigo()), usuario);

	}
	
	private void realizarPersistenciaAtualizacao(PixContaCorrenteVO obj, UsuarioVO usuario) throws Exception {
		alterar(obj, "pixContaCorrente", new AtributoPersistencia()
				.add("nossoNumero", obj.getNossoNumero())
				.add("contaReceber", obj.getContaReceberVO())
				.add("valorContaReceberEnvio", obj.getValorContaReceberEnvio())
				.add("tempoExpiracao", obj.getTempoExpiracao())
				, new AtributoPersistencia().add("codigo", obj.getCodigo()), usuario);
		
	}
	
	private void realizarPersistenciaRetorno(PixContaCorrenteVO obj, UsuarioVO usuario) throws Exception {
		alterar(obj, "pixContaCorrente", new AtributoPersistencia()
				.add("statusPixEnum", obj.getStatusPixEnum())
				.add("txid", obj.getTxid())				
				.add("qrCode", obj.getQrCode())
				.add("jsonRetorno", obj.getJsonRetorno()), new AtributoPersistencia().add("codigo", obj.getCodigo()), usuario);
		
	}
	
	private void realizarPersistenciaBaixaPix(PixContaCorrenteVO obj, UsuarioVO usuario) throws Exception {
		alterar(obj, "pixContaCorrente", new AtributoPersistencia()				
				.add("situacaoPixEnum", obj.getSituacaoPixEnum())
				.add("statusPixEnum", obj.getStatusPixEnum())
				.add("endToEndId", obj.getEndToEndId())				
				.add("horario", obj.getHorario())
				.add("infoPagador", obj.getInfoPagador())
				.add("documentoPagador", obj.getDocumentoPagador())
				.add("nomePagador", obj.getNomePagador())
				.add("valor", obj.getValor()), new AtributoPersistencia().add("codigo", obj.getCodigo()), usuario);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public TokenVO consultarTokenVO(ContaCorrenteVO obj) {
		Map<String, Object> parametroBody = new HashMap<>();
		try {
			Uteis.checkState(!obj.getHabilitarRegistroPix(), "O Pix para essa conta corrente " + obj.getNomeApresentacaoSistema() + " Nº" + obj.getNumero() + " Digito" + obj.getDigito() + " não foi habilitado.");
			ContaCorrenteVO.validarDadosContaCorrentePix(obj);
			parametroBody.put("grant_type", "client_credentials");
			if(obj.getAgencia().getBanco().isBancoItau()) {
				parametroBody.put("scope", "all");
				parametroBody.put("client_id", obj.getTokenIdRegistroPix());
				parametroBody.put("client_secret", obj.getTokenKeyRegistroPix());
				return TokenVO.autenticarFiedsTokenPorUnirest(obj.getAgencia().getBanco().getUrlApiPixAutenticacao() + UteisWebServiceUrl.URL_PIX_TOKEN, parametroBody);
			}else {
				parametroBody.put("scope", "cob.read cob.write pix.read pix.write");
				return TokenVO.autenticarBasicaTokenPorUnirest(obj.getAgencia().getBanco().getUrlApiPixAutenticacao() + UteisWebServiceUrl.URL_PIX_TOKEN, obj.getTokenIdRegistroPix(), obj.getTokenKeyRegistroPix(), parametroBody);
			}
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}finally {
			parametroBody = null;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void realizarValidacaoTokenPix(ContaCorrenteVO obj) {
		try {
			TokenVO token = consultarTokenVO(obj);
			Uteis.checkState(token.getAccess_token().isEmpty(), "Token Não foi autenticado");
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void realizarConfiguracaoWebhookPix(ContaCorrenteVO obj) {
		try {
			Gson gson = inicializaGson("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
			String urlExterna = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarUrlAcessoExternoAplicacaoPadraoSistema();
			Uteis.checkState(!Uteis.isAtributoPreenchido(urlExterna), "Não foi configurado a url externa do sistema.");
			if (urlExterna.endsWith("/")) {
				urlExterna = urlExterna + "webservice" + UteisWebServiceUrl.URL_PIX_WEBHOOK + "/" + obj.getCodigo() + "/pix";
			} else {
				urlExterna = urlExterna + "/webservice" + UteisWebServiceUrl.URL_PIX_WEBHOOK + "/" + obj.getCodigo() + "/pix";
			}
			WebhookRSVO webhookRSVO = new WebhookRSVO();
			webhookRSVO.setWebhookUrl(urlExterna);
			TokenVO token = consultarTokenVO(obj);
			RequestBodyEntity jsonEntity= null;
			jsonEntity  = Unirest.put(obj.getUrlRegistroWebhook())
			.header(UteisWebServiceUrl.Content_Type, UteisWebServiceUrl.Application_JSON)
			.header(UteisWebServiceUrl.Authorization, token.getToken_type() + " " + token.getAccess_token())
			.body(gson.toJson(webhookRSVO));
			inicialiarFiltrosPorBanco(obj, jsonEntity);
			HttpResponse<JsonNode> jsonResponse = jsonEntity.asJson();
			realizarValidacaoRetorno(obj, jsonResponse);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public String realizarConsultaWebhookPix(ContaCorrenteVO obj) {
		try {			
			TokenVO token = consultarTokenVO(obj);
			GetRequest getRequest= null;
			getRequest  = Unirest.get(obj.getUrlConsultaWebhook())
					.header(UteisWebServiceUrl.Content_Type, UteisWebServiceUrl.Application_JSON)
					.header(UteisWebServiceUrl.Authorization, token.getToken_type() + " " + token.getAccess_token());
			inicialiarFiltrosPorBanco(obj, getRequest);
			HttpResponse<JsonNode> jsonResponse = getRequest.asJson();
			realizarValidacaoRetorno(obj, jsonResponse);
			Gson gson = inicializaGson("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
			WebhookRSVO webhookRSVO = gson.fromJson(jsonResponse.getBody().toString(), WebhookRSVO.class);
			return webhookRSVO.getWebhookUrl();
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void realizarCancelamentoWebhookPix(ContaCorrenteVO obj) {
		try {
			TokenVO token = consultarTokenVO(obj);
			RequestBodyEntity jsonEntity= null;
			jsonEntity  = Unirest.delete(obj.getUrlCancelamentoWebhook())
					.header(UteisWebServiceUrl.Content_Type, UteisWebServiceUrl.Application_JSON)
					.header(UteisWebServiceUrl.Authorization, token.getToken_type() + " " + token.getAccess_token())
					.body("");
			inicialiarFiltrosPorBanco(obj, jsonEntity);
			HttpResponse<JsonNode> jsonResponse = jsonEntity.asJson();
			if (obj.getAgencia().getBanco().isBancoBrasil()) {
				Uteis.checkState(jsonResponse.getStatus() != 200 && jsonResponse.getBody().toString().contains("erros"), "Não foi possível realizar o cancelamento pix -"+jsonResponse.getBody().toString());
			}
			if (obj.getAgencia().getBanco().isBancoItau()) {
				Uteis.checkState(jsonResponse.getStatus() != 204, "Não foi possível realizar o cancelamento pix -"+jsonResponse.getBody().toString());	
			}
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	private void inicialiarFiltrosPorBanco(ContaCorrenteVO obj, RequestBodyEntity jsonEntity) {
		if (obj.getAgencia().getBanco().isBancoBrasil()) {
			jsonEntity.queryString("gw-dev-app-key", obj.getChaveAplicacaoDesenvolvedorPix());
		}
		if (obj.getAgencia().getBanco().isBancoItau()) {
			jsonEntity.header("x-correlationID", UUID.randomUUID().toString());
		}
	}
	
	private void inicialiarFiltrosPorBanco(ContaCorrenteVO obj, GetRequest getRequest) {
		if (obj.getAgencia().getBanco().isBancoBrasil()) {
			getRequest.queryString("gw-dev-app-key", obj.getChaveAplicacaoDesenvolvedorPix());
		}
		if (obj.getAgencia().getBanco().isBancoItau()) {
			getRequest.header("x-correlationID", UUID.randomUUID().toString());
		}
	}
	
	private void realizarValidacaoRetorno(ContaCorrenteVO obj, HttpResponse<JsonNode> jsonResponse) {
		if (obj.getAgencia().getBanco().isBancoBrasil()) {
			Uteis.checkState(jsonResponse.getStatus() != 200 && jsonResponse.getBody().toString().contains("erros"), "Não foi possível realizar a geração do pix -"+jsonResponse.getBody().toString());
		}
		if (obj.getAgencia().getBanco().isBancoItau()) {
			Uteis.checkState(!jsonResponse.isSuccess(), "Não foi possível realizar a geração do pix -"+jsonResponse.getBody().toString());	
		}
	}
	
	
	
	private PayloadPixRSVO realizarEnvioCobranca(PixContaCorrenteVO pixVO) {
		RequestBodyEntity jsonEntity= null;
		TokenVO token = null;
		Gson gson = inicializaGson("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		try {
			token = consultarTokenVO(pixVO.getContaCorrenteVO());
			jsonEntity = Unirest.put(pixVO.getUrlEnvioCobranca())
			.header(UteisWebServiceUrl.Content_Type, UteisWebServiceUrl.Application_JSON)
			.header(UteisWebServiceUrl.Authorization, token.getToken_type() + " " + token.getAccess_token())
			.body(pixVO.getJsonEnvio());
			inicialiarFiltrosPorBanco(pixVO.getContaCorrenteVO(), jsonEntity);
			HttpResponse<JsonNode> jsonResponse = jsonEntity.asJson();
			realizarValidacaoRetorno(pixVO.getContaCorrenteVO(), jsonResponse);
			pixVO.setJsonRetorno(jsonResponse.getBody().toString());
			return gson.fromJson(jsonResponse.getBody().toString(), PayloadPixRSVO.class);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		} finally {
			token = null;
			gson = null;
		}
	}

	
	
	private PayloadPixRSVO realizarRevisaoCobranca(PixContaCorrenteVO pixVO) {
		RequestBodyEntity jsonEntity= null;
		TokenVO token = null;
		Gson gson = inicializaGson("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		try {
			token = consultarTokenVO(pixVO.getContaCorrenteVO());
			jsonEntity = Unirest.patch(pixVO.getUrlRevisaoCobranca())
					.header(UteisWebServiceUrl.Content_Type, UteisWebServiceUrl.Application_JSON)
					.header(UteisWebServiceUrl.Authorization, token.getToken_type() + " " + token.getAccess_token())
					.body(gson.toJson(pixVO.getPayloadPixRSVO()));
			inicialiarFiltrosPorBanco(pixVO.getContaCorrenteVO(), jsonEntity);
			HttpResponse<JsonNode> jsonResponse = jsonEntity.asJson();
			realizarValidacaoRetorno(pixVO.getContaCorrenteVO(), jsonResponse);
			pixVO.setJsonRetorno(jsonResponse.getBody().toString());
			return gson.fromJson(jsonResponse.getBody().toString(), PayloadPixRSVO.class);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		} finally {
			token = null;
			gson = null;
		}
	}	
	
	private PayloadPixRSVO realizarConsultaCobranca(PixContaCorrenteVO pixVO) {
		GetRequest getRequest= null;
		TokenVO token = null;
		Gson gson = inicializaGson("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		try {
			token = consultarTokenVO(pixVO.getContaCorrenteVO());
			getRequest = Unirest.get(pixVO.getUrlConsultaCobranca())
					.header(UteisWebServiceUrl.Content_Type, UteisWebServiceUrl.Application_JSON)
					.header(UteisWebServiceUrl.Authorization, token.getToken_type() + " " + token.getAccess_token());
			inicialiarFiltrosPorBanco(pixVO.getContaCorrenteVO(), getRequest);
			HttpResponse<JsonNode> jsonResponse = getRequest.asJson();
			if(jsonResponse.getStatus() == 400 || jsonResponse.getStatus() == 200) {
				return gson.fromJson(jsonResponse.getBody().toString(), PayloadPixRSVO.class);	
			}
			return null;
		} catch (Exception e) {
			throw new StreamSeiException(e);
		} finally {
			token = null;
			gson = null;
		}
	}
	
	private String realizarConsultaQrCode(PixContaCorrenteVO pixVO) {
		GetRequest getRequest= null;
		TokenVO token = null;
		Gson gson = inicializaGson("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		try {
			token = consultarTokenVO(pixVO.getContaCorrenteVO());
			getRequest = Unirest.get(pixVO.getUrlConsultaQrCode())
					.header(UteisWebServiceUrl.Content_Type, UteisWebServiceUrl.Application_JSON)
					.header(UteisWebServiceUrl.Authorization, token.getToken_type() + " " + token.getAccess_token());
			inicialiarFiltrosPorBanco(pixVO.getContaCorrenteVO(), getRequest);
			HttpResponse<JsonNode> jsonResponse = getRequest.asJson();
			if(jsonResponse.getStatus() == 400 || jsonResponse.getStatus() == 200) {
				JsonObject jsonObject = gson.fromJson( jsonResponse.getBody().toString(), JsonObject.class);
				return jsonObject.get("emv").getAsString(); 
			}
			return null;
		} catch (Exception e) {
			throw new StreamSeiException(e);
		} finally {
			token = null;
			gson = null;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public PixContaCorrenteVO realizarVisualizacaoPix(ContaReceberVO contaReceberVO,  ConfiguracaoFinanceiroVO config, UsuarioVO usuarioVO) {
		try {			
			PixContaCorrenteVO pixVO = consultarPorNossoNumeroPorContaCorrentePorContaReceberPorStatus(contaReceberVO.getNossoNumero(), contaReceberVO.getContaCorrente(), contaReceberVO.getCodigo(), StatusPixEnum.ATIVA,  false, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
			if(!Uteis.isAtributoPreenchido(pixVO)) {
				pixVO.setContaCorrenteVO(getAplicacaoControle().getContaCorrenteVO(contaReceberVO.getContaCorrente(), usuarioVO));
				pixVO.setContaReceberVO(contaReceberVO);
			} else if (!pixVO.getContaCorrenteVO().getCodigo().equals(contaReceberVO.getContaCorrente())) {
				pixVO.setMotivoCancelamento("Cancelamento automático por PIX com alteração de conta corrente.");
				realizarCancelamentoPix(pixVO, usuarioVO);
				pixVO = new PixContaCorrenteVO();
				pixVO.setContaCorrenteVO(getAplicacaoControle().getContaCorrenteVO(contaReceberVO.getContaCorrente(), usuarioVO));
				pixVO.setContaReceberVO(contaReceberVO);			
			}else {
				realizarAtualizacaoPix(pixVO, contaReceberVO, config, usuarioVO);	
			} 
			return pixVO;
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public PixContaCorrenteVO realizarGeracaoPix(PixContaCorrenteVO pixVO, ConfiguracaoFinanceiroVO config, UsuarioVO usuarioVO) {
		try {
			PixContaCorrenteVO novoPixVO = realizarPreenchimentoPix(pixVO.getContaReceberVO().getCodigo(), config, usuarioVO);
			novoPixVO.setPayloadPixRSVO(realizarEnvioCobranca(novoPixVO));
// Teste pedro andrade			
//			novoPixVO.getPayloadPixRSVO().setStatus("ATIVA");
//			novoPixVO.getPayloadPixRSVO().setTextoImagemQRcode("00020101021226870014br.gov.bcb.pix2565qrcodepix-h.bb.com.br/pix/v2/83225b2d-8c55-4b04-99a9-59a1ef0680ff52040000530398654043.455802BR5920ALAN GUIACHERO BUENO6008BRASILIA62070503***6304A20E");
			
			novoPixVO.setTxid(novoPixVO.getPayloadPixRSVO().getTxid());			
			novoPixVO.setStatusPixEnum(StatusPixEnum.valueOf(novoPixVO.getPayloadPixRSVO().getStatus()));
			if(novoPixVO.getContaCorrenteVO().getAgencia().getBanco().isBancoBrasil()) {
				novoPixVO.setQrCode(novoPixVO.getPayloadPixRSVO().getTextoImagemQRcode());
			}else if(novoPixVO.getContaCorrenteVO().getAgencia().getBanco().isBancoItau()) {
				novoPixVO.setQrCode(realizarConsultaQrCode(novoPixVO));
			}
			realizarPersistenciaRetorno(novoPixVO, usuarioVO);
			return novoPixVO;
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	private PixContaCorrenteVO realizarPreenchimentoPix(Integer contaReceber, ConfiguracaoFinanceiroVO config, UsuarioVO usuarioVO) throws Exception {
		PixContaCorrenteVO pixVO = new PixContaCorrenteVO();
		ContaReceberVO obj = new ContaReceberVO();
		obj.setCodigo(contaReceber);
		getFacadeFactory().getContaReceberFacade().carregarDados(obj, config, usuarioVO);
		Uteis.checkState(!obj.getContaCorrenteVO().getAgencia().getBanco().isIntegracaoPix(), "O Banco da Conta Corrente informada não possui Integração com Pix.");
		Uteis.checkState(obj.getContaCorrenteVO().getChavePix().isEmpty(), "Não foi configurado o PIX para a conta corrente informada.");		
		pixVO.setUsuarioVO(usuarioVO);
		pixVO.setContaReceberVO(obj);
		pixVO.setNossoNumero(obj.getNossoNumero());		
		pixVO.setContaCorrenteVO(obj.getContaCorrenteVO());
		pixVO.setSituacaoPixEnum(SituacaoPixEnum.EM_PROCESSAMENTO);
		pixVO.setStatusPixEnum(StatusPixEnum.ATIVA);
		pixVO.setDataGeracao(new Date());
		executarMontagemJsonEnvioPix(pixVO, config, usuarioVO);
		persistir(pixVO, false, usuarioVO);
		return pixVO;
	}

	public void executarMontagemJsonEnvioPix(PixContaCorrenteVO pixVO, ConfiguracaoFinanceiroVO config, UsuarioVO usuarioVO) throws Exception {
		pixVO.setPayloadPixRSVO(new PayloadPixRSVO());
		montarDadosTxidPix(pixVO);
		montarDadosCalendarioPix(pixVO);
		ControleRemessaVO cr = new ControleRemessaVO();
		cr.setContaCorrenteVO(pixVO.getContaCorrenteVO());
		List<ControleRemessaContaReceberVO> lista = getFacadeFactory().getContaReceberFacade().consultaRapidaContasArquivoRemessaEntreDatas(pixVO.getContaReceberVO().getNossoNumero(), pixVO.getContaReceberVO().getCodigo(), pixVO.getContaReceberVO().getDataVencimento(), pixVO.getContaReceberVO().getDataVencimento(), pixVO.getContaReceberVO().getUnidadeEnsino().getCodigo(), cr, null, true, config, usuarioVO);
		ControleRemessaContaReceberVO crcrVO = !lista.isEmpty() ?  lista.get(0) : new  ControleRemessaContaReceberVO();
		pixVO.getPayloadPixRSVO().setDevedor(montarDadosDevedorPix(pixVO, crcrVO, usuarioVO));
		montarDadosValorCobrancaComVencimentoPix(pixVO, crcrVO, config);
		/**
		 * maxLength: 140 
		 * O campo solicitacaoPagador, opcional, determina um texto a ser apresentado ao pagador para que ele possa digitar uma informação correlata, em formato livre, a ser enviada ao recebedor. 
		 * Esse texto será preenchido, na pacs.008, pelo PSP do pagador, no campo RemittanceInformation . 
		 * O tamanho do campo na pacs.008 está limitado a 140 caracteres.
		 */
		pixVO.getPayloadPixRSVO().setSolicitacaoPagador("Pix utilizado para pagar conta receber de nosso numero " + crcrVO.getNossoNumero());
		pixVO.getPayloadPixRSVO().setChave(pixVO.getContaReceberVO().getContaCorrenteVO().getChavePix());		
		Gson convert = inicializaGson("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		pixVO.setJsonEnvio(convert.toJson(pixVO.getPayloadPixRSVO()));

	}

	private void montarDadosTxidPix(PixContaCorrenteVO pixVO) {
		StringBuilder txid = new StringBuilder();
		txid.append("D").append(Uteis.getDataAplicandoFormatacao(pixVO.getDataGeracao(), "ddMMyyyy"));
		txid.append("C").append(pixVO.getContaCorrenteVO().getCodigo());
		txid.append("N").append(pixVO.getNossoNumero());
		pixVO.setTxid(StringUtils.leftPad(txid.toString(), 35, "0"));
	}
	
	private void montarDadosCalendarioPix(PixContaCorrenteVO pixVO) {
		pixVO.getPayloadPixRSVO().setCalendario(new CalendarioPixRSVO());
		if(pixVO.getContaCorrenteVO().getAgencia().getBanco().getTipoCobrancaPixEnum().isImediata()) {
			pixVO.setTempoExpiracao(Uteis.getExpiracaoDia(pixVO.getDataGeracao()));
			pixVO.getPayloadPixRSVO().getCalendario().setExpiracao(pixVO.getTempoExpiracao());
		}else {
			pixVO.getPayloadPixRSVO().getCalendario().setDataDeVencimento(Uteis.getData(pixVO.getContaReceberVO().getDataVencimento(), "yyyy-MM-dd"));
			pixVO.getPayloadPixRSVO().getCalendario().setValidadeAposVencimento(0);	
		}
		
	}

	private PessoaPixRSVO montarDadosDevedorPix(PixContaCorrenteVO pixVO, ControleRemessaContaReceberVO crcrVO, UsuarioVO usuarioVO) {
		PessoaPixRSVO devedorPix = new PessoaPixRSVO();
		Uteis.checkState((crcrVO.getCodigoInscricao().equals(0) || crcrVO.getCodigoInscricao().equals(1)) && !Uteis.verificaCPF(crcrVO.getNumeroInscricao().toString()), "O cpf informado para a pessoa " + crcrVO.getNomeSacado() + " não é um cpf valido. Por favor verificar o Cadastro.");
		Uteis.checkState(crcrVO.getCodigoInscricao().equals(2) && !Uteis.validaCNPJ(crcrVO.getNumeroInscricao().toString()), "O CNPJ informado para a pessoa " + crcrVO.getNomeSacado() + " não é um cnpj valido. Por favor verificar o Cadastro.");
		if (crcrVO.getCodigoInscricao().equals(0) || crcrVO.getCodigoInscricao().equals(1)) {
			devedorPix.setCpf(Uteis.preencherComZerosPosicoesVagas(Uteis.removerMascara(crcrVO.getNumeroInscricao()), 11));
		} else {
			devedorPix.setCnpj(Uteis.preencherComZerosPosicoesVagas(Uteis.removerMascara(crcrVO.getNumeroInscricao()), 14));
		}
		devedorPix.setNome(Uteis.copiarDelimitandoTamanhoDoTexto(Uteis.removerAcentuacao(Uteis.removeCaractersEspeciais(crcrVO.getNomeSacado())), 30));
		if (pixVO.getContaCorrenteVO().getAgencia().getBanco().isBancoBrasil()) {
			devedorPix.setLogradouro(Uteis.copiarDelimitandoTamanhoDoTexto(Uteis.removerAcentuacao(Uteis.removeCaractersEspeciais(crcrVO.getLogradouro())), 40));
			devedorPix.setCidade(Uteis.copiarDelimitandoTamanhoDoTexto(Uteis.removerAcentuacao(crcrVO.getCidade()), 20));
			devedorPix.setUf(Uteis.copiarDelimitandoTamanhoDoTexto(crcrVO.getEstado(), 2));
			devedorPix.setCep(Uteis.copiarDelimitandoTamanhoDoTexto(Uteis.removerMascara(crcrVO.getCep()), 8));	
		}
		return devedorPix;
	}

	private void montarDadosValorCobrancaComVencimentoPix(PixContaCorrenteVO pixContaCorrenteVO, ControleRemessaContaReceberVO crcrVO, ConfiguracaoFinanceiroVO config) {
		ValorCobrancaComVencimentoPixRSVO valor = new ValorCobrancaComVencimentoPixRSVO();
		if(pixContaCorrenteVO.getContaReceberVO().getContaCorrenteVO().getAgencia().getBanco().getTipoCobrancaPixEnum().isImediata()) {
			valor.setOriginal(Uteis.arrendondarForcando2CadasDecimaisStr(pixContaCorrenteVO.getContaReceberVO().getValorRecebido()));
			pixContaCorrenteVO.setValorContaReceberEnvio(pixContaCorrenteVO.getContaReceberVO().getValorRecebido());
		}else {
			valor.setMulta(montarDadosMultaPix(crcrVO));
			valor.setJuro(montarDadosJurosPix(crcrVO, config));
			valor.setAbatimento(montarDadosAbatimentoPix(pixContaCorrenteVO.getContaReceberVO(), crcrVO));
			valor.setDesconto(montarDadosDescontoPix(crcrVO));
			if (pixContaCorrenteVO.getContaReceberVO().getContaCorrenteVO().getCarteiraRegistrada() && pixContaCorrenteVO.getContaReceberVO().getContaCorrenteVO().getGerarRemessaSemDescontoAbatido()) {
				valor.setOriginal(Uteis.arrendondarForcando2CadasDecimaisStr(crcrVO.getValorBaseComAcrescimo()));
				pixContaCorrenteVO.setValorContaReceberEnvio(crcrVO.getValorBaseComAcrescimo());
			} else {
				valor.setOriginal(Uteis.arrendondarForcando2CadasDecimaisStr(crcrVO.getValorComAcrescimo()));
				pixContaCorrenteVO.setValorContaReceberEnvio(crcrVO.getValorComAcrescimo());
			}	
		}
		pixContaCorrenteVO.getPayloadPixRSVO().setValor(valor);
	}

	private AbatimentoPixRSVO montarDadosAbatimentoPix(ContaReceberVO contaReceberVO, ControleRemessaContaReceberVO crcrVO) {
		AbatimentoPixRSVO abatimentoPix = new AbatimentoPixRSVO();
		/**
		 * Valores aceito somnete 1 ou 2 
		 * Sendo 1 para Valor Fixo
		 * Sendo 2 para Percentual
		 */
		abatimentoPix.setModalidade(1);
		if (contaReceberVO.getContaCorrenteVO().getCarteiraRegistrada() && contaReceberVO.getContaCorrenteVO().getGerarRemessaSemDescontoAbatido() && crcrVO.getValorBaseComAcrescimo() > 0 && crcrVO.getValorBaseComAcrescimo() > crcrVO.getValorComAcrescimo()) {
			Double valorDescFinal = crcrVO.getValorBaseComAcrescimo() - crcrVO.getValorComAcrescimo();
			abatimentoPix.setValorPerc(Uteis.arrendondarForcando2CadasDecimaisStr(valorDescFinal));
			return abatimentoPix;
		} else if (crcrVO.getValorAbatimento() > 0) {
			abatimentoPix.setValorPerc(Uteis.arrendondarForcando2CadasDecimaisStr(crcrVO.getValorAbatimento()));
			return abatimentoPix;
		}
		return null;
	}

	private MultaPixRSVO montarDadosMultaPix(ControleRemessaContaReceberVO crcrVO) {
		MultaPixRSVO multaVO = new MultaPixRSVO();
		/**
		 * Valores aceito somnete 1 ou 2 
		 * Sendo 1 para Valor Fixo
		 * Sendo 2 para Percentual
		 */
		multaVO.setModalidade(2);
		multaVO.setValorPerc("2.00");// 2% de multa
		return multaVO;
	}

	private JuroPixRSVO montarDadosJurosPix(ControleRemessaContaReceberVO crcrVO, ConfiguracaoFinanceiroVO config) {
		if (config.getUtilizarIntegracaoFinanceira()) {
			return null;
		} else {
			JuroPixRSVO jurosVO = new JuroPixRSVO();
			/*
			 * Valores aceito de 1 ate 8
			 * Sendo 1 valor dias corridos
			 * Sendo 2 percentual ao dia corrido
			 * Sendo 3 percentual ao mes dias corridos
			 * Sendo 4 percentual ao ano dias corridos
			 * Sendo 5 valor dia uteis
			 * Sendo 6 percentual ao dia uteis
			 * Sendo 7 percentual ao mes dias uteis
			 * Sendo 8 percentual ao ano dias uteis 
			 */
			jurosVO.setModalidade(3);
			jurosVO.setValorPerc("1.00");// 1% de juro ao mes
			return jurosVO;
		}
	}

	private DescontoPixRSVO montarDadosDescontoPix(ControleRemessaContaReceberVO crcrVO) {
		try {
			/**
			 * title: Data limite para o desconto absoluto da cobrança
			 * example: 2020-04-01
			 * Descontos por pagamento antecipado, com data fixa.
			 *  Matriz com até três elementos, sendo que cada elemento é composto por um par "data e valorPerc", 
			 *  para estabelecer descontos percentuais ou absolutos, até aquela data de pagamento. 
			 *  Trata-se de uma data, no formato yyyy-mm-dd, segundo ISO 8601. 
			 *  A data de desconto obrigatoriamente deverá ser menor que a data de vencimento da cobrança.
			 */
			List<DescontoDataFixaPixRSVO> lista = new ArrayList<>();
			DescontoDataFixaPixRSVO descontoDataFixaPixRSVO = null;
			if (crcrVO.getValorDescontoDataLimite() != 0 && (crcrVO.getDataLimiteConcessaoDesconto() == null || (crcrVO.getDataLimiteConcessaoDesconto() != null && UteisData.getCompareData(crcrVO.getDataLimiteConcessaoDesconto(), new Date()) >= 0))) {
				descontoDataFixaPixRSVO = new DescontoDataFixaPixRSVO();
				descontoDataFixaPixRSVO.setData(Uteis.getData(crcrVO.getDataLimiteConcessaoDesconto(), "yyyy-MM-dd"));
				descontoDataFixaPixRSVO.setValorPerc(Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(crcrVO.getValorDescontoDataLimite())), 17));
				lista.add(descontoDataFixaPixRSVO);
			}
			if (crcrVO.getValorDescontoDataLimite2() != 0 && (crcrVO.getDataLimiteConcessaoDesconto2() == null || (crcrVO.getDataLimiteConcessaoDesconto2() != null && UteisData.getCompareData(crcrVO.getDataLimiteConcessaoDesconto2(), new Date()) >= 0))) {
				descontoDataFixaPixRSVO = new DescontoDataFixaPixRSVO();
				descontoDataFixaPixRSVO.setData(Uteis.getData(crcrVO.getDataLimiteConcessaoDesconto2(), "yyyy-MM-dd"));
				descontoDataFixaPixRSVO.setValorPerc(Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(crcrVO.getValorDescontoDataLimite2())), 17));
				lista.add(descontoDataFixaPixRSVO);
			}
			if (crcrVO.getValorDescontoDataLimite3() != 0 && (crcrVO.getDataLimiteConcessaoDesconto3() == null || (crcrVO.getDataLimiteConcessaoDesconto3() != null && UteisData.getCompareData(crcrVO.getDataLimiteConcessaoDesconto3(), new Date()) >= 0))) {
				descontoDataFixaPixRSVO = new DescontoDataFixaPixRSVO();
				descontoDataFixaPixRSVO.setData(Uteis.getData(crcrVO.getDataLimiteConcessaoDesconto3(), "yyyy-MM-dd"));
				descontoDataFixaPixRSVO.setValorPerc(Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(crcrVO.getValorDescontoDataLimite3())), 17));
				lista.add(descontoDataFixaPixRSVO);
			}
			if (!lista.isEmpty()) {
				DescontoPixRSVO descontoPix = new DescontoPixRSVO();
				/*
				 * Valores aceito de 1 ate 6
				 * Sendo 1 valor fixo ate as datas informadas
				 * Sendo 2 percentual ate as datas informadas
				 * Sendo 3 Valor por antecipação dia corridos
				 * Sendo 4 Valor por antecipação dia útil
				 * Sendo 5 Percentual por antecipação dia corrido
				 * Sendo 6 Percentual por antecipação dia útil
				 */
				descontoPix.setModalidade(1);
				descontoPix.setValorPerc("");
				descontoPix.setDescontoDataFixa(lista);
				return descontoPix;
			}
			return null;
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarProcessamentoJobCancelamentoPix() throws Exception {
		UsuarioVO usuarioOperacaoExterna = getFacadeFactory().getUsuarioFacade().obterUsuarioResponsavelOperacoesExternas();
		List<PixContaCorrenteVO> lista = consultarPixContaCorrenteAptoCancelamento(new Date(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioOperacaoExterna);
		for (PixContaCorrenteVO pixContaCorrenteVO : lista) {
			try {
				pixContaCorrenteVO.setMotivoCancelamento("Cancelamento automático por PIX com data de vencimento expirado.");
				realizarCancelamentoPix(pixContaCorrenteVO, usuarioOperacaoExterna);
			} catch (Exception e) {
				try {
					RegistroExecucaoJobVO  registroExecucaoJob =  new RegistroExecucaoJobVO(JobsEnum.JOB_CANCELAR_PIX.getName(), "Erro ao tentar fazer o cancelamento do pix:" +pixContaCorrenteVO.getCodigo() + " erro:" + e.getMessage());
					getFacadeFactory().getRegistroExecucaoJobFacade().incluir(registroExecucaoJob);	
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}		
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarProcessamentoJobWebhookPix() throws Exception {
		UsuarioVO usuarioOperacaoExterna = getFacadeFactory().getUsuarioFacade().obterUsuarioResponsavelOperacoesExternas();
		List<PixContaCorrenteVO> lista = consultarComBancoIntegradoPixSemWebhookPorSituacao(StatusPixEnum.ATIVA, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioOperacaoExterna);
		for (PixContaCorrenteVO pixContaCorrenteVO : lista) {
			try {
				realizarVerificacaoBaixaPixContaCorrente(pixContaCorrenteVO, false, usuarioOperacaoExterna);	
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
	}
	
	private void realizarAtualizacaoPix(PixContaCorrenteVO pixVO,  ContaReceberVO contaReceber, ConfiguracaoFinanceiroVO config, UsuarioVO usuarioVO) throws Exception {		
		boolean existeAtualizacaoPix = false;
		boolean existePersistenciaPix = false;
		if(!pixVO.getContaReceberVO().getCodigo().equals(contaReceber.getCodigo())) {
			pixVO.getContaReceberVO().setCodigo(contaReceber.getCodigo());
			existePersistenciaPix= true;
		}
		getFacadeFactory().getContaReceberFacade().carregarDados(pixVO.getContaReceberVO(), config, usuarioVO);
		
		if(pixVO.getContaCorrenteVO().getAgencia().getBanco().getTipoCobrancaPixEnum().isImediata()
				&& !pixVO.getTempoExpiracao().equals(Uteis.getExpiracaoDia(pixVO.getDataGeracao()))) {
			pixVO.setTempoExpiracao(Uteis.getExpiracaoDia(pixVO.getDataGeracao()));
			pixVO.getPayloadPixRSVO().setCalendario(new CalendarioPixRSVO());
			pixVO.getPayloadPixRSVO().getCalendario().setExpiracao(pixVO.getTempoExpiracao());
			existeAtualizacaoPix = true;
			existePersistenciaPix= true;
		}
		
		if(pixVO.getContaCorrenteVO().getAgencia().getBanco().getTipoCobrancaPixEnum().isImediata()
				&& !pixVO.getValorContaReceberEnvio().equals(pixVO.getContaReceberVO().getValorRecebido())) {
			pixVO.setValorContaReceberEnvio(pixVO.getContaReceberVO().getValorRecebido());
			pixVO.getPayloadPixRSVO().setValor(new ValorCobrancaComVencimentoPixRSVO());
			pixVO.getPayloadPixRSVO().getValor().setOriginal(Uteis.arrendondarForcando2CadasDecimaisStr(pixVO.getContaReceberVO().getValorRecebido()));
			existeAtualizacaoPix = true;
			existePersistenciaPix= true;
		}
		
		if(!pixVO.getNossoNumero().equals(contaReceber.getNossoNumero())) {
			pixVO.setNossoNumero(contaReceber.getNossoNumero());
			existePersistenciaPix= true;
		}
		
		if(existePersistenciaPix) {
			realizarPersistenciaAtualizacao(pixVO, usuarioVO);	
		}
		if(existeAtualizacaoPix) {
			realizarRevisaoCobranca(pixVO);
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public String consultarJsonPix(PixContaCorrenteVO pixContaCorrenteVO, UsuarioVO usuario) throws Exception {
		pixContaCorrenteVO.setContaCorrenteVO(getAplicacaoControle().getContaCorrenteVO(pixContaCorrenteVO.getContaCorrenteVO().getCodigo(), usuario));
		PayloadPixRSVO  payloadPixRSVO = realizarConsultaCobranca(pixContaCorrenteVO);
		Gson gson = inicializaGson("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		return gson.toJson(payloadPixRSVO);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarCancelamentoPix(PixContaCorrenteVO pixContaCorrenteVO, UsuarioVO usuario) throws Exception {
		Uteis.checkState(!Uteis.isAtributoPreenchido(pixContaCorrenteVO.getMotivoCancelamento()), "O campo Motivo Cancelamento deve ser informado.");
		pixContaCorrenteVO.setContaCorrenteVO(getAplicacaoControle().getContaCorrenteVO(pixContaCorrenteVO.getContaCorrenteVO().getCodigo(), usuario));
		pixContaCorrenteVO.getPayloadPixRSVO().setStatus(StatusPixEnum.REMOVIDA_PELO_USUARIO_RECEBEDOR.name());
		PayloadPixRSVO  payloadPixRSVO = realizarRevisaoCobranca(pixContaCorrenteVO);
		if(payloadPixRSVO.getStatus().equals(StatusPixEnum.REMOVIDA_PELO_USUARIO_RECEBEDOR.name())) {
			pixContaCorrenteVO.setStatusPixEnum(StatusPixEnum.REMOVIDA_PELO_USUARIO_RECEBEDOR);
			pixContaCorrenteVO.setSituacaoPixEnum(SituacaoPixEnum.CANCELADO);
			realizarPersistenciaSituacaoPix(pixContaCorrenteVO, usuario);	
		}		
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public PixContaCorrenteVO realizarVerificacaoBaixaPixContaCorrente(PixContaCorrenteVO pixContaCorrenteVO, boolean isDevolverPixAtualizado, UsuarioVO usuario) throws Exception {
		pixContaCorrenteVO.setContaCorrenteVO(getAplicacaoControle().getContaCorrenteVO(pixContaCorrenteVO.getContaCorrenteVO().getCodigo(), usuario));
		PayloadPixRSVO  payloadPixRSVO = realizarConsultaCobranca(pixContaCorrenteVO);
		Uteis.checkState(payloadPixRSVO == null, "Não foi localizado os dados do Pix.");
		if(payloadPixRSVO.getErros() != null && !payloadPixRSVO.getErros().isEmpty()) {
			throw new StreamSeiException(payloadPixRSVO.getErros().stream().findFirst().get().getMensagem());
		}
		if(payloadPixRSVO.getPix() != null && !payloadPixRSVO.getPix().isEmpty()) {
			for (PixRSVO pixRSVO : payloadPixRSVO.getPix()) {
				if(pixRSVO.getTxid().equals(pixContaCorrenteVO.getTxid())) {
					realizarProcessamentoBaixaPix(pixRSVO, pixContaCorrenteVO.getContaCorrenteVO().getCodigo(), StatusPixEnum.valueOf(payloadPixRSVO.getStatus()), usuario);
					break;
				}
			}	
		}		
		if(!isDevolverPixAtualizado) {
			return pixContaCorrenteVO;	
		}
		return consultarPorChavePrimaria(pixContaCorrenteVO.getCodigo(), true, Uteis.NIVELMONTARDADOS_TODOS, usuario);
		 
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarProcessamentoBaixaPix(PixRSVO pixRSVO, Integer contaCorrente, StatusPixEnum statusPixEnumAtual, UsuarioVO usuario) throws Exception {
		PixContaCorrenteVO obj = consultarPorTxid(pixRSVO.getTxid(), contaCorrente, false, Uteis.NIVELMONTARDADOS_TODOS, usuario);
		obj.setEndToEndId(pixRSVO.getEndToEndId());
		obj.setInfoPagador(pixRSVO.getInfoPagador());
		obj.setValor(Double.parseDouble(pixRSVO.getValor()));
		obj.setHorario(Uteis.getDataISO8601Format(pixRSVO.getHorario()));
		obj.setInfoPagador(pixRSVO.getInfoPagador());
		obj.setNomePagador(pixRSVO.getPagador() != null && pixRSVO.getPagador().getNome() != null ? pixRSVO.getPagador().getNome(): "");
		obj.setDocumentoPagador(pixRSVO.getPagador() != null && pixRSVO.getPagador().getCpf() != null && !pixRSVO.getPagador().getCpf().isEmpty() ? pixRSVO.getPagador().getCpf() : pixRSVO.getPagador().getCnpj());
		obj.setContaCorrenteVO(getAplicacaoControle().getContaCorrenteVO(contaCorrente, usuario));
		if (!Uteis.isAtributoPreenchido(obj)) {
			obj.setSituacaoPixEnum(SituacaoPixEnum.NAO_LOCALIZADO_PIX);
			obj.setStatusPixEnum(statusPixEnumAtual);
			obj.setTxid(pixRSVO.getTxid());
			persistir(obj, false, usuario);
		} else if(statusPixEnumAtual != null && statusPixEnumAtual.isRemovidaPsp()){
			obj.setSituacaoPixEnum(SituacaoPixEnum.CANCELADO);
			obj.setStatusPixEnum(statusPixEnumAtual);
			persistir(obj, false, usuario);
		} else if(obj.getStatusPixEnum().isAtiva() && (statusPixEnumAtual == null || statusPixEnumAtual.isConcluida())){
			obj.setStatusPixEnum(statusPixEnumAtual);
			realizarVerificacaoParaBaixaPix(obj, contaCorrente, usuario);
		}
	}

	private void realizarVerificacaoParaBaixaPix(PixContaCorrenteVO pixVO,  Integer contaCorrente, UsuarioVO usuarioOperacaoExterna) throws Exception {
		Map<BigInteger, ContaReceberVO> map = getFacadeFactory().getContaReceberFacade().consultarContaBaixarPix(pixVO, false);
		if(map != null && map.size() > 0) {			
			pixVO.setContaReceberVO(map.get(new BigInteger(pixVO.getNossoNumero())));
			ConfiguracaoFinanceiroVO configFinanceiro = getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(pixVO.getContaReceberVO().getUnidadeEnsinoFinanceira().getCodigo(), usuarioOperacaoExterna);
			getFacadeFactory().getContaReceberFacade().carregarDados(pixVO.getContaReceberVO(), configFinanceiro, usuarioOperacaoExterna);			
			if (getFacadeFactory().getContaReceberFacade().verificaSituacaoContaReceberPorCodigoSituacao(pixVO.getContaReceberVO().getCodigo(), "RE", usuarioOperacaoExterna)) {
				realizarPreenchimentoContaReceberHistorico(pixVO, usuarioOperacaoExterna);
			}else {
				realizarPreenchimentoNegociacaoRecebimento(pixVO, configFinanceiro, usuarioOperacaoExterna);
				pixVO.setSituacaoPixEnum(SituacaoPixEnum.RECEBIDO);				
				realizarPersistenciaBaixaPix(pixVO, usuarioOperacaoExterna);	
			}	
		}else {
			realizarPreenchimentoContaReceberNaoLocalizadaArquivoRetorno(pixVO, contaCorrente, usuarioOperacaoExterna);
		}		
	}
	
	private void realizarPreenchimentoNegociacaoRecebimento(PixContaCorrenteVO pixVO, ConfiguracaoFinanceiroVO configFinanceiro, UsuarioVO usuarioOperacaoExterna) throws Exception, ParseException {		
		preencherContaReceberVOComPix(pixVO, configFinanceiro, usuarioOperacaoExterna);
		NegociacaoRecebimentoVO negociacaoRecebimentoVO = getFacadeFactory().getNegociacaoRecebimentoFacade().criarNegociacaoRecebimentoVOPorBaixaAutomatica(pixVO.getContaReceberVO(), pixVO.getContaReceberVO().getContaCorrenteVO(), configFinanceiro, usuarioOperacaoExterna);
		negociacaoRecebimentoVO.setData(pixVO.getHorario());
		negociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoVOs().add(criarFormaPagamentoNegociacaoRecebimentoVO(pixVO, usuarioOperacaoExterna));
		negociacaoRecebimentoVO.getContaReceberNegociacaoRecebimentoVOs().add(criarContaReceberNegociacaoRecebimentoVO(pixVO));
		negociacaoRecebimentoVO.calcularTotal(configFinanceiro, usuarioOperacaoExterna);
		negociacaoRecebimentoVO.setValorTotal(pixVO.getValor());
		negociacaoRecebimentoVO.setTipoPessoa(pixVO.getContaReceberVO().getTipoPessoa());
		negociacaoRecebimentoVO.setRecebimentoBoletoAutomatico(Boolean.TRUE);		
		if (negociacaoRecebimentoVO.getTipoPessoa().equals("AL")) {
			negociacaoRecebimentoVO.setMatricula(pixVO.getContaReceberVO().getMatriculaAluno().getMatricula());
		}
		getFacadeFactory().getNegociacaoRecebimentoFacade().incluir(negociacaoRecebimentoVO, configFinanceiro, false, usuarioOperacaoExterna);
		getFacadeFactory().getCampanhaFacade().finalizarAgendaCompromissoContaReceber(negociacaoRecebimentoVO);
	}

	private void realizarPreenchimentoContaReceberNaoLocalizadaArquivoRetorno(PixContaCorrenteVO pixVO,  Integer contaCorrente, UsuarioVO usuarioOperacaoExterna) throws Exception {
		ContaReceberNaoLocalizadaArquivoRetornoVO crnlar = (ContaReceberNaoLocalizadaArquivoRetornoVO) getFacadeFactory().getContaReceberNaoLocalizadaArquivoRetornoFacade().consultarPorNossoNumeroUnico(pixVO.getNossoNumero(), pixVO.getHorario(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioOperacaoExterna);
		if (crnlar.getCodigo() != 0) {
			getFacadeFactory().getContaReceberNaoLocalizadaArquivoRetornoFacade().alterar(crnlar, usuarioOperacaoExterna);
		} else {
			crnlar.setNossoNumero(pixVO.getNossoNumero());
			crnlar.setDataVcto(new Date());
			crnlar.setSituacao("");
			crnlar.setValor(0.0);
			crnlar.setDataPagamento(pixVO.getHorario());
			crnlar.setValorRecebido(pixVO.getValor());
			crnlar.getContaCorrenteVO().setCodigo(contaCorrente);
			getFacadeFactory().getContaReceberNaoLocalizadaArquivoRetornoFacade().incluir(crnlar, usuarioOperacaoExterna);
		}
		crnlar = null;
		pixVO.setSituacaoPixEnum(SituacaoPixEnum.NAO_LOCALIZADO_CONTARECEBER);		
		realizarPersistenciaBaixaPix(pixVO, usuarioOperacaoExterna);
	}

	private void realizarPreenchimentoContaReceberHistorico(PixContaCorrenteVO pixVO, UsuarioVO usuarioOperacaoExterna) throws Exception {
		ContaReceberHistoricoVO crh = new ContaReceberHistoricoVO();
		crh.setContaReceber(pixVO.getContaReceberVO().getCodigo());
		crh.setData(new Date());
		crh.setMotivo("Conta Receber já recebida. Tentativa em duplicidade! Pix!");
		crh.setNomeArquivo("");
		crh.setNossoNumero(pixVO.getContaReceberVO().getNossoNumero());
		crh.setResponsavel(usuarioOperacaoExterna);
		crh.setValorRecebimento(pixVO.getContaReceberVO().getValorRecebido());
		getFacadeFactory().getContaReceberHistoricoFacade().incluir(crh, usuarioOperacaoExterna);
		crh = null;
		pixVO.setSituacaoPixEnum(SituacaoPixEnum.CONTARECEBER_DUPLICIDADE);		
		realizarPersistenciaBaixaPix(pixVO, usuarioOperacaoExterna);
	}

	

	private void preencherContaReceberVOComPix(PixContaCorrenteVO pixVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
		pixVO.getContaReceberVO().setRealizandoRecebimento(true);
		Date dataVencimentoOriginal = null;
		if (configuracaoFinanceiroVO.getVencimentoParcelaDiaUtil()) {
			dataVencimentoOriginal = pixVO.getContaReceberVO().getDataVencimento();
		}
		if (configuracaoFinanceiroVO.getVencimentoParcelaDiaUtil()) {
			pixVO.getContaReceberVO().getDataOriginalVencimento();
			pixVO.getContaReceberVO().setDataVencimentoDiaUtil(getFacadeFactory().getContaReceberFacade().obterDataVerificandoDiaUtil(pixVO.getContaReceberVO().getDataVencimento(), pixVO.getContaReceberVO().getUnidadeEnsino().getCidade().getCodigo(), usuarioVO));
			pixVO.getContaReceberVO().setDataVencimento(pixVO.getContaReceberVO().getDataVencimentoDiaUtil());
		}
		pixVO.getContaReceberVO().getCalcularValorFinal(pixVO.getHorario(), configuracaoFinanceiroVO, false, pixVO.getHorario(), usuarioVO);
		if (configuracaoFinanceiroVO.getVencimentoParcelaDiaUtil()) {
			pixVO.getContaReceberVO().setDataVencimento(dataVencimentoOriginal);
		}
		// Acrescentado regra para somente gerar pendência somente se o valor for maior que que o valor configurado na configuração financeira do sistema -> campo = ValorMinimoGerarPendenciaControleCobranca
		if (pixVO.getValor() < pixVO.getContaReceberVO().getValorRecebido()) {
			pixVO.getContaReceberVO().setTipoDescontoLancadoRecebimento("VA");
			pixVO.getContaReceberVO().setValorDescontoLancadoRecebimento(pixVO.getContaReceberVO().getValorRecebido() - pixVO.getValor());
			pixVO.getContaReceberVO().setValorCalculadoDescontoLancadoRecebimento(pixVO.getContaReceberVO().getValorRecebido() - pixVO.getValor());
			if((pixVO.getContaReceberVO().getValorRecebido() - pixVO.getValor()) > configuracaoFinanceiroVO.getValorMinimoGerarPendenciaControleCobranca()) {
				MapaPendenciasControleCobrancaVO mapaPendenciasControleCobrancaVO = new MapaPendenciasControleCobrancaVO();
				if (Uteis.isAtributoPreenchido(pixVO.getContaReceberVO().getMatriculaAluno().getMatricula())) {
					mapaPendenciasControleCobrancaVO.setMatricula(pixVO.getContaReceberVO().getMatriculaAluno());
				}
				mapaPendenciasControleCobrancaVO.setContaReceber(pixVO.getContaReceberVO());
				mapaPendenciasControleCobrancaVO.setValorDiferenca(pixVO.getContaReceberVO().getValorRecebido() - pixVO.getValor());
				mapaPendenciasControleCobrancaVO.setDataPagamento(pixVO.getHorario());
				mapaPendenciasControleCobrancaVO.setJuro(pixVO.getContaReceberVO().getJuro());
				mapaPendenciasControleCobrancaVO.setMulta(pixVO.getContaReceberVO().getMulta());
				getFacadeFactory().getMapaPendenciasControleCobrancaFacade().incluir(mapaPendenciasControleCobrancaVO, usuarioVO);	
			}
		} else if (pixVO.getValor() > pixVO.getContaReceberVO().getValorRecebido()) {
			pixVO.getContaReceberVO().setAcrescimo(pixVO.getValor() - pixVO.getContaReceberVO().getValorRecebido());
		}
		pixVO.getContaReceberVO().setValorRecebido(pixVO.getValor());
	}

	private FormaPagamentoNegociacaoRecebimentoVO criarFormaPagamentoNegociacaoRecebimentoVO(PixContaCorrenteVO pixVO, UsuarioVO usuarioVO) throws Exception {
		FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO = new FormaPagamentoNegociacaoRecebimentoVO();
		formaPagamentoNegociacaoRecebimentoVO.setContaCorrente(pixVO.getContaReceberVO().getContaCorrenteVO());
		formaPagamentoNegociacaoRecebimentoVO.setFormaPagamento(pixVO.getContaReceberVO().getContaCorrenteVO().getFormaRecebimentoPadraoPix());
		formaPagamentoNegociacaoRecebimentoVO.setValorRecebimento(pixVO.getValor());
		formaPagamentoNegociacaoRecebimentoVO.setDataCredito(pixVO.getHorario());
		return formaPagamentoNegociacaoRecebimentoVO;
	}

	private ContaReceberNegociacaoRecebimentoVO criarContaReceberNegociacaoRecebimentoVO(PixContaCorrenteVO pixVO) throws Exception {
		ContaReceberNegociacaoRecebimentoVO contaReceberNegociacaoRecebimentoVO = new ContaReceberNegociacaoRecebimentoVO();
		contaReceberNegociacaoRecebimentoVO.setContaReceber(pixVO.getContaReceberVO());				
		return contaReceberNegociacaoRecebimentoVO;
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void consultar(DataModelo dataModelo, PixContaCorrenteVO obj) throws Exception {
		dataModelo.getListaConsulta().clear();
		dataModelo.getListaFiltros().clear();
		dataModelo.setListaConsulta(consultaRapidaPorFiltros(obj, dataModelo));
	}
	
	
	private List<PixContaCorrenteVO> consultaRapidaPorFiltros(PixContaCorrenteVO obj, DataModelo dataModelo) {
		try {
			ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());
			StringBuilder sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" WHERE 1= 1 ");
			montarFiltrosParaConsulta(obj, dataModelo, sqlStr);
			sqlStr.append(" ORDER BY pixContaCorrente.codigo desc ");
			UteisTexto.addLimitAndOffset(sqlStr, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), dataModelo.getListaFiltros().toArray());
			montarTotalizadorConsultaBasica(dataModelo, tabelaResultado);
			return montarDadosConsultaBasica(tabelaResultado);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	private void montarFiltrosParaConsulta(PixContaCorrenteVO obj, DataModelo dataModelo, StringBuilder sqlStr) {
		
		if(Uteis.isAtributoPreenchido(obj.getCodigo()) && !obj.getCodigo().equals(0)){
			sqlStr.append(" and pixContaCorrente.codigo = ? ");
			dataModelo.getListaFiltros().add(obj.getCodigo());	
		}
		if(Uteis.isAtributoPreenchido(dataModelo.getDataIni()) && Uteis.isAtributoPreenchido(dataModelo.getDataFim())){
			sqlStr.append("and pixContaCorrente.datageracao >= ? ");
			sqlStr.append(" and pixContaCorrente.datageracao <= ? ");
			dataModelo.getListaFiltros().add(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(dataModelo.getDataIni()));
			dataModelo.getListaFiltros().add(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(dataModelo.getDataFim()));
		}
		
		if(Uteis.isAtributoPreenchido(obj.getUsuarioVO().getNome())) {
			sqlStr.append(" and lower(sem_acentos(u.nome)) like(lower(sem_acentos(?)))");
			dataModelo.getListaFiltros().add(PERCENT + obj.getUsuarioVO().getNome().toLowerCase() + PERCENT);
		}
		
		if(Uteis.isAtributoPreenchido(obj.getTxid())) {
			sqlStr.append(" and  pixContaCorrente.txid = ?");
			dataModelo.getListaFiltros().add(obj.getTxid());
		}
		
		if(Uteis.isAtributoPreenchido(obj.getNossoNumero())) {
			sqlStr.append(" and  pixContaCorrente.nossoNumero like(?)");
			dataModelo.getListaFiltros().add(PERCENT + obj.getNossoNumero().toLowerCase() + PERCENT);
		}
		
		if(Uteis.isAtributoPreenchido(obj.getStatusPixEnum()) && !obj.getStatusPixEnum().isNenhum()) {
			sqlStr.append(" and  pixContaCorrente.statusPixEnum  = ? ");
			dataModelo.getListaFiltros().add(obj.getStatusPixEnum().name());
		}
		if(Uteis.isAtributoPreenchido(obj.getSituacaoPixEnum()) && !obj.getSituacaoPixEnum().isNenhum()) {
			sqlStr.append(" and  pixContaCorrente.situacaoPixEnum  = ? ");
			dataModelo.getListaFiltros().add(obj.getSituacaoPixEnum().name());
		}
		if(Uteis.isAtributoPreenchido(obj.getContaCorrenteVO().getCodigo())) {
			sqlStr.append(" and contacorrente.codigo = ? ");
			dataModelo.getListaFiltros().add(obj.getContaCorrenteVO().getCodigo());
		}
	}	
	
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private StringBuilder getSQLPadraoConsultaBasica() {
		StringBuilder sql = new StringBuilder(getSelectTotalizadorConsultaBasica());
		sql.append(" pixContaCorrente.codigo as \"pixContaCorrente.codigo\", pixContaCorrente.situacaoPixEnum as \"pixContaCorrente.situacaoPixEnum\",  ");
		sql.append(" pixContaCorrente.nossoNumero as \"pixContaCorrente.nossoNumero\", pixContaCorrente.contaReceber as \"pixContaCorrente.contaReceber\",   ");
		sql.append(" pixContaCorrente.valor as \"pixContaCorrente.valor\", ");
		sql.append(" pixContaCorrente.valorContaReceberEnvio as \"pixContaCorrente.valorContaReceberEnvio\", ");
		sql.append(" pixContaCorrente.datageracao as \"pixContaCorrente.datageracao\",  ");
		sql.append(" pixContaCorrente.txid as \"pixContaCorrente.txid\", pixContaCorrente.statusPixEnum as \"pixContaCorrente.statusPixEnum\", ");
				
		
		sql.append(" contaCorrente.codigo as \"contaCorrente.codigo\", contaCorrente.numero as \"contaCorrente.numero\",  contaCorrente.digito as \"contaCorrente.digito\", ");
		sql.append(" contaCorrente.tipoContaCorrente as \"contaCorrente.tipoContaCorrenteEnum\", contaCorrente.situacao as \"contaCorrente.situacao\", ");
		sql.append(" contaCorrente.carteira as \"contaCorrente.carteira\", contaCorrente.nomeApresentacaoSistema as \"contaCorrente.nomeApresentacaoSistema\", ");
		
		sql.append(" agencia.codigo as \"agencia.codigo\", agencia.nome as \"agencia.nome\", ");
		
		sql.append(" banco.codigo as \"banco.codigo\", banco.nome as \"banco.nome\", ");
		
		sql.append(" u.codigo as \"u.codigo\", u.nome as \"u.nome\", ");
		sql.append(" p.codigo as \"p.codigo\", p.nome as \"p.nome\", p.email as \"p.email\", p.email2 as \"p.email2\" ");
		sql.append(" FROM pixContaCorrente ");
		sql.append(" Inner JOIN contaCorrente  ON contaCorrente.codigo = pixContaCorrente.contaCorrente ");
		sql.append(" Inner JOIN agencia  ON agencia.codigo = contaCorrente.agencia ");
		sql.append(" Inner JOIN banco  ON banco.codigo = agencia.banco ");
		sql.append(" LEFT JOIN usuario u ON u.codigo = pixContaCorrente.usuario ");
		sql.append(" LEFT JOIN pessoa p ON p.codigo = u.pessoa ");
		return sql;
	}

	
	private PixContaCorrenteVO consultarPorNossoNumeroPorContaCorrentePorContaReceberPorStatus(String nossoNumero,  Integer contaCorrente, Integer contaReceber, StatusPixEnum statusPixEnum, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) {
		try {
			ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
			StringBuilder sql = new StringBuilder("select pixcontacorrente.* from pixcontacorrente ");
			sql.append(" inner join contacorrente  on contacorrente.codigo = pixcontacorrente.contacorrente ");
			sql.append(" inner join agencia on agencia.codigo  = contacorrente.agencia ");
			sql.append(" inner join banco  on banco.codigo= agencia.banco ");
			sql.append(" where pixcontacorrente.statusPixEnum = ?  ");
			sql.append(" and contacorrente.habilitarRegistroPix = true ");
			sql.append(" and banco.integracaoPix = true ");
			sql.append(" and ( ");
			sql.append(" 	(pixcontacorrente.nossoNumero = ? and pixcontacorrente.contaCorrente = ? ) ");
			sql.append(" 	or  ");
			sql.append(" 	(pixcontacorrente.contaReceber = ? )  ");
			sql.append(" ) ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), statusPixEnum.name(), nossoNumero, contaCorrente, contaReceber);
			if (!tabelaResultado.next()) {
				return new PixContaCorrenteVO();
			}
			return (montarDados(tabelaResultado, nivelMontarDados, usuario));
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	
	
	
	private PixContaCorrenteVO consultarPorTxid(String txid, Integer contaCorrente, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) {
		try {
			ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
			String sql = "SELECT * FROM pixContaCorrente WHERE txid = ? and contaCorrente = ?";
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, txid, contaCorrente);
			if (!tabelaResultado.next()) {
				return new PixContaCorrenteVO();
			}
			return (montarDados(tabelaResultado, nivelMontarDados, usuario));
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<PixContaCorrenteVO> consultarComBancoIntegradoPixSemWebhookPorSituacao(StatusPixEnum statusPixEnum, int nivelMontarDados, UsuarioVO usuario) {
		try {
			StringBuilder sql = new StringBuilder("select pixcontacorrente.* from pixcontacorrente ");
			sql.append(" inner join contacorrente  on contacorrente.codigo = pixcontacorrente.contacorrente ");
			sql.append(" inner join agencia on agencia.codigo  = contacorrente.agencia ");
			sql.append(" inner join banco  on banco.codigo= agencia.banco ");
			sql.append(" where pixcontacorrente.statusPixEnum = ? ");
			sql.append(" and contacorrente.habilitarRegistroPix = true ");
			sql.append(" and banco.integracaoPix = true ");
			sql.append(" and banco.possuiWebhook = false ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(),  statusPixEnum.name());
			return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	private List<PixContaCorrenteVO> consultarPixContaCorrenteAptoCancelamento(Date data,  int nivelMontarDados, UsuarioVO usuario) {
		try {
			StringBuilder sql = new StringBuilder("select pixcontacorrente.* from pixcontacorrente ");
			sql.append(" inner join contacorrente  on contacorrente.codigo = pixcontacorrente.contacorrente ");
			sql.append(" inner join agencia on agencia.codigo  = contacorrente.agencia ");
			sql.append(" inner join banco  on banco.codigo= agencia.banco ");
			sql.append(" where pixcontacorrente.statusPixEnum =  'ATIVA' ");
			sql.append(" and pixcontacorrente.datageracao < '").append(Uteis.getDataBD0000(data)).append("' ");
			sql.append(" and contacorrente.habilitarRegistroPix = true ");
			sql.append(" and banco.integracaoPix = true ");
			sql.append(" and banco.tipoCobrancaPixEnum = 'IMEDIATA' ");
			sql.append(" order by pixcontacorrente.datageracao ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
			return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public PixContaCorrenteVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) {
		try {
			ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
			String sql = "SELECT * FROM pixContaCorrente WHERE codigo = ?";
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, codigoPrm);
			if (!tabelaResultado.next()) {
				throw new StreamSeiException("Dados Não Encontrados ( PixVO ).");
			}
			return (montarDados(tabelaResultado, nivelMontarDados, usuario));
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	
	private List<PixContaCorrenteVO> montarDadosConsultaBasica(SqlRowSet tabelaResultado) throws Exception {
		List<PixContaCorrenteVO> vetResultado = new ArrayList<>(0);
		while (tabelaResultado.next()) {
			PixContaCorrenteVO obj = new PixContaCorrenteVO();
			montarDadosBasico(obj, tabelaResultado);
			vetResultado.add(obj);
		}
		return vetResultado;
	}
	
	private void montarDadosBasico(PixContaCorrenteVO obj, SqlRowSet dadosSQL) {
		obj.setNovoObj(Boolean.FALSE);
		obj.setCodigo((dadosSQL.getInt("pixContaCorrente.codigo")));
		obj.setDataGeracao(dadosSQL.getTimestamp("pixContaCorrente.datageracao"));
		obj.setSituacaoPixEnum(SituacaoPixEnum.valueOf(dadosSQL.getString("pixContaCorrente.situacaoPixEnum")));	
		obj.setStatusPixEnum(StatusPixEnum.valueOf(dadosSQL.getString("pixContaCorrente.statusPixEnum")));
		obj.setTxid(dadosSQL.getString("pixContaCorrente.txid"));
		obj.setNossoNumero(dadosSQL.getString("pixContaCorrente.nossoNumero"));
		obj.getContaReceberVO().setCodigo(dadosSQL.getInt("pixContaCorrente.contareceber"));
		obj.setValor(dadosSQL.getDouble("pixContaCorrente.valor"));
		obj.setValorContaReceberEnvio(dadosSQL.getDouble("pixContaCorrente.valorContaReceberEnvio"));
		

		obj.getContaCorrenteVO().setCodigo(dadosSQL.getInt("contacorrente.codigo"));
		obj.getContaCorrenteVO().setNomeApresentacaoSistema(dadosSQL.getString("contacorrente.nomeApresentacaoSistema"));
		obj.getContaCorrenteVO().setNumero(dadosSQL.getString("contacorrente.numero"));
		obj.getContaCorrenteVO().setDigito(dadosSQL.getString("contacorrente.digito"));
		obj.getContaCorrenteVO().setTipoContaCorrenteEnum(TipoContaCorrenteEnum.valueOf(dadosSQL.getString("contacorrente.tipoContaCorrenteEnum")));
		obj.getContaCorrenteVO().setSituacao(dadosSQL.getString("contacorrente.situacao"));
		obj.getContaCorrenteVO().setCarteira(dadosSQL.getString("contacorrente.carteira"));
		
		
		obj.getContaCorrenteVO().getAgencia().setCodigo(dadosSQL.getInt("agencia.codigo"));
		obj.getContaCorrenteVO().getAgencia().setNome(dadosSQL.getString("agencia.nome"));
		
		obj.getContaCorrenteVO().getAgencia().getBanco().setCodigo(dadosSQL.getInt("banco.codigo"));
		obj.getContaCorrenteVO().getAgencia().getBanco().setNome(dadosSQL.getString("banco.nome"));

		obj.getUsuarioVO().setCodigo(dadosSQL.getInt("u.codigo"));
		obj.getUsuarioVO().setNome(dadosSQL.getString("u.nome"));
		obj.getUsuarioVO().getPessoa().setCodigo(dadosSQL.getInt("p.codigo"));
		obj.getUsuarioVO().getPessoa().setNome(dadosSQL.getString("p.nome"));
		obj.getUsuarioVO().getPessoa().setEmail(dadosSQL.getString("p.email"));
		obj.getUsuarioVO().getPessoa().setEmail2(dadosSQL.getString("p.email2"));
	}
	
	private List<PixContaCorrenteVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<PixContaCorrenteVO> vetResultado = new ArrayList<>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	private PixContaCorrenteVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) {
		try {
			PixContaCorrenteVO obj = new PixContaCorrenteVO();
			obj.setNovoObj(Boolean.FALSE);
			obj.setCodigo(dadosSQL.getInt("codigo"));
			obj.setDataGeracao(dadosSQL.getTimestamp("dataGeracao"));
			obj.setTempoExpiracao(dadosSQL.getInt("tempoExpiracao"));
			obj.setTxid(dadosSQL.getString("txid"));			
			obj.setQrCode(dadosSQL.getString("qrCode"));
			obj.setJsonRetorno(dadosSQL.getString("jsonRetorno"));
			obj.setJsonEnvio(dadosSQL.getString("jsonEnvio"));
			obj.setNossoNumero(dadosSQL.getString("nossoNumero"));			
			obj.setSituacaoPixEnum(SituacaoPixEnum.valueOf(dadosSQL.getString("situacaoPixEnum")));
			obj.setStatusPixEnum(StatusPixEnum.valueOf(dadosSQL.getString("statusPixEnum")));
			obj.setHorario(dadosSQL.getTimestamp("horario"));
			obj.setValor(dadosSQL.getDouble("valor"));
			obj.setValorContaReceberEnvio(dadosSQL.getDouble("valorContaReceberEnvio"));
			obj.setEndToEndId(dadosSQL.getString("endToEndId"));
			obj.setDocumentoPagador(dadosSQL.getString("documentoPagador"));
			obj.setNomePagador(dadosSQL.getString("nomePagador"));
			obj.setInfoPagador(dadosSQL.getString("infoPagador"));			
			obj.setMotivoCancelamento(dadosSQL.getString("motivoCancelamento"));
			obj.getContaReceberVO().setCodigo(dadosSQL.getInt("contareceber"));
			obj.getContaCorrenteVO().setCodigo(dadosSQL.getInt("contacorrente"));
			obj.getUsuarioVO().setCodigo(dadosSQL.getInt("usuario"));			
			if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
				return obj;
			}
			if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
				obj.setContaCorrenteVO(getAplicacaoControle().getContaCorrenteVO(dadosSQL.getInt("contacorrente"), usuario));
				return obj;
			}
			obj.setContaCorrenteVO(getAplicacaoControle().getContaCorrenteVO(dadosSQL.getInt("contacorrente"), usuario));
			obj.setUsuarioVO(Uteis.montarDadosVO(dadosSQL.getInt("usuario"), UsuarioVO.class, p -> getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(p, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario)));
			return obj;
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

}
