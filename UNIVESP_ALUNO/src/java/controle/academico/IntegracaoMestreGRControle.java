package controle.academico;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import jakarta.annotation.PostConstruct;
import jakarta.faces.model.SelectItem;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;


import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.google.common.util.concurrent.RateLimiter;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import controle.arquitetura.ControleConsultaIntegracaoMestreGR;
import controle.arquitetura.DataModelo;
import controle.arquitetura.SuperControle;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.IntegracaoMestreGRVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ProcessarParalelismo;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.OperacaoTempoRealMestreGREnum;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import relatorio.controle.arquitetura.SuperControleRelatorio;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das
 * páginas turmaChamadoIntegracaoMestreGRForm.jsp com as funcionalidades da
 * classe <code>IntegracaoMestreGR</code>. Implemtação da camada controle
 * (Backing Bean).
 *
 * @see SuperControle
 * @see IntegracaoMestreGRControle
 * @see IntegracaoMestreGRVO
 */

@Controller("IntegracaoMestreGRControle")
@Scope("viewScope")
@Lazy
@SuppressWarnings("unchecked")
public class IntegracaoMestreGRControle extends SuperControleRelatorio {

	private static final long serialVersionUID = 1L;

	private static final int QTD_ITENS_POR_LOTE = 500;

	private static final int QTD_ITENS_POR_VEZ_EXCEL = 500;

	private OperacaoTempoRealMestreGREnum tipoLote;

	private String anoLote;
	private String semestre;
	private List<SelectItem> listaSelectItemSemestre;
	private Integer bimestre;
	private List<SelectItem> listaSelectItemBimestre;

	private DisciplinaVO disciplinaVO;
	private List<DisciplinaVO> listaConsultaDisciplina;
	private List<SelectItem> tipoConsultaComboDisciplina;
	private List<SelectItem> listaSelectItemOrigem;
	private List<SelectItem> listaSelectItemNivelEducacional;
	private String campoConsultaDisciplina;
	private String valorConsultaDisciplina;

	private String unidadeEnsinoApresentar;
	private String codigoUnidadesDeEnsinosSelecionadas;

	private String cursoApresentar;
	private String codigoCursosSelecionados;

	private IntegracaoMestreGRVO integracaoMestreGRVORegistro;
	private List<IntegracaoMestreGRVO> cargaInicialIntegracaoMestreGRVO;

	private ControleConsultaIntegracaoMestreGR controleConsultaIntegracaoMestreGR;
	private DataModelo controleConsultaIntegracaoLote;
	private DataModelo controleConsultaRegistroIntegracaoLote;
	private DataModelo controleConsultaErroRegistroIntegracaoLote;
	private DataModelo controleConsultaLogRegistroIntegracao;

	private Map<String, Object> filtros = new HashMap<>();
	private Map<String, Object> filtroLoteSelecionado = new HashMap<>();

	private Date dataInicioRequerimento;
	private Date dataFimRequerimento;
	private String caminhoUrl;
	private String nomeArquivo;
	private Boolean isDowloadArquivo = Boolean.FALSE;
	private TipoNivelEducacional nivelEducacional;

	public IntegracaoMestreGRControle() throws Exception {
		super();
	}

