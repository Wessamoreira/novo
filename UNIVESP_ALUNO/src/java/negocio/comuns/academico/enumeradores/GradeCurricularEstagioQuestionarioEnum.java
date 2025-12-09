package negocio.comuns.academico.enumeradores;

public enum GradeCurricularEstagioQuestionarioEnum {
	
	NENHUM,
	APROVEITAMENTO,
	EQUIVALENCIA;
	
	public boolean isAproveitamento() {
		return name() != null && name().equals(GradeCurricularEstagioQuestionarioEnum.APROVEITAMENTO.name());
	}
	
	public boolean isEquivalencia() {
		return name() != null && name().equals(GradeCurricularEstagioQuestionarioEnum.EQUIVALENCIA.name());
	}
	
	

}
