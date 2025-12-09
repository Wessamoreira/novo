package negocio.comuns.estagio.enumeradores;

import negocio.comuns.utilitarias.Uteis;

public enum RegrasSubstituicaoGrupoPessoaItemEnum {
	FACILITADOR_ESPECIFICO,
	ENTRE_GRUPOPESSOA;
	
	public boolean isFacilitadorEspecifico() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(RegrasSubstituicaoGrupoPessoaItemEnum.FACILITADOR_ESPECIFICO.name());
	}
	
	public boolean isEntreGrupoPessoa() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(RegrasSubstituicaoGrupoPessoaItemEnum.ENTRE_GRUPOPESSOA.name());
	}

}
