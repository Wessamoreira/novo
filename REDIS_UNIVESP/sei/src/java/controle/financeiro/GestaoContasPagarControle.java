package controle.financeiro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import controle.arquitetura.SuperControle;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.compras.FormaPagamentoVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroCartaoVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.ContaPagarVO;
import negocio.comuns.financeiro.FormaPagamentoNegociacaoPagamentoVO;
import negocio.comuns.financeiro.GestaoContasPagarVO;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

@Controller("GestaoContasPagarControle")
@Scope("viewScope")
@Lazy
public class GestaoContasPagarControle extends SuperControle implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5137015993006797062L;
	private static final String CONTEXT_PARA_EDICAO = "contaPagarItens";
	private GestaoContasPagarVO gestaoContasPagarVO;	
	private List<ContaPagarVO> listaContaPagar;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private List<SelectItem> listaSelectItemFormaPagamento;
	private List<SelectItem> listaSelectItemConfiguracaoFinanceiroCartao;
	private List<SelectItem> listaSelectItemCaixa;
	private List<SelectItem> listaSelectItemContaCorrente;
	private GestaoContasPagarVO gestaoContasPagarVOFiltro;
	private ProgressBarVO progressBarVO;
	
	
	
	private boolean marcaTodasContaPagar = false;
	
	public GestaoContasPagarControle()  {
		setControleConsultaOtimizado(new DataModelo());
		getGestaoContasPagarVO().setCampoConsulta(ContaPagarVO.enumCampoConsultaContaPagar.FAVORECIDO.name());
		getGestaoContasPagarVO().setDataVencimentoInicio(Uteis.getDataPrimeiroDiaMes(new Date()));
		getGestaoContasPagarVO().setDataVencimentoFim(Uteis.getDataUltimoDiaMes(new Date()));
		montarListaSelectItemUnidadeEnsino();
		setMensagemID(MSG_TELA.msg_entre_prmconsulta.name());
	}
	
	public void realizarNavegacaoParaOrigemContaPagar() {
		try {
		ContaPagarVO obj = (ContaPagarVO) context().getExternalContext().getRequestMap().get(CONTEXT_PARA_EDICAO);
		context().getExternalContext().getSessionMap().put("contaPagarGestaoContaPagar", obj);
		removerControleMemoriaFlash("ContaPagarControle");
		removerControleMemoriaTela("ContaPagarControle");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void limparCamposPesquisa() {
		try {
			getGestaoContasPagarVO().setValorConsulta("");
			setMarcaTodasContaPagar(false);
			getListaContaPagar().clear();
			if(getGestaoContasPagarVO().getGestaoContasPagarOperacaoEnum().isPagamento()) {
				montarListaSelectItemFormaPagamento();
				getGestaoContasPagarVO().setFormaPagamentoNegociacaoPagamentoVO(new FormaPagamentoNegociacaoPagamentoVO());
			}else if(getGestaoContasPagarVO().getGestaoContasPagarOperacaoEnum().isEstornoPagamento()) {
				getGestaoContasPagarVO().setMotivoAlteracao("");
				getGestaoContasPagarVO().setDesconsiderarConciliacaoBancaria(false);
			}
			setMensagemID(MSG_TELA.msg_entre_prmconsulta.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public String consultarContaPagar() {
		try {
			getListaContaPagar().clear();
			setListaContaPagar(getFacadeFactory().getGestaoContasPagarFacade().consultar(getGestaoContasPagarVO(), true, getUsuarioLogadoClone()));	
			setMarcaTodasContaPagar(false);
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
		return "";
	}
	
	public void scrollerListenerContaPagar(DataScrollEvent dataScrollerEvent) {
		try {
			getControleConsultaOtimizado().setPage(dataScrollerEvent.getPage());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	@Override
	public String consultar() {
		try {
			getControleConsultaOtimizado().preencherDadosParaConsulta(true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogadoClone());
			getControleConsultaOtimizado().setLimitePorPagina(5);
			getFacadeFactory().getGestaoContasPagarFacade().consultar(getControleConsultaOtimizado(), getGestaoContasPagarVOFiltro());
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			getListaConsulta().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
		return "";
	}
	
	public void scrollerListener(DataScrollEvent dataScrollerEvent) {
		try {
			getControleConsultaOtimizado().setPaginaAtual(dataScrollerEvent.getPage());
			getControleConsultaOtimizado().setPage(dataScrollerEvent.getPage());
			consultar();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void inicializarDadosParaConsultaLog() {
		try {
			setGestaoContasPagarVOFiltro(new GestaoContasPagarVO());
			getGestaoContasPagarVOFiltro().setGestaoContasPagarOperacaoEnum(getGestaoContasPagarVO().getGestaoContasPagarOperacaoEnum());
			getGestaoContasPagarVOFiltro().getUnidadeEnsinoVO().setCodigo(getGestaoContasPagarVO().getUnidadeEnsinoVO().getCodigo());
			setControleConsultaOtimizado(new DataModelo());
			getControleConsultaOtimizado().setDataIni(Uteis.getDataPrimeiroDiaMes(new Date()));
			getControleConsultaOtimizado().setDataFim(Uteis.getDataUltimoDiaMes(new Date()));
			setMensagemID(MSG_TELA.msg_entre_prmconsulta.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void persitir() {
		setOncompleteModal("");
		setProgressBarVO(new ProgressBarVO());	
		List<ContaPagarVO> listaTemp = new ArrayList<>();
		try {			
			ContaPagarVO obj = (ContaPagarVO) context().getExternalContext().getRequestMap().get(CONTEXT_PARA_EDICAO);
			obj.setSelecionado(true);
			listaTemp.add(obj);
			getGestaoContasPagarVO().setUnidadeEnsinoVO(obj.getUnidadeEnsino());
			getFacadeFactory().getGestaoContasPagarFacade().persitir(getGestaoContasPagarVO(), listaTemp, getProgressBarVO(), true, getUsuarioLogadoClone());
			obj.setSelecionado(false);
			if(!getGestaoContasPagarVO().getGestaoContasPagarOperacaoEnum().isAlteracao()) {
				getListaContaPagar().removeIf(p-> p.getCodigo().equals(obj.getCodigo()));	
			}
			getGestaoContasPagarVO().setUnidadeEnsinoVO(new UnidadeEnsinoVO());
			inicializarModalLogs();
			setMensagemID(MSG_TELA.msg_dados_gravados.name());
		} catch (Exception e) {
			setOncompleteModal("");
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		} finally{
			getProgressBarVO().setForcarEncerramento(true);
		}
	}
	
	public void persitirSelecionados() {
		try {			
			getFacadeFactory().getGestaoContasPagarFacade().persitir(getGestaoContasPagarVO(), getListaContaPagar(), getProgressBarVO(), true, getProgressBarVO().getUsuarioVO());		
			if(!getGestaoContasPagarVO().getGestaoContasPagarOperacaoEnum().isAlteracao()) {
				getListaContaPagar().removeIf(ContaPagarVO::isSelecionado);
			}else {
				getListaContaPagar().stream().forEach(contaPagarVO->contaPagarVO.setSelecionado(false));
			}
			if(getProgressBarVO().getSuperControle() != null) {
				getProgressBarVO().getSuperControle().setMensagemID(MSG_TELA.msg_dados_gravados.name());
			}else {
				setMensagemID(MSG_TELA.msg_dados_gravados.name());
			}			
			inicializarModalLogs();
		} catch (Exception e) {
			if(getProgressBarVO().getSuperControle() != null) {
				getProgressBarVO().getSuperControle().setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			}else {
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			}			
		} finally{
			getProgressBarVO().incrementar();
			getProgressBarVO().setForcarEncerramento(true);			
		}
	}

	private void inicializarModalLogs() {
		setOncompleteModal("RichFaces.$('panelGestaoContasPagarLogs').show();");
		setGestaoContasPagarVOFiltro(new GestaoContasPagarVO());
		getGestaoContasPagarVOFiltro().setUnidadeEnsinoVO(getGestaoContasPagarVO().getUnidadeEnsinoVO());
		getGestaoContasPagarVOFiltro().setGestaoContasPagarOperacaoEnum(getGestaoContasPagarVO().getGestaoContasPagarOperacaoEnum());
		setControleConsultaOtimizado(new DataModelo());
		getControleConsultaOtimizado().setDataIni(new Date());
		getControleConsultaOtimizado().setDataFim(new Date());
		getControleConsultaOtimizado().preencherDadosParaConsulta(true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogadoClone());
		getControleConsultaOtimizado().setLimitePorPagina(5);
		getControleConsultaOtimizado().setTotalRegistrosEncontrados(getGestaoContasPagarVO().getListaGestaoContasPagarLogs().size());
		getControleConsultaOtimizado().setListaConsulta(getGestaoContasPagarVO().getListaGestaoContasPagarLogs());
	}
	
	
	public void realizarInicioProgressBar() {
		try {
			setProgressBarVO(new ProgressBarVO());
			Uteis.checkState(getQtdContasSelecionadas() == 0L, "Não foi encontrado nenhuma conta pagar selecionada para realizar a operação de " + getGestaoContasPagarVO().getGestaoContasPagarOperacaoEnum().name());
			getProgressBarVO().resetar();
			getProgressBarVO().setAplicacaoControle(getAplicacaoControle());
			getProgressBarVO().setUsuarioVO(getUsuarioLogadoClone());
			getProgressBarVO().iniciar(0l, (getQtdContasSelecionadas().intValue()), "Iniciando Processamento da(s) operações.", true, this, "persitirSelecionados");
			setOncompleteModal("");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());	
		} 
	}
	
	public void realizarAlteracaoDataVencimento() {
		try {
			getFacadeFactory().getGestaoContasPagarFacade().executarAplicacaoDaAlteracaoDataVencimento(getGestaoContasPagarVO(), getListaContaPagar(), getUsuarioLogadoClone());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}	
	}
	
	public void realizarAlteracaoDataOperacaoAlterar() {
		try {
			getFacadeFactory().getGestaoContasPagarFacade().executarAplicacaoDaAlteracaoDataOperacao(getGestaoContasPagarVO(), getListaContaPagar(), getUsuarioLogadoClone());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}	
	}
	
	public void realizarAlteracaoValor() {
		try {
			getFacadeFactory().getGestaoContasPagarFacade().executarAplicacaoDaAlteracaoValor(getGestaoContasPagarVO(), getListaContaPagar(), getUsuarioLogadoClone());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}	
	}
	
	public void realizarMarcacaoTodasContaPagar(String codigo, String favorecido,String cnpjOuCpfFavorecido,String tipoOrigem_Apresentar,String codOrigem,String parcela,String nrDocumento) {
		try {
			getListaContaPagar().stream()
			.filter(p -> (!Uteis.isAtributoPreenchido(codigo) || (Uteis.isAtributoPreenchido(codigo) && Uteis.removerAcentos(p.getCodigo().toString().toLowerCase()).contains(Uteis.removerAcentos(codigo.toLowerCase()))))
					&& (!Uteis.isAtributoPreenchido(favorecido) || (Uteis.isAtributoPreenchido(favorecido) && Uteis.removerAcentos(p.getFavorecido().toLowerCase()).contains(Uteis.removerAcentos(favorecido.toLowerCase()))))
					&& (!Uteis.isAtributoPreenchido(cnpjOuCpfFavorecido) || (Uteis.isAtributoPreenchido(cnpjOuCpfFavorecido) && Uteis.removerAcentos(p.getCnpjOuCpfFavorecido().toLowerCase()).contains(Uteis.removerAcentos(cnpjOuCpfFavorecido.toLowerCase()))))
					&& (!Uteis.isAtributoPreenchido(tipoOrigem_Apresentar) || (Uteis.isAtributoPreenchido(tipoOrigem_Apresentar) && Uteis.removerAcentos(p.getTipoOrigem_Apresentar().toLowerCase()).contains(Uteis.removerAcentos(tipoOrigem_Apresentar.toLowerCase()))))
					&& (!Uteis.isAtributoPreenchido(codOrigem) || (Uteis.isAtributoPreenchido(codOrigem) && Uteis.removerAcentos(p.getCodOrigem().toLowerCase()).contains(Uteis.removerAcentos(codOrigem.toLowerCase()))))
					&& (!Uteis.isAtributoPreenchido(parcela) || (Uteis.isAtributoPreenchido(parcela) && Uteis.removerAcentos(p.getParcela().toLowerCase()).contains(Uteis.removerAcentos(parcela.toLowerCase()))))
					&& (!Uteis.isAtributoPreenchido(nrDocumento) || (Uteis.isAtributoPreenchido(nrDocumento) && Uteis.removerAcentos(p.getNrDocumento().toLowerCase()).contains(Uteis.removerAcentos(nrDocumento.toLowerCase()))))
					)
			.forEach(p->p.setSelecionado(isMarcaTodasContaPagar()));
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}	
	}
	
	public void realizarValidacaoSeTodasContasEstaoMarcadas() {
		try {
			setMarcaTodasContaPagar(getListaContaPagar().stream().allMatch(ContaPagarVO::isSelecionado));
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}	
	}
	
	public void consultarFormaPagamento() {
		try {
			if (!Uteis.isAtributoPreenchido(getGestaoContasPagarVO().getFormaPagamentoNegociacaoPagamentoVO().getFormaPagamento())) {
				getGestaoContasPagarVO().getFormaPagamentoNegociacaoPagamentoVO().setFormaPagamento(new FormaPagamentoVO());
			}else {
				getGestaoContasPagarVO().getFormaPagamentoNegociacaoPagamentoVO().setFormaPagamento(getFacadeFactory().getFormaPagamentoFacade().consultarPorChavePrimaria(getGestaoContasPagarVO().getFormaPagamentoNegociacaoPagamentoVO().getFormaPagamento().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogadoClone()));
				if(getGestaoContasPagarVO().getFormaPagamentoNegociacaoPagamentoVO().getInformarContaCorrente()) {
					montarListaSelectItemContaCorrente();
				}
				if(getGestaoContasPagarVO().getFormaPagamentoNegociacaoPagamentoVO().getInformarContaCaixa()) {
					montarListaSelectItemCaixa();
				}
				if(getGestaoContasPagarVO().getFormaPagamentoNegociacaoPagamentoVO().getIsInformaOperadoraCartao()) {
					montarListaSelectItemConfiguracaoFinanceiroCartao();
				}
			}
		} catch (Exception e) {
			getGestaoContasPagarVO().getFormaPagamentoNegociacaoPagamentoVO().setFormaPagamento(new FormaPagamentoVO());
		}
	}
	
	private void montarListaSelectItemConfiguracaoFinanceiroCartao() throws Exception {
		List<ConfiguracaoFinanceiroCartaoVO> lista = new ArrayList<ConfiguracaoFinanceiroCartaoVO>(0);
		getListaSelectItemConfiguracaoFinanceiroCartao().clear();
		if (getGestaoContasPagarVO().getFormaPagamentoNegociacaoPagamentoVO().getFormaPagamento().getTipo().equals("CA")) {
			lista = getFacadeFactory().getConfiguracaoFinanceiroCartaoFacade().consultarPorTipoOperadoraCartaoConfiguracaoFinanceiro("CARTAO_CREDITO", getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getGestaoContasPagarVO().getUnidadeEnsinoVO().getCodigo()).getCodigo(), false, 0, false, getUsuarioLogadoClone());
		} else if (getGestaoContasPagarVO().getFormaPagamentoNegociacaoPagamentoVO().getFormaPagamento().getTipo().equals("DE")) {
			lista = getFacadeFactory().getConfiguracaoFinanceiroCartaoFacade().consultarPorTipoOperadoraCartaoConfiguracaoFinanceiro("CARTAO_DEBITO", getConfiguracaoFinanceiroPadraoSistema().getCodigo(), false , 0, false, getUsuarioLogadoClone());
		}
		getListaSelectItemConfiguracaoFinanceiroCartao().add(new SelectItem(0, ""));
		for (ConfiguracaoFinanceiroCartaoVO obj : lista) {
			if (obj.getOperadoraCartaoVO().getTipo().equals("CARTAO_CREDITO")) {
				getListaSelectItemConfiguracaoFinanceiroCartao().add(new SelectItem(obj.getCodigo(), obj.getContaCorrenteVO().getBancoAgenciaContaCorrente() + "-" + obj.getOperadoraCartaoVO().getNome() + " - "+ obj.getOperadoraCartaoVO().getOperadoraCartaoCreditoApresentar()));
			} else {
				getListaSelectItemConfiguracaoFinanceiroCartao().add(new SelectItem(obj.getCodigo(), obj.getContaCorrenteVO().getBancoAgenciaContaCorrente() + "-" + obj.getOperadoraCartaoVO().getNome()));
			}
		}
		getListaSelectItemConfiguracaoFinanceiroCartao().get(0);
	}
	
	public void consultarConfiguracaoFinanceiroCartao()  {
		try {
			if (Uteis.isAtributoPreenchido(getGestaoContasPagarVO().getConfiguracaoFinanceiroCartaoVO())) {
				getGestaoContasPagarVO().setConfiguracaoFinanceiroCartaoVO(getFacadeFactory().getConfiguracaoFinanceiroCartaoFacade().consultarPorChavePrimaria(getGestaoContasPagarVO().getConfiguracaoFinanceiroCartaoVO().getCodigo(), NivelMontarDados.TODOS, getUsuarioLogadoClone()));
				getGestaoContasPagarVO().getFormaPagamentoNegociacaoPagamentoVO().setOperadoraCartaoVO(getGestaoContasPagarVO().getConfiguracaoFinanceiroCartaoVO().getOperadoraCartaoVO());
				getGestaoContasPagarVO().getFormaPagamentoNegociacaoPagamentoVO().setCategoriaDespesaVO(getGestaoContasPagarVO().getConfiguracaoFinanceiroCartaoVO().getCategoriaDespesaVO());
				getGestaoContasPagarVO().getFormaPagamentoNegociacaoPagamentoVO().setContaCorrenteOperadoraCartaoVO(getGestaoContasPagarVO().getConfiguracaoFinanceiroCartaoVO().getContaCorrenteVO());
				if (getGestaoContasPagarVO().getFormaPagamentoNegociacaoPagamentoVO().getFormaPagamento().getTipo().equals("DE")) {
					getGestaoContasPagarVO().getFormaPagamentoNegociacaoPagamentoVO().setQtdeParcelasCartaoCredito(1);
				}
			}else {
				getGestaoContasPagarVO().setConfiguracaoFinanceiroCartaoVO(null);	
			}
		} catch (Exception e) {
			getGestaoContasPagarVO().setConfiguracaoFinanceiroCartaoVO(null);
		}
	}
	
	public Long getQtdContasSelecionadas() {
		return getListaContaPagar().stream().filter(ContaPagarVO::isSelecionado).count();
	}
	
	public Double getValorTotalContasSelecionadas() {
		return getListaContaPagar().stream().filter(ContaPagarVO::isSelecionado).mapToDouble(ContaPagarVO::getValor).reduce(0D, (a, b) -> Uteis.arrendondarForcando2CadasDecimais(a + b));
	}
	
	public void montarListaSelectItemUnidadeEnsino() {
		try {
			getListaSelectItemUnidadeEnsino().clear();
			if (getUnidadeEnsinoLogado().getCodigo().intValue() != 0) {
				setListaSelectItemUnidadeEnsino(new ArrayList<>());
				UnidadeEnsinoVO obj = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCodigo(getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogadoClone());
				getGestaoContasPagarVO().setUnidadeEnsinoVO(obj);
				getListaSelectItemUnidadeEnsino().add(new SelectItem(0, ""));
				getListaSelectItemUnidadeEnsino().add(new SelectItem(obj.getCodigo(), obj.getNome_CNPJ()));
				return;
			}
			List<UnidadeEnsinoVO> resultadoConsulta = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome("",0, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogadoClone());
			setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome_CNPJ",true));
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void montarListaSelectItemFormaPagamento() {
		try {
			getListaSelectItemFormaPagamento().clear();
			getListaSelectItemFormaPagamento().add(new SelectItem(0,""));
			List<FormaPagamentoVO> resultadoConsulta = getFacadeFactory().getFormaPagamentoFacade().consultarPorNome("", false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogadoClone());
			resultadoConsulta.stream()
			.filter(p-> p.isDinheiro() || p.isBoletoBancario() || p.isDebitoEmConta() || p.isCartaoCredito() || p.isCartaoDebito())
			.forEach(p-> getListaSelectItemFormaPagamento().add(new SelectItem(p.getCodigo(), p.getNome().toString())));
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void montarListaSelectItemCaixa() {
		try {
			List<ContaCorrenteVO> resultadoConsulta = null;
			getListaSelectItemCaixa().clear();
			resultadoConsulta = consultarListaSelectItemParaContaCorrente(true);
			setListaSelectItemCaixa(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "descricaoCompletaConta"));
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void montarListaSelectItemContaCorrente() {
		try {
			List<ContaCorrenteVO> resultadoConsulta = null;
			getListaSelectItemContaCorrente().clear();
			resultadoConsulta = consultarListaSelectItemParaContaCorrente(false);
			setListaSelectItemContaCorrente(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "descricaoCompletaConta"));
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	private List<ContaCorrenteVO> consultarListaSelectItemParaContaCorrente(boolean isContaCaixa) throws Exception {
		List<ContaCorrenteVO> resultadoConsulta = null;
		boolean usuarioTemContaCaixa = getFacadeFactory().getContaCorrenteFacade().consultarSeUsuarioTemContaCaixaVinculadoAEle(getUsuarioLogadoClone().getPessoa().getCodigo());
		if (usuarioTemContaCaixa) {
			resultadoConsulta = getFacadeFactory().getContaCorrenteFacade().consultarPorFuncionarioResponsavel(getUsuarioLogadoClone().getPessoa().getCodigo(), getGestaoContasPagarVO().getUnidadeEnsinoVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogadoClone());
			if(!Uteis.isAtributoPreenchido(resultadoConsulta)) {
				resultadoConsulta = getFacadeFactory().getContaCorrenteFacade().consultarPorFuncionarioResponsavelDataAberturaFluxoCaixaSituacao(isContaCaixa, getUsuarioLogadoClone().getPessoa().getCodigo(), getGestaoContasPagarVO().getUnidadeEnsinoVO().getCodigo(), new Date(), "A", Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogadoClone());
			}
		} 
		if(!Uteis.isAtributoPreenchido(resultadoConsulta)) {
			resultadoConsulta = getFacadeFactory().getContaCorrenteFacade().consultarPorContaCaixa(isContaCaixa, getGestaoContasPagarVO().getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogadoClone());
			if(!Uteis.isAtributoPreenchido(resultadoConsulta)) {
				resultadoConsulta = getFacadeFactory().getContaCorrenteFacade().consultarPorContaCaixaDataAberturaFluxoCaixaSituacao(isContaCaixa, getGestaoContasPagarVO().getUnidadeEnsinoVO().getCodigo(), new Date(), "A", false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogadoClone());
			}
		}
		return resultadoConsulta;
	}
	
	public List<SelectItem> getListaSelectItemCaixa() {
		if (listaSelectItemCaixa == null) {
			listaSelectItemCaixa = new ArrayList<>();
		}
		return (listaSelectItemCaixa);
	}

	public void setListaSelectItemCaixa(List<SelectItem> listaSelectItemCaixa) {
		this.listaSelectItemCaixa = listaSelectItemCaixa;
	}
	
	public List<SelectItem> getListaSelectItemContaCorrente() {
		if (listaSelectItemContaCorrente == null) {
			listaSelectItemContaCorrente = new ArrayList<>();
		}
		return (listaSelectItemContaCorrente);
	}

	public void setListaSelectItemContaCorrente(List<SelectItem> listaSelectItemContaCorrente) {
		this.listaSelectItemContaCorrente = listaSelectItemContaCorrente;
	}
	
	
	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList<>();
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}
	
	public List<SelectItem> getListaSelectItemFormaPagamento() {
		if (listaSelectItemFormaPagamento == null) {
			listaSelectItemFormaPagamento = new ArrayList<>();
		}
		return (listaSelectItemFormaPagamento);
	}

	public void setListaSelectItemFormaPagamento(List<SelectItem> listaSelectItemFormaPagamento) {
		this.listaSelectItemFormaPagamento = listaSelectItemFormaPagamento;
	}
	
	public List<SelectItem> getListaSelectItemConfiguracaoFinanceiroCartao() {
		if (listaSelectItemConfiguracaoFinanceiroCartao == null) {
			listaSelectItemConfiguracaoFinanceiroCartao = new ArrayList<>();
		}
		return listaSelectItemConfiguracaoFinanceiroCartao;
	}

	public void setListaSelectItemConfiguracaoFinanceiroCartao(List<SelectItem> listaSelectItemConfiguracaoFinanceiroCartao) {
		this.listaSelectItemConfiguracaoFinanceiroCartao = listaSelectItemConfiguracaoFinanceiroCartao;
	}

	public List<ContaPagarVO> getListaContaPagar() {
		if (listaContaPagar == null) {
			listaContaPagar = new ArrayList<>();
		}
		return listaContaPagar;
	}

	public void setListaContaPagar(List<ContaPagarVO> listaContaPagar) {
		this.listaContaPagar = listaContaPagar;
	}

	public GestaoContasPagarVO getGestaoContasPagarVO() {
		if (gestaoContasPagarVO == null) {
			gestaoContasPagarVO = new GestaoContasPagarVO();
		}
		return gestaoContasPagarVO;
	}

	public void setGestaoContasPagarVO(GestaoContasPagarVO gestaoContasPagarVO) {
		this.gestaoContasPagarVO = gestaoContasPagarVO;
	}

	public GestaoContasPagarVO getGestaoContasPagarVOFiltro() {
		if (gestaoContasPagarVOFiltro == null) {
			gestaoContasPagarVOFiltro =  new GestaoContasPagarVO();
		}
		return gestaoContasPagarVOFiltro;
	}

	public void setGestaoContasPagarVOFiltro(GestaoContasPagarVO gestaoContasPagarVOFiltro) {
		this.gestaoContasPagarVOFiltro = gestaoContasPagarVOFiltro;
	}

	public boolean isCampoTipoOrigem() {
		return getGestaoContasPagarVO().getCampoConsulta().equals(ContaPagarVO.enumCampoConsultaContaPagar.TIPO_ORIGEM.name());
	}
	
	public String getCssCampoTipoOrigem() {
		return isCampoTipoOrigem() ? "form-control camposObrigatorios" : "form-control campos";
	}
	
	public boolean isCampoText() {
		return getGestaoContasPagarVO().getCampoConsulta().equals(ContaPagarVO.enumCampoConsultaContaPagar.FAVORECIDO.name())
				|| getGestaoContasPagarVO().getCampoConsulta().equals(ContaPagarVO.enumCampoConsultaContaPagar.TURMA.name())
				|| getGestaoContasPagarVO().getCampoConsulta().equals(ContaPagarVO.enumCampoConsultaContaPagar.DEPARTAMENTO.name()) 
				|| getGestaoContasPagarVO().getCampoConsulta().equals(ContaPagarVO.enumCampoConsultaContaPagar.TIPO_ORIGEM.name())				 
				|| getGestaoContasPagarVO().getCampoConsulta().equals(ContaPagarVO.enumCampoConsultaContaPagar.NR_DOCUMENTO.name());
	}
	
	public boolean isCampoCPF() {
		return getGestaoContasPagarVO().getCampoConsulta().equals(ContaPagarVO.enumCampoConsultaContaPagar.CPF_FAVORECIDO.name());
	}
	public boolean isCampoCNPJ() {
		return getGestaoContasPagarVO().getCampoConsulta().equals(ContaPagarVO.enumCampoConsultaContaPagar.CNPJ_FORNECEDOR.name());
	}

	public boolean isCampoValorNumerico() {
		return getGestaoContasPagarVO().getCampoConsulta().equals(ContaPagarVO.enumCampoConsultaContaPagar.CODIGO.name()) 
				|| getGestaoContasPagarVO().getCampoConsulta().equals(ContaPagarVO.enumCampoConsultaContaPagar.CODIGO_PAGAMENTO.name()) 
				|| getGestaoContasPagarVO().getCampoConsulta().equals(ContaPagarVO.enumCampoConsultaContaPagar.NUMERO_NOTA_FISCAL_ENTRADA.name())
				|| getGestaoContasPagarVO().getCampoConsulta().equals(ContaPagarVO.enumCampoConsultaContaPagar.CODIGO_NOTA_FISCAL_ENTRADA.name());

	}
	
	public boolean isMarcaTodasContaPagar() {
		return marcaTodasContaPagar;
	}

	public void setMarcaTodasContaPagar(boolean marcaTodasContaPagar) {
		this.marcaTodasContaPagar = marcaTodasContaPagar;
	}
	
	public ProgressBarVO getProgressBarVO() {
		if(progressBarVO == null) {
			progressBarVO =  new ProgressBarVO();
		}
		return progressBarVO;
	}

	public void setProgressBarVO(ProgressBarVO progressBarVO) {
		this.progressBarVO = progressBarVO;
	}
	

}
