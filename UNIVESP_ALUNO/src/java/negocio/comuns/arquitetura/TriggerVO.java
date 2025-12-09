package negocio.comuns.arquitetura;

import java.io.Serializable;

public class TriggerVO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String tabela;
	private String tamanhoTabela;
	private String tamanhoIndice;
	private String tamanhoToast;
	private String mesAnoLog;
	private Integer quantidade;
	
	public boolean getAtiva() {
		return getQuantidade().intValue() > 0;
	}
	
	
	
	public String getStatus() {
		if (getAtiva()) {
			return "Ativada";
		} else {
			return "Desativada";
		}
	}
	
	public String getTabela() {
		if (tabela == null) {
			tabela = "";
		}
		return tabela;
	}
	
	public void setTabela(String tabela) {
		this.tabela = tabela;
	}
	
	public Integer getQuantidade() {
		if (quantidade == null) {
			quantidade = 0;
		}
		return quantidade;
	}
	
	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public String getTamanhoTabela() {
		if (tamanhoTabela == null) {
			tamanhoTabela = "";
		}
		return tamanhoTabela;
	}

	public void setTamanhoTabela(String tamanhoTabela) {
		this.tamanhoTabela = tamanhoTabela;
	}

	public String getTamanhoIndice() {
		if (tamanhoIndice == null) {
			tamanhoIndice = "";
		}
		return tamanhoIndice;
	}

	public void setTamanhoIndice(String tamanhoIndice) {
		this.tamanhoIndice = tamanhoIndice;
	}

	public String getTamanhoToast() {
		if (tamanhoToast == null) {
			tamanhoToast = "";
		}
		return tamanhoToast;
	}

	public void setTamanhoToast(String tamanhoToast) {
		this.tamanhoToast = tamanhoToast;
	}



	/**
	 * @return the mesAnoLog
	 */
	public String getMesAnoLog() {
		if (mesAnoLog == null) {
			mesAnoLog = "";
		}
		return mesAnoLog;
	}



	/**
	 * @param mesAnoLog the mesAnoLog to set
	 */
	public void setMesAnoLog(String mesAnoLog) {
		this.mesAnoLog = mesAnoLog;
	}

	
	
}
