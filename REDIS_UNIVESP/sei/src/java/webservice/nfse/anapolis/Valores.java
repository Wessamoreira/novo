package webservice.nfse.anapolis;

import java.math.BigDecimal;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

@XStreamConverter(DoubleConverter.class)
@XStreamAlias("tc:Valores")
public class Valores {

	@XStreamAlias("tc:ValorServicos")
	private Double ValorServicos;
	
	@XStreamAlias("tc:ValorDeducoes")
	private Double ValorDeducoes;
	
	@XStreamAlias("tc:ValorPis")
	private Double ValorPis;
	
	@XStreamAlias("tc:ValorCofins")
	private Double ValorCofins;
	
	@XStreamAlias("tc:ValorInss")
	private Double ValorInss;
	
	@XStreamAlias("tc:ValorIr")
	private Double ValorIr;
	
	@XStreamAlias("tc:ValorCsll")
	private Double ValorCsll;
	
	@XStreamAlias("tc:IssRetido")
	private SimNao IssRetido;
	
	@XStreamAlias("tc:OutrasRetencoes")
	private Double OutrasRetencoes;
	
	@XStreamAlias("tc:ValorIss")
	private Double ValorIss;
	
	@XStreamAlias("tc:BaseCalculo")
	private Double BaseCalculo;
	
	@XStreamAlias("tc:Aliquota")
	private Double Aliquota;
	
	@XStreamAlias("tc:ValorLiquidoNfse")
	private Double ValorLiquidoNfse;
	
	@XStreamAlias("tc:DescontoIncondicionado")
	private Double DescontoIncondicionado;
	
	@XStreamAlias("tc:DescontoCondicionado")
	private Double DescontoCondicionado;
	
	public SimNao getIssRetido() {
		return IssRetido;
	}

	public void setIssRetido(SimNao issRetido) {
		IssRetido = issRetido;
	}

	public Double getBaseCalculo() {
		return BaseCalculo;
	}

	public void setBaseCalculo(Double baseCalculo) {
		BaseCalculo = baseCalculo;
	}

	public Double getValorLiquidoNfse() {
		return ValorLiquidoNfse;
	}

	public void setValorLiquidoNfse(Double valorLiquidoNfse) {
		ValorLiquidoNfse = valorLiquidoNfse;
	}

	public Double getValorServicos() {
		return ValorServicos;
	}

	public void setValorServicos(Double valorServicos) {
		if(Aliquota != null) {
			/*
			 * Alterado para autorizar nota fiscal com <tc:ValorServicos>562.25</tc:ValorServicos>
			 * <tc:ValorIss>11.25</tc:ValorIss> para <tc:ValorIss>11.24</tc:ValorIss>
			 */
			ValorIss = (new BigDecimal((valorServicos * Aliquota / 100) * 100.0).setScale(0, BigDecimal.ROUND_HALF_DOWN).intValue()) / 100.0;
//			ValorIss = (new BigDecimal((valorServicos * Aliquota / 100) * 100.0 + 0.01).setScale(0, BigDecimal.ROUND_HALF_DOWN).intValue()) / 100.0;
//			ValorIss = valorServicos * Aliquota / 100;
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
			/*
			 * Alterado para autorizar nota fiscal com <tc:ValorServicos>562.25</tc:ValorServicos>
			 * <tc:ValorIss>11.25</tc:ValorIss> para <tc:ValorIss>11.24</tc:ValorIss>
			 */
			ValorIss = (new BigDecimal((ValorServicos * aliquota / 100) * 100.0).setScale(0, BigDecimal.ROUND_HALF_DOWN).intValue()) / 100.0;
//			ValorIss = (new BigDecimal((ValorServicos * aliquota / 100) * 100.0 + 0.01).setScale(0, BigDecimal.ROUND_HALF_DOWN).intValue()) / 100.0;
//			ValorIss = ValorServicos * aliquota / 100;
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

	@Override
	public String toString() {
		return "Valores [ValorServicos=" + ValorServicos + ", ValorDeducoes=" + ValorDeducoes + ", ValorPis=" + ValorPis + ", ValorCofins=" + ValorCofins + ", ValorInss=" + ValorInss + ", ValorIr=" + ValorIr + ", ValorCsll=" + ValorCsll + ", IssRetido=" + IssRetido + ", OutrasRetencoes=" + OutrasRetencoes + ", ValorIss=" + ValorIss + ", BaseCalculo=" + BaseCalculo + ", Aliquota=" + Aliquota + ", ValorLiquidoNfse=" + ValorLiquidoNfse + ", DescontoIncondicionado=" + DescontoIncondicionado + ", DescontoCondicionado=" + DescontoCondicionado + "]";
	}
}