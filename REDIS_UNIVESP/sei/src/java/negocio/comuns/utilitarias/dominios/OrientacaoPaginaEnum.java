package negocio.comuns.utilitarias.dominios;

public enum OrientacaoPaginaEnum {

	PAISAGEM("PA", "Paisagem"), RETRATO("RE", "Retrato");

	private OrientacaoPaginaEnum(String key, String value) {
		this.key = key;
		this.value = value;
	}
	
	public static OrientacaoPaginaEnum getEnum(String key){
		if(key.equals(PAISAGEM.getKey())){
			return PAISAGEM;
		}
		return OrientacaoPaginaEnum.RETRATO;
	}

	private String key;
	private String value;

	public String getKey() {
		if (key == null) {
			key = "";
		}
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		if (value == null) {
			value = "";
		}
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public boolean isPaisagem() {
		return name().equals(OrientacaoPaginaEnum.PAISAGEM.name());
	}
	public boolean isRetrato() {
		return name().equals(OrientacaoPaginaEnum.RETRATO.name());
	}

}
