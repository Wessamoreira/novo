package negocio.comuns.administrativo.enumeradores;

public enum TratamentoLixoEnum {

	SEPARACAO_LIXO("SL","Separação do lixo/resíduos"),
	REAPROVEITAMENTO("RE","Reaproveitamento/Reutilização"),
	RECICLAGEM("RC","Reciclagem"),
	NAO_FAZ_TRATAMENTO("NT","Não faz Tratamento");
	
	String valor;
    String descricao;
    
    
    
	private TratamentoLixoEnum(String valor, String descricao) {
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
