package negocio.comuns.contabil.enumeradores;

/*
 * Pedro Andrade
 */
public enum TipoLayoutIntegracaoEnum {
	
	XML, TXT;
	
	public boolean isXml(){
		return name().equals(TipoLayoutIntegracaoEnum.XML.name());
	}
	
	public boolean isTxt(){
		return name().equals(TipoLayoutIntegracaoEnum.TXT.name());
	}

}
