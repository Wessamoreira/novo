package negocio.comuns.financeiro;

import java.util.Date;
import java.util.Optional;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.financeiro.enumerador.TipoCentroResultadoOrigemEnum;
import negocio.comuns.financeiro.enumerador.TipoMovimentacaoCentroResultadoOrigemEnum;
import negocio.comuns.financeiro.enumerador.TipoNivelCentroResultadoEnum;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;

/**
 * 
 * @author PedroOtimize
 *
 */
public class CentroResultadoOrigemVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4321922844005911854L;
	private Integer codigo;
	private String codOrigem;
	private TipoCentroResultadoOrigemEnum tipoCentroResultadoOrigemEnum;
	private TipoNivelCentroResultadoEnum tipoNivelCentroResultadoEnum;
	private TipoMovimentacaoCentroResultadoOrigemEnum tipoMovimentacaoCentroResultadoOrigemEnum;
	private Date dataMovimentacao;
	private CategoriaDespesaVO categoriaDespesaVO;
	private CentroReceitaVO centroReceitaVO;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private FuncionarioCargoVO funcionarioCargoVO;
	private DepartamentoVO departamentoVO;
	private CursoVO cursoVO;
	private TurnoVO turnoVO;
	private TurmaVO turmaVO;
	private CentroResultadoVO centroResultadoAdministrativo;
	private Double quantidade;
	private Double valor;
	private Double porcentagem;	

	public CentroResultadoOrigemVO getClone() {
		try {
			CentroResultadoOrigemVO centroResultadoOrigemVO = (CentroResultadoOrigemVO) super.clone();
			centroResultadoOrigemVO.setCodigo(0);
			centroResultadoOrigemVO.setCodOrigem("");
			centroResultadoOrigemVO.setNovoObj(true);
			centroResultadoOrigemVO.setTipoCentroResultadoOrigemEnum(getTipoCentroResultadoOrigemEnum());
			centroResultadoOrigemVO.setCategoriaDespesaVO((CategoriaDespesaVO) getCategoriaDespesaVO().clone());
			centroResultadoOrigemVO.setCentroReceitaVO((CentroReceitaVO) getCentroReceitaVO().clone());
			centroResultadoOrigemVO.setCursoVO((CursoVO) getCursoVO().clone());
			centroResultadoOrigemVO.setTurnoVO((TurnoVO) getTurnoVO().clone());
			centroResultadoOrigemVO.setFuncionarioCargoVO((FuncionarioCargoVO) getFuncionarioCargoVO().clone());
			centroResultadoOrigemVO.setUnidadeEnsinoVO((UnidadeEnsinoVO) getUnidadeEnsinoVO().clone());
			centroResultadoOrigemVO.setTurmaVO((TurmaVO) getTurmaVO().clone());
			centroResultadoOrigemVO.setDepartamentoVO((DepartamentoVO) getDepartamentoVO().clone());
			centroResultadoOrigemVO.setCentroResultadoAdministrativo((CentroResultadoVO) getCentroResultadoAdministrativo().clone());
			centroResultadoOrigemVO.setTipoMovimentacaoCentroResultadoOrigemEnum(getTipoMovimentacaoCentroResultadoOrigemEnum());
			centroResultadoOrigemVO.setDataMovimentacao(getDataMovimentacao());
			return centroResultadoOrigemVO;
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	public void limparCamposPorTipoNivelCentroResultadoEnum() {
		switch (getTipoNivelCentroResultadoEnum()) {
		case CURSO:
			setTurmaVO(new TurmaVO());
			setTurnoVO(new TurnoVO());
			if (!getCategoriaDespesaVO().getExigeCentroCustoRequisitante()) {
				setDepartamentoVO(new DepartamentoVO());
			}
			break;
		case CURSO_TURNO:
			setCursoVO(new CursoVO());
			setTurmaVO(new TurmaVO());
			if (!getCategoriaDespesaVO().getExigeCentroCustoRequisitante()) {
				setDepartamentoVO(new DepartamentoVO());
			}
			break;
		case TURMA:
			setCursoVO(new CursoVO());
			setTurnoVO(new TurnoVO());
			if (!getCategoriaDespesaVO().getExigeCentroCustoRequisitante()) {
				setDepartamentoVO(new DepartamentoVO());
			}
			break;
		case DEPARTAMENTO:
			setCursoVO(new CursoVO());
			setTurnoVO(new TurnoVO());
			setTurmaVO(new TurmaVO());
			break;
		case UNIDADE_ENSINO:
			setCursoVO(new CursoVO());
			setTurnoVO(new TurnoVO());
			setTurmaVO(new TurmaVO());
			if (!getCategoriaDespesaVO().getExigeCentroCustoRequisitante()) {
				setDepartamentoVO(new DepartamentoVO());
			}
			break;
		}
	}

	public void calcularPorcentagem(Double valoTotalCalculoPercentual) {
		if (Uteis.isAtributoPreenchido(valoTotalCalculoPercentual)) {
			setPorcentagem(Uteis.arrendondarForcandoCadasDecimais((100 * getValor()) / valoTotalCalculoPercentual, 8));
		}
	}

	public void calcularValor(Double valoTotalCalculoPercentual) {
		if (Uteis.isAtributoPreenchido(valoTotalCalculoPercentual)) {
			setValor(Uteis.arrendondarForcando2CadasDecimais((valoTotalCalculoPercentual * getPorcentagem()) / 100));
		}
	}

	public Integer getCodigo() {
		codigo = Optional.ofNullable(codigo).orElse(0);
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getCodOrigem() {
		codOrigem = Optional.ofNullable(codOrigem).orElse("");
		return codOrigem;
	}

	public void setCodOrigem(String codOrigem) {
		this.codOrigem = codOrigem;
	}

	public TipoCentroResultadoOrigemEnum getTipoCentroResultadoOrigemEnum() {
		return tipoCentroResultadoOrigemEnum;
	}

	public void setTipoCentroResultadoOrigemEnum(TipoCentroResultadoOrigemEnum tipoCentroResultadoOrigemEnum) {
		this.tipoCentroResultadoOrigemEnum = tipoCentroResultadoOrigemEnum;
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

	public Double getQuantidade() {
		quantidade = Optional.ofNullable(quantidade).orElse(0.0);
		return quantidade;
	}

	public void setQuantidade(Double quantidade) {
		this.quantidade = quantidade;
	}

	public Double getValor() {
		valor = Optional.ofNullable(valor).orElse(0.0);
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public Double getPorcentagem() {
		porcentagem = Optional.ofNullable(porcentagem).orElse(0.0);
		return porcentagem;
	}

	public void setPorcentagem(Double porcentagem) {
		this.porcentagem = porcentagem;
	}

	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		unidadeEnsinoVO = Optional.ofNullable(unidadeEnsinoVO).orElse(new UnidadeEnsinoVO());
		return unidadeEnsinoVO;
	}

	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}

	public FuncionarioCargoVO getFuncionarioCargoVO() {
		funcionarioCargoVO = Optional.ofNullable(funcionarioCargoVO).orElse(new FuncionarioCargoVO());
		return funcionarioCargoVO;
	}

	public void setFuncionarioCargoVO(FuncionarioCargoVO funcionarioCargoVO) {
		this.funcionarioCargoVO = funcionarioCargoVO;
	}

	public DepartamentoVO getDepartamentoVO() {
		departamentoVO = Optional.ofNullable(departamentoVO).orElse(new DepartamentoVO());
		return departamentoVO;
	}

	public void setDepartamentoVO(DepartamentoVO departamentoVO) {
		this.departamentoVO = departamentoVO;
	}

	public CursoVO getCursoVO() {
		cursoVO = Optional.ofNullable(cursoVO).orElse(new CursoVO());
		return cursoVO;
	}

	public void setCursoVO(CursoVO cursoVO) {
		this.cursoVO = cursoVO;
	}

	public TurnoVO getTurnoVO() {
		turnoVO = Optional.ofNullable(turnoVO).orElse(new TurnoVO());
		return turnoVO;
	}

	public void setTurnoVO(TurnoVO turnoVO) {
		this.turnoVO = turnoVO;
	}

	public TurmaVO getTurmaVO() {
		turmaVO = Optional.ofNullable(turmaVO).orElse(new TurmaVO());
		return turmaVO;
	}

	public void setTurmaVO(TurmaVO turmaVO) {
		this.turmaVO = turmaVO;
	}

	public TipoNivelCentroResultadoEnum getTipoNivelCentroResultadoEnum() {
		return tipoNivelCentroResultadoEnum;
	}

	public void setTipoNivelCentroResultadoEnum(TipoNivelCentroResultadoEnum tipoNivelCentroResultadoEnum) {
		this.tipoNivelCentroResultadoEnum = tipoNivelCentroResultadoEnum;
	}

	public TipoMovimentacaoCentroResultadoOrigemEnum getTipoMovimentacaoCentroResultadoOrigemEnum() {
		tipoMovimentacaoCentroResultadoOrigemEnum = Optional.ofNullable(tipoMovimentacaoCentroResultadoOrigemEnum).orElse(TipoMovimentacaoCentroResultadoOrigemEnum.NAO_CONTABILIZAR);
		return tipoMovimentacaoCentroResultadoOrigemEnum;
	}

	public void setTipoMovimentacaoCentroResultadoOrigemEnum(TipoMovimentacaoCentroResultadoOrigemEnum tipoMovimentacaoCentroResultadoOrigemEnum) {
		this.tipoMovimentacaoCentroResultadoOrigemEnum = tipoMovimentacaoCentroResultadoOrigemEnum;
	}

	public Date getDataMovimentacao() {
		dataMovimentacao = Optional.ofNullable(dataMovimentacao).orElse(new Date());
		return dataMovimentacao;
	}

	public void setDataMovimentacao(Date dataMovimentacao) {
		this.dataMovimentacao = dataMovimentacao;
	}

	public CentroReceitaVO getCentroReceitaVO() {
		centroReceitaVO = Optional.ofNullable(centroReceitaVO).orElse(new CentroReceitaVO());
		return centroReceitaVO;
	}

	public void setCentroReceitaVO(CentroReceitaVO centroReceitaVO) {
		this.centroReceitaVO = centroReceitaVO;
	}

	public String getNivelAdministrativo_Apresentar() {
		if (Uteis.isAtributoPreenchido(getTipoNivelCentroResultadoEnum()) && getTipoNivelCentroResultadoEnum().isDepartamento() && Uteis.isAtributoPreenchido(getDepartamentoVO().getNome())) {
			return getUnidadeEnsinoVO().getNome() + " - Dep: " + getDepartamentoVO().getNome();
		} else if (Uteis.isAtributoPreenchido(getTipoNivelCentroResultadoEnum()) && getTipoNivelCentroResultadoEnum().isUnidadeEnsino()) {
			return getUnidadeEnsinoVO().getNome();
		} else if (Uteis.isAtributoPreenchido(getTipoNivelCentroResultadoEnum()) && getTipoNivelCentroResultadoEnum().isCurso() && Uteis.isAtributoPreenchido(getCursoVO().getNome())) {
			return "Curso: " + getCursoVO().getNome();
		} else if (Uteis.isAtributoPreenchido(getTipoNivelCentroResultadoEnum()) && getTipoNivelCentroResultadoEnum().isTurma() && Uteis.isAtributoPreenchido(getTurmaVO().getIdentificadorTurma())) {
			return "Turma: " + getTurmaVO().getIdentificadorTurma();
		} else if (Uteis.isAtributoPreenchido(getTipoNivelCentroResultadoEnum()) && getTipoNivelCentroResultadoEnum().isCursoTurno() && Uteis.isAtributoPreenchido(getCursoVO().getNome()) && Uteis.isAtributoPreenchido(getTurnoVO().getNome())) {
			return "Curso: " + getCursoVO().getNome() + " - Turno:" + getTurnoVO().getNome();
		}
		return "Não Controla";
	}

	public boolean isNivelAdministrativo() {
		return Uteis.isAtributoPreenchido(getNivelAdministrativo_Apresentar());
	}

	public boolean isCategoriaDespesaInformada() {
		return Uteis.isAtributoPreenchido(getCategoriaDespesaVO());
	}

	public boolean equalsAgrupadoCentroResultadoOrigem(CentroResultadoOrigemVO obj) {
		return getCentroReceitaVO().getCodigo().equals(obj.getCentroReceitaVO().getCodigo())
				&& getCategoriaDespesaVO().getCodigo().equals(obj.getCategoriaDespesaVO().getCodigo())
				&& getCentroResultadoAdministrativo().getCodigo().equals(obj.getCentroResultadoAdministrativo().getCodigo());
	}

	public boolean equalsCentroResultadoOrigem(CentroResultadoOrigemVO obj) {
		return getCentroReceitaVO().getCodigo().equals(obj.getCentroReceitaVO().getCodigo())
				&& getCategoriaDespesaVO().getCodigo().equals(obj.getCategoriaDespesaVO().getCodigo())
				&& getCentroResultadoAdministrativo().getCodigo().equals(obj.getCentroResultadoAdministrativo().getCodigo())
				&& getTipoNivelCentroResultadoEnum().equals(obj.getTipoNivelCentroResultadoEnum()) 
				&& getDepartamentoVO().getCodigo().equals(obj.getDepartamentoVO().getCodigo());
	}

	@Override
	public String toString() {
		return "CentroResultadoOrigemVO [categoriaDespesaVO=" + getCategoriaDespesaVO().getCodigo() + ", centroReceitaVO=" + getCentroReceitaVO().getCodigo() + ", centroResultadoAdministrativo=" + getCentroResultadoAdministrativo().getCodigo() + ", nivelCentroResultadoAdministrativo=" + getNivelAdministrativo_Apresentar() + "]";
	}

}
