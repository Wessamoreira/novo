package webservice.servicos.objetos.enumeradores;

/**
 * @author Victor Hugo de Paula Costa - 13 de out de 2016
 *
 */
public enum MesesAbreviadosAplicativoEnum {
	/**
	 * @author Victor Hugo de Paula Costa - 13 de out de 2016
	 */
	JAN(1, "JAN"), FEV(2, "FEV"), MAR(3, "MAR"), ABR(4, "ABR"), MAI(5, "MAI"), JUN(6, "JUN"), JUL(7, "JUL"), AGO(8, "AGO"), SET(9, "SET"), OUT(10, "OUT"), NOV(11, "NOV"), DEZ(12, "DEZ");

	private Integer mes;
	private String valor;

	/**
	 * @param mes
	 * @param valor
	 */
	private MesesAbreviadosAplicativoEnum(Integer mes, String valor) {
		this.mes = mes;
		this.valor = valor;
	}
	
	public static MesesAbreviadosAplicativoEnum getEnumValor(Integer mes){
        for(MesesAbreviadosAplicativoEnum mesesAbreviadosAplicativoEnum: values()){
            if(mesesAbreviadosAplicativoEnum.getMes().equals(mes)){
                return mesesAbreviadosAplicativoEnum;
            }
        }
        return null;
    }

	public Integer getMes() {
		if (mes == null) {
			mes = 1;
		}
		return mes;
	}

	public void setMes(Integer mes) {
		this.mes = mes;
	}

	public String getValor() {
		if (valor == null) {
			valor = "";
		}
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}
}
