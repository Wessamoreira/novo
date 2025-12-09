package negocio.comuns.administrativo.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum TipoIdadeExigidaEnum {
	
	MINIMA("MINIMA","Minima"),
	MAXIMA("MAXIMA","Maxima");


	String valor;
    String descricao;

	public static TipoIdadeExigidaEnum getEnumPorValor(String valor) {
		for (TipoIdadeExigidaEnum tipoIdadeExigida: TipoIdadeExigidaEnum.values()) {
			
			if(tipoIdadeExigida.getValor().equals(valor))
				return tipoIdadeExigida;
		}
		
		return null;
	}
	
	public static TipoIdadeExigidaEnum getEnumPorName (String name) {
		for (TipoIdadeExigidaEnum tipoIdadeExigida : TipoIdadeExigidaEnum.values()) {
			
			if(tipoIdadeExigida.name().equals(name))
				return tipoIdadeExigida;
		}
		
		return null;
	}
    
    public String getValorApresentar(){
        return UteisJSF.internacionalizar("enum_TipoIdadeExigidaEnum_"+this.name());
    }
    
	private TipoIdadeExigidaEnum(String valor, String descricao) {
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
		return UteisJSF.internacionalizar("enum_TipoIdadeExigidaEnum_"+this.descricao);
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
}