package negocio.comuns.recursoshumanos.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum NaturezaEventoFolhaPagamentoEnum {
    
	OUTROS("OUTROS","Outros"),
	PREVIDENCIARIO("PREVIDENCIARIO","Previdenciario"),
	IMPOSTO_DE_RENDA("IMPOSTO_DE_RENDA","Imposto De Renda"),
	REMUNERATORIA("REMUNERATORIA","Remuneratoria"),
	INDENIZATORIA("INDENIZATORIAS","Indenizatoria");

	String valor;
    String descricao;

	public static NaturezaEventoFolhaPagamentoEnum getEnumPorValor(String valor) {
		for (NaturezaEventoFolhaPagamentoEnum naturezaEvento : NaturezaEventoFolhaPagamentoEnum.values()) {
			
			if(naturezaEvento.getValor().equals(valor))
				return naturezaEvento;
		}
		
		return null;
	}
    
    public String getValorApresentar(){
        return UteisJSF.internacionalizar("enum_NaturezaEventoFolhaPagamentoEnum_"+this.name());
    }
    
	private NaturezaEventoFolhaPagamentoEnum(String valor, String descricao) {
		this.valor = valor;
		this.descricao = descricao;
	}
	
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	
	public String getDescricao() {
		return UteisJSF.internacionalizar("enum_NaturezaEventoFolhaPagamentoEnum_"+this.valor);
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
}