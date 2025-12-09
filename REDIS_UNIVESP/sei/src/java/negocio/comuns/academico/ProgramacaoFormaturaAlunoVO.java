package negocio.comuns.academico;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Constantes;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoAcademicaProgramacaoFormaturaAluno;
import negocio.comuns.utilitarias.dominios.SituacaoColouGrauProgramacaoFormaturaAluno;
import negocio.comuns.utilitarias.dominios.SituacaoDocumentacaoProgramacaoFormaturaAluno;
import negocio.comuns.utilitarias.dominios.SituacaoFinanceiraProgramacaoFormaturaAluno;
import negocio.facade.jdbc.academico.ProgramacaoFormatura;

/**
 * Reponsável por manter os dados da entidade ProgramacaoFormaturaAluno. Classe
 * do tipo VO - Value Object composta pelos atributos da entidade com
 * visibilidade protegida e os métodos de acesso a estes atributos. Classe
 * utilizada para apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 * @see ProgramacaoFormatura
 */
public class ProgramacaoFormaturaAlunoVO extends SuperVO {

    private Integer codigo;
    private Integer programacaoFormatura;
    private String colouGrau;
    private String situacaoFinanceira;
    private String situacaoAcademica;
    private String situacaoDocumentacao;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Matricula </code>.
     */
    private MatriculaVO matricula;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Requerimento </code>.
     */
    private Integer requerimento;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>ColacaoGrau </code>.
     */
    private ColacaoGrauVO colacaoGrau;
    private boolean permitirAlterarSituacaoColouGrau;
    private boolean curriculoIntegralizado = false;
    
    /*
     * dataConclusaoCurso = transiente
     * 
     */
    private Date dataConclusaoCurso;
    
    private Integer horasAtividadeComplementarExigida;
    private Integer horasAtividadeComplementarCumprida;
    private Integer horasEstagioExigida;
    private Integer horasEstagioCumprida;
    private Integer horasDisciplinaObrigatoriaExigida;
    private Integer horasDisciplinaObrigatoriaCumprida;
    private List<GradeDisciplinaVO> listaGradeDisciplinaObrigatorioPendente;
    private Integer horasDisciplinaOptativaExigida;
    private Integer horasDisciplinaOptativaCumprida;
    
