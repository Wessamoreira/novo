package negocio.comuns.academico.enumeradores;

public enum TipoCalculoCargaHorariaFrequencia {
	CARGA_HORARIA_DISCIPLINA("Carga Horária da Disciplina"),
	HORA_AULA_DISCIPLINA("Hora Aula da Disciplina"),
	CARGA_HORARIA_REGISTRO_AULA("Carga Horária do Registro de Aula");
	
	private final String descricao;

	TipoCalculoCargaHorariaFrequencia(String descricao) {
        this.descricao = descricao;
    }
	
	public String getDescricao() {
        return descricao;
    }
}
