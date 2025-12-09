package negocio.comuns.recursoshumanos.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum LocalIncidenciaEnum {
    
	FOLHA_NORMAL("FOLHA_NORMAL","FolhaNormal"),
	FERIAS("FERIAS","Ferias"),
	DECIMO_TERCEIRO("DECIMO_TERCEIRO","DecimoTerceiro");

	String valor;
    String descricao;

	public static LocalIncidenciaEnum getEnumPorValor(String valor) {
		for (LocalIncidenciaEnum localIncidencia : LocalIncidenciaEnum.values()) {
			
			if(localIncidencia.getValor().equals(valor))
				return localIncidencia;
		}
		
		return null;
	}
    
    public String getValorApresentar(){
        return UteisJSF.internacionalizar("enum_LocalIncidenciaEnum_FOLHA_NORMAL"+this.name());
    }
    
	private LocalIncidenciaEnum(String valor, String descricao) {
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
		return UteisJSF.internacionalizar("enum_LocalIncidenciaEnum_FOLHA_NORMAL"+this.descricao);
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
}