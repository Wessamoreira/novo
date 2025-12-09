package negocio.comuns.administrativo;

import java.util.Optional;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.financeiro.CentroResultadoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;

public class UnidadeEnsinoNivelEducacionalCentroResultadoVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3011194786561199983L;
	private Integer codigo;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private CentroResultadoVO centroResultadoVO;
	private TipoNivelEducacional tipoNivelEducacional;

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

	public CentroResultadoVO getCentroResultadoVO() {
		centroResultadoVO = Optional.ofNullable(centroResultadoVO).orElse(new CentroResultadoVO());
		return centroResultadoVO;
	}

	public void setCentroResultadoVO(CentroResultadoVO centroResultadoVO) {
		this.centroResultadoVO = centroResultadoVO;
	}

	public TipoNivelEducacional getTipoNivelEducacional() {
		return tipoNivelEducacional;
	}

	public void setTipoNivelEducacional(TipoNivelEducacional tipoNivelEducacional) {
		this.tipoNivelEducacional = tipoNivelEducacional;
	}
	
	public boolean equalsCampoSelecaoLista(UnidadeEnsinoNivelEducacionalCentroResultadoVO obj){
		return Uteis.isAtributoPreenchido(getTipoNivelEducacional()) && Uteis.isAtributoPreenchido(obj.getTipoNivelEducacional()) && getTipoNivelEducacional().equals(obj.getTipoNivelEducacional());		
	}

}
