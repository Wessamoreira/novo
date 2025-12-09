package negocio.comuns.ead.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;


/*
 * @author Victor Hugo 07/10/2014
 */
public enum SituacaoAtividadeEnum {
	
	CONCLUIDA, NAO_CONCLUIDA;
	
	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_SituacaoAtividadeEnum_"+this.name());
	}
}
