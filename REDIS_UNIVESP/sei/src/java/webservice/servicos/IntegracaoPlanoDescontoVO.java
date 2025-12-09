package webservice.servicos;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import negocio.comuns.arquitetura.SuperVO;

@XmlRootElement
public class IntegracaoPlanoDescontoVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5372395060405245015L;
	private Integer planoDesconto;
	private String nome;

	 @XmlElement(name = "planoDesconto")
	public Integer getPlanoDesconto() {
		if (planoDesconto == null) {
			planoDesconto = 0;
		}
		return planoDesconto;
	}

	public void setPlanoDesconto(Integer planoDesconto) {
		this.planoDesconto = planoDesconto;
	}

	 @XmlElement(name = "nome")
	public String getNome() {
		if (nome == null) {
			nome = "";
		}
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

}
