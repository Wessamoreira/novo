package negocio.comuns.utilitarias.dominios;

public enum IntegracaoTechCertEnum {
    URL_HOMOLOGACAO("https://signer.techcert.com.br"),
    TOKEN_HOMOLOGACAO("UNIVESP|e3e1d711d72a214491af109cc582a925e1e41faa2a5045bbb5a2fc5ee1f60a64"),
    URL_PRODUCAO("URL_PRODUCAO"),
    TOKEN_PRODUCAO("TOKEN_PRODUCAO"),
    API_KEY("X-Api-Key");
    String valor;

    IntegracaoTechCertEnum(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }
}
