package negocio.comuns.academico;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.facade.jdbc.academico.TransferenciaEntrada;

/**
 * Reponsável por manter os dados da entidade
 * TransferenciaEntradaDisciplinasAproveitadas. Classe do tipo VO - Value Object
 * composta pelos atributos da entidade com visibilidade protegida e os métodos
 * de acesso a estes atributos. Classe utilizada para apresentar e manter em
 * memória os dados desta entidade.
 * 
 * @see SuperVO
 * @see TransferenciaEntrada
 */
public class TransferenciaEntradaDisciplinasAproveitadasVO extends SuperVO {

    private Integer codigo;
    private DisciplinaVO disciplina;
    private Double nota;
    private Double frequencia;
    private Double cargaHoraria;
    private Integer transferenciaEntrada;
    private String anoConclusaoDisciplina;
    private String semestreConclusaoDisciplina;
    private Boolean validarAnoSemestre;
    private ConfiguracaoAcademicoVO configuracaoAcademico;
    private GradeDisciplinaVO gradeDisciplinaVO;
    private GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO;
    private ConfiguracaoAcademicoNotaConceitoVO  configuracaoAcademicoNotaConceitoVO;
    private Boolean utilizanotafinalconceito;
    private String notafinalconceito;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe
     * <code>TransferenciaEntradaDisciplinasAproveitadas</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public TransferenciaEntradaDisciplinasAproveitadasVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>TransferenciaEntradaDisciplinasAproveitadasVO</code>. Todos os
     * tipos de consistência de dados são e devem ser implementadas neste
     * método. São validações típicas: verificação de campos obrigatórios,
     * verificação de valores válidos para os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(TransferenciaEntradaDisciplinasAproveitadasVO obj, String periodicidadeCurso) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if (obj.getDisciplina().getCodigo().intValue() == 0) {
            throw new ConsistirException("O campo DISCIPLINA (TransferenciaEntradaDisciplinasAproveitadas) deve ser informado.");
        }
        if (obj.getNota().doubleValue() == 0) {
            throw new ConsistirException("O campo NOTA (TransferenciaEntradaDisciplinasAproveitadas) deve ser informado.");
        }
        if (obj.getFrequencia().doubleValue() == 0) {
            throw new ConsistirException("O campo FREQUÊNCIA (TransferenciaEntradaDisciplinasAproveitadas) deve ser informado.");
        }
        if (obj.getCargaHoraria().doubleValue() == 0) {
            throw new ConsistirException("O campo CARGA HORÁRIA (TransferenciaEntradaDisciplinasAproveitadas) deve ser informado.");
        }
        if ((periodicidadeCurso.equals("AN") || periodicidadeCurso.equals("SE"))) {
			if(obj.getAnoConclusaoDisciplina().trim().equals("")){
				throw new ConsistirException("O campo ANO deve ser informado.");
			}
			if(obj.getAnoConclusaoDisciplina().trim().length() != 4){
				throw new ConsistirException("O campo ANO deve possuir 4 dígitos.");
			}
		}
		if((!periodicidadeCurso.equals("AN") && !periodicidadeCurso.equals("SE"))){
			obj.setAnoConclusaoDisciplina("");
		}
		
