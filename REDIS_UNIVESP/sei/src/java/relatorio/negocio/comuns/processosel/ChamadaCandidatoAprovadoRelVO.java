package relatorio.negocio.comuns.processosel;

import java.io.Serializable;
import java.math.BigDecimal;

import negocio.comuns.arquitetura.SuperVO;

public class ChamadaCandidatoAprovadoRelVO extends SuperVO implements Serializable {
	
	private String nomeCandidato;
	private String telefoneRes;
	private String celular;
	private String cpf;
	private String dataNascimento;
	private String email;
	private BigDecimal mediaNotasProcSeletivo;
	private Integer numeroVaga;
	private Integer numeroAcertos;
	private Integer nrCandidatosMatriculados;
	private Integer nrVagasDisponiveis;
	private Integer qtdeCandidatosChamar;
	private Integer numeroInscricao;
	private Integer classificacao;
	private BigDecimal notaRedacao;	
	private String regimeAprovacao;
	private String curso;
	private String turno;
	private String unidadeEnsino;
	private Integer chamada;
	private Boolean matriculado;
	
	private static final long serialVersionUID = 1L;

	public ChamadaCandidatoAprovadoRelVO() {
		
	}

	public String getNomeCandidato() {
		if (nomeCandidato == null) {
			nomeCandidato = "";
		}
		return nomeCandidato;
	}

	public void setNomeCandidato(String nomeCandidato) {
		this.nomeCandidato = nomeCandidato;
	}

	public String getTelefoneRes() {
		if (telefoneRes == null) {
			telefoneRes = "";
		}
		return telefoneRes;
	}

	public void setTelefoneRes(String telefoneRes) {
		this.telefoneRes = telefoneRes;
	}

	public String getCelular() {
		if (celular == null) {
			celular = "";
		}
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public String getCpf() {
		if (cpf == null) {
			cpf = "";
		}
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getDataNascimento() {
		if (dataNascimento == null) {
			dataNascimento = "";
		}
		return dataNascimento;
	}

	public void setDataNascimento(String dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public BigDecimal getMediaNotasProcSeletivo() {
		if (mediaNotasProcSeletivo == null) {
			mediaNotasProcSeletivo = BigDecimal.ZERO;
		}
		return mediaNotasProcSeletivo;
	}

	public void setMediaNotasProcSeletivo(BigDecimal mediaNotasProcSeletivo) {
		this.mediaNotasProcSeletivo = mediaNotasProcSeletivo;
	}

	public String getEmail() {
		if (email == null) {
			email = "";
		}
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getNumeroVaga() {
		if (numeroVaga == null) {
			numeroVaga = 0;
		}
		return numeroVaga;
	}

	public void setNumeroVaga(Integer numeroVaga) {
		this.numeroVaga = numeroVaga;
	}

	public Integer getNrCandidatosMatriculados() {
		if (nrCandidatosMatriculados == null) {
			nrCandidatosMatriculados = 0;
		}
		return nrCandidatosMatriculados;
	}

	public void setNrCandidatosMatriculados(Integer nrCandidatosMatriculados) {
		this.nrCandidatosMatriculados = nrCandidatosMatriculados;
	}

	public Integer getNrVagasDisponiveis() {
		if (nrVagasDisponiveis == null) {
			nrVagasDisponiveis = 0;
		}
		return nrVagasDisponiveis;
	}

	public void setNrVagasDisponiveis(Integer nrVagasDisponiveis) {
		this.nrVagasDisponiveis = nrVagasDisponiveis;
	}

	public Integer getQtdeCandidatosChamar() {
		if (qtdeCandidatosChamar == null) {
			qtdeCandidatosChamar = 0;
		}
		return qtdeCandidatosChamar;
	}

	public void setQtdeCandidatosChamar(Integer qtdeCandidatosChamar) {
		this.qtdeCandidatosChamar = qtdeCandidatosChamar;
	}

	public Integer getNumeroInscricao() {
		if (numeroInscricao == null) {
			numeroInscricao = 0;
		}
		return numeroInscricao;
	}

	public void setNumeroInscricao(Integer numeroInscricao) {
		this.numeroInscricao = numeroInscricao;
	}

	public Integer getClassificacao() {
		if(classificacao == null){
			classificacao = 0;
		}
		return classificacao;
	}

	public void setClassificacao(Integer classificacao) {
		this.classificacao = classificacao;
	}

	public BigDecimal getNotaRedacao() {
		if(notaRedacao == null){
			notaRedacao = BigDecimal.ZERO;
		}
		return notaRedacao;
	}

	public void setNotaRedacao(BigDecimal notaRedacao) {
		this.notaRedacao = notaRedacao;
	}

	public String getRegimeAprovacao() {
		if(regimeAprovacao == null){
			regimeAprovacao = "";
		}
		return regimeAprovacao;
	}

	public void setRegimeAprovacao(String regimeAprovacao) {
		this.regimeAprovacao = regimeAprovacao;
	}

	public String getCurso() {
		if(turno == null){
			turno = "";
		}
		return curso;
	}

	public void setCurso(String curso) {
		this.curso = curso;
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

	public String getUnidadeEnsino() {
		if(unidadeEnsino == null){
			unidadeEnsino = "";
		}
		return unidadeEnsino;
	}

	public void setUnidadeEnsino(String unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}

	public Integer getNumeroAcertos() {
		if(numeroAcertos == null){
			numeroAcertos = 0;
		}
		return numeroAcertos;
	}

	public void setNumeroAcertos(Integer numeroAcertos) {
		this.numeroAcertos = numeroAcertos;
	}
	
	public boolean getIsApresentarNotaRedacao(){		
		return getRegimeAprovacao().equals("quantidadeAcertosRedacao");
	}
	
	public boolean getApresentarAcertos(){		
		return getRegimeAprovacao().equals("quantidadeAcertosRedacao") || getRegimeAprovacao().equals("quantidadeAcertos");
	}

	public Integer getChamada() {
		if (chamada == null) {
			chamada = 0;
		}
		return chamada;
	}

	public void setChamada(Integer chamada) {
		this.chamada = chamada;
	}

	public Boolean getMatriculado() {
		if (matriculado == null) {
			matriculado = false;
		}
		return matriculado;
	}

	public void setMatriculado(Boolean matriculado) {
		this.matriculado = matriculado;
	}


}
