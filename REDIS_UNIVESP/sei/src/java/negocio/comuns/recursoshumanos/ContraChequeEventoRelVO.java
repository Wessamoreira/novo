package negocio.comuns.recursoshumanos;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;

/**
 * Reponsável por transportar os dados  para apresentação do relatorio 
 * RelatorioFichaFinanceira.jrxml.
 * 
 * @see SuperVO
 */
public class ContraChequeEventoRelVO extends SuperVO {

	private static final long serialVersionUID = 7654996808426507853L;

	private Integer codigo;
	private String identificador;
	private String descricao;
	private BigDecimal valorReferencia;
	private BigDecimal provento;
	private BigDecimal desconto;
	private SecaoFolhaPagamentoVO secaoFolhaPagamento;
	
	private List<ContraChequeEventoRelVO> listaEventosFolhaPagamento;

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public BigDecimal getValorReferencia() {
		return valorReferencia;
	}

	public void setValorReferencia(BigDecimal valorReferencia) {
		this.valorReferencia = valorReferencia;
	}

	public BigDecimal getProvento() {
		return provento;
	}

	public void setProvento(BigDecimal provento) {
		this.provento = provento;
	}

	public BigDecimal getDesconto() {
		return desconto;
	}

	public void setDesconto(BigDecimal desconto) {
		this.desconto = desconto;
	}

	public String getIdentificador() {
		return identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	public SecaoFolhaPagamentoVO getSecaoFolhaPagamento() {
		if (secaoFolhaPagamento == null) {
			secaoFolhaPagamento = new SecaoFolhaPagamentoVO();
		}
		return secaoFolhaPagamento;
	}

	public void setSecaoFolhaPagamento(SecaoFolhaPagamentoVO secaoFolhaPagamento) {
		this.secaoFolhaPagamento = secaoFolhaPagamento;
	}

	public List<ContraChequeEventoRelVO> getListaEventosFolhaPagamento() {
		if (listaEventosFolhaPagamento == null) {
			listaEventosFolhaPagamento = new ArrayList<>();
		}
		return listaEventosFolhaPagamento;
	}

	public void setListaEventosFolhaPagamento(List<ContraChequeEventoRelVO> listaEventosFolhaPagamento) {
		this.listaEventosFolhaPagamento = listaEventosFolhaPagamento;
	}
	
}