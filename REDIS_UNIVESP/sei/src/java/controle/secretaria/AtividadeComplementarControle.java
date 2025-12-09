package controle.secretaria;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.HorarioTurmaDisciplinaProgramadaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.secretaria.AtividadeComplementarMatriculaVO;
import negocio.comuns.secretaria.AtividadeComplementarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

/**
 *
 * @author Carlos
 */
@SuppressWarnings("unchecked")
@Controller("AtividadeComplementarControle")
@Scope("viewScope")
@Lazy
public class AtividadeComplementarControle extends SuperControle {

    private AtividadeComplementarVO atividadeComplementarVO;    
    private Integer codTurmaAgrupada;
    private Integer professor;
    private String codigoAgrupado;
    protected List<SelectItem> listaSelectItemTurma;
    protected List<DisciplinaVO> listaConsultasDisciplinas;
    protected List listaSelectItemDisciplina;
    private String campoConsultaDisciplina;
    private String valorConsultaDisciplina;
    private List listaConsultaDisciplina;
    private String campoConsultaCurso;
    private String valorConsultaCurso;
    private List listaConsultaCurso;
    private List listaConsultaTurma;
    private String campoConsultaTurma;
    private String valorConsultaTurma;
    private List listaSelectItemUnidadeEnsino;
    private List<SelectItem> listaSelectItemDisciplinaVOs;    
    private Boolean consultarAtividadeComplementarProfessor;    
    private AtividadeComplementarMatriculaVO atividadeComplementarMatriculaVOTemp;
    private Boolean marcarTodos;

    public AtividadeComplementarControle() throws Exception {
        setMensagemID("msg_entre_prmconsulta");
        //montarListaSelectItemUnidadeEnsino();
    }

