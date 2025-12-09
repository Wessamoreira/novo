package negocio.comuns.financeiro.enumerador;

import negocio.comuns.utilitarias.UteisJSF;

public enum NivelAgrupamentoContaReceberEnum {
	
	UNIDADE_ENSINO, CURSO, TURMA, ALUNO, RESPONSAVEL_FINANCEIRO;
	
	public String getValorApresentar(){
		return UteisJSF.internacionalizar("enum_NivelAgrupamentoContaReceberEnum_"+this.name());
	}
	
}
