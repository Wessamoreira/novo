package relatorio.controle.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TipoDocumentoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.academico.DebitoDocumentosAlunoRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.academico.DebitoDocumentosAlunoRel;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;

@SuppressWarnings("unchecked")
@Controller("DebitoDocumentosAlunoRelControle")
@Scope("viewScope")
@Lazy
public class DebitoDocumentosAlunoRelControle extends SuperControleRelatorio {

    protected List listaConsultaCurso;
    private List listaSelectItemUnidadeEnsino;
    private List listaSelectItemTipoDocumento;
    protected String valorConsultaCurso;
    protected String campoConsultaCurso;
    protected Boolean existeUnidadeEnsino;
    protected String tipoConsulta;
    protected List listaTipoConsulta;
    protected List listaConsultaAluno;
    protected String valorConsultaAluno;
    protected String campoConsultaAluno;
    private List listaConsultaFuncionario;
    private String valorConsultaFuncionario;
    private String campoConsultaFuncionario;
    private List<SelectItem> listaSelectItemTurma;
    protected DebitoDocumentosAlunoRelVO debitoDocumentosAlunoRelVO;
    private MatriculaPeriodoVO matriculaperiodoVO;
    private Date dataInicio;
    private Date dataFim;
    private String ano;
    private String semestre;
    private Boolean preMatricula;
	private FiltroRelatorioAcademicoVO filtroRelatorioAcademico;
	private List<SelectItem> listaSelectItemSemestre;
	private List<SelectItem> listaSelectItemAno;
    protected String situacaoDocumento;
	private String campoConsultaTurma;
	private String valorConsultaTurma;
	private List<TurmaVO> listaConsultaTurma;
	private TurmaVO turmaVO;
    
    //Boolean da situação do documento
   private Boolean documentoEntregue;
   private Boolean documentoPendente;
   private Boolean documentoPendenteAprovacao;
   private Boolean documentoEntregaIndeferida;
    

    public DebitoDocumentosAlunoRelControle() throws Exception {
        novo();
        setMensagemID("msg_entre_prmrelatorio");
    }

    public void novo() {
        montarListaSelectItemOrdenacao();
        montarListaTipoConsulta();
        montarListaSelectItemTipoDocumento();
        if (getUnidadeEnsinoLogado().getCodigo() != null && getUnidadeEnsinoLogado().getCodigo() != 0) {
			getDebitoDocumentosAlunoRelVO().getUnidadeEnsinoVO().setCodigo(getUnidadeEnsinoLogado().getCodigo());
		}        
    }
    
    public void mudarMensagem() {
        setMensagemID("msg_entre_prmconsulta");
    }

    public void emitirComprovante() throws Exception {
        imprimirPDF();
    }

    public void imprimirDebitoDocumento() throws Exception {
        imprimirPDF();
    }

