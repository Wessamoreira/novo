package webservice.servicos.objetos;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Victor Hugo de Paula Costa - 10 de out de 2016
 *
 */
@XmlRootElement(name = "perfilAcessoAplicativo")
public class PerfilAcessoAplicativoRSVO {
	/**
	 * @author Victor Hugo de Paula Costa - 10 de out de 2016
	 */
	public String permissao;
	public Boolean valor;

	@XmlElement(name = "permissao")
	public String getPermissao() {
		if (permissao == null) {
			permissao = "";
		}
		return permissao;
	}

	public void setPermissao(String permissao) {
		this.permissao = permissao;
	}

	@XmlElement(name = "valor")
	public Boolean getValor() {
		if (valor == null) {
			valor = false;
		}
		return valor;
	}

	public void setValor(Boolean valor) {
		this.valor = valor;
	}

}
