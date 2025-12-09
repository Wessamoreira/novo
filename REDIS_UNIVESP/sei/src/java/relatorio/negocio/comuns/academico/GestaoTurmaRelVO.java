package relatorio.negocio.comuns.academico;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.academico.HorarioTurmaVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.VagaTurmaDisciplinaVO;

public class GestaoTurmaRelVO {

	private TurmaVO turmaVO;
	private List<GestaoTurmaDisciplinaRelVO> gestaoTurmaDisciplinaRelVOs;
	/**
	 * Usado para Subturmas praticas, teoricas e agrupadas
	 */
	private List<MatriculaVO> matriculadoVOs;
	private List<MatriculaVO> preMatriculadoVOs;
	private String totalMatriculado;
	private String totalPreMatriculado;
	private Integer totalVaga;
	private Integer totalMaximaVaga;
	private HorarioTurmaVO horarioTurmaVO;
	private Integer cargaHoraria;
	private String horaAulaRegistrada;
	private String professor;
	private Integer cargaHorariaPratica;
	private Integer cargaHorariaTeorica;
	private Integer horaAula;	
	private Integer qtdeAulaProgramada;
	private String horaProgramada;
	private Integer qtdeAulaRegistrada;
	private VagaTurmaDisciplinaVO vagaTurmaDisciplinaVO;

	public TurmaVO getTurmaVO() {
		if (turmaVO == null) {
			turmaVO = new TurmaVO();
		}
		return turmaVO;
	}

	public void setTurmaVO(TurmaVO turmaVO) {
		this.turmaVO = turmaVO;
	}

	public List<GestaoTurmaDisciplinaRelVO> getGestaoTurmaDisciplinaRelVOs() {
		if (gestaoTurmaDisciplinaRelVOs == null) {
			gestaoTurmaDisciplinaRelVOs = new ArrayList<GestaoTurmaDisciplinaRelVO>(0);
		}
		return gestaoTurmaDisciplinaRelVOs;
	}

	public void setGestaoTurmaDisciplinaRelVOs(List<GestaoTurmaDisciplinaRelVO> gestaoTurmaDisciplinaRelVOs) {
		this.gestaoTurmaDisciplinaRelVOs = gestaoTurmaDisciplinaRelVOs;
	}

	public List<MatriculaVO> getMatriculadoVOs() {
		if (matriculadoVOs == null) {
			matriculadoVOs = new ArrayList<MatriculaVO>(0);
		}
		return matriculadoVOs;
	}

	public void setMatriculadoVOs(List<MatriculaVO> matriculadoVOs) {
		this.matriculadoVOs = matriculadoVOs;
	}

	public List<MatriculaVO> getPreMatriculadoVOs() {
		if (preMatriculadoVOs == null) {
			preMatriculadoVOs = new ArrayList<MatriculaVO>(0);
		}
		return preMatriculadoVOs;
	}

	public void setPreMatriculadoVOs(List<MatriculaVO> preMatriculadoVOs) {
		this.preMatriculadoVOs = preMatriculadoVOs;
	}

	public String getTotalMatriculado() {
		if (totalMatriculado == null) {
			totalMatriculado = "0";
		}
		return totalMatriculado;
	}

	public void setTotalMatriculado(String totalMatriculado) {
		this.totalMatriculado = totalMatriculado;
	}

	public String getTotalPreMatriculado() {
		if (totalPreMatriculado == null) {
			totalPreMatriculado = "0";
		}
		return totalPreMatriculado;
	}

	public void setTotalPreMatriculado(String totalPreMatriculado) {
		this.totalPreMatriculado = totalPreMatriculado;
	}

	public Integer getTotalVaga() {
		if (totalVaga == null) {
			totalVaga = 0;
		}
		return totalVaga;
	}

	public void setTotalVaga(Integer totalVaga) {
		this.totalVaga = totalVaga;
	}

