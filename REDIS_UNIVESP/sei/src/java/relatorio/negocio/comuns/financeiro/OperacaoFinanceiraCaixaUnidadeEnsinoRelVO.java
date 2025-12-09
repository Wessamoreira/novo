/**
 * 
 */
package relatorio.negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.compras.FormaPagamentoVO;
import negocio.comuns.financeiro.ChequeVO;
import negocio.comuns.utilitarias.dominios.TipoMovimentacaoFinanceira;
import negocio.comuns.utilitarias.dominios.TipoOrigemMovimentacaoCaixa;

/**
 * @author Rodrigo Wind
 *
 */
public class OperacaoFinanceiraCaixaUnidadeEnsinoRelVO {
	/**
	 * @author Rodrigo Wind - 04/11/2015
	 */
	private String unidadeEnsino;
	private String dataBase;
	private List<OperacaoFinanceiraCaixaItemRelVO> operacaoFinanceiraCaixaItemRelVOs;
	private List<OperacaoFinanceiraResumoFormaPagamentoRelVO> operacaoFinanceiraResumoFormaPagamentoRelVOs;
	private List<OperacaoFinanceiraResumoFormaPagamentoRelVO> operacaoFinanceiraResumoFormaRecebimentoRelVOs;
	private Double saldoDinheiro;
	private Double saldoCheque;
	private Double totalEntradaDia;
	private Double totalSaidaDia;
	private Integer qtdeEntrada;
	private Integer qtdeSaida;

	public String getUnidadeEnsino() {
		if (unidadeEnsino == null) {
			unidadeEnsino = "";
		}
		return unidadeEnsino;
	}
	public void setUnidadeEnsino(String unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}
	public List<OperacaoFinanceiraCaixaItemRelVO> getOperacaoFinanceiraCaixaItemRelVOs() {
		if (operacaoFinanceiraCaixaItemRelVOs == null) {
			operacaoFinanceiraCaixaItemRelVOs = new ArrayList<OperacaoFinanceiraCaixaItemRelVO>(0);
		}
		return operacaoFinanceiraCaixaItemRelVOs;
	}
	public void setOperacaoFinanceiraCaixaItemRelVOs(List<OperacaoFinanceiraCaixaItemRelVO> operacaoFinanceiraCaixaItemRelVOs) {
		this.operacaoFinanceiraCaixaItemRelVOs = operacaoFinanceiraCaixaItemRelVOs;
	}

	public List<OperacaoFinanceiraResumoFormaPagamentoRelVO> getOperacaoFinanceiraResumoFormaPagamentoRelVOs() {
		if (operacaoFinanceiraResumoFormaPagamentoRelVOs == null) {
			operacaoFinanceiraResumoFormaPagamentoRelVOs = new ArrayList<OperacaoFinanceiraResumoFormaPagamentoRelVO>(0);
		}
		return operacaoFinanceiraResumoFormaPagamentoRelVOs;
	}
	public void setOperacaoFinanceiraResumoFormaPagamentoRelVOs(List<OperacaoFinanceiraResumoFormaPagamentoRelVO> operacaoFinanceiraResumoFormaPagamentoRelVOs) {
		this.operacaoFinanceiraResumoFormaPagamentoRelVOs = operacaoFinanceiraResumoFormaPagamentoRelVOs;
	}
	public List<OperacaoFinanceiraResumoFormaPagamentoRelVO> getOperacaoFinanceiraResumoFormaRecebimentoRelVOs() {
		if (operacaoFinanceiraResumoFormaRecebimentoRelVOs == null) {
			operacaoFinanceiraResumoFormaRecebimentoRelVOs = new ArrayList<OperacaoFinanceiraResumoFormaPagamentoRelVO>(0);
		}
		return operacaoFinanceiraResumoFormaRecebimentoRelVOs;
	}
	public void setOperacaoFinanceiraResumoFormaRecebimentoRelVOs(List<OperacaoFinanceiraResumoFormaPagamentoRelVO> operacaoFinanceiraResumoFormaRecebimentoRelVOs) {
		this.operacaoFinanceiraResumoFormaRecebimentoRelVOs = operacaoFinanceiraResumoFormaRecebimentoRelVOs;
	}
	public String getDataBase() {
		if (dataBase == null) {
			dataBase = "";
		}
		return dataBase;
	}
	public void setDataBase(String dataBase) {
		this.dataBase = dataBase;
	}
	
	public Double getSaldoDinheiro() {
		if (saldoDinheiro == null) {
			saldoDinheiro = 0.0;
		}
		return saldoDinheiro;
	}
	/**
	 * @param saldoDinheiro the saldoDinheiro to set
	 */
	public void setSaldoDinheiro(Double saldoDinheiro) {
		this.saldoDinheiro = saldoDinheiro;
	}
	/**
	 * @return the saldoCheque
	 */
	public Double getSaldoCheque() {
		if (saldoCheque == null) {
			saldoCheque = 0.0;
		}
		return saldoCheque;
	}
	/**
	 * @param saldoCheque the saldoCheque to set
	 */
	public void setSaldoCheque(Double saldoCheque) {
		this.saldoCheque = saldoCheque;
	}
	/**
	 * @return the qtdeEntrada
	 */
	public Integer getQtdeEntrada() {
		if (qtdeEntrada == null) {
			qtdeEntrada = 0;
		}
		return qtdeEntrada;
	}
	/**
	 * @param qtdeEntrada the qtdeEntrada to set
	 */
	public void setQtdeEntrada(Integer qtdeEntrada) {
		this.qtdeEntrada = qtdeEntrada;
	}
	/**
	 * @return the qtdeSaida
	 */
	public Integer getQtdeSaida() {
		if (qtdeSaida == null) {
			qtdeSaida = 0;
		}
		return qtdeSaida;
	}
	/**
	 * @param qtdeSaida the qtdeSaida to set
	 */
	public void setQtdeSaida(Integer qtdeSaida) {
		this.qtdeSaida = qtdeSaida;
	}
	
	public Double getTotalEntradaDia() {
		if (totalEntradaDia == null) {
			totalEntradaDia = 0.0;
		}
		return totalEntradaDia;
	}
	/**
	 * @param totalEntradaDia the totalEntradaDia to set
	 */
	public void setTotalEntradaDia(Double totalEntradaDia) {
		this.totalEntradaDia = totalEntradaDia;
	}
	/**
	 * @return the totalSaidaDia
	 */
	public Double getTotalSaidaDia() {
		if (totalSaidaDia == null) {
			totalSaidaDia = 0.0;
		}
		return totalSaidaDia;
	}
	/**
	 * @param totalSaidaDia the totalSaidaDia to set
	 */
	public void setTotalSaidaDia(Double totalSaidaDia) {
		this.totalSaidaDia = totalSaidaDia;
	}

}
