package negocio.comuns.administrativo.enumeradores;

public enum CategoriaEscolaPrivadaEnum {

	PARTICULAR("PT","Particular",1),
	COMUNITARIA("CM","Comunitária",2),
	CONFESSIONAL("CF","Confecional",3),
	FILANTROPICA("FL","Filantrópica",4);
	
	
	private CategoriaEscolaPrivadaEnum(String valor, String descricao, int codigoCenso) {
		this.valor = valor;
		this.descricao = descricao;
		this.codigoCenso = codigoCenso;
	}
	
	
	
	public static Integer getCodigo(String valor) {
		CategoriaEscolaPrivadaEnum[] valores = values();
        for (CategoriaEscolaPrivadaEnum obj : valores) {
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
