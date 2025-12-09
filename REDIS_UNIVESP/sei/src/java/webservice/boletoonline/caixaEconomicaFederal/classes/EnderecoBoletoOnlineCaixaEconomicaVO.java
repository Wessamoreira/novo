package webservice.boletoonline.caixaEconomicaFederal.classes;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class EnderecoBoletoOnlineCaixaEconomicaVO {
	
	@XStreamAlias("LOGADOURO")
	private String logadouro ;
	
	@XStreamAlias("BAIRRO")
	private String bairro;
	
	@XStreamAlias("CIDADE")
	private String cidade;
	
	@XStreamAlias("UF")
	private String uf;
	
	@XStreamAlias("CEP")
	private String cep;
	
	public EnderecoBoletoOnlineCaixaEconomicaVO() {
		
	}

	
	public String getLogadouro() {
		return logadouro;
	}

	public void setLogadouro(String logadouro) {
		this.logadouro = logadouro;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

}
