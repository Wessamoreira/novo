package negocio.comuns.financeiro;

import negocio.comuns.arquitetura.SuperVO;

public class FechamentoFinanceiroContaCentroResultadoVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5844166325943150845L;
	private Integer codigo;
	private FechamentoFinanceiroCentroResultadoVO fechamentoFinanceiroCentroResultado;
	private FechamentoFinanceiroContaVO fechamentoFinanceiroConta;

	public Integer getCodigo() {
		if(codigo == null){
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public FechamentoFinanceiroCentroResultadoVO getFechamentoFinanceiroCentroResultado() {
		if(fechamentoFinanceiroCentroResultado == null){
			fechamentoFinanceiroCentroResultado = new FechamentoFinanceiroCentroResultadoVO();
		}
		return fechamentoFinanceiroCentroResultado;
	}

	public void setFechamentoFinanceiroCentroResultado(
			FechamentoFinanceiroCentroResultadoVO fechamentoFinanceiroCentroResultado) {
		this.fechamentoFinanceiroCentroResultado = fechamentoFinanceiroCentroResultado;
	}

	public FechamentoFinanceiroContaVO getFechamentoFinanceiroConta() {
		if(fechamentoFinanceiroConta == null){
			fechamentoFinanceiroConta = new FechamentoFinanceiroContaVO();
		}
		return fechamentoFinanceiroConta;
	}

	public void setFechamentoFinanceiroConta(FechamentoFinanceiroContaVO fechamentoFinanceiroConta) {
		this.fechamentoFinanceiroConta = fechamentoFinanceiroConta;
	}

}
