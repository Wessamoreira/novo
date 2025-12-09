package negocio.comuns.ead.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;


/**
 * @author Victor Hugo 10/10/2014
 */
public enum SituacaoAvaliacaoOnlineMatriculaEnum {

	APROVADO, REPROVADO, EM_REALIZACAO, AGUARDANDO_REALIZACAO, AGUARDANDO_DATA_LIBERACAO, EXPIRADO;
	
	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_SituacaoAvaliacaoOnlineMatriculaEnum_" + this.name());
	}
	
	public String getName() {
		return this.name();
	}	
	
	public boolean isAprovado() {
		return equals(APROVADO);
	}

	public boolean isReprovado() {
		return equals(REPROVADO);
	}
}
