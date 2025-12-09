package negocio.comuns.administrativo.enumeradores;

/**
 * 
 * @author Pedro
 */
public enum TipoOuvidoriaEnum {	
	RECLAMACAO("Reclamação"), 
	DUVIDA("Dúvida"), 
	SUGESTAO("Sugestão"), 
	ELOGIO("Elogio"), 
	OUTROS("Outros");
	
	private String valor;
	
	private TipoOuvidoriaEnum(String valor){
		this.valor = valor;
	}

	public String getValor() {
		if (valor == null) {
			valor = "";
		}
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}
	
}
