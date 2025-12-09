package negocio.comuns.biblioteca.enumeradores;

public enum SituacaoReservaEnum {

	CANCELADO("CA", "Cancelado"),	
    EM_EXECUCAO("EX", "Em Execução"),
    DISPONIVEL("DI", "Disponível"),
    EMPRESTADO("EM", "Emprestado"),
    FINALIZADO("FI", "Finalizado");
    
    private String key;
    private String value;
    
    private SituacaoReservaEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }
    
    public String getKey() {
        return key;
    }

    
    public void setKey(String key) {
        this.key = key;
    }
    
    public String getValue() {
        return value;
    }
    
    public void setValue(String value) {
        this.value = value;
    }
    
}
