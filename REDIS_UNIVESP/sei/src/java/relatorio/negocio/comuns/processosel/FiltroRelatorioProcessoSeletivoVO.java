package relatorio.negocio.comuns.processosel;

public class FiltroRelatorioProcessoSeletivoVO {

	private Boolean ativo = false;
	private Boolean canceladoOutraInscricao = false;
	private Boolean naoCompareceu = false;
	private Boolean cancelado = false;

	private Boolean confirmado = false;
	private Boolean pendenteFinanceiro = false;
	
	private Boolean processoSeletivo = true;
	private Boolean enem = true;
	private Boolean portadorDiploma = true;
	private Boolean transferencia = true;

	public void realizarMarcarTodasSituacoes() {
		realizarSelecionarTodosSituacoes(true);
	}

	public void realizarDesmarcarTodasSituacoes() {
		realizarSelecionarTodosSituacoes(false);
	}

	public void realizarSelecionarTodosSituacoes(boolean selecionado) {
		setAtivo(selecionado);
		setCancelado(selecionado);
		setCanceladoOutraInscricao(selecionado);
		setNaoCompareceu(selecionado);
	}

	public Boolean getAtivo() {
		if (ativo == null) {
			ativo = false;
		}
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public Boolean getCanceladoOutraInscricao() {
		if (canceladoOutraInscricao == null) {
			canceladoOutraInscricao = false;
		}
		return canceladoOutraInscricao;
	}

	public void setCanceladoOutraInscricao(Boolean canceladoOutraInscricao) {
		this.canceladoOutraInscricao = canceladoOutraInscricao;
	}

	public Boolean getNaoCompareceu() {
		if (naoCompareceu == null) {
			naoCompareceu = false;
		}
		return naoCompareceu;
	}

	public void setNaoCompareceu(Boolean naoCompareceu) {
		this.naoCompareceu = naoCompareceu;
	}

	public Boolean getCancelado() {
		if (cancelado == null) {
			cancelado = false;
		}
		return cancelado;
	}

	public void setCancelado(Boolean cancelado) {
		this.cancelado = cancelado;
	}

	public Boolean getConfirmado() {
		if (confirmado == null) {
			confirmado = false;
		}
		return confirmado;
	}

	public void setConfirmado(Boolean confirmado) {
		this.confirmado = confirmado;
	}

	public Boolean getPendenteFinanceiro() {
		if (pendenteFinanceiro == null) {
			pendenteFinanceiro = false;
		}
		return pendenteFinanceiro;
	}

	public void setPendenteFinanceiro(Boolean pendenteFinanceiro) {
		this.pendenteFinanceiro = pendenteFinanceiro;
	}
	
	public Boolean getProcessoSeletivo() {
		if (processoSeletivo == null) {
			processoSeletivo = true;
		}
		
		return processoSeletivo;
	}

	public void setProcessoSeletivo(Boolean processoSeletivo) {
		this.processoSeletivo = processoSeletivo;
	}

	public Boolean getEnem() {
		if (enem == null) {
			enem = true;
		}
		return enem;
	}

	public void setEnem(Boolean enem) {
		this.enem = enem;
	}

	public Boolean getPortadorDiploma() {
		if (portadorDiploma == null) {
			portadorDiploma = true;
		}
		return portadorDiploma;
	}

	public void setPortadorDiploma(Boolean portadorDiploma) {
		this.portadorDiploma = portadorDiploma;
	}

	public Boolean getTransferencia() {
		if (transferencia == null) {
			transferencia = true;
		}
		return transferencia;
	}

	public void setTransferencia(Boolean transferencia) {
		this.transferencia = transferencia;
	}
	
	
}