		if ((periodicidadeCurso.equals("SE")) && obj.getSemestreConclusaoDisciplina().trim().equals("")) {
			throw new ConsistirException("O campo SEMESTRE deve ser informado.");
		}
		if((!periodicidadeCurso.equals("SE"))){
			obj.setSemestreConclusaoDisciplina("");
		}
        
        
//        if (obj.getValidarAnoSemestre()) {
//        	if (obj.getAnoConclusaoDisciplina().trim().isEmpty()) {
//        		throw new ConsistirException("O campo ANO CONCLUSÃO DISCIPLINA (TransferenciaEntradaDisciplinasAproveitadas) deve ser informado.");
//        	}
//        	if (obj.getAnoConclusaoDisciplina().trim().length() != 4) {
//        		throw new ConsistirException("O campo ANO CONCLUSÃO DISCIPLINA (TransferenciaEntradaDisciplinasAproveitadas) deve possuir 4 digitos.");
//        	}
//        	if (obj.getSemestreConclusaoDisciplina().trim().isEmpty()) {
//        		throw new ConsistirException("O campo SEMESTRE CONCLUSÃO DISCIPLINA (TransferenciaEntradaDisciplinasAproveitadas) deve ser informado.");
//        	}
//        }
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(0);
        setNota(0.0);
        setFrequencia(0.0);
        setCargaHoraria(0.0);
    }

    public Integer getTransferenciaEntrada() {
        return (transferenciaEntrada);
    }

    public void setTransferenciaEntrada(Integer transferenciaEntrada) {
        this.transferenciaEntrada = transferenciaEntrada;
    }

    public Double getCargaHoraria() {
        return (cargaHoraria);
    }

    public void setCargaHoraria(Double cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    public Double getFrequencia() {
        return (frequencia);
    }

    public void setFrequencia(Double frequencia) {
        this.frequencia = frequencia;
    }

    public Double getNota() {
    	return (nota);
    }

    public void setNota(Double nota) {
    	if (nota != null) {
            if (nota > 10.0) {
            	nota = nota / 10;
            }
        }
    	this.nota = nota;
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
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getCampoOrdenacao() {
        return getDisciplina().getNome();
    }

    public boolean getIsPossuiFrequencia() {
        if (getFrequencia() > 0) {
            return true;
        }
        return false;
    }

	public String getAnoConclusaoDisciplina() {
		if(anoConclusaoDisciplina == null){
			anoConclusaoDisciplina = "";
		}
		return anoConclusaoDisciplina;
	}

	public void setAnoConclusaoDisciplina(String anoConclusaoDisciplina) {
		this.anoConclusaoDisciplina = anoConclusaoDisciplina;
	}

	public String getSemestreConclusaoDisciplina() {
		if(semestreConclusaoDisciplina == null){
			semestreConclusaoDisciplina = "";
		}
		return semestreConclusaoDisciplina;
	}

	public void setSemestreConclusaoDisciplina(String semestreConclusaoDisciplina) {
		this.semestreConclusaoDisciplina = semestreConclusaoDisciplina;
	}

	public Boolean getValidarAnoSemestre() {
		if (validarAnoSemestre == null) {
			validarAnoSemestre = Boolean.TRUE;
		}
		return validarAnoSemestre;
	}

	public void setValidarAnoSemestre(Boolean validarAnoSemestre) {
		this.validarAnoSemestre = validarAnoSemestre;
	}

	public ConfiguracaoAcademicoVO getConfiguracaoAcademico() {
		if (configuracaoAcademico == null) {
			configuracaoAcademico = new ConfiguracaoAcademicoVO();
		}
		return configuracaoAcademico;
	}

	public void setConfiguracaoAcademico(ConfiguracaoAcademicoVO configuracaoAcademico) {
		this.configuracaoAcademico = configuracaoAcademico;
	}

	public GradeDisciplinaVO getGradeDisciplinaVO() {
		return gradeDisciplinaVO;
	}

	public void setGradeDisciplinaVO(GradeDisciplinaVO gradeDisciplinaVO) {
		this.gradeDisciplinaVO = gradeDisciplinaVO;
	}

	public GradeCurricularGrupoOptativaDisciplinaVO getGradeCurricularGrupoOptativaDisciplinaVO() {
		return gradeCurricularGrupoOptativaDisciplinaVO;
	}

	public void setGradeCurricularGrupoOptativaDisciplinaVO(
			GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO) {
		this.gradeCurricularGrupoOptativaDisciplinaVO = gradeCurricularGrupoOptativaDisciplinaVO;
	}

	public ConfiguracaoAcademicoNotaConceitoVO getConfiguracaoAcademicoNotaConceitoVO() {
		if (configuracaoAcademicoNotaConceitoVO == null) {
			configuracaoAcademicoNotaConceitoVO = new ConfiguracaoAcademicoNotaConceitoVO();
		}
		return configuracaoAcademicoNotaConceitoVO;
	}

	public void setConfiguracaoAcademicoNotaConceitoVO(
			ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO) {
		this.configuracaoAcademicoNotaConceitoVO = configuracaoAcademicoNotaConceitoVO;
	}

	public Boolean getUtilizanotafinalconceito() {
		if (utilizanotafinalconceito == null) {
			utilizanotafinalconceito = Boolean.FALSE;
		}
		return utilizanotafinalconceito;
	}

	public void setUtilizanotafinalconceito(Boolean utilizanotafinalconceito) {
		this.utilizanotafinalconceito = utilizanotafinalconceito;
	}

	public String getNotafinalconceito() {
		if (notafinalconceito == null) {
			notafinalconceito = "";
		}
		return notafinalconceito;
	}

	public void setNotafinalconceito(String notafinalconceito) {
		this.notafinalconceito = notafinalconceito;
	}
    
    
	
}
