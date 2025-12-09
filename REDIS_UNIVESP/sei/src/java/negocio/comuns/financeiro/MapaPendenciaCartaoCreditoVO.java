package negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.compras.FormaPagamentoVO;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;

/**
 * Reponsável por manter os dados da entidade MovimentacaoFinanceira. Classe do tipo VO - Value Object composta pelos atributos da entidade com visibilidade protegida e os métodos de acesso a estes atributos. Classe utilizada para apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class MapaPendenciaCartaoCreditoVO extends SuperVO {

	public static final long serialVersionUID = 1L;
	private FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO;
	private FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO formaPagamentoNegociacaoRecebimentoCartaoCreditoVO;
	private Boolean efetuarBaixa;
	/**
	 * transient
	 */
	private ContaCorrenteVO contaCorrenteVO;
	private OperadoraCartaoVO operadoraCartaoVO;
	private FormaPagamentoVO formaPagamentoVO;
	private Double totalPorFormaPagamentoNegociacaoRecebimento;
	private List<FormaPagamentoNegociacaoRecebimentoVO> listaFormaPagamentoNegociacaoRecebimentoVO;

	public MapaPendenciaCartaoCreditoVO() {
		super();
	}
	
	public MapaPendenciaCartaoCreditoVO getClone() {
		try {
			MapaPendenciaCartaoCreditoVO mpcc = (MapaPendenciaCartaoCreditoVO) super.clone();
			mpcc.setNovoObj(true);
			mpcc.setFormaPagamentoNegociacaoRecebimentoVO((FormaPagamentoNegociacaoRecebimentoVO) getFormaPagamentoNegociacaoRecebimentoVO().clone());
			mpcc.setFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO((FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO) getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().clone());
			mpcc.setContaCorrenteVO((ContaCorrenteVO) getContaCorrenteVO().clone());
			mpcc.setOperadoraCartaoVO((OperadoraCartaoVO) getOperadoraCartaoVO().clone());
			mpcc.setFormaPagamentoVO((FormaPagamentoVO) getFormaPagamentoVO().clone());return mpcc;
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	public FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO() {
		if (formaPagamentoNegociacaoRecebimentoCartaoCreditoVO == null) {
			formaPagamentoNegociacaoRecebimentoCartaoCreditoVO = new FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO();
		}
		return formaPagamentoNegociacaoRecebimentoCartaoCreditoVO;
	}

	public void setFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO(FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO formaPagamentoNegociacaoRecebimentoCartaoCreditoVO) {
		this.formaPagamentoNegociacaoRecebimentoCartaoCreditoVO = formaPagamentoNegociacaoRecebimentoCartaoCreditoVO;
	}

	public FormaPagamentoNegociacaoRecebimentoVO getFormaPagamentoNegociacaoRecebimentoVO() {
		if (formaPagamentoNegociacaoRecebimentoVO == null) {
			formaPagamentoNegociacaoRecebimentoVO = new FormaPagamentoNegociacaoRecebimentoVO();
		}
		return formaPagamentoNegociacaoRecebimentoVO;
	}

	public void setFormaPagamentoNegociacaoRecebimentoVO(FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO) {
		this.formaPagamentoNegociacaoRecebimentoVO = formaPagamentoNegociacaoRecebimentoVO;
	}

	public Boolean getEfetuarBaixa() {
		if (efetuarBaixa == null) {
			efetuarBaixa = false;
		}
		return efetuarBaixa;
	}

	public void setEfetuarBaixa(Boolean efetuarBaixa) {
		this.efetuarBaixa = efetuarBaixa;
	}

	public boolean getIsRecebido() {
		if (getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getSituacao().equals("RE")) {
			return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((formaPagamentoNegociacaoRecebimentoCartaoCreditoVO == null) ? 0 : formaPagamentoNegociacaoRecebimentoCartaoCreditoVO.hashCode());
		result = prime * result + ((formaPagamentoNegociacaoRecebimentoVO == null) ? 0 : formaPagamentoNegociacaoRecebimentoVO.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MapaPendenciaCartaoCreditoVO other = (MapaPendenciaCartaoCreditoVO) obj;
		if (formaPagamentoNegociacaoRecebimentoCartaoCreditoVO == null) {
			if (other.formaPagamentoNegociacaoRecebimentoCartaoCreditoVO != null)
				return false;
		} else if (!formaPagamentoNegociacaoRecebimentoCartaoCreditoVO.getCodigo().equals(other.formaPagamentoNegociacaoRecebimentoCartaoCreditoVO.getCodigo()))
			return false;
		if (formaPagamentoNegociacaoRecebimentoVO == null) {
			if (other.formaPagamentoNegociacaoRecebimentoVO != null)
				return false;
		} else if (!formaPagamentoNegociacaoRecebimentoVO.getCodigo().equals(other.formaPagamentoNegociacaoRecebimentoVO.getCodigo()))
			return false;
		return true;
	}

	public Double valorTaxa;
	public Double valorTaxaAntecipacao;
	public Double valorTaxaUsar;
	public Double valorLiquido;
	

	public Double getValorLiquido() {
		return Uteis.arrendondarForcando2CadasDecimais(getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getValorParcela() - getValorTaxaUsar());
	}

	public void setValorLiquido(Double valorLiquido) {
		this.valorLiquido = valorLiquido;
	}

	public Double getValorTaxa() {
		if (valorTaxa == null) {
			valorTaxa = Uteis.arrendondarForcando2CadasDecimais((getFormaPagamentoNegociacaoRecebimentoVO().getTaxaDeOperacao() * getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getValorParcela()) / 100.0);
		}
		return valorTaxa;
	}

	public Double getValorTaxaAntecipacao() {
		if (valorTaxaAntecipacao == null) {
			if (getFormaPagamentoNegociacaoRecebimentoVO().getTaxaDeAntecipacao() > 0.0) {
				valorTaxaAntecipacao = Uteis.arrendondarForcando2CadasDecimais((getFormaPagamentoNegociacaoRecebimentoVO().getTaxaDeAntecipacao() * getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getValorParcela()) / 100.0);
			} else {
				valorTaxaAntecipacao = Uteis.arrendondarForcando2CadasDecimais((getFormaPagamentoNegociacaoRecebimentoVO().getTaxaDeOperacao() * getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getValorParcela()) / 100.0);
			}
		}
		return valorTaxaAntecipacao;
	}

	public Double taxaUsar;

	public Double getTaxaUsar() {
		if (valorTaxaUsar == null) {
			if (getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getDataRecebimento() != null
					&& getFormaPagamentoNegociacaoRecebimentoVO().getTaxaDeAntecipacao() > 0.0
					&& getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getDataVencimento() != null
					&& !Uteis.getData(getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getDataRecebimento()).equals(Uteis.getData(getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getDataVencimento()))
					&& getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getDataRecebimento().compareTo(getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getDataVencimento()) < 0) {
				taxaUsar = getFormaPagamentoNegociacaoRecebimentoVO().getTaxaDeAntecipacao();
			} else {
				taxaUsar = getFormaPagamentoNegociacaoRecebimentoVO().getTaxaDeOperacao();
			}
		}
		return taxaUsar;
	}

	public Double getValorTaxaUsar() {
		try {
			if (getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getDataRecebimento() != null
					&& getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getDataVencimento() != null
					&& UteisData.getCompareData(getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getDataRecebimento(), getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getDataVencimento()) < 0) {
				valorTaxaUsar = getValorTaxaAntecipacao();
			} else {
				valorTaxaUsar = getValorTaxa();
			}
		} catch (Exception e) {
			e.printStackTrace();			
		}
		return Uteis.arrendondarForcandoCadasDecimais(valorTaxaUsar - getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getAjustarValorLiquido(), 2);
	}

	public void setValorTaxaUsar(Double valorTaxaUsar) {
		this.valorTaxaUsar = valorTaxaUsar;
	}

	public void setValorTaxa(Double valorTaxa) {
		this.valorTaxa = valorTaxa;
	}

	public void setValorTaxaAntecipacao(Double valorTaxaAntecipacao) {
		this.valorTaxaAntecipacao = valorTaxaAntecipacao;
	}

	public Double valorTaxaCalculado;

	public Double getValorTaxaCalculado() {
		if (valorTaxaCalculado == null) {
			valorTaxaCalculado = 0.0;
			if (getFormaPagamentoNegociacaoRecebimentoVO().getTaxaDeOperacao() > 0) {
				valorTaxaCalculado = Uteis.arrendondarForcando2CadasDecimais((getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getValorParcela() * getFormaPagamentoNegociacaoRecebimentoVO().getTaxaDeOperacao()) / 100.0);
			}
		}
		return valorTaxaCalculado;
	}

	public void setValorTaxaCalculado(Double valorTaxaCalculado) {
		this.valorTaxaCalculado = valorTaxaCalculado;
	}

	public ContaCorrenteVO getContaCorrenteVO() {
		if (contaCorrenteVO == null) {
			contaCorrenteVO = new ContaCorrenteVO();
		}
		return contaCorrenteVO;
	}

	public void setContaCorrenteVO(ContaCorrenteVO contaCorrenteVO) {
		this.contaCorrenteVO = contaCorrenteVO;
	}

	public OperadoraCartaoVO getOperadoraCartaoVO() {
		if (operadoraCartaoVO == null) {
			operadoraCartaoVO = new OperadoraCartaoVO();
		}
		return operadoraCartaoVO;
	}

	public void setOperadoraCartaoVO(OperadoraCartaoVO operadoraCartaoVO) {
		this.operadoraCartaoVO = operadoraCartaoVO;
	}

	public FormaPagamentoVO getFormaPagamentoVO() {
		if (formaPagamentoVO == null) {
			formaPagamentoVO = new FormaPagamentoVO();
		}
		return formaPagamentoVO;
	}

	public void setFormaPagamentoVO(FormaPagamentoVO formaPagamentoVO) {
		this.formaPagamentoVO = formaPagamentoVO;
	}

	public List<FormaPagamentoNegociacaoRecebimentoVO> getListaFormaPagamentoNegociacaoRecebimentoVO() {
		if (listaFormaPagamentoNegociacaoRecebimentoVO == null) {
			listaFormaPagamentoNegociacaoRecebimentoVO = new ArrayList<FormaPagamentoNegociacaoRecebimentoVO>();
		}
		return listaFormaPagamentoNegociacaoRecebimentoVO;
	}

	public void setListaFormaPagamentoNegociacaoRecebimentoVO(List<FormaPagamentoNegociacaoRecebimentoVO> listaFormaPagamentoNegociacaoRecebimentoVO) {
		this.listaFormaPagamentoNegociacaoRecebimentoVO = listaFormaPagamentoNegociacaoRecebimentoVO;
	}
	
	public void carregarTotalPorContaCorrentePorOperadoraCartao() {
		setTotalPorFormaPagamentoNegociacaoRecebimento(getFormaPagamentoNegociacaoRecebimentoVO().getNegociacaoRecebimentoVO().getFormaPagamentoNegociacaoRecebimentoVOs()
				.stream().filter(p->p.getContaCorrente().getCodigo().equals(getContaCorrenteVO().getCodigo())
						&& p.getFormaPagamento().getCodigo().equals(getFormaPagamentoVO().getCodigo()) 
						&& p.getOperadoraCartaoVO().getCodigo().equals(getOperadoraCartaoVO().getCodigo()))
				.mapToDouble(p-> p.getValorRecebimento()).reduce(0D, (a, b) -> Uteis.arrendondarForcando2CadasDecimais(a + b)));
	}

	public Double getTotalPorFormaPagamentoNegociacaoRecebimento() {
		return totalPorFormaPagamentoNegociacaoRecebimento;
	}

	public void setTotalPorFormaPagamentoNegociacaoRecebimento(Double totalPorFormaPagamentoNegociacaoRecebimento) {
		this.totalPorFormaPagamentoNegociacaoRecebimento = totalPorFormaPagamentoNegociacaoRecebimento;
	}
	
	

}
