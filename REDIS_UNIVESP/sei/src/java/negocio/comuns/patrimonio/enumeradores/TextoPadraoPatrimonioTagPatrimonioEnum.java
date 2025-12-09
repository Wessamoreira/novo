package negocio.comuns.patrimonio.enumeradores;

import negocio.comuns.basico.enumeradores.EntidadeTextoPadraoEnum;
import negocio.comuns.basico.enumeradores.TipoCampoTagTextoPadraoEnum;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.interfaces.basico.TagTextoPadraoInterfaceEnum;

public enum TextoPadraoPatrimonioTagPatrimonioEnum implements TagTextoPadraoInterfaceEnum {
	
	PATRIMONIO_CODIGO(EntidadeTextoPadraoEnum.PATRIMONIO, "codigo", "[(40){}PATRIMONIO_CODIGO]", "Código Patrimônio", TipoCampoTagTextoPadraoEnum.INTEGER, null), 
	PATRIMONIO_DESCRICAO(EntidadeTextoPadraoEnum.PATRIMONIO, "descricao", "[(50){}PATRIMONIO_DESCRICAO]", UteisJSF.internacionalizar("prt_Patrimonio_descricao"),  TipoCampoTagTextoPadraoEnum.STRING, null), 
	PATRIMONIO_MARCA(EntidadeTextoPadraoEnum.PATRIMONIO,"marca","[(50){}PATRIMONIO_MARCA]", UteisJSF.internacionalizar("prt_Patrimonio_marca"), TipoCampoTagTextoPadraoEnum.STRING, null), 
	PATRIMONIO_MODELO(EntidadeTextoPadraoEnum.PATRIMONIO, "modelo","[(50){}PATRIMONIO_MODELO]", UteisJSF.internacionalizar("prt_Patrimonio_modelo"), TipoCampoTagTextoPadraoEnum.STRING, null),
	PATRIMONIO_DATA_ENTRADA(EntidadeTextoPadraoEnum.PATRIMONIO, "dataEntrada", "[(50){}PATRIMONIO_DATA_ENTRADA]", UteisJSF.internacionalizar("prt_Patrimonio_dataEntrada"), TipoCampoTagTextoPadraoEnum.DATA, null),
	PATRIMONIO_FORNECEDOR(EntidadeTextoPadraoEnum.PATRIMONIO, "fornecedorPatrimonio.nome", "[(50){}PATRIMONIO_FORNECEDOR]", UteisJSF.internacionalizar("prt_Patrimonio_fornecedor"), TipoCampoTagTextoPadraoEnum.STRING, null),
	PATRIMONIO_TIPO_PATRIMONIO(EntidadeTextoPadraoEnum.PATRIMONIO, "tipoPatrimonio.descricao", "[(50){}PATRIMONIO_TIPO_PATRIMONIO]", UteisJSF.internacionalizar("prt_Patrimonio_tipoPatrimonio"), TipoCampoTagTextoPadraoEnum.STRING, null),
//	PATRIMONIO_FORMA_ENTRADA(EntidadeTextoPadraoEnum.PATRIMONIO, "formaEntradaPatrimonio", "[(50){}PATRIMONIO_FORMA_ENTRADA]", UteisJSF.internacionalizar("prt_Patrimonio_formaEntrada"), TipoCampoTagTextoPadraoEnum.STRING, null),
	PATRIMONIO_NOTA_FISCAL(EntidadeTextoPadraoEnum.PATRIMONIO, "notaFiscal", "[(50){}PATRIMONIO_NOTA_FISCAL]", UteisJSF.internacionalizar("prt_Patrimonio_notaFiscal"), TipoCampoTagTextoPadraoEnum.STRING, null),
	PATRIMONIO_UNIDADE(EntidadeTextoPadraoEnum.PATRIMONIO,"patrimonioUnidadeVOs", "[(50){}PATRIMONIO_LISTA|"+"(10){"+UteisJSF.internacionalizar("prt_Patrimonio_CodigoDeBarra")+"}PATRIMONIOUNIDADE_CODIGO_BARRA,"+"(10){"+UteisJSF.internacionalizar("prt_LocalArmazenamento_localArmazenamento")+"}PATRIMONIOUNIDADE_LOCAL_ARMAZENAMENTO|]", "Lista Patrimônio Unidade", TipoCampoTagTextoPadraoEnum.LISTA, TextoPadraoPatrimonioTagPatrimonioUnidadeEnum.values());				
	
	
	private TextoPadraoPatrimonioTagPatrimonioEnum(EntidadeTextoPadraoEnum entidade, String campo, String tag, String value,  TipoCampoTagTextoPadraoEnum tipoCampo, Enum<? extends TagTextoPadraoInterfaceEnum>[] subTags) {
		this.entidade = entidade;
		this.tag = tag;
		this.campo = campo;
		this.value = value;
		this.tipoCampo = tipoCampo;
		this.subTags = subTags;
	}
	
	private EntidadeTextoPadraoEnum entidade;
	private String tag;
	private String value;	
	private String campo;	
	private Enum<? extends TagTextoPadraoInterfaceEnum>[] subTags;
	private TipoCampoTagTextoPadraoEnum tipoCampo;
		
	public String getTag() {
		if (tag == null) {
			tag = "";
		}
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getCampo() {
		if (campo == null) {
			campo = "";
		}
		return campo;
	}
	public void setCampo(String campo) {
		this.campo = campo;
	}
	public String getValue() {
		if (value == null) {
			value = "";
		}
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	
	public Enum<? extends TagTextoPadraoInterfaceEnum>[] getSubTags() {		
		return subTags;
	}
	
	
	public void setSubTags(Enum<? extends TagTextoPadraoInterfaceEnum>[] subTags) {
		this.subTags = subTags;
	}
	
	public TipoCampoTagTextoPadraoEnum getTipoCampo() {		
		return tipoCampo;
	}
	public void setTipoCampo(TipoCampoTagTextoPadraoEnum tipoCampo) {
		this.tipoCampo = tipoCampo;
	}
	public EntidadeTextoPadraoEnum getEntidade() {	
		return entidade;
	}
	public void setEntidade(EntidadeTextoPadraoEnum entidade) {
		this.entidade = entidade;
	}
	
	
	
	
	
}
