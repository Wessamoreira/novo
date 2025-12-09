package negocio.comuns.academico;

import java.sql.Timestamp;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;

/**
 * tipo VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class RegistroEntregaArtefatoAlunoVO extends SuperVO {

	private Integer codigo;
	private Timestamp data;
	private UsuarioVO usuario;
	private MatriculaPeriodoVO matriculaPeriodo;
	private ArtefatoEntregaAlunoVO artefatoEntregaAluno;
	private String situacao;
	private DisciplinaVO disciplina;
	

	private String turma;
	private String matricula;
	private String aluno;
	private Boolean entregue;
	

	public static final long serialVersionUID = 1L;

	/**
	 * Construtor padrão da classe <code>RegistroEntregaArtefatoAlunoVO</code>. Cria uma
	 * nova instância desta entidade, inicializando automaticamente seus atributos
	 * (Classe VO).
	 */
	public RegistroEntregaArtefatoAlunoVO() {
		super();
	}

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	
	public Timestamp getData() {
		return data;
	}

	public void setData(Timestamp data) {
		this.data = data;
	}

	public UsuarioVO getUsuario() {
		if (usuario == null) {
			usuario = new UsuarioVO();
		}
		return usuario;
	}

	public void setUsuario(UsuarioVO usuario) {
		this.usuario = usuario;
	}

	public MatriculaPeriodoVO getMatriculaPeriodo() {
		if (matriculaPeriodo == null) {
			matriculaPeriodo = new MatriculaPeriodoVO();
		}
		return matriculaPeriodo;
	}

	public void setMatriculaPeriodo(MatriculaPeriodoVO matriculaPeriodo) {
		this.matriculaPeriodo = matriculaPeriodo;
	}

	public ArtefatoEntregaAlunoVO getArtefatoEntregaAluno() {
		if (artefatoEntregaAluno == null) {
			artefatoEntregaAluno = new ArtefatoEntregaAlunoVO();
		}
		return artefatoEntregaAluno;
	}

	public void setArtefatoEntregaAluno(ArtefatoEntregaAlunoVO artefatoEntregaAluno) {
		this.artefatoEntregaAluno = artefatoEntregaAluno;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
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

	public String getTurma() {
		return turma;
	}

	public void setTurma(String turma) {
		this.turma = turma;
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public String getAluno() {
		return aluno;
	}

	public void setAluno(String aluno) {
		this.aluno = aluno;
	}

	public Boolean getEntregue() {
		if(entregue == null) {
			entregue = Boolean.FALSE;
		}
		return entregue;
	}

	public void setEntregue(Boolean entregue) {
		this.entregue = entregue;
	}

}
