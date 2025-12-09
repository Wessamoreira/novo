package negocio.comuns.academico;

import java.util.Date;

import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;

/**
 * Reponsável por manter os dados da entidade RegistroAula. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class ProfessorTitularDisciplinaTurmaVO extends SuperVO {

    private Integer codigo;
    private String semestre;
    private String ano;
    private TurmaVO turma;
    private DisciplinaVO disciplina;
    private FuncionarioVO professor;
    private Boolean titular;
    private DisciplinaVO disciplinaEquivalenteTurmaAgrupadaVO;
    private Integer ordemApresentacao;
    private Boolean ignorarTitularidadeProfessor;
    private CursoVO cursoVO;
    private UsuarioVO responsavelVO;
    private Date dataCadastro;
    private Date dataUltimaAlteracao;
    public static final long serialVersionUID = 1L;
    //Transient
    private String tipoDefinicaoProfessor;
    
    // transient
    private Boolean possuiProgramacaoAula;

    public ProfessorTitularDisciplinaTurmaVO() {
        super();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>RegistroAulaVO</code>. Todos os tipos de consistência de dados são
     * e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(ProfessorTitularDisciplinaTurmaVO obj) throws ConsistirException {
    	if (obj.getTipoDefinicaoProfessor().equals("TURMA")) {
    		if ((obj.getTurma() == null) || (obj.getTurma().getCodigo().intValue() == 0)) {
                throw new ConsistirException("O campo TURMA (Definir Professor Titular) deve ser informado.");
            }
    	} else {
    		if ((obj.getCursoVO() == null) || (obj.getCursoVO().getCodigo().intValue() == 0)) {
                throw new ConsistirException("O campo CURSO (Definir Professor Titular) deve ser informado.");
            }
    	}
        
        if ((obj.getDisciplina() == null) || (obj.getDisciplina().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo DISCIPLINA (Definir Professor Titular) deve ser informado.");
        }
        if ((obj.getProfessor().getPessoa() == null) || (obj.getProfessor().getPessoa().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo PROFESSOR (Definir Professor Titular) deve ser informado.");
        }
        if (obj.getTipoDefinicaoProfessor().equals("TURMA")) {
        	if (obj.getTurma().getSemestral()) {
                if (obj.getSemestre().equals("")) {
                    throw new ConsistirException("O campo SEMESTRE (Definir Professor Titular) deve ser informado");
                }
                if (obj.getAno().equals("")) {
                    throw new ConsistirException("O campo ANO (Definir Professor Titular) deve ser informado");
                }
            } else if (obj.getTurma().getAnual()) {
                if (obj.getAno().equals("")) {
                    throw new ConsistirException("O campo ANO (Definir Professor Titular) deve ser informado");
                }
                obj.setSemestre("");
            } else if (obj.getTurma().getIntegral()) {
                obj.setAno("");
                obj.setSemestre("");
            }
        } else {
        	if (obj.getCursoVO().getPeriodicidade().equals("SE")) {
        		if (obj.getSemestre().equals("")) {
                    throw new ConsistirException("O campo SEMESTRE (Definir Professor Titular) deve ser informado");
                }
                if (obj.getAno().equals("")) {
                    throw new ConsistirException("O campo ANO (Definir Professor Titular) deve ser informado");
                }
        	} else if (obj.getCursoVO().getPeriodicidade().equals("AN")) {
        		if (obj.getAno().equals("")) {
                    throw new ConsistirException("O campo ANO (Definir Professor Titular) deve ser informado");
                }
        		obj.setSemestre("");
        	} else {
        		obj.setAno("");
                obj.setSemestre("");
        	}
        }
        
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

    public TurmaVO getTurma() {
        if (turma == null) {
            turma = new TurmaVO();
        }
        return turma;
    }

    public void setTurma(TurmaVO turma) {
        this.turma = turma;
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

    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public FuncionarioVO getProfessor() {
        if (professor == null) {
            professor = new FuncionarioVO();
        }
        return professor;
    }

    public void setProfessor(FuncionarioVO professor) {
        this.professor = professor;
    }

    public Boolean getTitular() {
        if (titular == null) {
            titular = false;
        }
        return titular;
    }

    public void setTitular(Boolean titular) {
        this.titular = titular;
    }

	public DisciplinaVO getDisciplinaEquivalenteTurmaAgrupadaVO() {
		if (disciplinaEquivalenteTurmaAgrupadaVO == null) {
			disciplinaEquivalenteTurmaAgrupadaVO = new DisciplinaVO();
		}
		return disciplinaEquivalenteTurmaAgrupadaVO;
	}

	public void setDisciplinaEquivalenteTurmaAgrupadaVO(DisciplinaVO disciplinaEquivalenteTurmaAgrupadaVO) {
		this.disciplinaEquivalenteTurmaAgrupadaVO = disciplinaEquivalenteTurmaAgrupadaVO;
	}

	public Integer getOrdemApresentacao() {
		if (ordemApresentacao == null) {
			ordemApresentacao = 0;
		}
		return ordemApresentacao;
	}

	public void setOrdemApresentacao(Integer ordemApresentacao) {
		this.ordemApresentacao = ordemApresentacao;
	}

	public Boolean getIgnorarTitularidadeProfessor() {
		if (ignorarTitularidadeProfessor == null) {
			ignorarTitularidadeProfessor = false;
		}
		return ignorarTitularidadeProfessor;
	}

	public void setIgnorarTitularidadeProfessor(Boolean ignorarTitularidadeProfessor) {
		this.ignorarTitularidadeProfessor = ignorarTitularidadeProfessor;
	}

	public Boolean getPossuiProgramacaoAula() {
		if (possuiProgramacaoAula == null) {
			possuiProgramacaoAula = Boolean.FALSE;
		}
		return possuiProgramacaoAula;
	}

	public void setPossuiProgramacaoAula(Boolean possuiProgramacaoAula) {
		this.possuiProgramacaoAula = possuiProgramacaoAula;
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

	public UsuarioVO getResponsavelVO() {
		if (responsavelVO == null) {
			responsavelVO = new UsuarioVO();
		}
		return responsavelVO;
	}

	public void setResponsavelVO(UsuarioVO responsavelVO) {
		this.responsavelVO = responsavelVO;
	}

	public String getTipoDefinicaoProfessor() {
		if (tipoDefinicaoProfessor == null) {
			tipoDefinicaoProfessor = "TURMA";
		}
		return tipoDefinicaoProfessor;
	}

	public void setTipoDefinicaoProfessor(String tipoDefinicaoProfessor) {
		this.tipoDefinicaoProfessor = tipoDefinicaoProfessor;
	}

	public Date getDataCadastro() {
		if (dataCadastro == null) {
			dataCadastro = new Date();
		}
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public Date getDataUltimaAlteracao() {
		if (dataUltimaAlteracao == null) {
			dataUltimaAlteracao = new Date();
		}
		return dataUltimaAlteracao;
	}

	public void setDataUltimaAlteracao(Date dataUltimaAlteracao) {
		this.dataUltimaAlteracao = dataUltimaAlteracao;
	}
}
