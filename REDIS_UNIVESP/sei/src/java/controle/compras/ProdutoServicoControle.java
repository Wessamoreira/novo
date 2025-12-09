package controle.compras;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.compras.CategoriaProdutoVO;
import negocio.comuns.compras.EstoqueVO;
import negocio.comuns.compras.ProdutoServicoVO;
import negocio.comuns.compras.UnidadeMedidaVO;
import negocio.comuns.compras.enumeradores.OrdenarEstoqueEnum;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;

@Controller("ProdutoServicoControle")
@Scope("viewScope")
@Lazy
public class ProdutoServicoControle extends SuperControle implements Serializable {

	private static final long serialVersionUID = -3242898624512970262L;

	private ProdutoServicoVO produtoVO;
	private EstoqueVO estoqueVO;
	private UnidadeMedidaVO unidadeMedidaVO;
	private List<SelectItem> listaSelectItemCategoriaProduto;
	private boolean fecharPanelUnidadeMedida;
	private String nomeProdutoPesquisa;
	private Integer categoriaProdutoPesquisa;
	private String tipoProdutoPesquisa;
	private String situacaoProdutoPesquisa;

	public ProdutoServicoControle() {
		setControleConsulta(new ControleConsulta());
		getControleConsultaOtimizado().setLimitePorPagina(10);
		montarListaSelectItemCategoriaProduto();
		setMensagemID("msg_entre_prmconsulta");
	}

	public String novo() {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "ProdutoServicoControle", "Novo Produto/Serviço", "Novo");
			removerObjetoMemoria(this);
			setProdutoVO(new ProdutoServicoVO());
			setEstoqueVO(new EstoqueVO());
			inicializarListasSelectItemTodosComboBox();
			setMensagemID("msg_entre_dados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}

		return Uteis.getCaminhoRedirecionamentoNavegacao("produtoServicoForm");
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe <code>ProdutoServico</code> para alteração. O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa disponibilizá-lo para edição.
	 */
	public String editar() {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "ProdutoServicoControle", "Inicializando Editar Produto/Serviço", "Editando");
			ProdutoServicoVO obj = (ProdutoServicoVO) context().getExternalContext().getRequestMap().get("produtoItem");
			setProdutoVO(montarDadosProdutoServicoVOCompleto(obj));
			setEstoqueVO(new EstoqueVO());			
			montarListaEstoque();
			inicializarListasSelectItemTodosComboBox();
			registrarAtividadeUsuario(getUsuarioLogado(), "ProdutoServicoControle", "Finalizando Editar Produto/Serviço", "Editando");
			setMensagemID("msg_dados_editar");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}

