package negocio.comuns.financeiro.enumerador;

import negocio.comuns.utilitarias.Uteis;

public enum TipoAutenticacaoRegistroRemessaOnlineEnum {
	
	TOKEN, CERTIFICADO ,CHAVEAUTENTICACAO;
	
	public boolean isToken() {
		return Uteis.isAtributoPreenchido(this.name()) && TipoAutenticacaoRegistroRemessaOnlineEnum.TOKEN.name().equals(this.name());
	}
	
	public boolean isCertificado() {
		return Uteis.isAtributoPreenchido(this.name()) && TipoAutenticacaoRegistroRemessaOnlineEnum.CERTIFICADO.name().equals(this.name());
	}

	public boolean isChaveAutenticacao() {
		return Uteis.isAtributoPreenchido(this.name()) && TipoAutenticacaoRegistroRemessaOnlineEnum.CHAVEAUTENTICACAO.name().equals(this.name());
	}
}
