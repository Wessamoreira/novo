package negocio.comuns.patrimonio.enumeradores;

import negocio.comuns.basico.enumeradores.EntidadeTextoPadraoEnum;
import negocio.comuns.basico.enumeradores.TipoCampoTagTextoPadraoEnum;
import negocio.interfaces.basico.TagTextoPadraoInterfaceEnum;

public enum TextoPadraoPatrimonioTagAdicionalEnum implements TagTextoPadraoInterfaceEnum {

	DATA_ATUAL(EntidadeTextoPadraoEnum.TAG_ADICIONAIS, "dataAtual", "[(10){}DATA_ATUAL]", "Data Atual", TipoCampoTagTextoPadraoEnum.STRING, null),
	DATA_EXTENSO_ATUAL(EntidadeTextoPadraoEnum.TAG_ADICIONAIS, "dataAtualExtenso", "[(40){}DATA_EXTENSO_ATUAL]", "Data Atual Por Extenso", TipoCampoTagTextoPadraoEnum.STRING, null),
	NOME_USUARIO(EntidadeTextoPadraoEnum.TAG_ADICIONAIS, "nomeUsuario", "[(50){}NOME_USUARIO]", "Nome Usuário", TipoCampoTagTextoPadraoEnum.STRING, null),
	;
	
	private TextoPadraoPatrimonioTagAdicionalEnum(EntidadeTextoPadraoEnum entidade, String campo, String tag, String value,  TipoCampoTagTextoPadraoEnum tipoCampo, Enum<? extends TagTextoPadraoInterfaceEnum>[] subTags) {
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
