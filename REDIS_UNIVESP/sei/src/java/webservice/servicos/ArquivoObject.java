package webservice.servicos;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAttribute;

/**
 *
 * @author Carlos
 */
@XmlRootElement(name = "arquivo")
public class ArquivoObject {

    private Boolean arquivoExcluidoSucesso;
    private String caminho;

    @XmlAttribute(name = "excluido")
    public Boolean getArquivoExcluidoSucesso() {
        if (arquivoExcluidoSucesso == null) {
            arquivoExcluidoSucesso = Boolean.TRUE;
        }
        return arquivoExcluidoSucesso;
    }

    public void setArquivoExcluidoSucesso(Boolean arquivoExcluidoSucesso) {
        this.arquivoExcluidoSucesso = arquivoExcluidoSucesso;
    }

    /**
     * @return the caminho
     */
    @XmlAttribute(name = "caminho")
    public String getCaminho() {
        if(caminho == null){
            caminho = "";
        }
        return caminho;
    }

    /**
     * @param caminho the caminho to set
     */
    public void setCaminho(String caminho) {
        this.caminho = caminho;
    }
}
