package negocio.comuns.processosel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.processosel.enumeradores.TipoProcessamentoProvaPresencial;
import negocio.comuns.secretaria.MatriculaProvaPresencialNaoLocalizadaVO;
import negocio.comuns.secretaria.MatriculaProvaPresencialVO;
import negocio.comuns.secretaria.enumeradores.TipoAlteracaoSituacaoHistoricoEnum;


public class ResultadoProcessamentoArquivoRespostaProvaPresencialVO extends SuperVO implements Serializable {
	
	public ResultadoProcessamentoArquivoRespostaProvaPresencialVO() {
		super();
	}

	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private GabaritoVO gabaritoVO;
	private List<MatriculaProvaPresencialVO> matriculaProvaPresencialVOs;
	private List<MatriculaProvaPresencialNaoLocalizadaVO> matriculaProvaPresencialNaoLocalizadaVOs;
	private TipoProcessamentoProvaPresencial tipoProcessamentoProvaPresencialEnum;
	private ConfiguracaoAcademicoVO configuracaoAcademicoVO;
	private String variavelNota;
	private String periodicidadeCurso;
	private String ano;
	private String semestre;
	private Boolean realizarCalculoMediaLancamentoNota;
	private List<ResultadoProcessamentoProvaPresencialMotivoErroVO> listaResultadoProcessamentoProvaPresencialMotivoErroVOs;
	private String nomeArquivo;
	private UsuarioVO usuarioVO;
	private Date dataProcessamento;
	private TipoAlteracaoSituacaoHistoricoEnum tipoAlteracaoSituacaoHistorico;

	public GabaritoVO getGabaritoVO() {
		if (gabaritoVO == null) {
			gabaritoVO = new GabaritoVO();
		}
		return gabaritoVO;
	}

	public void setGabaritoVO(GabaritoVO gabaritoVO) {
		this.gabaritoVO = gabaritoVO;
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

	public List<MatriculaProvaPresencialVO> getMatriculaProvaPresencialVOs() {
		if (matriculaProvaPresencialVOs == null) {
			matriculaProvaPresencialVOs = new ArrayList<MatriculaProvaPresencialVO>(0);
		}
		return matriculaProvaPresencialVOs;
	}

	public void setMatriculaProvaPresencialVOs(List<MatriculaProvaPresencialVO> matriculaProvaPresencialVOs) {
		this.matriculaProvaPresencialVOs = matriculaProvaPresencialVOs;
	}

	public List<MatriculaProvaPresencialNaoLocalizadaVO> getMatriculaProvaPresencialNaoLocalizadaVOs() {
		if (matriculaProvaPresencialNaoLocalizadaVOs == null) {
			matriculaProvaPresencialNaoLocalizadaVOs = new ArrayList<MatriculaProvaPresencialNaoLocalizadaVO>(0);
		}
		return matriculaProvaPresencialNaoLocalizadaVOs;
	}

	public void setMatriculaProvaPresencialNaoLocalizadaVOs(List<MatriculaProvaPresencialNaoLocalizadaVO> matriculaProvaPresencialNaoLocalizadaVOs) {
		this.matriculaProvaPresencialNaoLocalizadaVOs = matriculaProvaPresencialNaoLocalizadaVOs;
	}

	public TipoProcessamentoProvaPresencial getTipoProcessamentoProvaPresencialEnum() {
		if (tipoProcessamentoProvaPresencialEnum == null) {
			tipoProcessamentoProvaPresencialEnum = TipoProcessamentoProvaPresencial.LEITURA_ARQUIVO_NOTA_LANCADA;
		}
		return tipoProcessamentoProvaPresencialEnum;
	}

	public void setTipoProcessamentoProvaPresencialEnum(TipoProcessamentoProvaPresencial tipoProcessamentoProvaPresencialEnum) {
		this.tipoProcessamentoProvaPresencialEnum = tipoProcessamentoProvaPresencialEnum;
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

	public String getPeriodicidadeCurso() {
		if (periodicidadeCurso == null) {
			periodicidadeCurso = "SE";
		}
		return periodicidadeCurso;
	}

	public void setPeriodicidadeCurso(String periodicidadeCurso) {
		this.periodicidadeCurso = periodicidadeCurso;
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

	public Boolean getRealizarCalculoMediaLancamentoNota() {
		if (realizarCalculoMediaLancamentoNota == null) {
			realizarCalculoMediaLancamentoNota = Boolean.TRUE;
		}
		return realizarCalculoMediaLancamentoNota;
	}

	public void setRealizarCalculoMediaLancamentoNota(Boolean realizarCalculoMediaLancamentoNota) {
		this.realizarCalculoMediaLancamentoNota = realizarCalculoMediaLancamentoNota;
	}
	
	public Integer getQuantidadeDisciplinaLocalizada() {
		Integer quantidade = 0;
		for (MatriculaProvaPresencialVO matriculaProvaPresencialVO : getMatriculaProvaPresencialVOs()) {
			quantidade = quantidade + matriculaProvaPresencialVO.getQuantidadeDisciplinaLocalizada();
		}
		return quantidade;
	}

	/**
	 * @return the listaResultadoProcessamentoProvaPresencialMotivoErroVOs
	 */
	public List<ResultadoProcessamentoProvaPresencialMotivoErroVO> getListaResultadoProcessamentoProvaPresencialMotivoErroVOs() {
		if (listaResultadoProcessamentoProvaPresencialMotivoErroVOs == null) {
			listaResultadoProcessamentoProvaPresencialMotivoErroVOs = new ArrayList<ResultadoProcessamentoProvaPresencialMotivoErroVO>(0);
		}
		return listaResultadoProcessamentoProvaPresencialMotivoErroVOs;
	}

	/**
	 * @param listaResultadoProcessamentoProvaPresencialMotivoErroVOs the listaResultadoProcessamentoProvaPresencialMotivoErroVOs to set
	 */
	public void setListaResultadoProcessamentoProvaPresencialMotivoErroVOs(List<ResultadoProcessamentoProvaPresencialMotivoErroVO> listaResultadoProcessamentoProvaPresencialMotivoErroVOs) {
		this.listaResultadoProcessamentoProvaPresencialMotivoErroVOs = listaResultadoProcessamentoProvaPresencialMotivoErroVOs;
	}

	public String getNomeArquivo() {
		if (nomeArquivo == null) {
			nomeArquivo = "";
		}
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

	public UsuarioVO getUsuarioVO() {
		if (usuarioVO == null) {
			usuarioVO = new UsuarioVO();
		}
		return usuarioVO;
	}

	public void setUsuarioVO(UsuarioVO usuarioVO) {
		this.usuarioVO = usuarioVO;
	}

	public Date getDataProcessamento() {
		if (dataProcessamento == null) {
			dataProcessamento = new Date();
		}
		return dataProcessamento;
	}

	public void setDataProcessamento(Date dataProcessamento) {
		this.dataProcessamento = dataProcessamento;
	}

	public TipoAlteracaoSituacaoHistoricoEnum getTipoAlteracaoSituacaoHistorico() {
		if (tipoAlteracaoSituacaoHistorico == null) {
			tipoAlteracaoSituacaoHistorico = TipoAlteracaoSituacaoHistoricoEnum.TODOS_HISTORICOS;
		}
		return tipoAlteracaoSituacaoHistorico;
	}

	public void setTipoAlteracaoSituacaoHistorico(TipoAlteracaoSituacaoHistoricoEnum tipoAlteracaoSituacaoHistorico) {
		this.tipoAlteracaoSituacaoHistorico = tipoAlteracaoSituacaoHistorico;
	}
}
