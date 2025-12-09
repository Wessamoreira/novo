package negocio.comuns.basico.enumeradores;

public enum TagsCatracaEnum {

	TAG_NOME, TAG_MATRICULA;

	public String getName() {
		return this.name();
	}

	public static TagsCatracaEnum getEnum(String valor) {
		for (TagsCatracaEnum obj : values()) {
			if (obj.name().equals(valor)) {
				return obj;
			}
		}
		return null;
	}

}
