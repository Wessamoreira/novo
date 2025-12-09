package relatorio.controle.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;
import negocio.comuns.academico.DisciplinaVO;

import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.utilitarias.Uteis;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.academico.DeclaracaoFrequenciaVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.academico.DeclaracaoFrequenciaRel;

@SuppressWarnings("unchecked")
@Controller("DeclaracaoFrequenciaRelControle")
@Scope("viewScope")
@Lazy
public class DeclaracaoFrequenciaRelControle extends SuperControleRelatorio {

    private List listaConsultaAluno;
    private String valorConsultaAluno;
    private String campoConsultaAluno;
    private DeclaracaoFrequenciaVO declaracaoFrequenciaVO;
    private List listaDeclaracao;
    private Boolean mostrarCamposAnoSemestre;
    private String ano;
    private String semestre;
    private Date dataDeclaracao;
    private List listaSelectItemTurmaDisciplina;
    private String campoConsultaDisciplinaMatricula;
    private String valorConsultaDisciplinaMatricula;
    private List listaConsultaDisciplinaMatricula;
    private DisciplinaVO disciplina;
    private String valorConsultaFuncionario;
    private String campoConsultaFuncionario;
    private List<FuncionarioVO> listaConsultaFuncionario;



    public DeclaracaoFrequenciaRelControle() throws Exception {
        setMensagemID("msg_entre_prmrelatorio");
    }

    public void imprimirPDF() {
        String titulo = "";
        if (getDeclaracaoFrequenciaVO().getTipoDeclaracao().intValue() == 1) {
            titulo = "Declaração de Frequência";
        }
        else {
            titulo = "Atestado de Frequência";
        }
        String nomeEntidade = super.getUnidadeEnsinoLogado().getNome();
        String design = DeclaracaoFrequenciaRel.getDesignIReportRelatorio(getDeclaracaoFrequenciaVO());
        List listaObjetos = new ArrayList(0);
        try {
            registrarAtividadeUsuario(getUsuarioLogado(), "DeclaracaoFrequenciaRelControle", "Inicializando Geração de Relatório Declaração Frequência", "Emitindo Relatório");
            DeclaracaoFrequenciaRel.validarDados(getDeclaracaoFrequenciaVO(),getDisciplina().getNomeDisciplinaGrade(), getDataDeclaracao());
            if (!getDisciplina().getNome().equals("")){
                getDeclaracaoFrequenciaVO().setDisciplina(getDisciplina().getNomeDisciplinaGrade());
            }
            listaObjetos.add(getFacadeFactory().getDeclaracaoFrequenciaRelFacade().consultarPorCodigoAluno(getDeclaracaoFrequenciaVO(),
                    getAno(), getSemestre(), getDataDeclaracao(), getDisciplina().getCodigo().intValue(),  Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));

            if (!listaObjetos.isEmpty()) {
                getSuperParametroRelVO().setTituloRelatorio(titulo);
                getSuperParametroRelVO().setNomeDesignIreport(design);
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
                getSuperParametroRelVO().setSubReport_Dir(DeclaracaoFrequenciaRel.getCaminhoBaseRelatorio());
                getSuperParametroRelVO().setCaminhoBaseRelatorio(DeclaracaoFrequenciaRel.getCaminhoBaseRelatorio());
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getSuperParametroRelVO().setUnidadeEnsino(nomeEntidade);
                getSuperParametroRelVO().setUsuarioVO(getUsuarioLogadoClone());
                getSuperParametroRelVO().setListaObjetos(listaObjetos);
                setMensagemID("msg_relatorio_ok");
                realizarImpressaoRelatorio();
                removerObjetoMemoria(this);
            } else {
                setMensagemID("msg_relatorio_sem_dados");
            }
            //apresentarRelatorioObjetos(DeclaracaoFrequenciaRel.getIdEntidade(), titulo, nomeEntidade, "", "PDF", "",
            //		design, getUsuarioLogado().getNome(), "", listaObjetos, "");
            //setMensagemID("msg_relatorio_ok");
            registrarAtividadeUsuario(getUsuarioLogado(), "DeclaracaoFrequenciaRelControle", "Finalizando Geração de Relatório Declaração Frequência", "Emitindo Relatório");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            titulo = null;
            design = null;
            nomeEntidade = null;
            listaObjetos = null;

        }
    }

