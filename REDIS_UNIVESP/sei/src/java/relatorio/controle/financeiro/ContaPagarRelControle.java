package relatorio.controle.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.faces.model.SelectItem;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.financeiro.BancoVO;
import negocio.comuns.financeiro.CategoriaDespesaVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.OperadoraCartaoVO;
import negocio.comuns.financeiro.ParceiroVO;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.TipoCartaoOperadoraCartaoEnum;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.financeiro.ContaPagarRelVO;
import relatorio.negocio.jdbc.financeiro.enumeradores.ContaPagarFiltrosEnum;
import controle.arquitetura.DataModelo;
import controle.arquitetura.SelectItemOrdemValor;

@Controller("ContaPagarRelControle")
@Scope("viewScope")
@Lazy
public class ContaPagarRelControle extends SuperControleRelatorio {
	
	private static final long serialVersionUID = -7886119780224670317L;

	protected List listaConsultaFornecedor;
	protected String valorConsultaFornecedor;
	protected String campoConsultaFornecedor;
	protected List listaConsultaBanco;
	protected List listaConsultaFuncionario;
	protected String valorConsultaBanco;
	protected String valorConsultaFuncionario;
	protected String campoConsultaBanco;
	protected String campoConsultaFuncionario;
	protected List listaConsultaCentroDespesa;
	private List<SelectItem> listaSelectItemDepartamento;
	protected String valorConsultaCentroDespesa;
	protected String campoConsultaCentroDespesa;
	private List listaSelectItemUnidadeEnsino;
	protected List listaSelectItemTurma;
	private String nomeRelatorio;
	private TurmaVO turmaVO;
	private String campoConsultaTurma;
	private String valorConsultaTurma;
	private List<TurmaVO> listaConsultaTurma;
	private ContaPagarRelVO contaPagarRelVO;
	private Boolean apresentarFiltroContaCorrente;
	private Boolean apresentarFiltroFornecedor;
	private Boolean apresentarFiltroFuncionario;
	private Boolean apresentarFiltroBanco;
	private Boolean apresentarFiltroAluno;
	private Boolean apresentarFiltroResponsavelFinanceiro;
	private Boolean apresentarFiltroParceiro;
	private Boolean apresentarFiltroDepartamento;
	private String filtroAPagar;
	private String filtroPago;
	private String filtroNegociada;
	private String filtroCancelada;
//	private String filtroPagoParcialmente;
	private Integer codigoContaCorrente;
	private String possuiConta;
	private List<SelectItem> listaSelectItemFiltroContaAPaga;
	private List<SelectItem> listaSelectItemFiltroPaga;
	private List listaSelectItemContaCorrente;
	private String tipoLayout;
	 //Consulta OperadoraCartao
    protected List<SelectItem> listaSelectItemOperadoraCartao;
    private Boolean apresentarFiltroOperadoraCartao;
    private DataModelo dataModeloAluno;

	public ContaPagarRelControle() {

		inicializarDadosControle();
		setMensagemID("msg_entre_prmrelatorio");
	}

	private void inicializarDadosControle() {
		try {
			inicializarListasSelectItemTodosComboBox();
			getContaPagarRelVO().setFornecedor(ContaPagarFiltrosEnum.TODOS.name());
			getContaPagarRelVO().setFuncionario(ContaPagarFiltrosEnum.TODOS.name());
			getContaPagarRelVO().setBanco(ContaPagarFiltrosEnum.TODOS.name());
			getContaPagarRelVO().setAluno(ContaPagarFiltrosEnum.TODOS.name());
			getContaPagarRelVO().setParceiro(ContaPagarFiltrosEnum.TODOS.name());
			getContaPagarRelVO().setResponsavelFinanceiro(ContaPagarFiltrosEnum.TODOS.name());
			getContaPagarRelVO().setDepartamento(ContaPagarFiltrosEnum.TODOS.name());
			getContaPagarRelVO().setOperadoraCartao(ContaPagarFiltrosEnum.TODOS.name());
			getContaPagarRelVO().setDataFim(new Date());
			getContaPagarRelVO().setDataInicio(Uteis.getNewDateComMesesAMenos(1));
			getContaPagarRelVO().setUnidadeEnsino(getUnidadeEnsinoLogado().getCodigo());
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}

	}
	public void scrollerListener(DataScrollEvent dataScrollEvent) throws Exception {
        getControleConsultaOtimizado().setPaginaAtual(dataScrollEvent.getPage());
        getControleConsultaOtimizado().setPage(dataScrollEvent.getPage());
        consultar();
    }
	
	 public void scrollerListenerAluno(DataScrollEvent dataScrollEvent) {
		getDataModeloAluno().setPaginaAtual(dataScrollEvent.getPage());
		getDataModeloAluno().setPage(dataScrollEvent.getPage());
		consultarAluno();
	}

