package relatorio.controle.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

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
import negocio.comuns.protocolo.TipoRequerimentoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.SituacaoContaReceber;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.financeiro.ContaRecebidaVersoContaReceberRelVO;
import relatorio.negocio.comuns.financeiro.FiltroRelatorioFinanceiroVO;
import relatorio.negocio.jdbc.financeiro.ContaRecebidaVersoContaReceberRel;

@Controller("ContaRecebidaVersoContaReceberRelControle")
@Lazy
@Scope("viewScope")
public class ContaRecebidaVersoContaReceberRelControle extends SuperControleRelatorio {

	private static final long serialVersionUID = -1752390307058253365L;
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
	private List<SelectItem> listaSelectItemCondicaoPagamentoPlanoFinanceiroCurso;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	protected Integer pessoa;
	private Integer parceiroCodigo;
	private Integer fornecedorCodigo;
	protected Integer centroReceita;
	protected String centroReceitaDescricao;
	private static String nomeRelatorio;

	protected List<FuncionarioVO> listaConsultaFuncionario;
	protected String valorConsultaFuncionario;
	protected String campoConsultaFuncionario;
	protected List<MatriculaVO> listaConsultaAluno;
	protected String valorConsultaAluno;
	protected String campoConsultaAluno;
	protected List<CursoVO> listaConsultaCurso;
	protected String valorConsultaCurso;
	protected String campoConsultaCurso;
	protected List<TurmaVO> listaConsultaTurma;
	protected String valorConsultaTurma;
	protected String campoConsultaTurma;
	protected List<PessoaVO> listaConsultaCandidato;
	protected String valorConsultaCandidato;
	protected String campoConsultaCandidato;
	protected List<ParceiroVO> listaConsultaParceiro;
	protected String valorConsultaParceiro;
	protected String campoConsultaParceiro;
	protected List<CentroReceitaVO> listaConsultaCentroReceita;
	protected String valorConsultaCentroReceita;
	protected String campoConsultaCentroReceita;
	private List<PlanoFinanceiroCursoVO> listaConsultaPlanoFinanceiroCurso;
	private String valorConsultaPlanoFinanceiroCurso;
	private String campoConsultaPlanoFinanceiroCurso;
	private PlanoFinanceiroCursoVO planoFinanceiroCursoVO;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	protected List<SelectItem> listaSelectItemContaCorrente;
	private Boolean gerarRelatorio;
	protected List<FornecedorVO> listaConsultaFornecedor;
	protected String valorConsultaFornecedor;
	protected String campoConsultaFornecedor;
	private Boolean filtrarContasRecebidasDataRecebimento;
	private String ordenarPor;
	private FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO;
	protected List<TipoRequerimentoVO> listaConsultaTipoRequerimento;
	protected String valorConsultaTipoRequerimento;
	protected String campoConsultaTipoRequerimento;
	private TipoRequerimentoVO tipoRequerimentoVO;
	private Boolean filtrarContasRegistroCobranca;

	private Boolean considerarUnidadeEnsinoFinanceira;
	private Date dataInicioAux;
	private Date dataFimAux; 
	
	public ContaRecebidaVersoContaReceberRelControle() throws Exception {
		inicializarDadosControle();
		setMensagemID("msg_entre_prmrelatorio");
		montarDadosSalvosEmissaoUltimoRelatorio();
	}

	private void inicializarDadosControle() {
		setListaConsultaAluno(new ArrayList<MatriculaVO>(0));
		inicializarListasSelectItemTodosComboBox();
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
		realizarCriacaoRelatorio(TipoRelatorioEnum.EXCEL);
	}

