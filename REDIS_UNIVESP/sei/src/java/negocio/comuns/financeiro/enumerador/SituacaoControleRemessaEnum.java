package negocio.comuns.financeiro.enumerador;

import negocio.comuns.utilitarias.UteisJSF;

public enum SituacaoControleRemessaEnum {
		
	TODOS("", "Todos"),
	ESTORNADO("ESTORNADO", "Estornado. Retorno Remessa"),
	AGUARDANDO_PROCESSAMENTO_RETORNO_REMESSA("AGUARDANDO_PROCESSAMENTO_RETORNO_REMESSA", "Aguardando Proc. Retorno Remessa"),
	RETORNO_REMESSA_PROCESSADO("RETORNO_REMESSA_PROCESSADO", "Retorno Remessa Processado");
	
    String valor;
    String descricao;

    SituacaoControleRemessaEnum(String valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    public String getValorApresentar(){
		return UteisJSF.internacionalizar("enum_SituacaoControleRemessaEnum_"+this.name());
	}
    
    public static SituacaoControleRemessaEnum getEnum(String valor) {
    	SituacaoControleRemessaEnum[] valores = values();
        for (SituacaoControleRemessaEnum obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
    	SituacaoControleRemessaEnum obj = getEnum(valor);
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
