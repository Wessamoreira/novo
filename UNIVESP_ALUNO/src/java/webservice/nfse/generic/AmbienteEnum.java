package webservice.nfse.generic;

public enum AmbienteEnum {
	
	PRODUCAO(0, "Produção"),
	HOMOLOGACAO(1, "Homologação");
	
	private int id;
	private String nome;
	
	AmbienteEnum(int id, String nome) {
		this.id = id;
		this.nome = nome;
	}
	
	public int getId() {
		return id;
	}
	
	public String getNome() {
		return nome;
	}
	
	public boolean isProducao() {
    	return name() != null && name().equals(AmbienteEnum.PRODUCAO.name());
    }
	
	public boolean isHomologacao() {
		return name() != null && name().equals(AmbienteEnum.HOMOLOGACAO.name());
	}
	
}
