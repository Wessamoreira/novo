package webservice.boletoonline.bancoBrasil.comuns;

public class JurosVO {


	private Integer tipo;
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
