package negocio.comuns.academico;

import java.util.Date;

import negocio.comuns.academico.enumeradores.SituacaoRelatorioFacilitadorEnum;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.Uteis;

public class RelatorioFinalFacilitadorVO extends SuperVO {

	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private Integer codigo;
	private MatriculaPeriodoTurmaDisciplinaVO matriculaperiodoturmadisciplinaVO;
	private String ano;
	private String semestre;
	private String mes;
	private QuestionarioRespostaOrigemVO questionarioRespostaOrigemVO;
	private SituacaoRelatorioFacilitadorEnum situacao;
	private Date dataEnvioAnalise;
	private Date dataEnvioCorrecao;
	private Date dataDeferimento;
	private Date dataIndeferimento;
	private Double nota;
	private PessoaVO supervisor;
	private String motivo;
	private Integer totalizadorNaoEnviouRelatorio;
	private Integer totalizadorEmAnalise;
	private Integer totalizadorCorrecaoAluno;
	private Integer totalizadorDeferido;
	private Integer totalizadorIndeferido;
	private Integer totalizadorSuspensaoBolsa;
	private String nomeResponsavel;
	private Date dataResponsavel;
	private String idSalaAulaBlackBoard;

	public Integer getCodigo() {
		if (this.codigo == null) {
			this.codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public MatriculaPeriodoTurmaDisciplinaVO getMatriculaperiodoturmadisciplinaVO() {
		if(matriculaperiodoturmadisciplinaVO == null) {
			matriculaperiodoturmadisciplinaVO = new MatriculaPeriodoTurmaDisciplinaVO();
		}
		return matriculaperiodoturmadisciplinaVO;
	}

	public void setMatriculaperiodoturmadisciplinaVO(MatriculaPeriodoTurmaDisciplinaVO matriculaperiodoturmadisciplinaVO) {
		this.matriculaperiodoturmadisciplinaVO = matriculaperiodoturmadisciplinaVO;
	}

	public String getAno() {
		if(ano == null) {
			ano = "";
		}

		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getSemestre() {
		if(semestre == null) {
			semestre = "";
		}
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	public String getMes() {
		if(mes == null) {
			mes = "";
		}
		return mes;
	}

	public void setMes(String mes) {
		this.mes = mes;
	}

	public QuestionarioRespostaOrigemVO getQuestionarioRespostaOrigemVO() {
		if(questionarioRespostaOrigemVO == null) {
			questionarioRespostaOrigemVO = new QuestionarioRespostaOrigemVO();
		}
		return questionarioRespostaOrigemVO;
	}

	public void setQuestionarioRespostaOrigemVO(QuestionarioRespostaOrigemVO questionarioRespostaOrigemVO) {
		this.questionarioRespostaOrigemVO = questionarioRespostaOrigemVO;
	}

	public SituacaoRelatorioFacilitadorEnum getSituacao() {
		if(situacao == null) {
			situacao = SituacaoRelatorioFacilitadorEnum.NENHUM;
		}
		return situacao;
	}

	public void setSituacao(SituacaoRelatorioFacilitadorEnum situacao) {
		this.situacao = situacao;
	}

	public Date getDataEnvioAnalise() {
		if(dataEnvioAnalise == null) {
			dataEnvioAnalise = new Date();
		}
		return dataEnvioAnalise;
	}

	public String getDataEnvioAnaliseApresentar() {
		return Uteis.getData(getDataEnvioAnalise());
	}
	
	public void setDataEnvioAnalise(Date dataEnvioAnalise) {
		this.dataEnvioAnalise = dataEnvioAnalise;
	}

	public Date getDataEnvioCorrecao() {
		return dataEnvioCorrecao;
	}

	public void setDataEnvioCorrecao(Date dataEnvioCorrecao) {
		this.dataEnvioCorrecao = dataEnvioCorrecao;
	}

	public Date getDataDeferimento() {
		return dataDeferimento;
	}

	public void setDataDeferimento(Date dataDeferimento) {
		this.dataDeferimento = dataDeferimento;
	}

	public Date getDataIndeferimento() {
		return dataIndeferimento;
	}

	public void setDataIndeferimento(Date dataIndeferimento) {
		this.dataIndeferimento = dataIndeferimento;
	}

	public Double getNota() {
		return nota;
	}
	
	public String getNotaApresentar() {
		if(nota == null) {
			return "--";
		}
		return nota.toString();
	}

	public void setNota(Double nota) {
		this.nota = nota;
	}

	public PessoaVO getSupervisor() {
		if(supervisor == null) {
			supervisor = new PessoaVO();
		}
		return supervisor;
	}

	public void setSupervisor(PessoaVO supervisor) {
		this.supervisor = supervisor;
	}

	public Integer getTotalizadorNaoEnviouRelatorio() {
		if (totalizadorNaoEnviouRelatorio == null) {
			totalizadorNaoEnviouRelatorio = 0;
		}
		return totalizadorNaoEnviouRelatorio;
	}

	public void setTotalizadorNaoEnviouRelatorio(Integer totalizadorNaoEnviouRelatorio) {
		this.totalizadorNaoEnviouRelatorio = totalizadorNaoEnviouRelatorio;
	}

	public Integer getTotalizadorEmAnalise() {
		if (totalizadorEmAnalise == null) {
			totalizadorEmAnalise = 0;
		}
		return totalizadorEmAnalise;
	}

	public void setTotalizadorEmAnalise(Integer totalizadorEmAnalise) {
		this.totalizadorEmAnalise = totalizadorEmAnalise;
	}

	public Integer getTotalizadorCorrecaoAluno() {
		if (totalizadorCorrecaoAluno == null) {
			totalizadorCorrecaoAluno = 0;
		}
		return totalizadorCorrecaoAluno;
	}

	public void setTotalizadorCorrecaoAluno(Integer totalizadorCorrecaoAluno) {
		this.totalizadorCorrecaoAluno = totalizadorCorrecaoAluno;
	}

	public Integer getTotalizadorDeferido() {
		if (totalizadorDeferido == null) {
			totalizadorDeferido = 0;
		}
		return totalizadorDeferido;
	}

	public void setTotalizadorDeferido(Integer totalizadorDeferido) {
		this.totalizadorDeferido = totalizadorDeferido;
	}

	public Integer getTotalizadorIndeferido() {
		if (totalizadorIndeferido == null) {
			totalizadorIndeferido = 0;
		}
		return totalizadorIndeferido;
	}

	public void setTotalizadorIndeferido(Integer totalizadorIndeferido) {
		this.totalizadorIndeferido = totalizadorIndeferido;
	}

	public Integer getTotalizadorSuspensaoBolsa() {
		if (totalizadorSuspensaoBolsa == null) {
			totalizadorSuspensaoBolsa = 0;
		}
		return totalizadorSuspensaoBolsa;
	}

	public void setTotalizadorSuspensaoBolsa(Integer totalizadorSuspensaoBolsa) {
		this.totalizadorSuspensaoBolsa = totalizadorSuspensaoBolsa;
	}

	public String getMotivo() {
		if(motivo == null) {
			motivo = "";
		}
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}


	public String getNomeResponsavel() {
		return nomeResponsavel;
	}

	public void setNomeResponsavel(String nomeResponsavel) {
		this.nomeResponsavel = nomeResponsavel;
	}

	public Date getDataResponsavel() {
		return dataResponsavel;
	}

	public String getDataResponsavel_Apresentar() {
		if(dataResponsavel == null) {
			return "";
		}
		return Uteis.getData(dataResponsavel);
	}
	
	public void setDataResponsavel(Date dataResponsavel) {
		this.dataResponsavel = dataResponsavel;
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
}