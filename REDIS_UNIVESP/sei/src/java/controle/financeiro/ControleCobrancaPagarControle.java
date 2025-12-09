package controle.financeiro;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.richfaces.event.FileUploadEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import jobs.JobBaixarContasPagarArquivoRetorno;
import jobs.JobProcessarArquivoRetornoPagar;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.financeiro.BancoVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.ContaPagarControleRemessaContaPagarVO;
import negocio.comuns.financeiro.ContaPagarHistoricoVO;
import negocio.comuns.financeiro.ContaPagarRegistroArquivoVO;
import negocio.comuns.financeiro.ContaPagarVO;
import negocio.comuns.financeiro.ControleCobrancaPagarVO;
import negocio.comuns.financeiro.MapaPendenciasControleCobrancaPagarVO;
import negocio.comuns.financeiro.enumerador.BancoEnum;
import negocio.comuns.financeiro.enumerador.SituacaoProcessamentoArquivoRetornoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.dominios.SituacaoArquivoRetorno;

@Controller("ControleCobrancaPagarControle")
@Scope("viewScope")
@Lazy
public class ControleCobrancaPagarControle extends SuperControle implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7359521226845847237L;
	private ControleCobrancaPagarVO controleCobrancaPagarVO;
	private List<MapaPendenciasControleCobrancaPagarVO> mapaPendenciasControleCobrancaPagarVOs;
	private JobBaixarContasPagarArquivoRetorno jobBaixarContasPagarArquivoRetorno;
	protected List<SelectItem> listaSelectItemUnidadeEnsino;
	protected List<SelectItem> listaSelectItemBanco;
	protected List<SelectItem> listaSelectItemContaCorrente;
	private boolean ativarPush = false;
	private boolean arquivoProcessado = false;
	private Boolean marcarTodos;
	private Double totalValorDiferencaPendencia;
	private Double valorTotalRecebido;
	private Double valorTotalRecebidoDuplicidade;	
	private Double valorTotalRecebidoNaoLoc;
	private Integer qtdRegistros;
	private Integer qtdRegistrosDuplicidade;	
	private Integer qtdRegistrosNaoLoc;
	private boolean abrirModalPendenciaArquivoRetorno = false;
	private boolean apresentarModalMensagemContaCorrente =false;
	private List<ContaPagarRegistroArquivoVO>  contasAgrupadasRegistroArquivos;
	private Integer qtdRegistrosAgrupado ; 
	private Double  vlrTotalRegistrosAgrupado ;
	private Integer qtdTotalRegistrosAgrupados;
	private Double vlrTotalRegistrosAgrupadoModal;

	public ControleCobrancaPagarControle() throws Exception {
		setControleConsulta(new ControleConsulta());
		getControleConsulta().setCampoConsulta("dataProcessamento");
		setMensagemID("msg_entre_prmconsulta");
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe <code>PlanoConta</code> para edição pelo usuário da aplicação.
	 */
	public String novo() {
		try {
			setControleCobrancaPagarVO(new ControleCobrancaPagarVO());
			getControleCobrancaPagarVO().setResponsavel(getUsuarioLogadoClone());
			setArquivoProcessado(false);
			setApresentarModalMensagemContaCorrente(false);
			inicializarListasSelectItemTodosComboBox();
			setMensagemID("msg_entre_dados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("controleCobrancaPagarForm.xhtml");
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe <code>PlanoConta</code> para alteração. O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa disponibilizá-lo para edição.
	 */
	public String editar() {
		try {
			ControleCobrancaPagarVO obj = (ControleCobrancaPagarVO) context().getExternalContext().getRequestMap().get("controleCobrancaItens");
			setControleCobrancaPagarVO(getFacadeFactory().getControleCobrancaPagarFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_PROCESSAMENTO, getUsuarioLogado()));
			if (getControleCobrancaPagarVO().getSituacaoProcessamento().isProcessamentoConcluido()) {
				Uteis.ARQUIVOS_CONTROLE_COBRANCA.put(Uteis.getNomeArquivoComUnidadeEnsino(getControleCobrancaPagarVO().getNomeArquivo_Apresentar(), getControleCobrancaPagarVO().getUnidadeEnsinoVO().getCodigo()), SituacaoArquivoRetorno.CONTAS_BAIXADAS.getValor());
			} else {
				Uteis.ARQUIVOS_CONTROLE_COBRANCA.put(Uteis.getNomeArquivoComUnidadeEnsino(getControleCobrancaPagarVO().getNomeArquivo_Apresentar(), getControleCobrancaPagarVO().getUnidadeEnsinoVO().getCodigo()), SituacaoArquivoRetorno.ARQUIVO_PROCESSADO.getValor());
			}
			setArquivoProcessado(true);
			setApresentarModalMensagemContaCorrente(false);
			setAtivarPush(getControleCobrancaPagarVO().getSituacaoProcessamento().isEmProcessamento());
			inicializarListasSelectItemTodosComboBox();
			montarListaSelectItemContaCorrente();
			contabilizarTotais();
			setMensagemID("msg_dados_editar");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("controleCobrancaPagarForm.xhtml");
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>PlanoConta</code>. Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é acionado o <code>alterar()</code>. Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado para o usuário juntamente com uma mensagem de erro.
	 */
	public void persistir() {
		try {
			// getFacadeFactory().getConfiguracaoContabilFacade().persistir(getConfiguracaoContabilVO(), true, getUsuarioLogado());
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP PlanoContaCons.jsp. Define o tipo de consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
	 */
	@Override
	public String consultar() {
		try {
			super.consultar();
			List<ControleCobrancaPagarVO> objs = new ArrayList<ControleCobrancaPagarVO>();
			if (getControleConsulta().getCampoConsulta().equals("codigo")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getControleCobrancaPagarFacade().consultaRapidaPorCodigo(new Integer(valorInt), getUnidadeEnsinoLogado().getCodigo(), true, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("dataProcessamento")) {
				objs = getFacadeFactory().getControleCobrancaPagarFacade().consultaRapidaPorDataProcessamento(getControleConsulta().getDataIni(), getControleConsulta().getDataFim(), getUnidadeEnsinoLogado().getCodigo(), true, getUsuarioLogado());
			}
			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			getListaConsulta().clear();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return "";
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
	 */
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setListaConsulta(new ArrayList(0));
		getControleConsulta().setDataIni(Uteis.getDataPrimeiroDiaMes(new Date()));
		getControleConsulta().setDataFim(Uteis.getDataUltimoDiaMes(new Date()));
		getControleConsulta().setCampoConsulta("dataProcessamento");
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("controleCobrancaPagarCons.xhtml");
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe <code>PlanoContaVO</code> Após a exclusão ela automaticamente aciona a rotina para uma nova inclusão.
	 */
	public String excluir() {
		try {
			getFacadeFactory().getControleCobrancaPagarFacade().excluir(getControleCobrancaPagarVO(), getUsuarioLogado());
			novo();
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("configuracaoContabilForm.xhtml");
	}

	public void upLoadArquivo(FileUploadEvent upload) {
		try {
			getFacadeFactory().getArquivoHelper().upLoad(upload, getControleCobrancaPagarVO().getArquivoRetornoContaPagar(), getConfiguracaoGeralPadraoSistema(), PastaBaseArquivoEnum.REMESSA_PG_TMP, getUsuarioLogado());
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada(e.getMessage());
		} finally {
			upload = null;
		}
	}

	public String processarArquivo() {
		try {
			if (!Uteis.isAtributoPreenchido(getControleCobrancaPagarVO().getBancoVO())) {
				throw new Exception("Para processar o arquivo deve-se informar o banco");
			}
			if (getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getUnidadeEnsinoLogado().getCodigo()).getObrigatorioSelecionarUnidadeEnsinoControleCobranca() && getControleCobrancaPagarVO().getUnidadeEnsinoVO().getCodigo().equals(0)) {
				throw new Exception("Para processar o arquivo deve-se informar a unidade de ensino");
			}
			if (!Uteis.isAtributoPreenchido(getControleCobrancaPagarVO())) {
				ExecutorService executor = Executors.newSingleThreadExecutor();
				Uteis.ARQUIVOS_CONTROLE_COBRANCA.put(Uteis.getNomeArquivoComUnidadeEnsino(getControleCobrancaPagarVO().getNomeArquivo_Apresentar(), getControleCobrancaPagarVO().getUnidadeEnsinoVO().getCodigo()), SituacaoArquivoRetorno.PROCESSANDO_ARQUIVO.getValor());
				setAtivarPush(true);
				setControleCobrancaPagarVO((ControleCobrancaPagarVO) executor.submit(new JobProcessarArquivoRetornoPagar(getControleCobrancaPagarVO(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getControleCobrancaPagarVO().getUnidadeEnsinoVO().getCodigo()), getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(getControleCobrancaPagarVO().getUnidadeEnsinoVO().getCodigo()), getUsuarioLogado())).get());
				executor.shutdown();
				executor.awaitTermination(5, TimeUnit.MINUTES);
				throw new ConsistirException(SituacaoArquivoRetorno.getDescricao(Uteis.ARQUIVOS_CONTROLE_COBRANCA.get(Uteis.getNomeArquivoComUnidadeEnsino(getControleCobrancaPagarVO().getNomeArquivo_Apresentar(), getControleCobrancaPagarVO().getUnidadeEnsinoVO().getCodigo()))));
			} else {
				throw new ConsistirException(SituacaoArquivoRetorno.ARQUIVO_PROCESSADO.getDescricao());
			}
			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage().replaceAll("(.*Exception:)", ""), true);
			if (!e.getMessage().equals("Arquivo processado e contas não baixadas.")) {
				setArquivoProcessado(false);
				setAtivarPush(false);
			}
		}
		return "";
	}

	public void gravarBaixandoContas() {
		try {
			setApresentarModalMensagemContaCorrente(false);
			getFacadeFactory().getControleCobrancaPagarFacade().realizarAtualizacaoDadosProcessamento(getControleCobrancaPagarVO());
			if (getControleCobrancaPagarVO().getSituacaoProcessamento().isEmProcessamento() || (!Uteis.ARQUIVOS_CONTROLE_COBRANCA.isEmpty() && Uteis.ARQUIVOS_CONTROLE_COBRANCA.get(Uteis.getNomeArquivoComUnidadeEnsino(getControleCobrancaPagarVO().getNomeArquivo_Apresentar(), getControleCobrancaPagarVO().getUnidadeEnsinoVO().getCodigo())).equals(SituacaoArquivoRetorno.BAIXANDO_CONTAS.getValor()))) {
				setAtivarPush(true);
				throw new ConsistirException("Arquivo já está sendo processado.");
			} else if ((!Uteis.ARQUIVOS_CONTROLE_COBRANCA.isEmpty() && !Uteis.ARQUIVOS_CONTROLE_COBRANCA.get(Uteis.getNomeArquivoComUnidadeEnsino(getControleCobrancaPagarVO().getNomeArquivo_Apresentar(), getControleCobrancaPagarVO().getUnidadeEnsinoVO().getCodigo())).equals(SituacaoArquivoRetorno.CONTAS_BAIXADAS.getValor()))) {
				getControleCobrancaPagarVO().setSituacaoProcessamento(SituacaoProcessamentoArquivoRetornoEnum.EM_PROCESSAMENTO);
				setJobBaixarContasPagarArquivoRetorno(new JobBaixarContasPagarArquivoRetorno(getControleCobrancaPagarVO(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getControleCobrancaPagarVO().getUnidadeEnsinoVO().getCodigo()), getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(getControleCobrancaPagarVO().getUnidadeEnsinoVO().getCodigo()), getUsuarioLogado()));
				Thread jobBaixarContasArquivoRetornos = new Thread(getJobBaixarContasPagarArquivoRetorno());
				jobBaixarContasArquivoRetornos.start();
				Uteis.ARQUIVOS_CONTROLE_COBRANCA.put(Uteis.getNomeArquivoComUnidadeEnsino(getControleCobrancaPagarVO().getNomeArquivo_Apresentar(), getControleCobrancaPagarVO().getUnidadeEnsinoVO().getCodigo()), SituacaoArquivoRetorno.BAIXANDO_CONTAS.getValor());
				setAtivarPush(true);
				throw new ConsistirException(SituacaoArquivoRetorno.BAIXANDO_CONTAS.getDescricao());
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public String getExecutarAtualizacaoProcessamentoCons() {
		try {
			ControleCobrancaPagarVO obj = (ControleCobrancaPagarVO) getRequestMap().get("controleCobrancaItens");
			if (obj.getSituacaoProcessamento().equals(SituacaoProcessamentoArquivoRetornoEnum.EM_PROCESSAMENTO)) {
				getFacadeFactory().getControleCobrancaPagarFacade().realizarAtualizacaoDadosProcessamento(obj);
			}
			return obj.getProgressBar();
		} catch (Exception e) {
			return "";
		}
	}

	public String getExecutarAtualizacaoProcessamento() {
		return getControleCobrancaPagarVO().getProgressBar();
	}

	public void realizarAtualizacaoProcessamento() {
		try {
			if (isAtivarPush()) {
				getFacadeFactory().getControleCobrancaPagarFacade().realizarAtualizacaoDadosProcessamento(getControleCobrancaPagarVO());
				if (getControleCobrancaPagarVO().getSituacaoProcessamento().isAguardandoProcessamento() || getControleCobrancaPagarVO().getSituacaoProcessamento().isErroProcessamento() || getControleCobrancaPagarVO().getSituacaoProcessamento().isInterrompidoProcessamento()) {
					executarConsultaAutomatica();
					if (getJobBaixarContasPagarArquivoRetorno() != null) {
						setMensagemDetalhada("", getJobBaixarContasPagarArquivoRetorno().getSituacao());
					}
					if (getControleCobrancaPagarVO().getSituacaoProcessamento().isErroProcessamento() || getControleCobrancaPagarVO().getSituacaoProcessamento().isInterrompidoProcessamento()) {
						setMensagemDetalhada("msg_erro", getControleCobrancaPagarVO().getMotivoErroProcessamento(), true);
						setAtivarPush(false);
					}
				}
				if (getControleCobrancaPagarVO().getSituacaoProcessamento().isProcessamentoConcluido() || getControleCobrancaPagarVO().getSituacaoProcessamento().isArquivoProcessado()) {
					setAtivarPush(false);
					executarConsultaAutomatica();
				}
			}
		} catch (Exception e) {
			if (Uteis.isAtributoPreenchido(getControleCobrancaPagarVO().getMotivoErroProcessamento()) && (getControleCobrancaPagarVO().getSituacaoProcessamento().isErroProcessamento() || getControleCobrancaPagarVO().getSituacaoProcessamento().isInterrompidoProcessamento())) {
				setMensagemDetalhada("msg_erro", getControleCobrancaPagarVO().getMotivoErroProcessamento(), true);
			} else if (getJobBaixarContasPagarArquivoRetorno() != null && Uteis.isAtributoPreenchido(getJobBaixarContasPagarArquivoRetorno().getSituacao())) {
				setMensagemDetalhada("msg_erro", getJobBaixarContasPagarArquivoRetorno().getSituacao(), true);
			} else {
				setMensagemDetalhada("msg_erro", e.getMessage(), true);
			}
			setJobBaixarContasPagarArquivoRetorno(null);
			setAtivarPush(false);
		}
		
	}

	public void executarConsultaAutomatica() throws Exception {
		if (Uteis.ARQUIVOS_CONTROLE_COBRANCA.get(Uteis.getNomeArquivoComUnidadeEnsino(getControleCobrancaPagarVO().getNomeArquivo_Apresentar(), getControleCobrancaPagarVO().getUnidadeEnsinoVO().getCodigo())).equals(SituacaoArquivoRetorno.ARQUIVO_PROCESSADO.getValor())) {
			if (getControleCobrancaPagarVO().getContaPagarRegistroArquivoVOs().isEmpty()) {
				getControleCobrancaPagarVO().setContaPagarRegistroArquivoVOs(getFacadeFactory().getContaPagarRegistroArquivoFacade().consultarPorControleCobrancaPagar(getControleCobrancaPagarVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			}
			setMapaPendenciasControleCobrancaPagarVOs(getFacadeFactory().getMapaPendenciasControleCobrancaPagarFacade().consultarPorControleCobrancaPagarSelecionado(getControleCobrancaPagarVO().getCodigo(), getControleConsulta().getLimitePorPagina2(), getControleConsulta().getOffset2(), null, getUsuarioLogado()));
			consultarTotalPendenciasProcessamentoArquivoRetorno();
			contabilizarTotais();			
			if (getControleCobrancaPagarVO().getListaContaCorrenteArquivoRetorno().size() > 1) {			
				setApresentarModalMensagemContaCorrente(true);
			}
			montarListaSelectItemContaCorrente();
			setArquivoProcessado(true);
			throw new ConsistirException(SituacaoArquivoRetorno.ARQUIVO_PROCESSADO.getDescricao());
		}
		if (Uteis.ARQUIVOS_CONTROLE_COBRANCA.get(Uteis.getNomeArquivoComUnidadeEnsino(getControleCobrancaPagarVO().getNomeArquivo_Apresentar(), getControleCobrancaPagarVO().getUnidadeEnsinoVO().getCodigo())).equals(SituacaoArquivoRetorno.CONTAS_BAIXADAS.getValor())) {
			getControleConsulta().setPaginaAtual(1);
			getControleCobrancaPagarVO().setContaPagarRegistroArquivoVOs(getFacadeFactory().getContaPagarRegistroArquivoFacade().consultarPorControleCobrancaPagar(getControleCobrancaPagarVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			setMapaPendenciasControleCobrancaPagarVOs(getFacadeFactory().getMapaPendenciasControleCobrancaPagarFacade().consultarPorControleCobrancaPagarSelecionado(getControleCobrancaPagarVO().getCodigo(), getControleConsulta().getLimitePorPagina2(), getControleConsulta().getOffset2(), null, getUsuarioLogado()));
			consultarTotalPendenciasProcessamentoArquivoRetorno();
			contabilizarTotais();
			montarListaSelectItemContaCorrente();
			getControleConsulta().setTotalRegistrosEncontrados2(getFacadeFactory().getMapaPendenciasControleCobrancaPagarFacade().consultarQtdeMapaPendenciaPorControleCobranca(getControleCobrancaPagarVO().getCodigo(), getUsuarioLogado()));			
			String msg = SituacaoArquivoRetorno.CONTAS_BAIXADAS.getDescricao();
			if (getJobBaixarContasPagarArquivoRetorno() != null) {
				msg = getJobBaixarContasPagarArquivoRetorno().getSituacao();
			}
			setJobBaixarContasPagarArquivoRetorno(null);
			throw new ConsistirException(msg);
		}
		
	}

	// Atualiza a página a cada 1 minuto
	public synchronized String getExecutarAtualizacaoPagina() {
		if (isAtivarPush()) {
			realizarAtualizacaoProcessamento();
			try {
				if (!isAtivarPush()) {
					if (getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getControleCobrancaPagarVO().getUnidadeEnsinoVO().getCodigo()).getCriarContaReceberPendenciaArquivoRetornoAutomaticamente() && isAbrirModalPendenciaArquivoRetorno()) {
						return getApresentarModalTotalPendenciaProcessamentoArquivoRetorno();
					}
					return "RichFaces.$('modalProcessamento').hide()";
				}
			} catch (Exception e) {
				return "RichFaces.$('modalProcessamento').hide();";
			}
			return "";
		} else if (getApresentarModalTotalPendenciaProcessamentoArquivoRetorno().trim().isEmpty()) {
			return "RichFaces.$('modalProcessamento').hide();";
		}
		return getApresentarModalTotalPendenciaProcessamentoArquivoRetorno();
	}

	public void desabilitarPush() {
		setAbrirModalPendenciaArquivoRetorno(false);
		setAtivarPush(false);
	}

	public void consultarTotalPendenciasProcessamentoArquivoRetorno() {
		try {
			ConfiguracaoFinanceiroVO config = getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getControleCobrancaPagarVO().getUnidadeEnsinoVO().getCodigo());
			if (!config.getCriarContaReceberPendenciaArquivoRetornoAutomaticamente()) {
				return;
			}
			Double totalValorDiferenca = 0.0;
			for (MapaPendenciasControleCobrancaPagarVO mpccVO : getMapaPendenciasControleCobrancaPagarVOs()) {
				totalValorDiferenca = totalValorDiferenca + mpccVO.getValorDiferenca();
			}
			setTotalValorDiferencaPendencia(totalValorDiferenca);
			if (totalValorDiferenca > 0) {
				setAbrirModalPendenciaArquivoRetorno(true);
			}
			setMensagemID("msg_contareCeber_contasCriadas");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void contabilizarTotais() {
		if (!getControleCobrancaPagarVO().getContaPagarRegistroArquivoVOs().isEmpty()) {
			setValorTotalRecebido(0.0);
			setQtdRegistros(0);
			Iterator<ContaPagarRegistroArquivoVO> i = getControleCobrancaPagarVO().getContaPagarRegistroArquivoVOs().iterator();
			while (i.hasNext()) {
				ContaPagarRegistroArquivoVO conta = (ContaPagarRegistroArquivoVO)i.next();
				if (conta.isContaPagarEfetivado() && conta.getRegistroDetalhePagarVO().getValorPagamento().doubleValue() > 0  && !Uteis.isAtributoPreenchido(conta.getRegistroDetalhePagarVO().getNossoNumeroContaAgrupada())) {
					setValorTotalRecebido(getValorTotalRecebido() + conta.getRegistroDetalhePagarVO().getValorPagamento());
					setQtdRegistros(getQtdRegistros() + 1);
				}
				
			}
		  Boolean  registroAgrupado = getControleCobrancaPagarVO().getContaPagarRegistroArquivoVOs().stream().anyMatch(p ->  Uteis.isAtributoPreenchido(p.getRegistroDetalhePagarVO().getNossoNumeroContaAgrupada()));
		  if(registroAgrupado) {			 
			 contabilizarContasAgrupadas();
		  }
		}
		
		if (!getControleCobrancaPagarVO().getContaPagarRegistroArquivoVOs().isEmpty()) {
			setValorTotalRecebidoNaoLoc(0.0);
			setQtdRegistrosNaoLoc(0);
			Iterator<ContaPagarRegistroArquivoVO> i = getControleCobrancaPagarVO().getContaPagarRegistroArquivoVOs().iterator();
			while (i.hasNext()) {
				ContaPagarRegistroArquivoVO conta = (ContaPagarRegistroArquivoVO)i.next();
				if (conta.getContaPagarVO().getObservacao().equals("Conta Não Localizada!")) {
					setValorTotalRecebidoNaoLoc(getValorTotalRecebidoNaoLoc() + conta.getRegistroDetalhePagarVO().getValorPagamento());
					setQtdRegistrosNaoLoc(getQtdRegistrosNaoLoc() + 1);
				}
			}
		}
		if (!getControleCobrancaPagarVO().getListaContaPagarHistorico().isEmpty()) {
			setValorTotalRecebidoDuplicidade(0.0);
			setQtdRegistrosDuplicidade(0);
			Iterator<ContaPagarHistoricoVO> i = getControleCobrancaPagarVO().getListaContaPagarHistorico().iterator();
			while (i.hasNext()) {
				ContaPagarHistoricoVO conta = (ContaPagarHistoricoVO)i.next();
				setValorTotalRecebidoDuplicidade(getValorTotalRecebidoDuplicidade() + conta.getValorPagamento());
				setQtdRegistrosDuplicidade(getQtdRegistrosDuplicidade() + 1);
			}
		}
	}

	public void realizarCriacaoContaReceberPendenciaArquivoRetorno() {
		try {
			/*
			 * ConfiguracaoFinanceiroVO config = getConfiguracaoFinanceiroPadraoSistema(); ContaCorrenteVO contaCorrente = new ContaCorrenteVO(); setMapaPendenciasControleCobrancaVOs(getFacadeFactory().getMapaPendenciasControleCobrancaFacade().consultarPorContaReceberRegistroArquivoSelecionado(getControleCobrancaBancoVO().getRegistroArquivoVO().getCodigo(), null, null, false, getUsuarioLogado())); for (MapaPendenciasControleCobrancaVO mpccVO : getMapaPendenciasControleCobrancaVOs()) { Integer codOrigem = 0; if (!mpccVO.getContaReceber().getCodOrigem().equals("")) { codOrigem = Integer.valueOf(mpccVO.getContaReceber().getCodOrigem()); } Double valorDiferenca = mpccVO.getValorDiferenca(); // mpccVO.getContaReceber().getValor() // - // mpccVO.getContaReceber().getValorRecebido() // - // mpccVO.getContaReceber().getValorDescontoRecebido(); contaCorrente = getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(config.getContaCorrentePadraoMensalidade(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
			 * getUsuarioLogado()); PessoaVO pessoaVO = mpccVO.getContaReceber().getPessoa(); if (mpccVO.getContaReceber().getTipoPessoa().equals(TipoPessoa.RESPONSAVEL_FINANCEIRO.getValor())) { pessoaVO = mpccVO.getContaReceber().getResponsavelFinanceiro(); pessoaVO.setAluno(false); pessoaVO.setFuncionario(false); pessoaVO.setProfessor(false); pessoaVO.setCandidato(false); pessoaVO.setMembroComunidade(false); pessoaVO.setPossuiAcessoVisaoPais(true); } getFacadeFactory().getContaReceberFacade().criarContaReceber(mpccVO.getContaReceber().getMatriculaAluno(), mpccVO.getContaReceber().getParceiroVO(), pessoaVO, mpccVO.getMatricula().getUnidadeEnsino(), mpccVO.getMatricula().getUnidadeEnsino(), contaCorrente, codOrigem, TipoOrigemContaReceber.OUTROS.getValor(), mpccVO.getContaReceber().getDataVencimento(), mpccVO.getContaReceber().getDataVencimento(), valorDiferenca, config.getCentroReceitaMensalidadePadrao().getCodigo(), 1, 1, TipoBoletoBancario.OUTROS.getValor(), "PE" + mpccVO.getContaReceber().getParcela(),
			 * getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado(), mpccVO.getContaReceber().getFornecedor(), 0, ""); getFacadeFactory().getMapaPendenciasControleCobrancaFacade().excluir(mpccVO, getUsuarioLogado()); } setMapaPendenciasControleCobrancaVOs(getFacadeFactory().getMapaPendenciasControleCobrancaFacade().consultarPorContaReceberRegistroArquivoSelecionado(getControleCobrancaBancoVO().getRegistroArquivoVO().getCodigo(), getControleConsulta().getLimitePorPagina2(), 0, null, getUsuarioLogado())); getControleConsulta().setTotalRegistrosEncontrados2(getFacadeFactory().getMapaPendenciasControleCobrancaFacade().consultarQtdeMapaPendenciaPorRegistroArquivo(getControleCobrancaBancoVO().getRegistroArquivoVO().getCodigo(), getUsuarioLogado())); contaCorrente = null; config = null;
			 */
			setMensagemID("msg_contareCeber_contasCriadas");
		} catch (Exception e) {
			try {
				setMapaPendenciasControleCobrancaPagarVOs(getFacadeFactory().getMapaPendenciasControleCobrancaPagarFacade().consultarPorControleCobrancaPagarSelecionado(getControleCobrancaPagarVO().getCodigo(), null, null, null, getUsuarioLogado()));
			} catch (Exception ex) {
				if (!ex.getMessage().equals("")) {
					setMensagemDetalhada("msg_erro", ex.getMessage());
				}
			}
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			setAbrirModalPendenciaArquivoRetorno(false);
		}
	}

	public void finalizarModalPendenciaArquivoRetorno() {
		setAbrirModalPendenciaArquivoRetorno(false);
	}
	
	public void alterarMapaPendenciasControleCobrancaSelecionado() {
		try {
			MapaPendenciasControleCobrancaPagarVO obj = (MapaPendenciasControleCobrancaPagarVO) context().getExternalContext().getRequestMap().get("mapaPendenciasItens");
			getFacadeFactory().getMapaPendenciasControleCobrancaPagarFacade().alterarSelecionado(obj.getCodigo(), obj.getSelecionado(), getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void setMarcarTodos(Boolean marcarTodos) {
		this.marcarTodos = marcarTodos;
	}

	public Boolean getMarcarTodos() {
		return marcarTodos;
	}

	public void selecionarTodos() {
		try {
			for (MapaPendenciasControleCobrancaPagarVO mapaPendenciasControleCobrancaVO : getMapaPendenciasControleCobrancaPagarVOs()) {
				mapaPendenciasControleCobrancaVO.setSelecionado(getMarcarTodos());
			}
			getFacadeFactory().getMapaPendenciasControleCobrancaPagarFacade().alterarSelecionadoPorControleCobrancaPagar(getControleCobrancaPagarVO().getCodigo(), true, getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void irPaginaInicial() throws Exception {
		this.consultar();

	}

	public void irPaginaAnterior() throws Exception {
		controleConsulta.setPaginaAtual(controleConsulta.getPaginaAtual() - 1);
		this.consultar();
	}

	public void irPaginaPosterior() throws Exception {
		controleConsulta.setPaginaAtual(controleConsulta.getPaginaAtual() + 1);
		this.consultar();
	}

	public void irPaginaFinal() throws Exception {
		controleConsulta.setPaginaAtual(controleConsulta.getNrTotalPaginas());
		this.consultar();

	}

	public void limparCampoConsulta() {
		getControleConsulta().setValorConsulta("");
		getControleConsulta().setDataIni(Uteis.getDataPrimeiroDiaMes(new Date()));
		getControleConsulta().setDataFim(Uteis.getDataUltimoDiaMes(new Date()));
	}

	public void inicializarListasSelectItemTodosComboBox() throws Exception {
		montarListaSelectItemUnidadeEnsino();
		montarListaSelectItemLayoutsBancos();
	}

	public void montarListaSelectItemUnidadeEnsino() {
		try {
			if (getUnidadeEnsinoLogado().getCodigo().intValue() != 0) {
				setListaSelectItemUnidadeEnsino(new ArrayList<SelectItem>());
				getListaSelectItemUnidadeEnsino().add(new SelectItem(0, ""));
				UnidadeEnsinoVO obj = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCodigo(getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
				getControleCobrancaPagarVO().setUnidadeEnsinoVO(obj);
				getListaSelectItemUnidadeEnsino().add(new SelectItem(obj.getCodigo(), obj.getNome()));
				return;
			}
			List resultadoConsulta = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome("", 0, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void montarListaSelectItemLayoutsBancos() {
		try {
			List listaBancoVOs = getFacadeFactory().getBancoFacade().consultarPorBancoNivelComboBox(false, getUsuarioLogado());
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, ""));
			Iterator j = listaBancoVOs.iterator();
			while (j.hasNext()) {
				BancoVO item = (BancoVO) j.next();
	            BancoEnum banco = BancoEnum.getEnum("CNAB240", item.getNrBanco());
	            if (banco != null && banco.getPossuiRemessaContaPagar()) {
	            	objs.add(new SelectItem(item.getCodigo(), item.getNome()));
	            }
	        }
			SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
			Collections.sort((List) objs, ordenador);
			setListaSelectItemBanco(objs);
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}

	}

	public void montarListaSelectItemContaCorrente() {
		getListaSelectItemContaCorrente().clear();
		if(Uteis.isAtributoPreenchido(getControleCobrancaPagarVO().getContaCorrenteVO()) && !Uteis.isAtributoPreenchido(getControleCobrancaPagarVO().getListaContaCorrenteArquivoRetorno()) ){
			if (Uteis.isAtributoPreenchido(getControleCobrancaPagarVO().getContaCorrenteVO().getNomeApresentacaoSistema())) {
				getListaSelectItemContaCorrente().add(new SelectItem(getControleCobrancaPagarVO().getContaCorrenteVO().getCodigo(), getControleCobrancaPagarVO().getContaCorrenteVO().getNomeApresentacaoSistema()));
			} else {
				getListaSelectItemContaCorrente().add(new SelectItem(getControleCobrancaPagarVO().getContaCorrenteVO().getCodigo(), getControleCobrancaPagarVO().getContaCorrenteVO().getNomeBancoAgenciaContaApresentar()));
			}
		}else if(Uteis.isAtributoPreenchido(getControleCobrancaPagarVO().getListaContaCorrenteArquivoRetorno())){
			for (ContaCorrenteVO obj : getControleCobrancaPagarVO().getListaContaCorrenteArquivoRetorno()) {
				if (Uteis.isAtributoPreenchido(obj.getNomeApresentacaoSistema())) {
					getListaSelectItemContaCorrente().add(new SelectItem(obj.getCodigo(), obj.getNomeApresentacaoSistema()));
				} else {
					getListaSelectItemContaCorrente().add(new SelectItem(obj.getCodigo(), obj.getNomeBancoAgenciaContaApresentar()));
				}
			}	
		}
	}
	
	
	public void desabilitarApresentarModalMensagemContaCorrente() {
		try {
			setApresentarModalMensagemContaCorrente(false);
			getFacadeFactory().getControleCobrancaPagarFacade().realizarAtualizacaoContaCorrente(getControleCobrancaPagarVO().getCodigo(), getControleCobrancaPagarVO().getContaCorrenteVO().getCodigo(),getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public ControleCobrancaPagarVO getControleCobrancaPagarVO() {
		if (controleCobrancaPagarVO == null) {
			controleCobrancaPagarVO = new ControleCobrancaPagarVO();
		}
		return controleCobrancaPagarVO;
	}

	public void setControleCobrancaPagarVO(ControleCobrancaPagarVO controleCobrancaPagarVO) {
		this.controleCobrancaPagarVO = controleCobrancaPagarVO;
	}
	
	public boolean isApresentarModalMensagemContaCorrente() {
		return apresentarModalMensagemContaCorrente;
	}

	public void setApresentarModalMensagemContaCorrente(Boolean apresentarModalMensagemContaCorrente) {
		this.apresentarModalMensagemContaCorrente = apresentarModalMensagemContaCorrente;
	}
	

	public List getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList(0);
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public List<SelectItem> getListaSelectItemBanco() {
		if (listaSelectItemBanco == null) {
			listaSelectItemBanco = new ArrayList(0);
		}
		return listaSelectItemBanco;
	}

	public void setListaSelectItemBanco(List<SelectItem> listaSelectItemBanco) {
		this.listaSelectItemBanco = listaSelectItemBanco;
	}

	public List getTipoConsultaCombo() {
		List itens = new ArrayList(0);		
		itens.add(new SelectItem("dataProcessamento", "Data de Processamento"));
		itens.add(new SelectItem("codigo", "Número"));
		return itens;
	}

	public boolean isAtivarPush() {
		return ativarPush;
	}

	public void setAtivarPush(boolean ativarPush) {
		this.ativarPush = ativarPush;
	}

	public boolean isArquivoProcessado() {
		return arquivoProcessado;
	}

	public void setArquivoProcessado(boolean arquivoProcessado) {
		this.arquivoProcessado = arquivoProcessado;
	}

	public List<MapaPendenciasControleCobrancaPagarVO> getMapaPendenciasControleCobrancaPagarVOs() {
		if (mapaPendenciasControleCobrancaPagarVOs == null) {
			mapaPendenciasControleCobrancaPagarVOs = new ArrayList<>();
		}
		return mapaPendenciasControleCobrancaPagarVOs;
	}

	public void setMapaPendenciasControleCobrancaPagarVOs(List<MapaPendenciasControleCobrancaPagarVO> mapaPendenciasControleCobrancaPagarVOs) {
		this.mapaPendenciasControleCobrancaPagarVOs = mapaPendenciasControleCobrancaPagarVOs;
	}

	public JobBaixarContasPagarArquivoRetorno getJobBaixarContasPagarArquivoRetorno() {
		return jobBaixarContasPagarArquivoRetorno;
	}

	public void setJobBaixarContasPagarArquivoRetorno(JobBaixarContasPagarArquivoRetorno jobBaixarContasPagarArquivoRetorno) {
		this.jobBaixarContasPagarArquivoRetorno = jobBaixarContasPagarArquivoRetorno;
	}

	public boolean isAbrirModalPendenciaArquivoRetorno() {
		return abrirModalPendenciaArquivoRetorno;
	}

	public void setAbrirModalPendenciaArquivoRetorno(boolean abrirModalPendenciaArquivoRetorno) {
		this.abrirModalPendenciaArquivoRetorno = abrirModalPendenciaArquivoRetorno;
	}

	public Double getTotalValorDiferencaPendencia() {
		if (totalValorDiferencaPendencia == null) {
			totalValorDiferencaPendencia = 0.0;
		}
		return totalValorDiferencaPendencia;
	}

	public void setTotalValorDiferencaPendencia(Double totalValorDiferencaPendencia) {
		this.totalValorDiferencaPendencia = totalValorDiferencaPendencia;
	}
	
	

	public Double getValorTotalRecebido() {
		if (valorTotalRecebido == null) {
			valorTotalRecebido = 0.0;
		}
		return valorTotalRecebido;
	}

	public void setValorTotalRecebido(Double valorTotalRecebido) {
		this.valorTotalRecebido = valorTotalRecebido;
	}

	public Double getValorTotalRecebidoDuplicidade() {
		if (valorTotalRecebidoDuplicidade == null) {
			valorTotalRecebidoDuplicidade = 0.0;
		}
		return valorTotalRecebidoDuplicidade;
	}

	public void setValorTotalRecebidoDuplicidade(Double valorTotalRecebidoDuplicidade) {
		this.valorTotalRecebidoDuplicidade = valorTotalRecebidoDuplicidade;
	}

	public Double getValorTotalRecebidoNaoLoc() {
		if (valorTotalRecebidoNaoLoc == null) {
			valorTotalRecebidoNaoLoc = 0.0;
		}
		return valorTotalRecebidoNaoLoc;
	}

	public void setValorTotalRecebidoNaoLoc(Double valorTotalRecebidoNaoLoc) {
		this.valorTotalRecebidoNaoLoc = valorTotalRecebidoNaoLoc;
	}

	public Integer getQtdRegistros() {
		if (qtdRegistros == null) {
			qtdRegistros = 0;
		}
		return qtdRegistros;
	}

	public void setQtdRegistros(Integer qtdRegistros) {
		this.qtdRegistros = qtdRegistros;
	}

	public Integer getQtdRegistrosDuplicidade() {
		if (qtdRegistrosDuplicidade == null) {
			qtdRegistrosDuplicidade = 0;
		}
		return qtdRegistrosDuplicidade;
	}

	public void setQtdRegistrosDuplicidade(Integer qtdRegistrosDuplicidade) {
		this.qtdRegistrosDuplicidade = qtdRegistrosDuplicidade;
	}

	public Integer getQtdRegistrosNaoLoc() {
		if (qtdRegistrosNaoLoc == null) {
			qtdRegistrosNaoLoc = 0;
		}
		return qtdRegistrosNaoLoc;
	}

	public void setQtdRegistrosNaoLoc(Integer qtdRegistrosNaoLoc) {
		this.qtdRegistrosNaoLoc = qtdRegistrosNaoLoc;
	}

	public List<SelectItem> getListaSelectItemContaCorrente() {
		if (listaSelectItemContaCorrente == null) {
			listaSelectItemContaCorrente = new ArrayList<>();
		}
		return listaSelectItemContaCorrente;
	}

	public void setListaSelectItemContaCorrente(List<SelectItem> listaSelectItemContaCorrente) {
		this.listaSelectItemContaCorrente = listaSelectItemContaCorrente;
	}

	public String getApresentarModalTotalPendenciaProcessamentoArquivoRetorno() {
		try {
			if (getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getControleCobrancaPagarVO().getUnidadeEnsinoVO().getCodigo()).getCriarContaReceberPendenciaArquivoRetornoAutomaticamente() && isAbrirModalPendenciaArquivoRetorno()) {
				return "RichFaces.$('modalProcessamento').hide();RichFaces.$('panelPendenciaArquivoRetorno').show()";
			} else if (!isAtivarPush()) {
				return "RichFaces.$('modalProcessamento').hide();";
			}
		} catch (Exception e) {

		}
		return "";
	}
	
	
	public void montarContasAgrupadasRegistroArquivoVOs() {
		getContasAgrupadasRegistroArquivos().clear();
		setQtdRegistrosAgrupado(0);
	    setVlrTotalRegistrosAgrupadoModal(0.0);	
		ContaPagarRegistroArquivoVO obj = (ContaPagarRegistroArquivoVO) context().getExternalContext().getRequestMap().get("contaPagarRegistroArquivoItens");
	    for(ContaPagarRegistroArquivoVO conta : getControleCobrancaPagarVO().getContaPagarRegistroArquivoVOs()){
	    	if(conta.getRegistroDetalhePagarVO().getNossoNumeroContaAgrupada().equals(obj.getRegistroDetalhePagarVO().getNossoNumeroContaAgrupada())) {
	    		getContasAgrupadasRegistroArquivos().add(conta);
	    		setQtdRegistrosAgrupado(getQtdRegistrosAgrupado() + 1);
	    		setVlrTotalRegistrosAgrupadoModal(getVlrTotalRegistrosAgrupadoModal() + conta.getRegistroDetalhePagarVO().getValorPagamento());
	    	}
	    }	
	}

	public List<ContaPagarRegistroArquivoVO> getContasAgrupadasRegistroArquivos() {
		      if(contasAgrupadasRegistroArquivos == null ) {
		    	  contasAgrupadasRegistroArquivos = new ArrayList<ContaPagarRegistroArquivoVO>(0);
		      }
		return contasAgrupadasRegistroArquivos;
	}

	public void setContasAgrupadasRegistroArquivos(List<ContaPagarRegistroArquivoVO> contasAgrupadasRegistroArquivos) {
		this.contasAgrupadasRegistroArquivos = contasAgrupadasRegistroArquivos;
	}
	 
	
	
	public Integer getQtdRegistrosAgrupado() {
		if (qtdRegistrosAgrupado == null) {
			qtdRegistrosAgrupado = 0;
		} 
		return qtdRegistrosAgrupado ;
	}
	
	public void setQtdRegistrosAgrupado(Integer qtdRegistrosAgrupado) {
		this.qtdRegistrosAgrupado = qtdRegistrosAgrupado;
	}
	
	public Double getVlrTotalRegistrosAgrupado() {   
		if (vlrTotalRegistrosAgrupado == null) {
			vlrTotalRegistrosAgrupado = 0.0;
		}
		return vlrTotalRegistrosAgrupado;
	}
	
	public void setVlrTotalRegistrosAgrupado(Double vlrTotalRegistrosAgrupado) {
		this.vlrTotalRegistrosAgrupado = vlrTotalRegistrosAgrupado;
	}
	
	public void contabilizarContasAgrupadas() {
		setQtdTotalRegistrosAgrupados(0);
		setVlrTotalRegistrosAgrupado(0.0);	
		
		for(ContaPagarRegistroArquivoVO  conta : getControleCobrancaPagarVO().getContaPagarRegistroArquivoVOs()) {
			if(conta.isContaPagarEfetivado() && Uteis.isAtributoPreenchido(conta.getRegistroDetalhePagarVO().getNossoNumeroContaAgrupada())) {
				setQtdTotalRegistrosAgrupados(getQtdTotalRegistrosAgrupados()+1);
				setVlrTotalRegistrosAgrupado(getVlrTotalRegistrosAgrupado() + conta.getRegistroDetalhePagarVO().getValorPagamento());
			}
			
			
		}
   
		
      

	}
	 
	
	
	public void realizarDownloadArquivo() {
		try {			
			if(getControleCobrancaPagarVO().getArquivoRetornoContaPagar() == null) {
				return;
			}		
	    		HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
	    		 getControleCobrancaPagarVO().getArquivoRetornoContaPagar().setPastaBaseArquivo(getFacadeFactory().getArquivoFacade().executarDefinicaoUrlFisicoAcessoArquivo( getControleCobrancaPagarVO().getArquivoRetornoContaPagar(), getControleCobrancaPagarVO().getArquivoRetornoContaPagar().getPastaBaseArquivoEnum(), getConfiguracaoGeralPadraoSistema()).replace( getControleCobrancaPagarVO().getArquivoRetornoContaPagar().getNome(), ""));
	    		request.setAttribute("arquivoVO",   getControleCobrancaPagarVO().getArquivoRetornoContaPagar());
				context().getExternalContext().dispatch("/DownloadSV");
				FacesContext.getCurrentInstance().responseComplete();
	    	
	    	
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	 
	
	public Integer getQtdTotalRegistrosAgrupados() {
		if(qtdTotalRegistrosAgrupados == null ) {
			qtdTotalRegistrosAgrupados = 0 ;
		}
		return qtdTotalRegistrosAgrupados;
	}

	public void setQtdTotalRegistrosAgrupados(Integer qtdTotalRegistrosAgrupados) {		
		this.qtdTotalRegistrosAgrupados = qtdTotalRegistrosAgrupados;
	}

	public Double getVlrTotalRegistrosAgrupadoModal() {
      if(vlrTotalRegistrosAgrupadoModal == null) {
    	  vlrTotalRegistrosAgrupadoModal = 0.0;
      }
		return vlrTotalRegistrosAgrupadoModal;
	}

	public void setVlrTotalRegistrosAgrupadoModal(Double vlrTotalRegistrosAgrupadoModal) {
		this.vlrTotalRegistrosAgrupadoModal = vlrTotalRegistrosAgrupadoModal;
	}
	 
	 
	 
	
}
