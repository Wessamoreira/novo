package webservice.techcert.comuns;

public enum StatusDocumentActionTechCertEnum {
    PENDING("Pending"),        // Pendente
    REFUSED("Refused"),        // Recusado
    FLOW_CONCLUDED("FlowConcluded"), // Fluxo concluído
    CONCLUDED("Concluded"),      // Concluído
    CANCELED("Canceled"),       // Cancelado
    EXPIRED("Expired");         // Expirado

    private final String nome;

    StatusDocumentActionTechCertEnum(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public static StatusDocumentActionTechCertEnum getFromnome(String nome) {
        for (StatusDocumentActionTechCertEnum status : StatusDocumentActionTechCertEnum.values()) {
            if (status.getNome().equalsIgnoreCase(nome)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Nome inválido: " + nome);
    }
}
