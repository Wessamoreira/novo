package negocio.comuns.processosel;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.PeriodoLetivoVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.academico.enumeradores.TipoCalculoGabaritoEnum;
import negocio.comuns.academico.enumeradores.TipoGabaritoEnum;
import negocio.comuns.academico.enumeradores.TipoRespostaGabaritoEnum;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.StreamSeiException;

public class GabaritoVO extends SuperVO {

	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private String descricao;
	private Integer quantidadeQuestao;
	private UsuarioVO responsavel;
	private List<GabaritoRespostaVO> gabaritoRespostaVOs;

	// Atributo não gravado em banco.
	@SuppressWarnings("unused")
	private Integer qtdeColuna;
	private List<ColunaGabaritoVO> colunaGabaritoVOs;

	/**
	 * Os campos abaixo serão utilizados apenas no caso de prova presencial, no caso
	 * de processo seletivo não será utilizado.
	 */
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private CursoVO cursoVO;
	private GradeCurricularVO gradeCurricularVO;
	private PeriodoLetivoVO periodoLetivoVO;
	private TurnoVO turnoVO;
	private ConfiguracaoAcademicoVO configuracaoAcademicoVO;
	private String variavelNota;
	private TipoGabaritoEnum TipoGabaritoEnum;
	private TipoRespostaGabaritoEnum TipoRespostaGabaritoEnum;
	private Boolean realizarCalculoMediaLancamentoNota;
	private String ano;
	private String semestre;
	private TipoCalculoGabaritoEnum tipoCalculoGabaritoEnum;
	private Integer tamanhoNrMatriculaArquivo;
	private Boolean controlarGabaritoPorDisciplina;
	private GrupoDisciplinaProcSeletivoVO grupoDisciplinaProcSeletivoVO;

	public GabaritoVO() {

	}

