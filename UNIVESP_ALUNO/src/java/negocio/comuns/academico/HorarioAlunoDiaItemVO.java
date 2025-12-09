package negocio.comuns.academico;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;

@XmlRootElement(name = "horarioAluno")
public class HorarioAlunoDiaItemVO implements Serializable {

    private Integer nrAula;
    private String horario;
    private String horarioInicio;
    private String horarioTermino;
    private PessoaVO professor;
    private TurmaVO turma;
    private DisciplinaVO disciplina;
    private Date data;
//    private SalaLocalAulaVO sala; 
    public static final long serialVersionUID = 1L;

    @XmlElement(name = "disciplina")
    public DisciplinaVO getDisciplina() {
        if (disciplina == null) {
            disciplina = new DisciplinaVO();
        }
        return disciplina;
    }

    public void setDisciplina(DisciplinaVO disciplina) {
        this.disciplina = disciplina;
    }

    @XmlElement(name = "horario")
    public String getHorario() {
        if (horario == null) {
            horario = "";
        }
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    @XmlElement(name = "nrAula")
    public Integer getNrAula() {
        if (nrAula == null) {
            nrAula = 0;
        }
        return nrAula;
    }

    public void setNrAula(Integer nrAula) {
        this.nrAula = nrAula;
    }

    @XmlElement(name = "professor")
    public PessoaVO getProfessor() {
        if (professor == null) {
            professor = new PessoaVO();
        }
        return professor;
    }

    public void setProfessor(PessoaVO professor) {
        this.professor = professor;
    }

    @XmlElement(name = "turma")
    public TurmaVO getTurma() {
        if (turma == null) {
            turma = new TurmaVO();
        }
        return turma;
    }

    public void setTurma(TurmaVO turma) {
        this.turma = turma;
    }

    public boolean getDisciplinaLivre() {
        if (getDisciplina().getCodigo().intValue() == 0) {
            return true;
        }
        return false;
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

    @XmlElement(name = "data")
    public String getData_Apresentar() {
        return Uteis.getData(getData());
    }

    @XmlElement(name = "apresentarProfessor")
    public Boolean getApresentarProfessorCronogramaAula() {
        if (getData().before(new Date())) {
            return true;
        }
        return false;
    }

//	/**
//	 * @return the sala
//	 */
//    @XmlElement(name = "sala")
//	public SalaLocalAulaVO getSala() {
//		if (sala == null) {
//			sala = new SalaLocalAulaVO();
//		}
//		return sala;
//	}
//
//	/**
//	 * @param sala the sala to set
//	 */
//	public void setSala(SalaLocalAulaVO sala) {
//		this.sala = sala;
//	}

	private List<HorarioAlunoDiaItemVO> horarioAlunoDiaItemVOs;

	public List<HorarioAlunoDiaItemVO> getHorarioAlunoDiaItemVOs() {
		if (horarioAlunoDiaItemVOs == null) {
			horarioAlunoDiaItemVOs = new ArrayList<HorarioAlunoDiaItemVO>(0);
		}
		return horarioAlunoDiaItemVOs;
	}

	public void setHorarioAlunoDiaItemVOs(List<HorarioAlunoDiaItemVO> horarioAlunoDiaItemVOs) {
		this.horarioAlunoDiaItemVOs = horarioAlunoDiaItemVOs;
	}

	private Boolean disciplinaIncluida;

	public Boolean getDisciplinaIncluida() {
		if (disciplinaIncluida == null) {
			disciplinaIncluida = false;
		}
		return disciplinaIncluida;
	}

	public void setDisciplinaIncluida(Boolean disciplinaIncluida) {
		this.disciplinaIncluida = disciplinaIncluida;
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
	
	public boolean isHorarioAlunoDiaItemDentroDoLimiteFinalDeRealizacao() throws ParseException {
		if(!Uteis.isAtributoPreenchido(getData()) 
				|| !Uteis.isAtributoPreenchido(getHorarioTermino())
					|| !getHorarioTermino().contains(":")
					|| getHorarioTermino().length() != 5) {
			return false;
		}
		int hora =  Integer.parseInt(getHorarioTermino().substring(0, getHorarioTermino().indexOf(":")));
		int minuto =  Integer.parseInt(getHorarioTermino().substring(getHorarioTermino().indexOf(":")+1, getHorarioTermino().length()));
		Date dataTermino = UteisData.getDateTime(getData(), hora, minuto, 0); 
		return Uteis.isAtributoPreenchido(dataTermino) &&  UteisData.validarDataInicialMaiorFinalComHora(dataTermino , new Date());
	}

	
	
	
	
    
}
