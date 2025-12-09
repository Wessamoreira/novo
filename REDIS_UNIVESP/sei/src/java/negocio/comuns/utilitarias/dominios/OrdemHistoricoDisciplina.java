package negocio.comuns.utilitarias.dominios;

/**
 * @author Diego
 */
public enum OrdemHistoricoDisciplina {

	ANO_SEMESTRE(2, "Ano/Semestre"),
        DATA_PROGRAMACAO(1, "Data Programação Aula"),
	PERIODO_LETIVO(3, "Período Letivo"), 
	ANO_SEMESTRE_PERIODO_SITUACAO(4, "Ano/Semestre/Período/Situação"),
	ANO_SEMESTRE_SITUACAO_PERIODO(5, "Ano/Semestre/Situação/Período"),
        MATRICULA_COM_HISTORICO_ALUNO(6, "Código Discplina/Ano/Semestre");

	int valor;
	String descricao;

	OrdemHistoricoDisciplina(int valor, String descricao) {
		this.valor = valor;
		this.descricao = descricao;
	}

	public static OrdemHistoricoDisciplina getEnum(int valor) {
		OrdemHistoricoDisciplina[] valores = values();
		for (OrdemHistoricoDisciplina obj : valores) {
			if (obj.getValor() == valor) {
				return obj;
			}
		}
		return null;
	}

	public static String getDescricao(int valor) {
		OrdemHistoricoDisciplina obj = getEnum(valor);
		if (obj != null) {
			return obj.getDescricao();
		}
		return "";
	}

	public int getValor() {
		return valor;
	}

	public void setValor(int valor) {
		this.valor = valor;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
