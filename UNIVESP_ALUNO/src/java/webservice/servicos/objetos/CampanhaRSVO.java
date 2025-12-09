package webservice.servicos.objetos;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;


import webservice.servicos.PessoaObject;

@XmlRootElement(name = "campanha")
public class CampanhaRSVO {
	
	private PessoaObject pessoaObject;
	private Integer codigoUnidadeEnsino;
	private Integer codigoCurso;
	private Integer codigoTurno;
//	private ProspectsVO prospectsVO;
	private String mensagem;
	private String duvida;
//	private TipoCompromissoEnum tipoCompromisso;
	
	
	@XmlElement(name = "pessoa")
	public PessoaObject getPessoaObject() {
		if (pessoaObject == null) {
			pessoaObject = new PessoaObject();
		}
		return pessoaObject;
	}
	public void setPessoaObject(PessoaObject pessoaObject) {
		this.pessoaObject = pessoaObject;
	}
	
	@XmlElement(name = "mensagem")
	public String getMensagem() {
		if(mensagem == null) {
			mensagem = "";
		}
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	
	@XmlElement(name = "codigoUnidadeEnsino")
	public Integer getCodigoUnidadeEnsino() {
		if (codigoUnidadeEnsino == null) {
			codigoUnidadeEnsino = 0;
		}
		return codigoUnidadeEnsino;
	}
	public void setCodigoUnidadeEnsino(Integer codigoUnidadeEnsino) {
		this.codigoUnidadeEnsino = codigoUnidadeEnsino;
	}
	
	@XmlElement(name = "codigoCurso")
	public Integer getCodigoCurso() {
		if (codigoCurso == null) {
			codigoCurso = 0;
		}
		return codigoCurso;
	}
	public void setCodigoCurso(Integer codigoCurso) {
		this.codigoCurso = codigoCurso;
	}
	
	@XmlElement(name = "codigoTurno")
	public Integer getCodigoTurno() {
		if (codigoTurno == null) {
			codigoTurno = 0;
		}
		return codigoTurno;
	}
	public void setCodigoTurno(Integer codigoTurno) {
		this.codigoTurno = codigoTurno;
	}
	
	
	
	@XmlElement(name = "duvida")
	public String getDuvida() {
		if (duvida == null) {
			duvida = "";
		}
		return duvida;
	}
	public void setDuvida(String duvida) {
		this.duvida = duvida;
	}
	
	


}
