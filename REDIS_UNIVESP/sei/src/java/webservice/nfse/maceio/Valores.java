package webservice.nfse.maceio;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

@XStreamConverter(DoubleConverter.class)
public class Valores {

	@XStreamAlias("ns3:ValorServicos")
	private Double ValorServicos;
	
	@XStreamAlias("ns3:ValorDeducoes")
	private Double ValorDeducoes;
	
	@XStreamAlias("ns3:ValorPis")
	private Double ValorPis;
	
	@XStreamAlias("ns3:ValorCofins")
	private Double ValorCofins;
	
	@XStreamAlias("ns3:ValorInss")
	private Double ValorInss;
	
	@XStreamAlias("ns3:ValorIr")
	private Double ValorIr;
	
	@XStreamAlias("ns3:ValorCsll")
	private Double ValorCsll;
	
	@XStreamAlias("ns3:IssRetido")
	private SimNao IssRetido;
	
	@XStreamAlias("ns3:ValorIss")
	private Double ValorIss;
	
	@XStreamAlias("ns3:OutrasRetencoes")
	private Double OutrasRetencoes;
	
	@XStreamAlias("ns3:Aliquota")
	private Double Aliquota;
	
	@XStreamAlias("ns3:DescontoIncondicionado")
	private Double DescontoIncondicionado;
	
	@XStreamAlias("ns3:DescontoCondicionado")
	private Double DescontoCondicionado;

	@Override
	public String toString() {
		return "Valores [ValorServicos=" + ValorServicos + ", ValorDeducoes="
				+ ValorDeducoes + ", ValorPis=" + ValorPis + ", ValorCofins="
				+ ValorCofins + ", ValorInss=" + ValorInss + ", ValorIr="
				+ ValorIr + ", ValorCsll=" + ValorCsll + ", OutrasRetencoes="
				+ OutrasRetencoes + ", ValorIss=" + ValorIss + ", Aliquota="
				+ Aliquota + ", DescontoIncondicionado="
				+ DescontoIncondicionado + ", DescontoCondicionado="
				+ DescontoCondicionado + "]";
	}

	public Double getValorServicos() {
		return ValorServicos;
	}

	public void setValorServicos(Double valorServicos) {
		if(Aliquota != null) {
			ValorIss = valorServicos * Aliquota / 100;
		}
		ValorServicos = valorServicos;
	}

	public Double getValorDeducoes() {
		return ValorDeducoes;
	}

	public void setValorDeducoes(Double valorDeducoes) {
		ValorDeducoes = valorDeducoes;
	}

	public Double getValorPis() {
		return ValorPis;
	}

	public void setValorPis(Double valorPis) {
		ValorPis = valorPis;
	}

	public Double getValorCofins() {
		return ValorCofins;
	}

	public void setValorCofins(Double valorCofins) {
		ValorCofins = valorCofins;
	}

	public Double getValorInss() {
		return ValorInss;
	}

	public void setValorInss(Double valorInss) {
		ValorInss = valorInss;
	}

	public Double getValorIr() {
		return ValorIr;
	}

	public void setValorIr(Double valorIr) {
		ValorIr = valorIr;
	}

	public Double getValorCsll() {
		return ValorCsll;
	}

	public void setValorCsll(Double valorCsll) {
		ValorCsll = valorCsll;
	}

	public Double getOutrasRetencoes() {
		return OutrasRetencoes;
	}

	public void setOutrasRetencoes(Double outrasRetencoes) {
		OutrasRetencoes = outrasRetencoes;
	}

	public Double getValorIss() {
		return ValorIss;
	}

	public void setValorIss(Double valorIss) {
		ValorIss = valorIss;
	}

	public Double getAliquota() {
		return Aliquota;
	}

	public void setAliquota(Double aliquota) {
		if(ValorServicos != null) {
			ValorIss = ValorServicos * aliquota / 100;
		}
		Aliquota = aliquota;
	}

	public Double getDescontoIncondicionado() {
		return DescontoIncondicionado;
	}

	public void setDescontoIncondicionado(Double descontoIncondicionado) {
		DescontoIncondicionado = descontoIncondicionado;
	}

	public Double getDescontoCondicionado() {
		return DescontoCondicionado;
	}

	public void setDescontoCondicionado(Double descontoCondicionado) {
		DescontoCondicionado = descontoCondicionado;
	}

	public SimNao getIssRetido() {
		return IssRetido;
	}

	public void setIssRetido(SimNao issRetido) {
		IssRetido = issRetido;
	}
	
}