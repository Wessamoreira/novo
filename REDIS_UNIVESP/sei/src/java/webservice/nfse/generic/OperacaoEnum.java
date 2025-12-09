package webservice.nfse.generic;

public enum OperacaoEnum {
	SEM_DEDUCAO("A", "Sem Dedução"), COM_DEDUCAO_MATERIAIS("B", "Com Dedução/Materiais"), IMUNE_ISENTA_DE_ISSQN("C", "Imune/Isenta de ISSQN"), DEVOLUCAO_SIMPLES_REMESSA("D", "Devolução/Simples Remessa"), INTERMEDIACAO("J", "Intemediação");
	private String id;
	private String nome;
	
	OperacaoEnum(String id, String nome) {
		this.id = id;
		this.nome = nome;
	}
	
	public String getId() {
		return id;
	}
	
	public String getNome() {
		return nome;
	}
	
}
