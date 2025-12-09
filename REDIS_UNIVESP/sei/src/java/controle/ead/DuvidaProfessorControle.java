package controle.ead;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.academico.VisaoAlunoControle;
import controle.academico.VisaoProfessorControle;
import controle.arquitetura.DataModelo;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.ead.DuvidaProfessorInteracaoVO;
import negocio.comuns.ead.DuvidaProfessorVO;
import negocio.comuns.ead.QuadroResumoDuvidaProfessorVO;
import negocio.comuns.ead.enumeradores.SituacaoDuvidaProfessorEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

@Controller("DuvidaProfessorControle")
@Scope("viewScope")
@Lazy
public class DuvidaProfessorControle extends SuperControle {

    private DuvidaProfessorVO duvidaProfessor;
    private DuvidaProfessorInteracaoVO duvidaProfessorInteracao;
    private DataModelo duvidaProfessorAlunoVOs;
    private DataModelo duvidaProfessorFrequentesVOs;
    private DataModelo duvidaProfessorColegasVOs;
    private DataModelo duvidaProfessorProfessorVOs;
    protected List<SelectItem> listaSelectItemDisciplina;
    protected List<SelectItem> listaSelectItemTurma;
    private List<QuadroResumoDuvidaProfessorVO> quadroResumoDuvidaProfessorVOs;
    private String matriculaAluno;
    private Integer disciplina;
    private Integer turma;
    private Integer qtdeNovaDuvida;
    private Integer qtdeAguardandoRespostaAluno;
    private Integer qtdeAguardandoRespostaProfessor;
    private Integer qtdeFinalizada;
    private SituacaoDuvidaProfessorEnum situacaoDuvidaProfessor;
    private TurmaVO turmaVO;
	private String ano;
	private String semestre;
	private MatriculaPeriodoVO matriculaPeriodoVO;

