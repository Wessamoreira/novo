package negocio.comuns.administrativo.enumeradores;

public enum EsgotoSanitarioEnum {

	REDE_PUBLICA("PU","Rede Pública"),
	FOSSA("FO","Fossa"),	
	FOSSA_RUDIMENTAR("FR", "Fossa Rudimentar"),
	INEXISTENTE("IN","Inexistente");
	
	String valor;
    String descricao;
    
    
    
	private EsgotoSanitarioEnum(String valor, String descricao) {
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
