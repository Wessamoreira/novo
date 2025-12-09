package negocio.comuns.financeiro;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.Uteis;

public class ProcessamentoArquivoRetornoParceiroExcelVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -244531164390036095L;
	private Integer codigo;
	private ProcessamentoArquivoRetornoParceiroAlunoVO processamentoArquivoRetornoParceiroAlunoVO;
	private ContaReceberVO contaReceberVO;
	private Double valorConta;
	private Date dataCompetencia;
	
	

	public ProcessamentoArquivoRetornoParceiroAlunoVO getProcessamentoArquivoRetornoParceiroAlunoVO() {
		if (processamentoArquivoRetornoParceiroAlunoVO == null) {
			processamentoArquivoRetornoParceiroAlunoVO = new ProcessamentoArquivoRetornoParceiroAlunoVO();
		}
		return processamentoArquivoRetornoParceiroAlunoVO;
	}

	public void setProcessamentoArquivoRetornoParceiroAlunoVO(ProcessamentoArquivoRetornoParceiroAlunoVO processamentoArquivoRetornoParceiroAlunoVO) {
		this.processamentoArquivoRetornoParceiroAlunoVO = processamentoArquivoRetornoParceiroAlunoVO;
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

	public ContaReceberVO getContaReceberVO() {
		if (contaReceberVO == null) {
			contaReceberVO = new ContaReceberVO();
		}
		return contaReceberVO;
	}

	public void setContaReceberVO(ContaReceberVO contaReceberVO) {
		this.contaReceberVO = contaReceberVO;
	}

	public Double getValorConta() {
		if (valorConta == null) {
			valorConta = 0.0;
		}
		return valorConta;
	}

	public void setValorConta(Double valorConta) {
		this.valorConta = valorConta;
	}

	public Date getDataCompetencia() {
		if (dataCompetencia == null) {
			dataCompetencia = new Date();
		}
		return dataCompetencia;
	}

	public void setDataCompetencia(Date dataCompetencia) {
		this.dataCompetencia = dataCompetencia;
	}
	
	public String getDataCompetencia_Apresentar() {
		if (getDataCompetencia() == null) {
			return "";
		}
		return (Uteis.getData(getDataCompetencia()));
	}
	
	public String getContaNaoLocalizada_Apresentar() {
		if (!isContaReceberExistente()) {
			return "Conta Não Localizada";
		}
		return "";
	}
	
	public boolean isContaReceberExistente(){
		return Uteis.isAtributoPreenchido(getContaReceberVO());
	}
	
	public boolean isValorDiferenteEntreContasAceita(){
		return (!getContaReceberVO().getValor().equals(getValorConta()) && ((getContaReceberVO().getValor() > getValorConta() && (getContaReceberVO().getValor() - getValorConta()) <= new Double(0.1)) || (getContaReceberVO().getValor() < getValorConta() && (getValorConta() - getContaReceberVO().getValor()) <= new Double(0.1))));
	}
	
	public boolean isValorDiferenteEntreContas(){
		return (!getContaReceberVO().getValor().equals(getValorConta()) && ((getContaReceberVO().getValor() > getValorConta() && (getContaReceberVO().getValor() - getValorConta()) > new Double(0.1)) || (getContaReceberVO().getValor() < getValorConta() && (getValorConta() - getContaReceberVO().getValor()) > new Double(0.1))));
	}	
	

}
