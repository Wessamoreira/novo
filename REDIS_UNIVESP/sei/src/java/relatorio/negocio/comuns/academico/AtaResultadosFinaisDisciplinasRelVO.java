package relatorio.negocio.comuns.academico;

import java.util.Date;

import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoHistorico;

public class AtaResultadosFinaisDisciplinasRelVO {

	private String matriculaAluno;
	private Integer ordem;
	private Integer codigoDisciplina;
	private String nomeDisciplina;
	private String nomeAluno;
	private String mediaFinal;
	private String situacao;
	private String situacaoFinal;
	private String situacaoUltimaMatriculaPeriodo;
	private String freguencia;
	private String cargaHorariaDisciplina;
	private Integer numeroChamada;
	private Date dataTransferencia;
	private Integer ordemLinha;

	public String nomeAlunoSemAcentos;
	private String situacaoHistoricoCompleto;

	public String getNomeDisciplina() {
		if (nomeDisciplina == null) {
			nomeDisciplina = "";
		}
		return nomeDisciplina;
	}

	public void setNomeDisciplina(String nomeDisciplina) {
		this.nomeDisciplina = nomeDisciplina;
	}

	public String getNomeAluno() {
		if (nomeAluno == null) {
			nomeAluno = "";
		}
		return nomeAluno;
	}

	public void setNomeAluno(String nomeAluno) {
		this.nomeAluno = nomeAluno;
	}

	public String getMediaFinal() {
		if (mediaFinal == null) {
			mediaFinal = "";
		}
		return mediaFinal;
	}

	public void setMediaFinal(String mediaFinal) {
		this.mediaFinal = mediaFinal;
	}

	public String getSituacaoFinal() {
		if (situacaoFinal == null) {
			situacaoFinal = "";
		}
		return situacaoFinal;
	}

	public void setSituacaoFinal(String situacaoFinal) {
		this.situacaoFinal = situacaoFinal;
	}

	public String getMatriculaAluno() {
		if (matriculaAluno == null) {
			matriculaAluno = "";
		}
		return matriculaAluno;
	}

	public void setMatriculaAluno(String matriculaAluno) {
		this.matriculaAluno = matriculaAluno;
	}

	public String getSituacao() {
		if (situacao == null) {
			situacao = "";
		}
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public Integer getCodigoDisciplina() {
		if (codigoDisciplina == null) {
			codigoDisciplina = 0;
		}
		return codigoDisciplina;
	}

	public void setCodigoDisciplina(Integer codigoDisciplina) {
		this.codigoDisciplina = codigoDisciplina;
	}

	public String getSituacaoUltimaMatriculaPeriodo() {
		if (situacaoUltimaMatriculaPeriodo == null) {
			situacaoUltimaMatriculaPeriodo = "";
		}
		return situacaoUltimaMatriculaPeriodo;
	}

	public void setSituacaoUltimaMatriculaPeriodo(String situacaoUltimaMatriculaPeriodo) {
		this.situacaoUltimaMatriculaPeriodo = situacaoUltimaMatriculaPeriodo;
	}

	public String getFreguencia() {
		if (freguencia == null) {
			freguencia = "";
		}
		return freguencia;
	}

	public void setFreguencia(String freguencia) {
		this.freguencia = freguencia;
	}

	public String getCargaHorariaDisciplina() {
		if (cargaHorariaDisciplina == null) {
			cargaHorariaDisciplina = "";
		}
		return cargaHorariaDisciplina;
	}

	public void setCargaHorariaDisciplina(String cargaHorariaDisciplina) {
		this.cargaHorariaDisciplina = cargaHorariaDisciplina;
	}

	public Integer getNumeroChamada() {
		if (numeroChamada == null) {
			numeroChamada = 0;
		}
		return numeroChamada;
	}

	public void setNumeroChamada(Integer numeroChamada) {
		this.numeroChamada = numeroChamada;
	}

	public Date getDataTransferencia() {
		if (dataTransferencia == null) {
			dataTransferencia = new Date();
		}
		return dataTransferencia;
	}

	public void setDataTransferencia(Date dataTransferencia) {
		this.dataTransferencia = dataTransferencia;
	}

	public Integer getOrdem() {
		if (ordem == null) {
			ordem = 0;
		}
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	public String getNomeAlunoSemAcentos() {
		if (nomeAlunoSemAcentos == null) {
			nomeAlunoSemAcentos = Uteis.removerAcentuacao(getNomeAluno());
		}
		return nomeAlunoSemAcentos;
	}

	public String getSituacaoHistoricoCompleto() {
		if (!getSituacao().equals("")) {
			situacaoHistoricoCompleto = SituacaoHistorico.getDescricao(getSituacao());
		} else {
			situacaoHistoricoCompleto = "";
		}
		return situacaoHistoricoCompleto;
	}

	public void setSituacaoHistoricoCompleto(String situacaoHistoricoCompleto) {
		this.situacaoHistoricoCompleto = situacaoHistoricoCompleto;
	}

	public Integer getOrdemLinha() {
		if (ordemLinha == null) {
			ordemLinha = 0;
		}
		return ordemLinha;
	}

	public void setOrdemLinha(Integer ordemLinha) {
		this.ordemLinha = ordemLinha;
	}
	
	public String getAgrupadorDisciplinaNota() {
		return getCodigoDisciplina()+getNomeDisciplina()+getOrdem();
	}
}
