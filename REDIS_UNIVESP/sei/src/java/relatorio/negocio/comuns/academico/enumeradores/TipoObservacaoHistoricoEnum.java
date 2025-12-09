package relatorio.negocio.comuns.academico.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum TipoObservacaoHistoricoEnum {
	COMPLEMENTAR, INTEGRALIZACAO, CERTIFICADO_ESTUDO;
	
	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_TipoObservacaoHistoricoEnum_"+this.name());
	}
}
