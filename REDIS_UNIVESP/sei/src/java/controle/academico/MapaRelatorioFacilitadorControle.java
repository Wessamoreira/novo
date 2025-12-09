package controle.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import controle.arquitetura.SuperControle.MSG_TELA;
import negocio.comuns.academico.CalendarioRelatorioFinalFacilitadorVO;
import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.RelatorioFinalFacilitadorVO;
import negocio.comuns.academico.enumeradores.SituacaoRelatorioFacilitadorEnum;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilReflexao;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import relatorio.controle.arquitetura.SuperControleRelatorio;


@SuppressWarnings("unchecked")
@Controller("MapaRelatorioFacilitadorControle")
@Scope("viewScope")
@Lazy
public class MapaRelatorioFacilitadorControle extends SuperControleRelatorio {


	private static final long serialVersionUID = 1L;
	private UnidadeEnsinoVO unidadeEnsinoVO;
    private String campoConsultaDisciplina;
    private String valorConsultaDisciplina;
    private List<DisciplinaVO> listaConsultaDisciplina;
    private DisciplinaVO disciplinaVO;
    private List<SelectItem> tipoConsultaComboDisciplina;
    private SituacaoRelatorioFacilitadorEnum renderizacaoSituacaoFacilitador;
	private List listaConsultaAluno;
	private String campoConsultaAluno;
	private String valorConsultaAluno;
	private Boolean permitirDeferir;
	private Boolean permitirIndeferir;
	private Boolean permitirAcessoSupervisores;
	private Boolean permitirCorrecaoAluno;
	private Boolean permitirSuspensaoBolsa;
	private Boolean apresentarSuspensaoBolsa;
	private Boolean apresentarIndeferimento;
	private Boolean apresentarDeferimento;
	private Boolean apresentarCorrecaoAluno;
	private Boolean apresentarAnalise;
	private Date dataEnvioAnaliseInicio;
	private Date dataEnvioAnaliseFim;
	private CalendarioRelatorioFinalFacilitadorVO calendarioRelatorioFinalFacilitadorFiltroVO;
	private CalendarioRelatorioFinalFacilitadorVO calendarioRelatorioFinalFacilitadorVO;

	private RelatorioFinalFacilitadorVO relatorioFinalFacilitadorVO;
	private RelatorioFinalFacilitadorVO relatorioFinalFacilitadorFiltroVO;
	private List<PessoaVO> listaConsultaPessoa;
	private String campoConsultaSupervisor;
	private String valorConsultaSupervisor;
	private DataModelo controleConsultaDashboradOtimizado;


	public MapaRelatorioFacilitadorControle() {
		getCalendarioRelatorioFinalFacilitadorFiltroVO().setAno(Uteis.getAnoDataAtual());
		getCalendarioRelatorioFinalFacilitadorFiltroVO().setSemestre(Uteis.getSemestreAtual());
		getCalendarioRelatorioFinalFacilitadorFiltroVO().setMes(String.valueOf(Uteis.getMesDataAtual()));
		verificarPermissaoDeferirRelatorio();
		verificarPermissaoIndeferirRelatorio();
		verificarPermissaoPermitirAcessoSupervisores();
		verificarPermissaoPermitirCorrecaoAluno();
		verificarPermissaoPermitirSuspensaoBolsa();
		if (!getPermitirAcessoSupervisores()) {
			getRelatorioFinalFacilitadorFiltroVO().setSupervisor(getUsuarioLogadoClone().getPessoa());
		}
		inicializarListaRelaotrioFinalFacilitador();
		consultarUnidadeEnsino();
	}

	
	public void mudarSituacaoRelatorio(String situacao) {
		setRenderizacaoSituacaoFacilitador(SituacaoRelatorioFacilitadorEnum.getEnum(situacao));	
	}
	
	public void inicializarListaRelaotrioFinalFacilitador() {
		getControleConsultaOtimizado().setLimitePorPagina(10);
		getControleConsultaOtimizado().setPaginaAtual(0);
		getControleConsultaOtimizado().setPage(0);
		getControleConsultaOtimizado().setOffset(0);
	}

	public void scrollerListenerRelaotrioFinalFacilitador(DataScrollEvent dataScrollerEvent) throws Exception {
		getControleConsultaOtimizado().setPaginaAtual(dataScrollerEvent.getPage());
		getControleConsultaOtimizado().setPage(dataScrollerEvent.getPage());
		consultarRelatorioFacilitador();
	}
	
