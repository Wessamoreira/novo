package webservice.servicos;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import negocio.comuns.financeiro.ContaReceberVO;

@XmlRootElement(name = "negociacaoRecebimento")
public class NegociacaoRecebimentoRSVO {

	private Double valorTotalAPagar;
	private Integer quantidadeParcelasAPagar;
	private List<BandeiraRSVO> bandeiraRSVOs;
	private Boolean possuiApenasUmaOpcaoBandeira = false;
	private List<FormaPagamentoMatriculaOnlineExternaRSVO> formaPagamentoMatriculaOnlineExternaRSVOs;
	private String matricula;
	private Integer codigoUnidadeEnsino;
	private Boolean pagamentoConfirmado = false;
	private Integer codigoMatriculaPeriodo;
	private String mensagem;
	private String linkDownloadComprovantePagamento;
	private MatriculaRSVO matriculaRSVO;
	private String linkDownloadBoleto;
	private Integer quantidadeLimiteParcela;
	private ContaReceberVO contaReceber;
	private Boolean apresentarOpcaoRecorrenciaAluno;
	

	@XmlElement(name = "valorTotalAPagar")
	public Double getValorTotalAPagar() {
		if(valorTotalAPagar == null) {
			valorTotalAPagar = 0.0;
		}
		return valorTotalAPagar;
	}

	public void setValorTotalAPagar(Double valorTotalAPagar) {
		this.valorTotalAPagar = valorTotalAPagar;
	}

	@XmlElement(name = "quantidadeParcelasAPagar")
	public Integer getQuantidadeParcelasAPagar() {
		if(quantidadeParcelasAPagar == null) {
			quantidadeParcelasAPagar = 0;
		}
		return quantidadeParcelasAPagar;
	}

	public void setQuantidadeParcelasAPagar(Integer quantidadeParcelasAPagar) {
		this.quantidadeParcelasAPagar = quantidadeParcelasAPagar;
	}

	@XmlElement(name = "bandeiraRSVOs")
	public List<BandeiraRSVO> getBandeiraRSVOs() {
		if(bandeiraRSVOs == null) {
			bandeiraRSVOs = new ArrayList<BandeiraRSVO>();
		}
		return bandeiraRSVOs;
	}

	public void setBandeiraRSVOs(List<BandeiraRSVO> bandeiraRSVOs) {
		this.bandeiraRSVOs = bandeiraRSVOs;
	}

	@XmlElement(name = "possuiApenasUmaOpcaoBandeira", required = false, defaultValue = "false")
	public Boolean getPossuiApenasUmaOpcaoBandeira() {
		return possuiApenasUmaOpcaoBandeira = getBandeiraRSVOs().size() > 1;
	}

	public void setPossuiApenasUmaOpcaoBandeira(Boolean possuiApenasUmaOpcaoBandeira) {
		this.possuiApenasUmaOpcaoBandeira = possuiApenasUmaOpcaoBandeira;
	}
						
	@XmlElement(name = "formaPagamentoMatriculaOnlineExternas")
	public List<FormaPagamentoMatriculaOnlineExternaRSVO> getFormaPagamentoMatriculaOnlineExternaRSVOs() {
		if(formaPagamentoMatriculaOnlineExternaRSVOs == null) {
			formaPagamentoMatriculaOnlineExternaRSVOs = new ArrayList<FormaPagamentoMatriculaOnlineExternaRSVO>();
		}
		return formaPagamentoMatriculaOnlineExternaRSVOs;
	}

	public void setFormaPagamentoMatriculaOnlineExternaRSVOs(List<FormaPagamentoMatriculaOnlineExternaRSVO> formaPagamentoMatriculaOnlineExternaRSVOs) {
		this.formaPagamentoMatriculaOnlineExternaRSVOs = formaPagamentoMatriculaOnlineExternaRSVOs;
	}

