package negocio.comuns.financeiro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.dominios.TipoOrigemContaReceber;

public class ItemPrestacaoContaOrigemContaReceberVO extends SuperVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7516429279594065722L;
	private Integer codigo;
	private TipoOrigemContaReceber tipoOrigemContaReceber;
	private PrestacaoContaVO prestacaoConta;
	private Double valor;
	private List<ItemPrestacaoContaReceberVO> itemPrestacaoContaReceberVOs;
	private Boolean valorInformadoManual;
	private Double valorManual;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public TipoOrigemContaReceber getTipoOrigemContaReceber() {
		if (tipoOrigemContaReceber == null) {
			tipoOrigemContaReceber = TipoOrigemContaReceber.OUTROS;
		}
		return tipoOrigemContaReceber;
	}

	public void setTipoOrigemContaReceber(TipoOrigemContaReceber tipoOrigemContaReceber) {
		this.tipoOrigemContaReceber = tipoOrigemContaReceber;
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

	public Double getValor() {
		if (valor == null) {
			valor = 0.0;
		}
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public List<ItemPrestacaoContaReceberVO> getItemPrestacaoContaReceberVOs() {
		if (itemPrestacaoContaReceberVOs == null) {
			itemPrestacaoContaReceberVOs = new ArrayList<ItemPrestacaoContaReceberVO>(0);
		}
		return itemPrestacaoContaReceberVOs;
	}

	public void setItemPrestacaoContaReceberVOs(List<ItemPrestacaoContaReceberVO> itemPrestacaoContaReceberVOs) {
		this.itemPrestacaoContaReceberVOs = itemPrestacaoContaReceberVOs;
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
	
	public Double getValorManual() {
		if (valorManual == null) {
			valorManual = 0.0;
		}
		return valorManual;
	}

	public void setValorManual(Double valorManual) {
		this.valorManual = valorManual;
	}
}
