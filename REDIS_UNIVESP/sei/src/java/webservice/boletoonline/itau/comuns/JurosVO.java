package webservice.boletoonline.itau.comuns;

public class JurosVO {

	private String data_juros;
	private Integer tipo_juros;
	private String valor_juros;
	private String percentual_juros;

	public String getData_juros() {
		if (data_juros == null) {
			data_juros = "";
		}
		return data_juros;
	}

	public void setData_juros(String data_juros) {
		this.data_juros = data_juros;
	}

	public Integer getTipo_juros() {
		if (tipo_juros == null) {
			tipo_juros = 0;
		}
		return tipo_juros;
	}

	public void setTipo_juros(Integer tipo_juros) {
		this.tipo_juros = tipo_juros;
	}

	public String getValor_juros() {
		if (valor_juros == null) {
			valor_juros = "";
		}
		return valor_juros;
	}

	public void setValor_juros(String valor_juros) {
		this.valor_juros = valor_juros;
	}

	public String getPercentual_juros() {
		if (percentual_juros == null) {
			percentual_juros = "";
		}
		return percentual_juros;
	}

	public void setPercentual_juros(String percentual_juros) {
		this.percentual_juros = percentual_juros;
	}

}
