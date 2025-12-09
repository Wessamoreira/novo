package negocio.comuns.utilitarias.dominios;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

public enum DiaSemanaJob {

	TODOS_DIAS("00", "TODOS OS DIAS"),
    DOMINGO("01", "DOMINGO"),
    SEGUNDA("02", "SEGUNDA-FEIRA"),
    TERCA("03", "TERÇA-FEIRA"),
    QUARTA("04", "QUARTA-FEIRA"),
    QUINTA("05", "QUINTA-FEIRA"),
    SEXTA("06", "SEXTA-FEIRA"),
    SABADO("07", "SÁBADO");
    String valor;
    String descricao;
    static List<SelectItem> listaSelectItem;

    DiaSemanaJob(String valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    public static DiaSemanaJob getEnum(String valor) {
        DiaSemanaJob[] valores = values();
        for (DiaSemanaJob obj : valores) {
            if (obj.getDescricao().equals(valor)) {
                return obj;
            }
        }
        return null;
    }
    
    public static DiaSemanaJob getEnum(Integer valor) {
    	DiaSemanaJob[] valores = values();
    	for (DiaSemanaJob obj : valores) {
    		if (obj.getValor().equals("0"+(valor.toString()))) {
    			return obj;
    		}
    	}
    	return null;
    }

    public static List<SelectItem> getComboDiaSemanaJob() {
        if (listaSelectItem == null || listaSelectItem.isEmpty()) {
            listaSelectItem = new ArrayList<SelectItem>();
            DiaSemanaJob[] valores = values();
            for (DiaSemanaJob obj : valores) {
                listaSelectItem.add(new SelectItem(obj, obj.getDescricao()));
            }
        }
        return listaSelectItem;
    }

    public static String getDescricao(String valor) {
        DiaSemanaJob obj = getEnum(valor);
        if (obj != null) {
            return obj.getDescricao();
        }
        return valor;
    }

    public static String getAbreviatura(String valor) {
        DiaSemanaJob obj = getEnum(valor);
        if (obj != null) {
            return obj.getDescricao().substring(0, 3);
        }
        return valor;
    }

    public static String getValor(Integer valor) {
        DiaSemanaJob obj = getEnum("0" + valor);
        if (obj != null) {
            return obj.getValor();
        }
        return "0";
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }	
	
	
	
}
