/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.utilitarias.dominios;

/**
 *
 * @author Diego
 */
public enum SituacaoControleLivroRegistroDiploma {
    //Situação do evento
	ABERTO("AB", "Aberto"),
    FECHADO("FE", "Fechado");
    
       
    
    String valor;
    String descricao;

    SituacaoControleLivroRegistroDiploma(String valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    public static SituacaoControleLivroRegistroDiploma getEnum(String valor) {
        SituacaoControleLivroRegistroDiploma[] valores = values();
        for (SituacaoControleLivroRegistroDiploma obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
        SituacaoControleLivroRegistroDiploma obj = getEnum(valor);
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
