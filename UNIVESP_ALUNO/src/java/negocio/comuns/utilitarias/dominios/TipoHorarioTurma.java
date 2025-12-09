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
public enum TipoHorarioTurma {

    SEMANAL("SE", "Semanal"),
    DIARIO("DI", "Diário");

     String valor;
    String descricao;

    TipoHorarioTurma(String valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

     public static List getComboDiaSemana(){
        List lista = new ArrayList();
        TipoHorarioTurma[] valores = values();
        for (TipoHorarioTurma obj : valores) {
            lista.add(new SelectItem(obj.getValor(), obj.getDescricao()));
        }
        return lista;
    }

    public static TipoHorarioTurma getEnum(String valor) {
        TipoHorarioTurma[] valores = values();
        for (TipoHorarioTurma obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
        TipoHorarioTurma obj = getEnum(valor);
        if (obj != null) {
            return obj.getDescricao();
        }
        return valor;
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
