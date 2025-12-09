package webservice.boletoonline.bancoBrasil.comuns;

public class DescontoVO {
	
	
	private Integer tipo ;
	private String dataExpiracao;
	private String porcentagem;
	private String valor;
	
	
	public Integer getTipo() {
		if(tipo == null) {
			tipo = 0;
		}
		return tipo;
	}
	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}
	public String getDataExpiracao() {
		if(dataExpiracao == null ) {
			dataExpiracao ="";
		}
		return dataExpiracao;
	}
	public void setDataExpiracao(String dataExpiracao) {
		this.dataExpiracao = dataExpiracao;
	}
	public String getPorcentagem() {
		if(porcentagem == null) {
			porcentagem ="";
		}
		return porcentagem;
	}
	public void setPorcentagem(String porcentagem) {
		this.porcentagem = porcentagem;
	}
	public String getValor() {
		if(valor ==null) {
			valor ="";
		}
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}

	

}
