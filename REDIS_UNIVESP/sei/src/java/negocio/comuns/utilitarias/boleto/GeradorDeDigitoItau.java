package negocio.comuns.utilitarias.boleto;

public class GeradorDeDigitoItau extends GeradorDeDigitoPadrao{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4812285718335841296L;
	
	public String calculaDVNossoNumero(String nossoNumero) {
		//4403003680710919010717
		if (nossoNumero == null || nossoNumero.length() > 22) {
			throw new IllegalArgumentException("Valores para o calculo do digito verificador Nosso Número inválido: " + nossoNumero);
		}
		return String.valueOf(super.geraDigitoMod10(nossoNumero));
	}

}
