package negocio.comuns.planoorcamentario.enumeradores;

import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;

public enum SituacaoPlanoOrcamentarioEnum {
	
	EM_CONSTRUCAO("EM", "Em Construção"),
	AGUARDANDO_APROVACAO("AA", "Aguardando Aprovação"),  
	APROVADO("AP", "Aprovado"),
	REVISAO("RE", "Revisão");

	private String valor;
	private String descricao;

	private SituacaoPlanoOrcamentarioEnum(String valor , String descricao) {
		this.valor = valor;
		this.descricao = descricao;
	}

	public static SituacaoPlanoOrcamentarioEnum getSituacaoPlanoOrcamentarioEnum(String escopo) {
		if (!Uteis.isAtributoPreenchido(escopo)) {
			return null;
		}

		for (SituacaoPlanoOrcamentarioEnum escopoEnum : values()) {
			if (escopoEnum.getValor().equals(escopo)) {
				return escopoEnum;
			}
		}
		return null;
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

	public String getValorApresentar(){
		return UteisJSF.internacionalizar("enum_SituacaoPlanoOrcamentarioEnum_"+this.name());				
	}
}