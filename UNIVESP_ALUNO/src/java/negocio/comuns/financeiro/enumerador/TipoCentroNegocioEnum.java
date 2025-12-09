package negocio.comuns.financeiro.enumerador;

/**
 * 
 * @author PedroOtimize
 *
 */
public enum TipoCentroNegocioEnum {
	ADMINISTRATIVO("ADMINISTRATIVO", "Administrativo"), ACADEMICO("ACADEMICO", "Acadêmico");

	String valor;
	String descricao;

	TipoCentroNegocioEnum(String valor, String descricao) {
		this.valor = valor;
		this.descricao = descricao;
	}

	public static TipoCentroNegocioEnum getEnum(String valor) {
		TipoCentroNegocioEnum[] valores = values();
		for (TipoCentroNegocioEnum obj : valores) {
			if (obj.getValor().equals(valor)) {
				return obj;
			}
		}
		return null;
	}

	public static String getDescricao(String valor) {
		TipoCentroNegocioEnum obj = getEnum(valor);
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

	public boolean isAcademico() {
		return name().equals(TipoCentroNegocioEnum.ACADEMICO.name());
	}

	public boolean isAdministrativo() {
		return name().equals(TipoCentroNegocioEnum.ADMINISTRATIVO.name());
	}

}
