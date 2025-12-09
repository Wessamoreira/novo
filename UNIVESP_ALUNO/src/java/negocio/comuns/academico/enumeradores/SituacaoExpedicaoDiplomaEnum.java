package negocio.comuns.academico.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;


public enum SituacaoExpedicaoDiplomaEnum {
    
	PENDENTE("PENDENTE", "XML Diploma Pendente Assinatura"), 
	ATIVO("ATIVO", "XML Diploma Assinado"), 
	ANULADO("ANULADO", "Anulado"), 
	AGUARDANDO_GERACAO_XML("AGUARDANDO_GERACAO_XML", "Aguardando Geração XML Diploma"),
	EXPEDIDO("FINALIZADO", "Diploma Emitido Sem XML");
	
	String valor;
    String descricao;

    SituacaoExpedicaoDiplomaEnum(String valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }
   
    
    public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_SituacaoExpedicaoDiplomaEnum_" + this.name());
	}

	public boolean isSituacaoPendente() {
		return name().equals(SituacaoExpedicaoDiplomaEnum.PENDENTE.name());
	}

	public boolean isSituacaoAtivo() {
		return name().equals(SituacaoExpedicaoDiplomaEnum.ATIVO.name());
	}

	public boolean isSituacaoAnulado() {
		return name().equals(SituacaoExpedicaoDiplomaEnum.ANULADO.name());
	}
	
	public boolean isSituacaoAguardandoGeracaoXML() {
		return name().equals(SituacaoExpedicaoDiplomaEnum.AGUARDANDO_GERACAO_XML.name());
	}
	
    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getDescricao() {
        return UteisJSF.internacionalizar(descricao);
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }	

}