    public void imprimirPDF() throws Exception {
        List<DebitoDocumentosAlunoRelVO> listaDebitoDocumentosAlunoRelVO = new ArrayList<DebitoDocumentosAlunoRelVO>(0);
        String titulo = "";
        if (getTipoConsulta().equals("aluno")) {
        	setPreMatricula(Boolean.TRUE);
        }
        int unidades = 0;
        for(UnidadeEnsinoVO obj : getUnidadeEnsinoVOs()){
        	if(obj.getFiltrarUnidadeEnsino()){
        		unidades++;
        	}
        }
        if(unidades > 1){
        	String nomeEmpresa = "---";
        }else {
        	String nomeEmpresa = getDebitoDocumentosAlunoRelVO().getUnidadeEnsinoVO().getNome();
		}
        
        String design = DebitoDocumentosAlunoRel.getDesignIReportRelatorio();
        try {
            registrarAtividadeUsuario(getUsuarioLogado(), "DebitoDocumentosAlunoRelControle", "Inicializando Geração de Relatório Débito Documentos Aluno", "Emitindo Relatório");
            DebitoDocumentosAlunoRel.validarDados(getTipoConsulta(), getDebitoDocumentosAlunoRelVO(), getFiltroRelatorioAcademicoVO().getFiltrarCursoAnual(), getFiltroRelatorioAcademicoVO().getFiltrarCursoSemestral(), getFiltroRelatorioAcademicoVO().getFiltrarCursoIntegral(), getAno(), getSemestre(), getDataInicio(), getDataFim(), getTurmaVO());
            getFacadeFactory().getDebitoDocumentosAlunoRelFacade().setOrdenarPor(getOpcaoOrdenacao().intValue());
            getFacadeFactory().getDebitoDocumentosAlunoRelFacade().setDescricaoFiltros("");

            if (getTipoConsulta().equals("aluno")) {
            	setDataInicio(null);
            	setDataFim(null);
            	getFiltroRelatorioAcademicoVO().setFiltrarCursoAnual(false);
            	getFiltroRelatorioAcademicoVO().setFiltrarCursoSemestral(false);
            	getFiltroRelatorioAcademicoVO().setFiltrarCursoIntegral(false);
            	setPreMatricula(false);
            	setUnidadeEnsinoVOs(new ArrayList<UnidadeEnsinoVO>(0));
            }
            if(!getFiltroRelatorioAcademicoVO().getFiltrarCursoAnual() && !getFiltroRelatorioAcademicoVO().getFiltrarCursoSemestral()) {
            	setAno(null);
            }
            if(!getFiltroRelatorioAcademicoVO().getFiltrarCursoSemestral()) {
            	setSemestre(null);
            }
            
            listaDebitoDocumentosAlunoRelVO = getFacadeFactory().getDebitoDocumentosAlunoRelFacade().criarObjeto(
                    getPreMatricula(), getDebitoDocumentosAlunoRelVO(), getMatriculaperiodoVO(), getDataInicio(), getDataFim(),
                    getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado(), getAno(), getSemestre(), 
                    getFiltroRelatorioAcademicoVO().getFiltrarCursoAnual(), getFiltroRelatorioAcademicoVO().getFiltrarCursoSemestral(), getFiltroRelatorioAcademicoVO().getFiltrarCursoIntegral(),
                    getDocumentoEntregue(), getDocumentoPendente(), getDocumentoEntregaIndeferida(), getDocumentoPendenteAprovacao(),
                    obterListaUnidadeEnsinoSelecionada(getUnidadeEnsinoVOs()), getTipoConsulta(), getFiltroRelatorioAcademicoVO(), getTurmaVO(), getControlaAprovacaoDocEntregue());
            if (!listaDebitoDocumentosAlunoRelVO.isEmpty()) {
                getSuperParametroRelVO().setTituloRelatorio("Controle de Entrega de Documentos");
                getSuperParametroRelVO().setNomeDesignIreport(design);
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
                getSuperParametroRelVO().setSubReport_Dir(DebitoDocumentosAlunoRel.getCaminhoBaseRelatorio());
                getSuperParametroRelVO().setCaminhoBaseRelatorio(DebitoDocumentosAlunoRel.getCaminhoBaseRelatorio());
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getSuperParametroRelVO().setUsuarioVO(getUsuarioLogadoClone());
                getSuperParametroRelVO().setListaObjetos(listaDebitoDocumentosAlunoRelVO);
				getSuperParametroRelVO().setUnidadeEnsino(getUnidadeEnsinoVO().getNome());
				getSuperParametroRelVO().adicionarParametro("curso", getDebitoDocumentosAlunoRelVO().getCursoVO().getNome());
				getSuperParametroRelVO().adicionarParametro("turma", getTurmaVO().getIdentificadorTurma());
				getSuperParametroRelVO().adicionarParametro("aluno", getDebitoDocumentosAlunoRelVO().getMatriculaVO().getAluno().getNome());
				getSuperParametroRelVO().adicionarParametro("consultor", getDebitoDocumentosAlunoRelVO().getFuncionarioVO().getPessoa().getNome());
                if (getDebitoDocumentosAlunoRelVO().getTipoDocumentoVO().getCodigo().intValue() != 0) {
                    getSuperParametroRelVO().adicionarParametro("tipoDocumento", getFacadeFactory().getTipoDeDocumentoFacade().consultarPorChavePrimaria(getDebitoDocumentosAlunoRelVO().getTipoDocumentoVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()).getNome());
                }
                else {
                	getSuperParametroRelVO().adicionarParametro("tipoDocumento", "Todos");
                }
                getSuperParametroRelVO().adicionarParametro("filtrarCursoAnual", getFiltroRelatorioAcademicoVO().getFiltrarCursoAnual());
                getSuperParametroRelVO().adicionarParametro("filtrarCursoSemestral", getFiltroRelatorioAcademicoVO().getFiltrarCursoSemestral());
                getSuperParametroRelVO().adicionarParametro("filtrarCursoIntegral", getFiltroRelatorioAcademicoVO().getFiltrarCursoIntegral());
                getSuperParametroRelVO().adicionarParametro("ano", getAno());
                getSuperParametroRelVO().adicionarParametro("semestre", getSemestre());
                if(getFiltroRelatorioAcademicoVO().getFiltrarCursoIntegral()) {
                	getSuperParametroRelVO().adicionarParametro("dataInicio", getDataInicio());
                	getSuperParametroRelVO().adicionarParametro("dataFim", getDataFim());
                }else {
                	getSuperParametroRelVO().adicionarParametro("dataInicio", null);
                	getSuperParametroRelVO().adicionarParametro("dataFim", null);
                }
                getSuperParametroRelVO().adicionarParametro("documentoEntregue", getDocumentoEntregue());
                getSuperParametroRelVO().adicionarParametro("documentoPendente", getDocumentoPendente());
                getSuperParametroRelVO().adicionarParametro("documentoPendenteAprovacao", getDocumentoPendenteAprovacao());
                getSuperParametroRelVO().adicionarParametro("documentoEntregaIndeferida", getDocumentoEntregaIndeferida());
                getSuperParametroRelVO().adicionarParametro("trazerAlunoPreMatricula", getPreMatricula());
                
                setMensagemID("msg_relatorio_ok");
                realizarImpressaoRelatorio();
                
                
                montarListaSelectItemOrdenacao();
                montarListaTipoConsulta();
            } else {
                setMensagemID("msg_relatorio_sem_dados");
            }
            registrarAtividadeUsuario(getUsuarioLogado(), "DebitoDocumentosAlunoRelControle", "Inicializando Geração de Relatório Débito Documentos Aluno", "Emitindo Relatório");
            //apresentarRelatorioObjetos(DebitoDocumentosAlunoRel.getIdEntidade(), titulo, nomeEmpresa, "", "PDF", "", design, getUsuarioLogado().getNome(), getFacadeFactory().getDebitoDocumentosAlunoRelFacade().getDescricaoFiltros(),
            //        listaDebitoDocumentosAlunoRelVO, DebitoDocumentosAlunoRel.getCaminhoBaseRelatorio());
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            titulo = null;
            design = null;
            
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
                MatriculaVO matriculaVO = getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatricula(getValorConsultaAluno(),
                        this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,
                        getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
                if (!matriculaVO.getMatricula().equals("")) {
                    objs.add(matriculaVO);
                } else {
                    removerObjetoMemoria(matriculaVO);
                }
            }
            if (getCampoConsultaAluno().equals("nomePessoa")) {
                objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(),
                        this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
            }
            if (getCampoConsultaAluno().equals("nomeCurso")) {
                objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCurso(getValorConsultaAluno(),
                        this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
            }
            setListaConsultaAluno(objs);
            setMensagemID("msg_dados_consultados");

        } catch (Exception e) {
            setListaConsultaAluno(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void consultarAlunoPorMatricula() throws Exception {
        try {
            MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatricula(getDebitoDocumentosAlunoRelVO().getMatriculaVO().getMatricula(),
                    this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            if (objAluno.getMatricula().equals("")) {
                throw new Exception("Aluno de matrícula " + getDebitoDocumentosAlunoRelVO().getMatriculaVO().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
            }
            getDebitoDocumentosAlunoRelVO().setMatriculaVO(objAluno);
            setCampoConsultaAluno("");
            setValorConsultaAluno("");
            setListaConsultaAluno(new ArrayList(0));
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void atualizarListaTurma() {
        try {
            if (Uteis.isAtributoPreenchido(getDebitoDocumentosAlunoRelVO().getCursoVO())) {
                List<TurmaVO> turmaVOs = getFacadeFactory().getTurmaFacade().consultarPorCurso(getDebitoDocumentosAlunoRelVO().getCursoVO().getCodigo(), getDebitoDocumentosAlunoRelVO().getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
                setListaSelectItemTurma(UtilSelectItem.getListaSelectItem(turmaVOs, "codigo", "identificadorTurma"));
            } else {
                setListaSelectItemTurma(new ArrayList<SelectItem>());
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public boolean isApresentarCamposConsultaCurso() {
        return getTipoConsulta().equals("curso");
    }

    public boolean isApresentarCamposConsultaAluno() {
        return getTipoConsulta().equals("aluno");
    }
    
    public boolean isApresentarCamposConsultaTurma() {
        return getTipoConsulta().equals("turma");
    }

    public boolean isApresentarListaTurmas() {
        return !getListaSelectItemTurma().isEmpty() && isApresentarCamposConsultaCurso();
    }

    public void selecionarAluno() throws Exception {
        MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
        getDebitoDocumentosAlunoRelVO().setMatriculaVO(obj);
        setCampoConsultaAluno("");
        setValorConsultaAluno("");
        setListaConsultaAluno(new ArrayList(0));
    }

    public void limparDadosAluno() {
        getDebitoDocumentosAlunoRelVO().getMatriculaVO().setMatricula("");
        getDebitoDocumentosAlunoRelVO().getMatriculaVO().getAluno().setNome("");
        novo();
    }

    public void limparDadosFuncionario() {
        getDebitoDocumentosAlunoRelVO().setFuncionarioVO(null);
    }

    public List<SelectItem> getTipoConsultaComboAluno() {
        List<SelectItem> itens = new ArrayList<SelectItem>();
        itens.add(new SelectItem("nomePessoa", "Nome Aluno"));
        itens.add(new SelectItem("matricula", "Matrícula"));
        itens.add(new SelectItem("nomeCurso", "Nome Curso"));
        return itens;
    }

    public void montarListaSelectItemTipoDocumento() {
        try {
            montarListaSelectItemTipoDocumento("");
        } catch (Exception e) {
            // System.out.println("MENSAGEM => " + e.getMessage());
            ;
        }
    }

    public List montarListaSelectItemTipoDocumento(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;

        resultadoConsulta = consultarTipoDocumentoPorNome("");
        i = resultadoConsulta.iterator();
        List objs = new ArrayList(0);
        objs.add(new SelectItem(0, ""));
        while (i.hasNext()) {
            TipoDocumentoVO obj = (TipoDocumentoVO) i.next();
            objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
        }
        setListaSelectItemTipoDocumento(objs);
        return objs;
    }

    public List consultarTipoDocumentoPorNome(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getTipoDeDocumentoFacade().consultarPorNome(nomePrm, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
        return lista;
    }


    public void consultarFuncionario() {
        try {
            setListaConsultaFuncionario(getFacadeFactory().getFuncionarioFacade().consultar(getValorConsultaFuncionario(), getCampoConsultaFuncionario(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
            setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
        } catch (Exception e) {
            getListaConsultaFuncionario().clear();
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void selecionarFuncionario() throws Exception {
        FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItens");
        this.getDebitoDocumentosAlunoRelVO().setFuncionarioVO(obj);
        Uteis.liberarListaMemoria(this.getListaConsultaFuncionario());
        this.setValorConsultaFuncionario(null);
    }

    public void consultarCurso() {
        try {
            super.consultar();
            List objs = new ArrayList(0);
            if (getValorConsultaCurso().equals("")) {
                setMensagemID("msg_entre_prmconsulta");
                return;
            }
            if (getCampoConsultaCurso().equals("nome")) {                
                objs = getFacadeFactory().getCursoFacade().consultaRapidaPorNome(getValorConsultaCurso(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,false, getUsuarioLogado());
            }
            setListaConsultaCurso(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaCurso(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void limparDadosCurso() throws Exception {
        removerObjetoMemoria(getDebitoDocumentosAlunoRelVO().getCursoVO());
        setAno("");
        setSemestre("");
    }

    public void selecionarCurso() {
        CursoVO cursoVO = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItens");
        getDebitoDocumentosAlunoRelVO().setCursoVO(cursoVO);
        //atualizarListaTurma();
        
        if(cursoVO.getPeriodicidade().equals("SE")){
        	setAno(Uteis.getAnoDataAtual4Digitos());
            setSemestre(Uteis.getSemestreAtual());
        }else if(cursoVO.getPeriodicidade().equals("AN")){
        	setAno(Uteis.getAnoDataAtual4Digitos());
        }else{
        	setAno("");
        	setSemestre("");
        }
    }

    public void montarListaSelectItemOrdenacao() {
        Vector opcoes = getFacadeFactory().getDebitoDocumentosAlunoRelFacade().getOrdenacoesRelatorio();
        Enumeration i = opcoes.elements();
        List objs = new ArrayList(0);
        int contador = 0;
        while (i.hasMoreElements()) {
            String opcao = (String) i.nextElement();
            objs.add(new SelectItem(new Integer(contador), opcao));
            contador++;
        }
        setListaSelectItemOrdenacoesRelatorio(objs);
    }

    /**
     * Rotina responsável por preencher a combo de consulta da telas.
     */
    public List getTipoConsultaComboCurso() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
//        itens.add(new SelectItem("nrRegistroInterno", "Número Registro Interno"));
//        itens.add(new SelectItem("nomeAreaConhecimento", "Área Conhecimento"));
//        itens.add(new SelectItem("nivelEducacional", "Nível Educacional"));
        return itens;
    }

    public void montarListaTipoConsulta() {
        List obj = new ArrayList(0);
        obj.add(new SelectItem("aluno", "Aluno"));
        obj.add(new SelectItem("curso", "Unidade Ensino"));
        obj.add(new SelectItem("turma", "Turma"));
        setListaTipoConsulta(obj);
    }

    public Boolean getExisteUnidadeEnsino() {
        if (existeUnidadeEnsino == null) {
            existeUnidadeEnsino = Boolean.FALSE;
        }
        return existeUnidadeEnsino;
    }

    public void setExisteUnidadeEnsino(Boolean existeUnidadeEnsino) {
        this.existeUnidadeEnsino = existeUnidadeEnsino;
    }

    /**
     * @return the valorConsultaCurso
     */
    public String getValorConsultaCurso() {
        if (valorConsultaCurso == null) {
            valorConsultaCurso = "";
        }
        return valorConsultaCurso;
    }

    /**
     * @param valorConsultaCurso
     *            the valorConsultaCurso to set
     */
    public void setValorConsultaCurso(String valorConsultaCurso) {
        this.valorConsultaCurso = valorConsultaCurso;
    }

    /**
     * @return the listaConsultaCurso
     */
    public List getListaConsultaCurso() {
        if (listaConsultaCurso == null) {
            listaConsultaCurso = new ArrayList();
        }
        return listaConsultaCurso;
    }

    /**
     * @param listaConsultaCurso
     *            the listaConsultaCurso to set
     */
    public void setListaConsultaCurso(List listaConsultaCurso) {
        this.listaConsultaCurso = listaConsultaCurso;
    }

    /**
     * @return the campoConsultaCurso
     */
    public String getCampoConsultaCurso() {
        if (campoConsultaCurso == null) {
            campoConsultaCurso = "";
        }
        return campoConsultaCurso;
    }

    /**
     * @param campoConsultaCurso
     *            the campoConsultaCurso to set
     */
    public void setCampoConsultaCurso(String campoConsultaCurso) {
        this.campoConsultaCurso = campoConsultaCurso;
    }

    /**
     * @return the tipoConsulta
     */
    public String getTipoConsulta() {
        if (tipoConsulta == null) {
            tipoConsulta = "aluno";
        }
        return tipoConsulta;
    }

    /**
     * @param tipoConsulta
     *            the tipoConsulta to set
     */
    public void setTipoConsulta(String tipoConsulta) {
        this.tipoConsulta = tipoConsulta;
    }

    /**
     * @return the listaTipoConsulta
     */
    public List getListaTipoConsulta() {
        if (listaTipoConsulta == null) {
            listaTipoConsulta = new ArrayList(0);
        }
        return listaTipoConsulta;
    }

    /**
     * @param listaTipoConsulta
     *            the listaTipoConsulta to set
     */
    public void setListaTipoConsulta(List listaTipoConsulta) {
        this.listaTipoConsulta = listaTipoConsulta;
    }

    /**
     * @return the listaConsultaAluno
     */
    public List getListaConsultaAluno() {
        if (listaConsultaAluno == null) {
            listaConsultaAluno = new ArrayList(0);
        }
        return listaConsultaAluno;
    }

    /**
     * @param listaConsultaAluno
     *            the listaConsultaAluno to set
     */
    public void setListaConsultaAluno(List listaConsultaAluno) {
        this.listaConsultaAluno = listaConsultaAluno;
    }

    /**
     * @return the valorConsultaAluno
     */
    public String getValorConsultaAluno() {
        if (valorConsultaAluno == null) {
            valorConsultaAluno = "";
        }
        return valorConsultaAluno;
    }

    /**
     * @param valorConsultaAluno
     *            the valorConsultaAluno to set
     */
    public void setValorConsultaAluno(String valorConsultaAluno) {
        this.valorConsultaAluno = valorConsultaAluno;
    }

    /**
     * @return the campoConsultaAluno
     */
    public String getCampoConsultaAluno() {
        if (campoConsultaAluno == null) {
            campoConsultaAluno = "";
        }
        return campoConsultaAluno;
    }

    /**
     * @param campoConsultaAluno
     *            the campoConsultaAluno to set
     */
    public void setCampoConsultaAluno(String campoConsultaAluno) {
        this.campoConsultaAluno = campoConsultaAluno;
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

    public DebitoDocumentosAlunoRelVO getDebitoDocumentosAlunoRelVO() {
        if (debitoDocumentosAlunoRelVO == null) {
            debitoDocumentosAlunoRelVO = new DebitoDocumentosAlunoRelVO();
        }
        return debitoDocumentosAlunoRelVO;
    }

    public void setDebitoDocumentosAlunoRelVO(DebitoDocumentosAlunoRelVO debitoDocumentosAlunoRelVO) {
        this.debitoDocumentosAlunoRelVO = debitoDocumentosAlunoRelVO;
    }

    public MatriculaPeriodoVO getMatriculaperiodoVO() {
        if (matriculaperiodoVO == null) {
            matriculaperiodoVO = new MatriculaPeriodoVO();
        }
        return matriculaperiodoVO;
    }

    public void setMatriculaperiodoVO(MatriculaPeriodoVO matriculaperiodoVO) {
        this.matriculaperiodoVO = matriculaperiodoVO;
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

    /**
     * @return the listaSelectItemTipoDocumento
     */
    public List getListaSelectItemTipoDocumento() {
        if (listaSelectItemTipoDocumento == null) {
            listaSelectItemTipoDocumento = new ArrayList(0);
        }
        return listaSelectItemTipoDocumento;
    }

    /**
     * @param listaSelectItemTipoDocumento the listaSelectItemTipoDocumento to set
     */
    public void setListaSelectItemTipoDocumento(List listaSelectItemTipoDocumento) {
        this.listaSelectItemTipoDocumento = listaSelectItemTipoDocumento;
    }

    public List getTipoConsultaComboFuncionario() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("matricula", "Matrícula"));
        itens.add(new SelectItem("CPF", "CPF"));
        itens.add(new SelectItem("cargo", "Cargo"));
        itens.add(new SelectItem("departamento", "Departamento"));
        itens.add(new SelectItem("unidadeEnsino", "Unidade de Ensino"));
        return itens;
    }

    /**
     * @return the listaConsultaFuncionarioUnificar
     */
    public List getListaConsultaFuncionario() {
        if (listaConsultaFuncionario == null) {
            listaConsultaFuncionario = new ArrayList();
        }
        return listaConsultaFuncionario;
    }

    /**
     * @param listaConsultaFuncionarioUnificar the listaConsultaFuncionarioUnificar to set
     */
    public void setListaConsultaFuncionario(List listaConsultaFuncionario) {
        this.listaConsultaFuncionario = listaConsultaFuncionario;
    }

    /**
     * @return the valorConsultaFuncionarioUnificar
     */
    public String getValorConsultaFuncionario() {
        if (valorConsultaFuncionario == null) {
            valorConsultaFuncionario = "";
        }
        return valorConsultaFuncionario;
    }

    /**
     * @param valorConsultaFuncionarioUnificar the valorConsultaFuncionarioUnificar to set
     */
    public void setValorConsultaFuncionario(String valorConsultaFuncionario) {
        this.valorConsultaFuncionario = valorConsultaFuncionario;
    }

    /**
     * @return the campoConsultaFuncionarioUnificar
     */
    public String getCampoConsultaFuncionario() {
        if (campoConsultaFuncionario == null) {
            campoConsultaFuncionario = "";
        }
        return campoConsultaFuncionario;
    }

    /**
     * @param campoConsultaFuncionarioUnificar the campoConsultaFuncionarioUnificar to set
     */
    public void setCampoConsultaFuncionario(String campoConsultaFuncionario) {
        this.campoConsultaFuncionario = campoConsultaFuncionario;
    }

    /**
     * @return the dataInicio
     */
    public Date getDataInicio() {
        if (dataInicio == null) {
            dataInicio = Uteis.getDataPrimeiroDiaMes(new Date());
        }
        return dataInicio;
    }

    /**
     * @param dataInicio the dataInicio to set
     */
    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    /**
     * @return the dataFim
     */
    public Date getDataFim() {
        if (dataFim == null) {
            dataFim = Uteis.getDataUltimoDiaMes(new Date());
        }
        return dataFim;
    }

    /**
     * @param dataFim the dataFim to set
     */
    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

	public String getAno() {
		if(ano == null){
			ano = "";
		}
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getSemestre() {
		if(semestre == null){
			semestre = "";
		}
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
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
	
	public List<SelectItem> getListaSelectItemAno() {
		try {
			if (listaSelectItemAno == null) {
				listaSelectItemAno = new ArrayList<SelectItem>(0);
				List<String> anos = getFacadeFactory().getMatriculaPeriodoFacade().consultarAnosMatriculaPeriodo();			
				for (String ano : anos) {
					listaSelectItemAno.add(new SelectItem(ano, ano));
				}
			}
			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return listaSelectItemAno;
	}

	public void setListaSelectItemAno(List<SelectItem> listaSelectItemAno) {
		this.listaSelectItemAno = listaSelectItemAno;
	}
    
	
	public boolean getIsApresentarAno(){
		return getDebitoDocumentosAlunoRelVO().getCursoVO().getCodigo() > 0;
	}
	
	public boolean getIsApresentarSemestre(){
		return getDebitoDocumentosAlunoRelVO().getCursoVO().getCodigo() > 0 && getDebitoDocumentosAlunoRelVO().getCursoVO().getPeriodicidade().equals("SE");
	}

	public Boolean getPreMatricula() {
		if (preMatricula == null) {
			preMatricula = Boolean.FALSE;
		}
		return preMatricula;
	}

	public void setPreMatricula(Boolean preMatricula) {
		this.preMatricula = preMatricula;
	}
	
	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if (unidadeEnsinoVO == null) {
			if (getUnidadeEnsinoLogado().getCodigo() > 0) {
				unidadeEnsinoVO = getUnidadeEnsinoLogado();
			} else {
				unidadeEnsinoVO = new UnidadeEnsinoVO();
			}
		}
		return unidadeEnsinoVO;
	}

	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}
	
	@PostConstruct
	public void consultarUnidadeEnsino() {
		try {
			consultarUnidadeEnsinoFiltroRelatorio("ImpostosRetidosContaReceberRel");
			verificarTodasUnidadesSelecionadas();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
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
	
	public List<UnidadeEnsinoVO> obterListaUnidadeEnsinoSelecionada(List<UnidadeEnsinoVO> unidadeEnsinoVOs) {
		List<UnidadeEnsinoVO> objs = new ArrayList<UnidadeEnsinoVO>(0);
		unidadeEnsinoVOs.forEach(obj->{
			if (obj.getFiltrarUnidadeEnsino()) {
				objs.add(obj);
			}
		});
		return objs;
	}
	
	public void limparUnidadeEnsino(){
		super.limparUnidadeEnsinos();
	}
	
	public boolean getApresentarFiltroAno() {
		return (getFiltroRelatorioAcademicoVO().getFiltrarCursoAnual() || getFiltroRelatorioAcademicoVO().getFiltrarCursoSemestral());
	}

	public boolean getApresentarFiltroSemestre() {
		return getFiltroRelatorioAcademicoVO().getFiltrarCursoSemestral();
	}

	public boolean getApresentarFiltroPeriodo() {
		return getFiltroRelatorioAcademicoVO().getFiltrarCursoIntegral();
	}
	
	public String getSituacaoDocumento() {
		return situacaoDocumento;
	}

	public void setSituacaoDocumento(String situacaoDocumento) {
		this.situacaoDocumento = situacaoDocumento;
	}

	public List<SelectItem> getListaSelectItemSituacaoDocumento() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("entregue", "Entregue"));
		itens.add(new SelectItem("pendente", "Pendente"));
		itens.add(new SelectItem("pendenteAprovacao", "Pendente Aprovação"));
		itens.add(new SelectItem("entregaIndeferida", "Entrega Indeferida"));
		return itens;
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
	
	public List<SelectItem> getTipoConsultaComboTurma() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("identificadorTurma", "Identificador"));
		return itens;
	}
	
	public void consultarTurma() {
		try {
			// if (getUnidadeEnsinoVO().getCodigo() == 0) {
			// throw new Exception("Informe a Unidade de Ensino.");
			// }
			super.consultar();
			List<TurmaVO> objs = new ArrayList<TurmaVO>(0);
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaCurso(getValorConsultaTurma(), 0, getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultaTurma(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList<TurmaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void selecionarTurma() {
		try {
			TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
			setTurmaVO(obj);	
			getDebitoDocumentosAlunoRelVO().setCursoVO(obj.getCurso());
			setUnidadeEnsinoVO(obj.getUnidadeEnsino());
			/*if (obj.getAnual()) {
				getFiltroRelatorioAcademico().setTipoFiltroPeriodoAcademico(TipoFiltroPeriodoAcademicoEnum.ANO);
			} else if (obj.getSemestral()) {
				getFiltroRelatorioAcademico().setTipoFiltroPeriodoAcademico(TipoFiltroPeriodoAcademicoEnum.ANO_SEMESTRE);
			} else {
				getFiltroRelatorioAcademico().setTipoFiltroPeriodoAcademico(TipoFiltroPeriodoAcademicoEnum.PERIODO_DATA);
			}*/
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparTurma() {
		try {
			setTurmaVO(null);
			getDebitoDocumentosAlunoRelVO().setCursoVO(null);
			setUnidadeEnsinoVO(null);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void limparCurso() {
		try {
			getDebitoDocumentosAlunoRelVO().setCursoVO(null);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public Boolean getDocumentoEntregue() {
		if (documentoEntregue == null) {
			documentoEntregue = Boolean.FALSE;
		}
		return documentoEntregue;
	}

	public void setDocumentoEntregue(Boolean documentoEntregue) {
		this.documentoEntregue = documentoEntregue;
	}

	public Boolean getDocumentoPendente() {
		if (documentoPendente == null) {
			documentoPendente = Boolean.FALSE;
		}
		return documentoPendente;
	}

	public void setDocumentoPendente(Boolean documentoPendente) {
		this.documentoPendente = documentoPendente;
	}

	public Boolean getDocumentoPendenteAprovacao() {
		if (documentoPendenteAprovacao == null) {
			documentoPendenteAprovacao = Boolean.FALSE;
		}
		return documentoPendenteAprovacao;
	}

	public void setDocumentoPendenteAprovacao(Boolean documentoPendenteAprovacao) {
		this.documentoPendenteAprovacao = documentoPendenteAprovacao;
	}

	public Boolean getDocumentoEntregaIndeferida() {
		if (documentoEntregaIndeferida == null) {
			documentoEntregaIndeferida = Boolean.FALSE;
		}
		return documentoEntregaIndeferida;
	}

	public void setDocumentoEntregaIndeferida(Boolean documentoEntregaIndeferida) {
		this.documentoEntregaIndeferida = documentoEntregaIndeferida;
	}
	
	public Boolean getControlaAprovacaoDocEntregue() {
		try {
			return getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(getDebitoDocumentosAlunoRelVO().getMatriculaVO().getUnidadeEnsino().getCodigo()).getControlaAprovacaoDocEntregue();
		} catch (Exception e) {
			return false;
		}
	}
	
}
