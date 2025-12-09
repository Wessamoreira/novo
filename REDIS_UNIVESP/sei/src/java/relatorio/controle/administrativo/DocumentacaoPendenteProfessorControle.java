package relatorio.controle.administrativo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.NivelFormacaoAcademica;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.administrativo.ProfessorRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.administrativo.DocumentacaoPendenteProfessorRel;

@Controller("DocumentacaoPendenteProfessorControle")
@Lazy
@Scope("viewScope")
public class DocumentacaoPendenteProfessorControle extends SuperControleRelatorio implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -2122177448628742375L;
    private List<SelectItem> listaSelectItemEscolaridade;
    private List<SelectItem> listaSelectItemUnidadeEnsino;
    private PessoaVO professor;
    private TurmaVO turma;
    private CursoVO curso;
    private Integer unidadeEnsino;
    protected List<TurmaVO> listaConsultaTurma;
    protected String valorConsultaTurma;
    protected String campoConsultaTurma;
    protected List listaConsultaCurso;
    protected String valorConsultaCurso;
    protected String campoConsultaCurso;
    protected String escolaridade;

    protected List<FuncionarioVO> listaConsultaProfessor;
    protected String valorConsultaProfessor;
    protected String campoConsultaProfessor;
    protected Boolean existeUnidadeEnsino;
    
    
    @PostConstruct
    public void inicializarUnidadeEnsino() {
        try {
            setUnidadeEnsino(getUnidadeEnsinoLogado().getCodigo());
            if (getUnidadeEnsino().intValue() == 0) {
                setExisteUnidadeEnsino(Boolean.FALSE);
            } else {
                setExisteUnidadeEnsino(Boolean.TRUE);
            }
        } catch (Exception e) {
            setExisteUnidadeEnsino(Boolean.FALSE);
        }
    }
    
    public Boolean getExisteUnidadeEnsino() {
        return existeUnidadeEnsino;
    }

    public void setExisteUnidadeEnsino(Boolean existeUnidadeEnsino) {
        this.existeUnidadeEnsino = existeUnidadeEnsino;
    }
    
    public void imprimirPDF() {
    	List<ProfessorRelVO> listaObjetos = null;
        try {
            registrarAtividadeUsuario(getUsuarioLogado(), "DocumentacaoPendenteProfessorRelControle", "Iniciando Impressao Relatorio PDF", "Emitindo Relatorio");                       
            listaObjetos = getFacadeFactory().getDocumentacaoPendenteProfessorRelFacade().consultarDadosGeracaoRelatorio(getUnidadeEnsino(), getProfessor().getCodigo(), getTurma().getCodigo(), getCurso().getCodigo(), getEscolaridade());
            if (listaObjetos.isEmpty()) {
                throw new Exception("Não há dados a serem exibidos no relatório.");
            }
            getSuperParametroRelVO().getParametros().remove("escolaridade");
            getSuperParametroRelVO().setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCodigo(getUnidadeEnsino(), false, getUsuarioLogado()).getNome());
            getSuperParametroRelVO().setTurma(getTurma().getIdentificadorTurma());
            getSuperParametroRelVO().setCurso(getCurso().getNome());
            getSuperParametroRelVO().setProfessor(getProfessor().getNome());
            getSuperParametroRelVO().setNomeDesignIreport(DocumentacaoPendenteProfessorRel.getDesignIReportRelatorio());
            getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
            getSuperParametroRelVO().setSubReport_Dir(DocumentacaoPendenteProfessorRel.getCaminhoBaseRelatorio());
            getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
            getSuperParametroRelVO().setTituloRelatorio("Relatório do Documentação Pendente Professor");
            getSuperParametroRelVO().setListaObjetos(listaObjetos);
            getSuperParametroRelVO().setCaminhoBaseRelatorio(DocumentacaoPendenteProfessorRel.getCaminhoBaseRelatorio());
            getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
            getSuperParametroRelVO().setQuantidade(listaObjetos.size());
            if (!getUnidadeEnsino().equals(0)) {
		getSuperParametroRelVO().adicionarLogoUnidadeEnsinoSelecionada(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsino(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
            }
            realizarImpressaoRelatorio();
            removerObjetoMemoria(this);
            setMensagemID("msg_relatorio_ok");
            registrarAtividadeUsuario(getUsuarioLogado(), "DocumentacaoPendenteProfessorRelControle", "Finalizando Impressao Relatorio PDF", "Emitindo Relatorio");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            Uteis.liberarListaMemoria(listaObjetos);            
        }
    }
    
    public void imprimirPDFExcel() {
    	List<ProfessorRelVO> listaObjetos = null;
    	try {
    		registrarAtividadeUsuario(getUsuarioLogado(), "DocumentacaoPendenteProfessorRelControle", "Iniciando Impressao Relatorio PDF", "Emitindo Relatorio");                       
    		listaObjetos = getFacadeFactory().getDocumentacaoPendenteProfessorRelFacade().consultarDadosGeracaoRelatorio(getUnidadeEnsino(), getProfessor().getCodigo(), getTurma().getCodigo(), getCurso().getCodigo(), getEscolaridade());
    		if (listaObjetos.isEmpty()) {
    			throw new Exception("Não há dados a serem exibidos no relatório.");
    		}
    		getSuperParametroRelVO().getParametros().remove("escolaridade");
    		getSuperParametroRelVO().setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCodigo(getUnidadeEnsino(), false, getUsuarioLogado()).getNome());
    		getSuperParametroRelVO().setTurma(getTurma().getIdentificadorTurma());
    		getSuperParametroRelVO().setCurso(getCurso().getNome());
    		getSuperParametroRelVO().setProfessor(getProfessor().getNome());
    		getSuperParametroRelVO().setNomeDesignIreport(DocumentacaoPendenteProfessorRel.getDesignIReportRelatorioExcel());
    		getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.EXCEL);
    		getSuperParametroRelVO().setSubReport_Dir(DocumentacaoPendenteProfessorRel.getCaminhoBaseRelatorio());
    		getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
    		getSuperParametroRelVO().setTituloRelatorio("Relatório do Documentação Pendente Professor");
    		getSuperParametroRelVO().setListaObjetos(listaObjetos);
    		getSuperParametroRelVO().setCaminhoBaseRelatorio(DocumentacaoPendenteProfessorRel.getCaminhoBaseRelatorio());
    		getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
    		getSuperParametroRelVO().setQuantidade(listaObjetos.size());
    		realizarImpressaoRelatorio();
    		removerObjetoMemoria(this);
    		setMensagemID("msg_relatorio_ok");
    		registrarAtividadeUsuario(getUsuarioLogado(), "DocumentacaoPendenteProfessorRelControle", "Finalizando Impressao Relatorio PDF", "Emitindo Relatorio");
    	} catch (Exception e) {
    		setMensagemDetalhada("msg_erro", e.getMessage());
    	} finally {
    		Uteis.liberarListaMemoria(listaObjetos);            
    	}
    }

    public List<SelectItem> getListaSelectItemEscolaridade() {
        if (listaSelectItemEscolaridade == null) {
            listaSelectItemEscolaridade = new ArrayList<SelectItem>(0);
            listaSelectItemEscolaridade.add(new SelectItem("", "Todas"));
            for (NivelFormacaoAcademica escolaridade : NivelFormacaoAcademica.values()) {
                listaSelectItemEscolaridade.add(new SelectItem(escolaridade.getValor(), escolaridade.getDescricao()));
            }
        }
        return listaSelectItemEscolaridade;
    }

    public void setListaSelectItemEscolaridade(List<SelectItem> listaSelectItemEscolaridade) {
        this.listaSelectItemEscolaridade = listaSelectItemEscolaridade;
    }

    public Integer getUnidadeEnsino() {
        return unidadeEnsino;
    }

    public void setUnidadeEnsino(Integer unidadeEnsino) {
        this.unidadeEnsino = unidadeEnsino;
    }

    public void montarListaSelectItemUnidadeEnsino() {
        if (listaSelectItemUnidadeEnsino != null) {
            listaSelectItemUnidadeEnsino.clear();
        }
        listaSelectItemUnidadeEnsino = null;
        getListaSelectItemUnidadeEnsino();
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getListaSelectItemUnidadeEnsino() {
        if (listaSelectItemUnidadeEnsino == null) {
            listaSelectItemUnidadeEnsino = new ArrayList<SelectItem>(0);
            
            List<UnidadeEnsinoVO> unidadeEnsinoVOs = null;
            try {
                if (getProfessor().getCodigo() > 0) {
                    unidadeEnsinoVOs = getFacadeFactory().getUnidadeEnsinoFacade().consultarUnidadeEnsinoProfessorPorCodigoPessoa(getProfessor().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
                } else {
                    unidadeEnsinoVOs = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome("", super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
                }
                if(!unidadeEnsinoVOs.isEmpty() && unidadeEnsinoVOs.size()==1){
                    setUnidadeEnsino(unidadeEnsinoVOs.get(0).getCodigo());
                }else{
                    listaSelectItemUnidadeEnsino.add(new SelectItem(0, ""));
                }
                for (UnidadeEnsinoVO unidadeEnsinoVO : unidadeEnsinoVOs) {
                    
                    listaSelectItemUnidadeEnsino.add(new SelectItem(unidadeEnsinoVO.getCodigo(), unidadeEnsinoVO.getNome()));
                }
            } catch (Exception e) {

            }
        }
        return listaSelectItemUnidadeEnsino;
    }

    public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
        this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
    }

    @SuppressWarnings("unchecked")
    public void consultarProfessor() {
        try {
            Uteis.liberarListaMemoria(getListaConsultaProfessor());
            if (getCampoConsultaProfessor().equals("nome")) {
                setListaConsultaProfessor(getFacadeFactory().getFuncionarioFacade().consultarPorNome(getValorConsultaProfessor(), "PR", true, getUnidadeEnsino(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
            }
            if (getCampoConsultaProfessor().equals("cpf")) {
                setListaConsultaProfessor(getFacadeFactory().getFuncionarioFacade().consultarPorCPF(getValorConsultaProfessor(), "PR", true, getUnidadeEnsino(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
            }
            if (getCampoConsultaProfessor().equals("matricula")) {
                FuncionarioVO funcionarioVO = getFacadeFactory().getFuncionarioFacade().consultarFuncionarioPorMatricula(getValorConsultaProfessor(), "PR", true, getUnidadeEnsino(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
                if (funcionarioVO != null && funcionarioVO.getCodigo()>0) {
                    setListaConsultaProfessor(new ArrayList<FuncionarioVO>());
                    getListaConsultaProfessor().add(funcionarioVO);
                }
            }

            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            Uteis.liberarListaMemoria(getListaConsultaProfessor());
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }
    
    public void montarTurma() throws Exception {
        try {
            String identificador = getTurma().getIdentificadorTurma();   
            limparTurma();
            setTurma(getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaEspecificoProfessorUnidadeEnsino(getTurma(), identificador, 
                     getProfessor().getCodigo(), getUnidadeEnsino(),
                     false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado()));
            
            
            if(getTurma() != null && getTurma().getCodigo()>0 && (getUnidadeEnsino() == null || getUnidadeEnsino() == 0)){
                setUnidadeEnsino(getTurma().getUnidadeEnsino().getCodigo());
            }           
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {            
            limparTurma();        
            setMensagemDetalhada("msg_erro", e.getMessage());
        } 
    }
    
    public void limparProfessor(){
        getProfessor().setCodigo(0);
        getProfessor().setNome(""); 
    }

    
    public void limparTurma(){
    	setTurma(new TurmaVO());
    }
    
    public void limparCurso(){
        setCurso(new CursoVO());
        limparTurma();
    }

    public void limparFiltros(){
       limparProfessor();
       limparTurma();
       limparCurso();
    }
    
    public void abrirModalConsultaTurma(){
        Uteis.liberarListaMemoria(getListaConsultaTurma());
        if(getProfessor().getCodigo()>0){
            try {
                setListaConsultaTurma(getFacadeFactory().getTurmaFacade().consultaTurmaDoProfessor(getProfessor().getCodigo(), getUnidadeEnsino(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false,  getUsuarioLogado()));
            } catch (Exception e) {                
                e.printStackTrace();
            }
                
        }
    }
    
    public void abrirModalConsultaCurso(){
        Uteis.liberarListaMemoria(getListaConsultaTurma());
        if(getProfessor().getCodigo()>0){
            try {
                setListaConsultaCurso(getFacadeFactory().getUnidadeEnsinoCursoFacade().consultaUnidadeEnsinoCursoDoProfessor(getProfessor().getCodigo(), getUnidadeEnsino(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false,  getUsuarioLogado()));
            } catch (Exception e) {                
                e.printStackTrace();
            }
                
        }
    }

    public void consultarTurma() {
        try {
            Uteis.liberarListaMemoria(getListaConsultaTurma());
            
            if (getCampoConsultaTurma().equals("identificadorTurma")) {
                setListaConsultaTurma(getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaCurso(getValorConsultaTurma(), getCurso().getCodigo(), getUnidadeEnsino(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
            }
            if (getCampoConsultaTurma().equals("nomeTurno")) {
                setListaConsultaTurma(getFacadeFactory().getTurmaFacade().consultaRapidaPorTurnoCurso(getValorConsultaTurma(), getCurso().getCodigo(), getUnidadeEnsino(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
            }

            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaTurma(new ArrayList<TurmaVO>(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }
    
    public void consultarCurso() {
        try {
            Uteis.liberarListaMemoria(getListaConsultaCurso());
            
            if (getCampoConsultaCurso().equals("nomeCurso")) {
                setListaConsultaCurso(getFacadeFactory().getCursoFacade().consultaRapidaPorNomeCursoUnidadeEnsino(getValorConsultaCurso(), getUnidadeEnsino(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
            }

            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaTurma(new ArrayList<TurmaVO>(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }
    
    public void selecionarProfessor() throws Exception {
        FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("professorItens");        
        setProfessor(obj.getPessoa());
        if(getTurma().getCodigo()>0){
            montarTurma();
        }
        limparMensagem();
        obj = null;
        valorConsultaProfessor = "";
        campoConsultaProfessor = "";
        listaConsultaProfessor.clear();
    }

    public void selecionarTurma() throws Exception {
        TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");        
        setTurma(obj);
        if(getTurma() != null && getTurma().getCodigo()>0 && (getUnidadeEnsino() == null || getUnidadeEnsino() == 0)){
            setUnidadeEnsino(getTurma().getUnidadeEnsino().getCodigo());
        }        
        if (getCurso().getCodigo() == 0) {
        	setCurso(obj.getCurso());
        }
        valorConsultaTurma = "";
        campoConsultaTurma = "";
        listaConsultaTurma.clear();
    }
    
    public void selecionarCurso() throws Exception {
        CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItens");        
        setTurma(new TurmaVO());
        setCurso(obj);
        valorConsultaCurso = "";
        campoConsultaCurso = "";
        listaConsultaCurso.clear();
    }

    private List<SelectItem> tipoConsultaComboTurma;

    public List<SelectItem> getTipoConsultaComboTurma() {
        if (tipoConsultaComboTurma == null) {
            tipoConsultaComboTurma = new ArrayList<SelectItem>(0);
            tipoConsultaComboTurma.add(new SelectItem("identificadorTurma", "Identificador"));
            tipoConsultaComboTurma.add(new SelectItem("nomeTurno", "Turno"));
        }
        return tipoConsultaComboTurma;
    }
    
    private List<SelectItem> tipoConsultaComboCurso;
    
    public List<SelectItem> getTipoConsultaComboCurso() {
        if (tipoConsultaComboCurso == null) {
            tipoConsultaComboCurso = new ArrayList<SelectItem>(0);
            tipoConsultaComboCurso.add(new SelectItem("nomeCurso", "Curso"));
            tipoConsultaComboCurso.add(new SelectItem("nomeUnidadeEnsino", "Unidade de Ensino"));
        }
        return tipoConsultaComboCurso;
    }
    
    private List<SelectItem> tipoConsultaComboProfessor;

    public List<SelectItem> getTipoConsultaComboProfessor() {
        if (tipoConsultaComboProfessor == null) {
            tipoConsultaComboProfessor = new ArrayList<SelectItem>(0);
            tipoConsultaComboProfessor.add(new SelectItem("nome", "Nome"));
            tipoConsultaComboProfessor.add(new SelectItem("cpf", "CPF"));
            tipoConsultaComboProfessor.add(new SelectItem("matricula", "Matricula"));            
        }
        return tipoConsultaComboProfessor;
    }

    public String getCampoConsultaTurma() {
        return campoConsultaTurma;
    }

    public void setCampoConsultaTurma(String campoConsultaTurma) {
        this.campoConsultaTurma = campoConsultaTurma;
    }

    public List<TurmaVO> getListaConsultaTurma() {
        return listaConsultaTurma;
    }

    public void setListaConsultaTurma(List<TurmaVO> listaConsultaTurma) {
        this.listaConsultaTurma = listaConsultaTurma;
    }

    public String getValorConsultaTurma() {
        return valorConsultaTurma;
    }

    public void setValorConsultaTurma(String valorConsultaTurma) {
        this.valorConsultaTurma = valorConsultaTurma;
    }

    public void limparIdentificador() {
        setTurma(null);
    }

    public PessoaVO getProfessor() {
        if(professor == null){
            professor = new PessoaVO();
        }
        return professor;
    }

    public void setProfessor(PessoaVO professor) {
        this.professor = professor;
    }

    public TurmaVO getTurma() {
        if(turma == null){
            turma = new TurmaVO();
        }
        return turma;
    }

    public void setTurma(TurmaVO turma) {
        this.turma = turma;
    }

    public List<FuncionarioVO> getListaConsultaProfessor() {
        return listaConsultaProfessor;
    }

    public void setListaConsultaProfessor(List<FuncionarioVO> listaConsultaProfessor) {
        this.listaConsultaProfessor = listaConsultaProfessor;
    }

    public String getValorConsultaProfessor() {
        return valorConsultaProfessor;
    }

    public void setValorConsultaProfessor(String valorConsultaProfessor) {
        this.valorConsultaProfessor = valorConsultaProfessor;
    }

    public String getCampoConsultaProfessor() {
        return campoConsultaProfessor;
    }

    public void setCampoConsultaProfessor(String campoConsultaProfessor) {
        this.campoConsultaProfessor = campoConsultaProfessor;
    }

    
    public String getEscolaridade() {
        if(escolaridade == null){
            escolaridade = "";
        }
        return escolaridade;
    }

    
    public void setEscolaridade(String escolaridade) {
        this.escolaridade = escolaridade;
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

	public CursoVO getCurso() {
		if (curso == null) {
			curso = new CursoVO();
		}
		return curso;
	}

	public void setCurso(CursoVO curso) {
		this.curso = curso;
	}
    
    

}
