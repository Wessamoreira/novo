package negocio.comuns.academico;

import java.io.Serializable;
import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.Uteis;

public class TransferenciaEntradaRegistroAulaFrequenciaVO extends SuperVO implements Serializable {

	private Integer codigo;
	private TransferenciaEntradaVO transferenciaEntradaVO;
	private RegistroAulaVO registroAulaVO;
	private TurmaVO turmaVO;
	private DisciplinaVO disciplinaVO;
	private PessoaVO professorVO;
	private Date dataRegistroAula;
	private String ano;
	private String semestre;
	private String horario;
	private Boolean presente;
	private Boolean abonado;
	private String diaSemana;
	private String conteudo;
	private static final long serialVersionUID = 1L;

	public TransferenciaEntradaRegistroAulaFrequenciaVO() {
		
	}

	public Boolean getPresente() {
		if (presente == null) {
			presente = Boolean.FALSE;
		}
		return presente;
	}

	public void setPresente(Boolean presente) {
		this.presente = presente;
	}

	public Boolean getAbonado() {
		if (abonado == null) {
			abonado = Boolean.TRUE;
		}
		return abonado;
	}

	public void setAbonado(Boolean abonado) {
		this.abonado = abonado;
	}

	public TransferenciaEntradaVO getTransferenciaEntradaVO() {
		if (transferenciaEntradaVO == null) {
			transferenciaEntradaVO = new TransferenciaEntradaVO();
		}
		return transferenciaEntradaVO;
	}

	public void setTransferenciaEntradaVO(TransferenciaEntradaVO transferenciaEntradaVO) {
		this.transferenciaEntradaVO = transferenciaEntradaVO;
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

	public DisciplinaVO getDisciplinaVO() {
		if (disciplinaVO == null) {
			disciplinaVO = new DisciplinaVO();
		}
		return disciplinaVO;
	}

	public void setDisciplinaVO(DisciplinaVO disciplinaVO) {
		this.disciplinaVO = disciplinaVO;
	}

	public Date getDataRegistroAula() {
		if (dataRegistroAula == null) {
			dataRegistroAula = new Date();
		}
		return dataRegistroAula;
	}

	public void setDataRegistroAula(Date dataRegistroAula) {
		this.dataRegistroAula = dataRegistroAula;
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

	public String getHorario() {
		if (horario == null) {
			horario = "";
		}
		return horario;
	}

	public void setHorario(String horario) {
		this.horario = horario;
	}

	public PessoaVO getProfessorVO() {
		if (professorVO == null) {
			professorVO = new PessoaVO();
		}
		return professorVO;
	}

	public void setProfessorVO(PessoaVO professorVO) {
		this.professorVO = professorVO;
	}

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	
	public String getDataRegistroAula_Apresentar() {
		return Uteis.getDataAno4Digitos(getDataRegistroAula());
	}
	
	public String getAnoSemestre() {
		return getAno() + " / " + getSemestre();
	}
	
	public String montarDiaSemanaAula() {
        int x = Uteis.getDiaSemana(this.getDataRegistroAula());
        this.setDiaSemana("0" + x);
        return getDiaSemana();
	}
	
    public String getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
    }

    public String getDiaSemana_Apresentar() {
        if (diaSemana.equals("01")) {
            return "Domingo";
        }
        if (diaSemana.equals("02")) {
            return "Segunda";
        }
        if (diaSemana.equals("03")) {
            return "Terça";
        }
        if (diaSemana.equals("04")) {
            return "Quarta";
        }
        if (diaSemana.equals("05")) {
            return "Quinta";
        }
        if (diaSemana.equals("06")) {
            return "Sexta";
        }
        if (diaSemana.equals("07")) {
            return "Sábado";
        }
        return (diaSemana);
    }

	public String getConteudo() {
		if (conteudo == null) {
			conteudo = "";
		}
		return conteudo;
	}

	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}

	public RegistroAulaVO getRegistroAulaVO() {
		if (registroAulaVO == null) {
			registroAulaVO = new RegistroAulaVO();
		}
		return registroAulaVO;
	}

	public void setRegistroAulaVO(RegistroAulaVO registroAulaVO) {
		this.registroAulaVO = registroAulaVO;
	}

}
