package webservice.servicos.objetos;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "disciplina")
public class DisciplinaRSVO {
	
   private Integer codigo; 
   private String  nome ;
   private Boolean estudar;
   
   
   
   
	public DisciplinaRSVO() {
	
    }


	public DisciplinaRSVO(Integer codigo, String nome, Boolean estudar) {	
		this.codigo = codigo;
		this.nome = nome;
		this.estudar = estudar;
   }
	
	
	@XmlElement(name = "codigo")
	public Integer getCodigo() {
		if(codigo == null ) {
			codigo = 0;
		}
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	
	@XmlElement(name = "nome")
	public String getNome() {
		if(nome == null ) {
			nome = "";
		}
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	@XmlElement(name = "estudar")
	public Boolean getEstudar() {
		if(estudar == null ) {
			estudar = Boolean.TRUE;
		}
		return estudar;
	}
	public void setEstudar(Boolean estudar) {
		this.estudar = estudar;
	}
	   
	   
   

}
