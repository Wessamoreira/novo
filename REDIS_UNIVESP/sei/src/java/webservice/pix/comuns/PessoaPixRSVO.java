package webservice.pix.comuns;

public class PessoaPixRSVO  {
	
	/**
	 * maxLength: 200
	 * obrigatorio
	 */
	private String nome;
	/**
	 * maxLength: 11
	 */
	private String cpf;
	/**
	 * maxLength: 14
	 */
	private String cnpj;	
	
	/**
	 * maxLength: 200
	 * obrigatorio
	 */
	private String logradouro;
	/**
	 * maxLength: 200
	 * obrigatorio
	 */
	private String cidade;
	/**
	 * obrigatorio
	 * maxLength: 2
	 */
	private String uf;
	/**
	 * obrigatorio
	 * maxLength: 8
	 */
	private String cep;
	
	private String email;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	

}
