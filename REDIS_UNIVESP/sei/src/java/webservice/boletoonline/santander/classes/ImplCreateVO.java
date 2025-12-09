package webservice.boletoonline.santander.classes;

public class ImplCreateVO {

	public TicketRequestVO ticketRequest;

	public ImplCreateVO() {
		setTicketRequest(new TicketRequestVO());
	}
	
	public TicketRequestVO getTicketRequest() {
		return ticketRequest;
	}

	public void setTicketRequest(TicketRequestVO ticketRequest) {
		this.ticketRequest = ticketRequest;
	}

}
