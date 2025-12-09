package negocio.comuns.academico.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum SituacaoRenovacaoTurmaEnum {
	
	AGUARDANDO_PROCESSAMENTO, EM_PROCESSAMENTO, PROCESSAMENTO_INTERROMPIDO, ERRO_PROCESSAMENTO, PROCESSAMENTO_CONCLUIDO;
	
	public String getValorApresentar(){
		return UteisJSF.internacionalizar("enum_SituacaoRenovacaoTurmaEnum_"+this.name());
	}

}
