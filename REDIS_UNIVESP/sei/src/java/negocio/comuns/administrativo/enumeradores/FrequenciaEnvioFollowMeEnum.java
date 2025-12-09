package negocio.comuns.administrativo.enumeradores;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.utilitarias.UteisJSF;


public enum FrequenciaEnvioFollowMeEnum {
    
    SEMANAL, MENSAL, DATA_ESPECIFICA;
    
    private static List<SelectItem> listaSelectItemFrequenciaEnvioFollowMeEnum;

    public String getValorApresentar(){
        return UteisJSF.internacionalizar("enum_FrequenciaEnvioFollowMeEnum_"+this.name());
    }
    
    public static List<SelectItem> getListaSelectItemFrequenciaEnvioFollowMeEnum() {
        if(listaSelectItemFrequenciaEnvioFollowMeEnum == null){
            listaSelectItemFrequenciaEnvioFollowMeEnum = new ArrayList<SelectItem>(0);
            for(FrequenciaEnvioFollowMeEnum frequenciaEnvioFollowMeEnum:FrequenciaEnvioFollowMeEnum.values()){
                listaSelectItemFrequenciaEnvioFollowMeEnum.add(new SelectItem(frequenciaEnvioFollowMeEnum, frequenciaEnvioFollowMeEnum.getValorApresentar()));
            }
        }
        return listaSelectItemFrequenciaEnvioFollowMeEnum;
    }
    
    
}
