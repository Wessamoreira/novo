package negocio.comuns.academico.enumeradores;

public enum GradeCurricularEstagioAreaEnum {
	
	DOCENCIA,
	GESTAO;
	
	public boolean isDocencia() {
		return name() != null && name().equals(GradeCurricularEstagioAreaEnum.DOCENCIA.name());
	}
	
	public boolean isGestao() {
		return name() != null && name().equals(GradeCurricularEstagioAreaEnum.GESTAO.name());
	}

}
