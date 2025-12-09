package controle.financeiro;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.arquitetura.OperacaoFuncionalidadeVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.OperacaoFuncionalidadeEnum;
import negocio.comuns.arquitetura.enumeradores.OrigemOperacaoFuncionalidadeEnum;
import negocio.comuns.financeiro.FluxoCaixaVO;
import negocio.comuns.financeiro.MovimentacaoFinanceiraVO;
import negocio.comuns.financeiro.enumerador.TipoContaCorrenteEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.dominios.SituacaoMovimentacaoFinanceiraEnum;
import negocio.facade.jdbc.arquitetura.ControleAcesso;

@Controller("MapaPendenciaMovimentacaoFinanceiraControle")
@Scope("viewScope")
@Lazy
public class MapaPendenciaMovimentacaoFinanceiraControle extends SuperControle {

	private static final long serialVersionUID = 1L;
	private List<MovimentacaoFinanceiraVO> listaMovimentacaoFinanceiraVO;
	private Date dataEmissaoInicial;
	private Date dataEmissaoFinal;
	private String situacaoMovimentacaoFinanceira;
	private List<SelectItem> listaSelectItemSituacaoMovimentacaoFinanceira;
	private MovimentacaoFinanceiraVO movimentacaoFinanceiraVO;
	private boolean abrirPainelDevolucaoChequeContaReceberNegociadoOuRecebido = false;
	private String nomeSacadoConsultaPorCheque;
	private String numeroConsultaPorCheque;
	private List<SelectItem> listaSelectItemFiltrarPor;
	private String filtraPor;
	private String modalLiberarContaCaixaFechada;
	private String usernameLiberarOperacaoFuncionalidade;
	private String senhaLiberarOperacaoFuncionalidade;

	public MapaPendenciaMovimentacaoFinanceiraControle() throws Exception {
		removerObjetoMemoria(this);
		setMensagemID("msg_entre_prmconsulta");
	}

