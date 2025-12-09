package webservice.servicos.objetos;

public class IntegracaoSerasaApiGeoRetornoRSVO {
	private int codigo;
	private String resposta;
	private String erro;
	private String id_transacao;
	private String postback;

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getResposta() {
		return resposta;
	}

	public void setResposta(String resposta) {
		this.resposta = resposta;
	}

	public String getId_transacao() {
		return id_transacao;
	}

	public void setId_transacao(String id_transacao) {
		this.id_transacao = id_transacao;
	}

	public String getPostback() {
		return postback;
	}

	public void setPostback(String postback) {
		this.postback = postback;
	}

	public String getErro() {
		return erro;
	}

	public void setErro(String erro) {
		this.erro = erro;
	}
	
	

}
