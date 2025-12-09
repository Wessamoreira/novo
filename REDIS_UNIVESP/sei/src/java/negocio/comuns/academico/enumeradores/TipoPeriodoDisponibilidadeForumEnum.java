package negocio.comuns.academico.enumeradores;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.utilitarias.UteisJSF;

/**
 * @author Marcos Paulo
 *
 */
public enum TipoPeriodoDisponibilidadeForumEnum {
	
	SEMPRE_DISPONIVEL,ANO,ANO_SEMESTRE;
	
    public static TipoPeriodoDisponibilidadeForumEnum getEnum(String valor) {
    	TipoPeriodoDisponibilidadeForumEnum[] valores = values();
        for (TipoPeriodoDisponibilidadeForumEnum obj : valores) {
            if (obj.toString().equals(valor)) {
                return obj;
            }
        }
        return null;
    }
	
    public static List<SelectItem> combobox;
    
    public static List<SelectItem> getCombobox(){
        if(combobox == null){
            combobox = new ArrayList<SelectItem>(0);
            for (TipoPeriodoDisponibilidadeForumEnum tipoPeriodoDisponibilidadeForumEnum:values()){
                combobox.add(new SelectItem(tipoPeriodoDisponibilidadeForumEnum, UteisJSF.internacionalizar("enum_TipoPeriodoDisponibilidadeForumEnum_"+tipoPeriodoDisponibilidadeForumEnum.name())));
            }
        }
        return combobox;
    }

}
