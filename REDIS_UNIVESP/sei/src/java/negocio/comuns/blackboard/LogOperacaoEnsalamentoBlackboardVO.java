package negocio.comuns.blackboard;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.blackboard.enumeradores.OperacaoEnsalacaoBlackboardEnum;
import negocio.comuns.blackboard.enumeradores.TipoSalaAulaBlackboardEnum;
import negocio.comuns.blackboard.enumeradores.TipoSalaAulaBlackboardPessoaEnum;

public class LogOperacaoEnsalamentoBlackboardVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -289881404555264671L;
	
	private Integer codigo;
	private Integer codigoDisciplina;
	private String nomeDisciplina;
	private String abreviaturaDisciplina;
	private String matricula;
	private TipoSalaAulaBlackboardPessoaEnum tipoSalaAulaBlackboardPessoa;
	private String pessoa;
	private String emailInstitucional;
	private Integer codigoSalaAulaBlackBoard;
	private String idSalaAulaBlackBoard;
	private TipoSalaAulaBlackboardEnum tipoSalaAulaBlackboard;
	private String salaAulaBlackBoard;
	private String idGrupoBlackBoard;
	private String grupoBlackBoard;
	private OperacaoEnsalacaoBlackboardEnum operacaoEnsalacaoBlackboard;	
	private String observacao;
	private String ano;
	private String semestre;
	
	public Integer getCodigoDisciplina() {
		if(codigoDisciplina == null) {
			codigoDisciplina =  0;
		}
		return codigoDisciplina;
	}
	public void setCodigoDisciplina(Integer codigoDisciplina) {
		this.codigoDisciplina = codigoDisciplina;
	}
	public String getNomeDisciplina() {
		if(nomeDisciplina == null) {
			nomeDisciplina =  "";
		}
		return nomeDisciplina;
	}
	public void setNomeDisciplina(String nomeDisciplina) {
		this.nomeDisciplina = nomeDisciplina;
	}
	public String getAbreviaturaDisciplina() {
		if(abreviaturaDisciplina == null) {
			abreviaturaDisciplina =  "";
		}
		return abreviaturaDisciplina;
	}
	public void setAbreviaturaDisciplina(String abreviaturaDisciplina) {
		this.abreviaturaDisciplina = abreviaturaDisciplina;
	}
	public String getMatricula() {
		if(matricula == null) {
			matricula =  "";
		}
		return matricula;
	}
	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}
	public TipoSalaAulaBlackboardPessoaEnum getTipoSalaAulaBlackboardPessoa() {
		if(tipoSalaAulaBlackboardPessoa == null) {
			tipoSalaAulaBlackboardPessoa =  TipoSalaAulaBlackboardPessoaEnum.ALUNO;
		}
		return tipoSalaAulaBlackboardPessoa;
	}
	public void setTipoSalaAulaBlackboardPessoa(TipoSalaAulaBlackboardPessoaEnum tipoSalaAulaBlackboardPessoa) {
		this.tipoSalaAulaBlackboardPessoa = tipoSalaAulaBlackboardPessoa;
	}
	public String getPessoa() {
		if(pessoa == null) {
			pessoa =  "";
		}
		return pessoa;
	}
	public void setPessoa(String pessoa) {
		this.pessoa = pessoa;
	}
	public String getEmailInstitucional() {
		if(emailInstitucional == null) {
			emailInstitucional =  "";
		}
		return emailInstitucional;
	}
	public void setEmailInstitucional(String emailInstitucional) {
		this.emailInstitucional = emailInstitucional;
	}
	public Integer getCodigoSalaAulaBlackBoard() {
		if(codigoSalaAulaBlackBoard == null) {
			codigoSalaAulaBlackBoard =  0;
		}
		return codigoSalaAulaBlackBoard;
	}
	public void setCodigoSalaAulaBlackBoard(Integer codigoSalaAulaBlackBoard) {
		this.codigoSalaAulaBlackBoard = codigoSalaAulaBlackBoard;
	}
	public TipoSalaAulaBlackboardEnum getTipoSalaAulaBlackboard() {
		if(tipoSalaAulaBlackboard == null) {
			tipoSalaAulaBlackboard =  TipoSalaAulaBlackboardEnum.DISCIPLINA;
		}
		return tipoSalaAulaBlackboard;
	}
	public void setTipoSalaAulaBlackboard(TipoSalaAulaBlackboardEnum tipoSalaAulaBlackboard) {
		this.tipoSalaAulaBlackboard = tipoSalaAulaBlackboard;
	}
	public String getSalaAulaBlackBoard() {
		if(salaAulaBlackBoard == null) {
			salaAulaBlackBoard =  "";
		}
		return salaAulaBlackBoard;
	}
	public void setSalaAulaBlackBoard(String salaAulaBlackBoard) {
		this.salaAulaBlackBoard = salaAulaBlackBoard;
	}
	public String getGrupoBlackBoard() {
		if(grupoBlackBoard == null) {
			grupoBlackBoard =  "";
		}
		return grupoBlackBoard;
	}
	public void setGrupoBlackBoard(String grupoBlackBoard) {
		this.grupoBlackBoard = grupoBlackBoard;
	}
	public OperacaoEnsalacaoBlackboardEnum getOperacaoEnsalacaoBlackboard() {
		return operacaoEnsalacaoBlackboard;
	}
	public void setOperacaoEnsalacaoBlackboard(OperacaoEnsalacaoBlackboardEnum operacaoEnsalacaoBlackboard) {
		this.operacaoEnsalacaoBlackboard = operacaoEnsalacaoBlackboard;
	}
	public String getObservacao() {
		if(observacao == null) {
			observacao =  "";
		}
		return observacao;
	}
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	public String getAno() {
		if(ano == null) {
			ano =  "";
		}
		return ano;
	}
	public void setAno(String ano) {
		this.ano = ano;
	}
	public String getSemestre() {
		if(semestre == null) {
			semestre =  "";
		}
		return semestre;
	}
	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}
	public String getIdSalaAulaBlackBoard() {
		if(idSalaAulaBlackBoard == null) {
			idSalaAulaBlackBoard = "";
		}
		return idSalaAulaBlackBoard;
	}
	public void setIdSalaAulaBlackBoard(String idSalaAulaBlackBoard) {
		this.idSalaAulaBlackBoard = idSalaAulaBlackBoard;
	}
	public String getIdGrupoBlackBoard() {
		if(idGrupoBlackBoard == null) {
			idGrupoBlackBoard = "";
		}
		return idGrupoBlackBoard;
	}
	public void setIdGrupoBlackBoard(String idGrupoBlackBoard) {
		this.idGrupoBlackBoard = idGrupoBlackBoard;
	}
	public Integer getCodigo() {
		if(codigo == null) {
			codigo =  0;
		}
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	
	
	
}
