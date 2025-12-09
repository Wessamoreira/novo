package negocio.comuns.financeiro;

import java.io.Serializable;
import java.util.Date;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.FormaPagamentoVO;
import negocio.comuns.financeiro.enumerador.OrigemExtratoContaCorrenteEnum;
import negocio.comuns.financeiro.enumerador.TipoSacadoExtratoContaCorrenteEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoMovimentacaoFinanceira;

public class ExtratoContaCorrenteVO extends SuperVO implements Serializable {

	/**
     * 
     */
	private static final long serialVersionUID = -5715430450542249622L;

	private Integer codigo;
	private Double valor;
	private Double valorTaxaBancaria;
	private Date data;
	private OrigemExtratoContaCorrenteEnum origemExtratoContaCorrente;
	private TipoMovimentacaoFinanceira tipoMovimentacaoFinanceira;
	private Integer codigoOrigem;
	private Integer codigoCheque;
	private String sacadoCheque;
	private String numeroCheque;
	private String bancoCheque;
	private String contaCorrenteCheque;
	private String agenciaCheque;
	private Date dataPrevisaoCheque;
	private String nomeSacado;
	private Integer codigoSacado;
	private TipoSacadoExtratoContaCorrenteEnum tipoSacado;
	private ContaCorrenteVO contaCorrente;
	private UnidadeEnsinoVO unidadeEnsino;
	private UsuarioVO responsavel;
	private FormaPagamentoVO formaPagamento;
	private FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO;
	private OperadoraCartaoVO operadoraCartaoVO;
	private ConciliacaoContaCorrenteDiaExtratoVO conciliacaoContaCorrenteDiaExtratoVO;
	private boolean desconsiderarConciliacaoBancaria = false;
	/*
	 * Transiente Usado no Rel. Extrato Conta Corrente
	 */
	private Boolean possuiOrigem;
	private Boolean lancamentoManual = false;
	private Double saldo;
	/*private String apresentarLancamentoConjunto;
	private Double valorLancamentoConjuntoUtilizado;
	private List<ConciliacaoContaCorrenteDiaExtratoConjuntaDetalheVO> listaLancamentoConjuntoDetalhe;*/
	

