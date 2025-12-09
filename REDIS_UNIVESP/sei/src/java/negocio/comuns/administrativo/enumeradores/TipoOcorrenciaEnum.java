package negocio.comuns.administrativo.enumeradores;

public enum TipoOcorrenciaEnum {

		ACESSO_DADOS("Acesso a Dados");
		
	
		private final String valor;
		
		private TipoOcorrenciaEnum(final String valor) {
			this.valor = valor;
		}

		public String getValor() {
			return valor;
		}
		
		
				
}
