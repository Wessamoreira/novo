package negocio.comuns.financeiro;

import java.util.Date;

import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.financeiro.enumerador.SituacaoTransacaoEnum;
import negocio.comuns.financeiro.enumerador.TipoOrigemOperadoraCodigoRetornoDCC;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoCartaoOperadoraCartaoEnum;
import negocio.comuns.utilitarias.dominios.TipoOrigemContaReceber;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;

/**
 * 
 * @author Victor Hugo de Paula Costa 5.0.4.0 06/04/2016
 *
 *
 */
public class TransacaoCartaoOnlineVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private String requisicao;
	private String retornoRequisicao;
	private Date dataTransacao;
	private UsuarioVO responsavelTransacao;
	private FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO formaPagamentoNegociacaoRecebimentoCartaoCreditoVO;
	private String chavePedido;
	private String chaveTransacao;
	private TipoPessoa tipoPessoa;
	private PessoaVO pessoaVO;
	private ParceiroVO parceiroVO;
	private FornecedorVO fornecedorVO;
	private String matricula;
	private String mensagem;
	private String contaReceberRecebimento;
	private SituacaoTransacaoEnum situacaoTransacao;
	private TipoCartaoOperadoraCartaoEnum tipoCartao;

	//	Atributos Provenientes do Retorno da tansação
	private TipoOrigemOperadoraCodigoRetornoDCC tipoOrigemOperadoraCodigoRetornoDCC;
	private Integer codigoOrigemOperadoraCodigoRetornoDCC;
	
	// OBJETOS PREENCHIDOS NA RECORRÊNCIA - DCC
	private Integer codigoContaReceber;
	private String nossoNumero;
	private MatriculaPeriodoVO matriculaPeriodoVO;
	private TipoOrigemContaReceber tipoOrigem;
	private String parcelaContaReceber;
	private Boolean transacaoProvenienteRecorrencia;
	private Boolean jobExecutadaManualmente;
	private CartaoCreditoDebitoRecorrenciaPessoaVO cartaoCreditoDebitoRecorrenciaPessoaVO;
	private String nomeCustomer;
	
	//Atributo Transient criado para apresentação em consulta
	private ContaReceberVO contaReceberVO;
	private String mensagemTransacao;
	private CieloCodigoRetornoVO cieloCodigoRetornoVO;

	public Integer getCodigo() {
		if(codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	
	public String getRequisicao() {
		if(requisicao == null) {
			requisicao = "";
		}
		return requisicao;
	}

	public void setRequisicao(String requisicao) {
		this.requisicao = requisicao;
	}

	public String getRetornoRequisicao() {
		if(retornoRequisicao == null) {
			retornoRequisicao = "";
		}
		return retornoRequisicao;
	}

	public void setRetornoRequisicao(String retornoRequisicao) {
		this.retornoRequisicao = retornoRequisicao;
	}

	public Date getDataTransacao() {
		if(dataTransacao == null) {
			dataTransacao = new Date();
		}
		return dataTransacao;
	}
	
	public String getDataTransacao_Apresentar() {
		return UteisData.getDataAno4Digitos(getDataTransacao());
		
	}

	public void setDataTransacao(Date dataTransacao) {
		this.dataTransacao = dataTransacao;
	}

	public UsuarioVO getResponsavelTransacao() {
		if(responsavelTransacao == null) {
			responsavelTransacao = new UsuarioVO();
		}
		return responsavelTransacao;
	}

	public void setResponsavelTransacao(UsuarioVO responsavelTransacao) {
		this.responsavelTransacao = responsavelTransacao;
	}

	public FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO() {
		if(formaPagamentoNegociacaoRecebimentoCartaoCreditoVO == null) {
			formaPagamentoNegociacaoRecebimentoCartaoCreditoVO = new FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO();
		}
		return formaPagamentoNegociacaoRecebimentoCartaoCreditoVO;
	}

	public void setFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO(FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO formaPagamentoNegociacaoRecebimentoCartaoCreditoVO) {
		this.formaPagamentoNegociacaoRecebimentoCartaoCreditoVO = formaPagamentoNegociacaoRecebimentoCartaoCreditoVO;
	}

	public String getChavePedido() {
		if(chavePedido == null) {
			chavePedido = "";
		}
		return chavePedido;
	}

	public void setChavePedido(String chavePedido) {
		this.chavePedido = chavePedido;
	}

	public String getChaveTransacao() {
		if(chaveTransacao == null) {
			chaveTransacao = "";
		}
		return chaveTransacao;
	}

	public void setChaveTransacao(String chaveTransacao) {
		this.chaveTransacao = chaveTransacao;
	}

	public TipoPessoa getTipoPessoa() {
		if(tipoPessoa == null) {
			tipoPessoa = TipoPessoa.ALUNO;
		}
		return tipoPessoa;
	}

	public void setTipoPessoa(TipoPessoa tipoPessoa) {
		this.tipoPessoa = tipoPessoa;
	}

	public PessoaVO getPessoaVO() {
		if(pessoaVO == null) {
			pessoaVO = new PessoaVO();
		}
		return pessoaVO;
	}

	public void setPessoaVO(PessoaVO pessoaVO) {
		this.pessoaVO = pessoaVO;
	}

	public ParceiroVO getParceiroVO() {
		if(parceiroVO == null) {
			parceiroVO = new ParceiroVO();
		}
		return parceiroVO;
	}

	public void setParceiroVO(ParceiroVO parceiroVO) {
		this.parceiroVO = parceiroVO;
	}

	public FornecedorVO getFornecedorVO() {
		if(fornecedorVO == null) {
			fornecedorVO = new FornecedorVO();
		}
		return fornecedorVO;
	}

	public void setFornecedorVO(FornecedorVO fornecedorVO) {
		this.fornecedorVO = fornecedorVO;
	}

	public String getMatricula() {
		if(matricula == null) {
			matricula = "";
		}
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public String getMensagem() {
		if(mensagem == null) {
			mensagem = "";
		}
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	
	private ContaReceberRecebimentoVO contaReceberRecebimentoVO;

	public ContaReceberRecebimentoVO getContaReceberRecebimentoVO() {
		if (contaReceberRecebimentoVO == null) {
			contaReceberRecebimentoVO = new ContaReceberRecebimentoVO();
		}
		return contaReceberRecebimentoVO;
	}

	public void setContaReceberRecebimentoVO(ContaReceberRecebimentoVO contaReceberRecebimentoVO) {
		this.contaReceberRecebimentoVO = contaReceberRecebimentoVO;
	}

	public String getContaReceberRecebimento() {
		if (contaReceberRecebimento == null) {
			contaReceberRecebimento = "";
		}
		return contaReceberRecebimento;
	}

	public void setContaReceberRecebimento(String contaReceberRecebimento) {
		this.contaReceberRecebimento = contaReceberRecebimento;
	}

	public SituacaoTransacaoEnum getSituacaoTransacao() {
		if (situacaoTransacao == null) {
			situacaoTransacao = SituacaoTransacaoEnum.APROVADO;
		}
		return situacaoTransacao;
	}

	public void setSituacaoTransacao(SituacaoTransacaoEnum situacaoTransacao) {
		this.situacaoTransacao = situacaoTransacao;
	}
	
	private String cartao;

	public String getCartao() {
		if (cartao == null) {
			cartao = "";
		}
		return cartao;
	}

	public void setCartao(String cartao) {
		this.cartao = cartao;
	}
	
	private String numeroParcela;
	private Double valorParcela;
	private Date dataVencimento;

	public String getNumeroParcela() {
		if (numeroParcela == null) {
			numeroParcela = "";
		}
		return numeroParcela;
	}

	public void setNumeroParcela(String numeroParcela) {
		this.numeroParcela = numeroParcela;
	}

	public Double getValorParcela() {
		if (valorParcela == null) {
			valorParcela = 0.0;
		}
		return valorParcela;
	}
	
	public String getValorParcela_Apresentar() {
		return Uteis.formatarDecimal(getValorParcela(), "#,##0.00");
	}

	public void setValorParcela(Double valorParcela) {
		this.valorParcela = valorParcela;
	}

	public Date getDataVencimento() {
		if (dataVencimento == null) {
			dataVencimento = new Date();
		}
		return dataVencimento;
	}

	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public TipoCartaoOperadoraCartaoEnum getTipoCartao() {
		if (tipoCartao ==  null) {
			tipoCartao = TipoCartaoOperadoraCartaoEnum.CARTAO_CREDITO;
		}
		return tipoCartao;
	}

	public void setTipoCartao(TipoCartaoOperadoraCartaoEnum tipoCartao) {
		this.tipoCartao = tipoCartao;
	}

	public MatriculaPeriodoVO getMatriculaPeriodoVO() {
		if (matriculaPeriodoVO == null) {
			matriculaPeriodoVO = new MatriculaPeriodoVO();
		}
		return matriculaPeriodoVO;
	}

	public void setMatriculaPeriodoVO(MatriculaPeriodoVO matriculaPeriodoVO) {
		this.matriculaPeriodoVO = matriculaPeriodoVO;
	}

	public TipoOrigemContaReceber getTipoOrigem() {
		return tipoOrigem;
	}

	public void setTipoOrigem(TipoOrigemContaReceber tipoOrigem) {
		this.tipoOrigem = tipoOrigem;
	}

	public String getParcelaContaReceber() {
		if (parcelaContaReceber == null) {
			parcelaContaReceber = "";
		}
		return parcelaContaReceber;
	}

	public void setParcelaContaReceber(String parcelaContaReceber) {
		this.parcelaContaReceber = parcelaContaReceber;
	}

	public TipoOrigemOperadoraCodigoRetornoDCC getTipoOrigemOperadoraCodigoRetornoDCC() {
		if (tipoOrigemOperadoraCodigoRetornoDCC == null) {
			tipoOrigemOperadoraCodigoRetornoDCC = TipoOrigemOperadoraCodigoRetornoDCC.CIELO;
		}
		return tipoOrigemOperadoraCodigoRetornoDCC;
	}

	public void setTipoOrigemOperadoraCodigoRetornoDCC(TipoOrigemOperadoraCodigoRetornoDCC tipoOrigemOperadoraCodigoRetornoDCC) {
		this.tipoOrigemOperadoraCodigoRetornoDCC = tipoOrigemOperadoraCodigoRetornoDCC;
	}

	public Integer getCodigoOrigemOperadoraCodigoRetornoDCC() {
		if (codigoOrigemOperadoraCodigoRetornoDCC == null) {
			codigoOrigemOperadoraCodigoRetornoDCC = 0;
		}
		return codigoOrigemOperadoraCodigoRetornoDCC;
	}

	public void setCodigoOrigemOperadoraCodigoRetornoDCC(Integer codigoOrigemOperadoraCodigoRetornoDCC) {
		this.codigoOrigemOperadoraCodigoRetornoDCC = codigoOrigemOperadoraCodigoRetornoDCC;
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

	public String getMensagemTransacao() {
		if (mensagemTransacao == null) {
			mensagemTransacao = "";
		}
		return mensagemTransacao;
	}

	public void setMensagemTransacao(String mensagemTransacao) {
		this.mensagemTransacao = mensagemTransacao;
	}

	public Boolean getTransacaoProvenienteRecorrencia() {
		if (transacaoProvenienteRecorrencia == null) {
			transacaoProvenienteRecorrencia = false;
		}
		return transacaoProvenienteRecorrencia;
	}

	public void setTransacaoProvenienteRecorrencia(Boolean transacaoProvenienteRecorrencia) {
		this.transacaoProvenienteRecorrencia = transacaoProvenienteRecorrencia;
	}

	public Boolean getJobExecutadaManualmente() {
		if (jobExecutadaManualmente == null) {
			jobExecutadaManualmente = false;
		}
		return jobExecutadaManualmente;
	}

	public void setJobExecutadaManualmente(Boolean jobExecutadaManualmente) {
		this.jobExecutadaManualmente = jobExecutadaManualmente;
	}

	public Integer getCodigoContaReceber() {
		if (codigoContaReceber == null) {
			codigoContaReceber = 0;
		}
		return codigoContaReceber;
	}

	public void setCodigoContaReceber(Integer codigoContaReceber) {
		this.codigoContaReceber = codigoContaReceber;
	}

	public String getNossoNumero() {
		if (nossoNumero == null) {
			nossoNumero = "";
		}
		return nossoNumero;
	}

	public void setNossoNumero(String nossoNumero) {
		this.nossoNumero = nossoNumero;
	}

	public CartaoCreditoDebitoRecorrenciaPessoaVO getCartaoCreditoDebitoRecorrenciaPessoaVO() {
		if (cartaoCreditoDebitoRecorrenciaPessoaVO == null) {
			cartaoCreditoDebitoRecorrenciaPessoaVO = new CartaoCreditoDebitoRecorrenciaPessoaVO();
		}
		return cartaoCreditoDebitoRecorrenciaPessoaVO;
	}

	public void setCartaoCreditoDebitoRecorrenciaPessoaVO(CartaoCreditoDebitoRecorrenciaPessoaVO cartaoCreditoDebitoRecorrenciaPessoaVO) {
		this.cartaoCreditoDebitoRecorrenciaPessoaVO = cartaoCreditoDebitoRecorrenciaPessoaVO;
	}

	public String getNomeCustomer() {
		if (nomeCustomer == null) {
			nomeCustomer = "";
		}
		return nomeCustomer;
	}

	public void setNomeCustomer(String nomeCustomer) {
		this.nomeCustomer = nomeCustomer;
	}

	public CieloCodigoRetornoVO getCieloCodigoRetornoVO() {
		if (cieloCodigoRetornoVO == null) {
			cieloCodigoRetornoVO = new CieloCodigoRetornoVO();
		}
		return cieloCodigoRetornoVO;
	}

	public void setCieloCodigoRetornoVO(CieloCodigoRetornoVO cieloCodigoRetornoVO) {
		this.cieloCodigoRetornoVO = cieloCodigoRetornoVO;
	}
	
}
