package negocio.comuns.ead;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.ead.enumeradores.SituacaoAvaliacaoOnlineMatriculaEnum;
import negocio.comuns.utilitarias.Uteis;

/**
 * @author Victor Hugo 10/10/2014
 */
public class AvaliacaoOnlineMatriculaVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private MatriculaVO matriculaVO;
	private MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO;
	private AvaliacaoOnlineVO avaliacaoOnlineVO;
	private Double nota;
	private SituacaoAvaliacaoOnlineMatriculaEnum situacaoAvaliacaoOnlineMatriculaEnum;
	private Date dataInicio;
	private Date dataLimiteTermino;
	private Date dataTermino;
	private Integer quantidadeAcertos;
	private Integer quantidadeErros;
	private Integer quantidadeNaoRespondida;
	private Double percentualAcerto;
	private Double percentualErro;
	private Double percentualNaoRespondido;
	private ConfiguracaoEADVO configuracaoEADVO;
	private List<AvaliacaoOnlineMatriculaQuestaoVO> avaliacaoOnlineMatriculaQuestaoVOs;
	/*
	 * Variável criada para verificar se a nota foi lançada no histórico se foi
	 * lançada, a mesma estará setado true.
	 */
	private Boolean notaLancadaNoHistorico;
	/*
	 * Transient
	 */
	public Integer quantidadeQuestaoMarcadas;
	private List<GraficoAproveitamentoAvaliacaoVO> graficoAproveitamentoAvaliacaoVOs;
	private String naoAcertouOuErrouNenhumaQuestao;
	private Date dataInicioLiberacao;
	private Date dataFimLiberacao;
	private Integer tamanhoListaAvaliacaoOnlineMatriculaQuestao;
	private Integer qtdeQuestaoFacil;
	private Integer qtdeQuestaoMedio;
	private Integer qtdeQuestaoDificil;
	private Boolean apresentarResultadoAvaliacaoOnline;
	private Integer codigoPrimeiraQuestaoNaoRespondida;
	/*
	 * Fim Transient
	 */

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public MatriculaVO getMatriculaVO() {
		if (matriculaVO == null) {
			matriculaVO = new MatriculaVO();
		}
		return matriculaVO;
	}

	public void setMatriculaVO(MatriculaVO matriculaVO) {
		this.matriculaVO = matriculaVO;
	}

	public MatriculaPeriodoTurmaDisciplinaVO getMatriculaPeriodoTurmaDisciplinaVO() {
		if (matriculaPeriodoTurmaDisciplinaVO == null) {
			matriculaPeriodoTurmaDisciplinaVO = new MatriculaPeriodoTurmaDisciplinaVO();
		}
		return matriculaPeriodoTurmaDisciplinaVO;
	}

	public void setMatriculaPeriodoTurmaDisciplinaVO(
			MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO) {
		this.matriculaPeriodoTurmaDisciplinaVO = matriculaPeriodoTurmaDisciplinaVO;
	}

	public AvaliacaoOnlineVO getAvaliacaoOnlineVO() {
		if (avaliacaoOnlineVO == null) {
			avaliacaoOnlineVO = new AvaliacaoOnlineVO();
		}
		return avaliacaoOnlineVO;
	}

	public void setAvaliacaoOnlineVO(AvaliacaoOnlineVO avaliacaoOnlineVO) {
		this.avaliacaoOnlineVO = avaliacaoOnlineVO;
	}

	public Double getNota() {
		if (nota == null) {
			nota = 0.00;
		}
		return nota;
	}

	public void setNota(Double nota) {
		this.nota = nota;
	}

	public SituacaoAvaliacaoOnlineMatriculaEnum getSituacaoAvaliacaoOnlineMatriculaEnum() {
		if (situacaoAvaliacaoOnlineMatriculaEnum == null) {
			situacaoAvaliacaoOnlineMatriculaEnum = SituacaoAvaliacaoOnlineMatriculaEnum.AGUARDANDO_REALIZACAO;
		}
		return situacaoAvaliacaoOnlineMatriculaEnum;
	}
	
	public String getStyleSituacao() {
		if (getSituacaoAvaliacaoOnlineMatriculaEnum().equals(SituacaoAvaliacaoOnlineMatriculaEnum.AGUARDANDO_REALIZACAO)) {
			return "text-warning";
		} else if (getSituacaoAvaliacaoOnlineMatriculaEnum().equals(SituacaoAvaliacaoOnlineMatriculaEnum.AGUARDANDO_DATA_LIBERACAO)) {
			return "text-warning";
		} else if (getSituacaoAvaliacaoOnlineMatriculaEnum().equals(SituacaoAvaliacaoOnlineMatriculaEnum.APROVADO)) {
			return "text-success";
		} else if (getSituacaoAvaliacaoOnlineMatriculaEnum().equals(SituacaoAvaliacaoOnlineMatriculaEnum.EM_REALIZACAO)) {
			return "text-info";
		} else if (getSituacaoAvaliacaoOnlineMatriculaEnum().equals(SituacaoAvaliacaoOnlineMatriculaEnum.EXPIRADO)) {
			return "text-danger";
		} else if (getSituacaoAvaliacaoOnlineMatriculaEnum().equals(SituacaoAvaliacaoOnlineMatriculaEnum.REPROVADO)) {
			return "text-danger";
		}
		return "text-default";
	}

	public void setSituacaoAvaliacaoOnlineMatriculaEnum(
			SituacaoAvaliacaoOnlineMatriculaEnum situacaoAvaliacaoOnlineMatriculaEnum) {
		this.situacaoAvaliacaoOnlineMatriculaEnum = situacaoAvaliacaoOnlineMatriculaEnum;
	}

	public Date getDataInicio() {
		if (dataInicio == null) {
			dataInicio = new Date();
		}
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataLimiteTermino() {
		if (dataLimiteTermino == null) {
			dataLimiteTermino = new Date();
		}
		return dataLimiteTermino;
	}

	public void setDataLimiteTermino(Date dataLimiteTermino) {
		this.dataLimiteTermino = dataLimiteTermino;
	}

	public Date getDataTermino() {
		if (dataTermino == null) {
			dataTermino = new Date();
		}
		return dataTermino;
	}

	public void setDataTermino(Date dataTermino) {
		this.dataTermino = dataTermino;
	}

	public Integer getQuantidadeAcertos() {
		if (quantidadeAcertos == null) {
			quantidadeAcertos = 0;
		}
		return quantidadeAcertos;
	}

	public void setQuantidadeAcertos(Integer quantidadeAcertos) {
		this.quantidadeAcertos = quantidadeAcertos;
	}

	public Integer getQuantidadeErros() {
		if (quantidadeErros == null) {
			quantidadeErros = 0;
		}
		return quantidadeErros;
	}

	public void setQuantidadeErros(Integer quantidadeErros) {
		this.quantidadeErros = quantidadeErros;
	}

	public Integer getQuantidadeNaoRespondida() {
		if (quantidadeNaoRespondida == null) {
			quantidadeNaoRespondida = 0;
		}
		return quantidadeNaoRespondida;
	}

	public void setQuantidadeNaoRespondida(Integer quantidadeNaoRespondida) {
		this.quantidadeNaoRespondida = quantidadeNaoRespondida;
	}

	public Double getPercentualAcerto() {
		if (percentualAcerto == null) {
			percentualAcerto = 0.00;
		}
		return percentualAcerto;
	}

	public void setPercentualAcerto(Double percentualAcerto) {
		this.percentualAcerto = percentualAcerto;
	}

	public Double getPercentualErro() {
		if (percentualErro == null) {
			percentualErro = 0.00;
		}
		return percentualErro;
	}

	public void setPercentualErro(Double percentualErro) {
		this.percentualErro = percentualErro;
	}

	public Double getPercentualNaoRespondido() {
		if (percentualNaoRespondido == null) {
			percentualNaoRespondido = 0.00;
		}
		return percentualNaoRespondido;
	}

	public void setPercentualNaoRespondido(Double percentualNaoRespondido) {
		this.percentualNaoRespondido = percentualNaoRespondido;
	}

	public String getSituacaoAvaliacaoOnlineMatriculaEnum_Apresentar() {
		return getSituacaoAvaliacaoOnlineMatriculaEnum().getValorApresentar();
	}

	public boolean getIsApresentarIniciarAvaliacaoOnline() {
		return getDataInicioLiberacao() != null && getDataFimLiberacao() != null
				&& (getDataInicioLiberacao().compareTo(new Date()) <= 0
						|| Uteis.getData(getDataInicioLiberacao()).equals(Uteis.getData(new Date())))
				&& (getDataFimLiberacao().compareTo(new Date()) >= 0
						|| Uteis.getData(getDataFimLiberacao()).equals(Uteis.getData(new Date())))
				&& (getSituacaoAvaliacaoOnlineMatriculaEnum()
						.equals(SituacaoAvaliacaoOnlineMatriculaEnum.AGUARDANDO_REALIZACAO));
	}

	public boolean isAvaliacaoOnlineNaoRealizada() {
		return getSituacaoAvaliacaoOnlineMatriculaEnum()
				.equals(SituacaoAvaliacaoOnlineMatriculaEnum.AGUARDANDO_DATA_LIBERACAO)
				|| getSituacaoAvaliacaoOnlineMatriculaEnum()
						.equals(SituacaoAvaliacaoOnlineMatriculaEnum.AGUARDANDO_REALIZACAO)
				|| getSituacaoAvaliacaoOnlineMatriculaEnum().equals(SituacaoAvaliacaoOnlineMatriculaEnum.EM_REALIZACAO)
				|| getSituacaoAvaliacaoOnlineMatriculaEnum().equals(SituacaoAvaliacaoOnlineMatriculaEnum.EXPIRADO);
	}

	public ConfiguracaoEADVO getConfiguracaoEADVO() {
		if (configuracaoEADVO == null) {
			configuracaoEADVO = new ConfiguracaoEADVO();
		}
		return configuracaoEADVO;
	}

	public void setConfiguracaoEADVO(ConfiguracaoEADVO configuracaoEADVO) {
		this.configuracaoEADVO = configuracaoEADVO;
	}

	public List<AvaliacaoOnlineMatriculaQuestaoVO> getAvaliacaoOnlineMatriculaQuestaoVOs() {
		if (avaliacaoOnlineMatriculaQuestaoVOs == null) {
			avaliacaoOnlineMatriculaQuestaoVOs = new ArrayList<AvaliacaoOnlineMatriculaQuestaoVO>();
		}
		return avaliacaoOnlineMatriculaQuestaoVOs;
	}

	public void setAvaliacaoOnlineMatriculaQuestaoVOs(
			List<AvaliacaoOnlineMatriculaQuestaoVO> avaliacaoOnlineMatriculaQuestaoVOs) {
		this.avaliacaoOnlineMatriculaQuestaoVOs = avaliacaoOnlineMatriculaQuestaoVOs;
	}

	public Integer getQuantidadeQuestaoMarcadas() {
		if (quantidadeQuestaoMarcadas == null) {
			quantidadeQuestaoMarcadas = 0;
		}
		return quantidadeQuestaoMarcadas;
	}

	public void setQuantidadeQuestaoMarcadas(Integer quantidadeQuestaoMarcadas) {
		this.quantidadeQuestaoMarcadas = quantidadeQuestaoMarcadas;
	}

	public long getDataInicioTemporizador() {
		return getDataInicio().getTime();
	}

	public long getDataLimiteTerminoTemporizador() {
		return getDataLimiteTermino().getTime();
	}

	public Boolean getNotaLancadaNoHistorico() {
		if (notaLancadaNoHistorico == null) {
			notaLancadaNoHistorico = false;
		}
		return notaLancadaNoHistorico;
	}

	public void setNotaLancadaNoHistorico(Boolean notaLancadaNoHistorico) {
		this.notaLancadaNoHistorico = notaLancadaNoHistorico;
	}

	public boolean getIsApresentarBotaVisualizarGabaritoVisaoAdministrativa() {
		return !(getSituacaoAvaliacaoOnlineMatriculaEnum()
				.equals(SituacaoAvaliacaoOnlineMatriculaEnum.AGUARDANDO_REALIZACAO));
	}

	public boolean getIsApresentarNota() {
		return !(getSituacaoAvaliacaoOnlineMatriculaEnum()
				.equals(SituacaoAvaliacaoOnlineMatriculaEnum.AGUARDANDO_REALIZACAO)
				|| getSituacaoAvaliacaoOnlineMatriculaEnum()
						.equals(SituacaoAvaliacaoOnlineMatriculaEnum.EM_REALIZACAO));
	}

	public boolean getIsAvaliacaoRealizada() {
		return !(getSituacaoAvaliacaoOnlineMatriculaEnum().equals(SituacaoAvaliacaoOnlineMatriculaEnum.APROVADO)
				|| getSituacaoAvaliacaoOnlineMatriculaEnum().equals(SituacaoAvaliacaoOnlineMatriculaEnum.REPROVADO));
	}

	public List<GraficoAproveitamentoAvaliacaoVO> getGraficoAproveitamentoAvaliacaoVOs() {
		if (graficoAproveitamentoAvaliacaoVOs == null) {
			graficoAproveitamentoAvaliacaoVOs = new ArrayList<GraficoAproveitamentoAvaliacaoVO>();
		}
		return graficoAproveitamentoAvaliacaoVOs;
	}

	public void setGraficoAproveitamentoAvaliacaoVOs(
			List<GraficoAproveitamentoAvaliacaoVO> graficoAproveitamentoAvaliacaoVOs) {
		this.graficoAproveitamentoAvaliacaoVOs = graficoAproveitamentoAvaliacaoVOs;
	}

	public boolean getIsApresentarMonitorConhecimentoPorAssunto() {
		return getGraficoAproveitamentoAvaliacaoVOs().size() != 0;
	}

	public String getNaoAcertouOuErrouNenhumaQuestao() {
		if (naoAcertouOuErrouNenhumaQuestao == null) {
			naoAcertouOuErrouNenhumaQuestao = "";
		}
		return naoAcertouOuErrouNenhumaQuestao;
	}

	public void setNaoAcertouOuErrouNenhumaQuestao(String naoAcertouOuErrouNenhumaQuestao) {
		this.naoAcertouOuErrouNenhumaQuestao = naoAcertouOuErrouNenhumaQuestao;
	}

	public Boolean getIsNaoAcertouOuErrouQuestao() {
		return getNaoAcertouOuErrouNenhumaQuestao().isEmpty();
	}

	public Date getDataInicioLiberacao() {
		return dataInicioLiberacao;
	}

	public void setDataInicioLiberacao(Date dataInicioLiberacao) {
		this.dataInicioLiberacao = dataInicioLiberacao;
	}

	public Date getDataFimLiberacao() {
		return dataFimLiberacao;
	}

	public void setDataFimLiberacao(Date dataFimLiberacao) {
		this.dataFimLiberacao = dataFimLiberacao;
	}

	public Integer getTamanhoListaAvaliacaoOnlineMatriculaQuestao() {
		if (tamanhoListaAvaliacaoOnlineMatriculaQuestao == null) {
			tamanhoListaAvaliacaoOnlineMatriculaQuestao = 0;
		}
		return tamanhoListaAvaliacaoOnlineMatriculaQuestao;
	}

	public void setTamanhoListaAvaliacaoOnlineMatriculaQuestao(Integer tamanhoListaAvaliacaoOnlineMatriculaQuestao) {
		this.tamanhoListaAvaliacaoOnlineMatriculaQuestao = tamanhoListaAvaliacaoOnlineMatriculaQuestao;
	}

	public Integer getQtdeQuestaoFacil() {
		if (qtdeQuestaoDificil == null) {
			qtdeQuestaoDificil = 0;
		}
		return qtdeQuestaoFacil;
	}

	public void setQtdeQuestaoFacil(Integer qtdeQuestaoFacil) {
		this.qtdeQuestaoFacil = qtdeQuestaoFacil;
	}

	public Integer getQtdeQuestaoMedio() {
		if (qtdeQuestaoDificil == null) {
			qtdeQuestaoDificil = 0;
		}
		return qtdeQuestaoMedio;
	}

	public void setQtdeQuestaoMedio(Integer qtdeQuestaoMedio) {
		this.qtdeQuestaoMedio = qtdeQuestaoMedio;
	}

	public Integer getQtdeQuestaoDificil() {
		if (qtdeQuestaoDificil == null) {
			qtdeQuestaoDificil = 0;
		}
		return qtdeQuestaoDificil;
	}

	public void setQtdeQuestaoDificil(Integer qtdeQuestaoDificil) {
		this.qtdeQuestaoDificil = qtdeQuestaoDificil;
	}

	public Integer getCodigoPrimeiraQuestaoNaoRespondida() {
		if(codigoPrimeiraQuestaoNaoRespondida == null) {
			codigoPrimeiraQuestaoNaoRespondida = -1;
		}
		return codigoPrimeiraQuestaoNaoRespondida;
	}

	public void setCodigoPrimeiraQuestaoNaoRespondida(Integer codigoPrimeiraQuestaoNaoRespondida) {
		this.codigoPrimeiraQuestaoNaoRespondida = codigoPrimeiraQuestaoNaoRespondida;
	}


}
