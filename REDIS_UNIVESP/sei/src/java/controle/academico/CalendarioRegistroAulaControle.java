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
import negocio.comuns.academico.CalendarioRegistroAulaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

@Controller("CalendarioRegistroAulaControle")
@Scope("viewScope")
@Lazy
public class CalendarioRegistroAulaControle extends SuperControle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4835656321242590046L;
	private CalendarioRegistroAulaVO calendarioRegistroAula;	
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

	private Boolean emCadastro;

	public String novo() {
		setCalendarioRegistroAula(null);
		setEmCadastro(false);
		limparMensagem();
		return Uteis.getCaminhoRedirecionamentoNavegacao("calendarioRegistroAulaForm.xhtml");
	}

	public String excluir() {
		try {
			getFacadeFactory().getCalendarioRegistroAulaFacade().excluir(getCalendarioRegistroAula(), true, getUsuarioLogado());
			novo();
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}

		return Uteis.getCaminhoRedirecionamentoNavegacao("calendarioRegistroAulaForm.xhtml");
	}

	public void realizarClonagemCalendarioRegistroAula() {
		try {
			getFacadeFactory().getCalendarioRegistroAulaFacade().realizarClonagemCalendarioRegistroAula(getCalendarioRegistroAula(), getUsuarioLogado());
			setMensagemID("msg_dados_clonados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public String editar() {
		try {
			setCalendarioRegistroAula((CalendarioRegistroAulaVO) getRequestMap().get("calendarioItens"));			
			setEmCadastro(true);
			limparMensagem();
		} catch (Exception e) {
			setEmCadastro(false);
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}

		return Uteis.getCaminhoRedirecionamentoNavegacao("calendarioRegistroAulaForm.xhtml");
	}

	public void persitir() {
		try {
			getFacadeFactory().getCalendarioRegistroAulaFacade().persistir(getCalendarioRegistroAula(), true, getUsuarioLogado());
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
			getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getCalendarioRegistroAulaFacade().consultar(getCalendarioRegistroAula().getUnidadeEnsino().getCodigo(), getCalendarioRegistroAula().getUnidadeEnsinoCurso().getCodigo(), getCalendarioRegistroAula().getTurma().getCodigo(), getCalendarioRegistroAula().getProfessor().getCodigo(),  getCalendarioRegistroAula().getAno(),  true, getUsuarioLogado(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset()));
			getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getCalendarioRegistroAulaFacade().consultarTotalRegistro(getCalendarioRegistroAula().getUnidadeEnsino().getCodigo(), getCalendarioRegistroAula().getUnidadeEnsinoCurso().getCodigo(), getCalendarioRegistroAula().getTurma().getCodigo(), getCalendarioRegistroAula().getProfessor().getCodigo(), getCalendarioRegistroAula().getAno()));
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("calendarioRegistroAulaCons.xhtml");
	}

	public void paginarConsulta(DataScrollEvent event) {
		getControleConsultaOtimizado().setPage(event.getPage());
		getControleConsultaOtimizado().setPaginaAtual(event.getPage());
		consultar();
	}

	public String inicializarConsulta() {
		setCalendarioRegistroAula(null);
		setControleConsultaOtimizado(null);
		return Uteis.getCaminhoRedirecionamentoNavegacao("calendarioRegistroAulaCons.xhtml");
	}

	public CalendarioRegistroAulaVO getCalendarioRegistroAula() {
		if (calendarioRegistroAula == null) {
			calendarioRegistroAula = new CalendarioRegistroAulaVO();
		}
		return calendarioRegistroAula;
	}

	public void setCalendarioRegistroAula(CalendarioRegistroAulaVO calendarioRegistroAula) {
		this.calendarioRegistroAula = calendarioRegistroAula;
	}

	public void consultarCurso() {
		try {
			getListaConsultaCurso().clear();
			if (getCampoConsultaCurso().equals("curso")) {

				setListaConsultaCurso(getFacadeFactory().getUnidadeEnsinoCursoFacade().consultaRapidaPorNomeCursoUnidadeEnsino(getValorConsultaCurso(), getCalendarioRegistroAula().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			}
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void consultarCursoCons() {
		try {

			getListaConsultaCurso().clear();
			if (getCampoConsultaCurso().equals("curso")) {
				setListaConsultaCurso(getFacadeFactory().getUnidadeEnsinoCursoFacade().consultaRapidaPorNomeCursoUnidadeEnsino(getValorConsultaCurso(), getCalendarioRegistroAula().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			}
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void selecionarCurso() {
		UnidadeEnsinoCursoVO obj = (UnidadeEnsinoCursoVO) getRequestMap().get("cursoItens");
		try {
			obj.setCurso(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(obj.getCurso().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSCONSULTA, false, getUsuarioLogado()));
			getCalendarioRegistroAula().setUnidadeEnsinoCurso(obj);			
			
			getCalendarioRegistroAula().setTurma(null);
			getCalendarioRegistroAula().setProfessor(null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		getCalendarioRegistroAula().setUnidadeEnsinoCurso(null);
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
//			getFacadeFactory().getCalendarioRegistroAulaFacade().validarDados(getCalendarioRegistroAula());
			getCalendarioRegistroAula().setTurma(getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaEspecifico(getCalendarioRegistroAula().getTurma(), getCalendarioRegistroAula().getTurma().getIdentificadorTurma(), getCalendarioRegistroAula().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			getCalendarioRegistroAula().getUnidadeEnsinoCurso().setCurso(getCalendarioRegistroAula().getTurma().getCurso());
			getCalendarioRegistroAula().getUnidadeEnsinoCurso().setTurno(getCalendarioRegistroAula().getTurma().getTurno());

			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void consultarTurmaPorIdentificadorCons() {
		try {
			getCalendarioRegistroAula().setTurma(getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaEspecifico(getCalendarioRegistroAula().getTurma(), getCalendarioRegistroAula().getTurma().getIdentificadorTurma(), getCalendarioRegistroAula().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			getCalendarioRegistroAula().setUnidadeEnsinoCurso(null);

			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void consultarTurma() {
		try {
			getListaConsultaTurma().clear();
			if (getCampoConsultaTurma().equals("identificacaoTurma")) {
				setListaConsultaTurma(getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaUnidadeEnsino(getValorConsultaTurma(), getCalendarioRegistroAula().getUnidadeEnsino().getCodigo(), false, getUsuarioLogado()));
			}

			if (getCampoConsultaTurma().equals("curso")) {
				setListaConsultaTurma(getFacadeFactory().getTurmaFacade().consultaRapidaNomeCurso(getValorConsultaTurma(), getCalendarioRegistroAula().getUnidadeEnsino().getCodigo(), null, null, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			}
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void consultarTurmaCons() {
		try {
			getListaConsultaTurma().clear();
			if (getCampoConsultaTurma().equals("identificacaoTurma")) {
				setListaConsultaTurma(getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaUnidadeEnsino(getValorConsultaTurma(), getCalendarioRegistroAula().getUnidadeEnsino().getCodigo(), false, getUsuarioLogado()));
			}
			if (getCampoConsultaTurma().equals("curso")) {
				setListaConsultaTurma(getFacadeFactory().getTurmaFacade().consultaRapidaNomeCurso(getValorConsultaTurma(), getCalendarioRegistroAula().getUnidadeEnsino().getCodigo(), null, null, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			}
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void selecionarTurma() {
		TurmaVO turma = (TurmaVO) getRequestMap().get("turmaItens");
		getCalendarioRegistroAula().getUnidadeEnsinoCurso().setCurso(turma.getCurso());
		getCalendarioRegistroAula().getUnidadeEnsinoCurso().setTurno(turma.getTurno());
		getCalendarioRegistroAula().setTurma(turma);

	}

	public void limparTurma() {
		getCalendarioRegistroAula().setTurma(null);
		getCalendarioRegistroAula().setProfessor(null);
		getCalendarioRegistroAula().setUnidadeEnsinoCurso(null);
		getListaConsultaProfessor().clear();
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
//			getFacadeFactory().getCalendarioRegistroAulaFacade().validarDados(getCalendarioRegistroAula());
			getListaConsultaProfessor().clear();
			setListaConsultaProfessor(getFacadeFactory().getPessoaFacade().consultarProfessoresDaTurmaPorTurmaAgrupada(getCalendarioRegistroAula().getTurma().getCodigo(), getCalendarioRegistroAula().getUnidadeEnsino().getCodigo(), Uteis.getSemestreAtual(), getCalendarioRegistroAula().getAno(), false, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void selecionarProfessor() {
		getCalendarioRegistroAula().setProfessor((PessoaVO) getRequestMap().get("professorItens"));
		limparMensagem();
	}

	public void limparProfessor() {
		getCalendarioRegistroAula().setProfessor(null);
		limparMensagem();
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
			listaSelectItemSemestre.add(new SelectItem("", ""));
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

}
