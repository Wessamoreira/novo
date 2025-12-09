package negocio.comuns.protocolo;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.gson.annotations.Expose;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.DisciplinasAproveitadasVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.annotation.ExcluirJsonAnnotation;
import negocio.comuns.protocolo.enumeradores.SituacaoRequerimentoDisciplinasAproveitadasEnum;
import negocio.comuns.utilitarias.Uteis;

@XmlRootElement(name = "requerimentoDisciplinasAproveitadas")
public class RequerimentoDisciplinasAproveitadasVO extends DisciplinasAproveitadasVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6009092738909604614L;
	@Expose(serialize = false, deserialize = false)
	@ExcluirJsonAnnotation
	private RequerimentoVO requerimentoVO;	
	private SituacaoRequerimentoDisciplinasAproveitadasEnum situacaoRequerimentoDisciplinasAproveitadasEnum;
	private String motivoSituacao;
	private String motivoRevisaoAnaliseAproveitamento;
	private UsuarioVO responsavelDeferimento;
	private Date dataDeferimento;
	private UsuarioVO responsavelIndeferimento;
	private Date dataIndeferimento;
	private ArquivoVO arquivoPlanoEnsino;	 
	private boolean apresentarMotivoRevisaoAnaliseAproveitamento = false;
	private boolean bloquearNovaSolicitacoes = false;
	/**
	 * Transient
	 */
	private Integer qtdIndeferimentos;

	public RequerimentoVO getRequerimentoVO() {
		if (requerimentoVO == null) {
			requerimentoVO = new RequerimentoVO();
		}
		return requerimentoVO;
	}

	public void setRequerimentoVO(RequerimentoVO requerimentoVO) {
		this.requerimentoVO = requerimentoVO;
	}

	@XmlElement(name = "situacaoRequerimentoDisciplinasAproveitadasEnum")
	public SituacaoRequerimentoDisciplinasAproveitadasEnum getSituacaoRequerimentoDisciplinasAproveitadasEnum() {
		if (situacaoRequerimentoDisciplinasAproveitadasEnum == null) {
			situacaoRequerimentoDisciplinasAproveitadasEnum = SituacaoRequerimentoDisciplinasAproveitadasEnum.AGUARDANDO_ANALISE;
		}
		return situacaoRequerimentoDisciplinasAproveitadasEnum;
	}

	public void setSituacaoRequerimentoDisciplinasAproveitadasEnum(SituacaoRequerimentoDisciplinasAproveitadasEnum situacaoRequerimentoDisciplinasAproveitadasEnum) {
		this.situacaoRequerimentoDisciplinasAproveitadasEnum = situacaoRequerimentoDisciplinasAproveitadasEnum;
	}

	@XmlElement(name = "motivoSituacao")
	public String getMotivoSituacao() {
		if (motivoSituacao == null) {
			motivoSituacao = "";
		}
		return motivoSituacao;
	}

	public void setMotivoSituacao(String motivoSituacao) {
		this.motivoSituacao = motivoSituacao;
	}	

	@XmlElement(name = "motivoRevisaoAnaliseAproveitamento")
	public String getMotivoRevisaoAnaliseAproveitamento() {
		if (motivoRevisaoAnaliseAproveitamento == null) {
			motivoRevisaoAnaliseAproveitamento = "";
		}
		return motivoRevisaoAnaliseAproveitamento;
	}

	public void setMotivoRevisaoAnaliseAproveitamento(String motivoRevisaoAnaliseAproveitamento) {
		this.motivoRevisaoAnaliseAproveitamento = motivoRevisaoAnaliseAproveitamento;
	}

	@XmlElement(name = "responsavelDeferimento")
	public UsuarioVO getResponsavelDeferimento() {
		if (responsavelDeferimento == null) {
			responsavelDeferimento = new UsuarioVO();
		}
		return responsavelDeferimento;
	}

	public void setResponsavelDeferimento(UsuarioVO responsavelDeferimento) {
		this.responsavelDeferimento = responsavelDeferimento;
	}

	@XmlElement(name = "dataDeferimento")
	public Date getDataDeferimento() {
		return dataDeferimento;
	}

	public void setDataDeferimento(Date dataDeferimento) {
		this.dataDeferimento = dataDeferimento;
	}

	@XmlElement(name = "responsavelIndeferimento")
	public UsuarioVO getResponsavelIndeferimento() {
		if (responsavelIndeferimento == null) {
			responsavelIndeferimento = new UsuarioVO();
		}
		return responsavelIndeferimento;
	}

	public void setResponsavelIndeferimento(UsuarioVO responsavelIndeferimento) {
		this.responsavelIndeferimento = responsavelIndeferimento;
	}

	@XmlElement(name = "dataIndeferimento")
	public Date getDataIndeferimento() {

		return dataIndeferimento;
	}

	public void setDataIndeferimento(Date dataIndeferimento) {
		this.dataIndeferimento = dataIndeferimento;
	}

	@XmlElement(name = "arquivoPlanoEnsino")
	public ArquivoVO getArquivoPlanoEnsino() {
		if (arquivoPlanoEnsino == null) {
			arquivoPlanoEnsino = new ArquivoVO();
		}
		return arquivoPlanoEnsino;
	}

	public void setArquivoPlanoEnsino(ArquivoVO arquivoPlanoEnsino) {
		this.arquivoPlanoEnsino = arquivoPlanoEnsino;
	}

	public boolean isApresentarMotivoRevisaoAnaliseAproveitamento() {
		return apresentarMotivoRevisaoAnaliseAproveitamento;
	}

	public void setApresentarMotivoRevisaoAnaliseAproveitamento(boolean apresentarMotivoRevisaoAnaliseAproveitamento) {
		this.apresentarMotivoRevisaoAnaliseAproveitamento = apresentarMotivoRevisaoAnaliseAproveitamento;
	}

	@XmlElement(name = "bloquearNovaSolicitacoes")
	public boolean isBloquearNovaSolicitacoes() {
		return bloquearNovaSolicitacoes;
	}

	public void setBloquearNovaSolicitacoes(boolean bloquearNovaSolicitacoes) {
		this.bloquearNovaSolicitacoes = bloquearNovaSolicitacoes;
	}

	public Integer getQtdIndeferimentos() {
		if (qtdIndeferimentos == null) {
			qtdIndeferimentos = 0;
		}
		return qtdIndeferimentos;
	}

	@XmlElement(name = "qtdIndeferimentos")
	public void setQtdIndeferimentos(Integer qtdIndeferimentos) {
		this.qtdIndeferimentos = qtdIndeferimentos;
	}
	
	public Boolean getDisabilitarNovaSolicitacoes() {
		if (Uteis.isAtributoPreenchido(getRequerimentoVO().getTipoRequerimento()) && Uteis.isAtributoPreenchido(getRequerimentoVO().getTipoRequerimento().getQtdeMaximaIndeferidoAproveitamento())) {
			return getQtdIndeferimentos() >= getRequerimentoVO().getTipoRequerimento().getQtdeMaximaIndeferidoAproveitamento();
		} else {
			return Boolean.FALSE;
		}
	}
	
	public boolean isSituacaoRequerimentoDisciplinaDeferido() {
		return Uteis.isAtributoPreenchido(getSituacaoRequerimentoDisciplinasAproveitadasEnum()) && getSituacaoRequerimentoDisciplinasAproveitadasEnum().isDeferido();
	}

	public boolean isSituacaoRequerimentoDisciplinaIndeferido() {
		return Uteis.isAtributoPreenchido(getSituacaoRequerimentoDisciplinasAproveitadasEnum()) && getSituacaoRequerimentoDisciplinasAproveitadasEnum().isIndeferido();
	}
}
