/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.utilitarias.dominios;

/**
 *
 * @author Alberto
 */
public enum TipoTextoPadrao {
    //Tipo do Texto Padrão
    CONTRATO("MA", "Matrícula"),
    FIADOR("FI", "Fiador"),
    EXTENSAO("EX", "Extensão"),
//    MODULAR("MO", "Especial"),
    ADITIVO("AD", "Aditivo"),
    RENOVACAO_ONLINE("RE", "Renovação Online"),
    INCLUSAO_REPOSICAO("IR", "Inclusão/Reposição"),
    TRANSFERENCIA_UNIDADE("TU","Transferência de Unidade");
       
    
    String valor;
    String descricao;

    TipoTextoPadrao(String valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    public static TipoTextoPadrao getEnum(String valor) {
        TipoTextoPadrao[] valores = values();
        for (TipoTextoPadrao obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
        TipoTextoPadrao obj = getEnum(valor);
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
