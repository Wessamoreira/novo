/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.utilitarias.dominios;

/**
 *
 * @author Diego
 */
public enum TipoDesconto {

    CREDITO("CR", "Crédito"),
    DEBITO("DE", "Débito"),
	JURO("JU", "Juros"),
	MULTA("MU", "Multas"),
	ACRESCIMO("AC", "Acréscimos"),
	ALUNO("AL", "Aluno"),
	RECEBIMENTO("RE", "Recebimento"),
	PAGAMENTO("PA", "Pagamento"),
	ADIANTAMENTO("AD", "Adiantamento"),
	RATEIO("RA", "Rateio"),
	PROGRESSIVO("PR", "Progressivo"),
	INSTITUCIONAL("IN", "Institucional"),
	CONVENIO("CO", "Convênio"),
	CUSTEADO_CONVENIO("CC", "Custeado Convênio");
	
    
    String valor;
    String descricao;

    TipoDesconto(String valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    public static TipoDesconto getEnum(String valor) {
        TipoDesconto[] valores = values();
        for (TipoDesconto obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
        TipoDesconto obj = getEnum(valor);
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
    
    public boolean isConvenio(){
    	return this.name() != null && this.name().equals(TipoDesconto.CONVENIO.name());
    }
    public boolean isBolsaConvenio(){
    	return this.name() != null && this.name().equals(TipoDesconto.CUSTEADO_CONVENIO.name());
    }
    
    public boolean isAdiantamento(){
    	return this.name() != null && this.name().equals(TipoDesconto.ADIANTAMENTO.name());
    }
}
