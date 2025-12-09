package relatorio.controle.academico;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.utilitarias.Uteis;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.academico.DeclaracaoCancelamentoMatriculaVO;
import relatorio.negocio.interfaces.academico.DeclaracaoCancelamentoMatriculaRelInterfaceFacade;
import relatorio.negocio.jdbc.academico.DeclaracaoCancelamentoMatriculaRel;

@SuppressWarnings("unchecked")
@Controller("DeclaracaoCancelamentoMatriculaRelControle")
@Scope("request")
@Lazy
public class DeclaracaoCancelamentoMatriculaRelControle extends SuperControleRelatorio {

    protected DeclaracaoCancelamentoMatriculaRel declaracaoCancelamentoMatriculaRel;
    protected List listaConsultaAluno;
    private String valorConsultaAluno;
    private String campoConsultaAluno;
    private DeclaracaoCancelamentoMatriculaVO declaracaoCancelamentoMatriculaVO;
    private List listaCancelamentoMatricula;

    public DeclaracaoCancelamentoMatriculaRelControle() throws Exception {
        setDeclaracaoCancelamentoMatriculaRel(new DeclaracaoCancelamentoMatriculaRel());
        setListaConsultaAluno(new ArrayList(0));
        setCampoConsultaAluno("");
        setValorConsultaAluno("");
        setDeclaracaoCancelamentoMatriculaVO(new DeclaracaoCancelamentoMatriculaVO());
        setListaCancelamentoMatricula(new ArrayList<DeclaracaoCancelamentoMatriculaVO>(0));
        //obterUsuarioLogado();
        setMensagemID("msg_entre_prmrelatorio");
    }

    public void imprimirPDF() {
        try {
            String titulo = "Declaração Cancelamento de Matrícula";
            String nomeEntidade = super.getUnidadeEnsinoLogado().getNome();
            String design = declaracaoCancelamentoMatriculaRel.getDesignIReportRelatorio();
            apresentarRelatorioObjetos(declaracaoCancelamentoMatriculaRel.getIdEntidade(), titulo, nomeEntidade, "", "PDF", "", design, getUsuarioLogado().getNome(), "",
                    getListaCancelamentoMatricula(), "");
            setMensagemID("msg_relatorio_ok");

        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            removerObjetoMemoria(this);
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
            MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatricula(getDeclaracaoCancelamentoMatriculaVO().getMatricula(),
                    this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            if (objAluno.getMatricula().equals("")) {
                throw new Exception("Aluno de matrícula " + getDeclaracaoCancelamentoMatriculaVO().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
            }
            setDeclaracaoCancelamentoMatriculaVO(getFacadeFactory().getDeclaracaoCancelamentoMatriculaRelFacade().consultarPorCodigoAluno(objAluno, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));
            if (getListaCancelamentoMatricula().size() == 0) {
                getListaCancelamentoMatricula().add(getDeclaracaoCancelamentoMatriculaVO());
            } else {
                getListaCancelamentoMatricula().set(0, getDeclaracaoCancelamentoMatriculaVO());
            }
            setCampoConsultaAluno("");
            setValorConsultaAluno("");
            setListaConsultaAluno(new ArrayList(0));

        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarAluno() throws Exception {
        MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matricula");
        setDeclaracaoCancelamentoMatriculaVO(getFacadeFactory().getDeclaracaoCancelamentoMatriculaRelFacade().consultarPorCodigoAluno(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));
        if (getListaCancelamentoMatricula().size() == 0) {
            getListaCancelamentoMatricula().add(getDeclaracaoCancelamentoMatriculaVO());
        } else {
            getListaCancelamentoMatricula().set(0, getDeclaracaoCancelamentoMatriculaVO());
        }
        setCampoConsultaAluno("");
        setValorConsultaAluno("");
        setListaConsultaAluno(new ArrayList(0));

    }

    public void limparDadosAluno() {
        getDeclaracaoCancelamentoMatriculaVO().setMatricula("");
        getDeclaracaoCancelamentoMatriculaVO().setNome("");
    }

    public List<SelectItem> getTipoConsultaComboAluno() {
        List<SelectItem> itens = new ArrayList<SelectItem>();
        itens.add(new SelectItem("matricula", "Matrícula"));
        itens.add(new SelectItem("nomePessoa", "Nome Aluno"));
        itens.add(new SelectItem("nomeCurso", "Nome Curso"));
        return itens;
    }

    /**
     * @return the valorConsultaAluno
     */
    public String getValorConsultaAluno() {
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
     * @return the declaracaoCancelamentoMatriculaRel
     */
    public DeclaracaoCancelamentoMatriculaRelInterfaceFacade getDeclaracaoCancelamentoMatriculaRel() {
        return declaracaoCancelamentoMatriculaRel;
    }

    /**
     * @param declaracaoCancelamentoMatriculaRel
     *            the declaracaoCancelamentoMatriculaRel to set
     */
    public void setDeclaracaoCancelamentoMatriculaRel(DeclaracaoCancelamentoMatriculaRel declaracaoCancelamentoMatriculaRel) {
        this.declaracaoCancelamentoMatriculaRel = declaracaoCancelamentoMatriculaRel;
    }

    /**
     * @return the declaracaoCancelamentoMatriculaVO
     */
    public DeclaracaoCancelamentoMatriculaVO getDeclaracaoCancelamentoMatriculaVO() {
        return declaracaoCancelamentoMatriculaVO;
    }

    /**
     * @param declaracaoCancelamentoMatriculaVO
     *            the declaracaoCancelamentoMatriculaVO to set
     */
    public void setDeclaracaoCancelamentoMatriculaVO(DeclaracaoCancelamentoMatriculaVO declaracaoCancelamentoMatriculaVO) {
        this.declaracaoCancelamentoMatriculaVO = declaracaoCancelamentoMatriculaVO;
    }

    /**
     * @return the listaCancelamentoMatricula
     */
    public List getListaCancelamentoMatricula() {
        return listaCancelamentoMatricula;
    }

    /**
     * @param listaCancelamentoMatricula
     *            the listaCancelamentoMatricula to set
     */
    public void setListaCancelamentoMatricula(List listaCancelamentoMatricula) {
        this.listaCancelamentoMatricula = listaCancelamentoMatricula;
    }
}
