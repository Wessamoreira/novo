package relatorio.controle.academico;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SelectItemOrdemValor;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaCompostaVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.HorarioTurmaDisciplinaProgramadaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilSelectItem;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.academico.AlunosPorDisciplinasRel;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;

@SuppressWarnings("unchecked")
@Controller("AlunosPorDisciplinasRelControle")
@Scope("viewScope")
@Lazy
public class AlunosPorDisciplinasRelControle extends SuperControleRelatorio {

	private UnidadeEnsinoVO unidadeEnsinoVO;
	private UnidadeEnsinoCursoVO unidadeEnsinoCursoVO;
	private CursoVO cursoVO;
	private String campoConsultaCurso;
	private String valorConsultaCurso;
	private List listaConsultaCurso;
	private TurmaVO turmaVO;
	private String ano;
	private String semestre;
	private String situacao;
	private List listaSelectItemUnidadeEnsino;
	private List listaSelectItemCurso;
	private String layout;
	private String campoConsultaTurma;
	private String valorConsultaTurma;
	private List listaConsultaTurma;
	private DisciplinaVO disciplina;
	private List listaSelectItemDisciplina;
	private String filtrarPor;
	private List<SelectItem> listaSelectItemTurno;
	private List<SelectItem> listaSelectItemGradeCurricular;
	private FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO;
	private Integer gradeCurricular;
	private Integer turno;
	private Boolean trazerAlunoTransferencia; 
	private Boolean marcarTodosTodasSituacoes;
	private String unidadeEnsinoApresentar;
	private String cursoApresentar;
	private PeriodicidadeEnum periodicidade;
	private List<SelectItem> periodicidadeEnums;

	public AlunosPorDisciplinasRelControle() throws Exception {
		incializarDados();
		setMensagemID("msg_entre_prmconsulta");
	}

	public void incializarDados() {
//		montarListaSelectItemUnidadeEnsino();
		getFiltroRelatorioAcademicoVO().setAtivo(true);
		getFiltroRelatorioAcademicoVO().setPreMatricula(true);
		getFiltroRelatorioAcademicoVO().setConcluido(true);
	}

