package negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.financeiro.enumerador.OrigemFechamentoFinanceiroContaEnum;
import negocio.comuns.utilitarias.dominios.SituacaoContaReceber;
import negocio.comuns.utilitarias.dominios.TipoOrigemContaReceber;
import negocio.comuns.utilitarias.dominios.TipoPessoa;

public class FechamentoFinanceiroContaVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -473984327646106131L;
	private Integer codigo;
	private TipoOrigemContaReceber tipoOrigemContaReceber;
	private String codigoOrigem;
	private String nossoNumero;
	private String parcela;
	private SituacaoContaReceber situacaoContaReceber;
	private TipoPessoa tipoPessoa;
	private Date dataVencimento;
	private Date dataCompetencia;
	private Date dataRecebimento;
	private Date dataCancelamento;
	private Date dataNegociacao;
	private MatriculaPeriodoVO matriculaPeriodo;
	private PessoaVO pessoa;
	private PessoaVO responsavelFinanceiro;
	private ParceiroVO parceiro;
	private FornecedorVO fornecedor;
	private FuncionarioVO funcionario;
	private String nomeSacado;
	private String cpfCnpjSacado;
	private MatriculaVO matricula;
	private UnidadeEnsinoVO unidadeEnsinoFinanceira;
	
	private UnidadeEnsinoVO unidadeEnsinoAcademica;
	private FechamentoFinanceiroVO fechamentoFinanceiro;
	private Double valor;
	private OrigemFechamentoFinanceiroContaEnum origemFechamentoFinanceiroConta;
	private Integer codOrigemFechamentoFinanceiro;
	
	private List<FechamentoFinanceiroFormaPagamentoVO> fechamentoFinanceiroFormaPagamentoVOs;	
	private List<FechamentoFinanceiroCentroResultadoVO> fechamentoFinanceiroCentroResultadoVOs;
	private List<FechamentoFinanceiroDetalhamentoValorVO> fechamentoFinanceiroDetalhamentoValorVOs;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public TipoOrigemContaReceber getTipoOrigemContaReceber() {
		if (tipoOrigemContaReceber == null) {
			tipoOrigemContaReceber = TipoOrigemContaReceber.MATRICULA;
		}
		return tipoOrigemContaReceber;
	}

	public void setTipoOrigemContaReceber(TipoOrigemContaReceber tipoOrigemContaReceber) {
		this.tipoOrigemContaReceber = tipoOrigemContaReceber;
	}

	public String getCodigoOrigem() {
		if (codigoOrigem == null) {
			codigoOrigem = "";
		}
		return codigoOrigem;
	}

	public void setCodigoOrigem(String codigoOrigem) {
		this.codigoOrigem = codigoOrigem;
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

	public SituacaoContaReceber getSituacaoContaReceber() {
		if (situacaoContaReceber == null) {
			situacaoContaReceber = SituacaoContaReceber.A_RECEBER;
		}
		return situacaoContaReceber;
	}

	public void setSituacaoContaReceber(SituacaoContaReceber situacaoContaReceber) {
		this.situacaoContaReceber = situacaoContaReceber;
	}

	public Date getDataVencimento() {

		return dataVencimento;
	}

	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public Date getDataCompetencia() {

		return dataCompetencia;
	}

	public void setDataCompetencia(Date dataCompetencia) {
		this.dataCompetencia = dataCompetencia;
	}

	public Date getDataRecebimento() {

		return dataRecebimento;
	}

	public void setDataRecebimento(Date dataRecebimento) {
		this.dataRecebimento = dataRecebimento;
	}

	public Date getDataCancelamento() {

		return dataCancelamento;
	}

	public void setDataCancelamento(Date dataCancelamento) {
		this.dataCancelamento = dataCancelamento;
	}

	public Date getDataNegociacao() {

		return dataNegociacao;
	}

	public void setDataNegociacao(Date dataNegociacao) {
		this.dataNegociacao = dataNegociacao;
	}

	public MatriculaPeriodoVO getMatriculaPeriodo() {
		if (matriculaPeriodo == null) {
			matriculaPeriodo = new MatriculaPeriodoVO();
		}
		return matriculaPeriodo;
	}

	public void setMatriculaPeriodo(MatriculaPeriodoVO matriculaPeriodo) {
		this.matriculaPeriodo = matriculaPeriodo;
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

	public PessoaVO getResponsavelFinanceiro() {
		if (responsavelFinanceiro == null) {
			responsavelFinanceiro = new PessoaVO();
		}
		return responsavelFinanceiro;
	}

	public void setResponsavelFinanceiro(PessoaVO responsavelFinanceiro) {
		this.responsavelFinanceiro = responsavelFinanceiro;
	}

	public ParceiroVO getParceiro() {
		if (parceiro == null) {
			parceiro = new ParceiroVO();
		}
		return parceiro;
	}

	public void setParceiro(ParceiroVO parceiro) {
		this.parceiro = parceiro;
	}

	public FornecedorVO getFornecedor() {
		if (fornecedor == null) {
			fornecedor = new FornecedorVO();
		}
		return fornecedor;
	}

	public void setFornecedor(FornecedorVO fornecedorVO) {
		this.fornecedor = fornecedorVO;
	}

	public FuncionarioVO getFuncionario() {
		if (funcionario == null) {
			funcionario = new FuncionarioVO();
		}
		return funcionario;
	}

	public void setFuncionario(FuncionarioVO funcionario) {
		this.funcionario = funcionario;
	}

	public UnidadeEnsinoVO getUnidadeEnsinoFinanceira() {
		if (unidadeEnsinoFinanceira == null) {
			unidadeEnsinoFinanceira = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoFinanceira;
	}

	public void setUnidadeEnsinoFinanceira(UnidadeEnsinoVO unidadeEnsinoFinanceira) {
		this.unidadeEnsinoFinanceira = unidadeEnsinoFinanceira;
	}

	public UnidadeEnsinoVO getUnidadeEnsinoAcademica() {
		if (unidadeEnsinoAcademica == null) {
			unidadeEnsinoAcademica = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoAcademica;
	}

	public void setUnidadeEnsinoAcademica(UnidadeEnsinoVO unidadeEnsinoAcademica) {
		this.unidadeEnsinoAcademica = unidadeEnsinoAcademica;
	}

	public List<FechamentoFinanceiroFormaPagamentoVO> getFechamentoFinanceiroFormaPagamentoVOs() {
		if(fechamentoFinanceiroFormaPagamentoVOs == null){
			fechamentoFinanceiroFormaPagamentoVOs = new ArrayList<FechamentoFinanceiroFormaPagamentoVO>(0);
		}
		return fechamentoFinanceiroFormaPagamentoVOs;
	}

	public void setFechamentoFinanceiroFormaPagamentoVOs(
			List<FechamentoFinanceiroFormaPagamentoVO> fechamentoFinanceiroFormaPagamentoVOs) {
		this.fechamentoFinanceiroFormaPagamentoVOs = fechamentoFinanceiroFormaPagamentoVOs;
	}

	public String getParcela() {
		if(parcela == null){
			parcela = "";
		}
		return parcela;
	}

	public void setParcela(String parcela) {
		this.parcela = parcela;
	}

	public List<FechamentoFinanceiroCentroResultadoVO> getFechamentoFinanceiroCentroResultadoVOs() {
		if(fechamentoFinanceiroCentroResultadoVOs == null){
			fechamentoFinanceiroCentroResultadoVOs = new ArrayList<FechamentoFinanceiroCentroResultadoVO>();
		}
		return fechamentoFinanceiroCentroResultadoVOs;
	}

	public void setFechamentoFinanceiroCentroResultadoVOs(
			List<FechamentoFinanceiroCentroResultadoVO> fechamentoFinanceiroCentroResultadoVOs) {
		this.fechamentoFinanceiroCentroResultadoVOs = fechamentoFinanceiroCentroResultadoVOs;
	}

	public String getNomeSacado() {
		if(nomeSacado == null){
			nomeSacado = "";
		}
		return nomeSacado;
	}

	public void setNomeSacado(String nomeSacado) {
		this.nomeSacado = nomeSacado;
	}

	public String getCpfCnpjSacado() {
		if(cpfCnpjSacado == null){
			cpfCnpjSacado = "";
		}
		return cpfCnpjSacado;
	}

	public void setCpfCnpjSacado(String cpfCnpjSacado) {
		this.cpfCnpjSacado = cpfCnpjSacado;
	}

	public MatriculaVO getMatricula() {
		if(matricula == null){
			matricula = new MatriculaVO();
		}
		return matricula;
	}

	public void setMatricula(MatriculaVO matricula) {
		this.matricula = matricula;
	}

	public FechamentoFinanceiroVO getFechamentoFinanceiro() {
		if(fechamentoFinanceiro == null){
			fechamentoFinanceiro = new FechamentoFinanceiroVO();
		}
		return fechamentoFinanceiro;
	}

	public void setFechamentoFinanceiro(FechamentoFinanceiroVO fechamentoFinanceiro) {
		this.fechamentoFinanceiro = fechamentoFinanceiro;
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

	public TipoPessoa getTipoPessoa() {
		if(tipoPessoa == null){
			tipoPessoa = TipoPessoa.ALUNO;
		}
		return tipoPessoa;
	}

	public void setTipoPessoa(TipoPessoa tipoPessoa) {
		this.tipoPessoa = tipoPessoa;
	}

	public OrigemFechamentoFinanceiroContaEnum getOrigemFechamentoFinanceiroConta() {
		if(origemFechamentoFinanceiroConta == null){
			origemFechamentoFinanceiroConta = OrigemFechamentoFinanceiroContaEnum.CONTA_RECEBER;
		}
		return origemFechamentoFinanceiroConta;
	}

	public void setOrigemFechamentoFinanceiroConta(OrigemFechamentoFinanceiroContaEnum origemFechamentoFinanceiroConta) {
		this.origemFechamentoFinanceiroConta = origemFechamentoFinanceiroConta;
	}

	public Integer getCodOrigemFechamentoFinanceiro() {
		if (codOrigemFechamentoFinanceiro == null) {
			codOrigemFechamentoFinanceiro = 0;
		}
		return codOrigemFechamentoFinanceiro;
	}

	public void setCodOrigemFechamentoFinanceiro(Integer codOrigemFechamentoFinanceiro) {
		this.codOrigemFechamentoFinanceiro = codOrigemFechamentoFinanceiro;
	}
	
	public List<FechamentoFinanceiroDetalhamentoValorVO> getFechamentoFinanceiroDetalhamentoValorVOs() {
		if(fechamentoFinanceiroDetalhamentoValorVOs == null){
			fechamentoFinanceiroDetalhamentoValorVOs = new ArrayList<FechamentoFinanceiroDetalhamentoValorVO>(0);
		}
		return fechamentoFinanceiroDetalhamentoValorVOs;
	}

	public void setFechamentoFinanceiroDetalhamentoValorVOs(
			List<FechamentoFinanceiroDetalhamentoValorVO> fechamentoFinanceiroDetalhamentoValorVOs) {
		this.fechamentoFinanceiroDetalhamentoValorVOs = fechamentoFinanceiroDetalhamentoValorVOs;
	}
	
	
	
	

}
