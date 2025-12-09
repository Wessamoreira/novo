package negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.financeiro.enumerador.OrigemFechamentoFinanceiroCentroResultadoEnum;
import negocio.comuns.financeiro.enumerador.TipoCentroResultadoOrigemEnum;
import negocio.comuns.financeiro.enumerador.TipoMovimentacaoCentroResultadoOrigemEnum;

public class FechamentoFinanceiroCentroResultadoVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2539460847050753812L;
	private Integer codigo;
	private TipoCentroResultadoOrigemEnum tipoCentroResultadoOrigemEnum;
	private Integer codigoOrigem;
	private CentroResultadoVO centroResultado;	
	private Double valor;
	private Double valorContaReceberRecebido;
	private Double valorContaPagarPago;
	private Double valorContaReceberNegociado;
	private Double valorContaReceberCancelado;
	private Double valorContaReceberAReceber;
	private Double valorContaPagarAPagar;
	private Double valorContaPagarNegociado;
	private Double valorContaPagarCancelado;
	private TipoMovimentacaoCentroResultadoOrigemEnum tipoMovimentacaoCentroResultadoOrigemEnum;
	private OrigemFechamentoFinanceiroCentroResultadoEnum origemFechamentoFinanceiroCentroResultadoOrigem;
	private Integer codOrigemFechamentoFinanceiro;
	private List<FechamentoFinanceiroContaCentroResultadoVO> fechamentoFinanceiroContaCentroResultadoVOs;	
	private CategoriaDespesaVO categoriaDespesa;
	private CentroReceitaVO centroReceita;
	

	public OrigemFechamentoFinanceiroCentroResultadoEnum getOrigemFechamentoFinanceiroCentroResultadoOrigem() {
		if (origemFechamentoFinanceiroCentroResultadoOrigem == null) {
			origemFechamentoFinanceiroCentroResultadoOrigem = OrigemFechamentoFinanceiroCentroResultadoEnum.FECHAMENTO_FINANCEIRO;
		}
		return origemFechamentoFinanceiroCentroResultadoOrigem;
	}

	public void setOrigemFechamentoFinanceiroCentroResultadoOrigem(
			OrigemFechamentoFinanceiroCentroResultadoEnum origemFechamentoFinanceiroCentroResultadoOrigem) {
		this.origemFechamentoFinanceiroCentroResultadoOrigem = origemFechamentoFinanceiroCentroResultadoOrigem;
	}
	
	

	public Integer getCodOrigemFechamentoFinanceiro() {
		if (codOrigemFechamentoFinanceiro == null) {
			codOrigemFechamentoFinanceiro = 0;
		}
		return codOrigemFechamentoFinanceiro;
	}

	public void setCodOrigemFechamentoFinanceiro(Integer codOrigemFechamentoFinanceiro) {
		this.codOrigemFechamentoFinanceiro = codOrigemFechamentoFinanceiro;
	}

	public Integer getCodigoOrigem() {
		if (codigoOrigem == null) {
			codigoOrigem = 0;
		}
		return codigoOrigem;
	}

	public void setCodigoOrigem(Integer codigoOrigem) {
		this.codigoOrigem = codigoOrigem;
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

	public TipoCentroResultadoOrigemEnum getTipoCentroResultadoOrigemEnum() {
		if (tipoCentroResultadoOrigemEnum == null) {
			tipoCentroResultadoOrigemEnum = TipoCentroResultadoOrigemEnum.CONTA_RECEBER;
		}
		return tipoCentroResultadoOrigemEnum;
	}

	public void setTipoCentroResultadoOrigemEnum(TipoCentroResultadoOrigemEnum tipoCentroResultadoOrigemEnum) {
		this.tipoCentroResultadoOrigemEnum = tipoCentroResultadoOrigemEnum;
	}

	public CentroResultadoVO getCentroResultado() {
		if (centroResultado == null) {
			centroResultado = new CentroResultadoVO();
		}
		return centroResultado;
	}

	public void setCentroResultado(CentroResultadoVO centroResultado) {
		this.centroResultado = centroResultado;
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

	public TipoMovimentacaoCentroResultadoOrigemEnum getTipoMovimentacaoCentroResultadoOrigemEnum() {
		if (tipoMovimentacaoCentroResultadoOrigemEnum == null) {
			tipoMovimentacaoCentroResultadoOrigemEnum = TipoMovimentacaoCentroResultadoOrigemEnum.ENTRADA;
		}
		return tipoMovimentacaoCentroResultadoOrigemEnum;
	}

	public void setTipoMovimentacaoCentroResultadoOrigemEnum(
			TipoMovimentacaoCentroResultadoOrigemEnum tipoMovimentacaoCentroResultadoOrigemEnum) {
		this.tipoMovimentacaoCentroResultadoOrigemEnum = tipoMovimentacaoCentroResultadoOrigemEnum;
	}

	public Double getValorContaReceberRecebido() {
		if (valorContaReceberRecebido == null) {
			valorContaReceberRecebido = 0.0;
		}
		return valorContaReceberRecebido;
	}

	public void setValorContaReceberRecebido(Double valorContaReceberRecebido) {
		this.valorContaReceberRecebido = valorContaReceberRecebido;
	}

	public Double getValorContaPagarPago() {
		if (valorContaPagarPago == null) {
			valorContaPagarPago = 0.0;
		}
		return valorContaPagarPago;
	}

	public void setValorContaPagarPago(Double valorContaPagarPago) {
		this.valorContaPagarPago = valorContaPagarPago;
	}

	public Double getValorContaReceberNegociado() {
		if (valorContaReceberNegociado == null) {
			valorContaReceberNegociado = 0.0;
		}
		return valorContaReceberNegociado;
	}

	public void setValorContaReceberNegociado(Double valorContaReceberNegociado) {
		this.valorContaReceberNegociado = valorContaReceberNegociado;
	}

	public Double getValorContaReceberCancelado() {
		if (valorContaReceberCancelado == null) {
			valorContaReceberCancelado = 0.0;
		}
		return valorContaReceberCancelado;
	}

	public void setValorContaReceberCancelado(Double valorContaReceberCancelado) {
		this.valorContaReceberCancelado = valorContaReceberCancelado;
	}

	public Double getValorContaReceberAReceber() {
		if (valorContaReceberAReceber == null) {
			valorContaReceberAReceber = 0.0;
		}
		return valorContaReceberAReceber;
	}

	public void setValorContaReceberAReceber(Double valorContaReceberAReceber) {
		this.valorContaReceberAReceber = valorContaReceberAReceber;
	}

	public Double getValorContaPagarAPagar() {
		if (valorContaPagarAPagar == null) {
			valorContaPagarAPagar = 0.0;
		}
		return valorContaPagarAPagar;
	}

	public void setValorContaPagarAPagar(Double valorContaPagarAPagar) {
		this.valorContaPagarAPagar = valorContaPagarAPagar;
	}

	public Double getValorContaPagarNegociado() {
		if (valorContaPagarNegociado == null) {
			valorContaPagarNegociado = 0.0;
		}
		return valorContaPagarNegociado;
	}

	public void setValorContaPagarNegociado(Double valorContaPagarNegociado) {
		this.valorContaPagarNegociado = valorContaPagarNegociado;
	}

	public Double getValorContaPagarCancelado() {
		if (valorContaPagarCancelado == null) {
			valorContaPagarCancelado = 0.0;
		}
		return valorContaPagarCancelado;
	}

	public void setValorContaPagarCancelado(Double valorContaPagarCancelado) {
		this.valorContaPagarCancelado = valorContaPagarCancelado;
	}

	public List<FechamentoFinanceiroContaCentroResultadoVO> getFechamentoFinanceiroContaCentroResultadoVOs() {
		if(fechamentoFinanceiroContaCentroResultadoVOs == null){
			fechamentoFinanceiroContaCentroResultadoVOs = new ArrayList<FechamentoFinanceiroContaCentroResultadoVO>(0);
		}
		return fechamentoFinanceiroContaCentroResultadoVOs;
	}

	public void setFechamentoFinanceiroContaCentroResultadoVOs( List<FechamentoFinanceiroContaCentroResultadoVO> fechamentoFinanceiroContaCentroResultadoVOs) {
		this.fechamentoFinanceiroContaCentroResultadoVOs = fechamentoFinanceiroContaCentroResultadoVOs;
	}	

	public CategoriaDespesaVO getCategoriaDespesa() {
		if(categoriaDespesa == null){
			categoriaDespesa = new CategoriaDespesaVO();
		}
		return categoriaDespesa;
	}

	public void setCategoriaDespesa(CategoriaDespesaVO categoriaDespesa) {
		this.categoriaDespesa = categoriaDespesa;
	}

	public CentroReceitaVO getCentroReceita() {
		if(centroReceita == null){
			centroReceita = new CentroReceitaVO();
		}
		return centroReceita;
	}

	public void setCentroReceita(CentroReceitaVO centroReceita) {
		this.centroReceita = centroReceita;
	}
	

}
