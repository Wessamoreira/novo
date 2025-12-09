package negocio.comuns.ead;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;

public class ParametrosGraficoComparativoAvaliacoesOnlinesVO extends SuperVO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MonitorConhecimentoVO monitorConhecimentoVO;
	private Double percentuaisEvolucaoAvaliacoesOnline;
	private Date dataTermino;

	public MonitorConhecimentoVO getMonitorConhecimentoVO() {
		if(monitorConhecimentoVO == null) {
			monitorConhecimentoVO = new MonitorConhecimentoVO();
		}
		return monitorConhecimentoVO;
	}

	public void setMonitorConhecimentoVO(MonitorConhecimentoVO monitorConhecimentoVO) {
		this.monitorConhecimentoVO = monitorConhecimentoVO;
	}

	public Double getPercentuaisEvolucaoAvaliacoesOnline() {
		if(percentuaisEvolucaoAvaliacoesOnline == null) {
			percentuaisEvolucaoAvaliacoesOnline = 0.0;
		}
		return percentuaisEvolucaoAvaliacoesOnline;
	}

	public void setPercentuaisEvolucaoAvaliacoesOnline(Double percentuaisEvolucaoAvaliacoesOnline) {
		this.percentuaisEvolucaoAvaliacoesOnline = percentuaisEvolucaoAvaliacoesOnline;
	}

	public Date getDataTermino() {
		if(dataTermino == null) {
			dataTermino = new Date();
		}
		return dataTermino;
	}

	public void setDataTermino(Date dataTermino) {
		this.dataTermino = dataTermino;
	}
}
