package relatorio.negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.financeiro.ContaReceberVO;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

public class ContaReceberRelVO {

	private String nomeTurma;
	private String tipoPessoa;
	private Date data;
	private String tipoOrigem;
	private String situacao;
	private String descricaoPagamento;
	private Double valor;
	private Double valorReceberCalculado;
	private Double valorBaseContaReceber;
	private Double valorCusteadoContaReceber;
	private Double valorDesconto;
	private Double valorRecebido;
	private Double juro;
	private Double juroPorcentagem;
	private Double multaPorcentagem;
	private String parcela;
	private String origemNegociacaoReceber;
	private String nomePessoa;
	private String nomeFornecedor;
	private String nrDocumento;
	private Double multa;
	private Double acrescimo;
	private Integer codigo;
	private String linhaDigitavelCodigoBarras;
	private String codigoBarra;
	private String nomeParceiro;
	private String recebimentoBancario;
	private String reconheceuDivida;
	private String numeroContaCorrente;
	private String digito;
	private String usuarioResponsavel;
	private Date dataNegociacaoRecebimento;
	private Date dataVencimento;
	private Integer codigoFormaPagamento;
	private String nomeFormaPagamento;
	private String matricula;
	private String nossoNumero;
	private Double valorAcrescimoJuroMulta;
	private Double valorDescontoCalculadoPrimeiraFaixaDescontos;
	private Double valorDescontoFixoCalculado;
	private Double descontoPrevistoInstituicao;
	private Double descontoFixo;
	private List<ContaReceberQuadroFormaRecebimentoRelVO> listaQuadroFormaRecebimento;
	private Integer codigoCentroReceita;
	private String nomeCentroReceita;
	private String identificadorCentroReceita;
	private ContaReceberVO contaReceberVO;
	private Double valorDescontoConvenio;
	private List<ContaReceberDescricaoDescontosRelVO> contaReceberDescricaoDescontosRelVOs;
	private String nomeResponsavelFinanceiro;

	public ContaReceberRelVO() {
		setCodigo(0);
		setCodigoBarra("");
		setData(new Date());
		setDataNegociacaoRecebimento(new Date());
		setDescricaoPagamento("");
		setDigito("");
		setJuro(0.0);
		setJuroPorcentagem(0.0);
		setMulta(0.0);
		setMultaPorcentagem(0.0);
		setNomeParceiro("");
		setNomePessoa("");
		setNrDocumento("");
		setNumeroContaCorrente("");
		setOrigemNegociacaoReceber("");
		setParcela("");
		setRecebimentoBancario("");
		setReconheceuDivida("");
		setSituacao("");
		setTipoOrigem("");
		setTipoPessoa("");
		setUsuarioResponsavel("");
		setValor(0.0);
		setValorDesconto(0.0);
		setValorRecebido(0.0);
		setValorDescontoCalculadoPrimeiraFaixaDescontos(0.0);
		setDataVencimento(new Date());
		setNomeTurma("");
	}
	
	public JRDataSource getContaReceberDescricaoDescontosRelVO() {
		return new JRBeanArrayDataSource(getContaReceberDescricaoDescontosRelVOs().toArray());
	}

	public String getTipoPessoa() {
		return tipoPessoa;
	}

