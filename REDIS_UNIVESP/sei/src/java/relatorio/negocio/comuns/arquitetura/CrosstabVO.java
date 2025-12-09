package relatorio.negocio.comuns.arquitetura;

import java.math.BigDecimal;

import negocio.comuns.arquitetura.SuperVO;

public class CrosstabVO extends SuperVO {

	private static final long serialVersionUID = 1L;
	private Integer ordemColuna;
	private Integer ordemLinha;
	private String labelColuna;
	private String labelColuna2;
	private String labelLinha;
	private String labelLinha2;
	private String labelLinha3;
	private String labelLinha4;
	private String valorString;
	private String valorString2;
	private Integer ordernador;
	private Boolean valorBoolean;
	private Boolean valorBoolean2;
	private Integer valorInteger;
	private Integer valorInteger2;
	private Double valorDouble;
	private Double valorDouble2;
	private BigDecimal valorBigDecimal;
	private BigDecimal valorBigDecimal2;

	public Integer getOrdemColuna() {
		if (ordemColuna == null) {
			ordemColuna = 0;
		}
		return ordemColuna;
	}

	public void setOrdemColuna(Integer ordemColuna) {
		this.ordemColuna = ordemColuna;
	}

	public Integer getOrdemLinha() {
		if (ordemLinha == null) {
			ordemLinha = 0;
		}
		return ordemLinha;
	}

	public void setOrdemLinha(Integer ordemLinha) {
		this.ordemLinha = ordemLinha;
	}

	public String getLabelColuna() {
		return labelColuna;
	}

	public void setLabelColuna(String labelColuna) {
		this.labelColuna = labelColuna;
	}

	public String getLabelLinha() {
		return labelLinha;
	}

	public void setLabelLinha(String labelLinha) {
		this.labelLinha = labelLinha;
	}

	public String getLabelColuna2() {
		if (labelColuna2 == null) {
			labelColuna2 = "";
		}
		return labelColuna2;
	}

	public void setLabelColuna2(String labelColuna2) {
		this.labelColuna2 = labelColuna2;
	}

	public String getLabelLinha2() {
		return labelLinha2;
	}

	public void setLabelLinha2(String labelLinha2) {
		this.labelLinha2 = labelLinha2;
	}

	public String getValorString() {
		return valorString;
	}

	public void setValorString(String valorString) {
		this.valorString = valorString;
	}

	public Boolean getValorBoolean() {
		return valorBoolean;
	}

	public void setValorBoolean(Boolean valorBoolean) {
		this.valorBoolean = valorBoolean;
	}

	public Integer getValorInteger() {
		return valorInteger;
	}

	public void setValorInteger(Integer valorInteger) {
		this.valorInteger = valorInteger;
	}

	public Double getValorDouble() {
		return valorDouble;
	}

	public void setValorDouble(Double valorDouble) {
		this.valorDouble = valorDouble;
	}

	public String getLabelLinha3() {
		if (labelLinha3 == null) {
			labelLinha3 = "";
		}
		return labelLinha3;
	}

	public void setLabelLinha3(String labelLinha3) {
		this.labelLinha3 = labelLinha3;
	}

	public String getLabelLinha4() {
		if (labelLinha4 == null) {
			labelLinha4 = "";
		}
		return labelLinha4;
	}

	public void setLabelLinha4(String labelLinha4) {
		this.labelLinha4 = labelLinha4;
	}

	public Integer getOrdernador() {
		if (ordernador == null) {
			ordernador = 0;
		}
		return ordernador;
	}

	public void setOrdernador(Integer ordernador) {
		this.ordernador = ordernador;
	}

	public BigDecimal getValorBigDecimal() {
		return valorBigDecimal;
	}

	public void setValorBigDecimal(BigDecimal valorBigDecimal) {
		this.valorBigDecimal = valorBigDecimal;
	}
	
	public BigDecimal getValorBigDecimal2() {
		return valorBigDecimal2;
	}

	public void setValorBigDecimal2(BigDecimal valorBigDecimal2) {
		this.valorBigDecimal2 = valorBigDecimal2;
	}
	
	public Double getValorDouble2() {
		return valorDouble2;
	}

	public void setValorDouble2(Double valorDouble2) {
		this.valorDouble2 = valorDouble2;
	}

	public Boolean getValorBoolean2() {
		return valorBoolean2;
	}

	public void setValorBoolean2(Boolean valorBoolean2) {
		this.valorBoolean2 = valorBoolean2;
	}

	public Integer getValorInteger2() {
		return valorInteger2;
	}

	public void setValorInteger2(Integer valorInteger2) {
		this.valorInteger2 = valorInteger2;
	}		

	public String getValorString2() {
		return valorString2;
	}

	public void setValorString2(String valorString2) {
		this.valorString2 = valorString2;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CrosstabVO [ordemColuna=" + ordemColuna + ", ordemLinha=" + ordemLinha + ", labelColuna=" + labelColuna
				+ ", labelColuna2=" + labelColuna2 + ", labelLinha=" + labelLinha + ", labelLinha2=" + labelLinha2
				+ ", labelLinha3=" + labelLinha3 + ", labelLinha4=" + labelLinha4 + ", valorString=" + valorString
				+ ", valorBoolean=" + valorBoolean + ", valorInteger=" + valorInteger + ", valorDouble=" + valorDouble
				+ "]";
	}

}
