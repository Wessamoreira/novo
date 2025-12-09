package relatorio.controle.contabil;

import controle.contabil.CalculoMesControle;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import negocio.comuns.contabil.DREVO;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import relatorio.controle.arquitetura.SuperControleRelatorio;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller("BalancoControleRel") @Scope("request") @Lazy
public class BalancoControleRel extends SuperControleRelatorio {

    public BalancoControleRel() throws Exception {
        inicializarFacades();
        setOpcaoOrdenacao(0);
        setMensagemID("msg_entre_prmrelatorio");
    }

    /**
     * Método responsável por gerar o relatório de <code>AniversariantesRel</code> no formato PDF
     */
    public void imprimirPDF() {    
        try {
            ControleAcesso.emitirRelatorio(getIdEntidade());
            List lista = new ArrayList(0);
            CalculoMesControle calculoMes = (CalculoMesControle) context().getExternalContext().getSessionMap().get("calculoMesControle");
            if (calculoMes != null) {
                lista = calculoMes.gerarLista();
            }
            String descricaoFiltros = "Mês: " + calculoMes.getCalculoMesVO().getMes() + "/" + calculoMes.getCalculoMesVO().getAno();

            if (!lista.isEmpty()) {
                String nomeRelatorio = getIdEntidade();
                String titulo = "Balancete";
                String design = getDesignIReportRelatorio();
                apresentarRelatorioObjetos(nomeRelatorio, titulo, getUnidadeEnsinoLogado().getRazaoSocial(), "", "PDF",
                        "/" + getIdEntidade() + "/registros", design, getUsuarioLogado().getNome(), descricaoFiltros, lista, getCaminhoBaseRelatorio());
            } else {
                setMensagemDetalhada("Não Foi possível gerar o Balancete.");
            }
            setMensagemID("msg_relatorio_ok");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            removerObjetoMemoria(this);
        }
    }
    public void imprimirDRE() {
        try {
            ControleAcesso.emitirRelatorio(getIdEntidade());
            List lista = new ArrayList(0);
            CalculoMesControle calculoMes = (CalculoMesControle) context().getExternalContext().getSessionMap().get("calculoMesControle");
            if (calculoMes != null) {
                lista = calculoMes.gerarDRE();
                //mudarCaracterSinal(lista);
            }
            String descricaoFiltros = "Mês: " + calculoMes.getCalculoMesVO().getMes() + "/" + calculoMes.getCalculoMesVO().getAno();

            if (!lista.isEmpty()) {
                String nomeRelatorio = getIdEntidadeDRE();
                String titulo = "Demonstração de Resultados";
                String design = getDesignIReportRelatorioDRE();
                apresentarRelatorioObjetos(nomeRelatorio, titulo, getUnidadeEnsinoLogado().getRazaoSocial(), "", "PDF",
                        "/" + getIdEntidadeDRE() + "/registros", design, getUsuarioLogado().getNome(), descricaoFiltros, lista, getDesignIReportRelatorioDRE());
            } else {
                setMensagemDetalhada("Não Foi possível gerar a DRE.");
            }
            setMensagemID("msg_relatorio_ok");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            removerObjetoMemoria(this);
        }
    }

    public void mudarCaracterSinal(List<DREVO> lista){
        for (DREVO obj : lista){
            if (obj.getSinal().equals("CR")){
                obj.setSinal("+");
            }else{
                obj.setSinal("-");
            }
        }
    }

    protected boolean inicializarFacades() {
        try {
            
            return true;
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro_conectarBD", e.getMessage());
            return false;
        }
    }

    public static String getDesignIReportRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "contabil" + File.separator + getIdEntidade() + ".jrxml");
    }
    public static String getDesignIReportRelatorioDRE() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "contabil" + File.separator + getIdEntidadeDRE() + ".jrxml");
    }
    public static String getCaminhoBaseRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "contabil" + File.separator);
    }

    public static String getIdEntidade() {
        return ("BalancoRel");
    }
    public static String getIdEntidadeDRE() {
        return ("DRERel");
    }

}
