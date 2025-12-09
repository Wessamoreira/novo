package negocio.comuns.contabil;

import java.util.Optional;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.financeiro.CentroResultadoVO;
import negocio.comuns.financeiro.enumerador.TipoCentroNegocioEnum;
import negocio.comuns.utilitarias.Uteis;

/**
 * 
 * @author PedroOtimize
 *
 */
public class LancamentoContabilCentroNegocioVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3858813978404361063L;
	private Integer codigo;
	private LancamentoContabilVO lancamentoContabilVO;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private DepartamentoVO departamentoVO;
	private CursoVO cursoVO;
	private TurnoVO turnoVO;
	private TurmaVO turmaVO;
	private CentroResultadoVO centroResultadoAdministrativo;
	private TipoCentroNegocioEnum tipoCentroNegocioEnum;
	private String codigoContabil;
	private String nomeContabil;
	private String nivelContabil;
	private Double percentual;
	private Double valor;

	public void recalcularLancmentoValor() throws Exception {
		if (getLancamentoContabilVO().getValor() == 0.0) {
			throw new Exception("Não e possível realizar o calculo de rateio, pois o valor do lancamento contábil é Zero.");
		}
		setPercentual(Uteis.arrendondarForcando2CadasDecimais((getValor() * 100) / getLancamentoContabilVO().getValor()));
	}

	public void recalcularLancmentoPercentual() {
		setValor(Uteis.arrendondarForcando2CadasDecimais((getPercentual() * getLancamentoContabilVO().getValor()) / 100));
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

	public LancamentoContabilVO getLancamentoContabilVO() {
		if (lancamentoContabilVO == null) {
			lancamentoContabilVO = new LancamentoContabilVO();
		}
		return lancamentoContabilVO;
	}

	public void setLancamentoContabilVO(LancamentoContabilVO lancamentoContabilVO) {
		this.lancamentoContabilVO = lancamentoContabilVO;
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

	public CursoVO getCursoVO() {
		if (cursoVO == null) {
			cursoVO = new CursoVO();
		}
		return cursoVO;
	}

	public void setCursoVO(CursoVO cursoVO) {
		this.cursoVO = cursoVO;
	}

	public DepartamentoVO getDepartamentoVO() {
		if (departamentoVO == null) {
			departamentoVO = new DepartamentoVO();
		}
		return departamentoVO;
	}

	public void setDepartamentoVO(DepartamentoVO departamentoVO) {
		this.departamentoVO = departamentoVO;
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

	public CentroResultadoVO getCentroResultadoAdministrativo() {
		centroResultadoAdministrativo = Optional.ofNullable(centroResultadoAdministrativo).orElse(new CentroResultadoVO());
		return centroResultadoAdministrativo;
	}

	public void setCentroResultadoAdministrativo(CentroResultadoVO centroResultadoAdministrativo) {
		this.centroResultadoAdministrativo = centroResultadoAdministrativo;
	}	

	public String getCodigoContabil() {
		if (codigoContabil == null) {
			codigoContabil = "";
		}
		return codigoContabil;
	}

	public void setCodigoContabil(String codigoContabil) {
		this.codigoContabil = codigoContabil;
	}

	public String getNomeContabil() {
		if (nomeContabil == null) {
			nomeContabil = "";
		}
		return nomeContabil;
	}

	public void setNomeContabil(String nomeContabil) {
		this.nomeContabil = nomeContabil;
	}

	public Double getPercentual() {
		if (percentual == null) {
			percentual = 0.0;
		}
		return percentual;
	}

	public void setPercentual(Double percentual) {
		this.percentual = percentual;
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

	public TipoCentroNegocioEnum getTipoCentroNegocioEnum() {
		tipoCentroNegocioEnum = Optional.ofNullable(tipoCentroNegocioEnum).orElse(TipoCentroNegocioEnum.ADMINISTRATIVO);
		return tipoCentroNegocioEnum;
	}

	public void setTipoCentroNegocioEnum(TipoCentroNegocioEnum tipoCentroNegocioEnum) {
		this.tipoCentroNegocioEnum = tipoCentroNegocioEnum;
	}

	public String getRateioContabil_Apresentacao() {
		if (isRateioAcademico()) {
			if (Uteis.isAtributoPreenchido(getTurmaVO())) {
				return "Turma - " + getTurmaVO().getIdentificadorTurma();
			} else if (Uteis.isAtributoPreenchido(getTurnoVO())) {
				return "Curso: " + getCursoVO().getNome() + " - Turno:" + getTurnoVO().getNome();
			}
			return "Curso - " + getCursoVO().getNome();
		} else if (isRateioAdministrativo()) {
			if (Uteis.isAtributoPreenchido(getDepartamentoVO())) {
				return "Departamento - " + getDepartamentoVO().getNome();
			}
			return getUnidadeEnsinoVO().getNome();
		}
		return "";
	}

	public boolean isRateioAcademico() {
		return Uteis.isAtributoPreenchido(getTipoCentroNegocioEnum()) && getTipoCentroNegocioEnum().equals(TipoCentroNegocioEnum.ACADEMICO);
	}

	public boolean isRateioAdministrativo() {
		return Uteis.isAtributoPreenchido(getTipoCentroNegocioEnum()) && getTipoCentroNegocioEnum().equals(TipoCentroNegocioEnum.ADMINISTRATIVO);
	}

	public boolean equalsCampoSelecaoLista(LancamentoContabilCentroNegocioVO obj) {
		return getUnidadeEnsinoVO().getCodigo().equals(obj.getUnidadeEnsinoVO().getCodigo()) &&
				((isRateioAcademico() 
						&& getCursoVO().getCodigo().equals(obj.getCursoVO().getCodigo()) 
						&& getTurnoVO().getCodigo().equals(obj.getTurnoVO().getCodigo()) 
						&& getTurmaVO().getCodigo().equals(obj.getTurmaVO().getCodigo()))
				  ||
				  (isRateioAdministrativo() 
						&& getDepartamentoVO().getCodigo().equals(obj.getDepartamentoVO().getCodigo())));
	}	

	public String getNivelContabil() {
		if (nivelContabil == null) {
			nivelContabil = "";
		}
		return nivelContabil;
	}

	public void setNivelContabil(String nivelContabil) {
		this.nivelContabil = nivelContabil;
	}
}
