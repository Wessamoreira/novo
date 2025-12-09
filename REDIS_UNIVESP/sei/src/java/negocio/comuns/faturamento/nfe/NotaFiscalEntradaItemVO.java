package negocio.comuns.faturamento.nfe;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.compras.ProdutoServicoVO;
import negocio.comuns.compras.RecebimentoCompraItemVO;
import negocio.comuns.financeiro.CentroResultadoOrigemVO;
import negocio.comuns.utilitarias.Uteis;

/**
 * 
 * @author Pedro Otimize
 *
 */
public class NotaFiscalEntradaItemVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1431850520669650270L;
	private Integer codigo;
	private NotaFiscalEntradaVO notaFiscalEntradaVO;
	private RecebimentoCompraItemVO recebimentoCompraItemVO;
	private ProdutoServicoVO produtoServicoVO;
	private Double quantidade;
	private Double valorUnitario;
	private List<CentroResultadoOrigemVO> listaCentroResultadoOrigemVOs;
	private List<NotaFiscalEntradaItemRecebimentoVO> listaNotaFiscalEntradaItemRecebimentoVOs;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public NotaFiscalEntradaVO getNotaFiscalEntradaVO() {
		if (notaFiscalEntradaVO == null) {
			notaFiscalEntradaVO = new NotaFiscalEntradaVO();
		}
		return notaFiscalEntradaVO;
	}

	public void setNotaFiscalEntradaVO(NotaFiscalEntradaVO notaFiscalEntradaVO) {
		this.notaFiscalEntradaVO = notaFiscalEntradaVO;
	}

	public ProdutoServicoVO getProdutoServicoVO() {
		if (produtoServicoVO == null) {
			produtoServicoVO = new ProdutoServicoVO();
		}
		return produtoServicoVO;
	}

	public void setProdutoServicoVO(ProdutoServicoVO produtoServicoVO) {
		this.produtoServicoVO = produtoServicoVO;
	}

	public RecebimentoCompraItemVO getRecebimentoCompraItemVO() {
		recebimentoCompraItemVO = Optional.ofNullable(recebimentoCompraItemVO).orElse(new RecebimentoCompraItemVO());
		return recebimentoCompraItemVO;
	}

	public void setRecebimentoCompraItemVO(RecebimentoCompraItemVO recebimentoCompraItemVO) {
		this.recebimentoCompraItemVO = recebimentoCompraItemVO;
	}

	public Double getQuantidade() {
		if (quantidade == null) {
			quantidade = 0.0;
		}
		return quantidade;
	}

	public void setQuantidade(Double quantidade) {
		this.quantidade = quantidade;
	}

	public Double getValorUnitario() {
		if (valorUnitario == null) {
			valorUnitario = 0.0;
		}
		return valorUnitario;
	}

	public void setValorUnitario(Double valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	public Double getValorTotal() {
		return Uteis.arrendondarForcando2CadasDecimais(getQuantidade() * getValorUnitario());
	}
	
	public List<CentroResultadoOrigemVO> getListaCentroResultadoOrigemVOs() {
		listaCentroResultadoOrigemVOs = Optional.ofNullable(listaCentroResultadoOrigemVOs).orElse(new ArrayList<>());
		return listaCentroResultadoOrigemVOs;
	}

	public void setListaCentroResultadoOrigemVOs(List<CentroResultadoOrigemVO> listaCentroResultadoOrigemVOs) {
		this.listaCentroResultadoOrigemVOs = listaCentroResultadoOrigemVOs;
	}
	
	public List<NotaFiscalEntradaItemRecebimentoVO> getListaNotaFiscalEntradaItemRecebimentoVOs() {
		listaNotaFiscalEntradaItemRecebimentoVOs = Optional.ofNullable(listaNotaFiscalEntradaItemRecebimentoVOs).orElse(new ArrayList<>());
		return listaNotaFiscalEntradaItemRecebimentoVOs;
	}

	public void setListaNotaFiscalEntradaItemRecebimentoVOs(List<NotaFiscalEntradaItemRecebimentoVO> listaNotaFiscalEntradaItemRecebimentoVOs) {
		this.listaNotaFiscalEntradaItemRecebimentoVOs = listaNotaFiscalEntradaItemRecebimentoVOs;
	}
	
	public Double getQuantidadeCentroResultadoTotal() {
		return getListaCentroResultadoOrigemVOs().stream().mapToDouble(CentroResultadoOrigemVO::getQuantidade).sum();
	}
	
	public Double getPorcentagemCentroResultadoTotal() {
		return getListaCentroResultadoOrigemVOs().stream().map(p -> p.getPorcentagem()).reduce(0D, (a,b) -> Uteis.arrendondarForcandoCadasDecimais(a + b, 8));
	}
	
	public Double getPrecoCentroResultadoTotal() {
		return getListaCentroResultadoOrigemVOs().stream().map(p -> p.getValor()).reduce(0D, (a,b) -> Uteis.arrendondarForcando2CadasDecimais(a + b));
	}

	public boolean equalsCampoSelecaoLista(NotaFiscalEntradaItemVO obj) {
		return Uteis.isAtributoPreenchido(getRecebimentoCompraItemVO())  && Uteis.isAtributoPreenchido(obj.getRecebimentoCompraItemVO()) && getRecebimentoCompraItemVO().getCodigo().equals(obj.getRecebimentoCompraItemVO().getCodigo());
	}
	
	public boolean equalsCampoSelecaoListaManual(NotaFiscalEntradaItemVO obj) {
		return Uteis.isAtributoPreenchido(getProdutoServicoVO())  && Uteis.isAtributoPreenchido(obj.getProdutoServicoVO()) && getProdutoServicoVO().getCodigo().equals(obj.getProdutoServicoVO().getCodigo())
				&& Uteis.isAtributoPreenchido(getValorUnitario())  && Uteis.isAtributoPreenchido(obj.getValorUnitario()) && getValorUnitario().equals(obj.getValorUnitario());
	}
	
	public Boolean getPermitiEditarValorCentroResultadoOrigem() {
		return !getPrecoCentroResultadoTotal().equals(getValorTotal());
	}
	
	public Boolean getPermitiEditarPorcentagemCentroResultadoOrigem() {
		return !getPorcentagemCentroResultadoTotal().equals(100.00);
	}
	
	public void calcularValorCentroResultadoOrigem() {
		getListaCentroResultadoOrigemVOs().forEach(crovo -> crovo.calcularValor(getValorTotal()));
	}
	
	public void calcularPorcentagemCentroResultadoOrigem() {
		getListaCentroResultadoOrigemVOs().forEach(crovo -> crovo.calcularPorcentagem(getValorTotal()));
	}
}
