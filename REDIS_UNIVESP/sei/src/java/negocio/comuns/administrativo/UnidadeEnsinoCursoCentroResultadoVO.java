package negocio.comuns.administrativo;

import java.util.Optional;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.arquitetura.SuperVO;
/**
 * 
 * @author PedroOtimize
 *
 */
import negocio.comuns.financeiro.CentroResultadoVO;
import negocio.comuns.utilitarias.Uteis;

public class UnidadeEnsinoCursoCentroResultadoVO extends SuperVO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6524360336967147080L;
	private Integer codigo;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private CursoVO cursoVO;
	private CentroResultadoVO centroResultadoVO;

	public Integer getCodigo() {
		codigo = Optional.ofNullable(codigo).orElse(0);
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		unidadeEnsinoVO = Optional.ofNullable(unidadeEnsinoVO).orElse(new UnidadeEnsinoVO());
		return unidadeEnsinoVO;
	}

	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}

	public CursoVO getCursoVO() {
		cursoVO = Optional.ofNullable(cursoVO).orElse(new CursoVO());
		return cursoVO;
	}

	public void setCursoVO(CursoVO cursoVO) {
		this.cursoVO = cursoVO;
	}

	public CentroResultadoVO getCentroResultadoVO() {
		centroResultadoVO = Optional.ofNullable(centroResultadoVO).orElse(new CentroResultadoVO());
		return centroResultadoVO;
	}

	public void setCentroResultadoVO(CentroResultadoVO centroResultadoVO) {
		this.centroResultadoVO = centroResultadoVO;
	}
	

	public boolean equalsCampoSelecaoLista(UnidadeEnsinoCursoCentroResultadoVO obj){
		return Uteis.isAtributoPreenchido(getCursoVO()) && Uteis.isAtributoPreenchido(obj.getCursoVO()) && getCursoVO().getCodigo().equals(obj.getCursoVO().getCodigo());
		
	}

}