	public void realizarCriacaoRelatorio(TipoRelatorioEnum tipoRelatorioEnum) {
		List<ContaRecebidaVersoContaReceberRelVO> listaObjetos = null;
		String tipoPessoa = null;
		try {
			if (getFiltroRelatorioFinanceiroVO().getFiltrarPorDataCompetencia()) {
				setDataInicio(Uteis.getDataPrimeiroDiaMes(getDataInicio()));
				setDataFim(Uteis.getDataUltimoDiaMes(getDataFim()));
			}
			listaObjetos = getFacadeFactory().getContaRecebidaVersoContaReceberRelFacade().realizarGeracaoRelatorio(getUnidadeEnsinoVOs(), getDataInicio(), getDataFim(), getUnidadeEnsinoCursoVO().getCurso().getCodigo(), getTurmaVO().getCodigo(), getTipoPessoa(), getAlunoMatriula(), getPessoa(), getPessoa(), getParceiroCodigo(), getFornecedorCodigo(), getPessoa(), getPessoa(), getContaCorrente().getCodigo(), getCentroReceita(), getPlanoFinanceiroCursoVO().getCodigo(), getCondicaoPagamentoPlanoFinanceiroCurso().getCodigo(), getFiltroRelatorioFinanceiroVO(), getFiltrarContasRecebidasDataRecebimento(), getFiltrarContasRegistroCobranca(), getOrdenarPor(), getTipoRequerimentoVO().getCodigo(), getUsuarioLogado(), getConsiderarUnidadeEnsinoFinanceira());
			if (!listaObjetos.isEmpty()) {
				getSuperParametroRelVO().setNomeDesignIreport(ContaRecebidaVersoContaReceberRel.getDesignIReportRelatorio());
				getSuperParametroRelVO().setTipoRelatorioEnum(tipoRelatorioEnum);
				getSuperParametroRelVO().setSubReport_Dir(ContaRecebidaVersoContaReceberRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio(UteisJSF.internacionalizar("prt_ContaRecebidaVersoContaReceber_tituloForm"));
				getSuperParametroRelVO().setListaObjetos(listaObjetos);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(ContaRecebidaVersoContaReceberRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setPeriodo(String.valueOf(Uteis.getData(getDataInicio())) + "  a  " + String.valueOf(Uteis.getData(getDataFim())));
				getSuperParametroRelVO().setTipoOrigem(getFiltroRelatorioFinanceiroVO().getItensFiltroTipoOrigem());
				getSuperParametroRelVO().adicionarParametro("considerarUnidadeEnsinoFinanceira", getConsiderarUnidadeEnsinoFinanceira());
				
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
					if(Uteis.isAtributoPreenchido(getContaCorrente().getNomeApresentacaoSistema())){
						getSuperParametroRelVO().setContaCorrente(getContaCorrente().getNomeApresentacaoSistema());
					}else{
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
				getSuperParametroRelVO().adicionarParametro("apenasRegistradaCobranca", getFiltrarContasRegistroCobranca());
				getSuperParametroRelVO().setOrdenadoPor(getOrdenarPor().equals("sacado") ? "Nome" : "Data Vencimento");
				gravarDadosPadroesEmissaoRelatorio();
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
			Uteis.liberarListaMemoria(listaObjetos);
			tipoPessoa = null;
			montarDadosSalvosEmissaoUltimoRelatorio();
		}
	}

	public void imprimirPDF() {
		realizarCriacaoRelatorio(TipoRelatorioEnum.PDF);
	}

	public void consultarTipoRequerimento() {
		try {
			List<TipoRequerimentoVO> objs = new ArrayList<TipoRequerimentoVO>(0);
			if (getValorConsultaTipoRequerimento().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			if (getCampoConsultaTipoRequerimento().equals("nome")) {
				objs = getFacadeFactory().getTipoRequerimentoFacade().consultarPorNome(getValorConsultaTipoRequerimento(), "TO", getUnidadeEnsinoLogado().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, false, getUsuarioLogado());
			}else if(getCampoConsultaTipoRequerimento().equals("codigo")){
				objs.add(getFacadeFactory().getTipoRequerimentoFacade().consultarPorChavePrimaria(Integer.parseInt(getValorConsultaTipoRequerimento()), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			}
			setListaConsultaTipoRequerimento(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void consultarFuncionario() {
		try {
			List<FuncionarioVO> objs = new ArrayList<FuncionarioVO>(0);
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
			setListaConsultaFuncionario(new ArrayList<FuncionarioVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarAluno() {
		try {

			List<MatriculaVO> objs = new ArrayList<MatriculaVO>(0);
			if (getValorConsultaAluno().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			if (getCampoConsultaAluno().equals("matricula")) {
				MatriculaVO matriculaVO = getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatricula(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
				if (!matriculaVO.getMatricula().trim().isEmpty()) {
					objs.add(matriculaVO);
				}
			}
			if (getCampoConsultaAluno().equals("nomePessoa")) {
				objs = getFacadeFactory().getMatriculaFacade().consultarPorNomePessoa(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("nomeCurso")) {
				objs = getFacadeFactory().getMatriculaFacade().consultarPorNomeCurso(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			}
			setListaConsultaAluno(objs);
			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			setListaConsultaAluno(new ArrayList<MatriculaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}

	public void consultarCandidato() {
		try {
			getFacadeFactory().getPessoaFacade().setIdEntidade("Candidato");
			List<PessoaVO> objs = new ArrayList<PessoaVO>(0);
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
			setListaConsultaCandidato(new ArrayList<PessoaVO>(0));
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
			List<ParceiroVO> objs = new ArrayList<ParceiroVO>(0);
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
			setListaConsultaParceiro(new ArrayList<ParceiroVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarCentroReceita() {
		try {
			List<CentroReceitaVO> objs = new ArrayList<CentroReceitaVO>(0);
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
			setListaConsultaCentroReceita(new ArrayList<CentroReceitaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	private List<SelectItem> tipoConsultaComboCentroReceita;

	public List<SelectItem> getTipoConsultaComboCentroReceita() {
		if (tipoConsultaComboCentroReceita == null) {
			tipoConsultaComboCentroReceita = new ArrayList<SelectItem>(0);
			tipoConsultaComboCentroReceita.add(new SelectItem("descricao", "Descrição"));
			tipoConsultaComboCentroReceita.add(new SelectItem("identificadorCentroReceita", "Identificador Centro Receita"));
			tipoConsultaComboCentroReceita.add(new SelectItem("nomeDepartamento", "Departamento"));
		}
		return tipoConsultaComboCentroReceita;
	}

	private List<SelectItem> tipoConsultaComboAluno;

	public List<SelectItem> getTipoConsultaComboAluno() {
		if (tipoConsultaComboAluno == null) {
			tipoConsultaComboAluno = new ArrayList<SelectItem>(0);
			tipoConsultaComboAluno.add(new SelectItem("matricula", "Matrícula"));
			tipoConsultaComboAluno.add(new SelectItem("nomePessoa", "Nome Aluno"));
			tipoConsultaComboAluno.add(new SelectItem("nomeCurso", "Nome Curso"));
		}
		return tipoConsultaComboAluno;
	}

	private List<SelectItem> tipoConsultaComboFuncionario;

	public List<SelectItem> getTipoConsultaComboFuncionario() {
		if (tipoConsultaComboFuncionario == null) {
			tipoConsultaComboFuncionario = new ArrayList<SelectItem>(0);
			tipoConsultaComboFuncionario.add(new SelectItem("matricula", "Matrícula"));
			tipoConsultaComboFuncionario.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboFuncionario.add(new SelectItem("CPF", "CPF"));
			tipoConsultaComboFuncionario.add(new SelectItem("cargo", "Cargo"));
			tipoConsultaComboFuncionario.add(new SelectItem("departamento", "Departamento"));
		}
		return tipoConsultaComboFuncionario;
	}

	public List<SelectItem> tipoConsultaComboCandidato;

	public List<SelectItem> getTipoConsultaComboCandidato() {
		if (tipoConsultaComboCandidato == null) {
			tipoConsultaComboCandidato = new ArrayList<SelectItem>(0);
			tipoConsultaComboCandidato.add(new SelectItem("codigo", "Código"));
			tipoConsultaComboCandidato.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboCandidato.add(new SelectItem("CPF", "CPF"));
			tipoConsultaComboCandidato.add(new SelectItem("RG", "RG"));
		}
		return tipoConsultaComboCandidato;
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	private List<SelectItem> tipoConsultaComboParceiro;

	public List<SelectItem> getTipoConsultaComboParceiro() {
		if (tipoConsultaComboParceiro == null) {
			tipoConsultaComboParceiro = new ArrayList<SelectItem>(0);
			tipoConsultaComboParceiro.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboParceiro.add(new SelectItem("razaoSocial", "Razão Social"));
			tipoConsultaComboParceiro.add(new SelectItem("RG", "RG"));
			tipoConsultaComboParceiro.add(new SelectItem("CPF", "CPF"));
			tipoConsultaComboParceiro.add(new SelectItem("tipoParceiro", "Tipo Parceiro"));
		}
		return tipoConsultaComboParceiro;
	}

	private List<SelectItem> tipoAgrupamento;

	public List<SelectItem> getTipoAgrupamento() {
		if (tipoAgrupamento == null) {
			tipoAgrupamento = new ArrayList<SelectItem>(0);
			tipoAgrupamento.add(new SelectItem("ContaReceberRel", ""));
			tipoAgrupamento.add(new SelectItem("ContaReceberResumidaPorDataRel", "Data"));
			tipoAgrupamento.add(new SelectItem("ContaReceberResumidaPorTipoSacadoRel", "Sacado"));
		}
		return tipoAgrupamento;
	}
	
	private List<SelectItem> tipoConsultaTipoRequerimento;

	public List<SelectItem> getTipoConsultaTipoRequerimento() {
		if (tipoConsultaTipoRequerimento == null) {
			tipoConsultaTipoRequerimento = new ArrayList<SelectItem>(0);
			tipoConsultaTipoRequerimento.add(new SelectItem("codigo", "codigo"));
			tipoConsultaTipoRequerimento.add(new SelectItem("nome", "Nome"));
		}
		return tipoConsultaTipoRequerimento;
	}
	public void selecionarFuncionario() {
		FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItens");
		setFuncionarioMatricula(obj.getMatricula());
		setFuncionarioNome(obj.getPessoa().getNome());
		setPessoa(obj.getPessoa().getCodigo());
		setCampoConsultaFuncionario("");
		setValorConsultaFuncionario("");
		setListaConsultaFuncionario(new ArrayList<FuncionarioVO>(0));

	}
	
	public void selecionarTipoRequerimento() {
		TipoRequerimentoVO obj = (TipoRequerimentoVO) context().getExternalContext().getRequestMap().get("tipoRequerimentoItem");
		setTipoRequerimentoVO(obj);
		getFiltroRelatorioFinanceiroVO().setTipoOrigemRequerimento(true);
		setCampoConsultaTipoRequerimento("");
		setValorConsultaTipoRequerimento("");
		setListaConsultaTipoRequerimento(new ArrayList<TipoRequerimentoVO>(0));

	}

	public void selecionarAluno() {
		MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("alunoItens");

		setAlunoMatriula(obj.getMatricula());
		setAlunoNome(obj.getAluno().getNome());
		setPessoa(obj.getAluno().getCodigo());
		setCampoConsultaAluno("");
		setValorConsultaAluno("");
		setListaConsultaAluno(new ArrayList<MatriculaVO>(0));

	}

	public void selecionarCandidato() {
		PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("candidatoItens");
		setCandidatoCpf(obj.getCPF());
		setCandidatoNome(obj.getNome());
		setPessoa(obj.getCodigo());
		setCampoConsultaCandidato("");
		setValorConsultaCandidato("");
		setListaConsultaCandidato(new ArrayList<PessoaVO>(0));
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
		setListaConsultaCentroReceita(new ArrayList<CentroReceitaVO>(0));
	}

	public void limparDadosAluno() {
		setAlunoMatriula("");
		setAlunoNome("");
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

	
	public void limparTipoRequerimento() {
		setTipoRequerimentoVO(null);
	}
	
	public void inicializarListasSelectItemTodosComboBox() {
		montarListaSelectItemContaCorrente();
	}

	public List<SelectItem> listaSelectItemTipoPessoa;

	public List<SelectItem> getListaSelectItemTipoPessoa() {
		if (listaSelectItemTipoPessoa == null) {
			listaSelectItemTipoPessoa = new ArrayList<SelectItem>(0);
			listaSelectItemTipoPessoa.add(new SelectItem("", ""));
			listaSelectItemTipoPessoa.add(new SelectItem(TipoPessoa.ALUNO.getValor(), TipoPessoa.ALUNO.getDescricao()));
			listaSelectItemTipoPessoa.add(new SelectItem(TipoPessoa.CANDIDATO.getValor(), TipoPessoa.CANDIDATO.getDescricao()));
			listaSelectItemTipoPessoa.add(new SelectItem(TipoPessoa.FORNECEDOR.getValor(), TipoPessoa.FORNECEDOR.getDescricao()));
			listaSelectItemTipoPessoa.add(new SelectItem(TipoPessoa.FUNCIONARIO.getValor(), TipoPessoa.FUNCIONARIO.getDescricao()));
			listaSelectItemTipoPessoa.add(new SelectItem(TipoPessoa.PARCEIRO.getValor(), TipoPessoa.PARCEIRO.getDescricao()));
			listaSelectItemTipoPessoa.add(new SelectItem(TipoPessoa.REQUERENTE.getValor(), TipoPessoa.REQUERENTE.getDescricao()));
			listaSelectItemTipoPessoa.add(new SelectItem(TipoPessoa.RESPONSAVEL_FINANCEIRO.getValor(), TipoPessoa.RESPONSAVEL_FINANCEIRO.getDescricao()));
		}
		return listaSelectItemTipoPessoa;
	}

	public List<SelectItem> listaSelectItemOpcaoOrdenacao;

	public List<SelectItem> getListaSelectItemOpcaoOrdenacao() {
		if (listaSelectItemOpcaoOrdenacao == null) {
			listaSelectItemOpcaoOrdenacao = new ArrayList<SelectItem>(0);
			listaSelectItemOpcaoOrdenacao.add(new SelectItem("sacado", "Nome"));
			listaSelectItemOpcaoOrdenacao.add(new SelectItem("dataVencimento", "Data Vencimento"));

		}
		return listaSelectItemOpcaoOrdenacao;
	}

	private List<SelectItem> listaSelectItemSituacaoContaReceber;

	public List<SelectItem> getListaSelectItemSituacaoContaReceber() throws Exception {
		if (listaSelectItemSituacaoContaReceber == null) {
			listaSelectItemSituacaoContaReceber = new ArrayList<SelectItem>(0);
			listaSelectItemSituacaoContaReceber.add(new SelectItem("AR", "A Receber"));
			listaSelectItemSituacaoContaReceber.add(new SelectItem("REnoMes", "Recebido no Mês"));
			listaSelectItemSituacaoContaReceber.add(new SelectItem("REdoMes", "Recebido do Mês"));
			listaSelectItemSituacaoContaReceber.add(new SelectItem("NE", "Renegociadas"));

		}
		return listaSelectItemSituacaoContaReceber;
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
			List<FornecedorVO> objs = new ArrayList<FornecedorVO>(0);
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
			setListaConsultaFornecedor(new ArrayList<FornecedorVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void montarListaSelectItemContaCorrente() {
		try {
			montarListaSelectItemContaCorrente("");
		} catch (Exception e) {
			//System.out.println("MENSAGEM => " + e.getMessage());
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
		}
	}

	public void montarListaSelectItemCondicaoPagamentoPlanoFinanceiroCurso(String prm) throws Exception {
		List<CondicaoPagamentoPlanoFinanceiroCursoVO> resultadoConsulta = null;
		try {
			resultadoConsulta = getFacadeFactory().getCondicaoPagamentoPlanoFinanceiroCursoFacade().consultarPorCodigoPlanoFinanceiroCurso(getPlanoFinanceiroCursoVO().getCodigo(), "AT", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());

			List<SelectItem> objs = new ArrayList<SelectItem>(0);
			objs.add(new SelectItem(0, ""));
			for (CondicaoPagamentoPlanoFinanceiroCursoVO obj : resultadoConsulta) {
				objs.add(new SelectItem(obj.getCodigo(), obj.getDescricao().toString()));
			}
			setListaSelectItemCondicaoPagamentoPlanoFinanceiroCurso(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
		}
	}

	public void montarListaSelectItemContaCorrente(String prm) throws Exception {
		List<ContaCorrenteVO> resultadoConsulta = null;
		try {
			resultadoConsulta = consultarContaCorrentePorNumero(prm);

			List<SelectItem> objs = new ArrayList<SelectItem>(0);
			objs.add(new SelectItem(0, ""));
			for (ContaCorrenteVO obj : resultadoConsulta) {
				if(Uteis.isAtributoPreenchido(obj.getDescricaoCompletaConta())){
					objs.add(new SelectItem(obj.getCodigo(), obj.getDescricaoCompletaConta()));
				}else{
					objs.add(new SelectItem(obj.getCodigo(), obj.getNumero().toString()));
				}
			}
			setListaSelectItemContaCorrente(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);

		}
	}

	public List<ContaCorrenteVO> consultarContaCorrentePorNumero(String numeroPrm) throws Exception {
		return getFacadeFactory().getContaCorrenteFacade().consultarPorNumero(numeroPrm, getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
	}

	public void consultarCurso() {
		try {
			List<CursoVO> objs = new ArrayList<CursoVO>(0);
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
			setListaConsultaCurso(new ArrayList<CursoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarCurso() throws Exception {
		CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItens");
		getUnidadeEnsinoCursoVO().setCurso(obj);
		setTurmaVO(new TurmaVO());
		setCampoConsultaCurso("");
		setValorConsultaCurso("");
		setListaConsultaCurso(new ArrayList<CursoVO>(0));
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
			List<TurmaVO> objs = new ArrayList<TurmaVO>(0);
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
			setListaConsultaTurma(new ArrayList<TurmaVO>(0));
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

	public List<SelectItem> tipoConsultaComboTurma;

	public List<SelectItem> getTipoConsultaComboTurma() {
		if (tipoConsultaComboTurma == null) {
			tipoConsultaComboTurma = new ArrayList<SelectItem>(0);
			tipoConsultaComboTurma.add(new SelectItem("identificadorTurma", "Identificador"));
			tipoConsultaComboTurma.add(new SelectItem("codigo", "Código"));
		}
		return tipoConsultaComboTurma;
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

	public List<MatriculaVO> getListaConsultaAluno() {
		return listaConsultaAluno;
	}

	public void setListaConsultaAluno(List<MatriculaVO> listaConsultaAluno) {
		this.listaConsultaAluno = listaConsultaAluno;
	}

	public List<PessoaVO> getListaConsultaCandidato() {
		return listaConsultaCandidato;
	}

	public void setListaConsultaCandidato(List<PessoaVO> listaConsultaCandidato) {
		this.listaConsultaCandidato = listaConsultaCandidato;
	}

	public List<CentroReceitaVO> getListaConsultaCentroReceita() {
		return listaConsultaCentroReceita;
	}

	public void setListaConsultaCentroReceita(List<CentroReceitaVO> listaConsultaCentroReceita) {
		this.listaConsultaCentroReceita = listaConsultaCentroReceita;
	}

	public List<FuncionarioVO> getListaConsultaFuncionario() {
		return listaConsultaFuncionario;
	}

	public void setListaConsultaFuncionario(List<FuncionarioVO> listaConsultaFuncionario) {
		this.listaConsultaFuncionario = listaConsultaFuncionario;
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

	public Boolean getCandidato() {
		return getTipoPessoa().equals(TipoPessoa.CANDIDATO.getValor());
	}

	public Boolean getFuncionario() {
		return getTipoPessoa().equals(TipoPessoa.FUNCIONARIO.getValor());
	}

	/**
	 * @return the listaConsultaParceiro
	 */
	public List<ParceiroVO> getListaConsultaParceiro() {
		return listaConsultaParceiro;
	}

	/**
	 * @param listaConsultaParceiro
	 *            the listaConsultaParceiro to set
	 */
	public void setListaConsultaParceiro(List<ParceiroVO> listaConsultaParceiro) {
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

	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemUnidadeEnsino;
	}

	public List<SelectItem> getListaSelectItemContaCorrente() {
		if (listaSelectItemContaCorrente == null) {
			listaSelectItemContaCorrente = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemContaCorrente;
	}

	public void setListaSelectItemContaCorrente(List<SelectItem> listaSelectItemContaCorrente) {
		this.listaSelectItemContaCorrente = listaSelectItemContaCorrente;
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
			return "A Receber";
		}
		if (getSituacao().equals("NE")) {
			return "NEGOCIADAS";
		}
		if (getSituacao().equals("REnoMes")) {
			return "Recebidas no Mês";
		}
		if (getSituacao().equals("REdoMes")) {
			return "Recebidas do Mês";
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
			unidadeEnsinoVO = new UnidadeEnsinoVO();
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

	public String getCampoConsultaTurma() {
		if (campoConsultaTurma == null) {
			campoConsultaTurma = "";
		}
		return campoConsultaTurma;
	}

	public void setCampoConsultaTurma(String campoConsultaTurma) {
		this.campoConsultaTurma = campoConsultaTurma;
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

	public String getValorConsultaTurma() {
		if (valorConsultaTurma == null) {
			valorConsultaTurma = "";
		}
		return valorConsultaTurma;
	}

	public void setValorConsultaTurma(String valorConsultaTurma) {
		this.valorConsultaTurma = valorConsultaTurma;
	}

	public List<SelectItem> tipoConsultaComboPlanoFinanceiroCurso;

	public List<SelectItem> getTipoConsultaComboPlanoFinanceiroCurso() {
		if (tipoConsultaComboPlanoFinanceiroCurso == null) {
			tipoConsultaComboPlanoFinanceiroCurso = new ArrayList<SelectItem>(0);
			tipoConsultaComboPlanoFinanceiroCurso.add(new SelectItem("descricao", "Descrição"));
			tipoConsultaComboPlanoFinanceiroCurso.add(new SelectItem("codigo", "Código"));
		}
		return tipoConsultaComboPlanoFinanceiroCurso;
	}

	public void consultarPlanoFinanceiroCurso() {
		List<PlanoFinanceiroCursoVO> objs = new ArrayList<PlanoFinanceiroCursoVO>(0);
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
		setListaConsultaPlanoFinanceiroCurso(new ArrayList<PlanoFinanceiroCursoVO>(0));
		montarListaSelectItemCondicaoPagamentoPlanoFinanceiroCurso();
	}

	/**
	 * @return the listaConsultaPlanoFinanceiroCurso
	 */
	public List<PlanoFinanceiroCursoVO> getListaConsultaPlanoFinanceiroCurso() {
		return listaConsultaPlanoFinanceiroCurso;
	}

	/**
	 * @param listaConsultaPlanoFinanceiroCurso
	 *            the listaConsultaPlanoFinanceiroCurso to set
	 */
	public void setListaConsultaPlanoFinanceiroCurso(List<PlanoFinanceiroCursoVO> listaConsultaPlanoFinanceiroCurso) {
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
	public List<SelectItem> getListaSelectItemCondicaoPagamentoPlanoFinanceiroCurso() {
		if (listaSelectItemCondicaoPagamentoPlanoFinanceiroCurso == null) {
			listaSelectItemCondicaoPagamentoPlanoFinanceiroCurso = new ArrayList<SelectItem>();
		}
		return listaSelectItemCondicaoPagamentoPlanoFinanceiroCurso;
	}

	/**
	 * @param listaSelectItemCondicaoPagamentoPlanoFinanceiroCurso
	 *            the listaSelectItemCondicaoPagamentoPlanoFinanceiroCurso to
	 *            set
	 */
	public void setListaSelectItemCondicaoPagamentoPlanoFinanceiroCurso(List<SelectItem> listaSelectItemCondicaoPagamentoPlanoFinanceiroCurso) {
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
		if(fornecedorNome == null) {
			fornecedorNome = "";
		}
		return fornecedorNome;
	}

	public void setFornecedorNome(String fornecedorNome) {
		this.fornecedorNome = fornecedorNome;
	}

	public Integer getFornecedorCodigo() {
		if (fornecedorCodigo == null) {
			fornecedorCodigo = 0;
		}
		return fornecedorCodigo;
	}

	public void setFornecedorCodigo(Integer fornecedorCodigo) {
		this.fornecedorCodigo = fornecedorCodigo;
	}

	public Boolean getFornecedor() {
		return getTipoPessoa().equals(TipoPessoa.FORNECEDOR.getValor());
	}

	public Boolean getFiltrarContasRecebidasDataRecebimento() {
		if (filtrarContasRecebidasDataRecebimento == null) {
			filtrarContasRecebidasDataRecebimento = false;
		}
		return filtrarContasRecebidasDataRecebimento;
	}

	public void setFiltrarContasRecebidasDataRecebimento(Boolean filtrarContasRecebidasDataRecebimento) {
		this.filtrarContasRecebidasDataRecebimento = filtrarContasRecebidasDataRecebimento;
	}

	public String getOrdenarPor() {
		if (ordenarPor == null) {
			ordenarPor = "";
		}
		return ordenarPor;
	}

	public void setOrdenarPor(String ordenarPor) {
		this.ordenarPor = ordenarPor;
	}

	@PostConstruct
	public void consultarUnidadeEnsino() {
		try {
			consultarUnidadeEnsinoFiltroRelatorio("ContaRecebidaVersoContaReceberRel");
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

	public boolean getIsApresentarBotaoSelecionarUnidadeEnsino() throws Exception {
		return getUnidadeEnsinoVOs().size() > 1;
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
	
	public TipoRequerimentoVO getTipoRequerimentoVO() {
		if(tipoRequerimentoVO == null){
			tipoRequerimentoVO = new TipoRequerimentoVO();
		}
		return tipoRequerimentoVO;
	}

	public void setTipoRequerimentoVO(TipoRequerimentoVO tipoRequerimentoVO) {
		this.tipoRequerimentoVO = tipoRequerimentoVO;
	}

	public String getValorConsultaTipoRequerimento() {
		if(valorConsultaTipoRequerimento == null){
			valorConsultaTipoRequerimento = "";
		}
		return valorConsultaTipoRequerimento;
	}

	public void setValorConsultaTipoRequerimento(String valorConsultaTipoRequerimento) {
		this.valorConsultaTipoRequerimento = valorConsultaTipoRequerimento;
	}

	public String getCampoConsultaTipoRequerimento() {
		return campoConsultaTipoRequerimento;
	}

	public void setCampoConsultaTipoRequerimento(String campoConsultaTipoRequerimento) {
		this.campoConsultaTipoRequerimento = campoConsultaTipoRequerimento;
	}

	public List<TipoRequerimentoVO> getListaConsultaTipoRequerimento() {
		return listaConsultaTipoRequerimento;
	}

	public void setListaConsultaTipoRequerimento(List<TipoRequerimentoVO> listaConsultaTipoRequerimento) {
		this.listaConsultaTipoRequerimento = listaConsultaTipoRequerimento;
	}

	public Boolean getFiltrarContasRegistroCobranca() {
		if (filtrarContasRegistroCobranca == null) {
			filtrarContasRegistroCobranca = false;
		}
		return filtrarContasRegistroCobranca;
	}

	public void setFiltrarContasRegistroCobranca(Boolean filtrarContasRegistroCobranca) {
		this.filtrarContasRegistroCobranca = filtrarContasRegistroCobranca;
	}
	
	public void montarDadosSalvosEmissaoUltimoRelatorio() {
		
		try {
			
			Map<String, String> retorno = getFacadeFactory().getLayoutPadraoFacade().consultarValoresPadroes(null, ContaRecebidaVersoContaReceberRel.class.getSimpleName());
			for (String key : retorno.keySet()) {
				
				if (key.equals("dataInicio") && retorno.get(key) != null
					&& !retorno.get(key).equalsIgnoreCase("null") && !retorno.get(key).trim().isEmpty()) {
					setDataInicio(Uteis.getData(retorno.get(key), "dd/MM/yyyy"));
				} 
				else if (key.equals("dataFim") && retorno.get(key) != null
						&& !retorno.get(key).equalsIgnoreCase("null") && !retorno.get(key).trim().isEmpty()) {
					setDataFim(Uteis.getData(retorno.get(key), "dd/MM/yyyy"));
				} 
				else if (key.equals("unidadeEnsino")) {
					setUnidadeEnsinosApresentar(retorno.get(key));
				}
				else if (key.equals("unidadeEnsinoVos")) {
					
					String unidadesVOs = retorno.get(key);
					String[] unidades = unidadesVOs.split(",");
					
					// Carregar o codigo das unidades de ensino na lista porque ela sera utilizada posteriormente
					getUnidadeEnsinoVOs().clear();
					for(String codigo : unidades) {
						UnidadeEnsinoVO unidade = new UnidadeEnsinoVO();
						if(!codigo.trim().isEmpty()) {
							unidade.setCodigo(new Integer(codigo.trim()));
							getUnidadeEnsinoVOs().add(unidade);							
						}
					}
				} 
				else if (key.equals("considerarUnidadeFinanceira") && retorno.get(key) != null
						&& !retorno.get(key).equalsIgnoreCase("null") && !retorno.get(key).trim().isEmpty()) {
					setConsiderarUnidadeEnsinoFinanceira(Boolean.valueOf(retorno.get(key)));
				}
				else if (key.equals("ordenarPor") && retorno.get(key) != null
						&& !retorno.get(key).equalsIgnoreCase("null") && !retorno.get(key).trim().isEmpty()) {
					setOrdenarPor(retorno.get(key));
				}
				else if (key.equals("tipoPessoa") && retorno.get(key) != null
						&& !retorno.get(key).equalsIgnoreCase("null") && !retorno.get(key).trim().isEmpty()) {
					setTipoPessoa(retorno.get(key));
				}
				else if (key.equals("filtrarContasRecebidasDataRecebimento") && retorno.get(key) != null
						&& !retorno.get(key).equalsIgnoreCase("null") && !retorno.get(key).trim().isEmpty()) {
					setFiltrarContasRecebidasDataRecebimento(Boolean.valueOf(retorno.get(key)));
				}
				else if (key.equals("curso") && retorno.get(key) != null
						&& !retorno.get(key).equalsIgnoreCase("null") && !retorno.get(key).trim().isEmpty()) {
					getUnidadeEnsinoCursoVO().getCurso().setCodigo(Integer.valueOf(retorno.get(key)));
				}
				else if (key.equals("cursoNome") && retorno.get(key) != null
						&& !retorno.get(key).equalsIgnoreCase("null") && !retorno.get(key).trim().isEmpty()) {
					getUnidadeEnsinoCursoVO().getCurso().setNome(retorno.get(key));
				}
				else if (key.equals("turma") && retorno.get(key) != null
						&& !retorno.get(key).equalsIgnoreCase("null") && !retorno.get(key).trim().isEmpty()) {
					getTurmaVO().setCodigo(Integer.valueOf(retorno.get(key)));
				}
				else if (key.equals("turmaNome") && retorno.get(key) != null
						&& !retorno.get(key).equalsIgnoreCase("null") && !retorno.get(key).trim().isEmpty()) {
					getTurmaVO().setIdentificadorTurma(retorno.get(key));
				}
				else if (key.equals("alunoMatricula") && retorno.get(key) != null
						&& !retorno.get(key).equalsIgnoreCase("null") && !retorno.get(key).trim().isEmpty()) {
					setAlunoMatriula(retorno.get(key));
				}
				else if (key.equals("pessoa") && retorno.get(key) != null
						&& !retorno.get(key).equalsIgnoreCase("null") && !retorno.get(key).trim().isEmpty()) {
					setPessoa(Integer.valueOf(retorno.get(key)));
				}
				else if (key.equals("parceiro") && retorno.get(key) != null
						&& !retorno.get(key).equalsIgnoreCase("null") && !retorno.get(key).trim().isEmpty()) {
					setParceiroCodigo(Integer.valueOf(retorno.get(key)));
				}
				else if (key.equals("parceiroNome") && retorno.get(key) != null
						&& !retorno.get(key).equalsIgnoreCase("null") && !retorno.get(key).trim().isEmpty()) {
					setParceiroNome(retorno.get(key));
				}
				else if (key.equals("fornecedorCodigo") && retorno.get(key) != null
						&& !retorno.get(key).equalsIgnoreCase("null") && !retorno.get(key).trim().isEmpty()) {
					setFornecedorCodigo(Integer.valueOf(retorno.get(key)));
				}
				else if (key.equals("fornecedorNome") && retorno.get(key) != null
						&& !retorno.get(key).equalsIgnoreCase("null") && !retorno.get(key).trim().isEmpty()) {
					setFornecedorNome(retorno.get(key));
				}
				else if (key.equals("contaCorrente") && retorno.get(key) != null
						&& !retorno.get(key).equalsIgnoreCase("null") && !retorno.get(key).trim().isEmpty()) {
					getContaCorrente().setCodigo(Integer.valueOf(retorno.get(key)));
				}
				else if (key.equals("contaCorrenteNumero") && retorno.get(key) != null
						&& !retorno.get(key).equalsIgnoreCase("null") && !retorno.get(key).trim().isEmpty()) {
					getContaCorrente().setNumero(retorno.get(key));
				}
				else if (key.equals("centroReceita") && retorno.get(key) != null
						&& !retorno.get(key).equalsIgnoreCase("null") && !retorno.get(key).trim().isEmpty()) {
					setCentroReceita(Integer.valueOf(retorno.get(key)));
				}
				else if (key.equals("centroReceitaDescricao") && retorno.get(key) != null
						&& !retorno.get(key).equalsIgnoreCase("null") && !retorno.get(key).trim().isEmpty()) {
					setCentroReceitaDescricao(retorno.get(key));
				}
				else if (key.equals("tipoRequerimento") && retorno.get(key) != null
						&& !retorno.get(key).equalsIgnoreCase("null") && !retorno.get(key).trim().isEmpty()) {
					setTipoRequerimento(retorno.get(key));
				}
				else if (key.equals("alunoNome") && retorno.get(key) != null
						&& !retorno.get(key).equalsIgnoreCase("null") && !retorno.get(key).trim().isEmpty()) {
					setAlunoNome(retorno.get(key));
				}
			}
				
			getFacadeFactory().getLayoutPadraoFacade().consultarPadraoFiltroTipoOrigemContaReceber(getFiltroRelatorioFinanceiroVO(), ContaRecebidaVersoContaReceberRel.class.getSimpleName(), getUsuarioLogado());
				
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void gravarDadosPadroesEmissaoRelatorio(){
		try{
			
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getConsiderarUnidadeEnsinoFinanceira().toString(), ContaRecebidaVersoContaReceberRel.class.getSimpleName(), "considerarUnidadeFinanceira", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getOrdenarPor(), ContaRecebidaVersoContaReceberRel.class.getSimpleName(), "ordenarPor", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirFiltroTipoOrigemContaReceber(getFiltroRelatorioFinanceiroVO(), ContaRecebidaVersoContaReceberRel.class.getSimpleName(), getUsuarioLogado());
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public Boolean getConsiderarUnidadeEnsinoFinanceira() {
		if (considerarUnidadeEnsinoFinanceira == null)
			considerarUnidadeEnsinoFinanceira = false;
		return considerarUnidadeEnsinoFinanceira;
	}
	
	public void setConsiderarUnidadeEnsinoFinanceira(Boolean considerarUnidadeEnsinoFinanceira) {
		this.considerarUnidadeEnsinoFinanceira = considerarUnidadeEnsinoFinanceira;
	}
	
	public Date getDataInicioAux() {
		if (dataInicioAux == null) {
			dataInicio = Uteis.getNewDateComMesesAMenos(1);
		}
		return dataInicioAux;
	}

	public void setDataInicioAux(Date dataInicioAux) {
		this.dataInicioAux = dataInicioAux;
	}

	public Date getDataFimAux() {
		if (dataFimAux == null) {
			dataFimAux = new Date();
		}
		return dataFimAux;
	}

	public void setDataFimAux(Date dataFimAux) {
		this.dataFimAux = dataFimAux;
	}
	
	public void retomarDatasAntesFiltroCompetencia() {
		if (getFiltroRelatorioFinanceiroVO().getFiltrarPorDataCompetencia()) {
			setDataInicioAux(getDataInicio());
			setDataFimAux(getDataFim());
		} else {
			setDataInicio(getDataInicioAux());
			setDataFim(getDataFimAux());
		}
	}
}