package negocio.comuns.financeiro.enumerador;

import integracoes.cartao.cielo.BandeiraCartaoCreditoEnum;
import negocio.comuns.utilitarias.UteisJSF;

public enum OperadoraCartaoCreditoEnum {
/**
 * Antes de colocar um item nesta listagem deve ser verificado se a CIELO e a REDE dão suporte para o tipo de orperadora
 * isto impacta diretamente na integração, Veja GerenciadorTransacaoCartao.montarDadosTransacaoCartaoCredito
 */
	AMEX("amex.png", PermitirCartaoEnum.CREDITO, BandeiraCartaoCreditoEnum.Amex),
	AURA("aura.png", PermitirCartaoEnum.CREDITO, BandeiraCartaoCreditoEnum.Aura),
	BANESCARD("banesCard.png", PermitirCartaoEnum.AMBOS,  null ),
	CABAL("cabal.png", PermitirCartaoEnum.AMBOS, null ),
	CASA_SHOW("casashow.png", PermitirCartaoEnum.CREDITO, BandeiraCartaoCreditoEnum.CasaShow),
	CREDSYSTEM("credSystem.png", PermitirCartaoEnum.CREDITO, BandeiraCartaoCreditoEnum.Credsystem),
	DINERS("dinner.png", PermitirCartaoEnum.CREDITO, BandeiraCartaoCreditoEnum.Diners),
	DISCOVER("discover.png", PermitirCartaoEnum.CREDITO, BandeiraCartaoCreditoEnum.Discover),
	ELO("elo.png", PermitirCartaoEnum.AMBOS, BandeiraCartaoCreditoEnum.Elo),
	GOODCARD("goodCard.png", PermitirCartaoEnum.CREDITO, null),
	GREENCARD("greenCard.png", PermitirCartaoEnum.CREDITO, null),
	HAVAN("havan.png", PermitirCartaoEnum.CREDITO, BandeiraCartaoCreditoEnum.Havan),
	HIPER_CARD("hipercard.png", PermitirCartaoEnum.AMBOS, BandeiraCartaoCreditoEnum.Hipercard),
	JCB("jcb.png", PermitirCartaoEnum.CREDITO, BandeiraCartaoCreditoEnum.JCB),
	MAESTRO("maestro.png", PermitirCartaoEnum.DEBITO, null),
	MASTERCARD("mastercard.png", PermitirCartaoEnum.CREDITO, BandeiraCartaoCreditoEnum.Mastercard),
	POLICARD("poliCard.png", PermitirCartaoEnum.CREDITO, null),
	SODEXO("sodexo.png", PermitirCartaoEnum.CREDITO, null),
	SOROCARD("soroCard.png", PermitirCartaoEnum.CREDITO, null),
	UNIONPAY("unionpay.png", PermitirCartaoEnum.AMBOS, null),
	VALECARD("valeCard.png", PermitirCartaoEnum.CREDITO, null),
	VEROCHEQUE("veroCheque.png", PermitirCartaoEnum.CREDITO, null),
	VISA("visa.png", PermitirCartaoEnum.CREDITO, BandeiraCartaoCreditoEnum.Visa),
	VISA_ELECTRON("visaelectron.png", PermitirCartaoEnum.DEBITO, null),
	VRBENEFICIOS("vrBeneficios.png", PermitirCartaoEnum.CREDITO, null);

	private String caminhoImagem;
	private PermitirCartaoEnum tipo;
	private BandeiraCartaoCreditoEnum bandeira;

	private OperadoraCartaoCreditoEnum(String caminhoImagem, PermitirCartaoEnum tipo, BandeiraCartaoCreditoEnum bandeira) {
		this.caminhoImagem = caminhoImagem;
		this.tipo = tipo;
		this.bandeira = bandeira;
	}

	public String getCaminhoImagem() {
		return caminhoImagem;
	}

	public void setCaminhoImagem(String caminhoImagem) {
		this.caminhoImagem = caminhoImagem;
	}

	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_OperadoraCartaoCreditoEnum_" + this.name());
	}

	public String getName() {
		return this.name();
	}

	public PermitirCartaoEnum getTipo() {
		return tipo;
	}

	public void setTipo(PermitirCartaoEnum tipo) {
		this.tipo = tipo;
	}

	public BandeiraCartaoCreditoEnum getBandeira() {
		return bandeira;
	}

	public void setBandeira(BandeiraCartaoCreditoEnum bandeira) {
		this.bandeira = bandeira;
	}
	
	
}
