package negocio.comuns.financeiro;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.financeiro.enumerador.TipoCentroNegocioEnum;

/***
 * 
 * @author PedroOtimize
 *
 */
public class CategoriaDespesaRateioVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2127435825062168068L;
	private Integer codigo;
	private CategoriaDespesaVO categoriaDespesaVO;
	private TipoCentroNegocioEnum tipoCategoriaDespesaRateioEnum;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private CursoVO cursoVO;
	private DepartamentoVO departamentoVO;
	private Double porcentagem;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public CategoriaDespesaVO getCategoriaDespesaVO() {
		if (categoriaDespesaVO == null) {
			categoriaDespesaVO = new CategoriaDespesaVO();
		}
		return categoriaDespesaVO;
	}

	public void setCategoriaDespesaVO(CategoriaDespesaVO categoriaDespesaVO) {
		this.categoriaDespesaVO = categoriaDespesaVO;
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

	public Double getPorcentagem() {
		return porcentagem;
	}

	public void setPorcentagem(Double porcentagem) {
		this.porcentagem = porcentagem;
	}

	public TipoCentroNegocioEnum getTipoCategoriaDespesaRateioEnum() {
		return tipoCategoriaDespesaRateioEnum;
	}

	public void setTipoCategoriaDespesaRateioEnum(TipoCentroNegocioEnum tipoCategoriaDespesaRateioEnum) {
		this.tipoCategoriaDespesaRateioEnum = tipoCategoriaDespesaRateioEnum;
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

	public boolean equalsCampoSelecaoLista(CategoriaDespesaRateioVO obj) {
		if (getTipoCategoriaDespesaRateioEnum().isAcademico() && getUnidadeEnsinoVO().getCodigo().equals(obj.getUnidadeEnsinoVO().getCodigo()) && getCursoVO().getCodigo().equals(obj.getCursoVO().getCodigo())) {
			return true;
		}
		if (getTipoCategoriaDespesaRateioEnum().isAdministrativo() && getUnidadeEnsinoVO().getCodigo().equals(obj.getUnidadeEnsinoVO().getCodigo()) && getDepartamentoVO().getCodigo().equals(obj.getDepartamentoVO().getCodigo())) {
			return true;
		}
		return false;
	}

}
