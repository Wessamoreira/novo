package negocio.comuns.secretaria.enumeradores;

public enum SituacaoMapaConvocacaoEnadeEnum {

	MAPA_EM_CONSTRUCAO("Construção"), MAPA_FINALIZADO("Finalizado");

	String descricao;

	SituacaoMapaConvocacaoEnadeEnum(String descricao) {
		this.descricao = descricao;
	}

	public static SituacaoMapaConvocacaoEnadeEnum getEnum(SituacaoMapaConvocacaoEnadeEnum valor) {
		SituacaoMapaConvocacaoEnadeEnum[] valores = values();
		for (SituacaoMapaConvocacaoEnadeEnum obj : valores) {
			if (obj.name().equals(valor.name())) {
				return obj;
			}
		}
		return null;
	}

	public static String getDescricao(SituacaoMapaConvocacaoEnadeEnum valor) {
		SituacaoMapaConvocacaoEnadeEnum obj = getEnum(valor);
		if (obj != null) {
			return obj.getDescricao();
		}
		return valor.getDescricao();
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}
