package controle.academico;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import jobs.enumeradores.TipoUsoNotaEnum;
import negocio.comuns.academico.ConfiguracaoAcademicaNotaVO;
import negocio.comuns.academico.ConfiguracaoAcademicoNotaConceitoVO;
import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.ForumInteracaoVO;
import negocio.comuns.academico.ForumPessoaVO;
import negocio.comuns.academico.ForumRegistrarNotaVO;
import negocio.comuns.academico.ForumVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.OpcaoOrdenacaoForumEnum;
import negocio.comuns.academico.enumeradores.OpcaoOrdenacaoForumInteracaoEnum;
import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.academico.enumeradores.SituacaoForumEnum;
import static negocio.comuns.academico.enumeradores.SituacaoMatriculaPeriodoEnum.ATIVA;
import static negocio.comuns.academico.enumeradores.SituacaoMatriculaPeriodoEnum.PRE_MATRICULA;
import negocio.comuns.academico.enumeradores.TipoPeriodoDisponibilidadeForumEnum;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.pesquisa.AreaConhecimentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilReflexao;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

@Controller("ForumControle")
@Scope("viewScope")
@Lazy
public class ForumControle extends SuperControle {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8719624439764914451L;

	private ForumVO forum;
	private ForumInteracaoVO forumInteracao;
	private ForumInteracaoVO forumInteracaoFilho;
	protected List<SelectItem> listaSelectItemDisciplina;
	private List<SelectItem> listaSelectItemDisciplinaForum;
	protected List<SelectItem> listaSelectItemTurma;
	protected List<DisciplinaVO> listaConsultaDisciplina;
	private String campoConsultaDisciplina;
	private String valorConsultaDisciplina;
	private List<TurmaVO> listaConsultaTurma;
	private String campoConsultaTurma;
	private String valorConsultaTurma;
	private List<UnidadeEnsinoCursoVO> listaConsultaCurso;
	private String campoConsultaCurso;
	private String valorConsultaCurso;
	protected List<SelectItem> listaSelectItemAreaConhecimento;
	protected List<SelectItem> listaSelectItemCurso;
	private String matriculaAluno;
	private Integer disciplinaAluno;
	private String filtroTema;
	private OpcaoOrdenacaoForumEnum opcaoOrdenacaoForum;
	private OpcaoOrdenacaoForumInteracaoEnum opcaoOrdenacaoForumInteracao;
	private DataModelo controleConsultaInteracao;
	private List<PessoaVO> listaConsultaPessoa;
	private String campoConsultaPessoa;
	private String valorConsultaPessoa;
	private List<SelectItem> listaSelectItemTurmaInteragiramForum;
	private TurmaVO turmaVigenteNota;
	private String anoVigenteNota;
	private String semestreVigenteNota;
	private ConfiguracaoAcademicoVO configuracaoAcademicoVigenteNota;
	private List<SelectItem> listaSelectItemConfiguracaoAcademico;
	private String variavelTipoNota;
	private List<SelectItem> listaSelectItemTipoInformarNota;
	private List<SelectItem> listaSelectItemNotaConceito;
	private ConfiguracaoAcademicaNotaVO configuracaoNota;
	private HashMap<Integer, ConfiguracaoAcademicoVO> mapaConfiguracoesAcademicos;
	private String cssConfiguracaoAcademicoVigenteNota;
	private DataModelo controleConsultaInteracaoRegistroNota;
	private ForumRegistrarNotaVO forumRegistrarNotaVO;
	private List<SelectItem> listaTipoPeriodoDisponibilizacao;
	private List<SelectItem> listaSelectItemSemestre;
	private Boolean validarFiltrosConsulta;
	private String filterAnoSemestre;
	private List<SelectItem> listaSelectItemAnoSemestre;
	private MatriculaVO matriculaVO;
    private List<SelectItem> listaSelectItemPeriodicidade;
    private PeriodicidadeEnum periodicidade;
	private String ano;
	private String semestre;
	private String fecharModalRegistrarNotaAluno;
	private boolean consultarTodosForumsNaoRespondidos = true;

	public ForumControle() {
		super();
	}

	@PostConstruct
	public void inicializarDados() throws Exception {
		getControleConsultaOtimizado().getListaConsulta().clear();
		getControleConsultaOtimizado().setPage(0);
		getControleConsultaOtimizado().setPaginaAtual(1);
		getControleConsultaOtimizado().setTotalRegistrosEncontrados(0);
		getControleConsultaOtimizado().setLimitePorPagina(10);
		if ((getUsuarioLogado().getVisaoLogar().equalsIgnoreCase("aluno") || getUsuarioLogado().getVisaoLogar().equalsIgnoreCase("pais"))) {
			VisaoAlunoControle visaoAlunoControle = (VisaoAlunoControle) context().getExternalContext().getSessionMap().get("VisaoAlunoControle");
			setMatriculaAluno(visaoAlunoControle.getMatricula().getMatricula());
			getControleConsultaOtimizado().setLimitePorPagina(10);
			getControleConsultaOtimizado().setPaginaAtual(1);
			getControleConsultaOtimizado().setPage(0);
			getMatriculaVO().setMatricula(getMatriculaAluno());
			getMatriculaVO().setNivelMontarDados(NivelMontarDados.NAO_INICIALIZADO);
			getFacadeFactory().getMatriculaFacade().carregarDados(getMatriculaVO(), NivelMontarDados.TODOS, getUsuarioLogado());
			setFilterAnoSemestre(getVisaoAlunoControle().getFiltroAnoSemestreTelaInicial());
			montarListaSelectItemDisciplinaForum();
			
			return;
		} else if ((getUsuarioLogado().getIsApresentarVisaoProfessor())) {
			try {
				getControleConsultaOtimizado().setLimitePorPagina(5);
				inicializarTelaConsultaForumVisaoProfessor();
				montarListaSelectItemAreaConhecimento();
				montarListaSelectItemCursoPorAreaConhecimento();
				montarListaSelectItemTurmaPorCurso();
				montarListaSelectItemTurmaDisciplina();				
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
				e.printStackTrace();
			}
		}
		if ((getUsuarioLogado().getIsApresentarVisaoProfessor() || getUsuarioLogado().getIsApresentarVisaoAdministrativa())) {
			montarListaSelectItemTipoPeriodoDisponibilizacao();
		}
		limparMensagem();
		getForum().setSituacaoForum(SituacaoForumEnum.TODOS);
	}

	public void scrollListenerInteracao(DataScrollEvent DataScrollEvent) {
		getControleConsultaInteracao().setPaginaAtual(DataScrollEvent.getPage());
		getControleConsultaInteracao().setPage(DataScrollEvent.getPage());
		try {
			consultarForumInteracao();
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}

	}

	public void scrollListener(DataScrollEvent DataScrollEvent) {
		setValidarFiltrosConsulta(false);
		getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
		getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
		if ((getUsuarioLogado().getVisaoLogar().equalsIgnoreCase("aluno") || getUsuarioLogado().getVisaoLogar().equalsIgnoreCase("pais"))) {
			consultarForumVisaoAluno();
		} else if ((getUsuarioLogado().getIsApresentarVisaoProfessor())) {
			if (consultarTodosForumsNaoRespondidos) {
				listarTodosForumsNaoRespondidos();
			} else {
				consultarForumVisaoProfessor(getValidarFiltrosConsulta());
			}
		} else {
			consultarForum();
		}
	}

