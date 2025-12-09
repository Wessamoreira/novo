package negocio.comuns.academico;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;

public class ProcessoMatriculaUnidadeEnsinoVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6111258469051032941L;
	private Integer codigo;
	private ProcessoMatriculaVO processoMatriculaVO;
	private UnidadeEnsinoVO unidadeEnsinoVO;	
	private  Boolean selecionado;
	
	
	public Integer getCodigo() {
		if(codigo == null) {
			codigo = 0;
		}
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	public ProcessoMatriculaVO getProcessoMatriculaVO() {
		if(processoMatriculaVO == null) {
			processoMatriculaVO = new ProcessoMatriculaVO();
		}
		return processoMatriculaVO;
	}
	public void setProcessoMatriculaVO(ProcessoMatriculaVO processoMatriculaVO) {
		this.processoMatriculaVO = processoMatriculaVO;
	}
	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if(unidadeEnsinoVO == null) {
			unidadeEnsinoVO = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoVO;
	}
	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}
	
    
	
	public Boolean getSelecionado() {
	if(selecionado == null) {
		selecionado =  false;
	}
	return selecionado;
	}
	public void setSelecionado(Boolean selecionado) {
		this.selecionado = selecionado;
	}
	
}
