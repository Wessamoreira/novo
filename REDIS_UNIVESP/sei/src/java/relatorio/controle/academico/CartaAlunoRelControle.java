package relatorio.controle.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.utilitarias.Uteis;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.academico.CartaAlunoVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.interfaces.academico.CartaAlunoRelInterfaceFacade;
import relatorio.negocio.jdbc.academico.CartaAlunoRel;

@SuppressWarnings("unchecked")
@Controller("CartaAlunoRelControle")
@Scope("viewScope")
@Lazy
public class CartaAlunoRelControle extends SuperControleRelatorio {

    protected List listaConsultaAluno;
    private String valorConsultaAluno;
    private String campoConsultaAluno;
    private CartaAlunoVO cartaAlunoVO;
    private List listaCartaAluno;
    private MatriculaVO alunoVO;

    public CartaAlunoRelControle() throws Exception {
        setMensagemID("msg_entre_prmrelatorio");
    }

    public void imprimirPDF() {
        String titulo = null;
        String nomeEntidade = null;
        String design = null;
        try {
            registrarAtividadeUsuario(getUsuarioLogado(), "CartaAlunoRelControle", "Inicializando Geração de Relatório Carta ao Aluno", "Emitindo Relatório");
            CartaAlunoRel.validarDados(getAlunoVO());
            titulo = "Feliz Aniversário";
            nomeEntidade = super.getUnidadeEnsinoLogado().getNome();
            design = CartaAlunoRel.getDesignIReportRelatorio();
            setCartaAlunoVO(getFacadeFactory().getCartaAlunoRelFacade().consultarPorCodigoAluno(getAlunoVO(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));
            getCartaAlunoVO().setCaminhoImagem(obterCaminhoWebImagem() + File.separator + "Presente.jpg");
            if (getListaCartaAluno().isEmpty()) {
                getListaCartaAluno().add(getCartaAlunoVO());
            } else {
                getListaCartaAluno().set(0, getCartaAlunoVO());
            }
            if (!getListaCartaAluno().isEmpty()) {
                getSuperParametroRelVO().setTituloRelatorio(titulo);
                getSuperParametroRelVO().setNomeDesignIreport(design);
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
                getSuperParametroRelVO().setSubReport_Dir(CartaAlunoRel.caminhoBaseRelatorio());
                getSuperParametroRelVO().setCaminhoBaseRelatorio(CartaAlunoRel.caminhoBaseRelatorio());
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getSuperParametroRelVO().setUnidadeEnsino(nomeEntidade);
                getSuperParametroRelVO().setListaObjetos(getListaCartaAluno());
                setMensagemID("msg_relatorio_ok");
                realizarImpressaoRelatorio();
                removerObjetoMemoria(this);
            } else {
                setMensagemID("msg_relatorio_sem_dados");
            }
            registrarAtividadeUsuario(getUsuarioLogado(), "CartaAlunoRelControle", "Finalizando Geração de Relatório Carta ao Aluno", "Emitindo Relatório");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            titulo = null;
            nomeEntidade = null;
            design = null;
            Uteis.liberarListaMemoria(getListaCartaAluno());
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
                        Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));
            }
            if (getCampoConsultaAluno().equals("nomePessoa")) {
                objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
            }
            if (getCampoConsultaAluno().equals("nomeCurso")) {
                objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCurso(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
            }
            setListaConsultaAluno(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            getListaConsultaAluno().clear();
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void consultarAlunoPorMatricula() throws Exception {
        try {
            MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatricula(getAlunoVO().getMatricula(), this.getUnidadeEnsinoLogado().getCodigo(), false,
                    Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            if (objAluno.getMatricula().equals("")) {
                throw new Exception("Aluno de matrícula " + getAlunoVO().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
            }
            setAlunoVO(objAluno);
            setCampoConsultaAluno("");
            setValorConsultaAluno("");
            setListaConsultaAluno(new ArrayList(0));
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarAluno() throws Exception {
        setAlunoVO((MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens"));
        setAlunoVO(getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatricula(getAlunoVO().getMatricula(), this.getUnidadeEnsinoLogado().getCodigo(), false,
                Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));
        setCampoConsultaAluno("");
        setValorConsultaAluno("");
        setListaConsultaAluno(new ArrayList(0));
        setMensagemID("msg_dados_consultados");
    }

    public void limparDadosAluno() {
        setAlunoVO(new MatriculaVO());
    }

    public List<SelectItem> getTipoConsultaComboAluno() {
        List<SelectItem> itens = new ArrayList<SelectItem>();        
        itens.add(new SelectItem("nomePessoa", "Nome Aluno"));
        itens.add(new SelectItem("matricula", "Matrícula"));
        itens.add(new SelectItem("nomeCurso", "Nome Curso"));
        return itens;
    }

    /**
     * @return the valorConsultaAluno
     */
    public String getValorConsultaAluno() {
        if(valorConsultaAluno == null){
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
        if(campoConsultaAluno == null){
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
        if(listaConsultaAluno == null){
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
     * @return the cartaAlunoVO
     */
    public CartaAlunoVO getCartaAlunoVO() {
        if(cartaAlunoVO == null){
            cartaAlunoVO = new CartaAlunoVO();
        }
        return cartaAlunoVO;
    }

    /**
     * @param cartaAlunoVO
     *            the cartaAlunoVO to set
     */
    public void setCartaAlunoVO(CartaAlunoVO cartaAlunoVO) {
        this.cartaAlunoVO = cartaAlunoVO;
    }

    /**
     * @return the listaCartaAluno
     */
    public List getListaCartaAluno() {
        if(listaCartaAluno == null){
            listaCartaAluno = new ArrayList(0);
        }
        return listaCartaAluno;
    }

    /**
     * @param listaCartaAluno
     *            the listaCartaAluno to set
     */
    public void setListaCartaAluno(List listaCartaAluno) {
        this.listaCartaAluno = listaCartaAluno;
    }

    public MatriculaVO getAlunoVO() {
        if (alunoVO == null) {
            alunoVO = new MatriculaVO();
        }
        return alunoVO;
    }

    public void setAlunoVO(MatriculaVO alunoVO) {
        this.alunoVO = alunoVO;
    }
}
