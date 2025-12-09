package negocio.comuns.utilitarias;

public enum SituacaoExecucaoEnum {

	AGUARDANDO_PROCESSAMENTO("Aguardando Processamento"), 
	EM_PROCESSAMENTO("Em Processamento"), 
	PROCESSADO("Processado"), 
	CANCELADO("Cancelado");

	
	String descricao;

	SituacaoExecucaoEnum(String descricao) {
	
		this.descricao = descricao;
	}
	
	public static SituacaoExecucaoEnum getEnum(String valor) {
		SituacaoExecucaoEnum[] valores = values();
        for (SituacaoExecucaoEnum obj : valores) {
            if (obj.name().toString().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

	public static String getDescricao(String valor) {
		SituacaoExecucaoEnum obj = getEnum(valor);
		if (obj != null) {
			return obj.getDescricao();
		}
		return valor;
	}
	

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
