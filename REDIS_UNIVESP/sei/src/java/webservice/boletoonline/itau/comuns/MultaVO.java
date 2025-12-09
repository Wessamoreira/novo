package webservice.boletoonline.itau.comuns;

public class MultaVO {

	private String data_multa;
	private Integer tipo_multa;
	private String valor_multa;
	private String percentual_multa;

	public String getData_multa() {
		if (data_multa == null) {
			data_multa = "";
		}
		return data_multa;
	}

	public void setData_multa(String data_multa) {
		this.data_multa = data_multa;
	}

	public Integer getTipo_multa() {
		if (tipo_multa == null) {
			tipo_multa = 0;
		}
		return tipo_multa;
	}

	public void setTipo_multa(Integer tipo_multa) {
		this.tipo_multa = tipo_multa;
	}

	public String getValor_multa() {
		if (valor_multa == null) {
			valor_multa = "";
		}
		return valor_multa;
	}

	public void setValor_multa(String valor_multa) {
		this.valor_multa = valor_multa;
	}

	public String getPercentual_multa() {
		if (percentual_multa == null) {
			percentual_multa = "";
		}
		return percentual_multa;
	}

	public void setPercentual_multa(String percentual_multa) {
		this.percentual_multa = percentual_multa;
	}

}
