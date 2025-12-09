package negocio.comuns.ead;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.academico.ConteudoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.enumeradores.PeriodoDisponibilizacaoListaExercicioEnum;
import negocio.comuns.ead.enumeradores.PoliticaSelecaoQuestaoEnum;
import negocio.comuns.ead.enumeradores.RegraDistribuicaoQuestaoEnum;
import negocio.comuns.ead.enumeradores.SituacaoListaExercicioEnum;
import negocio.comuns.ead.enumeradores.TipoGeracaoListaExercicioEnum;
import negocio.comuns.utilitarias.Uteis;

public class ListaExercicioVO extends SuperVO {

	private Integer codigo;
	private SituacaoListaExercicioEnum situacaoListaExercicio;
	private String descricao;
	private Date dataCriacao;
	private Date dataAlteracao;
	private UsuarioVO responsavelCriacao;
	private UsuarioVO responsavelAlteracao;
	private TurmaVO turma;
	private DisciplinaVO disciplina;
	private TipoGeracaoListaExercicioEnum tipoGeracaoListaExercicio;
	private PeriodoDisponibilizacaoListaExercicioEnum periodoDisponibilizacaoListaExercicio;
	private Date liberarDia;
	private Date encerrarDia;
	private Integer quantidadeNivelQuestaoMedio;
	private Integer quantidadeNivelQuestaoFacil;
	private Integer quantidadeNivelQuestaoDificil;
	private Integer quantidadeQualquerNivelQuestao;
	private List<QuestaoListaExercicioVO> questaoListaExercicioVOs;
	private PoliticaSelecaoQuestaoEnum politicaSelecaoQuestaoEnum;
	private RegraDistribuicaoQuestaoEnum regraDistribuicaoQuestaoEnum;
	private Boolean randomizarApenasQuestoesCadastradasPeloProfessor;
	/**
	 * @author Victor Hugo 09/01/2015
	 */
	private ConteudoVO conteudoVO;

	/**
	 * Variaveis Transientes
	 */
	private Integer numeroAcertos;
	private Integer numeroErros;

	private Boolean conteudoAlterado;

