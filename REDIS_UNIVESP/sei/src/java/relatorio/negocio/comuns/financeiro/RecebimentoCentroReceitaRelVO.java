package relatorio.negocio.comuns.financeiro;

public class RecebimentoCentroReceitaRelVO {

	private String centroReceita;
	private Double valor;
	
	public String getCentroReceita() {
		if(centroReceita == null){
			centroReceita = "";
		}
		return centroReceita;
	}
	public void setCentroReceita(String centroReceita) {
		this.centroReceita = centroReceita;
	}
	public Double getValor() {
		if(valor == null){
			valor = 0.0;
		}
		return valor;
	}
	public void setValor(Double valor) {
		this.valor = valor;
	}
	
	
	
	
}
