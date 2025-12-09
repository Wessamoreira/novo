/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.utilitarias.dominios;

/**
 *
 * @author Diego
 */
public enum TipoDescontoAluno {

    PORCENTO("PO", "Porcentagem", "%"),
    VALOR("VA", "Valor Específico", "R$");
    
    String valor;
    String descricao;
    String simbolo;

    TipoDescontoAluno(String valor, String descricao, String simbolo) {
        this.valor = valor;
        this.descricao = descricao;
        this.simbolo = simbolo;
    }

    public static TipoDescontoAluno getEnum(String valor) {
    	if(valor == null) {
    		return TipoDescontoAluno.VALOR;
    	}
        TipoDescontoAluno[] valores = values();
        for (TipoDescontoAluno obj : valores) {
            if (obj.getValor().equals(valor) || (obj.getValor().equals("VA") && valor.equals("VE"))  || (obj.getValor().equals("PO") && valor.equals("PE"))) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
        TipoDescontoAluno obj = getEnum(valor);
        if (obj != null) {
            return obj.getDescricao();
        }
        return valor;
    }
    
    public static String getSimbolo(String valor) {
    	TipoDescontoAluno obj = getEnum(valor);
    	if (obj != null) {
    		return obj.getSimbolo();
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

	public String getSimbolo() {
		if (simbolo == null) {
			simbolo = "";
		}
		return simbolo;
	}

	public void setSimbolo(String simbolo) {
		this.simbolo = simbolo;
	}
}
