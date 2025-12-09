package negocio.comuns.academico;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;

public class CensoUnidadeEnsinoVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3738566882462256295L;
	private Integer codigo;
	private CensoVO censoVO;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private  Boolean selecionado;
	
	public Integer getCodigo() {
		if(codigo == null) {
			codigo= 0;
		}
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	public CensoVO getCensoVO() {
		if(censoVO == null) {
			censoVO =  new CensoVO();
		}
		return censoVO;
	}
	public void setCensoVO(CensoVO censoVO) {
		this.censoVO = censoVO;
	}
	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if(unidadeEnsinoVO == null) {
			unidadeEnsinoVO =  new UnidadeEnsinoVO();
		}
		return unidadeEnsinoVO;
	}
	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}

	public boolean getIsSelecionado() {
		if (selecionado == null) {
			selecionado = false;
		}
		return selecionado;
	}

	public void setSelecionado(Boolean selecionado) {
		this.selecionado = selecionado;
	}

	public Boolean getSelecionado() {
		if (selecionado == null) {
			selecionado = Boolean.FALSE;
		}
		return selecionado;
	}
		
	
}
