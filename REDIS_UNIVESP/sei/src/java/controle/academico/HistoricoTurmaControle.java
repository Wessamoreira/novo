package controle.academico;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas historicoForm.jsp historicoCons.jsp) com as funcionalidades da classe <code>Historico</code>. Implemtação da
 * camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see Historico
 * @see HistoricoVO
 */
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.CalendarioLancamentoNotaVO;
import negocio.comuns.academico.ConfiguracaoAcademicoNotaConceitoVO;
import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.GradeCurricularGrupoOptativaDisciplinaVO;
import negocio.comuns.academico.GradeDisciplinaCompostaVO;
import negocio.comuns.academico.GradeDisciplinaVO;
import negocio.comuns.academico.HistoricoNotaParcialVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.RegistroAulaVO;
import negocio.comuns.academico.TurmaDisciplinaNotaParcialVO;
import negocio.comuns.academico.TurmaDisciplinaNotaTituloVO;
import negocio.comuns.academico.TurmaDisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.TipoNotaConceitoEnum;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoVisaoAlunoEnum;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.basico.enumeradores.ConsiderarFeriadoEnum;
import negocio.comuns.secretaria.enumeradores.FormaReplicarNotaOutraDisciplinaEnum;
import negocio.comuns.secretaria.enumeradores.TipoAlteracaoSituacaoHistoricoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.FechamentoPeriodoLetivoException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilReflexao;
import negocio.comuns.utilitarias.dominios.SituacaoHistorico;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.academico.Historico;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

