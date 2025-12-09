package webservice.techcert.comuns;

public enum StatusFlowActionsTechCertEnum {
//    [ Created, Pending, Completed, Refused ]

    CREATED("Created"),        // Created
    PENDING("Pending"),        // Pending
    COMPLETED("Completed"), // Completed
    REFUSED("Refused");      // Refused

    private final String nome;

    StatusFlowActionsTechCertEnum(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public static StatusFlowActionsTechCertEnum getFromnome(String nome) {
        for (StatusFlowActionsTechCertEnum status : StatusFlowActionsTechCertEnum.values()) {
            if (status.getNome().equalsIgnoreCase(nome)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Nome inválido: " + nome);
    }

}

