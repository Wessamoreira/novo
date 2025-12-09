package negocio.comuns.arquitetura.enumeradores;

public enum TipoFiltroEnum {
	
	IGUAL_DATA("igual a"),
	DIFERENTE_DATA("diferente de"),
	A_PARTIR_DATA("a partir"), 
	ATE_DATA("até"), 
	ENTRE_DATAS("entre o período"),
	NAO_ENTRE_DATAS("fora do período"),
	
	IGUAL_TEXTO("igual a"),
	DIFERENTE_TEXTO("diferente de"),
	INICIANDO_COM_TEXTO("iniciando com"), 
	TERMINANDO_COM_TEXTO("terminando com"), 
	CONTENDO_TEXTO("contendo"), 
	
	IGUAL_NUMERO("igual a"), 
	DIFERENTE_NUMERO("diferente de"),
	MAIOR_IGUAL_NUMERO("maior ou igual a"), 
	MENOR_IGUAL_NUMERO("menor ou igual a"), 
	MAIOR_NUMERO("maior que"), 
	MENOR_NUMERO("menor que"), 
	CONTENDO_NUMEROS("contendo os números"), 
	NAO_CONTENDO_NUMEROS("não contendo os números"),

	VERDADEIRO("é verdadeiro"),
	FALSO("é falso"), 
	
	NULO("é nulo"),
	NAO_NULO("não é nulo");
	
	private String texto;
	
    private TipoFiltroEnum(String texto) {
    	this.texto = texto;
    }

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

}
