package negocio.comuns.utilitarias.dominios;

/**
 *
 * @author Diego
 */
public enum AtividadeFormacaoComplementar {

    PESQUISA(1, "PE", "Pesquisa"),
    PESQUISA_REMUNERADA(1, "PR", "Pesquisa - Remunerada"),
    EXTENSAO(2, "EX", "Extensão"),
    EXTENSAO_REMUNERADA(2, "ER", "Extensão - Remunerada"),
    MONITORIA(3, "MO", "Monitoria"),
    MONITORIA_REMUNERADA(3, "MR", "Monitoria - Remunerada"),
    ESTAGIO_NAO_OBRIGATORIO(4, "EN", "Estágio Não Obrigatorio"),
    ESTAGIO_NAO_OBRIGATORIO_REMUNERADO(4, "ES", "Estágio Não Obrigatorio - Remunerado");
    
    int codigoCenso;
    String valor;
    String descricao;
    
    AtividadeFormacaoComplementar(int codigoCenso, String valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
        this.codigoCenso = codigoCenso;
    }

    public static AtividadeFormacaoComplementar getEnum(String valor) {
        AtividadeFormacaoComplementar[] valores = values();
        for (AtividadeFormacaoComplementar obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
        AtividadeFormacaoComplementar obj = getEnum(valor);
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
