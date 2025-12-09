package controle.academico;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

@Controller("ConsultorResponsavelMatriculaControle")
@Scope("viewScope")
@Lazy
public class ConsultorResponsavelMatriculaControle extends SuperControle implements Serializable {

    private MatriculaVO matriculaVO;
    private UnidadeEnsinoVO unidadeEnsinoVO;
    private FuncionarioVO consultorSubstituto;
    private String valorConsultaFuncionario;
    private String campoConsultaFuncionario;
    private List listaConsultaFuncionario;
    private String valorConsultaAluno;
    private String campoConsultaAluno;
    private List listaConsultaAluno;
    private List listaSelectItemUnidadeEnsino;
    private Boolean perfilAlterarConsultor;
    private Boolean apresentarCampoNovo;

    public ConsultorResponsavelMatriculaControle() throws Exception {
        inicializarListasSelectItemTodosComboBox();
        setMensagemID("msg_entre_prmconsulta");
    }

    /**
     * Rotina responsável por disponibilizar um novo objeto da classe <code>Censo</code> para edição pelo usuário da
     * aplicação.
     */
    public void novo() {
        try {
            limparDadosAluno();
            limparDadosFuncionario();
            setMensagemID("msg_entre_dados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void persistir() {
        try {
            getFacadeFactory().getMatriculaFacade().validarDadosAlteracaoConsultorMatricula(getMatriculaVO().getMatricula());
            verificarPermissaoUsuarioVisualizarConsultorMatricula();
            if (getPerfilAlterarConsultor()) {
                getFacadeFactory().getMatriculaFacade().alterarConsultorResponsavelMatricula(getMatriculaVO().getMatricula(), getMatriculaVO().getConsultor().getCodigo(), getConsultorSubstituto(), getUsuarioLogado());
            } else {
                throw new Exception("Usuário não possui permissão para alterar o consultor!");
            }
            setApresentarCampoNovo(Boolean.TRUE);
            setMensagemID("msg_AlterarConsultorMatricula_confirmacao");
        } catch (Exception ex) {
            setMensagemDetalhada("msg_erro", ex.getMessage());
        }

    }

    public static void verificarPermissaoUsuarioVisualizarConsultorMatricula(UsuarioVO usuario, String nomeEntidade) throws Exception {
        ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico(nomeEntidade, usuario);
    }

    public void verificarPermissaoUsuarioVisualizarConsultorMatricula() {
        Boolean liberar = false;
        try {
            verificarPermissaoUsuarioVisualizarConsultorMatricula(getUsuarioLogado(), "ApresentarConsultorMatricula");
            liberar = true;
        } catch (Exception e) {
            liberar = false;
        }
        this.setPerfilAlterarConsultor(liberar);
    }

    public void inicializarListasSelectItemTodosComboBox() {
        montarListaSelectItemUnidadeEnsino();
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
        List<UnidadeEnsinoVO> lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm,
                super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
        return lista;
    }

    public void consultarAluno() {
        try {
            getFacadeFactory().getUnidadeEnsinoFacade().validarDadosUnidadeEnsino(getUnidadeEnsinoVO().getCodigo());
            getListaConsultaAluno().clear();

            if (getValorConsultaAluno().equals("")) {
                throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
            }
            if (getCampoConsultaAluno().equals("matricula")) {
                MatriculaVO obj = getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatriculaUnica(getValorConsultaAluno(), getUnidadeEnsinoVO().getCodigo(), false, "", getUsuarioLogado());
                if (!obj.getMatricula().equals("")) {
                    getListaConsultaAluno().add(obj);
                }
            }
            if (getCampoConsultaAluno().equals("nomePessoa")) {
                setListaConsultaAluno(getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(), this.getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado()));
            }
            if (getCampoConsultaAluno().equals("nomeCurso")) {
                setListaConsultaAluno(getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCurso(getValorConsultaAluno(), this.getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado()));
            }
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            getListaConsultaAluno().clear();
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void consultarAlunoPorMatricula() {
        try {
        	getFacadeFactory().getUnidadeEnsinoFacade().validarDadosUnidadeEnsino(getUnidadeEnsinoVO().getCodigo());
            MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatriculaUnica(getMatriculaVO().getMatricula(),
                    this.getUnidadeEnsinoVO().getCodigo(), false, "", getUsuarioLogado());
            if (objAluno.getMatricula().equals("")) {
                throw new Exception("Aluno de matrícula " + getMatriculaVO().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto, ou se o aluno pertence a unidade de ensino selecionada.");
            }
            if (objAluno.getConsultor().getCodigo() != 0) {
                //objAluno.setConsultor(getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCodigoPessoa(objAluno.getConsultor().getCodigo(), false, getUsuarioLogado()));
                objAluno.setConsultor(getFacadeFactory().getFuncionarioFacade().consultaRapidaPorChavePrimaria(objAluno.getConsultor().getCodigo(), false, getUsuarioLogado()));
            }
            this.setMatriculaVO(objAluno);
            getFacadeFactory().getMatriculaFacade().carregarDados(this.getMatriculaVO(), NivelMontarDados.BASICO, getUsuarioLogado());
            setConsultorSubstituto(null);
            setMensagemDetalhada("");
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            this.setMatriculaVO(new MatriculaVO());
        }
    }

    public void selecionarAluno() throws Exception {
        MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("alunoItens");
        getFacadeFactory().getMatriculaFacade().carregarDados(obj, NivelMontarDados.BASICO, getUsuarioLogado());
        if (obj.getConsultor().getCodigo() != 0) {
            //obj.setConsultor(getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCodigoPessoa(obj.getConsultor().getCodigo(), false, getUsuarioLogado()));
            obj.setConsultor(getFacadeFactory().getFuncionarioFacade().consultaRapidaPorChavePrimaria(obj.getConsultor().getCodigo(), false, getUsuarioLogado()));
        }
        setMatriculaVO(obj);
        setConsultorSubstituto(null);
        obj = null;
        valorConsultaAluno = "";
        campoConsultaAluno = "";
        getListaConsultaAluno().clear();
    }

    public void limparDadosAluno() throws Exception {
        setMatriculaVO(null);
    }

    public void selecionarFuncionario() {
        FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItens");
        setConsultorSubstituto(obj);
    }

    public void limparConsultaFuncionario() {
        getListaConsultaFuncionario().clear();
    }

    public void limparDadosFuncionario() {
        setConsultorSubstituto(null);
    }

    public List getTipoConsultaComboAluno() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nomePessoa", "Aluno"));
        itens.add(new SelectItem("matricula", "Matrícula"));
        itens.add(new SelectItem("nomeCurso", "Curso"));
        return itens;
    }

    public void consultarFuncionario() {
        try {
            List objs = new ArrayList(0);
            if (getValorConsultaFuncionario().equals("")) {
                setMensagemID("msg_entre_prmconsulta");
                return;
            }
            if (getCampoConsultaFuncionario().equals("nome")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNome(getValorConsultaFuncionario(), "!PR", 0, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("matricula")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorMatricula(getValorConsultaFuncionario(), 0, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("CPF")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCPF(getValorConsultaFuncionario(), "!PR", 0, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("cargo")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCargo(getValorConsultaFuncionario(), 0, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("departamento")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNomeDepartamento(getValorConsultaFuncionario(), "FU", 0, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("unidadeEnsino")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorUnidadeEnsino(getValorConsultaFuncionario(), "!PR", 0, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            setListaConsultaFuncionario(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
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
     * @return the matriculaVO
     */
    public MatriculaVO getMatriculaVO() {
        if (matriculaVO == null) {
            matriculaVO = new MatriculaVO();
        }
        return matriculaVO;
    }

    /**
     * @param matriculaVO the matriculaVO to set
     */
    public void setMatriculaVO(MatriculaVO matriculaVO) {
        this.matriculaVO = matriculaVO;
    }

    /**
     * @return the valorConsultaFuncionario
     */
    public String getValorConsultaFuncionario() {
        if (valorConsultaFuncionario == null) {
            valorConsultaFuncionario = "";
        }
        return valorConsultaFuncionario;
    }

    /**
     * @param valorConsultaFuncionario the valorConsultaFuncionario to set
     */
    public void setValorConsultaFuncionario(String valorConsultaFuncionario) {
        this.valorConsultaFuncionario = valorConsultaFuncionario;
    }

    /**
     * @return the campoConsultaFuncionario
     */
    public String getCampoConsultaFuncionario() {
        if (campoConsultaFuncionario == null) {
            campoConsultaFuncionario = "";
        }
        return campoConsultaFuncionario;
    }

    /**
     * @param campoConsultaFuncionario the campoConsultaFuncionario to set
     */
    public void setCampoConsultaFuncionario(String campoConsultaFuncionario) {
        this.campoConsultaFuncionario = campoConsultaFuncionario;
    }

    /**
     * @return the listaConsultaFuncionario
     */
    public List getListaConsultaFuncionario() {
        if (listaConsultaFuncionario == null) {
            listaConsultaFuncionario = new ArrayList(0);
        }
        return listaConsultaFuncionario;
    }

    /**
     * @param listaConsultaFuncionario the listaConsultaFuncionario to set
     */
    public void setListaConsultaFuncionario(List listaConsultaFuncionario) {
        this.listaConsultaFuncionario = listaConsultaFuncionario;
    }

    /**
     * @return the valorConsultaMatricula
     */
    public String getValorConsultaAluno() {
        if (valorConsultaAluno == null) {
            valorConsultaAluno = "";
        }
        return valorConsultaAluno;
    }

    /**
     * @param valorConsultaAluno the valorConsultaMatricula to set
     */
    public void setValorConsultaAluno(String valorConsultaAluno) {
        this.valorConsultaAluno = valorConsultaAluno;
    }

    /**
     * @return the campoConsultaMatricula
     */
    public String getCampoConsultaAluno() {
        if (campoConsultaAluno == null) {
            campoConsultaAluno = "";
        }
        return campoConsultaAluno;
    }

    /**
     * @param campoConsultaAluno the campoConsultaMatricula to set
     */
    public void setCampoConsultaAluno(String campoConsultaAluno) {
        this.campoConsultaAluno = campoConsultaAluno;
    }

    /**
     * @return the listaConsultaMatricula
     */
    public List getListaConsultaAluno() {
        if (listaConsultaAluno == null) {
            listaConsultaAluno = new ArrayList(0);
        }
        return listaConsultaAluno;
    }

    /**
     * @param listaConsultaAluno the listaConsultaMatricula to set
     */
    public void setListaConsultaAluno(List listaConsultaAluno) {
        this.listaConsultaAluno = listaConsultaAluno;
    }

    /**
     * @return the listaSelectItemUnidadeEnsino
     */
    public List getListaSelectItemUnidadeEnsino() {
        if (listaSelectItemUnidadeEnsino == null) {
            listaSelectItemUnidadeEnsino = new ArrayList(0);
        }
        return listaSelectItemUnidadeEnsino;
    }

    /**
     * @param listaSelectItemUnidadeEnsino the listaSelectItemUnidadeEnsino to set
     */
    public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
        this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
    }

    /**
     * @return the unidadeEnsinoVO
     */
    public UnidadeEnsinoVO getUnidadeEnsinoVO() {
        if (unidadeEnsinoVO == null) {
            unidadeEnsinoVO = new UnidadeEnsinoVO();
        }
        return unidadeEnsinoVO;
    }

    /**
     * @param unidadeEnsinoVO the unidadeEnsinoVO to set
     */
    public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
        this.unidadeEnsinoVO = unidadeEnsinoVO;
    }

    /**
     * @return the perfilAlterarConsultor
     */
    public Boolean getPerfilAlterarConsultor() {
        if (perfilAlterarConsultor == null) {
            perfilAlterarConsultor = Boolean.FALSE;
        }
        return perfilAlterarConsultor;
    }

    /**
     * @param perfilAlterarConsultor the perfilAlterarConsultor to set
     */
    public void setPerfilAlterarConsultor(Boolean perfilAlterarConsultor) {
        this.perfilAlterarConsultor = perfilAlterarConsultor;
    }

    /**
     * @return the consultorSubstituto
     */
    public FuncionarioVO getConsultorSubstituto() {
        if (consultorSubstituto == null) {
            consultorSubstituto = new FuncionarioVO();
        }
        return consultorSubstituto;
    }

    /**
     * @param consultorSubstituto the consultorSubstituto to set
     */
    public void setConsultorSubstituto(FuncionarioVO consultorSubstituto) {
        this.consultorSubstituto = consultorSubstituto;
    }

    public boolean getApresentarCampoConsultor() {
        return !getMatriculaVO().getMatricula().equals("");
    }

    /**
     * @return the apresentarCampoNovo
     */
    public Boolean getApresentarCampoNovo() {
        if (apresentarCampoNovo == null) {
            apresentarCampoNovo = false;
        }
        return apresentarCampoNovo;
    }

    /**
     * @param apresentarCampoNovo the apresentarCampoNovo to set
     */
    public void setApresentarCampoNovo(Boolean apresentarCampoNovo) {
        this.apresentarCampoNovo = apresentarCampoNovo;
    }
}