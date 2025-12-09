package negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.BancoFactory;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.boleto.BoletoBanco;
import negocio.comuns.utilitarias.boleto.JBoletoBean;
import negocio.comuns.utilitarias.boleto.bancos.BancoBrasil;
import negocio.comuns.utilitarias.boleto.bancos.Banestes;
import negocio.comuns.utilitarias.boleto.bancos.Bradesco;
import negocio.comuns.utilitarias.boleto.bancos.CaixaEconomica;
import negocio.comuns.utilitarias.boleto.bancos.Hsbc;
import negocio.comuns.utilitarias.boleto.bancos.Itau;
import negocio.comuns.utilitarias.boleto.bancos.Santander;
import negocio.comuns.utilitarias.boleto.bancos.Sicred;
import negocio.comuns.utilitarias.boleto.bancos.Unibanco;
import negocio.comuns.utilitarias.dominios.SituacaoContaReceber;
import negocio.comuns.utilitarias.dominios.TipoPessoa;

public class ContaReceberAgrupadaVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2455794702292503387L;
	private Integer codigo;
	private Date dataAgrupamento;
	private UsuarioVO responsavelAgrupamento;
	private Date dataAlteracao;
	private UsuarioVO responsavelAlteracao;
	private ContaCorrenteVO contaCorrente;
	private UnidadeEnsinoVO unidadeEnsino;
	private SituacaoContaReceber situacao;
	private TipoPessoa tipoPessoa;
	private MatriculaVO matricula;

	private PessoaVO pessoa;

	private String nossoNumero;

	private Double valorTotalRecebido;
	private Double valorTotalJuro;
	private Double valorTotalMulta;
	private Double valorTotalAcrescimo;
	private Double valorTotalDesconto;
	private String descricao;
	private List<ContaReceberVO> contaReceberVOs;
	private Date dataVencimento;
	private Date dataRecebimento;
	private String codigoBarra;
	private String linhaDigitavelCodigoBarras;

	/**
	 * Usado para consulta
	 * 
	 * @return
	 */
	private Double valorTotal;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public Date getDataAgrupamento() {
		if (dataAgrupamento == null) {
			dataAgrupamento = new Date();
		}
		return dataAgrupamento;
	}

	public void setDataAgrupamento(Date dataAgrupamento) {
		this.dataAgrupamento = dataAgrupamento;
	}

	public UsuarioVO getResponsavelAgrupamento() {
		if (responsavelAgrupamento == null) {
			responsavelAgrupamento = new UsuarioVO();
		}
		return responsavelAgrupamento;
	}

	public void setResponsavelAgrupamento(UsuarioVO responsavelAgrupamento) {
		this.responsavelAgrupamento = responsavelAgrupamento;
	}

	public ContaCorrenteVO getContaCorrente() {
		if (contaCorrente == null) {
			contaCorrente = new ContaCorrenteVO();
		}
		return contaCorrente;
	}

	public void setContaCorrente(ContaCorrenteVO contaCorrente) {
		this.contaCorrente = contaCorrente;
	}

	public SituacaoContaReceber getSituacao() {
		if (situacao == null) {
			situacao = SituacaoContaReceber.A_RECEBER;
		}
		return situacao;
	}

	public void setSituacao(SituacaoContaReceber situacao) {
		this.situacao = situacao;
	}

	public TipoPessoa getTipoPessoa() {
		if (tipoPessoa == null) {
			tipoPessoa = TipoPessoa.ALUNO;
		}
		return tipoPessoa;
	}

	public void setTipoPessoa(TipoPessoa tipoPessoa) {
		this.tipoPessoa = tipoPessoa;
	}

	public MatriculaVO getMatricula() {
		if (matricula == null) {
			matricula = new MatriculaVO();
		}
		return matricula;
	}

	public void setMatricula(MatriculaVO matricula) {
		this.matricula = matricula;
	}

	public PessoaVO getPessoa() {
		if (pessoa == null) {
			pessoa = new PessoaVO();
		}
		return pessoa;
	}

	public void setPessoa(PessoaVO pessoa) {
		this.pessoa = pessoa;
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

	public Double getValorTotal() {
		if (valorTotal == null) {
			valorTotal = 0.0;
		}
		return valorTotal;
	}

	public Double getValorTotalRecebido() {
		if (valorTotalRecebido == null) {
			valorTotalRecebido = 0.0;
		}
		return valorTotalRecebido;
	}

	public void setValorTotalRecebido(Double valorTotalRecebido) {
		this.valorTotalRecebido = valorTotalRecebido;
	}

	public Double getValorTotalJuro() {
		if (valorTotalJuro == null) {
			valorTotalJuro = 0.0;
		}
		return valorTotalJuro;
	}

	public void setValorTotalJuro(Double valorTotalJuro) {
		this.valorTotalJuro = valorTotalJuro;
	}

	public Double getValorTotalMulta() {
		if (valorTotalMulta == null) {
			valorTotalMulta = 0.0;
		}
		return valorTotalMulta;
	}

	public void setValorTotalMulta(Double valorTotalMulta) {
		this.valorTotalMulta = valorTotalMulta;
	}

	public Double getValorTotalAcrescimo() {
		if (valorTotalAcrescimo == null) {
			valorTotalAcrescimo = 0.0;
		}
		return valorTotalAcrescimo;
	}

	public void setValorTotalAcrescimo(Double valorTotalAcrescimo) {
		this.valorTotalAcrescimo = valorTotalAcrescimo;
	}

	public Double getValorTotalDesconto() {
		if (valorTotalDesconto == null) {
			valorTotalDesconto = 0.0;
		}
		return valorTotalDesconto;
	}

	public void setValorTotalDesconto(Double valorTotalDesconto) {
		this.valorTotalDesconto = valorTotalDesconto;
	}

	public String getDescricao() {
		if (descricao == null) {
			descricao = "";
		}
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public List<ContaReceberVO> getContaReceberVOs() {
		if (contaReceberVOs == null) {
			contaReceberVOs = new ArrayList<ContaReceberVO>(0);
		}
		return contaReceberVOs;
	}

	public void setContaReceberVOs(List<ContaReceberVO> contaReceberVOs) {
		this.contaReceberVOs = contaReceberVOs;
	}

	public Date getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public Date getDataRecebimento() {
		return dataRecebimento;
	}

	public void setDataRecebimento(Date dataRecebimento) {
		this.dataRecebimento = dataRecebimento;
	}

	public UnidadeEnsinoVO getUnidadeEnsino() {
		if (unidadeEnsino == null) {
			unidadeEnsino = new UnidadeEnsinoVO();
		}
		return unidadeEnsino;
	}

	public void setUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}

	public Date getDataAlteracao() {
		return dataAlteracao;
	}

	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}

	public UsuarioVO getResponsavelAlteracao() {
		if (responsavelAlteracao == null) {
			responsavelAlteracao = new UsuarioVO();
		}
		return responsavelAlteracao;
	}

	public void setValorTotal(Double valorTotal) {
		this.valorTotal = valorTotal;
	}

	public void setResponsavelAlteracao(UsuarioVO responsavelAlteracao) {
		this.responsavelAlteracao = responsavelAlteracao;
	}

	public String getSacado() {
		if (getTipoPessoa().equals(TipoPessoa.ALUNO)) {
			return "Aluno(a): " + getPessoa().getNome() + " - " + getMatricula().getMatricula();
		}
		return "Resp. Finan.: " + getPessoa().getNome();
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void criarBoleto(ContaCorrenteVO contaCorrenteVO) throws Exception {
		try {
			JBoletoBean jBoletoBean = new JBoletoBean();
			jBoletoBean.setDataDocumento(Uteis.getDataAno4Digitos(getDataAgrupamento()));
			jBoletoBean.setDataProcessamento(Uteis.getDataAno4Digitos(new Date()));
			jBoletoBean.setDataVencimento(Uteis.getDataAno4Digitos(getDataVencimento()));
			jBoletoBean.setAgencia(contaCorrenteVO.getAgencia().getNumeroAgencia());
			jBoletoBean.setDvContaCorrente(contaCorrenteVO.getDigito());
			jBoletoBean.setContaCorrente(contaCorrenteVO.getNumero());
			jBoletoBean.setCarteira(contaCorrenteVO.getCarteira());
			jBoletoBean.setNumConvenio(contaCorrenteVO.getConvenio());
			jBoletoBean.setCedente(contaCorrenteVO.getCodigoCedente());
			jBoletoBean.setValorBoleto(String.valueOf(getValorTotal()));

			BoletoBanco banco = BancoFactory.getBoletoInstancia(contaCorrenteVO.getAgencia().getBanco().getNrBanco(), jBoletoBean);
			if (banco instanceof BancoBrasil) {
				jBoletoBean.setNossoNumero(String.valueOf(getNossoNumero()), 10);
			} else if (banco instanceof Unibanco) {
				jBoletoBean.setNossoNumero(String.valueOf(getNossoNumero()), 14);
			} else if (banco instanceof Hsbc) {
				jBoletoBean.setCodCliente(contaCorrenteVO.getCodigoCedente());
				jBoletoBean.setNossoNumero(String.valueOf(getNossoNumero()), 13);
			} else if (banco instanceof Santander) {
				jBoletoBean.setIOS("0");
				jBoletoBean.setCodCliente(contaCorrenteVO.getCodigoCedente());
				if (getNossoNumero().length() > 8) {
					jBoletoBean.setNossoNumero(String.valueOf(getNossoNumero()), getNossoNumero().length());
				} else {
					jBoletoBean.setNossoNumero(String.valueOf(getNossoNumero()), 8);
				}
			} else if (banco instanceof Bradesco) {
				jBoletoBean.setNossoNumero(String.valueOf(getNossoNumero()), 11);
			} else if (banco instanceof Itau) {
				jBoletoBean.setNossoNumero(String.valueOf(getNossoNumero()), 8);
			} else if (banco instanceof Banestes) {
				jBoletoBean.setNossoNumero(String.valueOf(getNossoNumero()), 17);
			} else if (banco instanceof Sicred) {
				jBoletoBean.setDvAgencia(contaCorrenteVO.getAgencia().getDigito());
				jBoletoBean.setNossoNumero(String.valueOf(getNossoNumero()), 9);
			} else if (banco instanceof CaixaEconomica) {
				jBoletoBean.setCedente(String.valueOf(contaCorrenteVO.getCodigoCedente().toString()));
				jBoletoBean.setNossoNumero(String.valueOf(getNossoNumero()), 15);
			} else {
				jBoletoBean.setNossoNumero(String.valueOf(getNossoNumero()), 14);
			}
			if (banco != null) {
				setCodigoBarra(banco.getCodigoBarras());
				setLinhaDigitavelCodigoBarras(banco.getLinhaDigitavel());
			}
		} catch (Exception e) {
			////System.out.println(e.getMessage());
			throw new Exception("Ocorreu um erro na geração do nosso número das contas a receber. Provavelmente o nr de carteira e/ou convênio estão errados!");
			// throw e;
		}
	}

	public String getCodigoBarra() {
		if (codigoBarra == null) {
			codigoBarra = "";
		}
		return codigoBarra;
	}

	public void setCodigoBarra(String codigoBarra) {
		this.codigoBarra = codigoBarra;
	}

	public String getLinhaDigitavelCodigoBarras() {
		if (linhaDigitavelCodigoBarras == null) {
			linhaDigitavelCodigoBarras = "";
		}
		return linhaDigitavelCodigoBarras;
	}

	public void setLinhaDigitavelCodigoBarras(String linhaDigitavelCodigoBarras) {
		this.linhaDigitavelCodigoBarras = linhaDigitavelCodigoBarras;
	}

}
