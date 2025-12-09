package relatorio.controle.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.academico.VisaoCoordenadorControle;
import controle.academico.VisaoProfessorControle;
import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.HorarioTurmaDisciplinaProgramadaVO;
import negocio.comuns.academico.ProfessorTitularDisciplinaTurmaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.SituacaoRecuperacaoNotaEnum;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilReflexao;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.academico.AtaProvaRelVO;
import relatorio.negocio.comuns.arquitetura.SuperParametroRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.academico.AtaProvaRel;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;

@Controller("AtaProvaRelControle")
@Scope("viewScope")
@Lazy
public class AtaProvaRelControle extends SuperControleRelatorio {

	private static final long serialVersionUID = 1L;
	private TurmaVO turmaVO;
	private DisciplinaVO disciplinaVO;
	private UnidadeEnsinoVO unidadeEnsino;
	private String ano;
	private String semestre;
	private String campoConsultaDisciplina;
	private String valorConsultaDisciplina;
	private List<DisciplinaVO> listaConsultaDisciplina;
	private List<TurmaVO> listaConsultaTurma;
	private String campoConsultaTurma;
	private String valorConsultaTurma;
	private List<SelectItem> listaSelectItemDisciplina;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private List<SelectItem> listaSelectItemTurma;
	private String layout;
	private FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO;
	private String mostrarModalConsultarProfessorTitularDisciplinaTurma;
	private String campoConsultaProfessorTitular;
	private String valorConsultaProfessorTitular;
	private List<FuncionarioVO> listaConsultaProfessorTitular;
	private ProfessorTitularDisciplinaTurmaVO professorTitularDisciplinaTurmaVO;
	private Boolean aprovados;
	private Boolean reprovados;
	private Boolean reprovadosPorFalta;
	private Boolean cursando;
	private Boolean trazerAlunoTransferencia;
	private Boolean marcarTodasSituacoesHistorico;
	private List<SelectItem> listaSelectItemSemestre;
	private List<SelectItem> listaSelectItemTituloNota;
	private List<SelectItem> listaSelectItemSituacaoRecuperacao;
	private SituacaoRecuperacaoNotaEnum situacaoRecuperacaoNota;
	private String tituloNota;

	public AtaProvaRelControle() throws Exception {
	
		setMensagemID("msg_entre_prmrelatorio");
	}
	
	@PostConstruct
	public void inicializarDados() {
		inicializarDadosListaSelectItemUnidadelEnsino();
		try {
			if(getUsuarioLogado().getIsApresentarVisaoAdministrativa()){
				getFacadeFactory().getLayoutPadraoFacade().consultarPadraoFiltroSituacaoAcademica(getFiltroRelatorioAcademicoVO(), AtaProvaRel.getIdEntidade(), getUsuarioLogado());
			}else if(getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
				novoVisaoCoordenador();
			}else if(getUsuarioLogado().getIsApresentarVisaoProfessor()) {
				novoVisaoProfessor();
			}
			Map<String, String> layoutPadraoVOs = getFacadeFactory().getLayoutPadraoFacade().consultarValoresPadroes(new String[] { "layout", "aprovados", "cursando", "reprovados", "reprovadosPorFalta" }, AtaProvaRel.getIdEntidade());
			if (layoutPadraoVOs.containsKey("layout")) {
				setLayout(layoutPadraoVOs.get("layout"));
			}
			if (layoutPadraoVOs.containsKey("aprovados")) {
				setAprovados(Boolean.valueOf(layoutPadraoVOs.get("aprovados")));
			}

			if (layoutPadraoVOs.containsKey("cursando")) {
				setCursando(Boolean.valueOf(layoutPadraoVOs.get("cursando")));
			}

			if (layoutPadraoVOs.containsKey("reprovados")) {
				setReprovados(Boolean.valueOf(layoutPadraoVOs.get("reprovados")));
			}

			if (layoutPadraoVOs.containsKey("reprovadosPorFalta")) {
				setReprovadosPorFalta(Boolean.valueOf(layoutPadraoVOs.get("reprovadosPorFalta")));
			}

		} catch (Exception e) {
			
		}
		setMensagemDetalhada("");
	}

