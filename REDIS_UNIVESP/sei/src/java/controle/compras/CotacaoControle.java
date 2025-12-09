package controle.compras;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoComprasEnum;
import negocio.comuns.compras.CategoriaProdutoVO;
import negocio.comuns.compras.CondicaoPagamentoVO;
import negocio.comuns.compras.CotacaoFornecedorVO;
import negocio.comuns.compras.CotacaoHistoricoVO;
import negocio.comuns.compras.CotacaoRelVO;
import negocio.comuns.compras.CotacaoVO;
import negocio.comuns.compras.DepartamentoTramiteCotacaoCompraVO;
import negocio.comuns.compras.EstoqueVO;
import negocio.comuns.compras.FormaPagamentoVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.compras.ItemCotacaoUnidadeEnsinoVO;
import negocio.comuns.compras.ItemCotacaoVO;
import negocio.comuns.compras.MapaCotacaoVO;
import negocio.comuns.compras.ParcelaCondicaoPagamentoVO;
import negocio.comuns.compras.ProdutoServicoVO;
import negocio.comuns.compras.RequisicaoItemVO;
import negocio.comuns.compras.RequisicaoVO;
import negocio.comuns.compras.TramiteCotacaoCompraVO;
import negocio.comuns.compras.enumeradores.TipoAutorizacaoRequisicaoEnum;
import negocio.comuns.ead.enumeradores.SituacaoEnum;
import negocio.comuns.financeiro.CategoriaDespesaVO;
import negocio.comuns.financeiro.CentroResultadoVO;
import negocio.comuns.financeiro.enumerador.TipoNivelCentroResultadoEnum;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.compras.MapaCotacao;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.compras.EstoqueRel;

