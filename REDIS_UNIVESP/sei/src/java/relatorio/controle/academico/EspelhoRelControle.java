package relatorio.controle.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.HorarioTurmaDisciplinaProgramadaVO;
import negocio.comuns.academico.HorarioTurmaVO;
import negocio.comuns.academico.LayoutPadraoVO;
import negocio.comuns.academico.ProfessorTitularDisciplinaTurmaVO;
import negocio.comuns.academico.RegistroAulaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.MesAnoEnum;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.basico.enumeradores.ModuloLayoutEtiquetaEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ProcessarParalelismo;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.academico.DiarioRegistroAulaVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.academico.EspelhoRel;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;
import controle.arquitetura.SelectItemOrdemValor;

@Controller("EspelhoRelControle")
@Scope("viewScope")
@Lazy
public class EspelhoRelControle extends SuperControleRelatorio {

	private static final long serialVersionUID = 1L;
	protected List<SelectItem> listaSelectItemDisciplina;
	protected List<SelectItem> listaSelectItemProfessor;
	protected List<PessoaVO> listaProfessor;
	protected List<TurmaVO> listaConsultaTurma;
	protected Boolean existeUnidadeEnsino;
	protected String valorConsultaTurma;
	protected String campoConsultaTurma;
	protected List<SelectItem> listaSelectItemUnidadeEnsino;
	protected List<DisciplinaVO> listaConsultasDisciplinas;
	protected TurmaVO turmaVO;
	protected Integer disciplina;
	protected String semestre;
	protected String ano;
	protected Integer professor;
	protected UnidadeEnsinoVO unidadeEnsinoVO;
	private String tipoLayout;
	private String filtroTipoCursoAluno;
	private String tipoAluno;
	private Date dataInicio;
	private Date dataFim;
	private Boolean turmaAgrupadaCursoPos;
	private Boolean liberarRegistroAulaEntrePeriodo;
	private String mes;
	private String anoMes;
	private FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO;
	private List<SelectItem> listaSelectItemTurma;
	private Date dataInicioPeriodoMatricula;
	private Date dataFimPeirodoMatricula;
	private Boolean apresentarDataMatricula;
	private Boolean apenasAlunosAtivos;
	private Boolean imprimirBackground;
	private Boolean trazerAlunoTransferencia;	
	private boolean permitirRealizarLancamentoAlunosPreMatriculados; 
	
	private List<SelectItem> listaSelectItemProfessorDisciplinaTurmaVOs;
	private Boolean apresentarComboBoxProfessorTurmaDisciplina;
	private ProfessorTitularDisciplinaTurmaVO professorDisciplinaTurmaVO;
	private Map<Integer, ProfessorTitularDisciplinaTurmaVO> mapProfessorDisciplinaTurmaVOs = new HashMap<Integer, ProfessorTitularDisciplinaTurmaVO>(0);
	private String tituloRelatorio; 

	public EspelhoRelControle() throws Exception {
		setModuloLayoutEtiquetaEnum(ModuloLayoutEtiquetaEnum.CRONOGRAMA_AULA);
		inicializarListasSelectItemTodosComboBox();
		verificarLayoutPadrao();
		setPermitirRealizarLancamentoAlunosPreMatriculados(getConfiguracaoGeralPadraoSistema().getPermitirProfessorRealizarLancamentoAlunosPreMatriculados());
		processarApresentarAno();
		processarApresentarSemestre();
		setMensagemID("msg_entre_prmrelatorio");
	}

	public void inicializarUnidadeEnsino() {
		try {
			setUnidadeEnsinoVO(getUnidadeEnsinoLogadoClone());
			if (Uteis.isAtributoPreenchido(getUnidadeEnsinoVO())) {
				setExisteUnidadeEnsino(Boolean.TRUE);
			} else {
				setExisteUnidadeEnsino(Boolean.FALSE);
			}
		} catch (Exception e) {
			setExisteUnidadeEnsino(Boolean.FALSE);
		}
	}
	
	public String getDownload() {
		if (getTipoLayout().equals("EspelhoDiarioControleNotaFrequenciaRel") || getApresentarLayoutEtiqueta()) {
			return super.getDownload();
		} else {
			if(getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
				return (getFazerDownload()?"RichFaces.$('panelOk').show();":"")+ super.getDownload() ;	
			}else {
				return (getFazerDownload()?"RichFaces.$('panelOkEspelho').show();":"")+ super.getDownload() ;
			}
			
		}
	}

	@SuppressWarnings("rawtypes")
	public void imprimirObjetoDiarioPDF() throws Exception {
		ProfessorTitularDisciplinaTurmaVO p = null;
		List listaRegistro = null;
//		String titulo = "Espelho da Turma";
		String titulo = getTituloRelatorio();
		String design = EspelhoRel.getDesignIReportRelatorio(getTipoLayout());
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "EspelhoDiarioRelControle", "Iniciando Impressao Relatorio PDF", "Emitindo Relatorio");
			if (getTurmaVO().getIntegralSemValidarLiberarRegistroAulaEntrePeriodo()) {
				setAno("");
				setSemestre("");
			}
			if (getTurmaVO().getAnual()) {
				setSemestre("");
			}
			if (!getTurmaVO().getIdentificadorTurma().equals("") && getDisciplina() != null && !getDisciplina().equals(0)) {
				// ProfessorMinistrouAulaTurmaVO p =
				// consultarProfessorTitularTurma(getEspelhoRel().getDisciplina());
				p = consultarProfessorTitularTurma(getDisciplina());
				setProfessor(p.getProfessor().getCodigo());
			} else {
				throw new Exception("Os campos Identicador Turma e Disciplina devem ser informados!");
			}
			listaRegistro = getFacadeFactory().getEspelhoRelFacade().consultarRegistroAula(getTurmaVO(), getSemestre(), getAno(), getProfessor(), getDisciplina(), getConfiguracaoFinanceiroPadraoSistema(), getFiltroRelatorioAcademicoVO(), getMes(), getAnoMes(), getUsuarioLogado());
			if (!listaRegistro.isEmpty()) {
				getSuperParametroRelVO().setNomeDesignIreport(design);
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setSubReport_Dir(EspelhoRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio(titulo);
				getSuperParametroRelVO().setListaObjetos(listaRegistro);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(EspelhoRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setQuantidade(listaRegistro.size());
				getSuperParametroRelVO().setUnidadeEnsino(getTurmaVO().getUnidadeEnsino().getNome());
				persistirLayoutPadrao(getTipoLayout());
				realizarImpressaoRelatorio();
				// removerObjetoMemoria(this);
				inicializarListasSelectItemTodosComboBox();
				verificarLayoutPadrao();

				setMensagemDetalhada("", "");
				// apresentarRelatorioObjetos(nomeRelatorio, titulo, "", "",
				// "PDF", "/" + EspelhoRel.getIdEntidade() + "/registros",
				// design, getUsuarioLogado().getNome(),
				// getFacadeFactory().getEspelhoRelFacade().getDescricaoFiltros(),
				// listaRegistro, EspelhoRel.getCaminhoBaseRelatorio());
			} else {
				persistirLayoutPadrao(getTipoLayout());
				setMensagemDetalhada("Não existe nenhum registro de aula para esta Turma neste Período.");
			}
			registrarAtividadeUsuario(getUsuarioLogado(), "EspelhoDiarioRelControle", "Finalizando Impressao Relatorio PDF", "Emitindo Relatorio");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			// Uteis.liberarListaMemoria(listaRegistro);
			// removerObjetoMemoria(p);
			// titulo = null;
			// design = null;
		}
	}

	private void persistirLayoutPadrao(String valor) throws Exception {
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getTipoLayout(), EspelhoRel.class.getSimpleName(), "tipoLayout", getUsuarioLogado());
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getTituloRelatorio(), EspelhoRel.class.getSimpleName(), getTipoLayout()+"Titulo", getUsuarioLogado());
	}

	private void verificarLayoutPadrao() throws Exception {
		LayoutPadraoVO layoutPadraoVO = getFacadeFactory().getLayoutPadraoFacade().consultarPorEntidadeCampo(EspelhoRel.class.getSimpleName(), "tipoLayout", false, getUsuarioLogado());
		if (!layoutPadraoVO.getValor().equals("")) {
			setTipoLayout(layoutPadraoVO.getValor());			
			selecionarLayoutRel();
		} else {
			setTituloRelatorio("");
		}
	}

	public void selecionarLayoutRel() throws Exception {
		LayoutPadraoVO layoutPadraoVO = getFacadeFactory().getLayoutPadraoFacade().consultarPorEntidadeCampo(EspelhoRel.class.getSimpleName(), getTipoLayout()+"Titulo", false, getUsuarioLogado());
		if(!layoutPadraoVO.getValor().equals("")) {
			setTituloRelatorio(layoutPadraoVO.getValor());
		}else {
			setTituloRelatorio("");
		}
	}

	public List<SelectItem> getListaTipoLayout() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		if (getTurmaVO().getCurso().getNivelEducacionalPosGraduacao() || getTurmaAgrupadaCursoPos()) {
			itens.add(new SelectItem("EspelhoDiarioModRetratoRel", "Layout 1 - Pós-Graduação"));
			itens.add(new SelectItem("EspelhoDiarioRel3", "Layout 2 - Pós-Graduação"));
			itens.add(new SelectItem("EspelhoDiarioReposicaoRel", "Reposição/Inclusão"));
			if (getTurmaVO().getCurso().getNivelEducacional().equals("EX")) {
				itens.add(new SelectItem("EspelhoDiarioRel", "Layout 2 - Extensão"));
				itens.add(new SelectItem("EspelhoDiarioRel2", "Layout 3 - Extensão"));
				itens.add(new SelectItem("EspelhoDiarioRel3", "Layout 4 - Faltas - Extensão"));
				itens.add(new SelectItem("EspelhoDiarioNotaRel", "Layout 5 - Notas - Extensão"));
			}
		}else if (getTurmaVO().getCurso().getNivelEducacional().equals("MT")){
			itens.add(new SelectItem("EspelhoDiarioModRetratoRel", "Layout 1 - Mestrado"));
			itens.add(new SelectItem("EspelhoDiarioRel3", "Layout 2 - Mestrado"));
			itens.add(new SelectItem("EspelhoDiarioRel", "Layout 3 - Mestrado"));
			itens.add(new SelectItem("EspelhoDiarioRel2", "Layout 4 - Mestrado"));
			itens.add(new SelectItem("EspelhoDiarioRel3", "Layout 5 - Faltas - Mestrado"));
			itens.add(new SelectItem("EspelhoDiarioNotaRel", "Layout 6 - Notas - Mestrado"));
			itens.add(new SelectItem("EspelhoDiarioControleNotaFrequenciaRel", "Layout 7 - Controle de Notas e Frequência - Mestrado"));
		} else {
			itens.add(new SelectItem("EspelhoDiarioRel", "Layout 2 - Graduação"));
			itens.add(new SelectItem("EspelhoDiarioRel2", "Layout 3 - Graduação"));
			itens.add(new SelectItem("EspelhoDiarioRel3", "Layout 4 - Faltas - Graduação"));
			itens.add(new SelectItem("EspelhoDiarioNotaRel", "Layout 5 - Notas - Graduação"));
			itens.add(new SelectItem("EspelhoDiarioControleNotaFrequenciaRel", "Layout 6 - Controle de Notas e Frequência - Graduação"));
		}
		return itens;
	}
	
	public boolean getIsLayoutControleNotaFrequencia() {
		return getTipoLayout().equals("EspelhoDiarioControleNotaFrequenciaRel");
	}

	@SuppressWarnings("rawtypes")
	public void imprimirObjetoDiarioVersoPDF() throws Exception {
		ProfessorTitularDisciplinaTurmaVO p = null;
		List listaRegistro = null;
		//String titulo = "Espelho da Turma";
		String titulo = getTituloRelatorio();
		String design = EspelhoRel.getDesignIReportRelatorioVerso();
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "EspelhoDiarioRelControle", "Iniciando Impressao Relatorio PDF - Verso", "Emitindo Relatorio");
			if (getTurmaVO().getIntegralSemValidarLiberarRegistroAulaEntrePeriodo()) {
				setAno("");
				setSemestre("");
			}
			if (getTurmaVO().getAnual()) {
				setSemestre("");
			}
			if (!getTurmaVO().getIdentificadorTurma().equals("") && getDisciplina() != null && !getDisciplina().equals(0)) {
				// ProfessorMinistrouAulaTurmaVO p =
				// consultarProfessorTitularTurma(getEspelhoRel().getDisciplina());
				p = consultarProfessorTitularTurma(getDisciplina());
				setProfessor(p.getProfessor().getCodigo());
			} else {
				throw new Exception("Os campos Identicador Turma e Disciplina devem ser informados!");
			}
			listaRegistro = getFacadeFactory().getEspelhoRelFacade().consultarRegistroAula(getTurmaVO(), getSemestre(), getAno(), getProfessor(), getDisciplina(), getConfiguracaoFinanceiroPadraoSistema(), getFiltroRelatorioAcademicoVO(), getMes(), getAnoMes(), getUsuarioLogado());
			if (!listaRegistro.isEmpty()) {

				setMensagemDetalhada("", "");
				getSuperParametroRelVO().setNomeDesignIreport(design);
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setSubReport_Dir(EspelhoRel.getCaminhoBaseRelatorioVerso());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio(titulo);
				getSuperParametroRelVO().setListaObjetos(listaRegistro);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(EspelhoRel.getCaminhoBaseRelatorioVerso());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setQuantidade(listaRegistro.size());
				getSuperParametroRelVO().setUnidadeEnsino(getTurmaVO().getUnidadeEnsino().getNome());
				persistirLayoutPadrao(getTipoLayout());
				realizarImpressaoRelatorio();
				removerObjetoMemoria(this);
				inicializarListasSelectItemTodosComboBox();
				verificarLayoutPadrao();
				// apresentarRelatorioObjetos(nomeRelatorio, titulo, "", "",
				// "PDF", "/" + EspelhoRel.getIdEntidadeVerso() + "/registros",
				// design, getUsuarioLogado().getNome(),
				// getFacadeFactory().getEspelhoRelFacade().getDescricaoFiltros(),
				// listaRegistro, EspelhoRel.getCaminhoBaseRelatorioVerso());
			} else {
				setMensagemDetalhada("Não existe nenhum registro de aula para esta Turma neste Período.");
			}
			registrarAtividadeUsuario(getUsuarioLogado(), "EspelhoDiarioRelControle", "Finalizando Impressao Relatorio PDF - Verso", "Emitindo Relatorio");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
