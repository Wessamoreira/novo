package relatorio.controle.biblioteca;

import java.util.ArrayList;
import java.util.List;
import javax.faces.model.SelectItem;

import negocio.comuns.basico.LayoutEtiquetaVO;
import negocio.comuns.basico.enumeradores.ModuloLayoutEtiquetaEnum;
import negocio.comuns.biblioteca.CatalogoVO;
import negocio.comuns.biblioteca.ExemplarVO;
import negocio.comuns.utilitarias.Uteis;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.biblioteca.EtiquetaCodigoBarraRelVO;

/**
 *
 * @author Crlos
 */
@Controller("EtiquetaCodigoBarraRelControle")
@Scope("request")
@Lazy
public class EtiquetaCodigoBarraRelControle extends SuperControleRelatorio {

    /**
	 * 
	 */
	private static final long serialVersionUID = 8340976976998659482L;
	private EtiquetaCodigoBarraRelVO etiquetaCodigoBarraRelVO;
    private String campoConsultarCatalogo;
    private String valorConsultarCatalogo;
    private List<CatalogoVO> listaConsultarCatalogo;
    private String campoConsultarExemplar;
    private String valorConsultarExemplar;
    private List<ExemplarVO> listaConsultarExemplar;
    private Integer numeroCopias;
    private Integer coluna;
    private Integer linha;
    private LayoutEtiquetaVO layoutEtiquetaVO;
    private List<SelectItem> listaSelectItemlayoutEtiqueta;
    private List<SelectItem> listaSelectItemColuna;
    private List<SelectItem> listaSelectItemLinha;
    
    
    public List<SelectItem> getListaSelectItemlayoutEtiqueta() {
        if (listaSelectItemlayoutEtiqueta == null) {
            listaSelectItemlayoutEtiqueta = new ArrayList<SelectItem>(0);
            try {
                List<LayoutEtiquetaVO> layoutEtiquetaVOs = getFacadeFactory().getLayoutEtiquetaFacade().consultarRapidaPorModulo(ModuloLayoutEtiquetaEnum.BIBLIOTECA, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
                listaSelectItemlayoutEtiqueta.add(new SelectItem(0, ""));
                for (LayoutEtiquetaVO layoutEtiquetaVO : layoutEtiquetaVOs) {
                    listaSelectItemlayoutEtiqueta.add(new SelectItem(layoutEtiquetaVO.getCodigo(), layoutEtiquetaVO.getDescricao()));

                }
            } catch (Exception e) {
                setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
            }
        }
        return listaSelectItemlayoutEtiqueta;
    }

    public void setListaSelectItemlayoutEtiqueta(List<SelectItem> listaSelectItemlayoutEtiqueta) {
        this.listaSelectItemlayoutEtiqueta = listaSelectItemlayoutEtiqueta;
    }
    
    public List<SelectItem> getListaSelectItemColuna() {
        if (listaSelectItemColuna == null) {
            listaSelectItemColuna = new ArrayList<SelectItem>(0);
        }
        return listaSelectItemColuna;
    }

    public void setListaSelectItemColuna(List<SelectItem> listaSelectItemColuna) {
        this.listaSelectItemColuna = listaSelectItemColuna;
    }

    public List<SelectItem> getListaSelectItemLinha() {
        if (listaSelectItemLinha == null) {
            listaSelectItemLinha = new ArrayList<SelectItem>(0);
        }
        return listaSelectItemLinha;
    }

    public void setListaSelectItemLinha(List<SelectItem> listaSelectItemLinha) {
        this.listaSelectItemLinha = listaSelectItemLinha;
    }

    public Integer getNumeroCopias() {
        if (numeroCopias == null) {
            numeroCopias = 1;
        }
        return numeroCopias;
    }

    public void setNumeroCopias(Integer numeroCopias) {
        this.numeroCopias = numeroCopias;
    }

    public LayoutEtiquetaVO getLayoutEtiquetaVO() {
        if (layoutEtiquetaVO == null) {
            layoutEtiquetaVO = new LayoutEtiquetaVO();
        }
        return layoutEtiquetaVO;
    }

    public void setLayoutEtiquetaVO(LayoutEtiquetaVO layoutEtiquetaVO) {
        this.layoutEtiquetaVO = layoutEtiquetaVO;
    }

    public Integer getColuna() {
        if (coluna == null) {
            coluna = 1;
        }
        return coluna;
    }

    public void setColuna(Integer coluna) {
        this.coluna = coluna;
    }

    public Integer getLinha() {
        if (linha == null) {
            linha = 1;
        }
        return linha;
    }

    public void setLinha(Integer linha) {
        this.linha = linha;
    }
    
    
    public void inicializarDadosLayoutEtiqueta() {
        try {
            getListaSelectItemColuna().clear();
            getListaSelectItemLinha().clear();
            if (getLayoutEtiquetaVO().getCodigo() > 0) {
                setLayoutEtiquetaVO(getFacadeFactory().getLayoutEtiquetaFacade().consultarPorChavePrimaria(getLayoutEtiquetaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado()));
                for (int x = 1; x <= getLayoutEtiquetaVO().getNumeroLinhasEtiqueta(); x++) {
                    getListaSelectItemLinha().add(new SelectItem(x, String.valueOf(x)));
                }
                for (int y = 1; y <= getLayoutEtiquetaVO().getNumeroColunasEtiqueta(); y++) {
                    getListaSelectItemColuna().add(new SelectItem(y, String.valueOf(y)));
                }
            }

        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public EtiquetaCodigoBarraRelControle() {
        setMensagemID("msg_entre_prmrelatorio");
    }

    public void imprimirCodigoBarra() {
    }

    public void consultarCatalogo() {
        try {
            setListaConsultarCatalogo(getFacadeFactory().getEtiquetaCodigoBarraFacade().consultarCatalogo(getCampoConsultarCatalogo(), getValorConsultarCatalogo(), getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado()));
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultarCatalogo(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void consultarExemplar() {
        try {
            setListaConsultarExemplar(getFacadeFactory().getEtiquetaCodigoBarraFacade().consultarExemplar(getCampoConsultarExemplar(), getValorConsultarExemplar(), getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado()));
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultarExemplar(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarCatalogo() throws Exception {
        try {
            CatalogoVO obj = (CatalogoVO) context().getExternalContext().getRequestMap().get("catalogo");
            this.getEtiquetaCodigoBarraRelVO().setCatalogoVO(obj);
            Uteis.liberarListaMemoria(this.getListaConsultarCatalogo());
            this.setValorConsultarCatalogo(null);
            this.setCampoConsultarCatalogo(null);
            setMensagemID("msg_dados_adicionados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarExemplar() throws Exception {
        ExemplarVO obj = (ExemplarVO) context().getExternalContext().getRequestMap().get("exemplar");
        getEtiquetaCodigoBarraRelVO().setExemplarVO(obj);
        getEtiquetaCodigoBarraRelVO().setCatalogoVO(obj.getCatalogo());
        Uteis.liberarListaMemoria(this.getListaConsultarExemplar());
        this.setValorConsultarExemplar(null);
        this.setCampoConsultarExemplar(null);
    }

    public List getTipoConsultarComboCatalogo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("titulo", "Título"));
        itens.add(new SelectItem("codigo", "Código"));
        return itens;
    }

    public List getTipoConsultarComboExemplar() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("codigo", "Código"));
        itens.add(new SelectItem("nomeBiblioteca", "Biblioteca"));
        itens.add(new SelectItem("obra", "Obra"));
        itens.add(new SelectItem("tipoExemplar", "Tipo Exemplar"));
        itens.add(new SelectItem("codigoBarra", "Código de Barra"));
        itens.add(new SelectItem("situacaoAtual", "Situação Atual"));
        return itens;
    }

    public void limparCampoCatalogo() {
        getEtiquetaCodigoBarraRelVO().setCatalogoVO(null);
    }

    public void limparCampoExemplar() {
        setEtiquetaCodigoBarraRelVO(null);
    }

    public EtiquetaCodigoBarraRelVO getEtiquetaCodigoBarraRelVO() {
        if (etiquetaCodigoBarraRelVO == null) {
            etiquetaCodigoBarraRelVO = new EtiquetaCodigoBarraRelVO();
        }
        return etiquetaCodigoBarraRelVO;
    }

    public void setEtiquetaCodigoBarraRelVO(EtiquetaCodigoBarraRelVO etiquetaCodigoBarraRelVO) {
        this.etiquetaCodigoBarraRelVO = etiquetaCodigoBarraRelVO;
    }

    public String getCampoConsultarCatalogo() {
        if (campoConsultarCatalogo == null) {
            campoConsultarCatalogo = "";
        }
        return campoConsultarCatalogo;
    }

    public void setCampoConsultarCatalogo(String campoConsultarCatalogo) {
        this.campoConsultarCatalogo = campoConsultarCatalogo;
    }

    public String getValorConsultarCatalogo() {
        if (valorConsultarCatalogo == null) {
            valorConsultarCatalogo = "";
        }
        return valorConsultarCatalogo;
    }

    public void setValorConsultarCatalogo(String valorConsultarCatalogo) {
        this.valorConsultarCatalogo = valorConsultarCatalogo;
    }

    public List getListaConsultarCatalogo() {
        if (listaConsultarCatalogo == null) {
            listaConsultarCatalogo = new ArrayList(0);
        }
        return listaConsultarCatalogo;
    }

    public void setListaConsultarCatalogo(List listaConsultarCatalogo) {
        this.listaConsultarCatalogo = listaConsultarCatalogo;
    }

    public String getCampoConsultarExemplar() {
        if (campoConsultarExemplar == null) {
            campoConsultarExemplar = "";
        }
        return campoConsultarExemplar;
    }

    public void setCampoConsultarExemplar(String campoConsultarExemplar) {
        this.campoConsultarExemplar = campoConsultarExemplar;
    }

    public String getValorConsultarExemplar() {
        if (valorConsultarExemplar == null) {
            valorConsultarExemplar = "";
        }
        return valorConsultarExemplar;
    }

    public void setValorConsultarExemplar(String valorConsultarExemplar) {
        this.valorConsultarExemplar = valorConsultarExemplar;
    }

    public List getListaConsultarExemplar() {
        if (listaConsultarExemplar == null) {
            listaConsultarExemplar = new ArrayList(0);
        }
        return listaConsultarExemplar;
    }

    public void setListaConsultarExemplar(List listaConsultarExemplar) {
        this.listaConsultarExemplar = listaConsultarExemplar;
    }
}
