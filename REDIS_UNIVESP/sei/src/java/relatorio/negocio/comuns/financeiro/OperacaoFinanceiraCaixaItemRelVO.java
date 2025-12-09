/**
 * 
 */
package relatorio.negocio.comuns.financeiro;

import java.util.Date;

import negocio.comuns.compras.FormaPagamentoVO;
import negocio.comuns.financeiro.ChequeVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.utilitarias.dominios.TipoMovimentacaoFinanceira;
import negocio.comuns.utilitarias.dominios.TipoOrigemMovimentacaoCaixa;

/**
 * @author Rodrigo Wind
 *
 */
public class OperacaoFinanceiraCaixaItemRelVO {
	/**
	 * @author Rodrigo Wind - 04/11/2015
	 */
	private TipoOrigemMovimentacaoCaixa tipoOrigem;
	private TipoMovimentacaoFinanceira tipoOperacao;
	private String unidadeEnsino;
	private String sacado;
	private FormaPagamentoVO formaPagamentoVO;
	private ChequeVO chequeVO;
	private Date dataPrevisao;
	private Double valor;
	private Integer codigoOrigem;
	private String nomeContaCorrenteMovimentacao;
	
	/**
	 * @return the tipoOrigem
	 */
	public TipoOrigemMovimentacaoCaixa getTipoOrigem() {
		if (tipoOrigem == null) {
			tipoOrigem = TipoOrigemMovimentacaoCaixa.RECEBIMENTO;
		}
		return tipoOrigem;
	}
	/**
	 * @param tipoOrigem the tipoOrigem to set
	 */
	public void setTipoOrigem(TipoOrigemMovimentacaoCaixa tipoOrigem) {
		this.tipoOrigem = tipoOrigem;
	}
	/**
	 * @return the tipoOperacao
	 */
	public TipoMovimentacaoFinanceira getTipoOperacao() {
		if (tipoOperacao == null) {
			tipoOperacao = TipoMovimentacaoFinanceira.ENTRADA;
		}
		return tipoOperacao;
	}
	/**
	 * @param tipoOperacao the tipoOperacao to set
	 */
	public void setTipoOperacao(TipoMovimentacaoFinanceira tipoOperacao) {
		this.tipoOperacao = tipoOperacao;
	}
	/**
	 * @return the sacado
	 */
	public String getSacado() {
		if (sacado == null) {
			sacado = "";
		}
		return sacado;
	}
	/**
	 * @param sacado the sacado to set
	 */
	public void setSacado(String sacado) {
		this.sacado = sacado;
	}
	/**
	 * @return the formaPagamentoVO
	 */
	public FormaPagamentoVO getFormaPagamentoVO() {
		if (formaPagamentoVO == null) {
			formaPagamentoVO = new FormaPagamentoVO();
		}
		return formaPagamentoVO;
	}
	/**
	 * @param formaPagamentoVO the formaPagamentoVO to set
	 */
	public void setFormaPagamentoVO(FormaPagamentoVO formaPagamentoVO) {
		this.formaPagamentoVO = formaPagamentoVO;
	}
	/**
	 * @return the chequeVO
	 */
	public ChequeVO getChequeVO() {
		if (chequeVO == null) {
			chequeVO = new ChequeVO();
		}
		return chequeVO;
	}
	/**
	 * @param chequeVO the chequeVO to set
	 */
	public void setChequeVO(ChequeVO chequeVO) {
		this.chequeVO = chequeVO;
	}
	/**
	 * @return the dataPrevisao
	 */
	public Date getDataPrevisao() {
		if (dataPrevisao == null) {
			dataPrevisao = null;
		}
		return dataPrevisao;
	}
	/**
	 * @param dataPrevisao the dataPrevisao to set
	 */
	public void setDataPrevisao(Date dataPrevisao) {
		this.dataPrevisao = dataPrevisao;
	}
	/**
	 * @return the valor
	 */
	public Double getValor() {
		if (valor == null) {
			valor = 0.0;
		}
		return valor;
	}
	/**
	 * @param valor the valor to set
	 */
	public void setValor(Double valor) {
		this.valor = valor;
	}
	/**
	 * @return the codigoOrigem
	 */
	public Integer getCodigoOrigem() {
		if (codigoOrigem == null) {
			codigoOrigem = 0;
		}
		return codigoOrigem;
	}
	/**
	 * @param codigoOrigem the codigoOrigem to set
	 */
	public void setCodigoOrigem(Integer codigoOrigem) {
		this.codigoOrigem = codigoOrigem;
	}
	/**
	 * @return the unidadeEnsino
	 */
	public String getUnidadeEnsino() {
		if (unidadeEnsino == null) {
			unidadeEnsino = "";
		}
		return unidadeEnsino;
	}
	/**
	 * @param unidadeEnsino the unidadeEnsino to set
	 */
	public void setUnidadeEnsino(String unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}
	
	public String getNomeContaCorrenteMovimentacao() {
		if (nomeContaCorrenteMovimentacao == null) {
			nomeContaCorrenteMovimentacao = "";
		}
		return nomeContaCorrenteMovimentacao;
	}

	public void setNomeContaCorrenteMovimentacao(String nomeContaCorrenteMovimentacao) {
		this.nomeContaCorrenteMovimentacao = nomeContaCorrenteMovimentacao;
	}
	
	
}
