package relatorio.controle.financeiro;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.financeiro.CategoriaDespesaVO;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SelectItemOrdemValor;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;

@Controller("CategoriaDespesaRelControle")
@Scope("viewScope")
public class CategoriaDespesaRelControle extends SuperControleRelatorio implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 6085044458459237857L;

    public void imprimirRelatorioExcel() {
        getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.EXCEL);
        getSuperParametroRelVO().adicionarParametro("excel", true);
        gerarRelatorio();
    }

    public void imprimirPDF() {
        getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
        getSuperParametroRelVO().adicionarParametro("excel", false);
        gerarRelatorio();
    }

    public void gerarRelatorio() {
        List<CategoriaDespesaVO> listaObjetos = null;
        try {
            listaObjetos = consultarDadosRelatorio();
            if (!listaObjetos.isEmpty()) {
                getSuperParametroRelVO().setNomeDesignIreport(designIReportRelatorio(getSuperParametroRelVO().getTipoRelatorioEnum()));
                getSuperParametroRelVO().setSubReport_Dir(caminhoBaseRelatorio());
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getSuperParametroRelVO().setTituloRelatorio("Relatório Categoria de Despesa");
                getSuperParametroRelVO().setListaObjetos(listaObjetos);
                getSuperParametroRelVO().setCaminhoBaseRelatorio(caminhoBaseRelatorio());
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
                for (SelectItem item : getTipoConsultaCombo()) {
                    if (item.getValue().equals((String) getControleConsulta().getCampoConsulta())) {
                        getSuperParametroRelVO().adicionarParametro("campoConsulta", item.getLabel());
                        break;
                    }
                }
                if(getApresentarNivelCategoriaDespesa()){
                    for (SelectItem item : getListaSelectItemNivelCategoriaDespesa()) {
                        if (item.getValue().equals((String) getControleConsulta().getValorConsulta())) {
                            getSuperParametroRelVO().adicionarParametro("valorConsulta", item.getLabel());
                            break;
                        }
                    }   
                }else{
                    getSuperParametroRelVO().adicionarParametro("valorConsulta", getControleConsulta().getValorConsulta());
                }
                realizarImpressaoRelatorio();
                removerObjetoMemoria(this);
            } else {
                setMensagemID("msg_relatorio_sem_dados");
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            Uteis.liberarListaMemoria(listaObjetos);
        }
    }

    public String designIReportRelatorio(TipoRelatorioEnum tipoRelatorioEnum) {
        if (tipoRelatorioEnum.equals(TipoRelatorioEnum.PDF)) {
            return (caminhoBaseRelatorio() + "CategoriaDespesaRel.jrxml");
        } else {
            return (caminhoBaseRelatorio() + "CategoriaDespesaRel.jrxml");
        }
    }

    public String caminhoBaseRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator);
    }

    @SuppressWarnings("unchecked")
    public List<CategoriaDespesaVO> consultarDadosRelatorio() {
        try {

            List<CategoriaDespesaVO> objs = new ArrayList<CategoriaDespesaVO>(0);
            
            if (getControleConsulta().getCampoConsulta().equals("categoriaDespesaPrincipal")) {                
                objs = getFacadeFactory().getCategoriaDespesaFacade().consultarPorNomeCategoriaDespesaPrincipal(getControleConsulta().getValorConsulta(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("identificadorCategoriaDespesa")) {
                objs = getFacadeFactory().getCategoriaDespesaFacade().consultarPorIdentificadorCategoriaDespesa(getControleConsulta().getValorConsulta(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("nivelCategoriaDespesa")) {
                objs = getFacadeFactory().getCategoriaDespesaFacade().consultarPorNivelCategoriaDespesa(getControleConsulta().getValorConsulta(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("nome")) {
                objs = getFacadeFactory().getCategoriaDespesaFacade().consultarPorDescricao(getControleConsulta().getValorConsulta(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            Ordenacao.ordenarLista(objs, "identificadorCategoriaDespesa");
            setMensagemID("msg_dados_consultados");
            return objs;
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return null;
        }
    }

    /**
     * Rotina responsável por preencher a combo de consulta da telas.
     */
    private List<SelectItem> tipoConsultaCombo;

    public List<SelectItem> getTipoConsultaCombo() {
        if (tipoConsultaCombo == null) {
            tipoConsultaCombo = new ArrayList<SelectItem>(0);
            tipoConsultaCombo.add(new SelectItem("nome", "Nome"));
            tipoConsultaCombo.add(new SelectItem("identificadorCategoriaDespesa", "Identificador"));
            tipoConsultaCombo.add(new SelectItem("categoriaDespesaPrincipal", "Nome Cat. Principal"));
        }
        return tipoConsultaCombo;
    }

    public Boolean getApresentarNivelCategoriaDespesa() {
        if(getControleConsulta().getCampoConsulta() == null){
            getControleConsulta().setCampoConsulta("identificadorCategoriaDespesa");
        }
        return getControleConsulta().getCampoConsulta().equals("nivelCategoriaDespesa");
    }

    private List<SelectItem> listaSelectItemNivelCategoriaDespesa;

    @SuppressWarnings("unchecked")
    public List<SelectItem> getListaSelectItemNivelCategoriaDespesa() throws Exception {
        if (listaSelectItemNivelCategoriaDespesa == null) {
            listaSelectItemNivelCategoriaDespesa = new ArrayList<SelectItem>(0);
            Hashtable<String, String> creditoDebitos = (Hashtable<String, String>) Dominios.getNivelCategoriaDespesa();
            Enumeration<String> keys = creditoDebitos.keys();
            while (keys.hasMoreElements()) {
                String value = (String) keys.nextElement();
                String label = (String) creditoDebitos.get(value);
                listaSelectItemNivelCategoriaDespesa.add(new SelectItem(value, label));
            }
           
        }
        return listaSelectItemNivelCategoriaDespesa;
    }

}
