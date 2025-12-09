package controle.ead;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.ead.AvaliacaoOnlineMatriculaVO;
import negocio.comuns.ead.CalendarioAtividadeMatriculaVO;
import negocio.comuns.ead.enumeradores.TipoCalendarioAtividadeMatriculaEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

/**
 * @author Victor Hugo 03/11/2014
 */
@Controller("GestaoAvaliacaoOnlineControle")
@Scope("viewScope")
public class GestaoAvaliacaoOnlineControle extends SuperControle {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6626061918248054886L;
	private String tipoFiltroConsulta;
	private AvaliacaoOnlineMatriculaVO avaliacaoOnlineMatriculaVO;
	private List<CalendarioAtividadeMatriculaVO> listaConsultaCalendario;
	private List<CalendarioAtividadeMatriculaVO> listaConsultaCalendarioExcluir;
	private CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO;
	private String situacaoAvaliacao;
	private String ano;
	private String semestre;
	private Boolean filtroMatricula;
	private Boolean filtroCurso;
	private Boolean filtroTurma;
	private Boolean filtroDisciplina;
	private Boolean filtroUnidadeEnsino;
	private List<SelectItem> listaSelectItemDisciplinasAluno;
	private Date periodoInicio;
	private Date periodoFim;
	private Boolean botaoConsultar;
	private Boolean marcarTodosCalendarios;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private List<SelectItem> listaSelectItemTurma;
	private List<SelectItem> listaSelectItemCursos;
	private String abrirModalExcluir;
	private TipoCalendarioAtividadeMatriculaEnum tipoCalendarioAtividade;
	private List<SelectItem> listaSelectItemTipoCalendarioAtividade;
	private String tituloRea;
	private AvaliacaoOnlineMatriculaVO avaliacaoOnlineMatriculaRecuperacaoDados;
	private List<MatriculaVO> matriculaVOs;
	private List<CursoVO> cursoVOs;
	private List<TurmaVO> turmaVOs;
	private List<DisciplinaVO> listaConsultaDisciplina;
	private String campoConsultaDisciplina;
	private String valorConsultaDisciplina;

	@PostConstruct
	public void init() {
		if (getUsuarioLogado().getIsApresentarVisaoProfessor()) { 
			setTipoFiltroConsulta("disciplina");
			apresentarFiltros();
			montarListaSelectItemDisciplinasProfessor();
		} else if (getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
		
		}else{
			montarListaSelectItemUnidadesEnsino();	
		}	
		aplicarRegraPeriodicidadeAnoSemestre();
		montarListaSelectItemTipoCalendarioAtividade();
		setPeriodoInicio(UteisData.getPrimeiroDataMes(new Date()));
		setPeriodoFim(new Date());
	}

