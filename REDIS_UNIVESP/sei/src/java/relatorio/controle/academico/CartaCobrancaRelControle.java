package relatorio.controle.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.ImpressaoContratoVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TextoPadraoDeclaracaoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.basico.LayoutEtiquetaVO;
import negocio.comuns.basico.enumeradores.ModuloLayoutEtiquetaEnum;
import negocio.comuns.financeiro.CartaCobrancaAlunoVO;
import negocio.comuns.financeiro.CartaCobrancaRelVO;
import negocio.comuns.financeiro.CartaCobrancaVO;
import negocio.comuns.financeiro.CentroReceitaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.financeiro.FiltroRelatorioFinanceiroVO;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;

@Controller("CartaCobrancaRelControle")
@Scope("viewScope")
@Lazy
public class CartaCobrancaRelControle extends SuperControleRelatorio {

	private static final long serialVersionUID = 1L;
	private UnidadeEnsinoVO unidadeEnsino;
	private MatriculaVO matricula;
	private UnidadeEnsinoCursoVO unidadeEnsinoCurso;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private List<MatriculaVO> listaConsultaAluno;
	private String campoConsultaAluno;
	private String filtroConsultaAluno;
	private Date periodoInicial;
	private Date periodoFinal;
	// MAURO
	private CursoVO cursoVO;
	private String campoConsultaCurso;
	private String valorConsultaCursos;
	List<SelectItem> tipoConsultaComboCurso;
	private List<CursoVO> listaConsultaCurso;
	private TurmaVO turmaVO;
	private String campoConsultaTurma;
	private String valorConsultaTurma;
	List<SelectItem> tipoConsultaComboTurma;
	private List<TurmaVO> listaConsultaTurma;
	private CartaCobrancaRelVO cartaCobrancaRel;
	private List<CartaCobrancaAlunoVO> listaCartaCobrancaAluno;
	private FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO;
	private FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO;
	private List<SelectItem> listaSelectItemTipoLayout;
	private String tipoLayout;
	private LayoutEtiquetaVO layoutEtiquetaVO;
	private List<SelectItem> listaSelectItemlayoutEtiqueta;
	private List<SelectItem> listaSelectItemColuna;
	private List<SelectItem> listaSelectItemLinha;
	private Integer numeroCopias;
	private Integer coluna;
	private Integer linha;
	private Boolean marcarTodasSituacoesFinanceiras;
	private String layout;
	private List<SelectItem> listaSelectItemTipoTextoPadrao;
	private Integer textoPadraoDeclaracao;
	
	private List<CentroReceitaVO> centroReceitaVOs;
	private Boolean marcarTodosCentroReceitas;

	CartaCobrancaRelControle() {
		carregarListaUnidadeEnsino();
		getControleConsulta().setCampoConsulta("dataGeracao");
	}
	
	@PostConstruct
	public void realizarImpressaoCartaCobrancaVindoTelaFichaAluno() {
		MatriculaVO obj = (MatriculaVO) context().getExternalContext().getSessionMap().get("matriculaImprimirCartaCobrancaFichaAluno");
		if (obj != null && !obj.getMatricula().equals("")) {
			try {
				novo();
				setUnidadeEnsino(obj.getUnidadeEnsino());
				setCursoVO(obj.getCurso());
				getMatricula().setMatricula(obj.getMatricula());
				consultarAlunoPorMatricula();
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			} finally {
				context().getExternalContext().getSessionMap().remove("matriculaImprimirCartaCobrancaFichaAluno");
			}
		}
	}


