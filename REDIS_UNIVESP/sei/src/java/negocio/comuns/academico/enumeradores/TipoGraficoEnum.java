package negocio.comuns.academico.enumeradores;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;


import negocio.comuns.utilitarias.UteisJSF;


public enum TipoGraficoEnum {
    PIZZA, COLUNA, BARRA, LINHA;
    
    public String getValorApresentar(){
        return UteisJSF.internacionalizar("enum_TipoGraficoEnum_"+this.name());
    }
    
    public Boolean getPermiteTituloXY(){
        switch (this) {
            case PIZZA:
                return false;
            case COLUNA:
                return true;
            case BARRA:
                return true;
            case LINHA:
                return true;
            default:
                return false;                
        }
    }
    
    public Boolean getExigeCategoria(){
        switch (this) {
            case PIZZA:
                return false;
            case COLUNA:
                return true;
            case BARRA:
                return true;
            case LINHA:
                return true;
            default:
                return false;                
        }
    }
    
    public Boolean getExigeValor(){
        switch (this) {
            case PIZZA:
                return true;
            case COLUNA:
                return false;
            case BARRA:
                return false;
            case LINHA:
                return false;
            default:
                return false;                
        }
    }
    
    public String getTipoGraficoApresentar(){
        switch (this) {
            case PIZZA:
                return "pie";
            case COLUNA:
                return "column";
            case BARRA:
                return "bar";
            case LINHA:
                return "line";
            default:
                return "";                
        }
    }
    
    private static List<SelectItem> combobox;
    public static List<SelectItem> getCombobox(){
        if(combobox == null){
            combobox = new ArrayList<SelectItem>(0);
            for(TipoGraficoEnum tipoGraficoEnum: values()){
                combobox.add(new SelectItem(tipoGraficoEnum, tipoGraficoEnum.getValorApresentar()));
            }
        }
        return combobox;
    }
    
    
}
