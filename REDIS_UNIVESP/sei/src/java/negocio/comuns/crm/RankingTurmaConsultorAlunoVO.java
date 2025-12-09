package negocio.comuns.crm;

import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;

public class RankingTurmaConsultorAlunoVO {
	
	private MatriculaVO matriculaVO;
	private TurmaVO turmaVO;
	private String motivo;
	private Double valorTotalAReceberTicketMedioCRM;	
	
	public MatriculaVO getMatriculaVO() {
		if(matriculaVO == null){
			matriculaVO = new MatriculaVO();
		}
		return matriculaVO;
	}
	public void setMatriculaVO(MatriculaVO matriculaVO) {
		this.matriculaVO = matriculaVO;
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
	public String getMotivo() {		
		return motivo;
	}
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
	public Double getValorTotalAReceberTicketMedioCRM() {
		if (valorTotalAReceberTicketMedioCRM == null) {
			valorTotalAReceberTicketMedioCRM = 0.0;
		}
		return valorTotalAReceberTicketMedioCRM;
	}
	public void setValorTotalAReceberTicketMedioCRM(Double valorTotalAReceberTicketMedioCRM) {
		this.valorTotalAReceberTicketMedioCRM = valorTotalAReceberTicketMedioCRM;
	}
	
	

}
