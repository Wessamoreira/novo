package negocio.comuns.secretaria;

import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.arquitetura.SuperVO;

public class CalendarioAgrupamentoDisciplinaVO extends SuperVO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2766962669593324074L;
	private Integer codigo;
	private CalendarioAgrupamentoTccVO calendarioAgrupamentoVO;
	private DisciplinaVO disciplinaVO;
	private Boolean selecionado;
	
	public Integer getCodigo() {
		if(codigo == null) {
			codigo =  0;
		}
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	public CalendarioAgrupamentoTccVO getCalendarioAgrupamentoVO() {
		if(calendarioAgrupamentoVO == null) {
			calendarioAgrupamentoVO =  new CalendarioAgrupamentoTccVO();
		}
		return calendarioAgrupamentoVO;
	}
	public void setCalendarioAgrupamentoVO(CalendarioAgrupamentoTccVO calendarioAgrupamentoVO) {
		this.calendarioAgrupamentoVO = calendarioAgrupamentoVO;
	}
	public DisciplinaVO getDisciplinaVO() {
		if(disciplinaVO == null) {
			disciplinaVO =  new DisciplinaVO();
		}
		return disciplinaVO;
	}
	public void setDisciplinaVO(DisciplinaVO disciplinaVO) {
		this.disciplinaVO = disciplinaVO;
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
	

	public String getOrdenacao() {
		return getDisciplinaVO().getAbreviatura()+getDisciplinaVO().getNome();
	}
		
	

}
