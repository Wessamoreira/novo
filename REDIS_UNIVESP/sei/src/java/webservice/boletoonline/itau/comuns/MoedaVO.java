package webservice.boletoonline.itau.comuns;

public class MoedaVO {

	private String codigo_moeda_cnab;
	private String quantidade_moeda;
	
	public MoedaVO() {}


	public MoedaVO(String codigo_moeda_cnab, String quantidade_moeda) {
		this.codigo_moeda_cnab = codigo_moeda_cnab;
		this.quantidade_moeda = quantidade_moeda;
	}

	public String getCodigo_moeda_cnab() {
		return codigo_moeda_cnab;
	}

	public void setCodigo_moeda_cnab(String codigo_moeda_cnab) {
		if (codigo_moeda_cnab == null) {
			codigo_moeda_cnab = "";
		}
		this.codigo_moeda_cnab = codigo_moeda_cnab;
	}

	public String getQuantidade_moeda() {
		if (quantidade_moeda == null) {
			quantidade_moeda = "";
		}
		return quantidade_moeda;
	}

	public void setQuantidade_moeda(String quantidade_moeda) {
		this.quantidade_moeda = quantidade_moeda;
	}

}
