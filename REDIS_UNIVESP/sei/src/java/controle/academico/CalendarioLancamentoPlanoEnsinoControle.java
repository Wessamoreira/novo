package controle.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.CalendarioLancamentoPlanoEnsinoVO;
import negocio.comuns.academico.CalendarioLancamentoPlanoEnsinoVO.EnumCampoCalendarioPor;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.academico.enumeradores.SemestreEnum;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.TipoPessoa;

@Controller("CalendarioLancamentoPlanoEnsinoControle")
@Scope("viewScope")
@Lazy
public class CalendarioLancamentoPlanoEnsinoControle extends SuperControle {

	private static final long serialVersionUID = 4835656321242590046L;
	
	private static final String TELA_CONS = "calendarioLancamentoPlanoEnsinoCons.xhtml";
	private static final String TELA_FORM = "calendarioLancamentoPlanoEnsinoForm.xhtml";
	private static final String CONTEXT_PARA_EDICAO = "calendarioItens";

	private CalendarioLancamentoPlanoEnsinoVO calendarioLancamentoPlanoEnsinoVO;
	
	private List<SelectItem> listaSelectItemAno;
	private List<SelectItem> tipoConsultaComboDisciplina;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private List<SelectItem> listaSelectItemTurno;

	private List<SelectItem> opcaoConsultaProfessor;
	private List<SelectItem> opcaoConsultaCurso;
	
	private DataModelo dataModeloCurso;
	private DataModelo dataModeloTurma;
	private DataModelo dataModeloProfessor;
	
	private String campoConsultaDisciplina;
	private String valorConsultaDisciplina;
	private List<DisciplinaVO> listaDisciplinas;
	private List<SelectItem> listaSelectItemCalendarioPor;
	private List<SelectItem> periodicidadeEnum;
	private List<SelectItem> semestreEnum;

	public String novo() {
		liberarBackingBeanMemoria();
		limparMensagem();
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}

