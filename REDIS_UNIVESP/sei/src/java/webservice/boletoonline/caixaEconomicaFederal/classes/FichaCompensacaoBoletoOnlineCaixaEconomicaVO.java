package webservice.boletoonline.caixaEconomicaFederal.classes;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class FichaCompensacaoBoletoOnlineCaixaEconomicaVO {
	
	@XStreamAlias("MENSAGENS")
	private List<String> mensagens ;
	
	public FichaCompensacaoBoletoOnlineCaixaEconomicaVO() {
	
	}

	public List<String> getMensagens() {
		return mensagens;
	}

	public void setMensagens(List<String> mensagens) {
		this.mensagens = mensagens;
	}

	

	
    
}
