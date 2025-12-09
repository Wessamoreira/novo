package relatorio.negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.financeiro.CentroResultadoOrigemVO;
import negocio.comuns.financeiro.ChequeVO;
import negocio.comuns.financeiro.ExtratoContaCorrenteVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoMovimentacaoFinanceira;

public class ExtratoContaCorrenteRelVO {

	private String unidadeEnsino;
	private String banco;
	private Integer codigoContaCorrente;
	private String agencia;
	private String contaCorrente;
	private Double saldoAnterior;

	private Double totalEntrada;
	private Double totalSaida;
	private Double totalChequeEntrada;
	private Double totalChequeSaida;
	private Double totalChequeDevolvido;
	private Double totalCartao;	
	private List<ExtratoContaCorrenteCartaoRelVO> extratoContaCorrenteCartaoRelVOs;
	private List<ExtratoContaCorrenteVO> extratoContaCorrenteVOs;
	private List<ChequeVO> chequeEntradaVOs;
	private List<ChequeVO> chequeSaidaVOs;
	private List<ChequeVO> chequeDevolvidoVOs;
	private Boolean minimizar;

	public String getBanco() {
		if (banco == null) {
			banco = "";
		}
		return banco;
	}

	public void setBanco(String banco) {
		this.banco = banco;
	}

	public String getAgencia() {
		if (agencia == null) {
			agencia = "";
		}
		return agencia;
	}

	public void setAgencia(String agencia) {
		this.agencia = agencia;
	}

	public String getContaCorrente() {
		if (contaCorrente == null) {
			contaCorrente = "";
		}
		return contaCorrente;
	}

	public void setContaCorrente(String contaCorrente) {
		this.contaCorrente = contaCorrente;
	}

	public Double getSaldoAnterior() {
		if (saldoAnterior == null) {
			saldoAnterior = 0.0;
		}
		return saldoAnterior;
	}

	public void setSaldoAnterior(Double saldoAnterior) {
		this.saldoAnterior = saldoAnterior;
	}
	
	public Double getTotalEntradaPorExtratoContaCorrenteVO() {
		return getExtratoContaCorrenteVOs().stream().filter(p-> p.getTipoMovimentacaoFinanceira().isMovimentacaoEntrada()).mapToDouble(p->p.getValor()).reduce(0D, (a, b) -> Uteis.arrendondarForcando2CadasDecimais(a + b));
	}

	public Double getTotalSaidaPorExtratoContaCorrenteVO() {
		return getExtratoContaCorrenteVOs().stream().filter(p-> p.getTipoMovimentacaoFinanceira().isMovimentacaoSaida()).mapToDouble(p->p.getValor()).reduce(0D, (a, b) -> Uteis.arrendondarForcando2CadasDecimais(a + b));
	}

	public List<ExtratoContaCorrenteVO> getExtratoContaCorrenteVOs() {
		if (extratoContaCorrenteVOs == null) {
			extratoContaCorrenteVOs = new ArrayList<ExtratoContaCorrenteVO>(0);
		}
		return extratoContaCorrenteVOs;
	}

	public void setExtratoContaCorrenteVOs(List<ExtratoContaCorrenteVO> extratoContaCorrenteVOs) {
		this.extratoContaCorrenteVOs = extratoContaCorrenteVOs;
	}

	public List<ChequeVO> getChequeEntradaVOs() {
		if (chequeEntradaVOs == null) {
			chequeEntradaVOs = new ArrayList<ChequeVO>(0);
		}
		return chequeEntradaVOs;
	}

	public void setChequeEntradaVOs(List<ChequeVO> chequeEntradaVOs) {
		this.chequeEntradaVOs = chequeEntradaVOs;
	}

	public List<ChequeVO> getChequeSaidaVOs() {
		if (chequeSaidaVOs == null) {
			chequeSaidaVOs = new ArrayList<ChequeVO>(0);
		}
		return chequeSaidaVOs;
	}

