package controle.academico;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.CursoTurnoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.PeriodoLetivoAtivoUnidadeEnsinoCursoVO;
import negocio.comuns.academico.ProcessoMatriculaCalendarioVO;
import negocio.comuns.academico.ProcessoMatriculaUnidadeEnsinoVO;
import negocio.comuns.academico.ProcessoMatriculaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.academico.enumeradores.NomeTurnoCensoEnum;
import negocio.comuns.academico.enumeradores.TipoAlunoCalendarioMatriculaEnum;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.enumeradores.Obrigatorio;
import negocio.comuns.financeiro.ControleGeracaoParcelaTurmaVO;
import negocio.comuns.financeiro.TextoPadraoVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.DiaSemana;
import negocio.comuns.utilitarias.dominios.RegimeCurso;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import negocio.comuns.utilitarias.dominios.TipoTextoPadrao;
import negocio.facade.jdbc.arquitetura.ControleAcesso;

@Controller("ProcessoMatriculaControle")
@Scope("viewScope")
public class ProcessoMatriculaControle extends SuperControle {

	private static final long serialVersionUID = 1L;
	private ProcessoMatriculaVO processoMatriculaVO;	
	private List<SelectItem> listaSelectItemControleGeracaoParcelaTurma;
	private List<CursoTurnoVO> listaConsultaCurso;
	private String campoConsultaCurso;
	private String valorConsultaCurso;
	private Boolean campoData;
	private Date campoConsultaData;
	private ProcessoMatriculaCalendarioVO processoMatriculaCalendarioVO;
	private String curso_Erro;
	private Boolean editarProcessoCalendarioVO;
	private Boolean apresentarAviso;
	private Boolean apresentarBotaoAtualizarProcessoMatricula;
	private Boolean bloquearSituacaoProcessoMatricula;
	private String situacaoProcessoMatriculaBanco;
	private Integer quantidadePreMatriculas;
	private List<SelectItem> listaSelectItemTextoPadraoContratoRenovacaoOnline;
	private Boolean apresentarDadosBimestre;
	private String apresentarPopAviso;
	private Date dataAlterarProcessoMatricula;
	private ProcessoMatriculaCalendarioVO processoMatriculaCalendarioAlteracaoPeriodoCalendarioVO;
	private Date dataInicioPeriodoMatricula;
	private Date dataFimPeriodoMatricula;
	private Date dataInicioMatriculaOnline;
	private Date dataFimMatriculaOnline;
	private Boolean possuiPermissaoAlterarPeriodoCalendario;
	private Boolean permiteAlterarSituacaoCalendarioMatricula;
	private boolean abrirPainelExcluir = false;
	private List<SelectItem> listaSelectItemDiaSemanaAula;
	private List<SelectItem> listaSelectItemTurnoAula;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private String unidadeEnsinoApresentar;
	private List<SelectItem> listaSelectItemTipoAlunoCalendarioMatricula;
	
