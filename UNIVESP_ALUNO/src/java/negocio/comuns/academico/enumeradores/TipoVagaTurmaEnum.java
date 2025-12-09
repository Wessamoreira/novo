package negocio.comuns.academico.enumeradores;

public enum TipoVagaTurmaEnum {
	NORMAL("nrvagas", "nrvagasmatricula"), REPOSICAO("nrVagasInclusaoReposicao","nrVagasMatriculaReposicao"), ENRIQUECIMENTO("nrVagasEnriquecimento", "nrVagasEnriquecimento"),EXTENSAO("nrVagasExtensao", "nrVagasExtensao");
	
	private String campoVagaTurmaDisciplina;
	private String campoVagaTurma;

	private TipoVagaTurmaEnum(String campoVagaTurma, String campoVagaTurmaDisciplina) {
		this.campoVagaTurma = campoVagaTurma;
		this.campoVagaTurmaDisciplina = campoVagaTurmaDisciplina;
	}

	public String getCampoVagaTurmaDisciplina() {
		return campoVagaTurmaDisciplina;
	}

	public void setCampoVagaTurmaDisciplina(String campoVagaTurmaDisciplina) {
		this.campoVagaTurmaDisciplina = campoVagaTurmaDisciplina;
	}

	public String getCampoVagaTurma() {
		return campoVagaTurma;
	}

	public void setCampoVagaTurma(String campoVagaTurma) {
		this.campoVagaTurma = campoVagaTurma;
	}

	public boolean isNormal() {
		return name() != null && name().equals(TipoVagaTurmaEnum.NORMAL.name());
	}
	public boolean isReposicao() {
		return name() != null && name().equals(TipoVagaTurmaEnum.REPOSICAO.name());
	}
	public boolean isEnriquecimento() {
		return name() != null && name().equals(TipoVagaTurmaEnum.ENRIQUECIMENTO.name());
	}
	public boolean isExtencao() {
		return name() != null && name().equals(TipoVagaTurmaEnum.EXTENSAO.name());
	}
	

	
}
