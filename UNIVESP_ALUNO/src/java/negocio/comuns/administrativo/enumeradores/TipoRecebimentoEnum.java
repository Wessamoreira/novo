package negocio.comuns.administrativo.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum TipoRecebimentoEnum {
    
	HORISTA("HORISTA","Horista"),
	MENSALISTA("MENSALISTA","Mensalista");

	String valor;
    String descricao;

	public static TipoRecebimentoEnum getEnumPorValor(String valor) {
		for (TipoRecebimentoEnum tipoRecebimento : TipoRecebimentoEnum.values()) {
			
			if(tipoRecebimento.getValor().equals(valor))
				return tipoRecebimento;
		}
		
		return null;
	}
	
	public static TipoRecebimentoEnum getEnumPorName (String name) {
		for (TipoRecebimentoEnum tipoRecebimento : TipoRecebimentoEnum.values()) {
			
			if(tipoRecebimento.name().equals(name))
				return tipoRecebimento;
		}
		
		return null;
	}
    
    public String getValorApresentar(){
        return UteisJSF.internacionalizar("enum_TipoRecebimentoEnum_"+this.name());
    }
    
	private TipoRecebimentoEnum(String valor, String descricao) {
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
		return UteisJSF.internacionalizar("enum_TipoRecebimentoEnum_"+this.descricao);
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
}