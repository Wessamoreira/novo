/**
 * 
 */
package negocio.comuns.patrimonio.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

/**
 * @author Rodrigo Wind
 *
 */
public enum TipoUsoTextoPadraoPatrimonioEnum {
	
	CADASTRO_PATRIMONIO, OCORRENCIA_EMPRESTIMO, 
	OCORRENCIA_MANUTENCAO, OCORRENCIA_TROCA_LOCAL, 
	OCORRENCIA_DESCARTE, OCORRENCIA_SEPARACAO_DESCARTE,
	MAPA_DESCARTE,OCORRENCIA_RESERVA_UNIDADE,OCORRENCIA_RESERVA_LOCAL;
	
	public String getValorApresentar(){
		return UteisJSF.internacionalizar("enum_TipoUsoTextoPadraoPatrimonioEnum_"+this.name());
	}

}
