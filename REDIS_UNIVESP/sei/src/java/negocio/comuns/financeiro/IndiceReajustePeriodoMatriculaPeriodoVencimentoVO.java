package negocio.comuns.financeiro;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.SituacaoExecucaoEnum;
import negocio.comuns.utilitarias.Uteis;

public class IndiceReajustePeriodoMatriculaPeriodoVencimentoVO extends SuperVO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private Date dataReajuste;
	private BigDecimal valorBaseContaReceberReajuste;
	private BigDecimal valorReajuste;
	private IndiceReajustePeriodoVO indiceReajustePeriodoVO;
	private String parcela;
	private MatriculaPeriodoVO matriculaPeriodoVO;
	private BigDecimal valorReferenteDiferencaParcelaRecebidaOuEnviadaRemessa;
	private String observacaoDiferencaParcelaRecebidaOuEnviadaRemessa;
	private SituacaoExecucaoEnum situacao;
	private Date dataCancelamento;
	private UsuarioVO responsavelCancelamentoVO;
	private String motivoCancelamento;
	private Boolean selecionado;
	private String origemContaReceber;
	
	public IndiceReajustePeriodoMatriculaPeriodoVencimentoVO() {
		super();
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

	public Date getDataReajuste() {
		if (dataReajuste == null) {
			dataReajuste = new Date();
		}
		return dataReajuste;
	}

	public void setDataReajuste(Date dataReajuste) {
		this.dataReajuste = dataReajuste;
	}

	public BigDecimal getValorBaseContaReceberReajuste() {
		if (valorBaseContaReceberReajuste == null) {
			valorBaseContaReceberReajuste = BigDecimal.ZERO;
		}
		return valorBaseContaReceberReajuste;
	}

	public void setValorBaseContaReceberReajuste(BigDecimal valorBaseContaReceberReajuste) {
		this.valorBaseContaReceberReajuste = valorBaseContaReceberReajuste;
	}

	public BigDecimal getValorReajuste() {
		if (valorReajuste == null) {
			valorReajuste = BigDecimal.ZERO;
		}
		return valorReajuste;
	}

	public void setValorReajuste(BigDecimal valorReajuste) {
		this.valorReajuste = valorReajuste;
	}

	public IndiceReajustePeriodoVO getIndiceReajustePeriodoVO() {
		if (indiceReajustePeriodoVO == null) {
			indiceReajustePeriodoVO = new IndiceReajustePeriodoVO();
		}
		return indiceReajustePeriodoVO;
	}

	public void setIndiceReajustePeriodoVO(IndiceReajustePeriodoVO indiceReajustePeriodoVO) {
		this.indiceReajustePeriodoVO = indiceReajustePeriodoVO;
	}

	public String getParcela() {
		if (parcela == null) {
			parcela = "";
		}
		return parcela;
	}

	public void setParcela(String parcela) {
		this.parcela = parcela;
	}

	public MatriculaPeriodoVO getMatriculaPeriodoVO() {
		if (matriculaPeriodoVO == null) {
			matriculaPeriodoVO = new MatriculaPeriodoVO();
		}
		return matriculaPeriodoVO;
	}

	public void setMatriculaPeriodoVO(MatriculaPeriodoVO matriculaPeriodoVO) {
		this.matriculaPeriodoVO = matriculaPeriodoVO;
	}

	public String getDataReajuste_Apresentar() {
		return Uteis.getDataComHora(getDataReajuste());
	}

	public BigDecimal getValorReferenteDiferencaParcelaRecebidaOuEnviadaRemessa() {
		if (valorReferenteDiferencaParcelaRecebidaOuEnviadaRemessa == null) {
			valorReferenteDiferencaParcelaRecebidaOuEnviadaRemessa = BigDecimal.ZERO;
		}
		return valorReferenteDiferencaParcelaRecebidaOuEnviadaRemessa;
	}

	public void setValorReferenteDiferencaParcelaRecebidaOuEnviadaRemessa(BigDecimal valorReferenteDiferencaParcelaRecebidaOuEnviadaRemessa) {
		this.valorReferenteDiferencaParcelaRecebidaOuEnviadaRemessa = valorReferenteDiferencaParcelaRecebidaOuEnviadaRemessa;
	}

	public String getObservacaoDiferencaParcelaRecebidaOuEnviadaRemessa() {
		if (observacaoDiferencaParcelaRecebidaOuEnviadaRemessa == null) {
			observacaoDiferencaParcelaRecebidaOuEnviadaRemessa = "";
		}
		return observacaoDiferencaParcelaRecebidaOuEnviadaRemessa;
	}

	public void setObservacaoDiferencaParcelaRecebidaOuEnviadaRemessa(String observacaoDiferencaParcelaRecebidaOuEnviadaRemessa) {
		this.observacaoDiferencaParcelaRecebidaOuEnviadaRemessa = observacaoDiferencaParcelaRecebidaOuEnviadaRemessa;
	}

	public Date getDataCancelamento() {
		return dataCancelamento;
	}

	public void setDataCancelamento(Date dataCancelamento) {
		this.dataCancelamento = dataCancelamento;
	}
	
	public String getDataCancelamento_Apresentar() {
		return Uteis.getDataComHora(getDataCancelamento());
	}

	public UsuarioVO getResponsavelCancelamentoVO() {
		if (responsavelCancelamentoVO == null) {
			responsavelCancelamentoVO = new UsuarioVO();
		}
		return responsavelCancelamentoVO;
	}

	public void setResponsavelCancelamentoVO(UsuarioVO responsavelCancelamentoVO) {
		this.responsavelCancelamentoVO = responsavelCancelamentoVO;
	}

	public String getMotivoCancelamento() {
		if (motivoCancelamento == null) {
			motivoCancelamento = "";
		}
		return motivoCancelamento;
	}

	public void setMotivoCancelamento(String motivoCancelamento) {
		this.motivoCancelamento = motivoCancelamento;
	}

	public SituacaoExecucaoEnum getSituacao() {
		if (situacao == null) {
			situacao = SituacaoExecucaoEnum.PROCESSADO;
		}
		return situacao;
	}

	public void setSituacao(SituacaoExecucaoEnum situacao) {
		this.situacao = situacao;
	}
	
	public String getSituacao_Apresentar() {
		return getSituacao().getDescricao();
	}

	public Boolean getSelecionado() {
		if (selecionado == null) {
			selecionado = Boolean.FALSE;
		}
		return selecionado;
	}

	public void setSelecionado(Boolean selecionado) {
		this.selecionado = selecionado;
	}

	public String getOrigemContaReceber() {
		if(origemContaReceber == null) {
			origemContaReceber = "";
		}
		return origemContaReceber;
	}

	public void setOrigemContaReceber(String origemContaReceber) {
		this.origemContaReceber = origemContaReceber;
	}
}
