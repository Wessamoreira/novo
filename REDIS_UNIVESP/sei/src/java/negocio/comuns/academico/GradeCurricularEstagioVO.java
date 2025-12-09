package negocio.comuns.academico;

import negocio.comuns.academico.enumeradores.GradeCurricularEstagioAreaEnum;
import negocio.comuns.academico.enumeradores.GradeCurricularEstagioQuestionarioEnum;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.annotation.ExcluirJsonAnnotation;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.blackboard.SalaAulaBlackboardVO;
import negocio.comuns.processosel.QuestionarioVO;

public class GradeCurricularEstagioVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 86005125291794431L;
	private Integer codigo;
	@ExcluirJsonAnnotation
	private GradeCurricularVO gradeCurricularVO;
	private GradeCurricularEstagioAreaEnum gradeCurricularEstagioAreaEnum;
	private GradeCurricularEstagioQuestionarioEnum gradeCurricularEstagioQuestionarioEnum;
	private String nome;
	private Integer cargaHorarioObrigatorio;
	private Integer horaMaximaAproveitamentoOuEquivalencia;
	private boolean permiteHorasFragmentadas = false;
	@ExcluirJsonAnnotation
	private TextoPadraoDeclaracaoVO textoPadraoDeclaracaoVO;
	@ExcluirJsonAnnotation
	private QuestionarioVO questionarioRelatorioFinal;
	@ExcluirJsonAnnotation
	private QuestionarioVO questionarioAproveitamentoPorDocenteRegular;
	@ExcluirJsonAnnotation
	private QuestionarioVO questionarioAproveitamentoPorLicenciatura;
	@ExcluirJsonAnnotation
	private QuestionarioVO questionarioEquivalencia;
	/**
	 * transient
	 */
	@ExcluirJsonAnnotation
	private SalaAulaBlackboardVO salaAulaBlackboardVO;
	@ExcluirJsonAnnotation
	private Boolean filtrarGradeCurricularEstagioVO;
	private Integer totalCargaHorarioEstagio;
	private PessoaVO docenteResponsavelEstagio;
	private Boolean editar;
	

	/**
	 * transient para usar no historicorel
	 */	
	private PessoaVO docente;
	private String nomeDocente;
	private String titulacaoDocente;
	private Integer cargaHorariaAprovada;
	private Integer cargaHorariaEmRealizacao;
	private Integer cargaHorariaIndeferida;
	private Integer cargaHorariaPendente;
	private String situacao;
	private Integer percentual;
	private String anoBimestre;
	private Boolean informarSupervisorEstagio;
	
	
	public Integer getObterCargaHorariaAproveitamentoOuEquivalencia(Integer cargaHorarioExistente, Integer cargaHorarioEspecifica) {
		Integer cargaHorariaRestante  = getCargaHorarioObrigatorio() - cargaHorarioExistente;
		Integer cargaHorariaRestanteEspecifica  = getHoraMaximaAproveitamentoOuEquivalencia()- cargaHorarioEspecifica;
		if (cargaHorariaRestante > 0 && cargaHorariaRestanteEspecifica > 0 && cargaHorariaRestante >= cargaHorariaRestanteEspecifica ) {
			return cargaHorariaRestanteEspecifica ;
		} else if(cargaHorariaRestante > 0 && cargaHorariaRestanteEspecifica > 0 && cargaHorariaRestante < cargaHorariaRestanteEspecifica ){
			return cargaHorariaRestante;
		}  
		return 0;
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

	public GradeCurricularVO getGradeCurricularVO() {
		if (gradeCurricularVO == null) {
			gradeCurricularVO = new GradeCurricularVO();
		}
		return gradeCurricularVO;
	}

	public void setGradeCurricularVO(GradeCurricularVO gradeCurricularVO) {
		this.gradeCurricularVO = gradeCurricularVO;
	}

	public String getNome() {
		if (nome == null) {
			nome = "";
		}
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Integer getCargaHorarioObrigatorio() {
		if (cargaHorarioObrigatorio == null) {
			cargaHorarioObrigatorio = 0;
		}
		return cargaHorarioObrigatorio;
	}

	public void setCargaHorarioObrigatorio(Integer cargaHorarioObrigatorio) {
		this.cargaHorarioObrigatorio = cargaHorarioObrigatorio;
	}

	public Integer getHoraMaximaAproveitamentoOuEquivalencia() {
		if (horaMaximaAproveitamentoOuEquivalencia == null) {
			horaMaximaAproveitamentoOuEquivalencia = 0;
		}
		return horaMaximaAproveitamentoOuEquivalencia;
	}

	public void setHoraMaximaAproveitamentoOuEquivalencia(Integer horaMaximaAproveitamentoOuEquivalencia) {
		this.horaMaximaAproveitamentoOuEquivalencia = horaMaximaAproveitamentoOuEquivalencia;
	}

	public TextoPadraoDeclaracaoVO getTextoPadraoDeclaracaoVO() {
		if (textoPadraoDeclaracaoVO == null) {
			textoPadraoDeclaracaoVO = new TextoPadraoDeclaracaoVO();
		}
		return textoPadraoDeclaracaoVO;
	}

	public void setTextoPadraoDeclaracaoVO(TextoPadraoDeclaracaoVO textoPadraoDeclaracaoVO) {
		this.textoPadraoDeclaracaoVO = textoPadraoDeclaracaoVO;
	}

	public QuestionarioVO getQuestionarioRelatorioFinal() {
		if (questionarioRelatorioFinal == null) {
			questionarioRelatorioFinal = new QuestionarioVO();
		}
		return questionarioRelatorioFinal;
	}

	public void setQuestionarioRelatorioFinal(QuestionarioVO questionarioRelatoriofinal) {
		this.questionarioRelatorioFinal = questionarioRelatoriofinal;
	}

	public QuestionarioVO getQuestionarioAproveitamentoPorDocenteRegular() {
		if (questionarioAproveitamentoPorDocenteRegular == null) {
			questionarioAproveitamentoPorDocenteRegular = new QuestionarioVO();
		}
		return questionarioAproveitamentoPorDocenteRegular;
	}

	public void setQuestionarioAproveitamentoPorDocenteRegular(QuestionarioVO questionarioAproveitamentoPorDocenteRegular) {
		this.questionarioAproveitamentoPorDocenteRegular = questionarioAproveitamentoPorDocenteRegular;
	}

	public QuestionarioVO getQuestionarioAproveitamentoPorLicenciatura() {
		if (questionarioAproveitamentoPorLicenciatura == null) {
			questionarioAproveitamentoPorLicenciatura = new QuestionarioVO();
		}
		return questionarioAproveitamentoPorLicenciatura;
	}

	public void setQuestionarioAproveitamentoPorLicenciatura(QuestionarioVO questionarioAproveitamentoPorLicenciatura) {
		this.questionarioAproveitamentoPorLicenciatura = questionarioAproveitamentoPorLicenciatura;
	}

	public QuestionarioVO getQuestionarioEquivalencia() {
		if (questionarioEquivalencia == null) {
			questionarioEquivalencia = new QuestionarioVO();
		}
		return questionarioEquivalencia;
	}

	public void setQuestionarioEquivalencia(QuestionarioVO questionarioEquivalencia) {
		this.questionarioEquivalencia = questionarioEquivalencia;
	}

	public GradeCurricularEstagioAreaEnum getGradeCurricularEstagioAreaEnum() {
		if (gradeCurricularEstagioAreaEnum == null) {
			gradeCurricularEstagioAreaEnum = GradeCurricularEstagioAreaEnum.DOCENCIA;
		}
		return gradeCurricularEstagioAreaEnum;
	}

	public void setGradeCurricularEstagioAreaEnum(GradeCurricularEstagioAreaEnum gradeCurricularEstagioAreaEnum) {
		this.gradeCurricularEstagioAreaEnum = gradeCurricularEstagioAreaEnum;
	}

	public GradeCurricularEstagioQuestionarioEnum getGradeCurricularEstagioQuestionarioEnum() {
		if (gradeCurricularEstagioQuestionarioEnum == null) {
			gradeCurricularEstagioQuestionarioEnum = GradeCurricularEstagioQuestionarioEnum.NENHUM;
		}
		return gradeCurricularEstagioQuestionarioEnum;
	}

	public void setGradeCurricularEstagioQuestionarioEnum(GradeCurricularEstagioQuestionarioEnum gradeCurricularEstagioQuestionarioEnum) {
		this.gradeCurricularEstagioQuestionarioEnum = gradeCurricularEstagioQuestionarioEnum;
	}

	public boolean isPermiteHorasFragmentadas() {
		return permiteHorasFragmentadas;
	}

	public void setPermiteHorasFragmentadas(boolean permiteHorasFragmentadas) {
		this.permiteHorasFragmentadas = permiteHorasFragmentadas;
	}

	public SalaAulaBlackboardVO getSalaAulaBlackboardVO() {
		if (salaAulaBlackboardVO == null) {
			salaAulaBlackboardVO = new SalaAulaBlackboardVO();
		}
		return salaAulaBlackboardVO;
	}

	public void setSalaAulaBlackboardVO(SalaAulaBlackboardVO salaAulaBlackboardVO) {
		this.salaAulaBlackboardVO = salaAulaBlackboardVO;
	}

	public Integer getTotalCargaHorarioEstagio() {
		if (totalCargaHorarioEstagio == null) {
			totalCargaHorarioEstagio = 0;
		}
		return totalCargaHorarioEstagio;
	}

	public void setTotalCargaHorarioEstagio(Integer totalCargaHorarioEstagio) {
		this.totalCargaHorarioEstagio = totalCargaHorarioEstagio;
	}	
	
	public PessoaVO getDocente() {
		if(docente == null) {
			docente = new PessoaVO();
		}
		return docente;
	}

	public void setDocente(PessoaVO docente) {
		this.docente = docente;
	}
	
	public String getNomeDocente() {
		if(nomeDocente == null) {
			nomeDocente = "";
		}
		return nomeDocente;
	}

	public void setNomeDocente(String nomeDocente) {
		this.nomeDocente = nomeDocente;
	}

	public String getTitulacaoDocente() {
		if(titulacaoDocente == null) {
			titulacaoDocente = "";
		}
		return titulacaoDocente;
	}

	public void setTitulacaoDocente(String titulacaoDocente) {
		this.titulacaoDocente = titulacaoDocente;
	}

	public Integer getCargaHorariaAprovada() {
		if(cargaHorariaAprovada == null) {
			cargaHorariaAprovada = 0;
		}
		return cargaHorariaAprovada;
	}

	public void setCargaHorariaAprovada(Integer cargaHorariaAprovada) {
		this.cargaHorariaAprovada = cargaHorariaAprovada;
	}

	public Integer getCargaHorariaEmRealizacao() {
		if(cargaHorariaEmRealizacao == null) {
			cargaHorariaEmRealizacao = 0;
		}
		return cargaHorariaEmRealizacao;
	}

	public void setCargaHorariaEmRealizacao(Integer cargaHorariaEmRealizacao) {
		this.cargaHorariaEmRealizacao = cargaHorariaEmRealizacao;
	}

	public Integer getCargaHorariaIndeferida() {
		if(cargaHorariaIndeferida == null) {
			cargaHorariaIndeferida = 0;
		}
		return cargaHorariaIndeferida;
	}

	public void setCargaHorariaIndeferida(Integer cargaHorariaIndeferida) {
		this.cargaHorariaIndeferida = cargaHorariaIndeferida;
	}

	public Integer getCargaHorariaPendente() {
		if(cargaHorariaPendente == null) {
			cargaHorariaPendente = 0;
		}
		return cargaHorariaPendente;
	}

	public void setCargaHorariaPendente(Integer cargaHorariaPendente) {
		this.cargaHorariaPendente = cargaHorariaPendente;
	}

	public String getSituacao() {
		if(situacao == null) {
			situacao = "";
		}
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public Integer getPercentual() {
		if(percentual == null) {
			percentual = 0;
		}
		return percentual;
	}

	public void setPercentual(Integer percentual) {
		this.percentual = percentual;
	}

	public String getAnoBimestre() {
		if(anoBimestre == null) {
			anoBimestre = "";
		}
		return anoBimestre;
	}

	public void setAnoBimestre(String anoBimestre) {
		this.anoBimestre = anoBimestre;
	}

	public Boolean getFiltrarGradeCurricularEstagioVO() {
		if (filtrarGradeCurricularEstagioVO == null) {
			filtrarGradeCurricularEstagioVO = false;
		}
		return filtrarGradeCurricularEstagioVO;
	}

	public void setFiltrarGradeCurricularEstagioVO(Boolean filtrarGradeCurricularEstagioVO) {
		this.filtrarGradeCurricularEstagioVO = filtrarGradeCurricularEstagioVO;
	}	
	
	public PessoaVO getDocenteResponsavelEstagio() {
		if (docenteResponsavelEstagio == null) {
			docenteResponsavelEstagio = new PessoaVO();
		}
		return docenteResponsavelEstagio;
	}
	
	public void setDocenteResponsavelEstagio(PessoaVO docenteResponsavelEstagio) {
		this.docenteResponsavelEstagio = docenteResponsavelEstagio;
	}

	public Boolean getEditar() {
		if (editar == null) {
			editar = Boolean.FALSE;
		}
		return editar;
	}
	
	public void setEditar(Boolean editar) {
		this.editar = editar;
	}

	public Boolean getInformarSupervisorEstagio() {
		if (informarSupervisorEstagio == null) {
			informarSupervisorEstagio = Boolean.FALSE;
		}
		return informarSupervisorEstagio;
	}

	public void setInformarSupervisorEstagio(Boolean informarSupervisorEstagio) {
		this.informarSupervisorEstagio = informarSupervisorEstagio;
	}
}
