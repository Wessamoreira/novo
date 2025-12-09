package negocio.comuns.financeiro.enumerador;

import negocio.comuns.utilitarias.UteisJSF;

/**
 * @author Victor Hugo de Paula Costa - 23 de mai de 2016
 *
 */
public enum SituacaoTransacaoEnum {
	/**
	 * @author Victor Hugo de Paula Costa - 23 de mai de 2016 
	 */
	
	APROVADO, REPROVADO, CANCELADO, CONSULTA, CANCELAMENTO_PENDENTE, ESTORNO_PENDENTE;
	
	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_SituacaoTransacaoEnum_" + this.name());
	}
	
	public String getName() {
		return this.name();
	}
}
