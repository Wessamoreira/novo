package negocio.comuns.recursoshumanos.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum TipoTemplateFolhaPagamentoEnum {

	GRUPO_LANCAMENTO("GRUPO_LANCAMENTO", "Grupo Lançamento"),
	LANCAMENTO("LANCAMENTO", "Lançamento"),
	RESCISAO("RESCISAO", "Rescisão");

	String valor;
    String descricao;

	public static TipoTemplateFolhaPagamentoEnum getEnumPorValor(String valor) {
		for (TipoTemplateFolhaPagamentoEnum tipoLancamento : TipoTemplateFolhaPagamentoEnum.values()) {

			if(tipoLancamento.getValor().equals(valor))
				return tipoLancamento;
		}
		
		return null;
	}
    
    public String getValorApresentar(){
        return UteisJSF.internacionalizar("enum_TipoTemplateFolhaPagamentoEnum_"+this.name());
    }
    
	private TipoTemplateFolhaPagamentoEnum(String valor, String descricao) {
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
		return UteisJSF.internacionalizar("enum_TipoTemplateFolhaPagamentoEnum_"+this.valor);
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
}