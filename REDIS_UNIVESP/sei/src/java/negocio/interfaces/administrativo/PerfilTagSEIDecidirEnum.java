package negocio.interfaces.administrativo;

import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;

public interface PerfilTagSEIDecidirEnum {
		
	public String getEntidade();
	public void setEntidade(String entidade);
	public void setCampo(String campo);
	public String getCampo();
	public void setAtributo(String campo);
	public String getAtributo();
	public String getTag();	
	
	public TipoCampoEnum getTipoCampo();
	public void setTipoCampo(TipoCampoEnum tipoCampo);
	public Integer getTamanhoCampo();
	public void setTamanhoCampo(Integer tamanhoCampo);
	
}
