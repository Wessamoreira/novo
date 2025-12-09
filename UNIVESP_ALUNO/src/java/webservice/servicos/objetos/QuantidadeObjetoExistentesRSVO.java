package webservice.servicos.objetos;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * @author Victor Hugo de Paula Costa - 20 de out de 2016
 *
 */
@XmlRootElement(name = "quantidade")
public class QuantidadeObjetoExistentesRSVO {
	/**
	 * @author Victor Hugo de Paula Costa - 20 de out de 2016
	 */
	private Integer valor;

	@XmlElement(name = "valor")
	public Integer getValor() {
		if (valor == null) {
			valor = 0;
		}
		return valor;
	}

	public void setValor(Integer valor) {
		this.valor = valor;
	}
}
