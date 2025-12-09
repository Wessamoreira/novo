package controle.financeiro;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas controleCobrancaForm.xhtml controleCobrancaCons.xhtml) com as funcionalidades da classe <code>ControleCobranca</code>
 * . Implemtação da camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see ControleCobranca
 * @see ControleCobrancaVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle.MSG_TELA;
import jobs.JobBaixarContasArquivoRetorno;
import jobs.JobExecutarArquivoRetornoLocalizarContaReceber;
import jobs.JobProcessarArquivoRetorno;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.ContaPagarVO;
import negocio.comuns.financeiro.ContaReceberNaoLocalizadaArquivoRetornoVO;
import negocio.comuns.financeiro.ContaReceberRegistroArquivoVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.ControleCobrancaVO;
import negocio.comuns.financeiro.MapaPendenciasControleCobrancaVO;
import negocio.comuns.financeiro.RegistroDetalheVO;
import negocio.comuns.financeiro.enumerador.SituacaoProcessamentoArquivoRetornoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.Bancos;
import negocio.comuns.utilitarias.dominios.SituacaoArquivoRetorno;
import negocio.comuns.utilitarias.dominios.TipoBoletoBancario;
import negocio.comuns.utilitarias.dominios.TipoOrigemContaReceber;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.financeiro.NegociacaoRecebimento;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.controle.arquitetura.SuperControleRelatorio;

@Controller("ControleCobrancaControle")
@Scope("viewScope")
@Lazy
public class ControleCobrancaControle extends SuperControleRelatorio implements Serializable {
	
	//25/04 periodo
	
	private ControleCobrancaVO controleCobrancaVO;
	private ControleCobrancaVO controleCobrancaBancoVO;
	private List<ContaReceberVO> contaReceberVOs = new ArrayList<ContaReceberVO>(0);
	private List<ContaReceberRegistroArquivoVO> contaReceberRegistroArquivoVOs = new ArrayList<ContaReceberRegistroArquivoVO>(0);
	private List<ContaReceberRegistroArquivoVO> contaReceberNegociadaRegistroArquivoVOs = new ArrayList<ContaReceberRegistroArquivoVO>(0);
	private List<ContaReceberRegistroArquivoVO> contaReceberRegistroArquivoDuplicidadeVOs = new ArrayList<ContaReceberRegistroArquivoVO>(0);
	private ContaReceberVO contaReceberVO = new ContaReceberVO();
	private boolean arquivoProcessado = false;
	private List<MapaPendenciasControleCobrancaVO> mapaPendenciasControleCobrancaVOs;
	private Boolean marcarTodos;
//	PushEventListener listener;
	private Boolean ativarPush;
	private JobProcessarArquivoRetorno jobProcessarArquivoRetorno;
	private JobBaixarContasArquivoRetorno jobBaixarContasArquivoRetorno;
	private List listaSelectItemUnidadeEnsino;
	private String tipoCarteira;
	private Double totalValorDiferencaPendencia;
	private Boolean abrirModalPendenciaArquivoRetorno;
	private Double valorTotalRecebido;
	private Double valorTotalRecebidoDuplicidade;	
	private Double valorTotalRecebidoNaoLoc;
	private Integer contaCorrente;
	private Integer qtdRegistros;
	private Integer qtdRegistrosDuplicidade;	
	private Integer qtdRegistrosNaoLoc;
	private Long qtdRegistrosConfirmados;
	private Long qtdRegistrosRejeitados;
	private Boolean apresentarModalMensagelContaPagar;
	private List<SelectItem> listaSelectItemContaCorrenteVOs;
	private List<SelectItem> listaSelectItemContaCorrente;
	private Boolean apresentarModalMensagemContaCorrente;
	private Boolean apresentarBotaoNeogicacao;
	private String pessoa;
	private String nrDocumento;
	private String nossoNumero;
	private String pessoaDuplicidade;
	private String nrDocumentoDuplicidade;
	private String nossoNumeroDuplicidade;
	public ContaReceberVO contaReceberLogBaixa; 
	public ContaReceberVO contaReceberBaixaManual;
	protected Boolean fazerDownload;
	