@Controller("CotacaoControle")
@Scope("viewScope")
@Lazy
public class CotacaoControle extends SuperControleRelatorio implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8869553522218602511L;
	private CotacaoVO cotacaoVO;
	private CotacaoFornecedorVO cotacaoFornecedorVO;
	private CotacaoFornecedorVO cotacaoFornecedorVOTemp;
	private ItemCotacaoVO itemCotacaoVO;
	private ItemCotacaoUnidadeEnsinoVO itemCotacaoUnidadeEnsinoVO;
	private RequisicaoItemVO requisicaoItemVO;

	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private List<SelectItem> listaSelectItemFornecedor;
	private List<SelectItem> listaSelectItemCondicaoPagamento;
	private List<SelectItem> listaSelectItemFormaPagamento;
	private List<SelectItem> listaSelectItemDepartamento;
	private List<SelectItem> listaSelectItemTipoNivelCentroResultadoEnum;
	private List<SelectItem> listaSelectItemUnidadeEnsinoTramiteCotacao;
	private List<SelectItem> listaSelectItemTramiteCotacaoCompra;

	private String campoConsultaCategoriaProduto;
	private String valorConsultaCategoriaProduto;
	private List<CategoriaProdutoVO> listaConsultaCategoriaProduto;

	private List<CategoriaDespesaVO> listaConsultaCategoriaDespesa;
	private String valorConsultaCategoriaDespesa;
	private String campoConsultaCategoriaDespesa;

	private String campoConsultaDepartamento;
	private String valorConsultaDepartamento;
	private List<DepartamentoVO> listaConsultaDepartamento;

	private String campoConsultaCurso;
	private String valorConsultaCurso;
	private List<CursoVO> listaConsultaCurso;

	private String campoConsultaTurma;
	private String valorConsultaTurma;
	private List<TurmaVO> listaConsultaTurma;

	private String campoConsultaCursoTurno;
	private String valorConsultaCursoTurno;
	private List<UnidadeEnsinoCursoVO> listaConsultaCursoTurno;

	private String campoConsultaProduto;
	private String valorConsultaProduto;
	private List<ProdutoServicoVO> listaConsultaProduto;

	private List<RequisicaoVO> listaRequisicaoVO;

	private DataModelo centroResultadoDataModelo;
	private boolean centroResultadoAdministrativo = false;

	private List<UnidadeEnsinoVO> listaConsultarUnidadeEnsino;

	/**
	 * Usuario Departamento
	 */
	private boolean abrirPanelSelecionar;
	private UsuarioVO usuarioResponsavelDepartamento;
	private List<UsuarioVO> listaUsuarioDepartamento;
	private boolean tituloQuantidade;
	private Integer requisicao;

	/**
	 * campos filtros
	 */
	private MapaCotacaoVO mapaCotacaoVO;
	private Integer departamentoFiltro;
	private String produtoFiltro;
	private EnumSituacaoTramitacao enumSituacaoTramitacaoFiltro;
	private boolean habilitarBotaoVoltar = false;
	private Integer codigoRequisicao;
	private String justificativaRequisicao;

	public CotacaoControle() {
		init();
		setMensagemID("msg_entre_prmconsulta");
	}

	public void init() {
		setControleConsulta(new ControleConsulta());
		setControleConsultaOtimizado(new DataModelo());
		getControleConsultaOtimizado().setDataIni(Uteis.obterDataFutura(new Date(), -5));
		getControleConsultaOtimizado().setDataFim(Uteis.getNewDateComUmMesAMais());
		getControleConsultaOtimizado().setListaConsulta(new ArrayList<>());
		setCotacaoVO(new CotacaoVO());
		getCotacaoVO().getAtualCotacaoHistoricoVO().setResponsavel(new UsuarioVO());
		getCotacaoVO().setSituacao("");
		montarListaUnidadeEnsino();
		montarListaDepartamento();
		realizarCarregamentoVindoTelaMapaCotacao();

	}
	
	public void realizarCarregamentoVindoTelaMapaCotacao() {
		try {
			MapaCotacaoVO mapaCotacao = (MapaCotacaoVO) context().getExternalContext().getSessionMap().get("mapaCotacao");
			if (mapaCotacao != null) {
				montarDadosEdicao(mapaCotacao.getCotacaoVO());
				setMapaCotacaoVO(mapaCotacao);
				setHabilitarBotaoVoltar(true);
				context().getExternalContext().getSessionMap().remove("mapaCotacao");
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}


	public void inicializarResponsavel() {
		try {
			cotacaoVO.setResponsavelCotacao(getUsuarioLogadoClone());
		} catch (Exception e) {
		}
	}

	public String novo() {
		try {
			registrarAtividadeUsuario(getUsuarioLogadoClone(), "CotacaoControle", "Novo Cotação", "Novo");
			removerObjetoMemoria(this);
			setCotacaoVO(new CotacaoVO());
			inicializarListasSelectItemTodosComboBox();
			setCotacaoFornecedorVO(new CotacaoFornecedorVO());
			setCotacaoFornecedorVOTemp(new CotacaoFornecedorVO());
			setItemCotacaoVO(new ItemCotacaoVO());
			setItemCotacaoUnidadeEnsinoVO(new ItemCotacaoUnidadeEnsinoVO());
			inicializarResponsavel();
			setTituloQuantidade(Boolean.FALSE);
			setMensagemID("msg_entre_dados");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("cotacaoForm.xhtml");
	}

	public String editar() {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "CotacaoControle", "Inicializando Editar Cotação", "Editando");
			CotacaoVO obj = ((CotacaoVO) context().getExternalContext().getRequestMap().get("mapaCotacaoItens"));
			montarDadosEdicao(obj);
			registrarAtividadeUsuario(getUsuarioLogado(), "CotacaoControle", "Finalizando Editar Cotação", "Editando");
			setMensagemID("msg_dados_editar");
			return Uteis.getCaminhoRedirecionamentoNavegacao("cotacaoForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("cotacaoCons.xhtml");
		}
	}

	private void montarDadosEdicao(CotacaoVO obj) throws Exception {
		setCotacaoVO(getFacadeFactory().getCotacaoFacade().consultarCompletaPorChavePrimaria(obj.getCodigo(), false, getUsuarioLogado()));
		getFacadeFactory().getCotacaoFacade().atualizarCssItemCotacao(getCotacaoVO());
		setCotacaoFornecedorVO(new CotacaoFornecedorVO());
		setCotacaoFornecedorVOTemp(new CotacaoFornecedorVO());
		setItemCotacaoVO(new ItemCotacaoVO());
		setItemCotacaoUnidadeEnsinoVO(new ItemCotacaoUnidadeEnsinoVO());
		montarListaSelectItemTipoNivelCentroResultadoEnum();
		montarListaSelectItemTramiteCotacaoCompra("");
		montarListaSelectItemUnidadeEnsinoTramiteCotacaoCompra();
		montarListaUnidadeEnsino();
		inicializarListasSelectItemTodosComboBox();
		preencherUnidadeEnsinoCotacao();
	}

	private void preencherUnidadeEnsinoCotacao() {
		if (Uteis.isAtributoPreenchido(getCotacaoVO().getItemCotacaoVOs())) {
			Iterator<UnidadeEnsinoVO> i = getCotacaoVO().getListaUnidadeEnsinoVOs().iterator();
			while (i.hasNext()) {
				UnidadeEnsinoVO objExistente = i.next();
				boolean removerUnidadeEnsino = true;
				for (ItemCotacaoUnidadeEnsinoVO itemCotacaoUnidadeEnsino : getCotacaoVO().getItemCotacaoVOs().get(0).getListaItemCotacaoUnidadeEnsinoVOs()) {
					if (itemCotacaoUnidadeEnsino.getUnidadeEnsinoVO().getCodigo().equals(objExistente.getCodigo())) {
						removerUnidadeEnsino = false;
						break;
					}
				}
				if (removerUnidadeEnsino) {
					i.remove();
				}
			}
		}
	}

	public boolean isCotacaoAguardandoAutorizacaoOuAutorizada() {
		return this.getCotacaoVO().isAguardandoAutorizacao() || this.getCotacaoVO().isAutorizada();
	}

	public boolean isCotacaoAguardandoAutorizacaoOuAutorizadaEMapaCotacaoNaoENull() {
		return this.getCotacaoVO().isAguardandoAutorizacao() || this.getCotacaoVO().isAutorizada();
	}

	public CotacaoVO montarAtributosCotacaoVOCompleto(CotacaoVO obj) {
		try {
			return getFacadeFactory().getCotacaoFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
		return new CotacaoVO();
	}

	public void finalizarCotacao() {
		String situacaoAnterior = getCotacaoVO().getSituacao();
		setUsuarioResponsavelDepartamento((UsuarioVO) context().getExternalContext().getRequestMap().get("responsavelItem"));
		try {
			getCotacaoVO().setSituacao("AA");
			getFacadeFactory().getCotacaoFacade().liberarCotacaoParaMapaComTramitacao(getCotacaoVO(), getUsuarioResponsavelDepartamento(), getUsuarioLogado());
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			getCotacaoVO().setSituacao(situacaoAnterior);
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void liberarCotacaoParaMapaCotacao() {
		String situacaoAnterior = getCotacaoVO().getSituacao();
		try {
			this.abrirPanelSelecionar = false;
			CotacaoVO.validarDados(getCotacaoVO());
			getCotacaoVO().validarDadosParaLiberacaoMapa();
			validarDadosTramitacao();
			if (!abrirPanelSelecionar) {
				getCotacaoVO().setSituacao("AA");
				getFacadeFactory().getCotacaoFacade().liberarCotacaoParaMapaComTramitacao(getCotacaoVO(), new UsuarioVO(), getUsuarioLogado());
				setMensagemID("msg_dados_gravados");
			} else {
				inicializarMensagemVazia();
			}
		} catch (Exception e) {
			this.abrirPanelSelecionar = false;
			getCotacaoVO().setSituacao(situacaoAnterior);
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	private void validarDadosTramitacao() throws Exception {
		if (Uteis.isAtributoPreenchido(getCotacaoVO().getTramiteCotacaoCompra())) {
			getFacadeFactory().getTramiteFacade().popularListaDepartamentoTransite(getCotacaoVO().getTramiteCotacaoCompra(), getUsuarioLogado());
			Optional<DepartamentoTramiteCotacaoCompraVO> findFirst = getCotacaoVO().getTramiteCotacaoCompra()
					.getListaDepartamentoTramite()
					.stream()
					.filter(p -> new CotacaoHistoricoVO().validaCotacaoPassaDepartamento(getCotacaoVO(), p))
					.findFirst();

			if (findFirst.isPresent() && Uteis.isAtributoPreenchido(findFirst.get().getTipoDistribuicaoCotacao())) {
				switch (findFirst.get().getTipoDistribuicaoCotacao()) {
				case COORDENADOR_CURSO_ESPECIFICO_TRAMITE:
					this.listaUsuarioDepartamento = getFacadeFactory().getUsuarioFacade().consultaUsuarioCoordenadorPorDepartamento(findFirst.get().getDepartamentoVO(), null, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
					this.abrirPanelSelecionar = true;
					return;

				case FUNCIONARIO_TRAMITE:
					this.listaUsuarioDepartamento = getFacadeFactory().getUsuarioFacade().consultaUsuarioPorDepartamento(findFirst.get().getDepartamentoVO(), null, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
					this.abrirPanelSelecionar = true;
					return;
				}
			}			
		}
	}

	public String getOnAvancarAbrirPanelSelecionar() {
		if (this.abrirPanelSelecionar) {
			return "RichFaces.$('panelSelecionaResponsavel').show();";
		}
		return "";
	}

	public String gravar() {
		try {
			if (!Uteis.isAtributoPreenchido(getCotacaoVO())) {
				registrarAtividadeUsuario(getUsuarioLogado(), "CotacaoControle", "Inicializando Incluir Cotação", "Incluindo");
				getFacadeFactory().getCotacaoFacade().incluir(cotacaoVO, getUsuarioLogado());
				registrarAtividadeUsuario(getUsuarioLogado(), "CotacaoControle", "Finalizando Incluir Cotação", "Incluindo");
			} else {
				if (getCotacaoVO().isAutorizada()) {
					throw new Exception("Esta cotação não pode ser alterada");
				} else {
					registrarAtividadeUsuario(getUsuarioLogado(), "CotacaoControle", "Inicializando Alterar Cotação", "Alterando");
					getFacadeFactory().getCotacaoFacade().alterar(cotacaoVO, getUsuarioLogado());
					registrarAtividadeUsuario(getUsuarioLogado(), "CotacaoControle", "Finalizando Alterar Cotação", "Alterando");
				}
			}
			setMensagemID("msg_dados_gravados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("cotacaoForm.xhtml");
		} catch (Exception e) {
			e.printStackTrace();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("cotacaoForm.xhtml");
		}
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

	@Override
	public String consultar() {
		try {
			registrarAtividadeUsuario(getUsuarioLogadoClone(), "CotacaoControle", "Inicializando Consultar Cotação", "Consultando");
			super.consultar();
			getControleConsultaOtimizado().preencherDadosParaConsulta(true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			getCotacaoVO().getAtualCotacaoHistoricoVO().setResponsavel(getUsuarioLogadoClone());
			if (!getPermiteConsultarCotacoesOutrosResponsaveisTodasUnidadeEnsino() && !getPermiteConsultarCotacoesOutrosResponsaveisMesmaUnidadeEnsino() && !getPermiteConsultarCotacoesOutrosResponsaveisMesmoDepartamento()){
				getCotacaoVO().setResponsavelCotacao(getUsuarioLogadoClone());
			}
			if (getPermiteConsultarCotacoesOutrosResponsaveisTodasUnidadeEnsino()){
				getCotacaoVO().setResponsavelCotacao(new UsuarioVO());
				getCotacaoVO().getAtualCotacaoHistoricoVO().setResponsavel(new UsuarioVO());
			}
			getFacadeFactory().getCotacaoFacade().consultar(getCotacaoVO(), getDepartamentoFiltro(), getProdutoFiltro(), getRequisicao(), getEnumSituacaoTramitacaoFiltro(), getControleConsultaOtimizado() , getPermiteConsultarCotacoesOutrosResponsaveisTodasUnidadeEnsino(),getPermiteConsultarCotacoesOutrosResponsaveisMesmaUnidadeEnsino() ,getPermiteConsultarCotacoesOutrosResponsaveisMesmoDepartamento());
			registrarAtividadeUsuario(getUsuarioLogado(), "CotacaoControle", "Finalizando Consultar Cotação", "Consultando");
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());

		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("cotacaoCons.xhtml");
	}

	private void montarHistoricoPorMapaCotacao(MapaCotacaoVO mapa) throws Exception {
		mapa.setCotacaoVO(getFacadeFactory().getCotacaoFacade().consultarCompletaPorChavePrimaria(mapa.getCotacaoVO().getCodigo(), false, getUsuarioLogado()));
		mapa.setCotacaoHistorico(getFacadeFactory().getCotacaoHistoricoInterfaceFacade().consultarPorCotacao(mapa.getCotacaoVO(), false, getUsuarioLogado()));
		if (Objects.nonNull(mapa.getCotacaoHistorico())) {
			getFacadeFactory().getTramiteFacade().popularListaDepartamentoTransite(mapa.getCotacaoHistorico().getCotacao().getTramiteCotacaoCompra(), getUsuarioLogado());
		}
	}

	public String excluir() {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "CotacaoControle", "Inicializando Excluir Cotação", "Excluindo");
			if (!getCotacaoVO().isAutorizada()) {
				getFacadeFactory().getCotacaoFacade().excluir(cotacaoVO, getUsuarioLogado());
				setCotacaoVO(new CotacaoVO());
				inicializarListasSelectItemTodosComboBox();
				setCotacaoFornecedorVO(new CotacaoFornecedorVO());
				setItemCotacaoVO(new ItemCotacaoVO());
				setItemCotacaoUnidadeEnsinoVO(new ItemCotacaoUnidadeEnsinoVO());
				inicializarResponsavel();
				registrarAtividadeUsuario(getUsuarioLogado(), "CotacaoControle", "Finalizando Excluir Cotação", "Excluindo");
				setMensagemID("msg_dados_excluidos");
			} else {
				throw new Exception("Esta cotação não pode ser excluida.");
			}
			return Uteis.getCaminhoRedirecionamentoNavegacao("cotacaoForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("cotacaoForm.xhtml");
		}
	}

	public void consultarProduto() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaProduto().equals("codigo")) {
				if (getValorConsultaProduto().equals("")) {
					setValorConsultaProduto("0");
				}
				objs = getFacadeFactory().getProdutoServicoFacade().consultarPorCodigoECategoriaProdutoAtivo(new Integer(getValorConsultaProduto()), getCotacaoVO().getCategoriaProduto().getCodigo(), null, true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getCampoConsultaProduto().equals("nome")) {
				objs = getFacadeFactory().getProdutoServicoFacade().consultarPorNomeECategoriaProdutoAtivo(getValorConsultaProduto(), getCotacaoVO().getCategoriaProduto().getCodigo(), null, true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			setListaConsultaProduto(objs);
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setListaConsultaProduto(new ArrayList(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void selecionarItemCotacaoVO() {
		try {
			setItemCotacaoVO((ItemCotacaoVO) context().getExternalContext().getRequestMap().get("itemCotacaoFornecedorItens"));
			if (getItemCotacaoVO().getProduto().getControlarEstoque().booleanValue()) {
				setTituloQuantidade(Boolean.FALSE);
			} else {
				setTituloQuantidade(Boolean.TRUE);
			}
			getItemCotacaoVO().setAlterou(false);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void atualizarCotacaoFornecedorEscolha() {
		try {
			ItemCotacaoVO item = (ItemCotacaoVO) context().getExternalContext().getRequestMap().get("itemCotacaoFornecedorItens");
			getFacadeFactory().getCotacaoFacade().atualizarCotacaoFornecedorEscolha(getCotacaoVO(), item, getUsuarioLogado());
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void atualizarCotacaoFornecedor() {
		try {
			setCotacaoVO(getFacadeFactory().getCotacaoFacade().montarListaCotacao(getCotacaoVO(), getItemCotacaoVO(), true, getUsuarioLogado()));
			setItemCotacaoVO(new ItemCotacaoVO());
			setItemCotacaoUnidadeEnsinoVO(new ItemCotacaoUnidadeEnsinoVO());
			inicializarMensagemVazia();
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void atualizarItemCotacao() {
		try {
			ItemCotacaoVO item = (ItemCotacaoVO) context().getExternalContext().getRequestMap().get("itemCotacaoFornecedorItens");
			setCotacaoVO(getFacadeFactory().getCotacaoFacade().montarListaCotacao(getCotacaoVO(), item, true, getUsuarioLogado()));
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void removerProduto() {
		try {
			ItemCotacaoVO obj = (ItemCotacaoVO) context().getExternalContext().getRequestMap().get("itemCotacaoFornecedorItens");
			setCotacaoVO(getFacadeFactory().getCotacaoFacade().removerItemCotacaoVO(getCotacaoVO(), obj, getUsuarioLogado()));
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}

	}

	public void selecionarProdutoServico() {
		try {
			ProdutoServicoVO obj = (ProdutoServicoVO) context().getExternalContext().getRequestMap().get("produtoServicoItens");
			setItemCotacaoVO(new ItemCotacaoVO());
			getItemCotacaoVO().setProduto(obj);
			getFacadeFactory().getCotacaoFacade().adicionarProdutoServicoNaCotacao(null, getCotacaoVO(), getItemCotacaoVO(), getUsuarioLogado());
			setItemCotacaoVO(new ItemCotacaoVO());
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void selecionarItemCotacaoUnidadeEnsinoVO() {
		try {
			setItemCotacaoUnidadeEnsinoVO((ItemCotacaoUnidadeEnsinoVO) context().getExternalContext().getRequestMap().get("itemCotacaoUnidadeEnsinoItens"));
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void selecionarRequisicao() {
		try {
			RequisicaoItemVO obj = (RequisicaoItemVO) context().getExternalContext().getRequestMap().get("itemRequisicao");
			setRequisicaoItemVO(obj);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void removerItemRequisicao() {
		try {
			getItemCotacaoUnidadeEnsinoVO().excluirObjItemRequisicaoVOs(getRequisicaoItemVO().getCodigo());
			setRequisicaoItemVO(new RequisicaoItemVO());
			getItemCotacaoVO().setAlterou(true);
			setMensagemID(MSG_TELA.msg_dados_excluidos.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void inicializarDadosListaCotacao() {
		getListaConsultaProduto().clear();
	}

	public List<SelectItem> getTipoConsultaComboProdutoServico() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	public void fecharFinanceiro()  {
		try {
			setCotacaoFornecedorVO(new CotacaoFornecedorVO());	
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
		
	}

	public void adicionarCotacaoFornecedor() {
		try {
			getCotacaoFornecedorVOTemp().setFornecedor(getFacadeFactory().getFornecedorFacade().consultarPorChavePrimaria(getCotacaoFornecedorVOTemp().getFornecedor().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			getFacadeFactory().getCotacaoFacade().adicionarNovaCotacaoFornecedor(getCotacaoFornecedorVOTemp(), getCotacaoVO());
			this.setCotacaoFornecedorVOTemp(new CotacaoFornecedorVO());
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void removerCotacaoFornecedor() {
		try {
			CotacaoFornecedorVO obj = (CotacaoFornecedorVO) context().getExternalContext().getRequestMap().get("fornecedorItens");
			getFacadeFactory().getCotacaoFacade().excluirObjCotacaoFornecedorVOs(getCotacaoVO(), obj.getFornecedor().getCodigo(), getUsuarioLogado());
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void verItemCotacaoFornecedor() {
		try {
			CotacaoFornecedorVO obj = (CotacaoFornecedorVO) context().getExternalContext().getRequestMap().get("fornecedorItens");
			this.setCotacaoFornecedorVO(obj);
			List<ParcelaCondicaoPagamentoVO> objs = getFacadeFactory().getParcelaCondicaoPagamentoFacade().consultarParcelaCondicaoPagamentos(getCotacaoFornecedorVO().getCondicaoPagamento().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS);
			getCotacaoFornecedorVO().montarListaCondicaoPagamento(getCotacaoVO().getDataCotacao(), objs);
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void montarListaCondicaoPagamento() {
		try {
			if(Uteis.isAtributoPreenchido(getCotacaoFornecedorVO().getFormaPagamento())){
				getCotacaoFornecedorVO().setFormaPagamento(getFacadeFactory().getFormaPagamentoFacade().consultarPorChavePrimaria(getCotacaoFornecedorVO().getFormaPagamento().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			}
			if(Uteis.isAtributoPreenchido(getCotacaoFornecedorVO().getCondicaoPagamento())){
				getCotacaoFornecedorVO().setCondicaoPagamento(getFacadeFactory().getCondicaoPagamentoFacade().consultarPorChavePrimaria(getCotacaoFornecedorVO().getCondicaoPagamento().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			}
			List<ParcelaCondicaoPagamentoVO> objs = getFacadeFactory().getParcelaCondicaoPagamentoFacade().consultarParcelaCondicaoPagamentos(getCotacaoFornecedorVO().getCondicaoPagamento().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS);
			if (getCotacaoVO().getDataAutorizacao() != null) {
				getCotacaoFornecedorVO().montarListaCondicaoPagamento(getCotacaoVO().getDataAutorizacao(), objs);
			} else {
				getCotacaoFornecedorVO().montarListaCondicaoPagamento(new Date(), objs);
			}
			inicializarMensagemVazia();
		} catch (Exception e) {
			getCotacaoFornecedorVO().setListaCondicaoPagamento(new ArrayList(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
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

	public void montarListaSelectItemFormaPagamento() {
		try {
			montarListaSelectItemFormaPagamento("");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void montarListaSelectItemFormaPagamento(String prm) throws Exception {
		getListaSelectItemFormaPagamento().clear();
		getListaSelectItemFormaPagamento().add(new SelectItem(0, ""));
		List<FormaPagamentoVO> resultadoConsulta = getFacadeFactory().getFormaPagamentoFacade().consultarPorNome("", false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
		resultadoConsulta.stream().forEach(p -> getListaSelectItemFormaPagamento().add(new SelectItem(p.getCodigo(), p.getNome())));
	}

	public void montarListaSelectItemFornecedor() {
		try {
			montarListaSelectItemFornecedor("");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void montarListaSelectItemFornecedor(String prm) throws Exception {
		if (!getCotacaoVO().isCategoriaProdutoInformada()) {
			setListaSelectItemFornecedor(new ArrayList<>(0));
			return;
		}
		getListaSelectItemFornecedor().clear();
		getListaSelectItemFornecedor().add(new SelectItem(0, ""));
		List<FornecedorVO> resultadoConsulta = getFacadeFactory().getFornecedorFacade().consultarPorCategoriaProduto(getCotacaoVO().getCategoriaProduto().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		resultadoConsulta.stream().forEach(p -> getListaSelectItemFornecedor().add(new SelectItem(p.getCodigo(), p.getNome())));
	}

	public void montarListaSelectItemCondicaoPagamento(Integer prm) throws Exception {
		List<CondicaoPagamentoVO> resultadoConsulta = getFacadeFactory().getCondicaoPagamentoFacade().consultarPorCodigo(prm, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		getListaSelectItemCondicaoPagamento().clear();
		getListaSelectItemCondicaoPagamento().add(new SelectItem(0, ""));
		resultadoConsulta.stream().forEach(p -> getListaSelectItemCondicaoPagamento().add(new SelectItem(p.getCodigo(), p.getNome())));

	}

	public void montarListaSelectItemCondicaoPagamento() {
		try {
			montarListaSelectItemCondicaoPagamento(0);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void imprimirPDF() {
		List<CotacaoRelVO> listaObjetos = null;
		try {
			setMapaCotacaoVO(new MapaCotacaoVO());
			getMapaCotacaoVO().setCotacaoVO(getCotacaoVO());
			getFacadeFactory().getMapaCotacaoFacade().preencherDadosCompraCotacao(getMapaCotacaoVO(), getUsuarioLogado());
			listaObjetos = getFacadeFactory().getMapaCotacaoFacade().getListaCotacaoRelatorio(this.getMapaCotacaoVO(), getUsuarioLogado());
			Collections.sort(listaObjetos, Comparator.comparing(CotacaoRelVO::getCodigoFornecedor));
			registrarAtividadeUsuario(getUsuarioLogado(), "MapaCotacaoControle", "Inicializando Geração de Relatório de Cotação", "Emitindo Relatório");
			getSuperParametroRelVO().setTituloRelatorio("Relatório Cotação");
			getSuperParametroRelVO().setNomeDesignIreport(this.getDesign());
			getSuperParametroRelVO().setSubReport_Dir(EstoqueRel.getCaminhoBaseRelatorio());

			if (!listaObjetos.isEmpty()) {
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setListaObjetos(listaObjetos);
				getSuperParametroRelVO().setQuantidade(listaObjetos.size());
				getSuperParametroRelVO().setCaminhoBaseRelatorio(this.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCodigo(getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado()).getNome());
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().adicionarParametro("listaCotacaoHistorico", this.getMapaCotacaoVO().getCotacaoVO().getListaCotacaoHistoricoVOs());
				realizarImpressaoRelatorio();
				registrarAtividadeUsuario(getUsuarioLogado(), "MapaCotacaoControle", "Finalizando Geração de Relatório de Cotação", "Finalizando Relatório");
				setMensagemID("msg_relatorio_ok");
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}
		} catch (Exception e) {
			e.printStackTrace();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		} finally {
			Uteis.liberarListaMemoria(listaObjetos);
		}

	}

	public String getCaminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "compras" + File.separator);
	}

	public String getDesign() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "compras" + File.separator + "CotacaoRel.jrxml");
	}

	public void inicializarListasSelectItemTodosComboBox() {
		montarListaSelectItemFornecedor();
		montarListaSelectItemCondicaoPagamento();
		montarListaSelectItemFormaPagamento();
		montarListaSelectItemUnidadeEnsino();
	}

	public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
		List<UnidadeEnsinoVO> resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
		for (UnidadeEnsinoVO obj : resultadoConsulta) {
			obj.setEscolhidaParaFazerCotacao(true);
		}
		getCotacaoVO().setListaUnidadeEnsinoVOs(resultadoConsulta);
	}

	public void montarListaSelectItemUnidadeEnsino() {
		try {
			montarListaSelectItemUnidadeEnsino("");
		} catch (Exception e) {
			// System.out.println(e.getMessage());
		}
	}

	public List consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
		List lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		return lista;
	}

	public void consultarUnidadeEnsino() {
		try {
			setListaConsultarUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarUnidadeEnsinoFaltandoLista(getCotacaoVO().getListaUnidadeEnsinoVOs(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultarUnidadeEnsino(new ArrayList(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}	

	public void selecionarUnidadeEnsino() {
		try {
			UnidadeEnsinoVO unidadeEnsinoVO = (UnidadeEnsinoVO) context().getExternalContext().getRequestMap().get("unidadeEnsinoConsItens");
			for (UnidadeEnsinoVO objExistente : getCotacaoVO().getListaUnidadeEnsinoVOs()) {
				if (objExistente.getCodigo().intValue() == unidadeEnsinoVO.getCodigo().intValue()) {
					return;
				}
			}
			unidadeEnsinoVO.setEscolhidaParaFazerCotacao(true);
			getCotacaoVO().getListaUnidadeEnsinoVOs().add(unidadeEnsinoVO);
			for (CotacaoFornecedorVO cotFor : getCotacaoVO().getCotacaoFornecedorVOs()) {
				for (ItemCotacaoVO itemCot : cotFor.getItemCotacaoVOs()) {
					setItemCotacaoUnidadeEnsinoVO(new ItemCotacaoUnidadeEnsinoVO());
					getItemCotacaoUnidadeEnsinoVO().setProdutoVO(itemCot.getProduto());
					getItemCotacaoUnidadeEnsinoVO().setUnidadeEnsinoVO(unidadeEnsinoVO);
					if (itemCot.getProduto().getControlarEstoque()) {
						EstoqueVO estoque = getFacadeFactory().getEstoqueFacade().consultarEstoquerPorProdutoPorUnidadeValidandoEstoqueMinino(itemCot.getProduto().getCodigo(), unidadeEnsinoVO.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS);
						getItemCotacaoUnidadeEnsinoVO().setQtdMinimaUnidade(estoque.getEstoqueMinimo());
					}
					getItemCotacaoUnidadeEnsinoVO().setListaRequisicaoItemVOs(getFacadeFactory().getRequisicaoItemFacade().consultarRequisicaoItemsPorItemCotacaoUnidadeEnsinoVO(null, getItemCotacaoUnidadeEnsinoVO(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
					itemCot.adicionarObjItemCotacaoUnidadeEnsinoVO(getItemCotacaoUnidadeEnsinoVO());
				}
			}
			getFacadeFactory().getCotacaoFacade().gerarCentroResultadoOrigem(getCotacaoVO(), getUsuarioLogado());
			consultarUnidadeEnsino();
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}

	}

	public void removerTodasUnidadeEnsino() {
		try {
			for (UnidadeEnsinoVO unidadeEnsino : getCotacaoVO().getListaUnidadeEnsinoVOs()) {
				getFacadeFactory().getCotacaoFacade().removerUnidadeEnsinoVO(getCotacaoVO(), unidadeEnsino, getUsuarioLogado());
			}
			getCotacaoVO().getListaUnidadeEnsinoVOs().clear();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}

	}

	public void removerUnidadeEnsino() {
		try {
			UnidadeEnsinoVO unidadeEnsinoVO = (UnidadeEnsinoVO) context().getExternalContext().getRequestMap().get("unidadeEnsinoItens");
			Iterator<UnidadeEnsinoVO> i = getCotacaoVO().getListaUnidadeEnsinoVOs().iterator();
			while (i.hasNext()) {
				UnidadeEnsinoVO objExistente = i.next();
				if (objExistente.getCodigo().intValue() == unidadeEnsinoVO.getCodigo().intValue()) {
					i.remove();
					break;
				}
			}
			getFacadeFactory().getCotacaoFacade().removerUnidadeEnsinoVO(getCotacaoVO(), unidadeEnsinoVO, getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setCotacaoVO(new CotacaoVO());
		getCotacaoVO().setSituacao("");
		setDepartamentoFiltro(0);
		setProdutoFiltro("");
		setMensagemID("msg_entre_prmconsulta");
		init();
		return Uteis.getCaminhoRedirecionamentoNavegacao("cotacaoCons.xhtml");
	}

	public void limparCampoCategoriaDespesa() {
		getCotacaoVO().setCursoCategoriaDespesa(new CursoVO());
		getCotacaoVO().setTurnoCategoriaDespesa(new TurnoVO());
		getCotacaoVO().setTurmaCategoriaDespesa(new TurmaVO());
		getCotacaoVO().setDepartamentoCategoriaDespesa(new DepartamentoVO());
		getCotacaoVO().setUnidadeEnsinoCategoriaDespesa(new UnidadeEnsinoVO());
		getCotacaoVO().setFuncionarioCategoriaDespesa(new FuncionarioVO());
		getCotacaoVO().setCentroResultadoAdministrativo(new CentroResultadoVO());
		montarListaUnidadeEnsino();
		preencherDadosPorCategoriaDespesa();
	}

	public List<SelectItem> getTipoConsultaComboCategoriaDespesa() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("descricao", "Descrição"));
		itens.add(new SelectItem("identificadorCategoriaDespesa", "Identificador Centro Despesa"));
		return itens;
	}

	public void consultarCategoriaDespesa() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaCategoriaDespesa().equals("descricao")) {
				objs = getFacadeFactory().getCategoriaDespesaFacade().consultarPorDescricao(getValorConsultaCategoriaDespesa(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getCampoConsultaCategoriaDespesa().equals("identificadorCategoriaDespesa")) {
				objs = getFacadeFactory().getCategoriaDespesaFacade().consultarPorIdentificadorCategoriaDespesa(getValorConsultaCategoriaDespesa(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			setListaConsultaCategoriaDespesa(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCategoriaDespesa(new ArrayList(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void selecionarCategoriaDespesa() {
		try {
			CategoriaDespesaVO obj = (CategoriaDespesaVO) context().getExternalContext().getRequestMap().get("categoriaDespesaItens");
			this.getCotacaoVO().setCategoriaDespesa(obj);
			montarListaSelectItemTipoNivelCentroResultadoEnum();
			limparCampoCategoriaDespesa();
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void selecionarCategoriaProduto() {
		try {
			CategoriaProdutoVO obj = (CategoriaProdutoVO) context().getExternalContext().getRequestMap().get("categoriaProdutoItem");
			getCotacaoVO().setCategoriaProduto(getFacadeFactory().getCategoriaProdutoFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			getCotacaoVO().setCategoriaDespesa(getCotacaoVO().getCategoriaProduto().getCategoriaDespesa());
			getCotacaoVO().getListaCentroResultadoOrigemVOs().clear();
			getCotacaoVO().getCotacaoFornecedorVOs().clear();
			montarListaSelectItemTipoNivelCentroResultadoEnum();
			limparCampoCategoriaDespesa();
			montarListaSelectItemTramiteCotacaoCompra();
			montarListaSelectItemUnidadeEnsinoTramiteCotacaoCompra();
			montarListaSelectItemFornecedor();
			preencherDadosPorCategoriaDespesa();
			this.listaConsultaCategoriaProduto.clear();
			this.valorConsultaCategoriaProduto = null;
			this.campoConsultaCategoriaProduto = null;
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarCategoriaProduto() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaCategoriaProduto().equals("codigo")) {
				if (getValorConsultaCategoriaProduto().equals("")) {
					setValorConsultaCategoriaProduto("0");
				}
				int valorInt = Uteis.getValorInteiro(getValorConsultaCategoriaProduto());
				objs = getFacadeFactory().getCategoriaProdutoFacade().consultarPorCodigo((valorInt), false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			}
			if (getCampoConsultaCategoriaProduto().equals("nome")) {
				objs = getFacadeFactory().getCategoriaProdutoFacade().consultarPorNome(getValorConsultaCategoriaProduto(), false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			}
			setListaConsultaCategoriaProduto(objs);
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setListaConsultaCategoriaProduto(new ArrayList(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public List getTipoConsultaComboCategoriaProduto() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	public void consultarDepartamento() {
		try {
			List objs = new ArrayList(0);
			Integer unidadeEnsino = 0;
			if (Uteis.isAtributoPreenchido(getCotacaoVO().getUnidadeEnsinoResponsavelTramitacao())) {
				unidadeEnsino = getCotacaoVO().getUnidadeEnsinoResponsavelTramitacao().getCodigo();
			} else {
				unidadeEnsino = getUnidadeEnsinoLogado().getCodigo();
			}
			if (getCampoConsultaDepartamento().equals("codigo")) {
				objs = getFacadeFactory().getDepartamentoFacade().consultarPorCodigoPorUnidadeEnsino(Uteis.getValorInteiro(getValorConsultaDepartamento()), unidadeEnsino, true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaDepartamento().equals("nome")) {
				objs = getFacadeFactory().getDepartamentoFacade().consultarPorNomePorUnidadeEnsino(getValorConsultaDepartamento(), unidadeEnsino, true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaDepartamento(objs);
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void selecionarDepartamento() {
		try {
			DepartamentoVO obj = (DepartamentoVO) context().getExternalContext().getRequestMap().get("departamentoItens");
			getCotacaoVO().setDepartamentoCategoriaDespesa(obj);
			preencherDadosPorCategoriaDespesa();
			getListaConsultaDepartamento().clear();
			setValorConsultaDepartamento("");
			setCampoConsultaDepartamento("");
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public List getTipoConsultaComboDepartamento() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	public void selecionarTurma() {
		try {
			TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
			obj = getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			getCotacaoVO().setTurmaCategoriaDespesa(obj);
			preencherDadosPorCategoriaDespesa();
			setCampoConsultaTurma("");
			setValorConsultaTurma("");
			setListaConsultaTurma(null);
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void limparTurma() {
		try {
			getCotacaoVO().setTurmaCategoriaDespesa(new TurmaVO());
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarTurma() {
		try {
			super.consultar();
			Integer unidadeEnsino = Uteis.isAtributoPreenchido(getCotacaoVO().getUnidadeEnsinoCategoriaDespesa()) ? getCotacaoVO().getUnidadeEnsinoCategoriaDespesa().getCodigo() : getCotacaoVO().getUnidadeEnsinoResponsavelTramitacao().getCodigo();
			setListaConsultaTurma(getFacadeFactory().getTurmaFacade().consultar(getCampoConsultaTurma(), getValorConsultaTurma(), unidadeEnsino, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			getListaConsultaTurma().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarCursoCategoriaDespesa() {
		try {
			Integer unidadeEnsino = Uteis.isAtributoPreenchido(getCotacaoVO().getUnidadeEnsinoCategoriaDespesa()) ? getCotacaoVO().getUnidadeEnsinoCategoriaDespesa().getCodigo() : getCotacaoVO().getUnidadeEnsinoResponsavelTramitacao().getCodigo();
			getListaConsultaCurso().addAll(getFacadeFactory().getCursoFacade().consultar(getCampoConsultaCurso(), getValorConsultaCurso(), unidadeEnsino, true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setListaConsultaCurso(null);
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void limparCurso() {
		try {
			getCotacaoVO().setCursoCategoriaDespesa(new CursoVO());
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void selecionarCursoCategoriaDespesa() {
		try {
			CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItens");
			getCotacaoVO().setCursoCategoriaDespesa(obj);
			preencherDadosPorCategoriaDespesa();
			listaConsultaCurso.clear();
			this.setValorConsultaCurso("");
			this.setCampoConsultaCurso("");
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public void consultarCursoTurnoCategoriaDespesa() {
		try {
			Integer unidadeEnsino = Uteis.isAtributoPreenchido(getCotacaoVO().getUnidadeEnsinoCategoriaDespesa()) ? getCotacaoVO().getUnidadeEnsinoCategoriaDespesa().getCodigo() : getCotacaoVO().getUnidadeEnsinoResponsavelTramitacao().getCodigo();
			setListaConsultaCursoTurno(getFacadeFactory().getUnidadeEnsinoCursoFacade().consultar(getCampoConsultaCursoTurno(), getValorConsultaCursoTurno(), unidadeEnsino, true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setListaConsultaCursoTurno(null);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarCursoTurnoCategoriaDespesa() {
		try {
			UnidadeEnsinoCursoVO obj = (UnidadeEnsinoCursoVO) context().getExternalContext().getRequestMap().get("unidadeEnsinoCursoItens");
			getCotacaoVO().setTurnoCategoriaDespesa(obj.getTurno());
			getCotacaoVO().setCursoCategoriaDespesa(obj.getCurso());
			preencherDadosPorCategoriaDespesa();
			setListaConsultaCursoTurno(null);
			this.setValorConsultaCursoTurno("");
			this.setCampoConsultaCursoTurno("");
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public void limparTurno() {
		try {
			getCotacaoVO().setTurnoCategoriaDespesa(new TurnoVO());
			getCotacaoVO().setCursoCategoriaDespesa(new CursoVO());
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void preencherDadosPorCategoriaDespesa() {
		try {
			if(Uteis.isAtributoPreenchido(getCotacaoVO().getUnidadeEnsinoCategoriaDespesa())){
				getCotacaoVO().setUnidadeEnsinoCategoriaDespesa(getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCodigo(getCotacaoVO().getUnidadeEnsinoCategoriaDespesa().getCodigo(), false, getUsuarioLogado()));
			}
			if(Uteis.isAtributoPreenchido(getCotacaoVO().getTipoNivelCentroResultadoEnum()) && getCotacaoVO().getTipoNivelCentroResultadoEnum().isDepartamento() && !Uteis.isAtributoPreenchido(getCotacaoVO().getDepartamentoCategoriaDespesa()) ){
				getCotacaoVO().setDepartamentoCategoriaDespesa(getFacadeFactory().getDepartamentoFacade().consultarDepartamentoControlaEstoquePorUnidadeEnsino(getCotacaoVO().getUnidadeEnsinoCategoriaDespesa().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			}
			
			getFacadeFactory().getCotacaoFacade().preencherDadosPorCategoriaDespesa(getCotacaoVO(), getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void inicializarDadoConsultaCentroResultadoAdministrativo() {
		try {
			setCentroResultadoAdministrativo(true);
			inicializarDadosComunsCentroResultado();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}

	}	

	private void inicializarDadosComunsCentroResultado() {
		setCentroResultadoDataModelo(new DataModelo());
		getCentroResultadoDataModelo().setCampoConsulta(CentroResultadoVO.enumCampoConsultaCentroResultado.DESCRICAO_CENTRO_RESULTADO.name());
	}

	public void selecionarCentroResultado() {
		try {
			CentroResultadoVO obj = (CentroResultadoVO) context().getExternalContext().getRequestMap().get("centroResultadoItens");
			if (isCentroResultadoAdministrativo()) {
				getCotacaoVO().setCentroResultadoAdministrativo(obj);
			} 
			getFacadeFactory().getCotacaoFacade().gerarCentroResultadoOrigem(getCotacaoVO(), getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void scrollerListenerCentroResultado(DataScrollEvent dataScrollerEvent) {
		try {
			getCentroResultadoDataModelo().setPaginaAtual(dataScrollerEvent.getPage());
			getCentroResultadoDataModelo().setPage(dataScrollerEvent.getPage());
			consultarCentroResultado();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarCentroResultado() {
		try {
			super.consultar();
			getCentroResultadoDataModelo().preencherDadosParaConsulta(false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			getFacadeFactory().getCentroResultadoFacade().consultar(SituacaoEnum.ATIVO, true, getCotacaoVO().getDepartamentoCategoriaDespesa(), getCotacaoVO().getCursoCategoriaDespesa(), getCotacaoVO().getTurmaCategoriaDespesa(), getCentroResultadoDataModelo());
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			getControleConsultaOtimizado().getListaConsulta().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarProdutoRequisicao() {
		try {
			setListaRequisicaoVO(getFacadeFactory().getRequisicaoFacade().consultarPorCategoriaProdutoPorUnidadeEnsinoPorTipoAutorizacaoRequisicaoComSituacaoAutorizadaComSituacaoEntreguePendente(getCotacaoVO().getCategoriaProduto().getCodigo(), getCotacaoVO().getListaUnidadeEnsinoVOs(), TipoAutorizacaoRequisicaoEnum.COTACAO, false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void adicionarProdutoRequisicao() {
		try {
			List<RequisicaoVO> listaRequisicao = getListaRequisicaoVO().stream().filter(RequisicaoVO::isSelecionado).collect(Collectors.toList());
			if (Uteis.isAtributoPreenchido(listaRequisicao)) {
				List<ProdutoServicoVO> listaProduto = getFacadeFactory().getProdutoServicoFacade().consultarPorListaRequisicao(listaRequisicao, TipoAutorizacaoRequisicaoEnum.COTACAO, false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
				for (ProdutoServicoVO produtoServicoVO : listaProduto) {
					ItemCotacaoVO item = new ItemCotacaoVO();
					item.setProduto(produtoServicoVO);
					getFacadeFactory().getCotacaoFacade().adicionarProdutoServicoNaCotacao(listaRequisicao, getCotacaoVO(), item, getUsuarioLogado());
				}
			}
			setMensagemID(MSG_TELA.msg_dados_adicionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public String realizarNavegacaoParaTelaMapaCotacao() {
		try {
			context().getExternalContext().getSessionMap().put("mapaCotacao", getMapaCotacaoVO());
			removerControleMemoriaFlash("MapaCotacaoControle");
			removerControleMemoriaTela("MapaCotacaoControle");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("mapaCotacaoForm.xhtml");
	}
	
	public void montarListaSelectItemUnidadeEnsinoTramiteCotacaoCompra()  {
		try {
			getListaSelectItemUnidadeEnsinoTramiteCotacao().clear();
			
			if(Uteis.isAtributoPreenchido(getCotacaoVO().getTramiteCotacaoCompra().getUnidadeEnsinoPadrao().getCodigo())) {
				getCotacaoVO().setUnidadeEnsinoResponsavelTramitacao(getCotacaoVO().getTramiteCotacaoCompra().getUnidadeEnsinoPadrao());				
			}
			else if (getUnidadeEnsinoLogado().getCodigo().intValue() != 0) {
				UnidadeEnsinoVO obj = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCodigo(getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
				getCotacaoVO().setUnidadeEnsinoResponsavelTramitacao(obj);
				//getListaSelectItemUnidadeEnsinoTramiteCotacao().add(new SelectItem(obj.getCodigo(), obj.getNome()));
			}
			List<UnidadeEnsinoVO> resultadoConsulta = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome("", 0, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			setListaSelectItemUnidadeEnsinoTramiteCotacao(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
							
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void montarListaSelectItemTramiteCotacaoCompra() {
		try {
			getCotacaoVO().setTramiteCotacaoCompra(null);
			montarListaSelectItemTramiteCotacaoCompra("");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void montarListaSelectItemTramiteCotacaoCompra(String prm) throws Exception {
		getListaSelectItemTramiteCotacaoCompra().clear();
		if (!getCotacaoVO().getCategoriaProduto().isPossuiTramiteEspecifico()) {
			List<TramiteCotacaoCompraVO> listaTramiteCotacaoCompraVO = getFacadeFactory().getTramiteFacade().consultarSituacaoAtivaTramitePadrao(true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			if (Uteis.isAtributoPreenchido(listaTramiteCotacaoCompraVO)) {
				if (!Uteis.isAtributoPreenchido(getCotacaoVO().getTramiteCotacaoCompra())) {
					getCotacaoVO().setTramiteCotacaoCompra(listaTramiteCotacaoCompraVO.get(0));
				}
				listaTramiteCotacaoCompraVO.stream().forEach(p -> getListaSelectItemTramiteCotacaoCompra().add(new SelectItem(p.getCodigo(), p.getNome())));
			}
		} else {
			TramiteCotacaoCompraVO tramite = getFacadeFactory().getTramiteFacade().consultarPorChavePrimaria(getCotacaoVO().getCategoriaProduto().getTramiteCotacaoCompra().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			getListaSelectItemTramiteCotacaoCompra().add(new SelectItem(tramite.getCodigo(), tramite.getNome()));
			if (!Uteis.isAtributoPreenchido(getCotacaoVO().getTramiteCotacaoCompra())) {
				getCotacaoVO().setTramiteCotacaoCompra(tramite);
			}
		}
	}

	public void montarListaUnidadeEnsino() {
		try {
			montarListaSelectItemUnidadeEnsino(0);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	private void montarListaSelectItemUnidadeEnsino(Integer prm) throws Exception {
		getListaSelectItemUnidadeEnsino().clear();
		if (getPermiteConsultarCotacoesOutrosResponsaveisTodasUnidadeEnsino() || getUnidadeEnsinoLogado().getCodigo().intValue() == 0){
			getListaSelectItemUnidadeEnsino().add(new SelectItem(0, ""));
			List<UnidadeEnsinoVO> resultadoConsulta = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome("", 0, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
		} else if (getUnidadeEnsinoLogado().getCodigo().intValue() != 0) {
			UnidadeEnsinoVO obj = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCodigo(getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			getCotacaoVO().setUnidadeEnsinoResponsavelTramitacao(obj);
			getCotacaoVO().setUnidadeEnsinoCategoriaDespesa(obj);
			getListaSelectItemUnidadeEnsino().add(new SelectItem(obj.getCodigo(), obj.getNome()));
		}
	}

	public void montarListaDepartamento() {
		try {
			montarListaSelectItemDepartamento();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	private void montarListaSelectItemDepartamento() {
		getListaSelectItemDepartamento().clear();
		List<DepartamentoVO> listaDepartamento = new ArrayList<>();
		try {
			if (getPermiteConsultarCotacoesOutrosResponsaveisTodasUnidadeEnsino() || getPermiteConsultarCotacoesOutrosResponsaveisMesmaUnidadeEnsino()) {
				listaDepartamento.addAll(getFacadeFactory().getDepartamentoFacade().consultarDepartamentoPorDepartamentoTramiteExistente(false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			} else {
				listaDepartamento.addAll(getFacadeFactory().getDepartamentoFacade().consultarPorCodigoPessoaFuncionario(getUsuarioLogado().getPessoa().getCodigo(), null, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			}
			if (getListaSelectItemDepartamento().size() > 1) {
				getListaSelectItemDepartamento().add(new SelectItem(0, " "));
			}
			listaDepartamento.stream().forEach(p ->getListaSelectItemDepartamento().add(new SelectItem(p.getCodigo(), p.getNome())));
		
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	public void limparCamposPorTipoNivelCentroResultadoEnum() {
		try {
			getCotacaoVO().limparCamposPorTipoNivelCentroResultadoEnum();
			preencherDadosPorCategoriaDespesa();
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}

	}
	
	private void montarListaSelectItemTipoNivelCentroResultadoEnum() {
		try {
			getListaSelectItemTipoNivelCentroResultadoEnum().clear();
			if(getCotacaoVO().isCategoriaDespesaInformada()){
				getFacadeFactory().getCategoriaDespesaFacade().montarListaSelectItemTipoNivelCentroResultadoEnum(getCotacaoVO().getCategoriaDespesa(), getListaSelectItemTipoNivelCentroResultadoEnum());
				if(!getListaSelectItemTipoNivelCentroResultadoEnum().isEmpty() && !Uteis.isAtributoPreenchido(getCotacaoVO().getTipoNivelCentroResultadoEnum())){
					getCotacaoVO().setTipoNivelCentroResultadoEnum((TipoNivelCentroResultadoEnum) getListaSelectItemTipoNivelCentroResultadoEnum().get(0).getValue());	
				}
				
			}
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
		
	}

	public String getCampoConsultaCurso() {
		if (campoConsultaCurso == null) {
			campoConsultaCurso = "";
		}
		return campoConsultaCurso;
	}

	public void setCampoConsultaCurso(String campoConsultaCurso) {
		this.campoConsultaCurso = campoConsultaCurso;
	}

	public List<CursoVO> getListaConsultaCurso() {
		if (listaConsultaCurso == null) {
			listaConsultaCurso = new ArrayList<CursoVO>(0);

		}
		return listaConsultaCurso;
	}

	public void setListaConsultaCurso(List<CursoVO> listaConsultaCurso) {
		this.listaConsultaCurso = listaConsultaCurso;
	}

	public String getValorConsultaCurso() {
		if (valorConsultaCurso == null) {
			valorConsultaCurso = "";
		}
		return valorConsultaCurso;
	}

	public void setValorConsultaCurso(String valorConsultaCurso) {
		this.valorConsultaCurso = valorConsultaCurso;
	}

	public String getCampoConsultaCursoTurno() {
		if (campoConsultaCursoTurno == null) {
			campoConsultaCursoTurno = "";
		}
		return campoConsultaCursoTurno;
	}

	public void setCampoConsultaCursoTurno(String campoConsultaCursoTurno) {
		this.campoConsultaCursoTurno = campoConsultaCursoTurno;
	}

	public List<UnidadeEnsinoCursoVO> getListaConsultaCursoTurno() {
		if (listaConsultaCursoTurno == null) {
			listaConsultaCursoTurno = new ArrayList<UnidadeEnsinoCursoVO>(0);
		}
		return listaConsultaCursoTurno;
	}

	public void setListaConsultaCursoTurno(List<UnidadeEnsinoCursoVO> listaConsultaCursoTurno) {
		this.listaConsultaCursoTurno = listaConsultaCursoTurno;
	}

	public String getValorConsultaCursoTurno() {
		if (valorConsultaCursoTurno == null) {
			valorConsultaCursoTurno = "";
		}
		return valorConsultaCursoTurno;
	}

	public void setValorConsultaCursoTurno(String valorConsultaCursoTurno) {
		this.valorConsultaCursoTurno = valorConsultaCursoTurno;
	}

	public List getListaConsultarUnidadeEnsino() {
		if (listaConsultarUnidadeEnsino == null) {
			listaConsultarUnidadeEnsino = new ArrayList(0);
		}
		return listaConsultarUnidadeEnsino;
	}

	public void setListaConsultarUnidadeEnsino(List listaConsultarUnidadeEnsino) {
		this.listaConsultarUnidadeEnsino = listaConsultarUnidadeEnsino;
	}

	public List<SelectItem> getTipoConsultaComboCentroDespesa() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("descricao", "Descrição"));
		itens.add(new SelectItem("identificadorCentroDespesa", "Identificador Centro Despesa"));
		return itens;
	}

	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList(0);
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}
	
	

	public List<SelectItem> getListaSelectItemUnidadeEnsinoTramiteCotacao() {
		listaSelectItemUnidadeEnsinoTramiteCotacao = Optional.ofNullable(listaSelectItemUnidadeEnsinoTramiteCotacao).orElse(new ArrayList<>());
		return listaSelectItemUnidadeEnsinoTramiteCotacao;
	}

	public void setListaSelectItemUnidadeEnsinoTramiteCotacao(List<SelectItem> listaSelectItemUnidadeEnsinoTramiteCotacao) {
		this.listaSelectItemUnidadeEnsinoTramiteCotacao = listaSelectItemUnidadeEnsinoTramiteCotacao;
	}

	public String getCampoConsultaTurma() {
		if (campoConsultaTurma == null) {
			campoConsultaTurma = "";
		}
		return campoConsultaTurma;
	}

	public void setCampoConsultaTurma(String campoConsultaTurma) {
		this.campoConsultaTurma = campoConsultaTurma;
	}

	public String getValorConsultaTurma() {
		if (valorConsultaTurma == null) {
			valorConsultaTurma = "";
		}
		return valorConsultaTurma;
	}

	public void setValorConsultaTurma(String valorConsultaTurma) {
		this.valorConsultaTurma = valorConsultaTurma;
	}

	public List<SelectItem> getTipoConsultaComboTurma() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("identificadorTurma", "Identificador"));
		itens.add(new SelectItem("nomeUnidadeEnsino", "Unidade Ensino"));
		itens.add(new SelectItem("nomeTurno", "Turno"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
	}

	public List<TurmaVO> getListaConsultaTurma() {
		if (listaConsultaTurma == null) {
			listaConsultaTurma = new ArrayList<TurmaVO>(0);
		}
		return listaConsultaTurma;
	}

	public void setListaConsultaTurma(List<TurmaVO> listaConsultaTurma) {
		this.listaConsultaTurma = listaConsultaTurma;
	}

	public List<SelectItem> getTipoConsultaComboCurso() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("nome", "Nome"));
		return itens;
	}

	public List<SelectItem> getTipoConsultaComboCursoTurno() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("curso", "Curso"));
		itens.add(new SelectItem("turno", "Turno"));
		return itens;
	}

	public List<SelectItem> getTipoConsultaComboSituacaoCotacao() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("", "Todas"));
		itens.add(new SelectItem("EC", "Em Cotação"));
		itens.add(new SelectItem("AA", "Aguardando Autorização"));
		itens.add(new SelectItem("AU", "Autorizado"));
		itens.add(new SelectItem("RC", "Revisar Cotação"));
		itens.add(new SelectItem("IN", "Indeferido"));
		return itens;
	}

	public boolean isMostrarBotaoFinalizarCotacao() {
		return (getCotacaoVO().isPermiteAlterarCotacao());
	}

	public boolean getIsApresentarCampoCurso() {
		return Uteis.isAtributoPreenchido(getCotacaoVO().getTipoNivelCentroResultadoEnum()) && (getCotacaoVO().getTipoNivelCentroResultadoEnum().isCurso());
	}

	public boolean getIsApresentarCampoCursoTurno() {
		return Uteis.isAtributoPreenchido(getCotacaoVO().getTipoNivelCentroResultadoEnum()) && (getCotacaoVO().getTipoNivelCentroResultadoEnum().isCursoTurno());
	}

	public boolean getIsApresentarCampoTurma() {
		return Uteis.isAtributoPreenchido(getCotacaoVO().getTipoNivelCentroResultadoEnum()) && (getCotacaoVO().getTipoNivelCentroResultadoEnum().isTurma());
	}

	public boolean isApresentarCampoDepartamento() {
		return Uteis.isAtributoPreenchido(getCotacaoVO().getTipoNivelCentroResultadoEnum()) && (getCotacaoVO().getTipoNivelCentroResultadoEnum().isDepartamento());
		
	}
	

	public Integer getDepartamentoFiltro() {
		departamentoFiltro = Optional.ofNullable(departamentoFiltro).orElse(0);
		return departamentoFiltro;
	}

	public void setDepartamentoFiltro(Integer departamentoFiltro) {
		this.departamentoFiltro = departamentoFiltro;
	}

	public String getProdutoFiltro() {
		produtoFiltro = Optional.ofNullable(produtoFiltro).orElse("");
		return produtoFiltro;
	}

	public void setProdutoFiltro(String produtoFiltro) {
		this.produtoFiltro = produtoFiltro;
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

	public String getCampoConsultaCategoriaProduto() {
		if (campoConsultaCategoriaProduto == null) {
			campoConsultaCategoriaProduto = "";
		}
		return campoConsultaCategoriaProduto;
	}

	public void setCampoConsultaCategoriaProduto(String campoConsultaCategoriaProduto) {
		this.campoConsultaCategoriaProduto = campoConsultaCategoriaProduto;
	}

	public List getListaConsultaCategoriaProduto() {
		return listaConsultaCategoriaProduto;
	}

	public void setListaConsultaCategoriaProduto(List listaConsultaCategoriaProduto) {
		this.listaConsultaCategoriaProduto = listaConsultaCategoriaProduto;
	}

	public String getValorConsultaCategoriaProduto() {
		return valorConsultaCategoriaProduto;
	}

	public void setValorConsultaCategoriaProduto(String valorConsultaCategoriaProduto) {
		this.valorConsultaCategoriaProduto = valorConsultaCategoriaProduto;
	}

	public String getCampoConsultaDepartamento() {
		if (campoConsultaDepartamento == null) {
			campoConsultaDepartamento = "";
		}
		return campoConsultaDepartamento;
	}

	public void setCampoConsultaDepartamento(String campoConsultaDepartamento) {
		this.campoConsultaDepartamento = campoConsultaDepartamento;
	}

	public String getValorConsultaDepartamento() {
		if (valorConsultaDepartamento == null) {
			valorConsultaDepartamento = "";
		}
		return valorConsultaDepartamento;
	}

	public void setValorConsultaDepartamento(String valorConsultaDepartamento) {
		this.valorConsultaDepartamento = valorConsultaDepartamento;
	}

	public List getListaConsultaDepartamento() {
		if (listaConsultaDepartamento == null) {
			listaConsultaDepartamento = new ArrayList(0);
		}
		return listaConsultaDepartamento;
	}

	public void setListaConsultaDepartamento(List listaConsultaDepartamento) {
		this.listaConsultaDepartamento = listaConsultaDepartamento;
	}

	public boolean isCentroResultadoAdministrativo() {
		return centroResultadoAdministrativo;
	}

	public void setCentroResultadoAdministrativo(boolean centroResultadoAdministrativo) {
		this.centroResultadoAdministrativo = centroResultadoAdministrativo;
	}

	public DataModelo getCentroResultadoDataModelo() {
		centroResultadoDataModelo = Optional.ofNullable(centroResultadoDataModelo).orElse(new DataModelo());
		return centroResultadoDataModelo;
	}

	public void setCentroResultadoDataModelo(DataModelo centroResultadoDataModelo) {
		this.centroResultadoDataModelo = centroResultadoDataModelo;
	}

	public List<SelectItem> getListaSelectItemTramiteCotacaoCompra() {
		listaSelectItemTramiteCotacaoCompra = Optional.ofNullable(listaSelectItemTramiteCotacaoCompra).orElse(new ArrayList<>());
		return listaSelectItemTramiteCotacaoCompra;
	}

	public void setListaSelectItemTramiteCotacaoCompra(List<SelectItem> listaSelectItemTramiteCotacaoCompra) {
		this.listaSelectItemTramiteCotacaoCompra = listaSelectItemTramiteCotacaoCompra;
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

	public List getListaConsultaProduto() {
		if (listaConsultaProduto == null) {
			listaConsultaProduto = new ArrayList(0);
		}
		return listaConsultaProduto;
	}

	public void setListaConsultaProduto(List listaConsultaProduto) {
		this.listaConsultaProduto = listaConsultaProduto;
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

	public List<SelectItem> getListaSelectItemFornecedor() {
		if (listaSelectItemFornecedor == null) {
			listaSelectItemFornecedor = new ArrayList<>();
		}
		return (listaSelectItemFornecedor);
	}

	public void setListaSelectItemFornecedor(List listaSelectItemFornecedor) {
		this.listaSelectItemFornecedor = listaSelectItemFornecedor;
	}

	public CotacaoFornecedorVO getCotacaoFornecedorVO() {
		return cotacaoFornecedorVO;
	}

	public void setCotacaoFornecedorVO(CotacaoFornecedorVO cotacaoFornecedorVO) {
		this.cotacaoFornecedorVO = cotacaoFornecedorVO;
	}

	public CotacaoVO getCotacaoVO() {
		if (cotacaoVO == null) {
			cotacaoVO = new CotacaoVO();
		}
		return cotacaoVO;
	}

	public void setCotacaoVO(CotacaoVO cotacaoVO) {
		this.cotacaoVO = cotacaoVO;
	}

	public ItemCotacaoUnidadeEnsinoVO getItemCotacaoUnidadeEnsinoVO() {
		return itemCotacaoUnidadeEnsinoVO;
	}

	public void setItemCotacaoUnidadeEnsinoVO(ItemCotacaoUnidadeEnsinoVO itemCotacaoUnidadeEnsinoVO) {
		this.itemCotacaoUnidadeEnsinoVO = itemCotacaoUnidadeEnsinoVO;
	}

	public ItemCotacaoVO getItemCotacaoVO() {
		return itemCotacaoVO;
	}

	public void setItemCotacaoVO(ItemCotacaoVO itemCotacaoVO) {
		this.itemCotacaoVO = itemCotacaoVO;
	}

	public List<SelectItem> getListaSelectItemCondicaoPagamento() {
		if (listaSelectItemCondicaoPagamento == null) {
			listaSelectItemCondicaoPagamento = new ArrayList<>();
		}
		return listaSelectItemCondicaoPagamento;
	}

	public void setListaSelectItemCondicaoPagamento(List<SelectItem> listaSelectItemCondicaoPagamento) {
		this.listaSelectItemCondicaoPagamento = listaSelectItemCondicaoPagamento;
	}

	public List<SelectItem> getListaSelectItemFormaPagamento() {
		if (listaSelectItemFormaPagamento == null) {
			listaSelectItemFormaPagamento = new ArrayList<>();
		}
		return listaSelectItemFormaPagamento;
	}

	public void setListaSelectItemFormaPagamento(List listaSelectItemFormaPagamento) {
		this.listaSelectItemFormaPagamento = listaSelectItemFormaPagamento;
	}

	public List<SelectItem> getListaSelectItemDepartamento() {
		listaSelectItemDepartamento = Optional.ofNullable(listaSelectItemDepartamento).orElse(new ArrayList<>());
		return listaSelectItemDepartamento;
	}

	public void setListaSelectItemDepartamento(List<SelectItem> listaSelectItemDepartamento) {
		this.listaSelectItemDepartamento = listaSelectItemDepartamento;
	}
	
	public List<SelectItem> getListaSelectItemTipoNivelCentroResultadoEnum() {
		listaSelectItemTipoNivelCentroResultadoEnum = Optional.ofNullable(listaSelectItemTipoNivelCentroResultadoEnum).orElse(new ArrayList<>());
		return listaSelectItemTipoNivelCentroResultadoEnum;
	}

	public void setListaSelectItemTipoNivelCentroResultadoEnum(List<SelectItem> listaSelectItemTipoNivelCentroResultadoEnum) {
		this.listaSelectItemTipoNivelCentroResultadoEnum = listaSelectItemTipoNivelCentroResultadoEnum;
	}

	public RequisicaoItemVO getRequisicaoItemVO() {
		return requisicaoItemVO;
	}

	public void setRequisicaoItemVO(RequisicaoItemVO requisicaoItemVO) {
		this.requisicaoItemVO = requisicaoItemVO;
	}

	public List<RequisicaoVO> getListaRequisicaoVO() {
		listaRequisicaoVO = Optional.ofNullable(listaRequisicaoVO).orElse(new ArrayList<>());
		return listaRequisicaoVO;
	}

	public void setListaRequisicaoVO(List<RequisicaoVO> listaRequisicaoVO) {
		this.listaRequisicaoVO = listaRequisicaoVO;
	}

	public Integer getTamanhoListaUnidadeEnsinoVOs() {
		if (getCotacaoVO().getListaUnidadeEnsinoVOs().isEmpty()) {
			return 0;
		} else {
			return getCotacaoVO().getListaUnidadeEnsinoVOs().size();
		}
	}

	public Integer getColunasUnidadeEnsino() {
		if (getCotacaoVO().getListaUnidadeEnsinoVOs().size() < 3) {
			return getCotacaoVO().getListaUnidadeEnsinoVOs().size();
		}
		return 3;
	}

	public CotacaoFornecedorVO getCotacaoFornecedorVOTemp() {
		return cotacaoFornecedorVOTemp;
	}

	public void setCotacaoFornecedorVOTemp(CotacaoFornecedorVO cotacaoFornecedorVOTemp) {
		this.cotacaoFornecedorVOTemp = cotacaoFornecedorVOTemp;
	}

	public UsuarioVO getUsuarioResponsavelDepartamento() {
		usuarioResponsavelDepartamento = Optional.ofNullable(usuarioResponsavelDepartamento).orElse(new UsuarioVO());
		return usuarioResponsavelDepartamento;
	}

	public void setUsuarioResponsavelDepartamento(UsuarioVO usuarioResponsavelDepartamento) {
		this.usuarioResponsavelDepartamento = usuarioResponsavelDepartamento;
	}

	public List<UsuarioVO> getListaUsuarioDepartamento() {
		listaUsuarioDepartamento = Optional.ofNullable(listaUsuarioDepartamento).orElse(new ArrayList<>());
		return listaUsuarioDepartamento;
	}

	public void setListaUsuarioDepartamento(List<UsuarioVO> listaUsuarioDepartamento) {
		this.listaUsuarioDepartamento = listaUsuarioDepartamento;
	}

	public MapaCotacaoVO getMapaCotacaoVO() {
		mapaCotacaoVO = Optional.ofNullable(mapaCotacaoVO).orElse(new MapaCotacaoVO());
		return mapaCotacaoVO;
	}

	public void setMapaCotacaoVO(MapaCotacaoVO mapaCotacaoVO) {
		this.mapaCotacaoVO = mapaCotacaoVO;
	}

	@Override
	protected void limparRecursosMemoria() {
		super.limparRecursosMemoria();
		cotacaoVO = null;
		Uteis.liberarListaMemoria(listaSelectItemFormaPagamento);
		Uteis.liberarListaMemoria(listaSelectItemCondicaoPagamento);
		cotacaoFornecedorVO = null;
		itemCotacaoVO = null;
		Uteis.liberarListaMemoria(listaSelectItemFornecedor);
	}

	public boolean isTituloQuantidade() {
		return tituloQuantidade;
	}

	public void setTituloQuantidade(boolean tituloQuantidade) {
		this.tituloQuantidade = tituloQuantidade;
	}

	public boolean isHabilitarBotaoVoltar() {
		return habilitarBotaoVoltar;
	}

	public void setHabilitarBotaoVoltar(boolean habilitarBotaoVoltar) {
		this.habilitarBotaoVoltar = habilitarBotaoVoltar;
	}

	public EnumSituacaoTramitacao getEnumSituacaoTramitacaoFiltro() {
		enumSituacaoTramitacaoFiltro = Optional.ofNullable(enumSituacaoTramitacaoFiltro).orElse(EnumSituacaoTramitacao.TODOS);
		return enumSituacaoTramitacaoFiltro;
	}

	public void setEnumSituacaoTramitacaoFiltro(EnumSituacaoTramitacao enumSituacaoTramitacaoFiltro) {
		this.enumSituacaoTramitacaoFiltro = enumSituacaoTramitacaoFiltro;
	}

	public boolean getPermiteConsultarCotacoesOutrosResponsaveisTodasUnidadeEnsino() {
		try {
			MapaCotacao.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoComprasEnum.PERMITE_CONSULTAR_COTACAO_OUTROS_RESPONSAVEIS_TODAS_UNIDADE_ENSINO, getUsuarioLogado());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean getPermiteConsultarCotacoesOutrosResponsaveisMesmaUnidadeEnsino() {
		try {
			MapaCotacao.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoComprasEnum.PERMITE_CONSULTAR_COTACAO_OUTROS_RESPONSAVEIS_MESMA_UNIDADE_ENSINO, getUsuarioLogado());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean getPermiteConsultarCotacoesOutrosResponsaveisMesmoDepartamento() {
		try {
			MapaCotacao.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoComprasEnum.PERMITE_CONSULTAR_COTACAO_OUTROS_RESPONSAVEIS_MESMO_DEPARTAMENTO, getUsuarioLogado());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean isPermiteAlterarCategoriaDespesa() {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoComprasEnum.PERMITE_ALTERAR_CATEGORIA_DESPESA_COTACAO, getUsuarioLogado());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean isPermiteAlterarCentroResultado() {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoComprasEnum.PERMITE_ALTERAR_CENTRO_RESULTADO_COTACAO, getUsuarioLogado());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public List<SelectItem> getTipoConsultaComboEnumSituacaoTramitacao() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem(EnumSituacaoTramitacao.TODOS, EnumSituacaoTramitacao.TODOS.getTitulo()));
		itens.add(new SelectItem(EnumSituacaoTramitacao.NO_PRAZO, EnumSituacaoTramitacao.NO_PRAZO.getTitulo()));
		itens.add(new SelectItem(EnumSituacaoTramitacao.ATRAZADO, EnumSituacaoTramitacao.ATRAZADO.getTitulo()));
		return itens;
	}

	public enum EnumSituacaoTramitacao {
		TODOS("Todos"), NO_PRAZO("No Prazo"), ATRAZADO("Atrasado");
		String titulo;

		private EnumSituacaoTramitacao(String titulo) {
			this.titulo = titulo;
		}

		public String getTitulo() {
			return titulo;
		}

		public boolean isTodos() {
			return Uteis.isAtributoPreenchido(name()) && name().equals(EnumSituacaoTramitacao.TODOS.name());
		}

		public boolean isNoPrazo() {
			return Uteis.isAtributoPreenchido(name()) && name().equals(EnumSituacaoTramitacao.NO_PRAZO.name());
		}

		public boolean isAtrazado() {
			return Uteis.isAtributoPreenchido(name()) && name().equals(EnumSituacaoTramitacao.ATRAZADO.name());
		}
	}
	
	public void downloadArquivoRequisicao() throws Exception {
		try {
		 
			RequisicaoItemVO obj = (RequisicaoItemVO) context().getExternalContext().getRequestMap().get("itemRequisicao");
			String arquivo = (getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.ARQUIVO + File.separator  + obj.getRequisicaoVO().getArquivoVO().getCpfAlunoDocumentacao() + File.separator + obj.getRequisicaoVO().getArquivoVO().getNome());
			InputStream fs = new FileInputStream(arquivo);
			
			HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
			request.getSession().setAttribute("nomeArquivo", obj.getRequisicaoVO().getArquivoVO().getNome());
			request.getSession().setAttribute("pastaBaseArquivo", arquivo.substring(0, arquivo.lastIndexOf(File.separator)));
			request.getSession().setAttribute("deletarArquivo", false);
			context().getExternalContext().dispatch("/DownloadSV");
			FacesContext.getCurrentInstance().responseComplete();
			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}


	public Integer getCodigoRequisicao() {
		if (codigoRequisicao == null) {
			codigoRequisicao = 0;
		}
		return codigoRequisicao;
	}

	public void setCodigoRequisicao(Integer codigoRequisicao) {
		this.codigoRequisicao = codigoRequisicao;
	}

	public String getJustificativaRequisicao() {
		if (justificativaRequisicao == null) {
			justificativaRequisicao = "";
		}
		return justificativaRequisicao;
	}

	public void setJustificativaRequisicao(String justificativaRequisicao) {
		this.justificativaRequisicao = justificativaRequisicao;
	}
	
	public void realizarMontagemJustificativa() {
		try {
			RequisicaoVO requisicaoVO = (RequisicaoVO) context().getExternalContext().getRequestMap().get("requisicaoItens");
			if (Uteis.isAtributoPreenchido(requisicaoVO)) {
				setCodigoRequisicao(requisicaoVO.getCodigo());
				setJustificativaRequisicao(requisicaoVO.getMotivoSituacaoAutorizacao());
			} else {
				setCodigoRequisicao(0);
				setJustificativaRequisicao("");
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public Integer getRequisicao() {
		if (requisicao == null) {
			requisicao = 0;
		}
		return requisicao;
	}

	public void setRequisicao(Integer requisicao) {
		this.requisicao = requisicao;
	}
}
