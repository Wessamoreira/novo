package negocio.comuns.academico;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.Uteis;

public class MapaLocalAulaTurmaVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4319569355808818040L;

	private TurmaVO turma;
	private TurmaDisciplinaVO turmaDisciplina;
	private PessoaVO professor;
	private Integer qtdeAluno;
	private Integer qtdeAlunoExtRep;
	private Integer qtdeAlunoPre;
	private Integer nrModulo;
	private Date dataAula;
	private String datasAulaStr;
	private Double avaliacao;
		

	public TurmaVO getTurma() {
		if (turma == null) {
			turma = new TurmaVO();
		}
		return turma;
	}

	public void setTurma(TurmaVO turma) {
		this.turma = turma;
	}

	public TurmaDisciplinaVO getTurmaDisciplina() {
		if (turmaDisciplina == null) {
			turmaDisciplina = new TurmaDisciplinaVO();
		}
		return turmaDisciplina;
	}

	public void setTurmaDisciplina(TurmaDisciplinaVO turmaDisciplina) {
		this.turmaDisciplina = turmaDisciplina;
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

	public Integer getQtdeAluno() {
		if (qtdeAluno == null) {
			qtdeAluno = 0;
		}
		return qtdeAluno;
	}

	public void setQtdeAluno(Integer qtdeAluno) {
		this.qtdeAluno = qtdeAluno;
	}

	public Date getDataAula() {
		if (dataAula == null) {
			dataAula = new Date();
		}
		return dataAula;
	}

	public boolean getDataAulaPassada() {
		if (dataAula == null) {
			dataAula = new Date();
		}

		return Uteis.isDataAnteriorSemConsiderarHoraMinutoSegundo(dataAula, new Date());
	
	}
	
	public void setDataAula(Date dataAula) {
		this.dataAula = dataAula;
	}

    /**
     * @return the qtdeAlunoExtRep
     */
    public Integer getQtdeAlunoExtRep() {
        if (qtdeAlunoExtRep == null) {
            qtdeAlunoExtRep = 0;
        }
        return qtdeAlunoExtRep;
    }

    /**
     * @param qtdeAlunoExtRep the qtdeAlunoExtRep to set
     */
    public void setQtdeAlunoExtRep(Integer qtdeAlunoExtRep) {
        this.qtdeAlunoExtRep = qtdeAlunoExtRep;
    }

    /**
     * @return the nrModulo
     */
    public Integer getNrModulo() {
        if (nrModulo == null) {
            nrModulo = 0;
        }
        return nrModulo;
    }

    /**
     * @param nrModulo the nrModulo to set
     */
    public void setNrModulo(Integer nrModulo) {
        this.nrModulo = nrModulo;
    }

    /**
     * @return the datasAulaStr
     */
    public String getDatasAulaStr() {
        if (datasAulaStr == null) {
            datasAulaStr ="";
        }
        return datasAulaStr;
    }

    /**
     * @param datasAulaStr the datasAulaStr to set
     */
    public void setDatasAulaStr(String datasAulaStr) {
        this.datasAulaStr = datasAulaStr;
    }

	public Double getAvaliacao() {
		if (avaliacao == null) {
			avaliacao = new Double(0);
		}
		return avaliacao;
	}

	public void setAvaliacao(Double avaliacao) {
		this.avaliacao = avaliacao;
	}

	public Integer getQtdeAlunoPre() {
		if (qtdeAlunoPre == null) {
			qtdeAlunoPre = 0;
		}
		return qtdeAlunoPre;
	}

	public void setQtdeAlunoPre(Integer qtdeAlunoPre) {
		this.qtdeAlunoPre = qtdeAlunoPre;
	}

}
