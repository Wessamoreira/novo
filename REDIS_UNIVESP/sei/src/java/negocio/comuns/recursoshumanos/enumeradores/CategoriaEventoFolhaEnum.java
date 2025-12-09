package negocio.comuns.recursoshumanos.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum CategoriaEventoFolhaEnum {
    
	SALARIO_BASE("SALARIO_BASE","SALARIO BASE"),
	IRRF("IRRF","IRRF"),
	INSS("INSS","INSS"),
	FERIAS("FERIAS","FERIAS"),
	BENEFICIOS("BENEFICIOS","BENEFICIOS");

	String valor;
    String descricao;

	public static CategoriaEventoFolhaEnum getEnumPorValor(String valor) {
		for (CategoriaEventoFolhaEnum tipoEvento : CategoriaEventoFolhaEnum.values()) {
			
			if(tipoEvento.getValor().equals(valor))
				return tipoEvento;
		}
		
		return null;
	}
    
    public String getValorApresentar(){
        return UteisJSF.internacionalizar("enum_CategoriaEventoFolhaEnum_"+this.name());
    }
    
	private CategoriaEventoFolhaEnum(String valor, String descricao) {
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
		return UteisJSF.internacionalizar("enum_TipoEventoFolhaPagamentoEnum_"+this.valor);
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
}