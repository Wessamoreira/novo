package negocio.comuns.recursoshumanos.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum TipoEventoFolhaPagamentoEnum {
    
	VALOR("VALOR","Valor"),
	HORA("HORA","Hora"),
	DIA("DIA","Dia"),
	REFERENCIA("REFERENCIA","Referencia");

	String valor;
    String descricao;

	public static TipoEventoFolhaPagamentoEnum getEnumPorValor(String valor) {
		for (TipoEventoFolhaPagamentoEnum tipoEvento : TipoEventoFolhaPagamentoEnum.values()) {
			
			if(tipoEvento.getValor().equals(valor))
				return tipoEvento;
		}
		
		return null;
	}
    
    public String getValorApresentar(){
        return UteisJSF.internacionalizar("enum_TipoEventoFolhaPagamentoEnum_"+this.name());
    }
    
	private TipoEventoFolhaPagamentoEnum(String valor, String descricao) {
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