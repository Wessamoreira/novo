/**
 * 
 */
package negocio.comuns.secretaria.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum TipoAlteracaoSituacaoHistoricoEnum {
	TODOS_HISTORICOS,
	APENAS_APROVADOS,
	APENAS_REPROVADOS,
	APENAS_REPROVADOS_POR_FALTA,
	NENHUM;
	
	public String getValorApresentar(){
        return UteisJSF.internacionalizar("enum_TipoAlteracaoSituacaoHistorico_"+this.name());
    }
}
