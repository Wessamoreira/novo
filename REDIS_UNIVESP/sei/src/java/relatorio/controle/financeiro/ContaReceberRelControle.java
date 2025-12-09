package relatorio.controle.financeiro;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SelectItemOrdemValor;
import negocio.comuns.academico.CondicaoPagamentoPlanoFinanceiroCursoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PlanoFinanceiroCursoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.financeiro.CentroReceitaVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.ParceiroVO;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.dominios.SituacaoContaReceber;
import negocio.comuns.utilitarias.dominios.TipoOrigemContaReceber;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.financeiro.ContaReceberRelVO;
import relatorio.negocio.comuns.financeiro.FiltroRelatorioFinanceiroVO;

@SuppressWarnings({"rawtypes","unchecked", "deprecation"})
@Controller("ContaReceberRelControle")
@Scope("viewScope")
@Lazy
public class ContaReceberRelControle extends SuperControleRelatorio {

	private static final long serialVersionUID = 1L;
	protected Date dataInicio;
	protected Date dataFim;
	protected String tipoPessoa;
	protected String alunoMatriula;
	protected String alunoNome;
	protected String funcionarioMatricula;
	protected String funcionarioNome;
	protected String candidatoCpf;
	protected String candidatoNome;
	protected String parceiroCPF;
	protected String parceiroCNPJ;
	protected String parceiroNome;
	protected String fornecedorNome;
	protected String situacao;
	private UnidadeEnsinoCursoVO unidadeEnsinoCursoVO;
	private TurmaVO turmaVO;
	protected ContaCorrenteVO contaCorrente;
	private CondicaoPagamentoPlanoFinanceiroCursoVO condicaoPagamentoPlanoFinanceiroCurso;
	private List listaSelectItemCondicaoPagamentoPlanoFinanceiroCurso;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	protected Integer pessoa;
	private Integer parceiroCodigo;
	private Integer fornecedorCodigo;
	protected Integer centroReceita;
	protected String centroReceitaDescricao;
	private static String nomeRelatorio;
	protected List listaSelectItemMatriculaAluno;
	protected List listaSelectItemFuncionario;
	protected List listaConsultaFuncionario;
	protected String valorConsultaFuncionario;
	protected String campoConsultaFuncionario;
	protected List listaConsultaAluno;
	protected String valorConsultaAluno;
	protected String campoConsultaAluno;
	protected List listaConsultaCurso;
	protected String valorConsultaCurso;
	protected String campoConsultaCurso;
	protected List listaConsultaTurma;
	protected String valorConsultaTurma;
	protected String campoConsultaTurma;
	protected List listaConsultaCandidato;
	protected String valorConsultaCandidato;
	protected String campoConsultaCandidato;
	protected List listaConsultaParceiro;
	protected String valorConsultaParceiro;
	protected String campoConsultaParceiro;
	protected List listaConsultaCentroReceita;
	protected String valorConsultaCentroReceita;
	protected String campoConsultaCentroReceita;
	private List listaConsultaPlanoFinanceiroCurso;
	private String valorConsultaPlanoFinanceiroCurso;
	private String campoConsultaPlanoFinanceiroCurso;
	private PlanoFinanceiroCursoVO planoFinanceiroCursoVO;
	protected Boolean parceiro;
	protected List listaSelectItemContaCorrente;
	private List listaSelectItemCurso;
	private List listaSelectItemTurma;
	private Boolean gerarRelatorio;
	private String tipoLayout;
	protected List<FornecedorVO> listaConsultaFornecedor;
	protected String valorConsultaFornecedor;
	protected String campoConsultaFornecedor;
	private FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO;
	private String campoConsultaResponsavelFinanceiro;
	private String valorConsultaResponsavelFinanceiro;
	protected List<PessoaVO> listaConsultaResponsavelFinanceiro;
	private Integer responsavelFinanceiroCodigo;
	private String responsavelFinanceiroNome;
	private String responsavelFinanceiroCPF;
	private Date dataInicioCompetencia;
	private Date dataFimCompetencia;
	private Boolean considerarUnidadeEnsinoFinanceira;

	public ContaReceberRelControle() throws Exception {
		setMensagemID("msg_entre_prmrelatorio");
	}

	@PostConstruct
	private void inicializarDadosControle() {
		setListaConsultaAluno(new ArrayList(0));
		inicializarListasSelectItemTodosComboBox();
		
		try {
			Map<String, String> keys = getFacadeFactory().getLayoutPadraoFacade().consultarValoresPadroes(new String[]{"situacao", "tipoLayout",  "tipoPessoa", "opcaoOrdenacao", "considerarUnidadeFinanceira"}, ContaReceberRelControle.class.getSimpleName());
			for(String key: keys.keySet()){
				try {
				if(key.equals("situacao")){
					setSituacao(keys.get(key));
				}
				else if(key.equals("tipoLayout")){
					setTipoLayout(keys.get(key));
				}
				else if(key.equals("tipoPessoa")){
					setTipoPessoa(keys.get(key));
				}
				else if(key.equals("opcaoOrdenacao") && keys.get(key) != "null" && !keys.get(key).trim().isEmpty()){
					setOpcaoOrdenacao(Integer.valueOf(keys.get(key)));
				}
				else if(key.equals("considerarUnidadeFinanceira")){
					setConsiderarUnidadeEnsinoFinanceira(Boolean.valueOf(keys.get(key)));
				}
				} catch (Exception e) {
					
				}
			}
			getFacadeFactory().getLayoutPadraoFacade().consultarPadraoFiltroTipoOrigemContaReceber(getFiltroRelatorioFinanceiroVO(), ContaReceberRelControle.class.getSimpleName(), getUsuarioLogado());
		} catch (Exception e) {
			
		}
	}
	
	public void limparDadosFornecedor() {
		setFornecedorCodigo(0);
		setFornecedorNome("");
	}

	public Boolean getGerarRelatorio() {
		if (gerarRelatorio == null) {
			gerarRelatorio = false;
		}
		return gerarRelatorio;
	}

	public void setGerarRelatorio(Boolean gerarRelatorio) {
		this.gerarRelatorio = gerarRelatorio;
	}

