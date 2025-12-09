/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.academico.enumeradores;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.faces. model.SelectItem;

import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;

/**
 *
 * @author Otimize-Not
 */
public enum MesAnoEnum {

    JANEIRO("01", "Janeiro", "JAN"),
    FEVEREIRO("02", "Fevereiro", "FEV"),
    MARCO("03", UteisJSF.internacionalizar("prt_Calendario_marco"), "MAR"),
    ABRIL("04", "Abril", "ABR"),
    MAIO("05", "Maio", "MAI"),
    JUNHO("06", "Junho", "JUN"),
    JULHO("07", "Julho", "JUL"),
    AGOSTO("08", "Agosto", "AGO"),
    SETEMBRO("09", "Setembro", "SET"),
    OUTUBRO("10", "Outubro", "OUT"),
    NOVEMBRO("11", "Novembro", "NOV"),
    DEZEMBRO("12", "Dezembro", "DEZ");
    String key;
    String mes;
    String mesAbreviado;

    private MesAnoEnum(String key, String mes, String mesAbreviado) {
        this.key = key;
        this.mes = mes;
        this.mesAbreviado = mesAbreviado;
    }

    public static MesAnoEnum getMesData(Date data) {
        int x = Uteis.getMesData(data);
        String key = "";
        if (x < 10) {
            key = "0" + x;
        } else {
            key = "" + x;
        }
        return getEnum(key);
    }
    
    

