package negocio.comuns.crm.enumerador;

import negocio.comuns.utilitarias.dominios.SituacaoContaReceber;


public enum TipoCampanhaEnum {

    CONTACTAR_PROSPECTS_EXISTENTES("Contactar prospects existentes"), 
    CONTACTAR_INSCRITOS_PROCSELETIVO("Contactar inscritos processo seletivo - gerar agenda automática"), 
    CONTACTAR_ALUNOS_COBRANCA("Contactar alunos para cobrança"),
    CONTACTAR_PROSPECTS_EXISTENTES_SEM_AGENDA("Contactar prospects existentes sem agenda"),
    CONTACTAR_PROSPECTS_EXISTENTES_SEM_AGENDA_COBRANCA("Contactar prospects existentes sem agenda - Cobrança"),
    MAPEAR_NOVOS_PROSPECTS("Mapear novos prospects"), 
    LIGACAO_RECEPTIVA("Ligação receptiva"), 
    PRE_INSCRICAO("Pr\u00e9-Inscri\u00e7\u00e3o");
    
    String descricao;
    
    TipoCampanhaEnum(String descricao) {
        this.descricao = descricao;
    }
    
    public static String getDescricao(String valor) {
    	TipoCampanhaEnum obj = getEnum(valor);
        if (obj != null) {
            return obj.getDescricao();
        }
        return valor;
    }
    
    public static TipoCampanhaEnum getEnum(String valor) {
    	TipoCampanhaEnum[] valores = values();
        for (TipoCampanhaEnum obj : valores) {
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
