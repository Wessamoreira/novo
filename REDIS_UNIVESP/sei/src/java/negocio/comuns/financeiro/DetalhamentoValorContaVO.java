package negocio.comuns.financeiro;

import java.util.Date;
import java.util.Optional;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.financeiro.enumerador.OrigemDetalhamentoContaEnum;
import negocio.comuns.financeiro.enumerador.TipoCentroResultadoOrigemDetalheEnum;
import negocio.comuns.utilitarias.dominios.FaixaDescontoProgressivo;
import negocio.comuns.utilitarias.dominios.TipoDescontoAluno;

/**
 * 
 * @author PedroOtimize
 *
 */
public class DetalhamentoValorContaVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -521284187080463382L;
	private Integer codigo;
	private OrigemDetalhamentoContaEnum origemDetalhamentoConta;
	private Integer codigoOrigem;
	private TipoCentroResultadoOrigemDetalheEnum tipoCentroResultadoOrigemDetalheEnum;
	private FaixaDescontoProgressivo faixaDescontoProgressivo;
	private Integer codOrigemDoTipoDetalhe;
	private String nomeOrigemDoTipoDetalhe;
	private Double valor;
	private TipoDescontoAluno tipoValor;
	private Double valorTipoValor;
	private Date dataLimiteAplicacaoDesconto;
	private Integer ordemApresentacao;
	private boolean utilizado = true;

	/**
	 * Campo Transient
	 */
	

	public void preencherCentroResultadoOrigemDetalheVO(Double valor, TipoCentroResultadoOrigemDetalheEnum tipoCentroResultadoOrigemDetalheEnum, OrigemDetalhamentoContaEnum origemDetalhamentoContaEnum, Integer codigoConta, Integer ordemApresentacao, TipoDescontoAluno tipoValor, Double valorTipoValor) {
		setValor(valor);
		setTipoCentroResultadoOrigemDetalheEnum(tipoCentroResultadoOrigemDetalheEnum);
		setOrdemApresentacao(ordemApresentacao);
		setOrigemDetalhamentoConta(origemDetalhamentoContaEnum);
		setCodigoOrigem(codigoConta);
		setTipoValor(tipoValor);
		setValorTipoValor(valorTipoValor);
	}

	public Integer getCodigo() {
		codigo = Optional.ofNullable(codigo).orElse(0);
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}


	public TipoCentroResultadoOrigemDetalheEnum getTipoCentroResultadoOrigemDetalheEnum() {
		return tipoCentroResultadoOrigemDetalheEnum;
	}

	public void setTipoCentroResultadoOrigemDetalheEnum(TipoCentroResultadoOrigemDetalheEnum tipoCentroResultadoOrigemDetalheEnum) {
		this.tipoCentroResultadoOrigemDetalheEnum = tipoCentroResultadoOrigemDetalheEnum;
	}

	public Double getValor() {
		valor = Optional.ofNullable(valor).orElse(0.0);
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public Date getDataLimiteAplicacaoDesconto() {
		return dataLimiteAplicacaoDesconto;
	}

	public void setDataLimiteAplicacaoDesconto(Date dataLimiteAplicacaoDesconto) {
		this.dataLimiteAplicacaoDesconto = dataLimiteAplicacaoDesconto;
	}

	public boolean isUtilizado() {
		return utilizado;
	}

	public void setUtilizado(boolean utilizado) {
		this.utilizado = utilizado;
	}

	public FaixaDescontoProgressivo getFaixaDescontoProgressivo() {
		faixaDescontoProgressivo = Optional.ofNullable(faixaDescontoProgressivo).orElse(FaixaDescontoProgressivo.NENHUM);
		return faixaDescontoProgressivo;
	}

	public void setFaixaDescontoProgressivo(FaixaDescontoProgressivo faixaDescontoProgressivo) {
		this.faixaDescontoProgressivo = faixaDescontoProgressivo;
	}

	public Integer getCodOrigemDoTipoDetalhe() {
		codOrigemDoTipoDetalhe = Optional.ofNullable(codOrigemDoTipoDetalhe).orElse(0);
		return codOrigemDoTipoDetalhe;
	}

	public void setCodOrigemDoTipoDetalhe(Integer codOrigemDoTipoDetalhe) {
		this.codOrigemDoTipoDetalhe = codOrigemDoTipoDetalhe;
	}

	public Integer getOrdemApresentacao() {
		ordemApresentacao = Optional.ofNullable(ordemApresentacao).orElse(100);
		return ordemApresentacao;
	}

	public void setOrdemApresentacao(Integer ordemApresentacao) {
		this.ordemApresentacao = ordemApresentacao;
	}

	public String getNomeOrigemDoTipoDetalhe() {
		nomeOrigemDoTipoDetalhe = Optional.ofNullable(nomeOrigemDoTipoDetalhe).orElse("");
		return nomeOrigemDoTipoDetalhe;
	}

	public void setNomeOrigemDoTipoDetalhe(String nomeOrigemDoTipoDetalhe) {
		this.nomeOrigemDoTipoDetalhe = nomeOrigemDoTipoDetalhe;
	}
	
	public boolean isTipoDetalheSoma(){
		return (isUtilizado() && (getTipoCentroResultadoOrigemDetalheEnum().isTipoDetalheSoma() 
				|| (getTipoCentroResultadoOrigemDetalheEnum().isReajustePorAtraso() && getValor() >= 0.0)
				)); 
	}
	
	public boolean isTipoDetalheSubtrair(){
		return (isUtilizado() && (getTipoCentroResultadoOrigemDetalheEnum().isTipoDetalheSubtrair() 
				|| (getTipoCentroResultadoOrigemDetalheEnum().isReajustePorAtraso() && getValor() < 0.0)
				)); 
	}


	public String getCssValor() {
		if (isTipoDetalheSoma()) {
			return "color:#0000FF;";
		} else if (isTipoDetalheSubtrair()){
			return "color:#FF0000;";
		} else if (!isUtilizado()) {
			return "color:#696969;";
		}
		return "";
	}

	public boolean equalsCentroResultadoOrigemDetalhe(DetalhamentoValorContaVO obj) {
		return getCodOrigemDoTipoDetalhe().equals(obj.getCodOrigemDoTipoDetalhe())
				&& getFaixaDescontoProgressivo().equals(obj.getFaixaDescontoProgressivo())
				&& getTipoCentroResultadoOrigemDetalheEnum().equals(obj.getTipoCentroResultadoOrigemDetalheEnum())
				&& ((getTipoCentroResultadoOrigemDetalheEnum().isAcrescimo()
						|| getTipoCentroResultadoOrigemDetalheEnum().isJuro()
						|| getTipoCentroResultadoOrigemDetalheEnum().isMulta()
						|| getTipoCentroResultadoOrigemDetalheEnum().isReajustePreco()
						|| getTipoCentroResultadoOrigemDetalheEnum().isReajustePorAtraso()
						|| getTipoCentroResultadoOrigemDetalheEnum().isValorBase()
						|| getTipoCentroResultadoOrigemDetalheEnum().isDescontoRateio()
						|| getTipoCentroResultadoOrigemDetalheEnum().isDescontoCusteadoContaReceber()
						|| getTipoCentroResultadoOrigemDetalheEnum().isDescontoAluno())
						
					|| 
					((getTipoCentroResultadoOrigemDetalheEnum().isDescontoProgressivo() 
						|| getTipoCentroResultadoOrigemDetalheEnum().isDescontoInstituicao()
						|| getTipoCentroResultadoOrigemDetalheEnum().isDescontoConvenio()
						|| getTipoCentroResultadoOrigemDetalheEnum().isDescontoManual()) && getValor().equals(obj.getValor())));
	}

	public TipoDescontoAluno getTipoValor() {
		if(tipoValor == null){
			tipoValor = TipoDescontoAluno.VALOR;
		}
		return tipoValor;
	}

	public void setTipoValor(TipoDescontoAluno tipoDesconto) {
		this.tipoValor = tipoDesconto;
	}

	public Double getValorTipoValor() {
		if(valorTipoValor == null){
			valorTipoValor = 0.0;
		}
		return valorTipoValor;
	}

	public void setValorTipoValor(Double valorTipoDesconto) {
		this.valorTipoValor = valorTipoDesconto;
	}

	public OrigemDetalhamentoContaEnum getOrigemDetalhamentoConta() {
		if(origemDetalhamentoConta == null){
			origemDetalhamentoConta = OrigemDetalhamentoContaEnum.CONTA_RECEBER;
		}
		return origemDetalhamentoConta;
	}

	public void setOrigemDetalhamentoConta(OrigemDetalhamentoContaEnum origemDetalhamentoConta) {
		this.origemDetalhamentoConta = origemDetalhamentoConta;
	}

	public Integer getCodigoOrigem() {
		if(codigoOrigem == null){
			codigoOrigem = 0;
		}
		return codigoOrigem;
	}

	public void setCodigoOrigem(Integer codigoOrigem) {
		this.codigoOrigem = codigoOrigem;
	}

	
	

}
