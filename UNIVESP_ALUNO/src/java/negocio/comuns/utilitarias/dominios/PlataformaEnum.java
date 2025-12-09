package negocio.comuns.utilitarias.dominios;

/**
 * 
 * @author Victor Hugo - 03 de nov de 2016
 * 
 */
public enum PlataformaEnum {

	android("Android"), ios("iOS");

	String value;

	private PlataformaEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
