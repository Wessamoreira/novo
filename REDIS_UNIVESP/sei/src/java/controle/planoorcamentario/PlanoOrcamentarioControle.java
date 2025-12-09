package controle.planoorcamentario;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas fluxoCaixaForm.jsp fluxoCaixaCons.jsp) com as funcionalidades da classe <code>FluxoCaixa</code>. Implemtação da
 * camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see FluxoCaixa
 * @see FluxoCaixaVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.TipoOrigemComunicacaoInternaEnum;
import negocio.comuns.compras.RequisicaoItemVO;
import negocio.comuns.planoorcamentario.DetalhamentoPeriodoOrcamentoVO;
import negocio.comuns.planoorcamentario.ItemSolicitacaoOrcamentoPlanoOrcamentarioVO;
import negocio.comuns.planoorcamentario.PlanoOrcamentarioVO;
import negocio.comuns.planoorcamentario.SolicitacaoOrcamentoPlanoOrcamentarioVO;
import negocio.comuns.planoorcamentario.UnidadesPlanoOrcamentarioVO;
import negocio.comuns.planoorcamentario.enumeradores.SituacaoPlanoOrcamentarioEnum;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.planoorcamentario.ItemSolicitacaoOrcamentoPlanoOrcamentario;

@Controller("PlanoOrcamentarioControle")
@Scope("viewScope")
@Lazy
public class PlanoOrcamentarioControle extends SuperControle implements Serializable {

	private static final long serialVersionUID = 6077927479074864250L;

	private PlanoOrcamentarioVO planoOrcamentarioVO;
	private SolicitacaoOrcamentoPlanoOrcamentarioVO solicitacaoOrcamentoPlanoOrcamentarioVO;
	private ItemSolicitacaoOrcamentoPlanoOrcamentarioVO itemSolicitacaoOrcamentoPlanoOrcamentarioVO;

	private String mensagemPadraoNotificacao;
	private Boolean enviarComunicadoPorEmail;
	private Boolean fecharModalDistribuiOrcamento;
	private Boolean permiteReativarPlanoOrcamentario;
	private Boolean fecharModalOrcamentoTotalPeridodo;
	private List<DetalhamentoPeriodoOrcamentoVO> listaDetalhamentoOrcamentarioPeriodoDepartamento;

	private List<RequisicaoItemVO> listaItemRequisicao = new ArrayList<>();
	private List<ComunicacaoInternaVO> listaComunicacaoInterna = new ArrayList<>();

	private List<SolicitacaoOrcamentoPlanoOrcamentarioVO> solicitacaoOrcamentoPlanoOrcamentarioVOsAprovados;
	private List<SolicitacaoOrcamentoPlanoOrcamentarioVO> solicitacaoOrcamentoPlanoOrcamentarioVOsEmRevisao;
	private List<SolicitacaoOrcamentoPlanoOrcamentarioVO> solicitacaoOrcamentoPlanoOrcamentarioVOsEmConstrucao;
	private List<SolicitacaoOrcamentoPlanoOrcamentarioVO> solicitacaoOrcamentoPlanoOrcamentarioVOsAguardandoAprovacao;

