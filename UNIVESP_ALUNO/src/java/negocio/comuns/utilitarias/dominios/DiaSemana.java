/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.utilitarias.dominios;

import java.util.ArrayList;
import java.util.List;

import jakarta.faces. model.SelectItem;

/**
 *
 * @author Otimize-TI
 */
public enum DiaSemana {

    NENHUM("00", "NENHUM", ""),
    DOMINGO("01", "DOMINGO", "D"),
    SEGUNGA("02", "SEGUNDA-FEIRA", "2"),
    TERCA("03", "TERÇA-FEIRA", "3"),
    QUARTA("04", "QUARTA-FEIRA", "4"),
    QUINTA("05", "QUINTA-FEIRA", "5"),
    SEXTA("06", "SEXTA-FEIRA", "6"),
    SABADO("07", "SÁBADO", "S");
    String valor;
    String descricao;
    String numeral;
    static List<SelectItem> listaSelectItem;

    DiaSemana(String valor, String descricao, String numeral) {
        this.valor = valor;
        this.descricao = descricao;
        this.numeral = numeral;
    }

    public static DiaSemana getEnum(String valor) {
        DiaSemana[] valores = values();
        for (DiaSemana obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }
    
    public static DiaSemana getEnum(Integer valor) {
    	DiaSemana[] valores = values();
    	for (DiaSemana obj : valores) {
    		if (obj.getValor().equals("0"+(valor.toString()))) {
    			return obj;
    		}
    	}
    	return null;
    }

    public static List<SelectItem> getComboDiaSemana() {
        if (listaSelectItem == null || listaSelectItem.isEmpty()) {
            listaSelectItem = new ArrayList<SelectItem>();
            DiaSemana[] valores = values();
            for (DiaSemana obj : valores) {
                listaSelectItem.add(new SelectItem(obj, obj.getAbreviatura(obj.getDescricao())));
            }
        }
        return listaSelectItem;
    }

    public static String getDescricao(String valor) {
        DiaSemana obj = getEnum(valor);
        if (obj != null) {
            return obj.getDescricao();
        }
        return valor;
    }

    public static String getAbreviatura(String valor) {
        DiaSemana obj = getEnum(valor);
        if (obj != null) {
            return obj.getDescricao().substring(0, 3);
        }
        return valor;
    }

    public static String getValor(Integer valor) {
        DiaSemana obj = getEnum("0" + valor);
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

	public String getNumeral() {
		return numeral;
	}

	public void setNumeral(String numeral) {
		this.numeral = numeral;
	}
    
    
}
