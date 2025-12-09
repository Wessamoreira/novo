package negocio.comuns.financeiro.enumerador;

/**
 * 
 * @author PedroOtimize
 *
 */
public enum TipoContaEnum {
	
	CREDITO_EM_CONTA_CORRENTE("01","Crédito em Conta Corrente"),
	CREDITO_EM_CONTA_POUPANCA("02","Crédito em Conta Poupança");
		
	private String valor;
	private String descricao;
	
	private TipoContaEnum(String valor, String descricao) {
		this.valor = valor;
		this.descricao = descricao;
	}
	
	public String getValor() {
		return this.valor;
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
	
	public boolean isDocPoupanca(){
		return name().equals(TipoContaEnum.CREDITO_EM_CONTA_POUPANCA.name());
	}


}
