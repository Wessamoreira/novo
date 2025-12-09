package negocio.comuns.avaliacaoinst;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;

/**
 * Reponsável por manter os dados da entidade RespostaAvaliacaoInstitucionalDW. Classe do tipo VO - Value Object 
 * composta pelos atributos da entidade com visibilidade protegida e os métodos de acesso a estes atributos.
 * Classe utilizada para apresentar e manter em memória os dados desta entidade.
 * @see SuperVO
 */

@XmlRootElement(name = "respostaAvaliacaoInstitucionalDWVO")
public class RespostaAvaliacaoInstitucionalDWVO extends SuperVO {

    private Integer codigo;
    private Integer unidadeEnsino;
    private Integer curso;
    private Integer disciplina;
    private Integer avaliacaoInstitucional;
    private Integer questionario;
    private Integer pergunta;
    private String tipoPergunta;
    private String resposta;
    private String respostaAdicional;
    private Integer areaConhecimento;
    private Integer periodo;
    private String matriculaFuncionario;
    private String matriculaAluno;
    private Integer matriculaPeriodo;
    private Integer turno;
    private String tipoPessoa;
    private Integer pessoa;
    private String publicoAlvo;
    private String escopo;
    private Integer unidadeEnsinoCurso;
    private Integer turma;
    private Integer pesoPergunta;
    private Integer professor;
    private Integer inscricaoProcessoSeletivo;
    private Integer processoSeletivo;
    private Integer requerimento;
    /**
     * Atributos de controle de questionario de requerimento do tramite
     */
    private Integer departamentoTramite;
    private Integer ordemTramite;
    /**
     * Atributos de controle de avaliação institucional onde está avaliando um departamento
     */
    private Integer departamento;
    /**
     * Atributos de controle de avaliação institucional onde está avaliando um cargo
     */
    private Integer cargo;        
    private Integer coordenador;    
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>RespostaAvaliacaoInstitucionalDW</code>.
     * Cria uma nova instância desta entidade, inicializando automaticamente seus atributos (Classe VO).
     */
    public RespostaAvaliacaoInstitucionalDWVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe <code>RespostaAvaliacaoInstitucionalDWVO</code>.
     * Todos os tipos de consistência de dados são e devem ser implementadas neste método.
     * São validações típicas: verificação de campos obrigatórios, verificação de valores válidos para os atributos.
     * @exception ConsistirExecption Se uma inconsistência for encontrada aumaticamente é gerada uma exceção descrevendo
     *                               o atributo e o erro ocorrido.
     */
    public static void validarDados(RespostaAvaliacaoInstitucionalDWVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
    }

    /**
     * Operação reponsável por realizar o UpperCase dos atributos do tipo String.
     */
    public void realizarUpperCaseDados() {
        setTipoPergunta(getTipoPergunta().toUpperCase());
        setResposta(getResposta().toUpperCase());
        setTipoPessoa(getTipoPessoa().toUpperCase());
        setPublicoAlvo(getPublicoAlvo().toUpperCase());
        setEscopo(getEscopo().toUpperCase());
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(0);
        setUnidadeEnsino(0);
        setCurso(0);
        setDisciplina(0);
        setAvaliacaoInstitucional(0);
        setQuestionario(0);
        setPergunta(0);
        setTipoPergunta("");
        setResposta("");
        setAreaConhecimento(0);
        setPeriodo(0);
        setMatriculaFuncionario("");
        setMatriculaAluno("");
        setMatriculaPeriodo(0);
        setTurno(0);
        setTipoPessoa("");
        setPessoa(0);
        setPublicoAlvo("");
        setEscopo("");
        setUnidadeEnsinoCurso(0);
        setTurma(0);
        setPesoPergunta(0);
        setProfessor(0);

    }

    @XmlElement(name = "inscricaoProcessoSeletivo")
    public Integer getInscricaoProcessoSeletivo() {
        if (inscricaoProcessoSeletivo == null) {
            inscricaoProcessoSeletivo = 0;
        }
        return inscricaoProcessoSeletivo;
    }

