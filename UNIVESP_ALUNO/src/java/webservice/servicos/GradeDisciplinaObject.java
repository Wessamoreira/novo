package webservice.servicos;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "gradeDisciplina")
public class GradeDisciplinaObject {

	private Integer codigo;
	private List<DisciplinaObject> disciplinaObjects;

	@XmlElement(name = "codigo")
	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	@XmlElement(name = "disciplinas")
	public List<DisciplinaObject> getDisciplinaObjects() {
		if (disciplinaObjects == null) {
			disciplinaObjects = new ArrayList<DisciplinaObject>();
		}
		return disciplinaObjects;
	}

	public void setDisciplinaObjects(List<DisciplinaObject> disciplinaObjects) {
		this.disciplinaObjects = disciplinaObjects;
	}
}
