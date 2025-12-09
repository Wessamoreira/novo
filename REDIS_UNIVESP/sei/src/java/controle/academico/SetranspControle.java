package controle.academico;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.SetranspVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.OrigemArquivo;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.facade.jdbc.academico.Setransp;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas setranspForm.jsp
 * setranspCons.jsp) com as funcionalidades da classe <code>Setransp</code>. Implemtação da camada controle (Backing
 * Bean).
 * 
 * @see SuperControle
 * @see Setransp
 * @see SetranspVO
 */
@SuppressWarnings("unchecked")
@Controller("SetranspControle")
@Scope("viewScope")
@Lazy
public class SetranspControle extends SuperControle implements Serializable {

    private SetranspVO setranspVO;
    private String valorConsultaFiltros;
    private Boolean filtroUnidadeEnsino;
    private Boolean filtroCurso;
    private Boolean filtroTurma;
    private Boolean filtroAluno;
    private TurmaVO turmaVO;
    private List listaConsultaTurma;
    private String valorConsultaTurma;
    private String campoConsultaTurma;
    private MatriculaVO matriculaVO;
    private List listaConsultaAluno;
    private String valorConsultaAluno;
    private String campoConsultaAluno;
    private CursoVO cursoVO;
    private List listaSelectItemCurso;
    private UnidadeEnsinoVO unidadeEnsinoVO;
    private List listaSelectItemUnidadeEnsino;
    private Date dataInicio;
    private Date dataFim;
    private String ano;
    private String semestre;
    private String filtroPeriodicidade;

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public SetranspControle() throws Exception {
        //obterUsuarioLogado();
        montarListaSelectItemCurso();
        setControleConsulta(new ControleConsulta());
        setMensagemID("msg_entre_prmconsulta");
    }

    /**
     * Rotina responsável por disponibilizar um novo objeto da classe <code>Setransp</code> para edição pelo usuário da
     * aplicação.
     *
     * @throws Exception
     */
    public String novo() throws Exception {
        removerObjetoMemoria(this);
        setSetranspVO(new SetranspVO());
        setValorConsultaFiltros("");
        setarFalseNosFiltros();
        setarNewNosObjetosDeConsulta();
        getSetranspVO().getResponsavel().setCodigo(getUsuarioLogado().getCodigo());
        getSetranspVO().getResponsavel().setNome(getUsuarioLogado().getNome());
        getSetranspVO().getUnidadeEnsino().setCodigo(getUnidadeEnsinoLogado().getCodigo());
        getSetranspVO().getUnidadeEnsino().setNome(getUnidadeEnsinoLogado().getNome());
        setMensagemID("msg_entre_dados");
        return Uteis.getCaminhoRedirecionamentoNavegacao("setranspForm.xhtml");
    }

