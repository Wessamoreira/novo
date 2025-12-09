/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.crm.enumerador;

/**
 *
 * @author PEDRO
 */
public enum TipoSituacaoCompromissoEnum {
	AGUARDANDO_CONTATO("Aguardando Contato"), 
    PARALIZADO("Paralizado"),
    REALIZADO("Realizado"), 
    REALIZADO_COM_INSUCESSO_CONTATO("Realizado com Insucesso Contato"), 
    REALIZADO_COM_REMARCACAO("Realizado com Remarcação"),
    NAO_POSSUI_AGENDA("Não possui Agenda"),
    CANCELADO("Cancelado Campanha");
    
    String descricao;
    
    TipoSituacaoCompromissoEnum(String descricao) {
		this.descricao = descricao;
	}
    
    public static TipoSituacaoCompromissoEnum getEnum(String valor) {
    	TipoSituacaoCompromissoEnum[] valores = values();
        for (TipoSituacaoCompromissoEnum obj : valores) {
            if (obj.toString().equals(valor)) {
                return obj;
            }
        }
        return null;
    }
    
    public static String getDescricao(String valor) {
    	TipoSituacaoCompromissoEnum obj = getEnum(valor);
        if (obj != null) {
            return obj.getDescricao();
        }
        return valor;
    }

	public String getDescricao() {
		if (descricao == null) {
			descricao = "";
		}
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
    
}
