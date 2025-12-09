package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.List;

public class RegistroAtividadeComplementarMatriculaPeriodoVO {
	
	private String ano;	
	private String semestre;
	private Integer cargaHorariaEvento;
	private Integer cargaHorariaConsiderada;	
	private Integer cargaHorariaRealizada;	
	private List<EventoAtividadeComplementarVO> eventoAtividadeComplementarVOs;	
	
	public RegistroAtividadeComplementarMatriculaPeriodoVO() {
		super();	
	}	
	
	public RegistroAtividadeComplementarMatriculaPeriodoVO(String ano, String semestre) {
		super();
		this.ano = ano;
		this.semestre = semestre;
	}



	public String getAno() {
		if (ano == null) {
			ano = "";
		}
		return ano;
	}
	public void setAno(String ano) {
		this.ano = ano;
	}
	public String getSemestre() {
		if (semestre == null) {
			semestre = "";
		}
		return semestre;
	}
	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}
	
	public List<EventoAtividadeComplementarVO> getEventoAtividadeComplementarVOs() {
		if (eventoAtividadeComplementarVOs == null) {
			eventoAtividadeComplementarVOs = new ArrayList<EventoAtividadeComplementarVO>(0);
		}
		return eventoAtividadeComplementarVOs;
	}

	public void setEventoAtividadeComplementarVOs(List<EventoAtividadeComplementarVO> eventoAtividadeComplementarVOs) {
		this.eventoAtividadeComplementarVOs = eventoAtividadeComplementarVOs;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ano == null) ? 0 : ano.hashCode());
		result = prime * result + ((semestre == null) ? 0 : semestre.hashCode());
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
		RegistroAtividadeComplementarMatriculaPeriodoVO other = (RegistroAtividadeComplementarMatriculaPeriodoVO) obj;
		if (ano == null) {
			if (other.ano != null)
				return false;
		} else if (!ano.equals(other.ano))
			return false;
		if (semestre == null) {
			if (other.semestre != null)
				return false;
		} else if (!semestre.equals(other.semestre))
			return false;
		return true;
	}
	
	public String getOrdenacao() {
		return getAno().isEmpty() ? "" : getSemestre().isEmpty() ? getAno() : getAno()+"/"+getSemestre();
	}

	public Integer getCargaHorariaEvento() {
		if (cargaHorariaEvento == null) {
			cargaHorariaEvento = 0;
		}
		return cargaHorariaEvento;
	}

	public void setCargaHorariaEvento(Integer cargaHorariaEvento) {
		this.cargaHorariaEvento = cargaHorariaEvento;
	}

	public Integer getCargaHorariaConsiderada() {
		if (cargaHorariaConsiderada == null) {
			cargaHorariaConsiderada = 0;
		}
		return cargaHorariaConsiderada;
	}

	public void setCargaHorariaConsiderada(Integer cargaHorariaConsiderada) {
		this.cargaHorariaConsiderada = cargaHorariaConsiderada;
	}

	public Integer getCargaHorariaRealizada() {
		if (cargaHorariaRealizada == null) {
			cargaHorariaRealizada = 0;
		}
		return cargaHorariaRealizada;
	}

	public void setCargaHorariaRealizada(Integer cargaHorariaRealizada) {
		this.cargaHorariaRealizada = cargaHorariaRealizada;
	}
	

	
}
