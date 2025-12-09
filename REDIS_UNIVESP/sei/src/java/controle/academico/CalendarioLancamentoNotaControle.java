package controle.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.CalendarioLancamentoNotaVO;
import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

@Controller("CalendarioLancamentoNotaControle")
@Scope("viewScope")
@Lazy
public class CalendarioLancamentoNotaControle extends SuperControle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4835656321242590046L;
	private CalendarioLancamentoNotaVO calendarioLancamentoNota;
	private List<SelectItem> listaSelectItemConfiguracaoAcademico;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private List<SelectItem> listaSelectItemAno;
	private List<SelectItem> listaSelectItemSemestre;
	private List<PessoaVO> listaConsultaProfessor;
	private List<UnidadeEnsinoCursoVO> listaConsultaCurso;
	private String valorConsultaCurso;
	private String campoConsultaCurso;
	private String valorConsultaProfessor;
	private String campoConsultaProfessor;
	private List<TurmaVO> listaConsultaTurma;
	private String valorConsultaTurma;
	private String campoConsultaTurma;
	private String campoConsultaDisciplina;
	private String valorConsultaDisciplina;
	private List<DisciplinaVO> listaConsultaDisciplina;
	private List<SelectItem> tipoConsultaComboDisciplina;
	private List<SelectItem> listaSelectItemCalendarioPor;
	private List<SelectItem> listaSelectItemPeriodicidade;
	private Boolean atualizarCalendarioAtividadeMatriculaComPeriodo;
	private Integer filtroCodigo;

	private Boolean emCadastro;
	private Boolean isDisciplinaComposta;

	public String novo() {
		setCalendarioLancamentoNota(null);
		setEmCadastro(false);
		setListaSelectItemSemestre(null);
		setIsDisciplinaComposta(Boolean.FALSE);
		limparMensagem();
		return Uteis.getCaminhoRedirecionamentoNavegacao("calendarioLancamentoNotaForm.xhtml");
	}

	public String excluir() {
		try {
			getFacadeFactory().getCalendarioLancamentoNotaFacade().excluir(getCalendarioLancamentoNota(), true, getUsuarioLogado());
			novo();
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}

		return Uteis.getCaminhoRedirecionamentoNavegacao("calendarioLancamentoNotaForm.xhtml");
	}

	public void realizarClonagemCalendarioNota() {
		try {
			CalendarioLancamentoNotaVO calendarioLancamentoNotaClone = (CalendarioLancamentoNotaVO) Uteis.clonar(getCalendarioLancamentoNota());
			calendarioLancamentoNotaClone.setNovoObj(true);
			calendarioLancamentoNotaClone.setCodigo(null);
			setCalendarioLancamentoNota(calendarioLancamentoNotaClone);
			setIsDisciplinaComposta(getFacadeFactory().getTurmaDisciplinaFacade().consultarDisciplinaCompostaTurmaDisciplina(getCalendarioLancamentoNota().getTurma().getCodigo(), getCalendarioLancamentoNota().getDisciplina().getCodigo()));
			setMensagemID("msg_dados_clonados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public String editar() {
		try {
			setCalendarioLancamentoNota((CalendarioLancamentoNotaVO) getRequestMap().get("calendarioItens"));
			getCalendarioLancamentoNota().setConfiguracaoAcademico(getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorChavePrimaria(getCalendarioLancamentoNota().getConfiguracaoAcademico().getCodigo(), getUsuarioLogado()));
			setIsDisciplinaComposta(getFacadeFactory().getTurmaDisciplinaFacade().consultarDisciplinaCompostaTurmaDisciplina(getCalendarioLancamentoNota().getTurma().getCodigo(), getCalendarioLancamentoNota().getDisciplina().getCodigo()));
			setListaSelectItemSemestre(null);
			setEmCadastro(true);
			limparMensagem();
		} catch (Exception e) {
			setEmCadastro(false);
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}

		return Uteis.getCaminhoRedirecionamentoNavegacao("calendarioLancamentoNotaForm.xhtml");
	}

	public void persitir() {
		try {
			getFacadeFactory().getCalendarioLancamentoNotaFacade().persistir(getCalendarioLancamentoNota(), true, getUsuarioLogado(), getCalendarioLancamentoNota().getAtualizarCalendarioAtividadeMatriculaComPeriodo());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public String consultar() {
		try {
			getControleConsultaOtimizado().setLimitePorPagina(10);
			getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getCalendarioLancamentoNotaFacade().consultar(getFiltroCodigo(), getCalendarioLancamentoNota().getUnidadeEnsino().getCodigo(), getCalendarioLancamentoNota().getUnidadeEnsinoCurso().getCodigo(), getCalendarioLancamentoNota().getTurma().getCodigo(), getCalendarioLancamentoNota().getProfessor().getCodigo(), getCalendarioLancamentoNota().getDisciplina().getCodigo(), getCalendarioLancamentoNota().getConfiguracaoAcademico().getCodigo(), getCalendarioLancamentoNota().getAno(), getCalendarioLancamentoNota().getSemestre(), true, getUsuarioLogado(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset()));
			getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getCalendarioLancamentoNotaFacade().consultarTotalRegistro(getFiltroCodigo(), getCalendarioLancamentoNota().getUnidadeEnsino().getCodigo(), getCalendarioLancamentoNota().getUnidadeEnsinoCurso().getCodigo(), getCalendarioLancamentoNota().getTurma().getCodigo(), getCalendarioLancamentoNota().getProfessor().getCodigo(), getCalendarioLancamentoNota().getDisciplina().getCodigo(), getCalendarioLancamentoNota().getConfiguracaoAcademico().getCodigo(), getCalendarioLancamentoNota().getAno(), getCalendarioLancamentoNota().getSemestre()));
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("calendarioLancamentoNotaCons.xhtml");
	}

	public void paginarConsulta(DataScrollEvent event) {
		getControleConsultaOtimizado().setPage(event.getPage());
		getControleConsultaOtimizado().setPaginaAtual(event.getPage());
		consultar();
	}

	public String inicializarConsulta() {
		setCalendarioLancamentoNota(null);
		setControleConsultaOtimizado(null);
		setListaSelectItemSemestre(null);
		return Uteis.getCaminhoRedirecionamentoNavegacao("calendarioLancamentoNotaCons.xhtml");
	}

	public void consultarCalendarioLancamentoNota() {
		try {

			CalendarioLancamentoNotaVO obj = getFacadeFactory().getCalendarioLancamentoNotaFacade().consultarPorConfiguracaoAcademicoAnoSemestre(null, getCalendarioLancamentoNota().getUnidadeEnsino().getCodigo(), getCalendarioLancamentoNota().getUnidadeEnsinoCurso().getCodigo(), getCalendarioLancamentoNota().getTurma().getCodigo(), getCalendarioLancamentoNota().getProfessor().getCodigo(), getCalendarioLancamentoNota().getDisciplina().getCodigo(), getCalendarioLancamentoNota().getConfiguracaoAcademico().getCodigo(), getCalendarioLancamentoNota().getAno(), getCalendarioLancamentoNota().getSemestre(), true, getUsuarioLogado());
			if (obj == null) {
				getCalendarioLancamentoNota().setConfiguracaoAcademico(getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorChavePrimaria(getCalendarioLancamentoNota().getConfiguracaoAcademico().getCodigo(), getUsuarioLogado()));
			} else {
				setCalendarioLancamentoNota(obj);
			}
			setEmCadastro(true);
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setEmCadastro(false);
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public CalendarioLancamentoNotaVO getCalendarioLancamentoNota() {
		if (calendarioLancamentoNota == null) {
			calendarioLancamentoNota = new CalendarioLancamentoNotaVO();
		}
		return calendarioLancamentoNota;
	}

	public void setCalendarioLancamentoNota(CalendarioLancamentoNotaVO calendarioLancamentoNota) {
		this.calendarioLancamentoNota = calendarioLancamentoNota;
	}

	public void consultarCurso() {
		try {
			
			getListaConsultaCurso().clear();
			if (getCampoConsultaCurso().equals("curso")) {

				setListaConsultaCurso(getFacadeFactory().getUnidadeEnsinoCursoFacade().consultaRapidaPorNomeCursoUnidadeEnsino(getValorConsultaCurso(), getCalendarioLancamentoNota().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			}
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void selecionarCurso() {
		UnidadeEnsinoCursoVO obj = (UnidadeEnsinoCursoVO) getRequestMap().get("cursoItens");
		try {
			if (obj.getCurso().getPeriodicidade().equals("IN")) {
				setMensagemDetalhada("msg_erro", "Este recurso não permite cadastro para curso ou turno de periodicidade integral.", Uteis.ERRO);
			} else {
				obj.setCurso(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(obj.getCurso().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSCONSULTA, false, getUsuarioLogado()));
				getCalendarioLancamentoNota().setUnidadeEnsinoCurso(obj);
				getCalendarioLancamentoNota().getConfiguracaoAcademico().setCodigo(obj.getCurso().getConfiguracaoAcademico().getCodigo());
				limparConfiguracaoAcademico();
				limparTurma();
				setListaSelectItemSemestre(null);
				getCalendarioLancamentoNota().setPeriodicidade(obj.getCurso().getPeriodicidade());
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			e.printStackTrace();
		}
	}
	
	public boolean apresentarProfessorExclusivoLancamentoDeNota() {
		return getCalendarioLancamentoNota().getCalendarioPor().equals("TU") && getIsDisciplinaComposta();
	}

	public void limparConfiguracaoAcademico() {
		try {
			if (getCalendarioLancamentoNota().getConfiguracaoAcademico().getCodigo() > 0) {
				getCalendarioLancamentoNota().setConfiguracaoAcademico(getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorChavePrimaria(getCalendarioLancamentoNota().getConfiguracaoAcademico().getCodigo(), getUsuarioLogado()));
			} else {
				getCalendarioLancamentoNota().setConfiguracaoAcademico(null);
			}
			setEmCadastro(true);
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setEmCadastro(false);
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void limparUnidadeEnsino() {

		limparCurso();
		limparTurma();
	}

	private List<SelectItem> opcaoConsultaCurso;

	public List<SelectItem> getOpcaoConsultaCurso() {
		if (opcaoConsultaCurso == null) {
			opcaoConsultaCurso = new ArrayList<SelectItem>(0);
			opcaoConsultaCurso.add(new SelectItem("curso", "Curso"));
		}
		return opcaoConsultaCurso;
	}

	public void limparCurso() {		
		getCalendarioLancamentoNota().setUnidadeEnsinoCurso(null);
		limparPeriodicidade();
	}

	private List<SelectItem> opcaoConsultaTurma;

	public List<SelectItem> getOpcaoConsultaTurma() {
		if (opcaoConsultaTurma == null) {
			opcaoConsultaTurma = new ArrayList<SelectItem>(0);
			opcaoConsultaTurma.add(new SelectItem("identificacaoTurma", "Identificação Turma"));
			opcaoConsultaTurma.add(new SelectItem("curso", "Curso"));
		}
		return opcaoConsultaTurma;
	}

	public void consultarTurmaPorIdentificador() {
		try {			
			getCalendarioLancamentoNota().setTurma(getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaEspecifico(getCalendarioLancamentoNota().getTurma(), getCalendarioLancamentoNota().getTurma().getIdentificadorTurma(), getCalendarioLancamentoNota().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			getCalendarioLancamentoNota().setUnidadeEnsinoCurso(null);
			setListaSelectItemSemestre(null);
			setListaSelectItemUnidadeEnsino(null);
			getCalendarioLancamentoNota().getUnidadeEnsinoCurso().setCurso(getCalendarioLancamentoNota().getTurma().getCurso());
			getCalendarioLancamentoNota().setPeriodicidade(getCalendarioLancamentoNota().getTurma().getPeriodicidade());
			getCalendarioLancamentoNota().setUnidadeEnsino(getCalendarioLancamentoNota().getTurma().getUnidadeEnsino());
			getCalendarioLancamentoNota().getUnidadeEnsinoCurso().getCurso().setConfiguracaoAcademico(getCalendarioLancamentoNota().getCurso().getConfiguracaoAcademico());
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	
	public void consultarTurma() {
		try {
			
			getListaConsultaTurma().clear();
			if (getCampoConsultaTurma().equals("identificacaoTurma")) {
				setListaConsultaTurma(getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaUnidadeEnsino(getValorConsultaTurma(), getCalendarioLancamentoNota().getUnidadeEnsino().getCodigo(), false, getUsuarioLogado()));
			}

			if (getCampoConsultaTurma().equals("curso")) {
				setListaConsultaTurma(getFacadeFactory().getTurmaFacade().consultaRapidaNomeCurso(getValorConsultaTurma(), getCalendarioLancamentoNota().getUnidadeEnsino().getCodigo(), null, null, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			}
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	
	public void selecionarTurma() {
		TurmaVO turma = (TurmaVO) getRequestMap().get("turmaItens");
		if (turma.getCurso().getPeriodicidade().equals("IN")) {
			setMensagemDetalhada("msg_erro", "Este recurso não permite cadastro para curso ou turno de periodicidade integral.", Uteis.ERRO);
		} else {
			getCalendarioLancamentoNota().setUnidadeEnsinoCurso(null);
			getCalendarioLancamentoNota().setUnidadeEnsino(turma.getUnidadeEnsino());
			getCalendarioLancamentoNota().setTurma(turma);
			getCalendarioLancamentoNota().getUnidadeEnsinoCurso().setCurso(turma.getCurso());
			getCalendarioLancamentoNota().setConfiguracaoAcademico(turma.getCurso().getConfiguracaoAcademico());
			getCalendarioLancamentoNota().setPeriodicidade(turma.getPeriodicidade());
			setListaSelectItemSemestre(null);
		}
	}

	public void limparTurma() {
		getCalendarioLancamentoNota().setTurma(null);
		getCalendarioLancamentoNota().setProfessor(null);
		getListaConsultaProfessor().clear();
		limparPeriodicidade();
	}

	public void consultarProfessor() {
		try {
			getListaConsultaProfessor().clear();
			setListaConsultaProfessor(getFacadeFactory().getPessoaFacade().consultaRapidaResumidaPorNome(getValorConsultaProfessor(), "PR", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}

	}

	private List<SelectItem> opcaoConsultaProfessor;

	public List<SelectItem> getOpcaoConsultaProfessor() {
		if (opcaoConsultaProfessor == null) {
			opcaoConsultaProfessor = new ArrayList<SelectItem>(0);
			opcaoConsultaProfessor.add(new SelectItem("nome", "Nome"));
		}
		return opcaoConsultaProfessor;
	}

	@SuppressWarnings("unchecked")
	public void montarListaConsultaProfessor() {
		try {
			
			getListaConsultaProfessor().clear();
			setListaConsultaProfessor(getFacadeFactory().getPessoaFacade().consultarProfessoresDaTurmaPorTurmaAgrupada(getCalendarioLancamentoNota().getTurma().getCodigo(), getCalendarioLancamentoNota().getUnidadeEnsino().getCodigo(), getCalendarioLancamentoNota().getSemestre(), getCalendarioLancamentoNota().getAno(), false, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void selecionarProfessor() {
		getCalendarioLancamentoNota().setProfessor((PessoaVO) getRequestMap().get("professorItens"));
		limparMensagem();
	}

	public void limparProfessor() {
		getCalendarioLancamentoNota().setProfessor(null);
		limparMensagem();
	}

	public void selecionarProfessorExclusivo() {
		getCalendarioLancamentoNota().setProfessorExclusivoLancamentoDeNota((PessoaVO) getRequestMap().get("professorItens"));
		limparMensagem();
	}
	
	public void limparProfessorExclusivo() {
		getCalendarioLancamentoNota().setProfessorExclusivoLancamentoDeNota(null);
		limparMensagem();
	}

	// consultarProfessoresDaTurmaPorTurmaAgrupada

	public List<SelectItem> getListaSelectItemConfiguracaoAcademico() {
		if (listaSelectItemConfiguracaoAcademico == null) {
			listaSelectItemConfiguracaoAcademico = new ArrayList<SelectItem>(0);

			try {
				List<ConfiguracaoAcademicoVO> configuracaoAcademicoVOs = getFacadeFactory().getConfiguracaoAcademicoFacade().consultarConfiguracaoAcademicoDeTodasConfiguracoesNivelCombobox(false, getUsuarioLogado());
				listaSelectItemConfiguracaoAcademico.add(new SelectItem(0, ""));
				for (ConfiguracaoAcademicoVO obj : configuracaoAcademicoVOs) {
					listaSelectItemConfiguracaoAcademico.add(new SelectItem(obj.getCodigo(), obj.getNome()));
				}
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}

		}
		return listaSelectItemConfiguracaoAcademico;
	}

	public void setListaSelectItemConfiguracaoAcademico(List<SelectItem> listaSelectItemConfiguracaoAcademico) {
		this.listaSelectItemConfiguracaoAcademico = listaSelectItemConfiguracaoAcademico;
	}

	public List<SelectItem> getListaSelectItemAno() {
		if (listaSelectItemAno == null) {
			listaSelectItemAno = new ArrayList<SelectItem>(0);
			listaSelectItemAno.add(new SelectItem("", ""));
			for (int x = 2012; x <= Uteis.getAnoData(new Date()) + 2; x++) {
				listaSelectItemAno.add(new SelectItem(String.valueOf(x), String.valueOf(x)));
			}
		}
		return listaSelectItemAno;
	}

	public void setListaSelectItemAno(List<SelectItem> listaSelectItemAno) {
		this.listaSelectItemAno = listaSelectItemAno;
	}

	public List<SelectItem> getListaSelectItemSemestre() {
		if (listaSelectItemSemestre == null) {
			listaSelectItemSemestre = new ArrayList<SelectItem>(0);
			if(!Uteis.isAtributoPreenchido(getCalendarioLancamentoNota().getTurma().getCodigo()) 
					&& !Uteis.isAtributoPreenchido(getCalendarioLancamentoNota().getUnidadeEnsinoCurso().getCurso().getCodigo())){
				listaSelectItemSemestre.add(new SelectItem("", ""));
			}
			listaSelectItemSemestre.add(new SelectItem("1", "1ª"));
			listaSelectItemSemestre.add(new SelectItem("2", "2ª"));
		}
		return listaSelectItemSemestre;
	}

	public void setListaSelectItemSemestre(List<SelectItem> listaSelectItemSemestre) {
		this.listaSelectItemSemestre = listaSelectItemSemestre;
	}

	public Boolean getEmCadastro() {
		if (emCadastro == null) {
			emCadastro = false;
		}
		return emCadastro;
	}

	public void setEmCadastro(Boolean emCadastro) {
		this.emCadastro = emCadastro;
	}

	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList<SelectItem>(0);
			try {
				List<UnidadeEnsinoVO> unidadeEnsinoVOs = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome("", getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
				listaSelectItemUnidadeEnsino.add(new SelectItem(0, ""));
				for (UnidadeEnsinoVO unidadeEnsinoVO : unidadeEnsinoVOs) {
					listaSelectItemUnidadeEnsino.add(new SelectItem(unidadeEnsinoVO.getCodigo(), unidadeEnsinoVO.getNome()));
				}
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}

		}
		return listaSelectItemUnidadeEnsino;
	}

	public List<PessoaVO> getListaConsultaProfessor() {
		if (listaConsultaProfessor == null) {
			listaConsultaProfessor = new ArrayList<PessoaVO>(0);
		}
		return listaConsultaProfessor;
	}

	public void setListaConsultaProfessor(List<PessoaVO> listaConsultaProfessor) {
		this.listaConsultaProfessor = listaConsultaProfessor;
	}

	public List<UnidadeEnsinoCursoVO> getListaConsultaCurso() {
		if (listaConsultaCurso == null) {
			listaConsultaCurso = new ArrayList<UnidadeEnsinoCursoVO>(0);
		}
		return listaConsultaCurso;
	}

	public void setListaConsultaCurso(List<UnidadeEnsinoCursoVO> listaConsultaCurso) {
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

	public String getCampoConsultaCurso() {
		if (campoConsultaCurso == null) {
			campoConsultaCurso = "";
		}
		return campoConsultaCurso;
	}

	public void setCampoConsultaCurso(String campoConsultaCurso) {
		this.campoConsultaCurso = campoConsultaCurso;
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

	public String getCampoConsultaTurma() {
		if (campoConsultaTurma == null) {
			campoConsultaTurma = "";
		}
		return campoConsultaTurma;
	}

	public void setCampoConsultaTurma(String campoConsultaTurma) {
		this.campoConsultaTurma = campoConsultaTurma;
	}

	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public void setOpcaoConsultaCurso(List<SelectItem> opcaoConsultaCurso) {
		this.opcaoConsultaCurso = opcaoConsultaCurso;
	}

	public void setOpcaoConsultaTurma(List<SelectItem> opcaoConsultaTurma) {
		this.opcaoConsultaTurma = opcaoConsultaTurma;
	}

	public String getValorConsultaProfessor() {
		if (valorConsultaProfessor == null) {
			valorConsultaProfessor = "";
		}
		return valorConsultaProfessor;
	}

	public void setValorConsultaProfessor(String valorConsultaProfessor) {
		this.valorConsultaProfessor = valorConsultaProfessor;
	}

	public String getCampoConsultaProfessor() {
		if (campoConsultaProfessor == null) {
			campoConsultaProfessor = "";
		}
		return campoConsultaProfessor;
	}

	public void setCampoConsultaProfessor(String campoConsultaProfessor) {
		this.campoConsultaProfessor = campoConsultaProfessor;
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

	public void setListaConsultaDisciplina(
			List<DisciplinaVO> listaConsultaDisciplina) {
		this.listaConsultaDisciplina = listaConsultaDisciplina;
	}

	public List<SelectItem> getTipoConsultaComboDisciplina() {
		if (tipoConsultaComboDisciplina == null) {
			tipoConsultaComboDisciplina = new ArrayList<SelectItem>(0);
			tipoConsultaComboDisciplina.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboDisciplina.add(new SelectItem("codigo", "Código"));
		}
		return tipoConsultaComboDisciplina;
	}
	
	public void consultarDisciplina() {
		try {
			List<DisciplinaVO> objs = new ArrayList<DisciplinaVO>(0);
			if (getCampoConsultaDisciplina().equals("codigo")) {
				if (getValorConsultaDisciplina().equals("")) {
					setValorConsultaDisciplina("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaDisciplina());
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorCodigo(valorInt, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaDisciplina().equals("nome")) {
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorNome(getValorConsultaDisciplina(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
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
			setIsDisciplinaComposta(getFacadeFactory().getTurmaDisciplinaFacade().consultarDisciplinaCompostaTurmaDisciplina(getCalendarioLancamentoNota().getTurma().getCodigo(), obj.getCodigo()));
			getCalendarioLancamentoNota().setDisciplina(obj);
		} catch (Exception e) {
		}
	}

	public void limparDisciplina() throws Exception {
		try {
			getCalendarioLancamentoNota().setDisciplina(null);
		} catch (Exception e) {
		}
	}
	
	public List<SelectItem> getListaSelectItemCalendarioPor() {
		if (listaSelectItemCalendarioPor == null) {
			listaSelectItemCalendarioPor = new ArrayList<SelectItem>(0);
			listaSelectItemCalendarioPor.add(new SelectItem("UE", "Unidade de Ensino"));
			listaSelectItemCalendarioPor.add(new SelectItem("CU", "Curso"));
			listaSelectItemCalendarioPor.add(new SelectItem("TU", "Turma"));
			listaSelectItemCalendarioPor.add(new SelectItem("PR", "Professor"));
		}
		return listaSelectItemCalendarioPor;
	}

	public void setListaSelectItemCalendarioPor(
			List<SelectItem> listaSelectItemCalendarioPor) {
		this.listaSelectItemCalendarioPor = listaSelectItemCalendarioPor;
	}
	
	public String getEstiloProfessorConformeTipoCalendario() {
		if (getCalendarioLancamentoNota().getCalendarioPor().equals("PR")) {
			return "camposSomenteLeituraObrigatorio";
		} else {
			return "camposSomenteLeitura";
		}
	}
	
	public List<SelectItem> getListaSelectItemPeriodicidade() {
		if (listaSelectItemPeriodicidade == null) {
			listaSelectItemPeriodicidade = new ArrayList<SelectItem>(0);
			listaSelectItemPeriodicidade.add(new SelectItem("", ""));
			listaSelectItemPeriodicidade.add(new SelectItem("AN", "Anual"));
			listaSelectItemPeriodicidade.add(new SelectItem("SE", "Semestral"));
		}
		return listaSelectItemPeriodicidade;
	}

	public void setListaSelectItemPeriodicidade(List<SelectItem> listaSelectItemPeriodicidade) {
		this.listaSelectItemPeriodicidade = listaSelectItemPeriodicidade;
	}
	
	public void limparPeriodicidade() {
		getCalendarioLancamentoNota().setPeriodicidade("");
	}

	public Boolean getAtualizarCalendarioAtividadeMatriculaComPeriodo() {
		if (atualizarCalendarioAtividadeMatriculaComPeriodo == null) {
			atualizarCalendarioAtividadeMatriculaComPeriodo = false;
		}
		return atualizarCalendarioAtividadeMatriculaComPeriodo;
	}

	public void setAtualizarCalendarioAtividadeMatriculaComPeriodo(Boolean atualizarCalendarioAtividadeMatriculaComPeriodo) {
		this.atualizarCalendarioAtividadeMatriculaComPeriodo = atualizarCalendarioAtividadeMatriculaComPeriodo;
	}

	public Boolean getIsDisciplinaComposta() {
		if (isDisciplinaComposta == null) {
			isDisciplinaComposta = Boolean.FALSE;
		}
		return isDisciplinaComposta;
	}

	public void setIsDisciplinaComposta(Boolean isDisciplinaComposta) {
		this.isDisciplinaComposta = isDisciplinaComposta;
	}

	public Integer getFiltroCodigo() {
		if(filtroCodigo == null) {
			filtroCodigo = 0;
		}
		return filtroCodigo;
	}

	public void setFiltroCodigo(Integer filtroCodigo) {
		this.filtroCodigo = filtroCodigo;
	}
	
	public void limparFiltroCodigo(){
		setFiltroCodigo(0);
		consultar();
	}
	
	
}
