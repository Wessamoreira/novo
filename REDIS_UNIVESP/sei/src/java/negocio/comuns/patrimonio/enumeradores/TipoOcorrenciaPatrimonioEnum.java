/**
 * 
 */
package negocio.comuns.patrimonio.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

/**
 * @author Rodrigo Wind
 *
 */
public enum TipoOcorrenciaPatrimonioEnum {

	MANUTENCAO, EMPRESTIMO, TROCA_LOCAL, SEPARAR_DESCARTE, DESCARTE, RESERVA_UNIDADE, RESERVA_LOCAL;
	
	public String getValorApresentar(){
		return UteisJSF.internacionalizar("enum_TipoOcorrenciaPatrimonioEnum_"+this.name());
	}
	
	public TipoUsoTextoPadraoPatrimonioEnum getTipoUsoTextoPadrao(){
		switch (this) {
		case DESCARTE:
			return TipoUsoTextoPadraoPatrimonioEnum.OCORRENCIA_DESCARTE;
		case EMPRESTIMO:
			return TipoUsoTextoPadraoPatrimonioEnum.OCORRENCIA_EMPRESTIMO;
		case MANUTENCAO:
			return TipoUsoTextoPadraoPatrimonioEnum.OCORRENCIA_MANUTENCAO;
		case SEPARAR_DESCARTE:
			return TipoUsoTextoPadraoPatrimonioEnum.OCORRENCIA_SEPARACAO_DESCARTE;
		case TROCA_LOCAL:
			return TipoUsoTextoPadraoPatrimonioEnum.OCORRENCIA_TROCA_LOCAL;
		case RESERVA_UNIDADE:
			return TipoUsoTextoPadraoPatrimonioEnum.OCORRENCIA_RESERVA_UNIDADE;
		case RESERVA_LOCAL:
			return TipoUsoTextoPadraoPatrimonioEnum.OCORRENCIA_RESERVA_LOCAL;
		default:
			return TipoUsoTextoPadraoPatrimonioEnum.CADASTRO_PATRIMONIO;
		}
	}
	
	
}