		return Uteis.getCaminhoRedirecionamentoNavegacao("produtoServicoForm");
	}

	private ProdutoServicoVO montarDadosProdutoServicoVOCompleto(ProdutoServicoVO obj) {
		try {
			return getFacadeFactory().getProdutoServicoFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return new ProdutoServicoVO();
	}

	public String gravar() {
		try {
			if (produtoVO.isNovoObj().booleanValue()) {
				registrarAtividadeUsuario(getUsuarioLogado(), "ProdutoServicoControle", "Inicializando Incluir Produto/Serviço", "Incluindo");
				getFacadeFactory().getProdutoServicoFacade().incluir(produtoVO, getUsuarioLogado());
				registrarAtividadeUsuario(getUsuarioLogado(), "ProdutoServicoControle", "Inicializando Incluir Produto/Serviço", "Incluindo");
			} else {
				registrarAtividadeUsuario(getUsuarioLogado(), "ProdutoServicoControle", "Inicializando Alterar Produto/Serviço", "Alterando");
				getFacadeFactory().getProdutoServicoFacade().alterar(produtoVO, getUsuarioLogado());
				registrarAtividadeUsuario(getUsuarioLogado(), "ProdutoServicoControle", "Finalizando Alterar Produto/Serviço", "Alterando");
			}
			if(getProdutoVO().getTipoProdutoServicoEnum().isServico() && !getProdutoVO().getEstoqueVOs().isEmpty()){
				excluirProdutoEstoque();
			}
			setMensagemID("msg_dados_gravados");
			return "";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}

	public void adicionarUnidadeMedida() {
		getProdutoVO().setUnidadeMedida(new UnidadeMedidaVO());
		setUnidadeMedidaVO(new UnidadeMedidaVO());
	}

	public void excluirUnidadeMedida() {
		try {
			if (getProdutoVO().getUnidadeMedida().getCodigo().intValue() != 0) {
				getFacadeFactory().getUnidadeMedidaFacade().excluir(getProdutoVO().getUnidadeMedida());
			} else {
				throw new Exception("Selecione uma unidade de medida para realizar a excluão");
			}
			getProdutoVO().setUnidadeMedida(new UnidadeMedidaVO());
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String getOnAvancarAbrirPanelSelecionar() {
		if (this.fecharPanelUnidadeMedida) {
			this.fecharPanelUnidadeMedida = false;
			return "RichFaces.$('panelUnidadeMedida').hide();";
		}

		return "";
	}

	public void gravarUnidadeMedida() {
		try {
			if (unidadeMedidaVO.isNovoObj().booleanValue()) {
				getFacadeFactory().getUnidadeMedidaFacade().incluir(unidadeMedidaVO);
			} else {
				getFacadeFactory().getUnidadeMedidaFacade().alterar(unidadeMedidaVO);
			}

			this.fecharPanelUnidadeMedida = true;

			this.getProdutoVO().setUnidadeMedida(unidadeMedidaVO);
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			this.fecharPanelUnidadeMedida = false;
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String getModalExcluirEstoqueProduto() {
		if (!produtoVO.getControlarEstoque() && !produtoVO.getEstoqueVOs().isEmpty()) {
			return "RichFaces.$('panelExcluirEstoque').show();";
		}
		return "";
	}

	public void excluirProdutoEstoque() {
		try {
			List<EstoqueVO> estoque =getProdutoVO().getEstoqueVOs().stream().flatMap(estAgrupadado -> estAgrupadado.getEstoqueVOs().stream()).collect(Collectors.toList());
			getFacadeFactory().getEstoqueFacade().excluir(estoque, getUsuarioLogado(), true);
			getProdutoVO().getEstoqueVOs().clear();
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP ProdutoCons.jsp. Define o tipo de consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
	 */
	@Override
	public String consultar() {
		try {
			getControleConsultaOtimizado().preencherDadosParaConsulta(true, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getProdutoServicoFacade().consultarPorFiltros(this.getNomeProdutoPesquisa(), this.getCategoriaProdutoPesquisa(), this.getTipoProdutoPesquisa(), this.getSituacaoProdutoPesquisa(), true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado(), getControleConsultaOtimizado()));
			getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getProdutoServicoFacade().consultarTotalPorFiltros(this.getNomeProdutoPesquisa(), this.getCategoriaProdutoPesquisa(), this.getTipoProdutoPesquisa(), this.getSituacaoProdutoPesquisa(), true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
			return "";
		} catch (Exception e) {
			setListaConsulta(new ArrayList<>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe <code>ProdutoServicoVO</code> Após a exclusão ela automaticamente aciona a rotina para uma nova inclusão.
	 */
	public void excluir() {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "FornecedorControle", "Inicializando Excluir Produto/Serviço", "Excluindo");
			excluirProdutoEstoque();
			getFacadeFactory().getProdutoServicoFacade().excluir(produtoVO, getUsuarioLogado());
			setProdutoVO(new ProdutoServicoVO());
			registrarAtividadeUsuario(getUsuarioLogado(), "FornecedorControle", "Finalizando Excluir Produto/Serviço", "Excluindo");
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void montarListaSelectItemCategoriaProduto(String prm) throws Exception {
		List<CategoriaProdutoVO> resultadoConsulta = null;
		Iterator<CategoriaProdutoVO> i = null;
		try {
			resultadoConsulta = consultarCategoriaProdutoPorNome(prm, Uteis.NIVELMONTARDADOS_TODOS);
			i = resultadoConsulta.iterator();
			List<SelectItem> objs = new ArrayList<>(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				CategoriaProdutoVO obj = (CategoriaProdutoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
			}
			setListaSelectItemCategoriaProduto(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	public void montarListaSelectItemCategoriaProduto() {
		try {
			montarListaSelectItemCategoriaProduto("");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	@Override
	public boolean getApresentarResultadoConsulta() {
		return getControleConsultaOtimizado().getListaConsulta().size() > 0 ;		
	}

	public boolean getApresentarPaginadorResultadoConsulta() {
		return getControleConsultaOtimizado().getTotalRegistrosEncontrados() > 10;
	}

	public List<CategoriaProdutoVO> consultarCategoriaProdutoPorNome(String descricaoPrm, int nivelMontarDados) throws Exception {
		return getFacadeFactory().getCategoriaProdutoFacade().consultarPorNome(descricaoPrm, false, nivelMontarDados, getUsuarioLogado());
	}

	public void inicializarListasSelectItemTodosComboBox() {
		montarListaSelectItemCategoriaProduto();
	}

	public void scrollerListener(DataScrollEvent DataScrollEvent) throws Exception {
		getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
		getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
		this.consultar();
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

	public List<SelectItem> getListaSelectItemUnidadeMedidaProduto() throws Exception {
		List<SelectItem> objs = new ArrayList<>(0);
		objs.add(new SelectItem("", ""));
		List<UnidadeMedidaVO> lista = getFacadeFactory().getUnidadeMedidaFacade().consultarPorNome("", false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
		Iterator<UnidadeMedidaVO> i = lista.iterator();
		while (i.hasNext()) {
			UnidadeMedidaVO uni = (UnidadeMedidaVO) i.next();
			objs.add(new SelectItem(uni.getCodigo(), uni.getNome()));
		}
		return objs;
	}

	public void consultarCategoriaProdutoPorChavePrimaria() {
		try {
			Integer campoConsulta = produtoVO.getCategoriaProduto().getCodigo();
			CategoriaProdutoVO categoriaProduto = getFacadeFactory().getCategoriaProdutoFacade().consultarPorChavePrimaria(campoConsulta, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			produtoVO.getCategoriaProduto().setNome(categoriaProduto.getNome());

			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			produtoVO.getCategoriaProduto().setNome("");
			produtoVO.getCategoriaProduto().setCodigo(0);
		}
	}

	public void visualizarEstoque() {
		EstoqueVO obj = (EstoqueVO) context().getExternalContext().getRequestMap().get("estoqueProdutoServico");
		setEstoqueVO(obj);
		Collections.sort(getEstoqueVO().getEstoqueVOs(), OrdenarEstoqueEnum.DATA_ENTRADA_QUANTIDADE.desc());
		
	}

	public void atualizarEstoqueMinino() {
		try {
			EstoqueVO obj = (EstoqueVO) context().getExternalContext().getRequestMap().get("estoqueProdutoServico");
			obj.atualizarEstoqueMinino();
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void atualizarEstoqueMaximo() {
		try {
			EstoqueVO obj = (EstoqueVO) context().getExternalContext().getRequestMap().get("estoqueProdutoServico");
			obj.atualizarEstoqueMaximo();
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setListaConsulta(new ArrayList<>(0));
		setMensagemID("msg_entre_prmconsulta");
		montarListaSelectItemCategoriaProduto();
		return Uteis.getCaminhoRedirecionamentoNavegacao("produtoServicoCons");
	}

	public void montarListaEstoque() {
		try {
			if(getProdutoVO().getControlarEstoque()){
				if (Uteis.isAtributoPreenchido(getProdutoVO())) {
					getProdutoVO().setEstoqueVOs(getFacadeFactory().getEstoqueFacade().consultarPorCodigoProduto(getProdutoVO().getCodigo(), 0, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
				}
				List<UnidadeEnsinoVO> listaUnidadeEnsino = getFacadeFactory().getUnidadeEnsinoFacade().consultarTodasUnidades(false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				getProdutoVO().montarListaEstoque(listaUnidadeEnsino);
			}
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void validarDadosTipoProduto() {
		try {
			if(getProdutoVO().getTipoProdutoServicoEnum().isServico()){
				getProdutoVO().setControlarEstoque(false);
			}else{
				montarListaEstoque();
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> getListaSelectItemCategoriaProduto() {
		if (listaSelectItemCategoriaProduto == null ) {
			listaSelectItemCategoriaProduto = new ArrayList<>(0);
		}
		return listaSelectItemCategoriaProduto;
	}

	public void setListaSelectItemCategoriaProduto(List<SelectItem> listaSelectItemCategoriaProduto) {
		this.listaSelectItemCategoriaProduto = listaSelectItemCategoriaProduto;
	}

	public ProdutoServicoVO getProdutoVO() {
		return produtoVO;
	}

	public void setProdutoVO(ProdutoServicoVO produtoVO) {
		this.produtoVO = produtoVO;
	}

	public EstoqueVO getEstoqueVO() {
		return estoqueVO;
	}

	public void setEstoqueVO(EstoqueVO estoqueVO) {
		this.estoqueVO = estoqueVO;
	}

	public UnidadeMedidaVO getUnidadeMedidaVO() {
		if (unidadeMedidaVO == null) {
			unidadeMedidaVO = new UnidadeMedidaVO();
		}
		return unidadeMedidaVO;
	}

	public void setUnidadeMedidaVO(UnidadeMedidaVO unidadeMedidaVO) {
		this.unidadeMedidaVO = unidadeMedidaVO;
	}
	
	public List<SelectItem> getListaSelectItemSituacao() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("AT", "Ativo"));
		itens.add(new SelectItem("IN", "Inativo"));
		return itens;
	}

	public String getNomeProdutoPesquisa() {
		return nomeProdutoPesquisa;
	}

	public void setNomeProdutoPesquisa(String nomeProdutoPesquisa) {
		this.nomeProdutoPesquisa = nomeProdutoPesquisa;
	}

	public Integer getCategoriaProdutoPesquisa() {
		return categoriaProdutoPesquisa;
	}

	public void setCategoriaProdutoPesquisa(Integer categoriaProdutoPesquisa) {
		this.categoriaProdutoPesquisa = categoriaProdutoPesquisa;
	}

	public String getTipoProdutoPesquisa() {
		return tipoProdutoPesquisa;
	}

	public void setTipoProdutoPesquisa(String tipoProdutoPesquisa) {
		this.tipoProdutoPesquisa = tipoProdutoPesquisa;
	}

	public String getSituacaoProdutoPesquisa() {
		return situacaoProdutoPesquisa;
	}

	public void setSituacaoProdutoPesquisa(String situacaoProdutoPesquisa) {
		this.situacaoProdutoPesquisa = situacaoProdutoPesquisa;
	}
}
