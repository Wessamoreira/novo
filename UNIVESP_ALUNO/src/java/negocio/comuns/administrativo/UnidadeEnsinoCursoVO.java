package negocio.comuns.administrativo;


import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.administrativo.UnidadeEnsino;

/**
 * Reponsável por manter os dados da entidade UnidadeEnsinoCurso. Classe do tipo
 * VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 * @see UnidadeEnsino
 */

@XmlRootElement(name = "unidadeEnsinoCursoVO")
public class UnidadeEnsinoCursoVO extends SuperVO {

	public static final long serialVersionUID = 1L;

    private Integer unidadeEnsino;
    private String nomeUnidadeEnsino;
    private Integer codigo;
    private String situacaoCurso;
    private Integer nrVagasPeriodoLetivo;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Curso </code>.
     */
    private CursoVO curso;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Turno </code>.
     */
    private TurnoVO turno;
//    private PlanoFinanceiroCursoVO planoFinanceiroCurso;
    private String mantenedora;
    private String mantida;
    private FuncionarioVO coordenadorTCC;
    private Double valorMensalidade;
    private String codigoItemListaServico;
    private Integer codigoInep;   
    private Integer codigoCursoUnidadeEnsinoGinfes;

    
    private Boolean filtrarUnidadeEnsinoCurso;
    
    public enum EnumCampoConsultaUnidadeEnsinoCurso {
		UNIDADEENSINO
	}


    /**
     * Construtor padrão da classe <code>UnidadeEnsinoCurso</code>. Cria uma
     * nova instância desta entidade, inicializando automaticamente seus
     * atributos (Classe VO).
     */
    public UnidadeEnsinoCursoVO() {
        super();
        inicializarDados();
    }
    
