package negocio.comuns.academico.enumeradores;

import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;

public enum ClassificacaoDisciplinaEnum {
  NENHUMA, TCC, ESTAGIO, PROJETO_INTEGRADOR;
  
  public String getValorApresentar() {
	  return UteisJSF.internacionalizar("enum_ClassificacaoDisciplinaEnum_"+this.name());
  }
  
  public boolean isTcc() {
	return Uteis.isAtributoPreenchido(name()) && name().equals(ClassificacaoDisciplinaEnum.TCC.name());
  }
  
  public boolean isEstagio() {
	  return Uteis.isAtributoPreenchido(name()) && name().equals(ClassificacaoDisciplinaEnum.ESTAGIO.name());
  }
  
  public boolean isProjetoIntegrador() {
	  return Uteis.isAtributoPreenchido(name()) && name().equals(ClassificacaoDisciplinaEnum.PROJETO_INTEGRADOR.name());
  }
  
}
