package negocio.comuns.financeiro;

import java.io.Serializable;
import java.util.Optional;

import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.compras.FormaPagamentoVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.financeiro.enumerador.OrigemExtratoContaCorrenteEnum;
import negocio.comuns.financeiro.enumerador.TipoFinanciamentoEnum;
import negocio.comuns.financeiro.enumerador.TipoSacadoExtratoContaCorrenteEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoFormaPagamento;
import negocio.comuns.utilitarias.dominios.TipoMovimentacaoFinanceira;

public class ParametrizarOperacoesAutomaticasConciliacaoItemVO extends SuperVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3976931787533659098L;
	private Integer codigo;
	private String nomeLancamento;
	private OrigemExtratoContaCorrenteEnum origemExtratoContaCorrenteEnum;
	private TipoMovimentacaoFinanceira tipoMovimentacaoFinanceira;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private ContaCorrenteVO contaCorrenteVO;
	private TipoFormaPagamento tipoFormaPagamento;
	private FormaPagamentoVO formaPagamentoVO;

	private TipoSacadoExtratoContaCorrenteEnum tipoSacado;
	private CentroReceitaVO centroReceitaVO;
	private FuncionarioVO funcionarioSacado;
	private FornecedorVO fornecedorSacado;
	private ParceiroVO parceiroSacado;
	private BancoVO bancoSacado;
	private OperadoraCartaoVO operadoraCartaoSacado;
	private OperadoraCartaoVO operadoraCartao;
	private TipoFinanciamentoEnum tipoFinanciamentoEnum;
	private Integer qtdeParcelasCartaoCredito;
	private ConfiguracaoFinanceiroCartaoVO configuracaoFinanceiroCartaoVO;
	private ConfiguracaoRecebimentoCartaoOnlineVO configuracaoRecebimentoCartaoOnlineVO;
	private String nomeCartaoCredito;
	private String numeroCartao;
	private Integer mesValidade;
	private Integer anoValidade;
	private String codigoVerificacao;
	private ContaCorrenteVO contaCorrenteDestinoVO;
	private CategoriaDespesaVO categoriaDespesaVO;
	private CentroResultadoVO centroResultadoAdministrativo;

	private String nomeSacado;

	public ContaCorrenteVO getContaCorrenteDestinoVO() {
		if (contaCorrenteDestinoVO == null) {
			contaCorrenteDestinoVO = new ContaCorrenteVO();
		}
		return contaCorrenteDestinoVO;
	}

	public void setContaCorrenteDestinoVO(ContaCorrenteVO contaCorrenteDestinoVO) {
		this.contaCorrenteDestinoVO = contaCorrenteDestinoVO;
	}

	public OrigemExtratoContaCorrenteEnum getOrigemExtratoContaCorrenteEnum() {
		return origemExtratoContaCorrenteEnum;
	}

	public void setOrigemExtratoContaCorrenteEnum(OrigemExtratoContaCorrenteEnum origemExtratoContaCorrenteEnum) {
		this.origemExtratoContaCorrenteEnum = origemExtratoContaCorrenteEnum;
	}

	public String getFavorecido_Apresentar() {
		switch (getOrigemExtratoContaCorrenteEnum()) {
		case MOVIMENTACAO_FINANCEIRA:
			return getFavorecidoMovimentacaoFinanceira();
		case PAGAMENTO:
			return getFavorecidoPagamento();
		case RECEBIMENTO:
			return getFavorecidoRecebimento();
		default:
			return "";
		}
	}

	public String getFavorecidoMovimentacaoFinanceira() {
		return "Conta Origem - " + getContaCorrenteVO().getNomeApresentacaoSistema() + " Para Destino - " + getContaCorrenteDestinoVO().getNomeApresentacaoSistema();
	}

	public String getFavorecidoPagamento() {
		switch (getTipoSacado()) {
		case BANCO:
			return "Banco - " + getBancoSacado().getNome();
		case FORNECEDOR:
			return "Fornecedor - " + getFornecedorSacado().getNome();
		case FUNCIONARIO_PROFESSOR:
			return "Funcionário - " + getFuncionarioSacado().getPessoa().getNome();
		case PARCEIRO:
			return "Parceiro - " + getParceiroSacado().getNome();
		default:
			return "";
		}
	}

	public String getFavorecidoRecebimento() {
		switch (getTipoSacado()) {
		case FORNECEDOR:
			return "Fornecedor - " + getFornecedorSacado().getNome();
		case PARCEIRO:
			return "Parceiro - " + getParceiroSacado().getNome();
		case FUNCIONARIO_PROFESSOR:
			return "Funcionário - " + getFuncionarioSacado().getPessoa().getNome();
		default:
			return "";
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

	public String getNomeLancamento() {
		if (nomeLancamento == null) {
			nomeLancamento = "";
		}
		return nomeLancamento;
	}

	public void setNomeLancamento(String nomeLancamento) {
		this.nomeLancamento = nomeLancamento;
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

	public TipoFormaPagamento getTipoFormaPagamento() {
		return tipoFormaPagamento;
	}

	public void setTipoFormaPagamento(TipoFormaPagamento tipoFormaPagamento) {
		this.tipoFormaPagamento = tipoFormaPagamento;
	}

	public boolean getExisteTipoFormaPagamento() {
		return Uteis.isAtributoPreenchido(getTipoFormaPagamento());
	}

	public CategoriaDespesaVO getCategoriaDespesaVO() {
		categoriaDespesaVO = Optional.ofNullable(categoriaDespesaVO).orElse(new CategoriaDespesaVO());
		return categoriaDespesaVO;
	}

	public void setCategoriaDespesaVO(CategoriaDespesaVO categoriaDespesaVO) {
		this.categoriaDespesaVO = categoriaDespesaVO;
	}

	public CentroResultadoVO getCentroResultadoAdministrativo() {
		centroResultadoAdministrativo = Optional.ofNullable(centroResultadoAdministrativo).orElse(new CentroResultadoVO());
		return centroResultadoAdministrativo;
	}

	public void setCentroResultadoAdministrativo(CentroResultadoVO centroResultadoAdministrativo) {
		this.centroResultadoAdministrativo = centroResultadoAdministrativo;
	}

	public CentroReceitaVO getCentroReceitaVO() {
		if (centroReceitaVO == null) {
			centroReceitaVO = new CentroReceitaVO();
		}
		return centroReceitaVO;
	}

	public void setCentroReceitaVO(CentroReceitaVO centroReceitaVO) {
		this.centroReceitaVO = centroReceitaVO;
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

	public FormaPagamentoVO getFormaPagamentoVO() {
		if (formaPagamentoVO == null) {
			formaPagamentoVO = new FormaPagamentoVO();
		}
		return formaPagamentoVO;
	}

	public void setFormaPagamentoVO(FormaPagamentoVO formaPagamentoVO) {
		this.formaPagamentoVO = formaPagamentoVO;
	}

	public TipoSacadoExtratoContaCorrenteEnum getTipoSacado() {
		return tipoSacado;
	}

	public void setTipoSacado(TipoSacadoExtratoContaCorrenteEnum tipoSacado) {
		this.tipoSacado = tipoSacado;
	}

	public String getNomeSacado() {
		if (nomeSacado == null) {
			nomeSacado = "";
		}
		return nomeSacado;
	}

	public void setNomeSacado(String nomeSacado) {
		this.nomeSacado = nomeSacado;
	}

	public TipoMovimentacaoFinanceira getTipoMovimentacaoFinanceira() {
		if (tipoMovimentacaoFinanceira == null) {
			tipoMovimentacaoFinanceira = TipoMovimentacaoFinanceira.ENTRADA;
		}
		return tipoMovimentacaoFinanceira;
	}

	public void setTipoMovimentacaoFinanceira(TipoMovimentacaoFinanceira tipoMovimentacaoFinanceira) {
		this.tipoMovimentacaoFinanceira = tipoMovimentacaoFinanceira;
	}

	public FuncionarioVO getFuncionarioSacado() {
		if (funcionarioSacado == null) {
			funcionarioSacado = new FuncionarioVO();
		}
		return funcionarioSacado;
	}

	public void setFuncionarioSacado(FuncionarioVO funcionarioSacado) {
		this.funcionarioSacado = funcionarioSacado;
	}

	public FornecedorVO getFornecedorSacado() {
		if (fornecedorSacado == null) {
			fornecedorSacado = new FornecedorVO();
		}
		return fornecedorSacado;
	}

	public void setFornecedorSacado(FornecedorVO fornecedorSacado) {
		this.fornecedorSacado = fornecedorSacado;
	}

	public ParceiroVO getParceiroSacado() {
		if (parceiroSacado == null) {
			parceiroSacado = new ParceiroVO();
		}
		return parceiroSacado;
	}

	public void setParceiroSacado(ParceiroVO parceiroSacado) {
		this.parceiroSacado = parceiroSacado;
	}

	public BancoVO getBancoSacado() {
		if (bancoSacado == null) {
			bancoSacado = new BancoVO();
		}
		return bancoSacado;
	}

	public void setBancoSacado(BancoVO bancoSacado) {
		this.bancoSacado = bancoSacado;
	}

	public OperadoraCartaoVO getOperadoraCartaoSacado() {
		if (operadoraCartaoSacado == null) {
			operadoraCartaoSacado = new OperadoraCartaoVO();
		}
		return operadoraCartaoSacado;
	}

	public void setOperadoraCartaoSacado(OperadoraCartaoVO operadoraCartaoSacado) {
		this.operadoraCartaoSacado = operadoraCartaoSacado;
	}

	public OperadoraCartaoVO getOperadoraCartao() {
		if (operadoraCartao == null) {
			operadoraCartao = new OperadoraCartaoVO();
		}
		return operadoraCartao;
	}

	public void setOperadoraCartao(OperadoraCartaoVO operadoraCartao) {
		this.operadoraCartao = operadoraCartao;
	}

	public TipoFinanciamentoEnum getTipoFinanciamentoEnum() {
		return tipoFinanciamentoEnum;
	}

	public void setTipoFinanciamentoEnum(TipoFinanciamentoEnum tipoFinanciamentoEnum) {
		this.tipoFinanciamentoEnum = tipoFinanciamentoEnum;
	}

	public Integer getQtdeParcelasCartaoCredito() {
		if (qtdeParcelasCartaoCredito == null) {
			qtdeParcelasCartaoCredito = 0;
		}
		return qtdeParcelasCartaoCredito;
	}

	public void setQtdeParcelasCartaoCredito(Integer qtdeParcelasCartaoCredito) {
		this.qtdeParcelasCartaoCredito = qtdeParcelasCartaoCredito;
	}

	public ConfiguracaoFinanceiroCartaoVO getConfiguracaoFinanceiroCartaoVO() {
		if (configuracaoFinanceiroCartaoVO == null) {
			configuracaoFinanceiroCartaoVO = new ConfiguracaoFinanceiroCartaoVO();
		}
		return configuracaoFinanceiroCartaoVO;
	}

	public void setConfiguracaoFinanceiroCartaoVO(ConfiguracaoFinanceiroCartaoVO configuracaoFinanceiroCartaoVO) {
		this.configuracaoFinanceiroCartaoVO = configuracaoFinanceiroCartaoVO;
	}

	public ConfiguracaoRecebimentoCartaoOnlineVO getConfiguracaoRecebimentoCartaoOnlineVO() {
		if (configuracaoRecebimentoCartaoOnlineVO == null) {
			configuracaoRecebimentoCartaoOnlineVO = new ConfiguracaoRecebimentoCartaoOnlineVO();
		}
		return configuracaoRecebimentoCartaoOnlineVO;
	}

	public void setConfiguracaoRecebimentoCartaoOnlineVO(ConfiguracaoRecebimentoCartaoOnlineVO configuracaoRecebimentoCartaoOnlineVO) {
		this.configuracaoRecebimentoCartaoOnlineVO = configuracaoRecebimentoCartaoOnlineVO;
	}

	public String getNomeCartaoCredito() {
		if (nomeCartaoCredito == null) {
			nomeCartaoCredito = "";
		}
		return nomeCartaoCredito;
	}

	public void setNomeCartaoCredito(String nomeCartaoCredito) {
		this.nomeCartaoCredito = nomeCartaoCredito;
	}

	public String getNumeroCartao() {
		if (numeroCartao == null) {
			numeroCartao = "";
		}
		return numeroCartao;
	}

	public void setNumeroCartao(String numeroCartao) {
		this.numeroCartao = numeroCartao;
	}

	public Integer getMesValidade() {
		if (mesValidade == null) {
			mesValidade = 0;
		}
		return mesValidade;
	}

	public void setMesValidade(Integer mesValidade) {
		this.mesValidade = mesValidade;
	}

	public Integer getAnoValidade() {
		if (anoValidade == null) {
			anoValidade = 0;
		}
		return anoValidade;
	}

	public void setAnoValidade(Integer anoValidade) {
		this.anoValidade = anoValidade;
	}

	public String getCodigoVerificacao() {
		if (codigoVerificacao == null) {
			codigoVerificacao = "";
		}
		return codigoVerificacao;
	}

	public void setCodigoVerificacao(String codigoVerificacao) {
		this.codigoVerificacao = codigoVerificacao;
	}

}
