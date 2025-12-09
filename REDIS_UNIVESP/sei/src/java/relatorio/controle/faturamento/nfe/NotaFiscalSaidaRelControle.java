package relatorio.controle.faturamento.nfe;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import negocio.comuns.faturamento.nfe.CartaCorrecaoVO;
import negocio.comuns.faturamento.nfe.NotaFiscalSaidaVO;
import negocio.comuns.faturamento.nfe.enumeradores.TipoIntegracaoNfeEnum;
import negocio.comuns.utilitarias.Uteis;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;

@Controller("NotaFiscalSaidaRelControle")
@Scope("request")
@Lazy
public class NotaFiscalSaidaRelControle extends SuperControleRelatorio {
	
	private NotaFiscalSaidaVO notaFiscalSaidaVO;
	
	public void imprimirPDF() {
		String design = null;
		List lista = new ArrayList(0);
		
		try {
			if(getNotaFiscalSaidaVO().getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getTipoIntegracaoNfeEnum().equals(TipoIntegracaoNfeEnum.NFSE)){
				design = "relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + getNotaFiscalSaidaVO().getWebServicesNFSEEnum().getLayoutRelatorioString() +".jrxml";
			}else if(getNotaFiscalSaidaVO().getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getTipoIntegracaoNfeEnum().equals(TipoIntegracaoNfeEnum.NFE)){
				design = getFacadeFactory().getNotaFiscalSaidaFacade().getDesignIReportRelatorio();
			}
			getNotaFiscalSaidaVO().setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getNotaFiscalSaidaVO().getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getNotaFiscalSaidaVO().getResponsavel()));
			lista.add(getNotaFiscalSaidaVO());
			getSuperParametroRelVO().setNomeDesignIreport(design);
			getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
			getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getNotaFiscalSaidaFacade().getCaminhoBaseRelatorio());
			getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getNotaFiscalSaidaFacade().getCaminhoBaseRelatorio());
			getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
			getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());			
			getSuperParametroRelVO().setListaObjetos(lista);
			getSuperParametroRelVO().adicionarParametro("usuario", getNotaFiscalSaidaVO().getResponsavel().getNome());
			getSuperParametroRelVO().adicionarParametro("codigoUsuario", Integer.parseInt(getNotaFiscalSaidaVO().getResponsavel().getCodigo().toString()));
			getSuperParametroRelVO().adicionarParametro("nomeEmpresa", getNotaFiscalSaidaVO().getUnidadeEnsinoVO().getNome());
			getSuperParametroRelVO().adicionarLogoUnidadeEnsinoSelecionada(getNotaFiscalSaidaVO().getUnidadeEnsinoVO());
			setMensagemID("msg_relatorio_ok");
			realizarImpressaoRelatorio();
			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void imprimirCartaCorrecao(CartaCorrecaoVO obj) {
		String design = getFacadeFactory().getCartaCorrecaoNotaFiscalFacade().getDesignIReportRelatorio();
		List lista = new ArrayList(0);
		try {
			getSuperParametroRelVO().setNomeDesignIreport(design);
			getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
			getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getNotaFiscalSaidaFacade().getCaminhoBaseRelatorio());
			getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getNotaFiscalSaidaFacade().getCaminhoBaseRelatorio());
			getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
			getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());

			lista.add(obj);

			getSuperParametroRelVO().setListaObjetos(lista);
			getSuperParametroRelVO().adicionarParametro("usuario", getNotaFiscalSaidaVO().getResponsavel().getNome());
			getSuperParametroRelVO().adicionarParametro("codigoUsuario", Integer.parseInt(getNotaFiscalSaidaVO().getResponsavel().getCodigo().toString()));
			getSuperParametroRelVO().adicionarParametro("nomeEmpresa", getNotaFiscalSaidaVO().getUnidadeEnsinoVO().getNome());

			setMensagemID("msg_relatorio_ok");
			realizarImpressaoRelatorio();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	

	
	public NotaFiscalSaidaVO getNotaFiscalSaidaVO() {
		if (notaFiscalSaidaVO == null) {
			notaFiscalSaidaVO = new NotaFiscalSaidaVO();
		}
		return notaFiscalSaidaVO;
	}

	public void setNotaFiscalSaidaVO(NotaFiscalSaidaVO notaFiscalSaidaVO) {
		this.notaFiscalSaidaVO = notaFiscalSaidaVO;
	}

}