	@XmlElement(name = "matricula")
	public String getMatricula() {
		if(matricula == null) {
			matricula = "";
		}
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	@XmlElement(name = "codigoUnidadeEnsino")
	public Integer getCodigoUnidadeEnsino() {
		if(codigoUnidadeEnsino == null) {
			codigoUnidadeEnsino = 0;
		}
		return codigoUnidadeEnsino;
	}

	public void setCodigoUnidadeEnsino(Integer codigoUnidadeEnsino) {
		this.codigoUnidadeEnsino = codigoUnidadeEnsino;
	}

	@XmlElement(name = "pagamentoConfirmado", required = false, defaultValue = "false")
	public Boolean getPagamentoConfirmado() {
		if(pagamentoConfirmado == null) {
			pagamentoConfirmado = false;
		}
		return pagamentoConfirmado;
	}

	public void setPagamentoConfirmado(Boolean pagamentoConfirmado) {
		this.pagamentoConfirmado = pagamentoConfirmado;
	}

	@XmlElement(name = "codigoMatriculaPeriodo")
	public Integer getCodigoMatriculaPeriodo() {
		if(codigoMatriculaPeriodo == null) {
			codigoMatriculaPeriodo = 0;
		}
		return codigoMatriculaPeriodo;
	}

	public void setCodigoMatriculaPeriodo(Integer codigoMatriculaPeriodo) {
		this.codigoMatriculaPeriodo = codigoMatriculaPeriodo;
	}

	@XmlElement(name = "mensagem")
	public String getMensagem() {
		if(mensagem == null) {
			mensagem = "";
		}
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	@XmlElement(name = "linkDownloadComprovantePagamento")
	public String getLinkDownloadComprovantePagamento() {
		if(linkDownloadComprovantePagamento == null) {
			linkDownloadComprovantePagamento = "";
		}
		return linkDownloadComprovantePagamento;
	}

	public void setLinkDownloadComprovantePagamento(String linkDownloadComprovantePagamento) {
		this.linkDownloadComprovantePagamento = linkDownloadComprovantePagamento;
	}

	@XmlElement(name = "matriculaRSVO")
	public MatriculaRSVO getMatriculaRSVO() {
		if(matriculaRSVO == null) {
			matriculaRSVO = new MatriculaRSVO();
		}
		return matriculaRSVO;
	}

	public void setMatriculaRSVO(MatriculaRSVO matriculaRSVO) {
		this.matriculaRSVO = matriculaRSVO;
	}

	@XmlElement(name = "linkDownloadBoleto")
	public String getLinkDownloadBoleto() {
		if(linkDownloadBoleto == null) {
			linkDownloadBoleto = "";
		}
		return linkDownloadBoleto;
	}

	public void setLinkDownloadBoleto(String linkDownloadBoleto) {
		this.linkDownloadBoleto = linkDownloadBoleto;
	}

	public Integer getQuantidadeLimiteParcela() {
		if (quantidadeLimiteParcela == null) {
			quantidadeLimiteParcela = 1;
		}
		return quantidadeLimiteParcela;
	}

	public void setQuantidadeLimiteParcela(Integer quantidadeLimiteParcela) {
		this.quantidadeLimiteParcela = quantidadeLimiteParcela;
	}

	public ContaReceberVO getContaReceber() {
		if (contaReceber == null) {
			contaReceber = new ContaReceberVO();
		}
		return contaReceber;
	}

	public void setContaReceber(ContaReceberVO contaReceber) {
		this.contaReceber = contaReceber;
	}

	@XmlElement(name = "apresentarOpcaoRecorrenciaAluno")
	public Boolean getApresentarOpcaoRecorrenciaAluno() {
		if (apresentarOpcaoRecorrenciaAluno == null) {
			apresentarOpcaoRecorrenciaAluno = false;
		}
		return apresentarOpcaoRecorrenciaAluno;
	}

	public void setApresentarOpcaoRecorrenciaAluno(Boolean apresentarOpcaoRecorrenciaAluno) {
		this.apresentarOpcaoRecorrenciaAluno = apresentarOpcaoRecorrenciaAluno;
	}
	
	
	
}