	public void setTipoPessoa(String tipoPessoa) {
		this.tipoPessoa = tipoPessoa;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getTipoOrigem() {
		return tipoOrigem;
	}

	public void setTipoOrigem(String tipoOrigem) {
		this.tipoOrigem = tipoOrigem;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public String getDescricaoPagamento() {
		return descricaoPagamento;
	}

	public void setDescricaoPagamento(String descricaoPagamento) {
		this.descricaoPagamento = descricaoPagamento;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public Double getValorDesconto() {
		return valorDesconto;
	}

	public void setValorDesconto(Double valorDesconto) {
		this.valorDesconto = valorDesconto;
	}

	public Double getValorRecebido() {
		return valorRecebido;
	}

	public void setValorRecebido(Double valorRecebido) {
		this.valorRecebido = valorRecebido;
	}

	public Double getJuro() {
		if (juro == null) {
			juro = 0.0;
		}
		return juro;
	}

	public void setJuro(Double juro) {
		this.juro = juro;
	}

	public Double getJuroPorcentagem() {
		return juroPorcentagem;
	}

	public void setJuroPorcentagem(Double juroPorcentagem) {
		this.juroPorcentagem = juroPorcentagem;
	}

	public Double getMultaPorcentagem() {
		return multaPorcentagem;
	}

	public void setMultaPorcentagem(Double multaPorcentagem) {
		this.multaPorcentagem = multaPorcentagem;
	}

	public String getParcela() {
		return parcela;
	}

	public void setParcela(String parcela) {
		this.parcela = parcela;
	}

	public String getOrigemNegociacaoReceber() {
		return origemNegociacaoReceber;
	}

	public void setOrigemNegociacaoReceber(String origemNegociacaoReceber) {
		this.origemNegociacaoReceber = origemNegociacaoReceber;
	}

	public String getNomePessoa() {
		return nomePessoa;
	}

	public void setNomePessoa(String nomePessoa) {
		this.nomePessoa = nomePessoa;
	}

	public String getNrDocumento() {
		return nrDocumento;
	}

	public void setNrDocumento(String nrDocumento) {
		this.nrDocumento = nrDocumento;
	}

	public Double getMulta() {
		if (multa == null) {
			multa = 0.0;
		}
		return multa;
	}

	public void setMulta(Double multa) {
		this.multa = multa;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getLinhaDigitavelCodigoBarras() {
		return linhaDigitavelCodigoBarras;
	}

	public void setLinhaDigitavelCodigoBarras(String linhaDigitavelCodigoBarras) {
		this.linhaDigitavelCodigoBarras = linhaDigitavelCodigoBarras;
	}

	public String getCodigoBarra() {
		return codigoBarra;
	}

	public void setCodigoBarra(String codigoBarra) {
		this.codigoBarra = codigoBarra;
	}

	public String getNomeParceiro() {
		return nomeParceiro;
	}

	public void setNomeParceiro(String nomeParceiro) {
		this.nomeParceiro = nomeParceiro;
	}

	public String getRecebimentoBancario() {
		return recebimentoBancario;
	}

	public void setRecebimentoBancario(String recebimentoBancario) {
		this.recebimentoBancario = recebimentoBancario;
	}

	public String getReconheceuDivida() {
		return reconheceuDivida;
	}

	public void setReconheceuDivida(String reconheceuDivida) {
		this.reconheceuDivida = reconheceuDivida;
	}

	public String getNumeroContaCorrente() {
		return numeroContaCorrente;
	}

	public void setNumeroContaCorrente(String numeroContaCorrente) {
		this.numeroContaCorrente = numeroContaCorrente;
	}

	public String getDigito() {
		return digito;
	}

	public void setDigito(String digito) {
		this.digito = digito;
	}

	public String getUsuarioResponsavel() {
		return usuarioResponsavel;
	}

	public void setUsuarioResponsavel(String usuarioResponsavel) {
		this.usuarioResponsavel = usuarioResponsavel;
	}

	public Date getDataNegociacaoRecebimento() {
		return dataNegociacaoRecebimento;
	}

	public void setDataNegociacaoRecebimento(Date dataNegociacaoRecebimento) {
		this.dataNegociacaoRecebimento = dataNegociacaoRecebimento;
	}

	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public Date getDataVencimento() {
		return dataVencimento;
	}

	public void setNomeTurma(String nomeTurma) {
		this.nomeTurma = nomeTurma;
	}

	public String getNomeTurma() {
		return nomeTurma;
	}

	public Integer getCodigoFormaPagamento() {
		return codigoFormaPagamento;
	}

	public void setCodigoFormaPagamento(Integer codigoFormaPagamento) {
		this.codigoFormaPagamento = codigoFormaPagamento;
	}

	public String getNomeFormaPagamento() {
		return nomeFormaPagamento;
	}

	public void setNomeFormaPagamento(String nomeFormaPagamento) {
		this.nomeFormaPagamento = nomeFormaPagamento;
	}

	public JRDataSource getListaQuadroFormaRecebimentoJRDataSource() {
		return new JRBeanArrayDataSource(getListaQuadroFormaRecebimento().toArray());
	}

	public List<ContaReceberQuadroFormaRecebimentoRelVO> getListaQuadroFormaRecebimento() {
		if (listaQuadroFormaRecebimento == null) {
			listaQuadroFormaRecebimento = new ArrayList<ContaReceberQuadroFormaRecebimentoRelVO>(0);
		}
		return listaQuadroFormaRecebimento;
	}

	public void setListaQuadroFormaRecebimento(List<ContaReceberQuadroFormaRecebimentoRelVO> listaQuadroFormaRecebimento) {
		this.listaQuadroFormaRecebimento = listaQuadroFormaRecebimento;
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	/**
	 * @return the acrescimo
	 */
	public Double getAcrescimo() {
		if (acrescimo == null) {
			acrescimo = 0.0;
		}
		return acrescimo;
	}

	/**
	 * @param acrescimo
	 *            the acrescimo to set
	 */
	public void setAcrescimo(Double acrescimo) {
		this.acrescimo = acrescimo;
	}

	/**
	 * @return the valorAcrescimoJutoMulta
	 */
	public Double getValorAcrescimoJuroMulta() {
		if (valorAcrescimoJuroMulta == null) {
			valorAcrescimoJuroMulta = 0.0;
		}
		return valorAcrescimoJuroMulta;
	}

	/**
	 * @param valorAcrescimoJutoMulta
	 *            the valorAcrescimoJutoMulta to set
	 */
	public void setValorAcrescimoJuroMulta(Double valorAcrescimoJuroMulta) {
		this.valorAcrescimoJuroMulta = valorAcrescimoJuroMulta;
	}

	public Double getValorDescontoCalculadoPrimeiraFaixaDescontos() {
		return valorDescontoCalculadoPrimeiraFaixaDescontos;
	}

	public void setValorDescontoCalculadoPrimeiraFaixaDescontos(Double valorDescontoCalculadoPrimeiraFaixaDescontos) {
		this.valorDescontoCalculadoPrimeiraFaixaDescontos = valorDescontoCalculadoPrimeiraFaixaDescontos;
	}

	public Double getDescontoPrevistoInstituicao() {
		if (descontoPrevistoInstituicao == null) {
			descontoPrevistoInstituicao = 0.0;
		}
		return descontoPrevistoInstituicao;
	}

	public void setDescontoPrevistoInstituicao(Double descontoPrevistoInstituicao) {
		this.descontoPrevistoInstituicao = descontoPrevistoInstituicao;
	}

	public Double getDescontoFixo() {
		if (descontoFixo == null) {
			descontoFixo = 0.0;
		}
		return descontoFixo;
	}

	public void setDescontoFixo(Double descontoFixo) {
		this.descontoFixo = descontoFixo;
	}

	public Double getValorDescontoFixoCalculado() {
		if (valorDescontoFixoCalculado == null) {
			valorDescontoFixoCalculado = 0.0;
		}
		return valorDescontoFixoCalculado;
	}

	public void setValorDescontoFixoCalculado(Double valorDescontoFixoCalculado) {
		this.valorDescontoFixoCalculado = valorDescontoFixoCalculado;
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

	public String getNomeFornecedor() {
		if (nomeFornecedor == null) {
			nomeFornecedor = "";
		}
		return nomeFornecedor;
	}

	public void setNomeFornecedor(String nomeFornecedor) {
		this.nomeFornecedor = nomeFornecedor;
	}

	public Integer getCodigoCentroReceita() {
		if (codigoCentroReceita == null) {
			codigoCentroReceita = 0;
		}
		return codigoCentroReceita;
	}

	public void setCodigoCentroReceita(Integer codigoCentroReceita) {
		this.codigoCentroReceita = codigoCentroReceita;
	}

	public String getNomeCentroReceita() {
		if (nomeCentroReceita == null) {
			nomeCentroReceita = "";
		}
		return nomeCentroReceita;
	}

	public void setNomeCentroReceita(String nomeCentroReceita) {
		this.nomeCentroReceita = nomeCentroReceita;
	}

	public String getIdentificadorCentroReceita() {
		if (identificadorCentroReceita == null) {
			identificadorCentroReceita = "";
		}
		return identificadorCentroReceita;
	}

	public void setIdentificadorCentroReceita(String identificadorCentroReceita) {
		this.identificadorCentroReceita = identificadorCentroReceita;
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

	/**
	 * @return the valorBaseContaReceber
	 */
	public Double getValorBaseContaReceber() {
		if (valorBaseContaReceber == null) {
			valorBaseContaReceber = 0.0;
		}
		return valorBaseContaReceber;
	}

	/**
	 * @param valorBaseContaReceber
	 *            the valorBaseContaReceber to set
	 */
	public void setValorBaseContaReceber(Double valorBaseContaReceber) {
		this.valorBaseContaReceber = valorBaseContaReceber;
	}

	/**
	 * @return the valorCusteadoContaReceber
	 */
	public Double getValorCusteadoContaReceber() {
		if (valorCusteadoContaReceber == null) {
			valorCusteadoContaReceber = 0.0;
		}
		return valorCusteadoContaReceber;
	}

	/**
	 * @param valorCusteadoContaReceber
	 *            the valorCusteadoContaReceber to set
	 */
	public void setValorCusteadoContaReceber(Double valorCusteadoContaReceber) {
		this.valorCusteadoContaReceber = valorCusteadoContaReceber;
	}

	/**
	 * @return the valorReceberCalculado
	 */
	public Double getValorReceberCalculado() {
		if (valorReceberCalculado == null) {
			valorReceberCalculado = 0.0;
		}
		return valorReceberCalculado;
	}

	/**
	 * @param valorReceberCalculado
	 *            the valorReceberCalculado to set
	 */
	public void setValorReceberCalculado(Double valorReceberCalculado) {
		this.valorReceberCalculado = valorReceberCalculado;
	}

	public Double getValorDescontoConvenio() {
		if (valorDescontoConvenio == null) {
			valorDescontoConvenio = 0.0;
		}
		return valorDescontoConvenio;
	}

	public void setValorDescontoConvenio(Double valorDescontoConvenio) {
		this.valorDescontoConvenio = valorDescontoConvenio;
	}

	public List<ContaReceberDescricaoDescontosRelVO> getContaReceberDescricaoDescontosRelVOs() {
		if (contaReceberDescricaoDescontosRelVOs == null) {
			contaReceberDescricaoDescontosRelVOs = new ArrayList<ContaReceberDescricaoDescontosRelVO>();
		}
		return contaReceberDescricaoDescontosRelVOs;
	}

	public void setContaReceberDescricaoDescontosRelVOs(List<ContaReceberDescricaoDescontosRelVO> contaReceberDescricaoDescontosRelVOs) {
		this.contaReceberDescricaoDescontosRelVOs = contaReceberDescricaoDescontosRelVOs;
	}

	public String getNomeResponsavelFinanceiro() {
		if (nomeResponsavelFinanceiro == null) {
			nomeResponsavelFinanceiro = "";
		}
		return nomeResponsavelFinanceiro;
	}

	public void setNomeResponsavelFinanceiro(String nomeResponsavelFinanceiro) {
		this.nomeResponsavelFinanceiro = nomeResponsavelFinanceiro;
	}

}
