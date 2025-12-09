package negocio.comuns.utilitarias.dominios;


public enum FinanciamentoEstudantil {

    FIES(1, "FI", "Fies - Reembolsável"),
    GOVERNO_ESTADUAL(2, "GE", "Governo Estadual - Reembolsável"),
    GOVERNO_MUNICIPAL(3, "GM", "Governo Municipal - Reembolsável"),
    IES(4, "IE", "IES - Reembolsável"),
    ENTIDADES_EXTERNAS(5, "EE", "Entidades Externas - Reembolsável"),
    //OUTROS(5, "OU", "Outros"),
    PROUNI_INTEGRAL(6, "PI", "ProUni - Integral - Não Reembolsável"),
    PROUNI_PARCIAL(7, "PP", "ProUni - Parcial - Reembolsável"),
    ENTIDADES_EXTERNAS_NAO_REEMBOLSAVEL(8, "ER", "Entidades Externas - Não Reembolsável"),
    GOVERNO_ESTADUAL_NAO_REEMBOLSAVEL(9, "EN", "Governo Estadual - Não Reembolsável"),
    IES_NAO_REEMBOLSAVEL(10, "IN", "IES - Não Reembolsável"),
    GOVERNO_MUNICIPAL_NAO_REEMBOLSAVEL(11, "MN", "Governo Municipal - Não Reembolsável"),
	PROUNI_PARCIAL_NAO_REEMBOLSAVEL(12, "PN", "ProUni - Parcial - Não Reembolsável");
    //OUTROS_NAO_REEMBOLSAVEL(12, "ON", "Outros - Não Reembolsável"),
    //NAO_INFORMADO(0, "NI", "Não Informado");
    
    int codigoCenso;
    String valor;
    String descricao;
    
    FinanciamentoEstudantil(int codigoCenso, String valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
        this.codigoCenso = codigoCenso;
    }

    public static FinanciamentoEstudantil getEnum(String valor) {
        FinanciamentoEstudantil[] valores = values();
        for (FinanciamentoEstudantil obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
        FinanciamentoEstudantil obj = getEnum(valor);
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
