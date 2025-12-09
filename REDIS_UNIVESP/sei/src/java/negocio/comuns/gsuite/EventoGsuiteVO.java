package negocio.comuns.gsuite;

import negocio.comuns.arquitetura.SuperVO;

public class EventoGsuiteVO extends SuperVO {

	private static final long serialVersionUID = -6727071501897476735L;

	private String idEvento;
	private java.lang.String hangoutLink;
	private java.lang.String htmlLink;
	
	public EventoGsuiteVO() { }

	public EventoGsuiteVO(String idEvento, String hangoutLink, String htmlLink) {
		super();
		this.idEvento = idEvento;
		this.hangoutLink = hangoutLink;
		this.htmlLink = htmlLink;
	}

	public String getIdEvento() {
		return idEvento;
	}

	public void setIdEvento(String idEvento) {
		this.idEvento = idEvento;
	}

	public java.lang.String getHangoutLink() {
		return hangoutLink;
	}

	public void setHangoutLink(java.lang.String hangoutLink) {
		this.hangoutLink = hangoutLink;
	}

	public java.lang.String getHtmlLink() {
		return htmlLink;
	}

	public void setHtmlLink(java.lang.String htmlLink) {
		this.htmlLink = htmlLink;
	}

}
