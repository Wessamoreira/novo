package controle.financeiro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.FileUploadEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import jobs.JobBaixarContasArquivoRetornoParceiro;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.financeiro.ParceiroVO;
import negocio.comuns.financeiro.ProcessamentoArquivoRetornoParceiroVO;
import negocio.comuns.financeiro.enumerador.SituacaoProcessamentoArquivoRetornoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.SituacaoArquivoRetorno;

@Controller("ProcessamentoArquivoRetornoParceiroControle")
@Scope("viewScope")
@Lazy
public class ProcessamentoArquivoRetornoParceiroControle extends SuperControle implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1475887310866136580L;
	private ProcessamentoArquivoRetornoParceiroVO processamentoArquivoRetornoParceiroVO;
	private JobBaixarContasArquivoRetornoParceiro jobBaixarContasArquivoRetornoParceiro;
	//private PushEventListener listener;
	private List listaSelectItemUnidadeEnsino;
	private List listaConsultaParceiro;
	private String valorConsultaParceiro;
	private String campoConsultaParceiro;
	private Boolean marcarTodos;
	private Boolean ativarPush;

	public ProcessamentoArquivoRetornoParceiroControle() throws Exception {
		setControleConsulta(new ControleConsulta());
		getControleConsulta().setCampoConsulta("codigo");
		setMensagemID("msg_entre_prmconsulta");
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe
	 * <code>Banco</code> para edição pelo usuário da aplicação.
	 */
	public String novo() {
		try {
			removerObjetoMemoria(this);
			setProcessamentoArquivoRetornoParceiroVO(new ProcessamentoArquivoRetornoParceiroVO());
			getProcessamentoArquivoRetornoParceiroVO().setResponsavel(getUsuarioLogadoClone());
			getProcessamentoArquivoRetornoParceiroVO().setDataGeracao(new Date());
			montarListaSelectItemUnidadeEnsino();
			setMensagemID("msg_entre_dados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("processamentoArquivoRetornoParceiroForm.xhtml");
		
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe
	 * <code>Banco</code> para alteração. O objeto desta classe é
	 * disponibilizado na session da página (request) para que o JSP
	 * correspondente possa disponibilizá-lo para edição.
	 */
	public String editar() {
		try {
			ProcessamentoArquivoRetornoParceiroVO obj = (ProcessamentoArquivoRetornoParceiroVO) context().getExternalContext().getRequestMap().get("processamentoArquivoRetornoParceiroItens");
			setProcessamentoArquivoRetornoParceiroVO(getFacadeFactory().getProcessamentoArquivoRetornoParceiroFacade().consultarPorChavePrimaria(obj.getCodigo(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			if (getProcessamentoArquivoRetornoParceiroVO().getSituacaoProcessamento().isEmProcessamento()) {
				Uteis.ARQUIVOS_CONTROLE_COBRANCA.put(Uteis.getNomeArquivoComUnidadeEnsino(getProcessamentoArquivoRetornoParceiroVO().getNomeArquivo_Apresentar(), getProcessamentoArquivoRetornoParceiroVO().getUnidadeEnsinoVO().getCodigo()),SituacaoArquivoRetorno.BAIXANDO_CONTAS.getValor());
			} else if (getProcessamentoArquivoRetornoParceiroVO().getSituacaoProcessamento().isProcessamentoConcluido()) {
				Uteis.ARQUIVOS_CONTROLE_COBRANCA.put(Uteis.getNomeArquivoComUnidadeEnsino(getProcessamentoArquivoRetornoParceiroVO().getNomeArquivo_Apresentar(), getProcessamentoArquivoRetornoParceiroVO().getUnidadeEnsinoVO().getCodigo()),SituacaoArquivoRetorno.CONTAS_BAIXADAS.getValor());
			}else{
				Uteis.ARQUIVOS_CONTROLE_COBRANCA.put(Uteis.getNomeArquivoComUnidadeEnsino(getProcessamentoArquivoRetornoParceiroVO().getNomeArquivo_Apresentar(), getProcessamentoArquivoRetornoParceiroVO().getUnidadeEnsinoVO().getCodigo()),SituacaoArquivoRetorno.ARQUIVO_PROCESSADO.getValor());
			}
			montarListaSelectItemUnidadeEnsino();
			setMensagemID("msg_dados_editar");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("processamentoArquivoRetornoParceiroForm.xhtml");
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto
	 * da classe <code>Banco</code>. Caso o objeto seja novo (ainda não gravado
	 * no BD) é acionado a operação <code>incluir()</code>. Caso contrário é
	 * acionado o <code>alterar()</code>. Se houver alguma inconsistência o
	 * objeto não é gravado, sendo re-apresentado para o usuário juntamente com
	 * uma mensagem de erro.
	 */
	public String gravar() {
		try {
			getFacadeFactory().getProcessamentoArquivoRetornoParceiroFacade().persistir(getProcessamentoArquivoRetornoParceiroVO(), true, getUsuarioLogado());
			setMensagemID("msg_dados_gravados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("processamentoArquivoRetornoParceiroForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("processamentoArquivoRetornoParceiroForm.xhtml");
		}
	}

	public void gravarBaixandoContas() throws Exception {
		try {
			getFacadeFactory().getProcessamentoArquivoRetornoParceiroFacade().persistir(getProcessamentoArquivoRetornoParceiroVO(), true, getUsuarioLogado());
			if (getProcessamentoArquivoRetornoParceiroVO().getSituacaoProcessamento().isEmProcessamento()) {
				setAtivarPush(true);
				throw new ConsistirException("Arquivo já está sendo processado.");
			}
			if (!Uteis.ARQUIVOS_CONTROLE_COBRANCA.isEmpty()
					&& Uteis.ARQUIVOS_CONTROLE_COBRANCA.containsKey(Uteis.getNomeArquivoComUnidadeEnsino(getProcessamentoArquivoRetornoParceiroVO().getNomeArquivo_Apresentar(), getProcessamentoArquivoRetornoParceiroVO().getUnidadeEnsinoVO().getCodigo()))
					&& Uteis.ARQUIVOS_CONTROLE_COBRANCA.get(Uteis.getNomeArquivoComUnidadeEnsino(getProcessamentoArquivoRetornoParceiroVO().getNomeArquivo_Apresentar(), getProcessamentoArquivoRetornoParceiroVO().getUnidadeEnsinoVO().getCodigo())).equals(SituacaoArquivoRetorno.BAIXANDO_CONTAS.getValor())) {
				setAtivarPush(true);
				throw new ConsistirException(getJobBaixarContasArquivoRetornoParceiro().getSituacao());
			} else {
				if (!Uteis.ARQUIVOS_CONTROLE_COBRANCA.isEmpty() 
						&& Uteis.ARQUIVOS_CONTROLE_COBRANCA.containsKey(Uteis.getNomeArquivoComUnidadeEnsino(getProcessamentoArquivoRetornoParceiroVO().getNomeArquivo_Apresentar(), getProcessamentoArquivoRetornoParceiroVO().getUnidadeEnsinoVO().getCodigo()))
						&& Uteis.ARQUIVOS_CONTROLE_COBRANCA.get(Uteis.getNomeArquivoComUnidadeEnsino(getProcessamentoArquivoRetornoParceiroVO().getNomeArquivo_Apresentar(), getProcessamentoArquivoRetornoParceiroVO().getUnidadeEnsinoVO().getCodigo())).equals(SituacaoArquivoRetorno.CONTAS_BAIXADAS.getValor())) {
					getProcessamentoArquivoRetornoParceiroVO().setContasBaixadas(true);
				}
				if ((!Uteis.ARQUIVOS_CONTROLE_COBRANCA.isEmpty()
						&& Uteis.ARQUIVOS_CONTROLE_COBRANCA.containsKey(Uteis.getNomeArquivoComUnidadeEnsino(getProcessamentoArquivoRetornoParceiroVO().getNomeArquivo_Apresentar(), getProcessamentoArquivoRetornoParceiroVO().getUnidadeEnsinoVO().getCodigo()))
						&& !Uteis.ARQUIVOS_CONTROLE_COBRANCA.get(Uteis.getNomeArquivoComUnidadeEnsino(getProcessamentoArquivoRetornoParceiroVO().getNomeArquivo_Apresentar(), getProcessamentoArquivoRetornoParceiroVO().getUnidadeEnsinoVO().getCodigo())).equals(SituacaoArquivoRetorno.CONTAS_BAIXADAS.getValor())) 
						|| !getProcessamentoArquivoRetornoParceiroVO().getContasBaixadas()) {
					setJobBaixarContasArquivoRetornoParceiro(new JobBaixarContasArquivoRetornoParceiro(getProcessamentoArquivoRetornoParceiroVO(), getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));
					Thread jobBaixarContasArquivoRetornos = new Thread(getJobBaixarContasArquivoRetornoParceiro());
					jobBaixarContasArquivoRetornos.start();
					Uteis.ARQUIVOS_CONTROLE_COBRANCA.put(Uteis.getNomeArquivoComUnidadeEnsino(getProcessamentoArquivoRetornoParceiroVO().getNomeArquivo_Apresentar(), getProcessamentoArquivoRetornoParceiroVO().getUnidadeEnsinoVO().getCodigo()), SituacaoArquivoRetorno.BAIXANDO_CONTAS.getValor());
					setAtivarPush(true);
					getProcessamentoArquivoRetornoParceiroVO().setSituacaoProcessamento(SituacaoProcessamentoArquivoRetornoEnum.EM_PROCESSAMENTO);
					throw new ConsistirException(SituacaoArquivoRetorno.BAIXANDO_CONTAS.getDescricao());
				} else {
					setAtivarPush(false);
					throw new ConsistirException(SituacaoArquivoRetorno.CONTAS_BAIXADAS.getDescricao());
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String getExecutarAtualizacaoProcessamento() {
		realizarAtualizacaoProcessamento();
		return getProcessamentoArquivoRetornoParceiroVO().getProgressBar();
	}

	// Atualiza a página a cada 1 minuto
	public synchronized void executarAtualizacaoPagina() {
		if (getAtivarPush()) {
			realizarAtualizacaoProcessamento();
		}
	}

	public void realizarAtualizacaoProcessamento() {
		try {
			if (getAtivarPush()) {
				getFacadeFactory().getProcessamentoArquivoRetornoParceiroFacade().realizarAtualizacaoDadosProcessamento(getProcessamentoArquivoRetornoParceiroVO());
				if (getProcessamentoArquivoRetornoParceiroVO().getSituacaoProcessamento().isAguardandoProcessamento() || getProcessamentoArquivoRetornoParceiroVO().getSituacaoProcessamento().isErroProcessamento() || getProcessamentoArquivoRetornoParceiroVO().getSituacaoProcessamento().isInterrompidoProcessamento()) {
					executarConsultaAutomatica();
					if (getJobBaixarContasArquivoRetornoParceiro() != null) {
						setMensagemDetalhada("", getJobBaixarContasArquivoRetornoParceiro().getSituacao());
					}
					if (getProcessamentoArquivoRetornoParceiroVO().getSituacaoProcessamento().isErroProcessamento() || getProcessamentoArquivoRetornoParceiroVO().getSituacaoProcessamento().isInterrompidoProcessamento()) {
						setMensagemDetalhada("msg_erro", getProcessamentoArquivoRetornoParceiroVO().getMotivoErroProcessamento(), true);
						setAtivarPush(false);
					}
				}
				if (getProcessamentoArquivoRetornoParceiroVO().getSituacaoProcessamento().isProcessamentoConcluido()) {
					setAtivarPush(false);
					executarConsultaAutomatica();
				}
			}
		} catch (Exception e) {
			if (getProcessamentoArquivoRetornoParceiroVO().getSituacaoProcessamento().isErroProcessamento() || getProcessamentoArquivoRetornoParceiroVO().getSituacaoProcessamento().isInterrompidoProcessamento()) {
				setMensagemDetalhada("msg_erro", getProcessamentoArquivoRetornoParceiroVO().getMotivoErroProcessamento(), true);
				setAtivarPush(false);
			} else if (getJobBaixarContasArquivoRetornoParceiro() != null) {
				setMensagemDetalhada("msg_erro", getJobBaixarContasArquivoRetornoParceiro().getSituacao(), true);
			} else {
				setMensagemDetalhada("msg_erro", e.getMessage(), true);
			}
		}
	}

	public void executarConsultaAutomatica() throws Exception {
		if (!Uteis.ARQUIVOS_CONTROLE_COBRANCA.isEmpty() && Uteis.ARQUIVOS_CONTROLE_COBRANCA.get(Uteis.getNomeArquivoComUnidadeEnsino(getProcessamentoArquivoRetornoParceiroVO().getNomeArquivo_Apresentar(), getProcessamentoArquivoRetornoParceiroVO().getUnidadeEnsinoVO().getCodigo())).equals(SituacaoArquivoRetorno.ARQUIVO_PROCESSADO.getValor())) {
			setJobBaixarContasArquivoRetornoParceiro(null);
			setAtivarPush(false);
			throw new ConsistirException(SituacaoArquivoRetorno.ARQUIVO_PROCESSADO.getDescricao());
		}
		if (!Uteis.ARQUIVOS_CONTROLE_COBRANCA.isEmpty() && Uteis.ARQUIVOS_CONTROLE_COBRANCA.get(Uteis.getNomeArquivoComUnidadeEnsino(getProcessamentoArquivoRetornoParceiroVO().getNomeArquivo_Apresentar(), getProcessamentoArquivoRetornoParceiroVO().getUnidadeEnsinoVO().getCodigo())).equals(SituacaoArquivoRetorno.CONTAS_BAIXADAS.getValor())) {
			setProcessamentoArquivoRetornoParceiroVO(getFacadeFactory().getProcessamentoArquivoRetornoParceiroFacade().consultarPorChavePrimaria(getProcessamentoArquivoRetornoParceiroVO().getCodigo(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			getProcessamentoArquivoRetornoParceiroVO().setContasBaixadas(Boolean.TRUE);
			setAtivarPush(false);
			String msg = SituacaoArquivoRetorno.CONTAS_BAIXADAS.getDescricao();
			if (getJobBaixarContasArquivoRetornoParceiro() != null) {
				msg = getJobBaixarContasArquivoRetornoParceiro().getSituacao();
			}
			setJobBaixarContasArquivoRetornoParceiro(null);
			throw new ConsistirException(msg);
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP
	 * BancoCons.jsp. Define o tipo de consulta a ser executada, por meio de
	 * ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
	 * resultado, disponibiliza um List com os objetos selecionados na sessao da
	 * pagina.
	 */
	public String consultar() {
		try {
			super.consultar();
			List objs = new ArrayList(0);
			if (getControleConsulta().getCampoConsulta().equals("codigo")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getProcessamentoArquivoRetornoParceiroFacade().consultaRapidaPorCodigo(new Integer(valorInt), getUnidadeEnsinoLogado().getCodigo(), true, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("dataGeracao")) {
				objs = getFacadeFactory().getProcessamentoArquivoRetornoParceiroFacade().consultaRapidaPorDataGeracao(getControleConsulta().getDataIni(), getControleConsulta().getDataFim(), getUnidadeEnsinoLogado().getCodigo(), true, getUsuarioLogado());
			}
			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("processamentoArquivoRetornoParceiroCons.xhtml");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("processamentoArquivoRetornoParceiroCons.xhtml");
		}
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe
	 * <code>BancoVO</code> Após a exclusão ela automaticamente aciona a rotina
	 * para uma nova inclusão.
	 */
	public String excluir() {
		try {
			getFacadeFactory().getProcessamentoArquivoRetornoParceiroFacade().excluir(getProcessamentoArquivoRetornoParceiroVO(), true, getUsuarioLogado());
			novo();
			setMensagemID("msg_dados_excluidos");
			return Uteis.getCaminhoRedirecionamentoNavegacao("processamentoArquivoRetornoParceiroForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("processamentoArquivoRetornoParceiroForm.xhtml");
		}
	}

	public void uploadArquivo(FileUploadEvent upload) {
		try {
			if(!Uteis.isAtributoPreenchido(getProcessamentoArquivoRetornoParceiroVO().getUnidadeEnsinoVO())){
				throw new Exception("O campo UNIDADE ENSINO deve ser informado.");
			}
			getFacadeFactory().getProcessamentoArquivoRetornoParceiroFacade().realizarLeituraArquivoExcel(upload, getProcessamentoArquivoRetornoParceiroVO(), getUsuarioLogado());
			Uteis.ARQUIVOS_CONTROLE_COBRANCA.remove(Uteis.getNomeArquivoComUnidadeEnsino(getProcessamentoArquivoRetornoParceiroVO().getNomeArquivo_Apresentar(), getProcessamentoArquivoRetornoParceiroVO().getUnidadeEnsinoVO().getCodigo()));
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void consultarParceiro() {
		try {
			super.consultar();
			List objs = new ArrayList(0);
			if (getCampoConsultaParceiro().equals("codigo")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getParceiroFacade().consultarPorCodigo(new Integer(valorInt), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaParceiro().equals("nome")) {
				objs = getFacadeFactory().getParceiroFacade().consultarPorNome(getValorConsultaParceiro(), false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado());
			}
			if (getCampoConsultaParceiro().equals("razaoSocial")) {
				objs = getFacadeFactory().getParceiroFacade().consultarPorRazaoSocial(getValorConsultaParceiro(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaParceiro().equals("RG")) {
				objs = getFacadeFactory().getParceiroFacade().consultarPorRG(getValorConsultaParceiro(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaParceiro().equals("CPF")) {
				objs = getFacadeFactory().getParceiroFacade().consultarPorCPF(getValorConsultaParceiro(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaParceiro().equals("tipoParceiro")) {
				objs = getFacadeFactory().getParceiroFacade().consultarPorTipoParceiro(getValorConsultaParceiro(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaParceiro(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(null);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarParceiro() {
		ParceiroVO obj = null;
		try {
			if (!Uteis.isAtributoPreenchido(getProcessamentoArquivoRetornoParceiroVO().getUnidadeEnsinoVO())) {
				throw new Exception("A Unidade de Ensino deve ser informada.");
			}
			
			obj = (ParceiroVO) context().getExternalContext().getRequestMap().get("parceiroItens");
			getProcessamentoArquivoRetornoParceiroVO().setParceiroVO(getFacadeFactory().getParceiroFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado()));
			if (!Uteis.isAtributoPreenchido(getProcessamentoArquivoRetornoParceiroVO().getParceiroVO().getCategoriaDespesa())) {
				throw new Exception("A CATEGORIA de DESPESA no cadastro do parceiro deve ser informada.");
			}

			if (!Uteis.isAtributoPreenchido(getProcessamentoArquivoRetornoParceiroVO().getParceiroVO().getContaCorrenteDaUnidadeEnsino(getProcessamentoArquivoRetornoParceiroVO().getUnidadeEnsinoVO()))) {
				throw new Exception("Não foi encontrado uma CONTA CORRENTE no cadastro do parceiro para a unidade de Ensino informada.");
			}

			if (!Uteis.isAtributoPreenchido(getProcessamentoArquivoRetornoParceiroVO().getParceiroVO().getFormaPagamentoRecebimento())) {
				throw new Exception("A FORMA de RECEBIMENTO no cadastro do parceiro deve ser informada.");
			}

			if (!Uteis.isAtributoPreenchido(getProcessamentoArquivoRetornoParceiroVO().getParceiroVO().getFormaPagamento())) {
				throw new Exception("A FORMA de PAGAMENTO no cadastro do parceiro deve ser informada.");
			}
		} catch (Exception e) {
			getProcessamentoArquivoRetornoParceiroVO().setParceiroVO(new ParceiroVO());
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			Uteis.liberarListaMemoria(getListaConsultaParceiro());
			removerObjetoMemoria(obj);
			valorConsultaParceiro = "";
			campoConsultaParceiro = "";
		}
	}
	
	public void atualizarParceiro(){
		try {
			getFacadeFactory().getParceiroFacade().atualizarValorIsentarJuroMulta(getProcessamentoArquivoRetornoParceiroVO().getParceiroVO(), getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} 
	}

	private void montarListaSelectItemUnidadeEnsino() throws Exception {
		if (getUnidadeEnsinoLogado().getCodigo().intValue() != 0) {
			setListaSelectItemUnidadeEnsino(new ArrayList<SelectItem>());
			getListaSelectItemUnidadeEnsino().add(new SelectItem(0, ""));
			UnidadeEnsinoVO obj = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCodigo(getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			getListaSelectItemUnidadeEnsino().add(new SelectItem(obj.getCodigo(), obj.getNome()));
			getProcessamentoArquivoRetornoParceiroVO().setUnidadeEnsinoVO(obj);
			return;
		}
		List resultadoConsulta = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome("", 0, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));

	}	

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List getTipoConsultaComboParceiro() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("razaoSocial", "Razão Social"));
		itens.add(new SelectItem("RG", "RG"));
		itens.add(new SelectItem("CPF", "CPF"));
		itens.add(new SelectItem("tipoParceiro", "Tipo Parceiro"));
		return itens;
	}

	public void irPaginaInicial() throws Exception {
		this.consultar();
	}

	public void irPaginaAnterior() throws Exception {
		getControleConsulta().setPaginaAtual(controleConsulta.getPaginaAtual() - 1);
		this.consultar();
	}

	public void irPaginaPosterior() throws Exception {
		getControleConsulta().setPaginaAtual(controleConsulta.getPaginaAtual() + 1);
		this.consultar();
	}

	public void irPaginaFinal() throws Exception {
		getControleConsulta().setPaginaAtual(controleConsulta.getNrTotalPaginas());
		this.consultar();
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes
	 * de uma consulta.
	 */
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setListaConsulta(new ArrayList(0));
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("processamentoArquivoRetornoParceiroCons.xhtml");
	}

	public List getTipoConsultaCombo() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("codigo", "Número"));
		itens.add(new SelectItem("dataGeracao", "Data de Geração"));
		return itens;
	}

	public ProcessamentoArquivoRetornoParceiroVO getProcessamentoArquivoRetornoParceiroVO() {
		if (processamentoArquivoRetornoParceiroVO == null) {
			processamentoArquivoRetornoParceiroVO = new ProcessamentoArquivoRetornoParceiroVO();
		}
		return processamentoArquivoRetornoParceiroVO;
	}

	public void setProcessamentoArquivoRetornoParceiroVO(ProcessamentoArquivoRetornoParceiroVO processamentoArquivoRetornoParceiroVO) {
		this.processamentoArquivoRetornoParceiroVO = processamentoArquivoRetornoParceiroVO;
	}

	/**
	 * @return the listaConsultaParceiro
	 */
	public List getListaConsultaParceiro() {
		if (listaConsultaParceiro == null) {
			listaConsultaParceiro = new ArrayList(0);
		}
		return listaConsultaParceiro;
	}

	/**
	 * @param listaConsultaParceiro
	 *            the listaConsultaParceiro to set
	 */
	public void setListaConsultaParceiro(List listaConsultaParceiro) {
		this.listaConsultaParceiro = listaConsultaParceiro;
	}

	/**
	 * @return the valorConsultaParceiro
	 */
	public String getValorConsultaParceiro() {
		if (valorConsultaParceiro == null) {
			valorConsultaParceiro = "";
		}
		return valorConsultaParceiro;
	}

	/**
	 * @param valorConsultaParceiro
	 *            the valorConsultaParceiro to set
	 */
	public void setValorConsultaParceiro(String valorConsultaParceiro) {
		this.valorConsultaParceiro = valorConsultaParceiro;
	}

	/**
	 * @return the campoConsultaParceiro
	 */
	public String getCampoConsultaParceiro() {
		if (campoConsultaParceiro == null) {
			campoConsultaParceiro = "";
		}
		return campoConsultaParceiro;
	}

	/**
	 * @param campoConsultaParceiro
	 *            the campoConsultaParceiro to set
	 */
	public void setCampoConsultaParceiro(String campoConsultaParceiro) {
		this.campoConsultaParceiro = campoConsultaParceiro;
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

	public Boolean getMarcarTodos() {
		return marcarTodos;
	}

	public void setMarcarTodos(Boolean marcarTodos) {
		this.marcarTodos = marcarTodos;
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

	public JobBaixarContasArquivoRetornoParceiro getJobBaixarContasArquivoRetornoParceiro() {
		return jobBaixarContasArquivoRetornoParceiro;
	}

	public void setJobBaixarContasArquivoRetornoParceiro(JobBaixarContasArquivoRetornoParceiro jobBaixarContasArquivoRetornoParceiro) {
		this.jobBaixarContasArquivoRetornoParceiro = jobBaixarContasArquivoRetornoParceiro;
	}

	/*public PushEventListener getListener() {
		return listener;
	}

	public void setListener(PushEventListener listener) {
		this.listener = listener;
	}*/
	
	public void limparCampoParceiro() {
		getProcessamentoArquivoRetornoParceiroVO().setParceiroVO(new ParceiroVO());
	}

	public void limparCampoConsulta() {
		getControleConsulta().setValorConsulta("");
		getControleConsulta().setDataIni(Uteis.getDataPrimeiroDiaMes(new Date()));
		getControleConsulta().setDataFim(Uteis.getDataUltimoDiaMes(new Date()));
	}

	public boolean getIsConsultaPorNumeroSelecionado() {
		return getControleConsulta().getCampoConsulta().equals("codigo");
	}

	public boolean getIsConsultaPorDataGeracaoSelecionado() {
		return getControleConsulta().getCampoConsulta().equals("dataGeracao");
	}

}
