package negocio.comuns.utilitarias.dominios;

/**
 *
 * @author Diego
 */
public enum ReservasVagas {

    ETNICO(1, "ET", "Étnico"),
    DEFICIENCIA(2, "DE", "Deficiência"),
    PROCEDENTE_ENSINO_PUBLICO(3, "EP", "Estudante Procedente de Ensino Público"),
    SOCIAL_RENDA_FAMILIAR(4, "RF", "Social/Renda Familiar"),
    QUILOMBOLA(5, "QUI", "Quilombola"),
    TRANSGENERO_TRAVESTI(6, "TRANS", "Estudante transgênero e/ou travesti"),
    ESTRANGEIRO(7, "EI", "Estudante internacional"),
    REFUGIADO_APATRIADO_VISTO(8, "RE", "Refugiado, Apátrida ou Portador de visto humanitário"),
    IDOSO(9, "ID", "Idoso"),
    COMUNIDADE_TRADICIONAL(10,"CO", "Estudante pertencente a povos e comunidades tradicionais"),
    COMPETICOES_CONHECIMENTO(11, "MED", "Medalhista em olimpíadas científicas e competições de conhecimento"),
    OUTROS(0, "OU", "Outros");
    
    int codigoCenso;
    String valor;
    String descricao;
    
    ReservasVagas(int codigoCenso, String valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
        this.codigoCenso = codigoCenso;
    }

    public static ReservasVagas getEnum(String valor) {
        ReservasVagas[] valores = values();
        for (ReservasVagas obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
        ReservasVagas obj = getEnum(valor);
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