    public void setInscricaoProcessoSeletivo(Integer inscricaoProcessoSeletivo) {
        this.inscricaoProcessoSeletivo = inscricaoProcessoSeletivo;
    }

    @XmlElement(name = "processoSeletivo")
    public Integer getProcessoSeletivo() {
        if (processoSeletivo == null) {
            processoSeletivo = 0;
        }
        return processoSeletivo;
    }

    public void setProcessoSeletivo(Integer processoSeletivo) {
        this.processoSeletivo = processoSeletivo;
    }

//    public static RespostaAvaliacaoInstitucionalDWVO popularRespostaAvaliacao(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO,
//            Integer pessoa, String tipoPessoa, Integer unidadeEnsinoVO, Integer disciplina, Integer turma,
//            AvaliacaoInstitucionalVO avaliacao, QuestionarioVO questionario, PerguntaVO pergunta,
//            String matriculaFuncionario, Integer inscricaoProcessoSeletivo, Integer processoSeletivo, Boolean validarImportanciaPergunta, Boolean obrigarResposta) throws ConsistirException {
//        RespostaAvaliacaoInstitucionalDWVO obj = new RespostaAvaliacaoInstitucionalDWVO();
//        obj.setUnidadeEnsino(unidadeEnsinoVO);
//        if (matriculaVO != null) {
//            obj.setCurso(matriculaVO.getCurso().getCodigo());
//            obj.setMatriculaAluno(matriculaVO.getMatricula());
//            obj.setTurno(matriculaVO.getTurno().getCodigo());
//            obj.setAreaConhecimento(matriculaVO.getCurso().getAreaConhecimento().getCodigo());
//        }
//        if (matriculaPeriodoVO != null) {
//            obj.setMatriculaPeriodo(matriculaPeriodoVO.getCodigo());
//            obj.setUnidadeEnsinoCurso(matriculaPeriodoVO.getUnidadeEnsinoCurso());
//        }
//        if (avaliacao != null) {
//            obj.setAvaliacaoInstitucional(avaliacao.getCodigo());
//            obj.setPublicoAlvo(avaliacao.getPublicoAlvo());
//        }
//        obj.setQuestionario(questionario.getCodigo());
//        obj.setResposta(pergunta.getResposta(validarImportanciaPergunta, obrigarResposta));
//        obj.setPergunta(pergunta.getCodigo());
//        obj.setTipoPergunta(pergunta.getTipoResposta());
//        obj.setDisciplina(disciplina);
//        obj.setPeriodo(Uteis.getAnoData(new Date()));
//        obj.setMatriculaFuncionario(matriculaFuncionario);
//        obj.setPessoa(pessoa);
//        obj.setTipoPessoa(tipoPessoa);
//
//        obj.setEscopo(questionario.getEscopo());
//
//        obj.setTurma(turma);
//        obj.setPesoPergunta(pergunta.getPeso());
//        obj.setProfessor(questionario.getProfessor());
//        obj.setInscricaoProcessoSeletivo(inscricaoProcessoSeletivo);
//        obj.setProcessoSeletivo(processoSeletivo);
//        return obj;
//    }

    @XmlElement(name = "professor")
    public Integer getProfessor() {
        if (professor == null) {
            professor = 0;
        }
        return (professor);
    }

    public void setProfessor(Integer professor) {
        this.professor = professor;
    }

    @XmlElement(name = "pesoPergunta")
    public Integer getPesoPergunta() {
        if (pesoPergunta == null) {
            pesoPergunta = 0;
        }
        return (pesoPergunta);
    }

    public void setPesoPergunta(Integer pesoPergunta) {
        this.pesoPergunta = pesoPergunta;
    }

    @XmlElement(name = "turma")
    public Integer getTurma() {
        if (turma == null) {
            turma = 0;
        }
        return (turma);
    }

