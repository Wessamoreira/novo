package negocio.comuns.administrativo.enumeradores;

public enum FormaOcupacaoPredioEnum {

	PROPRIO("PR","Próprio",1),
	ALUGADO("AL","Alugado",2),
	CEDIDO("CD","Cedido",3);
	
	String valor;
    String descricao;
    int codigoCenso;
    
    
    public static Integer getCodigo(String valor) {
    	FormaOcupacaoPredioEnum[] valores = values();
        for (FormaOcupacaoPredioEnum obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj.getCodigoCenso();
            }
        }
        return null;
    }
    
    
	private FormaOcupacaoPredioEnum(String valor, String descricao, int codigoCenso) {
		this.valor = valor;
		this.descricao = descricao;
		this.codigoCenso = codigoCenso;
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
	public int getCodigoCenso() {
		return codigoCenso;
	}
	public void setCodigoCenso(int codigoCenso) {
		this.codigoCenso = codigoCenso;
	}
    
    
	
}
