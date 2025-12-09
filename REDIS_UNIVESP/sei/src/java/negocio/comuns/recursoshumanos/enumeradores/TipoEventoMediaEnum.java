package negocio.comuns.recursoshumanos.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum TipoEventoMediaEnum {

	INCIDE_FERIAS("INCIDE_FERIAS","Incide Férias"),
	INCIDE_FERIAS_RESCISAO("INCIDE_FERIAS_RESCISAO","Incide Férias Rescisão"),
	INCIDE_13("INCIDE_13","Incide 13"),
	INCIDE_13_RESCISAO("INCIDE_13_RESCISAO","Incide 13 Rescisão"),
	INCIDE_AVISO_PREVIO("INCIDE_AVISO_PREVIO","Incide Aviso Prévio"),
	INCIDE_LICENCA_MATERNIDADE("INCIDE_LICENCA_MATERNIDADE","Licença Maternidade");

	String valor;
    String descricao;

	public static TipoEventoMediaEnum getEnumPorValor(String valor) {
		for (TipoEventoMediaEnum tipoEvento : TipoEventoMediaEnum.values()) {
			
			if(tipoEvento.getValor().equals(valor))
				return tipoEvento;
		}
		
		return null;
	}
    
    public String getValorApresentar(){
        return UteisJSF.internacionalizar("enum_TipoEventoMediaEnum_"+this.name());
    }
    
	private TipoEventoMediaEnum(String valor, String descricao) {
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
		return UteisJSF.internacionalizar("enum_TipoEventoFolhaPagamentoEnum_"+this.name());
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
}