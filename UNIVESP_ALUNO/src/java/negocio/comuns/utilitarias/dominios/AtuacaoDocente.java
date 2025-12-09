package negocio.comuns.utilitarias.dominios;


/**
 *
 * @author Diego
 */
public enum AtuacaoDocente {

	CURSO_SEQUENCIAL_FORMACAO_ESPECIFICA("01", "Ensino em curso sequencial de formação específica"),
    CURSO_GRADUACAO_PRESENCIAL("02", "Ensino em curso de graduação presencial"),
    CURSO_EAD("03", "Ensino em curso de graduação a distância"),
    POSGRADUACAO_STRICTO_SENSU_PRESENCIAL("04", "Ensino de pósgraduação stricto sensu presencial"),
    POSGRADUACAO_STRICTO_SENSU_EAD("05", "Ensino de pós-graduação stricto sensu a distância"),
    PESQUISA("06", "Pesquisa"),
    PESQUISA_COM_BOLSA("07", "Pesquisa com Bolsa"),
    EXTENSAO("08", "Extensão"),
    GESTAO_PLANEJAMENTO_AVALIACAO("09", "Gestão, planejamento e avaliação");

    String valor;
    String descricao;

    AtuacaoDocente(String valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    public static AtuacaoDocente getEnum(String valor) {
        AtuacaoDocente[] valores = values();
        for (AtuacaoDocente obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
        AtuacaoDocente obj = getEnum(valor);
        if (obj != null) {
            return obj.getDescricao();
        }
        return valor;
    }

    public static String getAbreviatura(String valor) {
        AtuacaoDocente obj = getEnum(valor);
        if (obj != null) {
            return obj.getDescricao().substring(0, 3);
        }
        return valor;
    }

    public static String getValor(Integer valor) {
        AtuacaoDocente obj = getEnum("0"+valor);
        if (obj != null) {
            return obj.getValor();
        }
        return "0";
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