@Controller("HistoricoTurmaControle")
@Scope("viewScope")
@Lazy
public class HistoricoTurmaControle extends SuperControle implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7461235368389811474L;
	private HistoricoVO historicoVO;
	private HistoricoVO historicoAprensentarVO;
	private ConfiguracaoAcademicoVO configuracaoAcademicoVO;
	private TurmaVO turmaVO;
	private String tipoInformarNota;
	
	private List<SelectItem> listaSelectItemTipoInformarNota;
	private List<SelectItem> listaSelectItemDisciplinas;
	private List<SelectItem> listaSelectItemTurma;
	private List<SelectItem> listaSelectItemPeriodosLetivos;
	private String semestre;
	private String ano;
	private String campoConsultaTurma;
	private String valorConsultaTurma;
	private List<TurmaVO> listaConsultaTurma;
	private List<HistoricoVO> alunosTurma;
	private String tipoNota;
	private List<HistoricoVO> listaAnterior;
	private Boolean listasIguais;
	private List<SelectItem> listaSelectItemProfessoresTurma;
	private PessoaVO professor;
	private Boolean permiteGravarVisaoCoordenador;
	private List<SelectItem> listaSelectItemNota1Conceito;
	private List<SelectItem> listaSelectItemNota2Conceito;
	private List<SelectItem> listaSelectItemNota3Conceito;
	private List<SelectItem> listaSelectItemNota4Conceito;
	private List<SelectItem> listaSelectItemNota5Conceito;
	private List<SelectItem> listaSelectItemNota6Conceito;
	private List<SelectItem> listaSelectItemNota7Conceito;
	private List<SelectItem> listaSelectItemNota8Conceito;
	private List<SelectItem> listaSelectItemNota9Conceito;
	private List<SelectItem> listaSelectItemNota10Conceito;
	private List<SelectItem> listaSelectItemNota11Conceito;
	private List<SelectItem> listaSelectItemNota12Conceito;
	private List<SelectItem> listaSelectItemNota13Conceito;
	private List<SelectItem> listaSelectItemNota14Conceito;
	private List<SelectItem> listaSelectItemNota15Conceito;
	private List<SelectItem> listaSelectItemNota16Conceito;
	private List<SelectItem> listaSelectItemNota17Conceito;
	private List<SelectItem> listaSelectItemNota18Conceito;
	private List<SelectItem> listaSelectItemNota19Conceito;
	private List<SelectItem> listaSelectItemNota20Conceito;
	private List<SelectItem> listaSelectItemNota21Conceito;
	private List<SelectItem> listaSelectItemNota22Conceito;
	private List<SelectItem> listaSelectItemNota23Conceito;
	private List<SelectItem> listaSelectItemNota24Conceito;
	private List<SelectItem> listaSelectItemNota25Conceito;
	private List<SelectItem> listaSelectItemNota26Conceito;
	private List<SelectItem> listaSelectItemNota27Conceito;
	private List<SelectItem> listaSelectItemNota28Conceito;
	private List<SelectItem> listaSelectItemNota29Conceito;
	private List<SelectItem> listaSelectItemNota30Conceito;
	private List<SelectItem> listaSelectItemNota31Conceito;
	private List<SelectItem> listaSelectItemNota32Conceito;
	private List<SelectItem> listaSelectItemNota33Conceito;
	private List<SelectItem> listaSelectItemNota34Conceito;
	private List<SelectItem> listaSelectItemNota35Conceito;
	private List<SelectItem> listaSelectItemNota36Conceito;
	private List<SelectItem> listaSelectItemNota37Conceito;
	private List<SelectItem> listaSelectItemNota38Conceito;
	private List<SelectItem> listaSelectItemNota39Conceito;
	private List<SelectItem> listaSelectItemNota40Conceito;
	
	
	private CalendarioLancamentoNotaVO calendarioLancamentoNotaVO;
	private Boolean existeRegistroAula;
	HashMap<Integer, ConfiguracaoAcademicoVO> mapConfiguracaoAcademicoVOs;
	private Boolean possuiDiversidadeConfiguracaoAcademico;
	private List<SelectItem> listaSelectItemConfiguracaoAcademico;
	private DataModelo controleConsultaOtimizado;
	private Boolean consultaDataScroller;
	private String valorConsultaAluno;
	private String letra;
	private Boolean consultaIndividualPorAluno;
	private Boolean consultaPorInicialNome;
	private Boolean possuiHistoricoAlteradoLista;
	private Integer paginaRequisitada;
	private Integer paginaAtual;
	private Boolean calcularMediaApenasParaDadosAlterados;
	private Boolean calcularMediaParaTodosAlunos;
	private Map<String, HistoricoVO> mapHistoricoAlteradoVOs;
	private Boolean realizouCalculoMedia;
	private Boolean ocultarSituacaoMatricula;
	private TipoAlteracaoSituacaoHistoricoEnum tipoAlteracaoSituacaoHistorico;
	private Boolean bloquearLancamentosNotasAulasFeriadosFinaisSemana;
	private List<MatriculaPeriodoVO> matriculaPeriodoVOs;
	private TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTituloVO;
	private FormaReplicarNotaOutraDisciplinaEnum formaReplicarNotaOutraDisciplina;	
	private List<SelectItem> listaSelectItemFormaReplicarNotaOutraDisciplina;
	private TipoNotaConceitoEnum tipoNotaReplicar;
	private Boolean permitirLancarNotaRetroativa;
	private TurmaDisciplinaNotaParcialVO turmaDisciplinaNotaParcial;
	private List<HistoricoNotaParcialVO> historicoNotaParcialVOs;
	private HistoricoVO historicoTemporarioVO;
	private List<HistoricoNotaParcialVO> historicoNotaParcialGeralVOs;
	private String tipoNotaUsar;
	private String tituloNotaApresentar;
	private Map<Integer, List<HistoricoNotaParcialVO>> mapHistoricoNotaParcialVOs;
	
	public HistoricoTurmaControle() throws Exception {
		setMensagemID("msg_entre_prmconsulta");
		getControleConsultaOtimizado().setLimitePorPagina(10);
		}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe
	 * <code>Historico</code> para edição pelo usuário da aplicação.
	 */
	public String novo() {
		removerObjetoMemoria(this);
		// inicializarListasSelectItemTodosComboBox();
		inicializarUsuarioResponsavelMatriculaUsuarioLogado();
		setCalendarioLancamentoNotaVO(null);
		verificarPermissaoOcultarSituacaoMatricula();
		getControleConsultaOtimizado().setLimitePorPagina(10);
		setMensagemID("msg_entre_dados");
		return "editar";
	}

	public String novoVisaoProfessor() throws Exception {
		try {
			novo();
			getFacadeFactory().getHistoricoFacade().validarConsultaDoUsuarioVisaoCoordenador("LancamentoNota", getUsuarioLogado());			
			verificarPermitirLancarNotaRetroativa();
			montarListaSelectItemTurma();
			setMensagemID("msg_entre_dados");
			VisaoProfessorControle visaoProfessor = (VisaoProfessorControle) context().getExternalContext().getSessionMap().get("VisaoProfessorControle");
			if (visaoProfessor != null) {
				visaoProfessor.inicializarMenuRegistroNota();
			}
			setRealizouCalculoMedia(Boolean.FALSE);
			setCalcularMediaParaTodosAlunos(Boolean.FALSE);
			setCalcularMediaApenasParaDadosAlterados(Boolean.TRUE);
			verificarBloquearLancamentosNotasAulasFeriadosFinaisSemana();
			getControleConsultaOtimizado().setLimitePorPagina(10);
			return Uteis.getCaminhoRedirecionamentoNavegacao("registrarNotaProfessor.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}
	
	@PostConstruct
	public void init() {
		inicializarDadosOrigemExterna();
	}

	private void inicializarDadosOrigemExterna() {
		try {
			TurmaVO turma = (TurmaVO) context().getExternalContext().getSessionMap().get("turmaVO");
			DisciplinaVO disciplina = (DisciplinaVO) context().getExternalContext().getSessionMap().get("disciplinaVO");
			if(Uteis.isAtributoPreenchido(turma) && Uteis.isAtributoPreenchido(disciplina)) {
				setTurmaVO(turma);
				montarDadosHistoricoVisaoProfessor();
				montarListaSelectItemDisciplinas();
				getHistoricoVO().setDisciplina(disciplina); 
				montarComboboxConfiguracaoAcademico();
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}finally {
			context().getExternalContext().getSessionMap().remove("turmaVO");
			context().getExternalContext().getSessionMap().remove("disciplinaVO");
		}
	}
	

	public String novoVisaoCoordenador() throws Exception {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "HistoricoTurmaControle", "Novo Visão Coordenador", "Novo");
			novo();
			getFacadeFactory().getHistoricoFacade().validarConsultaDoUsuarioVisaoCoordenador("LancamentoNota", getUsuarioLogado());
			// getFacadeFactory().getHistoricoFacade().validarConsultaDoUsuario(getUsuarioLogadoClone());
			ano = Uteis.getAnoDataAtual4Digitos();
			semestre = Uteis.getSemestreAtual();
			montarListaSelectItemTurmaCoordenador();
			montarListaSelectItemProfessoresTurmaCoordenador();
			setMensagemID("msg_entre_dados");
			VisaoCoordenadorControle visaoCoordenador = (VisaoCoordenadorControle) context().getExternalContext().getSessionMap().get("VisaoCoordenadorControle");
			if (visaoCoordenador != null) {
				visaoCoordenador.inicializarMenuRegistroNota();
			}
			verificarBloquearLancamentosNotasAulasFeriadosFinaisSemana();
			return Uteis.getCaminhoRedirecionamentoNavegacao("registrarNotaCoordenador.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}

	public void montarListaSelectItemTurmaCoordenador() {
		try {
			montarListaSelectItemTurmaCoordenador("");
		} catch (Exception e) {
			// System.out.println("Erro HistoricoTurmaControle.montarListaSelectItemTurmaCoordenador: " + e.getMessage());
		}
	}

	public void montarListaSelectItemTurmaCoordenador(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarTurmaCoordenador();
			i = resultadoConsulta.iterator();
			getListaSelectItemTurma().clear();
			getListaSelectItemTurma().add(new SelectItem(0, ""));
			while (i.hasNext()) {
				TurmaVO obj = (TurmaVO) i.next();
				getListaSelectItemTurma().add(new SelectItem(obj.getCodigo(), obj.getIdentificadorTurma().toString()));
				removerObjetoMemoria(obj);
			}
			resultadoConsulta.clear();
			resultadoConsulta = null;
			i = null;
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	public List consultarTurmaCoordenador() throws Exception {
		// return
		// getFacadeFactory().getTurmaFacade().consultaRapidaPorCoordenador(getUsuarioLogado().getPessoa().getCodigo(),
		// false, true, true, getUnidadeEnsinoLogado().getCodigo(), false,
		// getUsuarioLogado());
		return getFacadeFactory().getTurmaFacade().consultaRapidaPorCoordenadorAnoSemestre(getUsuarioLogado().getPessoa().getCodigo(), false, false, true, false, getAno(), getSemestre(), getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
	}

	public void montarListaSelectItemProfessoresTurmaCoordenador() {
		try {
			if (!Uteis.isAtributoPreenchido(getTurmaVO().getCodigo()) && !getLoginControle().getPermissaoAcessoMenuVO().getPermitirLancarNotaRetroativo()) {
				setAno(Uteis.getAnoDataAtual4Digitos());
				setSemestre(Uteis.getSemestreAtual());
			}
			if (getTurmaVO().getCodigo() != 0) {
				getTurmaVO().setNivelMontarDados(NivelMontarDados.BASICO);
				getFacadeFactory().getTurmaFacade().carregarDados(getTurmaVO(), NivelMontarDados.TODOS, getUsuarioLogado());
				getHistoricoVO().getMatriculaPeriodo().setPeridoLetivo(getTurmaVO().getPeridoLetivo());
			}
			getProfessor().setCodigo(0);
			getHistoricoVO().getDisciplina().setCodigo(0);
			getListaSelectItemDisciplinas().clear();
			montarListaSelectItemProfessoresTurmaCoordenador("");
		} catch (Exception e) {
			// System.out.println("Erro HistoricoTurmaControle.montarListaSelectItemProfessoresTurmaCoordenador: " + e.getMessage());
		}
	}

	public void montarListaSelectItemProfessoresTurmaCoordenador(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			getListaSelectItemProfessoresTurma().clear();
			if (getTurmaVO().getCodigo() != null && !getTurmaVO().getCodigo().equals(0)) {
				resultadoConsulta = consultarProfessoresTurmaCoordenador();
				i = resultadoConsulta.iterator();
				getListaSelectItemProfessoresTurma().clear();
				getListaSelectItemProfessoresTurma().add(new SelectItem(0, ""));
				while (i.hasNext()) {
					PessoaVO obj = (PessoaVO) i.next();
					getListaSelectItemProfessoresTurma().add(new SelectItem(obj.getCodigo(), obj.getNome()));
					removerObjetoMemoria(obj);
				}
				resultadoConsulta.clear();
				resultadoConsulta = null;
				i = null;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	public List consultarProfessoresTurmaCoordenador() throws Exception {
		return getFacadeFactory().getPessoaFacade().consultarProfessoresDaTurmaPorTurma(getTurmaVO().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), getSemestre(), getAno(), false, getUsuarioLogado());
	}

	public void montarListaSelectItemTurma() {
		List<Integer> mapAuxiliarSelectItem = new ArrayList();
		List listaResultado = null;
		Iterator i = null;
		String nomeCurso = null;
		try {
			getAlunosTurma().clear();
			getHistoricoVO().setDisciplina(new DisciplinaVO());
			setListaSelectItemDisciplinas(new ArrayList<>());
			listaResultado = consultarTurmaPorProfessor();
			getListaSelectItemTurma().clear();
			getListaSelectItemTurma().add(new SelectItem(0, ""));
			i = listaResultado.iterator();
			while (i.hasNext()) {
				TurmaVO turma = (TurmaVO) i.next();
				if(!mapAuxiliarSelectItem.contains(turma.getCodigo())){
					getListaSelectItemTurma().add(new SelectItem(turma.getCodigo(), turma.aplicarRegraNomeCursoApresentarCombobox()));
            		mapAuxiliarSelectItem.add(turma.getCodigo());
				}
			}
			montarListaDisciplinaTurmaVisaoProfessor();
		} catch (Exception e) {

			getListaSelectItemTurma().clear();
			getListaSelectItemTipoInformarNota().clear();
			removerObjetoMemoria(getHistoricoVO());
			setAlunosTurma(new ArrayList<>());
		} finally {
			Uteis.liberarListaMemoria(listaResultado);
			i = null;
			nomeCurso = null;
		}
	}

	public List consultarTurmaPorProfessor() throws Exception {
		return getFacadeFactory().getTurmaFacade().consultarTurmaPorProfessorAnoSemestreTurmaAnteriorCursoNivelDadosCombobox(getUsuarioLogado().getPessoa().getCodigo(),getSemestre(),getAno(),getPermitirLancarNotaRetroativa(),"AT",getUnidadeEnsinoLogado().getCodigo(),0,getUsuarioLogado().getVisaoLogar().equals("professor"),false,false,true, null,false, null);
		//return getFacadeFactory().getTurmaFacade().consultarTurmaPorProfessorAnoSemestreNivelDadosCombobox(getUsuarioLogado().getPessoa().getCodigo(), getSemestre(), getAno(), "AT", 0, getUsuarioLogado().getVisaoLogar().equals("professor"), true, true);
		
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe
	 * <code>Historico</code> para alteração. O objeto desta classe é
	 * disponibilizado na session da página (request) para que o JSP
	 * correspondente possa disponibilizá-lo para edição.
	 */
	public String editar() throws Exception {
		TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("historicoItens");
		obj = getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
		obj.setNovoObj(Boolean.FALSE);
		setTurmaVO(obj);
		inicializarListasSelectItemTodosComboBox();
		montarConfiguracaoAcademico();
		inicializarUsuarioResponsavelMatriculaUsuarioLogado();
		setMensagemID("msg_dados_editar");
		return "editar";
	}

	public void montarConfiguracaoAcademico() throws Exception {
		GradeDisciplinaVO gradeDisciplinaVO = null;
		GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO =null;
		GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO =null;
		TurmaDisciplinaVO turmaDisciplinaVO = null;
		try {
			if ((this.getHistoricoVO().getDisciplina() == null) || (this.getHistoricoVO().getDisciplina().getCodigo().equals(0))) {
				getHistoricoVO().getConfiguracaoAcademico().setCodigo(0);
				setListaSelectItemTipoInformarNota(new ArrayList<SelectItem>(0));
				return;
			}
			turmaDisciplinaVO = getFacadeFactory().getTurmaDisciplinaFacade().consultarPorTurmaDisciplina(getTurmaVO().getCodigo(), getHistoricoVO().getDisciplina().getCodigo(), false, getUsuarioLogado());
			if (!getTurmaVO().getGradeCurricularVO().getCodigo().equals(0)) {
				gradeDisciplinaVO = getFacadeFactory().getGradeDisciplinaFacade().consultarPorGradeCurricularEDisciplina(getTurmaVO().getGradeCurricularVO().getCodigo(), this.getHistoricoVO().getDisciplina().getCodigo(), getUsuarioLogado(), null);
				gradeCurricularGrupoOptativaDisciplinaVO = getFacadeFactory().getGradeCurricularGrupoOptativaDisciplinaFacade().consultarPorDisciplinaMatrizCurricularPeriodoLetivo(this.getHistoricoVO().getDisciplina().getCodigo(), getTurmaVO().getGradeCurricularVO().getCodigo(), getUsuarioLogado());
				gradeDisciplinaCompostaVO =  getFacadeFactory().getGradeDisciplinaCompostaFacade().consultarPorCodigoDisciplinaEMatriz(getHistoricoVO().getDisciplina().getCodigo(), getTurmaVO().getGradeCurricularVO().getCodigo(), getUsuarioLogado());
			}
			
			if (!this.getHistoricoVO().getConfiguracaoAcademico().getCodigo().equals(0)) {
				getConfiguracaoAcademicoVO().setCodigo(this.getHistoricoVO().getConfiguracaoAcademico().getCodigo());
			} else if (!turmaDisciplinaVO.getConfiguracaoAcademicoVO().getCodigo().equals(0)) {
				getConfiguracaoAcademicoVO().setCodigo(turmaDisciplinaVO.getConfiguracaoAcademicoVO().getCodigo());
			} else if (gradeDisciplinaVO != null && !gradeDisciplinaVO.getConfiguracaoAcademico().getCodigo().equals(0)) {
				getConfiguracaoAcademicoVO().setCodigo(gradeDisciplinaVO.getConfiguracaoAcademico().getCodigo());
			} else if (gradeCurricularGrupoOptativaDisciplinaVO != null && !gradeCurricularGrupoOptativaDisciplinaVO.getConfiguracaoAcademico().getCodigo().equals(0)) {
				getConfiguracaoAcademicoVO().setCodigo(gradeCurricularGrupoOptativaDisciplinaVO.getConfiguracaoAcademico().getCodigo());
			} else if (gradeDisciplinaCompostaVO != null && !gradeDisciplinaCompostaVO.getConfiguracaoAcademico().getCodigo().equals(0)) {
				getConfiguracaoAcademicoVO().setCodigo(gradeDisciplinaCompostaVO.getConfiguracaoAcademico().getCodigo());
			} else {
				if (getTurmaVO().getTurmaAgrupada().booleanValue()) {
					CursoVO c = new CursoVO();
					Integer codCurso = 0;
					if (!getTurmaVO().getTurmaAgrupadaVOs().isEmpty()) {
						codCurso = getTurmaVO().getTurmaAgrupadaVOs().get(0).getTurma().getCurso().getCodigo();
					}
					c = getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(codCurso, Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, getUsuarioLogado());
					setConfiguracaoAcademicoVO(c.getConfiguracaoAcademico());
				} else {
					if(!Uteis.isAtributoPreenchido(getTurmaVO().getCurso().getConfiguracaoAcademico().getCodigo())){
						getTurmaVO().setCurso(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(getTurmaVO().getCurso().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, getUsuarioLogado()));
					}
					setConfiguracaoAcademicoVO(getTurmaVO().getCurso().getConfiguracaoAcademico());
				}
			}
			setConfiguracaoAcademicoVO(getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorChavePrimaria(getConfiguracaoAcademicoVO().getCodigo(), getUsuarioLogado()));
			montarListaOpcoesNotas(getConfiguracaoAcademicoVO());
			inicializarOpcaoNotaConceito();
			
			if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
				setCalendarioLancamentoNotaVO(getFacadeFactory().getCalendarioLancamentoNotaFacade().consultarPorCalendarioAcademicoUtilizar(getTurmaVO().getUnidadeEnsino().getCodigo(), getTurmaVO().getCodigo(), getTurmaVO().getTurmaAgrupada(), getUsuarioLogado().getPessoa().getCodigo(), getHistoricoVO().getDisciplina().getCodigo(), getConfiguracaoAcademicoVO().getCodigo(), getTurmaVO().getPeriodicidade(), getAno(), getSemestre(), false, getUsuarioLogado()));
				
				getFacadeFactory().getHistoricoFacade().validarProfessorExclusivoLancamentoNota(getCalendarioLancamentoNotaVO(), getUsuarioLogado());
			} else if (getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
				setCalendarioLancamentoNotaVO(getFacadeFactory().getCalendarioLancamentoNotaFacade().consultarPorCalendarioAcademicoUtilizar(getTurmaVO().getUnidadeEnsino().getCodigo(), getTurmaVO().getCodigo(), getTurmaVO().getTurmaAgrupada(), 0, getHistoricoVO().getDisciplina().getCodigo(), getConfiguracaoAcademicoVO().getCodigo(), getTurmaVO().getPeriodicidade(), getAno(), getSemestre(), false, getUsuarioLogado()));
			} else {
				setCalendarioLancamentoNotaVO(null);
			}
			getListaSelectItemConfiguracaoAcademico().clear();
		} catch (Exception e) {
			throw e;
		}finally {
			gradeDisciplinaVO = null;
			gradeCurricularGrupoOptativaDisciplinaVO =null;
			gradeDisciplinaCompostaVO =null;
			turmaDisciplinaVO = null;
		}
	}

	public void montarListaOpcoesNotas() {
		if (!getConfiguracaoAcademicoVO().getCodigo().equals(0)) {
			try {
				setConfiguracaoAcademicoVO(getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorChavePrimaria(this.configuracaoAcademicoVO.getCodigo(), getUsuarioLogado()));
				montarListaOpcoesNotas(getConfiguracaoAcademicoVO());
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			}
		}
	}

	public void inicializarDadosDependentesConfiguracaoAcademico() throws Exception {
		inicializarOpcaoNotaConceito();
		if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
			setCalendarioLancamentoNotaVO(getFacadeFactory().getCalendarioLancamentoNotaFacade().consultarPorCalendarioAcademicoUtilizar(getTurmaVO().getUnidadeEnsino().getCodigo(), getTurmaVO().getCodigo(), getTurmaVO().getTurmaAgrupada(), getUsuarioLogado().getPessoa().getCodigo(), getHistoricoVO().getDisciplina().getCodigo(), getConfiguracaoAcademicoVO().getCodigo(), getTurmaVO().getPeriodicidade(), getAno(), getSemestre(), false, getUsuarioLogado()));
		} else if (getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
			setCalendarioLancamentoNotaVO(getFacadeFactory().getCalendarioLancamentoNotaFacade().consultarPorCalendarioAcademicoUtilizar(getTurmaVO().getUnidadeEnsino().getCodigo(), getTurmaVO().getCodigo(), getTurmaVO().getTurmaAgrupada(), 0, getHistoricoVO().getDisciplina().getCodigo(), getConfiguracaoAcademicoVO().getCodigo(), getTurmaVO().getPeriodicidade(), getAno(), getSemestre(), false, getUsuarioLogado()));
		} else {
			setCalendarioLancamentoNotaVO(null);
		}
	}

	public void inicializarOpcaoNotaConceito() {
		setListaSelectItemNota1Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota1ConceitoVOs()));
		setListaSelectItemNota2Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota2ConceitoVOs()));
		setListaSelectItemNota3Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota3ConceitoVOs()));
		setListaSelectItemNota4Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota4ConceitoVOs()));
		setListaSelectItemNota5Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota5ConceitoVOs()));
		setListaSelectItemNota6Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota6ConceitoVOs()));
		setListaSelectItemNota7Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota7ConceitoVOs()));
		setListaSelectItemNota8Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota8ConceitoVOs()));
		setListaSelectItemNota9Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota9ConceitoVOs()));
		setListaSelectItemNota10Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota10ConceitoVOs()));
		setListaSelectItemNota11Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota11ConceitoVOs()));
		setListaSelectItemNota12Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota12ConceitoVOs()));
		setListaSelectItemNota13Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota13ConceitoVOs()));
		setListaSelectItemNota14Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota14ConceitoVOs()));
		setListaSelectItemNota15Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota15ConceitoVOs()));
		setListaSelectItemNota16Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota16ConceitoVOs()));
		setListaSelectItemNota17Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota17ConceitoVOs()));
		setListaSelectItemNota18Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota18ConceitoVOs()));
		setListaSelectItemNota19Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota19ConceitoVOs()));
		setListaSelectItemNota20Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota20ConceitoVOs()));
		setListaSelectItemNota21Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota21ConceitoVOs()));
		setListaSelectItemNota22Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota22ConceitoVOs()));
		setListaSelectItemNota23Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota23ConceitoVOs()));
		setListaSelectItemNota24Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota24ConceitoVOs()));
		setListaSelectItemNota25Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota25ConceitoVOs()));
		setListaSelectItemNota26Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota26ConceitoVOs()));
		setListaSelectItemNota27Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota27ConceitoVOs()));
		setListaSelectItemNota28Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota28ConceitoVOs()));
		setListaSelectItemNota29Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota29ConceitoVOs()));
		setListaSelectItemNota30Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota30ConceitoVOs()));
		setListaSelectItemNota31Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota31ConceitoVOs()));
		setListaSelectItemNota32Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota32ConceitoVOs()));
		setListaSelectItemNota33Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota33ConceitoVOs()));
		setListaSelectItemNota34Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota34ConceitoVOs()));
		setListaSelectItemNota35Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota35ConceitoVOs()));
		setListaSelectItemNota36Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota36ConceitoVOs()));
		setListaSelectItemNota37Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota37ConceitoVOs()));
		setListaSelectItemNota38Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota38ConceitoVOs()));
		setListaSelectItemNota39Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota39ConceitoVOs()));
		setListaSelectItemNota40Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota10ConceitoVOs()));
	}

	private List<SelectItem> getListaSelectItemOpcaoNotaConceito(List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNotaConceitoVOs) {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem(0, ""));
		for (ConfiguracaoAcademicoNotaConceitoVO obj : configuracaoAcademicoNotaConceitoVOs) {
			itens.add(new SelectItem(obj.getCodigo(), obj.getConceitoNota()));
		}
		return itens;

	}

	public void montarDadosHistoricoDisciplinaVisaoProfessor() {
		try {
			setOncompleteModal("");
			getAlunosTurma().clear();
			if (getTurmaVO().getCurso().getPeriodicidade().equals("IN") && ControleAcesso.verificarPermissaoFuncionalidadeUsuario("CursosIntegraisDevemSerRegistradoTelaRegistroAulaNotaPos", getUsuarioLogado())) {
				boolean isTurmaDisciplinaEOnline = getFacadeFactory().getTurmaDisciplinaFacade().validarTurmaDisciplinaEOnline(getTurmaVO(), getHistoricoVO().getDisciplina(), getUsuarioLogado());
				if(!isTurmaDisciplinaEOnline) {
					setOncompleteModal("RichFaces.$('panelAvisoLancamentoNota').show()");
					return;
				}
			}
			montarConfiguracaoAcademico();
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			//getListaSelectItemDisciplinas().clear();
			getListaSelectItemTipoInformarNota().clear();
			removerObjetoMemoria(getHistoricoVO());
			getAlunosTurma().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public String redirecionarTelaRegistroDeAulaENota() {
		context().getExternalContext().getSessionMap().put("turmaVO", getTurmaVO());
		context().getExternalContext().getSessionMap().put("disciplinaVO", getHistoricoVO().getDisciplina());
		removerControleMemoriaFlash("HistoricoTurmaControle");
		removerControleMemoriaTela("HistoricoTurmaControle");
		return Uteis.getCaminhoRedirecionamentoNavegacao("registrarAulaNotaProfessor.xhtml");
	}
	
	public void montarDadosHistoricoDisciplinaVisaoCoordenador() {
		try {
			getAlunosTurma().clear();
			montarConfiguracaoAcademico();
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			getListaSelectItemDisciplinas().clear();
			getListaSelectItemTipoInformarNota().clear();
			removerObjetoMemoria(getHistoricoVO());
			getAlunosTurma().clear();
		}
	}

	public void montarDadosHistoricoVisaoProfessor() {
		try {
			setCalendarioLancamentoNotaVO(null);
			getAlunosTurma().clear();
			getHistoricoVO().setDisciplina(null);
			Integer campoConsulta = getTurmaVO().getCodigo();
			if (campoConsulta.equals(0)) {
				if (!getLoginControle().getPermissaoAcessoMenuVO().getPermitirLancarNotaRetroativo()) {
					setAno(Uteis.getAnoDataAtual4Digitos());
					setSemestre(Uteis.getSemestreAtual());
				}
				throw new Exception();
			} else {
				TurmaVO turma = getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(campoConsulta, NivelMontarDados.TODOS, getUsuarioLogado());
				setTurmaVO(turma);
				getIsApresentarAnoVisaoProfessorCoordenador();
				getIsApresentarSemestreVisaoProfessorCoordenador();
				montarListaSelectPeriodosLetivos();
				getHistoricoVO().getMatriculaPeriodo().setPeridoLetivo(turma.getPeridoLetivo());
				getHistoricoVO().getMatriculaPeriodo().setGradeCurricular(turma.getGradeCurricularVO());
				montarListaDisciplinaTurmaVisaoProfessor();
				setMensagemID("msg_dados_consultados");
			}
			setMensagemID("msg_dados_consultados" );
		} catch (Exception e) {
			e.printStackTrace();
			getListaSelectItemDisciplinas().clear();
			getListaSelectItemTipoInformarNota().clear();
			removerObjetoMemoria(getHistoricoVO());
			getAlunosTurma().clear();
		}
	}

	public void montarListaDisciplinaTurmaVisaoProfessor() throws Exception {

		List<DisciplinaVO> resultado = consultarDisciplinaProfessorTurma();
		Iterator<DisciplinaVO> i = resultado.iterator();
		getListaSelectItemDisciplinas().clear();
		getListaSelectItemDisciplinas().add(new SelectItem(0, ""));
		while (i.hasNext()) {
			DisciplinaVO obj = (DisciplinaVO) i.next();
			getListaSelectItemDisciplinas().add(new SelectItem(obj.getCodigo(), obj.getNome()));
			removerObjetoMemoria(obj);
		}
		Uteis.liberarListaMemoria(resultado);
		i = null;

	}

	public List<DisciplinaVO> consultarDisciplinaProfessorTurma() throws Exception {
		// List listaConsultas =
		// getFacadeFactory().getDisciplinaFacade().consultarDisciplinaProfessorTurma(getUsuarioLogado().getPessoa().getCodigo(),
		// getTurmaVO().getCodigo(), false,
		// Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		// return listaConsultas;
		/*if (!Uteis.isAtributoPreenchido(getCalendarioLancamentoNotaVO())) {
			montarConfiguracaoAcademico();
		}

		boolean professorExclusivo = getFacadeFactory().getHistoricoFacade().professorExclusivoLancamentoNota(getCalendarioLancamentoNotaVO(), getUsuarioLogado());*/
		List<DisciplinaVO> listaConsultas = new ArrayList<>(0);
		if(Uteis.isAtributoPreenchido(getTurmaVO())){
			if (getTurmaVO().getIntegral()) {
				listaConsultas = getFacadeFactory().getDisciplinaFacade().consultarDisciplinasDoProfessor(getUsuarioLogado().getPessoa().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), null, getTurmaVO().getCodigo(), null, null, Uteis.NIVELMONTARDADOS_DADOSBASICOS, true, getPermiteLancarNotaDisciplinaComposta(), getUsuarioLogado(), true);
			} else {
				listaConsultas = getFacadeFactory().getDisciplinaFacade().consultarDisciplinasDoProfessor(getUsuarioLogado().getPessoa().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), null, getTurmaVO().getCodigo(), getSemestre(), getAno(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, true, getPermiteLancarNotaDisciplinaComposta(), getUsuarioLogado(), true);				
			}
		}
		return listaConsultas;
	}

	public void montarDadosHistoricoVisaoCoordenador() {
		try {
			getAlunosTurma().clear();
			getHistoricoVO().getDisciplina().setCodigo(0);
			// Integer campoConsulta = getTurmaVO().getCodigo();
			// if (campoConsulta.equals(0)) {
			// throw new Exception();
			// } else {
			// TurmaVO turma =
			// getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(campoConsulta,
			// NivelMontarDados.TODOS, getUsuarioLogado());
			// setTurmaVO(turma);
			// montarConfiguracaoAcademico();
			montarListaSelectPeriodosLetivos();
			getHistoricoVO().getMatriculaPeriodo().setPeridoLetivo(getTurmaVO().getPeridoLetivo());
			getHistoricoVO().getMatriculaPeriodo().setGradeCurricular(getTurmaVO().getGradeCurricularVO());
			getIsApresentarSemestreVisaoProfessorCoordenador();
			montarListaDisciplinaTurmaVisaoCoordenador();
			setMensagemID("msg_dados_consultados");
			// }
			// setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			getListaSelectItemDisciplinas().clear();
			getListaSelectItemTipoInformarNota().clear();
			removerObjetoMemoria(getHistoricoVO());
			getAlunosTurma().clear();
		}
	}

	public void montarListaDisciplinaTurmaVisaoCoordenador() throws Exception {

		List resultado = consultarDisciplinaProfessorTurmaVisaoCoordenador();
		Iterator i = resultado.iterator();
		getListaSelectItemDisciplinas().clear();
		getListaSelectItemDisciplinas().add(new SelectItem(0, ""));
		while (i.hasNext()) {
			DisciplinaVO obj = (DisciplinaVO) i.next();
			getListaSelectItemDisciplinas().add(new SelectItem(obj.getCodigo(), obj.getNome()));
			removerObjetoMemoria(obj);
		}
		Uteis.liberarListaMemoria(resultado);
		i = null;

	}

	public List consultarDisciplinaProfessorTurmaVisaoCoordenador() throws Exception {
		// List listaConsultas =
		// getFacadeFactory().getDisciplinaFacade().consultarDisciplinaProfessorTurma(getUsuarioLogado().getPessoa().getCodigo(),
		// getTurmaVO().getCodigo(), false,
		// Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		// return listaConsultas;
		List listaConsultas = new ArrayList(0);
		if (getTurmaVO().getCurso().getNivelEducacionalPosGraduacao()) {
			listaConsultas = getFacadeFactory().getDisciplinaFacade().consultarDisciplinasDoProfessor(getProfessor().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), null, getTurmaVO().getCodigo(), null, null, Uteis.NIVELMONTARDADOS_DADOSBASICOS, true, getPermiteLancarNotaDisciplinaComposta(), getUsuarioLogado());
			//listaConsultas = getFacadeFactory().getDisciplinaFacade().consultarDisciplinaProfessorTurmaValidandoHorarioTurmaDia(getProfessor().getCodigo(), getTurmaVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			return listaConsultas;
		} else {
			listaConsultas = getFacadeFactory().getDisciplinaFacade().consultarDisciplinasDoProfessor(getProfessor().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), null, getTurmaVO().getCodigo(), getSemestre(), getAno(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, true, getPermiteLancarNotaDisciplinaComposta(), getUsuarioLogado());
			//listaConsultas = getFacadeFactory().getDisciplinaFacade().consultarDisciplinaProfessorTurmaValidandoHorarioTurmaDiaSemestreAtual(getProfessor().getCodigo(), getTurmaVO().getCodigo(), getAno(), getSemestre(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			return listaConsultas;
		}
	}

	public void montarListaSelectPeriodosLetivos() {
		try {
			montarListaSelectPeriodosLetivos("");
		} catch (Exception e) {
			// System.out.println("Erro HistoricoTurmaControle.montarListaSelectPeriodosLetivos: " + e.getMessage());
		}
	}

	public void montarListaSelectPeriodosLetivos(String prm) throws Exception {
		if (getTurmaVO().getCurso().getCodigo().intValue() == 0) {
			setListaSelectItemPeriodosLetivos(new ArrayList(0));
			return;
		}
		List objs = new ArrayList(0);
		getTurmaVO().setPeridoLetivo(getFacadeFactory().getPeriodoLetivoFacade().consultarPorChavePrimaria(getTurmaVO().getPeridoLetivo().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
		objs.add(new SelectItem(getTurmaVO().getPeridoLetivo().getCodigo(), getTurmaVO().getPeridoLetivo().getDescricao()));
		setListaSelectItemPeriodosLetivos(objs);
	}

	/**
	 * Método responsável por consultar dados da entidade
	 * <code><code> e montar o atributo <code>sigla</code> Este atributo é uma
	 * lista (<code>List</code>) utilizada para definir os valores a serem
	 * apresentados no ComboBox correspondente
	 */
	public List consultarPeriodoLetivoPorGradecurricular(String prm, Integer codigo) throws Exception {
		List lista = getFacadeFactory().getCursoFacade().consultarPeriodoLetivoPorGradeCurricular(prm, codigo, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
		return lista;
	}
	
	public void gravarVisaoCoordenador() {
		try {
			executarValidacaoSimulacaoVisaoCoordenador();
			setOncompleteModal("");
			if(getCalcularMediaAoGravar()){
				setCalcularMediaApenasParaDadosAlterados(false);
				setCalcularMediaParaTodosAlunos(true);
				calcularMediaHistoricoVisaoProfessor();
			}
			realizarCloneListaHistorico();
			getFacadeFactory().getHistoricoFacade().incluirListaHistoricoVisaoProfessor(getAlunosTurma(), "LancamentoNota", getUsuarioLogado(), "Visão do Professor", true, getCalendarioLancamentoNotaVO(), getCalcularMediaAoGravar());
			validarDadosComparacaoListaHistorico();
			setMatriculaPeriodoVOs(getFacadeFactory().getMatriculaPeriodoFacade().consultarMatriculaPeriodoPrecisaReprovarPeriodoLetivo(getAlunosTurma(), getAno(), getSemestre(), getUsuarioLogado()));
			if(!getMatriculaPeriodoVOs().isEmpty()){
				setOncompleteModal("RichFaces.$('panelReprovarPeriodoLetivo').show();");
			}
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (ConsistirException ex) {			
			setConsistirExceptionMensagemDetalhada("msg_erro", ex, Uteis.ERRO);
		} catch (Exception e) {						
			setMensagemDetalhada("msg_erro", e.getMessage());			
		}
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto
	 * da classe <code>Historico</code>. Caso o objeto seja novo (ainda não
	 * gravado no BD) é acionado a operação <code>incluir()</code>. Caso
	 * contrário é acionado o <code>alterar()</code>. Se houver alguma
	 * inconsistência o objeto não é gravado, sendo re-apresentado para o
	 * usuário juntamente com uma mensagem de erro.
	 * @throws Exception 
	 * @throws ConsistirException 
	 */
	// public String gravar() {
	// try {
	// getFacadeFactory().getHistoricoFacade().incluirListaHistorico(getAlunosTurma(),
	// getUsuarioLogado(), "Visão Administrativa");
	// setMensagemID("msg_dados_gravados");
	// return "editar";
	// } catch (Exception e) {
	// setMensagemDetalhada("msg_erro", e.getMessage());
	// return "editar";
	// }
	// }
	public void gravarVisaoProfessor() {

		try {
			executarValidacaoSimulacaoVisaoProfessor();
			getFacadeFactory().getRegistroAulaFacade().validarQuantidadeDiasProfessorPoderaRegistrarAulaAposUltimaAulaVisaoProfessor(new Date(), getTurmaVO(), getHistoricoVO().getDisciplina(), getUsuarioLogado().getPessoa(), getAno(), getSemestre(), getUsuarioLogado());
			setOncompleteModal("");
			/*if(getCalcularMediaAoGravar() || validarCalculoMediaAutomaticaAoGravar()){
					setCalcularMediaApenasParaDadosAlterados(false);
					setCalcularMediaParaTodosAlunos(true);
					calcularMediaHistoricoVisaoProfessor();
				
			}*/
			this.setAlunosTurma(this.getControleConsultaOtimizado().getListaConsulta());
			realizarCloneListaHistorico();
			if (validarCalculoMediaAutomaticaAoGravar()) {
				inicializarOpcaoDadosAlteradosHistorico();
			} 
			getFacadeFactory().getHistoricoFacade().incluirListaHistoricoVisaoProfessor(getAlunosTurma(), "LancamentoNota", getUsuarioLogado(), "Visão do Professor", true, getCalendarioLancamentoNotaVO(), getCalcularMediaAoGravar());
			validarDadosComparacaoListaHistorico();
			realizarCopiaMapDosHistoricosAlterados();
			inicializarDadosHistoricoNaoAlterado();
			if (getRealizouCalculoMedia()) {
				gravarDadosHistoricoAlterado();
				setRealizouCalculoMedia(Boolean.FALSE);
			}
			setMatriculaPeriodoVOs(getFacadeFactory().getMatriculaPeriodoFacade().consultarMatriculaPeriodoPrecisaReprovarPeriodoLetivo(getAlunosTurma(), getAno(), getSemestre(), getUsuarioLogado()));
			if(!getMatriculaPeriodoVOs().isEmpty()){
				setOncompleteModal("RichFaces.$('panelReprovarPeriodoLetivo').show();");
			}
			setMensagemID("msg_dados_gravados");
		} catch (ConsistirException ex) {			
			setConsistirExceptionMensagemDetalhada("msg_erro", ex, Uteis.ERRO);
		} catch (Exception e) {						
			setMensagemDetalhada("msg_erro", e.getMessage());			
		}

	}

	public void gravarDadosHistoricoAlterado() throws Exception {
		List<HistoricoVO> listaHistoricoVOs = new ArrayList<HistoricoVO>(0); 
		for (HistoricoVO historicoVO : getMapHistoricoAlteradoVOs().values()) {
			listaHistoricoVOs.add(historicoVO);
		}
		if (!listaHistoricoVOs.isEmpty()) {
			getFacadeFactory().getHistoricoFacade().incluirListaHistoricoVisaoProfessor(listaHistoricoVOs, "LancamentoNota", getUsuarioLogado(), "Visão do Professor", true,getCalendarioLancamentoNotaVO(), getCalcularMediaAoGravar());
		}
	}
	
	public void inicializarDadosHistoricoNaoAlterado() {
		for (HistoricoVO historicoVO : getAlunosTurma()) {
			if (historicoVO.getHistoricoAlterado()) {
				historicoVO.setHistoricoSalvo(Boolean.TRUE);
			} else {
				historicoVO.setHistoricoSalvo(Boolean.FALSE);
				historicoVO.setHistoricoCalculado(Boolean.FALSE);
			}
		}
	}
	 
	
	public void persistirHistoricoAlunoIndividual() {
		try {
			executarValidacaoSimulacaoVisaoProfessor();
			setOncompleteModal("");
			HistoricoVO historicoVO = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
			realizarCloneListaHistorico();
			getFacadeFactory().getRegistroAulaFacade().validarQuantidadeDiasProfessorPoderaRegistrarAulaAposUltimaAulaVisaoProfessor(new Date(), getTurmaVO(), historicoVO.getDisciplina(), getUsuarioLogado().getPessoa(), getAno(), getSemestre(), getUsuarioLogado());
			getFacadeFactory().getHistoricoFacade().gravarLancamentoNota(historicoVO, true, "Visão do Professor", TipoAlteracaoSituacaoHistoricoEnum.TODOS_HISTORICOS, getUsuarioLogado());
			validarDadosComparacaoListaHistorico();
			historicoVO.setHistoricoAlterado(Boolean.FALSE);
			historicoVO.setHistoricoSalvo(Boolean.TRUE);
			List<HistoricoVO> historicoVOs = new ArrayList<HistoricoVO>(0);
			historicoVOs.add(historicoVO);
			setMatriculaPeriodoVOs(getFacadeFactory().getMatriculaPeriodoFacade().consultarMatriculaPeriodoPrecisaReprovarPeriodoLetivo(historicoVOs, getAno(), getSemestre(), getUsuarioLogado()));
			if(!getMatriculaPeriodoVOs().isEmpty()){
				setOncompleteModal("RichFaces.$('panelReprovarPeriodoLetivo').show();");
			}
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void calcularMediaHistoricoAlunoIndividual() {
		HistoricoVO histVO = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		try {

			ConfiguracaoAcademicoVO ca = this.getConfiguracaoAcademicoVO();
			histVO.setMediaFinal(null);
			histVO.setMediaFinalConceito(null);

			histVO.setConfiguracaoAcademico(ca);
			getFacadeFactory().getHistoricoFacade().verificarNotasLancadas(histVO, getUsuarioLogado());
			getFacadeFactory().getConfiguracaoAcademicoFacade().prepararVariaveisNotaParaSubstituicaoFormulaNota(ca, histVO, getUsuarioLogado());
			Map<Integer, ConfiguracaoAcademicoVO> mapConf = new HashMap<Integer, ConfiguracaoAcademicoVO>(0);
			try {
				calcularMedia(ca, histVO, mapConf);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
				montarFreguenciaAluno(histVO, ca);
				verificaAlunoReprovadoFalta(histVO);
				if (!histVO.getSituacao().equals("RF")) {
					histVO.setSituacao("CS");
				}
				histVO.setMediaFinal(null);
			}
			histVO.setHistoricoAlterado(Boolean.TRUE);
			histVO.setHistoricoCalculado(Boolean.TRUE);
			realizarCopiaMapDosHistoricosAlterados(histVO);
			setMensagemID("msg_dados_calculados", Uteis.SUCESSO);
		} catch (Exception e) {
			getHistoricoVO().setSituacao("VS");

			setMensagemID("msg_erro_dadosnaoencontrados");
		}
	}
	
	public void calcularMediaHistoricoVisaoProfessor() {
		try {
			if (!getCalcularMediaApenasParaDadosAlterados() && !getCalcularMediaParaTodosAlunos()) {
				throw new Exception("Deve ser selecionado ao menos uma opção!");
			}
			Map<Integer, ConfiguracaoAcademicoVO> mapConf = new HashMap<Integer, ConfiguracaoAcademicoVO>(0);
			if (getCalcularMediaParaTodosAlunos()) {
				getControleConsultaOtimizado().setLimitePorPagina(10000);
				//setAlunosTurma(getControleConsultaOtimizado().getListaConsulta());
				realizarCopiaMapDosHistoricosAlterados();
				montarAlunosTurma(Boolean.FALSE, Boolean.FALSE);
				realizarAtualizacaoDosHistoricosAlterados();
				inicializarOpcaoDadosAlteradosHistorico();
				
				Iterator i = getAlunosTurma().iterator();
				ConfiguracaoAcademicoVO ca = this.getConfiguracaoAcademicoVO();
				while (i.hasNext()) {
					HistoricoVO histVO = (HistoricoVO) i.next();

					verificarAprovacaoAluno(histVO, mapConf);
					histVO.setHistoricoSalvo(Boolean.FALSE);
					histVO.setHistoricoCalculado(Boolean.TRUE);
				}

				
			} else {
				//setAlunosTurma(getControleConsultaOtimizado().getListaConsulta());
				
				Iterator i = getAlunosTurma().iterator();
				ConfiguracaoAcademicoVO ca = this.getConfiguracaoAcademicoVO();
				while (i.hasNext()) {
					HistoricoVO histVO = (HistoricoVO) i.next();

					if (!histVO.getHistoricoAlterado()) {
						continue;
					}
					verificarAprovacaoAluno(histVO, mapConf);
					histVO.setHistoricoSalvo(Boolean.FALSE);
					histVO.setHistoricoCalculado(Boolean.TRUE);
				}
				for (HistoricoVO obj : getMapHistoricoAlteradoVOs().values()) {
					verificarAprovacaoAluno(obj, mapConf);
				}

			}
			setRealizouCalculoMedia(Boolean.TRUE);
			setMensagemID("msg_dados_calculados", Uteis.SUCESSO);
		} catch (Exception e) {
			getHistoricoVO().setSituacao("VS");
			setMensagemID("msg_erro_dadosnaoencontrados");
		}
	}
	
	public void calcularMediaDadosAlterados() {
		Map<Integer, ConfiguracaoAcademicoVO> mapConf = new HashMap<Integer, ConfiguracaoAcademicoVO>(0);
		for (HistoricoVO obj : getMapHistoricoAlteradoVOs().values()) {
				verificarAprovacaoAluno(obj, mapConf);
		}
	}
	
	public void realizarCopiaMapDosHistoricosAlterados() {
		for (HistoricoVO historicoVO : getAlunosTurma()) {
			if (historicoVO.getHistoricoAlterado() && !getMapHistoricoAlteradoVOs().containsKey(historicoVO.getMatricula().getMatricula())) {
				getMapHistoricoAlteradoVOs().put(historicoVO.getMatricula().getMatricula(), historicoVO);
			}
		}
	}
	
	public void realizarCopiaMapDosHistoricosAlterados(HistoricoVO historicoVO) {
		if (historicoVO.getHistoricoAlterado() && !getMapHistoricoAlteradoVOs().containsKey(historicoVO.getMatricula().getMatricula())) {
			getMapHistoricoAlteradoVOs().put(historicoVO.getMatricula().getMatricula(), historicoVO);
		}
	}

	public void realizarAtualizacaoDosHistoricosAlterados() {
		
		for (String matricula : getMapHistoricoAlteradoVOs().keySet()) {
			int index = 0;
			for (HistoricoVO historicoVO : getAlunosTurma()) {
				if (matricula.equals(historicoVO.getMatricula().getMatricula())) {
					getAlunosTurma().set(index, getMapHistoricoAlteradoVOs().get(matricula));
					break;
				}
				index++;
			}
		}
	}
	
	public void inicializarOpcaoDadosAlteradosHistorico() {
		for (HistoricoVO historicoVO : getAlunosTurma()) {
			historicoVO.setHistoricoAlterado(Boolean.TRUE);
		}
	}

	public void inicializarUsuarioResponsavelMatriculaUsuarioLogado(HistoricoVO histVO) {
		try {
			histVO.setResponsavel(getUsuarioLogadoClone());
		} catch (Exception e) {
			// System.out.println("Erro HistoricoTurmaControle.inicializarUsuarioResponsavelMatriculaUsuarioLogado: " + e.getMessage());
		}
	}

	public void inicializarUsuarioResponsavelMatriculaUsuarioLogado() {
		try {
			getHistoricoVO().setResponsavel(getUsuarioLogadoClone());
		} catch (Exception e) {
			// System.out.println("Erro HistoricoTurmaControle.inicializarUsuarioResponsavelMatriculaUsuarioLogado: " + e.getMessage());
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP
	 * HistoricoCons.jsp. Define o tipo de consulta a ser executada, por meio de
	 * ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
	 * resultado, disponibiliza um List com os objetos selecionados na sessao da
	 * pagina.
	 */
	@Override
	public String consultar() {
		try {
			super.consultar();
			List objs = new ArrayList(0);
			if (getControleConsulta().getCampoConsulta().equals("turma")) {
				objs = getFacadeFactory().getTurmaFacade().consultarPorIdentificadorTurma(getControleConsulta().getValorConsulta(), super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			objs = ControleConsulta.obterSubListPaginaApresentar(objs, controleConsulta);
			definirVisibilidadeLinksNavegacao(controleConsulta.getPaginaAtual(), controleConsulta.getNrTotalPaginas());
			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
			return "consultar";
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "consultar";
		}
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe
	 * <code>HistoricoVO</code> Após a exclusão ela automaticamente aciona a
	 * rotina para uma nova inclusão.
	 */
	public String excluir() {
		try {
			getFacadeFactory().getHistoricoFacade().excluir(getHistoricoVO(), true, getUsuarioLogado());
			removerObjetoMemoria(getHistoricoVO());

			setMensagemID("msg_dados_excluidos");
			setTipoNota("");
			return "editar";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "editar";
		}
	}

	public void consultarTurma() {
		try {
			super.consultar();
			List objs = new ArrayList(0);
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				objs = getFacadeFactory().getTurmaFacade().consultarPorIdentificadorTurma(getValorConsultaTurma(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeUnidadeEnsino")) {
				objs = getFacadeFactory().getTurmaFacade().consultarPorNomeUnidadeEnsino(getValorConsultaTurma(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeTurno")) {
				objs = getFacadeFactory().getTurmaFacade().consultarPorNomeTurno(getValorConsultaTurma(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeCurso")) {
				objs = getFacadeFactory().getTurmaFacade().consultarPorNomeCurso(getValorConsultaTurma(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaTurma(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarTurma() {
		try {
			TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turma");
			setTurmaVO(obj);
			if (getTurmaVO().getSubturma()) {
				getTurmaVO().setCurso(getFacadeFactory().getCursoFacade().consultarCursoPorTurma(getTurmaVO().getTurmaPrincipal(), false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
			}
			consultarTurmaPorIdentificador();

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}

	}

	List tipoConsultaComboTurma;

	public List getTipoConsultaComboTurma() {
		if (tipoConsultaComboTurma == null) {
			tipoConsultaComboTurma = new ArrayList(0);
			tipoConsultaComboTurma.add(new SelectItem("identificadorTurma", "Identificador"));
			tipoConsultaComboTurma.add(new SelectItem("nomeUnidadeEnsino", "Unidade Ensino"));
			tipoConsultaComboTurma.add(new SelectItem("nomeTurno", "Turno"));
			tipoConsultaComboTurma.add(new SelectItem("nomeCurso", "Curso"));
		}
		return tipoConsultaComboTurma;
	}

	public void irPaginaInicial() throws Exception {
		controleConsulta.setPaginaAtual(1);
		this.consultar();
	}

	public void irPaginaAnterior() throws Exception {
		controleConsulta.setPaginaAtual(controleConsulta.getPaginaAtual() - 1);
		this.consultar();
	}

	public void irPaginaPosterior() throws Exception {
		controleConsulta.setPaginaAtual(controleConsulta.getPaginaAtual() + 1);
		this.consultar();
	}

	public void irPaginaFinal() throws Exception {
		controleConsulta.setPaginaAtual(controleConsulta.getNrTotalPaginas());
		this.consultar();
	}

	public void montarListaOpcoesNotas(ConfiguracaoAcademicoVO ca) {
		List<SelectItem> lista = new ArrayList<SelectItem>(0);
		lista.add(new SelectItem("", ""));
		if (ca == null) {
			setListaSelectItemTipoInformarNota(lista);
			return;
		}
		if (ca.getCodigo().intValue() != 0) {
			for (int i = 1; i <= 40; i++) {
				Boolean utilizarNota = (Boolean) UtilReflexao.invocarMetodoGet(ca, "utilizarNota" + i);
				Boolean apresentarNota = (Boolean) UtilReflexao.invocarMetodoGet(ca, "apresentarNota" + i);
				String tituloNotaApresentar = (String) UtilReflexao.invocarMetodoGet(ca, "tituloNotaApresentar" + i);
				String tituloNota = (String) UtilReflexao.invocarMetodoGet(ca, "tituloNota" + i);
				if (utilizarNota && apresentarNota) {
					TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTituloVO = (TurmaDisciplinaNotaTituloVO) UtilReflexao.invocarMetodoGet(ca, "turmaDisciplinaNotaTitulo"+ i);
					if(Uteis.isAtributoPreenchido(turmaDisciplinaNotaTituloVO)){
						lista.add(new SelectItem(tituloNota, turmaDisciplinaNotaTituloVO.getTituloNotaApresentar()));
					}else{
						lista.add(new SelectItem(tituloNota, tituloNotaApresentar));
					}
				}
			}
		}
		setListaSelectItemTipoInformarNota(lista);
	}

	public void montarListaSelectItemDisciplinas() {
		try {
			montarListaSelectItemDisciplinas("");
		} catch (Exception e) {
			// System.out.println("Erro HistoricoTurmaControle.montarListaSelectItemDisciplinas: " + e.getMessage());
		}
	}

	public void montarListaSelectItemDisciplinas(String prm) throws Exception {

		if (getTurmaVO().getTurmaAgrupada()) {
			montarListaDisciplinaAgrupada();
		} else {
			montarListaDisciplinaNaoAgrupada();
		}

	}

	public void montarListaDisciplinaAgrupada() throws Exception {
		if (getTurmaVO().getCodigo().intValue() == 0) {
			setListaSelectItemDisciplinas(new ArrayList(0));
			return;
		}
		List resultadoConsulta = consultarDisciplinaTurmaAgrupada();
		Iterator i = resultadoConsulta.iterator();
		List objs = new ArrayList(0);
		objs.add(new SelectItem(0, ""));
		while (i.hasNext()) {
			DisciplinaVO obj = (DisciplinaVO) i.next();
			objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		setListaSelectItemDisciplinas(objs);
	}

	public List<DisciplinaVO> consultarDisciplinaTurmaAgrupada() throws Exception {
		return getFacadeFactory().getDisciplinaFacade().consultarDisciplinaTurmaAgrupada(getTurmaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
	}

	public void montarListaDisciplinaNaoAgrupada() throws Exception {
		if (this.getTurmaVO().getCodigo().intValue() == 0) {
			setListaSelectItemDisciplinas(new ArrayList(0));
			return;
		}
		List resultadoConsulta = new ArrayList(0);
		if (getTurmaVO().getPeridoLetivo().getGradeDisciplinaVOs().equals(new ArrayList(0))) {
			resultadoConsulta = consultarDisciplinasPorNome("");
		} else {
			resultadoConsulta = getTurmaVO().getPeridoLetivo().getGradeDisciplinaVOs();
		}
		Iterator i = resultadoConsulta.iterator();
		List objs = new ArrayList(0);
		objs.add(new SelectItem(0, ""));
		while (i.hasNext()) {
			GradeDisciplinaVO obj = (GradeDisciplinaVO) i.next();
			objs.add(new SelectItem(obj.getDisciplina().getCodigo(), obj.getDisciplina().getNome()));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		setListaSelectItemDisciplinas(objs);
	}

	/**
	 * Método responsável por consultar dados da entidade
	 * <code><code> e montar o atributo <code>nome</code> Este atributo é uma
	 * lista (<code>List</code>) utilizada para definir os valores a serem
	 * apresentados no ComboBox correspondente
	 */
	public List consultarDisciplinasPorNome(String nomePrm) throws Exception {
		return getFacadeFactory().getGradeDisciplinaFacade().consultarGradeDisciplinas(getTurmaVO().getPeridoLetivo().getCodigo(), false, getUsuarioLogado(), null);
	}

	public List apresentarTipoInformarNota(ConfiguracaoAcademicoVO ca) {
		List h = new ArrayList(0);
		if (ca.getUtilizarNota1().booleanValue()) {
			if (ca.getFormulaCalculoNota1().equals("")) {
				h.add(ca.getTituloNota1());
			}
		}
		if (ca.getUtilizarNota2().booleanValue()) {
			if (ca.getFormulaCalculoNota2().equals("")) {
				h.add(ca.getTituloNota2());
			}
		}
		if (ca.getUtilizarNota3().booleanValue()) {
			if (ca.getFormulaCalculoNota3().equals("")) {
				h.add(ca.getTituloNota3());
			}
		}
		if (ca.getUtilizarNota4().booleanValue()) {
			if (ca.getFormulaCalculoNota4().equals("")) {
				h.add(ca.getTituloNota4());
			}
		}
		if (ca.getUtilizarNota5().booleanValue()) {
			if (ca.getFormulaCalculoNota5().equals("")) {
				h.add(ca.getTituloNota5());
			}
		}
		if (ca.getUtilizarNota6().booleanValue()) {
			if (ca.getFormulaCalculoNota6().equals("")) {
				h.add(ca.getTituloNota6());
			}
		}
		if (ca.getUtilizarNota7().booleanValue()) {
			if (ca.getFormulaCalculoNota7().equals("")) {
				h.add(ca.getTituloNota7());
			}
		}
		if (ca.getUtilizarNota8().booleanValue()) {
			if (ca.getFormulaCalculoNota8().equals("")) {
				h.add(ca.getTituloNota8());
			}
		}
		if (ca.getUtilizarNota9().booleanValue()) {
			if (ca.getFormulaCalculoNota9().equals("")) {
				h.add(ca.getTituloNota9());
			}
		}
		if (ca.getUtilizarNota10().booleanValue()) {
			if (ca.getFormulaCalculoNota10().equals("")) {
				h.add(ca.getTituloNota10());
			}
		}
		if (ca.getUtilizarNota11().booleanValue()) {
			if (ca.getFormulaCalculoNota11().equals("")) {
				h.add(ca.getTituloNota11());
			}
		}
		if (ca.getUtilizarNota12().booleanValue()) {
			if (ca.getFormulaCalculoNota12().equals("")) {
				h.add(ca.getTituloNota12());
			}
		}
		if (ca.getUtilizarNota13().booleanValue()) {
			if (ca.getFormulaCalculoNota13().equals("")) {
				h.add(ca.getTituloNota13());
			}
		}
		return h;
	}

	public Boolean isPeriodoLancamentoNota() {
		if (getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().getCodigo().equals(0)) {
			
			if ((getCalendarioLancamentoNotaVO().getDataInicioCalculoMediaFinal() == null ||
					getCalendarioLancamentoNotaVO().getDataInicioCalculoMediaFinal().before(new Date()) ||
					Uteis.getData(getCalendarioLancamentoNotaVO().getDataInicioCalculoMediaFinal()).equals(Uteis.getData(new Date())))
				&&
				(getCalendarioLancamentoNotaVO().getDataTerminoCalculoMediaFinal() == null ||
					getCalendarioLancamentoNotaVO().getDataTerminoCalculoMediaFinal().after(new Date()) ||
					Uteis.getData(getCalendarioLancamentoNotaVO().getDataTerminoCalculoMediaFinal()).equals(Uteis.getData(new Date())))) {
				return Boolean.TRUE;
			} else {
				return Boolean.FALSE;
			}
		} else {
			return Boolean.TRUE;
		}
	}

	public void calcularMedia(ConfiguracaoAcademicoVO ca, HistoricoVO histVO, Map<Integer, ConfiguracaoAcademicoVO> mapConf) throws Exception {
		
		String situacaoAtual = histVO.getSituacao();
		boolean resultado = false;
		if (isPeriodoLancamentoNota()) {
			try {
				histVO.setFreguencia(0.0);
				montarFreguenciaAluno(histVO, ca);
				verificaAlunoReprovadoFalta(histVO);
				if(histVO.getHistoricoDisciplinaComposta() && !Uteis.isAtributoPreenchido(histVO.getHistoricoDisciplinaFilhaComposicaoVOs())) {
					histVO.setHistoricoDisciplinaFilhaComposicaoVOs(getFacadeFactory().getHistoricoFacade().consultaRapidaHistoricoFazParteComposicaoPorMatriculaPorGradeCurricularPorMatriculaPeriodo(histVO.getMatricula().getMatricula(), 0, histVO.getMatriculaPeriodo().getCodigo(), histVO.getMatrizCurricular().getCodigo(), histVO.getGradeDisciplinaVO().getCodigo(), histVO.getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo(), false, getUsuarioLogado()));
					if(!mapConf.containsKey(histVO.getConfiguracaoAcademico().getCodigo())){
						mapConf.put(histVO.getConfiguracaoAcademico().getCodigo(), histVO.getConfiguracaoAcademico());
					}
					mapConf.put(histVO.getConfiguracaoAcademico().getCodigo(), histVO.getConfiguracaoAcademico());
					for (HistoricoVO obj : histVO.getHistoricoDisciplinaFilhaComposicaoVOs()) {
						if(!obj.getConfiguracaoAcademico().getNivelMontarDados().equals(NivelMontarDados.TODOS)) {
							if (!mapConf.containsKey(obj.getConfiguracaoAcademico().getCodigo())) {			
								mapConf.put(obj.getConfiguracaoAcademico().getCodigo(), getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorChavePrimaria(obj.getConfiguracaoAcademico().getCodigo(), getUsuarioLogado()));
							}
							obj.setConfiguracaoAcademico(mapConf.get(obj.getConfiguracaoAcademico().getCodigo()));
						}
					}	
				}
				resultado = ca.substituirVariaveisFormulaPorValores(histVO, histVO.getHistoricoDisciplinaFilhaComposicaoVOs(), true);
				if(!resultado){
					if(!histVO.getSituacao().equals("RF")){
						if(histVO.getMediaFinal() != null ){													
							histVO.setSituacao("RE");						
						}else{						
							histVO.setSituacao("CS");
						}
					}
				}
				if(histVO.getHistoricoDisciplinaFazParteComposicao()){					
					resultado = getFacadeFactory().getHistoricoFacade().executarAtualizacaoDisciplinaComposta(histVO, histVO.getHistoricoDisciplinaFilhaComposicaoVOs(), getTipoAlteracaoSituacaoHistorico(), false, resultado, getUsuarioLogado());								
				}else if(histVO.getHistoricoDisciplinaComposta()){
					getFacadeFactory().getHistoricoFacade().executarAtualizacaoDisciplinaFilhaComposicaoComBaseDisciplinaCompostaComposta(histVO,  getTipoAlteracaoSituacaoHistorico(), false, getUsuarioLogado());
				}else {					
					histVO.setEmRecuperacao(false);
					histVO.getHistoricoNotaVOs().clear();
					getFacadeFactory().getHistoricoFacade().realizarCriacaoHistoricoNotaVO(histVO, histVO.getHistoricoDisciplinaFilhaComposicaoVOs());
				}
			} catch (FechamentoPeriodoLetivoException e) {
				histVO.setMediaFinal(null);
				getFacadeFactory().getLogFechamentoFacade().realizarRegistroLogFechamento(histVO.getMatricula().getMatricula());
			}
			if (histVO.getMediaFinal() != null) {
				montarFreguenciaAluno(histVO, ca);
				verificaAlunoReprovadoFalta(histVO);
				if ((!histVO.getSituacao().equals(SituacaoHistorico.REPROVADO_FALTA.getValor())) && (!histVO.getSituacao().equals(SituacaoHistorico.ISENTO.getValor())) && !histVO.getSituacao().equals("")) {
					if (resultado) {
						histVO.setSituacao(SituacaoHistorico.APROVADO.getValor());
					} else {
						histVO.setSituacao(SituacaoHistorico.REPROVADO.getValor());
					}
				}
				realizarAtualizacaoSituacaoRegraEstabelecidaComboBoxTipoAlteracaoSituacaoHistorico(histVO, getTipoAlteracaoSituacaoHistorico(), situacaoAtual);
			} else  if(!situacaoAtual.equals(SituacaoHistorico.TRANCAMENTO.getValor()) && !situacaoAtual.equals(SituacaoHistorico.ABANDONO_CURSO.getValor()) && !situacaoAtual.equals(SituacaoHistorico.CANCELADO.getValor())) {
				histVO.setSituacao(SituacaoHistorico.CURSANDO.getValor());
			}
		}
		getFacadeFactory().getHistoricoFacade().realizarAlteracaoSituacaoHistoricoReprovadoPeriodoLetivoDeAcordoRegraConfiguracaoAcademica(histVO, situacaoAtual, getAno(), getSemestre(), getUsuarioLogado());
}

public void verificarAprovacaoAluno() {
	try {
		Iterator i = getAlunosTurma().iterator();
		ConfiguracaoAcademicoVO ca = this.getConfiguracaoAcademicoVO();
		Map<Integer, ConfiguracaoAcademicoVO> mapConf = new HashMap<Integer, ConfiguracaoAcademicoVO>(0);
		while (i.hasNext()) {
			HistoricoVO histVO = (HistoricoVO) i.next();
			histVO.setMediaFinal(null);
			histVO.setMediaFinalConceito(null);
			if (getUsuarioLogado().getPerfilAcesso().getCodigo().equals(getConfiguracaoGeralPadraoSistema().getPerfilPadraoProfessorGraduacao().getCodigo()) && histVO.getIsProfessorNaoPodeAlterarRegistro()) {
				continue;
			} else {
				histVO.setConfiguracaoAcademico(ca);
				getFacadeFactory().getHistoricoFacade().verificarNotasLancadas(histVO, getUsuarioLogado());
                                    getFacadeFactory().getConfiguracaoAcademicoFacade().prepararVariaveisNotaParaSubstituicaoFormulaNota(ca, histVO, getUsuarioLogado());
				try {
					calcularMedia(ca, histVO, mapConf);
					histVO.setHistoricoSalvo(Boolean.FALSE);
					histVO.setHistoricoCalculado(Boolean.TRUE);
				} catch (Exception e) {
					setMensagemDetalhada("msg_erro", e.getMessage());
					montarFreguenciaAluno(histVO, ca);
					verificaAlunoReprovadoFalta(histVO);
					if (!histVO.getSituacao().equals("RF")) {
						histVO.setSituacao("CS");
					}
					histVO.setMediaFinal(null);
				}
			}
		}

	} catch (Exception e) {
		getHistoricoVO().setSituacao("VS");

		setMensagemID("msg_erro_dadosnaoencontrados");
	}
}


public void verificarAprovacaoAluno(HistoricoVO histVO, Map<Integer, ConfiguracaoAcademicoVO> mapConf) {
	try {
		ConfiguracaoAcademicoVO ca = this.getConfiguracaoAcademicoVO();
		histVO.setMediaFinal(null);
		histVO.setMediaFinalConceito(null);

		histVO.setConfiguracaoAcademico(ca);
		getFacadeFactory().getHistoricoFacade().verificarNotasLancadas(histVO, getUsuarioLogado());
		getFacadeFactory().getConfiguracaoAcademicoFacade().prepararVariaveisNotaParaSubstituicaoFormulaNota(ca, histVO, getUsuarioLogado());
		try {
			calcularMedia(ca, histVO, mapConf);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			montarFreguenciaAluno(histVO, ca);
			verificaAlunoReprovadoFalta(histVO);
			if (!histVO.getSituacao().equals("RF")) {
				histVO.setSituacao("CS");
			}
			histVO.setMediaFinal(null);
		}

	} catch (Exception e) {
		getHistoricoVO().setSituacao("VS");

		setMensagemID("msg_erro_dadosnaoencontrados");
	}
}

	/**
	 * Método responsável por processar a consulta na entidade
	 * <code>Turma</code> por meio de sua respectiva chave primária. Esta rotina
	 * é utilizada fundamentalmente por requisições Ajax, que realizam busca
	 * pela chave primária da entidade montando automaticamente o resultado da
	 * consulta para apresentação.
	 */
	public void consultarTurmaPorIdentificador() {
		try {

			this.setAlunosTurma(new ArrayList(0));
			String campoConsulta = getTurmaVO().getIdentificadorTurma();
			if (campoConsulta.equalsIgnoreCase("")) {
				throw new Exception();
			} else {
				TurmaVO turma = getFacadeFactory().getTurmaFacade().consultarTurmaPorIdentificadorTurma(campoConsulta, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
				setTurmaVO(turma);
				montarConfiguracaoAcademico();
				montarListaSelectPeriodosLetivos();
				getHistoricoVO().getMatriculaPeriodo().setPeridoLetivo(turma.getPeridoLetivo());
				getHistoricoVO().getMatriculaPeriodo().setGradeCurricular(turma.getGradeCurricularVO());
				montarListaSelectItemDisciplinas();
				if (!getTurmaVO().getIntegral() && getAno().isEmpty()) {
					setAno(Uteis.getAnoDataAtual4Digitos());
				}

				setMensagemID("msg_dados_consultados");
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaSelectItemDisciplinas(new ArrayList(0));
			setListaSelectItemTipoInformarNota(new ArrayList(0));
			setAlunosTurma(new ArrayList(0));
			setMensagemID("msg_erro_dadosnaoencontrados");
			setTurmaVO(new TurmaVO());
			setMensagemDetalhada(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
		}
	}

	public void montarListaHistoricoAlunoDisciplina() {
		try {
			this.setAlunosTurma(new ArrayList(0));
			if (getHistoricoVO().getDisciplina().getCodigo().intValue() == 0) {
				throw new Exception();
			}
			if (getTurmaVO().getCodigo().intValue() == 0) {
				throw new Exception();
			}
			montarAlunosTurma(Boolean.TRUE, Boolean.FALSE);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			this.setAlunosTurma(new ArrayList(0));
		}

	}

	public void inicializarSemestreAno() throws Exception {
		if (getTurmaVO().getIntegral()) {
			setAno("");
			setSemestre("");
		}
	}

	public void consultarTurmaPorChavePrimaria() {
		try {
			getListaAnterior().clear();
			this.setAlunosTurma(new ArrayList(0));
			this.setConsultaIndividualPorAluno(Boolean.FALSE);
			this.setConsultaPorInicialNome(Boolean.FALSE);
			this.setPossuiHistoricoAlteradoLista(Boolean.FALSE);
			setCalcularMediaApenasParaDadosAlterados(Boolean.TRUE);
			setCalcularMediaParaTodosAlunos(Boolean.FALSE);
			if(getControleConsultaOtimizado().getLimitePorPagina() == 0) {
				getControleConsultaOtimizado().setLimitePorPagina(10);
			}
			this.setLetra("");
			getMapHistoricoAlteradoVOs().clear();
			// inicializarSemestreAno();
			HistoricoVO.validarDadosHistoricoTurma(getTurmaVO(), getHistoricoVO().getDisciplina().getCodigo(), getAno(), getSemestre());
			boolean turmaDisciplinaEad = getFacadeFactory().getTurmaDisciplinaFacade().consultarSeTurmaDisciplinaSaoEad(getTurmaVO().getCodigo(), getHistoricoVO().getDisciplina().getCodigo(), getUsuarioLogado());
			
			boolean professorExclusivo = false;
			if (!Uteis.isAtributoPreenchido(getCalendarioLancamentoNotaVO())) {
				CalendarioLancamentoNotaVO obj = getFacadeFactory().getCalendarioLancamentoNotaFacade().consultarPorCalendarioAcademicoProfessorExcluisoLancamentoNota(getTurmaVO().getUnidadeEnsino().getCodigo(), getTurmaVO().getCodigo(), getTurmaVO().getTurmaAgrupada(), getUsuarioLogado().getPessoa().getCodigo(), getHistoricoVO().getDisciplina().getCodigo(), getConfiguracaoAcademicoVO().getCodigo(), getTurmaVO().getPeriodicidade(), getAno(), getSemestre(), false, getUsuarioLogado());
				professorExclusivo = getFacadeFactory().getHistoricoFacade().professorExclusivoLancamentoNota(obj, getUsuarioLogado()); 
			}
			
			if(!professorExclusivo && !turmaDisciplinaEad && !getFacadeFactory().getDisciplinaFacade().realizarVerificacaoDisciplinaECompostaTurma(getTurmaVO().getCodigo(), getHistoricoVO().getDisciplina().getCodigo())){
				Date dataPrimeiraAula = null;
				if (getTurmaVO().getCurso().getNivelEducacionalPosGraduacao()) {
					dataPrimeiraAula = getFacadeFactory().getHorarioTurmaFacade().consultarPrimeiroDiaAulaTurmaDisciplina(getTurmaVO().getCodigo(), getHistoricoVO().getDisciplina().getCodigo(), "", "");
				} else {
					dataPrimeiraAula = getFacadeFactory().getHorarioTurmaFacade().consultarPrimeiroDiaAulaTurmaDisciplina(getTurmaVO().getCodigo(), getHistoricoVO().getDisciplina().getCodigo(), getAno(), getSemestre());
				}
				if (dataPrimeiraAula == null || Uteis.getDataJDBC(dataPrimeiraAula).compareTo(Uteis.getDataJDBC(new Date())) >= 1) {
					if (dataPrimeiraAula != null) {
						throw new Exception(UteisJSF.internacionalizar("msg_Historico_aulaNaoIniciada").replace("{0}", Uteis.getData(dataPrimeiraAula)));
					}
					throw new Exception(UteisJSF.internacionalizar("msg_Historico_aulaNaoIniciada").replace("{0}", "Data não cadastrada"));
				}
			}

			boolean trazerAlunoPendenteFinanceiramente = getFacadeFactory().getConfiguracaoGeralSistemaFacade().executarVerificacaoApresentarAlunoPendenteFinanceiramente(getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado());			
			boolean permitiVisualizarAlunoTR_CA = verificarPermissaoVisualizarAlunoTR_CA();
			List<ConfiguracaoAcademicoVO> configuracaoAcademicoVOs = getFacadeFactory().getConfiguracaoAcademicoFacade().consultaRapidaPorUnidadeEnsinoCursoDisciplinaTurmaAnoSemestreSituacaoMatSituacaoHistFiltroVisaoProfessor(0, 0, getHistoricoVO().getDisciplina().getCodigo(), getTurmaVO(), ano, semestre, "", "'AA', 'CC', 'CH', 'IS'", false, true, trazerAlunoPendenteFinanceiramente, permitiVisualizarAlunoTR_CA ,false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			setPossuiDiversidadeConfiguracaoAcademico(configuracaoAcademicoVOs.size() > 1);
			if(configuracaoAcademicoVOs.size() > 1) {
				getMapConfiguracaoAcademicoVOs().clear();
				for(ConfiguracaoAcademicoVO configuracaoAcademicoVO: configuracaoAcademicoVOs) {
					getMapConfiguracaoAcademicoVOs().put(configuracaoAcademicoVO.getCodigo(), configuracaoAcademicoVO);	
				}
				montarComboboxConfiguracaoAcademico();				
			}
			montarAlunosTurma(Boolean.TRUE, professorExclusivo);
			// realizarCloneListaHistorico();			
			validarDadosComparacaoListaHistorico();
			getFacadeFactory().getHistoricoFacade().realizarVerificacaoBloqueiNotaDisciplinaComposta(getAlunosTurma(), getConfiguracaoAcademicoVO(), getUsuarioLogado());
			getFacadeFactory().getTurmaDisciplinaNotaTituloFacade().realizarCriacaoTurmaDisciplinaNotaTituloComBaseTipoTurma(getTurmaVO(), getHistoricoVO().getDisciplina(), getAno(), getSemestre(), this.getConfiguracaoAcademicoVO(), getUsuarioLogado());
			montarListaOpcoesNotas(this.getConfiguracaoAcademicoVO());
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			this.setAlunosTurma(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void consultarVisaoCoordenador() {
		try {
			getListaAnterior().clear();
			getControleConsultaOtimizado().getListaConsulta().clear();
			this.setAlunosTurma(new ArrayList(0));
			// inicializarSemestreAno();
			HistoricoVO.validarDadosHistoricoTurma(getTurmaVO(), getHistoricoVO().getDisciplina().getCodigo(), getAno(), getSemestre());
			if(!getFacadeFactory().getDisciplinaFacade().realizarVerificacaoDisciplinaECompostaTurma(getTurmaVO().getCodigo(), getHistoricoVO().getDisciplina().getCodigo())){
			Date dataPrimeiraAula = null;
			if(getTurmaVO().getCurso().getNivelEducacionalPosGraduacao()) {
				dataPrimeiraAula = getFacadeFactory().getHorarioTurmaFacade().consultarPrimeiroDiaAulaTurmaDisciplina(getTurmaVO().getCodigo(), getHistoricoVO().getDisciplina().getCodigo(), "", "");
			} else {
				dataPrimeiraAula = getFacadeFactory().getHorarioTurmaFacade().consultarPrimeiroDiaAulaTurmaDisciplina(getTurmaVO().getCodigo(), getHistoricoVO().getDisciplina().getCodigo(), getAno(), getSemestre()); 
			}
			if (dataPrimeiraAula == null || Uteis.getDataJDBC(dataPrimeiraAula).compareTo(Uteis.getDataJDBC(new Date())) >= 1) {
				if (dataPrimeiraAula != null) {
					throw new Exception(UteisJSF.internacionalizar("msg_Historico_aulaNaoIniciada").replace("{0}", Uteis.getData(dataPrimeiraAula)));
				}
				throw new Exception(UteisJSF.internacionalizar("msg_Historico_aulaNaoIniciada").replace("{0}", "Data não cadastrada"));
			}
			}
			consultarAlunosTurmaVisaoCoordenador();
			// realizarCloneListaHistorico();
			validarDadosComparacaoListaHistorico();
			getFacadeFactory().getTurmaDisciplinaNotaTituloFacade().realizarCriacaoTurmaDisciplinaNotaTituloComBaseTipoTurma(getTurmaVO(), getHistoricoVO().getDisciplina(), getAno(), getSemestre(), this.getConfiguracaoAcademicoVO(), getUsuarioLogado());
			montarListaOpcoesNotas(getConfiguracaoAcademicoVO());
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			this.setAlunosTurma(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	List listaSelectSemestre;

	public List getListaSelectSemestre() {
		if (listaSelectSemestre == null) {
			listaSelectSemestre = new ArrayList(0);
			listaSelectSemestre.add(new SelectItem("", " "));
			listaSelectSemestre.add(new SelectItem("1", "1º"));
			listaSelectSemestre.add(new SelectItem("2", "2º"));
		}
		return listaSelectSemestre;
	}
	
	public void consultarAlunosTurmaVisaoCoordenador() {

		try {
			this.setAlunosTurma(new ArrayList<HistoricoVO>(0));
			if (!getTurmaVO().getTurmaAgrupada().booleanValue()) {
				if (getTurmaVO().getCurso().getCodigo().intValue() == 0 && !getTurmaVO().getSubturma()) {
					throw new Exception(getMensagemInternalizacao("msg_LancamentoNota_curso"));
				}
			} else {
				getTurmaVO().getCurso().setLiberarRegistroAulaEntrePeriodo(getFacadeFactory().getTurmaFacade().consultarLiberarRegistroAulaEnterPeriodoTurmaAgrupada(getTurmaVO().getCodigo(), getUsuarioLogado()));
			}
			if (getHistoricoVO().getDisciplina().getCodigo().intValue() == 0) {
				throw new Exception(getMensagemInternalizacao("msg_LancamentoNota_disciplina"));
			}
			if (this.getTurmaVO().getIntegral()) {
				ano = "";
				semestre = "";
			}
			if (this.getTurmaVO().getAnual()) {
				// ano = Uteis.getAnoDataAtual();
				semestre = "";
			}
			// if (this.getTurmaVO().getSemestral()) {
			// ano = Uteis.getAnoDataAtual();
			// semestre = Uteis.getSemestreAtual();
			// }

			if (this.getTurmaVO().getCurso().getLiberarRegistroAulaEntrePeriodo()) {
				ano = "";
				semestre = "";
			}
			boolean trazerAlunoPendenteFinanceiramente = getFacadeFactory().getConfiguracaoGeralSistemaFacade().executarVerificacaoApresentarAlunoPendenteFinanceiramente(getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado());
			boolean permitiVisualizarAlunoTR_CA = verificarPermissaoVisualizarAlunoTR_CA();
			List<HistoricoVO> lista = null;
			if (getPossuiDiversidadeConfiguracaoAcademico()) {
				if (!this.getConfiguracaoAcademicoVO().getCodigo().equals(0)) {
					lista = getFacadeFactory().getHistoricoFacade().consultaRapidaPorUnidadeEnsinoCursoDisciplinaTurmaAnoSemestreSituacaoMatSituacaoHistFiltroVisaoProfessorRegistroNota(0, 0, getHistoricoVO().getDisciplina().getCodigo(), getTurmaVO(), ano, semestre, trazerAlunoPendenteFinanceiramente, "'AA', 'CC', 'CH', 'IS'", false, getUsuarioLogado().getIsApresentarVisaoProfessor(), false, this.getConfiguracaoAcademicoVO(), permitiVisualizarAlunoTR_CA, getPermiteLancarNotaDisciplinaComposta(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema().getPermitirProfessorRealizarLancamentoAlunosPreMatriculados());
					montarConfiguracaoAcademicoPorTurma();
				}
				this.setAlunosTurma(lista);
			} else {
				lista = getFacadeFactory().getHistoricoFacade().consultaRapidaPorUnidadeEnsinoCursoDisciplinaTurmaAnoSemestreSituacaoMatSituacaoHistFiltroVisaoProfessorRegistroNota(0, 0, getHistoricoVO().getDisciplina().getCodigo(), getTurmaVO(), ano, semestre, trazerAlunoPendenteFinanceiramente, "'AA', 'CC', 'CH', 'IS'", false, getUsuarioLogado().getIsApresentarVisaoProfessor(), false, null, permitiVisualizarAlunoTR_CA, getPermiteLancarNotaDisciplinaComposta(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema().getPermitirProfessorRealizarLancamentoAlunosPreMatriculados());
				this.setAlunosTurma(lista);
				realizarVerificacaoDiversidadeConfiguracaoAcademico();
			}

			// TODO Alberto 13/12/2010 Corrigido para apresentar alunos de
			// outros cursos
			// lista =
			// getFacadeFactory().getHistoricoFacade().consultaRapidaPorUnidadeEnsinoCursoDisciplinaTurmaAnoSemestreSituacaoMatSituacaoHistFiltroVisaoProfessorRegistroNota(0,
			// 0, getHistoricoVO().getDisciplina().getCodigo(),
			// getTurmaVO().getCodigo(), ano, semestre,
			// trazerAlunoPendenteFinanceiramente, "", false, true, false,
			// Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			// TODO Alberto 13/12/2010 Corrigido para apresentar alunos de
			// outros cursos
			// List<HistoricoVO> lista =
			// getFacadeFactory().getHistoricoFacade().consultaRapidaPorUnidadeEnsinoCursoDisciplinaTurmaAnoSemestreSituacao(getUnidadeEnsinoLogado().getCodigo(),
			// getTurmaVO().getCurso().getCodigo(),
			// historicoVO.getDisciplina().getCodigo(),
			// getTurmaVO().getCodigo(), ano, semestre, "AT", false, false,
			// Uteis.NIVELMONTARDADOS_TODOS);
			// this.setAlunosTurma(lista);
			// montarConfiguracaoAcademico();
			setExisteRegistroAula(verificarExistenciaRegistroAula(null, getTurmaVO().getCodigo(), getHistoricoVO().getDisciplina().getCodigo(), null, semestre, ano));
			if (getExisteRegistroAula()) {
				preencherFrequenciaAlunos(getAlunosTurma());
			}
			getFacadeFactory().getTurmaDisciplinaNotaTituloFacade().realizarCriacaoTurmaDisciplinaNotaTituloComBaseTipoTurma(getTurmaVO(), getHistoricoVO().getDisciplina(), getAno(), getSemestre(), this.getConfiguracaoAcademicoVO(), getUsuarioLogado());
			montarListaOpcoesNotas(getConfiguracaoAcademicoVO());
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			this.setAlunosTurma(new ArrayList<HistoricoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void montarAlunosTurma(Boolean utilizarOffSet, boolean considerarProfessorExclusivo) throws Exception {
		this.setAlunosTurma(new ArrayList<HistoricoVO>(0));
		this.getControleConsultaOtimizado().setListaConsulta(new ArrayList(0));
		if (!getTurmaVO().getTurmaAgrupada().booleanValue()) {
			if (getTurmaVO().getCurso().getCodigo().intValue() == 0 && !getTurmaVO().getSubturma()) {
				throw new Exception(getMensagemInternalizacao("msg_LancamentoNota_curso"));
			}
		} else {
			getTurmaVO().getCurso().setLiberarRegistroAulaEntrePeriodo(getFacadeFactory().getTurmaFacade().consultarLiberarRegistroAulaEnterPeriodoTurmaAgrupada(getTurmaVO().getCodigo(), getUsuarioLogado()));
		}
		if (getHistoricoVO().getDisciplina().getCodigo().intValue() == 0) {
			throw new Exception(getMensagemInternalizacao("msg_LancamentoNota_disciplina"));
		}
		if ((getTurmaVO().getAnual() || getTurmaVO().getSemestral()) && !Uteis.isAtributoPreenchido(getAno()) && !getTurmaVO().getCurso().getLiberarRegistroAulaEntrePeriodo()) {
			throw new Exception(getMensagemInternalizacao("msg_AlunosMatriculadosGeralRel_ano"));
		} else if (getTurmaVO().getSemestral() && !Uteis.isAtributoPreenchido(getSemestre()) && !getTurmaVO().getCurso().getLiberarRegistroAulaEntrePeriodo()) {
			throw new Exception(getMensagemInternalizacao("msg_AlunosMatriculadosGeralRel_semestre"));
		}
//		if (getTurmaVO().getCurso().getLiberarRegistroAulaEntrePeriodo()) {
//			setAno("");
//			setSemestre("");
//		}
		boolean trazerAlunoPendenteFinanceiramente = getFacadeFactory().getConfiguracaoGeralSistemaFacade().executarVerificacaoApresentarAlunoPendenteFinanceiramente(getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado());
		
		boolean permitiVisualizarAlunoTR_CA = verificarPermissaoVisualizarAlunoTR_CA();
		
		boolean permitirRealizarLancamentoAlunosPreMatriculados = getConfiguracaoGeralPadraoSistema().getPermitirProfessorRealizarLancamentoAlunosPreMatriculados();
		
		List<HistoricoVO> lista = null;
		if (getPossuiDiversidadeConfiguracaoAcademico()) {
			if (!this.getConfiguracaoAcademicoVO().getCodigo().equals(0)) {
				if (utilizarOffSet) {
					lista = getFacadeFactory().getHistoricoFacade().consultaRapidaPorUnidadeEnsinoCursoDisciplinaTurmaAnoSemestreSituacaoMatSituacaoHistFiltroVisaoProfessorRegistroNotaLimiteOffSet(0, 0, getHistoricoVO().getDisciplina().getCodigo(), getTurmaVO(), ano, semestre, trazerAlunoPendenteFinanceiramente, "'AA', 'CC', 'CH', 'IS'", false, true, getUsuarioLogado().getPessoa().getCodigo(), false, this.getConfiguracaoAcademicoVO(), permitiVisualizarAlunoTR_CA, getPermiteLancarNotaDisciplinaComposta(), Uteis.NIVELMONTARDADOS_TODOS, getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), getUsuarioLogado(), permitirRealizarLancamentoAlunosPreMatriculados, considerarProfessorExclusivo);
					getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getHistoricoFacade().consultaRapidaTotalizadoPorUnidadeEnsinoCursoDisciplinaTurmaAnoSemestreSituacaoMatSituacaoHistFiltroVisaoProfessorRegistroNotaLimiteOffSet(0, 0, getHistoricoVO().getDisciplina().getCodigo(), getTurmaVO(), ano, semestre, trazerAlunoPendenteFinanceiramente, "'AA', 'CC', 'CH', 'IS'", false, true, false, this.getConfiguracaoAcademicoVO(), permitiVisualizarAlunoTR_CA, getPermiteLancarNotaDisciplinaComposta(), Uteis.NIVELMONTARDADOS_TODOS, getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), getUsuarioLogado(), permitirRealizarLancamentoAlunosPreMatriculados, considerarProfessorExclusivo));
				} else {
					lista = getFacadeFactory().getHistoricoFacade().consultaRapidaPorUnidadeEnsinoCursoDisciplinaTurmaAnoSemestreSituacaoMatSituacaoHistFiltroVisaoProfessorRegistroNotaLimiteOffSet(0, 0, getHistoricoVO().getDisciplina().getCodigo(), getTurmaVO(), ano, semestre, trazerAlunoPendenteFinanceiramente, "'AA', 'CC', 'CH', 'IS'", false, true, getUsuarioLogado().getPessoa().getCodigo(),false, this.getConfiguracaoAcademicoVO(), permitiVisualizarAlunoTR_CA,getPermiteLancarNotaDisciplinaComposta(),  Uteis.NIVELMONTARDADOS_TODOS, getControleConsultaOtimizado().getLimitePorPagina(), 0, getUsuarioLogado(), permitirRealizarLancamentoAlunosPreMatriculados, considerarProfessorExclusivo);
					getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getHistoricoFacade().consultaRapidaTotalizadoPorUnidadeEnsinoCursoDisciplinaTurmaAnoSemestreSituacaoMatSituacaoHistFiltroVisaoProfessorRegistroNotaLimiteOffSet(0, 0, getHistoricoVO().getDisciplina().getCodigo(), getTurmaVO(), ano, semestre, trazerAlunoPendenteFinanceiramente, "'AA', 'CC', 'CH', 'IS'", false, true, false, this.getConfiguracaoAcademicoVO(), permitiVisualizarAlunoTR_CA,getPermiteLancarNotaDisciplinaComposta(),  Uteis.NIVELMONTARDADOS_TODOS, getControleConsultaOtimizado().getLimitePorPagina(), 0, getUsuarioLogado(), permitirRealizarLancamentoAlunosPreMatriculados, considerarProfessorExclusivo));
				}
				montarConfiguracaoAcademicoPorTurma();				
			}
			this.setAlunosTurma(lista);
		} else {
			if (utilizarOffSet) {
				lista = getFacadeFactory().getHistoricoFacade().consultaRapidaPorUnidadeEnsinoCursoDisciplinaTurmaAnoSemestreSituacaoMatSituacaoHistFiltroVisaoProfessorRegistroNotaLimiteOffSet(0, 0, getHistoricoVO().getDisciplina().getCodigo(), getTurmaVO(), ano, semestre, trazerAlunoPendenteFinanceiramente, "'AA', 'CC', 'CH', 'IS'", false, true, getUsuarioLogado().getPessoa().getCodigo(), false, null, permitiVisualizarAlunoTR_CA, getPermiteLancarNotaDisciplinaComposta(), Uteis.NIVELMONTARDADOS_TODOS, getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), getUsuarioLogado(), permitirRealizarLancamentoAlunosPreMatriculados, considerarProfessorExclusivo);
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getHistoricoFacade().consultaRapidaTotalizadoPorUnidadeEnsinoCursoDisciplinaTurmaAnoSemestreSituacaoMatSituacaoHistFiltroVisaoProfessorRegistroNotaLimiteOffSet(0, 0, getHistoricoVO().getDisciplina().getCodigo(), getTurmaVO(), ano, semestre, trazerAlunoPendenteFinanceiramente, "'AA', 'CC', 'CH', 'IS'", false, true, false, null, permitiVisualizarAlunoTR_CA, getPermiteLancarNotaDisciplinaComposta(), Uteis.NIVELMONTARDADOS_TODOS, getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), getUsuarioLogado(), permitirRealizarLancamentoAlunosPreMatriculados, considerarProfessorExclusivo));
			} else {
				lista = getFacadeFactory().getHistoricoFacade().consultaRapidaPorUnidadeEnsinoCursoDisciplinaTurmaAnoSemestreSituacaoMatSituacaoHistFiltroVisaoProfessorRegistroNotaLimiteOffSet(0, 0, getHistoricoVO().getDisciplina().getCodigo(), getTurmaVO(), ano, semestre, trazerAlunoPendenteFinanceiramente, "'AA', 'CC', 'CH', 'IS'", false, true, getUsuarioLogado().getPessoa().getCodigo(), false, null, permitiVisualizarAlunoTR_CA, getPermiteLancarNotaDisciplinaComposta(), Uteis.NIVELMONTARDADOS_TODOS, getControleConsultaOtimizado().getLimitePorPagina(), 0, getUsuarioLogado(), permitirRealizarLancamentoAlunosPreMatriculados, considerarProfessorExclusivo);
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getHistoricoFacade().consultaRapidaTotalizadoPorUnidadeEnsinoCursoDisciplinaTurmaAnoSemestreSituacaoMatSituacaoHistFiltroVisaoProfessorRegistroNotaLimiteOffSet(0, 0, getHistoricoVO().getDisciplina().getCodigo(), getTurmaVO(), ano, semestre, trazerAlunoPendenteFinanceiramente, "'AA', 'CC', 'CH', 'IS'", false, true, false, null, permitiVisualizarAlunoTR_CA, getPermiteLancarNotaDisciplinaComposta(), Uteis.NIVELMONTARDADOS_TODOS, getControleConsultaOtimizado().getLimitePorPagina(), 0, getUsuarioLogado(), permitirRealizarLancamentoAlunosPreMatriculados, considerarProfessorExclusivo));

			}
			this.setAlunosTurma(lista);
			realizarVerificacaoDiversidadeConfiguracaoAcademico();
		}
		if (lista != null && !lista.isEmpty()) {
			getControleConsultaOtimizado().setListaConsulta(lista);
			setPaginaAtual(getControleConsultaOtimizado().getPaginaAtual());
			setExisteRegistroAula(verificarExistenciaRegistroAula(null, getTurmaVO().getCodigo(), getHistoricoVO().getDisciplina().getCodigo(), null, semestre, ano));
			if (getExisteRegistroAula()) {
				preencherFrequenciaAlunos(getAlunosTurma());
			}
		}
		getFacadeFactory().getTurmaDisciplinaNotaTituloFacade().realizarCriacaoTurmaDisciplinaNotaTituloComBaseTipoTurma(getTurmaVO(), getHistoricoVO().getDisciplina(), getAno(), getSemestre(), this.getConfiguracaoAcademicoVO(), getUsuarioLogado());
		setMensagemID("msg_dados_consultados");
	}
	
	@Override
	public void anularDataModelo() {
		setControleConsultaOtimizado(null);
	}

	public void scrollerListener(DataScrollEvent dataScrollerEvent) throws Exception {
		realizarVerificacaoHistoricoAlteradoLista();
		
		boolean professorExclusivo = false;
		if (!Uteis.isAtributoPreenchido(getCalendarioLancamentoNotaVO())) {
			CalendarioLancamentoNotaVO obj = getFacadeFactory().getCalendarioLancamentoNotaFacade().consultarPorCalendarioAcademicoProfessorExcluisoLancamentoNota(getTurmaVO().getUnidadeEnsino().getCodigo(), getTurmaVO().getCodigo(), getTurmaVO().getTurmaAgrupada(), getUsuarioLogado().getPessoa().getCodigo(), getHistoricoVO().getDisciplina().getCodigo(), getConfiguracaoAcademicoVO().getCodigo(), getTurmaVO().getPeriodicidade(), getAno(), getSemestre(), false, getUsuarioLogado());
			professorExclusivo = getFacadeFactory().getHistoricoFacade().professorExclusivoLancamentoNota(obj, getUsuarioLogado()); 
		}
		
		if (getConsultaIndividualPorAluno() || getConsultaPorInicialNome()) {
			
			if (!getPossuiHistoricoAlteradoLista()) {
				getControleConsultaOtimizado().setPaginaAtual(dataScrollerEvent.getPage());
				getControleConsultaOtimizado().setPage(dataScrollerEvent.getPage());
				setConsultaDataScroller(true);
				consultarAlunoPorNome();
				getFacadeFactory().getHistoricoFacade().incluirHistoricoNotaParcial(getAlunosTurma(), getConfiguracaoAcademicaVO(), getUsuarioLogado());
			} else {
				setPaginaRequisitada(dataScrollerEvent.getPage());
				getControleConsultaOtimizado().setPaginaAtual(getPaginaAtual());
				getControleConsultaOtimizado().setPage(getPaginaAtual());
				setConsultaDataScroller(true);
			}
			
		} else {
			if (!getPossuiHistoricoAlteradoLista()) {
				getControleConsultaOtimizado().setPaginaAtual(dataScrollerEvent.getPage());
				getControleConsultaOtimizado().setPage(dataScrollerEvent.getPage());
				setConsultaDataScroller(true);
				if(getControleConsultaOtimizado().getLimitePorPagina() == 0) {
					getControleConsultaOtimizado().setLimitePorPagina(10);
				}
				montarAlunosTurma(Boolean.TRUE, professorExclusivo);
				getFacadeFactory().getHistoricoFacade().incluirHistoricoNotaParcial(getAlunosTurma(), getConfiguracaoAcademicaVO(), getUsuarioLogado());
			} else {
				setPaginaRequisitada(dataScrollerEvent.getPage());
				getControleConsultaOtimizado().setPaginaAtual(getPaginaAtual());
				getControleConsultaOtimizado().setPage(getPaginaAtual());
				setConsultaDataScroller(true);
				
			}
		}
		realizarAtualizacaoDosHistoricosAlterados();
		//realizarAtualizacaoDosHistoricoNotaParcial();
		
	}
	
	public void realizarVerificacaoHistoricoAlteradoLista() {
		this.setAlunosTurma(this.getControleConsultaOtimizado().getListaConsulta());
		for (HistoricoVO historicoVO : this.getAlunosTurma()) {
			if (historicoVO.getHistoricoAlterado() && !historicoVO.getHistoricoSalvo()) {
				setPossuiHistoricoAlteradoLista(Boolean.TRUE);
				break;
			}
		}
	}
	
	public String getAbrirModalGravarDescartar() {
		if (getBloquearLancamentosNotasAulasFeriadosFinaisSemana()) {
			return "";
		}
		if (getPossuiHistoricoAlteradoLista()) {
			return "RichFaces.$('panelAvisoGravarOuDescartar').show()";
		}
		return "";
	}
	
	public void gravarRealizandoNavegacaoScrollerLista() {
		try {
			gravarVisaoProfessor();

			getControleConsultaOtimizado().setPaginaAtual(getPaginaRequisitada());
			getControleConsultaOtimizado().setPage(getPaginaRequisitada());
			setConsultaDataScroller(true);
			setPossuiHistoricoAlteradoLista(Boolean.FALSE);
			if (getConsultaIndividualPorAluno() || getConsultaPorInicialNome()) {
				consultarAlunoPorNome();
			} else {
				montarAlunosTurma(Boolean.TRUE, Boolean.FALSE);
			}
			realizarAtualizacaoDosHistoricosAlterados();
		} catch (Exception e) {
			getListaAnterior().clear();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void realizarNavegacaoScrollerLista() {
		try {
			inicializarDadosHistoricoAlterado();
			getControleConsultaOtimizado().setPaginaAtual(getPaginaRequisitada());
			getControleConsultaOtimizado().setPage(getPaginaRequisitada());
			setConsultaDataScroller(true);
			setPossuiHistoricoAlteradoLista(Boolean.FALSE);
			if (getConsultaIndividualPorAluno() || getConsultaPorInicialNome()) {
				consultarAlunoPorNome();
			} else {
				montarAlunosTurma(Boolean.TRUE, Boolean.FALSE);
			}
			realizarAtualizacaoDosHistoricosAlterados();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void inicializarDadosHistoricoAlterado() {
		for (HistoricoVO HistoricoVO : getAlunosTurma()) {
			if (historicoVO.getHistoricoAlterado() && !getMapHistoricoAlteradoVOs().containsKey(historicoVO.getMatricula().getMatricula())) {
				getMapHistoricoAlteradoVOs().put(historicoVO.getMatricula().getMatricula(), historicoVO);
			}
		}
	}
	    
	public void consultarAlunoPorNome() throws Exception {
		try {
			List lista = null;
			if(getControleConsultaOtimizado().getLimitePorPagina() == 0) {
				getControleConsultaOtimizado().setLimitePorPagina(10);
			}
			boolean trazerAlunoPendenteFinanceiramente = getFacadeFactory().getConfiguracaoGeralSistemaFacade().executarVerificacaoApresentarAlunoPendenteFinanceiramente(getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado());
			boolean permitiVisualizarAlunoTR_CA = verificarPermissaoVisualizarAlunoTR_CA();
			boolean permitirRealizarLancamentoAlunosPreMatriculados = getConfiguracaoGeralPadraoSistema().getPermitirProfessorRealizarLancamentoAlunosPreMatriculados();
			this.setAlunosTurma(null);
			getControleConsultaOtimizado().setListaConsulta(new ArrayList(0));
			if (getConsultaIndividualPorAluno()) {

				if (getPossuiDiversidadeConfiguracaoAcademico()) {
					if (!this.getConfiguracaoAcademicoVO().getCodigo().equals(0)) {
						lista = getFacadeFactory().getHistoricoFacade().consultaRapidaPorUnidadeEnsinoCursoDisciplinaTurmaAnoSemestreSituacaoMatSituacaoHistFiltroVisaoProfessorRegistroNotaNomeAluno(getValorConsultaAluno(), 0, 0, getHistoricoVO().getDisciplina().getCodigo(), getTurmaVO(), getAno(), getSemestre(), trazerAlunoPendenteFinanceiramente, "'AA', 'CC', 'CH', 'IS'", false, true, false, this.getConfiguracaoAcademicoVO(), permitiVisualizarAlunoTR_CA, getPermiteLancarNotaDisciplinaComposta(), Uteis.NIVELMONTARDADOS_TODOS, getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), getUsuarioLogado(), permitirRealizarLancamentoAlunosPreMatriculados, getControleConsultaOtimizado());
						montarConfiguracaoAcademicoPorTurma();
						this.setAlunosTurma(lista);
					}
				} else {
					lista = getFacadeFactory().getHistoricoFacade().consultaRapidaPorUnidadeEnsinoCursoDisciplinaTurmaAnoSemestreSituacaoMatSituacaoHistFiltroVisaoProfessorRegistroNotaNomeAluno(getValorConsultaAluno(), 0, 0, getHistoricoVO().getDisciplina().getCodigo(), getTurmaVO(), getAno(), getSemestre(), trazerAlunoPendenteFinanceiramente, "'AA', 'CC', 'CH', 'IS'", false, true, false, null, permitiVisualizarAlunoTR_CA, getPermiteLancarNotaDisciplinaComposta(), Uteis.NIVELMONTARDADOS_TODOS, getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), getUsuarioLogado(), permitirRealizarLancamentoAlunosPreMatriculados, getControleConsultaOtimizado());
					this.setAlunosTurma(lista);
					if (!lista.isEmpty()) {
						realizarVerificacaoDiversidadeConfiguracaoAcademico();
					}
				}
				
			} else {
				
				if (getPossuiDiversidadeConfiguracaoAcademico()) {
					if (!this.getConfiguracaoAcademicoVO().getCodigo().equals(0)) {
						lista = getFacadeFactory().getHistoricoFacade().consultaRapidaPorUnidadeEnsinoCursoDisciplinaTurmaAnoSemestreSituacaoMatSituacaoHistFiltroVisaoProfessorRegistroNotaNomeAluno(getLetra(), 0, 0, getHistoricoVO().getDisciplina().getCodigo(), getTurmaVO(), getAno(), getSemestre(), trazerAlunoPendenteFinanceiramente, "'AA', 'CC', 'CH', 'IS'", false, true, false, this.getConfiguracaoAcademicoVO(), permitiVisualizarAlunoTR_CA, getPermiteLancarNotaDisciplinaComposta(),  Uteis.NIVELMONTARDADOS_TODOS, getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), getUsuarioLogado(), permitirRealizarLancamentoAlunosPreMatriculados, getControleConsultaOtimizado());
						montarConfiguracaoAcademicoPorTurma();
						this.setAlunosTurma(lista);
					}
				} else {
					lista = getFacadeFactory().getHistoricoFacade().consultaRapidaPorUnidadeEnsinoCursoDisciplinaTurmaAnoSemestreSituacaoMatSituacaoHistFiltroVisaoProfessorRegistroNotaNomeAluno(getLetra(), 0, 0, getHistoricoVO().getDisciplina().getCodigo(), getTurmaVO(), getAno(), getSemestre(), trazerAlunoPendenteFinanceiramente, "'AA', 'CC', 'CH', 'IS'", false, true, false, null, permitiVisualizarAlunoTR_CA, getPermiteLancarNotaDisciplinaComposta(),  Uteis.NIVELMONTARDADOS_TODOS, getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), getUsuarioLogado(), permitirRealizarLancamentoAlunosPreMatriculados, getControleConsultaOtimizado());
					this.setAlunosTurma(lista);
					if (!lista.isEmpty()) {
						realizarVerificacaoDiversidadeConfiguracaoAcademico();
					}
				}
			}
			if (lista != null && !lista.isEmpty()) {
				getControleConsultaOtimizado().setListaConsulta(lista);
				setPaginaAtual(getControleConsultaOtimizado().getPaginaAtual());
				setExisteRegistroAula(verificarExistenciaRegistroAula(null, getTurmaVO().getCodigo(), getHistoricoVO().getDisciplina().getCodigo(), null, semestre, ano));
				if (getExisteRegistroAula()) {
					preencherFrequenciaAlunos(getAlunosTurma());
				}
			}
			setPaginaAtual(getControleConsultaOtimizado().getPaginaAtual());
			getFacadeFactory().getHistoricoFacade().realizarVerificacaoBloqueiNotaDisciplinaComposta(getAlunosTurma(), getConfiguracaoAcademicoVO(), getUsuarioLogado());
			getFacadeFactory().getTurmaDisciplinaNotaTituloFacade().realizarCriacaoTurmaDisciplinaNotaTituloComBaseTipoTurma(getTurmaVO(), getHistoricoVO().getDisciplina(), getAno(), getSemestre(), this.getConfiguracaoAcademicoVO(), getUsuarioLogado());
			montarListaOpcoesNotas(getConfiguracaoAcademicoVO());
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			throw e;
		}
	}
	
	public void consultarAluno() {
		try {
			if (getValorConsultaAluno().equals("")) {
				throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
			}
			if (getHistoricoVO().getDisciplina().getCodigo().intValue() == 0) {
				throw new Exception(getMensagemInternalizacao("msg_LancamentoNota_disciplina"));
			}
			if ((getTurmaVO().getAnual() || getTurmaVO().getSemestral()) && !Uteis.isAtributoPreenchido(getAno()) && !getTurmaVO().getCurso().getLiberarRegistroAulaEntrePeriodo()) {
				throw new Exception(getMensagemInternalizacao("msg_AlunosMatriculadosGeralRel_ano"));
			} else if (getTurmaVO().getSemestral() && !Uteis.isAtributoPreenchido(getSemestre()) && !getTurmaVO().getCurso().getLiberarRegistroAulaEntrePeriodo()) {
				throw new Exception(getMensagemInternalizacao("msg_AlunosMatriculadosGeralRel_semestre"));
			}
//			if (getTurmaVO().getCurso().getLiberarRegistroAulaEntrePeriodo()) {
//				setAno("");
//				setSemestre("");
//			}
			this.setLetra("");
			getMapHistoricoAlteradoVOs().clear();
			setCalcularMediaApenasParaDadosAlterados(Boolean.TRUE);
			setCalcularMediaParaTodosAlunos(Boolean.FALSE);
			consultarAlunoPorNome();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}

	}
	
	public void consultarPorLetraListener(ActionEvent evt) {
		try {
			setLetra((String) evt.getComponent().getId());
	        getControleConsultaOtimizado().setPaginaAtual(1);
	        getControleConsultaOtimizado().setPage(1);
	        if (getLetra().length() > 1) {
	            letra = "A";
	        }
	        this.setConsultaPorInicialNome(Boolean.TRUE);
	        this.setConsultaIndividualPorAluno(Boolean.FALSE);
	        setCalcularMediaApenasParaDadosAlterados(Boolean.TRUE);
			setCalcularMediaParaTodosAlunos(Boolean.FALSE);
	        getMapHistoricoAlteradoVOs().clear();
	        consultarAlunoPorNome();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
        
    }
	
	
	public void realizarVerificacaoDiversidadeConfiguracaoAcademico() throws Exception {
		getMapConfiguracaoAcademicoVOs().clear();
		for (HistoricoVO historicoVO : getAlunosTurma()) {
			if (historicoVO != null && !historicoVO.getConfiguracaoAcademico().getCodigo().equals(0)) {
				if (!getMapConfiguracaoAcademicoVOs().containsKey(historicoVO.getConfiguracaoAcademico().getCodigo())) {
					getMapConfiguracaoAcademicoVOs().put(historicoVO.getConfiguracaoAcademico().getCodigo(), historicoVO.getConfiguracaoAcademico());
				}
			}
		}
		if (getMapConfiguracaoAcademicoVOs().size() > 1) {
			setPossuiDiversidadeConfiguracaoAcademico(Boolean.TRUE);
			this.getConfiguracaoAcademicoVO().setCodigo(0);
			montarComboboxConfiguracaoAcademico();
			getAlunosTurma().clear();
			getControleConsultaOtimizado().getListaConsulta().clear();
		} else {
			setPossuiDiversidadeConfiguracaoAcademico(Boolean.FALSE);
			if (!getAlunosTurma().isEmpty()) {
				this.setConfiguracaoAcademicoVO(getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorChavePrimaria(getAlunosTurma().get(0).getConfiguracaoAcademico().getCodigo(), getUsuarioLogado()));
				inicializarDadosDependentesConfiguracaoAcademico();
			}
		}
	}

	public void montarConfiguracaoAcademicoPorTurma() throws Exception {
		this.setConfiguracaoAcademicoVO(getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorChavePrimaria(this.getConfiguracaoAcademicoVO().getCodigo(), getUsuarioLogado()));
		inicializarDadosDependentesConfiguracaoAcademico();
	}

	public void montarComboboxConfiguracaoAcademico() {
		List objs = new ArrayList(0);
		objs.add(new SelectItem("", ""));
		for (ConfiguracaoAcademicoVO configuracao : getMapConfiguracaoAcademicoVOs().values()) {
			objs.add(new SelectItem(configuracao.getCodigo(), configuracao.getNome()));
		}
		setListaSelectItemConfiguracaoAcademico(objs);
	}

	public Boolean verificarExistenciaRegistroAula(String matricula, Integer turma, Integer disciplina, Integer professor, String semestre, String ano) throws Exception {
		try {
			return getFacadeFactory().getRegistroAulaFacade().consultarExistenciaRegistroAula(matricula, turma, disciplina, professor, semestre, ano);
		} catch (Exception e) {
			throw e;
		}
	}

	public void preencherFrequenciaAlunos(List<HistoricoVO> listaHistoricos) {
		for (HistoricoVO obj : listaHistoricos) {
			montarFreguenciaAluno(obj, this.getConfiguracaoAcademicoVO());
		}
	}

	public void montarAlunosTurma2() throws Exception {

		Integer disciplina = getHistoricoVO().getDisciplina().getCodigo();
		// <BY THYAGO - FILTRO DE ALUNOS PENDENTES FINANCEIRAMENTE>
		List lista = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarPorCodigoTurmaDisciplinaSemestreAno(turmaVO, disciplina, ano, semestre, true, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		Iterator i = lista.iterator();
		setAlunosTurma(new ArrayList(0));
		while (i.hasNext()) {
			MatriculaPeriodoTurmaDisciplinaVO obj = (MatriculaPeriodoTurmaDisciplinaVO) i.next();
			MatriculaPeriodoVO matricula = getFacadeFactory().getMatriculaPeriodoFacade().consultarPorChavePrimaria(obj.getMatriculaPeriodo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			MatriculaVO mat = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(matricula.getMatricula(), 0, NivelMontarDados.BASICO, getUsuarioLogado());
			if (!mat.getSituacao().equalsIgnoreCase("AT")) {
				continue;
			}
			HistoricoVO histVO = getFacadeFactory().getHistoricoFacade().consultarPorMatricula_matriculaPeriodo_Disciplina(matricula.getMatricula(), obj.getMatriculaPeriodo(), disciplina, false, Uteis.NIVELMONTARDADOS_TODOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			if (histVO == null) {
				histVO = new HistoricoVO();
				histVO.setDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(historicoVO.getDisciplina().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
				histVO.setMatricula(getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(matricula.getMatricula(), 0, NivelMontarDados.BASICO, getUsuarioLogado()));
				histVO.setMatriculaPeriodo(matricula);
				histVO.setMatriculaPeriodoTurmaDisciplina(obj);
				histVO.setDataRegistro(getHistoricoVO().getDataRegistro());
				histVO.setSituacao("");

				// histVO.setCargaHoraria(histVO.getDisciplina().getCargaHoraria().doubleValue());
			}
			if (matricula.getSituacaoMatriculaPeriodo().equals("FI")) {
				histVO.setEditavel(Boolean.TRUE);
			}
			histVO.getResponsavel().setCodigo(getUsuarioLogado().getCodigo());
			montarFreguenciaAluno(histVO, this.getConfiguracaoAcademicoVO());
			this.getAlunosTurma().add(histVO);
		}
		Ordenacao.ordenarLista(getAlunosTurma(), "ordenacao");

	}

	public void atualizarSituacao() {
		if (!getHistoricoVO().getTipoHistorico().equalsIgnoreCase("NO")) {
			getHistoricoVO().setSituacao("AP");
		} else {
			getHistoricoVO().setSituacao("");
		}
	}

	public void montarFreguenciaAluno(HistoricoVO obj, ConfiguracaoAcademicoVO configuracaoAcademicoVO) {
		try {
            getFacadeFactory().getHistoricoFacade().executarGeracaoFaltaPrimeiroSegundoTerceiroQuartoBimestreTotalFaltaFrequenciaHistorico(obj, configuracaoAcademicoVO, getUsuarioLogado());
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
		}
	}

	public void verificaAlunoReprovadoFalta(HistoricoVO obj) {
		try {
			getFacadeFactory().getHistoricoFacade().verificaAlunoReprovadoFalta(obj, this.getConfiguracaoAcademicoVO(), getUsuarioLogado());
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
		}
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	List tipoConsultaCombo;

	public List getTipoConsultaCombo() {
		if (tipoConsultaCombo == null) {
			tipoConsultaCombo = new ArrayList(0);
			tipoConsultaCombo.add(new SelectItem("turma", "Turma"));
		}
		return tipoConsultaCombo;
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes
	 * de uma consulta.
	 */
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setPaginaAtualDeTodas("0/0");
		setListaConsulta(new ArrayList(0));
		definirVisibilidadeLinksNavegacao(0, 0);
		setMensagemID("msg_entre_prmconsulta");
		return "consultar";
	}

	public void inicializarListasSelectItemTodosComboBox() {
		montarListaSelectPeriodosLetivos();
		montarListaSelectItemDisciplinas();
		montarListaOpcoesNotas(null);
	}

	private Date dataBase = new Date();

	public Date getDataBase() {
		if (dataBase == null) {
			dataBase = new Date();
		}
		return dataBase;
	}

	public Boolean getNotaAptoApresentar(Integer nrNota) {
		return (Boolean) UtilReflexao.invocarMetodoGet(this, "nota"+nrNota+"AptoApresentar");
		
	}
	public Boolean getNota1AptoApresentar() {
		if (dataBase == null) {
			dataBase = Uteis.getDataJDBC(new Date());
		}
		if (getConfiguracaoAcademicoVO().getNota1TipoLancamento()  && getConfiguracaoAcademicoVO().getApresentarNota1() && getTipoNota1() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota1() == null || getCalendarioLancamentoNotaVO().getDataInicioNota1().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota1().before(getDataBase());

		}
		return getConfiguracaoAcademicoVO().getUtilizarNota1() && getConfiguracaoAcademicoVO().getApresentarNota1() && getTipoNota1();
	}

	public Boolean getNota2AptoApresentar() {
		if (getConfiguracaoAcademicoVO().getNota2TipoLancamento() && getConfiguracaoAcademicoVO().getApresentarNota2() && getTipoNota2() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota2() == null || getCalendarioLancamentoNotaVO().getDataInicioNota2().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota2().before(getDataBase());
		}
		return getConfiguracaoAcademicoVO().getUtilizarNota2() && getConfiguracaoAcademicoVO().getApresentarNota2() && getTipoNota2();
	}

	public Boolean getNota3AptoApresentar() {
		if (getConfiguracaoAcademicoVO().getNota3TipoLancamento() && getConfiguracaoAcademicoVO().getApresentarNota3() && getTipoNota3() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota3() == null || getCalendarioLancamentoNotaVO().getDataInicioNota3().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota3().before(getDataBase());
		}
		return getConfiguracaoAcademicoVO().getUtilizarNota3() && getConfiguracaoAcademicoVO().getApresentarNota3() && getTipoNota3();
	}

	public Boolean getNota4AptoApresentar() {
		if (getConfiguracaoAcademicoVO().getNota4TipoLancamento() && getConfiguracaoAcademicoVO().getApresentarNota4() && getTipoNota4() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota4() == null || getCalendarioLancamentoNotaVO().getDataInicioNota4().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota4().before(getDataBase());
		}
		return getConfiguracaoAcademicoVO().getUtilizarNota4() && getConfiguracaoAcademicoVO().getApresentarNota4() && getTipoNota4();
	}

	public Boolean getNota5AptoApresentar() {
		if (getConfiguracaoAcademicoVO().getNota5TipoLancamento() && getConfiguracaoAcademicoVO().getApresentarNota5() && getTipoNota5() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota5() == null || getCalendarioLancamentoNotaVO().getDataInicioNota5().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota5().before(getDataBase());
		}
		return getConfiguracaoAcademicoVO().getUtilizarNota5() && getConfiguracaoAcademicoVO().getApresentarNota5() && getTipoNota5();
	}

	public Boolean getNota6AptoApresentar() {
		if (getConfiguracaoAcademicoVO().getNota6TipoLancamento() && getConfiguracaoAcademicoVO().getApresentarNota6() && getTipoNota6() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota6() == null || getCalendarioLancamentoNotaVO().getDataInicioNota6().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota6().before(getDataBase());
		}
		return getConfiguracaoAcademicoVO().getUtilizarNota6() && getConfiguracaoAcademicoVO().getApresentarNota6() && getTipoNota6();
	}

	public Boolean getNota7AptoApresentar() {
		if (getConfiguracaoAcademicoVO().getNota7TipoLancamento() && getConfiguracaoAcademicoVO().getApresentarNota7() && getTipoNota7() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota7() == null || getCalendarioLancamentoNotaVO().getDataInicioNota7().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota7().before(getDataBase());
		}
		return getConfiguracaoAcademicoVO().getUtilizarNota7() && getConfiguracaoAcademicoVO().getApresentarNota7() && getTipoNota7();
	}

	public Boolean getNota8AptoApresentar() {
		if (getConfiguracaoAcademicoVO().getNota8TipoLancamento() && getConfiguracaoAcademicoVO().getApresentarNota8() && getTipoNota8() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota8() == null || getCalendarioLancamentoNotaVO().getDataInicioNota8().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota8().before(getDataBase());
		}
		return getConfiguracaoAcademicoVO().getUtilizarNota8() && getConfiguracaoAcademicoVO().getApresentarNota8() && getTipoNota8();
	}

	public Boolean getNota9AptoApresentar() {
		if (getConfiguracaoAcademicoVO().getNota9TipoLancamento() && getConfiguracaoAcademicoVO().getApresentarNota9() && getTipoNota9() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota9() == null || getCalendarioLancamentoNotaVO().getDataInicioNota9().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota9().before(getDataBase());
		}
		return getConfiguracaoAcademicoVO().getUtilizarNota9() && getConfiguracaoAcademicoVO().getApresentarNota9() && getTipoNota9();
	}

	public Boolean getNota10AptoApresentar() {
		if (getConfiguracaoAcademicoVO().getNota10TipoLancamento() && getConfiguracaoAcademicoVO().getApresentarNota10() && getTipoNota10() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota10() == null || getCalendarioLancamentoNotaVO().getDataInicioNota10().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota10().before(getDataBase());
		}
		return getConfiguracaoAcademicoVO().getUtilizarNota10() && getConfiguracaoAcademicoVO().getApresentarNota10() && getTipoNota10();
	}

	public Boolean getNota11AptoApresentar() {
		if (getConfiguracaoAcademicoVO().getNota11TipoLancamento() && getConfiguracaoAcademicoVO().getApresentarNota11() && getTipoNota11() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota11() == null || getCalendarioLancamentoNotaVO().getDataInicioNota11().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota11().before(getDataBase());
		}
		return getConfiguracaoAcademicoVO().getUtilizarNota11() && getConfiguracaoAcademicoVO().getApresentarNota11() && getTipoNota11();
	}

	public Boolean getNota12AptoApresentar() {
		if (getConfiguracaoAcademicoVO().getNota12TipoLancamento() && getConfiguracaoAcademicoVO().getApresentarNota12() && getTipoNota12() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota12() == null || getCalendarioLancamentoNotaVO().getDataInicioNota12().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota12().before(getDataBase());
		}
		return getConfiguracaoAcademicoVO().getUtilizarNota12() && getConfiguracaoAcademicoVO().getApresentarNota12() && getTipoNota12();
	}

	public Boolean getNota13AptoApresentar() {
		if (getConfiguracaoAcademicoVO().getNota13TipoLancamento() && getConfiguracaoAcademicoVO().getApresentarNota13() && getTipoNota13() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota13() == null || getCalendarioLancamentoNotaVO().getDataInicioNota13().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota13().before(getDataBase());
		}
		return getConfiguracaoAcademicoVO().getUtilizarNota13() && getConfiguracaoAcademicoVO().getApresentarNota13() && getTipoNota13();
	}

	public Boolean getNota14AptoApresentar() {
		if (getConfiguracaoAcademicoVO().getNota14TipoLancamento() && getConfiguracaoAcademicoVO().getApresentarNota14() && getTipoNota14() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota14() == null || getCalendarioLancamentoNotaVO().getDataInicioNota14().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota14().before(getDataBase());
		}
		return getConfiguracaoAcademicoVO().getUtilizarNota14() && getConfiguracaoAcademicoVO().getApresentarNota14() && getTipoNota14();
	}

	public Boolean getNota15AptoApresentar() {
		if (getConfiguracaoAcademicoVO().getNota15TipoLancamento() && getConfiguracaoAcademicoVO().getApresentarNota15() && getTipoNota15() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota15() == null || getCalendarioLancamentoNotaVO().getDataInicioNota15().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota15().before(getDataBase());
		}
		return getConfiguracaoAcademicoVO().getUtilizarNota15() && getConfiguracaoAcademicoVO().getApresentarNota15() && getTipoNota15();
	}

	public Boolean getNota16AptoApresentar() {
		if (getConfiguracaoAcademicoVO().getNota16TipoLancamento() && getConfiguracaoAcademicoVO().getApresentarNota16() && getTipoNota16() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota16() == null || getCalendarioLancamentoNotaVO().getDataInicioNota16().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota16().before(getDataBase());
		}
		return getConfiguracaoAcademicoVO().getUtilizarNota16() && getConfiguracaoAcademicoVO().getApresentarNota16() && getTipoNota16();
	}

	public Boolean getNota17AptoApresentar() {
		if (getConfiguracaoAcademicoVO().getNota17TipoLancamento() && getConfiguracaoAcademicoVO().getApresentarNota17() && getTipoNota17() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota17() == null || getCalendarioLancamentoNotaVO().getDataInicioNota17().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota17().before(getDataBase());
		}
		return getConfiguracaoAcademicoVO().getUtilizarNota17() && getConfiguracaoAcademicoVO().getApresentarNota17() && getTipoNota17();
	}

	public Boolean getNota18AptoApresentar() {
		if (getConfiguracaoAcademicoVO().getNota18TipoLancamento() && getConfiguracaoAcademicoVO().getApresentarNota18() && getTipoNota18() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota18() == null || getCalendarioLancamentoNotaVO().getDataInicioNota18().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota18().before(getDataBase());
		}
		return getConfiguracaoAcademicoVO().getUtilizarNota18() && getConfiguracaoAcademicoVO().getApresentarNota18() && getTipoNota18();
	}

	public Boolean getNota19AptoApresentar() {
		if (getConfiguracaoAcademicoVO().getNota19TipoLancamento() && getConfiguracaoAcademicoVO().getApresentarNota19() && getTipoNota19() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota19() == null || getCalendarioLancamentoNotaVO().getDataInicioNota19().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota19().before(getDataBase());
		}
		return getConfiguracaoAcademicoVO().getUtilizarNota19() && getConfiguracaoAcademicoVO().getApresentarNota19() && getTipoNota19();
	}

	public Boolean getNota20AptoApresentar() {
		if (getConfiguracaoAcademicoVO().getNota20TipoLancamento() && getConfiguracaoAcademicoVO().getApresentarNota20() && getTipoNota20() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota20() == null || getCalendarioLancamentoNotaVO().getDataInicioNota20().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota20().before(getDataBase());
		}
		return getConfiguracaoAcademicoVO().getUtilizarNota20() && getConfiguracaoAcademicoVO().getApresentarNota20() && getTipoNota20();
	}

	public Boolean getNota21AptoApresentar() {
		if (getConfiguracaoAcademicoVO().getNota21TipoLancamento() && getConfiguracaoAcademicoVO().getApresentarNota21() && getTipoNota21() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota21() == null || getCalendarioLancamentoNotaVO().getDataInicioNota21().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota21().before(getDataBase());
		}
		return getConfiguracaoAcademicoVO().getUtilizarNota21() && getConfiguracaoAcademicoVO().getApresentarNota21() && getTipoNota21();
	}

	public Boolean getNota22AptoApresentar() {
		if (getConfiguracaoAcademicoVO().getNota22TipoLancamento() && getConfiguracaoAcademicoVO().getApresentarNota22() && getTipoNota22() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota22() == null || getCalendarioLancamentoNotaVO().getDataInicioNota22().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota22().before(getDataBase());
		}
		return getConfiguracaoAcademicoVO().getUtilizarNota22() && getConfiguracaoAcademicoVO().getApresentarNota22() && getTipoNota22();
	}

	public Boolean getNota23AptoApresentar() {
		if (getConfiguracaoAcademicoVO().getNota23TipoLancamento()  && getConfiguracaoAcademicoVO().getApresentarNota23() && getTipoNota23() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota23() == null || getCalendarioLancamentoNotaVO().getDataInicioNota23().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota23().before(getDataBase());
		}
		return getConfiguracaoAcademicoVO().getUtilizarNota23() && getConfiguracaoAcademicoVO().getApresentarNota23() && getTipoNota23();
	}

	public Boolean getNota24AptoApresentar() {
		if (getConfiguracaoAcademicoVO().getNota24TipoLancamento() && getConfiguracaoAcademicoVO().getApresentarNota24() && getTipoNota24() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota24() == null || getCalendarioLancamentoNotaVO().getDataInicioNota24().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota24().before(getDataBase());
		}
		return getConfiguracaoAcademicoVO().getUtilizarNota24() && getConfiguracaoAcademicoVO().getApresentarNota24() && getTipoNota24();
	}

	public Boolean getNota25AptoApresentar() {
		if (getConfiguracaoAcademicoVO().getNota25TipoLancamento() && getConfiguracaoAcademicoVO().getApresentarNota25() && getTipoNota25() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota25() == null || getCalendarioLancamentoNotaVO().getDataInicioNota25().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota25().before(getDataBase());
		}
		return getConfiguracaoAcademicoVO().getUtilizarNota25() && getConfiguracaoAcademicoVO().getApresentarNota25() && getTipoNota25();
	}

	public Boolean getNota26AptoApresentar() {
		if (getConfiguracaoAcademicoVO().getNota26TipoLancamento() && getConfiguracaoAcademicoVO().getApresentarNota26() && getTipoNota26() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota26() == null || getCalendarioLancamentoNotaVO().getDataInicioNota26().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota26().before(getDataBase());
		}
		return getConfiguracaoAcademicoVO().getUtilizarNota26() && getConfiguracaoAcademicoVO().getApresentarNota26() && getTipoNota26();
	}

	public Boolean getNota27AptoApresentar() {
		if (getConfiguracaoAcademicoVO().getNota27TipoLancamento() && getConfiguracaoAcademicoVO().getApresentarNota27() && getTipoNota27() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota27() == null || getCalendarioLancamentoNotaVO().getDataInicioNota27().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota27().before(getDataBase());
		}
		return getConfiguracaoAcademicoVO().getUtilizarNota27() && getConfiguracaoAcademicoVO().getApresentarNota27() && getTipoNota27();
	}
	
	public Boolean getNota28AptoApresentar() {
		if (getConfiguracaoAcademicoVO().getNota28TipoLancamento() && getConfiguracaoAcademicoVO().getApresentarNota28() && getTipoNota28() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota28() == null || getCalendarioLancamentoNotaVO().getDataInicioNota28().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota28().before(getDataBase());
		}
		return getConfiguracaoAcademicoVO().getUtilizarNota28() && getConfiguracaoAcademicoVO().getApresentarNota28() && getTipoNota28();
	}
	
	public Boolean getNota29AptoApresentar() {
		if (getConfiguracaoAcademicoVO().getNota29TipoLancamento() && getConfiguracaoAcademicoVO().getApresentarNota29() && getTipoNota29() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota29() == null || getCalendarioLancamentoNotaVO().getDataInicioNota29().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota29().before(getDataBase());
		}
		return getConfiguracaoAcademicoVO().getUtilizarNota29() && getConfiguracaoAcademicoVO().getApresentarNota29() && getTipoNota29();
	}
	
	public Boolean getNota30AptoApresentar() {
		if (getConfiguracaoAcademicoVO().getNota30TipoLancamento() && getConfiguracaoAcademicoVO().getApresentarNota30() && getTipoNota30() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota30() == null || getCalendarioLancamentoNotaVO().getDataInicioNota30().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota30().before(getDataBase());
		}
		return getConfiguracaoAcademicoVO().getUtilizarNota30() && getConfiguracaoAcademicoVO().getApresentarNota30() && getTipoNota30();
	}

	public Boolean getNota31AptoApresentar() {
		if (getConfiguracaoAcademicoVO().getNota31TipoLancamento() && getConfiguracaoAcademicoVO().getApresentarNota31() && getTipoNota31() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota31() == null || getCalendarioLancamentoNotaVO().getDataInicioNota31().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota31().before(getDataBase());
		}
		return getConfiguracaoAcademicoVO().getUtilizarNota31() && getConfiguracaoAcademicoVO().getApresentarNota31() && getTipoNota31();
	}

	public Boolean getNota32AptoApresentar() {
		if (getConfiguracaoAcademicoVO().getNota32TipoLancamento() && getConfiguracaoAcademicoVO().getApresentarNota32() && getTipoNota32() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota32() == null || getCalendarioLancamentoNotaVO().getDataInicioNota32().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota32().before(getDataBase());
		}
		return getConfiguracaoAcademicoVO().getUtilizarNota32() && getConfiguracaoAcademicoVO().getApresentarNota32() && getTipoNota32();
	}

	public Boolean getNota33AptoApresentar() {
		if (getConfiguracaoAcademicoVO().getNota33TipoLancamento() && getConfiguracaoAcademicoVO().getApresentarNota33() && getTipoNota33() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota33() == null || getCalendarioLancamentoNotaVO().getDataInicioNota33().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota33().before(getDataBase());
		}
		return getConfiguracaoAcademicoVO().getUtilizarNota33() && getConfiguracaoAcademicoVO().getApresentarNota33() && getTipoNota33();
	}

	public Boolean getNota34AptoApresentar() {
		if (getConfiguracaoAcademicoVO().getNota34TipoLancamento() && getConfiguracaoAcademicoVO().getApresentarNota34() && getTipoNota34() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota34() == null || getCalendarioLancamentoNotaVO().getDataInicioNota34().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota34().before(getDataBase());
		}
		return getConfiguracaoAcademicoVO().getUtilizarNota34() && getConfiguracaoAcademicoVO().getApresentarNota34() && getTipoNota34();
	}

	public Boolean getNota35AptoApresentar() {
		if (getConfiguracaoAcademicoVO().getNota35TipoLancamento() && getConfiguracaoAcademicoVO().getApresentarNota35() && getTipoNota35() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota35() == null || getCalendarioLancamentoNotaVO().getDataInicioNota35().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota35().before(getDataBase());
		}
		return getConfiguracaoAcademicoVO().getUtilizarNota35() && getConfiguracaoAcademicoVO().getApresentarNota35() && getTipoNota35();
	}

	public Boolean getNota36AptoApresentar() {
		if (getConfiguracaoAcademicoVO().getNota36TipoLancamento() && getConfiguracaoAcademicoVO().getApresentarNota36() && getTipoNota36() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota36() == null || getCalendarioLancamentoNotaVO().getDataInicioNota36().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota36().before(getDataBase());
		}
		return getConfiguracaoAcademicoVO().getUtilizarNota36() && getConfiguracaoAcademicoVO().getApresentarNota36() && getTipoNota36();
	}

	public Boolean getNota37AptoApresentar() {
		if (getConfiguracaoAcademicoVO().getNota37TipoLancamento() && getConfiguracaoAcademicoVO().getApresentarNota37() && getTipoNota37() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota37() == null || getCalendarioLancamentoNotaVO().getDataInicioNota37().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota37().before(getDataBase());
		}
		return getConfiguracaoAcademicoVO().getUtilizarNota37() && getConfiguracaoAcademicoVO().getApresentarNota37() && getTipoNota37();
	}

	public Boolean getNota38AptoApresentar() {
		if (getConfiguracaoAcademicoVO().getNota38TipoLancamento() && getConfiguracaoAcademicoVO().getApresentarNota38() && getTipoNota38() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota38() == null || getCalendarioLancamentoNotaVO().getDataInicioNota38().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota38().before(getDataBase());
		}
		return getConfiguracaoAcademicoVO().getUtilizarNota38() && getConfiguracaoAcademicoVO().getApresentarNota38() && getTipoNota38();
	}

	public Boolean getNota39AptoApresentar() {
		if (getConfiguracaoAcademicoVO().getNota39TipoLancamento() && getConfiguracaoAcademicoVO().getApresentarNota39() && getTipoNota39() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota39() == null || getCalendarioLancamentoNotaVO().getDataInicioNota39().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota39().before(getDataBase());
		}
		return getConfiguracaoAcademicoVO().getUtilizarNota39() && getConfiguracaoAcademicoVO().getApresentarNota39() && getTipoNota39();
	}

	public Boolean getNota40AptoApresentar() {
		if (getConfiguracaoAcademicoVO().getNota40TipoLancamento() && getConfiguracaoAcademicoVO().getApresentarNota40() && getTipoNota40() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota40() == null || getCalendarioLancamentoNotaVO().getDataInicioNota40().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota40().before(getDataBase());
		}
		return getConfiguracaoAcademicoVO().getUtilizarNota40() && getConfiguracaoAcademicoVO().getApresentarNota40() && getTipoNota40();
	}


	
	public boolean getTipoNota1() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota1())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota2() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota2())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota3() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota3())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota4() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota4())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota5() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota5())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota6() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota6())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota7() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota7())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota8() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota8())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota9() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota9())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota10() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota10())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota11() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota11())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota12() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota12())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota13() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota13())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota14() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota14())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota15() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota15())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota16() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota16())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota17() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota17())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota18() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota18())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota19() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota19())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota20() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota20())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota21() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota21())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota22() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota22())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota23() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota23())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota24() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota24())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota25() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota25())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota26() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota26())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota27() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota27())) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	public boolean getTipoNota28() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota28())) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	public boolean getTipoNota29() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota29())) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	public boolean getTipoNota30() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota30())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota31() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota31())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota32() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota32())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota33() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota33())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota34() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota34())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota35() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota35())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota36() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota36())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota37() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota37())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota38() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota38())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota39() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota39())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota40() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota40())) {
				return true;
			} else {
				return false;
			}
		}
	}


	public void verificarPermissaoOcultarSituacaoMatricula() {
		try {
			getUsuarioLogado().setUsuarioPerfilAcessoVOs(null);
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("LancamentoNota_OcultarSituacaoMatricula", getUsuarioLogado());
			setOcultarSituacaoMatricula(true);
		} catch (Exception e) {
			setOcultarSituacaoMatricula(false);
		}
	}
	
	public boolean verificarPermissaoVisualizarAlunoTR_CA() {
		try {
			getUsuarioLogado().setUsuarioPerfilAcessoVOs(null);
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("LancamentoNotaa_VisualizarMatriculaTR_CA", getUsuarioLogado());
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public void verificarPermissaoParaGravarLancamentoNotaVisaoCoordenador() {
		try {
			if (getUsuarioLogado().getVisaoLogar().equals("coordenador")) {
				ControleAcesso.incluir("LancamentoNota", getUsuarioLogado());
				setPermiteGravarVisaoCoordenador(true);
			}
		} catch (Exception e) {
			setPermiteGravarVisaoCoordenador(false);
		}
	}

	public String getAno() {
		if (ano == null) {
			if(getLoginControle().getPermissaoAcessoMenuVO().getPermitirLancarNotaRetroativo() && getUsuarioLogado().getIsApresentarVisaoProfessor()) {
				ano = getVisaoProfessorControle().getAno();
			}else if(getLoginControle().getPermissaoAcessoMenuVO().getPermitirLancarNotaRetroativo() && getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
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
		if (semestre == null) {
			if(getLoginControle().getPermissaoAcessoMenuVO().getPermitirLancarNotaRetroativo() && getUsuarioLogado().getIsApresentarVisaoProfessor()) {
				semestre = getVisaoProfessorControle().getSemestre();
			}else if(getLoginControle().getPermissaoAcessoMenuVO().getPermitirLancarNotaRetroativo() && getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
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

	public String getTipoNota() {
		if (tipoNota == null) {
			tipoNota = "";
		}
		return tipoNota;
	}

	public void setTipoNota(String tipoNota) {
		this.tipoNota = tipoNota;
	}

	public HistoricoVO getHistoricoVO() {
		if (historicoVO == null) {
			historicoVO = new HistoricoVO();
		}
		return historicoVO;
	}

	public void setHistoricoVO(HistoricoVO historicoVO) {
		this.historicoVO = historicoVO;
	}
	
	public List<HistoricoVO> getAlunosTurma() {
		if (alunosTurma == null) {
			alunosTurma = new ArrayList<HistoricoVO>();
		}
		return alunosTurma;
	}

	public void setAlunosTurma(List alunosTurma) {
		this.alunosTurma = alunosTurma;
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

	public HistoricoVO getHistoricoAprensentarVO() {
		if (historicoAprensentarVO == null) {
			historicoAprensentarVO = new HistoricoVO();
		}
		return historicoAprensentarVO;
	}

	public void setHistoricoAprensentarVO(HistoricoVO historicoAprensentarVO) {
		this.historicoAprensentarVO = historicoAprensentarVO;
	}

	public String getTipoInformarNota() {
		if (tipoInformarNota == null) {
			tipoInformarNota = "";
		}
		return tipoInformarNota;
	}

	public void setTipoInformarNota(String tipoInformarNota) {
		this.tipoInformarNota = tipoInformarNota;
	}

	public List<SelectItem> getListaSelectItemTipoInformarNota() {
		if (listaSelectItemTipoInformarNota == null) {
			listaSelectItemTipoInformarNota = new ArrayList<SelectItem>();
		}
		return listaSelectItemTipoInformarNota;
	}

	public void setListaSelectItemTipoInformarNota(List<SelectItem> listaSelectItemTipoInformarNota) {
		this.listaSelectItemTipoInformarNota = listaSelectItemTipoInformarNota;
	}

	public Boolean getNota28Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() &&
					((getCalendarioLancamentoNotaVO().getDataInicioNota28() == null ||
						getCalendarioLancamentoNotaVO().getDataInicioNota28().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataInicioNota28().before(getDataBase())) &&
					 (getCalendarioLancamentoNotaVO().getDataTerminoNota28() == null ||
					 	getCalendarioLancamentoNotaVO().getDataTerminoNota28().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataTerminoNota28().after(getDataBase()))))));
	}
	
	public Boolean getNota29Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() &&
					((getCalendarioLancamentoNotaVO().getDataInicioNota29() == null ||
						getCalendarioLancamentoNotaVO().getDataInicioNota29().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataInicioNota29().before(getDataBase())) &&
					 (getCalendarioLancamentoNotaVO().getDataTerminoNota29() == null ||
					 	getCalendarioLancamentoNotaVO().getDataTerminoNota29().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataTerminoNota29().after(getDataBase()))))));
	}
	
	public Boolean getNota30Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() &&
					((getCalendarioLancamentoNotaVO().getDataInicioNota30() == null ||
						getCalendarioLancamentoNotaVO().getDataInicioNota30().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataInicioNota30().before(getDataBase())) &&
					 (getCalendarioLancamentoNotaVO().getDataTerminoNota30() == null ||
					 	getCalendarioLancamentoNotaVO().getDataTerminoNota30().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataTerminoNota30().after(getDataBase()))))));
	}

	
	public Boolean getNota27Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() &&
					((getCalendarioLancamentoNotaVO().getDataInicioNota27() == null ||
						getCalendarioLancamentoNotaVO().getDataInicioNota27().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataInicioNota27().before(getDataBase())) &&
					 (getCalendarioLancamentoNotaVO().getDataTerminoNota27() == null ||
					 	getCalendarioLancamentoNotaVO().getDataTerminoNota27().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataTerminoNota27().after(getDataBase()))))));
	}

	

	public Boolean getNota26Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() &&
					((getCalendarioLancamentoNotaVO().getDataInicioNota26() == null ||
						getCalendarioLancamentoNotaVO().getDataInicioNota26().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataInicioNota26().before(getDataBase())) &&
					 (getCalendarioLancamentoNotaVO().getDataTerminoNota26() == null ||
					 	getCalendarioLancamentoNotaVO().getDataTerminoNota26().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataTerminoNota26().after(getDataBase()))))));
	}

	
	public Boolean getNota25Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() &&
					((getCalendarioLancamentoNotaVO().getDataInicioNota25() == null ||
						getCalendarioLancamentoNotaVO().getDataInicioNota25().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataInicioNota25().before(getDataBase())) &&
					 (getCalendarioLancamentoNotaVO().getDataTerminoNota25() == null ||
					 	getCalendarioLancamentoNotaVO().getDataTerminoNota25().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataTerminoNota25().after(getDataBase()))))));
	}

	

	public Boolean getNota24Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() &&
					((getCalendarioLancamentoNotaVO().getDataInicioNota24() == null ||
						getCalendarioLancamentoNotaVO().getDataInicioNota24().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataInicioNota24().before(getDataBase())) &&
					 (getCalendarioLancamentoNotaVO().getDataTerminoNota24() == null ||
					 	getCalendarioLancamentoNotaVO().getDataTerminoNota24().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataTerminoNota24().after(getDataBase()))))));
	}

	

	public Boolean getNota23Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() &&
					((getCalendarioLancamentoNotaVO().getDataInicioNota23() == null ||
						getCalendarioLancamentoNotaVO().getDataInicioNota23().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataInicioNota23().before(getDataBase())) &&
					 (getCalendarioLancamentoNotaVO().getDataTerminoNota23() == null ||
					 	getCalendarioLancamentoNotaVO().getDataTerminoNota23().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataTerminoNota23().after(getDataBase()))))));
	}

	
	public Boolean getNota22Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() &&
					((getCalendarioLancamentoNotaVO().getDataInicioNota22() == null ||
						getCalendarioLancamentoNotaVO().getDataInicioNota22().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataInicioNota22().before(getDataBase())) &&
					 (getCalendarioLancamentoNotaVO().getDataTerminoNota22() == null ||
					 	getCalendarioLancamentoNotaVO().getDataTerminoNota22().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataTerminoNota22().after(getDataBase()))))));
	}

	
	public Boolean getNota21Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() &&
					((getCalendarioLancamentoNotaVO().getDataInicioNota21() == null ||
						getCalendarioLancamentoNotaVO().getDataInicioNota21().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataInicioNota21().before(getDataBase())) &&
					 (getCalendarioLancamentoNotaVO().getDataTerminoNota21() == null ||
					 	getCalendarioLancamentoNotaVO().getDataTerminoNota21().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataTerminoNota21().after(getDataBase()))))));
	}

	
	public Boolean getNota20Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() &&
					((getCalendarioLancamentoNotaVO().getDataInicioNota20() == null ||
						getCalendarioLancamentoNotaVO().getDataInicioNota20().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataInicioNota20().before(getDataBase())) &&
					 (getCalendarioLancamentoNotaVO().getDataTerminoNota20() == null ||
					 	getCalendarioLancamentoNotaVO().getDataTerminoNota20().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataTerminoNota20().after(getDataBase()))))));
	}

	

	public Boolean getNota19Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() &&
					((getCalendarioLancamentoNotaVO().getDataInicioNota19() == null ||
						getCalendarioLancamentoNotaVO().getDataInicioNota19().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataInicioNota19().before(getDataBase())) &&
					 (getCalendarioLancamentoNotaVO().getDataTerminoNota19() == null ||
					 	getCalendarioLancamentoNotaVO().getDataTerminoNota19().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataTerminoNota19().after(getDataBase()))))));
	}

	

	public Boolean getNota18Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() &&
					((getCalendarioLancamentoNotaVO().getDataInicioNota18() == null ||
						getCalendarioLancamentoNotaVO().getDataInicioNota18().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataInicioNota18().before(getDataBase())) &&
					 (getCalendarioLancamentoNotaVO().getDataTerminoNota18() == null ||
					 	getCalendarioLancamentoNotaVO().getDataTerminoNota18().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataTerminoNota18().after(getDataBase()))))));
	}

	

	public Boolean getNota17Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() &&
					((getCalendarioLancamentoNotaVO().getDataInicioNota17() == null ||
						getCalendarioLancamentoNotaVO().getDataInicioNota17().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataInicioNota17().before(getDataBase())) &&
					 (getCalendarioLancamentoNotaVO().getDataTerminoNota17() == null ||
					 	getCalendarioLancamentoNotaVO().getDataTerminoNota17().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataTerminoNota17().after(getDataBase()))))));
	}

	

	public Boolean getNota16Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() &&
					((getCalendarioLancamentoNotaVO().getDataInicioNota16() == null ||
						getCalendarioLancamentoNotaVO().getDataInicioNota16().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataInicioNota16().before(getDataBase())) &&
					 (getCalendarioLancamentoNotaVO().getDataTerminoNota16() == null ||
					 	getCalendarioLancamentoNotaVO().getDataTerminoNota16().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataTerminoNota16().after(getDataBase()))))));
	}

	

	public Boolean getNota15Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() &&
					((getCalendarioLancamentoNotaVO().getDataInicioNota15() == null ||
						getCalendarioLancamentoNotaVO().getDataInicioNota15().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataInicioNota15().before(getDataBase())) &&
					 (getCalendarioLancamentoNotaVO().getDataTerminoNota15() == null ||
					 	getCalendarioLancamentoNotaVO().getDataTerminoNota15().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataTerminoNota15().after(getDataBase()))))));
	}

	

	public Boolean getNota14Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() &&
					((getCalendarioLancamentoNotaVO().getDataInicioNota14() == null ||
						getCalendarioLancamentoNotaVO().getDataInicioNota14().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataInicioNota14().before(getDataBase())) &&
					 (getCalendarioLancamentoNotaVO().getDataTerminoNota14() == null ||
					 	getCalendarioLancamentoNotaVO().getDataTerminoNota14().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataTerminoNota14().after(getDataBase()))))));
	}

	

	public Boolean getNota13Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() &&
					((getCalendarioLancamentoNotaVO().getDataInicioNota13() == null ||
						getCalendarioLancamentoNotaVO().getDataInicioNota13().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataInicioNota13().before(getDataBase())) &&
					 (getCalendarioLancamentoNotaVO().getDataTerminoNota13() == null ||
					 	getCalendarioLancamentoNotaVO().getDataTerminoNota13().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataTerminoNota13().after(getDataBase()))))));

	}

	

	public Boolean getNota12Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() &&
					((getCalendarioLancamentoNotaVO().getDataInicioNota12() == null ||
						getCalendarioLancamentoNotaVO().getDataInicioNota12().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataInicioNota12().before(getDataBase())) &&
					 (getCalendarioLancamentoNotaVO().getDataTerminoNota12() == null ||
					 	getCalendarioLancamentoNotaVO().getDataTerminoNota12().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataTerminoNota12().after(getDataBase()))))));
	}

	
	public Boolean getNota11Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() &&
					((getCalendarioLancamentoNotaVO().getDataInicioNota11() == null ||
						getCalendarioLancamentoNotaVO().getDataInicioNota11().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataInicioNota11().before(getDataBase())) &&
					 (getCalendarioLancamentoNotaVO().getDataTerminoNota11() == null ||
					 	getCalendarioLancamentoNotaVO().getDataTerminoNota11().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataTerminoNota11().after(getDataBase()))))));
	}

	

	public Boolean getNota10Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() &&
					((getCalendarioLancamentoNotaVO().getDataInicioNota10() == null ||
						getCalendarioLancamentoNotaVO().getDataInicioNota10().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataInicioNota10().before(getDataBase())) &&
					 (getCalendarioLancamentoNotaVO().getDataTerminoNota10() == null ||
					 	getCalendarioLancamentoNotaVO().getDataTerminoNota10().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataTerminoNota10().after(getDataBase()))))));
	}

	
	public Boolean getNota1Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() &&
					((getCalendarioLancamentoNotaVO().getDataInicioNota1() == null ||
						getCalendarioLancamentoNotaVO().getDataInicioNota1().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataInicioNota1().before(getDataBase())) &&
					 (getCalendarioLancamentoNotaVO().getDataTerminoNota1() == null ||
					 	getCalendarioLancamentoNotaVO().getDataTerminoNota1().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataTerminoNota1().after(getDataBase()))))));
	}

	

	public Boolean getNota2Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() &&
					((getCalendarioLancamentoNotaVO().getDataInicioNota2() == null ||
						getCalendarioLancamentoNotaVO().getDataInicioNota2().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataInicioNota2().before(getDataBase())) &&
					 (getCalendarioLancamentoNotaVO().getDataTerminoNota2() == null ||
					 	getCalendarioLancamentoNotaVO().getDataTerminoNota2().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataTerminoNota2().after(getDataBase()))))));
	}

	

	public Boolean getNota3Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() &&
					((getCalendarioLancamentoNotaVO().getDataInicioNota3() == null ||
						getCalendarioLancamentoNotaVO().getDataInicioNota3().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataInicioNota3().before(getDataBase())) &&
					 (getCalendarioLancamentoNotaVO().getDataTerminoNota3() == null ||
					 	getCalendarioLancamentoNotaVO().getDataTerminoNota3().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataTerminoNota3().after(getDataBase()))))));
	}

	

	public Boolean getNota4Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() &&
					((getCalendarioLancamentoNotaVO().getDataInicioNota4() == null ||
						getCalendarioLancamentoNotaVO().getDataInicioNota4().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataInicioNota4().before(getDataBase())) &&
					 (getCalendarioLancamentoNotaVO().getDataTerminoNota4() == null ||
					 	getCalendarioLancamentoNotaVO().getDataTerminoNota4().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataTerminoNota4().after(getDataBase()))))));
	}

	

	public Boolean getNota5Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() &&
					((getCalendarioLancamentoNotaVO().getDataInicioNota5() == null ||
						getCalendarioLancamentoNotaVO().getDataInicioNota5().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataInicioNota5().before(getDataBase())) &&
					 (getCalendarioLancamentoNotaVO().getDataTerminoNota5() == null ||
					 	getCalendarioLancamentoNotaVO().getDataTerminoNota5().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataTerminoNota5().after(getDataBase()))))));
	}

	

	public Boolean getNota6Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() &&
					((getCalendarioLancamentoNotaVO().getDataInicioNota6() == null ||
						getCalendarioLancamentoNotaVO().getDataInicioNota6().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataInicioNota6().before(getDataBase())) &&
					 (getCalendarioLancamentoNotaVO().getDataTerminoNota6() == null ||
					 	getCalendarioLancamentoNotaVO().getDataTerminoNota6().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataTerminoNota6().after(getDataBase()))))));
	}

	

	public Boolean getNota7Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() &&
					((getCalendarioLancamentoNotaVO().getDataInicioNota7() == null ||
						getCalendarioLancamentoNotaVO().getDataInicioNota7().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataInicioNota7().before(getDataBase())) &&
					 (getCalendarioLancamentoNotaVO().getDataTerminoNota7() == null ||
					 	getCalendarioLancamentoNotaVO().getDataTerminoNota7().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataTerminoNota7().after(getDataBase()))))));
	}

	

	public Boolean getNota8Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() &&
					((getCalendarioLancamentoNotaVO().getDataInicioNota8() == null ||
						getCalendarioLancamentoNotaVO().getDataInicioNota8().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataInicioNota8().before(getDataBase())) &&
					 (getCalendarioLancamentoNotaVO().getDataTerminoNota8() == null ||
					 	getCalendarioLancamentoNotaVO().getDataTerminoNota8().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataTerminoNota8().after(getDataBase()))))));
	}

	

	public Boolean getNota9Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() &&
					((getCalendarioLancamentoNotaVO().getDataInicioNota9() == null ||
						getCalendarioLancamentoNotaVO().getDataInicioNota9().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataInicioNota9().before(getDataBase())) &&
					 (getCalendarioLancamentoNotaVO().getDataTerminoNota9() == null ||
					 	getCalendarioLancamentoNotaVO().getDataTerminoNota9().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataTerminoNota9().after(getDataBase()))))));
	}
	
	public Boolean getNota31Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() &&
					((getCalendarioLancamentoNotaVO().getDataInicioNota31() == null ||
						getCalendarioLancamentoNotaVO().getDataInicioNota31().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataInicioNota31().before(getDataBase())) &&
					 (getCalendarioLancamentoNotaVO().getDataTerminoNota31() == null ||
					 	getCalendarioLancamentoNotaVO().getDataTerminoNota31().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataTerminoNota31().after(getDataBase()))))));
	}

	public Boolean getNota32Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() &&
					((getCalendarioLancamentoNotaVO().getDataInicioNota32() == null ||
						getCalendarioLancamentoNotaVO().getDataInicioNota32().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataInicioNota32().before(getDataBase())) &&
					 (getCalendarioLancamentoNotaVO().getDataTerminoNota32() == null ||
					 	getCalendarioLancamentoNotaVO().getDataTerminoNota32().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataTerminoNota32().after(getDataBase()))))));
	}

	public Boolean getNota33Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() &&
					((getCalendarioLancamentoNotaVO().getDataInicioNota33() == null ||
						getCalendarioLancamentoNotaVO().getDataInicioNota33().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataInicioNota33().before(getDataBase())) &&
					 (getCalendarioLancamentoNotaVO().getDataTerminoNota33() == null ||
					 	getCalendarioLancamentoNotaVO().getDataTerminoNota33().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataTerminoNota33().after(getDataBase()))))));
	}

	public Boolean getNota34Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() &&
					((getCalendarioLancamentoNotaVO().getDataInicioNota34() == null ||
						getCalendarioLancamentoNotaVO().getDataInicioNota34().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataInicioNota34().before(getDataBase())) &&
					 (getCalendarioLancamentoNotaVO().getDataTerminoNota34() == null ||
					 	getCalendarioLancamentoNotaVO().getDataTerminoNota34().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataTerminoNota34().after(getDataBase()))))));
	}

	public Boolean getNota35Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() &&
					((getCalendarioLancamentoNotaVO().getDataInicioNota35() == null ||
						getCalendarioLancamentoNotaVO().getDataInicioNota35().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataInicioNota35().before(getDataBase())) &&
					 (getCalendarioLancamentoNotaVO().getDataTerminoNota35() == null ||
					 	getCalendarioLancamentoNotaVO().getDataTerminoNota35().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataTerminoNota35().after(getDataBase()))))));
	}

	public Boolean getNota36Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() &&
					((getCalendarioLancamentoNotaVO().getDataInicioNota36() == null ||
						getCalendarioLancamentoNotaVO().getDataInicioNota36().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataInicioNota36().before(getDataBase())) &&
					 (getCalendarioLancamentoNotaVO().getDataTerminoNota36() == null ||
					 	getCalendarioLancamentoNotaVO().getDataTerminoNota36().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataTerminoNota36().after(getDataBase()))))));
	}

	public Boolean getNota37Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() &&
					((getCalendarioLancamentoNotaVO().getDataInicioNota37() == null ||
						getCalendarioLancamentoNotaVO().getDataInicioNota37().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataInicioNota37().before(getDataBase())) &&
					 (getCalendarioLancamentoNotaVO().getDataTerminoNota37() == null ||
					 	getCalendarioLancamentoNotaVO().getDataTerminoNota37().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataTerminoNota37().after(getDataBase()))))));
	}

	public Boolean getNota38Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() &&
					((getCalendarioLancamentoNotaVO().getDataInicioNota38() == null ||
						getCalendarioLancamentoNotaVO().getDataInicioNota38().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataInicioNota38().before(getDataBase())) &&
					 (getCalendarioLancamentoNotaVO().getDataTerminoNota38() == null ||
					 	getCalendarioLancamentoNotaVO().getDataTerminoNota38().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataTerminoNota38().after(getDataBase()))))));
	}

	public Boolean getNota39Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() &&
					((getCalendarioLancamentoNotaVO().getDataInicioNota39() == null ||
						getCalendarioLancamentoNotaVO().getDataInicioNota39().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataInicioNota39().before(getDataBase())) &&
					 (getCalendarioLancamentoNotaVO().getDataTerminoNota39() == null ||
					 	getCalendarioLancamentoNotaVO().getDataTerminoNota39().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataTerminoNota39().after(getDataBase()))))));
	}

	public Boolean getNota40Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() &&
					((getCalendarioLancamentoNotaVO().getDataInicioNota40() == null ||
						getCalendarioLancamentoNotaVO().getDataInicioNota40().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataInicioNota40().before(getDataBase())) &&
					 (getCalendarioLancamentoNotaVO().getDataTerminoNota40() == null ||
					 	getCalendarioLancamentoNotaVO().getDataTerminoNota40().toString().equals(getDataBase().toString()) ||
						getCalendarioLancamentoNotaVO().getDataTerminoNota40().after(getDataBase()))))));
	}

	
	
	public Boolean getIsNotaMedia(Integer nrNota) {
		return (Boolean) UtilReflexao.invocarMetodoGet(this, "isNota"+nrNota+"Media");
	}
	
	public Boolean getIsNota1Media() {
		if (!getConfiguracaoAcademicoVO().getNota1TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(1)) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota2Media() {
		if (!getConfiguracaoAcademicoVO().getNota2TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(2)) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota3Media() {
		if (!getConfiguracaoAcademicoVO().getNota3TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(3)) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota4Media() {
		if (!getConfiguracaoAcademicoVO().getNota4TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(4)) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota5Media() {
		if (!getConfiguracaoAcademicoVO().getNota5TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(5)) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota6Media() {
		if (!getConfiguracaoAcademicoVO().getNota6TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(6)) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota7Media() {
		if (!getConfiguracaoAcademicoVO().getNota7TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(7)) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota8Media() {
		if (!getConfiguracaoAcademicoVO().getNota8TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(8)) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota9Media() {
		if (!getConfiguracaoAcademicoVO().getNota9TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(9)) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota10Media() {
		if (!getConfiguracaoAcademicoVO().getNota10TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(10)) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota11Media() {
		if (!getConfiguracaoAcademicoVO().getNota11TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(11)) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota12Media() {
		if (!getConfiguracaoAcademicoVO().getNota12TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(12)) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota13Media() {
		if (!getConfiguracaoAcademicoVO().getNota13TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(13)) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota14Media() {
		if (!getConfiguracaoAcademicoVO().getNota14TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(14)) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota15Media() {
		if (!getConfiguracaoAcademicoVO().getNota15TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(15)) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota16Media() {
		if (!getConfiguracaoAcademicoVO().getNota16TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(16)) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota17Media() {
		if (!getConfiguracaoAcademicoVO().getNota17TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(17)) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota18Media() {
		if (!getConfiguracaoAcademicoVO().getNota18TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(18)) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota19Media() {
		if (!getConfiguracaoAcademicoVO().getNota19TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(19)) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota20Media() {
		if (!getConfiguracaoAcademicoVO().getNota20TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(20)) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota21Media() {
		if (!getConfiguracaoAcademicoVO().getNota21TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(21)) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota22Media() {
		if (!getConfiguracaoAcademicoVO().getNota22TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(22)) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota23Media() {
		if (!getConfiguracaoAcademicoVO().getNota23TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(23)) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota24Media() {
		if (!getConfiguracaoAcademicoVO().getNota24TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(24)) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota25Media() {
		if (!getConfiguracaoAcademicoVO().getNota25TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(25)) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota26Media() {
		if (!getConfiguracaoAcademicoVO().getNota26TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(26)) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota27Media() {
		if (!getConfiguracaoAcademicoVO().getNota27TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(27)) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota28Media() {
		if (!getConfiguracaoAcademicoVO().getNota28TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(28)) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota29Media() {
		if (!getConfiguracaoAcademicoVO().getNota29TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(29)) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota30Media() {
		if (!getConfiguracaoAcademicoVO().getNota30TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(30)) {
			return true;
		} else {
			return false;
		}
	}
	
	public Boolean getIsNota31Media() {
		if (!getConfiguracaoAcademicoVO().getNota31TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(31)) {
			return true;
		} else {
			return false;
		}
	}
	
	public Boolean getIsNota32Media() {
		if (!getConfiguracaoAcademicoVO().getNota32TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(32)) {
			return true;
		} else {
			return false;
		}
	}
	
	public Boolean getIsNota33Media() {
		if (!getConfiguracaoAcademicoVO().getNota33TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(33)) {
			return true;
		} else {
			return false;
		}
	}
	
	public Boolean getIsNota34Media() {
		if (!getConfiguracaoAcademicoVO().getNota34TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(34)) {
			return true;
		} else {
			return false;
		}
	}
	
	public Boolean getIsNota35Media() {
		if (!getConfiguracaoAcademicoVO().getNota35TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(35)) {
			return true;
		} else {
			return false;
		}
	}
	
	public Boolean getIsNota36Media() {
		if (!getConfiguracaoAcademicoVO().getNota36TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(36)) {
			return true;
		} else {
			return false;
		}
	}
	
	public Boolean getIsNota37Media() {
		if (!getConfiguracaoAcademicoVO().getNota37TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(37)) {
			return true;
		} else {
			return false;
		}
	}
	
	public Boolean getIsNota38Media() {
		if (!getConfiguracaoAcademicoVO().getNota38TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(38)) {
			return true;
		} else {
			return false;
		}
	}
	
	public Boolean getIsNota39Media() {
		if (!getConfiguracaoAcademicoVO().getNota39TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(39)) {
			return true;
		} else {
			return false;
		}
	}
	
	public Boolean getIsNota40Media() {
		if (!getConfiguracaoAcademicoVO().getNota40TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(40)) {
			return true;
		} else {
			return false;
		}
	}


	public List<SelectItem> getListaSelectItemDisciplinas() {
		if (listaSelectItemDisciplinas == null) {
			listaSelectItemDisciplinas = new ArrayList();
		}
		return listaSelectItemDisciplinas;
	}

	public void setListaSelectItemDisciplinas(List<SelectItem> listaSelectItemDisciplinas) {
		this.listaSelectItemDisciplinas = listaSelectItemDisciplinas;
	}

	public List getListaSelectItemPeriodosLetivos() {
		if (listaSelectItemPeriodosLetivos == null) {
			listaSelectItemPeriodosLetivos = new ArrayList();
		}
		return listaSelectItemPeriodosLetivos;
	}

	public void setListaSelectItemPeriodosLetivos(List listaSelectItemPeriodosLetivos) {
		this.listaSelectItemPeriodosLetivos = listaSelectItemPeriodosLetivos;
	}

	public List getListaSelectItemTurma() {
		if (listaSelectItemTurma == null) {
			listaSelectItemTurma = new ArrayList();
		}
		return listaSelectItemTurma;
	}

	public void setListaSelectItemTurma(List listaSelectItemTurma) {
		this.listaSelectItemTurma = listaSelectItemTurma;
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

	public List getListaConsultaTurma() {
		if (listaConsultaTurma == null) {
			listaConsultaTurma = new ArrayList();
		}
		return listaConsultaTurma;
	}

	public void setListaConsultaTurma(List listaConsultaTurma) {
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

	@Override
	protected void limparRecursosMemoria() {
		super.limparRecursosMemoria();
		historicoVO = null;
		historicoAprensentarVO = null;
		turmaVO = null;

		tipoInformarNota = null;
		Uteis.liberarListaMemoria(listaSelectItemTipoInformarNota);
		Uteis.liberarListaMemoria(listaSelectItemDisciplinas);
		Uteis.liberarListaMemoria(listaSelectItemTurma);
		Uteis.liberarListaMemoria(listaSelectItemPeriodosLetivos);		
		Uteis.liberarListaMemoria(alunosTurma);
	}

	/**
	 * @return the configuracaoAcademicoVO
	 */
	public ConfiguracaoAcademicoVO getConfiguracaoAcademicoVO() {
		if (configuracaoAcademicoVO == null) {
			configuracaoAcademicoVO = new ConfiguracaoAcademicoVO();
		}
		return configuracaoAcademicoVO;
	}

	/**
	 * @param configuracaoAcademicoVO
	 *            the configuracaoAcademicoVO to set
	 */
	public void setConfiguracaoAcademicoVO(ConfiguracaoAcademicoVO configuracaoAcademicoVO) {
		this.configuracaoAcademicoVO = configuracaoAcademicoVO;
	}

	public boolean getIsApresentarAnoVisaoProfessorCoordenador() {
		if ((getUsuarioLogado().getVisaoLogar().equals("professor") || getUsuarioLogado().getVisaoLogar().equals("coordenador")) && Uteis.isAtributoPreenchido(getTurmaVO())) {
			if (getTurmaVO().getSemestral() || getTurmaVO().getAnual()) {
				if (!getLoginControle().getPermissaoAcessoMenuVO().getPermitirLancarNotaRetroativo()) {
					setAno(Uteis.getAnoDataAtual());
				}
				return true;
			}
			setAno("");
			return false;
		}
		return true;
	}

	public boolean getIsApresentarSemestreVisaoProfessorCoordenador() {
		if ((getUsuarioLogado().getVisaoLogar().equals("professor") || getUsuarioLogado().getVisaoLogar().equals("coordenador")) && Uteis.isAtributoPreenchido(getTurmaVO())) {
			if (getTurmaVO().getSemestral()) {
				if (!getLoginControle().getPermissaoAcessoMenuVO().getPermitirLancarNotaRetroativo()) {
					setSemestre(Uteis.getSemestreAtual());
				}
				return true;
			}
			setSemestre("");
			return false;
		}
		return true;
	}

	public void verificaNota1Lancada() {
		HistoricoVO historicoVo = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		if (historicoVo.getNota1() != null) {
			historicoVo.setNota1Lancada(true);
		}
	}

	public void verificaNota2Lancada() {
		HistoricoVO historicoVo = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		if (historicoVo.getNota2() != null) {
			historicoVo.setNota2Lancada(true);
		}
	}

	public void verificaNota3Lancada() {
		HistoricoVO historicoVo = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		if (historicoVo.getNota3() != null) {
			historicoVo.setNota3Lancada(true);
		}
	}

	public void verificaNota4Lancada() {
		HistoricoVO historicoVo = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		if (historicoVo.getNota4() != null) {
			historicoVo.setNota4Lancada(true);
		}
	}

	public void verificaNota5Lancada() {
		HistoricoVO historicoVo = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		if (historicoVo.getNota5() != null) {
			historicoVo.setNota5Lancada(true);
		}
	}

	public void verificaNota6Lancada() {
		HistoricoVO historicoVo = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		if (historicoVo.getNota6() != null) {
			historicoVo.setNota6Lancada(true);
		}
	}

	public void verificaNota7Lancada() {
		HistoricoVO historicoVo = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		if (historicoVo.getNota7() != null) {
			historicoVo.setNota7Lancada(true);
		}
	}

	public void verificaNota8Lancada() {
		HistoricoVO historicoVo = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		if (historicoVo.getNota8() != null) {
			historicoVo.setNota8Lancada(true);
		}
	}

	public void verificaNota9Lancada() {
		HistoricoVO historicoVo = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		if (historicoVo.getNota9() != null) {
			historicoVo.setNota9Lancada(true);
		}
	}

	public void verificaNota10Lancada() {
		HistoricoVO historicoVo = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		if (historicoVo.getNota10() != null) {
			historicoVo.setNota10Lancada(true);
		}
	}

	public void verificaNota11Lancada() {
		HistoricoVO historicoVo = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		if (historicoVo.getNota11() != null) {
			historicoVo.setNota11Lancada(true);
		}
	}

	public void verificaNota12Lancada() {
		HistoricoVO historicoVo = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		if (historicoVo.getNota12() != null) {
			historicoVo.setNota12Lancada(true);
		}
	}

	public void verificaNota13Lancada() {
		HistoricoVO historicoVo = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		if (historicoVo.getNota13() != null) {
			historicoVo.setNota13Lancada(true);
		}
	}

	public void verificaNota14Lancada() {
		HistoricoVO historicoVo = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		if (historicoVo.getNota14() != null) {
			historicoVo.setNota14Lancada(true);
		}
	}

	public void verificaNota15Lancada() {
		HistoricoVO historicoVo = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		if (historicoVo.getNota15() != null) {
			historicoVo.setNota15Lancada(true);
		}
	}

	public void verificaNota16Lancada() {
		HistoricoVO historicoVo = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		if (historicoVo.getNota16() != null) {
			historicoVo.setNota16Lancada(true);
		}
	}

	public void verificaNota17Lancada() {
		HistoricoVO historicoVo = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		if (historicoVo.getNota17() != null) {
			historicoVo.setNota17Lancada(true);
		}
	}

	public void verificaNota18Lancada() {
		HistoricoVO historicoVo = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		if (historicoVo.getNota18() != null) {
			historicoVo.setNota18Lancada(true);
		}
	}

	public void verificaNota19Lancada() {
		HistoricoVO historicoVo = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		if (historicoVo.getNota19() != null) {
			historicoVo.setNota19Lancada(true);
		}
	}

	public void verificaNota20Lancada() {
		HistoricoVO historicoVo = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		if (historicoVo.getNota20() != null) {
			historicoVo.setNota20Lancada(true);
		}
	}

	public void verificaNota21Lancada() {
		HistoricoVO historicoVo = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		if (historicoVo.getNota21() != null) {
			historicoVo.setNota21Lancada(true);
		}
	}

	public void verificaNota22Lancada() {
		HistoricoVO historicoVo = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		if (historicoVo.getNota22() != null) {
			historicoVo.setNota22Lancada(true);
		}
	}

	public void verificaNota23Lancada() {
		HistoricoVO historicoVo = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		if (historicoVo.getNota23() != null) {
			historicoVo.setNota23Lancada(true);
		}
	}

	public void verificaNota24Lancada() {
		HistoricoVO historicoVo = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		if (historicoVo.getNota24() != null) {
			historicoVo.setNota24Lancada(true);
		}
	}

	public void verificaNota25Lancada() {
		HistoricoVO historicoVo = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		if (historicoVo.getNota25() != null) {
			historicoVo.setNota25Lancada(true);
		}
	}

	public void verificaNota26Lancada() {
		HistoricoVO historicoVo = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		if (historicoVo.getNota26() != null) {
			historicoVo.setNota26Lancada(true);
		}
	}

	public void verificaNota27Lancada() {
		HistoricoVO historicoVo = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		if (historicoVo.getNota27() != null) {
			historicoVo.setNota27Lancada(true);
		}
	}

	public void verificaNota28Lancada() {
		HistoricoVO historicoVo = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		if (historicoVo.getNota28() != null) {
			historicoVo.setNota28Lancada(true);
		}
	}

	public void realizarCloneListaHistorico() throws CloneNotSupportedException {
		// setListaAnterior((List<?>) ((ArrayList<?>)getAlunosTurma()).clone());
		getListaAnterior().clear();
		for (HistoricoVO obj : getAlunosTurma()) {
			getListaAnterior().add((HistoricoVO) obj.clone());
		}
	}

	public void validarDadosComparacaoListaHistorico() {
		setListasIguais(getListaAnterior().equals(getAlunosTurma()));
	}

	public Boolean getApresentarAvisoGravadosComSucesso() {
		return  !((List<HistoricoVO>)getControleConsultaOtimizado().getListaConsulta()).stream().anyMatch(h -> h.getHistoricoAlterado())
			&& ((List<HistoricoVO>)getControleConsultaOtimizado().getListaConsulta()).stream().anyMatch(h -> h.getHistoricoSalvo());
	}

	public Boolean getApresentarAvisoDadosNaoGravados() {
		return  ((List<HistoricoVO>)getControleConsultaOtimizado().getListaConsulta()).stream().anyMatch(h -> h.getHistoricoAlterado());
	}

	/**
	 * @return the listaAnterior
	 */
	public List<HistoricoVO> getListaAnterior() {
		if (listaAnterior == null) {
			listaAnterior = new ArrayList<HistoricoVO>(0);
		}
		return listaAnterior;
	}

	/**
	 * @param listaAnterior
	 *            the listaAnterior to set
	 */
	public void setListaAnterior(List<HistoricoVO> listaAnterior) {
		this.listaAnterior = listaAnterior;
	}

	/**
	 * @return the listasIguais
	 */
	public Boolean getListasIguais() {
		if (listasIguais == null) {
			listasIguais = Boolean.FALSE;
		}
		return listasIguais;
	}

	/**
	 * @param listasIguais
	 *            the listasIguais to set
	 */
	public void setListasIguais(Boolean listasIguais) {
		this.listasIguais = listasIguais;
	}

	public boolean getApresentarDadosAposSelecionarTurma() {
		return getTurmaVO().getCodigo() != 0;
	}

	public boolean getApresentarDadosAposSelecionarDisciplina() {
		return getHistoricoVO().getDisciplina().getCodigo() != 0;
	}

	public void checarFormatarValoresNota1() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		if (historicoAux.getNota1() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 1);				
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota2() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		limparMensagem();
		if (historicoAux.getNota2() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 2);				
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota3() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		if (historicoAux.getNota3() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 3);				
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota4() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		if (historicoAux.getNota4() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 4);				
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota5() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		if (historicoAux.getNota5() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 5);				
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota6() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		if (historicoAux.getNota6() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 6);				
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota7() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		if (historicoAux.getNota7() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 7);				
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota8() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		if (historicoAux.getNota8() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 8);				
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota9() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		if (historicoAux.getNota9() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 9);				
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota10() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		if (historicoAux.getNota10() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 10);				
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota11() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		if (historicoAux.getNota11() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 11);				
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota12() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		if (historicoAux.getNota12() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 12);				
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota13() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		if (historicoAux.getNota13() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 13);				
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota14() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		if (historicoAux.getNota14() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 14);				
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota15() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		if (historicoAux.getNota15() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 15);				
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota16() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		if (historicoAux.getNota16() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(),16);				
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota17() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		if (historicoAux.getNota17() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 17);				
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota18() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		if (historicoAux.getNota18() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 18);				
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota19() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		if (historicoAux.getNota19() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 19);				
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota20() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		if (historicoAux.getNota20() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 20);				
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota21() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		if (historicoAux.getNota21() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 21);				
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota22() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		if (historicoAux.getNota22() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 22);				
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota23() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		if (historicoAux.getNota23() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(),23);				
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota24() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		if (historicoAux.getNota24() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 24);				
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota25() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		if (historicoAux.getNota25() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 25);				
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota26() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		if (historicoAux.getNota26() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 26);				
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota27() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		if (historicoAux.getNota27() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 27);				
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota28() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		if (historicoAux.getNota28() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 28);				
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}
	
	public void checarFormatarValoresNota29() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		if (historicoAux.getNota29() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 29);				
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}
	
	public void checarFormatarValoresNota30() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		if (historicoAux.getNota30() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 30);				
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota31() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		if (historicoAux.getNota31() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 31);				
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota32() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		if (historicoAux.getNota32() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 32);				
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota33() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		if (historicoAux.getNota33() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 33);				
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota34() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		if (historicoAux.getNota34() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 34);				
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota35() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		if (historicoAux.getNota35() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 35);				
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota36() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		if (historicoAux.getNota36() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 36);				
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota37() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		if (historicoAux.getNota37() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 37);				
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota38() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		if (historicoAux.getNota38() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 38);				
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota39() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		if (historicoAux.getNota39() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 39);				
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota40() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		if (historicoAux.getNota40() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 40);				
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}


	public List getListaSelectItemProfessoresTurma() {
		if (listaSelectItemProfessoresTurma == null) {
			listaSelectItemProfessoresTurma = new ArrayList(0);
		}
		return listaSelectItemProfessoresTurma;
	}

	public void setListaSelectItemProfessoresTurma(List listaSelectItemProfessoresTurma) {
		this.listaSelectItemProfessoresTurma = listaSelectItemProfessoresTurma;
	}

	public PessoaVO getProfessor() {
		if (professor == null) {
			professor = new PessoaVO();
		}
		return professor;
	}

	public void setProfessor(PessoaVO professor) {
		this.professor = professor;
	}

	public boolean getIsApresentarDisciplinas() {
		return (!getProfessor().getCodigo().equals(0) && getListaSelectItemDisciplinas().size() > 1);
	}

	public Boolean getPermiteGravarVisaoCoordenador() {
		if (permiteGravarVisaoCoordenador == null) {
			verificarPermissaoParaGravarLancamentoNotaVisaoCoordenador();
		}
		return permiteGravarVisaoCoordenador;
	}

	public void setPermiteGravarVisaoCoordenador(Boolean permiteGravarVisaoCoordenador) {
		this.permiteGravarVisaoCoordenador = permiteGravarVisaoCoordenador;
	}

	public String getNomeColunas() {
		StringBuilder nomeColunas = new StringBuilder("colunaMatricula,colunaSituacaoMatricula,colunaNomeAluno,colunaSituacao,colunaFrequencia");
		if (getConfiguracaoAcademicoVO().getUtilizarNota1()) {
			nomeColunas.append(",colunaNota1");
		}
		if (getConfiguracaoAcademicoVO().getUtilizarNota2()) {
			nomeColunas.append(",colunaNota2");
		}
		if (getConfiguracaoAcademicoVO().getUtilizarNota3()) {
			nomeColunas.append(",colunaNota3");
		}
		if (getConfiguracaoAcademicoVO().getUtilizarNota4()) {
			nomeColunas.append(",colunaNota4");
		}
		if (getConfiguracaoAcademicoVO().getUtilizarNota5()) {
			nomeColunas.append(",colunaNota5");
		}
		if (getConfiguracaoAcademicoVO().getUtilizarNota6()) {
			nomeColunas.append(",colunaNota6");
		}
		if (getConfiguracaoAcademicoVO().getUtilizarNota7()) {
			nomeColunas.append(",colunaNota7");
		}
		if (getConfiguracaoAcademicoVO().getUtilizarNota8()) {
			nomeColunas.append(",colunaNota8");
		}
		if (getConfiguracaoAcademicoVO().getUtilizarNota9()) {
			nomeColunas.append(",colunaNota9");
		}
		if (getConfiguracaoAcademicoVO().getUtilizarNota10()) {
			nomeColunas.append(",colunaNota10");
		}
		if (getConfiguracaoAcademicoVO().getUtilizarNota11()) {
			nomeColunas.append(",colunaNota11");
		}
		if (getConfiguracaoAcademicoVO().getUtilizarNota12()) {
			nomeColunas.append(",colunaNota12");
		}
		if (getConfiguracaoAcademicoVO().getUtilizarNota13()) {
			nomeColunas.append(",colunaNota13");
		}
		return nomeColunas.toString();
	}

	public List<SelectItem> getListaSelectItemNota1Conceito() {
		if (listaSelectItemNota1Conceito == null) {
			listaSelectItemNota1Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota1Conceito;
	}

	public void setListaSelectItemNota1Conceito(List<SelectItem> listaSelectItemNota1Conceito) {
		this.listaSelectItemNota1Conceito = listaSelectItemNota1Conceito;
	}

	public List<SelectItem> getListaSelectItemNota2Conceito() {
		if (listaSelectItemNota2Conceito == null) {
			listaSelectItemNota2Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota2Conceito;
	}

	public void setListaSelectItemNota2Conceito(List<SelectItem> listaSelectItemNota2Conceito) {
		this.listaSelectItemNota2Conceito = listaSelectItemNota2Conceito;
	}

	public List<SelectItem> getListaSelectItemNota3Conceito() {
		if (listaSelectItemNota3Conceito == null) {
			listaSelectItemNota3Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota3Conceito;
	}

	public void setListaSelectItemNota3Conceito(List<SelectItem> listaSelectItemNota3Conceito) {
		this.listaSelectItemNota3Conceito = listaSelectItemNota3Conceito;
	}

	public List<SelectItem> getListaSelectItemNota4Conceito() {
		if (listaSelectItemNota4Conceito == null) {
			listaSelectItemNota4Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota4Conceito;
	}

	public void setListaSelectItemNota4Conceito(List<SelectItem> listaSelectItemNota4Conceito) {
		this.listaSelectItemNota4Conceito = listaSelectItemNota4Conceito;
	}

	public List<SelectItem> getListaSelectItemNota5Conceito() {
		if (listaSelectItemNota5Conceito == null) {
			listaSelectItemNota5Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota5Conceito;
	}

	public void setListaSelectItemNota5Conceito(List<SelectItem> listaSelectItemNota5Conceito) {
		this.listaSelectItemNota5Conceito = listaSelectItemNota5Conceito;
	}

	public List<SelectItem> getListaSelectItemNota6Conceito() {
		if (listaSelectItemNota6Conceito == null) {
			listaSelectItemNota6Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota6Conceito;
	}

	public void setListaSelectItemNota6Conceito(List<SelectItem> listaSelectItemNota6Conceito) {
		this.listaSelectItemNota6Conceito = listaSelectItemNota6Conceito;
	}

	public List<SelectItem> getListaSelectItemNota7Conceito() {
		if (listaSelectItemNota7Conceito == null) {
			listaSelectItemNota7Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota7Conceito;
	}

	public void setListaSelectItemNota7Conceito(List<SelectItem> listaSelectItemNota7Conceito) {
		this.listaSelectItemNota7Conceito = listaSelectItemNota7Conceito;
	}

	public List<SelectItem> getListaSelectItemNota8Conceito() {
		if (listaSelectItemNota8Conceito == null) {
			listaSelectItemNota8Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota8Conceito;
	}

	public void setListaSelectItemNota8Conceito(List<SelectItem> listaSelectItemNota8Conceito) {
		this.listaSelectItemNota8Conceito = listaSelectItemNota8Conceito;
	}

	public List<SelectItem> getListaSelectItemNota9Conceito() {
		if (listaSelectItemNota9Conceito == null) {
			listaSelectItemNota9Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota9Conceito;
	}

	public void setListaSelectItemNota9Conceito(List<SelectItem> listaSelectItemNota9Conceito) {
		this.listaSelectItemNota9Conceito = listaSelectItemNota9Conceito;
	}

	public List<SelectItem> getListaSelectItemNota10Conceito() {
		if (listaSelectItemNota10Conceito == null) {
			listaSelectItemNota10Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota10Conceito;
	}

	public void setListaSelectItemNota10Conceito(List<SelectItem> listaSelectItemNota10Conceito) {
		this.listaSelectItemNota10Conceito = listaSelectItemNota10Conceito;
	}

	public List<SelectItem> getListaSelectItemNota11Conceito() {
		if (listaSelectItemNota11Conceito == null) {
			listaSelectItemNota11Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota11Conceito;
	}

	public void setListaSelectItemNota11Conceito(List<SelectItem> listaSelectItemNota11Conceito) {
		this.listaSelectItemNota11Conceito = listaSelectItemNota11Conceito;
	}

	public List<SelectItem> getListaSelectItemNota12Conceito() {
		if (listaSelectItemNota12Conceito == null) {
			listaSelectItemNota12Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota12Conceito;
	}

	public void setListaSelectItemNota12Conceito(List<SelectItem> listaSelectItemNota12Conceito) {
		this.listaSelectItemNota12Conceito = listaSelectItemNota12Conceito;
	}

	public List<SelectItem> getListaSelectItemNota13Conceito() {
		if (listaSelectItemNota13Conceito == null) {
			listaSelectItemNota13Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota13Conceito;
	}

	public void setListaSelectItemNota13Conceito(List<SelectItem> listaSelectItemNota13Conceito) {
		this.listaSelectItemNota13Conceito = listaSelectItemNota13Conceito;
	}

	public List<SelectItem> getListaSelectItemNota14Conceito() {
		if (listaSelectItemNota14Conceito == null) {
			listaSelectItemNota14Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota14Conceito;
	}

	public void setListaSelectItemNota14Conceito(List<SelectItem> listaSelectItemNota14Conceito) {
		this.listaSelectItemNota14Conceito = listaSelectItemNota14Conceito;
	}

	public List<SelectItem> getListaSelectItemNota15Conceito() {
		if (listaSelectItemNota15Conceito == null) {
			listaSelectItemNota15Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota15Conceito;
	}

	public void setListaSelectItemNota15Conceito(List<SelectItem> listaSelectItemNota15Conceito) {
		this.listaSelectItemNota15Conceito = listaSelectItemNota15Conceito;
	}

	public List<SelectItem> getListaSelectItemNota16Conceito() {
		if (listaSelectItemNota16Conceito == null) {
			listaSelectItemNota16Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota16Conceito;
	}

	public void setListaSelectItemNota16Conceito(List<SelectItem> listaSelectItemNota16Conceito) {
		this.listaSelectItemNota16Conceito = listaSelectItemNota16Conceito;
	}

	public List<SelectItem> getListaSelectItemNota17Conceito() {
		if (listaSelectItemNota17Conceito == null) {
			listaSelectItemNota17Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota17Conceito;
	}

	public void setListaSelectItemNota17Conceito(List<SelectItem> listaSelectItemNota17Conceito) {
		this.listaSelectItemNota17Conceito = listaSelectItemNota17Conceito;
	}

	public List<SelectItem> getListaSelectItemNota18Conceito() {
		if (listaSelectItemNota18Conceito == null) {
			listaSelectItemNota18Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota18Conceito;
	}

	public void setListaSelectItemNota18Conceito(List<SelectItem> listaSelectItemNota18Conceito) {
		this.listaSelectItemNota18Conceito = listaSelectItemNota18Conceito;
	}

	public List<SelectItem> getListaSelectItemNota19Conceito() {
		if (listaSelectItemNota19Conceito == null) {
			listaSelectItemNota19Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota19Conceito;
	}

	public void setListaSelectItemNota19Conceito(List<SelectItem> listaSelectItemNota19Conceito) {
		this.listaSelectItemNota19Conceito = listaSelectItemNota19Conceito;
	}

	public List<SelectItem> getListaSelectItemNota20Conceito() {
		if (listaSelectItemNota20Conceito == null) {
			listaSelectItemNota20Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota20Conceito;
	}

	public void setListaSelectItemNota20Conceito(List<SelectItem> listaSelectItemNota20Conceito) {
		this.listaSelectItemNota20Conceito = listaSelectItemNota20Conceito;
	}

	public List<SelectItem> getListaSelectItemNota21Conceito() {
		if (listaSelectItemNota21Conceito == null) {
			listaSelectItemNota21Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota21Conceito;
	}

	public void setListaSelectItemNota21Conceito(List<SelectItem> listaSelectItemNota21Conceito) {
		this.listaSelectItemNota21Conceito = listaSelectItemNota21Conceito;
	}

	public List<SelectItem> getListaSelectItemNota22Conceito() {
		if (listaSelectItemNota22Conceito == null) {
			listaSelectItemNota22Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota22Conceito;
	}

	public void setListaSelectItemNota22Conceito(List<SelectItem> listaSelectItemNota22Conceito) {
		this.listaSelectItemNota22Conceito = listaSelectItemNota22Conceito;
	}

	public List<SelectItem> getListaSelectItemNota23Conceito() {
		if (listaSelectItemNota23Conceito == null) {
			listaSelectItemNota23Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota23Conceito;
	}

	public void setListaSelectItemNota23Conceito(List<SelectItem> listaSelectItemNota23Conceito) {
		this.listaSelectItemNota23Conceito = listaSelectItemNota23Conceito;
	}

	public List<SelectItem> getListaSelectItemNota24Conceito() {
		if (listaSelectItemNota24Conceito == null) {
			listaSelectItemNota24Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota24Conceito;
	}

	public void setListaSelectItemNota24Conceito(List<SelectItem> listaSelectItemNota24Conceito) {
		this.listaSelectItemNota24Conceito = listaSelectItemNota24Conceito;
	}

	public List<SelectItem> getListaSelectItemNota25Conceito() {
		if (listaSelectItemNota25Conceito == null) {
			listaSelectItemNota25Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota25Conceito;
	}

	public void setListaSelectItemNota25Conceito(List<SelectItem> listaSelectItemNota25Conceito) {
		this.listaSelectItemNota25Conceito = listaSelectItemNota25Conceito;
	}

	public List<SelectItem> getListaSelectItemNota26Conceito() {
		if (listaSelectItemNota26Conceito == null) {
			listaSelectItemNota26Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota26Conceito;
	}

	public void setListaSelectItemNota26Conceito(List<SelectItem> listaSelectItemNota26Conceito) {
		this.listaSelectItemNota26Conceito = listaSelectItemNota26Conceito;
	}

	public List<SelectItem> getListaSelectItemNota27Conceito() {
		if (listaSelectItemNota27Conceito == null) {
			listaSelectItemNota27Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota27Conceito;
	}

	public void setListaSelectItemNota27Conceito(List<SelectItem> listaSelectItemNota27Conceito) {
		this.listaSelectItemNota27Conceito = listaSelectItemNota27Conceito;
	}

	public CalendarioLancamentoNotaVO getCalendarioLancamentoNotaVO() {
		if (calendarioLancamentoNotaVO == null) {
			calendarioLancamentoNotaVO = new CalendarioLancamentoNotaVO();
		}
		return calendarioLancamentoNotaVO;
	}

	public void setCalendarioLancamentoNotaVO(CalendarioLancamentoNotaVO calendarioLancamentoNotaVO) {
		this.calendarioLancamentoNotaVO = calendarioLancamentoNotaVO;
	}

	public Boolean getExisteRegistroAula() {
		if (existeRegistroAula == null) {
			existeRegistroAula = false;
		}
		return existeRegistroAula;
	}

	public void setExisteRegistroAula(Boolean existeRegistroAula) {
		this.existeRegistroAula = existeRegistroAula;
	}

	public Boolean getPossuiDiversidadeConfiguracaoAcademico() {
		if (possuiDiversidadeConfiguracaoAcademico == null) {
			possuiDiversidadeConfiguracaoAcademico = Boolean.FALSE;
		}
		return possuiDiversidadeConfiguracaoAcademico;
	}

	public void setPossuiDiversidadeConfiguracaoAcademico(Boolean possuiDiversidadeConfiguracaoAcademico) {
		this.possuiDiversidadeConfiguracaoAcademico = possuiDiversidadeConfiguracaoAcademico;
	}

	public HashMap<Integer, ConfiguracaoAcademicoVO> getMapConfiguracaoAcademicoVOs() {
		if (mapConfiguracaoAcademicoVOs == null) {
			mapConfiguracaoAcademicoVOs = new HashMap<Integer, ConfiguracaoAcademicoVO>(0);
		}
		return mapConfiguracaoAcademicoVOs;
	}

	public void setMapConfiguracaoAcademicoVOs(HashMap<Integer, ConfiguracaoAcademicoVO> mapConfiguracaoAcademicoVOs) {
		this.mapConfiguracaoAcademicoVOs = mapConfiguracaoAcademicoVOs;
	}

	public List getListaSelectItemConfiguracaoAcademico() {
		if (listaSelectItemConfiguracaoAcademico == null) {
			listaSelectItemConfiguracaoAcademico = new ArrayList(0);
		}
		return listaSelectItemConfiguracaoAcademico;
	}

	public void setListaSelectItemConfiguracaoAcademico(List listaSelectItemConfiguracaoAcademico) {
		this.listaSelectItemConfiguracaoAcademico = listaSelectItemConfiguracaoAcademico;
	}

	public Boolean getApresentarComboboxConfiguracaoAcademico() {
		return getPossuiDiversidadeConfiguracaoAcademico();
	}

	public String getApresentarModalAvisoDiversidadeConfiguracaoAcademico() {
		if (getPossuiDiversidadeConfiguracaoAcademico() && this.getConfiguracaoAcademicoVO().getCodigo().equals(0)) {
			return "RichFaces.$('panelAvisoDiversidadeConfiguracaoAcademico').show()";
		}
		return "";
	}

	public void limparAlunosTurma() throws Exception {
		setAlunosTurma(new ArrayList(0));
		montarListaDisciplinaTurmaVisaoProfessor();
	}

	public boolean getMostrarAnoSemestre() {
		if ((getTurmaVO().getSemestral() && !getTurmaVO().getCurso().getLiberarRegistroAulaEntrePeriodo()) || getTurmaVO().getAnual()) {
			return true;
		} else {
			return false;
		}
	}

	public boolean getMostrarSemestre() {
		if ((getTurmaVO().getSemestral() && !getTurmaVO().getCurso().getLiberarRegistroAulaEntrePeriodo())) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean getApresentarBotaoCalcularMedia() {
		return isPeriodoLancamentoNota() && !getControleConsultaOtimizado().getListaConsulta().isEmpty();
	}
	
	public boolean getApresentarBotaoCalcularMediaVisaoCoordenador() {
		return isPeriodoLancamentoNota() && getPermiteGravarVisaoCoordenador();
	}

	public List<SelectItem> getListaSelectItemNota28Conceito() {
		if (listaSelectItemNota28Conceito == null) {
			listaSelectItemNota28Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota28Conceito;
	}

	public void setListaSelectItemNota28Conceito(List<SelectItem> listaSelectItemNota28Conceito) {
		this.listaSelectItemNota28Conceito = listaSelectItemNota28Conceito;
	}

	public List<SelectItem> getListaSelectItemNota29Conceito() {
		if (listaSelectItemNota29Conceito == null) {
			listaSelectItemNota29Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota29Conceito;
	}

	public void setListaSelectItemNota29Conceito(List<SelectItem> listaSelectItemNota29Conceito) {
		this.listaSelectItemNota29Conceito = listaSelectItemNota29Conceito;
	}

	public List<SelectItem> getListaSelectItemNota30Conceito() {
		if (listaSelectItemNota30Conceito == null) {
			listaSelectItemNota30Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota30Conceito;
	}

	public void setListaSelectItemNota30Conceito(List<SelectItem> listaSelectItemNota30Conceito) {
		this.listaSelectItemNota30Conceito = listaSelectItemNota30Conceito;
	}

	public List<SelectItem> getListaSelectItemNota31Conceito() {
		if (listaSelectItemNota31Conceito == null) {
			listaSelectItemNota31Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota31Conceito;
	}

	public void setListaSelectItemNota31Conceito(List<SelectItem> listaSelectItemNota31Conceito) {
		this.listaSelectItemNota31Conceito = listaSelectItemNota31Conceito;
	}

	public List<SelectItem> getListaSelectItemNota32Conceito() {
		if (listaSelectItemNota32Conceito == null) {
			listaSelectItemNota32Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota32Conceito;
	}

	public void setListaSelectItemNota32Conceito(List<SelectItem> listaSelectItemNota32Conceito) {
		this.listaSelectItemNota32Conceito = listaSelectItemNota32Conceito;
	}

	public List<SelectItem> getListaSelectItemNota33Conceito() {
		if (listaSelectItemNota33Conceito == null) {
			listaSelectItemNota33Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota33Conceito;
	}

	public void setListaSelectItemNota33Conceito(List<SelectItem> listaSelectItemNota33Conceito) {
		this.listaSelectItemNota33Conceito = listaSelectItemNota33Conceito;
	}

	public List<SelectItem> getListaSelectItemNota34Conceito() {
		if (listaSelectItemNota34Conceito == null) {
			listaSelectItemNota34Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota34Conceito;
	}

	public void setListaSelectItemNota34Conceito(List<SelectItem> listaSelectItemNota34Conceito) {
		this.listaSelectItemNota34Conceito = listaSelectItemNota34Conceito;
	}

	public List<SelectItem> getListaSelectItemNota35Conceito() {
		if (listaSelectItemNota35Conceito == null) {
			listaSelectItemNota35Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota35Conceito;
	}

	public void setListaSelectItemNota35Conceito(List<SelectItem> listaSelectItemNota35Conceito) {
		this.listaSelectItemNota35Conceito = listaSelectItemNota35Conceito;
	}

	public List<SelectItem> getListaSelectItemNota36Conceito() {
		if (listaSelectItemNota36Conceito == null) {
			listaSelectItemNota36Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota36Conceito;
	}

	public void setListaSelectItemNota36Conceito(List<SelectItem> listaSelectItemNota36Conceito) {
		this.listaSelectItemNota36Conceito = listaSelectItemNota36Conceito;
	}

	public List<SelectItem> getListaSelectItemNota37Conceito() {
		if (listaSelectItemNota37Conceito == null) {
			listaSelectItemNota37Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota37Conceito;
	}

	public void setListaSelectItemNota37Conceito(List<SelectItem> listaSelectItemNota37Conceito) {
		this.listaSelectItemNota37Conceito = listaSelectItemNota37Conceito;
	}

	public List<SelectItem> getListaSelectItemNota38Conceito() {
		if (listaSelectItemNota38Conceito == null) {
			listaSelectItemNota38Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota38Conceito;
	}

	public void setListaSelectItemNota38Conceito(List<SelectItem> listaSelectItemNota38Conceito) {
		this.listaSelectItemNota38Conceito = listaSelectItemNota38Conceito;
	}

	public List<SelectItem> getListaSelectItemNota39Conceito() {
		if (listaSelectItemNota39Conceito == null) {
			listaSelectItemNota39Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota39Conceito;
	}

	public void setListaSelectItemNota39Conceito(List<SelectItem> listaSelectItemNota39Conceito) {
		this.listaSelectItemNota39Conceito = listaSelectItemNota39Conceito;
	}

	public List<SelectItem> getListaSelectItemNota40Conceito() {
		if (listaSelectItemNota40Conceito == null) {
			listaSelectItemNota40Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota40Conceito;
	}

	public void setListaSelectItemNota40Conceito(List<SelectItem> listaSelectItemNota40Conceito) {
		this.listaSelectItemNota40Conceito = listaSelectItemNota40Conceito;
	}


	public DataModelo getControleConsultaOtimizado() {
		if (controleConsultaOtimizado == null) {
			controleConsultaOtimizado = new DataModelo();
		}
		return controleConsultaOtimizado;
	}

	public void setControleConsultaOtimizado(DataModelo controleConsultaOtimizado) {
		this.controleConsultaOtimizado = controleConsultaOtimizado;
	}

    public Boolean getConsultaDataScroller() {
        if (consultaDataScroller == null) {
            consultaDataScroller = false;
        }
        return consultaDataScroller;
    }

    public void setConsultaDataScroller(Boolean consultaDataScroller) {
        this.consultaDataScroller = consultaDataScroller;
    }

	public String getValorConsultaAluno() {
		if (valorConsultaAluno == null) {
			valorConsultaAluno = "";
		}
		return valorConsultaAluno;
	}

	public void setValorConsultaAluno(String valorConsultaAluno) {
		this.valorConsultaAluno = valorConsultaAluno;
	}

	public String getLetra() {
		if (letra == null) {
			letra = "";
		}
		return letra;
	}

	public void setLetra(String letra) {
		this.letra = letra;
	}

	public boolean getApresentarLetraASelecionada() {
		return getLetra().equalsIgnoreCase("A");
	}
	
	public boolean getApresentarLetraBSelecionada() {
		return getLetra().equalsIgnoreCase("B");
	}

	public boolean getApresentarLetraCSelecionada() {
		return getLetra().equalsIgnoreCase("C");
	}

	public boolean getApresentarLetraDSelecionada() {
		return getLetra().equalsIgnoreCase("D");
	}
	

	public boolean getApresentarLetraESelecionada() {
		return getLetra().equalsIgnoreCase("E");
	}

	public boolean getApresentarLetraFSelecionada() {
		return getLetra().equalsIgnoreCase("F");
	}

	public boolean getApresentarLetraGSelecionada() {
		return getLetra().equalsIgnoreCase("G");
	}

	public boolean getApresentarLetraHSelecionada() {
		return getLetra().equalsIgnoreCase("H");
	}

	public boolean getApresentarLetraISelecionada() {
		return getLetra().equalsIgnoreCase("I");
	}

	public boolean getApresentarLetraJSelecionada() {
		return getLetra().equalsIgnoreCase("J");
	}

	public boolean getApresentarLetraKSelecionada() {
		return getLetra().equalsIgnoreCase("K");
	}

	public boolean getApresentarLetraLSelecionada() {
		return getLetra().equalsIgnoreCase("L");
	}

	public boolean getApresentarLetraMSelecionada() {
		return getLetra().equalsIgnoreCase("M");
	}

	public boolean getApresentarLetraNSelecionada() {
		return getLetra().equalsIgnoreCase("N");
	}

	public boolean getApresentarLetraOSelecionada() {
		return getLetra().equalsIgnoreCase("O");
	}

	public boolean getApresentarLetraPSelecionada() {
		return getLetra().equalsIgnoreCase("P");
	}

	public boolean getApresentarLetraQSelecionada() {
		return getLetra().equalsIgnoreCase("Q");
	}

	public boolean getApresentarLetraRSelecionada() {
		return getLetra().equalsIgnoreCase("R");
	}

	public boolean getApresentarLetraSSelecionada() {
		return getLetra().equalsIgnoreCase("S");
	}

	public boolean getApresentarLetraTSelecionada() {
		return getLetra().equalsIgnoreCase("T");
	}

	public boolean getApresentarLetraUSelecionada() {
		return getLetra().equalsIgnoreCase("U");
	}

	public boolean getApresentarLetraVSelecionada() {
		return getLetra().equalsIgnoreCase("V");
	}

	public boolean getApresentarLetraXSelecionada() {
		return getLetra().equalsIgnoreCase("X");
	}

	public boolean getApresentarLetraWSelecionada() {
		return getLetra().equalsIgnoreCase("W");
	}
	
	public boolean getApresentarLetraYSelecionada() {
		return getLetra().equalsIgnoreCase("Y");
	}
	
	public boolean getApresentarLetraZSelecionada() {
		return getLetra().equalsIgnoreCase("Z");
	}

	public Boolean getConsultaIndividualPorAluno() {
		if (consultaIndividualPorAluno == null) {
			consultaIndividualPorAluno = Boolean.FALSE;
		}
		return consultaIndividualPorAluno;
	}

	public void setConsultaIndividualPorAluno(Boolean consultaIndividualPorAluno) {
		this.consultaIndividualPorAluno = consultaIndividualPorAluno;
	}

	public Boolean getConsultaPorInicialNome() {
		if (consultaPorInicialNome == null) {
			consultaPorInicialNome = Boolean.FALSE;
		}
		return consultaPorInicialNome;
	}

	public void setConsultaPorInicialNome(Boolean consultaPorInicialNome) {
		this.consultaPorInicialNome = consultaPorInicialNome;
	}

	public Boolean getPossuiHistoricoAlteradoLista() {
		if (possuiHistoricoAlteradoLista == null) {
			possuiHistoricoAlteradoLista = Boolean.FALSE;
		}
		return possuiHistoricoAlteradoLista;
	}

	public void setPossuiHistoricoAlteradoLista(Boolean possuiHistoricoAlteradoLista) {
		this.possuiHistoricoAlteradoLista = possuiHistoricoAlteradoLista;
	}

	public Integer getPaginaRequisitada() {
		if (paginaRequisitada == null) {
			paginaRequisitada = 0;
		}
		return paginaRequisitada;
	}

	public void setPaginaRequisitada(Integer paginaRequisitada) {
		this.paginaRequisitada = paginaRequisitada;
	}

	public Integer getPaginaAtual() {
		if (paginaAtual == null) {
			paginaAtual = 0;
		}
		return paginaAtual;
	}

	public void setPaginaAtual(Integer paginaAtual) {
		this.paginaAtual = paginaAtual;
	}

	public Boolean getCalcularMediaApenasParaDadosAlterados() {
		if (calcularMediaApenasParaDadosAlterados == null) {
			calcularMediaApenasParaDadosAlterados = Boolean.TRUE;
		}
		return calcularMediaApenasParaDadosAlterados;
	}

	public void setCalcularMediaApenasParaDadosAlterados(Boolean calcularMediaApenasParaDadosAlterados) {
		this.calcularMediaApenasParaDadosAlterados = calcularMediaApenasParaDadosAlterados;
	}

	public Boolean getCalcularMediaParaTodosAlunos() {
		if (calcularMediaParaTodosAlunos == null) {
			calcularMediaParaTodosAlunos = Boolean.FALSE;
		}
		return calcularMediaParaTodosAlunos;
	}

	public void setCalcularMediaParaTodosAlunos(Boolean calcularMediaParaTodosAlunos) {
		this.calcularMediaParaTodosAlunos = calcularMediaParaTodosAlunos;
	}
	
	public void desativarOpcaoCalcularMediaTodosAlunos() {
		setCalcularMediaParaTodosAlunos(Boolean.FALSE);
	}
	
	public void desativarOpcaoCalcularMediaApenasParaDadosAlterados() {
		setCalcularMediaApenasParaDadosAlterados(Boolean.FALSE);
	}
	
	public String getFecharModalCalcularMedia() {
		if (getCalcularMediaApenasParaDadosAlterados() || getCalcularMediaParaTodosAlunos()) {
			return "RichFaces.$('panelAvisoCalcularMedia').hide()";
		}
		return "";
	}

	public Map<String, HistoricoVO> getMapHistoricoAlteradoVOs() {
		if (mapHistoricoAlteradoVOs == null) {
			mapHistoricoAlteradoVOs = new HashMap<String, HistoricoVO>(0);
		}
		return mapHistoricoAlteradoVOs;
	}

	public void setMapHistoricoAlteradoVOs(Map<String, HistoricoVO> mapHistoricoAlteradoVOs) {
		this.mapHistoricoAlteradoVOs = mapHistoricoAlteradoVOs;
	}

	public Boolean getRealizouCalculoMedia() {
		if (realizouCalculoMedia == null) {
			realizouCalculoMedia = Boolean.FALSE;
		}
		return realizouCalculoMedia;
	}

	public void setRealizouCalculoMedia(Boolean realizouCalculoMedia) {
		this.realizouCalculoMedia = realizouCalculoMedia;
	}
	
	public Boolean getOcultarSituacaoMatricula() {
		if (ocultarSituacaoMatricula == null) {
			ocultarSituacaoMatricula = Boolean.FALSE;
		}
		return ocultarSituacaoMatricula;
	}

	public void setOcultarSituacaoMatricula(Boolean ocultarSituacaoMatricula) {
		this.ocultarSituacaoMatricula = ocultarSituacaoMatricula;
	}
	
	public TipoAlteracaoSituacaoHistoricoEnum getTipoAlteracaoSituacaoHistorico() {
		if (tipoAlteracaoSituacaoHistorico == null) {
			tipoAlteracaoSituacaoHistorico = TipoAlteracaoSituacaoHistoricoEnum.TODOS_HISTORICOS;
		}
		return tipoAlteracaoSituacaoHistorico;
	}

	public void setTipoAlteracaoSituacaoHistorico(TipoAlteracaoSituacaoHistoricoEnum tipoAlteracaoSituacaoHistorico) {
		this.tipoAlteracaoSituacaoHistorico = tipoAlteracaoSituacaoHistorico;
	}

	private List<SelectItem> listaSelectItemTipoAlteracaoSituacaoHistorico;
	
	public List<SelectItem> getListaSelectItemTipoAlteracaoSituacaoHistorico() {
		if (listaSelectItemTipoAlteracaoSituacaoHistorico == null) {
			listaSelectItemTipoAlteracaoSituacaoHistorico = new ArrayList<SelectItem>(0);
			listaSelectItemTipoAlteracaoSituacaoHistorico.add(new SelectItem(TipoAlteracaoSituacaoHistoricoEnum.TODOS_HISTORICOS, "Todos Históricos"));
			listaSelectItemTipoAlteracaoSituacaoHistorico.add(new SelectItem(TipoAlteracaoSituacaoHistoricoEnum.APENAS_APROVADOS, "Apenas Aprovados"));
			listaSelectItemTipoAlteracaoSituacaoHistorico.add(new SelectItem(TipoAlteracaoSituacaoHistoricoEnum.APENAS_REPROVADOS, "Apenas Reprovados"));
			listaSelectItemTipoAlteracaoSituacaoHistorico.add(new SelectItem(TipoAlteracaoSituacaoHistoricoEnum.APENAS_REPROVADOS_POR_FALTA, "Apenas Reprovados Por Falta"));
			listaSelectItemTipoAlteracaoSituacaoHistorico.add(new SelectItem(TipoAlteracaoSituacaoHistoricoEnum.NENHUM, "Nenhum"));

		}
		return listaSelectItemTipoAlteracaoSituacaoHistorico;
	}

	public void realizarAtualizacaoSituacaoRegraEstabelecidaComboBoxTipoAlteracaoSituacaoHistorico(HistoricoVO histVO, TipoAlteracaoSituacaoHistoricoEnum tipoAlteracaoSituacaoHistorico, String situacaoAtual) {
		if (tipoAlteracaoSituacaoHistorico.equals(TipoAlteracaoSituacaoHistoricoEnum.APENAS_APROVADOS)) {
			if (!histVO.getSituacao().equals(SituacaoHistorico.APROVADO.getValor())) {
				histVO.setSituacao(situacaoAtual);
			}
		} else if (tipoAlteracaoSituacaoHistorico.equals(TipoAlteracaoSituacaoHistoricoEnum.APENAS_REPROVADOS)) {
			if (!histVO.getSituacao().equals(SituacaoHistorico.REPROVADO.getValor())) {
				histVO.setSituacao(situacaoAtual);
			}
		} else if (tipoAlteracaoSituacaoHistorico.equals(TipoAlteracaoSituacaoHistoricoEnum.APENAS_REPROVADOS_POR_FALTA)) {
			if (!histVO.getSituacao().equals(SituacaoHistorico.REPROVADO_FALTA.getValor())) {
				histVO.setSituacao(situacaoAtual);
			}
		} else if (tipoAlteracaoSituacaoHistorico.equals(TipoAlteracaoSituacaoHistoricoEnum.NENHUM)) {
			histVO.setSituacao(situacaoAtual);
		}
	}
	
	public Boolean getApresentarSituacaoAplicandoRegraConfiguracaoAcademica() {
		if (getAlunosTurma() != null && !getAlunosTurma().isEmpty() && !getAlunosTurma().get(0).getHistoricoDisciplinaFazParteComposicao()) {
			return true;
		}
		if (getAlunosTurma() != null && !getAlunosTurma().isEmpty() && getAlunosTurma().get(0).getHistoricoDisciplinaFazParteComposicao() && !getConfiguracaoAcademicoVO().getOcultarSituacaoHistoricoDisciplinaQueFazParteComposicao()) {
			return true;
		}
		return false;
	}
	
	
	public Boolean permiteLancarNotaDisciplinaComposta;
	public Boolean getPermiteLancarNotaDisciplinaComposta() {
		if(permiteLancarNotaDisciplinaComposta == null){
			try {
				permiteLancarNotaDisciplinaComposta = Historico.verificarPermissaoFuncionalidadeUsuario("PermiteLancarNotaDisciplinaComposta", getUsuarioLogado());
			} catch (Exception e) {
				permiteLancarNotaDisciplinaComposta = false;
			}
		}
		return permiteLancarNotaDisciplinaComposta;
	}

	public void setPermiteLancarNotaDisciplinaComposta(Boolean permiteLancarNotaDisciplinaComposta) {
		this.permiteLancarNotaDisciplinaComposta = permiteLancarNotaDisciplinaComposta;
	}

	public Boolean getCalcularMediaAoGravar(){
		try {
			return getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().getCodigo().equals(0) &&
					getApresentarBotaoCalcularMedia() && getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(getTurmaVO().getUnidadeEnsino().getCodigo()).getCalcularMediaAoGravar();
		} catch (Exception e) {
			return false;
		}
	}

	public boolean validarCalculoMediaAutomaticaAoGravar() {
		try {
			if (!getUsuarioLogado().getIsApresentarVisaoProfessor()) {
				return true;
			}
			return  Uteis.isAtributoPreenchido(getCalendarioLancamentoNotaVO().getCalcularMediaAutomaticamenteGravarLancamentoNotasProfessorPartirde()) &&
						UteisData.validarDataInicialMaiorFinal(new Date(), getCalendarioLancamentoNotaVO().getCalcularMediaAutomaticamenteGravarLancamentoNotasProfessorPartirde());
		} catch (ParseException e) {
			return false;
		}
	}

	public void verificarBloquearLancamentosNotasAulasFeriadosFinaisSemana() {
		try {
			boolean feriadoNaData = false;
			boolean bloquearLancamentosNotasAulasFeriadosFinaisSemana = getConfiguracaoGeralPadraoSistema().getBloquearLancamentosNotasAulasFeriadosFinaisSemana();
			feriadoNaData = getFacadeFactory().getFeriadoFacade().verificarFeriadoNesteDia(new Date(), 0, ConsiderarFeriadoEnum.ACADEMICO, false,  getUsuarioLogado());
			if (bloquearLancamentosNotasAulasFeriadosFinaisSemana && (feriadoNaData || UteisData.isFinalDeSemanaComSextaFeira(new Date(), false))) {
				setBloquearLancamentosNotasAulasFeriadosFinaisSemana(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Boolean getBloquearLancamentosNotasAulasFeriadosFinaisSemana() {
		if (bloquearLancamentosNotasAulasFeriadosFinaisSemana == null) {
			bloquearLancamentosNotasAulasFeriadosFinaisSemana = false;
		}
		return bloquearLancamentosNotasAulasFeriadosFinaisSemana;
	}

	public void setBloquearLancamentosNotasAulasFeriadosFinaisSemana(Boolean bloquearLancamentosNotasAulasFeriadosFinaisSemana) {
		this.bloquearLancamentosNotasAulasFeriadosFinaisSemana = bloquearLancamentosNotasAulasFeriadosFinaisSemana;
	}
	
	

	public List<MatriculaPeriodoVO> getMatriculaPeriodoVOs() {
		if(matriculaPeriodoVOs == null){
			matriculaPeriodoVOs = new ArrayList<MatriculaPeriodoVO>(0);
		}
		return matriculaPeriodoVOs;
	}

	public void setMatriculaPeriodoVOs(List<MatriculaPeriodoVO> matriculaPeriodoVOs) {
		this.matriculaPeriodoVOs = matriculaPeriodoVOs;
	}

	public void realizarReprovacaoPeriodoLetivo(){
		try {
			setOncompleteModal("");
			getFacadeFactory().getHistoricoFacade().realizarAlteracaoHistoricoReprovadoPeriodoLetivoDeAcordoConfiguracaoAcademica(getMatriculaPeriodoVOs(), getAno(), getSemestre(), getUsuarioLogado());
			for(MatriculaPeriodoVO matriculaPeriodoVO: getMatriculaPeriodoVOs()){
				for(HistoricoVO historicoVO:getAlunosTurma()){
					if(historicoVO.getMatriculaPeriodo().getCodigo().equals(matriculaPeriodoVO.getCodigo())){
						if(!historicoVO.getReprovado()){
							historicoVO.setSituacao(SituacaoHistorico.REPROVADO_PERIODO_LETIVO.getValor());
						}
						break;
					}
				}
			}
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	

	public TurmaDisciplinaNotaTituloVO getTurmaDisciplinaNotaTituloVO() {
		if(turmaDisciplinaNotaTituloVO == null){
			turmaDisciplinaNotaTituloVO =  new TurmaDisciplinaNotaTituloVO();
		}
		return turmaDisciplinaNotaTituloVO;
	}

	public void setTurmaDisciplinaNotaTituloVO(TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTituloVO) {
		this.turmaDisciplinaNotaTituloVO = turmaDisciplinaNotaTituloVO;
	}

    public void gravarTituloNota(){
    	try {
    		executarValidacaoSimulacaoVisaoProfessor();
    		if(getTurmaDisciplinaNotaTituloVO().getPossuiFormula() && !Uteis.isAtributoPreenchido(getTurmaDisciplinaNotaTituloVO().getFormula())) {
    			setOncompleteModal("");
    			throw new Exception("O campo Fórmula Cálculo deve ser informado!");
    		}
    		List<HistoricoVO> historicoVOs = montarListaAlunosNotaParcialGeral(); 
    		if(Uteis.isAtributoPreenchido(getTurmaDisciplinaNotaTituloVO())){
    			getFacadeFactory().getTurmaDisciplinaNotaTituloFacade().alterar(getTurmaDisciplinaNotaTituloVO(), historicoVOs, getTurmaVO(), getUsuarioLogado(), null, false, false);
    		}else{
    			getFacadeFactory().getTurmaDisciplinaNotaTituloFacade().incluir(getTurmaDisciplinaNotaTituloVO(), historicoVOs, getTurmaVO(), getUsuarioLogado(), null, false, false);
    		}
    		if (getConsultaIndividualPorAluno() || getConsultaPorInicialNome()) {
    			consultarAlunoPorNome();
    		}else {
    			montarAlunosTurma(Boolean.TRUE, Boolean.FALSE);
    		}
    		getTurmaDisciplinaNotaTituloVO().setTituloOriginal(getTurmaDisciplinaNotaTituloVO().getTitulo());
    		for(SelectItem item: getListaSelectItemTipoInformarNota()){
    			if(item.getValue().equals(getTurmaDisciplinaNotaTituloVO().getVariavelConfiguracao())){
    				item.setLabel(getTurmaDisciplinaNotaTituloVO().getTituloNotaApresentar());
    				break;
    			}
    		}
    		setOncompleteModal("RichFaces.$('panelTituloNota').hide();");
    		/*if(getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
    			consultarVisaoCoordenador();
    		}
    		else if(getUsuarioLogado().getIsApresentarVisaoProfessor()) {
    			consultarTurmaPorChavePrimaria();
    		}
    		*/
    		setTurmaDisciplinaNotaTituloVO(new TurmaDisciplinaNotaTituloVO());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
    }
    
    public void cancelarTituloNota(){
    	getTurmaDisciplinaNotaTituloVO().setTitulo(getTurmaDisciplinaNotaTituloVO().getTituloOriginal());
    }

	public Boolean getPermiteInformarTituloNota() {
			try{
			return ControleAcesso.verificarPermissaoFuncionalidadeUsuario("PermiteInformarTituloNota", getUsuarioLogado());
			}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage());
			return false;
			}
		}

	public FormaReplicarNotaOutraDisciplinaEnum getFormaReplicarNotaOutraDisciplina() {
		if(formaReplicarNotaOutraDisciplina == null){
			formaReplicarNotaOutraDisciplina = FormaReplicarNotaOutraDisciplinaEnum.TODAS;
		}
		return formaReplicarNotaOutraDisciplina;
	}

	public void setFormaReplicarNotaOutraDisciplina(FormaReplicarNotaOutraDisciplinaEnum formaReplicarNotaOutraDisciplina) {
		this.formaReplicarNotaOutraDisciplina = formaReplicarNotaOutraDisciplina;
	}

	public List<SelectItem> getListaSelectItemFormaReplicarNotaOutraDisciplina() {
		if(listaSelectItemFormaReplicarNotaOutraDisciplina == null){
			listaSelectItemFormaReplicarNotaOutraDisciplina =  new ArrayList<SelectItem>(0);
			listaSelectItemFormaReplicarNotaOutraDisciplina.add(new SelectItem(FormaReplicarNotaOutraDisciplinaEnum.TODAS, FormaReplicarNotaOutraDisciplinaEnum.TODAS.getValorApresentar()));
			listaSelectItemFormaReplicarNotaOutraDisciplina.add(new SelectItem(FormaReplicarNotaOutraDisciplinaEnum.NOTAS_LANCADAS, FormaReplicarNotaOutraDisciplinaEnum.NOTAS_LANCADAS.getValorApresentar()));
			listaSelectItemFormaReplicarNotaOutraDisciplina.add(new SelectItem(FormaReplicarNotaOutraDisciplinaEnum.NOTAS_NAO_LANCADAS, FormaReplicarNotaOutraDisciplinaEnum.NOTAS_NAO_LANCADAS.getValorApresentar()));
		}
		return listaSelectItemFormaReplicarNotaOutraDisciplina;
	}

	public void setListaSelectItemFormaReplicarNotaOutraDisciplina(
			List<SelectItem> listaSelectItemFormaReplicarNotaOutraDisciplina) {
		this.listaSelectItemFormaReplicarNotaOutraDisciplina = listaSelectItemFormaReplicarNotaOutraDisciplina;
	}

	public TipoNotaConceitoEnum getTipoNotaReplicar() {
		return tipoNotaReplicar;
	}

	public void setTipoNotaReplicar(TipoNotaConceitoEnum tipoNotaReplicar) {
		this.tipoNotaReplicar = tipoNotaReplicar;
	}

	public void gravarReplicacaoNotaOutraDisciplina(){
		try{		
			executarValidacaoSimulacaoVisaoProfessor();
			getFacadeFactory().getHistoricoFacade().realizarReplicacaoNota(getAlunosTurma(), getConfiguracaoAcademicoVO(), getTipoNotaReplicar(), getFormaReplicarNotaOutraDisciplina(), getUsuarioLogado());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void alterarSituacaoHistorico() {
		HistoricoVO histoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		getAlunosTurma().stream().filter(h -> h.getCodigo().equals(histoAux.getCodigo())).findFirst().ifPresent(h -> h.setHistoricoAlterado(Boolean.TRUE));
	}

	/**
	 * @return the permitirLancarNotaRetroativa
	 */
	public Boolean getPermitirLancarNotaRetroativa() {
		if (permitirLancarNotaRetroativa == null) {
			permitirLancarNotaRetroativa = false;
		}
		return permitirLancarNotaRetroativa;
	}

	/**
	 * @param permitirLancarNotaRetroativa the permitirLancarNotaRetroativa to set
	 */
	public void setPermitirLancarNotaRetroativa(Boolean permitirLancarNotaRetroativa) {
		this.permitirLancarNotaRetroativa = permitirLancarNotaRetroativa;
	}
	
	public void verificarPermitirLancarNotaRetroativa() {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("PermitirLancarNotaRetroativo", getUsuarioLogado());
			setPermitirLancarNotaRetroativa(true);
		} catch (Exception e) {
			setPermitirLancarNotaRetroativa(false);
		}
	}
	
	private Boolean permitirVisualizarNotaFrequenciaOutroProfessor;
	
	public Boolean getPermitirVisualizarNotaFrequenciaOutroProfessor() {
		if (permitirVisualizarNotaFrequenciaOutroProfessor == null) {
			try {
				ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("LancamentoNota_PermitirVisualizarNotaFrequenciaOutroProfessor", getUsuarioLogado());
				setPermitirVisualizarNotaFrequenciaOutroProfessor(true);
			} catch (Exception e) {
				setPermitirVisualizarNotaFrequenciaOutroProfessor(false);
}
		}
		return permitirVisualizarNotaFrequenciaOutroProfessor;
	}

	public void setPermitirVisualizarNotaFrequenciaOutroProfessor(Boolean permitirVisualizarNotaFrequenciaOutroProfessor) {
		this.permitirVisualizarNotaFrequenciaOutroProfessor = permitirVisualizarNotaFrequenciaOutroProfessor;
	}

	private List<HistoricoVO> listaHistoricoMinhasNotasAlunoVOs;	
	private MatriculaVO matriculaVO;
	

	public MatriculaVO getMatriculaVO() {
		if(matriculaVO == null) {
			matriculaVO =  new MatriculaVO();
		}
		return matriculaVO;
	}

	public void setMatriculaVO(MatriculaVO matriculaVO) {
		this.matriculaVO = matriculaVO;
	}

	public List<HistoricoVO> getListaHistoricoMinhasNotasAlunoVOs() {
		return listaHistoricoMinhasNotasAlunoVOs;
	}

	public void setListaHistoricoMinhasNotasAlunoVOs(List<HistoricoVO> listaHistoricoMinhasNotasAlunoVOs) {
		this.listaHistoricoMinhasNotasAlunoVOs = listaHistoricoMinhasNotasAlunoVOs;
	}

	public void consultarMinhasNotasAluno() {
		try {
			HistoricoVO historicoVO = (HistoricoVO)getRequestMap().get("historicoItens");
			setMatriculaVO(historicoVO.getMatricula());
			setListaHistoricoMinhasNotasAlunoVOs(getFacadeFactory().getHistoricoFacade().executarMontagemListaHistoricoAluno(historicoVO.getMatricula(), 0, null, null, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(historicoVO.getMatricula().getUnidadeEnsino().getCodigo()), 
					historicoVO.getAnoSemestreApresentar(), false, false, false , getUsuarioLogado()));
			boolean permitirApresentarTodasNotasParametrizadasConfiguracaoAcademica = 
					ControleAcesso.verificarPermissaoFuncionalidadeUsuario(PerfilAcessoPermissaoVisaoAlunoEnum.PERMITIR_APRESENTAR_TODAS_NOTAS_PARAMETRIZADAS_CONFIGURACAO_ACADEMICA.getValor(), getUsuarioLogado());
			getFacadeFactory().getHistoricoFacade().realizarCriacaoDescricaoNotasParciaisHistoricoVOs(getListaHistoricoMinhasNotasAlunoVOs(), permitirApresentarTodasNotasParametrizadasConfiguracaoAcademica, getUsuarioLogado());
			setMensagemID("msg_dados_consultados", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}		 
	}
	
	
	public void consultarFaltasAluno() {
		try {
			setListaDetalhesMinhasFaltasVOs(new ArrayList<RegistroAulaVO>(0));
			HistoricoVO historicoVO = (HistoricoVO)getRequestMap().get("historicoItens");
			setMatriculaVO(historicoVO.getMatricula());			
			if(!Uteis.isAtributoPreenchido(historicoVO.getMatriculaPeriodoTurmaDisciplina().getTurmaPratica()) && !Uteis.isAtributoPreenchido(historicoVO.getMatriculaPeriodoTurmaDisciplina().getTurmaTeorica())) {
				setListaDetalhesMinhasFaltasVOs(getFacadeFactory().getRegistroAulaFacade().consultaRapidaFaltasAlunoTurma(historicoVO.getMatriculaPeriodoTurmaDisciplina().getTurma().getCodigo(), getMatriculaVO().getMatricula(), historicoVO.getDisciplina().getCodigo(), historicoVO.getSemestreHistorico(), historicoVO.getAnoHistorico(), false, getUsuarioLogado()));
			}
			if(Uteis.isAtributoPreenchido(historicoVO.getMatriculaPeriodoTurmaDisciplina().getTurmaPratica())) {
				getListaDetalhesMinhasFaltasVOs().addAll(getFacadeFactory().getRegistroAulaFacade().consultaRapidaFaltasAlunoTurma(historicoVO.getMatriculaPeriodoTurmaDisciplina().getTurmaPratica().getCodigo(), getMatriculaVO().getMatricula(), historicoVO.getDisciplina().getCodigo(), historicoVO.getSemestreHistorico(), historicoVO.getAnoHistorico(), false, getUsuarioLogado()));
			}
			if(Uteis.isAtributoPreenchido(historicoVO.getMatriculaPeriodoTurmaDisciplina().getTurmaTeorica())) {
				getListaDetalhesMinhasFaltasVOs().addAll(getFacadeFactory().getRegistroAulaFacade().consultaRapidaFaltasAlunoTurma(historicoVO.getMatriculaPeriodoTurmaDisciplina().getTurmaTeorica().getCodigo(), getMatriculaVO().getMatricula(), historicoVO.getDisciplina().getCodigo(), historicoVO.getSemestreHistorico(), historicoVO.getAnoHistorico(), false, getUsuarioLogado()));
			}
			setMensagemID("msg_dados_consultados", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	private List<RegistroAulaVO> listaDetalhesMinhasFaltasVOs;
	
	/**
	 * @return the listaDetalhesMinhasFaltasVOs
	 */
	public List<RegistroAulaVO> getListaDetalhesMinhasFaltasVOs() {
		if (listaDetalhesMinhasFaltasVOs == null) {
			listaDetalhesMinhasFaltasVOs = new ArrayList<RegistroAulaVO>();
		}
		return listaDetalhesMinhasFaltasVOs;
	}

	/**
	 * @param listaDetalhesMinhasFaltasVOs
	 *            the listaDetalhesMinhasFaltasVOs to set
	 */
	public void setListaDetalhesMinhasFaltasVOs(List<RegistroAulaVO> listaDetalhesMinhasFaltasVOs) {
		this.listaDetalhesMinhasFaltasVOs = listaDetalhesMinhasFaltasVOs;
	}
	
	public TurmaDisciplinaNotaParcialVO getTurmaDisciplinaNotaParcial() {
		if(turmaDisciplinaNotaParcial == null) {
			turmaDisciplinaNotaParcial= new TurmaDisciplinaNotaParcialVO();
}
		return turmaDisciplinaNotaParcial;
	}

	public void setTurmaDisciplinaNotaParcial(TurmaDisciplinaNotaParcialVO turmaDisciplinaNotaParcial) {
		this.turmaDisciplinaNotaParcial = turmaDisciplinaNotaParcial;
	}

	public void adicionarTurmaDisciplinaNotaParcialItem(){
		try {
			
			getFacadeFactory().getTurmaDisciplinaNotaParcialInterfaceFacade().adicionarTurmaDisciplinaNotaParcialItem(getTurmaDisciplinaNotaParcial(), getTurmaDisciplinaNotaTituloVO());
			
			setTurmaDisciplinaNotaParcial(new TurmaDisciplinaNotaParcialVO());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void removerTurmaDisciplinaNotaParcialItem(){
		try {
			executarValidacaoSimulacaoVisaoProfessor();
		
		TurmaDisciplinaNotaParcialVO turmaDisciplinaNotaParcialVO = (TurmaDisciplinaNotaParcialVO) context().getExternalContext().getRequestMap().get("turmaDisciplinaNotaParcialItens");
		getFacadeFactory().getTurmaDisciplinaNotaParcialInterfaceFacade().removerTurmaDisciplinaNotaParcialItem(turmaDisciplinaNotaParcialVO, getTurmaDisciplinaNotaTituloVO(), getUsuarioLogado());
		
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}	
		
	public List<HistoricoNotaParcialVO> getHistoricoNotaParcialVOs() {
		if(historicoNotaParcialVOs == null) {
			historicoNotaParcialVOs = new ArrayList<HistoricoNotaParcialVO>(0);
		}
		return historicoNotaParcialVOs;
	}

	public void setHistoricoNotaParcialVOs(List<HistoricoNotaParcialVO> historicoNotaParcialVOs) {
		this.historicoNotaParcialVOs = historicoNotaParcialVOs;
	}

	public void buscarHistoricoParcialNota(String tipoNota) throws Exception {

		setHistoricoTemporarioVO((HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens"));
		setHistoricoNotaParcialVOs(getFacadeFactory().getHistoricoNotaParcialInterfaceFacade().consultarPorHistorico(getHistoricoTemporarioVO(), tipoNota, getUsuarioLogado(), getAno(), getSemestre(), Uteis.NIVELMONTARDADOS_DADOSBASICOS));	
		setarTituloNotaApresentar(tipoNota);
	}
	
	public void alterarHistoricosNotaParcial() {
		try {
			executarValidacaoSimulacaoVisaoProfessor();
			getFacadeFactory().getHistoricoNotaParcialInterfaceFacade().alterarHistoricosNotaParcial(getHistoricoNotaParcialVOs(), getHistoricoTemporarioVO(), getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return;
		}
		
	}

	public HistoricoVO getHistoricoTemporarioVO() {
		if (historicoTemporarioVO == null) {
			historicoTemporarioVO = new HistoricoVO();
		}
		return historicoTemporarioVO;
	}

	public void setHistoricoTemporarioVO(HistoricoVO historicoTemporarioVO) {
		this.historicoTemporarioVO = historicoTemporarioVO;
	}
	
	public void buscarHistoricoParcialNotaGeral(String tipoNota) throws Exception {
		getHistoricoNotaParcialGeralVOs().clear();
		//consultarTurmaPorChavePrimaria();
		//montarAlunosTurma(Boolean.FALSE);
		/*if(getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
			consultarVisaoCoordenador();
		}
		else if(getUsuarioLogado().getIsApresentarVisaoProfessor()) {
			montarListaAlunosNotaParcialGeral();
			//consultarTurmaPorChavePrimaria();
		}*/
				
//		if(getUsuarioLogado().getIsApresentarVisaoProfessor()) {
//			montarListaAlunosNotaParcialGeral();
//		}
		for (HistoricoVO obj : getAlunosTurma()) {
			obj.setHistoricoNotaParcialNotaVOs(getFacadeFactory().getHistoricoNotaParcialInterfaceFacade().consultarPorHistorico(obj, tipoNota, getUsuarioLogado(), getAno(), getSemestre(), Uteis.NIVELMONTARDADOS_DADOSBASICOS));	
			//obj.getObterListaNotaParcialPorTipoNota(tipoNota);
			//boolean primeiraVez = true;
			/*for (HistoricoNotaParcialVO historicoNotaParcialVO : obj.getObterListaNotaParcialPorTipoNota(tipoNota)) {
				if (primeiraVez) {
					historicoNotaParcialVO.getHistorico().getMatricula().setMatricula(obj.getMatricula().getMatricula());
					historicoNotaParcialVO.getHistorico().getMatricula().getAluno().setNome(obj.getMatricula().getAluno().getNome());
					primeiraVez = false;
				}
				getHistoricoNotaParcialGeralVOs().add(historicoNotaParcialVO);
			}*/
		}
		setTipoNotaUsar(tipoNota);
		setarTituloNotaApresentar(tipoNota);
		//setHistoricoTemporarioVO((HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens"));
//		setHistoricoNotaParcialGeralVOs(getFacadeFactory().getHistoricoNotaParcialInterfaceFacade().consultarPorTipoNota(tipoNota, getUsuarioLogado(), Uteis.NIVELMONTARDADOS_DADOSBASICOS));	
		
	}

	public List<HistoricoNotaParcialVO> getHistoricoNotaParcialGeralVOs() {
		if (historicoNotaParcialGeralVOs == null) {
			historicoNotaParcialGeralVOs = new ArrayList<HistoricoNotaParcialVO>(0);
		}
		return historicoNotaParcialGeralVOs;
	}

	public void setHistoricoNotaParcialGeralVOs(List<HistoricoNotaParcialVO> historicoNotaParcialGeralVOs) {
		this.historicoNotaParcialGeralVOs = historicoNotaParcialGeralVOs;
	}
	
	public void alterarHistoricosNotaParcialGeral(){
		try {			
			for (HistoricoVO obj : getAlunosTurma()) {
				if(Uteis.isAtributoPreenchido(obj.getHistoricoNotaParcialNotaVOs())) {
					setHistoricoNotaParcialVOs(obj.getHistoricoNotaParcialNotaVOs());
					setHistoricoTemporarioVO(obj);
					alterarHistoricosNotaParcial();
				}
			}
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());	
		}	
	}

	public String getTipoNotaUsar() {
		if (tipoNotaUsar == null) {
			tipoNotaUsar = "";
		}
		return tipoNotaUsar;
	}

	public void setTipoNotaUsar(String tipoNotaUsar) {
		this.tipoNotaUsar = tipoNotaUsar;
	}
	
	public Boolean buscarTurmaDisciplinaNotaParcial(String tipoNota) {
		
		List<TurmaDisciplinaNotaParcialVO> listTurmaDisciplinaNotaParcialVO = new ArrayList<TurmaDisciplinaNotaParcialVO>(0);
		try {
			listTurmaDisciplinaNotaParcialVO = getFacadeFactory().getTurmaDisciplinaNotaParcialInterfaceFacade().consultarPorTurmaDisciplinaTipoNota(getTurmaVO(), getHistoricoVO().getDisciplina(), tipoNota, getHistoricoVO().getAnoHistorico(), getHistoricoVO().getSemestreHistorico(), getHistoricoVO().getConfiguracaoAcademico().getCodigo(), getUsuarioLogado(), Uteis.NIVELMONTARDADOS_DADOSBASICOS);
			if(Uteis.isAtributoPreenchido(listTurmaDisciplinaNotaParcialVO)) {				
				return true;
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());	
		}
		return false;
		
	}

	public String getTituloNotaApresentar() {
		if (tituloNotaApresentar == null) {
			tituloNotaApresentar = "";
		}
		return tituloNotaApresentar;
	}

	public void setTituloNotaApresentar(String tituloNotaApresentar) {
		this.tituloNotaApresentar = tituloNotaApresentar;
	}

	public void setarTituloNotaApresentar(String tipoNota) {
		setTituloNotaApresentar(getFacadeFactory().getTurmaDisciplinaNotaTituloFacade().setarTituloNotaApresentar(tipoNota, getConfiguracaoAcademicoVO().getTurmaDisciplinaNotaTitulo1().getTituloNotaApresentar()));
	}
	
//	public void montarListaAlunosNotaParcialGeral() throws Exception {
//		boolean trazerAlunoPendenteFinanceiramente = getFacadeFactory().getConfiguracaoGeralSistemaFacade().executarVerificacaoApresentarAlunoPendenteFinanceiramente(getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado());
//		
//		boolean permitiVisualizarAlunoTR_CA = verificarPermissaoVisualizarAlunoTR_CA();
//		
//		boolean permitirRealizarLancamentoAlunosPreMatriculados = getConfiguracaoGeralPadraoSistema().getPermitirProfessorRealizarLancamentoAlunosPreMatriculados();
//		
//		setAlunosTurma(getFacadeFactory().getHistoricoFacade().consultaRapidaPorUnidadeEnsinoCursoDisciplinaTurmaAnoSemestreSituacaoMatSituacaoHistFiltroVisaoProfessorRegistroNotaLimiteOffSet(0, 0, getHistoricoVO().getDisciplina().getCodigo(), getTurmaVO(), ano, semestre, trazerAlunoPendenteFinanceiramente, "'AA', 'CC', 'CH', 'IS'", false, true, getUsuarioLogado().getPessoa().getCodigo(), false, null, permitiVisualizarAlunoTR_CA, getPermiteLancarNotaDisciplinaComposta(), Uteis.NIVELMONTARDADOS_TODOS, null, 0, getUsuarioLogado(), permitirRealizarLancamentoAlunosPreMatriculados));
//		
//	}
	
	public List<HistoricoVO> montarListaAlunosNotaParcialGeral() throws Exception {
		boolean trazerAlunoPendenteFinanceiramente = getFacadeFactory().getConfiguracaoGeralSistemaFacade().executarVerificacaoApresentarAlunoPendenteFinanceiramente(getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado());
		
		boolean permitiVisualizarAlunoTR_CA = verificarPermissaoVisualizarAlunoTR_CA();
		
		boolean permitirRealizarLancamentoAlunosPreMatriculados = getConfiguracaoGeralPadraoSistema().getPermitirProfessorRealizarLancamentoAlunosPreMatriculados();
		
		return getFacadeFactory().getHistoricoFacade().consultaRapidaPorUnidadeEnsinoCursoDisciplinaTurmaAnoSemestreSituacaoMatSituacaoHistFiltroVisaoProfessorRegistroNotaLimiteOffSet(0, 0, getHistoricoVO().getDisciplina().getCodigo(), getTurmaVO(), ano, semestre, trazerAlunoPendenteFinanceiramente, "'AA', 'CC', 'CH', 'IS'", false, true, getUsuarioLogado().getPessoa().getCodigo(), false, null, permitiVisualizarAlunoTR_CA, getPermiteLancarNotaDisciplinaComposta(), Uteis.NIVELMONTARDADOS_TODOS, null, 0, getUsuarioLogado(), permitirRealizarLancamentoAlunosPreMatriculados);
		
	}
	
	public Boolean getPermiteDefinirDetalhamentoNota() {
		try{
			return ControleAcesso.verificarPermissaoFuncionalidadeUsuario("LancamentoNota_PermiteDefinirDetalhamentoNota", getUsuarioLogado());
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage());
			return false;
		}		
	}

	public int getTamanhoPopupPanel() {
		if(getPermiteDefinirDetalhamentoNota()) {
			return 600;
		}
		else {
			return 200;
		}
	}

	public Map<Integer, List<HistoricoNotaParcialVO>> getMapHistoricoNotaParcialVOs() {
		if (mapHistoricoNotaParcialVOs == null) {
			mapHistoricoNotaParcialVOs = new HashMap<Integer, List<HistoricoNotaParcialVO>>(0);
		}
		return mapHistoricoNotaParcialVOs;
	}

	public void setMapHistoricoNotaParcialVOs(Map<Integer, List<HistoricoNotaParcialVO>> mapHistoricoNotaParcialVOs) {
		this.mapHistoricoNotaParcialVOs = mapHistoricoNotaParcialVOs;
	}

@PostConstruct
	public void inicializarDados() {
		try {
			if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
				novoVisaoProfessor();
			} else if (getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
				novoVisaoCoordenador();
			} else {
				novo();
}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
}
	