	public void carregarListaUnidadeEnsino() {
		try {
			setListaSelectItemUnidadeEnsino(getFacadeFactory().getCartaCobrancaRelFacade().montarListaSelectItemUnidadeEnsino(getUnidadeEnsinoLogado(), getUsuarioLogado()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarAluno() {
		try {
			List<MatriculaVO> objs = new ArrayList<MatriculaVO>(0);
			if (getUnidadeEnsino() == null || getUnidadeEnsino().getCodigo() == 0) {
				throw new Exception("Deve ser informado a Uidade de Ensino.");
			}
			if (getCampoConsultaAluno().equals("")) {
				throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
			}
			if (getFiltroConsultaAluno().equals("matricula")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatricula(getCampoConsultaAluno(), getUnidadeEnsino().getCodigo(), false, getUsuarioLogado());
			}
			if (getFiltroConsultaAluno().equals("nomePessoa")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getCampoConsultaAluno(), getUnidadeEnsino().getCodigo(), false, getUsuarioLogado());
			}
			if (getFiltroConsultaAluno().equals("nomeCurso")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCurso(getCampoConsultaAluno(), getUnidadeEnsino().getCodigo(), false, getUsuarioLogado());
			}
			setListaConsultaAluno(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAluno(new ArrayList<MatriculaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarAluno() throws Exception {
		MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
		obj = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula(), obj.getUnidadeEnsino().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado());
		setMatricula(obj);
		setCursoVO(obj.getCurso());
		getUnidadeEnsino().setCodigo(obj.getUnidadeEnsino().getCodigo());
		setFiltroConsultaAluno("");
		setCampoConsultaAluno("");
		getListaConsultaAluno().clear();
	}

	public void imprimirPDF() {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "Carta Cobrança Rel ", "Iniciando Impressao PDF", "Emitindo Relatorio");
			getFacadeFactory().getCartaCobrancaRelFacade().validarDados(getUnidadeEnsino(), getCursoVO(), getPeriodoFinal());
			List<CartaCobrancaRelVO> listaObjetos = getFacadeFactory().getCartaCobrancaRelFacade().criarObjeto(getUnidadeEnsino(), getCursoVO(), getTurmaVO(), getMatricula(), getCartaCobrancaRel(), getUsuarioLogado(), getPeriodoInicial(), getPeriodoFinal(), getMatricula().getAluno().getNome(), getFiltroRelatorioAcademicoVO(), getFiltroRelatorioFinanceiroVO(), centroReceitaVOs, getCartaCobrancaVO().getCentroReceitaApresentar());
			if (!listaObjetos.isEmpty()) {
				if (getLayout().equals("TextoPadrao")) {
					TextoPadraoDeclaracaoVO textoPadraoDeclaracaoVO = getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorChavePrimaria(textoPadraoDeclaracao, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
					textoPadraoDeclaracaoVO.setObjetos(listaObjetos);
					String caminhoRelatorio = "";		
					ImpressaoContratoVO impressaoContratoVO = new ImpressaoContratoVO();
					caminhoRelatorio = getFacadeFactory().getImpressaoDeclaracaoFacade().executarValidacaoImpressaoEmPdf(impressaoContratoVO, textoPadraoDeclaracaoVO, "", true, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
					setCaminhoRelatorio(caminhoRelatorio);
					if (getCaminhoRelatorio().isEmpty()) {
						setFazerDownload(false);
					} else {
						setFazerDownload(true);
					}
				} else {
					getCartaCobrancaRel().setNomeDesignIreport(getFacadeFactory().getCartaCobrancaRelFacade().designIReportRelatorio(getTipoLayout()));
					getCartaCobrancaRel().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
					getCartaCobrancaRel().setSubReport_Dir(getFacadeFactory().getCartaCobrancaRelFacade().caminhoBaseRelatorio());
					getCartaCobrancaRel().setNomeUsuario(getUsuarioLogado().getNome());
					getCartaCobrancaRel().setTituloRelatorio("CARTA DE COBRANÇA");
					getCartaCobrancaRel().setListaObjetos(listaObjetos);
					getCartaCobrancaRel().setCaminhoBaseRelatorio(getFacadeFactory().getCartaCobrancaRelFacade().caminhoBaseRelatorio());
					getCartaCobrancaRel().setVersaoSoftware(getVersaoSistema());
					realizarImpressaoRelatorio(getCartaCobrancaRel());
				}
				removerObjetoMemoria(this);
				novo();
				setMensagemID("msg_relatorio_ok");
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}
			registrarAtividadeUsuario(getUsuarioLogado(), "Carta Cobrança Rel", "Finalizando Impressao PDF", "Emitindo Relatorio");
		} catch (Exception e) {
			setFazerDownload(false);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
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

	public UnidadeEnsinoVO getUnidadeEnsino() {
		if (unidadeEnsino == null) {
			unidadeEnsino = new UnidadeEnsinoVO();
		}
		return unidadeEnsino;
	}

	public void setUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
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

	public List<SelectItem> getListaSelectIntensAluno() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nomePessoa", "Aluno"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
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

	public MatriculaVO getMatricula() {
		if (matricula == null) {
			matricula = new MatriculaVO();
		}
		return matricula;
	}

	public void setMatricula(MatriculaVO matricula) {
		this.matricula = matricula;
	}

	public UnidadeEnsinoCursoVO getUnidadeEnsinoCursoVO() {
		if (unidadeEnsinoCurso == null) {
			unidadeEnsinoCurso = new UnidadeEnsinoCursoVO();
		}
		return unidadeEnsinoCurso;
	}

	public void setUnidadeEnsinoCursoVO(UnidadeEnsinoCursoVO unidadeEnsinoCurso) {
		this.unidadeEnsinoCurso = unidadeEnsinoCurso;
	}

	public String getFiltroConsultaAluno() {
		if (filtroConsultaAluno == null) {
			filtroConsultaAluno = "";
		}
		return filtroConsultaAluno;
	}

	public void setFiltroConsultaAluno(String filtroConsultaAluno) {
		this.filtroConsultaAluno = filtroConsultaAluno;
	}

	public Date getPeriodoFinal() {
		if (periodoFinal == null) {
			periodoFinal = new Date();
		}
		return periodoFinal;
	}

	public void setPeriodoFinal(Date periodoFinal) {
		this.periodoFinal = periodoFinal;
	}

	public Date getPeriodoInicial() {
		if (periodoInicial == null) {
			periodoInicial = new Date();
		}
		return periodoInicial;
	}

	public void setPeriodoInicial(Date periodoInicial) {
		this.periodoInicial = periodoInicial;
	}

	// MAURO VASCONCELOS

	public void consultarTurma() {
		try {
//			if (!Uteis.isAtributoPreenchido(getCursoVO())) {
//				throw new ConsistirException("O campo Curso (Relatório Carta de Cobrança) deve ser informado.");
//			}
			List<TurmaVO> objs = new ArrayList<TurmaVO>(0);
			if (getCampoConsultaTurma().equals("codigo")) {
				if (getCampoConsultaTurma().equals("")) {
					setValorConsultaTurma("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaTurma());
				objs = getFacadeFactory().getTurmaFacade().consultarPorCodigoTurmaCursoEUnidadeEnsino(valorInt, getCursoVO().getCodigo(), getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nome")) {
				objs = getFacadeFactory().getTurmaFacade().consultarPorNomeTurmaCursoEUnidadeEnsino(getValorConsultaTurma(), getCursoVO().getCodigo(), getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			setListaConsultaTurma(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaTurma(new ArrayList<TurmaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}

	public void selecionarCurso() throws Exception {
		try {
			CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItens");
			setCursoVO(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, getUsuarioLogado()));
			getUnidadeEnsinoCursoVO().setCurso(getCursoVO());
			setCampoConsultaCurso("");
			setValorConsultaCursos("");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarTurma() throws Exception {
		try {
			TurmaVO turma = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
			setTurmaVO(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(turma.getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			setCursoVO(getFacadeFactory().getCursoFacade().consultarCursoPorTurma(turma.getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			setCampoConsultaTurma("");
			setCampoConsultaTurma("");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparCurso() throws Exception {
		try {
			setCursoVO(new CursoVO());
			setListaConsultaCurso(null);
			limparTurma();
			limparDados();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparTurma() throws Exception {
		try {
			setTurmaVO(new TurmaVO());
			setListaConsultaTurma(null);
			setCursoVO(new CursoVO());
		} catch (Exception e) {
		}
	}

	public void limparDadosAluno() {
		setMatricula(null);
//		setUnidadeEnsino(null);
//		setListaConsultaTurma(null);
//		setCursoVO(null);
//		setTurmaVO(null);
//		setListaConsultaCurso(null);
	}

	public void consultarCurso() {
		try {
			if (!Uteis.isAtributoPreenchido(getUnidadeEnsino())) {
				throw new ConsistirException("O campo Unidade De Ensino (Relatório Carta de Cobrança) deve ser informado.");
			}
			List<CursoVO> objs = new ArrayList<CursoVO>(0);
			if (getCampoConsultaCurso().equals("codigo")) {
				if (getValorConsultaCursos().equals("")) {
					setValorConsultaCursos("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaCursos());
				objs = getFacadeFactory().getCursoFacade().consultarCursoPorCodigoUnidadeEnsino(valorInt, getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			if (getCampoConsultaCurso().equals("nome")) {
				objs = getFacadeFactory().getCursoFacade().consultarPorNomeCursoUnidadeEnsinoBasica(getValorConsultaCursos(), getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			setListaConsultaCurso(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList<CursoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void montarListaCartaCobranca() throws Exception {
		setListaCartaCobrancaAluno(getFacadeFactory().getCartaCobrancaAlunoFacade().consultarPorCartaCobranca(getCartaCobrancaVO().getCodigo(), false, getUsuarioLogado()));
	}

	public void limparDados() throws Exception {
		setCursoVO(null);
		setListaConsultaCurso(null);
		setTurmaVO(null);
		setListaConsultaTurma(null);
		setMatricula(null);
		setListaConsultaAluno(null);
		if (getUnidadeEnsino().getCodigo().intValue() > 0) {
			setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
		}
		setMensagemID("msg_entre_dados");
	}

	public String inicializarConsultar() {
		getControleConsulta().setCampoConsulta("dataGeracao");
		getListaConsulta().clear();
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("cartaCobrancaCons.xhtml");
	}

	public CursoVO getCursoVO() {
		if (cursoVO == null) {
			cursoVO = new CursoVO();
		}
		return cursoVO;
	}

	public void setCursoVO(CursoVO cursoVO) {
		this.cursoVO = cursoVO;
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

	public TurmaVO getTurmaVO() {
		if (turmaVO == null) {
			turmaVO = new TurmaVO();
		}
		return turmaVO;
	}

	public void setTurmaVO(TurmaVO turmaVO) {
		this.turmaVO = turmaVO;
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

	public List<SelectItem> getTipoConsultaComboCurso() {
		if (tipoConsultaComboCurso == null) {
			tipoConsultaComboCurso = new ArrayList<SelectItem>(0);
			tipoConsultaComboCurso.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboCurso.add(new SelectItem("codigo", "Código"));
		}
		return tipoConsultaComboCurso;
	}

	public List<SelectItem> getTipoConsultaComboTurma() {
		if (tipoConsultaComboTurma == null) {
			tipoConsultaComboTurma = new ArrayList<SelectItem>(0);
			tipoConsultaComboTurma.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboTurma.add(new SelectItem("codigo", "Código"));
		}
		return tipoConsultaComboTurma;
	}

	public String getValorConsultaCursos() {
		if (valorConsultaCursos == null) {
			valorConsultaCursos = "";
		}
		return valorConsultaCursos;
	}

	public void setValorConsultaCursos(String valorConsultaCursos) {
		this.valorConsultaCursos = valorConsultaCursos;
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

	public List<TurmaVO> getListaConsultaTurma() {
		if (listaConsultaTurma == null) {
			listaConsultaTurma = new ArrayList<TurmaVO>();
		}
		return listaConsultaTurma;
	}

	public void setListaConsultaTurma(List<TurmaVO> listaConsultaTurma) {
		this.listaConsultaTurma = listaConsultaTurma;
	}

	public CartaCobrancaRelVO getCartaCobrancaRel() {
		if (cartaCobrancaRel == null) {
			cartaCobrancaRel = new CartaCobrancaRelVO();
		}
		return cartaCobrancaRel;
	}

	public void setCartaCobrancaRel(CartaCobrancaRelVO cartaCobrancaRel) {
		this.cartaCobrancaRel = cartaCobrancaRel;
	}

	// public void novo() {
	// incializarDados();
	// setMensagemID("msg_entre_prmconsulta");
	// }

	public void incializarDados() {
		limparDadosAluno();
		setTurmaVO(new TurmaVO());
		carregarListaUnidadeEnsino();

	}

	public List<CartaCobrancaAlunoVO> getListaCartaCobrancaAluno() {
		if (listaCartaCobrancaAluno == null) {
			listaCartaCobrancaAluno = new ArrayList<CartaCobrancaAlunoVO>();
		}
		return listaCartaCobrancaAluno;
	}

	public void setListaCartaCobrancaAluno(List<CartaCobrancaAlunoVO> listaCartaCobrancaAluno) {
		this.listaCartaCobrancaAluno = listaCartaCobrancaAluno;
	}

	// Outra Tela

	private CartaCobrancaVO cartaCobrancaVO;
	private Date dataGeracaoInicio;
	private Date dataGeracaoFim;
	private String campoConsulta;
	private String valorConsulta;

	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("dataGeracao", "Data Geração Relatório"));
		itens.add(new SelectItem("unidadeEnsino", "Unidade Ensino"));
		itens.add(new SelectItem("curso", "Curso"));
		itens.add(new SelectItem("turma", "Turma"));
		itens.add(new SelectItem("aluno", "Aluno"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("usuario", "Usuário"));
		return itens;
	}

	@SuppressWarnings("rawtypes")
	public String consultar() {
		try {
			super.consultar();
			List<CartaCobrancaVO> objs = null;
			if (getControleConsulta().getCampoConsulta().equals("dataGeracao")) {
				objs = getFacadeFactory().getCartaCobrancaFacade().consultarPorDataGeracao(getDataGeracaoInicio(), getDataGeracaoFim(), false, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("unidadeEnsino")) {
				objs = getFacadeFactory().getCartaCobrancaFacade().consultarPorUnidadeEnsino(getControleConsulta().getValorConsulta(), false, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("curso")) {
				objs = getFacadeFactory().getCartaCobrancaFacade().consultarPorCurso(getControleConsulta().getValorConsulta(), false, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("turma")) {
				objs = getFacadeFactory().getCartaCobrancaFacade().consultarPorTurma(getControleConsulta().getValorConsulta(), false, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("aluno")) {
				objs = getFacadeFactory().getCartaCobrancaFacade().consultarPorAluno(getControleConsulta().getValorConsulta(), false, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("matricula")) {
				objs = getFacadeFactory().getCartaCobrancaFacade().consultarPorMatricula(getControleConsulta().getValorConsulta(), false, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("usuario")) {
				objs = getFacadeFactory().getCartaCobrancaFacade().consultarPorUsuario(getControleConsulta().getValorConsulta(), false, getUsuarioLogado());
			}
			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("cartaCobrancaCons.xhtml");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("cartaCobrancaCons.xhtml");
		}
	}

	public String editar() {
		try {
			CartaCobrancaVO obj = (CartaCobrancaVO) context().getExternalContext().getRequestMap().get("cartaItens");
			getCartaCobrancaVO().setCodigo(obj.getCodigo());
			getCartaCobrancaVO().setCentroReceitaApresentar(obj.getCentroReceitaApresentar());
			getCartaCobrancaVO().setEditar(Boolean.FALSE);
			setCursoVO(obj.getCursoVO());
			setTurmaVO(obj.getTurmaVO());
			setUnidadeEnsino(obj.getUnidadeEnsinoVO());
			setPeriodoInicial(obj.getDataInicioFiltro());
			setPeriodoFinal(obj.getDataFimFiltro());
			getMatricula().setMatricula(obj.getMatricula());
			getMatricula().getAluno().setNome(obj.getAluno());
			setFiltroRelatorioAcademicoVO(obj.getFiltroRelatorioAcademicoVO());
			setFiltroRelatorioFinanceiroVO(obj.getFiltroRelatorioFinanceiroVO());
			carregarListaUnidadeEnsino();
			montarListaCartaCobranca();
			consultarCentroReceitaFiltroRelatorio();
			if(!obj.getCentroReceitaApresentar().trim().isEmpty()) {
				for(CentroReceitaVO centroReceitaVO: getCentroReceitaVOs()) {
					if(obj.getCentroReceitaApresentar().contains(centroReceitaVO.getDescricao())) {
						centroReceitaVO.setFiltrarCentroReceitaVO(true);
					}
				}
			}
			setMensagemID("msg_entre_prmconsulta");
			return Uteis.getCaminhoRedirecionamentoNavegacao("cartaCobrancaRel.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}

	public void irPaginaInicial() throws Exception {
		removerObjetoMemoria(getCartaCobrancaVO());
		this.consultar();
	}

	public CartaCobrancaVO getCartaCobrancaVO() {
		if (cartaCobrancaVO == null) {
			cartaCobrancaVO = new CartaCobrancaVO();
		}
		return cartaCobrancaVO;
	}

	public void setCartaCobrancaVO(CartaCobrancaVO cartaCobrancaVO) {
		this.cartaCobrancaVO = cartaCobrancaVO;
	}

	public String getCampoConsulta() {
		if (campoConsulta == null) {
			campoConsulta = "";
		}
		return campoConsulta;
	}

	public void setCampoConsulta(String campoConsulta) {
		this.campoConsulta = campoConsulta;
	}

	public String getValorConsulta() {
		if (valorConsulta == null) {
			valorConsulta = "";
		}
		return valorConsulta;
	}

	public void setValorConsulta(String valorConsulta) {
		this.valorConsulta = valorConsulta;
	}

	public Boolean getApresentarCalenderDataGeracao() {
		return getControleConsulta().getCampoConsulta().equals("dataGeracao");
	}

	public Date getDataGeracaoInicio() {
		if (dataGeracaoInicio == null) {
			dataGeracaoInicio = new Date();
		}
		return dataGeracaoInicio;
	}

	public void setDataGeracaoInicio(Date dataGeracaoInicio) {
		this.dataGeracaoInicio = dataGeracaoInicio;
	}

	public Date getDataGeracaoFim() {
		if (dataGeracaoFim == null) {
			dataGeracaoFim = new Date();
		}
		return dataGeracaoFim;
	}

	public void setDataGeracaoFim(Date dataGeracaoFim) {
		this.dataGeracaoFim = dataGeracaoFim;
	}

	public String novo() throws Exception {
		setCartaCobrancaVO(new CartaCobrancaVO());
		setFiltroRelatorioAcademicoVO(new FiltroRelatorioAcademicoVO());
		setFiltroRelatorioFinanceiroVO(new FiltroRelatorioFinanceiroVO(getLoginControle().getPermissaoAcessoMenuVO().getPermitirApenasContasDaBiblioteca()));
		consultarCentroReceitaFiltroRelatorio();
		setMarcarTodasSituacoesAcademicas(false);
		setMarcarTodasSituacoesFinanceiras(false);
		setCursoVO(new CursoVO());
		setListaConsultaCurso(null);
		setTurmaVO(new TurmaVO());
		setListaConsultaTurma(null);
		setMatricula(new MatriculaVO());
		setListaConsultaAluno(null);
		getListaCartaCobrancaAluno().clear();
		setUnidadeEnsino(new UnidadeEnsinoVO());
		setPeriodoInicial(new Date());
		setPeriodoFinal(new Date());
		carregarListaUnidadeEnsino();
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("cartaCobrancaRel.xhtml");
	}

	public void consultarAlunoPorMatricula() {
		try {
			MatriculaVO obj = getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatriculaUnica(getMatriculaVO().getMatricula(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
//			MatriculaVO obj = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(getMatricula().getMatricula(), getUnidadeEnsino().getCodigo(), 0, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			if (obj.getMatricula().equals("")) {
				throw new Exception("Aluno de matrícula " + getMatriculaVO().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
			}
			setMatricula(obj);
			setCursoVO(obj.getCurso());
			setUnidadeEnsino(obj.getUnidadeEnsino());
			setFiltroConsultaAluno("");
			setCampoConsultaAluno("");
			getListaConsultaAluno().clear();
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setMatriculaVO(new MatriculaVO());
		}
	}

	public void realizarGeracaoEtiqueta() {
		setFazerDownload(false);
		try {
			getFacadeFactory().getCartaCobrancaRelFacade().validarDados(getUnidadeEnsino(), getCursoVO(), getPeriodoFinal());
			String tipoRelatorio = "cartaCobranca";	
			setCaminhoRelatorio(getFacadeFactory().getEtiquetaAlunoRelFacade().realizarImpressaoEtiquetaCartaCobranca(getLayoutEtiquetaVO(), getCartaCobrancaRel(), getPeriodoInicial(), getPeriodoFinal(), getFiltroRelatorioAcademicoVO(), getFiltroRelatorioFinanceiroVO(), getNumeroCopias(), getLinha(), getColuna(), getCursoVO().getCodigo(), getTurmaVO().getCodigo(), getMatricula().getMatricula(), "", "", 0, tipoRelatorio, getCursoVO().getNivelEducacional(), getCentroReceitaVOs(), getUnidadeEnsino(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado()));
			super.setFazerDownload(true);
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

//	public void validarDadosAnoSemestre() throws Exception {
//		if (getIsApresentarAno().equals(true)) {
//			if (getAno().equals(null) || getAno().equals("")) {
//				throw new Exception("O campo ANO deve ser informado.");
//			}
//		}
//		if (getIsApresentarSemestre().equals(true)) {
//			if (getSemestre().equals(null) || getSemestre().equals("")) {
//				throw new Exception("O campo SEMESTRE deve ser informado.");
//			}
//		}
//	}
	

	public void inicializarDadosLayoutEtiqueta() {
		try {
			getListaSelectItemColuna().clear();
			getListaSelectItemLinha().clear();
			if (getLayoutEtiquetaVO().getCodigo() > 0) {
				setLayoutEtiquetaVO(getFacadeFactory().getLayoutEtiquetaFacade().consultarPorChavePrimaria(getLayoutEtiquetaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado()));
				for (int x = 1; x <= getLayoutEtiquetaVO().getNumeroLinhasEtiqueta(); x++) {
					getListaSelectItemLinha().add(new SelectItem(x, String.valueOf(x)));
				}
				for (int y = 1; y <= getLayoutEtiquetaVO().getNumeroColunasEtiqueta(); y++) {
					getListaSelectItemColuna().add(new SelectItem(y, String.valueOf(y)));
				}
			}

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public List<SelectItem> getListaSelectItemlayoutEtiqueta() {
		if (listaSelectItemlayoutEtiqueta == null) {
			listaSelectItemlayoutEtiqueta = new ArrayList<SelectItem>(0);
			try {
				List<LayoutEtiquetaVO> layoutEtiquetaVOs = getFacadeFactory().getLayoutEtiquetaFacade().consultarRapidaPorModulo(ModuloLayoutEtiquetaEnum.MATRICULA, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
				listaSelectItemlayoutEtiqueta.add(new SelectItem(0, ""));
				for (LayoutEtiquetaVO layoutEtiquetaVO : layoutEtiquetaVOs) {
					listaSelectItemlayoutEtiqueta.add(new SelectItem(layoutEtiquetaVO.getCodigo(), layoutEtiquetaVO.getDescricao()));

				}
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
		return listaSelectItemlayoutEtiqueta;
	}
	
	public void consultarLayoutEtiquetaPorModulo() {
		try {
			getListaSelectItemlayoutEtiqueta().clear();
			List<LayoutEtiquetaVO> layoutEtiquetaVOs = getFacadeFactory().getLayoutEtiquetaFacade().consultarRapidaPorModulo(ModuloLayoutEtiquetaEnum.CARTA_COBRANCA, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
			listaSelectItemlayoutEtiqueta.add(new SelectItem(0, ""));
			for (LayoutEtiquetaVO layoutEtiquetaVO : layoutEtiquetaVOs) {
				listaSelectItemlayoutEtiqueta.add(new SelectItem(layoutEtiquetaVO.getCodigo(), layoutEtiquetaVO.getDescricao()));
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}

	}
	
	public FiltroRelatorioAcademicoVO getFiltroRelatorioAcademicoVO() {
		if (filtroRelatorioAcademicoVO == null) {
			filtroRelatorioAcademicoVO = new FiltroRelatorioAcademicoVO();
		}
		return filtroRelatorioAcademicoVO;
	}

	public void setFiltroRelatorioAcademicoVO(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO) {
		this.filtroRelatorioAcademicoVO = filtroRelatorioAcademicoVO;
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

	public List<SelectItem> getListaSelectItemTipoLayout() {
		if (listaSelectItemTipoLayout == null) {
			listaSelectItemTipoLayout = new ArrayList<SelectItem>(0);
			listaSelectItemTipoLayout.add(new SelectItem("paisagem", "Paisagem"));
			listaSelectItemTipoLayout.add(new SelectItem("retrato", "Retrato"));
		}
		return listaSelectItemTipoLayout;
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
	
	public List<SelectItem> getListaSelectItemColuna() {
		if (listaSelectItemColuna == null) {
			listaSelectItemColuna = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemColuna;
	}

	public void setListaSelectItemColuna(List<SelectItem> listaSelectItemColuna) {
		this.listaSelectItemColuna = listaSelectItemColuna;
	}

	public List<SelectItem> getListaSelectItemLinha() {
		if (listaSelectItemLinha == null) {
			listaSelectItemLinha = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemLinha;
	}

	public void setListaSelectItemLinha(List<SelectItem> listaSelectItemLinha) {
		this.listaSelectItemLinha = listaSelectItemLinha;
	}

	public LayoutEtiquetaVO getLayoutEtiquetaVO() {
		if (layoutEtiquetaVO == null) {
			layoutEtiquetaVO = new LayoutEtiquetaVO();
		}
		return layoutEtiquetaVO;
	}

	public void setLayoutEtiquetaVO(LayoutEtiquetaVO layoutEtiquetaVO) {
		this.layoutEtiquetaVO = layoutEtiquetaVO;
	}

	public Integer getNumeroCopias() {
		if (numeroCopias == null) {
			numeroCopias = 1;
		}
		return numeroCopias;
	}

	public void setNumeroCopias(Integer numeroCopias) {
		this.numeroCopias = numeroCopias;
	}

	public Integer getColuna() {
		if (coluna == null) {
			coluna = 1;
		}
		return coluna;
	}

	public void setColuna(Integer coluna) {
		this.coluna = coluna;
	}

	public Integer getLinha() {
		if (linha == null) {
			linha = 1;
		}
		return linha;
	}

	public void setLinha(Integer linha) {
		this.linha = linha;
	}
	
	public void realizarSelecaoCheckboxMarcarDesmarcarTodosSituacaoFinanceira() {
		if (getMarcarTodasSituacoesFinanceiras()) {
			getFiltroRelatorioFinanceiroVO().realizarMarcarTodosTipoOrigem();
		} else {
			getFiltroRelatorioFinanceiroVO().realizarDesmarcarTodosTipoOrigem();
		}
	}

	public Boolean getMarcarTodasSituacoesFinanceiras() {
		if (marcarTodasSituacoesFinanceiras == null) {
			marcarTodasSituacoesFinanceiras = false;
		}
		return marcarTodasSituacoesFinanceiras;
	}

	public void setMarcarTodasSituacoesFinanceiras(Boolean marcarTodasSituacoesFinanceiras) {
		this.marcarTodasSituacoesFinanceiras = marcarTodasSituacoesFinanceiras;
	}
	
	public String getIsApresentarTextoCheckBoxMarcarDesmarcarTodosFinanceiro() {
		if (getMarcarTodasSituacoesFinanceiras()) {
			return UteisJSF.internacionalizar("prt_Inadimplencia_desmarcarTodos");
		}
		return UteisJSF.internacionalizar("prt_Inadimplencia_marcarTodos");
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
	
    public List<SelectItem> getListaLayout() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
			itens.add(new SelectItem("CartaCobrancaRel", "Layout 1"));
			itens.add(new SelectItem("TextoPadrao", "Texto Padrão"));
		return itens;
	}

	public List<SelectItem> getListaSelectItemTipoTextoPadrao() {
		if (listaSelectItemTipoTextoPadrao == null) {
			listaSelectItemTipoTextoPadrao = new ArrayList<SelectItem>();
		}
		return listaSelectItemTipoTextoPadrao;
	}

	public void setListaSelectItemTipoTextoPadrao(List<SelectItem> listaSelectItemTipoTextoPadrao) {
		this.listaSelectItemTipoTextoPadrao = listaSelectItemTipoTextoPadrao;
	}
	
    public void montarListaSelectItemTipoTextoPadrao() {
    	try {
            if (getLayout().equals("TextoPadrao")) {
            	consultarListaSelectItemTipoTextoPadrao(0);
            }
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
    }
	
    public void consultarListaSelectItemTipoTextoPadrao(Integer unidadeEnsino) {
        try {
            getListaSelectItemTipoTextoPadrao().clear();
            List<TextoPadraoDeclaracaoVO> lista = getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorTipo("CO", unidadeEnsino, "", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            for (TextoPadraoDeclaracaoVO objeto : lista) {
                getListaSelectItemTipoTextoPadrao().add(new SelectItem(objeto.getCodigo(), objeto.getDescricao()));
            }
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }
    
	public Integer getTextoPadraoDeclaracao() {
		if (textoPadraoDeclaracao == null) {
			textoPadraoDeclaracao = 0;
		}
		return textoPadraoDeclaracao;
	}

	public void setTextoPadraoDeclaracao(Integer textoPadraoDeclaracao) {
		this.textoPadraoDeclaracao = textoPadraoDeclaracao;
	}
	
	public void marcarTodosCentroReceitasAction() {
		for (CentroReceitaVO centroReceitaVO : centroReceitaVOs) {
			centroReceitaVO.setFiltrarCentroReceitaVO(getMarcarTodosCentroReceitas());
		}
		verificarTodosCentroReceitaSelecionados();
	}
	
	public void verificarTodosCentroReceitaSelecionados() {
		StringBuilder centroReceita = new StringBuilder();
		if (centroReceitaVOs.size() > 1) {
			for (CentroReceitaVO obj : centroReceitaVOs) {
				if (obj.getFiltrarCentroReceitaVO()) {
					centroReceita.append(obj.getDescricao().trim()).append("; ");
				}
			}
			getCartaCobrancaVO().setCentroReceitaApresentar(centroReceita.toString());
		} else {
			if (!centroReceitaVOs.isEmpty()) {
				if (centroReceitaVOs.get(0).getFiltrarCentroReceitaVO()) {
					getCartaCobrancaVO().setCentroReceitaApresentar(centroReceitaVOs.get(0).getDescricao().trim());
				}
			} else {
				getCartaCobrancaVO().setCentroReceitaApresentar(centroReceita.toString());
			}
		}
	}
	
	public Boolean getMarcarTodosCentroReceitas() {
		if (marcarTodosCentroReceitas == null) {
			marcarTodosCentroReceitas = false;
		}
		return marcarTodosCentroReceitas;
	}
	
	public void setMarcarTodosCentroReceitas(Boolean marcarTodosCentroReceitas) {
		this.marcarTodosCentroReceitas = marcarTodosCentroReceitas;
	}
	
	public List<CentroReceitaVO> getCentroReceitaVOs() {
		if (centroReceitaVOs == null) {
			centroReceitaVOs = new ArrayList(0);
		}
		return centroReceitaVOs;
	}
	
	public void setCentroReceitaVOs(List<CentroReceitaVO> centroReceitaVOs) {
		this.centroReceitaVOs = centroReceitaVOs;
	}
	
	public void consultarCentroReceitaFiltroRelatorio() {
		try {
			setCentroReceitaVOs(getFacadeFactory().getCentroReceitaFacade().consultarCentroReceitaVinculadoContaReceber(null, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void limparCentroReceita() {
		getCartaCobrancaVO().setCentroReceitaApresentar("");
		setMarcarTodosCentroReceitas(false);
		marcarTodosCentroReceitasAction();
	}
	
	private CartaCobrancaAlunoVO cartaCobrancaAlunoVO;

	public CartaCobrancaAlunoVO getCartaCobrancaAlunoVO() {
		if (cartaCobrancaAlunoVO == null) {
			cartaCobrancaAlunoVO = new CartaCobrancaAlunoVO();
		}
		return cartaCobrancaAlunoVO;
	}

	public void setCartaCobrancaAlunoVO(CartaCobrancaAlunoVO cartaCobrancaAlunoVO) {
		this.cartaCobrancaAlunoVO = cartaCobrancaAlunoVO;
	}
	
	public MatriculaVO getMatriculaVO() {
		if (matricula == null) {
			matricula = new MatriculaVO();
		}
		return matricula;
	}
	
	public void setMatriculaVO(MatriculaVO matriculaVO) {
		this.matricula = matriculaVO;
	}
 	
}