	public void imprimirPDF() {
		String titulo = "ALUNOS POR DISCIPLINAS";
		String nomeEntidade = super.getUnidadeEnsinoLogado().getNome();
		String design = "";
		if (getLayout().equals("layout1")) {
			design = AlunosPorDisciplinasRel.getDesignIReportRelatorio();
		} else {
			design = AlunosPorDisciplinasRel.getDesignIReportRelatorioLyout();
		}
		List listaObjs = null;
		try {
//			AlunosPorDisciplinasRel.
			getFacadeFactory().getAlunosPorDisciplinasRelFacade().validarDados(getUnidadeEnsinoVOs(), getCursoVOs());
			registrarAtividadeUsuario(getUsuarioLogado(), "AlunosPorDisciplinasRelControle", "Inicializando Geração de Relatório Alunos Por Disciplinas", "Emitindo Relatório");
			if (getDisciplina() != null && getDisciplina().getCodigo() != 0) {
				setDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(disciplina.getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			}
			setUnidadeEnsinoCursoVO(getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorCursoUnidadeEnsino(getUnidadeEnsinoCursoVO().getCurso().getCodigo(), getUnidadeEnsinoCursoVO().getUnidadeEnsino(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			listaObjs = getFacadeFactory().getAlunosPorDisciplinasRelFacade().criarObjeto(getUnidadeEnsinoVOs(), getCursoVOs(), getAno(), getSemestre(), getFiltroRelatorioAcademicoVO(), getLayout(), getTurmaVO(), getDisciplina(), getTurno(), getGradeCurricular(), getUsuarioLogado(), getTrazerAlunoTransferencia());
			apresentarRelatorioObjetos(AlunosPorDisciplinasRel.getIdEntidade(), titulo, nomeEntidade, "", "PDF", "", design, getUsuarioLogado().getNome(), "", listaObjs, AlunosPorDisciplinasRel.getCaminhoBaseRelatorio());
			removerObjetoMemoria(this);
			incializarDados();
			if (!listaObjs.isEmpty()) {
				setMensagemID("msg_relatorio_ok");
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}
			registrarAtividadeUsuario(getUsuarioLogado(), "AlunosPorDisciplinasRelControle", "Finalizando Geração de Relatório Alunos Por Disciplinas", "Emitindo Relatório");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			titulo = null;
			nomeEntidade = null;
			design = null;
			Uteis.liberarListaMemoria(listaObjs);
		}
	}

	public void imprimirRelPDF() throws Exception {
		List listaObjs = null;
		try {
			getFacadeFactory().getAlunosPorDisciplinasRelFacade().validarDados(getUnidadeEnsinoVOs(), getCursoVOs());
			registrarAtividadeUsuario(getUsuarioLogado(), "AlunosPorDisciplinasRelControle", "Inicializando Geração de Relatório Alunos Por Disciplinas", "Emitindo Relatório");
//			if (Uteis.isAtributoPreenchido(getCursoVO()) && Uteis.isAtributoPreenchido(getUnidadeEnsinoVO())) {
//				setUnidadeEnsinoCursoVO(getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorCursoUnidadeEnsino(getCursoVO().getCodigo(), getUnidadeEnsinoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
//			}
			listaObjs = getFacadeFactory().getAlunosPorDisciplinasRelFacade().criarObjeto(getUnidadeEnsinoVOs(), getCursoVOs(), getAno(), getSemestre(), getFiltroRelatorioAcademicoVO(), getLayout(), getTurmaVO(), getDisciplina(), getTurno(), getGradeCurricular(), getUsuarioLogado(), getTrazerAlunoTransferencia());
			if (!listaObjs.isEmpty()) {
				getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getAlunosMatriculadosGeralRelFacade().getDesignIReportRelatorio());
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				if (getLayout().equals("layout1")) {
					getSuperParametroRelVO().setNomeDesignIreport(AlunosPorDisciplinasRel.getDesignIReportRelatorio());
				} else {
					getSuperParametroRelVO().setNomeDesignIreport(AlunosPorDisciplinasRel.getDesignIReportRelatorioLyout());
				}
				getSuperParametroRelVO().setSubReport_Dir(AlunosPorDisciplinasRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio("ALUNOS POR DISCIPLINAS");
				getSuperParametroRelVO().setListaObjetos(listaObjs);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(AlunosPorDisciplinasRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				if (Uteis.isAtributoPreenchido(getUnidadeEnsinoVO())) {
					getSuperParametroRelVO().setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()).getNome());
				}
				if (getCursoVO().getCodigo() > 0) {
					CursoVO curso = getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(getCursoVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, false, getUsuarioLogado());
					getSuperParametroRelVO().setCurso(curso.getNome());
				} else {
					getSuperParametroRelVO().setCurso("Todas");
				}
				if (getAno() != null && !getAno().equals("")) {
					getSuperParametroRelVO().setAno(getAno());
				} else {
					getSuperParametroRelVO().setAno("Todos");
				}
				String semestre = "Todas";
				if (getSemestre() != null && !getSemestre().equals("")) {
					if (getSemestre().equals("1")) {
						semestre = "1º";
					} else {
						semestre = "2º";
					}
				}
				getSuperParametroRelVO().setSemestre(semestre);
                		if (!getUnidadeEnsinoVO().getCodigo().equals(0)) {
                		    setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
                		    getSuperParametroRelVO().adicionarLogoUnidadeEnsinoSelecionada(getUnidadeEnsinoVO());
                		}
				realizarImpressaoRelatorio();
				setMensagemID("msg_relatorio_ok");
				removerObjetoMemoria(this);
				incializarDados();
//				verificarTodosCursosSelecionados();
				verificarTodasUnidadesSelecionadas();
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}
			registrarAtividadeUsuario(getUsuarioLogado(), "AlunosPorDisciplinasRelControle", "Finalizando Geração de Relatório Alunos Por Disciplinas", "Emitindo Relatório");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}

	}

	public void imprimirRelExcel() throws Exception {
		List listaObjs = null;
		try {
			getFacadeFactory().getAlunosPorDisciplinasRelFacade().validarDados(getUnidadeEnsinoVOs(), getCursoVOs());
			registrarAtividadeUsuario(getUsuarioLogado(), "AlunosPorDisciplinasRelControle", "Inicializando Geração de Relatório Alunos Por Disciplinas", "Emitindo Relatório");
			if (Uteis.isAtributoPreenchido(getCursoVO()) && Uteis.isAtributoPreenchido(getUnidadeEnsinoVO())) {
				setUnidadeEnsinoCursoVO(getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorCursoUnidadeEnsino(getCursoVO().getCodigo(), getUnidadeEnsinoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			listaObjs = getFacadeFactory().getAlunosPorDisciplinasRelFacade().criarObjeto(getUnidadeEnsinoVOs(), getCursoVOs(), getAno(), getSemestre(), getFiltroRelatorioAcademicoVO(), getLayout(), getTurmaVO(), getDisciplina(), getTurno(), getGradeCurricular(), getUsuarioLogado(), getTrazerAlunoTransferencia());
			if (!listaObjs.isEmpty()) {
				getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getAlunosMatriculadosGeralRelFacade().getDesignIReportRelatorio());
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.EXCEL);
				if (getLayout().equals("layout1")) {
					getSuperParametroRelVO().setNomeDesignIreport(AlunosPorDisciplinasRel.getDesignIReportRelatorioExcel());
				} else {
					getSuperParametroRelVO().setNomeDesignIreport(AlunosPorDisciplinasRel.getDesignIReportRelatorioLayoutExcel());
				}
				getSuperParametroRelVO().setSubReport_Dir(AlunosPorDisciplinasRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio("ALUNOS POR DISCIPLINAS");
				getSuperParametroRelVO().setListaObjetos(listaObjs);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(AlunosPorDisciplinasRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				if (Uteis.isAtributoPreenchido(getUnidadeEnsinoVO())) {
					getSuperParametroRelVO().setUnidadeEnsino((getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())).getNome());
				}
				if (getCursoVO().getCodigo() > 0) {
					CursoVO curso = getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(getCursoVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, false, getUsuarioLogado());
					getSuperParametroRelVO().setCurso(curso.getNome());
				} else {
					getSuperParametroRelVO().setCurso("Todas");
				}
				if (getAno() != null && !getAno().equals("")) {
					getSuperParametroRelVO().setAno(getAno());
				} else {
					getSuperParametroRelVO().setAno("Todos");
				}
				String semestre = "Todas";
				if (getSemestre() != null && !getSemestre().equals("")) {
					if (getSemestre().equals("1")) {
						semestre = "1º";
					} else {
						semestre = "2º";
					}
				}
				getSuperParametroRelVO().setSemestre(semestre);
				// getSuperParametroRelVO().setTipoRelatorio(getLayout());
				realizarImpressaoRelatorio();
				setMensagemID("msg_relatorio_ok");
				removerObjetoMemoria(this);
				incializarDados();
				verificarTodasUnidadesSelecionadas();
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}
			registrarAtividadeUsuario(getUsuarioLogado(), "AlunosPorDisciplinasRelControle", "Finalizando Geração de Relatório Alunos Por Disciplinas", "Emitindo Relatório");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void montarListaSelectItemUnidadeEnsino() {
		try {
			List<UnidadeEnsinoVO> resultadoConsulta = consultarUnidadeEnsinoPorNome("");
			setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	private List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
		List<UnidadeEnsinoVO> lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		return lista;
	}

	public void montarListaSelectItemCurso() throws Exception {
		List<UnidadeEnsinoCursoVO> resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarCursoPorUnidadeEnsino(getUnidadeEnsinoVO().getCodigo());
			setListaSelectItemCurso(new ArrayList(0));
			i = resultadoConsulta.iterator();
			getListaSelectItemCurso().add(new SelectItem(0, ""));
			while (i.hasNext()) {
				UnidadeEnsinoCursoVO unidadeEnsinoCurso = (UnidadeEnsinoCursoVO) i.next();
				getListaSelectItemCurso().add(new SelectItem(unidadeEnsinoCurso.getCodigo(), unidadeEnsinoCurso.getNomeCursoTurno()));
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	private List<UnidadeEnsinoCursoVO> consultarCursoPorUnidadeEnsino(Integer codigoUnidadeEnsino) throws Exception {
		List<UnidadeEnsinoCursoVO> lista = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorCodigoUnidadeEnsino(codigoUnidadeEnsino, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
		return lista;
	}

	// public void montarListaSelectItemCurso() throws Exception {
	// try {
	// List<CursoVO> resultadoConsulta =
	// consultarCursoPorUnidadeEnsino(getUnidadeEnsinoVO().getCodigo());
	// setListaSelectItemCurso(UtilSelectItem.getListaSelectItem(resultadoConsulta,
	// "codigo", "nome"));
	// } catch (Exception e) {
	// setMensagemDetalhada("msg_erro", e.getMessage());
	// }
	// }
	//
	// private List<CursoVO> consultarCursoPorUnidadeEnsino(Integer
	// codigoUnidadeEnsino) throws Exception {
	// List<CursoVO> lista =
	// getFacadeFactory().getCursoFacade().consultarPorUnidadeEnsino(codigoUnidadeEnsino,
	// false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS);
	// return lista;
	// }
	public List getListaSelectItemSemestre() {
		List lista = new ArrayList(0);
		lista.add(new SelectItem("", ""));
		lista.add(new SelectItem("1", "1º"));
		lista.add(new SelectItem("2", "2º"));
		return lista;
	}

	public List getListaSelectItemSituacao() {
		List lista = new ArrayList(0);
		lista.add(new SelectItem("", ""));
		lista.add(new SelectItem("AT", "Matriculados"));
		lista.add(new SelectItem("PR", "Pré-Matriculados"));
		return lista;
	}

	public void consultarCurso() {
		try {

			List objs = new ArrayList(0);
			if (getCampoConsultaCurso().equals("codigo")) {
				if (getValorConsultaCurso().equals("")) {
					setValorConsultaCurso("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaCurso());
				objs = getFacadeFactory().getCursoFacade().consultarCursoPorCodigoUnidadeEnsino(valorInt, getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());

			}

			if (getCampoConsultaCurso().equals("nome")) {
				objs = getFacadeFactory().getCursoFacade().consultarPorNomeCursoUnidadeEnsinoBasica(getValorConsultaCurso(), getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			setListaConsultaCurso(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}

	public void consultarTurma() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaTurma().equals("codigo")) {
				if (getCampoConsultaTurma().equals("")) {
					setValorConsultaTurma("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaTurma());
				objs = getFacadeFactory().getTurmaFacade().consultarPorCodigoTurmaCursoEUnidadeEnsino(valorInt, getCursoVO().getCodigo(), getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nome")) {
				objs = getFacadeFactory().getTurmaFacade().consultarPorNomeTurmaCursoEUnidadeEnsino(getValorConsultaTurma(), getCursoVO().getCodigo(), getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			setListaConsultaTurma(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaTurma(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}

	public void selecionarCurso() throws Exception {
		try {
			CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItens");
			setCursoVO(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, getUsuarioLogado()));
			getUnidadeEnsinoCursoVO().setCurso(getCursoVO());			
			montarListaSelectItemTurno();
			montarListaSelectItemGradeCurricular();
			montarListaSelectItemDisciplinaPorCurso();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarTurma() throws Exception {
		try {
			TurmaVO turma = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
			setTurmaVO(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(turma.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			if (getTurmaVO().getSubturma()) {
				setCursoVO(getFacadeFactory().getCursoFacade().consultarCursoPorTurma(getTurmaVO().getTurmaPrincipal(), false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
			} else if (getTurmaVO().getTurmaAgrupada()) {
				setCursoVO(new CursoVO());
			} else {
				setCursoVO(getTurmaVO().getCurso());
			}
			montarListaSelectItemDisciplina();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparCurso() {
		try {
			setCursoVO(new CursoVO());
			limparTurma();
			setSituacao("");
			setLayout("layout1");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	List<SelectItem> tipoConsultaComboCurso;

	public List getTipoConsultaComboCurso() {
		if (tipoConsultaComboCurso == null) {
			tipoConsultaComboCurso = new ArrayList(0);
			tipoConsultaComboCurso.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboCurso.add(new SelectItem("codigo", "Código"));
		}
		return tipoConsultaComboCurso;
	}

	public List getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList(0);
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
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

	public String getAno() {
		if (ano == null) {
			ano = "";
		}
		return ano;
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

	public CursoVO getCursoVO() {
		if (cursoVO == null) {
			cursoVO = new CursoVO();
		}
		return cursoVO;
	}

	public void setCursoVO(CursoVO cursoVO) {
		this.cursoVO = cursoVO;
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

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getSemestre() {
		if (semestre == null) {
			semestre = "";
		}
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	public String getSituacao() {
		if (situacao == null) {
			situacao = "";
		}
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public void setUnidadeEnsinoCursoVO(UnidadeEnsinoCursoVO unidadeEnsinoCursoVO) {
		this.unidadeEnsinoCursoVO = unidadeEnsinoCursoVO;
	}

	public UnidadeEnsinoCursoVO getUnidadeEnsinoCursoVO() {
		if (unidadeEnsinoCursoVO == null) {
			unidadeEnsinoCursoVO = new UnidadeEnsinoCursoVO();
		}
		return unidadeEnsinoCursoVO;
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

	public String getValorConsultaCurso() {
		if (valorConsultaCurso == null) {
			valorConsultaCurso = "";
		}
		return valorConsultaCurso;
	}

	public void setValorConsultaCurso(String valorConsultaCurso) {
		this.valorConsultaCurso = valorConsultaCurso;
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

	public List getTipoConsultaComboLayout() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("layout1", "Layout 1"));
		itens.add(new SelectItem("layout2", "Layout 2"));
		return itens;
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

	public List getListaConsultaTurma() {
		if (listaConsultaTurma == null) {
			listaConsultaTurma = new ArrayList<TurmaVO>();
		}
		return listaConsultaTurma;
	}

	public void setListaConsultaTurma(List listaConsultaTurma) {
		this.listaConsultaTurma = listaConsultaTurma;
	}

	public List getTipoConsultaComboTurma() {
		if (tipoConsultaComboCurso == null) {
			tipoConsultaComboCurso = new ArrayList(0);
			tipoConsultaComboCurso.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboCurso.add(new SelectItem("codigo", "Código"));
		}
		return tipoConsultaComboCurso;
	}

	public void limparTurma() {
		try {
			setTurmaVO(new TurmaVO());
			setDisciplina(new DisciplinaVO());
			montarListaSelectItemDisciplina();
		} catch (Exception e) {
		}
	}

	public void limparDados() {
		getUnidadeEnsinoCursoVO().setUnidadeEnsino(getUnidadeEnsinoVO().getCodigo());
		setCursoVO(new CursoVO());
		setTurmaVO(new TurmaVO());
		setSituacao("");
		setLayout("layout1");
	}

	public Boolean getApresentarDisciplina() {
		return (getTurmaVO() != null && getTurmaVO().getCodigo() != 0) || (getCursoVO() != null && getCursoVO().getCodigo() != 0);
	}

	public DisciplinaVO getDisciplina() {
		if (disciplina == null) {
			disciplina = new DisciplinaVO();
		}
		return disciplina;
	}

	public void setDisciplina(DisciplinaVO disciplina) {
		this.disciplina = disciplina;
	}

	public List getListaSelectItemDisciplina() {
		if (listaSelectItemDisciplina == null) {
			listaSelectItemDisciplina = new ArrayList<DisciplinaVO>();
		}
		return listaSelectItemDisciplina;
	}

	public void setListaSelectItemDisciplina(List listaSelectItemDisciplina) {
		this.listaSelectItemDisciplina = listaSelectItemDisciplina;
	}

	public void montarListaSelectItemDisciplina() throws Exception {
		try {
			setListaSelectItemDisciplina(new ArrayList<SelectItem>(0));
//			List<DisciplinaVO> listaDisciplina = getFacadeFactory().getDisciplinaFacade().consutlarDisciplinaPorCodigoTurma(getTurmaVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
//			List<SelectItem> objs = new ArrayList<SelectItem>(0);
//			objs.add(new SelectItem(0, ""));
//			Iterator<DisciplinaVO> i = listaDisciplina.iterator();
//			while (i.hasNext()) {
//				DisciplinaVO obj = (DisciplinaVO) i.next();
//				if (obj.getDisciplinaComposta()) {
//					obj.setDisciplinaCompostaVOs(getFacadeFactory().getDisciplinaCompostaFacade().consultarDisciplinaComposta(obj.getCodigo(), false, getUsuarioLogado()));
//					for (DisciplinaCompostaVO disciplinaCompostaVO : obj.getDisciplinaCompostaVOs()) {
//						objs.add(new SelectItem(disciplinaCompostaVO.getCompostaVO().getCodigo(), disciplinaCompostaVO.getCompostaVO().getNome()));
//					}
//				} else {
//					objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
//				}
//
//			}
//			SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
//			Collections.sort((List<SelectItem>) objs, ordenador);
//			setListaSelectItemDisciplina(objs);
			List<HorarioTurmaDisciplinaProgramadaVO> horarioTurmaDisciplinaProgramadaVOs = getFacadeFactory().getHorarioTurmaFacade().consultarHorarioTurmaDisciplinaProgramadaPorTurma(getTurmaVO().getCodigo(), false, true, 0);
			getListaSelectItemDisciplina().add(new SelectItem(0, ""));
			for (HorarioTurmaDisciplinaProgramadaVO obj : horarioTurmaDisciplinaProgramadaVOs) {
				getListaSelectItemDisciplina().add(new SelectItem(obj.getCodigoDisciplina(), obj.getNomeDisciplina() + " - CH: " + obj.getChDisciplina()));
			}
		} catch (Exception e) {
			setMensagemID("msg_entre_prmconsulta");
		}
	}

	public void montarListaSelectItemDisciplinaPorCurso() throws Exception {
		try {
			setListaSelectItemDisciplina(new ArrayList<SelectItem>(0));
			List<DisciplinaVO> listaDisciplina = getFacadeFactory().getDisciplinaFacade().consultarDisciplinaGradeEOptativaPorGradeCurricularFazParteComposicao(getGradeCurricular() , false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			List<SelectItem> objs = new ArrayList<SelectItem>(0);
			objs.add(new SelectItem(0, ""));
			Iterator<DisciplinaVO> i = listaDisciplina.iterator();
			while (i.hasNext()) {
				DisciplinaVO obj = (DisciplinaVO) i.next();
				if (obj.getDisciplinaComposta()) {
					obj.setDisciplinaCompostaVOs(getFacadeFactory().getDisciplinaCompostaFacade().consultarDisciplinaComposta(obj.getCodigo(), false, getUsuarioLogado()));
					for (DisciplinaCompostaVO disciplinaCompostaVO : obj.getDisciplinaCompostaVOs()) {
						objs.add(new SelectItem(disciplinaCompostaVO.getCompostaVO().getCodigo(), disciplinaCompostaVO.getCompostaVO().getNome()));
					}
				} else {
					objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
				}

			}
			SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
			Collections.sort((List<SelectItem>) objs, ordenador);
			setListaSelectItemDisciplina(objs);
		} catch (Exception e) {
			setMensagemID("msg_entre_prmconsulta");
		}
	}

	public Boolean getApresentarNivelEducacionalPosGraduacao() {
		return (getCursoVO() != null && getCursoVO().getCodigo() != 0 && getCursoVO().getNivelEducacional().equals("PO") || getCursoVO().getNivelEducacional().equals("EX"));
	}

	public Boolean getApresentarTurma() {
		return (getUnidadeEnsinoCursoVO() != null && getUnidadeEnsinoCursoVO().getUnidadeEnsino() != 0);
	}

	public boolean getLayout2() {
		if (getLayout().equals("layout1")) {
			return false;
		} else {
			return true;
		}
	}

	public List<SelectItem> getListaSelectItemFiltrarPor() {
		List<SelectItem> objs = new ArrayList<SelectItem>(0);
		objs.add(new SelectItem("curso", "Curso"));
		objs.add(new SelectItem("turma", "Turma"));
		return objs;
	}

	public String getFiltrarPor() {
		if (filtrarPor == null) {
			filtrarPor = "curso";
		}
		return filtrarPor;
	}

	public void setFiltrarPor(String filtrarPor) {
		this.filtrarPor = filtrarPor;
	}

	public boolean getIsApresentarFiltroTurma() {
		return getFiltrarPor().equals("turma");
	}

	public boolean getIsApresentarFiltroTurnoMatrizCurricular() {
		return !getCursoVO().getCodigo().equals(0) && !getIsApresentarFiltroTurma();
	}

	public List<SelectItem> getListaSelectItemTurno() {
		if (listaSelectItemTurno == null) {
			listaSelectItemTurno = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemTurno;
	}

	public void setListaSelectItemTurno(List<SelectItem> listaSelectItemTurno) {
		this.listaSelectItemTurno = listaSelectItemTurno;
	}

	public void montarListaSelectItemTurno() throws Exception {
		List<TurnoVO> turnoVOs = getFacadeFactory().getTurnoFacade().consultarPorCodigoCursoUnidadeEnsino(getCursoVO().getCodigo(), getUnidadeEnsinoVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		setListaSelectItemTurno(UtilSelectItem.getListaSelectItem(turnoVOs, "codigo", "nome", true));
	}

	public List<SelectItem> getListaSelectItemGradeCurricular() {
		if (listaSelectItemGradeCurricular == null) {
			listaSelectItemGradeCurricular = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemGradeCurricular;
	}

	public void setListaSelectItemGradeCurricular(List<SelectItem> listaSelectItemGradeCurricular) {
		this.listaSelectItemGradeCurricular = listaSelectItemGradeCurricular;
	}

	public void montarListaSelectItemGradeCurricular() throws Exception {
		List<GradeCurricularVO> gradeCurricularVOs = getFacadeFactory().getGradeCurricularFacade().consultarPorCodigoCurso(getCursoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		setListaSelectItemGradeCurricular(UtilSelectItem.getListaSelectItem(gradeCurricularVOs, "codigo", "nome", true));
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

	public Integer getGradeCurricular() {
		if (gradeCurricular == null) {
			gradeCurricular = 0;
		}
		return gradeCurricular;
	}

	public void setGradeCurricular(Integer gradeCurricular) {
		this.gradeCurricular = gradeCurricular;
	}

	public Integer getTurno() {
		if (turno == null) {
			turno = 0;
		}
		return turno;
	}

	public void setTurno(Integer turno) {
		this.turno = turno;
	}

	public boolean getIsApresentarAno() {
		if ((getUnidadeEnsinoCursoVO().getCurso().getPeriodicidade().equals("SE") || getUnidadeEnsinoCursoVO().getCurso().getPeriodicidade().equals("AN")) && !getUnidadeEnsinoCursoVO().getCurso().getNivelEducacionalPosGraduacao()) {
			return true;
		} else if ((getTurmaVO().getCurso().getPeriodicidade().equals("SE") || getTurmaVO().getCurso().getPeriodicidade().equals("AN")) && !getTurmaVO().getCurso().getNivelEducacionalPosGraduacao()) {
			return true;
		} else {
			setAno("");
			setSemestre("");
		}
		return false;
	}

	public boolean getIsApresentarSemestre() {
		if (getUnidadeEnsinoCursoVO().getCurso().getPeriodicidade().equals("SE") && !getUnidadeEnsinoCursoVO().getCurso().getNivelEducacionalPosGraduacao()) {
			setSemestre(Uteis.getSemestreAtual());
			return true;
		} else if (getTurmaVO().getCurso().getPeriodicidade().equals("SE") && !getTurmaVO().getCurso().getNivelEducacionalPosGraduacao()) {
			setSemestre(Uteis.getSemestreAtual());
			return true;
		} else {
			setSemestre("");
		}
		return false;
	}
	
	public void selecionarFiltrarPor() {
		this.limparCurso();
		this.limparTurma();
	}

	public Boolean getTrazerAlunoTransferencia() {
		if (trazerAlunoTransferencia == null) {
			trazerAlunoTransferencia = false;
		}
		return trazerAlunoTransferencia;
	}

	public void setTrazerAlunoTransferencia(Boolean trazerAlunoTransferencia) {
		this.trazerAlunoTransferencia = trazerAlunoTransferencia;
	}	
	
	
	
	public Boolean getMarcarTodosTodasSituacoes() {
		if (marcarTodosTodasSituacoes == null) {
			marcarTodosTodasSituacoes = Boolean.FALSE;
		}
		return marcarTodosTodasSituacoes;
	}

	public void setMarcarTodosTodasSituacoes(Boolean marcarTodosTodasSituacoes) {
		this.marcarTodosTodasSituacoes = marcarTodosTodasSituacoes;
	}
	
	public void realizarSelecaoCheckboxMarcarDesmarcarTodosTipoOrigem() {
		if (getMarcarTodosTodasSituacoes()) {
			filtroRelatorioAcademicoVO.realizarMarcarTodasSituacoes();  
		} else {
			filtroRelatorioAcademicoVO.realizarDesmarcarTodasSituacoes();
		}
	}	
	public String getIsApresentarTextoCheckBoxMarcarDesmarcarTodasSituacoes() {
		if (getMarcarTodosTodasSituacoes()) {
			return UteisJSF.internacionalizar("prt_Inadimplencia_desmarcarTodos");
		}
		return UteisJSF.internacionalizar("prt_Inadimplencia_marcarTodos");
	}

	public String getUnidadeEnsinoApresentar() {
		if (unidadeEnsinoApresentar == null) {
			unidadeEnsinoApresentar = "";
		}
		return unidadeEnsinoApresentar;
	}

	public void setUnidadeEnsinoApresentar(String unidadeEnsinoApresentar) {
		this.unidadeEnsinoApresentar = unidadeEnsinoApresentar;
	}
	
	public void verificarTodasUnidadesSelecionadas() {
		StringBuilder unidade = new StringBuilder();
		if (getUnidadeEnsinoVOs().size() > 1) {
			for (UnidadeEnsinoVO obj : getUnidadeEnsinoVOs()) {
				if (obj.getFiltrarUnidadeEnsino()) {
					unidade.append(obj.getNome().trim()).append("; ");
				}
			}
			setUnidadeEnsinoApresentar(unidade.toString());
		} else {
			if (!getUnidadeEnsinoVOs().isEmpty()) {
				if (getUnidadeEnsinoVOs().get(0).getFiltrarUnidadeEnsino()) {
					setUnidadeEnsinoApresentar(getUnidadeEnsinoVOs().get(0).getNome());
				}
			}
		}
		consultarCursoFiltroRelatorio(getPeriodicidade().getValor());
//		consultarTurnoFiltroRelatorio();
	}
	
	public void marcarTodasUnidadesEnsinoAction() {
		for (UnidadeEnsinoVO unidade : getUnidadeEnsinoVOs()) {
			unidade.setFiltrarUnidadeEnsino(getMarcarTodasUnidadeEnsino());
		}
		verificarTodasUnidadesSelecionadas();
	}
	
	public String getCursoApresentar() {
		if(cursoApresentar == null) {
			cursoApresentar = "";
		}
		return cursoApresentar;
	}
	
	public void setCursoApresentar(String cursoApresentar) {
		this.cursoApresentar = cursoApresentar;
	}
	
	public void marcarTodosCursosAction() throws Exception {
		for (CursoVO cursoVO : getCursoVOs()) {
			cursoVO.setFiltrarCursoVO(getMarcarTodosCursos());
		}
		verificarTodosCursosSelecionados();
	}

	public void verificarTodosCursosSelecionados() {
		StringBuilder curso = new StringBuilder();
		if (getCursoVOs().size() > 1) {
			int i = 0;
			for (CursoVO obj : getCursoVOs()) {
				if (obj.getFiltrarCursoVO()) {
					curso.append(obj.getCodigo()).append(" - ");
					curso.append(obj.getNome()).append("; ");
					i++;
				}
			}
			if (i == 1) {
				try {
					getCursoVO().setCodigo(getCursoVOs().stream().filter(c -> c.getFiltrarCursoVO()).collect(Collectors.toList()).get(0).getCodigo());
					montarListaSelectItemGradeCurricular();
				} catch (Exception e) {
					setMensagemDetalhada("msg_erro", e.getMessage());
				}
			} else {
				setCursoVO(new CursoVO());
				setGradeCurricular(0);
				setListaSelectItemGradeCurricular(new ArrayList<SelectItem>(0));
				setDisciplina(new DisciplinaVO());
			}
			setCursoApresentar(curso.toString());
		} else {
			if (!getCursoVOs().isEmpty()) {
				if (getCursoVOs().get(0).getFiltrarCursoVO()) {
					setCursoApresentar(getCursoVOs().get(0).getNome());
					try {
						getCursoVO().setCodigo(getCursoVOs().get(0).getCodigo());
						montarListaSelectItemGradeCurricular();
					} catch (Exception e) {
						setMensagemDetalhada("msg_erro", e.getMessage());
					}
				} else {
					setCursoVO(new CursoVO());
					setGradeCurricular(0);
					setListaSelectItemGradeCurricular(new ArrayList<SelectItem>(0));
					setDisciplina(new DisciplinaVO());
				}
			}
		}
	}
	
	@PostConstruct
	public void consultarUnidadeEnsino() {
		try {
			consultarUnidadeEnsinoFiltroRelatorio("");
			verificarTodasUnidadesSelecionadas();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	@PostConstruct
	public void consultarCursos() {
		try {
			consultarCursoFiltroRelatorio(getPeriodicidade().getValor());
			verificarTodosCursosSelecionados();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public PeriodicidadeEnum getPeriodicidade() {
		if (periodicidade == null) {
			periodicidade = PeriodicidadeEnum.ANUAL;
		}
		return periodicidade;
	}
	
	public void setPeriodicidade(PeriodicidadeEnum periodicidade) {
		this.periodicidade = periodicidade;
	}
	
	public List<SelectItem> getPeriodicidadeEnums() {
		if (periodicidadeEnums == null) {
			periodicidadeEnums = new ArrayList<SelectItem>(0);
			for (PeriodicidadeEnum obj : PeriodicidadeEnum.values()) {
				periodicidadeEnums.add(new SelectItem(obj, obj.getDescricao()));
			}
		}
		return periodicidadeEnums;
	}
	
	public void setPeriodicidadeEnums(List<SelectItem> periodicidadeEnums) {
		this.periodicidadeEnums = periodicidadeEnums;
	}
	
}
