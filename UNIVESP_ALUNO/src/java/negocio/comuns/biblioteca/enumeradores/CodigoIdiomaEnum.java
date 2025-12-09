package negocio.comuns.biblioteca.enumeradores;

public enum CodigoIdiomaEnum {

	POR("por", "Português"), 
	SPA("spa", "Espanhol"), 
	ENG("eng", "Inglês"), 
	FRE("fre", "Francês"), 
	JPN("jpn", "Japonês"), 
	ITA("ita", "Italiano"),
	RUS("rus", "Russo"),
	CHI("chi", "Chinês"),
	GER("ger", "Alemão");

	private String key;
	private String value;

	public static CodigoIdiomaEnum getEnumPorValor(String valor) {
		for (CodigoIdiomaEnum codigoIdioma : CodigoIdiomaEnum.values()) {
			if (codigoIdioma.getKey().equalsIgnoreCase(valor)) {
				return codigoIdioma;
			}
		}
		return null;
	}

	private CodigoIdiomaEnum(String key, String value) {
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
