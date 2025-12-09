package negocio.comuns.ead.enumeradores;

import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;

/**
 * 
 * @author Victor Hugo de Paula Costa 03/03/2015
 *
 */
public enum PoliticaSelecaoQuestaoEnum {

	NENHUM, QUALQUER_QUESTAO, QUESTOES_TODOS_ASSUNTOS_CONTEUDO, QUESTOES_ASSUNTO_UNIDADE, QUESTOES_ASSUNTO_ESTUDADOS;		
	
    public String getValorApresentar(){
        return UteisJSF.internacionalizar("enum_PoliticaSelecaoQuestaoEnum_"+this.name());
    }
    
    public String getName() {
    	return this.name();
    }
    
    public boolean isNenhum() {
		return Uteis.isAtributoPreenchido(name()) &&  name().equals(PoliticaSelecaoQuestaoEnum.NENHUM.name());
	}
    
    public boolean isQualquerQuestao() {
    	return Uteis.isAtributoPreenchido(name()) &&  name().equals(PoliticaSelecaoQuestaoEnum.QUALQUER_QUESTAO.name());
    }
    
    public boolean isQuestoesTodosAssuntoConteudo() {
    	return Uteis.isAtributoPreenchido(name()) &&  name().equals(PoliticaSelecaoQuestaoEnum.QUESTOES_TODOS_ASSUNTOS_CONTEUDO.name());
    }
    
    public boolean isQuestoesAssuntoUnidade() {
    	return Uteis.isAtributoPreenchido(name()) &&  name().equals(PoliticaSelecaoQuestaoEnum.QUESTOES_ASSUNTO_UNIDADE.name());
    }
    
    public boolean isQuestoesAssuntoEstudado() {
    	return Uteis.isAtributoPreenchido(name()) &&  name().equals(PoliticaSelecaoQuestaoEnum.QUESTOES_ASSUNTO_ESTUDADOS.name());
    }
    
}
