package negocio.comuns.financeiro.enumerador;

import negocio.comuns.utilitarias.UteisJSF;

public enum SituacaoControleRemessaContaReceberEnum {
	
	AGUARDANDO_PROCESSAMENTO("AGUARDANDO_PROCESSAMENTO", "Aguardando Proc. Retorno Remessa"), 
	ERRO_ENVIO("ERRO_ENVIO", "Erro Envio"), 
	REMETIDA("REMETIDA", "Registrada"), 
	REMETIDA_TITULO_CANCELADO("REMETIDA_TITULO_CANCELADO", "Título Cancelado no Banco"), 	
	NAO_REMETIDA("NAO_REMETIDA", "Não Remetida"), 
	ESTORNADO("ESTORNADO","Rejeitada"),
	ESTORNADO_PELO_USUARIO("ESTORNADO_PELO_USUARIO","Estornado");	
	
    String valor;
    String descricao;

    SituacaoControleRemessaContaReceberEnum(String valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    public String getValorApresentar(){
		return UteisJSF.internacionalizar("enum_SituacaoControleRemessaContaReceberEnum_"+this.name());
	}
    
    public static SituacaoControleRemessaContaReceberEnum getEnum(String valor) {
    	SituacaoControleRemessaContaReceberEnum[] valores = values();
        for (SituacaoControleRemessaContaReceberEnum obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
    	SituacaoControleRemessaContaReceberEnum obj = getEnum(valor);
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
