package negocio.comuns.academico;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.Uteis;

public class EnadeCursoVO extends SuperVO {

	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private EnadeVO enadeVO;
	private CursoVO cursoVO;
	private String anoBaseIngressante;
	private Integer percentualCargaHorariaIngressante;
	private Integer percentualCargaHorariaConcluinte;
	private Date periodoPrevistoTerminoIngressante;
	private Date periodoPrevistoTerminoConcluinte;
	private Boolean considerarCargaHorariaAtividadeComplementar;
	private Boolean considerarCargaHorariaEstagio;

	public EnadeCursoVO() {
		super();
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

	public EnadeVO getEnadeVO() {
		if (enadeVO == null) {
			enadeVO = new EnadeVO();
		}
		return enadeVO;
	}

	public void setEnadeVO(EnadeVO enadeVO) {
		this.enadeVO = enadeVO;
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

	public String getAnoBaseIngressante() {
		if (anoBaseIngressante == null) {
			anoBaseIngressante = "";
		}
		return anoBaseIngressante;
	}

	public void setAnoBaseIngressante(String anoBaseIngressante) {
		this.anoBaseIngressante = anoBaseIngressante;
	}

	public Integer getPercentualCargaHorariaIngressante() {
		if (percentualCargaHorariaIngressante == null) {
			percentualCargaHorariaIngressante = 0;
		}
		return percentualCargaHorariaIngressante;
	}

	public void setPercentualCargaHorariaIngressante(Integer percentualCargaHorariaIngressante) {
		this.percentualCargaHorariaIngressante = percentualCargaHorariaIngressante;
	}

	public Integer getPercentualCargaHorariaConcluinte() {
		if (percentualCargaHorariaConcluinte == null) {
			percentualCargaHorariaConcluinte = 0;
		}
		return percentualCargaHorariaConcluinte;
	}

	public void setPercentualCargaHorariaConcluinte(Integer percentualCargaHorariaConcluinte) {
		this.percentualCargaHorariaConcluinte = percentualCargaHorariaConcluinte;
	}

	public Date getPeriodoPrevistoTerminoIngressante() {
		if (periodoPrevistoTerminoIngressante == null) {
			periodoPrevistoTerminoIngressante = new Date();
		}
		return periodoPrevistoTerminoIngressante;
	}

	public void setPeriodoPrevistoTerminoIngressante(Date periodoPrevistoTerminoIngressante) {
		this.periodoPrevistoTerminoIngressante = periodoPrevistoTerminoIngressante;
	}

	public Date getPeriodoPrevistoTerminoConcluinte() {
		if (periodoPrevistoTerminoConcluinte == null) {
			periodoPrevistoTerminoConcluinte = new Date();
		}
		return periodoPrevistoTerminoConcluinte;
	}

	public void setPeriodoPrevistoTerminoConcluinte(Date periodoPrevistoTerminoConcluinte) {
		this.periodoPrevistoTerminoConcluinte = periodoPrevistoTerminoConcluinte;
	}
	
	public String getPeriodoPrevistoTerminoIngressante_Apresentar() {
		return Uteis.getDataAno4Digitos(getPeriodoPrevistoTerminoIngressante());
	}
	
	public String getPeriodoPrevistoTerminoConcluinte_Apresentar() {
		return Uteis.getDataAno4Digitos(getPeriodoPrevistoTerminoConcluinte());
	}

	public Boolean getConsiderarCargaHorariaAtividadeComplementar() {
	if (considerarCargaHorariaAtividadeComplementar == null) {
		considerarCargaHorariaAtividadeComplementar = Boolean.TRUE;
	}
	return considerarCargaHorariaAtividadeComplementar;}
	

	public void setConsiderarCargaHorariaAtividadeComplementar(Boolean considerarCargaHorariaAtividadeComplementar) {
		this.considerarCargaHorariaAtividadeComplementar = considerarCargaHorariaAtividadeComplementar;
	}

	public Boolean getConsiderarCargaHorariaEstagio() {
	if (considerarCargaHorariaEstagio == null) {
		considerarCargaHorariaEstagio = Boolean.TRUE;
	}
	return considerarCargaHorariaEstagio;}
	

	public void setConsiderarCargaHorariaEstagio(Boolean considerarCargaHorariaEstagio) {
		this.considerarCargaHorariaEstagio = considerarCargaHorariaEstagio;
	}

}
