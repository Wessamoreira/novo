package negocio.comuns.administrativo.enumeradores;

public enum EstadoBemEnum {

	OTIMO("OTIMO","Ótimo"),
	BOM("BOM","Bom"),
	RAZOAVEL("RAZOAVEL","Razoável"),
	RUIM("RUIM","Ruim"),
	QUEBRADO("QUEBRADO","Quebrado"),
	NAO_FUNCIONA("NAO_FUNCIONA","Não Funciona");
	
	String valor;
    String descricao;
    
	private EstadoBemEnum(String valor, String descricao) {
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
