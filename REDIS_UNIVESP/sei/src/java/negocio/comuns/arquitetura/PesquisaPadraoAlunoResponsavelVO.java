package negocio.comuns.arquitetura;

import java.io.Serializable;

public class PesquisaPadraoAlunoResponsavelVO implements Serializable {

	private static final long serialVersionUID = 1L;
	private String nome;
    private String cpf;
    private String email;
    private String tipo;
    
	public String getNome() {
		if (nome == null) {
			nome = "";
		}
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getCpf() {
		if (cpf == null) {
			cpf = "";
		}
		return cpf;
	}
	
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	
	public String getEmail() {
		if (email == null) {
			email = "";
		}
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}

	public String getTipo() {
		if (tipo == null) {
			tipo = "";
		}
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
    
}