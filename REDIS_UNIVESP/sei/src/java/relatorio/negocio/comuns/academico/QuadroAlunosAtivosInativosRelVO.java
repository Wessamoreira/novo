package relatorio.negocio.comuns.academico;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class QuadroAlunosAtivosInativosRelVO {

	private List<AlunosAtivosInativosVO> alunosAtivosInativosVOs;

	private String semestre;
	private String ano;
	private int nrTotalAlunos;
	private int nrAlunosAtivos;
	private int nrAlunosInativos;
	private String unidadeEnsino;
	private String curso;
	private String turma;

	public QuadroAlunosAtivosInativosRelVO() {

	}

	public JRDataSource getListaAlunosAtivosInativosVOs() {
		JRBeanCollectionDataSource jr = new JRBeanCollectionDataSource(getAlunosAtivosInativosVOs());
		return jr;
	}

	public List<AlunosAtivosInativosVO> getAlunosAtivosInativosVOs() {
		if (alunosAtivosInativosVOs == null) {
			alunosAtivosInativosVOs = new ArrayList<AlunosAtivosInativosVO>(0);
		}
		return alunosAtivosInativosVOs;
	}

	public void setAlunosAtivosInativosVOs(List<AlunosAtivosInativosVO> alunosAtivosInativosVOs) {
		this.alunosAtivosInativosVOs = alunosAtivosInativosVOs;
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

	public String getAno() {
		if (ano == null) {
			ano = "";
		}
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public int getNrTotalAlunos() {
		return nrTotalAlunos;
	}

	public void setNrTotalAlunos(int nrTotalAlunos) {
		this.nrTotalAlunos = nrTotalAlunos;
	}

	public int getNrAlunosAtivos() {
		return nrAlunosAtivos;
	}

	public void setNrAlunosAtivos(int nrAlunosAtivos) {
		this.nrAlunosAtivos = nrAlunosAtivos;
	}

	public int getNrAlunosInativos() {
		return nrAlunosInativos;
	}

	public void setNrAlunosInativos(int nrAlunosInativos) {
		this.nrAlunosInativos = nrAlunosInativos;
	}

	public void setUnidadeEnsino(String unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}

	public String getUnidadeEnsino() {
		if (unidadeEnsino == null) {
			unidadeEnsino = "";
		}
		return unidadeEnsino;
	}

	public void setCurso(String curso) {
		this.curso = curso;
	}

	public String getCurso() {
		if (curso == null) {
			curso = "";
		}
		return curso;
	}

	public String getTurma() {
		if (turma == null) {
			turma = "";
		}
		return turma;
	}

	public void setTurma(String turma) {
		this.turma = turma;
	}

}