	public void consultarRelatorioFacilitador() {
		try {
			getControleConsultaOtimizado().setListaConsulta(new ArrayList<>());
			getRelatorioFinalFacilitadorVO().setAno(getCalendarioRelatorioFinalFacilitadorFiltroVO().getAno());
			getRelatorioFinalFacilitadorVO().setSemestre(getCalendarioRelatorioFinalFacilitadorFiltroVO().getSemestre());
			getRelatorioFinalFacilitadorVO().setMes(getCalendarioRelatorioFinalFacilitadorFiltroVO().getMes());
			getFacadeFactory().getRelatorioFinalFacilitadorInterfaceFacade().validarDados(getRelatorioFinalFacilitadorVO());
			getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getRelatorioFinalFacilitadorInterfaceFacade().consultarRelatorioFacilitador(getRelatorioFinalFacilitadorFiltroVO(), getUnidadeEnsinoVOMarcadasParaSeremUtilizadas(), getDataEnvioAnaliseInicio(), getDataEnvioAnaliseFim(), getCalendarioRelatorioFinalFacilitadorFiltroVO(), getMultiplasSituacoes(), Uteis.NIVELMONTARDADOS_TODOS, getControleConsultaOtimizado(), getUsuarioLogadoClone()));
			if(Uteis.isAtributoPreenchido(getControleConsultaOtimizado().getListaConsulta())) {
				getRelatorioFinalFacilitadorFiltroVO().setTotalizadorNaoEnviouRelatorio(((RelatorioFinalFacilitadorVO) getControleConsultaOtimizado().getListaConsulta().get(0)).getTotalizadorNaoEnviouRelatorio());
				getRelatorioFinalFacilitadorFiltroVO().setTotalizadorEmAnalise(((RelatorioFinalFacilitadorVO) getControleConsultaOtimizado().getListaConsulta().get(0)).getTotalizadorEmAnalise());
				getRelatorioFinalFacilitadorFiltroVO().setTotalizadorCorrecaoAluno(((RelatorioFinalFacilitadorVO) getControleConsultaOtimizado().getListaConsulta().get(0)).getTotalizadorCorrecaoAluno());
				getRelatorioFinalFacilitadorFiltroVO().setTotalizadorDeferido(((RelatorioFinalFacilitadorVO) getControleConsultaOtimizado().getListaConsulta().get(0)).getTotalizadorDeferido());
				getRelatorioFinalFacilitadorFiltroVO().setTotalizadorIndeferido(((RelatorioFinalFacilitadorVO) getControleConsultaOtimizado().getListaConsulta().get(0)).getTotalizadorIndeferido());
				getRelatorioFinalFacilitadorFiltroVO().setTotalizadorSuspensaoBolsa(((RelatorioFinalFacilitadorVO) getControleConsultaOtimizado().getListaConsulta().get(0)).getTotalizadorSuspensaoBolsa());
			} else {
				getRelatorioFinalFacilitadorFiltroVO().setTotalizadorNaoEnviouRelatorio(getFacadeFactory().getRelatorioFinalFacilitadorInterfaceFacade().consultarTotalizadorNaoEnviouRelatorio(getRelatorioFinalFacilitadorFiltroVO(), getUnidadeEnsinoVOMarcadasParaSeremUtilizadas(), getDataEnvioAnaliseInicio(), getDataEnvioAnaliseFim(), getCalendarioRelatorioFinalFacilitadorFiltroVO(), getMultiplasSituacoes()));
				getRelatorioFinalFacilitadorFiltroVO().setTotalizadorEmAnalise(0);
				getRelatorioFinalFacilitadorFiltroVO().setTotalizadorCorrecaoAluno(0);
				getRelatorioFinalFacilitadorFiltroVO().setTotalizadorDeferido(0);
				getRelatorioFinalFacilitadorFiltroVO().setTotalizadorIndeferido(0);
				getRelatorioFinalFacilitadorFiltroVO().setTotalizadorSuspensaoBolsa(0);
			}
			setMensagemID("msg_dados_consultados", Uteis.ALERTA);
		} catch (Exception ex) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), ex.getMessage(), ex, getUsuarioLogado(), "", "", true);
			getRelatorioFinalFacilitadorFiltroVO().setTotalizadorNaoEnviouRelatorio(0);
			getRelatorioFinalFacilitadorFiltroVO().setTotalizadorEmAnalise(0);
			getRelatorioFinalFacilitadorFiltroVO().setTotalizadorCorrecaoAluno(0);
			getRelatorioFinalFacilitadorFiltroVO().setTotalizadorDeferido(0);
			getRelatorioFinalFacilitadorFiltroVO().setTotalizadorIndeferido(0);
			getRelatorioFinalFacilitadorFiltroVO().setTotalizadorSuspensaoBolsa(0);
		}
	}

	public void realizarVisualizacaoRelatorioFinalFacilitador() {
		try {
			RelatorioFinalFacilitadorVO obj = (RelatorioFinalFacilitadorVO) context().getExternalContext().getRequestMap().get("relatorioFacilitadorItens");
			setRelatorioFinalFacilitadorVO((RelatorioFinalFacilitadorVO) Uteis.clonar(obj));
			setCalendarioRelatorioFinalFacilitadorVO(getFacadeFactory().getCalendarioRelatorioFinalFacilitadorInterfaceFacade().consultarPorRelatorioFacilitadoresRegistroUnico(obj.getMatriculaperiodoturmadisciplinaVO().getDisciplina().getCodigo(), "", obj.getAno(), obj.getSemestre(), obj.getMes(), false, getUsuarioLogado()));
			getFacadeFactory().getRelatorioFinalFacilitadorInterfaceFacade().preencherDadosRelatorioFacilitadorQuestionarioRespostaOrigem(getRelatorioFinalFacilitadorVO(), getRelatorioFinalFacilitadorVO().getQuestionarioRespostaOrigemVO().getCodigo(), getUsuarioLogado());
			limparMensagem();
			getControleConsultaDashboradOtimizado().getListaConsulta().clear();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}
	
	public void consultarDashboardRelarorioFinalFacilitador(String situacao) {
		try {
			mudarSituacaoRelatorio(situacao);
			if(getRenderizacaoSituacaoFacilitador().equals(SituacaoRelatorioFacilitadorEnum.NENHUM)) {
				getControleConsultaDashboradOtimizado().setListaConsulta(getFacadeFactory().getRelatorioFinalFacilitadorInterfaceFacade().consultarDashboradRelatorioFacilitador(getRelatorioFinalFacilitadorFiltroVO(), getUnidadeEnsinoVOMarcadasParaSeremUtilizadas(), getDataEnvioAnaliseInicio(), getDataEnvioAnaliseFim(),
						getCalendarioRelatorioFinalFacilitadorFiltroVO(), getControleConsultaDashboradOtimizado(), getUsuarioLogadoClone()));
			} else {
				getControleConsultaDashboradOtimizado().setListaConsulta(getFacadeFactory().getRelatorioFinalFacilitadorInterfaceFacade().consultarRelatorioFacilitador(getRelatorioFinalFacilitadorFiltroVO(), getUnidadeEnsinoVOMarcadasParaSeremUtilizadas(),
						 getDataEnvioAnaliseInicio(), getDataEnvioAnaliseFim(), getCalendarioRelatorioFinalFacilitadorFiltroVO(), getRenderizacaoSituacaoFacilitador().toString(), Uteis.NIVELMONTARDADOS_TODOS, getControleConsultaDashboradOtimizado(), getUsuarioLogadoClone()));
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}
	   public void inicializarListaDashboardRelaotrioFinalFacilitador(String situacao) {
		   getControleConsultaDashboradOtimizado().setPaginaAtual(1);
		   getControleConsultaDashboradOtimizado().setPage(1);
		   if(!Uteis.isAtributoPreenchido(getControleConsultaDashboradOtimizado().getLimitePorPagina())) {
				getControleConsultaDashboradOtimizado().setLimitePorPagina(10);	
			}
			consultarDashboardRelarorioFinalFacilitador(situacao);
		}
	    
	    public void scrollerListenerDashboardRelaotrioFinalFacilitador(DataScrollEvent dataScrollerEvent) throws Exception {
	    	getControleConsultaDashboradOtimizado().setPaginaAtual(dataScrollerEvent.getPage());
	    	getControleConsultaDashboradOtimizado().setPage(dataScrollerEvent.getPage());
	    	if(!Uteis.isAtributoPreenchido(getControleConsultaDashboradOtimizado().getLimitePorPagina())) {
				getControleConsultaDashboradOtimizado().setLimitePorPagina(10);	
			}
			consultarDashboardRelarorioFinalFacilitador(getRenderizacaoSituacaoFacilitador().getKey());
		}
	    
		public DataModelo getControleConsultaDashboradOtimizado() {
			if (controleConsultaDashboradOtimizado == null) {
				controleConsultaDashboradOtimizado = new DataModelo();
			}
			return controleConsultaDashboradOtimizado;
		}

		public void setControleConsultaDashboradOtimizado(DataModelo controleConsultaDashboradOtimizado) {
			this.controleConsultaDashboradOtimizado = controleConsultaDashboradOtimizado;
		}

		private String multiplasSituacoes;
		public String getMultiplasSituacoes() {
			if (multiplasSituacoes == null) {
				multiplasSituacoes = "";
			}
			return multiplasSituacoes;
		}

		public void setMultiplasSituacoes(String multiplasSituacoes) {
			this.multiplasSituacoes = multiplasSituacoes;
		}
		
		public void definirSituacaoRelatorioFacilitador() {
			StringBuilder situacao = new StringBuilder();
	        if (getApresentarAnalise()) {
	            situacao.append(SituacaoRelatorioFacilitadorEnum.ANALISE_SUPERVISOR.toString() + ",");
	        }
	        if (getApresentarCorrecaoAluno()) {
	            situacao.append(SituacaoRelatorioFacilitadorEnum.CORRECAO_ALUNO.toString() + ",");
	        }
	        if (getApresentarDeferimento()) {
	            situacao.append(SituacaoRelatorioFacilitadorEnum.DEFERIDO_SUPERVISOR.toString() + ",");
	        }
	        if (getApresentarIndeferimento()) {
	            situacao.append(SituacaoRelatorioFacilitadorEnum.INDEFERIDO_SUPERVISOR.toString() + ",");
	        }
	        if (getApresentarSuspensaoBolsa()) {
	            situacao.append(SituacaoRelatorioFacilitadorEnum.SUSPENSAO_BOLSA.toString() + ",");
	        }
	        if (situacao.length() > 0) {
	            situacao.setLength(situacao.length() - 1);
	        }
	        setMultiplasSituacoes(situacao.toString());
	        inicializarListaRelaotrioFinalFacilitador();
	        consultarRelatorioFacilitador(); 
		}

	public void consultarUnidadeEnsino() {
		try {
			consultarUnidadeEnsinoFiltroRelatorio("");
			setMarcarTodasUnidadeEnsino(false);
			marcarTodasUnidadesEnsinoAction();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}
	
	public void verificarTodasUnidadesSelecionadas() {
		StringBuilder unidade = new StringBuilder();
		if (getUnidadeEnsinoVOs().size() > 1) {
			for (UnidadeEnsinoVO obj : getUnidadeEnsinoVOs()) {
				if (obj.getFiltrarUnidadeEnsino()) {
					unidade.append(obj.getNome()).append("; ");
				}
			}
			getUnidadeEnsinoVO().setNome(unidade.toString());
		} else {
			if (!getUnidadeEnsinoVOs().isEmpty()) {
				if (getUnidadeEnsinoVOs().get(0).getFiltrarUnidadeEnsino()) {
					getUnidadeEnsinoVO().setNome(getUnidadeEnsinoVOs().get(0).getNome());
				}
			} else {
				getUnidadeEnsinoVO().setNome(unidade.toString());
			}
		}
		inicializarListaRelaotrioFinalFacilitador();
        consultarRelatorioFacilitador(); 
	}	

	public void marcarTodasUnidadesEnsinoAction() {
		for (UnidadeEnsinoVO unidade : getUnidadeEnsinoVOs()) {
			if (getMarcarTodasUnidadeEnsino()) {
				unidade.setFiltrarUnidadeEnsino(Boolean.TRUE);
			} else {
				unidade.setFiltrarUnidadeEnsino(Boolean.FALSE);
			}
		}
		verificarTodasUnidadesSelecionadas();
	}

	public void consultarSupervisor() {
		try {
			super.consultar();
			List<PessoaVO> objs = new ArrayList<>(0);
			if (getCampoConsultaSupervisor().equals("nome")) {
				if (getValorConsultaSupervisor().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = (List<PessoaVO>) getFacadeFactory().getRelatorioFinalFacilitadorInterfaceFacade().consultarSupervisorPorNome(getValorConsultaSupervisor(), 
						getCalendarioRelatorioFinalFacilitadorFiltroVO().getAno(), getCalendarioRelatorioFinalFacilitadorFiltroVO().getSemestre(),getUsuarioLogado());
			}
			if (getCampoConsultaSupervisor().equals("cpf")) {
				if (getValorConsultaSupervisor().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getRelatorioFinalFacilitadorInterfaceFacade().consultarSupervisorPorCPF(getValorConsultaSupervisor(), 
						getCalendarioRelatorioFinalFacilitadorFiltroVO().getAno(), getCalendarioRelatorioFinalFacilitadorFiltroVO().getSemestre(),getUsuarioLogado());
			}
			setListaConsultaPessoa(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList<>(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}
	
	public void selecionarSupervisor() {
		try {
			PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("supervisorItem");
			getRelatorioFinalFacilitadorFiltroVO().setSupervisor(obj);
			getListaConsultaPessoa().clear();
			this.setValorConsultaSupervisor("");
			this.setCampoConsultaSupervisor("");
			inicializarListaRelaotrioFinalFacilitador();
	        consultarRelatorioFacilitador();
		} catch (Exception ex) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), ex.getMessage(), ex, getUsuarioLogado(), "", "", true);
		}
	}

	public void limparSupervisor() {
		try {
			getRelatorioFinalFacilitadorFiltroVO().setSupervisor(new PessoaVO());
			inicializarListaRelaotrioFinalFacilitador();
	        consultarRelatorioFacilitador();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}
	
	public Boolean getApresentarCampoCpf() {
		return getCampoConsultaSupervisor().equals("cpf") ? true : false;
	}

    public void consultarDisciplina() {
        try {
            List objs = new ArrayList<>(0);
            if (getCampoConsultaDisciplina().equals("codigo")) {
                if (getValorConsultaDisciplina().equals("")) {
                    setValorConsultaDisciplina("0");
                }
                int valorInt = Integer.parseInt(getValorConsultaDisciplina());
                objs = getFacadeFactory().getDisciplinaFacade().consultarPorCodigo_CalendarioRelatorioFacilitador(valorInt, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaDisciplina().equals("nome")) {
				Uteis.checkState(getValorConsultaDisciplina().length() < 2, getMensagemInternalizacao("msg_Autor_valorConsultaVazio"));
                objs = getFacadeFactory().getDisciplinaFacade().consultarPorNome_CalendarioRelatorioFacilitador(getValorConsultaDisciplina(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            setListaConsultaDisciplina(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaDisciplina(new ArrayList<>(0));
            setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);

        }
    }
    
    public void limparDisciplina() {
        try {
        	getRelatorioFinalFacilitadorFiltroVO().getMatriculaperiodoturmadisciplinaVO().setDisciplina(new DisciplinaVO());
        	inicializarListaRelaotrioFinalFacilitador();
	        consultarRelatorioFacilitador();
        } catch (Exception e) {
        	setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
        }
    }
    
    public void selecionarDisciplina() throws Exception {
        DisciplinaVO obj = (DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplinaItens");
        getRelatorioFinalFacilitadorFiltroVO().getMatriculaperiodoturmadisciplinaVO().setDisciplina(obj);
        getListaConsultaDisciplina().clear();
        inicializarListaRelaotrioFinalFacilitador();
        consultarRelatorioFacilitador(); 
    }

    public List<SelectItem> getTipoConsultaComboDisciplina() {
        if (tipoConsultaComboDisciplina == null) {
            tipoConsultaComboDisciplina = new ArrayList<SelectItem>(0);
            tipoConsultaComboDisciplina.add(new SelectItem("nome", "Nome"));
            tipoConsultaComboDisciplina.add(new SelectItem("codigo", "Código"));
        }
        return tipoConsultaComboDisciplina;
    }
    
	public void consultarAlunoPorMatricula() {
		try {
			MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatriculaFacilitador(getRelatorioFinalFacilitadorFiltroVO().getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO().getMatricula(), 
					getCalendarioRelatorioFinalFacilitadorFiltroVO().getAno(), getCalendarioRelatorioFinalFacilitadorFiltroVO().getSemestre(), false, getUsuarioLogado()); 
			if (objAluno.getMatricula().equals("")) {
				throw new Exception("Aluno de matrícula " + getRelatorioFinalFacilitadorFiltroVO().getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
			}
			getRelatorioFinalFacilitadorFiltroVO().getMatriculaperiodoturmadisciplinaVO().setMatriculaObjetoVO(objAluno);
	        consultarRelatorioFacilitador(); 
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
			getRelatorioFinalFacilitadorFiltroVO().getMatriculaperiodoturmadisciplinaVO().setMatriculaObjetoVO(new MatriculaVO());
			consultarRelatorioFacilitador();
		}
	}
	
	public void consultarAluno() {
		try {
			List objs = new ArrayList(0);
			if (getValorConsultaAluno().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			if (getCampoConsultaAluno().equals("matricula")) {
				MatriculaVO matriculaVO = getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatriculaFacilitador(getValorConsultaAluno(), getCalendarioRelatorioFinalFacilitadorFiltroVO().getAno(), getCalendarioRelatorioFinalFacilitadorFiltroVO().getSemestre(), false, getUsuarioLogado());
				if (!matriculaVO.getMatricula().equals("")) {
					objs.add(matriculaVO);
				} else {
					removerObjetoMemoria(matriculaVO);
				}
			}
			if (getCampoConsultaAluno().equals("nomePessoa")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeFacilitador(getValorConsultaAluno(), getCalendarioRelatorioFinalFacilitadorFiltroVO().getAno(), getCalendarioRelatorioFinalFacilitadorFiltroVO().getSemestre(), false, getUsuarioLogado());
			}
			setListaConsultaAluno(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAluno(new ArrayList(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}

	public void selecionarAluno() throws Exception {
		MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItem");
		getRelatorioFinalFacilitadorFiltroVO().getMatriculaperiodoturmadisciplinaVO().setMatriculaObjetoVO(obj);
		inicializarListaRelaotrioFinalFacilitador();
        consultarRelatorioFacilitador(); 
		valorConsultaAluno = "";
		campoConsultaAluno = "";
		getListaConsultaAluno().clear();
	}
	
	public void limparDadosAluno() throws Exception {
		try {
			getRelatorioFinalFacilitadorFiltroVO().getMatriculaperiodoturmadisciplinaVO().setMatriculaObjetoVO(null);
			inicializarListaRelaotrioFinalFacilitador();
	        consultarRelatorioFacilitador();
			getListaConsulta().clear();
		} catch (Exception e) {
		}
	}

	public String getUrlDonloadSV() {
		if(getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
			return "location.href='../../DownloadSV'";
		}else {
			return "location.href='../DownloadSV'";
		}
	}
	
	public List<SelectItem> getTipoConsultaComboPessoa() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("cpf", "CPF"));
		return itens;
	}

	public List<PessoaVO> getListaConsultaPessoa() {
		if (listaConsultaPessoa == null) {
			listaConsultaPessoa = new ArrayList<PessoaVO>();
		}
		return listaConsultaPessoa;
	}

	public void setListaConsultaPessoa(List<PessoaVO> listaConsultaPessoa) {
		this.listaConsultaPessoa = listaConsultaPessoa;
	}

	public String getCampoConsultaSupervisor() {
		if (campoConsultaSupervisor == null) {
			campoConsultaSupervisor = "";
		}
		return campoConsultaSupervisor;
	}

	public void setCampoConsultaSupervisor(String campoConsultaSupervisor) {
		this.campoConsultaSupervisor = campoConsultaSupervisor;
	}

	public String getValorConsultaSupervisor() {
		if (valorConsultaSupervisor == null) {
			valorConsultaSupervisor = "";
		}
		return valorConsultaSupervisor;
	}

	public void setValorConsultaSupervisor(String valorConsultaSupervisor) {
		this.valorConsultaSupervisor = valorConsultaSupervisor;
	}
	
	public List getTipoConsultaComboAluno() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nomePessoa", "Nome"));
		itens.add(new SelectItem("matricula", "Matricula"));
		return itens;
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

    public List getListaConsultaDisciplina() {
        if (listaConsultaDisciplina == null) {
            listaConsultaDisciplina = new ArrayList<>(0);
        }
        return listaConsultaDisciplina;
    }

    public DisciplinaVO getDisciplinaVO() {
        if (disciplinaVO == null) {
            disciplinaVO = new DisciplinaVO();
        }
        return disciplinaVO;
    }

    public void setDisciplinaVO(DisciplinaVO disciplinaVO) {
        this.disciplinaVO = disciplinaVO;
    }

    
    public void setListaConsultaDisciplina(List listaConsultaDisciplina) {
        this.listaConsultaDisciplina = listaConsultaDisciplina;
    }

	public List getListaConsultaAluno() {
		if (listaConsultaAluno == null) {
			listaConsultaAluno = new ArrayList(0);
		}
		return listaConsultaAluno;
	}

	public void setListaConsultaAluno(List listaConsultaAluno) {
		this.listaConsultaAluno = listaConsultaAluno;
	}

	public String getCampoConsultaAluno() {
		if (campoConsultaAluno == null) {
			campoConsultaAluno = "";
		}
		return campoConsultaAluno;
	}

	public void setCampoConsultaAluno(String campoConsultaAluno) {
		this.campoConsultaAluno = campoConsultaAluno;
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
		
	public void alterarDefinirSituacaoRelatorio(String situacao) {
	    try {
	        List<RelatorioFinalFacilitadorVO> listaFacilitadoresSemRelatorio = new ArrayList<>(0);
	        setOncompleteModal("");
	        mudarSituacaoRelatorio(situacao);
	        getRelatorioFinalFacilitadorVO().setSituacao(getRenderizacaoSituacaoFacilitador());
	        definirDataPorSituacao(situacao);
	        listaFacilitadoresSemRelatorio = (List<RelatorioFinalFacilitadorVO>) getControleConsultaDashboradOtimizado().getListaConsulta().stream()
	        		.filter(p -> ((RelatorioFinalFacilitadorVO) p).getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO().getAlunoSelecionado()).collect(Collectors.toList());
	        if (Uteis.isAtributoPreenchido(listaFacilitadoresSemRelatorio)) {
	            persistirAlunosSemRelatorio(listaFacilitadoresSemRelatorio);
	        } else {
	            processarNotaESituacaoRelatorio(situacao);
	            listaFacilitadoresSemRelatorio.add(getRelatorioFinalFacilitadorVO());
	        }
	        getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemSituacaoRelatorioFacilitador(listaFacilitadoresSemRelatorio, situacao, getUsuario());
	        inicializarListaRelaotrioFinalFacilitador();
	        consultarRelatorioFacilitador();
	    } catch (Exception e) {
	    	if (SituacaoRelatorioFacilitadorEnum.DEFERIDO_SUPERVISOR.getKey().contentEquals(situacao)) {
	            setOncompleteModal("RichFaces.$('panelDeferir').show(); RichFaces.$('panelQuestionario').show()");
	        } else {
	            setOncompleteModal("RichFaces.$('panelMotivo').show(); RichFaces.$('panelQuestionario').show()");
	        }
	    	setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
	    }
	}

	private void definirDataPorSituacao(String situacao) {
	    Date dataAtual = new Date();
	    switch (situacao) {
	        case "DE":
	            getRelatorioFinalFacilitadorVO().setDataDeferimento(Uteis.getDataJDBCTimestamp(dataAtual));
	            break;
	        case "CO":
	            getRelatorioFinalFacilitadorVO().setDataEnvioCorrecao(Uteis.getDataJDBCTimestamp(dataAtual));
	            break;
	        case "IN":
	        case "SB":
	            getRelatorioFinalFacilitadorVO().setDataIndeferimento(Uteis.getDataJDBCTimestamp(dataAtual));
	            break;
	    }
	}

	private void processarNotaESituacaoRelatorio(String situacao) throws Exception {
        if (!SituacaoRelatorioFacilitadorEnum.DEFERIDO_SUPERVISOR.getKey().contentEquals(situacao)) {
        	if (SituacaoRelatorioFacilitadorEnum.INDEFERIDO_SUPERVISOR.getKey().contentEquals(situacao) || 
        			SituacaoRelatorioFacilitadorEnum.SUSPENSAO_BOLSA.getKey().contentEquals(situacao)) {
        		getRelatorioFinalFacilitadorVO().setNota(0.0);
        	}
            Uteis.checkState(getRelatorioFinalFacilitadorVO().getMotivo() == "", "Informe um Motivo para o aluno");
        }
        if (!SituacaoRelatorioFacilitadorEnum.CORRECAO_ALUNO.getKey().contentEquals(situacao)) {
	        Uteis.checkState(getRelatorioFinalFacilitadorVO().getNota() == null, "Informe uma nota para o aluno");
	        getRelatorioFinalFacilitadorVO().setNomeResponsavel(getUsuarioLogadoClone().getPessoa().getNome());
	        getRelatorioFinalFacilitadorVO().setDataResponsavel(new Date());
	        getFacadeFactory().getHistoricoFacade().realizarLancamentoNotaHistoricoAutomaticamente(getCalendarioRelatorioFinalFacilitadorVO().getVariavelTipoNota(),
	            getRelatorioFinalFacilitadorVO().getNota(), getRelatorioFinalFacilitadorVO().getMatriculaperiodoturmadisciplinaVO(), false, getUsuarioLogado());
	    }
	    getFacadeFactory().getRelatorioFinalFacilitadorInterfaceFacade().alterarSituacaoRelatorioFacilitador(getRelatorioFinalFacilitadorVO(), getRelatorioFinalFacilitadorVO().getNota(), getUsuarioLogado());
	    configurarOncompleteModal(situacao);
	}

	private void configurarOncompleteModal(String situacao) {
	    if (SituacaoRelatorioFacilitadorEnum.DEFERIDO_SUPERVISOR.getKey().contentEquals(situacao)) {
	        setOncompleteModal("RichFaces.$('panelDeferir').hide(); RichFaces.$('panelQuestionario').hide()");
	    } else {
	        setOncompleteModal("RichFaces.$('panelMotivo').hide(); RichFaces.$('panelQuestionario').hide()");
	    }
	}

	public void persistirAlunosSemRelatorio(List<RelatorioFinalFacilitadorVO> listaFacilitadoresSemRelatorio) {
		try { 
			if(Uteis.isAtributoPreenchido(listaFacilitadoresSemRelatorio)){
				for (RelatorioFinalFacilitadorVO relatorioFinalFacilitadorVO : listaFacilitadoresSemRelatorio) {
					setCalendarioRelatorioFinalFacilitadorVO(getFacadeFactory().getCalendarioRelatorioFinalFacilitadorInterfaceFacade().consultarPorRelatorioFacilitadoresRegistroUnico(relatorioFinalFacilitadorVO.getMatriculaperiodoturmadisciplinaVO().getDisciplina().getCodigo(), "Em aberto",
							getCalendarioRelatorioFinalFacilitadorVO().getAno(), getCalendarioRelatorioFinalFacilitadorVO().getSemestre(), getCalendarioRelatorioFinalFacilitadorVO().getMes(), false, getUsuarioLogado()));
					relatorioFinalFacilitadorVO.setDataIndeferimento(new Date());
					relatorioFinalFacilitadorVO.setNota(0.0);
					relatorioFinalFacilitadorVO.setMes(getCalendarioRelatorioFinalFacilitadorVO().getMes());
					relatorioFinalFacilitadorVO.setAno(getCalendarioRelatorioFinalFacilitadorVO().getAno());
					relatorioFinalFacilitadorVO.setSemestre(getCalendarioRelatorioFinalFacilitadorVO().getSemestre());
					relatorioFinalFacilitadorVO.setQuestionarioRespostaOrigemVO(null);
					relatorioFinalFacilitadorVO.setMotivo(getRelatorioFinalFacilitadorVO().getMotivo());
					relatorioFinalFacilitadorVO.setSituacao(getRelatorioFinalFacilitadorVO().getSituacao());
					getFacadeFactory().getRelatorioFinalFacilitadorInterfaceFacade().persistir(relatorioFinalFacilitadorVO, getUsuarioLogadoClone());
					getFacadeFactory().getHistoricoFacade().realizarLancamentoNotaHistoricoAutomaticamente(getCalendarioRelatorioFinalFacilitadorVO().getVariavelTipoNota(), getRelatorioFinalFacilitadorVO().getNota(), getRelatorioFinalFacilitadorVO().getMatriculaperiodoturmadisciplinaVO(), false, getUsuarioLogado());
				}
				inicializarListaRelaotrioFinalFacilitador();
		        consultarRelatorioFacilitador();
			}
			setOncompleteModal("RichFaces.$('panelMotivo').hide(); RichFaces.$('panelDashboard').hide()");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}
	
	public void geraRelatorioExcel() {
		try {			
			getControleConsultaDashboradOtimizado().setOffset(0);
			getControleConsultaDashboradOtimizado().setLimitePorPagina(0);
			consultarDashboardRelarorioFinalFacilitador(getRenderizacaoSituacaoFacilitador().getKey());
			File arquivo = getFacadeFactory().getRelatorioFinalFacilitadorInterfaceFacade().realizarGeracaoExcel((List<RelatorioFinalFacilitadorVO>) getControleConsultaDashboradOtimizado().getListaConsulta(), getLogoPadraoRelatorio(), getUsuarioLogado());
			setCaminhoRelatorio(arquivo.getName());
			setFazerDownload(true);
			setMensagemID("msg_relatorio_ok");
		} catch (Exception e) {
			setFazerDownload(false);
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}

	}
	
	public void preencherTodosRelatorioFacilitador() {
		getFacadeFactory().getRelatorioFinalFacilitadorInterfaceFacade().preencherTodosRelatorioFacilitador((List<RelatorioFinalFacilitadorVO>) getControleConsultaDashboradOtimizado().getListaConsulta());
	}

	public void desmarcarTodosRelatorioFacilitador() {
		getFacadeFactory().getRelatorioFinalFacilitadorInterfaceFacade().desmarcarTodosRelatorioFacilitador((List<RelatorioFinalFacilitadorVO>) getControleConsultaDashboradOtimizado().getListaConsulta());
	}

	public void checarFormatarValoresNota() {
		try {
			Double nota = getRelatorioFinalFacilitadorVO().getNota();
			HistoricoVO historicoVO = new HistoricoVO();
			ConfiguracaoAcademicoVO configuracaoAcademicoVO = new ConfiguracaoAcademicoVO();
				historicoVO = getFacadeFactory().getHistoricoFacade().consultarPorMatriculaPeriodoTurmaDisciplina(getRelatorioFinalFacilitadorVO().getMatriculaperiodoturmadisciplinaVO().getCodigo(), false, false, getUsuarioLogado());
			configuracaoAcademicoVO = getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorChavePrimaria(historicoVO.getConfiguracaoAcademico().getCodigo(), getUsuarioLogado());
			if (nota != null) {
				UtilReflexao.invocarMetodo(historicoVO, "setNota" + getCalendarioRelatorioFinalFacilitadorVO().getVariavelTipoNota(), nota);
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoVO, null, configuracaoAcademicoVO, Integer.parseInt(getCalendarioRelatorioFinalFacilitadorVO().getVariavelTipoNota()));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public CalendarioRelatorioFinalFacilitadorVO getCalendarioRelatorioFinalFacilitadorVO() {
		if(calendarioRelatorioFinalFacilitadorVO == null) {
			calendarioRelatorioFinalFacilitadorVO = new CalendarioRelatorioFinalFacilitadorVO();
		}
		return calendarioRelatorioFinalFacilitadorVO;
	}

	public void setCalendarioRelatorioFinalFacilitadorVO(CalendarioRelatorioFinalFacilitadorVO calendarioRelatorioFinalFacilitadorVO) {
		this.calendarioRelatorioFinalFacilitadorVO = calendarioRelatorioFinalFacilitadorVO;
	}
	
	public RelatorioFinalFacilitadorVO getRelatorioFinalFacilitadorVO() {
		if(relatorioFinalFacilitadorVO == null) {
			relatorioFinalFacilitadorVO = new RelatorioFinalFacilitadorVO();
		}
		return relatorioFinalFacilitadorVO;
	}

	public void setRelatorioFinalFacilitadorVO(RelatorioFinalFacilitadorVO relatorioFinalFacilitadorVO) {
		this.relatorioFinalFacilitadorVO = relatorioFinalFacilitadorVO;
	}
	
	public RelatorioFinalFacilitadorVO getRelatorioFinalFacilitadorFiltroVO() {
		if(relatorioFinalFacilitadorFiltroVO == null) {
			relatorioFinalFacilitadorFiltroVO = new RelatorioFinalFacilitadorVO();
		}
		return relatorioFinalFacilitadorFiltroVO;
	}

	public void setRelatorioFinalFacilitadorFiltroVO(RelatorioFinalFacilitadorVO relatorioFinalFacilitadorFiltroVO) {
		this.relatorioFinalFacilitadorFiltroVO = relatorioFinalFacilitadorFiltroVO;
	}

	public Boolean getApresentarSuspensaoBolsa() {
		if(apresentarSuspensaoBolsa == null) {
			apresentarSuspensaoBolsa = Boolean.FALSE;
		}
		return apresentarSuspensaoBolsa;
	}

	public void setApresentarSuspensaoBolsa(Boolean apresentarSuspensaoBolsa) {
		this.apresentarSuspensaoBolsa = apresentarSuspensaoBolsa;
	}

	public Boolean getApresentarIndeferimento() {
		if(apresentarIndeferimento == null) {
			apresentarIndeferimento = Boolean.FALSE;
		}
		return apresentarIndeferimento;
	}

	public void setApresentarIndeferimento(Boolean apresentarIndeferimento) {
		this.apresentarIndeferimento = apresentarIndeferimento;
	}

	public Boolean getApresentarDeferimento() {
		if(apresentarDeferimento == null) {
			apresentarDeferimento = Boolean.FALSE;
		}
		return apresentarDeferimento;
	}

	public void setApresentarDeferimento(Boolean apresentarDeferimento) {
		this.apresentarDeferimento = apresentarDeferimento;
	}

	public Boolean getApresentarCorrecaoAluno() {
		if(apresentarCorrecaoAluno == null) {
			apresentarCorrecaoAluno = Boolean.FALSE;
		}
		return apresentarCorrecaoAluno;
	}

	public void setApresentarCorrecaoAluno(Boolean apresentarCorrecaoAluno) {
		this.apresentarCorrecaoAluno = apresentarCorrecaoAluno;
	}

	public Boolean getApresentarAnalise() {
		if(apresentarAnalise == null) {
			apresentarAnalise = Boolean.FALSE;
		}
		return apresentarAnalise;
	}

	public void setApresentarAnalise(Boolean apresentarAnalise) {
		this.apresentarAnalise = apresentarAnalise;
	}

	public Date getDataEnvioAnaliseInicio() {
		return dataEnvioAnaliseInicio;
	}

	public void setDataEnvioAnaliseInicio(Date dataEnvioAnaliseInicio) {
		this.dataEnvioAnaliseInicio = dataEnvioAnaliseInicio;
	}

	public Date getDataEnvioAnaliseFim() {
		return dataEnvioAnaliseFim;
	}

	public void setDataEnvioAnaliseFim(Date dataEnvioAnaliseFim) {
		this.dataEnvioAnaliseFim = dataEnvioAnaliseFim;
	}


	public void verificarPermissaoDeferirRelatorio(){
		try {
			if(ControleAcesso.verificarPermissaoFuncionalidadeUsuario("RelatorioFacilitador_PermitirDeferir", getUsuarioLogado())){
				setPermitirDeferir(Boolean.TRUE);
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}		
	}
	
	public void verificarPermissaoPermitirCorrecaoAluno(){
		try {
			if(ControleAcesso.verificarPermissaoFuncionalidadeUsuario("RelatorioFacilitador_PermitirCorrecaoAluno", getUsuarioLogado())){
				setPermitirCorrecaoAluno(Boolean.TRUE);
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}		
	}
	public void verificarPermissaoPermitirAcessoSupervisores(){
		try {
			if(ControleAcesso.verificarPermissaoFuncionalidadeUsuario("RelatorioFacilitador_PermitirAcessoSupervisores", getUsuarioLogado())){
				setPermitirAcessoSupervisores(Boolean.TRUE);
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}		
	}
	
	public void verificarPermissaoIndeferirRelatorio(){
		try {
			if(ControleAcesso.verificarPermissaoFuncionalidadeUsuario("RelatorioFacilitador_PermitirIndeferir", getUsuarioLogado())){
				setPermitirIndeferir(Boolean.TRUE);
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}		
	}
	public void verificarPermissaoPermitirSuspensaoBolsa(){
		try {
			if(ControleAcesso.verificarPermissaoFuncionalidadeUsuario("RelatorioFacilitador_PermitirSuspensaoBolsa", getUsuarioLogado())){
				setPermitirSuspensaoBolsa(Boolean.TRUE);
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}		
	}
	
	public Boolean getVerificarAlunosSemRelatorio() {
		return getControleConsultaDashboradOtimizado().getListaConsulta().stream().allMatch(p ->((SuperVO) p).getNovoObj());
	}
	
	public Boolean getVerificarRenderizacaoDeferimento() {
		return (getRelatorioFinalFacilitadorVO().getSituacao().equals(SituacaoRelatorioFacilitadorEnum.ANALISE_SUPERVISOR) || 
				getRelatorioFinalFacilitadorVO().getSituacao().equals(SituacaoRelatorioFacilitadorEnum.CORRECAO_ALUNO)) && getPermitirDeferir();
	}
	
	public Boolean getVerificarRenderizacaoCorrecao() {
		if(getFacadeFactory().getCalendarioRelatorioFinalFacilitadorInterfaceFacade().verificarCalendarioEmAbertoPorDisciplinaAnoSemestre(
				getRelatorioFinalFacilitadorVO().getMatriculaperiodoturmadisciplinaVO().getDisciplina(), getCalendarioRelatorioFinalFacilitadorVO().getAno(),
				getCalendarioRelatorioFinalFacilitadorVO().getSemestre(), new Date())) {
			return (getRelatorioFinalFacilitadorVO().getSituacao().equals(SituacaoRelatorioFacilitadorEnum.ANALISE_SUPERVISOR) ||
					getRelatorioFinalFacilitadorVO().getSituacao().equals(SituacaoRelatorioFacilitadorEnum.CORRECAO_ALUNO)) && getPermitirCorrecaoAluno();
			}
		return false;
	}
	
	public Boolean getVerificarRenderizacaoIndeferido() {
		return (getRelatorioFinalFacilitadorVO().getSituacao().equals(SituacaoRelatorioFacilitadorEnum.ANALISE_SUPERVISOR) || 
				getRelatorioFinalFacilitadorVO().getSituacao().equals(SituacaoRelatorioFacilitadorEnum.CORRECAO_ALUNO)) && getPermitirIndeferir();
	}
	
	public Boolean getVerificarRenderizacaoSupensaoBolsa() {
		return (getRelatorioFinalFacilitadorVO().getSituacao().equals(SituacaoRelatorioFacilitadorEnum.ANALISE_SUPERVISOR) || 
				getRelatorioFinalFacilitadorVO().getSituacao().equals(SituacaoRelatorioFacilitadorEnum.CORRECAO_ALUNO)) && getPermitirSuspensaoBolsa();
	}
	
	public Boolean getPermitirDeferir() {
		if(permitirDeferir== null) {
			permitirDeferir = Boolean.FALSE;
		}
		return permitirDeferir;
	}

	public void setPermitirDeferir(Boolean permitirDeferir) {
		this.permitirDeferir = permitirDeferir;
	}

	public Boolean getPermitirIndeferir() {
		if(permitirIndeferir== null) {
			permitirIndeferir = Boolean.FALSE;
		}
		return permitirIndeferir;
	}

	public void setPermitirIndeferir(Boolean permitirIndeferir) {
		this.permitirIndeferir = permitirIndeferir;
	}

	public Boolean getPermitirAcessoSupervisores() {
		if(permitirAcessoSupervisores== null) {
			permitirAcessoSupervisores = Boolean.FALSE;
		}
		return permitirAcessoSupervisores;
	}

	public void setPermitirAcessoSupervisores(Boolean permitirAcessoSupervisores) {
		this.permitirAcessoSupervisores = permitirAcessoSupervisores;
	}

	public Boolean getPermitirCorrecaoAluno() {
		if(permitirCorrecaoAluno== null) {
			permitirCorrecaoAluno = Boolean.FALSE;
		}
		return permitirCorrecaoAluno;
	}

	public void setPermitirCorrecaoAluno(Boolean permitirCorrecaoAluno) {
		this.permitirCorrecaoAluno = permitirCorrecaoAluno;
	}

	public Boolean getPermitirSuspensaoBolsa() {
		if(permitirSuspensaoBolsa== null) {
			permitirSuspensaoBolsa = Boolean.FALSE;
		}
		return permitirSuspensaoBolsa;
	}

	public void setPermitirSuspensaoBolsa(Boolean permitirSuspensaoBolsa) {
		this.permitirSuspensaoBolsa = permitirSuspensaoBolsa;
	}


	public SituacaoRelatorioFacilitadorEnum getRenderizacaoSituacaoFacilitador() {
		if(renderizacaoSituacaoFacilitador == null) {
			renderizacaoSituacaoFacilitador = SituacaoRelatorioFacilitadorEnum.NENHUM;
		}
		return renderizacaoSituacaoFacilitador;
	}


	public void setRenderizacaoSituacaoFacilitador(SituacaoRelatorioFacilitadorEnum renderizacaoSituacaoFacilitador) {
		this.renderizacaoSituacaoFacilitador = renderizacaoSituacaoFacilitador;
	}


	public CalendarioRelatorioFinalFacilitadorVO getCalendarioRelatorioFinalFacilitadorFiltroVO() {
		if(calendarioRelatorioFinalFacilitadorFiltroVO == null) {
			calendarioRelatorioFinalFacilitadorFiltroVO = new CalendarioRelatorioFinalFacilitadorVO();
		}
		return calendarioRelatorioFinalFacilitadorFiltroVO;
	}


	public void setCalendarioRelatorioFinalFacilitadorFiltroVO(
			CalendarioRelatorioFinalFacilitadorVO calendarioRelatorioFinalFacilitadorFiltroVO) {
		this.calendarioRelatorioFinalFacilitadorFiltroVO = calendarioRelatorioFinalFacilitadorFiltroVO;
	}

	
}
