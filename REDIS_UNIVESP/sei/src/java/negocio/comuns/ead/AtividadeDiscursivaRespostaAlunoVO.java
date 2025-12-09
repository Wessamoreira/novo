package negocio.comuns.ead;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import negocio.comuns.academico.ConfiguracaoAcademicoNotaConceitoVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.enumeradores.TipoNotaConceitoEnum;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.ead.enumeradores.SituacaoRespostaAtividadeDiscursivaEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;

/**
 * @author Victor Hugo 17/09/2014
 */
@XmlRootElement(name = "atividadeDiscursivaRespostaAlunoVO")
public class AtividadeDiscursivaRespostaAlunoVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private String resposta;
	private String arquivo;
	private String nomeArquivoApresentar;
	private Date dataUpload;
	private Double nota;
	private Boolean utilizaNotaConceito;
	private ConfiguracaoAcademicoNotaConceitoVO notaConceito;
	private TipoNotaConceitoEnum tipoNota;
	private AtividadeDiscursivaVO atividadeDiscursivaVO;
	private MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO;
	private SituacaoRespostaAtividadeDiscursivaEnum situacaoRespostaAtividadeDiscursiva;
	private List<AtividadeDiscursivaInteracaoVO> atividadeDiscursivaInteracaoVOs;
	private PastaBaseArquivoEnum pastaBaseArquivo;
	private Date dataInicioAtividade;
	private Date dataLimiteEntrega;
	private Date dataPrimeiraNotificacao;
	private Date dataSegundaNotificacao;
	private Date dataTerceiraNotificacao;
	private Date dataNotificacaoPrazoExecucao;
	/*
	 * Início Transient
	 */
	private Integer interacao;
	private String urlImagem;
	private String nomeArquivoAnt;
	/*
	 * Fim Transient
	 */
	/*
	 * Variável criada para verificar se a nota foi lançada no histórico se foi
	 * lançada, a mesma estará setado true, senão, false.
	 */
	private Boolean notaLancadaNoHistorico;

	@XmlElement(name="codigo")
	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getArquivo() {
		if (arquivo == null) {
			arquivo = "";
		}
		return arquivo;
	}

	public void setArquivo(String arquivo) {
		this.arquivo = arquivo;
	}

	public Date getDataUpload() {
		if (dataUpload == null) {
			dataUpload = new Date();
		}
		return dataUpload;
	}

	public void setDataUpload(Date dataUpload) {
		this.dataUpload = dataUpload;
	}

	public Double getNota() {		
		return nota;
	}

	public void setNota(Double nota) {
		this.nota = nota;
	}
	
	@XmlElement(name="atividadeDiscursivaVO")
	public AtividadeDiscursivaVO getAtividadeDiscursivaVO() {
		if (atividadeDiscursivaVO == null) {
			atividadeDiscursivaVO = new AtividadeDiscursivaVO();
		}
		return atividadeDiscursivaVO;
	}

	public void setAtividadeDiscursivaVO(AtividadeDiscursivaVO atividadeDiscursivaVO) {
		this.atividadeDiscursivaVO = atividadeDiscursivaVO;
	}

	public MatriculaPeriodoTurmaDisciplinaVO getMatriculaPeriodoTurmaDisciplinaVO() {
		if (matriculaPeriodoTurmaDisciplinaVO == null) {
			matriculaPeriodoTurmaDisciplinaVO = new MatriculaPeriodoTurmaDisciplinaVO();
		}
		return matriculaPeriodoTurmaDisciplinaVO;
	}

	public void setMatriculaPeriodoTurmaDisciplinaVO(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO) {
		this.matriculaPeriodoTurmaDisciplinaVO = matriculaPeriodoTurmaDisciplinaVO;
	}

	@XmlElement(name = "resposta")
	public String getResposta() {
		if (resposta == null) {
			resposta = "";
		}
		return resposta;
	}

	public void setResposta(String resposta) {
		this.resposta = resposta;
	}

	@XmlElement(name="situacaoRespostaAtividadeDiscursiva")
	public SituacaoRespostaAtividadeDiscursivaEnum getSituacaoRespostaAtividadeDiscursiva() {
		if (situacaoRespostaAtividadeDiscursiva == null) {
			situacaoRespostaAtividadeDiscursiva = SituacaoRespostaAtividadeDiscursivaEnum.AGUARDANDO_RESPOSTA;
		}
		return situacaoRespostaAtividadeDiscursiva;
	}

	public String getSituacaoRespostaAtividadeDiscursiva_Apresentar() {
		return getSituacaoRespostaAtividadeDiscursiva().getValorApresentar();
	}

	public void setSituacaoRespostaAtividadeDiscursiva(SituacaoRespostaAtividadeDiscursivaEnum situacaoRespostaAtividadeDiscursiva) {
		this.situacaoRespostaAtividadeDiscursiva = situacaoRespostaAtividadeDiscursiva;
	}

	public List<AtividadeDiscursivaInteracaoVO> getAtividadeDiscursivaInteracaoVOs() {
		if (atividadeDiscursivaInteracaoVOs == null) {
			atividadeDiscursivaInteracaoVOs = new ArrayList<AtividadeDiscursivaInteracaoVO>();
		}
		return atividadeDiscursivaInteracaoVOs;
	}

	public void setAtividadeDiscursivaInteracaoVOs(List<AtividadeDiscursivaInteracaoVO> atividadeDiscursivaInteracaoVOs) {
		this.atividadeDiscursivaInteracaoVOs = atividadeDiscursivaInteracaoVOs;
	}

	public Integer getInteracao() {
		if (interacao == null) {
			interacao = 0;
		}
		return interacao;
	}

	public void setInteracao(Integer interacao) {
		this.interacao = interacao;
	}

	public PastaBaseArquivoEnum getPastaBaseArquivo() {
		if (pastaBaseArquivo == null) {
			pastaBaseArquivo = PastaBaseArquivoEnum.ATIVIDADE_DISCURSIVA_RESPOSTA_ALUNO_TMP;
		}
		return pastaBaseArquivo;
	}

	public void setPastaBaseArquivo(PastaBaseArquivoEnum pastaBaseArquivo) {
		this.pastaBaseArquivo = pastaBaseArquivo;
	}

	public String getUrlImagem() {
		if (urlImagem == null) {
			urlImagem = "";
		}
		return urlImagem;
	}

	public void setUrlImagem(String urlImagem) {
		this.urlImagem = urlImagem;
	}

	public String getNomeArquivoApresentar() {
		if (nomeArquivoApresentar == null) {
			nomeArquivoApresentar = "";
		}
		return nomeArquivoApresentar;
	}

	public void setNomeArquivoApresentar(String nomeArquivoApresentar) {
		this.nomeArquivoApresentar = nomeArquivoApresentar;
	}

	public String getNomeArquivoAnt() {
		if (nomeArquivoAnt == null) {
			nomeArquivoAnt = "";
		}
		return nomeArquivoAnt;
	}

	public void setNomeArquivoAnt(String nomeArquivoAnt) {
		this.nomeArquivoAnt = nomeArquivoAnt;
	}

	public boolean getIsEncobrirRespostaSeDataLimiteEntregaExpirar() {
		try {
			if (getSituacaoRespostaAtividadeDiscursiva().isAvaliado()
					|| getSituacaoRespostaAtividadeDiscursiva().isAguardandoAvaliacaoProfessor()
					|| getAtividadeDiscursivaVO().getDataLimiteEntrega().compareTo( new Date()) < 0) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean getIsEncobrirUploadSeDataLimiteEntregaExpirar() {
		try {
			if (getSituacaoRespostaAtividadeDiscursiva().isAvaliado()
					|| getSituacaoRespostaAtividadeDiscursiva().isAguardandoAvaliacaoProfessor()
					|| getAtividadeDiscursivaVO().getDataLimiteEntrega().compareTo( new Date()) < 0) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
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

	public Date getDataInicioAtividade() {
		/*if (dataInicioAtividade == null) {
			dataInicioAtividade = new Date();
		}*/
		return dataInicioAtividade;
	}

	public void setDataInicioAtividade(Date dataInicioAtividade) {
		this.dataInicioAtividade = dataInicioAtividade;
	}

	public Date getDataLimiteEntrega() {
		/*if (dataLimiteEntrega == null) {
			dataLimiteEntrega = new Date();
		}*/
		return dataLimiteEntrega;
	}

	public void setDataLimiteEntrega(Date dataLimiteEntrega) {
		this.dataLimiteEntrega = dataLimiteEntrega;
	}

	public Date getDataPrimeiraNotificacao() {
		/*if (dataPrimeiraNotificacao == null) {
			dataPrimeiraNotificacao = new Date();
		}*/
		return dataPrimeiraNotificacao;
	}

	public void setDataPrimeiraNotificacao(Date dataPrimeiraNotificacao) {
		this.dataPrimeiraNotificacao = dataPrimeiraNotificacao;
	}

	public Date getDataSegundaNotificacao() {
		/*if (dataSegundaNotificacao == null) {
			dataSegundaNotificacao = new Date();
		}*/
		return dataSegundaNotificacao;
	}

	public void setDataSegundaNotificacao(Date dataSegundaNotificacao) {
		this.dataSegundaNotificacao = dataSegundaNotificacao;
	}

	public Date getDataTerceiraNotificacao() {
		/*if (dataTerceiraNotificacao == null) {
			dataTerceiraNotificacao = new Date();
		}*/
		return dataTerceiraNotificacao;
	}

	public void setDataTerceiraNotificacao(Date dataTerceiraNotificacao) {
		this.dataTerceiraNotificacao = dataTerceiraNotificacao;
	}

	public Date getDataNotificacaoPrazoExecucao() {
		/*if (dataNotificacaoPrazoExecucao == null) {
			dataNotificacaoPrazoExecucao = new Date();
		}*/
		return dataNotificacaoPrazoExecucao;
	}

	public void setDataNotificacaoPrazoExecucao(Date dataNotificacaoPrazoExecucao) {
		this.dataNotificacaoPrazoExecucao = dataNotificacaoPrazoExecucao;
	}

	/**
	 * @return the tipoNota
	 */
	public TipoNotaConceitoEnum getTipoNota() {
		return tipoNota;
	}

	/**
	 * @param tipoNota the tipoNota to set
	 */
	public void setTipoNota(TipoNotaConceitoEnum tipoNota) {
		this.tipoNota = tipoNota;
	}

	/**
	 * @return the utilizaNotaConceito
	 */
	public Boolean getUtilizaNotaConceito() {
		if (utilizaNotaConceito == null) {
			utilizaNotaConceito = false;
		}
		return utilizaNotaConceito;
	}

	/**
	 * @param utilizaNotaConceito the utilizaNotaConceito to set
	 */
	public void setUtilizaNotaConceito(Boolean utilizaNotaConceito) {
		this.utilizaNotaConceito = utilizaNotaConceito;
	}

	/**
	 * @return the notaConceito
	 */
	public ConfiguracaoAcademicoNotaConceitoVO getNotaConceito() {
		if (notaConceito == null) {
			notaConceito = new ConfiguracaoAcademicoNotaConceitoVO();
		}
		return notaConceito;
	}

	/**
	 * @param notaConceito the notaConceito to set
	 */
	public void setNotaConceito(ConfiguracaoAcademicoNotaConceitoVO notaConceito) {
		this.notaConceito = notaConceito;
	}
	
	public boolean getIsApresentarBotaoAvaliado() {
		if (!(getSituacaoRespostaAtividadeDiscursiva().equals(SituacaoRespostaAtividadeDiscursivaEnum.AGUARDANDO_RESPOSTA)) && !(getSituacaoRespostaAtividadeDiscursiva().equals(SituacaoRespostaAtividadeDiscursivaEnum.AVALIADO))) {
			return true;
		} else if ((getSituacaoRespostaAtividadeDiscursiva().equals(SituacaoRespostaAtividadeDiscursivaEnum.AGUARDANDO_RESPOSTA)) && (Uteis.isAtributoPreenchido(getDataLimiteEntrega()) && getDataLimiteEntrega().compareTo(new Date()) == -1)) {
			return true;
		} else if ((getSituacaoRespostaAtividadeDiscursiva().equals(SituacaoRespostaAtividadeDiscursivaEnum.AGUARDANDO_RESPOSTA) || getSituacaoRespostaAtividadeDiscursiva().equals(SituacaoRespostaAtividadeDiscursivaEnum.AGUARDANDO_NOVA_RESPOSTA)) && (!Uteis.isAtributoPreenchido(getDataLimiteEntrega()) && Uteis.isAtributoPreenchido(getAtividadeDiscursivaVO().getDataLimiteEntrega()) && getAtividadeDiscursivaVO().getDataLimiteEntrega().compareTo(new Date()) == -1)) {
			return true;
		}
		return false;
	}

	public boolean getIsApresentarBotaoSolicitarNovaResposta() {
		boolean podeAvaliar = Uteis.isAtributoPreenchido(getDataLimiteEntrega()) && getDataLimiteEntrega().compareTo(new Date()) == -1;
		if (podeAvaliar) {
			return false;
		} else if (!(getSituacaoRespostaAtividadeDiscursiva().equals(SituacaoRespostaAtividadeDiscursivaEnum.AGUARDANDO_RESPOSTA)) && !(getSituacaoRespostaAtividadeDiscursiva().equals(SituacaoRespostaAtividadeDiscursivaEnum.AVALIADO)) && !(getSituacaoRespostaAtividadeDiscursiva().equals(SituacaoRespostaAtividadeDiscursivaEnum.AGUARDANDO_NOVA_RESPOSTA))) {
			return true;
		}
		return false;
	}

	public boolean getIsApresentarBotaoSolicitarAvaliacaoProfessor() {
		boolean podeAvaliar = Uteis.isAtributoPreenchido(getDataLimiteEntrega()) && getDataLimiteEntrega().compareTo(new Date()) == -1;
		if (podeAvaliar) {
			return false;
		} else if (getSituacaoRespostaAtividadeDiscursiva().equals(SituacaoRespostaAtividadeDiscursivaEnum.AGUARDANDO_AVALIACAO_PROFESSOR) || getSituacaoRespostaAtividadeDiscursiva().equals(SituacaoRespostaAtividadeDiscursivaEnum.AVALIADO)) {
			return false;
		}
		return true;
	}
	
	public boolean getIsApresentarBotaoSolicitarAvaliacaoProfessorConsiderandoArtefatoEntregaUploadArquivo() {
		if (getAtividadeDiscursivaVO().getIsApresentarArtefatoEntregaUploadArquivo() && getArquivo().isEmpty()) {
			return false;
		}
		return true;
	}
	
	public boolean getIsAptaSolicitarAvaliacaoProfessor() {
		return !getSituacaoRespostaAtividadeDiscursiva().isResponder() ? false :
				getAtividadeDiscursivaVO().getIsApresentarArtefatoEntregaTextual() ? 
						Uteis.isAtributoPreenchido(getResposta().replaceAll("(\r\n|\n\r|\r|\n)", "").replace(" ", "")) : Uteis.isAtributoPreenchido(getArquivo().replace(" ", ""));
	}
}
