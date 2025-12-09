package negocio.comuns.academico.enumeradores;

public enum OrigemCriterioAvaliacaoIndicadorEnum {
	DISCIPLINA, GERAL;
	
	public boolean isDisciplina(){
		return name().equals(OrigemCriterioAvaliacaoIndicadorEnum.DISCIPLINA.name());
	}
	public boolean isGeral(){
		return name().equals(OrigemCriterioAvaliacaoIndicadorEnum.GERAL.name());
	}
}
