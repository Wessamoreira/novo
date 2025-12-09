package negocio.comuns.utilitarias.dominios;

/**
 *
 * @author Rodrigo
 */
public enum OrigemArquivo {
    
    INSTITUICAO("IN", "Instituição"),
    PROFESSOR("PR", "Professor"),
    SETRANSP("SE", "Setransp"),
    SERASA("SR", "Setransp"),
    CONTABIL("CO", "Contabil"),
    CENSO_ALUNO("CA", "Censo Aluno"),
    CENSO_PROFESSOR("CP", "Censo Professor"),
    CENSO_TURMA("CT", "Censo Turma"),
    CENSO_LAYOUT_TECNICO("LT", "Censo Layout Tecnico"),
    ALUNO("AL", "Aluno"),    
    OBRA_DIGITAL("OD", "Obra Digital"),
    OBRA_SUMARIO("OS", "Obra Sumário"),
    REMESSA("RE", "Remessa"),
	REMESSA_PG("RP", "Remessa Conta Pagar"),
    AMBAS("AM", "Ambas"),
    DOCUMENTACAO_MATRICULA("DM", "Documentação Matrícula"),
    DOCUMENTACAO_PROFESSOR("DP", "Documentação Professor"),
    REQUERIMENTO("RQ", "Requerimento"),
    TRAMITE_COTACAO_COMPRA("TC", "tramiteCotacaoCompra"),
    ATENDIMENTO("AT", "Atendimento"),
    ATIVIDADE_DISCURSIVA("AD","Atividade Discursiva"),
    CENSO_LAYOUT_ENSINO_MEDIO("EM","Censo Ensino Médio Aluno"),
    CENSO_LAYOUT_EDUCACAO_BASICA_TECNICO("EB","Censo Educação Básica ou Técnico Aluno"),
    INTEGRACAO_FINANCEIRO("IF", "Integração Financeiro"),
	DOCUMENTO_GED("DC", "Documento GED"),
	QUESTAO_EAD("QE", "Questão EAD"),
	AFASTAMENTO_FUNCIONARIO("AF", "Afastamento do Funcionário"), 
	PROCESSO_SELETIVO_INSCRICAO("PSI", "Processo Seletivo Inscrição"),
	CERTIFICADO_DIGITAL_PESSOA("CDPE", "Certificado Digital Pessoa"),
	CERTIFICADO_DIGITAL_INSTITUICAO("CDIN", "Certificado Digital Instituição"),
	CERTIFICADO_GSUITE("CGS", "Certificado Gsuite"),
	LAYOUT_RELATORIO_SEI_DECIDIDIR("LR", "Layout Relatório SEI Decidir"),
	CERTIFICADO_GED("CG", "Certificado GED"),
	SELO_GED("SG", "Selo GED"),
	REQUISICAO("RS", "Requisicao"),
	ATIVIDADE_EXTRA_CLASSE_PROFESSOR("AECL", "Atividade Extra Classe Professor."),
	HISTORICO_ALUNO("HA","Historico Aluno"),
	IMPOSTO_RENDA("IR","Imposto de Renda"),
	TEXTO_PADRAO("TP","Texto Padrão"),
	TEXTO_PADRAO_DECLARACAO("TD","Texto Padrão Declaracao"),
	PERGUNTA_RESPOSTA_ORIGEM_ANEXO("PERGUNTA_RESPOSTA_ORIGEM_ANEXO", "Pergunta Resposta Origem Anexo"),
	PERGUNTA_RESPOSTA_ORIGEM("PERGUNTA_RESPOSTA_ORIGEM", "Pergunta Resposta Origem"),
	PROCESSO_SELETIVO_QUESTOES("PS", "Questões Processo Seletivo"),
	PROCESSO_SELETIVO_IMPORTACAO("PROCESSO_SELETIVO_IMPORTACAO", "Processo Seletivo Importação"),
	LAYOUT_HISTORICO("LH", "Layout de Histórico"),
	MATRICULA_ENADE("ME", "Matrícula Enade"),
	IMPORTACAO_CID ("CID", "Importação Arquivo Cid"),
	DOCUMENTO_ASSINADO("DOCASS", "Documento Assinado");

    String valor;
    String descricao;

    OrigemArquivo( String valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    public static OrigemArquivo getEnum(String valor) {
        OrigemArquivo[] valores = values();
        for (OrigemArquivo obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
        OrigemArquivo obj = getEnum(valor);
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
