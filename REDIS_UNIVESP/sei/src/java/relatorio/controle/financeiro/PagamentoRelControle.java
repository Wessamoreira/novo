package relatorio.controle.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.financeiro.BancoVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.OperadoraCartaoVO;
import negocio.comuns.financeiro.ParceiroVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.TipoCartaoOperadoraCartaoEnum;
import negocio.comuns.utilitarias.dominios.TipoFormaPagamento;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.financeiro.PagamentoRelVO;
import relatorio.negocio.jdbc.financeiro.enumeradores.ContaPagarFiltrosEnum;

@Controller("PagamentoRelControle")
@Scope("viewScope")
@Lazy
public class PagamentoRelControle extends SuperControleRelatorio {

	private static final long serialVersionUID = 8570654202098682262L;

	protected List listaConsultaFornecedor;
	protected String valorConsultaFornecedor;
	protected String campoConsultaFornecedor;
	protected List listaConsultaBanco;
	protected List listaConsultaFuncionario;
	protected String valorConsultaBanco;
	protected String valorConsultaFuncionario;
	protected String campoConsultaBanco;
	protected String campoConsultaFuncionario;
	private List<SelectItem> listaSelectItemUnidadeEnsino;

	protected Date dataInicio;
	protected Date dataFim;
	protected String fornecedorNome;
	protected String fornecedorCpfCnpj;
	protected Integer fornecedor;
	protected UnidadeEnsinoVO unidadeEnsino;
	protected Integer funcionario;
	protected String funcionarioNome;
	protected String responsavelFinanceiroNome;
	protected Integer responsavelFinanceiro;
	protected String parceiroNome;
	protected Integer parceiro;
	protected Integer banco;
	private Integer aluno;
	private Integer tipo;
	protected String bancoNome;
	private String alunoNome;
	private String formaPagamento;
	private Integer filtroFornecedor;
	private Integer filtroFuncionario;
	private Integer filtroResponsavelFinanceiro;
	private Integer filtroParceiro;
	private Integer filtroBanco;
	private Integer filtroAluno;
	private Integer filtroTipo;
	private Boolean apresentarFiltroFornecedor;
	private Boolean apresentarFiltroResponsavelFinanceiro;
	private Boolean apresentarFiltroParceiro;
	private Boolean apresentarFiltroFuncionario;
	private Boolean apresentarFiltroBanco;
	private Boolean apresentarFiltroAluno;
	private Boolean apresentarFiltroTipo;
	private List<SelectItem> listaFiltroFornecedor;
	private List<SelectItem> listaFiltroFuncionario;
	private List<SelectItem> listaFiltroResponsavelFinanceiro;
	private List<SelectItem> listaFiltroParceiro;
	private List<SelectItem> listaFiltroBanco;
	private List<SelectItem> listaFiltroAluno;
	private List<SelectItem> listaFiltroTipo;
	private String layout;
	private String ordenar;
	private String tipoRelatorio;
	private String tipoConta;
	private Boolean filtrarDataFatoGerador;

	private Boolean apresentarCampoContaCorrente;
	private Boolean apresentarCampoContaCaixa;
	private List<SelectItem> listaSelectItemContaCorrente;
	private List<SelectItem> listaSelectItemContaCaixa;
	private Integer codigoContaCorrenteCaixa;
	protected List<SelectItem> listaSelectItemOperadoraCartao;
	private Boolean apresentarFiltroOperadoraCartao;
	private Integer filtroOperadoraCartao;
	private Integer operadoraCartao;

	private DataModelo dataModeloAluno;

	//
	public PagamentoRelControle() throws Exception {
		inicializarListasSelectItemTodosComboBox();
		setTipoRelatorio("AN");
		setMensagemID("msg_entre_prmrelatorio");
	}