	public void setChequeSaidaVOs(List<ChequeVO> chequeSaidaVOs) {
		this.chequeSaidaVOs = chequeSaidaVOs;
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

	public Integer getCodigoContaCorrente() {
		if (codigoContaCorrente == null) {
			codigoContaCorrente = 0;
		}
		return codigoContaCorrente;
	}

	public void setCodigoContaCorrente(Integer codigoContaCorrente) {
		this.codigoContaCorrente = codigoContaCorrente;
	}

	public Double getSaldoFinal() {
		return getSaldoAnterior() + getTotalEntrada() - getTotalSaida();
	}

	public Double getTotalGeral() {
		return getSaldoAnterior() + getTotalEntrada() + getTotalChequeEntrada() - getTotalSaida() - getTotalChequeSaida() - getTotalCartao();
	}

	public List<ExtratoContaCorrenteCartaoRelVO> getExtratoContaCorrenteCartaoRelVOs() {
		if (extratoContaCorrenteCartaoRelVOs == null) {
			extratoContaCorrenteCartaoRelVOs = new ArrayList<ExtratoContaCorrenteCartaoRelVO>(0);
		}
		return extratoContaCorrenteCartaoRelVOs;
	}

	public void setExtratoContaCorrenteCartaoRelVOs(List<ExtratoContaCorrenteCartaoRelVO> extratoContaCorrenteCartaoRelVOs) {
		this.extratoContaCorrenteCartaoRelVOs = extratoContaCorrenteCartaoRelVOs;
	}

	public Double getTotalEntrada() {
		if (totalEntrada == null) {
			totalEntrada = 0.0;
		}
		return totalEntrada;
	}

	public void setTotalEntrada(Double totalEntrada) {
		this.totalEntrada = totalEntrada;
	}

	public Double getTotalSaida() {
		if (totalSaida == null) {
			totalSaida = 0.0;
		}
		return totalSaida;
	}

	public void setTotalSaida(Double totalSaida) {
		this.totalSaida = totalSaida;
	}

	public Double getTotalChequeEntrada() {
		if (totalChequeEntrada == null) {
			totalChequeEntrada = 0.0;
		}
		return totalChequeEntrada;
	}

	public void setTotalChequeEntrada(Double totalChequeEntrada) {
		this.totalChequeEntrada = totalChequeEntrada;
	}

	public Double getTotalChequeSaida() {
		if (totalChequeSaida == null) {
			totalChequeSaida = 0.0;
		}
		return totalChequeSaida;
	}

	public void setTotalChequeSaida(Double totalChequeSaida) {
		this.totalChequeSaida = totalChequeSaida;
	}

	public Double getTotalCartao() {
		if (totalCartao == null) {
			totalCartao = 0.0;
		}
		return totalCartao;
	}

	public void setTotalCartao(Double totalCartao) {
		this.totalCartao = totalCartao;
	}

	public List<ChequeVO> getChequeDevolvidoVOs() {
		if (chequeDevolvidoVOs == null) {
			chequeDevolvidoVOs = new ArrayList<ChequeVO>(0);
		}
		return chequeDevolvidoVOs;
	}

	public void setChequeDevolvidoVOs(List<ChequeVO> chequeDevolvidoVOs) {
		this.chequeDevolvidoVOs = chequeDevolvidoVOs;
	}

	public Double getTotalChequeDevolvido() {
		if (totalChequeDevolvido == null) {
			totalChequeDevolvido = 0.0;
		}
		return totalChequeDevolvido;
	}

	public void setTotalChequeDevolvido(Double totalChequeDevolvido) {
		this.totalChequeDevolvido = totalChequeDevolvido;
	}
	
	public Double saldoFinalApresentar;
	public Double getSaldoFinalApresentar() {
		if(saldoFinalApresentar == null) {
			saldoFinalApresentar = Uteis.arrendondarForcando2CadasDecimais(getSaldoAnterior() + getTotalEntrada() - getTotalSaida());
			if(saldoFinalApresentar.equals(-0.0)) {
				saldoFinalApresentar = 0.0;
			}
			
		}
		return saldoFinalApresentar;
	}
	
	public Double saldoAnteriorApresentar;
	public Double getSaldoAnteriorApresentar() {
		if(saldoAnteriorApresentar == null) {
			saldoAnteriorApresentar = getSaldoAnterior();			
		}
		return saldoAnteriorApresentar;
	}

	public void setSaldoFinalApresentar(Double saldoFinalApresentar) {
		this.saldoFinalApresentar = saldoFinalApresentar;
	}

	public void setSaldoAnteriorApresentar(Double saldoAnteriorApresentar) {
		this.saldoAnteriorApresentar = saldoAnteriorApresentar;
	}
	

	private Date dataInicio;
	private Date dataFinal;

	public Date getDataInicio() {		
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFinal() {
		
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	public Boolean getMinimizar() {
		if (minimizar == null) {
			minimizar = false;
		}
		return minimizar;
	}

	public void setMinimizar(Boolean minimizar) {
		this.minimizar = minimizar;
	}
	
	public Integer getQtdeExtrato() {
		return getExtratoContaCorrenteVOs().size();
	}

}
