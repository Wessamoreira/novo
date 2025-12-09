package relatorio.controle.financeiro;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.financeiro.BalanceteRelVO;
import relatorio.negocio.jdbc.financeiro.BalanceteRel;

@Controller("BalanceteRelControle")
@Scope("request")
@Lazy
public class BalanceteRelControle extends SuperControleRelatorio {

    protected BalanceteRelVO balanceteRelVO;
    protected BalanceteRel balanceteRel;
    protected boolean mostrarDetalheReceitaDespesa;
    protected List<BalanceteRelVO> listaBalancete;

    public BalanceteRelControle() {
        inicializarDados();
    }

    public void inicializarDados() {
        setBalanceteRelVO(new BalanceteRelVO());
        setMostrarDetalheReceitaDespesa(Boolean.FALSE);
        setBalanceteRel(new BalanceteRel());
        setListaBalancete(new ArrayList<BalanceteRelVO>(0));
    }

    public void gerarBalancete() throws ParseException {
        setListaBalancete(new ArrayList<BalanceteRelVO>(0));
        if (getBalanceteRelVO().getPesquisar().equals("PR")) {
            List<BalanceteRelVO> lista = new ArrayList<BalanceteRelVO>(0);
            lista = getFacadeFactory().getBalanceteRelFacade().consultarContasPagasRecebidas(getBalanceteRelVO(), getUsuarioLogado());
            getBalanceteRelVO().setListaBalanceteRelVOTotal(lista);
        } else {
            List<BalanceteRelVO> lista = new ArrayList<BalanceteRelVO>(0);
            lista = getFacadeFactory().getBalanceteRelFacade().consultarTodasContas(getBalanceteRelVO());
            getBalanceteRelVO().setListaBalanceteRelVOTotal(lista);
        }
        getBalanceteRelVO().montarCentroReceitaDespesa();
    }

    public void apresentarDetalhesReceitaDespesa() {
        if (isMostrarDetalheReceitaDespesa()) {
            setMostrarDetalheReceitaDespesa(Boolean.FALSE);
        } else {
            setMostrarDetalheReceitaDespesa(Boolean.TRUE);
        }
    }

    public void imprimirRelatorio() {
        if (isMostrarDetalheReceitaDespesa()) {
            imprimirPDFCompleto();
        } else {
            imprimirPDFSintetico();
        }
    }

    public void imprimirPDFSintetico() {
        String nomeEntidade = null;
        String titulo = null;
        String design = null;
        try {
            balanceteRel.setDescricaoFiltros("");
            titulo = "Balancete Sintético";
            nomeEntidade = super.getUnidadeEnsinoLogado().getNome();
            design = getBalanceteRel().getDesignIReportRelatorio();
            apresentarRelatorioObjetos(getBalanceteRel().getIdEntidade(), titulo, nomeEntidade, "", "PDF", "", design, getUsuarioLogado().getNome(), getBalanceteRel().getDescricaoFiltros(),
                    criarObjeto(), getBalanceteRel().getCaminhoBaseRelatorio());
            setMensagemID("msg_relatorio_ok");
            removerObjetoMemoria(design);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            nomeEntidade = null;
            titulo = null;
            design = null;
        }
    }

    public void imprimirPDFCompleto() {
        String nomeEntidade = null;
        String titulo = null;
        String design = null;
        try {
            balanceteRel.setDescricaoFiltros("");
            titulo = "Balancete";
            nomeEntidade = super.getUnidadeEnsinoLogado().getNome();
            design = getBalanceteRel().getDesignIReportRelatorioCompleto();
            apresentarRelatorioObjetos(getBalanceteRel().getIdEntidadeCompleto(), titulo, nomeEntidade, "", "PDF", "", design, getUsuarioLogado().getNome(), getBalanceteRel().getDescricaoFiltros(),
                    criarObjeto(), getBalanceteRel().getCaminhoBaseRelatorioCompleto());
            setMensagemID("msg_relatorio_ok");
            removerObjetoMemoria(design);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            nomeEntidade = null;
            titulo = null;
            design = null;
        }
    }

    public List<BalanceteRelVO> criarObjeto() {
        getListaBalancete().add(getBalanceteRelVO());
        return getListaBalancete();
    }

    public List<SelectItem> getListaTipoPesquisa() {
        List<SelectItem> lista = new ArrayList<SelectItem>();
        lista.add(new SelectItem("PR", "Contas Recebidas e Pagas"));
        lista.add(new SelectItem("TD", "Todas as Contas"));
        return lista;
    }

    /**
     * @return the balanceteRelVO
     */
    public BalanceteRelVO getBalanceteRelVO() {
        return balanceteRelVO;
    }

    /**
     * @param balanceteRelVO
     *            the balanceteRelVO to set
     */
    public void setBalanceteRelVO(BalanceteRelVO balanceteRelVO) {
        this.balanceteRelVO = balanceteRelVO;
    }

    /**
     * @return the mostrarDetalheReceitaDespesa
     */
    public boolean isMostrarDetalheReceitaDespesa() {
        return mostrarDetalheReceitaDespesa;
    }

    /**
     * @param mostrarDetalheReceitaDespesa
     *            the mostrarDetalheReceitaDespesa to set
     */
    public void setMostrarDetalheReceitaDespesa(boolean mostrarDetalheReceitaDespesa) {
        this.mostrarDetalheReceitaDespesa = mostrarDetalheReceitaDespesa;
    }

    /**
     * @return the balanceteRel
     */
    public BalanceteRel getBalanceteRel() {
        return balanceteRel;
    }

    /**
     * @param balanceteRel
     *            the balanceteRel to set
     */
    public void setBalanceteRel(BalanceteRel balanceteRel) {
        this.balanceteRel = balanceteRel;
    }

    /**
     * @return the listaBalancete
     */
    public List<BalanceteRelVO> getListaBalancete() {
        return listaBalancete;
    }

    /**
     * @param listaBalancete
     *            the listaBalancete to set
     */
    public void setListaBalancete(List<BalanceteRelVO> listaBalancete) {
        this.listaBalancete = listaBalancete;
    }
}
