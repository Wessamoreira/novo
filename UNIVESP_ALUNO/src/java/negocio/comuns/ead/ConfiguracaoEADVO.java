package negocio.comuns.ead;
import negocio.comuns.administrativo.GrupoDestinatariosVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.ead.enumeradores.OrdemEstudoDisciplinasOnlineEnum;
import negocio.comuns.ead.enumeradores.SituacaoEnum;
import negocio.comuns.ead.enumeradores.TipoControleLiberacaoAvaliacaoOnlineEnum;
import negocio.comuns.ead.enumeradores.TipoControleLiberacaoProvaPresencialEnum;
import negocio.comuns.ead.enumeradores.TipoControleLiberarAcessoProximaDisciplinaEnum;
import negocio.comuns.ead.enumeradores.TipoControleTempoLimiteConclusaoDisciplinaEnum;

public class ConfiguracaoEADVO extends SuperVO {

	private static final long serialVersionUID = -8701471861440173564L;
	private Integer codigo;
	private String descricao;
	/**
	 * Pode variar conforme o tipo de controle de tempo.No caso de
	 * NR.DIAS_POR_HORA, o sitema deve perguntar o NR.DIAS_POR_HORA. Por exemplo
	 * ,se o usuario informar 1,5 dias por hora. Uma disciplina de 30 horas o
	 * aluno tera 45 dias para concluir os estudos online da mesma
	 * (estudar,fazer atividades e fazer avaliação on-line)
	 *
	 */
	private TipoControleTempoLimiteConclusaoDisciplinaEnum tipoControleTempoLimiteConclusaoDisciplina;
	private Double tempoLimiteConclusaoDisciplina;
	private Integer tempoLimiteConclusaoTodasDisciplinas;
	private Integer tempoLimiteConclusaoCursoIncluindoTCC;
	/**
	 * Se desmarcar significará que se a disciplina exige a realização de prova
	 * presencial. Então o tempo limite para realização da mesma já estará
	 * imbutida no tempo total para realização da disciplina para os estudos
	 * on-line.
	 */
	private Boolean controlarTempoLimiteRealizarProvaPresencial;
	private Integer tempoLimiteRealizarProvaPresencial;
	/**
	 * Apresentado somente quando OrdemEstudoDisciplinasOnline for sequenciado
	 * ou tiver numero maximo de disciplinas simultaneas.
	 */
	private TipoControleLiberacaoAvaliacaoOnlineEnum tipoControleLiberacaoAvaliacaoOnline;
	private Integer valorControleLiberacaoAvalicaoOnline;
	private Integer nrVezesPodeRepetirAvaliacaoOnline;
	private Integer nrDiasEntreAvalicaoOnline;
	private OrdemEstudoDisciplinasOnlineEnum ordemEstudoDisciplinasOnline;
	private Integer nrMaximoDisciplinasOnline;
	private Integer nrMaximoDisciplinasSimultaneas;
	private TipoControleLiberarAcessoProximaDisciplinaEnum tipoControleLiberarAcessoProximaDisciplina;
	private Integer valorControleLiberarAcessoProximaDisciplina;
	private TipoControleLiberacaoProvaPresencialEnum tipoControleLiberacaoProvaPresencial;
	private boolean permitirAcessoEadSemConteudo = false;
	/**
	 * Apresentar somente quando TipoControleLiberacaoProvaPresencialEnum for
	 * por PERCENTUAL
	 */
	private Integer valorControleLiberacaoProvaPresencial;
	private Integer tempoLimiteRealizacaoProvaPresencial;
	private String variavelNotaCfgPadraoAtividadeDiscursiva;
	private String variavelNotaCfgPadraoAvaliacaoOnline;
	private SituacaoEnum situacao;
	private ParametrosMonitoramentoAvaliacaoOnlineVO parametrosMonitoramentoAvaliacaoOnlineVO;
	

