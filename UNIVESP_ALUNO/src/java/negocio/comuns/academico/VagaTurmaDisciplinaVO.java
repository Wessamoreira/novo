package negocio.comuns.academico;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.facade.jdbc.academico.Turma;

/**
 * Reponsável por manter os dados da entidade TurmaDisciplina. Classe do tipo VO
 * - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 * @see Turma
 */
public class VagaTurmaDisciplinaVO extends SuperVO {

	private Integer codigo;
	private Integer vagaTurma;
	private DisciplinaVO disciplina;
	private Integer nrVagasMatricula;
	private Integer nrVagasMatriculaReposicao;
	private Integer nrMaximoMatricula;
	private Boolean disciplinaEquivalente;
	public static final long serialVersionUID = 1L;

	/**
	 * Construtor padrão da classe <code>TurmaDisciplina</code>. Cria uma nova
	 * instância desta entidade, inicializando automaticamente seus atributos
	 * (Classe VO).
	 */
	public VagaTurmaDisciplinaVO() {
		super();
		inicializarDados();
	}

	/**
	 * Operação responsável por validar os dados de um objeto da classe
	 * <code>TurmaDisciplinaVO</code>. Todos os tipos de consistência de dados
	 * são e devem ser implementadas neste método. São validações típicas:
	 * verificação de campos obrigatórios, verificação de valores válidos para
	 * os atributos.
	 * 
	 * @exception ConsistirExecption
	 *                Se uma inconsistência for encontrada aumaticamente é
	 *                gerada uma exceção descrevendo o atributo e o erro
	 *                ocorrido.
	 */
	public static void validarDados(VagaTurmaDisciplinaVO obj) throws ConsistirException {
		if (!obj.isValidarDados().booleanValue()) {
			return;
		}
		if (obj.getDisciplina() == null || obj.getDisciplina().getCodigo().intValue() == 0) {
			throw new ConsistirException("O campo DISCIPLINA (Turma Disciplina) deve ser informado.");
		}
		 if(obj.getNrVagasMatricula() > 0 && obj.getNrVagasMatricula() > obj.getNrMaximoMatricula()) {
       	  throw new ConsistirException("O campo NÚMERO DE VAGAS ("+obj.getDisciplina().getNome()+") não pode ser maior que o campo NÚMERO MÁXIMO MATRÍCULA.");
       }
	}

	/**
	 * Operação reponsável por inicializar os atributos da classe.
	 */
	public void inicializarDados() {
		setCodigo(0);
		setNrMaximoMatricula(0);
		setNrVagasMatricula(0);
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

	public Integer getNrMaximoMatricula() {
		if (nrMaximoMatricula == null) {
			nrMaximoMatricula = 0;
		}
		return nrMaximoMatricula;
	}

	public void setNrMaximoMatricula(Integer nrMaximoMatricula) {
		this.nrMaximoMatricula = nrMaximoMatricula;
	}

	public Integer getNrVagasMatricula() {
		if (nrVagasMatricula == null) {
			nrVagasMatricula = 0;
		}
		return nrVagasMatricula;
	}

	public void setNrVagasMatricula(Integer nrVagasMatricula) {
		this.nrVagasMatricula = nrVagasMatricula;
	}

	public Integer getVagaTurma() {
		if (vagaTurma == null) {
			vagaTurma = 0;
		}
		return (vagaTurma);
	}

	public void setVagaTurma(Integer vagaTurma) {
		this.vagaTurma = vagaTurma;
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

	@Override
	public String toString() {
		return "TurmaDisciplinaVO [codigo=" + codigo + "]" + "DisciplinaVO " + disciplina.toString();
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getDisciplina().getCodigo() == null) ? 0 : getDisciplina().getCodigo().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VagaTurmaDisciplinaVO other = (VagaTurmaDisciplinaVO) obj;
		if (getDisciplina().getCodigo() == null) {
			if (other.getDisciplina().getCodigo() != null)
				return false;
		} else if (!getDisciplina().getCodigo().equals(other.getDisciplina().getCodigo()))
			return false;
		return true;
	}

	/**
	 * NÇÃO USAR SINGLETON
	 * @return
	 */
	public Integer getNrVagasMatriculaReposicao() {		
		return nrVagasMatriculaReposicao;
	}

	public void setNrVagasMatriculaReposicao(Integer nrVagasMatriculaReposicao) {
		this.nrVagasMatriculaReposicao = nrVagasMatriculaReposicao;
	}

	public Boolean getDisciplinaEquivalente() {
		if (disciplinaEquivalente == null) {
			disciplinaEquivalente = false;
		}
		return disciplinaEquivalente;
	}

	public void setDisciplinaEquivalente(Boolean disciplinaEquivalente) {
		this.disciplinaEquivalente = disciplinaEquivalente;
	}

	
}
