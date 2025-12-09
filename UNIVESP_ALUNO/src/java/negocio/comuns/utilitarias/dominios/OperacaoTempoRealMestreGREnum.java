package negocio.comuns.utilitarias.dominios;

public enum OperacaoTempoRealMestreGREnum {

    TRANCAMENTO("TRANCAMENTO", "Trancamento"),
    ABANDONO_CURSO("ABANDONO_CURSO", "Abandono de Curso"),
    ESTORNO_ABANDONO_CURSO("ESTORNO_ABANDONO_CURSO", "Estorno Abandono de Curso"),
    ESTORNO_TRANCAMENTO("ESTORNO_TRANCAMENTO", "Estorno de Trancamento"),
    JUBILAMENTO("JUBILAMENTO", "Jubilamento"),
    ESTORNO_JUBILAMENTO("ESTORNO_JUBILAMENTO", "Estorno de Jubilamento"),
    CANCELAMENTO("CANCELAMENTO", "Cancelamento"),
    ESTORNO_CANCELAMENTO("ESTORNO_CANCELAMENTO", "Estorno de Jubilamento"),
    TRANSFERENCIA("TRANSFERENCIA", "Transfrência"),
    APROVEITAMENTO_DISCIPLINA("APROVEITAMENTO_DISCIPLINA", "Aproveitamento de Disciplina"),
    EXCLUSAO_DISCIPLINA("EXCLUSAO_DISCIPLINA", "Exclusão de Disciplina"),
    ATUALIZACAO_ALUNO("ATUALIZACAO_ALUNO", "Atualização de Aluno"),
    INCLUSAO_DISCIPLINA("INCLUSAO_DISCIPLINA", "Inclusão de Disciplina"),
	TURMA("TURMA", "Lote Turma"),
	TURMA_2CH("TURMA_2CH", "Lote Segunda Chamada"),
	TURMA_EXAME("TURMA_EXAME", "Lote Exame");

    private String valor;
    private String valorApresentar;

    private OperacaoTempoRealMestreGREnum(String valor, String valorApresentar) {
        this.valor = valor;
        this.valorApresentar = valorApresentar;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

	public String getValorApresentar() {
		return valorApresentar;
	}

	public void setValorApresentar(String valorApresentar) {
		this.valorApresentar = valorApresentar;
	}
    
    

}
