package relatorio.controle.financeiro;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.financeiro.ContaPagarVO;
import negocio.comuns.utilitarias.Uteis;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.financeiro.AutorizacaoPagamentoRelVO;
import relatorio.negocio.jdbc.academico.DisciplinasGradeRel;
import relatorio.negocio.jdbc.financeiro.AutorizacaoPagamentoRel;

@Controller("AutorizacaoPagamentoRelControle")
@Scope("request")
@Lazy
public class AutorizacaoPagamentoRelControle extends SuperControleRelatorio {

	private static final long serialVersionUID = 1L;

	public AutorizacaoPagamentoRelControle() {
		
	}
	
	public void imprimirPDF(ContaPagarVO contaPagarVO) {
		String titulo = "Autorização de Pagamento";
        String design = AutorizacaoPagamentoRel.getDesignIReportRelatorio();
        List<AutorizacaoPagamentoRelVO> listaRegistro = new ArrayList<AutorizacaoPagamentoRelVO>(0);
        try {
            registrarAtividadeUsuario(getUsuarioLogado(), "AutorizacaoPagamentoRelControle", "Inicializando Geração de Relatório Autorização de Pagamento", "Emitindo Relatório");
            contaPagarVO = getFacadeFactory().getContaPagarFacade().consultarPorChavePrimaria(contaPagarVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            listaRegistro.add(getFacadeFactory().getAutorizacaoPagamentoRelFacade().criarObjeto(contaPagarVO, getUsuarioLogado()));
            getSuperParametroRelVO().setNomeDesignIreport(design);
            getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
            getSuperParametroRelVO().setSubReport_Dir(AutorizacaoPagamentoRel.getCaminhoBaseRelatorio());
            getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
            getSuperParametroRelVO().setTituloRelatorio(titulo);
            getSuperParametroRelVO().setListaObjetos(listaRegistro);
            getSuperParametroRelVO().setUnidadeEnsino(contaPagarVO.getUnidadeEnsino().getNome());
            getSuperParametroRelVO().setCaminhoBaseRelatorio(AutorizacaoPagamentoRel.getCaminhoBaseRelatorio());
            getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
            getSuperParametroRelVO().setQuantidade(listaRegistro.size());
            realizarImpressaoRelatorio();
            removerObjetoMemoria(this);
            registrarAtividadeUsuario(getUsuarioLogado(), "AutorizacaoPagamentoRelControle", "Finalizando Geração de Relatório Autorização de Pagamento", "Emitindo Relatório");
            setMensagemID("msg_relatorio_ok");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            titulo = null;
            design = null;
            Uteis.liberarListaMemoria(listaRegistro);
        }
	}
}
