/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.crm;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author RODRIGO
 */
@XmlRootElement(name="etapas")
@XmlAccessorType(XmlAccessType.FIELD)
public class EstatisticaPorEtapaCampanhaXMLVO  implements Serializable  {
    
       private static final long serialVersionUID = 1L;
    
    @XmlElement(name="etapa")    
    private List<EstatisticaPorEtapaCampanhaVO> estatisticaPorEtapaCampanhaVOs;

    public List<EstatisticaPorEtapaCampanhaVO> getEstatisticaPorEtapaCampanhaVOs() {
        return estatisticaPorEtapaCampanhaVOs;
    }

    public void setEstatisticaPorEtapaCampanhaVOs(List<EstatisticaPorEtapaCampanhaVO> estatisticaPorEtapaCampanhaVOs) {
        this.estatisticaPorEtapaCampanhaVOs = estatisticaPorEtapaCampanhaVOs;
    }
    
    
}
