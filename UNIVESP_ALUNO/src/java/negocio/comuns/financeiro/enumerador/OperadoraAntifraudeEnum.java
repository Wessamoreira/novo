package negocio.comuns.financeiro.enumerador;


import java.util.UUID;

public enum OperadoraAntifraudeEnum {

    NENHUM(false),
    GETNET(true);

    private String urlAntifraude;
    private String sessionId;
    private final boolean sessionIdObrigatorio;

    OperadoraAntifraudeEnum(boolean sessionIdObrigatorio) {
        this.sessionIdObrigatorio = sessionIdObrigatorio;
        this.urlAntifraude = "";
    }

//    public String getUrlFingerprintAntifraude() {
//
//        if (this == OperadoraAntifraudeEnum.GETNET) {
//            sessionId = configuracaoFinanceiro.getIdEcommerceGetnet() + UUID.randomUUID();
//            urlAntifraude = AmbienteCartaoCreditoEnum.PRODUCAO.equals(configuracaoFinanceiro.getAmbienteCartaoCreditoEnum())
//                    ? "https://api.globalgetnet.com/dpm/digital-platform/antifraud/afdf.js?session=" + sessionId + "&country=BR"
//                    : "https://api.pre.globalgetnet.com/dpm/digital-platform/antifraud/afdf.js?session=" + sessionId + "&country=BR";
//            return urlAntifraude;
//        }
//
//        return urlAntifraude;
//    }

    public String getSessionId() {
        return sessionId;
    }

    public boolean getSessionIdObrigatorio() {
        return sessionIdObrigatorio;
    }
}