	public ExtratoContaCorrenteVO() {
		super();

	}
	public ExtratoContaCorrenteVO(Integer codigo, Double valor) {
		super();
		this.codigo = codigo;
		this.valor = valor;
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

	public Double getValorSemTaxaBancaria() {	
		if (valor == null) {
			valor = 0.0;
		}
		return valor;
	}
	
	public Double getValor() {	
		if (valor == null) {
			valor = 0.0;
		}
		return valor - getValorTaxaBancaria();
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public OrigemExtratoContaCorrenteEnum getOrigemExtratoContaCorrente() {
		return origemExtratoContaCorrente;
	}

	public void setOrigemExtratoContaCorrente(OrigemExtratoContaCorrenteEnum origemExtratoContaCorrente) {
		this.origemExtratoContaCorrente = origemExtratoContaCorrente;
	}

	public TipoMovimentacaoFinanceira getTipoMovimentacaoFinanceira() {
		return tipoMovimentacaoFinanceira;
	}

	public void setTipoMovimentacaoFinanceira(TipoMovimentacaoFinanceira tipoMovimentacaoFinanceira) {
		this.tipoMovimentacaoFinanceira = tipoMovimentacaoFinanceira;
	}

	public Integer getCodigoOrigem() {
		return codigoOrigem;
	}

	public void setCodigoOrigem(Integer codigoOrigem) {
		this.codigoOrigem = codigoOrigem;
	}

	public String getSacadoCheque() {
		return sacadoCheque;
	}

	public void setSacadoCheque(String sacadoCheque) {
		this.sacadoCheque = sacadoCheque;
	}

	public String getNumeroCheque() {
		return numeroCheque;
	}

	public void setNumeroCheque(String numeroCheque) {
		this.numeroCheque = numeroCheque;
	}

	public String getBancoCheque() {
		return bancoCheque;
	}

	public void setBancoCheque(String bancoCheque) {
		this.bancoCheque = bancoCheque;
	}

	public String getContaCorrenteCheque() {
		return contaCorrenteCheque;
	}

	public void setContaCorrenteCheque(String contaCorrenteCheque) {
		this.contaCorrenteCheque = contaCorrenteCheque;
	}

	public Date getDataPrevisaoCheque() {
		return dataPrevisaoCheque;
	}

	public void setDataPrevisaoCheque(Date dataPrevisaoCheque) {
		this.dataPrevisaoCheque = dataPrevisaoCheque;
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

	public Integer getCodigoSacado() {
		return codigoSacado;
	}

	public void setCodigoSacado(Integer codigoSacado) {
		this.codigoSacado = codigoSacado;
	}

	public TipoSacadoExtratoContaCorrenteEnum getTipoSacado() {
		return tipoSacado;
	}

	public void setTipoSacado(TipoSacadoExtratoContaCorrenteEnum tipoSacado) {
		this.tipoSacado = tipoSacado;
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

	public OperadoraCartaoVO getOperadoraCartaoVO() {
		if (operadoraCartaoVO == null) {
			operadoraCartaoVO = new OperadoraCartaoVO();
		}
		return operadoraCartaoVO;
	}

	public void setOperadoraCartaoVO(OperadoraCartaoVO operadoraCartaoVO) {
		this.operadoraCartaoVO = operadoraCartaoVO;
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

	public UsuarioVO getResponsavel() {
		if (responsavel == null) {
			responsavel = new UsuarioVO();
		}
		return responsavel;
	}

	public void setResponsavel(UsuarioVO responsavel) {
		this.responsavel = responsavel;
	}

	public FormaPagamentoVO getFormaPagamento() {
		if (formaPagamento == null) {
			formaPagamento = new FormaPagamentoVO();
		}
		return formaPagamento;
	}

	public void setFormaPagamento(FormaPagamentoVO formaPagamento) {
		this.formaPagamento = formaPagamento;
	}

	public FormaPagamentoNegociacaoRecebimentoVO getFormaPagamentoNegociacaoRecebimentoVO() {
		if (formaPagamentoNegociacaoRecebimentoVO == null) {
			formaPagamentoNegociacaoRecebimentoVO = new FormaPagamentoNegociacaoRecebimentoVO();
		}
		return formaPagamentoNegociacaoRecebimentoVO;
	}
	
	public void setFormaPagamentoNegociacaoRecebimentoVO(FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO) {
		this.formaPagamentoNegociacaoRecebimentoVO = formaPagamentoNegociacaoRecebimentoVO;
	}
	
	public Integer getCodigoCheque() {
		if (codigoCheque == null) {
			codigoCheque = 0;
		}
		return codigoCheque;
	}

	public void setCodigoCheque(Integer codigoCheque) {
		this.codigoCheque = codigoCheque;
	}

	public String getAgenciaCheque() {
		return agenciaCheque;
	}

	public void setAgenciaCheque(String agenciaCheque) {
		this.agenciaCheque = agenciaCheque;
	}

	public ConciliacaoContaCorrenteDiaExtratoVO getConciliacaoContaCorrenteDiaExtratoVO() {
		if (conciliacaoContaCorrenteDiaExtratoVO == null) {
			conciliacaoContaCorrenteDiaExtratoVO = new ConciliacaoContaCorrenteDiaExtratoVO();
		}
		return conciliacaoContaCorrenteDiaExtratoVO;
	}

	public void setConciliacaoContaCorrenteDiaExtratoVO(ConciliacaoContaCorrenteDiaExtratoVO conciliacaoContaCorrenteDiaExtratoVO) {
		this.conciliacaoContaCorrenteDiaExtratoVO = conciliacaoContaCorrenteDiaExtratoVO;
	}
	
	public boolean isDesconsiderarConciliacaoBancaria() {
		return desconsiderarConciliacaoBancaria;
	}
	
	public void setDesconsiderarConciliacaoBancaria(boolean desconsiderarConciliacaoBancaria) {
		this.desconsiderarConciliacaoBancaria = desconsiderarConciliacaoBancaria;
	}
	
	
	public boolean isPermiteEstornoExtrato(){
		return Uteis.isAtributoPreenchido(getOrigemExtratoContaCorrente()) && (getOrigemExtratoContaCorrente().isMovimentacaoFinanceira() ||  getOrigemExtratoContaCorrente().isPagamento() || getOrigemExtratoContaCorrente().isRecebimento()); 
	}
	
	public String getTituloBotaoEstornoExtrato(){
		return isPermiteEstornoExtrato() ? "Estornar Lançamento":"Não é possível realizar o Estornar do Lançamento para a Origem Informada."; 
	}
	

	public String getFormaPagamentoConciliacao() {
		if (getFormaPagamento().getTipo().equals("CD") || getFormaPagamento().getTipo().equals("CA")) {
			return getOperadoraCartaoVO().getNome() + " - " + getFormaPagamento().getTipo_Apresentar();
		}
		return getFormaPagamento().getTipo_Apresentar();
	}
	
	public String getFormaPagamentoConciliacao_key() {
		if (getFormaPagamento().getTipo().equals("CD") || getFormaPagamento().getTipo().equals("CA")) {
			return getOperadoraCartaoVO().getCodigo() + " - " + getFormaPagamento().getCodigo();
		}
		return getFormaPagamento().getCodigo().toString();
	}

	public Double getValorTaxaBancaria() {
		if (valorTaxaBancaria == null) {
			valorTaxaBancaria = 0.0;
		}
		return valorTaxaBancaria;
	}
	
	public void setValorTaxaBancaria(Double valorTaxaBancaria) {
		this.valorTaxaBancaria = valorTaxaBancaria;
	}
	
	public Boolean getPossuiOrigem() {
		if (possuiOrigem == null) {
			possuiOrigem = false;
		}
		return possuiOrigem;
	}
	
	public void setPossuiOrigem(Boolean possuiOrigem) {
		this.possuiOrigem = possuiOrigem;
	}
		
	public String getLancamentoManualApresentar() {
		return Uteis.isAtributoPreenchido(getLancamentoManual())  && getLancamentoManual() ? "Sim":"Não";
	}
	
	public Boolean getLancamentoManual() {
		if (lancamentoManual == null) {
			lancamentoManual = false;
		}
		return lancamentoManual;
	}
	
	public void setLancamentoManual(Boolean lancamentoManual) {
		this.lancamentoManual = lancamentoManual;
	}
	
	public Boolean getIsConciliado() {
		return Uteis.isAtributoPreenchido(getConciliacaoContaCorrenteDiaExtratoVO().getCodigo());
	}

	public Double valorApresentar;


	public Double getValorApresentar() {		
		if (valorApresentar == null) {
			valorApresentar = getTipoMovimentacaoFinanceira().equals(TipoMovimentacaoFinanceira.SAIDA) ? getValor()*-1 : getValor();
		}
		return valorApresentar;
	}
	public void setValorApresentar(Double valorApresentar) {
		this.valorApresentar = valorApresentar;
	}
	
	private Date dataAlterar;


	public Date getDataAlterar() {
		if (dataAlterar == null) {
			dataAlterar = getData();
		}
		return dataAlterar;
	}
	public void setDataAlterar(Date dataAlterar) {
		this.dataAlterar = dataAlterar;
	}
	public Double getSaldo() {
		if (saldo == null) {
			saldo = 0.0;
		}
		return saldo;
	}
	public void setSaldo(Double saldo) {
		this.saldo = saldo;
	}

	/*public String getApresentarLancamentoConjunto() {
		if (apresentarLancamentoConjunto == null) {
			apresentarLancamentoConjunto = "";
		}
		return apresentarLancamentoConjunto;
	}
	public void setApresentarLancamentoConjunto(String apresentarLancamentoConjunto) {
		this.apresentarLancamentoConjunto = apresentarLancamentoConjunto;
	}

	public Double getValorLancamentoConjuntoUtilizado() {
		if (valorLancamentoConjuntoUtilizado == null) {
			valorLancamentoConjuntoUtilizado = 0.0;
		}
		return valorLancamentoConjuntoUtilizado;
	}
	public void setValorLancamentoConjuntoUtilizado(Double valorLancamentoConjuntoUtilizado) {
		this.valorLancamentoConjuntoUtilizado = valorLancamentoConjuntoUtilizado;
	}
	
	public Integer getQtdLancamentoConjunto() {
		if(!Uteis.isAtributoPreenchido(getApresentarLancamentoConjunto())) {
			return 0;
		}
		return (StringUtils.countMatches(getApresentarLancamentoConjunto(), ",")) ;
	}
	

	public List<ConciliacaoContaCorrenteDiaExtratoConjuntaDetalheVO> getListaLancamentoConjuntoDetalhe() {
		if (listaLancamentoConjuntoDetalhe == null) {
			listaLancamentoConjuntoDetalhe = new ArrayList<ConciliacaoContaCorrenteDiaExtratoConjuntaDetalheVO>();
		}
		return listaLancamentoConjuntoDetalhe;
	}
	public void setListaLancamentoConjuntoDetalhe(List<ConciliacaoContaCorrenteDiaExtratoConjuntaDetalheVO> listaLancamentoConjuntoDetalhe) {
		this.listaLancamentoConjuntoDetalhe = listaLancamentoConjuntoDetalhe;
	}
*/
	public String origemApresentar;
	
	public String getOrigemApresentar() {
		if(origemApresentar == null) {
			origemApresentar = getOrigemExtratoContaCorrente().getDescricao();
			if(Uteis.isAtributoPreenchido(getCodigoCheque())) {
				origemApresentar += " - BANCO: "+getBancoCheque()+" AG:"+getAgenciaCheque()+" CC:"+getAgenciaCheque()+" Nr:"+getNumeroCheque();			
			}
			if(Uteis.isAtributoPreenchido(getCodigoOrigem())) {
				origemApresentar+= " ("+getCodigoOrigem()+")";
			}
		}
		return origemApresentar;
		
	}
	
	
}
