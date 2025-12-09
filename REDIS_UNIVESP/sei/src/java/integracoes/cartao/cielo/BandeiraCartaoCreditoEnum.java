package integracoes.cartao.cielo;


/**
 * Possiveis erros da integração com a CIELO  
 * 29/06/2017
 * @author Kennedy
 */
public enum BandeiraCartaoCreditoEnum {

    Visa("Visa"),
    Mastercard("Master"),
    Hipercard("Hipercard"),
    Amex("Amex"),
    Diners("Diners"),
    Elo("Elo"),
    Aura("Aura"),
    Discover("Discover"),
    CasaShow("CasaShow"),
    Havan("Havan"),
    HugCard("HugCard"),
    AndarAki("AndarAki"),
    Credsystem("Credsystem"),
    JCB("Jcb"),
    LeaderCard("LeaderCard");
   
	private String descricao;
	
	private BandeiraCartaoCreditoEnum(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}

}
