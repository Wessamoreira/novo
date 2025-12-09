package negocio.comuns.academico.enumeradores;

import negocio.comuns.financeiro.enumerador.TipoTransacaoOFXEnum;
import negocio.comuns.utilitarias.Uteis;

/**
 * @author Wellington Rodrigues - 16 de jul de 2015
 *
 */
public enum PeriodicidadeEnum {

	ANUAL("AN", "Anual"), INTEGRAL("IN", "Integral"), SEMESTRAL("SE", "Semestral");

	private String valor;
	private String descricao;

	private PeriodicidadeEnum(String valor, String descricao) {
		this.valor = valor;
		this.descricao = descricao;
	}

	public String getValor() {
		if (valor == null) {
			valor = "";
		}
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getDescricao() {
		if (descricao == null) {
			descricao = "";
		}
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public static PeriodicidadeEnum getEnumPorValor(String valor) {
		for (PeriodicidadeEnum pe : PeriodicidadeEnum.values()) {
			if (pe.getValor().equals(valor)) {
				return pe;
			}
		}
		return null;
	}
	
	 public boolean isIntegral(){
	    	return Uteis.isAtributoPreenchido(name()) && name().equals(PeriodicidadeEnum.INTEGRAL.name());
	    }

}
