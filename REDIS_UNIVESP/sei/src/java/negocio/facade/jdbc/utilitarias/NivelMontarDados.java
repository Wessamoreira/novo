/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.facade.jdbc.utilitarias;

/**
 *
 * @author Diego
 */
public enum NivelMontarDados {

    NAO_INICIALIZADO(0, "Dados não inicializados"),
    BASICO(1, "Somente atributos básicos são montados - não incluindo entidades relacionadas e subordinadas (LAZY)"),
    TODOS(2, "Todos os dados da entidade são montados"),
    FORCAR_RECARGATODOSOSDADOS(3, "Força uma recarga de todos os dados"),
    COMBOBOX(4, "Sao carregados somente os campos para a combobox");
    
    Integer valor;
    String descricao;

    NivelMontarDados(Integer valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    public int compararOutroEnum(NivelMontarDados nivelPrm) {
        if (this.equals(nivelPrm)) {
            return 0;
        }
        return this.getValor().compareTo(nivelPrm.getValor());
        //return 0;
    }


    public static NivelMontarDados getEnum(Integer valor) {
        NivelMontarDados[] valores = values();
        for (NivelMontarDados obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(Integer valor) {
        NivelMontarDados obj = getEnum(valor);
        if (obj == null) {
        	return "";
        }
        return obj.getDescricao();
    }

    public Integer getValor() {
        return valor;
    }

    public void setValor(Integer valor) {
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
