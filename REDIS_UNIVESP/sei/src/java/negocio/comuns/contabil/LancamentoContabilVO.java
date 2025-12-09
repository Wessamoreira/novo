package negocio.comuns.contabil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.compras.CategoriaProdutoVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.contabil.enumeradores.SituacaoLancamentoContabilEnum;
import negocio.comuns.contabil.enumeradores.TipoOrigemLancamentoContabilEnum;
import negocio.comuns.contabil.enumeradores.TipoPlanoContaEnum;
import negocio.comuns.contabil.enumeradores.TipoValorLancamentoContabilEnum;
import negocio.comuns.faturamento.nfe.ImpostoVO;
import negocio.comuns.faturamento.nfe.NotaFiscalEntradaVO;
import negocio.comuns.financeiro.BancoVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.ContaPagarVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.ParceiroVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoSacado;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;

/**
 * 
 * @author PedroOtimize
 *
 */
public class LancamentoContabilVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7279293817731871619L;
	private Integer codigo;
	private IntegracaoContabilVO integracaoContabilVO;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private PlanoContaVO planoContaVO;
	private ContaCorrenteVO contaCorrenteVO;
	private Date dataRegistro;
	private Date dataCompensacao;
	private String nomeLancamento;
	private Double valor;
	private TipoSacado tipoSacado;
	private FornecedorVO fornecedorVO;
	private FuncionarioVO funcionarioVO;
	private BancoVO bancoVO;
	private PessoaVO pessoaVO;
	private ParceiroVO parceiroVO;
	private SituacaoLancamentoContabilEnum situacaoLancamentoContabilEnum;
	private TipoOrigemLancamentoContabilEnum tipoOrigemLancamentoContabilEnum;
	private String codOrigem;
	private TipoValorLancamentoContabilEnum tipoValorLancamentoContabilEnum;
	private List<LancamentoContabilCentroNegocioVO> listaCentroNegocioAdministrativo;
	private List<LancamentoContabilCentroNegocioVO> listaCentroNegocioAcademico;
	private List<LancamentoContabilCentroNegocioVO> listaLancamentoContabilCentroNegocioVO;
	private TipoPlanoContaEnum tipoPlanoConta;
	private CategoriaProdutoVO categoriaProdutoVO;
	private ImpostoVO impostoVO;
	

	/**
	 * transient
	 */
	private String nomeSacado;
	private String cnpjSacado;
	private String naturezaLancamento;
	private String indTitulo;
	private String viaArrecadacao;
	private String tipoArrecadacao;
	private String bancoArrecadacao;
	private String agenciaArrecadacao;
	private String contaArrecadacao;
	private String digitoContaArrecadacao;
	private NotaFiscalEntradaVO notaFiscalEntradaVO;
	private ContaReceberVO contaReceberVO;
	private ContaPagarVO contaPagarVO;

	public void recalcularRaterioLancamentoContabil() {
		for (LancamentoContabilCentroNegocioVO lccn : getListaCentroNegocioAcademico()) {
			lccn.setValor(Uteis.arrendondarForcando2CadasDecimais((getValor() * lccn.getPercentual()) / 100));
		}
		for (LancamentoContabilCentroNegocioVO lccn : getListaCentroNegocioAdministrativo()) {
			lccn.setValor(Uteis.arrendondarForcando2CadasDecimais((getValor() * lccn.getPercentual()) / 100));
		}
		validarTotalizadoDoLancamentoContabilCentroNegociacao(getValor());
	}

	public void validarTotalizadoDoLancamentoContabilCentroNegociacao(Double valorPagamento) {
		validarTotalizadoresValor(valorPagamento);
		validarTotalizadoresPercentual();
	}

	private void validarTotalizadoresValor(Double valorPagamento) {
		if (Uteis.isAtributoPreenchido(getListaCentroNegocioAcademico())) {
			if (getTotalCentroNegocioAcademico() < valorPagamento) {
				LancamentoContabilCentroNegocioVO lccn = getListaCentroNegocioAcademico().get(getListaCentroNegocioAcademico().size() - 1);
				lccn.setValor(lccn.getValor() + (valorPagamento - getTotalCentroNegocioAcademico()));
				getListaCentroNegocioAcademico().set(getListaCentroNegocioAcademico().size() - 1, lccn);
			} else if (getTotalCentroNegocioAcademico() > valorPagamento) {
				LancamentoContabilCentroNegocioVO lccn = getListaCentroNegocioAcademico().get(getListaCentroNegocioAcademico().size() - 1);
				lccn.setValor(lccn.getValor() - (getTotalCentroNegocioAcademico() - valorPagamento));
				getListaCentroNegocioAcademico().set(getListaCentroNegocioAcademico().size() - 1, lccn);
			}
		}
		if (Uteis.isAtributoPreenchido(getListaCentroNegocioAdministrativo())) {
			if (getTotalCentroNegocioAdministrativo() < valorPagamento) {
				LancamentoContabilCentroNegocioVO lccn = getListaCentroNegocioAdministrativo().get(getListaCentroNegocioAdministrativo().size() - 1);
				lccn.setValor(lccn.getValor() + (valorPagamento - getTotalCentroNegocioAdministrativo()));
				getListaCentroNegocioAdministrativo().set(getListaCentroNegocioAdministrativo().size() - 1, lccn);
			} else if (getTotalCentroNegocioAdministrativo() > valorPagamento) {
				LancamentoContabilCentroNegocioVO lccn = getListaCentroNegocioAdministrativo().get(getListaCentroNegocioAdministrativo().size() - 1);
				lccn.setValor(lccn.getValor() - (getTotalCentroNegocioAdministrativo() - valorPagamento));
				getListaCentroNegocioAdministrativo().set(getListaCentroNegocioAdministrativo().size() - 1, lccn);
			}
		}
	}

	private void validarTotalizadoresPercentual() {
		Double valorPagamento = 100.00;
		if (Uteis.isAtributoPreenchido(getListaCentroNegocioAcademico())) {
			if (getPercentualCentroNegocioAcademico() < valorPagamento) {
				LancamentoContabilCentroNegocioVO lccn = getListaCentroNegocioAcademico().get(getListaCentroNegocioAcademico().size() - 1);
				lccn.setPercentual(lccn.getPercentual() + (valorPagamento - getPercentualCentroNegocioAcademico()));
				getListaCentroNegocioAcademico().set(getListaCentroNegocioAcademico().size() - 1, lccn);
			} else if (getTotalCentroNegocioAcademico() > valorPagamento) {
				LancamentoContabilCentroNegocioVO lccn = getListaCentroNegocioAcademico().get(getListaCentroNegocioAcademico().size() - 1);
				lccn.setPercentual(lccn.getPercentual() - (getPercentualCentroNegocioAcademico() - valorPagamento));
				getListaCentroNegocioAcademico().set(getListaCentroNegocioAcademico().size() - 1, lccn);
			}
		}
		if (Uteis.isAtributoPreenchido(getListaCentroNegocioAdministrativo())) {
			if (getPercentualCentroNegocioAdministrativo() < valorPagamento) {
				LancamentoContabilCentroNegocioVO lccn = getListaCentroNegocioAdministrativo().get(getListaCentroNegocioAdministrativo().size() - 1);
				lccn.setPercentual(lccn.getPercentual() + (valorPagamento - getPercentualCentroNegocioAdministrativo()));
				getListaCentroNegocioAdministrativo().set(getListaCentroNegocioAdministrativo().size() - 1, lccn);
			} else if (getTotalCentroNegocioAdministrativo() > valorPagamento) {
				LancamentoContabilCentroNegocioVO lccn = getListaCentroNegocioAdministrativo().get(getListaCentroNegocioAdministrativo().size() - 1);
				lccn.setPercentual(lccn.getPercentual() - (getPercentualCentroNegocioAdministrativo() - valorPagamento));
				getListaCentroNegocioAdministrativo().set(getListaCentroNegocioAdministrativo().size() - 1, lccn);
			}
		}
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

	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if (unidadeEnsinoVO == null) {
			unidadeEnsinoVO = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoVO;
	}

	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}

	public PlanoContaVO getPlanoContaVO() {
		if (planoContaVO == null) {
			planoContaVO = new PlanoContaVO();
		}
		return planoContaVO;
	}

	public void setPlanoContaVO(PlanoContaVO planoContaVO) {
		this.planoContaVO = planoContaVO;
	}

	public ContaCorrenteVO getContaCorrenteVO() {
		if (contaCorrenteVO == null) {
			contaCorrenteVO = new ContaCorrenteVO();
		}
		return contaCorrenteVO;
	}

	public void setContaCorrenteVO(ContaCorrenteVO contaCorrenteVO) {
		this.contaCorrenteVO = contaCorrenteVO;
	}

	public Date getDataRegistro() {
		if (dataRegistro == null) {
			dataRegistro = new Date();
		}
		return dataRegistro;
	}

	public void setDataRegistro(Date dataRegistro) {
		this.dataRegistro = dataRegistro;
	}

	public Date getDataCompensacao() {
		return dataCompensacao;
	}

	public void setDataCompensacao(Date dataCompensacao) {
		this.dataCompensacao = dataCompensacao;
	}

	public String getNomeLancamento() {
		if (nomeLancamento == null) {
			nomeLancamento = "";
		}
		return nomeLancamento;
	}

	public void setNomeLancamento(String nomeLancamento) {
		this.nomeLancamento = nomeLancamento;
	}

	public String getHistoricoLancamento() {
		return getTipoOrigemLancamentoContabilEnum().isReceber() ? "Conta Receber Nosso Nº - " + getContaReceberVO().getNossoNumero() + " - Parc. " + getContaReceberVO().getParcela() + " - " + getNomeSacado() :
			   getTipoOrigemLancamentoContabilEnum().isPagar() ? "Conta Pagar Nº Doc. - " + getContaPagarVO().getNrDocumento() + " - Parc. " + getContaPagarVO().getParcela() + " - " + getNomeSacado() :
			   getTipoOrigemLancamentoContabilEnum().isNotaFiscalEntrada() && getTipoValorLancamentoContabilEnum().isNotaFiscalEntradaCategoriaProduto() ? "Vlr. ref. NOTA FISCAL Nº - " + getNotaFiscalEntradaVO().getNumero() + " - " + getNomeSacado() : 
			   getTipoOrigemLancamentoContabilEnum().isNotaFiscalEntrada() && getTipoValorLancamentoContabilEnum().isNotaFiscalEntradaImposto() ? "Tributo Retido ref. NOTA FISCAL Nº - " + getNotaFiscalEntradaVO().getNumero() + " - " + getNomeSacado() : 
			   getTipoOrigemLancamentoContabilEnum().isMovimentacaoFinanceira() ? "Mov. Finan. - "+getCodOrigem()+" ref. Conta corrente  - " + getContaCorrenteVO().getNomeApresentacaoSistema() :
			   getTipoOrigemLancamentoContabilEnum().isCartaoCredito() ? "Cartão de Crédito Baixa - " + getCodOrigem() + " - Sacado " + getNomeSacado() :
			   getTipoOrigemLancamentoContabilEnum().isNegocicaoContaPagar() && getTipoValorLancamentoContabilEnum().isJuro() ? "Neg. Conta Pagar - " + getCodOrigem() + " - Valor Referente ao Juro " : 
			   getTipoOrigemLancamentoContabilEnum().isNegocicaoContaPagar() && getTipoValorLancamentoContabilEnum().isMulta() ? "Neg. Conta Pagar - " + getCodOrigem() + " - Valor Referente a Multa ": 
			   getTipoOrigemLancamentoContabilEnum().isNegocicaoContaPagar() && getTipoValorLancamentoContabilEnum().isPagamento() ? "Neg. Conta Pagar - " + getCodOrigem() + " - Valor Referente ao Desconto " : "";
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

	public String getNomeSacado() {
		if (Uteis.isAtributoPreenchido(getTipoSacado()) && getTipoSacado().isParceiro()) {
			nomeSacado = getParceiroVO().getNome();
		} else if ((Uteis.isAtributoPreenchido(getTipoSacado()) && getTipoSacado().isFornecedor())) {
			nomeSacado = getFornecedorVO().getNome();
		} else if (Uteis.isAtributoPreenchido(getTipoSacado()) && getTipoSacado().isFuncionario()) {
			nomeSacado = getFuncionarioVO().getPessoa().getNome();
		} else if (Uteis.isAtributoPreenchido(getTipoSacado()) && getTipoSacado().isBanco()) {
			nomeSacado = getBancoVO().getNome();
		} else if (Uteis.isAtributoPreenchido(getTipoSacado()) && (getTipoSacado().isAluno() || getTipoSacado().isResponsavelFinanceiro() || getTipoSacado().isCandidato() || getTipoSacado().isRequerente())) {
			nomeSacado = getPessoaVO().getNome();
		}
		return nomeSacado;
	}

	public void setNomeSacado(String nomeSacado) {
		this.nomeSacado = nomeSacado;
	}

	public String getCnpjSacado() {
		if (Uteis.isAtributoPreenchido(getTipoSacado()) && getTipoSacado().isParceiro()) {
			cnpjSacado = getParceiroVO().getCNPJ();
		} else if ((Uteis.isAtributoPreenchido(getTipoSacado()) && getTipoSacado().isFornecedor())) {
			cnpjSacado = getFornecedorVO().getCNPJ();
		} else if (Uteis.isAtributoPreenchido(getTipoSacado()) && getTipoSacado().isFuncionario()) {
			cnpjSacado = getFuncionarioVO().getPessoa().getCPF();
		} else if (Uteis.isAtributoPreenchido(getTipoSacado()) && getTipoSacado().isBanco()) {
			cnpjSacado = "";
		} else if (Uteis.isAtributoPreenchido(getTipoSacado()) && (getTipoSacado().isAluno() || getTipoSacado().isResponsavelFinanceiro())) {
			cnpjSacado = getPessoaVO().getCPF();
		}
		return cnpjSacado;
	}

	public void setCnpjSacado(String cnpjSacado) {
		this.cnpjSacado = cnpjSacado;
	}

	public String getTipoSacado_Apresentar() {
		if (Uteis.isAtributoPreenchido(getTipoSacado())) {
			return getTipoSacado().getDescricao();
		}
		return "";
	}

	public TipoSacado getTipoSacado() {
		return tipoSacado;
	}

	public void setTipoSacado(TipoSacado tipoSacado) {
		this.tipoSacado = tipoSacado;
	}

	public FornecedorVO getFornecedorVO() {
		if (fornecedorVO == null) {
			fornecedorVO = new FornecedorVO();
		}
		return fornecedorVO;
	}

	public void setFornecedorVO(FornecedorVO fornecedorVO) {
		this.fornecedorVO = fornecedorVO;
	}

	public FuncionarioVO getFuncionarioVO() {
		if (funcionarioVO == null) {
			funcionarioVO = new FuncionarioVO();
		}
		return funcionarioVO;
	}

	public void setFuncionarioVO(FuncionarioVO funcionarioVO) {
		this.funcionarioVO = funcionarioVO;
	}

	public BancoVO getBancoVO() {
		if (bancoVO == null) {
			bancoVO = new BancoVO();
		}
		return bancoVO;
	}

	public void setBancoVO(BancoVO bancoVO) {
		this.bancoVO = bancoVO;
	}

	public PessoaVO getPessoaVO() {
		if (pessoaVO == null) {
			pessoaVO = new PessoaVO();
		}
		return pessoaVO;
	}

	public void setPessoaVO(PessoaVO pessoaVO) {
		this.pessoaVO = pessoaVO;
	}

	public ParceiroVO getParceiroVO() {
		if (parceiroVO == null) {
			parceiroVO = new ParceiroVO();
		}
		return parceiroVO;
	}

	public void setParceiroVO(ParceiroVO parceiroVO) {
		this.parceiroVO = parceiroVO;
	}

	public IntegracaoContabilVO getIntegracaoContabilVO() {
		if (integracaoContabilVO == null) {
			integracaoContabilVO = new IntegracaoContabilVO();
		}
		return integracaoContabilVO;
	}

	public void setIntegracaoContabilVO(IntegracaoContabilVO integracaoContabilVO) {
		this.integracaoContabilVO = integracaoContabilVO;
	}

	public TipoOrigemLancamentoContabilEnum getTipoOrigemLancamentoContabilEnum() {
		return tipoOrigemLancamentoContabilEnum;
	}

	public void setTipoOrigemLancamentoContabilEnum(TipoOrigemLancamentoContabilEnum tipoOrigemLancamentoContabilEnum) {
		this.tipoOrigemLancamentoContabilEnum = tipoOrigemLancamentoContabilEnum;
	}

	public String getCodOrigem() {
		if (codOrigem == null) {
			codOrigem = "";
		}
		return codOrigem;
	}

	public void setCodOrigem(String codOrigem) {
		this.codOrigem = codOrigem;
	}

	public TipoValorLancamentoContabilEnum getTipoValorLancamentoContabilEnum() {
		return tipoValorLancamentoContabilEnum;
	}

	public void setTipoValorLancamentoContabilEnum(TipoValorLancamentoContabilEnum tipoValorLancamentoContabilEnum) {
		this.tipoValorLancamentoContabilEnum = tipoValorLancamentoContabilEnum;
	}
	
	public SituacaoLancamentoContabilEnum getSituacaoLancamentoContabilEnum() {
		return situacaoLancamentoContabilEnum;
	}

	public void setSituacaoLancamentoContabilEnum(SituacaoLancamentoContabilEnum situacaoLancamentoContabilEnum) {
		this.situacaoLancamentoContabilEnum = situacaoLancamentoContabilEnum;
	}

	public String getNaturezaLancamento() {
		if (getTipoPlanoConta().isCredito()) {
			naturezaLancamento = "C";
		} else if (getTipoPlanoConta().isDebito()) {
			naturezaLancamento = "D";
		}
		return naturezaLancamento;
	}

	public void setNaturezaLancamento(String naturezaLancamento) {
		this.naturezaLancamento = naturezaLancamento;
	}

	public String getIndTitulo() {
		if (getTipoOrigemLancamentoContabilEnum().isPagar()) {
			indTitulo = "P";
		} else {
			indTitulo = "R";
		}
		return indTitulo;
	}

	public void setIndTitulo(String indTitulo) {
		this.indTitulo = indTitulo;
	}

	public List<LancamentoContabilCentroNegocioVO> getListaCentroNegocioAdministrativo() {
		if (listaCentroNegocioAdministrativo == null) {
			listaCentroNegocioAdministrativo = new ArrayList<>();
		}
		return listaCentroNegocioAdministrativo;
	}

	public void setListaCentroNegocioAdministrativo(List<LancamentoContabilCentroNegocioVO> listaCentroNegocioAdministrativo) {
		this.listaCentroNegocioAdministrativo = listaCentroNegocioAdministrativo;
	}

	public List<LancamentoContabilCentroNegocioVO> getListaCentroNegocioAcademico() {
		if (listaCentroNegocioAcademico == null) {
			listaCentroNegocioAcademico = new ArrayList<>();
		}
		return listaCentroNegocioAcademico;
	}

	public void setListaCentroNegocioAcademico(List<LancamentoContabilCentroNegocioVO> listaCentroNegocioAcademico) {
		this.listaCentroNegocioAcademico = listaCentroNegocioAcademico;
	}

	public List<LancamentoContabilCentroNegocioVO> getListaLancamentoContabilCentroNegocioVO() {
		if (listaLancamentoContabilCentroNegocioVO == null) {
			listaLancamentoContabilCentroNegocioVO = new ArrayList<>();
		}
		return listaLancamentoContabilCentroNegocioVO;
	}

	public void setListaLancamentoContabilCentroNegocioVO(List<LancamentoContabilCentroNegocioVO> listaLancamentoContabilCentroNegocioVO) {
		this.listaLancamentoContabilCentroNegocioVO = listaLancamentoContabilCentroNegocioVO;
	}

	public Double getTotalCentroNegocioAcademico() {
		return getListaCentroNegocioAcademico().stream().map(LancamentoContabilCentroNegocioVO::getValor).reduce(0D, (a, b) -> Uteis.arrendondarForcando2CadasDecimais(a + b));
	}

	public Double getTotalCentroNegocioAdministrativo() {
		return getListaCentroNegocioAdministrativo().stream().map(LancamentoContabilCentroNegocioVO::getValor).reduce(0D, (a, b) -> Uteis.arrendondarForcando2CadasDecimais(a + b));
	}

	public Double getPercentualCentroNegocioAcademico() {
		return getListaCentroNegocioAcademico().stream().map(LancamentoContabilCentroNegocioVO::getPercentual).reduce(0D, (a, b) -> Uteis.arrendondarForcando2CadasDecimais(a + b));
	}

	public Double getPercentualCentroNegocioAdministrativo() {
		return getListaCentroNegocioAdministrativo().stream().map(LancamentoContabilCentroNegocioVO::getPercentual).reduce(0D, (a, b) -> Uteis.arrendondarForcando2CadasDecimais(a + b));
	}

	public boolean equalsCampoSelecaoLista(LancamentoContabilVO obj) {
		return (getTipoValorLancamentoContabilEnum().equals(obj.getTipoValorLancamentoContabilEnum())
				&& getTipoOrigemLancamentoContabilEnum().equals(obj.getTipoOrigemLancamentoContabilEnum())
				&& getCodOrigem().equals(obj.getCodOrigem())
				&& getUnidadeEnsinoVO().getCodigo().equals(obj.getUnidadeEnsinoVO().getCodigo())
				&& getTipoPlanoConta().equals(obj.getTipoPlanoConta())
				&& getPlanoContaVO().getCodigo().equals(obj.getPlanoContaVO().getCodigo())
				&& getTipoSacado().name().equals(obj.getTipoSacado().name())
				&& getFuncionarioVO().getCodigo().equals(obj.getFuncionarioVO().getCodigo())
				&& getFornecedorVO().getCodigo().equals(obj.getFornecedorVO().getCodigo())
				&& getBancoVO().getCodigo().equals(obj.getBancoVO().getCodigo())
				&& getPessoaVO().getCodigo().equals(obj.getPessoaVO().getCodigo())
				&& getParceiroVO().getCodigo().equals(obj.getParceiroVO().getCodigo())
				&& getContaCorrenteVO().getCodigo().equals(obj.getContaCorrenteVO().getCodigo())
				&& getCategoriaProdutoVO().getCodigo().equals(obj.getCategoriaProdutoVO().getCodigo())
				&& getImpostoVO().getCodigo().equals(obj.getImpostoVO().getCodigo()));

	}

	public TipoPlanoContaEnum getTipoPlanoConta() {
		if (tipoPlanoConta == null) {
			tipoPlanoConta = TipoPlanoContaEnum.CREDITO;
		}
		return tipoPlanoConta;
	}

	public void setTipoPlanoConta(TipoPlanoContaEnum tipoPlanoConta) {
		this.tipoPlanoConta = tipoPlanoConta;
	}

	public boolean isViaArrecadacaoCaixa() {
		return getViaArrecadacao().equals("1");
	}

	public String getViaArrecadacao() {
		if (Uteis.isAtributoPreenchido(getContaCorrenteVO()) && getContaCorrenteVO().getContaCaixa()) {
			viaArrecadacao = "1";
		} else if (Uteis.isAtributoPreenchido(getContaCorrenteVO()) && !getContaCorrenteVO().getContaCaixa()) {
			viaArrecadacao = "2";
		} else {
			viaArrecadacao = "";
		}
		return viaArrecadacao;
	}

	public void setViaArrecadacao(String viaArrecadacao) {
		this.viaArrecadacao = viaArrecadacao;
	}

	public String getBancoArrecadacao() {
		if (isViaArrecadacaoCaixa()) {
			bancoArrecadacao = "001";
		} else {
			bancoArrecadacao = getContaCorrenteVO().getAgencia().getBanco().getNrBanco();
		}
		return bancoArrecadacao;
	}

	public void setBancoArrecadacao(String bancoArrecadacao) {
		this.bancoArrecadacao = bancoArrecadacao;
	}

	public String getAgenciaArrecadacao() {
		if (isViaArrecadacaoCaixa()) {
			agenciaArrecadacao = "";
		} else {
			agenciaArrecadacao = getContaCorrenteVO().getAgencia().getNumeroAgencia();
		}
		return agenciaArrecadacao;
	}

	public void setAgenciaArrecadacao(String agenciaArrecadacao) {
		this.agenciaArrecadacao = agenciaArrecadacao;
	}

	public String getContaArrecadacao() {
		if (isViaArrecadacaoCaixa()) {
			contaArrecadacao = "";
		} else {
			contaArrecadacao = getContaCorrenteVO().getNumero();
		}
		return contaArrecadacao;
	}

	public void setContaArrecadacao(String contaArrecadacao) {
		this.contaArrecadacao = contaArrecadacao;
	}

	public String getDigitoContaArrecadacao() {
		if (isViaArrecadacaoCaixa()) {
			digitoContaArrecadacao = "";
		} else {
			digitoContaArrecadacao = getContaCorrenteVO().getDigito();
		}
		return digitoContaArrecadacao;
	}

	public void setDigitoContaArrecadacao(String digitoContaArrecadacao) {
		this.digitoContaArrecadacao = digitoContaArrecadacao;
	}

	public String getTipoArrecadacao() {
		if (Uteis.isAtributoPreenchido(getTipoValorLancamentoContabilEnum()) && getTipoValorLancamentoContabilEnum().isContaReceber()) {
			tipoArrecadacao = "1";
		} else if (Uteis.isAtributoPreenchido(getTipoValorLancamentoContabilEnum()) && getTipoValorLancamentoContabilEnum().isJuro()) {
			tipoArrecadacao = "3";
		} else if (Uteis.isAtributoPreenchido(getTipoValorLancamentoContabilEnum()) && getTipoValorLancamentoContabilEnum().isMulta()) {
			tipoArrecadacao = "4";
		} else if (Uteis.isAtributoPreenchido(getTipoValorLancamentoContabilEnum()) && getTipoValorLancamentoContabilEnum().isDesconto()) {
			tipoArrecadacao = "5";
		} else {
			tipoArrecadacao = "";
		}
		return tipoArrecadacao;
	}

	public void setTipoArrecadacao(String tipoArrecadacao) {
		this.tipoArrecadacao = tipoArrecadacao;
	}

	public CategoriaProdutoVO getCategoriaProdutoVO() {
		categoriaProdutoVO = Optional.ofNullable(categoriaProdutoVO).orElse(new CategoriaProdutoVO());
		return categoriaProdutoVO;
	}

	public void setCategoriaProdutoVO(CategoriaProdutoVO categoriaProdutoVO) {
		this.categoriaProdutoVO = categoriaProdutoVO;
	}

	public ImpostoVO getImpostoVO() {
		impostoVO = Optional.ofNullable(impostoVO).orElse(new ImpostoVO());
		return impostoVO;
	}

	public void setImpostoVO(ImpostoVO impostoVO) {
		this.impostoVO = impostoVO;
	}

	public NotaFiscalEntradaVO getNotaFiscalEntradaVO() {
		if (notaFiscalEntradaVO == null) {
			notaFiscalEntradaVO = new NotaFiscalEntradaVO();
		}
		return notaFiscalEntradaVO;
	}

	public void setNotaFiscalEntradaVO(NotaFiscalEntradaVO notaFiscalEntradaVO) {
		this.notaFiscalEntradaVO = notaFiscalEntradaVO;
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

	public ContaPagarVO getContaPagarVO() {
		if (contaPagarVO == null) {
			contaPagarVO = new ContaPagarVO();
		}
		return contaPagarVO;
	}

	public void setContaPagarVO(ContaPagarVO contaPagarVO) {
		this.contaPagarVO = contaPagarVO;
	}

	public boolean isNotaFiscalEntradaCategoriaProduto() {
		return Uteis.isAtributoPreenchido(getTipoOrigemLancamentoContabilEnum()) && getTipoOrigemLancamentoContabilEnum().isNotaFiscalEntrada() && Uteis.isAtributoPreenchido(getCategoriaProdutoVO());
	}

	public boolean isNotaFiscalEntradaImposto() {
		return Uteis.isAtributoPreenchido(getTipoOrigemLancamentoContabilEnum()) && getTipoOrigemLancamentoContabilEnum().isNotaFiscalEntrada() && Uteis.isAtributoPreenchido(getImpostoVO());
	}

	public Boolean getFiltrarLancamentoContabilReceber(IntegracaoContabilVO integracaoContabilVO) {
		if (getTipoOrigemLancamentoContabilEnum().isReceber()) {
			if (Uteis.isAtributoPreenchido(integracaoContabilVO.getNossaNumeroFiltro()) &&
					(getContaReceberVO().getNossoNumero().isEmpty() || !getContaReceberVO().getNossoNumero().equals(integracaoContabilVO.getNossaNumeroFiltro()))) {
				return false;
			}
			return getFiltrarLancamentoContabil(integracaoContabilVO);
		}
		return false;
	}

	public Boolean getFiltrarLancamentoContabilPagar(IntegracaoContabilVO integracaoContabilVO) {
		if (getTipoOrigemLancamentoContabilEnum().isPagar()) {
			if (Uteis.isAtributoPreenchido(integracaoContabilVO.getNossaNumeroFiltro()) &&
					(getContaPagarVO().getNossoNumero().equals(0L) || !getContaPagarVO().getNossoNumero().toString().contains(integracaoContabilVO.getNossaNumeroFiltro()))) {
				return false;
			}
			if (Uteis.isAtributoPreenchido(integracaoContabilVO.getNumeroNotaFiscalEntradaFiltro()) &&
					(getContaPagarVO().getNumeroNotaFiscalEntrada().isEmpty() || !getContaPagarVO().getNumeroNotaFiscalEntrada().contains(integracaoContabilVO.getNumeroNotaFiscalEntradaFiltro()))) {
				return false;
			}
			return getFiltrarLancamentoContabil(integracaoContabilVO);
		}
		return false;
	}

	public Boolean getFiltrarLancamentoContabilNotaFiscal(IntegracaoContabilVO integracaoContabilVO) {
		if (getTipoOrigemLancamentoContabilEnum().isNotaFiscalEntrada()) {
			if (Uteis.isAtributoPreenchido(integracaoContabilVO.getNumeroNotaFiscalEntradaFiltro())
					&& (getNotaFiscalEntradaVO().getNumero().equals(0L) || !getNotaFiscalEntradaVO().getNumero().toString().contains(integracaoContabilVO.getNumeroNotaFiscalEntradaFiltro()))) {
				return false;
			}
			return getFiltrarLancamentoContabil(integracaoContabilVO);
		}
		return false;
	}

	public Boolean getFiltrarLancamentoContabilMovFinanceira(IntegracaoContabilVO integracaoContabilVO) {
		if (getTipoOrigemLancamentoContabilEnum().isMovimentacaoFinanceira()) {
			return getFiltrarLancamentoContabil(integracaoContabilVO);
		}
		return false;
	}
	
	public Boolean getFiltrarLancamentoContabilCartaoCredito(IntegracaoContabilVO integracaoContabilVO) {
		if (getTipoOrigemLancamentoContabilEnum().isCartaoCredito()) {
			return getFiltrarLancamentoContabil(integracaoContabilVO);
		}
		return false;
	}

	public Boolean getFiltrarLancamentoContabilNegContaPagar(IntegracaoContabilVO integracaoContabilVO) {
		if (getTipoOrigemLancamentoContabilEnum().isNegocicaoContaPagar()) {
			return getFiltrarLancamentoContabil(integracaoContabilVO);
		}
		return false;
	}

	private boolean getFiltrarLancamentoContabil(IntegracaoContabilVO integracaoContabilVO) {
		if (Uteis.isAtributoPreenchido(integracaoContabilVO.getCodigoOrigemFiltro()) &&
				(getCodOrigem().isEmpty() || !getCodOrigem().contains(integracaoContabilVO.getCodigoOrigemFiltro()))) {
			return false;
		}
		if (Uteis.isAtributoPreenchido(integracaoContabilVO.getSacadoFiltro()) &&
				(getNomeSacado().isEmpty() || !UteisTexto.retirarAcentuacaoAndCaracteresEspeciasRegex(getNomeSacado().toLowerCase()).contains(UteisTexto.retirarAcentuacaoAndCaracteresEspeciasRegex(integracaoContabilVO.getSacadoFiltro().toLowerCase())))) {
			return false;
		}
		if (Uteis.isAtributoPreenchido(integracaoContabilVO.getNumeroContaCorrenteFiltro()) &&
				(getContaCorrenteVO().getNumero().isEmpty() || !getContaCorrenteVO().getNumero().contains(integracaoContabilVO.getNumeroContaCorrenteFiltro()))) {
			return false;
		}
		if (Uteis.isAtributoPreenchido(integracaoContabilVO.getPlanoContaFiltro()) &&
				((integracaoContabilVO.getTipoCampoPlanoContaFiltro().equals("descricao") && (getPlanoContaVO().getDescricao().isEmpty() || !UteisTexto.retirarAcentuacaoAndCaracteresEspeciasRegex(getPlanoContaVO().getDescricao().toLowerCase()).contains(UteisTexto.retirarAcentuacaoAndCaracteresEspeciasRegex(integracaoContabilVO.getPlanoContaFiltro().toLowerCase()))))
						||
						(integracaoContabilVO.getTipoCampoPlanoContaFiltro().equals("identificador") && (getPlanoContaVO().getIdentificadorPlanoConta().isEmpty() || !UteisTexto.retirarAcentuacaoAndCaracteresEspeciasRegex(getPlanoContaVO().getIdentificadorPlanoConta().toLowerCase()).contains(UteisTexto.retirarAcentuacaoAndCaracteresEspeciasRegex(integracaoContabilVO.getPlanoContaFiltro().toLowerCase()))))
						||
						(integracaoContabilVO.getTipoCampoPlanoContaFiltro().equals("codigoReduzido") && (getPlanoContaVO().getCodigoReduzido() == 0 || !getPlanoContaVO().getCodigoReduzido().toString().contains(integracaoContabilVO.getPlanoContaFiltro()))))) {
			return false;
		}
		return true;
	}

}
