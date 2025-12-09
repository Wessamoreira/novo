package negocio.comuns.blackboard.enumeradores;


import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;

public enum TipoSalaAulaBlackboardEnum {
	
	NENHUM,
	DISCIPLINA, 
	TCC, 
	TCC_GRUPO, 
	TCC_AMBIENTACAO, 
	ESTAGIO, 
	COMPONENTE_ESTAGIO, 
	PROJETO_INTEGRADOR, 
	PROJETO_INTEGRADOR_AMBIENTACAO,
	PROJETO_INTEGRADOR_GRUPO, 
	IMPORTACAO;
	
	public boolean isDisciplina(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoSalaAulaBlackboardEnum.DISCIPLINA.name()); 
	}
	
	public boolean isTcc(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoSalaAulaBlackboardEnum.TCC.name()); 
	}
	
	public boolean isTccGrupo(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoSalaAulaBlackboardEnum.TCC_GRUPO.name()); 
	}
	
	public boolean isTccAmbientacao(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoSalaAulaBlackboardEnum.TCC_AMBIENTACAO.name()); 
	}
	
	public boolean isEstagio(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoSalaAulaBlackboardEnum.ESTAGIO.name()); 
	}
	
	public boolean isComponenteEstagio(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoSalaAulaBlackboardEnum.COMPONENTE_ESTAGIO.name()); 
	}
	
	public boolean isProjetoIntegrador(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoSalaAulaBlackboardEnum.PROJETO_INTEGRADOR.name()); 
	}
	
	public boolean isProjetoIntegradorAmbientacao() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoSalaAulaBlackboardEnum.PROJETO_INTEGRADOR_AMBIENTACAO.name());
	}
	
	public boolean isProjetoIntegradorGrupo(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoSalaAulaBlackboardEnum.PROJETO_INTEGRADOR_GRUPO.name()); 
	}
	
	public boolean isImportacao(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoSalaAulaBlackboardEnum.IMPORTACAO.name()); 
	}
	
	
	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_TipoSalaAulaBlackboardEnum_"+this.name());
	}
	
}
