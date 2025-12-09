package relatorio.negocio.comuns.avaliacaoInst;

import java.io.Serializable;

import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;

public class AvaliacaoInstitucionalAnaliticoRelVO  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4939095387070121913L;
	private Integer aluno;
	private String nome;
	private String telefone;
	private String email;
	private String matricula;
	private String curso;
	private String turno;
	private String turma;	
	private Boolean jaRespondeu;
	private Boolean enviarEmail;
	// usados para avaliação de ultimo módulo
	private String identificadorTurma;	
	private String disciplina;	
	private UsuarioVO usuarioVO;
	private MatriculaVO matriculaVO;
	private String nomeUnidadeEnsino;

	
	public Integer getAluno() {
		if(aluno == null){
			aluno = 0;
		}
		return aluno;
	}
	public void setAluno(Integer aluno) {
		this.aluno = aluno;
	}
	public String getNome() {
		if(nome == null){
			nome = "";
		}
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getTelefone() {
		if(telefone == null){
			telefone = "";
		}
		return telefone;
	}
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	public String getEmail() {
		if(email == null){
			email = "";
		}
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMatricula() {
		if(matricula == null){
			matricula = "";
		}
		return matricula;
	}
	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}
	public String getCurso() {
		if(curso == null){
			curso = "";
		}
		return curso;
	}
	public void setCurso(String curso) {
		this.curso = curso;
	}
	public String getTurma() {
		if(turma == null){
			turma = "";
		}
		return turma;
	}
	public void setTurma(String turma) {
		this.turma = turma;
	}
	public Boolean getJaRespondeu() {
		if(jaRespondeu == null){
			jaRespondeu = false;
		}
		return jaRespondeu;
	}
	public void setJaRespondeu(Boolean jaRespondeu) {
		this.jaRespondeu = jaRespondeu;
	}
	public Boolean getEnviarEmail() {
		if(enviarEmail == null){
			enviarEmail = false;
		}
		return enviarEmail;
	}
	public void setEnviarEmail(Boolean enviarEmail) {
		this.enviarEmail = enviarEmail;
	}
	public String getTurno() {
		if(turno == null){
			turno = "";
		}			
		return turno;
	}
	public void setTurno(String turno) {
		this.turno = turno;
	}
	public String getIdentificadorTurma() {
		if (identificadorTurma == null) {
			identificadorTurma = "";
		}
		return identificadorTurma;
	}
	public void setIdentificadorTurma(String identificadorTurma) {
		this.identificadorTurma = identificadorTurma;
	}
	public String getDisciplina() {
		if (disciplina == null) {
			disciplina = "";
		}
		return disciplina;
	}
	public void setDisciplina(String disciplina) {
		this.disciplina = disciplina;
	}

	
	public UsuarioVO getUsuarioVO() {
		if(usuarioVO == null){
			usuarioVO = new UsuarioVO();
		}
		return usuarioVO;
	}
	
	public void setUsuarioVO(UsuarioVO usuarioVO) {
		this.usuarioVO = usuarioVO;
	}
	public MatriculaVO getMatriculaVO() {
		if(matriculaVO == null){
			matriculaVO = new MatriculaVO();
		}
		return matriculaVO;
	}
	public void setMatriculaVO(MatriculaVO matriculaVO) {
		this.matriculaVO = matriculaVO;
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
}
