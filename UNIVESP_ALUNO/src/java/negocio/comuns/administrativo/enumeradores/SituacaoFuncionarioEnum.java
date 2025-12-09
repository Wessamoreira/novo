package negocio.comuns.administrativo.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum SituacaoFuncionarioEnum {

	ATIVO("ATIVO", "Ativo", true),
	DEMITIDO("DEMITIDO", "Demitido", false),
	LICENCA_MATERNIDADE("LICENCA_MATERNIDADE", "Licença Maternidade", false),
	FERIAS("FERIAS", "Férias", false),
	LICENCA_SEM_VENCIMENTO("LICENCA_SEM_VENCIMENTO", "Licença sem Vencimento", false),
	AFASTAMENTO_PREVIDENCIA("AFASTAMENTO_PREVIDENCIA", "Afastamento Prêvidencia", false),
	LICENCA_REMUNERADA("LICENCA_REMUNERADA", "Licença Remunerada", false),
	LICENCA_PATERNIDADE("LICENCA_PATERNIDADE", "Licença Paternidade", false),
	OUTROS("OUTROS", "Outros", false);

	private final String valor;
    private final String descricao;
    private final boolean ativo;

	public static SituacaoFuncionarioEnum getEnumPorValor(String valor) {
		for (SituacaoFuncionarioEnum situacaoFuncionario : SituacaoFuncionarioEnum.values()) {
			
			if(situacaoFuncionario.getValor().equals(valor))
				return situacaoFuncionario;
		}
		
		return null;
	}
    
	public static SituacaoFuncionarioEnum getEnumPorName(String name) {
		for (SituacaoFuncionarioEnum situacaoFuncionario : SituacaoFuncionarioEnum.values()) {
			
			if(situacaoFuncionario.name().equals(name))
				return situacaoFuncionario;
		}
		
		return null;
	}
	
    public String getValorApresentar(){
        return UteisJSF.internacionalizar("enum_SituacaoFuncionarioEnum_"+this.name());
    }
    
	private SituacaoFuncionarioEnum(String valor, String descricao, boolean ativo) {
		this.valor = valor;
		this.descricao = descricao;
		this.ativo = ativo;
	}
	
	public String getValor() {
		return valor;
	}
	
	public String getDescricao() {
		return descricao;
	}

	public boolean getAtivo() {
		return ativo;
	}
}