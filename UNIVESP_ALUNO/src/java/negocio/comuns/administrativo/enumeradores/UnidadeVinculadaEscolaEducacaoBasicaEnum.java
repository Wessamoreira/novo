package negocio.comuns.administrativo.enumeradores;

public enum UnidadeVinculadaEscolaEducacaoBasicaEnum {

	SEM_VINCULO_OUTRA_INSTITUICAO("SV","Sem Vínculo Com Outra Instituição",0),
	UNIDADE_VINCULADA_ESCOLA_EDUCACAO_BASICA("UV","Unidade Vinculada a Escola de Educação Básica",1),
	UNIDADE_OFERTANTE_EDUCACAO_SUPERIOR("UO","Unidade Ofertante de Educação Superior",2);
	
	
	private UnidadeVinculadaEscolaEducacaoBasicaEnum(String valor, String descricao, int codigoCenso) {
		this.valor = valor;
		this.descricao = descricao;
		this.codigoCenso = codigoCenso;
	}
	
	
	
	public static Integer getCodigo(String valor) {
		UnidadeVinculadaEscolaEducacaoBasicaEnum[] valores = values();
        for (UnidadeVinculadaEscolaEducacaoBasicaEnum obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj.getCodigoCenso();
            }
        }
        return null;
    }
	
	String valor;
    String descricao;
    int codigoCenso;
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
	public int getCodigoCenso() {
		return codigoCenso;
	}
	public void setCodigoCenso(int codigoCenso) {
		this.codigoCenso = codigoCenso;
	}
    
    
}
