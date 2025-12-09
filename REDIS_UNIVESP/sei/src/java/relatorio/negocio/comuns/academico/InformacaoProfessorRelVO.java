package relatorio.negocio.comuns.academico;

import java.util.List;

import java.util.ArrayList;

public class InformacaoProfessorRelVO {

	private String email;
	private String disciplinaMinistrada;
	private String turmaDisciplinaMinistrada;
	private String nome;
	private Integer codigoProfessor;
	private String areaConhecimento;
	private List<String> formacaoAcademicaVOs;
	private String celular;
	
	public String getEmail() {
		if (email == null) {
			email = "";
		}
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getDisciplinaMinistrada() {
		if (disciplinaMinistrada == null) {
			disciplinaMinistrada = "";
		}
		return disciplinaMinistrada;
	}
	
	public void setDisciplinaMinistrada(String disciplinaMinistrada) {
		this.disciplinaMinistrada = disciplinaMinistrada;
	}
	
	public String getNome() {
		if (nome == null) {
			nome = "";
		}
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getAreaConhecimento() {
		if (areaConhecimento == null) {
			areaConhecimento = "";
		}
		return areaConhecimento;
	}
	
	public void setAreaConhecimento(String areaConhecimento) {
		this.areaConhecimento = areaConhecimento;
	}
	
	public List<String> getFormacaoAcademicaVOs() {
		if (formacaoAcademicaVOs == null) {
			formacaoAcademicaVOs = new ArrayList<String>(0);
		}
		return formacaoAcademicaVOs;
	}
	
	public void setFormacaoAcademicaVOs(List<String> formacaoAcademicaVOs) {
		this.formacaoAcademicaVOs = formacaoAcademicaVOs;
	}
	
	public String getCelular() {
		if (celular == null) {
			celular = "";
		}
		return celular;
	}
	
	public void setCelular(String celular) {
		this.celular = celular;
	}

	public String getTurmaDisciplinaMinistrada() {
		if (turmaDisciplinaMinistrada == null) {
			turmaDisciplinaMinistrada = "";
		}
		return turmaDisciplinaMinistrada;
	}

	public void setTurmaDisciplinaMinistrada(String turmaDisciplinaMinistrada) {
		this.turmaDisciplinaMinistrada = turmaDisciplinaMinistrada;
	}

	public Integer getCodigoProfessor() {
		if (codigoProfessor == null) {
			codigoProfessor = 0;
		}
		return codigoProfessor;
	}

	public void setCodigoProfessor(Integer codigoProfessor) {
		this.codigoProfessor = codigoProfessor;
	}
	
	
	
}
