package negocio.comuns.academico.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum ModeloGeracaoSalaBlackboardEnum {
	DISCIPLINA, CURSO_DISCIPLINA, TURMA_DISCIPLINA;
	
	 public String getValorApresentar() {
		  return UteisJSF.internacionalizar("enum_ModeloGeracaoSalaBlackboardEnum_"+this.name());
	  } 
}
