/**
 * 
 */
package relatorio.negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Rodrigo Wind
 *
 */
public class OperacaoFinanceiraCaixaRelVO {
	/**
	 * @author Rodrigo Wind - 04/11/2015
	 */
	private Integer codigoCaixa;
	private String contaCaixa;
	private String responsavelAberturaCaixa;
	private String responsavelFechamentoCaixa;
	private String unidadeEnsino;
	private Date dataAberturaCaixa;
	private Date dataFechamentoCaixa;
	private Double saldoInicialCaixa;
	private Double saldoInicialDinheiro;
	private Double saldoInicialCheque;
	private Double saldoFinalDinheiro;
	private Double saldoFinalCheque;
	private Double saldoFinalCaixa;
	private Double totalEntradaDia;	
	private Double totalSaidaDia;
	private Integer qtdeEntrada;
	private Integer qtdeSaida;
	private List<OperacaoFinanceiraCaixaUnidadeEnsinoRelVO> operacaoFinanceiraCaixaUnidadeEnsinoRelVOs;
	private List<OperacaoFinanceiraResumoFormaPagamentoRelVO> operacaoFinanceiraResumoFormaPagamentoRelVOs;
	private List<OperacaoFinanceiraResumoFormaPagamentoRelVO> operacaoFinanceiraResumoFormaRecebimentoRelVOs;
	
	
	/**
	 * @return the codigoCaixa
	 */
	public Integer getCodigoCaixa() {
		if (codigoCaixa == null) {
			codigoCaixa = 0;
		}
		return codigoCaixa;
	}
	/**
	 * @param codigoCaixa the codigoCaixa to set
	 */
	public void setCodigoCaixa(Integer codigoCaixa) {
		this.codigoCaixa = codigoCaixa;
	}
	/**
	 * @return the contaCaixa
	 */
	public String getContaCaixa() {
		if (contaCaixa == null) {
			contaCaixa = "";
		}
		return contaCaixa;
	}
	/**
	 * @param contaCaixa the contaCaixa to set
	 */
	public void setContaCaixa(String contaCaixa) {
		this.contaCaixa = contaCaixa;
	}
	/**
	 * @return the responsavelAberturaCaixa
	 */
	public String getResponsavelAberturaCaixa() {
		if (responsavelAberturaCaixa == null) {
			responsavelAberturaCaixa = "";
		}
		return responsavelAberturaCaixa;
	}
	/**
	 * @param responsavelAberturaCaixa the responsavelAberturaCaixa to set
	 */
	public void setResponsavelAberturaCaixa(String responsavelAberturaCaixa) {
		this.responsavelAberturaCaixa = responsavelAberturaCaixa;
	}
	/**
	 * @return the responsavelFechamentoCaixa
	 */
	public String getResponsavelFechamentoCaixa() {
		if (responsavelFechamentoCaixa == null) {
			responsavelFechamentoCaixa = "";
		}
		return responsavelFechamentoCaixa;
	}
	/**
	 * @param responsavelFechamentoCaixa the responsavelFechamentoCaixa to set
	 */
	public void setResponsavelFechamentoCaixa(String responsavelFechamentoCaixa) {
		this.responsavelFechamentoCaixa = responsavelFechamentoCaixa;
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
	/**
	 * @return the dataAberturaCaixa
	 */
	public Date getDataAberturaCaixa() {		
		return dataAberturaCaixa;
	}
	/**
	 * @param dataAberturaCaixa the dataAberturaCaixa to set
	 */
	public void setDataAberturaCaixa(Date dataAberturaCaixa) {
		this.dataAberturaCaixa = dataAberturaCaixa;
	}
	/**
	 * @return the dataFechamentoCaixa
	 */
	public Date getDataFechamentoCaixa() {		
		return dataFechamentoCaixa;
	}
	/**
	 * @param dataFechamentoCaixa the dataFechamentoCaixa to set
	 */
	public void setDataFechamentoCaixa(Date dataFechamentoCaixa) {
		this.dataFechamentoCaixa = dataFechamentoCaixa;
	}
	/**
	 * @return the saldoInicialCaixa
	 */
	public Double getSaldoInicialCaixa() {
		if (saldoInicialCaixa == null) {
			saldoInicialCaixa = 0.0;
		}
		return saldoInicialCaixa;
	}
	/**
	 * @param saldoInicialCaixa the saldoInicialCaixa to set
	 */
	public void setSaldoInicialCaixa(Double saldoInicialCaixa) {
		this.saldoInicialCaixa = saldoInicialCaixa;
	}
	/**
	 * @return the saldoInicialDinheiro
	 */
	public Double getSaldoInicialDinheiro() {
		if (saldoInicialDinheiro == null) {
			saldoInicialDinheiro = 0.0;
		}
		return saldoInicialDinheiro;
	}
	/**
	 * @param saldoInicialDinheiro the saldoInicialDinheiro to set
	 */
	public void setSaldoInicialDinheiro(Double saldoInicialDinheiro) {
		this.saldoInicialDinheiro = saldoInicialDinheiro;
	}
	/**
	 * @return the saldoInicialCheque
	 */
	public Double getSaldoInicialCheque() {
		if (saldoInicialCheque == null) {
			saldoInicialCheque = 0.0;
		}
		return saldoInicialCheque;
	}
	/**
	 * @param saldoInicialCheque the saldoInicialCheque to set
	 */
	public void setSaldoInicialCheque(Double saldoInicialCheque) {
		this.saldoInicialCheque = saldoInicialCheque;
	}
	/**
	 * @return the saldoFinalDinheiro
	 */
	public Double getSaldoFinalDinheiro() {
		if (saldoFinalDinheiro == null) {
			saldoFinalDinheiro = 0.0;
		}
		return saldoFinalDinheiro;
	}
	/**
	 * @param saldoFinalDinheiro the saldoFinalDinheiro to set
	 */
	public void setSaldoFinalDinheiro(Double saldoFinalDinheiro) {
		this.saldoFinalDinheiro = saldoFinalDinheiro;
	}
	/**
	 * @return the saldoFinalCheque
	 */
	public Double getSaldoFinalCheque() {
		if (saldoFinalCheque == null) {
			saldoFinalCheque = 0.0;
		}
		return saldoFinalCheque;
	}
	/**
	 * @param saldoFinalCheque the saldoFinalCheque to set
	 */
	public void setSaldoFinalCheque(Double saldoFinalCheque) {
		this.saldoFinalCheque = saldoFinalCheque;
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
	/**
	 * @return the saldoFinalCaixa
	 */
	public Double getSaldoFinalCaixa() {
		if (saldoFinalCaixa == null) {
			saldoFinalCaixa = 0.0;
		}
		return saldoFinalCaixa;
	}
	/**
	 * @param saldoFinalCaixa the saldoFinalCaixa to set
	 */
	public void setSaldoFinalCaixa(Double saldoFinalCaixa) {
		this.saldoFinalCaixa = saldoFinalCaixa;
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

	public List<OperacaoFinanceiraCaixaUnidadeEnsinoRelVO> getOperacaoFinanceiraCaixaUnidadeEnsinoRelVOs() {
		if (operacaoFinanceiraCaixaUnidadeEnsinoRelVOs == null) {
			operacaoFinanceiraCaixaUnidadeEnsinoRelVOs = new ArrayList<OperacaoFinanceiraCaixaUnidadeEnsinoRelVO>(0);
		}
		return operacaoFinanceiraCaixaUnidadeEnsinoRelVOs;
	}
	public void setOperacaoFinanceiraCaixaUnidadeEnsinoRelVOs(List<OperacaoFinanceiraCaixaUnidadeEnsinoRelVO> operacaoFinanceiraCaixaUnidadeEnsinoRelVOs) {
		this.operacaoFinanceiraCaixaUnidadeEnsinoRelVOs = operacaoFinanceiraCaixaUnidadeEnsinoRelVOs;
	}
	
}
