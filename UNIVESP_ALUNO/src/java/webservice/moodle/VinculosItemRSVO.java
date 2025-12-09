package webservice.moodle;

import java.sql.Timestamp;
import java.util.Date;

import com.google.gson.annotations.SerializedName;

import negocio.comuns.utilitarias.Uteis;

public class VinculosItemRSVO {
	
	@SerializedName("cpf")
	private String cpf;
	@SerializedName("curso_codigo")
	private Integer codigoCurso;
	@SerializedName("turma_codigo")
	private Integer codigoTurma;
	@SerializedName("modulo_codigo")
	private Integer codigoModulo;

	@SerializedName("vinculo_situacao_codigo")
	private Integer codigoSituacao;	
	@SerializedName("vinculo_situacao_nome")
	private String situacaoMatricula;
	
	@SerializedName("vinculo_inicio")
	private Long vinculoInicio;
	@SerializedName("confirmacao_matricula")
	private Long confirmacaoMatricula;
	@SerializedName("vinculo_fim")
	private Long vinculoFim;
	
	@SerializedName("curso_codigoorigem")
	private Integer cursoCodigoOrigem;
	@SerializedName("codigo_reposicao")
	private Integer codigoReposicao;
	@SerializedName("vinculo_periodoLetivo")
	private Integer periodoLetivo;
	@SerializedName("vinculo_ano")
	private String ano;
	@SerializedName("vinculo_semestre")
	private String semestre;
	
	private transient Timestamp dataConfirmacao;
	private transient Timestamp dataInicio;
	private transient Timestamp dataFim;
	
	public String getCpf() {
		if (cpf == null)
			cpf = "";
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	public Integer getCodigoCurso() {
		if (codigoCurso == null)
			codigoCurso = 0;
		return codigoCurso;
	}
	public void setCodigoCurso(Integer codigoCurso) {
		this.codigoCurso = codigoCurso;
	}
	public Integer getCodigoTurma() {
		if (codigoTurma == null)
			codigoTurma = 0;
		return codigoTurma;
	}
	public void setCodigoTurma(Integer codigoTurma) {
		this.codigoTurma = codigoTurma;
	}
	public Integer getCodigoModulo() {
		if (codigoModulo == null)
			codigoModulo = 0;
		return codigoModulo;
	}
	public void setCodigoModulo(Integer codigoModulo) {
		this.codigoModulo = codigoModulo;
	}
	public Long getVinculoInicio() {
		if (vinculoInicio == null)
			vinculoInicio = 0l;
		return vinculoInicio;
	}
	public void setVinculoInicio(Long vinculoInicio) {
		this.vinculoInicio = vinculoInicio;
	}
	public Long getConfirmacaoMatricula() {
		if (confirmacaoMatricula == null)
			confirmacaoMatricula = 0l;
		return confirmacaoMatricula;
	}
	public void setConfirmacaoMatricula(Long confirmacaoMatricula) {
		this.confirmacaoMatricula = confirmacaoMatricula;
	}
	public Long getVinculoFim() {
		if (vinculoFim == null)
			vinculoFim = 0l;
		return vinculoFim;
	}
	public void setVinculoFim(Long vinculoFim) {
		this.vinculoFim = vinculoFim;
	}
	public Timestamp getDataInicio() {
		if (dataInicio == null)
			dataInicio = Uteis.getDataJDBCTimestamp(new Date());
		return dataInicio;
	}
	public void setDataInicio(Timestamp dataInicio) {
		this.dataInicio = dataInicio;
	}
	public Timestamp getDataConfirmacao() {
		if (dataConfirmacao == null)
			dataConfirmacao = Uteis.getDataJDBCTimestamp(new Date());
		return dataConfirmacao;
	}
	public void setDataConfirmacao(Timestamp dataConfirmacao) {
		this.dataConfirmacao = dataConfirmacao;
	}
	public Timestamp getDataFim() {
//		if (dataFim == null)
//			dataFim = Uteis.getDataJDBCTimestamp(new Date());
		return dataFim;
	}
	public void setDataFim(Timestamp dataFim) {
		this.dataFim = dataFim;
	}
	
	public Integer getCodigoSituacao() {
		if (codigoSituacao == null) {
			codigoSituacao = 0;
		}
		return codigoSituacao;
	}
	
	public void setCodigoSituacao(Integer codigoSituacao) {
		this.codigoSituacao = codigoSituacao;
	}
	
	public String getSituacaoMatricula() {
		if (situacaoMatricula == null) {
			situacaoMatricula = "";
		}
		return situacaoMatricula;
	}
	
	public void setSituacaoMatricula(String situacaoMatricula) {
		this.situacaoMatricula = situacaoMatricula;
	}
	public Integer getCursoCodigoOrigem() {
		if(cursoCodigoOrigem == null) {
			cursoCodigoOrigem = 0;
		}
		return cursoCodigoOrigem;
	}
	public void setCursoCodigoOrigem(Integer cursoCodigoOrigem) {
		this.cursoCodigoOrigem = cursoCodigoOrigem;
	}
	public Integer getCodigoReposicao() {
		if(codigoReposicao == null) {
			codigoReposicao = 0;
		}
		return codigoReposicao;
	}
	
	public void setCodigoReposicao(Integer codigoReposicao) {
		this.codigoReposicao = codigoReposicao;
	}
	
	public Integer getPeriodoLetivo() {
		if (periodoLetivo == null) {
			periodoLetivo = 0;
		}
		return periodoLetivo;
	}

	public void setPeriodoLetivo(Integer periodoLetivo) {
		this.periodoLetivo = periodoLetivo;
	}

	public String getAno() {
		if (ano == null) {
			ano = "";
		}
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getSemestre() {
		if (semestre == null) {
			semestre = "";
		}
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}
}