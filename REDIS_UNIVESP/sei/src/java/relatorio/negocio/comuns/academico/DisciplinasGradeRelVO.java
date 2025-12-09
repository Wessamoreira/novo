package relatorio.negocio.comuns.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

public class DisciplinasGradeRelVO {

	private String gradeCurricular;
	private String nomeCurso;
	private Integer curso;
	private Integer totalDiaLetivoAno;
	private Integer totalSemanaLetivaAno;
	private Integer duracaoIntervalo;
	private Integer duracaoModuloAula;
	private List<DisciplinasGradeDisciplinasRelVO> disciplinasGradeDisciplinasRelVOs;
	private String anoSemestreDataAtivacao;
	private String anoSemestreDataFinalVigencia;
	private Integer totalCargaHoraria;
	private Integer totalCredito;
	private Integer totalEstagio;
	private Integer totalAtividadeComplementar;
	private String autorizacao;
	private Date dataReconhecimento;
	private Date dataAutorizacao;
	private String reconhecimento;

	public String getGradeCurricular() {
		if (gradeCurricular == null) {
			gradeCurricular = "";
		}
		return gradeCurricular;
	}

	public void setGradeCurricular(String gradeCurricular) {
		this.gradeCurricular = gradeCurricular;
	}

	public String getNomeCurso() {
		if (nomeCurso == null) {
			nomeCurso = "";
		}
		return nomeCurso;
	}

	public void setNomeCurso(String nomeCurso) {
		this.nomeCurso = nomeCurso;
	}

	public Integer getCurso() {
		if (curso == null) {
			curso = 0;
		}
		return curso;
	}

	public void setCurso(Integer curso) {
		this.curso = curso;
	}

	public Integer getTotalDiaLetivoAno() {
		if (totalDiaLetivoAno == null) {
			totalDiaLetivoAno = 0;
		}
		return totalDiaLetivoAno;
	}

	public void setTotalDiaLetivoAno(Integer totalDiaLetivoAno) {
		this.totalDiaLetivoAno = totalDiaLetivoAno;
	}

	public Integer getTotalSemanaLetivaAno() {
		if (totalSemanaLetivaAno == null) {
			totalSemanaLetivaAno = 0;
		}
		return totalSemanaLetivaAno;
	}

	public void setTotalSemanaLetivaAno(Integer totalSemanaLetivaAno) {
		this.totalSemanaLetivaAno = totalSemanaLetivaAno;
	}

	public Integer getDuracaoIntervalo() {
		if (duracaoIntervalo == null) {
			duracaoIntervalo = 0;
		}
		return duracaoIntervalo;
	}

	public void setDuracaoIntervalo(Integer duracaoIntervalo) {
		this.duracaoIntervalo = duracaoIntervalo;
	}

	public Integer getDuracaoModuloAula() {
		if (duracaoModuloAula == null) {
			duracaoModuloAula = 0;
		}
		return duracaoModuloAula;
	}

	public void setDuracaoModuloAula(Integer duracaoModuloAula) {
		this.duracaoModuloAula = duracaoModuloAula;
	}

	public List<DisciplinasGradeDisciplinasRelVO> getDisciplinasGradeDisciplinasRelVOs() {
		if (disciplinasGradeDisciplinasRelVOs == null) {
			disciplinasGradeDisciplinasRelVOs = new ArrayList<DisciplinasGradeDisciplinasRelVO>(0);
		}
		return disciplinasGradeDisciplinasRelVOs;
	}

	public void setDisciplinasGradeDisciplinasRelVOs(List<DisciplinasGradeDisciplinasRelVO> disciplinasGradeDisciplinasRelVOs) {
		this.disciplinasGradeDisciplinasRelVOs = disciplinasGradeDisciplinasRelVOs;
	}

	public JRDataSource getDisciplinasGradeDisciplinasRelVO() {
		JRDataSource jr = new JRBeanArrayDataSource(getDisciplinasGradeDisciplinasRelVOs().toArray());
		return jr;
	}

	public String getAnoSemestreDataAtivacao() {
		if (anoSemestreDataAtivacao == null) {
			anoSemestreDataAtivacao = "";
		}
		return anoSemestreDataAtivacao;
	}

	public void setAnoSemestreDataAtivacao(String anoSemestreDataAtivacao) {
		this.anoSemestreDataAtivacao = anoSemestreDataAtivacao;
	}

	public String getAnoSemestreDataFinalVigencia() {
		if (anoSemestreDataFinalVigencia == null) {
			anoSemestreDataFinalVigencia = "";
		}
		return anoSemestreDataFinalVigencia;
	}

	public void setAnoSemestreDataFinalVigencia(String anoSemestreDataFinalVigencia) {
		this.anoSemestreDataFinalVigencia = anoSemestreDataFinalVigencia;
	}

	public Integer getTotalCargaHoraria() {
		if (totalCargaHoraria == null) {
			totalCargaHoraria = 0;
		}
		return totalCargaHoraria;
	}

	public void setTotalCargaHoraria(Integer totalCargaHoraria) {
		this.totalCargaHoraria = totalCargaHoraria;
	}

	public Integer getTotalCredito() {
		if (totalCredito == null) {
			totalCredito = 0;
		}
		return totalCredito;
	}

	public void setTotalCredito(Integer totalCredito) {
		this.totalCredito = totalCredito;
	}

	public Integer getTotalEstagio() {
		if (totalEstagio == null) {
			totalEstagio = 0;
		}
		return totalEstagio;
	}

	public void setTotalEstagio(Integer totalEstagio) {
		this.totalEstagio = totalEstagio;
	}

	public Integer getTotalAtividadeComplementar() {
		if (totalAtividadeComplementar == null) {
			totalAtividadeComplementar = 0;
		}
		return totalAtividadeComplementar;
	}

	public void setTotalAtividadeComplementar(Integer totalAtividadeComplementar) {
		this.totalAtividadeComplementar = totalAtividadeComplementar;
	}

	public String getAutorizacao() {
		if (autorizacao == null) {
			autorizacao = "";
		}
		return autorizacao;
	}

	public void setAutorizacao(String autorizacao) {
		this.autorizacao = autorizacao;
	}

	public Date getDataReconhecimento() {
//		if (dataReconhecimento == null) {
//			dataReconhecimento = new Date();
//		}
		return dataReconhecimento;
	}

	public void setDataReconhecimento(Date dataReconhecimento) {
		this.dataReconhecimento = dataReconhecimento;
	}

	public Date getDataAutorizacao() {
//		if (dataAutorizacao == null) {
//			dataAutorizacao = new Date();
//		}
		return dataAutorizacao;
	}

	public void setDataAutorizacao(Date dataAutorizacao) {
		this.dataAutorizacao = dataAutorizacao;
	}

	public String getReconhecimento() {
		if (reconhecimento == null) {
			reconhecimento = "";
		}
		return reconhecimento;
	}

	public void setReconhecimento(String reconhecimento) {
		this.reconhecimento = reconhecimento;
	}
}
