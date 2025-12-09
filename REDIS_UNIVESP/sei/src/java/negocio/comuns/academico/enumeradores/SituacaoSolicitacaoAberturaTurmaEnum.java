package negocio.comuns.academico.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum SituacaoSolicitacaoAberturaTurmaEnum {

		AGUARDANDO_AUTORIZACAO, AUTORIZADO, FINALIZADO,  NAO_AUTORIZADO, EM_REVISAO, TODAS;
		
		public String getValorApresentar(){
			return UteisJSF.internacionalizar("enum_SituacaoSolicitacaoAberturaTurmaEnum_"+this.name());
		}
		
		
	
}
