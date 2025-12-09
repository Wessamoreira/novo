package negocio.comuns.utilitarias.dominios;

/**
 *
 * @author Alberto
 */
public enum TipoVisaoAcesso {

    VISAO_ALUNO("VAL", "Visão Aluno"),
    VISAO_CANDIDATO("VCA", "Visão Candidato"),
    VISAO_COORDENADOR("VCO", "Visão Coordenador"),
    VISAO_DIRETOR_MULTI_CAMPUS("VDM", "Visão Diretor Multi Campus"),
    VISAO_FUNCIONARIO("VFU", "Visão Funcionário"),
    VISAO_PARCEIRO("VPA", "Visão Parceiro"),
    VISAO_RESPONSAL_LEGAL("VRL", "Visão Responsável Legal"),
    VISAO_PROFESSOR("VPR", "Visão Professor");
    
    String valor;
    String descricao;

    TipoVisaoAcesso(String valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    public static TipoVisaoAcesso getEnum(String valor) {
        TipoVisaoAcesso[] valores = values();
        for (TipoVisaoAcesso obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
        TipoVisaoAcesso obj = getEnum(valor);
        if (obj != null) {
            return obj.getDescricao();
        }
        return valor;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
