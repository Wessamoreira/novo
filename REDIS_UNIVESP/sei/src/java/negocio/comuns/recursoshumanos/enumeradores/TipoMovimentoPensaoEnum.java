package negocio.comuns.recursoshumanos.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum TipoMovimentoPensaoEnum {

	MOVIMENTO("MOVIMENTO","Movimento");

	String valor;
    String descricao;

	public static TipoMovimentoPensaoEnum getEnumPorValor(String valor) {
		for (TipoMovimentoPensaoEnum tipoMovimentoPensao : TipoMovimentoPensaoEnum.values()) {
			
			if(tipoMovimentoPensao.getValor().equals(valor))
				return tipoMovimentoPensao;
		}
		
		return null;
	}
    
    public String getValorApresentar(){
        return getDescricao();
    }
    
	private TipoMovimentoPensaoEnum(String valor, String descricao) {
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
		return UteisJSF.internacionalizar("enum_TipoMovimentoPensaoEnum_"+this.getValor());
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
}