package negocio.comuns.administrativo;

public enum AbastecimentoEnergiaEnum {

	REDE_PUBLICA("PU","Rede Pública"),
	GERADOR("GE","Gerador"),
	PUBLICA_GERADOR("PG","Pública e Gerador"),
	OUTROS("OU","Outros"),
	INEXISTENTE("IN","Inexistente");
	
	
	String valor;
    String descricao;
    
	private AbastecimentoEnergiaEnum(String valor, String descricao) {
		this.valor = valor;
		this.descricao = descricao;
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
