package relatorio.controle.academico;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.processosel.InscricaoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.academico.DeclaracaoAprovacaoVestVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.interfaces.academico.DeclaracaoAprovacaoVestRelInterfaceFacade;
import relatorio.negocio.jdbc.academico.DeclaracaoAprovacaoVestRel;
import relatorio.negocio.jdbc.academico.DeclaracaoConclusaoCursoRel;

@SuppressWarnings("unchecked")
@Controller("DeclaracaoAprovacaoVestRelControle")
@Scope("viewScope")
@Lazy
public class DeclaracaoAprovacaoVestRelControle extends SuperControleRelatorio {

    private String valorConsultaAluno;
    private String campoConsultaAluno;
    protected List listaConsultaAluno;
    private List listaDeclaracaoAprovacaoVest;
    private DeclaracaoAprovacaoVestVO declaracaoAprovacaoVestVO;

    public DeclaracaoAprovacaoVestRelControle() throws Exception {
        setListaConsultaAluno(null);
        setCampoConsultaAluno("");
        setValorConsultaAluno("");
        setDeclaracaoAprovacaoVestVO(null);
        setListaDeclaracaoAprovacaoVest(null);
        setMensagemID("msg_entre_prmrelatorio");
    }

    public void imprimirPDF() {
        String titulo = null;
        String nomeEntidade = null;
        String design = null;
        try {
            DeclaracaoAprovacaoVestRel.validarDados(getDeclaracaoAprovacaoVestVO());
            titulo = "Declaração de Aprovação no Vestibular";
            nomeEntidade = super.getUnidadeEnsinoLogado().getNome();
            design = DeclaracaoAprovacaoVestRel.getDesignIReportRelatorio();

            if (!getListaDeclaracaoAprovacaoVest().isEmpty()) {
                getSuperParametroRelVO().setTituloRelatorio(titulo);
                getSuperParametroRelVO().setNomeDesignIreport(design);
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
                getSuperParametroRelVO().setSubReport_Dir(DeclaracaoAprovacaoVestRel.getCaminhoBaseRelatorio());
                getSuperParametroRelVO().setCaminhoBaseRelatorio(DeclaracaoAprovacaoVestRel.getCaminhoBaseRelatorio());
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getSuperParametroRelVO().setUsuarioVO(getUsuarioLogadoClone());
                getSuperParametroRelVO().setListaObjetos(getListaDeclaracaoAprovacaoVest());
                setMensagemID("msg_relatorio_ok");
                realizarImpressaoRelatorio();
                removerObjetoMemoria(this);
                setListaConsultaAluno(null);
                setCampoConsultaAluno("");
                setValorConsultaAluno("");
                setDeclaracaoAprovacaoVestVO(null);
                setListaDeclaracaoAprovacaoVest(null);
            } else {
                setMensagemID("msg_relatorio_sem_dados");
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            titulo = null;
            design = null;
            nomeEntidade = null;
        }
    }

    public String consultar() {
        try {
            super.consultar();
            List objs = new ArrayList(0);
            if (getCampoConsultaAluno().equals("codigo")) {
                if (getValorConsultaAluno().equals("")) {
                    throw new ConsistirException("Por favor informe o CÓDIGO desejado.");
                }
                int valorInt = Integer.parseInt(getValorConsultaAluno());
                objs = getFacadeFactory().getInscricaoFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaAluno().equals("nomeUnidadeEnsino")) {
                if (getValorConsultaAluno().equals("")) {
                    throw new ConsistirException("Por favor informe o NOME da UNIDADE ENSINO desejado.");
                }
                objs = getFacadeFactory().getInscricaoFacade().consultarPorNomeUnidadeEnsino(getValorConsultaAluno(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaAluno().equals("nomePessoa")) {
                if (getValorConsultaAluno().equals("")) {
                    throw new ConsistirException("Por favor informe o NOME do CANDIDATO desejado.");
                }
                objs = getFacadeFactory().getInscricaoFacade().consultarPorNomePessoa(getValorConsultaAluno(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaAluno().equals("descricaoProcSeletivo")) {
                if (getValorConsultaAluno().equals("")) {
                    throw new ConsistirException("Por favor informe o NOME do PROCESSO SELETIVO desejado.");
                }
                objs = getFacadeFactory().getInscricaoFacade().consultarPorDescricaoProcSeletivo(getValorConsultaAluno(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaAluno().equals("nomeCurso")) {
                if (getValorConsultaAluno().equals("")) {
                    throw new ConsistirException("Por favor informe o NOME  do CURSO desejado.");
                }
                objs = getFacadeFactory().getInscricaoFacade().consultarPorNomeCurso(getValorConsultaAluno(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            setListaConsultaAluno(objs);
            setMensagemID("msg_dados_consultados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("");
        } catch (Exception e) {
            setListaConsultaAluno(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("");
        }
    }

    public void selecionarAluno() throws Exception {
        try {
            InscricaoVO obj = (InscricaoVO) context().getExternalContext().getRequestMap().get("inscricaoItens");
            obj = getFacadeFactory().getInscricaoFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            setDeclaracaoAprovacaoVestVO(getFacadeFactory().getDeclaracaoAprovacaoVestRelFacade().consultarPorInscricaoCandidato(obj, Uteis.NIVELMONTARDADOS_TODOS));
            if (getListaDeclaracaoAprovacaoVest().size() == 0) {
                getListaDeclaracaoAprovacaoVest().add(getDeclaracaoAprovacaoVestVO());
            } else {
                getListaDeclaracaoAprovacaoVest().set(0, getDeclaracaoAprovacaoVestVO());
            }
            setCampoConsultaAluno("");
            setValorConsultaAluno("");
            setListaConsultaAluno(new ArrayList(0));
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void consultarAlunoPorInscricao() throws Exception {
        InscricaoVO objs = new InscricaoVO();
        objs = getFacadeFactory().getInscricaoFacade().consultarPorChavePrimaria(getDeclaracaoAprovacaoVestVO().getInscricao(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
        setDeclaracaoAprovacaoVestVO(getFacadeFactory().getDeclaracaoAprovacaoVestRelFacade().consultarPorInscricaoCandidato(objs, Uteis.NIVELMONTARDADOS_TODOS));
        if (getListaDeclaracaoAprovacaoVest().size() == 0) {
            getListaDeclaracaoAprovacaoVest().add(getDeclaracaoAprovacaoVestVO());
        } else {
            getListaDeclaracaoAprovacaoVest().set(0, getDeclaracaoAprovacaoVestVO());
        }
    }

    public void limparDadosAluno() {
        setDeclaracaoAprovacaoVestVO(new DeclaracaoAprovacaoVestVO());
        setListaDeclaracaoAprovacaoVest(new ArrayList(0));
    }

    public List getTipoConsultaCombo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("codigo", "Número da Inscrição"));
        itens.add(new SelectItem("nomeUnidadeEnsino", "Unidade Ensino"));
        itens.add(new SelectItem("nomePessoa", "Candidato"));
        itens.add(new SelectItem("descricaoProcSeletivo", "Processo Seletivo"));
        itens.add(new SelectItem("nomeCurso", "Curso"));
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

    public DeclaracaoAprovacaoVestVO getDeclaracaoAprovacaoVestVO() {
    	if (declaracaoAprovacaoVestVO == null) {
    		declaracaoAprovacaoVestVO = new DeclaracaoAprovacaoVestVO();
		}
        return declaracaoAprovacaoVestVO;
    }

    public void setDeclaracaoAprovacaoVestVO(DeclaracaoAprovacaoVestVO declaracaoAprovacaoVestVO) {
        this.declaracaoAprovacaoVestVO = declaracaoAprovacaoVestVO;
    }

    public List getListaDeclaracaoAprovacaoVest() {
    	if (listaDeclaracaoAprovacaoVest == null) {
    		listaDeclaracaoAprovacaoVest = new ArrayList(0);
		}
        return listaDeclaracaoAprovacaoVest;
    }

    public void setListaDeclaracaoAprovacaoVest(List listaDeclaracaoAprovacaoVest) {
        this.listaDeclaracaoAprovacaoVest = listaDeclaracaoAprovacaoVest;
    }
}
