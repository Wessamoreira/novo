package negocio.comuns.compras;

import negocio.comuns.utilitarias.Uteis;

public enum TipoDistribuicaoCotacaoEnum {

	GERENTE_DEPARTAMENTO("Gerente do departamento"),
	FUNCIONARIO_DEPARTAMENTO("Funcionário do departamento"),
	FUNCIONARIO_CARGO_DEPARTAMENTO("Funcionário cargo departamento"),
	FUNCIONARIO_ESPECIFICO("Funcionário específico"),
	FUNCIONARIO_TRAMITE("Funcionário trâmite"),
	COORDENADOR_CURSO_ESPECIFICO_TRAMITE("Coordenador curso específico trâmite");

	private String nome;

	private TipoDistribuicaoCotacaoEnum(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	
	public boolean isGerenteDepartamento(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoDistribuicaoCotacaoEnum.GERENTE_DEPARTAMENTO.name());
	}
	
	public boolean isFuncionarioDepartamento(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoDistribuicaoCotacaoEnum.FUNCIONARIO_DEPARTAMENTO.name());
	}
	
	public boolean isFuncionarioCargoDepartamento(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoDistribuicaoCotacaoEnum.FUNCIONARIO_CARGO_DEPARTAMENTO.name());
	}
	
	public boolean isFuncionarioEspecifico(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoDistribuicaoCotacaoEnum.FUNCIONARIO_ESPECIFICO.name());
	}
	
	public boolean isFuncionarioTramite(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoDistribuicaoCotacaoEnum.FUNCIONARIO_TRAMITE.name());
	}
	
	public boolean isCoordenadorCursoEspecificoTramite(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoDistribuicaoCotacaoEnum.COORDENADOR_CURSO_ESPECIFICO_TRAMITE.name());
	}
	
}
