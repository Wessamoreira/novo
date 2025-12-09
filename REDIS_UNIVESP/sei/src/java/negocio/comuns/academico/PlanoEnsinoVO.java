package negocio.comuns.academico;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.academico.enumeradores.SituacaoPlanoEnsinoEnum;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.Uteis;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

public class PlanoEnsinoVO extends SuperVO {

	private static final long serialVersionUID = -391591898831509601L;

	private Integer codigo;
	private DisciplinaVO disciplina;
	private CursoVO curso;
	private UnidadeEnsinoVO unidadeEnsino;
	private String ano;
	private String semestre;
	private String descricao;
	private Date dataCadastro;
	private UsuarioVO responsavel;
	private List<ReferenciaBibliograficaVO> referenciaBibliograficaVOs;
	private List<ConteudoPlanejamentoVO> conteudoPlanejamentoVOs;
	private List<PlanoEnsinoHorarioAulaVO> planoEnsinoHorarioAulaVOs;
	private String ementa;
	private String objetivoGeral;
	private String objetivoEspecifico;
	//private String habilidadeCompetencia;
	private String estrategiaAvaliacao;
	private String procedimentoDidatico;
	private PessoaVO professorResponsavel;
	private TurnoVO turno;
	private String perfilEgresso;
    private String link;
    private String nomeLink;

	/**
	 * Transient Utilizado na Geracao do Relatorio
	 */
	private PeriodoLetivoVO periodoLetivo;
	private Boolean preRequisito;
	private GradeCurricularVO gradeCurricular;
	private GradeDisciplinaVO gradeDisciplinaVO;
	private GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO;
	private GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO;
	private String situacao;
	private BigDecimal totalCargaHoraria;
	private String professor;
	private String motivo;
	
	private CalendarioLancamentoPlanoEnsinoVO calendarioLancamentoPlanoEnsinoVO;
	private String situacaoHistorico;
	private Boolean historicoAprovado;
    private String periodicidade;
	private UsuarioVO responsavelAutorizacao;
	
	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
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

	public CursoVO getCurso() {
		if (curso == null) {
			curso = new CursoVO();
		}
		return curso;
	}

	public void setCurso(CursoVO curso) {
		this.curso = curso;
	}

	public UnidadeEnsinoVO getUnidadeEnsino() {
		if (unidadeEnsino == null) {
			unidadeEnsino = new UnidadeEnsinoVO();
		}
		return unidadeEnsino;
	}

	public void setUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
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

