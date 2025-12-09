package negocio.comuns.financeiro;

import java.util.Date;
import java.util.Optional;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.financeiro.enumerador.TipoCentroResultadoOrigemDetalheEnum;
import negocio.comuns.utilitarias.dominios.FaixaDescontoProgressivo;
import negocio.comuns.utilitarias.dominios.TipoDescontoAluno;

public class FechamentoFinanceiroDetalhamentoValorVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5993524737919906034L;
	private Integer codigo;
	private FechamentoFinanceiroContaVO fechamentoFinanceiroContaVO;
	private TipoCentroResultadoOrigemDetalheEnum tipoCentroResultadoOrigemDetalhe;
	private FaixaDescontoProgressivo faixaDescontoProgressivo;
	private Integer codOrigemDoTipoDetalhe;
	private String nomeOrigemDoTipoDetalhe;
	private Double valor;
	private TipoDescontoAluno tipoValor;
	private Double valorTipoValor;
	private Date dataLimiteAplicacaoDesconto;
	private Integer ordemApresentacao;
	private boolean utilizado = true;

	public Integer getCodigo() {
		codigo = Optional.ofNullable(codigo).orElse(0);
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
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
		faixaDescontoProgressivo = Optional.ofNullable(faixaDescontoProgressivo)
				.orElse(FaixaDescontoProgressivo.NENHUM);
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

	public TipoCentroResultadoOrigemDetalheEnum getTipoCentroResultadoOrigemDetalhe() {
		if (tipoCentroResultadoOrigemDetalhe == null) {
			tipoCentroResultadoOrigemDetalhe = TipoCentroResultadoOrigemDetalheEnum.VALOR_BASE;
		}
		return tipoCentroResultadoOrigemDetalhe;
	}

	public void setTipoCentroResultadoOrigemDetalhe(TipoCentroResultadoOrigemDetalheEnum tipoCentroResultadoOrigemDetalhe) {
		this.tipoCentroResultadoOrigemDetalhe = tipoCentroResultadoOrigemDetalhe;
	}

	public FechamentoFinanceiroContaVO getFechamentoFinanceiroContaVO() {
		if (fechamentoFinanceiroContaVO == null) {
			fechamentoFinanceiroContaVO = new FechamentoFinanceiroContaVO();
		}
		return fechamentoFinanceiroContaVO;
	}

	public void setFechamentoFinanceiroContaVO(FechamentoFinanceiroContaVO fechamentoFinanceiroContaVO) {
		this.fechamentoFinanceiroContaVO = fechamentoFinanceiroContaVO;
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

}
