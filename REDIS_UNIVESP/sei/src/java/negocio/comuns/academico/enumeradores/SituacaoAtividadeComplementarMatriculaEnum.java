package negocio.comuns.academico.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum SituacaoAtividadeComplementarMatriculaEnum {
	AGUARDANDO_DEFERIMENTO, DEFERIDO, INDEFERIDO, ANULADA;
	
	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_SituacaoAtividadeComplementarMatriculaEnum_"+this.name());
	}
}
