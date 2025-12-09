package negocio.comuns.academico.enumeradores;

public enum SituacaoMapaRegistroEvasaoCursoMatriculaPeriodoEnum {

	NENHUM("NENHUM", ""),
	AGUARDANDO_PROCESSAMENTO("AGUARDANDO_PROCESSAMENTO", "Aguardando Processamento"),
	ERRO("ERRO", "Erro"), 
	PROCESSADO("PROCESSADO", "Processado"), 
	ESTORNADO("ESTORNADO", "Estornado");

	String valor;
	String descricao;

	SituacaoMapaRegistroEvasaoCursoMatriculaPeriodoEnum(String valor, String descricao) {
		this.valor = valor;
		this.descricao = descricao;
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

	public boolean isAguardandoProcessamento() {
		return name() != null && name().equals(SituacaoMapaRegistroEvasaoCursoMatriculaPeriodoEnum.AGUARDANDO_PROCESSAMENTO.name());
	}

	public boolean isErro() {
		return name() != null && name().equals(SituacaoMapaRegistroEvasaoCursoMatriculaPeriodoEnum.ERRO.name());
	}

	public boolean isProcessado() {
		return name() != null && name().equals(SituacaoMapaRegistroEvasaoCursoMatriculaPeriodoEnum.PROCESSADO.name());
	}

	public boolean isEstornado() {
		return name() != null && name().equals(SituacaoMapaRegistroEvasaoCursoMatriculaPeriodoEnum.ESTORNADO.name());
	}
}
