package negocio.comuns.utilitarias.dominios;

/**
 *
 * @author Diego
 */
public enum SituacaoCancelamentoTrancamentoEnum {

    FINALIZADO_DEFERIDO("FD", "Finalizado - Deferido"),
    FINALIZADO_INDEFERIDO("FI", "Finalizado - Indeferido"),
    EM_EXECUCAO("EX", "Em Execução"),
    PENDENTE("PE", "Novo - Aguardando Início Execução"),
    AGUARDANDO_PAGAMENTO("AP", "Aguardando Pagamento"),
    ISENTO("IS", "Isento"),
    PRONTO_PARA_RETIRADA("PR", "Pronto para Retirada"),
    ATRASADO("AT", "Atrasado"),
    ESTORNADO("ES", "Estornado");
    
    String valor;
    String descricao;

    SituacaoCancelamentoTrancamentoEnum(String valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    public static SituacaoCancelamentoTrancamentoEnum getEnum(String valor) {
        SituacaoCancelamentoTrancamentoEnum[] valores = values();
        for (SituacaoCancelamentoTrancamentoEnum obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
        SituacaoCancelamentoTrancamentoEnum obj = getEnum(valor);
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
