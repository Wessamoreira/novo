package webservice.boletoonline.caixaEconomicaFederal.classes;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class ReciboPagadorBoletoOnlineCaixaEconomicaVO {
	
	@XStreamAlias("MENSAGENS")
	private List<String> mensagens ;
	
	public ReciboPagadorBoletoOnlineCaixaEconomicaVO() {
	
	}

	public List<String> getMensagens() {
		return mensagens;
	}

	public void setMensagens(List<String> mensagens) {
		this.mensagens = mensagens;
	}

	
	
    

}
