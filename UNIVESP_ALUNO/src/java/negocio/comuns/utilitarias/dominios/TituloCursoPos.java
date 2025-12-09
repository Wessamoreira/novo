/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.utilitarias.dominios;

/**
 *
 * @author Diego
 */
public enum TituloCursoPos {

    LATO_SENSU("LS", "Lato Sensu"),         //Pós-Graduação: <Lato Sensu(MBA ou equivalentes)>
    STRICTO_SENSU("SS", "Stricto Sensu"),   //Pós-Graduação: <Stricto Sensu(Mestrado e Doutorado)>
    RESIDENCIA_MEDICA("RM", "Residência Médica");
    
    
    String valor;
    String descricao;

    TituloCursoPos(String valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    public static TituloCursoPos getEnum(String valor) {
        TituloCursoPos[] valores = values();
        for (TituloCursoPos obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
        TituloCursoPos obj = getEnum(valor);
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
