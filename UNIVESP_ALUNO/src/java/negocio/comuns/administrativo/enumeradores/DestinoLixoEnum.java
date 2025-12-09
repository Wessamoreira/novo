package negocio.comuns.administrativo.enumeradores;

public enum DestinoLixoEnum {

	COLETA_PERIODICA("CP","Coleta Periódica"),
	QUEIMA("QU","Queima"),
	JOGA_OUTRA_AREA("JA","Joga em outra área"),
	RECICLA("RC","Recicla"),
	ENTERRA("EN","Enterra"),
	OUTROS("OU","Outros");
	
	String valor;
    String descricao;
    
    
    
	private DestinoLixoEnum(String valor, String descricao) {
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