	public void inicializarDadosListaSelectItemUnidadelEnsino() {
		try {
			List<UnidadeEnsinoVO> listaResultado = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorNome("", getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(listaResultado, "codigo", "nome"));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String novoVisaoProfessor() throws Exception {
		// inicializarDados();
		registrarAtividadeUsuario(getUsuarioLogado(), "AtaProvaRelControle", "Novo Visão Professor", "Novo");
		montarListaSelectItemTurmaProfessor();
		executarMetodoControle(VisaoProfessorControle.class.getSimpleName(), "inicializarAtaProva");
		return Uteis.getCaminhoRedirecionamentoNavegacao("ataProvaProfessor.xhtml");
	}

	public String novoVisaoCoordenador() throws Exception {
		registrarAtividadeUsuario(getUsuarioLogado(), "AtaProvaRelControle", "Novo Visão Coordenador", "Novo");
		setMensagemDetalhada("");
		montarListaSelectItemTurmaCoordenador();
		executarMetodoControle(VisaoCoordenadorControle.class.getSimpleName(), "inicializarAtaProva");
		return Uteis.getCaminhoRedirecionamentoNavegacao("ataProvaVisaoCoordenador.xhtml");
	}

	public void montarListaSelectItemTurmaCoordenador() {
		try {
			montarListaSelectItemTurmaCoordenador("");
		} catch (Exception e) {
		}
	}

	public void montarListaSelectItemTurmaCoordenador(String prm) throws Exception {
		List<TurmaVO> resultadoConsulta = null;
		try {
			resultadoConsulta = consultarTurmaCoordenador();
			setListaSelectItemTurma(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "identificadorTurma"));
			montarListaSelectItemTituloNota();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
		}
	}

	public List<TurmaVO> consultarTurmaCoordenador() throws Exception {
		return getFacadeFactory().getTurmaFacade().consultaRapidaPorCoordenadorAnoSemestre(getUsuarioLogado().getPessoa().getCodigo(), false, false, true, getLoginControle().getPermissaoAcessoMenuVO().getPermitirGerarAtaProvaRetroativo(), getAno(), getSemestre(), getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
	}

	public void montarListaDisciplinaTurmaVisaoCoordenador() {
		try {
			if (getTurmaVO().getCodigo() != 0) {
				getTurmaVO().setNivelMontarDados(NivelMontarDados.NAO_INICIALIZADO);
				getFacadeFactory().getTurmaFacade().carregarDados(getTurmaVO(), NivelMontarDados.BASICO, getUsuarioLogado());
				List<DisciplinaVO> resultado = consultarDisciplinaTurmaVisaoCoordenador();
				setListaSelectItemDisciplina(UtilSelectItem.getListaSelectItem(resultado, "codigo", "nome"));
				if (getTurmaVO().getSubturma()) {
					getTurmaVO().setCurso(getFacadeFactory().getCursoFacade().consultarCursoPorTurma(getTurmaVO().getTurmaPrincipal(), false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
				}
				montarListaSelectItemTituloNota();
			}
		} catch (Exception e) {
			setListaSelectItemDisciplina(null);
		}
	}

	@SuppressWarnings("unchecked")
	public List<DisciplinaVO> consultarDisciplinaTurmaVisaoCoordenador() throws Exception {
		List<DisciplinaVO> listaConsultas = getFacadeFactory().getDisciplinaFacade().consultarDisciplinaCoordenadorPorTurma(getUsuarioLogado().getPessoa().getCodigo(), getTurmaVO().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		return listaConsultas;
	}

	public void montarListaSelectItemDisciplinasCoordenador() throws Exception {
		setListaSelectItemDisciplina(UtilSelectItem.getListaSelectItem(getFacadeFactory().getDisciplinaFacade().consultarDisciplinaCoordenadorPorTurma(getUsuarioLogado().getPessoa().getCodigo(), getTurmaVO().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()), "codigo", "nome"));
	}

	public void imprimirPDFVisaoCoordenador() throws Exception {
		try {
			getFacadeFactory().getAtaProvaRelFacade().validarDadosVisaoCoordenador(getTurmaVO(), getDisciplinaVO().getCodigo(), getAno(), getSemestre(), getLoginControle().getPermissaoAcessoMenuVO().getPermitirGerarAtaProvaRetroativo());
			List<AtaProvaRelVO> listaObjetos = getFacadeFactory().getAtaProvaRelFacade().executarConsultaParametrizada(getTurmaVO(), getTurmaVO().getCurso(), getDisciplinaVO().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), getAno(), getSemestre(), getTrazerAlunoPendenteFinanceiramente(), getAprovados(), getReprovados(), getReprovadosPorFalta(), getCursando(), getSituacaoRecuperacaoNota(), getConfiguracaoGeralPadraoSistema().getPermitirProfessorRealizarLancamentoAlunosPreMatriculados(), getUsuarioLogado());
			if (listaObjetos.isEmpty()) {
				throw new Exception("Não foi encontrado nenhum resultado com os parâmetros passados.");
			}
			getSuperParametroRelVO().setNomeDesignIreport(AtaProvaRel.getDesignIReportRelatorio(getLayout()));
			getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
			getSuperParametroRelVO().setSubReport_Dir(getCaminhoBaseRelatorio());
			getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
			getSuperParametroRelVO().setTituloRelatorio("Ata de Prova");
			getSuperParametroRelVO().setListaObjetos(listaObjetos);
			getSuperParametroRelVO().setCaminhoBaseRelatorio(getCaminhoBaseRelatorio());
			getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
			getSuperParametroRelVO().setQuantidade(listaObjetos.size());
			getSuperParametroRelVO().adicionarParametro("tituloNota", getTituloNota());
			if(getIsApresentarAno()){
				getSuperParametroRelVO().setAno(getAno());
			}else{
				getSuperParametroRelVO().setAno("");
			}
			if(getIsApresentarAno()){
				getSuperParametroRelVO().setSemestre(getSemestre());
			}else{
				getSuperParametroRelVO().setSemestre("");
			}
			if (getSituacaoRecuperacaoNota() != null && !getSituacaoRecuperacaoNota().equals(SituacaoRecuperacaoNotaEnum.TODAS)) {
				getSuperParametroRelVO().adicionarParametro("situacaoRecuperacao", getSituacaoRecuperacaoNota().getValorApresentar());
			}else{
				getSuperParametroRelVO().adicionarParametro("situacaoRecuperacao", "");
			}			
			if (!getTurmaVO().getUnidadeEnsino().getCodigo().equals(0)) {
				setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getTurmaVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
				getSuperParametroRelVO().adicionarLogoUnidadeEnsinoSelecionada(getUnidadeEnsino());
			}
			realizarImpressaoRelatorio();
			removerObjetoMemoria(this);
			novoVisaoCoordenador();
			setMensagemID("msg_relatorio_ok");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void imprimirPDFFuncionario() {
		try {
			imprimirPDF(false);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void imprimirPDFVisaoProfessor() {
		try {
			imprimirPDF(true);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void imprimirPDF(boolean filtroVisaoProfessor) throws Exception {
		try {
			boolean permitirRealizarLancamentoAlunosPreMatriculados = getConfiguracaoGeralPadraoSistema().getPermitirProfessorRealizarLancamentoAlunosPreMatriculados();
			getFacadeFactory().getAtaProvaRelFacade().validarDados(getTurmaVO().getCodigo(), getDisciplinaVO().getCodigo(), true, getAno(), getLoginControle().getPermissaoAcessoMenuVO().getPermitirGerarAtaProvaRetroativo());
			setTurmaVO(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(getTurmaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			if (filtroVisaoProfessor) {
				if (!getLoginControle().getPermissaoAcessoMenuVO().getPermitirGerarAtaProvaRetroativo()) {
					if (getTurmaVO().getAnual() || getTurmaVO().getSemestral()) {
						setAno(Uteis.getAnoDataAtual4Digitos());
					}
					if (getTurmaVO().getSemestral()) {
						setSemestre(Uteis.getSemestreAtual());
					}
				}
				getFiltroRelatorioAcademicoVO().setPendenteFinanceiro(getTrazerAlunoPendenteFinanceiramente());
			}
			if (getProfessorTitularDisciplinaTurmaVO().getProfessor().getCodigo().equals(0)) {
				setProfessorTitularDisciplinaTurmaVO(getFacadeFactory().getDiarioRelFacade().consultarProfessorTitularTurma(getTurmaVO(), getDisciplinaVO().getCodigo(), getAno(), getSemestre(), false, getUsuarioLogado()));
			}
			if (getProfessorTitularDisciplinaTurmaVO().getProfessor().getCodigo().equals(0)) {
				setMostrarModalConsultarProfessorTitularDisciplinaTurma("RichFaces.$('panelProfessorTitular').show()");
				throw new Exception("Nenhum Professor Titular localizado! Favor selecioná-lo manualmente.");
			}
			setMostrarModalConsultarProfessorTitularDisciplinaTurma("");
			List<AtaProvaRelVO> listaObjetos = getFacadeFactory().getAtaProvaRelFacade().executarConsultaParametrizadaVisaoFuncionario(getTurmaVO(), getTurmaVO().getCurso(), getDisciplinaVO().getCodigo(), getProfessorTitularDisciplinaTurmaVO().getProfessor().getCodigo(), getUnidadeEnsino().getCodigo(), getAno(), getSemestre(), getFiltroRelatorioAcademicoVO(), getAprovados(), getReprovados(), getReprovadosPorFalta(), getCursando(), getTrazerAlunoTransferencia(), getSituacaoRecuperacaoNota(), getUsuarioLogado(), permitirRealizarLancamentoAlunosPreMatriculados);
			if (listaObjetos.isEmpty()) {
				throw new Exception("Não foi encontrado nenhum resultado com os parâmetros passados.");
			}
			getSuperParametroRelVO().setNomeDesignIreport(AtaProvaRel.getDesignIReportRelatorio(getLayout()));
			getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
			getSuperParametroRelVO().setSubReport_Dir(getCaminhoBaseRelatorio());
			getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
			getSuperParametroRelVO().setTituloRelatorio("Ata de Prova");
			getSuperParametroRelVO().setListaObjetos(listaObjetos);
			getSuperParametroRelVO().setCaminhoBaseRelatorio(getCaminhoBaseRelatorio());
			getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
			getSuperParametroRelVO().setQuantidade(listaObjetos.size());
			getSuperParametroRelVO().adicionarParametro("tituloNota", getTituloNota());
			if(getIsApresentarAno()){
				getSuperParametroRelVO().setAno(getAno());
			}else{
				getSuperParametroRelVO().setAno("");
			}
			if(getIsApresentarAno()){
				getSuperParametroRelVO().setSemestre(getSemestre());
			}else{
				getSuperParametroRelVO().setSemestre("");
			}
			if (getSituacaoRecuperacaoNota() != null && !getSituacaoRecuperacaoNota().equals(SituacaoRecuperacaoNotaEnum.TODAS)) {
				getSuperParametroRelVO().adicionarParametro("situacaoRecuperacao", getSituacaoRecuperacaoNota().getValorApresentar());
			}else{
				getSuperParametroRelVO().adicionarParametro("situacaoRecuperacao", "");
			}
			if (!getTurmaVO().getUnidadeEnsino().getCodigo().equals(0)) {
				setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getTurmaVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
				getSuperParametroRelVO().adicionarLogoUnidadeEnsinoSelecionada(getUnidadeEnsino());
			}
			adicionarFiltroSituacaoAcademica(getSuperParametroRelVO());
			realizarImpressaoRelatorio();
			removerObjetoMemoria(this);
			inicializarDados();
			novoVisaoProfessor();
			setMensagemID("msg_relatorio_ok");
		} catch (Exception e) {
			setProfessorTitularDisciplinaTurmaVO(null);
			throw e;
		}
	}

	public void adicionarFiltroSituacaoAcademica(SuperParametroRelVO superParametroRelVO) {
		superParametroRelVO.adicionarParametro("filtroAcademicoAtivo", getFiltroRelatorioAcademicoVO().getAtivo());
		superParametroRelVO.adicionarParametro("filtroAcademicoTrancado", getFiltroRelatorioAcademicoVO().getTrancado());
		superParametroRelVO.adicionarParametro("filtroAcademicoCancelado", getFiltroRelatorioAcademicoVO().getCancelado());
		superParametroRelVO.adicionarParametro("filtroAcademicoPreMatricula", getFiltroRelatorioAcademicoVO().getPreMatricula());
		superParametroRelVO.adicionarParametro("filtroAcademicoPreMatriculaCancelada", getFiltroRelatorioAcademicoVO().getPreMatriculaCancelada());
		superParametroRelVO.adicionarParametro("filtroAcademicoConcluido", getFiltroRelatorioAcademicoVO().getConcluido());
		superParametroRelVO.adicionarParametro("filtroAcademicoPendenteFinanceiro", getFiltroRelatorioAcademicoVO().getPendenteFinanceiro());
		try{
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getLayout(), AtaProvaRel.getIdEntidade(), "layout", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getAprovados().toString(), AtaProvaRel.getIdEntidade(), "aprovados", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getCursando().toString(), AtaProvaRel.getIdEntidade(), "cursando", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getReprovados().toString(), AtaProvaRel.getIdEntidade(), "reprovados", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getReprovadosPorFalta().toString(), AtaProvaRel.getIdEntidade(), "reprovadosPorFalta", getUsuarioLogado());
			if(getUsuarioLogado().getIsApresentarVisaoAdministrativa()){
				getFacadeFactory().getLayoutPadraoFacade().persistirFiltroSituacaoAcademica(getFiltroRelatorioAcademicoVO(), AtaProvaRel.getIdEntidade(), getUsuarioLogado());
			}
		}catch(Exception e){
			
		}
	}

	public String getCaminhoBaseRelatorio() {
		return "relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator;
	}

	@SuppressWarnings("unchecked")
	public void consultarDisciplina() {
		try {
			List<DisciplinaVO> objs = new ArrayList<DisciplinaVO>(0);
			if (getTurmaVO().getCodigo().equals(0)) {
				getFacadeFactory().getAtaProvaRelFacade().validarDados(getTurmaVO().getCodigo(), 0, false, null, false);
			}
			if (getTurmaVO().getTurmaAgrupada()) {
				if (getCampoConsultaDisciplina().equals("codigo")) {
					if (getValorConsultaDisciplina().equals("")) {
						setValorConsultaDisciplina("0");
					}
					int valorInt = Integer.parseInt(getValorConsultaDisciplina());
					objs = getFacadeFactory().getDisciplinaFacade().consultarPorCodigoDisciplinaUnidadeEnsinoCodigoTurmaAgrupada(valorInt, getUnidadeEnsino().getCodigo(), getTurmaVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
				}
				if (getCampoConsultaDisciplina().equals("nome")) {
					objs = getFacadeFactory().getDisciplinaFacade().consultarPorNomeDisciplinaUnidadeEnsinoCodigoTurmaAgrupada(getValorConsultaDisciplina(), getUnidadeEnsino().getCodigo(), getTurmaVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
				}
			} else if (getTurmaVO().getSubturma()) {
				if (getCampoConsultaDisciplina().equals("codigo")) {
					if (getValorConsultaDisciplina().equals("")) {
						setValorConsultaDisciplina("0");
					}
					int valorInt = Integer.parseInt(getValorConsultaDisciplina());
					objs = getFacadeFactory().getDisciplinaFacade().consultarPorCodigoDisciplinaUnidadeEnsinoCodigoCursoCodigoTurma(valorInt, getUnidadeEnsino().getCodigo(), getTurmaVO().getCurso().getCodigo(), getTurmaVO().getTurmaPrincipal(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
				}
				if (getCampoConsultaDisciplina().equals("nome")) {
					objs = getFacadeFactory().getDisciplinaFacade().consultarPorNomeDisciplinaUnidadeEnsinoCodigoCursoCodigoTurma(getValorConsultaDisciplina(), getUnidadeEnsino().getCodigo(), getTurmaVO().getCurso().getCodigo(), getTurmaVO().getTurmaPrincipal(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
				}
			} else {
				if (getCampoConsultaDisciplina().equals("codigo")) {
					if (getValorConsultaDisciplina().equals("")) {
						setValorConsultaDisciplina("0");
					}
					int valorInt = Integer.parseInt(getValorConsultaDisciplina());
					objs = getFacadeFactory().getDisciplinaFacade().consultarPorCodigoDisciplinaUnidadeEnsinoCodigoCursoCodigoTurma(valorInt, getUnidadeEnsino().getCodigo(), getTurmaVO().getCurso().getCodigo(), getTurmaVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
				}
				if (getCampoConsultaDisciplina().equals("nome")) {
					objs = getFacadeFactory().getDisciplinaFacade().consultarPorNomeDisciplinaUnidadeEnsinoCodigoCursoCodigoTurma(getValorConsultaDisciplina(), getUnidadeEnsino().getCodigo(), getTurmaVO().getCurso().getCodigo(), getTurmaVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
				}
			}
			setListaConsultaDisciplina(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaDisciplina(new ArrayList<DisciplinaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}

	public void selecionarDisciplina() throws Exception {
		try {
			DisciplinaVO obj = (DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplinaItens");
			setDisciplinaVO(obj);
		} catch (Exception e) {
			throw e;
		}
	}

	public void limparDisciplina() throws Exception {
		try {
			setDisciplinaVO(null);
		} catch (Exception e) {
		}
	}

	public void consultarTurma() {
		try {
			List<TurmaVO> objs = new ArrayList<TurmaVO>(0);
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				objs = getFacadeFactory().getTurmaFacade().consultarPorIdentificadorTurma(getValorConsultaTurma(), getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaTurma(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaTurma(new ArrayList<TurmaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarTurma() throws Exception {
		TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
		removerObjetoMemoria(getDisciplinaVO());
		setTurmaVO(obj);
		if (getTurmaVO().getSubturma()) {
			getTurmaVO().setCurso(getFacadeFactory().getCursoFacade().consultarCursoPorTurma(getTurmaVO().getTurmaPrincipal(), false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
		} else if (getTurmaVO().getTurmaAgrupada()) {
			getTurmaVO().setCurso(new CursoVO());
		}
		obj = null;
		setValorConsultaTurma(null);
		setCampoConsultaTurma(null);
		Uteis.liberarListaMemoria(getListaConsultaTurma());
		getUnidadeEnsino().setCodigo(getTurmaVO().getUnidadeEnsino().getCodigo());
		montarListaSelectItemDisciplinaTurma();
	}

	private List<SelectItem> tipoConsultaComboTurma;

	public List<SelectItem> getTipoConsultaComboTurma() {
		if (tipoConsultaComboTurma == null) {
			tipoConsultaComboTurma = new ArrayList<SelectItem>(0);
			tipoConsultaComboTurma.add(new SelectItem("identificadorTurma", "Identificador"));
		}
		return tipoConsultaComboTurma;
	}

	private List<SelectItem> tipoConsultaComboDisciplina;

	public List<SelectItem> getTipoConsultaComboDisciplina() {
		if (tipoConsultaComboDisciplina == null) {
			tipoConsultaComboDisciplina = new ArrayList<SelectItem>(0);
			tipoConsultaComboDisciplina.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboDisciplina.add(new SelectItem("codigo", "Código"));
		}
		return tipoConsultaComboDisciplina;
	}

	public void limparTurma() throws Exception {
		try {
			removerObjetoMemoria(getTurmaVO());
			removerObjetoMemoria(getDisciplinaVO());
			Uteis.liberarListaMemoria(getListaConsultaTurma());
		} catch (Exception e) {
		}
	}

	List<SelectItem> tipoConsultaComboCurso;

	public List<SelectItem> getTipoConsultaComboCurso() {
		if (tipoConsultaComboCurso == null) {
			tipoConsultaComboCurso = new ArrayList<SelectItem>(0);
			tipoConsultaComboCurso.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboCurso.add(new SelectItem("codigo", "Código"));
		}
		return tipoConsultaComboCurso;
	}

	@SuppressWarnings("unchecked")
	public List<CursoVO> consultarCursoPorUnidadeEnsino() throws Exception {
		List<CursoVO> listaConsulta = getFacadeFactory().getCursoFacade().consultarPorNomeUnidadeEnsino("", getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
		return listaConsulta;
	}

	public void montarListaSelectItemDisciplinaTurma() {
		try {
			getListaSelectItemDisciplina().clear();
			if (!getTurmaVO().getIdentificadorTurma().equals("")) {
				setTurmaVO(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(getTurmaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
				List<HorarioTurmaDisciplinaProgramadaVO> horarioTurmaDisciplinaProgramadaVOs = getFacadeFactory().getHorarioTurmaFacade().consultarHorarioTurmaDisciplinaProgramadaPorTurma(getTurmaVO().getCodigo(), false, true, 0);
				getListaSelectItemDisciplina().add(new SelectItem(0, ""));
				for (HorarioTurmaDisciplinaProgramadaVO obj : horarioTurmaDisciplinaProgramadaVOs) {
					getListaSelectItemDisciplina().add(new SelectItem(obj.getCodigoDisciplina(), obj.getNomeDisciplina() + " - CH: " + obj.getChDisciplina()));
				}
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_entre_prmconsulta");
		}
	}

	public List<DisciplinaVO> consultarDisciplinaTurma() throws Exception {
		List<DisciplinaVO> listaConsultas = getFacadeFactory().getDisciplinaFacade().consultarDisciplinaPorTurmaHorarioTurmaProfessorDisciplina(getTurmaVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
		return listaConsultas;
	}

	public void montarListaSelectItemTurmaProfessor() {
		List<TurmaVO> listaResultado = null;
		Iterator<TurmaVO> i = null;
		String value = "";
		try {
			listaResultado = consultarTurmaPorProfessor();
			getListaSelectItemTurma().clear();
			getListaSelectItemTurma().add(new SelectItem(0, ""));
			i = listaResultado.iterator();

			while (i.hasNext()) {
				TurmaVO turma = (TurmaVO) i.next();

				if (turma.getTurmaAgrupada()) {
					value = turma.getIdentificadorTurma() + " - Turno " + turma.getTurno().getNome();
				} else if (turma.getSubturma()) {
					turma.setCurso(getFacadeFactory().getCursoFacade().consultarCursoPorTurma(turma.getTurmaPrincipal(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
					value = turma.getIdentificadorTurma() + " - Curso " + turma.getCurso().getNome() + " - Turno " + turma.getTurno().getNome();
				} else {
					value = turma.getIdentificadorTurma() + " - Curso " + turma.getCurso().getNome() + " - Turno " + turma.getTurno().getNome();
				}
				getListaSelectItemTurma().add(new SelectItem(turma.getCodigo(), value));
				removerObjetoMemoria(turma);
			}
		} catch (Exception e) {
			getListaSelectItemTurma().clear();
		} finally {
			Uteis.liberarListaMemoria(listaResultado);
			i = null;
			value = null;
		}
	}

	public List<TurmaVO> consultarTurmaPorProfessor() throws Exception {	
//		if(!getUsuarioLogado().getIsApresentarVisaoAdministrativa() && !getLoginControle().getPermissaoAcessoMenuVO().getPermitirRegistrarAulaRetroativo()) {
//			setSemestre(Uteis.getSemestreAtual());
//			setAno(Uteis.getData(new Date(), "yyyy"));
//		}
		if (getConfiguracaoGeralPadraoSistema().getPerfilPadraoProfessorGraduacao().getCodigo().intValue() == getUsuarioLogado().getPerfilAcesso().getCodigo()) {			
			return getFacadeFactory().getTurmaFacade().consultarTurmaPorProfessorAnoSemestreTurmaAnteriorCursoNivelDadosCombobox(getUsuarioLogado().getPessoa().getCodigo(), getSemestre(), getAno(),getLoginControle().getPermissaoAcessoMenuVO().getPermitirGerarAtaProvaRetroativo(),"AT",getUnidadeEnsinoLogado().getCodigo(),0,getUsuarioLogado().getVisaoLogar().equals("professor"),false,true,true, null,false, null);
		} else if (getConfiguracaoGeralPadraoSistema().getPerfilPadraoProfessorPosGraduacao().getCodigo().intValue() == getUsuarioLogado().getPerfilAcesso().getCodigo()) {

			return getFacadeFactory().getTurmaFacade().consultarTurmaPorProfessorAnoSemestreTurmaAnteriorCursoNivelDadosCombobox(getUsuarioLogado().getPessoa().getCodigo(),getSemestre(), getAno(),getLoginControle().getPermissaoAcessoMenuVO().getPermitirGerarAtaProvaRetroativo(),"AT",getUnidadeEnsinoLogado().getCodigo(),0,getUsuarioLogado().getVisaoLogar().equals("professor"),true,false,true, null,false, null);
		} else {
			return getFacadeFactory().getTurmaFacade().consultarTurmaPorProfessorAnoSemestreTurmaAnteriorCursoNivelDadosCombobox(getUsuarioLogado().getPessoa().getCodigo(),getSemestre(), getAno(),getLoginControle().getPermissaoAcessoMenuVO().getPermitirGerarAtaProvaRetroativo(),"AT",getUnidadeEnsinoLogado().getCodigo(),0,getUsuarioLogado().getVisaoLogar().equals("professor"),false,false,true, null,false, null);
		}
	}

	public void montarListaDisciplinaTurmaVisaoProfessor() {
		try {
			if(Uteis.isAtributoPreenchido(getTurmaVO())) {
				getFacadeFactory().getTurmaFacade().carregarDados(getTurmaVO(), NivelMontarDados.BASICO, getUsuarioLogado());
			}
			setDisciplinaVO(null);
			List<DisciplinaVO> resultado = consultarDisciplinaProfessorTurma();
			setListaSelectItemDisciplina(UtilSelectItem.getListaSelectItem(resultado, "codigo", "nome"));
			montarListaSelectItemTituloNota();
			setMensagemID("msg_entre_prmrelatorio");
		} catch (Exception e) {
			setListaSelectItemDisciplina(new ArrayList<SelectItem>(0));
		}
	}

	@SuppressWarnings("unchecked")
	public List<DisciplinaVO> consultarDisciplinaProfessorTurma() throws Exception {
//		List<DisciplinaVO> listaConsultas = getFacadeFactory().getDisciplinaFacade().consultarDisciplinaProfessorTurmaAgrupada(getUsuarioLogado().getPessoa().getCodigo(), getTurmaVO().getCodigo(),getSemestre(), getAno(),  false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(), true);
		List listaConsultas = new ArrayList(0);
		if(Uteis.isAtributoPreenchido(getTurmaVO())){
			if (getTurmaVO().getIntegral()) {
				listaConsultas = getFacadeFactory().getDisciplinaFacade().consultarDisciplinasDoProfessor(getUsuarioLogado().getPessoa().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), null, getTurmaVO().getCodigo(), null, null, Uteis.NIVELMONTARDADOS_DADOSBASICOS, true, false, getUsuarioLogado());
			} else {
				listaConsultas = getFacadeFactory().getDisciplinaFacade().consultarDisciplinasDoProfessor(getUsuarioLogado().getPessoa().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), null, getTurmaVO().getCodigo(), getSemestre(), getAno(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, true, false, getUsuarioLogado());				
			}
		}
		return listaConsultas;
	}

	public List<SelectItem> getTipoLayout() {
		List<SelectItem> tipoLayoutLista = new ArrayList<SelectItem>(0);
		tipoLayoutLista.add(new SelectItem("layout1", "Layout 1"));
		tipoLayoutLista.add(new SelectItem("layout2", "Layout 2"));
		tipoLayoutLista.add(new SelectItem("layout3", "Layout 3"));

		return tipoLayoutLista;
	}

	public List<SelectItem> getListaSelectItemDisciplina() {
		if (listaSelectItemDisciplina == null) {
			listaSelectItemDisciplina = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemDisciplina;
	}

	public void setListaSelectItemDisciplina(List<SelectItem> listaSelectItemDisciplina) {
		this.listaSelectItemDisciplina = listaSelectItemDisciplina;
	}

	/**
	 * @return the listaSelectItemUnidadeEnsino
	 */
	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemUnidadeEnsino;
	}

	/**
	 * @param listaSelectItemUnidadeEnsino
	 *            the listaSelectItemUnidadeEnsino to set
	 */
	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	/**
	 * @return the turmaVO
	 */
	public TurmaVO getTurmaVO() {
		if (turmaVO == null) {
			turmaVO = new TurmaVO();
		}
		return turmaVO;
	}

	/**
	 * @param turmaVO
	 *            the turmaVO to set
	 */
	public void setTurmaVO(TurmaVO turmaVO) {
		this.turmaVO = turmaVO;
	}

	/**
	 * @return the disciplinaVO
	 */
	public DisciplinaVO getDisciplinaVO() {
		if (disciplinaVO == null) {
			disciplinaVO = new DisciplinaVO();
		}
		return disciplinaVO;
	}

	/**
	 * @param disciplinaVO
	 *            the disciplinaVO to set
	 */
	public void setDisciplinaVO(DisciplinaVO disciplinaVO) {
		this.disciplinaVO = disciplinaVO;
	}

	/**
	 * @return the unidadeEnsino
	 */
	public UnidadeEnsinoVO getUnidadeEnsino() {
		if (unidadeEnsino == null) {
			unidadeEnsino = new UnidadeEnsinoVO();
		}
		return unidadeEnsino;
	}

	/**
	 * @param unidadeEnsino
	 *            the unidadeEnsino to set
	 */
	public void setUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}
	

	public boolean getIsApresentarDadosAposSelecionarTurma() {
		return getTurmaVO().getCodigo() != 0;
	}

	public boolean getIsApresentarDadosAposSelecionarTurmaPosGraduacao() {
		return (getTurmaVO().getCodigo() != 0 && !getTurmaVO().getCurso().getNivelEducacionalPosGraduacao());
	}

	public boolean getIsApresentarDadosAposSelecionarTurmaCursoSemestral() {
		return (getTurmaVO().getCodigo() != 0 && !getTurmaVO().getCurso().getNivelEducacionalPosGraduacao() && (getTurmaVO().getCurso().getPeriodicidade().equals("SE") || getTurmaVO().getSemestral()));
	}

	public boolean getIsApresentarDadosAposSelecionarTurmaCursoSemestralAnual() {
		return (getTurmaVO().getCodigo() != 0 && !getTurmaVO().getCurso().getNivelEducacionalPosGraduacao() && ((getTurmaVO().getCurso().getPeriodicidade().equals("SE") || getTurmaVO().getCurso().getPeriodicidade().equals("AN")) || (getTurmaVO().getSemestral() || getTurmaVO().getAnual())));
	}

	public String getCampoConsultaDisciplina() {
		if (campoConsultaDisciplina == null) {
			campoConsultaDisciplina = "";
		}
		return campoConsultaDisciplina;
	}

	public void setCampoConsultaDisciplina(String campoConsultaDisciplina) {
		this.campoConsultaDisciplina = campoConsultaDisciplina;
	}

	public String getValorConsultaDisciplina() {
		if (valorConsultaDisciplina == null) {
			valorConsultaDisciplina = "";
		}
		return valorConsultaDisciplina;
	}

	public void setValorConsultaDisciplina(String valorConsultaDisciplina) {
		this.valorConsultaDisciplina = valorConsultaDisciplina;
	}

	public List<DisciplinaVO> getListaConsultaDisciplina() {
		if (listaConsultaDisciplina == null) {
			listaConsultaDisciplina = new ArrayList<DisciplinaVO>(0);
		}
		return listaConsultaDisciplina;
	}

	public void setListaConsultaDisciplina(List<DisciplinaVO> listaConsultaDisciplina) {
		this.listaConsultaDisciplina = listaConsultaDisciplina;
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

	public String getLayout() {
		if (layout == null) {
			layout = "layout1";
		}
		return layout;
	}

	public void setLayout(String layout) {
		this.layout = layout;
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

	public Boolean getIsApresentarCampoAno() {
		if (getTurmaVO().getTurmaAgrupada())
			return getTurmaVO().getSemestral() || getTurmaVO().getAnual();
		return getTurmaVO().getCurso().getPeriodicidade().equals("AN") || getTurmaVO().getCurso().getPeriodicidade().equals("SE");
	}

	public Boolean getIsApresentarCampoSemestre() {
		if (getTurmaVO().getTurmaAgrupada())
			return getTurmaVO().getSemestral();
		return getTurmaVO().getCurso().getPeriodicidade().equals("SE");
	}

	public void consultarProfessorTitular() {
		try {
			List<FuncionarioVO> objs = new ArrayList<FuncionarioVO>(0);
			if (getTurmaVO().getCodigo().equals(0) || getDisciplinaVO().getCodigo().equals(0)) {
				getFacadeFactory().getAtaProvaRelFacade().validarDados(getTurmaVO().getCodigo(), getDisciplinaVO().getCodigo(), false, null, false);
			}
			if (getCampoConsultaProfessorTitular().equals("matricula")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorMatricula(getValorConsultaProfessorTitular(), "PR", getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaProfessorTitular().equals("nome")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorNome(getValorConsultaProfessorTitular(), "PR", true, getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaProfessorTitular(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaProfessorTitular(new ArrayList<FuncionarioVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarProfessorTitular() throws Exception {
		try {
			FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("professorTitularItens");
			getProfessorTitularDisciplinaTurmaVO().setProfessor(obj);
			imprimirPDF(false);
			getListaConsultaProfessorTitular().clear();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> getTipoConsultaComboProfessorTitular() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		return itens;
	}

	public void limparProfessorTitular() throws Exception {
		setProfessorTitularDisciplinaTurmaVO(null);
	}

	public String getCampoConsultaProfessorTitular() {
		if (campoConsultaProfessorTitular == null) {
			campoConsultaProfessorTitular = "";
		}
		return campoConsultaProfessorTitular;
	}

	public void setCampoConsultaProfessorTitular(String campoConsultaProfessorTitular) {
		this.campoConsultaProfessorTitular = campoConsultaProfessorTitular;
	}

	public String getValorConsultaProfessorTitular() {
		if (valorConsultaProfessorTitular == null) {
			valorConsultaProfessorTitular = "";
		}
		return valorConsultaProfessorTitular;
	}

	public void setValorConsultaProfessorTitular(String valorConsultaProfessorTitular) {
		this.valorConsultaProfessorTitular = valorConsultaProfessorTitular;
	}

	public List<FuncionarioVO> getListaConsultaProfessorTitular() {
		if (listaConsultaProfessorTitular == null) {
			listaConsultaProfessorTitular = new ArrayList<FuncionarioVO>();
		}
		return listaConsultaProfessorTitular;
	}

	public void setListaConsultaProfessorTitular(List<FuncionarioVO> listaConsultaProfessorTitular) {
		this.listaConsultaProfessorTitular = listaConsultaProfessorTitular;
	}

	public String getMostrarModalConsultarProfessorTitularDisciplinaTurma() {
		if (mostrarModalConsultarProfessorTitularDisciplinaTurma == null) {
			mostrarModalConsultarProfessorTitularDisciplinaTurma = "";
		}
		return mostrarModalConsultarProfessorTitularDisciplinaTurma;
	}

	public void setMostrarModalConsultarProfessorTitularDisciplinaTurma(String mostrarModalConsultarProfessorTitularDisciplinaTurma) {
		this.mostrarModalConsultarProfessorTitularDisciplinaTurma = mostrarModalConsultarProfessorTitularDisciplinaTurma;
	}

	public ProfessorTitularDisciplinaTurmaVO getProfessorTitularDisciplinaTurmaVO() {
		if (professorTitularDisciplinaTurmaVO == null) {
			professorTitularDisciplinaTurmaVO = new ProfessorTitularDisciplinaTurmaVO();
		}
		return professorTitularDisciplinaTurmaVO;
	}

	public void setProfessorTitularDisciplinaTurmaVO(ProfessorTitularDisciplinaTurmaVO professorTitularDisciplinaTurmaVO) {
		this.professorTitularDisciplinaTurmaVO = professorTitularDisciplinaTurmaVO;
	}

	

	public List<SelectItem> getListaSelectSemestre() {
		List<SelectItem> lista = new ArrayList<SelectItem>(0);
		lista.add(new SelectItem("", ""));
		lista.add(new SelectItem("1", "1º"));
		lista.add(new SelectItem("2", "2º"));
		return lista;
	}

	public List<SelectItem> getListaSelectItemTurma() {
		if (listaSelectItemTurma == null) {
			listaSelectItemTurma = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemTurma;
	}

	public void setListaSelectItemTurma(List<SelectItem> listaSelectItemTurma) {
		this.listaSelectItemTurma = listaSelectItemTurma;
	}

	public boolean getTrazerAlunoPendenteFinanceiramente() throws Exception {
		return getFacadeFactory().getConfiguracaoGeralSistemaFacade().executarVerificacaoApresentarAlunoPendenteFinanceiramente(getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado());
	}

	public Boolean getAprovados() {
		if (aprovados == null) {
			aprovados = false;
		}
		return aprovados;
	}

	public void setAprovados(Boolean aprovados) {
		this.aprovados = aprovados;
	}

	public Boolean getReprovados() {
		if (reprovados == null) {
			reprovados = false;
		}
		return reprovados;
	}

	public void setReprovados(Boolean reprovados) {
		this.reprovados = reprovados;
	}

	public Boolean getReprovadosPorFalta() {
		if (reprovadosPorFalta == null) {
			reprovadosPorFalta = false;
		}
		return reprovadosPorFalta;
	}

	public void setReprovadosPorFalta(Boolean reprovadosPorFalta) {
		this.reprovadosPorFalta = reprovadosPorFalta;
	}

	public Boolean getCursando() {
		if (cursando == null) {
			cursando = false;
		}
		return cursando;
	}

	public void setCursando(Boolean cursando) {
		this.cursando = cursando;
	}

	public Boolean getTrazerAlunoTransferencia() {
		if (trazerAlunoTransferencia == null) {
			trazerAlunoTransferencia = Boolean.FALSE;
		}
		return trazerAlunoTransferencia;
	}

	public void setTrazerAlunoTransferencia(Boolean trazerAlunoTransferencia) {
		this.trazerAlunoTransferencia = trazerAlunoTransferencia;
	}
	
	public void realizarSelecaoCheckboxMarcarDesmarcarTodosSituacaoHistoricoAtaProva() {
		if (getMarcarTodasSituacoesHistorico()) {
			setAprovados(true);
			setCursando(true);
			setReprovados(true);
			setReprovadosPorFalta(true);
		} else {
			setAprovados(false);
			setCursando(false);
			setReprovados(false);
			setReprovadosPorFalta(false);
		}
	}
	
	public Boolean getMarcarTodasSituacoesHistorico() {
		if (marcarTodasSituacoesHistorico == null) {
			marcarTodasSituacoesHistorico = false;
		}
		return marcarTodasSituacoesHistorico;
	}

	public void setMarcarTodasSituacoesHistorico(Boolean marcarTodasSituacoesHistorico) {
		this.marcarTodasSituacoesHistorico = marcarTodasSituacoesHistorico;
	}
	
	public String getIsApresentarTextoCheckBoxMarcarDesmarcarTodosHistorico() {
		if (getMarcarTodasSituacoesHistorico()) {
			return UteisJSF.internacionalizar("prt_Inadimplencia_desmarcarTodos");
		}
		return UteisJSF.internacionalizar("prt_Inadimplencia_marcarTodos");
	}
	
	public String getAno() {
		if(ano == null){
			if(getLoginControle().getPermissaoAcessoMenuVO().getPermitirGerarAtaProvaRetroativo() && getUsuarioLogado().getIsApresentarVisaoProfessor()) {
				ano = getVisaoProfessorControle().getAno();
			}else if(getLoginControle().getPermissaoAcessoMenuVO().getPermitirGerarAtaProvaRetroativo() && getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
				ano = getVisaoCoordenadorControle().getAno();
			}else {
				ano = Uteis.getAnoDataAtual4Digitos();
			}
		}
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getSemestre() {
		if(semestre == null){
			if(getLoginControle().getPermissaoAcessoMenuVO().getPermitirGerarAtaProvaRetroativo() && getUsuarioLogado().getIsApresentarVisaoProfessor()) {
				semestre = getVisaoProfessorControle().getSemestre();
			}else if(getLoginControle().getPermissaoAcessoMenuVO().getPermitirGerarAtaProvaRetroativo() && getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
				semestre = getVisaoCoordenadorControle().getSemestre();
			}else {
				semestre = Uteis.getSemestreAtual();
			}
		}
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	public List<SelectItem> getListaSelectItemSemestre() {
		if(listaSelectItemSemestre == null){
			listaSelectItemSemestre = new ArrayList<SelectItem>(0);
			listaSelectItemSemestre.add(new SelectItem("1", "1º"));
			listaSelectItemSemestre.add(new SelectItem("2", "2º"));
		}
		return listaSelectItemSemestre;
	}

	public void setListaSelectItemSemestre(List<SelectItem> listaSelectItemSemestre) {
		this.listaSelectItemSemestre = listaSelectItemSemestre;
	}
	
	public Boolean getIsApresentarAno(){
		return Uteis.isAtributoPreenchido(getTurmaVO()) && !getTurmaVO().getIntegral() && 
				((!getUsuarioLogado().getIsApresentarVisaoAdministrativa() && getLoginControle().getPermissaoAcessoMenuVO().getPermitirRegistrarAulaRetroativo())
						|| getUsuarioLogado().getIsApresentarVisaoAdministrativa());		
	}
	
	public Boolean getIsApresentarSemestre(){
		return Uteis.isAtributoPreenchido(getTurmaVO()) && getTurmaVO().getSemestral() && ((!getUsuarioLogado().getIsApresentarVisaoAdministrativa() && getLoginControle().getPermissaoAcessoMenuVO().getPermitirRegistrarAulaRetroativo())
				|| getUsuarioLogado().getIsApresentarVisaoAdministrativa());		
	}
	
	
	public SituacaoRecuperacaoNotaEnum getSituacaoRecuperacaoNota() {
		return situacaoRecuperacaoNota;
	}

	public void setSituacaoRecuperacaoNota(SituacaoRecuperacaoNotaEnum situacaoRecuperacaoNota) {
		this.situacaoRecuperacaoNota = situacaoRecuperacaoNota;
	}
	
	public List<SelectItem> getListaSelectItemSituacaoRecuperacao() {
		if (listaSelectItemSituacaoRecuperacao == null) {
			listaSelectItemSituacaoRecuperacao = new ArrayList<SelectItem>(0);
			listaSelectItemSituacaoRecuperacao.add(new SelectItem(null, ""));
			listaSelectItemSituacaoRecuperacao.add(new SelectItem(SituacaoRecuperacaoNotaEnum.TODAS, SituacaoRecuperacaoNotaEnum.TODAS.getValorApresentar()));
			listaSelectItemSituacaoRecuperacao.add(new SelectItem(SituacaoRecuperacaoNotaEnum.EM_RECUPERACAO, SituacaoRecuperacaoNotaEnum.EM_RECUPERACAO.getValorApresentar()));
			listaSelectItemSituacaoRecuperacao.add(new SelectItem(SituacaoRecuperacaoNotaEnum.NOTA_RECUPERADA, SituacaoRecuperacaoNotaEnum.NOTA_RECUPERADA.getValorApresentar()));
			listaSelectItemSituacaoRecuperacao.add(new SelectItem(SituacaoRecuperacaoNotaEnum.NOTA_NAO_RECUPERADA, SituacaoRecuperacaoNotaEnum.NOTA_NAO_RECUPERADA.getValorApresentar()));
			listaSelectItemSituacaoRecuperacao.add(new SelectItem(SituacaoRecuperacaoNotaEnum.SEM_RECUPERACAO, SituacaoRecuperacaoNotaEnum.SEM_RECUPERACAO.getValorApresentar()));
		}
		return listaSelectItemSituacaoRecuperacao;
	}
	
	public void setListaSelectItemSituacaoRecuperacao(List<SelectItem> listaSelectItemSituacaoRecuperacao) {
		this.listaSelectItemSituacaoRecuperacao = listaSelectItemSituacaoRecuperacao;
	}

	public String getTituloNota() {
		if(tituloNota == null){
			tituloNota = "";
		}
		return tituloNota;
	}

	public void setTituloNota(String tituloNota) {
		this.tituloNota = tituloNota;
	}
	
	public void montarListaSelectItemTituloNota(){
		getListaSelectItemTituloNota().clear();
		if(getIsApresentarCampoNota()){
			try {
				getListaSelectItemTituloNota().add(new SelectItem("Nota", ""));
				List<ConfiguracaoAcademicoVO> configuracaoAcademicoVOs = getFacadeFactory().getConfiguracaoAcademicoFacade().consultaRapidaPorUnidadeEnsinoCursoDisciplinaTurmaAnoSemestreSituacaoMatSituacaoHistFiltroVisaoProfessorRegistroNota(0, 0, getDisciplinaVO().getCodigo(), getTurmaVO(), getAno(), getSemestre(), false, "", true, false, false, null, false, false, true, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
				for(ConfiguracaoAcademicoVO configuracaoAcademicoVO: configuracaoAcademicoVOs){
					for(int x = 1;x<=40;x++){
						if((Boolean)UtilReflexao.invocarMetodoGet(configuracaoAcademicoVO, "utilizarNota"+x) 
						&& (Boolean)UtilReflexao.invocarMetodoGet(configuracaoAcademicoVO, "apresentarNota"+x)){
							SelectItem selectItem = new SelectItem((String)UtilReflexao.invocarMetodoGet(configuracaoAcademicoVO, "tituloNotaApresentar"+x), (String)UtilReflexao.invocarMetodoGet(configuracaoAcademicoVO, "tituloNotaApresentar"+x));
							if(!getListaSelectItemTituloNota().contains(selectItem)){
								getListaSelectItemTituloNota().add(selectItem);
							}
						}
					}
				}
				limparMensagem();
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public List<SelectItem> getListaSelectItemTituloNota() {
		if(listaSelectItemTituloNota == null){
			listaSelectItemTituloNota = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemTituloNota;
	}

	public void setListaSelectItemTituloNota(List<SelectItem> listaSelectItemTituloNota) {
		this.listaSelectItemTituloNota = listaSelectItemTituloNota;
	}

	public Boolean getIsApresentarCampoNota(){
		return (getLayout().equals("layout3") || getLayout().equals("layout1")) && Uteis.isAtributoPreenchido(getTurmaVO()) && Uteis.isAtributoPreenchido(getDisciplinaVO()) 
				&& ((getIsApresentarCampoAno() && getAno().trim().length() == 4)
						|| (getIsApresentarCampoSemestre() && getAno().trim().length() == 4 && (getSemestre().equals("1") || getSemestre().equals("2")))
						|| (!getIsApresentarAno() && !getIsApresentarCampoSemestre()));				
	}
	
	public boolean getIsApresentarAnoVisaoProfessorCoordenador() {
		if (getUsuarioLogado().getVisaoLogar().equals("professor") || getUsuarioLogado().getVisaoLogar().equals("coordenador")) {
			if (getLoginControle().getPermissaoAcessoMenuVO().getPermitirGerarAtaProvaRetroativo()) {
				if (!getTurmaVO().getCodigo().equals(0)) {
					if (getTurmaVO().getSemestral()) {
						return true;
					} else if (getTurmaVO().getAnual()) {
						return true;
					} else {
						setAno("");
						return false;
					}
				}
				return true;
			} else {
				if (!getTurmaVO().getCodigo().equals(0)) {
					if (!(getTurmaVO().getSemestral() || getTurmaVO().getAnual())) {
						setAno("");
					}
				}
				return false;
			}
		}
		return true;
	}
	
	public boolean getIsApresentarSemestreVisaoProfessorCoordenador() {
		if (getUsuarioLogado().getVisaoLogar().equals("professor") || getUsuarioLogado().getVisaoLogar().equals("coordenador")) {
			if (getLoginControle().getPermissaoAcessoMenuVO().getPermitirGerarAtaProvaRetroativo()) {
				if (!getTurmaVO().getCodigo().equals(0)) {
					if (getTurmaVO().getSemestral()) {
						return true;
					} else {
						setSemestre("");
						return false;
					}
				}
				return true;
			} else {
				if (!getTurmaVO().getCodigo().equals(0)) {
					if (!(getTurmaVO().getSemestral())) {
						setSemestre("");
					}
				}
				return false;
			}
		}
		return true;
	}
}