    public void setTurma(Integer turma) {
        this.turma = turma;
    }

    @XmlElement(name = "unidadeEnsinoCurso")
    public Integer getUnidadeEnsinoCurso() {
        if (unidadeEnsinoCurso == null) {
            unidadeEnsinoCurso = 0;
        }
        return (unidadeEnsinoCurso);
    }

    public void setUnidadeEnsinoCurso(Integer unidadeEnsinoCurso) {
        this.unidadeEnsinoCurso = unidadeEnsinoCurso;
    }

    @XmlElement(name = "escopo")
    public String getEscopo() {
        if (escopo == null) {
            escopo = "";
        }
        return (escopo);
    }

    public void setEscopo(String escopo) {
        this.escopo = escopo;
    }

    @XmlElement(name = "publicoAlvo")
    public String getPublicoAlvo() {
        if (publicoAlvo == null) {
            publicoAlvo = "";
        }
        return (publicoAlvo);
    }

    public void setPublicoAlvo(String publicoAlvo) {
        this.publicoAlvo = publicoAlvo;
    }

    @XmlElement(name = "pessoa")
    public Integer getPessoa() {
        if (pessoa == null) {
            pessoa = 0;
        }
        return (pessoa);
    }

    public void setPessoa(Integer pessoa) {
        this.pessoa = pessoa;
    }

    @XmlElement(name = "tipoPessoa")
    public String getTipoPessoa() {
        if (tipoPessoa == null) {
            tipoPessoa = "";
        }
        return (tipoPessoa);
    }

    public void setTipoPessoa(String tipoPessoa) {
        this.tipoPessoa = tipoPessoa;
    }

    @XmlElement(name = "turno")
    public Integer getTurno() {
        if (turno == null) {
            turno = 0;
        }
        return (turno);
    }

    public void setTurno(Integer turno) {
        this.turno = turno;
    }

    @XmlElement(name = "matriculaPeriodo")
    public Integer getMatriculaPeriodo() {
        if (matriculaPeriodo == null) {
            matriculaPeriodo = 0;
        }
        return (matriculaPeriodo);
    }

    public void setMatriculaPeriodo(Integer matriculaPeriodo) {
        this.matriculaPeriodo = matriculaPeriodo;
    }

    @XmlElement(name = "matriculaAluno")
    public String getMatriculaAluno() {
        if (matriculaAluno == null) {
            matriculaAluno = "";
        }
        return (matriculaAluno);
    }

    public void setMatriculaAluno(String matriculaAluno) {
        this.matriculaAluno = matriculaAluno;
    }

    @XmlElement(name = "matriculaFuncionario")
    public String getMatriculaFuncionario() {
        if (matriculaFuncionario == null) {
            matriculaFuncionario = "";
        }
        return (matriculaFuncionario);
    }

    public void setMatriculaFuncionario(String matriculaFuncionario) {
        this.matriculaFuncionario = matriculaFuncionario;
    }

    @XmlElement(name = "periodo")
    public Integer getPeriodo() {
        if (periodo == null) {
            periodo = 0;
        }
        return (periodo);
    }

    public void setPeriodo(Integer periodo) {
        this.periodo = periodo;
    }

    @XmlElement(name = "areaConhecimento")
    public Integer getAreaConhecimento() {
        if (areaConhecimento == null) {
            areaConhecimento = 0;
        }
        return (areaConhecimento);
    }

    public void setAreaConhecimento(Integer areaConhecimento) {
        this.areaConhecimento = areaConhecimento;
    }

    @XmlElement(name = "resposta")
    public String getResposta() {
        if (resposta == null) {
            resposta = "";
        }
        return (resposta);
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
    }

    @XmlElement(name = "tipoPergunta")
    public String getTipoPergunta() {
        if (tipoPergunta == null) {
            tipoPergunta = "";
        }
        return (tipoPergunta);
    }

