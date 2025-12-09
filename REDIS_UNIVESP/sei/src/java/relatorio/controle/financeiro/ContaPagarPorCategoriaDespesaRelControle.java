package relatorio.controle.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.financeiro.CategoriaDespesaVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.utilitarias.Uteis;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.financeiro.ContaPagarPorCategoriaDespesaRelVO;
import relatorio.negocio.jdbc.financeiro.enumeradores.ContaPagarFiltrosEnum;

/**
 * 
 * @author Carlos
 */
@Controller("ContaPagarPorCategoriaDespesaRelControle")
@Scope("viewScope")
@Lazy
public class ContaPagarPorCategoriaDespesaRelControle extends SuperControleRelatorio {

	private CategoriaDespesaVO categoriaDespesaVO;
	private Date dataInicio;
	private Date dataFim;
	private String campoConsultaCentroDespesa;
	private String valorConsultaCentroDespesa;
	private List listaConsultaCentroDespesa;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private String situacao;
	private String filtroAPagar;
	private String filtroPago;
	private String filtroCancelada;
	private String filtroPagoParcialmente;
	private List<SelectItem> listaSelectItemFiltroContaAPaga;
	private List<SelectItem> listaSelectItemFiltroPaga;
	private List<SelectItem> listaSelectItemLayout;
	private String layout;
	private String possuiConta;
	private Boolean apresentarFiltroContaCorrente;
	private Integer codigoContaCorrente;
	private List<SelectItem> listaSelectItemContaCorrente;
	private Boolean trazerContasSubcategoria;

	public ContaPagarPorCategoriaDespesaRelControle() {
		montarListaSelectItemUnidadeEnsino();
		montarListaSelectItemContaCorrente();
	}
	
	
	public void montarListaSelectItemContaCorrente() {
		try {
			montarListaSelectItemContaCorrente("");
		} catch (Exception e) {
			//System.out.println("MENSAGEM => " + e.getMessage());
			
		}
	}
	
	public void montarListaSelectItemContaCorrente(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarContaCorrentePorNumero(prm);
			i = resultadoConsulta.iterator();
			List<SelectItem> objs = new ArrayList<SelectItem>(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				ContaCorrenteVO obj = (ContaCorrenteVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNumero().toString()));
			}
			setListaSelectItemContaCorrente(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}
	
	
	public List consultarContaCorrentePorNumero(String numeroPrm) throws Exception {
		List lista = getFacadeFactory().getContaCorrenteFacade().consultarPorNumero(numeroPrm, getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
		return lista;
	}
	
	public List<SelectItem> getListaContaCorrente() {
		List<SelectItem> lista = new ArrayList<SelectItem>(0);
		lista.add(new SelectItem("TODAS", "Todas"));
		lista.add(new SelectItem("FILTRAR", "Filtrar"));
		return lista;
	}
	
	public void validarFiltroContaCorrente() {
		if (getPossuiConta().equals("FILTRAR")) {
			setApresentarFiltroContaCorrente(true);
		} else {
			setCodigoContaCorrente(0);
			setApresentarFiltroContaCorrente(false);
		}
	}

	private void inicializarDadosControle() {
		try {
			setDataInicio(Uteis.getDataPrimeiroDiaMes(new Date()));
			setDataFim(Uteis.getDataUltimoDiaMes(new Date()));
			montarListaSelectItemUnidadeEnsino();
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}

	}

	public void gerarRelatorioSintetico(TipoRelatorioEnum tipoRelatorioEnum) {
		List<ContaPagarPorCategoriaDespesaRelVO> listaContaPagarRelVO = null;
		try {
			listaContaPagarRelVO = getFacadeFactory().getContaPagarPorCategoriaDespesaFacade().criarObjeto(getCategoriaDespesaVO().getCodigo(), getUnidadeEnsinoVO().getCodigo(), getDataInicio(), getDataFim(), getFiltroAPagar(), getFiltroPago(), getFiltroPagoParcialmente(),getFiltroCancelada(),getPossuiConta(),getCodigoContaCorrente(),getTrazerContasSubcategoria());
			if (!listaContaPagarRelVO.isEmpty()) {
				if (getLayout().equals("sintetico")) {
					getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getContaPagarPorCategoriaDespesaFacade().designIReportRelatorioSintetico());
				} else {
					getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getContaPagarPorCategoriaDespesaFacade().designIReportRelatorio());
				}
				getSuperParametroRelVO().setTipoRelatorioEnum(tipoRelatorioEnum);
				getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getContaPagarPorCategoriaDespesaFacade().caminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio("Relatório de Contas a Pagar Por Categoria Despesa");
				getSuperParametroRelVO().setListaObjetos(listaContaPagarRelVO);
				Double valorTotal = listaContaPagarRelVO.stream().map(ContaPagarPorCategoriaDespesaRelVO::getValor).reduce(Double::sum).orElse(0.0);
				Double valorTotalPago = listaContaPagarRelVO.stream().map(ContaPagarPorCategoriaDespesaRelVO::getValorPago).reduce(Double::sum).orElse(0.0);
				getSuperParametroRelVO().adicionarParametro("valorTotal", valorTotal);
				getSuperParametroRelVO().adicionarParametro("valorTotalPago", valorTotalPago);
				getSuperParametroRelVO().adicionarParametro("porcentagemValorTotal",(valorTotalPago * 100) / valorTotal);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getContaPagarPorCategoriaDespesaFacade().caminhoBaseRelatorio());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setPeriodo(String.valueOf(Uteis.getData(getDataInicio())) + "  até  " + String.valueOf(Uteis.getData(getDataFim())));
				if (getUnidadeEnsinoVO().getCodigo().intValue() != 0) {
					setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCodigo(getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado()));
					getSuperParametroRelVO().setUnidadeEnsino(getUnidadeEnsinoVO().getNome());
				} else {
					getSuperParametroRelVO().setUnidadeEnsino("Todas");
				}
				if (getCategoriaDespesaVO() == null || getCategoriaDespesaVO().getCodigo() == 0) {
					getSuperParametroRelVO().setCategoriaDespesa(ContaPagarFiltrosEnum.TODOS.getDescricao());
				} else {
					getSuperParametroRelVO().setCategoriaDespesa(getCategoriaDespesaVO().getDescricao());
				}

