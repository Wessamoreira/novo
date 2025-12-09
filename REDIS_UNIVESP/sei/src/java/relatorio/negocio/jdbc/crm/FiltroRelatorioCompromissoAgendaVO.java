package relatorio.negocio.jdbc.crm;

import negocio.comuns.arquitetura.SuperVO;

public class FiltroRelatorioCompromissoAgendaVO  extends SuperVO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4827850056476511641L;
	private Boolean aguardandoContato;
	private Boolean paralizado;
	private Boolean realizado;
	private Boolean realizadoComInsucessoContato;
	private Boolean realizadoComRemarcacao;
	private Boolean naoPossuiAgenda;

	public void realizarMarcarTodasSituacoes() {
		realizarSelecionarTodosSituacoes(true);
	}

	public void realizarDesmarcarTodasSituacoes() {
		realizarSelecionarTodosSituacoes(false);
	}

	public void realizarSelecionarTodosSituacoes(boolean selecionado) {
		setAguardandoContato(selecionado);
		setNaoPossuiAgenda(selecionado);
		setParalizado(selecionado);
		setRealizado(selecionado);
		setRealizadoComInsucessoContato(selecionado);
		setRealizadoComRemarcacao(selecionado);
	}

	public Boolean getAguardandoContato() {
		if (aguardandoContato == null) {
			aguardandoContato = Boolean.FALSE;
		}
		return aguardandoContato;
	}

	public void setAguardandoContato(Boolean aguardandoContato) {
		this.aguardandoContato = aguardandoContato;
	}

	public Boolean getParalizado() {
		if (paralizado == null) {
			paralizado = Boolean.FALSE;
		}
		return paralizado;
	}

	public void setParalizado(Boolean paralizado) {
		this.paralizado = paralizado;
	}

	public Boolean getRealizado() {
		if (realizado == null) {
			realizado = Boolean.FALSE;
		}
		return realizado;
	}

	public void setRealizado(Boolean realizado) {
		this.realizado = realizado;
	}

	public Boolean getRealizadoComInsucessoContato() {
		if (realizadoComInsucessoContato == null) {
			realizadoComInsucessoContato = Boolean.FALSE;
		}
		return realizadoComInsucessoContato;
	}

	public void setRealizadoComInsucessoContato(Boolean realizadoComInsucessoContato) {
		this.realizadoComInsucessoContato = realizadoComInsucessoContato;
	}

	public Boolean getRealizadoComRemarcacao() {
		if (realizadoComRemarcacao == null) {
			realizadoComRemarcacao = Boolean.FALSE;
		}
		return realizadoComRemarcacao;
	}

	public void setRealizadoComRemarcacao(Boolean realizadoComRemarcacao) {
		this.realizadoComRemarcacao = realizadoComRemarcacao;
	}

	public Boolean getNaoPossuiAgenda() {
		if (naoPossuiAgenda == null) {
			naoPossuiAgenda = Boolean.FALSE;
		}
		return naoPossuiAgenda;
	}

	public void setNaoPossuiAgenda(Boolean naoPossuiAgenda) {
		this.naoPossuiAgenda = naoPossuiAgenda;
	}

}