    public static MesAnoEnum getEnum(String key) {
        if (key.length() == 1) {
            key = "0" + key;
        } 
        for (MesAnoEnum mesAnoEnum : values()) {
            if (mesAnoEnum.getKey().equals(key)) {
                return mesAnoEnum;
            }
        }
        return null;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public String getMesAbreviado() {
        return mesAbreviado;
    }

    public void setMesAbreviado(String mesAbreviado) {
        this.mesAbreviado = mesAbreviado;       
    }
    
    private static List<SelectItem> listaSelectItemKeyMes;
    public static List<SelectItem> getListaSelectItemKeyMes(){
    	if(MesAnoEnum.listaSelectItemKeyMes == null){
    		MesAnoEnum.listaSelectItemKeyMes = new ArrayList<SelectItem>();
    		MesAnoEnum.listaSelectItemKeyMes.add(new SelectItem("", ""));
    		for(MesAnoEnum mesAnoEnum : MesAnoEnum.values()){
    			MesAnoEnum.listaSelectItemKeyMes.add(new SelectItem(mesAnoEnum.getKey(), mesAnoEnum.getKey()));
    		}
    	}
    	return MesAnoEnum.listaSelectItemKeyMes;
    }
    
    private static List<SelectItem> listaSelectItemDescricaoMes;
    public static List<SelectItem> getListaSelectItemDescricaoMes(){
    	if(MesAnoEnum.listaSelectItemDescricaoMes == null){
    		MesAnoEnum.listaSelectItemDescricaoMes = new ArrayList<SelectItem>();
    		MesAnoEnum.listaSelectItemDescricaoMes.add(new SelectItem("", ""));
    		for(MesAnoEnum mesAnoEnum : MesAnoEnum.values()){
    			MesAnoEnum.listaSelectItemDescricaoMes.add(new SelectItem(mesAnoEnum.getKey(), mesAnoEnum.getMes()));
    		}
    	}
    	return MesAnoEnum.listaSelectItemDescricaoMes;
    }
    
    public static List<SelectItem> getValorMesAnoEnum() {
		return montarListaSelectItem(MesAnoEnum.values());
	}

	private static List<SelectItem> montarListaSelectItem(MesAnoEnum[] modulos) {
		List<SelectItem> tagList = new ArrayList<SelectItem>();
		tagList.add(new SelectItem("", ""));
		for (MesAnoEnum tag : modulos) {
			tagList.add(new SelectItem(tag, tag.getMes()));
		}
		return tagList;
	}
    
    private static List<SelectItem> listaSelectItemDescricaoAbreviadoMes;
    public static List<SelectItem> getListaSelectItemDescricaoAbreviadoMes(){
    	if(MesAnoEnum.listaSelectItemDescricaoAbreviadoMes == null){
    		MesAnoEnum.listaSelectItemDescricaoAbreviadoMes = new ArrayList<SelectItem>();
    		MesAnoEnum.listaSelectItemDescricaoAbreviadoMes.add(new SelectItem("", ""));
    		for(MesAnoEnum mesAnoEnum : MesAnoEnum.values()){
    			MesAnoEnum.listaSelectItemDescricaoAbreviadoMes.add(new SelectItem(mesAnoEnum.getKey(), mesAnoEnum.getMesAbreviado()));
    		}
    	}
    	return MesAnoEnum.listaSelectItemDescricaoAbreviadoMes;
    }
    
    public String getMesAnoAnterior(String ano){
        switch (this) {
            case JANEIRO:
                return MesAnoEnum.DEZEMBRO.getMes()+"/"+(Integer.parseInt(ano)-1);                
            case FEVEREIRO:
                return MesAnoEnum.JANEIRO.getMes()+"/"+ano;                
            case MARCO:
                return MesAnoEnum.FEVEREIRO.getMes()+"/"+ano;                
            case ABRIL:
                return MesAnoEnum.MARCO.getMes()+"/"+ano;                
            case MAIO:
                return MesAnoEnum.ABRIL.getMes()+"/"+ano;                
            case JUNHO:
                return MesAnoEnum.MAIO.getMes()+"/"+ano;                
            case JULHO:
                return MesAnoEnum.JUNHO.getMes()+"/"+ano;                
            case AGOSTO:
                return MesAnoEnum.JULHO.getMes()+"/"+ano;                
            case SETEMBRO:
                return MesAnoEnum.AGOSTO.getMes()+"/"+ano;                
            case OUTUBRO:
                return MesAnoEnum.SETEMBRO.getMes()+"/"+ano;                
            case NOVEMBRO:
                return MesAnoEnum.OUTUBRO.getMes()+"/"+ano;                
            case DEZEMBRO:
                return MesAnoEnum.NOVEMBRO.getMes()+"/"+ano;                
                         
            default:
                return "";                
        }
    }
    
    public MesAnoEnum getMesAnoAnterior(){
        switch (this) {
            case JANEIRO:
                return MesAnoEnum.DEZEMBRO;                
            case FEVEREIRO:
                return MesAnoEnum.JANEIRO;                
            case MARCO:
                return MesAnoEnum.FEVEREIRO;                
            case ABRIL:
                return MesAnoEnum.MARCO;                
            case MAIO:
                return MesAnoEnum.ABRIL;                
            case JUNHO:
                return MesAnoEnum.MAIO;                
            case JULHO:
                return MesAnoEnum.JUNHO;                
            case AGOSTO:
                return MesAnoEnum.JULHO;                
            case SETEMBRO:
                return MesAnoEnum.AGOSTO;                
            case OUTUBRO:
                return MesAnoEnum.SETEMBRO;                
            case NOVEMBRO:
                return MesAnoEnum.OUTUBRO;                
            case DEZEMBRO:
                return MesAnoEnum.NOVEMBRO;                
                         
            default:
                return null;                
        }
    }
    
    public MesAnoEnum getMesAnoPosterior(){
        switch (this) {
            case JANEIRO:
                return MesAnoEnum.FEVEREIRO;                
            case FEVEREIRO:
                return MesAnoEnum.MARCO;                
            case MARCO:
                return MesAnoEnum.ABRIL;                
            case ABRIL:
                return MesAnoEnum.MAIO;                
            case MAIO:
                return MesAnoEnum.JUNHO;                
            case JUNHO:
                return MesAnoEnum.JULHO;                
            case JULHO:
                return MesAnoEnum.AGOSTO;                
            case AGOSTO:
                return MesAnoEnum.SETEMBRO;                
            case SETEMBRO:
                return MesAnoEnum.OUTUBRO;                
            case OUTUBRO:
                return MesAnoEnum.NOVEMBRO;                
            case NOVEMBRO:
                return MesAnoEnum.DEZEMBRO;                
            case DEZEMBRO:
                return MesAnoEnum.JANEIRO;                
                         
            default:
                return null;                
        }
    }
    
    public String getMesAnoPosterior(String ano){
        switch (this) {
            case JANEIRO:
                return MesAnoEnum.FEVEREIRO.getMes()+"/"+ano;                
            case FEVEREIRO:
                return MesAnoEnum.MARCO.getMes()+"/"+ano;                
            case MARCO:
                return MesAnoEnum.ABRIL.getMes()+"/"+ano;                
            case ABRIL:
                return MesAnoEnum.MAIO.getMes()+"/"+ano;                
            case MAIO:
                return MesAnoEnum.JUNHO.getMes()+"/"+ano;                
            case JUNHO:
                return MesAnoEnum.JULHO.getMes()+"/"+ano;                
            case JULHO:
                return MesAnoEnum.AGOSTO.getMes()+"/"+ano;                
            case AGOSTO:
                return MesAnoEnum.SETEMBRO.getMes()+"/"+ano;                
            case SETEMBRO:
                return MesAnoEnum.OUTUBRO.getMes()+"/"+ano;                
            case OUTUBRO:
                return MesAnoEnum.NOVEMBRO.getMes()+"/"+ano;                
            case NOVEMBRO:
                return MesAnoEnum.DEZEMBRO.getMes()+"/"+ano;                
            case DEZEMBRO:
                return MesAnoEnum.JANEIRO.getMes()+"/"+(Integer.parseInt(ano)+1);                
                         
            default:
                return "";                
        }
    }
    
    public String getMesAnoAnteriorAbreviado(String ano){
        if(ano.length()>2){
            ano = ano.substring(2, ano.length());
        }
        switch (this) {
            case JANEIRO:
                return MesAnoEnum.DEZEMBRO.getMesAbreviado()+"/"+ (ano.isEmpty()?0:(Integer.parseInt(ano)-1));                
            case FEVEREIRO:
                return MesAnoEnum.JANEIRO.getMesAbreviado()+"/"+ano;                
            case MARCO:
                return MesAnoEnum.FEVEREIRO.getMesAbreviado()+"/"+ano;                
            case ABRIL:
                return MesAnoEnum.MARCO.getMesAbreviado()+"/"+ano;                
            case MAIO:
                return MesAnoEnum.ABRIL.getMesAbreviado()+"/"+ano;                
            case JUNHO:
                return MesAnoEnum.MAIO.getMesAbreviado()+"/"+ano;                
            case JULHO:
                return MesAnoEnum.JUNHO.getMesAbreviado()+"/"+ano;                
            case AGOSTO:
                return MesAnoEnum.JULHO.getMesAbreviado()+"/"+ano;                
            case SETEMBRO:
                return MesAnoEnum.AGOSTO.getMesAbreviado()+"/"+ano;                
            case OUTUBRO:
                return MesAnoEnum.SETEMBRO.getMesAbreviado()+"/"+ano;                
            case NOVEMBRO:
                return MesAnoEnum.OUTUBRO.getMesAbreviado()+"/"+ano;                
            case DEZEMBRO:
                return MesAnoEnum.NOVEMBRO.getMesAbreviado()+"/"+ano;                
                         
            default:
                return "";                
        }
    }
    
    public String getMesAnoPosteriorAbreviado(String ano){
        if(ano.length()>2){
            ano = ano.substring(2, ano.length());
        }
        switch (this) {
            case JANEIRO:
                return MesAnoEnum.FEVEREIRO.getMesAbreviado()+"/"+ano;                
            case FEVEREIRO:
                return MesAnoEnum.MARCO.getMesAbreviado()+"/"+ano;                
            case MARCO:
                return MesAnoEnum.ABRIL.getMesAbreviado()+"/"+ano;                
            case ABRIL:
                return MesAnoEnum.MAIO.getMesAbreviado()+"/"+ano;                
            case MAIO:
                return MesAnoEnum.JUNHO.getMesAbreviado()+"/"+ano;                
            case JUNHO:
                return MesAnoEnum.JULHO.getMesAbreviado()+"/"+ano;                
            case JULHO:
                return MesAnoEnum.AGOSTO.getMesAbreviado()+"/"+ano;                
            case AGOSTO:
                return MesAnoEnum.SETEMBRO.getMesAbreviado()+"/"+ano;                
            case SETEMBRO:
                return MesAnoEnum.OUTUBRO.getMesAbreviado()+"/"+ano;                
            case OUTUBRO:
                return MesAnoEnum.NOVEMBRO.getMesAbreviado()+"/"+ano;                
            case NOVEMBRO:
                return MesAnoEnum.DEZEMBRO.getMesAbreviado()+"/"+ano;                
            case DEZEMBRO:
                return MesAnoEnum.JANEIRO.getMesAbreviado()+"/"+(Integer.parseInt(ano)+1);                
                         
            default:
                return "";                
        }
    }
}