	@PostConstruct
	public void carregarDadosRelatorio() {
		try {
			inicializarListasSelectItemTodosComboBox();
			setTipoRelatorio("AN");
			setMensagemID("msg_entre_prmrelatorio");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void imprimirPDF() {
		List<PagamentoRelVO> lista = null;
		try {
			lista = getFacadeFactory().getPagamentoRelFacade().criarObjeto(getDataInicio(), getDataFim(),
					getFiltrarDataFatoGerador(), getUnidadeEnsino(), getFornecedor(), getFornecedorNome(),
					getFornecedorCpfCnpj(), getFuncionarioNome(), getFuncionario(), getBanco(), getBancoNome(),
					getFiltroFornecedor(), getFiltroFuncionario(), getFiltroBanco(), getAluno(), getFiltroAluno(),
					getOrdenar(), getFormaPagamento(), getFiltroTipo(), getTipoRelatorio(),
					getFiltroResponsavelFinanceiro(), getResponsavelFinanceiro(), getResponsavelFinanceiroNome(),
					getFiltroParceiro(), getParceiro(), getParceiroNome(), getTipoConta(),
					getCodigoContaCorrenteCaixa(), getFiltroOperadoraCartao(), getOperadoraCartao());
			if (!lista.isEmpty()) {
				getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getPagamentoRelFacade()
						.designIReportRelatorio(getTipoRelatorio(), getLayout()));
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO()
						.setSubReport_Dir(getFacadeFactory().getPagamentoRelFacade().caminhoBaseIReportRelatorio());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				if (getTipoRelatorio().equals("AN")) {
					getSuperParametroRelVO().setTituloRelatorio("Relatório de Pagamentos (Analítico)");
				} else {
					getSuperParametroRelVO().setTituloRelatorio("Relatório de Pagamentos (Sintético)");
				}
				getSuperParametroRelVO().setListaObjetos(lista);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(
						getFacadeFactory().getPagamentoRelFacade().caminhoBaseIReportRelatorio());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setPeriodo(String.valueOf(Uteis.getData(getDataInicio())) + "  até  "
						+ String.valueOf(Uteis.getData(getDataFim())));

				if (getUnidadeEnsino().getCodigo() != null && getUnidadeEnsino().getCodigo() > 0) {
					getSuperParametroRelVO().setUnidadeEnsino((getFacadeFactory().getUnidadeEnsinoFacade()
							.consultarPorChavePrimaria(getUnidadeEnsino().getCodigo(), false,
									Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())).getNome());
				} else {
					getSuperParametroRelVO().setUnidadeEnsino("Todas");
				}

				getSuperParametroRelVO()
						.setFornecedor(getListaFiltroFornecedor().get(getFiltroFornecedor()).getLabel());

				getSuperParametroRelVO()
						.setFuncionario(getListaFiltroFuncionario().get(getFiltroFuncionario()).getLabel());

				getSuperParametroRelVO().setBanco(getListaFiltroBanco().get(getFiltroBanco()).getLabel());

				getSuperParametroRelVO().setAluno(getListaFiltroAluno().get(getFiltroAluno()).getLabel());

				getSuperParametroRelVO().setResponsavelFinanceiro(
						getListaFiltroResponsavelFinanceiro().get(getFiltroResponsavelFinanceiro()).getLabel());

				getSuperParametroRelVO().setParceiro(getListaFiltroParceiro().get(getFiltroParceiro()).getLabel());

				getSuperParametroRelVO().adicionarParametro("operadoraCartao",
						getListaFiltroParceiro().get(getFiltroOperadoraCartao()).getLabel());

				if (getFiltroTipo() == 0) {
					getSuperParametroRelVO().setFormaPagamento("Todos");
				} else if (getFiltroTipo() == 2) {
					getSuperParametroRelVO().setFormaPagamento(getFormaPagamento_Apresentar());
				}
				realizarImpressaoRelatorio();
				setMensagemID("msg_relatorio_ok");
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			Uteis.liberarListaMemoria(lista);

		}

	}

	public void consultarFornecedor() {
		try {
			List objs = new ArrayList<>(0);
			if (getCampoConsultaFornecedor().equals("nome")) {
				objs = getFacadeFactory().getFornecedorFacade().consultarPorNome(getValorConsultaFornecedor(), "AT",
						false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getCampoConsultaFornecedor().equals("razaoSocial")) {
				objs = getFacadeFactory().getFornecedorFacade().consultarPorRazaoSocial(getValorConsultaFornecedor(),
						"AT", false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getCampoConsultaFornecedor().equals("CNPJ")) {
				objs = getFacadeFactory().getFornecedorFacade().consultarPorCNPJ(getValorConsultaFornecedor(), "AT",
						false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getCampoConsultaFornecedor().equals("CPF")) {
				objs = getFacadeFactory().getFornecedorFacade().consultarPorCPF(getValorConsultaFornecedor(), "AT",
						false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			setListaConsultaFornecedor(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaFornecedor(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparDadosFornecedor() {
		setFornecedor(0);
		setFornecedorCpfCnpj("");
		setFornecedorNome("");
	}

	public void limparDadosFuncionario() {
		setFuncionario(0);
		setFuncionarioNome("");
	}

	public void limparDadosBanco() {
		setBanco(0);
		setBancoNome("");

	}

	public void limparDadosAluno() {
		setAluno(0);
		setAlunoNome("");

	}

	public void consultarBanco() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaBanco().equals("nome")) {
				objs = getFacadeFactory().getBancoFacade().consultarPorNome(getValorConsultaBanco(), false,
						Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaBanco().equals("nrBanco")) {
				objs = getFacadeFactory().getBancoFacade().consultarPorNrBanco(getValorConsultaBanco(), false,
						Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultaBanco(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaBanco(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarFuncionario() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaFuncionario().equals("nome")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorNome(getValorConsultaFuncionario(),
						getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("cpf")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorCPF(getValorConsultaFuncionario(),
						getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultaFuncionario(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaFuncionario(new ArrayList<>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarAluno() {
		try {
			List<MatriculaVO> objs = new ArrayList<>(0);
			if (getDataModeloAluno().getValorConsulta().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			if (getDataModeloAluno().getCampoConsulta().equals("matricula")) {
				objs.add(getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatricula(
						getDataModeloAluno().getValorConsulta(), this.getUnidadeEnsinoLogado().getCodigo(), false,
						Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(),
						getUsuarioLogado()));
				getDataModeloAluno().setListaConsulta(objs);
				if (Uteis.isAtributoPreenchido(objs) && Uteis.isAtributoPreenchido(objs.get(0).getAluno().getNome())) {
					getDataModeloAluno().setTotalRegistrosEncontrados(1);
				} else {
					getDataModeloAluno().setTotalRegistrosEncontrados(0);
				}
			}
			if (getDataModeloAluno().getCampoConsulta().equals("nomePessoa")) {
				getDataModeloAluno().setValorConsulta(getDataModeloAluno().getValorConsulta());
				getFacadeFactory().getMatriculaFacade().consultarMatriculas(getDataModeloAluno(), getUsuarioLogado());
			}

			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> tipoConsultaComboLayout;

	public List<SelectItem> getTipoConsultaComboLayout() {
		if (tipoConsultaComboLayout == null) {
			tipoConsultaComboLayout = new ArrayList<SelectItem>(0);
			tipoConsultaComboLayout.add(new SelectItem("layout1", "Layout 1"));
			tipoConsultaComboLayout.add(new SelectItem("layout2", "Layout 2"));
		}
		return tipoConsultaComboLayout;
	}

	public List<SelectItem> tipoConsultaComboOrdenacao;

	public List<SelectItem> getTipoConsultaComboOrdenacao() {
		if (tipoConsultaComboOrdenacao == null) {
			tipoConsultaComboOrdenacao = new ArrayList<SelectItem>(0);
			tipoConsultaComboOrdenacao.add(new SelectItem("nome", "Nome Favorecido"));
			tipoConsultaComboOrdenacao.add(new SelectItem("dataVencimento", "Data de Vencimento"));
			tipoConsultaComboOrdenacao.add(new SelectItem("dataPagamento", "Data de Pagamento"));
		}
		return tipoConsultaComboOrdenacao;
	}

	public List<SelectItem> tipoConsultaComboCentroDespesa;

	public List<SelectItem> getTipoConsultaComboCentroDespesa() {
		if (tipoConsultaComboCentroDespesa == null) {
			tipoConsultaComboCentroDespesa = new ArrayList<SelectItem>(0);
			tipoConsultaComboCentroDespesa.add(new SelectItem("descricao", "Descrição"));
			tipoConsultaComboCentroDespesa
					.add(new SelectItem("identificadorCentroDespesa", "Identificador Centro Despesa"));
		}
		return tipoConsultaComboCentroDespesa;
	}

	public List<SelectItem> tipoConsultaComboFornecedor;

	public List<SelectItem> getTipoConsultaComboFornecedor() {
		if (tipoConsultaComboFornecedor == null) {
			tipoConsultaComboFornecedor = new ArrayList<SelectItem>(0);
			tipoConsultaComboFornecedor.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboFornecedor.add(new SelectItem("razaoSocial", "Razão Social"));
			tipoConsultaComboFornecedor.add(new SelectItem("CNPJ", "CNPJ"));
			tipoConsultaComboFornecedor.add(new SelectItem("CPF", "CPF"));
		}
		return tipoConsultaComboFornecedor;
	}

	public List<SelectItem> tipoConsultaComboFuncionario;

	public List<SelectItem> getTipoConsultaComboFuncionario() {
		if (tipoConsultaComboFuncionario == null) {
			tipoConsultaComboFuncionario = new ArrayList<SelectItem>(0);
			tipoConsultaComboFuncionario.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboFuncionario.add(new SelectItem("CPF", "CPF"));
		}
		return tipoConsultaComboFuncionario;
	}

	public List<SelectItem> tipoConsultaComboBanco;

	public List<SelectItem> getTipoConsultaComboBanco() {
		if (tipoConsultaComboBanco == null) {
			tipoConsultaComboBanco = new ArrayList<SelectItem>(0);
			tipoConsultaComboBanco.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboBanco.add(new SelectItem("nrBanco", "Número Banco"));
		}
		return tipoConsultaComboBanco;
	}

	public List<SelectItem> tipoConsultaComboAluno;

	public List<SelectItem> getTipoConsultaComboAluno() {
		if (tipoConsultaComboAluno == null) {
			tipoConsultaComboAluno = new ArrayList<SelectItem>(0);
			tipoConsultaComboAluno.add(new SelectItem("nomePessoa", "Aluno/Candidato"));
			tipoConsultaComboAluno.add(new SelectItem("matricula", "Matrícula"));
		}
		return tipoConsultaComboAluno;
	}

	public List<SelectItem> tipoConsultaComboTipoRelatorio;

	public List<SelectItem> getTipoConsultaComboTipoRelatorio() {
		if (tipoConsultaComboTipoRelatorio == null) {
			tipoConsultaComboTipoRelatorio = new ArrayList<SelectItem>(0);
			tipoConsultaComboTipoRelatorio.add(new SelectItem("AN", "Analítico"));
			tipoConsultaComboTipoRelatorio.add(new SelectItem("SI", "Sintético"));
		}
		return tipoConsultaComboTipoRelatorio;
	}

	public List<SelectItem> tipoConsultaComboPlanoConta;

	public List<SelectItem> getTipoConsultaComboPlanoConta() {
		if (tipoConsultaComboPlanoConta == null) {
			tipoConsultaComboPlanoConta = new ArrayList<SelectItem>(0);
			tipoConsultaComboPlanoConta.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboPlanoConta.add(new SelectItem("identificadorPlanoConta", "Identificador Plano Conta"));
		}
		return tipoConsultaComboPlanoConta;
	}

	public void selecionarFornecedor() {
		FornecedorVO obj = (FornecedorVO) context().getExternalContext().getRequestMap().get("fornecedorItem");
		this.setFornecedorNome(obj.getNome());
		if (obj.getTipoEmpresa().equals("FI")) {
			this.setFornecedorCpfCnpj(obj.getCPF());
		} else {
			this.setFornecedorCpfCnpj(obj.getCNPJ());
		}
		this.setFornecedor(obj.getCodigo());
		setCampoConsultaFornecedor("");
		setValorConsultaFornecedor("");
		setListaConsultaFornecedor(new ArrayList(0));

	}

	public void scrollerListener(DataScrollEvent DataScrollEvent) throws Exception {
		getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
		getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
		consultar();
	}

	public void scrollerListenerAluno(DataScrollEvent dataScrollEvent) {
		getDataModeloAluno().setPaginaAtual(dataScrollEvent.getPage());
		getDataModeloAluno().setPage(dataScrollEvent.getPage());
		consultarAluno();
	}

	public void selecionarAluno() {
		MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("aluno");
		setAlunoNome(obj.getAluno().getNome());

		setDataModeloAluno(new DataModelo());
		getDataModeloAluno().setLimitePorPagina(10);
	}

	public void selecionarFuncionario() {
		FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItem");
		this.setFuncionario(obj.getCodigo());
		this.setFuncionarioNome(obj.getPessoa().getNome());
		setCampoConsultaFuncionario("");
		setValorConsultaFuncionario("");
		setListaConsultaFuncionario(new ArrayList<>(0));
	}

	public void selecionarBanco() {
		BancoVO obj = (BancoVO) context().getExternalContext().getRequestMap().get("bancoItem");
		this.setBanco(obj.getCodigo());
		this.setBancoNome(obj.getNome());
		setCampoConsultaBanco("");
		setValorConsultaBanco("");
		setListaConsultaBanco(new ArrayList<>(0));
	}

	public void inicializarListasSelectItemTodosComboBox() {
		montarListaSelectItemOrdenacao();
		montarListaSelectItemUnidadeEnsino();

		setFiltroFornecedor(0);
		setFiltroFuncionario(0);
		setFiltroBanco(0);
		validarFiltroFornecedor();
		validarFiltroFuncionario();
		validarFiltroBanco();
	}

	public void montarListaSelectItemOrdenacao() {
		Vector opcoes = getFacadeFactory().getPagamentoRelFacade().getOrdenacoesRelatorio();
		Enumeration i = opcoes.elements();
		List<SelectItem> objs = new ArrayList<SelectItem>(0);
		int contador = 0;
		while (i.hasMoreElements()) {
			String option = (String) i.nextElement();
			objs.add(new SelectItem(new Integer(contador), option));
			contador++;
		}
		setListaSelectItemOrdenacoesRelatorio(objs);
	}

	public void montarListaSelectItemUnidadeEnsino() {
		try {
			montarListaSelectItemUnidadeEnsino("");
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());
			;
		}

	}

	public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
		List<UnidadeEnsinoVO> resultadoConsulta = null;
		Iterator i = null;
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

	public List getListaSelectItemTipoFormaPagamento() {
		List objs = new ArrayList(0);
		objs = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoFormaPagamento.class);
		objs.remove(0);
		return objs;

	}

	public List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
		List<UnidadeEnsinoVO> lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm,
				super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,
				getUsuarioLogado());
		return lista;
	}

	public List<SelectItem> listaContaPagarFiltrosEnum;

	public List<SelectItem> getListaContaPagarFiltrosEnum() {
		if (listaContaPagarFiltrosEnum == null) {
			listaContaPagarFiltrosEnum = new ArrayList<>(0);
			listaContaPagarFiltrosEnum.add(
					new SelectItem(ContaPagarFiltrosEnum.TODOS.name(), ContaPagarFiltrosEnum.TODOS.getDescricao()));
			listaContaPagarFiltrosEnum.add(
					new SelectItem(ContaPagarFiltrosEnum.NENHUM.name(), ContaPagarFiltrosEnum.NENHUM.getDescricao()));
			listaContaPagarFiltrosEnum.add(
					new SelectItem(ContaPagarFiltrosEnum.FILTRAR.name(), ContaPagarFiltrosEnum.FILTRAR.getDescricao()));
		}
		return listaContaPagarFiltrosEnum;
	}

	public void validarFiltroFornecedor() {
		if (getFiltroFornecedor().intValue() == 2) {
			setApresentarFiltroFornecedor(true);
		} else {
			setApresentarFiltroFornecedor(false);
		}
	}

	public void validarFiltroFuncionario() {
		if (getFiltroFuncionario().intValue() == 2) {
			setApresentarFiltroFuncionario(true);
		} else {
			setApresentarFiltroFuncionario(false);
		}
	}

	public void validarFiltroBanco() {
		if (getFiltroBanco().intValue() == 2) {
			setApresentarFiltroBanco(true);
		} else {
			setApresentarFiltroBanco(false);
		}
	}

	public void validarFiltroParceiro() {
		if (getFiltroParceiro().intValue() == 2) {
			setApresentarFiltroParceiro(true);
		} else {
			setApresentarFiltroParceiro(false);
		}
	}

	public void validarFiltroResponsavelFinanceiro() {
		if (getFiltroResponsavelFinanceiro().intValue() == 2) {
			setApresentarFiltroResponsavelFinanceiro(true);
		} else {
			setApresentarFiltroResponsavelFinanceiro(false);
		}
	}

	public void validarFiltroAluno() {
		if (getFiltroAluno().intValue() == 2) {
			setApresentarFiltroAluno(true);
		} else {
			setApresentarFiltroAluno(false);
		}
	}

	public void validarFiltroTipo() {
		if (getFiltroTipo().intValue() == 2) {
			setApresentarFiltroTipo(true);
		} else {
			setApresentarFiltroTipo(false);
		}
	}

	public String getCampoConsultaFornecedor() {
		return campoConsultaFornecedor;
	}

	public void setCampoConsultaFornecedor(String campoConsultaFornecedor) {
		this.campoConsultaFornecedor = campoConsultaFornecedor;
	}

	public List getListaConsultaFornecedor() {
		return listaConsultaFornecedor;
	}

	public void setListaConsultaFornecedor(List listaConsultaFornecedor) {
		this.listaConsultaFornecedor = listaConsultaFornecedor;
	}

	public String getValorConsultaFornecedor() {
		return valorConsultaFornecedor;
	}

	public void setValorConsultaFornecedor(String valorConsultaFornecedor) {
		this.valorConsultaFornecedor = valorConsultaFornecedor;
	}

	/**
	 * @return the listaConsultaBanco
	 */
	public List getListaConsultaBanco() {
		return listaConsultaBanco;
	}

	/**
	 * @param listaConsultaBanco the listaConsultaBanco to set
	 */
	public void setListaConsultaBanco(List listaConsultaBanco) {
		this.listaConsultaBanco = listaConsultaBanco;
	}

	/**
	 * @return the listaConsultaFuncionario
	 */
	public List getListaConsultaFuncionario() {
		return listaConsultaFuncionario;
	}

	/**
	 * @param listaConsultaFuncionario the listaConsultaFuncionario to set
	 */
	public void setListaConsultaFuncionario(List listaConsultaFuncionario) {
		this.listaConsultaFuncionario = listaConsultaFuncionario;
	}

	/**
	 * @return the valorConsultaBanco
	 */
	public String getValorConsultaBanco() {
		return valorConsultaBanco;
	}

	/**
	 * @param valorConsultaBanco the valorConsultaBanco to set
	 */
	public void setValorConsultaBanco(String valorConsultaBanco) {
		this.valorConsultaBanco = valorConsultaBanco;
	}

	/**
	 * @return the valorConsultaFuncionario
	 */
	public String getValorConsultaFuncionario() {
		return valorConsultaFuncionario;
	}

	/**
	 * @param valorConsultaFuncionario the valorConsultaFuncionario to set
	 */
	public void setValorConsultaFuncionario(String valorConsultaFuncionario) {
		this.valorConsultaFuncionario = valorConsultaFuncionario;
	}

	/**
	 * @return the campoConsultaBanco
	 */
	public String getCampoConsultaBanco() {
		return campoConsultaBanco;
	}

	/**
	 * @param campoConsultaBanco the campoConsultaBanco to set
	 */
	public void setCampoConsultaBanco(String campoConsultaBanco) {
		this.campoConsultaBanco = campoConsultaBanco;
	}

	/**
	 * @return the campoConsultaFuncionario
	 */
	public String getCampoConsultaFuncionario() {
		return campoConsultaFuncionario;
	}

	/**
	 * @param campoConsultaFuncionario the campoConsultaFuncionario to set
	 */
	public void setCampoConsultaFuncionario(String campoConsultaFuncionario) {
		this.campoConsultaFuncionario = campoConsultaFuncionario;
	}

	/**
	 * @return the listaSelectItemUnidadeEnsino
	 */
	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList<>();
		}
		return listaSelectItemUnidadeEnsino;
	}

	/**
	 * @param listaSelectItemUnidadeEnsino the listaSelectItemUnidadeEnsino to set
	 */
	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public Integer getBanco() {
		return banco;
	}

	public void setBanco(Integer banco) {
		this.banco = banco;
	}

	public String getBancoNome() {
		return bancoNome;
	}

	public void setBancoNome(String bancoNome) {
		this.bancoNome = bancoNome;
	}

	public Date getDataFim() {
		if (dataFim == null) {
			dataFim = new Date();
		}
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public Date getDataInicio() {
		if (dataInicio == null) {
			dataInicio = Uteis.getNewDateComMesesAMenos(1);
		}
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Integer getFiltroBanco() {
		if (filtroBanco == null) {
			filtroBanco = 0;
		}
		return filtroBanco;
	}

	public void setFiltroBanco(Integer filtroBanco) {
		this.filtroBanco = filtroBanco;
	}

	public Integer getFiltroFornecedor() {
		if (filtroFornecedor == null) {
			filtroFornecedor = 0;
		}
		return filtroFornecedor;
	}

	public void setFiltroFornecedor(Integer filtroFornecedor) {
		this.filtroFornecedor = filtroFornecedor;
	}

	public Integer getFiltroFuncionario() {
		if (filtroFuncionario == null) {
			filtroFuncionario = 0;
		}
		return filtroFuncionario;
	}

	public void setFiltroFuncionario(Integer filtroFuncionario) {
		this.filtroFuncionario = filtroFuncionario;
	}

	public Integer getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Integer fornecedor) {
		this.fornecedor = fornecedor;
	}

	public String getFornecedorCpfCnpj() {
		return fornecedorCpfCnpj;
	}

	public void setFornecedorCpfCnpj(String fornecedorCpfCnpj) {
		this.fornecedorCpfCnpj = fornecedorCpfCnpj;
	}

	public String getFornecedorNome() {
		return fornecedorNome;
	}

	public void setFornecedorNome(String fornecedorNome) {
		this.fornecedorNome = fornecedorNome;
	}

	public Integer getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Integer funcionario) {
		this.funcionario = funcionario;
	}

	public String getFuncionarioNome() {
		return funcionarioNome;
	}

	public void setFuncionarioNome(String funcionarioNome) {
		this.funcionarioNome = funcionarioNome;
	}

	public UnidadeEnsinoVO getUnidadeEnsino() {
		if (unidadeEnsino == null) {
			unidadeEnsino = new UnidadeEnsinoVO();
		}
		return unidadeEnsino;
	}

	public void setUnidadeEnsinoCodigo(UnidadeEnsinoVO unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}

	public Boolean getApresentarFiltroFornecedor() {
		return apresentarFiltroFornecedor;
	}

	public void setApresentarFiltroFornecedor(Boolean apresentarFiltroFornecedor) {
		this.apresentarFiltroFornecedor = apresentarFiltroFornecedor;
	}

	public Boolean getApresentarFiltroFuncionario() {
		return apresentarFiltroFuncionario;
	}

	public void setApresentarFiltroFuncionario(Boolean apresentarFiltroFuncionario) {
		this.apresentarFiltroFuncionario = apresentarFiltroFuncionario;
	}

	public Boolean getApresentarFiltroBanco() {
		return apresentarFiltroBanco;
	}

	public void setApresentarFiltroBanco(Boolean apresentarFiltroBanco) {
		this.apresentarFiltroBanco = apresentarFiltroBanco;
	}

	public List<SelectItem> getListaFiltroFornecedor() {
		return listaFiltroFornecedor;
	}

	public void setListaFiltroFornecedor(List<SelectItem> listaFiltroFornecedor) {
		this.listaFiltroFornecedor = listaFiltroFornecedor;
	}

	public List<SelectItem> getListaFiltroFuncionario() {
		return listaFiltroFuncionario;
	}

	public void setListaFiltroFuncionario(List<SelectItem> listaFiltroFuncionario) {
		this.listaFiltroFuncionario = listaFiltroFuncionario;
	}

	public List<SelectItem> getListaFiltroBanco() {
		return listaFiltroBanco;
	}

	public void setListaFiltroBanco(List<SelectItem> listaFiltroBanco) {
		this.listaFiltroBanco = listaFiltroBanco;
	}

	public String getLayout() {
		if (layout == null) {
			layout = "";
		}
		return layout;
	}

	public void setLayout(String layout) {
		this.layout = layout;
	}

	public Integer getFiltroAluno() {
		if (filtroAluno == null) {
			filtroAluno = 0;
		}
		return filtroAluno;
	}

	public void setFiltroAluno(Integer filtroAluno) {
		this.filtroAluno = filtroAluno;
	}

	public Boolean getApresentarFiltroAluno() {
		if (apresentarFiltroAluno == null) {
			apresentarFiltroAluno = Boolean.FALSE;
		}
		return apresentarFiltroAluno;
	}

	public void setApresentarFiltroAluno(Boolean apresentarFiltroAluno) {
		this.apresentarFiltroAluno = apresentarFiltroAluno;
	}

	public List<SelectItem> getListaFiltroAluno() {
		if (listaFiltroAluno == null) {
			listaFiltroAluno = new ArrayList<SelectItem>();
		}
		return listaFiltroAluno;
	}

	public void setListaFiltroAluno(List<SelectItem> listaFiltroAluno) {
		this.listaFiltroAluno = listaFiltroAluno;
	}

	public String getAlunoNome() {
		if (alunoNome == null) {
			alunoNome = "";
		}
		return alunoNome;
	}

	public void setAlunoNome(String alunoNome) {
		this.alunoNome = alunoNome;
	}

	public Integer getAluno() {
		if (aluno == null) {
			aluno = 0;
		}
		return aluno;
	}

	public void setAluno(Integer aluno) {
		this.aluno = aluno;
	}

	public String getOrdenar() {
		if (ordenar == null) {
			ordenar = "";
		}
		return ordenar;
	}

	public void setOrdenar(String ordenar) {
		this.ordenar = ordenar;
	}

	public Integer getTipo() {
		if (tipo == null) {
			tipo = 0;
		}
		return tipo;
	}

	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}

	public Integer getFiltroTipo() {
		if (filtroTipo == null) {
			filtroTipo = 0;
		}
		return filtroTipo;
	}

	public void setFiltroTipo(Integer filtroTipo) {
		this.filtroTipo = filtroTipo;
	}

	public Boolean getApresentarFiltroTipo() {
		if (apresentarFiltroTipo == null) {
			apresentarFiltroTipo = Boolean.FALSE;
		}
		return apresentarFiltroTipo;
	}

	public void setApresentarFiltroTipo(Boolean apresentarFiltroTipo) {
		this.apresentarFiltroTipo = apresentarFiltroTipo;
	}

	public List<SelectItem> getListaFiltroTipo() {
		if (listaFiltroTipo == null) {
			setListaFiltroTipo((List<SelectItem>) new ArrayList(0));
		}
		return listaFiltroTipo;
	}

	public void setListaFiltroTipo(List<SelectItem> listaFiltroTipo) {
		this.listaFiltroTipo = listaFiltroTipo;
	}

	public String getFormaPagamento() {
		if (formaPagamento == null) {
			formaPagamento = "";
		}
		return formaPagamento;
	}

	public void setFormaPagamento(String formaPagamento) {
		this.formaPagamento = formaPagamento;
	}

	public String getFormaPagamento_Apresentar() {
		if (formaPagamento.equals("CA")) {
			return "Cartão de Crédito";
		}
		if (formaPagamento.equals("DI")) {
			return "Dinheiro";
		}
		if (formaPagamento.equals("CH")) {
			return "Cheque";
		}
		if (formaPagamento.equals("BO")) {
			return "Boleto Bancário";
		}
		if (formaPagamento.equals("DE")) {
			return "Débito em Conta";
		}
		return formaPagamento;
	}

	public String getTipoRelatorio() {
		if (tipoRelatorio == null) {
			tipoRelatorio = "";
		}
		return tipoRelatorio;
	}

	public void setTipoRelatorio(String tipoRelatorio) {
		this.tipoRelatorio = tipoRelatorio;
	}

	public Boolean getApresentarComboTipoLayout() {
		return getTipoRelatorio().equals("AN");
	}

	public String getResponsavelFinanceiroNome() {
		return responsavelFinanceiroNome;
	}

	public void setResponsavelFinanceiroNome(String responsavelFinanceiroNome) {
		this.responsavelFinanceiroNome = responsavelFinanceiroNome;
	}

	public Integer getResponsavelFinanceiro() {
		return responsavelFinanceiro;
	}

	public void setResponsavelFinanceiro(Integer responsavelFinanceiro) {
		this.responsavelFinanceiro = responsavelFinanceiro;
	}

	public String getParceiroNome() {
		return parceiroNome;
	}

	public void setParceiroNome(String parceiroNome) {
		this.parceiroNome = parceiroNome;
	}

	public Integer getParceiro() {
		return parceiro;
	}

	public void setParceiro(Integer parceiro) {
		this.parceiro = parceiro;
	}

	public Integer getFiltroResponsavelFinanceiro() {
		return filtroResponsavelFinanceiro;
	}

	public void setFiltroResponsavelFinanceiro(Integer filtroResponsavelFinanceiro) {
		this.filtroResponsavelFinanceiro = filtroResponsavelFinanceiro;
	}

	public Integer getFiltroParceiro() {
		if (filtroParceiro == null) {
			filtroParceiro = 0;
		}
		return filtroParceiro;
	}

	public void setFiltroParceiro(Integer filtroParceiro) {
		this.filtroParceiro = filtroParceiro;
	}

	public Boolean getApresentarFiltroResponsavelFinanceiro() {
		return apresentarFiltroResponsavelFinanceiro;
	}

	public void setApresentarFiltroResponsavelFinanceiro(Boolean apresentarFiltroResponsavelFinanceiro) {
		this.apresentarFiltroResponsavelFinanceiro = apresentarFiltroResponsavelFinanceiro;
	}

	public Boolean getApresentarFiltroParceiro() {
		return apresentarFiltroParceiro;
	}

	public void setApresentarFiltroParceiro(Boolean apresentarFiltroParceiro) {
		this.apresentarFiltroParceiro = apresentarFiltroParceiro;
	}

	public List<SelectItem> getListaFiltroResponsavelFinanceiro() {
		if (listaFiltroResponsavelFinanceiro == null) {
			setFiltroResponsavelFinanceiro(0);
			listaFiltroResponsavelFinanceiro = new ArrayList<SelectItem>(0);
			listaFiltroResponsavelFinanceiro.add(new SelectItem(0, "TODOS / "));
			listaFiltroResponsavelFinanceiro.add(new SelectItem(new Integer(1), "NENHUM / "));
			listaFiltroResponsavelFinanceiro.add(new SelectItem(new Integer(2), "FILTRAR"));
		}
		return listaFiltroResponsavelFinanceiro;
	}

	public List<SelectItem> getListaFiltroParceiro() {
		if (listaFiltroParceiro == null) {
			setFiltroParceiro(0);
			listaFiltroParceiro = new ArrayList<SelectItem>(0);
			listaFiltroParceiro.add(new SelectItem(0, "TODOS / "));
			listaFiltroParceiro.add(new SelectItem(new Integer(1), "NENHUM / "));
			listaFiltroParceiro.add(new SelectItem(new Integer(2), "FILTRAR"));
		}
		return listaFiltroParceiro;
	}

	private List listaConsultaParceiro;
	private String valorConsultaParceiro;
	private String campoConsultaParceiro;

	public List getListaConsultaParceiro() {
		if (listaConsultaParceiro == null) {
			listaConsultaParceiro = new ArrayList(0);
		}
		return listaConsultaParceiro;
	}

	public void setListaConsultaParceiro(List listaConsultaParceiro) {
		this.listaConsultaParceiro = listaConsultaParceiro;
	}

	public String getValorConsultaParceiro() {
		if (valorConsultaParceiro == null) {
			valorConsultaParceiro = "";
		}
		return valorConsultaParceiro;
	}

	public void setValorConsultaParceiro(String valorConsultaParceiro) {
		this.valorConsultaParceiro = valorConsultaParceiro;
	}

	public String getCampoConsultaParceiro() {
		if (campoConsultaParceiro == null) {
			campoConsultaParceiro = "";
		}
		return campoConsultaParceiro;
	}

	public void setCampoConsultaParceiro(String campoConsultaParceiro) {
		this.campoConsultaParceiro = campoConsultaParceiro;
	}

	protected List<PessoaVO> listaConsultaResponsavelFinanceiro;
	protected String valorConsultaResponsavelFinanceiro;
	protected String campoConsultaResponsavelFinanceiro;

	public void consultarResponsavelFinanceiro() {
		try {

			if (getValorConsultaResponsavelFinanceiro().trim().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			getFacadeFactory().getPessoaFacade().setIdEntidade("ResponsavelFinanceiro");
			getListaConsultaResponsavelFinanceiro().clear();
			if (getCampoConsultaResponsavelFinanceiro().equals("nome")) {
				setListaConsultaResponsavelFinanceiro(getFacadeFactory().getPessoaFacade()
						.consultaRapidaPorNomeResponsavelFinanceiro(getValorConsultaResponsavelFinanceiro(),
								this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado()));
			}
			if (getCampoConsultaResponsavelFinanceiro().equals("nomeAluno")) {
				setListaConsultaResponsavelFinanceiro(getFacadeFactory().getPessoaFacade()
						.consultaRapidaPorNomeAlunoResponsavelFinanceiro(getValorConsultaResponsavelFinanceiro(),
								this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado()));
			}
			if (getCampoConsultaResponsavelFinanceiro().equals("CPF")) {
				setListaConsultaResponsavelFinanceiro(getFacadeFactory().getPessoaFacade()
						.consultaRapidaPorCpfResponsavelFinanceiro(getValorConsultaResponsavelFinanceiro(),
								this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado()));
			}

			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			setListaConsultaResponsavelFinanceiro(new ArrayList<PessoaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	private List<SelectItem> tipoConsultaComboResponsavelFinanceiro;

	public List<SelectItem> getTipoConsultaComboResponsavelFinanceiro() {
		if (tipoConsultaComboResponsavelFinanceiro == null) {
			tipoConsultaComboResponsavelFinanceiro = new ArrayList<SelectItem>(0);
			tipoConsultaComboResponsavelFinanceiro.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboResponsavelFinanceiro.add(new SelectItem("nomeAluno", "Aluno"));
			tipoConsultaComboResponsavelFinanceiro.add(new SelectItem("CPF", "CPF"));
		}
		return tipoConsultaComboResponsavelFinanceiro;
	}

	public void selecionarResponsavelFinanceiro() {
		try {
			PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("responsavelFinanceiro");
			setResponsavelFinanceiroNome(obj.getNome());
			setResponsavelFinanceiro(obj.getCodigo());
			getListaConsultaResponsavelFinanceiro().clear();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<PessoaVO> getListaConsultaResponsavelFinanceiro() {
		if (listaConsultaResponsavelFinanceiro == null) {
			listaConsultaResponsavelFinanceiro = new ArrayList<PessoaVO>(0);
		}
		return listaConsultaResponsavelFinanceiro;
	}

	public void setListaConsultaResponsavelFinanceiro(List<PessoaVO> listaConsultaResponsavelFinanceiro) {
		this.listaConsultaResponsavelFinanceiro = listaConsultaResponsavelFinanceiro;
	}

	public String getValorConsultaResponsavelFinanceiro() {
		if (valorConsultaResponsavelFinanceiro == null) {
			valorConsultaResponsavelFinanceiro = "";
		}
		return valorConsultaResponsavelFinanceiro;
	}

	public void setValorConsultaResponsavelFinanceiro(String valorConsultaResponsavelFinanceiro) {
		this.valorConsultaResponsavelFinanceiro = valorConsultaResponsavelFinanceiro;
	}

	public String getCampoConsultaResponsavelFinanceiro() {
		if (campoConsultaResponsavelFinanceiro == null) {
			campoConsultaResponsavelFinanceiro = "";
		}
		return campoConsultaResponsavelFinanceiro;
	}

	public void setCampoConsultaResponsavelFinanceiro(String campoConsultaResponsavelFinanceiro) {
		this.campoConsultaResponsavelFinanceiro = campoConsultaResponsavelFinanceiro;
	}

	public void consultarParceiro() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaParceiro().equals("nome")) {
				objs = getFacadeFactory().getParceiroFacade().consultarPorNome(getValorConsultaParceiro(), true,
						Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaParceiro().equals("razaoSocial")) {
				objs = getFacadeFactory().getParceiroFacade().consultarPorRazaoSocial(getValorConsultaParceiro(), true,
						Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaParceiro(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> getTipoConsultaParceiro() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("razaoSocial", "Razão social"));
		return itens;
	}

	public List<SelectItem> getTipoContaCombo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("todas", "Todas"));
		itens.add(new SelectItem("caixa", "Conta Caixa"));
		itens.add(new SelectItem("corrente", "Conta Corrente"));
		return itens;
	}

	public void selecionarParceiro() {
		ParceiroVO obj = (ParceiroVO) context().getExternalContext().getRequestMap().get("parceiroItem");
		setParceiroNome(obj.getNome());
		setParceiro(obj.getCodigo());
		listaConsultaParceiro.clear();
		this.setValorConsultaParceiro("");
		this.setCampoConsultaParceiro("");
	}

	public void limparDadosParceiro() {
		setParceiroNome("");
		setParceiro(0);
	}

	public void limparDadosResponsavelFinanceiro() {
		setResponsavelFinanceiroNome("");
		setResponsavelFinanceiro(0);
	}

	public String getTipoConta() {
		if (tipoConta == null) {
			tipoConta = "todas";
		}
		return tipoConta;
	}

	public void setTipoConta(String tipoConta) {
		this.tipoConta = tipoConta;
	}

	/**
	 * @return the filtrarDataFatoGerador
	 */
	public Boolean getFiltrarDataFatoGerador() {
		if (filtrarDataFatoGerador == null) {
			filtrarDataFatoGerador = Boolean.FALSE;
		}
		return filtrarDataFatoGerador;
	}

	/**
	 * @param filtrarDataFatoGerador the filtrarDataFatoGerador to set
	 */
	public void setFiltrarDataFatoGerador(Boolean filtrarDataFatoGerador) {
		this.filtrarDataFatoGerador = filtrarDataFatoGerador;
	}

	public void validarTipoContaCorrente() throws Exception {
		if (getTipoConta().equals("caixa")) {
			setApresentarCampoContaCaixa(Boolean.TRUE);
			montarListaSelectItemContaCaixa();
			setApresentarCampoContaCorrente(Boolean.FALSE);
			getListaSelectItemContaCorrente().clear();
		} else if (getTipoConta().equals("corrente")) {
			setApresentarCampoContaCorrente(Boolean.TRUE);
			montarListaSelectItemContaCorrente();
			setApresentarCampoContaCaixa(Boolean.FALSE);
			getListaSelectItemContaCaixa().clear();
		} else {
			setApresentarCampoContaCorrente(Boolean.FALSE);
			setApresentarCampoContaCaixa(Boolean.FALSE);
			getListaSelectItemContaCaixa().clear();
			getListaSelectItemContaCorrente().clear();
		}
	}

	public List consultarContaCorrente() throws Exception {
		List lista = getFacadeFactory().getContaCorrenteFacade().consultarContaCorrente(
				getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		return lista;
	}

	public List consultarContaCaixa() throws Exception {
		List lista = getFacadeFactory().getContaCorrenteFacade().consultarContaCaixa(
				getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		return lista;
	}

	public void montarListaSelectItemContaCorrente() throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarContaCorrente();
			i = resultadoConsulta.iterator();
			List<SelectItem> objs = new ArrayList<SelectItem>(0);
			objs.add(new SelectItem(0, "TODAS"));
			while (i.hasNext()) {
				ContaCorrenteVO obj = (ContaCorrenteVO) i.next();
				if (Uteis.isAtributoPreenchido(obj.getNomeApresentacaoSistema())) {
					objs.add(new SelectItem(obj.getCodigo(), obj.getNomeApresentacaoSistema()));
				} else {
					objs.add(new SelectItem(obj.getCodigo(), obj.getNumero().toString()));
				}
			}
			setListaSelectItemContaCorrente(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	public void montarListaSelectItemContaCaixa() throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarContaCaixa();
			i = resultadoConsulta.iterator();
			List<SelectItem> objs = new ArrayList<SelectItem>(0);
			objs.add(new SelectItem(0, "TODAS"));
			while (i.hasNext()) {
				ContaCorrenteVO obj = (ContaCorrenteVO) i.next();
				if (Uteis.isAtributoPreenchido(obj.getNomeApresentacaoSistema())) {
					objs.add(new SelectItem(obj.getCodigo(), obj.getNomeApresentacaoSistema()));
				} else {
					objs.add(new SelectItem(obj.getCodigo(), obj.getNumero().toString()));
				}
			}
			setListaSelectItemContaCaixa(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	public Boolean getApresentarCampoContaCorrente() {
		if (apresentarCampoContaCorrente == null) {
			apresentarCampoContaCorrente = Boolean.FALSE;
		}
		return apresentarCampoContaCorrente;
	}

	public void setApresentarCampoContaCorrente(Boolean apresentarCampoContaCorrente) {
		this.apresentarCampoContaCorrente = apresentarCampoContaCorrente;
	}

	public Boolean getApresentarCampoContaCaixa() {
		if (apresentarCampoContaCaixa == null) {
			apresentarCampoContaCaixa = Boolean.FALSE;
		}
		return apresentarCampoContaCaixa;
	}

	public void setApresentarCampoContaCaixa(Boolean apresentarCampoContaCaixa) {
		this.apresentarCampoContaCaixa = apresentarCampoContaCaixa;
	}

	public List<SelectItem> getListaSelectItemContaCorrente() {
		if (listaSelectItemContaCorrente == null) {
			listaSelectItemContaCorrente = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemContaCorrente;
	}

	public void setListaSelectItemContaCorrente(List listaSelectItemContaCorrente) {
		this.listaSelectItemContaCorrente = listaSelectItemContaCorrente;
	}

	public List<SelectItem> getListaSelectItemContaCaixa() {
		if (listaSelectItemContaCaixa == null) {
			listaSelectItemContaCaixa = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemContaCaixa;
	}

	public void setListaSelectItemContaCaixa(List listaSelectItemContaCaixa) {
		this.listaSelectItemContaCaixa = listaSelectItemContaCaixa;
	}

	public Integer getCodigoContaCorrenteCaixa() {
		if (codigoContaCorrenteCaixa == null) {
			codigoContaCorrenteCaixa = 0;
		}
		return codigoContaCorrenteCaixa;
	}

	public void setCodigoContaCorrenteCaixa(Integer codigoContaCorrenteCaixa) {
		this.codigoContaCorrenteCaixa = codigoContaCorrenteCaixa;
	}

	public List<SelectItem> getListaSelectItemOperadoraCartao() {
		if (listaSelectItemOperadoraCartao == null) {
			listaSelectItemOperadoraCartao = new ArrayList<SelectItem>(0);
			try {
				List<OperadoraCartaoVO> operadoraCartaoVOs = getFacadeFactory().getOperadoraCartaoFacade()
						.consultarPorTipo(TipoCartaoOperadoraCartaoEnum.CARTAO_CREDITO.name(), false,
								Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
				listaSelectItemOperadoraCartao = UtilSelectItem.getListaSelectItem(operadoraCartaoVOs, "codigo",
						"nome");
			} catch (Exception e) {
			}
		}
		return listaSelectItemOperadoraCartao;
	}

	public Boolean getApresentarFiltroOperadoraCartao() {
		if (apresentarFiltroOperadoraCartao == null) {
			apresentarFiltroOperadoraCartao = false;
		}
		return apresentarFiltroOperadoraCartao;
	}

	public void setApresentarFiltroOperadoraCartao(Boolean apresentarFiltroOperadoraCartao) {
		this.apresentarFiltroOperadoraCartao = apresentarFiltroOperadoraCartao;
	}

	public void setListaSelectItemOperadoraCartao(List<SelectItem> listaSelectItemOperadoraCartao) {
		this.listaSelectItemOperadoraCartao = listaSelectItemOperadoraCartao;
	}

	public Integer getFiltroOperadoraCartao() {
		if (filtroOperadoraCartao == null) {
			filtroOperadoraCartao = 0;
		}
		return filtroOperadoraCartao;
	}

	public void setFiltroOperadoraCartao(Integer filtroOperadoraCartao) {
		this.filtroOperadoraCartao = filtroOperadoraCartao;
	}

	public Integer getOperadoraCartao() {
		if (operadoraCartao == null) {
			operadoraCartao = 0;
		}
		return operadoraCartao;
	}

	public void setOperadoraCartao(Integer operadoraCartao) {
		this.operadoraCartao = operadoraCartao;
	}

	public void validarFiltroOperadoraCartao() {
		setApresentarFiltroOperadoraCartao(getFiltroOperadoraCartao().equals(2));
	}

	public DataModelo getDataModeloAluno() {
		if (dataModeloAluno == null) {
			dataModeloAluno = new DataModelo();
			dataModeloAluno.setLimitePorPagina(10);
		}
		return dataModeloAluno;
	}

	public void setDataModeloAluno(DataModelo dataModeloAluno) {
		this.dataModeloAluno = dataModeloAluno;
	}
}
