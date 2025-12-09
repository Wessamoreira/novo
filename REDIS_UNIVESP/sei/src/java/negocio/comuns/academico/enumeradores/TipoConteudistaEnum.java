package negocio.comuns.academico.enumeradores;

import java.util.List;
import java.util.ArrayList;

import javax.faces.model.SelectItem;



import negocio.comuns.utilitarias.UteisJSF;


public enum TipoConteudistaEnum {
    
    INTERNO, EXTERNO;
    
    public String getValorApresentar(){
        return UteisJSF.internacionalizar("enum_TipoConteudistaEnum_"+this.name());
    }
    
    
    public static List<SelectItem> combobox;
    public static List<SelectItem> getCombobox(){
        if(combobox == null){
            combobox = new ArrayList<SelectItem>(0);
            for(TipoConteudistaEnum tipoConteudistaEnum:values()){
                combobox.add(new SelectItem(tipoConteudistaEnum, UteisJSF.internacionalizar("enum_TipoConteudistaEnum_"+tipoConteudistaEnum.name())));
            }
        }
        return combobox;
    }

}
