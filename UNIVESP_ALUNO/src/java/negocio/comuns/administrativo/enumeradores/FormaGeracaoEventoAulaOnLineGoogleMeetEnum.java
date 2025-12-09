package negocio.comuns.administrativo.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum FormaGeracaoEventoAulaOnLineGoogleMeetEnum {

	AUTOMATICA("AUTOMATICA", "Automática"),
	MANUAL("MANUAL", "Manual");

	String valor;
	String descricao;

	public static FormaGeracaoEventoAulaOnLineGoogleMeetEnum getEnumPorValor(String valor) {
		for (FormaGeracaoEventoAulaOnLineGoogleMeetEnum formaContratacaoFuncionario : FormaGeracaoEventoAulaOnLineGoogleMeetEnum.values()) {

			if (formaContratacaoFuncionario.getValor().equals(valor)) {
				return formaContratacaoFuncionario;
			}
		}

		return null;
	}

	public static FormaGeracaoEventoAulaOnLineGoogleMeetEnum getEnumPorName(String name) {
		for (FormaGeracaoEventoAulaOnLineGoogleMeetEnum formaContratacaoFuncionario : FormaGeracaoEventoAulaOnLineGoogleMeetEnum
				.values()) {

			if (formaContratacaoFuncionario.name().equals(name)) {
				return formaContratacaoFuncionario;
			}
		}

		return null;
	}

	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_FormaGeracaoEventoAulaOnLineGoogleMeetEnum_" + this.name());
	}

	private FormaGeracaoEventoAulaOnLineGoogleMeetEnum(String valor, String descricao) {
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
		return this.descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}