    public void setTipoPergunta(String tipoPergunta) {
        this.tipoPergunta = tipoPergunta;
    }

    @XmlElement(name = "pergunta")
    public Integer getPergunta() {
        if (pergunta == null) {
            pergunta = 0;
        }
        return (pergunta);
    }

    public void setPergunta(Integer pergunta) {
        this.pergunta = pergunta;
    }

    @XmlElement(name = "questionario")
    public Integer getQuestionario() {
        if (questionario == null) {
            questionario = 0;
        }
        return (questionario);
    }

    public void setQuestionario(Integer questionario) {
        this.questionario = questionario;
    }

    @XmlElement(name = "avaliacaoInstitucional")
    public Integer getAvaliacaoInstitucional() {
        if (avaliacaoInstitucional == null) {
            avaliacaoInstitucional = 0;
        }
        return (avaliacaoInstitucional);
    }

    public void setAvaliacaoInstitucional(Integer avaliacaoInstitucional) {
        this.avaliacaoInstitucional = avaliacaoInstitucional;
    }

    @XmlElement(name = "disciplina")
    public Integer getDisciplina() {
        if (disciplina == null) {
            disciplina = 0;
        }
        return (disciplina);
    }

    public void setDisciplina(Integer disciplina) {
        this.disciplina = disciplina;
    }

    @XmlElement(name = "curso")
    public Integer getCurso() {
        if (curso == null) {
            curso = 0;
        }
        return (curso);
    }

    public void setCurso(Integer curso) {
        this.curso = curso;
    }

    @XmlElement(name = "unidadeEnsino")
    public Integer getUnidadeEnsino() {
        if (unidadeEnsino == null) {
            unidadeEnsino = 0;
        }
        return (unidadeEnsino);
    }

    public void setUnidadeEnsino(Integer unidadeEnsino) {
        this.unidadeEnsino = unidadeEnsino;
    }

    @XmlElement(name = "codigo")
    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    @XmlElement(name = "respostaAdicional")
	public String getRespostaAdicional() {
		if(respostaAdicional == null){
			respostaAdicional = "";
		}
		return respostaAdicional;
	}

	public void setRespostaAdicional(String respostaAdicional) {
		this.respostaAdicional = respostaAdicional;
	}

	@XmlElement(name = "requerimento")
	public Integer getRequerimento() {
		if(requerimento == null){
			requerimento = 0;
		}
		return requerimento;
	}

	public void setRequerimento(Integer requerimento) {
		this.requerimento = requerimento;
	}

	@XmlElement(name = "departamentoTramite")
	public Integer getDepartamentoTramite() {
		if (departamentoTramite == null) {
			departamentoTramite = 0;
		}
		return departamentoTramite;
	}

	public void setDepartamentoTramite(Integer departamentoTramite) {
		this.departamentoTramite = departamentoTramite;
	}

	@XmlElement(name = "ordemTramite")
	public Integer getOrdemTramite() {
		if (ordemTramite == null) {
			ordemTramite = 0;
		}
		return ordemTramite;
	}

	public void setOrdemTramite(Integer ordemTramite) {
		this.ordemTramite = ordemTramite;
	}

	@XmlElement(name = "departamento")
	public Integer getDepartamento() {
		if (departamento == null) {
			departamento = 0;
		}
		return departamento;
	}

	public void setDepartamento(Integer departamento) {
		this.departamento = departamento;
	}

	@XmlElement(name = "cargo")
	public Integer getCargo() {
		if (cargo == null) {
			cargo = 0;
		}
		return cargo;
	}

	public void setCargo(Integer cargo) {
		this.cargo = cargo;
	}

	@XmlElement(name = "coordenador")
	public Integer getCoordenador() {
		if (coordenador == null) {
			coordenador = 0;
		}
		return coordenador;
	}

	public void setCoordenador(Integer coordenador) {
		this.coordenador = coordenador;
	}

	
    
}
