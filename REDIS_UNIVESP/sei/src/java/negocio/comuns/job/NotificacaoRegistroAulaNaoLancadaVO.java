package negocio.comuns.job;

import java.util.Date;

import negocio.comuns.basico.PessoaVO;

public class NotificacaoRegistroAulaNaoLancadaVO {

	private String turma;
	private String disciplina;
	private PessoaVO professor;
	private String tipoNotificacao;
	private Date dataAula;
	
	public String getTurma() {
		if(turma == null){
			turma = "";
		}
		return turma;
	}
	public void setTurma(String turma) {
		this.turma = turma;
	}
	public String getDisciplina() {
		if(disciplina == null){
			disciplina = "";
		}
		return disciplina;
	}
	public void setDisciplina(String disciplina) {
		this.disciplina = disciplina;
	}
	public PessoaVO getProfessor() {
		if(professor == null){
			professor = new PessoaVO();
		}
		return professor;
	}
	public void setProfessor(PessoaVO professor) {
		this.professor = professor;
	}
	public String getTipoNotificacao() {
		if(tipoNotificacao == null){
			tipoNotificacao = "";
		}
		return tipoNotificacao;
	}
	public void setTipoNotificacao(String tipoNotificacao) {
		this.tipoNotificacao = tipoNotificacao;
	}
	public Date getDataAula() {
		if(dataAula == null){
			dataAula = new Date();
		}
		return dataAula;
	}
	public void setDataAula(Date dataAula) {
		this.dataAula = dataAula;
	}
	
	
}
