/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.controle.compras;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.compras.CategoriaProdutoVO;
import negocio.comuns.compras.ProdutoServicoVO;
import negocio.comuns.utilitarias.Uteis;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.compras.EstoqueRelVO;
import relatorio.negocio.jdbc.compras.EstoqueRel;

import static relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum.PDF;
import static relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum.EXCEL;

/**
 * 
 * @author Manoel
 */
@Controller("EstoqueRelControle")
@Scope("viewScope")
@Lazy
public class EstoqueRelControle extends SuperControleRelatorio {

	private static final long serialVersionUID = 1L;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private String campoConsultaProduto;
	private String valorConsultaProduto;
	private List<SelectItem> listaConsultaProduto;
	private List<SelectItem> listaConsultaCategoriaProduto;
	private ProdutoServicoVO produtoServicoVO;
	private CategoriaProdutoVO categoriaProdutoVO;
	private List listaSelectItemUnidadeEnsino;
	private Date dataInicio;
	private Date dataFim;
	private String situacaoEstoque;
	private String campoConsultaCategoriaProduto;
	private String valorConsultaCategoriaProduto;
	private String valorConsultaTipoProduto;
	private String valorConsultaSituacaoProduto;
	private String tipoLayout;
	private Boolean trazerProdutoZerado;
	private Boolean trazerProdutoSaldoeZerado;

	public EstoqueRelControle() {
		this.montarListaSelectItemUnidadeEnsino();
	}

