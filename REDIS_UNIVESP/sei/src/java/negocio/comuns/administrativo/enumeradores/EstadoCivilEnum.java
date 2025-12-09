package negocio.comuns.administrativo.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum EstadoCivilEnum {
    
	SOLTEIRO("SOLTEIRO", "Solteiro"),
	CASADO("CASADO", "Casado"),
	DIVORCIADO("DIVORCIADO", "Divorciado"),
	VIUVO("VIUVO", "Viuvo"),
	SEPARADO("SEPARADO", "Separado"),
	OUTROS("OUTROS", "Outros");
	
	String valor;
    String descricao;

    public String getValorApresentar(){
        return UteisJSF.internacionalizar("enum_EstadoCivilEnum_"+this.name());
    }
    
	private EstadoCivilEnum(String valor, String descricao) {
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
		return UteisJSF.internacionalizar("enum_EstadoCivilEnum_"+this.descricao);
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
}