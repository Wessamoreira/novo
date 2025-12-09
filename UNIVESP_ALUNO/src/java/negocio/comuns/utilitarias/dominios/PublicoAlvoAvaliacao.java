package negocio.comuns.utilitarias.dominios;

/**
 *
 * @author Carlos
 */
public enum PublicoAlvoAvaliacao {

    ALUNO("AL", "Todos Alunos"),
    PROFESSOR("PR", "Todos Professores"),
    FUNCIONARIO("FU", "Todos Funcionários/Gestores"),
    COORDENADOR("CO", "Todos Coordenadores"),
    CURSO("CU", "Curso"),
    TURMA("TU", "Turma"),
    TODOS("TO", "Todos");

    
    private String valor;
    private String descricao;

    PublicoAlvoAvaliacao(String valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    public static PublicoAlvoAvaliacao getEnum(String valor) {
        PublicoAlvoAvaliacao[] valores = values();
        for (PublicoAlvoAvaliacao obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
        PublicoAlvoAvaliacao obj = getEnum(valor);
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