	public String consultar() {
		try {
			limparMensagem();
			validarDadosConsulta();
			setListaConsultaCalendario(getFacadeFactory().getCalendarioAtividadeMatriculaInterfaceFacade()
					.consultarCalendarioAtividadeMatriculaTelaGestaoAvaliacaoOnline(getAvaliacaoOnlineMatriculaVO().getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().getMatricula(), 
							getAvaliacaoOnlineMatriculaVO().getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().getUnidadeEnsino().getCodigo(), 
							getAvaliacaoOnlineMatriculaVO().getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().getCurso().getCodigo(), 
							getAvaliacaoOnlineMatriculaVO().getMatriculaPeriodoTurmaDisciplinaVO().getTurma().getCodigo(), getAvaliacaoOnlineMatriculaVO().getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina().getCodigo(), 
							getAno(), getSemestre(), getPeriodoInicio(), getPeriodoFim(), getSituacaoAvaliacao(), 
							getTipoCalendarioAtividade(), getTituloRea().trim(), getUsuarioLogado()));
			if (getListaConsultaCalendario().isEmpty()) {
				throw new Exception(UteisJSF.internacionalizar("msg_relatorio_vazio"));
			}
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return "";
	}

	public void selecionarCalendarioParaExclusao() {
		try {
			CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO = (CalendarioAtividadeMatriculaVO) context().getExternalContext().getRequestMap().get("calendario");
			getListaConsultaCalendarioExcluir().clear();
			getListaConsultaCalendarioExcluir().add(calendarioAtividadeMatriculaVO);
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void selecionarTodosCalendarioMarcadoParaExclusao() {
		try {
			getListaConsultaCalendarioExcluir().clear();
			for (CalendarioAtividadeMatriculaVO obj : getListaConsultaCalendario()) {
				if(obj.getSelecionarAtividade()){
					getListaConsultaCalendarioExcluir().add(obj);		
				}
			}
			if(getListaConsultaCalendarioExcluir().isEmpty()){
				throw new Exception("Não foi marcado nenhuma Avaliação Online para exclusão.");
			}
			setAbrirModalExcluir("RichFaces.$('panelExcluir').show()");
			inicializarMensagemVazia();
		} catch (Exception e) {
			setAbrirModalExcluir("");
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	
	public String excluir() {
		try {
			getFacadeFactory().getCalendarioAtividadeMatriculaInterfaceFacade().alterarCalendarioAtividadeMatriculaAndExlcuirAvaliacaoOnlineMatricula(getListaConsultaCalendarioExcluir(), true, getUsuarioLogado());
			consultar();
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return "";
	}
	
	
	public String voltar() {
		recuperarDadosAvaliacaoOnlineVO();
		if (getUsuarioLogado().getIsApresentarVisaoProfessor()) { 
			return Uteis.getCaminhoRedirecionamentoNavegacao("gestaoAvaliacaoOnlineProfessorCons.xhtml");	
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("gestaoAvaliacaoOnlineCons.xhtml");
	}

	public String visualizarGabarito() {
		try {
			CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO = (CalendarioAtividadeMatriculaVO) context().getExternalContext().getRequestMap().get("calendario");
			copiarDadosAvaliacaoOnlineVO();
			setCalendarioAtividadeMatriculaVO(calendarioAtividadeMatriculaVO);
			setAvaliacaoOnlineMatriculaVO(getFacadeFactory().getAvaliacaoOnlineMatriculaInterfaceFacade().realizarVisualizacaoGabarito(calendarioAtividadeMatriculaVO.getAvaliacaoOnlineMatriculaVO(), getUsuarioLogado()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
			return Uteis.getCaminhoRedirecionamentoNavegacao("gestaoAvaliacaoOnlineProfessorGabaritoCons.xhtml");	
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("gestaoAvaliacaoOnlineGabaritoForm.xhtml");
	}

	public void consultarAlunoPorMatricula() {
		try {
			MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(getAvaliacaoOnlineMatriculaVO().getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().getMatricula(), this.getUnidadeEnsinoLogado().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado());
			if (objAluno.getMatricula().equals("")) {
				throw new Exception("Aluno de matrícula " + getAvaliacaoOnlineMatriculaVO().getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
			}
			getAvaliacaoOnlineMatriculaVO().getMatriculaPeriodoTurmaDisciplinaVO().setMatriculaObjetoVO(objAluno);
			aplicarRegraPeriodicidadeAnoSemestre();
			setMensagemDetalhada("");
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			getAvaliacaoOnlineMatriculaVO().getMatriculaPeriodoTurmaDisciplinaVO().setMatriculaObjetoVO(new MatriculaVO());
		}
	}

	public void consultarAlunoPorMatriculaOuNomeAluno() {
		try {
			if (getControleConsulta().getValorConsulta().equals("")) {
				throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
			}
			if (getControleConsulta().getCampoConsulta().equals("matricula")) {
				setMatriculaVOs(getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatricula(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("nomePessoa")) {
				setMatriculaVOs(getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado()));
			}
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			getMatriculaVOs().clear();
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void selecionarAluno() {
		try {
			MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItem");
			obj = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula(), obj.getUnidadeEnsino().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado());
			getAvaliacaoOnlineMatriculaVO().getMatriculaPeriodoTurmaDisciplinaVO().setMatriculaObjetoVO(obj);
			aplicarRegraPeriodicidadeAnoSemestre();
			getMatriculaVOs().clear();
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void consultarDadosCurso() {
		try {
			super.consultar();
			if (getControleConsulta().getCampoConsulta().equals("nome")) {
				setCursoVOs(getFacadeFactory().getCursoFacade().consultaRapidaPorNome(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, Boolean.TRUE, getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("nrRegistroInterno")) {
				setCursoVOs(getFacadeFactory().getCursoFacade().consultaRapidaPorNrRegistroInterno(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeAreaConhecimento")) {
				setCursoVOs(getFacadeFactory().getCursoFacade().consultaRapidaPorNomeAreaConhecimento(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("nivelEducacional")) {
				setCursoVOs(getFacadeFactory().getCursoFacade().consultaRapidaPorNrNivelEducacional(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("unidadeEnsino")) {
				setCursoVOs(getFacadeFactory().getCursoFacade().consultaRapidaPorNomeUnidadeEnsino(getControleConsulta().getValorConsulta(), 0, true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			setMensagemDetalhada("");
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			getCursoVOs().clear();
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void selecionarCurso() {
		try {
			CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItem");
			obj = getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, false, getUsuarioLogado());
			getAvaliacaoOnlineMatriculaVO().getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().setCurso(obj);
			aplicarRegraPeriodicidadeAnoSemestre();
			getCursoVOs().clear();				
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void consultarTurma() {
		try {
			if (getUnidadeEnsinoLogado().getCodigo().equals(0) && getAvaliacaoOnlineMatriculaVO().getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().getUnidadeEnsino().getCodigo().equals(0)) {
				throw new Exception("Por Favor Informe a Unidade de Ensino.");
			}
			int codigoUnidadeEnsino = !getUnidadeEnsinoLogado().getCodigo().equals(0) ? 
					getUnidadeEnsinoLogado().getCodigo() : getAvaliacaoOnlineMatriculaVO().getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().getUnidadeEnsino().getCodigo();
			if (getControleConsulta().getCampoConsulta().equals("identificadorTurma")) {
				setTurmaVOs(getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(getControleConsulta().getValorConsulta(), codigoUnidadeEnsino, false, false, "", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeTurno")) {
				setTurmaVOs(getFacadeFactory().getTurmaFacade().consultaRapidaPorTurno(getControleConsulta().getValorConsulta(), codigoUnidadeEnsino, false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeCurso")) {
				setTurmaVOs(getFacadeFactory().getTurmaFacade().consultaRapidaNomeCurso(getControleConsulta().getValorConsulta(), codigoUnidadeEnsino, false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			getTurmaVOs().clear();
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void montarTurma()  {
		try {
			if (!getUnidadeEnsinoLogado().getCodigo().equals(0) || !getAvaliacaoOnlineMatriculaVO().getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().getUnidadeEnsino().getCodigo().equals(0)) {
				int codigoUnidadeEnsino = !getUnidadeEnsinoLogado().getCodigo().equals(0) ? 
						getUnidadeEnsinoLogado().getCodigo() : getAvaliacaoOnlineMatriculaVO().getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().getUnidadeEnsino().getCodigo();
				getAvaliacaoOnlineMatriculaVO().getMatriculaPeriodoTurmaDisciplinaVO().setTurma(getFacadeFactory().getTurmaFacade()
						.consultarPorCodigoUnidadeEnsinoIdentificadorTurma(codigoUnidadeEnsino, 
								getAvaliacaoOnlineMatriculaVO().getMatriculaPeriodoTurmaDisciplinaVO().getTurma().getIdentificadorTurma(), 
								false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado()));
				aplicarRegraPeriodicidadeAnoSemestre();				
				setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
			} else {
				throw new Exception("Por Favor Informe a Unidade de Ensino.");
			}
		} catch (Exception e) {
			getAvaliacaoOnlineMatriculaVO().getMatriculaPeriodoTurmaDisciplinaVO().setTurma(new TurmaVO());
			setListaSelectItemDisciplinasAluno(new ArrayList<>(0));
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void selecionarTurma()  {
		try {
			TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItem");
			obj = getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(obj.getCodigo(), 0, getUsuarioLogado());
			getAvaliacaoOnlineMatriculaVO().getMatriculaPeriodoTurmaDisciplinaVO().setTurma(obj);
			aplicarRegraPeriodicidadeAnoSemestre();			
			getTurmaVOs().clear();
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void apresentarFiltros() {
		getCursoVOs().clear();
		getTurmaVOs().clear();
		getMatriculaVOs().clear();
		if (tipoFiltroConsulta.equals("selecione")) {
			setFiltroMatricula(false);
			setFiltroTurma(false);
			setFiltroCurso(false);
			setFiltroDisciplina(false);
			setFiltroUnidadeEnsino(false);
			setBotaoConsultar(false);
			setMensagemID("msg_segmentacao_vazio");
			setMensagemDetalhada("");
			limparDadosAluno();
			limparDadosCurso();
			limparDadosTurma();
			getListaConsultaCalendario().clear();
			setIconeMensagem("");
			setTipoCalendarioAtividade(null);
		} else if (tipoFiltroConsulta.equals("matricula")) {
			setFiltroMatricula(true);
			setFiltroTurma(false);
			setFiltroCurso(false);
			setFiltroUnidadeEnsino(false);
			setFiltroDisciplina(true);
			setBotaoConsultar(true);
			limparDadosAluno();
			limparDadosCurso();
			limparDadosTurma();
			getListaConsultaCalendario().clear();
		} else if (tipoFiltroConsulta.equals("curso")) {
			setFiltroCurso(true);
			setFiltroMatricula(false);
			setFiltroTurma(false);
			setFiltroUnidadeEnsino(false);
			setFiltroDisciplina(true);
			setBotaoConsultar(true);
			limparDadosAluno();
			limparDadosCurso();
			limparDadosTurma();
			getListaConsultaCalendario().clear();			
			if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
				montarListaSelectItemCursosProfessor();
				getListaSelectItemDisciplinasAluno().clear();
				getAvaliacaoOnlineMatriculaVO().getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().getUnidadeEnsino().setCodigo(getUnidadeEnsinoLogado().getCodigo());
			}
		} else if (tipoFiltroConsulta.equals("turma")) {
			setFiltroTurma(true);
			setFiltroMatricula(false);
			setFiltroCurso(false);
			setFiltroUnidadeEnsino(false);
			setFiltroDisciplina(true);
			setBotaoConsultar(true);
			limparDadosAluno();
			limparDadosCurso();
			limparDadosTurma();
			getListaConsultaCalendario().clear();
			if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
				montarListaSelectItemTurmasProfessor();	
				getAvaliacaoOnlineMatriculaVO().getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().getUnidadeEnsino().setCodigo(getUnidadeEnsinoLogado().getCodigo());
			}
		} else if (tipoFiltroConsulta.equals("unidadeEnsino")) {
			setFiltroUnidadeEnsino(true);
			setFiltroTurma(false);
			setFiltroMatricula(false);
			setFiltroCurso(false);
			setFiltroDisciplina(true);
			setBotaoConsultar(true);
			limparDadosAluno();
			limparDadosCurso();
			limparDadosTurma();
			if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
				montarListaSelectItemDisciplinasUnidadeEnsinoProfessor();
			}
		} else if (tipoFiltroConsulta.equals("disciplina")) {
			setFiltroDisciplina(true);
			setFiltroUnidadeEnsino(false);
			setFiltroTurma(false);
			setFiltroMatricula(false);
			setFiltroCurso(false);
			setBotaoConsultar(true);
			limparDadosAluno();
			limparDadosCurso();
			limparDadosTurma();
			aplicarRegraPeriodicidadeAnoSemestre();
			if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
				montarListaSelectItemDisciplinasProfessor();
				getAvaliacaoOnlineMatriculaVO().getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().getUnidadeEnsino().setCodigo(getUnidadeEnsinoLogado().getCodigo());
			}
		}
		
	}

	public void limparDadosAluno() {
		removerObjetoMemoria(getAvaliacaoOnlineMatriculaVO().getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO());
		getListaConsultaCalendario().clear();
		getListaSelectItemDisciplinasAluno().clear();
	}

	public void limparDadosCurso() {
		removerObjetoMemoria(getAvaliacaoOnlineMatriculaVO().getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().getCurso());
		getListaConsultaCalendario().clear();
		getListaSelectItemDisciplinasAluno().clear();
	}

	public void limparDadosTurma() {
		removerObjetoMemoria(getAvaliacaoOnlineMatriculaVO().getMatriculaPeriodoTurmaDisciplinaVO().getTurma());
		getListaConsultaCalendario().clear();
		getListaSelectItemDisciplinasAluno().clear();
	}	

	public void montarListaDisciplinasTurma() {
		try {
			setListaSelectItemDisciplinasAluno(UtilSelectItem.getListaSelectItem(consultarDisciplina(), "codigo", "nome", false));
			getListaConsultaCalendario().clear();
		} catch (Exception e) {
			setListaSelectItemDisciplinasAluno(new ArrayList<>(0));
		}
	}

	private List<DisciplinaVO> consultarDisciplina() throws Exception {
		/*if((getFiltroMatricula()||getFiltroCurso() || getFiltroTurma() || getFiltroUnidadeEnsino()) &&  getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
			return getFacadeFactory().getDisciplinaFacade() .consultarDisciplinasPorMatriculaPeriodoTurmaDisciplina(getAvaliacaoOnlineMatriculaVO().getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().getMatricula(), 
							getAvaliacaoOnlineMatriculaVO().getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().getUnidadeEnsino().getCodigo(), 
							getAvaliacaoOnlineMatriculaVO().getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().getCurso().getCodigo(), 
							getAvaliacaoOnlineMatriculaVO().getMatriculaPeriodoTurmaDisciplinaVO().getTurma().getCodigo(), 
							getSemestre(), getAno(), Uteis.NIVELMONTARDADOS_COMBOBOX, true, 
							Arrays.asList(ModalidadeDisciplinaEnum.ON_LINE), getUsuarioLogado());
		}else*/ 
		if(getFiltroUnidadeEnsino() && getUsuarioLogado().getIsApresentarVisaoProfessor()) {
			return getFacadeFactory().getDisciplinaFacade().consultarDisciplinasDoProfessor(getUsuarioLogado().getPessoa().getCodigo(), getAvaliacaoOnlineMatriculaVO().getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().getUnidadeEnsino().getCodigo(), null, null, null, null, Uteis.NIVELMONTARDADOS_COMBOBOX, true, true, getUsuarioLogadoClone());
		}else if(getFiltroCurso() && getUsuarioLogado().getIsApresentarVisaoProfessor()) {
			return getFacadeFactory().getDisciplinaFacade().consultarDisciplinasDoProfessor(getUsuarioLogado().getPessoa().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), getAvaliacaoOnlineMatriculaVO().getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().getCurso().getCodigo(), null, getSemestre(), getAno(), Uteis.NIVELMONTARDADOS_COMBOBOX, true, true, getUsuarioLogadoClone());
		}else if(getFiltroTurma() && getUsuarioLogado().getIsApresentarVisaoProfessor()) {
			if (getAvaliacaoOnlineMatriculaVO().getMatriculaPeriodoTurmaDisciplinaVO().getTurma().getIntegral()) {
				return getFacadeFactory().getDisciplinaFacade().consultarDisciplinasDoProfessor(getUsuarioLogado().getPessoa().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), null, getAvaliacaoOnlineMatriculaVO().getMatriculaPeriodoTurmaDisciplinaVO().getTurma().getCodigo(), null, null, Uteis.NIVELMONTARDADOS_COMBOBOX, true, true, getUsuarioLogadoClone());
			} else if (!getAvaliacaoOnlineMatriculaVO().getMatriculaPeriodoTurmaDisciplinaVO().getTurma().getIntegral()) {
				return getFacadeFactory().getDisciplinaFacade().consultarDisciplinasDoProfessor(getUsuarioLogado().getPessoa().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), null, getAvaliacaoOnlineMatriculaVO().getMatriculaPeriodoTurmaDisciplinaVO().getTurma().getCodigo(), getSemestre(), getAno(), Uteis.NIVELMONTARDADOS_COMBOBOX, true, true, getUsuarioLogadoClone());				
			} 
		}else if (getFiltroDisciplina() && getUsuarioLogado().getIsApresentarVisaoProfessor()) {
			return getFacadeFactory().getDisciplinaFacade().consultarDisciplinasDoProfessor(getUsuarioLogado().getPessoa().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), null, null, getSemestre(), getAno(), Uteis.NIVELMONTARDADOS_COMBOBOX, true, true, getUsuarioLogadoClone());
		}
		return new ArrayList<DisciplinaVO>();
		 
	}
	
	public void realizarSelecaoCheckboxMarcarDesmarcarTodosCalendarios() {
		if (getMarcarTodosCalendarios()) {
			realizarSelecaoTodosCalendarios(true);
		} else {
			realizarSelecaoTodosCalendarios(false);
		}
	}
	
	public void realizarSelecaoTodosCalendarios(boolean selecionado){
		for (CalendarioAtividadeMatriculaVO obj : getListaConsultaCalendario()) {
			if(!obj.getAvaliacaoOnlineMatriculaVO().getIsAvaliacaoRealizada()){
				obj.setSelecionarAtividade(selecionado);	
			}
		}
	}

	// Getters and Setters
	public String getTipoFiltroConsulta() {
		if (tipoFiltroConsulta == null) {
			tipoFiltroConsulta = "";
		}
		return tipoFiltroConsulta;
	}

	public void setTipoFiltroConsulta(String tipoFiltroConsulta) {
		this.tipoFiltroConsulta = tipoFiltroConsulta;
	}

	public AvaliacaoOnlineMatriculaVO getAvaliacaoOnlineMatriculaVO() {
		if (avaliacaoOnlineMatriculaVO == null) {
			avaliacaoOnlineMatriculaVO = new AvaliacaoOnlineMatriculaVO();
		}
		return avaliacaoOnlineMatriculaVO;
	}

	public void setAvaliacaoOnlineMatriculaVO(AvaliacaoOnlineMatriculaVO avaliacaoOnlineMatriculaVO) {
		this.avaliacaoOnlineMatriculaVO = avaliacaoOnlineMatriculaVO;
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

	public Boolean getFiltroMatricula() {
		if (filtroMatricula == null) {
			filtroMatricula = false;
		}
		return filtroMatricula;
	}

	public void setFiltroMatricula(Boolean filtroMatricula) {
		this.filtroMatricula = filtroMatricula;
	}

	public Boolean getFiltroCurso() {
		if (filtroCurso == null) {
			filtroCurso = false;
		}
		return filtroCurso;
	}

	public void setFiltroCurso(Boolean filtroCurso) {
		this.filtroCurso = filtroCurso;
	}

	public Boolean getFiltroTurma() {
		if (filtroTurma == null) {
			filtroTurma = false;
		}
		return filtroTurma;
	}

	public void setFiltroTurma(Boolean filtroTurma) {
		this.filtroTurma = filtroTurma;
	}

	public Boolean getFiltroDisciplina() {
		if (filtroDisciplina == null) {
			filtroDisciplina = false;
		}
		return filtroDisciplina;
	}

	public void setFiltroDisciplina(Boolean filtroDisciplina) {
		this.filtroDisciplina = filtroDisciplina;
	}

	public List<SelectItem> getListaSelectItemDisciplinasAluno() {
		if (listaSelectItemDisciplinasAluno == null) {
			listaSelectItemDisciplinasAluno = new ArrayList<SelectItem>();
		}
		return listaSelectItemDisciplinasAluno;
	}

	public void setListaSelectItemDisciplinasAluno(List<SelectItem> listaSelectItemDisciplinasAluno) {
		this.listaSelectItemDisciplinasAluno = listaSelectItemDisciplinasAluno;
	}

	public Date getPeriodoInicio() {
		return periodoInicio;
	}

	public void setPeriodoInicio(Date periodoInicio) {
		this.periodoInicio = periodoInicio;
	}

	public Date getPeriodoFim() {
		return periodoFim;
	}

	public void setPeriodoFim(Date periodoFim) {
		this.periodoFim = periodoFim;
	}

	public Boolean getBotaoConsultar() {
		if (botaoConsultar == null) {
			botaoConsultar = false;
		}
		return botaoConsultar;
	}

	public void setBotaoConsultar(Boolean botaoConsultar) {
		this.botaoConsultar = botaoConsultar;
	}

	public List<CalendarioAtividadeMatriculaVO> getListaConsultaCalendario() {
		if (listaConsultaCalendario == null) {
			listaConsultaCalendario = new ArrayList<CalendarioAtividadeMatriculaVO>();
		}
		return listaConsultaCalendario;
	}

	public void setListaConsultaCalendario(List<CalendarioAtividadeMatriculaVO> listaConsultaCalendario) {
		this.listaConsultaCalendario = listaConsultaCalendario;
	}

	public String getSituacaoAvaliacao() {
		if (situacaoAvaliacao == null) {
			situacaoAvaliacao = "";
		}
		return situacaoAvaliacao;
	}

	public void setSituacaoAvaliacao(String situacaoAvaliacao) {
		this.situacaoAvaliacao = situacaoAvaliacao;
	}

	public CalendarioAtividadeMatriculaVO getCalendarioAtividadeMatriculaVO() {
		if (calendarioAtividadeMatriculaVO == null) {
			calendarioAtividadeMatriculaVO = new CalendarioAtividadeMatriculaVO();
		}
		return calendarioAtividadeMatriculaVO;
	}

	public void setCalendarioAtividadeMatriculaVO(CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO) {
		this.calendarioAtividadeMatriculaVO = calendarioAtividadeMatriculaVO;
	}
	
	public boolean getIsApresentarAno() {
		if ((getAvaliacaoOnlineMatriculaVO().getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().getCurso().getCodigo() != 0 && (getAvaliacaoOnlineMatriculaVO().getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().getCurso().getPeriodicidade().equals("SE") || getAvaliacaoOnlineMatriculaVO().getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().getCurso().getPeriodicidade().equals("AN")))
				|| (getAvaliacaoOnlineMatriculaVO().getMatriculaPeriodoTurmaDisciplinaVO().getTurma().getCodigo() != 0 && (getAvaliacaoOnlineMatriculaVO().getMatriculaPeriodoTurmaDisciplinaVO().getTurma().getCurso().getPeriodicidade().equals("SE") || getAvaliacaoOnlineMatriculaVO().getMatriculaPeriodoTurmaDisciplinaVO().getTurma().getCurso().getPeriodicidade().equals("AN")))
				|| getTipoFiltroConsulta().equals("disciplina")
				|| getTipoFiltroConsulta().equals("unidadeEnsino")) {
			return true;
		}
		return false;
	}

	public boolean getIsApresentarSemestre() {
		if ((getAvaliacaoOnlineMatriculaVO().getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().getCurso().getCodigo() != 0 && getAvaliacaoOnlineMatriculaVO().getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().getCurso().getPeriodicidade().equals("SE"))
		|| (getAvaliacaoOnlineMatriculaVO().getMatriculaPeriodoTurmaDisciplinaVO().getTurma().getCodigo() != 0 && (getAvaliacaoOnlineMatriculaVO().getMatriculaPeriodoTurmaDisciplinaVO().getTurma().getCurso().getPeriodicidade().equals("SE") || getAvaliacaoOnlineMatriculaVO().getMatriculaPeriodoTurmaDisciplinaVO().getTurma().getCurso().getPeriodicidade().equals("AN")))
		|| getTipoFiltroConsulta().equals("disciplina")
		|| getTipoFiltroConsulta().equals("unidadeEnsino")) {		
			return true;	
		}
		return false;
	}
	
	public List<SelectItem> getTipoFiltroConsultaAvaliacaOnline() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("selecione", "Selecione um filtro"));
		if (getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
			itens.add(new SelectItem("matricula", "Matrícula"));
			itens.add(new SelectItem("unidadeEnsino", "Unidade de Ensino"));
		}
		itens.add(new SelectItem("curso", "Curso"));
		itens.add(new SelectItem("turma", "Turma"));
		if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
			itens.add(new SelectItem("disciplina", "Disciplina"));
		}
		return itens;
	}

	public List<SelectItem> getTipoConsultaComboAluno() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("nomePessoa", "Aluno"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		return itens;
	}

	public List<SelectItem> getTipoConsultaComboCurso() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("nome", "Nome"));		
		itens.add(new SelectItem("nrRegistroInterno", "Número (Registro Interno)"));
		itens.add(new SelectItem("nomeAreaConhecimento", "Área Conhecimento"));
		itens.add(new SelectItem("nivelEducacional", "Nível Educacional"));
		itens.add(new SelectItem("unidadeEnsino", "Unidade de Ensino"));
		return itens;
	}

	public List<SelectItem> getTipoConsultaComboTurma() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("identificadorTurma", "Identificador"));
		itens.add(new SelectItem("nomeUnidadeEnsino", "Unidade de Ensino"));
		itens.add(new SelectItem("nomeTurno", "Turno"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
	}

	public List<SelectItem> getCampoSemestreTurma() {
		List<SelectItem> objs = new ArrayList<SelectItem>(0);

		objs.add(new SelectItem("1", "1º"));
		objs.add(new SelectItem("2", "2º"));

		return objs;
	}

	public List<SelectItem> getListaSelectItemSituacaoAtividadeOnline() {
		List<SelectItem> objs = new ArrayList<SelectItem>(0);

		objs.add(new SelectItem("TODAS", "Todas"));
		objs.add(new SelectItem("APROVADO", "Aprovado"));
		objs.add(new SelectItem("REPROVADO", "Reprovado"));
		objs.add(new SelectItem("NAO_CONCLUIDA", "Aguardando Avaliação"));
		return objs;
	}
	
	public void montarListaSelectItemUnidadesEnsino() {
		try {			
			setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(getFacadeFactory().getUnidadeEnsinoFacade().consultarTodasUnidades(false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()), "codigo", "nome", true));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void montarListaSelectItemDisciplinasProfessor() {
		try {
			setListaSelectItemDisciplinasAluno(UtilSelectItem.getListaSelectItem(consultarDisciplina(), "codigo", "nome", true));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void montarListaSelectItemDisciplinasCursoProfessor() {
		try {
			if(Uteis.isAtributoPreenchido(getAvaliacaoOnlineMatriculaVO().getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().getCurso().getCodigo())){
				getAvaliacaoOnlineMatriculaVO().getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().setCurso(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(getAvaliacaoOnlineMatriculaVO().getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().getCurso().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, false, getUsuarioLogado()));
				aplicarRegraPeriodicidadeAnoSemestre();
				setListaSelectItemDisciplinasAluno(UtilSelectItem.getListaSelectItem(consultarDisciplina(), "codigo", "nome", true));
			}else {
				getListaSelectItemDisciplinasAluno().clear();		
			}
			getListaConsultaCalendario().clear();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void montarListaSelectItemDisciplinasTurmaProfessor() {
		try {
			if(Uteis.isAtributoPreenchido(getAvaliacaoOnlineMatriculaVO().getMatriculaPeriodoTurmaDisciplinaVO().getTurma())){
				getAvaliacaoOnlineMatriculaVO().getMatriculaPeriodoTurmaDisciplinaVO().getTurma().setNivelMontarDados(NivelMontarDados.NAO_INICIALIZADO);
				getFacadeFactory().getTurmaFacade().carregarDados(getAvaliacaoOnlineMatriculaVO().getMatriculaPeriodoTurmaDisciplinaVO().getTurma(), NivelMontarDados.BASICO, getUsuarioLogadoClone());
				aplicarRegraPeriodicidadeAnoSemestre();
				setListaSelectItemDisciplinasAluno(UtilSelectItem.getListaSelectItem(consultarDisciplina(), "codigo", "nome", true));
			}else {
				getListaSelectItemDisciplinasAluno().clear();
			}
			getListaConsultaCalendario().clear();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	
	
	public void montarListaSelectItemDisciplinasUnidadeEnsinoProfessor() {
		try {
			setListaSelectItemDisciplinasAluno(UtilSelectItem.getListaSelectItem(consultarDisciplina(), "codigo", "nome", true));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	public void montarListaSelectItemTurmasProfessor() {
		List<TurmaVO> listaTurmas = null;
		List<Integer> mapAuxiliarSelectItem = new ArrayList<>();
		try {
			listaTurmas = consultarTurmaPorProfessor();
			getListaSelectItemTurma().clear();
			getListaSelectItemTurma().add(new SelectItem(0, ""));
			for (TurmaVO turmaVO : listaTurmas) {
				if(!mapAuxiliarSelectItem.contains(turmaVO.getCodigo())){
					getListaSelectItemTurma().add(new SelectItem(turmaVO.getCodigo(), turmaVO.aplicarRegraNomeCursoApresentarCombobox()));
					mapAuxiliarSelectItem.add(turmaVO.getCodigo());
					removerObjetoMemoria(turmaVO);
				}
			}
			getListaSelectItemDisciplinasAluno().clear();
		} catch (Exception e) {
			getListaSelectItemTurma().clear();
		} finally {
			Uteis.liberarListaMemoria(listaTurmas);
			mapAuxiliarSelectItem = null;
		}
	}

	public List<TurmaVO> consultarTurmaPorProfessor() {
		try {
			return getFacadeFactory().getTurmaFacade().consultarTurmaPorProfessorAnoSemestreTurmaAnteriorNivelDadosCombobox(getUsuarioLogado().getPessoa().getCodigo(), Uteis.getSemestreAtual(), Uteis.getData(new Date(), "yyyy"), false, "AT", getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado().getVisaoLogar().equals("professor"), false, true, true);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return new ArrayList<TurmaVO>();
	}
	
	public void montarListaSelectItemCursosProfessor() {
		try {
			setListaSelectItemCursos(UtilSelectItem.getListaSelectItem(getFacadeFactory().getCursoFacade().consultaCursoDoProfessor(getUsuarioLogado().getPessoa().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_COMBOBOX, false, getUsuarioLogado()), "codigo", "nome", true));
			getListaSelectItemDisciplinasAluno().clear();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void consultarDisciplinaModal() {
		try {
			if (getValorConsultaDisciplina().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			if (getCampoConsultaDisciplina().equals("codigo")) {
				int valorInt = Integer.parseInt(getValorConsultaDisciplina());
				DisciplinaVO disciplina = getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(new Integer(valorInt), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				if (!disciplina.equals(new DisciplinaVO()) || disciplina != null) {
					getListaConsultaDisciplina().add(disciplina);
				}
			}
			if (getCampoConsultaDisciplina().equals("nome")) {
				setListaConsultaDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorNome(getValorConsultaDisciplina(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			}
			if (getCampoConsultaDisciplina().equals("areaConhecimento")) {
				setListaConsultaDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorNomeAreaConhecimento(getValorConsultaDisciplina(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaDisciplina(new ArrayList<DisciplinaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void limparDisciplina() {
		try {
			getAvaliacaoOnlineMatriculaVO().getMatriculaPeriodoTurmaDisciplinaVO().setDisciplina(new DisciplinaVO());
			getListaConsultaDisciplina().clear();
			getListaConsulta().clear();
			setMensagemID("msg_informe_disciplina", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}	
	}

	public void selecionarDisciplina()  {
		try {
			DisciplinaVO disciplina = (DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplinaItens");
			getAvaliacaoOnlineMatriculaVO().getMatriculaPeriodoTurmaDisciplinaVO().setDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(disciplina.getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogadoClone()));
			getListaConsultaDisciplina().clear();
		} catch (Exception e) {			
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public List<SelectItem> getTipoConsultaComboDisciplina() {
		List<SelectItem> objs = new ArrayList<SelectItem>(0);		
		objs.add(new SelectItem("nome", "Nome"));
		objs.add(new SelectItem("codigo", "Código"));
		return objs;
	}
	
	
	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList<SelectItem>();
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}
	
	

	public Boolean getMarcarTodosCalendarios() {
		if(marcarTodosCalendarios == null){
			marcarTodosCalendarios = false;
		}
		return marcarTodosCalendarios;
	}

	public void setMarcarTodosCalendarios(Boolean marcarTodosCalendarios) {
		this.marcarTodosCalendarios = marcarTodosCalendarios;
	}

	public List<CalendarioAtividadeMatriculaVO> getListaConsultaCalendarioExcluir() {
		if (listaConsultaCalendarioExcluir == null) {
			listaConsultaCalendarioExcluir = new ArrayList<CalendarioAtividadeMatriculaVO>();
		}
		return listaConsultaCalendarioExcluir;
	}

	public void setListaConsultaCalendarioExcluir(List<CalendarioAtividadeMatriculaVO> listaConsultaCalendarioExcluir) {
		this.listaConsultaCalendarioExcluir = listaConsultaCalendarioExcluir;
	}

	public String getAbrirModalExcluir() {
		if(abrirModalExcluir == null){
			abrirModalExcluir = "";
		}
		return abrirModalExcluir;
	}

	public void setAbrirModalExcluir(String abrirModalExcluir) {
		this.abrirModalExcluir = abrirModalExcluir;
	}
	
	public Boolean getFiltroUnidadeEnsino() {
		if(filtroUnidadeEnsino == null){
			filtroUnidadeEnsino = false;
		}
		return filtroUnidadeEnsino;
	}

	public void setFiltroUnidadeEnsino(Boolean filtroUnidadeEnsino) {
		this.filtroUnidadeEnsino = filtroUnidadeEnsino;
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
	
	
	public List<SelectItem> getListaSelectItemCursos() {
		if (listaSelectItemCursos == null) {
			listaSelectItemCursos = new ArrayList<SelectItem>();
		}
		return listaSelectItemCursos;
	}

	public void setListaSelectItemCursos(List<SelectItem> listaSelectItemCursos) {
		this.listaSelectItemCursos = listaSelectItemCursos;
	}
	
	private void validarDadosConsulta() throws Exception {
		if (((getFiltroUnidadeEnsino()||getFiltroMatricula()||getFiltroCurso()||getFiltroTurma())) 
				&& getUsuarioLogado().getIsApresentarVisaoAdministrativa()
				&& !Uteis.isAtributoPreenchido(getAvaliacaoOnlineMatriculaVO().getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().getUnidadeEnsino().getCodigo())) {
			throw new Exception(UteisJSF.internacionalizar("msg_MonitoramentoAlunosEAD_unidadeEnsinoDeveSerInfomrada"));
		}else if (getFiltroMatricula() && getAvaliacaoOnlineMatriculaVO().getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().getMatricula().equals("")) {
			throw new Exception(UteisJSF.internacionalizar("msg_InformeUmaMatricula"));
		} else if (getFiltroCurso() && getAvaliacaoOnlineMatriculaVO().getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().getCurso().getCodigo().equals(0)) {
			throw new Exception(UteisJSF.internacionalizar("msg_InformeUmCurso"));
		} else if (getFiltroTurma() && getAvaliacaoOnlineMatriculaVO().getMatriculaPeriodoTurmaDisciplinaVO().getTurma().getCodigo().equals(0)) {
			throw new Exception(UteisJSF.internacionalizar("msg_InformeUmaTurma"));
		}
		if (getIsApresentarAno() &&  !Uteis.isAtributoPreenchido(getAno())) {
			throw new Exception("O filtro Ano deve ser informado para consulta");
		}
		if (getIsApresentarSemestre() &&  !Uteis.isAtributoPreenchido(getSemestre())) {
			throw new Exception("O filtro Semestre deve ser informado para consulta");
		}
		if (!Uteis.isAtributoPreenchido(getAvaliacaoOnlineMatriculaVO().getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina().getCodigo())) {
			throw new Exception(UteisJSF.internacionalizar("msg_MonitoramentoAlunosEAD_aDisciplinaDeveSerInformada"));
		}
		if (!Uteis.isAtributoPreenchido(getPeriodoInicio()) || !Uteis.isAtributoPreenchido(getPeriodoFim())) {
			throw new Exception("Deve ser informado o Período da Avaliação Online.");
		}
	}
	
	private void aplicarRegraPeriodicidadeAnoSemestre() {
		setSemestre(getIsApresentarSemestre() ? Uteis.getSemestreAtual() : "");
		setAno(getIsApresentarAno() ? Uteis.getAno(new Date()) : "");
	}

	public TipoCalendarioAtividadeMatriculaEnum getTipoCalendarioAtividade() {
		return tipoCalendarioAtividade;
	}

	public void setTipoCalendarioAtividade(TipoCalendarioAtividadeMatriculaEnum tipoCalendarioAtividade) {
		this.tipoCalendarioAtividade = tipoCalendarioAtividade;
	}

	public List<SelectItem> getListaSelectItemTipoCalendarioAtividade() {
		if (listaSelectItemTipoCalendarioAtividade == null) {
			listaSelectItemTipoCalendarioAtividade = new ArrayList<>(0);
		}
		return listaSelectItemTipoCalendarioAtividade;
	}

	public void setListaSelectItemTipoCalendarioAtividade(List<SelectItem> listaSelectItemTipoCalendarioAtividade) {
		this.listaSelectItemTipoCalendarioAtividade = listaSelectItemTipoCalendarioAtividade;
	}
	
	public String getTituloRea() {
		if (tituloRea == null) {
			tituloRea = "";
		}
		return tituloRea;
	}

	public void setTituloRea(String tituloRea) {
		this.tituloRea = tituloRea;
	}

	private void montarListaSelectItemTipoCalendarioAtividade() {
		if (getListaSelectItemTipoCalendarioAtividade().isEmpty()) {
			Predicate<TipoCalendarioAtividadeMatriculaEnum> predicateAvaliacaoOnline = p -> p.equals(TipoCalendarioAtividadeMatriculaEnum.PERIODO_REALIZACAO_AVALIACAO_ONLINE);
			Predicate<TipoCalendarioAtividadeMatriculaEnum> predicateAvaliacaoOnlineREA = p -> p.equals(TipoCalendarioAtividadeMatriculaEnum.PERIODO_REALIZACAO_AVALIACAO_ONLINE_REA);
			getListaSelectItemTipoCalendarioAtividade().add(new SelectItem("", ""));
			Stream.of(TipoCalendarioAtividadeMatriculaEnum.values()).filter(predicateAvaliacaoOnline.or(predicateAvaliacaoOnlineREA))
			.map(p -> new SelectItem(p.getName(), p.getValorApresentar()))
			.forEach(getListaSelectItemTipoCalendarioAtividade()::add);
		}
	}
	
	public boolean getApresentarTituloRea() {
		return Uteis.isAtributoPreenchido(getTipoCalendarioAtividade())
				&& getFiltroDisciplina() && getTipoCalendarioAtividade().equals(TipoCalendarioAtividadeMatriculaEnum.PERIODO_REALIZACAO_AVALIACAO_ONLINE_REA);
	}
	
	public void limparTituloRea() {
		setTituloRea(getApresentarTituloRea() ? getTituloRea() : "");
		getListaConsultaCalendario().clear();
	}
	
	public AvaliacaoOnlineMatriculaVO getAvaliacaoOnlineMatriculaRecuperacaoDados() {
		if (avaliacaoOnlineMatriculaRecuperacaoDados == null) {
			avaliacaoOnlineMatriculaRecuperacaoDados = new AvaliacaoOnlineMatriculaVO();
		}
		return avaliacaoOnlineMatriculaRecuperacaoDados;
	}

	public void setAvaliacaoOnlineMatriculaRecuperacaoDados(
			AvaliacaoOnlineMatriculaVO avaliacaoOnlineMatriculaRecuperacaoDados) {
		this.avaliacaoOnlineMatriculaRecuperacaoDados = avaliacaoOnlineMatriculaRecuperacaoDados;
	}
	
	private void copiarDadosAvaliacaoOnlineVO() {
		getAvaliacaoOnlineMatriculaRecuperacaoDados().getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().setMatricula(getAvaliacaoOnlineMatriculaVO().getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().getMatricula());
		getAvaliacaoOnlineMatriculaRecuperacaoDados().getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().getUnidadeEnsino().setCodigo(getAvaliacaoOnlineMatriculaVO().getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().getUnidadeEnsino().getCodigo());
		getAvaliacaoOnlineMatriculaRecuperacaoDados().getMatriculaPeriodoTurmaDisciplinaVO().getTurma().setCodigo(getAvaliacaoOnlineMatriculaVO().getMatriculaPeriodoTurmaDisciplinaVO().getTurma().getCodigo());
		getAvaliacaoOnlineMatriculaRecuperacaoDados().getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().getCurso().setCodigo(getAvaliacaoOnlineMatriculaVO().getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().getCurso().getCodigo());
		getAvaliacaoOnlineMatriculaRecuperacaoDados().setSituacaoAvaliacaoOnlineMatriculaEnum(getAvaliacaoOnlineMatriculaVO().getSituacaoAvaliacaoOnlineMatriculaEnum());
	}
	
	private void recuperarDadosAvaliacaoOnlineVO() {
		getAvaliacaoOnlineMatriculaVO().getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().setMatricula(getAvaliacaoOnlineMatriculaRecuperacaoDados().getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().getMatricula());
		getAvaliacaoOnlineMatriculaVO().getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().getUnidadeEnsino().setCodigo(getAvaliacaoOnlineMatriculaRecuperacaoDados().getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().getUnidadeEnsino().getCodigo());
		getAvaliacaoOnlineMatriculaVO().getMatriculaPeriodoTurmaDisciplinaVO().getTurma().setCodigo(getAvaliacaoOnlineMatriculaRecuperacaoDados().getMatriculaPeriodoTurmaDisciplinaVO().getTurma().getCodigo());
		getAvaliacaoOnlineMatriculaVO().getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().getCurso().setCodigo(getAvaliacaoOnlineMatriculaRecuperacaoDados().getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().getCurso().getCodigo());
		getAvaliacaoOnlineMatriculaVO().setSituacaoAvaliacaoOnlineMatriculaEnum(getAvaliacaoOnlineMatriculaRecuperacaoDados().getSituacaoAvaliacaoOnlineMatriculaEnum());
	}

	public List<MatriculaVO> getMatriculaVOs() {
		if (matriculaVOs == null) {
			matriculaVOs = new ArrayList<>(0);
		}
		return matriculaVOs;
	}

	public void setMatriculaVOs(List<MatriculaVO> matriculaVOs) {
		this.matriculaVOs = matriculaVOs;
	}

	public List<CursoVO> getCursoVOs() {
		if (cursoVOs == null) {
			cursoVOs = new ArrayList<>(0);
		}
		return cursoVOs;
	}

	public void setCursoVOs(List<CursoVO> cursoVOs) {
		this.cursoVOs = cursoVOs;
	}

	public List<TurmaVO> getTurmaVOs() {
		if (turmaVOs == null) {
			turmaVOs = new ArrayList<>(0);
		}
		return turmaVOs;
	}

	public void setTurmaVOs(List<TurmaVO> turmaVOs) {
		this.turmaVOs = turmaVOs;
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

	public String getCampoConsultaDisciplina() {
		return campoConsultaDisciplina;
	}

	public void setCampoConsultaDisciplina(String campoConsultaDisciplina) {
		this.campoConsultaDisciplina = campoConsultaDisciplina;
	}

	public String getValorConsultaDisciplina() {
		return valorConsultaDisciplina;
	}

	public void setValorConsultaDisciplina(String valorConsultaDisciplina) {
		this.valorConsultaDisciplina = valorConsultaDisciplina;
	}
}
