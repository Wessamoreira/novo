package negocio.comuns.ead;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.academico.ConteudoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.UnidadeConteudoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.ead.enumeradores.PoliticaSelecaoQuestaoEnum;
import negocio.comuns.ead.enumeradores.RegraDefinicaoPeriodoAvaliacaoOnlineEnum;
import negocio.comuns.ead.enumeradores.RegraDistribuicaoQuestaoEnum;
import negocio.comuns.ead.enumeradores.SituacaoEnum;
import negocio.comuns.ead.enumeradores.TipoGeracaoProvaOnlineEnum;
import negocio.comuns.ead.enumeradores.TipoUsoEnum;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;

/**
 * @author Victor Hugo 10/10/2014
 */
public class AvaliacaoOnlineVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private String nome;
	private DisciplinaVO disciplinaVO;
	private TurmaVO turmaVO;
	private String ano;
	private String semestre;
	private Date dataCriacao;
	private UsuarioVO reposnsavelCriacao;
	private Date dataAtivacao; 	
	private UsuarioVO reposnsavelAtivacao;
	private Date dataInativacao;
	private UsuarioVO reposnsavelInativacao;
	private Integer quantidadeNivelQuestaoMedio;
	private Integer quantidadeNivelQuestaoFacil;
	private Integer quantidadeNivelQuestaoDificil;
	private Integer quantidadeQualquerNivelQuestao;
	private Double notaPorQuestaoNivelMedio;
	private Double notaPorQuestaoNivelFacil;
	private Double notaPorQuestaoNivelDificil;
	private Double notaPorQuestaoQualquerNivel;
	private Double notaMaximaAvaliacao;
	private Integer percentualAprovacao;
	private SituacaoEnum situacao;
	private TipoGeracaoProvaOnlineEnum tipoGeracaoProvaOnline;
	private TipoUsoEnum tipoUso;
	private List<AvaliacaoOnlineQuestaoVO> avaliacaoOnlineQuestaoVOs;
	private ConteudoVO conteudoVO;
	private UnidadeConteudoVO unidadeConteudoVO;
	private Boolean usoExclusivoProfessor;
	private PessoaVO professor;
	private PoliticaSelecaoQuestaoEnum politicaSelecaoQuestaoEnum;
	private RegraDistribuicaoQuestaoEnum regraDistribuicaoQuestaoEnum;
	private Boolean permiteRepeticoesDeQuestoesAPartirSegundaAvaliacaoOnlineAluno;
	private ParametrosMonitoramentoAvaliacaoOnlineVO parametrosMonitoramentoAvaliacaoOnlineVO;
	private Integer qtdDiasLimiteResponderAvaliacaOnline;
	private RegraDefinicaoPeriodoAvaliacaoOnlineEnum regraDefinicaoPeriodoAvaliacaoOnline;
	private Date dataInicioAvaliacaoFixo;
	private Date dataTerminoAvaliacaoFixo;
	private boolean apresentarNotaDaQuestao = true;
	/**
	 * Campo usado inicialmente para informa a nota da configuracao qndo o tipo de uso for
	 * REA, porem agora e utilizado de forma geral sempre sera apresentado na tela só que para o uso do rea e obrigatorio caso contrario
	 * podera ser utilizado o que estiver informado aqui ou na configuracao do ead. Pedro andrade 31/03/2020
	 */
	private String variavelNotaCfgPadraoAvaliacaoOnlineRea;
	private List<AvaliacaoOnlineTemaAssuntoVO> avaliacaoOnlineTemaAssuntoVOs;
	/**
	 * transient utilizado para lista aluno que serao gerado os calendario avaliacaoonline
	 */
	private List<HistoricoVO> listaHistoricoAluno;
	private Date dataInicioAvaliacaoFixoAntesAlteracao;
	private Date dataTerminoAvaliacaoFixoAntesAlteracao;
	private boolean excluirCalendarioAtividadeAguardandoRealizacao = false;
	private Boolean apresentarGabaritoProvaAlunoAposDataTerminoPeriodoRealizacao;
	private boolean limiteTempoProvaAlunoDentroPeriodoRealizacao = true;
	private Boolean randomizarApenasQuestoesCadastradasPeloProfessor;
	private Integer nrDiasEntreAvalicaoOnline ; 
	private Integer nrVezesPodeRepetirAvaliacaoOnline;
	
	public AvaliacaoOnlineVO clone() throws CloneNotSupportedException {
		return (AvaliacaoOnlineVO) Uteis.clonar(this);		
	}
	
	public String getPreencherStatusProgressBarVO(ProgressBarVO progressBarVO, HistoricoVO historico) {
		StringBuilder sb = new StringBuilder();
		sb.append(" Processando ").append(progressBarVO.getProgresso()).append(" de ").append(progressBarVO.getMaxValue());
		sb.append(" - (Matricula Atual = ").append(historico.getMatricula().getMatricula()).append(") ");
		return sb.toString();		
	}

	public String getNome() {
		if (nome == null) {
			nome = "";
		}
		return nome;
	}

	public DisciplinaVO getDisciplinaVO() {
		if (disciplinaVO == null) {
			disciplinaVO = new DisciplinaVO();
		}
		return disciplinaVO;
	}

	public TurmaVO getTurmaVO() {
		if (turmaVO == null) {
			turmaVO = new TurmaVO();
		}
		return turmaVO;
	}

	public void setTurmaVO(TurmaVO turmaVO) {
		this.turmaVO = turmaVO;
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

	public Date getDataCriacao() {
		if (dataCriacao == null) {
			dataCriacao = new Date();
		}
		return dataCriacao;
	}

	public UsuarioVO getReposnsavelCriacao() {
		if (reposnsavelCriacao == null) {
			reposnsavelCriacao = new UsuarioVO();
		}
		return reposnsavelCriacao;
	}

	public Date getDataAtivacao() {
		if (dataAtivacao == null) {
			dataAtivacao = new Date();
		}
		return dataAtivacao;
	}

	public UsuarioVO getReposnsavelAtivacao() {
		if (reposnsavelAtivacao == null) {
			reposnsavelAtivacao = new UsuarioVO();
		}
		return reposnsavelAtivacao;
	}

	public Date getDataInativacao() {
		if (dataInativacao == null) {
			dataInativacao = new Date();
		}
		return dataInativacao;
	}

	public UsuarioVO getReposnsavelInativacao() {
		if (reposnsavelInativacao == null) {
			reposnsavelInativacao = new UsuarioVO();
		}
		return reposnsavelInativacao;
	}

	public Integer getQuantidadeNivelQuestaoMedio() {
		if (quantidadeNivelQuestaoMedio == null) {
			quantidadeNivelQuestaoMedio = 0;
		}
		return quantidadeNivelQuestaoMedio;
	}

	public Integer getQuantidadeNivelQuestaoFacil() {
		if (quantidadeNivelQuestaoFacil == null) {
			quantidadeNivelQuestaoFacil = 0;
		}
		return quantidadeNivelQuestaoFacil;
	}

	public Integer getQuantidadeNivelQuestaoDificil() {
		if (quantidadeNivelQuestaoDificil == null) {
			quantidadeNivelQuestaoDificil = 0;
		}
		return quantidadeNivelQuestaoDificil;
	}

	public Integer getQuantidadeQualquerNivelQuestao() {
		if (quantidadeQualquerNivelQuestao == null) {
			quantidadeQualquerNivelQuestao = 0;
		}
		return quantidadeQualquerNivelQuestao;
	}

	public Double getNotaPorQuestaoNivelMedio() {
		if (notaPorQuestaoNivelMedio == null) {
			notaPorQuestaoNivelMedio = 0.00;
		}
		return notaPorQuestaoNivelMedio;
	}

	public Double getNotaPorQuestaoNivelFacil() {
		if (notaPorQuestaoNivelFacil == null) {
			notaPorQuestaoNivelFacil = 0.00;
		}
		return notaPorQuestaoNivelFacil;
	}

	public Double getNotaPorQuestaoNivelDificil() {
		if (notaPorQuestaoNivelDificil == null) {
			notaPorQuestaoNivelDificil = 0.00;
		}
		return notaPorQuestaoNivelDificil;
	}

	public Integer getPercentualAprovacao() {
		if (percentualAprovacao == null) {
			percentualAprovacao = 0;
		}
		return percentualAprovacao;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setDisciplinaVO(DisciplinaVO disciplinaVO) {
		this.disciplinaVO = disciplinaVO;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public void setReposnsavelCriacao(UsuarioVO reposnsavelCriacao) {
		this.reposnsavelCriacao = reposnsavelCriacao;
	}

	public void setDataAtivacao(Date dataAtivacao) {
		this.dataAtivacao = dataAtivacao;
	}

	public void setReposnsavelAtivacao(UsuarioVO reposnsavelAtivacao) {
		this.reposnsavelAtivacao = reposnsavelAtivacao;
	}

	public void setDataInativacao(Date dataInativacao) {
		this.dataInativacao = dataInativacao;
	}

	public void setReposnsavelInativacao(UsuarioVO reposnsavelInativacao) {
		this.reposnsavelInativacao = reposnsavelInativacao;
	}

	public void setQuantidadeNivelQuestaoMedio(Integer quantidadeNivelQuestaoMedio) {
		this.quantidadeNivelQuestaoMedio = quantidadeNivelQuestaoMedio;
	}

	public void setQuantidadeNivelQuestaoFacil(Integer quantidadeNivelQuestaoFacil) {
		this.quantidadeNivelQuestaoFacil = quantidadeNivelQuestaoFacil;
	}

	public void setQuantidadeNivelQuestaoDificil(Integer quantidadeNivelQuestaoDificil) {
		this.quantidadeNivelQuestaoDificil = quantidadeNivelQuestaoDificil;
	}

	public void setQuantidadeQualquerNivelQuestao(Integer quantidadeQualquerNivelQuestao) {
		this.quantidadeQualquerNivelQuestao = quantidadeQualquerNivelQuestao;
	}

	public void setNotaPorQuestaoNivelMedio(Double notaPorQuestaoNivelMedio) {
		this.notaPorQuestaoNivelMedio = notaPorQuestaoNivelMedio;
	}

	public void setNotaPorQuestaoNivelFacil(Double notaPorQuestaoNivelFacil) {
		this.notaPorQuestaoNivelFacil = notaPorQuestaoNivelFacil;
	}

	public void setNotaPorQuestaoNivelDificil(Double notaPorQuestaoNivelDificil) {
		this.notaPorQuestaoNivelDificil = notaPorQuestaoNivelDificil;
	}

	public void setPercentualAprovacao(Integer percentualAprovacao) {
		this.percentualAprovacao = percentualAprovacao;
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

	public TipoGeracaoProvaOnlineEnum getTipoGeracaoProvaOnline() {
		if (tipoGeracaoProvaOnline == null) {
			tipoGeracaoProvaOnline = TipoGeracaoProvaOnlineEnum.NENHUM;
		}
		return tipoGeracaoProvaOnline;
	}

	public void setTipoGeracaoProvaOnline(TipoGeracaoProvaOnlineEnum tipoGeracaoProvaOnline) {
		this.tipoGeracaoProvaOnline = tipoGeracaoProvaOnline;
	}

	public TipoUsoEnum getTipoUso() {
		if (tipoUso == null) {
			tipoUso = TipoUsoEnum.GERAL;
		}
		return tipoUso;
	}

	public void setTipoUso(TipoUsoEnum tipoUso) {
		this.tipoUso = tipoUso;
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

	public String getSituacaoEnum_Apresentar() {
		return getSituacao().getValorApresentar();
	}

	public String getTipoUso_Apresentar() {
		return getTipoUso().getValorApresentar();
	}

	public String getTipoGeracaoProvaOnline_Apresentar() {
		return getTipoGeracaoProvaOnline().getValorApresentar();
	}

	public Double getNotaPorQuestaoQualquerNivel() {
		if (notaPorQuestaoQualquerNivel == null) {
			notaPorQuestaoQualquerNivel = 0.00;
		}
		return notaPorQuestaoQualquerNivel;
	}

	public void setNotaPorQuestaoQualquerNivel(Double notaPorQuestaoQualquerNivel) {
		this.notaPorQuestaoQualquerNivel = notaPorQuestaoQualquerNivel;
	}

	public List<AvaliacaoOnlineQuestaoVO> getAvaliacaoOnlineQuestaoVOs() {
		if (avaliacaoOnlineQuestaoVOs == null) {
			avaliacaoOnlineQuestaoVOs = new ArrayList<AvaliacaoOnlineQuestaoVO>();
		}
		return avaliacaoOnlineQuestaoVOs;
	}

	public void setAvaliacaoOnlineQuestaoVOs(List<AvaliacaoOnlineQuestaoVO> avaliacaoOnlineQuestaoVOs) {
		this.avaliacaoOnlineQuestaoVOs = avaliacaoOnlineQuestaoVOs;
	}

	public Integer getNumeroQuestoes() {
		return getAvaliacaoOnlineQuestaoVOs().size();
	}

	public Double getNotaMaximaAvaliacao() {
		if (notaMaximaAvaliacao == null) {
			notaMaximaAvaliacao = 0.00;
		}
		return notaMaximaAvaliacao;
	}

	public void setNotaMaximaAvaliacao(Double notaMaximaAvaliacao) {
		this.notaMaximaAvaliacao = notaMaximaAvaliacao;
	}

	public ConteudoVO getConteudoVO() {
		if (conteudoVO == null) {
			conteudoVO = new ConteudoVO();
		}
		return conteudoVO;
	}

	public void setConteudoVO(ConteudoVO conteudoVO) {
		this.conteudoVO = conteudoVO;
	}
	
	public UnidadeConteudoVO getUnidadeConteudoVO() {
		if (unidadeConteudoVO == null) {
			unidadeConteudoVO = new UnidadeConteudoVO();
		}
		return unidadeConteudoVO;
	}

	public void setUnidadeConteudoVO(UnidadeConteudoVO unidadeConteudoVO) {
		this.unidadeConteudoVO = unidadeConteudoVO;
	}

	public Boolean getUsoExclusivoProfessor() {
		if(usoExclusivoProfessor == null) {
			usoExclusivoProfessor = false;
		}
		return usoExclusivoProfessor;
	}

	public void setUsoExclusivoProfessor(Boolean usoExclusivoProfessor) {
		this.usoExclusivoProfessor = usoExclusivoProfessor;
	}

	public PessoaVO getProfessor() {
		if(professor == null) {
			professor = new PessoaVO();
		}
		return professor;
	}

	public void setProfessor(PessoaVO professor) {
		this.professor = professor;
	}

	public PoliticaSelecaoQuestaoEnum getPoliticaSelecaoQuestaoEnum() {
		if(politicaSelecaoQuestaoEnum == null) {
			politicaSelecaoQuestaoEnum = PoliticaSelecaoQuestaoEnum.NENHUM;
		}
		return politicaSelecaoQuestaoEnum;
	}

	public void setPoliticaSelecaoQuestaoEnum(PoliticaSelecaoQuestaoEnum politicaSelecaoQuestaoEnum) {
		this.politicaSelecaoQuestaoEnum = politicaSelecaoQuestaoEnum;
	}

	public RegraDistribuicaoQuestaoEnum getRegraDistribuicaoQuestaoEnum() {
		if(regraDistribuicaoQuestaoEnum == null) {
			regraDistribuicaoQuestaoEnum = RegraDistribuicaoQuestaoEnum.NENHUM;
		}
		return regraDistribuicaoQuestaoEnum;
	}

	public void setRegraDistribuicaoQuestaoEnum(RegraDistribuicaoQuestaoEnum regraDistribuicaoQuestaoEnum) {
		this.regraDistribuicaoQuestaoEnum = regraDistribuicaoQuestaoEnum;
	}

	public Boolean getPermiteRepeticoesDeQuestoesAPartirSegundaAvaliacaoOnlineAluno() {
		if(permiteRepeticoesDeQuestoesAPartirSegundaAvaliacaoOnlineAluno == null) {
			permiteRepeticoesDeQuestoesAPartirSegundaAvaliacaoOnlineAluno = false;
		}
		return permiteRepeticoesDeQuestoesAPartirSegundaAvaliacaoOnlineAluno;
	}

	public void setPermiteRepeticoesDeQuestoesAPartirSegundaAvaliacaoOnlineAluno(Boolean permiteRepeticoesDeQuestoesAPartirSegundaAvaliacaoOnlineAluno) {
		this.permiteRepeticoesDeQuestoesAPartirSegundaAvaliacaoOnlineAluno = permiteRepeticoesDeQuestoesAPartirSegundaAvaliacaoOnlineAluno;
	}

	public ParametrosMonitoramentoAvaliacaoOnlineVO getParametrosMonitoramentoAvaliacaoOnlineVO() {
		if(parametrosMonitoramentoAvaliacaoOnlineVO == null) {
			parametrosMonitoramentoAvaliacaoOnlineVO = new ParametrosMonitoramentoAvaliacaoOnlineVO();
		}
		return parametrosMonitoramentoAvaliacaoOnlineVO;
	}

	public void setParametrosMonitoramentoAvaliacaoOnlineVO(ParametrosMonitoramentoAvaliacaoOnlineVO parametrosMonitoramentoAvaliacaoOnlineVO) {
		this.parametrosMonitoramentoAvaliacaoOnlineVO = parametrosMonitoramentoAvaliacaoOnlineVO;
	}
	
	private Integer tempoLimiteRealizacaoAvaliacaoOnline;

	public Integer getTempoLimiteRealizacaoAvaliacaoOnline() {
		if(tempoLimiteRealizacaoAvaliacaoOnline == null) {
			tempoLimiteRealizacaoAvaliacaoOnline = 0;
		}
		return tempoLimiteRealizacaoAvaliacaoOnline;
	}

	public void setTempoLimiteRealizacaoAvaliacaoOnline(Integer tempoLimiteRealizacaoAvaliacaoOnline) {
		this.tempoLimiteRealizacaoAvaliacaoOnline = tempoLimiteRealizacaoAvaliacaoOnline;
	}
	
	public String getVariavelNotaCfgPadraoAvaliacaoOnlineRea() {
		if (variavelNotaCfgPadraoAvaliacaoOnlineRea == null) {
			variavelNotaCfgPadraoAvaliacaoOnlineRea = "";
		}
		return variavelNotaCfgPadraoAvaliacaoOnlineRea;
	}

	public void setVariavelNotaCfgPadraoAvaliacaoOnlineRea(String variavelNotaCfgPadraoAvaliacaoOnlineRea) {
		this.variavelNotaCfgPadraoAvaliacaoOnlineRea = variavelNotaCfgPadraoAvaliacaoOnlineRea;
	}

	public boolean isRenderizarComboBoxPoliticaSelecaoDeQuestao() {
		return getTipoGeracaoProvaOnline().equals(TipoGeracaoProvaOnlineEnum.RANDOMICO_POR_COMPLEXIDADE);
	}

	public Integer getQtdDiasLimiteResponderAvaliacaOnline() {
		if (qtdDiasLimiteResponderAvaliacaOnline == null) {
			qtdDiasLimiteResponderAvaliacaOnline = 0;
		}
		return qtdDiasLimiteResponderAvaliacaOnline;
	}

	public void setQtdDiasLimiteResponderAvaliacaOnline(Integer qtdDiasLimiteResponderAvaliacaOnline) {
		this.qtdDiasLimiteResponderAvaliacaOnline = qtdDiasLimiteResponderAvaliacaOnline;
	}
	
	public RegraDefinicaoPeriodoAvaliacaoOnlineEnum getRegraDefinicaoPeriodoAvaliacaoOnline() {
		if (regraDefinicaoPeriodoAvaliacaoOnline == null) {
			regraDefinicaoPeriodoAvaliacaoOnline = RegraDefinicaoPeriodoAvaliacaoOnlineEnum.PERIODO_ACESSO_DISCIPLINA;
		}
		return regraDefinicaoPeriodoAvaliacaoOnline;
	}

	public void setRegraDefinicaoPeriodoAvaliacaoOnline(RegraDefinicaoPeriodoAvaliacaoOnlineEnum regraDefinicaoPeriodoAvaliacaoOnline) {
		this.regraDefinicaoPeriodoAvaliacaoOnline = regraDefinicaoPeriodoAvaliacaoOnline;
	}
	
	public Date getDataInicioAvaliacaoFixo() {
		return dataInicioAvaliacaoFixo;
	}

	public void setDataInicioAvaliacaoFixo(Date dataInicioAvaliacaoFixo) {
		this.dataInicioAvaliacaoFixo = dataInicioAvaliacaoFixo;
	}

	public Date getDataTerminoAvaliacaoFixo() {
		return dataTerminoAvaliacaoFixo;
	}

	public void setDataTerminoAvaliacaoFixo(Date dataTerminoAvaliacaoFixo) {
		this.dataTerminoAvaliacaoFixo = dataTerminoAvaliacaoFixo;
	}

	public boolean isApresentarNotaDaQuestao() {
		return apresentarNotaDaQuestao;
	}

	public void setApresentarNotaDaQuestao(boolean apresentarNotaDaQuestao) {
		this.apresentarNotaDaQuestao = apresentarNotaDaQuestao;
	}

	public Date getDataInicioAvaliacaoFixoAntesAlteracao() {
		return dataInicioAvaliacaoFixoAntesAlteracao;
	}

	public void setDataInicioAvaliacaoFixoAntesAlteracao(Date dataInicioAvaliacaoFixoAntesAlteracao) {
		this.dataInicioAvaliacaoFixoAntesAlteracao = dataInicioAvaliacaoFixoAntesAlteracao;
	}

	public Date getDataTerminoAvaliacaoFixoAntesAlteracao() {
		return dataTerminoAvaliacaoFixoAntesAlteracao;
	}

	public void setDataTerminoAvaliacaoFixoAntesAlteracao(Date dataTerminoAvaliacaoFixoAntesAlteracao) {
		this.dataTerminoAvaliacaoFixoAntesAlteracao = dataTerminoAvaliacaoFixoAntesAlteracao;
	}

	public List<HistoricoVO> getListaHistoricoAluno() {
		return listaHistoricoAluno;
	}

	public void setListaHistoricoAluno(List<HistoricoVO> listaHistoricoAluno) {
		this.listaHistoricoAluno = listaHistoricoAluno;
	}

	public boolean isExcluirCalendarioAtividadeAguardandoRealizacao() {
		return excluirCalendarioAtividadeAguardandoRealizacao;
	}

	public void setExcluirCalendarioAtividadeAguardandoRealizacao(boolean excluirCalendarioAtividadeAguardandoRealizacao) {
		this.excluirCalendarioAtividadeAguardandoRealizacao = excluirCalendarioAtividadeAguardandoRealizacao;
	}

	public Boolean getApresentarGabaritoProvaAlunoAposDataTerminoPeriodoRealizacao() {
		if (apresentarGabaritoProvaAlunoAposDataTerminoPeriodoRealizacao == null) {
			apresentarGabaritoProvaAlunoAposDataTerminoPeriodoRealizacao = Boolean.FALSE;
		}
		return apresentarGabaritoProvaAlunoAposDataTerminoPeriodoRealizacao;
	}

	public void setApresentarGabaritoProvaAlunoAposDataTerminoPeriodoRealizacao(
			Boolean apresentarGabaritoProvaAlunoAposDataTerminoPeriodoRealizacao) {
		this.apresentarGabaritoProvaAlunoAposDataTerminoPeriodoRealizacao = apresentarGabaritoProvaAlunoAposDataTerminoPeriodoRealizacao;
	}

	public boolean isLimiteTempoProvaAlunoDentroPeriodoRealizacao() {
		return limiteTempoProvaAlunoDentroPeriodoRealizacao;
	}

	public void setLimiteTempoProvaAlunoDentroPeriodoRealizacao(boolean limiteTempoProvaAlunoDentroPeriodoRealizacao) {
		this.limiteTempoProvaAlunoDentroPeriodoRealizacao = limiteTempoProvaAlunoDentroPeriodoRealizacao;
	}

	public Boolean getRandomizarApenasQuestoesCadastradasPeloProfessor() {
		if (randomizarApenasQuestoesCadastradasPeloProfessor == null) {
			randomizarApenasQuestoesCadastradasPeloProfessor = false;
		}
		return randomizarApenasQuestoesCadastradasPeloProfessor;
	}

	public void setRandomizarApenasQuestoesCadastradasPeloProfessor(Boolean randomizarApenasQuestoesCadastradasPeloProfessor) {
		this.randomizarApenasQuestoesCadastradasPeloProfessor = randomizarApenasQuestoesCadastradasPeloProfessor;
	}

	public List<AvaliacaoOnlineTemaAssuntoVO> getAvaliacaoOnlineTemaAssuntoVOs() {
		if(avaliacaoOnlineTemaAssuntoVOs == null) {
			avaliacaoOnlineTemaAssuntoVOs =  new ArrayList<AvaliacaoOnlineTemaAssuntoVO>(0);
		}
		return avaliacaoOnlineTemaAssuntoVOs;
	}

	public void setAvaliacaoOnlineTemaAssuntoVOs(List<AvaliacaoOnlineTemaAssuntoVO> avaliacaoOnlineTemaAssuntoVOs) {
		this.avaliacaoOnlineTemaAssuntoVOs = avaliacaoOnlineTemaAssuntoVOs;
	}
	
	public Boolean getPossuiTemaAssuntoSelecionado() {
		return !getAvaliacaoOnlineTemaAssuntoVOs().isEmpty() && getAvaliacaoOnlineTemaAssuntoVOs().stream().anyMatch(t -> t.getSelecionado()); 
	}
	
	public Boolean getIsPermiteInformarTemaAssunto() {
		return getTipoGeracaoProvaOnline().equals(TipoGeracaoProvaOnlineEnum.RANDOMICO_POR_COMPLEXIDADE) 
			   && getPoliticaSelecaoQuestaoEnum().equals(PoliticaSelecaoQuestaoEnum.QUALQUER_QUESTAO)
			   && !getTipoUso().isGeral() && !getTipoUso().isRea();
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
		if (nrDiasEntreAvalicaoOnline  == null) {
			nrDiasEntreAvalicaoOnline = 0;
		}
		return nrDiasEntreAvalicaoOnline;
	}

	public void setNrDiasEntreAvalicaoOnline(Integer nrDiasEntreAvalicaoOnline) {
		this.nrDiasEntreAvalicaoOnline = nrDiasEntreAvalicaoOnline;
	}
	
}
