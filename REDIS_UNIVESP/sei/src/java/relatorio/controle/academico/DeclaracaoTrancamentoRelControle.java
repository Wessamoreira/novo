package relatorio.controle.academico;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.TextoPadraoDeclaracaoVO;
import negocio.comuns.academico.TrancamentoVO;
import negocio.comuns.utilitarias.Uteis;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.academico.DeclaracaoTrancamentoRel;

@SuppressWarnings("unchecked")
@Controller("DeclaracaoTrancamentoRelControle")
@Scope("request")
@Lazy
public class DeclaracaoTrancamentoRelControle extends SuperControleRelatorio {

    private DeclaracaoTrancamentoRel declaracaoTrancamentoRel;

    public DeclaracaoTrancamentoRelControle() throws Exception {
        //obterUsuarioLogado();
        setMensagemID("msg_entre_prmrelatorio");
    }

    public void imprimirPDF(TrancamentoVO trancamentoVO) {
        List objtetos = null;
        String titulo = "DECLARAÇÃO DE TRANCAMENTO";
        String nomeEntidade = super.getUnidadeEnsinoLogado().getNome();
        String design = DeclaracaoTrancamentoRel.getDesignIReportRelatorio();
        try {
            getDeclaracaoTrancamentoRel().setDescricaoFiltros("");
            objtetos = getFacadeFactory().getDeclaracaoTrancamentoRelFacade().montarListaObjetos(trancamentoVO, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            getSuperParametroRelVO().setCaminhoBaseRelatorio(DeclaracaoTrancamentoRel.getCaminhoBaseRelatorio());
            getSuperParametroRelVO().setSubReport_Dir(DeclaracaoTrancamentoRel.getCaminhoBaseRelatorio());
            getSuperParametroRelVO().setListaObjetos(objtetos);
            getSuperParametroRelVO().setUnidadeEnsino(nomeEntidade);
            getSuperParametroRelVO().setTituloRelatorio(titulo);
            getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
            getSuperParametroRelVO().setNomeDesignIreport(design);
            getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
//            apresentarRelatorioObjetos(DeclaracaoTrancamentoRel.getIdEntidade(), titulo, nomeEntidade, "", "PDF", "", design, getUsuarioLogado().getNome(),
//                    getDeclaracaoTrancamentoRel().getDescricaoFiltros(),
//                    objtetos, DeclaracaoTrancamentoRel.getCaminhoBaseRelatorio());
            realizarImpressaoRelatorio();
            setMensagemID("msg_relatorio_ok");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            Uteis.liberarListaMemoria(objtetos);
            titulo = null;
            nomeEntidade = null;
            design = null;
        }
    }

    public DeclaracaoTrancamentoRel getDeclaracaoTrancamentoRel() throws Exception {
        if (declaracaoTrancamentoRel == null) {
            declaracaoTrancamentoRel = new DeclaracaoTrancamentoRel();
        }
        return declaracaoTrancamentoRel;
    }

    public void setDeclaracaoTrancamentoRel(DeclaracaoTrancamentoRel declaracaoTrancamentoRel) {
        this.declaracaoTrancamentoRel = declaracaoTrancamentoRel;
    }
    
}