	public GabaritoVO getClone() {
		try {
			GabaritoVO obj = (GabaritoVO) super.clone();
			obj.setCodigo(0);
			obj.setNovoObj(true);
			obj.setDescricao(getDescricao() + "-Clonado");
			obj.setResponsavel((UsuarioVO) getResponsavel().clone());
			obj.setUnidadeEnsinoVO((UnidadeEnsinoVO) getUnidadeEnsinoVO().clone());
			obj.setCursoVO((CursoVO) getCursoVO().clone());
			obj.setGradeCurricularVO((GradeCurricularVO) getGradeCurricularVO().clone());
			obj.setPeriodoLetivoVO((PeriodoLetivoVO) getPeriodoLetivoVO().clone());
			obj.setTurnoVO((TurnoVO) getTurnoVO().clone());
			obj.setConfiguracaoAcademicoVO(getConfiguracaoAcademicoVO());
			obj.setGrupoDisciplinaProcSeletivoVO((GrupoDisciplinaProcSeletivoVO) getGrupoDisciplinaProcSeletivoVO().clone());
			
			obj.getGabaritoRespostaVOs().clear();

			for (ColunaGabaritoVO colunaGabaritoVO : colunaGabaritoVOs) {
				for (GabaritoRespostaVO gabaritoRespostaVO : colunaGabaritoVO.getGabaritoRespostaVOs()) {
					gabaritoRespostaVO.setCodigo(0);
					gabaritoRespostaVO.setAnulado(false);
					gabaritoRespostaVO.setHistoricoAnulado("");
					obj.getGabaritoRespostaVOs().add(gabaritoRespostaVO);
				}
			}

			return obj;
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
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

	public Integer getQuantidadeQuestao() {
		if (quantidadeQuestao == null) {
			quantidadeQuestao = 0;
		}
		return quantidadeQuestao;
	}

	public void setQuantidadeQuestao(Integer quantidadeQuestao) {
		this.quantidadeQuestao = quantidadeQuestao;
	}

	public List<GabaritoRespostaVO> getGabaritoRespostaVOs() {
		if (gabaritoRespostaVOs == null) {
			gabaritoRespostaVOs = new ArrayList<GabaritoRespostaVO>(0);
		}
		return gabaritoRespostaVOs;
	}

	public void setGabaritoRespostaVOs(List<GabaritoRespostaVO> gabaritoRespostaVOs) {
		this.gabaritoRespostaVOs = gabaritoRespostaVOs;
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

	public UsuarioVO getResponsavel() {
		if (responsavel == null) {
			responsavel = new UsuarioVO();
		}
		return responsavel;
	}

	public void setResponsavel(UsuarioVO responsavel) {
		this.responsavel = responsavel;
	}

	public Integer getQtdeColuna() {
		return getColunaGabaritoVOs().size();
	}

	public void setQtdeColuna(Integer qtdeColuna) {
		this.qtdeColuna = qtdeColuna;
	}

	public List<ColunaGabaritoVO> getColunaGabaritoVOs() {
		if (colunaGabaritoVOs == null) {
			colunaGabaritoVOs = new ArrayList<ColunaGabaritoVO>(0);
		}
		return colunaGabaritoVOs;
	}

	public void setColunaGabaritoVOs(List<ColunaGabaritoVO> colunaGabaritoVOs) {
		this.colunaGabaritoVOs = colunaGabaritoVOs;
	}

	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if (unidadeEnsinoVO == null) {
			unidadeEnsinoVO = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoVO;
	}

	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}

	public CursoVO getCursoVO() {
		if (cursoVO == null) {
			cursoVO = new CursoVO();
		}
		return cursoVO;
	}

	public void setCursoVO(CursoVO cursoVO) {
		this.cursoVO = cursoVO;
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

	public PeriodoLetivoVO getPeriodoLetivoVO() {
		if (periodoLetivoVO == null) {
			periodoLetivoVO = new PeriodoLetivoVO();
		}
		return periodoLetivoVO;
	}

	public void setPeriodoLetivoVO(PeriodoLetivoVO periodoLetivoVO) {
		this.periodoLetivoVO = periodoLetivoVO;
	}

	public TurnoVO getTurnoVO() {
		if (turnoVO == null) {
			turnoVO = new TurnoVO();
		}
		return turnoVO;
	}

	public void setTurnoVO(TurnoVO turnoVO) {
		this.turnoVO = turnoVO;
	}

	public ConfiguracaoAcademicoVO getConfiguracaoAcademicoVO() {
		if (configuracaoAcademicoVO == null) {
			configuracaoAcademicoVO = new ConfiguracaoAcademicoVO();
		}
		return configuracaoAcademicoVO;
	}

	public void setConfiguracaoAcademicoVO(ConfiguracaoAcademicoVO configuracaoAcademicoVO) {
		this.configuracaoAcademicoVO = configuracaoAcademicoVO;
	}

	public String getVariavelNota() {
		if (variavelNota == null) {
			variavelNota = "";
		}
		return variavelNota;
	}

	public void setVariavelNota(String variavelNota) {
		this.variavelNota = variavelNota;
	}

	public TipoGabaritoEnum getTipoGabaritoEnum() {
		if (TipoGabaritoEnum == null) {
			TipoGabaritoEnum = TipoGabaritoEnum.PROCESSO_SELETIVO;
		}
		return TipoGabaritoEnum;
	}

	public void setTipoGabaritoEnum(TipoGabaritoEnum tipoGabaritoEnum) {
		TipoGabaritoEnum = tipoGabaritoEnum;
	}

	public Boolean getRealizarCalculoMediaLancamentoNota() {
		if (realizarCalculoMediaLancamentoNota == null) {
			realizarCalculoMediaLancamentoNota = Boolean.FALSE;
		}
		return realizarCalculoMediaLancamentoNota;
	}

	public void setRealizarCalculoMediaLancamentoNota(Boolean realizarCalculoMediaLancamentoNota) {
		this.realizarCalculoMediaLancamentoNota = realizarCalculoMediaLancamentoNota;
	}

	public TipoRespostaGabaritoEnum getTipoRespostaGabaritoEnum() {
		if (TipoRespostaGabaritoEnum == null) {
			TipoRespostaGabaritoEnum = TipoRespostaGabaritoEnum.DISCIPLINA;
		}
		return TipoRespostaGabaritoEnum;
	}

	public void setTipoRespostaGabaritoEnum(TipoRespostaGabaritoEnum tipoRespostaGabaritoEnum) {
		TipoRespostaGabaritoEnum = tipoRespostaGabaritoEnum;
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

	public TipoCalculoGabaritoEnum getTipoCalculoGabaritoEnum() {
		if (tipoCalculoGabaritoEnum == null) {
			tipoCalculoGabaritoEnum = TipoCalculoGabaritoEnum.INDIVIDUAL_DISCIPLINA;
		}
		return tipoCalculoGabaritoEnum;
	}

	public void setTipoCalculoGabaritoEnum(TipoCalculoGabaritoEnum tipoCalculoGabaritoEnum) {
		this.tipoCalculoGabaritoEnum = tipoCalculoGabaritoEnum;
	}

	public Integer getTamanhoNrMatriculaArquivo() {
		if (tamanhoNrMatriculaArquivo == null) {
			tamanhoNrMatriculaArquivo = 0;
		}
		return tamanhoNrMatriculaArquivo;
	}

	public void setTamanhoNrMatriculaArquivo(Integer tamanhoNrMatriculaArquivo) {
		this.tamanhoNrMatriculaArquivo = tamanhoNrMatriculaArquivo;
	}

	public Boolean getControlarGabaritoPorDisciplina() {
		if (controlarGabaritoPorDisciplina == null) {
			controlarGabaritoPorDisciplina = false;
		}
		return controlarGabaritoPorDisciplina;
	}

	public void setControlarGabaritoPorDisciplina(Boolean controlarGabaritoPorDisciplina) {
		this.controlarGabaritoPorDisciplina = controlarGabaritoPorDisciplina;
	}

	public GrupoDisciplinaProcSeletivoVO getGrupoDisciplinaProcSeletivoVO() {
		if (grupoDisciplinaProcSeletivoVO == null) {
			grupoDisciplinaProcSeletivoVO = new GrupoDisciplinaProcSeletivoVO();
		}
		return grupoDisciplinaProcSeletivoVO;
	}

	public void setGrupoDisciplinaProcSeletivoVO(GrupoDisciplinaProcSeletivoVO grupoDisciplinaProcSeletivoVO) {
		this.grupoDisciplinaProcSeletivoVO = grupoDisciplinaProcSeletivoVO;
	}
}