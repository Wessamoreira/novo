package negocio.comuns.administrativo.enumeradores;

public enum LocalDeFuncionamentoEscolarEnum {

	PREDIO_ESCOLAR("PE","Prédio Escolar"),
	TEMPLO_IGREJA("TI","Templo - Igreja"),
	SALAS_EMPRESA("SE","Salas de Empresa"),
	CASA_PROFESSOR("CP","Casa do Professor"),
	SALAS_OUTRAS_ESCOLAS("SO","Salas em Outras Escolas"),
	GALPAO_RANCHO("GR","Galpão - Rancho - Paiol - Barracão"),
	UNIDADE_ATENDIMENTO_SOCIOEDUCATIVO("US","Unidade de Atendimento Socioeducativo"),
	UNIDADE_PRISIONAL("UP","Unidade Prisional"),
	OUTROS("OU","Outros");
	
	
	String valor;
    String descricao;
    
    
    
	private LocalDeFuncionamentoEscolarEnum(String valor, String descricao) {
		this.valor = valor;
		this.descricao = descricao;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
    
    
}
