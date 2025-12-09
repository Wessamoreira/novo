package negocio.comuns.ead.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

/**
 * @author Victor Hugo 11/11/2014
 */
public enum SituacaoProgramacaoTutoriaOnlineEnum {

	ATIVO, INATIVO;
	
	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_SituacaoProgramacaoTutoriaOnlineEnum_" + this.name());
	}
	
	public boolean isSituacaoAtivo() {
		return name().equals(SituacaoProgramacaoTutoriaOnlineEnum.ATIVO.name());
	}
	public boolean isSituacaInativo() {
		return name().equals(SituacaoProgramacaoTutoriaOnlineEnum.INATIVO.name());
	}
}
