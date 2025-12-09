/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.crm.enumerador;

import negocio.comuns.utilitarias.UteisJSF;

/**
 *
 * @author RODRIGO
 */
public enum TempoContatoMetaEnum {
    NA_MEDIA, ABAIXO_MEDIA, ACIMA_MEDIA;
    
    public String getTempoContatoMetaEnum_Apresentar(){
        return UteisJSF.internacionalizar("enum_TempoContatoMetaEnum_"+name());
    }
}
