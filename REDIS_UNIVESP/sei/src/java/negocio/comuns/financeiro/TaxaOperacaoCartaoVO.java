package negocio.comuns.financeiro;

import java.io.Serializable;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.financeiro.enumerador.TipoFinanciamentoEnum;

public class TaxaOperacaoCartaoVO extends SuperVO implements Serializable {
	private Integer codigo;
	private Integer configuracaoFinanceiroCartao;
	private Integer parcela;
	private Double taxa;
	private TipoFinanciamentoEnum tipoFinanciamentoEnum;	
	private String descricao;
	private String situacao;

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

	public Integer getConfiguracaoFinanceiroCartao() {
		if (configuracaoFinanceiroCartao == null) {
			configuracaoFinanceiroCartao = 0;
		}
		return configuracaoFinanceiroCartao;
	}

	public void setConfiguracaoFinanceiroCartao(Integer configuracaoFinanceiroCartao) {
		this.configuracaoFinanceiroCartao = configuracaoFinanceiroCartao;
	}

	public TipoFinanciamentoEnum getTipoFinanciamentoEnum() {
		if (tipoFinanciamentoEnum == null) {
			tipoFinanciamentoEnum = TipoFinanciamentoEnum.OPERADORA;
		}
		return tipoFinanciamentoEnum;
	}

	public void setTipoFinanciamentoEnum(TipoFinanciamentoEnum tipoFinanciamentoEnum) {
		this.tipoFinanciamentoEnum = tipoFinanciamentoEnum;
	}

	public Double getTaxa() {
		if (taxa == null) {
			taxa = 0.0;
		}
		return taxa;
	}

	public void setTaxa(Double taxa) {
		this.taxa = taxa;
	}

	public Integer getParcela() {
		if (parcela == null) {
			parcela = 0;
		}
		return parcela;
	}

	public void setParcela(Integer parcela) {
		this.parcela = parcela;
	}

	
}
