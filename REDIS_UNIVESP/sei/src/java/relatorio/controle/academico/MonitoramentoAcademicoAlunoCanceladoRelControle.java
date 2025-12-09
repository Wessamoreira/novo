package relatorio.controle.academico;

import java.io.File;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;

@SuppressWarnings("unchecked")
@Controller("MonitoramentoAcademicoAlunoCanceladoRelControle")
@Scope("request")
@Lazy
public class MonitoramentoAcademicoAlunoCanceladoRelControle extends SuperControleRelatorio {

    MonitoramentoAcademicoAlunoCanceladoRelControle() {
    }

    public void imprimirExcelSintetico(List listaRel) {
        String design = null;
        List listaObjetos = null;
        try {
            design = ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "MonitoramentoAcademicoAlunoCanceladoRel" + ".jrxml");
            listaObjetos = listaRel;
            if (!listaObjetos.isEmpty()) {
                getSuperParametroRelVO().limparParametros();
                getSuperParametroRelVO().setNomeDesignIreport(design);
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.EXCEL);
                getSuperParametroRelVO().setSubReport_Dir(("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator));
                getSuperParametroRelVO().setTituloRelatorio("Relação de Alunos");
                getSuperParametroRelVO().setListaObjetos(listaObjetos);
                getSuperParametroRelVO().setCaminhoBaseRelatorio(("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator));
                realizarImpressaoRelatorio();
                removerObjetoMemoria(this);
                setMensagemID("msg_relatorio_ok");
            } else {
                setMensagemID("msg_relatorio_sem_dados");
            }

        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            design = null;
        }
    }

}
