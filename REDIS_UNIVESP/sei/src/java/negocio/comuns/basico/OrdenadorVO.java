/**
 * 
 */
package negocio.comuns.basico;

import java.io.Serializable;

import negocio.comuns.arquitetura.SuperVO;


/**
 * @author Carlos Eugênio
 *
 */
public class OrdenadorVO extends SuperVO implements Serializable {

	private static final long serialVersionUID = 1L;
	private String descricao;
	private String campoOrdenar;
	private Integer ordem;
	private Boolean utilizar;

	public OrdenadorVO() {
		
	}

	public String getCampoOrdenar() {
		if (campoOrdenar == null) {
			campoOrdenar = "";
		}
		return campoOrdenar;
	}

	public void setCampoOrdenar(String campoOrdenar) {
		this.campoOrdenar = campoOrdenar;
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

	public Boolean getUtilizar() {
		if (utilizar == null) {
			utilizar = true;
		}
		return utilizar;
	}

	public void setUtilizar(Boolean utilizar) {
		this.utilizar = utilizar;
	}

	public String getDescricao() {
		if (descricao == null) {
			descricao = "";
		}
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
}
