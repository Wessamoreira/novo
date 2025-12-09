package negocio.comuns.financeiro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.Uteis;

public class ItemPrestacaoContaCategoriaDespesaVO extends SuperVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -888561320324522842L;
	private Integer codigo;
	private PrestacaoContaVO prestacaoConta;
	private CategoriaDespesaVO categoriaDespesa;
	private List<ItemPrestacaoContaPagarVO> itemPrestacaoContaPagarVOs;
	private Double valor;
	private Double valorTotalItemPrestacaoContaPagar;
	private Boolean valorInformadoManual;

	public void calcularValorTotalItemPrestacaoContaPagar() {
		setValorTotalItemPrestacaoContaPagar(
				getItemPrestacaoContaPagarVOs().stream().map(p -> p.getValorCategoriaDespesa()).reduce(0D,
						(a, b) -> Uteis.arrendondarForcando2CadasDecimais(a + b)));
	}

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public CategoriaDespesaVO getCategoriaDespesa() {
		if (categoriaDespesa == null) {
			categoriaDespesa = new CategoriaDespesaVO();
		}
		return categoriaDespesa;
	}

	public void setCategoriaDespesa(CategoriaDespesaVO categoriaDespesa) {
		this.categoriaDespesa = categoriaDespesa;
	}

	public Double getValorTotalPrestacaoConta() {
		return Uteis.arrendondarForcando2CadasDecimais(getValor() + getValorTotalItemPrestacaoContaPagar());
	}

	public Double getValorTotalItemPrestacaoContaPagar() {
		valorTotalItemPrestacaoContaPagar = Optional.ofNullable(valorTotalItemPrestacaoContaPagar).orElse(0.0);
		return valorTotalItemPrestacaoContaPagar;
	}

	public void setValorTotalItemPrestacaoContaPagar(Double valorTotalItemPrestacaoContaPagar) {
		this.valorTotalItemPrestacaoContaPagar = valorTotalItemPrestacaoContaPagar;
	}

	public Double getValor() {
		valor = Optional.ofNullable(valor).orElse(0.0);
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public List<ItemPrestacaoContaPagarVO> getItemPrestacaoContaPagarVOs() {
		if (itemPrestacaoContaPagarVOs == null) {
			itemPrestacaoContaPagarVOs = new ArrayList<>(0);
		}
		return itemPrestacaoContaPagarVOs;
	}

	public void setItemPrestacaoContaPagarVOs(List<ItemPrestacaoContaPagarVO> itemPrestacaoContaPagarVOs) {
		this.itemPrestacaoContaPagarVOs = itemPrestacaoContaPagarVOs;
	}

	public PrestacaoContaVO getPrestacaoConta() {
		if (prestacaoConta == null) {
			prestacaoConta = new PrestacaoContaVO();
		}
		return prestacaoConta;
	}

	public void setPrestacaoConta(PrestacaoContaVO prestacaoConta) {
		this.prestacaoConta = prestacaoConta;
	}

	public boolean equalsCampoSelecaoLista(ItemPrestacaoContaCategoriaDespesaVO obj) {
		return Uteis.isAtributoPreenchido(getCategoriaDespesa())
				&& Uteis.isAtributoPreenchido(obj.getCategoriaDespesa())
				&& getCategoriaDespesa().getCodigo().equals(obj.getCategoriaDespesa().getCodigo());

	}

	public Boolean getValorInformadoManual() {
		if (valorInformadoManual == null) {
			valorInformadoManual = Boolean.FALSE;
		}
		return valorInformadoManual;
	}

	public void setValorInformadoManual(Boolean valorInformadoManual) {
		this.valorInformadoManual = valorInformadoManual;
	}

}