    public void consultarAluno() {
        List objs = new ArrayList(0);
        try {
            if (getValorConsultaAluno().equals("")) {
                setMensagemID("msg_entre_prmconsulta");
                return;
            }
            if (getCampoConsultaAluno().equals("matricula")) {
                objs = (getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatricula(
                        getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado()));
            }
            if (getCampoConsultaAluno().equals("nomePessoa")) {
                objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(
                        getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
            }
            if (getCampoConsultaAluno().equals("nomeCurso")) {
                objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCurso(
                        getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
            }
            setListaConsultaAluno(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaAluno(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            objs = null;
        }
    }


    public void consultarDisciplinaMatriculaRich() {
        try {
            List objs = new ArrayList(0);
            if (getCampoConsultaDisciplinaMatricula().equals("codigo")) {
                if (getValorConsultaDisciplinaMatricula().equals("")) {
                    setValorConsultaDisciplinaMatricula("0");
                }
                if (getValorConsultaDisciplinaMatricula().trim() != null || !getValorConsultaDisciplinaMatricula().trim().isEmpty()) {
                    Uteis.validarSomenteNumeroString(getValorConsultaDisciplinaMatricula().trim());
                }
                int valorInt = Integer.parseInt(getValorConsultaDisciplinaMatricula());
                objs = getFacadeFactory().getDisciplinaFacade().consultarPorCodigo_Matricula_DisciplinaEquivalente(new Integer(valorInt), getDeclaracaoFrequenciaVO().getMatricula(), false,
                        Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getCampoConsultaDisciplinaMatricula().equals("nome")) {
                objs = getFacadeFactory().getDisciplinaFacade().consultarPorNome_Matricula_DisciplinaEquivalente(getValorConsultaDisciplinaMatricula(), getDeclaracaoFrequenciaVO().getMatricula(), false,
                        Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            setListaConsultaDisciplinaMatricula(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaDisciplinaMatricula(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }


    public void consultarFuncionarioPrincipal() throws Exception {
        try {
            getDeclaracaoFrequenciaVO().setFuncionarioPrincipalVO(consultarFuncionarioPorMatricula(getDeclaracaoFrequenciaVO().getFuncionarioPrincipalVO().getMatricula()));
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public FuncionarioVO consultarFuncionarioPorMatricula(String matricula) throws Exception {
        FuncionarioVO funcionarioVO = null;
        try {
            funcionarioVO = getFacadeFactory().getFuncionarioFacade().consultarPorMatricula(matricula, 0, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            if (Uteis.isAtributoPreenchido(funcionarioVO)) {
                return funcionarioVO;
            } else {
                setMensagemDetalhada("msg_erro", "Funcionário de matrícula " + matricula + " não encontrado. Verifique se o número de matrícula está correto.");
            }
            setMensagemDetalhada("");
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
        return new FuncionarioVO();
    }



    public void consultarAlunoPorMatricula() throws Exception {
        try {
            MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatriculaUnica(
                    getDeclaracaoFrequenciaVO().getMatricula().toUpperCase(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
            if (objAluno.getMatricula().equals("")) {
                throw new Exception("Aluno de matrícula " + getDeclaracaoFrequenciaVO().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
            }
            getDeclaracaoFrequenciaVO().setMatricula(objAluno.getMatricula());
            getDeclaracaoFrequenciaVO().setNome(objAluno.getAluno().getNome());
            if (!objAluno.getCurso().getNivelEducacionalPosGraduacao()) {
                setMostrarCamposAnoSemestre(true);
            }
        } catch (Exception e) {
            setMostrarCamposAnoSemestre(false);
            setDeclaracaoFrequenciaVO(new DeclaracaoFrequenciaVO());
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            setCampoConsultaAluno("");
            setValorConsultaAluno("");
            getListaConsultaAluno().clear();
        }
    }

    public void selecionarAluno() throws Exception {
        try {
            MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
            getDeclaracaoFrequenciaVO().setMatricula(obj.getMatricula());
            getDeclaracaoFrequenciaVO().setNome(obj.getAluno().getNome());
            if (!obj.getCurso().getNivelEducacionalPosGraduacao()) {
                setMostrarCamposAnoSemestre(true);
            }
        } catch (Exception e) {
            setMostrarCamposAnoSemestre(false);
            setDeclaracaoFrequenciaVO(new DeclaracaoFrequenciaVO());
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            setCampoConsultaAluno("");
            setValorConsultaAluno("");
            getListaConsultaAluno().clear();
        }
    }

    public List getListaSelectSemestre() {
        List lista = new ArrayList(0);
        lista.add(new SelectItem("", ""));
        lista.add(new SelectItem("1", "1º"));
        lista.add(new SelectItem("2", "2º"));
        return lista;
    }

    public List getListaSelectTipoDeclaracao() {
        List lista = new ArrayList(0);
        lista.add(new SelectItem(0, "Declaração de Frequência"));
        lista.add(new SelectItem(1, "Atestado de Frequência"));
        return lista;
    }

    public void limparDadosAluno() {
        getDeclaracaoFrequenciaVO().setMatricula("");
        getDeclaracaoFrequenciaVO().setNome("");
        setAno("");
        setSemestre("");
        setMostrarCamposAnoSemestre(false);
    }

    public List<SelectItem> getTipoConsultaComboAluno() {
        List<SelectItem> itens = new ArrayList<SelectItem>();
        itens.add(new SelectItem("nomePessoa", "Aluno"));
        itens.add(new SelectItem("matricula", "Matrícula"));
        itens.add(new SelectItem("nomeCurso", "Curso"));
        return itens;
    }

    public void limparDadosDisciplinaTurma() throws Exception {
        setDisciplina(new DisciplinaVO());
        setListaSelectItemTurmaDisciplina(new ArrayList(0));
        setMensagemID("msg_entre_prmconsulta");
    }

    public List getTipoConsultaComboFuncionario() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("matricula", "Matrícula"));
        itens.add(new SelectItem("CPF", "CPF"));
        itens.add(new SelectItem("cargo", "Cargo"));
        itens.add(new SelectItem("departamento", "Departamento"));
        return itens;
    }

    public void consultarFuncionario() {
        try {
            List objs = new ArrayList(0);
            if (getValorConsultaFuncionario().equals("")) {
                setMensagemID("msg_entre_prmconsulta");
                return;
            }
            getFacadeFactory().getPessoaFacade().setIdEntidade("Funcionario");

            if (getCampoConsultaFuncionario().equals("nome")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNome(getValorConsultaFuncionario(), "FU", 0, true, true, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("matricula")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorMatricula(getValorConsultaFuncionario(), 0, true, true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("nomeCidade")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCidade(getValorConsultaFuncionario(), 0, true, true, false,
                        Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("CPF")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCPF(getValorConsultaFuncionario(), "FU", 0, true, true, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("cargo")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCargo(getValorConsultaFuncionario(), 0, true, true, false,
                        Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("departamento")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNomeDepartamento(getValorConsultaFuncionario(), "FU", 0, true, true, false,
                        Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("unidadeEnsino")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorUnidadeEnsino(getValorConsultaFuncionario(), "FU", 0, true, true, false,
                        Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            setListaConsultaFuncionario(objs);
            setMensagemID("msg_dados_consultados");

        } catch (Exception e) {
            setListaConsultaFuncionario(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarFuncionarioPrincipal() throws Exception {
        FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioPrincipalItens");
        getDeclaracaoFrequenciaVO().setFuncionarioPrincipalVO(obj);
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
     * @param declaracaoSetranspVO
     *            the declaracaoSetranspVO to set
     */
    public void setDeclaracaoFrequenciaVO(DeclaracaoFrequenciaVO declaracaoFrequenciaVO) {
        this.declaracaoFrequenciaVO = declaracaoFrequenciaVO;
    }

    /**
     * @return the declaracaoSetranspVO
     */
    public DeclaracaoFrequenciaVO getDeclaracaoFrequenciaVO() {
        if (declaracaoFrequenciaVO == null) {
            declaracaoFrequenciaVO = new DeclaracaoFrequenciaVO();
        }
        return declaracaoFrequenciaVO;
    }

    /**
     * @param listaDeclaracao
     *            the listaDeclaracao to set
     */
    public void setListaDeclaracao(List listaDeclaracao) {
        this.listaDeclaracao = listaDeclaracao;
    }

    /**
     * @return the listaDeclaracao
     */
    public List getListaDeclaracao() {
        if (listaDeclaracao == null) {
            listaDeclaracao = new ArrayList(0);
        }
        return listaDeclaracao;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }

    public String getSemestre() {
        if (semestre == null) {
            semestre = "";
        }
        return semestre;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public String getAno() {
        if (ano == null) {
            ano = "";
        }
        return ano;
    }

    public void setMostrarCamposAnoSemestre(Boolean mostrarCamposAnoSemestre) {
        this.mostrarCamposAnoSemestre = mostrarCamposAnoSemestre;
    }

    public Boolean getMostrarCamposAnoSemestre() {
        return mostrarCamposAnoSemestre;
    }

    public Boolean getIsMostrarCamposAnoSemestre() {
        return mostrarCamposAnoSemestre;
    }

    public void setDataDeclaracao(Date dataDeclaracao) {
        this.dataDeclaracao = dataDeclaracao;
    }

    public Date getDataDeclaracao() {
        if (dataDeclaracao == null) {
            dataDeclaracao = new Date();
        }
        return dataDeclaracao;
    }

    public List getListaSelectItemTurmaDisciplina() {
        return listaSelectItemTurmaDisciplina;
    }

    public void setListaSelectItemTurmaDisciplina(List listaSelectItemTurmaDisciplina) {
        this.listaSelectItemTurmaDisciplina = listaSelectItemTurmaDisciplina;
    }

    public String getCampoConsultaDisciplinaMatricula() {
        return campoConsultaDisciplinaMatricula;
    }

    public void setCampoConsultaDisciplinaMatricula(String campoConsultaDisciplinaMatricula) {
        this.campoConsultaDisciplinaMatricula = campoConsultaDisciplinaMatricula;
    }

    public List getTipoConsultaComboDisciplina() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("codigo", "Código"));
        return itens;
    }

    public String getValorConsultaDisciplinaMatricula() {
        return valorConsultaDisciplinaMatricula;
    }

    public void setValorConsultaDisciplinaMatricula(String valorConsultaDisciplinaMatricula) {
        this.valorConsultaDisciplinaMatricula = valorConsultaDisciplinaMatricula;
    }

    public List getListaConsultaDisciplinaMatricula() {
        return listaConsultaDisciplinaMatricula;
    }

    public void setListaConsultaDisciplinaMatricula(List listaConsultaDisciplinaMatricula) {
        this.listaConsultaDisciplinaMatricula = listaConsultaDisciplinaMatricula;
    }

    public void selecionarDisciplinaMatricula() throws Exception {
        DisciplinaVO obj = (DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplinaMatriculaItens");
        setDisciplina(obj);
        setListaConsultaDisciplinaMatricula(new ArrayList(0));
        obj = null;
        setValorConsultaDisciplinaMatricula("");
        setCampoConsultaDisciplinaMatricula("");
        getListaConsultaDisciplinaMatricula().clear();
    }
    
    public DisciplinaVO getDisciplina() {
        if (disciplina == null){
            disciplina = new DisciplinaVO();
        }
        return disciplina;
    }

    public void setDisciplina(DisciplinaVO disciplina) {
        this.disciplina = disciplina;
    }

    public String getValorConsultaFuncionario() {
        if (valorConsultaFuncionario == null) {
            valorConsultaFuncionario = "";
        }
        return valorConsultaFuncionario;
    }

    public void setValorConsultaFuncionario(String valorConsultaFuncionario) {
        this.valorConsultaFuncionario = valorConsultaFuncionario;
    }

    public String getCampoConsultaFuncionario() {
        if (campoConsultaFuncionario == null) {
            campoConsultaFuncionario = "";
        }
        return campoConsultaFuncionario;
    }

    public void setCampoConsultaFuncionario(String campoConsultaFuncionario) {
        this.campoConsultaFuncionario = campoConsultaFuncionario;
    }

    public List<FuncionarioVO> getListaConsultaFuncionario() {
        if (listaConsultaFuncionario == null) {
            listaConsultaFuncionario = null;
        }
        return listaConsultaFuncionario;
    }

    public void setListaConsultaFuncionario(List<FuncionarioVO> listaConsultaFuncionario) {
        this.listaConsultaFuncionario = listaConsultaFuncionario;
    }


    public void limparDadosFuncionarioPrincipal() {
        removerObjetoMemoria(getDeclaracaoFrequenciaVO().getFuncionarioPrincipalVO());
        getDeclaracaoFrequenciaVO().setFuncionarioPrincipalVO(new FuncionarioVO());
    }



}
