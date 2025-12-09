/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.controle.compras;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.faces.model.SelectItem;

import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.compras.CategoriaProdutoVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.compras.ProdutoServicoVO;
import negocio.comuns.compras.RecebimentoCompraVO;
import negocio.comuns.financeiro.CategoriaDespesaVO;
import negocio.comuns.utilitarias.Uteis;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.compras.RecebimentoCompraRelVO;

/**
 * 
 * @author Philippe
 */
@Controller("RecebimentoCompraRelControle")
@Scope("viewScope")
@Lazy
public class RecebimentoCompraRelControle extends SuperControleRelatorio {

	private Date dataInicio;
	private Date dataFim;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private CategoriaDespesaVO categoriaDespesaVO;
	private CategoriaProdutoVO categoriaProdutoVO;
	private ProdutoServicoVO produtoServicoVO;
	private FornecedorVO fornecedorVO;
	private String campoConsultaProduto;
	private String valorConsultaProduto;
	private List listaConsultaProduto;
	private String campoConsultaFornecedor;
	private String valorConsultaFornecedor;
	private List listaConsultaFornecedor;
	private String campoConsultaCategoriaProduto;
	private String valorConsultaCategoriaProduto;
	private List listaConsultaCategoriaProduto;
	private List listaConsultaCategoriaDespesa;
	protected List listaSelectItemFornecedor;
	private String valorConsultaCategoriaDespesa;
	private String campoConsultaCategoriaDespesa;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private String ordenacaoRel;
	private String situacaoRecebimentoCompra;

	public RecebimentoCompraRelControle() {
		montarListaSelectItemUnidadeEnsino();
	}

