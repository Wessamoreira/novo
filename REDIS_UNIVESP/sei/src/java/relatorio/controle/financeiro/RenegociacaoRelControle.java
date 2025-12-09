package relatorio.controle.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.financeiro.ParceiroVO;
import negocio.comuns.utilitarias.Uteis;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.financeiro.RenegociacaoRelVO;
import relatorio.negocio.jdbc.financeiro.RenegociacaoRel;

/**
 *
 * @author Carlos
 */
@Controller("RenegociacaoRelControle")
@Scope("viewScope")
@Lazy
public class RenegociacaoRelControle extends SuperControleRelatorio {

	private static final long serialVersionUID = -6587521779221130272L;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private TurmaVO turmaVO;
	private MatriculaVO alunoVO;
	private String campoConsultaTurma;
	private String valorConsultaTurma;
	private List<TurmaVO> listaConsultaTurma;
	private String campoConsultaAluno;
	private String valorConsultaAluno;
	private List<MatriculaVO> listaConsultaAluno;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private List<SelectItem> listaSelectItemSituacaoContaReceber;
	private List<SelectItem> listaSelectItemTipoPeriodo;
	private List<SelectItem> listaSelectItemOrdenarPor;
	private String situacaoContaReceber;
	private String tipo;
	private String tipoRelatorio;
	private Date dataInicio;
	private Date dataFim;
	private String tipoContaNegociada;
	private String tipoPeriodo;
	private String ordernarPor;
	private List<FuncionarioVO> listaConsultaFuncionario;
	private String valorConsultaFuncionario;
	private String campoConsultaFuncionario;
	private List<ParceiroVO> listaConsultaParceiro;
	private String valorConsultaParceiro;
	private String campoConsultaParceiro;
	protected List<FornecedorVO> listaConsultaFornecedor;
	protected String valorConsultaFornecedor;
	protected String campoConsultaFornecedor;
	private ParceiroVO parceiroVO;
	private FuncionarioVO funcionarioVO;
	private FornecedorVO fornecedorVO;
	private UsuarioVO responsavelRenegociacao;
	private String campoConsultaResponsavelRenegociacao;
	private String valorConsultaResponsavelRenegociacao;
	private List<UsuarioVO> responsavelRenegociacaoVOs;
	private Boolean imprimirExcel;

	public RenegociacaoRelControle() {
		setTipo("aluno");
	}

