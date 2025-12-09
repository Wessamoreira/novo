package relatorio.controle.academico;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.utilitarias.Uteis;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.academico.DeclaracaoSetranspVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.interfaces.academico.DeclaracaoSetranspRelInterfaceFacade;
import relatorio.negocio.jdbc.academico.DeclaracaoSetranspRel;

@SuppressWarnings("unchecked")
@Controller("DeclaracaoSetranspRelControle")
@Scope("viewScope")
@Lazy
public class DeclaracaoSetranspRelControle extends SuperControleRelatorio {

    protected DeclaracaoSetranspRel declaracaoSetranspRel;
    protected List listaConsultaAluno;
    private String valorConsultaAluno;
    private String campoConsultaAluno;
    private DeclaracaoSetranspVO declaracaoSetranspVO;
    private List listaDeclaracao;

    public DeclaracaoSetranspRelControle() throws Exception {
        setMensagemID("msg_entre_prmrelatorio");
    }

    public void imprimirPDF() {
        String titulo = "Declaração Setransp";
        String nomeEntidade = super.getUnidadeEnsinoLogado().getNome();
        String design = DeclaracaoSetranspRel.getDesignIReportRelatorio();
        try {
            registrarAtividadeUsuario(getUsuarioLogado(), "DeclaracaoSetranspRelControle", "Inicializando Geração de Relatório Declaração Setransp", "Emitindo Relatório");
            DeclaracaoSetranspRel.validarDados(getDeclaracaoSetranspVO());
            if (!getListaDeclaracao().isEmpty()) {
                getSuperParametroRelVO().setTituloRelatorio(titulo);
                getSuperParametroRelVO().setNomeDesignIreport(design);
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
                getSuperParametroRelVO().setSubReport_Dir(DeclaracaoSetranspRel.getCaminhoBaseRelatorio());
                getSuperParametroRelVO().setCaminhoBaseRelatorio(DeclaracaoSetranspRel.getCaminhoBaseRelatorio());
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getSuperParametroRelVO().setUnidadeEnsino(nomeEntidade);
                getSuperParametroRelVO().setUsuarioVO(getUsuarioLogadoClone());
                getSuperParametroRelVO().setListaObjetos(getListaDeclaracao());
                setMensagemID("msg_relatorio_ok");
                realizarImpressaoRelatorio();
                removerObjetoMemoria(this);
            } else {
                setMensagemID("msg_relatorio_sem_dados");
            }

            //apresentarRelatorioObjetos(DeclaracaoSetranspRel.getIdEntidade(), titulo, nomeEntidade,
            //		"", "PDF", "", design, getUsuarioLogado().getNome(), "", getListaDeclaracao(), "");
            //setMensagemID("msg_relatorio_ok");
            registrarAtividadeUsuario(getUsuarioLogado(), "DeclaracaoSetranspRelControle", "Finalizando Geração de Relatório Declaração Setransp", "Emitindo Relatório");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            titulo = null;
            nomeEntidade = null;
            design = null;
            Uteis.liberarListaMemoria(listaDeclaracao);
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
                if (!matriculaVO.getMatricula().isEmpty()) {
                    objs.add(matriculaVO);
                } else {
                    matriculaVO = null;
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
            MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatricula(getDeclaracaoSetranspVO().getMatricula(), this.getUnidadeEnsinoLogado().getCodigo(), false,
                    Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            if (objAluno.getMatricula().equals("")) {
                throw new Exception("Aluno de matrícula " + getDeclaracaoSetranspVO().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
            }
            List<MatriculaPeriodoVO> lista = new ArrayList<MatriculaPeriodoVO>(0);
            lista = getFacadeFactory().getMatriculaPeriodoFacade().consultarUltimaMatriculaPeriodoPorMatricula(objAluno.getMatricula(),
                    false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            boolean alunoFormado = true;
            for (MatriculaPeriodoVO matPd : lista) {
                if (matPd.getSituacaoMatriculaPeriodo().equals("AT")) {
                    alunoFormado = false;
                }
            }
            if (alunoFormado) {
                setDeclaracaoSetranspVO(new DeclaracaoSetranspVO());
                if (getListaDeclaracao().size() == 0) {
                    getListaDeclaracao().add(getDeclaracaoSetranspVO());
                } else {
                    getListaDeclaracao().set(0, getDeclaracaoSetranspVO());
                }
                throw new Exception("Aluno de matrícula " + getDeclaracaoSetranspVO().getMatricula() + " está inativo.");
            }
            setDeclaracaoSetranspVO(getFacadeFactory().getDeclaracaoSetranspRelFacade().consultarPorCodigoAluno(objAluno, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));
            if (getListaDeclaracao().size() == 0) {
                getListaDeclaracao().add(getDeclaracaoSetranspVO());
            } else {
                getListaDeclaracao().set(0, getDeclaracaoSetranspVO());
            }
        } catch (Exception e) {
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
            List<MatriculaPeriodoVO> lista = new ArrayList<MatriculaPeriodoVO>(0);
            lista = getFacadeFactory().getMatriculaPeriodoFacade().consultarUltimaMatriculaPeriodoPorMatricula(obj.getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            boolean alunoFormado = true;
            for (MatriculaPeriodoVO matPd : lista) {
                if (matPd.getSituacaoMatriculaPeriodo().equals("AT")) {
                    alunoFormado = false;
                }
            }
            if (alunoFormado) {
                setDeclaracaoSetranspVO(new DeclaracaoSetranspVO());
                if (getListaDeclaracao().size() == 0) {
                    getListaDeclaracao().add(getDeclaracaoSetranspVO());
                } else {
                    getListaDeclaracao().set(0, getDeclaracaoSetranspVO());
                }
                throw new Exception("Aluno de matrícula " + getDeclaracaoSetranspVO().getMatricula() + " está inativo.");
            }
            setDeclaracaoSetranspVO(getFacadeFactory().getDeclaracaoSetranspRelFacade().consultarPorCodigoAluno(obj,
                    Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));
            if (getListaDeclaracao().size() == 0) {
                getListaDeclaracao().add(getDeclaracaoSetranspVO());
            } else {
                getListaDeclaracao().set(0, getDeclaracaoSetranspVO());
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            setCampoConsultaAluno("");
            setValorConsultaAluno("");
            setListaConsultaAluno(new ArrayList(0));
        }
    }

    public void limparDadosAluno() {
        getDeclaracaoSetranspVO().setMatricula("");
        getDeclaracaoSetranspVO().setNome("");
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

    protected void setDeclaracaoSetranspRel(DeclaracaoSetranspRel declaracaoSetranspRel) {
        this.declaracaoSetranspRel = declaracaoSetranspRel;
    }

    protected DeclaracaoSetranspRelInterfaceFacade getDeclaracaoSetranspRel() {
        if (declaracaoSetranspRel == null) {
            declaracaoSetranspRel = new DeclaracaoSetranspRel();
        }
        return declaracaoSetranspRel;
    }

    /**
     * @param declaracaoSetranspVO
     *            the declaracaoSetranspVO to set
     */
    public void setDeclaracaoSetranspVO(DeclaracaoSetranspVO declaracaoSetranspVO) {
        this.declaracaoSetranspVO = declaracaoSetranspVO;
    }

    /**
     * @return the declaracaoSetranspVO
     */
    public DeclaracaoSetranspVO getDeclaracaoSetranspVO() {
        if (declaracaoSetranspVO == null) {
            declaracaoSetranspVO = new DeclaracaoSetranspVO();
        }
        return declaracaoSetranspVO;
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
            listaDeclaracao = new ArrayList<>(0);
        }
        return listaDeclaracao;
    }
}
