package negocio.comuns.biblioteca.enumeradores;

public enum TipoOrigemTimeLineEnum {
	EMPRESTIMO ("Empréstimo Realizado", "fas fa-book-reader", "color:#cccccc"),
	EMPRESTIMO_ATRASADO ("Empréstimo Atrasado", "fas fa-book-reader", "color:#e55039"),
	EMPRESTIMO_EM_DIA("Empréstimo em Dia", "fas fa-book-reader", "color:#47d1af"),
	EMPRESTIMO_DEVOLVIDO_ATRASADO ("Empréstimo Devolvido em Atraso", "fas fa-book", "color:#e55039"),
	EMPRESTIMO_DEVOLVIDO_EM_DIA("Empréstimo Devolvido em Dia", "fas fa-book", "color:#cccccc"),
	EMPRESTIMO_RENOVADO ("Empréstimo Renovado", "fas fa-book", "color:#cccccc"),
	CONTA_RECEBER_VENCIMENTO ("Multa a Pagar", "fas fa-dollar-sign", "color:#e55039"),
	CONTA_RECEBER_PAGAMENTO ("Multa Paga", "fas fa-dollar-sign", "color:#47d1af"),
	RESERVA_ATIVA("Reserva Ativa", "fas fa-atlas", "color:#47d1af"),
	RESERVA_CANCELADA("Reserva Cancelada", "fas fa-atlas", "color:#cccccc"),
	RESERVA_FINALIZADA("Reserva Finalizada", "fas fa-atlas", "color:#e55039"),
	BLOQUEIO_BIBLIOTECA("Bloqueado na Biblioteca", "fas fa-user-lock", "color:#e55039"),
	BLOQUEIO_BIBLIOTECA_LIMITE("Liberação Bloqueado na Biblioteca", "fas fa-user-clock", "color:#47d1af");
	
	
	
	private TipoOrigemTimeLineEnum(String valorApresentar, String icon, String styleIcon) {
		this.valorApresentar = valorApresentar;
		this.icon = icon;
		this.styleIcon = styleIcon;
	}
	
	private String valorApresentar;
	private String icon;
	private String styleIcon;
	
	public String getValorApresentar() {
		return valorApresentar;
	}
	public void setValorApresentar(String valorApresentar) {
		this.valorApresentar = valorApresentar;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getStyleIcon() {
		return styleIcon;
	}
	public void setStyleIcon(String styleIcon) {
		this.styleIcon = styleIcon;
	}
	
	
}
