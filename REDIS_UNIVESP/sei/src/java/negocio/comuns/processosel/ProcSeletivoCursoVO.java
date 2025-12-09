package negocio.comuns.processosel;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.facade.jdbc.processosel.ProcSeletivo;

/**
 * Reponsável por manter os dados da entidade ProcSeletivoCurso. Classe do tipo
 * VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 * @see ProcSeletivo
 */
@XmlRootElement(name = "procSeletivoCurso")
public class ProcSeletivoCursoVO extends SuperVO {

    private ProcSeletivoUnidadeEnsinoVO procSeletivoUnidadeEnsino; 
    private Integer codigo;
    private Integer numeroVaga;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Curso </code>.
     */
    private UnidadeEnsinoCursoVO unidadeEnsinoCurso;
	private GrupoDisciplinaProcSeletivoVO grupoDisciplinaProcSeletivo;
    public static final long serialVersionUID = 1L;
    private Date dataInicioProcSeletivoCurso;
    private Date dataFimProcSeletivoCurso;

    /**
     * Construtor padrão da classe <code>ProcSeletivoCurso</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public ProcSeletivoCursoVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>ProcSeletivoCursoVO</code>. Todos os tipos de consistência de dados
     * são e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(ProcSeletivoCursoVO obj) throws ConsistirException {
        if (obj.getUnidadeEnsinoCurso() == null || obj.getUnidadeEnsinoCurso().getCodigo().equals(0)) {
            throw new ConsistirException("O campo CURSO (Cursos Processo Seletivo) deve ser informado.");
        }
        if (obj.getDataInicioProcSeletivoCurso() != null && (obj.getDataInicioProcSeletivoCurso().before(obj.getProcSeletivoUnidadeEnsino().getProcSeletivo().getDataInicio()) 
        		|| obj.getDataInicioProcSeletivoCurso().after(obj.getProcSeletivoUnidadeEnsino().getProcSeletivo().getDataFim()))) {
			throw new ConsistirException("O campo Período de Inscrição do curso " + obj.getUnidadeEnsinoCurso().getCurso().getNome() + " (Cursos Processo Seletivo) deve estar entre a Data Início e Data Fim do Processo Seletivo.");
        }
        if (obj.getDataFimProcSeletivoCurso() != null && (obj.getDataFimProcSeletivoCurso().before(obj.getProcSeletivoUnidadeEnsino().getProcSeletivo().getDataInicio()) 
        		|| obj.getDataFimProcSeletivoCurso().after(obj.getProcSeletivoUnidadeEnsino().getProcSeletivo().getDataFim()))) {
        	throw new ConsistirException("O campo Período de Inscrição do curso " + obj.getUnidadeEnsinoCurso().getCurso().getNome() + " (Cursos Processo Seletivo) deve estar entre a Data Início e Data Fim do Processo Seletivo.");
        }
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(0);
        getProcSeletivoUnidadeEnsino().setCodigo(0);
    }

    @XmlElement(name = "unidadeEnsinoCurso")
    public UnidadeEnsinoCursoVO getUnidadeEnsinoCurso() {
        if (unidadeEnsinoCurso == null) {
            unidadeEnsinoCurso = new UnidadeEnsinoCursoVO();
        }
        return unidadeEnsinoCurso;
    }

    public void setUnidadeEnsinoCurso(UnidadeEnsinoCursoVO unidadeEnsinoCurso) {
        this.unidadeEnsinoCurso = unidadeEnsinoCurso;
    }

    /**
     * Retorna o objeto da classe <code>Curso</code> relacionado com (
     * <code>ProcSeletivoCurso</code>).
     */
    @XmlElement(name ="codigo")
    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public ProcSeletivoUnidadeEnsinoVO getProcSeletivoUnidadeEnsino() {
		if (procSeletivoUnidadeEnsino == null) {
			procSeletivoUnidadeEnsino = new ProcSeletivoUnidadeEnsinoVO();
		}
		return (procSeletivoUnidadeEnsino);
	}

    public void setProcSeletivoUnidadeEnsino(ProcSeletivoUnidadeEnsinoVO procSeletivoUnidadeEnsino) {
        this.procSeletivoUnidadeEnsino = procSeletivoUnidadeEnsino;
    }

    
    @XmlElement(name = "numeroVaga")
	public Integer getNumeroVaga() {
		if(numeroVaga == null){
			numeroVaga = 0;
		}
		return numeroVaga;
	}

	public void setNumeroVaga(Integer numeroVaga) {
		this.numeroVaga = numeroVaga;
	}

	 @XmlElement(name ="dataInicioProcSeletivoCurso")
	public Date getDataInicioProcSeletivoCurso() {
		return dataInicioProcSeletivoCurso;
	}

	public void setDataInicioProcSeletivoCurso(Date dataInicioProcSeletivoCurso) {
		this.dataInicioProcSeletivoCurso = dataInicioProcSeletivoCurso;
	}

	 @XmlElement(name ="dataFimProcSeletivoCurso")
	public Date getDataFimProcSeletivoCurso() {
		return dataFimProcSeletivoCurso;
	}

	public void setDataFimProcSeletivoCurso(Date dataFimProcSeletivoCurso) {
		this.dataFimProcSeletivoCurso = dataFimProcSeletivoCurso;
	}
    
	 @XmlElement(name ="grupoDisciplinaProcSeletivo")
	public GrupoDisciplinaProcSeletivoVO getGrupoDisciplinaProcSeletivo() {
		if (grupoDisciplinaProcSeletivo == null) {
			grupoDisciplinaProcSeletivo = new GrupoDisciplinaProcSeletivoVO();
		}
		return grupoDisciplinaProcSeletivo;
	}

	public void setGrupoDisciplinaProcSeletivo(GrupoDisciplinaProcSeletivoVO grupoDisciplinaProcSeletivo) {
		this.grupoDisciplinaProcSeletivo = grupoDisciplinaProcSeletivo;
	}
    
    public String getOrdenacao() {
    	return getUnidadeEnsinoCurso().getNomeCursoTurno();
    }
}
