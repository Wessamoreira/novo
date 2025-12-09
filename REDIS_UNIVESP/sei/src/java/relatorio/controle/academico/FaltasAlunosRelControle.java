package relatorio.controle.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.HorarioTurmaDisciplinaProgramadaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.academico.FaltasAlunosRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.academico.FaltasAlunosRel;

@SuppressWarnings("unchecked")
@Controller("FaltasAlunosRelControle")
@Scope("viewScope")
@Lazy
public class FaltasAlunosRelControle extends SuperControleRelatorio {

	private UnidadeEnsinoVO unidadeEnsino;
	private UnidadeEnsinoCursoVO unidadeEnsinoCurso;
	private TurmaVO turma;
	private DisciplinaVO disciplina;
	private List listaSelectItemUnidadeEnsino;
	private List listaSelectItemCurso;
	private List listaSelectItemTurma;
	private List listaSelectItemDisciplina;
	private Boolean trazerSomenteAlunosAtivos;
	private String campoConsultaTurma;
	private String valorConsultaTurma;
	private List listaConsultaTurma;
	private String campoConsultaCurso;
	private String valorConsultaCurso;
	private List listaConsultaCurso;
	private String ano;
	private String semestre;

	public FaltasAlunosRelControle() throws Exception {
		incializarDados();
		setMensagemID("msg_entre_prmconsulta");
	}

	public void limparListasConsultas() {
		setTurma(null);
		setDisciplina(null);
		setUnidadeEnsinoCurso(null);
		getListaConsultaCurso().clear();
		getListaConsultaTurma().clear();
		getListaSelectItemDisciplina().clear();
	}

	private void incializarDados() {
		montarListaSelectItemUnidadeEnsino();
	}

	public void imprimirPDF() {
		String titulo = null;
		CursoVO curso = null;
		String design = null;
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "FaltasAlunosRelControle", "Inicializando Geração de Relatório Faltas de Alunos", "Emitindo Relatório");
			FaltasAlunosRel.validarDados(getUnidadeEnsino(), getUnidadeEnsinoCurso(), getTurma(), getDisciplina());
			titulo = "Faltas de Alunos";
			if (getUnidadeEnsinoCurso().getCurso().getCodigo() != 0) {
				curso = getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(getUnidadeEnsinoCurso().getCurso().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, getUsuarioLogado());
			}
			design = FaltasAlunosRel.getDesignIReportRelatorio();
			List<FaltasAlunosRelVO> listaObjetos = getFacadeFactory().getFaltasAlunosRelFacade().consultaFaltasAlunosRelatorio(getUnidadeEnsino().getCodigo(), getTurma().getCodigo(), curso.getCodigo(), getDisciplina().getCodigo(), getAno(), getSemestre(), false, getUsuarioLogado());
			if (listaObjetos.isEmpty()) {
				setMensagemID("msg_relatorio_sem_dados");
			} else {
				setMensagemDetalhada("", "");
				getSuperParametroRelVO().setNomeDesignIreport(design);
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio(titulo);
				getSuperParametroRelVO().setListaObjetos(listaObjetos);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(FaltasAlunosRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setUnidadeEnsino(getUnidadeEnsinoLogado().getNome());
				if (!getUnidadeEnsino().getCodigo().equals(0)) {
					setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
					getSuperParametroRelVO().adicionarLogoUnidadeEnsinoSelecionada(getUnidadeEnsino());
				}
				realizarImpressaoRelatorio();
				removerObjetoMemoria(this);
				setMensagemID("msg_relatorio_ok");
				incializarDados();
				// apresentarRelatorioObjetos(
				// getIdEntidade(),
				// titulo,
				// nomeEntidade,
				// curso.getNome(),
				// "PDF",
				// "",
				// design,
				// getUsuarioLogado().getNome(),
				// "", listaObjetos, getCaminhoBaseRelatorio());
			}
			registrarAtividadeUsuario(getUsuarioLogado(), "FaltasAlunosRelControle", "Finalizando Geração de Relatório Faltas de Alunos", "Emitindo Relatório");
		} catch (Exception e) {
			setFazerDownload(false);
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			titulo = null;
			removerObjetoMemoria(curso);
			design = null;
		}
	}

	public List getListaSelectItemSemestre() {
		List lista = new ArrayList(0);
		lista.add(new SelectItem("1", "1º"));
		lista.add(new SelectItem("2", "2º"));
		return lista;
	}

