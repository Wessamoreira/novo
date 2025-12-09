package negocio.comuns.financeiro.enumerador;

public enum TipoSacadoExtratoContaCorrenteEnum {

	ALUNO, BANCO, CANDIDATO, FORNECEDOR, FUNCIONARIO_PROFESSOR, RESPONSAVEL_FINANCEIRO, PARCEIRO, REQUERENTE, CONTA_CORRENTE, OPERADORA_CARTAO, MANUAL;

	public boolean isAluno() {
		return name().equals(ALUNO.name());
	}

	public boolean isBanco() {
		return name().equals(BANCO.name());
	}

	public boolean isCandidato() {
		return name().equals(CANDIDATO.name());
	}

	public boolean isFornecedor() {
		return name().equals(FORNECEDOR.name());
	}

	public boolean isFuncionario() {
		return name().equals(FUNCIONARIO_PROFESSOR.name());
	}

	public boolean isParceiro() {
		return name().equals(PARCEIRO.name());
	}

	public boolean isRequerente() {
		return name().equals(REQUERENTE.name());
	}

	public boolean isContaCorrnete() {
		return name().equals(CONTA_CORRENTE.name());
	}

	public boolean isOperadoraCartao() {
		return name().equals(OPERADORA_CARTAO.name());
	}

}
