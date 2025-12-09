package negocio.comuns.administrativo.enumeradores;

import negocio.comuns.utilitarias.Uteis;

/**
 * 
 * @author pedroandrade
 *
 */
public enum TipoGeracaoEmailIntegracaoEnum {
	NOME, MATRICULA;
	
	
	public boolean isNome() {
		return Uteis.isAtributoPreenchido(name()) &&  name().equals(TipoGeracaoEmailIntegracaoEnum.NOME.name());
	}
	
	public boolean isMatricula() {
		return Uteis.isAtributoPreenchido(name()) &&  name().equals(TipoGeracaoEmailIntegracaoEnum.MATRICULA.name());
	}
}