	public PlanoOrcamentarioControle() {
		setControleConsulta(new ControleConsulta());
		getControleConsulta().setCampoConsulta("A");
		setMensagemID("msg_entre_prmconsulta");
		verificarPermissaoReativarPlanoOrcamentario();
		consultarUnidadeEnsino();
		adicionarListaUnidades();
		if (getSessionAplicacao().getAttribute("planoOrcamentarioItens") != null) {
			context().getExternalContext().getRequestMap().put("planoOrcamentarioItens", getSessionAplicacao().getAttribute("planoOrcamentarioItens"));
			getSessionAplicacao().removeAttribute("planoOrcamentarioItens");
			editar();
		}
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe
	 * <code>FluxoCaixa</code> para edição pelo usuário da aplicação.
	 */
	public String novo() {
		removerObjetoMemoria(this);
		setPlanoOrcamentarioVO(new PlanoOrcamentarioVO());
		getPlanoOrcamentarioVO().setResponsavel(getUsuarioLogado());
		consultarUnidadeEnsino();
		adicionarListaUnidades();
		setMarcarTodasUnidadeEnsino(true);
		marcarTodasUnidadesEnsinoAction();
		verificarPermissaoReativarPlanoOrcamentario();
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("planoOrcamentarioForm.xhtml");
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe
	 * <code>FluxoCaixa</code> para alteração. O objeto desta classe é
	 * disponibilizado na session da página (request) para que o JSP correspondente
	 * possa disponibilizá-lo para edição.
	 *
	 * @throws Exception
	 */
	public String editar() {
		try {
			limparSolicitacoesOrcamentos();
			PlanoOrcamentarioVO obj = (PlanoOrcamentarioVO) context().getExternalContext().getRequestMap()
					.get("planoOrcamentarioItens");
			setPlanoOrcamentarioVO(obj);
			getFacadeFactory().getPlanoOrcamentarioFacade().carregarDados(getPlanoOrcamentarioVO(), getUsuarioLogado());

			setUnidadeEnsinosApresentar("");
			for (UnidadesPlanoOrcamentarioVO unidade : getPlanoOrcamentarioVO().getListaUnidades()) {
				UnidadeEnsinoVO unidadeEnsinoVO = getFacadeFactory().getUnidadeEnsinoFacade()
						.consultaRapidaPorCodigo(unidade.getUnidadeEnsino().getCodigo(), false, getUsuarioLogado());
				setUnidadeEnsinosApresentar(getUnidadeEnsinosApresentar() + unidadeEnsinoVO.getRazaoSocial() + ";");
			}

			if (!Uteis.isAtributoPreenchido(getPlanoOrcamentarioVO().getResponsavel())) {
				getPlanoOrcamentarioVO().setResponsavel(getUsuarioLogado());
			}

			montarListaSolicitacoesOrcamentos(obj);
			ordenarSolicitacoes();
			obj.setNovoObj(Boolean.FALSE);
			setMensagemID("msg_dados_editar");
			verificarPermissaoReativarPlanoOrcamentario();
			return Uteis.getCaminhoRedirecionamentoNavegacao("planoOrcamentarioForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return Uteis.getCaminhoRedirecionamentoNavegacao("planoOrcamentarioCons.xhtml");
		}
	}

	private void limparSolicitacoesOrcamentos() {
		setSolicitacaoOrcamentoPlanoOrcamentarioVOsEmRevisao(new ArrayList<>(0));
		setSolicitacaoOrcamentoPlanoOrcamentarioVOsAguardandoAprovacao(new ArrayList<>(0));
		setSolicitacaoOrcamentoPlanoOrcamentarioVOsEmRevisao(new ArrayList<>(0));
		setSolicitacaoOrcamentoPlanoOrcamentarioVOAprovados(new ArrayList<>(0));
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto da
	 * classe <code>FluxoCaixa</code>. Caso o objeto seja novo (ainda não gravado no
	 * BD) é acionado a operação <code>incluir()</code>. Caso contrário é acionado o
	 * <code>alterar()</code>. Se houver alguma inconsistência o objeto não é
	 * gravado, sendo re-apresentado para o usuário juntamente com uma mensagem de
	 * erro.
	 */
	public void gravar() {
		try {
			validarDadosUnidadeEnsino();

			if (getPlanoOrcamentarioVO().isNovoObj().booleanValue()) {
				getFacadeFactory().getPlanoOrcamentarioFacade().incluir(getPlanoOrcamentarioVO(), getUsuarioLogado(),
						permitirRealizarManejamentoSaldoAprovado());
			} else {
				getFacadeFactory().getPlanoOrcamentarioFacade().alterar(getPlanoOrcamentarioVO(), getUsuarioLogado(),
						permitirRealizarManejamentoSaldoAprovado());
			}
			setMensagemID(MSG_TELA.msg_dados_gravados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void selecionarItemSolicitacaoOrcamentoPlanoOrcamentario() {
		try {
			setItemSolicitacaoOrcamentoPlanoOrcamentarioVO((ItemSolicitacaoOrcamentoPlanoOrcamentarioVO) context()
					.getExternalContext().getRequestMap().get("detalhamentoItens"));

			getItemSolicitacaoOrcamentoPlanoOrcamentarioVO().setDetalhamentoPeriodoOrcamentoVOs(getFacadeFactory()
					.getDetalhamentoPeriodoOrcamentoFacade().consultarDetalhamentoPorItemSolicitacaoPlanoOrcamentario(
							getItemSolicitacaoOrcamentoPlanoOrcamentarioVO().getCodigo(),
							Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, getUsuarioLogado()));
			setMensagemID("msg_entre_dados");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void gravarItemSolicitacaoOrcamentoPlanoOrcamentario() {
		try {
			getFacadeFactory().getItemSolicitacaoOrcamentoPlanoOrcamentarioFacade().gravarItemSolicitacao(
					getItemSolicitacaoOrcamentoPlanoOrcamentarioVO(), getSolicitacaoOrcamentoPlanoOrcamentarioVO(),
					getUsuarioLogado(), false);

			setMensagemID("msg_SolicitacaoPlanoOrcamentario_remanejarValorAprovado");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	private void validarDadosUnidadeEnsino() {
		getPlanoOrcamentarioVO().setListaUnidades(new ArrayList<>());
		if (Uteis.isAtributoPreenchido(getUnidadeEnsinoVOs())) {
			UnidadesPlanoOrcamentarioVO obj = new UnidadesPlanoOrcamentarioVO();
			for (UnidadeEnsinoVO unidadeEnsinoVO : getUnidadeEnsinoVOs()) {
				boolean naoExisteUnidadeEnsino = !getPlanoOrcamentarioVO().getListaUnidades().stream()
						.anyMatch(p -> p.getUnidadeEnsino().getCodigo().equals(unidadeEnsinoVO.getCodigo()));
				if (naoExisteUnidadeEnsino) {
					obj.setUnidadeEnsino(unidadeEnsinoVO);
					getPlanoOrcamentarioVO().getListaUnidades().add(obj);
				}
				obj = new UnidadesPlanoOrcamentarioVO();
			}
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP
	 * FluxoCaixaCons.jsp. Define o tipo de consulta a ser executada, por meio de
	 * ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
	 * resultado, disponibiliza um List com os objetos selecionados na sessao da
	 * pagina.
	 */
	public String consultar() {
		try {
			super.consultar();
			List<PlanoOrcamentarioVO> objs = new ArrayList<>(0);
			if (getControleConsulta().getCampoConsulta().equals("codigo")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getPlanoOrcamentarioFacade().consultaRapidaPorCodigo(new Integer(valorInt),
						true, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nome")) {
				objs = getFacadeFactory().getPlanoOrcamentarioFacade()
						.consultaRapidaPorNome(getControleConsulta().getValorConsulta(), false, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("situacao")) {
				objs = getFacadeFactory().getPlanoOrcamentarioFacade()
						.consultaRapidaPorSituacao(getControleConsulta().getValorConsulta(), true, getUsuarioLogado());
			}
			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("planoOrcamentarioCons.xhtml");
		} catch (Exception e) {
			setListaConsulta(new ArrayList<>(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("planoOrcamentarioCons.xhtml");
		}
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe
	 * <code>FluxoCaixaVO</code> Após a exclusão ela automaticamente aciona a rotina
	 * para uma nova inclusão.
	 */
	public void excluir() {
		try {
			getFacadeFactory().getRequisicaoItemFacade()
					.validarExisteRequisicaoItemPorPlanoOrcamentario(getPlanoOrcamentarioVO());
			getFacadeFactory().getPlanoOrcamentarioFacade().excluir(getPlanoOrcamentarioVO());
			setPlanoOrcamentarioVO(new PlanoOrcamentarioVO());
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarUnidadeEnsino() {
		try {
			setUnidadeEnsinoVOs(getFacadeFactory().getUnidadeEnsinoFacade().consultarUnidadeEnsinoFaltandoLista(
					getPlanoOrcamentarioVO().obterListaUnidadeEnsino(), Uteis.NIVELMONTARDADOS_COMBOBOX,
					getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			getPlanoOrcamentarioVO().setListaUnidades(new ArrayList<>(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void adicionarListaUnidades() {
		getPlanoOrcamentarioVO().adicionarListaUnidades(getUnidadeEnsinoVOs());
	}

	public void selecionarSolicitacaoOrcamentoPlanoOrcamentario() {
		try {
			SolicitacaoOrcamentoPlanoOrcamentarioVO obj = (SolicitacaoOrcamentoPlanoOrcamentarioVO) context()
					.getExternalContext().getRequestMap().get("solicitacaoOrcamentoPlanoOrcamentarioItens");
			obj.setItemSolicitacaoOrcamentoPlanoOrcamentarioVOs(ItemSolicitacaoOrcamentoPlanoOrcamentario
					.consultarItemSolicitacaoOrcamentoPlanoOrcamentarios(obj.getCodigo(), false, getUsuarioLogado()));
			if (obj.getDetalhamentoPeriodoOrcamentoVOs().isEmpty()) {
				getFacadeFactory().getSolicitacaoOrcamentoPlanoOrcamentarioFacade()
						.realizarCriacaoDetalhamentoPeriodoGeral(obj);
			}
			setSolicitacaoOrcamentoPlanoOrcamentarioVO(obj);
			setFecharModalDistribuiOrcamento(Boolean.FALSE);
			setFecharModalOrcamentoTotalPeridodo(Boolean.TRUE);
		} catch (Exception e) {
			setFecharModalDistribuiOrcamento(Boolean.TRUE);
			setFecharModalOrcamentoTotalPeridodo(Boolean.TRUE);
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void distribuirOrcamentoPeriodo() {
		try {
			getFacadeFactory().getDetalhamentoPeriodoOrcamentoFacade()
					.realizarGeracaoDetalhamentoPorPeriodoPorPlanoOrcamentario(getPlanoOrcamentarioVO());
			setFecharModalOrcamentoTotalPeridodo(Boolean.FALSE);
			setFecharModalDistribuiOrcamento(Boolean.TRUE);
		} catch (Exception e) {
			setFecharModalOrcamentoTotalPeridodo(Boolean.TRUE);
			setFecharModalDistribuiOrcamento(Boolean.TRUE);
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarRequisicaoPorPlanoOrcamentario() {
		try {
			getFacadeFactory().getRequisicaoFacade().consultarPorPlanoOrcamentario(getPlanoOrcamentarioVO(),
					Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarRequisicaoItemConsumidoPlanoOrcamentario() {
		try {
			SolicitacaoOrcamentoPlanoOrcamentarioVO obj = (SolicitacaoOrcamentoPlanoOrcamentarioVO) context()
					.getExternalContext().getRequestMap().get("solicitacaoOrcamentoPlanoOrcamentarioItens");

			getPlanoOrcamentarioVO().setRequisicaoItemVOs(
					getFacadeFactory().getRequisicaoItemFacade().consultarRequisicaoItemConsumidoPlanoOrcamentario(
							getPlanoOrcamentarioVO().getCodigo(), obj.getCodigo(), null, null, null, null, null));

			getPlanoOrcamentarioVO().getRequisicaoItemVOs().stream().forEach(p -> {
				p.getRequisicaoVO().setRequisicaoItemVOs(getPlanoOrcamentarioVO().getRequisicaoItemVOs());
			});
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void removerTodasUnidadeEnsino() {
		getPlanoOrcamentarioVO().getListaUnidades();
		getPlanoOrcamentarioVO().setListaUnidades(null);
	}

	public void consultarDadosOrcamentarios() {
		try {
			getFacadeFactory().getPlanoOrcamentarioFacade().consultarDadosOrcamentarios(getPlanoOrcamentarioVO(),
					getPlanoOrcamentarioVO().getDataInicio(), getPlanoOrcamentarioVO().getDataFinal());
		} catch (Exception ex) {
			ex.printStackTrace();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), ex.getMessage());
		}
	}

	public void notificarResponsavelDepartamento() {
		try {
			selecionarSolicitacaoOrcamentoPlanoOrcamentario();
			getSolicitacaoOrcamentoPlanoOrcamentarioVO()
					.setDepartamento(getFacadeFactory().getDepartamentoFacade().consultarPorChavePrimaria(
							getSolicitacaoOrcamentoPlanoOrcamentarioVO().getDepartamento().getCodigo(), false,
							Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void enviarEmailResponsavelDepartamento() {
		try {
			getFacadeFactory().getSolicitacaoOrcamentoPlanoOrcamentarioFacade().enviarEmailResponsavelDepartamento(
					getSolicitacaoOrcamentoPlanoOrcamentarioVO().getDepartamento(),
					getSolicitacaoOrcamentoPlanoOrcamentarioVO().getUnidadeEnsino(), getEnviarComunicadoPorEmail(),
					getMensagemPadraoNotificacao(), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema(),
					getSolicitacaoOrcamentoPlanoOrcamentarioVO().getCodigo(), getUsuarioLogado().getPessoa(),
					getSolicitacaoOrcamentoPlanoOrcamentarioVO().getDepartamento().getResponsavel());
			setMensagemPadraoNotificacao(null);
			setMensagemID("msg_msg_enviados");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void enviarEmailResposta() {
		try {
			getFacadeFactory().getSolicitacaoOrcamentoPlanoOrcamentarioFacade().enviarEmailResponsavelDepartamento(
					getSolicitacaoOrcamentoPlanoOrcamentarioVO().getDepartamento(),
					getSolicitacaoOrcamentoPlanoOrcamentarioVO().getUnidadeEnsino(), getEnviarComunicadoPorEmail(),
					getMensagemPadraoNotificacao(), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema(),
					getComunicacaoInternaVO().getCodigoTipoOrigemComunicacaoInterna(), getUsuarioLogado().getPessoa(),
					getComunicacaoInternaVO().getResponsavel());
			setMensagemPadraoNotificacao(null);
			setMensagemID("msg_msg_enviados");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void responderComunicacaoInterna() {
		try {
			ComunicacaoInternaVO obj = (ComunicacaoInternaVO) context().getExternalContext().getRequestMap()
					.get("comunicadoInterno");

			setComunicacaoInternaVO(getFacadeFactory().getComunicacaoInternaFacade().consultarPorChavePrimaria(
					obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarComunicadosInterno() {
		try {
			SolicitacaoOrcamentoPlanoOrcamentarioVO obj = (SolicitacaoOrcamentoPlanoOrcamentarioVO) context()
					.getExternalContext().getRequestMap().get("solicitacaoOrcamentoPlanoOrcamentarioItens");

			setListaComunicacaoInterna(
					getFacadeFactory().getComunicacaoInternaFacade().consultarPorTipoOrigemECodigoTipoOrigemEPessoa(
							TipoOrigemComunicacaoInternaEnum.SOLICITACAO_ORCAMENTO_PLANO_ORCAMENTARIO, obj.getCodigo(),
							getUsuarioLogado().getPessoa(), getUsuarioLogado()));
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void clonar() {
		try {
			setPlanoOrcamentarioVO(getFacadeFactory().getPlanoOrcamentarioFacade().clonar(getPlanoOrcamentarioVO()));
			setMensagemID("msg_dados_clonados");
		} catch (CloneNotSupportedException e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public String getFecharModalDistribuicaoOrcamento() {
		if (getFecharModalDistribuiOrcamento()) {
			return "RichFaces.$('panelDetalhamentoDepartamento').hide();";
		}
		return "RichFaces.$('panelDetalhamentoDepartamento').show();";
	}

	public String getFecharModalOrcamentoTotalPeriodo() {
		if (getFecharModalOrcamentoTotalPeridodo()) {
			return "RichFaces.$('panelDistribuirOrcamentoPeriodo').hide();";
		}
		return "RichFaces.$('panelDistribuirOrcamentoPeriodo').show();";
	}

	private void montarListaSolicitacoesOrcamentos(PlanoOrcamentarioVO obj) {
		for (SolicitacaoOrcamentoPlanoOrcamentarioVO solicitacaoOrcamentoPlanoOrcamentario : obj
				.getSolicitacaoOrcamentoPlanoOrcamentarioVOs()) {
			switch (solicitacaoOrcamentoPlanoOrcamentario.getSituacao()) {
			case EM_CONSTRUCAO:
				getSolicitacaoOrcamentoPlanoOrcamentarioVOsEmConstrucao().add(solicitacaoOrcamentoPlanoOrcamentario);
				break;
			case AGUARDANDO_APROVACAO:
				getSolicitacaoOrcamentoPlanoOrcamentarioVOsAguardandoAprovacao()
						.add(solicitacaoOrcamentoPlanoOrcamentario);
				break;
			case APROVADO:
				getSolicitacaoOrcamentoPlanoOrcamentarioVOsAprovados().add(solicitacaoOrcamentoPlanoOrcamentario);
				break;
			case REVISAO:
				getSolicitacaoOrcamentoPlanoOrcamentarioVOsEmRevisao().add(solicitacaoOrcamentoPlanoOrcamentario);
				break;
			default:
				break;
			}
		}
	}

	public void irPaginaInicial() {
		this.consultar();
	}

	public void irPaginaAnterior() {
		controleConsulta.setPaginaAtual(controleConsulta.getPaginaAtual() - 1);
		this.consultar();
	}

	public void irPaginaPosterior() {
		controleConsulta.setPaginaAtual(controleConsulta.getPaginaAtual() + 1);
		this.consultar();
	}

	public void irPaginaFinal() throws Exception {
		controleConsulta.setPaginaAtual(controleConsulta.getNrTotalPaginas());
		this.consultar();
	}

	public List<SelectItem> getTipoConsultaComboSituacao() {
		List<SelectItem> selectItems = new ArrayList<>();
		selectItems.add(new SelectItem("EM", "Em Construção"));
		selectItems.add(new SelectItem("AT", "Ativo"));
		selectItems.add(new SelectItem("FI", "Finalizado"));
		selectItems.add(new SelectItem("AP", "Aprovado"));
		selectItems.add(new SelectItem("RE", "Revisão"));
		return selectItems;
	}

	public Boolean getApresentarSituacao() {
		return getControleConsulta().getCampoConsulta().equals("situacao");
	}

	public Double getTotalValorUnitarioItemRequisicao() {
		try {
			return getListaItemRequisicao().stream().mapToDouble(RequisicaoItemVO::getValorUnitario).sum();
		} catch (Exception e) {

			return 0.0;
		}
	}

	public Double getTotalItemRequisicao() {
		try {
			return getListaItemRequisicao().stream().mapToDouble(RequisicaoItemVO::getValorTotal).sum();
		} catch (Exception e) {

			return 0.0;
		}
	}

	// Totalizadores Em Construção
	public Double getTotalRequidoGestor() {
		try {
			return getSolicitacaoOrcamentoPlanoOrcamentarioVOsEmConstrucao().stream()
					.mapToDouble(SolicitacaoOrcamentoPlanoOrcamentarioVO::getValorTotalSolicitado).sum();
		} catch (Exception e) {
			return 0.0;
		}
	}

	public Double getTotalValorConsumido() {
		try {
			return getSolicitacaoOrcamentoPlanoOrcamentarioVOsEmConstrucao().stream()
					.mapToDouble(SolicitacaoOrcamentoPlanoOrcamentarioVO::getValorConsumido).sum();
		} catch (Exception e) {
			return 0.0;
		}
	}

	// Totalizadores Aprovado
	public Double getTotalRequidoGestorAprovado() {
		try {
			return getSolicitacaoOrcamentoPlanoOrcamentarioVOsAprovados().stream()
					.mapToDouble(SolicitacaoOrcamentoPlanoOrcamentarioVO::getValorTotalSolicitado).sum();
		} catch (Exception e) {
			return 0.0;
		}
	}

	public Double getTotalOrcamentoTotalDepartamentoAprovado() {
		try {
			return getSolicitacaoOrcamentoPlanoOrcamentarioVOsAprovados().stream()
					.mapToDouble(SolicitacaoOrcamentoPlanoOrcamentarioVO::getValorTotalAprovado).sum();
		} catch (Exception e) {
			return 0.0;
		}
	}

	public Double getTotalValorConsumidoAprovado() {
		try {
			return getSolicitacaoOrcamentoPlanoOrcamentarioVOsAprovados().stream()
					.mapToDouble(SolicitacaoOrcamentoPlanoOrcamentarioVO::getValorConsumido).sum();
		} catch (Exception e) {

			return 0.0;
		}
	}

	// Totalizadores Aguardando Aprovação
	public Double getTotalRequidoGestorAguardandoAprovacao() {
		try {
			return getSolicitacaoOrcamentoPlanoOrcamentarioVOsAguardandoAprovacao().stream()
					.mapToDouble(SolicitacaoOrcamentoPlanoOrcamentarioVO::getValorTotalSolicitado).sum();
		} catch (Exception e) {
			return 0.0;
		}
	}

	public Double getTotalOrcamentoTotalDepartamentoAguardandoAprovacao() {
		try {
			return getSolicitacaoOrcamentoPlanoOrcamentarioVOsAguardandoAprovacao().stream()
					.mapToDouble(SolicitacaoOrcamentoPlanoOrcamentarioVO::getValorTotalAprovado).sum();
		} catch (Exception e) {
			return 0.0;
		}
	}

	public Double getTotalValorConsumidoAguardandoAprovacao() {
		try {
			return getSolicitacaoOrcamentoPlanoOrcamentarioVOsAguardandoAprovacao().stream()
					.mapToDouble(SolicitacaoOrcamentoPlanoOrcamentarioVO::getValorConsumido).sum();
		} catch (Exception e) {
			return 0.0;
		}
	}

	// Totalizadores Em Revisão
	public Double getTotalRequidoGestorEmRevisao() {
		try {
			return getSolicitacaoOrcamentoPlanoOrcamentarioVOsEmRevisao().stream()
					.mapToDouble(SolicitacaoOrcamentoPlanoOrcamentarioVO::getValorTotalSolicitado).sum();
		} catch (Exception e) {
			return 0.0;
		}
	}

	public Double getTotalValorConsumidoEmRevisao() {
		try {
			return getSolicitacaoOrcamentoPlanoOrcamentarioVOsEmRevisao().stream()
					.mapToDouble(SolicitacaoOrcamentoPlanoOrcamentarioVO::getValorConsumido).sum();
		} catch (Exception e) {
			return 0.0;
		}
	}

	public double getTotalValorRequerido() {
		try {
			return getSolicitacaoOrcamentoPlanoOrcamentarioVO().getDetalhamentoPeriodoOrcamentoVOs().stream()
					.mapToDouble(DetalhamentoPeriodoOrcamentoVO::getOrcamentoRequeridoGestor).sum();
		} catch (Exception e) {
			return 0.0;
		}
	}

	public double getTotalValorAprovado() {
		try {
			return getSolicitacaoOrcamentoPlanoOrcamentarioVO().getDetalhamentoPeriodoOrcamentoVOs().stream()
					.mapToDouble(DetalhamentoPeriodoOrcamentoVO::getOrcamentoTotal).sum();
		} catch (Exception e) {
			return 0.0;
		}
	}

	public double getTotalQuantidadeAutorizada() {
		try {
			return getPlanoOrcamentarioVO().getRequisicaoItemVOs().stream()
					.mapToDouble(RequisicaoItemVO::getQuantidadeAutorizada).sum();
		} catch (Exception e) {
			return 0.0;
		}
	}

	public double getTotalRequisicao() {
		try {
			return getPlanoOrcamentarioVO().getRequisicaoItemVOs().stream().mapToDouble(RequisicaoItemVO::getValorTotal)
					.sum();
		} catch (Exception e) {
			return 0.0;
		}
	}

	// Distribuicao periodo
	public double getTotalDistribuicaoPeriodoRequeridoGestor() {
		try {
			return getSolicitacaoOrcamentoPlanoOrcamentarioVO().getDetalhamentoPeriodoOrcamentoVOs().stream()
					.mapToDouble(DetalhamentoPeriodoOrcamentoVO::getOrcamentoRequeridoGestor).sum();
		} catch (Exception e) {
			return 0.0;
		}
	}

	public double getTotalDistribuicaoPeriodoOrcamentoTotal() {
		try {
			return getSolicitacaoOrcamentoPlanoOrcamentarioVO().getDetalhamentoPeriodoOrcamentoVOs().stream()
					.mapToDouble(DetalhamentoPeriodoOrcamentoVO::getOrcamentoTotal).sum();
		} catch (Exception e) {
			return 0.0;
		}
	}

	public double getTotalDistribuicaoPeriodoValorConsumido() {
		try {
			return getSolicitacaoOrcamentoPlanoOrcamentarioVO().getDetalhamentoPeriodoOrcamentoVOs().stream()
					.mapToDouble(DetalhamentoPeriodoOrcamentoVO::getValorConsumido).sum();
		} catch (Exception e) {
			return 0.0;
		}
	}

	public double getTotalDistribuicaoPeriodoValorDisponivel() {
		try {
			return getSolicitacaoOrcamentoPlanoOrcamentarioVO().getDetalhamentoPeriodoOrcamentoVOs().stream()
					.mapToDouble(DetalhamentoPeriodoOrcamentoVO::getValorDisponivel).sum();
		} catch (Exception e) {
			return 0.0;
		}
	}

	public double getTotalVisualizacaoDistribuicaoPeriodoValorRequirido() {
		return getItemSolicitacaoOrcamentoPlanoOrcamentarioVO().getDetalhamentoPeriodoOrcamentoVOs().stream()
				.mapToDouble(DetalhamentoPeriodoOrcamentoVO::getOrcamentoRequeridoGestor).sum();
	}

	public double getTotalVisualizacaoDistribuicaoPeriodoValorAprovado() {
		return getItemSolicitacaoOrcamentoPlanoOrcamentarioVO().getDetalhamentoPeriodoOrcamentoVOs().stream()
				.mapToDouble(DetalhamentoPeriodoOrcamentoVO::getOrcamentoTotal).sum();
	}

	public List<SelectItem> getTipoComboSituacao() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("EM", "Em Construção"));
		itens.add(new SelectItem("AT", "Ativo"));
		itens.add(new SelectItem("FI", "Finalizado"));
		return itens;
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("situacao", "Situação"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes de
	 * uma consulta.
	 */
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setListaConsulta(new ArrayList<>(0));
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("planoOrcamentarioCons.xhtml");
	}

	@Override
	protected void limparRecursosMemoria() {
		super.limparRecursosMemoria();
		setPlanoOrcamentarioVO(null);
	}

	/**
	 * @return the planoOrcamentarioVO
	 */
	public PlanoOrcamentarioVO getPlanoOrcamentarioVO() {
		if (planoOrcamentarioVO == null) {
			planoOrcamentarioVO = new PlanoOrcamentarioVO();
		}
		return planoOrcamentarioVO;
	}

	/**
	 * @param planoOrcamentarioVO the planoOrcamentarioVO to set
	 */
	public void setPlanoOrcamentarioVO(PlanoOrcamentarioVO planoOrcamentarioVO) {
		this.planoOrcamentarioVO = planoOrcamentarioVO;
	}

	public boolean getApresentarBotaoGravar() {
		return getPlanoOrcamentarioVO().getSituacao().equals("EM")
				|| getPlanoOrcamentarioVO().getSituacao().equals("AT");
	}

	public int getCodigoUsuarioLogado() {
		return getUsuarioLogado().getPessoa().getCodigo();
	}

	public boolean getApresentarBotaoExcluirAtivar() {
		return !getPlanoOrcamentarioVO().getNovoObj().booleanValue()
				&& getPlanoOrcamentarioVO().getSituacao().equals("EM");
	}

	public boolean getApresentarBotaoFinalizar() {
		return !getPlanoOrcamentarioVO().getNovoObj().booleanValue()
				&& getPlanoOrcamentarioVO().getSituacao().equals("AT");
	}

	public boolean getApresentarVoltarParaConstrucao() {
		return (getPlanoOrcamentarioVO().getSituacao().equals("FI")
				|| getPlanoOrcamentarioVO().getSituacao().equals("AT")) && getPermiteReativarPlanoOrcamentario();
	}

	public void ativar() {
		try {
			getFacadeFactory().getPlanoOrcamentarioFacade()
					.validarSeTodasSolicitacoesOrcamentoAprovadas(getPlanoOrcamentarioVO());
			getPlanoOrcamentarioVO().ativarPlanoOrcamentario();
			getFacadeFactory().getPlanoOrcamentarioFacade().alterar(getPlanoOrcamentarioVO(), getUsuarioLogado(), true);
			setMensagemID("msg_PlanoOrcamentario_ativar");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void emConstrucao() {
		try {
			getPlanoOrcamentarioVO().voltarParaConstrucao();
			getFacadeFactory().getPlanoOrcamentarioFacade().alterar(getPlanoOrcamentarioVO(), getUsuarioLogado(), true);
			setMensagemID("msg_PlanoOrcamentario_emConstrucao");
		} catch (Exception e) {
			getPlanoOrcamentarioVO().ativarPlanoOrcamentario();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void preencherSolicitacaoOrcamentoPlanoOrcamentario() {
		try {

			SolicitacaoOrcamentoPlanoOrcamentarioVO obj = (SolicitacaoOrcamentoPlanoOrcamentarioVO) context()
					.getExternalContext().getRequestMap().get("solicitacaoOrcamentoPlanoOrcamentarioItens");
			setSolicitacaoOrcamentoPlanoOrcamentarioVO(obj);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void ordenarSolicitacaoAprovada() {
		Collections.sort(getSolicitacaoOrcamentoPlanoOrcamentarioVOsAprovados(), new Comparator() {
			@Override
			public int compare(Object p1, Object p2) {
				return ((SolicitacaoOrcamentoPlanoOrcamentarioVO) p1).getDepartamento().getNome()
						.compareTo(((SolicitacaoOrcamentoPlanoOrcamentarioVO) p2).getDepartamento().getNome());
			}
		});
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void ordenarSolicitacaoAguardandoAprovacao() {
		Collections.sort(getSolicitacaoOrcamentoPlanoOrcamentarioVOsAguardandoAprovacao(), new Comparator() {
			@Override
			public int compare(Object p1, Object p2) {
				return ((SolicitacaoOrcamentoPlanoOrcamentarioVO) p1).getDepartamento().getNome()
						.compareTo(((SolicitacaoOrcamentoPlanoOrcamentarioVO) p2).getDepartamento().getNome());
			}
		});
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void ordenarSolicitacaoAguardandoEmRevisao() {
		Collections.sort(getSolicitacaoOrcamentoPlanoOrcamentarioVOsEmRevisao(), new Comparator() {
			@Override
			public int compare(Object p1, Object p2) {
				return ((SolicitacaoOrcamentoPlanoOrcamentarioVO) p1).getDepartamento().getNome()
						.compareTo(((SolicitacaoOrcamentoPlanoOrcamentarioVO) p2).getDepartamento().getNome());
			}
		});
	}

	public void ordenarSolicitacoes() {
		ordenarSolicitacaoAprovada();
		ordenarSolicitacaoAguardandoAprovacao();
		ordenarSolicitacaoAguardandoEmRevisao();
	}

	public void alterarEmConstrucao() {
		try {
			if (getSolicitacaoOrcamentoPlanoOrcamentarioVO().getSituacao()
					.equals(SituacaoPlanoOrcamentarioEnum.AGUARDANDO_APROVACAO)) {
				getSolicitacaoOrcamentoPlanoOrcamentarioVOsAguardandoAprovacao()
						.removeIf(p -> p.getCodigo().equals(getSolicitacaoOrcamentoPlanoOrcamentarioVO().getCodigo()));
				getSolicitacaoOrcamentoPlanoOrcamentarioVOsEmConstrucao()
						.add(getSolicitacaoOrcamentoPlanoOrcamentarioVO());
			} else {
				getSolicitacaoOrcamentoPlanoOrcamentarioVOsAprovados()
						.removeIf(p -> p.getCodigo().equals(getSolicitacaoOrcamentoPlanoOrcamentarioVO().getCodigo()));
				getSolicitacaoOrcamentoPlanoOrcamentarioVOsEmConstrucao()
						.add(getSolicitacaoOrcamentoPlanoOrcamentarioVO());
			}
			getFacadeFactory().getSolicitacaoOrcamentoPlanoOrcamentarioFacade()
					.alterarSituacaoSolicitacaoPlanoOrcamentario(getSolicitacaoOrcamentoPlanoOrcamentarioVO(),
							SituacaoPlanoOrcamentarioEnum.EM_CONSTRUCAO.getValor(), getUsuarioLogado());
			ordenarSolicitacoes();
			setMensagemID("msg_SolicitacaoPlanoOrcamentario_emConstrucao", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void alterarRevisao() {
		try {
			if (getSolicitacaoOrcamentoPlanoOrcamentarioVO().getSituacao()
					.equals(SituacaoPlanoOrcamentarioEnum.AGUARDANDO_APROVACAO)) {
				getSolicitacaoOrcamentoPlanoOrcamentarioVOsAguardandoAprovacao()
						.removeIf(p -> p.getCodigo().equals(getSolicitacaoOrcamentoPlanoOrcamentarioVO().getCodigo()));
				getSolicitacaoOrcamentoPlanoOrcamentarioVOsEmRevisao()
						.add(getSolicitacaoOrcamentoPlanoOrcamentarioVO());
			} else {
				getSolicitacaoOrcamentoPlanoOrcamentarioVOsAprovados()
						.removeIf(p -> p.getCodigo().equals(getSolicitacaoOrcamentoPlanoOrcamentarioVO().getCodigo()));
				getSolicitacaoOrcamentoPlanoOrcamentarioVOsEmRevisao()
						.add(getSolicitacaoOrcamentoPlanoOrcamentarioVO());
			}

			getSolicitacaoOrcamentoPlanoOrcamentarioVO().setSituacao(SituacaoPlanoOrcamentarioEnum.REVISAO);
			getFacadeFactory().getSolicitacaoOrcamentoPlanoOrcamentarioFacade()
					.alterarSituacaoSolicitacaoPlanoOrcamentario(getSolicitacaoOrcamentoPlanoOrcamentarioVO(),
							SituacaoPlanoOrcamentarioEnum.REVISAO.getValor(), getUsuarioLogado());
			ordenarSolicitacoes();

			setMensagemID("msg_SolicitacaoPlanoOrcamentario_revisao", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void alterarAguardandoAprovacao() {
		try {
			getFacadeFactory().getSolicitacaoOrcamentoPlanoOrcamentarioFacade()
					.alterarSituacaoSolicitacaoPlanoOrcamentario(getSolicitacaoOrcamentoPlanoOrcamentarioVO(),
							SituacaoPlanoOrcamentarioEnum.AGUARDANDO_APROVACAO.getValor(), getUsuarioLogado());

			getSolicitacaoOrcamentoPlanoOrcamentarioVOsAprovados()
					.removeIf(p -> p.getCodigo().equals(getSolicitacaoOrcamentoPlanoOrcamentarioVO().getCodigo()));
			getSolicitacaoOrcamentoPlanoOrcamentarioVOsAguardandoAprovacao()
					.add(getSolicitacaoOrcamentoPlanoOrcamentarioVO());
			ordenarSolicitacoes();
			limparMensagem();
			getFacadeFactory().getSolicitacaoOrcamentoPlanoOrcamentarioFacade()
					.realizarCalculoValorAprovadoPorCategoriaDespesa(getSolicitacaoOrcamentoPlanoOrcamentarioVO());
			setMensagemID("msg_SolicitacaoPlanoOrcamentario_aguardandoAprovado", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void alterarAprovado() {
		try {
			limparMensagem();
			getFacadeFactory().getSolicitacaoOrcamentoPlanoOrcamentarioFacade()
					.realizarCalculoValorAprovadoPorCategoriaDespesa(getSolicitacaoOrcamentoPlanoOrcamentarioVO());
			getSolicitacaoOrcamentoPlanoOrcamentarioVO().setSituacao(SituacaoPlanoOrcamentarioEnum.APROVADO);

			getFacadeFactory().getSolicitacaoOrcamentoPlanoOrcamentarioFacade()
					.alterar(getSolicitacaoOrcamentoPlanoOrcamentarioVO(), getUsuarioLogado());

			getSolicitacaoOrcamentoPlanoOrcamentarioVOsAguardandoAprovacao()
					.removeIf(p -> p.getCodigo().equals(getSolicitacaoOrcamentoPlanoOrcamentarioVO().getCodigo()));
			getSolicitacaoOrcamentoPlanoOrcamentarioVOsAprovados().add(getSolicitacaoOrcamentoPlanoOrcamentarioVO());

			ordenarSolicitacoes();

			setMensagemID("msg_SolicitacaoPlanoOrcamentario_aprovado", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void aprovarSolicitacaoOrcamentoPlanoOrcamentario() {
		try {
			getFacadeFactory().getPlanoOrcamentarioFacade().alterar(getPlanoOrcamentarioVO(), getUsuarioLogado(),
					permitirRealizarManejamentoSaldoAprovado());
			setMensagemID("msg_PlanoOrcamentario_ativar");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public String finalizar() {
		try {
			getPlanoOrcamentarioVO().finalizarPlanoOrcamentario();
			getFacadeFactory().getPlanoOrcamentarioFacade().finalizar(getPlanoOrcamentarioVO(), getUsuarioLogado(),
					true);
			setMensagemID("msg_PlanoOrcamentario_finalizar");
			return Uteis.getCaminhoRedirecionamentoNavegacao("planoOrcamentarioForm.xhtml");
		} catch (Exception e) {
			getPlanoOrcamentarioVO().ativarPlanoOrcamentario();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("planoOrcamentarioForm.xhtml");
		}
	}

	public boolean permitirRealizarManejamentoSaldoAprovado() {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidade(
					"PlanoOrcamentario_permitirRealizarManejamentoSaldoAprovado", getUsuarioLogado());

			return getSolicitacaoOrcamentoPlanoOrcamentarioVO().getSituacao()
					.equals(SituacaoPlanoOrcamentarioEnum.APROVADO);
		} catch (Exception e) {
			return false;
		}
	}

	public List<DetalhamentoPeriodoOrcamentoVO> getListaDetalhamentoOrcamentarioPeriodoDepartamento() {
		if (listaDetalhamentoOrcamentarioPeriodoDepartamento == null) {
			listaDetalhamentoOrcamentarioPeriodoDepartamento = new ArrayList<>();
		}
		return listaDetalhamentoOrcamentarioPeriodoDepartamento;
	}

	public void setListaDetalhamentoOrcamentarioPeriodoDepartamento(
			List<DetalhamentoPeriodoOrcamentoVO> listaDetalhamentoOrcamentarioPeriodoDepartamento) {
		this.listaDetalhamentoOrcamentarioPeriodoDepartamento = listaDetalhamentoOrcamentarioPeriodoDepartamento;
	}

	public Boolean getFecharModalDistribuiOrcamento() {
		if (fecharModalDistribuiOrcamento == null) {
			fecharModalDistribuiOrcamento = Boolean.FALSE;
		}
		return fecharModalDistribuiOrcamento;
	}

	public void setFecharModalDistribuiOrcamento(Boolean fecharModalDistribuiOrcamento) {
		this.fecharModalDistribuiOrcamento = fecharModalDistribuiOrcamento;
	}

	public Boolean getFecharModalOrcamentoTotalPeridodo() {
		if (fecharModalOrcamentoTotalPeridodo == null) {
			fecharModalOrcamentoTotalPeridodo = Boolean.FALSE;
		}
		return fecharModalOrcamentoTotalPeridodo;
	}

	public void setFecharModalOrcamentoTotalPeridodo(Boolean fecharModalOrcamentoTotalPeridodo) {
		this.fecharModalOrcamentoTotalPeridodo = fecharModalOrcamentoTotalPeridodo;
	}

	public String getMensagemPadraoNotificacao() {
		if (mensagemPadraoNotificacao == null) {
			mensagemPadraoNotificacao = "";
		}
		return mensagemPadraoNotificacao;
	}

	public void setMensagemPadraoNotificacao(String mensagemPadraoNotificacao) {
		this.mensagemPadraoNotificacao = mensagemPadraoNotificacao;
	}

	public Boolean getEnviarComunicadoPorEmail() {
		if (enviarComunicadoPorEmail == null) {
			enviarComunicadoPorEmail = Boolean.TRUE;
		}
		return enviarComunicadoPorEmail;
	}

	public void setEnviarComunicadoPorEmail(Boolean enviarComunicadoPorEmail) {
		this.enviarComunicadoPorEmail = enviarComunicadoPorEmail;
	}

	public Boolean getPermiteReativarPlanoOrcamentario() {
		if (permiteReativarPlanoOrcamentario == null) {
			permiteReativarPlanoOrcamentario = false;
		}
		return permiteReativarPlanoOrcamentario;
	}

	public void setPermiteReativarPlanoOrcamentario(Boolean permiteReativarPlanoOrcamentario) {
		this.permiteReativarPlanoOrcamentario = permiteReativarPlanoOrcamentario;
	}

	public void verificarPermissaoReativarPlanoOrcamentario() {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidade("PermitirReativarPlanoOrcamentario",
					getUsuarioLogado());
			setPermiteReativarPlanoOrcamentario(Boolean.TRUE);
		} catch (Exception e) {
			setPermiteReativarPlanoOrcamentario(Boolean.FALSE);
		}
	}

	public List<ComunicacaoInternaVO> getListaComunicacaoInterna() {
		if (listaComunicacaoInterna == null) {
			listaComunicacaoInterna = new ArrayList<>();
		}
		return listaComunicacaoInterna;
	}

	public void setListaComunicacaoInterna(List<ComunicacaoInternaVO> listaComunicacaoInterna) {
		this.listaComunicacaoInterna = listaComunicacaoInterna;
	}

	public List<RequisicaoItemVO> getListaItemRequisicao() {
		if (listaItemRequisicao == null) {
			listaItemRequisicao = new ArrayList<>();
		}
		return listaItemRequisicao;
	}

	public void setListaItemRequisicao(List<RequisicaoItemVO> listaItemRequisicao) {
		this.listaItemRequisicao = listaItemRequisicao;
	}

	public SolicitacaoOrcamentoPlanoOrcamentarioVO getSolicitacaoOrcamentoPlanoOrcamentarioVO() {
		if (solicitacaoOrcamentoPlanoOrcamentarioVO == null) {
			solicitacaoOrcamentoPlanoOrcamentarioVO = new SolicitacaoOrcamentoPlanoOrcamentarioVO();
		}
		return solicitacaoOrcamentoPlanoOrcamentarioVO;
	}

	public void setSolicitacaoOrcamentoPlanoOrcamentarioVO(
			SolicitacaoOrcamentoPlanoOrcamentarioVO solicitacaoOrcamentoPlanoOrcamentarioVO) {
		this.solicitacaoOrcamentoPlanoOrcamentarioVO = solicitacaoOrcamentoPlanoOrcamentarioVO;
	}

	public void realizarAjustesValoresConformeValorAutorizado() {
		selecionarSolicitacaoOrcamentoPlanoOrcamentario();
		try {
			limparMensagem();
			getFacadeFactory().getSolicitacaoOrcamentoPlanoOrcamentarioFacade()
					.realizarCalculoValorAprovadoPorCategoriaDespesa(getSolicitacaoOrcamentoPlanoOrcamentarioVO());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public List<SolicitacaoOrcamentoPlanoOrcamentarioVO> getSolicitacaoOrcamentoPlanoOrcamentarioVOsEmConstrucao() {
		if (solicitacaoOrcamentoPlanoOrcamentarioVOsEmConstrucao == null) {
			solicitacaoOrcamentoPlanoOrcamentarioVOsEmConstrucao = new ArrayList<>();
		}
		return solicitacaoOrcamentoPlanoOrcamentarioVOsEmConstrucao;
	}

	public void setSolicitacaoOrcamentoPlanoOrcamentarioVOsEmConstrucao(
			List<SolicitacaoOrcamentoPlanoOrcamentarioVO> solicitacaoOrcamentoPlanoOrcamentarioVOsEmConstrucao) {
		this.solicitacaoOrcamentoPlanoOrcamentarioVOsEmConstrucao = solicitacaoOrcamentoPlanoOrcamentarioVOsEmConstrucao;
	}

	public List<SolicitacaoOrcamentoPlanoOrcamentarioVO> getSolicitacaoOrcamentoPlanoOrcamentarioVOsAguardandoAprovacao() {
		if (solicitacaoOrcamentoPlanoOrcamentarioVOsAguardandoAprovacao == null) {
			solicitacaoOrcamentoPlanoOrcamentarioVOsAguardandoAprovacao = new ArrayList<>();
		}
		return solicitacaoOrcamentoPlanoOrcamentarioVOsAguardandoAprovacao;
	}

	public void setSolicitacaoOrcamentoPlanoOrcamentarioVOsAguardandoAprovacao(
			List<SolicitacaoOrcamentoPlanoOrcamentarioVO> solicitacaoOrcamentoPlanoOrcamentarioVOsAguardandoAprovacao) {
		this.solicitacaoOrcamentoPlanoOrcamentarioVOsAguardandoAprovacao = solicitacaoOrcamentoPlanoOrcamentarioVOsAguardandoAprovacao;
	}

	public List<SolicitacaoOrcamentoPlanoOrcamentarioVO> getSolicitacaoOrcamentoPlanoOrcamentarioVOsAprovados() {
		if (solicitacaoOrcamentoPlanoOrcamentarioVOsAprovados == null) {
			solicitacaoOrcamentoPlanoOrcamentarioVOsAprovados = new ArrayList<>();
		}
		return solicitacaoOrcamentoPlanoOrcamentarioVOsAprovados;
	}

	public void setSolicitacaoOrcamentoPlanoOrcamentarioVOAprovados(
			List<SolicitacaoOrcamentoPlanoOrcamentarioVO> solicitacaoOrcamentoPlanoOrcamentarioVOsAprovados) {
		this.solicitacaoOrcamentoPlanoOrcamentarioVOsAprovados = solicitacaoOrcamentoPlanoOrcamentarioVOsAprovados;
	}

	public List<SolicitacaoOrcamentoPlanoOrcamentarioVO> getSolicitacaoOrcamentoPlanoOrcamentarioVOsEmRevisao() {
		if (solicitacaoOrcamentoPlanoOrcamentarioVOsEmRevisao == null) {
			solicitacaoOrcamentoPlanoOrcamentarioVOsEmRevisao = new ArrayList<>();
		}
		return solicitacaoOrcamentoPlanoOrcamentarioVOsEmRevisao;
	}

	public void setSolicitacaoOrcamentoPlanoOrcamentarioVOsEmRevisao(
			List<SolicitacaoOrcamentoPlanoOrcamentarioVO> solicitacaoOrcamentoPlanoOrcamentarioVOsEmRevisao) {
		this.solicitacaoOrcamentoPlanoOrcamentarioVOsEmRevisao = solicitacaoOrcamentoPlanoOrcamentarioVOsEmRevisao;
	}

	public ItemSolicitacaoOrcamentoPlanoOrcamentarioVO getItemSolicitacaoOrcamentoPlanoOrcamentarioVO() {
		if (itemSolicitacaoOrcamentoPlanoOrcamentarioVO == null) {
			itemSolicitacaoOrcamentoPlanoOrcamentarioVO = new ItemSolicitacaoOrcamentoPlanoOrcamentarioVO();
		}
		return itemSolicitacaoOrcamentoPlanoOrcamentarioVO;
	}

	public void setItemSolicitacaoOrcamentoPlanoOrcamentarioVO(
			ItemSolicitacaoOrcamentoPlanoOrcamentarioVO itemSolicitacaoOrcamentoPlanoOrcamentarioVO) {
		this.itemSolicitacaoOrcamentoPlanoOrcamentarioVO = itemSolicitacaoOrcamentoPlanoOrcamentarioVO;
	}
}