    public UnidadeEnsinoCursoVO getClone() {
		try {
			UnidadeEnsinoCursoVO obj = (UnidadeEnsinoCursoVO) super.clone();
			obj.setCodigo(getCodigo());
			obj.setCurso((CursoVO) getCurso().clone());
			obj.setTurno((TurnoVO) getTurno().clone());
//			obj.setPlanoFinanceiroCurso((PlanoFinanceiroCursoVO) getPlanoFinanceiroCurso().clone());
			obj.setCoordenadorTCC((FuncionarioVO) getCoordenadorTCC().clone());
			obj.setUnidadeEnsino(getUnidadeEnsino());
			obj.setNomeUnidadeEnsino(getNomeUnidadeEnsino());
		    obj.setSituacaoCurso(getSituacaoCurso());
		    obj.setNrVagasPeriodoLetivo(getNrVagasPeriodoLetivo());
		    obj.setMantenedora(getMantenedora());
		    obj.setMantida(getMantida());
		    obj.setValorMensalidade(getValorMensalidade());
		    obj.setCodigoItemListaServico(getCodigoItemListaServico());
		    obj.setCodigoCursoUnidadeEnsinoGinfes(getCodigoCursoUnidadeEnsinoGinfes());
			return obj;
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>UnidadeEnsinoCursoVO</code>. Todos os tipos de consistência de
     * dados são e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(UnidadeEnsinoCursoVO obj) throws ConsistirException {
        if ((obj.getCurso() == null) || (obj.getCurso().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo CURSO (Cursos) deve ser informado.");
        }
        if (obj.getSituacaoCurso() == null || obj.getSituacaoCurso().equals("")) {
            throw new ConsistirException("O campo SITUAÇÃO CURSO (Cursos) deve ser informado.");
        }
        // if (obj.getNrVagasPeriodoLetivo().intValue() == 0) {
        // throw new
        // ConsistirException("O campo NR. VAGAS POR PERÍODO LETIVO (Cursos) deve ser informado.");
        // }
        if ((obj.getTurno() == null) || (obj.getTurno().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo TURNO (Cursos) deve ser informado.");
        }
        // if ((obj.getPlanoFinanceiroCurso() == null) ||
        // (obj.getPlanoFinanceiroCurso().getCodigo().intValue() == 0)) {
        // throw new
        // ConsistirException("O campo PLANO FINANCEIRO DO CURSO (Cursos) deve ser informado.");
        // }
    }

    @XmlElement(name = "nomeCursoTurnoApresentar")
    public String getNomeCursoTurno() {
    	 if (getCurso() != null && !Uteis.isAtributoPreenchido(getTurno())) {
             return getCurso().getNome();
         }
        if (getCurso() != null && getTurno() != null) {
            return getCurso().getNome() + " - " + getTurno().getNome();
        }
        return "";
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setSituacaoCurso("");
        setNrVagasPeriodoLetivo(0);
        setCodigo(0);
    }

    /**
     * Retorna o objeto da classe <code>Turno</code> relacionado com (
     * <code>UnidadeEnsinoCurso</code>).
     */
    @XmlElement(name = "turno")
    public TurnoVO getTurno() {
        if (turno == null) {
            turno = new TurnoVO();
        }
        return (turno);
    }

    /**
     * Define o objeto da classe <code>Turno</code> relacionado com (
     * <code>UnidadeEnsinoCurso</code>).
     */
    public void setTurno(TurnoVO obj) {
        this.turno = obj;
    }

    /**
     * Retorna o objeto da classe <code>Curso</code> relacionado com (
     * <code>UnidadeEnsinoCurso</code>).
     */
    @XmlElement(name = "curso")
    public CursoVO getCurso() {
        if (curso == null) {
            curso = new CursoVO();
        }
        return (curso);
    }

    /**
     * Define o objeto da classe <code>Curso</code> relacionado com (
     * <code>UnidadeEnsinoCurso</code>).
     */
    public void setCurso(CursoVO obj) {
        this.curso = obj;
    }

    public Integer getNrVagasPeriodoLetivo() {
        if (nrVagasPeriodoLetivo == null) {
            nrVagasPeriodoLetivo = 0;
        }
        return (nrVagasPeriodoLetivo);
    }

    public void setNrVagasPeriodoLetivo(Integer nrVagasPeriodoLetivo) {
        this.nrVagasPeriodoLetivo = nrVagasPeriodoLetivo;
    }

    public String getSituacaoCurso() {
        if (situacaoCurso == null) {
            situacaoCurso = "";
        }
        return (situacaoCurso);
    }

    @XmlElement(name = "codigo")
    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    /**
     * Operação responsável por retornar o valor de apresentação de um atributo
     * com um domínio específico. Com base no valor de armazenamento do atributo
     * esta função é capaz de retornar o de apresentação correspondente. Útil
     * para campos como sexo, escolaridade, etc.
     */
    public String getSituacaoCurso_Apresentar() {
        if (getSituacaoCurso().equals("IN")) {
            return "Inativo";
        }
        if (getSituacaoCurso().equals("AT")) {
            return "Ativo";
        }
        return (situacaoCurso);
    }

    public void setSituacaoCurso(String situacaoCurso) {
        this.situacaoCurso = situacaoCurso;
    }

    public Integer getUnidadeEnsino() {
        if (unidadeEnsino == null) {
            unidadeEnsino = 0;
        }
        return (unidadeEnsino);
    }

    public void setUnidadeEnsino(Integer unidadeEnsino) {
        this.unidadeEnsino = unidadeEnsino;
    }

    public String getNomeUnidadeEnsino() {
        if(nomeUnidadeEnsino == null){
            nomeUnidadeEnsino = "";
        }
        return nomeUnidadeEnsino;
    }

    public void setNomeUnidadeEnsino(String nomeUnidadeEnsino) {
        this.nomeUnidadeEnsino = nomeUnidadeEnsino;
    }

   
    
    public String getMantenedora() {
		if (mantenedora == null) {
			mantenedora = "";
		}
		return mantenedora;
	}

	public void setMantenedora(String mantenedora) {
		this.mantenedora = mantenedora;
	}

    public String getMantida() {
        if (mantida == null) {
            mantida = "";
        }
        return mantida;
    }

    public void setMantida(String mantida) {
        this.mantida = mantida;
    }

	public FuncionarioVO getCoordenadorTCC() {
		if (coordenadorTCC == null) {
			coordenadorTCC = new FuncionarioVO();
		}
		return coordenadorTCC;
	}

	public void setCoordenadorTCC(FuncionarioVO coordenadorTCC) {
		this.coordenadorTCC = coordenadorTCC;
	}
	
	public Double getValorMensalidade() {
		if (valorMensalidade == null) {
			valorMensalidade = 0.0;
		}
		return valorMensalidade;
	}

	public void setValorMensalidade(Double valorMensalidade) {
		this.valorMensalidade = valorMensalidade;
	}

	public String getCodigoItemListaServico() {
		if (codigoItemListaServico == null) {
			codigoItemListaServico = "";
		}
		return codigoItemListaServico;
	}

	public void setCodigoItemListaServico(String codigoItemListaServico) {
		this.codigoItemListaServico = codigoItemListaServico;
	}
	
	public Integer getCodigoCursoUnidadeEnsinoGinfes() {
		if(codigoCursoUnidadeEnsinoGinfes == null) {
			codigoCursoUnidadeEnsinoGinfes = 0;
		}
		return codigoCursoUnidadeEnsinoGinfes;
	}

	public void setCodigoCursoUnidadeEnsinoGinfes(Integer codigoCursoUnidadeEnsinoGinfes) {
		this.codigoCursoUnidadeEnsinoGinfes = codigoCursoUnidadeEnsinoGinfes;
	}

	
	public Integer getCodigoInep() {
		if (codigoInep == null) {
			codigoInep = 0;
		}
		return codigoInep;
	}

	public void setCodigoInep(Integer codigoInep) {
		this.codigoInep = codigoInep;
	}
	
	public Boolean getFiltrarUnidadeEnsinoCurso() {
		if (filtrarUnidadeEnsinoCurso == null) {
			filtrarUnidadeEnsinoCurso = false;
		}
		return filtrarUnidadeEnsinoCurso;
	}
	
	public void setFiltrarUnidadeEnsinoCurso(Boolean filtrarUnidadeEnsinoCurso) {
		this.filtrarUnidadeEnsinoCurso = filtrarUnidadeEnsinoCurso;
	}
	
}
