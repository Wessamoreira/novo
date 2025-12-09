package negocio.comuns.administrativo.enumeradores;

public enum LocalizacaoDiferenciadaEscolaEnum {

	AREA_ASSENTAMENTO("AA","Área de Assentamento",1),
	TERRA_INDIGENA("TI","Terra Indígena",2),
	AREA_LOCALIZADA_COMUNIDADE_REMANECENTE_QUILOMBOS("AQ","Área Onde se Localiza Comunidade Remanescente de Quilombos",3),
	ESCOLA_NAO_ESTA_AREA_LOCALIZACAO_DIFERENCIADA("ND","A Escola Não Está em Área de Localização Diferenciada",7);
	
	
	private LocalizacaoDiferenciadaEscolaEnum(String valor, String descricao, int codigoCenso) {
		this.valor = valor;
		this.descricao = descricao;
		this.codigoCenso = codigoCenso;
	}
	
	
	
	public static Integer getCodigo(String valor) {
		LocalizacaoDiferenciadaEscolaEnum[] valores = values();
        for (LocalizacaoDiferenciadaEscolaEnum obj : valores) {
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
