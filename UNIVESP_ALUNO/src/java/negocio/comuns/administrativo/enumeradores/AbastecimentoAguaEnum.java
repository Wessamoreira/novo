package negocio.comuns.administrativo.enumeradores;

public enum AbastecimentoAguaEnum {

	REDE_PUBLICA("PU","Rede Pública"),
	POCO_ARTESIANO("PO","Poço Artesiano"),
	CACIMBA_CISTERNA_POCO("CC","Cacimba - Cisterna - Poço"),
	FONTE_RIO_IGARAPE_RIACHO_CORREGO("FR","Fonte - Rio - Igarapé - Riacho - Corrégo"),
	INEXISTENTE("IN","Inexistente");
	
	
	
	private AbastecimentoAguaEnum(String valor, String descricao) {
		this.valor = valor;
		this.descricao = descricao;
	}
	
	String valor;
    String descricao;
    
    
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