	public void imprimirPDF() throws Exception {
		String titulo = null;
		List<RenegociacaoRelVO> listaRenegociacaoVOs = null;
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "RenegociacaoRelControle", "Inicializando Geração de Relatório Renegociação", "Emitindo Relatório");
			if (getTipoRelatorio().contains("sintetico")) {
				titulo = "Renegociação Sintético";
			} else {
				titulo = "Renegociação Analítico";
			}
			listaRenegociacaoVOs = getFacadeFactory().getRenegociacaoRelFacade().realizarCriacaoObjRel(getUnidadeEnsinoVOs(), getTurmaVO().getCodigo(), getAlunoVO().getMatricula(), getTipoContaNegociada(), getTipo(), getTipoRelatorio(), getDataInicio(), getDataFim(), getSituacaoContaReceber(), getOrdernarPor(), getTipoPeriodo(), getUsuarioLogado(), getResponsavelFinanceiro().getCodigo(), getFuncionarioVO().getCodigo(), getParceiroVO().getCodigo(), getFornecedorVO().getCodigo(), getResponsavelRenegociacao().getCodigo());
			if (!listaRenegociacaoVOs.isEmpty()) {
				getSuperParametroRelVO().setTituloRelatorio(titulo);
				getSuperParametroRelVO().setNomeDesignIreport(RenegociacaoRel.getDesignIReportRelatorio(getTipoRelatorio(), getImprimirExcel()));
				if (getImprimirExcel()) {
					getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.EXCEL);
				} else {
					getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				}
				getSuperParametroRelVO().setSubReport_Dir(RenegociacaoRel.caminhoBaseRelatorio());
				getSuperParametroRelVO().setCaminhoBaseRelatorio(RenegociacaoRel.caminhoBaseRelatorio());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setDataInicio(Uteis.getData(getDataInicio()));
				getSuperParametroRelVO().setDataFim(Uteis.getData(getDataFim()));
				if (getTipoContaNegociada().equals("")) {
					getSuperParametroRelVO().setTipoContaReceber("Todas");
				} else if (getTipoContaNegociada().equals("")) {
					getSuperParametroRelVO().setTipoContaReceber("À Vencer");
				} else {
					getSuperParametroRelVO().setTipoContaReceber("Vencidas");
				}
				getSuperParametroRelVO().setUnidadeEnsino(getUnidadeEnsinoVO().getNome());
				getSuperParametroRelVO().setListaObjetos(listaRenegociacaoVOs);
				setMensagemID("msg_relatorio_ok");
				realizarImpressaoRelatorio();
				removerObjetoMemoria(this);
				setTipo("aluno");
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}
			registrarAtividadeUsuario(getUsuarioLogado(), "RenegociacaoRelControle", "Finalizando Geração de Relatório Renegociação", "Emitindo Relatório");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			titulo = null;
			Uteis.liberarListaMemoria(listaRenegociacaoVOs);
			setMarcarTodasUnidadeEnsino(true);
			marcarTodasUnidadesEnsinoAction();
			setImprimirExcel(Boolean.FALSE);
		}
	}

	public void alterarTipoPessoa() {
		setTurmaVO(null);
		setParceiroVO(null);
		setFuncionarioVO(null);
		setFornecedorVO(null);
		setAlunoVO(null);
	}

	public void consultarAlunoPorMatricula() throws Exception {
		try {
			MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatricula(getAlunoVO().getMatricula(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			if (objAluno.getMatricula().equals("")) {
				setAlunoVO(objAluno);
				throw new Exception("Aluno de matrícula " + getAlunoVO().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
			}
			setAlunoVO(objAluno);
			setCampoConsultaAluno("");
			setValorConsultaAluno("");
			setListaConsultaAluno(new ArrayList<MatriculaVO>(0));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarAluno() {
		try {
			setListaConsultaAluno(getFacadeFactory().getRenegociacaoRelFacade().consultarAluno(campoConsultaAluno, valorConsultaAluno, getTurmaVO().getCodigo(), getUnidadeEnsinoVO().getCodigo(), getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			getListaConsultaAluno().clear();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarTurma() {
		try {
			super.consultar();
			setListaConsultaTurma(getFacadeFactory().getRenegociacaoRelFacade().consultarTurma(getCampoConsultaTurma(), getValorConsultaTurma(), getUnidadeEnsinoVO().getCodigo(), getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			getListaConsultaTurma().clear();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> getTipoConsultaComboTurma() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("identificadorTurma", "Identificador"));
		return itens;
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
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorNome(getValorConsultaFuncionario(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("matricula")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorMatricula(getValorConsultaFuncionario(), this.getUnidadeEnsinoLogado().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("nomeCidade")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeCidade(getValorConsultaFuncionario(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("CPF")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorCPF(getValorConsultaFuncionario(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("cargo")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeCargo(getValorConsultaFuncionario(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("departamento")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeDepartamento(getValorConsultaFuncionario(), "FU", this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("unidadeEnsino")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeUnidadeEnsino(getValorConsultaFuncionario(), "FU", this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultaFuncionario(objs);
			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			setListaConsultaFuncionario(new ArrayList<FuncionarioVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarFornecedor() {
		FornecedorVO obj = (FornecedorVO) context().getExternalContext().getRequestMap().get("fornecedorItens");
		setFornecedorVO(obj);
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

	public List<SelectItem> getTipoConsultaComboFuncionario() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("CPF", "CPF"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("cargo", "Cargo"));
		itens.add(new SelectItem("departamento", "Departamento"));
		return itens;
	}

	public void selecionarFuncionario() {
		FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItens");
		setFuncionarioVO(obj);
		Uteis.liberarListaMemoria(getListaConsultaFuncionario());
		campoConsultaFuncionario = null;
		valorConsultaFuncionario = null;
	}

	@SuppressWarnings("rawtypes")
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
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarParceiro() {
		try {
			ParceiroVO obj = (ParceiroVO) context().getExternalContext().getRequestMap().get("parceiroItens");
			setParceiroVO(obj);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String getCampoConsultaFuncionario() {
		return campoConsultaFuncionario;
	}

	public void setCampoConsultaFuncionario(String campoConsultaFuncionario) {
		this.campoConsultaFuncionario = campoConsultaFuncionario;
	}

	public List<FuncionarioVO> getListaConsultaFuncionario() {
		return listaConsultaFuncionario;
	}

	public void setListaConsultaFuncionario(List<FuncionarioVO> listaConsultaFuncionario) {
		this.listaConsultaFuncionario = listaConsultaFuncionario;
	}

	public String getValorConsultaFuncionario() {
		return valorConsultaFuncionario;
	}

	public void setValorConsultaFuncionario(String valorConsultaFuncionario) {
		this.valorConsultaFuncionario = valorConsultaFuncionario;
	}

	public List<ParceiroVO> getListaConsultaParceiro() {
		if (listaConsultaParceiro == null) {
			listaConsultaParceiro = new ArrayList<ParceiroVO>(0);
		}
		return listaConsultaParceiro;
	}

	public void setListaConsultaParceiro(List<ParceiroVO> listaConsultaParceiro) {
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

	public List<SelectItem> getTipoConsultaComboTipo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("todos", "TODOS"));
		itens.add(new SelectItem("aluno", "Aluno"));
		itens.add(new SelectItem("responsavelFinanceiro", "Responsável Financeiro"));
		itens.add(new SelectItem("turma", "Turma"));
		itens.add(new SelectItem("fornecedor", "Fornecedor"));
		itens.add(new SelectItem("parceiro", "Parceiro"));
		itens.add(new SelectItem("funcionario", "Funcionário"));
		return itens;
	}

	public List<SelectItem> getTipoContasNegociadas() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("", "Todas"));
		itens.add(new SelectItem("AV", "À Vencer"));
		itens.add(new SelectItem("VE", "Vencidas"));
		return itens;
	}

	public List<SelectItem> getTipoConsultaComboTipoRelatorio() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("analiticoPorCurso", "Analítico por Curso"));
		itens.add(new SelectItem("analiticoPorTurma", "Analítico por Turma"));
		itens.add(new SelectItem("sinteticoPorCurso", "Sintético por Curso"));
		itens.add(new SelectItem("sinteticoPorTurma", "Sintético por Turma"));
		return itens;
	}

	public List<SelectItem> getTipoConsultaComboAluno() {
		List<SelectItem> itens = new ArrayList<SelectItem>();
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("nomePessoa", "Nome Aluno"));
		itens.add(new SelectItem("nomeCurso", "Nome Curso"));
		return itens;
	}

	public void selecionarTurma() {
		TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
		setTurmaVO(obj);
	}

	public void selecionarAluno() throws Exception {
		setAlunoVO((MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens"));
		setCampoConsultaAluno("");
		setValorConsultaAluno("");
		setListaConsultaAluno(new ArrayList<MatriculaVO>(0));
		setMensagemID("msg_dados_consultados");
	}

	public void limparDadosAluno() {
		setAlunoVO(new MatriculaVO());
	}

	public void limparDadosResponsavelFinanceiro() {
		getResponsavelFinanceiro().setCodigo(0);
		getResponsavelFinanceiro().setNome("");
	}

	public void limparDadosTurma() {
		setTurmaVO(new TurmaVO());
	}

	public void limparConsultaTurma() {
		getListaConsultaTurma().clear();
	}

	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if (unidadeEnsinoVO == null) {
			unidadeEnsinoVO = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoVO;
	}

	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
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

	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
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
			listaConsultaTurma = new ArrayList<TurmaVO>(0);
		}
		return listaConsultaTurma;
	}

	public void setListaConsultaTurma(List<TurmaVO> listaConsultaTurma) {
		this.listaConsultaTurma = listaConsultaTurma;
	}

	public String getCampoConsultaAluno() {
		if (campoConsultaAluno == null) {
			campoConsultaAluno = "";
		}
		return campoConsultaAluno;
	}

	public void setCampoConsultaAluno(String campoConsultaAluno) {
		this.campoConsultaAluno = campoConsultaAluno;
	}

	public String getValorConsultaAluno() {
		if (valorConsultaAluno == null) {
			valorConsultaAluno = "";
		}
		return valorConsultaAluno;
	}

	public void setValorConsultaAluno(String valorConsultaAluno) {
		this.valorConsultaAluno = valorConsultaAluno;
	}

	public List<MatriculaVO> getListaConsultaAluno() {
		if (listaConsultaAluno == null) {
			listaConsultaAluno = new ArrayList<MatriculaVO>(0);
		}
		return listaConsultaAluno;
	}

	public void setListaConsultaAluno(List<MatriculaVO> listaConsultaAluno) {
		this.listaConsultaAluno = listaConsultaAluno;
	}

	public MatriculaVO getAlunoVO() {
		if (alunoVO == null) {
			alunoVO = new MatriculaVO();
		}
		return alunoVO;
	}

	public void setAlunoVO(MatriculaVO alunoVO) {
		this.alunoVO = alunoVO;
	}

	public String getTipo() {
		if (tipo == null) {
			tipo = "";
		}
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public boolean getApresentarCampoAluno() {
		return getTipo().equals("aluno");
	}

	public boolean getApresentarCampoParceiro() {
		return getTipo().equals("parceiro");
	}

	public boolean getApresentarCampoFuncionario() {
		return getTipo().equals("funcionario");
	}

	public boolean getApresentarCampoForncedor() {
		return getTipo().equals("fornecedor");
	}

	public boolean getApresentarCampoResponsavelFinanceiro() {
		return getTipo().equals("responsavelFinanceiro");
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

	public Date getDataInicio() {
		if (dataInicio == null) {
			dataInicio = Uteis.getDataPrimeiroDiaMes(new Date());
		}
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		if (dataFim == null) {
			dataFim = Uteis.getDataUltimoDiaMes(new Date());
		}
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public String getTipoContaNegociada() {
		if (tipoContaNegociada == null) {
			tipoContaNegociada = "";
		}
		return tipoContaNegociada;
	}

	public void setTipoContaNegociada(String tipoContaNegociada) {
		this.tipoContaNegociada = tipoContaNegociada;
	}

	public Boolean getApresentarComboTipoContasNegociadas() {
		if (getTipoRelatorio().equals("analitico")) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	protected PessoaVO responsavelFinanceiro;

	public PessoaVO getResponsavelFinanceiro() {
		if (responsavelFinanceiro == null) {
			responsavelFinanceiro = new PessoaVO();
		}
		return responsavelFinanceiro;
	}

	public void setResponsavelFinanceiro(PessoaVO responsavelFinanceiro) {
		this.responsavelFinanceiro = responsavelFinanceiro;
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
			tipoConsultaComboResponsavelFinanceiro = new ArrayList<SelectItem>(0);
			tipoConsultaComboResponsavelFinanceiro.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboResponsavelFinanceiro.add(new SelectItem("nomeAluno", "Aluno"));
			tipoConsultaComboResponsavelFinanceiro.add(new SelectItem("CPF", "CPF"));
		}
		return tipoConsultaComboResponsavelFinanceiro;
	}

	public void selecionarResponsavelFinanceiro() {
		try {
			PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("responsavelFinanceiroItens");
			getListaConsultaResponsavelFinanceiro().clear();
			this.setResponsavelFinanceiro(obj);
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

	public List<SelectItem> getListaSelectItemSituacaoContaReceber() {
		if (listaSelectItemSituacaoContaReceber == null) {
			listaSelectItemSituacaoContaReceber = new ArrayList<SelectItem>(0);
			listaSelectItemSituacaoContaReceber.add(new SelectItem("", ""));
			listaSelectItemSituacaoContaReceber.add(new SelectItem("AR", "A Receber"));
			listaSelectItemSituacaoContaReceber.add(new SelectItem("VE", "Vencidas"));
			listaSelectItemSituacaoContaReceber.add(new SelectItem("RE", "Recebidas"));
		}
		return listaSelectItemSituacaoContaReceber;
	}

	public void setListaSelectItemSituacaoContaReceber(List<SelectItem> listaSelectItemSituacaoContaReceber) {
		this.listaSelectItemSituacaoContaReceber = listaSelectItemSituacaoContaReceber;
	}

	public String getSituacaoContaReceber() {
		return situacaoContaReceber;
	}

	public void setSituacaoContaReceber(String situacaoContaReceber) {
		this.situacaoContaReceber = situacaoContaReceber;
	}

	public List<SelectItem> getListaSelectItemOrdenarPor() {
		if (listaSelectItemOrdenarPor == null) {
			listaSelectItemOrdenarPor = new ArrayList<SelectItem>(0);
			listaSelectItemOrdenarPor.add(new SelectItem("NOME", "Nome"));
			listaSelectItemOrdenarPor.add(new SelectItem("DATA", "Data"));
		}
		return listaSelectItemOrdenarPor;
	}

	public void setListaSelectItemOrdenarPor(List<SelectItem> listaSelectItemOrdenarPor) {
		this.listaSelectItemOrdenarPor = listaSelectItemOrdenarPor;
	}

	public String getOrdernarPor() {
		if (ordernarPor == null) {
			ordernarPor = "NOME";
		}

		return ordernarPor;
	}

	public void setOrdernarPor(String ordernarPor) {
		this.ordernarPor = ordernarPor;
	}

	public String getTipoPeriodo() {
		if (tipoPeriodo == null) {
			tipoPeriodo = "DATA_RENEGOCIACAO";
		}
		return tipoPeriodo;
	}

	public void setTipoPeriodo(String tipoPeriodo) {
		this.tipoPeriodo = tipoPeriodo;
	}

	public void setTipoConsultaComboResponsavelFinanceiro(List<SelectItem> tipoConsultaComboResponsavelFinanceiro) {
		this.tipoConsultaComboResponsavelFinanceiro = tipoConsultaComboResponsavelFinanceiro;
	}

	public List<SelectItem> getTipoConsultaComboParceiro() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("razaoSocial", "Razão Social"));
		itens.add(new SelectItem("RG", "RG"));
		itens.add(new SelectItem("CPF", "CPF"));
		itens.add(new SelectItem("tipoParceiro", "Tipo Parceiro"));
		return itens;
	}

	public List<SelectItem> getListaSelectItemTipoPeriodo() {
		if (listaSelectItemTipoPeriodo == null) {
			listaSelectItemTipoPeriodo = new ArrayList<SelectItem>(0);
			listaSelectItemTipoPeriodo.add(new SelectItem("DATA_RENEGOCIACAO", "Data da Renegociação"));
			listaSelectItemTipoPeriodo.add(new SelectItem("DATA_COMPETENCIA_CONTA_RECEBER", "Data de Competência da Conta Receber"));
			listaSelectItemTipoPeriodo.add(new SelectItem("DATA_VENCIMENTO_CONTA_RECEBER", "Data de Vencimento da Conta Receber"));
		}
		return listaSelectItemTipoPeriodo;
	}

	public void setListaSelectItemTipoPeriodo(List<SelectItem> listaSelectItemTipoPeriodo) {
		this.listaSelectItemTipoPeriodo = listaSelectItemTipoPeriodo;
	}

	public ParceiroVO getParceiroVO() {
		if (parceiroVO == null) {
			parceiroVO = new ParceiroVO();
		}
		return parceiroVO;
	}

	public void setParceiroVO(ParceiroVO parceiroVO) {
		this.parceiroVO = parceiroVO;
	}

	public FuncionarioVO getFuncionarioVO() {
		if (funcionarioVO == null) {
			funcionarioVO = new FuncionarioVO();
		}
		return funcionarioVO;
	}

	public void setFuncionarioVO(FuncionarioVO funcionarioVO) {
		this.funcionarioVO = funcionarioVO;
	}

	public FornecedorVO getFornecedorVO() {
		if (fornecedorVO == null) {
			fornecedorVO = new FornecedorVO();
		}
		return fornecedorVO;
	}

	public void setFornecedorVO(FornecedorVO fornecedorVO) {
		this.fornecedorVO = fornecedorVO;
	}

	public void limparParceiro() {
		setParceiroVO(null);
	}

	public void limparFornecedor() {
		setFornecedorVO(null);
	}

	public void limparFuncionario() {
		setFuncionarioVO(null);
	}

	@PostConstruct
	public void consultarUnidadeEnsino() {
		try {
			consultarUnidadeEnsinoFiltroRelatorio("RenegociacaoRel");
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

	public boolean getIsApresentarFiltroDataCompetencia() {
		return getTipoPeriodo().equals("DATA_COMPETENCIA_CONTA_RECEBER");
	}

	public UsuarioVO getResponsavelRenegociacao() {
		if (responsavelRenegociacao == null) {
			responsavelRenegociacao = new UsuarioVO();
		}
		return responsavelRenegociacao;
	}

	public void setResponsavelRenegociacao(UsuarioVO responsavelRenegociacao) {
		this.responsavelRenegociacao = responsavelRenegociacao;
	}

	public String getCampoConsultaResponsavelRenegociacao() {
		if (campoConsultaResponsavelRenegociacao == null) {
			campoConsultaResponsavelRenegociacao = "";
		}
		return campoConsultaResponsavelRenegociacao;
	}

	public void setCampoConsultaResponsavelRenegociacao(String campoConsultaResponsavelRenegociacao) {
		this.campoConsultaResponsavelRenegociacao = campoConsultaResponsavelRenegociacao;
	}

	public String getValorConsultaResponsavelRenegociacao() {
		if (valorConsultaResponsavelRenegociacao == null) {
			valorConsultaResponsavelRenegociacao = "";
		}
		return valorConsultaResponsavelRenegociacao;
	}

	public void setValorConsultaResponsavelRenegociacao(String valorConsultaResponsavelRenegociacao) {
		this.valorConsultaResponsavelRenegociacao = valorConsultaResponsavelRenegociacao;
	}

	public List<UsuarioVO> getResponsavelRenegociacaoVOs() {
		if (responsavelRenegociacaoVOs == null) {
			responsavelRenegociacaoVOs = new ArrayList<UsuarioVO>(0);
		}
		return responsavelRenegociacaoVOs;
	}

	public void setResponsavelRenegociacaoVOs(List<UsuarioVO> responsavelRenegociacaoVOs) {
		this.responsavelRenegociacaoVOs = responsavelRenegociacaoVOs;
	}

	public List<SelectItem> getListaSelectItemResponsavelRenegociacao() {
		List<SelectItem> objs = new ArrayList<SelectItem>(0);
		objs.add(new SelectItem("nome", "Nome"));
		return objs;
	}

	@SuppressWarnings("unchecked")
	public void consultarResponsavelRenegociacao() throws Exception {
		try {
			setResponsavelRenegociacaoVOs(getFacadeFactory().getUsuarioFacade().consultarPorNome(getValorConsultaResponsavelRenegociacao(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void limparDadosResponsavelRenegociacao() {
		setResponsavelRenegociacao(null);
	}

	public void selecionarResponsavelRenegociacao() {
		setResponsavelRenegociacao((UsuarioVO) context().getExternalContext().getRequestMap().get("responsavelRenegociacaoItens"));
		setResponsavelRenegociacaoVOs(null);
		setValorConsultaResponsavelRenegociacao("");
	}
	
	public Boolean getImprimirExcel() {
		if (imprimirExcel == null) {
			imprimirExcel = Boolean.FALSE;
		}
		return imprimirExcel;
	}
	
	public void setImprimirExcel(Boolean imprimirExcel) {
		this.imprimirExcel = imprimirExcel;
	}
	
	public void imprimirExcel() throws Exception {
		setImprimirExcel(Boolean.TRUE);
		imprimirPDF();
	}

}