package negocio.comuns.crm.enumerador;

public enum PoliticaGerarAgendaEnum {

    GERAR_NO_ATO_INSCRICAO("No Ato da Inscricao"), 
    GERAR_AO_LANCAR_RESULTADO_INSCRICAO("No Registro do Resultado Proc. Seletivo");
    
    String descricao;
    
    PoliticaGerarAgendaEnum(String descricao) {
        this.descricao = descricao;
    }
    
    public static String getDescricao(String valor) {
    	PoliticaGerarAgendaEnum obj = getEnum(valor);
        if (obj != null) {
            return obj.getDescricao();
        }
        return valor;
    }
    
    public static PoliticaGerarAgendaEnum getEnum(String valor) {
    	PoliticaGerarAgendaEnum[] valores = values();
        for (PoliticaGerarAgendaEnum obj : valores) {
            if (obj.toString().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
    
    
}
