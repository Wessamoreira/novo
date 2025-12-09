package webservice.nfse.maceio;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class Endereco {

	@XStreamAlias("ns3:Endereco")
	private String Endereco;
	
	@XStreamAlias("ns3:Numero")
	private Integer Numero;
	
	@XStreamAlias("ns3:Complemento")
	private String Complemento;
	
	@XStreamAlias("ns3:Bairro")
	private String Bairro;
	
	@XStreamAlias("ns3:CodigoMunicipio")
	private String CodigoMunicipio;
	
	@XStreamAlias("ns3:Uf")
	private String Uf;
	
	@XStreamAlias("ns3:Cep")
	private String Cep;

	@Override
	public String toString() {
		return "Endereco [Endereco=" + Endereco + ", Numero=" + Numero
				+ ", Complemento=" + Complemento + ", Bairro=" + Bairro
				+ ", CodigoMunicipio=" + CodigoMunicipio + ", Uf=" + Uf
				+ ", Cep=" + Cep + "]";
	}

	public String getEndereco() {
		return Endereco;
	}

	public void setEndereco(String endereco) {
		Endereco = endereco;
	}

	public Integer getNumero() {
		return Numero;
	}

	public void setNumero(Integer numero) {
		Numero = numero;
	}

	public String getComplemento() {
		if(Complemento == null || Complemento.isEmpty()) {
			return null;
		}
		return Complemento;
	}

	public void setComplemento(String complemento) {
		Complemento = complemento;
	}

	public String getBairro() {
		return Bairro;
	}

	public void setBairro(String bairro) {
		Bairro = bairro;
	}

	public String getCodigoMunicipio() {
		return CodigoMunicipio;
	}

	public void setCodigoMunicipio(String codigoMunicipio) {
		CodigoMunicipio = codigoMunicipio;
	}

	public String getUf() {
		return Uf;
	}

	public void setUf(String uf) {
		Uf = uf;
	}

	public String getCep() {
		return Cep;
	}

	public void setCep(String string) {
		Cep = string;
	}
	
}