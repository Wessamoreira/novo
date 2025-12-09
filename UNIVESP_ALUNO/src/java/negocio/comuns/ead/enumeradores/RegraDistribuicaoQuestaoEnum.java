package negocio.comuns.ead.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

/**
 * 
 * @author Victor Hugo de Paula Costa 03/03/2015
 *
 */
public enum RegraDistribuicaoQuestaoEnum {

	NENHUM, QUANTIDADE_FIXA_ASSUNTO, QUANTIDADE_DISTRUIBUIDA_ENTRE_ASSUNTOS;
	
    public String getValorApresentar(){
        return UteisJSF.internacionalizar("enum_RegraDistribuicaoQuestaoEnum_"+this.name());
    }
    
    public String getName() {
    	return this.name();
    }
}
