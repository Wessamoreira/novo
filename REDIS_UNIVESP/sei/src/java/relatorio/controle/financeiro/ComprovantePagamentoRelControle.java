package relatorio.controle.financeiro;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.financeiro.NegociacaoPagamentoVO;
import negocio.comuns.utilitarias.Uteis;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.financeiro.ComprovantePagamentoRelVO;

@Controller("ComprovantePagamentoRelControle")
@Scope("request")
@Lazy
public class ComprovantePagamentoRelControle extends SuperControleRelatorio {

	protected List<ComprovantePagamentoRelVO> listaComprovantePagamentoRelVO;
	protected NegociacaoPagamentoVO negociacaoPagamentoVO;

	public ComprovantePagamentoRelControle() throws Exception {
	}

	public void imprimirPDF() {
		String titulo = null;
		String design = null;
		try {
			titulo = "COMPROVANTE PAGAMENTO";
			design = getFacadeFactory().getComprovantePagamentoRelFacade().getDesignIReportRelatorio();
			getNegociacaoPagamentoVO().setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCodigo(getNegociacaoPagamentoVO().getUnidadeEnsino().getCodigo(), false, getUsuarioLogado()));
			setListaComprovantePagamentoRelVO(getFacadeFactory().getComprovantePagamentoRelFacade().criarObjeto(getNegociacaoPagamentoVO(), getUsuarioLogado()));
			if (!getNegociacaoPagamentoVO().getCodigo().equals(0) && !getListaComprovantePagamentoRelVO().isEmpty()) {
				getSuperParametroRelVO().adicionarParametro("tipoPessoaApresentar", getNegociacaoPagamentoVO().getTipoPessoaApresentar());
				getSuperParametroRelVO().setTituloRelatorio(titulo);
				getSuperParametroRelVO().setNomeDesignIreport(design);
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getComprovantePagamentoRelFacade().getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getComprovantePagamentoRelFacade().getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setUnidadeEnsino(getNegociacaoPagamentoVO().getUnidadeEnsino().getNome());
				getSuperParametroRelVO().setUsuarioVO(getUsuarioLogadoClone());
				getSuperParametroRelVO().setListaObjetos(getListaComprovantePagamentoRelVO());
				setMensagemID("msg_relatorio_ok");
				realizarImpressaoRelatorio();
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			Uteis.liberarListaMemoria(getListaComprovantePagamentoRelVO());
		}

	}

	public List<ComprovantePagamentoRelVO> getListaComprovantePagamentoRelVO() {
		if (listaComprovantePagamentoRelVO == null) {
			listaComprovantePagamentoRelVO = new ArrayList<ComprovantePagamentoRelVO>(0);
		}
		return listaComprovantePagamentoRelVO;
	}

	public void setListaComprovantePagamentoRelVO(List<ComprovantePagamentoRelVO> listaComprovantePagamentoRelVO) {
		this.listaComprovantePagamentoRelVO = listaComprovantePagamentoRelVO;
	}

	public NegociacaoPagamentoVO getNegociacaoPagamentoVO() {
		if (negociacaoPagamentoVO == null) {
			negociacaoPagamentoVO = new NegociacaoPagamentoVO();
		}
		return negociacaoPagamentoVO;
	}

	public void setNegociacaoPagamentoVO(NegociacaoPagamentoVO negociacaoPagamentoVO) {
		this.negociacaoPagamentoVO = negociacaoPagamentoVO;
	}

}