	public void consultarForumVisaoAluno() {
		try {
			getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getForumFacade().consultarForumPorMatricula(getMatriculaAluno(), getDisciplinaAluno(), getFiltroTema(), getOpcaoOrdenacaoForum(), getFilterAnoSemestre(), getUsuarioLogado().getCodigo(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), Uteis.NIVELMONTARDADOS_DADOSBASICOS));
			getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getForumFacade().consultarTotalRegistroForumPorMatricula(getMatriculaAluno(), getDisciplinaAluno(), getFiltroTema(), getFilterAnoSemestre(), getUsuarioLogado().getCodigo()));
			if (getControleConsultaOtimizado().getApresentarListaConsulta()) {
				setMensagemID("msg_selecione_forum", Uteis.ALERTA);
			} else {
				setMensagemID("msg_forum_inexistente", Uteis.ALERTA);
			}

		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void consultarForumVisaoProfessor(Boolean validarFiltrosConsulta) {
		try {
			limparMensagem();
			consultarTodosForumsNaoRespondidos = false;
			getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getForumFacade().consultarForumVisaoProfessor(getForum(), getOpcaoOrdenacaoForum(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), getPeriodicidade(), getAno(), getSemestre(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getForumFacade().consultarTotalRegistroForumVisaoProfessor(getForum(), getPeriodicidade(), getAno(), getSemestre(), getUsuarioLogado()));
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			if (validarFiltrosConsulta) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		} finally {
			setValidarFiltrosConsulta(true);
		}
	}
	  
	  public String consultar() {
		    try {
		    	getControleConsultaOtimizado().setPage(0);
		        getControleConsultaOtimizado().setPaginaAtual(1);
		        return consultarForum();
		    } catch (Exception e) {
		        setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		    }
		    return "";
	    }

	public String consultarForum() {
		try {
			if (!Uteis.isAtributoPreenchido(getForum().getDisciplina().getCodigo())) {
				throw new Exception("Informe a Disciplina Para Consulta.");
			}
            getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getForumFacade().consultarForumPorDisciplina(getForum(), getFiltroTema(), getOpcaoOrdenacaoForum(), getUsuarioLogado().getCodigo(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), Uteis.NIVELMONTARDADOS_DADOSBASICOS));
			getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getForumFacade().consultarTotalRegistroForumPorDisciplina(getForum(), getFiltroTema(), getOpcaoOrdenacaoForum(), getUsuarioLogado().getCodigo()));
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);

		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
        return Uteis.getCaminhoRedirecionamentoNavegacao("forumCons");
	}

	public void inativar() {
		try {
			executarValidacaoSimulacaoVisaoProfessor();
			getFacadeFactory().getForumFacade().inativar(getForum(), getUsuarioLogado().getVisaoLogar().equalsIgnoreCase("professor") ? "AtivarInativarForumProfessor" : "AtivarInativarForum", true, getUsuarioLogado());
			setMensagemID("msg_forum_inativado", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void ativar() {
		try {
			executarValidacaoSimulacaoVisaoProfessor();
			getFacadeFactory().getForumFacade().ativar(getForum(), getUsuarioLogado().getVisaoLogar().equalsIgnoreCase("professor") ? "AtivarInativarForumProfessor" : "AtivarInativarForum", true, getUsuarioLogado());
			setMensagemID("msg_forum_ativado", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void persistir() {
		try {
			executarValidacaoSimulacaoVisaoProfessor();
			if (getForum().getTipoPeriodoDisponibilizacao().equals(TipoPeriodoDisponibilidadeForumEnum.SEMPRE_DISPONIVEL.name())) {
				getForum().setAno("");
				getForum().setSemestre("");
			} else if (getForum().getTipoPeriodoDisponibilizacao().equals(TipoPeriodoDisponibilidadeForumEnum.ANO.name())) {
				getForum().setSemestre("");
			} 
            getFacadeFactory().getForumFacade().persistir(getForum(), getUsuarioLogado().getIsApresentarVisaoProfessor() ? "ForumProfessor" : "Forum", true, getUsuarioLogado());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void gostarForumInteracao() {
		try {
			executarValidacaoSimulacaoVisaoAluno();
			ForumInteracaoVO forumInteracaoVO = new ForumInteracaoVO();
			forumInteracaoVO = (ForumInteracaoVO) context().getExternalContext().getRequestMap().get("forumInteracaoItens");
			getFacadeFactory().getForumInteracaoGostadoFacade().incluir(forumInteracaoVO, getUsuarioLogado());
			forumInteracaoVO.setQtdeGostado(getFacadeFactory().getForumInteracaoFacade().consultarTotalGostado(forumInteracaoVO.getCodigo()));
			setMensagemID("msg_forum_gostado", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void excluirGostarForumInteracao() {
		try {
			executarValidacaoSimulacaoVisaoProfessor();
			executarValidacaoSimulacaoVisaoAluno();
			ForumInteracaoVO forumInteracaoVO = new ForumInteracaoVO();
			forumInteracaoVO = (ForumInteracaoVO) context().getExternalContext().getRequestMap().get("forumInteracaoItens");
			getFacadeFactory().getForumInteracaoGostadoFacade().excluir(forumInteracaoVO, getUsuarioLogado());
			forumInteracaoVO.setQtdeGostado(getFacadeFactory().getForumInteracaoFacade().consultarTotalGostado(forumInteracaoVO.getCodigo()));
			setMensagemID("msg_forum_gostado", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void excluirForumInteracao() {
		try {
			executarValidacaoSimulacaoVisaoProfessor();
			executarValidacaoSimulacaoVisaoAluno();
			getFacadeFactory().getForumInteracaoFacade().excluir((ForumInteracaoVO) context().getExternalContext().getRequestMap().get("forumInteracaoItens"), false, getUsuarioLogado());
			setMensagemID("msg_interacao_excluida", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	  public void adicionarForumInteracao() {	    	
	        try {
	        	executarValidacaoSimulacaoVisaoProfessor();
	        	executarValidacaoSimulacaoVisaoAluno();
	            getForumInteracao().setForum(getForum().getCodigo());
	            getForumInteracao().setForumInteracaoPai(new ForumInteracaoVO());
	            getForumInteracao().setUsuarioInteracao(getUsuarioLogadoClone());
	            getFacadeFactory().getForumInteracaoFacade().persistir(getForumInteracao(), false, getUsuarioLogado());
	            consultarForumInteracao();
	            setForumInteracao(new ForumInteracaoVO());
	            setMensagemID("msg_foruminteracao_adicionado", Uteis.SUCESSO);
	        } catch (ConsistirException e) {
	            setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
	        } catch (Exception e) {
	            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);	            
	        }
	    }
	    
	    public void responderComentarioForumInteracao() {
	        try {
	        	ForumInteracaoVO obj = (ForumInteracaoVO) context().getExternalContext().getRequestMap().get("forumInteracaoItens");
	            setForumInteracaoFilho(new ForumInteracaoVO());
	            getForumInteracaoFilho().setForum(getForum().getCodigo());
	            getForumInteracaoFilho().setForumInteracaoPai(obj);
	            getForumInteracaoFilho().setUsuarioInteracao(getUsuarioLogadoClone());
	            setMensagemID("msg_foruminteracao_adicionado", Uteis.SUCESSO);
	        } catch (Exception e) {
	            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
	        }
	    }
	    
	    public void persistirRespostaForumInteracao() {
	        try {
	        	executarValidacaoSimulacaoVisaoProfessor();
	        	executarValidacaoSimulacaoVisaoAluno();
	            getFacadeFactory().getForumInteracaoFacade().persistir(getForumInteracaoFilho(), false, getUsuarioLogado());
	            consultarForumInteracao();
	            setForumInteracaoFilho(new ForumInteracaoVO());
	            setMensagemID("msg_foruminteracao_adicionado", Uteis.SUCESSO);
	        } catch (ConsistirException e) {
	            setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
	        } catch (Exception e) {
	            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
	        }
	    }

	public void inicializarDadosForumInteracao() {
		try {
			getControleConsultaInteracao().setPage(0);
			getControleConsultaInteracao().setPaginaAtual(1);
			consultarForumInteracao();
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public String editar() {
		try {
			ForumVO obj   = (ForumVO) context().getExternalContext().getRequestMap().get("forumItens");
            obj = getFacadeFactory().getForumFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS);
            setForum(obj);
			setForumInteracao(null);
			getControleConsultaInteracao().setPage(0);
			getControleConsultaInteracao().setPaginaAtual(1);			
			consultarForumInteracao();
			executarValidacaoSimulacaoVisaoProfessor();
			getFacadeFactory().getForumAcessoFacade().incluir(getForum(), getUsuarioLogado());
			if ((getUsuarioLogado().getVisaoLogar().equalsIgnoreCase("aluno") || getUsuarioLogado().getVisaoLogar().equalsIgnoreCase("pais"))) {
				VisaoAlunoControle visaoAlunoControle = (VisaoAlunoControle) context().getExternalContext().getSessionMap().get("VisaoAlunoControle");
				visaoAlunoControle.setQtdeAtualizacaoForum(null);
				limparMensagem();
				setMensagemID("msg_ForumInteracao_envieUmaNovaInteracao", Uteis.ALERTA);
				return Uteis.getCaminhoRedirecionamentoNavegacao("forumAlunoForm.xhtml");				
			}else if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
				try {
					VisaoProfessorControle visaoProfessorControle = (VisaoProfessorControle) context().getExternalContext().getSessionMap().get("VisaoProfessorControle");
					visaoProfessorControle.setQtdeAtualizacaoForum(null);
					limparMensagem();
					return Uteis.getCaminhoRedirecionamentoNavegacao("forumProfessorForm.xhtml");
				} catch (Exception e) {
					setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
					e.printStackTrace();
				}
			}
			setMensagemID("msg_entre_dados", Uteis.ALERTA);
			return Uteis.getCaminhoRedirecionamentoNavegacao("forumForm");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return Uteis.getCaminhoRedirecionamentoNavegacao("forumCons");
		}
	}

	public void consultarForumInteracao() throws Exception {
		getControleConsultaInteracao().setLimitePorPagina(10);
		if (Uteis.isAtributoPreenchido(getUsuarioLogado().getTipoUsuario()) && getUsuarioLogado().getTipoUsuario().equals("AL")) {
			getControleConsultaInteracao().setListaConsulta(getFacadeFactory().getForumInteracaoFacade().consultarPorForumPorCodigoUsuarioLogado(getForum().getCodigo(), getOpcaoOrdenacaoForumInteracao(), getControleConsultaInteracao().getLimitePorPagina(), getControleConsultaInteracao().getOffset(), false, getUsuarioLogado()));
		}else {
			getControleConsultaInteracao().setListaConsulta(getFacadeFactory().getForumInteracaoFacade().consultarPorForum(getForum().getCodigo(), getOpcaoOrdenacaoForumInteracao(), getControleConsultaInteracao().getLimitePorPagina(), getControleConsultaInteracao().getOffset(), false, getUsuarioLogado()));
		}
		getControleConsultaInteracao().setTotalRegistrosEncontrados(getFacadeFactory().getForumInteracaoFacade().consultarTotalRegistroPorForum(getForum().getCodigo()));
	}

	 public String novo() {
	        try {
	            setForum(new ForumVO());
	            setControleConsultaInteracao(new DataModelo());
	            getControleConsultaInteracao().setPage(0);
	            getControleConsultaInteracao().setPaginaAtual(1);
	            getForum().setResponsavelCriacao(getUsuarioLogadoClone());
	            getForum().setTipoPeriodoDisponibilizacao(TipoPeriodoDisponibilidadeForumEnum.SEMPRE_DISPONIVEL.name());
				setAno("");
				setSemestre("");
	            montarListaSelectItemAreaConhecimento();
	            montarListaSelectItemCursoPorAreaConhecimento();
	            montarListaSelectItemTurmaPorCurso();
	            montarListaSelectItemTurmaDisciplina();
	            montarListaSelectItemTipoPeriodoDisponibilizacao();
	            setMensagemID("msg_entre_dados", Uteis.ALERTA);
	            
	            if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
					return Uteis.getCaminhoRedirecionamentoNavegacao("forumProfessorForm.xhtml");
				}
	            return Uteis.getCaminhoRedirecionamentoNavegacao("forumForm");
	        } catch (Exception e) {
	            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
	            return Uteis.getCaminhoRedirecionamentoNavegacao("forumCons");
	            
	        }
	    }

	public void consultarTurmaPorIdentificadorTurma() {
		try {
			if (getForum().getTurma().getIdentificadorTurma() != null && !getForum().getTurma().getIdentificadorTurma().trim().equals("")) {
				getForum().setTurma(getFacadeFactory().getTurmaFacade().consultarTurmaPorIdentificadorTurma(getForum().getTurma().getIdentificadorTurma(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
				montarListaSelectItemTurmaDisciplina();
			} else {
				getForum().setTurma(new TurmaVO());
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			getForum().setTurma(new TurmaVO());
			setMensagemID("msg_erro_dadosnaoencontrados");
		}
	}

	public void selecionarTurma() {
		try {
			TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
			getForum().setTurma(obj);
			montarListaSelectItemTurmaDisciplina();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	 public List<SelectItem> getListaSelectItemTurma() {
	        if (listaSelectItemTurma == null) {
	            listaSelectItemTurma = new ArrayList<SelectItem>(0);           
	        }
	        return listaSelectItemTurma;
	}

	 public List<TurmaVO> consultarTurmaPorProfessor() {
	    	try {
				return getFacadeFactory().getTurmaFacade().consultarTurmaPorProfessorAnoSemestreTurmaAnteriorCursoNivelDadosCombobox(getUsuarioLogado().getPessoa().getCodigo(), getSemestre(), getAno(), true, "AT", getUnidadeEnsinoLogado().getCodigo(), getForum().getCursoVO().getCodigo(), getUsuarioLogado().getIsApresentarVisaoProfessor(), null, null, true, getPeriodicidade() , true, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
	    	return null;
	    }

	public void setListaSelectItemTurma(List<SelectItem> listaSelectItemTurma) {
		this.listaSelectItemTurma = listaSelectItemTurma;
	}
	
	public void montarListaSelectItemTurmaDisciplina() {
		try {
			if (!Uteis.isAtributoPreenchido(getAno()) && (PeriodicidadeEnum.SEMESTRAL.equals(getPeriodicidade()) || PeriodicidadeEnum.ANUAL.equals(getPeriodicidade()))) {
				setAno(Uteis.getAnoDataAtual());
			}
			if (!Uteis.isAtributoPreenchido(getSemestre()) && PeriodicidadeEnum.SEMESTRAL.equals(getPeriodicidade())) {
				setSemestre(Uteis.getSemestreAtual());
			}
			montarListaSelectItemTurmaDisciplina(!Uteis.isAtributoPreenchido(getPeriodicidade()) || getPeriodicidade().equals(PeriodicidadeEnum.INTEGRAL) ? "" : getAno(), !Uteis.isAtributoPreenchido(getPeriodicidade()) || (getPeriodicidade().equals(PeriodicidadeEnum.INTEGRAL) || getPeriodicidade().equals(PeriodicidadeEnum.ANUAL)) ? "" : getSemestre());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void montarListaSelectItemTurmaDisciplina(String ano, String semestre) throws Exception {
		montarListaSelectItemTurmaPorCurso();
		getForum().setDisciplina(new DisciplinaVO());
		List<DisciplinaVO> listaConsultas = null;
		getListaSelectItemDisciplina().clear();
		if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
			listaConsultas = getFacadeFactory().getDisciplinaFacade().consultarPorCodigoAreaConhecimentoCursoTurmaVisaoCoordenador(getForum().getAreaConhecimentoVO().getCodigo(), getUsuarioLogado().getPessoa().getCodigo(),
							getForum().getTurma().getCodigo(), getForum().getCursoVO().getCodigo(),	getUnidadeEnsinoLogado().getCodigo(), ano, semestre, false,	Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		} else {
			listaConsultas = getFacadeFactory().getDisciplinaFacade().consultarPorCodigoAreaConhecimentoCursoTurmaVisaoCoordenador(getForum().getAreaConhecimentoVO().getCodigo(), null, getForum().getTurma().getCodigo(),
							getForum().getCursoVO().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), null, null, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		}
		getListaSelectItemDisciplina().add(new SelectItem(0, ""));
		for (DisciplinaVO item : listaConsultas) {
			getListaSelectItemDisciplina().add(new SelectItem(item.getCodigo(), item.getCodigo().toString() + " - " + item.getNome()));
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

	public void consultarDisciplina() {
		try {
			List<DisciplinaVO> objs = new ArrayList<DisciplinaVO>(0);
			if (getValorConsultaDisciplina().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			if (getCampoConsultaDisciplina().equals("codigo")) {
				if (!Uteis.getIsValorNumerico(getValorConsultaDisciplina())) {
					throw new Exception("Informe apenas valores numéricos.");
				}
				DisciplinaVO disciplina = getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(new Integer(getValorConsultaDisciplina()), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				if (!disciplina.equals(new DisciplinaVO()) || disciplina != null) {
					objs.add(disciplina);
				}
			}
			if (getCampoConsultaDisciplina().equals("nome")) {
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorNomeDisciplinaCurso(getValorConsultaDisciplina(), getForum().getCursoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultaDisciplina(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaDisciplina(new ArrayList<DisciplinaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	 public void limparTurma() {
	        getForum().setTurma(new TurmaVO());
	        getForum().setDisciplina(new DisciplinaVO());
	    }

	public void limparDisciplina() {
		getControleConsultaOtimizado().getListaConsulta().clear();
		getControleConsultaOtimizado().setPage(0);
		getControleConsultaOtimizado().setPaginaAtual(1);
		getControleConsultaOtimizado().setTotalRegistrosEncontrados(0);
		getControleConsultaOtimizado().setLimitePorPagina(10);
		getForum().setDisciplina(null);
		setMensagemID("msg_informe_disciplina", Uteis.ALERTA);
	}

	public void selecionarDisciplina() throws Exception {
		try {
			setMensagemDetalhada("");
			DisciplinaVO disciplina = (DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplinaItens");
			getForum().setDisciplina(disciplina);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarDisciplinaConsulta() throws Exception {
		try {
			setMensagemDetalhada("");
			DisciplinaVO disciplina = (DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplinaLista");
			getForum().setDisciplina(disciplina);
			getControleConsultaOtimizado().setPage(0);
			getControleConsultaOtimizado().setPaginaAtual(1);
			consultarForum();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	  public void limparCampoRestricaoPublicoAlvo(){
	    	
	    	if(Uteis.isAtributoPreenchido(getForum().getPublicoAlvoForumEnum()) && getForum().getPublicoAlvoForumEnum().isPublicoAluno()){
	    		getForum().setRestricaoPublicoAlvoForumEnum(null);
	    	}
	    	getForum().setAreaConhecimentoVO(new AreaConhecimentoVO());
	    	getForum().setCursoVO(new CursoVO());
	    	getForum().setTurma(new TurmaVO());
	    	getForum().setDisciplina(new DisciplinaVO());
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

	public String inicializarConsultar() throws Exception {
		setForum(new ForumVO());
		inicializarDados();
		limparMensagem();
		if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
			return Uteis.getCaminhoRedirecionamentoNavegacao("forumProfessorCons.xhtml");
		}
		if (getUsuarioLogado().getIsApresentarVisaoAlunoOuPais()) {
			consultarForumVisaoAluno();
			return Uteis.getCaminhoRedirecionamentoNavegacao("forumAlunoCons.xhtml");
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("forumCons");
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

	public ForumVO getForum() {
		if (forum == null) {
			forum = new ForumVO();
		}
		return forum;
	}

	public void setForum(ForumVO forum) {
		this.forum = forum;
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

	public ForumInteracaoVO getForumInteracao() {
		if (forumInteracao == null) {
			forumInteracao = new ForumInteracaoVO();
		}
		return forumInteracao;
	}

	public void setForumInteracao(ForumInteracaoVO forumInteracao) {
		this.forumInteracao = forumInteracao;
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

	public String getFiltroTema() {
		if (filtroTema == null) {
			filtroTema = "";
		}
		return filtroTema;
	}

	public void setFiltroTema(String filtroTema) {
		this.filtroTema = filtroTema;
	}

	public OpcaoOrdenacaoForumEnum getOpcaoOrdenacaoForum() {
		if (opcaoOrdenacaoForum == null) {
			opcaoOrdenacaoForum = OpcaoOrdenacaoForumEnum.ULTIMA_ATUALIZACAO;
		}
		return opcaoOrdenacaoForum;
	}

	public void setOpcaoOrdenacaoForum(OpcaoOrdenacaoForumEnum opcaoOrdenacaoForum) {
		this.opcaoOrdenacaoForum = opcaoOrdenacaoForum;
	}

	private List<SelectItem> listaSelectItemOpcaoOrdenacaoForum;

	public List<SelectItem> getListaSelectItemOpcaoOrdenacaoForum() {
		if (listaSelectItemOpcaoOrdenacaoForum == null) {
			listaSelectItemOpcaoOrdenacaoForum = new ArrayList<SelectItem>(0);
			for (OpcaoOrdenacaoForumEnum opcao : OpcaoOrdenacaoForumEnum.values()) {
				listaSelectItemOpcaoOrdenacaoForum.add(new SelectItem(opcao, opcao.getValorApresentar()));
			}
		}
		return listaSelectItemOpcaoOrdenacaoForum;
	}

	private List<SelectItem> listaSelectItemOpcaoOrdenacaoForumInteracao;

	public List<SelectItem> getListaSelectItemOpcaoOrdenacaoForumInteracao() {
		if (listaSelectItemOpcaoOrdenacaoForumInteracao == null) {
			listaSelectItemOpcaoOrdenacaoForumInteracao = new ArrayList<SelectItem>(0);
			for (OpcaoOrdenacaoForumInteracaoEnum opcao : OpcaoOrdenacaoForumInteracaoEnum.values()) {
				listaSelectItemOpcaoOrdenacaoForumInteracao.add(new SelectItem(opcao, opcao.getValorApresentar()));
			}
		}
		return listaSelectItemOpcaoOrdenacaoForumInteracao;
	}

	public String getCaminhoBaseFoto() {
		try {

			return getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo();
		} catch (Exception e) {
			return "/resources/imagens/visao/foto_usuario.png";
		}
	}

	public DataModelo getControleConsultaInteracao() {
		if (controleConsultaInteracao == null) {
			controleConsultaInteracao = new DataModelo();
		}
		return controleConsultaInteracao;
	}

	public void setControleConsultaInteracao(DataModelo controleConsultaInteracao) {
		this.controleConsultaInteracao = controleConsultaInteracao;
	}

	public OpcaoOrdenacaoForumInteracaoEnum getOpcaoOrdenacaoForumInteracao() {
		if (opcaoOrdenacaoForumInteracao == null) {
			opcaoOrdenacaoForumInteracao = OpcaoOrdenacaoForumInteracaoEnum.DATA_INTERACAO;
		}
		return opcaoOrdenacaoForumInteracao;
	}

	public void setOpcaoOrdenacaoForumInteracao(OpcaoOrdenacaoForumInteracaoEnum opcaoOrdenacaoForumInteracao) {
		this.opcaoOrdenacaoForumInteracao = opcaoOrdenacaoForumInteracao;
	}
	
	public ForumInteracaoVO getForumInteracaoFilho() {
		if (forumInteracaoFilho == null) {
			forumInteracaoFilho = new ForumInteracaoVO();
        }
		return forumInteracaoFilho;
	}

	public void setForumInteracaoFilho(ForumInteracaoVO forumInteracaoFilho) {
		this.forumInteracaoFilho = forumInteracaoFilho;
	}
	
	
	public void montarListaSelectItemDisciplinaForum() throws Exception {
		getListaSelectItemDisciplinaForum().clear();
		List<DisciplinaVO> listaConsultas = null;
		if (getUsuarioLogado().getIsApresentarVisaoAlunoOuPais()) {
			List<MatriculaPeriodoTurmaDisciplinaVO>mptdVOs = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarDisciplinaDoAlunoPorMatricula(getMatriculaVO().getMatricula(), getFilterAnoSemestre(), getLoginControle().getPermissaoAcessoMenuVO(), null, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			getListaSelectItemDisciplinaForum().add(new SelectItem(0, ""));
			for(MatriculaPeriodoTurmaDisciplinaVO mptd : mptdVOs) {
				getListaSelectItemDisciplinaForum().add(new SelectItem(mptd.getDisciplina().getCodigo(), mptd.getDisciplina().getNome()));
			}
			if(context().getExternalContext().getSessionMap().get("disciplinaSelecionada") != null) {
				MatriculaPeriodoTurmaDisciplinaVO mptd = ((MatriculaPeriodoTurmaDisciplinaVO)context().getExternalContext().getSessionMap().remove("disciplinaSelecionada"));
				setDisciplinaAluno(mptd.getDisciplina().getCodigo());	
			}else {
				setDisciplinaAluno(getVisaoAlunoControle().getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina().getCodigo());
			}
			consultarForumVisaoAluno();
			return;
		}else	if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
			listaConsultas = getFacadeFactory().getDisciplinaFacade().consultarPorCodigoAreaConhecimentoCursoTurmaVisaoCoordenador(getForum().getAreaConhecimentoVO().getCodigo(), getUsuarioLogado().getPessoa().getCodigo(), getForum().getTurma().getCodigo(), getForum().getCursoVO().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), getAno(), getSemestre(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		} else {
			listaConsultas = getFacadeFactory().getDisciplinaFacade().consultarPorCodigoAreaConhecimentoCursoTurmaVisaoCoordenador(getForum().getAreaConhecimentoVO().getCodigo(), null, getForum().getTurma().getCodigo(), getForum().getCursoVO().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), null, null, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		}
		getListaSelectItemDisciplinaForum().add(new SelectItem(0, ""));
		for (DisciplinaVO item : listaConsultas) {
			getListaSelectItemDisciplinaForum().add(new SelectItem(item.getCodigo(), item.getCodigo().toString() + " "+ item.getNome()));			
		}
	}
	
	public List<SelectItem> getListaSelectItemDisciplinaForum() {
		if(listaSelectItemDisciplinaForum == null){
			listaSelectItemDisciplinaForum = new ArrayList<SelectItem>();
		}
		return listaSelectItemDisciplinaForum;
	}

	public void setListaSelectItemDisciplinaForum(List<SelectItem> listaSelectItemDisciplinaForum) {
		this.listaSelectItemDisciplinaForum = listaSelectItemDisciplinaForum;
	}
	
	/**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>AreaConhecimento</code>.
	 */
	public void montarListaSelectItemAreaConhecimento(String prm) throws Exception {
		SelectItemOrdemValor ordenador = null;
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarAreaConhecimentoPorNome(prm);
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				AreaConhecimentoVO obj = (AreaConhecimentoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
			}
			ordenador = new SelectItemOrdemValor();
			Collections.sort((List) objs, ordenador);
			setListaSelectItemAreaConhecimento(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			ordenador = null;
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>AreaConhecimento</code>. Buscando todos os objetos correspondentes
	 * a entidade <code>AreaConhecimento</code>. Esta rotina não recebe
	 * parâmetros para filtragem de dados, isto é importante para a
	 * inicialização dos dados da tela para o acionamento por meio requisições
	 * Ajax.
	 */
	public void montarListaSelectItemAreaConhecimento() {
		try {
			montarListaSelectItemAreaConhecimento("");
		} catch (Exception e) {
			//System.out.println("MENSAGEM => " + e.getMessage());
			;
		}
	}

	/**
	 * Método responsável por consultar dados da entidade
	 * <code><code> e montar o atributo <code>nome</code> Este atributo é uma
	 * lista (<code>List</code>) utilizada para definir os valores a serem
	 * apresentados no ComboBox correspondente
	 */
	public List consultarAreaConhecimentoPorNome(String nomePrm) throws Exception {
		List lista = getFacadeFactory().getAreaConhecimentoFacade().consultarPorNome(nomePrm, false, getUsuarioLogado());
		return lista;
	}
	
	public List getListaSelectItemAreaConhecimento() {
		if (listaSelectItemAreaConhecimento == null) {
			listaSelectItemAreaConhecimento = new ArrayList<SelectItem>(0);
		}
		return (listaSelectItemAreaConhecimento);
	}

	public void setListaSelectItemAreaConhecimento(List listaSelectItemAreaConhecimento) {
		this.listaSelectItemAreaConhecimento = listaSelectItemAreaConhecimento;
	}

	public List<UnidadeEnsinoCursoVO> getListaConsultaCurso() {
		if(listaConsultaCurso == null){
			listaConsultaCurso = new ArrayList<UnidadeEnsinoCursoVO>();
		}
		return listaConsultaCurso;
	}

	public void setListaConsultaCurso(List<UnidadeEnsinoCursoVO> listaConsultaCurso) {
		this.listaConsultaCurso = listaConsultaCurso;
	}

	public String getCampoConsultaCurso() {
		if(campoConsultaCurso == null){
			campoConsultaCurso = "";
		}
		return campoConsultaCurso;
	}

	public void setCampoConsultaCurso(String campoConsultaCurso) {
		this.campoConsultaCurso = campoConsultaCurso;
	}

	public String getValorConsultaCurso() {
		if(valorConsultaCurso == null){
			valorConsultaCurso = "";
		}
		return valorConsultaCurso;
	}

	public void setValorConsultaCurso(String valorConsultaCurso) {
		this.valorConsultaCurso = valorConsultaCurso;
	}
	
	public List<SelectItem> getTipoConsultaComboCurso() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		return itens;
	}
	
	public void carregarDadosCurso(){
		try {
			getForum().setCursoVO(new CursoVO());
			getForum().setTurma(new TurmaVO());
			getForum().setDisciplina(new DisciplinaVO());
			montarListaSelectItemCursoPorAreaConhecimento();
			montarListaSelectItemTurmaPorCurso();
			montarListaSelectItemTurmaDisciplina();
			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	
	public void consultarCurso() {
		try {
			List<UnidadeEnsinoCursoVO> objs = new ArrayList<UnidadeEnsinoCursoVO>(0);
			if (getCampoConsultaCurso().equals("nome")) {
				objs = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorNomeCursoUnidadeEnsino(getValorConsultaCurso(),null, false, "", false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			setListaConsultaCurso(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList<UnidadeEnsinoCursoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarCurso() {
		try {
			UnidadeEnsinoCursoVO unidadeEnsinoCurso = (UnidadeEnsinoCursoVO) context().getExternalContext().getRequestMap().get("unidadeensinocurso");
			getForum().setCursoVO(unidadeEnsinoCurso.getCurso());
			carregarDadosTurma();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void limparCurso() {
        getForum().setCursoVO(null);
        getForum().setTurma(new TurmaVO());
        getForum().setDisciplina(new DisciplinaVO());
        setMensagemID("msg_informe_disciplina", Uteis.ALERTA);
    }

	public List<SelectItem> getListaSelectItemCurso() {
		if(listaSelectItemCurso == null){
			listaSelectItemCurso = new ArrayList<SelectItem>();
		}
		return listaSelectItemCurso;
	}

	public void setListaSelectItemCurso(List<SelectItem> listaSelectItemCurso) {
		this.listaSelectItemCurso = listaSelectItemCurso;
	}
	
	public void montarListaSelectItemCursoPorAreaConhecimento() throws Exception {
		getListaSelectItemCurso().clear();
		List<CursoVO> listaConsultas = new ArrayList<CursoVO>();
		if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
            listaConsultas = getFacadeFactory().getCursoFacade().consultarCursoPorProfessorNivelDadosCombobox(getUsuarioLogado().getPessoa().getCodigo(), null, null, "AT", getForum().getAreaConhecimentoVO().getCodigo(), 0, true, null, null, getPeriodicidade(), getUsuarioLogado());
        } else {            
        	listaConsultas = getFacadeFactory().getCursoFacade().consultaRapidaPorCodigoAreaConhecimento(getForum().getAreaConhecimentoVO().getCodigo(), getPeriodicidade(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());        
        }
		getListaSelectItemCurso().addAll(UtilSelectItem.getListaSelectItem(listaConsultas, "codigo", "nome", true));
	}	
	
	public void montarListaSelectItemTurmaPorCurso() throws Exception {
		List<TurmaVO> turmas = null;
		List<Integer> mapAuxiliarSelectItem = new ArrayList();
		try { 
			getListaSelectItemTurma().clear();
			if(getUsuarioLogado().getIsApresentarVisaoProfessor()){
				turmas = consultarTurmaPorProfessor();	
			}else {
				turmas = getFacadeFactory().getTurmaFacade().consultaRapidaPorCodigoTurmaCursoUnidadeEnsino(null, getForum().getCursoVO().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado());
			}
			getListaSelectItemTurma().add(new SelectItem(0, ""));
			for (TurmaVO turmaVO : turmas) {
				if(!mapAuxiliarSelectItem.contains(turmaVO.getCodigo())){
					getListaSelectItemTurma().add(new SelectItem(turmaVO.getCodigo(), turmaVO.aplicarRegraNomeCursoApresentarCombobox()));
					mapAuxiliarSelectItem.add(turmaVO.getCodigo());
				}	
			}
		} catch (Exception e) {
			getListaSelectItemTurma().clear();
		}finally{
			mapAuxiliarSelectItem = null;
			turmas = null;
		}
	}	
	
	public void carregarDadosTurma(){
		try {
			getForum().setTurma(new TurmaVO());
			getForum().setDisciplina(new DisciplinaVO());
			montarListaSelectItemTurmaPorCurso();
			montarListaSelectItemTurmaDisciplina();		
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void consultarAlunoParaRegistrarNota() {
		try {
			
			if (!Uteis.isAtributoPreenchido(getTurmaVigenteNota())) {
				throw new Exception(getMensagemInternalizacao("msg_forum_turma"));
			}
			if (getTurmaVigenteNota().isApresentarAnoSemestre() && !Uteis.isAtributoPreenchido(getAnoVigenteNota())) {
				throw new Exception(getMensagemInternalizacao("msg_forum_ano"));
			}
			if (getTurmaVigenteNota().getSemestral() && !Uteis.isAtributoPreenchido(getSemestreVigenteNota())) {
				throw new Exception(getMensagemInternalizacao("msg_forum_semestre"));
			}
			setConfiguracaoAcademicoVigenteNota(new ConfiguracaoAcademicoVO());
			setConfiguracaoNota(new ConfiguracaoAcademicaNotaVO());
			getForum().getForumRegistrarNotaVOs().clear();
			getForum().getForumRegistrarNotaVOs().addAll(getFacadeFactory().getForumRegistrarNotaFacade().consultarForumRegistrarNotaRapidaPorTurmaAnoSemestre(getForum(), getTurmaVigenteNota(), getAnoVigenteNota(),getSemestreVigenteNota(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			// validar alunos que ainda nao tiveram sua nota gravada.
			getForum().getForumRegistrarNotaVOs().addAll(getFacadeFactory().getForumRegistrarNotaFacade().consultarPessoaInteracaoForumRapidaPorTurmaAnoSemestre(getForum(), getTurmaVigenteNota(), getAnoVigenteNota(),getSemestreVigenteNota(), getUsuarioLogado()));
			montarListaDeConfiguracaoAcademico();
			if(!Uteis.isAtributoPreenchido(getForum().getForumRegistrarNotaVOs())){
				throw new Exception(getMensagemInternalizacao("msg_dados_nenhum_registro"));
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	public void montarListaDeConfiguracaoAcademico() {
		List<Integer> listaCod = new ArrayList<Integer>();
		try {
			getListaSelectItemTipoInformarNota().clear();
			getListaSelectItemConfiguracaoAcademico().clear();
			getListaSelectItemConfiguracaoAcademico().add(new SelectItem(0, ""));
			for (ForumRegistrarNotaVO obj : getForum().getForumRegistrarNotaVOs()) {
				if(!listaCod.contains(obj.getHistoricoVO().getConfiguracaoAcademico().getCodigo())){
					getListaSelectItemConfiguracaoAcademico().add(new SelectItem(obj.getHistoricoVO().getConfiguracaoAcademico().getCodigo(), obj.getHistoricoVO().getConfiguracaoAcademico().getNome()));
					listaCod.add(obj.getHistoricoVO().getConfiguracaoAcademico().getCodigo());
				}
			}
			if(listaCod.size() == 1) {
				getConfiguracaoAcademicoVigenteNota().setCodigo(listaCod.get(0));				
				setCssConfiguracaoAcademicoVigenteNota("camposSomenteLeitura");
				montarListaDeNotasDaConfiguracaoAcademico();
			}else{
				setCssConfiguracaoAcademicoVigenteNota("camposObrigatorios");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void montarListaDeNotasDaConfiguracaoAcademico() {
		try {
			getListaSelectItemTipoInformarNota().clear();
			if(Uteis.isAtributoPreenchido(getConfiguracaoAcademicoVigenteNota().getCodigo())){
				if(getMapaConfiguracoesAcademicos().containsKey(getConfiguracaoAcademicoVigenteNota().getCodigo())){
					setConfiguracaoAcademicoVigenteNota(getMapaConfiguracoesAcademicos().get(getConfiguracaoAcademicoVigenteNota().getCodigo()));					
				}else{
					setConfiguracaoAcademicoVigenteNota(getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorChavePrimaria(getConfiguracaoAcademicoVigenteNota().getCodigo(), getUsuarioLogado()));
					getMapaConfiguracoesAcademicos().put(getConfiguracaoAcademicoVigenteNota().getCodigo(), getConfiguracaoAcademicoVigenteNota());
				}
				getListaSelectItemTipoInformarNota().addAll(getFacadeFactory().getConfiguracaoAcademicoFacade().montarListaSelectItemOpcoesDeNotas(getConfiguracaoAcademicoVigenteNota(), true, TipoUsoNotaEnum.FORUM));
				getListaSelectItemTipoInformarNota().addAll(getFacadeFactory().getConfiguracaoAcademicoFacade().consultarVariavelTituloJaRegistradasForumRegistroNotaPorForumTurmaAnoSemestre(getListaSelectItemTipoInformarNota(), getForum(), getTurmaVigenteNota(), getAnoVigenteNota(),getSemestreVigenteNota(), getUsuarioLogado()));
				if(getDesablitarConfiguracaoAcademicoVigenteNota()){
					for (ForumRegistrarNotaVO obj : getForum().getForumRegistrarNotaVOs()) {
						if(Uteis.isAtributoPreenchido(obj.getVariavelTipoNota())){
							setVariavelTipoNota(obj.getVariavelTipoNota());
							setConfiguracaoNota(getConfiguracaoAcademicoVigenteNota().getMapaConfigNotas().get(getVariavelTipoNota()));
							setListaSelectItemNotaConceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoNota().getConfiguracaoAcademicoNotaConceitoVOs()));	
						}
						break;
					}
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void montarListaDeTurmaAlunoQueInteragiramForum() {
		try {
			setConfiguracaoAcademicoVigenteNota(new ConfiguracaoAcademicoVO());
			setConfiguracaoNota(new ConfiguracaoAcademicaNotaVO());
			getForum().getForumRegistrarNotaVOs().clear();
			getListaSelectItemTurmaInteragiramForum().clear();
			List<TurmaVO> lista = getFacadeFactory().getTurmaFacade().consultaRapidaNivelComboboxPorForum(getForum(), getUsuarioLogado());
			getListaSelectItemTurmaInteragiramForum().addAll(UtilSelectItem.getListaSelectItem(lista, "codigo", "identificadorTurma", true));
			setTurmaVigenteNota(new TurmaVO());
			setAnoVigenteNota("");
			setSemestreVigenteNota("");
			setMensagemID("msg_entre_dados", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void limparCampoListaAlunoRegistrarNota(){    	
		getForum().getForumRegistrarNotaVOs().clear();
    }
	
	public void consultarTurmaAlunoQueInteragiramForum() {
		TurmaVO obj = null;
		try {
        	if(Uteis.isAtributoPreenchido(getTurmaVigenteNota().getCodigo())){
        		obj = getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(getTurmaVigenteNota().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());	
        	}
        	if(Uteis.isAtributoPreenchido(obj)){
        		setTurmaVigenteNota(obj);	
        	}else{
        		setTurmaVigenteNota(new TurmaVO());
        	}	
        	getForum().getForumRegistrarNotaVOs().clear();
        	setConfiguracaoAcademicoVigenteNota(new ConfiguracaoAcademicoVO());
			setConfiguracaoNota(new ConfiguracaoAcademicaNotaVO());
			setAnoVigenteNota("");
			setSemestreVigenteNota("");
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
        	setTurmaVigenteNota(new TurmaVO());
            setMensagemID("msg_erro_dadosnaoencontrados");
        }
    }
	
	public void checarFormatarValoresNota() {
		try {
			validarForumRegistroNota();
			ForumRegistrarNotaVO forumAux = (ForumRegistrarNotaVO) context().getExternalContext().getRequestMap().get("forumRegistrar");
			forumAux.setVariavelTipoNota(getVariavelTipoNota());
			limparMensagem();
			setSucesso(false);
			if (forumAux.getNota() != null) {
				UtilReflexao.invocarMetodo(forumAux.getHistoricoVO(), "setNota" + getVariavelTipoNota(), forumAux.getNota());
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(forumAux.getHistoricoVO(), null, getConfiguracaoAcademicoVigenteNota(), Integer.parseInt(getVariavelTipoNota()));
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void carregarDadosConfiguracaoAcadimicoPeloTipoDaNota(){ 
		try {
			setConfiguracaoNota(getConfiguracaoAcademicoVigenteNota().getMapaConfigNotas().get(getVariavelTipoNota()));
			setListaSelectItemNotaConceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoNota().getConfiguracaoAcademicoNotaConceitoVOs()));
			getFacadeFactory().getForumRegistrarNotaFacade().carregarDadosConfiguracaoAcadimicoPeloTipoDaNota(getForum(), getVariavelTipoNota(), getConfiguracaoAcademicoVigenteNota(), getConfiguracaoNota().getUtilizarNotaPorConceito(), getUsuarioLogado());				
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void carregarDadosConfiguracaoAcadimicoNotaConceito(){
		try {
			ForumRegistrarNotaVO forumAux = (ForumRegistrarNotaVO) context().getExternalContext().getRequestMap().get("forumRegistrar");
			ConfiguracaoAcademicoNotaConceitoVO notaConceito = getFacadeFactory().getConfiguracaoAcademicoNotaConceitoFacade().consultarPorChavePrimaria(forumAux.getNotaConceito().getCodigo());
			forumAux.setNota(notaConceito.getFaixaNota2());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	private List<SelectItem> getListaSelectItemOpcaoNotaConceito(List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNotaConceitoVOs) {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem(0, ""));
		for (ConfiguracaoAcademicoNotaConceitoVO obj : configuracaoAcademicoNotaConceitoVOs) {
			itens.add(new SelectItem(obj.getCodigo(), obj.getConceitoNota()));
		}
		return itens;

	}
	
	public void persistirForumRegistrarNota() {
        try {
        	validarForumRegistroNota();
        	getFacadeFactory().getForumRegistrarNotaFacade().persistirForumRegistrarNota(getForum(), getMapaConfiguracoesAcademicos(), getConfiguracaoNota(), false, getUsuarioLogado());
        	setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
        } catch (Exception e) {
        	setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }
	
	public void persistirRegistroHistorioForumNota() {
        try {
        	setFecharModalRegistrarNotaAluno("");
        	validarForumRegistroNota();
       		getFacadeFactory().getForumRegistrarNotaFacade().alterarForumRegistrarNotaParaRegistrarNotaHistorico(getForum().getForumRegistrarNotaVOs(), 
       				getMapaConfiguracoesAcademicos(), getConfiguracaoNota(), getUsuarioLogado());
        	setFecharModalRegistrarNotaAluno("RichFaces.$('panelRegistrarNotaAluno').hide()");
        	setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
        } catch (Exception e) {
        	setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }
	
	public void consultarForumInteracaoRegistroNota()  {
		try {
			setForumRegistrarNotaVO((ForumRegistrarNotaVO) context().getExternalContext().getRequestMap().get("forumRegistrar"));
			getControleConsultaInteracaoRegistroNota().setPage(0);
			getControleConsultaInteracaoRegistroNota().setPaginaAtual(1);
			realizarConsultarForumInteracaoRegistroNota();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
        
    }
	
	public void scrollListenerInteracaoRegistroNota(DataScrollEvent dataScrollerEvent) {
		getControleConsultaInteracaoRegistroNota().setPaginaAtual(dataScrollerEvent.getPage());
		getControleConsultaInteracaoRegistroNota().setPage(dataScrollerEvent.getPage());
        try {
        	realizarConsultarForumInteracaoRegistroNota();
            limparMensagem();
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }

    }
	
	public void realizarConsultarForumInteracaoRegistroNota()  {
		try {
			getControleConsultaInteracaoRegistroNota().setLimitePorPagina(5);
	        getControleConsultaInteracaoRegistroNota().setListaConsulta(getFacadeFactory().getForumInteracaoFacade().consultarPorForumPorUsuario(getForum().getCodigo(), getForumRegistrarNotaVO().getPessoaVO().getCodigo(), getControleConsultaInteracaoRegistroNota().getLimitePorPagina(), getControleConsultaInteracaoRegistroNota().getOffset(), false, getUsuarioLogado()));
	        getControleConsultaInteracaoRegistroNota().setTotalRegistrosEncontrados(getFacadeFactory().getForumInteracaoFacade().consultarTotalRegistroPorForumPorUsuario(getForum().getCodigo(), getForumRegistrarNotaVO().getPessoaVO().getCodigo()));	
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
        
    }
	
	public void consultarPessoa() {
		try {
			super.consultar();
			List objs = new ArrayList(0);
			if (getCampoConsultaPessoa().equals("nome")) {
				if (getValorConsultaPessoa().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getPessoaFacade().consultarPorNome(getValorConsultaPessoa(), TipoPessoa.PROFESSOR.getValor(), false, Uteis.NIVELMONTARDADOS_DADOSLOGIN, getUsuarioLogado());
			}
			if (getCampoConsultaPessoa().equals("cpf")) {
				if (getValorConsultaPessoa().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getPessoaFacade().consultarPorCPF(getValorConsultaPessoa(), TipoPessoa.PROFESSOR.getValor(), false, Uteis.NIVELMONTARDADOS_DADOSLOGIN, getUsuarioLogado());
			}
			setListaConsultaPessoa(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void removerForumPessoa() {
		ForumPessoaVO forumPessoa = null;
		try {
			forumPessoa = (ForumPessoaVO) context().getExternalContext().getRequestMap().get("forumPessoa1");
			getFacadeFactory().getForumFacade().removerForumPessoa(getForum(), forumPessoa);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}finally{
			forumPessoa = null;
		}
	}
	
	public void selecionarPessoa() {
		ForumPessoaVO forumPessoa = new ForumPessoaVO();
		try {
			forumPessoa.setPessoaVO((PessoaVO) context().getExternalContext().getRequestMap().get("professor"));
			getFacadeFactory().getForumFacade().adiconarForumPessoa(getForum(), forumPessoa);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}finally{
			forumPessoa = null;
		}

	}
	
	public Boolean getApresentarCampoCpf() {
		return getCampoConsultaPessoa().equals("cpf") ? true : false;
	}
	
	public List getTipoConsultaComboPessoa() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("cpf", "CPF"));
		return itens;
	}

	public List<PessoaVO> getListaConsultaPessoa() {
		if(listaConsultaPessoa == null){
			listaConsultaPessoa = new ArrayList<PessoaVO>();
		}
		return listaConsultaPessoa;
	}

	public void setListaConsultaPessoa(List<PessoaVO> listaConsultaPessoa) {
		this.listaConsultaPessoa = listaConsultaPessoa;
	}

	public String getCampoConsultaPessoa() {
		if (campoConsultaPessoa == null) {
			campoConsultaPessoa = "";
		}
		return campoConsultaPessoa;
	}

	public void setCampoConsultaPessoa(String campoConsultaPessoa) {
		this.campoConsultaPessoa = campoConsultaPessoa;
	}

	public String getValorConsultaPessoa() {
		if (valorConsultaPessoa == null) {
			valorConsultaPessoa = "";
		}
		return valorConsultaPessoa;
	}

	public void setValorConsultaPessoa(String valorConsultaPessoa) {
		this.valorConsultaPessoa = valorConsultaPessoa;
	}

	public List<SelectItem> getListaSelectItemTurmaInteragiramForum() {
		if(listaSelectItemTurmaInteragiramForum == null){
			listaSelectItemTurmaInteragiramForum = new ArrayList<SelectItem>();
		}
		return listaSelectItemTurmaInteragiramForum;
	}

	public void setListaSelectItemTurmaInteragiramForum(List<SelectItem> listaSelectItemTurmaInteragiramForum) {
		this.listaSelectItemTurmaInteragiramForum = listaSelectItemTurmaInteragiramForum;
	}

	public TurmaVO getTurmaVigenteNota() {
		if(turmaVigenteNota == null){
			turmaVigenteNota = new TurmaVO();
		}
		return turmaVigenteNota;
	}

	public void setTurmaVigenteNota(TurmaVO turmaVigenteNota) {
		this.turmaVigenteNota = turmaVigenteNota;
	}

	public String getAnoVigenteNota() {
		if(anoVigenteNota == null){
			anoVigenteNota = "";
		}
		return anoVigenteNota;
	}

	public void setAnoVigenteNota(String anoVigenteNota) {
		this.anoVigenteNota = anoVigenteNota;
	}

	public String getSemestreVigenteNota() {
		if(semestreVigenteNota == null){
			semestreVigenteNota = "";
		}
		return semestreVigenteNota;
	}

	public void setSemestreVigenteNota(String semestreVigenteNota) {
		this.semestreVigenteNota = semestreVigenteNota;
	}

	public List<SelectItem> getListaSelectItemConfiguracaoAcademico() {
		if(listaSelectItemConfiguracaoAcademico == null){
			listaSelectItemConfiguracaoAcademico = new ArrayList<SelectItem>();
		}
		return listaSelectItemConfiguracaoAcademico;
	}

	public void setListaSelectItemConfiguracaoAcademico(List<SelectItem> listaSelectItemConfiguracaoAcademico) {
		this.listaSelectItemConfiguracaoAcademico = listaSelectItemConfiguracaoAcademico;
	}

	

	public ConfiguracaoAcademicoVO getConfiguracaoAcademicoVigenteNota() {
		if(configuracaoAcademicoVigenteNota == null){
			configuracaoAcademicoVigenteNota = new ConfiguracaoAcademicoVO();
		}
		return configuracaoAcademicoVigenteNota;
	}

	public void setConfiguracaoAcademicoVigenteNota(ConfiguracaoAcademicoVO configuracaoAcademicoVigenteNota) {
		this.configuracaoAcademicoVigenteNota = configuracaoAcademicoVigenteNota;
	}

	public String getVariavelTipoNota() {
		if(variavelTipoNota == null){
			variavelTipoNota = "";
		}
		return variavelTipoNota;
	}

	public void setVariavelTipoNota(String variavelTipoNota) {
		this.variavelTipoNota = variavelTipoNota;
	}

	public List<SelectItem> getListaSelectItemTipoInformarNota() {
		if(listaSelectItemTipoInformarNota == null){
			listaSelectItemTipoInformarNota = new ArrayList<SelectItem>();
		}
		return listaSelectItemTipoInformarNota;
	}

	public void setListaSelectItemTipoInformarNota(List<SelectItem> listaSelectItemTipoInformarNota) {
		this.listaSelectItemTipoInformarNota = listaSelectItemTipoInformarNota;
	}
	
	
	
	public ConfiguracaoAcademicaNotaVO getConfiguracaoNota() {
		if(configuracaoNota == null){
			configuracaoNota = new ConfiguracaoAcademicaNotaVO();
		}
		return configuracaoNota;
	}

	public void setConfiguracaoNota(ConfiguracaoAcademicaNotaVO configuracaoNota) {
		this.configuracaoNota = configuracaoNota;
	}

	public HashMap<Integer, ConfiguracaoAcademicoVO> getMapaConfiguracoesAcademicos() {
		if(mapaConfiguracoesAcademicos == null){
			mapaConfiguracoesAcademicos = new HashMap<Integer, ConfiguracaoAcademicoVO>();
		}
		return mapaConfiguracoesAcademicos;
	}

	public void setMapaConfiguracoesAcademicos(HashMap<Integer, ConfiguracaoAcademicoVO> mapaConfiguracoesAcademicos) {
		this.mapaConfiguracoesAcademicos = mapaConfiguracoesAcademicos;
	}
	
	public List<SelectItem> getListaSelectItemNotaConceito() {
		if (listaSelectItemNotaConceito == null) {
			listaSelectItemNotaConceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNotaConceito;
	}

	public void setListaSelectItemNotaConceito(List<SelectItem> listaSelectItemNotaConceito) {
		this.listaSelectItemNotaConceito = listaSelectItemNotaConceito;
	}

	public String getCssConfiguracaoAcademicoVigenteNota() {
		if(cssConfiguracaoAcademicoVigenteNota == null){
			cssConfiguracaoAcademicoVigenteNota = "";
		}
		return cssConfiguracaoAcademicoVigenteNota;
	}

	public void setCssConfiguracaoAcademicoVigenteNota(String cssConfiguracaoAcademicoVigenteNota) {
		this.cssConfiguracaoAcademicoVigenteNota = cssConfiguracaoAcademicoVigenteNota;
	}	
	
	public Boolean getDesablitarConfiguracaoAcademicoVigenteNota(){
		return getCssConfiguracaoAcademicoVigenteNota().equals("camposSomenteLeitura") ? true: false;
	}

	public DataModelo getControleConsultaInteracaoRegistroNota() {
		if(controleConsultaInteracaoRegistroNota == null){
			controleConsultaInteracaoRegistroNota = new DataModelo();
		}
		return controleConsultaInteracaoRegistroNota;
	}

	public void setControleConsultaInteracaoRegistroNota(DataModelo controleConsultaInteracaoRegistroNota) {
		this.controleConsultaInteracaoRegistroNota = controleConsultaInteracaoRegistroNota;
	}

	public ForumRegistrarNotaVO getForumRegistrarNotaVO() {
		return forumRegistrarNotaVO;
	}

	public void setForumRegistrarNotaVO(ForumRegistrarNotaVO forumRegistrarNotaVO) {
		this.forumRegistrarNotaVO = forumRegistrarNotaVO;
	}
	
	public List<SelectItem> getListaTipoPeriodoDisponibilizacao() {
		if (listaTipoPeriodoDisponibilizacao == null) {
			listaTipoPeriodoDisponibilizacao = new ArrayList<SelectItem>();
		}
		return listaTipoPeriodoDisponibilizacao;
	}

	public void setListaTipoPeriodoDisponibilizacao(List<SelectItem> listaTipoPeriodoDisponibilizacao) {
		this.listaTipoPeriodoDisponibilizacao = listaTipoPeriodoDisponibilizacao;
	}

	public void montarListaSelectItemTipoPeriodoDisponibilizacao() {
		setListaTipoPeriodoDisponibilizacao(TipoPeriodoDisponibilidadeForumEnum.getCombobox());
	}
	
	public List<SelectItem> getListaSelectItemSemestre() {
		if (listaSelectItemSemestre == null) {
			listaSelectItemSemestre = new ArrayList<SelectItem>(0);
			listaSelectItemSemestre.add(new SelectItem("1", "1º"));
			listaSelectItemSemestre.add(new SelectItem("2", "2º"));
		}
		return listaSelectItemSemestre;
	}
	
	public void setListaSelectItemSemestre(List<SelectItem> listaSelectItemSemestre) {
		this.listaSelectItemSemestre = listaSelectItemSemestre;
	}
	
	public String inicializarTelaConsultaForumVisaoProfessor() {
		listarTodosForumsNaoRespondidos();
		return Uteis.getCaminhoRedirecionamentoNavegacao("forumProfessorCons.xhtml");
	}
	
	public void listarTodosForumsNaoRespondidos() {
		try {
			consultarTodosForumsNaoRespondidos = true;
			getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getForumFacade().consultarAtualizacaoForumPorProfessor(getUsuarioLogado().getPessoa().getCodigo(), getUsuarioLogado().getUnidadeEnsinoLogado().getCodigo(),getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), getUsuarioLogado()));
			getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getForumFacade().consultarTotalRegistroAtualizacaoForumVisaoProfessor(getForum(),getUsuarioLogado().getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Boolean getValidarFiltrosConsulta() {
		if (validarFiltrosConsulta == null) {
			validarFiltrosConsulta = true;
		}
		return validarFiltrosConsulta;
	}

	public void setValidarFiltrosConsulta(Boolean validarFiltrosConsulta) {
		this.validarFiltrosConsulta = validarFiltrosConsulta;
	}
	
	/**
	 * @return the filterAnoSemestre
	 */
	public String getFilterAnoSemestre() {
		if (filterAnoSemestre == null) {
			filterAnoSemestre = "";
		}
		return filterAnoSemestre;
	}

	/**
	 * @param filterAnoSemestre
	 *            the filterAnoSemestre to set
	 */
	public void setFilterAnoSemestre(String filterAnoSemestre) {
		this.filterAnoSemestre = filterAnoSemestre;
	}
	
	/**
	 * @return the listaSelectItemAnoSemestre
	 */
	public List<SelectItem> getListaSelectItemAnoSemestre() {
		if (listaSelectItemAnoSemestre == null) {
			listaSelectItemAnoSemestre = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemAnoSemestre;
	}

	/**
	 * @param listaSelectItemAnoSemestre
	 *            the listaSelectItemAnoSemestre to set
	 */
	public void setListaSelectItemAnoSemestre(List<SelectItem> listaSelectItemAnoSemestre) {
		this.listaSelectItemAnoSemestre = listaSelectItemAnoSemestre;
	}
	
	private void montarListaSemestreAno() throws Exception {
//		getListaSelectItemAnoSemestre().clear();
//		getListaSelectItemAnoSemestre().add(new SelectItem("", ""));
//		setFilterAnoSemestre("");
		if(getIsApresentarCampoAno()){	
//			getListaSelectItemAnoSemestre().addAll(getVisaoAlunoControle().getListaSelectItemAnoSemestre());
			setFilterAnoSemestre(getVisaoAlunoControle().getFilterAnoSemestre());
//			setFilterAnoSemestre(getFacadeFactory().getHistoricoFacade().inicializarDadosAnoSemestreHistoricoPriorizandoAtivoConcluido(getMatriculaVO().getMatricula(), getListaSelectItemAnoSemestre(), ATIVA, PRE_MATRICULA));
		}
	}
	
	public boolean getIsApresentarCampoAno() {
		return getMatriculaVO().getCurso().getPeriodicidade().equals("AN") || getMatriculaVO().getCurso().getPeriodicidade().equals("SE");
	}

	public boolean getIsApresentarCampoSemestre() {
		return getMatriculaVO().getCurso().getPeriodicidade().equals("SE");
	}

	public MatriculaVO getMatriculaVO() {
		if(matriculaVO == null) {
			matriculaVO = new MatriculaVO();
		}
		return matriculaVO;
	}

	public void setMatriculaVO(MatriculaVO matriculaVO) {
		this.matriculaVO = matriculaVO;
	}
	
	/**
	 * @return the listaSelectItemPeriodicidade
	 */
	public List<SelectItem> getListaSelectItemPeriodicidade() {
		if (listaSelectItemPeriodicidade == null) {
			listaSelectItemPeriodicidade = new ArrayList<SelectItem>(0);
			listaSelectItemPeriodicidade.add(new SelectItem("", ""));
			listaSelectItemPeriodicidade.add(new SelectItem(PeriodicidadeEnum.SEMESTRAL, PeriodicidadeEnum.SEMESTRAL.getDescricao()));
			listaSelectItemPeriodicidade.add(new SelectItem(PeriodicidadeEnum.ANUAL, PeriodicidadeEnum.ANUAL.getDescricao()));
			listaSelectItemPeriodicidade.add(new SelectItem(PeriodicidadeEnum.INTEGRAL, PeriodicidadeEnum.INTEGRAL.getDescricao()));
		}
		return listaSelectItemPeriodicidade;
	}

	/**
	 * @param listaSelectItemPeriodicidade the listaSelectItemPeriodicidade to set
	 */
	public void setListaSelectItemPeriodicidade(List<SelectItem> listaSelectItemPeriodicidade) {
		this.listaSelectItemPeriodicidade = listaSelectItemPeriodicidade;
	}

	/**
	 * @return the periodicidade
	 */
	public PeriodicidadeEnum getPeriodicidade() {
		return periodicidade;
	}

	/**
	 * @param periodicidade the periodicidade to set
	 */
	public void setPeriodicidade(PeriodicidadeEnum periodicidade) {
		this.periodicidade = periodicidade;
	}
	
	public boolean getApresentarAno(){
		return Uteis.isAtributoPreenchido(getPeriodicidade()) && !getPeriodicidade().equals(PeriodicidadeEnum.INTEGRAL);
	}
	
	public boolean getApresentarSemestre(){
		return Uteis.isAtributoPreenchido(getPeriodicidade()) && getPeriodicidade().equals(PeriodicidadeEnum.SEMESTRAL);
	}
	
	public String getAno() {
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getSemestre() {
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}
	
	
	public void setarPeriodicidade() {
		try {
			if (Uteis.isAtributoPreenchido(getForum().getCursoVO())) {
				getForum().setCursoVO(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(getForum().getCursoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, getUsuarioLogado()));
				setPeriodicidade(PeriodicidadeEnum.getEnumPorValor(getForum().getCursoVO().getPeriodicidade()));
			}
			carregarDadosTurma();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void montarListaCursoEturmaPorPeriodicidade() {
		try {
			montarListaSelectItemCursoPorAreaConhecimento();
			montarListaSelectItemTurmaDisciplina();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void montarListaItemTurmaDisciplina() {
		try {
			setAno(getForum().getAno());
			setSemestre(getForum().getSemestre());
			montarListaSelectItemTurmaDisciplina(getAno(), getSemestre());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void consultarListaSelectItemTurmaDisciplina() {
		try {
			if (getForum().getTipoPeriodoDisponibilizacao().equals(TipoPeriodoDisponibilidadeForumEnum.SEMPRE_DISPONIVEL.name())) {
				setAno("");
				setSemestre("");
			}else if(getForum().getTipoPeriodoDisponibilizacao().equals(TipoPeriodoDisponibilidadeForumEnum.ANO.name())){
				getForum().setAno(Uteis.getData(new Date(), "yyyy"));
				getForum().setSemestre("");
				setAno(Uteis.getData(new Date(), "yyyy"));
				setSemestre("");
			}else {
				setAno(Uteis.getData(new Date(), "yyyy"));
				setSemestre(Uteis.getSemestreAtual());	
			}
			montarListaSelectItemTurmaDisciplina(getAno(), getSemestre());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private void validarForumRegistroNota() throws Exception {
		if (!Uteis.isAtributoPreenchido(getConfiguracaoAcademicoVigenteNota())) {
			throw new Exception(UteisJSF.internacionalizar("msg_ForumRegistrarNota_configuracoesAcademicas"));
		}
		if (!Uteis.isAtributoPreenchido(getVariavelTipoNota())) {
			throw new Exception(UteisJSF.internacionalizar("msg_ForumRegistrarNota_tipoNota"));
		}
	}

	public String getFecharModalRegistrarNotaAluno() {
		if (fecharModalRegistrarNotaAluno == null) {
			fecharModalRegistrarNotaAluno = "";
		}
		return fecharModalRegistrarNotaAluno;
	}

	public void setFecharModalRegistrarNotaAluno(String fecharModalRegistrarNotaAluno) {
		this.fecharModalRegistrarNotaAluno = fecharModalRegistrarNotaAluno;
	}

	public void montardadosdisciplina() {
		  try {
			  if (Uteis.isAtributoPreenchido(getForum().getDisciplina().getCodigo())) {
				  getForum().setDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(getForum().getDisciplina().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public List<SelectItem> getListaSelectItemOrdenador() {
		if (listaSelectItemPeriodicidade == null) {
			listaSelectItemPeriodicidade = new ArrayList<SelectItem>(0);
			listaSelectItemPeriodicidade.add(new SelectItem(opcaoOrdenacaoForum.ANTIGO, "Antigo"));
			listaSelectItemPeriodicidade.add(new SelectItem(opcaoOrdenacaoForum.NOVO, "Recente"));
		}
		return listaSelectItemPeriodicidade;
	}
	

}
