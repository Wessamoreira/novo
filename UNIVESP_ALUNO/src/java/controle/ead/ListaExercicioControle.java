package controle.ead;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.annotation.PostConstruct;
import jakarta.faces. model.SelectItem;



import org.primefaces.event.DragDropEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import controle.academico.VisaoAlunoControle;
import controle.arquitetura.DataModelo;
import controle.arquitetura.LoginControle;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.ConteudoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.TemaAssuntoVO;
import negocio.comuns.academico.TurmaDisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.SituacaoConteudoEnum;
import negocio.comuns.ead.ListaExercicioVO;
import negocio.comuns.ead.OpcaoRespostaQuestaoVO;
import negocio.comuns.ead.QuestaoListaExercicioVO;
import negocio.comuns.ead.QuestaoVO;
import negocio.comuns.ead.enumeradores.NivelComplexidadeQuestaoEnum;
import negocio.comuns.ead.enumeradores.PeriodoDisponibilizacaoListaExercicioEnum;
import negocio.comuns.ead.enumeradores.PoliticaSelecaoQuestaoEnum;
import negocio.comuns.ead.enumeradores.SituacaoListaExercicioEnum;
import negocio.comuns.ead.enumeradores.SituacaoQuestaoEnum;
import negocio.comuns.ead.enumeradores.TipoGeracaoListaExercicioEnum;
import negocio.comuns.ead.enumeradores.TipoQuestaoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

@Controller("ListaExercicioControle")
@Scope("viewScope")
public class ListaExercicioControle extends SuperControle {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ListaExercicioVO listaExercicioVO;
	protected List<SelectItem> listaSelectItemDisciplina;
	protected List<SelectItem> listaSelectItemSituacaoListaExercicio;
	protected List<SelectItem> listaSelectItemPeriodoDisponibilizacaoListaExercicio;
	protected List<SelectItem> listaSelectItemTipoGeracaoListaExercicio;
	protected List<SelectItem> listaSelectItemPeriodoDisponibilizacaoListaExercicioConsulta;
	protected List<SelectItem> listaSelectItemTipoGeracaoListaExercicioConsulta;
	private List<SelectItem> listaSelectItemTipoQuestaoConsulta;
	private List<SelectItem> listaSelectItemComplexidadeQuestaoConsulta;
	protected List<SelectItem> listaSelectItemTurma;
	protected List<DisciplinaVO> listaConsultaDisciplina;
	private String campoConsultaDisciplina;
	private String valorConsultaDisciplina;
	private List<TurmaVO> listaConsultaTurma;
	private String campoConsultaTurma;
	private String valorConsultaTurma;
	private String matriculaAluno;
	private Integer disciplinaAluno;
	private Integer turmaAluno;
	private Integer qtdeExercicioFacil;
	private Integer qtdeExercicioMedio;
	private Integer qtdeExercicioDificil;
	private Boolean mostarGabarito;
	private QuestaoVO questaoVO;
	private DataModelo controleConsultaQuestao;
	private List<SelectItem> listaSelectItemConteudo;
	private List<ConteudoVO> listaConteudoVOs;
	private List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaOnlineVOs;
	private List<SelectItem> listaSelectItemMatriculaPeriodoTurmaDisciplinaOnlineVOs;
	private MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO;
	private String valorConsulta;
	private String campoConsulta;
	private Boolean desabilitarRecursoRandomizacaoQuestaoProfessor;

	@PostConstruct
	public void inicializarDados() {
		getControleConsultaOtimizado().getListaConsulta().clear();
		getControleConsultaOtimizado().setPage(0);
		getControleConsultaOtimizado().setPaginaAtual(10);
		getControleConsultaOtimizado().setTotalRegistrosEncontrados(0);
		getControleConsultaOtimizado().setLimitePorPagina(10);
		if ((getUsuarioLogado().getIsApresentarVisaoAluno() || getUsuarioLogado().getIsApresentarVisaoPais())) {
			carregarDadosVisaoAluno();
		}else if ((getUsuarioLogado().getIsApresentarVisaoProfessor())) {
			carregarDadosVisaoProfessor();
		}
	}
	
	public void carregarDadosVisaoProfessor(){
		try {
			montarListaSelectItemTurmaDisciplina();
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			e.printStackTrace();
		}
		setMensagemID("msg_entre_prmconsulta", Uteis.ALERTA);
	}
	
	public void carregarDadosVisaoAluno(){
		if((context().getExternalContext().getSessionMap().get("booleanoListaExercicio") != null) && ((Boolean) context().getExternalContext().getSessionMap().get("booleanoListaExercicio"))) {
			setListaExercicioVO((ListaExercicioVO) context().getExternalContext().getSessionMap().get("listaExercicioMonitorConhecimento"));
			setMensagemID("msg_ListaExercicioVisaoAluno_desejamosBoaSorte", Uteis.ALERTA);
			context().getExternalContext().getSessionMap().remove("booleanoListaExercicio");
			context().getExternalContext().getSessionMap().remove("listaExercicioMonitorConhecimento");
		} else {
			VisaoAlunoControle visaoAlunoControle = (VisaoAlunoControle) context().getExternalContext().getSessionMap().get("VisaoAlunoControle");
			setMatriculaAluno(visaoAlunoControle.getMatricula().getMatricula());
			getControleConsultaOtimizado().setLimitePorPagina(10);
			getControleConsultaOtimizado().setPaginaAtual(1);
			getControleConsultaOtimizado().setPage(0);
			if(Uteis.isAtributoPreenchido(visaoAlunoControle.getMatriculaPeriodoTurmaDisciplinaVO())) {
				setMatriculaPeriodoTurmaDisciplinaVO(visaoAlunoControle.getMatriculaPeriodoTurmaDisciplinaVO());
				setDisciplinaAluno(visaoAlunoControle.getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina().getCodigo());
				setTurmaAluno(visaoAlunoControle.getMatriculaPeriodoTurmaDisciplinaVO().getTurma().getCodigo());
			}else if(context().getExternalContext().getSessionMap().get("disciplinaSelecionada") != null) {
				setMatriculaPeriodoTurmaDisciplinaVO((MatriculaPeriodoTurmaDisciplinaVO)context().getExternalContext().getSessionMap().remove("disciplinaSelecionada"));
				setDisciplinaAluno(getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina().getCodigo());
				setTurmaAluno(getMatriculaPeriodoTurmaDisciplinaVO().getTurma().getCodigo());
			}
			consultarMatriculaPeriodoTurmaDisciplinaOnline();
			if(Uteis.isAtributoPreenchido(getMatriculaPeriodoTurmaDisciplinaVO().getCodigo())) {
				consultarListaExercicioVisaoAluno();
			}
		}
	}
	

	public void scrollListener() {
		
		
		if ((getUsuarioLogado().getVisaoLogar().equalsIgnoreCase("aluno") || getUsuarioLogado().getVisaoLogar().equalsIgnoreCase("pais"))) {
			consultarListaExercicioVisaoAluno();
		} else if ((getUsuarioLogado().getVisaoLogar().equalsIgnoreCase("professor"))) {
			consultarListaExercicioProfessor();
		} else {
			consultar();
		}
	}

