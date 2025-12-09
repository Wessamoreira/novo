/**
 * 
 */
package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Rodrigo Wind
 *
 */
public class ChoqueHorarioAlunoDetalheVO {
	/**
	 * @author Rodrigo Wind - 16/09/2015
	 */	
	private Date data;
	private String horarioInicio;
	private String horarioTermino;
	private List<TurmaDisciplinaVO> turmaDisciplinaVOs;
	
	/**
	 * @return the turmaDisciplinaVOs
	 */
	public List<TurmaDisciplinaVO> getTurmaDisciplinaVOs() {
		if (turmaDisciplinaVOs == null) {
			turmaDisciplinaVOs = new ArrayList<TurmaDisciplinaVO>();
		}
		return turmaDisciplinaVOs;
	}
	/**
	 * @param turmaDisciplinaVOs the turmaDisciplinaVOs to set
	 */
	public void setTurmaDisciplinaVOs(List<TurmaDisciplinaVO> turmaDisciplinaVOs) {
		this.turmaDisciplinaVOs = turmaDisciplinaVOs;
	}
	/**
	 * @return the data
	 */
	public Date getData() {
		if (data == null) {
			data = new Date();
		}
		return data;
	}
	/**
	 * @param data the data to set
	 */
	public void setData(Date data) {
		this.data = data;
	}
	/**
	 * @return the horarioInicio
	 */
	public String getHorarioInicio() {
		if (horarioInicio == null) {
			horarioInicio = "";
		}
		return horarioInicio;
	}
	/**
	 * @param horarioInicio the horarioInicio to set
	 */
	public void setHorarioInicio(String horarioInicio) {
		this.horarioInicio = horarioInicio;
	}
	/**
	 * @return the horarioTermino
	 */
	public String getHorarioTermino() {
		if (horarioTermino == null) {
			horarioTermino = "";
		}
		return horarioTermino;
	}
	/**
	 * @param horarioTermino the horarioTermino to set
	 */
	public void setHorarioTermino(String horarioTermino) {
		this.horarioTermino = horarioTermino;
	}
	
	public Integer getQtdeTurmaDisciplinaVOs(){
		return getTurmaDisciplinaVOs().size();
	}
	
	
}
