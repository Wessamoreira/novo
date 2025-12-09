package negocio.comuns.contabil.enumeradores;

public enum TipoCampoTagEnum {
	STRING("STRING","String"),
	INTEIRO("INTEIRO","Inteiro"),
	DOUBLE("DOUBLE","Double"),
	DATA("DATA","Data"),
	ENUM("ENUM","Enum"),
	BOOLEAN("BOOLEAN","Boolean");
	
	String valor;
	String descricao;

	TipoCampoTagEnum(String valor, String descricao) {
		this.valor = valor;
		this.descricao = descricao;
	}

	public static TipoCampoTagEnum getEnum(String valor) {
		TipoCampoTagEnum[] valores = values();
		for (TipoCampoTagEnum obj : valores) {
			if (obj.getValor().equals(valor)) {
				return obj;
			}
		}
		return null;
	}

	public static String getDescricao(String valor) {
		TipoCampoTagEnum obj = getEnum(valor);
		if (obj != null) {
			return obj.getDescricao();
		}
		return valor;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public boolean isTipoString(){
		return name().equals(TipoCampoTagEnum.STRING.name());
	}

	public boolean isTipoInteiro(){
		return name().equals(TipoCampoTagEnum.INTEIRO.name());
	}
	
	public boolean isTipoDouble(){
		return name().equals(TipoCampoTagEnum.DOUBLE.name());
	}
	
	public boolean isTipoBoolean(){
		return name().equals(TipoCampoTagEnum.BOOLEAN.name());
	}
	
	public boolean isTipoData(){
		return name().equals(TipoCampoTagEnum.DATA.name());
	}
	
	public boolean isTipoEnum(){
		return name().equals(TipoCampoTagEnum.ENUM.name());
	}
	
}
