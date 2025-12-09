package negocio.comuns.academico.enumeradores;

/**
 * 
 * @author PedroOtimize
 *
 */
public enum ModuloDisponibilizarMaterialEnum {
	ADMINISTRATIVO("ADMINISTRATIVO", "Administrativo"), ACADEMICO("ACADEMICO", "Acadêmico");

	String valor;
	String descricao;

	ModuloDisponibilizarMaterialEnum(String valor, String descricao) {
		this.valor = valor;
		this.descricao = descricao;
	}

	public static ModuloDisponibilizarMaterialEnum getEnum(String valor) {
		ModuloDisponibilizarMaterialEnum[] valores = values();
		for (ModuloDisponibilizarMaterialEnum obj : valores) {
			if (obj.getValor().equals(valor)) {
				return obj;
			}
		}
		return null;
	}

	public static String getDescricao(String valor) {
		ModuloDisponibilizarMaterialEnum obj = getEnum(valor);
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
		return name().equals(ModuloDisponibilizarMaterialEnum.ACADEMICO.name());
	}

	public boolean isAdministrativo() {
		return name().equals(ModuloDisponibilizarMaterialEnum.ADMINISTRATIVO.name());
	}

}
