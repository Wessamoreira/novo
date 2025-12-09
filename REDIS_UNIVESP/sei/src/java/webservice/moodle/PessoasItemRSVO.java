package webservice.moodle;

import com.google.gson.annotations.SerializedName;

public class PessoasItemRSVO {
	
	@SerializedName("cpf")
	private String cpf;
	@SerializedName("nome")
	private String nome;
	@SerializedName("codigo")
	private String codigo;
	@SerializedName("email")
	private String email;
	@SerializedName("senha")
	private String senha;
	
	public String getCpf() {
		if (cpf == null)
			cpf = "";
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	
	public String getNome() {
		if(nome == null)
			nome = "";
		
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getCodigo() {
		if(codigo == null)
			codigo = "";
		
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	public String getEmail() {
		if(email == null)
			email = "";
		
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getSenha() {
		if (senha == null)
			senha = "";
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
}