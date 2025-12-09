package webservice.boletoonline.itau.comuns;

public class SacadorAvalistaVO {

	private String cpf_cnpj_sacador_avalista;
	private String nome_sacador_avalista;
	private String logradouro_sacador_avalista;
	private String bairro_sacador_avalista;
	private String cidade_sacador_avalista;
	private String uf_sacador_avalista;
	private String cep_sacador_avalista;

	public String getCpf_cnpj_sacador_avalista() {
		if (cpf_cnpj_sacador_avalista == null) {
			cpf_cnpj_sacador_avalista = "";
		}
		return cpf_cnpj_sacador_avalista;
	}

	public void setCpf_cnpj_sacador_avalista(String cpf_cnpj_sacador_avalista) {
		this.cpf_cnpj_sacador_avalista = cpf_cnpj_sacador_avalista;
	}

	public String getNome_sacador_avalista() {
		if (nome_sacador_avalista == null) {
			nome_sacador_avalista = "";
		}
		return nome_sacador_avalista;
	}

	public void setNome_sacador_avalista(String nome_sacador_avalista) {
		this.nome_sacador_avalista = nome_sacador_avalista;
	}

	public String getLogradouro_sacador_avalista() {
		if (logradouro_sacador_avalista == null) {
			logradouro_sacador_avalista = "";
		}
		return logradouro_sacador_avalista;
	}

	public void setLogradouro_sacador_avalista(String logradouro_sacador_avalista) {
		this.logradouro_sacador_avalista = logradouro_sacador_avalista;
	}

	public String getBairro_sacador_avalista() {
		if (bairro_sacador_avalista == null) {
			bairro_sacador_avalista = "";
		}
		return bairro_sacador_avalista;
	}

	public void setBairro_sacador_avalista(String bairro_sacador_avalista) {
		this.bairro_sacador_avalista = bairro_sacador_avalista;
	}

	public String getCidade_sacador_avalista() {
		if (cidade_sacador_avalista == null) {
			cidade_sacador_avalista = "";
		}
		return cidade_sacador_avalista;
	}

	public void setCidade_sacador_avalista(String cidade_sacador_avalista) {
		this.cidade_sacador_avalista = cidade_sacador_avalista;
	}

	public String getUf_sacador_avalista() {
		if (uf_sacador_avalista == null) {
			uf_sacador_avalista = "";
		}
		return uf_sacador_avalista;
	}

	public void setUf_sacador_avalista(String uf_sacador_avalista) {
		this.uf_sacador_avalista = uf_sacador_avalista;
	}

	public String getCep_sacador_avalista() {
		if (cep_sacador_avalista == null) {
			cep_sacador_avalista = "";
		}
		return cep_sacador_avalista;
	}

	public void setCep_sacador_avalista(String cep_sacador_avalista) {
		this.cep_sacador_avalista = cep_sacador_avalista;
	}

}
