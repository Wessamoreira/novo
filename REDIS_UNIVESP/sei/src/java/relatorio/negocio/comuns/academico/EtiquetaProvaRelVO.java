package relatorio.negocio.comuns.academico;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.academico.MatriculaPeriodoVO;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

/**
 *
 * @author Carlos
 */
public class EtiquetaProvaRelVO {

    private String matricula;
    private String nome;
    private String turma;
    private String periodo;
    private String disciplina;
    private MatriculaPeriodoVO matriculaPeriodoVO;
    private List<EtiquetaProvaRelVO> listaEtiquetaProvaColuna1;
    private List<EtiquetaProvaRelVO> listaEtiquetaProvaColuna2;
    private List<EtiquetaProvaRelVO> listaEtiquetaProvaColuna3;
    private String turno;
    private String tipoRelatorio;
    private String professor;

    public EtiquetaProvaRelVO() {
    }

    public JRDataSource getEtiquetaProvaColuna1VOs() {
        return new JRBeanArrayDataSource(getListaEtiquetaProvaColuna1().toArray());
    }

    public JRDataSource getEtiquetaProvaColuna2VOs() {
        return new JRBeanArrayDataSource(getListaEtiquetaProvaColuna2().toArray());
    }

    public JRDataSource getEtiquetaProvaColuna3VOs() {
        return new JRBeanArrayDataSource(getListaEtiquetaProvaColuna3().toArray());
    }

    public String getDisciplina() {
        if (disciplina == null) {
            disciplina = "";
        }
        return disciplina;
    }

    public void setDisciplina(String disciplina) {
        this.disciplina = disciplina;
    }

    public String getMatricula() {
        if (matricula == null) {
            matricula = "";
        }
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getNome() {
        if (nome == null) {
            nome = "";
        }
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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

    public String getPeriodo() {
        if (periodo == null) {
            periodo = "";
        }
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public String getTurmaPeriodo() {
        return getTurma() + " / " + getPeriodo();
    }

    public List<EtiquetaProvaRelVO> getListaEtiquetaProvaColuna1() {
        if (listaEtiquetaProvaColuna1 == null) {
            listaEtiquetaProvaColuna1 = new ArrayList(0);
        }
        return listaEtiquetaProvaColuna1;
    }

    public void setListaEtiquetaProvaColuna1(List<EtiquetaProvaRelVO> listaEtiquetaProvaColuna1) {
        this.listaEtiquetaProvaColuna1 = listaEtiquetaProvaColuna1;
    }

    public List<EtiquetaProvaRelVO> getListaEtiquetaProvaColuna2() {
        if (listaEtiquetaProvaColuna2 == null) {
            listaEtiquetaProvaColuna2 = new ArrayList(0);
        }
        return listaEtiquetaProvaColuna2;
    }

    public void setListaEtiquetaProvaColuna2(List<EtiquetaProvaRelVO> listaEtiquetaProvaColuna2) {
        this.listaEtiquetaProvaColuna2 = listaEtiquetaProvaColuna2;
    }

    public List<EtiquetaProvaRelVO> getListaEtiquetaProvaColuna3() {
        if (listaEtiquetaProvaColuna3 == null) {
            listaEtiquetaProvaColuna3 = new ArrayList(0);
        }
        return listaEtiquetaProvaColuna3;
    }

    public void setListaEtiquetaProvaColuna3(List<EtiquetaProvaRelVO> listaEtiquetaProvaColuna3) {
        this.listaEtiquetaProvaColuna3 = listaEtiquetaProvaColuna3;
    }
    public String getTurno() {
        if(turno == null){
            turno = "";
        }

        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public String getTipoRelatorio() {
        if (tipoRelatorio == null) {
            tipoRelatorio = "";
        }
        return tipoRelatorio;
    }

    public void setTipoRelatorio(String tipoRelatorio) {
        this.tipoRelatorio = tipoRelatorio;
    }

	public String getProfessor() {
		if (professor == null) {
			professor = "";
		}
		return professor;
	}

	public void setProfessor(String professor) {
		this.professor = professor;
	}

	public MatriculaPeriodoVO getMatriculaPeriodoVO() {
		if(matriculaPeriodoVO == null){
			matriculaPeriodoVO = new MatriculaPeriodoVO();
		}
		return matriculaPeriodoVO;
	}

	public void setMatriculaPeriodoVO(MatriculaPeriodoVO matriculaPeriodoVO) {
		this.matriculaPeriodoVO = matriculaPeriodoVO;
	}
	
	
}
