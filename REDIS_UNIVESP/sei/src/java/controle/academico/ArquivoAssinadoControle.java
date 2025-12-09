package controle.academico;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.academico.DocumentoAssinadoVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import relatorio.controle.arquitetura.SuperControleRelatorio;

@Controller("ArquivoAssinadoControle")
@Scope("viewScope")
@Lazy
public class ArquivoAssinadoControle extends SuperControleRelatorio{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3754812241281676250L;

	public ArquivoAssinadoControle() throws Exception {
		setControleConsulta(new ControleConsulta());
		getControleConsulta().setCampoConsulta("codigo");
		getControleConsultaOtimizado().setPaginaAtual(1);
		getControleConsultaOtimizado().setPage(1);
		setMensagemID("msg_entre_prmconsulta");
	}
	
	
	
	public String consultar() {
		try {
			executarValidacaoParametroConsultaVazio();
			getControleConsultaOtimizado().setLimitePorPagina(10);
			getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getDocumentoAssinadoFacade().consultarArquivoAssinado(getControleConsulta().getCampoConsulta(), getControleConsulta().getValorConsulta(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, true, null, null, getUsuarioLogado(), true, true));
			getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getDocumentoAssinadoFacade().consultarTotalRegistroArquivoAssinados(getControleConsulta().getCampoConsulta(), getControleConsulta().getValorConsulta(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, true, null, null, getUsuarioLogado(), true, true));
			if(!Uteis.isAtributoPreenchido(getControleConsultaOtimizado().getListaConsulta())){
				 throw new Exception(UteisJSF.internacionalizar("msg_erro_dadosnaoencontrados"));
			}
			setMensagemID("msg_dados_consultados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("arquivoAssinadoCons.xhtml");
		} catch (Exception e) {
			setListaConsulta(null);
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("arquivoAssinadoCons.xhtml");
		}
	}
	
	public void impressaoDeclaracaoContratoJaGerada() {
		DocumentoAssinadoVO obj = (DocumentoAssinadoVO) context().getExternalContext().getRequestMap().get("documentoAssinado1");
    	try {
    		limparMensagem();
			setFazerDownload(false);
			this.setCaminhoRelatorio("");
			getFacadeFactory().getArquivoFacade().carregarArquivoDigitalmenteAssinado(obj.getArquivo(), getConfiguracaoGeralPadraoSistema(),getUsuarioLogado());
			setCaminhoRelatorio(obj.getArquivo().getNome());
			setFazerDownload(true);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
    }
	
	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("codigo", "N° Documento Assinado"));
		itens.add(new SelectItem("descricao", "Descrição"));
		itens.add(new SelectItem("requerente", "Requerente"));
		return itens;
	}

}
