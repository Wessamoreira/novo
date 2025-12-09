/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controle.academico;

/**
 * 
 * @author Carlos
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

@Controller("InclusaoExclusaoDisciplinaControle")
@Scope("request")
@Lazy
public class InclusaoExclusaoDisciplinaControle extends SuperControle implements Serializable {

	private MatriculaVO matriculaVO;
	private MatriculaPeriodoTurmaDisciplinaVO matriculaPeridoTurmaDisciplinaVO;
	private List<MatriculaVO> listaAlunosTurma;
	private List listaSelectItemTurma;
	private DisciplinaVO disciplinaIncluida;
	private CursoVO cursoApresentar;
	private TurmaVO turmaVO;
	private String ano;
	private String semestre;
	private String valorConsultaAluno;
	private String campoConsultaAluno;
	private List listaConsultaAluno;
	private String campoConsultaTurma;
	private String valorConsultaTurma;
	private List listaConsultaTurma;
	private Boolean excluirDisciplina;
	private List listaExclusaoDisciplinas;
	private List listaInclusaoTurmaDisciplina;
	private String abaSelecionada;
	private Boolean apresentarAbaExclusaoDisicplina;
	private Boolean apresentarAbaInclusaoDisicplina;
	private String campoConsultaDisciplinaIncluida;
	private String valorConsultaDisciplinaIncluida;
	private List listaConsultaDisciplinaIncluida;
	private List listaSelectItemTurmaDisciplinaIncluida;
	private String campoTurmaDisciplinaIncluida;
	private List listaExcluirDisciplinaAposPersistir;
	private Boolean apresentarBotaoGravar;
	private String tipoBusca;
	private List<MatriculaPeriodoVO> matriculaPeriodoVOs;
	private MatriculaPeriodoVO matriculaPeriodoVO;
	private Boolean mostrarModalAvisoGradesDiferentes;
	private HashMap<Integer, MatriculaPeriodoVO> hashMap;
	private Integer periodoLetivo;
	private List listaSelectItemPeriodoLetivo;
	private String valorConsultaSituacaoMatricula;

	public InclusaoExclusaoDisciplinaControle() throws Exception {
		// obterUsuarioLogado();
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");
		novo();
	}

	public String novo() {
		removerObjetoMemoria(this);
		setMensagemID("msg_entre_dados");
		setAbaSelecionada("panelAlunos");
		setMatriculaVO(new MatriculaVO());
		setMatriculaPeridoTurmaDisciplinaVO(new MatriculaPeriodoTurmaDisciplinaVO());
		setTurmaVO(new TurmaVO());
		setListaAlunosTurma(new ArrayList<MatriculaVO>(0));
		setListaExclusaoDisciplinas(new ArrayList(0));
		setListaInclusaoTurmaDisciplina(new ArrayList(0));
		setListaConsultaTurma(new ArrayList(0));
		setListaSelectItemTurmaDisciplinaIncluida(new ArrayList(0));
		setApresentarAbaExclusaoDisicplina(false);
		setApresentarAbaInclusaoDisicplina(false);
		setApresentarBotaoGravar(Boolean.TRUE);
		setValorConsultaTurma("");
		setValorConsultaAluno("");
		setValorConsultaDisciplinaIncluida("");
		setMostrarModalAvisoGradesDiferentes(Boolean.FALSE);
		setHashMap(null);
		setMatriculaPeriodoVO(null);
		setMatriculaPeriodoVOs(null);
		setListaSelectItemPeriodoLetivo(new ArrayList(0));
		// setPeriodoLetivo(0);
		return "editar";
	}

	public void persistir() {
		try {
			getFacadeFactory().getInclusaoExclusaoDisciplina().persistir(getListaExclusaoDisciplinas(), getListaAlunosTurma(), getListaInclusaoTurmaDisciplina(), getListaExcluirDisciplinaAposPersistir(), getPeriodoLetivo(), getTipoBusca(), getAno(), getSemestre(), getTurmaVO().getCodigo(), getCursoApresentar().getNivelEducacional(), getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			setAbaSelecionada("panelAlunos");
			setApresentarAbaExclusaoDisicplina(false);
			setApresentarAbaInclusaoDisicplina(false);
			setApresentarBotaoGravar(Boolean.FALSE);
			setMensagemID("msg_dados_gravados");
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public void consultarAlunoPorMatricula() {
		try {
			this.setMatriculaVO(getFacadeFactory().getInclusaoExclusaoDisciplina().consultarAlunoPorMatricula(getMatriculaVO().getMatricula(), this.getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado()));
			// setMatriculaPeriodoVO(getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaBasicaUltimaMatriculaPeriodoPorMatricula(getMatriculaVO().getMatricula(),
			// false,
			// Uteis.NIVELMONTARDADOS_DADOSMINIMOS));
			realizarMontagemListaPeriodoLetivo();
			setTipoBusca("BM");
			setMensagemDetalhada("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			this.setMatriculaVO(new MatriculaVO());
			setListaSelectItemPeriodoLetivo(new ArrayList(0));
		}
	}

	public void consultarAlunosPorMatricula() {
		try {
			this.setListaAlunosTurma(new ArrayList(0));
			this.setListaAlunosTurma(getFacadeFactory().getInclusaoExclusaoDisciplina().consultarAlunosPorMatricula(getMatriculaVO().getMatricula(), getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado()));
			setMatriculaPeriodoVO(getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaBasicaUltimaMatriculaPeriodoPorMatricula(getMatriculaVO().getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			setTipoBusca("BM");
			setAno(getMatriculaPeriodoVO().getAno());
			setTurmaVO(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(getMatriculaPeriodoVO().getTurma().getCodigo().intValue(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			setSemestre(getMatriculaPeriodoVO().getSemestre());
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			this.setListaAlunosTurma(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarTurmaPorChavePrimaria() {
		try {
			getFacadeFactory().getInclusaoExclusaoDisciplina().consultarTurmaPorChavePrimaria(getTurmaVO(), getAno(), getSemestre(), getUsuarioLogado());
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			turmaVO = new TurmaVO();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarAlunosPorTurma() {
		try {
			this.setListaAlunosTurma(new ArrayList<MatriculaVO>(0));
			this.setListaAlunosTurma(getFacadeFactory().getInclusaoExclusaoDisciplina().consultarAlunosPorTurma(getTurmaVO(), getCursoApresentar(), getAno(), getSemestre(), getTurmaVO().getUnidadeEnsino().getCodigo(), getValorConsultaSituacaoMatricula(), getUsuarioLogado()));
			getHashMap().clear();
			for (MatriculaVO matriculaVO : getListaAlunosTurma()) {
				setMatriculaPeriodoVO(getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaBasicaMatriculaPeriodoPorMatriculaAnoSemestre(matriculaVO.getMatricula(), getAno(), getSemestre(), getTurmaVO().getCodigo(), getCursoApresentar().getNivelEducacional(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado(), getValorConsultaSituacaoMatricula()));
				getMatriculaPeriodoVOs().add(getMatriculaPeriodoVO());
				if (getHashMap().containsKey(getMatriculaPeriodoVO().getGradeCurricular().getCodigo())) {
					setMostrarModalAvisoGradesDiferentes(Boolean.TRUE);
				}
				getHashMap().put(getMatriculaPeriodoVO().getGradeCurricular().getCodigo(), getMatriculaPeriodoVO());
			}
			setTipoBusca("BT");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			this.setListaAlunosTurma(new ArrayList<MatriculaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarAluno() {
		try {
			setListaConsultaAluno(getFacadeFactory().getInclusaoExclusaoDisciplina().consultarAluno(getValorConsultaAluno(), getCampoConsultaAluno(), getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAluno(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarAluno() throws Exception {
		try {
			MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matricula");
			MatriculaVO objCompleto = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula(), obj.getUnidadeEnsino().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado());
			setMatriculaVO(objCompleto);
			realizarMontagemListaPeriodoLetivo();
			setListaAlunosTurma(new ArrayList<MatriculaVO>(0));
			setListaSelectItemTurma(new ArrayList(0));
			obj = null;
			objCompleto = null;
			setValorConsultaAluno("");
			setCampoConsultaAluno("");
			getListaConsultaAluno().clear();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarDisciplinaIncluida() throws Exception {
		DisciplinaVO obj = (DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplinaIncluida");
		getMatriculaPeridoTurmaDisciplinaVO().setDisciplina(obj);
		montarDadosSelectItemTurma();
		if (getListaSelectItemTurmaDisciplinaIncluida().isEmpty()) {
			throw new Exception("Não possui nenhuma TURMA (INLUIR DISCIPLINA) para a disciplina selecionada. ");
		}
		setValorConsultaDisciplinaIncluida("");
		setCampoConsultaDisciplinaIncluida("");
		setListaConsultaDisciplinaIncluida(new ArrayList(0));
	}

	public void selecionarTurma() {
		try {
			turmaVO = (TurmaVO) context().getExternalContext().getRequestMap().get("turma");
			consultarTurmaPorChavePrimaria();
			setListaAlunosTurma(new ArrayList(0));
			setListaConsultaTurma(new ArrayList(0));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void montarDadosSelectItemTurma() {
		try {

			List resultadoConsulta = getFacadeFactory().getTurmaFacade().consultaRapidaPorDisciplinaUnidadeEnsino(getMatriculaPeridoTurmaDisciplinaVO().getDisciplina().getCodigo(), getMatriculaVO().getUnidadeEnsino().getCodigo(), false, getUsuarioLogado());
			if (resultadoConsulta.isEmpty()) {
				DisciplinaVO dp = getFacadeFactory().getDisciplinaFacade().consultarDisciplinaPorDisciplinaCompostaMatriculaPeriodo(getMatriculaPeridoTurmaDisciplinaVO().getDisciplina().getCodigo(), 0, getUsuarioLogado());
				if(dp.getCodigo()>0){
					resultadoConsulta = getFacadeFactory().getTurmaFacade().consultaRapidaPorDisciplinaUnidadeEnsino(dp.getCodigo(), getMatriculaVO().getUnidadeEnsino().getCodigo(), false, getUsuarioLogado());
				}
			}
			Iterator i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				TurmaVO obj = (TurmaVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getIdentificadorTurma()));
			}
			SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
			Collections.sort((List) objs, ordenador);
			setListaSelectItemTurmaDisciplinaIncluida(objs);
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public void consultarDisciplinaIncluidaRich() {
		try {
			setMostrarModalAvisoGradesDiferentes(Boolean.FALSE);
			if (getTipoBusca().equals("BM")) {
				setListaConsultaDisciplinaIncluida(getFacadeFactory().getInclusaoExclusaoDisciplina().consultarDisciplinaIncluidaRich(getMatriculaPeriodoVO(), getCampoConsultaDisciplinaIncluida(), getValorConsultaDisciplinaIncluida(), getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado()));

			} else if (getTipoBusca().equals("BT")) {
				setListaConsultaDisciplinaIncluida(getFacadeFactory().getInclusaoExclusaoDisciplina().consultarDisciplinaIncluidaRich(getHashMap(), getCampoConsultaDisciplinaIncluida(), getValorConsultaDisciplinaIncluida(), getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado()));
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaDisciplinaIncluida(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarTurma() {
		try {
			super.consultar();
			List objs = new ArrayList(0);
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(getValorConsultaTurma(), getUnidadeEnsinoLogado().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeUnidadeEnsino")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorUnidadeEnsino(getValorConsultaTurma(), getUnidadeEnsinoLogado().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeTurno")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorTurno(getValorConsultaTurma(), getUnidadeEnsinoLogado().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeCurso")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaNomeCurso(getValorConsultaTurma(), getUnidadeEnsinoLogado().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaTurma(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void adicionarDisciplinaIncluida() {
		try {
			getMatriculaPeridoTurmaDisciplinaVO().setTurma(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(getMatriculaPeridoTurmaDisciplinaVO().getTurma().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			// List<DisciplinaVO> listaDisciplinasAlunoVaiCursar = null;
			// listaDisciplinasAlunoVaiCursar = getListaExclusaoDisciplinas();
			//
			// getFacadeFactory().getInclusaoExclusaoDisciplina().executarVerificarSeHaIncompatibilidadeHorarioDeDisciplinas(getMatriculaPeridoTurmaDisciplinaVO(),
			// getListaInclusaoTurmaDisciplina(),
			// listaDisciplinasAlunoVaiCursar, getTurmaVO(), getSemestre(),
			// getAno());
			getFacadeFactory().getInclusaoExclusaoDisciplina().adicionarTurmaDisciplina(getMatriculaPeridoTurmaDisciplinaVO(), getListaInclusaoTurmaDisciplina());
			setMatriculaPeridoTurmaDisciplinaVO(new MatriculaPeriodoTurmaDisciplinaVO());
			setMensagemID("msg_dados_adicionados");
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public void removerTurmaDisciplinaIncluida() throws Exception {
		MatriculaPeriodoTurmaDisciplinaVO obj = (MatriculaPeriodoTurmaDisciplinaVO) context().getExternalContext().getRequestMap().get("inclusaoTurmaDisciplina");
		getFacadeFactory().getInclusaoExclusaoDisciplina().excluirObjsTurmaDisciplinaIncluidaVOs(obj, getListaInclusaoTurmaDisciplina(), getListaExcluirDisciplinaAposPersistir());
		setMensagemID("msg_dados_excluidos");
	}

	public void limparDadosAluno() throws Exception {
		setMatriculaVO(new MatriculaVO());
		setListaAlunosTurma(new ArrayList(0));
		setMensagemID("msg_entre_prmconsulta");
	}

	public void limparDadosDisciplinaIncluida() throws Exception {
		getMatriculaPeridoTurmaDisciplinaVO().setDisciplina(new DisciplinaVO());
		setListaSelectItemTurmaDisciplinaIncluida(new ArrayList(0));
		setMensagemID("msg_entre_prmconsulta");
	}

	public void limparLista() {
		setListaAlunosTurma(new ArrayList(0));
	}

	public List getTipoConsultaComboAluno() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nomePessoa", "Aluno"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
	}

	public List getTipoConsultaComboDisciplina() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	public List getTipoConsultaComboTurma() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("identificadorTurma", "Identificador"));
		itens.add(new SelectItem("nomeUnidadeEnsino", "Unidade Ensino"));
		itens.add(new SelectItem("nomeTurno", "Turno"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
	}

	public void executarInsercaoDisciplinasListaPorMatriculaPeriodoSituacaoAtiva() {
		try {
			if (getTipoBusca().equals("BM")) {
				setListaExclusaoDisciplinas(getFacadeFactory().getInclusaoExclusaoDisciplina().executarInsercaoDisciplinasListaPorMatriculaPeriodoSituacaoAtiva(getListaAlunosTurma(), getUnidadeEnsinoLogado().getCodigo(), getMatriculaPeriodoVO().getCodigo(), getUsuarioLogado()));
			} else {
				setListaExclusaoDisciplinas(getFacadeFactory().getInclusaoExclusaoDisciplina().executarInsercaoDisciplinasPorListaMatricula(getListaAlunosTurma(), getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado()));
			}
			setAbaSelecionada("panelExclusaoDisciplina");
			setApresentarAbaExclusaoDisicplina(true);
			setApresentarAbaInclusaoDisicplina(false);
			setMensagemID("");
			setMensagem("");
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public void realizarNavegacaoAvancarAbaIncluirDisciplina() {
		setAbaSelecionada("panelInclusaoDisciplinas");
		setApresentarAbaInclusaoDisicplina(true);
	}

	public void realizarNavegacaoVoltarAbaPanelAlunos() {
		setAbaSelecionada("panelAlunos");
		setApresentarAbaExclusaoDisicplina(false);
		setApresentarAbaInclusaoDisicplina(false);
	}

	public void realizarNavegacaoVoltarAbaExcluirDisciplina() {
		setAbaSelecionada("panelExclusaoDisciplina");
		setApresentarAbaInclusaoDisicplina(false);
		setApresentarAbaExclusaoDisicplina(true);
	}

	public void realizarMontagemListaPeriodoLetivo() throws Exception {
		setListaSelectItemPeriodoLetivo(getFacadeFactory().getInclusaoExclusaoDisciplina().realizarMontagemListaPeriodoLetivo(getMatriculaVO(), getUsuarioLogado(), false));
	}

	public void removerAlunoLista() {
		try {
			MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaAluno");
			getListaAlunosTurma().remove(obj);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * @return the alunosTurma
	 */
	public List<MatriculaVO> getListaAlunosTurma() {
		if (listaAlunosTurma == null) {
			listaAlunosTurma = new ArrayList<MatriculaVO>(0);
		}
		return listaAlunosTurma;
	}

	/**
	 * @param alunosTurma
	 *            the alunosTurma to set
	 */
	public void setListaAlunosTurma(List<MatriculaVO> listaAlunosTurma) {
		this.listaAlunosTurma = listaAlunosTurma;
	}

	/**
	 * @return the listaSelectItemTurma
	 */
	public List getListaSelectItemTurma() {
		if (listaSelectItemTurma == null) {
			listaSelectItemTurma = new ArrayList(0);
		}
		return listaSelectItemTurma;
	}

	/**
	 * @param listaSelectItemTurma
	 *            the listaSelectItemTurma to set
	 */
	public void setListaSelectItemTurma(List listaSelectItemTurma) {
		this.listaSelectItemTurma = listaSelectItemTurma;
	}

	/**
	 * @return the cursoApresentar
	 */
	public CursoVO getCursoApresentar() {
		if (cursoApresentar == null) {
			cursoApresentar = new CursoVO();
		}
		return cursoApresentar;
	}

	/**
	 * @param cursoApresentar
	 *            the cursoApresentar to set
	 */
	public void setCursoApresentar(CursoVO cursoApresentar) {
		this.cursoApresentar = cursoApresentar;
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
	 * @return the ano
	 */
	public String getAno() {
		if (ano == null) {
			ano = "";
		}
		return ano;
	}

	/**
	 * @param ano
	 *            the ano to set
	 */
	public void setAno(String ano) {
		this.ano = ano;
	}

	public List getListaSelectSemestre() {
		List lista = new ArrayList(0);
		lista.add(new SelectItem("1", "1º"));
		lista.add(new SelectItem("2", "2º"));
		return lista;
	}

	/**
	 * @return the semestre
	 */
	public String getSemestre() {
		if (semestre == null) {
			semestre = "";
		}
		return semestre;
	}

	/**
	 * @param semestre
	 *            the semestre to set
	 */
	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	/**
	 * @return the valorConsultaAluno
	 */
	public String getValorConsultaAluno() {
		if (valorConsultaAluno == null) {
			valorConsultaAluno = "";
		}
		return valorConsultaAluno;
	}

	/**
	 * @param valorConsultaAluno
	 *            the valorConsultaAluno to set
	 */
	public void setValorConsultaAluno(String valorConsultaAluno) {
		this.valorConsultaAluno = valorConsultaAluno;
	}

	/**
	 * @return the campoConsultaAluno
	 */
	public String getCampoConsultaAluno() {
		if (campoConsultaAluno == null) {
			campoConsultaAluno = "";
		}
		return campoConsultaAluno;
	}

	/**
	 * @param campoConsultaAluno
	 *            the campoConsultaAluno to set
	 */
	public void setCampoConsultaAluno(String campoConsultaAluno) {
		this.campoConsultaAluno = campoConsultaAluno;
	}

	/**
	 * @return the listaConsultaAluno
	 */
	public List getListaConsultaAluno() {
		if (listaConsultaAluno == null) {
			listaConsultaAluno = new ArrayList(0);
		}
		return listaConsultaAluno;
	}

	/**
	 * @param listaConsultaAluno
	 *            the listaConsultaAluno to set
	 */
	public void setListaConsultaAluno(List listaConsultaAluno) {
		this.listaConsultaAluno = listaConsultaAluno;
	}

	/**
	 * @return the campoConsultaTurma
	 */
	public String getCampoConsultaTurma() {
		if (campoConsultaTurma == null) {
			campoConsultaTurma = "";
		}
		return campoConsultaTurma;
	}

	/**
	 * @param campoConsultaTurma
	 *            the campoConsultaTurma to set
	 */
	public void setCampoConsultaTurma(String campoConsultaTurma) {
		this.campoConsultaTurma = campoConsultaTurma;
	}

	/**
	 * @return the valorConsultaTurma
	 */
	public String getValorConsultaTurma() {
		if (valorConsultaTurma == null) {
			valorConsultaTurma = "";
		}
		return valorConsultaTurma;
	}

	/**
	 * @param valorConsultaTurma
	 *            the valorConsultaTurma to set
	 */
	public void setValorConsultaTurma(String valorConsultaTurma) {
		this.valorConsultaTurma = valorConsultaTurma;
	}

	/**
	 * @return the listaConsultaTurma
	 */
	public List getListaConsultaTurma() {
		if (listaConsultaTurma == null) {
			listaConsultaTurma = new ArrayList(0);
		}
		return listaConsultaTurma;
	}

	/**
	 * @param listaConsultaTurma
	 *            the listaConsultaTurma to set
	 */
	public void setListaConsultaTurma(List listaConsultaTurma) {
		this.listaConsultaTurma = listaConsultaTurma;
	}

	/**
	 * @return the excluirDisciplina
	 */
	public Boolean getExcluirDisciplina() {
		if (excluirDisciplina == null) {
			excluirDisciplina = Boolean.FALSE;
		}
		return excluirDisciplina;
	}

	/**
	 * @param excluirDisciplina
	 *            the excluirDisciplina to set
	 */
	public void setExcluirDisciplina(Boolean excluirDisciplina) {
		this.excluirDisciplina = excluirDisciplina;
	}

	/**
	 * @return the listaDisciplinas
	 */
	public List getListaExclusaoDisciplinas() {
		if (listaExclusaoDisciplinas == null) {
			listaExclusaoDisciplinas = new ArrayList(0);
		}
		return listaExclusaoDisciplinas;
	}

	/**
	 * @param listaDisciplinas
	 *            the listaDisciplinas to set
	 */
	public void setListaExclusaoDisciplinas(List listaExclusaoDisciplinas) {
		this.listaExclusaoDisciplinas = listaExclusaoDisciplinas;
	}

	/**
	 * @return the abaSelecionada
	 */
	public String getAbaSelecionada() {
		if (abaSelecionada == null) {
			abaSelecionada = "";
		}
		return abaSelecionada;
	}

	/**
	 * @param abaSelecionada
	 *            the abaSelecionada to set
	 */
	public void setAbaSelecionada(String abaSelecionada) {
		this.abaSelecionada = abaSelecionada;
	}

	/**
	 * @return the apresentarAbaExclusaoDisicplina
	 */
	public Boolean getApresentarAbaExclusaoDisicplina() {
		if (apresentarAbaExclusaoDisicplina == null) {
			apresentarAbaExclusaoDisicplina = Boolean.FALSE;
		}
		return apresentarAbaExclusaoDisicplina;
	}

	/**
	 * @param apresentarAbaExclusaoDisicplina
	 *            the apresentarAbaExclusaoDisicplina to set
	 */
	public void setApresentarAbaExclusaoDisicplina(Boolean apresentarAbaExclusaoDisicplina) {
		this.apresentarAbaExclusaoDisicplina = apresentarAbaExclusaoDisicplina;
	}

	/**
	 * @return the apresentarAbaInclusaooDisicplina
	 */
	public Boolean getApresentarAbaInclusaoDisicplina() {
		if (apresentarAbaInclusaoDisicplina == null) {
			apresentarAbaInclusaoDisicplina = Boolean.FALSE;
		}
		return apresentarAbaInclusaoDisicplina;
	}

	/**
	 * @param apresentarAbaInclusaooDisicplina
	 *            the apresentarAbaInclusaooDisicplina to set
	 */
	public void setApresentarAbaInclusaoDisicplina(Boolean apresentarAbaInclusaoDisicplina) {
		this.apresentarAbaInclusaoDisicplina = apresentarAbaInclusaoDisicplina;
	}

	/**
	 * @return the listaInclusaoDisciplinas
	 */
	public List getListaInclusaoTurmaDisciplina() {
		if (listaInclusaoTurmaDisciplina == null) {
			listaInclusaoTurmaDisciplina = new ArrayList(0);
		}
		return listaInclusaoTurmaDisciplina;
	}

	/**
	 * @param listaInclusaoDisciplinas
	 *            the listaInclusaoDisciplinas to set
	 */
	public void setListaInclusaoTurmaDisciplina(List listaInclusaoTurmaDisciplina) {
		this.listaInclusaoTurmaDisciplina = listaInclusaoTurmaDisciplina;
	}

	/**
	 * @return the disciplinaIncluida
	 */
	public DisciplinaVO getDisciplinaIncluida() {
		if (disciplinaIncluida == null) {
			disciplinaIncluida = new DisciplinaVO();
		}
		return disciplinaIncluida;
	}

	/**
	 * @param disciplinaIncluida
	 *            the disciplinaIncluida to set
	 */
	public void setDisciplinaIncluida(DisciplinaVO disciplinaIncluida) {
		this.disciplinaIncluida = disciplinaIncluida;
	}

	/**
	 * @return the campoConsultaDisciplinaIncluida
	 */
	public String getCampoConsultaDisciplinaIncluida() {
		if (campoConsultaDisciplinaIncluida == null) {
			campoConsultaDisciplinaIncluida = "";
		}
		return campoConsultaDisciplinaIncluida;
	}

	/**
	 * @param campoConsultaDisciplinaIncluida
	 *            the campoConsultaDisciplinaIncluida to set
	 */
	public void setCampoConsultaDisciplinaIncluida(String campoConsultaDisciplinaIncluida) {
		this.campoConsultaDisciplinaIncluida = campoConsultaDisciplinaIncluida;
	}

	/**
	 * @return the valorConsultaDisciplinaIncluida
	 */
	public String getValorConsultaDisciplinaIncluida() {
		if (valorConsultaDisciplinaIncluida == null) {
			valorConsultaDisciplinaIncluida = "";
		}
		return valorConsultaDisciplinaIncluida;
	}

	/**
	 * @param valorConsultaDisciplinaIncluida
	 *            the valorConsultaDisciplinaIncluida to set
	 */
	public void setValorConsultaDisciplinaIncluida(String valorConsultaDisciplinaIncluida) {
		this.valorConsultaDisciplinaIncluida = valorConsultaDisciplinaIncluida;
	}

	/**
	 * @return the listaConsultaDisciplinaIncluida
	 */
	public List getListaConsultaDisciplinaIncluida() {
		if (listaConsultaDisciplinaIncluida == null) {
			listaConsultaDisciplinaIncluida = new ArrayList(0);
		}
		return listaConsultaDisciplinaIncluida;
	}

	/**
	 * @param listaConsultaDisciplinaIncluida
	 *            the listaConsultaDisciplinaIncluida to set
	 */
	public void setListaConsultaDisciplinaIncluida(List listaConsultaDisciplinaIncluida) {
		this.listaConsultaDisciplinaIncluida = listaConsultaDisciplinaIncluida;
	}

	/**
	 * @return the matriculaPeridoTurmaDisciplina
	 */
	public MatriculaPeriodoTurmaDisciplinaVO getMatriculaPeridoTurmaDisciplinaVO() {
		if (matriculaPeridoTurmaDisciplinaVO == null) {
			matriculaPeridoTurmaDisciplinaVO = new MatriculaPeriodoTurmaDisciplinaVO();
		}
		return matriculaPeridoTurmaDisciplinaVO;
	}

	/**
	 * @param matriculaPeridoTurmaDisciplina
	 *            the matriculaPeridoTurmaDisciplina to set
	 */
	public void setMatriculaPeridoTurmaDisciplinaVO(MatriculaPeriodoTurmaDisciplinaVO matriculaPeridoTurmaDisciplinaVO) {
		this.matriculaPeridoTurmaDisciplinaVO = matriculaPeridoTurmaDisciplinaVO;
	}

	/**
	 * @return the listaSelectItemTurmaDisciplinaIncluida
	 */
	public List getListaSelectItemTurmaDisciplinaIncluida() {
		if (listaSelectItemTurmaDisciplinaIncluida == null) {
			listaSelectItemTurmaDisciplinaIncluida = new ArrayList(0);
		}
		return listaSelectItemTurmaDisciplinaIncluida;
	}

	/**
	 * @param listaSelectItemTurmaDisciplinaIncluida
	 *            the listaSelectItemTurmaDisciplinaIncluida to set
	 */
	public void setListaSelectItemTurmaDisciplinaIncluida(List listaSelectItemTurmaDisciplinaIncluida) {
		this.listaSelectItemTurmaDisciplinaIncluida = listaSelectItemTurmaDisciplinaIncluida;
	}

	/**
	 * @return the campoTurmaDisciplinaIncluida
	 */
	public String getCampoTurmaDisciplinaIncluida() {
		if (campoTurmaDisciplinaIncluida == null) {
			campoTurmaDisciplinaIncluida = "";
		}
		return campoTurmaDisciplinaIncluida;
	}

	/**
	 * @param campoTurmaDisciplinaIncluida
	 *            the campoTurmaDisciplinaIncluida to set
	 */
	public void setCampoTurmaDisciplinaIncluida(String campoTurmaDisciplinaIncluida) {
		this.campoTurmaDisciplinaIncluida = campoTurmaDisciplinaIncluida;
	}

	public Boolean getApresentarTurmaInclusao() {
		if (!getListaSelectItemTurmaDisciplinaIncluida().isEmpty()) {
			return true;
		}
		return false;
	}

	/**
	 * @return the listaExcluirDisciplinaAposPersistir
	 */
	public List getListaExcluirDisciplinaAposPersistir() {
		if (listaExcluirDisciplinaAposPersistir == null) {
			listaExcluirDisciplinaAposPersistir = new ArrayList(0);
		}
		return listaExcluirDisciplinaAposPersistir;
	}

	/**
	 * @param listaExcluirDisciplinaAposPersistir
	 *            the listaExcluirDisciplinaAposPersistir to set
	 */
	public void setListaExcluirDisciplinaAposPersistir(List listaExcluirDisciplinaAposPersistir) {
		this.listaExcluirDisciplinaAposPersistir = listaExcluirDisciplinaAposPersistir;
	}

	/**
	 * @return the matriculaVO
	 */
	public MatriculaVO getMatriculaVO() {
		if (matriculaVO == null) {
			matriculaVO = new MatriculaVO();
		}
		return matriculaVO;
	}

	/**
	 * @param matriculaVO
	 *            the matriculaVO to set
	 */
	public void setMatriculaVO(MatriculaVO matriculaVO) {
		this.matriculaVO = matriculaVO;
	}

	/**
	 * @return the apresentarBotaoGravar
	 */
	public Boolean getApresentarBotaoGravar() {
		if (apresentarBotaoGravar == null) {
			apresentarBotaoGravar = Boolean.TRUE;
		}
		return apresentarBotaoGravar;
	}

	/**
	 * @param apresentarBotaoGravar
	 *            the apresentarBotaoGravar to set
	 */
	public void setApresentarBotaoGravar(Boolean apresentarBotaoGravar) {
		this.apresentarBotaoGravar = apresentarBotaoGravar;
	}

	/**
	 * @return the tipoBusca
	 */
	public String getTipoBusca() {
		if (tipoBusca == null) {
			tipoBusca = "";
		}
		return tipoBusca;
	}

	/**
	 * @param tipoBusca
	 *            the tipoBusca to set
	 */
	public void setTipoBusca(String tipoBusca) {
		this.tipoBusca = tipoBusca;
	}

	public void setMatriculaPeriodoVO(MatriculaPeriodoVO matriculaPeriodoVO) {
		this.matriculaPeriodoVO = matriculaPeriodoVO;
	}

	public MatriculaPeriodoVO getMatriculaPeriodoVO() {
		if (matriculaPeriodoVO == null) {
			matriculaPeriodoVO = new MatriculaPeriodoVO();
		}
		return matriculaPeriodoVO;
	}

	public void setMatriculaPeriodoVOs(List<MatriculaPeriodoVO> matriculaPeriodoVOs) {
		this.matriculaPeriodoVOs = matriculaPeriodoVOs;
	}

	public List<MatriculaPeriodoVO> getMatriculaPeriodoVOs() {
		if (matriculaPeriodoVOs == null) {
			matriculaPeriodoVOs = new ArrayList<MatriculaPeriodoVO>(0);
		}
		return matriculaPeriodoVOs;
	}

	public void setMostrarModalAvisoGradesDiferentes(Boolean mostrarModalAvisoGradesDiferentes) {
		this.mostrarModalAvisoGradesDiferentes = mostrarModalAvisoGradesDiferentes;
	}

	public Boolean getMostrarModalAvisoGradesDiferentes() {
		if (mostrarModalAvisoGradesDiferentes == null) {
			mostrarModalAvisoGradesDiferentes = Boolean.FALSE;
		}
		return mostrarModalAvisoGradesDiferentes;
	}

	public void setHashMap(HashMap<Integer, MatriculaPeriodoVO> hashMap) {
		this.hashMap = hashMap;
	}

	public HashMap<Integer, MatriculaPeriodoVO> getHashMap() {
		if (hashMap == null) {
			hashMap = new HashMap<Integer, MatriculaPeriodoVO>(0);
		}
		return hashMap;
	}

	/**
	 * @return the periodoLetivo
	 */
	public Integer getPeriodoLetivo() {
		if (periodoLetivo == null) {
			periodoLetivo = 0;
		}
		return periodoLetivo;
	}

	/**
	 * @param periodoLetivo
	 *            the periodoLetivo to set
	 */
	public void setPeriodoLetivo(Integer periodoLetivo) {
		this.periodoLetivo = periodoLetivo;
	}

	/**
	 * @return the listaSelectItemPeriodoLetivo
	 */
	public List getListaSelectItemPeriodoLetivo() {
		if (listaSelectItemPeriodoLetivo == null) {
			listaSelectItemPeriodoLetivo = new ArrayList(0);
		}
		return listaSelectItemPeriodoLetivo;
	}

	/**
	 * @param listaSelectItemPeriodoLetivo
	 *            the listaSelectItemPeriodoLetivo to set
	 */
	public void setListaSelectItemPeriodoLetivo(List listaSelectItemPeriodoLetivo) {
		this.listaSelectItemPeriodoLetivo = listaSelectItemPeriodoLetivo;
	}
	
	public List<SelectItem> getListaSelectItemSituacaoMatricula() {
		List<SelectItem> lista = new ArrayList<SelectItem>(0);
		lista.add(new SelectItem("", ""));
		lista.add(new SelectItem("ativo", "Ativo"));
		lista.add(new SelectItem("inativo", "Inativo"));
		lista.add(new SelectItem("todos", "Todos"));
		return lista;
	}

	public String getValorConsultaSituacaoMatricula() {
		if (valorConsultaSituacaoMatricula == null) {
			valorConsultaSituacaoMatricula = "";
		}
		return valorConsultaSituacaoMatricula;
	}

	public void setValorConsultaSituacaoMatricula(String valorConsultaSituacaoMatricula) {
		this.valorConsultaSituacaoMatricula = valorConsultaSituacaoMatricula;
	}
	
}