    private MatriculaEnadeVO matriculaEnadeVO;
    /**
     * atributo Transient
     */
    private Boolean existeProgramacaoFormaturaDuplicada;
    private ProgramacaoFormaturaVO programacaoFormaturaVO;
	private List<String> listaMensagemErroProcessamento;
    
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>ProgramacaoFormaturaAluno</code>. Cria
     * uma nova instância desta entidade, inicializando automaticamente seus
     * atributos (Classe VO).
     */
    public ProgramacaoFormaturaAlunoVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar a unicidade dos dados de um objeto da
     * classe <code>ProgramacaoFormaturaAlunoVO</code>.
     */
    public static void validarUnicidade(List<ProgramacaoFormaturaAlunoVO> lista, ProgramacaoFormaturaAlunoVO obj) throws ConsistirException {
        for (ProgramacaoFormaturaAlunoVO repetido : lista) {
        }
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>ProgramacaoFormaturaAlunoVO</code>. Todos os tipos de consistência
     * de dados são e devem ser implementadas neste método. São validações
     * típicas: verificação de campos obrigatórios, verificação de valores
     * válidos para os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(ProgramacaoFormaturaAlunoVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if ((obj.getMatricula().getMatricula() == null) || (obj.getMatricula().getMatricula().equals(""))) {
            throw new ConsistirException("O campo ALUNO (Programação Formatura Aluno) deve ser informado.");
        }
        if (!obj.getMatricula().getCurso().getNivelEducacionalPosGraduacao()) {
            if ((obj.getColacaoGrau() == null) || (obj.getColacaoGrau().getCodigo().intValue() == 0)) {
                throw new ConsistirException("O campo COLAÇÃO GRAU (Programação Formatura Aluno) do aluno "
                        + obj.getMatricula().getAluno().getNome() + "deve ser informado.");
            }
        }

    }

    /**
     * Operação reponsável por realizar o UpperCase dos atributos do tipo
     * String.
     */
    public void realizarUpperCaseDados() {
        if (!Uteis.realizarUpperCaseDadosAntesPersistencia) {
            return;
        }
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(null);
    }

    /**
     * Retorna o objeto da classe <code>ColacaoGrau</code> relacionado com (
     * <code>ProgramacaoFormaturaAluno</code>).
     */
    public ColacaoGrauVO getColacaoGrau() {
        if (colacaoGrau == null) {
            colacaoGrau = new ColacaoGrauVO();
        }
        return (colacaoGrau);
    }

    /**
     * Define o objeto da classe <code>ColacaoGrau</code> relacionado com (
     * <code>ProgramacaoFormaturaAluno</code>).
     */
    public void setColacaoGrau(ColacaoGrauVO obj) {
        this.colacaoGrau = obj;
    }

    /**
     * Retorna o objeto da classe <code>Matricula</code> relacionado com (
     * <code>ProgramacaoFormaturaAluno</code>).
     */
    public MatriculaVO getMatricula() {
        if (matricula == null) {
            matricula = new MatriculaVO();
        }
        return (matricula);
    }

    /**
     * Define o objeto da classe <code>Matricula</code> relacionado com (
     * <code>ProgramacaoFormaturaAluno</code>).
     */
    public void setMatricula(MatriculaVO obj) {
        this.matricula = obj;
    }

    public Integer getProgramacaoFormatura() {
        if (programacaoFormatura == null) {
            programacaoFormatura = 0;
        }
        return (programacaoFormatura);
    }

    public void setProgramacaoFormatura(Integer programacaoFormatura) {
        this.programacaoFormatura = programacaoFormatura;
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

    /**
     * @return the requerimento
     */
    public Integer getRequerimento() {
        if (requerimento == null) {
            requerimento = 0;
        }
        return requerimento;
    }

    /**
     * @param requerimento
     *            the requerimento to set
     */
    public void setRequerimento(Integer requerimento) {
        this.requerimento = requerimento;
    }

    /**
     * @return the situacaoFinanceira
     */
    public String getSituacaoFinanceira() {
        if (situacaoFinanceira == null) {
            situacaoFinanceira = "";
        }
        return situacaoFinanceira;
    }

    /**
     * @param situacaoFinanceira
     *            the situacaoFinanceira to set
     */
    public void setSituacaoFinanceira(String situacaoFinanceira) {
        this.situacaoFinanceira = situacaoFinanceira;
    }

    public String getSituacaoFinanceira_Apresentar() {
        return SituacaoFinanceiraProgramacaoFormaturaAluno.getDescricao(situacaoFinanceira);
    }

    /**
     * @return the situacaoAcademica
     */
    public String getSituacaoAcademica() {
        if (situacaoAcademica == null) {
            situacaoAcademica = "";
        }
        return situacaoAcademica;
    }

    /**
     * @param situacaoAcademica
     *            the situacaoAcademica to set
     */
    public void setSituacaoAcademica(String situacaoAcademica) {
        this.situacaoAcademica = situacaoAcademica;
    }

    public String getSituacaoAcademica_Apresentar() {
        return SituacaoAcademicaProgramacaoFormaturaAluno.getDescricao(isCurriculoIntegralizado() ? "Integralizado" : "Não Integralizado");
    }

    /**
     * @return the situacaoDocumentacao
     */
    public String getSituacaoDocumentacao() {
        if (situacaoDocumentacao == null) {
            situacaoDocumentacao = "";
        }
        return situacaoDocumentacao;
    }

    /**
     * @param situacaoDocumentacao
     *            the situacaoDocumentacao to set
     */
    public void setSituacaoDocumentacao(String situacaoDocumentacao) {
        this.situacaoDocumentacao = situacaoDocumentacao;
    }

    public String getSituacaoDocumentacao_Apresentar() {
        return SituacaoDocumentacaoProgramacaoFormaturaAluno.getDescricao(situacaoDocumentacao);
    }

    /**
     * @return the colouGrau
     */
    public String getColouGrau() {
        if (colouGrau == null) {
            colouGrau = "";
        }
        return colouGrau;
    }

    /**
     * @param colouGrau
     *            the colouGrau to set
     */
    public void setColouGrau(String colouGrau) {
        this.colouGrau = colouGrau;
    }

    public String getColouGrau_Apresentar() {
        return SituacaoColouGrauProgramacaoFormaturaAluno.getDescricao(colouGrau);
    }

    /**
     * @return the permitirAlterarSituacaoColouGrau
     */
    public boolean isPermitirAlterarSituacaoColouGrau() {
        return permitirAlterarSituacaoColouGrau;
    }

    /**
     * @param permitirAlterarSituacaoColouGrau
     *            the permitirAlterarSituacaoColouGrau to set
     */
    public void setPermitirAlterarSituacaoColouGrau(boolean permitirAlterarSituacaoColouGrau) {
        this.permitirAlterarSituacaoColouGrau = permitirAlterarSituacaoColouGrau;
    }

    
    /*
     * dataConclusaoCurso = transiente
     * 
     */
    public Date getDataConclusaoCurso() {
        return dataConclusaoCurso;
    }

    public void setDataConclusaoCurso(Date dataConclusaoCurso) {
        this.dataConclusaoCurso = dataConclusaoCurso;
    }
    
    public boolean isCurriculoIntegralizado() {
		return curriculoIntegralizado;
	}

	public void setCurriculoIntegralizado(boolean curriculoIntegralizado) {
		this.curriculoIntegralizado = curriculoIntegralizado;
	}

	public Integer getHorasAtividadeComplementarExigida() {
		if (horasAtividadeComplementarExigida == null) {
			horasAtividadeComplementarExigida = 0;
		}
		return horasAtividadeComplementarExigida;
	}

	public void setHorasAtividadeComplementarExigida(Integer horasAtividadeComplementarExigida) {
		this.horasAtividadeComplementarExigida = horasAtividadeComplementarExigida;
	}

	public Integer getHorasAtividadeComplementarCumprida() {
		if (horasAtividadeComplementarCumprida == null) {
			horasAtividadeComplementarCumprida = 0;
		}
		return horasAtividadeComplementarCumprida;
	}

	public void setHorasAtividadeComplementarCumprida(Integer horasAtividadeComplementarCumprida) {
		this.horasAtividadeComplementarCumprida = horasAtividadeComplementarCumprida;
	}

	public Integer getHorasEstagioExigida() {
		if (horasEstagioExigida == null) {
			horasEstagioExigida = 0;
		}
		return horasEstagioExigida;
	}

	public void setHorasEstagioExigida(Integer horasEstagioExigida) {
		this.horasEstagioExigida = horasEstagioExigida;
	}

	public Integer getHorasEstagioCumprida() {
		if (horasEstagioCumprida == null) {
			horasEstagioCumprida = 0;
		}
		return horasEstagioCumprida;
	}

	public void setHorasEstagioCumprida(Integer horasEstagioCumprida) {
		this.horasEstagioCumprida = horasEstagioCumprida;
	}

	public Integer getHorasDisciplinaObrigatoriaExigida() {
		if (horasDisciplinaObrigatoriaExigida == null) {
			horasDisciplinaObrigatoriaExigida = 0;
		}
		return horasDisciplinaObrigatoriaExigida;
	}

	public void setHorasDisciplinaObrigatoriaExigida(Integer horasDisciplinaObrigatoriaExigida) {
		this.horasDisciplinaObrigatoriaExigida = horasDisciplinaObrigatoriaExigida;
	}

	public Integer getHorasDisciplinaObrigatoriaCumprida() {
		if(horasDisciplinaObrigatoriaCumprida == null) {
			horasDisciplinaObrigatoriaCumprida = 0;
		}
		return horasDisciplinaObrigatoriaCumprida;
	}

	public void setHorasDisciplinaObrigatoriaCumprida(Integer horasDisciplinaObrigatoriaCumprida) {
		this.horasDisciplinaObrigatoriaCumprida = horasDisciplinaObrigatoriaCumprida;
	}

	public List<GradeDisciplinaVO> getListaGradeDisciplinaObrigatorioPendente() {
		if (listaGradeDisciplinaObrigatorioPendente == null) {
			listaGradeDisciplinaObrigatorioPendente = new ArrayList<>(0);
		}
		return listaGradeDisciplinaObrigatorioPendente;
	}

	public void setListaGradeDisciplinaObrigatorioPendente(
			List<GradeDisciplinaVO> listaGradeDisciplinaObrigatorioPendente) {
		this.listaGradeDisciplinaObrigatorioPendente = listaGradeDisciplinaObrigatorioPendente;
	}

	public Integer getHorasDisciplinaOptativaExigida() {
		if (horasDisciplinaOptativaExigida == null) {
			horasDisciplinaOptativaExigida = 0;
		}
		return horasDisciplinaOptativaExigida;
	}

	public void setHorasDisciplinaOptativaExigida(Integer horasDisciplinaOptativaExigida) {
		this.horasDisciplinaOptativaExigida = horasDisciplinaOptativaExigida;
	}

	public Integer getHorasDisciplinaOptativaCumprida() {
		if (horasDisciplinaOptativaCumprida == null) {
			horasDisciplinaOptativaCumprida = 0;
		}
		return horasDisciplinaOptativaCumprida;
	}

	public void setHorasDisciplinaOptativaCumprida(Integer horasDisciplinaOptativaCumprida) {
		this.horasDisciplinaOptativaCumprida = horasDisciplinaOptativaCumprida;
	}
	
	public Integer getHorasPendenciaAtividadeComplementar() {
		Integer horasPendenciaAtividadeComplementar = getHorasAtividadeComplementarExigida() - getHorasAtividadeComplementarCumprida();
		if (horasPendenciaAtividadeComplementar < 0 ) {
			horasPendenciaAtividadeComplementar = 0;
		}
		return horasPendenciaAtividadeComplementar;
	}
	public Integer getHorasPendenciaEstagio() {
		Integer horasPendenciaEstagio = getHorasEstagioExigida() - getHorasEstagioCumprida();
		if (horasPendenciaEstagio < 0 ) {
			horasPendenciaEstagio = 0;
		}
		return horasPendenciaEstagio;
	}
	public Integer getHorasPendenciaDisciplinaOptativa() {
		Integer horasPendenciaDisciplinaOptativa = getHorasDisciplinaOptativaExigida() - getHorasDisciplinaOptativaCumprida();
		if (horasPendenciaDisciplinaOptativa < 0 ) {
			horasPendenciaDisciplinaOptativa = 0;
		}
		return horasPendenciaDisciplinaOptativa;
	}
	
	public MatriculaEnadeVO getMatriculaEnadeVO() {
		if (matriculaEnadeVO == null) {
			matriculaEnadeVO = new MatriculaEnadeVO();
		}
		return matriculaEnadeVO;
	}
	
	public void setMatriculaEnadeVO(MatriculaEnadeVO matriculaEnadeVO) {
		this.matriculaEnadeVO = matriculaEnadeVO;
	}

	

	private Boolean possuiDiploma;
	
	public Boolean getPossuiDiploma() {
		if (possuiDiploma == null) {
			possuiDiploma = false;
		}
		return possuiDiploma;
	}
	
	public void setPossuiDiploma(Boolean possuiDiploma) {
		this.possuiDiploma = possuiDiploma;
	}
	
	
	private Boolean matriculaAptaInativacaoCredenciasAlunosFormados;

	
	public Boolean getMatriculaAptaInativacaoCredenciasAlunosFormados() {
		if(matriculaAptaInativacaoCredenciasAlunosFormados == null ) {
			matriculaAptaInativacaoCredenciasAlunosFormados = Boolean.FALSE;
		}
		return matriculaAptaInativacaoCredenciasAlunosFormados;
	}

	public void setMatriculaAptaInativacaoCredenciasAlunosFormados(
			Boolean matriculaAptaInativacaoCredenciasAlunosFormados) {
		this.matriculaAptaInativacaoCredenciasAlunosFormados = matriculaAptaInativacaoCredenciasAlunosFormados;
	}
	
	public Boolean getExisteProgramacaoFormaturaDuplicada() {
		if (existeProgramacaoFormaturaDuplicada == null) {
			existeProgramacaoFormaturaDuplicada = Boolean.FALSE;
		}
		return existeProgramacaoFormaturaDuplicada;
	}
	
	public void setExisteProgramacaoFormaturaDuplicada(Boolean existeProgramacaoFormaturaDuplicada) {
		this.existeProgramacaoFormaturaDuplicada = existeProgramacaoFormaturaDuplicada;
	}

	public ProgramacaoFormaturaVO getProgramacaoFormaturaVO() {
		if (programacaoFormaturaVO == null) {
			programacaoFormaturaVO = new ProgramacaoFormaturaVO();
		}
		return programacaoFormaturaVO;
	}
	
	public void setProgramacaoFormaturaVO(ProgramacaoFormaturaVO programacaoFormaturaVO) {
		this.programacaoFormaturaVO = programacaoFormaturaVO;
	}
		
	public List<String> getListaMensagemErroProcessamento() {
		if(listaMensagemErroProcessamento == null) {
			listaMensagemErroProcessamento = new ArrayList<String>(0);
		}
		return listaMensagemErroProcessamento;
	}

	public void setListaMensagemErroProcessamento(List<String> listaMensagemErroProcessamento) {
		this.listaMensagemErroProcessamento = listaMensagemErroProcessamento;
	}
}
