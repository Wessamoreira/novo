package negocio.comuns.utilitarias.dominios;

public enum TimeType {

	START_OF_DAY(1,"StartOfDay"),
	END_OF_DAY(2,"EndOfDay"),
	PERSONALIZED_TIME(3,"PersonalizedTime");
	
    private final Integer valor;
    private final String descricao;
 
    TimeType(Integer valor, String descricao) {
    	this.valor = valor;
        this.descricao = descricao;
    }

    public Integer getValor() {
        return valor;
    }

    public String getDescricao() {
        return descricao;
    }
}
