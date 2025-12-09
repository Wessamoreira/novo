/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.utilitarias.dominios;

/**
 *
 * @author EDIGARANTONIO
 */
public enum TipoHistorico {
	
    ISENTO_PORTADOR("ID", "Insento Portador Diploma", false),
    HISTORICO_TRANSFERENCIA("AP", "Histórico Transferência", false),
    ISENTO_NOTOTIO_SABER("IS", "Insento Notótio Saber", false),
    /**
     * Esta situação é utilizada para os alunos que vieram de transferencia e na instituição anterior a disciplina incluida não era exigida, porém
     * na unidade em que o aluno está entrando esta disciplina é obrigatório: EX: O aluno está entrando no 2º ano do Ensino Médio e não Cursou a Disciplina
     * Filosofia no 1º Ano pois a escola anterior não exigia, então será incluida esta disciplina como adaptação
     */
    ADAPTACAO("AD", "Adaptação", true),
    COMPLEMENTACAO_CARGA_HORARIA("CO", "Complementação de Carga Horária", true),
    /**
     * Esta situação é utilizada para alunos que reprovaram a disciplina no ano anterior e está cursando a mesma disciplina novamente.
     */
    DEPENDENCIA("DE", "Dependência", true),
    NORMAL("NO", "Normal", true);
    
    String valor;
    String descricao;
    Boolean apresentarLancamentoNota;

    TipoHistorico(String valor, String descricao, boolean apresentarLancamentoNota) {
        this.valor = valor;
        this.descricao = descricao;
        this.apresentarLancamentoNota = apresentarLancamentoNota;
    }

    public static TipoHistorico getEnum(String valor) {
    	if(valor == null || valor.trim().isEmpty()){
    		return TipoHistorico.NORMAL;
    	}
        TipoHistorico[] valores = values();
        for (TipoHistorico obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
        TipoHistorico obj = getEnum(valor);
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

	public Boolean getApresentarLancamentoNota() {
		
		return apresentarLancamentoNota;
	}

	public void setApresentarLancamentoNota(Boolean apresentarLancamentoNota) {
		this.apresentarLancamentoNota = apresentarLancamentoNota;
	}
    
    
}
