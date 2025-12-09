package controle.administrativo;

import java.io.Serializable;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import negocio.comuns.academico.TextoPadraoDeclaracaoVO;
import negocio.comuns.utilitarias.Uteis;
import relatorio.controle.arquitetura.SuperControleRelatorio;

@Controller("OcorrenciaLGPDControle")
@Scope("viewScope")
@Lazy
public class OcorrenciaLGPDControle extends SuperControleRelatorio implements Serializable {
	private static final long serialVersionUID = 1L;
	private Boolean imprimirContrato;

	public void realizarGeracaoTextoPadraoLGPD() {
		try {
			validarImpressaoContrato();
			this.setCaminhoRelatorio("");
//			setCaminhoRelatorio(getFacadeFactory().getImpressaoDeclaracaoFacade().gerarDeclaracaoLGPD(getUnidadeEnsinoLogadoClone(), getUsuarioLogadoClone()));
			setImprimirContrato(false);
			setFazerDownload(true);
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setFazerDownload(false);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void validarImpressaoContrato() throws Exception {
		TextoPadraoDeclaracaoVO texto = getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorChavePrimaria(
				getConfiguracaoGeralSistemaVO().getTextoPadraoDadosSensiveisLGPD().getCodigo(),
				Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
//		getFacadeFactory().getImpressaoDeclaracaoFacade().validarDadosPermissaoImpressaoContrato(texto,getUsuarioLogado());
	}

	public Boolean getImprimirContrato() {
		if (imprimirContrato == null) {
			imprimirContrato = Boolean.FALSE;
		}
		return imprimirContrato;
	}

	public void setImprimirContrato(Boolean imprimirContrato) {
		this.imprimirContrato = imprimirContrato;
	}

}
