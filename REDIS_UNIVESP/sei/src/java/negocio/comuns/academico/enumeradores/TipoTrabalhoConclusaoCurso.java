package negocio.comuns.academico.enumeradores;

public enum TipoTrabalhoConclusaoCurso {

	ARTIGO("AR", "Artigo"), MONOGRAFIA("MO", "Monografia");

	public String valor;
	public String descricao;

	TipoTrabalhoConclusaoCurso(String valor, String descricao) {
		this.valor = valor;
		this.descricao = descricao;
	}

	public static TipoTrabalhoConclusaoCurso getEnum(String valor) {
		TipoTrabalhoConclusaoCurso[] valores = values();
		for (TipoTrabalhoConclusaoCurso obj : valores) {
			if (obj.getValor().equals(valor)) {
				return obj;
			}
		}
		return null;
	}

	public static String getDescricao(String valor) {
		TipoTrabalhoConclusaoCurso obj = getEnum(valor);
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
}