	public HorarioTurmaVO getHorarioTurmaVO() {
		if (horarioTurmaVO == null) {
			horarioTurmaVO = new HorarioTurmaVO();
		}
		return horarioTurmaVO;
	}

	public void setHorarioTurmaVO(HorarioTurmaVO horarioTurmaVO) {
		this.horarioTurmaVO = horarioTurmaVO;
	}

	public VagaTurmaDisciplinaVO getVagaTurmaDisciplinaVO() {
		if (vagaTurmaDisciplinaVO == null) {
			vagaTurmaDisciplinaVO = new VagaTurmaDisciplinaVO();
		}
		return vagaTurmaDisciplinaVO;
	}

	public void setVagaTurmaDisciplinaVO(VagaTurmaDisciplinaVO vagaTurmaDisciplinaVO) {
		this.vagaTurmaDisciplinaVO = vagaTurmaDisciplinaVO;
	}
	

	public Integer getTotalMaximaVaga() {
		if(totalMaximaVaga == null){
			totalMaximaVaga = 0;
		}
		return totalMaximaVaga;
	}

	public void setTotalMaximaVaga(Integer totalMaximaVaga) {
		this.totalMaximaVaga = totalMaximaVaga;
	}
	
	public Integer getCargaHorariaPratica() {
		if(cargaHorariaPratica == null){
			cargaHorariaPratica = 0;
		}
		return cargaHorariaPratica;
	}

	public void setCargaHorariaPratica(Integer cargaHorariaPratica) {
		this.cargaHorariaPratica = cargaHorariaPratica;
	}

	public Integer getCargaHorariaTeorica() {
		if(cargaHorariaTeorica == null){
			cargaHorariaTeorica = 0;
		}
		return cargaHorariaTeorica;
	}

	public void setCargaHorariaTeorica(Integer cargaHorariaTeorica) {
		this.cargaHorariaTeorica = cargaHorariaTeorica;
	}

	public Integer getHoraAula() {
		if(horaAula == null){
			horaAula = 0;
		}
		return horaAula;
	}

	public void setHoraAula(Integer horaAula) {
		this.horaAula = horaAula;
	}

	public String getHoraProgramada() {
		if(horaProgramada == null){
			horaProgramada = "";
		}
		return horaProgramada;
	}

	public void setHoraProgramada(String horaProgramada) {
		this.horaProgramada = horaProgramada;
	}

	public Integer getQtdeAulaProgramada() {
		if(qtdeAulaProgramada == null){
			qtdeAulaProgramada = 0;
		}
		return qtdeAulaProgramada;
	}

	public void setQtdeAulaProgramada(Integer qtdeAulaProgramada) {
		this.qtdeAulaProgramada = qtdeAulaProgramada;
	}

	public Integer getQtdeAulaRegistrada() {
		if(qtdeAulaRegistrada == null){
			qtdeAulaRegistrada = 0;
		}
		return qtdeAulaRegistrada;
	}

	public void setQtdeAulaRegistrada(Integer qtdeAulaRegistrada) {
		this.qtdeAulaRegistrada = qtdeAulaRegistrada;
	}

	public Integer getCargaHoraria() {
		if(cargaHoraria == null){
			cargaHoraria = 0;
		}
		return cargaHoraria;
	}

	public void setCargaHoraria(Integer cargaHoraria) {
		this.cargaHoraria = cargaHoraria;
	}

	public String getHoraAulaRegistrada() {
		if(horaAulaRegistrada == null){
			horaAulaRegistrada = "";
		}
		return horaAulaRegistrada;
	}

	public void setHoraAulaRegistrada(String horaAulaRegistrada) {
		this.horaAulaRegistrada = horaAulaRegistrada;
	}

	public String getProfessor() {
		if(professor == null){
			professor = "";
		}
		return professor;
	}

	public void setProfessor(String professor) {
		this.professor = professor;
	}
	

}
