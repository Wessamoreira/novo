package webservice.servicos;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "periodoLetivo")
public class PeriodoLetivoRSVO {
	
	private Integer codigo;
	private String nome;
	private Integer periodoLetivo;

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
	
	@XmlElement(name = "periodoLetivo")
	public Integer getPeriodoLetivo() {
		if(periodoLetivo == null) {
			periodoLetivo =  1;
		}
		
		return periodoLetivo;
	}

	
	public void setPeriodoLetivo(Integer periodoLetivo) {
		this.periodoLetivo = periodoLetivo;
	}

}
