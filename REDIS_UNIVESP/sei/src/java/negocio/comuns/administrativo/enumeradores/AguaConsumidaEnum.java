package negocio.comuns.administrativo.enumeradores;

public enum AguaConsumidaEnum {

	FILTRADA("FR","Filtrada",2),
	NAO_FILTRADA("NF","Não Filtrada",1);
	
	String valor;
    String descricao;
    private int codigoCenso;
    
    
    public static Integer getCodigo(String valor) {
    	AguaConsumidaEnum[] valores = values();
        for (AguaConsumidaEnum obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj.getCodigoCenso();
            }
        }
        return null;
    }
    
    
	private AguaConsumidaEnum(String valor, String descricao, int codigoCenso) {
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
