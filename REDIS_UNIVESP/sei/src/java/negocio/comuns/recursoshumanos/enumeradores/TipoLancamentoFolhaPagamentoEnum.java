package negocio.comuns.recursoshumanos.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum TipoLancamentoFolhaPagamentoEnum {
    
	PROVENTO("PROVENTO","Provento"),
	DESCONTO("DESCONTO","Desconto"),
	BASE_CALCULO("BASE_CALCULO","BaseCalculo");

	String valor;
    String descricao;

	public static TipoLancamentoFolhaPagamentoEnum getEnumPorValor(String valor) {
		for (TipoLancamentoFolhaPagamentoEnum tipoLancamento : TipoLancamentoFolhaPagamentoEnum.values()) {
			
			if(tipoLancamento.getValor().equals(valor))
				return tipoLancamento;
		}
		
		return null;
	}
    
    public String getValorApresentar(){
        return UteisJSF.internacionalizar("enum_TipoLancamentoFolhaPagamentoEnum_"+this.name());
    }
    
	private TipoLancamentoFolhaPagamentoEnum(String valor, String descricao) {
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
		return UteisJSF.internacionalizar("enum_TipoLancamentoFolhaPagamentoEnum_"+this.valor);
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
}