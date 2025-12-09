package negocio.comuns.academico.enumeradores;

import negocio.comuns.utilitarias.Uteis;

public enum TipoInscricaoEnadeEnum {
	
	
	NENHUM("", "nenhum"),
	INGRESSANTE("IN","Ingressante"),
	CONCLUINTE("CO","Concluinte ");

	
	
	
	private String valor;
	private String descricao;
	
	private TipoInscricaoEnadeEnum(String valor, String descricao) {
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
	
	

	public boolean isIngressante(){
		return Uteis.isAtributoPreenchido(this.name()) && this.name().equals(TipoInscricaoEnadeEnum.INGRESSANTE.name());
	}
	
	public boolean isConcluinte(){
		return Uteis.isAtributoPreenchido(this.name()) && this.name().equals(TipoInscricaoEnadeEnum.CONCLUINTE.name());
	}
	
	public static TipoInscricaoEnadeEnum getEnumPorValor(String valor) {
		for(TipoInscricaoEnadeEnum tipoInscricaoEnade: TipoInscricaoEnadeEnum.values()) {
			if(tipoInscricaoEnade.getValor().equals(valor)) {
				return tipoInscricaoEnade;
			}
		}
		return TipoInscricaoEnadeEnum.NENHUM;
	}
}
