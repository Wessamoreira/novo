package webservice.servicos;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "processoMatricula")
public class ProcessoMatriculaRSVO {

	private Integer codigo;
	private String nome;
	private Boolean permiteAlunoIncluirExcluirDisciplina;

	@XmlElement(name = "codigo")
	public Integer getCodigo() {
		if(codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	@XmlElement(name = "nome")
	public String getNome() {
		if(nome == null) {
			nome = "";
		}
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	

	public Boolean getPermiteAlunoIncluirExcluirDisciplina() {
		if(permiteAlunoIncluirExcluirDisciplina == null ) {
			permiteAlunoIncluirExcluirDisciplina = Boolean.FALSE;
		}
		return permiteAlunoIncluirExcluirDisciplina;
	}

	public void setPermiteAlunoIncluirExcluirDisciplina(Boolean permiteAlunoIncluirExcluirDisciplina) {
		this.permiteAlunoIncluirExcluirDisciplina = permiteAlunoIncluirExcluirDisciplina;
	}
}