	public ListaExercicioVO clone() throws CloneNotSupportedException {
		ListaExercicioVO clone = (ListaExercicioVO) super.clone();
		clone.setCodigo(0);
		clone.setNovoObj(true);
		clone.setResponsavelCriacao(new UsuarioVO());
		clone.setDataCriacao(new Date());
		clone.setDescricao(this.getDescricao() + " - Clone");
		clone.setSituacaoListaExercicio(SituacaoListaExercicioEnum.EM_ELABORACAO);
		clone.setQuestaoListaExercicioVOs(new ArrayList<QuestaoListaExercicioVO>(0));
		for (QuestaoListaExercicioVO questaoListaExercicioVO : this.getQuestaoListaExercicioVOs()) {
			QuestaoListaExercicioVO cloneQuestaoListaExercicio = questaoListaExercicioVO.clone();
			cloneQuestaoListaExercicio.setListaExercicio(clone);
			clone.getQuestaoListaExercicioVOs().add(cloneQuestaoListaExercicio);
		}
		return clone;
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

	public SituacaoListaExercicioEnum getSituacaoListaExercicio() {
		if(situacaoListaExercicio == null) {
			situacaoListaExercicio = SituacaoListaExercicioEnum.EM_ELABORACAO;
		}
		return situacaoListaExercicio;
	}

	public void setSituacaoListaExercicio(SituacaoListaExercicioEnum situacaoListaExercicio) {
		this.situacaoListaExercicio = situacaoListaExercicio;
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

	public Date getDataCriacao() {
		if (dataCriacao == null) {
			dataCriacao = new Date();
		}
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public Date getDataAlteracao() {
		return dataAlteracao;
	}

	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}

	public UsuarioVO getResponsavelCriacao() {
		if (responsavelCriacao == null) {
			responsavelCriacao = new UsuarioVO();
		}
		return responsavelCriacao;
	}

	public void setResponsavelCriacao(UsuarioVO responsavelCriacao) {
		this.responsavelCriacao = responsavelCriacao;
	}

	public UsuarioVO getResponsavelAlteracao() {
		if (responsavelAlteracao == null) {
			responsavelAlteracao = new UsuarioVO();
		}
		return responsavelAlteracao;
	}

	public void setResponsavelAlteracao(UsuarioVO responsavelAlteracao) {
		this.responsavelAlteracao = responsavelAlteracao;
	}

	public TurmaVO getTurma() {
		if (turma == null) {
			turma = new TurmaVO();
		}
		return turma;
	}

	public void setTurma(TurmaVO turma) {
		this.turma = turma;
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

	public TipoGeracaoListaExercicioEnum getTipoGeracaoListaExercicio() {
		if(tipoGeracaoListaExercicio == null) {
			tipoGeracaoListaExercicio = TipoGeracaoListaExercicioEnum.RANDOMICO;
		}
		return tipoGeracaoListaExercicio;
	}

	public void setTipoGeracaoListaExercicio(TipoGeracaoListaExercicioEnum tipoGeracaoListaExercicio) {
		this.tipoGeracaoListaExercicio = tipoGeracaoListaExercicio;
	}

	public PeriodoDisponibilizacaoListaExercicioEnum getPeriodoDisponibilizacaoListaExercicio() {
		return periodoDisponibilizacaoListaExercicio;
	}

	public void setPeriodoDisponibilizacaoListaExercicio(PeriodoDisponibilizacaoListaExercicioEnum periodoDisponibilizacaoListaExercicio) {
		this.periodoDisponibilizacaoListaExercicio = periodoDisponibilizacaoListaExercicio;
	}

	public Integer getQuantidadeNivelQuestaoMedio() {
		if (quantidadeNivelQuestaoMedio == null) {
			quantidadeNivelQuestaoMedio = 0;
		}
		return quantidadeNivelQuestaoMedio;
	}

	public void setQuantidadeNivelQuestaoMedio(Integer quantidadeNivelQuestaoMedio) {
		this.quantidadeNivelQuestaoMedio = quantidadeNivelQuestaoMedio;
	}

	public Integer getQuantidadeNivelQuestaoFacil() {
		if (quantidadeNivelQuestaoFacil == null) {
			quantidadeNivelQuestaoFacil = 0;
		}
		return quantidadeNivelQuestaoFacil;
	}

	public void setQuantidadeNivelQuestaoFacil(Integer quantidadeNivelQuestaoFacil) {
		this.quantidadeNivelQuestaoFacil = quantidadeNivelQuestaoFacil;
	}

	public Integer getQuantidadeNivelQuestaoDificil() {
		if (quantidadeNivelQuestaoDificil == null) {
			quantidadeNivelQuestaoDificil = 0;
		}
		return quantidadeNivelQuestaoDificil;
	}

	public void setQuantidadeNivelQuestaoDificil(Integer quantidadeNivelQuestaoDificil) {
		this.quantidadeNivelQuestaoDificil = quantidadeNivelQuestaoDificil;
	}

	public List<QuestaoListaExercicioVO> getQuestaoListaExercicioVOs() {
		if (questaoListaExercicioVOs == null) {
			questaoListaExercicioVOs = new ArrayList<QuestaoListaExercicioVO>(0);
		}
		return questaoListaExercicioVOs;
	}

	public void setQuestaoListaExercicioVOs(List<QuestaoListaExercicioVO> questaoListaExercicioVOs) {
		this.questaoListaExercicioVOs = questaoListaExercicioVOs;
	}

	public Date getLiberarDia() {
		return liberarDia;
	}

	public void setLiberarDia(Date liberarDia) {
		this.liberarDia = liberarDia;
	}

	public Date getEncerrarDia() {
		return encerrarDia;
	}

	public void setEncerrarDia(Date encerrarDia) {
		this.encerrarDia = encerrarDia;
	}

	public Integer getQuantidadeQualquerNivelQuestao() {
		if (quantidadeQualquerNivelQuestao == null) {
			quantidadeQualquerNivelQuestao = 0;
		}
		return quantidadeQualquerNivelQuestao;
	}

	public void setQuantidadeQualquerNivelQuestao(Integer quantidadeQualquerNivelQuestao) {
		this.quantidadeQualquerNivelQuestao = quantidadeQualquerNivelQuestao;
	}

	public String getPeriodoDisponibilizacaoApresentar() {
		return getPeriodoDisponibilizacaoListaExercicio() != null && getPeriodoDisponibilizacaoListaExercicio().equals(PeriodoDisponibilizacaoListaExercicioEnum.INDETERMINADO) ? PeriodoDisponibilizacaoListaExercicioEnum.INDETERMINADO.getValorApresentar() : (Uteis.getData(getLiberarDia()) + " até " + Uteis.getData(getEncerrarDia()));
	}

	public Integer getNumeroQuestoes() {
		return getQuestaoListaExercicioVOs().size();
	}

	public Integer getNumeroAcertos() {
		if (numeroAcertos == null) {
			numeroAcertos = 0;
		}
		return numeroAcertos;
	}

	public void setNumeroAcertos(Integer numeroAcertos) {
		this.numeroAcertos = numeroAcertos;
	}

	public Integer getNumeroErros() {
		if (numeroErros == null) {
			numeroErros = 0;
		}
		return numeroErros;
	}

	public void setNumeroErros(Integer numeroErros) {
		this.numeroErros = numeroErros;
	}

	public String getPorcentagemAcertos() {
		return Uteis.getDoubleFormatado(Uteis.arrendondarForcando2CadasDecimais((getNumeroAcertos().doubleValue() * 100) / getQuestaoListaExercicioVOs().size())) + "%";
	}

	public String getPorcentagemErros() {
		return Uteis.getDoubleFormatado(Uteis.arrendondarForcando2CadasDecimais((getNumeroErros().doubleValue() * 100) / getQuestaoListaExercicioVOs().size())) + "%";
	}

	public Boolean getConteudoAlterado() {
		if (conteudoAlterado == null) {
			conteudoAlterado = false;
		}
		return conteudoAlterado;
	}

	public void setConteudoAlterado(Boolean conteudoAlterado) {
		this.conteudoAlterado = conteudoAlterado;
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

	public PoliticaSelecaoQuestaoEnum getPoliticaSelecaoQuestaoEnum() {
		if(politicaSelecaoQuestaoEnum == null) {
			politicaSelecaoQuestaoEnum = PoliticaSelecaoQuestaoEnum.QUALQUER_QUESTAO;
		}
		return politicaSelecaoQuestaoEnum;
	}

	public void setPoliticaSelecaoQuestaoEnum(PoliticaSelecaoQuestaoEnum politicaSelecaoQuestaoEnum) {
		this.politicaSelecaoQuestaoEnum = politicaSelecaoQuestaoEnum;
	}

	public RegraDistribuicaoQuestaoEnum getRegraDistribuicaoQuestaoEnum() {
		if(regraDistribuicaoQuestaoEnum == null) {
			regraDistribuicaoQuestaoEnum = RegraDistribuicaoQuestaoEnum.QUANTIDADE_FIXA_ASSUNTO;
		}
		return regraDistribuicaoQuestaoEnum;
	}

	public void setRegraDistribuicaoQuestaoEnum(RegraDistribuicaoQuestaoEnum regraDistribuicaoQuestaoEnum) {
		this.regraDistribuicaoQuestaoEnum = regraDistribuicaoQuestaoEnum;
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
}
