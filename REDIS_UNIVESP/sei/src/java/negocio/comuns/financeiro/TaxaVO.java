package negocio.comuns.financeiro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;

public class TaxaVO extends SuperVO implements Serializable {
	private Integer codigo;
	private String descricao;
	private String situacao;
	private List<TaxaValorVO> taxaValorVOs;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
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

	public String getSituacao() {
		if (situacao == null) {
			situacao = "";
		}
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public List<TaxaValorVO> getTaxaValorVOs() {
		if (taxaValorVOs == null) {
			taxaValorVOs = new ArrayList<TaxaValorVO>();
		}
		return taxaValorVOs;
	}

	public void setTaxaValorVOs(List<TaxaValorVO> taxaValorVOs) {
		this.taxaValorVOs = taxaValorVOs;
	}

}