	public void consultarListaExercicioVisaoAluno() {
		try {
			getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getListaExercicioFacade().consultarListaExercicioDisponivelAluno(getMatriculaPeriodoTurmaDisciplinaVO().getCodigo(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset()));
			getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getListaExercicioFacade().consultarTotalRegistroListaExercicioDisponivelAluno(getMatriculaPeriodoTurmaDisciplinaVO().getCodigo()));
			if (!Uteis.isAtributoPreenchido(getMatriculaPeriodoTurmaDisciplinaVO().getCodigo())) {
				setMensagemDetalhada("msg_erro", "O campo DISCIPLINA (Lista de Execício) deve ser informado.", Uteis.ERRO);
			} else {
				setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
			}
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void consultarListaExercicioProfessor() {
		try {
			if (!Uteis.isAtributoPreenchido(getListaExercicioVO().getDisciplina().getCodigo())) {
				throw new Exception("O campo DISCIPLINA deve ser preenchido.");
			}
			getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getListaExercicioFacade().consultar(getListaExercicioVO().getDisciplina().getCodigo(), getListaExercicioVO().getTurma().getCodigo(), getListaExercicioVO().getDescricao(), getListaExercicioVO().getSituacaoListaExercicio(), getListaExercicioVO().getTipoGeracaoListaExercicio(), getListaExercicioVO().getPeriodoDisponibilizacaoListaExercicio(), true, "ListaExercicioProfessor", getUsuarioLogado(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset()));
			getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getListaExercicioFacade().consultarTotalRegistro(getListaExercicioVO().getDisciplina().getCodigo(), getListaExercicioVO().getTurma().getCodigo(), getListaExercicioVO().getDescricao(), getListaExercicioVO().getSituacaoListaExercicio(), getListaExercicioVO().getTipoGeracaoListaExercicio(), getListaExercicioVO().getPeriodoDisponibilizacaoListaExercicio()));
			if (getControleConsultaOtimizado().getListaConsulta().isEmpty()) {
				throw new Exception(UteisJSF.internacionalizar("msg_relatorio_vazio"));
			}
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public String consultar() {
		try {
			getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getListaExercicioFacade().consultar(getListaExercicioVO().getDisciplina().getCodigo(), getListaExercicioVO().getTurma().getCodigo(), getListaExercicioVO().getDescricao(), getListaExercicioVO().getSituacaoListaExercicio(), getListaExercicioVO().getTipoGeracaoListaExercicio(), getListaExercicioVO().getPeriodoDisponibilizacaoListaExercicio(), true, "ListaExercicio", getUsuarioLogado(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset()));
			getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getListaExercicioFacade().consultarTotalRegistro(getListaExercicioVO().getDisciplina().getCodigo(), getListaExercicioVO().getTurma().getCodigo(), getListaExercicioVO().getDescricao(), getListaExercicioVO().getSituacaoListaExercicio(), getListaExercicioVO().getTipoGeracaoListaExercicio(), getListaExercicioVO().getPeriodoDisponibilizacaoListaExercicio()));
			if (getControleConsultaOtimizado().getListaConsulta().isEmpty()) {
				throw new Exception(UteisJSF.internacionalizar("msg_relatorio_vazio"));
			}
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("listaExercicioEstudoOnlineCons.xhtml");
	}

	public void inativar() {
		try {
			getFacadeFactory().getListaExercicioFacade().inativar(getListaExercicioVO(), true, getUsuarioLogado().getIsApresentarVisaoProfessor() ? "InativarListaExercicioProfessor" : "InativarListaExercicio", getUsuarioLogado());
			setMensagemID("msg_dados_inativado", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void excluir() {
		try {
			executarValidacaoSimulacaoVisaoProfessor();
			if (getListaExercicioVO().isNovoObj()) {
				throw new Exception("Nenhuma Lista Exercício Para Ser Excluída.");
			}
			getFacadeFactory().getListaExercicioFacade().excluir(getListaExercicioVO(), true, getUsuarioLogado().getIsApresentarVisaoProfessor() ? "ListaExercicioProfessor" : "ListaExercicio", getUsuarioLogado());
			novo();
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void clonar() {
		try {
			executarValidacaoSimulacaoVisaoProfessor();
			setListaExercicioVO(getFacadeFactory().getListaExercicioFacade().clonarListaExercicio(getListaExercicioVO()));
			setMensagemID("msg_dados_clonados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void ativar() {
		try {
			executarValidacaoSimulacaoVisaoProfessor();
			getFacadeFactory().getListaExercicioFacade().ativar(getListaExercicioVO(), true, getUsuarioLogado().getIsApresentarVisaoProfessor() ? "ListaExercicioProfessor" : "ListaExercicio", getUsuarioLogado());
			setMensagemID("msg_dados_ativado", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void persistir() {
		try {
			executarValidacaoSimulacaoVisaoProfessor();
			getFacadeFactory().getListaExercicioFacade().persistir(getListaExercicioVO(), true, getUsuarioLogado().getIsApresentarVisaoProfessor() ? "ListaExercicioProfessor" : "ListaExercicio", getUsuarioLogado());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public String editar() {
		try {
			executarValidacaoSimulacaoVisaoProfessor();
			setListaExercicioVO((ListaExercicioVO) context().getExternalContext().getRequestMap().get("listaExercicioItem"));
			if (!getUsuarioLogado().getIsApresentarVisaoAluno() && !getUsuarioLogado().getIsApresentarVisaoPais()) {
				if (getListaExercicioVO().getTipoGeracaoListaExercicio().equals(TipoGeracaoListaExercicioEnum.FIXO)) {
					getListaExercicioVO().setQuestaoListaExercicioVOs(getFacadeFactory().getQuestaoListaExercicioFacade().consultarPorListaExercicio(getListaExercicioVO().getCodigo(), NivelMontarDados.BASICO));
				}
				montarListaSelectItemConteudos();
				inicializarTotalNivelComplexidadeExercicio();
				if (getPermiteAlterarListaExercicio()) {
					setMensagemID("msg_dados_editar", Uteis.ALERTA);
				} else {
					setMensagemID("msg_ListaExercicio_listaAtiva", Uteis.ALERTA);
				}
			} else {
				setMostarGabarito(false);
				setMatriculaPeriodoTurmaDisciplinaVO(getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarPorChavePrimaria(getMatriculaPeriodoTurmaDisciplinaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
//				if(getMatriculaPeriodoTurmaDisciplinaVO().getCodigo() == null) {
//					throw new Exception("Matricula Periodo Vazia");
//				}
				getFacadeFactory().getQuestaoListaExercicioFacade().consultarPorListaExercicioParaRespostaAluno(getListaExercicioVO(), getMatriculaPeriodoTurmaDisciplinaVO(), 0, 0, getUsuarioLogado());
				setMensagemID("msg_ListaExercicioVisaoAluno_desejamosBoaSorte", Uteis.ALERTA);
			}
			if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
				return Uteis.getCaminhoRedirecionamentoNavegacao("listaExercicioProfessorForm");
			}
			if (getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
				return Uteis.getCaminhoRedirecionamentoNavegacao("listaExercicioForm");
			}
			return Uteis.getCaminhoRedirecionamentoNavegacao("listaExercicioAlunoForm");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return Uteis.getCaminhoRedirecionamentoNavegacao("listaExercicioAlunoCons");
		}
	}
	
	public String editarEstudoOnline() {
		try {
			setListaExercicioVO((ListaExercicioVO) context().getExternalContext().getRequestMap().get("listaExercicioItem"));
			if (!getUsuarioLogado().getIsApresentarVisaoAluno() && !getUsuarioLogado().getIsApresentarVisaoPais()) {
				if (getListaExercicioVO().getTipoGeracaoListaExercicio().equals(TipoGeracaoListaExercicioEnum.FIXO)) {
					getListaExercicioVO().setQuestaoListaExercicioVOs(getFacadeFactory().getQuestaoListaExercicioFacade().consultarPorListaExercicio(getListaExercicioVO().getCodigo(), NivelMontarDados.BASICO));
				}
				montarListaSelectItemConteudos();
				inicializarTotalNivelComplexidadeExercicio();
				if (getPermiteAlterarListaExercicio()) {
					setMensagemID("msg_dados_editar", Uteis.ALERTA);
				} else {
					setMensagemID("msg_ListaExercicio_listaAtiva", Uteis.ALERTA);
				}
			} else {
				setMostarGabarito(false);
				getFacadeFactory().getQuestaoListaExercicioFacade().consultarPorListaExercicioParaRespostaAluno(getListaExercicioVO(), getMatriculaPeriodoTurmaDisciplinaVO(), 0, 0, getUsuarioLogado());
				setMensagemID("msg_ListaExercicioVisaoAluno_desejamosBoaSorte", Uteis.ALERTA);
			}
			return Uteis.getCaminhoRedirecionamentoNavegacao("listaExercicioEstudoOnlineForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return "";
		}
	}

	public String novo() {
		try {
			setListaExercicioVO(getFacadeFactory().getListaExercicioFacade().novo());
			getListaExercicioVO().getResponsavelCriacao().setCodigo(getUsuarioLogado().getCodigo());
			getListaExercicioVO().getResponsavelCriacao().setNome(getUsuarioLogado().getNome());
			getListaExercicioVO().setSituacaoListaExercicio(SituacaoListaExercicioEnum.EM_ELABORACAO);
			getListaSelectItemConteudo().clear();
			getListaConteudoVOs().clear();
			verificarPermissaoRecursoRandomizarQuestaoProfessor();
			verificarPermissaoDesabilitarRecursoRandomizarQuestaoProfessor();
			if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
				montarListaSelectItemTurmaDisciplina();
			}
			setMensagemID("msg_entre_dados", Uteis.ALERTA);
			if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
				return Uteis.getCaminhoRedirecionamentoNavegacao("listaExercicioProfessorForm");	
			}
			return Uteis.getCaminhoRedirecionamentoNavegacao("listaExercicioForm");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return Uteis.getCaminhoRedirecionamentoNavegacao("listaExercicioCons");
		}
	}

	public void alterarOrdemApresentacaoQuestaoListaExercicio(DragDropEvent<?> dropEvent) {
		try {
			if (dropEvent.getData() instanceof QuestaoListaExercicioVO && dropEvent.getData() instanceof QuestaoListaExercicioVO) {
				getFacadeFactory().getListaExercicioFacade().alterarOrdemApresentacaoQuestaoListaExercicio(getListaExercicioVO(), (QuestaoListaExercicioVO) dropEvent.getData(), (QuestaoListaExercicioVO) dropEvent.getData());
				limparMensagem();
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void subirQuestaoListaExercicio() {
		try {
			QuestaoListaExercicioVO questaoListaExercicioVO = (QuestaoListaExercicioVO) context().getExternalContext().getRequestMap().get("questaoListaExercicioItem");
			if (questaoListaExercicioVO.getOrdemApresentacao() > 1) {
				QuestaoListaExercicioVO questaoListaExercicio2 = getListaExercicioVO().getQuestaoListaExercicioVOs().get(questaoListaExercicioVO.getOrdemApresentacao() - 2);
				getFacadeFactory().getListaExercicioFacade().alterarOrdemApresentacaoQuestaoListaExercicio(getListaExercicioVO(), questaoListaExercicioVO, questaoListaExercicio2);
			}
			limparMensagem();

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void descerQuestaoListaExercicio() {
		try {
			QuestaoListaExercicioVO questaoListaExercicioVO = (QuestaoListaExercicioVO) context().getExternalContext().getRequestMap().get("questaoListaExercicioItem");
			if (getListaExercicioVO().getQuestaoListaExercicioVOs().size() >= questaoListaExercicioVO.getOrdemApresentacao()) {
				QuestaoListaExercicioVO questaoListaExercicio2 = getListaExercicioVO().getQuestaoListaExercicioVOs().get(questaoListaExercicioVO.getOrdemApresentacao());
				getFacadeFactory().getListaExercicioFacade().alterarOrdemApresentacaoQuestaoListaExercicio(getListaExercicioVO(), questaoListaExercicioVO, questaoListaExercicio2);
			}
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void adicionarQuestao() {
		try {
			QuestaoListaExercicioVO questaoListaExercicioVO = new QuestaoListaExercicioVO();
			QuestaoVO questaoVO = (QuestaoVO) context().getExternalContext().getRequestMap().get("questaoItem");
			questaoListaExercicioVO.setQuestao(questaoVO);
			getFacadeFactory().getListaExercicioFacade().adicionarQuestaoListaExercicio(getListaExercicioVO(), questaoListaExercicioVO);
			questaoVO.setSelecionado(true);
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void removerQuestao() {
		try {
			QuestaoListaExercicioVO questaoListaExercicioVO = new QuestaoListaExercicioVO();
			QuestaoVO questaoVO = (QuestaoVO) context().getExternalContext().getRequestMap().get("questaoItem");
			questaoListaExercicioVO.setQuestao(questaoVO);
			getFacadeFactory().getListaExercicioFacade().removerQuestaoListaExercicio(getListaExercicioVO(), questaoListaExercicioVO);
			questaoVO.setSelecionado(false);
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void removerQuestaoListaExercicio() {
		try {
			getFacadeFactory().getListaExercicioFacade().removerQuestaoListaExercicio(getListaExercicioVO(), (QuestaoListaExercicioVO) context().getExternalContext().getRequestMap().get("questaoListaExercicioItem"));
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void inicializarConsultaQuestao() {
		setControleConsultaQuestao(null);
		limparMensagem();
	}

	public void consultarQuestao() {
		try {
			getControleConsultaQuestao().setLimitePorPagina(5);
			List objs = new ArrayList<>();
			if (getCampoConsulta().equals("codigo")) {
				if (getValorConsulta().equals("")) {
					setValorConsulta("0");
				}
				if (getValorConsulta().trim() != null || !getValorConsulta().trim().isEmpty()) {
					Uteis.validarSomenteNumeroString(getValorConsulta().trim());
				}
				QuestaoVO questaoVO = getFacadeFactory().getQuestaoFacade().consultarPorChavePrimaria(Integer.parseInt(getValorConsulta()));
				objs.add(questaoVO);
				getControleConsultaQuestao().setListaConsulta(objs);
				getControleConsultaQuestao().setTotalRegistrosEncontrados(1);
			} else {
				getControleConsultaQuestao().setListaConsulta(getFacadeFactory().getQuestaoFacade().consultar(getQuestaoVO().getEnunciado(), new TemaAssuntoVO(), getListaExercicioVO().getDisciplina().getCodigo(), SituacaoQuestaoEnum.ATIVA, false, false, true, false, getQuestaoVO().getTipoQuestaoEnum(), getQuestaoVO().getNivelComplexidadeQuestao(), false, "", getUsuarioLogado(), getControleConsultaQuestao().getLimitePorPagina(), getControleConsultaQuestao().getOffset(), getListaExercicioVO().getConteudoVO().getCodigo(), getListaExercicioVO().getPoliticaSelecaoQuestaoEnum(), false, null, null, false));
				getControleConsultaQuestao().setTotalRegistrosEncontrados(getFacadeFactory().getQuestaoFacade().consultarTotalResgistro(getQuestaoVO().getEnunciado(), new TemaAssuntoVO(), getListaExercicioVO().getDisciplina().getCodigo(), SituacaoQuestaoEnum.ATIVA, false, false, true, false, getQuestaoVO().getTipoQuestaoEnum(), getQuestaoVO().getNivelComplexidadeQuestao(), getListaExercicioVO().getConteudoVO().getCodigo(), getListaExercicioVO().getPoliticaSelecaoQuestaoEnum(), false, null, null, getUsuarioLogado(), false));
			}
			marcarQuestaoJaSelecionada();
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}


	public void marcarQuestaoJaSelecionada() {
		q: for (QuestaoVO questaoVO : (List<QuestaoVO>) getControleConsultaQuestao().getListaConsulta()) {
			for (QuestaoListaExercicioVO questaoListaExercicioVO : getListaExercicioVO().getQuestaoListaExercicioVOs()) {
				if (questaoVO.getCodigo().intValue() == questaoListaExercicioVO.getQuestao().getCodigo().intValue()) {
					questaoVO.setSelecionado(true);
					continue q;
				}
			}
		}
	}

	public void paginarQuestao() {
		
		consultarQuestao();
	}

	public void inicializarTotalNivelComplexidadeExercicio() {
		try {
			setListaConteudoVOs(getFacadeFactory().getConteudoFacade().consultarConteudoPorCodigoDisciplina(getListaExercicioVO().getDisciplina().getCodigo(), NivelMontarDados.BASICO, false, getUsuarioLogado()));
			MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO = null;
			if(getListaExercicioVO().getRandomizarApenasQuestoesCadastradasPeloProfessor()) {
				matriculaPeriodoTurmaDisciplinaVO =  new MatriculaPeriodoTurmaDisciplinaVO();
				matriculaPeriodoTurmaDisciplinaVO.getDisciplina().setCodigo(getListaExercicioVO().getDisciplina().getCodigo());
				matriculaPeriodoTurmaDisciplinaVO.getTurma().setCodigo(getListaExercicioVO().getTurma().getCodigo());
				if(getUsuarioLogado().getIsApresentarVisaoProfessor()) {
					matriculaPeriodoTurmaDisciplinaVO.getProfessor().setCodigo(getUsuarioLogado().getPessoa().getCodigo());
				}else {
					matriculaPeriodoTurmaDisciplinaVO.getProfessor().setCodigo(getListaExercicioVO().getResponsavelCriacao().getPessoa().getCodigo());
				}
			}
			for (ConteudoVO conteudoVO : getListaConteudoVOs()) {
				conteudoVO.setQtdeExercicioDificil(getFacadeFactory().getQuestaoFacade().consultarTotalResgistro("", new TemaAssuntoVO(), listaExercicioVO.getDisciplina().getCodigo(), SituacaoQuestaoEnum.ATIVA, false, false, true, false, null, NivelComplexidadeQuestaoEnum.DIFICIL, conteudoVO.getCodigo(), getListaExercicioVO().getPoliticaSelecaoQuestaoEnum(), getListaExercicioVO().getRandomizarApenasQuestoesCadastradasPeloProfessor(), matriculaPeriodoTurmaDisciplinaVO, null, getUsuarioLogado(), false));
				conteudoVO.setQtdeExercicioMedio(getFacadeFactory().getQuestaoFacade().consultarTotalResgistro("", new TemaAssuntoVO(), listaExercicioVO.getDisciplina().getCodigo(), SituacaoQuestaoEnum.ATIVA, false, false, true, false, null, NivelComplexidadeQuestaoEnum.MEDIO, conteudoVO.getCodigo(), getListaExercicioVO().getPoliticaSelecaoQuestaoEnum(), getListaExercicioVO().getRandomizarApenasQuestoesCadastradasPeloProfessor(), matriculaPeriodoTurmaDisciplinaVO, null, getUsuarioLogado(), false));
				conteudoVO.setQtdeExercicioFacil(getFacadeFactory().getQuestaoFacade().consultarTotalResgistro("", new TemaAssuntoVO(), listaExercicioVO.getDisciplina().getCodigo(), SituacaoQuestaoEnum.ATIVA, false, false, true, false, null, NivelComplexidadeQuestaoEnum.FACIL, conteudoVO.getCodigo(), getListaExercicioVO().getPoliticaSelecaoQuestaoEnum(), getListaExercicioVO().getRandomizarApenasQuestoesCadastradasPeloProfessor(), matriculaPeriodoTurmaDisciplinaVO, null, getUsuarioLogado(), false));
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarTurmaPorIdentificadorTurma() {
		try {
			if (getListaExercicioVO().getTurma().getIdentificadorTurma() != null && !getListaExercicioVO().getTurma().getIdentificadorTurma().trim().equals("")) {
				getListaExercicioVO().setTurma(getFacadeFactory().getTurmaFacade().consultarTurmaPorIdentificadorTurma(getListaExercicioVO().getTurma().getIdentificadorTurma(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
				montarListaSelectItemTurmaDisciplina();
			} else {
				getListaExercicioVO().setTurma(new TurmaVO());
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			getListaExercicioVO().setTurma(new TurmaVO());
			setMensagemID("msg_erro_dadosnaoencontrados");
		}
	}

	public void selecionarTurma() {
		try {
			TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItem");
			getListaExercicioVO().setTurma(obj);
			montarListaSelectItemTurmaDisciplina();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> getListaSelectItemTurma() {
		if (listaSelectItemTurma == null) {
			listaSelectItemTurma = new ArrayList<SelectItem>(0);
			List<Integer> mapAuxiliarSelectItem = new ArrayList();
			try {
				List<TurmaVO> turmas = consultarTurmaPorProfessor();
				listaSelectItemTurma.add(new SelectItem(0, ""));
				for (TurmaVO turmaVO : turmas) {
					if(!mapAuxiliarSelectItem.contains(turmaVO.getCodigo())){
						listaSelectItemTurma.add(new SelectItem(turmaVO.getCodigo(), turmaVO.aplicarRegraNomeCursoApresentarCombobox()));
						mapAuxiliarSelectItem.add(turmaVO.getCodigo());
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally{				
				mapAuxiliarSelectItem = null;
			}
		}
		return listaSelectItemTurma;
	}

	public List<TurmaVO> consultarTurmaPorProfessor() {
		try {
			if (getConfiguracaoGeralPadraoSistema().getPerfilPadraoProfessorGraduacao().getCodigo().intValue() == getUsuarioLogado().getPerfilAcesso().getCodigo()) {
				return getFacadeFactory().getTurmaFacade().consultarTurmaPorProfessorAnoSemestreTurmaAnteriorNivelDadosCombobox(getUsuarioLogado().getPessoa().getCodigo(), Uteis.getSemestreAtual(), Uteis.getData(new Date(), "yyyy"), true, "AT", 0, getUsuarioLogado().getVisaoLogar().equals("professor"), false, true, true);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return null;
	}

	public void setListaSelectItemTurma(List<SelectItem> listaSelectItemTurma) {
		this.listaSelectItemTurma = listaSelectItemTurma;
	}

	public void montarListaSelectItemTurmaDisciplina() throws Exception {
		List<DisciplinaVO> listaConsultas = new ArrayList<>();
		getListaSelectItemDisciplina().clear();
		getListaSelectItemDisciplina().add(new SelectItem(0, ""));
		if (!getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
			if (getUsuarioLogado().getIsApresentarVisaoAluno() || getUsuarioLogado().getIsApresentarVisaoPais()) {
				listaConsultas = getFacadeFactory().getDisciplinaFacade().consultarDisciplinaPorMatriculaComListaExercicio(getMatriculaAluno(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			} else if ((getUsuarioLogado().getVisaoLogar().equalsIgnoreCase("professor"))) {
				if (getListaExercicioVO().getTurma().getCodigo() > 0) {
					listaConsultas = getFacadeFactory().getDisciplinaFacade().consultarDisciplinasDoProfessor(getUsuarioLogado().getPessoa().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), null, getListaExercicioVO().getTurma().getCodigo(), null, null, Uteis.NIVELMONTARDADOS_COMBOBOX, true, false, getUsuarioLogado());
				} else {
					listaConsultas = getFacadeFactory().getDisciplinaFacade().consultarDisciplinasDoProfessor(getUsuarioLogado().getPessoa().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), null, null, null, null, Uteis.NIVELMONTARDADOS_COMBOBOX, true, false, getUsuarioLogado());
				}
			}
			for (DisciplinaVO obj : listaConsultas) {
				if (getUsuarioLogado().getVisaoLogar().equalsIgnoreCase("professor")) {
					getListaSelectItemDisciplina().add(new SelectItem(obj.getCodigo(), obj.getDescricaoParaCombobox()));
				} else {
					getListaSelectItemDisciplina().add(new SelectItem(obj.getCodigo(), obj.getNome()));
				}
			}
		} else {
			List<TurmaDisciplinaVO> turmaDisciplinaVOs = getFacadeFactory().getTurmaDisciplinaFacade().consultarTurmaDisciplinas(getListaExercicioVO().getTurma().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			for (TurmaDisciplinaVO turmaDisciplinaVO : turmaDisciplinaVOs) {
				getListaSelectItemDisciplina().add(new SelectItem(turmaDisciplinaVO.getDisciplina().getCodigo(), turmaDisciplinaVO.getDisciplina().getNome()));
			}
		}
	}

	public void consultarTurma() {
		try {
			super.consultar();
			List<TurmaVO> objs = new ArrayList<TurmaVO>(0);
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				if (getValorConsultaTurma().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(getValorConsultaTurma(), getUnidadeEnsinoLogado().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeUnidadeEnsino")) {
				if (getValorConsultaTurma().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorUnidadeEnsino(getValorConsultaTurma(), getUnidadeEnsinoLogado().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeTurno")) {
				if (getValorConsultaTurma().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorTurno(getValorConsultaTurma(), getUnidadeEnsinoLogado().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeCurso")) {
				if (getValorConsultaTurma().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaNomeCurso(getValorConsultaTurma(), getUnidadeEnsinoLogado().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaTurma(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaTurma(new ArrayList<TurmaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	private List<SelectItem> tipoConsultaComboTurma;

	public List<SelectItem> getTipoConsultaComboTurma() {
		if (tipoConsultaComboTurma == null) {
			tipoConsultaComboTurma = new ArrayList<SelectItem>(0);
			tipoConsultaComboTurma.add(new SelectItem("identificadorTurma", "Identificador"));
			tipoConsultaComboTurma.add(new SelectItem("nomeUnidadeEnsino", "Unidade Ensino"));
			tipoConsultaComboTurma.add(new SelectItem("nomeTurno", "Turno"));
			tipoConsultaComboTurma.add(new SelectItem("nomeCurso", "Curso"));
		}
		return tipoConsultaComboTurma;
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

	public List<TurmaVO> getListaConsultaTurma() {
		if (listaConsultaTurma == null) {
			listaConsultaTurma = new ArrayList<TurmaVO>(0);
		}
		return listaConsultaTurma;
	}

	public void setListaConsultaTurma(List<TurmaVO> listaConsultaTurma) {
		this.listaConsultaTurma = listaConsultaTurma;
	}

	@SuppressWarnings("unchecked")
	public void consultarDisciplina() {
		try {
			List<DisciplinaVO> objs = new ArrayList<DisciplinaVO>(0);
			if (getValorConsultaDisciplina().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			if (getCampoConsultaDisciplina().equals("codigo")) {
				int valorInt = Integer.parseInt(getValorConsultaDisciplina());
				DisciplinaVO disciplina = getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(new Integer(valorInt), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				if (Uteis.isAtributoPreenchido(disciplina)) {
					objs.add(disciplina);
				}
			}
			if (getCampoConsultaDisciplina().equals("nome")) {
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorNome(getValorConsultaDisciplina(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaDisciplina().equals("areaConhecimento")) {
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorNomeAreaConhecimento(getValorConsultaDisciplina(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}

			setListaConsultaDisciplina(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaDisciplina(new ArrayList<DisciplinaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparTurma() {
		getListaExercicioVO().setTurma(new TurmaVO());
	}

	public void alterarDisciplina() {
		getListaExercicioVO().getQuestaoListaExercicioVOs().clear();
		inicializarTotalNivelComplexidadeExercicio();
	}

	public void limparDisciplina() {
		getListaExercicioVO().setDisciplina(null);
		inicializarTotalNivelComplexidadeExercicio();
		setMensagemID("msg_informe_disciplina", Uteis.ALERTA);
	}

	public void selecionarDisciplina() throws Exception {
		try {
			setMensagemDetalhada("");
			DisciplinaVO disciplina = (DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplinaItem");
			getListaExercicioVO().setDisciplina(disciplina);
			inicializarTotalNivelComplexidadeExercicio();
			montarListaSelectItemConteudos();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarDisciplinaConsulta() throws Exception {
		try {
			setMensagemDetalhada("");
			DisciplinaVO disciplina = (DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplinaItem");
			getListaExercicioVO().setDisciplina(disciplina);
			getControleConsultaOtimizado().setPage(0);
			getControleConsultaOtimizado().setPaginaAtual(1);
			consultar();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> tipoConsultaComboDisciplina;

	public List<SelectItem> getTipoConsultaComboDisciplina() {
		if (tipoConsultaComboDisciplina == null) {
			tipoConsultaComboDisciplina = new ArrayList<SelectItem>(0);
			tipoConsultaComboDisciplina.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboDisciplina.add(new SelectItem("codigo", "Código"));
		}
		return tipoConsultaComboDisciplina;
	}

	public ListaExercicioVO getListaExercicioVO() {
		if (listaExercicioVO == null) {
			listaExercicioVO = new ListaExercicioVO();
		}
		return listaExercicioVO;
	}

	public void setListaExercicioVO(ListaExercicioVO listaExercicioVO) {
		this.listaExercicioVO = listaExercicioVO;
	}

	public String inicializarConsultar() {
		setListaExercicioVO(new ListaExercicioVO());
		inicializarDados();
		limparMensagem();
		if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
			return Uteis.getCaminhoRedirecionamentoNavegacao("listaExercicioProfessorCons");	
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("listaExercicioCons");
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

	public List<SelectItem> getListaSelectItemDisciplina() {
		if (listaSelectItemDisciplina == null) {
			listaSelectItemDisciplina = new ArrayList<SelectItem>();
		}
		return listaSelectItemDisciplina;
	}

	public void setListaSelectItemDisciplina(List<SelectItem> listaSelectItemDisciplina) {
		this.listaSelectItemDisciplina = listaSelectItemDisciplina;
	}

	public String getMatriculaAluno() {
		return matriculaAluno;
	}

	public void setMatriculaAluno(String matriculaAluno) {
		this.matriculaAluno = matriculaAluno;
	}

	public Integer getDisciplinaAluno() {
		return disciplinaAluno;
	}

	public void setDisciplinaAluno(Integer disciplinaAluno) {
		this.disciplinaAluno = disciplinaAluno;
	}

	public Integer getTurmaAluno() {
		return turmaAluno;
	}

	public void setTurmaAluno(Integer turmaAluno) {
		this.turmaAluno = turmaAluno;
	}

	public List<SelectItem> getListaSelectItemSituacaoListaExercicio() {
		if (listaSelectItemSituacaoListaExercicio == null) {
			listaSelectItemSituacaoListaExercicio = new ArrayList<SelectItem>(0);
			listaSelectItemSituacaoListaExercicio = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(SituacaoListaExercicioEnum.class, "name", "valorApresentar", true);
		}
		return listaSelectItemSituacaoListaExercicio;
	}

	public List<SelectItem> getListaSelectItemPeriodoDisponibilizacaoListaExercicio() {
		if (listaSelectItemPeriodoDisponibilizacaoListaExercicio == null) {
			listaSelectItemPeriodoDisponibilizacaoListaExercicio = new ArrayList<SelectItem>(0);
			listaSelectItemPeriodoDisponibilizacaoListaExercicio = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(PeriodoDisponibilizacaoListaExercicioEnum.class, "name", "valorApresentar", true);
		}
		return listaSelectItemPeriodoDisponibilizacaoListaExercicio;
	}

	public List<SelectItem> getListaSelectItemTipoGeracaoListaExercicio() {
		if (listaSelectItemTipoGeracaoListaExercicio == null) {
			listaSelectItemTipoGeracaoListaExercicio = new ArrayList<SelectItem>(0);
			listaSelectItemTipoGeracaoListaExercicio = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoGeracaoListaExercicioEnum.class, "name", "valorApresentar", false);
		}
		return listaSelectItemTipoGeracaoListaExercicio;
	}

	public Integer getQtdeExercicioFacil() {
		if (qtdeExercicioFacil == null) {
			qtdeExercicioFacil = 0;
		}
		return qtdeExercicioFacil;
	}

	public void setQtdeExercicioFacil(Integer qtdeExercicioFacil) {
		this.qtdeExercicioFacil = qtdeExercicioFacil;
	}

	public Integer getQtdeExercicioMedio() {
		if (qtdeExercicioMedio == null) {
			qtdeExercicioMedio = 0;
		}
		return qtdeExercicioMedio;
	}

	public void setQtdeExercicioMedio(Integer qtdeExercicioMedio) {
		this.qtdeExercicioMedio = qtdeExercicioMedio;
	}

	public Integer getQtdeExercicioDificil() {
		if (qtdeExercicioDificil == null) {
			qtdeExercicioDificil = 0;
		}
		return qtdeExercicioDificil;
	}

	public void setQtdeExercicioDificil(Integer qtdeExercicioDificil) {
		this.qtdeExercicioDificil = qtdeExercicioDificil;
	}

	public QuestaoVO getQuestaoVO() {
		if (questaoVO == null) {
			questaoVO = new QuestaoVO();
		}
		return questaoVO;
	}

	public void setQuestaoVO(QuestaoVO questaoVO) {
		this.questaoVO = questaoVO;
	}

	public DataModelo getControleConsultaQuestao() {
		if (controleConsultaQuestao == null) {
			controleConsultaQuestao = new DataModelo();
			controleConsultaQuestao.setLimitePorPagina(10);
		}
		return controleConsultaQuestao;
	}

	public void setControleConsultaQuestao(DataModelo controleConsultaQuestao) {
		this.controleConsultaQuestao = controleConsultaQuestao;
	}

	public List<SelectItem> getListaSelectItemTipoQuestaoConsulta() {
		if (listaSelectItemTipoQuestaoConsulta == null) {
			listaSelectItemTipoQuestaoConsulta = new ArrayList<SelectItem>(0);
			listaSelectItemTipoQuestaoConsulta.add(new SelectItem(null, ""));
			listaSelectItemTipoQuestaoConsulta.add(new SelectItem(TipoQuestaoEnum.UNICA_ESCOLHA, TipoQuestaoEnum.UNICA_ESCOLHA.getValorApresentar()));
			listaSelectItemTipoQuestaoConsulta.add(new SelectItem(TipoQuestaoEnum.MULTIPLA_ESCOLHA, TipoQuestaoEnum.MULTIPLA_ESCOLHA.getValorApresentar()));
		}
		return listaSelectItemTipoQuestaoConsulta;
	}

	public List<SelectItem> getListaSelectItemComplexidadeQuestaoConsulta() {
		if (listaSelectItemComplexidadeQuestaoConsulta == null) {
			listaSelectItemComplexidadeQuestaoConsulta = new ArrayList<SelectItem>(0);
			if (!getUsuarioLogado().getIsApresentarVisaoProfessor()) {
				listaSelectItemComplexidadeQuestaoConsulta.add(new SelectItem(null, ""));
			}
			listaSelectItemComplexidadeQuestaoConsulta.add(new SelectItem(NivelComplexidadeQuestaoEnum.FACIL, NivelComplexidadeQuestaoEnum.FACIL.getValorApresentar()));
			listaSelectItemComplexidadeQuestaoConsulta.add(new SelectItem(NivelComplexidadeQuestaoEnum.MEDIO, NivelComplexidadeQuestaoEnum.MEDIO.getValorApresentar()));
			listaSelectItemComplexidadeQuestaoConsulta.add(new SelectItem(NivelComplexidadeQuestaoEnum.DIFICIL, NivelComplexidadeQuestaoEnum.DIFICIL.getValorApresentar()));
		}
		return listaSelectItemComplexidadeQuestaoConsulta;
	}

	private LoginControle loginControle = (LoginControle) getControlador("LoginControle");

	public Boolean getPermiteAlterarListaExercicio() {
		if (getListaExercicioVO().getSituacaoListaExercicio() == null) {
			getListaExercicioVO().setSituacaoListaExercicio(SituacaoListaExercicioEnum.EM_ELABORACAO);
		}
		return (getListaExercicioVO().isNovoObj() || getListaExercicioVO().getResponsavelCriacao().getCodigo().equals(getUsuarioLogado().getCodigo()) || loginControle.getPermissaoAcessoMenuVO().getAlterarListaExercicioOutroProfessor()) && getListaExercicioVO().getSituacaoListaExercicio().equals(SituacaoListaExercicioEnum.EM_ELABORACAO);
	}

	public Boolean getPermiteAlterarListaExercicioProfessor() {
		if (getListaExercicioVO().getSituacaoListaExercicio() == null) {
			getListaExercicioVO().setSituacaoListaExercicio(SituacaoListaExercicioEnum.EM_ELABORACAO);
		}
		return (getListaExercicioVO().isNovoObj() || getListaExercicioVO().getResponsavelCriacao().getCodigo().equals(getUsuarioLogado().getCodigo()) || loginControle.getPermissaoAcessoMenuVO().getAlterarListaExercicioOutroProfessorProfessor()) && getListaExercicioVO().getSituacaoListaExercicio().equals(SituacaoListaExercicioEnum.EM_ELABORACAO);
	}

	public Boolean getPossuiPermissaoInativar() {
		if (getListaExercicioVO().getSituacaoListaExercicio() == null) {
			getListaExercicioVO().setSituacaoListaExercicio(SituacaoListaExercicioEnum.EM_ELABORACAO);
		}
		return getListaExercicioVO().getSituacaoListaExercicio() != null && getListaExercicioVO().getSituacaoListaExercicio().equals(SituacaoListaExercicioEnum.ATIVA) && (getListaExercicioVO().getResponsavelCriacao().getCodigo().equals(getUsuarioLogado().getCodigo()) || loginControle.getPermissaoAcessoMenuVO().getAlterarListaExercicioOutroProfessor());
	}

	public Boolean getPossuiPermissaoInativarProfessor() {
		if (getListaExercicioVO().getSituacaoListaExercicio() == null) {
			getListaExercicioVO().setSituacaoListaExercicio(SituacaoListaExercicioEnum.EM_ELABORACAO);
		}
		return getListaExercicioVO().getSituacaoListaExercicio() != null && getListaExercicioVO().getSituacaoListaExercicio().equals(SituacaoListaExercicioEnum.ATIVA) && (getListaExercicioVO().getResponsavelCriacao().getCodigo().equals(getUsuarioLogado().getCodigo()) || loginControle.getPermissaoAcessoMenuVO().getAlterarListaExercicioOutroProfessorProfessor());
	}

	public void realizarCorrecaoExercicio() {
		try {
			getFacadeFactory().getListaExercicioFacade().realizarGeracaoGabarito(getListaExercicioVO());
			setMensagem("");
			setMensagemID("");
			setMensagemDetalhada("");
			setIconeMensagem("");
			setSucesso(false);
			getListaMensagemErro().clear();
			setMostarGabarito(true);
		} catch (ConsistirException e) {
			setMostarGabarito(false);
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ALERTA);
		} catch (Exception e) {
			setMostarGabarito(false);
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ALERTA);
		}
	}

	public Boolean getMostarGabarito() {
		if (mostarGabarito == null) {
			mostarGabarito = false;
		}
		return mostarGabarito;
	}

	public void setMostarGabarito(Boolean mostarGabarito) {
		this.mostarGabarito = mostarGabarito;
	}

	public void realizarVerificacaoQuestaoUnicaEscolha() {
		OpcaoRespostaQuestaoVO orq = (OpcaoRespostaQuestaoVO) context().getExternalContext().getRequestMap().get("opcaoRespostaQuestaoItens");
		if (orq.getMarcada()) {
			orq.setMarcada(false);
		} else {
			orq.setMarcada(true);
			getFacadeFactory().getListaExercicioFacade().realizarVerificacaoQuestaoUnicaEscolha(getListaExercicioVO(), orq);
		}
	}

	/**
	 * @author Victor Hugo 09/01/2015
	 */
	public List<SelectItem> getListaSelectItemConteudo() {
		if (listaSelectItemConteudo == null) {
			listaSelectItemConteudo = new ArrayList<SelectItem>();
		}
		return listaSelectItemConteudo;
	}

	public void setListaSelectItemConteudo(List<SelectItem> listaSelectItemConteudo) {
		this.listaSelectItemConteudo = listaSelectItemConteudo;
	}

	public void montarListaSelectItemConteudos() {
		try {
			List<ConteudoVO> resultado = getFacadeFactory().getConteudoFacade().consultarConteudoPorCodigoDisciplina(getListaExercicioVO().getDisciplina().getCodigo(), NivelMontarDados.BASICO, false, getUsuarioLogado());
			if (!getListaExercicioVO().getCodigo().equals(0) && !getListaExercicioVO().getConteudoVO().getCodigo().equals(0)) {
				getListaExercicioVO().setConteudoVO(getFacadeFactory().getConteudoFacade().consultarPorChavePrimaria(getListaExercicioVO().getConteudoVO().getCodigo(), NivelMontarDados.BASICO, false, getUsuarioLogado()));
				if (!getListaExercicioVO().getConteudoVO().getSituacaoConteudo().equals(SituacaoConteudoEnum.ATIVO)) {
					resultado.add(getListaExercicioVO().getConteudoVO());
				}
			}
			setListaSelectItemConteudo(UtilSelectItem.getListaSelectItem(resultado, "codigo", "descricao", true));
			inicializarTotalNivelComplexidadeExercicio();
			getIsApresentarCamposQuestaoFixaListaExercicio();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Boolean getIsApresentarCamposQuestaoFixaListaExercicio() {
		return (getListaExercicioVO().getTipoGeracaoListaExercicio().equals(TipoGeracaoListaExercicioEnum.FIXO)) && (getListaExercicioVO().getDisciplina().getCodigo() != 0);
	}

	public List<ConteudoVO> getListaConteudoVOs() {
		if (listaConteudoVOs == null) {
			listaConteudoVOs = new ArrayList<ConteudoVO>();
		}
		return listaConteudoVOs;
	}

	public void setListaConteudoVOs(List<ConteudoVO> listaConteudoVOs) {
		this.listaConteudoVOs = listaConteudoVOs;
	}

	public Boolean getIsApresentarRegraDistribuicaoQuestao() {
		return getListaExercicioVO().getTipoGeracaoListaExercicio().equals(TipoGeracaoListaExercicioEnum.RANDOMICO) && (getListaExercicioVO().getPoliticaSelecaoQuestaoEnum().equals(PoliticaSelecaoQuestaoEnum.QUESTOES_TODOS_ASSUNTOS_CONTEUDO) || getListaExercicioVO().getPoliticaSelecaoQuestaoEnum().equals(PoliticaSelecaoQuestaoEnum.QUESTOES_ASSUNTO_ESTUDADOS));
	}

	public void consultarMatriculaPeriodoTurmaDisciplinaOnline() {
		try {
//			setMatriculaPeriodoTurmaDisciplinaOnlineVOs(getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarDisciplinaOnlineDoAlunoPorMatricula(getVisaoAlunoControle().getMatricula().getMatricula(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
//			Ordenacao.ordenarLista(getMatriculaPeriodoTurmaDisciplinaOnlineVOs(), "ordemEstudoOnline");
			getListaSelectItemMatriculaPeriodoTurmaDisciplinaOnlineVOs().clear();
			getListaSelectItemMatriculaPeriodoTurmaDisciplinaOnlineVOs().add(new SelectItem("", ""));
			for (MatriculaPeriodoTurmaDisciplinaVO obj : getVisaoAlunoControle().getListaMatriculaPeriodoTurmaDisciplinaVOs()) {
				if(obj.getQtdeListaExercicio() > 0) {
					getListaSelectItemMatriculaPeriodoTurmaDisciplinaOnlineVOs().add(new SelectItem(obj.getCodigo(), obj.getDisciplina().getNome()));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<MatriculaPeriodoTurmaDisciplinaVO> getMatriculaPeriodoTurmaDisciplinaOnlineVOs() {
		if (matriculaPeriodoTurmaDisciplinaOnlineVOs == null) {
			matriculaPeriodoTurmaDisciplinaOnlineVOs = new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>();
		}
		return matriculaPeriodoTurmaDisciplinaOnlineVOs;
	}

	public void setMatriculaPeriodoTurmaDisciplinaOnlineVOs(List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaOnlineVOs) {
		this.matriculaPeriodoTurmaDisciplinaOnlineVOs = matriculaPeriodoTurmaDisciplinaOnlineVOs;
	}

	public List<SelectItem> getListaSelectItemMatriculaPeriodoTurmaDisciplinaOnlineVOs() {
		if (listaSelectItemMatriculaPeriodoTurmaDisciplinaOnlineVOs == null) {
			listaSelectItemMatriculaPeriodoTurmaDisciplinaOnlineVOs = new ArrayList<SelectItem>();
		}
		return listaSelectItemMatriculaPeriodoTurmaDisciplinaOnlineVOs;
	}

	public void setListaSelectItemMatriculaPeriodoTurmaDisciplinaOnlineVOs(List<SelectItem> listaSelectItemMatriculaPeriodoTurmaDisciplinaOnlineVOs) {
		this.listaSelectItemMatriculaPeriodoTurmaDisciplinaOnlineVOs = listaSelectItemMatriculaPeriodoTurmaDisciplinaOnlineVOs;
	}

	public MatriculaPeriodoTurmaDisciplinaVO getMatriculaPeriodoTurmaDisciplinaVO() {
		if (matriculaPeriodoTurmaDisciplinaVO == null) {
			matriculaPeriodoTurmaDisciplinaVO = new MatriculaPeriodoTurmaDisciplinaVO();
		}
		return matriculaPeriodoTurmaDisciplinaVO;
	}

	public void setMatriculaPeriodoTurmaDisciplinaVO(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO) {
		this.matriculaPeriodoTurmaDisciplinaVO = matriculaPeriodoTurmaDisciplinaVO;
	}
	
	public List<SelectItem> getComboBoxRegraPoliticaSelecaoQuestao() {
		List<SelectItem> comboBoxRegraPoliticaSelecaoQuestao = new ArrayList<SelectItem>();
		comboBoxRegraPoliticaSelecaoQuestao = new ArrayList<SelectItem>();
		comboBoxRegraPoliticaSelecaoQuestao.add(new SelectItem(PoliticaSelecaoQuestaoEnum.QUALQUER_QUESTAO, PoliticaSelecaoQuestaoEnum.QUALQUER_QUESTAO.getValorApresentar()));		
		comboBoxRegraPoliticaSelecaoQuestao.add(new SelectItem(PoliticaSelecaoQuestaoEnum.QUESTOES_TODOS_ASSUNTOS_CONTEUDO, PoliticaSelecaoQuestaoEnum.QUESTOES_TODOS_ASSUNTOS_CONTEUDO.getValorApresentar()));
		comboBoxRegraPoliticaSelecaoQuestao.add(new SelectItem(PoliticaSelecaoQuestaoEnum.QUESTOES_ASSUNTO_ESTUDADOS, PoliticaSelecaoQuestaoEnum.QUESTOES_ASSUNTO_ESTUDADOS.getValorApresentar()));
		return comboBoxRegraPoliticaSelecaoQuestao;
	}
	
	public Boolean getPermiteReativarListaExercicioProfessor() {
		return (getListaExercicioVO().getResponsavelCriacao().getCodigo().equals(getUsuarioLogado().getCodigo()) || loginControle.getPermissaoAcessoMenuVO().getAlterarListaExercicioOutroProfessorProfessor()) && (getListaExercicioVO().getSituacaoListaExercicio().equals(SituacaoListaExercicioEnum.INATIVA) || getListaExercicioVO().getSituacaoListaExercicio().equals(SituacaoListaExercicioEnum.EM_ELABORACAO) && !getListaExercicioVO().isNovoObj());
	}
	
	public List getTipoConsultaCombo() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("codigo", "Código"));
		itens.add(new SelectItem("questao", "Questão"));
		return itens;
	}
	
	public void limparCamposBusca() {
		getControleConsultaQuestao().setListaConsulta(new ArrayList<>());
		setValorConsulta("");
	}

	public String getValorConsulta() {
		if (valorConsulta == null) {
			valorConsulta = "";
		}
		return valorConsulta;
	}

	public void setValorConsulta(String valorConsulta) {
		this.valorConsulta = valorConsulta;
	}

	public String getCampoConsulta() {
		if (campoConsulta == null) {
			campoConsulta = "";
		}
		return campoConsulta;
	}

	public void setCampoConsulta(String campoConsulta) {
		this.campoConsulta = campoConsulta;
	}

	private void verificarPermissaoRecursoRandomizarQuestaoProfessor() {
		try {
			getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("IniciarAtivoRecursoRandomizarQuestoesCadastradasProfessor", getUsuarioLogadoClone());
			getListaExercicioVO().setRandomizarApenasQuestoesCadastradasPeloProfessor(true);
		} catch (Exception e) {
			
		}
	}
	
	private void verificarPermissaoDesabilitarRecursoRandomizarQuestaoProfessor() {
		try {
			getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("DesabilitarAlteracaoRecursoRandomizarQuestoesCadastradasProfessor", getUsuarioLogadoClone());
			setDesabilitarRecursoRandomizacaoQuestaoProfessor(true);
		} catch (Exception e) {
			setDesabilitarRecursoRandomizacaoQuestaoProfessor(false);
		}
	}

	public Boolean getDesabilitarRecursoRandomizacaoQuestaoProfessor() {
		if (desabilitarRecursoRandomizacaoQuestaoProfessor == null) {
			desabilitarRecursoRandomizacaoQuestaoProfessor = false;
		}
		return desabilitarRecursoRandomizacaoQuestaoProfessor;
	}

	public void setDesabilitarRecursoRandomizacaoQuestaoProfessor(Boolean desabilitarRecursoRandomizacaoQuestaoProfessor) {
		this.desabilitarRecursoRandomizacaoQuestaoProfessor = desabilitarRecursoRandomizacaoQuestaoProfessor;
	}
}
