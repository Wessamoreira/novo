package webservice.nfse.araguaina;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class Endereco {

	@XStreamAlias("Endereco")
	private String Endereco;
	
	@XStreamAlias("Numero")
	private Integer Numero;
	
	@XStreamAlias("Complemento")
	private String Complemento;
	
	@XStreamAlias("Bairro")
	private String Bairro;
	
	@XStreamAlias("CodigoMunicipio")
	private String CodigoMunicipio;
	
	@XStreamAlias("Uf")
	private String Uf;
	
	@XStreamAlias("Cep")
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