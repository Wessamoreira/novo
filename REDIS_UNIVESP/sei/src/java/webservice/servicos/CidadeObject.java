package webservice.servicos;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


import negocio.comuns.basico.EstadoVO;

/**
 * 
 * @author Alessandro
 */
@XmlRootElement(name = "cidade")
public class CidadeObject {

	private Integer codigo;
	private String nome;
	private EstadoVO estado;
	private String cep;
	private Integer codigoInep;
	

	public CidadeObject() {
	}


	@XmlElement(name = "codigo")
	public Integer getCodigo() {
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

	@XmlElement(name = "estado")
	public EstadoVO getEstado() {
		if(estado == null) {
			estado = new EstadoVO();
		}
		return estado;
	}


	public void setEstado(EstadoVO estado) {
		this.estado = estado;
	}

	@XmlElement(name = "cep")
	public String getCep() {
		return cep;
	}


	public void setCep(String cep) {
		this.cep = cep;
	}

	@XmlElement(name = "codigoInep")
	public Integer getCodigoInep() {
		return codigoInep;
	}


	public void setCodigoInep(Integer codigoInep) {
		this.codigoInep = codigoInep;
	}
	
}