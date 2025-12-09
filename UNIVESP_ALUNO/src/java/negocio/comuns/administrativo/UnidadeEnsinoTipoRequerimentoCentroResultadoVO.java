package negocio.comuns.administrativo;

import java.util.Optional;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.protocolo.TipoRequerimentoVO;
import negocio.comuns.utilitarias.Uteis;

public class UnidadeEnsinoTipoRequerimentoCentroResultadoVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4075494263297855121L;
	private Integer codigo;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private TipoRequerimentoVO tipoRequerimentoVO;

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

	
	public TipoRequerimentoVO getTipoRequerimentoVO() {
		tipoRequerimentoVO = Optional.ofNullable(tipoRequerimentoVO).orElse(new TipoRequerimentoVO());
		return tipoRequerimentoVO;
	}

	public void setTipoRequerimentoVO(TipoRequerimentoVO tipoRequerimentoVO) {
		this.tipoRequerimentoVO = tipoRequerimentoVO;
	}
	
	public boolean equalsCampoSelecaoLista(UnidadeEnsinoTipoRequerimentoCentroResultadoVO obj){
		return Uteis.isAtributoPreenchido(getTipoRequerimentoVO()) && Uteis.isAtributoPreenchido(obj.getTipoRequerimentoVO()) && getTipoRequerimentoVO().getCodigo().equals(obj.getTipoRequerimentoVO().getCodigo());		
	}

}
