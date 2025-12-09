package negocio.comuns.academico;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;

import negocio.comuns.academico.enumeradores.NomeTurnoCensoEnum;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.annotation.ExcluirJsonAnnotation;
import negocio.comuns.processosel.GrupoDisciplinaProcSeletivoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.dominios.DiaSemana;
import negocio.facade.jdbc.academico.Curso;

/**
 * Reponsável por manter os dados da entidade CursoTurno. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 * @see Curso
 */
public class CursoTurnoVO extends SuperVO {

    /**
	 * 
	 */
	private static final long serialVersionUID = -7078010716216419515L;
	private Integer curso;
    private CursoVO cursoVO;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Turno </code>.
     */
    private TurnoVO turno;
    private NomeTurnoCensoEnum nomeTurnoCenso;
    @ExcluirJsonAnnotation
    private DiaSemana diaSemanaAula;
    @ExcluirJsonAnnotation
    private NomeTurnoCensoEnum turnoAula;
    @ExcluirJsonAnnotation
    private Integer nrVagas;
    @ExcluirJsonAnnotation
    private GrupoDisciplinaProcSeletivoVO grupoDisciplinaProcSeletivo;
    @ExcluirJsonAnnotation
    private Date dataInicioProcSeletivoCurso;
    @ExcluirJsonAnnotation
    private Date dataFimProcSeletivoCurso;
    

    /**
     * Construtor padrão da classe <code>CursoTurno</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public CursoTurnoVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>CursoTurnoVO</code>. Todos os tipos de consistência de dados são e
     * devem ser implementadas neste método. São validações típicas: verificação
     * de campos obrigatórios, verificação de valores válidos para os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(CursoTurnoVO obj) throws ConsistirException {
        if ((obj.getTurno() == null) || (obj.getTurno().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo TURNO (Turnos) deve ser informado.");
        }
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
    }

    /**
     * Retorna o objeto da classe <code>Turno</code> relacionado com (
     * <code>CursoTurno</code>).
     */
    public TurnoVO getTurno() {
        if (turno == null) {
            turno = new TurnoVO();
        }
        return (turno);
    }

    /**
     * Define o objeto da classe <code>Turno</code> relacionado com (
     * <code>CursoTurno</code>).
     */
    public void setTurno(TurnoVO obj) {
        this.turno = obj;
    }

    public Integer getCurso() {
        return (curso);
    }

    public void setCurso(Integer curso) {
        this.curso = curso;
    }
    
    public NomeTurnoCensoEnum getNomeTurnoCenso() {
		return nomeTurnoCenso;
	}

	public void setNomeTurnoCenso(NomeTurnoCensoEnum nomeTurnoCenso) {
		this.nomeTurnoCenso = nomeTurnoCenso;
	}

	public CursoVO getCursoVO() {
		if(cursoVO == null) {
			cursoVO = new CursoVO();
		}
		return cursoVO;
	}

	public void setCursoVO(CursoVO cursoVO) {
		this.cursoVO = cursoVO;
	}

	public DiaSemana getDiaSemanaAula() {
		if(diaSemanaAula == null) {
			diaSemanaAula =  DiaSemana.NENHUM;
		}
		return diaSemanaAula;
	}

	public void setDiaSemanaAula(DiaSemana diaSemanaAula) {
		this.diaSemanaAula = diaSemanaAula;
	}

	public NomeTurnoCensoEnum getTurnoAula() {
		if(turnoAula == null) {
			turnoAula =  NomeTurnoCensoEnum.NENHUM;
		}
		return turnoAula;
	}

	public void setTurnoAula(NomeTurnoCensoEnum turnoAula) {
		this.turnoAula = turnoAula;
	}

	public Integer getNrVagas() {
		if(nrVagas == null) {
			nrVagas =  0;
		}
		return nrVagas;
	}

	public void setNrVagas(Integer nrVagas) {
		this.nrVagas = nrVagas;
	}


	 
	public Date getDataInicioProcSeletivoCurso() {
		return dataInicioProcSeletivoCurso;
	}

	public void setDataInicioProcSeletivoCurso(Date dataInicioProcSeletivoCurso) {
		this.dataInicioProcSeletivoCurso = dataInicioProcSeletivoCurso;
	}

	 
	public Date getDataFimProcSeletivoCurso() {
		return dataFimProcSeletivoCurso;
	}

	public void setDataFimProcSeletivoCurso(Date dataFimProcSeletivoCurso) {
		this.dataFimProcSeletivoCurso = dataFimProcSeletivoCurso;
	}
   
	 
	public GrupoDisciplinaProcSeletivoVO getGrupoDisciplinaProcSeletivo() {
		if (grupoDisciplinaProcSeletivo == null) {
			grupoDisciplinaProcSeletivo = new GrupoDisciplinaProcSeletivoVO();
		}
		return grupoDisciplinaProcSeletivo;
	}

	public void setGrupoDisciplinaProcSeletivo(GrupoDisciplinaProcSeletivoVO grupoDisciplinaProcSeletivo) {
		this.grupoDisciplinaProcSeletivo = grupoDisciplinaProcSeletivo;
	}
	
}
