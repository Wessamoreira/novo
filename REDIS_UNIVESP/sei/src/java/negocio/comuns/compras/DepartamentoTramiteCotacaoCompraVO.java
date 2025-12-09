package negocio.comuns.compras;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

import com.google.common.base.Preconditions;

import negocio.comuns.administrativo.CargoVO;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.protocolo.enumeradores.TipoPoliticaDistribuicaoEnum;

public class DepartamentoTramiteCotacaoCompraVO extends SuperVO implements Comparable<DepartamentoTramiteCotacaoCompraVO> {

	private static final long serialVersionUID = -4348570195651159129L;

	private Integer codigo;

	private TramiteCotacaoCompraVO tramiteVO;

	private DepartamentoVO departamentoVO;

	private TipoDistribuicaoCotacaoEnum tipoDistribuicaoCotacao = TipoDistribuicaoCotacaoEnum.COORDENADOR_CURSO_ESPECIFICO_TRAMITE;

	private CargoVO cargoVO;

	private FuncionarioVO funcionario;

	private Integer ordem;

	private TipoControleFinanceiroEnum tipoControleFinanceiro;

	private TipoPoliticaDistribuicaoEnum tipoPoliticaDistribuicao;

	private boolean observacaoObrigatoria = false;

	private Integer prazoExecucao;

	private String observacao;

	private String orientacaoResponsavel;

	private BigDecimal valorMinimo;

	private BigDecimal valorMaximo;

	public enum TipoControleFinanceiroEnum {
		NAO_CONTROLA("Não Controla valor"), VALOR_MINIMO("Valor Minimo"), FAIXA_VALORES("Faixa de Valores");

		String valorApresentar;

		private TipoControleFinanceiroEnum(String valorApresentar) {
			this.valorApresentar = valorApresentar;
		}

		public String getValorApresentar() {
			return valorApresentar;
		}

	}

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public DepartamentoVO getDepartamentoVO() {
		this.departamentoVO = Optional.ofNullable(this.departamentoVO).orElse(new DepartamentoVO());
		return departamentoVO;
	}

	public void setDepartamentoVO(DepartamentoVO departamentoVO) {
		this.departamentoVO = departamentoVO;
	}

	public TipoDistribuicaoCotacaoEnum getTipoDistribuicaoCotacao() {
		return tipoDistribuicaoCotacao;
	}

	public void setTipoDistribuicaoCotacao(TipoDistribuicaoCotacaoEnum tipoDistribuicaoCotacao) {
		this.tipoDistribuicaoCotacao = tipoDistribuicaoCotacao;
	}

	public CargoVO getCargoVO() {
		return cargoVO;
	}

	public void setCargoVO(CargoVO cargoVO) {
		this.cargoVO = cargoVO;
	}

	public TipoPoliticaDistribuicaoEnum getTipoPoliticaDistribuicao() {
		return tipoPoliticaDistribuicao;
	}

	public void setTipoPoliticaDistribuicao(TipoPoliticaDistribuicaoEnum tipoPoliticaDistribuicao) {
		this.tipoPoliticaDistribuicao = tipoPoliticaDistribuicao;
	}

	public Integer getPrazoExecucao() {
		return prazoExecucao;
	}

