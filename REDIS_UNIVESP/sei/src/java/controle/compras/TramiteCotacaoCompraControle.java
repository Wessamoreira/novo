package controle.compras;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.administrativo.CargoVO;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.compras.DepartamentoTramiteCotacaoCompraVO;
import negocio.comuns.compras.DepartamentoTramiteCotacaoCompraVO.TipoControleFinanceiroEnum;
import negocio.comuns.compras.SituacaoTramiteEnum;
import negocio.comuns.compras.TipoDistribuicaoCotacaoEnum;
import negocio.comuns.compras.TramiteCotacaoCompraVO;
import negocio.comuns.protocolo.enumeradores.TipoPoliticaDistribuicaoEnum;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;

@Controller("TramiteCotacaoCompraControle")
@Scope("viewScope")
@Lazy
public class TramiteCotacaoCompraControle extends SuperControle implements Serializable {

	private static final long serialVersionUID = 4663556939511335812L;

	private Boolean abrirPanelGravar;

	// inicio novos campos

	private TramiteCotacaoCompraVO tramiteVO;
	private DepartamentoTramiteCotacaoCompraVO departamentoTramiteVO;

	private List<SelectItem> listaSelectItemSituacaoTramite;
	private List<SelectItem> listaSelectItemTipoDistribuicaoCotacao;
	private List<SelectItem> listaSelectItemTipoPoliticaDistribuicao;
	private List<SelectItem> listaSelectItemTipoControleFinanceiro;	

	private String campoConsultaDepartamento;
	private String valorConsultaDepartamento;
	protected List<DepartamentoVO> listaConsultaDepartamento;

	private String campoConsultaCargo;
	private String valorConsultaCargo;
	protected List<CargoVO> listaConsultaCargo;

	private String campoConsultaPessoa;
	private String valorConsultaPessoa;
	protected List<FuncionarioVO> listaConsultaPessoa;
	private List<SelectItem> listaSelectItemUnidadeEnsino;

	Locale locale;
	// fim novos campos

	public TramiteCotacaoCompraControle() throws Exception {
		// obterUsuarioLogado();
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");
	}