	@PostConstruct
	public void inicializarIntegracaoMestreGR() {
		try {
			montarSemestreAtual();
			consultarUnidadeEnsinoFiltroRelatorio("");
			verificarTodasUnidadesSelecionadas();
			consultarIntegracaoLoteExistentes();
			setMensagemID("msg_entre_prmconsulta");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Consome o recurso da API de Integracao Mestre GR Gera o codigo dos lotes que
	 * forem passados como parametro o modulo
	 *
	 * @param integracaoMestreGR
	 * @return Integer
	 * @see #moduloRegistroIntegracao(String)
	 */
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Integer gerarCodigoLoteIntegracao(IntegracaoMestreGRVO integracaoMestreGR,
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws ConsistirException {
		try {
			if (!configuracaoGeralSistemaVO.getHabilitarIntegracaoSistemaProvas()) {
				throw new ConsistirException("Integração Sistema de Provas está desativada na configuração geral");
			}
			HttpResponse<String> jsonResponse = Unirest
					.post(configuracaoGeralSistemaVO.getIntegracaoMestreGRURLBaseAPI() + "/ws/api/"
							+ configuracaoGeralSistemaVO.getTokenIntegracaoSistemasProvaMestreGR() + "/lote/save")
					.header("Content-Type", "application/json").connectTimeout(30000)
					.body(integracaoMestreGR.getDadosEnvio()).asString();
			JSONObject responseJson = new JSONObject(jsonResponse.getBody());
			integracaoMestreGR.setSituacao("AGUARDANDO");
			integracaoMestreGR.setCodigoLote(responseJson.getJSONObject("data").getInt("id"));
			validarResponseLote(jsonResponse, integracaoMestreGR);
			return responseJson.getJSONObject("data").getInt("id");
		} catch (Exception e) {
			getProgressBarIntegracaoMestreGR().encerrarForcado();
			setMensagemID("msg_IntegracaoMestreGR_erro_gerar_lote", Uteis.ERRO, Boolean.TRUE);
			throw new ConsistirException("Erro ao Gerar Lote");
		}
	}

	/**
	 * Consome o recurso da API de Integracao Mestre GR Realiza a criacao,
	 * atualizacao de um lote apartir do codigo gerado em gerarCodigoLote
	 *
	 * @param integracaoMestreGR
	 * @return Integer
	 */
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public HttpResponse<String> integrarLote(IntegracaoMestreGRVO integracaoMestreGR,
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws ConsistirException {
		try {
			if (!configuracaoGeralSistemaVO.getHabilitarIntegracaoSistemaProvas()) {
				throw new ConsistirException("Integração Sistema de Provas está desativada na configuração geral");
			}
			HttpResponse<String> jsonResponse = Unirest
					.post(configuracaoGeralSistemaVO.getIntegracaoMestreGRURLBaseAPI() + "/ws/api/"
							+ configuracaoGeralSistemaVO.getTokenIntegracaoSistemasProvaMestreGR() + "/lote/saveline")
					.header("Content-Type", "application/json").connectTimeout(30000)
					.body(integracaoMestreGR.getDadosEnvio()).asString();
			integracaoMestreGR.setSituacao("TRANSMITIDO");
			return jsonResponse;
		} catch (Exception e) {
			getProgressBarIntegracaoMestreGR().encerrarForcado();
			getProgressBarIntegracaoMestreGR().getSuperControle().setMensagemID("msg_IntegracaoMestreGR_erro_gerar_lote", Uteis.ERRO, Boolean.TRUE);
			throw new ConsistirException("Erro ao integrar lote: "+e.getMessage());
		}
	}

	public String montarNovoLoteJson(IntegracaoMestreGRVO config) {
		JsonObject dataJson = new JsonObject();
		dataJson.addProperty("descricao", config.getNomeLote());
		dataJson.addProperty("status", config.getStatus());
		dataJson.addProperty("modulo", config.getModulo());
		return dataJson.toString();
	}

	/**
	 * Valida as respostas das integracoes de maneira padronizada
	 *
	 * @param jsonResponse
	 * @param integracaoMestreGR
	 * @throws Exception
	 */
	private void validarResponseLote(HttpResponse<String> jsonResponse, IntegracaoMestreGRVO integracaoMestreGR)
			throws Exception {
		if (jsonResponse.getBody().trim().isEmpty()) {
			throw new Exception("Não foi encontrado o lote " + integracaoMestreGR.getCodigoLote()
					+ " ou o lote já foi transmitido.");
		}
		try {
		JSONObject responseJson = new JSONObject(jsonResponse.getBody());
		switch (jsonResponse.getStatus()) {
		case 200:

			// Se for cadastro
			if ("success".equalsIgnoreCase(responseJson.optString("status"))
					&& integracaoMestreGR.getSituacao().equals("AGUARDANDO")) {
				integracaoMestreGR.setDadosRetorno(responseJson.toString());
				integracaoMestreGR.setCreated(new Date());
				persistirRegistroIntegracao(integracaoMestreGR);
				getProgressBarIntegracaoMestreGR().getSuperControle().setMensagemID("msg_IntegracaoMestreGR_lote_gerado_com_sucesso", Uteis.SUCESSO, Boolean.TRUE);
			}

			// Se for transmissao ou lote selecionado
			if ("success".equalsIgnoreCase(responseJson.optString("status"))
					&& integracaoMestreGR.getSituacao().equals("TRANSMITIDO")) {
				integracaoMestreGR.setDadosRetorno(responseJson.toString());
				integracaoMestreGR.setDataTransmissao(new Date());
				alterarRegistroIntegracao(integracaoMestreGR);
				alterarIntegracaoItem(integracaoMestreGR);
				getProgressBarIntegracaoMestreGR().getSuperControle().setMensagemID("msg_IntegracaoMestreGR_lote_transmitido_com_sucesso", Uteis.SUCESSO, Boolean.TRUE);
			}

			if ("error".equalsIgnoreCase(responseJson.optString("status"))
					&& integracaoMestreGR.getSituacao().equals("TRANSMITIDO")) {
				integracaoMestreGR.setSituacao("ERRO");
				integracaoMestreGR.setDataTransmissao(new Date());
				integracaoMestreGR.setDadosRetorno(responseJson.toString());
				integracaoMestreGR.setMensagemErro(responseJson.toString());
				if (responseJson.optString("data").equals("[]")) {
					integracaoMestreGR.setMensagemErro("Retornou null, []");
				}
//                    System.out.println("Resposta api: " + responseJson.toString());
				getProgressBarIntegracaoMestreGR().getSuperControle().setMensagemID("msg_IntegracaoMestreGR_erro_transmitir_lote", Uteis.ERRO, Boolean.TRUE);
				alterarRegistroIntegracao(integracaoMestreGR);
				alterarIntegracaoItem(integracaoMestreGR);
				getProgressBarIntegracaoMestreGR().setForcarEncerramento(true);
			}

			if ("error".equalsIgnoreCase(responseJson.optString("status"))
					&& integracaoMestreGR.getSituacao().equals("AGUARDANDO")) {
				integracaoMestreGR.setSituacao("ERRO");
				integracaoMestreGR.setDataTransmissao(new Date());
				integracaoMestreGR.setDadosRetorno(responseJson.toString());
				integracaoMestreGR.setMensagemErro(responseJson.toString());
				if (responseJson.optString("data").equals("[]")) {
					integracaoMestreGR.setMensagemErro("Retornou null, []");
				}
//                    System.out.println("Resposta api: " + responseJson.toString());
				getProgressBarIntegracaoMestreGR().getSuperControle().setMensagemID("msg_IntegracaoMestreGR_erro_gerar_lote", Uteis.ERRO, Boolean.TRUE);
				alterarRegistroIntegracao(integracaoMestreGR);
				persistirRegistroIntegracaoItem(integracaoMestreGR);
				getProgressBarIntegracaoMestreGR().setForcarEncerramento(true);
			}
			break;
		case 400:
			getProgressBarIntegracaoMestreGR().getSuperControle().setMensagemID("msg_IntegracaoMestreGR_erro_400_integracao", Uteis.ERRO, Boolean.TRUE);
			integracaoMestreGR.setSituacao("ERRO");
			integracaoMestreGR.setDataTransmissao(new Date());
			integracaoMestreGR.setDadosRetorno(responseJson.toString());
			integracaoMestreGR.setMensagemErro(responseJson.toString());
			if (responseJson.optString("data").equals("[]")) {
				integracaoMestreGR.setMensagemErro("Retornou null, []");
			}
//                    System.out.println("Resposta api: " + responseJson.toString());
			alterarRegistroIntegracao(integracaoMestreGR);
			persistirRegistroIntegracaoItem(integracaoMestreGR);
			getProgressBarIntegracaoMestreGR().setForcarEncerramento(true);
			break;
		default:
			getProgressBarIntegracaoMestreGR().getSuperControle().setMensagemID("msg_IntegracaoMestreGR_erro_integracao_generico", Uteis.ERRO, Boolean.TRUE);
			integracaoMestreGR.setSituacao("ERRO");
			integracaoMestreGR.setDataTransmissao(new Date());
			integracaoMestreGR.setDadosRetorno(responseJson.toString());
			integracaoMestreGR.setMensagemErro(responseJson.toString());
			if (responseJson.optString("data").equals("[]")) {
				integracaoMestreGR.setMensagemErro("Retornou null, []");
			}
//                    System.out.println("Resposta api: " + responseJson.toString());
			alterarRegistroIntegracao(integracaoMestreGR);
			persistirRegistroIntegracaoItem(integracaoMestreGR);
			getProgressBarIntegracaoMestreGR().setForcarEncerramento(true);		
		
		}
		}catch (Exception e) {
			integracaoMestreGR.setSituacao("ERRO");
			integracaoMestreGR.setDataTransmissao(new Date());
			integracaoMestreGR.setDadosRetorno(jsonResponse.getBody().toString());
			integracaoMestreGR.setMensagemErro(jsonResponse.getBody().toString());
			alterarRegistroIntegracao(integracaoMestreGR);
			persistirRegistroIntegracaoItem(integracaoMestreGR);
			getProgressBarIntegracaoMestreGR().setForcarEncerramento(true);		
			throw e;
		}
	}

	public void consultarRegistroIntegracaoLote() {
		try {
			getControleConsultaRegistroIntegracaoLote().setLimitePorPagina(10);
			getFacadeFactory().getIntegracaoMestreGRInterfaceFacade().consultarRegistroIntegracaoLote(
					mapeamentoFiltros(), getControleConsultaRegistroIntegracaoLote(), false,
					getProgressBarIntegracaoMestreGR().getUsuarioVO());
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	private void persistirRegistroIntegracao(IntegracaoMestreGRVO integracaoMestreGR) {
		try {
			getFacadeFactory().getIntegracaoMestreGRInterfaceFacade().incluir(integracaoMestreGR,
					getProgressBarIntegracaoMestreGR().getUsuarioVO());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			throw new RuntimeException(e);
		}
	}

	private void alterarRegistroIntegracao(IntegracaoMestreGRVO integracaoMestreGR) {
		try {
			getFacadeFactory().getIntegracaoMestreGRInterfaceFacade().atualizar(integracaoMestreGR, getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			throw new RuntimeException(e);
		}
	}

	private void alterarIntegracaoItem(IntegracaoMestreGRVO integracaoMestreGR) {
		try {
			getFacadeFactory().getIntegracaoMestreGRInterfaceFacade().atualizarItemIntegracao(integracaoMestreGR,
					getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			throw new RuntimeException(e);
		}
	}

	private void persistirRegistroIntegracaoItem(IntegracaoMestreGRVO integracaoMestreGR) {
		try {
			getFacadeFactory().getIntegracaoMestreGRInterfaceFacade().incluirItemIntegracao(integracaoMestreGR,
					getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			throw new RuntimeException(e);
		}
	}

	/**
	 * Responsavel por Transimitir o lote selecionado com o array json ja existente
	 * no DB
	 */
	public void transmitirLoteSelecionado() {
		try {			
			setCargaInicialIntegracaoMestreGRVO(getFacadeFactory().getIntegracaoMestreGRInterfaceFacade()
					.consultarItensIntegracaoPorCodigo(((Integer)getFiltroLoteSelecionado().get("codigointegracao")), getControleConsultaIntegracaoLote(),
							getProgressBarIntegracaoMestreGR().getUsuarioVO()));
			
			if (!getCargaInicialIntegracaoMestreGRVO().isEmpty()) {
				if (getProgressBarIntegracaoMestreGR().getForcarEncerramento()) {
					return;
				}
				getProgressBarIntegracaoMestreGR().incrementar();
				getProgressBarIntegracaoMestreGR().setMaxValue(getCargaInicialIntegracaoMestreGRVO().size()+2);
				Integer codigoIntegracao = new Integer(getFiltroLoteSelecionado().get("codigointegracao").toString());
				double permitsPerSecond = 1000.0 / Uteis.delayMillisIntegracaoMestreGR;
				RateLimiter limiter = RateLimiter.create(permitsPerSecond);
				int x = 1;				
				for (IntegracaoMestreGRVO lote: getCargaInicialIntegracaoMestreGRVO()) {
					if(getProgressBarIntegracaoMestreGR().getForcarEncerramento()) {
						lote.setSituacao("INTERROMPIDO");
						alterarRegistroIntegracao(lote);						
						return;
					}					
					getProgressBarIntegracaoMestreGR().setStatus("Transmitindo lote "+(x)+" de "+getCargaInicialIntegracaoMestreGRVO().size());
					limiter.acquire();
					lote.setCodigo(codigoIntegracao);					
					validarResponseLote(integrarLote(lote, getProgressBarIntegracaoMestreGR().getConfiguracaoGeralSistemaVO()), lote);
					if(getProgressBarIntegracaoMestreGR().getForcarEncerramento() && lote.getSituacao().equals("ERRO")) {						
						return;
					}
					getProgressBarIntegracaoMestreGR().incrementar();
					x++;
				}
							
				getFacadeFactory().getProcessamentoFilaThreadsStrategy().aguardarProcessamentoFila(getProgressBarIntegracaoMestreGR(), "Fechando transmissão dos lotes");
				
				
			}else {
				throw new Exception("Não foi encontrado nenhum lote a ser trasmitido.");
			}		
		} catch (Exception e) {
			getProgressBarIntegracaoMestreGR().getSuperControle().setMensagemDetalhada("msg_erro", e.getMessage());
		}finally {
			Uteis.liberarListaMemoria(getCargaInicialIntegracaoMestreGRVO());
			limparTipoLote();
			consultarRegistroIntegracaoLote();
			getProgressBarIntegracaoMestreGR().incrementar();
			getProgressBarIntegracaoMestreGR().setForcarEncerramento(true);
			getProgressBarIntegracaoMestreGR().encerrarForcado();	
		}
	}

	/**
	 * Excluir Registro de integracao somente quando o status for aguardando
	 */
	public void excluirRegistroLoteIntegracao() {
		try {
			getFacadeFactory().getIntegracaoMestreGRInterfaceFacade()
					.excluir(getControleConsultaIntegracaoMestreGR().getCodigoIntegracao(), getUsuarioLogado());
			getControleConsultaIntegracaoLote().setLimitePorPagina(10);
			limparTipoLote();
			limparFiltros();
			consultarRegistroIntegracaoLote();
			setMensagemID("msg_IntegracaoMestreGR_lote_integracao_excluido", Uteis.SUCESSO, Boolean.TRUE);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Faz a consulta da carga que vai ser convertida em Json, realiza o registro e
	 * deixa disponivel para Transmitir
	 *
	 * @param gerarIntegracao
	 * @throws Exception
	 */
	public void gerarLoteIntegracao() throws Exception {
		List<IntegracaoMestreGRVO> cargaLote = null;
		try {

			
			cargaLote = getFacadeFactory().getIntegracaoMestreGRInterfaceFacade()
					.consultarCargaLote(getTipoLote(), mapeamentoFiltros(), getControleConsultaIntegracaoLote(), false,
							getProgressBarIntegracaoMestreGR().getUsuarioVO(),
							getAplicacaoControle().getProgressBarIntegracaoMestreGR());
			if (cargaLote.isEmpty()) {
				getProgressBarIntegracaoMestreGR().getSuperControle()
						.setMensagemID("msg_IntegracaoMestreGR_nenhum_registro", Uteis.ERRO, Boolean.TRUE);
			}

			if (!cargaLote.isEmpty()) {
				if (getProgressBarIntegracaoMestreGR().getForcarEncerramento()) {
					return;
				}
				IntegracaoMestreGRVO gerarIntegracao = new IntegracaoMestreGRVO();
				gerarIntegracao.setOrigem(getTipoLote());
				gerarIntegracao.setModulo("TURMAS");
				gerarIntegracao.setNomeCreated(getProgressBarIntegracaoMestreGR().getUsuarioVO().getNome());
				gerarIntegracao.setCodigoCreated(getProgressBarIntegracaoMestreGR().getUsuarioVO().getCodigo());
				gerarIntegracao.setStatus("C");
				gerarIntegracao.setQtdeRegistros(cargaLote.size());
				if((cargaLote.size() / QTD_ITENS_POR_LOTE) < 1) {
					getProgressBarIntegracaoMestreGR().setMaxValue(4);
				}else {
					getProgressBarIntegracaoMestreGR().setMaxValue((cargaLote.size() / QTD_ITENS_POR_LOTE) + 3);
				}
				getProgressBarIntegracaoMestreGR().incrementar();
				// Nome do Lote Atual
				String nomeLoteAtual = getFacadeFactory().getIntegracaoMestreGRInterfaceFacade().montarNomeLote(
						getTipoLote(), new Integer(getAnoLote()), new Integer(getSemestre()),
						new Integer(getBimestre()));
				gerarIntegracao.setNomeLote(nomeLoteAtual);
				// Montar o Json para novo Lote
				gerarIntegracao.setDadosEnvio(montarNovoLoteJson(gerarIntegracao));
				// Inserir item de primeira busca
				gerarIntegracao.setCreated(new Date());
				gerarIntegracao.setOrigem(getTipoLote());
				gerarIntegracao.setUnidadeEnsinos(getUnidadeEnsinoApresentar());
				gerarIntegracao.setNivelEducacional(getNivelEducacional());
				gerarIntegracao.setCursos(getCursoApresentar());
				gerarIntegracao.getDisciplinaVO().setCodigo(getDisciplinaVO().getCodigo());
				gerarIntegracao.getDisciplinaVO().setAbreviatura(getDisciplinaVO().getAbreviatura());
				gerarIntegracao.getDisciplinaVO().setNome(getDisciplinaVO().getNome());
				gerarIntegracao.setAno(getAnoLote());
				gerarIntegracao.setSemestre(getSemestre());
				gerarIntegracao.setBimestre(Integer.valueOf(getBimestre()));

				if (getTipoLote().equals(OperacaoTempoRealMestreGREnum.TURMA_2CH)) {
					gerarIntegracao.setPeriodoRequerimentoInicio(getDataInicioRequerimento());
					gerarIntegracao.setPeriodoRequerimentoTermino(getDataFimRequerimento());
				}				
				// Gerar Codigo
				Integer codigoLote = gerarCodigoLoteIntegracao(gerarIntegracao, getProgressBarIntegracaoMestreGR().getConfiguracaoGeralSistemaVO());
				getProgressBarIntegracaoMestreGR().getParams().put("codigointegracao", gerarIntegracao.getCodigo());
				getProgressBarIntegracaoMestreGR().getParams().put("codigoiditem", gerarIntegracao.getIdItem());
				
				int tamanhoLista = cargaLote.size();
				int totalRegistrosProcessados = 0; 
				for (int i = 0; i < tamanhoLista; i += QTD_ITENS_POR_LOTE) {
					getProgressBarIntegracaoMestreGR()
							.setStatus("Processando lote " + getProgressBarIntegracaoMestreGR().getProgresso() + " de "
									+ (getProgressBarIntegracaoMestreGR().getMaxValue() - 2)+" - total de registros processados "+totalRegistrosProcessados+" de "+cargaLote.size());
					if (getProgressBarIntegracaoMestreGR().getForcarEncerramento()) {
						break;
					}
					int indiceFinal = Math.min(i + QTD_ITENS_POR_LOTE, tamanhoLista);
					List<IntegracaoMestreGRVO> lote = cargaLote.subList(i, indiceFinal);
					totalRegistrosProcessados += lote.size();
					IntegracaoMestreGRVO loteAtual = new IntegracaoMestreGRVO();
					loteAtual.setCodigo(gerarIntegracao.getCodigo());
					loteAtual.setDadosEnvio(converterListaEmJsonCodigo(lote, codigoLote).toString());
					loteAtual.setDadosRetorno("");
					getFacadeFactory().getIntegracaoMestreGRInterfaceFacade().incluirItemIntegracao(loteAtual,
							getProgressBarIntegracaoMestreGR().getUsuarioVO());
					getProgressBarIntegracaoMestreGR().incrementar();
					loteAtual.setDadosEnvio(null);
					loteAtual.setDadosRetorno(null);
				}
				limparTipoLote();
				getProgressBarIntegracaoMestreGR().setStatus("Fechando lotes...");
				consultarRegistroIntegracaoLote();
				getProgressBarIntegracaoMestreGR().incrementar();
				getProgressBarIntegracaoMestreGR().setForcarEncerramento(true);
			}
		} catch (Exception e) {
			getProgressBarIntegracaoMestreGR().getSuperControle().setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			Uteis.liberarListaMemoria(cargaLote);
			getProgressBarIntegracaoMestreGR().encerrarForcado();
		}
	}

	private JsonArray converterListaEmJsonCodigo(List<IntegracaoMestreGRVO> lotes, Integer codigo) {
//		getProgressBarIntegracaoMestreGR().setStatus("Convertendo Registros Em Lote");
		lotes.forEach(l -> {
			l.setCodigoLote(codigo);
		});
		return getFacadeFactory().getIntegracaoMestreGRInterfaceFacade().montarLoteJson(lotes,
				getControleConsultaIntegracaoMestreGR().getOrigem());
	}

	public void realizarInicioProgressBarGerarLote() {
		try {
			setMensagemID("msg_dados_consultados");
			testeDeConexaoIncial();
			limparCargaInicialIntegracaoMestreGRVO();
			limparIntegracaoMestreGRVORegistro();
			setFazerDownload(false);
			setOncompleteModal("");
			getAplicacaoControle().setProgressBarIntegracaoMestreGR(new ProgressBarVO());
			getProgressBarIntegracaoMestreGR().resetar();
			getProgressBarIntegracaoMestreGR().setAplicacaoControle(getAplicacaoControle());
			getProgressBarIntegracaoMestreGR().setUsuarioVO(getUsuarioLogadoClone());
			validarFiltros(getAnoLote(), getSemestre(), getBimestre());
			if (getTipoLote().equals(OperacaoTempoRealMestreGREnum.TURMA_2CH)) {
				if (!validarDataRequerimento()) {
					return;
				}
				if (!validarBimestreLoteRequerimento(getBimestre())) {
					return;
				}
				;
			}
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoPadraoSistema();
			getProgressBarIntegracaoMestreGR().setConfiguracaoGeralSistemaVO(configuracaoGeralSistemaVO);
			if (!configuracaoGeralSistemaVO.getHabilitarIntegracaoSistemaProvas()) {
				throw new ConsistirException("Integração Sistema de Provas está desativada na configuração geral");
			}			
			getControleConsultaIntegracaoLote().setPaginaAtual(1);
			getControleConsultaIntegracaoLote().setPage(1);
			getControleConsultaIntegracaoLote().setLimitePorPagina(QTD_ITENS_POR_LOTE);
			getProgressBarIntegracaoMestreGR().iniciar(0L, 2, "Consultando dados para geração do lote", true, this,
					"gerarLoteIntegracao");
			if (getTipoLote().equals(OperacaoTempoRealMestreGREnum.TURMA_2CH)) {
				setOncompleteModal("PF('panelLoteIntegracaoAlunoSegundaChamada').hide()");
			} else if (getTipoLote().equals(OperacaoTempoRealMestreGREnum.TURMA)) {
				setOncompleteModal("PF('panelLoteIntegracaoAluno').hide()");
			} else if (getTipoLote().equals(OperacaoTempoRealMestreGREnum.TURMA_EXAME)) {
				setOncompleteModal("PF('panelLoteIntegracaoAlunoExame').hide()");
			}
		} catch (Exception e) {
			getProgressBarIntegracaoMestreGR().encerrarForcado();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void realizarInicioProgressBarGerarTransmitirLote() {
		try {
			setMensagemID("msg_dados_consultados");
			setFazerDownload(false);
			testeDeConexaoIncial();
			limparCargaInicialIntegracaoMestreGRVO();
			limparIntegracaoMestreGRVORegistro();

			if (getTipoLote().equals(OperacaoTempoRealMestreGREnum.TURMA_2CH)) {
				if (!validarDataRequerimento()) {
					return;
				}
				if (!validarBimestreLoteRequerimento(getBimestre())) {
					return;
				}
				;
			}
			setOncompleteModal("");
			getAplicacaoControle().setProgressBarIntegracaoMestreGR(new ProgressBarVO());
			getProgressBarIntegracaoMestreGR().resetar();
			getProgressBarIntegracaoMestreGR().setAplicacaoControle(getAplicacaoControle());
			getProgressBarIntegracaoMestreGR().setUsuarioVO(getUsuarioLogadoClone());
			getControleConsultaIntegracaoLote().setPaginaAtual(1);
			getControleConsultaIntegracaoLote().setPage(1);
			getControleConsultaIntegracaoLote().setLimitePorPagina(QTD_ITENS_POR_LOTE);
			List<IntegracaoMestreGRVO> cargaInicial = getFacadeFactory().getIntegracaoMestreGRInterfaceFacade()
					.consultarCargaLote(getTipoLote(), mapeamentoFiltros(), getControleConsultaIntegracaoLote(), false,
							getProgressBarIntegracaoMestreGR().getUsuarioVO(),
							getAplicacaoControle().getProgressBarIntegracaoMestreGR());
			if (!cargaInicial.isEmpty()) {
				setCargaInicialIntegracaoMestreGRVO(cargaInicial);
				getProgressBarIntegracaoMestreGR().iniciar(0L,
						getControleConsultaIntegracaoLote().getTotalRegistrosEncontrados(),
						"Iniciando Processamento da(s) operações.", true, this, "gerarTransmitirPorTipoLote");
			}
			if (cargaInicial.isEmpty()) {
				setMensagemID("msg_IntegracaoMestreGR_nenhum_registro", Uteis.ERRO, Boolean.TRUE);
			}
		} catch (Exception e) {
			getProgressBarIntegracaoMestreGR().encerrarForcado();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void realizarInicioProgressBarTransmitirLoteSelecionado() {
		try {
			setMensagemID("msg_dados_consultados");
			setFazerDownload(false);
			testeDeConexaoIncial();
			limparCargaInicialIntegracaoMestreGRVO();
			limparIntegracaoMestreGRVORegistro();
			Map<String, Object> novoFiltro = mapeamentoFiltros();
			novoFiltro.put("codigointegracao", getControleConsultaIntegracaoMestreGR().getCodigoIntegracao());
			novoFiltro.put("situacao", "AGUARDANDO");
			setFiltroLoteSelecionado(novoFiltro);

			getControleConsultaIntegracaoLote().setLimitePorPagina(1);
			getControleConsultaIntegracaoLote().setPaginaAtual(1);
			getControleConsultaIntegracaoLote().setPage(1);

			setOncompleteModal("");
			getAplicacaoControle().setProgressBarIntegracaoMestreGR(new ProgressBarVO());
			getProgressBarIntegracaoMestreGR().resetar();
			getProgressBarIntegracaoMestreGR().setAplicacaoControle(getAplicacaoControle());
			getProgressBarIntegracaoMestreGR().setUsuarioVO(getUsuarioLogadoClone());
			getProgressBarIntegracaoMestreGR().setConfiguracaoGeralSistemaVO(getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade());
			getProgressBarIntegracaoMestreGR().iniciar(0L,2,
						"Iniciando Processamento da(s) operações.", true, this, "transmitirLoteSelecionado");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			getProgressBarIntegracaoMestreGR().encerrarForcado();
		}
	}

	@Override
	public void anularDataModelo() {
		setControleConsultaIntegracaoLote(null);
		setControleConsultaRegistroIntegracaoLote(null);
		setControleConsultaErroRegistroIntegracaoLote(null);
		limparIntegracaoMestreGRVORegistro();
	}

	public void scrollerListenerRegistroIntegracaoLote() throws Exception {
		
		consultarRegistroIntegracaoLote();
	}

	public DataModelo getControleConsultaRegistroIntegracaoLote() {
		if (controleConsultaRegistroIntegracaoLote == null) {
			controleConsultaRegistroIntegracaoLote = new DataModelo();
		}
		return controleConsultaRegistroIntegracaoLote;
	}

	public void setControleConsultaRegistroIntegracaoLote(DataModelo controleConsultaRegistroIntegracaoLote) {
		this.controleConsultaRegistroIntegracaoLote = controleConsultaRegistroIntegracaoLote;
	}

	public DataModelo getControleConsultaErroRegistroIntegracaoLote() {
		if (controleConsultaErroRegistroIntegracaoLote == null) {
			controleConsultaErroRegistroIntegracaoLote = new DataModelo();
		}
		return controleConsultaErroRegistroIntegracaoLote;
	}

	public void setControleConsultaErroRegistroIntegracaoLote(DataModelo controleConsultaErroRegistroIntegracaoLote) {
		this.controleConsultaErroRegistroIntegracaoLote = controleConsultaErroRegistroIntegracaoLote;
	}

	public void scrollerListenerErroIntegracaoLote() throws Exception {
		
		consultarErroLote();
	}

	public DataModelo getControleConsultaLogRegistroIntegracaoLote() {
		if (controleConsultaLogRegistroIntegracao == null) {
			controleConsultaLogRegistroIntegracao = new DataModelo();
		}
		return controleConsultaLogRegistroIntegracao;
	}

	public void setControleConsultaLogRegistroIntegracaoLote(DataModelo controleConsultaLogRegistroIntegracao) {
		this.controleConsultaLogRegistroIntegracao = controleConsultaLogRegistroIntegracao;
	}

	public void scrollerListenerLogIntegracaoLote() throws Exception {
		
		consultarLogsAlunos();
	}

	public void scrollerListenerIntegracaoLote() throws Exception {
		
		consultarLoteItem();
	}

	public DataModelo getControleConsultaIntegracaoLote() {
		if (controleConsultaIntegracaoLote == null) {
			controleConsultaIntegracaoLote = new DataModelo();
		}
		return controleConsultaIntegracaoLote;
	}

	public void setControleConsultaIntegracaoLote(DataModelo controleConsultaIntegracaoLote) {
		this.controleConsultaIntegracaoLote = controleConsultaIntegracaoLote;
	}

	public void consultarIntegracaoLote(String tipoLote) {
		setOncompleteModal("");
		if (validarFiltros(getAnoLote(), getSemestre(), getBimestre())) {
			getControleConsultaRegistroIntegracaoLote().setPaginaAtual(1);
			getControleConsultaRegistroIntegracaoLote().setPage(1);
			setTipoLote(OperacaoTempoRealMestreGREnum.valueOf(tipoLote));
			consultarRegistroIntegracaoLote();
			switch (getTipoLote()) {
			case TURMA:
				setOncompleteModal("PF('panelLoteIntegracaoAluno').show()");
				break;
			case TURMA_2CH:
				setOncompleteModal("PF('panelLoteIntegracaoAlunoSegundaChamada').show()");
				break;
			case TURMA_EXAME:
				setOncompleteModal("PF('panelLoteIntegracaoAlunoExame').show()");
				break;

			default:
				break;
			}
		}
	}

	public void consultarIntegracaoLoteExistentes() {
		setMensagemID("msg_dados_consultados");
		limparTipoLote();
		limparFiltrosItens();
		getControleConsultaRegistroIntegracaoLote().setLimitePorPagina(10);
		getControleConsultaRegistroIntegracaoLote().setPaginaAtual(1);
		getControleConsultaRegistroIntegracaoLote().setPage(1);
		consultarRegistroIntegracaoLote();
	}

	public void consultarIntegracaoLoteExistentesInterromperProgressBar() {
		limparTipoLote();
		limparFiltrosItens();
		getControleConsultaRegistroIntegracaoLote().setPage(1);
		getControleConsultaRegistroIntegracaoLote().setPaginaAtual(1);
		consultarRegistroIntegracaoLote();
	}

	public void consultarLoteItem() {
		try {
			getControleConsultaIntegracaoLote().setLimitePorPagina(10);
			getFacadeFactory().getIntegracaoMestreGRInterfaceFacade().consultarIntegracaoDadosEnvioJson(
					mapeamentoFiltroJson(), getControleConsultaIntegracaoLote(), false, getUsuarioLogado());
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void errosLoteGeralExistentes() {
		setMensagemID("msg_dados_consultados");
		setControleConsultaIntegracaoMestreGR(null);
		getControleConsultaErroRegistroIntegracaoLote().setLimitePorPagina(10);
		getControleConsultaErroRegistroIntegracaoLote().setPaginaAtual(1);
		getControleConsultaErroRegistroIntegracaoLote().setPage(1);
		limparFiltrosLogErro();
		consultarErroLote();
	}

	public void consultarItensLoteFiltrados(Integer codigoIntegracao, String origem) {
		getControleConsultaIntegracaoLote().setPaginaAtual(1);
		getControleConsultaIntegracaoLote().setPage(1);
		getControleConsultaIntegracaoMestreGR().setPaginaAtual(1);
		getControleConsultaIntegracaoMestreGR().setPage(1);
		getControleConsultaIntegracaoMestreGR().setCodigoIntegracao(codigoIntegracao);
		getControleConsultaIntegracaoMestreGR().setOrigem(origem);
		limparFiltrosItens();
		consultarLoteItem();
	}

	public void consultarItensFiltrados() {
		consultarLoteItem();
	}

	public void consultarOperacoesFiltradas() {
		getControleConsultaLogRegistroIntegracaoLote().setOffset(-1);
		consultarLogsAlunos();
	}

	public void consultarLogsAlunosFiltro() {
		getControleConsultaLogRegistroIntegracaoLote().setPage(1);
		getControleConsultaLogRegistroIntegracaoLote().setPaginaAtual(1);
		consultarLogsAlunos();
	}
	
	public void consultarLoteFiltroNivelEducacional() {
		limparCursos();
		consultarCursoFiltroRelatorio("SE", getNivelEducacional().getValor());
		consultarLoteFiltro();
	}

	public void consultarLoteFiltro() {
		limparTipoLote();
		setControleConsultaRegistroIntegracaoLote(new DataModelo());
		getControleConsultaRegistroIntegracaoLote().setLimitePorPagina(10);
		getControleConsultaRegistroIntegracaoLote().setPage(1);
		getControleConsultaRegistroIntegracaoLote().setPaginaAtual(1);
		consultarRegistroIntegracaoLote();
	}

	public void consultarErroLoteSelecionado() {
		try {
			getControleConsultaIntegracaoLote().setLimitePorPagina(10);
			getFacadeFactory().getIntegracaoMestreGRInterfaceFacade().consultarItemErroIntegracaoJson(
					mapeamentoFiltroJsonItemErro(), getControleConsultaErroRegistroIntegracaoLote(), false,
					getUsuarioLogado());
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarIntegracaoLogAlunos() {
		setOncompleteModal("");
		if (validarFiltros(getAnoLote(), getSemestre(), getBimestre())) {
			setMensagemID("msg_dados_consultados");
			limparFiltrosLogOperacoes();
			getControleConsultaLogRegistroIntegracaoLote().setLimitePorPagina(10);
			getControleConsultaLogRegistroIntegracaoLote().setPaginaAtual(1);
			getControleConsultaLogRegistroIntegracaoLote().setPage(1);
			consultarLogsAlunos();
			setOncompleteModal("PF('panelLoteIntegracaoLogAluno').show()");
		}
	}

	private Map<String, Object> mapeamentoFiltrosLog() {
//        limparFiltros();
		getFiltros().put("ano", getAnoLote());
		getFiltros().put("semestre", getSemestre());
//        getFiltros().put("bimestre", getBimestreFiltroLogAlunos());
		getFiltros().put("bimestrenumero", getControleConsultaIntegracaoMestreGR().getBimestre());
		getFiltros().put("unidadeensino", getCodigoUnidadesDeEnsinosSelecionadas());
		getFiltros().put("curso", getCodigoCursosSelecionados());
		getFiltros().put("disciplina", getDisciplinaVO().getCodigo().toString());
		getFiltros().put("origem", getControleConsultaIntegracaoMestreGR().getOrigem());
		getFiltros().put("email", getControleConsultaIntegracaoMestreGR().getEmailAluno());
//        getFiltros().put("matricula", getControleConsultaIntegracaoMestreGR().getMatricula());
		getFiltros().put("codigoAluno", getControleConsultaIntegracaoMestreGR().getCodigoAluno());
//        getFiltros().put("codigosdisciplinas", getControleConsultaIntegracaoMestreGR().getCodigosDisciplinas());
		getFiltros().put("chaveTurma", getControleConsultaIntegracaoMestreGR().getChaveTurma());
		getFiltros().put("nomealuno", getControleConsultaIntegracaoMestreGR().getNomeAluno());
		getFiltros().put("situacao", getControleConsultaIntegracaoMestreGR().getSituacao());
		getFiltros().put("mensagemErro", getControleConsultaIntegracaoMestreGR().getMensagemErro());
		getFiltros().put("created", getControleConsultaIntegracaoMestreGR().getCreated());
		getFiltros().put("createdFinal", getControleConsultaIntegracaoMestreGR().getCreatedFinal());
		return getFiltros();
	}

	public void consultarLogsAlunos() {
		try {
			getControleConsultaLogRegistroIntegracaoLote().setLimitePorPagina(10);
			getFacadeFactory().getIntegracaoMestreGRInterfaceFacade().consultarLogIntegracaoAluno(
					mapeamentoFiltrosLog(), getControleConsultaLogRegistroIntegracaoLote(), false, getUsuarioLogado());
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {

			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarErroLoteInicial() {
		getControleConsultaIntegracaoLote().setPaginaAtual(1);
		getControleConsultaIntegracaoLote().setPage(1);
		consultarErroLote();
	}

	public void consultarErroLote() {
		try {
			getControleConsultaIntegracaoLote().setLimitePorPagina(10);
			getFacadeFactory().getIntegracaoMestreGRInterfaceFacade().consultarErroIntegracaoJson(
					mapeamentoFiltrosLogErro(), getControleConsultaErroRegistroIntegracaoLote(), false,
					getUsuarioLogado());
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	private Map<String, Object> mapeamentoFiltrosLogErro() {
//        limparFiltros();
		getFiltros().put("ano", getAnoLote());
		getFiltros().put("semestre", getSemestre());
		getFiltros().put("bimestre", getBimestre());
		getFiltros().put("codigolote", getControleConsultaIntegracaoMestreGR().getCodigoLote());
		getFiltros().put("matricula", getControleConsultaIntegracaoMestreGR().getMatricula());
		getFiltros().put("nomeDisciplina", getControleConsultaIntegracaoMestreGR().getNomeDisciplina());
		getFiltros().put("nomeLote", getControleConsultaIntegracaoMestreGR().getNomeLote());
		getFiltros().put("origem", getControleConsultaIntegracaoMestreGR().getOrigem());
		getFiltros().put("created", getControleConsultaIntegracaoMestreGR().getCreated());
		getFiltros().put("createdFinal", getControleConsultaIntegracaoMestreGR().getCreatedFinal());
		return getFiltros();
	}

	private Map<String, Object> mapeamentoFiltrosLogExcel() {
//        limparFiltros();
		getFiltros().put("ano", getAnoLote());
		getFiltros().put("semestre", getSemestre());
		getFiltros().put("bimestre", getBimestre());
		return getFiltros();
	}

	private Map<String, Object> mapeamentoFiltrosLote() {
		getFiltros().keySet().retainAll(Collections.singleton("codigointegracao"));
		getFiltros().put("origem", "TURMAS");
		return getFiltros();
	}

	private Map<String, Object> mapeamentoFiltros() {
		limparFiltros();
		getFiltros().put("ano", getAnoLote());
		getFiltros().put("semestre", getSemestre());
		getFiltros().put("bimestre", getBimestre());
		getFiltros().put("unidadeensino", getCodigoUnidadesDeEnsinosSelecionadas());
		getFiltros().put("curso", getCodigoCursosSelecionados());
		getFiltros().put("disciplina", getDisciplinaVO().getCodigo().toString());
		getFiltros().put("codigocreated", getProgressBarIntegracaoMestreGR().getUsuarioVO().getCodigo());
		getFiltros().put("tipolote", getTipoLote());
		getFiltros().put("nivelEducacional", getNivelEducacional());
		getFiltros().put("datainiciorequerimento", dataFormatada(getDataInicioRequerimento(), "yyyy-MM-dd"));
		getFiltros().put("datafimrequerimento", dataFormatada(getDataFimRequerimento(), "yyyy-MM-dd"));
		return getFiltros();
	}

	private Map<String, Object> mapeamentoFiltroJson() {
		limparFiltros();
		getFiltros().put("ano", getControleConsultaIntegracaoMestreGR().getAno());
		getFiltros().put("semestre", getControleConsultaIntegracaoMestreGR().getSemestre());
		getFiltros().put("bimestre", getControleConsultaIntegracaoMestreGR().getBimestre());
		getFiltros().put("codigocreated", getProgressBarIntegracaoMestreGR().getUsuarioVO().getCodigo());
		getFiltros().put("tipolote", getTipoLote());
		getFiltros().put("codigointegracao", getControleConsultaIntegracaoMestreGR().getCodigoIntegracao());
		getFiltros().put("turno", getControleConsultaIntegracaoMestreGR().getTurno());
		getFiltros().put("codigointernoturma", getControleConsultaIntegracaoMestreGR().getCodigoInternoTurma());
		getFiltros().put("nometurma", getControleConsultaIntegracaoMestreGR().getNomeTurma());
		getFiltros().put("origem", getControleConsultaIntegracaoMestreGR().getOrigem());

		getFiltros().put("codigodisciplina", getControleConsultaIntegracaoMestreGR().getCodigoDisciplina());
		getFiltros().put("sigladisciplina", getControleConsultaIntegracaoMestreGR().getSiglaDisciplina());
		getFiltros().put("nomedisciplina", getControleConsultaIntegracaoMestreGR().getNomeDisciplina());

		getFiltros().put("codigointernoaluno", getControleConsultaIntegracaoMestreGR().getCodigoInternoAluno());
		getFiltros().put("codigoaluno", getControleConsultaIntegracaoMestreGR().getCodigoAluno());
		getFiltros().put("nomealuno", getControleConsultaIntegracaoMestreGR().getNomeAluno());
		getFiltros().put("emailaluno", getControleConsultaIntegracaoMestreGR().getEmailAluno());
		getFiltros().put("codigodiasemanaaluno", getControleConsultaIntegracaoMestreGR().getCodigoDiaSemanaAluno());
		getFiltros().put("tempoestendidoaluno", getControleConsultaIntegracaoMestreGR().getTempoEstendidoAluno());
		getFiltros().put("numerocelularaluno", getControleConsultaIntegracaoMestreGR().getNumeroCelularAluno());

		getFiltros().put("codigointernopolo", getControleConsultaIntegracaoMestreGR().getCodigoInternoPolo());
		getFiltros().put("codigopolo", getControleConsultaIntegracaoMestreGR().getCodigoPolo());
		getFiltros().put("descricaopolo", getControleConsultaIntegracaoMestreGR().getDescricaoPolo());

		getFiltros().put("codigointernocurso", getControleConsultaIntegracaoMestreGR().getCodigoInternoCurso());
		getFiltros().put("codigocurso", getControleConsultaIntegracaoMestreGR().getCodigoCurso());
		getFiltros().put("descricaocurso", getControleConsultaIntegracaoMestreGR().getDescricaoCurso());

		return getFiltros();
	}

	private Map<String, Object> mapeamentoFiltroJsonItemErro() {
//        limparFiltros();
		getFiltros().put("codigointegracao", getControleConsultaIntegracaoMestreGR().getCodigoIntegracao());
		getFiltros().put("iditem", getControleConsultaIntegracaoMestreGR().getIdItem());
		return getFiltros();
	}

	private Boolean validarDataRequerimento() {

		// Verifica se as datas são nulas
		if (getDataInicioRequerimento() == null && getDataFimRequerimento() == null) {
			setMensagemID("msg_IntegracaoMestreGR_erro_datas_inicio_fim_nao_informadas", Uteis.ERRO, Boolean.TRUE);
			return false;
		}
		if (getDataInicioRequerimento() == null) {

			setMensagemID("msg_IntegracaoMestreGR_erro_data_inicio_nao_informada", Uteis.ERRO, Boolean.TRUE);
			return false;
		}
		if (getDataFimRequerimento() == null) {

			setMensagemID("msg_IntegracaoMestreGR_erro_data_final_nao_informada", Uteis.ERRO, Boolean.TRUE);
			return false;
		}

		// Verifica se a data de início é anterior à data final
		if (getDataInicioRequerimento().after(getDataFimRequerimento())) {

			setMensagemID("msg_IntegracaoMestreGR_erro_data_inicio_maior_data_final", Uteis.ERRO, Boolean.TRUE);
			return false;
		}

		return true;
	}

	public void marcarTodosCursosAction() throws Exception {
		for (CursoVO cursoVO : getCursoVOs()) {
			cursoVO.setFiltrarCursoVO(getMarcarTodosCursos());
		}
		verificarTodosCursosSelecionados();
	}

	@Override
	public void marcarTodasUnidadesEnsinoAction() {
		for (UnidadeEnsinoVO unidade : getUnidadeEnsinoVOs()) {
			unidade.setFiltrarUnidadeEnsino(getMarcarTodasUnidadeEnsino());

		}
		verificarTodasUnidadesSelecionadas();
	}

	@Override
	public void verificarTodosCursosSelecionados() {
		StringBuilder curso = new StringBuilder();
		StringJoiner codigoCursos = new StringJoiner(", ");
		if (getCursoVOs().size() > 1) {
			for (CursoVO obj : getCursosFiltradosGraduacao()) {
				if (obj.getFiltrarCursoVO()) {
					curso.append(obj.getCodigo()).append(" - ");
					curso.append(obj.getNome()).append("; ");
					codigoCursos.add(obj.getCodigo().toString());
				}
			}
			setCodigoCursosSelecionados(codigoCursos.toString());
			setCursoApresentar(curso.toString());
		} else {
			if (!getCursosFiltradosGraduacao().isEmpty()) {
				if (getCursosFiltradosGraduacao().get(0).getFiltrarCursoVO()) {
					setCursoApresentar(getCursosFiltradosGraduacao().get(0).getNome());
				}
			}
		}
	}

	public void verificarTodasUnidadesSelecionadas() {
		StringBuilder unidade = new StringBuilder();
		StringJoiner codigoUnidades = new StringJoiner(", ");
		if (getUnidadeEnsinoVOs().size() > 1) {
			for (UnidadeEnsinoVO obj : getUnidadeEnsinoVOs()) {
				if (obj.getFiltrarUnidadeEnsino()) {
					unidade.append(obj.getNome().trim()).append("; ");
					codigoUnidades.add(obj.getCodigo().toString());
				}
			}
			setUnidadeEnsinoApresentar(unidade.toString());
			setCodigoUnidadesDeEnsinosSelecionadas(codigoUnidades.toString());
		} else {
			if (!getUnidadeEnsinoVOs().isEmpty()) {
				if (getUnidadeEnsinoVOs().get(0).getFiltrarUnidadeEnsino()) {
					setUnidadeEnsinoApresentar(getUnidadeEnsinoVOs().get(0).getNome());
				}
			}
		}
		consultarCursoFiltroRelatorio("SE", getNivelEducacional().getValor());
	}

	/**
	 * Consome o recurso da API de Integracao Mestre GR Realiza um teste de conexao
	 * utilizando a url e token para validar seu estado
	 */
	public void testeDeConexaoIncial() throws Exception {
		ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade()
				.consultarConfiguracaoPadraoSistema();
		if (!configuracaoGeralSistemaVO.getHabilitarIntegracaoSistemaProvas()) {
			throw new ConsistirException("Integração Sistema de Provas está desativada na configuração geral");
		}
		try {

			HttpResponse<String> jsonResponse = Unirest
					.get(configuracaoGeralSistemaVO.getIntegracaoMestreGRURLBaseAPI() + "/ws/api/"
							+ configuracaoGeralSistemaVO.getTokenIntegracaoSistemasProvaMestreGR() + "/lote/list/2024")
					.header("Content-Type", "application/json").asString();
			if (jsonResponse.getStatus() == 200) {
				JSONObject responseJson = new JSONObject(jsonResponse.getBody());
				if (!"success".equalsIgnoreCase(responseJson.optString("status"))) {
					setMensagemID("msg_IntegracaoMestreGR_conexao_nao_estabelecida", Uteis.ERRO, Boolean.TRUE);
				}
			} else {
				setMensagemID("msg_IntegracaoMestreGR_conexao_nao_estabelecida", Uteis.ERRO, Boolean.TRUE);
			}
		} catch (Exception e) {
			setMensagemID("msg_IntegracaoMestreGR_conexao_nao_estabelecida", Uteis.ERRO, Boolean.TRUE);
		}
	}

	/**
	 * Validar filtros obrigatorios
	 *
	 * @param anoCampo
	 * @param semestreCampo
	 * @param bimestreCampo
	 */
	private Boolean validarFiltros(String anoCampo, String semestreCampo, Integer bimestreCampo) {
		if ((anoCampo == null || anoCampo.trim().isEmpty()) && (anoCampo.trim().length() < 4)
				&& (semestreCampo == null || semestreCampo.equals("0"))
				&& (bimestreCampo == null || bimestreCampo == 0)) {
			setMensagemID("msg_IntegracaoMestreGR_anoSemestreBimestre", Uteis.ERRO, Boolean.TRUE);
			return false;
		}
		// Verifica se o campo Ano está vazio ou inválido
		if (anoCampo == null || anoCampo.trim().isEmpty()) {
			setMensagemID("msg_IntegracaoMestreGR_ano", Uteis.ERRO, Boolean.TRUE);
			return false;
		}
		if (anoCampo.trim().length() < 4) {
			setMensagemID("msg_IntegracaoMestreGR_ano_minimo_digitos", Uteis.ERRO, Boolean.TRUE);
			return false;
		}
		// Verifica se o campo Semestre é inválido
		if (semestreCampo == null || semestreCampo.equals("0")) {
			setMensagemID("msg_IntegracaoMestreGR_semestre", Uteis.ERRO, Boolean.TRUE);
			return false;
		}

		// Verifica se o campo Bimestre é inválido
		if (bimestreCampo == null || bimestreCampo == 0) {
			setMensagemID("msg_IntegracaoMestreGR_bimestre", Uteis.ERRO, Boolean.TRUE);
			return false;
		}
		return true;
	}

	private Boolean validarBimestreLoteRequerimento(Integer bimestreCampo) {
		if (bimestreCampo == null) {

			setMensagemID("O campo BIMESTRE deve ser informado", Uteis.ERRO, Boolean.TRUE);
			return false;
		}
		return true;
	}

	public String getAnoLote() {
		return anoLote;
	}

	public void setAnoLote(String anoLote) {
		this.anoLote = anoLote;
	}

	public String getSemestre() {
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	public Integer getBimestre() {
		if (bimestre == null) {
			bimestre = 1;
		}
		return bimestre;
	}

	public void setBimestre(Integer bimestre) {
		this.bimestre = bimestre;
	}

	public List<SelectItem> getListaSelectItemSemestre() {
		if (listaSelectItemSemestre == null) {
			listaSelectItemSemestre = new ArrayList<SelectItem>(0);
			listaSelectItemSemestre.add(new SelectItem("0", ""));
			listaSelectItemSemestre.add(new SelectItem("1", "1º"));
			listaSelectItemSemestre.add(new SelectItem("2", "2º"));
		}
		return listaSelectItemSemestre;
	}

	public void setListaSelectItemSemestre(List<SelectItem> listaSelectItemSemestre) {
		this.listaSelectItemSemestre = listaSelectItemSemestre;
	}

	public List<SelectItem> getListaSelectItemBimestre() {
		if (listaSelectItemBimestre == null) {
			listaSelectItemBimestre = new ArrayList<SelectItem>(0);
//            listaSelectItemBimestre.add(new SelectItem(0, ""));
			listaSelectItemBimestre.add(new SelectItem(1, "1º"));
			listaSelectItemBimestre.add(new SelectItem(2, "2º"));
//            listaSelectItemBimestre.add(new SelectItem(3, "Ambos"));
		}
		return listaSelectItemBimestre;
	}

	public void setListaSelectItemBimestre(List<SelectItem> listaSelectItemBimestre) {
		this.listaSelectItemBimestre = listaSelectItemBimestre;
	}

	public List<DisciplinaVO> getListaConsultaDisciplina() {
		if (listaConsultaDisciplina == null) {
			listaConsultaDisciplina = new ArrayList<>(0);
		}
		return listaConsultaDisciplina;
	}

	public void setListaConsultaDisciplina(List<DisciplinaVO> listaConsultaDisciplina) {
		this.listaConsultaDisciplina = listaConsultaDisciplina;
	}

	private void montarSemestreAtual() {
		setAnoLote(Uteis.getAnoDataAtual());
		setSemestre(Uteis.getSemestreAtual());
		setBimestre(Integer.valueOf(Uteis.getBimestreDoSemestreAtual()));
	}

	public List<SelectItem> getTipoConsultaComboDisciplina() {
		if (tipoConsultaComboDisciplina == null) {
			tipoConsultaComboDisciplina = new ArrayList<>(0);
			tipoConsultaComboDisciplina.add(new SelectItem("abreviatura", "Abreviatura"));
			tipoConsultaComboDisciplina.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboDisciplina.add(new SelectItem("codigo", "Código"));
		}
		return tipoConsultaComboDisciplina;
	}

	public DisciplinaVO getDisciplinaVO() {
		if (disciplinaVO == null) {
			disciplinaVO = new DisciplinaVO();
		}
		return disciplinaVO;
	}

	public void setDisciplinaVO(DisciplinaVO disciplinaVO) {
		this.disciplinaVO = disciplinaVO;
	}

	public String getCampoConsultaDisciplina() {
		if (campoConsultaDisciplina == null) {
			campoConsultaDisciplina = "";
		}
		return campoConsultaDisciplina;
	}

	public void setCampoConsultaDisciplina(String campoConsultaDisciplina) {
		this.campoConsultaDisciplina = campoConsultaDisciplina;
	}

	public String getValorConsultaDisciplina() {
		if (valorConsultaDisciplina == null) {
			valorConsultaDisciplina = "";
		}
		return valorConsultaDisciplina;
	}

	public void setValorConsultaDisciplina(String valorConsultaDisciplina) {
		this.valorConsultaDisciplina = valorConsultaDisciplina;
	}

	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if (unidadeEnsinoVO == null) {
			unidadeEnsinoVO = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoVO;
	}

	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}

	public String getUnidadeEnsinoApresentar() {
		if (unidadeEnsinoApresentar == null) {
			unidadeEnsinoApresentar = "";
		}
		return unidadeEnsinoApresentar;
	}

	public void setUnidadeEnsinoApresentar(String unidadeEnsinoApresentar) {
		this.unidadeEnsinoApresentar = unidadeEnsinoApresentar;
	}

	public String getCursoApresentar() {
		if (cursoApresentar == null) {
			cursoApresentar = "";
		}
		return cursoApresentar;
	}

	public void setCursoApresentar(String cursoApresentar) {
		this.cursoApresentar = cursoApresentar;
	}

	public void consultarDisciplina() {
		try {
			if (getCampoConsultaDisciplina().equals("nome")) {
				setListaConsultaDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorNome(
						getValorConsultaDisciplina(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			} else if (getCampoConsultaDisciplina().equals("codigo")) {
				if (!Uteis.getIsValorNumerico(getValorConsultaDisciplina())) {
					throw new Exception("Informe apenas um valor numérico.");
				}
				setListaConsultaDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorCodigo(
						Integer.valueOf(getValorConsultaDisciplina()), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
						getUsuarioLogado()));
			} else if (getCampoConsultaDisciplina().equals("abreviatura")) {
				setListaConsultaDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorAbreviatura(
						getValorConsultaDisciplina(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaDisciplina(new ArrayList<>(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void selecionarDisciplina() throws Exception {
		try {
			DisciplinaVO obj = (DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplinaItens");
			setDisciplinaVO(obj);
			consultarIntegracaoLoteExistentes();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public String getCodigoUnidadesDeEnsinosSelecionadas() {
		return codigoUnidadesDeEnsinosSelecionadas;
	}

	public void setCodigoUnidadesDeEnsinosSelecionadas(String codigoUnidadesDeEnsinosSelecionadas) {
		this.codigoUnidadesDeEnsinosSelecionadas = codigoUnidadesDeEnsinosSelecionadas;
	}

	public String getCodigoCursosSelecionados() {
		return codigoCursosSelecionados;
	}

	public void setCodigoCursosSelecionados(String codigoCursosSelecionados) {
		this.codigoCursosSelecionados = codigoCursosSelecionados;
	}

	public ControleConsultaIntegracaoMestreGR getControleConsultaIntegracaoMestreGR() {
		if (controleConsultaIntegracaoMestreGR == null) {
			controleConsultaIntegracaoMestreGR = new ControleConsultaIntegracaoMestreGR();
		}
		return controleConsultaIntegracaoMestreGR;
	}

	public void setControleConsultaIntegracaoMestreGR(
			ControleConsultaIntegracaoMestreGR controleConsultaIntegracaoMestreGR) {
		this.controleConsultaIntegracaoMestreGR = controleConsultaIntegracaoMestreGR;
	}

	public OperacaoTempoRealMestreGREnum getTipoLote() {
		return tipoLote;
	}

	public void setTipoLote(OperacaoTempoRealMestreGREnum tipoLote) {
		this.tipoLote = tipoLote;
	}

	public IntegracaoMestreGRVO getIntegracaoMestreGRVORegistro() {
		if (integracaoMestreGRVORegistro == null) {
			integracaoMestreGRVORegistro = new IntegracaoMestreGRVO();
		}
		return integracaoMestreGRVORegistro;
	}

	public void setIntegracaoMestreGRVORegistro(IntegracaoMestreGRVO integracaoMestreGRVORegistro) {
		this.integracaoMestreGRVORegistro = integracaoMestreGRVORegistro;
	}

	public Map<String, Object> getFiltros() {
		return filtros;
	}

	public void setFiltros(Map<String, Object> filtros) {
		this.filtros = filtros;
	}

	public Date getDataInicioRequerimento() {
		return dataInicioRequerimento;
	}

	public void setDataInicioRequerimento(Date dataInicioRequerimento) {
		this.dataInicioRequerimento = dataInicioRequerimento;
	}

	public Date getDataFimRequerimento() {
		return dataFimRequerimento;
	}

	public void setDataFimRequerimento(Date dataFimRequerimento) {
		this.dataFimRequerimento = dataFimRequerimento;
	}

	public List<IntegracaoMestreGRVO> getCargaInicialIntegracaoMestreGRVO() {
		if (cargaInicialIntegracaoMestreGRVO == null) {
			cargaInicialIntegracaoMestreGRVO = new ArrayList<IntegracaoMestreGRVO>(0);
		}
		return cargaInicialIntegracaoMestreGRVO;
	}

	public void setCargaInicialIntegracaoMestreGRVO(List<IntegracaoMestreGRVO> cargaInicialIntegracaoMestreGRVO) {
		this.cargaInicialIntegracaoMestreGRVO = cargaInicialIntegracaoMestreGRVO;
	}

	public ProgressBarVO getProgressBarIntegracaoMestreGR() {
		return getAplicacaoControle().getProgressBarIntegracaoMestreGR();
	}

	public Map<String, Object> getFiltroLoteSelecionado() {
		if (filtroLoteSelecionado == null) {
			filtroLoteSelecionado = new HashMap<>();
		}
		return filtroLoteSelecionado;
	}

	public void setFiltroLoteSelecionado(Map<String, Object> filtroLoteSelecionado) {
		this.filtroLoteSelecionado = filtroLoteSelecionado;
	}

	private String dataFormatada(Date data, String tipoDataHora) {
		if (data == null) {
			return "";
		}
		SimpleDateFormat formatada = new SimpleDateFormat(tipoDataHora);
		return formatada.format(data);
	}

	public String getCaminhoUrl() {
		if (caminhoUrl == null) {
			caminhoUrl = "";
		}
		return caminhoUrl;
	}

	public void setCaminhoUrl(String caminhoUrl) {
		this.caminhoUrl = caminhoUrl;
	}

	public String getNomeArquivo() {
		if (nomeArquivo == null) {
			nomeArquivo = "";
		}
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

	public Boolean getDowloadArquivo() {
		if (isDowloadArquivo == null) {
			isDowloadArquivo = false;
		}
		return isDowloadArquivo;
	}

	public void setDowloadArquivo(Boolean dowloadArquivo) {
		isDowloadArquivo = dowloadArquivo;
	}

	/**
	 * Limpar Filtros
	 */
	public void limparCurso() {
		try {
			setCursoApresentar(null);
			setMarcarTodosCursos(false);
			marcarTodosCursosAction();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparDisciplina() {
		try {
			setDisciplinaVO(null);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparFiltros() {
		setFiltros(new HashMap<>());
	}

	private void limparTipoLote() {
		setTipoLote(null);
	}

	public void limparAno() {
		if (controleConsultaIntegracaoMestreGR != null) {
			controleConsultaIntegracaoMestreGR.setAno(null);
			consultarLoteItem();
		}
	}

	public void limparEnsino() {
		if (controleConsultaIntegracaoMestreGR != null) {
			controleConsultaIntegracaoMestreGR.setEnsino(null);
			consultarLoteItem();
		}
	}

	public void limparSemestre() {
		if (controleConsultaIntegracaoMestreGR != null) {
			controleConsultaIntegracaoMestreGR.setSemestre(null);
			consultarLoteItem();
		}
	}

	public void limparBimestre() {
		if (controleConsultaIntegracaoMestreGR != null) {
			controleConsultaIntegracaoMestreGR.setBimestre(null);
			consultarLoteItem();
		}
	}

	public void limparTurno() {
		if (controleConsultaIntegracaoMestreGR != null) {
			controleConsultaIntegracaoMestreGR.setTurno(null);
			consultarLoteItem();
		}
	}

	public void limparCodigoInternoTurma() {
		if (controleConsultaIntegracaoMestreGR != null) {
			controleConsultaIntegracaoMestreGR.setCodigoInternoTurma(null);
			consultarLoteItem();
		}
	}

	public void limparNomeTurma() {
		if (controleConsultaIntegracaoMestreGR != null) {
			controleConsultaIntegracaoMestreGR.setNomeTurma(null);
			consultarLoteItem();
		}
	}

	public void limparSiglaDisciplina() {
		if (controleConsultaIntegracaoMestreGR != null) {
			controleConsultaIntegracaoMestreGR.setSiglaDisciplina(null);
			consultarLoteItem();
		}
	}

	public void limparNomeDisciplina() {
		if (controleConsultaIntegracaoMestreGR != null) {
			controleConsultaIntegracaoMestreGR.setNomeDisciplina(null);
			consultarLoteItem();
		}
	}

	public void limparCodigoDisciplina() {
		if (controleConsultaIntegracaoMestreGR != null) {
			controleConsultaIntegracaoMestreGR.setCodigoDisciplina(null);
			consultarLoteItem();
		}
	}

	// ALUNOS
	public void limparCodigoInternoAluno() {
		if (controleConsultaIntegracaoMestreGR != null) {
			controleConsultaIntegracaoMestreGR.setCodigoInternoAluno(null);
			consultarLoteItem();
		}
	}

	public void limparCodigoAluno() {
		if (controleConsultaIntegracaoMestreGR != null) {
			controleConsultaIntegracaoMestreGR.setCodigoAluno(null);
			consultarLoteItem();
		}
	}

	public void limparNomeAluno() {
		if (controleConsultaIntegracaoMestreGR != null) {
			controleConsultaIntegracaoMestreGR.setNomeAluno(null);
			consultarLoteItem();
		}
	}

	public void limparEmailAluno() {
		if (controleConsultaIntegracaoMestreGR != null) {
			controleConsultaIntegracaoMestreGR.setEmailAluno(null);
			consultarLoteItem();
		}
	}

	public void limparCodigoDiaSemanaAluno() {
		if (controleConsultaIntegracaoMestreGR != null) {
			controleConsultaIntegracaoMestreGR.setCodigoDiaSemanaAluno(null);
			consultarLoteItem();
		}
	}

	public void limparCodigoInternoPolo() {
		if (controleConsultaIntegracaoMestreGR != null) {
			controleConsultaIntegracaoMestreGR.setCodigoInternoPolo(null);
			consultarLoteItem();
		}
	}

	public void limparCodigoPolo() {
		if (controleConsultaIntegracaoMestreGR != null) {
			controleConsultaIntegracaoMestreGR.setCodigoPolo(null);
			consultarLoteItem();
		}
	}

	public void limparDescricaoPolo() {
		if (controleConsultaIntegracaoMestreGR != null) {
			controleConsultaIntegracaoMestreGR.setDescricaoPolo(null);
			consultarLoteItem();
		}
	}

	public void limparCodigoInternoCurso() {
		if (controleConsultaIntegracaoMestreGR != null) {
			controleConsultaIntegracaoMestreGR.setCodigoInternoCurso(null);
			consultarLoteItem();
		}
	}

	public void limparCodigoCurso() {
		if (controleConsultaIntegracaoMestreGR != null) {
			controleConsultaIntegracaoMestreGR.setCodigoCurso(null);
			consultarLoteItem();
		}
	}

	public void limparDescricaoCurso() {
		if (controleConsultaIntegracaoMestreGR != null) {
			controleConsultaIntegracaoMestreGR.setDescricaoCurso(null);
			consultarLoteItem();
		}
	}

	public void limparTempoEstendidoAluno() {
		if (controleConsultaIntegracaoMestreGR != null) {
			controleConsultaIntegracaoMestreGR.setTempoEstendidoAluno(null);
			consultarLoteItem();
		}
	}

	public void limparNumeroCelular() {
		if (controleConsultaIntegracaoMestreGR != null) {
			controleConsultaIntegracaoMestreGR.setNumeroCelularAluno(null);
			consultarLoteItem();
		}
	}

	public void limparOrigemLogAluno() {
		if (controleConsultaIntegracaoMestreGR != null) {
			controleConsultaIntegracaoMestreGR.setOrigem(null);
			consultarLogsAlunosFiltro();
		}
	}

	public void limparMatriculaLogAluno() {
		if (controleConsultaIntegracaoMestreGR != null) {
			controleConsultaIntegracaoMestreGR.setMatricula(null);
			consultarLogsAlunosFiltro();
		}
	}

	public void limparCodigosDisciplinasLogAluno() {
		if (controleConsultaIntegracaoMestreGR != null) {
			controleConsultaIntegracaoMestreGR.setCodigosDisciplinas(null);
			consultarLogsAlunosFiltro();
		}
	}

	public void limparCodigoAlunoLogAluno() {
		if (controleConsultaIntegracaoMestreGR != null) {
			controleConsultaIntegracaoMestreGR.setCodigoAluno(null);
			consultarLogsAlunosFiltro();
		}
	}

	public void limparSituacaoLogAluno() {
		if (controleConsultaIntegracaoMestreGR != null) {
			controleConsultaIntegracaoMestreGR.setSituacao(null);
			consultarLogsAlunosFiltro();
		}
	}

	public void limparAnoLogAluno() {
		if (controleConsultaIntegracaoMestreGR != null) {
			controleConsultaIntegracaoMestreGR.setAno(null);
			consultarLogsAlunosFiltro();
		}
	}

	public void limparSemestreLogAluno() {
		if (controleConsultaIntegracaoMestreGR != null) {
			controleConsultaIntegracaoMestreGR.setSemestre(null);
			consultarLogsAlunosFiltro();
		}
	}

	public void limparBimestreLogAluno() {
		if (controleConsultaIntegracaoMestreGR != null) {
			controleConsultaIntegracaoMestreGR.setBimestre(null);
			consultarLogsAlunosFiltro();
		}
	}

	public void limparAlunoLogAluno() {
		if (controleConsultaIntegracaoMestreGR != null) {
			controleConsultaIntegracaoMestreGR.setNomeAluno(null);
			consultarLogsAlunosFiltro();
		}
	}

	public void limparEmailLogAluno() {
		if (controleConsultaIntegracaoMestreGR != null) {
			controleConsultaIntegracaoMestreGR.setEmailAluno(null);
			consultarLogsAlunosFiltro();
		}
	}

	public void limparErroLogAluno() {
		if (controleConsultaIntegracaoMestreGR != null) {
			controleConsultaIntegracaoMestreGR.setMensagemErro(null);
			consultarLogsAlunosFiltro();
		}
	}

	public void limparCodigoLogErro() {
		if (controleConsultaIntegracaoMestreGR != null) {
			controleConsultaIntegracaoMestreGR.setCodigoLote(null);
			consultarErroLoteInicial();
		}
	}

	public void limparLogOrigemErro() {
		if (controleConsultaIntegracaoMestreGR != null) {
			controleConsultaIntegracaoMestreGR.setOrigem(null);
			consultarErroLoteInicial();
		}
	}

	public void limparLogNomeLoteErro() {
		if (controleConsultaIntegracaoMestreGR != null) {
			controleConsultaIntegracaoMestreGR.setNomeLote(null);
			consultarErroLoteInicial();
		}
	}

	public void limparAbreviaturaDisciplinas() {
		if (controleConsultaIntegracaoMestreGR != null) {
			controleConsultaIntegracaoMestreGR.setAbreviaturaDisciplinas(null);
			consultarLogsAlunosFiltro();
		}
	}

	public void limparChaveTurmaDisciplinas() {
		if (controleConsultaIntegracaoMestreGR != null) {
			controleConsultaIntegracaoMestreGR.setChaveTurma(null);
			consultarLogsAlunosFiltro();
		}
	}

	private void limparIntegracaoMestreGRVORegistro() {
		setIntegracaoMestreGRVORegistro(null);
	}

	public void limparCargaInicialIntegracaoMestreGRVO() {
		setCargaInicialIntegracaoMestreGRVO(null);
	}

	public void limparLogMatriculaErro() {
		if (controleConsultaIntegracaoMestreGR != null) {
			controleConsultaIntegracaoMestreGR.setMatricula(null);
			consultarErroLoteInicial();
		}
	}

	public void limparLogNomeDisciplinaErro() {
		if (controleConsultaIntegracaoMestreGR != null) {
			controleConsultaIntegracaoMestreGR.setNomeDisciplina(null);
			consultarErroLoteInicial();
		}
	}

	/**
	 * Responsavel por limpar todos os campos de filtros de itens json do lote
	 */
	private void limparFiltrosItens() {
		getControleConsultaIntegracaoMestreGR().setAno(null);
		getControleConsultaIntegracaoMestreGR().setSemestre(null);
		getControleConsultaIntegracaoMestreGR().setTurno(null);
		getControleConsultaIntegracaoMestreGR().setCodigoInternoTurma(null);
		getControleConsultaIntegracaoMestreGR().setNomeTurma(null);
		getControleConsultaIntegracaoMestreGR().setSiglaDisciplina(null);
		getControleConsultaIntegracaoMestreGR().setNomeDisciplina(null);
		getControleConsultaIntegracaoMestreGR().setCodigoDisciplina(null);
		getControleConsultaIntegracaoMestreGR().setCodigoInternoAluno(null);
		getControleConsultaIntegracaoMestreGR().setCodigoAluno(null);
		getControleConsultaIntegracaoMestreGR().setNomeAluno(null);
		getControleConsultaIntegracaoMestreGR().setEmailAluno(null);
		getControleConsultaIntegracaoMestreGR().setCodigoDiaSemanaAluno(null);
		getControleConsultaIntegracaoMestreGR().setCodigoInternoPolo(null);
		getControleConsultaIntegracaoMestreGR().setCodigoPolo(null);
		getControleConsultaIntegracaoMestreGR().setDescricaoPolo(null);
		getControleConsultaIntegracaoMestreGR().setCodigoInternoCurso(null);
		getControleConsultaIntegracaoMestreGR().setCodigoCurso(null);
		getControleConsultaIntegracaoMestreGR().setDescricaoCurso(null);
		getControleConsultaIntegracaoMestreGR().setTempoEstendidoAluno(null);
		getControleConsultaIntegracaoMestreGR().setNumeroCelularAluno(null);
	}

	private void limparFiltrosLogOperacoes() {
		getControleConsultaIntegracaoMestreGR().setOrigem(null);
		getControleConsultaIntegracaoMestreGR().setMatricula(null);
		getControleConsultaIntegracaoMestreGR().setNomeAluno(null);
		getControleConsultaIntegracaoMestreGR().setAno(null);
		getControleConsultaIntegracaoMestreGR().setSemestre(null);
		getControleConsultaIntegracaoMestreGR().setBimestre(null);
		getControleConsultaIntegracaoMestreGR().setCodigosDisciplinas(null);
		getControleConsultaIntegracaoMestreGR().setAbreviaturaDisciplinas(null);
	}

	private void limparFiltrosLogErro() {
		limparFiltros();
		setControleConsultaErroRegistroIntegracaoLote(null);
	}

	public List<CursoVO> getCursosFiltradosGraduacao() {
		return getCursoVOs().stream().filter(
				cursoVO -> cursoVO.getNivelEducacionalGraduacao() || cursoVO.getNivelEducacionalGraduacaoTecnologica())
				.collect(Collectors.toList());
	}

	/**
	 * Metodo responsavel por realizar a interrupção da progressbar e integração, de
	 * forma controlada para atualizar o status do lote atual.
	 *
	 * @throws ConsistirException
	 */
	public void realizarInterrupcaoProcessamento() throws ConsistirException {
		try {
			if (!getDowloadArquivo()) {
				setFazerDownload(false);
				getFacadeFactory().getProcessamentoFilaThreadsStrategy().encerrarFila();
				getProgressBarIntegracaoMestreGR().setStatus("Interrompendo a Integração do Lote...");
				if (getProgressBarIntegracaoMestreGR().getParams().containsKey("codigointegracao")
						&& getProgressBarIntegracaoMestreGR().getParams().get("codigointegracao") != null
						&& !getProgressBarIntegracaoMestreGR().getParams().get("codigointegracao").toString().trim()
								.isEmpty()
						&& !getProgressBarIntegracaoMestreGR().getParams().get("codigointegracao").toString().trim()
								.equals("0")) {

					IntegracaoMestreGRVO integracaoAtual = new IntegracaoMestreGRVO();
					integracaoAtual.setCodigo(
							(Integer) getProgressBarIntegracaoMestreGR().getParams().get("codigointegracao"));
					integracaoAtual
							.setDadosRetorno("{\"status\":\"error\",\"data\":\"Interrompida a Integração do Lote\"}");
					integracaoAtual
							.setMensagemErro("{\"status\":\"error\",\"data\":\"Interrompida a Integração do Lote\"}");
					integracaoAtual.setSituacao("ERRO");
					integracaoAtual.setDataTransmissao(new Date());
					setMensagemID("msg_IntegracaoMestreGR_sucesso_interromper_gerar_lote", Uteis.ERRO, Boolean.TRUE);
					getFacadeFactory().getProcessamentoFilaThreadsStrategy()
							.aguardarProcessamentoFila(getProgressBarIntegracaoMestreGR(), "Validando...");

					alterarRegistroIntegracao(integracaoAtual);
					if (getProgressBarIntegracaoMestreGR().getParams().containsKey("codigoiditem")
							&& getProgressBarIntegracaoMestreGR().getParams().get("codigoiditem") != null
							&& !getProgressBarIntegracaoMestreGR().getParams().get("codigoiditem").toString().trim()
									.isEmpty()
							&& !getProgressBarIntegracaoMestreGR().getParams().get("codigoiditem").toString().trim()
									.equals("0")) {
						integracaoAtual.setIdItem(
								(Integer) getProgressBarIntegracaoMestreGR().getParams().get("codigoiditem"));
						alterarIntegracaoItem(integracaoAtual);
					}
					if (!getProgressBarIntegracaoMestreGR().getParams().containsKey("codigoiditem")
							&& getProgressBarIntegracaoMestreGR().getParams().get("codigoiditem") == null
							&& getProgressBarIntegracaoMestreGR().getParams().get("codigoiditem").toString().trim()
									.isEmpty()
							&& getProgressBarIntegracaoMestreGR().getParams().get("codigoiditem").toString().trim()
									.equals("0")) {
						integracaoAtual
								.setDadosEnvio("{\"status\":\"error\",\"data\":\"Interrompida a Integração do Lote\"}");
						persistirRegistroIntegracaoItem(integracaoAtual);
					}
				} else {
					setMensagemID("msg_IntegracaoMestreGR_interrompida_validacao", Uteis.ERRO, Boolean.TRUE);
				}
				getProgressBarIntegracaoMestreGR().encerrarForcado();
				consultarIntegracaoLoteExistentesInterromperProgressBar();
			}
			if (getDowloadArquivo()) {
				setDowloadArquivo(false);
				setMensagemID("msg_IntegracaoMestreGR_interrompida_criacao_arquivo", Uteis.ERRO, Boolean.TRUE);
				getProgressBarIntegracaoMestreGR().setStatus("Interrompendo a Geração do Arquivo...");
				getProgressBarIntegracaoMestreGR().encerrarForcado();
			}
		} catch (Exception e) {
		}
	}

	public void realizarInicioProgressBarGerarExcelLote() {
		try {
			limparCargaInicialIntegracaoMestreGRVO();
			limparIntegracaoMestreGRVORegistro();

			setOncompleteModal("");
			setCaminhoUrl(null);
			setDowloadArquivo(true);

			HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
			setCaminhoUrl(request.getRequestURL().toString().replace(request.getRequestURI().toString(), "")
					+ request.getContextPath() + "/");

			getControleConsultaIntegracaoLote().setLimitePorPagina(QTD_ITENS_POR_VEZ_EXCEL);
			getControleConsultaIntegracaoLote().setPaginaAtual(1);
			getControleConsultaIntegracaoLote().setPage(1);

			getAplicacaoControle().setProgressBarIntegracaoMestreGR(new ProgressBarVO());
			getProgressBarIntegracaoMestreGR().resetar();
			getProgressBarIntegracaoMestreGR().setAplicacaoControle(getAplicacaoControle());
			getProgressBarIntegracaoMestreGR().setUsuarioVO(getUsuarioLogadoClone());

			List<IntegracaoMestreGRVO> cargaInicial = getFacadeFactory().getIntegracaoMestreGRInterfaceFacade()
					.consultarIntegracaoDadosEnvioJson(mapeamentoFiltrosLote(), getControleConsultaIntegracaoLote(),
							false, getProgressBarIntegracaoMestreGR().getUsuarioVO());
			if (!cargaInicial.isEmpty()) {
				setCargaInicialIntegracaoMestreGRVO(cargaInicial);
				getProgressBarIntegracaoMestreGR().iniciar(0L, getControleConsultaIntegracaoLote().getTotalPaginas(),
						"Iniciando Processamento da(s) operações.", true, this, "gerarExcelLote");
			}
			if (cargaInicial.isEmpty()) {
				setMensagemID("msg_IntegracaoMestreGR_nenhum_registro", Uteis.ERRO, Boolean.TRUE);
				setDowloadArquivo(false);
			}
		} catch (Exception e) {
			setDowloadArquivo(false);
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			getProgressBarIntegracaoMestreGR().encerrarForcado();
		}
	}

	public void realizarInicioProgressBarGerarExcelLog() {
		try {
			limparFiltrosItens();
			limparCargaInicialIntegracaoMestreGRVO();
			limparIntegracaoMestreGRVORegistro();
			limparFiltros();
			setOncompleteModal("");
			setCaminhoUrl(null);
			setDowloadArquivo(true);

			HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
			setCaminhoUrl(request.getRequestURL().toString().replace(request.getRequestURI().toString(), "")
					+ request.getContextPath() + "/");

			getControleConsultaIntegracaoLote().setLimitePorPagina(QTD_ITENS_POR_VEZ_EXCEL);
			getControleConsultaIntegracaoLote().setPaginaAtual(1);
			getControleConsultaIntegracaoLote().setPage(1);

			getAplicacaoControle().setProgressBarIntegracaoMestreGR(new ProgressBarVO());
			getProgressBarIntegracaoMestreGR().resetar();
			getProgressBarIntegracaoMestreGR().setAplicacaoControle(getAplicacaoControle());
			getProgressBarIntegracaoMestreGR().setUsuarioVO(getUsuarioLogadoClone());

			List<IntegracaoMestreGRVO> cargaInicial = getFacadeFactory().getIntegracaoMestreGRInterfaceFacade()
					.consultarLogIntegracaoAluno(mapeamentoFiltrosLogExcel(), getControleConsultaIntegracaoLote(),
							false, getProgressBarIntegracaoMestreGR().getUsuarioVO());
			if (!cargaInicial.isEmpty()) {
				setCargaInicialIntegracaoMestreGRVO(cargaInicial);
				getProgressBarIntegracaoMestreGR().iniciar(0L, getControleConsultaIntegracaoLote().getTotalPaginas(),
						"Iniciando Processamento da(s) operações.", true, this, "gerarExcelLog");
			}
			if (cargaInicial.isEmpty()) {
				setDowloadArquivo(false);
				setMensagemID("msg_IntegracaoMestreGR_nenhum_registro", Uteis.ERRO, Boolean.TRUE);
			}
		} catch (Exception e) {
			setDowloadArquivo(false);
			getProgressBarIntegracaoMestreGR().encerrarForcado();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void realizarInicioProgressBarGerarExcelLogErro() {
		try {
			limparFiltrosItens();
			limparCargaInicialIntegracaoMestreGRVO();
			limparIntegracaoMestreGRVORegistro();
			limparFiltros();
			setOncompleteModal("");
			setCaminhoUrl(null);
			setDowloadArquivo(true);

			HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
			setCaminhoUrl(request.getRequestURL().toString().replace(request.getRequestURI().toString(), "")
					+ request.getContextPath() + "/");

			getControleConsultaIntegracaoLote().setLimitePorPagina(QTD_ITENS_POR_VEZ_EXCEL);
			getControleConsultaIntegracaoLote().setPaginaAtual(1);
			getControleConsultaIntegracaoLote().setPage(1);

//            superRelVO.getTipoRelatorioEnum().equals(TipoRelatorioEnum.EXCEL);
			getAplicacaoControle().setProgressBarIntegracaoMestreGR(new ProgressBarVO());
			getProgressBarIntegracaoMestreGR().resetar();
			getProgressBarIntegracaoMestreGR().setAplicacaoControle(getAplicacaoControle());
			getProgressBarIntegracaoMestreGR().setUsuarioVO(getUsuarioLogadoClone());
			getProgressBarIntegracaoMestreGR().getParams().put("PermiteGerarFormatoExportacaoDados", true);

			getProgressBarIntegracaoMestreGR().setCaminhoWebRelatorio(UteisJSF.getCaminhoWeb());
			getProgressBarIntegracaoMestreGR().setConfiguracaoGeralSistemaVO(getConfiguracaoGeralSistemaVO());

			List<IntegracaoMestreGRVO> cargaInicial = getFacadeFactory().getIntegracaoMestreGRInterfaceFacade()
					.consultarErroIntegracaoJson(mapeamentoFiltrosLogExcel(), getControleConsultaIntegracaoLote(),
							false, getProgressBarIntegracaoMestreGR().getUsuarioVO());
			if (!cargaInicial.isEmpty()) {
				setCargaInicialIntegracaoMestreGRVO(cargaInicial);
				getProgressBarIntegracaoMestreGR().iniciar(0L, getControleConsultaIntegracaoLote().getTotalPaginas(),
						"Iniciando Processamento da(s) operações.", true, this, "gerarExcelLogErro");
			}
			if (cargaInicial.isEmpty()) {
				setDowloadArquivo(false);
				setMensagemID("msg_IntegracaoMestreGR_nenhum_registro", Uteis.ERRO, Boolean.TRUE);
			}
		} catch (Exception e) {
			setDowloadArquivo(false);
			getProgressBarIntegracaoMestreGR().encerrarForcado();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void gerarExcelLote() {
		try {
			File arquivo = getFacadeFactory().getIntegracaoMestreGRInterfaceFacade().realizarGeracaoExcelLote(
					mapeamentoFiltrosLote(), getCargaInicialIntegracaoMestreGRVO(), getControleConsultaIntegracaoLote(),
					getProgressBarIntegracaoMestreGR(), getUsuarioLogadoClone());
			setCaminhoRelatorio(arquivo.getName());
			setFazerDownload(true);
			getProgressBarIntegracaoMestreGR().setCaminhoWebRelatorio(arquivo.getAbsolutePath());
			setNomeArquivo(arquivo.getName());
			setMensagemID(MSG_TELA.msg_relatorio_ok.name());
			getProgressBarIntegracaoMestreGR().setForcarEncerramento(true);
		} catch (Exception e) {
			getProgressBarIntegracaoMestreGR().encerrarForcado();
			setFazerDownload(false);
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		} finally {
			getProgressBarIntegracaoMestreGR().encerrar();
		}
	}

	public void gerarExcelLog() {
		try {
			File arquivo = getFacadeFactory().getIntegracaoMestreGRInterfaceFacade().realizarGeracaoExcelLog(
					mapeamentoFiltrosLogExcel(), getCargaInicialIntegracaoMestreGRVO(),
					getControleConsultaIntegracaoLote(), getProgressBarIntegracaoMestreGR(), getUsuarioLogadoClone());
			setCaminhoRelatorio(arquivo.getName());
			setFazerDownload(true);
			getProgressBarIntegracaoMestreGR().setCaminhoWebRelatorio(arquivo.getAbsolutePath());
			setNomeArquivo(arquivo.getName());
			setMensagemID(MSG_TELA.msg_relatorio_ok.name());
			getProgressBarIntegracaoMestreGR().setForcarEncerramento(true);
		} catch (Exception e) {
			getProgressBarIntegracaoMestreGR().encerrarForcado();
			setFazerDownload(false);
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		} finally {
			getProgressBarIntegracaoMestreGR().encerrar();
		}
	}

	public void gerarExcelLogErro() {
		try {
			File arquivo = getFacadeFactory().getIntegracaoMestreGRInterfaceFacade().realizarGeracaoExcelLogErro(
					mapeamentoFiltrosLogExcel(), getCargaInicialIntegracaoMestreGRVO(),
					getControleConsultaIntegracaoLote(), getProgressBarIntegracaoMestreGR(), getUsuarioLogadoClone());
			setCaminhoRelatorio(arquivo.getName());
			setFazerDownload(true);
			getProgressBarIntegracaoMestreGR().setCaminhoWebRelatorio(arquivo.getAbsolutePath());
			setNomeArquivo(arquivo.getName());
			setMensagemID(MSG_TELA.msg_relatorio_ok.name());
			getProgressBarIntegracaoMestreGR().setForcarEncerramento(true);
		} catch (Exception e) {
			getProgressBarIntegracaoMestreGR().encerrarForcado();
			setFazerDownload(false);
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		} finally {
			getProgressBarIntegracaoMestreGR().encerrar();
		}
	}

	public List<SelectItem> getListaSelectItemOrigem() {
		if (listaSelectItemOrigem == null) {
			listaSelectItemOrigem = new ArrayList<SelectItem>(0);
			listaSelectItemOrigem.add(new SelectItem("", ""));
			for (OperacaoTempoRealMestreGREnum oper : OperacaoTempoRealMestreGREnum.values()) {
				listaSelectItemOrigem.add(new SelectItem(oper.getValor(), oper.getValorApresentar()));
			}
		}
		return listaSelectItemOrigem;
	}

	public void setListaSelectItemOrigem(List<SelectItem> listaSelectItemOrigem) {
		this.listaSelectItemOrigem = listaSelectItemOrigem;
	}

	public List<SelectItem> getListaSelectItemNivelEducacional() {
		if(listaSelectItemNivelEducacional == null) {
			listaSelectItemNivelEducacional = new ArrayList<SelectItem>();
			listaSelectItemNivelEducacional.add(new SelectItem(TipoNivelEducacional.SUPERIOR, TipoNivelEducacional.SUPERIOR.getDescricao()));
			listaSelectItemNivelEducacional.add(new SelectItem(TipoNivelEducacional.GRADUACAO_TECNOLOGICA, TipoNivelEducacional.GRADUACAO_TECNOLOGICA.getDescricao()));
			listaSelectItemNivelEducacional.add(new SelectItem(TipoNivelEducacional.EXTENSAO, TipoNivelEducacional.EXTENSAO.getDescricao()));
			listaSelectItemNivelEducacional.add(new SelectItem(TipoNivelEducacional.SEQUENCIAL, TipoNivelEducacional.SEQUENCIAL.getDescricao()));
			listaSelectItemNivelEducacional.add(new SelectItem(TipoNivelEducacional.POS_GRADUACAO, TipoNivelEducacional.POS_GRADUACAO.getDescricao()));
		}
		return listaSelectItemNivelEducacional;
	}

	public void setListaSelectItemNivelEducacional(List<SelectItem> listaSelectItemNivelEducacional) {
		this.listaSelectItemNivelEducacional = listaSelectItemNivelEducacional;
	}

	public TipoNivelEducacional getNivelEducacional() {
		if(nivelEducacional == null) {
			nivelEducacional =  TipoNivelEducacional.SUPERIOR;
		}
		return nivelEducacional;
	}

	public void setNivelEducacional(TipoNivelEducacional nivelEducacional) {
		this.nivelEducacional = nivelEducacional;
	}
	
	

}