	public void imprimirPDF() {
		try {
			criarRelatorio(TipoRelatorioEnum.PDF);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void montaPeriodo(Date dataInicial, Date dataFinal) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String periodo = "";
		if ((getDataInicio() != null) && (getDataFim() != null)) {
			periodo = sdf.format(dataInicial) + "  à  " + sdf.format(dataFinal);
			getSuperParametroRelVO().adicionarParametro("periodo", periodo);
		} else if ((getDataInicio() == null) && (getDataFim() != null)) {
			periodo = "Todas as datas " + "  à  " + sdf.format(dataFinal);
			getSuperParametroRelVO().adicionarParametro("periodo", periodo);
		} else if ((getDataInicio() != null) && (getDataFim() == null)) {
			periodo = sdf.format(dataInicial) + "  à  " + "Todas as datas";
			getSuperParametroRelVO().adicionarParametro("periodo", periodo);
		} else {
			getSuperParametroRelVO().adicionarParametro("periodo", "");
		}

	}

	public void imprimirExcel() {
		try {
			criarRelatorio(TipoRelatorioEnum.EXCEL);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} 
	}

	public void consultarProduto() {
		try {
			List<ProdutoServicoVO> objs = new ArrayList<>(0);
			if (getCampoConsultaProduto().equals("nome")) {
				objs = getFacadeFactory().getProdutoServicoFacade().consultarPorNome(getValorConsultaProduto(), null, true, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			} else if (getCampoConsultaProduto().equals("nomeCategoriaProduto")) {
				objs = getFacadeFactory().getProdutoServicoFacade().consultarPorNomeCategoriaProduto(getValorConsultaProduto(), null,  false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			setListaConsultaProduto(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaProduto(new ArrayList<>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarFornecedor() {
		try {
			List<FornecedorVO> objs = new ArrayList<>(0);
			if (getCampoConsultaFornecedor().equals("nome")) {
				objs = getFacadeFactory().getFornecedorFacade().consultarPorNome(getValorConsultaFornecedor(), "AT", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFornecedor().equals("cnpj")) {
				objs = getFacadeFactory().getFornecedorFacade().consultarPorCNPJ(getValorConsultaFornecedor(), "AT", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultaFornecedor(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaFornecedor(new ArrayList<>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarProduto() throws Exception {
		ProdutoServicoVO obj = (ProdutoServicoVO) context().getExternalContext().getRequestMap().get("produtoItens");
		setProdutoServicoVO(obj);
		getListaConsultaProduto().clear();
		valorConsultaProduto = null;
		campoConsultaProduto = null;
		//setCategoriaDespesaVO(getFacadeFactory().getCategoriaDespesaFacade().consultarPorChavePrimaria(getProdutoServicoVO().getCategoriaProduto().getCategoriaDespesa().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
	}

	public void selecionarFornecedor() throws Exception {
		FornecedorVO obj = (FornecedorVO) context().getExternalContext().getRequestMap().get("fornecedorItens");
		setFornecedorVO(obj);
		getListaConsultaFornecedor().clear();
		valorConsultaFornecedor = null;
		campoConsultaFornecedor = null;
	}

	public void selecionarCategoriaProduto() throws Exception {
		CategoriaProdutoVO obj = (CategoriaProdutoVO) context().getExternalContext().getRequestMap().get("categoriaProduto");
		this.setCategoriaProdutoVO(obj);
		this.setCategoriaDespesaVO(obj.getCategoriaDespesa());
		this.listaConsultaCategoriaProduto.clear();
		this.valorConsultaCategoriaProduto = null;
		this.campoConsultaCategoriaProduto = null;

	}

	public void consultarCategoriaDespesa() {
		try {
			List<CategoriaDespesaVO> objs = new ArrayList<>(0);
			if (getCampoConsultaCategoriaDespesa().equals("descricao")) {
				objs = getFacadeFactory().getCategoriaDespesaFacade().consultarPorDescricaoPassandoCodCategoriaDespesa(getValorConsultaCategoriaDespesa(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado(), getProdutoServicoVO().getCategoriaProduto().getCategoriaDespesa().getCodigo());
			}
			if (getCampoConsultaCategoriaDespesa().equals("identificadorCategoriaDespesa")) {
				objs = getFacadeFactory().getCategoriaDespesaFacade().consultarPorIdentificadorCategoriaDespesaPassandoCodCategoriaDespesa(getValorConsultaCategoriaDespesa(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado(), getProdutoServicoVO().getCategoriaProduto().getCategoriaDespesa().getCodigo());
			}
			setListaConsultaCategoriaDespesa(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCategoriaDespesa(new ArrayList<>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarCategoriaDespesa() throws Exception {
		CategoriaDespesaVO obj = (CategoriaDespesaVO) context().getExternalContext().getRequestMap().get("categoriaDespesaItens");
		setCategoriaDespesaVO(obj);
		getListaConsultaCategoriaDespesa().clear();
		valorConsultaCategoriaDespesa = null;
		campoConsultaCategoriaDespesa = null;
	}

	public List getTipoConsultaComboCategoriaDespesa() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("descricao", "Descrição"));
		itens.add(new SelectItem("identificadorCategoriaDespesa", "Identificador Centro Despesa"));
		return itens;
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List getTipoConsultaCombo() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("fornecedor", "Fornecedor"));
		itens.add(new SelectItem("codigoCompra", "Compra"));
		return itens;
	}

	public List getTipoOrdenacaoCombo() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("", ""));
		itens.add(new SelectItem("unidadeEnsino", "Unidade Ensino"));
		itens.add(new SelectItem("fornecedor", "Fornecedor"));
		itens.add(new SelectItem("categoriaDespesa", "Categoria Despesa"));
		return itens;
	}

	public List getTipoConsultaComboCategoriaProduto() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	public List getTipoConsultaComboProduto() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("nomeCategoriaProduto", "Categoria Produto"));

		return itens;
	}

	public List getTipoConsultaComboFornecedor() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("cnpj", "CNPJ"));
		return itens;
	}

	public List getTipoConsultaComboDepartamento() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	public List getTipoSituacaoRecebimentoCompra() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("TODAS", "Todas"));
		itens.add(new SelectItem("PR", "Previsão"));
		itens.add(new SelectItem("EF", "Efetivada"));
		return itens;
	}

	public void limparDadosFornecedor() {
		setFornecedorVO(null);
	}

	public void limparDadosServicoProduto() {
		setProdutoServicoVO(null);
	}

	public void limparDadosCategoriaDespesa() {
		setCategoriaDespesaVO(null);
	}

	public void limparDadosCategoriaProduto() {
		setCategoriaProdutoVO(null);
	}

	public void montarListaSelectItemUnidadeEnsino() {
		try {
			montarListaSelectItemUnidadeEnsino("");
		} catch (Exception e) {
			//System.out.println("MENSAGEM => " + e.getMessage());
			;
		}
	}

	public void consultarCategoriaProduto() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaCategoriaProduto().equals("codigo")) {
				if (getValorConsultaCategoriaProduto().equals("")) {
					setValorConsultaCategoriaProduto("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaCategoriaProduto());
				objs = getFacadeFactory().getCategoriaProdutoFacade().consultarPorCodigo(new Integer(valorInt), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getCampoConsultaCategoriaProduto().equals("nome")) {
				objs = getFacadeFactory().getCategoriaProdutoFacade().consultarPorNome(getValorConsultaCategoriaProduto(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
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
		getListaSelectItemUnidadeEnsino().clear();
		try {
			resultadoConsulta = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorUsuarioUnidadeEnsinoVinculadaAoUsuario(getUsuarioLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			if (resultadoConsulta.isEmpty()) {
				resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
			}
			if (getUnidadeEnsinoLogado().getCodigo().equals(0)) {
				getListaSelectItemUnidadeEnsino().add(new SelectItem(0, ""));
			}
			for (UnidadeEnsinoVO obj : resultadoConsulta) {
				if (getUnidadeEnsinoLogado().getCodigo().equals(0)) {
					getListaSelectItemUnidadeEnsino().add(new SelectItem(obj.getCodigo(), obj.getNome()));
				} else if (obj.getCodigo().equals(getUnidadeEnsinoLogado().getCodigo())) {
					getListaSelectItemUnidadeEnsino().add(new SelectItem(obj.getCodigo(), obj.getNome()));
					setUnidadeEnsinoVO(obj);
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
		}
	}

	public List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
		List<UnidadeEnsinoVO> lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		return lista;
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

	public CategoriaDespesaVO getCategoriaDespesaVO() {
		if (categoriaDespesaVO == null) {
			categoriaDespesaVO = new CategoriaDespesaVO();
		}
		return categoriaDespesaVO;
	}

	public void setCategoriaDespesaVO(CategoriaDespesaVO categoriaDespesaVO) {
		this.categoriaDespesaVO = categoriaDespesaVO;
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

	public ProdutoServicoVO getProdutoServicoVO() {
		if (produtoServicoVO == null) {
			produtoServicoVO = new ProdutoServicoVO();
		}
		return produtoServicoVO;
	}

	public void setProdutoServicoVO(ProdutoServicoVO produtoServicoVO) {
		this.produtoServicoVO = produtoServicoVO;
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

	public List getListaConsultaProduto() {
		if (listaConsultaProduto == null) {
			listaConsultaProduto = new ArrayList(0);
		}
		return listaConsultaProduto;
	}

	public void setListaConsultaProduto(List listaConsultaProduto) {
		this.listaConsultaProduto = listaConsultaProduto;
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

	public List getListaConsultaCategoriaProduto() {
		if (listaConsultaCategoriaProduto == null) {
			listaConsultaCategoriaProduto = new ArrayList(0);
		}
		return listaConsultaCategoriaProduto;
	}

	public void setListaConsultaCategoriaProduto(List listaConsultaCategoriaProduto) {
		this.listaConsultaCategoriaProduto = listaConsultaCategoriaProduto;
	}

	public List getListaConsultaCategoriaDespesa() {
		if (listaConsultaCategoriaDespesa == null) {
			listaConsultaCategoriaDespesa = new ArrayList(0);
		}
		return listaConsultaCategoriaDespesa;
	}

	public void setListaConsultaCategoriaDespesa(List listaConsultaCategoriaDespesa) {
		this.listaConsultaCategoriaDespesa = listaConsultaCategoriaDespesa;
	}

	public String getValorConsultaCategoriaDespesa() {
		if (valorConsultaCategoriaDespesa == null) {
			valorConsultaCategoriaDespesa = "";
		}
		return valorConsultaCategoriaDespesa;
	}

	public void setValorConsultaCategoriaDespesa(String valorConsultaCategoriaDespesa) {
		this.valorConsultaCategoriaDespesa = valorConsultaCategoriaDespesa;
	}

	public String getCampoConsultaCategoriaDespesa() {
		if (campoConsultaCategoriaDespesa == null) {
			campoConsultaCategoriaDespesa = "";
		}
		return campoConsultaCategoriaDespesa;
	}

	public void setCampoConsultaCategoriaDespesa(String campoConsultaCategoriaDespesa) {
		this.campoConsultaCategoriaDespesa = campoConsultaCategoriaDespesa;
	}

	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList<>(0);
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
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

	public List getListaSelectItemFornecedor() {
		return listaSelectItemFornecedor;
	}

	public void setListaSelectItemFornecedor(List listaSelectItemFornecedor) {
		this.listaSelectItemFornecedor = listaSelectItemFornecedor;
	}

	public String getCampoConsultaFornecedor() {
		if (campoConsultaFornecedor == null) {
			campoConsultaFornecedor = "";
		}
		return campoConsultaFornecedor;
	}

	public void setCampoConsultaFornecedor(String campoConsultaFornecedor) {
		this.campoConsultaFornecedor = campoConsultaFornecedor;
	}

	public String getValorConsultaFornecedor() {
		if (valorConsultaFornecedor == null) {
			valorConsultaFornecedor = "";
		}
		return valorConsultaFornecedor;
	}

	public void setValorConsultaFornecedor(String valorConsultaFornecedor) {
		this.valorConsultaFornecedor = valorConsultaFornecedor;
	}

	public List getListaConsultaFornecedor() {
		if (listaConsultaFornecedor == null) {
			listaConsultaFornecedor = new ArrayList(0);
		}
		return listaConsultaFornecedor;
	}

	public void setListaConsultaFornecedor(List listaConsultaFornecedor) {
		this.listaConsultaFornecedor = listaConsultaFornecedor;
	}

	public FornecedorVO getFornecedorVO() {
		if (fornecedorVO == null) {
			fornecedorVO = new FornecedorVO();
		}
		return fornecedorVO;
	}

	public void setFornecedorVO(FornecedorVO fornecedorVO) {
		this.fornecedorVO = fornecedorVO;
	}

	public String getOrdenacaoRel() {
		if (ordenacaoRel == null) {
			ordenacaoRel = "";
		}
		return ordenacaoRel;
	}

	public void setOrdenacaoRel(String ordenacaoRel) {
		this.ordenacaoRel = ordenacaoRel;
	}

	public String getSituacaoRecebimentoCompra() {
		if (situacaoRecebimentoCompra == null) {
			situacaoRecebimentoCompra = "Todas";
		}
		return situacaoRecebimentoCompra;
	}

	public void setSituacaoRecebimentoCompra(String situacaoRecebimentoCompra) {
		this.situacaoRecebimentoCompra = situacaoRecebimentoCompra;
	}
	
	private void criarRelatorio(TipoRelatorioEnum tipoRelatorioEnum) throws Exception {
		List<RecebimentoCompraRelVO> listaObjetos = null;
		listaObjetos = getFacadeFactory().getRecebimentoCompraRelFacade().criarObjetoLayoutRecebimentoCompra(getUnidadeEnsinoVO(), Optional.ofNullable(getDataInicio()), Optional.ofNullable(getDataFim()), getCategoriaDespesaVO(), getCategoriaProdutoVO(), getProdutoServicoVO(), getFornecedorVO(), getSituacaoRecebimentoCompra(), getOrdenacaoRel());
		if (!listaObjetos.isEmpty()) {
			getSuperParametroRelVO().setUnidadeEnsino(getUnidadeEnsinoVO().getNome());
			getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getRequisicaoRelFacade().designIReportRelatorio("RecebimentoCompraRel"));
			getSuperParametroRelVO().setTipoRelatorioEnum(tipoRelatorioEnum);
			getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getRecebimentoCompraRelFacade().caminhoBaseRelatorio());
			getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
			getSuperParametroRelVO().setTituloRelatorio("Recebimento Compra");
			getSuperParametroRelVO().setListaObjetos(listaObjetos);
			getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getRecebimentoCompraRelFacade().caminhoBaseRelatorio());
			getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
			this.montaPeriodo(getDataInicio(), getDataFim());
			getSuperParametroRelVO().adicionarParametro("fornecedor", getFornecedorVO().getNome());
			getSuperParametroRelVO().adicionarParametro("produto", getProdutoServicoVO().getNome());
			getSuperParametroRelVO().adicionarParametro("categoriaDespesa", getCategoriaDespesaVO().getDescricao());
			getSuperParametroRelVO().adicionarParametro("fornecedor", getFornecedorVO().getNome());
			getSuperParametroRelVO().adicionarParametro("situacaoRecebimento", getSituacaoRecebimentoCompra());
			realizarImpressaoRelatorio();
			setMensagemID("msg_relatorio_ok");
		} else {
			setMensagemID("msg_relatorio_sem_dados");
		}
	}
}
