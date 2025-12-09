package negocio.comuns.recursoshumanos.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum TipoPensaoDependenteEnum {
    
	PENSAO_JUDICIAL("PENSAO_JUDICIAL","PensaoJudicial");

	String valor;
    String descricao;

	public static TipoPensaoDependenteEnum getEnumPorValor(String valor) {
		for (TipoPensaoDependenteEnum tipoPensaoDependente : TipoPensaoDependenteEnum.values()) {
			
			if(tipoPensaoDependente.getValor().equals(valor))
				return tipoPensaoDependente;
		}
		
		return null;
	}
    
    public String getValorApresentar(){
        return getDescricao();
    }
    
	private TipoPensaoDependenteEnum(String valor, String descricao) {
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
		return UteisJSF.internacionalizar("enum_TipoPensaoDependenteEnum_"+this.descricao);
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
}