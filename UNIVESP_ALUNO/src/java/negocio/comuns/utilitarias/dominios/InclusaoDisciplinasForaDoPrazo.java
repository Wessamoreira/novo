package negocio.comuns.utilitarias.dominios;

public enum InclusaoDisciplinasForaDoPrazo {

    MATRICULA_FORA_PRAZO(1, "MP", "Matrícula Fora do Prazo"),
    ATESTADO_MEDICO(2, "AT", "Atestado Médico"),
    ACORDO_FINANCEIRO_RETENCAO(3, "AF", "Acordo Financeiro/Retenção"),
    ACORDO_COMERCIAL(4, "AC", "Acordo Comercial"),
    OUTROS(5, "OU", "Outros");
    
    int codigoCenso;
    String valor;
    String descricao;
    
    InclusaoDisciplinasForaDoPrazo(int codigoCenso, String valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
        this.codigoCenso = codigoCenso;
    }

    public static InclusaoDisciplinasForaDoPrazo getEnum(String valor) {
        InclusaoDisciplinasForaDoPrazo[] valores = values();
        for (InclusaoDisciplinasForaDoPrazo obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
        InclusaoDisciplinasForaDoPrazo obj = getEnum(valor);
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
