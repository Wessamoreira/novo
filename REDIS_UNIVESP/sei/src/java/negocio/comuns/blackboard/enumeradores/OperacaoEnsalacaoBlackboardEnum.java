package negocio.comuns.blackboard.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum OperacaoEnsalacaoBlackboardEnum {
  INCLUIR, EXCLUIR;
	
	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_OperacaoEnsalacaoBlackboardEnum_"+this.name());
	}
}
