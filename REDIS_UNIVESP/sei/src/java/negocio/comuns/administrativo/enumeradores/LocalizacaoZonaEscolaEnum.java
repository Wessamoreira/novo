package negocio.comuns.administrativo.enumeradores;

public enum LocalizacaoZonaEscolaEnum {

	URBANA("UR","Urbana",1),
	RURAL("RU","Rural",2);
	
	
	private LocalizacaoZonaEscolaEnum(String valor, String descricao, int codigoCenso) {
		this.valor = valor;
		this.descricao = descricao;
		this.codigoCenso = codigoCenso;
	}
	
	
	
	public static Integer getCodigo(String valor) {
		LocalizacaoZonaEscolaEnum[] valores = values();
        for (LocalizacaoZonaEscolaEnum obj : valores) {
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