	public String consultar() {
		try {
			validarDadosConsulta();
			setListaMovimentacaoFinanceiraVO(getFacadeFactory().getMovimentacaoFinanceiraFacade().consultarMapaPendenciaPorDataEmissaoResponsavelContaCorrenteSituacao(getSituacaoMovimentacaoFinanceira(), 
					getDataEmissaoInicial(), getDataEmissaoFinal(), getNumeroConsultaPorCheque(), getNomeSacadoConsultaPorCheque(), false, getUsuarioLogado()));
			if (getListaMovimentacaoFinanceiraVO().isEmpty()) {
				setMensagemID("msg_erro_dadosnaoencontrados", Uteis.ALERTA);
			} else {
				setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("mapaPendenciaMovimentacaoFinanceiraCons.xhtml");
	}

	public void finalizarMovimentacaoFinanceira() {
		try {
			setMovimentacaoFinanceiraVO((MovimentacaoFinanceiraVO) context().getExternalContext().getRequestMap().get("movimentacaoFinanceira"));
			executarFinalizacaoMovimentacaoFinanceira();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void selecionarMovimentacaoFinanceira() {
		try {
			setMovimentacaoFinanceiraVO((MovimentacaoFinanceiraVO) context().getExternalContext().getRequestMap().get("movimentacaoFinanceiraItens"));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void recusarSituacaoMovimentacaoFinanceira() {
		try {
			String motivoRecusa = getMovimentacaoFinanceiraVO().getMotivoRecusa();
			setMovimentacaoFinanceiraVO(getFacadeFactory().getMovimentacaoFinanceiraFacade().consultarPorChavePrimaria(getMovimentacaoFinanceiraVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			getMovimentacaoFinanceiraVO().setMotivoRecusa(motivoRecusa);
			getMovimentacaoFinanceiraVO().setSituacao("RE");
			getFacadeFactory().getMapaPendenciaMovimentacaoFinanceiraFacade().recusarMovimentacaoFinanceira(getMovimentacaoFinanceiraVO(), getUsuarioLogado());
			setMensagemID("msg_MapaPendenciaMovimentacaoFinanceira_movimentacaoFinanceiraRecusada", Uteis.SUCESSO);
		} catch (Exception e) {
			getMovimentacaoFinanceiraVO().setSituacao("PE");
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void consultarMovimentacaoFinanceiraItem() {
		try {
			setMovimentacaoFinanceiraVO((MovimentacaoFinanceiraVO) context().getExternalContext().getRequestMap().get("movimentacaoFinanceiraItens"));
			getMovimentacaoFinanceiraVO().setMovimentacaoFinanceiraItemVOs(getFacadeFactory().getMovimentacaoFinanceiraItemFacade().consultarPorMovimentacaoFinanceiraMapaPendenciaMovimentacaoFinanceira(getMovimentacaoFinanceiraVO().getCodigo(), getUsuarioLogado()));
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	/**
	 * Rotina responsável por preencher a combo de filtro por situação.
	 */
	public List<SelectItem> getListaSituacaoMovimentacaoFinanceira() {
		List<SelectItem> listaSituacaoMovimentacaoFinanceira = new ArrayList<SelectItem>(0);
		listaSituacaoMovimentacaoFinanceira.add(new SelectItem("TO", "Todas"));
		listaSituacaoMovimentacaoFinanceira.addAll(UtilPropriedadesDoEnum.getListaSelectItemDoEnum(SituacaoMovimentacaoFinanceiraEnum.class, false));
		return listaSituacaoMovimentacaoFinanceira;
	}

	private void validarDadosConsulta() throws Exception {
		if ((getDataEmissaoInicial() == null) || (getDataEmissaoFinal() == null)) {
			throw new ConsistirException("Informe o período de Emissão para efetuar a consulta.");
		}
	}

	public List<MovimentacaoFinanceiraVO> getListaMovimentacaoFinanceiraVO() {
		if (listaMovimentacaoFinanceiraVO == null) {
			listaMovimentacaoFinanceiraVO = new ArrayList<MovimentacaoFinanceiraVO>(0);
		}
		return listaMovimentacaoFinanceiraVO;
	}

	public void setListaMovimentacaoFinanceiraVO(List<MovimentacaoFinanceiraVO> listaMovimentacaoFinanceiraVO) {
		this.listaMovimentacaoFinanceiraVO = listaMovimentacaoFinanceiraVO;
	}

	public Date getDataEmissaoInicial() {
		if (dataEmissaoInicial == null) {
			dataEmissaoInicial = Uteis.getDataPrimeiroDiaMes(new Date());
		}
		return dataEmissaoInicial;
	}

	public void setDataEmissaoInicial(Date dataEmissaoInicial) {
		this.dataEmissaoInicial = dataEmissaoInicial;
	}

	public Date getDataEmissaoFinal() {
		if (dataEmissaoFinal == null) {
			dataEmissaoFinal = Uteis.getDataUltimoDiaMes(new Date());
		}
		return dataEmissaoFinal;
	}

	public void setDataEmissaoFinal(Date dataEmissaoFinal) {
		this.dataEmissaoFinal = dataEmissaoFinal;
	}

	public List<SelectItem> getListaSelectItemSituacaoMovimentacaoFinanceira() {
		if (listaSelectItemSituacaoMovimentacaoFinanceira == null) {
			listaSelectItemSituacaoMovimentacaoFinanceira = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemSituacaoMovimentacaoFinanceira;
	}

	public void setListaSelectItemSituacaoMovimentacaoFinanceira(List<SelectItem> listaSelectItemSituacaoMovimentacaoFinanceira) {
		this.listaSelectItemSituacaoMovimentacaoFinanceira = listaSelectItemSituacaoMovimentacaoFinanceira;
	}

	public String getSituacaoMovimentacaoFinanceira() {
		if (situacaoMovimentacaoFinanceira == null) {
			situacaoMovimentacaoFinanceira = "TO";
		}
		return situacaoMovimentacaoFinanceira;
	}

	public void setSituacaoMovimentacaoFinanceira(String situacaoMovimentacaoFinanceira) {
		this.situacaoMovimentacaoFinanceira = situacaoMovimentacaoFinanceira;
	}

	public MovimentacaoFinanceiraVO getMovimentacaoFinanceiraVO() {
		if (movimentacaoFinanceiraVO == null) {
			movimentacaoFinanceiraVO = new MovimentacaoFinanceiraVO();
		}
		return movimentacaoFinanceiraVO;
	}

	public void setMovimentacaoFinanceiraVO(MovimentacaoFinanceiraVO movimentacaoFinanceiraVO) {
		this.movimentacaoFinanceiraVO = movimentacaoFinanceiraVO;
	}

	public boolean getIsPossuiMovimentacoes() {
		return !getListaMovimentacaoFinanceiraVO().isEmpty();
	}

	public boolean getIsListaMovimentacaoFinanceiraItemMaiorDez() {
		return getMovimentacaoFinanceiraVO().getMovimentacaoFinanceiraItemVOs().size() > 9;
	}
	
	public void executarVerificacaoExisteDevolucaoChequeComContaReceberNegociadaOuRecebida() {
		try {
			setMovimentacaoFinanceiraVO((MovimentacaoFinanceiraVO) context().getExternalContext().getRequestMap().get("movimentacaoFinanceiraItens"));
			setAbrirPainelDevolucaoChequeContaReceberNegociadoOuRecebido(getFacadeFactory().getMapaPendenciaMovimentacaoFinanceiraFacade().executarVerificacaoExisteDevolucaoChequeComContaReceberNegociadaOuRecebida(getMovimentacaoFinanceiraVO(), getUsuarioLogado()));
			if (!isAbrirPainelDevolucaoChequeContaReceberNegociadoOuRecebido()) {
				
				FluxoCaixaVO fluxoCaixaContaCorrenteOrigem = new FluxoCaixaVO();
				FluxoCaixaVO fluxoCaixaContaCorrenteDestino = new FluxoCaixaVO();
				
		    	if (!getMovimentacaoFinanceiraVO().isSomenteContaDestino() && getMovimentacaoFinanceiraVO().getContaCorrenteOrigem().getTipoContaCorrenteEnum().equals(TipoContaCorrenteEnum.CAIXA)) {
		    		 fluxoCaixaContaCorrenteOrigem = getFacadeFactory().getFluxoCaixaFacade().consultarPorFluxoCaixaAberto(new Date(), getMovimentacaoFinanceiraVO().getContaCorrenteOrigem().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				}
		    	if (getMovimentacaoFinanceiraVO().getContaCorrenteDestino().getTipoContaCorrenteEnum().equals(TipoContaCorrenteEnum.CAIXA)) {
		    		fluxoCaixaContaCorrenteDestino = getFacadeFactory().getFluxoCaixaFacade().consultarPorFluxoCaixaAberto(new Date(), getMovimentacaoFinanceiraVO().getContaCorrenteDestino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				}
				if ((!Uteis.isAtributoPreenchido(fluxoCaixaContaCorrenteOrigem) && !getMovimentacaoFinanceiraVO().isSomenteContaDestino() && getMovimentacaoFinanceiraVO().getContaCorrenteOrigem().getTipoContaCorrenteEnum().equals(TipoContaCorrenteEnum.CAIXA))
					|| (!Uteis.isAtributoPreenchido(fluxoCaixaContaCorrenteDestino) && getMovimentacaoFinanceiraVO().getContaCorrenteDestino().getTipoContaCorrenteEnum().equals(TipoContaCorrenteEnum.CAIXA)) ) {
					setModalLiberarContaCaixaFechada("RichFaces.$('panelLiberarBloqueioMovimentacaoContaFechada').show()");
				}else {
					executarFinalizacaoMovimentacaoFinanceira();
				}
				
				
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public boolean isAbrirPainelDevolucaoChequeContaReceberNegociadoOuRecebido() {
		return abrirPainelDevolucaoChequeContaReceberNegociadoOuRecebido;
	}

	public void setAbrirPainelDevolucaoChequeContaReceberNegociadoOuRecebido(boolean abrirPainelDevolucaoChequeContaReceberNegociadoOuRecebido) {
		this.abrirPainelDevolucaoChequeContaReceberNegociadoOuRecebido = abrirPainelDevolucaoChequeContaReceberNegociadoOuRecebido;
	}
	
	public void excluirMovimentacaoFinanceiraItemDevolucaoChequeComContaReceberNegociadaOuRecebida() {
		try {
			getFacadeFactory().getMapaPendenciaMovimentacaoFinanceiraFacade().excluirMovimentacaoFinanceiraItemDevolucaoChequeComContaReceberNegociadaOuRecebida(getMovimentacaoFinanceiraVO(), getUsuarioLogado());
			setListaMovimentacaoFinanceiraVO(getFacadeFactory().getMovimentacaoFinanceiraFacade().consultarMapaPendenciaPorDataEmissaoResponsavelContaCorrenteSituacao(getSituacaoMovimentacaoFinanceira(),
					getDataEmissaoInicial(), getDataEmissaoFinal(), getNumeroConsultaPorCheque(), getNomeSacadoConsultaPorCheque(), false, getUsuarioLogado()));
			setMensagemID("msg_MapaPendenciaMovimentacaoFinanceira_movimentacaoFinanceiraFinalizada", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		} finally {
			setAbrirPainelDevolucaoChequeContaReceberNegociadoOuRecebido(false);
		}
	}
	
	private void executarFinalizacaoMovimentacaoFinanceira() throws Exception {
			
		getFacadeFactory().getMapaPendenciaMovimentacaoFinanceiraFacade().finalizarMovimentacaoFinanceira(getMovimentacaoFinanceiraVO(), getUsuarioLogado());
		setAbrirPainelDevolucaoChequeContaReceberNegociadoOuRecebido(false);
		setListaMovimentacaoFinanceiraVO(getFacadeFactory().getMovimentacaoFinanceiraFacade().consultarMapaPendenciaPorDataEmissaoResponsavelContaCorrenteSituacao(getSituacaoMovimentacaoFinanceira(),
				getDataEmissaoInicial(), getDataEmissaoFinal(), getNumeroConsultaPorCheque(), getNomeSacadoConsultaPorCheque(), false, getUsuarioLogado()));
		setMensagemID("msg_MapaPendenciaMovimentacaoFinanceira_movimentacaoFinanceiraFinalizada", Uteis.SUCESSO);
	}

	public String getNomeSacadoConsultaPorCheque() {
		if (nomeSacadoConsultaPorCheque == null) {
			nomeSacadoConsultaPorCheque = "";
		}
		return nomeSacadoConsultaPorCheque;
	}

	public void setNomeSacadoConsultaPorCheque(String nomeSacadoConsultaPorCheque) {
		this.nomeSacadoConsultaPorCheque = nomeSacadoConsultaPorCheque;
	}

	public String getNumeroConsultaPorCheque() {
		if (numeroConsultaPorCheque == null) {
			numeroConsultaPorCheque = "";
		}
		return numeroConsultaPorCheque;
	}

	public void setNumeroConsultaPorCheque(String numeroConsultaPorCheque) {
		this.numeroConsultaPorCheque = numeroConsultaPorCheque;
	}

	public List<SelectItem> getListaSelectItemFiltrarPor() {
		if (listaSelectItemFiltrarPor == null) {
			listaSelectItemFiltrarPor = Arrays.asList(new SelectItem("", ""), new SelectItem("cheque", "Cheque"));
		}
		return listaSelectItemFiltrarPor;
	}

	public void setListaSelectItemFiltrarPor(List<SelectItem> listaSelectItemFiltrarPor) {
		this.listaSelectItemFiltrarPor = listaSelectItemFiltrarPor;
	}
	
	public String getFiltraPor() {
		if (filtraPor == null) {
			filtraPor = "";
		}
		return filtraPor;
	}

	public void setFiltraPor(String filtraPor) {
		this.filtraPor = filtraPor;
	}

	public void limparCamposConsultaConformeFiltro() {
		if (!getFiltraPor().equals("cheque")) {
			setNomeSacadoConsultaPorCheque("");
			setNumeroConsultaPorCheque("");
		}
	}
	
	public boolean getApresentarCamposConsultaCheque() {
		return getFiltraPor().equals("cheque");
	}

	public String getModalLiberarContaCaixaFechada() {
		if (modalLiberarContaCaixaFechada == null) {
			modalLiberarContaCaixaFechada = "";
		}
		return modalLiberarContaCaixaFechada;
	}

	public void setModalLiberarContaCaixaFechada(String modalLiberarContaCaixaFechada) {
		this.modalLiberarContaCaixaFechada = modalLiberarContaCaixaFechada;
	}

	public String getUsernameLiberarOperacaoFuncionalidade() {
		if (usernameLiberarOperacaoFuncionalidade == null) {
			usernameLiberarOperacaoFuncionalidade = "";
		}
		return usernameLiberarOperacaoFuncionalidade;
	}

	public void setUsernameLiberarOperacaoFuncionalidade(String usernameLiberarOperacaoFuncionalidade) {
		this.usernameLiberarOperacaoFuncionalidade = usernameLiberarOperacaoFuncionalidade;
	}

	public String getSenhaLiberarOperacaoFuncionalidade() {
		if (senhaLiberarOperacaoFuncionalidade == null) {
			senhaLiberarOperacaoFuncionalidade = "";
		}
		return senhaLiberarOperacaoFuncionalidade;
	}

	public void setSenhaLiberarOperacaoFuncionalidade(String senhaLiberarOperacaoFuncionalidade) {
		this.senhaLiberarOperacaoFuncionalidade = senhaLiberarOperacaoFuncionalidade;
	}
	
	public synchronized void autorizarMovimentacaoFinanceiraContaCaixaFechado() {
		try {
			UsuarioVO usuarioVerif = ControleAcesso.verificarLoginUsuario(this.getUsernameLiberarOperacaoFuncionalidade(), this.getSenhaLiberarOperacaoFuncionalidade(), true, Uteis.NIVELMONTARDADOS_TODOS);
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("PermiteAutorizacaoMovimentacaoFinanceiraContaCaixaOrigemFechada", usuarioVerif);
			getFacadeFactory().getMapaPendenciaMovimentacaoFinanceiraFacade().autorizarMovimentacaoFinanceiraContaCaixaFechado(getMovimentacaoFinanceiraVO() , getUsuarioLogado());
			setAbrirPainelDevolucaoChequeContaReceberNegociadoOuRecebido(false);
			setListaMovimentacaoFinanceiraVO(getFacadeFactory().getMovimentacaoFinanceiraFacade().consultarMapaPendenciaPorDataEmissaoResponsavelContaCorrenteSituacao(getSituacaoMovimentacaoFinanceira(),
					getDataEmissaoInicial(), getDataEmissaoFinal(), getNumeroConsultaPorCheque(), getNomeSacadoConsultaPorCheque(), false, getUsuarioLogado()));
			setMensagemID("msg_MapaPendenciaMovimentacaoFinanceira_movimentacaoFinanceiraFinalizada", Uteis.SUCESSO);
			setModalLiberarContaCaixaFechada("RichFaces.$('panelLiberarBloqueioMovimentacaoContaFechada').hide()");
		} catch (Exception e) {
			movimentacaoFinanceiraVO.setSituacao("PE");
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
}
