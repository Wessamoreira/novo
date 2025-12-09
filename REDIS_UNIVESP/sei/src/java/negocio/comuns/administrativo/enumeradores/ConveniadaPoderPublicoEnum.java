package negocio.comuns.administrativo.enumeradores;

public enum ConveniadaPoderPublicoEnum {

	ESTADUAL("ES","Estadual",1),
	MUNICIPAL("MU","Municipal",2),
	ESTADUAL_MUNICIPAL("EM","Estadual e Municipal",3);
	
	
	
	public static Integer getCodigo(String valor) {
		ConveniadaPoderPublicoEnum[] valores = values();
        for (ConveniadaPoderPublicoEnum obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj.getCodigoCenso();
            }
        }
        return null;
    }
	
	
	private ConveniadaPoderPublicoEnum(String valor, String descricao, int codigoCenso) {
		this.valor = valor;
		this.descricao = descricao;
		this.codigoCenso = codigoCenso;
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