	public void setPrazoExecucao(Integer prazoExecucao) {
		this.prazoExecucao = prazoExecucao;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public String getOrientacaoResponsavel() {
		return orientacaoResponsavel;
	}

	public void setOrientacaoResponsavel(String orientacaoResponsavel) {
		this.orientacaoResponsavel = orientacaoResponsavel;
	}

	public boolean isObservacaoObrigatoria() {
		return observacaoObrigatoria;
	}

	public void setObservacaoObrigatoria(boolean observacaoObrigatoria) {
		this.observacaoObrigatoria = observacaoObrigatoria;
	}

	public BigDecimal getValorMinimo() {
		this.valorMinimo = Optional.ofNullable(this.valorMinimo).orElse(BigDecimal.ZERO);
		return valorMinimo;
	}

	public void setValorMinimo(BigDecimal valorMinimo) {
		this.valorMinimo = valorMinimo;
	}

	public TramiteCotacaoCompraVO getTramiteVO() {
		return this.tramiteVO;
	}

	public void setTramiteVO(TramiteCotacaoCompraVO tramiteVO) {
		this.tramiteVO = tramiteVO;
	}

	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	public FuncionarioVO getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(FuncionarioVO funcionario) {
		this.funcionario = funcionario;
	}

	public TipoControleFinanceiroEnum getTipoControleFinanceiro() {
		this.tipoControleFinanceiro = java.util.Optional.ofNullable(this.tipoControleFinanceiro).orElse(TipoControleFinanceiroEnum.NAO_CONTROLA);
		return tipoControleFinanceiro;
	}

	public void setTipoControleFinanceiro(TipoControleFinanceiroEnum tipoControleFinanceiroEnum) {
		this.tipoControleFinanceiro = tipoControleFinanceiroEnum;
	}

	public BigDecimal getValorMaximo() {
		valorMaximo = Optional.ofNullable(this.valorMaximo).orElse(BigDecimal.ZERO);
		return valorMaximo;
	}

	public void setValorMaximo(BigDecimal valorMaximo) {
		this.valorMaximo = valorMaximo;
	}

	@Override
	public int compareTo(DepartamentoTramiteCotacaoCompraVO o) {
		return this.getOrdem().compareTo(o.getOrdem());
	}

	@Override
	public String toString() {
		return "DepartamentoTramiteCotacaoCompraVO [codigo=" + codigo + ", ordem=" + ordem + "]";
	}

	@SuppressWarnings("incomplete-switch")
	public void validarDados() throws Exception {

		Preconditions.checkState(Objects.nonNull(this.getDepartamentoVO()), "O campo DEPARTAMENTO (Departamentos) deve ser informado.");
		Preconditions.checkState(Objects.nonNull(this.getPrazoExecucao()) && this.getPrazoExecucao() > 0, "O campo PRAZO DE EXECUÇÃO (Departamentos) deve ser informado.");

		switch (this.getTipoControleFinanceiro()) {
		case FAIXA_VALORES:
			Preconditions.checkState(Objects.nonNull(this.getValorMaximo()) && this.getValorMaximo().compareTo(BigDecimal.ZERO) > 0, "O campo VALOR MÁXIMO PARA TRAMITAR NO DEPARTAMENTO (Departamentos) deve ser informado.");
			Preconditions.checkState(this.getValorMaximo().compareTo(this.getValorMinimo()) > 0, "O campo VALOR MíNIMO NÃO PODE SER MAIOR QUE VALOR MáXIMO (Departamentos) deve ser informado.");
			break;
		case VALOR_MINIMO:
			Preconditions.checkState(Objects.nonNull(this.getValorMinimo()) && this.getValorMinimo().compareTo(BigDecimal.ZERO) > 0, "O campo VALOR MÍNIMO PARA TRAMITAR NO DEPARTAMENTO (Departamentos) deve ser informado.");
			break;
		}

		this.getTipoDistribuicaoCotacao().equals(TipoDistribuicaoCotacaoEnum.FUNCIONARIO_CARGO_DEPARTAMENTO);

		if (this.getTipoDistribuicaoCotacao().equals(TipoDistribuicaoCotacaoEnum.FUNCIONARIO_CARGO_DEPARTAMENTO) && Objects.isNull(this.getCargoVO())) {
			throw new IllegalStateException("O campo CARGO (Departamentos) deve ser informado.");
		}

		if (this.getTipoDistribuicaoCotacao().equals(TipoDistribuicaoCotacaoEnum.FUNCIONARIO_ESPECIFICO) && Objects.isNull(this.getFuncionario())) {
			throw new IllegalStateException("O campo FUNCIONÁRIO (Departamentos) deve ser informado.");
		}

		if ((this.getTipoDistribuicaoCotacao().equals(TipoDistribuicaoCotacaoEnum.FUNCIONARIO_DEPARTAMENTO) || this.getTipoDistribuicaoCotacao().equals(TipoDistribuicaoCotacaoEnum.FUNCIONARIO_CARGO_DEPARTAMENTO)) && Objects.isNull(this.getTipoPoliticaDistribuicao())) {
			throw new IllegalStateException("O campo TIPO DA POLITICA DE DISTRIBUIÇÃO (Departamentos) deve ser informado.");
		}

	}

}
