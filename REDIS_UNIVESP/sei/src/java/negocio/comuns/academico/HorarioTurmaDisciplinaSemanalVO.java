package negocio.comuns.academico;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.dominios.DiaSemana;

public class HorarioTurmaDisciplinaSemanalVO extends SuperVO implements Serializable{

	private static final long serialVersionUID = 1L;
	private DiaSemana diaSemana;
	private Integer nrDiaSemana;
	private Date dataInicio;
	private Date dataTermino;
	private List<String> listaHorario;

	public HorarioTurmaDisciplinaSemanalVO() {
		super();
	}

	public DiaSemana getDiaSemana() {
		if (diaSemana == null) {
			diaSemana = DiaSemana.NENHUM;
		}
		return diaSemana;
	}

	public void setDiaSemana(DiaSemana diaSemana) {
		this.diaSemana = diaSemana;
	}

	public List<String> getListaHorario() {
		if (listaHorario == null) {
			listaHorario = new ArrayList<String>(0);
		}
		return listaHorario;
	}

	public void setListaHorario(List<String> listaHorario) {
		this.listaHorario = listaHorario;
	}

	public Integer getNrDiaSemana() {
		if (nrDiaSemana == null) {
			nrDiaSemana = 0;
		}
		return nrDiaSemana;
	}

	public void setNrDiaSemana(Integer nrDiaSemana) {
		this.nrDiaSemana = nrDiaSemana;
	}

	public Date getDataInicio() {
		if (dataInicio == null) {
			dataInicio = new Date();
		}
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataTermino() {
		if (dataTermino == null) {
			dataTermino = new Date();
		}
		return dataTermino;
	}

	public void setDataTermino(Date dataTermino) {
		this.dataTermino = dataTermino;
	}

}
