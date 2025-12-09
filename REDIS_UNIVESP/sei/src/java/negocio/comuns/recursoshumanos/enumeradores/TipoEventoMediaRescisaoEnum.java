package negocio.comuns.recursoshumanos.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum TipoEventoMediaRescisaoEnum {

	INCIDE_13_PROPORCIONAL("INCIDE_13_PROPORCIONAL","Incide 13º Proporcional"),
	INCIDE_FERIAS_VENCIDAS("INCIDE_FERIAS_VENCIDAS","Incide Férias Vencidas"),
	INCIDE_FERIAS_PROPORCIONAIS("INCIDE_FERIAS_PROPORCIONAIS","Incide Férias Proporcionais");

	String valor;
	String descricao;

	public static TipoEventoMediaRescisaoEnum getEnumPorValor(String valor) {
		for (TipoEventoMediaRescisaoEnum tipoEvento : TipoEventoMediaRescisaoEnum.values()) {

			if (tipoEvento.getValor().equals(valor))
				return tipoEvento;
		}

		return null;
	}

	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_TipoEventoMediaRescisaoEnum_" + this.name());
	}

	private TipoEventoMediaRescisaoEnum(String valor, String descricao) {
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
		return UteisJSF.internacionalizar("enum_TipoEventoMediaRescisaoEnum_" + this.name());
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}