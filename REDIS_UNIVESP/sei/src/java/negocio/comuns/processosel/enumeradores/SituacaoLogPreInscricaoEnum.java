package negocio.comuns.processosel.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum SituacaoLogPreInscricaoEnum {
	
	SUCESSO, FALHA, REENVIADO;
	
	public String getValorApresentar(){
        return UteisJSF.internacionalizar("enum_SituacaoLogPreInscricaoEnum_"+this.name());
    }
}
