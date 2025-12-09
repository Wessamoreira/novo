package negocio.comuns.biblioteca;

public class ReturnResponseMinhaBibliotecaVO  {

	private String AuthenticatedUrl;
	private Boolean Success; 
	private String  Message;
	
	public String getAuthenticatedUrl() {
		return AuthenticatedUrl;
	}
	public void setAuthenticatedUrl(String authenticatedUrl) {
		this.AuthenticatedUrl = authenticatedUrl;
	}
	public Boolean getSuccess() {
		return Success;
	}
	public void setSuccess(Boolean Success) {
		this.Success = Success;
	}
	public String getMessage() {
		return Message;
	}
	public void setMessage(String Message) {
		this.Message = Message;
	}
	
//	{"AuthenticatedUrl":"https://jigsaw.minhabiblioteca.com.br/auth/redirects/8QE25ZNGA3YYVATCASDHSSZE3DZWX5NT7KHBD73GTPQEGZ232P","Success":true,"Message":null}


}
