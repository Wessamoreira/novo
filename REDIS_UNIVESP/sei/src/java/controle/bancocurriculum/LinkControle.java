package controle.bancocurriculum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.FileUploadEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.bancocurriculum.LinkVO;
import negocio.comuns.basico.enumeradores.StatusAtivoInativoEnum;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;

@Controller("LinkControle")
@Scope("viewScope")
@Lazy
public class LinkControle extends SuperControle {

    /**
     * 
     */
    private static final long serialVersionUID = -662568552022578241L;
    private LinkVO link;
    private List<SelectItem> listaSelectItemUnidadeEnsino;
    private List<SelectItem> listaSelectItemSituacaoLink;
    private Date dataInicio = Uteis.getDataPrimeiroDiaMes(new Date());
    private Date dataFim = Uteis.getDataUltimoDiaMes(new Date());

    public LinkControle() {
        super();
        setMensagemID("msg_entre_prmconsulta", Uteis.ALERTA);
    }

    public void persistir() {
        try {
            getFacadeFactory().getLinkFacade().persistir(getLink(), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
            setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void realizarAtivacao() {
        try {
            getFacadeFactory().getLinkFacade().realizarAtivacao(getLink(), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
            setMensagemID("msg_dados_ativado", Uteis.SUCESSO);
        } catch (Exception e) {
            getLink().setSituacao(StatusAtivoInativoEnum.INATIVO);
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void realizarInativacao() {
        try {
            getFacadeFactory().getLinkFacade().realizarInativacao(getLink(), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
            setMensagemID("msg_dados_inativado", Uteis.SUCESSO);
        } catch (Exception e) {
            getLink().setSituacao(StatusAtivoInativoEnum.ATIVO);
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public String editar() {
        try {
            LinkVO data = (LinkVO) context().getExternalContext().getRequestMap().get("itemItens");
            setLink(getFacadeFactory().getLinkFacade().consultarPorChavePrimaria(data.getCodigo(), false, getUsuarioLogado(), getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade()));
            inicializarDadosImagem();
            setMensagemID("msg_dados_editar", Uteis.ALERTA);
            return Uteis.getCaminhoRedirecionamentoNavegacao("linkForm.xhtml");
        } catch (Exception e) {
            return "";
        }
    }

    public void inicializarDadosImagem() throws Exception {
        setCaminhoFotoUsuario(getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(getLink().getIcone(), PastaBaseArquivoEnum.IMAGEM.getValue(), getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(), "foto_usuario.png", false));
        if (getLink().getIcone().getCodigo().intValue() != 0) {
            setExibirBotao(Boolean.TRUE);
        } else {
            setExibirBotao(Boolean.FALSE);
        }
        
    }

    public String novo() {
        setLink(null);
        setCaminhoFotoUsuario("");
        setMensagemID("msg_entre_dados", Uteis.ALERTA);
        return Uteis.getCaminhoRedirecionamentoNavegacao("linkForm.xhtml");
    }

    public String irParaConsultar() {
        setLink(null);
        setListaConsulta(null);
        return Uteis.getCaminhoRedirecionamentoNavegacao("linkCons.xhtml");
    }

    public String consultar() {
        try {
            Uteis.liberarListaMemoria(getListaConsulta());
            setListaConsulta(getFacadeFactory().getLinkFacade().consultar(getDataInicio(), getDataFim(), getLink().getLink(), getLink().getEscopo(), getLink().getSituacao(), getLink().getUnidadeEnsino().getCodigo(), true, getUsuarioLogado(), getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade()));
            setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
        return Uteis.getCaminhoRedirecionamentoNavegacao("linkCons.xhtml");
    }

    public void upLoadImagem(FileUploadEvent uploadEvent) {
        try {
            getFacadeFactory().getArquivoHelper().upLoad(uploadEvent, getLink().getIcone(), getConfiguracaoGeralPadraoSistema(), PastaBaseArquivoEnum.IMAGEM, getUsuarioLogado());
            setCaminhoFotoUsuario(getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(getLink().getIcone(), PastaBaseArquivoEnum.IMAGEM.getValue(), getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(), "foto_usuario.png", true));
            setExibirBotao(Boolean.TRUE);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            uploadEvent = null;
        }
    }

    public String getShowFotoCrop() {
        try {
            if (getLink().getIcone().getNome() == null) {
                return "";
            }
            return getCaminhoFotoUsuario()+"?UID="+new Date().getTime();
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getTagImageComFotoPadrao();
        }
    }

    public void renderizarUpload() {
        setExibirUpload(false);
    }

    public void cancelar() {
        setExibirUpload(true);
        setExibirBotao(false);
    }

    public void montarListaSelectItens() {
        Uteis.liberarListaMemoria(listaSelectItemUnidadeEnsino);
        Uteis.liberarListaMemoria(listaSelectItemSituacaoLink);
        listaSelectItemUnidadeEnsino = null;
        listaSelectItemSituacaoLink = null;
        getListaSelectItemUnidadeEnsino();
        getListaSelectItemSituacaoLink();
    }

    public List getListaSelectItemEscopo() throws Exception {
        List objs = new ArrayList(0);
        Hashtable escopoLink = (Hashtable) Dominios.getEscopoLink();
        Enumeration keys = escopoLink.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) escopoLink.get(value);
            objs.add(new SelectItem(value, label));
        }
        SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
        Collections.sort((List) objs, ordenador);
        return objs;
    }

    public LinkVO getLink() {
        if (link == null) {
            link = new LinkVO();
        }
        return link;
    }

    public void setLink(LinkVO link) {
        this.link = link;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getListaSelectItemUnidadeEnsino() {
        if (listaSelectItemUnidadeEnsino == null) {
            listaSelectItemUnidadeEnsino = new ArrayList<SelectItem>(0);
            listaSelectItemUnidadeEnsino.add(new SelectItem("", ""));
            try {
                List<UnidadeEnsinoVO> unidadeEnsinoVOs = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome("", getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
                for (UnidadeEnsinoVO unidadeEnsinoVO : unidadeEnsinoVOs) {
                    listaSelectItemUnidadeEnsino.add(new SelectItem(unidadeEnsinoVO.getCodigo(), unidadeEnsinoVO.getNome()));
                }
            } catch (Exception e) {
                setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
                listaSelectItemUnidadeEnsino = null;
            }
        }
        return listaSelectItemUnidadeEnsino;
    }

    public List<SelectItem> getListaSelectItemSituacaoLink() {
        if (listaSelectItemSituacaoLink == null) {
            listaSelectItemSituacaoLink = new ArrayList<SelectItem>(0);
            try {
                listaSelectItemSituacaoLink.add(new SelectItem(null, ""));

                for (StatusAtivoInativoEnum situacao : StatusAtivoInativoEnum.values()) {
                    listaSelectItemSituacaoLink.add(new SelectItem(situacao, situacao.getValorApresentar()));
                }

            } catch (Exception e) {
                setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
                listaSelectItemSituacaoLink = null;
            }
        }
        return listaSelectItemSituacaoLink;
    }
}