//	public void montarListaSelectItemUnidadeEnsino() {
//		List<UnidadeEnsinoVO> resultadoConsulta = null;
//		try {
//			resultadoConsulta = consultarUnidadeEnsinoPorNome("");
//			setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
//		} catch (Exception e) {
//			setMensagemDetalhada("msg_erro", e.getMessage());
//		} finally {
//			Uteis.liberarListaMemoria(resultadoConsulta);
//		}
//	}
	
    public void montarListaSelectItemUnidadeEnsino() {
        try {
            List<UnidadeEnsinoVO> resultadoConsulta = consultarUnidadeEnsinoPorNome("");
            setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
            removerObjetoMemoria(resultadoConsulta);
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
			if (getUnidadeEnsino().getCodigo() == 0) {
				setListaSelectItemTurma(new ArrayList(0));
				setListaSelectItemCurso(new ArrayList(0));
			}
			resultadoConsulta = consultarCursoPorUnidadeEnsino(getUnidadeEnsino().getCodigo());
			getListaSelectItemTurma().clear();
			getListaSelectItemCurso().clear();
			i = resultadoConsulta.iterator();
			getListaSelectItemCurso().add(new SelectItem(0, ""));
			while (i.hasNext()) {
				UnidadeEnsinoCursoVO unidadeEnsinoCurso = (UnidadeEnsinoCursoVO) i.next();
				getListaSelectItemCurso().add(new SelectItem(unidadeEnsinoCurso.getCodigo(), unidadeEnsinoCurso.getCurso().getNome() + " - " + unidadeEnsinoCurso.getTurno().getNome()));
				removerObjetoMemoria(unidadeEnsinoCurso);
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_entre_prmconsulta");
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	private List<UnidadeEnsinoCursoVO> consultarCursoPorUnidadeEnsino(Integer codigoUnidadeEnsino) throws Exception {
		List<UnidadeEnsinoCursoVO> lista = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorCodigoUnidadeEnsino(codigoUnidadeEnsino, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
		return lista;
	}

	private List<TurmaVO> consultarTurmasPorCurso(String nomeCurso) throws Exception {
		List<TurmaVO> lista = getFacadeFactory().getTurmaFacade().consultarPorUnidadeEnsinoCursoTurno(getUnidadeEnsino().getCodigo(), getUnidadeEnsinoCurso().getCurso().getCodigo(), getUnidadeEnsinoCurso().getTurno().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
		return lista;
	}

	private List<DisciplinaVO> consultarDisciplinasPorTurma(int codigoTurma) throws Exception {
		List<DisciplinaVO> lista = getFacadeFactory().getDisciplinaFacade().consultarPorTurmaOuTurmaAgrupada(getTurma().getIdentificadorTurma(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
		return lista;
	}

	public void montarListaSelectItemDisciplina() throws Exception {
		try {
			getListaSelectItemDisciplina().clear();
			if (!getTurma().getIdentificadorTurma().equals("")) {
				setTurma(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(getTurma().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
				List<HorarioTurmaDisciplinaProgramadaVO> horarioTurmaDisciplinaProgramadaVOs = getFacadeFactory().getHorarioTurmaFacade().consultarHorarioTurmaDisciplinaProgramadaPorTurma(getTurma().getCodigo(), false, true, 0);
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

	public void montarListaSelectItemTurma() throws Exception {
		List<TurmaVO> resultadoConsulta = null;
		try {
			if (getUnidadeEnsinoCurso().getCodigo().intValue() == 0) {
				setListaSelectItemTurma(new ArrayList(0));
			}
			setUnidadeEnsinoCurso(getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorChavePrimaria(getUnidadeEnsinoCurso().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			resultadoConsulta = consultarTurmasPorCurso(getUnidadeEnsinoCurso().getCurso().getNome());
			setListaSelectItemTurma(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "identificadorTurma"));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_entre_prmconsulta");
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
		}
	}

	public void consultarTurma() {
		try {
			if (getUnidadeEnsino().getCodigo() == 0) {
				throw new Exception("Informe a Unidade de Ensino.");
			}
			super.consultar();
			List objs = new ArrayList(0);
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaCurso(getValorConsultaTurma(), getUnidadeEnsinoCurso().getCurso().getCodigo(), getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaTurma(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarTurmaPorChavePrimaria() {
		try {
			if (getUnidadeEnsino().getCodigo() == 0) {
				throw new Exception("Informe a Unidade de Ensino.");
			}
			setTurma(getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(getTurma(), getTurma().getIdentificadorTurma(), getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			if (getTurma().getCodigo() == 0) {
				setTurma(null);
			}
			montarListaSelectItemDisciplina();
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setTurma(null);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarTurma() {
		try {
			TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
			setTurma(obj);
			if (getUnidadeEnsinoCurso().getCurso().getCodigo() == 0) {
				setUnidadeEnsinoCurso(getFacadeFactory().getUnidadeEnsinoCursoFacade().consultaRapidaPorCursoUnidadeTurno(getTurma().getCurso().getCodigo(), getUnidadeEnsino().getCodigo(), getTurma().getTurno().getCodigo(), getUsuarioLogado()));
			}
			montarListaSelectItemDisciplina();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparTurma() {
		try {
			setDisciplina(null);
			setTurma(null);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List getTipoConsultaComboTurma() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("identificadorTurma", "Identificador"));
		return itens;
	}

	public void consultarCurso() {
		try {
			if (getUnidadeEnsino().getCodigo() == 0) {
				throw new Exception("Informe a Unidade de Ensino.");
			}
			List objs = new ArrayList(0);
			if (getCampoConsultaCurso().equals("codigo")) {
				if (getValorConsultaCurso().equals("")) {
					setValorConsultaCurso("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaCurso());
				objs = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorCodigoCursoUnidadeEnsino(new Integer(valorInt), getUnidadeEnsino().getCodigo(),"", false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			if (getCampoConsultaCurso().equals("nome")) {
				objs = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorNomeCursoUnidadeEnsino(getValorConsultaCurso(), getUnidadeEnsino().getCodigo(), false,  false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			setListaConsultaCurso(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}

	public void selecionarCurso() throws Exception {
		try {
			UnidadeEnsinoCursoVO obj = (UnidadeEnsinoCursoVO) context().getExternalContext().getRequestMap().get("unidadeensinocursoItens");
			setUnidadeEnsinoCurso(obj);
			limparTurma();
			getListaConsultaTurma().clear();
			getListaSelectItemDisciplina().clear();
		} catch (Exception e) {
		}
	}

	public void limparCurso() throws Exception {
		try {
			setUnidadeEnsinoCurso(null);
		} catch (Exception e) {
		}
	}

	public List getTipoConsultaComboCurso() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
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

	public List getListaSelectItemTurma() {
		if (listaSelectItemTurma == null) {
			listaSelectItemTurma = new ArrayList(0);
		}
		return listaSelectItemTurma;
	}

	public void setListaSelectItemTurma(List listaSelectItemTurma) {
		this.listaSelectItemTurma = listaSelectItemTurma;
	}

	public void setTrazerSomenteAlunosAtivos(Boolean trazerSomenteAlunosAtivos) {
		this.trazerSomenteAlunosAtivos = trazerSomenteAlunosAtivos;
	}

	public Boolean getTrazerSomenteAlunosAtivos() {
		if (trazerSomenteAlunosAtivos == null) {
			trazerSomenteAlunosAtivos = true;
		}
		return trazerSomenteAlunosAtivos;
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

	public UnidadeEnsinoCursoVO getUnidadeEnsinoCurso() {
		if (unidadeEnsinoCurso == null) {
			unidadeEnsinoCurso = new UnidadeEnsinoCursoVO();
		}
		return unidadeEnsinoCurso;
	}

	public void setUnidadeEnsinoCurso(UnidadeEnsinoCursoVO unidadeEnsinoCurso) {
		this.unidadeEnsinoCurso = unidadeEnsinoCurso;
	}

	public TurmaVO getTurma() {
		if (turma == null) {
			turma = new TurmaVO();
		}
		return turma;
	}

	public void setTurma(TurmaVO turma) {
		this.turma = turma;
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
			listaSelectItemDisciplina = new ArrayList(0);
		}
		return listaSelectItemDisciplina;
	}

	public void setListaSelectItemDisciplina(List listaSelectItemDisciplina) {
		this.listaSelectItemDisciplina = listaSelectItemDisciplina;
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
			listaConsultaTurma = new ArrayList(0);
		}
		return listaConsultaTurma;
	}

	public void setListaConsultaTurma(List listaConsultaTurma) {
		this.listaConsultaTurma = listaConsultaTurma;
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

	public String getAno() {
		if (ano == null) {
			ano = "";
		}
		return ano;
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
	
	public boolean getIsApresentarAno() {
		if (getUnidadeEnsinoCurso().getCurso().getSemestral() || getUnidadeEnsinoCurso().getCurso().getAnual()) {
			setAno(Uteis.getAnoDataAtual4Digitos());
			return true;
		} else {
			setAno("");
			setSemestre("");
		}
		return false;
	}

	public boolean getIsApresentarSemestre() {
		if (getUnidadeEnsinoCurso().getCurso().getSemestral()) {
			setSemestre(Uteis.getSemestreAtual());
			return true;
		} else {
			setSemestre("");
		}
		return false;
	}
	
}