	public void imprimirPDF() {
		List<ContaPagarRelVO> listaContaPagarRelVO = null;		
		try {
			listaContaPagarRelVO = getFacadeFactory().getContaPagarRelFacade().criarObjeto(getContaPagarRelVO(), getTurmaVO().getCodigo(), getFiltroAPagar(), getFiltroPago(), getFiltroNegociada(), getFiltroCancelada(), getCodigoContaCorrente(), getApresentarFiltroContaCorrente(), getUsuarioLogado());
			if (!listaContaPagarRelVO.isEmpty()) {
				if(getTipoLayout().equals("layout1")){
					getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getContaPagarRelFacade().designIReportRelatorio());
				}else{
					getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getContaPagarRelFacade().designIReportRelatorioEspecifica());
				}
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getContaPagarRelFacade().caminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio("Relatório de Contas a Pagar/Pagamento");

				getSuperParametroRelVO().setListaObjetos(listaContaPagarRelVO);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getContaPagarRelFacade().caminhoBaseRelatorio());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());

				getSuperParametroRelVO().setPeriodo(String.valueOf(Uteis.getData(getContaPagarRelVO().getDataInicio())) + "  até  " + Uteis.getData(getContaPagarRelVO().getDataFim()));

				if (!getContaPagarRelVO().getUnidadeEnsino().equals(0)) {
					getSuperParametroRelVO().setUnidadeEnsino((getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getContaPagarRelVO().getUnidadeEnsino(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())).getNome());
				} else {
					getSuperParametroRelVO().setUnidadeEnsino("TODAS");
				}

				getSuperParametroRelVO().setTurma(getTurmaVO().getIdentificadorTurma() == null ? ContaPagarFiltrosEnum.TODOS.getDescricao() : getTurmaVO().getIdentificadorTurma().equals("") ? ContaPagarFiltrosEnum.TODOS.getDescricao() : getTurmaVO().getIdentificadorTurma());

				getSuperParametroRelVO().setSituacao(montarSituacaoFiltroApresentar());

				getSuperParametroRelVO().setFornecedor(getContaPagarRelVO().getFornecedor());

				getSuperParametroRelVO().setFuncionario(getContaPagarRelVO().getFuncionario());

				getSuperParametroRelVO().setBanco(getContaPagarRelVO().getBanco());

				getSuperParametroRelVO().adicionarParametro("departamento", getContaPagarRelVO().getDepartamento() == null || getContaPagarRelVO().getDepartamento().equals(ContaPagarFiltrosEnum.TODOS.getDescricao()) ? ContaPagarFiltrosEnum.TODOS.getDescricao() : getContaPagarRelVO().getCodigoDepartamento() != null && getContaPagarRelVO().getCodigoDepartamento() > 0 ? getFacadeFactory().getDepartamentoFacade().consultarPorChavePrimaria(getContaPagarRelVO().getCodigoDepartamento(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()).getNome() : getContaPagarRelVO().getDepartamento());
				getSuperParametroRelVO().adicionarParametro("possuiConta", possuiConta);
				getSuperParametroRelVO().adicionarParametro("codigoContaCorrente", codigoContaCorrente);

				getSuperParametroRelVO().setCategoriaDespesa(getContaPagarRelVO().getCategoriaDespesa() == null ? ContaPagarFiltrosEnum.TODOS.getDescricao() : getContaPagarRelVO().getCategoriaDespesa());

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
	

	public void imprimirRelatorioExcel() {
		List<ContaPagarRelVO> listaContaPagarRelVO = null;
		try {
			listaContaPagarRelVO = getFacadeFactory().getContaPagarRelFacade().criarObjeto(getContaPagarRelVO(), getTurmaVO().getCodigo(), getFiltroAPagar(), getFiltroPago(), getFiltroNegociada(), getFiltroCancelada(), getCodigoContaCorrente(), getApresentarFiltroContaCorrente(), getUsuarioLogado());
			if (!listaContaPagarRelVO.isEmpty()) {
				if(getTipoLayout().equals("layout1")){
					getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getContaPagarRelFacade().designIReportRelatorio());
				}else{
					getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getContaPagarRelFacade().designIReportRelatorioEspecifica());
				}
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.EXCEL);
				getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getContaPagarRelFacade().caminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio("Relatório de Contas a Pagar/Pagamento");
				getSuperParametroRelVO().setListaObjetos(listaContaPagarRelVO);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getContaPagarRelFacade().caminhoBaseRelatorio());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());

				getSuperParametroRelVO().setPeriodo(String.valueOf(Uteis.getData(getContaPagarRelVO().getDataInicio())) + "  até  " + Uteis.getData(getContaPagarRelVO().getDataFim()));

				if (!getContaPagarRelVO().getUnidadeEnsino().equals(0)) {
					getSuperParametroRelVO().setUnidadeEnsino((getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getContaPagarRelVO().getUnidadeEnsino(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())).getNome());
				} else {
					getSuperParametroRelVO().setUnidadeEnsino("TODAS");
				}

				getSuperParametroRelVO().setTurma(getTurmaVO().getIdentificadorTurma() == null ? ContaPagarFiltrosEnum.TODOS.getDescricao() : getTurmaVO().getIdentificadorTurma().equals("") ? ContaPagarFiltrosEnum.TODOS.getDescricao() : getTurmaVO().getIdentificadorTurma());
				getSuperParametroRelVO().setSituacao(montarSituacaoFiltroApresentar());

				getSuperParametroRelVO().setFornecedor(getContaPagarRelVO().getFornecedor());

				getSuperParametroRelVO().setFuncionario(getContaPagarRelVO().getFuncionario());

				getSuperParametroRelVO().setBanco(getContaPagarRelVO().getBanco());

				getSuperParametroRelVO().setCategoriaDespesa(getContaPagarRelVO().getCategoriaDespesa() == null ? ContaPagarFiltrosEnum.TODOS.getDescricao() : getContaPagarRelVO().getCategoriaDespesa());
				
				getSuperParametroRelVO().adicionarParametro("possuiConta", possuiConta);
				
				getSuperParametroRelVO().adicionarParametro("codigoContaCorrente", codigoContaCorrente);

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
	
	private String montarSituacaoFiltroApresentar() {
		String situacao = "";
		if (getFiltroAPagar().equals("naoFiltrar") || getFiltroPago().equals("naoFiltrar") || getFiltroNegociada().equals("naoFiltrar") || getFiltroCancelada().equals("naoFiltrar")) {
			if (!getFiltroAPagar().equals("naoFiltrar")) {
				situacao = "A Pagar";
			}
			if (!getFiltroPago().equals("naoFiltrar")) {
				situacao += situacao.trim().isEmpty() ? "Pago" : "/Pago";
			}
			if (!getFiltroNegociada().equals("naoFiltrar")) {
				situacao += situacao.trim().isEmpty() ? "Negociada" : "/Negociada";
			}
			if (!getFiltroCancelada().equals("naoFiltrar")) {
				situacao += situacao.trim().isEmpty() ? "Cancelada" : "/Cancelada";
			}
		} else {
			situacao = "TODAS";
		}
		return situacao;
	}

	public void consultarCentroDespesa() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaCentroDespesa().equals("descricao")) {
				objs = getFacadeFactory().getCategoriaDespesaFacade().consultarPorDescricao(getValorConsultaCentroDespesa(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaCentroDespesa().equals("identificadorCentroDespesa")) {
				objs = getFacadeFactory().getCategoriaDespesaFacade().consultarPorIdentificadorCategoriaDespesa(getValorConsultaCentroDespesa(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultaCentroDespesa(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCentroDespesa(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarFornecedor() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaFornecedor().equals("nome")) {
				objs = getFacadeFactory().getFornecedorFacade().consultarPorNome(getValorConsultaFornecedor(), "AT", false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getCampoConsultaFornecedor().equals("razaoSocial")) {
				objs = getFacadeFactory().getFornecedorFacade().consultarPorRazaoSocial(getValorConsultaFornecedor(), "AT", false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getCampoConsultaFornecedor().equals("CNPJ")) {
				objs = getFacadeFactory().getFornecedorFacade().consultarPorCNPJ(getValorConsultaFornecedor(), "AT", false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getCampoConsultaFornecedor().equals("CPF")) {
				objs = getFacadeFactory().getFornecedorFacade().consultarPorCPF(getValorConsultaFornecedor(), "AT", false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			setListaConsultaFornecedor(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaFornecedor(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> listaContaCorrente;
	public List<SelectItem> getListaContaCorrente() {
		if(listaContaCorrente == null) {
			listaContaCorrente = new ArrayList<>(0);
			listaContaCorrente.add(new SelectItem("TODOS", " Todas"));
			listaContaCorrente.add(new SelectItem("FILTRAR", "Filtrar"));
		}
		return listaContaCorrente;
	}

	public List<SelectItem> listaContaPagarFiltrosEnum;
	public List<SelectItem> getListaContaPagarFiltrosEnum() {
		if(listaContaPagarFiltrosEnum == null) {
			listaContaPagarFiltrosEnum = new ArrayList<>(0);
			listaContaPagarFiltrosEnum.add(new SelectItem(ContaPagarFiltrosEnum.TODOS.name(), ContaPagarFiltrosEnum.TODOS.getDescricao()));
			listaContaPagarFiltrosEnum.add(new SelectItem(ContaPagarFiltrosEnum.NENHUM.name(), ContaPagarFiltrosEnum.NENHUM.getDescricao()));
			listaContaPagarFiltrosEnum.add(new SelectItem(ContaPagarFiltrosEnum.FILTRAR.name(), ContaPagarFiltrosEnum.FILTRAR.getDescricao()));
		}
		return listaContaPagarFiltrosEnum;
	}

	

	public void validarFiltroContaCorrente() {
		getContaPagarRelVO().getSituacao();
		if (getPossuiConta().equals("FILTRAR")) {
			setApresentarFiltroContaCorrente(true);
		} else {
			setCodigoContaCorrente(0);
			setApresentarFiltroContaCorrente(false);
		}
	}

	public void validarFiltroFornecedor() {
		getContaPagarRelVO().getSituacao();
		if (getContaPagarRelVO().getFornecedor().equals("FILTRAR")) {
			setApresentarFiltroFornecedor(true);
		} else {
			setApresentarFiltroFornecedor(false);
		}
	}

	public void validarFiltroDepartamento() {
		getContaPagarRelVO().getSituacao();
		if (getContaPagarRelVO().getDepartamento().equals("FILTRAR")) {
			setApresentarFiltroDepartamento(true);
		} else {
			setApresentarFiltroDepartamento(false);
		}
	}

	public void validarFiltroAluno() {
		getContaPagarRelVO().getSituacao();
		if (getContaPagarRelVO().getAluno().equals("FILTRAR")) {
			setApresentarFiltroAluno(true);
		} else {
			setApresentarFiltroAluno(false);
		}
	}

	public void validarFiltroParceiro() {
		getContaPagarRelVO().getSituacao();
		if (getContaPagarRelVO().getParceiro().equals("FILTRAR")) {
			setApresentarFiltroParceiro(true);
		} else {
			setApresentarFiltroParceiro(false);
		}
	}

	public void validarFiltroResponsavelFinanceiro() {
		getContaPagarRelVO().getSituacao();
		if (getContaPagarRelVO().getResponsavelFinanceiro().equals("FILTRAR")) {
			setApresentarFiltroResponsavelFinanceiro(true);
		} else {
			setApresentarFiltroResponsavelFinanceiro(false);
		}
	}

	public Boolean getApresentarFiltroAluno() {
		if (apresentarFiltroAluno == null) {
			apresentarFiltroAluno = false;
		}
		return apresentarFiltroAluno;
	}

	public void setApresentarFiltroAluno(Boolean apresentarFiltroAluno) {
		this.apresentarFiltroAluno = apresentarFiltroAluno;
	}

	public Boolean getApresentarFiltroResponsavelFinanceiro() {
		if (apresentarFiltroResponsavelFinanceiro == null) {
			apresentarFiltroResponsavelFinanceiro = false;
		}
		return apresentarFiltroResponsavelFinanceiro;
	}

	public void setApresentarFiltroResponsavelFinanceiro(Boolean apresentarFiltroResponsavelFinanceiro) {
		this.apresentarFiltroResponsavelFinanceiro = apresentarFiltroResponsavelFinanceiro;
	}

	public Boolean getApresentarFiltroParceiro() {
		if (apresentarFiltroParceiro == null) {
			apresentarFiltroParceiro = false;
		}
		return apresentarFiltroParceiro;
	}

	public void setApresentarFiltroParceiro(Boolean apresentarFiltroParceiro) {
		this.apresentarFiltroParceiro = apresentarFiltroParceiro;
	}

	public void validarFiltroFuncionario() {
		if (getContaPagarRelVO().getFuncionario().equals("FILTRAR")) {
			setApresentarFiltroFuncionario(true);
		} else {
			setApresentarFiltroFuncionario(false);
		}
	}

	public void validarFiltroBanco() {
		if (getContaPagarRelVO().getBanco().equals("FILTRAR")) {
			setApresentarFiltroBanco(true);
		} else {
			setApresentarFiltroBanco(false);
		}
	}
	
	public void validarFiltroOperadoraCartao() {
		if (getContaPagarRelVO().getOperadoraCartao().equals("FILTRAR")) {
			setApresentarFiltroOperadoraCartao(true);
		} else {
			setApresentarFiltroOperadoraCartao(false);
		}
	}

	public void limparDadosFornecedor() {
		getContaPagarRelVO().setNomeFornecedor("");
		getContaPagarRelVO().setFornecedorCpfCnpj("");
		getContaPagarRelVO().setCodigoFornecedor(0);
	}

	public void limparDadosFuncionario() {
		getContaPagarRelVO().setNomeFuncionario("");
		getContaPagarRelVO().setCodigoFuncionario(0);
	}

	public void limparDadosBanco() {
		getContaPagarRelVO().setNomeBanco("");
		getContaPagarRelVO().setCodigoBanco(0);
	}

	public void limparDadosCentroDespesa() {
		getContaPagarRelVO().setCategoriaDespesa("");
		getContaPagarRelVO().setCodigoCategoriaDespesa(0);
	}

	public void consultarBanco() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaBanco().equals("nome")) {
				objs = getFacadeFactory().getBancoFacade().consultarPorNome(getValorConsultaBanco(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaBanco().equals("nrBanco")) {
				objs = getFacadeFactory().getBancoFacade().consultarPorNrBanco(getValorConsultaBanco(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
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
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorNome(getValorConsultaFuncionario(), getContaPagarRelVO().getUnidadeEnsino(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("cpf")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorCPF(getValorConsultaFuncionario(), getContaPagarRelVO().getUnidadeEnsino(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultaFuncionario(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaFuncionario(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> getTipoConsultaComboCentroDespesa() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("descricao", "Descrição"));
		itens.add(new SelectItem("identificadorCentroDespesa", "Identificador Centro Despesa"));
		return itens;
	}

	public List<SelectItem> getTipoConsultaComboFornecedor() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("razaoSocial", "Razão Social"));
		itens.add(new SelectItem("CNPJ", "CNPJ"));
		itens.add(new SelectItem("CPF", "CPF"));
		return itens;
	}

	public List<SelectItem> getTipoConsultaComboFuncionario() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("CPF", "CPF"));
		return itens;
	}

	public List<SelectItem> getTipoConsultaComboBanco() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("nrBanco", "Número Banco"));
		return itens;
	}

	public List<SelectItem> getTipoConsultaComboPlanoConta() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("identificadorPlanoConta", "Identificador Plano Conta"));
		return itens;
	}

	public void selecionarFornecedor() {
		FornecedorVO obj = (FornecedorVO) context().getExternalContext().getRequestMap().get("fornecedorItem");
		getContaPagarRelVO().setNomeFornecedor(obj.getNome());
		if (obj.getTipoEmpresa().equals("FI")) {
			getContaPagarRelVO().setFornecedorCpfCnpj(obj.getCPF());
		} else {
			getContaPagarRelVO().setFornecedorCpfCnpj(obj.getCNPJ());
		}
		getContaPagarRelVO().setCodigoFornecedor(obj.getCodigo());
		setCampoConsultaFornecedor("");
		setValorConsultaFornecedor("");
		setListaConsultaFornecedor(new ArrayList(0));

	}

	public void selecionarCentroDespesa() {
		CategoriaDespesaVO obj = (CategoriaDespesaVO) context().getExternalContext().getRequestMap().get("centroDespesa");
		getContaPagarRelVO().setCodigoCategoriaDespesa(obj.getCodigo());
		getContaPagarRelVO().setCategoriaDespesa(obj.getDescricao());
		setCampoConsultaCentroDespesa("");
		setValorConsultaCentroDespesa("");
		setListaConsultaCentroDespesa(new ArrayList(0));
	}

	public void selecionarFuncionario() {
		FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItem");
		getContaPagarRelVO().setNomeFuncionario(obj.getPessoa().getNome());
		getContaPagarRelVO().setCodigoFuncionario(obj.getCodigo());
		setCampoConsultaFuncionario("");
		setValorConsultaFuncionario("");
		setListaConsultaFuncionario(new ArrayList(0));
	}

	public void selecionarBanco() {
		BancoVO obj = (BancoVO) context().getExternalContext().getRequestMap().get("bancoItem");
		getContaPagarRelVO().setNomeBanco(obj.getNome());
		getContaPagarRelVO().setCodigoBanco(obj.getCodigo());
		setCampoConsultaBanco("");
		setValorConsultaBanco("");
		setListaConsultaBanco(new ArrayList(0));
	}

	public void inicializarListasSelectItemTodosComboBox() throws Exception {
		montarListaSelectItemOrdenacao();
		montarListaSelectItemUnidadeEnsino();
		montarListaSelectItemTurma();
		montarListaSelectItemContaCorrente();
	}

	public void montarListaSelectItemOrdenacao() {
		Vector opcoes = getFacadeFactory().getContaPagarRelFacade().getOrdenacoesRelatorio();
		Enumeration i = opcoes.elements();
		List objs = new ArrayList(0);
		int contador = 0;
		while (i.hasMoreElements()) {
			String option = (String) i.nextElement();
			objs.add(new SelectItem(contador, option));
			contador++;
		}
		setListaSelectItemOrdenacoesRelatorio(objs);
	}

	public void montarListaSelectItemUnidadeEnsino() {
		try {
			montarListaSelectItemUnidadeEnsino("");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}

	}

	public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
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

	public void montarListaSelectItemTurma() throws Exception {
		List objs = new ArrayList(0);
		objs.add(new SelectItem(0, ""));
		List<TurmaVO> listaTurma = null;
		listaTurma = getFacadeFactory().getTurmaFacade().consultarPorCodigoUnidadeEnsino(getContaPagarRelVO().getUnidadeEnsino(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		for (TurmaVO turma : listaTurma) {
			objs.add(new SelectItem(turma.getCodigo(), turma.getIdentificadorTurma()));
		}
		setListaSelectItemTurma(objs);
	}

	public void montarListaSelectItemContaCorrente() {
		try {
			montarListaSelectItemContaCorrente("");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public boolean isApresentarComboTurma() {
		try {
			return (getUnidadeEnsinoLogado().getCodigo() != 0);
		} catch (Exception e) {
			return false;
		}
	}

	public List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
		return getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
	}
	
	public List<SelectItem> getTipoComboBoxFiltroData() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("dataVencimento", "Data Vencimento"));
		itens.add(new SelectItem("dataCompetencia", "Data Competência"));
		itens.add(new SelectItem("dataPagamento", "Data Pagamento"));
		return itens;
	}

	public List<SelectItem> getListaSelectItemSituacaoContaPagar() throws Exception {
		List<SelectItem> objs = new ArrayList<>(0);
		objs.add(new SelectItem("", "TODAS"));
		Hashtable pagarRecebidoNegociado =  Dominios.getPagarPagoNegociado();
		Enumeration keys = pagarRecebidoNegociado.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) pagarRecebidoNegociado.get(value);
			objs.add(new SelectItem(value, label));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		// Collections.sort((List) objs, ordenador);
		return objs;
	}

	public void montarListaSelectItemContaCorrente(String prm) throws Exception {
		List<ContaCorrenteVO> resultadoConsulta = null;
		Iterator<ContaCorrenteVO> i = null;
		try {
			resultadoConsulta = consultarContaCorrentePorNumero(prm);
			i = resultadoConsulta.iterator();
			List<SelectItem> objs = new ArrayList<>(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				ContaCorrenteVO obj = (ContaCorrenteVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNumero()));
			}
			setListaSelectItemContaCorrente(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	public List<ContaCorrenteVO> consultarContaCorrentePorNumero(String numeroPrm) throws Exception {
		return getFacadeFactory().getContaCorrenteFacade().consultarPorNumero(numeroPrm, getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
	}

	public String getCampoConsultaCentroDespesa() {
		return campoConsultaCentroDespesa;
	}

	public void setCampoConsultaCentroDespesa(String campoConsultaCentroDespesa) {
		this.campoConsultaCentroDespesa = campoConsultaCentroDespesa;
	}

	public String getCampoConsultaFornecedor() {
		return campoConsultaFornecedor;
	}

	public void setCampoConsultaFornecedor(String campoConsultaFornecedor) {
		this.campoConsultaFornecedor = campoConsultaFornecedor;
	}

	public List getListaConsultaCentroDespesa() {
		return listaConsultaCentroDespesa;
	}

	public void setListaConsultaCentroDespesa(List listaConsultaCentroDespesa) {
		this.listaConsultaCentroDespesa = listaConsultaCentroDespesa;
	}

	public List getListaConsultaFornecedor() {
		return listaConsultaFornecedor;
	}

	public void setListaConsultaFornecedor(List listaConsultaFornecedor) {
		this.listaConsultaFornecedor = listaConsultaFornecedor;
	}

	public String getValorConsultaCentroDespesa() {
		return valorConsultaCentroDespesa;
	}

	public void setValorConsultaCentroDespesa(String valorConsultaCentroDespesa) {
		this.valorConsultaCentroDespesa = valorConsultaCentroDespesa;
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
	 * @param listaConsultaBanco
	 *            the listaConsultaBanco to set
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
	 * @param listaConsultaFuncionario
	 *            the listaConsultaFuncionario to set
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
	 * @param valorConsultaBanco
	 *            the valorConsultaBanco to set
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
	 * @param valorConsultaFuncionario
	 *            the valorConsultaFuncionario to set
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
	 * @param campoConsultaBanco
	 *            the campoConsultaBanco to set
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
	 * @param campoConsultaFuncionario
	 *            the campoConsultaFuncionario to set
	 */
	public void setCampoConsultaFuncionario(String campoConsultaFuncionario) {
		this.campoConsultaFuncionario = campoConsultaFuncionario;
	}

	/**
	 * @return the listaSelectItemUnidadeEnsino
	 */
	public List getListaSelectItemUnidadeEnsino() {
		return listaSelectItemUnidadeEnsino;
	}

	/**
	 * @param listaSelectItemUnidadeEnsino
	 *            the listaSelectItemUnidadeEnsino to set
	 */
	public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	/**
	 * @return the nomeRelatorio
	 */
	public String getNomeRelatorio() {
		return nomeRelatorio;
	}

	/**
	 * @param nomeRelatorio
	 *            the nomeRelatorio to set
	 */
	public void setNomeRelatorio(String nomeRelatorio) {
		this.nomeRelatorio = nomeRelatorio;
	}

	/**
	 * @return the listaSelectItemTurma
	 */
	public List getListaSelectItemTurma() {
		return listaSelectItemTurma;
	}

	/**
	 * @param listaSelectItemTurma
	 *            the listaSelectItemTurma to set
	 */
	public void setListaSelectItemTurma(List listaSelectItemTurma) {
		this.listaSelectItemTurma = listaSelectItemTurma;
	}

	public void consultarTurma() {
		try {
			super.consultar();
			List<TurmaVO> objs = new ArrayList<TurmaVO>(0);
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				objs = getFacadeFactory().getTurmaFacade().consultarPorIdentificadorTurma(getValorConsultaTurma(), getContaPagarRelVO().getUnidadeEnsino(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeUnidadeEnsino")) {
				objs = getFacadeFactory().getTurmaFacade().consultarPorNomeUnidadeEnsino(getValorConsultaTurma(), getContaPagarRelVO().getUnidadeEnsino(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeTurno")) {
				objs = getFacadeFactory().getTurmaFacade().consultarPorNomeTurno(getValorConsultaTurma(), getContaPagarRelVO().getUnidadeEnsino(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeCurso")) {
				objs = getFacadeFactory().getTurmaFacade().consultarPorNomeCurso(getValorConsultaTurma(), getContaPagarRelVO().getUnidadeEnsino(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaTurma(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			getListaConsulta().clear();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> getTipoConsultaComboTurma() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("identificadorTurma", "Identificador"));
		itens.add(new SelectItem("nomeUnidadeEnsino", "Unidade Ensino"));
		itens.add(new SelectItem("nomeTurno", "Turno"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
	}

	public void selecionarTurma() {
		try {
			TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItem");
			setTurmaVO(obj);
		} catch (Exception ex) {
			setListaSelectItemTurma(new ArrayList<>(0));
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public void limparDadosTurma() {
		setTurmaVO(new TurmaVO());
		getTurmaVO().setCurso(new CursoVO());
		getTurmaVO().setTurno(new TurnoVO());
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

	public List<TurmaVO> getListaConsultaTurma() {
		if (listaConsultaTurma == null) {
			listaConsultaTurma = new ArrayList<>(0);
		}
		return listaConsultaTurma;
	}

	public void setListaConsultaTurma(List<TurmaVO> listaConsultaTurma) {
		this.listaConsultaTurma = listaConsultaTurma;
	}

	public TurmaVO getTurmaVO() {
		if (turmaVO == null) {
			turmaVO = new TurmaVO();
		}
		return turmaVO;
	}

	public void setTurmaVO(TurmaVO turmaVO) {
		this.turmaVO = turmaVO;
	}

	public ContaPagarRelVO getContaPagarRelVO() {
		if (contaPagarRelVO == null) {
			contaPagarRelVO = new ContaPagarRelVO();
		}
		return contaPagarRelVO;
	}

	public void setContaPagarRelVO(ContaPagarRelVO contaPagarRelVO) {
		this.contaPagarRelVO = contaPagarRelVO;
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

	private List listaConsultaParceiro;
	private String valorConsultaParceiro;
	private String campoConsultaParceiro;


	public List<SelectItem> getTipoConsultaComboAluno() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("nomePessoa", "Aluno/Candidato"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		return itens;
	}

	public void consultarAluno() {
		try {
			List<MatriculaVO> objs = new ArrayList<>(0);
			if (getDataModeloAluno().getValorConsulta().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			if (getDataModeloAluno().getCampoConsulta().equals("matricula")) {
				objs.add(getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatricula(getDataModeloAluno().getValorConsulta(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));
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

	public void selecionarAluno() {
		MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItem");
		getContaPagarRelVO().setMatriculaAluno(obj.getMatricula());
		getContaPagarRelVO().setNomeAluno(obj.getAluno().getNome());
		getContaPagarRelVO().setCodigoAluno(obj.getAluno().getCodigo());
		setDataModeloAluno(new DataModelo());
		getDataModeloAluno().setLimitePorPagina(10);
	}

	public List getListaConsultaParceiro() {
		if (listaConsultaParceiro == null) {
			listaConsultaParceiro = new ArrayList<>(0);
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
				setListaConsultaResponsavelFinanceiro(getFacadeFactory().getPessoaFacade().consultaRapidaPorNomeResponsavelFinanceiro(getValorConsultaResponsavelFinanceiro(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado()));
			}
			if (getCampoConsultaResponsavelFinanceiro().equals("nomeAluno")) {
				setListaConsultaResponsavelFinanceiro(getFacadeFactory().getPessoaFacade().consultaRapidaPorNomeAlunoResponsavelFinanceiro(getValorConsultaResponsavelFinanceiro(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado()));
			}
			if (getCampoConsultaResponsavelFinanceiro().equals("CPF")) {
				setListaConsultaResponsavelFinanceiro(getFacadeFactory().getPessoaFacade().consultaRapidaPorCpfResponsavelFinanceiro(getValorConsultaResponsavelFinanceiro(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado()));
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
			tipoConsultaComboResponsavelFinanceiro = new ArrayList<>(0);
			tipoConsultaComboResponsavelFinanceiro.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboResponsavelFinanceiro.add(new SelectItem("nomeAluno", "Aluno"));
			tipoConsultaComboResponsavelFinanceiro.add(new SelectItem("CPF", "CPF"));
		}
		return tipoConsultaComboResponsavelFinanceiro;
	}

	public void selecionarResponsavelFinanceiro() {
		try {
			PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("responsavelFinanceiro");
			getContaPagarRelVO().setNomeResponsavelFinanceiro(obj.getNome());
			getContaPagarRelVO().setCodigoResponsavelFinanceiro(obj.getCodigo());
			getListaConsultaResponsavelFinanceiro().clear();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<PessoaVO> getListaConsultaResponsavelFinanceiro() {
		if (listaConsultaResponsavelFinanceiro == null) {
			listaConsultaResponsavelFinanceiro = new ArrayList<>(0);
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
				objs = getFacadeFactory().getParceiroFacade().consultarPorNome(getValorConsultaParceiro(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaParceiro().equals("razaoSocial")) {
				objs = getFacadeFactory().getParceiroFacade().consultarPorRazaoSocial(getValorConsultaParceiro(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaParceiro(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> getTipoConsultaParceiro() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("razaoSocial", "Razão social"));
		return itens;
	}

	public void selecionarParceiro() {
		ParceiroVO obj = (ParceiroVO) context().getExternalContext().getRequestMap().get("parceiroItem");
		getContaPagarRelVO().setNomeParceiro(obj.getNome());
		getContaPagarRelVO().setCodigoParceiro(obj.getCodigo());
		listaConsultaParceiro.clear();
		this.setValorConsultaParceiro("");
		this.setCampoConsultaParceiro("");
	}

	public void limparDadosParceiro() {
		getContaPagarRelVO().setNomeParceiro("");
		getContaPagarRelVO().setCodigoParceiro(0);
	}

	public void limparDadosAluno() {
		getContaPagarRelVO().setMatriculaAluno("");
		getContaPagarRelVO().setNomeAluno("");
		getContaPagarRelVO().setCodigoAluno(0);
	}

	public void limparDadosDepartamento() {
		getContaPagarRelVO().setDepartamento("");
		getContaPagarRelVO().setCodigoDepartamento(0);
	}

	public void limparDadosResponsavelFinanceiro() {
		getContaPagarRelVO().setNomeResponsavelFinanceiro("");
		getContaPagarRelVO().setCodigoResponsavelFinanceiro(0);
	}

	public Boolean getApresentarFiltroDepartamento() {
		if (apresentarFiltroDepartamento == null) {
			apresentarFiltroDepartamento = false;
		}
		return apresentarFiltroDepartamento;
	}

	public void setApresentarFiltroDepartamento(Boolean apresentarFiltroDepartamento) {
		this.apresentarFiltroDepartamento = apresentarFiltroDepartamento;
	}

	public List<SelectItem> getListaSelectItemDepartamento() {
		if (listaSelectItemDepartamento == null) {
			listaSelectItemDepartamento = new ArrayList<>(0);
			try {
				List<DepartamentoVO> departamentoVOs = getFacadeFactory().getDepartamentoFacade().consultarDepartamentoContaPagar(getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
				listaSelectItemDepartamento.add(new SelectItem(0, ""));
				for (DepartamentoVO departamentoVO : departamentoVOs) {
					listaSelectItemDepartamento.add(new SelectItem(departamentoVO.getCodigo(), departamentoVO.getNome()));
				}
			} catch (Exception e) {
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			}
		}
		return listaSelectItemDepartamento;
	}

	public void setListaSelectItemDepartamento(List<SelectItem> listaSelectItemDepartamento) {
		this.listaSelectItemDepartamento = listaSelectItemDepartamento;
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
	
	public String getFiltroNegociada() {
		if (filtroNegociada == null) {
			filtroNegociada = "naoFiltrar";
		}
		return filtroNegociada;
	}

	public void setFiltroNegociada(String filtroNegociada) {
		this.filtroNegociada = filtroNegociada;
	}

	public String getFiltroCancelada() {
		if (filtroCancelada == null) {
			filtroCancelada = "naoFiltrar";
		}
		return filtroCancelada;
	}

	public void setFiltroCancelada(String filtroCancelada) {
		this.filtroCancelada = filtroCancelada;
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

	public String getPossuiConta() {

		if (possuiConta == null) {
			possuiConta = "TODAS";
		}

		return possuiConta;
	}

	public void setPossuiConta(String possuiConta) {
		this.possuiConta = possuiConta;
	}

	public List<SelectItem> getListaSelectItemFiltroContaAPaga() {
		if (listaSelectItemFiltroContaAPaga == null) {
			listaSelectItemFiltroContaAPaga = new ArrayList<>(0);
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
			listaSelectItemFiltroPaga = new ArrayList<>(0);
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

	public List getListaSelectItemContaCorrente() {
		if (listaSelectItemContaCorrente == null) {
			listaSelectItemContaCorrente = new ArrayList(0);
		}
		return listaSelectItemContaCorrente;
	}

	public void setListaSelectItemContaCorrente(List listaSelectItemContaCorrente) {
		this.listaSelectItemContaCorrente = listaSelectItemContaCorrente;
	}

	public List<SelectItem> getListaSelectItemTipoLayout(){
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("layout1", "Layout 1"));
		itens.add(new SelectItem("layout2", "Layout 2"));
		return itens;
	}

	public String getTipoLayout() {
		if (tipoLayout == null) {
			tipoLayout = "";
		}
		return tipoLayout;
	}

	public void setTipoLayout(String tipoLayout) {
		this.tipoLayout = tipoLayout;
	}
	
	public List<SelectItem> getListaSelectItemOperadoraCartao() {
		if (listaSelectItemOperadoraCartao == null) {
			listaSelectItemOperadoraCartao = new ArrayList<>(0);
			try {
				List<OperadoraCartaoVO> operadoraCartaoVOs =  getFacadeFactory().getOperadoraCartaoFacade().consultarPorTipo(TipoCartaoOperadoraCartaoEnum.CARTAO_CREDITO.name(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
				listaSelectItemOperadoraCartao = UtilSelectItem.getListaSelectItem(operadoraCartaoVOs, "codigo", "nome");
			}catch (Exception e) {
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
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
