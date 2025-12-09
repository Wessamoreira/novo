package negocio.comuns.recursoshumanos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.recursoshumanos.enumeradores.MotivoDemissaoEnum;
import negocio.comuns.recursoshumanos.enumeradores.SituacaoRescisaoEnum;
import negocio.comuns.recursoshumanos.enumeradores.TipoDemissaoEnum;

public class RescisaoVO extends SuperVO {

	private static final long serialVersionUID = 1L;

	private Integer codigo;
	private TipoDemissaoEnum tipoDemissao;
	private MotivoDemissaoEnum motivoDemissao;
	private Date dataDemissao;
	private CompetenciaFolhaPagamentoVO competenciaFolhaPagamento;
	private TemplateLancamentoFolhaPagamentoVO templateLancamentoFolhaPagamento;
	private SituacaoRescisaoEnum situacao;

	private List<RescisaoIndividualVO> rescisaoIndividualVOs;

	public enum EnumCampoConsultaRescisao {
		DATA_COMPETENCIA;
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

	public TipoDemissaoEnum getTipoDemissao() {
		return tipoDemissao;
	}

	public void setTipoDemissao(TipoDemissaoEnum tipoDemissao) {
		this.tipoDemissao = tipoDemissao;
	}

	public MotivoDemissaoEnum getMotivoDemissao() {
		return motivoDemissao;
	}

	public void setMotivoDemissao(MotivoDemissaoEnum motivoDemissao) {
		this.motivoDemissao = motivoDemissao;
	}

	public Date getDataDemissao() {
		if (dataDemissao == null) {
			dataDemissao = new Date();
		}
		return dataDemissao;
	}

	public void setDataDemissao(Date dataDemissao) {
		this.dataDemissao = dataDemissao;
	}

	public CompetenciaFolhaPagamentoVO getCompetenciaFolhaPagamento() {
		if (competenciaFolhaPagamento == null) {
			competenciaFolhaPagamento = new CompetenciaFolhaPagamentoVO();
		}
		return competenciaFolhaPagamento;
	}

	public void setCompetenciaFolhaPagamento(CompetenciaFolhaPagamentoVO competenciaFolhaPagamento) {
		this.competenciaFolhaPagamento = competenciaFolhaPagamento;
	}

	public TemplateLancamentoFolhaPagamentoVO getTemplateLancamentoFolhaPagamento() {
		if (templateLancamentoFolhaPagamento == null) {
			templateLancamentoFolhaPagamento = new TemplateLancamentoFolhaPagamentoVO();
		}
		return templateLancamentoFolhaPagamento;
	}

	public void setTemplateLancamentoFolhaPagamento(
			TemplateLancamentoFolhaPagamentoVO templateLancamentoFolhaPagamento) {
		this.templateLancamentoFolhaPagamento = templateLancamentoFolhaPagamento;
	}

	public SituacaoRescisaoEnum getSituacao() {
		if (situacao == null) {
			situacao = SituacaoRescisaoEnum.ATIVO;
		}
		return situacao;
	}

	public void setSituacao(SituacaoRescisaoEnum situacao) {
		this.situacao = situacao;
	}

	public List<RescisaoIndividualVO> getRescisaoIndividualVOs() {
		if (rescisaoIndividualVOs == null) {
			rescisaoIndividualVOs = new ArrayList<>();
		}
		return rescisaoIndividualVOs;
	}

	public void setRescisaoIndividualVOs(List<RescisaoIndividualVO> rescisaoIndividualVOs) {
		this.rescisaoIndividualVOs = rescisaoIndividualVOs;
	}
	
}
