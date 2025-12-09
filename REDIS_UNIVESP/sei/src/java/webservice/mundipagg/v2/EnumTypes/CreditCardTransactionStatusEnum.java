package webservice.mundipagg.v2.EnumTypes;

/**
 * Enumerador de Status para transação de Cartão de Crédito
 */
public enum CreditCardTransactionStatusEnum {
    
    /**
     * Autorizado e pendente de captura
     */
    AuthorizedPendingCapture(""),

    /**
     * Não autorizado
     */
    NotAuthorized(""),

    /**
     * Previsão de chargeback
     */
    ChargebackPreview(""),

    /**
     * Previsão de estorno
     */
    RefundPreview(""),

    /**
     * Previsão de depósito
     */
    DepositPreview(""),

    /**
     * Capturado
     */
    Captured(""),

    /**
     * Capturado parcialmente
     */
    PartialCapture(""),

    /**
     * Estornado
     */
    Refunded(""),

    /**
     * Cancelado
     */
    Voided(""),

    /**
     * Depositado
     */
    Deposited(""),

    /**
     * Aberto e pendente de autorização
     */
    OpenedPendingAuth(""),

    /**
     * Chargeback
     */
    Chargedback(""),

    /**
     * Pendente de cancelamento
     */
    PendingVoid(""),

    /**
     * Inválido
     */
    Invalid4(""),

    /**
     * Autorizado parcialmente
     */
    PartialAuthorize(""),

    /**
     * Estornado parcialmente
     */
    PartialRefunded(""),

    /**
     * Capturado com valor a cima do original
     */
    OverCapture(""),

    /**
     * Parcialmente capturado
     */
    PartialVoid(""),

    /**
     * Pendente de estorno
     */
    PendingRefund(""),

    /**
     * Não agendado
     */
    UnScheduled(""),

    /**
     * Criado
     */
    Created(""),

    /**
     * Autorizado parcialmente
     */
    PartialAuthorized(""),

    /**
     * Não localizado na adquirente
     */
    NotFoundInAcquirer(""),

    /**
     * Pendente de autorização
     */
    PendingAuthorize(""),

    /**
     * Com erro
     */
    WithError("");
	
	private String nome;
        
    private CreditCardTransactionStatusEnum(String nome) {
    	this.nome = nome;
    }

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
}
