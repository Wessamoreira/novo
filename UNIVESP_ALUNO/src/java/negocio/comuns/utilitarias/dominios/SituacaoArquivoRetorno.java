package negocio.comuns.utilitarias.dominios;

/**
 *  @author Diego
 */
public enum SituacaoArquivoRetorno {

    PROCESSANDO_ARQUIVO("PA", "O arquivo está sendo processado."),
    //ARQUIVO_PROCESSADO("AP", "O arquivo já foi processado."),
    ARQUIVO_PROCESSADO("AP", "Arquivo processado e contas não baixadas."),
    BAIXANDO_CONTAS("BC", "Efetuando baixa das contas encontradas."),
    CONTAS_BAIXADAS("CB", "Baixa das contas efetuada."),
	ARQUIVO_PROCESSADO_OFX("AP", "Arquivo processado com sucesso."),;
    String valor;
    String descricao;

    SituacaoArquivoRetorno(String valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    public static SituacaoArquivoRetorno getEnum(String valor) {
        SituacaoArquivoRetorno[] valores = values();
        for (SituacaoArquivoRetorno obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
        SituacaoArquivoRetorno obj = getEnum(valor);
        if (obj != null) {
            return obj.getDescricao();
        }
        return valor;
    }

    public String getValor() {
        if (valor == null) {
            valor = "";
        }
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getDescricao() {
        if (descricao == null) {
            descricao = "";
        }
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
