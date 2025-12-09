package controle.arquitetura;

import jakarta.faces. component.UIComponent;
import jakarta.faces. context.FacesContext;
import jakarta.faces. convert.Converter;

import negocio.comuns.academico.ArquivoVO;

public class ConverterOrigemImagem extends SuperControle implements Converter {

    /**
	 * 
	 */
	private static final long serialVersionUID = -4488043285905516646L;

	public String getAsString(FacesContext fc, UIComponent uic, Object valor) {
        try {
        	if(valor instanceof ArquivoVO) {
            ArquivoVO arquivo = (ArquivoVO) valor;
            return montarTagImage(arquivo);
        	}
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
        return "";
    }

    public Object getAsObject(FacesContext fc, UIComponent uic, String string) {
        return null;
    }

    public String montarTagImage(ArquivoVO arquivo) throws Exception {
        StringBuilder str = new StringBuilder("");
        try {
            if (!arquivo.getPastaBaseArquivo().isEmpty()) {
                str = new StringBuilder("");
                String caminhoParaArquivosWeb = getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo();                
                str.append("<img style='").append(arquivo.getZoom()).append("' src='").append(caminhoParaArquivosWeb).
                append("/").append(arquivo.getPastaBaseArquivo()).append("/").append(arquivo.getNome()).append(".").append(arquivo.getExtensao()).append("'/>");
            }
        } catch (Exception e) {
            throw e;
        }
        return str.toString();
    }

   
}
