package negocio.comuns.recursoshumanos.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum SituacaoHoraAtividadeExtraClasseEnum {
    
	AGUARDANDO_APROVACAO("AGUARDANDO_APROVACAO","Aguardando Aprovação"),
	APROVADO("APROVADO","Aprovado"),
	INDEFERIDO("INDEFERIDO","Indeferido"),
	PENDENTE("PENDENTE","Pendente");

	String valor;
    String descricao;

	public static SituacaoHoraAtividadeExtraClasseEnum getEnumPorValor(String valor) {
		for (SituacaoHoraAtividadeExtraClasseEnum situacaoHoraAtividadeExtraClasse : SituacaoHoraAtividadeExtraClasseEnum.values()) {
			
			if(situacaoHoraAtividadeExtraClasse.getValor().equals(valor))
				return situacaoHoraAtividadeExtraClasse;
		}
		
		return null;
	}
    
    public String getValorApresentar(){
        return UteisJSF.internacionalizar("enum_SituacaoHoraAtividadeExtraClasseEnum_"+this.name());
    }
    
	private SituacaoHoraAtividadeExtraClasseEnum(String valor, String descricao) {
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
		return UteisJSF.internacionalizar("enum_SituacaoHoraAtividadeExtraClasseEnum_"+this.valor);
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
}