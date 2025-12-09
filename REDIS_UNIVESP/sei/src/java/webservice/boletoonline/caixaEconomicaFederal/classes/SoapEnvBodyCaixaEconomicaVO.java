package webservice.boletoonline.caixaEconomicaFederal.classes;

public class SoapEnvBodyCaixaEconomicaVO {
	
	
	private ExternoVO externoVO ;

	public SoapEnvBodyCaixaEconomicaVO() {
		setExternoVO(new ExternoVO());
	}
	
	public void setExternoVO(ExternoVO externo) {
		this.externoVO = externo;
	}
	
	public ExternoVO getExternoVO() {
		return this.externoVO;
	}

}