    /**
     * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>Setransp</code>. Caso o
     * objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é
     * acionado o <code>alterar()</code>. Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado
     * para o usuário juntamente com uma mensagem de erro.
     */
    public String gravar() {
        try {
            gerarArquivoSetransp();
            getFacadeFactory().getArquivoFacade().incluirArquivoSetransp(getSetranspVO(), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
            setMensagemID("msg_dados_gravados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("setranspForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "";
        }
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP SetranspCons.jsp. Define o tipo de consulta a ser
     * executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como resultado,
     * disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    @Override
    public String consultar() {
        try {
            super.consultar();
            List objs = new ArrayList(0);
            if (getControleConsulta().getCampoConsulta().equals("codigo")) {
                if (getControleConsulta().getValorConsulta().equals("")) {
                    getControleConsulta().setValorConsulta("0");
                }
                int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
                objs = getFacadeFactory().getSetranspFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("dataGeracao")) {
                Date valorData = Uteis.getDate(getControleConsulta().getValorConsulta());
                objs = getFacadeFactory().getSetranspFacade().consultarPorDataGeracao(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), true,
                        Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("responsavel")) {
                if (getControleConsulta().getValorConsulta().equals("")) {
                    getControleConsulta().setValorConsulta("0");
                }
                int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
                objs = getFacadeFactory().getSetranspFacade().consultarPorResponsavel(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            setListaConsulta(objs);
            setMensagemID("msg_dados_consultados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("setranspCons.xhtml");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("setranspCons.xhtml");
        }
    }

    /**
     * Operação responsável por processar a exclusão um objeto da classe <code>SetranspVO</code> Após a exclusão ela
     * automaticamente aciona a rotina para uma nova inclusão.
     */
    public String excluirTelaCons() {
        try {
            selecionarArquivoSetransp();
            getFacadeFactory().getArquivoFacade().excluirArquivoSetransp(getSetranspVO(), getUsuarioLogado());
            novo();
            consultar();
            setMensagemID("msg_dados_excluidos");
            return "";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "";
        }
    }

    public String excluir() {
        try {
            getFacadeFactory().getArquivoFacade().excluirArquivoSetransp(getSetranspVO(), getUsuarioLogado());
            novo();
            setMensagemID("msg_dados_excluidos");
            return Uteis.getCaminhoRedirecionamentoNavegacao("setranspForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("setranspForm.xhtml");
        }
    }

    public List<SelectItem> getListaSelectItemFiltros() {
        List<SelectItem> itens = new ArrayList<SelectItem>();
        itens.add(new SelectItem("", ""));
        itens.add(new SelectItem("unidadeEnsino", "Unidade de Ensino"));
        itens.add(new SelectItem("curso", "Curso"));
        itens.add(new SelectItem("turma", "Turma"));
        itens.add(new SelectItem("aluno", "Aluno"));
        return itens;
    }

    public void gerarArquivoSetransp() throws Exception {
        setSetranspVO(getFacadeFactory().getSetranspFacade().criarObjetoSetranspVO(getSetranspVO(), getUnidadeEnsinoVO().getCodigo(), getCursoVO().getCodigo(), 
        		getTurmaVO().getCodigo(), getMatriculaVO().getMatricula(), getDataInicio(), getDataFim(), getAno(), getSemestre(), getFiltroPeriodicidade()));
        getSetranspVO().getArquivo().setNome(getFacadeFactory().getArquivoFacade().criarNomeArquivoSetransp(getValorConsultaFiltros(), getUsuarioLogado()));
        setSetranspVO(getFacadeFactory().getSetranspFacade().gerarArquivo(getSetranspVO(), getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoTemp() + File.separator
                + PastaBaseArquivoEnum.SETRANSP_TMP.getValue(), getUsuarioLogado()));
        getSetranspVO().getArquivo().setOrigem(OrigemArquivo.SETRANSP.getValor());
        getSetranspVO().setResponsavel(getUsuarioLogadoClone());
    }

    public void selecionarFiltro() throws Exception {
        setarFalseNosFiltros();
        setarNewNosObjetosDeConsulta();
        if (getValorConsultaFiltros().equals("curso")) {
            setFiltroCurso(true);
            montarListaSelectItemCurso();
        } else if (getValorConsultaFiltros().equals("unidadeEnsino")) {
            setFiltroUnidadeEnsino(true);
            montarListaSelectItemUnidadeEnsino();
        } else if (getValorConsultaFiltros().equals("turma")) {
            setFiltroTurma(true);
        } else if (getValorConsultaFiltros().equals("aluno")) {
            setFiltroAluno(true);
        }
    }

    private void setarFalseNosFiltros() {
        setFiltroAluno(false);
        setFiltroCurso(false);
        setFiltroTurma(false);
        setFiltroUnidadeEnsino(false);
    }

    private void setarNewNosObjetosDeConsulta() {
        setCursoVO(new CursoVO());
        setTurmaVO(new TurmaVO());
        setUnidadeEnsinoVO(new UnidadeEnsinoVO());
        setMatriculaVO(new MatriculaVO());
    }

    public String consultarTurma() {
        try {
            super.consultar();
            List objs = new ArrayList(0);
            if (getCampoConsultaTurma().equals("identificadorTurma")) {
                objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(getValorConsultaTurma(), super.getUnidadeEnsinoLogado().getCodigo(), null, null, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }

            if (getCampoConsultaTurma().equals("nomeUnidadeEnsino")) {
                objs = getFacadeFactory().getTurmaFacade().consultarPorNomeUnidadeEnsino(getValorConsultaTurma(), super.getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }

            if (getCampoConsultaTurma().equals("nomeCurso")) {
                objs = getFacadeFactory().getTurmaFacade().consultarPorNomeCursoUnidadeEnsino(getValorConsultaTurma(), getUnidadeEnsinoVO().getCodigo().intValue(), false,
                        Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }

            if (getCampoConsultaTurma().equals("nomeTurno")) {
                objs = getFacadeFactory().getTurmaFacade().consultarPorNomeTurno(getValorConsultaTurma(), super.getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            setListaConsultaTurma(objs);
            setMensagemID("msg_dados_consultados");
            return "consultarTurma";
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "consultarTurma";
        }

    }

    public void selecionarTurma() {
        TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
        setTurmaVO(obj);
        setCampoConsultaTurma("");
        setValorConsultaTurma("");
        setListaConsultaTurma(new ArrayList(0));
    }

    public List getTipoConsultaComboTurma() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("identificadorTurma", "Identificador"));
        itens.add(new SelectItem("nomeUnidadeEnsino", "Unidade Ensino"));
        itens.add(new SelectItem("nomeTurno", "Turno"));
        itens.add(new SelectItem("nomeCurso", "Curso"));
        return itens;
    }

    public void limparDadosTurma() {
        setTurmaVO(new TurmaVO());
    }

    public void consultarAluno() {
        try {
            List objs = new ArrayList(0);
            if (getValorConsultaAluno().equals("")) {
                setMensagemID("msg_entre_prmconsulta");
                return;
            }
            if (getCampoConsultaAluno().equals("matricula")) {
                objs.add(getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatricula(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false,
                        Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));
            }
            if (getCampoConsultaAluno().equals("nomePessoa")) {
                objs = getFacadeFactory().getMatriculaFacade().consultarPorNomePessoa(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            }
            if (getCampoConsultaAluno().equals("nomeCurso")) {
                objs = getFacadeFactory().getMatriculaFacade().consultarPorNomeCurso(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
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
            MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatricula(getMatriculaVO().getMatricula(), this.getUnidadeEnsinoLogado().getCodigo(), false,
                    Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            if (objAluno.getMatricula().equals("")) {
                throw new Exception("Aluno de matrícula " + getMatriculaVO().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
            }
            setMatriculaVO(objAluno);

            setCampoConsultaAluno("");
            setValorConsultaAluno("");
            setListaConsultaAluno(new ArrayList(0));

        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarAluno() throws Exception {
        MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
        setMatriculaVO(obj);
        setCampoConsultaAluno("");
        setValorConsultaAluno("");
        setListaConsultaAluno(new ArrayList(0));
    }

    public void limparDadosAluno() {
        setMatriculaVO(new MatriculaVO());
    }

    public List<SelectItem> getTipoConsultaComboAluno() {
        List<SelectItem> itens = new ArrayList<SelectItem>();
        itens.add(new SelectItem("nomePessoa", "Nome Aluno"));
        itens.add(new SelectItem("matricula", "Matrícula"));
        return itens;
    }

    /**
     * Rotina responsável por preencher a combo de consulta da telas.
     */
    public List getTipoConsultaCombo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("codigo", "Código"));
        itens.add(new SelectItem("dataGeracao", "Data Geração"));
        itens.add(new SelectItem("responsavel", "Responsável"));
        return itens;
    }

    public String selecionarArquivoSetransp() {
        try {
            SetranspVO obj = (SetranspVO) context().getExternalContext().getRequestMap().get("setranspItens");
            setSetranspVO(obj);
            return "";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
        return "";
    }

    public String getDownloadArquivoTelaForm() {
        try {
//			selecionarArquivoSetransp();
            HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
            request.setAttribute("codigoArquivo", getSetranspVO().getArquivo().getCodigo());
//            request.setAttribute("urlAcessoArquivo", getFacadeFactory().getArquivoFacade().executarDefinicaoUrlAcessoArquivo(getSetranspVO().getArquivo(), PastaBaseArquivoEnum.SETRANSP, getConfiguracaoGeralPadraoSistema(), request.getRemoteAddr()));
            context().getExternalContext().dispatch("/DownloadSV");
            FacesContext.getCurrentInstance().responseComplete();
            return "";
//			return ("DownloadSV?arquivo=" +h getSetranspVO().getArquivo().getCodigo());
        } catch (Exception e) {
            setMensagemDetalhada("Erro");
        }
        return "";
    }

    public String getDownloadArquivoTelaCons() {
        try {
            selecionarArquivoSetransp();
            HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
            request.setAttribute("codigoArquivo", getSetranspVO().getArquivo().getCodigo());
//            request.setAttribute("urlAcessoArquivo", getFacadeFactory().getArquivoFacade().executarDefinicaoUrlAcessoArquivo(getSetranspVO().getArquivo(), PastaBaseArquivoEnum.SETRANSP, getConfiguracaoGeralPadraoSistema(), request.getRemoteAddr()));
            context().getExternalContext().dispatch("/DownloadSV");
            FacesContext.getCurrentInstance().responseComplete();
            return "";
//			return ("DownloadSV?arquivo=" + getSetranspVO().getArquivo().getCodigo());
        } catch (Exception e) {
            setMensagemDetalhada("Erro");
        }
        return "";
    }

    public boolean isApresentarBotaoDownload() {
        return !getSetranspVO().isNovoObj();
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

    public void montarListaSelectItemCurso() throws Exception {
        try {
            List<CursoVO> resultadoConsulta = consultarCursoPorNome("");
            setListaSelectItemCurso(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    private List<CursoVO> consultarCursoPorNome(String nome) throws Exception {
        List<CursoVO> lista = getFacadeFactory().getCursoFacade().consultarPorNome(nome, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
        return lista;
    }

    public String inicializarConsultar() {
        removerObjetoMemoria(this);
        setListaConsulta(new ArrayList(0));
        setMensagemID("msg_entre_prmconsulta");
        return Uteis.getCaminhoRedirecionamentoNavegacao("setranspCons.xhtml");
    }

    public SetranspVO getSetranspVO() {
        if (setranspVO == null) {
            setranspVO = new SetranspVO();
        }
        return setranspVO;
    }

    public void setSetranspVO(SetranspVO setranspVO) {
        this.setranspVO = setranspVO;
    }

    public String getValorConsultaFiltros() {
        if (valorConsultaFiltros == null) {
            valorConsultaFiltros = "";
        }
        return valorConsultaFiltros;
    }

    public void setValorConsultaFiltros(String valorConsultaFiltros) {
        this.valorConsultaFiltros = valorConsultaFiltros;
    }

    public Boolean getFiltroUnidadeEnsino() {
        if (filtroUnidadeEnsino == null) {
            filtroUnidadeEnsino = false;
        }
        return filtroUnidadeEnsino;
    }

    public void setFiltroUnidadeEnsino(Boolean filtroUnidadeEnsino) {
        this.filtroUnidadeEnsino = filtroUnidadeEnsino;
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

    public Boolean getFiltroAluno() {
        if (filtroAluno == null) {
            filtroAluno = false;
        }
        return filtroAluno;
    }

    public void setFiltroAluno(Boolean filtroAluno) {
        this.filtroAluno = filtroAluno;
    }

    public List getListaSelectItemCurso() {
        if (listaSelectItemCurso == null) {
            listaSelectItemCurso = new ArrayList(0);
        }
        return listaSelectItemCurso;
    }

    public void setListaSelectItemCurso(List listaSelectItemCurso) {
        this.listaSelectItemCurso = listaSelectItemCurso;
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

    public MatriculaVO getMatriculaVO() {
        if (matriculaVO == null) {
            matriculaVO = new MatriculaVO();
        }
        return matriculaVO;
    }

    public void setMatriculaVO(MatriculaVO matriculaVO) {
        this.matriculaVO = matriculaVO;
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

    public String getValorConsultaTurma() {
        if (valorConsultaTurma == null) {
            valorConsultaTurma = "";
        }
        return valorConsultaTurma;
    }

    public void setValorConsultaTurma(String valorConsultaTurma) {
        this.valorConsultaTurma = valorConsultaTurma;
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

    public List getListaConsultaAluno() {
        if (listaConsultaAluno == null) {
            listaConsultaAluno = new ArrayList(0);
        }
        return listaConsultaAluno;
    }

    public void setListaConsultaAluno(List listaConsultaAluno) {
        this.listaConsultaAluno = listaConsultaAluno;
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

    public String getCampoConsultaAluno() {
        if (campoConsultaAluno == null) {
            campoConsultaAluno = "";
        }
        return campoConsultaAluno;
    }

    public void setCampoConsultaAluno(String campoConsultaAluno) {
        this.campoConsultaAluno = campoConsultaAluno;
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

    public List getListaSelectItemUnidadeEnsino() {
        if (listaSelectItemUnidadeEnsino == null) {
            listaSelectItemUnidadeEnsino = new ArrayList(0);
        }
        return listaSelectItemUnidadeEnsino;
    }

    public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
        this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
    }

    public void setCursoVO(CursoVO cursoVO) {
        this.cursoVO = cursoVO;
    }

    public CursoVO getCursoVO() {
        if (cursoVO == null) {
            cursoVO = new CursoVO();
        }
        return cursoVO;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }
    
    public List<SelectItem> getListaSelectItemFiltroPeriodicidade() {
    	List<SelectItem> listaFiltro = Arrays.asList(
    			new SelectItem("AN", "Anual"), 
    			new SelectItem("IN", "Integral"), 
    			new SelectItem("SE", "Semestral"));
    	return listaFiltro;
    }
    
    public List<SelectItem> getListaSelectItemSemestre(){ 
    	List<SelectItem> listaFiltro = Arrays.asList(
    			new SelectItem("", ""),
    			new SelectItem("1", "1"),
    			new SelectItem("2", "2"));
    	return listaFiltro;
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

	public String getFiltroPeriodicidade() {
		if (filtroPeriodicidade == null) {
			filtroPeriodicidade = "AN";
		}
		return filtroPeriodicidade;
	}

	public void setFiltroPeriodicidade(String filtroPeriodicidade) {
		this.filtroPeriodicidade = filtroPeriodicidade;
	}

	public boolean getIsFiltroAnual() {
		return getFiltroPeriodicidade().equals("AN");
	}

	public boolean getIsFiltroSemestral() {
		return getFiltroPeriodicidade().equals("SE");
	}

	public boolean getIsFiltroIntegral() {
		return getFiltroPeriodicidade().equals("IN");
	}
}
