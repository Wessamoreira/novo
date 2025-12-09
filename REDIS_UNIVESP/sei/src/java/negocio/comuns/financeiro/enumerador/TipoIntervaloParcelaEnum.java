package negocio.comuns.financeiro.enumerador;


/**
 * 
 * @author Pedro
 *
 */
public enum TipoIntervaloParcelaEnum {
	DATA_BASE, ENTRE_DIAS;

	public boolean isIntervaloDataBase() {
		return this.name().equals(TipoIntervaloParcelaEnum.DATA_BASE.name());
	}
	
	public boolean isIntervaloEntreDias() {
		return this.name().equals(TipoIntervaloParcelaEnum.ENTRE_DIAS.name());
	}

}
