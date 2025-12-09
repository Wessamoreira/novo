package negocio.comuns.academico.enumeradores;


public enum TipoOrigemDisciplinaAproveitadaAlteradaMatriculaEnum {
	APROVEITAMENTO_DISCIPLINA,
	TRANSFERENCIA_ENTRADA,
	DISCIPLINA_FORA_GRADE,
	DISCIPLINA_APROVEITADA_FORA_GRADE,
	HISTORICO;
	
	public static TipoOrigemDisciplinaAproveitadaAlteradaMatriculaEnum getEnum(String desc) {
		if (desc == null) {
			desc = "";
		}
		TipoOrigemDisciplinaAproveitadaAlteradaMatriculaEnum[] valores = values();
		for (TipoOrigemDisciplinaAproveitadaAlteradaMatriculaEnum obj : valores) {
			if (obj.name().equals(desc)) {
				return obj;
			}
		}
		return null;
	}
}