	public String getDescricao() {
		if (descricao == null) {
			descricao = "";
		}
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Date getDataCadastro() {
		if (dataCadastro == null) {
			dataCadastro = new Date();
		}
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public UsuarioVO getResponsavel() {
		if (responsavel == null) {
			responsavel = new UsuarioVO();
		}
		return responsavel;
	}

	public void setResponsavel(UsuarioVO responsavel) {
		this.responsavel = responsavel;
	}

	public List<ReferenciaBibliograficaVO> getReferenciaBibliograficaVOs() {
		if (referenciaBibliograficaVOs == null) {
			referenciaBibliograficaVOs = new ArrayList<ReferenciaBibliograficaVO>(0);
		}
		return referenciaBibliograficaVOs;
	}

	public void setReferenciaBibliograficaVOs(List<ReferenciaBibliograficaVO> referenciaBibliograficaVOs) {
		this.referenciaBibliograficaVOs = referenciaBibliograficaVOs;
	}

	public List<ConteudoPlanejamentoVO> getConteudoPlanejamentoVOs() {
		if (conteudoPlanejamentoVOs == null) {
			conteudoPlanejamentoVOs = new ArrayList<ConteudoPlanejamentoVO>(0);
		}
		return conteudoPlanejamentoVOs;
	}

	public void setConteudoPlanejamentoVOs(List<ConteudoPlanejamentoVO> conteudoPlanejamentoVOs) {
		this.conteudoPlanejamentoVOs = conteudoPlanejamentoVOs;
	}

	public String getEmenta() {
		if (ementa == null) {
			ementa = "";
		}
		return ementa;
	}

	public void setEmenta(String ementa) {
		this.ementa = ementa;
	}

	public String getObjetivoGeral() {
		if (objetivoGeral == null) {
			objetivoGeral = "";
		}
		return objetivoGeral;
	}

	public void setObjetivoGeral(String objetivoGeral) {
		this.objetivoGeral = objetivoGeral;
	}

	public String getObjetivoEspecifico() {
		if (objetivoEspecifico == null) {
			objetivoEspecifico = "";
		}
		return objetivoEspecifico;
	}

	public void setObjetivoEspecifico(String objetivoEspecifico) {
		this.objetivoEspecifico = objetivoEspecifico;
	}

//	public String getHabilidadeCompetencia() {
//		if (habilidadeCompetencia == null) {
//			habilidadeCompetencia = "";
//		}
//		return habilidadeCompetencia;
//	}
//
//	public void setHabilidadeCompetencia(String habilidadeCompetencia) {
//		this.habilidadeCompetencia = habilidadeCompetencia;
//	}

	public String getEstrategiaAvaliacao() {
		if (estrategiaAvaliacao == null) {
			estrategiaAvaliacao = "";
		}
		return estrategiaAvaliacao;
	}

	public void setEstrategiaAvaliacao(String estrategiaAvaliacao) {
		this.estrategiaAvaliacao = estrategiaAvaliacao;
	}

	public String getProcedimentoDidatico() {
		if (procedimentoDidatico == null) {
			procedimentoDidatico = "";
		}
		return procedimentoDidatico;
	}

	public void setProcedimentoDidatico(String procedimentoDidatico) {
		this.procedimentoDidatico = procedimentoDidatico;
	}

	public PeriodoLetivoVO getPeriodoLetivo() {
		if (periodoLetivo == null) {
			periodoLetivo = new PeriodoLetivoVO();
		}
		return periodoLetivo;
	}

	public void setPeriodoLetivo(PeriodoLetivoVO periodoLetivo) {
		this.periodoLetivo = periodoLetivo;
	}

	public JRDataSource getPlanoReferenciaBibliograficaJRDataSource() {
		return new JRBeanArrayDataSource(getReferenciaBibliograficaVOs().toArray());
	}

	public JRDataSource getPlanoConteudoPlanejamentoJRDataSource() {
		return new JRBeanArrayDataSource(getConteudoPlanejamentoVOs().toArray());
	}

	public Boolean getPreRequisito() {
		if (preRequisito == null) {
			preRequisito = false;
		}
		return preRequisito;
	}

	public void setPreRequisito(Boolean preRequisito) {
		this.preRequisito = preRequisito;
	}

	public GradeCurricularVO getGradeCurricular() {
		if (gradeCurricular == null) {
			gradeCurricular = new GradeCurricularVO();
		}
		return gradeCurricular;
	}

	public void setGradeCurricular(GradeCurricularVO gradeCurricular) {
		this.gradeCurricular = gradeCurricular;
	}

	public GradeDisciplinaVO getGradeDisciplinaVO() {
		if (gradeDisciplinaVO == null) {
			gradeDisciplinaVO = new GradeDisciplinaVO();
		}
		return gradeDisciplinaVO;
	}

	public void setGradeDisciplinaVO(GradeDisciplinaVO gradeDisciplinaVO) {
		this.gradeDisciplinaVO = gradeDisciplinaVO;
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

	public String getSituacao_Apresentar() {
		return SituacaoPlanoEnsinoEnum.getDescricao(getSituacao());
	}

	public Integer getNumeroConteudoPlanejados() {
		return getConteudoPlanejamentoVOs().size();
	}

	public GradeDisciplinaCompostaVO getGradeDisciplinaCompostaVO() {
		if (gradeDisciplinaCompostaVO == null) {
			gradeDisciplinaCompostaVO = new GradeDisciplinaCompostaVO();
		}
		return gradeDisciplinaCompostaVO;
	}

	public void setGradeDisciplinaCompostaVO(GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO) {
		this.gradeDisciplinaCompostaVO = gradeDisciplinaCompostaVO;
	}

	public GradeCurricularGrupoOptativaDisciplinaVO getGradeCurricularGrupoOptativaDisciplinaVO() {
		if (gradeCurricularGrupoOptativaDisciplinaVO == null) {
			gradeCurricularGrupoOptativaDisciplinaVO = new GradeCurricularGrupoOptativaDisciplinaVO();
		}
		return gradeCurricularGrupoOptativaDisciplinaVO;
	}

	public void setGradeCurricularGrupoOptativaDisciplinaVO(
			GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO) {
		this.gradeCurricularGrupoOptativaDisciplinaVO = gradeCurricularGrupoOptativaDisciplinaVO;
	}

	public Integer getCargaHoraria() {
		if (Uteis.isAtributoPreenchido(getGradeDisciplinaVO())) {
			return getGradeDisciplinaVO().getCargaHoraria();
		}

		if (Uteis.isAtributoPreenchido(getGradeDisciplinaCompostaVO())) {
			return getGradeDisciplinaCompostaVO().getCargaHoraria();
		}

		if (Uteis.isAtributoPreenchido(getGradeCurricularGrupoOptativaDisciplinaVO())) {
			return getGradeCurricularGrupoOptativaDisciplinaVO().getCargaHoraria();
		}
		return 0;
	}

	public Integer getHoraAula() {
		if (Uteis.isAtributoPreenchido(getGradeDisciplinaVO())) {
			return getGradeDisciplinaVO().getHoraAula();
		}

		if (Uteis.isAtributoPreenchido(getGradeDisciplinaCompostaVO())) {
			return getGradeDisciplinaCompostaVO().getHoraAula();
		}

		if (Uteis.isAtributoPreenchido(getGradeCurricularGrupoOptativaDisciplinaVO())) {
			return getGradeCurricularGrupoOptativaDisciplinaVO().getHoraAula();
		}
		return 0;
	}

	public BigDecimal getTotalCargaHoraria() {
		if (totalCargaHoraria == null) {
			totalCargaHoraria = BigDecimal.ZERO;
		}
		return totalCargaHoraria;
	}

	public void setTotalCargaHoraria(BigDecimal totalCargaHoraria) {
		this.totalCargaHoraria = totalCargaHoraria;
	}

	public String getProfessor() {
		if (professor == null) {
			professor = "";
		}
		return professor;
	}

	public void setProfessor(String professor) {
		this.professor = professor;
	}

	public List<PlanoEnsinoHorarioAulaVO> getPlanoEnsinoHorarioAulaVOs() {
		if (planoEnsinoHorarioAulaVOs == null) {
			planoEnsinoHorarioAulaVOs = new ArrayList<PlanoEnsinoHorarioAulaVO>(0);
		}
		return planoEnsinoHorarioAulaVOs;
	}

	public void setPlanoEnsinoHorarioAulaVOs(List<PlanoEnsinoHorarioAulaVO> planoEnsinoHorarioAulaVOs) {
		this.planoEnsinoHorarioAulaVOs = planoEnsinoHorarioAulaVOs;
	}

	public PessoaVO getProfessorResponsavel() {
		if (professorResponsavel == null) {
			professorResponsavel = new PessoaVO();
		}
		return professorResponsavel;
	}

	public void setProfessorResponsavel(PessoaVO professorResponsavel) {
		this.professorResponsavel = professorResponsavel;
	}

	public TurnoVO getTurno() {
		if (turno == null) {
			turno = new TurnoVO();
		}
		return turno;
	}

	public void setTurno(TurnoVO turno) {
		this.turno = turno;
	}

	public String getPerfilEgresso() {
		if(perfilEgresso == null ) {
			perfilEgresso = "" ;
		}
		return perfilEgresso;
	}

	public void setPerfilEgresso(String perfilEgresso) {
		this.perfilEgresso = perfilEgresso;
	}
	
	private QuestionarioRespostaOrigemVO questionarioRespostaOrigemVO;
	
	public QuestionarioRespostaOrigemVO getQuestionarioRespostaOrigemVO() {
		if(questionarioRespostaOrigemVO == null) {
			questionarioRespostaOrigemVO = new QuestionarioRespostaOrigemVO();
		}
		return questionarioRespostaOrigemVO;
	}

	public void setQuestionarioRespostaOrigemVO(QuestionarioRespostaOrigemVO questionarioRespostaOrigemVO) {
		this.questionarioRespostaOrigemVO = questionarioRespostaOrigemVO;
	}
	
	public String getLink() {
		if(link == null){
			link = "";
		}
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getNomeLink() {
		if(nomeLink == null){
			nomeLink = "";
		}
		return nomeLink;
	}

	public void setNomeLink(String nomeLink) {
		this.nomeLink = nomeLink;
	}

	public CalendarioLancamentoPlanoEnsinoVO getCalendarioLancamentoPlanoEnsinoVO() {
		if (calendarioLancamentoPlanoEnsinoVO == null) {
			calendarioLancamentoPlanoEnsinoVO = new CalendarioLancamentoPlanoEnsinoVO();
		}
		return calendarioLancamentoPlanoEnsinoVO;
	}

	public void setCalendarioLancamentoPlanoEnsinoVO(CalendarioLancamentoPlanoEnsinoVO calendarioLancamentoPlanoEnsinoVO) {
		this.calendarioLancamentoPlanoEnsinoVO = calendarioLancamentoPlanoEnsinoVO;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public String getSituacaoHistorico() {
		if (situacaoHistorico == null) {
			situacaoHistorico = "";
		}
		return situacaoHistorico;
	}

	public void setSituacaoHistorico(String situacaoHistorico) {
		this.situacaoHistorico = situacaoHistorico;
	}

	public Boolean getHistoricoAprovado() {
		if (historicoAprovado == null) {
			historicoAprovado = false;
		}
		return historicoAprovado;
	}

	public void setHistoricoAprovado(Boolean historicoAprovado) {
		this.historicoAprovado = historicoAprovado;
	}
	
	public String getPeriodicidade() {
    	if (periodicidade == null) {
    		periodicidade = "SE";
    	}
        return (periodicidade);
    }

    public String getPeriodicidade_Apresentar() {
        if (getPeriodicidade().equals("IN")) {
            return "Integral";
        }
        if (getPeriodicidade().equals("AN")) {
            return "Anual";
        }
        if (getPeriodicidade().equals("SE")) {
            return "Semestral";
        }
        return (periodicidade);
    }

    public void setPeriodicidade(String periodicidade) {
        this.periodicidade = periodicidade;
    }

	public UsuarioVO getResponsavelAutorizacao() {
		if (responsavelAutorizacao == null) {
			responsavelAutorizacao = new UsuarioVO();
		}
		return responsavelAutorizacao;
	}

	public void setResponsavelAutorizacao(UsuarioVO responsavelAutorizacao) {
		this.responsavelAutorizacao = responsavelAutorizacao;
	}
    
    

}