	public void persitir() {
		try {
			getFacadeFactory().getCalendarioLancamentoPlanoEnsinoInterfaceFacade().persistir(getCalendarioLancamentoPlanoEnsinoVO(), true, getUsuarioLogado());
			setMensagemID(MSG_TELA.msg_dados_gravados.name(), Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}
	
	public String editar() {
		try {
			setCalendarioLancamentoPlanoEnsinoVO((CalendarioLancamentoPlanoEnsinoVO) getRequestMap().get(CONTEXT_PARA_EDICAO));

			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}

		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}

	public void excluir() {
		try {
			getFacadeFactory().getCalendarioLancamentoPlanoEnsinoInterfaceFacade().excluir(getCalendarioLancamentoPlanoEnsinoVO(), true, getUsuarioLogado());
			novo();
			setMensagemID(MSG_TELA.msg_dados_excluidos.name(), Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}

	public String consultar() {
		try {
			getControleConsultaOtimizado().setLimitePorPagina(10);
			getFacadeFactory().getCalendarioLancamentoPlanoEnsinoInterfaceFacade().consultar(getControleConsultaOtimizado(), getCalendarioLancamentoPlanoEnsinoVO());
			setMensagemID(MSG_TELA.msg_dados_consultados.name(), Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_CONS);
	}

	public void clonar() {
		CalendarioLancamentoPlanoEnsinoVO obj = (CalendarioLancamentoPlanoEnsinoVO) Uteis.clonar(getCalendarioLancamentoPlanoEnsinoVO());
		obj.setCodigo(0);
		setCalendarioLancamentoPlanoEnsinoVO(obj);
	}

	public void scrollerListener(DataScrollEvent event) {
		getControleConsultaOtimizado().setPage(event.getPage());
		getControleConsultaOtimizado().setPaginaAtual(event.getPage());
		consultar();
	}

	public void scrollerListenerProfessor(DataScrollEvent event) {
		getDataModeloProfessor().setPage(event.getPage());
		getDataModeloProfessor().setPaginaAtual(event.getPage());
		consultarProfessor();
	}

	public void scrollerListenerCurso(DataScrollEvent event) {
		getDataModeloCurso().setPage(event.getPage());
		getDataModeloCurso().setPaginaAtual(event.getPage());
		consultarCurso();
	}

	public String inicializarConsulta() {
		setCalendarioLancamentoPlanoEnsinoVO(null);
		setControleConsultaOtimizado(null);
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_CONS);
	}

	public void consultarCurso() {
		try {
			getFacadeFactory().getCursoFacade().consultarCursoDataModelo(getDataModeloCurso(), getUsuarioLogado(), getCalendarioLancamentoPlanoEnsinoVO().getPeriodicidade());

			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void selecionarCurso() {
		try {
			CursoVO obj = (CursoVO) getRequestMap().get("cursoItens");
			getCalendarioLancamentoPlanoEnsinoVO().setCursoVO(obj);
			
			montarTurnoPorCurso(obj);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	private void montarTurnoPorCurso(CursoVO obj) throws Exception {
		if (Uteis.isAtributoPreenchido(obj)) {
			List<TurnoVO> turnos = getFacadeFactory().getTurnoFacade().consultarPorCodigoCurso(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());

			getListaSelectItemTurno().add(new SelectItem("", ""));
			for (TurnoVO turnoVO : turnos) {
				getListaSelectItemTurno().add(new SelectItem(turnoVO.getCodigo(), turnoVO.getNome()));
			}
		}
	}

	public void limparUnidadeEnsino() {
		limparCurso();
	}
	
	public void selecionarCalendarioPor() {
		limparCurso();
		limparDisciplina();
		limparTurnos();
		limparProfessor();
		limparMensagem();
	}
	
	public void limparTurnos() {
		getCalendarioLancamentoPlanoEnsinoVO().setTurnoVO(new TurnoVO());
		setListaSelectItemTurno(new ArrayList<>());
	}

	public List<SelectItem> getOpcaoConsultaCurso() {
		if (opcaoConsultaCurso == null) {
			opcaoConsultaCurso = new ArrayList<SelectItem>(0);
			opcaoConsultaCurso.add(new SelectItem("nome", "Nome"));
		}
		return opcaoConsultaCurso;
	}

	public void limparCurso() {
		getCalendarioLancamentoPlanoEnsinoVO().setCursoVO(new CursoVO());
		setDataModeloCurso(new DataModelo());
		getDataModeloCurso().preencherDadosParaConsulta(false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		limparMensagem();
	}

	public void consultarProfessor() {
		try {
			getFacadeFactory().getPessoaFacade().consultaPessoaDataModeloPorNome(getDataModeloProfessor(), TipoPessoa.PROFESSOR, getUsuarioLogado());
			setMensagemID(MSG_TELA.msg_dados_consultados.name(), Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}

	public List<SelectItem> getOpcaoConsultaProfessor() {
		if (opcaoConsultaProfessor == null) {
			opcaoConsultaProfessor = new ArrayList<SelectItem>(0);
			opcaoConsultaProfessor.add(new SelectItem("nome", "Nome"));
		}
		return opcaoConsultaProfessor;
	}

	public void selecionarProfessor() {
		getCalendarioLancamentoPlanoEnsinoVO().setProfessor((PessoaVO) getRequestMap().get("professorItens"));
		limparMensagem();
	}

	public void limparProfessor() {
		getCalendarioLancamentoPlanoEnsinoVO().setProfessor(null);
		setDataModeloProfessor(new DataModelo());
		getDataModeloProfessor().preencherDadosParaConsulta(false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
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
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
			}

		}
		return listaSelectItemUnidadeEnsino;
	}

	public List<SelectItem> getTipoConsultaComboDisciplina() {
		if (tipoConsultaComboDisciplina == null) {
			tipoConsultaComboDisciplina = new ArrayList<SelectItem>(0);
			tipoConsultaComboDisciplina.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboDisciplina.add(new SelectItem("codigo", "Código"));
		}
		return tipoConsultaComboDisciplina;
	}
	
	/*public void consultarDisciplina() {
		try {
			if (getCampoConsultaDisciplina().equals("codigo")) {
				if (getValorConsultaDisciplina().equals("")) {
					getDataModeloDisciplina().setValorConsulta("0");
				}
				getDataModeloDisciplina().setListaConsulta(getFacadeFactory().getDisciplinaFacade().consultarPorCodigo(Integer.parseInt(getValorConsultaDisciplina()), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
				getDataModeloDisciplina().setTotalRegistrosEncontrados(getDataModeloDisciplina().getListaConsulta().size());
			}

			if (getCampoConsultaDisciplina().equals("nome")) {
				getFacadeFactory().getDisciplinaFacade().consultar(getDataModeloDisciplina());
			}

			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}*/
	
	public void consultarDisciplina() {
		try {
			if (getCampoConsultaDisciplina().equals("codigo")) {
				if (getValorConsultaDisciplina().equals("")) {
					setValorConsultaDisciplina("0");
				}
				setListaDisciplinas(getFacadeFactory().getDisciplinaFacade().consultarPorCodigoDisciplinaUnidadeEnsinoCodigoCursoCodigoTurma(Integer.parseInt(getValorConsultaDisciplina()), getCalendarioLancamentoPlanoEnsinoVO().getUnidadeEnsinoVO().getCodigo(), getCalendarioLancamentoPlanoEnsinoVO().getCursoVO().getCodigo(),
						null, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}

			if (getCampoConsultaDisciplina().equals("nome")) {
				setListaDisciplinas(getFacadeFactory().getDisciplinaFacade().consultarPorNomeDisciplinaUnidadeEnsinoCodigoCursoCodigoTurma(
						getValorConsultaDisciplina(), getCalendarioLancamentoPlanoEnsinoVO().getUnidadeEnsinoVO().getCodigo(), getCalendarioLancamentoPlanoEnsinoVO().getCursoVO().getCodigo(),
						null, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}

			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void selecionarDisciplina() throws Exception {
		try {
			DisciplinaVO obj = (DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplinaItens");
			getCalendarioLancamentoPlanoEnsinoVO().setDisciplinaVO(obj);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void limparDisciplina() {
		getCalendarioLancamentoPlanoEnsinoVO().setDisciplinaVO(null);
		setListaDisciplinas(new ArrayList<>(0));
	}
	
	public boolean apresentarClonar() {
		return Uteis.isAtributoPreenchido(getCalendarioLancamentoPlanoEnsinoVO().getCodigo());
	}
	
	public boolean apresentarCamposCurso() {
		return getCalendarioLancamentoPlanoEnsinoVO().getCalendarioPor().equals("CURSO");
	}

	public boolean apresentarCamposProfessor() {
		return getCalendarioLancamentoPlanoEnsinoVO().getCalendarioPor().equals("PROFESSOR");
	}

	public boolean apresentarCamposPeriodicidadeSemestral() {
		return getCalendarioLancamentoPlanoEnsinoVO().getPeriodicidade().equals(PeriodicidadeEnum.SEMESTRAL);
	}

	public void limparPeriodicidade() {
		getCalendarioLancamentoPlanoEnsinoVO().setPeriodicidade(PeriodicidadeEnum.SEMESTRAL);
	}

	public CalendarioLancamentoPlanoEnsinoVO getCalendarioLancamentoPlanoEnsinoVO() {
		if (calendarioLancamentoPlanoEnsinoVO == null) {
			calendarioLancamentoPlanoEnsinoVO = new CalendarioLancamentoPlanoEnsinoVO();
		}
		return calendarioLancamentoPlanoEnsinoVO;
	}

	public DataModelo getDataModeloCurso() {
		if (dataModeloCurso == null) {
			dataModeloCurso = new DataModelo();
			dataModeloCurso.preencherDadosParaConsulta(false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		}
		return dataModeloCurso;
	}

	public DataModelo getDataModeloTurma() {
		if (dataModeloTurma == null) {
			dataModeloTurma = new DataModelo();
			dataModeloTurma.preencherDadosParaConsulta(false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		}
		return dataModeloTurma;
	}

	public DataModelo getDataModeloProfessor() {
		if (dataModeloProfessor == null) {
			dataModeloProfessor = new DataModelo();
			dataModeloProfessor.preencherDadosParaConsulta(false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		}
		return dataModeloProfessor;
	}

	public void setCalendarioLancamentoPlanoEnsinoVO(CalendarioLancamentoPlanoEnsinoVO calendarioLancamentoPlanoEnsinoVO) {
		this.calendarioLancamentoPlanoEnsinoVO = calendarioLancamentoPlanoEnsinoVO;
	}

	public void setTipoConsultaComboDisciplina(List<SelectItem> tipoConsultaComboDisciplina) {
		this.tipoConsultaComboDisciplina = tipoConsultaComboDisciplina;
	}

	public void setDataModeloCurso(DataModelo dataModeloCurso) {
		this.dataModeloCurso = dataModeloCurso;
	}

	public void setDataModeloTurma(DataModelo dataModeloTurma) {
		this.dataModeloTurma = dataModeloTurma;
	}

	public void setDataModeloProfessor(DataModelo dataModeloProfessor) {
		this.dataModeloProfessor = dataModeloProfessor;
	}

	public void setOpcaoConsultaProfessor(List<SelectItem> opcaoConsultaProfessor) {
		this.opcaoConsultaProfessor = opcaoConsultaProfessor;
	}

	public List<SelectItem> getListaSelectItemTurno() {
		if (listaSelectItemTurno == null) {
			listaSelectItemTurno = new ArrayList<>(0);
		}
		return listaSelectItemTurno;
	}

	public void setListaSelectItemTurno(List<SelectItem> listaSelectItemTurno) {
		this.listaSelectItemTurno = listaSelectItemTurno;
	}

	public List<DisciplinaVO> getListaDisciplinas() {
		if (listaDisciplinas == null) {
			listaDisciplinas = new ArrayList<>(0);
		}
		return listaDisciplinas;
	}

	public void setListaDisciplinas(List<DisciplinaVO> listaDisciplinas) {
		this.listaDisciplinas = listaDisciplinas;
	}

	public String getCampoConsultaDisciplina() {
		if (campoConsultaDisciplina == null) {
			campoConsultaDisciplina = "";
		}
		return campoConsultaDisciplina;
	}

	public String getValorConsultaDisciplina() {
		if (valorConsultaDisciplina == null) {
			valorConsultaDisciplina = "";
		}
		return valorConsultaDisciplina;
	}

	public void setCampoConsultaDisciplina(String campoConsultaDisciplina) {
		this.campoConsultaDisciplina = campoConsultaDisciplina;
	}

	public void setValorConsultaDisciplina(String valorConsultaDisciplina) {
		this.valorConsultaDisciplina = valorConsultaDisciplina;
	}
	
	public List<SelectItem> getListaSelectItemCalendarioPor() {
		if (listaSelectItemCalendarioPor == null) {
			listaSelectItemCalendarioPor = new ArrayList<>(0);
			listaSelectItemCalendarioPor.add(new SelectItem(EnumCampoCalendarioPor.UNIDADE_ENSINO, "Unidade Ensino"));
			listaSelectItemCalendarioPor.add(new SelectItem(EnumCampoCalendarioPor.PROFESSOR, "Professor"));
			listaSelectItemCalendarioPor.add(new SelectItem(EnumCampoCalendarioPor.CURSO, "Curso"));
		}
		return listaSelectItemCalendarioPor;
	}
	
	public List<SelectItem> getPeriodicidadeEnum() {
		if (periodicidadeEnum == null) {
			periodicidadeEnum = new ArrayList<>(0);
			periodicidadeEnum.add(new SelectItem(PeriodicidadeEnum.ANUAL, PeriodicidadeEnum.ANUAL.getDescricao()));
			periodicidadeEnum.add(new SelectItem(PeriodicidadeEnum.INTEGRAL, PeriodicidadeEnum.INTEGRAL.getDescricao()));
			periodicidadeEnum.add(new SelectItem(PeriodicidadeEnum.SEMESTRAL, PeriodicidadeEnum.SEMESTRAL.getDescricao()));
		}
		return periodicidadeEnum;
	}
	
	public void setPeriodicidadeEnum(List<SelectItem> periodicidadeEnum) {
		this.periodicidadeEnum = periodicidadeEnum;
	}
	
	public List<SelectItem> getSemestreEnum() {
		if (semestreEnum == null) {
			semestreEnum = new ArrayList<>(0);
			semestreEnum.add(new SelectItem(SemestreEnum.PRIMEIRO_SEMESTRE, SemestreEnum.PRIMEIRO_SEMESTRE.getDescricao()));
			semestreEnum.add(new SelectItem(SemestreEnum.SEGUNDO_SEMESTRE, SemestreEnum.SEGUNDO_SEMESTRE.getDescricao()));
		}
		return semestreEnum;
	}
	
	public void setSemestreEnum(List<SelectItem> semestreEnum) {
		this.semestreEnum = semestreEnum;
	}
	
}
