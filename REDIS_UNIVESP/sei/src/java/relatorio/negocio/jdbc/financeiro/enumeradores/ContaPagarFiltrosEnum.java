package relatorio.negocio.jdbc.financeiro.enumeradores;

/**
 * 
 * @author brethener
 */

public enum ContaPagarFiltrosEnum {

    TODOS(1, "Todos"),
    NENHUM(2, "Nenhum"),
    FILTRAR(3, "Filtrar");

    private final int codigo;
    private final String descricao;

    ContaPagarFiltrosEnum(int codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public static ContaPagarFiltrosEnum valueOf(int i) {
        if (i == FILTRAR.codigo) {
            return ContaPagarFiltrosEnum.FILTRAR;
        } else if (i == NENHUM.codigo) {
            return ContaPagarFiltrosEnum.NENHUM;
        } else {
            return ContaPagarFiltrosEnum.TODOS;
        }
    }

}