	public void imprimirRelatorioExcel() {
		List<ContaReceberRelVO> listaObjetos = null;
		String tipoPessoa = null;
		String ordenacao = null;
		if (getTipoLayout().equals("layout3")) {
			setOpcaoOrdenacao(3);
		}
		if (getTipoLayout().equals("layout5")) {
			setOpcaoOrdenacao(4);
		}
		try {
			if (getDataInicioCompetencia() != null) {
				setDataInicioCompetencia(Uteis.getDataPrimeiroDiaMes(getDataInicioCompetencia()));
			}
			if (getDataFimCompetencia() != null) {
				setDataFimCompetencia(Uteis.getDataUltimoDiaMes(getDataFimCompetencia()));
			}
			listaObjetos = getFacadeFactory().getContaReceberRelFacade().criarObjeto(getFiltroRelatorioFinanceiroVO(), getDataInicio(), getDataFim(), getTipoPessoa(), getAlunoMatriula(), getAlunoNome(), getFuncionarioMatricula(), getFuncionarioNome(), getCandidatoCpf(), getCandidatoNome(), getParceiroCPF(), getParceiroCNPJ(), getParceiroNome(), getSituacao(), getUnidadeEnsinoVOs(), getUnidadeEnsinoCursoVO(), getTurmaVO(), getContaCorrente(), getPessoa(), getParceiroCodigo(), getOpcaoOrdenacao(), getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema(), getCentroReceita(), getCondicaoPagamentoPlanoFinanceiroCurso().getCodigo(), getFornecedorCodigo(), getResponsavelFinanceiroCodigo(), getDataInicioCompetencia(), getDataFimCompetencia(), getConsiderarUnidadeEnsinoFinanceira());
			if (!listaObjetos.isEmpty()) {
				getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getContaReceberRelFacade().designIReportRelatorioExcel(getTipoLayout()));
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.EXCEL);
				getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getContaReceberRelFacade().caminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio("Relatório de Receitas (" + this.getSituacao_Apresentar() + ")");
				getSuperParametroRelVO().setListaObjetos(listaObjetos);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getContaReceberRelFacade().caminhoBaseRelatorio());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setPeriodo(String.valueOf(Uteis.getData(getDataInicio())) + "  a  " + String.valueOf(Uteis.getData(getDataFim())));
				String filtroTipoOrigem = getFiltroRelatorioFinanceiroVO().getItensFiltroTipoOrigem();
				getSuperParametroRelVO().setTipoOrigem(filtroTipoOrigem.equals("") ? "TODOS" : filtroTipoOrigem);
				tipoPessoa = "TODAS";
				if (getTipoPessoa() != null && !getTipoPessoa().equals("")) {
					tipoPessoa = ((Hashtable<String, String>) Dominios.getAlunoFuncionarioCandidatoParceiro()).get(getTipoPessoa());
				}
				getSuperParametroRelVO().setTipoPessoa(tipoPessoa);
				getSuperParametroRelVO().setSituacao(getSituacao() == null ? "TODAS" : getSituacao().equals("") ? "TODAS" : getSituacao_Apresentar());
				getSuperParametroRelVO().setUnidadeEnsino(getUnidadeEnsinoVO().getNome());
				if (getUnidadeEnsinoCursoVO().getCodigo() > 0) {
					getSuperParametroRelVO().setCurso((getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorChavePrimaria(getUnidadeEnsinoCursoVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())).getNomeCursoTurno());
				} else {
					getSuperParametroRelVO().setCurso("TODOS");
				}
				if (getTurmaVO().getCodigo() != 0) {
					setTurmaVO(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(getTurmaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
					getSuperParametroRelVO().setTurma(getTurmaVO().getIdentificadorTurma());
				} else {
					getSuperParametroRelVO().setTurma("TODAS");
				}
				if (getContaCorrente().getCodigo() != 0) {
					setContaCorrente(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(getContaCorrente().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
					getSuperParametroRelVO().setContaCorrente(getContaCorrente().getNumero());
				} else {
					getSuperParametroRelVO().setContaCorrente("TODAS");
				}
				if (!getCentroReceita().equals(0)) {
					getSuperParametroRelVO().adicionarParametro("centroReceita", getCentroReceitaDescricao());
				} else {
					getSuperParametroRelVO().adicionarParametro("centroReceita", "TODOS");
				}
				if (!getPlanoFinanceiroCursoVO().getCodigo().equals(0)) {
					getSuperParametroRelVO().adicionarParametro("planoFinanceiroCurso", getPlanoFinanceiroCursoVO().getDescricao());
				} else {
					getSuperParametroRelVO().adicionarParametro("planoFinanceiroCurso", "TODOS");
				}
				if (!getCondicaoPagamentoPlanoFinanceiroCurso().getCodigo().equals(0)) {
					setCondicaoPagamentoPlanoFinanceiroCurso(getFacadeFactory().getCondicaoPagamentoPlanoFinanceiroCursoFacade().consultarPorChavePrimaria(getCondicaoPagamentoPlanoFinanceiroCurso().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
					getSuperParametroRelVO().adicionarParametro("condicaoPagamento", getCondicaoPagamentoPlanoFinanceiroCurso().getDescricao());
				} else {
					getSuperParametroRelVO().adicionarParametro("condicaoPagamento", "TODOS");
				}
				if (getDataInicioCompetencia() != null) {
					getSuperParametroRelVO().setDataInicioCompetencia(Uteis.getMesData(getDataInicioCompetencia()) + "/" + Uteis.getAnoData(getDataInicioCompetencia()));
				} else {
					getSuperParametroRelVO().setDataInicioCompetencia("");
				}
				if (getDataFimCompetencia() != null) {
					getSuperParametroRelVO().setDataFimCompetencia(Uteis.getMesData(getDataFimCompetencia()) + "/" + Uteis.getAnoData(getDataFimCompetencia()));
				} else {
					getSuperParametroRelVO().setDataFimCompetencia("");
				}
				getSuperParametroRelVO().adicionarParametro("considerarUnidadeEnsinoFinanceira", getConsiderarUnidadeEnsinoFinanceira());
				
				ordenacao = "";
				if (getOpcaoOrdenacao() == 0) {
					ordenacao = "Nome";
				} else if (getOpcaoOrdenacao() == 1) {
					ordenacao = "Data";
				} else if (getOpcaoOrdenacao() == 2) {
					ordenacao = "Tipo de Origem";
				}
				getSuperParametroRelVO().setOrdenadoPor(ordenacao);
				realizarImpressaoRelatorio();
				getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getSituacao(), ContaReceberRelControle.class.getSimpleName(), "situacao", getUsuarioLogado());
				getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getTipoLayout(), ContaReceberRelControle.class.getSimpleName(), "tipoLayout", getUsuarioLogado());
				getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getTipoPessoa(), ContaReceberRelControle.class.getSimpleName(), "tipoPessoa", getUsuarioLogado());
				getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getOpcaoOrdenacao().toString(), ContaReceberRelControle.class.getSimpleName(), "opcaoOrdenacao", getUsuarioLogado());
				getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getConsiderarUnidadeEnsinoFinanceira().toString(), ContaReceberRelControle.class.getSimpleName(), "considerarUnidadeFinanceira", getUsuarioLogado());
				getFacadeFactory().getLayoutPadraoFacade().persistirFiltroTipoOrigemContaReceber(getFiltroRelatorioFinanceiroVO(), ContaReceberRelControle.class.getSimpleName(), getUsuarioLogado());
				removerObjetoMemoria(this);
				inicializarDadosControle();
				consultarUnidadeEnsino();
				setMensagemID("msg_relatorio_ok");
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			Uteis.liberarListaMemoria(listaObjetos);
			tipoPessoa = null;
			ordenacao = null;
			setGerarRelatorio(false);
		}
	}

	public void imprimirPDF() {
		List<ContaReceberRelVO> listaObjetos = null;
		String tipoPessoa = null;
		String ordenacao = null;
		if (getTipoLayout().equals("layout3")) {
			setOpcaoOrdenacao(3);
		}
		if (getTipoLayout().equals("layout5")) {
			setOpcaoOrdenacao(4);
		}
		try {
			getFacadeFactory().getContaReceberRelFacade().validarDados(getUnidadeEnsinoVOs());
			if (getDataInicioCompetencia() != null) {
				setDataInicioCompetencia(Uteis.getDataPrimeiroDiaMes(getDataInicioCompetencia()));
			}
			if (getDataFimCompetencia() != null) {
				setDataFimCompetencia(Uteis.getDataUltimoDiaMes(getDataFimCompetencia()));
			}
			listaObjetos = getFacadeFactory().getContaReceberRelFacade().criarObjeto(getFiltroRelatorioFinanceiroVO(), getDataInicio(), getDataFim(), getTipoPessoa(), getAlunoMatriula(), getAlunoNome(), getFuncionarioMatricula(), getFuncionarioNome(), getCandidatoCpf(), getCandidatoNome(), getParceiroCPF(), getParceiroCNPJ(), getParceiroNome(), getSituacao(), getUnidadeEnsinoVOs(), getUnidadeEnsinoCursoVO(), getTurmaVO(), getContaCorrente(), getPessoa(), getParceiroCodigo(), getOpcaoOrdenacao(), getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema(), getCentroReceita(), getCondicaoPagamentoPlanoFinanceiroCurso().getCodigo(), getFornecedorCodigo(), getResponsavelFinanceiroCodigo(), getDataInicioCompetencia(), getDataFimCompetencia(), getConsiderarUnidadeEnsinoFinanceira());
			if (!listaObjetos.isEmpty()) {
				getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getContaReceberRelFacade().designIReportRelatorio(getTipoLayout()));
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getContaReceberRelFacade().caminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio("Relatório de Receitas (" + this.getSituacao_Apresentar() + ")");
				getSuperParametroRelVO().setListaObjetos(listaObjetos);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getContaReceberRelFacade().caminhoBaseRelatorio());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setPeriodo(String.valueOf(Uteis.getData(getDataInicio())) + "  a  " + String.valueOf(Uteis.getData(getDataFim())));
				String filtroTipoOrigem = getFiltroRelatorioFinanceiroVO().getItensFiltroTipoOrigem();
				getSuperParametroRelVO().setTipoOrigem(filtroTipoOrigem.equals("") ? "TODOS" : filtroTipoOrigem);
				tipoPessoa = "TODAS";
				if (getTipoPessoa() != null && !getTipoPessoa().equals("")) {
					getSuperParametroRelVO().adicionarParametro("tipoPessoaApresentar", TipoPessoa.getEnum(getTipoPessoa()).getDescricao());
				} else {
					getSuperParametroRelVO().adicionarParametro("tipoPessoaApresentar", "TODOS");
				}
				getSuperParametroRelVO().setTipoPessoa(tipoPessoa);
				getSuperParametroRelVO().setSituacao(getSituacao() == null ? "TODAS" : getSituacao().equals("") ? "TODAS" : getSituacao_Apresentar());
				getSuperParametroRelVO().setUnidadeEnsino(getUnidadeEnsinoVO().getNome());
				if (getUnidadeEnsinoCursoVO().getCurso().getCodigo() > 0) {
					getSuperParametroRelVO().setCurso(getUnidadeEnsinoCursoVO().getCurso().getNome());
					if (getTurmaVO().getTurno().getNome() != null && !getTurmaVO().getTurno().getNome().equals("")) {
						getSuperParametroRelVO().setCurso(getSuperParametroRelVO().getCurso() + " - " + getTurmaVO().getTurno().getNome());
					}
				} else {
					getSuperParametroRelVO().setCurso("TODOS");
				}
				if (getTurmaVO().getCodigo() != 0) {
					getSuperParametroRelVO().setTurma(getTurmaVO().getIdentificadorTurma());
				} else {
					getSuperParametroRelVO().setTurma("TODAS");
				}
				if (getContaCorrente().getCodigo() != 0) {
					setContaCorrente(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(getContaCorrente().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
					if (Uteis.isAtributoPreenchido(getContaCorrente().getNomeApresentacaoSistema())) {
						getSuperParametroRelVO().setContaCorrente(getContaCorrente().getNomeApresentacaoSistema());

					} else {
						getSuperParametroRelVO().setContaCorrente(getContaCorrente().getNumero());
					}
				} else {
					getSuperParametroRelVO().setContaCorrente("TODAS");
				}
				if (!getPlanoFinanceiroCursoVO().getCodigo().equals(0)) {
					getSuperParametroRelVO().adicionarParametro("planoFinanceiroCurso", getPlanoFinanceiroCursoVO().getDescricao());
				} else {
					getSuperParametroRelVO().adicionarParametro("planoFinanceiroCurso", "TODOS");
				}
				if (!getCondicaoPagamentoPlanoFinanceiroCurso().getCodigo().equals(0)) {
					setCondicaoPagamentoPlanoFinanceiroCurso(getFacadeFactory().getCondicaoPagamentoPlanoFinanceiroCursoFacade().consultarPorChavePrimaria(getCondicaoPagamentoPlanoFinanceiroCurso().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
					getSuperParametroRelVO().adicionarParametro("condicaoPagamento", getCondicaoPagamentoPlanoFinanceiroCurso().getDescricao());
				} else {
					getSuperParametroRelVO().adicionarParametro("condicaoPagamento", "TODOS");
				}
				if (!getCentroReceita().equals(0)) {
					getSuperParametroRelVO().adicionarParametro("centroReceita", getCentroReceitaDescricao());
				} else {
					getSuperParametroRelVO().adicionarParametro("centroReceita", "TODOS");
				}
				if (getDataInicioCompetencia() != null) {
					getSuperParametroRelVO().setDataInicioCompetencia(Uteis.getMesData(getDataInicioCompetencia()) + "/" + Uteis.getAnoData(getDataInicioCompetencia()));
				} else {
					getSuperParametroRelVO().setDataInicioCompetencia("");
				}
				if (getDataFimCompetencia() != null) {
					getSuperParametroRelVO().setDataFimCompetencia(Uteis.getMesData(getDataFimCompetencia()) + "/" + Uteis.getAnoData(getDataFimCompetencia()));
				} else {
					getSuperParametroRelVO().setDataFimCompetencia("");
				}
				getSuperParametroRelVO().adicionarParametro("considerarUnidadeEnsinoFinanceira", getConsiderarUnidadeEnsinoFinanceira());
				ordenacao = "";
				if (getOpcaoOrdenacao() == 0) {
					ordenacao = "Nome";
				} else if (getOpcaoOrdenacao() == 1) {
					ordenacao = "Data";
				} else if (getOpcaoOrdenacao() == 2) {
					ordenacao = "Tipo de Origem";
				}
				getSuperParametroRelVO().setOrdenadoPor(ordenacao);
				realizarImpressaoRelatorio();
				getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getSituacao(), ContaReceberRelControle.class.getSimpleName(), "situacao", getUsuarioLogado());
				getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getTipoLayout(), ContaReceberRelControle.class.getSimpleName(), "tipoLayout", getUsuarioLogado());
				getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getTipoPessoa(), ContaReceberRelControle.class.getSimpleName(), "tipoPessoa", getUsuarioLogado());
				getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getOpcaoOrdenacao().toString(), ContaReceberRelControle.class.getSimpleName(), "opcaoOrdenacao", getUsuarioLogado());
				getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getConsiderarUnidadeEnsinoFinanceira().toString(), ContaReceberRelControle.class.getSimpleName(), "considerarUnidadeFinanceira", getUsuarioLogado());
				getFacadeFactory().getLayoutPadraoFacade().persistirFiltroTipoOrigemContaReceber(getFiltroRelatorioFinanceiroVO(), ContaReceberRelControle.class.getSimpleName(), getUsuarioLogado());
				removerObjetoMemoria(this);
				inicializarDadosControle();
				consultarUnidadeEnsino();
				setMensagemID("msg_relatorio_ok");
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			Uteis.liberarListaMemoria(listaObjetos);
			tipoPessoa = null;
			ordenacao = null;
		}
	}

	public void consultarFuncionario() {
		try {
			List objs = new ArrayList(0);
			if (getValorConsultaFuncionario().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			getFacadeFactory().getPessoaFacade().setIdEntidade("Funcionario");
			if (getCampoConsultaFuncionario().equals("nome")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorNome(getValorConsultaFuncionario(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("matricula")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorMatricula(getValorConsultaFuncionario(), this.getUnidadeEnsinoLogado().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("nomeCidade")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeCidade(getValorConsultaFuncionario(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("CPF")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorCPF(getValorConsultaFuncionario(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("cargo")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeCargo(getValorConsultaFuncionario(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("departamento")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeDepartamento(getValorConsultaFuncionario(), "", this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("unidadeEnsino")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeUnidadeEnsino(getValorConsultaFuncionario(), "", this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			setListaConsultaFuncionario(objs);
			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			setListaConsultaFuncionario(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarAluno() {
		try {

			List objs = new ArrayList(0);
			if (getValorConsultaAluno().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			if (getCampoConsultaAluno().equals("matricula")) {
				objs.add(getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatricula(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));
			}
			if (getCampoConsultaAluno().equals("nomePessoa")) {
				objs = getFacadeFactory().getMatriculaFacade().consultarPorNomePessoa(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			}
			setListaConsultaAluno(objs);
			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			setListaConsultaAluno(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}
	
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

    public void selecionarResponsavelFinanceiro() {
        try {
            PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("responsavelFinanceiroItens");
            getListaConsultaResponsavelFinanceiro().clear();
            setResponsavelFinanceiroCPF(obj.getCPF());
            setResponsavelFinanceiroNome(obj.getNome());
            setResponsavelFinanceiroCodigo(obj.getCodigo());
            
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

	public void consultarCandidato() {
		try {
			getFacadeFactory().getPessoaFacade().setIdEntidade("Candidato");
			List objs = new ArrayList(0);
			if (getValorConsultaCandidato().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			if (getCampoConsultaCandidato().equals("codigo")) {
				int valorInt = Integer.parseInt(getValorConsultaCandidato());
				objs = getFacadeFactory().getPessoaFacade().consultarPorCodigo(new Integer(valorInt), "CA", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaCandidato().equals("nome")) {
				objs = getFacadeFactory().getPessoaFacade().consultarPorNome(getValorConsultaCandidato(), "CA", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}

			if (getCampoConsultaCandidato().equals("nomeCidade")) {
				objs = getFacadeFactory().getPessoaFacade().consultarPorNomeCidade(getValorConsultaCandidato(), "CA", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaCandidato().equals("CPF")) {
				objs = getFacadeFactory().getPessoaFacade().consultarPorCPF(getValorConsultaCandidato(), "CA", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaCandidato().equals("RG")) {
				objs = getFacadeFactory().getPessoaFacade().consultarPorRG(getValorConsultaCandidato(), "CA", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaCandidato().equals("necessidadesEspeciais")) {
				objs = getFacadeFactory().getPessoaFacade().consultarPorNecessidadesEspeciais(getValorConsultaCandidato(), "CA", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultaCandidato(objs);
			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			setListaConsultaCandidato(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP
	 * ParceiroCons.jsp. Define o tipo de consulta a ser executada, por meio de
	 * ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
	 * resultado, disponibiliza um List com os objetos selecionados na sessao da
	 * pagina.
	 */
	public void consultarParceiro() {
		try {
			super.consultar();
			List objs = new ArrayList(0);
			if (getCampoConsultaParceiro().equals("codigo")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getParceiroFacade().consultarPorCodigo(new Integer(valorInt), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaParceiro().equals("nome")) {
				objs = getFacadeFactory().getParceiroFacade().consultarPorNome(getValorConsultaParceiro(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaParceiro().equals("razaoSocial")) {
				objs = getFacadeFactory().getParceiroFacade().consultarPorRazaoSocial(getValorConsultaParceiro(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaParceiro().equals("RG")) {
				objs = getFacadeFactory().getParceiroFacade().consultarPorRG(getValorConsultaParceiro(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaParceiro().equals("CPF")) {
				objs = getFacadeFactory().getParceiroFacade().consultarPorCPF(getValorConsultaParceiro(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaParceiro().equals("tipoParceiro")) {
				objs = getFacadeFactory().getParceiroFacade().consultarPorTipoParceiro(getValorConsultaParceiro(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaParceiro(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarCentroReceita() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaCentroReceita().equals("descricao")) {
				objs = getFacadeFactory().getCentroReceitaFacade().consultarPorDescricao(getValorConsultaCentroReceita(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getCampoConsultaCentroReceita().equals("identificadorCentroReceita")) {
				objs = getFacadeFactory().getCentroReceitaFacade().consultarPorIdentificadorCentroReceita(getValorConsultaCentroReceita(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getCampoConsultaCentroReceita().equals("nomeDepartamento")) {
				objs = getFacadeFactory().getCentroReceitaFacade().consultarPorNomeDepartamento(getValorConsultaCentroReceita(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			setListaConsultaCentroReceita(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List getTipoConsultaComboCentroReceita() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("descricao", "Descrição"));
		itens.add(new SelectItem("identificadorCentroReceita", "Identificador Centro Receita"));
		itens.add(new SelectItem("nomeDepartamento", "Departamento"));
		return itens;
	}

	public List getTipoConsultaComboAluno() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("nomePessoa", "Nome Aluno"));
		return itens;
	}

	public List getTipoLayoutCombo() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("layout1", "Analítico 1"));
		itens.add(new SelectItem("layout2", "Analítico 2"));
		itens.add(new SelectItem("layout5", "Analítico Por Turma"));
		itens.add(new SelectItem("layout3", "Sintético Por Centro Receita"));

		return itens;
	}

	public List getTipoConsultaComboFuncionario() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("CPF", "CPF"));
		itens.add(new SelectItem("cargo", "Cargo"));
		itens.add(new SelectItem("departamento", "Departamento"));
		return itens;
	}

	public List getTipoConsultaComboCandidato() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("codigo", "Código"));
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("CPF", "CPF"));
		itens.add(new SelectItem("RG", "RG"));
		return itens;
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List getTipoConsultaComboParceiro() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("razaoSocial", "Razão Social"));
		itens.add(new SelectItem("RG", "RG"));
		itens.add(new SelectItem("CPF", "CPF"));
		itens.add(new SelectItem("tipoParceiro", "Tipo Parceiro"));
		return itens;
	}

	public List getTipoAgrupamento() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("ContaReceberRel", ""));
		itens.add(new SelectItem("ContaReceberResumidaPorDataRel", "Data"));
		itens.add(new SelectItem("ContaReceberResumidaPorTipoSacadoRel", "Sacado"));
		return itens;
	}

	public void selecionarFuncionario() {
		FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItens");
		setFuncionarioMatricula(obj.getMatricula());
		setFuncionarioNome(obj.getPessoa().getNome());
		setPessoa(obj.getPessoa().getCodigo());
		setCampoConsultaFuncionario("");
		setValorConsultaFuncionario("");
		setListaConsultaFuncionario(new ArrayList(0));

	}

	public void selecionarAluno() {
		MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("alunoItens");

		setAlunoMatriula(obj.getMatricula());
		setAlunoNome(obj.getAluno().getNome());
		setPessoa(obj.getAluno().getCodigo());
		setCampoConsultaAluno("");
		setValorConsultaAluno("");
		setListaConsultaAluno(new ArrayList(0));

	}

	public void selecionarCandidato() {
		PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("candidatoItens");
		setCandidatoCpf(obj.getCPF());
		setCandidatoNome(obj.getNome());
		setPessoa(obj.getCodigo());
		setCampoConsultaCandidato("");
		setValorConsultaCandidato("");
		setListaConsultaCandidato(new ArrayList(0));
	}

	public void selecionarParceiro() {
		ParceiroVO obj = (ParceiroVO) context().getExternalContext().getRequestMap().get("parceiroItens");
		setParceiroCPF(obj.getCPF());
		setParceiroCNPJ(obj.getCNPJ());
		setParceiroNome(obj.getNome());
		setParceiroCodigo(obj.getCodigo());
		Uteis.liberarListaMemoria(getListaConsultaCandidato());
		setValorConsultaCandidato("");
		setCampoConsultaCandidato("");
	}

	public void selecionarCentroReceita() {
		CentroReceitaVO obj = (CentroReceitaVO) context().getExternalContext().getRequestMap().get("centroReceitaItens");
		setCentroReceita(obj.getCodigo());
		setCentroReceitaDescricao(obj.getDescricao());
		setCampoConsultaCentroReceita("");
		setValorConsultaCentroReceita("");
		setListaConsultaCentroReceita(new ArrayList(0));
	}

	public void limparDadosAluno() {
		setAlunoMatriula("");
		setAlunoNome("");
	}

	public void limparDadosResponsavelFinanceiro() {
		setResponsavelFinanceiroNome("");
		setResponsavelFinanceiroCPF("");
	}
	
	public void limparDadosCandidato() {
		setCandidatoCpf("");
		setCandidatoNome("");
	}

	public void limparDadosParceiro() {
		setParceiroCPF("");
		setParceiroCNPJ("");
		setParceiroNome("");
	}

	public void limparDadosFuncionario() {
		setFuncionarioMatricula("");
		setFuncionarioNome("");
	}

	public void inicializarListasSelectItemTodosComboBox() {
		montarListaSelectItemOrdenacao();
		montarListaSelectItemContaCorrente();
	}

	public void montarListaSelectItemOrdenacao() {
		Vector opcoes = getFacadeFactory().getContaReceberRelFacade().getOrdenacoesRelatorio();
		Enumeration i = opcoes.elements();
		List objs = new ArrayList(0);
		int contador = 0;
		while (i.hasMoreElements()) {
			String opcao = (String) i.nextElement();
			objs.add(new SelectItem(new Integer(contador), opcao));
			contador++;
		}
		setListaSelectItemOrdenacoesRelatorio(objs);
	}

	public List getListaSelectItemAlunoFuncionarioCandidato() throws Exception {
		List objs = new ArrayList(0);
		objs.add(new SelectItem("", ""));
		Hashtable alunoFuncionarioCandidato = (Hashtable) Dominios.getAlunoFuncionarioCandidatoParceiro();
		Enumeration keys = alunoFuncionarioCandidato.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) alunoFuncionarioCandidato.get(value);
			objs.add(new SelectItem(value, label));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		return objs;
	}

	public List<SelectItem> getListaSelectItemAlunoFuncionario() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("", ""));
		itens.add(new SelectItem("AL", "Aluno"));
		itens.add(new SelectItem("FU", "Funcionário"));
		return itens;
	}

	private List<SelectItem> listaSelectItemSituacaoContaReceber;

	public List<SelectItem> getListaSelectItemSituacaoContaReceber() throws Exception {
		if (listaSelectItemSituacaoContaReceber == null) {
			listaSelectItemSituacaoContaReceber = new ArrayList<SelectItem>(0);
			listaSelectItemSituacaoContaReceber.add(new SelectItem("AR", "A Receber"));
			listaSelectItemSituacaoContaReceber.add(new SelectItem("AR/REdoMes", "A Receber/Recebido do Mês"));
			listaSelectItemSituacaoContaReceber.add(new SelectItem("REnoMes", "Recebido no Mês"));
			listaSelectItemSituacaoContaReceber.add(new SelectItem("REdoMes", "Recebido do Mês"));
			listaSelectItemSituacaoContaReceber.add(new SelectItem("NE", "Renegociadas"));

		}
		return listaSelectItemSituacaoContaReceber;
	}

	public List getListaSelectItemTipoOrigemContaReceber() throws Exception {
		return UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoOrigemContaReceber.class);
	}

	public void selecionarFornecedor() {
		FornecedorVO obj = (FornecedorVO) context().getExternalContext().getRequestMap().get("fornecedorItens");
		this.setFornecedorCodigo(obj.getCodigo());
		this.setFornecedorNome(obj.getNome());
	}

	private List<SelectItem> tipoConsultaComboFornecedor;

	public String getMascaraConsultaFornecedor() {
		if (getCampoConsultaFornecedor().equals("CPF")) {
			return "return mascara(this.form, 'formFornecedor:valorConsultaFornecedor', '99.999.999/9999-99', event);";
		} else if (getCampoConsultaFornecedor().equals("CNPJ")) {
			return "return mascara(this.form, 'formFornecedor:valorConsultaFornecedor', '999.999.999-99', event);";
		} else if (getCampoConsultaFornecedor().equals("codigo")) {
			return "return mascara(this.form, 'formFornecedor:valorConsultaFornecedor', '99999999999999', event);";
		}
		return "";
	}

	public List<SelectItem> getTipoConsultaComboFornecedor() {
		if (tipoConsultaComboFornecedor == null) {
			tipoConsultaComboFornecedor = new ArrayList<SelectItem>(0);
			tipoConsultaComboFornecedor.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboFornecedor.add(new SelectItem("razaoSocial", "Razão Social"));
			tipoConsultaComboFornecedor.add(new SelectItem("CNPJ", "CNPJ"));
			tipoConsultaComboFornecedor.add(new SelectItem("CPF", "CPF"));
			tipoConsultaComboFornecedor.add(new SelectItem("RG", "RG"));
			tipoConsultaComboFornecedor.add(new SelectItem("codigo", "codigo"));
		}
		return tipoConsultaComboFornecedor;
	}

	public void consultarFornecedor() {
		try {
			super.consultar();
			List objs = new ArrayList(0);
			if (getCampoConsultaFornecedor().equals("codigo")) {
				if (getValorConsultaFornecedor().equals("")) {
					setValorConsultaFornecedor("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaFornecedor());
				objs = getFacadeFactory().getFornecedorFacade().consultarPorCodigo(new Integer(valorInt), "AT", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFornecedor().equals("nome")) {
				objs = getFacadeFactory().getFornecedorFacade().consultarPorNome(getValorConsultaFornecedor(), "AT", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFornecedor().equals("razaoSocial")) {
				objs = getFacadeFactory().getFornecedorFacade().consultarPorRazaoSocial(getValorConsultaFornecedor(), "AT", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFornecedor().equals("RG")) {
				objs = getFacadeFactory().getFornecedorFacade().consultarPorRG(getValorConsultaFornecedor(), "AT", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFornecedor().equals("CPF")) {
				objs = getFacadeFactory().getFornecedorFacade().consultarPorCPF(getValorConsultaFornecedor(), "AT", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFornecedor().equals("CNPJ")) {
				objs = getFacadeFactory().getFornecedorFacade().consultarPorCNPJ(getValorConsultaFornecedor(), "AT", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaFornecedor(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaFornecedor(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void montarListaSelectItemContaCorrente() {
		try {
			montarListaSelectItemContaCorrente("");
		} catch (Exception e) {
			System.out.println("MENSAGEM => " + e.getMessage());
		}
	}

	public Boolean getApresentarCondicaoPagamento() {
		if (getPlanoFinanceiroCursoVO().getCodigo().equals(0)) {
			return Boolean.FALSE;
		} else {
			return Boolean.TRUE;
		}
	}

	public void montarListaSelectItemCondicaoPagamentoPlanoFinanceiroCurso() {
		try {
			montarListaSelectItemCondicaoPagamentoPlanoFinanceiroCurso("");
		} catch (Exception e) {
			//System.out.println("MENSAGEM => " + e.getMessage());
			;
		}
	}

	public void montarListaSelectItemCondicaoPagamentoPlanoFinanceiroCurso(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = getFacadeFactory().getCondicaoPagamentoPlanoFinanceiroCursoFacade().consultarPorCodigoPlanoFinanceiroCurso(getPlanoFinanceiroCursoVO().getCodigo(), "AT", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				CondicaoPagamentoPlanoFinanceiroCursoVO obj = (CondicaoPagamentoPlanoFinanceiroCursoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getDescricao().toString()));
			}
			setListaSelectItemCondicaoPagamentoPlanoFinanceiroCurso(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	public void montarListaSelectItemContaCorrente(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarContaCorrentePorNumero(prm);
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				ContaCorrenteVO obj = (ContaCorrenteVO) i.next();
				if(Uteis.isAtributoPreenchido(obj.getNomeApresentacaoSistema())){
					objs.add(new SelectItem(obj.getCodigo(), obj.getNomeApresentacaoSistema()));
				}else{
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

	public List consultarContaCorrentePorNumero(String numeroPrm) throws Exception {
		List lista = getFacadeFactory().getContaCorrenteFacade().consultarPorNumero(numeroPrm, getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
		return lista;
	}

	public void consultarCurso() {
		try {
			List objs = new ArrayList(0);
			if (getValorConsultaCurso().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			if (getCampoConsultaCurso().equals("codigo")) {
				if (getValorConsultaCurso().equals("")) {
					setValorConsultaCurso("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaCurso());
				objs = getFacadeFactory().getCursoFacade().consultaRapidaPorCodigoCursoUnidadeEnsino(new Integer(valorInt), getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			if (getCampoConsultaCurso().equals("nome")) {
				objs = getFacadeFactory().getCursoFacade().consultaRapidaPorNomeCursoUnidadeEnsino(getValorConsultaCurso(), getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaCurso(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarCurso() throws Exception {
		CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItens");
		getUnidadeEnsinoCursoVO().setCurso(obj);
		setTurmaVO(new TurmaVO());
		setCampoConsultaCurso("");
		setValorConsultaCurso("");
		setListaConsultaCurso(new ArrayList(0));
	}

	public void limparDadosCurso() {
		setUnidadeEnsinoCursoVO(new UnidadeEnsinoCursoVO());
	}

	public List<SelectItem> getTipoConsultaComboCurso() {
		List<SelectItem> itens = new ArrayList<SelectItem>();
		itens.add(new SelectItem("nome", "Nome Curso"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	public void limparDadosTurmaCurso() {
		setTurmaVO(new TurmaVO());
		setUnidadeEnsinoCursoVO(new UnidadeEnsinoCursoVO());
	}

	public void consultarTurma() {
		try {
			List objs = new ArrayList(0);
			if (getValorConsultaTurma().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			if (getCampoConsultaTurma().equals("codigo")) {
				if (getValorConsultaTurma().equals("")) {
					setValorConsultaCurso("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaTurma());
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorCodigoTurmaCursoUnidadeEnsino(new Integer(valorInt), getUnidadeEnsinoCursoVO().getCurso().getCodigo(), getUnidadeEnsinoVO().getCodigo(), getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaUnidadeEnsinoCursoTurno(getValorConsultaTurma(), getUnidadeEnsinoVO().getCodigo(), getUnidadeEnsinoCursoVO().getCurso().getCodigo(), getUnidadeEnsinoCursoVO().getTurno().getCodigo(), false, getUsuarioLogado());

			}
			setListaConsultaTurma(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaTurma(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparIdentificador() {
		setTurmaVO(null);
	}

	public void limparCentroReceita() {
		setCentroReceita(0);
		setCentroReceitaDescricao("");
	}

	public void limparPlanoFinanceiroCurso() {
		setPlanoFinanceiroCursoVO(new PlanoFinanceiroCursoVO());
	}

	public void selecionarTurma() throws Exception {
		try {
			TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
			setTurmaVO(obj);
			obj = null;
			valorConsultaTurma = "";
			campoConsultaTurma = "";
			listaConsultaTurma.clear();
		} catch (Exception e) {
			setTurmaVO(new TurmaVO());
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void montarTurma() throws Exception {
		try {
			if (!getTurmaVO().getIdentificadorTurma().equals("")) {
				setTurmaVO(getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(getTurmaVO(), getTurmaVO().getIdentificadorTurma(), getUnidadeEnsinoCursoVO().getCurso().getCodigo(), getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado()));
			} else {
				throw new Exception("Informe a Turma.");
			}
			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			setTurmaVO(null);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List getTipoConsultaComboTurma() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("identificadorTurma", "Identificador"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	public String getCampoConsultaAluno() {
		return campoConsultaAluno;
	}

	public void setCampoConsultaAluno(String campoConsultaAluno) {
		this.campoConsultaAluno = campoConsultaAluno;
	}

	public String getCampoConsultaCandidato() {
		return campoConsultaCandidato;
	}

	public void setCampoConsultaCandidato(String campoConsultaCandidato) {
		this.campoConsultaCandidato = campoConsultaCandidato;
	}

	public String getCampoConsultaCentroReceita() {
		return campoConsultaCentroReceita;
	}

	public void setCampoConsultaCentroReceita(String campoConsultaCentroReceita) {
		this.campoConsultaCentroReceita = campoConsultaCentroReceita;
	}

	public String getCampoConsultaFuncionario() {
		return campoConsultaFuncionario;
	}

	public void setCampoConsultaFuncionario(String campoConsultaFuncionario) {
		this.campoConsultaFuncionario = campoConsultaFuncionario;
	}

	public List getListaConsultaAluno() {
		return listaConsultaAluno;
	}

	public void setListaConsultaAluno(List listaConsultaAluno) {
		this.listaConsultaAluno = listaConsultaAluno;
	}

	public List getListaConsultaCandidato() {
		return listaConsultaCandidato;
	}

	public void setListaConsultaCandidato(List listaConsultaCandidato) {
		this.listaConsultaCandidato = listaConsultaCandidato;
	}

	public List getListaConsultaCentroReceita() {
		return listaConsultaCentroReceita;
	}

	public void setListaConsultaCentroReceita(List listaConsultaCentroReceita) {
		this.listaConsultaCentroReceita = listaConsultaCentroReceita;
	}

	public List getListaConsultaFuncionario() {
		return listaConsultaFuncionario;
	}

	public void setListaConsultaFuncionario(List listaConsultaFuncionario) {
		this.listaConsultaFuncionario = listaConsultaFuncionario;
	}

	public List getListaSelectItemFuncionario() {
		return listaSelectItemFuncionario;
	}

	public void setListaSelectItemFuncionario(List listaSelectItemFuncionario) {
		this.listaSelectItemFuncionario = listaSelectItemFuncionario;
	}

	public List getListaSelectItemMatriculaAluno() {
		return listaSelectItemMatriculaAluno;
	}

	public void setListaSelectItemMatriculaAluno(List listaSelectItemMatriculaAluno) {
		this.listaSelectItemMatriculaAluno = listaSelectItemMatriculaAluno;
	}

	public String getValorConsultaAluno() {
		return valorConsultaAluno;
	}

	public void setValorConsultaAluno(String valorConsultaAluno) {
		this.valorConsultaAluno = valorConsultaAluno;
	}

	public String getValorConsultaCandidato() {
		return valorConsultaCandidato;
	}

	public void setValorConsultaCandidato(String valorConsultaCandidato) {
		this.valorConsultaCandidato = valorConsultaCandidato;
	}

	public String getValorConsultaCentroReceita() {
		return valorConsultaCentroReceita;
	}

	public void setValorConsultaCentroReceita(String valorConsultaCentroReceita) {
		this.valorConsultaCentroReceita = valorConsultaCentroReceita;
	}

	public String getValorConsultaFuncionario() {
		return valorConsultaFuncionario;
	}

	public void setValorConsultaFuncionario(String valorConsultaFuncionario) {
		this.valorConsultaFuncionario = valorConsultaFuncionario;
	}

	public Boolean getAluno() {
		return getTipoPessoa().equals(TipoPessoa.ALUNO.getValor());
	}

	public Boolean getResponsavelFinanceiro() {
		return getTipoPessoa().equals(TipoPessoa.RESPONSAVEL_FINANCEIRO.getValor());
	}

	public Boolean getCandidato() {
		return getTipoPessoa().equals(TipoPessoa.CANDIDATO.getValor());
	}

	public Boolean getFuncionario() {
		return getTipoPessoa().equals(TipoPessoa.FUNCIONARIO.getValor());
	}

	/**
	 * @return the listaConsultaParceiro
	 */
	public List getListaConsultaParceiro() {
		return listaConsultaParceiro;
	}

	/**
	 * @param listaConsultaParceiro
	 *            the listaConsultaParceiro to set
	 */
	public void setListaConsultaParceiro(List listaConsultaParceiro) {
		this.listaConsultaParceiro = listaConsultaParceiro;
	}

	/**
	 * @return the valorConsultaParceiro
	 */
	public String getValorConsultaParceiro() {
		return valorConsultaParceiro;
	}

	/**
	 * @param valorConsultaParceiro
	 *            the valorConsultaParceiro to set
	 */
	public void setValorConsultaParceiro(String valorConsultaParceiro) {
		this.valorConsultaParceiro = valorConsultaParceiro;
	}

	/**
	 * @return the campoConsultaParceiro
	 */
	public String getCampoConsultaParceiro() {
		return campoConsultaParceiro;
	}

	/**
	 * @param campoConsultaParceiro
	 *            the campoConsultaParceiro to set
	 */
	public void setCampoConsultaParceiro(String campoConsultaParceiro) {
		this.campoConsultaParceiro = campoConsultaParceiro;
	}

	/**
	 * @return the parceiro
	 */
	public Boolean getParceiro() {
		return getTipoPessoa().equals(TipoPessoa.PARCEIRO.getValor());
	}

	/**
	 * @param parceiro
	 *            the parceiro to set
	 */
	public void setParceiro(Boolean parceiro) {
		this.parceiro = parceiro;
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

	public List getListaSelectItemCurso() {
		if (listaSelectItemCurso == null) {
			listaSelectItemCurso = new ArrayList(0);
		}
		return listaSelectItemCurso;
	}

	public void setListaSelectItemCurso(List listaSelectItemCurso) {
		this.listaSelectItemCurso = listaSelectItemCurso;
	}

	public List getListaSelectItemTurma() {
		if (listaSelectItemTurma == null) {
			listaSelectItemTurma = new ArrayList(0);
		}
		return listaSelectItemTurma;
	}

	public void setListaSelectItemTurma(List listaSelectItemTurma) {
		this.listaSelectItemTurma = listaSelectItemTurma;
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

	public Date getDataFim() {
		if (dataFim == null) {
			dataFim = new Date();
		}
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public String getTipoPessoa() {
		if (tipoPessoa == null) {
			tipoPessoa = "";
		}
		return tipoPessoa;
	}

	public void setTipoPessoa(String tipoPessoa) {
		this.tipoPessoa = tipoPessoa;
	}

	public String getAlunoMatriula() {
		if (alunoMatriula == null) {
			alunoMatriula = "";
		}
		return alunoMatriula;
	}

	public void setAlunoMatriula(String alunoMatriula) {
		this.alunoMatriula = alunoMatriula;
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

	public String getFuncionarioMatricula() {
		if (funcionarioMatricula == null) {
			funcionarioMatricula = "";
		}
		return funcionarioMatricula;
	}

	public void setFuncionarioMatricula(String funcionarioMatricula) {
		this.funcionarioMatricula = funcionarioMatricula;
	}

	public String getFuncionarioNome() {
		if (funcionarioNome == null) {
			funcionarioNome = "";
		}
		return funcionarioNome;
	}

	public void setFuncionarioNome(String funcionarioNome) {
		this.funcionarioNome = funcionarioNome;
	}

	public String getCandidatoCpf() {
		if (candidatoCpf == null) {
			candidatoCpf = "";
		}
		return candidatoCpf;
	}

	public void setCandidatoCpf(String candidatoCpf) {
		this.candidatoCpf = candidatoCpf;
	}

	public String getCandidatoNome() {
		if (candidatoNome == null) {
			candidatoNome = "";
		}
		return candidatoNome;
	}

	public void setCandidatoNome(String candidatoNome) {
		this.candidatoNome = candidatoNome;
	}

	public String getParceiroCPF() {
		if (parceiroCPF == null) {
			parceiroCPF = "";
		}
		return parceiroCPF;
	}

	public void setParceiroCPF(String parceiroCPF) {
		this.parceiroCPF = parceiroCPF;
	}

	public String getParceiroCNPJ() {
		if (parceiroCNPJ == null) {
			parceiroCNPJ = "";
		}
		return parceiroCNPJ;
	}

	public void setParceiroCNPJ(String parceiroCNPJ) {
		this.parceiroCNPJ = parceiroCNPJ;
	}

	public String getParceiroNome() {
		if (parceiroNome == null) {
			parceiroNome = "";
		}
		return parceiroNome;
	}

	public void setParceiroNome(String parceiroNome) {
		this.parceiroNome = parceiroNome;
	}

	public String getSituacao() {
		if (situacao == null) {
			situacao = "AR";
		}
		return situacao;
	}

	public String getSituacao_Apresentar() {

		if (getSituacao().equals("AR")) {
			return "Contas a Receber";
		}
		if (getSituacao().equals("NE")) {
			return "Contas NEGOCIADAS";
		}
		if (getSituacao().equals("AR/REdoMes")) {
			return "Contas a Receber/Recebidas do Mês";
		}
		if (getSituacao().equals("REnoMes")) {
			return "Contas Recebidas no Mês";
		}
		if (getSituacao().equals("REdoMes")) {
			return "Contas Recebidas do Mês";
		}
		return SituacaoContaReceber.getDescricao(situacao);
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public UnidadeEnsinoCursoVO getUnidadeEnsinoCursoVO() {
		if (unidadeEnsinoCursoVO == null) {
			unidadeEnsinoCursoVO = new UnidadeEnsinoCursoVO();
		}
		return unidadeEnsinoCursoVO;
	}

	public void setUnidadeEnsinoCursoVO(UnidadeEnsinoCursoVO unidadeEnsinoCursoVO) {
		this.unidadeEnsinoCursoVO = unidadeEnsinoCursoVO;
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

	public ContaCorrenteVO getContaCorrente() {
		if (contaCorrente == null) {
			contaCorrente = new ContaCorrenteVO();
		}
		return contaCorrente;
	}

	public void setContaCorrente(ContaCorrenteVO contaCorrente) {
		this.contaCorrente = contaCorrente;
	}

	public Integer getPessoa() {
		if (pessoa == null) {
			pessoa = 0;
		}
		return pessoa;
	}

	public void setPessoa(Integer pessoa) {
		this.pessoa = pessoa;
	}

	public Integer getParceiroCodigo() {
		if (parceiroCodigo == null) {
			parceiroCodigo = 0;
		}
		return parceiroCodigo;
	}

	public void setParceiroCodigo(Integer parceiroCodigo) {
		this.parceiroCodigo = parceiroCodigo;
	}

	public Integer getCentroReceita() {
		if (centroReceita == null) {
			centroReceita = new Integer(0);
		}
		return centroReceita;
	}

	public void setCentroReceita(Integer centroReceita) {
		this.centroReceita = centroReceita;
	}

	public String getCentroReceitaDescricao() {
		if (centroReceitaDescricao == null) {
			centroReceitaDescricao = "";
		}
		return centroReceitaDescricao;
	}

	public void setCentroReceitaDescricao(String centroReceitaDescricao) {
		this.centroReceitaDescricao = centroReceitaDescricao;
	}

	public String getNomeRelatorio() {
		return nomeRelatorio;
	}

	public void setNomeRelatorio(String aNomeRelatorio) {
		nomeRelatorio = aNomeRelatorio;
	}

	/**
	 * @return the unidadeEnsino
	 */
	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if (unidadeEnsinoVO == null) {
			if (getUnidadeEnsinoLogado().getCodigo() > 0) {
				unidadeEnsinoVO = getUnidadeEnsinoLogado();
			} else {
				unidadeEnsinoVO = new UnidadeEnsinoVO();
			}
		}
		return unidadeEnsinoVO;
	}

	/**
	 * @param unidadeEnsino
	 *            the unidadeEnsino to set
	 */
	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
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

	public List getListaConsultaCurso() {
		if (listaConsultaCurso == null) {
			listaConsultaCurso = new ArrayList(0);
		}
		return listaConsultaCurso;
	}

	public void setListaConsultaCurso(List listaConsultaCurso) {
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

	public String getCampoConsultaTurma() {
		if (campoConsultaTurma == null) {
			campoConsultaTurma = "";
		}
		return campoConsultaTurma;
	}

	public void setCampoConsultaTurma(String campoConsultaTurma) {
		this.campoConsultaTurma = campoConsultaTurma;
	}

	public List getListaConsultaTurma() {
		if (listaConsultaTurma == null) {
			listaConsultaTurma = new ArrayList(0);
		}
		return listaConsultaTurma;
	}

	public void setListaConsultaTurma(List listaConsultaTurma) {
		this.listaConsultaTurma = listaConsultaTurma;
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

	public String getTipoLayout() {
		if (tipoLayout == null) {
			tipoLayout = "";
		}
		return tipoLayout;
	}

	public void setTipoLayout(String tipoLayout) {
		this.tipoLayout = tipoLayout;
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


	public List getTipoConsultaComboPlanoFinanceiroCurso() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("descricao", "Descrição"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	public void consultarPlanoFinanceiroCurso() {
		List objs = new ArrayList(0);
		try {
			if (getCampoConsultaPlanoFinanceiroCurso().equals("descricao")) {
				objs = getFacadeFactory().getPlanoFinanceiroCursoFacade().consultarPorDescricao(getValorConsultaPlanoFinanceiroCurso(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaPlanoFinanceiroCurso().equals("codigo")) {
				Integer codigoBuscar = 0;
				try {
					codigoBuscar = Integer.valueOf(getValorConsultaPlanoFinanceiroCurso());
				} catch (Exception e) {
					codigoBuscar = 0;
				}
				objs = getFacadeFactory().getPlanoFinanceiroCursoFacade().consultarPorCodigo(codigoBuscar, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaPlanoFinanceiroCurso(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			getListaConsulta().clear();
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			objs = null;
		}
	}

	public void selecionarPlanoFinanceiroCurso() {
		PlanoFinanceiroCursoVO obj = (PlanoFinanceiroCursoVO) context().getExternalContext().getRequestMap().get("planoFinanceiroCursoItens");
		setPlanoFinanceiroCursoVO(obj);
		setCampoConsultaPlanoFinanceiroCurso("");
		setValorConsultaPlanoFinanceiroCurso("");
		setListaConsultaPlanoFinanceiroCurso(new ArrayList(0));
		montarListaSelectItemCondicaoPagamentoPlanoFinanceiroCurso();
	}

	/**
	 * @return the listaConsultaPlanoFinanceiroCurso
	 */
	public List getListaConsultaPlanoFinanceiroCurso() {
		return listaConsultaPlanoFinanceiroCurso;
	}

	/**
	 * @param listaConsultaPlanoFinanceiroCurso
	 *            the listaConsultaPlanoFinanceiroCurso to set
	 */
	public void setListaConsultaPlanoFinanceiroCurso(List listaConsultaPlanoFinanceiroCurso) {
		this.listaConsultaPlanoFinanceiroCurso = listaConsultaPlanoFinanceiroCurso;
	}

	/**
	 * @return the valorConsultaPlanoFinanceiroCurso
	 */
	public String getValorConsultaPlanoFinanceiroCurso() {
		return valorConsultaPlanoFinanceiroCurso;
	}

	/**
	 * @param valorConsultaPlanoFinanceiroCurso
	 *            the valorConsultaPlanoFinanceiroCurso to set
	 */
	public void setValorConsultaPlanoFinanceiroCurso(String valorConsultaPlanoFinanceiroCurso) {
		this.valorConsultaPlanoFinanceiroCurso = valorConsultaPlanoFinanceiroCurso;
	}

	/**
	 * @return the campoConsultaPlanoFinanceiroCurso
	 */
	public String getCampoConsultaPlanoFinanceiroCurso() {
		return campoConsultaPlanoFinanceiroCurso;
	}

	/**
	 * @param campoConsultaPlanoFinanceiroCurso
	 *            the campoConsultaPlanoFinanceiroCurso to set
	 */
	public void setCampoConsultaPlanoFinanceiroCurso(String campoConsultaPlanoFinanceiroCurso) {
		this.campoConsultaPlanoFinanceiroCurso = campoConsultaPlanoFinanceiroCurso;
	}

	/**
	 * @return the planoFinanceiroCursoVO
	 */
	public PlanoFinanceiroCursoVO getPlanoFinanceiroCursoVO() {
		if (planoFinanceiroCursoVO == null) {
			planoFinanceiroCursoVO = new PlanoFinanceiroCursoVO();
		}
		return planoFinanceiroCursoVO;
	}

	/**
	 * @param planoFinanceiroCursoVO
	 *            the planoFinanceiroCursoVO to set
	 */
	public void setPlanoFinanceiroCursoVO(PlanoFinanceiroCursoVO planoFinanceiroCursoVO) {
		this.planoFinanceiroCursoVO = planoFinanceiroCursoVO;
	}

	/**
	 * @return the listaSelectItemCondicaoPagamentoPlanoFinanceiroCurso
	 */
	public List getListaSelectItemCondicaoPagamentoPlanoFinanceiroCurso() {
		if (listaSelectItemCondicaoPagamentoPlanoFinanceiroCurso == null) {
			listaSelectItemCondicaoPagamentoPlanoFinanceiroCurso = new ArrayList();
		}
		return listaSelectItemCondicaoPagamentoPlanoFinanceiroCurso;
	}

	/**
	 * @param listaSelectItemCondicaoPagamentoPlanoFinanceiroCurso
	 *            the listaSelectItemCondicaoPagamentoPlanoFinanceiroCurso to
	 *            set
	 */
	public void setListaSelectItemCondicaoPagamentoPlanoFinanceiroCurso(List listaSelectItemCondicaoPagamentoPlanoFinanceiroCurso) {
		this.listaSelectItemCondicaoPagamentoPlanoFinanceiroCurso = listaSelectItemCondicaoPagamentoPlanoFinanceiroCurso;
	}

	/**
	 * @return the condicaoPagamentoPlanoFinanceiroCurso
	 */
	public CondicaoPagamentoPlanoFinanceiroCursoVO getCondicaoPagamentoPlanoFinanceiroCurso() {
		if (condicaoPagamentoPlanoFinanceiroCurso == null) {
			condicaoPagamentoPlanoFinanceiroCurso = new CondicaoPagamentoPlanoFinanceiroCursoVO();
		}
		return condicaoPagamentoPlanoFinanceiroCurso;
	}

	/**
	 * @param condicaoPagamentoPlanoFinanceiroCurso
	 *            the condicaoPagamentoPlanoFinanceiroCurso to set
	 */
	public void setCondicaoPagamentoPlanoFinanceiroCurso(CondicaoPagamentoPlanoFinanceiroCursoVO condicaoPagamentoPlanoFinanceiroCurso) {
		this.condicaoPagamentoPlanoFinanceiroCurso = condicaoPagamentoPlanoFinanceiroCurso;
	}

	public boolean getApresentaPlanoFinanceiroAluno() {
		return getTipoPessoa().equals("AL");
	}

	public List<FornecedorVO> getListaConsultaFornecedor() {
		if (listaConsultaFornecedor == null) {
			listaConsultaFornecedor = new ArrayList<FornecedorVO>(0);
		}
		return listaConsultaFornecedor;
	}

	public void setListaConsultaFornecedor(List<FornecedorVO> listaConsultaFornecedor) {
		this.listaConsultaFornecedor = listaConsultaFornecedor;
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

	public String getCampoConsultaFornecedor() {
		if (campoConsultaFornecedor == null) {
			campoConsultaFornecedor = "";
		}
		return campoConsultaFornecedor;
	}

	public void setCampoConsultaFornecedor(String campoConsultaFornecedor) {
		this.campoConsultaFornecedor = campoConsultaFornecedor;
	}

	public String getFornecedorNome() {
		return fornecedorNome;
	}

	public void setFornecedorNome(String fornecedorNome) {
		this.fornecedorNome = fornecedorNome;
	}

	public Integer getFornecedorCodigo() {
		return fornecedorCodigo;
	}

	public void setFornecedorCodigo(Integer fornecedorCodigo) {
		this.fornecedorCodigo = fornecedorCodigo;
	}

	public Boolean getFornecedor() {
		return getTipoPessoa().equals(TipoPessoa.FORNECEDOR.getValor());
	}

	@PostConstruct
	public void consultarUnidadeEnsino() {
		try {
			consultarUnidadeEnsinoFiltroRelatorio("ContaReceberRel");
			verificarTodasUnidadesSelecionadas();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void verificarTodasUnidadesSelecionadas() {
		StringBuilder unidade = new StringBuilder();
		if (getUnidadeEnsinoVOs().size() > 1) {
			for (UnidadeEnsinoVO obj : getUnidadeEnsinoVOs()) {
				if (obj.getFiltrarUnidadeEnsino()) {
					unidade.append(obj.getNome()).append("; ");
				} 
			}
			getUnidadeEnsinoVO().setNome(unidade.toString());
		} else {
			if (!getUnidadeEnsinoVOs().isEmpty()) {
				if (getUnidadeEnsinoVOs().get(0).getFiltrarUnidadeEnsino()) {
					getUnidadeEnsinoVO().setNome(getUnidadeEnsinoVOs().get(0).getNome());
				}
			} else {
				getUnidadeEnsinoVO().setNome(unidade.toString());
			}
		}
	}

	public void marcarTodasUnidadesEnsinoAction() {
		for (UnidadeEnsinoVO unidade : getUnidadeEnsinoVOs()) {
			if (getMarcarTodasUnidadeEnsino()) {
				unidade.setFiltrarUnidadeEnsino(Boolean.TRUE);
			} else {
				unidade.setFiltrarUnidadeEnsino(Boolean.FALSE);
			}
		}
		verificarTodasUnidadesSelecionadas();
	}

	public FiltroRelatorioFinanceiroVO getFiltroRelatorioFinanceiroVO() {
		if (filtroRelatorioFinanceiroVO == null) {
			filtroRelatorioFinanceiroVO = new FiltroRelatorioFinanceiroVO(getLoginControle().getPermissaoAcessoMenuVO().getPermitirApenasContasDaBiblioteca());
		}
		return filtroRelatorioFinanceiroVO;
	}

	public void setFiltroRelatorioFinanceiroVO(FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO) {
		this.filtroRelatorioFinanceiroVO = filtroRelatorioFinanceiroVO;
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

	public String getValorConsultaResponsavelFinanceiro() {
		if (valorConsultaResponsavelFinanceiro == null) {
			valorConsultaResponsavelFinanceiro = "";
		}
		return valorConsultaResponsavelFinanceiro;
	}

	public void setValorConsultaResponsavelFinanceiro(String valorConsultaResponsavelFinanceiro) {
		this.valorConsultaResponsavelFinanceiro = valorConsultaResponsavelFinanceiro;
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

	public Integer getResponsavelFinanceiroCodigo() {
		if (responsavelFinanceiroCodigo == null) {
			responsavelFinanceiroCodigo = 0;
		}
		return responsavelFinanceiroCodigo;
	}

	public void setResponsavelFinanceiroCodigo(Integer responsavelFinanceiroCodigo) {
		this.responsavelFinanceiroCodigo = responsavelFinanceiroCodigo;
	}

	public String getResponsavelFinanceiroNome() {
		if (responsavelFinanceiroNome == null) {
			responsavelFinanceiroNome = "";
		}
		return responsavelFinanceiroNome;
	}

	public void setResponsavelFinanceiroNome(String responsavelFinanceiroNome) {
		this.responsavelFinanceiroNome = responsavelFinanceiroNome;
	}

	public String getResponsavelFinanceiroCPF() {
		if (responsavelFinanceiroCPF == null) {
			responsavelFinanceiroCPF = "";
		}
		return responsavelFinanceiroCPF;
	}

	public void setResponsavelFinanceiroCPF(String responsavelFinanceiroCPF) {
		this.responsavelFinanceiroCPF = responsavelFinanceiroCPF;
	}
	
		public Date getDataInicioCompetencia() {
		return dataInicioCompetencia;
	}

	public void setDataInicioCompetencia(Date dataInicioCompetencia) {
		this.dataInicioCompetencia = dataInicioCompetencia;
	}

	public Date getDataFimCompetencia() {
		return dataFimCompetencia;
	}

	public void setDataFimCompetencia(Date dataFimCompetencia) {
		this.dataFimCompetencia = dataFimCompetencia;
	}

	public Boolean getConsiderarUnidadeEnsinoFinanceira() {
		if (considerarUnidadeEnsinoFinanceira == null)
			considerarUnidadeEnsinoFinanceira = false;
		return considerarUnidadeEnsinoFinanceira;
	}

	public void setConsiderarUnidadeEnsinoFinanceira(Boolean considerarUnidadeEnsinoFinanceira) {
		this.considerarUnidadeEnsinoFinanceira = considerarUnidadeEnsinoFinanceira;
	}

}