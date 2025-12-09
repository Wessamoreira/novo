package negocio.comuns.financeiro.enumerador;

public enum TipoContratoAgenteNegativacaoCobrancaEnum {
	
	NOSSO_NUMERO,
	MATRICULA,
	MATRICULA_PERIODO;
	
	public boolean isNossoNumero() {
    	return name() != null && name().equals(TipoContratoAgenteNegativacaoCobrancaEnum.NOSSO_NUMERO.name());
    }
	
	public boolean isMatricula() {
		return name() != null && name().equals(TipoContratoAgenteNegativacaoCobrancaEnum.MATRICULA.name());
	}
	
	public boolean isMatriculaPeriodo() {
		return name() != null && name().equals(TipoContratoAgenteNegativacaoCobrancaEnum.MATRICULA_PERIODO.name());
	}

}
