package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;

/**
 * Reponsável por manter os dados da entidade ProgramacaoFormatura. Classe do
 * tipo VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class ProgramacaoFormaturaVO extends SuperVO {

    private Integer codigo;
    private Date dataCadastro;
    /**
     * Atributo responsável por manter os objetos da classe
     * <code>ProgramacaoFormaturaAluno</code>.
     */
    private List<ProgramacaoFormaturaAlunoVO> programacaoFormaturaAlunoVOs;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>UnidadeEnsino </code>.
     */
    private UnidadeEnsinoVO unidadeEnsino;
    
    private String unidadeEnsinoDescricao;
    
    private List<ProgramacaoFormaturaUnidadeEnsinoVO> programacaoFormaturaUnidadeEnsinoVOs;
    
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Curso </code>.
     */
    // private CursoVO curso;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Turno </code>.
     */
    // private TurnoVO turno;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Turma </code>.
     */
    // private TurmaVO turma;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Matricula </code>.
     */
    // private MatriculaVO aluno;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Requerimento </code>.
     */
    // private RequerimentoVO requerimento;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>ColacaoGrau </code>.
     */
    // private ColacaoGrauVO colacaoGrau;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Usuario </code>.
     */
    private UsuarioVO responsavelCadastro;
    
    private ColacaoGrauVO colacaoGrauVO;
    
    private String nivelEducacional;
    
    /*
     * Atributo transiente
     * CursoVO
     */
    private CursoVO curso;
    private Date dataLimiteAssinaturaAta;
    
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>ProgramacaoFormatura</code>. Cria uma
     * nova instância desta entidade, inicializando automaticamente seus
     * atributos (Classe VO).
     */
    public ProgramacaoFormaturaVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar a unicidade dos dados de um objeto da
     * classe <code>ProgramacaoFormaturaVO</code>.
     */
    public static void validarUnicidade(List<ProgramacaoFormaturaVO> lista, ProgramacaoFormaturaVO obj) throws ConsistirException {
        for (ProgramacaoFormaturaVO repetido : lista) {
        }
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>ProgramacaoFormaturaVO</code>. Todos os tipos de consistência de
     * dados são e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(ProgramacaoFormaturaVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if(!obj.getProgramacaoFormaturaUnidadeEnsinoVOs().stream().anyMatch(t -> t.getSelecionado())) {
     	   throw new ConsistirException("O campo UNIDADE ENSINO (Programação Formatura) deve ser informado.");
        }
        if (!Uteis.isAtributoPreenchido(obj.getColacaoGrauVO())) {
            throw new ConsistirException("O campo COLAÇÃO GRAU (Programação Formatura) deve ser informado.");
        }
        if (!Uteis.isAtributoPreenchido(obj.getNivelEducacional())) {
            throw new ConsistirException("O campo NÍVEL EDUCACIONAL (Programação Formatura) deve ser informado.");
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
        setDataCadastro(new Date());
    }

    /**
     * Operação responsável por adicionar um novo objeto da classe
     * <code>ProgramacaoFormaturaAlunoVO</code> ao List
     * <code>programacaoFormaturaAlunoVOs</code>. Utiliza o atributo padrão de
     * consulta da classe <code>ProgramacaoFormaturaAluno</code> -
     * getAluno().getMatricula() - como identificador (key) do objeto no List.
     *
     * @param obj
     *            Objeto da classe <code>ProgramacaoFormaturaAlunoVO</code> que
     *            será adiocionado ao Hashtable correspondente.
     */
    public void adicionarObjProgramacaoFormaturaAlunoVOs(ProgramacaoFormaturaAlunoVO obj) throws Exception {
        ProgramacaoFormaturaAlunoVO.validarDados(obj);
        // obj.setProgramacaoFormatura(this);
        int index = 0;
        Iterator i = getProgramacaoFormaturaAlunoVOs().iterator();
        while (i.hasNext()) {
            ProgramacaoFormaturaAlunoVO objExistente = (ProgramacaoFormaturaAlunoVO) i.next();
            if ((objExistente.getMatricula().getMatricula().equals(obj.getMatricula().getMatricula()))
                    && (!objExistente.getColouGrau().equals("NO"))) {
                getProgramacaoFormaturaAlunoVOs().set(index, obj);
                return;
            }
            index++;
        }
        getProgramacaoFormaturaAlunoVOs().add(obj);
    }

    /**
     * Operação responsável por excluir um objeto da classe
     * <code>ProgramacaoFormaturaAlunoVO</code> no List
     * <code>programacaoFormaturaAlunoVOs</code>. Utiliza o atributo padrão de
     * consulta da classe <code>ProgramacaoFormaturaAluno</code> -
     * getAluno().getMatricula() - como identificador (key) do objeto no List.
     *
     * @param aluno
     *            Parâmetro para localizar e remover o objeto do List.
     */
    public void excluirObjProgramacaoFormaturaAlunoVOs(String matricula) throws Exception {
        int index = 0;
        Iterator i = getProgramacaoFormaturaAlunoVOs().iterator();
        while (i.hasNext()) {
            ProgramacaoFormaturaAlunoVO objExistente = (ProgramacaoFormaturaAlunoVO) i.next();
            if (objExistente.getMatricula().getMatricula().equals(matricula)
                    && (objExistente.getColouGrau().equals("NI"))) {
                getProgramacaoFormaturaAlunoVOs().remove(index);
                return;
            }
            index++;
        }
        throw new ConsistirException("Não foi encontrado nenhum dado para exclusão.");
    }

    /**
     * Operação responsável por consultar um objeto da classe
     * <code>ProgramacaoFormaturaAlunoVO</code> no List
     * <code>programacaoFormaturaAlunoVOs</code>. Utiliza o atributo padrão de
     * consulta da classe <code>ProgramacaoFormaturaAluno</code> -
     * getAluno().getMatricula() - como identificador (key) do objeto no List.
     *
     * @param aluno
     *            Parâmetro para localizar o objeto do List.
     */
    public ProgramacaoFormaturaAlunoVO consultarObjProgramacaoFormaturaAlunoVO(String matricula) throws Exception {
        Iterator i = getProgramacaoFormaturaAlunoVOs().iterator();
        while (i.hasNext()) {
            ProgramacaoFormaturaAlunoVO objExistente = (ProgramacaoFormaturaAlunoVO) i.next();
            if (objExistente.getMatricula().getMatricula().equals(matricula)) {
                return objExistente;
            }
        }
        return null;
    }

    /**
     * Retorna o objeto da classe <code>Usuario</code> relacionado com (
     * <code>ProgramacaoFormatura</code>).
     */
    public UsuarioVO getResponsavelCadastro() {
        if (responsavelCadastro == null) {
            responsavelCadastro = new UsuarioVO();
        }
        return (responsavelCadastro);
    }

    /**
     * Define o objeto da classe <code>Usuario</code> relacionado com (
     * <code>ProgramacaoFormatura</code>).
     */
    public void setResponsavelCadastro(UsuarioVO obj) {
        this.responsavelCadastro = obj;
    }

    /**
     * Retorna o objeto da classe <code>UnidadeEnsino</code> relacionado com (
     * <code>ProgramacaoFormatura</code>).
     */
    public UnidadeEnsinoVO getUnidadeEnsino() {
        if (unidadeEnsino == null) {
            unidadeEnsino = new UnidadeEnsinoVO();
        }
        return (unidadeEnsino);
    }

    /**
     * Define o objeto da classe <code>UnidadeEnsino</code> relacionado com (
     * <code>ProgramacaoFormatura</code>).
     */
    public void setUnidadeEnsino(UnidadeEnsinoVO obj) {
        this.unidadeEnsino = obj;
    }

    /**
     * Retorna Atributo responsável por manter os objetos da classe
     * <code>ProgramacaoFormaturaAluno</code>.
     */
    public List<ProgramacaoFormaturaAlunoVO> getProgramacaoFormaturaAlunoVOs() {
        if (programacaoFormaturaAlunoVOs == null) {
            programacaoFormaturaAlunoVOs = new ArrayList<>();
        }
        return (programacaoFormaturaAlunoVOs);
    }

    /**
     * Define Atributo responsável por manter os objetos da classe
     * <code>ProgramacaoFormaturaAluno</code>.
     */
    public void setProgramacaoFormaturaAlunoVOs(List programacaoFormaturaAlunoVOs) {
        this.programacaoFormaturaAlunoVOs = programacaoFormaturaAlunoVOs;
    }

    public Date getDataCadastro() {
        if (dataCadastro == null) {
            dataCadastro = new Date();
        }
        return (dataCadastro);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getDataCadastro_Apresentar() {
        return (Uteis.getData(dataCadastro));
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
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
    
    public List<ProgramacaoFormaturaUnidadeEnsinoVO> getProgramacaoFormaturaUnidadeEnsinoVOs() {
		if (programacaoFormaturaUnidadeEnsinoVOs == null) {
			programacaoFormaturaUnidadeEnsinoVOs = new ArrayList<ProgramacaoFormaturaUnidadeEnsinoVO>(0);
		}
    	return programacaoFormaturaUnidadeEnsinoVOs;
	}
    
    public void setProgramacaoFormaturaUnidadeEnsinoVOs(List<ProgramacaoFormaturaUnidadeEnsinoVO> programacaoFormaturaUnidadeEnsinoVOs) {
		this.programacaoFormaturaUnidadeEnsinoVOs = programacaoFormaturaUnidadeEnsinoVOs;
	}
    
    public String getUnidadeEnsinoDescricao() {
		if (unidadeEnsinoDescricao == null) {
			unidadeEnsinoDescricao = "";
			if (Uteis.isAtributoPreenchido(getProgramacaoFormaturaUnidadeEnsinoVOs())) {
				unidadeEnsinoDescricao = getProgramacaoFormaturaUnidadeEnsinoVOs().stream().filter(ProgramacaoFormaturaUnidadeEnsinoVO::getSelecionado).map(programacaoFormaturaUnidadeEnsino -> programacaoFormaturaUnidadeEnsino.getUnidadeEnsinoVO().getNome()).collect(Collectors.joining(", "));
			}
		}
    	return unidadeEnsinoDescricao;
	}
    
    public void setUnidadeEnsinoDescricao(String unidadeEnsinoDescricao) {
		this.unidadeEnsinoDescricao = unidadeEnsinoDescricao;
	}
    
    private String unidadeConsultaApresentar;
    
    public String getUnidadeConsultaApresentar() {
		if (unidadeConsultaApresentar == null) {
			unidadeConsultaApresentar = "";
		}
    	return unidadeConsultaApresentar;
	}
    
    public void setUnidadeConsultaApresentar(String unidadeConsultaApresentar) {
		this.unidadeConsultaApresentar = unidadeConsultaApresentar;
	}
    
    public ColacaoGrauVO getColacaoGrauVO() {
		if (colacaoGrauVO == null) {
			colacaoGrauVO = new ColacaoGrauVO();
		}
    	return colacaoGrauVO;
	}
    
    public void setColacaoGrauVO(ColacaoGrauVO colacaoGrauVO) {
		this.colacaoGrauVO = colacaoGrauVO;
	}
    
    public String getNivelEducacional() {
		if (nivelEducacional == null) {
			nivelEducacional = "";
		}
    	return nivelEducacional;
	}
    
    public void setNivelEducacional(String nivelEducacional) {
		this.nivelEducacional = nivelEducacional;
	}
    
    public String getNivelEducacional_Apresentar() {
        return TipoNivelEducacional.getDescricao(getNivelEducacional());
    }

    /*
     * Atributo transiente
     * CursoVO
     */
    public CursoVO getCurso() {
		if (curso == null) {
			curso = new CursoVO();
		}
    	return curso;
	}
    
    public void setCurso(CursoVO curso) {
		this.curso = curso;
	}
    
    public Date getDataLimiteAssinaturaAta() {
		return dataLimiteAssinaturaAta;
	}
    
    public void setDataLimiteAssinaturaAta(Date dataLimiteAssinaturaAta) {
		this.dataLimiteAssinaturaAta = dataLimiteAssinaturaAta;
	}
    
    public Boolean getNivelEducacionalGraduacaoGraduacaoTecnologica() {
		if (getNivelEducacional().equals("SU") || getNivelEducacional().equals("GT")) {
			return true;
		}
		return false;
	}
}
