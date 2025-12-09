package controle.compras;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoModuloEnum;
import negocio.comuns.arquitetura.enumeradores.TipoVisaoEnum;
import negocio.comuns.compras.EstatisticaCompraVO;
import negocio.comuns.compras.EstatisticaCotacaoVO;
import negocio.comuns.compras.EstatisticaEstoqueCategoriaProdutoVO;
import negocio.comuns.compras.EstatisticaEstoqueVO;
import negocio.comuns.compras.EstatisticaRequisicaoVO;
import negocio.comuns.compras.EstoqueVO;
import negocio.comuns.compras.ItemSumarioUnidadeEstatisticaVO;
import negocio.comuns.utilitarias.DashboardVO;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoDashboardEnum;
import negocio.comuns.utilitarias.dominios.TipoVisaoAcesso;
import negocio.facade.jdbc.arquitetura.ControleAcesso;

@Controller("PainelGestorCompraControle")
@Scope("session")
@Lazy
public class PainelGestorCompraControle extends SuperControle implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -381320438882477118L;
	private EstatisticaRequisicaoVO estatisticaRequisicaoVO;
	private EstatisticaCotacaoVO estatisticaCotacaoVO;
	private EstatisticaEstoqueVO estatisticaEstoqueVO;
	private EstatisticaCompraVO estatisticaCompraVO;
	private Boolean apresentarEstatisticaRequisicaoVO;
	private Boolean apresentarEstatisticaCotacaoVO;
	private Boolean apresentarEstatisticaProdutoVO;
	private Boolean apresentarEstatisticaCompraVO;
	private String valorGraficoCompra;
	private String valorCategoriaCompra;
	private String valorGraficoCotacao;
	private String valorCategoriaCotacao;
	private String valorGraficoRequisicao;
	private String valorCategoriaRequisicao;
	private String valorGraficoEstoque;
	private String valorCategoriaEstoque;
	private List<EstoqueVO> estoqueVOs;
	private String descricaoCategoriaUnidadeEnsino;

	/** Creates a new instance of PainelGestorCompraControle */
	public PainelGestorCompraControle() {
		
	}
	
	private DashboardVO dashBoardRequisicao;
	private DashboardVO dashBoardCotacao;
	private DashboardVO dashBoardCompra;
	private DashboardVO dashBoardEstoque;
	
	public DashboardVO getDashBoardRequisicao() {		
		return dashBoardRequisicao;
	}

	public void setDashBoardRequisicao(DashboardVO dashBoardRequisicao) {
		this.dashBoardRequisicao = dashBoardRequisicao;
	}
	
	public DashboardVO getDashBoardCotacao() {
		return dashBoardCotacao;
	}

	public void setDashBoardCotacao(DashboardVO dashBoardCotacao) {
		this.dashBoardCotacao = dashBoardCotacao;
	}

	public DashboardVO getDashBoardCompra() {
		return dashBoardCompra;
	}

	public void setDashBoardCompra(DashboardVO dashBoardCompra) {
		this.dashBoardCompra = dashBoardCompra;
	}

	public DashboardVO getDashBoardEstoque() {
		return dashBoardEstoque;
	}

	public void setDashBoardEstoque(DashboardVO dashBoardEstoque) {
		this.dashBoardEstoque = dashBoardEstoque;
	}

	@PostConstruct
	public void init() {
		estatisticaRequisicaoVO = new EstatisticaRequisicaoVO();
		estatisticaCotacaoVO = new EstatisticaCotacaoVO();
		estatisticaEstoqueVO = new EstatisticaEstoqueVO();
		estatisticaCompraVO = new EstatisticaCompraVO();
		if(getAplicacaoControle().getCliente().getPermitirAcessoModuloCompras()) {
			inicializarDadosEstatisticaRequisicoes();
			inicializarDadosEstatisticaCotacao();
			inicializarDadosEstatisticaCompra();
			inicializarDadosEstatisticaEstoque();		
		}
	}
	
	public void inicializarDadosEstatisticaRequisicoes() {
		if (getPerfilAcessoPainelRequisicoesAguardandoAutorizacao() && this.getUsuarioLogado().getPerfilAdministrador()) {			
			if (!getLoginControle().getMapDashboards().containsKey(TipoDashboardEnum.REQUISICAO_PENDENTE_AUTORIZACAO.name())) {
				dashBoardRequisicao = new DashboardVO(TipoDashboardEnum.REQUISICAO_PENDENTE_AUTORIZACAO, false, 6,
						TipoVisaoEnum.ADMINISTRATIVA, PerfilAcessoModuloEnum.ADMINISTRATIVO, getUsuarioLogado()); 
				getLoginControle().getMapDashboards().put(TipoDashboardEnum.REQUISICAO_PENDENTE_AUTORIZACAO.name(),dashBoardRequisicao);
			}else {
				dashBoardRequisicao = getLoginControle().getMapDashboards().get(TipoDashboardEnum.REQUISICAO_PENDENTE_AUTORIZACAO.name());
			}
			this.apresentarEstatisticaRequisicaoVO = true;
			
			dashBoardRequisicao.setUsuarioVO(getUsuarioLogadoClone());
			dashBoardRequisicao.iniciar(1l, 2, "Carregando..", true, this, "atualizarEstatisticaRequisicoes");
			dashBoardRequisicao.iniciarAssincrono();

		}else {
			if (getLoginControle().getMapDashboards()
					.containsKey(TipoDashboardEnum.REQUISICAO_PENDENTE_AUTORIZACAO.name())) {
				getLoginControle().getMapDashboards()
						.remove(TipoDashboardEnum.REQUISICAO_PENDENTE_AUTORIZACAO.name());
			}
		}
	}
	
	public void inicializarDadosEstatisticaCotacao() {
		if (getPerfilAcessoPainelCotacaoAguardandoAutorizacao() && this.getUsuarioLogado().getPerfilAdministrador()) {			
			if (!getLoginControle().getMapDashboards().containsKey(TipoDashboardEnum.COTACAO.name())) {
				dashBoardCotacao = new DashboardVO(TipoDashboardEnum.COTACAO, false, 7,
						TipoVisaoEnum.ADMINISTRATIVA, PerfilAcessoModuloEnum.ADMINISTRATIVO, getUsuarioLogado()); 
				getLoginControle().getMapDashboards().put(TipoDashboardEnum.COTACAO.name(),dashBoardCotacao);
			}else {
				dashBoardCotacao = getLoginControle().getMapDashboards().get(TipoDashboardEnum.COTACAO.name());
			}
			this.apresentarEstatisticaCotacaoVO = true;			
			dashBoardCotacao.setUsuarioVO(getUsuarioLogadoClone());
			dashBoardCotacao.iniciar(1l, 2, "Carregando..", true, this, "atualizarEstatisticaCotacao");
			dashBoardCotacao.iniciarAssincrono();
			
		}else {
			if (getLoginControle().getMapDashboards()
					.containsKey(TipoDashboardEnum.COTACAO.name())) {
				getLoginControle().getMapDashboards()
				.remove(TipoDashboardEnum.COTACAO.name());
			}
		}
	}
	public void inicializarDadosEstatisticaCompra() {
		if (getPerfilAcessoPainelComprasAguardandoRecebimento() && this.getUsuarioLogado().getPerfilAdministrador()) {			
			if (!getLoginControle().getMapDashboards().containsKey(TipoDashboardEnum.RECEBIMENTO_COMPRA.name())) {
				dashBoardCompra = new DashboardVO(TipoDashboardEnum.RECEBIMENTO_COMPRA, false, 8,
						TipoVisaoEnum.ADMINISTRATIVA, PerfilAcessoModuloEnum.ADMINISTRATIVO, getUsuarioLogado()); 
				getLoginControle().getMapDashboards().put(TipoDashboardEnum.RECEBIMENTO_COMPRA.name(),dashBoardCompra);
			}else {
				dashBoardCompra = getLoginControle().getMapDashboards().get(TipoDashboardEnum.RECEBIMENTO_COMPRA.name());
			}
			this.apresentarEstatisticaCompraVO = true;
			
			dashBoardCompra.setUsuarioVO(getUsuarioLogadoClone());
			dashBoardCompra.iniciar(1l, 2, "Carregando..", true, this, "atualizarEstatisticaCompras");
			dashBoardCompra.iniciarAssincrono();
			
		}else {
			if (getLoginControle().getMapDashboards()
					.containsKey(TipoDashboardEnum.RECEBIMENTO_COMPRA.name())) {
				getLoginControle().getMapDashboards()
				.remove(TipoDashboardEnum.RECEBIMENTO_COMPRA.name());
			}
		}
	}
	
	public void inicializarDadosEstatisticaEstoque() {
		if (getPerfilAcessoPainelEstoqueAbaixoMinimo() && this.getUsuarioLogado().getPerfilAdministrador()) {			
			if (!getLoginControle().getMapDashboards().containsKey(TipoDashboardEnum.ESTOQUE.name())) {
				dashBoardEstoque = new DashboardVO(TipoDashboardEnum.ESTOQUE, false, 9,
						TipoVisaoEnum.ADMINISTRATIVA, PerfilAcessoModuloEnum.ADMINISTRATIVO, getUsuarioLogado()); 
				getLoginControle().getMapDashboards().put(TipoDashboardEnum.ESTOQUE.name(),dashBoardEstoque);
			}else {
				dashBoardEstoque = getLoginControle().getMapDashboards().get(TipoDashboardEnum.ESTOQUE.name());
			}
			this.apresentarEstatisticaProdutoVO = true;
			
			dashBoardEstoque.setUsuarioVO(getUsuarioLogadoClone());
			dashBoardEstoque.iniciar(1l, 2, "Carregando..", true, this, "atualizarEstatisticaEstoque");
			dashBoardEstoque.iniciarAssincrono();
			
		}else {
			if (getLoginControle().getMapDashboards()
					.containsKey(TipoDashboardEnum.ESTOQUE.name())) {
				getLoginControle().getMapDashboards()
				.remove(TipoDashboardEnum.ESTOQUE.name());
			}
		}
	}

	public void atualizarEstatisticaRequisicoes() {
		try {											
				setEstatisticaRequisicaoVO(getFacadeFactory().getMapaRequisicaoFacade().consultarEstatisticaRequisicoesAtualizada(getDashBoardRequisicao().getUsuarioVO()));
				
				StringBuilder dados = new StringBuilder("");
				StringBuilder categorias = new StringBuilder("");
				for (ItemSumarioUnidadeEstatisticaVO obj : getEstatisticaRequisicaoVO().getSumarioPorUnidade()) {
					if (!dados.toString().isEmpty()) {
						dados.append(", ");
						categorias.append(", ");
					}
					categorias.append("'")
							.append(Uteis.removerAcentos(obj.getNome()).replaceAll("ª", "").replaceAll("º", ""))
							.append("'");
					dados.append(obj.getQuantidade());
				}
				setValorGraficoRequisicao(dados.toString());
				setValorCategoriaRequisicao(categorias.toString());
		} catch (Exception e) {
			setApresentarEstatisticaRequisicaoVO(false);
		} finally {
			getDashBoardRequisicao().incrementar();
			getDashBoardRequisicao().encerrar();
		}
	}

	public Object[][] realizarMontagemJSONItemSumarioUnidadeEstatisticaVO(
			List<ItemSumarioUnidadeEstatisticaVO> itemSumarioUnidadeEstatisticaVOs) {
		Object[][] dados = new Object[itemSumarioUnidadeEstatisticaVOs.size()][3];

		int x = 0;
		for (ItemSumarioUnidadeEstatisticaVO obj : itemSumarioUnidadeEstatisticaVOs) {
			dados[x][0] = Uteis.removerAcentos(obj.getNome()).replaceAll("ª", "").replaceAll("º", "");
			dados[x][1] = obj.getQuantidade();
			dados[x][2] = obj.getCodigo();
			x++;
		}
		return dados;
	}

	public void atualizarEstatisticaCotacao() {
		try {
			if (getPerfilAcessoPainelCotacaoAguardandoAutorizacao()
					&& this.getUsuarioLogado().getPerfilAdministrador()) {
				this.estatisticaCotacaoVO = getFacadeFactory().getCotacaoFacade().consultarEstatisticaCotacoesAtualizada(getDashBoardCotacao().getUsuarioVO());
				this.estatisticaCotacaoVO.atualizarGraficoBarraSumarioPorUnidade();
				StringBuilder dados = new StringBuilder("");
				StringBuilder categorias = new StringBuilder("");
				for (ItemSumarioUnidadeEstatisticaVO obj : estatisticaCotacaoVO.getSumarioPorUnidade()) {
					if (!dados.toString().isEmpty()) {
						dados.append(", ");
						categorias.append(", ");
					}
					categorias.append("'")
							.append(Uteis.removerAcentos(obj.getNome()).replaceAll("ª", "").replaceAll("º", ""))
							.append("'");
					dados.append(obj.getQuantidade());
				}
				setValorGraficoCotacao(dados.toString());
				setValorCategoriaCotacao(categorias.toString());
				apresentarEstatisticaCotacaoVO = true;
			} else {
				apresentarEstatisticaCotacaoVO = false;
			}
		} catch (Exception e) {
			apresentarEstatisticaCotacaoVO = false;

		} finally {
			getDashBoardCotacao().incrementar();
			getDashBoardCotacao().encerrar();
		}
	}

	public void atualizarEstatisticaCompras() {
		try {
			if (getPerfilAcessoPainelComprasAguardandoRecebimento()
					&& this.getUsuarioLogado().getPerfilAdministrador()) {
				this.estatisticaCompraVO = getFacadeFactory().getCompraFacade()
						.consultarEstatisticaRecebimentoCompraAtualizada(getDashBoardCompra().getUsuarioVO());
				this.estatisticaCompraVO.atualizarGraficoBarraSumarioPorUnidade();
				StringBuilder dados = new StringBuilder("");
				StringBuilder categorias = new StringBuilder("");
				for (ItemSumarioUnidadeEstatisticaVO obj : estatisticaCompraVO.getSumarioPorUnidade()) {
					if (!dados.toString().isEmpty()) {
						dados.append(", ");
						categorias.append(", ");
					}
					categorias.append("'")
							.append(Uteis.removerAcentos(obj.getNome()).replaceAll("ª", "").replaceAll("º", ""))
							.append("'");
					dados.append(obj.getQuantidade());
				}
				setValorGraficoCompra(dados.toString());
				setValorCategoriaCompra(categorias.toString());
				apresentarEstatisticaCompraVO = true;
			} else {
				apresentarEstatisticaCompraVO = false;
			}
		} catch (Exception e) {
			apresentarEstatisticaCompraVO = false;

		} finally {
			getDashBoardCompra().incrementar();
			getDashBoardCompra().encerrar();
		}
	}

	public void atualizarEstatisticaEstoque() {
		try {
						this.setEstatisticaEstoqueVO(getFacadeFactory().getEstoqueFacade()
						.consultarEstatisticaEstoqueAtualizada(getDashBoardEstoque().getUsuarioVO()));
				StringBuilder dados = new StringBuilder("");
				StringBuilder categorias = new StringBuilder("");
				for (EstatisticaEstoqueCategoriaProdutoVO obj : getEstatisticaEstoqueVO()
						.getResumoEstatisticaEstoqueCategoriaProdutoVO()) {
					if (!dados.toString().isEmpty()) {
						dados.append(", ");
						categorias.append(", ");
					}
					dados.append("{name: '").append(
							Uteis.removerAcentos(obj.getNomeCategoriaProduto()).replaceAll("ª", "").replaceAll("º", ""))
							.append("', ");
					categorias.append("'").append(
							Uteis.removerAcentos(obj.getNomeCategoriaProduto()).replaceAll("ª", "").replaceAll("º", ""))
							.append("'");
					dados.append("data:[").append(obj.getQuantidadeUnitariaProdutoAbaixoMinimo()).append("]}");
				}
				setValorGraficoEstoque(dados.toString());
				setValorCategoriaEstoque(categorias.toString());
		} catch (Exception e) {
		
		} finally {
			getDashBoardEstoque().incrementar();
			getDashBoardEstoque().encerrar();
		}
	}

	private Boolean perfilAcessoPainelRequisicoesAguardandoAutorizacao;

	public Boolean getPerfilAcessoPainelRequisicoesAguardandoAutorizacao() {
		if (perfilAcessoPainelRequisicoesAguardandoAutorizacao == null) {
			try {
				ControleAcesso.consultar("PainelGestorRequisiçõesAguardandoAutorização", true, getUsuarioLogado());
				perfilAcessoPainelRequisicoesAguardandoAutorizacao = true;
			} catch (Exception e) {
				perfilAcessoPainelRequisicoesAguardandoAutorizacao = false;
			}
		}
		return perfilAcessoPainelRequisicoesAguardandoAutorizacao;
	}

	private Boolean perfilAcessoPainelCotacaoAguardandoAutorizacao;

	public Boolean getPerfilAcessoPainelCotacaoAguardandoAutorizacao() {
		if (perfilAcessoPainelCotacaoAguardandoAutorizacao == null) {
			try {
				ControleAcesso.consultar("PainelGestorCotaçãoAguardandoAutorização", true, getUsuarioLogado());
				perfilAcessoPainelCotacaoAguardandoAutorizacao = true;
			} catch (Exception e) {
				perfilAcessoPainelCotacaoAguardandoAutorizacao = false;
			}
		}
		return perfilAcessoPainelCotacaoAguardandoAutorizacao;
	}

	private Boolean perfilAcessoPainelEstoqueAbaixoMinimo;

	public Boolean getPerfilAcessoPainelEstoqueAbaixoMinimo() {
		if (perfilAcessoPainelEstoqueAbaixoMinimo == null) {
			try {
				ControleAcesso.consultar("PainelGestorEstoqueAbaixoMínimo", true, getUsuarioLogado());
				perfilAcessoPainelEstoqueAbaixoMinimo = true;
			} catch (Exception e) {
				perfilAcessoPainelEstoqueAbaixoMinimo = false;
			}
		}
		return perfilAcessoPainelEstoqueAbaixoMinimo;
	}

	private Boolean perfilAcessoPainelComprasAguardandoRecebimento;

	public Boolean getPerfilAcessoPainelComprasAguardandoRecebimento() {
		if (perfilAcessoPainelComprasAguardandoRecebimento == null) {
			try {
				ControleAcesso.consultar("PainelGestorComprasAguardandoRecebimento", true, getUsuarioLogado());
				perfilAcessoPainelComprasAguardandoRecebimento = true;
			} catch (Exception e) {
				perfilAcessoPainelComprasAguardandoRecebimento = false;
			}
		}
		return perfilAcessoPainelComprasAguardandoRecebimento;
	}

	/**
	 * @return the estatisticaRequisicaoVO
	 */
	public EstatisticaRequisicaoVO getEstatisticaRequisicaoVO() {
		return estatisticaRequisicaoVO;
	}

	/**
	 * @param estatisticaRequisicaoVO the estatisticaRequisicaoVO to set
	 */
	public void setEstatisticaRequisicaoVO(EstatisticaRequisicaoVO estatisticaRequisicaoVO) {
		this.estatisticaRequisicaoVO = estatisticaRequisicaoVO;
	}

	/**
	 * @return the apresentarEstatisticaRequisicaoVO
	 */
	public Boolean getApresentarEstatisticaRequisicaoVO() {
		return apresentarEstatisticaRequisicaoVO;
	}

	/**
	 * @param apresentarEstatisticaRequisicaoVO the
	 *                                          apresentarEstatisticaRequisicaoVO to
	 *                                          set
	 */
	public void setApresentarEstatisticaRequisicaoVO(Boolean apresentarEstatisticaRequisicaoVO) {
		this.apresentarEstatisticaRequisicaoVO = apresentarEstatisticaRequisicaoVO;
	}

	/**
	 * @return the apresentarEstatisticaCotacaoVO
	 */
	public Boolean getApresentarEstatisticaCotacaoVO() {
		return apresentarEstatisticaCotacaoVO;
	}

	/**
	 * @param apresentarEstatisticaCotacaoVO the apresentarEstatisticaCotacaoVO to
	 *                                       set
	 */
	public void setApresentarEstatisticaCotacaoVO(Boolean apresentarEstatisticaCotacaoVO) {
		this.apresentarEstatisticaCotacaoVO = apresentarEstatisticaCotacaoVO;
	}

	/**
	 * @return the apresentarEstatisticaProdutoVO
	 */
	public Boolean getApresentarEstatisticaProdutoVO() {
		return apresentarEstatisticaProdutoVO;
	}

	/**
	 * @param apresentarEstatisticaProdutoVO the apresentarEstatisticaProdutoVO to
	 *                                       set
	 */
	public void setApresentarEstatisticaProdutoVO(Boolean apresentarEstatisticaProdutoVO) {
		this.apresentarEstatisticaProdutoVO = apresentarEstatisticaProdutoVO;
	}

	/**
	 * @return the apresentarEstatisticaCompraVO
	 */
	public Boolean getApresentarEstatisticaCompraVO() {
		return apresentarEstatisticaCompraVO;
	}

	/**
	 * @param apresentarEstatisticaCompraVO the apresentarEstatisticaCompraVO to set
	 */
	public void setApresentarEstatisticaCompraVO(Boolean apresentarEstatisticaCompraVO) {
		this.apresentarEstatisticaCompraVO = apresentarEstatisticaCompraVO;
	}

	/**
	 * @return the estatisticaCotacaoVO
	 */
	public EstatisticaCotacaoVO getEstatisticaCotacaoVO() {
		return estatisticaCotacaoVO;
	}

	/**
	 * @param estatisticaCotacaoVO the estatisticaCotacaoVO to set
	 */
	public void setEstatisticaCotacaoVO(EstatisticaCotacaoVO estatisticaCotacaoVO) {
		this.estatisticaCotacaoVO = estatisticaCotacaoVO;
	}

	/**
	 * @return the estatisticaEstoqueVO
	 */
	public EstatisticaEstoqueVO getEstatisticaEstoqueVO() {
		return estatisticaEstoqueVO;
	}

	/**
	 * @param estatisticaEstoqueVO the estatisticaEstoqueVO to set
	 */
	public void setEstatisticaEstoqueVO(EstatisticaEstoqueVO estatisticaEstoqueVO) {
		this.estatisticaEstoqueVO = estatisticaEstoqueVO;
	}

	/**
	 * @return the estatisticaCompraVO
	 */
	public EstatisticaCompraVO getEstatisticaCompraVO() {
		return estatisticaCompraVO;
	}

	/**
	 * @param estatisticaCompraVO the estatisticaCompraVO to set
	 */
	public void setEstatisticaCompraVO(EstatisticaCompraVO estatisticaCompraVO) {
		this.estatisticaCompraVO = estatisticaCompraVO;
	}

	public String getValorGraficoCompra() {
		if (valorGraficoCompra == null) {
			valorGraficoCompra = "";
		}
		return valorGraficoCompra;
	}

	public void setValorGraficoCompra(String valorGraficoCompra) {
		this.valorGraficoCompra = valorGraficoCompra;
	}

	public String getValorCategoriaCompra() {
		if (valorCategoriaCompra == null) {
			valorCategoriaCompra = "";
		}
		return valorCategoriaCompra;
	}

	public void setValorCategoriaCompra(String valorCategoriaCompra) {
		this.valorCategoriaCompra = valorCategoriaCompra;
	}

	public String getValorGraficoCotacao() {
		if (valorGraficoCotacao == null) {
			valorGraficoCotacao = "";
		}
		return valorGraficoCotacao;
	}

	public void setValorGraficoCotacao(String valorGraficoCotacao) {
		this.valorGraficoCotacao = valorGraficoCotacao;
	}

	public String getValorCategoriaCotacao() {
		if (valorCategoriaCotacao == null) {
			valorCategoriaCotacao = "";
		}
		return valorCategoriaCotacao;
	}

	public void setValorCategoriaCotacao(String valorCategoriaCotacao) {
		this.valorCategoriaCotacao = valorCategoriaCotacao;
	}

	public String getValorGraficoRequisicao() {
		if (valorGraficoRequisicao == null) {
			valorGraficoRequisicao = "";
		}
		return valorGraficoRequisicao;
	}

	public void setValorGraficoRequisicao(String valorGraficoRequisicao) {
		this.valorGraficoRequisicao = valorGraficoRequisicao;
	}

	public String getValorCategoriaRequisicao() {
		if (valorCategoriaRequisicao == null) {
			valorCategoriaRequisicao = "";
		}
		return valorCategoriaRequisicao;
	}

	public void setValorCategoriaRequisicao(String valorCategoriaRequisicao) {
		this.valorCategoriaRequisicao = valorCategoriaRequisicao;
	}

	public String getValorGraficoEstoque() {
		if (valorGraficoEstoque == null) {
			valorGraficoEstoque = "";
		}
		return valorGraficoEstoque;
	}

	public void setValorGraficoEstoque(String valorGraficoEstoque) {
		this.valorGraficoEstoque = valorGraficoEstoque;
	}

	public String getValorCategoriaEstoque() {
		if (valorCategoriaEstoque == null) {
			valorCategoriaEstoque = "";
		}
		return valorCategoriaEstoque;
	}

	public void setValorCategoriaEstoque(String valorCategoriaEstoque) {
		this.valorCategoriaEstoque = valorCategoriaEstoque;
	}

	public List<EstoqueVO> getEstoqueVOs() {
		if (estoqueVOs == null) {
			estoqueVOs = new ArrayList<>();
		}
		return estoqueVOs;
	}

	public void setEstoqueVOs(List<EstoqueVO> estoqueVOs) {
		this.estoqueVOs = estoqueVOs;
	}

	public String getDescricaoCategoriaUnidadeEnsino() {
		if (descricaoCategoriaUnidadeEnsino == null) {
			descricaoCategoriaUnidadeEnsino = "";
		}
		return descricaoCategoriaUnidadeEnsino;
	}

	public void setDescricaoCategoriaUnidadeEnsino(String descricaoCategoriaUnidadeEnsino) {
		this.descricaoCategoriaUnidadeEnsino = descricaoCategoriaUnidadeEnsino;
	}

	public void consultarEstoques() {
		try {
			getEstoqueVOs().clear();
			setDescricaoCategoriaUnidadeEnsino("");
			EstatisticaEstoqueCategoriaProdutoVO obj = (EstatisticaEstoqueCategoriaProdutoVO) context()
					.getExternalContext().getRequestMap().get("categoriaEstoque");
			if (obj != null) {
				setDescricaoCategoriaUnidadeEnsino(obj.getNomeUnidadeEnsino() + " - " + obj.getNomeCategoriaProduto());
				setEstoqueVOs(getFacadeFactory().getEstoqueFacade().consultarPorNomeCategoriaProdutoAgrupado(
						obj.getNomeCategoriaProduto(), obj.getCodigoUnidadeEnsino(), "IEM", false, false,
						Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			}
		} catch (Exception e) {
			getEstoqueVOs().clear();
			setDescricaoCategoriaUnidadeEnsino("");
		}
	}
}
