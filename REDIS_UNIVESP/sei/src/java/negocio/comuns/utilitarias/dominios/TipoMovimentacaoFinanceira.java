/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.utilitarias.dominios;

/**
 *
 * @author Diego
 */
public enum TipoMovimentacaoFinanceira {

    ENTRADA("EN", "Entrada", "E"),
    SAIDA("SA", "Saída", "S");
    
    String valor;
    String descricao;
    String abreviatura;

    TipoMovimentacaoFinanceira(String valor, String descricao, String abreviatura) {
        this.valor = valor;
        this.descricao = descricao;
        this.abreviatura = abreviatura;
    }

    public static TipoMovimentacaoFinanceira getEnum(String valor) {
        TipoMovimentacaoFinanceira[] valores = values();
        for (TipoMovimentacaoFinanceira obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
        TipoMovimentacaoFinanceira obj = getEnum(valor);
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
    
    public boolean isMovimentacaoSaida(){
    	return name().equals(TipoMovimentacaoFinanceira.SAIDA.name());
    }
    
    public boolean isMovimentacaoEntrada(){
    	return name().equals(TipoMovimentacaoFinanceira.ENTRADA.name());
    }

	public String getAbreviatura() {
		if (abreviatura == null) {
			abreviatura = "";
		}
		return abreviatura;
	}

	public void setAbreviatura(String abreviatura) {
		this.abreviatura = abreviatura;
	}
    
    
}
