package webservice.servicos;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "curso")
public class CursoObject {

	private Integer codigo;
	private String nome;
	private GradeDisciplinaObject gradeDisciplinaObject;

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
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@XmlElement(name = "gradeDisciplina")
	public GradeDisciplinaObject getGradeDisciplinaObject() {
		if(gradeDisciplinaObject == null) {
			gradeDisciplinaObject = new GradeDisciplinaObject();
		}
		return gradeDisciplinaObject;
	}

	public void setGradeDisciplinaObject(GradeDisciplinaObject gradeDisciplinaObject) {
		this.gradeDisciplinaObject = gradeDisciplinaObject;
	}
}
