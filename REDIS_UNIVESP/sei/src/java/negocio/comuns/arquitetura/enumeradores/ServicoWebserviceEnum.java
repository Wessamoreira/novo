package negocio.comuns.arquitetura.enumeradores;

public enum ServicoWebserviceEnum {
	VINCULACOES_NOTA("/vinculacoes/notas");
	
		
	private ServicoWebserviceEnum(String nome) {
		this.nome = nome;
	}

	private String nome;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	
	
}