	public void consultarProduto() {
		try {

			List objs = new ArrayList(0);
			if (getCampoConsultaProduto().equals("nome")) {
				objs = getFacadeFactory().getProdutoServicoFacade().consultarPorNome(getValorConsultaProduto(), null,
						true, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getCampoConsultaProduto().equals("nomeCategoriaProduto")) {
				objs = getFacadeFactory().getProdutoServicoFacade().consultarPorNomeCategoriaProduto(
						getValorConsultaProduto(), true, null, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			setListaConsultaProduto(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaProduto(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}

	}

	public void consultarCategoriaProduto() {
		try {

			List objs = new ArrayList(0);
			if (getCampoConsultaCategoriaProduto().equals("nome")) {
				objs = getFacadeFactory().getCategoriaProdutoFacade().consultarPorNome(
						getValorConsultaCategoriaProduto(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getCampoConsultaCategoriaProduto().equals("nomeCategoriaDespesa")) {
				objs = getFacadeFactory().getCategoriaProdutoFacade()
						.consultarCategoriaProdutoPassandoDescricaoCategoriaDespesa(getValorConsultaCategoriaProduto(),
								true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			setListaConsultaCategoriaProduto(objs);
			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			setListaConsultaCategoriaProduto(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}

	}

	public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
		List<UnidadeEnsinoVO> resultadoConsulta = null;
		Iterator<UnidadeEnsinoVO> i = null;
		try {
			resultadoConsulta = getFacadeFactory().getUnidadeEnsinoFacade()
					.consultarPorUsuarioUnidadeEnsinoVinculadaAoUsuario(getUsuarioLogado().getCodigo(), false,
							Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			if (resultadoConsulta.isEmpty()) {
				resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
			}
			i = resultadoConsulta.iterator();
			List<SelectItem> objs = new ArrayList<SelectItem>(0);
			if (getUnidadeEnsinoLogado().getCodigo() == 0) {
				objs.add(new SelectItem(0, ""));
			}
			while (i.hasNext()) {
				UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
				if (getUnidadeEnsinoLogado().getCodigo() == 0) {
					objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
				} else if (obj.getCodigo().equals(getUnidadeEnsinoLogado().getCodigo())) {
					objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
					setUnidadeEnsinoVO(obj);
				}
			}
			setListaSelectItemUnidadeEnsino(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	public void montarListaSelectItemUnidadeEnsino() {
		try {
			montarListaSelectItemUnidadeEnsino("");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
		List<UnidadeEnsinoVO> lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm,
				super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		return lista;
	}

	public void selecionarProduto() throws Exception {
		ProdutoServicoVO obj = (ProdutoServicoVO) context().getExternalContext().getRequestMap().get("produtoItens");
		setProdutoServicoVO(obj);
		setCategoriaProdutoVO(obj.getCategoriaProduto());
		setValorConsultaTipoProduto(obj.getTipoProdutoServicoEnum().name());
		setValorConsultaSituacaoProduto(obj.getSituacao());
		getListaConsultaProduto().clear();
		valorConsultaProduto = null;
		campoConsultaProduto = null;
	}

	public void selecionarCategoriaProduto() throws Exception {
		CategoriaProdutoVO obj = (CategoriaProdutoVO) context().getExternalContext().getRequestMap()
				.get("categoriaItens");
		setCategoriaProdutoVO(obj);
		getListaConsultaCategoriaProduto().clear();
		valorConsultaCategoriaProduto = null;
		campoConsultaCategoriaProduto = null;
	}

	public void imprimirExcel() {
		imprimirRelatorio(EXCEL);
	}

	public void imprimirPDF() {
		imprimirRelatorio(PDF);
	}
	
	private void imprimirRelatorio(TipoRelatorioEnum tipoRelatorioEnum) {
		List<EstoqueRelVO> listaObjetos = null;
		try {
			if (getTipoLayout().equals("AE")) {
				listaObjetos = getFacadeFactory().getEstoqueRelInterfaceFacade().criarObjeto(
						getUnidadeEnsinoVO().getCodigo(), getProdutoServicoVO().getCodigo(),
						getCategoriaProdutoVO().getCodigo(), getDataInicio(), getDataFim(), getSituacaoEstoque() , getTrazerProdutoZerado() , getTrazerProdutoSaldoeZerado());
				registrarAtividadeUsuario(getUsuarioLogado(), "EstoqueRelControle",
						"Inicializando Geração de Relatório de Estoque", "Emitindo Relatório");
				getSuperParametroRelVO().setTituloRelatorio("Relatório Analítico por Data Entrada");
				getSuperParametroRelVO().setNomeDesignIreport(EstoqueRel.getDesign());
				getSuperParametroRelVO().setSubReport_Dir(EstoqueRel.getCaminhoBaseRelatorio());
			} else if (getTipoLayout().equals("EZ")) {
				listaObjetos = getFacadeFactory().getEstoqueRelInterfaceFacade().criarObjetoAnaliticoPorProduto(
						getUnidadeEnsinoVO(), getProdutoServicoVO(), getCategoriaProdutoVO(),
						getValorConsultaTipoProduto(), getValorConsultaSituacaoProduto(), getTrazerProdutoZerado(), false);
				registrarAtividadeUsuario(getUsuarioLogado(), "EstoqueRelControle",           
						"Inicializando Geração de Relatório de Estoque", "Emitindo Relatório");
				getSuperParametroRelVO().setTituloRelatorio("Produtos Com Estoque Zerado");
				getSuperParametroRelVO().setNomeDesignIreport(EstoqueRel.getDesignAnaliticoPorProdutoZerado());
			} else {
				listaObjetos = getFacadeFactory().getEstoqueRelInterfaceFacade().criarObjetoAnaliticoPorProduto(
						getUnidadeEnsinoVO(), getProdutoServicoVO(), getCategoriaProdutoVO(),
						getValorConsultaTipoProduto(), getValorConsultaSituacaoProduto(), getTrazerProdutoZerado(), getTrazerProdutoSaldoeZerado());
				registrarAtividadeUsuario(getUsuarioLogado(), "EstoqueRelControle",
						"Inicializando Geração de Relatório de Estoque", "Emitindo Relatório");
				getSuperParametroRelVO().setTituloRelatorio("Relatório Analítico por Produto");
				getSuperParametroRelVO().setNomeDesignIreport(EstoqueRel.getDesignAnaliticoPorProduto());
			}
			if (!listaObjetos.isEmpty()) {
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setListaObjetos(listaObjetos);
				getSuperParametroRelVO().setQuantidade(listaObjetos.size());
				getSuperParametroRelVO().setCaminhoBaseRelatorio(EstoqueRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade()
						.consultaRapidaPorCodigo(getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado())
						.getNome());
				getSuperParametroRelVO().setTipoRelatorioEnum(tipoRelatorioEnum);
				registrarAtividadeUsuario(getUsuarioLogado(), "EstoqueRelControle",
						"Finalizando Geração de Relatório de Estoque", "Finalizando Relatório");
				getSuperParametroRelVO().adicionarParametro("produto", getProdutoServicoVO().getNome());
				getSuperParametroRelVO().adicionarParametro("categoria", getCategoriaProdutoVO().getNome());
				getSuperParametroRelVO().adicionarParametro("tipoProduto", getValorConsultaTipoProduto());
				getSuperParametroRelVO().adicionarParametro("situacaoProduto", getValorConsultaSituacaoProduto());
				this.montaPeriodo();
				getSuperParametroRelVO().adicionarParametro("estoqueMinimo", this.situacaoEstoque());
				getSuperParametroRelVO().adicionarParametro("listaEstoque",
						getFacadeFactory().getEstoqueRelInterfaceFacade().montaListaUnidadeMedidaEstoque(
								getUnidadeEnsinoVO().getCodigo(), getProdutoServicoVO().getCodigo(),
								getCategoriaProdutoVO().getCodigo(), getDataInicio(), getDataFim(),
								getSituacaoEstoque()));
				realizarImpressaoRelatorio();
				setMensagemID("msg_relatorio_ok");
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			Uteis.liberarListaMemoria(listaObjetos);
		}
	}

	public void montaPeriodo() {
		String periodo = "";
		if ((getDataInicio() != null) && (getDataFim() != null)) {
			periodo = Uteis.getData(getDataInicio(), "dd/MM/yyyy") + "  à  "
					+ Uteis.getData(getDataFim(), "dd/MM/yyyy");
			getSuperParametroRelVO().adicionarParametro("periodo", periodo);
		} else if ((getDataInicio() == null) && (getDataFim() != null)) {
			periodo = "Todas as datas " + "  à  " + Uteis.getData(getDataFim(), "dd/MM/yyyy");
			getSuperParametroRelVO().adicionarParametro("periodo", periodo);
		} else if ((getDataInicio() != null) && (getDataFim() == null)) {
			periodo = Uteis.getData(getDataInicio(), "dd/MM/yyyy") + "  à  " + "Todas as datas";
			getSuperParametroRelVO().adicionarParametro("periodo", periodo);
		} else {
			getSuperParametroRelVO().adicionarParametro("periodo", "");
		}

	}

	public List<SelectItem> getTipoConsultaComboProduto() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("nomeCategoriaProduto", "Categoria Produto"));

		return itens;
	}

	public List<SelectItem> getTipoConsultaComboCategoriaProduto() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("nomeCategoriaDespesa", "Categoria Despesa"));

		return itens;
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

	public String getCampoConsultaProduto() {
		if (campoConsultaProduto == null) {
			campoConsultaProduto = "";
		}
		return campoConsultaProduto;
	}

	public void setCampoConsultaProduto(String campoConsultaProduto) {
		this.campoConsultaProduto = campoConsultaProduto;
	}

	public String getValorConsultaProduto() {
		if (valorConsultaProduto == null) {
			valorConsultaProduto = "";
		}
		return valorConsultaProduto;
	}

	public void setValorConsultaProduto(String valorConsultaProduto) {
		this.valorConsultaProduto = valorConsultaProduto;
	}

	public List<SelectItem> getListaConsultaProduto() {
		if (listaConsultaProduto == null) {
			listaConsultaProduto = new ArrayList<SelectItem>(0);
		}
		return listaConsultaProduto;
	}

	public void setListaConsultaProduto(List<SelectItem> listaConsultaProduto) {
		this.listaConsultaProduto = listaConsultaProduto;
	}

	public List<SelectItem> getListaConsultaCategoriaProduto() {
		if (listaConsultaCategoriaProduto == null) {
			listaConsultaCategoriaProduto = new ArrayList<SelectItem>(0);
		}
		return listaConsultaCategoriaProduto;
	}

	public void setListaConsultaCategoriaProduto(List<SelectItem> listaConsultaCategoriaProduto) {
		this.listaConsultaCategoriaProduto = listaConsultaCategoriaProduto;
	}

	public ProdutoServicoVO getProdutoServicoVO() {
		if (produtoServicoVO == null) {
			produtoServicoVO = new ProdutoServicoVO();
		}
		return produtoServicoVO;
	}

	public void setProdutoServicoVO(ProdutoServicoVO produtoServicoVO) {
		this.produtoServicoVO = produtoServicoVO;
	}

	public CategoriaProdutoVO getCategoriaProdutoVO() {
		if (categoriaProdutoVO == null) {
			categoriaProdutoVO = new CategoriaProdutoVO();
		}
		return categoriaProdutoVO;
	}

	public void setCategoriaProdutoVO(CategoriaProdutoVO categoriaProdutoVO) {
		this.categoriaProdutoVO = categoriaProdutoVO;
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

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public String getSituacaoEstoque() {
		if (situacaoEstoque == null) {
			situacaoEstoque = "";
		}
		return situacaoEstoque;
	}

	public void setSituacaoEstoque(String situacaoEstoque) {
		this.situacaoEstoque = situacaoEstoque;
	}

	public List<SelectItem> getTipoSituacaoEstoque() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("TODOS", "Todos"));
		itens.add(new SelectItem("AEM", "Abaixo Estoque Mínimo"));
		itens.add(new SelectItem("ACEM", "Acima Estoque Mínimo"));
		return itens;
	}

	public String situacaoEstoque() {
		if (getSituacaoEstoque().equals("TODOS")) {
			return "Todos";
		}
		if (getSituacaoEstoque().equals("AEM")) {
			return "Abaixo Estoque Mínimo";
		}
		if (getSituacaoEstoque().equals("ACEM")) {
			return "Acima Estoque Mínimo";
		}
		return "";
	}

	public void limparDadosServicoProduto() {
		setProdutoServicoVO(null);
	}

	public void limparDadosCategoriaProduto() {
		setCategoriaProdutoVO(null);
	}

	public String getCampoConsultaCategoriaProduto() {
		if (campoConsultaCategoriaProduto == null) {
			campoConsultaCategoriaProduto = "";
		}
		return campoConsultaCategoriaProduto;
	}

	public void setCampoConsultaCategoriaProduto(String campoConsultaCategoriaProduto) {
		this.campoConsultaCategoriaProduto = campoConsultaCategoriaProduto;
	}

	public String getValorConsultaCategoriaProduto() {
		if (valorConsultaCategoriaProduto == null) {
			valorConsultaCategoriaProduto = "";
		}
		return valorConsultaCategoriaProduto;
	}

	public void setValorConsultaCategoriaProduto(String valorConsultaCategoriaProduto) {
		this.valorConsultaCategoriaProduto = valorConsultaCategoriaProduto;
	}

	public String getValorConsultaTipoProduto() {
		return valorConsultaTipoProduto;
	}

	public void setValorConsultaTipoProduto(String valorConsultaTipoProduto) {
		this.valorConsultaTipoProduto = valorConsultaTipoProduto;
	}

	public String getValorConsultaSituacaoProduto() {
		return valorConsultaSituacaoProduto;
	}

	public void setValorConsultaSituacaoProduto(String valorConsultaSituacaoProduto) {
		this.valorConsultaSituacaoProduto = valorConsultaSituacaoProduto;
	}

	public List<SelectItem> getListaSelectItemSituacao() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("AT", "Ativo"));
		itens.add(new SelectItem("IN", "Inativo"));
		return itens;
	}

	public List<SelectItem> getListaSelectItemTipoLayout() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("AP", "Analítico por Produto"));
		itens.add(new SelectItem("AE", "Analítico por Data Entrada"));
		itens.add(new SelectItem("EZ", "Produtos Com Estoque Zerado"));
		return itens;
	}

	public String getTipoLayout() {
		if (tipoLayout == null) {
			tipoLayout = "AP";
		}
		return tipoLayout;
	}

	public void setTipoLayout(String tipoLayout) {
		this.tipoLayout = tipoLayout;
	}

	public boolean getIsApresentarDataEntrada() {
		return getTipoLayout().equals("AE");
	}
	
	public boolean getIsApresentarTrazerEstoque() {
		return getTipoLayout().equals("AE") || getTipoLayout().equals("AP");
	}

	public Boolean getTrazerProdutoZerado() {
		
			if (getTipoLayout().equals("EZ")) {
				trazerProdutoZerado = true;
			} else {
				trazerProdutoZerado = false;
			}
		
		return trazerProdutoZerado;
	}

	public void setTrazerProdutoZerado(Boolean trazerProdutoZerado) {
		this.trazerProdutoZerado = trazerProdutoZerado;
	}

	public Boolean getTrazerProdutoSaldoeZerado() {
		if (trazerProdutoSaldoeZerado == null) {
			trazerProdutoSaldoeZerado = false;
		}
		return trazerProdutoSaldoeZerado;
	}

	public void setTrazerProdutoSaldoeZerado(Boolean trazerProdutoSaldoeZerado) {
		this.trazerProdutoSaldoeZerado = trazerProdutoSaldoeZerado;
	}

}