	public ProcessoMatriculaControle() throws Exception {
		// obterUsuarioLogado();
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");
		verificarApresentarDadosBimestre();
		verificarApresentarBotoesMatricularRenovar();
		montarListaSelectItemUnidadeEnsino();
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe <code>ProcessoMatricula</code> para edição pelo usuário da aplicação.
	 */
	public String novo() {
		try {
			removerObjetoMemoria(this);
			setApresentarAviso(Boolean.FALSE);
			setProcessoMatriculaVO(new ProcessoMatriculaVO());
			inicializarListasSelectItemTodosComboBox();
			setProcessoMatriculaCalendarioVO(new ProcessoMatriculaCalendarioVO());
			setMensagemID("msg_entre_dados");
			setEditarProcessoCalendarioVO(false);
			setSituacaoProcessoMatriculaBanco(null);
			setApresentarBotaoAtualizarProcessoMatricula(false);
			setPossuiPermissaoAlterarPeriodoCalendario(false);
			listaSelectItemSituacaoProcessoMatricula = null;
			getFacadeFactory().getProcessoMatriculaUnidadeEnsinoFacade().carregarUnidadeEnsinoNaoSelecionado(getProcessoMatriculaVO(), getUnidadeEnsinoLogado().getCodigo());
			return Uteis.getCaminhoRedirecionamentoNavegacao("processoMatriculaForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
        return Uteis.getCaminhoRedirecionamentoNavegacao("processoMatriculaForm.xhtml");
    }

    /**
     * Rotina responsável por disponibilizar os dados de um objeto da classe <code>ProcessoMatricula</code> para alteração. O objeto desta classe é disponibilizado na session da página (request) para
     * que o JSP correspondente possa disponibilizá-lo para edição.
     */
    public String editar() throws Exception {
        ProcessoMatriculaVO obj = (ProcessoMatriculaVO) context().getExternalContext().getRequestMap().get("processoMatriculaItens");
        getFacadeFactory().getProcessoMatriculaFacade().carregarDados(obj, getUsuarioLogado());
        getFacadeFactory().getProcessoMatriculaUnidadeEnsinoFacade().carregarUnidadeEnsinoNaoSelecionado(obj, getUnidadeEnsinoLogado().getCodigo());
        for(ProcessoMatriculaCalendarioVO processoMatriculaCalendarioVO: obj.getProcessoMatriculaCalendarioVOs()) {        
        	processoMatriculaCalendarioVO.montarListaSelectItemPoliticaDivulgacaoMatriculaVOs(getFacadeFactory().getPoliticaDivulgacaoMatriculaOnlineInterfaceFacade().consultarPorCodigoCurso(processoMatriculaCalendarioVO.getCursoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null));
    	}
        //verificarVinculoMatricula(obj);
        setSituacaoProcessoMatriculaBanco(obj.getSituacao());
        obj.setNovoObj(Boolean.FALSE);
        setProcessoMatriculaVO(obj);
        setProcessoMatriculaCalendarioVO(new ProcessoMatriculaCalendarioVO());
        if(!obj.getProcessoMatriculaCalendarioVOs().isEmpty()) {
        	setProcessoMatriculaCalendarioVO(obj.getProcessoMatriculaCalendarioVOs().get(0).clone());
        	getProcessoMatriculaCalendarioVO().setPeriodoLetivoAtivolUnidadeEnsinoCursoVO(obj.getProcessoMatriculaCalendarioVOs().get(0).getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().clone());        	
        	getProcessoMatriculaCalendarioVO().getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().setNovoObj(true);
        	getProcessoMatriculaCalendarioVO().getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().setCodigo(0);
        	getProcessoMatriculaCalendarioVO().setNovoObj(true);
        	getProcessoMatriculaCalendarioVO().setCursoVO(new CursoVO());
        	getProcessoMatriculaCalendarioVO().setTurnoVO(new TurnoVO());
        	getProcessoMatriculaCalendarioVO().getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().setCursoVO(new CursoVO());
        	getProcessoMatriculaCalendarioVO().getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().setTurnoVO(new TurnoVO());
        	
        }
        inicializarListasSelectItemTodosComboBox();
        verificarPermiteAlterarSituacaoCalendarioMatricula();
		if (getProcessoMatriculaVO().getSituacao().equals("FI")) {
			if(!getPermiteAlterarSituacaoCalendarioMatricula()){
				setBloquearSituacaoProcessoMatricula(true);
			}
			setApresentarBotaoAtualizarProcessoMatricula(false);
		} else {
			setBloquearSituacaoProcessoMatricula(false);
		}
        verificarApresentarDadosBimestre();
        verificarApresentarBotoesMatricularRenovar();
        listaSelectItemSituacaoProcessoMatricula = null;
        setMensagemID("msg_dados_editar");
        getProcessoMatriculaVO().setUnidadeEnsinoDescricao(null);
        return Uteis.getCaminhoRedirecionamentoNavegacao("processoMatriculaForm.xhtml");
    }

	// public String alterarSituacaoProcessoMatricula() throws Exception {
	// try {
	// getFacadeFactory().getProcessoMatriculaFacade().alterarSituacaoProcessoMatricula(getProcessoMatriculaVO().getCodigo(),
	// getProcessoMatriculaVO().getSituacao());
	// if (getProcessoMatriculaVO().getSituacao().equals("FI")) {
	// setBloquearSituacaoProcessoMatricula(true);
	// setApresentarBotaoAtualizarProcessoMatricula(false);
	// }
	// } catch (Exception e) {
	// setMensagemDetalhada(e.getMessage());
	// }
	// return "editar";
	// }
	public void apresentarBotaoAtualizarProcesso() throws Exception {
		if (getProcessoMatriculaVO().getSituacao() != null && (getProcessoMatriculaVO().getSituacao().equals("FI") || (getSituacaoProcessoMatriculaBanco().equals("PR") && !getProcessoMatriculaVO().getSituacao().equals("PR")))) {
			setApresentarBotaoAtualizarProcessoMatricula(true);
		} else {
			setApresentarBotaoAtualizarProcessoMatricula(false);
		}
	}

    public void consultarQuantidadePreMatriculas() {
        try {
        	setQuantidadePreMatriculas(getFacadeFactory().getMatriculaPeriodoFacade().consultarQuantidadeAlunosMatriculadosProcessoMatricula(getProcessoMatriculaVO().getCodigo()));
        	if (getProcessoMatriculaVO().getSituacao().equals("AT") && getQuantidadePreMatriculas() > 0) {
        		setApresentarPopAviso("RichFaces.$('panelQuantidadePreMatriculas').show()");
        	} else {
        		setApresentarPopAviso("");
        		gravar();
        	}
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>ProcessoMatricula</code>. Caso o objeto seja novo
	 * (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é acionado o <code>alterar()</code>. Se houver alguma
	 * inconsistência o objeto não é gravado, sendo re-apresentado para o usuário juntamente com uma mensagem de erro.
	 */
	public String gravar() {
		try {
			inicializarDadosDataInicioDataFimProcessoMatriculaCalendarioCursoIntegral();
			if (getProcessoMatriculaVO().isNovoObj().booleanValue()) {
				getFacadeFactory().getProcessoMatriculaFacade().incluir(processoMatriculaVO, getUsuarioLogado());
				verificarApresentarBotoesMatricularRenovar();
			} else {
				getFacadeFactory().getProcessoMatriculaFacade().alterar(processoMatriculaVO, getUsuarioLogado());
				// getFacadeFactory().getProcessoMatriculaFacade().alterarSituacaoProcessoMatricula(getProcessoMatriculaVO(),
				// getUnidadeEnsinoLogado().getCodigo(), getSituacaoProcessoMatriculaBanco(), getConfiguracaoFinanceiroPadraoSistema(),
				// getUsuarioLogado());
				if (getProcessoMatriculaVO().getSituacao().equals("FI") ) {
					if(!getPermiteAlterarSituacaoCalendarioMatricula()){
						setBloquearSituacaoProcessoMatricula(true);
					}
					setApresentarBotaoAtualizarProcessoMatricula(false);
				}
				setSituacaoProcessoMatriculaBanco(getProcessoMatriculaVO().getSituacao());
				// getFacadeFactory().getProcessoMatriculaFacade().alterar(processoMatriculaVO, getListaProcessoMatriculaExcluirPeriodoLetivo(),
				// getUsuarioLogado());
				// getFacadeFactory().getProcessoMatriculaCalendarioFacade().alterarProcessoMatriculaCalendarios(processoMatriculaVO,
				// processoMatriculaVO.getProcessoMatriculaCalendarioVOs(), getUsuarioLogado());
			}
			setMensagemID("msg_dados_gravados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("processoMatriculaForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("processoMatriculaForm.xhtml");
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP ProcessoMatriculaCons.jsp. Define o tipo de consulta a ser executada, por meio
	 * de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como resultado, disponibiliza um List com os objetos selecionados na sessao
	 * da pagina.
	 */
	public String consultar() {
		try {
			super.consultar();
			List<ProcessoMatriculaVO> objs = new ArrayList<ProcessoMatriculaVO>(0);
			if (getControleConsulta().getCampoConsulta().equals("descricao")) {
				objs = getFacadeFactory().getProcessoMatriculaFacade().consultaRapidaPorDescricao(getControleConsulta().getValorConsulta(), super.getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("data")) {
				objs = getFacadeFactory().getProcessoMatriculaFacade().consultaRapidaPorData(getCampoConsultaData(), getCampoConsultaData(), super.getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("dataInicio")) {
				objs = getFacadeFactory().getProcessoMatriculaFacade().consultaRapidaPorDataInicio(getCampoConsultaData(), getCampoConsultaData(), super.getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("dataFinal")) {
				objs = getFacadeFactory().getProcessoMatriculaFacade().consultaRapidaPorDataFinal(getCampoConsultaData(), getCampoConsultaData(), super.getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeUnidadeEnsino")) {
				objs = getFacadeFactory().getProcessoMatriculaFacade().consultaRapidaPorNomeUnidadeEnsino(getControleConsulta().getValorConsulta(), super.getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("processoMatriculaCons.xhtml");
		} catch (Exception e) {
			setListaConsulta(new ArrayList<ProcessoMatriculaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("processoMatriculaCons.xhtml");
		}
	}

	public void consultarCurso() {
		try {
			
			setListaConsultaCurso(getFacadeFactory().getCursoTurnoFacade().consultarCursoTurnoProcessoMatricula(getCampoConsultaCurso(), getValorConsultaCurso(), getProcessoMatriculaVO().getNivelProcessoMatricula(), getProcessoMatriculaVO().getProcessoMatriculaUnidadeEnsinoVOs(), getUnidadeEnsinoLogado().getCodigo()));
			
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList<CursoTurnoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void inicializarDadosCalendario() {
		getFacadeFactory().getProcessoMatriculaCalendarioFacade().inicializarDadosCalendario(getProcessoMatriculaVO(), getProcessoMatriculaCalendarioVO());
		verificarApresentarDadosBimestre();
	}

	public Boolean getSemestral() {
		if (getProcessoMatriculaCalendarioVO().getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().getTipoPeriodoLetivo().equals(RegimeCurso.SEMESTRAL.getValor())) {
			return true;
		}
		return false;
	}

	public Boolean getAnual() {
		if (getProcessoMatriculaCalendarioVO().getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().getTipoPeriodoLetivo().equals(RegimeCurso.ANUAL.getValor())) {
			return true;
		}
		return false;
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe <code>ProcessoMatriculaVO</code> Após a exclusão ela automaticamente aciona a
	 * rotina para uma nova inclusão.
	 */
	public String excluir() {
		try {
			getFacadeFactory().getProcessoMatriculaFacade().excluir(processoMatriculaVO, getUsuarioLogado());
			setProcessoMatriculaVO(new ProcessoMatriculaVO());
			setProcessoMatriculaCalendarioVO(new ProcessoMatriculaCalendarioVO());
			setMensagemID("msg_dados_excluidos");
            return Uteis.getCaminhoRedirecionamentoNavegacao("processoMatriculaForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("processoMatriculaForm.xhtml");
		}
	}

	public void realizarLimpezaListaCurso() {
		getListaConsultaCurso().clear();
		// setProcessoMatriculaCalendarioVO(new ProcessoMatriculaCalendarioVO());
	}

	/**
	 * Método responsável por adicionar um novo objeto da classe <code>ProcessoMatriculaCalendario</code> para o objeto
	 * <code>processoMatriculaVO</code> da classe <code>ProcessoMatricula</code>
	 **/
	public void adicionarProcessoMatriculaCalendario() throws Exception {
		try {
			if (getEditarProcessoCalendarioVO()) {
				getFacadeFactory().getProcessoMatriculaFacade().adicionarObjProcessoMatriculaCalendarioVOs(getProcessoMatriculaVO(), getProcessoMatriculaCalendarioVO(), getEditarProcessoCalendarioVO());
				setProcessoMatriculaCalendarioVO(new ProcessoMatriculaCalendarioVO());
			} else {
				CursoTurnoVO obj = (CursoTurnoVO) context().getExternalContext().getRequestMap().get("cursoTurnoItens");
				ProcessoMatriculaCalendarioVO pmcTemp = getProcessoMatriculaCalendarioVO().clone();				
				pmcTemp.montarListaSelectItemPoliticaDivulgacaoMatriculaVOs(getFacadeFactory().getPoliticaDivulgacaoMatriculaOnlineInterfaceFacade().consultarPorCodigoCurso(obj.getCursoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
				getFacadeFactory().getProcessoMatriculaFacade().adicionarProcessoMatriculaCalendario(getProcessoMatriculaVO(), pmcTemp, obj, getUsuarioLogado());
				getListaConsultaCurso().remove(obj);
			}
			setEditarProcessoCalendarioVO(false);
			this.setValorConsultaCurso("");
			this.setCampoConsultaCurso("");
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void fecharEditarProcessoAlterarData() {
		this.setProcessoMatriculaCalendarioVO(null);
	}

	public void finalizarPeriodoLetivo() {
		try {
			ProcessoMatriculaCalendarioVO obj = this.getProcessoMatriculaCalendarioVO();
			getFacadeFactory().getPeriodoLetivoAtivoUnidadeEnsinoCursoFacade().alterarDataPeriodoLetivoAtivoParaFinalizacao(getFacadeFactory().getPeriodoLetivoAtivoUnidadeEnsinoCursoFacade().consultarPorChavePrimaria(obj.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()), getDataAlterarProcessoMatricula(), getUsuarioLogado());
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void editarProcessoAlterarData() {
		ProcessoMatriculaCalendarioVO obj = (ProcessoMatriculaCalendarioVO) context().getExternalContext().getRequestMap().get("processoMatriculaCalendarioItens");
		this.setProcessoMatriculaCalendarioVO(obj);
	}

	public Boolean getApresentarSemestre() {
		if (getProcessoMatriculaCalendarioVO().getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().getTipoPeriodoLetivo().equals(RegimeCurso.SEMESTRAL.getValor())) {
			return true;
		}
		return false;
	}

	public Boolean getApresentarAno() {
		if (getProcessoMatriculaCalendarioVO().getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().getTipoPeriodoLetivo().equals(RegimeCurso.SEMESTRAL.getValor()) || getProcessoMatriculaCalendarioVO().getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().getTipoPeriodoLetivo().equals(RegimeCurso.ANUAL.getValor())) {
			return true;
		}
		return false;
	}

	public List<SelectItem> getListaSelectItemTipoPeriodoLetivo() {
		List<SelectItem> objs = new ArrayList<SelectItem>(0);
		objs.add(new SelectItem("", ""));
		objs.add(new SelectItem(RegimeCurso.ANUAL.getValor(), "Anual"));
		objs.add(new SelectItem(RegimeCurso.SEMESTRAL.getValor(), "Semestral"));
		objs.add(new SelectItem(RegimeCurso.INTEGRAL.getValor(), "Integral"));
		return objs;
	}

	/**
	 * Método responsável por disponibilizar dados de um objeto da classe <code>ProcessoMatriculaCalendario</code> para edição pelo usuário.
	 **/
	public void editarProcessoMatriculaCalendario() throws Exception {
		getListaConsultaCurso().clear();
		setProcessoMatriculaCalendarioVO(new ProcessoMatriculaCalendarioVO());
		ProcessoMatriculaCalendarioVO obj = (ProcessoMatriculaCalendarioVO) context().getExternalContext().getRequestMap().get("processoMatriculaCalendarioItens");
		CursoTurnoVO cursoTurno = new CursoTurnoVO();
		cursoTurno.setCurso(obj.getCursoVO().getCodigo());
		cursoTurno.setCursoVO(obj.getCursoVO());
		cursoTurno.setTurno(obj.getTurnoVO());		
		setProcessoMatriculaCalendarioVO(obj);
		getFacadeFactory().getProcessoMatriculaCalendarioFacade().excluirObjProcessoMatriculaCalendarioVOs(obj.getCursoVO().getCodigo(), obj.getTurnoVO().getCodigo(), getProcessoMatriculaVO().getProcessoMatriculaCalendarioVOs());
		obj.getCursoVO().setInserirNoProcessoMatricula(true);
		getListaConsultaCurso().add(cursoTurno);
		setEditarProcessoCalendarioVO(true);
		montarListaSelectItemPoliticaDivulgacaoMatriculaOnlineVOs(obj.getCursoVO().getCodigo());
		removerProcessoMatriculaCalendario();
	}

	/**
	 * Método responsável por disponibilizar dados de um objeto da classe <code>ProcessoMatriculaCalendario</code> para edição pelo usuário.
	 **/
	public void visualizarProcessoMatriculaCalendario() throws Exception {

		ProcessoMatriculaCalendarioVO obj = (ProcessoMatriculaCalendarioVO) context().getExternalContext().getRequestMap().get("processoMatriculaCalendarioItens");
		setEditarProcessoCalendarioVO(true);
		setProcessoMatriculaCalendarioVO(obj);
	}

	public void fecharVisualizacaoProcessoMatriculaCalendario() throws Exception {
		setEditarProcessoCalendarioVO(false);
		setProcessoMatriculaCalendarioVO(new ProcessoMatriculaCalendarioVO());
	}

	/**
	 * Método responsável por remover um novo objeto da classe <code>ProcessoMatriculaCalendario</code> do objeto <code>processoMatriculaVO</code> da
	 * classe <code>ProcessoMatricula</code>
	 **/
	public void removerProcessoMatriculaCalendario() throws Exception {
		ProcessoMatriculaCalendarioVO obj = (ProcessoMatriculaCalendarioVO) context().getExternalContext().getRequestMap().get("processoMatriculaCalendarioItens");
		getFacadeFactory().getProcessoMatriculaCalendarioFacade().excluirObjProcessoMatriculaCalendarioVOs(obj.getCursoVO().getCodigo(), obj.getTurnoVO().getCodigo(), getProcessoMatriculaVO().getProcessoMatriculaCalendarioVOs());
		setMensagemID("msg_dados_excluidos");
	}

	public void limparListaCalendarioProcessoMatriculaCurso() {
		getProcessoMatriculaVO().setProcessoMatriculaCalendarioVOs(new ArrayList<ProcessoMatriculaCalendarioVO>(0));
	}

	public List<CursoVO> consultarCursoSuggestionbox(Object event) {
		try {
			return getFacadeFactory().getCursoFacade().consultarPorNome(event.toString(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		} catch (Exception e) {
			return new ArrayList<CursoVO>(0);
		}
	}

	/**
	 * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo <code>situacao</code>
	 **/
	public List<SelectItem> listaSelectItemSituacaoProcessoMatricula;
	@SuppressWarnings("unchecked")
	public List<SelectItem> getListaSelectItemSituacaoProcessoMatricula() throws Exception {
		if(listaSelectItemSituacaoProcessoMatricula == null) {
			listaSelectItemSituacaoProcessoMatricula = new ArrayList<SelectItem>(0);
			listaSelectItemSituacaoProcessoMatricula.add(new SelectItem("", ""));
		int index = 1;
		Hashtable<String, String> situacaoProcessoMatriculas = (Hashtable<String, String>) Dominios.getSituacaoProcessoMatricula();
		Enumeration<String> keys = situacaoProcessoMatriculas.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) situacaoProcessoMatriculas.get(value);
			listaSelectItemSituacaoProcessoMatricula.add(new SelectItem(value, label));
			if (getSituacaoProcessoMatriculaBanco().equals("AT") && value.equals("PR") && !getPermiteAlterarSituacaoCalendarioMatricula()) {
				listaSelectItemSituacaoProcessoMatricula.remove(index);
				index--;
			}
			index++;
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List<SelectItem>) listaSelectItemSituacaoProcessoMatricula, ordenador);
		}
		return listaSelectItemSituacaoProcessoMatricula;
	}
	

	public void montarListaSelectItemTextoPadraoContratoMatricula() throws Exception {
		List<TextoPadraoVO> textoPadraoVORenovacaoOnline = consultarTextoPadraoPorTipo(TipoTextoPadrao.RENOVACAO_ONLINE.getValor());
		setListaSelectItemTextoPadraoContratoRenovacaoOnline(UtilSelectItem.getListaSelectItem(textoPadraoVORenovacaoOnline, "codigo", "descricao"));
	}

	@SuppressWarnings("unchecked")
	public List<TextoPadraoVO> consultarTextoPadraoPorTipo(String nomePrm) throws Exception {
		List<TextoPadraoVO> lista = getFacadeFactory().getTextoPadraoFacade().consultarPorTipoNivelComboBox(nomePrm, null, "", false, getUsuarioLogado());
		return lista;
	}

	public void montarListaSelectItemControleGeracaoParcelaTurma() {
		try {
			@SuppressWarnings("unchecked")
			List<ControleGeracaoParcelaTurmaVO> resultadoConsulta = getFacadeFactory().getControleGeracaoParcelaTurmaFacade().consultarPorNome("", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			setListaSelectItemControleGeracaoParcelaTurma(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
		} catch (Exception e) {
		}
	}

	/**
	 * Método responsável por inicializar a lista de valores ( <code>SelectItem</code>) para todos os ComboBox's.
	 * 
	 * @throws Exception
	 */
	public void inicializarListasSelectItemTodosComboBox() throws Exception {		
		montarListaSelectItemControleGeracaoParcelaTurma();
		montarListaSelectItemTextoPadraoContratoMatricula();
	}

	public void inicializarDadosTipoPeriodoLetivo() {
		getProcessoMatriculaVO().setProcessoMatriculaCalendarioVOs(new ArrayList<ProcessoMatriculaCalendarioVO>(0));
		if (getProcessoMatriculaVO().getNivelProcessoMatricula().equals(TipoNivelEducacional.POS_GRADUACAO.getValor())) {
			getProcessoMatriculaCalendarioVO().setPeriodoLetivoAtivolUnidadeEnsinoCursoVO(new PeriodoLetivoAtivoUnidadeEnsinoCursoVO());
			getProcessoMatriculaCalendarioVO().getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().setTipoPeriodoLetivo("");
			getProcessoMatriculaCalendarioVO().getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().setAnoReferenciaPeriodoLetivo("");
		} else if (getProcessoMatriculaVO().getNivelProcessoMatricula().equals(TipoNivelEducacional.EXTENSAO.getValor())) {
			getProcessoMatriculaCalendarioVO().setPeriodoLetivoAtivolUnidadeEnsinoCursoVO(new PeriodoLetivoAtivoUnidadeEnsinoCursoVO());
			getProcessoMatriculaCalendarioVO().getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().setTipoPeriodoLetivo(RegimeCurso.INTEGRAL.getValor());
			getProcessoMatriculaCalendarioVO().getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().setAnoReferenciaPeriodoLetivo("");
		} else if (getProcessoMatriculaVO().getNivelProcessoMatricula().equals(TipoNivelEducacional.BASICO.getValor()) || getProcessoMatriculaVO().getNivelProcessoMatricula().equals(TipoNivelEducacional.MEDIO.getValor()) || getProcessoMatriculaVO().getNivelProcessoMatricula().equals(TipoNivelEducacional.INFANTIL.getValor())) {
			getProcessoMatriculaCalendarioVO().getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().setTipoPeriodoLetivo(RegimeCurso.ANUAL.getValor());
			inicializarDadosCalendario();
		} else {
			getProcessoMatriculaCalendarioVO().setPeriodoLetivoAtivolUnidadeEnsinoCursoVO(new PeriodoLetivoAtivoUnidadeEnsinoCursoVO());
			getProcessoMatriculaCalendarioVO().getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().setTipoPeriodoLetivo("");
		}
		verificarApresentarDadosBimestre();
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List<SelectItem> tipoConsultaCombo;
	public List<SelectItem> getTipoConsultaCombo() {
		if(tipoConsultaCombo == null) {
			tipoConsultaCombo = new ArrayList<SelectItem>(0);
			tipoConsultaCombo.add(new SelectItem("descricao", "Descrição"));
			tipoConsultaCombo.add(new SelectItem("data", "Data Cadastro"));
			tipoConsultaCombo.add(new SelectItem("dataInicio", "Data Início Matrícula"));
			tipoConsultaCombo.add(new SelectItem("dataFinal", "Data Final Matrícula"));
			tipoConsultaCombo.add(new SelectItem("nomeUnidadeEnsino", "Unidade Ensino"));
		}
		return tipoConsultaCombo;
	}

	public List<SelectItem> tipoConsultaComboCurso;
	public List<SelectItem> getTipoConsultaComboCurso() {
		if(tipoConsultaComboCurso == null) {
			tipoConsultaComboCurso = new ArrayList<SelectItem>(0);
			tipoConsultaComboCurso.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboCurso.add(new SelectItem("codigo", "Código"));
		}
		return tipoConsultaComboCurso;
	}

	public List<SelectItem> getListaSelectItemNivelProcessoMatricula() {
		return UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoNivelEducacional.class, true);
	}

	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setApresentarAviso(Boolean.FALSE);
		getListaConsulta().clear();
		setMensagemID("msg_entre_prmconsulta");
        return Uteis.getCaminhoRedirecionamentoNavegacao("processoMatriculaCons.xhtml");
	}

	public void setarNullNaDataDeVencimento() {
		if (getProcessoMatriculaCalendarioVO().getUsarDataVencimentoDataMatricula()) {
			getProcessoMatriculaCalendarioVO().setDataVencimentoMatricula(null);
		}
	}

	public void setarNullNoMesEAnoDasMensalidades() {
		if (getProcessoMatriculaCalendarioVO().getMesSubsequenteMatricula()) {
			getProcessoMatriculaCalendarioVO().setMesVencimentoPrimeiraMensalidade(null);
			getProcessoMatriculaCalendarioVO().setAnoVencimentoPrimeiraMensalidade(null);
		} else if (getProcessoMatriculaCalendarioVO().getMesDataBaseGeracaoParcelas()) {
			getProcessoMatriculaCalendarioVO().setDiaVencimentoPrimeiraMensalidade(null);
			getProcessoMatriculaCalendarioVO().setMesVencimentoPrimeiraMensalidade(null);
			getProcessoMatriculaCalendarioVO().setAnoVencimentoPrimeiraMensalidade(null);
		}
	}

	public void selecionarOpcaoMesSubSequenteAMatricula() {
		if (getProcessoMatriculaCalendarioVO().getMesSubsequenteMatricula()) {
			getProcessoMatriculaCalendarioVO().setMesDataBaseGeracaoParcelas(false);
			setarNullNoMesEAnoDasMensalidades();
		}
	}

	public void selecionarOpcaoMesDataBaseGeracaoParcelas() {
		if (getProcessoMatriculaCalendarioVO().getMesDataBaseGeracaoParcelas()) {
			getProcessoMatriculaCalendarioVO().setMesSubsequenteMatricula(false);
			setarNullNoMesEAnoDasMensalidades();
		}
	}

	public String getCurso_Erro() {
		return curso_Erro;
	}

	public void setCurso_Erro(String curso_Erro) {
		this.curso_Erro = curso_Erro;
	}

	public ProcessoMatriculaCalendarioVO getProcessoMatriculaCalendarioVO() {
		if (processoMatriculaCalendarioVO == null) {
			processoMatriculaCalendarioVO = new ProcessoMatriculaCalendarioVO();
		}
		return processoMatriculaCalendarioVO;
	}

	public void setProcessoMatriculaCalendarioVO(ProcessoMatriculaCalendarioVO processoMatriculaCalendarioVO) {
		this.processoMatriculaCalendarioVO = processoMatriculaCalendarioVO;
	}

	public ProcessoMatriculaVO getProcessoMatriculaVO() {
		if (processoMatriculaVO == null) {
			processoMatriculaVO = new ProcessoMatriculaVO();
		}
		return processoMatriculaVO;
	}

	public void setProcessoMatriculaVO(ProcessoMatriculaVO processoMatriculaVO) {
		this.processoMatriculaVO = processoMatriculaVO;
	}

	public List<CursoTurnoVO> getListaConsultaCurso() {
		if (listaConsultaCurso == null) {
			listaConsultaCurso = new ArrayList<CursoTurnoVO>(0);
		}
		return listaConsultaCurso;
	}

	public void setListaConsultaCurso(List<CursoTurnoVO> listaConsultaCurso) {
		this.listaConsultaCurso = listaConsultaCurso;
	}

	public String getCampoConsultaCurso() {
		return campoConsultaCurso;
	}

	public void setCampoConsultaCurso(String campoConsultaCurso) {
		this.campoConsultaCurso = campoConsultaCurso;
	}

	public String getValorConsultaCurso() {
		return valorConsultaCurso;
	}

	public void setValorConsultaCurso(String valorConsultaCurso) {
		this.valorConsultaCurso = valorConsultaCurso;
	}

	public boolean getVerificarTipoConsulta() {
		return getControleConsulta().getCampoConsulta().equals("data") || getControleConsulta().getCampoConsulta().equals("dataInicio") || getControleConsulta().getCampoConsulta().equals("dataFinal");
	}

	public Boolean getCampoData() {
		if (campoData == null) {
			campoData = false;
		}
		return campoData;
	}

	public void setCampoData(Boolean campoData) {
		this.campoData = campoData;
	}

	public Date getCampoConsultaData() {
		if (campoConsultaData == null) {
			campoConsultaData = new Date();
		}
		return campoConsultaData;
	}

	public void setCampoConsultaData(Date campoConsultaData) {
		this.campoConsultaData = campoConsultaData;
	}

	public boolean getApresentarCamposDadosBasicos() {
		return !getProcessoMatriculaVO().getNivelProcessoMatricula().equals("");
	}

	public boolean getIsUsarDiaMatricula() {
		if (getProcessoMatriculaCalendarioVO().getUsarDataVencimentoDataMatricula()) {
			return true;
		}
		return false;
	}

	public boolean getIsMesSubsequenteMatricula() {
		if (getProcessoMatriculaCalendarioVO().getMesSubsequenteMatricula()) {
			return true;
		}
		return false;
	}

	public boolean getIsMesDataBaseGeracaoParcelas() {
		if (getProcessoMatriculaCalendarioVO().getMesDataBaseGeracaoParcelas()) {
			return true;
		}
		return false;
	}

	public boolean getIsNivelPosGraduacao() {
		return getProcessoMatriculaVO().getNivelProcessoMatricula().equals(TipoNivelEducacional.POS_GRADUACAO.getValor());
	}

	public boolean getIsNivelExtensao() {
		return (getProcessoMatriculaVO().getNivelProcessoMatricula().equals(TipoNivelEducacional.EXTENSAO.getValor()));
	}

	public Boolean getEditarProcessoCalendarioVO() {
		if (editarProcessoCalendarioVO == null) {
			editarProcessoCalendarioVO = Boolean.FALSE;
		}
		return editarProcessoCalendarioVO;
	}

	public void setEditarProcessoCalendarioVO(Boolean editarProcessoCalendarioVO) {
		this.editarProcessoCalendarioVO = editarProcessoCalendarioVO;
	}

	/**
	 * @return the apresentarAviso
	 */
	public Boolean getApresentarAviso() {
		if (apresentarAviso == null) {
			apresentarAviso = Boolean.FALSE;
		}
		return apresentarAviso;
	}

	/**
	 * @param apresentarAviso
	 *            the apresentarAviso to set
	 */
	public void setApresentarAviso(Boolean apresentarAviso) {
		this.apresentarAviso = apresentarAviso;
	}

	public Boolean getApresentarBotaoAtualizarProcessoMatricula() {
		if (apresentarBotaoAtualizarProcessoMatricula == null) {
			apresentarBotaoAtualizarProcessoMatricula = false;
		}
		return apresentarBotaoAtualizarProcessoMatricula;
	}

	public void setApresentarBotaoAtualizarProcessoMatricula(Boolean apresentarBotaoAtualizarProcessoMatricula) {
		this.apresentarBotaoAtualizarProcessoMatricula = apresentarBotaoAtualizarProcessoMatricula;
	}

	public Boolean getBloquearSituacaoProcessoMatricula() {
		if (bloquearSituacaoProcessoMatricula == null) {
			bloquearSituacaoProcessoMatricula = false;
		}
		return bloquearSituacaoProcessoMatricula;
	}

	public void setBloquearSituacaoProcessoMatricula(Boolean bloquearSituacaoProcessoMatricula) {
		this.bloquearSituacaoProcessoMatricula = bloquearSituacaoProcessoMatricula;
	}

	public String getSituacaoProcessoMatriculaBanco() {
		if (situacaoProcessoMatriculaBanco == null) {
			situacaoProcessoMatriculaBanco = "";
		}
		return situacaoProcessoMatriculaBanco;
	}

	public void setSituacaoProcessoMatriculaBanco(String situacaoProcessoMatriculaBanco) {
		this.situacaoProcessoMatriculaBanco = situacaoProcessoMatriculaBanco;
	}

	public Integer getQuantidadePreMatriculas() {
		if (quantidadePreMatriculas == null) {
			quantidadePreMatriculas = 0;
		}
		return quantidadePreMatriculas;
	}

	public void setQuantidadePreMatriculas(Integer quantidadePreMatriculas) {
		this.quantidadePreMatriculas = quantidadePreMatriculas;
	}

	public Boolean getIsApresentarBotaoGravar() {
		if ((!getApresentarAviso() || getApresentarBotaoAtualizarProcessoMatricula()) && !getSituacaoProcessoMatriculaBanco().equals("PR") && !getBloquearSituacaoProcessoMatricula()) {
			return true;
		}
		return false;
	}

	public Boolean getIsApresentarBotaoAtivarPreMatricula() {
		if ((!getApresentarAviso() || getApresentarBotaoAtualizarProcessoMatricula()) && getSituacaoProcessoMatriculaBanco().equals("PR")) {
			return true;
		}
		return false;
	}

	public Boolean getIsApresentarAviso() {
		if (getApresentarAviso() && !getSituacaoProcessoMatriculaBanco().equals("FI")) {
			return true;
		}
		return false;
	}

	public Boolean getIsApresentarPeriodoMatriculaOnline() {
		return getProcessoMatriculaVO().getApresentarProcessoVisaoAluno();
	}

	/**
	 * @return the listaSelectItemControleGeracaoParcelaTurma
	 */
	public List<SelectItem> getListaSelectItemControleGeracaoParcelaTurma() {
		if (listaSelectItemControleGeracaoParcelaTurma == null) {
			listaSelectItemControleGeracaoParcelaTurma = new ArrayList<SelectItem>();
		}
		return listaSelectItemControleGeracaoParcelaTurma;
	}

	/**
	 * @param listaSelectItemControleGeracaoParcelaTurma
	 *            the listaSelectItemControleGeracaoParcelaTurma to set
	 */
	public void setListaSelectItemControleGeracaoParcelaTurma(List<SelectItem> listaSelectItemControleGeracaoParcelaTurma) {
		this.listaSelectItemControleGeracaoParcelaTurma = listaSelectItemControleGeracaoParcelaTurma;
	}

	public List<SelectItem> getListaSelectItemTextoPadraoContratoRenovacaoOnline() {
		if (listaSelectItemTextoPadraoContratoRenovacaoOnline == null) {
			listaSelectItemTextoPadraoContratoRenovacaoOnline = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemTextoPadraoContratoRenovacaoOnline;
	}

	public void setListaSelectItemTextoPadraoContratoRenovacaoOnline(List<SelectItem> listaSelectItemTextoPadraoContratoRenovacaoOnline) {
		this.listaSelectItemTextoPadraoContratoRenovacaoOnline = listaSelectItemTextoPadraoContratoRenovacaoOnline;
	}

	public Boolean getApresentarDadosBimestre() {
		if (apresentarDadosBimestre == null) {
			apresentarDadosBimestre = false;
		}
		return apresentarDadosBimestre;
	}

	public Boolean getIsApresentarDadosBimestre() {
		return apresentarDadosBimestre;
	}

	public void setApresentarDadosBimestre(Boolean apresentarDadosBimestre) {
		this.apresentarDadosBimestre = apresentarDadosBimestre;
	}

	public void verificarApresentarDadosBimestre() {
		if (getProcessoMatriculaVO().getNivelProcessoMatricula().equals(TipoNivelEducacional.BASICO.getValor()) || getProcessoMatriculaVO().getNivelProcessoMatricula().equals(TipoNivelEducacional.MEDIO.getValor()) || getProcessoMatriculaVO().getNivelProcessoMatricula().equals(TipoNivelEducacional.INFANTIL.getValor())) {
			setApresentarDadosBimestre(true);
		} else {
			setApresentarDadosBimestre(false);
		}
	}

	/**
	 * Método responsável por calcular a quantidade de dias e semanas letivas de cada bimestre de nível fundamental e médio de acordo com a data
	 * selecionada.
	 * 
	 * @throws Exception
	 */
	public void calcularDadosDiaSemanaLetiva() throws Exception {
		try {
			getFacadeFactory().getPeriodoLetivoAtivoUnidadeEnsinoCursoFacade().calcularDadosDiaSemanaLetiva(getProcessoMatriculaCalendarioVO().getPeriodoLetivoAtivolUnidadeEnsinoCursoVO());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void calcularDadosDiaSemanaLetivaAlteracao() throws Exception {
		try {
			getFacadeFactory().getPeriodoLetivoAtivoUnidadeEnsinoCursoFacade().calcularDadosDiaSemanaLetiva(getProcessoMatriculaCalendarioAlteracaoPeriodoCalendarioVO().getPeriodoLetivoAtivolUnidadeEnsinoCursoVO());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Método responsável por calcular a quantidade de dias e semanas letivas de cada bimestre de nível fundamental e médio de acordo com que os dias
	 * é alterado.
	 * 
	 * @throws Exception
	 */
	public void calcularDadosDiaSemanaLetivaQtdeDias() throws Exception {
		try {
			getFacadeFactory().getPeriodoLetivoAtivoUnidadeEnsinoCursoFacade().calcularDadosDiaSemanaLetivaQtdeDias(getProcessoMatriculaCalendarioAlteracaoPeriodoCalendarioVO().getPeriodoLetivoAtivolUnidadeEnsinoCursoVO());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void calcularDadosDiaSemanaLetivaQtdeDiasAlteracao() throws Exception {
		try {
			getFacadeFactory().getPeriodoLetivoAtivoUnidadeEnsinoCursoFacade().calcularDadosDiaSemanaLetivaQtdeDias(getProcessoMatriculaCalendarioVO().getPeriodoLetivoAtivolUnidadeEnsinoCursoVO());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String getApresentarPopAviso() {
		if (apresentarPopAviso == null) {
			apresentarPopAviso = "";
		}
		return apresentarPopAviso;
	}

	public void setApresentarPopAviso(String apresentarPopAviso) {
		this.apresentarPopAviso = apresentarPopAviso;
	}

	public Date getDataAlterarProcessoMatricula() {
		if (dataAlterarProcessoMatricula == null) {
			dataAlterarProcessoMatricula = new Date();
		}
		return dataAlterarProcessoMatricula;
	}

	public void setDataAlterarProcessoMatricula(Date dataAlterarProcessoMatricula) {
		this.dataAlterarProcessoMatricula = dataAlterarProcessoMatricula;
	}

	public ProcessoMatriculaCalendarioVO getProcessoMatriculaCalendarioAlteracaoPeriodoCalendarioVO() {
		if (processoMatriculaCalendarioAlteracaoPeriodoCalendarioVO == null) {
			processoMatriculaCalendarioAlteracaoPeriodoCalendarioVO = new ProcessoMatriculaCalendarioVO();
		}
		return processoMatriculaCalendarioAlteracaoPeriodoCalendarioVO;
	}

	public void setProcessoMatriculaCalendarioAlteracaoPeriodoCalendarioVO(ProcessoMatriculaCalendarioVO processoMatriculaCalendarioAlteracaoPeriodoCalendarioVO) {
		this.processoMatriculaCalendarioAlteracaoPeriodoCalendarioVO = processoMatriculaCalendarioAlteracaoPeriodoCalendarioVO;
	}

	public Date getDataInicioPeriodoMatricula() {
		return dataInicioPeriodoMatricula;
	}

	public void setDataInicioPeriodoMatricula(Date dataInicioPeriodoMatricula) {
		this.dataInicioPeriodoMatricula = dataInicioPeriodoMatricula;
	}

	public Date getDataFimPeriodoMatricula() {
		return dataFimPeriodoMatricula;
	}

	public void setDataFimPeriodoMatricula(Date dataFimPeriodoMatricula) {
		this.dataFimPeriodoMatricula = dataFimPeriodoMatricula;
	}

	public Date getDataInicioMatriculaOnline() {
		return dataInicioMatriculaOnline;
	}

	public void setDataInicioMatriculaOnline(Date dataInicioMatriculaOnline) {
		this.dataInicioMatriculaOnline = dataInicioMatriculaOnline;
	}

	public Date getDataFimMatriculaOnline() {
		return dataFimMatriculaOnline;
	}

	public void setDataFimMatriculaOnline(Date dataFimMatriculaOnline) {
		this.dataFimMatriculaOnline = dataFimMatriculaOnline;
	}

	public void selecionarProcessoMatriculaCalendario() {
		try {
			setProcessoMatriculaCalendarioVO((ProcessoMatriculaCalendarioVO) getRequestMap().get("processoMatriculaCalendarioItens"));
			if (getFacadeFactory().getMatriculaPeriodoFacade().consultarPorCodigoProcessoMatriculaUnidadeEnsinoCurso(getProcessoMatriculaCalendarioVO().getProcessoMatricula(), getProcessoMatriculaCalendarioVO().getCursoVO().getCodigo() , getProcessoMatriculaCalendarioVO().getTurnoVO().getCodigo())) {
				setAbrirPainelExcluir(false);
				throw new Exception(UteisJSF.internacionalizar("msg_processoMatricula_ProcessoMatriculaCalendarioVinculadoMatricula"));
			}
			setAbrirPainelExcluir(true);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void excluirProcessoMatriculaCalendario() {
		try {
			getFacadeFactory().getProcessoMatriculaCalendarioFacade().excluir(getProcessoMatriculaCalendarioVO(), getUsuarioLogado());
			getFacadeFactory().getProcessoMatriculaCalendarioFacade().excluirObjProcessoMatriculaCalendarioVOs(getProcessoMatriculaCalendarioVO().getCursoVO().getCodigo(), getProcessoMatriculaCalendarioVO().getTurnoVO().getCodigo(), getProcessoMatriculaVO().getProcessoMatriculaCalendarioVOs());
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			setAbrirPainelExcluir(false);
		}
	}

	public boolean isAbrirPainelExcluir() {
		return abrirPainelExcluir;
	}

	public void setAbrirPainelExcluir(boolean abrirPainelExcluir) {
		this.abrirPainelExcluir = abrirPainelExcluir;
	}

	private List<SelectItem> listaSelectItemPoliticaDivulgacaoMatriculaVOs;

	public List<SelectItem> getListaSelectItemPoliticaDivulgacaoMatriculaVOs() {
		if(listaSelectItemPoliticaDivulgacaoMatriculaVOs == null) {
			listaSelectItemPoliticaDivulgacaoMatriculaVOs = new ArrayList<SelectItem>();
		}
		return listaSelectItemPoliticaDivulgacaoMatriculaVOs;
	}

	public void setListaSelectItemPoliticaDivulgacaoMatriculaVOs(List<SelectItem> listaSelectItemPoliticaDivulgacaoMatriculaVOs) {
		this.listaSelectItemPoliticaDivulgacaoMatriculaVOs = listaSelectItemPoliticaDivulgacaoMatriculaVOs;
	}
	
	public void montarListaSelectItemPoliticaDivulgacaoMatriculaOnlineVOs(Integer codigoCurso) {
		try {
			setListaSelectItemPoliticaDivulgacaoMatriculaVOs(UtilSelectItem.getListaSelectItem(getFacadeFactory().getPoliticaDivulgacaoMatriculaOnlineInterfaceFacade().consultarPorCodigoCurso(codigoCurso, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()), "codigo", "nome", true));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void alterarAnoReferenciaPeriodoLetivo() {
		getProcessoMatriculaCalendarioVO().getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().setAnoReferenciaPeriodoLetivo(getProcessoMatriculaCalendarioVO().getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().getAnoReferenciaPeriodoLetivo());
	}

	public void realizarAlteracaoPeriodoCalendario() {
		try {
			if (getProcessoMatriculaCalendarioAlteracaoPeriodoCalendarioVO().getAtualizacaoIndividual()) {
				inicializarDadosAlteracaoPeriodoProcessoMatricula();
				getProcessoMatriculaCalendarioAlteracaoPeriodoCalendarioVO().setProcessoMatricula(getProcessoMatriculaVO().getCodigo());
				getFacadeFactory().getProcessoMatriculaCalendarioFacade().realizarAlteracaoPeriodoCalendario(getProcessoMatriculaVO(), getProcessoMatriculaCalendarioAlteracaoPeriodoCalendarioVO(), getUsuarioLogado());
				
			} else {
				inicializarDadosTodosCursosPeriodoCalendarioAposAtualizacao();
				for (ProcessoMatriculaCalendarioVO processoCalendarioVO : getProcessoMatriculaVO().getProcessoMatriculaCalendarioVOs()) {
					getFacadeFactory().getProcessoMatriculaCalendarioFacade().realizarAlteracaoPeriodoCalendario(getProcessoMatriculaVO(), processoCalendarioVO, getUsuarioLogado());				
				}
			}
			
			setMensagemID("msg_acao_realizadaComSucesso");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void inicializarDadosAlteracaoPeriodoProcessoMatricula() {
		if (getDataInicioPeriodoMatricula() != null) {
			getProcessoMatriculaVO().setDataInicio(getDataInicioPeriodoMatricula());
		}
		if (getDataFimPeriodoMatricula() != null) {
			getProcessoMatriculaVO().setDataFinal(getDataFimPeriodoMatricula());
		}
		if (getDataInicioMatriculaOnline() != null) {
			getProcessoMatriculaVO().setDataInicioMatriculaOnline(getDataInicioMatriculaOnline());
		}
		if (getDataFimMatriculaOnline() != null) {
			getProcessoMatriculaVO().setDataFimMatriculaOnline(getDataFimMatriculaOnline());
		}
		
	}
	
	public void inicializarDadosAlteracaoPeriodoProcessoMatriculaCalendario(ProcessoMatriculaCalendarioVO processoMatriculaCalendarioVO) {
		setProcessoMatriculaCalendarioAlteracaoPeriodoCalendarioVO(processoMatriculaCalendarioVO);
	}
	
	public void selecionarProcessoMatriculaCalendarioParaAlterarPeriodoCalendario() {
		ProcessoMatriculaCalendarioVO obj = (ProcessoMatriculaCalendarioVO) context().getExternalContext().getRequestMap().get("processoMatriculaCalendarioItens");
		obj.setAtualizacaoIndividual(Boolean.TRUE);		
		inicializarDadosAlteracaoPeriodoProcessoMatriculaCalendario(obj);
	}
	
	public void inicializarDadosAlteracaoPeriodoCalendario() {
		setProcessoMatriculaCalendarioAlteracaoPeriodoCalendarioVO(null);
		getProcessoMatriculaCalendarioAlteracaoPeriodoCalendarioVO().setAtualizacaoIndividual(Boolean.FALSE);
		if(getProcessoMatriculaVO().getProcessoMatriculaCalendarioVOs().isEmpty()) {
			getProcessoMatriculaCalendarioAlteracaoPeriodoCalendarioVO().getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().setTipoPeriodoLetivo(getProcessoMatriculaCalendarioVO().getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().getTipoPeriodoLetivo());
		}else if(!getProcessoMatriculaVO().getProcessoMatriculaCalendarioVOs().isEmpty()) {
			getProcessoMatriculaCalendarioAlteracaoPeriodoCalendarioVO().getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().setTipoPeriodoLetivo(getProcessoMatriculaVO().getProcessoMatriculaCalendarioVOs().get(0).getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().getTipoPeriodoLetivo());
		}
	}
	
	public void verificarApresentarBotoesMatricularRenovar() throws Exception {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("PermiteAlterarPeriodoCalendario", getUsuarioLogado());
			setPossuiPermissaoAlterarPeriodoCalendario(Boolean.TRUE);
		} catch (Exception e) {
			setPossuiPermissaoAlterarPeriodoCalendario(Boolean.FALSE);
		}
	}
	
	public Boolean getApresentarBotaoAlterarPeriodoCalendario() {
		return !getProcessoMatriculaVO().getCodigo().equals(0) && getPossuiPermissaoAlterarPeriodoCalendario();
	}

	public Boolean getPossuiPermissaoAlterarPeriodoCalendario() {
		if (possuiPermissaoAlterarPeriodoCalendario == null) {
			possuiPermissaoAlterarPeriodoCalendario = Boolean.FALSE;
		}
		return possuiPermissaoAlterarPeriodoCalendario;
	}

	public void setPossuiPermissaoAlterarPeriodoCalendario(Boolean possuiPermissaoAlterarPeriodoCalendario) {
		this.possuiPermissaoAlterarPeriodoCalendario = possuiPermissaoAlterarPeriodoCalendario;
	}
	
	public void inicializarDadosTodosCursosPeriodoCalendarioAposAtualizacao() {
		for (ProcessoMatriculaCalendarioVO procMatriculaCalendarioVO : getProcessoMatriculaVO().getProcessoMatriculaCalendarioVOs()) {
			if (getProcessoMatriculaCalendarioAlteracaoPeriodoCalendarioVO().getDataInicioMatricula() != null) {
	        	procMatriculaCalendarioVO.setDataInicioMatricula(getProcessoMatriculaCalendarioAlteracaoPeriodoCalendarioVO().getDataInicioMatricula());
	        }
	        if (getProcessoMatriculaCalendarioAlteracaoPeriodoCalendarioVO().getDataFinalMatricula() != null) {
	        	procMatriculaCalendarioVO.setDataFinalMatricula(getProcessoMatriculaCalendarioAlteracaoPeriodoCalendarioVO().getDataFinalMatricula());
	        }
	        if (getProcessoMatriculaCalendarioAlteracaoPeriodoCalendarioVO().getDataInicioInclusaoDisciplina() != null) {
	        	procMatriculaCalendarioVO.setDataInicioInclusaoDisciplina(getProcessoMatriculaCalendarioAlteracaoPeriodoCalendarioVO().getDataInicioInclusaoDisciplina());
	        }
	        if (getProcessoMatriculaCalendarioAlteracaoPeriodoCalendarioVO().getDataFinalInclusaoDisciplina() != null) {
	        	procMatriculaCalendarioVO.setDataFinalInclusaoDisciplina(getProcessoMatriculaCalendarioAlteracaoPeriodoCalendarioVO().getDataFinalInclusaoDisciplina());
	        }
	        if (getProcessoMatriculaCalendarioAlteracaoPeriodoCalendarioVO().getDataInicioMatForaPrazo() != null) {
	        	procMatriculaCalendarioVO.setDataInicioMatForaPrazo(getProcessoMatriculaCalendarioAlteracaoPeriodoCalendarioVO().getDataInicioMatForaPrazo());
	        }
	        if (getProcessoMatriculaCalendarioAlteracaoPeriodoCalendarioVO().getDataFinalMatForaPrazo() != null) {
	        	procMatriculaCalendarioVO.setDataFinalMatForaPrazo(getProcessoMatriculaCalendarioAlteracaoPeriodoCalendarioVO().getDataFinalMatForaPrazo());
	        }
	        if (!getProcessoMatriculaCalendarioAlteracaoPeriodoCalendarioVO().getIntegral()) {
	        	 if (getProcessoMatriculaCalendarioAlteracaoPeriodoCalendarioVO().getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().getDataInicioPeriodoLetivo() != null) {
	        		 procMatriculaCalendarioVO.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().setDataInicioPeriodoLetivo(getProcessoMatriculaCalendarioAlteracaoPeriodoCalendarioVO().getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().getDataInicioPeriodoLetivo());
	        	 }
	        	 if (getProcessoMatriculaCalendarioAlteracaoPeriodoCalendarioVO().getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().getDataFimPeriodoLetivo() != null) {
	        		 procMatriculaCalendarioVO.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().setDataFimPeriodoLetivo(getProcessoMatriculaCalendarioAlteracaoPeriodoCalendarioVO().getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().getDataFimPeriodoLetivo());
	        	 }
	        	 if (getProcessoMatriculaCalendarioAlteracaoPeriodoCalendarioVO().getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().getTotalDiaLetivoAno() > 0) {
	        		 procMatriculaCalendarioVO.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().setTotalDiaLetivoAno(getProcessoMatriculaCalendarioAlteracaoPeriodoCalendarioVO().getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().getTotalDiaLetivoAno());
	        	 }
	        	 if (getProcessoMatriculaCalendarioAlteracaoPeriodoCalendarioVO().getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().getTotalSemanaLetivaAno() > 0) {
	        		 procMatriculaCalendarioVO.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().setTotalSemanaLetivaAno(getProcessoMatriculaCalendarioAlteracaoPeriodoCalendarioVO().getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().getTotalSemanaLetivaAno());
	        	 }
	        	 
	        	 if (getProcessoMatriculaCalendarioAlteracaoPeriodoCalendarioVO().getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().getDataInicioPeriodoLetivoPrimeiroBimestre() != null) {
	        		 procMatriculaCalendarioVO.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().setDataInicioPeriodoLetivoPrimeiroBimestre(getProcessoMatriculaCalendarioAlteracaoPeriodoCalendarioVO().getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().getDataInicioPeriodoLetivoPrimeiroBimestre());
	        	 }
	        	 if (getProcessoMatriculaCalendarioAlteracaoPeriodoCalendarioVO().getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().getDataFimPeriodoLetivoPrimeiroBimestre() != null) {
	        		 procMatriculaCalendarioVO.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().setDataFimPeriodoLetivoPrimeiroBimestre(getProcessoMatriculaCalendarioAlteracaoPeriodoCalendarioVO().getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().getDataFimPeriodoLetivoPrimeiroBimestre());
	        	 }
	        	 if (getProcessoMatriculaCalendarioAlteracaoPeriodoCalendarioVO().getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().getQtdeDiaLetivoPrimeiroBimestre() > 0) {
	        		 procMatriculaCalendarioVO.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().setQtdeDiaLetivoPrimeiroBimestre(getProcessoMatriculaCalendarioAlteracaoPeriodoCalendarioVO().getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().getQtdeDiaLetivoPrimeiroBimestre());
	        	 }
	        	 if (getProcessoMatriculaCalendarioAlteracaoPeriodoCalendarioVO().getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().getQtdeSemanaLetivaPrimeiroBimestre() > 0) {
	        		 procMatriculaCalendarioVO.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().setQtdeSemanaLetivaPrimeiroBimestre(getProcessoMatriculaCalendarioAlteracaoPeriodoCalendarioVO().getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().getQtdeSemanaLetivaPrimeiroBimestre());
	        	 }
	        	 
	        	 
	        	 if (getProcessoMatriculaCalendarioAlteracaoPeriodoCalendarioVO().getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().getDataInicioPeriodoLetivoSegundoBimestre() != null) {
	        		 procMatriculaCalendarioVO.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().setDataInicioPeriodoLetivoSegundoBimestre(getProcessoMatriculaCalendarioAlteracaoPeriodoCalendarioVO().getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().getDataInicioPeriodoLetivoSegundoBimestre());
	        	 }
	        	 if (getProcessoMatriculaCalendarioAlteracaoPeriodoCalendarioVO().getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().getDataFimPeriodoLetivoSegundoBimestre() != null) {
	        		 procMatriculaCalendarioVO.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().setDataFimPeriodoLetivoSegundoBimestre(getProcessoMatriculaCalendarioAlteracaoPeriodoCalendarioVO().getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().getDataFimPeriodoLetivoSegundoBimestre());
	        	 }
	        	 if (getProcessoMatriculaCalendarioAlteracaoPeriodoCalendarioVO().getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().getQtdeDiaLetivoSegundoBimestre() > 0) {
	        		 procMatriculaCalendarioVO.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().setQtdeDiaLetivoSegundoBimestre(getProcessoMatriculaCalendarioAlteracaoPeriodoCalendarioVO().getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().getQtdeDiaLetivoSegundoBimestre());
	        	 }
	        	 if (getProcessoMatriculaCalendarioAlteracaoPeriodoCalendarioVO().getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().getQtdeSemanaLetivaSegundoBimestre() > 0) {
	        		 procMatriculaCalendarioVO.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().setQtdeSemanaLetivaSegundoBimestre(getProcessoMatriculaCalendarioAlteracaoPeriodoCalendarioVO().getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().getQtdeSemanaLetivaSegundoBimestre());
	        	 }
	        	 
	        	 
	        	 if (getProcessoMatriculaCalendarioAlteracaoPeriodoCalendarioVO().getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().getDataInicioPeriodoLetivoTerceiroBimestre() != null) {
	        		 procMatriculaCalendarioVO.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().setDataInicioPeriodoLetivoTerceiroBimestre(getProcessoMatriculaCalendarioAlteracaoPeriodoCalendarioVO().getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().getDataInicioPeriodoLetivoTerceiroBimestre());
	        	 }
	        	 if (getProcessoMatriculaCalendarioAlteracaoPeriodoCalendarioVO().getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().getDataFimPeriodoLetivoTerceiroBimestre() != null) {
	        		 procMatriculaCalendarioVO.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().setDataFimPeriodoLetivoTerceiroBimestre(getProcessoMatriculaCalendarioAlteracaoPeriodoCalendarioVO().getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().getDataFimPeriodoLetivoTerceiroBimestre());
	        	 }
	        	 if (getProcessoMatriculaCalendarioAlteracaoPeriodoCalendarioVO().getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().getQtdeDiaLetivoTerceiroBimestre() > 0) {
	        		 procMatriculaCalendarioVO.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().setQtdeDiaLetivoTerceiroBimestre(getProcessoMatriculaCalendarioAlteracaoPeriodoCalendarioVO().getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().getQtdeDiaLetivoTerceiroBimestre());
	        	 }
	        	 if (getProcessoMatriculaCalendarioAlteracaoPeriodoCalendarioVO().getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().getQtdeSemanaLetivaTerceiroBimestre() > 0) {
	        		 procMatriculaCalendarioVO.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().setQtdeSemanaLetivaTerceiroBimestre(getProcessoMatriculaCalendarioAlteracaoPeriodoCalendarioVO().getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().getQtdeSemanaLetivaTerceiroBimestre());
	        	 }
	        	 
	        	 if (getProcessoMatriculaCalendarioAlteracaoPeriodoCalendarioVO().getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().getDataInicioPeriodoLetivoQuartoBimestre() != null) {
	        		 procMatriculaCalendarioVO.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().setDataInicioPeriodoLetivoQuartoBimestre(getProcessoMatriculaCalendarioAlteracaoPeriodoCalendarioVO().getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().getDataInicioPeriodoLetivoQuartoBimestre());
	        	 }
	        	 if (getProcessoMatriculaCalendarioAlteracaoPeriodoCalendarioVO().getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().getDataFimPeriodoLetivoQuartoBimestre() != null) {
	        		 procMatriculaCalendarioVO.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().setDataFimPeriodoLetivoQuartoBimestre(getProcessoMatriculaCalendarioAlteracaoPeriodoCalendarioVO().getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().getDataFimPeriodoLetivoQuartoBimestre());
	        	 }
	        	 if (getProcessoMatriculaCalendarioAlteracaoPeriodoCalendarioVO().getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().getQtdeDiaLetivoQuartoBimestre() > 0) {
	        		 procMatriculaCalendarioVO.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().setQtdeDiaLetivoQuartoBimestre(getProcessoMatriculaCalendarioAlteracaoPeriodoCalendarioVO().getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().getQtdeDiaLetivoQuartoBimestre());
	        	 }
	        	 if (getProcessoMatriculaCalendarioAlteracaoPeriodoCalendarioVO().getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().getQtdeSemanaLetivaQuartoBimestre() > 0) {
	        		 procMatriculaCalendarioVO.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().setQtdeSemanaLetivaQuartoBimestre(getProcessoMatriculaCalendarioAlteracaoPeriodoCalendarioVO().getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().getQtdeSemanaLetivaQuartoBimestre());
	        	 }
	        	 
	        }
		}
	}
	
	public void inicializarDadosRegraPlanoFinanceiroAposMarcacaoAcrescentarPlanoFinanCursoRenovacaoAlemExistentePlanoFinanceiroAluno() {
		if (getProcessoMatriculaCalendarioVO().getAcrescentarDescInstPlanoFinanCursoRenovAlemExistPlanoFinanAluno()) {
			getProcessoMatriculaCalendarioVO().setUtilizarDescInstituicaoPlanoFinanceiroCursoConfiguracaoAtual(Boolean.FALSE);
			getProcessoMatriculaCalendarioVO().setZerarValorDescontoPlanoFinanceiroAluno(Boolean.FALSE);
		}
	}
	
	public void inicializarDadosRegraPlanoFinanceiroAposMarcacaoZerarValorDoDesconto() {
		if (getProcessoMatriculaCalendarioVO().getZerarValorDescontoPlanoFinanceiroAluno()) {
//			getProcessoMatriculaCalendarioVO().setUtilizarDescInstituicaoPlanoFinanceiroCursoConfiguracaoAtual(Boolean.FALSE);
			getProcessoMatriculaCalendarioVO().setAcrescentarDescInstPlanoFinanCursoRenovAlemExistPlanoFinanAluno(Boolean.FALSE);
		}
	}
	
	public void inicializarDadosRegraPlanoFinanceiroAposMarcacaoUtilizarDescontoConfiguracaoAtual() {
		if (getProcessoMatriculaCalendarioVO().getUtilizarDescInstituicaoPlanoFinanceiroCursoConfiguracaoAtual()) {
//			getProcessoMatriculaCalendarioVO().setZerarValorDescontoPlanoFinanceiroAluno(Boolean.FALSE);
			getProcessoMatriculaCalendarioVO().setAcrescentarDescInstPlanoFinanCursoRenovAlemExistPlanoFinanAluno(Boolean.FALSE);
		}
	}
	
	public void inicializarDadosDataInicioDataFimProcessoMatriculaCalendarioCursoIntegral() {
		for (ProcessoMatriculaCalendarioVO processoMatriculaCalendarioVO : getProcessoMatriculaVO().getProcessoMatriculaCalendarioVOs()) {
			if (processoMatriculaCalendarioVO.getCursoVO().getPeriodicidade().equals("IN")) {
				  processoMatriculaCalendarioVO.setDataInicioMatricula(getProcessoMatriculaVO().getDataInicio());
				  processoMatriculaCalendarioVO.setDataFinalMatricula(getProcessoMatriculaVO().getDataFinal());
				  processoMatriculaCalendarioVO.setDataFinalMatForaPrazo(getProcessoMatriculaVO().getDataFinal());
				  processoMatriculaCalendarioVO.setDataInicioMatForaPrazo(getProcessoMatriculaVO().getDataInicio());
				  processoMatriculaCalendarioVO.setDataInicioInclusaoDisciplina(getProcessoMatriculaVO().getDataInicio());
				  processoMatriculaCalendarioVO.setDataFinalInclusaoDisciplina(getProcessoMatriculaVO().getDataFinal());

			}
		}
	}
	
	
	public void verificarPermiteAlterarSituacaoCalendarioMatricula()  {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("PermiteAlterarSituacaoCalendarioMatricula", getUsuarioLogado());
			setPermiteAlterarSituacaoCalendarioMatricula(Boolean.TRUE);
		} catch (Exception e) {
			setPermiteAlterarSituacaoCalendarioMatricula(Boolean.FALSE);
		}
	}

	public Boolean getPermiteAlterarSituacaoCalendarioMatricula() {
		if(permiteAlterarSituacaoCalendarioMatricula == null){
			verificarPermiteAlterarSituacaoCalendarioMatricula();
		}
		return permiteAlterarSituacaoCalendarioMatricula;
	}

	public void setPermiteAlterarSituacaoCalendarioMatricula(Boolean permiteAlterarSituacaoCalendarioMatricula) {
		this.permiteAlterarSituacaoCalendarioMatricula = permiteAlterarSituacaoCalendarioMatricula;
	}

	public void limparReferenciaTextoPadraoTermoAceite() {
		if (!getProcessoMatriculaVO().getApresentarTermoAceite()) {
			getProcessoMatriculaVO().getTextoPadraoContratoRenovacaoOnline().setCodigo(0);
		}
	}
	
	public void marcarTodasUnidadesEnsinoAction() {
		for (ProcessoMatriculaUnidadeEnsinoVO unidade : getProcessoMatriculaVO().getProcessoMatriculaUnidadeEnsinoVOs()) {
			unidade.setSelecionado(getMarcarTodasUnidadeEnsino());
		}
		verificarTodasUnidadesSelecionadas();
	}
	
	public List<SelectItem> getListaSelectItemDiaSemanaAula() {
		if(listaSelectItemDiaSemanaAula == null) {
			listaSelectItemDiaSemanaAula = UtilSelectItem.getListaSelectItemEnum(DiaSemana.values(), Obrigatorio.SIM);
		}
		return listaSelectItemDiaSemanaAula;
	}

	public void setListaSelectItemDiaSemanaAula(List<SelectItem> listaSelectItemDiaSemanaAula) {
		this.listaSelectItemDiaSemanaAula = listaSelectItemDiaSemanaAula;
	}

	public List<SelectItem> getListaSelectItemTurnoAula() {
		if(listaSelectItemTurnoAula == null) {
			listaSelectItemTurnoAula = UtilSelectItem.getListaSelectItemEnum(NomeTurnoCensoEnum.values(), Obrigatorio.SIM);
		}
		return listaSelectItemTurnoAula;
	}

	public void setListaSelectItemTurnoAula(List<SelectItem> listaSelectItemTurnoAula) {
		this.listaSelectItemTurnoAula = listaSelectItemTurnoAula;
	}
	
	public void verificarTodasUnidadesSelecionadas() {
		StringBuilder unidade = new StringBuilder();
		if(getProcessoMatriculaVO().getProcessoMatriculaUnidadeEnsinoVOs().size() > 1) {
			for (ProcessoMatriculaUnidadeEnsinoVO obj : getProcessoMatriculaVO().getProcessoMatriculaUnidadeEnsinoVOs()) {
				if (obj.getSelecionado()) {
					unidade.append(obj.getUnidadeEnsinoVO().getNome().trim()).append("; ");
				}
			}
			getProcessoMatriculaVO().setUnidadeEnsinoDescricao(unidade.toString());
		} else {
			if (!getProcessoMatriculaVO().getProcessoMatriculaUnidadeEnsinoVOs().isEmpty()) {
				if (getProcessoMatriculaVO().getProcessoMatriculaUnidadeEnsinoVOs().get(0).getSelecionado()) {
					setUnidadeEnsinoApresentar(getUnidadeEnsinoVOs().get(0).getNome());
				}
			}
		}
		
	}
	
	public void montarListaSelectItemUnidadeEnsino() {
		try {
			List<UnidadeEnsinoVO> resultadoConsulta = consultarUnidadeEnsinoPorNome("");
			setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	private List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
		List<UnidadeEnsinoVO> lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		return lista;
	}
	
	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		if(listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemUnidadeEnsino;
	}
	
	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}
	
	public String getUnidadeEnsinoApresentar() {
		if(unidadeEnsinoApresentar == null) {
			unidadeEnsinoApresentar = "";
		}
		return unidadeEnsinoApresentar;
	}
	
	public void setUnidadeEnsinoApresentar(String unidadeEnsinoApresentar) {
		this.unidadeEnsinoApresentar = unidadeEnsinoApresentar;
	}

	public List<SelectItem> getListaSelectItemTipoAlunoCalendarioMatricula() {
		if (listaSelectItemTipoAlunoCalendarioMatricula == null) {
			listaSelectItemTipoAlunoCalendarioMatricula = new ArrayList<SelectItem>(0);
			for (TipoAlunoCalendarioMatriculaEnum obj : TipoAlunoCalendarioMatriculaEnum.values()) {
				listaSelectItemTipoAlunoCalendarioMatricula.add(new SelectItem(obj, obj.getValorApresentar()));
			}
		}
		return listaSelectItemTipoAlunoCalendarioMatricula;
	}

}