	public ControleCobrancaControle() throws Exception {
		// obterUsuarioLogado();
		
		setControleConsulta(new ControleConsulta());
		montarListaSelectItemUnidadeEnsino();
		montarListaSelectItemContaCorrente2();
		getControleConsulta().setCampoConsulta("dataProcessamento");
		getControleConsulta().setDataIni(Uteis.obterDataAntiga(new Date(), 10));
		getControleConsulta().setDataFim(new Date());
		//consultar();
		setTipoCarteira("tipoCNAB");		
		setMensagemID("msg_entre_prmconsulta");
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe
	 * <code>ControleCobranca</code> para edição pelo usuário da aplicação.
	 *
	 * @throws Exception
	 */
	public String novo() throws Exception {
		removerObjetoMemoria(this);
		setControleCobrancaVO(null);
		setContaReceberVOs(null);
		setMapaPendenciasControleCobrancaVOs(null);
		inserirDadosResponsavelControleCobranca();
		setArquivoProcessado(false);
		setAtivarPush(false);
		montarListaSelectItemUnidadeEnsino();
		setTipoCarteira("tipoCNAB");		
		getControleCobrancaVO().getUnidadeEnsinoVO().setCodigo(getUnidadeEnsinoLogado().getCodigo());
		setMensagemID("msg_entre_dados");
		verificarPermissaoNegociacaoContaReceber();
		return Uteis.getCaminhoRedirecionamentoNavegacao("controleCobrancaForm.xhtml");
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe
	 * <code>ControleCobranca</code> para alteração. O objeto desta classe é
	 * disponibilizado na session da página (request) para que o JSP
	 * correspondente possa disponibilizá-lo para edição.
	 */
	public String editar() {
		try {
			ControleCobrancaVO obj = (ControleCobrancaVO) context().getExternalContext().getRequestMap().get("controleCobrancaItens");
			obj.setNovoObj(Boolean.FALSE);
			setControleCobrancaVO(obj);
			montarListaSelectItemUnidadeEnsino();
			setControleCobrancaBancoVO(getFacadeFactory().getControleCobrancaFacade().consultarPorChavePrimariaCompleto(obj.getCodigo(),Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
//			montarListaSelectItemContaCorrente2();
			getControleConsulta().setPaginaAtual(1);
			getControleConsulta().setPaginaAtual2(1);
			getControleConsulta().setPaginaAtual3(1);
			limparFiltroLista();
			verificarPermissaoNegociacaoContaReceber();
			getControleCobrancaBancoVO().setRegistroArquivoVO(getFacadeFactory().getRegistroArquivoFacade().consultarPorControleCobranca(getControleCobrancaBancoVO().getCodigo(), null, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			getControleConsulta().setTotalRegistrosEncontrados(getFacadeFactory().getContaReceberRegistroArquivoFacade().consultarQtdeContaReceberRegistroArquivoPorRegistroArquivo(getControleCobrancaBancoVO().getRegistroArquivoVO().getCodigo(), "", "", "", false, getUsuarioLogado(), false));
			setContaReceberRegistroArquivoVOs(getControleCobrancaBancoVO().getRegistroArquivoVO().getContaReceberRegistroArquivoVOs());
			getControleCobrancaBancoVO().getRegistroArquivoVO().setContaReceberRegistroArquivoVOs(getFacadeFactory().getContaReceberRegistroArquivoFacade().consultaRapidaPorRegistroArquivo(getControleCobrancaBancoVO().getRegistroArquivoVO().getCodigo(), null, 0, "", "", "", false, getUsuarioLogado(), false));
			getControleCobrancaVO().getRegistroArquivoVO().setContaReceberRegistroArquivoVOs(getControleCobrancaBancoVO().getRegistroArquivoVO().getContaReceberRegistroArquivoVOs());
			getControleCobrancaVO().getRegistroArquivoVO().setRegistroDetalheVOs(getFacadeFactory().getRegistroDetalheFacade().consultarRegistroDetalhesConfirmadosOuRejeitados(getControleCobrancaVO().getRegistroArquivoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));

			setContaReceberNegociadaRegistroArquivoVOs(getControleCobrancaBancoVO().getRegistroArquivoVO().getContaReceberNegociadaRegistroArquivoVOs());
			consultarDataScrollerContaReceberDuplicidade();
			//setContaReceberRegistroArquivoDuplicidadeVOs(getFacadeFactory().getContaReceberRegistroArquivoFacade().consultaRapidaPorRegistroArquivoDuplicidade(obj.getRegistroArquivoVO().getCodigo(), null, null, false, getUsuarioLogado()));
			//getControleConsulta().setTotalRegistrosEncontrados3(getFacadeFactory().getContaReceberRegistroArquivoFacade().consultarQtdeContaReceberRegistroArquivoPorRegistroArquivo(getControleCobrancaBancoVO().getRegistroArquivoVO().getCodigo(), "", "", "", false, getUsuarioLogado(), true));			
			setMapaPendenciasControleCobrancaVOs(getFacadeFactory().getMapaPendenciasControleCobrancaFacade().consultarPorContaReceberRegistroArquivoSelecionado(getControleCobrancaBancoVO().getRegistroArquivoVO().getCodigo(), getControleConsulta().getLimitePorPagina2(), getControleConsulta().getOffset2(), null, getUsuarioLogado()));
			getControleConsulta().setTotalRegistrosEncontrados2(getFacadeFactory().getMapaPendenciasControleCobrancaFacade().consultarQtdeMapaPendenciaPorRegistroArquivo(getControleCobrancaBancoVO().getRegistroArquivoVO().getCodigo(), getUsuarioLogado()));
			setArquivoProcessado(getControleCobrancaBancoVO().getRegistroArquivoVO().getArquivoProcessado());
			if(!getControleCobrancaBancoVO().getSituacaoProcessamento().isProcessamentoConcluido()) {
				getControleCobrancaVO().getRegistroArquivoVO().setContasBaixadas(false);	
			}else {
				getControleCobrancaVO().getRegistroArquivoVO().setContasBaixadas(!getFacadeFactory().getContaReceberRegistroArquivoFacade().consultSeExisteContaReceberRegistroArquivoComSituacaoReceber(getControleCobrancaVO(), false, getUsuarioLogadoClone()));	
			}
			if (getControleCobrancaVO().getRegistroArquivoVO().getContasBaixadas()) {
				getControleCobrancaVO().getRegistroArquivoVO().setSituacao(SituacaoArquivoRetorno.CONTAS_BAIXADAS.getValor());
				Uteis.ARQUIVOS_CONTROLE_COBRANCA.put(Uteis.getNomeArquivoComUnidadeEnsino(getControleCobrancaVO().getNomeArquivo_Apresentar(), getControleCobrancaVO().getUnidadeEnsinoVO().getCodigo()), getControleCobrancaVO().getRegistroArquivoVO().getSituacao());
			} else if (getControleCobrancaVO().getRegistroArquivoVO().getArquivoProcessado()) {
				getControleCobrancaVO().getRegistroArquivoVO().setSituacao(SituacaoArquivoRetorno.ARQUIVO_PROCESSADO.getValor());
				Uteis.ARQUIVOS_CONTROLE_COBRANCA.put(Uteis.getNomeArquivoComUnidadeEnsino(getControleCobrancaVO().getNomeArquivo_Apresentar(), getControleCobrancaVO().getUnidadeEnsinoVO().getCodigo()), getControleCobrancaVO().getRegistroArquivoVO().getSituacao());
			}
			setAtivarPush(getControleCobrancaBancoVO().getSituacaoProcessamento().equals(SituacaoProcessamentoArquivoRetornoEnum.EM_PROCESSAMENTO));
			contabilizarTotais();
			if(getAplicacaoControle().getMapThreadControleCobranca().containsKey(getControleCobrancaVO().getCodigo())) {
				Uteis.ARQUIVOS_CONTROLE_COBRANCA.put(Uteis.getNomeArquivoComUnidadeEnsino(getControleCobrancaVO().getNomeArquivo_Apresentar(), getControleCobrancaVO().getUnidadeEnsinoVO().getCodigo()), SituacaoArquivoRetorno.ARQUIVO_PROCESSADO.getValor());
				getControleCobrancaVO().setEdicaoManual(true);
				setProgressBarVO(getAplicacaoControle().getMapThreadControleCobranca().get(getControleCobrancaVO().getCodigo()));
			}
			List<ContaCorrenteVO> contaCorrenteVOs =  new ArrayList<ContaCorrenteVO>(0);
			if( Uteis.isAtributoPreenchido(getControleCobrancaBancoVO()) && !Uteis.isAtributoPreenchido(getControleCobrancaBancoVO().getContaCorrenteVO())) {
				contaCorrenteVOs = getFacadeFactory().getContaCorrenteFacade().consultarPorBancoPorNumeroContaCorrentePorDigitoContaCorrente(Bancos.getEnum(getControleCobrancaBancoVO().getBanco()).getNumeroBanco(), getControleCobrancaBancoVO().getRegistroArquivoVO().getRegistroHeader().getNumeroConta().toString(), getControleCobrancaBancoVO().getRegistroArquivoVO().getRegistroHeader().getDigitoConta(), getControleCobrancaBancoVO().getRegistroArquivoVO().getRegistroHeader().getCodigoConvenioBanco(), 0, false, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				getControleCobrancaVO().setListaContaCorrenteVOs(contaCorrenteVOs);
				getControleCobrancaVO().getRegistroArquivoVO().setListaContaCorrenteVOs(contaCorrenteVOs);
			}else if( Uteis.isAtributoPreenchido(getControleCobrancaBancoVO()) && Uteis.isAtributoPreenchido(getControleCobrancaBancoVO().getContaCorrenteVO())) {
				contaCorrenteVOs = new ArrayList<ContaCorrenteVO>(0);
				contaCorrenteVOs.add(getControleCobrancaBancoVO().getContaCorrenteVO());
			}
			if(!contaCorrenteVOs.isEmpty()) {
				montarListaSelectItemContaCorrente(contaCorrenteVOs);
			}
			setMensagemID("msg_dados_editar");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("controleCobrancaCons.xhtml");
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("controleCobrancaForm.xhtml");
	}
	
	public void visualizarContaReceberOutroNossoNumero() {
		try {
			ContaReceberRegistroArquivoVO obj = (ContaReceberRegistroArquivoVO) context().getExternalContext().getRequestMap().get("contaReceberRegistroArquivoItens");
			if (obj.getContaReceberVO().getCr_recebidooutronossonumero() && !obj.getContaReceberVO().getNossoNumeroOriginouBaixa().equals("")) {
				setContaReceberLogBaixa(getFacadeFactory().getContaReceberFacade().consultarNossoNumeroContaReceberPorNossoNumero(obj.getContaReceberVO().getNossoNumeroOriginouBaixa(), getUsuarioLogado()));				
			}
		} catch (Exception e) {
			setContaReceberLogBaixa(null);
		}
		
	}

	public void visualizarContaReceberBaixaManual() {
		try {
			ContaReceberRegistroArquivoVO obj = (ContaReceberRegistroArquivoVO) context().getExternalContext().getRequestMap().get("contaReceberRegistroArquivoItens");
			setContaReceberBaixaManual(obj.getContaReceberBaixaManualmente());				
		} catch (Exception e) {
			setContaReceberBaixaManual(null);
		}
		
	}
	
	public void verificarPermissaoNegociacaoContaReceber() {
		try {
			NegociacaoRecebimento.excluir("NegociacaoRecebimento", getUsuarioLogado());
			setApresentarBotaoNeogicacao(Boolean.TRUE);
		} catch (Exception e) {
			setApresentarBotaoNeogicacao(Boolean.FALSE);
		}
	}
	
	public String getExecutarAtualizacaoProcessamento() {		
		return getControleCobrancaBancoVO().getProgressBar();
	}

	public void realizarAtualizacaoProcessamento() {
		try {
			if (getAtivarPush()) {
				getFacadeFactory().getControleCobrancaFacade().realizarAtualizacaoDadosProcessamento(getControleCobrancaBancoVO());
				if (getControleCobrancaBancoVO().getSituacaoProcessamento().equals(SituacaoProcessamentoArquivoRetornoEnum.AGUARDANDO_PROCESSAMENTO) || getControleCobrancaBancoVO().getSituacaoProcessamento().equals(SituacaoProcessamentoArquivoRetornoEnum.ERRO_PROCESSAMENTO) || getControleCobrancaBancoVO().getSituacaoProcessamento().equals(SituacaoProcessamentoArquivoRetornoEnum.PROCESSAMENTO_INTERROMPIDO)) {
					executarConsultaAutomatica();
					if (getJobBaixarContasArquivoRetorno() != null) {
						setMensagemDetalhada("", getJobBaixarContasArquivoRetorno().getSituacao());
					}
					if (getControleCobrancaBancoVO().getSituacaoProcessamento().equals(SituacaoProcessamentoArquivoRetornoEnum.ERRO_PROCESSAMENTO) || getControleCobrancaBancoVO().getSituacaoProcessamento().equals(SituacaoProcessamentoArquivoRetornoEnum.PROCESSAMENTO_INTERROMPIDO)) {
						setMensagemDetalhada("msg_erro", getControleCobrancaBancoVO().getMotivoErroProcessamento(), true);
						setAtivarPush(false);
					}
				}
				if (getControleCobrancaBancoVO().getSituacaoProcessamento().equals(SituacaoProcessamentoArquivoRetornoEnum.PROCESSAMENTO_CONCLUIDO)) {
					setAtivarPush(false);
					executarConsultaAutomatica();					
				}
			}
		} catch (Exception e) {
			if (getControleCobrancaBancoVO().getSituacaoProcessamento().equals(SituacaoProcessamentoArquivoRetornoEnum.ERRO_PROCESSAMENTO) || getControleCobrancaBancoVO().getSituacaoProcessamento().equals(SituacaoProcessamentoArquivoRetornoEnum.PROCESSAMENTO_INTERROMPIDO)) {
				setMensagemDetalhada("msg_erro", getControleCobrancaBancoVO().getMotivoErroProcessamento(), true);
				setAtivarPush(false);
			} else if (getJobBaixarContasArquivoRetorno() != null) {
				setMensagemDetalhada("msg_erro", getJobBaixarContasArquivoRetorno().getSituacao(), true);
			} else {
				setMensagemDetalhada("msg_erro", e.getMessage(), true);
			}
		}
	}

	public void inicializarDadosNavegacaoTelaNegociacaoRecebimento() {
		ContaReceberRegistroArquivoVO obj = (ContaReceberRegistroArquivoVO) context().getExternalContext().getRequestMap().get("contaReceberNegociadaRegistroArquivoItens");
		if (obj != null && obj.getCodigo()>0) {
			removerControleMemoriaFlashTela("NegociacaoRecebimentoControle");
			context().getExternalContext().getSessionMap().put("contaReceberNegociacao", obj.getContaReceberVO().getNossoNumero());
		}
	}
	
	public String getExecutarAtualizacaoProcessamentoCons() {
		try {
			ControleCobrancaVO obj = (ControleCobrancaVO) getRequestMap().get("controleCobrancaItens");
			if (obj.getSituacaoProcessamento().equals(SituacaoProcessamentoArquivoRetornoEnum.EM_PROCESSAMENTO)) {
				getFacadeFactory().getControleCobrancaFacade().realizarAtualizacaoDadosProcessamento(obj);
			}
			return obj.getProgressBar();
		} catch (Exception e) {
			return "";
		}
	}

	// Atualiza a página a cada 1 minuto
	public synchronized String getExecutarAtualizacaoPagina() {		
		if (getAtivarPush()) {
			realizarAtualizacaoProcessamento();
			try {
				if(!getAtivarPush()){					
					if(getConfiguracaoFinanceiroPadraoSistema().getCriarContaReceberPendenciaArquivoRetornoAutomaticamente() && getAbrirModalPendenciaArquivoRetorno()){					
						return getApresentarModalTotalPendenciaProcessamentoArquivoRetorno();
					}
					return "RichFaces.$('modalProcessamento').hide()";					
				}
			} catch (Exception e) {
				return "RichFaces.$('modalProcessamento').hide();";
			}
			return "";
		}else if(getApresentarModalTotalPendenciaProcessamentoArquivoRetorno().trim().isEmpty()){
			return "RichFaces.$('modalProcessamento').hide();";
		}
		return getApresentarModalTotalPendenciaProcessamentoArquivoRetorno();
	}

	public void executarConsultaAutomatica() throws Exception {
		// if
		// (!getControleCobrancaBancoVO().getNomeArquivo_Apresentar().equals(""))
		// {
		// if (!Uteis.ARQUIVOS_CONTROLE_COBRANCA.isEmpty() &&
		// Uteis.ARQUIVOS_CONTROLE_COBRANCA.containsKey(Uteis.getNomeArquivoComUnidadeEnsino(getControleCobrancaBancoVO().getNomeArquivo_Apresentar(),
		// getControleCobrancaBancoVO().getUnidadeEnsinoVO().getCodigo()))) {
		// if (getContaReceberRegistroArquivoVOs().isEmpty() &&
		// Uteis.ARQUIVOS_CONTROLE_COBRANCA.get(Uteis.getNomeArquivoComUnidadeEnsino(getControleCobrancaBancoVO().getNomeArquivo_Apresentar(),
		// getControleCobrancaBancoVO().getUnidadeEnsinoVO().getCodigo())).equals(SituacaoArquivoRetorno.ARQUIVO_PROCESSADO.getValor()))
		// {
		// //
		// setControleCobrancaBancoVO(getFacadeFactory().getControleCobrancaFacade().consultarPorNomeArquivoAnoProcessamento(getControleCobrancaVO().getNomeArquivo_Apresentar(),
		// Uteis.getAnoDataAtual(), Uteis.NIVELMONTARDADOS_DADOSBASICOS,
		// getUsuarioLogado()));
		// setControleCobrancaBancoVO(getFacadeFactory().getControleCobrancaFacade().consultarPorNomeArquivoAnoProcessamentoUnidadeEnsino(getControleCobrancaVO().getNomeArquivo_Apresentar(),
		// Uteis.getAnoDataAtual(),
		// getControleCobrancaVO().getUnidadeEnsinoVO().getCodigo(),
		// Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
		// getControleCobrancaBancoVO().setRegistroArquivoVO(getFacadeFactory().getRegistroArquivoFacade().consultarPorControleCobranca(getControleCobrancaBancoVO().getCodigo(),
		// null, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
		// getControleConsulta().setTotalRegistrosEncontrados(getFacadeFactory().getContaReceberRegistroArquivoFacade().consultarQtdeContaReceberRegistroArquivoPorRegistroArquivo(getControleCobrancaBancoVO().getRegistroArquivoVO().getCodigo(),
		// false, getUsuarioLogado()));
		// setContaReceberRegistroArquivoVOs(getControleCobrancaBancoVO().getRegistroArquivoVO().getContaReceberRegistroArquivoVOs());
		// setArquivoProcessado(true);
		// setAtivarPush(false);
		// setListener(null);
		// setJobBaixarContasArquivoRetorno(null);
		// throw new
		// ConsistirException(SituacaoArquivoRetorno.ARQUIVO_PROCESSADO.getDescricao());
		// } else if (getMapaPendenciasControleCobrancaVOs().isEmpty() &&
		// Uteis.ARQUIVOS_CONTROLE_COBRANCA.get(Uteis.getNomeArquivoComUnidadeEnsino(getControleCobrancaBancoVO().getNomeArquivo_Apresentar(),
		// getControleCobrancaBancoVO().getUnidadeEnsinoVO().getCodigo())).equals(SituacaoArquivoRetorno.CONTAS_BAIXADAS.getValor()))
		// {
		// getControleConsulta().setPaginaAtual(1);
		// setContaReceberRegistroArquivoVOs(getFacadeFactory().getContaReceberRegistroArquivoFacade().consultaRapidaPorRegistroArquivo(getControleCobrancaBancoVO().getRegistroArquivoVO().getCodigo(),
		// getControleConsulta().getLimitePorPagina(),
		// getControleConsulta().getOffset(), false, getUsuarioLogado()));
		// getControleConsulta().setTotalRegistrosEncontrados(getFacadeFactory().getContaReceberRegistroArquivoFacade().consultarQtdeContaReceberRegistroArquivoPorRegistroArquivo(getControleCobrancaBancoVO().getRegistroArquivoVO().getCodigo(),
		// false, getUsuarioLogado()));
		// setMapaPendenciasControleCobrancaVOs(getFacadeFactory().getMapaPendenciasControleCobrancaFacade().consultarPorContaReceberRegistroArquivoSelecionado(getControleCobrancaBancoVO().getRegistroArquivoVO().getCodigo(),
		// getControleConsulta().getLimitePorPagina2(),
		// getControleConsulta().getOffset2(), null, getUsuarioLogado()));
		// getControleConsulta().setTotalRegistrosEncontrados2(getFacadeFactory().getMapaPendenciasControleCobrancaFacade().consultarQtdeMapaPendenciaPorRegistroArquivo(getControleCobrancaBancoVO().getRegistroArquivoVO().getCodigo(),
		// getUsuarioLogado()));
		// setAtivarPush(false);
		// setListener(null);
		// String msg = SituacaoArquivoRetorno.CONTAS_BAIXADAS.getDescricao();
		// if (getJobBaixarContasArquivoRetorno() != null) {
		// msg = getJobBaixarContasArquivoRetorno().getSituacao();
		// }
		// setJobBaixarContasArquivoRetorno(null);
		// throw new ConsistirException(msg);
		// }
		// }
		// }
		if (getControleCobrancaVO().getListaContaCorrenteVOs().size() > 1) {
			montarListaSelectItemContaCorrente(getControleCobrancaVO().getListaContaCorrenteVOs());
//			this.setApresentarModalMensagemContaCorrente(true);
		}
		if (getContaReceberRegistroArquivoVOs().isEmpty() && Uteis.ARQUIVOS_CONTROLE_COBRANCA.get(Uteis.getNomeArquivoComUnidadeEnsino(getControleCobrancaBancoVO().getNomeArquivo_Apresentar(), getControleCobrancaBancoVO().getUnidadeEnsinoVO().getCodigo())).equals(SituacaoArquivoRetorno.ARQUIVO_PROCESSADO.getValor())) {
			limparFiltroLista();
			getControleConsulta().setPaginaAtual(1);
			getControleConsulta().setTotalRegistrosEncontrados(getFacadeFactory().getContaReceberRegistroArquivoFacade().consultarQtdeContaReceberRegistroArquivoPorRegistroArquivo(getControleCobrancaBancoVO().getRegistroArquivoVO().getCodigo(), "", "", "", false, getUsuarioLogado(), false));
			setContaReceberRegistroArquivoVOs(getControleCobrancaBancoVO().getRegistroArquivoVO().getContaReceberRegistroArquivoVOs());
			setMapaPendenciasControleCobrancaVOs(getFacadeFactory().getMapaPendenciasControleCobrancaFacade().consultarPorContaReceberRegistroArquivoSelecionado(getControleCobrancaBancoVO().getRegistroArquivoVO().getCodigo(), getControleConsulta().getLimitePorPagina2(), getControleConsulta().getOffset2(), null, getUsuarioLogado()));
			consultarTotalPendenciasProcessamentoArquivoRetorno();
			setJobBaixarContasArquivoRetorno(null);
			setArquivoProcessado(true);
			setAtivarPush(false);
//			setListener(null);
			throw new ConsistirException(SituacaoArquivoRetorno.ARQUIVO_PROCESSADO.getDescricao());
		}
		if (Uteis.ARQUIVOS_CONTROLE_COBRANCA.get(Uteis.getNomeArquivoComUnidadeEnsino(getControleCobrancaBancoVO().getNomeArquivo_Apresentar(), getControleCobrancaBancoVO().getUnidadeEnsinoVO().getCodigo())).equals(SituacaoArquivoRetorno.CONTAS_BAIXADAS.getValor())) {
			limparFiltroLista();
			getControleConsulta().setPaginaAtual(1);
			setContaReceberRegistroArquivoVOs(getFacadeFactory().getContaReceberRegistroArquivoFacade().consultaRapidaPorRegistroArquivo(getControleCobrancaBancoVO().getRegistroArquivoVO().getCodigo(), getControleConsulta().getLimitePorPagina(), getControleConsulta().getOffset(), "", "", "", false, getUsuarioLogado(), null));
			getControleConsulta().setTotalRegistrosEncontrados(getFacadeFactory().getContaReceberRegistroArquivoFacade().consultarQtdeContaReceberRegistroArquivoPorRegistroArquivo(getControleCobrancaBancoVO().getRegistroArquivoVO().getCodigo(), "", "", "", false, getUsuarioLogado(), false));
			setMapaPendenciasControleCobrancaVOs(getFacadeFactory().getMapaPendenciasControleCobrancaFacade().consultarPorContaReceberRegistroArquivoSelecionado(getControleCobrancaBancoVO().getRegistroArquivoVO().getCodigo(), getControleConsulta().getLimitePorPagina2(), getControleConsulta().getOffset2(), null, getUsuarioLogado()));
			consultarTotalPendenciasProcessamentoArquivoRetorno();
			getControleConsulta().setTotalRegistrosEncontrados2(getFacadeFactory().getMapaPendenciasControleCobrancaFacade().consultarQtdeMapaPendenciaPorRegistroArquivo(getControleCobrancaBancoVO().getRegistroArquivoVO().getCodigo(), getUsuarioLogado()));
			getControleCobrancaBancoVO().getRegistroArquivoVO().setContasBaixadas(Boolean.TRUE);
			setAtivarPush(false);
//			setListener(null);
			String msg = SituacaoArquivoRetorno.CONTAS_BAIXADAS.getDescricao();
			if (getJobBaixarContasArquivoRetorno() != null) {
				msg = getJobBaixarContasArquivoRetorno().getSituacao();
			}
			setJobBaixarContasArquivoRetorno(null);
			throw new ConsistirException(msg);
		}
		
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto
	 * da classe <code>ControleCobranca</code>. Caso o objeto seja novo (ainda
	 * não gravado no BD) é acionado a operação <code>incluir()</code>. Caso
	 * contrário é acionado o <code>alterar()</code>. Se houver alguma
	 * inconsistência o objeto não é gravado, sendo re-apresentado para o
	 * usuário juntamente com uma mensagem de erro.
	 */
	// public void gravar() {
	// try {
	// if (controleCobrancaVO.isNovoObj().booleanValue()) {
	// getFacadeFactory().getControleCobrancaFacade().incluir(controleCobrancaVO,
	// getContaReceberVOs(), getRegistroArquivo(),
	// getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
	// } else {
	// getFacadeFactory().getControleCobrancaFacade().alterar(controleCobrancaVO);
	// }
	// setMapaPendenciasControleCobrancaVOs(getFacadeFactory().getMapaPendenciasControleCobrancaFacade().consultarPorListaContaReceber(
	// getContaReceberVOs(), getConfiguracaoFinanceiroPadraoSistema(),
	// getUsuarioLogado()));
	// setMensagemID("msg_dados_gravados");
	// } catch (Exception e) {
	// setMensagemDetalhada("msg_erro", e.getMessage());
	// }
	// }
	public void gravarBaixandoContas() throws Exception {
		try {
			getFacadeFactory().getControleCobrancaFacade().realizarAtualizacaoDadosProcessamento(getControleCobrancaBancoVO());
			if (Uteis.isAtributoPreenchido(getControleCobrancaVO().getContaCorrenteVO().getCodigo())) {
				getControleCobrancaBancoVO().setContaCorrenteVO(getControleCobrancaVO().getContaCorrenteVO());
			} else {
				throw new Exception("O campo CONTA CORRENTE deve ser informado.");
			}
			getControleCobrancaBancoVO().setResponsavel(getUsuarioLogadoClone());
			if(getControleCobrancaBancoVO().getSituacaoProcessamento().equals(SituacaoProcessamentoArquivoRetornoEnum.EM_PROCESSAMENTO)){
				setAtivarPush(true);
				throw new ConsistirException("Arquivo já está sendo processado.");
			}
			if (!Uteis.ARQUIVOS_CONTROLE_COBRANCA.isEmpty() && Uteis.ARQUIVOS_CONTROLE_COBRANCA.get(Uteis.getNomeArquivoComUnidadeEnsino(getControleCobrancaBancoVO().getNomeArquivo_Apresentar(), getControleCobrancaBancoVO().getUnidadeEnsinoVO().getCodigo())).equals(SituacaoArquivoRetorno.BAIXANDO_CONTAS.getValor())) {
				setAtivarPush(true);
				throw new ConsistirException(getJobBaixarContasArquivoRetorno().getSituacao());
			} else {
				if (!Uteis.ARQUIVOS_CONTROLE_COBRANCA.isEmpty() && Uteis.ARQUIVOS_CONTROLE_COBRANCA.get(Uteis.getNomeArquivoComUnidadeEnsino(getControleCobrancaBancoVO().getNomeArquivo_Apresentar(), getControleCobrancaBancoVO().getUnidadeEnsinoVO().getCodigo())).equals(SituacaoArquivoRetorno.CONTAS_BAIXADAS.getValor())) {
					getControleCobrancaBancoVO().getRegistroArquivoVO().setContasBaixadas(true);
				}
				if ((!Uteis.ARQUIVOS_CONTROLE_COBRANCA.isEmpty() && !Uteis.ARQUIVOS_CONTROLE_COBRANCA.get(Uteis.getNomeArquivoComUnidadeEnsino(getControleCobrancaBancoVO().getNomeArquivo_Apresentar(), getControleCobrancaBancoVO().getUnidadeEnsinoVO().getCodigo())).equals(SituacaoArquivoRetorno.CONTAS_BAIXADAS.getValor())) || !getControleCobrancaBancoVO().getRegistroArquivoVO().getContasBaixadas()) {
					if (!Uteis.isAtributoPreenchido(getControleCobrancaBancoVO().getResponsavel())) {
						inserirDadosResponsavelControleCobranca();
					}
					setJobBaixarContasArquivoRetorno(new JobBaixarContasArquivoRetorno(getControleCobrancaBancoVO(), getConfiguracaoFinanceiroPadraoSistema(), getControleCobrancaBancoVO().getResponsavel()));
					Thread jobBaixarContasArquivoRetornos = new Thread(getJobBaixarContasArquivoRetorno());
					jobBaixarContasArquivoRetornos.start();
					Uteis.ARQUIVOS_CONTROLE_COBRANCA.put(Uteis.getNomeArquivoComUnidadeEnsino(getControleCobrancaBancoVO().getNomeArquivo_Apresentar(), getControleCobrancaBancoVO().getUnidadeEnsinoVO().getCodigo()), SituacaoArquivoRetorno.BAIXANDO_CONTAS.getValor());
					setAtivarPush(true);
					getControleCobrancaBancoVO().setSituacaoProcessamento(SituacaoProcessamentoArquivoRetornoEnum.EM_PROCESSAMENTO);
					contabilizarTotais();
					throw new ConsistirException(SituacaoArquivoRetorno.BAIXANDO_CONTAS.getDescricao());
				} else {
					limparFiltroLista();
					getControleConsulta().setPaginaAtual(1);
					setContaReceberRegistroArquivoVOs(getFacadeFactory().getContaReceberRegistroArquivoFacade().consultaRapidaPorRegistroArquivo(getControleCobrancaBancoVO().getRegistroArquivoVO().getCodigo(), getControleConsulta().getLimitePorPagina(), getControleConsulta().getOffset(), "", "", "", false, getUsuarioLogado(), null));
					getControleConsulta().setTotalRegistrosEncontrados(getFacadeFactory().getContaReceberRegistroArquivoFacade().consultarQtdeContaReceberRegistroArquivoPorRegistroArquivo(getControleCobrancaBancoVO().getRegistroArquivoVO().getCodigo(), "", "", "", false, getUsuarioLogado(), false));
					setMapaPendenciasControleCobrancaVOs(getFacadeFactory().getMapaPendenciasControleCobrancaFacade().consultarPorContaReceberRegistroArquivoSelecionado(getControleCobrancaBancoVO().getRegistroArquivoVO().getCodigo(), getControleConsulta().getLimitePorPagina2(), getControleConsulta().getOffset2(), null, getUsuarioLogado()));
					getControleConsulta().setTotalRegistrosEncontrados2(getFacadeFactory().getMapaPendenciasControleCobrancaFacade().consultarQtdeMapaPendenciaPorRegistroArquivo(getControleCobrancaBancoVO().getRegistroArquivoVO().getCodigo(), getUsuarioLogado()));
					setAtivarPush(false);
					contabilizarContaDuplicidade();
					contabilizarTotais();
					throw new ConsistirException(SituacaoArquivoRetorno.CONTAS_BAIXADAS.getDescricao());
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String processarArquivo() {
		try {
			if (getConfiguracaoFinanceiroPadraoSistema().getObrigatorioSelecionarUnidadeEnsinoControleCobranca() && getControleCobrancaVO().getUnidadeEnsinoVO().getCodigo().equals(0)) {
				throw new Exception("Para processar o arquivo deve-se informar a unidade de ensino");
			}
			limparFiltroLista();
			ControleCobrancaVO.validarDados(getControleCobrancaVO());			
			// consultarArquivo();
			if (!getControleCobrancaBancoVO().getCodigo().equals(0) 
					&& Uteis.ARQUIVOS_CONTROLE_COBRANCA.containsKey(Uteis.getNomeArquivoComUnidadeEnsino(getControleCobrancaVO().getNomeArquivo_Apresentar(), getControleCobrancaVO().getUnidadeEnsinoVO().getCodigo()))
					&& !getControleCobrancaBancoVO().getSituacaoProcessamento().equals(SituacaoProcessamentoArquivoRetornoEnum.AGUARDANDO_PROCESSAMENTO)) {
				if (!getControleCobrancaBancoVO().getCodigo().equals(0)) {
					getControleCobrancaBancoVO().setRegistroArquivoVO(getFacadeFactory().getRegistroArquivoFacade().consultarPorControleCobranca(getControleCobrancaBancoVO().getCodigo(), "", Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
					if (!getControleCobrancaBancoVO().getRegistroArquivoVO().getCodigo().equals(0)) {
						if (getControleCobrancaBancoVO().getRegistroArquivoVO().getArquivoProcessado()) {
							setControleCobrancaBancoVO(getFacadeFactory().getControleCobrancaFacade().consultarPorNomeArquivoAnoProcessamentoUnidadeEnsino(getControleCobrancaVO().getNomeArquivo_Apresentar(), Uteis.getAnoDataAtual(), getControleCobrancaVO().getUnidadeEnsinoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
							getControleCobrancaBancoVO().setRegistroArquivoVO(getFacadeFactory().getRegistroArquivoFacade().consultarPorControleCobranca(getControleCobrancaBancoVO().getCodigo(), null, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
							getControleConsulta().setTotalRegistrosEncontrados(getFacadeFactory().getContaReceberRegistroArquivoFacade().consultarQtdeContaReceberRegistroArquivoPorRegistroArquivo(getControleCobrancaBancoVO().getRegistroArquivoVO().getCodigo(), "", "", "",  false, getUsuarioLogado(), false));
							setContaReceberRegistroArquivoVOs(getControleCobrancaBancoVO().getRegistroArquivoVO().getContaReceberRegistroArquivoVOs());
							setArquivoProcessado(true);
							if (Uteis.ARQUIVOS_CONTROLE_COBRANCA.get(Uteis.getNomeArquivoComUnidadeEnsino(getControleCobrancaBancoVO().getNomeArquivo_Apresentar(), getControleCobrancaBancoVO().getUnidadeEnsinoVO().getCodigo())).equals(SituacaoArquivoRetorno.CONTAS_BAIXADAS.getValor())) {
								setMapaPendenciasControleCobrancaVOs(getFacadeFactory().getMapaPendenciasControleCobrancaFacade().consultarPorContaReceberRegistroArquivoSelecionado(getControleCobrancaBancoVO().getRegistroArquivoVO().getCodigo(), getControleConsulta().getLimitePorPagina2(), getControleConsulta().getOffset2(), null, getUsuarioLogado()));
								getControleConsulta().setTotalRegistrosEncontrados2(getFacadeFactory().getMapaPendenciasControleCobrancaFacade().consultarQtdeMapaPendenciaPorRegistroArquivo(getControleCobrancaBancoVO().getRegistroArquivoVO().getCodigo(), getUsuarioLogado()));
								setAtivarPush(false);
								throw new ConsistirException(SituacaoArquivoRetorno.CONTAS_BAIXADAS.getDescricao());
							}
							setAtivarPush(false);
							throw new ConsistirException(SituacaoArquivoRetorno.ARQUIVO_PROCESSADO.getDescricao());
						} else {
							setArquivoProcessado(false);
							setAtivarPush(true);
							throw new ConsistirException(SituacaoArquivoRetorno.PROCESSANDO_ARQUIVO.getDescricao());
						}
					}
				} else {
					if (Uteis.ARQUIVOS_CONTROLE_COBRANCA.get(Uteis.getNomeArquivoComUnidadeEnsino(getControleCobrancaVO().getNomeArquivo_Apresentar(), getControleCobrancaVO().getUnidadeEnsinoVO().getCodigo())).equals(SituacaoArquivoRetorno.PROCESSANDO_ARQUIVO.getValor())) {
						setArquivoProcessado(false);
						setAtivarPush(true);
						throw new ConsistirException(SituacaoArquivoRetorno.PROCESSANDO_ARQUIVO.getDescricao());
					}
				}
			} else {
				ExecutorService executor = Executors.newSingleThreadExecutor();
				Uteis.ARQUIVOS_CONTROLE_COBRANCA.put(Uteis.getNomeArquivoComUnidadeEnsino(getControleCobrancaVO().getNomeArquivo_Apresentar(), getControleCobrancaVO().getUnidadeEnsinoVO().getCodigo()), SituacaoArquivoRetorno.PROCESSANDO_ARQUIVO.getValor());
				getControleCobrancaBancoVO().setNomeArquivo(getControleCobrancaVO().getNomeArquivo());				
				setControleCobrancaBancoVO((ControleCobrancaVO) executor.submit(new JobProcessarArquivoRetorno(getControleCobrancaVO(), getCaminhoPastaArquivosCobranca(), getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado())).get());
				contabilizarTotais();
				contabilizarContaPagarTaxaBancarias();
				contabilizarContaDuplicidade();				
				executor.shutdown();
				executor.awaitTermination(5, TimeUnit.MINUTES);
				if(executor.isTerminated()){	
					if(getControleCobrancaVO().getRegistroArquivoVO().getRegistroDetalheVOs().isEmpty()) {
						setAtivarPush(true);
						executarConsultaAutomatica();
					}else {
						setAtivarPush(false);										
						setProgressBarVO(new ProgressBarVO());
						Uteis.checkState(getControleCobrancaVO().getRegistroArquivoVO().getRegistroDetalheVOs().size() == 0L, "Não foi carregado nenhum registro detalhe do arquivo de retorno");
						getProgressBarVO().resetar();
						getProgressBarVO().setAplicacaoControle(getAplicacaoControle());
						getProgressBarVO().setUsuarioVO(getUsuarioLogadoClone());
						getProgressBarVO().setCaminhoWebRelatorio( getCaminhoPastaArquivosCobranca() +  File.separator + getControleCobrancaVO().getNomeArquivo());
						getProgressBarVO().iniciar(0l, (getControleCobrancaVO().getRegistroArquivoVO().getRegistroDetalheVOs().size()+1), "Iniciando Processamento da(s) operações.", true, this, "processamentoArquivoProgressBar");
						getAplicacaoControle().adicionarMapThreadControleCobranca(getControleCobrancaVO().getCodigo(), getProgressBarVO());
						setOncompleteModal("");	
					}
				}
			}
			contabilizarTotais();
			contabilizarContaPagarTaxaBancarias();
			contabilizarContaDuplicidade();
			if (!Uteis.isAtributoPreenchido(getControleCobrancaVO().getContaCorrenteVO().getCodigo()) && !getControleCobrancaVO().getListaContaCorrenteVOs().isEmpty()) {
				montarListaSelectItemContaCorrente(getControleCobrancaVO().getListaContaCorrenteVOs());
			}
			if (getQtdRegistros() == 0) {
				throw new Exception("Não foi localizado nenhuma conta para baixa, as contas presentes no arquivo são referente a registro/cancelamento/baixa automática/tarifas de boletos!");
			}
		} catch (ConsistirException e) {
			if(!e.getListaMensagemErro().isEmpty()) {
				setMensagemDetalhada("msg_erro", e.getListaMensagemErro().get(0), true);
			}else {
				setMensagemDetalhada("msg_erro", e.getMessage().replaceAll("(.*Exception:)", ""), true);
			}
		} catch (Exception e) {
			if(e.getCause() instanceof ConsistirException) {
				if(!((ConsistirException)e.getCause()).getListaMensagemErro().isEmpty()) {
					setMensagemDetalhada("msg_erro", ((ConsistirException)e.getCause()).getListaMensagemErro().get(0), true);
				}else {
					setMensagemDetalhada("msg_erro", e.getMessage().replaceAll("(.*Exception:)", ""), true);
				}	
			}else {
				setMensagemDetalhada("msg_erro", e.getMessage().replaceAll("(.*Exception:)", ""), true);
			}
			if (!e.getMessage().equals("Arquivo processado e contas não baixadas.")) {
				setArquivoProcessado(false);
				setAtivarPush(false);
			}
		}
		return "";
	}
	
	
	public void processamentoArquivoProgressBar() {
		try {
			JobExecutarArquivoRetornoLocalizarContaReceber jearlc = new JobExecutarArquivoRetornoLocalizarContaReceber(getControleCobrancaVO(), getProgressBarVO(), getProgressBarVO().getUsuarioVO());
			jearlc.executarLocalizacaoContaReceber();
		} catch (Exception e) {
			if(e.getCause() instanceof ConsistirException) {
				if(!((ConsistirException)e.getCause()).getListaMensagemErro().isEmpty()) {
					setMensagemDetalhada("msg_erro", ((ConsistirException)e.getCause()).getListaMensagemErro().get(0), true);
				}else {
					setMensagemDetalhada("msg_erro", e.getMessage().replaceAll("(.*Exception:)", ""), true);
				}	
			}else {
				setMensagemDetalhada("msg_erro", e.getMessage().replaceAll("(.*Exception:)", ""), true);
			}
			if (!e.getMessage().equals("Arquivo processado e contas não baixadas.")) {
				setArquivoProcessado(false);
				setAtivarPush(false);
			}
		} finally{
			getProgressBarVO().incrementarSemStatus();
			getProgressBarVO().setForcarEncerramento(true);
			getAplicacaoControle().removerMapThreadControleCobranca(getControleCobrancaVO().getCodigo());
		}
	}

	public void realizarAtualizacaoControleCobrancaAposProcessamentoProgressBar() {
		try {
			if(getControleCobrancaVO().isEdicaoManual()) {
				getControleCobrancaVO().setRegistroArquivoVO(getFacadeFactory().getRegistroArquivoFacade().consultarPorControleCobranca(getControleCobrancaVO().getCodigo(), null, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			setControleCobrancaBancoVO(getControleCobrancaVO());
			Uteis.ARQUIVOS_CONTROLE_COBRANCA.remove(Uteis.getNomeArquivoComUnidadeEnsino(controleCobrancaVO.getNomeArquivo_Apresentar(), controleCobrancaVO.getUnidadeEnsinoVO().getCodigo()));
			Uteis.ARQUIVOS_CONTROLE_COBRANCA.put(Uteis.getNomeArquivoComUnidadeEnsino(controleCobrancaVO.getNomeArquivo_Apresentar(), controleCobrancaVO.getUnidadeEnsinoVO().getCodigo()), SituacaoArquivoRetorno.ARQUIVO_PROCESSADO.getValor());
			limparFiltroLista();
			getControleConsulta().setPaginaAtual(1);
			getControleConsulta().setPaginaAtual2(1);
			getControleConsulta().setPaginaAtual3(1);
			getControleConsulta().setTotalRegistrosEncontrados(getFacadeFactory().getContaReceberRegistroArquivoFacade().consultarQtdeContaReceberRegistroArquivoPorRegistroArquivo(getControleCobrancaBancoVO().getRegistroArquivoVO().getCodigo(), "", "", "", false, getUsuarioLogado(), false));
			setContaReceberRegistroArquivoVOs(getControleCobrancaBancoVO().getRegistroArquivoVO().getContaReceberRegistroArquivoVOs());
			getControleCobrancaBancoVO().getRegistroArquivoVO().setContaReceberRegistroArquivoVOs(getFacadeFactory().getContaReceberRegistroArquivoFacade().consultaRapidaPorRegistroArquivo(getControleCobrancaBancoVO().getRegistroArquivoVO().getCodigo(), null, 0, "", "", "", false, getUsuarioLogado(), false));
			getControleCobrancaVO().getRegistroArquivoVO().setContaReceberRegistroArquivoVOs(getControleCobrancaBancoVO().getRegistroArquivoVO().getContaReceberRegistroArquivoVOs());
			setContaReceberNegociadaRegistroArquivoVOs(getControleCobrancaBancoVO().getRegistroArquivoVO().getContaReceberNegociadaRegistroArquivoVOs());
			consultarDataScrollerContaReceberDuplicidade();
			setMapaPendenciasControleCobrancaVOs(getFacadeFactory().getMapaPendenciasControleCobrancaFacade().consultarPorContaReceberRegistroArquivoSelecionado(getControleCobrancaBancoVO().getRegistroArquivoVO().getCodigo(), getControleConsulta().getLimitePorPagina2(), getControleConsulta().getOffset2(), null, getUsuarioLogado()));
			getControleConsulta().setTotalRegistrosEncontrados2(getFacadeFactory().getMapaPendenciasControleCobrancaFacade().consultarQtdeMapaPendenciaPorRegistroArquivo(getControleCobrancaBancoVO().getRegistroArquivoVO().getCodigo(), getUsuarioLogado()));
			setArquivoProcessado(getControleCobrancaBancoVO().getRegistroArquivoVO().getArquivoProcessado());
			getControleCobrancaVO().getRegistroArquivoVO().setContasBaixadas(false);
			setAtivarPush(false);
			contabilizarTotais();
			contabilizarContaPagarTaxaBancarias();
			contabilizarContaDuplicidade();
			if(getControleCobrancaVO().getListaContaCorrenteVOs().size() > 1) {
				montarListaSelectItemContaCorrente(getControleCobrancaVO().getListaContaCorrenteVOs());
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		
	}

	public void contabilizarContaPagarTaxaBancarias() {
		if (getControleCobrancaVO().getRegistroArquivoVO().getTotalTaxaBoletoCobradoBanco().doubleValue() > 0) {
			setApresentarModalMensagelContaPagar(Boolean.TRUE);
		}
	}

	public void contabilizarContaDuplicidade() {
		if (!getControleCobrancaVO().getRegistroArquivoVO().getContaReceberRegistroArquivoVOs().isEmpty()) {
			List<ContaReceberRegistroArquivoVO> listaFinal = new ArrayList<ContaReceberRegistroArquivoVO>();
			getContaReceberRegistroArquivoDuplicidadeVOs().clear();
			Iterator i = getControleCobrancaVO().getRegistroArquivoVO().getContaReceberRegistroArquivoVOs().iterator();
			while (i.hasNext()) {
				ContaReceberRegistroArquivoVO conta = (ContaReceberRegistroArquivoVO)i.next();
				if (conta.getContaRecebidaDuplicidade()) {
					getContaReceberRegistroArquivoDuplicidadeVOs().add(conta);
				} else {
					listaFinal.add(conta);
				}
			}
			getControleCobrancaVO().getRegistroArquivoVO().getContaReceberRegistroArquivoVOs().clear();
			getControleCobrancaVO().getRegistroArquivoVO().setContaReceberRegistroArquivoVOs(listaFinal);
			contabilizarTotais();
		}
	}

	public void contabilizarTotais() {
		if (!getControleCobrancaVO().getRegistroArquivoVO().getRegistroDetalheVOs().isEmpty()) {
			setValorTotalRecebido(0.0);
			setQtdRegistros(0);
			Iterator<RegistroDetalheVO> i = getControleCobrancaVO().getRegistroArquivoVO().getRegistroDetalheVOs().iterator();
			while (i.hasNext()) {
				RegistroDetalheVO conta = (RegistroDetalheVO)i.next();
				if (conta.getValorPago().doubleValue() > 0) {
					setValorTotalRecebido(getValorTotalRecebido() + conta.getValorPago());
					setQtdRegistros(getQtdRegistros() + 1);
				}
			}
		}
		if (!getControleCobrancaVO().getRegistroArquivoVO().getContaReceberRegistroArquivoVOs().isEmpty()) {
			getControleCobrancaVO().getRegistroArquivoVO().setContaReceberNaoLocalizadaRegistroArquivoVOs(null);			
			setValorTotalRecebidoNaoLoc(0.0);
			setQtdRegistrosNaoLoc(0);
			Iterator<ContaReceberRegistroArquivoVO> i = getControleCobrancaVO().getRegistroArquivoVO().getContaReceberRegistroArquivoVOs().iterator();
			while (i.hasNext()) {
				ContaReceberRegistroArquivoVO conta = (ContaReceberRegistroArquivoVO)i.next();
				if (conta.getContaReceberVO().getObservacao().equals("Conta Não Localizada!")) {
					getControleCobrancaVO().getRegistroArquivoVO().getContaReceberNaoLocalizadaRegistroArquivoVOs().add(conta);
					try {
						ContaReceberNaoLocalizadaArquivoRetornoVO obj = getFacadeFactory().getContaReceberNaoLocalizadaArquivoRetornoFacade().consultarPorNossoNumeroVinculadoContaReceber(conta.getContaReceberVO().getNossoNumero(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
						if (obj != null) {
							conta.getContaReceberVO().setObservacao("Conta Não Localizada e Baixada Manualmente");
							conta.setContaReceberBaixaManualmente(obj.getContaReceberVO());
							conta.getContaReceberVO().setDataCredito(obj.getContaReceberVO().getDataCredito());
						}
					} catch (Exception e) {
					}
					setValorTotalRecebidoNaoLoc(getValorTotalRecebidoNaoLoc() + conta.getContaReceberVO().getValorRecebido());
					setQtdRegistrosNaoLoc(getQtdRegistrosNaoLoc() + 1);
				}
			}
			getControleCobrancaVO().getRegistroArquivoVO().setContaReceberDataCreditoLocalizadaRegistroArquivoVOs(null);
			getControleCobrancaVO().getRegistroArquivoVO().preencherContaReceberDataCreditoLocalizadaRegistroArquivoVO();;
		}
		if (!getContaReceberRegistroArquivoDuplicidadeVOs().isEmpty()) {
			setValorTotalRecebidoDuplicidade(0.0);
			setQtdRegistrosDuplicidade(0);
			Iterator<ContaReceberRegistroArquivoVO> i = getContaReceberRegistroArquivoDuplicidadeVOs().iterator();
			while (i.hasNext()) {
				ContaReceberRegistroArquivoVO conta = (ContaReceberRegistroArquivoVO)i.next();
				setValorTotalRecebidoDuplicidade(getValorTotalRecebidoDuplicidade() + conta.getContaReceberVO().getValorRecebido());
				setQtdRegistrosDuplicidade(getQtdRegistrosDuplicidade() + 1);
			}
		}
		getControleConsulta().setTotalRegistrosEncontrados(getControleConsulta().getTotalRegistrosEncontrados() - getQtdRegistrosNaoLoc());
		
		setQtdRegistrosConfirmados(getControleCobrancaVO().getRegistroArquivoVO().getRegistroDetalheVOs().stream().filter(p-> p.getSituacaoRegistroDetalheEnum().isSituacaoConfirmado()).count());
		setQtdRegistrosRejeitados(getControleCobrancaVO().getRegistroArquivoVO().getRegistroDetalheVOs().stream().filter(p-> p.getSituacaoRegistroDetalheEnum().isSituacaoRejeitado()).count());
	}
	
	

	public List getListaSelectItemTipoCarteira() {
		List itens = new ArrayList(0);
//		itens.add(new SelectItem("", "CARTEIRA NORMAL"));
		itens.add(new SelectItem("CNAB400", "CNAB400"));
		itens.add(new SelectItem("CNAB240", "CNAB240"));
		return itens;
	}

	public void upLoadArquivo(FileUploadEvent upload) {
		UploadedFile item = upload.getUploadedFile();
		File arquivo;
		BufferedReader reader;
		String linha;
		try {
			getControleCobrancaVO().setArquivo(item.getData());
			getControleCobrancaVO().setNomeArquivo(Uteis.getNomeArquivo(item.getName()));
			
            arquivo = controleCobrancaVO.getArquivo(getCaminhoPastaArquivosCobranca() + File.separator + controleCobrancaVO.getNomeArquivo());            
            reader = new BufferedReader(new FileReader(arquivo));
            while ((linha = reader.readLine()) != null) {
                if (linha.equals("")) {
                    break;
                } else {
                	if (linha.length() < 250 && linha.length() > 200) {
                		this.setTipoCarteira("CNAB240");
                		this.getControleCobrancaVO().setTipoCNAB("CNAB240");
                		this.getControleCobrancaVO().setTipoCarteira("CNAB240");
                		this.getControleCobrancaVO().setBanco((Bancos.getEnum(linha.substring(0, 3))).getCodigo());
                		break;
                	} else if (linha.length() > 250) {
                		this.setTipoCarteira("CNAB400");
                		this.getControleCobrancaVO().setTipoCNAB("CNAB400");
                		this.getControleCobrancaVO().setTipoCarteira("CNAB400");
            			this.getControleCobrancaVO().setBanco((Bancos.getEnum(linha.substring(76, 79))).getCodigo());
                		break;
                	} else {
                		throw new Exception("Não foi possível definir o CNAB (240 ou 400) e/ou o Banco para processamento, verifique o arquivo anexado para processamento!");
                	}
                }
            }			
		} catch (Exception e) {
			setMensagemID("");
			setMensagem("");
			setMensagemDetalhada(e.getMessage());
		} finally {
			upload = null;
			item = null;
			arquivo = null;
			reader = null;
			linha = null;
		}
	}

	public void consultarArquivo() {
		try {
			// setControleCobrancaBancoVO(getFacadeFactory().getControleCobrancaFacade().consultarPorNomeArquivoAnoProcessamento(getControleCobrancaVO().getNomeArquivo_Apresentar(),
			// Uteis.getAnoDataAtual(), Uteis.NIVELMONTARDADOS_COMBOBOX,
			// getUsuarioLogado()));
			setControleCobrancaBancoVO(getFacadeFactory().getControleCobrancaFacade().consultarPorNomeArquivoAnoProcessamentoUnidadeEnsino(getControleCobrancaVO().getNomeArquivo_Apresentar(), Uteis.getAnoDataAtual(), getControleCobrancaVO().getUnidadeEnsinoVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			if (getControleCobrancaBancoVO().getCodigo().equals(0)) {
				getControleCobrancaBancoVO().getUnidadeEnsinoVO().setCodigo(getControleCobrancaVO().getUnidadeEnsinoVO().getCodigo());
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setArquivoProcessado(false);
		}
	}
	
	public void consultarDataScrollerContaReceber() {
		try {
			setContaReceberRegistroArquivoVOs(getFacadeFactory().getContaReceberRegistroArquivoFacade().consultaRapidaPorRegistroArquivo(getControleCobrancaBancoVO().getRegistroArquivoVO().getCodigo(), getControleConsulta().getLimitePorPagina(), getControleConsulta().getOffset(), getPessoa(), getNossoNumero(), getNrDocumento(), false, getUsuarioLogado(), false));
			getControleConsulta().setTotalRegistrosEncontrados(getFacadeFactory().getContaReceberRegistroArquivoFacade().consultarQtdeContaReceberRegistroArquivoPorRegistroArquivo(getControleCobrancaBancoVO().getRegistroArquivoVO().getCodigo(), getPessoa(), getNossoNumero(), getNrDocumento(), false, getUsuarioLogado(), false) - getQtdRegistrosNaoLoc());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setArquivoProcessado(false);
		}
	}

	public void consultarDataScrollerContaReceberDuplicidade() {
		try {
			setContaReceberRegistroArquivoDuplicidadeVOs(getFacadeFactory().getContaReceberRegistroArquivoFacade().consultaRapidaPorRegistroArquivo(getControleCobrancaBancoVO().getRegistroArquivoVO().getCodigo(), getControleConsulta().getLimitePorPagina3(), getControleConsulta().getOffset3(), getPessoaDuplicidade(), getNossoNumeroDuplicidade(), getNrDocumentoDuplicidade(), false, getUsuarioLogado(), true));
			getControleConsulta().setTotalRegistrosEncontrados3(getFacadeFactory().getContaReceberRegistroArquivoFacade().consultarQtdeContaReceberRegistroArquivoPorRegistroArquivo(getControleCobrancaBancoVO().getRegistroArquivoVO().getCodigo(), getPessoaDuplicidade(), getNossoNumeroDuplicidade(), getNrDocumentoDuplicidade(), false, getUsuarioLogado(), true));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setArquivoProcessado(false);
		}
	}

	public void consultarDataScrollerMapaPendencia() {
		try {
			setMapaPendenciasControleCobrancaVOs(getFacadeFactory().getMapaPendenciasControleCobrancaFacade().consultarPorContaReceberRegistroArquivoSelecionado(getControleCobrancaBancoVO().getRegistroArquivoVO().getCodigo(), getControleConsulta().getLimitePorPagina2(), getControleConsulta().getOffset2(), null, getUsuarioLogado()));
			getControleConsulta().setTotalRegistrosEncontrados2(getFacadeFactory().getMapaPendenciasControleCobrancaFacade().consultarQtdeMapaPendenciaPorRegistroArquivo(getControleCobrancaBancoVO().getRegistroArquivoVO().getCodigo(), getUsuarioLogado()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void estornarContas() {
		try {
			getFacadeFactory().getControleCobrancaFacade().estornarConta(getControleCobrancaVO(), getCaminhoPastaArquivosCobranca(), getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}
	
	public void mensagemRelExcel () {
		setMensagemID("msg_relatorio_ok");
	} 
	
	public void imprimirPDF(Boolean mensagem) {
		try {
			setContaReceberRegistroArquivoVOs(getFacadeFactory().getContaReceberRegistroArquivoFacade().consultaRapidaPorRegistroArquivo(getControleCobrancaBancoVO().getRegistroArquivoVO().getCodigo(), null, null, "", "", "", false, getUsuarioLogado(), null));
			getControleCobrancaBancoVO().getRegistroArquivoVO().setRegistroDetalheVOs(getFacadeFactory().getRegistroDetalheFacade().consultarPorRegistroArquivoIdentificacaoRegistroEmpresa("", getControleCobrancaBancoVO().getRegistroArquivoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			executarMetodoControle("ControleCobrancaRelControle", "imprimirPDF", getControleCobrancaBancoVO(), getContaReceberRegistroArquivoVOs(), getContaReceberRegistroArquivoDuplicidadeVOs());
			getControleCobrancaBancoVO().setRegistroArquivoVO(getFacadeFactory().getRegistroArquivoFacade().consultarPorControleCobranca(getControleCobrancaBancoVO().getCodigo(), null, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			getControleConsulta().setTotalRegistrosEncontrados(getFacadeFactory().getContaReceberRegistroArquivoFacade().consultarQtdeContaReceberRegistroArquivoPorRegistroArquivo(getControleCobrancaBancoVO().getRegistroArquivoVO().getCodigo(), "", "", "", false,  getUsuarioLogado(), false));
			getControleCobrancaVO().getRegistroArquivoVO().setContaReceberRegistroArquivoVOs(getContaReceberRegistroArquivoVOs());
			setContaReceberRegistroArquivoDuplicidadeVOs(getFacadeFactory().getContaReceberRegistroArquivoFacade().consultaRapidaPorRegistroArquivoDuplicidade(getControleCobrancaVO().getRegistroArquivoVO().getCodigo(), null, null, false, getUsuarioLogado()));
			setMapaPendenciasControleCobrancaVOs(getFacadeFactory().getMapaPendenciasControleCobrancaFacade().consultarPorContaReceberRegistroArquivoSelecionado(getControleCobrancaBancoVO().getRegistroArquivoVO().getCodigo(), null, null, null, getUsuarioLogado()));
			getControleConsulta().setTotalRegistrosEncontrados2(getFacadeFactory().getMapaPendenciasControleCobrancaFacade().consultarQtdeMapaPendenciaPorRegistroArquivo(getControleCobrancaBancoVO().getRegistroArquivoVO().getCodigo(), getUsuarioLogado()));
			contabilizarTotais();
			if (mensagem) {
				setMensagemID("msg_relatorio_ok");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void imprimirPDF() {
		try {
			imprimirPDF(true);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String getRelatorioControleCobranca() {
		return "abrirPopup2('ControleCobrancaSV?registroArquivo=" + getControleCobrancaVO().getRegistroArquivoVO().getCodigo().intValue() + "')";
	}

	public String selecionarContaReceberVO() {
		ContaReceberVO obj = (ContaReceberVO) context().getExternalContext().getRequestMap().get("contaReceberItens");
		setContaReceberVO(obj);
		return "";
	}

	private void montarListaSelectItemUnidadeEnsino() {
		List resultadoConsulta = null;
		try {
			if (getUnidadeEnsinoLogado().getCodigo().intValue() != 0) {
				setListaSelectItemUnidadeEnsino(new ArrayList<SelectItem>());
				getListaSelectItemUnidadeEnsino().add(new SelectItem(0, ""));
				UnidadeEnsinoVO obj = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCodigo(getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
				getListaSelectItemUnidadeEnsino().add(new SelectItem(obj.getCodigo(), obj.getNome()));
				// setContaReceberVO(new ContaReceberVO());
				getContaReceberVO().setUnidadeEnsino(obj);
				return;
			}
			resultadoConsulta = consultarUnidadeEnsinoPorNome("");
			setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
		} catch (Exception e) {
			// System.out.println(e.getMessage());
		}
	}

	public List consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
		List lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, 0, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		return lista;
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP
	 * ControleCobrancaCons.jsp. Define o tipo de consulta a ser executada, por
	 * meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP.
	 * Como resultado, disponibiliza um List com os objetos selecionados na
	 * sessao da pagina.
	 */
	@Override
	public String consultar() {
		try {
			super.consultar();
			List objs = new ArrayList(0);
			if (getControleConsulta().getCampoConsulta().equals("codigo")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getControleCobrancaFacade().consultaRapidaPorCodigo(false, new Integer(valorInt), getUnidadeEnsinoLogado().getCodigo(), true, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("dataProcessamento")) {
				objs = getFacadeFactory().getControleCobrancaFacade().consultaRapidaPorDataProcessamento(false, getControleConsulta().getDataIni(), getControleConsulta().getDataFim(), getUnidadeEnsinoLogado().getCodigo(), true, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nossoNumero")) {
				if(getControleConsulta().getValorConsulta().trim().isEmpty() && getControleConsulta().getValorConsulta().trim().length() < 2) {
					throw new Exception("msg_ParametroConsulta_vazio");
				}
				objs = getFacadeFactory().getControleCobrancaFacade().consultaRapidaPorNossoNumero(getControleConsulta().getValorConsulta(), false, getUnidadeEnsinoLogado().getCodigo(), true, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("contaCorrente")) {
				objs = getFacadeFactory().getControleCobrancaFacade().consultaRapidaPorContaCorrenteDataProcessamento(this.getContaCorrente(), false, getControleConsulta().getDataIni(), getControleConsulta().getDataFim(), getUnidadeEnsinoLogado().getCodigo(), true, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeSacado")) {
				if(getControleConsulta().getValorConsulta().trim().isEmpty() && getControleConsulta().getValorConsulta().trim().length() < 2) {
					throw new Exception("msg_ParametroConsulta_vazio");
				}
				objs = getFacadeFactory().getControleCobrancaFacade().consultaRapidaPorSacado(getControleConsulta().getValorConsulta(), false,  getControleConsulta().getDataIni(), getControleConsulta().getDataFim(), getUnidadeEnsinoLogado().getCodigo(), true, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("matricula")) {
				if(getControleConsulta().getValorConsulta().trim().isEmpty() && getControleConsulta().getValorConsulta().trim().length() < 2) {
					throw new Exception("msg_ParametroConsulta_vazio");
				}
				objs = getFacadeFactory().getControleCobrancaFacade().consultaRapidaPorMatricula(getControleConsulta().getValorConsulta(), false,  getControleConsulta().getDataIni(), getControleConsulta().getDataFim(), getUnidadeEnsinoLogado().getCodigo(), true, getUsuarioLogado());
			}
			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("controleCobrancaCons.xhtml");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("controleCobrancaCons.xhtml");
		}
	}

	public void limparCampoConsulta() {
		getControleConsulta().setValorConsulta("");
		getControleConsulta().setDataIni(Uteis.getDataPrimeiroDiaMes(new Date()));
		getControleConsulta().setDataFim(Uteis.getDataUltimoDiaMes(new Date()));
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe
	 * <code>ControleCobrancaVO</code> Após a exclusão ela automaticamente
	 * aciona a rotina para uma nova inclusão.
	 */
	public String excluir() {
		try {
			getFacadeFactory().getControleCobrancaFacade().excluir(getControleCobrancaBancoVO(), getUsuarioLogado());
			novo();
			setMensagemID("msg_dados_excluidos");
			return Uteis.getCaminhoRedirecionamentoNavegacao("controleCobrancaForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("controleCobrancaForm.xhtml");
		}
	}

	public void criarContasReceberBaseadasNasPendencias() {
		try {
			ConfiguracaoFinanceiroVO config = getConfiguracaoFinanceiroPadraoSistema();
			ContaCorrenteVO contaCorrente = new ContaCorrenteVO();
			setMapaPendenciasControleCobrancaVOs(getFacadeFactory().getMapaPendenciasControleCobrancaFacade().consultarPorContaReceberRegistroArquivoSelecionado(getControleCobrancaBancoVO().getRegistroArquivoVO().getCodigo(), null, null, true, getUsuarioLogado()));
			for (MapaPendenciasControleCobrancaVO mpccVO : getMapaPendenciasControleCobrancaVOs()) {
				if (mpccVO.getSelecionado()) {
					Integer codOrigem = 0;
					if (!mpccVO.getContaReceber().getCodOrigem().equals("")) {
						codOrigem = Integer.valueOf(mpccVO.getContaReceber().getCodOrigem());
					}
					Double valorDiferenca = mpccVO.getValorDiferenca(); // mpccVO.getContaReceber().getValor()
																		// -
																		// mpccVO.getContaReceber().getValorRecebido()
																		// -
																		// mpccVO.getContaReceber().getValorDescontoRecebido();
					contaCorrente = getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(config.getContaCorrentePadraoMensalidade(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
					PessoaVO pessoaVO = mpccVO.getContaReceber().getPessoa();
					if (mpccVO.getContaReceber().getTipoPessoa().equals(TipoPessoa.RESPONSAVEL_FINANCEIRO.getValor())) {
						pessoaVO = mpccVO.getContaReceber().getResponsavelFinanceiro();
						pessoaVO.setAluno(false);
						pessoaVO.setFuncionario(false);
						pessoaVO.setProfessor(false);
						pessoaVO.setCandidato(false);
						pessoaVO.setMembroComunidade(false);
						pessoaVO.setPossuiAcessoVisaoPais(true);
					}
					getFacadeFactory().getContaReceberFacade().criarContaReceber(mpccVO.getContaReceber().getMatriculaAluno(), mpccVO.getContaReceber().getParceiroVO(), pessoaVO, mpccVO.getMatricula().getUnidadeEnsino(), mpccVO.getMatricula().getUnidadeEnsino(), contaCorrente, codOrigem, TipoOrigemContaReceber.OUTROS.getValor(), mpccVO.getContaReceber().getDataVencimento(), mpccVO.getContaReceber().getDataVencimento(), valorDiferenca, config.getCentroReceitaMensalidadePadrao().getCodigo(), 1, 1, TipoBoletoBancario.OUTROS.getValor(), "PE" + mpccVO.getContaReceber().getParcela(), getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado(), mpccVO.getContaReceber().getFornecedor(), 0, "", null);
					getFacadeFactory().getMapaPendenciasControleCobrancaFacade().excluir(mpccVO, getUsuarioLogado());
				}
			}
			setMapaPendenciasControleCobrancaVOs(getFacadeFactory().getMapaPendenciasControleCobrancaFacade().consultarPorContaReceberRegistroArquivoSelecionado(getControleCobrancaBancoVO().getRegistroArquivoVO().getCodigo(), getControleConsulta().getLimitePorPagina2(), 0, null, getUsuarioLogado()));
			getControleConsulta().setTotalRegistrosEncontrados2(getFacadeFactory().getMapaPendenciasControleCobrancaFacade().consultarQtdeMapaPendenciaPorRegistroArquivo(getControleCobrancaBancoVO().getRegistroArquivoVO().getCodigo(), getUsuarioLogado()));
			contaCorrente = null;
			config = null;
			setMensagemID("msg_contareCeber_contasCriadas");
		} catch (Exception e) {
			try {
				setMapaPendenciasControleCobrancaVOs(getFacadeFactory().getMapaPendenciasControleCobrancaFacade().consultarPorContaReceberRegistroArquivoSelecionado(getControleCobrancaBancoVO().getRegistroArquivoVO().getCodigo(), null, null, null, getUsuarioLogado()));
			} catch (Exception ex) {
				if (!ex.getMessage().equals("")) {
					setMensagemDetalhada("msg_erro", ex.getMessage());
				}
			}
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void isentarContasReceberBaseadasNasPendencias() {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidade("ContaReceber_IsentarContas", getUsuarioLogado());
			setMapaPendenciasControleCobrancaVOs(getFacadeFactory().getMapaPendenciasControleCobrancaFacade().consultarPorContaReceberRegistroArquivoSelecionado(getControleCobrancaBancoVO().getRegistroArquivoVO().getCodigo(), null, null, true, getUsuarioLogado()));
			for (MapaPendenciasControleCobrancaVO mpccVO : getMapaPendenciasControleCobrancaVOs()) {
				// if (mpccVO.getSelecionado()) {
				getFacadeFactory().getMapaPendenciasControleCobrancaFacade().excluir(mpccVO, getUsuarioLogado());
				// }
			}
			setMapaPendenciasControleCobrancaVOs(getFacadeFactory().getMapaPendenciasControleCobrancaFacade().consultarPorContaReceberRegistroArquivoSelecionado(getControleCobrancaBancoVO().getRegistroArquivoVO().getCodigo(), getControleConsulta().getLimitePorPagina2(), 0, null, getUsuarioLogado()));
			getControleConsulta().setTotalRegistrosEncontrados2(getFacadeFactory().getMapaPendenciasControleCobrancaFacade().consultarQtdeMapaPendenciaPorRegistroArquivo(getControleCobrancaBancoVO().getRegistroArquivoVO().getCodigo(), getUsuarioLogado()));
			setMensagemID("msg_contareCeber_dadosIsentados");
		} catch (Exception e) {
			try {
				setMapaPendenciasControleCobrancaVOs(getFacadeFactory().getMapaPendenciasControleCobrancaFacade().consultarPorContaReceberRegistroArquivoSelecionado(getControleCobrancaBancoVO().getRegistroArquivoVO().getCodigo(), getControleConsulta().getLimitePorPagina2(), 0, null, getUsuarioLogado()));
				getControleConsulta().setTotalRegistrosEncontrados2(getFacadeFactory().getMapaPendenciasControleCobrancaFacade().consultarQtdeMapaPendenciaPorRegistroArquivo(getControleCobrancaBancoVO().getRegistroArquivoVO().getCodigo(), getUsuarioLogado()));
			} catch (Exception ex) {
				if (!ex.getMessage().equals("")) {
					setMensagemDetalhada("msg_erro", ex.getMessage());
				}
			}
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void alterarMapaPendenciasControleCobrancaSelecionado() {
		try {
			MapaPendenciasControleCobrancaVO obj = (MapaPendenciasControleCobrancaVO) context().getExternalContext().getRequestMap().get("mapaPendenciasItens");
			getFacadeFactory().getMapaPendenciasControleCobrancaFacade().alterarSelecionado(obj.getCodigo(), obj.getSelecionado(), getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Rotina responsável por atribui um javascript com o método de mascara para
	 * campos do tipo Data, CPF, CNPJ, etc.
	 */
	public String getMascaraConsulta() {
		return "";
	}

	public List<SelectItem> getListaSelectItemLayoutsBancos() {
		List<SelectItem> lista = new ArrayList<SelectItem>(0);
		lista = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(Bancos.class, "codigo", "nome", true);
		for (int i = 0; i < lista.size(); i++) {
			if (lista.get(i).getValue().equals(10)) {
				lista.remove(i);
			}
		}
		return lista;
	}


	public boolean getApresentarComboBoxTipoCNAB() {
		return getControleCobrancaVO().getBanco().equals(4) || getControleCobrancaVO().getBanco().equals(7) || getControleCobrancaVO().getBanco().equals(5);
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List getTipoConsultaComboCNAB() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("CNAB400", "CNAB 400"));
		itens.add(new SelectItem("CNAB240", "CNAB 240"));
		return itens;
	}

	public List getTipoConsultaCombo() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("codigo", "Número"));
		itens.add(new SelectItem("contaCorrente", "Conta Corrente"));
		itens.add(new SelectItem("dataProcessamento", "Data de Processamento"));
		itens.add(new SelectItem("nossoNumero", "Nosso Número"));
		itens.add(new SelectItem("nomeSacado", "Nome Sacado"));
		itens.add(new SelectItem("matricula", "Matrícula Aluno"));
		return itens;
	}

	public List getListaSelectItemContaCorrente() {
		if (listaSelectItemContaCorrente == null) {
			listaSelectItemContaCorrente = new ArrayList<>();
		}
		return listaSelectItemContaCorrente;
	}
	
	public void setListaSelectItemContaCorrente(List<SelectItem> listaSelectItemContaCorrente) {
		this.listaSelectItemContaCorrente = listaSelectItemContaCorrente;
	}	
	
    public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
            }
            setListaSelectItemUnidadeEnsino(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }
	

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes
	 * de uma consulta.
	 */
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setListaConsulta(new ArrayList(0));
		getControleConsulta().setDataIni(Uteis.obterDataAntiga(new Date(), 10));
		getControleConsulta().setDataFim(Uteis.getDataUltimoDiaMes(new Date()));
		montarListaSelectItemContaCorrente2();
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("controleCobrancaCons.xhtml");
	}

	/**
	 * Operação que libera todos os recursos (atributos, listas, objetos) do
	 * backing bean. Garantindo uma melhor atuação do Garbage Coletor do Java. A
	 * mesma é automaticamente quando realiza o logout.
	 */
	@Override
	protected void limparRecursosMemoria() {
		super.limparRecursosMemoria();
		controleCobrancaVO = null;
	}

	public ControleCobrancaVO getControleCobrancaVO() {
		if (controleCobrancaVO == null) {
			controleCobrancaVO = new ControleCobrancaVO();
		}
		return controleCobrancaVO;
	}

	public void setControleCobrancaVO(ControleCobrancaVO controleCobrancaVO) {
		this.controleCobrancaVO = controleCobrancaVO;
	}

	public List<ContaReceberVO> getContaReceberVOs() {
		if (contaReceberVOs == null) {
			contaReceberVOs = new ArrayList<ContaReceberVO>(0);
		}
		return contaReceberVOs;
	}

	public void setContaReceberVOs(List<ContaReceberVO> contaReceberVOs) {
		this.contaReceberVOs = contaReceberVOs;
	}

	public boolean getArquivoProcessado() {
		return arquivoProcessado;
	}

	public void setArquivoProcessado(boolean arquivoProcessado) {
		this.arquivoProcessado = arquivoProcessado;
	}

	public void setMapaPendenciasControleCobrancaVOs(List<MapaPendenciasControleCobrancaVO> mapaPendenciasControleCobrancaVOs) {
		this.mapaPendenciasControleCobrancaVOs = mapaPendenciasControleCobrancaVOs;
	}

	public List<MapaPendenciasControleCobrancaVO> getMapaPendenciasControleCobrancaVOs() {
		if (mapaPendenciasControleCobrancaVOs == null) {
			mapaPendenciasControleCobrancaVOs = new ArrayList<MapaPendenciasControleCobrancaVO>(0);
		}
		return mapaPendenciasControleCobrancaVOs;
	}

	public void setMarcarTodos(Boolean marcarTodos) {
		this.marcarTodos = marcarTodos;
	}

	public Boolean getMarcarTodos() {
		return marcarTodos;
	}

	public void selecionarTodos() {
		try {
			if (getMarcarTodos() == true) {
				for (MapaPendenciasControleCobrancaVO mapaPendenciasControleCobrancaVO : getMapaPendenciasControleCobrancaVOs()) {
					mapaPendenciasControleCobrancaVO.setSelecionado(true);
				}
				getFacadeFactory().getMapaPendenciasControleCobrancaFacade().alterarSelecionadoPorRegistroArquivo(getControleCobrancaBancoVO().getRegistroArquivoVO().getCodigo(), true, getUsuarioLogado());
			} else {
				for (MapaPendenciasControleCobrancaVO mapaPendenciasControleCobrancaVO : getMapaPendenciasControleCobrancaVOs()) {
					mapaPendenciasControleCobrancaVO.setSelecionado(false);
				}
				getFacadeFactory().getMapaPendenciasControleCobrancaFacade().alterarSelecionadoPorRegistroArquivo(getControleCobrancaBancoVO().getRegistroArquivoVO().getCodigo(), false, getUsuarioLogado());
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<ContaReceberRegistroArquivoVO> getContaReceberRegistroArquivoVOs() {
		if (contaReceberRegistroArquivoVOs == null) {
			contaReceberRegistroArquivoVOs = new ArrayList<ContaReceberRegistroArquivoVO>(0);
		}
		return contaReceberRegistroArquivoVOs;
	}

	public void setContaReceberRegistroArquivoVOs(List<ContaReceberRegistroArquivoVO> contaReceberRegistroArquivoVOs) {
		this.contaReceberRegistroArquivoVOs = contaReceberRegistroArquivoVOs;
	}

	public List<ContaReceberRegistroArquivoVO> getContaReceberNegociadaRegistroArquivoVOs() {
		if (contaReceberNegociadaRegistroArquivoVOs == null) {
			contaReceberNegociadaRegistroArquivoVOs = new ArrayList<ContaReceberRegistroArquivoVO>(0);
		}
		return contaReceberNegociadaRegistroArquivoVOs;
	}
	
	public void setContaReceberNegociadaRegistroArquivoVOs(List<ContaReceberRegistroArquivoVO> contaReceberNegociadaRegistroArquivoVOs) {
		this.contaReceberNegociadaRegistroArquivoVOs = contaReceberNegociadaRegistroArquivoVOs;
	}

	public ContaReceberVO getContaReceberVO() {
		return contaReceberVO;
	}

	public void setContaReceberVO(ContaReceberVO contaReceberVO) {
		this.contaReceberVO = contaReceberVO;
	}

	public boolean getIsApresentarResultadoContaReceberRegistroArquivoDuplicidade() {
		if (getContaReceberRegistroArquivoDuplicidadeVOs() == null || getContaReceberRegistroArquivoDuplicidadeVOs().isEmpty()) {
			return false;
		}
		return true;
	}

	public boolean getIsApresentarResultadoContaReceberNaoLocalizadoRegistroArquivo() {
		if (getControleCobrancaVO().getRegistroArquivoVO().getContaReceberNaoLocalizadaRegistroArquivoVOs() == null || getControleCobrancaVO().getRegistroArquivoVO().getContaReceberNaoLocalizadaRegistroArquivoVOs().isEmpty()) {
			return false;
		}
		return true;
	}
	
	public boolean getIsApresentarResultadoContaReceberRegistroArquivo() {
		if (getContaReceberRegistroArquivoVOs() == null || getContaReceberRegistroArquivoVOs().isEmpty()) {
			return false;
		}
		return true;
	}

	public boolean getIsApresentarResultadoMapaPendencia() {
		if (getMapaPendenciasControleCobrancaVOs() == null || getMapaPendenciasControleCobrancaVOs().isEmpty()) {
			return false;
		}
		return true;
	}

	public ControleCobrancaVO getControleCobrancaBancoVO() {
		if (controleCobrancaBancoVO == null) {
			controleCobrancaBancoVO = new ControleCobrancaVO();
		}
		return controleCobrancaBancoVO;
	}

	public void setControleCobrancaBancoVO(ControleCobrancaVO controleCobrancaBancoVO) {
		this.controleCobrancaBancoVO = controleCobrancaBancoVO;
	}

	public boolean getIsArquivoSelecionado() {
		if ((getControleCobrancaVO().getNomeArquivo() != null 
				&& !getControleCobrancaVO().getNomeArquivo().equals("")) 
				|| (getControleCobrancaBancoVO().getNomeArquivo() != null 
				&& !getControleCobrancaBancoVO().getNomeArquivo().equals(""))) {
			return true;
		}
		return false;
	}

	public boolean getIsConsultaPorNumeroSelecionado() {
		if (getControleConsulta().getCampoConsulta().equals("codigo")) {
			return true;
		}
		return false;
	}

	public boolean getIsConsultaPorTipoControleSelecionado() {
		if (getControleConsulta().getCampoConsulta().equals("tipoControle")) {
			return true;
		}
		return false;
	}

	public boolean getIsConsultaPorDataProcessamentoSelecionado() {
		if (getControleConsulta().getCampoConsulta().equals("dataProcessamento")) {
			return true;
		}
		return false;
	}

	public boolean getIsConsultaPorContaCorrenteSelecionado() {
		if (getControleConsulta().getCampoConsulta().equals("contaCorrente")) {
			return true;
		}
		return false;
	}

	public boolean getIsArquivoSemBaixaContas() {
		return getArquivoProcessado() && !getControleCobrancaBancoVO().getRegistroArquivoVO().getContasBaixadas();
	}

	public Boolean getAtivarPush() {
		if (ativarPush == null) {
			ativarPush = false;
		}
		return ativarPush;
	}

	public void setAtivarPush(Boolean ativarPush) {
		this.ativarPush = ativarPush;
	}

//	public PushEventListener getListener() {
//		return listener;
//	}
//
//	public void setListener(PushEventListener listener) {
//		this.listener = listener;
//	}

	public JobProcessarArquivoRetorno getJobProcessarArquivoRetorno() {

		return jobProcessarArquivoRetorno;
	}

	public void setJobProcessarArquivoRetorno(JobProcessarArquivoRetorno jobProcessarArquivoRetorno) {
		this.jobProcessarArquivoRetorno = jobProcessarArquivoRetorno;
	}

	public JobBaixarContasArquivoRetorno getJobBaixarContasArquivoRetorno() {
		return jobBaixarContasArquivoRetorno;
	}

	public void setJobBaixarContasArquivoRetorno(JobBaixarContasArquivoRetorno jobBaixarContasArquivoRetorno) {
		this.jobBaixarContasArquivoRetorno = jobBaixarContasArquivoRetorno;
	}

	public List getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList(0);
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public boolean getIsTipoControleArquivoRetorno() {
		return getControleCobrancaVO().getTipoControle().equals("RT");
	}

	/**
	 * @return the tipoCarteira
	 */
	public String getTipoCarteira() {
		if (tipoCarteira == null) {
			tipoCarteira = "CNAB400";
		}
		return tipoCarteira;
	}

	/**
	 * @param tipoCarteira
	 *            the tipoCarteira to set
	 */
	public void setTipoCarteira(String tipoCarteira) {
		this.tipoCarteira = tipoCarteira;
	}
	
	public String navegarParaHistoricoArquivo(){
		return Uteis.getCaminhoRedirecionamentoNavegacao("registroArquivoCons");
	}
	
	public void consultarTotalPendenciasProcessamentoArquivoRetorno() {
		try {
			ConfiguracaoFinanceiroVO config = getConfiguracaoFinanceiroPadraoSistema();
			if (!config.getCriarContaReceberPendenciaArquivoRetornoAutomaticamente()) {
				return;
			}
			Double totalValorDiferenca = 0.0;
			for (MapaPendenciasControleCobrancaVO mpccVO : getMapaPendenciasControleCobrancaVOs()) {
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
	
	public void realizarCriacaoContaReceberPendenciaArquivoRetorno() {
		try {
			ConfiguracaoFinanceiroVO config = getConfiguracaoFinanceiroPadraoSistema();
			ContaCorrenteVO contaCorrente = new ContaCorrenteVO();
			setMapaPendenciasControleCobrancaVOs(getFacadeFactory().getMapaPendenciasControleCobrancaFacade().consultarPorContaReceberRegistroArquivoSelecionado(getControleCobrancaBancoVO().getRegistroArquivoVO().getCodigo(), null, null, false, getUsuarioLogado()));
			for (MapaPendenciasControleCobrancaVO mpccVO : getMapaPendenciasControleCobrancaVOs()) {
				Integer codOrigem = 0;
				if (!mpccVO.getContaReceber().getCodOrigem().equals("")) {
					codOrigem = Integer.valueOf(mpccVO.getContaReceber().getCodOrigem());
				}
				Double valorDiferenca = mpccVO.getValorDiferenca(); // mpccVO.getContaReceber().getValor()
																	// -
																	// mpccVO.getContaReceber().getValorRecebido()
																	// -
																	// mpccVO.getContaReceber().getValorDescontoRecebido();
				contaCorrente = getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(config.getContaCorrentePadraoMensalidade(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				PessoaVO pessoaVO = mpccVO.getContaReceber().getPessoa();
				if (mpccVO.getContaReceber().getTipoPessoa().equals(TipoPessoa.RESPONSAVEL_FINANCEIRO.getValor())) {
					pessoaVO = mpccVO.getContaReceber().getResponsavelFinanceiro();
					pessoaVO.setAluno(false);
					pessoaVO.setFuncionario(false);
					pessoaVO.setProfessor(false);
					pessoaVO.setCandidato(false);
					pessoaVO.setMembroComunidade(false);
					pessoaVO.setPossuiAcessoVisaoPais(true);
				}
				getFacadeFactory().getContaReceberFacade().criarContaReceber(mpccVO.getContaReceber().getMatriculaAluno(), mpccVO.getContaReceber().getParceiroVO(), pessoaVO, mpccVO.getMatricula().getUnidadeEnsino(), mpccVO.getMatricula().getUnidadeEnsino(), contaCorrente, codOrigem, TipoOrigemContaReceber.OUTROS.getValor(), mpccVO.getContaReceber().getDataVencimento(), mpccVO.getContaReceber().getDataVencimento(), valorDiferenca, config.getCentroReceitaMensalidadePadrao().getCodigo(), 1, 1, TipoBoletoBancario.OUTROS.getValor(), "PE" + mpccVO.getContaReceber().getParcela(), getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado(), mpccVO.getContaReceber().getFornecedor(), 0, "", null);
				getFacadeFactory().getMapaPendenciasControleCobrancaFacade().excluir(mpccVO, getUsuarioLogado());
			}
			setMapaPendenciasControleCobrancaVOs(getFacadeFactory().getMapaPendenciasControleCobrancaFacade().consultarPorContaReceberRegistroArquivoSelecionado(getControleCobrancaBancoVO().getRegistroArquivoVO().getCodigo(), getControleConsulta().getLimitePorPagina2(), 0, null, getUsuarioLogado()));
			getControleConsulta().setTotalRegistrosEncontrados2(getFacadeFactory().getMapaPendenciasControleCobrancaFacade().consultarQtdeMapaPendenciaPorRegistroArquivo(getControleCobrancaBancoVO().getRegistroArquivoVO().getCodigo(), getUsuarioLogado()));
			contaCorrente = null;
			config = null;
			setMensagemID("msg_contareCeber_contasCriadas");
		} catch (Exception e) {
			try {
				setMapaPendenciasControleCobrancaVOs(getFacadeFactory().getMapaPendenciasControleCobrancaFacade().consultarPorContaReceberRegistroArquivoSelecionado(getControleCobrancaBancoVO().getRegistroArquivoVO().getCodigo(), null, null, null, getUsuarioLogado()));
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
	
	public String getApresentarModalTotalPendenciaProcessamentoArquivoRetorno(){
		try {
			if (getConfiguracaoFinanceiroPadraoSistema().getCriarContaReceberPendenciaArquivoRetornoAutomaticamente() && getAbrirModalPendenciaArquivoRetorno()) {
				return "RichFaces.$('modalProcessamento').hide();RichFaces.$('panelPendenciaArquivoRetorno').show()";
			}else if(!getAtivarPush()){
				return "RichFaces.$('modalProcessamento').hide();";
			}
		} catch (Exception e) {
			
		}
		return "";
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

	public Boolean getAbrirModalPendenciaArquivoRetorno() {
		if (abrirModalPendenciaArquivoRetorno == null) {
			abrirModalPendenciaArquivoRetorno = Boolean.FALSE;
		}
		return abrirModalPendenciaArquivoRetorno;
	}

	public void setAbrirModalPendenciaArquivoRetorno(Boolean abrirModalPendenciaArquivoRetorno) {
		this.abrirModalPendenciaArquivoRetorno = abrirModalPendenciaArquivoRetorno;
	}
	
	public void desabilitarPush(){
		setAbrirModalPendenciaArquivoRetorno(false);
		setAtivarPush(false);
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

	public Integer getQtdRegistrosNaoLoc() {
		if (qtdRegistrosNaoLoc == null) {
			qtdRegistrosNaoLoc = 0;
		}
		return qtdRegistrosNaoLoc;
	}

	public void setQtdRegistrosNaoLoc(Integer qtdRegistrosNaoLoc) {
		this.qtdRegistrosNaoLoc = qtdRegistrosNaoLoc;
	}

	public Long getQtdRegistrosConfirmados() {
		if (qtdRegistrosConfirmados == null) {
			qtdRegistrosConfirmados = 0L;
		}
		return qtdRegistrosConfirmados;
	}

	public void setQtdRegistrosConfirmados(Long qtdRegistrosConfirmados) {
		this.qtdRegistrosConfirmados = qtdRegistrosConfirmados;
	}

	public Long getQtdRegistrosRejeitados() {
		if (qtdRegistrosRejeitados == null) {
			qtdRegistrosRejeitados = 0L;
		}
		return qtdRegistrosRejeitados;
	}

	public void setQtdRegistrosRejeitados(Long qtdRegistrosRejeitados) {
		this.qtdRegistrosRejeitados = qtdRegistrosRejeitados;
	}

	public Boolean getApresentarModalMensagelContaPagar() {
		if (apresentarModalMensagelContaPagar == null) {
			apresentarModalMensagelContaPagar = Boolean.FALSE;
		}
		return apresentarModalMensagelContaPagar;
	}

	public void setApresentarModalMensagelContaPagar(Boolean apresentarModalMensagelContaPagar) {
		this.apresentarModalMensagelContaPagar = apresentarModalMensagelContaPagar;
	}

	public List<ContaReceberRegistroArquivoVO> getContaReceberRegistroArquivoDuplicidadeVOs() {
		if (contaReceberRegistroArquivoDuplicidadeVOs == null) {
			contaReceberRegistroArquivoDuplicidadeVOs = new ArrayList<ContaReceberRegistroArquivoVO>(0);
		}
		return contaReceberRegistroArquivoDuplicidadeVOs;
	}
	
	public void setContaReceberRegistroArquivoDuplicidadeVOs(List<ContaReceberRegistroArquivoVO> contaReceberRegistroArquivoDuplicidadeVOs) {
		this.contaReceberRegistroArquivoDuplicidadeVOs = contaReceberRegistroArquivoDuplicidadeVOs;
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

	public Integer getQtdRegistrosDuplicidade() {
		if (qtdRegistrosDuplicidade == null) {
			qtdRegistrosDuplicidade = 0;
		}
		return qtdRegistrosDuplicidade;
	}

	public void setQtdRegistrosDuplicidade(Integer qtdRegistrosDuplicidade) {
		this.qtdRegistrosDuplicidade = qtdRegistrosDuplicidade;
	}

	public List<SelectItem> getListaSelectItemContaCorrenteVOs() {
		if (listaSelectItemContaCorrenteVOs == null) {
			listaSelectItemContaCorrenteVOs = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemContaCorrenteVOs;
	}

	public void setListaSelectItemContaCorrenteVOs(List<SelectItem> listaSelectItemContaCorrenteVOs) {
		this.listaSelectItemContaCorrenteVOs = listaSelectItemContaCorrenteVOs;
	}

	public void montarListaSelectItemContaCorrente(List<ContaCorrenteVO> listaContaCorrenteVOs) {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem(0, ""));
		for (ContaCorrenteVO contaCorrenteVO : listaContaCorrenteVOs) {			
			itens.add(new SelectItem(contaCorrenteVO.getCodigo(), contaCorrenteVO.getDescricaoCompletaConta()));			
		}
		setListaSelectItemContaCorrenteVOs(itens);
	}

	public void montarListaSelectItemContaCorrente2() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		try {
			List<ContaCorrenteVO> lista = null;
			lista = getFacadeFactory().getContaCorrenteFacade().consultarContaCorrenteCaixaPorNumeroPorUnidadeEnsino("", new ArrayList<>(), "tipo", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());			
			for (ContaCorrenteVO contaCorrenteVO : lista) {
				itens.add(new SelectItem(contaCorrenteVO.getCodigo(), contaCorrenteVO.getDescricaoCompletaConta()));			
			}
			setListaSelectItemContaCorrente(itens);
		} catch (Exception e) {
			setListaSelectItemContaCorrente(null);
		}
	}

	public Boolean getApresentarModalMensagemContaCorrente() {
		if (apresentarModalMensagemContaCorrente == null) {
			apresentarModalMensagemContaCorrente = false;
		}
		return apresentarModalMensagemContaCorrente;
	}

	public void setApresentarModalMensagemContaCorrente(Boolean apresentarModalMensagemContaCorrente) {
		this.apresentarModalMensagemContaCorrente = apresentarModalMensagemContaCorrente;
	}
	
	public void desabilitarApresentarModalMensagemContaCorrente() {
		setApresentarModalMensagemContaCorrente(false);
		if (!getControleCobrancaVO().getContaCorrenteVO().getCodigo().equals(0)) {
			try {
				getControleCobrancaVO().setContaCorrenteVO(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(getControleCobrancaVO().getContaCorrenteVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
				getFacadeFactory().getControleCobrancaFacade().alterar(getControleCobrancaVO(),getUsuarioLogado());
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			}
		}
	}
	
	public Boolean getApresentarInputContaCorrente() {
		return getControleCobrancaVO().getListaContaCorrenteVOs().isEmpty() || getControleCobrancaVO().getListaContaCorrenteVOs().size() == 1;
	}
	
	public Boolean getApresentarComboBoxContaCorrente() {
		return getControleCobrancaVO().getListaContaCorrenteVOs().size() > 1;
	}
	
	public void realizarDownloadArquivo() {
		try {			
			if(getControleCobrancaVO().getArquivo() == null) {
				return;
			}
			HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
			request.getSession().setAttribute("file", getControleCobrancaVO().getArquivo());
			request.getSession().setAttribute("nomeArquivo", getControleCobrancaVO().getNomeArquivo());
			context().getExternalContext().dispatch("/DownloadSV");
			FacesContext.getCurrentInstance().responseComplete();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public Boolean getValorConsultaNumerico() {
		return getControleConsulta().getCampoConsulta().equals("codigo") || getControleConsulta().getCampoConsulta().equals("nossoNumero");
	}
	
	public void limparFiltroLista() {
		setPessoa("");
		setNrDocumento("");
		setNossoNumero("");
		setPessoaDuplicidade("");
		setNrDocumentoDuplicidade("");
		setNossoNumeroDuplicidade("");
	}

	public String getPessoa() {
		if (pessoa == null) {
			pessoa = "";
		}
		return pessoa;
	}

	public void setPessoa(String pessoa) {
		this.pessoa = pessoa;
	}

	public String getNrDocumento() {
		if (nrDocumento == null) {
			nrDocumento = "";
		}
		return nrDocumento;
	}

	public void setNrDocumento(String nrDocumento) {
		this.nrDocumento = nrDocumento;
	}

	public String getNossoNumero() {
		if (nossoNumero == null) {
			nossoNumero = "";
		}
		return nossoNumero;
	}

	public void setNossoNumero(String nossoNumero) {
		this.nossoNumero = nossoNumero;
	}
	
	public void consultarDataScrollerContaReceberPorFiltro() {
		getControleConsulta().setPaginaAtual(1);
		consultarDataScrollerContaReceber();
	}
	
	public void consultarDataScrollerContaReceberDuplicidadePorFiltro() {
		getControleConsulta().setPaginaAtual3(1);
		consultarDataScrollerContaReceberDuplicidade();
	}

	public String getPessoaDuplicidade() {
		if (pessoaDuplicidade == null) {
			pessoaDuplicidade = "";
		}
		return pessoaDuplicidade;
	}

	public void setPessoaDuplicidade(String pessoaDuplicidade) {
		this.pessoaDuplicidade = pessoaDuplicidade;
	}

	public String getNrDocumentoDuplicidade() {
		if (nrDocumentoDuplicidade == null) {
			nrDocumentoDuplicidade = "";
		}
		return nrDocumentoDuplicidade;
	}

	public void setNrDocumentoDuplicidade(String nrDocumentoDuplicidade) {
		this.nrDocumentoDuplicidade = nrDocumentoDuplicidade;
	}

	public String getNossoNumeroDuplicidade() {
		if (nossoNumeroDuplicidade == null) {
			nossoNumeroDuplicidade = "";
		}
		return nossoNumeroDuplicidade;
	}

	public void setNossoNumeroDuplicidade(String nossoNumeroDuplicidade) {
		this.nossoNumeroDuplicidade = nossoNumeroDuplicidade;
	}
	
	private void inserirDadosResponsavelControleCobranca() {
		getControleCobrancaVO().getResponsavel().setCodigo(getUsuarioLogado().getCodigo());
		getControleCobrancaVO().getResponsavel().setNome(getUsuarioLogado().getNome());
	}

	public Boolean getApresentarBotaoNeogicacao() {
		if (apresentarBotaoNeogicacao == null) {
			apresentarBotaoNeogicacao = Boolean.FALSE;
		}
		return apresentarBotaoNeogicacao;
	}

	public void setApresentarBotaoNeogicacao(Boolean apresentarBotaoNeogicacao) {
		this.apresentarBotaoNeogicacao = apresentarBotaoNeogicacao;
	}

	public ContaReceberVO getContaReceberLogBaixa() {
		if (contaReceberLogBaixa == null) {
			contaReceberLogBaixa = new ContaReceberVO();
		}
		return contaReceberLogBaixa;
	}

	public void setContaReceberLogBaixa(ContaReceberVO contaReceberLogBaixa) {
		this.contaReceberLogBaixa = contaReceberLogBaixa;
	}

	public Integer getContaCorrente() {
		return contaCorrente;
	}

	public void setContaCorrente(Integer contaCorrente) {
		this.contaCorrente = contaCorrente;
	}

	public ContaReceberVO getContaReceberBaixaManual() {
		if (contaReceberBaixaManual == null) {
			contaReceberBaixaManual = new ContaReceberVO();
		}
		return contaReceberBaixaManual;
	}

	public void setContaReceberBaixaManual(ContaReceberVO contaReceberBaixaManual) {
		this.contaReceberBaixaManual = contaReceberBaixaManual;
	}
	
	public Boolean getFazerDownload() {
		if (fazerDownload == null) {
			fazerDownload = false;
		}
		return fazerDownload;
	}

	public void setFazerDownload(Boolean fazerDownload) {
		this.fazerDownload = fazerDownload;
	}

	public String realizarGeracaoRelatorioExcel()  {
		try {
			imprimirPDF(false);
			File arquivo = getFacadeFactory().getControleCobrancaFacade().realizarGeracaoRelatorioExcel(getContaReceberRegistroArquivoVOs(), getContaReceberRegistroArquivoDuplicidadeVOs(), getLogoPadraoRelatorio());
			HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
			request.getSession().setAttribute("nomeArquivo", arquivo.getName());
			request.getSession().setAttribute("pastaBaseArquivo", arquivo.getPath().substring(0, arquivo.getPath().lastIndexOf(File.separator)));
			request.getSession().setAttribute("deletarArquivo", false);
			context().getExternalContext().dispatch("/DownloadSV");
//			setMensagemID("msg_relatorio_ok");
			FacesContext.getCurrentInstance().responseComplete();
			return Uteis.getCaminhoRedirecionamentoNavegacao("controleCobrancaForm");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return "";
		}
		
	}// fim do metodo exp
	
	private ProgressBarVO progressBarVO;
	public ProgressBarVO getProgressBarVO() {
		if(progressBarVO == null) {
			progressBarVO =  new ProgressBarVO();
		}
		return progressBarVO;
	}

	public void setProgressBarVO(ProgressBarVO progressBarVO) {
		this.progressBarVO = progressBarVO;
	}
	
	public void alterarContaCorrente() {
		try {
			if (Uteis.isAtributoPreenchido(getControleCobrancaVO().getContaCorrenteVO().getCodigo())) {
				getFacadeFactory().getControleCobrancaFacade().alterarContaCorrenteControleCobranca(getControleCobrancaVO().getCodigo(), getControleCobrancaVO().getContaCorrenteVO().getCodigo(), getUsuarioLogado());
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
}

