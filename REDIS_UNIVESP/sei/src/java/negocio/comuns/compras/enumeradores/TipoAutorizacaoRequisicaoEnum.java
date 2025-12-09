package negocio.comuns.compras.enumeradores;

import negocio.comuns.utilitarias.Uteis;

/**
 * 
 * @author PedroOtimize
 *
 */
public enum TipoAutorizacaoRequisicaoEnum {

	NENHUM, RETIRADA, COMPRA_DIRETA, COTACAO;

	public boolean isNenhum() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoAutorizacaoRequisicaoEnum.NENHUM.name());
	}
	
	public boolean isRetirada() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoAutorizacaoRequisicaoEnum.RETIRADA.name());
	}

	public boolean isCompraDireta() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoAutorizacaoRequisicaoEnum.COMPRA_DIRETA.name());
	}

	public boolean isCotacao() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoAutorizacaoRequisicaoEnum.COTACAO.name());
	}

}
