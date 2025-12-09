/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.negocio.comuns.academico;

import java.util.ArrayList;
import java.util.List;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

/**
 *
 * @author Otimize-Not
 */
public class AlunoNaoCursouDisciplinaFiltroRelVO {

    private DisciplinaVO disciplinaVO;
    private TurmaVO turmaVO;
    private CursoVO cursoVO;
    private UnidadeEnsinoVO unidadeEnsinoVO;
    private Boolean matriculaAtiva;
    private Boolean matriculaConcluida;
    private String anoBaseInicio;
    private String anoBaseFim;
    private List<AlunoNaoCursouDisciplinaRelVO> alunoNaoCursouDisciplinaRelVOs;
    private List<String> ordernarPor;
    private List<UnidadeEnsinoVO> unidadeEnsinoVOs;
    private List<CursoVO> cursoVOs;
    private String ano;
    private String semestre;
    private Boolean trazerAlunosReprovaramNaoCursamDisciplina;
    private Boolean considerarDisciplinaOptativaForaGrupoOptativas;
    private Boolean considerarDisciplinaGrupoOptativas;
    private Boolean agruparApenasPorDisciplinaCargaHoraria;
    private Boolean filtrarCursoIntegral;
    private Boolean filtrarCursoAnual;
    private Boolean filtrarCursoSemestral;
    private Boolean apresentarPeriodoLetivoDisciplina;

    public List<String> getOrdernarPor() {
        if (ordernarPor == null) {
            ordernarPor = new ArrayList<String>();
        }
        return ordernarPor;
    }

    public void setOrdernarPor(List<String> ordernarPor) {
        this.ordernarPor = ordernarPor;
    }

    public JRDataSource getAlunoNaoCursouDisciplinaRelJR() {
        return new JRBeanArrayDataSource(getAlunoNaoCursouDisciplinaRelVOs().toArray());
    }

    public List<AlunoNaoCursouDisciplinaRelVO> getAlunoNaoCursouDisciplinaRelVOs() {
        if (alunoNaoCursouDisciplinaRelVOs == null) {
            alunoNaoCursouDisciplinaRelVOs = new ArrayList<AlunoNaoCursouDisciplinaRelVO>(0);
        }
        return alunoNaoCursouDisciplinaRelVOs;
    }

    public void setAlunoNaoCursouDisciplinaRelVOs(List<AlunoNaoCursouDisciplinaRelVO> alunoNaoCursouDisciplinaRelVOs) {
        this.alunoNaoCursouDisciplinaRelVOs = alunoNaoCursouDisciplinaRelVOs;
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

    public DisciplinaVO getDisciplinaVO() {
        if (disciplinaVO == null) {
            disciplinaVO = new DisciplinaVO();
        }
        return disciplinaVO;
    }

    public void setDisciplinaVO(DisciplinaVO disciplinaVO) {
        this.disciplinaVO = disciplinaVO;
    }

    public Boolean getMatriculaAtiva() {
        if (matriculaAtiva == null) {
            matriculaAtiva = Boolean.TRUE;
        }
        return matriculaAtiva;
    }

    public void setMatriculaAtiva(Boolean matriculaAtiva) {

        this.matriculaAtiva = matriculaAtiva;
    }

    public Boolean getMatriculaConcluida() {
        if (matriculaConcluida == null) {
            matriculaConcluida = Boolean.FALSE;
        }
        return matriculaConcluida;
    }

    public void setMatriculaConcluida(Boolean matriculaConcluida) {
        this.matriculaConcluida = matriculaConcluida;
    }

    public TurmaVO getTurmaVO() {
        if (turmaVO == null) {
            turmaVO = new TurmaVO();
        }
        return turmaVO;
    }

    public void setTurmaVO(TurmaVO turmaVO) {
        this.turmaVO = turmaVO;
    }

    public UnidadeEnsinoVO getUnidadeEnsinoVO() {
        if (unidadeEnsinoVO == null) {
            unidadeEnsinoVO = new UnidadeEnsinoVO();
        }
        return unidadeEnsinoVO;
    }

    public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
        this.unidadeEnsinoVO = unidadeEnsinoVO;
    }

    public String getAnoBaseInicio() {
        if (anoBaseInicio == null) {
            anoBaseInicio = "";
        }
        return anoBaseInicio;
    }

    public void setAnoBaseInicio(String anoBaseInicio) {
        this.anoBaseInicio = anoBaseInicio;
    }

    public String getAnoBaseFim() {
        if (anoBaseFim == null){
            anoBaseFim = "";
        }
        return anoBaseFim;
    }

    public void setAnoBaseFim(String anoBaseFim) {
        this.anoBaseFim = anoBaseFim;
    }

