package webservice.servicos;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "aluno")
public class AlunoAutoAtendimentoRSVO {
	
	private String aluno;
	private String curso;
	private String turno;
	private String cpf;
	private String celular;
	private String telefoneResidencial;
	private String telefoneRecado;
	private String endereco;
	private String numero;
	private String setor;
	private String cep;
	private String cidade;
	private String estado;
	private String complemento;
	private String email;
	private String matricula;
	
	public String getAluno() {
		if(aluno == null){
			aluno = "";
		}
		return aluno;
	}
	public void setAluno(String aluno) {
		this.aluno = aluno;
	}
	public String getCurso() {
		if(curso == null){
			curso = "";
		}
		return curso;
	}
	public void setCurso(String curso) {
		this.curso = curso;
	}
	public String getTurno() {
		if(turno == null){
			turno = "";
		}
		return turno;
	}
	public void setTurno(String turno) {
		this.turno = turno;
	}
	public String getCpf() {
		if(cpf == null){
			cpf = "";
		}
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	public String getCelular() {
		if(celular == null){
			celular = "";
		}
		return celular;
	}
	public void setCelular(String celular) {
		this.celular = celular;
	}
	public String getTelefoneResidencial() {
		if(telefoneResidencial == null){
			telefoneResidencial = "";
		}
		return telefoneResidencial;
	}
	public void setTelefoneResidencial(String telefoneResidencial) {
		this.telefoneResidencial = telefoneResidencial;
	}
	public String getTelefoneRecado() {
		if(telefoneRecado == null){
			telefoneRecado = "";
		}
		return telefoneRecado;
	}
	public void setTelefoneRecado(String telefoneRecado) {
		this.telefoneRecado = telefoneRecado;
	}
	public String getEndereco() {
		if(endereco == null){
			endereco = "";
		}
		return endereco;
	}
	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
	public String getEmail() {
		if(email == null){
			email = "";
		}
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMatricula() {
		if(matricula == null){
			matricula = "";
		}
		return matricula;
	}
	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}
	public String getNumero() {
		if(numero == null){
			numero = "";
		}
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getSetor() {
		if(setor == null){
			setor = "";
		}
		return setor;
	}
	public void setSetor(String setor) {
		this.setor = setor;
	}
	public String getCep() {
		if(cep == null){
			cep = "";
		}
		return cep;
	}
	public void setCep(String cep) {
		this.cep = cep;
	}
	public String getCidade() {
		if(cidade == null){
			cidade = "";
		}
		return cidade;
	}
	public void setCidade(String cidade) {
		this.cidade = cidade;
	}
	public String getEstado() {
		if(estado == null){
			estado = "";
		}
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getComplemento() {
		if(complemento == null){
			complemento = "";
		}
		return complemento;
	}
	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}
	
	

}
