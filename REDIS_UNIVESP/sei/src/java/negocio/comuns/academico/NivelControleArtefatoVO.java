/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.academico;

import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;

/**
 *
 * @author Ana Claudia
 */
public class NivelControleArtefatoVO extends SuperVO {

	private UnidadeEnsinoVO unidadeEnsino;
	private CursoVO curso;
	private DisciplinaVO disciplina;
	private FuncionarioVO funcionario;
	private ArtefatoEntregaAlunoVO artefatoEntregaAluno;
	private Integer codigo;
	private String tipo;

	public static final long serialVersionUID = 1L;

	public UnidadeEnsinoVO getUnidadeEnsino() {
		if (unidadeEnsino == null) {
			unidadeEnsino = new UnidadeEnsinoVO();
		}
		return unidadeEnsino;
	}

	public void setUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}

	public CursoVO getCurso() {
		if (curso == null) {
			curso = new CursoVO();
		}
		return curso;
	}

	public void setCurso(CursoVO curso) {
		this.curso = curso;
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

	public FuncionarioVO getFuncionario() {
		if (funcionario == null) {
			funcionario = new FuncionarioVO();
		}
		return funcionario;
	}

	public void setFuncionario(FuncionarioVO funcionario) {
		this.funcionario = funcionario;
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

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
}
