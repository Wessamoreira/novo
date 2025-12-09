package relatorio.negocio.comuns.academico;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import relatorio.negocio.comuns.arquitetura.CrosstabVO;

public class FrequenciaAlunoRelVO extends SuperVO {

	private static final long serialVersionUID = 1L;
	private String nomeDisciplina;
	private String periodoLetivo;
	private Integer cargaHoraria;
	private Integer numeroAulas;
	private List<FrequenciaAlunoMesDiaRelVO> frequenciaAlunoMesDiaRelVOs;
	private List<CrosstabVO> crosstabVOs;
	private String matricula;
	private String nomeAluno;
	private String ano;
	private String semestre;

	
	public String getNomeDisciplina() {
		if (nomeDisciplina == null) {
			nomeDisciplina = "";
		}
		return nomeDisciplina;
	}

	public void setNomeDisciplina(String nomeDisciplina) {
		this.nomeDisciplina = nomeDisciplina;
	}

	public String getPeriodoLetivo() {
		if (periodoLetivo == null) {
			periodoLetivo = "";
		}
		return periodoLetivo;
	}

	public void setPeriodoLetivo(String periodoLetivo) {
		this.periodoLetivo = periodoLetivo;
	}

	public Integer getCargaHoraria() {
		if (cargaHoraria == null) {
			cargaHoraria = 0;
		}
		return cargaHoraria;
	}

	public void setCargaHoraria(Integer cargaHoraria) {
		this.cargaHoraria = cargaHoraria;
	}

	public Integer getNumeroAulas() {
		if (numeroAulas == null) {
			numeroAulas = 0;
		}
		return numeroAulas;
	}

	public void setNumeroAulas(Integer numeroAulas) {
		this.numeroAulas = numeroAulas;
	}

	public List<FrequenciaAlunoMesDiaRelVO> getFrequenciaAlunoMesDiaRelVOs() {
		if (frequenciaAlunoMesDiaRelVOs == null) {
			frequenciaAlunoMesDiaRelVOs = new ArrayList<FrequenciaAlunoMesDiaRelVO>(0);
		}
		return frequenciaAlunoMesDiaRelVOs;
	}

	public void setFrequenciaAlunoMesDiaRelVOs(List<FrequenciaAlunoMesDiaRelVO> frequenciaAlunoMesDiaRelVOs) {
		this.frequenciaAlunoMesDiaRelVOs = frequenciaAlunoMesDiaRelVOs;
	}

	public List<CrosstabVO> getCrosstabVOs() {
		if (crosstabVOs == null) {
			crosstabVOs = new ArrayList<CrosstabVO>(0);
		}
		return crosstabVOs;
	}

	public void setCrosstabVOs(List<CrosstabVO> crosstabVOs) {
		this.crosstabVOs = crosstabVOs;
	}
	
	public String getMatricula() {
		if(matricula == null) {
			matricula = "";
		}
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public String getNomeAluno() {
		if(nomeAluno == null) {
			nomeAluno = "";
		}
		return nomeAluno;
	}

	public void setNomeAluno(String nomeAluno) {
		this.nomeAluno = nomeAluno;
	}

	public String getAno() {
		if(ano == null) {
			ano = "";
		}
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getSemestre() {
		if(semestre == null) {
			semestre = "";
		}
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

}
