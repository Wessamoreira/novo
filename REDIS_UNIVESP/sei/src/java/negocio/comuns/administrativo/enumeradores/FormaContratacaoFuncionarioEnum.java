package negocio.comuns.administrativo.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum FormaContratacaoFuncionarioEnum {
    
	ESTATUTARIO("ESTATUTARIO","Estatutario"),
	CEDIDO("CEDIDO","Cedido"),
	NORMAL("NORMAL","Normal"),
	OUTROS("OUTROS","Outros");

	String valor;
    String descricao;

	public static FormaContratacaoFuncionarioEnum getEnumPorValor(String valor) {
		for (FormaContratacaoFuncionarioEnum formaContratacaoFuncionario : FormaContratacaoFuncionarioEnum.values()) {
			
			if(formaContratacaoFuncionario.getValor().equals(valor))
				return formaContratacaoFuncionario;
		}
		
		return null;
	}
	
	public static FormaContratacaoFuncionarioEnum getEnumPorName(String name) {
		for (FormaContratacaoFuncionarioEnum formaContratacaoFuncionario : FormaContratacaoFuncionarioEnum.values()) {
			
			if(formaContratacaoFuncionario.name().equals(name))
				return formaContratacaoFuncionario;
		}
		
		return null;
	}
    
    public String getValorApresentar(){
        return UteisJSF.internacionalizar("enum_FormaContratacaoEnum_"+this.name());
    }
    
	private FormaContratacaoFuncionarioEnum(String valor, String descricao) {
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
		return UteisJSF.internacionalizar("enum_FormaContratacaoEnum_"+this.descricao);
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
}