package negocio.comuns.crm;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.crm.enumerador.TipoFollowUpEnum;
import negocio.comuns.utilitarias.Uteis;
import webservice.servicos.objetos.enumeradores.MesesAbreviadosAplicativoEnum;

public class InteracaoWorkflowHistoricoVO extends SuperVO implements Serializable {

	private static final long serialVersionUID = 1L;
	private TipoFollowUpEnum tipoFollowUp;
	private InteracaoWorkflowVO interacaoWorkflowVO;
	private HistoricoFollowUpVO historicoFollowUpVO;
	private String mesAbreviado;

	public TipoFollowUpEnum getTipoFollowUp() {
		if (tipoFollowUp == null) {
			tipoFollowUp = TipoFollowUpEnum.INTERACAO_WORKFLOW;
		}
		return tipoFollowUp;
	}

	public void setTipoFollowUp(TipoFollowUpEnum tipoFollowUp) {
		this.tipoFollowUp = tipoFollowUp;
	}

	public InteracaoWorkflowVO getInteracaoWorkflowVO() {
		if (interacaoWorkflowVO == null) {
			interacaoWorkflowVO = new InteracaoWorkflowVO();
		}
		return interacaoWorkflowVO;
	}

	public void setInteracaoWorkflowVO(InteracaoWorkflowVO interacaoWorkflowVO) {
		this.interacaoWorkflowVO = interacaoWorkflowVO;
	}

	public HistoricoFollowUpVO getHistoricoFollowUpVO() {
		if (historicoFollowUpVO == null) {
			historicoFollowUpVO = new HistoricoFollowUpVO();
		}
		return historicoFollowUpVO;
	}

	public void setHistoricoFollowUpVO(HistoricoFollowUpVO historicoFollowUpVO) {
		this.historicoFollowUpVO = historicoFollowUpVO;
	}

	public String getMesAbreviadoAnoData() {
		if (mesAbreviado == null) {
			Calendar calendar = Calendar.getInstance();
			if (getTipoFollowUp().equals(TipoFollowUpEnum.INTERACAO_WORKFLOW)) {
				calendar.setTime(getInteracaoWorkflowVO().getDataInicio());
			} else {
				calendar.setTime(getHistoricoFollowUpVO().getDataregistro());
			}
			mesAbreviado = MesesAbreviadosAplicativoEnum.getEnumValor(calendar.get(Calendar.MONTH) + 1).getValor();
		}
		if (getTipoFollowUp().equals(TipoFollowUpEnum.INTERACAO_WORKFLOW)) {
			return mesAbreviado + "/" + Uteis.getAno2Digitos(getInteracaoWorkflowVO().getDataInicio());
		}
		return mesAbreviado + "/" + Uteis.getAno2Digitos(getHistoricoFollowUpVO().getDataregistro());
		
	}

	public String getMesAbreviado() {
		if (mesAbreviado == null) {
			mesAbreviado = "";
		}
		return mesAbreviado;
	}

	public void setMesAbreviado(String mesAbreviado) {
		this.mesAbreviado = mesAbreviado;
	}

	public String getCssTimeLineFichaAluno() {
		return "timelineInteracaoWorkflow-badge";
	}
	
	public Date getOrdenacao() {
		if (getTipoFollowUp().equals(TipoFollowUpEnum.INTERACAO_WORKFLOW)) {
			if(getInteracaoWorkflowVO().getHoraInicio().isEmpty()) {
				return getInteracaoWorkflowVO().getDataInicio();
			}
			try {
				return Uteis.getData(Uteis.getData(getInteracaoWorkflowVO().getDataInicio())+" "+getInteracaoWorkflowVO().getHoraInicio(), "dd/MM/yyyy HH:mm");
			} catch (ParseException e) {
				return getInteracaoWorkflowVO().getDataInicio();
			}
		}
		return getHistoricoFollowUpVO().getDataregistro();
	}
}
