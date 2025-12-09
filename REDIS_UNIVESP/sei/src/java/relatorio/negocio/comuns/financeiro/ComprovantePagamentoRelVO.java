package relatorio.negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.financeiro.ContaPagarNegociacaoPagamentoVO;
import negocio.comuns.financeiro.FormaPagamentoNegociacaoPagamentoVO;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

public class ComprovantePagamentoRelVO {

	private String nomeResponsavel;
	private String nomeAluno;
	private String matricula;
	private String turma;
	private Integer codigoNegociacaoPagamento;
	private List<ContaPagarNegociacaoPagamentoVO> listaContaPagarNegociacaoPagamentoVO;
	private List<FormaPagamentoNegociacaoPagamentoVO> listaFormaPagamentoNegociacaoPagamentoVO;
	private String parcelasPagasNrDocumentoDataVencimento;
	private String valorTotalPagamentoPorExtenso;
	private String cidadeDataPagamentoPorExtenso;
	private Double valorTotalPagamento;

	public ComprovantePagamentoRelVO() {
	}

	public String getNomeResponsavel() {
		return nomeResponsavel;
	}

	public void setNomeResponsavel(String nomeResponsavel) {
		this.nomeResponsavel = nomeResponsavel;
	}

	public String getNomeAluno() {
		if(nomeAluno == null) {
			nomeAluno = "";
		}
		return nomeAluno;
	}

	public void setNomeAluno(String nomeAluno) {
		this.nomeAluno = nomeAluno;
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public String getTurma() {
		return turma;
	}

	public void setTurma(String turma) {
		this.turma = turma;
	}

	public Integer getCodigoNegociacaoPagamento() {
		if (codigoNegociacaoPagamento == null) {
			codigoNegociacaoPagamento = 0;
		}
		return codigoNegociacaoPagamento;
	}

	public void setCodigoNegociacaoPagamento(Integer codigoNegociacaoPagamento) {
		this.codigoNegociacaoPagamento = codigoNegociacaoPagamento;
	}

	public JRDataSource getListaContaPagarNegociacaoPagamentoDataSource() {
		return new JRBeanArrayDataSource(getListaContaPagarNegociacaoPagamentoVO().toArray());
	}

	public JRDataSource getListaContaPagarNegociacaoPagamentoDataSource1() {
		return new JRBeanArrayDataSource(getListaContaPagarNegociacaoPagamentoVO().toArray());
	}

	public JRDataSource getListaFormaPagamentoNegociacaoPagamentoDataSource() {
		return new JRBeanArrayDataSource(getListaFormaPagamentoNegociacaoPagamentoVO().toArray());
	}

	public JRDataSource getListaFormaPagamentoNegociacaoPagamentoDataSource1() {
		return new JRBeanArrayDataSource(getListaFormaPagamentoNegociacaoPagamentoVO().toArray());
	}

	public List<ContaPagarNegociacaoPagamentoVO> getListaContaPagarNegociacaoPagamentoVO() {
		if (listaContaPagarNegociacaoPagamentoVO == null) {
			listaContaPagarNegociacaoPagamentoVO = new ArrayList<ContaPagarNegociacaoPagamentoVO>(0);
		}
		return listaContaPagarNegociacaoPagamentoVO;
	}

	public void setListaContaPagarNegociacaoPagamentoVO(List<ContaPagarNegociacaoPagamentoVO> listaContaPagarNegociacaoPagamentoVO) {
		this.listaContaPagarNegociacaoPagamentoVO = listaContaPagarNegociacaoPagamentoVO;
	}

	public List<FormaPagamentoNegociacaoPagamentoVO> getListaFormaPagamentoNegociacaoPagamentoVO() {
		if (listaFormaPagamentoNegociacaoPagamentoVO == null) {
			listaFormaPagamentoNegociacaoPagamentoVO = new ArrayList<FormaPagamentoNegociacaoPagamentoVO>(0);
		}
		return listaFormaPagamentoNegociacaoPagamentoVO;
	}

	public void setListaFormaPagamentoNegociacaoPagamentoVO(List<FormaPagamentoNegociacaoPagamentoVO> listaFormaPagamentoNegociacaoPagamentoVO) {
		this.listaFormaPagamentoNegociacaoPagamentoVO = listaFormaPagamentoNegociacaoPagamentoVO;
	}

	public String getParcelasPagasNrDocumentoDataVencimento() {
		return parcelasPagasNrDocumentoDataVencimento;
	}

	public void setParcelasPagasNrDocumentoDataVencimento(String parcelasPagasNrDocumentoDataVencimento) {
		this.parcelasPagasNrDocumentoDataVencimento = parcelasPagasNrDocumentoDataVencimento;
	}

	public String getValorTotalPagamentoPorExtenso() {
		if (valorTotalPagamentoPorExtenso == null) {
			valorTotalPagamentoPorExtenso = "";
		}
		return valorTotalPagamentoPorExtenso;
	}

	public void setValorTotalPagamentoPorExtenso(String valorTotalPagamentoPorExtenso) {
		this.valorTotalPagamentoPorExtenso = valorTotalPagamentoPorExtenso;
	}

	public String getCidadeDataPagamentoPorExtenso() {
		return cidadeDataPagamentoPorExtenso;
	}

	public void setCidadeDataPagamentoPorExtenso(String cidadeDataPagamentoPorExtenso) {
		this.cidadeDataPagamentoPorExtenso = cidadeDataPagamentoPorExtenso;
	}

	public Double getValorTotalPagamento() {
		if (valorTotalPagamento == null) {
			valorTotalPagamento = 0.0;
		}
		return valorTotalPagamento;
	}

	public void setValorTotalPagamento(Double valorTotalPagamento) {
		this.valorTotalPagamento = valorTotalPagamento;
	}

}