//			Uteis.liberarListaMemoria(listaRegistro);
//			removerObjetoMemoria(p);
			titulo = null;
			design = null;
		}
	}

	
	public void imprimirObjetoEspelhoDiarioPDF() throws Exception {
//		String titulo = "";
//		if (getTipoLayout().equals("EspelhoDiarioReposicaoRel")) {
//			titulo = "Diário de Reposição";
//		} else if (getTipoLayout().equals("EspelhoDiarioControleNotaFrequenciaRel")) {
//			titulo = "Controle de Notas e Frequência (CNF)";
//		} else {
//			titulo = "Espelho do Diário da Turma";
//		}
		String titulo = getTituloRelatorio();
		String design = EspelhoRel.getDesignIReportRelatorioEspelhoDiario(getTipoLayout());
		if (getImprimirBackground()) {
			design = EspelhoRel.getDesignIReportRelatorioEspelhoDiarioComBackground();
		}
		if (getTipoLayout().equals("EspelhoDiarioReposicaoRel")) {
			titulo = getTituloRelatorio() + " (Reposição/Inclusão)";
		}
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "EspelhoDiarioRelControle", "Iniciando Impressao Relatorio PDF - Espelho", "Emitindo Relatorio");
			if (!Uteis.isAtributoPreenchido(getTurmaVO().getIdentificadorTurma())) {
				throw new Exception(UteisJSF.internacionalizar("msg_ParametroConsulta_turmaVazio"));
			}
			if (getTurmaVO().getIntegralSemValidarLiberarRegistroAulaEntrePeriodo()) {
				setAno("");
				setSemestre("");
			}
			if (getTurmaVO().getAnual()) {
				setSemestre("");
			}
			final List<Integer> listaDisciplina = new ArrayList<Integer>();
			int contador = 1;
			while (contador < getListaSelectItemDisciplina().size()) {
				if (getDisciplina() == 0) {
					listaDisciplina.add(Integer.parseInt((((SelectItem)getListaSelectItemDisciplina().get(contador)).getValue()).toString()));
				} else {
					if (!listaDisciplina.contains(getDisciplina())) {
						listaDisciplina.add(getDisciplina());
						break;
					}
				}
				contador++;
			}
			final UsuarioVO usuario = getUsuarioLogadoClone();
			final Boolean trazerAlunoPendenteFinanceiramente = getTrazerAlunoPendenteFinanceiramente();
			List<DiarioRegistroAulaVO> listaRegistro = new ArrayList<DiarioRegistroAulaVO>();
			final Map<Integer, List<DiarioRegistroAulaVO>> mapListaRegistro = new HashMap<Integer, List<DiarioRegistroAulaVO>>(0);
			final  ConsistirException consistirException = new ConsistirException("");
			ProcessarParalelismo.Processo p = new ProcessarParalelismo.Processo() {				
				@Override
				public void run(int i) {					
					Integer disciplinaParam = 0;	
					List<ProfessorTitularDisciplinaTurmaVO> listaProfessores = new ArrayList<ProfessorTitularDisciplinaTurmaVO>(0);
					ProfessorTitularDisciplinaTurmaVO p = new ProfessorTitularDisciplinaTurmaVO();
					List<DiarioRegistroAulaVO> listaDiarioRelVO = new ArrayList<DiarioRegistroAulaVO>(0);
					try {
						disciplinaParam = listaDisciplina.get(i);
						if (!getTurmaVO().getIdentificadorTurma().equals("")) {
							if (!getProfessorDisciplinaTurmaVO().getProfessor().getCodigo().equals(0) && getMapProfessorDisciplinaTurmaVOs().containsKey(getProfessorDisciplinaTurmaVO().getProfessor().getCodigo())) {
								p = getMapProfessorDisciplinaTurmaVOs().get(getProfessorDisciplinaTurmaVO().getProfessor().getCodigo());
							} else {
								p = getFacadeFactory().getDiarioRelFacade().consultarProfessorTitularTurma(getTurmaVO(), disciplinaParam, getAno(), getSemestre(), false, usuario);
							}
							listaProfessores.add(p);
						}							
						
						if (usuario.getIsApresentarVisaoCoordenador()) {
							listaDiarioRelVO = getFacadeFactory().getEspelhoRelFacade().consultarRegistroAulaEspelho(listaProfessores, getTurmaVO(), disciplinaParam, getSemestre(), getAno(), getApenasAlunosAtivos(), trazerAlunoPendenteFinanceiramente, getFiltroTipoCursoAluno(), null, usuario, getTipoLayout(), getTipoAluno(), getDataInicio(), getDataFim(), null, getMes(), getAnoMes(), getDataInicioPeriodoMatricula(), getDataFimPeirodoMatricula(), getTrazerAlunoTransferencia(), isPermitirRealizarLancamentoAlunosPreMatriculados(), getApresentarDataMatricula());
							
							if(!listaDiarioRelVO.isEmpty()) {
								mapListaRegistro.put(disciplinaParam, listaDiarioRelVO);
							}
							
						} else {
							listaDiarioRelVO = getFacadeFactory().getEspelhoRelFacade().consultarRegistroAulaEspelho(listaProfessores, getTurmaVO(), disciplinaParam, getSemestre(), getAno(), false, trazerAlunoPendenteFinanceiramente, getFiltroTipoCursoAluno(), null, usuario, getTipoLayout(), getTipoAluno(), getDataInicio(), getDataFim(), getFiltroRelatorioAcademicoVO(), getMes(), getAnoMes(), getDataInicioPeriodoMatricula(), getDataFimPeirodoMatricula(), getTrazerAlunoTransferencia(), isPermitirRealizarLancamentoAlunosPreMatriculados(), getApresentarDataMatricula());
							 
							if(!listaDiarioRelVO.isEmpty()) {
								mapListaRegistro.put(disciplinaParam, listaDiarioRelVO);
							}
						}
//						mapListaRegistro.put(disciplinaParam, getFacadeFactory().getDiarioRelFacade().consultarRegistroAulaVerso(listaProfessores, getDisciplina(), getTurmaVO(), getSemestre(), getAno(), true, trazerAlunoPendenteFinanceiramente, false, getFiltroTipoCursoAluno(), getTipoAluno(), usuario, getFiltroRelatorioAcademicoVO(), getMes(), getAnoMes(), getConfiguracaoAcademico(), getApresentarAulasNaoRegistradas(), getApresentarSituacaoMatricula(), getTipoLayout(), getTrazerAlunoTransferencia()).get(0));

					} catch (Exception e) {
						if (!consistirException.getListaMensagemErro().contains(e.getMessage()) && Uteis.isAtributoPreenchido(e.getMessage())) {
							  consistirException.adicionarListaMensagemErro(e.getMessage());
						}
					}finally {
						p = null;
						Uteis.liberarListaMemoria(listaProfessores);
					}
				}
			};
			ProcessarParalelismo.executar(0, listaDisciplina.size(), consistirException, p);
			p = null;
			if(!mapListaRegistro.isEmpty()) {
				int index = 1;
				for(Integer key: mapListaRegistro.keySet()) {
					for(DiarioRegistroAulaVO registroAulaVO: mapListaRegistro.get(key)) {
						registroAulaVO.setIndice(index++);
						listaRegistro.add(registroAulaVO);
			}
				}
			}
			if(listaRegistro.stream().allMatch(a -> a.getDiarioFrequenciaVOs().isEmpty())) {
				throw new Exception("Não existe nenhum aluno matriculado ou aula programada nesta TURMA/DISCIPLINA neste período.");
			}else {
				listaRegistro.removeIf(a-> a.getDiarioFrequenciaVOs().isEmpty());
			}
			if (!consistirException.getListaMensagemErro().isEmpty()) {
				throw new Exception(consistirException.getToStringMensagemErro());
			}
 
			if (!listaRegistro.isEmpty()) {
				if (getTurmaVO().getTurmaAgrupada() && !Uteis.isAtributoPreenchido(getTurmaVO().getCurso().getNome()) && Uteis.isAtributoPreenchido(getTurmaVO().getAbreviaturaCurso())) {
					getTurmaVO().getCurso().setNome(getTurmaVO().getAbreviaturaCurso());
				}
				setMensagemDetalhada("", "");
				getSuperParametroRelVO().setNomeDesignIreport(design);
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setSubReport_Dir(EspelhoRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio(titulo);
				getSuperParametroRelVO().setListaObjetos(listaRegistro);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(EspelhoRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setQuantidade(listaRegistro.size());
				getSuperParametroRelVO().setUnidadeEnsino(getTurmaVO().getUnidadeEnsino().getNome());
				getSuperParametroRelVO().adicionarParametro("apresentarDataMatricula", getApresentarDataMatricula());
				getSuperParametroRelVO().setAno(getAno());
				getSuperParametroRelVO().setSemestre(getSemestre());
				if(!getUnidadeEnsinoVO().getCodigo().equals(0)) {
					setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
					getSuperParametroRelVO().adicionarLogoUnidadeEnsinoSelecionada(getUnidadeEnsinoVO());
				}
				persistirLayoutPadrao(getTipoLayout());
				realizarImpressaoRelatorio();
				setMensagemID("msg_relatorio_ok");
				inicializarListasSelectItemTodosComboBox();
				verificarLayoutPadrao();
			} else {
				if(getTipoLayout().equals("EspelhoDiarioReposicaoRel")) {
					setMensagemDetalhada("Não existe nenhum aluno de Reposição/Inclusão para a geração deste relatório.");
				}else {
				setMensagemDetalhada("Não existe nenhum aluno matriculado ou aula programada nesta TURMA/DISCIPLINA neste período.");
			}
			}
			registrarAtividadeUsuario(getUsuarioLogado(), "EspelhoDiarioRelControle", "Finalizando Impressao Relatorio PDF - Espelho", "Emitindo Relatorio");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			// Uteis.liberarListaMemoria(listaRegistro);
			// Uteis.liberarListaMemoria(listaProfessores);
			// removerObjetoMemoria(p);
			// titulo = null;
			// design = null;
		}
	}

	@SuppressWarnings("rawtypes")
	public void imprimirObjetoEspelhoDiarioVersoPDF() throws Exception {
		List<ProfessorTitularDisciplinaTurmaVO> listaProfessores = new ArrayList<ProfessorTitularDisciplinaTurmaVO>(0);
		ProfessorTitularDisciplinaTurmaVO p = null;
		String titulo = "Verso do " + getTituloRelatorio();		
		//String titulo = "Verso do Espelho do Diário da Turma";
		String design = "";
		if (getTipoLayout().equals("EspelhoDiarioModRetratoRel")) {
			design = EspelhoRel.getDesignIReportRelatorioEspelhoDiarioVersoPos();
		} else if (getTipoLayout().equals("EspelhoDiarioRel3") || getTipoLayout().equals("EspelhoDiarioNotaRel")) {
			design = EspelhoRel.getDesignIReportRelatorioEspelhoDiarioVersoRel3();
		} else {
			design = EspelhoRel.getDesignIReportRelatorioEspelhoDiarioVersoGraduacao();
		}
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "EspelhoDiarioRelControle", "iniciando Impressao Relatorio PDF - Espelho - Verso", "Emitindo Relatorio");


			if (!Uteis.isAtributoPreenchido(getTurmaVO().getIdentificadorTurma())) {
				throw new Exception(UteisJSF.internacionalizar("msg_ParametroConsulta_turmaVazio"));
			}
			
			if (getTurmaVO().getIntegralSemValidarLiberarRegistroAulaEntrePeriodo()) {
				setAno("");
				setSemestre("");
			}
			if (getTurmaVO().getAnual()) {
				setSemestre("");
			}
 
			final List<Integer> listaDisciplina = new ArrayList<Integer>();
			int contador = 1;
			while (contador < getListaSelectItemDisciplina().size()) {
				if (getDisciplina() == 0) {
					listaDisciplina.add(Integer.parseInt((((SelectItem)getListaSelectItemDisciplina().get(contador)).getValue()).toString()));
				} else {
					listaDisciplina.add(getDisciplina());
				}
				contador++;
			}
			final UsuarioVO usuario = getUsuarioLogado();
			final Boolean trazerAlunoPendenteFinanceiramente = getTrazerAlunoPendenteFinanceiramente();
			List<DiarioRegistroAulaVO> listaRegistro = new ArrayList<DiarioRegistroAulaVO>();
			final Map<Integer, DiarioRegistroAulaVO> mapListaRegistro = new HashMap<Integer, DiarioRegistroAulaVO>(0);
			final  ConsistirException consistirException = new ConsistirException("");
			ProcessarParalelismo.executar(0, listaDisciplina.size(), consistirException, new ProcessarParalelismo.Processo() {				
				@Override
				public void run(int i) {					
					Integer disciplinaParam = 0;	
					List<ProfessorTitularDisciplinaTurmaVO> listaProfessores = new ArrayList<ProfessorTitularDisciplinaTurmaVO>(0);
					ProfessorTitularDisciplinaTurmaVO p = new ProfessorTitularDisciplinaTurmaVO();
					try {
						disciplinaParam = listaDisciplina.get(i);
						if (!getTurmaVO().getIdentificadorTurma().equals("")) {
							if (!getProfessorDisciplinaTurmaVO().getProfessor().getCodigo().equals(0)) {
								p = getMapProfessorDisciplinaTurmaVOs().get(getProfessorDisciplinaTurmaVO().getProfessor().getCodigo());
							} else {
								p = getFacadeFactory().getDiarioRelFacade().consultarProfessorTitularTurma(getTurmaVO(), disciplinaParam, getAno(), getSemestre(), false, usuario);
							}
							listaProfessores.add(p);
						}							
						if (usuario.getIsApresentarVisaoCoordenador()) {
							mapListaRegistro.put(disciplinaParam, getFacadeFactory().getEspelhoRelFacade().consultarRegistroAulaEspelho(listaProfessores, getTurmaVO(), disciplinaParam, getSemestre(), getAno(), false, trazerAlunoPendenteFinanceiramente, getFiltroTipoCursoAluno(), null, usuario, getTipoLayout(), getTipoAluno(), getDataInicio(), getDataFim(), null, getMes(), getAnoMes(), getDataInicioPeriodoMatricula(), getDataFimPeirodoMatricula(), getTrazerAlunoTransferencia(), isPermitirRealizarLancamentoAlunosPreMatriculados(), getApresentarDataMatricula()).get(0));
						} else {
							mapListaRegistro.put(disciplinaParam, getFacadeFactory().getEspelhoRelFacade().consultarRegistroAulaEspelho(listaProfessores, getTurmaVO(), disciplinaParam, getSemestre(), getAno(), false, trazerAlunoPendenteFinanceiramente, getFiltroTipoCursoAluno(), null, usuario, getTipoLayout(), getTipoAluno(), getDataInicio(), getDataFim(), getFiltroRelatorioAcademicoVO(), getMes(), getAnoMes(), getDataInicioPeriodoMatricula(), getDataFimPeirodoMatricula(), getTrazerAlunoTransferencia(), isPermitirRealizarLancamentoAlunosPreMatriculados(), getApresentarDataMatricula()).get(0));
						}

					} catch (Exception e) {
						if (!consistirException.getListaMensagemErro().contains(e.getMessage()) && Uteis.isAtributoPreenchido(e.getMessage())) {
							  consistirException.adicionarListaMensagemErro(e.getMessage());
						}
					}
				}
			});
			listaRegistro.addAll(mapListaRegistro.values());
			if(!consistirException.getListaMensagemErro().isEmpty()){
//				cscsjcscschjk
			}			
			if (!listaRegistro.isEmpty()) {
				setMensagemDetalhada("", "");
				getSuperParametroRelVO().setNomeDesignIreport(design);
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setSubReport_Dir(EspelhoRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio(titulo);
				getSuperParametroRelVO().setListaObjetos(listaRegistro);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(EspelhoRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setQuantidade(listaRegistro.size());
				getSuperParametroRelVO().setUnidadeEnsino(getTurmaVO().getUnidadeEnsino().getNome());
				getSuperParametroRelVO().adicionarParametro("apresentarDataMatricula", getApresentarDataMatricula());
				getSuperParametroRelVO().setAno(getAno());
				getSuperParametroRelVO().setSemestre(getSemestre());
				persistirLayoutPadrao(getTipoLayout());
				realizarImpressaoRelatorio();
//				removerObjetoMemoria(this);
//				inicializarListasSelectItemTodosComboBox();
//				verificarLayoutPadrao();
				// apresentarRelatorioObjetos(nomeRelatorio, titulo, "", "",
				// "PDF", "/" + EspelhoRel.getIdEntidade() + "/registros",
				// design, getUsuarioLogado().getNome(),
				// getFacadeFactory().getEspelhoRelFacade().getDescricaoFiltros(),
				// listaRegistro, EspelhoRel.getCaminhoBaseRelatorio());
			} else {
				setMensagemDetalhada("Não existe nenhum aluno matriculado nesta TURMA neste período.");
			}
			registrarAtividadeUsuario(getUsuarioLogado(), "EspelhoDiarioRelControle", "Finalizando Impressao Relatorio PDF - Espelho - Verso", "Emitindo Relatorio");
			setMensagemID("msg_relatorio_ok");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			//Uteis.liberarListaMemoria(listaRegistro);
//			Uteis.liberarListaMemoria(listaProfessores);
//			removerObjetoMemoria(p);
//			if (getUsuarioLogado().getVisaoLogar().equals("coordenador")) {
//				montarListaSelectItemTurmaVisaoCoordenador();
//			}
			titulo = null;
			design = null;
		}
	}

	@SuppressWarnings("rawtypes")
	public void imprimirObjetoEspelhoDiarioPDFVisaoProfessor() throws Exception {
		// Rotinas ja otimizadas by Pedro.
		List<ProfessorTitularDisciplinaTurmaVO> listaProfessores = new ArrayList<ProfessorTitularDisciplinaTurmaVO>(0);
		ProfessorTitularDisciplinaTurmaVO p = null;
		List listaRegistro = null;
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "EspelhoDiarioRelControle", "Iniciando Impressao Relatorio PDF - Espelho - Visao Professor", "Emitindo Relatorio");

			if (!getTurmaVO().getIdentificadorTurma().equals("")) {
				// ProfessorMinistrouAulaTurmaVO p = new
				// ProfessorMinistrouAulaTurmaVO();
				p = new ProfessorTitularDisciplinaTurmaVO();
				if (getDisciplina() == null || getDisciplina().equals(0)) {
					throw new Exception("O campo Disciplina deve ser informado!");
					// Regra de negocio alterada nao é mais permitido gerar o
					// relatorio sem informa uma disciplina
					// for (DisciplinaVO disciplinaVO :
					// getListaConsultasDisciplinas()) {
					// p =
					// consultarProfessorTitularTurma(disciplinaVO.getCodigo());
					// listaProfessores.add(p);
					// // p = new ProfessorMinistrouAulaTurmaVO();
					// p = new ProfessorTitularDisciplinaTurmaVO();
					// }
				} else {
					p = consultarProfessorTitularTurma(getDisciplina());
					listaProfessores.add(p);
				}
			} else {
				throw new Exception("O campo Identicador Turma deve ser informado!");
			}
			if (getDisciplina().intValue() == 0) {
				throw new Exception("O campo DISCIPLINA deve ser informado!");
			}

			if (getTurmaVO().getIntegralSemValidarLiberarRegistroAulaEntrePeriodo()) {
				setAno("");
				setSemestre("");
			}
			if (getTurmaVO().getAnual()) {
				setSemestre("");
			}
			// if (getTurmaVO().getCurso().getNivelEducacionalPosGraduacao()) {
			// setFiltroTipoCursoAluno("posGraduacao");
			// } else {
			// setFiltroTipoCursoAluno("todos");
			// }
			listaRegistro = getFacadeFactory().getEspelhoRelFacade().consultarRegistroAulaEspelho(listaProfessores, getTurmaVO(), getDisciplina(), getSemestre(), getAno(), true, getTrazerAlunoPendenteFinanceiramente(), getFiltroTipoCursoAluno(), getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado(), getTipoLayout(), getTipoAluno(), getDataInicio(), getDataFim(), null, getMes(), getAnoMes(), getDataInicioPeriodoMatricula(), getDataFimPeirodoMatricula(), getTrazerAlunoTransferencia(), isPermitirRealizarLancamentoAlunosPreMatriculados(), getApresentarDataMatricula());
			if (!listaRegistro.isEmpty()) {
				setMensagemDetalhada("", "");
				String design = EspelhoRel.getDesignIReportRelatorioEspelhoDiario(getTipoLayout());
				if (getImprimirBackground()) {
					design = EspelhoRel.getDesignIReportRelatorioEspelhoDiarioComBackground();
				}
				getSuperParametroRelVO().setNomeDesignIreport(design);
				getSuperParametroRelVO().setNomeDesignIreport(EspelhoRel.getDesignIReportRelatorioEspelhoDiario(getTipoLayout()));
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setSubReport_Dir(EspelhoRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio(getTituloRelatorio());
				getSuperParametroRelVO().setListaObjetos(listaRegistro);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(EspelhoRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setQuantidade(listaRegistro.size());
				getSuperParametroRelVO().setUnidadeEnsino(getTurmaVO().getUnidadeEnsino().getNome());
				getSuperParametroRelVO().adicionarParametro("apresentarDataMatricula", getApresentarDataMatricula());
				getSuperParametroRelVO().setAno(getAno());
				getSuperParametroRelVO().setSemestre(getSemestre());
				persistirLayoutPadrao(getTipoLayout());
				realizarImpressaoRelatorio();
				// removerObjetoMemoria(this);
				inicializarListasSelectItemTodosComboBox();
				verificarLayoutPadrao();
			} else {
				if(getTipoLayout().equals("EspelhoDiarioReposicaoRel")) {
					setMensagemDetalhada("Não existe nenhum aluno de Reposição/Inclusão para a geração deste relatório.");
				}else {
				setMensagemDetalhada("Não existe nenhum aluno matriculado nesta TURMA neste período.");
			}
			}
			registrarAtividadeUsuario(getUsuarioLogado(), "EspelhoDiarioRelControle", "Finalizando Impressao Relatorio PDF - Espelho - Visao Professor", "Emitindo Relatorio");
			setMensagemID("msg_relatorio_ok");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			// Uteis.liberarListaMemoria(listaRegistro);
			// Uteis.liberarListaMemoria(listaProfessores);
			// removerObjetoMemoria(p);
			// montarListaSelectItemTurma();
		}
	}

	@SuppressWarnings("rawtypes")
	public void imprimirObjetoEspelhoDiarioVersoPDFVisaoProfessor() throws Exception {
		// Rotinas ja otimizadas by Pedro.
		List<ProfessorTitularDisciplinaTurmaVO> listaProfessores = new ArrayList<ProfessorTitularDisciplinaTurmaVO>(0);
		ProfessorTitularDisciplinaTurmaVO p = null;
		List listaRegistro = null;
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "EspelhoDiarioRelControle", "Iniciando Impressao Relatorio PDF - Espelho Verso - Visao Professor", "Emitindo Relatorio");

			if (!getTurmaVO().getIdentificadorTurma().equals("")) {
				p = new ProfessorTitularDisciplinaTurmaVO();
				if (getDisciplina() == null || getDisciplina().equals(0)) {
					throw new Exception("O campo Disciplina deve ser informado!");			
				} else {
					p = consultarProfessorTitularTurma(getDisciplina());
					listaProfessores.add(p);
				}
			} else {
				throw new Exception("O campo Identicador Turma deve ser informado!");
			}
			if (getDisciplina().intValue() == 0) {
				throw new Exception("O campo DISCIPLINA deve ser informado!");
			}
			if (getTurmaVO().getIntegralSemValidarLiberarRegistroAulaEntrePeriodo()) {
				setAno("");
				setSemestre("");
			}
			if (getTurmaVO().getAnual()) {
				setSemestre("");
			}
			if (getTurmaVO().getCurso().getNivelEducacionalPosGraduacao()) {
				setFiltroTipoCursoAluno("posGraduacao");
			} else {
				setFiltroTipoCursoAluno("todos");
			}
			listaRegistro = getFacadeFactory().getEspelhoRelFacade().consultarRegistroAulaEspelho(listaProfessores, getTurmaVO(), getDisciplina(), getSemestre(), getAno(), true, getTrazerAlunoPendenteFinanceiramente(), getFiltroTipoCursoAluno(), getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado(), getTipoLayout(), getTipoAluno(), getDataInicio(), getDataFim(), null, getMes(), getAnoMes(), getDataInicioPeriodoMatricula(), getDataFimPeirodoMatricula(), getTrazerAlunoTransferencia(), isPermitirRealizarLancamentoAlunosPreMatriculados(), getApresentarDataMatricula());
			if (!listaRegistro.isEmpty()) {
				setMensagemDetalhada("", "");
				if (getTipoLayout().equals("EspelhoDiarioModRetratoRel")) {
					getSuperParametroRelVO().setNomeDesignIreport(EspelhoRel.getDesignIReportRelatorioEspelhoDiarioVersoPos());
				} else {
					getSuperParametroRelVO().setNomeDesignIreport(EspelhoRel.getDesignIReportRelatorioEspelhoDiarioVersoGraduacao());
				}
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setSubReport_Dir(EspelhoRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio(getTituloRelatorio());
				getSuperParametroRelVO().setListaObjetos(listaRegistro);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(EspelhoRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setQuantidade(listaRegistro.size());
				getSuperParametroRelVO().setUnidadeEnsino(getTurmaVO().getUnidadeEnsino().getNome());
				getSuperParametroRelVO().adicionarParametro("apresentarDataMatricula", getApresentarDataMatricula());
				getSuperParametroRelVO().setAno(getAno());
				getSuperParametroRelVO().setSemestre(getSemestre());
				persistirLayoutPadrao(getTipoLayout());
				realizarImpressaoRelatorio();
				limparDadosMemoriaVisaoProfessor();
				inicializarListasSelectItemTodosComboBox();
				verificarLayoutPadrao();
			} else {
				setMensagemDetalhada("Não existe nenhum aluno matriculado nesta TURMA neste período.");
			}
			registrarAtividadeUsuario(getUsuarioLogado(), "EspelhoDiarioRelControle", "Finalizando Impressao Relatorio PDF - Espelho Verso - Visao Professor", "Emitindo Relatorio");
			setMensagemID("msg_relatorio_ok");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			Uteis.liberarListaMemoria(listaRegistro);
			Uteis.liberarListaMemoria(listaProfessores);
			removerObjetoMemoria(p);
			montarListaSelectItemTurmaVisaoProfessor();
		}
	}

	public void limparDadosMemoriaVisaoCoordenador() {
		removerObjetoMemoria(this);
		inicializarListasSelectItemTodosComboBox();
		montarListaSelectItemTurmaVisaoCoordenador();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void montarListaSelectItemTurmaVisaoCoordenador() {
		List<TurmaVO> listaResultado = null;
		Iterator<TurmaVO> i = null;
		try {
			listaResultado = consultarTurmaPorCoordenador();
			getListaSelectItemTurma().clear();
			i = listaResultado.iterator();
			String value = "";
			while (i.hasNext()) {
				TurmaVO turma = i.next();
				if (turma.getTurmaAgrupada()) {
					value = turma.getIdentificadorTurma() + " - Turno " + turma.getTurno().getNome();
				} else {
					value = turma.getIdentificadorTurma() + " - Curso " + turma.getCurso().getNome() + " - Turno " + turma.getTurno().getNome();
				}
				getListaSelectItemTurma().add(new SelectItem(turma.getCodigo(), value));
			}
			if(!listaResultado.isEmpty()){
				setTurmaVO(listaResultado.get(0));
			
			}
			montarListaSelectItemDisciplinaTurma();
			SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
			Collections.sort((List) getListaSelectItemTurma(), ordenador);
		} catch (Exception e) {
			setListaSelectItemTurma(new ArrayList<SelectItem>(0));
		} finally {
			Uteis.liberarListaMemoria(listaResultado);
			i = null;
		}
	}

	public List<TurmaVO> consultarTurmaPorCoordenador() throws Exception {
		return getFacadeFactory().getTurmaFacade().consultaRapidaPorCoordenadorAnoSemestre(getUsuarioLogado().getPessoa().getCodigo(), false, false, true, false, getAno(), getSemestre(), getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
	}

	
	public void montarListaDisciplinaTurmaVisaoCoordenador() {
		try {
			if (getUsuarioLogado().getIsApresentarVisaoCoordenador() && Uteis.isAtributoPreenchido(getTurmaVO())) {
				getTurmaVO().setNivelMontarDados(NivelMontarDados.NAO_INICIALIZADO);
				getFacadeFactory().getTurmaFacade().carregarDados(getTurmaVO(), NivelMontarDados.BASICO, getUsuarioLogado());
			}
			montarListaSelectItemDisciplinaTurma();
		} catch (Exception e) {
			setListaSelectItemDisciplina(null);
		}
	}
	


	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void montarListaSelectItemTurma() {
		List<Integer> mapAuxiliarSelectItem = new ArrayList();
		List<TurmaVO> listaResultado = null;
		Iterator<TurmaVO> i = null;
		try {
			List<SelectItem> obj = new ArrayList<SelectItem>(0);
			listaResultado = consultarTurmaPorProfessor();
			obj.add(new SelectItem(0, ""));
			i = listaResultado.iterator();
			String value = "";
			while (i.hasNext()) {
				TurmaVO turma = i.next();
				if(!mapAuxiliarSelectItem.contains(turma.getCodigo())){
					obj.add(new SelectItem(turma.getCodigo(), turma.aplicarRegraNomeCursoApresentarCombobox()));
					mapAuxiliarSelectItem.add(turma.getCodigo());
				}
			}
			SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
			Collections.sort((List) obj, ordenador);
			setListaSelectItemTurma(obj);
		} catch (Exception e) {
			setListaSelectItemTurma(new ArrayList<SelectItem>(0));
		} finally {
			Uteis.liberarListaMemoria(listaResultado);
			i = null;
		}
	}

	public List<TurmaVO> consultarTurmaPorProfessor() throws Exception {
		// if (getUsuarioLogado().getVisaoLogar().equals("professor")) {
		if (getConfiguracaoGeralPadraoSistema().getPerfilPadraoProfessorGraduacao().getCodigo().intValue() == getUsuarioLogado().getPerfilAcesso().getCodigo()) {
			return getFacadeFactory().getTurmaFacade().consultarTurmaPorProfessorAnoSemestreNivelDadosCombobox(getUsuarioLogado().getPessoa().getCodigo(), getSemestre(), getAno(), "AT", 0, getUsuarioLogado().getVisaoLogar().equals("professor"), false, false);
		} else if (getConfiguracaoGeralPadraoSistema().getPerfilPadraoProfessorPosGraduacao().getCodigo().intValue() == getUsuarioLogado().getPerfilAcesso().getCodigo()) {
			return getFacadeFactory().getTurmaFacade().consultarTurmaPorProfessorAnoSemestreNivelDadosCombobox(getUsuarioLogado().getPessoa().getCodigo(), getSemestre(), getAno(), "AT", 0, getUsuarioLogado().getVisaoLogar().equals("professor"), true, false);
		} else {
			return getFacadeFactory().getTurmaFacade().consultarTurmaPorProfessorAnoSemestreNivelDadosCombobox(getUsuarioLogado().getPessoa().getCodigo(), getSemestre(), getAno(), "AT", 0, getUsuarioLogado().getVisaoLogar().equals("professor"), false, true);
		}
		// } else {
		// return
		// getFacadeFactory().getTurmaFacade().consultarTurmaPorProfessor(getUsuarioLogado().getPessoa().getCodigo(),
		// getUnidadeEnsinoLogado().getCodigo(), false,
		// Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
		// }
	}

	public void consultarTurmaProfessor() throws Exception {
		try {
			getTurmaVO().setNivelMontarDados(NivelMontarDados.NAO_INICIALIZADO);
			getFacadeFactory().getTurmaFacade().carregarDados(getTurmaVO(), NivelMontarDados.BASICO, getUsuarioLogado());
			getTurmaVO().getUnidadeEnsino().setCidade(getFacadeFactory().getCidadeFacade().consultarCidadePorUnidadeEnsino(getTurmaVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			processarApresentarAno();
			processarApresentarSemestre();
			montarListaSelectItemDisciplinaTurma();
			setProfessor(getUsuarioLogado().getPessoa().getCodigo());
		} catch (Exception e) {
			setTurmaVO(new TurmaVO());
			setListaSelectItemDisciplina(new ArrayList<SelectItem>(0));
			processarApresentarAno();
			processarApresentarSemestre();
			montarListaSelectItemTurmaVisaoProfessor();
		}
	}

	public void montarListaSelectItemDisciplinaTurma() {		
		setListaSelectItemDisciplina(new ArrayList<SelectItem>(0));
		try {
			if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
				List<DisciplinaVO> resultado = getFacadeFactory().getDisciplinaFacade().consultaRapidaPorDisciplinaProfessorTurmaAgrupada(getUsuarioLogado().getPessoa().getCodigo(), getTurmaVO().getCodigo(), getSemestre(), getAno(), getTurmaVO().getCurso().getLiberarRegistroAulaEntrePeriodo(), false, getUsuarioLogado());
				setListaSelectItemDisciplina(UtilSelectItem.getListaSelectItem(resultado, "codigo", "nome"));
			} else {
				List<HorarioTurmaDisciplinaProgramadaVO> resultado = getFacadeFactory().getHorarioTurmaFacade().consultarHorarioTurmaDisciplinaProgramadaPorTurma(getTurmaVO().getCodigo(), false, true, 0);
				setListaSelectItemDisciplina(UtilSelectItem.getListaSelectItem(resultado, "codigoDisciplina", "nomeDisciplina"));				
			}			
		} catch (Exception e) {
			setListaSelectItemDisciplina(new ArrayList<SelectItem>(0));
		}
	}
	


	public String getApresentarRelatrio() {
		if (getMensagemDetalhada().equals("")) {
			return "";
		}
		
		return "";
	}

	@SuppressWarnings("rawtypes")
	public void imprimirObjetoEspelhoDiarioHTML() throws Exception {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "EspelhoDiarioRelControle", "Iniciando Impressao Relatorio HTML ", "Emitindo Relatorio");
			List listaRegistro = getFacadeFactory().getEspelhoRelFacade().consultarRegistroAulaEspelho(getTurmaVO(), getSemestre(), getAno(), getProfessor(), getDisciplina(), getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema().getPermitirProfessorRealizarLancamentoAlunosPreMatriculados());
			String nomeRelatorio = EspelhoRel.getIdEntidade();
			//String titulo = "Espelho Diário da Turma";
			String titulo = getTituloRelatorio();
			String design = EspelhoRel.getDesignIReportRelatorio(getTipoLayout());
			setMensagemDetalhada("", "");
			if (!listaRegistro.isEmpty()) {
				apresentarRelatorioObjetos(nomeRelatorio, titulo, "", "", "HTML", "/" + EspelhoRel.getIdEntidade() + "/registros", design, getUsuarioLogado().getNome(), getFacadeFactory().getEspelhoRelFacade().getDescricaoFiltros(), listaRegistro, EspelhoRel.getCaminhoBaseRelatorio());
			} else {
				setMensagemDetalhada("Não existe nenhum aluno matriculado nesta TURMA neste período.");
			}
			registrarAtividadeUsuario(getUsuarioLogado(), "EspelhoDiarioRelControle", "Finalizando Impressao Relatorio HTML ", "Emitindo Relatorio");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarTurma() {
		try {
			List<TurmaVO> objs = new ArrayList<TurmaVO>(0);
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(getValorConsultaTurma(), getUnidadeEnsinoVO().getCodigo(), false, false, "", false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeCurso")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaNomeCurso(getValorConsultaTurma(), getUnidadeEnsinoVO().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado());
			}
			setListaConsultaTurma(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaTurma(new ArrayList<TurmaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparIdentificador() {
		setTurmaVO(new TurmaVO());
		setListaSelectItemDisciplina(new ArrayList<SelectItem>(0));
	}

	public void selecionarTurma() throws Exception {
		TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
		setTurmaVO(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
		if (getTurmaVO().getSubturma()) {
			getTurmaVO().setCurso(getFacadeFactory().getCursoFacade().consultarCursoPorTurma(getTurmaVO().getTurmaPrincipal(), false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
		} else if (getTurmaVO().getTurmaAgrupada()) {
			getTurmaVO().setCurso(new CursoVO());
		} else {
			getTurmaVO().setCurso(getTurmaVO().getCurso());
		}
		getUnidadeEnsinoVO().setCodigo(getTurmaVO().getUnidadeEnsino().getCodigo());
		setProfessor(getUsuarioLogado().getPessoa().getCodigo());
		montarListaSelectItemDisciplinaTurma();
		verificarTurmaAgrupadaDefinicaoLayoutApresentar();
		setLiberarRegistroAulaEntrePeriodo(getFacadeFactory().getTurmaFacade().consultarLiberarRegistroAulaEntrePeriodoConsiderandoTodosCursosTurmaAgrupada(getTurmaVO()));
		if (!getMostrarAnoSemestre()) {
			setSemestre("");
		}
		if (!getMostrarSemestre()) {
			setAno("");
		}
		if (getTurmaVO().getCurso().getNivelEducacionalPosGraduacao() || getTurmaAgrupadaCursoPos()) {
			tipoLayout = "EspelhoDiarioModRetratoRel";
		}else if (getTurmaVO().getCurso().getNivelEducacional().equals("MT")){
			tipoLayout = "EspelhoDiarioModRetratoRel";
		} else {
			tipoLayout = "EspelhoDiarioRel";
		}
		verificarLayoutPadrao();
		obj = null;
		valorConsultaTurma = "";
		campoConsultaTurma = "";
		listaConsultaTurma.clear();
	}

	public void verificarTurmaAgrupadaDefinicaoLayoutApresentar() {
		if (getTurmaVO().getTurmaAgrupada()) {
			List<String> listaNivelEducacional = getFacadeFactory().getCursoFacade().consultarNivelEducacionalPorTurmaAgrupada(getTurmaVO().getCodigo(), getUsuarioLogado());
			for (String nivelEducacional : listaNivelEducacional) {
				if (nivelEducacional.equals("PO") || nivelEducacional.equals("EX")) {
					setTurmaAgrupadaCursoPos(Boolean.TRUE);
					break;
				}
			}
		}
	}

	public List<SelectItem> getListaSelectItemTipoAluno() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("todos", "Todos"));
		itens.add(new SelectItem("normal", "Alunos (Turma Origem)"));
		itens.add(new SelectItem("reposicao", "Alunos (Reposição/Inclusão)"));
		return itens;
	}

	public List<SelectItem> getTipoConsultaComboTurma() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("identificadorTurma", "Identificador"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
	}

	public Boolean getSolicitarSemestreAnoParaEmissaoDiario() {
		if (getTurmaVO().getCurso().getNivelEducacionalPosGraduacao()) {
			return false;
		} else {
			return true;
		}
	}

	public void montarTurma() throws Exception {
		try {
			if (!getTurmaVO().getIdentificadorTurma().equals("")) {
				setTurmaVO(getFacadeFactory().getTurmaFacade().consultarTurmaPorIdentificadorTurma(getTurmaVO().getIdentificadorTurma(), getUnidadeEnsinoLogado().getCodigo().intValue(), false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado()));
				setLiberarRegistroAulaEntrePeriodo(getFacadeFactory().getTurmaFacade().consultarLiberarRegistroAulaEntrePeriodoConsiderandoTodosCursosTurmaAgrupada(getTurmaVO()));
			} else {
				throw new Exception("Informe a Turma.");
			}
			if (!getMostrarAnoSemestre()) {
				setSemestre("");
			}
			if (!getMostrarSemestre()) {
				setAno("");
			}
			if (getTurmaVO().getSubturma()) {
				getTurmaVO().setCurso(getFacadeFactory().getCursoFacade().consultarCursoPorTurma(getTurmaVO().getTurmaPrincipal(), false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
			} else if (getTurmaVO().getTurmaAgrupada()) {
				getTurmaVO().setCurso(new CursoVO());
			}
			getUnidadeEnsinoVO().setCodigo(getTurmaVO().getUnidadeEnsino().getCodigo());
			setListaSelectItemDisciplina(new ArrayList<SelectItem>(0));
			montarListaSelectItemDisciplinaTurma();
			verificarTurmaAgrupadaDefinicaoLayoutApresentar();
			if (getTurmaVO().getCurso().getNivelEducacionalPosGraduacao() || getTurmaAgrupadaCursoPos()) {
				tipoLayout = "EspelhoDiarioModRetratoRel";
			}else if (getTurmaVO().getCurso().getNivelEducacional().equals("MT")){
				tipoLayout = "EspelhoDiarioModRetratoRel";
			} else {
				tipoLayout = "EspelhoDiarioRel";
			}
			verificarLayoutPadrao();
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setTurmaVO(new TurmaVO());
			setListaSelectItemDisciplina(new ArrayList<SelectItem>(0));
			setListaSelectItemProfessor(new ArrayList<SelectItem>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void montarTurmaProfessor() throws Exception {
		try {

			if (!getTurmaVO().getIdentificadorTurma().equals("")) {
				setTurmaVO(getFacadeFactory().getTurmaFacade().consultarTurmaPorIdentificadorTurma(getTurmaVO().getIdentificadorTurma(), getUnidadeEnsinoLogado().getCodigo().intValue(), false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado()));
			} else {
				throw new Exception("Informe a Turma.");
			}
			setListaSelectItemDisciplina(new ArrayList<SelectItem>(0));
			setProfessor(getUsuarioLogado().getPessoa().getCodigo());
			montarListaSelectItemDisciplinaTurma();
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setTurmaVO(new TurmaVO());
			setListaSelectItemDisciplina(new ArrayList<SelectItem>(0));
			setListaSelectItemProfessor(new ArrayList<SelectItem>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<DisciplinaVO> consultarDisciplinaTurmaAgrupada() throws Exception {
		return getFacadeFactory().getDisciplinaFacade().consultarDisciplinaTurmaAgrupada(getTurmaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
	}

	public void montarDadosProfessorApartirHorarioTurma() throws Exception {
		List<HorarioTurmaVO> objs = getFacadeFactory().getHorarioTurmaFacade().consultarPorCodigoTurma(getTurmaVO().getCodigo().intValue(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
		int tam = objs.size();
		if (tam > 0) {
			setListaProfessor(new ArrayList<PessoaVO>(0));
			HorarioTurmaVO obj = (HorarioTurmaVO) objs.get(0);
			// Integer codigoProfessor =
			// getFacadeFactory().getHorarioTurmaFacade().consultarProfessorLecionaDisciplina(obj,getEspelhoRel().getDisciplina(),
			// TipoHorarioTurma.SEMANAL);
			// if (codigoProfessor.intValue() == 0) {
			List<Integer> professores = getFacadeFactory().getHorarioTurmaFacade().consultarProfessorLecionaDisciplina(obj, getDisciplina());// }
			for (Integer codigoProfessor : professores) {
				adicionarProfessor(codigoProfessor);
				// montarListaSelectItemProfessor();
			}
		}
	}

	public void montarProfessor() throws Exception {
		setListaSelectItemProfessor(new ArrayList<SelectItem>(0));
		if (!getTurmaVO().getIdentificadorTurma().equals("") && getDisciplina() != null && !getDisciplina().equals(0)) {
			HorarioTurmaVO h = getFacadeFactory().getHorarioTurmaFacade().consultarPorHorarioTurmaPorIdentificadorTurmaTurma(getTurmaVO().getIdentificadorTurma(), getUnidadeEnsinoLogado().getCodigo().intValue(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			if (h == null || h.getCodigo().intValue() == 0) {
				throw new Exception("Não existe uma programação de aula registrada para essa turma! Registre a programação de aula e tente novamente emitir o espelho do diário.");
			}
			// Integer codProfSemana =
			// getFacadeFactory().getHorarioTurmaFacade().consultarProfessorLecionaDisciplina(h,getEspelhoRel().getDisciplina(),
			// TipoHorarioTurma.SEMANAL);
			List<Integer> codProfessoresDia = getFacadeFactory().getHorarioTurmaFacade().consultarProfessorLecionaDisciplina(h, getDisciplina());
			// if (codProfSemana != null && codProfSemana.intValue() != 0) {
			// espelhoRel.setProfessor(codProfSemana);
			// }
			if (!codProfessoresDia.isEmpty()) {
				setProfessor(codProfessoresDia.get(0));
			}
			setMensagemDetalhada("");
		}
	}

	public void adicionarProfessor(Integer codigoProfessor) throws Exception {
		Iterator<PessoaVO> i = getListaProfessor().iterator();
		while (i.hasNext()) {
			PessoaVO professorExistente = i.next();
			if (professorExistente.getCodigo().equals(codigoProfessor)) {
				return;
			}
		}
		PessoaVO professor = getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(codigoProfessor, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		getListaProfessor().add(professor);
	}

	@SuppressWarnings("unchecked")
	public List<RegistroAulaVO> consultarListaRegistroAula() throws Exception {
		String semestrePrm = "";
		String anoPrm = "";
		if (getTurmaVO().getAnual()) {
			anoPrm = getAno();
		}
		if (getTurmaVO().getSemestral()) {
			semestrePrm = getSemestre();
		}
		return getFacadeFactory().getRegistroAulaFacade().consultarPorCodigoDisciplinaCodigoTurma(getDisciplina(), getTurmaVO().getCodigo(), semestrePrm, anoPrm, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
	}

	public ProfessorTitularDisciplinaTurmaVO consultarProfessorTitularTurma(Integer disciplina) throws Exception {
		String semestrePrm = "";
		String anoPrm = "";
		if (getTurmaVO().getAnual()) {
			anoPrm = getAno();
			semestrePrm = "";
		}
		if (getTurmaVO().getSemestral()) {
			semestrePrm = getSemestre();
			anoPrm = getAno();
		}
		// ProfessorMinistrouAulaTurmaVO p = new
		// ProfessorMinistrouAulaTurmaVO();
		ProfessorTitularDisciplinaTurmaVO p = new ProfessorTitularDisciplinaTurmaVO();
		p.setAno(anoPrm);
		p.setSemestre(semestrePrm);
		p.setTurma(getTurmaVO());
		p.getDisciplina().setCodigo(disciplina);
		// p =
		// getFacadeFactory().getProfessorMinistrouAulaTurmaFacade().montarProfessoresMinistrouAulaTurmaTitular(p,
		// Uteis.NIVELMONTARDADOS_DADOSBASICOS);
		// p =
		// getFacadeFactory().getProfessorTitularDisciplinaTurmaFacade().montarProfessoresTitularDisciplinaTurmaTitular(p,
		// Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
		p = getFacadeFactory().getProfessorTitularDisciplinaTurmaFacade().montarProfessoresTitularDisciplinaTurmaAgrupadaTitular(p, getLiberarRegistroAulaEntrePeriodo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, "", getUsuarioLogado());
		return p;
	}

	// public ProfessorMinistrouAulaTurmaVO
	// consultarProfessorTitularTurma(Integer disciplina) throws Exception {
	// String semestrePrm = "";
	// String anoPrm = "";
	// if (this.getEspelhoRel().getTurmaVO().getAnual()) {
	// anoPrm = getEspelhoRel().getAno();
	// semestrePrm = "";
	// }
	// if (this.getEspelhoRel().getTurmaVO().getSemestral()) {
	// semestrePrm = getEspelhoRel().getSemestre();
	// anoPrm = getEspelhoRel().getAno();
	// }
	// // ProfessorMinistrouAulaTurmaVO p = new ProfessorMinistrouAulaTurmaVO();
	// ProfessorTitularDisciplinaTurmaVO p = new
	// ProfessorTitularDisciplinaTurmaVO();
	// p.setAno(anoPrm);
	// p.setSemestre(semestrePrm);
	// p.setTurma(getEspelhoRel().getTurmaVO());
	// p.getDisciplina().setCodigo(disciplina);
	// // p =
	// getFacadeFactory().getProfessorMinistrouAulaTurmaFacade().montarProfessoresMinistrouAulaTurmaTitular(p,
	// Uteis.NIVELMONTARDADOS_DADOSBASICOS);
	// p =
	// getFacadeFactory().getProfessorTitularDisciplinaTurmaFacade().montarProfessoresTitularDisciplinaTurmaTitular(p,
	// disciplina);
	// return p;
	// }

	// public void montarListaSelectItemProfessor() {
	// try {
	// setMensagemDetalhada("", "");
	// setListaSelectItemProfessor(new ArrayList(0));
	// Iterator i = getListaProfessor().iterator();
	// while (i.hasNext()) {
	// PessoaVO professor = (PessoaVO) i.next();
	// getListaSelectItemProfessor().add(new SelectItem(professor.getCodigo(),
	// professor.getNome()));
	// }
	// setMensagemID("msg_dados_consultados");
	// } catch (Exception e) {
	// // System.out.println("MENSAGEM => " + e.getMessage());;
	// }
	// }
	public void limparDadosMemoria() {
		removerObjetoMemoria(this);
		inicializarListasSelectItemTodosComboBox();
		executarInicializacaoFiltroRelatorioAcademicoDefault();
	}

	public void limparDadosMemoriaVisaoProfessor() {
		removerObjetoMemoria(this);
		inicializarListasSelectItemTodosComboBox();
		montarListaSelectItemTurmaVisaoProfessor();
	}

	public void inicializarListasSelectItemTodosComboBox() {
		montarListaSelectItemUnidadeEnsino();
	}

	public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
		List<UnidadeEnsinoVO> resultadoConsulta = null;
		Iterator<UnidadeEnsinoVO> i = null;
		try {
			resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
			i = resultadoConsulta.iterator();
			getListaSelectItemUnidadeEnsino().clear();
			if (super.getUnidadeEnsinoLogado().getCodigo().equals(0)) {
				getListaSelectItemUnidadeEnsino().add(new SelectItem(0, ""));
			}
			while (i.hasNext()) {
				UnidadeEnsinoVO obj = i.next();
				getListaSelectItemUnidadeEnsino().add(new SelectItem(obj.getCodigo(), obj.getNome()));
				removerObjetoMemoria(obj);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	public void limparDados() {
		setTurmaVO(null);
		setSemestre(null);
		setAno(null);
		setDisciplina(null);
		setProfessor(null);
	}

	public List<SelectItem> getListaSelectItemMes() {
		List<SelectItem> listaSelectItemMes = new ArrayList<SelectItem>(0);
		listaSelectItemMes.add(new SelectItem("", ""));
		if (getTurmaVO().getCurso() != null && !getTurmaVO().getCurso().getCodigo().equals(0)) {
			if (getTurmaVO().getCurso().getPeriodicidade().equals("AN") || getTurmaVO().getCurso().getPeriodicidade().equals("IN")) {
				for (MesAnoEnum mesAnoEnum : MesAnoEnum.values()) {
					listaSelectItemMes.add(new SelectItem(mesAnoEnum.getKey(), mesAnoEnum.getMes()));
				}
			}
			if (getTurmaVO().getCurso().getPeriodicidade().equals("SE")) {
				for (MesAnoEnum mesAnoEnum : MesAnoEnum.values()) {
					if (Integer.valueOf(mesAnoEnum.getKey()) >= 1 && Integer.valueOf(mesAnoEnum.getKey()) <= 7 && getSemestre().equals("1")) {
						listaSelectItemMes.add(new SelectItem(mesAnoEnum.getKey(), mesAnoEnum.getMes()));
					}
					if (Integer.valueOf(mesAnoEnum.getKey()) >= 7 && Integer.valueOf(mesAnoEnum.getKey()) <= 12 && getSemestre().equals("2")) {
						listaSelectItemMes.add(new SelectItem(mesAnoEnum.getKey(), mesAnoEnum.getMes()));
					}
				}
			}
		}
		return listaSelectItemMes;
	}

	public boolean getMostrarTurmaAgrupadaSemestre() {
		return getTurmaVO().getTurmaAgrupada() && getTurmaVO().getSemestral();
	}

	public boolean getMostrarTurmaAgrupadaAno() {
		return getTurmaVO().getTurmaAgrupada() && getTurmaVO().getAnual();
	}

	public boolean getMostrarSemestre() {
		return getTurmaVO().getSemestral();
	}

	public boolean getMostrarSemestreSemAno() {
		return getTurmaVO().getSemestral() && !getTurmaVO().getAnual();
	}

	public boolean getMostrarAnoSemestre() {
		return getTurmaVO().getSemestral() || getTurmaVO().getAnual();
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>UnidadeEnsino</code>. Buscando todos os objetos correspondentes a
	 * entidade <code>UnidadeEnsino</code>. Esta rotina não recebe parâmetros
	 * para filtragem de dados, isto é importante para a inicialização dos dados
	 * da tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemUnidadeEnsino() {
		try {
			montarListaSelectItemUnidadeEnsino("");
		} catch (Exception e) {
		}
	}

	public List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
		return getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
	}

	public List<SelectItem> getTipoFiltroComboAluno() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("todos", "Todos"));
		itens.add(new SelectItem("NO", "CONTRATO PÓS"));
		itens.add(new SelectItem("EX", "CONTRATO EXTENSÃO"));
		itens.add(new SelectItem("MO", "CONTRATO ESPECIAL"));
		return itens;
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

	public List<SelectItem> getListaSelectItemDisciplina() {
		if (listaSelectItemDisciplina == null) {
			listaSelectItemDisciplina = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemDisciplina;
	}

	public void setListaSelectItemDisciplina(List<SelectItem> listaSelectItemDisciplina) {
		this.listaSelectItemDisciplina = listaSelectItemDisciplina;
	}

	public String getValorConsultaTurma() {
		return valorConsultaTurma;
	}

	public void setValorConsultaTurma(String valorConsultaTurma) {
		this.valorConsultaTurma = valorConsultaTurma;
	}

	public List<SelectItem> getListaSelectItemProfessor() {
		if (listaSelectItemProfessor == null) {
			listaSelectItemProfessor = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemProfessor;
	}

	public void setListaSelectItemProfessor(List<SelectItem> listaSelectItemProfessor) {
		this.listaSelectItemProfessor = listaSelectItemProfessor;
	}

	public List<PessoaVO> getListaProfessor() {
		if (listaProfessor == null) {
			listaProfessor = new ArrayList<PessoaVO>(0);
		}
		return listaProfessor;
	}

	public void setListaProfessor(List<PessoaVO> listaProfessor) {
		this.listaProfessor = listaProfessor;
	}

	public Boolean getExisteUnidadeEnsino() {
		if (existeUnidadeEnsino == null) {
			existeUnidadeEnsino = false;
		}
		return existeUnidadeEnsino;
	}

	public void setExisteUnidadeEnsino(Boolean existeUnidadeEnsino) {
		this.existeUnidadeEnsino = existeUnidadeEnsino;
	}

	private Boolean isApresentarAnoVisaoProfessorCoordenador;
	
	public boolean getIsApresentarAnoVisaoProfessorCoordenador() {
		if (isApresentarAnoVisaoProfessorCoordenador == null) {
			isApresentarAnoVisaoProfessorCoordenador = true;
		}
		return isApresentarAnoVisaoProfessorCoordenador;
	}
	
	private void processarApresentarAno(){
		if (getUsuarioLogado().getVisaoLogar().equals("professor") || getUsuarioLogado().getVisaoLogar().equals("coordenador")) {
			if (getLoginControle().getPermissaoAcessoMenuVO().getPermitirRegistrarAulaRetroativo()) {
				if (Uteis.isAtributoPreenchido(getTurmaVO())) {
					if (getTurmaVO().getAnual() || getTurmaVO().getSemestral()) {
						if (!Uteis.isAtributoPreenchido(getAno())) {
							setAno(Uteis.getAnoDataAtual4Digitos());
						}
						isApresentarAnoVisaoProfessorCoordenador = true;
						return;
					}
					setAno("");
					isApresentarAnoVisaoProfessorCoordenador = false;
					return;
				}
				isApresentarAnoVisaoProfessorCoordenador = true;
				return;
			} else {
				if (!getTurmaVO().getCodigo().equals(0)) {
					if (getTurmaVO().getAnual() || getTurmaVO().getSemestral()) {
						setAno(Uteis.getAnoDataAtual4Digitos());
					} else {
						setAno("");
					}
				}
				isApresentarAnoVisaoProfessorCoordenador = false;
				return;
			}
		}
		isApresentarAnoVisaoProfessorCoordenador = true;
	}
	
	public Boolean isApresentarSemestreVisaoProfessorCoordenador;
	
	public boolean getIsApresentarSemestreVisaoProfessorCoordenador() {
		if (isApresentarSemestreVisaoProfessorCoordenador == null) {
			isApresentarSemestreVisaoProfessorCoordenador = true;
		}
		return isApresentarSemestreVisaoProfessorCoordenador;
	}		
		
	private void processarApresentarSemestre() {
		if (getUsuarioLogado().getVisaoLogar().equals("professor") || getUsuarioLogado().getVisaoLogar().equals("coordenador")) {
			if (getLoginControle().getPermissaoAcessoMenuVO().getPermitirRegistrarAulaRetroativo()) {
				if (Uteis.isAtributoPreenchido(getTurmaVO())) {
					if (getTurmaVO().getSemestral()) {
						if (!Uteis.isAtributoPreenchido(getSemestre())) {
							setSemestre(Uteis.getSemestreAtual());
						}
						isApresentarSemestreVisaoProfessorCoordenador = true;
						return;
					}
					setSemestre("");
					isApresentarSemestreVisaoProfessorCoordenador = false;
					return;
				}
				isApresentarSemestreVisaoProfessorCoordenador = true;
				return;
			} else {
				if (!getTurmaVO().getCodigo().equals(0)) {
					if (getTurmaVO().getSemestral()) {
						setSemestre(Uteis.getSemestreAtual());
					} else {
						setSemestre("");
					}
				}
				isApresentarSemestreVisaoProfessorCoordenador = false;
				return;
			}
		}
		isApresentarSemestreVisaoProfessorCoordenador = true;
	}

	public List<DisciplinaVO> getListaConsultasDisciplinas() {
		if (listaConsultasDisciplinas == null) {
			listaConsultasDisciplinas = new ArrayList<DisciplinaVO>(0);
		}
		return listaConsultasDisciplinas;
	}

	public void setListaConsultasDisciplinas(List<DisciplinaVO> listaConsultasDisciplinas) {
		this.listaConsultasDisciplinas = listaConsultasDisciplinas;
	}

	public String getAno() {
		if (ano == null) {
			if(getLoginControle().getPermissaoAcessoMenuVO().getPermitirRegistrarAulaRetroativo() && getUsuarioLogado().getIsApresentarVisaoProfessor()) {
				ano = getVisaoProfessorControle().getAno();
			}else if(getLoginControle().getPermissaoAcessoMenuVO().getPermitirRegistrarAulaRetroativo() && getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
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

	public Integer getDisciplina() {
		if (disciplina == null) {
			disciplina = 0;
		}
		return disciplina;
	}

	public void setDisciplina(Integer disciplina) {
		this.disciplina = disciplina;
	}

	public Integer getProfessor() {
		if (professor == null) {
			professor = 0;
		}
		return professor;
	}

	public void setProfessor(Integer professor) {
		this.professor = professor;
	}

	public String getSemestre() {
		if (semestre == null) {
			if(getLoginControle().getPermissaoAcessoMenuVO().getPermitirRegistrarAulaRetroativo() && getUsuarioLogado().getIsApresentarVisaoProfessor()) {
				semestre = getVisaoProfessorControle().getSemestre();
			}else if(getLoginControle().getPermissaoAcessoMenuVO().getPermitirRegistrarAulaRetroativo() && getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
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

	public TurmaVO getTurmaVO() {
		if (turmaVO == null) {
			turmaVO = new TurmaVO();
		}
		return turmaVO;
	}

	public void setTurmaVO(TurmaVO turmaVO) {
		this.turmaVO = turmaVO;
	}

	public void setTipoLayout(String tipoLayout) {
		this.tipoLayout = tipoLayout;
	}

	public String getTipoLayout() {
		if (tipoLayout == null) {
			tipoLayout = "";
		}
		return tipoLayout;
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

	public boolean getIsApresentarDadosAposSelecionarTurma() {
		return getTurmaVO().getCodigo() != 0;
	}

	public String getFiltroTipoCursoAluno() {
		if (filtroTipoCursoAluno == null) {
			filtroTipoCursoAluno = "";
		}
		return filtroTipoCursoAluno;
	}

	public void setFiltroTipoCursoAluno(String filtroTipoCursoAluno) {
		this.filtroTipoCursoAluno = filtroTipoCursoAluno;
	}

	public boolean getIsApresentarFiltro() {
		if (getTurmaVO().getCurso().getNivelEducacionalPosGraduacao()) {
			return true;
		}
		return false;
	}

	public String getTipoAluno() {
		if (tipoAluno == null) {
			tipoAluno = "todos";
		}
		return tipoAluno;
	}

	public void setTipoAluno(String tipoAluno) {
		this.tipoAluno = tipoAluno;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public Boolean getTurmaAgrupadaCursoPos() {
		if (turmaAgrupadaCursoPos == null) {
			turmaAgrupadaCursoPos = Boolean.FALSE;
		}
		return turmaAgrupadaCursoPos;
	}

	public void setTurmaAgrupadaCursoPos(Boolean turmaAgrupadaCursoPos) {
		this.turmaAgrupadaCursoPos = turmaAgrupadaCursoPos;
	}

	public Boolean getLiberarRegistroAulaEntrePeriodo() {
		if (liberarRegistroAulaEntrePeriodo == null) {
			liberarRegistroAulaEntrePeriodo = Boolean.FALSE;
		}
		return liberarRegistroAulaEntrePeriodo;
	}

	public void setLiberarRegistroAulaEntrePeriodo(Boolean liberarRegistroAulaEntrePeriodo) {
		this.liberarRegistroAulaEntrePeriodo = liberarRegistroAulaEntrePeriodo;
	}

	public String getMes() {
		if (mes == null) {
			mes = "";
		}
		return mes;
	}

	public void setMes(String mes) {
		this.mes = mes;
	}

	public String getAnoMes() {
		if (anoMes == null) {
			anoMes = "";
		}
		return anoMes;
	}

	public void setAnoMes(String anoMes) {
		this.anoMes = anoMes;
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

	public boolean getIsApresentarPeriodo() {
		return getTurmaVO().getCurso().getNivelEducacionalPosGraduacao() || getTipoLayout().equals("EspelhoDiarioRel2");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void montarListaSelectItemTurmaVisaoProfessor() {
		List<TurmaVO> listaResultado = null;
		Iterator<TurmaVO> i = null;
		Map<Integer, String> hashTurmasAdicionadas = new HashMap<Integer, String>(0);
		try {
			getListaSelectItemTurma().clear();
			getListaSelectItemTurma().add(new SelectItem(0, ""));
			listaResultado = consultarTurmaPorProfessor();
			i = listaResultado.iterator();
			String value = "";
			while (i.hasNext()) {
				TurmaVO turma = i.next();
				if (!getUsuarioLogado().getVisaoLogar().equals("professor")) {
					if (!hashTurmasAdicionadas.containsKey(turma.getCodigo())) {
						if (turma.getTurmaAgrupada()) {
							value = turma.getIdentificadorTurma() + " - Turno " + turma.getTurno().getNome();
						} else {
							value = turma.getIdentificadorTurma() + " - Curso " + turma.getCurso().getNome() + " - Turno " + turma.getTurno().getNome();
						}
						getListaSelectItemTurma().add(new SelectItem(turma.getCodigo(), value));
						hashTurmasAdicionadas.put(turma.getCodigo(), turma.getIdentificadorTurma());
					}
				} else {
					if (turma.getTurmaAgrupada() && !turma.getSubturma()) {
						value = turma.getIdentificadorTurma() + " - Turno " + turma.getTurno().getNome();
						getListaSelectItemTurma().add(new SelectItem(turma.getCodigo(), value));
						List<TurmaVO> listaTurmasAgrupadas = getFacadeFactory().getTurmaFacade().consultaRapidaTurmasNasQuaisTurmaParticipaDeAgrupamento(turma.getCodigo(), false, getUsuarioLogado());
						for (TurmaVO turmaParticipaAgrupamento : listaTurmasAgrupadas) {
							if (!hashTurmasAdicionadas.containsKey(turmaParticipaAgrupamento.getCodigo())) {
								value = turmaParticipaAgrupamento.getIdentificadorTurma() + " - Curso " + turmaParticipaAgrupamento.getCurso().getNome() + " - Turno " + turmaParticipaAgrupamento.getTurno().getNome();
								getListaSelectItemTurma().add(new SelectItem(turmaParticipaAgrupamento.getCodigo(), value));
								hashTurmasAdicionadas.put(turmaParticipaAgrupamento.getCodigo(), turmaParticipaAgrupamento.getIdentificadorTurma());
							}
						}
					} else {
						if (!hashTurmasAdicionadas.containsKey(turma.getCodigo())) {
							value = turma.getIdentificadorTurma() + " - Curso " + turma.getCurso().getNome() + " - Turno " + turma.getTurno().getNome();
							getListaSelectItemTurma().add(new SelectItem(turma.getCodigo(), value));
							hashTurmasAdicionadas.put(turma.getCodigo(), turma.getIdentificadorTurma());
						}
					}
				}
			}
			SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
			Collections.sort((List) getListaSelectItemTurma(), ordenador);
		} catch (Exception e) {
		} finally {
			Uteis.liberarListaMemoria(listaResultado);
			hashTurmasAdicionadas = null;
			i = null;
		}
	}
	


	public List<SelectItem> getListaSelectItemTurma() {
		if (listaSelectItemTurma == null) {
			listaSelectItemTurma = new ArrayList<SelectItem>();
		}
		return listaSelectItemTurma;
	}

	public void setListaSelectItemTurma(List<SelectItem> listaSelectItemTurma) {
		this.listaSelectItemTurma = listaSelectItemTurma;
	}

	public Date getDataInicioPeriodoMatricula() {
		return dataInicioPeriodoMatricula;
	}

	public void setDataInicioPeriodoMatricula(Date dataInicioPeriodoMatricula) {
		this.dataInicioPeriodoMatricula = dataInicioPeriodoMatricula;
	}

	public Date getDataFimPeirodoMatricula() {
		return dataFimPeirodoMatricula;
	}

	public void setDataFimPeirodoMatricula(Date dataFimPeirodoMatricula) {
		this.dataFimPeirodoMatricula = dataFimPeirodoMatricula;
	}

	public Boolean getApresentarDataMatricula() {
		if (apresentarDataMatricula == null) {
			apresentarDataMatricula = false;
		}
		return apresentarDataMatricula;
	}

	public void setApresentarDataMatricula(Boolean apresentarDataMatricula) {
		this.apresentarDataMatricula = apresentarDataMatricula;
	}

	public boolean getTrazerAlunoPendenteFinanceiramente() throws Exception {
		return getFacadeFactory().getConfiguracaoGeralSistemaFacade().executarVerificacaoApresentarAlunoPendenteFinanceiramente(getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado());
	}

	/**
	 * @return the apenasAlunosAtivos
	 */
	public Boolean getApenasAlunosAtivos() {
		if (apenasAlunosAtivos == null) {
			apenasAlunosAtivos = true;
		}
		return apenasAlunosAtivos;
	}

	/**
	 * @param apenasAlunosAtivos the apenasAlunosAtivos to set
	 */
	public void setApenasAlunosAtivos(Boolean apenasAlunosAtivos) {
		this.apenasAlunosAtivos = apenasAlunosAtivos;
	}
	
	@PostConstruct
	public void executarInicializacaoFiltroRelatorioAcademicoDefault() {
		getFiltroRelatorioAcademicoVO().setAtivo(true);
		getFiltroRelatorioAcademicoVO().setConcluido(true);
		getFiltroRelatorioAcademicoVO().setFormado(true);
		if(getUsuarioLogado().getIsApresentarVisaoProfessor()) {
			montarListaSelectItemTurmaVisaoProfessor();
		}else if(getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
			montarListaSelectItemTurmaVisaoCoordenador();
		}
	}

	public Boolean getImprimirBackground() {
		if (imprimirBackground == null) {
			imprimirBackground = false;
		}
		return imprimirBackground;
	}

	public void setImprimirBackground(Boolean imprimirBackground) {
		this.imprimirBackground = imprimirBackground;
	}


	public boolean getApresentarLayoutEtiqueta(){
    	return getTipoLayout().equals("LayoutImpressaoEtiqueta");
    }
	
	public void realizarImpressaoEtiqueta(){		
		try{
			setFazerDownload(false);
			setCaminhoRelatorio(getFacadeFactory().getDiarioRelFacade().realizarImpressaoEtiqueta(getTurmaVO(), getDisciplina(), getAno(), getSemestre(), 
					getLayoutEtiquetaVO(), getTipoLayout(), getNumeroCopias(), getLinha(), getColuna(), getRemoverEspacoTAGVazia(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado()));
			setFazerDownload(true);
			removerObjetoMemoria(this);
			inicializarListasSelectItemTodosComboBox();
			verificarLayoutPadrao();
			limparMensagem();
			setMensagemID("msg_relatorio_ok");
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
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
	
	
	public boolean isPermitirRealizarLancamentoAlunosPreMatriculados() {
		return permitirRealizarLancamentoAlunosPreMatriculados;
	}

	public void setPermitirRealizarLancamentoAlunosPreMatriculados(boolean permitirRealizarLancamentoAlunosPreMatriculados) {
		this.permitirRealizarLancamentoAlunosPreMatriculados = permitirRealizarLancamentoAlunosPreMatriculados;
	}
	
	public List<SelectItem> getListaSelectItemProfessorDisciplinaTurmaVOs() {
		if (listaSelectItemProfessorDisciplinaTurmaVOs == null) {
			listaSelectItemProfessorDisciplinaTurmaVOs = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemProfessorDisciplinaTurmaVOs;
	}

	public void setListaSelectItemProfessorDisciplinaTurmaVOs(List<SelectItem> listaSelectItemProfessorDisciplinaTurmaVOs) {
		this.listaSelectItemProfessorDisciplinaTurmaVOs = listaSelectItemProfessorDisciplinaTurmaVOs;
	}
	
	public void consultarProfessoresLecionamTurmaDisciplina() {
		consultarProfessorDisciplinaTurma();
	}
	
	public List<ProfessorTitularDisciplinaTurmaVO> consultarProfessorDisciplinaTurma() {
		List<ProfessorTitularDisciplinaTurmaVO> listaProfessorDisciplinaVOs = new ArrayList<ProfessorTitularDisciplinaTurmaVO>(0);
		if (!getDisciplina().equals(0)) {
			try {
				listaProfessorDisciplinaVOs = getFacadeFactory().getProfessorTitularDisciplinaTurmaFacade().consultarProfessoresDisciplinaTurma(getTurmaVO(), getDisciplina(), getAno(), getSemestre(), false, getTurmaVO().getCurso().getLiberarRegistroAulaEntrePeriodo(), "", Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				montarListaSelectItemProfessorTitularDisciplinaTurma(listaProfessorDisciplinaVOs);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			}
		} else {
			setProfessorDisciplinaTurmaVO(null);
			getMapProfessorDisciplinaTurmaVOs().clear();
			setApresentarComboBoxProfessorTurmaDisciplina(false);
		}
		return listaProfessorDisciplinaVOs;
	}
	
	public void montarListaSelectItemProfessorTitularDisciplinaTurma(List<ProfessorTitularDisciplinaTurmaVO> listaProfessorDisciplinaVOs) {
		getMapProfessorDisciplinaTurmaVOs().clear();
		if (listaProfessorDisciplinaVOs.size() > 1) {
			List<SelectItem> itens = new ArrayList<SelectItem>(0); 
			setApresentarComboBoxProfessorTurmaDisciplina(true);
			for (ProfessorTitularDisciplinaTurmaVO professorTitularDisciplinaTurmaVO : listaProfessorDisciplinaVOs) {
				if (!getMapProfessorDisciplinaTurmaVOs().containsKey(professorTitularDisciplinaTurmaVO.getProfessor().getCodigo())) {
					professorTitularDisciplinaTurmaVO.setIgnorarTitularidadeProfessor(true);
					getMapProfessorDisciplinaTurmaVOs().put(professorTitularDisciplinaTurmaVO.getProfessor().getCodigo(), professorTitularDisciplinaTurmaVO);
				}
				itens.add(new SelectItem(professorTitularDisciplinaTurmaVO.getProfessor().getCodigo(), professorTitularDisciplinaTurmaVO.getProfessor().getPessoa().getNome()));
			}
			setListaSelectItemProfessorDisciplinaTurmaVOs(itens);
		} else {
			setApresentarComboBoxProfessorTurmaDisciplina(false);
		}
	}
	
	public ProfessorTitularDisciplinaTurmaVO getProfessorDisciplinaTurmaVO() {
		if (professorDisciplinaTurmaVO == null) {
			professorDisciplinaTurmaVO = new ProfessorTitularDisciplinaTurmaVO();
		}
		return professorDisciplinaTurmaVO;
	}

	public void setProfessorDisciplinaTurmaVO(ProfessorTitularDisciplinaTurmaVO professorDisciplinaTurmaVO) {
		this.professorDisciplinaTurmaVO = professorDisciplinaTurmaVO;
	}

	public Map<Integer, ProfessorTitularDisciplinaTurmaVO> getMapProfessorDisciplinaTurmaVOs() {
		if (mapProfessorDisciplinaTurmaVOs == null) {
			mapProfessorDisciplinaTurmaVOs = new HashMap<Integer, ProfessorTitularDisciplinaTurmaVO>(0);
		}
		return mapProfessorDisciplinaTurmaVOs;
	}

	public void setMapProfessorDisciplinaTurmaVOs(Map<Integer, ProfessorTitularDisciplinaTurmaVO> mapProfessorDisciplinaTurmaVOs) {
		this.mapProfessorDisciplinaTurmaVOs = mapProfessorDisciplinaTurmaVOs;
	}

	public Boolean getApresentarComboBoxProfessorTurmaDisciplina() {
		if (apresentarComboBoxProfessorTurmaDisciplina == null) {
			apresentarComboBoxProfessorTurmaDisciplina = false;
		}
		return apresentarComboBoxProfessorTurmaDisciplina;
	}

	public void setApresentarComboBoxProfessorTurmaDisciplina(Boolean apresentarComboBoxProfessorTurmaDisciplina) {
		this.apresentarComboBoxProfessorTurmaDisciplina = apresentarComboBoxProfessorTurmaDisciplina;
	}

	public String getTituloRelatorio() {
		if (tituloRelatorio == null || tituloRelatorio.equals("")) {
			if (getTipoLayout().equals("EspelhoDiarioReposicaoRel")) {
				tituloRelatorio = "Diário de Reposição";
			} else if (getTipoLayout().equals("EspelhoDiarioControleNotaFrequenciaRel")) {
				tituloRelatorio = "Controle de Notas e Frequência (CNF)";
			} else {
				tituloRelatorio = "Espelho do Diário da Turma";
			}
		}
		return tituloRelatorio;
	}

	public void setTituloRelatorio(String tituloRelatorio) {
		this.tituloRelatorio = tituloRelatorio;
	}

}
