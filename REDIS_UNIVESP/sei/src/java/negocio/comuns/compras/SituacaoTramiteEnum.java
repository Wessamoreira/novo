package negocio.comuns.compras;

public enum SituacaoTramiteEnum {

	EM_CONSTRUCAO("Em construção"), INATIVO("Inativo"), ATIVO("Ativo");

	String nome;

	private SituacaoTramiteEnum(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

}
