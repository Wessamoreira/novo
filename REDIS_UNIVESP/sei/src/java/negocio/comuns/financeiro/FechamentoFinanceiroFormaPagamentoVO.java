package negocio.comuns.financeiro;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.compras.FormaPagamentoVO;

public class FechamentoFinanceiroFormaPagamentoVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4940804803843045894L;
	private Integer codigo;
	private FechamentoFinanceiroContaVO fechamentoFinanceiroContaVO;
	private Double valor;
	private FormaPagamentoVO formaPagamento;
	private ContaCorrenteVO contaCorrente;
	private Date dataCompensacao;

	public Integer getCodigo() {
		if(codigo == null){
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}


	public Double getValor() {
		if(valor == null){
			valor = 0.0;
		}
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public FormaPagamentoVO getFormaPagamento() {
		if(formaPagamento == null){
			formaPagamento = new FormaPagamentoVO();
		}
		return formaPagamento;
	}

	public void setFormaPagamento(FormaPagamentoVO formaPagamento) {
		this.formaPagamento = formaPagamento;
	}


	public Date getDataCompensacao() {
		
		return dataCompensacao;
	}

	public void setDataCompensacao(Date dataCompensacao) {
		this.dataCompensacao = dataCompensacao;
	}

	public FechamentoFinanceiroContaVO getFechamentoFinanceiroContaVO() {
		if(fechamentoFinanceiroContaVO == null){
			fechamentoFinanceiroContaVO = new FechamentoFinanceiroContaVO();
		}
		return fechamentoFinanceiroContaVO;
	}

	public void setFechamentoFinanceiroContaVO(FechamentoFinanceiroContaVO fechamentoFinanceiroContaVO) {
		this.fechamentoFinanceiroContaVO = fechamentoFinanceiroContaVO;
	}

	public ContaCorrenteVO getContaCorrente() {
		if(contaCorrente == null){
			contaCorrente = new ContaCorrenteVO();
		}
		return contaCorrente;
	}

	public void setContaCorrente(ContaCorrenteVO contaCorrente) {
		this.contaCorrente = contaCorrente;
	}
	
	

}