	public List<UnidadeEnsinoVO> getUnidadeEnsinoVOs() {
		if (unidadeEnsinoVOs == null) {
			unidadeEnsinoVOs = new ArrayList<>();
		}
		return unidadeEnsinoVOs;
	}

	public void setUnidadeEnsinoVOs(List<UnidadeEnsinoVO> unidadeEnsinoVOs) {
		this.unidadeEnsinoVOs = unidadeEnsinoVOs;
	}

	public List<CursoVO> getCursoVOs() {
		if (cursoVOs == null) {
			cursoVOs = new ArrayList<>();
		}
		return cursoVOs;
	}

	public void setCursoVOs(List<CursoVO> cursoVOs) {
		this.cursoVOs = cursoVOs;
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
	
	public Boolean getTrazerAlunosReprovaramNaoCursamDisciplina() {
		if (trazerAlunosReprovaramNaoCursamDisciplina == null) {
			trazerAlunosReprovaramNaoCursamDisciplina = false;
		}
		return trazerAlunosReprovaramNaoCursamDisciplina;
	}

	public void setTrazerAlunosReprovaramNaoCursamDisciplina(Boolean trazerAlunosReprovaramNaoCursamDisciplina) {
		this.trazerAlunosReprovaramNaoCursamDisciplina = trazerAlunosReprovaramNaoCursamDisciplina;
	}

	public Boolean getConsiderarDisciplinaOptativaForaGrupoOptativas() {
		if (considerarDisciplinaOptativaForaGrupoOptativas == null) {
			considerarDisciplinaOptativaForaGrupoOptativas = false;
		}
		return considerarDisciplinaOptativaForaGrupoOptativas;
	}

	public void setConsiderarDisciplinaOptativaForaGrupoOptativas(Boolean considerarDisciplinaOptativaForaGrupoOptativas) {
		this.considerarDisciplinaOptativaForaGrupoOptativas = considerarDisciplinaOptativaForaGrupoOptativas;
	}

	public Boolean getConsiderarDisciplinaGrupoOptativas() {
		if (considerarDisciplinaGrupoOptativas == null) {
			considerarDisciplinaGrupoOptativas = false;
		}
		return considerarDisciplinaGrupoOptativas;
	}

	public void setConsiderarDisciplinaGrupoOptativas(Boolean considerarDisciplinaGrupoOptativas) {
		this.considerarDisciplinaGrupoOptativas = considerarDisciplinaGrupoOptativas;
	}

	public Boolean getAgruparApenasPorDisciplinaCargaHoraria() {
		if (agruparApenasPorDisciplinaCargaHoraria == null) {
			agruparApenasPorDisciplinaCargaHoraria = false;
		}
		return agruparApenasPorDisciplinaCargaHoraria;
	}

	public void setAgruparApenasPorDisciplinaCargaHoraria(Boolean agruparApenasPorDisciplinaCargaHoraria) {
		this.agruparApenasPorDisciplinaCargaHoraria = agruparApenasPorDisciplinaCargaHoraria;
	}

	public Boolean getFiltrarCursoIntegral() {
		if (filtrarCursoIntegral == null) {
			filtrarCursoIntegral = false;
		}
		return filtrarCursoIntegral;
	}

	public void setFiltrarCursoIntegral(Boolean filtrarCursoIntegral) {
		this.filtrarCursoIntegral = filtrarCursoIntegral;
	}

	public Boolean getFiltrarCursoAnual() {
		if (filtrarCursoAnual == null) {
			filtrarCursoAnual = false;
		}
		return filtrarCursoAnual;
	}

	public void setFiltrarCursoAnual(Boolean filtrarCursoAnual) {
		this.filtrarCursoAnual = filtrarCursoAnual;
	}

	public Boolean getFiltrarCursoSemestral() {
		if (filtrarCursoSemestral == null) {
			filtrarCursoSemestral = false;
		}
		return filtrarCursoSemestral;
	}

	public void setFiltrarCursoSemestral(Boolean filtrarCursoSemestral) {
		this.filtrarCursoSemestral = filtrarCursoSemestral;
	}

	public Boolean getApresentarPeriodoLetivoDisciplina() {
		if (apresentarPeriodoLetivoDisciplina == null) {
			apresentarPeriodoLetivoDisciplina = false;
		}
		return apresentarPeriodoLetivoDisciplina;
	}

	public void setApresentarPeriodoLetivoDisciplina(Boolean apresentarPeriodoLetivoDisciplina) {
		this.apresentarPeriodoLetivoDisciplina = apresentarPeriodoLetivoDisciplina;
	}
}
