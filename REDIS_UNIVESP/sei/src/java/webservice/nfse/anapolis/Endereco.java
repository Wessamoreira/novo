package webservice.nfse.anapolis;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("tc:Endereco")
public class Endereco {

	@XStreamAlias("tc:Endereco")
	private String Endereco;
	
	@XStreamAlias("tc:Numero")
	private Integer Numero;
	
	@XStreamAlias("tc:Complemento")
	private String Complemento;
	
	@XStreamAlias("tc:Bairro")
	private String Bairro;
	
	@XStreamAlias("tc:Cidade")
	private String Cidade;
	
	@XStreamAlias("tc:Estado")
	private String Estado;
	
	@XStreamAlias("tc:Cep")
	private String Cep;

	

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

	public String getCidade() {
		if (Cidade == null) {
			Cidade = "";
		}
		return Cidade;
	}

	public void setCidade(String cidade) {
		Cidade = cidade;
	}

	public String getCep() {
		return Cep;
	}

	public void setCep(String string) {
		Cep = string;
	}

	public String getEstado() {
		if (Estado == null) {
			Estado = "";
		}
		return Estado;
	}

	public void setEstado(String estado) {
		Estado = estado;
	}

	@Override
	public String toString() {
		return "Endereco [Endereco=" + Endereco + ", Numero=" + Numero + ", Complemento=" + Complemento + ", Bairro=" + Bairro + ", Cidade=" + Cidade + ", Estado=" + Estado + ", Cep=" + Cep + "]";
	}
}