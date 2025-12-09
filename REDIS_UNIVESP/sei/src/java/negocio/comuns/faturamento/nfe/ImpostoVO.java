package negocio.comuns.faturamento.nfe;

import negocio.comuns.arquitetura.SuperVO;

/**
 *
 * @author Pedro
 */
public class ImpostoVO extends SuperVO {

	private static final long serialVersionUID = 1817578134621184175L;

	private Integer codigo;
	private String nome;
	private Boolean utilizarFolhaPagamento;
	private Boolean filtrarImposto;
	private Double valorTotalImposto;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		if (nome == null) {
			nome = "";
		}
		return nome;
	} 

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Boolean getFiltrarImposto() {
		if (filtrarImposto == null) {
			filtrarImposto = Boolean.FALSE;
		}
		return filtrarImposto;
	}

	public void setFiltrarImposto(Boolean filtrarImposto) {
		this.filtrarImposto = filtrarImposto;
	}

	public Boolean getUtilizarFolhaPagamento() {
		if (utilizarFolhaPagamento == null) {
			utilizarFolhaPagamento = Boolean.FALSE;
		}
		return utilizarFolhaPagamento;
	}

	public void setUtilizarFolhaPagamento(Boolean utilizarFolhaPagamento) {
		this.utilizarFolhaPagamento = utilizarFolhaPagamento;
	}

	public Double getValorTotalImposto() {
		return valorTotalImposto;
	}

	public void setValorTotalImposto(Double valorTotalImposto) {
		this.valorTotalImposto = valorTotalImposto;
	}
	
}
