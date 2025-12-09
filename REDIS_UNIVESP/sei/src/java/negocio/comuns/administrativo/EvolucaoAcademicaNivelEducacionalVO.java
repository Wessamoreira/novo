/**
 * 
 */
package negocio.comuns.administrativo;

import java.io.Serializable;

import negocio.comuns.arquitetura.SuperVO;

/**
 * @author Carlos Eugênio
 *
 */
public class EvolucaoAcademicaNivelEducacionalVO extends SuperVO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer quantidade;
	private String nivelEducacional;
	private String tipo;
	private Integer ordem;

	public EvolucaoAcademicaNivelEducacionalVO() {
		super();
	}

	public Integer getQuantidade() {
		if (quantidade == null) {
			quantidade = 0;
		}
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public String getNivelEducacional() {
		if (nivelEducacional == null) {
			nivelEducacional = "";
		}
		return nivelEducacional;
	}

	public void setNivelEducacional(String nivelEducacional) {
		this.nivelEducacional = nivelEducacional;
	}

	public String getTipo() {
		if (tipo == null) {
			tipo = "";
		}
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Integer getOrdem() {
		if (ordem == null) {
			ordem = 0;
		}
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}
	
	public String getNivelEducacionalNomeCompleto() {
		if (getNivelEducacional().equals("IN")) {
			return "INFANTIL";
		}
		if (getNivelEducacional().equals("BA")) {
			return "FUNDAMENTAL";
		}
		if (getNivelEducacional().equals("ME")) {
			return "MEDIO";
		}
		if (getNivelEducacional().equals("SU")) {
			return "SUPERIOR";
		}
		if (getNivelEducacional().equals("PO")) {
			return "POS_GRADUACAO";
		}
		if (getNivelEducacional().equals("GT")) {
			return "GRADUACAO_TECNOLOGICA";
		}
		return "";
	}
}
