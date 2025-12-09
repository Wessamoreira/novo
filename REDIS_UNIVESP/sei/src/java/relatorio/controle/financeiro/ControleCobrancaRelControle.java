package relatorio.controle.financeiro;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.financeiro.BancoVO;
import negocio.comuns.financeiro.ContaReceberRegistroArquivoVO;
import negocio.comuns.financeiro.ControleCobrancaVO;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.Bancos;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.financeiro.ControleCobrancaRel;

@Controller("ControleCobrancaRelControle")
@Scope("request")
@Lazy
public class ControleCobrancaRelControle extends SuperControleRelatorio {

    public ControleCobrancaRelControle() throws Exception {
    }

    public void imprimirPDF(ControleCobrancaVO controleCobrancaVO, ArrayList<ContaReceberRegistroArquivoVO> contaReceberRegistroArquivoVOs, ArrayList<ContaReceberRegistroArquivoVO> contaReceberRegistroArquivoDuplicidadeVOs) {
        List listaObjetos = new ArrayList(0);
        try {
            listaObjetos = getFacadeFactory().getControleCobrancaRelFacade().criarObjeto(controleCobrancaVO, contaReceberRegistroArquivoVOs, getUsuarioLogado());
            if (!listaObjetos.isEmpty()) {
            	Ordenacao.ordenarLista(listaObjetos, "ordenacao");
                getSuperParametroRelVO().setNomeDesignIreport(ControleCobrancaRel.getDesignIReportRelatorio());
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
                getSuperParametroRelVO().setSubReport_Dir(ControleCobrancaRel.getCaminhoBaseRelatorio());
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getSuperParametroRelVO().setTituloRelatorio("Controle de Cobrança");
                getSuperParametroRelVO().setListaObjetos(listaObjetos);
                getSuperParametroRelVO().setCaminhoBaseRelatorio(ControleCobrancaRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().adicionarParametro("responsavel", controleCobrancaVO.getResponsavel().getNome());
				getSuperParametroRelVO().adicionarParametro("banco",Bancos.getNome(controleCobrancaVO.getBanco()));
				getSuperParametroRelVO().adicionarParametro("dataProcessamento", controleCobrancaVO.getDataProcessamento());
				getSuperParametroRelVO().adicionarParametro("contaReceberRegistroArquivoDuplicidadeVOs", contaReceberRegistroArquivoDuplicidadeVOs);
				
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
                if (!getUnidadeEnsinoLogado().getNome().equals("")) {
                    getSuperParametroRelVO().setUnidadeEnsino(getUnidadeEnsinoLogado().getNome());
                } else {
                    getSuperParametroRelVO().setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCodigo(controleCobrancaVO.getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado()).getNome());
                }
                realizarImpressaoRelatorio();
//                setMensagemID("msg_relatorio_ok");
            } else {
                setMensagemID("msg_relatorio_sem_dados");
            }
        } catch (Exception ex) {
            setUsarTargetBlank("_self");
            setMensagemDetalhada("msg_erro", ex.getMessage());
           // //System.out.println(ex.getMessage());
            //ex.printStackTrace();
        } finally {
            listaObjetos = null;
        }
    }
}
