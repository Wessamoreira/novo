/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.utilitarias.dominios;

/**
 *
 * @author Diego
 */
public enum TituloCursoSuperior {

    BACHARELADO("BA", "Bacharelado"),       
    LICENCIATURA("LI", "Licenciatura"),
    TECNOLOGO("TC", "Tecnólogo"),
    SEQUENCIAL("SE", "Sequencial"),
	TECNICO("TE", "Técnico");
    
    String valor;
    String descricao;

    TituloCursoSuperior(String valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    public static TituloCursoSuperior getEnum(String valor) {
        TituloCursoSuperior[] valores = values();
        for (TituloCursoSuperior obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
        TituloCursoSuperior obj = getEnum(valor);
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
