package relatorio.negocio.comuns.processosel;

import negocio.comuns.arquitetura.SuperVO;

public class ProcessoSeletivoOcorrenciaRelVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String linhas;

	public String getLinhas() {
		if (linhas == null) {
			linhas = "";
		}
		return linhas;
	}

	public void setLinhas(String linhas) {
		this.linhas = linhas;
	}

}
