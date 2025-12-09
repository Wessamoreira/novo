package negocio.comuns.patrimonio.enumeradores;

import negocio.comuns.basico.enumeradores.EntidadeTextoPadraoEnum;
import negocio.comuns.basico.enumeradores.TipoCampoTagTextoPadraoEnum;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.interfaces.basico.TagTextoPadraoInterfaceEnum;

public enum TextoPadraoPatrimonioTagLocalArmazenamentoEnum implements TagTextoPadraoInterfaceEnum {

	LOCAL_NOME_ARMAZENAMENTO(EntidadeTextoPadraoEnum.PATRIMONIO_UNIDADE, "localArmazenamento.localArmazenamento", "[(30){"+UteisJSF.internacionalizar("prt_LocalArmazenamento_localArmazenamento")+"}LOCAL_NOME_ARMAZENAMENTO]", UteisJSF.internacionalizar("prt_LocalArmazenamento_localArmazenamento"), TipoCampoTagTextoPadraoEnum.STRING, null),
	LOCAL_NOME_ARMAZENAMENTO_SUPERIOR(EntidadeTextoPadraoEnum.PATRIMONIO_UNIDADE, "localArmazenamentoSuperior.localArmazenamento", "[(30){"+UteisJSF.internacionalizar("prt_LocalArmazenamento_localArmazenamentoSuperior")+"}LOCAL_NOME_ARMAZENAMENTO_SUPERIOR]", UteisJSF.internacionalizar("prt_LocalArmazenamento_localArmazenamentoSuperior"), TipoCampoTagTextoPadraoEnum.STRING, null),
	LOCAL_NOME_UNIDADE_ENSINO(EntidadeTextoPadraoEnum.PATRIMONIO_UNIDADE, "unidadeEnsinoLocalArmazenamento.nome", "[(30){"+UteisJSF.internacionalizar("prt_LocalArmazenamento_unidadeEnsino")+"}LOCAL_NOME_UNIDADE_ENSINO]", UteisJSF.internacionalizar("prt_LocalArmazenamento_unidadeEnsino"), TipoCampoTagTextoPadraoEnum.STRING, null);
	
	private TextoPadraoPatrimonioTagLocalArmazenamentoEnum(EntidadeTextoPadraoEnum entidade, String campo, String tag, String value,  TipoCampoTagTextoPadraoEnum tipoCampo, Enum<? extends TagTextoPadraoInterfaceEnum>[] subTags) {
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
