package negocio.comuns.arquitetura.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum AtivoInativoEnum {
    
	ATIVO("ATIVO", "Ativo"),
	INATIVO("INATIVO", "Inativo");
	
	String valor;
    String descricao;

    public String getValorApresentar(){
        return UteisJSF.internacionalizar("enum_AtivoInativoEnum_"+this.name());
    }
    
	private AtivoInativoEnum(String valor, String descricao) {
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
		return UteisJSF.internacionalizar("enum_AtivoInativoEnum_"+this.valor);
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
}