	public String getDescricao() {
		if (descricao == null) {
			descricao = "";
		}
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public TipoControleTempoLimiteConclusaoDisciplinaEnum getTipoControleTempoLimiteConclusaoDisciplina() {
		if (tipoControleTempoLimiteConclusaoDisciplina == null) {
			tipoControleTempoLimiteConclusaoDisciplina = TipoControleTempoLimiteConclusaoDisciplinaEnum.NR_DIAS_POR_HORA_DA_DISCIPLINA_CH;
		}
		return tipoControleTempoLimiteConclusaoDisciplina;
	}

	public void setTipoControleTempoLimiteConclusaoDisciplina(TipoControleTempoLimiteConclusaoDisciplinaEnum tipoControleTempoLimiteConclusaoDisciplina) {
		this.tipoControleTempoLimiteConclusaoDisciplina = tipoControleTempoLimiteConclusaoDisciplina;
	}

	public Double getTempoLimiteConclusaoDisciplina() {
		if (tempoLimiteConclusaoDisciplina == null) {
			tempoLimiteConclusaoDisciplina = 0.0;
		}
		return tempoLimiteConclusaoDisciplina;
	}

	public void setTempoLimiteConclusaoDisciplina(Double tempoLimiteConclusaoDisciplina) {
		this.tempoLimiteConclusaoDisciplina = tempoLimiteConclusaoDisciplina;
	}

	public Integer getTempoLimiteConclusaoTodasDisciplinas() {
		if (tempoLimiteConclusaoTodasDisciplinas == null) {
			tempoLimiteConclusaoTodasDisciplinas = 0;
		}
		return tempoLimiteConclusaoTodasDisciplinas;
	}

	public void setTempoLimiteConclusaoTodasDisciplinas(Integer tempoLimiteConclusaoTodasDisciplinas) {
		this.tempoLimiteConclusaoTodasDisciplinas = tempoLimiteConclusaoTodasDisciplinas;
	}

	public Integer getTempoLimiteConclusaoCursoIncluindoTCC() {
		if (tempoLimiteConclusaoCursoIncluindoTCC == null) {
			tempoLimiteConclusaoCursoIncluindoTCC = 0;
		}
		return tempoLimiteConclusaoCursoIncluindoTCC;
	}

	public void setTempoLimiteConclusaoCursoIncluindoTCC(Integer tempoLimiteConclusaoCursoIncluindoTCC) {
		this.tempoLimiteConclusaoCursoIncluindoTCC = tempoLimiteConclusaoCursoIncluindoTCC;
	}

	public Boolean getControlarTempoLimiteRealizarProvaPresencial() {
		if (controlarTempoLimiteRealizarProvaPresencial == null) {
			controlarTempoLimiteRealizarProvaPresencial = false;
		}
		return controlarTempoLimiteRealizarProvaPresencial;
	}

	public void setControlarTempoLimiteRealizarProvaPresencial(Boolean controlarTempoLimiteRealizarProvaPresencial) {
		this.controlarTempoLimiteRealizarProvaPresencial = controlarTempoLimiteRealizarProvaPresencial;
	}

	public Integer getTempoLimiteRealizarProvaPresencial() {
		if (tempoLimiteRealizarProvaPresencial == null) {
			tempoLimiteRealizarProvaPresencial = 0;
		}
		return tempoLimiteRealizarProvaPresencial;
	}

	public void setTempoLimiteRealizarProvaPresencial(Integer tempoLimiteRealizarProvaPresencial) {
		this.tempoLimiteRealizarProvaPresencial = tempoLimiteRealizarProvaPresencial;
	}

	public TipoControleLiberacaoAvaliacaoOnlineEnum getTipoControleLiberacaoAvaliacaoOnline() {
		if (tipoControleLiberacaoAvaliacaoOnline == null) {
			tipoControleLiberacaoAvaliacaoOnline = TipoControleLiberacaoAvaliacaoOnlineEnum.NAO_APLICAR_AVALIACAO_ONLINE;
		}
		return tipoControleLiberacaoAvaliacaoOnline;
	}

	public void setTipoControleLiberacaoAvaliacaoOnline(TipoControleLiberacaoAvaliacaoOnlineEnum tipoControleLiberacaoAvaliacaoOnline) {
		this.tipoControleLiberacaoAvaliacaoOnline = tipoControleLiberacaoAvaliacaoOnline;
	}

	public Integer getValorControleLiberacaoAvalicaoOnline() {
		if (valorControleLiberacaoAvalicaoOnline == null) {
			valorControleLiberacaoAvalicaoOnline = 0;
		}
		return valorControleLiberacaoAvalicaoOnline;
	}

	public void setValorControleLiberacaoAvalicaoOnline(Integer valorControleLiberacaoAvalicaoOnline) {
		this.valorControleLiberacaoAvalicaoOnline = valorControleLiberacaoAvalicaoOnline;
	}

	public Integer getNrVezesPodeRepetirAvaliacaoOnline() {
		if (nrVezesPodeRepetirAvaliacaoOnline == null) {
			nrVezesPodeRepetirAvaliacaoOnline = 0;
		}
		return nrVezesPodeRepetirAvaliacaoOnline;
	}

	public void setNrVezesPodeRepetirAvaliacaoOnline(Integer nrVezesPodeRepetirAvaliacaoOnline) {
		this.nrVezesPodeRepetirAvaliacaoOnline = nrVezesPodeRepetirAvaliacaoOnline;
	}

	public Integer getNrDiasEntreAvalicaoOnline() {
		if (nrDiasEntreAvalicaoOnline == null) {
			nrDiasEntreAvalicaoOnline = 0;
		}
		return nrDiasEntreAvalicaoOnline;
	}

	public void setNrDiasEntreAvalicaoOnline(Integer nrDiasEntreAvalicaoOnline) {
		this.nrDiasEntreAvalicaoOnline = nrDiasEntreAvalicaoOnline;
	}

	public OrdemEstudoDisciplinasOnlineEnum getOrdemEstudoDisciplinasOnline() {
		if (ordemEstudoDisciplinasOnline == null) {
			ordemEstudoDisciplinasOnline = OrdemEstudoDisciplinasOnlineEnum.SEQUENCIADAS;
		}
		return ordemEstudoDisciplinasOnline;
	}

	public void setOrdemEstudoDisciplinasOnline(OrdemEstudoDisciplinasOnlineEnum ordemEstudoDisciplinasOnline) {
		this.ordemEstudoDisciplinasOnline = ordemEstudoDisciplinasOnline;
	}

	public Integer getNrMaximoDisciplinasOnline() {
		if (nrMaximoDisciplinasOnline == null) {
			nrMaximoDisciplinasOnline = 0;
		}
		return nrMaximoDisciplinasOnline;
	}

	public void setNrMaximoDisciplinasOnline(Integer nrMaximoDisciplinasOnline) {
		this.nrMaximoDisciplinasOnline = nrMaximoDisciplinasOnline;
	}

	public TipoControleLiberarAcessoProximaDisciplinaEnum getTipoControleLiberarAcessoProximaDisciplina() {
		if (tipoControleLiberarAcessoProximaDisciplina == null) {
			tipoControleLiberarAcessoProximaDisciplina = TipoControleLiberarAcessoProximaDisciplinaEnum.APROVADO_AVALIACAO_ONLINE;
		}
		return tipoControleLiberarAcessoProximaDisciplina;
	}

	public void setTipoControleLiberarAcessoProximaDisciplina(TipoControleLiberarAcessoProximaDisciplinaEnum tipoControleLiberarAcessoProximaDisciplina) {
		this.tipoControleLiberarAcessoProximaDisciplina = tipoControleLiberarAcessoProximaDisciplina;
	}

	public Integer getValorControleLiberarAcessoProximaDisciplina() {
		if (valorControleLiberarAcessoProximaDisciplina == null) {
			valorControleLiberarAcessoProximaDisciplina = 0;
		}
		return valorControleLiberarAcessoProximaDisciplina;
	}

	public void setValorControleLiberarAcessoProximaDisciplina(Integer valorControleLiberarAcessoProximaDisciplina) {
		this.valorControleLiberarAcessoProximaDisciplina = valorControleLiberarAcessoProximaDisciplina;
	}

	public TipoControleLiberacaoProvaPresencialEnum getTipoControleLiberacaoProvaPresencial() {
		if (tipoControleLiberacaoProvaPresencial == null) {
			tipoControleLiberacaoProvaPresencial = TipoControleLiberacaoProvaPresencialEnum.NAO_CONTROLAR_PROVA_PRESENCIAL;
		}
		return tipoControleLiberacaoProvaPresencial;
	}

	public void setTipoControleLiberacaoProvaPresencial(TipoControleLiberacaoProvaPresencialEnum tipoControleLiberacaoProvaPresencial) {
		this.tipoControleLiberacaoProvaPresencial = tipoControleLiberacaoProvaPresencial;
	}

	public Integer getValorControleLiberacaoProvaPresencial() {
		if (valorControleLiberacaoProvaPresencial == null) {
			valorControleLiberacaoProvaPresencial = 0;
		}
		return valorControleLiberacaoProvaPresencial;
	}

	public void setValorControleLiberacaoProvaPresencial(Integer valorControleLiberacaoProvaPresencial) {
		this.valorControleLiberacaoProvaPresencial = valorControleLiberacaoProvaPresencial;
	}

	public Integer getTempoLimiteRealizacaoProvaPresencial() {
		if (tempoLimiteRealizacaoProvaPresencial == null) {
			tempoLimiteRealizacaoProvaPresencial = 0;
		}
		return tempoLimiteRealizacaoProvaPresencial;
	}

	public void setTempoLimiteRealizacaoProvaPresencial(Integer tempoLimiteRealizacaoProvaPresencial) {
		this.tempoLimiteRealizacaoProvaPresencial = tempoLimiteRealizacaoProvaPresencial;
	}

	public String getVariavelNotaCfgPadraoAtividadeDiscursiva() {
		if (variavelNotaCfgPadraoAtividadeDiscursiva == null) {
			variavelNotaCfgPadraoAtividadeDiscursiva = "";
		}
		return variavelNotaCfgPadraoAtividadeDiscursiva;
	}

	public void setVariavelNotaCfgPadraoAtividadeDiscursiva(String variavelNotaCfgPadraoAtividadeDiscursiva) {
		this.variavelNotaCfgPadraoAtividadeDiscursiva = variavelNotaCfgPadraoAtividadeDiscursiva;
	}

	public Integer getNrMaximoDisciplinasSimultaneas() {
		if (nrMaximoDisciplinasSimultaneas == null) {
			nrMaximoDisciplinasSimultaneas = 0;
		}
		return nrMaximoDisciplinasSimultaneas;
	}

	public void setNrMaximoDisciplinasSimultaneas(Integer nrMaximoDisciplinasSimultaneas) {
		this.nrMaximoDisciplinasSimultaneas = nrMaximoDisciplinasSimultaneas;
	}

	public String getVariavelNotaCfgPadraoAvaliacaoOnline() {
		if (variavelNotaCfgPadraoAvaliacaoOnline == null) {
			variavelNotaCfgPadraoAvaliacaoOnline = "";
		}
		return variavelNotaCfgPadraoAvaliacaoOnline;
	}

	public void setVariavelNotaCfgPadraoAvaliacaoOnline(String variavelNotaCfgPadraoAvaliacaoOnline) {
		this.variavelNotaCfgPadraoAvaliacaoOnline = variavelNotaCfgPadraoAvaliacaoOnline;
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

	public SituacaoEnum getSituacao() {
		if (situacao == null) {
			situacao = SituacaoEnum.EM_CONSTRUCAO;
		}
		return situacao;
	}

	public void setSituacao(SituacaoEnum situacao) {
		this.situacao = situacao;
	}
	
	public String getSituacaoEnum_Apresentar() {
		return getSituacao().getValorApresentar();
	}
	
	/**
	 * @author Victor Hugo 20/11/2014
	 * 
	 */
	private String instrucoesAvaliacaoOnline;


	public String getInstrucoesAvaliacaoOnline() {
		if (instrucoesAvaliacaoOnline == null) {
			instrucoesAvaliacaoOnline = "";
		}
		return instrucoesAvaliacaoOnline;
	}

	public void setInstrucoesAvaliacaoOnline(String instrucoesAvaliacaoOnline) {
		this.instrucoesAvaliacaoOnline = instrucoesAvaliacaoOnline;
	}
	
	private Boolean calcularMediaFinalAposRealizacaoAtividadeDiscursiva;
	private Boolean calcularMediaFinalAposRealizacaoAvaliacaoOnline;


	public Boolean getCalcularMediaFinalAposRealizacaoAtividadeDiscursiva() {
		if (calcularMediaFinalAposRealizacaoAtividadeDiscursiva == null) {
			calcularMediaFinalAposRealizacaoAtividadeDiscursiva = false;
		}
		return calcularMediaFinalAposRealizacaoAtividadeDiscursiva;
	}

	public void setCalcularMediaFinalAposRealizacaoAtividadeDiscursiva(Boolean calcularMediaFinalAposRealizacaoAtividadeDiscursiva) {
		this.calcularMediaFinalAposRealizacaoAtividadeDiscursiva = calcularMediaFinalAposRealizacaoAtividadeDiscursiva;
	}

	public Boolean getCalcularMediaFinalAposRealizacaoAvaliacaoOnline() {
		if (calcularMediaFinalAposRealizacaoAvaliacaoOnline == null) {
			calcularMediaFinalAposRealizacaoAvaliacaoOnline = false;
		}
		return calcularMediaFinalAposRealizacaoAvaliacaoOnline;
	}

	public void setCalcularMediaFinalAposRealizacaoAvaliacaoOnline(Boolean calcularMediaFinalAposRealizacaoAvaliacaoOnline) {
		this.calcularMediaFinalAposRealizacaoAvaliacaoOnline = calcularMediaFinalAposRealizacaoAvaliacaoOnline;
	}
	
	/**
	 * 
	 * @author Victor Hugo 20/11/2014
	 * 
	 */
	private String variavelNotaCfgPadraoProvaPresencial;
	private Boolean calcularMediaFinalAposRealizacaoProvaPresencial;
	private Boolean permitirAcessoConsultaConteudoDisciplinaConclusaoCurso;
	private Integer tempoLimiteAcessoConsultaConteudoDisciplinaAposConclusao;


	public String getVariavelNotaCfgPadraoProvaPresencial() {
		if(variavelNotaCfgPadraoProvaPresencial == null) {
			variavelNotaCfgPadraoProvaPresencial = "";
		}
		return variavelNotaCfgPadraoProvaPresencial;
	}

	public void setVariavelNotaCfgPadraoProvaPresencial(String variavelNotaCfgPadraoProvaPresencial) {
		this.variavelNotaCfgPadraoProvaPresencial = variavelNotaCfgPadraoProvaPresencial;
	}

	public Boolean getCalcularMediaFinalAposRealizacaoProvaPresencial() {
		if(calcularMediaFinalAposRealizacaoProvaPresencial == null) {
			calcularMediaFinalAposRealizacaoProvaPresencial = false;
		}
		return calcularMediaFinalAposRealizacaoProvaPresencial;
	}

	public void setCalcularMediaFinalAposRealizacaoProvaPresencial(Boolean calcularMediaFinalAposRealizacaoProvaPresencial) {
		this.calcularMediaFinalAposRealizacaoProvaPresencial = calcularMediaFinalAposRealizacaoProvaPresencial;
	}

	public Boolean getPermitirAcessoConsultaConteudoDisciplinaConclusaoCurso() {
		if(permitirAcessoConsultaConteudoDisciplinaConclusaoCurso == null) {
			permitirAcessoConsultaConteudoDisciplinaConclusaoCurso = false;
		}
		return permitirAcessoConsultaConteudoDisciplinaConclusaoCurso;
	}

	public void setPermitirAcessoConsultaConteudoDisciplinaConclusaoCurso(Boolean permitirAcessoConsultaConteudoDisciplinaConclusaoCurso) {
		this.permitirAcessoConsultaConteudoDisciplinaConclusaoCurso = permitirAcessoConsultaConteudoDisciplinaConclusaoCurso;
	}

	public Integer getTempoLimiteAcessoConsultaConteudoDisciplinaAposConclusao() {
		if(tempoLimiteAcessoConsultaConteudoDisciplinaAposConclusao == null) {
			tempoLimiteAcessoConsultaConteudoDisciplinaAposConclusao = 0;
		}
		return tempoLimiteAcessoConsultaConteudoDisciplinaAposConclusao;
	}

	public void setTempoLimiteAcessoConsultaConteudoDisciplinaAposConclusao(Integer tempoLimiteAcessoConsultaConteudoDisciplinaAposConclusao) {
		this.tempoLimiteAcessoConsultaConteudoDisciplinaAposConclusao = tempoLimiteAcessoConsultaConteudoDisciplinaAposConclusao;
	}
	
	public String getTipoControleLiberacaoAvaliacaoOnline_Apresentar() {
		return getTipoControleLiberacaoAvaliacaoOnline().getValorApresentar();
	}
	
	/**
	 * @author Victor Hugo 04/12/2014
	 * 
	 */
	private Integer notUmPrazoConclusaoEstudos;
	private Integer notDoisPrazoConclusaoEstudos;
	private Integer notTresPrazoConclusaoEstudos;
	private Boolean notificarAluno;


	public Integer getNotUmPrazoConclusaoEstudos() {
		if (notUmPrazoConclusaoEstudos == null) {
			notUmPrazoConclusaoEstudos = 0;
		}
		return notUmPrazoConclusaoEstudos;
	}

	public void setNotUmPrazoConclusaoEstudos(Integer notUmPrazoConclusaoEstudos) {
		this.notUmPrazoConclusaoEstudos = notUmPrazoConclusaoEstudos;
	}

	public Integer getNotDoisPrazoConclusaoEstudos() {
		if (notDoisPrazoConclusaoEstudos == null) {
			notDoisPrazoConclusaoEstudos = 0;
		}
		return notDoisPrazoConclusaoEstudos;
	}

	public void setNotDoisPrazoConclusaoEstudos(Integer notDoisPrazoConclusaoEstudos) {
		this.notDoisPrazoConclusaoEstudos = notDoisPrazoConclusaoEstudos;
	}

	public Integer getNotTresPrazoConclusaoEstudos() {
		if (notTresPrazoConclusaoEstudos == null) {
			notTresPrazoConclusaoEstudos = 0;
		}
		return notTresPrazoConclusaoEstudos;
	}

	public void setNotTresPrazoConclusaoEstudos(Integer notTresPrazoConclusaoEstudos) {
		this.notTresPrazoConclusaoEstudos = notTresPrazoConclusaoEstudos;
	}

	public Boolean getNotificarAluno() {
		if (notificarAluno == null) {
			notificarAluno = false;
		}
		return notificarAluno;
	}

	public void setNotificarAluno(Boolean notificarAluno) {
		this.notificarAluno = notificarAluno;
	}
	
	public boolean isPermitirAcessoEadSemConteudo() {
		return permitirAcessoEadSemConteudo;
	}

	public void setPermitirAcessoEadSemConteudo(boolean permitirAcessoEadSemConteudo) {
		this.permitirAcessoEadSemConteudo = permitirAcessoEadSemConteudo;
	}

	/**
	 * @author Victor Hugo 10/12/2014
	 */
	private Boolean notificarAlunoDiasFicarSemLogarSistema;
	private Integer notUmDiasSemLogarSistema;
	private Integer notDoisDiasSemLogarSistema;
	private Integer notTresDiasSemLogarSistema;


	public Boolean getNotificarAlunoDiasFicarSemLogarSistema() {
		if (notificarAlunoDiasFicarSemLogarSistema == null) {
			notificarAlunoDiasFicarSemLogarSistema = false;
		}
		return notificarAlunoDiasFicarSemLogarSistema;
	}

	public void setNotificarAlunoDiasFicarSemLogarSistema(Boolean notificarAlunoDiasFicarSemLogarSistema) {
		this.notificarAlunoDiasFicarSemLogarSistema = notificarAlunoDiasFicarSemLogarSistema;
	}

	public Integer getNotUmDiasSemLogarSistema() {
		if (notUmDiasSemLogarSistema == null) {
			notUmDiasSemLogarSistema = 0;
		}
		return notUmDiasSemLogarSistema;
	}

	public void setNotUmDiasSemLogarSistema(Integer notUmDiasSemLogarSistema) {
		this.notUmDiasSemLogarSistema = notUmDiasSemLogarSistema;
	}

	public Integer getNotDoisDiasSemLogarSistema() {
		if (notDoisDiasSemLogarSistema == null) {
			notDoisDiasSemLogarSistema = 0;
		}
		return notDoisDiasSemLogarSistema;
	}

	public void setNotDoisDiasSemLogarSistema(Integer notDoisDiasSemLogarSistema) {
		this.notDoisDiasSemLogarSistema = notDoisDiasSemLogarSistema;
	}

	public Integer getNotTresDiasSemLogarSistema() {
		if (notTresDiasSemLogarSistema == null) {
			notTresDiasSemLogarSistema = 0;
		}
		return notTresDiasSemLogarSistema;
	}

	public void setNotTresDiasSemLogarSistema(Integer notTresDiasSemLogarSistema) {
		this.notTresDiasSemLogarSistema = notTresDiasSemLogarSistema;
	}
	
	/**
	 * @author Victor Hugo 23/12/2014
	 */
	private Boolean notificarAlunosAtividadeDiscursivaPrazoConclusaoCurso;
	private Integer notUmAtividadeDiscursivaPrazoConclusaoCurso;
	private Integer notDoisAtividadeDiscursivaPrazoConclusaoCurso;
	private Integer notTresAtividadeDiscursivaPrazoConclusaoCurso;


	public Boolean getNotificarAlunosAtividadeDiscursivaPrazoConclusaoCurso() {
		if (notificarAlunosAtividadeDiscursivaPrazoConclusaoCurso == null) {
			notificarAlunosAtividadeDiscursivaPrazoConclusaoCurso = false;
		}
		return notificarAlunosAtividadeDiscursivaPrazoConclusaoCurso;
	}

	public void setNotificarAlunosAtividadeDiscursivaPrazoConclusaoCurso(Boolean notificarAlunosAtividadeDiscursivaPrazoConclusaoCurso) {
		this.notificarAlunosAtividadeDiscursivaPrazoConclusaoCurso = notificarAlunosAtividadeDiscursivaPrazoConclusaoCurso;
	}

	public Integer getNotUmAtividadeDiscursivaPrazoConclusaoCurso() {
		if (notUmAtividadeDiscursivaPrazoConclusaoCurso == null) {
			notUmAtividadeDiscursivaPrazoConclusaoCurso = 0;
		}
		return notUmAtividadeDiscursivaPrazoConclusaoCurso;
	}

	public void setNotUmAtividadeDiscursivaPrazoConclusaoCurso(Integer notUmAtividadeDiscursivaPrazoConclusaoCurso) {
		this.notUmAtividadeDiscursivaPrazoConclusaoCurso = notUmAtividadeDiscursivaPrazoConclusaoCurso;
	}

	public Integer getNotDoisAtividadeDiscursivaPrazoConclusaoCurso() {
		if (notDoisAtividadeDiscursivaPrazoConclusaoCurso == null) {
			notDoisAtividadeDiscursivaPrazoConclusaoCurso = 0;
		}
		return notDoisAtividadeDiscursivaPrazoConclusaoCurso;
	}

	public void setNotDoisAtividadeDiscursivaPrazoConclusaoCurso(Integer notDoisAtividadeDiscursivaPrazoConclusaoCurso) {
		this.notDoisAtividadeDiscursivaPrazoConclusaoCurso = notDoisAtividadeDiscursivaPrazoConclusaoCurso;
	}

	public Integer getNotTresAtividadeDiscursivaPrazoConclusaoCurso() {
		if (notTresAtividadeDiscursivaPrazoConclusaoCurso == null) {
			notTresAtividadeDiscursivaPrazoConclusaoCurso = 0;
		}
		return notTresAtividadeDiscursivaPrazoConclusaoCurso;
	}

	public void setNotTresAtividadeDiscursivaPrazoConclusaoCurso(Integer notTresAtividadeDiscursivaPrazoConclusaoCurso) {
		this.notTresAtividadeDiscursivaPrazoConclusaoCurso = notTresAtividadeDiscursivaPrazoConclusaoCurso;
	}
	
	/**
	 * @author  Victor Hugo 06/01/2015
	 */
	private Boolean notificarProfessorDuvidasNaoRespondidas;
	private Integer notificacaoProfessorDiasDuvidasNaoRespondidas;
	private Boolean notificarCoodenadorDuvidasProfessorNaoRespondidas;
	private Boolean notificarGrupoDestinatarioDuvidasNaoRespondidasProfessor;
	private Integer notificacaoGrupoDestinatarioDiasDuvidasNaoRespondidas;
	private GrupoDestinatariosVO grupoDestinatariosVO;
	
	public Boolean getNotificarProfessorDuvidasNaoRespondidas() {
		if (notificarProfessorDuvidasNaoRespondidas == null) {
			notificarProfessorDuvidasNaoRespondidas = false;
		}
		return notificarProfessorDuvidasNaoRespondidas;
	}

	public void setNotificarProfessorDuvidasNaoRespondidas(Boolean notificarProfessorDuvidasNaoRespondidas) {
		this.notificarProfessorDuvidasNaoRespondidas = notificarProfessorDuvidasNaoRespondidas;
	}

	public Integer getNotificacaoProfessorDiasDuvidasNaoRespondidas() {
		if (notificacaoProfessorDiasDuvidasNaoRespondidas == null) {
			notificacaoProfessorDiasDuvidasNaoRespondidas = 0;
		}
		return notificacaoProfessorDiasDuvidasNaoRespondidas;
	}

	public void setNotificacaoProfessorDiasDuvidasNaoRespondidas(Integer notificacaoProfessorDiasDuvidasNaoRespondidas) {
		this.notificacaoProfessorDiasDuvidasNaoRespondidas = notificacaoProfessorDiasDuvidasNaoRespondidas;
	}

	public Boolean getNotificarCoodenadorDuvidasProfessorNaoRespondidas() {
		if (notificarCoodenadorDuvidasProfessorNaoRespondidas == null) {
			notificarCoodenadorDuvidasProfessorNaoRespondidas = false;
		}
		return notificarCoodenadorDuvidasProfessorNaoRespondidas;
	}

	public void setNotificarCoodenadorDuvidasProfessorNaoRespondidas(Boolean notificarCoodenadorDuvidasProfessorNaoRespondidas) {
		this.notificarCoodenadorDuvidasProfessorNaoRespondidas = notificarCoodenadorDuvidasProfessorNaoRespondidas;
	}

	public Boolean getNotificarGrupoDestinatarioDuvidasNaoRespondidasProfessor() {
		if (notificarGrupoDestinatarioDuvidasNaoRespondidasProfessor == null) {
			notificarGrupoDestinatarioDuvidasNaoRespondidasProfessor = false;
		}
		return notificarGrupoDestinatarioDuvidasNaoRespondidasProfessor;
	}

	public void setNotificarGrupoDestinatarioDuvidasNaoRespondidasProfessor(Boolean notificarGrupoDestinatarioDuvidasNaoRespondidasProfessor) {
		this.notificarGrupoDestinatarioDuvidasNaoRespondidasProfessor = notificarGrupoDestinatarioDuvidasNaoRespondidasProfessor;
	}

	public Integer getNotificacaoGrupoDestinatarioDiasDuvidasNaoRespondidas() {
		if (notificacaoGrupoDestinatarioDiasDuvidasNaoRespondidas == null) {
			notificacaoGrupoDestinatarioDiasDuvidasNaoRespondidas = 0;
		}
		return notificacaoGrupoDestinatarioDiasDuvidasNaoRespondidas;
	}

	public void setNotificacaoGrupoDestinatarioDiasDuvidasNaoRespondidas(Integer notificacaoGrupoDestinatarioDiasDuvidasNaoRespondidas) {
		this.notificacaoGrupoDestinatarioDiasDuvidasNaoRespondidas = notificacaoGrupoDestinatarioDiasDuvidasNaoRespondidas;
	}

	public GrupoDestinatariosVO getGrupoDestinatariosVO() {
		if (grupoDestinatariosVO == null) {
			grupoDestinatariosVO = new GrupoDestinatariosVO();
		}
		return grupoDestinatariosVO;
	}

	public void setGrupoDestinatariosVO(GrupoDestinatariosVO grupoDestinatariosVO) {
		this.grupoDestinatariosVO = grupoDestinatariosVO;
	}
	
	public ParametrosMonitoramentoAvaliacaoOnlineVO getParametrosMonitoramentoAvaliacaoOnlineVO() {
		if (parametrosMonitoramentoAvaliacaoOnlineVO == null) {
			parametrosMonitoramentoAvaliacaoOnlineVO = new ParametrosMonitoramentoAvaliacaoOnlineVO();
		}
		return parametrosMonitoramentoAvaliacaoOnlineVO;
	}

	public void setParametrosMonitoramentoAvaliacaoOnlineVO(ParametrosMonitoramentoAvaliacaoOnlineVO parametrosMonitoramentoAvaliacaoOnlineVO) {
		this.parametrosMonitoramentoAvaliacaoOnlineVO = parametrosMonitoramentoAvaliacaoOnlineVO;
	}
}
