package negocio.comuns.patrimonio.enumeradores;

import negocio.comuns.basico.enumeradores.EntidadeTextoPadraoEnum;
import negocio.comuns.basico.enumeradores.TipoCampoTagTextoPadraoEnum;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.interfaces.basico.TagTextoPadraoInterfaceEnum;

public enum TextoPadraoPatrimonioTagPatrimonioUnidadeEnum implements TagTextoPadraoInterfaceEnum {

	PATRIMONIOUNIDADE_CODIGO_BARRA(EntidadeTextoPadraoEnum.PATRIMONIO_UNIDADE, "codigoBarra", "[(10){"+UteisJSF.internacionalizar("prt_Patrimonio_CodigoDeBarra")+"}PATRIMONIOUNIDADE_CODIGO_BARRA]", UteisJSF.internacionalizar("prt_Patrimonio_CodigoDeBarra"), TipoCampoTagTextoPadraoEnum.STRING, null),
	PATRIMONIOUNIDADE_NUMERO_SERIE(EntidadeTextoPadraoEnum.PATRIMONIO_UNIDADE, "numeroDeSerie", "[(20){"+UteisJSF.internacionalizar("prt_Patrimonio_NrDeSerie")+"}PATRIMONIOUNIDADE_NUMERO_SERIE]", UteisJSF.internacionalizar("prt_Patrimonio_NrDeSerie"), TipoCampoTagTextoPadraoEnum.STRING, null),
	PATRIMONIOUNIDADE_SITUACAO(EntidadeTextoPadraoEnum.PATRIMONIO_UNIDADE, "situacaoPatrimonioUnidade" , "[(20){"+UteisJSF.internacionalizar("prt_Patrimonio_situacao")+"}PATRIMONIOUNIDADE_SITUACAO]", UteisJSF.internacionalizar("prt_Patrimonio_situacao"), TipoCampoTagTextoPadraoEnum.STRING, null),
	PATRIMONIOUNIDADE_VALOR(EntidadeTextoPadraoEnum.PATRIMONIO_UNIDADE, "valorRecurso", "[(12){"+UteisJSF.internacionalizar("prt_Patrimonio_ValorRecurso")+"}PATRIMONIOUNIDADE_VALOR]", UteisJSF.internacionalizar("prt_Patrimonio_ValorRecurso"), TipoCampoTagTextoPadraoEnum.DOUBLE, null),
	PATRIMONIOUNIDADE_PERMITE_RESERVA(EntidadeTextoPadraoEnum.PATRIMONIO_UNIDADE, "permiteReserva", "[(10){"+UteisJSF.internacionalizar("prt_Patrimonio_PermiteReserva")+"}PATRIMONIOUNIDADE_PERMITE_RESERVA]", UteisJSF.internacionalizar("prt_Patrimonio_PermiteReserva"), TipoCampoTagTextoPadraoEnum.BOOLEAN, null),
	PATRIMONIOUNIDADE_PERMITE_LOCACAO(EntidadeTextoPadraoEnum.PATRIMONIO_UNIDADE, "unidadeLocado", "[(10){"+UteisJSF.internacionalizar("prt_Patrimonio_UnidadeLocado")+"}PATRIMONIOUNIDADE_PERMITE_LOCACAO]", UteisJSF.internacionalizar("prt_Patrimonio_UnidadeLocado"), TipoCampoTagTextoPadraoEnum.BOOLEAN, null),
	PATRIMONIOUNIDADE_LOCAL_ARMAZENAMENTO(EntidadeTextoPadraoEnum.PATRIMONIO_UNIDADE, "localArmazenamento.localArmazenamento", "[(30){"+UteisJSF.internacionalizar("prt_LocalArmazenamento_localArmazenamento")+"}PATRIMONIOUNIDADE_LOCAL_ARMAZENAMENTO]", UteisJSF.internacionalizar("prt_LocalArmazenamento_localArmazenamento"), TipoCampoTagTextoPadraoEnum.STRING, null),
	PATRIMONIOUNIDADE_LOCAL_ARMAZENAMENTO_SUPERIOR(EntidadeTextoPadraoEnum.PATRIMONIO_UNIDADE, "localArmazenamentoSuperior.localArmazenamento", "[(30){"+UteisJSF.internacionalizar("prt_LocalArmazenamento_localArmazenamentoSuperior")+"}PATRIMONIOUNIDADE_LOCAL_ARMAZENAMENTO_SUPERIOR]", UteisJSF.internacionalizar("prt_LocalArmazenamento_localArmazenamentoSuperior"), TipoCampoTagTextoPadraoEnum.STRING, null),
	PATRIMONIOUNIDADE_UNIDADE_ENSINO(EntidadeTextoPadraoEnum.PATRIMONIO_UNIDADE, "unidadeEnsinoLocalArmazenamento.nome", "[(30){"+UteisJSF.internacionalizar("prt_LocalArmazenamento_unidadeEnsino")+"}PATRIMONIOUNIDADE_UNIDADE_ENSINO]", UteisJSF.internacionalizar("prt_LocalArmazenamento_unidadeEnsino"), TipoCampoTagTextoPadraoEnum.STRING, null);
	
	private TextoPadraoPatrimonioTagPatrimonioUnidadeEnum(EntidadeTextoPadraoEnum entidade, String campo, String tag, String value,  TipoCampoTagTextoPadraoEnum tipoCampo, Enum<? extends TagTextoPadraoInterfaceEnum>[] subTags) {
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
