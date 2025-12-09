package negocio.comuns.financeiro.enumerador;

import negocio.comuns.utilitarias.Uteis;

/**
 * 
 * @author PedroOtimize
 *
 */
public enum RestricaoUsoCentroResultadoEnum {
	
	NENHUM,
	APENAS_COORDENADOR_CURSO,
	APENAS_FUNCIONARIO_DEPARTAMENTO,
	APENAS_GESTOR_DEPARTAMENTO,
	USUARIO_PERFIL_ACESSO,
	USUARIO_ESPECIFICOS,
	NUNCA_RECEBER_REGISTRO;
	
	public boolean isNenhuma(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(RestricaoUsoCentroResultadoEnum.NENHUM.name());
	}
	
	public boolean isApenasCoordenadorCurso(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(RestricaoUsoCentroResultadoEnum.APENAS_COORDENADOR_CURSO.name());
	}
	
	public boolean isApenasFuncionarioDepartamento(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(RestricaoUsoCentroResultadoEnum.APENAS_FUNCIONARIO_DEPARTAMENTO.name());
	}
	
	public boolean isApenasGestorDepartamento(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(RestricaoUsoCentroResultadoEnum.APENAS_GESTOR_DEPARTAMENTO.name());
	}
	
	public boolean isUsuarioPerfilAcesso(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(RestricaoUsoCentroResultadoEnum.USUARIO_PERFIL_ACESSO.name());
	}
	
	public boolean isUsuarioEspecificos(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(RestricaoUsoCentroResultadoEnum.USUARIO_ESPECIFICOS.name());
	}
	
	public boolean isNuncaReceberRegistro(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(RestricaoUsoCentroResultadoEnum.NUNCA_RECEBER_REGISTRO.name());
	}
	
		

}
