package negocio.comuns.utilitarias.dominios;

import java.util.ArrayList;
import java.util.List;

import jakarta.faces. model.SelectItem;

public enum Horas {

	ZERO("00", "00"),
	UMA("01", "01"),
	DUAS("02", "02"),
	TRES("03", "03"),
	QUATRO("04", "04"),
	CINCO("05", "05"),
	SEIS("06", "06"),
	SETE("07", "07"),
	OITO("08", "08"),
	NOVE("09", "09"),
	DEZ("10", "10"),
	ONZE("11", "11"),
	DOZE("12", "12"),
	TREZE("13", "13"),
	QUATORZE("14", "14"),
	QUINZE("15", "15"),
	DEZESSEIS("16", "16"),
	DEZESSETE("17", "17"),
	DEZOITO("18", "18"),
	DEZENOVE("19", "19"),
	VINTE("20", "20"),
	VINTE_UM("21", "21"),
	VINTE_DOIS("22", "22"),
	VINTE_TRES("23", "23");
	
	Horas(String valor, String descricao) {
	  this.valor = valor;
	  this.descricao = descricao;
	}
	String valor;
    String descricao;
    static List<SelectItem> listaSelectItem;
    
    
    public static List<SelectItem> getComboHoras() {
        if (listaSelectItem == null || listaSelectItem.isEmpty()) {
            listaSelectItem = new ArrayList<SelectItem>();
            Horas[] valores = values();
            for (Horas obj : valores) {
                listaSelectItem.add(new SelectItem(obj, obj.getDescricao()));
            }
        }
        return listaSelectItem;
    }
    
    
    public static Horas getEnum(String valor) {
    	Horas[] valores = values();
        for (Horas obj : valores) {
            if (obj.getValor().equals(Integer.parseInt(valor) < 10 ? "0".concat(valor) : valor)) {
                return obj;
            }
        }
        return null;
    }   

	public String getValor() {
		return valor;
	}

	public String getDescricao() {
		return descricao;
	}
    
}
