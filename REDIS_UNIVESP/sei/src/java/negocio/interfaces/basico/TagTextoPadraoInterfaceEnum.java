package negocio.interfaces.basico;

import negocio.comuns.basico.enumeradores.EntidadeTextoPadraoEnum;
import negocio.comuns.basico.enumeradores.TipoCampoTagTextoPadraoEnum;

public interface TagTextoPadraoInterfaceEnum {

	public String getTag();
	public void setTag(String tag);
	
	public void setValue(String value);
	public String getValue();
	
	public void setCampo(String campo);
	public String getCampo();
	
	public void setEntidade(EntidadeTextoPadraoEnum entidade);
	public EntidadeTextoPadraoEnum getEntidade();
	
	public void setSubTags(Enum<? extends TagTextoPadraoInterfaceEnum>[] subTags);
	public Enum<? extends TagTextoPadraoInterfaceEnum>[] getSubTags();
	
	public TipoCampoTagTextoPadraoEnum getTipoCampo();
	public void setTipoCampo(TipoCampoTagTextoPadraoEnum tipoCampo);
	
}
