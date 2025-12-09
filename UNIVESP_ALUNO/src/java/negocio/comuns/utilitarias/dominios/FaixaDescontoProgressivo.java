package negocio.comuns.utilitarias.dominios;

import negocio.comuns.financeiro.enumerador.TipoCentroResultadoOrigemDetalheEnum;
import negocio.comuns.utilitarias.Uteis;

public enum FaixaDescontoProgressivo {

	NENHUM(0, "Nenhum"), PRIMEIRO(1, "Primeiro"), SEGUNDO(2, "Segundo"), TERCEIRO(3, "Terceiro"), QUARTO(4, "Quarto");

	Integer valor;
	String descricao;

	FaixaDescontoProgressivo(Integer valor, String descricao) {
		this.valor = valor;
		this.descricao = descricao;
	}

	public static FaixaDescontoProgressivo getEnum(String desc) {
		if (desc == null) {
			desc = "";
		}
		FaixaDescontoProgressivo[] valores = values();
		for (FaixaDescontoProgressivo obj : valores) {
			if (obj.getDescricao().equals(desc)) {
				return obj;
			}
		}
		return null;
	}
	
	public static FaixaDescontoProgressivo getEnum(Integer val) {
		if (val == null) {
			val = 0;
		}
		FaixaDescontoProgressivo[] valores = values();
		for (FaixaDescontoProgressivo obj : valores) {
			if (obj.getValor().equals(val)) {
				return obj;
			}
		}
		return null;
	}

	public static String getDescricao(String valor) {
		FaixaDescontoProgressivo obj = getEnum(valor);
		if (obj != null) {
			return obj.getDescricao();
		}
		return valor;
	}
	
	public static String getDescricao(Integer valor) {
		FaixaDescontoProgressivo obj = getEnum(valor);
		if (obj != null) {
			return obj.getDescricao();
		}
		return "";
	}

	public Integer getValor() {
		return valor;
	}

	public void setValor(Integer valor) {
		this.valor = valor;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public boolean isPrimeiraFaixa(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(FaixaDescontoProgressivo.PRIMEIRO.name());
	}
	
	public boolean isSegundaFaixa(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(FaixaDescontoProgressivo.SEGUNDO.name());
	}
	
	public boolean isTerceiraFaixa(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(FaixaDescontoProgressivo.TERCEIRO.name());
	}
	
	public boolean isQuartaFaixa(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(FaixaDescontoProgressivo.QUARTO.name());
	}

}