				realizarImpressaoRelatorio();
				removerObjetoMemoria(this);
				inicializarDadosControle();
				setMensagemID("msg_relatorio_ok");
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {

			Uteis.liberarListaMemoria(listaContaPagarRelVO);
		}
	}

	public void imprimirPDF() {
		gerarRelatorioSintetico(TipoRelatorioEnum.PDF);
	}

	public void imprimirEXCEL() {
		gerarRelatorioSintetico(TipoRelatorioEnum.EXCEL);
	}

	public void montarListaSelectItemUnidadeEnsino() {
		try {
			montarListaSelectItemUnidadeEnsino("");
		} catch (Exception e) {
			//System.out.println("MENSAGEM => " + e.getMessage());
			
		}

	}

	public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
		List<UnidadeEnsinoVO> resultadoConsulta = null;
		Iterator<UnidadeEnsinoVO> i = null;
		try {
			resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
			i = resultadoConsulta.iterator();
			List<SelectItem> objs = new ArrayList<SelectItem>(0);
			if (super.getUnidadeEnsinoLogado().getCodigo().equals(0)) {
				objs.add(new SelectItem(0, ""));
			}
			while (i.hasNext()) {
				UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
			}
			setListaSelectItemUnidadeEnsino(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	public List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
		List<UnidadeEnsinoVO> lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
		return lista;
	}

	public void consultarCentroDespesa() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaCentroDespesa().equals("descricao")) {
				objs = getFacadeFactory().getCategoriaDespesaFacade().consultarPorDescricao(getValorConsultaCentroDespesa(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			if (getCampoConsultaCentroDespesa().equals("identificadorCentroDespesa")) {
				objs = getFacadeFactory().getCategoriaDespesaFacade().consultarPorIdentificadorCategoriaDespesa(getValorConsultaCentroDespesa(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			setListaConsultaCentroDespesa(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCentroDespesa(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> getTipoConsultaComboCentroDespesa() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("descricao", "Descrição"));
		itens.add(new SelectItem("identificadorCentroDespesa", "Identificador Centro Despesa"));
		return itens;
	}

	public List<SelectItem> getTipoConsultaComboSituacao() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("TO", "Todas"));
		itens.add(new SelectItem("AP", "A Pagar"));
		itens.add(new SelectItem("PA", "Paga"));
		return itens;
	}

	public void selecionarCentroDespesa() {
		CategoriaDespesaVO obj = (CategoriaDespesaVO) context().getExternalContext().getRequestMap().get("centroDespesa");
		setCategoriaDespesaVO(obj);
//		setCampoConsultaCentroDespesa("");
//		setValorConsultaCentroDespesa("");
//		setListaConsultaCentroDespesa(new ArrayList<>(0));
//		anularDataModelo();
		}
	

	public void limparDadosCentroDespesa() {
		setCategoriaDespesaVO(null);
	}

	public void limparDadosListaCentroDespesa() {
		setCampoConsultaCentroDespesa("");
		setValorConsultaCentroDespesa("");
		setListaConsultaCentroDespesa(new ArrayList(0));
	}

	public Date getDataInicio() {
		if (dataInicio == null) {
			dataInicio = Uteis.getDataPrimeiroDiaMes(new Date());
		}
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		if (dataFim == null) {
			dataFim = Uteis.getDataUltimoDiaMes(new Date());
		}
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public CategoriaDespesaVO getCategoriaDespesaVO() {
		if (categoriaDespesaVO == null) {
			categoriaDespesaVO = new CategoriaDespesaVO();
		}
		return categoriaDespesaVO;
	}

	public void setCategoriaDespesaVO(CategoriaDespesaVO categoriaDespesaVO) {
		this.categoriaDespesaVO = categoriaDespesaVO;
	}

	public String getCampoConsultaCentroDespesa() {
		if (campoConsultaCentroDespesa == null) {
			campoConsultaCentroDespesa = "";
		}
		return campoConsultaCentroDespesa;
	}

	public void setCampoConsultaCentroDespesa(String campoConsultaCentroDespesa) {
		this.campoConsultaCentroDespesa = campoConsultaCentroDespesa;
	}

	public String getValorConsultaCentroDespesa() {
		if (valorConsultaCentroDespesa == null) {
			valorConsultaCentroDespesa = "";
		}
		return valorConsultaCentroDespesa;
	}

	public void setValorConsultaCentroDespesa(String valorConsultaCentroDespesa) {
		this.valorConsultaCentroDespesa = valorConsultaCentroDespesa;
	}

	public List getListaConsultaCentroDespesa() {
		if (listaConsultaCentroDespesa == null) {
			listaConsultaCentroDespesa = new ArrayList(0);
		}
		return listaConsultaCentroDespesa;
	}

	public void setListaConsultaCentroDespesa(List listaConsultaCentroDespesa) {
		this.listaConsultaCentroDespesa = listaConsultaCentroDespesa;
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

	public List getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList(0);
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public String getSituacao() {
		if (situacao == null) {
			situacao = "";
		}
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public String getFiltroAPagar() {
		if (filtroAPagar == null) {
			filtroAPagar = "contapagar.datavencimento";
		}
		return filtroAPagar;
	}

	public void setFiltroAPagar(String filtroAPagar) {
		this.filtroAPagar = filtroAPagar;
	}

	public String getFiltroPago() {
		if (filtroPago == null) {
			filtroPago = "contapagar.datavencimento";
		}
		return filtroPago;
	}

	public void setFiltroPago(String filtroPago) {
		this.filtroPago = filtroPago;
	}
	
	

	public String getFiltroCancelada() {
		if (filtroCancelada == null) {
			filtroCancelada = "contapagar.datavencimento";
		}
		return filtroCancelada;
	}


	public void setFiltroCancelada(String filtroCancelada) {
		this.filtroCancelada = filtroCancelada;
	}


	public String getFiltroPagoParcialmente() {
		if (filtroPagoParcialmente == null) {
			filtroPagoParcialmente = "contapagar.datavencimento";
		}
		return filtroPagoParcialmente;
	}
	
	public void scrollerListener(DataScrollEvent DataScrollEvent) throws Exception {
        getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
        getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
        consultar();
    }

	public void setFiltroPagoParcialmente(String filtroPagoParcialmente) {
		this.filtroPagoParcialmente = filtroPagoParcialmente;
	}

	public List<SelectItem> getListaSelectItemFiltroContaAPaga() {
		if (listaSelectItemFiltroContaAPaga == null) {
			listaSelectItemFiltroContaAPaga = new ArrayList<SelectItem>(0);
			listaSelectItemFiltroContaAPaga.add(new SelectItem("contapagar.datavencimento", "Data Vencimento"));
			listaSelectItemFiltroContaAPaga.add(new SelectItem("contapagar.dataFatoGerador", "Data Competência"));
			listaSelectItemFiltroContaAPaga.add(new SelectItem("naoFiltrar", "Não Filtrar"));
		}
		return listaSelectItemFiltroContaAPaga;
	}

	public void setListaSelectItemFiltroContaAPaga(List<SelectItem> listaSelectItemFiltroContaAPaga) {
		this.listaSelectItemFiltroContaAPaga = listaSelectItemFiltroContaAPaga;
	}

	public List<SelectItem> getListaSelectItemFiltroPaga() {
		if (listaSelectItemFiltroPaga == null) {
			listaSelectItemFiltroPaga = new ArrayList<SelectItem>(0);
			listaSelectItemFiltroPaga.add(new SelectItem("contapagar.datavencimento", "Data Vencimento"));
			listaSelectItemFiltroPaga.add(new SelectItem("negociacaopagamento.data", "Data Pagamento"));
			listaSelectItemFiltroPaga.add(new SelectItem("contapagar.dataFatoGerador", "Data Competência"));
			listaSelectItemFiltroPaga.add(new SelectItem("naoFiltrar", "Não Filtrar"));
		}
		return listaSelectItemFiltroPaga;
	}

	public void setListaSelectItemFiltroPaga(List<SelectItem> listaSelectItemFiltroPaga) {
		this.listaSelectItemFiltroPaga = listaSelectItemFiltroPaga;
	}

	public List<SelectItem> getListaSelectItemLayout() {
		if (listaSelectItemLayout == null) {
			listaSelectItemLayout = new ArrayList<SelectItem>(0);
			listaSelectItemLayout.add(new SelectItem("analitico", "Analítico"));
			listaSelectItemLayout.add(new SelectItem("sintetico", "Sintético"));
		}
		return listaSelectItemLayout;
	}

	public void setListaSelectItemLayout(List<SelectItem> listaSelectItemLayout) {
		this.listaSelectItemLayout = listaSelectItemLayout;
	}

	public String getLayout() {
		if (layout == null) {
			layout = "analitico";
		}
		return layout;
	}

	public void setLayout(String layout) {
		this.layout = layout;
	}
	
	public String getPossuiConta() {

		if (possuiConta == null) {
			possuiConta = "TODAS";
		}

		return possuiConta;
	}

	public void setPossuiConta(String possuiConta) {
		this.possuiConta = possuiConta;
	}
	
	public Boolean getApresentarFiltroContaCorrente() {
		if(apresentarFiltroContaCorrente == null){
			apresentarFiltroContaCorrente = Boolean.FALSE;
		}
		
		return apresentarFiltroContaCorrente;
	}

	public void setApresentarFiltroContaCorrente(Boolean apresentarFiltroContaCorrente) {
		this.apresentarFiltroContaCorrente = apresentarFiltroContaCorrente;
	}
	
	
	public Integer getCodigoContaCorrente() {

		if (codigoContaCorrente == null) {
			codigoContaCorrente = 0;
		}

		return codigoContaCorrente;
	}

	public void setCodigoContaCorrente(Integer codigoContaCorrente) {
		this.codigoContaCorrente = codigoContaCorrente;
	}

	
	public List getListaSelectItemContaCorrente() {
		if (listaSelectItemContaCorrente == null) {
			listaSelectItemContaCorrente = new ArrayList(0);
		}
		return listaSelectItemContaCorrente;
	}

	public void setListaSelectItemContaCorrente(List listaSelectItemContaCorrente) {
		this.listaSelectItemContaCorrente = listaSelectItemContaCorrente;
	}


	public Boolean getTrazerContasSubcategoria() {
		if (trazerContasSubcategoria == null) {
			trazerContasSubcategoria = true;
		}
		return trazerContasSubcategoria;
	}


	public void setTrazerContasSubcategoria(Boolean trazerContasSubcategoria) {
		this.trazerContasSubcategoria = trazerContasSubcategoria;
	}
	
	public Boolean getApresentarTrazerContasSubcategoria() {
		if (Uteis.isAtributoPreenchido(getCategoriaDespesaVO())) {
			return true;
		}else {
	    	return false;
    	}
	}
	public void anularDataModelo() {
		setControleConsultaOtimizado(null);
	}
}
