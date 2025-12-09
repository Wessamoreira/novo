package webservice.servicos.objetos;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.gson.annotations.SerializedName;

@XmlRootElement(name = "custom_fields")
public class CampoPersonalizadoLeadRSVO {
	
	@SerializedName("unidade_ensino")
	private String unidadeEnsino;
	
	@SerializedName("curso_interesse")
	private String cursoInteresse;
	
	@XmlElement(name = "unidade_ensino")
	public String getUnidadeEnsino() {
		if(unidadeEnsino == null)
			unidadeEnsino = "";
		
		return unidadeEnsino;
	}
	public void setUnidadeEnsino(String unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}
	
	@XmlElement(name = "curso_interesse")
	public String getCursoInteresse() {
		if(cursoInteresse == null)
			cursoInteresse = "";
		
		return cursoInteresse;
	}
	public void setCursoInteresse(String cursoInteresse) {
		this.cursoInteresse = cursoInteresse;
	}
	
}