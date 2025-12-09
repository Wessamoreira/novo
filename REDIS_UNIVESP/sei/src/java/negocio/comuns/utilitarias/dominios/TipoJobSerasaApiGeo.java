package negocio.comuns.utilitarias.dominios;

public enum TipoJobSerasaApiGeo {

	REGISTRARSERASAAPIGEO("REGISTRARSERASAAPIGEO"),
	REMOVERSERASAAPIGEO("REMOVERSERASAAPIGEO");
	
	String valor;

    TipoJobSerasaApiGeo(String valor) {
        this.valor = valor;
    }

	public String getValor() {
		return valor;
	}
	
}
