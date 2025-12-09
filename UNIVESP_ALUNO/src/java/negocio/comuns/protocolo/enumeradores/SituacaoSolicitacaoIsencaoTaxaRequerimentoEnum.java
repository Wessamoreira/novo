package negocio.comuns.protocolo.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum SituacaoSolicitacaoIsencaoTaxaRequerimentoEnum {

	NAO_REQUERIDA, AGUARDANDO_RESPOSTA, DEFERIDO, INDEFERIDO;
	
	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_SituacaoSolicitacaoIsencaoTaxaRequerimentoEnum_"+this.name());
	}
}
