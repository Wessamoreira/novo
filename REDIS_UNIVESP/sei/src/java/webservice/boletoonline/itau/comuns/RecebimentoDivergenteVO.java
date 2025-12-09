package webservice.boletoonline.itau.comuns;

public class RecebimentoDivergenteVO {

	private String tipo_autorizacao_recebimento;
	private String tipo_valor_percentual_recebimento;
	private String valor_minimo_recebimento;
	private String percentual_minimo_recebimento;
	private String valor_maximo_recebimento;
	private String percentual_maximo_recebimento;

	public String getTipo_autorizacao_recebimento() {
		if (tipo_autorizacao_recebimento == null) {
			tipo_autorizacao_recebimento = "";
		}
		return tipo_autorizacao_recebimento;
	}

	public void setTipo_autorizacao_recebimento(String tipo_autorizacao_recebimento) {
		this.tipo_autorizacao_recebimento = tipo_autorizacao_recebimento;
	}

	public String getTipo_valor_percentual_recebimento() {
		if (tipo_valor_percentual_recebimento == null) {
			tipo_valor_percentual_recebimento = "";
		}
		return tipo_valor_percentual_recebimento;
	}

	public void setTipo_valor_percentual_recebimento(String tipo_valor_percentual_recebimento) {
		this.tipo_valor_percentual_recebimento = tipo_valor_percentual_recebimento;
	}

	public String getValor_minimo_recebimento() {
		if (valor_minimo_recebimento == null) {
			valor_minimo_recebimento = "";
		}
		return valor_minimo_recebimento;
	}

	public void setValor_minimo_recebimento(String valor_minimo_recebimento) {
		this.valor_minimo_recebimento = valor_minimo_recebimento;
	}

	public String getPercentual_minimo_recebimento() {
		if (percentual_minimo_recebimento == null) {
			percentual_minimo_recebimento = "";
		}
		return percentual_minimo_recebimento;
	}

	public void setPercentual_minimo_recebimento(String percentual_minimo_recebimento) {
		this.percentual_minimo_recebimento = percentual_minimo_recebimento;
	}

	public String getValor_maximo_recebimento() {
		if (valor_maximo_recebimento == null) {
			valor_maximo_recebimento = "";
		}

		return valor_maximo_recebimento;
	}

	public void setValor_maximo_recebimento(String valor_maximo_recebimento) {
		this.valor_maximo_recebimento = valor_maximo_recebimento;
	}

	public String getPercentual_maximo_recebimento() {
		if (percentual_maximo_recebimento == null) {
			percentual_maximo_recebimento = "";
		}
		return percentual_maximo_recebimento;
	}

	public void setPercentual_maximo_recebimento(String percentual_maximo_recebimento) {
		this.percentual_maximo_recebimento = percentual_maximo_recebimento;
	}

}
