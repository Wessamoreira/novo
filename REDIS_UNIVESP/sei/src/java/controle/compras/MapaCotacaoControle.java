/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controle.compras;

import java.io.File;
/**
 * 
 * @author Rodrigo
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.richfaces.event.FileUploadEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import controle.compras.CotacaoControle.EnumSituacaoTramitacao;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoComprasEnum;
import negocio.comuns.compras.CompraAgrupador;
import negocio.comuns.compras.CompraVO;
import negocio.comuns.compras.CotacaoHistoricoVO;
import negocio.comuns.compras.CotacaoRelVO;
import negocio.comuns.compras.CotacaoVO;
import negocio.comuns.compras.DepartamentoTramiteCotacaoCompraVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.compras.MapaCotacaoVO;
import negocio.comuns.compras.MaterialTramiteCotacaoCompraVO;
import negocio.comuns.compras.ProdutoAgrupador;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.OrigemArquivo;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.dominios.SituacaoArquivo;
import negocio.facade.jdbc.compras.MapaCotacao;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.compras.EstoqueRel;

@Controller("MapaCotacaoControle")
@Scope("viewScope")
@Lazy
public class MapaCotacaoControle extends SuperControleRelatorio implements Serializable {

	private static final long serialVersionUID = -413777982221367825L;
	private MapaCotacaoVO mapaCotacaoVO;
	private CompraVO compraVO;
	private List<CompraAgrupador> agrupadoresCompras;

	private boolean revisao;
	private boolean retorno;
	private boolean gravado;
	private UsuarioVO usuarioSelecionado;
	private boolean trocaResponsavel;
	private CotacaoHistoricoVO cotacaoHistoricoSelecionada;
	private List<DepartamentoVO> departamentos = new ArrayList<>();
	private List<UsuarioVO> usuarios = new ArrayList<>();
	private List<UsuarioVO> listaUsuarioDepartamento = new ArrayList<>();
	private MaterialTramiteCotacaoCompraVO materialTramiteCotacaoCompra;
	private boolean abrirPanelSelecionar;
	private ProdutoAgrupador produtoAgrupador;

	private String campoTrocaResponsavel;
	private String valorTrocaResponsavel;

	/**
	 * campos filtros
	 */
	private Integer departamentoFiltro;
	private String produtoFiltro;
	private EnumSituacaoTramitacao enumSituacaoTramitacaoFiltro;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private List<SelectItem> listaSelectItemDepartamento;

	public MapaCotacaoControle()  {
		init();
	}

	public void init() {
		setControleConsulta(new ControleConsulta());
		setControleConsultaOtimizado(new DataModelo());
		getControleConsultaOtimizado().setDataIni(Uteis.obterDataFutura(new Date(), -5));
		getControleConsultaOtimizado().setDataFim(Uteis.getNewDateComUmMesAMais());
		setMapaCotacaoVO(new MapaCotacaoVO());
		getMapaCotacaoVO().setCotacaoVO(new CotacaoVO());
		getMapaCotacaoVO().getCotacaoVO().setSituacao("MC");
		getMapaCotacaoVO().getCotacaoVO().setAtualCotacaoHistoricoVO(new CotacaoHistoricoVO());
		getMapaCotacaoVO().getCotacaoVO().getAtualCotacaoHistoricoVO().setResponsavel(new UsuarioVO());
		setDepartamentoFiltro(0);
		setProdutoFiltro("");
		setEnumSituacaoTramitacaoFiltro(EnumSituacaoTramitacao.TODOS);
		setRevisao(false);
		setGravado(false);
		setCompraVO(new CompraVO());
		montarListaUnidadeEnsino();
		montarListaDepartamento();
		if(!realizarCarregamentoVindoTelaCotacao()){
			consultar();	
		}
	}
	
	public boolean realizarCarregamentoVindoTelaCotacao() {
		try {
			MapaCotacaoVO mapaCotacao = (MapaCotacaoVO) context().getExternalContext().getSessionMap().get("mapaCotacao");
			if (mapaCotacao != null) {
				setGravado(false);
				setMapaCotacaoVO(new MapaCotacaoVO());
				getMapaCotacaoVO().setCotacaoVO(mapaCotacao.getCotacaoVO());
				if (getMapaCotacaoVO().getCompraVOs().isEmpty()) {
					getFacadeFactory().getMapaCotacaoFacade().preencherDadosCompraCotacao(getMapaCotacaoVO(), getUsuarioLogado());
					montarListaCotacaoHistorico();
					this.agruparCompras(getMapaCotacaoVO().getCompraVOs());
				}
				context().getExternalContext().getSessionMap().remove("mapaCotacao");
				return true;
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
		return false;
	}

	@Override
	public String consultar() {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "MapaCotacaoControle", "Inicializando Consultar Mapa Cotação", "Consultando");
			super.consultar();
			getControleConsultaOtimizado().preencherDadosParaConsulta(true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			if (!isApresentarReponsavelTramitacao()) {
				getMapaCotacaoVO().getCotacaoVO().getAtualCotacaoHistoricoVO().setResponsavel(getUsuarioLogadoClone());
			}
			getMapaCotacaoVO().getCotacaoVO().setResponsavelCotacao(getUsuarioLogadoClone());
			getMapaCotacaoVO().getCotacaoVO().getAtualCotacaoHistoricoVO().setResponsavel(getUsuarioLogadoClone());
			if (!getPermiteConsultarCotacoesOutrosResponsaveisTodasUnidadeEnsino() && !getPermiteConsultarCotacoesOutrosResponsaveisMesmaUnidadeEnsino() && !getPermiteConsultarCotacoesOutrosResponsaveisMesmoDepartamento()){
				getMapaCotacaoVO().getCotacaoVO().setResponsavelCotacao(getUsuarioLogadoClone());
			}
			if (getPermiteConsultarCotacoesOutrosResponsaveisTodasUnidadeEnsino()){
				getMapaCotacaoVO().getCotacaoVO().setResponsavelCotacao(new UsuarioVO());
				getMapaCotacaoVO().getCotacaoVO().getAtualCotacaoHistoricoVO().setResponsavel(new UsuarioVO());
			}
			getFacadeFactory().getMapaCotacaoFacade().consultar(getMapaCotacaoVO().getCotacaoVO(), getDepartamentoFiltro(), getProdutoFiltro(), getEnumSituacaoTramitacaoFiltro(), getControleConsultaOtimizado(), getPermiteConsultarCotacoesOutrosResponsaveisTodasUnidadeEnsino(),getPermiteConsultarCotacoesOutrosResponsaveisMesmaUnidadeEnsino() ,getPermiteConsultarCotacoesOutrosResponsaveisMesmoDepartamento());
			registrarAtividadeUsuario(getUsuarioLogado(), "MapaCotacaoControle", "Finalizando Consultar Mapa Cotação", "Consultando");
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());

		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("mapaCotacaoCons.xhtml");
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

	public String visualizar() {
		try {
			CotacaoVO obj = (CotacaoVO) context().getExternalContext().getRequestMap().get("mapaCotacaoItens");
			setGravado(false);
			setMapaCotacaoVO(new MapaCotacaoVO());
			getMapaCotacaoVO().setCotacaoVO(getFacadeFactory().getCotacaoFacade().consultarCompletaPorChavePrimaria(obj.getCodigo(), false, getUsuarioLogado()));
			if (getMapaCotacaoVO().getCompraVOs().isEmpty()) {
				getFacadeFactory().getMapaCotacaoFacade().preencherDadosCompraCotacao(getMapaCotacaoVO(), getUsuarioLogado());
				montarListaCotacaoHistorico();
				this.agruparCompras(getMapaCotacaoVO().getCompraVOs());
			}
			setMensagemID("msg_dados_consultados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("mapaCotacaoForm.xhtml");
		} catch (Exception e) {
			setMapaCotacaoVO(new MapaCotacaoVO());
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("mapaCotacaoCons.xhtml");

		}
	}

	public void agruparCompras(List<CompraVO> list) {
		Map<FornecedorVO, List<CompraVO>> collect = list.stream().collect(Collectors.groupingBy(CompraVO::getFornecedor));
		ArrayList<CompraAgrupador> agrupadores = new ArrayList<>();
		collect.entrySet().forEach(p -> agrupadores.add(new CompraAgrupador(p.getKey(), p.getValue())));
		getAgrupadoresCompras().clear();
		getAgrupadoresCompras().addAll(agrupadores);

	}

	public String inicializarConsultar() {
		try {
//			removerObjetoMemoria(this);
			this.init();
			setMensagemID("msg_entre_prmconsulta");
			return Uteis.getCaminhoRedirecionamentoNavegacao("mapaCotacaoCons.xhtml");
		} catch (Exception e) {
			return Uteis.getCaminhoRedirecionamentoNavegacao("mapaCotacaoCons.xhtml");
		}
	}

	public void montarListaCotacaoHistorico() throws Exception {
		getMapaCotacaoVO().setCotacaoHistorico(getFacadeFactory().getCotacaoHistoricoInterfaceFacade().consultarPorCotacao(getMapaCotacaoVO().getCotacaoVO(), false, getUsuarioLogado()));
		if (Objects.nonNull(getMapaCotacaoVO().getCotacaoHistorico())) {
			getFacadeFactory().getTramiteFacade().popularListaDepartamentoTransite(getMapaCotacaoVO().getCotacaoVO().getTramiteCotacaoCompra(), getUsuarioLogado());
		}
		if (Objects.isNull(getMapaCotacaoVO().getCotacaoHistorico())) {
			return;
		}
		getMapaCotacaoVO().getCotacaoVO().getListaCotacaoHistoricoVOs().clear();
		CotacaoHistoricoVO parametro = getMapaCotacaoVO().getCotacaoHistorico();
		getMapaCotacaoVO().getCotacaoVO().getListaCotacaoHistoricoVOs().add(parametro);
		while (Objects.nonNull(parametro.getAnteriorHistorico())) {
			parametro = parametro.getAnteriorHistorico();
			getMapaCotacaoVO().getCotacaoVO().getListaCotacaoHistoricoVOs().add(parametro);
		}
	}

	public List<CotacaoHistoricoVO> montarrListaCotacaoHistorico(CotacaoHistoricoVO cotacaoHistorico) {
		ArrayList<CotacaoHistoricoVO> arrayList = new ArrayList<>();
		if(Uteis.isAtributoPreenchido(cotacaoHistorico)){
			CotacaoHistoricoVO parametro = cotacaoHistorico;
			arrayList.add(parametro);
			while (Objects.nonNull(parametro.getAnteriorHistorico())) {
				parametro = parametro.getAnteriorHistorico();
				arrayList.add(parametro);
			}
		}
		return arrayList;
	}

	public String avancarTramiteCotacao() {
		try {
			CotacaoHistoricoVO cotacaoHistorico = this.getMapaCotacaoVO().getCotacaoHistorico();
			if (getFacadeFactory().getCotacaoHistoricoInterfaceFacade().historicoAnteriorPossuiOrdemMaiorQueAtual(cotacaoHistorico)) {
				return this.concluirAvancar(cotacaoHistorico);
			}
			DepartamentoTramiteCotacaoCompraVO tramiteCotacaoCompraVO = getFacadeFactory().getCotacaoHistoricoInterfaceFacade().buscarProximoDepartamentoTramiteCotacaoCompra(cotacaoHistorico);
			switch (tramiteCotacaoCompraVO.getTipoDistribuicaoCotacao()) {
			case COORDENADOR_CURSO_ESPECIFICO_TRAMITE:
				if (isAbrirPopup()) {
					this.listaUsuarioDepartamento = getFacadeFactory().getUsuarioFacade().consultaUsuarioCoordenadorPorDepartamento(tramiteCotacaoCompraVO.getDepartamentoVO(), null, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
					this.abrirPanelSelecionar = true;
					return "";
				}
				break;

			case FUNCIONARIO_TRAMITE:
				if (isAbrirPopup()) {
					this.listaUsuarioDepartamento = getFacadeFactory().getUsuarioFacade().consultaUsuarioPorDepartamento(tramiteCotacaoCompraVO.getDepartamentoVO(), null, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
					this.abrirPanelSelecionar = true;
					return "";
				}
				break;

			}
			this.abrirPanelSelecionar = false;
			return concluirAvancar(cotacaoHistorico);
		} catch (Exception e) {
			e.printStackTrace();
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("mapaCotacaoForm.xhtml");

		}
	}

	private String concluirAvancar(CotacaoHistoricoVO cotacaoHistorico) throws Exception {
		getFacadeFactory().getCotacaoHistoricoInterfaceFacade().avancarTramiteCotacao(cotacaoHistorico, this.usuarioSelecionado, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
		this.init();
		this.usuarioSelecionado = null;
		setMensagemID("msg_dados_gravados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("mapaCotacaoCons.xhtml");
	}

	public String getOnAvancarAbrirPanelSelecionar() {
		if (this.abrirPanelSelecionar) {
			return "RichFaces.$('panelSelecionaResponsavel').show();";
		}
		return "";
	}

	private boolean isAbrirPopup() {
		return Objects.isNull(usuarioSelecionado);
	}

	public String alterarTramiteCotacao() {
		try {
			getFacadeFactory().getCotacaoHistoricoInterfaceFacade().alterar(this.getMapaCotacaoVO().getCotacaoHistorico(), false, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
			this.init();
			setMensagemID("msg_dados_gravados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("mapaCotacaoCons.xhtml");
		} catch (Exception e) {
			e.printStackTrace();
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("mapaCotacaoForm.xhtml");
		}
	}

	public String retornarTramiteCotacao() {
		try {
			getFacadeFactory().getCotacaoHistoricoInterfaceFacade().retornarTramiteCotacao(this.getMapaCotacaoVO().getCotacaoHistorico(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
			this.init();
			setMensagemID("msg_dados_gravados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("mapaCotacaoCons.xhtml");
		} catch (Exception e) {
			e.printStackTrace();
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("mapaCotacaoForm.xhtml");
		}
	}

	public void autorizar() {
		String situacaoCotacaoAnterior = getMapaCotacaoVO().getCotacaoVO().getSituacao();
		try {
			Uteis.checkState(!getMapaCotacaoVO().getCotacaoVO().isAguardandoAutorizacao(), "Nao e possivel autorizar a cotacao, pois a mesma encontra-se com a situacao diferente de aguardando autorizacao.");
			getMapaCotacaoVO().getCotacaoVO().setSituacao("AU");
			getMapaCotacaoVO().getCotacaoVO().setMotivoRevisao("");
			getFacadeFactory().getMapaCotacaoFacade().aprovarCotacao(getMapaCotacaoVO(), situacaoCotacaoAnterior, getUsuarioLogado(), getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(getMapaCotacaoVO().getCotacaoVO().getUnidadeEnsinoResponsavelTramitacao().getCodigo()));
			setGravado(true);
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			e.printStackTrace();
			setGravado(false);
			getMapaCotacaoVO().getCotacaoVO().setSituacao(situacaoCotacaoAnterior);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void revisar() {
		String situacaoCotacaoAnterior = getMapaCotacaoVO().getCotacaoVO().getSituacao();
		try {
			getMapaCotacaoVO().getCotacaoVO().setSituacao("RC");
			getFacadeFactory().getMapaCotacaoFacade().revisarCotacao(getMapaCotacaoVO(), getUsuarioLogado());
			setMensagemID("msg_dados_gravados");
			setGravado(true);
		} catch (Exception e) {
			getMapaCotacaoVO().getCotacaoVO().setSituacao(situacaoCotacaoAnterior);
			setGravado(false);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void indeferir() {
		String situacaoCotacaoAnterior = getMapaCotacaoVO().getCotacaoVO().getSituacao();
		try {
			getMapaCotacaoVO().getCotacaoVO().setSituacao("IN");
			getFacadeFactory().getMapaCotacaoFacade().indeferirCotacao(getMapaCotacaoVO(), getUsuarioLogado(), getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(getMapaCotacaoVO().getCotacaoVO().getUnidadeEnsinoResponsavelTramitacao().getCodigo()));
			setMensagemID("msg_dados_gravados");
			setGravado(false);
		} catch (Exception e) {
			getMapaCotacaoVO().getCotacaoVO().setSituacao(situacaoCotacaoAnterior);
			setGravado(true);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void motivoRevisar() {
		try {
			limparMotivoRevisao();
			setRevisao(true);	
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		
	}

	public void motivoIndeferir() {
		try {
			limparMotivoRevisao();
			setRevisao(false);
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		
	}

	public void limparMotivoRevisao() {
		getMapaCotacaoVO().getCotacaoVO().setMotivoRevisao("");
	}

	public void editarCompra() {
		try {
			CompraVO obj = (CompraVO) context().getExternalContext().getRequestMap().get("compraItens");
			setCompraVO(obj);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}

	}

	public void selecionarTrocaResponsavel() {
		try {
			UsuarioVO usuarioVO = (UsuarioVO) context().getExternalContext().getRequestMap().get("responsavelItem");
			this.getMapaCotacaoVO().getCotacaoHistorico().setResponsavel(usuarioVO);
			setMensagemID("msg_dados_selecionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarTrocaResponsavel() {
		try {
			setListaUsuarioDepartamento(new ArrayList<>());
			getListaUsuarioDepartamento().addAll(getFacadeFactory().getUsuarioFacade().consultaUsuarioPorDepartamento(getMapaCotacaoVO().getCotacaoHistorico().getDepartamentoTramiteCotacaoCompra().getDepartamentoVO(), null, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void gravarTrocaResponsavel() {
		try {
			getFacadeFactory().getCotacaoHistoricoInterfaceFacade().atualizarResponsavelCotacaoHistorio(getMapaCotacaoVO().getCotacaoHistorico(), getUsuarioLogado());
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public String realizarNavegacaoParaTelaCotacao() {
		try {
			context().getExternalContext().getSessionMap().put("mapaCotacao", getMapaCotacaoVO());
			removerControleMemoriaFlash("CotacaoControle");
			removerControleMemoriaTela("CotacaoControle");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("cotacaoForm.xhtml");
	}

	public String getFecharModal() {
		if (getGravado()) {
			setGravado(false);
			return "RichFaces.$('panelCotacao').hide(); RichFaces.$('panelMotivo').hide();";
		}
		return "";
	}

	public void upLoadArquivoMaterialTramite(FileUploadEvent uploadEvent) {
		try {
			getFacadeFactory().getArquivoHelper().upLoad(uploadEvent, this.getMaterialTramiteCotacaoCompra().getArquivoVO(), getConfiguracaoGeralPadraoSistema(), PastaBaseArquivoEnum.TRAMITE_COTACAO_COMPRA_TMP, getUsuarioLogado());

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			uploadEvent = null;
		}
	}

	public void adicionarMaterialTramiteCotacaoCompra() throws Exception {
		try {
			this.getConfiguracaoGeralPadraoSistema();
			validarDadosInclusaoAnexo();
			this.adicionarArquivo(this.getMaterialTramiteCotacaoCompra().getArquivoVO());
			this.getMapaCotacaoVO().getCotacaoHistorico().adicionarMaterialTramiteCotacaoCompra(this.getMaterialTramiteCotacaoCompra());
			this.setMaterialTramiteCotacaoCompra(new MaterialTramiteCotacaoCompraVO());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	private void adicionarArquivo(ArquivoVO arquivo) {
		arquivo.setResponsavelUpload(getUsuarioLogadoClone());
		arquivo.setDataUpload(new Date());
		arquivo.setManterDisponibilizacao(true);
		arquivo.setDataDisponibilizacao(arquivo.getDataUpload());
		arquivo.setDataIndisponibilizacao(null);
		arquivo.setSituacao(SituacaoArquivo.ATIVO.getValor());
		arquivo.setOrigem(OrigemArquivo.TRAMITE_COTACAO_COMPRA.getValor());
		arquivo.setPastaBaseArquivoEnum(PastaBaseArquivoEnum.TRAMITE_COTACAO_COMPRA_TMP);

	}

	public void removerMaterialRequerimento() {
		try {
			MaterialTramiteCotacaoCompraVO obj = (MaterialTramiteCotacaoCompraVO) context().getExternalContext().getRequestMap().get("materialTramiteCotacaoCompraItem");
			this.getMapaCotacaoVO().getCotacaoHistorico().removerMaterialTramiteCotacaoCompra(obj);
		} catch (Exception e) {
			e.printStackTrace();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void realizarDownloadMaterialTramiteCotacaoCompra() {
		MaterialTramiteCotacaoCompraVO obj = (MaterialTramiteCotacaoCompraVO) context().getExternalContext().getRequestMap().get("materialTramiteCotacaoCompraItem");
		context().getExternalContext().getSessionMap().put("nomeArquivo", obj.getArquivoVO().getNome());
		context().getExternalContext().getSessionMap().put("pastaBaseArquivo", getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.TRAMITE_COTACAO_COMPRA.getValue());
	}

	public void selecionarCompraItem() {
		try {
			this.produtoAgrupador = (ProdutoAgrupador) context().getExternalContext().getRequestMap().get("compraItemItens");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void validarDadosInclusaoAnexo() throws Exception {
		try {
			if (getMaterialTramiteCotacaoCompra().getDescricao() == null || getMaterialTramiteCotacaoCompra().getDescricao().equals("")) {
				throw new Exception("O campo DESCRIÇÃO deve ser informado.");
			}
			if (getMaterialTramiteCotacaoCompra().getArquivoVO().getNome() == null || getMaterialTramiteCotacaoCompra().getArquivoVO().getNome().equals("")) {
				throw new Exception("O campo ARQUIVO deve ser informado.");
			}
		} catch (Exception e) {
			throw e;
		}

	}

	public String getVerificarUltrapassouTamanhoMaximoUpload() {
		try {
			return "Arquivo não Enviado. Tamanho Máximo Permitido " + getConfiguracaoGeralPadraoSistema().getTamanhoMaximoUpload() + "MB.";
		} catch (Exception e) {
			return "";
		}

	}

	public String getTamanhoMaximoUpload() {
		try {
			return "Tamanho Máximo Permitido: " + getConfiguracaoGeralPadraoSistema().getTamanhoMaximoUpload() + "MB.";
		} catch (Exception e) {
			return "Tamanho Máximo Não Configurado";
		}
	}

	public void imprimirPDF() {
		List<CotacaoRelVO> listaObjetos = null;
		try {
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
				registrarAtividadeUsuario(getUsuarioLogado(), "MapaCotacaoControle", "Finalizando Geração de Relatório de Cotação", "Finalizando Relatório");
				getSuperParametroRelVO().adicionarParametro("listaCotacaoHistorico", this.montarrListaCotacaoHistorico(this.mapaCotacaoVO.getCotacaoHistorico()));
				realizarImpressaoRelatorio();
				setMensagemID("msg_relatorio_ok");
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}
		} catch (Exception e) {
			e.printStackTrace();
			setMensagemDetalhada("msg_erro", e.getMessage());
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

	public List<SelectItem> getTipoConsultaComboTrocaResponsavel() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("nome", "Nome"));
		this.setCampoTrocaResponsavel("nome");
		return itens;
	}

	public MaterialTramiteCotacaoCompraVO getMaterialTramiteCotacaoCompra() {
		this.materialTramiteCotacaoCompra = Optional.ofNullable(this.materialTramiteCotacaoCompra).orElse(new MaterialTramiteCotacaoCompraVO());
		return materialTramiteCotacaoCompra;
	}

	public void setMaterialTramiteCotacaoCompra(MaterialTramiteCotacaoCompraVO materialTramiteCotacaoCompra) {
		this.materialTramiteCotacaoCompra = materialTramiteCotacaoCompra;
	}

	public boolean isRetorno() {
		return retorno;
	}

	public void setRetorno(boolean retorno) {
		this.retorno = retorno;
	}

	public MapaCotacaoVO getMapaCotacaoVO() {
		if (mapaCotacaoVO == null) {
			mapaCotacaoVO = new MapaCotacaoVO();
		}
		return mapaCotacaoVO;
	}

	public void atualizaCotacaoHistoricoSelecionada() {
		CotacaoHistoricoVO obj = (CotacaoHistoricoVO) context().getExternalContext().getRequestMap().get("tramiteHistorico");
		this.setCotacaoHistoricoSelecionada(obj);

	}

	public CotacaoHistoricoVO getCotacaoHistoricoSelecionada() {
		return cotacaoHistoricoSelecionada;
	}

	public void setCotacaoHistoricoSelecionada(CotacaoHistoricoVO cotacaoHistoricoSelecionada) {
		this.cotacaoHistoricoSelecionada = cotacaoHistoricoSelecionada;
	}

	public void setMapaCotacaoVO(MapaCotacaoVO mapaCotacaoVO) {
		this.mapaCotacaoVO = mapaCotacaoVO;
	}

	public boolean isRevisao() {
		return revisao;
	}

	public void setRevisao(boolean revisao) {
		this.revisao = revisao;
	}

	public List<DepartamentoVO> getDepartamentos() {
		return departamentos;
	}

	public List<UsuarioVO> getUsuarios() {
		return usuarios;
	}

	public boolean getGravado() {
		return gravado;
	}

	public void setGravado(boolean gravado) {
		this.gravado = gravado;
	}

	public CompraVO getCompraVO() {
		return compraVO;
	}

	public void setCompraVO(CompraVO compraVO) {
		this.compraVO = compraVO;
	}

	public UsuarioVO getUsuarioSelecionado() {
		return usuarioSelecionado;
	}

	public void setUsuarioSelecionado(UsuarioVO usuarioSelecionado) {
		this.usuarioSelecionado = usuarioSelecionado;
	}

	public boolean isTrocaResponsavel() {
		return trocaResponsavel;
	}

	public void setTrocaResponsavel(boolean trocaResponsavel) {
		this.trocaResponsavel = trocaResponsavel;
		this.setValorTrocaResponsavel("");
	}

	public List<UsuarioVO> getListaUsuarioDepartamento() {
		return listaUsuarioDepartamento;
	}

	public void setListaUsuarioDepartamento(List<UsuarioVO> listaUsuarioDepartamento) {
		this.listaUsuarioDepartamento = listaUsuarioDepartamento;
	}

	public boolean isMostrarBotaoAutorizar() {
		return (Objects.isNull(this.mapaCotacaoVO.getCotacaoHistorico()) 
				|| this.mapaCotacaoVO.getCotacaoHistorico().isUltimoPasso())
				&& !this.mapaCotacaoVO.getCotacaoVO().isAutorizada();
	}	

	public boolean isMostrarBotaoAvancar() {
		return Objects.nonNull(this.mapaCotacaoVO.getCotacaoHistorico()) 
				&& !this.mapaCotacaoVO.getCotacaoHistorico().isUltimoPasso() 
				&& !this.mapaCotacaoVO.getCotacaoVO().isAutorizada();
	}

	public boolean isMostrarBotaoRetornar() {
		return Objects.nonNull(this.mapaCotacaoVO.getCotacaoHistorico()) 
				&& !this.mapaCotacaoVO.getCotacaoHistorico().isPrimeiroPasso()
				&& !this.mapaCotacaoVO.getCotacaoVO().isAutorizada();
	}

	public List<CompraAgrupador> getAgrupadoresCompras() {
		this.agrupadoresCompras = Optional.ofNullable(this.agrupadoresCompras).orElse(new ArrayList<>());
		return agrupadoresCompras;
	}

	public String getCampoTrocaResponsavel() {
		return campoTrocaResponsavel;
	}

	public void setCampoTrocaResponsavel(String campoTrocaResponsavel) {
		this.campoTrocaResponsavel = campoTrocaResponsavel;
	}

	public String getValorTrocaResponsavel() {
		this.valorTrocaResponsavel = Optional.ofNullable(this.valorTrocaResponsavel).orElse("");
		return valorTrocaResponsavel;
	}

	public void setValorTrocaResponsavel(String valorTrocaResponsavel) {
		this.valorTrocaResponsavel = valorTrocaResponsavel;
	}

	public ProdutoAgrupador getProdutoAgrupador() {
		return produtoAgrupador;
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
		if(getPermiteConsultarCotacoesOutrosResponsaveisTodasUnidadeEnsino() || getUnidadeEnsinoLogado().getCodigo().intValue() == 0){
			List<UnidadeEnsinoVO> resultadoConsulta = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome("", 0, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			getListaSelectItemUnidadeEnsino().add(new SelectItem(0, ""));
			setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
		} else if (getUnidadeEnsinoLogado().getCodigo().intValue() != 0) {
			UnidadeEnsinoVO obj = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCodigo(getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			getMapaCotacaoVO().getCotacaoVO().setUnidadeEnsinoResponsavelTramitacao(obj);
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
				getListaSelectItemDepartamento().add(new SelectItem(0, ""));
				listaDepartamento.addAll(getFacadeFactory().getDepartamentoFacade().consultarDepartamentoPorDepartamentoTramiteExistente(false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			} else {
				listaDepartamento.addAll(getFacadeFactory().getDepartamentoFacade().consultarPorCodigoPessoaFuncionario(getUsuarioLogado().getPessoa().getCodigo(), null, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			}
			listaDepartamento.stream().forEach(p ->getListaSelectItemDepartamento().add(new SelectItem(p.getCodigo(), p.getNome())));
		
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
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

	public List<SelectItem> getListaSelectItemDepartamento() {
		listaSelectItemDepartamento = Optional.ofNullable(listaSelectItemDepartamento).orElse(new ArrayList<>());
		return listaSelectItemDepartamento;
	}

	public void setListaSelectItemDepartamento(List<SelectItem> listaSelectItemDepartamento) {
		this.listaSelectItemDepartamento = listaSelectItemDepartamento;
	}

	public List<SelectItem> getTipoConsultaComboSituacaoCotacao() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("MC", "Todas"));
		itens.add(new SelectItem("AA", "Aguardando Autorização"));
		itens.add(new SelectItem("AU", "Autorizado"));
		itens.add(new SelectItem("IN", "Indeferido"));
		return itens;
	}

	public List<SelectItem> getTipoConsultaComboEnumSituacaoTramitacao() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem(EnumSituacaoTramitacao.TODOS, EnumSituacaoTramitacao.TODOS.getTitulo()));
		itens.add(new SelectItem(EnumSituacaoTramitacao.NO_PRAZO, EnumSituacaoTramitacao.NO_PRAZO.getTitulo()));
		itens.add(new SelectItem(EnumSituacaoTramitacao.ATRAZADO, EnumSituacaoTramitacao.ATRAZADO.getTitulo()));
		return itens;
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

	public EnumSituacaoTramitacao getEnumSituacaoTramitacaoFiltro() {
		enumSituacaoTramitacaoFiltro = Optional.ofNullable(enumSituacaoTramitacaoFiltro).orElse(EnumSituacaoTramitacao.TODOS);
		return enumSituacaoTramitacaoFiltro;
	}

	public void setEnumSituacaoTramitacaoFiltro(EnumSituacaoTramitacao enumSituacaoTramitacaoFiltro) {
		this.enumSituacaoTramitacaoFiltro = enumSituacaoTramitacaoFiltro;
	}

	public boolean isApresentarReponsavelTramitacao() {
		return getPermiteConsultarCotacoesOutrosResponsaveisMesmaUnidadeEnsino() || getPermiteConsultarCotacoesOutrosResponsaveisMesmoDepartamento() || getPermiteConsultarCotacoesOutrosResponsaveisTodasUnidadeEnsino();
	}

	public boolean getPermiteConsultarCotacoesOutrosResponsaveisTodasUnidadeEnsino() {
		try {
			MapaCotacao.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoComprasEnum.PERMITE_CONSULTAR_MAPA_COTACOES_OUTROS_RESPONSAVEIS_TODAS_UNIDADE_ENSINO, getUsuarioLogado());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean getPermiteConsultarCotacoesOutrosResponsaveisMesmaUnidadeEnsino() {
		try {
			MapaCotacao.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoComprasEnum.PERMITE_CONSULTAR_MAPA_COTACOES_OUTROS_RESPONSAVEIS_MESMA_UNIDADE_ENSINO, getUsuarioLogado());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean getPermiteConsultarCotacoesOutrosResponsaveisMesmoDepartamento() {
		try {
			MapaCotacao.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoComprasEnum.PERMITE_CONSULTAR_MAPA_COTACOES_OUTROS_RESPONSAVEIS_MESMO_DEPARTAMENTO, getUsuarioLogado());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean getPermiteRetornarTramite() {
		try {
			MapaCotacao.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoComprasEnum.PERMITE_RETORNAR_TRAMITE, getUsuarioLogado());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean getPermiteAlterarResponsavel() {
		try {
			MapaCotacao.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoComprasEnum.PERMITE_ALTERAR_RESPONSAVEL, getUsuarioLogado());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}