package negocio.comuns.academico.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum SituacaoDocumentoAssinadoPessoaEnum {
	
	PENDENTE, ASSINADO, REJEITADO;
	
	public boolean isPendente() {
		return name()!= null && name().equals(SituacaoDocumentoAssinadoPessoaEnum.PENDENTE.name());
	}
	public boolean isAssinado() {
		return name()!= null && name().equals(SituacaoDocumentoAssinadoPessoaEnum.ASSINADO.name());
	}
	public boolean isRejeitado() {
		return name()!= null && name().equals(SituacaoDocumentoAssinadoPessoaEnum.REJEITADO.name());
	}
	
	public String getDescricao() {
		return UteisJSF.internacionalizar("enum_SituacaoDocumentoAssinadoPessoaEnum_"+this.name());
	}

}
