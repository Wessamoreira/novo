package negocio.comuns.compras.enumeradores;

import negocio.comuns.utilitarias.Uteis;

/**
 * 
 * @author Pedro Otimize
 *
 */
public enum TipoProdutoServicoEnum {
	PRODUTO, SERVICO;
	
	public boolean isProduto() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoProdutoServicoEnum.PRODUTO.name());
	}

	public boolean isServico() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoProdutoServicoEnum.SERVICO.name());
	}

}
