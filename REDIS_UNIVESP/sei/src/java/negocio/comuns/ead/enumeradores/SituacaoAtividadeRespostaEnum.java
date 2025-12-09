package negocio.comuns.ead.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;


/**
 * @author Victor Hugo 10/10/2014
 */
public enum SituacaoAtividadeRespostaEnum {

	ACERTOU, ERROU, NAO_RESPONDIDA, CANCELADA, ANULADA;
	
	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_SituacaoAtividadeRespostaEnum_" + this.name());
	}
}
