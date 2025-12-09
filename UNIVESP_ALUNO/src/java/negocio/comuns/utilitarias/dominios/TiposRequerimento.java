package negocio.comuns.utilitarias.dominios;

/**
 *
 * @author Diego
 */
public enum TiposRequerimento {

	APROVEITAMENTO_DISCIPLINA("AD", "Aproveitamento Disciplina"),
	ATIVIDADE_DISCURSIVA("ATIVIDADE_DISCURSIVA", "Atividade Discursiva"),
	SEGUNDA_CHAMADA("SEGUNDA_CHAMADA", "2ª Chamada"),
	CANCELAMENTO("CA", "Cancelamento"),
	CARTEIRINHA_ESTUDANTIL("CE", "Carteirinha Estudantil"),
    COLACAO_GRAU("CG", "Colação Grau"),
    DECLARACAO("DE", "Declaração"),
    HISTORICO("HI", "Histórico"),
    OUTROS("OU", "Outros"),
    PORTADOR_DE_DIPLOMA("PO", "Portador de Diploma"),
    EXPEDICAO_DIPLOMA("ED", "Expedição de Diploma"),
    REATIVACAO_MATRICULA("RM", "Reativação de Matrícula"),
    REPOSICAO("RE", "Reposição"),
    TAXA_TCC("TC", "Trabalho de Conclusão de Curso - TCC"),
    TRANCAMENTO("TR", "Trancamento"),
    TRANSF_ENTRADA("TE", "Transf. Externa"),
    TRANSF_SAIDA("TS", "Transf. Saída"),
    TRANSF_INTERNA("TI", "Transf. Interna"),
	INCLUSAO_DISCIPLINA("INCLUSAO_DISCIPLINA", "Inclusão Disciplina"),
	CERTIFICADO_MODULAR("CM", "Certificado Modular"),
	EMISSAO_CERTIFICADO("EC", "Emissão Certificado"),
	CERTIFICADO_PARTICIPACAO_TCC("CPT", "Certificado Participação TCC"),
	CONTRATO_ESTAGIO_NAO_OBRIGATORIO("CENO", "Contrato de Estágio Não Obrigatório");
	
    String valor;
    String descricao;

    TiposRequerimento(String valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    public static TiposRequerimento getEnum(String valor) {
        TiposRequerimento[] valores = values();
        for (TiposRequerimento obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
        TiposRequerimento obj = getEnum(valor);
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
