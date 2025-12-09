/**
 * 
 */
package negocio.comuns.processosel.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

/**
 * @author Carlos Eugênio
 *
 */
public enum TipoProcessamentoProvaPresencial {
	LEITURA_ARQUIVO_NOTA_LANCADA,
	LEITURA_ARQUIVO_RESPOSTA_GABARITO;
	
	public String getValorApresentar(){
        return UteisJSF.internacionalizar("enum_TipoProcessamentoProvaPresencial_"+this.name());
    }
}
