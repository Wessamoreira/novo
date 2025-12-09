package negocio.comuns.utilitarias.dominios;

/**
 *
 * @author Diego
 */
public enum ApoioSocial {

    ALIMENTACAO(1, "AL", "Alimentação"),
    MORADIA(2, "MO", "Moradia"),
    TRANSPORTE(3, "TR", "Transporte"),
    MATERIAL_DIDATICO(4, "MD", "Material Didático"),
    BOLSA_TRABALHO(5, "BT", "Bolsa Trabalho"),
    BOLSA_PERMANENCIA(6, "PB", "Bolsa Permanência");
    
    int codigoCenso;
    String valor;
    String descricao;
    
    ApoioSocial(int codigoCenso, String valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
        this.codigoCenso = codigoCenso;
    }

    public static ApoioSocial getEnum(String valor) {
        ApoioSocial[] valores = values();
        for (ApoioSocial obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
        ApoioSocial obj = getEnum(valor);
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