    @PostConstruct
    public void inicializarDados() throws Exception {
    	try {
	        if ((getUsuarioLogado().getIsApresentarVisaoAluno() || getUsuarioLogado().getIsApresentarVisaoPais())) {
	            VisaoAlunoControle visaoAlunoControle = (VisaoAlunoControle) context().getExternalContext().getSessionMap().get("VisaoAlunoControle");
	            setMatriculaAluno(visaoAlunoControle.getMatricula().getMatricula());
	            setDisciplina(visaoAlunoControle.getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina().getCodigo());
	            setTurma(visaoAlunoControle.getMatriculaPeriodoTurmaDisciplinaVO().getTurma().getCodigo());
	            getDuvidaProfessorAlunoVOs().setPage(0);
	            getDuvidaProfessorAlunoVOs().setPaginaAtual(0);
	            getDuvidaProfessorColegasVOs().setPage(0);
	            getDuvidaProfessorColegasVOs().setPaginaAtual(0);
	            getDuvidaProfessorFrequentesVOs().setPage(0);
	            getDuvidaProfessorFrequentesVOs().setPaginaAtual(0);
	            consultarDuvidaProfessorAluno();
	            consultarDuvidaProfessorFrequente();
	            consultarDuvidaProfessorColegas();
	            consultarQuadroResumo();
	            setMatriculaPeriodoVO(getFacadeFactory().getMatriculaPeriodoFacade().consultarPorChavePrimaria(visaoAlunoControle.getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaPeriodo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));
	        }
	        if ((getUsuarioLogado().getIsApresentarVisaoProfessor())) {
            	setAno(Uteis.getAnoDataAtual4Digitos());
            	setSemestre(Uteis.getSemestreAtual());            	
            	consultarTurmasProfessor();
            	MatriculaPeriodoTurmaDisciplinaVO mptd = (MatriculaPeriodoTurmaDisciplinaVO) context().getExternalContext().getSessionMap().get("matriculaPeriodoTurmaDisciplina");
                if(mptd != null){
                    setTurma(mptd.getTurma().getCodigo());
                    montarListaSelectItemDisciplina();
                    setDisciplina(mptd.getDisciplina().getCodigo());
                    context().getExternalContext().getSessionMap().remove("matriculaPeriodoTurmaDisciplina");	
                }
                inicializarDuvidaProfessor();
//                inicializarTelaConsultaDuvidaProfessorVisaoProfessor();
                limparMensagem();
	        }
        } catch (Exception e) {
        	setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void consultarQuadroResumo() {
        try {
            if ((getUsuarioLogado().getIsApresentarVisaoAluno() || getUsuarioLogado().getIsApresentarVisaoPais())) {
                setQuadroResumoDuvidaProfessorVOs(getFacadeFactory().getDuvidaProfessorFacade().consultarResumoDuvidaProfessor(getMatriculaAluno(), getTurma(), getDisciplina(), getUsuarioLogado(),  getAno(), getSemestre()));
            }else if (getUsuarioLogado().getIsApresentarVisaoProfessor()) { 
            	setQuadroResumoDuvidaProfessorVOs(getFacadeFactory().getDuvidaProfessorFacade().consultarResumoDuvidaProfessor(null, getTurma(), getDisciplina(), getUsuarioLogado(), getAno(), getSemestre()));
            }else {
                setQuadroResumoDuvidaProfessorVOs(getFacadeFactory().getDuvidaProfessorFacade().consultarResumoDuvidaProfessor(null, null, getDisciplina(), getUsuarioLogado(),  getAno(), getSemestre()));
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void consultarDuvidaProfessorAluno() {
        try {
            getDuvidaProfessorAlunoVOs().setLimitePorPagina(10);
            getDuvidaProfessorAlunoVOs().setListaConsulta(getFacadeFactory().getDuvidaProfessorFacade().consutar(getMatriculaAluno(), getTurma(), getDisciplina(), getSituacaoDuvidaProfessor(), null, null, false, getUsuarioLogado(), getDuvidaProfessorAlunoVOs().getLimitePorPagina(), getDuvidaProfessorAlunoVOs().getOffset(), getAno(), getSemestre()));
            getDuvidaProfessorAlunoVOs().setTotalRegistrosEncontrados(getFacadeFactory().getDuvidaProfessorFacade().consutarTotalRegistro(getMatriculaAluno(), getTurma(), getDisciplina(), null, null, null, getUsuarioLogado(), getAno(), getSemestre()));
            setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void paginarDuvidaProfessorAluno(DataScrollEvent DataScrollEvent) {
        getDuvidaProfessorAlunoVOs().setPaginaAtual(DataScrollEvent.getPage());
        getDuvidaProfessorAlunoVOs().setPage(DataScrollEvent.getPage());
        consultarDuvidaProfessorAluno();
    }

    public void consultarDuvidaProfessorFrequente() {
        try {
            getDuvidaProfessorFrequentesVOs().setLimitePorPagina(10);
            if(getUsuarioLogado().getIsApresentarVisaoProfessor()){
                getDuvidaProfessorFrequentesVOs().setListaConsulta(getFacadeFactory().getDuvidaProfessorFacade().consutar(
                        null, null, getDisciplina(), null, true, true, false, getUsuarioLogado(), getDuvidaProfessorFrequentesVOs().getLimitePorPagina(), getDuvidaProfessorFrequentesVOs().getOffset(), getAno(), getSemestre()));
                getDuvidaProfessorFrequentesVOs().setTotalRegistrosEncontrados(getFacadeFactory().getDuvidaProfessorFacade().consutarTotalRegistro(null, null, getDisciplina(), null, true, true, getUsuarioLogado(), getAno(), getSemestre()));
            }else{
                getDuvidaProfessorFrequentesVOs().setListaConsulta(getFacadeFactory().getDuvidaProfessorFacade().consutar(
                        getMatriculaAluno(), getTurma(), getDisciplina(), null, true, true, false, getUsuarioLogado(), getDuvidaProfessorFrequentesVOs().getLimitePorPagina(), getDuvidaProfessorFrequentesVOs().getOffset(), getAno(), getSemestre()));
                getDuvidaProfessorFrequentesVOs().setTotalRegistrosEncontrados(getFacadeFactory().getDuvidaProfessorFacade().consutarTotalRegistro(getMatriculaAluno(), getTurma(), getDisciplina(), null, true, true, getUsuarioLogado(), getAno(), getSemestre()));    
            }
            
            
            setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void paginarDuvidaProfessorFrequente(DataScrollEvent DataScrollEvent) {
        getDuvidaProfessorFrequentesVOs().setPaginaAtual(DataScrollEvent.getPage());
        getDuvidaProfessorFrequentesVOs().setPage(DataScrollEvent.getPage());
        consultarDuvidaProfessorFrequente();
    }

    public void consultarDuvidaProfessorColegas() {
        try {
            getDuvidaProfessorColegasVOs().setLimitePorPagina(10);
            getDuvidaProfessorColegasVOs().setListaConsulta(getFacadeFactory().getDuvidaProfessorFacade().consutar(getMatriculaAluno(), getTurma(), getDisciplina(), null, null, true, false, getUsuarioLogado(), getDuvidaProfessorColegasVOs().getLimitePorPagina(), getDuvidaProfessorColegasVOs().getOffset(), getAno(), getSemestre()));
            getDuvidaProfessorColegasVOs().setTotalRegistrosEncontrados(getFacadeFactory().getDuvidaProfessorFacade().consutarTotalRegistro(getMatriculaAluno(), getTurma(), getDisciplina(), null, null, true, getUsuarioLogado(), getAno(), getSemestre()));
            setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void paginarDuvidaProfessorColegas(DataScrollEvent DataScrollEvent) {
        getDuvidaProfessorColegasVOs().setPaginaAtual(DataScrollEvent.getPage());
        getDuvidaProfessorColegasVOs().setPage(DataScrollEvent.getPage());
        consultarDuvidaProfessorColegas();
    }

    public String consultarDuvidaProfessor() {
        try {
            getDuvidaProfessorProfessorVOs().setLimitePorPagina(10);
            //if(Uteis.isAtributoPreenchido(getTurma()) && Uteis.isAtributoPreenchido(getDisciplina())){
            	getDuvidaProfessorProfessorVOs().setListaConsulta(getFacadeFactory().getDuvidaProfessorFacade().consutar(null, getTurma(), getDisciplina(), getSituacaoDuvidaProfessor(), null, null, false, getUsuarioLogado(), getDuvidaProfessorProfessorVOs().getLimitePorPagina(), getDuvidaProfessorProfessorVOs().getOffset(), getAno(), getSemestre()));
                getDuvidaProfessorProfessorVOs().setTotalRegistrosEncontrados(getFacadeFactory().getDuvidaProfessorFacade().consutarTotalRegistro(null, getTurma(), getDisciplina(), getSituacaoDuvidaProfessor(), null, null, getUsuarioLogado(), getAno(), getSemestre()));	
            //}
            setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
            return Uteis.getCaminhoRedirecionamentoNavegacao("duvidaProfessorCons.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
            return "";
        }
    }

    public void paginarDuvidaProfessor(DataScrollEvent DataScrollEvent) {
        getDuvidaProfessorProfessorVOs().setPaginaAtual(DataScrollEvent.getPage());
        getDuvidaProfessorProfessorVOs().setPage(DataScrollEvent.getPage());
        consultarDuvidaProfessor();
    }

    public String consultarDuvidaProfessorFiltroSituacao() {
        try {
            QuadroResumoDuvidaProfessorVO quadroResumoDuvidaProfessorVO = (QuadroResumoDuvidaProfessorVO) context().getExternalContext().getRequestMap().get("resumoItens");
            String caminhoRedirecionamento = "";
   
            if (quadroResumoDuvidaProfessorVO.getQuantidade() > 0) {
                setSituacaoDuvidaProfessor(quadroResumoDuvidaProfessorVO.getSituacaoDuvidaProfessor());
                if(getUsuarioLogado().getIsApresentarVisaoProfessor()){
                    getDuvidaProfessorProfessorVOs().setPage(1);
                    getDuvidaProfessorProfessorVOs().setPaginaAtual(1);
                    consultarDuvidaProfessor();
                    caminhoRedirecionamento = "duvidaProfessorCons.xhtml";
                }else{
                    getDuvidaProfessorAlunoVOs().setPage(1);
                    getDuvidaProfessorAlunoVOs().setPaginaAtual(1);
                    consultarDuvidaProfessorAluno();
                    caminhoRedirecionamento = "duvidaProfessorAlunoCons.xhtml";
                }
                setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
            }
                return Uteis.getCaminhoRedirecionamentoNavegacao(caminhoRedirecionamento);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
            return "";
        }
        
    }

    public void montarListaSelectItemDisciplina() throws Exception {
        try {
            if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
                getListaSelectItemDisciplina().clear();
                List<DisciplinaVO> listaConsultas = null;
                if(Uteis.isAtributoPreenchido(getTurma())) {
	                listaConsultas = getFacadeFactory().getDisciplinaFacade().consultarDisciplinasDoProfessor(getUsuarioLogado().getPessoa().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), null, getTurma(), getSemestre(), getAno(), Uteis.NIVELMONTARDADOS_COMBOBOX, true, false, getUsuarioLogado());
	
	                for (DisciplinaVO obj : listaConsultas) {
	                    getListaSelectItemDisciplina().add(new SelectItem(obj.getCodigo(), obj.getNome()));
	                }
	                if (!listaConsultas.isEmpty()) {
	                    setDisciplina(listaConsultas.get(0).getCodigo());
	                    //inicializarDuvidaProfessor();                    
	                }
	                buscarTurma();
                }
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public String inicializarDuvidaProfessor() {
        getDuvidaProfessorProfessorVOs().setPage(0);
        getDuvidaProfessorProfessorVOs().setPaginaAtual(0);
        getDuvidaProfessorFrequentesVOs().setPage(0);
        getDuvidaProfessorFrequentesVOs().setPaginaAtual(0);
        consultarDuvidaProfessor();
        consultarDuvidaProfessorFrequente();
        consultarQuadroResumo();
        iniciarDadosDuvidaProfessor();
        return Uteis.getCaminhoRedirecionamentoNavegacao("duvidaProfessorCons.xhtml");
    }

	public void montarListaSelectItemTurma() {
		try {
			consultarTurmasProfessor();
		} catch (Exception e) {
			getListaSelectItemTurma().clear();
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public List<TurmaVO> consultarTurmaPorProfessor() throws Exception {
		try {
			return getFacadeFactory().getTurmaFacade().consultarTurmaPorProfessorAnoSemestreTurmaAnteriorNivelDadosCombobox(getUsuarioLogado().getPessoa().getCodigo(), getSemestre(), getAno(), true, "AT", 0, getUsuarioLogado().getVisaoLogar().equals("professor"), false, true, true);
		} catch (Exception e) {
			throw e;
		}
	}

    public String novo() throws Exception {
        setDuvidaProfessor(null);
        setDuvidaProfessorInteracao(null);
        getDuvidaProfessor().setMatricula(getMatriculaAluno());
        getDuvidaProfessor().getTurma().setCodigo(getTurma());
        getDuvidaProfessor().getDisciplina().setCodigo(getDisciplina());
        getDuvidaProfessor().getAluno().setCodigo(getUsuarioLogado().getPessoa().getCodigo());
        getDuvidaProfessor().getAluno().setNome(getUsuarioLogado().getPessoa().getNome());
        getDuvidaProfessor().getAluno().setArquivoImagem(getUsuarioLogado().getPessoa().getArquivoImagem());
        String telaAtual = getNomeTelaAtual();
       if (telaAtual.contains("duvidaProfessorAlunoCons.xhtml")) {
    	   return Uteis.getCaminhoRedirecionamentoNavegacao("duvidaProfessorAlunoForm.xhtml");   
       }
        return Uteis.getCaminhoRedirecionamentoNavegacao("duvidaProfessorForm.xhtml");
    }

    public String editar() {
        try {
            setDuvidaProfessor(null);
            setDuvidaProfessorInteracao(null);
            setDuvidaProfessor((DuvidaProfessorVO) context().getExternalContext().getRequestMap().get("duvidaItens"));
            getDuvidaProfessor().setDuvidaProfessorInteracaoVOs(getFacadeFactory().getDuvidaProfessorInteracaoFacade().consultarPorDuvidaProfessor(getDuvidaProfessor().getCodigo()));
            limparMensagem();
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
        if (getUsuarioLogado().getIsApresentarVisaoAluno()) {
        	return Uteis.getCaminhoRedirecionamentoNavegacao("duvidaProfessorAlunoForm.xhtml");
        } 
        	return Uteis.getCaminhoRedirecionamentoNavegacao("duvidaProfessorForm.xhtml");
    }

    public void registrarDuvidaComoFrequenteTelaConsulta() {
        try {
        	executarValidacaoSimulacaoVisaoProfessor();
        	executarValidacaoSimulacaoVisaoAluno();
            Integer codigoDuvida = ((DuvidaProfessorVO) context().getExternalContext().getRequestMap().get("duvidaItens")).getCodigo();
            getFacadeFactory().getDuvidaProfessorFacade().realizarRegistroDuvidaComoFrequente(codigoDuvida, true);
            ((DuvidaProfessorVO) context().getExternalContext().getRequestMap().get("duvidaItens")).setDuvidaFrequente(true);
            for(DuvidaProfessorVO duv:(List<DuvidaProfessorVO>)getDuvidaProfessorProfessorVOs().getListaConsulta()){
                if(duv.getCodigo().intValue() == codigoDuvida.intValue()){
                    duv.setDuvidaFrequente(true);
                    break;
                }
            }
            consultarDuvidaProfessorFrequente();            
            setMensagemID("msg_duvida_frequente_sucesso", Uteis.SUCESSO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void registrarDuvidaComoNaoFrequenteTelaConsulta() {
        try {
        	executarValidacaoSimulacaoVisaoProfessor();
        	executarValidacaoSimulacaoVisaoAluno();
            Integer codigoDuvida = ((DuvidaProfessorVO) context().getExternalContext().getRequestMap().get("duvidaItens")).getCodigo();
            getFacadeFactory().getDuvidaProfessorFacade().realizarRegistroDuvidaComoFrequente(codigoDuvida, false);
            ((DuvidaProfessorVO) context().getExternalContext().getRequestMap().get("duvidaItens")).setDuvidaFrequente(false);
            for(DuvidaProfessorVO duv:(List<DuvidaProfessorVO>)getDuvidaProfessorProfessorVOs().getListaConsulta()){
                if(duv.getCodigo().intValue() == codigoDuvida.intValue()){
                    duv.setDuvidaFrequente(false);
                    break;
                }
            }
            consultarDuvidaProfessorFrequente();
            setMensagemID("msg_duvida_nao_frequente_sucesso", Uteis.SUCESSO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void registrarDuvidaComoFrequente() {
        try {
        	executarValidacaoSimulacaoVisaoProfessor();
        	executarValidacaoSimulacaoVisaoAluno();
            getFacadeFactory().getDuvidaProfessorFacade().realizarRegistroDuvidaComoFrequente(getDuvidaProfessor().getCodigo(), true);
            getDuvidaProfessor().setDuvidaFrequente(true);
            for(DuvidaProfessorVO duv:(List<DuvidaProfessorVO>)getDuvidaProfessorProfessorVOs().getListaConsulta()){
                if(duv.getCodigo().intValue() == getDuvidaProfessor().getCodigo().intValue()){
                    duv.setDuvidaFrequente(true);
                    break;
                }
            }
            consultarDuvidaProfessorFrequente();            
            setMensagemID("msg_duvida_frequente_sucesso", Uteis.SUCESSO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void registrarDuvidaComoNaoFrequente() {
        try {
        	executarValidacaoSimulacaoVisaoProfessor();
        	executarValidacaoSimulacaoVisaoAluno();
            getFacadeFactory().getDuvidaProfessorFacade().realizarRegistroDuvidaComoFrequente(getDuvidaProfessor().getCodigo(), false);
            getDuvidaProfessor().setDuvidaFrequente(false);
            for(DuvidaProfessorVO duv:(List<DuvidaProfessorVO>)getDuvidaProfessorProfessorVOs().getListaConsulta()){
                if(duv.getCodigo().intValue() == getDuvidaProfessor().getCodigo().intValue()){
                    duv.setDuvidaFrequente(false);
                    break;
                }
            }
            consultarDuvidaProfessorFrequente();
            for(DuvidaProfessorVO duv:(List<DuvidaProfessorVO>)getDuvidaProfessorFrequentesVOs().getListaConsulta()){
                if(duv.getCodigo().intValue() == getDuvidaProfessor().getCodigo().intValue()){
                    consultarDuvidaProfessorFrequente();
                    break;
                }
            }
            setMensagemID("msg_duvida_nao_frequente_sucesso", Uteis.SUCESSO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void validarCompartilharDuvida() {
    	if (Uteis.isAtributoPreenchido(getDuvidaProfessor())) {
    		persistir();
    	}
    }

    public void persistir() {
        try {
        	executarValidacaoSimulacaoVisaoProfessor();
        	executarValidacaoSimulacaoVisaoAluno();
            getFacadeFactory().getDuvidaProfessorFacade().persistir(getDuvidaProfessor(), true, getUsuarioLogado(), getIdEntidade());
            getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemNotificacaoNovaDuvidaProfessorCriada(getDuvidaProfessor().getCodigo(), getUsuarioLogado());
            consultarDuvidaProfessorAluno();
            setMensagemID("msg_interacaoDuvidaProfessor_adicionada", Uteis.SUCESSO);
        } catch (ConsistirException consistirException) {
        	setConsistirExceptionMensagemDetalhada("msg_erro", consistirException, Uteis.ERRO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public String getIdEntidade() {
        return getUsuarioLogado().getIsApresentarVisaoAluno() || getUsuarioLogado().getIsApresentarVisaoPais() ? "DuvidaProfessorAluno" : "DuvidaProfessor";
    }

    public void finalizarDuvida() {
        try {
        	executarValidacaoSimulacaoVisaoProfessor();
        	executarValidacaoSimulacaoVisaoAluno();
            getFacadeFactory().getDuvidaProfessorFacade().finalizarDuvidaProfessor(getDuvidaProfessor(), getUsuarioLogado());
            getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemNotificacaoFinalizacaoDuvidaProfessor(getDuvidaProfessor().getCodigo(), getUsuarioLogado());
            consultarQuadroResumo();
            setMensagemID("msg_duvidaProfessor_finalizada", Uteis.SUCESSO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }

    }

    public void incluirDuvidaProfessorInteracao() {
        try {
        	executarValidacaoSimulacaoVisaoProfessor();
        	executarValidacaoSimulacaoVisaoAluno();
            getFacadeFactory().getDuvidaProfessorFacade().incluirDuvidaProfessorInteracao(getDuvidaProfessor(), getDuvidaProfessorInteracao(), getUsuarioLogado());
            setDuvidaProfessorInteracao(new DuvidaProfessorInteracaoVO());
            if(getUsuarioLogado().getIsApresentarVisaoProfessor()){
                VisaoProfessorControle visaoProfessorControle=(VisaoProfessorControle) getControlador("VisaoProfessorControle");
                if(visaoProfessorControle != null && visaoProfessorControle.getQtdeAtualizacaoDuvidaProfessor()>0){
                    visaoProfessorControle.setQtdeAtualizacaoDuvidaProfessor(visaoProfessorControle.getQtdeAtualizacaoDuvidaProfessor()-1);
                }
            }else if(getUsuarioLogado().getIsApresentarVisaoAluno() || getUsuarioLogado().getIsApresentarVisaoPais()){
                VisaoAlunoControle visaoAlunoControle=(VisaoAlunoControle) getControlador("VisaoAlunoControle");
                if(visaoAlunoControle != null && visaoAlunoControle.getQtdeAtualizacaoDuvidaProfessor()>0){
                    visaoAlunoControle.setQtdeAtualizacaoDuvidaProfessor(visaoAlunoControle.getQtdeAtualizacaoDuvidaProfessor()-1);
                }
            }
            consultarQuadroResumo();
            setMensagemID("msg_interacaoDuvidaProfessor_adicionada", Uteis.SUCESSO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public DuvidaProfessorVO getDuvidaProfessor() {
        if (duvidaProfessor == null) {
            duvidaProfessor = new DuvidaProfessorVO();
        }
        return duvidaProfessor;
    }

    public void setDuvidaProfessor(DuvidaProfessorVO duvidaProfessor) {
        this.duvidaProfessor = duvidaProfessor;
    }

    public DuvidaProfessorInteracaoVO getDuvidaProfessorInteracao() {
        if (duvidaProfessorInteracao == null) {
            duvidaProfessorInteracao = new DuvidaProfessorInteracaoVO();
        }
        return duvidaProfessorInteracao;
    }

    public void setDuvidaProfessorInteracao(DuvidaProfessorInteracaoVO duvidaProfessorInteracao) {
        this.duvidaProfessorInteracao = duvidaProfessorInteracao;
    }

    public DataModelo getDuvidaProfessorAlunoVOs() {
        if (duvidaProfessorAlunoVOs == null) {
            duvidaProfessorAlunoVOs = new DataModelo();
        }
        return duvidaProfessorAlunoVOs;
    }

    public void setDuvidaProfessorAlunoVOs(DataModelo duvidaProfessorAlunoVOs) {
        this.duvidaProfessorAlunoVOs = duvidaProfessorAlunoVOs;
    }

    public DataModelo getDuvidaProfessorFrequentesVOs() {
        if (duvidaProfessorFrequentesVOs == null) {
            duvidaProfessorFrequentesVOs = new DataModelo();
        }
        return duvidaProfessorFrequentesVOs;
    }

    public void setDuvidaProfessorFrequentesVOs(DataModelo duvidaProfessorFrequentesVOs) {
        this.duvidaProfessorFrequentesVOs = duvidaProfessorFrequentesVOs;
    }

    public DataModelo getDuvidaProfessorColegasVOs() {
        if (duvidaProfessorColegasVOs == null) {
            duvidaProfessorColegasVOs = new DataModelo();
        }
        return duvidaProfessorColegasVOs;
    }

    public void setDuvidaProfessorColegasVOs(DataModelo duvidaProfessorColegasVOs) {
        this.duvidaProfessorColegasVOs = duvidaProfessorColegasVOs;
    }

    public DataModelo getDuvidaProfessorProfessorVOs() {
        if (duvidaProfessorProfessorVOs == null) {
            duvidaProfessorProfessorVOs = new DataModelo();
        }
        return duvidaProfessorProfessorVOs;
    }

    public void setDuvidaProfessorProfessorVOs(DataModelo duvidaProfessorProfessorVOs) {
        this.duvidaProfessorProfessorVOs = duvidaProfessorProfessorVOs;
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

    public List<SelectItem> getListaSelectItemTurma() {
        if (listaSelectItemTurma == null) {
            listaSelectItemTurma = new ArrayList<SelectItem>(0);
        }
        return listaSelectItemTurma;
    }

    public void setListaSelectItemTurma(List<SelectItem> listaSelectItemTurma) {
        this.listaSelectItemTurma = listaSelectItemTurma;
    }

    public String getMatriculaAluno() {
        if (matriculaAluno == null) {
            matriculaAluno = "";
        }
        return matriculaAluno;
    }

    public void setMatriculaAluno(String matriculaAluno) {
        this.matriculaAluno = matriculaAluno;
    }

    public Integer getDisciplina() {
        if (disciplina == null) {
            disciplina = 0;
        }
        return disciplina;
    }

    public void setDisciplina(Integer disciplina) {
        this.disciplina = disciplina;
    }

    public Integer getTurma() {
        if (turma == null) {
            turma = 0;
        }
        return turma;
    }

    public void setTurma(Integer turma) {
        this.turma = turma;
    }

    public Boolean getPermiteRealizarInteracao() {
        return !getDuvidaProfessor().isNovoObj() && ((getUsuarioLogado().getIsApresentarVisaoProfessor() && !getDuvidaProfessor().getSituacaoDuvidaProfessor().equals(SituacaoDuvidaProfessorEnum.FINALIZADA))
                || ((getUsuarioLogado().getIsApresentarVisaoAluno() || getUsuarioLogado().getIsApresentarVisaoPais())
                && !getDuvidaProfessor().getSituacaoDuvidaProfessor().equals(SituacaoDuvidaProfessorEnum.FINALIZADA)
                && getDuvidaProfessor().getAluno().getCodigo().intValue() == getUsuarioLogado().getPessoa().getCodigo().intValue()));

    }

    public Integer getQtdeNovaDuvida() {
        if (qtdeNovaDuvida == null) {
            qtdeNovaDuvida = 0;
        }
        return qtdeNovaDuvida;
    }

    public void setQtdeNovaDuvida(Integer qtdeNovaDuvida) {
        this.qtdeNovaDuvida = qtdeNovaDuvida;
    }

    public Integer getQtdeAguardandoRespostaAluno() {
        if (qtdeAguardandoRespostaAluno == null) {
            qtdeAguardandoRespostaAluno = 0;
        }
        return qtdeAguardandoRespostaAluno;
    }

    public void setQtdeAguardandoRespostaAluno(Integer qtdeAguardandoRespostaAluno) {
        this.qtdeAguardandoRespostaAluno = qtdeAguardandoRespostaAluno;
    }

    public Integer getQtdeAguardandoRespostaProfessor() {
        if (qtdeAguardandoRespostaProfessor == null) {
            qtdeAguardandoRespostaProfessor = 0;
        }
        return qtdeAguardandoRespostaProfessor;
    }

    public void setQtdeAguardandoRespostaProfessor(Integer qtdeAguardandoRespostaProfessor) {
        this.qtdeAguardandoRespostaProfessor = qtdeAguardandoRespostaProfessor;
    }

    public Integer getQtdeFinalizada() {
        if (qtdeFinalizada == null) {
            qtdeFinalizada = 0;
        }
        return qtdeFinalizada;
    }

    public void setQtdeFinalizada(Integer qtdeFinalizada) {
        this.qtdeFinalizada = qtdeFinalizada;
    }

    public SituacaoDuvidaProfessorEnum getSituacaoDuvidaProfessor() {
        if(situacaoDuvidaProfessor == null){
            situacaoDuvidaProfessor = SituacaoDuvidaProfessorEnum.TODAS;
        }
        return situacaoDuvidaProfessor;
    }

    public void setSituacaoDuvidaProfessor(SituacaoDuvidaProfessorEnum situacaoDuvidaProfessor) {
        this.situacaoDuvidaProfessor = situacaoDuvidaProfessor;
    }

    public List<QuadroResumoDuvidaProfessorVO> getQuadroResumoDuvidaProfessorVOs() {
        if (quadroResumoDuvidaProfessorVOs == null) {
            quadroResumoDuvidaProfessorVOs = new ArrayList<QuadroResumoDuvidaProfessorVO>(0);
        }
        return quadroResumoDuvidaProfessorVOs;
    }

    public void setQuadroResumoDuvidaProfessorVOs(List<QuadroResumoDuvidaProfessorVO> quadroResumoDuvidaProfessorVOs) {
        this.quadroResumoDuvidaProfessorVOs = quadroResumoDuvidaProfessorVOs;
    }
    
    public String getCaminhoBaseFoto() {
        try {
            return getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo();
        } catch (Exception e) {
            return "resources/imagens/visao/foto_usuario.png";
        }
    }
    
    public String voltar() throws Exception {
    	inicializarDados();
    	if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
    		consultarDuvidaProfessor();
    	}
    	return Uteis.getCaminhoRedirecionamentoNavegacao("duvidaProfessorAlunoCons.xhtml");   
    }
    
	public String inicializarTelaConsultaDuvidaProfessorVisaoProfessor() {
		setTurma(0);
		setDisciplina(0);
		setTurmaVO(new TurmaVO());
		getListaSelectItemDisciplina().clear();
		consultarQuadroResumo();
		if (getQuadroResumoDuvidaProfessorVOs().stream().anyMatch(p-> p.getSituacaoDuvidaProfessor().equals(SituacaoDuvidaProfessorEnum.PENDENCIAS) && !p.getQuantidade().equals(0))) {
			setSituacaoDuvidaProfessor(SituacaoDuvidaProfessorEnum.PENDENCIAS);	
		}else {
			setSituacaoDuvidaProfessor(SituacaoDuvidaProfessorEnum.TODAS);
		}
		consultarDuvidaProfessor();
		
		
		return Uteis.getCaminhoRedirecionamentoNavegacao("duvidaProfessorCons.xhtml");
	}
	
	public void listarTodasDuvidasNaoRespondidas() {
		try {
			setSituacaoDuvidaProfessor(SituacaoDuvidaProfessorEnum.PENDENCIAS);
//			getDuvidaProfessorProfessorVOs().setListaConsulta(getFacadeFactory().getDuvidaProfessorFacade().consultarAtualizacaoDuvidaPorUsuarioProfessor(getUsuarioLogado(), getUsuarioLogado().getUnidadeEnsinoLogado().getCodigo(), getAno(), getSemestre()));
//			getDuvidaProfessorProfessorVOs().setTotalRegistrosEncontrados(getFacadeFactory().getDuvidaProfessorFacade().consultarTotalRegistroAtualizacaoDuvidaPorProfessor(getUsuarioLogado(), getUsuarioLogado().getUnidadeEnsinoLogado().getCodigo(), getAno(), getSemestre()));
			consultarDuvidaProfessor();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	
	public TurmaVO getTurmaVO() {
		if(turmaVO ==  null) {
			turmaVO = new TurmaVO();
		}
		return turmaVO;
	}
	public String getAno() {
		if (ano == null) {
			ano = Uteis.getAnoDataAtual();
		}
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;

	}

	public String getSemestre() {
		if (semestre == null) {
			semestre = "1";
		}
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	public void setTurmaVO(TurmaVO turmaVO) {
		this.turmaVO = turmaVO;
	}

	public void buscarTurma() throws Exception {
		setTurmaVO(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(getTurma(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
	}
	
	
	public MatriculaPeriodoVO getMatriculaPeriodoVO() {
		if(matriculaPeriodoVO == null) {
			matriculaPeriodoVO = new MatriculaPeriodoVO();
		}
		return matriculaPeriodoVO;
	}

	public void setMatriculaPeriodoVO(MatriculaPeriodoVO matriculaPeriodoVO) {
		this.matriculaPeriodoVO = matriculaPeriodoVO;
	}
	
	private void consultarTurmasProfessor() throws Exception {
		getListaSelectItemTurma().clear();
		List<Integer> mapAuxiliarSelectItem = new ArrayList<>();
		List<TurmaVO> turmas = consultarTurmaPorProfessor();
		for (TurmaVO turmaVO : turmas) {
			if (!mapAuxiliarSelectItem.contains(turmaVO.getCodigo())) {
				getListaSelectItemTurma().add(new SelectItem(turmaVO.getCodigo(), turmaVO.aplicarRegraNomeCursoApresentarCombobox()));
				mapAuxiliarSelectItem.add(turmaVO.getCodigo());
			}
		}
//		if (!turmas.isEmpty()) {
//			setTurma(turmas.get(0).getCodigo());
//			montarListaSelectItemDisciplina();
//		}
	}
	
public void iniciarDadosDuvidaProfessor() {
	inicializarTelaConsultaDuvidaProfessorVisaoProfessor();
}
}
