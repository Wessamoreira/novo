package negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.compras.FormaPagamentoVO;
import negocio.comuns.utilitarias.Uteis;

public class MapaPendenciaCartaoCreditoTotalVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2805077661310830717L;
	private OperadoraCartaoVO operadoraCartaoVO;
	private ContaCorrenteVO contaCorrenteVO;
	private Integer quantidade;
	private Double valorPrevisto;
	private Double valor;
	private Double valorTaxa;
	private Double valorLiquido;
	private FormaPagamentoVO formaPagamentoVO;
	private Boolean abaterTaxaExtratoContaCorrente;
	private List<MapaPendenciaCartaoCreditoVO> mapaPendenciaCartaoCreditoVOs;

	public OperadoraCartaoVO getOperadoraCartaoVO() {
		if (operadoraCartaoVO == null) {
			operadoraCartaoVO = new OperadoraCartaoVO();
		}
		return operadoraCartaoVO;
	}

	public void setOperadoraCartaoVO(OperadoraCartaoVO operadoraCartaoVO) {
		this.operadoraCartaoVO = operadoraCartaoVO;
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

	public Integer getQuantidade() {
		if (quantidade == null) {
			quantidade = 0;
		}
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public Double getValor() {
		if (valor == null) {
			valor = 0.0;
		}
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public Double getValorTaxa() {
		if (valorTaxa == null) {
			valorTaxa = 0.0;
		}
		return valorTaxa;
	}

	public void setValorTaxa(Double valorTaxa) {
		this.valorTaxa = valorTaxa;
	}

	public Double getValorLiquido() {
		return Uteis.arrendondarForcando2CadasDecimais(getValor() - getValorTaxa());
	}

	public void setValorLiquido(Double valorLiquido) {
		this.valorLiquido = valorLiquido;
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

	public Boolean getAbaterTaxaExtratoContaCorrente() {
		if (abaterTaxaExtratoContaCorrente == null) {
			abaterTaxaExtratoContaCorrente = true;
		}
		return abaterTaxaExtratoContaCorrente;
	}

	public void setAbaterTaxaExtratoContaCorrente(Boolean abaterTaxaExtratoContaCorrente) {
		this.abaterTaxaExtratoContaCorrente = abaterTaxaExtratoContaCorrente;
	}

	public List<MapaPendenciaCartaoCreditoVO> getMapaPendenciaCartaoCreditoVOs() {
		if (mapaPendenciaCartaoCreditoVOs == null) {
			mapaPendenciaCartaoCreditoVOs = new ArrayList<MapaPendenciaCartaoCreditoVO>(0);
		}
		return mapaPendenciaCartaoCreditoVOs;
	}

	public void setMapaPendenciaCartaoCreditoVOs(List<MapaPendenciaCartaoCreditoVO> mapaPendenciaCartaoCreditoVOs) {
		this.mapaPendenciaCartaoCreditoVOs = mapaPendenciaCartaoCreditoVOs;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((contaCorrenteVO == null) ? 0 : contaCorrenteVO.hashCode());
		result = prime * result + ((operadoraCartaoVO == null) ? 0 : operadoraCartaoVO.hashCode());
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
		MapaPendenciaCartaoCreditoTotalVO other = (MapaPendenciaCartaoCreditoTotalVO) obj;
		if (contaCorrenteVO == null) {
			if (other.contaCorrenteVO != null)
				return false;
		} else if (!contaCorrenteVO.getCodigo().equals(other.contaCorrenteVO.getCodigo()))
			return false;
		if (operadoraCartaoVO == null) {
			if (other.operadoraCartaoVO != null)
				return false;
		} else if (!operadoraCartaoVO.getCodigo().equals(other.operadoraCartaoVO.getCodigo()))
			return false;
		return true;
	}

	public Double getValorPrevisto() {
		if (valorPrevisto == null) {
			valorPrevisto = 0.0;
		}
		return valorPrevisto;
	}

	public void setValorPrevisto(Double valorPrevisto) {
		this.valorPrevisto = valorPrevisto;
	}
	
	/*private Integer quantidade;
	private Double valorPrevisto;
	private Double valor;
	private Double valorTaxa;*/
	
	public Long getQuantidadeResumoOperacao(boolean efetuarBaixaListaMapaCartao){
		if(efetuarBaixaListaMapaCartao){
			return getMapaPendenciaCartaoCreditoVOs().stream().filter(p-> p.getEfetuarBaixa() && p.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getSituacao().equals("AR")).count();	
		}
		return getMapaPendenciaCartaoCreditoVOs().stream().filter(p-> p.getEfetuarBaixa() && p.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getSituacao().equals("RE")).count();
	}
	
	
	
	public Double getValorResumoOperacao(boolean efetuarBaixaListaMapaCartao){
		if(efetuarBaixaListaMapaCartao){
			return getMapaPendenciaCartaoCreditoVOs().stream().filter(p-> p.getEfetuarBaixa() && p.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getSituacao().equals("AR")).mapToDouble(p-> p.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getValorParcela()).reduce(0D, (a, b) -> Uteis.arrendondarForcando2CadasDecimais(a + b));	
		}
		return getMapaPendenciaCartaoCreditoVOs().stream().filter(p-> p.getEfetuarBaixa() && p.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getSituacao().equals("RE")).mapToDouble(p-> p.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getValorParcela()).reduce(0D, (a, b) -> Uteis.arrendondarForcando2CadasDecimais(a + b));
	}
	
	
	
	public Double getValorTaxaResumoOperacao(boolean efetuarBaixaListaMapaCartao){
		if(efetuarBaixaListaMapaCartao){
			return getMapaPendenciaCartaoCreditoVOs().stream().filter(p-> p.getEfetuarBaixa() && p.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getSituacao().equals("AR")).mapToDouble(p-> p.getValorTaxaUsar()).reduce(0D, (a, b) -> Uteis.arrendondarForcando2CadasDecimais(a + b));	
		}
		return getMapaPendenciaCartaoCreditoVOs().stream().filter(p-> p.getEfetuarBaixa() && p.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getSituacao().equals("RE")).mapToDouble(p-> p.getValorTaxaUsar()).reduce(0D, (a, b) -> Uteis.arrendondarForcando2CadasDecimais(a + b));
	}
	
	public Double getValorLiquidoResumoOperacao(boolean efetuarBaixaListaMapaCartao){
		if(efetuarBaixaListaMapaCartao){
			return getMapaPendenciaCartaoCreditoVOs().stream().filter(p-> p.getEfetuarBaixa() && p.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getSituacao().equals("AR")).mapToDouble(p-> p.getValorLiquido()).reduce(0D, (a, b) -> Uteis.arrendondarForcando2CadasDecimais(a + b));	
		}
		return getMapaPendenciaCartaoCreditoVOs().stream().filter(p-> p.getEfetuarBaixa() && p.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getSituacao().equals("RE")).mapToDouble(p-> p.getValorLiquido()).reduce(0D, (a, b) -> Uteis.arrendondarForcando2CadasDecimais(a + b));
	}
	
		
	

}
