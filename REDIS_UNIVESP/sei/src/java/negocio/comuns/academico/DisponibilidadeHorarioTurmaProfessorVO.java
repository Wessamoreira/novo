package negocio.comuns.academico;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.dominios.DiaSemana;

public class DisponibilidadeHorarioTurmaProfessorVO extends SuperVO {

    private String horario;
    private String horarioInicio;
    private String horarioTermino;
    private PessoaVO professor;
    private TurmaVO turmaVO;
    private DisciplinaVO disciplina;
    private Integer nrAula;
    private DiaSemana diaSemana;
    private Date data;
    private Boolean horarioLivre;
    private Boolean programarAula;
    private Boolean naoPossueChoqueHorario;
    private Boolean possuiChoqueSala;
    private Boolean possuiChoqueAulaExcesso;
    private Integer duracaoAula;
    public static final long serialVersionUID = 1L;

    /** Creates a new instance of HorarioProgramacao */
    public DisponibilidadeHorarioTurmaProfessorVO() {
        super();
    }

    public Boolean getProgramarAula() {
        if (programarAula == null) {
            programarAula = false;
        }
        return programarAula;
    }

    public void setProgramarAula(Boolean programarAula) {
        this.programarAula = programarAula;
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

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Integer getNrAula() {
        if (nrAula == null) {
            nrAula = 0;
        }
        return nrAula;
    }

    public void setNrAula(Integer nrAula) {
        this.nrAula = nrAula;
    }

    public DisciplinaVO getDisciplina() {
        if (disciplina == null) {
            disciplina = new DisciplinaVO();
        }
        return disciplina;
    }

    public void setDisciplina(DisciplinaVO disciplina) {
        this.disciplina = disciplina;
    }

    public String getHorario() {
        if (horario == null) {
            horario = "";
        }
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public DiaSemana getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(DiaSemana diaSemana) {
        this.diaSemana = diaSemana;
    }

    public PessoaVO getProfessor() {
        if (professor == null) {
            professor = new PessoaVO();
        }
        return professor;
    }

    public void setProfessor(PessoaVO professor) {
        this.professor = professor;
    }

    public void setHorarioLivre(Boolean horarioLivre) {
        this.horarioLivre = horarioLivre;
    }

    public boolean getHorarioLivre() {
        if (horarioLivre == null) {
            horarioLivre = true;
        }
        return horarioLivre;
    }

	public String getHorarioInicio() {
		if (horarioInicio == null) {
			horarioInicio = "";
		}
		return horarioInicio;
	}

	public void setHorarioInicio(String horarioInicio) {
		this.horarioInicio = horarioInicio;
	}

	public String getHorarioTermino() {
		if (horarioTermino == null) {
			horarioTermino = "";
		}
		return horarioTermino;
	}

	public void setHorarioTermino(String horarioTermino) {
		this.horarioTermino = horarioTermino;
	}

	public Boolean getNaoPossueChoqueHorario() {
		if (naoPossueChoqueHorario == null) {
			naoPossueChoqueHorario = true;
		}
		return naoPossueChoqueHorario;
	}

	public void setNaoPossueChoqueHorario(Boolean naoPossueChoqueHorario) {
		this.naoPossueChoqueHorario = naoPossueChoqueHorario;
	}

	/**
	 * @return the possuiChoqueSala
	 */
	public Boolean getPossuiChoqueSala() {
		if (possuiChoqueSala == null) {
			possuiChoqueSala = false;
		}
		return possuiChoqueSala;
	}

	/**
	 * @param possuiChoqueSala the possuiChoqueSala to set
	 */
	public void setPossuiChoqueSala(Boolean possuiChoqueSala) {
		this.possuiChoqueSala = possuiChoqueSala;
	}

	/**
	 * @return the possuiChoqueAulaExcesso
	 */
	public Boolean getPossuiChoqueAulaExcesso() {
		if (possuiChoqueAulaExcesso == null) {
			possuiChoqueAulaExcesso = false;
		}
		return possuiChoqueAulaExcesso;
	}

	/**
	 * @param possuiChoqueAulaExcesso the possuiChoqueAulaExcesso to set
	 */
	public void setPossuiChoqueAulaExcesso(Boolean possuiChoqueAulaExcesso) {
		this.possuiChoqueAulaExcesso = possuiChoqueAulaExcesso;
	}

	public Integer getDuracaoAula() {
		if(duracaoAula == null){
			duracaoAula = 0;
		}
		return duracaoAula;
	}

	public void setDuracaoAula(Integer duracaoAula) {
		this.duracaoAula = duracaoAula;
	}
    
    
}
