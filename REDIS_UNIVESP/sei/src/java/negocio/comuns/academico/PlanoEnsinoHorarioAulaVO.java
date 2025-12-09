package negocio.comuns.academico;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.dominios.DiaSemana;

public class PlanoEnsinoHorarioAulaVO extends SuperVO {

	private Integer codigo;
	private PlanoEnsinoVO planoEnsinoVO;
	private DiaSemana diaSemana;
	private String inicioAula;
	private String terminoAula;
	private TurmaVO turmaVO;
	
	public Integer getCodigo() {
		if(codigo == null){
			codigo = 0;
		}
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	public PlanoEnsinoVO getPlanoEnsinoVO() {
		if(planoEnsinoVO == null){
			planoEnsinoVO =  new PlanoEnsinoVO();
		}
		return planoEnsinoVO;
	}
	public void setPlanoEnsinoVO(PlanoEnsinoVO planoEnsinoVO) {
		this.planoEnsinoVO = planoEnsinoVO;
	}
	public DiaSemana getDiaSemana() {
		if(diaSemana == null){
			diaSemana = DiaSemana.SEGUNGA;
		}
		return diaSemana;
	}
	public void setDiaSemana(DiaSemana diaSemana) {
		this.diaSemana = diaSemana;
	}
	public String getInicioAula() {
		if(inicioAula == null){
			inicioAula = "";
		}
		return inicioAula;
	}
	public void setInicioAula(String inicioAula) {
		this.inicioAula = inicioAula;
	}
	public String getTerminoAula() {
		if(terminoAula == null){
			terminoAula = "";
		}
		return terminoAula;
	}
	public void setTerminoAula(String terminoAula) {
		this.terminoAula = terminoAula;
	}
	public TurmaVO getTurmaVO() {
		if(turmaVO == null){
			turmaVO = new TurmaVO();
		}
		return turmaVO;
	}
	public void setTurmaVO(TurmaVO turmaVO) {
		this.turmaVO = turmaVO;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((diaSemana == null) ? 0 : diaSemana.hashCode());
		result = prime * result + ((inicioAula == null) ? 0 : inicioAula.hashCode());
		result = prime * result + ((terminoAula == null) ? 0 : terminoAula.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PlanoEnsinoHorarioAulaVO other = (PlanoEnsinoHorarioAulaVO) obj;
		if (diaSemana != other.diaSemana)
			return false;
		if (inicioAula == null) {
			if (other.inicioAula != null)
				return false;
		} else if (!inicioAula.equals(other.inicioAula))
			return false;
		if (terminoAula == null) {
			if (other.terminoAula != null)
				return false;
		} else if (!terminoAula.equals(other.terminoAula))
			return false;
		return true;
	}

	public String getOrdenacao(){
		return getDiaSemana().getValor()+getInicioAula()+getTerminoAula();
	} 
	
}
