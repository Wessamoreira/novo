package webservice.boletoonline.caixaEconomicaFederal.classes;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class TituloVO {
	
	@XStreamAlias("NOSSO_NUMERO")	
	private String nosso_Numero;
	
	@XStreamAlias("NUMERO_DOCUMENTO")	
	private String numero_documento;
	
	@XStreamAlias("DATA_VENCIMENTO")	
	private String data_vencimento;
	
	@XStreamAlias("VALOR")	
	private String valor;
	
	@XStreamAlias("TIPO_ESPECIE")	
	private String tipo_especie;
	
	@XStreamAlias("FLAG_ACEITE")	
	private String flag_aceite;
	
	@XStreamAlias("DATA_EMISSAO")	
	private String data_emissao;
	
	@XStreamAlias("JUROS_MORA")	
	private Juros_moraCaixaEconomicaVO juros_mora;
	
	@XStreamAlias("VALOR_ABATIMENTO")	
	private String valor_abatimento;
	
	@XStreamAlias("POS_VENCIMENTO")	
	private PosVencimentoBoletoOnlineCaixaEconomicaVO pos_vencimento;
	
	@XStreamAlias("CODIGO_MOEDA")	
	private String codigo_moeda;

	@XStreamAlias("PAGADOR")	
	private PagadorBoletoOnlineCaixaEconomicaVO pagador;
	
	@XStreamAlias("MULTA")	
	private MultaBoletoOnlineCaixaEconomicaVO  multa;
	
	@XStreamAlias("DESCONTOS")	
	private List<DescontosBoletoOnlineCaixaEconomicaVO> descontos;
	
	@XStreamAlias("FICHA_COMPESACAO")	
	private FichaCompensacaoBoletoOnlineCaixaEconomicaVO ficha_compensacao;
	
	@XStreamAlias("RECIBO_PAGADOR")	
	private ReciboPagadorBoletoOnlineCaixaEconomicaVO recibo_pagador;
	
	@XStreamAlias("PAGAMENTO")	
	private PagamentoBoletoOnlineCaixaEconomicaVO pagamento;
	
	public TituloVO() {	
		
	}

	public String getNosso_Numero() {
		return nosso_Numero;
	}

	public void setNosso_Numero(String nosso_Numero) {
		this.nosso_Numero = nosso_Numero;
	}

	public String getNumero_documento() {
		return numero_documento;
	}

	public void setNumero_documento(String numero_documento) {
		this.numero_documento = numero_documento;
	}

	public String getData_vencimento() {
		return data_vencimento;
	}

	public void setData_vencimento(String data_vencimento) {
		this.data_vencimento = data_vencimento;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getTipo_especie() {
		return tipo_especie;
	}

	public void setTipo_especie(String tipo_especie) {
		this.tipo_especie = tipo_especie;
	}

	public String getFlag_aceite() {
		return flag_aceite;
	}

	public void setFlag_aceite(String flag_aceite) {
		this.flag_aceite = flag_aceite;
	}

	public String getData_emissao() {
		return data_emissao;
	}

	public void setData_emissao(String data_emissao) {
		this.data_emissao = data_emissao;
	}

	public Juros_moraCaixaEconomicaVO getJuros_mora() {
		return juros_mora;
	}

	public void setJuros_mora(Juros_moraCaixaEconomicaVO juros_mora) {
		this.juros_mora = juros_mora;
	}

	public String getValor_abatimento() {
		return valor_abatimento;
	}

	public void setValor_abatimento(String valor_abatimento) {
		this.valor_abatimento = valor_abatimento;
	}

	public PosVencimentoBoletoOnlineCaixaEconomicaVO getPos_vencimento() {
		return pos_vencimento;
	}

	public void setPos_vencimento(PosVencimentoBoletoOnlineCaixaEconomicaVO pos_vencimento) {
		this.pos_vencimento = pos_vencimento;
	}

	public String getCodigo_moeda() {
		return codigo_moeda;
	}

	public void setCodigo_moeda(String codigo_moeda) {
		this.codigo_moeda = codigo_moeda;
	}

	public PagadorBoletoOnlineCaixaEconomicaVO getPagador() {
		return pagador;
	}

	public void setPagador(PagadorBoletoOnlineCaixaEconomicaVO pagador) {
		this.pagador = pagador;
	}

	public MultaBoletoOnlineCaixaEconomicaVO getMulta() {
		return multa;
	}

	public void setMulta(MultaBoletoOnlineCaixaEconomicaVO multa) {
		this.multa = multa;
	}

	public List<DescontosBoletoOnlineCaixaEconomicaVO> getDescontos() {
		return descontos;
	}

	public void setDescontos(List<DescontosBoletoOnlineCaixaEconomicaVO> descontos) {
		this.descontos = descontos;
	}

	public FichaCompensacaoBoletoOnlineCaixaEconomicaVO getFicha_compensacao() {
		return ficha_compensacao;
	}

	public void setFicha_compensacao(FichaCompensacaoBoletoOnlineCaixaEconomicaVO ficha_compensacao) {
		this.ficha_compensacao = ficha_compensacao;
	}

	public ReciboPagadorBoletoOnlineCaixaEconomicaVO getRecibo_pagador() {
		return recibo_pagador;
	}

	public void setRecibo_pagador(ReciboPagadorBoletoOnlineCaixaEconomicaVO recibo_pagador) {
		this.recibo_pagador = recibo_pagador;
	}

	public PagamentoBoletoOnlineCaixaEconomicaVO getPagamento() {
		return pagamento;
	}

	public void setPagamento(PagamentoBoletoOnlineCaixaEconomicaVO pagamento) {
		this.pagamento = pagamento;
	}
	
	
	

}
