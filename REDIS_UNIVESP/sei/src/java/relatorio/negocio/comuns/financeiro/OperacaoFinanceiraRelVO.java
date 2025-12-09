/**
 * 
 */
package relatorio.negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.utilitarias.Uteis;

/**
 * @author Rodrigo Wind
 *
 */
public class OperacaoFinanceiraRelVO {
	/**
	 * @author Rodrigo Wind - 04/11/2015
	 */
	private Date data;
	private List<OperacaoFinanceiraRelVO> operacaoFinanceiraRelVOs;
	private List<OperacaoFinanceiraCaixaRelVO> operacaoFinanceiraCaixaRelVOs;
	private List<OperacaoFinanceiraResumoFormaPagamentoRelVO> operacaoFinanceiraResumoFormaPagamentoRelVOs;
	private List<OperacaoFinanceiraResumoFormaPagamentoRelVO> operacaoFinanceiraResumoFormaRecebimentoRelVOs;
	private Double saldoDinheiro;
	private Double saldoCheque;
	private Double totalEntradaDia;
	private Double totalSaidaDia;
	private Integer qtdeEntrada;
	private Integer qtdeSaida;
	private String unidadeEnsino;	
	
	/**
	 * @return the operacaoFinanceiraCaixaRelVOs
	 */
	public List<OperacaoFinanceiraCaixaRelVO> getOperacaoFinanceiraCaixaRelVOs() {
		if (operacaoFinanceiraCaixaRelVOs == null) {
			operacaoFinanceiraCaixaRelVOs = new ArrayList<OperacaoFinanceiraCaixaRelVO>(0);
		}
		return operacaoFinanceiraCaixaRelVOs;
	}
	/**
	 * @param operacaoFinanceiraCaixaRelVOs the operacaoFinanceiraCaixaRelVOs to set
	 */
	public void setOperacaoFinanceiraCaixaRelVOs(List<OperacaoFinanceiraCaixaRelVO> operacaoFinanceiraCaixaRelVOs) {
		this.operacaoFinanceiraCaixaRelVOs = operacaoFinanceiraCaixaRelVOs;
	}
	/**
	 * @return the operacaoFinanceiraResumoFormaPagamentoRelVOs
	 */
	public List<OperacaoFinanceiraResumoFormaPagamentoRelVO> getOperacaoFinanceiraResumoFormaPagamentoRelVOs() {
		if (operacaoFinanceiraResumoFormaPagamentoRelVOs == null) {
			operacaoFinanceiraResumoFormaPagamentoRelVOs = new ArrayList<OperacaoFinanceiraResumoFormaPagamentoRelVO>(0);
		}
		return operacaoFinanceiraResumoFormaPagamentoRelVOs;
	}
	/**
	 * @param operacaoFinanceiraResumoFormaPagamentoRelVOs the operacaoFinanceiraResumoFormaPagamentoRelVOs to set
	 */
	public void setOperacaoFinanceiraResumoFormaPagamentoRelVOs(List<OperacaoFinanceiraResumoFormaPagamentoRelVO> operacaoFinanceiraResumoFormaPagamentoRelVOs) {
		this.operacaoFinanceiraResumoFormaPagamentoRelVOs = operacaoFinanceiraResumoFormaPagamentoRelVOs;
	}
	/**
	 * @return the saldoDinheiro
	 */
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
	/**
	 * @return the operacaoFinanceiraResumoFormaRecebimentoRelVOs
	 */
	public List<OperacaoFinanceiraResumoFormaPagamentoRelVO> getOperacaoFinanceiraResumoFormaRecebimentoRelVOs() {
		if (operacaoFinanceiraResumoFormaRecebimentoRelVOs == null) {
			operacaoFinanceiraResumoFormaRecebimentoRelVOs = new ArrayList<OperacaoFinanceiraResumoFormaPagamentoRelVO>(0);
		}
		return operacaoFinanceiraResumoFormaRecebimentoRelVOs;
	}
	/**
	 * @param operacaoFinanceiraResumoFormaRecebimentoRelVOs the operacaoFinanceiraResumoFormaRecebimentoRelVOs to set
	 */
	public void setOperacaoFinanceiraResumoFormaRecebimentoRelVOs(List<OperacaoFinanceiraResumoFormaPagamentoRelVO> operacaoFinanceiraResumoFormaRecebimentoRelVOs) {
		this.operacaoFinanceiraResumoFormaRecebimentoRelVOs = operacaoFinanceiraResumoFormaRecebimentoRelVOs;
	}
	/**
	 * @return the data
	 */
	public Date getData() {		
		return data;
	}
	/**
	 * @param data the data to set
	 */
	public void setData(Date data) {
		this.data = data;
	}
	/**
	 * @return the operacaoFinanceiraRelVOs
	 */
	public List<OperacaoFinanceiraRelVO> getOperacaoFinanceiraRelVOs() {
		if (operacaoFinanceiraRelVOs == null) {
			operacaoFinanceiraRelVOs = new ArrayList<OperacaoFinanceiraRelVO>(0);
		}
		return operacaoFinanceiraRelVOs;
	}
	/**
	 * @param operacaoFinanceiraRelVOs the operacaoFinanceiraRelVOs to set
	 */
	public void setOperacaoFinanceiraRelVOs(List<OperacaoFinanceiraRelVO> operacaoFinanceiraRelVOs) {
		this.operacaoFinanceiraRelVOs = operacaoFinanceiraRelVOs;
	}
	/**
	 * @return the totalEntradaDia
	 */
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
	
	
	public String getDataApresentar(){
		return Uteis.getData(getData());
	}

	public String getUnidadeEnsino() {
		if (unidadeEnsino == null) {
			unidadeEnsino = "";
		}
		return unidadeEnsino;
	}

	public void setUnidadeEnsino(String unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}
		
}
