package relatorio.controle.financeiro;

import java.util.ArrayList;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.financeiro.SerasaRelVO;
import relatorio.negocio.jdbc.financeiro.SerasaRel;

@Controller("SerasaRelControle")
@Scope("request")
@Lazy
public class SerasaRelControle extends SuperControleRelatorio {

    private SerasaRel serasaRel;

    public SerasaRelControle() throws Exception {
        //obterUsuarioLogado();
        setMensagemID("msg_entre_prmrelatorio");
    }

    public void imprimirPDF(ArrayList<SerasaRelVO> serasaRelVOs) {
        String nomeEntidade = null;
        String titulo = null;
        String design = null;
        try {
            serasaRel.setDescricaoFiltros("");
            nomeEntidade = super.getUnidadeEnsinoLogado().getNome();
            titulo = " Relatório do SERASA ";
            design = SerasaRel.getDesignIReportRelatorio();
            apresentarRelatorioObjetos(SerasaRel.getIdEntidade(), titulo, nomeEntidade, getUnidadeEnsinoLogado().getNome(), "PDF", "", design, getUsuarioLogado().getNome(),
                    serasaRel.getDescricaoFiltros(), serasaRelVOs, SerasaRel.getCaminhoBaseRelatorio());
            setMensagemID("msg_relatorio_ok");
            removerObjetoMemoria(getSerasaRel());
        } catch (Exception e) {            
            setMensagemDetalhada("msg_erro", e.getMessage());
        }finally{
            nomeEntidade = null;
            titulo = null;
            design = null;
        }
    }

    public void setSerasaRel(SerasaRel serasaRel) {

        this.serasaRel = serasaRel;
    }

    public SerasaRel getSerasaRel() {
        if (serasaRel == null) {
            serasaRel = new SerasaRel();
        }
        return serasaRel;
    }
}
