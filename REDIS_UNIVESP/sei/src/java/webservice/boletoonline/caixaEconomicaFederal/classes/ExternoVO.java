package webservice.boletoonline.caixaEconomicaFederal.classes;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class ExternoVO {
	
	@XStreamAlias("sib:HEADER")
	public SibarVO sibar;
	
	@XStreamAlias("DADOS")
	public DadosBoletoOnlineCaixaEconomicaVO dados;	

	public ExternoVO() {
		setSibar(new SibarVO());
		setDados(new DadosBoletoOnlineCaixaEconomicaVO());
	}

	public void setSibar(SibarVO sibar) {
		this.sibar = sibar;
	}

	public SibarVO getSibar() {
		return this.sibar;
	}

	public DadosBoletoOnlineCaixaEconomicaVO getDados() {
		return dados;
	}

	public void setDados(DadosBoletoOnlineCaixaEconomicaVO dados) {
		this.dados = dados;
	}

	
	
	
}
