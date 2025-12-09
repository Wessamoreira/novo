package webservice.techcert.comuns;

public enum WebhookEventTypeTechCertVO {
    DOCUMENT_CONCLUDED("DocumentConcluded"),
    INVOICE_CLOSED("InvoiceClosed"),
    DOCUMENT_REFUSED("DocumentRefused"),
    DOCUMENT_APPROVED("DocumentApproved"),
    DOCUMENT_SIGNED("DocumentSigned"),
    DOCUMENT_CANCELED("DocumentCanceled"),
    DOCUMENT_EXPIRED("DocumentExpired"),
    DOCUMENTS_CREATED("DocumentsCreated"),
    DOCUMENT_DELETED("DocumentDeleted");

    private final String value;

    WebhookEventTypeTechCertVO(String value) {
        this.value = value;
    }

    /**
     * Retorna o valor da enumeração como String.
     */
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
