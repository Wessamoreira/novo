package webservice.servicos;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Carlos
 */
@XmlRootElement(name = "upload")
public class CaminhoUploadObject {

    private String caminho;
    private Integer conf;

    public CaminhoUploadObject() {

    }

    /**
     * @return the caminho
     */
    @XmlAttribute(name = "caminho")
    public String getCaminho() {
        if (caminho == null) {
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

    /**
     * @return the conf
     */
    @XmlAttribute(name = "conf")
    public Integer getConf() {
        if (conf == null) {
            conf = 0;
        }
        return conf;
    }

    /**
     * @param conf the conf to set
     */
    public void setConf(Integer conf) {
        this.conf = conf;
    }

}