	public void inicializarResponsavel() {
		try {
			this.getTramiteVO().setResponsavel(getUsuarioLogadoClone());
		} catch (Exception e) {
		}
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe
	 * <code>Compra</code> para edição pelo usuário da aplicação.
	 */
	public String novo() throws Exception {
		registrarAtividadeUsuario(getUsuarioLogado(), "TramiteCotacaoCompra", "Novo Tramite", "Novo");
		this.setTramiteVO(new TramiteCotacaoCompraVO());
		this.setDepartamentoTramiteVO(new DepartamentoTramiteCotacaoCompraVO());
		inicializarResponsavel();
		montarListaSelectItemUnidadeEnsino();
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("tramiteCotacaoCompraForm.xhtml");
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe
	 * <code>Compra</code> para alteração. O objeto desta classe é disponibilizado
	 * na session da página (request) para que o JSP correspondente possa
	 * disponibilizá-lo para edição.
	 */
	public String editar() throws Exception {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "TramiteCotacaoCompra", "Inicializando Editar Tramite", "Editando");
			TramiteCotacaoCompraVO obj = (TramiteCotacaoCompraVO) context().getExternalContext().getRequestMap().get("compraItens");
			setTramiteVO(getFacadeFactory().getTramiteFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			getTramiteVO().setTramiteUsado(getFacadeFactory().getTramiteFacade().consultarUsoTramite(obj, this.getUsuarioLogado()));
			setDepartamentoTramiteVO(new DepartamentoTramiteCotacaoCompraVO());
			registrarAtividadeUsuario(getUsuarioLogado(), "TramiteCotacaoCompra", "Inicializando Editar Tramite", "Editando");
			montarListaSelectItemUnidadeEnsino();
			setMensagemID("msg_dados_editar");
			return Uteis.getCaminhoRedirecionamentoNavegacao("tramiteCotacaoCompraForm.xhtml");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}	

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto da
	 * classe <code>Compra</code>. Caso o objeto seja novo (ainda não gravado no BD)
	 * é acionado a operação <code>incluir()</code>. Caso contrário é acionado o
	 * <code>alterar()</code>. Se houver alguma inconsistência o objeto não é
	 * gravado, sendo re-apresentado para o usuário juntamente com uma mensagem de
	 * erro.
	 */
	public String gravar() {
		try {

			this.getTramiteVO().validarDados();
			if (this.getTramiteVO().isNovoObj()) {

				registrarAtividadeUsuario(getUsuarioLogado(), "TramiteCotacaoCompra", "Inicializando Incluir Tramite", "Incluindo");
				getFacadeFactory().getTramiteFacade().incluir(this.getTramiteVO(), true, getUsuarioLogado());
				registrarAtividadeUsuario(getUsuarioLogado(), "TramiteCotacaoCompra", "Finalizando Incluir Tramite", "Incluindo");

			} else {
				registrarAtividadeUsuario(getUsuarioLogado(), "CompraControle", "Inicializando Alterar Compra", "Alterando");
				getFacadeFactory().getTramiteFacade().alterar(this.getTramiteVO(), true, getUsuarioLogado());
				registrarAtividadeUsuario(getUsuarioLogado(), "CompraControle", "Finalizando Alterar Compra", "Alterando");
			}
			setMensagemID("msg_dados_gravados");
			return "";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP
	 * tramiteCons.jsp. Define o tipo de consulta a ser executada, por meio de
	 * ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
	 * resultado, disponibiliza um List com os objetos selecionados na sessao da
	 * pagina.
	 */
	public String consultar() {
		try {
			this.buscarNoBanco();
			return Uteis.getCaminhoRedirecionamentoNavegacao("tramiteCotacaoCompraForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("tramiteCotacaoCompraForm.xhtml");
		}
	}

	private void buscarNoBanco() throws Exception {
		getControleConsultaOtimizado().setLimitePorPagina(10);
		registrarAtividadeUsuario(getUsuarioLogado(), "TramiteCotacaoCompra", "Inicializando Consultar Tramite", "Consultando");
		super.consultar();
		if (getControleConsulta().getCampoConsulta().equals("nome")) {

			if (getControleConsulta().getValorConsulta() == null) {
				getControleConsulta().setValorConsulta("");
			}

			getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getTramiteFacade().consultarPornome(getControleConsulta().getValorConsulta(), false, getUsuarioLogado()));
		}

		registrarAtividadeUsuario(getUsuarioLogado(), "CompraControle", "Finalizando Consultar Compra", "Consultando");
		setMensagemID("msg_dados_consultados");
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe
	 * <code>CompraVO</code> Após a exclusão ela automaticamente aciona a rotina
	 * para uma nova inclusão.
	 */
	public String excluir() {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "TramiteCotacaoCompra", "Inicializando Excluir Tramite", "Excluindo");
			getFacadeFactory().getTramiteFacade().excluir(this.getTramiteVO(), true, getUsuarioLogado());
			setTramiteVO(new TramiteCotacaoCompraVO());
			setDepartamentoTramiteVO(new DepartamentoTramiteCotacaoCompraVO());
			inicializarResponsavel();
			registrarAtividadeUsuario(getUsuarioLogado(), "TramiteCotacaoCompra", "Finalizando Excluir Tramite", "Excluido");
			setMensagemID("msg_dados_excluidos");
			return Uteis.getCaminhoRedirecionamentoNavegacao("tramiteCotacaoCompraForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("tramiteCotacaoCompraForm.xhtml");
		}
	}

	public List<SelectItem> getTipoConsultaComboDepartamentos() {
		List<SelectItem> itens = new ArrayList<>();
		itens.add(new SelectItem("nome", "Nome"));

		return itens;
	}

	public List<SelectItem> getTipoConsultaComboCargos() {
		List<SelectItem> itens = new ArrayList<>();
		itens.add(new SelectItem("nome", "Nome"));

		return itens;
	}

	public List<SelectItem> getTipoConsultaComboPessoas() {
		List<SelectItem> itens = new ArrayList<>();
		itens.add(new SelectItem("nome", "Nome"));

		return itens;
	}

	public boolean mostrarValorMinimo() {
		return this.departamentoTramiteVO.getTipoControleFinanceiro().equals(TipoControleFinanceiroEnum.FAIXA_VALORES) ||
		        this.departamentoTramiteVO.getTipoControleFinanceiro().equals(TipoControleFinanceiroEnum.VALOR_MINIMO);
	}

	public boolean mostrarValorMaximo() {
		return this.departamentoTramiteVO.getTipoControleFinanceiro().equals(TipoControleFinanceiroEnum.FAIXA_VALORES);
	}

	public void consultarDepartamento() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaDepartamento().equals("codigo")) {
				objs = getFacadeFactory().getDepartamentoFacade().consultarPorCodigoPorUnidadeEnsino(Uteis.getValorInteiro(getValorConsultaDepartamento()), null, true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaDepartamento().equals("nome")) {
				objs = getFacadeFactory().getDepartamentoFacade().consultarPorNomePorUnidadeEnsino(getValorConsultaDepartamento(), null, true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaDepartamento(objs);
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public void consultarCargo() {

		try {

			if (this.getCampoConsultaCargo().equals("nome")) {
				this.setListaConsultaCargo(getFacadeFactory().getCargoFacade().consultarPorDepartamentoENome(this.getValorConsultaCargo(), this.getDepartamentoTramiteVO().getDepartamentoVO(), true, Uteis.NIVELMONTARDADOS_TODOS, this.getUsuarioLogado()));

			}

			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			setListaConsultaDepartamento(new ArrayList<>());
			setMensagemDetalhada("msg_erro", e.getMessage());
		}

	}

	public void consultarPessoa() {

		try {

			if (this.getCampoConsultaPessoa().equals("nome")) {
				this.setListaConsultaPessoa(getFacadeFactory().getFuncionarioFacade().consultarFuncionarioPorNomeDepartamentoAtivo(this.getValorConsultaPessoa(), getDepartamentoTramiteVO().getDepartamentoVO(), true, Uteis.NIVELMONTARDADOS_COMBOBOX, this.getUsuarioLogado()));

			}

			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			setListaConsultaDepartamento(new ArrayList<>());
			setMensagemDetalhada("msg_erro", e.getMessage());
		}

	}

	/*
	 * Método responsável por adicionar um novo objeto da classe
	 * <code>CompraItem</code> para o objeto <code>compraVO</code> da classe
	 * <code>Compra</code>
	 */

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

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes de
	 * uma consulta.
	 */
	public String inicializarConsultar() {
		try {
			removerObjetoMemoria(this);
			this.buscarNoBanco();
			setMensagemID("msg_entre_prmconsulta");
			return Uteis.getCaminhoRedirecionamentoNavegacao("tramiteCotacaoCompraCons.xhtml");
		} catch (Exception e) {
			return Uteis.getCaminhoRedirecionamentoNavegacao("tramiteCotacaoCompraCons.xhtml");
		}
	}

	public String getOncompletGravar() {
		if (getAbrirPanelGravar()) {
			return "RichFaces.$('panelGravar').show();";
		}
		return "";
	}

	public void scrollerListener(DataScrollEvent DataScrollEvent) throws Exception {
		getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
		getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
		consultar();
	}

	public Boolean getIsApresentarPeriodo() {
		if (getControleConsulta().getCampoConsulta().equals("data")) {
			return true;
		}
		return false;
	}

	public Boolean getAbrirPanelGravar() {
		if (abrirPanelGravar == null) {
			abrirPanelGravar = Boolean.FALSE;
		}
		return abrirPanelGravar;
	}

	public void setAbrirPanelGravar(Boolean abrirPanelGravar) {
		this.abrirPanelGravar = abrirPanelGravar;
	}

	// inicio novos metodos

	public void limparCampoDepartamento() {
		this.departamentoTramiteVO.setDepartamentoVO(null);
		this.limparDepartamentoTramiteCotacaoCompra();
	}

	public void limparCargoEFuncionario() {
		this.departamentoTramiteVO.setCargoVO(null);
		this.departamentoTramiteVO.setFuncionario(null);
		if(!isFuncionarioDepartamentoOuFuncionarioCargoDepartamento()){
			this.getDepartamentoTramiteVO().setTipoPoliticaDistribuicao(null);
		}
	}

	public void limparDepartamentoTramiteCotacaoCompra() {
		this.getDepartamentoTramiteVO().setTipoPoliticaDistribuicao(null);
		this.getDepartamentoTramiteVO().setTipoDistribuicaoCotacao(TipoDistribuicaoCotacaoEnum.COORDENADOR_CURSO_ESPECIFICO_TRAMITE);
		this.limparCargoEFuncionario();
	}

	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> itens = new ArrayList<>();
		itens.add(new SelectItem("nome", "Nome"));

		return itens;
	}

	public void editarDepartamentoTransite() throws Exception {
		DepartamentoTramiteCotacaoCompraVO departamentoTramite = (DepartamentoTramiteCotacaoCompraVO) context().getExternalContext().getRequestMap().get("itemDepartamentoTransite");
		this.setDepartamentoTramiteVO(departamentoTramite);
	}

	public void aumentarPrioridade() throws Exception {
		DepartamentoTramiteCotacaoCompraVO departamentoTramite = (DepartamentoTramiteCotacaoCompraVO) context().getExternalContext().getRequestMap().get("itemDepartamentoTransite");
		this.getTramiteVO().aumentarPrioridade(departamentoTramite);
	}

	public void diminuirPrioridade() throws Exception {
		DepartamentoTramiteCotacaoCompraVO departamentoTramite = (DepartamentoTramiteCotacaoCompraVO) context().getExternalContext().getRequestMap().get("itemDepartamentoTransite");
		this.getTramiteVO().diminuirPrioridade(departamentoTramite);
	}

	public void removerDepartamentoTransite() throws Exception {
		DepartamentoTramiteCotacaoCompraVO departamentoTramite = (DepartamentoTramiteCotacaoCompraVO) context().getExternalContext().getRequestMap().get("itemDepartamentoTransite");
		this.getTramiteVO().removerDepartamentoTramite(departamentoTramite);
		setMensagemID("msg_dados_excluidos");
	}

	public void adicionarDepartamentoTramite()  {
		try {
			this.getDepartamentoTramiteVO().validarDados();
			this.getTramiteVO().adicionarDepartamentoTramite(this.getDepartamentoTramiteVO());
			this.setDepartamentoTramiteVO(null);
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarDepartamento() {
		DepartamentoVO departamentoVO = (DepartamentoVO) context().getExternalContext().getRequestMap().get("departamentoItem");
		this.getDepartamentoTramiteVO().setDepartamentoVO(departamentoVO);
		this.listaConsultaDepartamento.clear();
		this.valorConsultaDepartamento = null;
		this.campoConsultaDepartamento = null;
	}

	public void selecionarCargo() {
		CargoVO cargoVO = (CargoVO) context().getExternalContext().getRequestMap().get("cargoItem");
		this.getDepartamentoTramiteVO().setCargoVO(cargoVO);
		this.listaConsultaCargo.clear();
		this.valorConsultaCargo = null;
		this.campoConsultaCargo = null;
	}

	public void selecionarPessoa() {
		FuncionarioVO pessoa = (FuncionarioVO) context().getExternalContext().getRequestMap().get("pessoaItem");
		this.getDepartamentoTramiteVO().setFuncionario(pessoa);
		this.listaConsultaPessoa.clear();
		this.valorConsultaPessoa = null;
		this.campoConsultaPessoa = null;
	}

	public boolean isFuncionarioCargoDepartamento() {
		return this.getDepartamentoTramiteVO().getTipoDistribuicaoCotacao().equals(TipoDistribuicaoCotacaoEnum.FUNCIONARIO_CARGO_DEPARTAMENTO);
	}

	public boolean isCotacaoFuncionarioEspecifico() {
		return this.getDepartamentoTramiteVO().getTipoDistribuicaoCotacao().equals(TipoDistribuicaoCotacaoEnum.FUNCIONARIO_ESPECIFICO);
	}

	public boolean isFuncionarioDepartamentoOuFuncionarioCargoDepartamento() {
		return this.getDepartamentoTramiteVO().getTipoDistribuicaoCotacao().equals(TipoDistribuicaoCotacaoEnum.FUNCIONARIO_DEPARTAMENTO) || this.getDepartamentoTramiteVO().getTipoDistribuicaoCotacao().equals(TipoDistribuicaoCotacaoEnum.FUNCIONARIO_CARGO_DEPARTAMENTO);

	}

	public TramiteCotacaoCompraVO getTramiteVO() {
		this.tramiteVO = Optional.ofNullable(this.tramiteVO).orElse(new TramiteCotacaoCompraVO());
		return tramiteVO;
	}

	public void setTramiteVO(TramiteCotacaoCompraVO tramiteVO) {
		this.tramiteVO = tramiteVO;
	}

	public DepartamentoTramiteCotacaoCompraVO getDepartamentoTramiteVO() {

		this.departamentoTramiteVO = Optional.ofNullable(this.departamentoTramiteVO).orElse(new DepartamentoTramiteCotacaoCompraVO());

		return departamentoTramiteVO;
	}

	public void setDepartamentoTramiteVO(DepartamentoTramiteCotacaoCompraVO departamentoTramiteVO) {
		this.departamentoTramiteVO = departamentoTramiteVO;
	}

	public List<SelectItem> montarListaSelectItemSituacaoTramite() {
		List<SelectItem> items = new ArrayList<>();

		for (SituacaoTramiteEnum situacaoTramite : SituacaoTramiteEnum.values()) {
			items.add(new SelectItem(situacaoTramite, situacaoTramite.getNome()));
		}
		return items;
	}

	public List<SelectItem> montarListaSelectItemTipoDistribuicaoCotacao() {
		List<SelectItem> items = new ArrayList<>();

		for (TipoDistribuicaoCotacaoEnum tipoDistribuicaoCotacao : TipoDistribuicaoCotacaoEnum.values()) {
			items.add(new SelectItem(tipoDistribuicaoCotacao, tipoDistribuicaoCotacao.getNome()));
		}

		return items;

	}

	public List<SelectItem> montarListaSelectItemTipoPoliticaDistribuicao() {
		List<SelectItem> items = new ArrayList<>();

		items.add(new SelectItem(null, ""));
		for (TipoPoliticaDistribuicaoEnum tipoPoliticaDistribuicao : TipoPoliticaDistribuicaoEnum.values()) {
			items.add(new SelectItem(tipoPoliticaDistribuicao, tipoPoliticaDistribuicao.getValorApresentar()));
		}

		return items;

	}

	public List<SelectItem> montarListaSelectItemTipoControleFinanceiro() {
		List<SelectItem> items = new ArrayList<>();

		for (TipoControleFinanceiroEnum tipoPoliticaDistribuicao : TipoControleFinanceiroEnum.values()) {
			items.add(new SelectItem(tipoPoliticaDistribuicao, tipoPoliticaDistribuicao.getValorApresentar()));
		}

		return items;

	}

	public List<SelectItem> getListaSelectItemSituacaoTramite() {

		this.listaSelectItemSituacaoTramite = Optional.ofNullable(this.listaSelectItemSituacaoTramite).orElseGet(() -> this.montarListaSelectItemSituacaoTramite());
		return this.listaSelectItemSituacaoTramite;

	}

	public void setListaSelectItemSituacaoTramite(List<SelectItem> listaSelectItemSituacaoTramite) {
		this.listaSelectItemSituacaoTramite = listaSelectItemSituacaoTramite;
	}

	public List<SelectItem> getListaSelectItemTipoDistribuicaoCotacao() {

		this.listaSelectItemTipoDistribuicaoCotacao = Optional.ofNullable(this.listaSelectItemTipoDistribuicaoCotacao).orElseGet(() -> this.montarListaSelectItemTipoDistribuicaoCotacao());
		return this.listaSelectItemTipoDistribuicaoCotacao;
	}

	public void setListaSelectItemTipoDistribuicaoCotacao(List<SelectItem> listaSelectItemTipoDistribuicaoCotacao) {
		this.listaSelectItemTipoDistribuicaoCotacao = listaSelectItemTipoDistribuicaoCotacao;
	}

	public List<SelectItem> getListaSelectItemTipoPoliticaDistribuicao() {

		this.listaSelectItemTipoPoliticaDistribuicao = Optional.ofNullable(this.listaSelectItemTipoPoliticaDistribuicao).orElseGet(() -> this.montarListaSelectItemTipoPoliticaDistribuicao());
		return this.listaSelectItemTipoPoliticaDistribuicao;
	}

	public void setListaSelectItemTipoPoliticaDistribuicao(List<SelectItem> listaSelectItemTipoPoliticaDistribuicao) {
		this.listaSelectItemTipoPoliticaDistribuicao = listaSelectItemTipoPoliticaDistribuicao;
	}

	public String getCampoConsultaDepartamento() {
		return campoConsultaDepartamento;
	}

	public void setCampoConsultaDepartamento(String campoConsultaDepartamento) {
		this.campoConsultaDepartamento = campoConsultaDepartamento;
	}

	public String getValorConsultaDepartamento() {
		return valorConsultaDepartamento;
	}

	public void setValorConsultaDepartamento(String valorConsultaDepartamento) {
		this.valorConsultaDepartamento = valorConsultaDepartamento;
	}

	public String getCampoConsultaCargo() {
		return campoConsultaCargo;
	}

	public void setCampoConsultaCargo(String campoConsultaCargo) {
		this.campoConsultaCargo = campoConsultaCargo;
	}

	public String getValorConsultaCargo() {
		return valorConsultaCargo;
	}

	public void setValorConsultaCargo(String valorConsultaCargo) {
		this.valorConsultaCargo = valorConsultaCargo;
	}

	public String getCampoConsultaPessoa() {
		return campoConsultaPessoa;
	}

	public void setCampoConsultaPessoa(String campoConsultaPessoa) {
		this.campoConsultaPessoa = campoConsultaPessoa;
	}

	public String getValorConsultaPessoa() {
		return valorConsultaPessoa;
	}

	public void setValorConsultaPessoa(String valorConsultaPessoa) {
		this.valorConsultaPessoa = valorConsultaPessoa;
	}

	public List<DepartamentoVO> getListaConsultaDepartamento() {
		return listaConsultaDepartamento;
	}

	public void setListaConsultaDepartamento(List<DepartamentoVO> listaConsultaDepartamento) {
		this.listaConsultaDepartamento = listaConsultaDepartamento;
	}

	public List<CargoVO> getListaConsultaCargo() {
		return listaConsultaCargo;
	}

	public void setListaConsultaCargo(List<CargoVO> listaConsultaCargo) {
		this.listaConsultaCargo = listaConsultaCargo;
	}

	public List<FuncionarioVO> getListaConsultaPessoa() {
		return listaConsultaPessoa;
	}

	public void setListaConsultaPessoa(List<FuncionarioVO> listaConsultaPessoa) {
		this.listaConsultaPessoa = listaConsultaPessoa;
	}

	public Locale getLocale() {
		locale = Optional.ofNullable(this.locale).orElse(new Locale("pt", "BR"));
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public List<SelectItem> getListaSelectItemTipoControleFinanceiro() {
		this.listaSelectItemTipoControleFinanceiro = Optional.ofNullable(this.listaSelectItemTipoControleFinanceiro).orElse(this.montarListaSelectItemTipoControleFinanceiro());
		return listaSelectItemTipoControleFinanceiro;
	}

	public void setListaSelectItemTipoControleFinanceiro(List<SelectItem> listaSelectItemTipoControleFinanceiro) {
		this.listaSelectItemTipoControleFinanceiro = listaSelectItemTipoControleFinanceiro;
	}

	// fim novos methodos
	
	public List getListaSelectItemUnidadeEnsino() {
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}
	
	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo <code>GrupoTrabalho</code>. Buscando todos os objetos correspondentes a entidade <code>GrupoTrabalho</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemUnidadeEnsino() {
		try {
			montarListaSelectItemUnidadeEnsino("");
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());;
		}
	}
	
	public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			if (this.getUnidadeEnsinoLogado().getCodigo().intValue() != 0) {
				List objs = new ArrayList(0);
				objs.add(new SelectItem(this.getUnidadeEnsinoLogado().getCodigo(), this.getUnidadeEnsinoLogado().getNome()));
				setListaSelectItemUnidadeEnsino(objs);
				getTramiteVO().getUnidadeEnsinoPadrao().setCodigo(this.getUnidadeEnsinoLogado().getCodigo());
				return;
			}
			resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
			}
			setListaSelectItemUnidadeEnsino(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}
	
	public List consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
		List lista = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorNome(nomePrm, this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		return lista;
	}

}
