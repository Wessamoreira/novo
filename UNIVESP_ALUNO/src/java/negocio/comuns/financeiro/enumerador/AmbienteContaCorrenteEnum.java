package negocio.comuns.financeiro.enumerador;

import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;

public enum AmbienteContaCorrenteEnum {

	HOMOLOGACAO, PRODUCAO;

	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_AmbienteContaCorrenteEnum_" + this.name());
	}

	public boolean isHomologacao() {
		return Uteis.isAtributoPreenchido(this.name()) && AmbienteContaCorrenteEnum.HOMOLOGACAO.name().equals(this.name());
	}
	
	public boolean isProducao() {
		return Uteis.isAtributoPreenchido(this.name()) && AmbienteContaCorrenteEnum.PRODUCAO.name().equals(this.name());
	}
}
