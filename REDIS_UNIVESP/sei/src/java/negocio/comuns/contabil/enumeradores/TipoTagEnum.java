package negocio.comuns.contabil.enumeradores;

/**
 * 
 * @author PedroOtimize
 *
 */
public enum TipoTagEnum {
	TAG("TAG", "Tag"),
	TAG_ROOT("TAG_ROOT", "Tag Root"),
	TAG_LIST("TAG_LIST", "Tag Lista"),
	TAG_FORMULA("TAG_FORMULA", "Tag Fórmula"),
	FIXO("FIXO", "Fixo"), 
	CAMPO("CAMPO", "Campo");

	String valor;
	String descricao;

	TipoTagEnum(String valor, String descricao) {
		this.valor = valor;
		this.descricao = descricao;
	}

	public static TipoTagEnum getEnum(String valor) {
		TipoTagEnum[] valores = values();
		for (TipoTagEnum obj : valores) {
			if (obj.getValor().equals(valor)) {
				return obj;
			}
		}
		return null;
	}

	public static String getDescricao(String valor) {
		TipoTagEnum obj = getEnum(valor);
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
	
	public boolean isTag(){
		return name().equals(TipoTagEnum.TAG.name());
	}
	
	public boolean isTagRoot(){
		return name().equals(TipoTagEnum.TAG_ROOT.name());
	}
	
	public boolean isTagList(){
		return name().equals(TipoTagEnum.TAG_LIST.name());
	}
	
	public boolean isTagFormula(){
		return name().equals(TipoTagEnum.TAG_FORMULA.name());
	}
	
	public boolean isFixo(){
		return name().equals(TipoTagEnum.FIXO.name());
	}
	
	public boolean isCampo(){
		return name().equals(TipoTagEnum.CAMPO.name());
	}

}
