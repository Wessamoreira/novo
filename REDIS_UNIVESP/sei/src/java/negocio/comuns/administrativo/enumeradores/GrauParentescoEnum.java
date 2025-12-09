package negocio.comuns.administrativo.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum GrauParentescoEnum {
    
	FILHO_VALIDO("FILHO_VALIDO", "FilhoValido"),
	FILHO_INVALIDO("FILHO_INVALIDO", "FilhoInvalido"),
	CONJUGE("CONJUGE", "Conjuge"),
	SOGRO("SOGRO", "Sogro"),
	OUTROS("OUTROS", "Outros"),
	AVO("AVO", "Avo"),
	COMPANHEIRO("COMPANHEIRO", "Companheiro"),
	ENTEADO("ENTEADO", "Enteado"),
	EX_CONJUGE("EX_CONJUGE", "ExConjuge"),
	IRMA_VALIDA("IRMA_VALIDA", "IrmaValida"),
	IRMA_INVALIDA("IRMA_INVALIDA", "IrmaInvalida"),
	EX_COMPANHEIRO("EX_COMPANHEIRO", "ExCompanheiro"),
	EX_SOGRO("EX_SOGRO", "ExSogro"),
	NETO("NETO", "Neto"),
	EX_ENTEADO("EX_ENTEADO", "ExEnteado"),
	PAI("PAI", "Pai"),
	MAE("MAE", "Mãe"),
	EXCLUIDO("EXCLUIDO", "Excluído");

	String valor;
    String descricao;

    public String getValorApresentar(){
        return UteisJSF.internacionalizar("enum_GrauParentescoEnum_"+this.name());
    }
    
	private GrauParentescoEnum(String valor, String descricao) {
		this.valor = valor;
		this.descricao = descricao;
	}
	
	public String getValor() {
		return valor;
	}
	
	public void setValor(String valor) {
		this.valor = valor;
	}
	
	public String getDescricao() {
		return UteisJSF.internacionalizar("enum_GrauParentescoEnum_"+this.descricao);
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
}