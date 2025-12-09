/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.negocio.comuns.financeiro;

/**
 * 
 * @author Ana Claudia
 */
public class ImpostosRetidosContaReceberRelVO {

	private String cidade;
	private String estado;
	private String imposto;
	private Double valor;
	private String parceiro;

	public ImpostosRetidosContaReceberRelVO() {
		setCidade("");
		setEstado("");
		setImposto("");
		setValor(0.0);
		setParceiro("");
	}

	public String getCidade() {
		if (cidade == null) {
			cidade = "";
		}
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getEstado() {
		if (estado == null) {
			estado = "";
		}
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getImposto() {
		if (imposto == null) {
			imposto = "";
		}
		return imposto;
	}

	public void setImposto(String imposto) {
		this.imposto = imposto;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public String getParceiro() {
		if (parceiro == null) {
			parceiro = "";
		}
		return parceiro;
	}

	public void setParceiro(String parceiro) {
		this.parceiro = parceiro;
	}

}