    public String editar() {
    	try {
    		AtividadeComplementarVO obj = (AtividadeComplementarVO) context().getExternalContext().getRequestMap().get("atividadeComplementarItens");
    		getFacadeFactory().getAtividadeComplementarFacade().carregarDados(obj, NivelMontarDados.TODOS, getUsuarioLogado());
    		obj.setNovoObj(Boolean.FALSE);
    		setAtividadeComplementarVO(obj);
    		setConsultarAtividadeComplementarProfessor(false);
    		consultarHoraComplementarAluno();    		
    		setMensagemID("msg_dados_editar");
    		if(getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
    			montarListaSelectItemDisciplina();
    			return Uteis.getCaminhoRedirecionamentoNavegacao("atividadeComplementarForm.xhtml");
    		}
    		montarListaSelectItemDisciplinaTurma(obj.getTurmaVO(), obj.getAno(), obj.getSemestre());
    		return "";
    	}catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			if(!getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
				return Uteis.getCaminhoRedirecionamentoNavegacao("atividadeComplementarCons.xhtml");
			}
			return "";
		}
    }

    @PostConstruct
    public void inicializarAtividadeComplementarVisaoProfessor() {
		if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
			limparMensagem();
			setAtividadeComplementarVO(new AtividadeComplementarVO());			
			getAtividadeComplementarVO().setAno(Uteis.getAnoDataAtual4Digitos());
			getAtividadeComplementarVO().setSemestre(Uteis.getSemestreAtual());			
			montarListaSelectItemTurmaVisaoProfessor();
			getControleConsultaOtimizado().setLimitePorPagina(10);
			getControleConsultaOtimizado().setPage(0);
			getControleConsultaOtimizado().setPaginaAtual(1);
			consultarAtividadeComplementarVisaoProfessor();
		}
    }

    public String novo() throws Exception {
        removerObjetoMemoria(this);
        setAtividadeComplementarVO(new AtividadeComplementarVO());
        setMensagemID("msg_entre_dados");
        return Uteis.getCaminhoRedirecionamentoNavegacao("atividadeComplementarForm.xhtml");
    }

    public void novoVisaoProfessor() throws Exception {        
    	setAtividadeComplementarVO(new AtividadeComplementarVO());			
		getAtividadeComplementarVO().setAno(Uteis.getAnoDataAtual4Digitos());
		getAtividadeComplementarVO().setSemestre(Uteis.getSemestreAtual());			
        setConsultarAtividadeComplementarProfessor(false);        
        setMensagemID("msg_entre_dados");
    }



    public String inicializarConsultar() {
        removerObjetoMemoria(this);        
        getControleConsultaOtimizado().setLimitePorPagina(10);
        getControleConsultaOtimizado().setPage(0);
        getControleConsultaOtimizado().setPaginaAtual(1);
        getControleConsultaOtimizado().getListaConsulta().clear();
        setMensagemID("msg_entre_prmconsulta");
        return Uteis.getCaminhoRedirecionamentoNavegacao("atividadeComplementarCons.xhtml");
    }

    public String persistir() {
        try {
        	executarValidacaoSimulacaoVisaoProfessor();
            getFacadeFactory().getAtividadeComplementarFacade().persistir(atividadeComplementarVO, getLoginControle().getPermissaoAcessoMenuVO().getPermiteLancamentoAtividadeComplementarFutura(), getUsuarioLogado());                        	            
            setMensagemID("msg_dados_gravados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("atividadeComplementarForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("atividadeComplementarForm.xhtml");
        }
    }

    public String excluir() {
        try {
        	executarValidacaoSimulacaoVisaoProfessor();
        	getAtividadeComplementarVO().getRegistroAulaVO().setTurma(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(getAtividadeComplementarVO().getRegistroAulaVO().getTurma().getCodigo(), NivelMontarDados.BASICO, getUsuario()));
            getFacadeFactory().getAtividadeComplementarFacade().excluir(getAtividadeComplementarVO(),  getUsuarioLogado());
            if(getUsuarioLogado().getIsApresentarVisaoProfessor()) {
            	novoVisaoProfessor();
            }else {
            	novo();
            }
            setMensagemID("msg_dados_excluidos");
            return Uteis.getCaminhoRedirecionamentoNavegacao("atividadeComplementarForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("atividadeComplementarForm.xhtml");
        }
    }

//    public void realizarAlteracaoHoraComplementarAluno() {
//        try {
//            getFacadeFactory().getAtividadeComplementarFacade().realizarAlteracaoHoraComplementarAluno(getAtividadeComplementarVO(), getAtividadeComplementarVO().getListaAtividadeComplementarMatriculaVOs(), getAtividadeComplementarVO().getQtdeHoraComplementar(), getAno(), getSemestre(), getAtividadeComplementarVO().getTurmaVO(), getAtividadeComplementarVO().getDisciplinaVO(), getAtividadeComplementarVO().getDataAtividade(), getUsuarioLogado());
//            setMensagemID("msg_dados_gravados");
//        } catch (Exception ex) {
//            setMensagemDetalhada("msg_erro", ex.getMessage());
//        }
//    }
    public void consultarHoraComplementarAluno() {
        try {
        	boolean permitirRealizarLancamentoAlunosPreMatriculados = getUsuarioLogado().getIsApresentarVisaoAdministrativa() ? true : getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(getAtividadeComplementarVO().getTurmaVO().getUnidadeEnsino().getCodigo()).getPermitirProfessorRealizarLancamentoAlunosPreMatriculados();
        	boolean permitirRealizarLancamentoAlunoPendenteFinanceiro = getUsuarioLogado().getIsApresentarVisaoAdministrativa() ? true : getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(getAtividadeComplementarVO().getTurmaVO().getUnidadeEnsino().getCodigo()).getApresentarAlunoPendenteFinanceiroVisaoProfessor();
            getAtividadeComplementarVO().setListaAtividadeComplementarMatriculaVOs(getFacadeFactory().getAtividadeComplementarMatriculaFacade().consultarHoraComplementarAluno(getAtividadeComplementarVO(), getAtividadeComplementarVO().getTurmaVO(), getAtividadeComplementarVO().getDisciplinaVO().getCodigo(), getAtividadeComplementarVO().getAno(), getAtividadeComplementarVO().getSemestre(), permitirRealizarLancamentoAlunosPreMatriculados, permitirRealizarLancamentoAlunoPendenteFinanceiro, false, getUsuarioLogado()));
            setMensagemID("msg_dados_consultados");
        } catch (Exception ex) {
            setMensagemDetalhada("msg_erro", ex.getMessage());
        }
    }

    public List<TurmaVO> consultarTurmaPorProfessor() throws Exception {
    	 return getFacadeFactory().getTurmaFacade().consultarTurmaPorProfessorAnoSemestreNivelDadosCombobox(getUsuarioLogado().getPessoa().getCodigo(), getAtividadeComplementarVO().getSemestre(), getAtividadeComplementarVO().getAno(), "AT", getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado().getVisaoLogar().equals("professor"), true, true);
    }

    public void montarListaSelectItemTurmaVisaoProfessor() {
    	List<Integer> mapAuxiliarSelectItem = new ArrayList<Integer>();
		List<TurmaVO> listaResultado = null;
		Iterator<TurmaVO> i = null;
		try {
			List<SelectItem> obj = new ArrayList<SelectItem>(0);
			listaResultado = consultarTurmaPorProfessor();
			obj.add(new SelectItem(0, ""));
			i = listaResultado.iterator();			
			while (i.hasNext()) {
				TurmaVO turma = (TurmaVO) i.next();
				if(!mapAuxiliarSelectItem.contains(turma.getCodigo())){
					obj.add(new SelectItem(turma.getCodigo(), turma.aplicarRegraNomeCursoApresentarCombobox()));
            		mapAuxiliarSelectItem.add(turma.getCodigo());
				}
			}
			setListaSelectItemTurma(obj);
		} catch (Exception e) {
			setListaSelectItemTurma(new ArrayList<SelectItem>(0));
		} finally {
			Uteis.liberarListaMemoria(listaResultado);
			i = null;
		}
    }

    public void consultarTurmaProfessor() throws Exception {
        try {
            getFacadeFactory().getTurmaFacade().carregarDados(getAtividadeComplementarVO().getTurmaVO(), NivelMontarDados.BASICO, getUsuarioLogado());                       
            if(!Uteis.isAtributoPreenchido(getAtividadeComplementarVO().getSemestre())){
            	getAtividadeComplementarVO().setSemestre(Uteis.getSemestreAtual());
            }
            if(!getAtividadeComplementarVO().getTurmaVO().getSemestral()) {
            	getAtividadeComplementarVO().setSemestre("");
            }
            if(!Uteis.isAtributoPreenchido(getAtividadeComplementarVO().getAno())){
            	getAtividadeComplementarVO().setAno(Uteis.getAnoDataAtual());
            }
            if(getAtividadeComplementarVO().getTurmaVO().getIntegralSemValidarLiberarRegistroAulaEntrePeriodo()) {
            	getAtividadeComplementarVO().setAno("");
            }
            montarListaSelectItemDisciplinaTurma(getAtividadeComplementarVO().getTurmaVO(), getAtividadeComplementarVO().getAno(), getAtividadeComplementarVO().getSemestre());
            setProfessor(getUsuarioLogado().getPessoa().getCodigo());
        } catch (Exception e) {
            getAtividadeComplementarVO().setTurmaVO(new TurmaVO());
            setProfessor(0);
            getListaSelectItemDisciplina().clear();
        }
    }
  
    public void montarListaSelectItemDisciplinaTurma(TurmaVO turmaVO, String ano, String semestre) {        
        try {        	           
            setListaSelectItemDisciplina(UtilSelectItem.getListaSelectItem(consultarDisciplinaProfessorTurma(turmaVO, ano, semestre), "codigo", "nome"));
        } catch (Exception e) {
            setListaSelectItemDisciplina(new ArrayList<SelectItem>(0));
        }
    }

    public List<DisciplinaVO> consultarDisciplinaProfessorTurma(TurmaVO turmaVO, String ano, String semestre) throws Exception {    	
		return getFacadeFactory().getDisciplinaFacade().consultarDisciplinaProfessorTurmaValidandoHorarioTurmaDiaSemestreAtual(getUsuarioLogado().getPessoa().getCodigo(), turmaVO.getCodigo(), ano, semestre, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());		
    }

    public void preencherTodosListaAluno() {
        getFacadeFactory().getAtividadeComplementarFacade().preencherTodosListaAluno(getAtividadeComplementarVO().getListaAtividadeComplementarMatriculaVOs() , getMarcarTodos());
    }

    public void desmarcarTodosListaAluno() {
        getFacadeFactory().getAtividadeComplementarFacade().desmarcarTodosListaAluno(getAtividadeComplementarVO().getListaAtividadeComplementarMatriculaVOs());
    }

    public void montarListaSelectItemUnidadeEnsino() {
        List<UnidadeEnsinoVO> resultadoConsulta = null;
        try {
            resultadoConsulta = consultarUnidadeEnsinoPorNome("");
            setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
        }
    }

    public void limparListaConsulta() {
        getAtividadeComplementarVO().getListaAtividadeComplementarMatriculaVOs().clear();
    }

    private List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
        List<UnidadeEnsinoVO> lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm,
                super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
        return lista;
    }
    
    public void montarListaSelectItemDisciplina() {
    	try {
    		getListaSelectItemDisciplinaVOs().clear();
    		Boolean permiteProgramarAulaDisciplinaOnlineTipoTutoriaDinamica = getLoginControle().getPermissaoAcessoMenuVO().getPermiteProgramarAulaDisciplinaOnlineTipoTutoriaDinamica();
    		List<HorarioTurmaDisciplinaProgramadaVO> horarioTurmaDisciplinaProgramadaVOs = getFacadeFactory().getHorarioTurmaFacade().consultarHorarioTurmaDisciplinaProgramadaPorTurma(getAtividadeComplementarVO().getTurmaVO().getCodigo(), false, permiteProgramarAulaDisciplinaOnlineTipoTutoriaDinamica, 0);
    		for (HorarioTurmaDisciplinaProgramadaVO obj : horarioTurmaDisciplinaProgramadaVOs) {			
    			getListaSelectItemDisciplinaVOs().add(new SelectItem(obj.getCodigoDisciplina(), obj.getNomeDisciplina()));
    		}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
    }

    public String consultar() {
        try {
        	getControleConsultaOtimizado().setLimitePorPagina(10);
            getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getAtividadeComplementarFacade().consultar(getControleConsulta().getCampoConsulta(), getControleConsulta().getValorConsulta(), getAtividadeComplementarVO().getAno(), getAtividadeComplementarVO().getSemestre(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), getUsuarioLogado()));
            getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getAtividadeComplementarFacade().consultarTotalRegistro(getControleConsulta().getCampoConsulta(), getControleConsulta().getValorConsulta(), getAtividadeComplementarVO().getAno(), getAtividadeComplementarVO().getSemestre()));
            setMensagemID("msg_dados_consultados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("atividadeComplementarCons.xhtml");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("atividadeComplementarCons.xhtml");
        }
    }
        
    public void consultarAtividadeComplementarVisaoProfessor() {
        try {
        	if(getUsuarioLogado().getIsApresentarVisaoProfessor()) {
        		if(!getConsultarAtividadeComplementarProfessor()) {
        			novoVisaoProfessor();
        			setConsultarAtividadeComplementarProfessor(true);
        		}
        		getControleConsultaOtimizado().setLimitePorPagina(10);
        		getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getAtividadeComplementarFacade().consultaRapidaPorProfessor(getUsuarioLogado().getPessoa().getCodigo(), getAtividadeComplementarVO().getTurmaVO(), getAtividadeComplementarVO().getDisciplinaVO().getCodigo(), getAtividadeComplementarVO().getAno(), getAtividadeComplementarVO().getSemestre(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), false, getUsuarioLogado()));
        		getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getAtividadeComplementarFacade().consultaTotalPorProfessor(getUsuarioLogado().getPessoa().getCodigo(), getAtividadeComplementarVO().getTurmaVO(), getAtividadeComplementarVO().getDisciplinaVO().getCodigo(), getAtividadeComplementarVO().getAno(), getAtividadeComplementarVO().getSemestre()));
        	}
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
        	getControleConsultaOtimizado().getListaConsulta().clear();
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }
    
    public void scrollerListener(DataScrollEvent DataScrollEvent) throws Exception {
        getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
        getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
        if(getUsuarioLogado().getIsApresentarVisaoProfessor()) {
        	consultarAtividadeComplementarVisaoProfessor();
        }else {
        	consultar();
        }
    }
    
    
    public void selecionarDisciplina() throws Exception {
        try {
            DisciplinaVO obj = (DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplinaItens");
            getAtividadeComplementarVO().setDisciplinaVO(obj);
        } catch (Exception e) {
        }
    }

    public void limparDisciplina() throws Exception {
        try {
            getAtividadeComplementarVO().setDisciplinaVO(null);
            getAtividadeComplementarVO().getListaAtividadeComplementarMatriculaVOs().clear();
        } catch (Exception e) {
        }
    }

    public void consultarTurma() {
        try {
            List objs = new ArrayList(0);
            if (getCampoConsultaTurma().equals("identificadorTurma")) {
                objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaUnidadeEnsinoCursoTurno(getValorConsultaTurma(), getUnidadeEnsinoLogado().getCodigo(), getAtividadeComplementarVO().getTurmaVO().getCurso().getCodigo(), 0, false, getUsuarioLogado());
            }
            setListaConsultaTurma(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaTurma(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarTurma() throws Exception {
        TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
        getAtividadeComplementarVO().setTurmaVO(obj);
        removerObjetoMemoria(getAtividadeComplementarVO().getDisciplinaVO());
        obj = null;
        setValorConsultaTurma(null);
        setCampoConsultaTurma(null);
        montarListaSelectItemDisciplina();
        Uteis.liberarListaMemoria(getListaConsultaTurma());
    }
    private List<SelectItem> tipoConsultaComboTurma;

    public List getTipoConsultaComboTurma() {
        if (tipoConsultaComboTurma == null) {
            tipoConsultaComboTurma = new ArrayList(0);
            tipoConsultaComboTurma.add(new SelectItem("identificadorTurma", "Identificador"));
        }
        return tipoConsultaComboTurma;
    }
    private List<SelectItem> tipoConsultaComboDisciplina;

    public List getTipoConsultaComboDisciplina() {
        if (tipoConsultaComboDisciplina == null) {
            tipoConsultaComboDisciplina = new ArrayList(0);
            tipoConsultaComboDisciplina.add(new SelectItem("nome", "Nome"));
            tipoConsultaComboDisciplina.add(new SelectItem("codigo", "Código"));
        }
        return tipoConsultaComboDisciplina;
    }

//    public void consultarCurso() {
//        try {
//            List objs = new ArrayList(0);
//            if (getCampoConsultaCurso().equals("nome")) {
//                objs = getFacadeFactory().getCursoFacade().consultaRapidaPorNomeEUnidadeDeEnsino(getValorConsultaCurso(),
//                        getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
//            }
//            setListaConsultaCurso(objs);
//            setMensagemID("msg_dados_consultados");
//        } catch (Exception e) {
//            setListaConsultaCurso(new ArrayList(0));
//            setMensagemDetalhada("msg_erro", e.getMessage());
//
//        }
//    }
//
//    public void selecionarCurso() throws Exception {
//        try {
//            CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("curso");
//            setCurso(obj);
//            setDisciplina(null);
//            setTurma(null);
//            setListaConsultaDisciplina(null);
//        } catch (Exception e) {
//        }
//    }
//
//    public void limparCurso() throws Exception {
//        try {
//            setCurso(null);
//            getListaAtividadeComplementarVOs().clear();
//        } catch (Exception e) {
//        }
//    }
    public void limparTurma() throws Exception {
        try {
            getAtividadeComplementarVO().setTurmaVO(null);
            getAtividadeComplementarVO().getListaAtividadeComplementarMatriculaVOs().clear();
        } catch (Exception e) {
        }
    }
    List<SelectItem> tipoConsultaComboCurso;

    public List getTipoConsultaComboCurso() {
        if (tipoConsultaComboCurso == null) {
            tipoConsultaComboCurso = new ArrayList(0);
            tipoConsultaComboCurso.add(new SelectItem("nome", "Nome"));
            tipoConsultaComboCurso.add(new SelectItem("codigo", "Código"));
        }
        return tipoConsultaComboCurso;
    }

    public List getTipoConsultaCombo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("turma", "Turma"));
        itens.add(new SelectItem("disciplina", "Disciplina"));
        return itens;
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
            listaConsultaDisciplina = new ArrayList(0);
        }
        return listaConsultaDisciplina;
    }

    public void setListaConsultaDisciplina(List listaConsultaDisciplina) {
        this.listaConsultaDisciplina = listaConsultaDisciplina;
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

    public List getListaConsultaTurma() {
        if (listaConsultaTurma == null) {
            listaConsultaTurma = new ArrayList(0);
        }
        return listaConsultaTurma;
    }

    public void setListaConsultaTurma(List listaConsultaTurma) {
        this.listaConsultaTurma = listaConsultaTurma;
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

    public List getListaSelectItemUnidadeEnsino() {
        if (listaSelectItemUnidadeEnsino == null) {
            listaSelectItemUnidadeEnsino = new ArrayList(0);
        }
        return listaSelectItemUnidadeEnsino;
    }

    public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
        this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
    }

    public Boolean getApresentarResultadoAtividadeComplementar() {
        return !getAtividadeComplementarVO().getListaAtividadeComplementarMatriculaVOs().isEmpty();
    }

    public List<SelectItem> getListaSelectItemTurma() {
        if (listaSelectItemTurma == null) {
            listaSelectItemTurma = new ArrayList<SelectItem>(0);
        }
        return listaSelectItemTurma;
    }

    public void setListaSelectItemTurma(List<SelectItem> listaSelectItemTurma) {
        this.listaSelectItemTurma = listaSelectItemTurma;
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

    public Integer getCodTurmaAgrupada() {
        if (codTurmaAgrupada == null) {
            codTurmaAgrupada = 0;
        }
        return codTurmaAgrupada;
    }

    public void setCodTurmaAgrupada(Integer codTurmaAgrupada) {
        this.codTurmaAgrupada = codTurmaAgrupada;
    }

    public Integer getProfessor() {
        if (professor == null) {
            professor = 0;
        }
        return professor;
    }

    public void setProfessor(Integer professor) {
        this.professor = professor;
    }

    public String getCodigoAgrupado() {
        if (codigoAgrupado == null) {
            codigoAgrupado = "";
        }
        return codigoAgrupado;
    }

    public void setCodigoAgrupado(String codigoAgrupado) {
        this.codigoAgrupado = codigoAgrupado;
    }

    public List<DisciplinaVO> getListaConsultasDisciplinas() {
        if (listaConsultasDisciplinas == null) {
            listaConsultasDisciplinas = new ArrayList<DisciplinaVO>(0);
        }
        return listaConsultasDisciplinas;
    }

    public void setListaConsultasDisciplinas(List<DisciplinaVO> listaConsultasDisciplinas) {
        this.listaConsultasDisciplinas = listaConsultasDisciplinas;
    }

    public List getListaSelectSemestre() {
        List lista = new ArrayList(0);
        lista.add(new SelectItem("1", "1º"));
        lista.add(new SelectItem("2", "2º"));
        return lista;
    }

    public boolean getIsApresentarAnoSemestre() {
        return !getAtividadeComplementarVO().getTurmaVO().getCurso().getNivelEducacional().equals("PO") && !getAtividadeComplementarVO().getTurmaVO().getCurso().getNivelEducacional().equals("");
    }
    
    public boolean getIsApresentarAno() {
        return Uteis.isAtributoPreenchido(getAtividadeComplementarVO().getTurmaVO()) && !getAtividadeComplementarVO().getTurmaVO().getIntegral();
    }
    
    public boolean getIsApresentarSemestre() {
    	return Uteis.isAtributoPreenchido(getAtividadeComplementarVO().getTurmaVO()) && getAtividadeComplementarVO().getTurmaVO().getSemestral();
    }

    public AtividadeComplementarVO getAtividadeComplementarVO() {
        if (atividadeComplementarVO == null) {
            atividadeComplementarVO = new AtividadeComplementarVO();
        }
        return atividadeComplementarVO;
    }

    public void setAtividadeComplementarVO(AtividadeComplementarVO atividadeComplementarVO) {
        this.atividadeComplementarVO = atividadeComplementarVO;
    }

    public boolean getDesabilitarDisciplina() {
        return !getAtividadeComplementarVO().getCodigo().equals(0);
    }

    public List<SelectItem> getListaSelectItemDisciplinaVOs() {
		if (listaSelectItemDisciplinaVOs == null) {
			listaSelectItemDisciplinaVOs = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemDisciplinaVOs;
	}

	public void setListaSelectItemDisciplinaVOs(List<SelectItem> listaSelectItemDisciplinaVOs) {
		this.listaSelectItemDisciplinaVOs = listaSelectItemDisciplinaVOs;
	}

	public Boolean getConsultarAtividadeComplementarProfessor() {
		if (consultarAtividadeComplementarProfessor == null) {
			consultarAtividadeComplementarProfessor = true;
		}
		return consultarAtividadeComplementarProfessor;
	}

	public void setConsultarAtividadeComplementarProfessor(Boolean consultarAtividadeComplementarProfessor) {
		this.consultarAtividadeComplementarProfessor = consultarAtividadeComplementarProfessor;
	}
	

	public boolean getIsApresentarAnoVisaoProfessorCoordenador() {
		return getAtividadeComplementarVO().getTurmaVO().getCodigo().equals(0) || (getAtividadeComplementarVO().getTurmaVO().getCodigo() > 0 && !getAtividadeComplementarVO().getTurmaVO().getIntegral());
	}
	
	public boolean getIsApresentarSemestreVisaoProfessorCoordenador() {
		return getAtividadeComplementarVO().getTurmaVO().getCodigo().equals(0) || (getAtividadeComplementarVO().getTurmaVO().getCodigo() > 0 && getAtividadeComplementarVO().getTurmaVO().getSemestral());
	}
	
	public boolean getIsDesabilitarAnoVisaoProfessorCoordenador() {
		return !getAtividadeComplementarVO().getListaAtividadeComplementarMatriculaVOs().isEmpty() || (getAtividadeComplementarVO().getTurmaVO().getCodigo() > 0 && !getAtividadeComplementarVO().getTurmaVO().getIntegral())
				|| !getLoginControle().getPermissaoAcessoMenuVO().getPermitirRegistrarAulaRetroativo();
	}
	
	public boolean getIsDesabilitarSemestreVisaoProfessorCoordenador() {
		return !getAtividadeComplementarVO().getListaAtividadeComplementarMatriculaVOs().isEmpty() || (getAtividadeComplementarVO().getTurmaVO().getCodigo() > 0 && getAtividadeComplementarVO().getTurmaVO().getSemestral())
				|| !getLoginControle().getPermissaoAcessoMenuVO().getPermitirRegistrarAulaRetroativo();
	}
	
	public void prepararExclusaoAluno(){
		setAtividadeComplementarMatriculaVOTemp(new AtividadeComplementarMatriculaVO());
		setAtividadeComplementarMatriculaVOTemp((AtividadeComplementarMatriculaVO) context().getExternalContext().getRequestMap().get("atividadeItens"));
	}
	
	public void excluirAluno(){
		getAtividadeComplementarVO().getListaAtividadeComplementarMatriculaVOs().remove(getAtividadeComplementarMatriculaVOTemp());
	}

	public AtividadeComplementarMatriculaVO getAtividadeComplementarMatriculaVOTemp() {
		return atividadeComplementarMatriculaVOTemp;
	}

	public void setAtividadeComplementarMatriculaVOTemp(AtividadeComplementarMatriculaVO atividadeComplementarMatriculaVOTemp) {
		this.atividadeComplementarMatriculaVOTemp = atividadeComplementarMatriculaVOTemp;
	}
    
	public void limparListaAtividadeComplementarMatriculaVOs() {
		try {
			getAtividadeComplementarVO().setListaAtividadeComplementarMatriculaVOs(new ArrayList<>());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public Boolean getMarcarTodos() {
		if (marcarTodos == null) {
			marcarTodos = true;
		}
		return marcarTodos;
	}

	public void setMarcarTodos(Boolean marcarTodos) {
		this.marcarTodos = marcarTodos;
	}
	
	
}
