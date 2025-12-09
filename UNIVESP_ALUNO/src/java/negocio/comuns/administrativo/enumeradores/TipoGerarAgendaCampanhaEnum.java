/**
 * 
 */
package negocio.comuns.administrativo.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

/**
 * @author Carlos Eugênio
 *
 */
public enum TipoGerarAgendaCampanhaEnum {
	GERAR_AGENDA_PROSPECT_DISTRIBUINDO_IGUALITARIAMENTE_ENTRE_CONSULTORES_CAMPANHA,
	GERAR_AGENDA_PROSPECT_PRIORIZANDO_CONSULTOR_RESPONSAVEL,
	GERAR_AGENDA_PROSPECT_PRIORIZANDO_CONSULTOR_ULTIMA_INTERACAO;
	
	 public String getValorApresentar(){
	        return UteisJSF.internacionalizar("enum_TipoGerarAgendaCampanhaEnum_"+this.name());
	    }

}
