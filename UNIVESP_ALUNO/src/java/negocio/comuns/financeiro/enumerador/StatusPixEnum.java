package negocio.comuns.financeiro.enumerador;

public enum StatusPixEnum {

	NENHUM, ATIVA, CONCLUIDA, REMOVIDA_PELO_USUARIO_RECEBEDOR, REMOVIDA_PELO_PSP;
	
	public boolean isNenhum() {
		return name() != null && name().equals(SituacaoPixEnum.NENHUM.name());
	}

	public boolean isAtiva() {
		return name() != null && name().equals(StatusPixEnum.ATIVA.name());
	}

	public boolean isConcluida() {
		return name() != null && name().equals(StatusPixEnum.CONCLUIDA.name());
	}

	public boolean isRemovidaUsuarioRecebedor() {
		return name() != null && name().equals(StatusPixEnum.REMOVIDA_PELO_USUARIO_RECEBEDOR.name());
	}

	public boolean isRemovidaPsp() {
		return name() != null && name().equals(StatusPixEnum.REMOVIDA_PELO_PSP.name());
	}

}
